package com.br.hermescomercial.controller.pdv;

import com.br.hermescomercial.business.pdv.CupomFiscalManager;
import com.br.hermescomercial.business.pdv.ImpressoraManager;
// import com.br.hermescomercial.model.Cliente; - não utilizado
// import com.br.hermescomercial.model.ItemVenda; - não utilizado
import com.br.hermescomercial.model.Usuario;
// import com.br.hermescomercial.model.Pagamento; - não utilizado
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.stage.Stage;

import java.net.URL;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.LocalDate;
import java.util.ResourceBundle;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class PDVCupomFiscalController implements Initializable {

    // Managers
    private CupomFiscalManager cupomManager;
    private ImpressoraManager impressoraManager;
    
    // Dados
    private Usuario operadorAtual;
    private ObservableList<CupomFiscal> cuponsDisponiveis;
    private CupomFiscal cupomSelecionado;
    
    // Timer para atualização de data/hora
    private Timer timer;
    private DateTimeFormatter formatadorDataHora = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");

    // Componentes FXML - Header
    @FXML private Label lblDataHora;
    @FXML private Label lblOperador;
    
    // Botões Principais
    @FXML private Button btnNovoCupom;
    @FXML private Button btnBuscarCupom;
    @FXML private Button btnImprimir;
    @FXML private Button btnCancelar;
    @FXML private Button btnEstornar;
    @FXML private Button btnVoltar;
    
    // Área de Visualização
    @FXML private ScrollPane scrollPaneCupom;
    @FXML private TextArea txtAreaCupom;
    
    // Informações do Cupom
    @FXML private Label lblNumeroCupom;
    @FXML private Label lblDataEmissao;
    @FXML private Label lblCliente;
    @FXML private Label lblValorTotal;
    @FXML private Label lblFormaPagamento;
    @FXML private Label lblStatus;
    
    // Filtros de Busca
    @FXML private DatePicker dtDataInicio;
    @FXML private DatePicker dtDataFim;
    @FXML private TextField txtNumeroCupom;
    @FXML private TextField txtCliente;
    @FXML private Button btnBuscarFiltros;
    @FXML private Button btnLimparFiltros;
    
    // Tabela de Cupons
    @FXML private TableView<CupomFiscal> tblCupons;
    @FXML private TableColumn<CupomFiscal, String> colNumero;
    @FXML private TableColumn<CupomFiscal, String> colData;
    @FXML private TableColumn<CupomFiscal, String> colCliente;
    @FXML private TableColumn<CupomFiscal, BigDecimal> colValor;
    @FXML private TableColumn<CupomFiscal, String> colStatus;
    @FXML private TableColumn<CupomFiscal, String> colAcoes;
    
    // Resumo
    @FXML private Label lblTotalCupons;
    @FXML private Label lblValorTotalGeral;
    
    // Status
    @FXML private Label lblStatusOperacao;
    @FXML private Label lblEmpresa;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        inicializarManagers();
        inicializarComponentes();
        inicializarListeners();
        inicializarTabelas();
        inicializarTimer();
        carregarDadosIniciais();
    }

    private void inicializarManagers() {
        // Inicializar managers usando Singleton
        this.cupomManager = CupomFiscalManager.getInstance();
        this.impressoraManager = ImpressoraManager.getInstance();
        
        // Configurar modo debug para impressora
        impressoraManager.setModoDebug(true);
    }

    private void inicializarComponentes() {
        // Inicializar listas observáveis
        this.cuponsDisponiveis = FXCollections.observableArrayList();
        
        // Configurar informações do operador (simulado - viria do login)
        this.operadorAtual = new Usuario();
        operadorAtual.setNome("João Silva");
        operadorAtual.setId(1L);
        lblOperador.setText("Operador: " + operadorAtual.getNome());
        
        // Configurar informações da empresa
        lblEmpresa.setText(cupomManager.getNomeEmpresa());
        
        // Configurar datas padrão (últimos 7 dias)
        LocalDate hoje = LocalDate.now();
        dtDataFim.setValue(hoje);
        dtDataInicio.setValue(hoje.minusDays(7));
        
        // Configurar status inicial
        atualizarStatus("Pronto para operar");
    }

    private void inicializarListeners() {
        // Listener para busca por número do cupom
        txtNumeroCupom.textProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue != null && !newValue.trim().isEmpty()) {
                buscarCupomPorNumero(newValue);
            } else {
                carregarCuponsPadrao();
            }
        });
        
        // Listener para busca por cliente
        txtCliente.textProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue != null && newValue.length() >= 3) {
                buscarCuponsPorCliente(newValue);
            }
        });
        
        // Listener para seleção na tabela de cupons
        tblCupons.getSelectionModel().selectedItemProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue != null) {
                selecionarCupom(newValue);
            }
        });
        
        // Listener para atualização da lista de cupons
        cuponsDisponiveis.addListener((javafx.collections.ListChangeListener<CupomFiscal>) change -> {
            atualizarResumo();
            atualizarBotoesAcao();
        });
    }

    private void inicializarTabelas() {
        // Configurar tabela de cupons
        colNumero.setCellValueFactory(new PropertyValueFactory<>("numero"));
        colData.setCellValueFactory(new PropertyValueFactory<>("dataEmissaoFormatada"));
        colCliente.setCellValueFactory(new PropertyValueFactory<>("nomeCliente"));
        colValor.setCellValueFactory(new PropertyValueFactory<>("valorTotal"));
        colStatus.setCellValueFactory(new PropertyValueFactory<>("status"));
        colAcoes.setCellValueFactory(new PropertyValueFactory<>("acoes"));
        
        tblCupons.setItems(cuponsDisponiveis);
    }

    private void inicializarTimer() {
        timer = new Timer(true);
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                javafx.application.Platform.runLater(() -> {
                    lblDataHora.setText(LocalDateTime.now().format(formatadorDataHora));
                });
            }
        }, 0, 1000);
    }

    private void carregarDadosIniciais() {
        carregarCuponsPadrao();
        atualizarResumo();
        atualizarBotoesAcao();
    }

    // Métodos de Ação dos Botões
    @FXML
    private void onNovoCupom() {
        limparCampos();
        cupomSelecionado = null;
        atualizarStatus("Novo cupom fiscal iniciado");
    }

    @FXML
    private void onBuscarCupom() {
        String numero = txtNumeroCupom.getText();
        if (numero == null || numero.trim().isEmpty()) {
            mostrarAlerta("Digite o número do cupom para buscar", Alert.AlertType.WARNING);
            txtNumeroCupom.requestFocus();
            return;
        }
        buscarCupomPorNumero(numero);
    }

    @FXML
    private void onImprimir() {
        if (cupomSelecionado == null) {
            mostrarAlerta("Selecione um cupom para imprimir", Alert.AlertType.WARNING);
            return;
        }
        
        try {
            String conteudoCupom = txtAreaCupom.getText();
            if (conteudoCupom == null || conteudoCupom.trim().isEmpty()) {
                mostrarAlerta("Não há conteúdo para imprimir", Alert.AlertType.WARNING);
                return;
            }
            
            boolean sucesso = impressoraManager.imprimirCupom(conteudoCupom);
            if (sucesso) {
                atualizarStatus("Cupom impresso com sucesso");
                mostrarAlerta("Cupom impresso com sucesso", Alert.AlertType.INFORMATION);
            } else {
                atualizarStatus("Erro ao imprimir cupom");
                mostrarAlerta("Erro ao imprimir cupom", Alert.AlertType.ERROR);
            }
        } catch (Exception e) {
            atualizarStatus("Erro ao imprimir: " + e.getMessage());
            mostrarAlerta("Erro ao imprimir: " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    @FXML
    private void onCancelar() {
        if (cupomSelecionado == null) {
            mostrarAlerta("Selecione um cupom para cancelar", Alert.AlertType.WARNING);
            return;
        }
        
        if ("CANCELADO".equals(cupomSelecionado.getStatus())) {
            mostrarAlerta("Este cupom já está cancelado", Alert.AlertType.WARNING);
            return;
        }
        
        if (confirmarCancelamento()) {
            try {
                cupomSelecionado.cancelar();
                String motivo = "Cancelamento via PDV";
                String cupomCancelamento = cupomManager.gerarCupomCancelamento(
                    cupomSelecionado.getNumero(), 
                    cupomSelecionado.getValorTotal(), 
                    motivo, 
                    operadorAtual
                );
                
                atualizarInterfaceCupom();
                atualizarTabelaCupons();
                atualizarStatus("Cupom cancelado com sucesso");
                mostrarAlerta("Cupom cancelado com sucesso", Alert.AlertType.INFORMATION);
                
                // Mostrar cupom de cancelamento
                mostrarCupomCancelamento(cupomCancelamento);
                
            } catch (Exception e) {
                atualizarStatus("Erro ao cancelar cupom: " + e.getMessage());
                mostrarAlerta("Erro ao cancelar cupom: " + e.getMessage(), Alert.AlertType.ERROR);
            }
        }
    }

    @FXML
    private void onEstornar() {
        if (cupomSelecionado == null) {
            mostrarAlerta("Selecione um cupom para estornar", Alert.AlertType.WARNING);
            return;
        }
        
        if ("CANCELADO".equals(cupomSelecionado.getStatus())) {
            mostrarAlerta("Não é possível estornar um cupom cancelado", Alert.AlertType.WARNING);
            return;
        }
        
        if (confirmarEstorno()) {
            try {
                // Simular estorno
                cupomSelecionado.estornar();
                
                atualizarInterfaceCupom();
                atualizarTabelaCupons();
                atualizarStatus("Cupom estornado com sucesso");
                mostrarAlerta("Cupom estornado com sucesso", Alert.AlertType.INFORMATION);
                
            } catch (Exception e) {
                atualizarStatus("Erro ao estornar cupom: " + e.getMessage());
                mostrarAlerta("Erro ao estornar cupom: " + e.getMessage(), Alert.AlertType.ERROR);
            }
        }
    }

    @FXML
    private void onVoltar() {
        fecharTela();
    }

    @FXML
    private void onBuscarComFiltros() {
        buscarCuponsComFiltros();
    }

    @FXML
    private void onLimparFiltros() {
        limparFiltros();
        carregarCuponsPadrao();
    }

    // Métodos de Negócio
    private void buscarCupomPorNumero(String numero) {
        try {
            // Simular busca por número
            CupomFiscal cupom = simularBuscaCupomPorNumero(numero);
            
            if (cupom != null) {
                cuponsDisponiveis.clear();
                cuponsDisponiveis.add(cupom);
                tblCupons.getSelectionModel().select(cupom);
                atualizarStatus("Cupom encontrado: " + numero);
            } else {
                cuponsDisponiveis.clear();
                limparCampos();
                atualizarStatus("Nenhum cupom encontrado para: " + numero);
            }
            
        } catch (Exception e) {
            atualizarStatus("Erro ao buscar cupom: " + e.getMessage());
        }
    }

    private void buscarCuponsPorCliente(String nomeCliente) {
        try {
            // Simular busca por cliente
            List<CupomFiscal> cupons = simularBuscaCuponsPorCliente(nomeCliente);
            
            cuponsDisponiveis.clear();
            cuponsDisponiveis.addAll(cupons);
            
            atualizarStatus(cupons.size() + " cupons encontrados para: " + nomeCliente);
            
        } catch (Exception e) {
            atualizarStatus("Erro ao buscar cupons por cliente: " + e.getMessage());
        }
    }

    private void buscarCuponsComFiltros() {
        try {
            LocalDate dataInicio = dtDataInicio.getValue();
            LocalDate dataFim = dtDataFim.getValue();
            String numero = txtNumeroCupom.getText();
            String cliente = txtCliente.getText();
            
            // Simular busca com filtros
            List<CupomFiscal> cupons = simularBuscaComFiltros(dataInicio, dataFim, numero, cliente);
            
            cuponsDisponiveis.clear();
            cuponsDisponiveis.addAll(cupons);
            
            atualizarStatus(cupons.size() + " cupons encontrados com os filtros");
            
        } catch (Exception e) {
            atualizarStatus("Erro ao buscar com filtros: " + e.getMessage());
        }
    }

    private void carregarCuponsPadrao() {
        cuponsDisponiveis.clear();
        
        // Simular cupons dos últimos dias
        for (int i = 1; i <= 10; i++) {
            CupomFiscal cupom = simularCupom(i);
            cuponsDisponiveis.add(cupom);
        }
    }

    private void selecionarCupom(CupomFiscal cupom) {
        this.cupomSelecionado = cupom;
        atualizarInterfaceCupom();
        atualizarStatus("Cupom selecionado: " + cupom.getNumero());
    }

    private void atualizarInterfaceCupom() {
        if (cupomSelecionado != null) {
            lblNumeroCupom.setText(cupomSelecionado.getNumero());
            lblDataEmissao.setText(cupomSelecionado.getDataEmissaoFormatada());
            lblCliente.setText(cupomSelecionado.getNomeCliente());
            lblValorTotal.setText(formatarMoeda(cupomSelecionado.getValorTotal()));
            lblFormaPagamento.setText(cupomSelecionado.getFormaPagamento());
            lblStatus.setText(cupomSelecionado.getStatus());
            
            // Atualizar cor do status
            switch (cupomSelecionado.getStatus()) {
                case "ATIVO":
                    lblStatus.setStyle("-fx-font-weight: bold; -fx-text-fill: #28a745;");
                    break;
                case "CANCELADO":
                    lblStatus.setStyle("-fx-font-weight: bold; -fx-text-fill: #e74c3c;");
                    break;
                case "ESTORNADO":
                    lblStatus.setStyle("-fx-font-weight: bold; -fx-text-fill: #f39c12;");
                    break;
                default:
                    lblStatus.setStyle("-fx-font-weight: bold; -fx-text-fill: #666;");
            }
            
            // Gerar visualização do cupom
            String cupomFormatado = gerarVisualizacaoCupom(cupomSelecionado);
            txtAreaCupom.setText(cupomFormatado);
            
        } else {
            limparCampos();
        }
    }

    private void atualizarTabelaCupons() {
        // Força atualização da tabela
        tblCupons.refresh();
    }

    private void atualizarResumo() {
        int totalCupons = cuponsDisponiveis.size();
        BigDecimal valorTotalGeral = cuponsDisponiveis.stream()
                .map(CupomFiscal::getValorTotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        
        lblTotalCupons.setText(String.valueOf(totalCupons));
        lblValorTotalGeral.setText(formatarMoeda(valorTotalGeral));
    }

    private void atualizarBotoesAcao() {
        boolean temCupomSelecionado = cupomSelecionado != null;
        // boolean temCupons = !cuponsDisponiveis.isEmpty(); - não utilizado
        
        btnImprimir.setDisable(!temCupomSelecionado);
        btnCancelar.setDisable(!temCupomSelecionado);
        btnEstornar.setDisable(!temCupomSelecionado);
        
        // Desabilitar cancelar e estornar se já estiver cancelado
        if (temCupomSelecionado && "CANCELADO".equals(cupomSelecionado.getStatus())) {
            btnCancelar.setDisable(true);
            btnEstornar.setDisable(true);
        }
    }

    private void limparCampos() {
        lblNumeroCupom.setText("000001");
        lblDataEmissao.setText("01/01/2024 12:00:00");
        lblCliente.setText("Não informado");
        lblValorTotal.setText("R$ 0,00");
        lblFormaPagamento.setText("DINHEIRO");
        lblStatus.setText("ATIVO");
        lblStatus.setStyle("-fx-font-weight: bold; -fx-text-fill: #28a745;");
        txtAreaCupom.clear();
    }

    private void limparFiltros() {
        txtNumeroCupom.clear();
        txtCliente.clear();
        
        // Resetar datas para padrão
        LocalDate hoje = LocalDate.now();
        dtDataFim.setValue(hoje);
        dtDataInicio.setValue(hoje.minusDays(7));
    }

    private void atualizarStatus(String mensagem) {
        lblStatusOperacao.setText(mensagem);
        
        // Atualizar cor do status baseado na mensagem
        if (mensagem.toLowerCase().contains("erro")) {
            lblStatusOperacao.setStyle("-fx-text-fill: #e74c3c; -fx-font-weight: bold;");
        } else if (mensagem.toLowerCase().contains("sucesso") || mensagem.toLowerCase().contains("pronto")) {
            lblStatusOperacao.setStyle("-fx-text-fill: #28a745; -fx-font-weight: bold;");
        } else {
            lblStatusOperacao.setStyle("-fx-text-fill: #f39c12; -fx-font-weight: bold;");
        }
    }

    // Métodos de Utilidade
    private String formatarMoeda(BigDecimal valor) {
        if (valor == null) return "R$ 0,00";
        return String.format("R$ %,.2f", valor).replace('.', ',');
    }

    private void mostrarAlerta(String mensagem, Alert.AlertType tipo) {
        Alert alert = new Alert(tipo);
        alert.setTitle("Cupom Fiscal");
        alert.setHeaderText(null);
        alert.setContentText(mensagem);
        alert.showAndWait();
    }

    private boolean confirmarCancelamento() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmar Cancelamento");
        alert.setHeaderText("Deseja realmente cancelar este cupom?");
        alert.setContentText("Esta ação não poderá ser desfeita.");
        
        return alert.showAndWait().get() == ButtonType.OK;
    }

    private boolean confirmarEstorno() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmar Estorno");
        alert.setHeaderText("Deseja realmente estornar este cupom?");
        alert.setContentText("Esta ação gerará um crédito para o cliente.");
        
        return alert.showAndWait().get() == ButtonType.OK;
    }

    private void fecharTela() {
        Stage stage = (Stage) btnVoltar.getScene().getWindow();
        stage.close();
    }

    private void mostrarCupomCancelamento(String cupomCancelamento) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Cupom de Cancelamento");
        alert.setHeaderText("Cupom de Cancelamento Gerado");
        
        TextArea textArea = new TextArea(cupomCancelamento);
        textArea.setEditable(false);
        textArea.setWrapText(true);
        textArea.setStyle("-fx-font-family: 'Courier New', monospace; -fx-font-size: 10px;");
        
        alert.getDialogPane().setContent(textArea);
        alert.setResizable(true);
        alert.showAndWait();
    }

    // Métodos de Simulação (em produção, viriam do banco de dados)
    private CupomFiscal simularCupom(int numero) {
        CupomFiscal cupom = new CupomFiscal();
        cupom.setNumero(String.format("%06d", numero));
        cupom.setDataEmissao(LocalDateTime.now().minusDays(numero % 7));
        cupom.setNomeCliente(numero % 3 == 0 ? "Cliente " + numero : "Não informado");
        cupom.setValorTotal(new BigDecimal(10.50 * numero));
        cupom.setFormaPagamento(numero % 2 == 0 ? "DINHEIRO" : "CARTAO_CREDITO");
        cupom.setStatus("ATIVO");
        return cupom;
    }

    private CupomFiscal simularBuscaCupomPorNumero(String numero) {
        // Simular busca específica
        if ("000001".equals(numero)) {
            CupomFiscal cupom = new CupomFiscal();
            cupom.setNumero("000001");
            cupom.setDataEmissao(LocalDateTime.now().minusHours(2));
            cupom.setNomeCliente("João Silva");
            cupom.setValorTotal(new BigDecimal("150.75"));
            cupom.setFormaPagamento("DINHEIRO");
            cupom.setStatus("ATIVO");
            return cupom;
        }
        return null;
    }

    private List<CupomFiscal> simularBuscaCuponsPorCliente(String nomeCliente) {
        List<CupomFiscal> cupons = new java.util.ArrayList<>();
        for (int i = 1; i <= 5; i++) {
            CupomFiscal cupom = simularCupom(i);
            cupom.setNomeCliente(nomeCliente);
            cupons.add(cupom);
        }
        return cupons;
    }

    private List<CupomFiscal> simularBuscaComFiltros(LocalDate dataInicio, LocalDate dataFim, String numero, String cliente) {
        List<CupomFiscal> cupons = new java.util.ArrayList<>();
        for (int i = 1; i <= 5; i++) {
            CupomFiscal cupom = simularCupom(i);
            if (numero != null && !numero.trim().isEmpty()) {
                cupom.setNumero(numero);
            }
            if (cliente != null && !cliente.trim().isEmpty()) {
                cupom.setNomeCliente(cliente);
            }
            cupons.add(cupom);
        }
        return cupons;
    }

    private String gerarVisualizacaoCupom(CupomFiscal cupom) {
        StringBuilder sb = new StringBuilder();
        sb.append(cupomManager.getNomeEmpresa()).append("\n");
        sb.append("CNPJ: ").append(cupomManager.getCnpjEmpresa()).append("\n");
        sb.append("================================\n");
        sb.append("CUPOM FISCAL ELETRÔNICO\n");
        sb.append("Nº: ").append(cupom.getNumero()).append("\n");
        sb.append("Data: ").append(cupom.getDataEmissaoFormatada()).append("\n");
        sb.append("Terminal: ").append(cupomManager.getNumeroTerminal()).append("\n");
        sb.append("================================\n");
        
        if (cupom.getNomeCliente() != null && !cupom.getNomeCliente().equals("Não informado")) {
            sb.append("DADOS DO CLIENTE\n");
            sb.append("Nome: ").append(cupom.getNomeCliente()).append("\n");
            sb.append("--------------------------------\n");
        }
        
        sb.append("ITEM  CODIGO  DESCRIÇÃO                     QTD   UN   VL.UNIT   VL.TOTAL\n");
        sb.append("--------------------------------\n");
        sb.append("01    0001    PRODUTO EXEMPLO                1    UN     10,50      10,50\n");
        sb.append("02    0002    OUTRO PRODUTO                  2    UN     25,00      50,00\n");
        sb.append("--------------------------------\n");
        sb.append("SUBTOTAL:                                   R$ 60,50\n");
        sb.append("TOTAL:                                      R$ 60,50\n");
        sb.append("================================\n");
        sb.append("FORMA PAGAMENTO: ").append(cupom.getFormaPagamento()).append("\n");
        sb.append("VALOR PAGO: R$ ").append(formatarMoeda(cupom.getValorTotal()).replace("R$ ", "")).append("\n");
        sb.append("================================\n");
        sb.append("Operador: ").append(operadorAtual.getNome()).append("\n");
        sb.append("Obrigado pela preferência!\n");
        sb.append("Volte sempre!\n");
        sb.append("================================\n");
        sb.append("CUPOM NÃO FISCAL - SEM VALOR FISCAL\n");
        
        return sb.toString();
    }

    // Classe interna para representar o Cupom Fiscal
    public static class CupomFiscal {
        private String numero;
        private LocalDateTime dataEmissao;
        private String nomeCliente;
        private BigDecimal valorTotal;
        private String formaPagamento;
        private String status;
        
        public String getNumero() { return numero; }
        public void setNumero(String numero) { this.numero = numero; }
        
        public LocalDateTime getDataEmissao() { return dataEmissao; }
        public void setDataEmissao(LocalDateTime dataEmissao) { this.dataEmissao = dataEmissao; }
        
        public String getDataEmissaoFormatada() {
            if (dataEmissao == null) return "";
            return dataEmissao.format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss"));
        }
        
        public String getNomeCliente() { return nomeCliente; }
        public void setNomeCliente(String nomeCliente) { this.nomeCliente = nomeCliente; }
        
        public BigDecimal getValorTotal() { return valorTotal; }
        public void setValorTotal(BigDecimal valorTotal) { this.valorTotal = valorTotal; }
        
        public String getFormaPagamento() { return formaPagamento; }
        public void setFormaPagamento(String formaPagamento) { this.formaPagamento = formaPagamento; }
        
        public String getStatus() { return status; }
        public void setStatus(String status) { this.status = status; }
        
        public void cancelar() {
            this.status = "CANCELADO";
        }
        
        public void estornar() {
            this.status = "ESTORNADO";
        }
        
        public String getAcoes() {
            return "Visualizar | Imprimir";
        }
    }
}
