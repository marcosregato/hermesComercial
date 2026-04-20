package com.br.hermescomercial.controller.pdv;

import com.br.hermescomercial.business.pdv.PDVManager;
import com.br.hermescomercial.business.pdv.PagamentoManager;
import com.br.hermescomercial.business.pdv.CupomFiscalManager;
import com.br.hermescomercial.business.pdv.ImpressoraManager;
import com.br.hermescomercial.model.CarrinhoCompras;
import com.br.hermescomercial.model.ItemVenda;
import com.br.hermescomercial.model.Pagamento;
import com.br.hermescomercial.model.Produto;
import com.br.hermescomercial.model.VendaPDV;
import com.br.hermescomercial.model.Usuario;
// import com.br.hermescomercial.model.Cliente; - não utilizado
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.net.URL;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;
// import java.util.List; - não utilizado
import java.util.Timer;
import java.util.TimerTask;

public class PDVPagamentoController implements Initializable {

    // Managers
    private PDVManager pdvManager;
    private PagamentoManager pagamentoManager;
    private CupomFiscalManager cupomManager;
    private ImpressoraManager impressoraManager;
    
    // Dados da Venda
    private CarrinhoCompras carrinhoAtual;
    private Usuario operadorAtual;
    private Pagamento pagamentoProcessado;
    private VendaPDV vendaFinalizada;
    
    // Timer para atualização de data/hora
    private Timer timer;
    private DateTimeFormatter formatadorDataHora = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");

    // Componentes FXML - Header
    @FXML private Label lblDataHora;
    @FXML private Button btnVoltar;
    
    // Resumo da Venda
    @FXML private Label lblCliente;
    @FXML private TableView<ItemVenda> tblItensVenda;
    @FXML private TableColumn<ItemVenda, String> colItemDescricao;
    @FXML private TableColumn<ItemVenda, Integer> colItemQtd;
    @FXML private TableColumn<ItemVenda, BigDecimal> colItemValor;
    @FXML private TableColumn<ItemVenda, BigDecimal> colItemTotal;
    @FXML private Label lblSubtotal;
    @FXML private Label lblDesconto;
    @FXML private Label lblTotal;
    
    // Formas de Pagamento
    @FXML private ComboBox<String> comboFormaPagamento;
    @FXML private Label lblFormaPagamentoInfo;
    
    // Pagamento em Dinheiro
    @FXML private VBox paneDinheiro;
    @FXML private TextField txtValorRecebido;
    @FXML private Button btn5, btn10, btn20, btn50, btn100, btn200;
    @FXML private Button btnExato, btnLimparValor;
    @FXML private Label lblTroco;
    
    // Pagamento Cartão
    @FXML private VBox paneCartao;
    @FXML private ComboBox<String> comboBandeira;
    @FXML private ComboBox<Integer> comboParcelas;
    @FXML private TextField txtNumeroCartao;
    @FXML private TextField txtNomeCartao;
    @FXML private TextField txtValidade;
    @FXML private TextField txtCVV;
    
    // Pagamento PIX
    @FXML private VBox panePIX;
    @FXML private Label lblChavePIX;
    @FXML private Button btnConfirmarPIX;
    
    // Pagamento Transferência
    @FXML private VBox paneTransferencia;
    @FXML private Label lblBanco;
    @FXML private Label lblAgencia;
    @FXML private Label lblConta;
    @FXML private Label lblCNPJ;
    @FXML private Button btnConfirmarTransferencia;
    
    // Múltiplos Pagamentos
    @FXML private VBox paneMultiplos;
    @FXML private TableView<Pagamento> tblPagamentos;
    @FXML private TableColumn<Pagamento, String> colPagamentoForma;
    @FXML private TableColumn<Pagamento, BigDecimal> colPagamentoValor;
    @FXML private TableColumn<Pagamento, String> colPagamentoStatus;
    @FXML private TableColumn<Pagamento, String> colPagamentoAcoes;
    @FXML private ComboBox<String> comboFormaAdicional;
    @FXML private TextField txtValorAdicional;
    @FXML private Button btnAdicionarPagamento;
    @FXML private Label lblTotalPago;
    @FXML private Label lblRestante;
    
    // Botões de Ação
    @FXML private Button btnCancelar;
    @FXML private Button btnProcessar;
    @FXML private Button btnFinalizar;
    
    // Status
    @FXML private Label lblStatus;
    @FXML private Label lblOperador;
    @FXML private Label lblTerminal;
    
    // Listas observáveis
    private ObservableList<ItemVenda> itensVenda;
    private ObservableList<Pagamento> pagamentos;

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
        this.pdvManager = PDVManager.getInstance();
        this.pagamentoManager = PagamentoManager.getInstance();
        this.cupomManager = CupomFiscalManager.getInstance();
        this.impressoraManager = ImpressoraManager.getInstance();
        
        // Configurar modo debug para impressora
        impressoraManager.setModoDebug(true);
    }

    private void inicializarComponentes() {
        // Inicializar listas observáveis
        this.itensVenda = FXCollections.observableArrayList();
        this.pagamentos = FXCollections.observableArrayList();
        
        // Configurar informações do terminal
        lblTerminal.setText("Terminal: " + pdvManager.getNumeroTerminal());
        
        // Configurar combo boxes
        comboFormaPagamento.setItems(FXCollections.observableArrayList(
            "DINHEIRO", "CARTAO_DEBITO", "CARTAO_CREDITO", "PIX", "TRANSFERENCIA", "MULTIPLOS"
        ));
        
        comboBandeira.setItems(FXCollections.observableArrayList(
            "VISA", "MASTERCARD", "ELO", "HIPERCARD", "AMEX", "DINERS"
        ));
        
        comboParcelas.setItems(FXCollections.observableArrayList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12));
        
        comboFormaAdicional.setItems(FXCollections.observableArrayList(
            "DINHEIRO", "CARTAO_DEBITO", "CARTAO_CREDITO", "PIX", "TRANSFERENCIA"
        ));
        
        // Configurar informações da empresa
        lblBanco.setText("Banco do Brasil - 001");
        lblAgencia.setText("1234-5");
        lblConta.setText("12345-6");
        lblCNPJ.setText("12.345.678/0001-95");
        lblChavePIX.setText("chave@pix.com.br");
        
        // Esconder painéis de pagamento
        esconderTodosPaineisPagamento();
        
        // Configurar status inicial
        atualizarStatus("Aguardando forma de pagamento...");
    }

    private void inicializarListeners() {
        // Listener para seleção de forma de pagamento
        comboFormaPagamento.getSelectionModel().selectedItemProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue != null) {
                mostrarPainelPagamento(newValue);
                atualizarInformacaoFormaPagamento(newValue);
            }
        });
        
        // Listener para valor recebido (dinheiro)
        txtValorRecebido.textProperty().addListener((obs, oldValue, newValue) -> {
            calcularTroco();
        });
        
        // Listener para valor adicional (múltiplos pagamentos)
        txtValorAdicional.textProperty().addListener((obs, oldValue, newValue) -> {
            atualizarTotaisMultiplos();
        });
        
        // Listener para seleção na tabela de pagamentos
        tblPagamentos.getSelectionModel().selectedItemProperty().addListener((obs, oldValue, newValue) -> {
            // Implementar ações para seleção de pagamento
        });
    }

    private void inicializarTabelas() {
        // Configurar tabela de itens da venda
        colItemDescricao.setCellValueFactory(cellData -> {
            Produto produto = cellData.getValue().getProduto();
            return javafx.beans.binding.Bindings.createStringBinding(() -> 
                produto != null ? produto.getNome() : "");
        });
        colItemQtd.setCellValueFactory(new PropertyValueFactory<>("quantidade"));
        colItemValor.setCellValueFactory(new PropertyValueFactory<>("valorUnitario"));
        colItemTotal.setCellValueFactory(new PropertyValueFactory<>("valorFinal"));
        
        tblItensVenda.setItems(itensVenda);
        
        // Configurar tabela de pagamentos
        colPagamentoForma.setCellValueFactory(new PropertyValueFactory<>("tipoPagamento"));
        colPagamentoValor.setCellValueFactory(new PropertyValueFactory<>("valorPago"));
        colPagamentoStatus.setCellValueFactory(new PropertyValueFactory<>("status"));
        colPagamentoAcoes.setCellValueFactory(new PropertyValueFactory<>("acoes"));
        
        tblPagamentos.setItems(pagamentos);
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
        // Dados serão carregados quando o controller receber os dados da venda
        atualizarBotoesAcao();
    }

    // Métodos para receber dados da venda
    public void setDadosVenda(CarrinhoCompras carrinho, Usuario operador) {
        this.carrinhoAtual = carrinho;
        this.operadorAtual = operador;
        
        // Atualizar interface
        atualizarResumoVenda();
        atualizarInformacoesOperador();
        
        // Carregar itens na tabela
        if (carrinho != null && carrinho.getItens() != null) {
            itensVenda.clear();
            itensVenda.addAll(carrinho.getItens());
        }
    }

    private void atualizarResumoVenda() {
        if (carrinhoAtual != null) {
            // Cliente
            if (carrinhoAtual.getCliente() != null) {
                lblCliente.setText(carrinhoAtual.getCliente().getNome());
            } else {
                lblCliente.setText("Não informado");
            }
            
            // Totais
            BigDecimal subtotal = carrinhoAtual.getValorTotal();
            BigDecimal desconto = BigDecimal.ZERO; // Implementar quando tiver método
            BigDecimal total = carrinhoAtual.getValorFinal();
            
            lblSubtotal.setText(formatarMoeda(subtotal));
            lblDesconto.setText(formatarMoeda(desconto));
            lblTotal.setText(formatarMoeda(total));
        }
    }

    private void atualizarInformacoesOperador() {
        if (operadorAtual != null) {
            lblOperador.setText("Operador: " + operadorAtual.getNome());
        }
    }

    // Métodos de Ação dos Botões
    @FXML
    private void onVoltar() {
        if (confirmarCancelamento()) {
            fecharTela();
        }
    }

    @FXML
    private void onCancelar() {
        if (confirmarCancelamentoVenda()) {
            // Cancelar venda no PDVManager
            if (pdvManager.cancelarVenda()) {
                mostrarAlerta("Venda cancelada com sucesso", Alert.AlertType.INFORMATION);
                fecharTela();
            } else {
                mostrarAlerta("Erro ao cancelar venda", Alert.AlertType.ERROR);
            }
        }
    }

    @FXML
    private void onProcessar() {
        String formaPagamento = comboFormaPagamento.getSelectionModel().getSelectedItem();
        
        if (formaPagamento == null) {
            mostrarAlerta("Selecione uma forma de pagamento", Alert.AlertType.WARNING);
            return;
        }
        
        if (carrinhoAtual == null || carrinhoAtual.estaVazio()) {
            mostrarAlerta("Carrinho vazio", Alert.AlertType.WARNING);
            return;
        }
        
        try {
            BigDecimal valorTotal = carrinhoAtual.getValorFinal();
            
            switch (formaPagamento) {
                case "DINHEIRO":
                    processarPagamentoDinheiro(valorTotal);
                    break;
                case "CARTAO_DEBITO":
                    processarPagamentoCartaoDebito(valorTotal);
                    break;
                case "CARTAO_CREDITO":
                    processarPagamentoCartaoCredito(valorTotal);
                    break;
                case "PIX":
                    processarPagamentoPIX(valorTotal);
                    break;
                case "TRANSFERENCIA":
                    processarPagamentoTransferencia(valorTotal);
                    break;
                case "MULTIPLOS":
                    processarPagamentoMultiplo(valorTotal);
                    break;
            }
            
        } catch (Exception e) {
            atualizarStatus("Erro ao processar pagamento: " + e.getMessage());
        }
    }

    @FXML
    private void onFinalizar() {
        if (pagamentoProcessado == null) {
            mostrarAlerta("Processe o pagamento primeiro", Alert.AlertType.WARNING);
            return;
        }
        
        try {
            // Finalizar venda
            vendaFinalizada = pdvManager.finalizarVenda(pagamentoProcessado);
            
            if (vendaFinalizada != null) {
                // Gerar cupom fiscal
                String cupom = cupomManager.gerarCupomFiscal(
                    vendaFinalizada.getItens(),
                    vendaFinalizada.getPagamento(),
                    vendaFinalizada.getCliente(),
                    vendaFinalizada.getOperador(),
                    vendaFinalizada.getNumeroCupom()
                );
                
                // Imprimir cupom
                if (impressoraManager.imprimirCupom(cupom)) {
                    mostrarAlerta("Venda finalizada com sucesso!\nCupom impresso.", Alert.AlertType.INFORMATION);
                } else {
                    mostrarAlerta("Venda finalizada, mas erro ao imprimir cupom", Alert.AlertType.WARNING);
                }
                
                atualizarStatus("Venda finalizada com sucesso");
                btnFinalizar.setDisable(true);
                btnProcessar.setDisable(true);
                
                // Fechar tela após alguns segundos
                fecharTelaAposDelay();
                
            } else {
                mostrarAlerta("Erro ao finalizar venda", Alert.AlertType.ERROR);
            }
            
        } catch (Exception e) {
            atualizarStatus("Erro ao finalizar venda: " + e.getMessage());
        }
    }

    // Métodos de Pagamento Dinheiro
    @FXML
    private void onValorRapido(javafx.event.ActionEvent event) {
        Button botao = (Button) event.getSource();
        String texto = botao.getText().replace("R$ ", "").replace(",", ".");
        
        try {
            BigDecimal valor = new BigDecimal(texto);
            BigDecimal valorAtual = txtValorRecebido.getText() != null && !txtValorRecebido.getText().isEmpty() 
                ? new BigDecimal(txtValorRecebido.getText().replace(",", "."))
                : BigDecimal.ZERO;
            
            BigDecimal novoValor = valorAtual.add(valor);
            txtValorRecebido.setText(formatarMoedaSemSimbolo(novoValor));
            calcularTroco();
            
        } catch (Exception e) {
            atualizarStatus("Erro ao adicionar valor: " + e.getMessage());
        }
    }

    @FXML
    private void onValorExato() {
        if (carrinhoAtual != null) {
            BigDecimal valorTotal = carrinhoAtual.getValorFinal();
            txtValorRecebido.setText(formatarMoedaSemSimbolo(valorTotal));
            calcularTroco();
        }
    }

    @FXML
    private void onLimparValor() {
        txtValorRecebido.clear();
        lblTroco.setText("R$ 0,00");
    }

    // Métodos de Pagamento PIX
    @FXML
    private void onConfirmarPIX() {
        if (carrinhoAtual != null) {
            BigDecimal valorTotal = carrinhoAtual.getValorFinal();
            pagamentoProcessado = pagamentoManager.processarPagamentoPix(valorTotal, lblChavePIX.getText());
            
            if (pagamentoProcessado != null) {
                atualizarStatus("Pagamento PIX confirmado");
                habilitarFinalizacao();
            } else {
                mostrarAlerta("Erro ao processar pagamento PIX", Alert.AlertType.ERROR);
            }
        }
    }

    // Métodos de Pagamento Transferência
    @FXML
    private void onConfirmarTransferencia() {
        if (carrinhoAtual != null) {
            BigDecimal valorTotal = carrinhoAtual.getValorFinal();
            pagamentoProcessado = pagamentoManager.processarPagamentoUnico("TRANSFERENCIA", valorTotal, valorTotal);
            
            if (pagamentoProcessado != null) {
                atualizarStatus("Transferência confirmada");
                habilitarFinalizacao();
            } else {
                mostrarAlerta("Erro ao processar transferência", Alert.AlertType.ERROR);
            }
        }
    }

    // Métodos de Múltiplos Pagamentos
    @FXML
    private void onAdicionarPagamento() {
        String forma = comboFormaAdicional.getSelectionModel().getSelectedItem();
        String valorTexto = txtValorAdicional.getText();
        
        if (forma == null || valorTexto == null || valorTexto.trim().isEmpty()) {
            mostrarAlerta("Selecione a forma e informe o valor", Alert.AlertType.WARNING);
            return;
        }
        
        try {
            BigDecimal valor = new BigDecimal(valorTexto.replace(",", "."));
            
            if (valor.compareTo(BigDecimal.ZERO) <= 0) {
                mostrarAlerta("Valor deve ser maior que zero", Alert.AlertType.WARNING);
                return;
            }
            
            // Criar pagamento
            Pagamento pagamento = new Pagamento();
            pagamento.setTipoPagamento(forma);
            pagamento.setValorPago(valor);
            pagamento.setDataPagamento(LocalDateTime.now());
            pagamento.aprovar();
            
            // Adicionar à lista
            pagamentos.add(pagamento);
            
            // Limpar campos
            comboFormaAdicional.getSelectionModel().clearSelection();
            txtValorAdicional.clear();
            
            atualizarTotaisMultiplos();
            atualizarStatus("Pagamento adicionado: " + forma + " - " + formatarMoeda(valor));
            
        } catch (Exception e) {
            mostrarAlerta("Valor inválido", Alert.AlertType.ERROR);
        }
    }

    // Métodos de Negócio
    private void processarPagamentoDinheiro(BigDecimal valorTotal) {
        String valorTexto = txtValorRecebido.getText();
        
        if (valorTexto == null || valorTexto.trim().isEmpty()) {
            mostrarAlerta("Informe o valor recebido", Alert.AlertType.WARNING);
            return;
        }
        
        try {
            BigDecimal valorRecebido = new BigDecimal(valorTexto.replace(",", "."));
            
            if (valorRecebido.compareTo(valorTotal) < 0) {
                mostrarAlerta("Valor recebido é insuficiente", Alert.AlertType.WARNING);
                return;
            }
            
            pagamentoProcessado = pagamentoManager.processarPagamentoUnico("DINHEIRO", valorRecebido, valorTotal);
            
            if (pagamentoProcessado != null) {
                atualizarStatus("Pagamento em dinheiro processado");
                habilitarFinalizacao();
            } else {
                mostrarAlerta("Erro ao processar pagamento", Alert.AlertType.ERROR);
            }
            
        } catch (Exception e) {
            mostrarAlerta("Valor inválido", Alert.AlertType.ERROR);
        }
    }

    private void processarPagamentoCartaoDebito(BigDecimal valorTotal) {
        if (!validarDadosCartao()) {
            return;
        }
        
        pagamentoProcessado = pagamentoManager.processarPagamentoUnico("CARTAO_DEBITO", valorTotal, valorTotal);
        
        if (pagamentoProcessado != null) {
            atualizarStatus("Pagamento com cartão de débito processado");
            habilitarFinalizacao();
        } else {
            mostrarAlerta("Erro ao processar pagamento", Alert.AlertType.ERROR);
        }
    }

    private void processarPagamentoCartaoCredito(BigDecimal valorTotal) {
        if (!validarDadosCartao()) {
            return;
        }
        
        Integer parcelas = comboParcelas.getSelectionModel().getSelectedItem();
        String bandeira = comboBandeira.getSelectionModel().getSelectedItem();
        
        if (parcelas == null || bandeira == null) {
            mostrarAlerta("Selecione a bandeira e o número de parcelas", Alert.AlertType.WARNING);
            return;
        }
        
        pagamentoProcessado = pagamentoManager.processarPagamentoParcelado(valorTotal, parcelas, bandeira);
        
        if (pagamentoProcessado != null) {
            atualizarStatus("Pagamento com cartão de crédito processado");
            habilitarFinalizacao();
        } else {
            mostrarAlerta("Erro ao processar pagamento", Alert.AlertType.ERROR);
        }
    }

    private void processarPagamentoPIX(BigDecimal valorTotal) {
        // PIX é processado no botão de confirmação
        atualizarStatus("Confirme o pagamento PIX");
    }

    private void processarPagamentoTransferencia(BigDecimal valorTotal) {
        // Transferência é processada no botão de confirmação
        atualizarStatus("Confirme a transferência bancária");
    }

    private void processarPagamentoMultiplo(BigDecimal valorTotal) {
        BigDecimal totalPago = calcularTotalPago();
        
        if (totalPago.compareTo(valorTotal) < 0) {
            mostrarAlerta("Valor pago é insuficiente", Alert.AlertType.WARNING);
            return;
        }
        
        if (pagamentoManager.processarMultiplosPagamentos(pagamentos, valorTotal)) {
            // Criar pagamento consolidado
            pagamentoProcessado = new Pagamento();
            pagamentoProcessado.setTipoPagamento("MULTIPLOS");
            pagamentoProcessado.setValorPago(totalPago);
            pagamentoProcessado.setDataPagamento(LocalDateTime.now());
            pagamentoProcessado.aprovar();
            
            atualizarStatus("Múltiplos pagamentos processados");
            habilitarFinalizacao();
        } else {
            mostrarAlerta("Erro ao processar múltiplos pagamentos", Alert.AlertType.ERROR);
        }
    }

    // Métodos Utilitários
    private void mostrarPainelPagamento(String formaPagamento) {
        esconderTodosPaineisPagamento();
        
        switch (formaPagamento) {
            case "DINHEIRO":
                paneDinheiro.setVisible(true);
                break;
            case "CARTAO_DEBITO":
            case "CARTAO_CREDITO":
                paneCartao.setVisible(true);
                break;
            case "PIX":
                panePIX.setVisible(true);
                break;
            case "TRANSFERENCIA":
                paneTransferencia.setVisible(true);
                break;
            case "MULTIPLOS":
                paneMultiplos.setVisible(true);
                break;
        }
    }

    private void esconderTodosPaineisPagamento() {
        paneDinheiro.setVisible(false);
        paneCartao.setVisible(false);
        panePIX.setVisible(false);
        paneTransferencia.setVisible(false);
        paneMultiplos.setVisible(false);
    }

    private void atualizarInformacaoFormaPagamento(String formaPagamento) {
        String info = "";
        
        switch (formaPagamento) {
            case "DINHEIRO":
                info = "Informe o valor recebido e o sistema calculará o troco";
                break;
            case "CARTAO_DEBITO":
                info = "Passe o cartão na máquina";
                break;
            case "CARTAO_CREDITO":
                info = "Selecione a bandeira e o número de parcelas";
                break;
            case "PIX":
                info = "Escaneie o QR Code ou utilize a chave PIX";
                break;
            case "TRANSFERENCIA":
                info = "Faça a transferência para os dados informados";
                break;
            case "MULTIPLOS":
                info = "Adicione múltiplas formas de pagamento";
                break;
        }
        
        lblFormaPagamentoInfo.setText(info);
    }

    private void calcularTroco() {
        if (carrinhoAtual != null && txtValorRecebido.getText() != null && !txtValorRecebido.getText().isEmpty()) {
            try {
                BigDecimal valorTotal = carrinhoAtual.getValorFinal();
                BigDecimal valorRecebido = new BigDecimal(txtValorRecebido.getText().replace(",", "."));
                
                if (valorRecebido.compareTo(valorTotal) >= 0) {
                    BigDecimal troco = valorRecebido.subtract(valorTotal);
                    lblTroco.setText(formatarMoeda(troco));
                } else {
                    lblTroco.setText("R$ 0,00");
                }
            } catch (Exception e) {
                lblTroco.setText("R$ 0,00");
            }
        } else {
            lblTroco.setText("R$ 0,00");
        }
    }

    private BigDecimal calcularTotalPago() {
        BigDecimal total = BigDecimal.ZERO;
        
        for (Pagamento pagamento : pagamentos) {
            total = total.add(pagamento.getValorPago());
        }
        
        return total;
    }

    private void atualizarTotaisMultiplos() {
        if (carrinhoAtual != null) {
            BigDecimal valorTotal = carrinhoAtual.getValorFinal();
            BigDecimal totalPago = calcularTotalPago();
            BigDecimal restante = valorTotal.subtract(totalPago);
            
            lblTotalPago.setText(formatarMoeda(totalPago));
            lblRestante.setText(formatarMoeda(restante));
            
            // Atualizar cor do restante
            if (restante.compareTo(BigDecimal.ZERO) > 0) {
                lblRestante.setStyle("-fx-text-fill: #e74c3c; -fx-font-weight: bold;");
            } else {
                lblRestante.setStyle("-fx-text-fill: #27ae60; -fx-font-weight: bold;");
            }
        }
    }

    private boolean validarDadosCartao() {
        String numero = txtNumeroCartao.getText();
        String nome = txtNomeCartao.getText();
        String validade = txtValidade.getText();
        String cvv = txtCVV.getText();
        
        if (numero == null || numero.trim().isEmpty() || numero.length() < 16) {
            mostrarAlerta("Número do cartão inválido", Alert.AlertType.WARNING);
            return false;
        }
        
        if (nome == null || nome.trim().isEmpty()) {
            mostrarAlerta("Nome no cartão é obrigatório", Alert.AlertType.WARNING);
            return false;
        }
        
        if (validade == null || validade.trim().isEmpty() || !validade.matches("\\d{2}/\\d{2}")) {
            mostrarAlerta("Validade inválida (use MM/AA)", Alert.AlertType.WARNING);
            return false;
        }
        
        if (cvv == null || cvv.trim().isEmpty() || cvv.length() < 3) {
            mostrarAlerta("CVV inválido", Alert.AlertType.WARNING);
            return false;
        }
        
        return true;
    }

    private void habilitarFinalizacao() {
        btnFinalizar.setDisable(false);
        btnProcessar.setDisable(true);
        comboFormaPagamento.setDisable(true);
    }

    private void atualizarBotoesAcao() {
        boolean temCarrinho = carrinhoAtual != null && !carrinhoAtual.estaVazio();
        
        btnProcessar.setDisable(!temCarrinho);
        btnFinalizar.setDisable(true);
    }

    private void atualizarStatus(String mensagem) {
        lblStatus.setText(mensagem);
        
        // Atualizar cor do status
        if (mensagem.toLowerCase().contains("erro")) {
            lblStatus.setStyle("-fx-text-fill: #e74c3c; -fx-font-weight: bold;");
        } else if (mensagem.toLowerCase().contains("processado") || mensagem.toLowerCase().contains("confirmado")) {
            lblStatus.setStyle("-fx-text-fill: #28a745; -fx-font-weight: bold;");
        } else {
            lblStatus.setStyle("-fx-text-fill: #f39c12; -fx-font-weight: bold;");
        }
    }

    // Métodos Utilitários
    private String formatarMoeda(BigDecimal valor) {
        if (valor == null) return "R$ 0,00";
        return String.format("R$ %,.2f", valor).replace('.', ',');
    }

    private String formatarMoedaSemSimbolo(BigDecimal valor) {
        if (valor == null) return "0,00";
        return String.format("%,.2f", valor).replace('.', ',');
    }

    private void mostrarAlerta(String mensagem, Alert.AlertType tipo) {
        Alert alert = new Alert(tipo);
        alert.setTitle("Pagamento PDV");
        alert.setHeaderText(null);
        alert.setContentText(mensagem);
        alert.showAndWait();
    }

    private boolean confirmarCancelamento() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmar Cancelamento");
        alert.setHeaderText("Deseja voltar para a tela principal?");
        alert.setContentText("O processo de pagamento será cancelado.");
        
        return alert.showAndWait().get() == ButtonType.OK;
    }

    private boolean confirmarCancelamentoVenda() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmar Cancelamento");
        alert.setHeaderText("Deseja realmente cancelar esta venda?");
        alert.setContentText("Todos os itens serão removidos do carrinho.");
        
        return alert.showAndWait().get() == ButtonType.OK;
    }

    private void fecharTela() {
        // Fechar a janela atual
        javafx.stage.Stage stage = (javafx.stage.Stage) lblStatus.getScene().getWindow();
        stage.close();
    }

    private void fecharTelaAposDelay() {
        // Fechar a tela após 3 segundos
        javafx.application.Platform.runLater(() -> {
            try {
                Thread.sleep(3000);
                javafx.application.Platform.runLater(this::fecharTela);
            } catch (InterruptedException e) {
                // Ignorar
            }
        });
    }

    // Getters e Setters
    public CarrinhoCompras getCarrinhoAtual() {
        return carrinhoAtual;
    }

    public void setCarrinhoAtual(CarrinhoCompras carrinhoAtual) {
        this.carrinhoAtual = carrinhoAtual;
    }

    public Usuario getOperadorAtual() {
        return operadorAtual;
    }

    public void setOperadorAtual(Usuario operadorAtual) {
        this.operadorAtual = operadorAtual;
    }
}
