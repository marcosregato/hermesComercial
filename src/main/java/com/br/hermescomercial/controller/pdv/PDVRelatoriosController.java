package com.br.hermescomercial.controller.pdv;

import com.br.hermescomercial.model.VendaPDV;
import com.br.hermescomercial.model.ItemVenda;
import com.br.hermescomercial.model.Usuario;
// import com.br.hermescomercial.dao.VendaDao; - não utilizado
import com.br.hermescomercial.service.UsuarioServiceBasico;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.net.URL;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;
import java.util.List;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;
// import java.util.Map; - não utilizado
// import java.util.HashMap; - não utilizado

public class PDVRelatoriosController implements Initializable {

    // Services
    // private VendaDao vendaDao; - não utilizado
    private UsuarioServiceBasico usuarioService;
    
    // Dados
    private ObservableList<VendaPDV> vendasDetalhadas;
    private ObservableList<ItemVenda> produtosMaisVendidos;
    private ObservableList<RelatorioPagamento> formasPagamento;
    private ObservableList<RelatorioHora> vendasPorHora;
    private ObservableList<RelatorioOperador> desempenhoOperadores;
    
    // Timer para atualização de data/hora
    private Timer timer;
    private DateTimeFormatter formatadorDataHora = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");

    // Componentes FXML - Header
    @FXML private Label lblDataHora;
    @FXML private Button btnVoltar;
    
    // Filtros do Relatório
    @FXML private ComboBox<String> comboTipoRelatorio;
    @FXML private DatePicker dpDataInicial;
    @FXML private DatePicker dpDataFinal;
    @FXML private ComboBox<String> comboOperador;
    @FXML private ComboBox<String> comboTerminal;
    @FXML private ComboBox<String> comboFormaPagamento;
    @FXML private CheckBox chkAgruparPorDia;
    @FXML private CheckBox chkAgruparPorOperador;
    @FXML private CheckBox chkAgruparPorFormaPagamento;
    @FXML private CheckBox chkMostrarCancelados;
    @FXML private Button btnGerarRelatorio;
    @FXML private Button btnLimparFiltros;
    @FXML private Button btnExportarPDF;
    @FXML private Button btnExportarExcel;
    @FXML private Button btnImprimir;
    
    // Resumo do Relatório
    @FXML private Label lblTotalVendas;
    @FXML private Label lblValorTotal;
    @FXML private Label lblTicketMedio;
    @FXML private Label lblVendasCanceladas;
    
    // Tabelas de Resultados
    @FXML private TabPane tabPaneResultados;
    
    // Tab Vendas Detalhadas
    @FXML private TableView<VendaPDV> tblVendasDetalhadas;
    @FXML private TableColumn<VendaPDV, String> colVendaData;
    @FXML private TableColumn<VendaPDV, String> colVendaNumero;
    @FXML private TableColumn<VendaPDV, String> colVendaOperador;
    @FXML private TableColumn<VendaPDV, String> colVendaCliente;
    @FXML private TableColumn<VendaPDV, BigDecimal> colVendaValor;
    @FXML private TableColumn<VendaPDV, String> colVendaForma;
    @FXML private TableColumn<VendaPDV, String> colVendaStatus;
    @FXML private TableColumn<VendaPDV, String> colVendaAcoes;
    
    // Tab Produtos Mais Vendidos
    @FXML private TableView<ItemVenda> tblProdutosMaisVendidos;
    @FXML private TableColumn<ItemVenda, String> colProdutoNome;
    @FXML private TableColumn<ItemVenda, Integer> colProdutoQuantidade;
    @FXML private TableColumn<ItemVenda, BigDecimal> colProdutoValor;
    @FXML private TableColumn<ItemVenda, String> colProdutoPercentual;
    
    // Tab Formas de Pagamento
    @FXML private TableView<RelatorioPagamento> tblFormasPagamento;
    @FXML private TableColumn<RelatorioPagamento, String> colFormaNome;
    @FXML private TableColumn<RelatorioPagamento, Integer> colFormaQuantidade;
    @FXML private TableColumn<RelatorioPagamento, BigDecimal> colFormaValor;
    @FXML private TableColumn<RelatorioPagamento, String> colFormaPercentual;
    
    // Tab Vendas por Hora
    @FXML private TableView<RelatorioHora> tblVendasPorHora;
    @FXML private TableColumn<RelatorioHora, String> colHoraPeriodo;
    @FXML private TableColumn<RelatorioHora, Integer> colHoraQuantidade;
    @FXML private TableColumn<RelatorioHora, BigDecimal> colHoraValor;
    @FXML private TableColumn<RelatorioHora, String> colHoraPercentual;
    
    // Tab Desempenho Operadores
    @FXML private TableView<RelatorioOperador> tblDesempenhoOperadores;
    @FXML private TableColumn<RelatorioOperador, String> colOperadorNome;
    @FXML private TableColumn<RelatorioOperador, Integer> colOperadorVendas;
    @FXML private TableColumn<RelatorioOperador, BigDecimal> colOperadorValor;
    @FXML private TableColumn<RelatorioOperador, BigDecimal> colOperadorTicket;
    @FXML private TableColumn<RelatorioOperador, Integer> colOperadorCanceladas;
    
    // Status
    @FXML private Label lblStatus;
    @FXML private Label lblOperador;
    @FXML private Label lblTerminal;

    // Classes auxiliares para relatórios
    public static class RelatorioPagamento {
        private String forma;
        private int quantidade;
        private BigDecimal valor;
        private String percentual;

        // Getters e Setters
        public String getForma() { return forma; }
        public void setForma(String forma) { this.forma = forma; }
        public int getQuantidade() { return quantidade; }
        public void setQuantidade(int quantidade) { this.quantidade = quantidade; }
        public BigDecimal getValor() { return valor; }
        public void setValor(BigDecimal valor) { this.valor = valor; }
        public String getPercentual() { return percentual; }
        public void setPercentual(String percentual) { this.percentual = percentual; }
    }

    public static class RelatorioHora {
        private String periodo;
        private int quantidade;
        private BigDecimal valor;
        private String percentual;

        // Getters e Setters
        public String getPeriodo() { return periodo; }
        public void setPeriodo(String periodo) { this.periodo = periodo; }
        public int getQuantidade() { return quantidade; }
        public void setQuantidade(int quantidade) { this.quantidade = quantidade; }
        public BigDecimal getValor() { return valor; }
        public void setValor(BigDecimal valor) { this.valor = valor; }
        public String getPercentual() { return percentual; }
        public void setPercentual(String percentual) { this.percentual = percentual; }
    }

    public static class RelatorioOperador {
        private String nome;
        private int vendas;
        private BigDecimal valor;
        private BigDecimal ticketMedio;
        private int canceladas;

        // Getters e Setters
        public String getNome() { return nome; }
        public void setNome(String nome) { this.nome = nome; }
        public int getVendas() { return vendas; }
        public void setVendas(int vendas) { this.vendas = vendas; }
        public BigDecimal getValor() { return valor; }
        public void setValor(BigDecimal valor) { this.valor = valor; }
        public BigDecimal getTicketMedio() { return ticketMedio; }
        public void setTicketMedio(BigDecimal ticketMedio) { this.ticketMedio = ticketMedio; }
        public int getCanceladas() { return canceladas; }
        public void setCanceladas(int canceladas) { this.canceladas = canceladas; }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        inicializarComponentes();
        inicializarListeners();
        inicializarTabelas();
        inicializarTimer();
        carregarDadosIniciais();
    }

    private void inicializarComponentes() {
        inicializarDAOs();
        inicializarListasObservaveis();
        configurarComboBoxes();
        configurarSelecoesPadrao();
        configurarDatasPadrao();
        configurarInformacoesTerminal();
        configurarStatusInicial();
        configurarBotoesExportacao();
    }
    
    private void inicializarDAOs() {
        // this.vendaDao = new VendaDao(); - não utilizado
        this.usuarioService = new UsuarioServiceBasico();
    }
    
    private void inicializarListasObservaveis() {
        this.vendasDetalhadas = FXCollections.observableArrayList();
        this.produtosMaisVendidos = FXCollections.observableArrayList();
        this.formasPagamento = FXCollections.observableArrayList();
        this.vendasPorHora = FXCollections.observableArrayList();
        this.desempenhoOperadores = FXCollections.observableArrayList();
    }
    
    private void configurarComboBoxes() {
        configurarComboTipoRelatorio();
        configurarComboOperador();
        configurarComboTerminal();
        configurarComboFormaPagamento();
    }
    
    private void configurarComboTipoRelatorio() {
        comboTipoRelatorio.setItems(FXCollections.observableArrayList(
            "VENDAS_DETALHADAS", "PRODUTOS_MAIS_VENDIDOS", "FORMAS_PAGAMENTO", 
            "VENDAS_POR_HORA", "DESEMPENHO_OPERADORES", "RESUMO_COMPLETO"
        ));
        comboTipoRelatorio.getSelectionModel().selectFirst();
    }
    
    private void configurarComboOperador() {
        comboOperador.setItems(FXCollections.observableArrayList("Todos"));
    }
    
    private void configurarComboTerminal() {
        comboTerminal.setItems(FXCollections.observableArrayList("Todos", "001", "002", "003"));
    }
    
    private void configurarComboFormaPagamento() {
        comboFormaPagamento.setItems(FXCollections.observableArrayList(
            "Todas", "DINHEIRO", "CARTAO_DEBITO", "CARTAO_CREDITO", "PIX", "TRANSFERENCIA"
        ));
    }
    
    private void configurarSelecoesPadrao() {
        comboOperador.getSelectionModel().selectFirst();
        comboTerminal.getSelectionModel().selectFirst();
        comboFormaPagamento.getSelectionModel().selectFirst();
    }
    
    private void configurarDatasPadrao() {
        LocalDate hoje = LocalDate.now();
        dpDataFinal.setValue(hoje);
        dpDataInicial.setValue(hoje.minusDays(7));
    }
    
    private void configurarInformacoesTerminal() {
        lblTerminal.setText("Terminal: 001");
        lblOperador.setText("Operador: João");
    }
    
    private void configurarStatusInicial() {
        atualizarStatus("Selecione os filtros e gere o relatório");
    }
    
    private void configurarBotoesExportacao() {
        btnExportarPDF.setDisable(true);
        btnExportarExcel.setDisable(true);
        btnImprimir.setDisable(true);
    }

    private void inicializarListeners() {
        // Listener para tipo de relatório
        comboTipoRelatorio.getSelectionModel().selectedItemProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue != null) {
                configurarFiltrosParaTipoRelatorio(newValue);
            }
        });
        
        // Listener para seleção na tabela de vendas
        tblVendasDetalhadas.getSelectionModel().selectedItemProperty().addListener((obs, oldValue, newValue) -> {
            // Implementar ações para seleção de venda
        });
    }

    private void inicializarTabelas() {
        // Configurar tabela de vendas detalhadas
        colVendaData.setCellValueFactory(cellData -> {
            VendaPDV venda = cellData.getValue();
            return javafx.beans.binding.Bindings.createStringBinding(() -> 
                venda.getDataVenda() != null ? venda.getDataVenda().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")) : "");
        });
        colVendaNumero.setCellValueFactory(new PropertyValueFactory<>("numeroCupom"));
        colVendaOperador.setCellValueFactory(cellData -> {
            VendaPDV venda = cellData.getValue();
            return javafx.beans.binding.Bindings.createStringBinding(() -> 
                venda.getOperador() != null ? venda.getOperador().getNome() : "");
        });
        colVendaCliente.setCellValueFactory(cellData -> {
            VendaPDV venda = cellData.getValue();
            return javafx.beans.binding.Bindings.createStringBinding(() -> 
                venda.getCliente() != null ? venda.getCliente().getNome() : "Não informado");
        });
        colVendaValor.setCellValueFactory(new PropertyValueFactory<>("valorTotal"));
        colVendaForma.setCellValueFactory(cellData -> {
            VendaPDV venda = cellData.getValue();
            return javafx.beans.binding.Bindings.createStringBinding(() -> 
                venda.getPagamento() != null ? venda.getPagamento().getTipoPagamento() : "");
        });
        colVendaStatus.setCellValueFactory(new PropertyValueFactory<>("status"));
        colVendaAcoes.setCellValueFactory(new PropertyValueFactory<>("acoes"));
        
        tblVendasDetalhadas.setItems(vendasDetalhadas);
        
        // Configurar tabela de produtos mais vendidos
        colProdutoNome.setCellValueFactory(cellData -> {
            ItemVenda item = cellData.getValue();
            return javafx.beans.binding.Bindings.createStringBinding(() -> 
                item.getProduto() != null ? item.getProduto().getNome() : "");
        });
        colProdutoQuantidade.setCellValueFactory(new PropertyValueFactory<>("quantidade"));
        colProdutoValor.setCellValueFactory(new PropertyValueFactory<>("valorFinal"));
        colProdutoPercentual.setCellValueFactory(new PropertyValueFactory<>("percentual"));
        
        tblProdutosMaisVendidos.setItems(produtosMaisVendidos);
        
        // Configurar tabela de formas de pagamento
        colFormaNome.setCellValueFactory(new PropertyValueFactory<>("forma"));
        colFormaQuantidade.setCellValueFactory(new PropertyValueFactory<>("quantidade"));
        colFormaValor.setCellValueFactory(new PropertyValueFactory<>("valor"));
        colFormaPercentual.setCellValueFactory(new PropertyValueFactory<>("percentual"));
        
        tblFormasPagamento.setItems(formasPagamento);
        
        // Configurar tabela de vendas por hora
        colHoraPeriodo.setCellValueFactory(new PropertyValueFactory<>("periodo"));
        colHoraQuantidade.setCellValueFactory(new PropertyValueFactory<>("quantidade"));
        colHoraValor.setCellValueFactory(new PropertyValueFactory<>("valor"));
        colHoraPercentual.setCellValueFactory(new PropertyValueFactory<>("percentual"));
        
        tblVendasPorHora.setItems(vendasPorHora);
        
        // Configurar tabela de desempenho de operadores
        colOperadorNome.setCellValueFactory(new PropertyValueFactory<>("nome"));
        colOperadorVendas.setCellValueFactory(new PropertyValueFactory<>("vendas"));
        colOperadorValor.setCellValueFactory(new PropertyValueFactory<>("valor"));
        colOperadorTicket.setCellValueFactory(new PropertyValueFactory<>("ticketMedio"));
        colOperadorCanceladas.setCellValueFactory(new PropertyValueFactory<>("canceladas"));
        
        tblDesempenhoOperadores.setItems(desempenhoOperadores);
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
        // Carregar operadores disponíveis
        try {
            // Usar service para verificar se há usuários disponíveis
            List<Usuario> operadores = new ArrayList<>();
            if (usuarioService != null) {
                // TODO: Implementar método listar quando disponível no service
                // operadores = usuarioService.listar();
                System.out.println("DEBUG: Service de usuários inicializado, aguardando implementação do método listar()");
            }
            ObservableList<String> operadoresNomes = FXCollections.observableArrayList("Todos");
            
            for (Usuario operador : operadores) {
                operadoresNomes.add(operador.getNome());
            }
            
            comboOperador.setItems(operadoresNomes);
            
        } catch (Exception e) {
            atualizarStatus("Erro ao carregar operadores: " + e.getMessage());
        }
    }

    // Métodos de Ação dos Botões
    @FXML
    private void onVoltar() {
        fecharTela();
    }

    @FXML
    private void onGerarRelatorio() {
        if (!validarFiltros()) {
            return;
        }
        
        try {
            String tipoRelatorio = comboTipoRelatorio.getSelectionModel().getSelectedItem();
            LocalDate dataInicial = dpDataInicial.getValue();
            LocalDate dataFinal = dpDataFinal.getValue();
            String operador = comboOperador.getSelectionModel().getSelectedItem();
            String terminal = comboTerminal.getSelectionModel().getSelectedItem();
            String formaPagamento = comboFormaPagamento.getSelectionModel().getSelectedItem();
            
            atualizarStatus("Gerando relatório...");
            
            // Gerar relatório baseado no tipo selecionado
            switch (tipoRelatorio) {
                case "VENDAS_DETALHADAS":
                    gerarRelatorioVendasDetalhadas(dataInicial, dataFinal, operador, terminal, formaPagamento);
                    break;
                case "PRODUTOS_MAIS_VENDIDOS":
                    gerarRelatorioProdutosMaisVendidos(dataInicial, dataFinal, operador, terminal);
                    break;
                case "FORMAS_PAGAMENTO":
                    gerarRelatorioFormasPagamento(dataInicial, dataFinal, operador, terminal);
                    break;
                case "VENDAS_POR_HORA":
                    gerarRelatorioVendasPorHora(dataInicial, dataFinal, operador, terminal);
                    break;
                case "DESEMPENHO_OPERADORES":
                    gerarRelatorioDesempenhoOperadores(dataInicial, dataFinal, terminal);
                    break;
                case "RESUMO_COMPLETO":
                    gerarRelatorioCompleto(dataInicial, dataFinal, operador, terminal, formaPagamento);
                    break;
            }
            
            // Habilitar botões de exportação
            btnExportarPDF.setDisable(false);
            btnExportarExcel.setDisable(false);
            btnImprimir.setDisable(false);
            
            atualizarStatus("Relatório gerado com sucesso");
            
        } catch (Exception e) {
            atualizarStatus("Erro ao gerar relatório: " + e.getMessage());
            mostrarAlerta("Erro ao gerar relatório: " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    @FXML
    private void onLimparFiltros() {
        // Resetar filtros para valores padrão
        comboTipoRelatorio.getSelectionModel().selectFirst();
        LocalDate hoje = LocalDate.now();
        dpDataFinal.setValue(hoje);
        dpDataInicial.setValue(hoje.minusDays(7));
        comboOperador.getSelectionModel().selectFirst();
        comboTerminal.getSelectionModel().selectFirst();
        comboFormaPagamento.getSelectionModel().selectFirst();
        chkAgruparPorDia.setSelected(false);
        chkAgruparPorOperador.setSelected(false);
        chkAgruparPorFormaPagamento.setSelected(false);
        chkMostrarCancelados.setSelected(false);
        
        // Limpar tabelas
        limparTabelas();
        
        // Desabilitar botões de exportação
        btnExportarPDF.setDisable(true);
        btnExportarExcel.setDisable(true);
        btnImprimir.setDisable(true);
        
        atualizarStatus("Filtros limpos");
    }

    @FXML
    private void onExportarPDF() {
        try {
            atualizarStatus("Exportando relatório para PDF...");
            // Implementar exportação PDF
            mostrarAlerta("Relatório exportado para PDF com sucesso", Alert.AlertType.INFORMATION);
            atualizarStatus("PDF exportado com sucesso");
        } catch (Exception e) {
            atualizarStatus("Erro ao exportar PDF: " + e.getMessage());
            mostrarAlerta("Erro ao exportar PDF: " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    @FXML
    private void onExportarExcel() {
        try {
            atualizarStatus("Exportando relatório para Excel...");
            // Implementar exportação Excel
            mostrarAlerta("Relatório exportado para Excel com sucesso", Alert.AlertType.INFORMATION);
            atualizarStatus("Excel exportado com sucesso");
        } catch (Exception e) {
            atualizarStatus("Erro ao exportar Excel: " + e.getMessage());
            mostrarAlerta("Erro ao exportar Excel: " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    @FXML
    private void onImprimir() {
        try {
            atualizarStatus("Imprimindo relatório...");
            // Implementar impressão
            mostrarAlerta("Relatório enviado para impressão", Alert.AlertType.INFORMATION);
            atualizarStatus("Relatório impresso com sucesso");
        } catch (Exception e) {
            atualizarStatus("Erro ao imprimir: " + e.getMessage());
            mostrarAlerta("Erro ao imprimir: " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    // Métodos de Geração de Relatórios
    private void gerarRelatorioVendasDetalhadas(LocalDate dataInicial, LocalDate dataFinal, 
                                               String operador, String terminal, String formaPagamento) {
        try {
            // Simular busca de vendas (implementar com DAO real)
            vendasDetalhadas.clear();
            
            // Dados simulados
            for (int i = 1; i <= 10; i++) {
                VendaPDV venda = new VendaPDV();
                venda.setDataVenda(LocalDateTime.now().minusDays(i));
                venda.setNumeroCupom("CUPOM" + String.format("%06d", i));
                venda.setValorTotal(new BigDecimal(100.50 * i));
                venda.setStatus("CONCLUIDA");
                
                vendasDetalhadas.add(venda);
            }
            
            // Atualizar resumo
            atualizarResumo(vendasDetalhadas.size(), 
                vendasDetalhadas.stream().map(VendaPDV::getValorTotal).reduce(BigDecimal.ZERO, BigDecimal::add),
                BigDecimal.ZERO);
            
            // Selecionar tab de vendas detalhadas
            tabPaneResultados.getSelectionModel().select(0);
            
        } catch (Exception e) {
            throw new RuntimeException("Erro ao gerar relatório de vendas detalhadas", e);
        }
    }

    private void gerarRelatorioProdutosMaisVendidos(LocalDate dataInicial, LocalDate dataFinal, 
                                                   String operador, String terminal) {
        try {
            produtosMaisVendidos.clear();
            
            // Dados simulados
            for (int i = 1; i <= 10; i++) {
                ItemVenda item = new ItemVenda();
                item.setQuantidade(i * 5);
                item.setValorFinal(new BigDecimal(50.0 * i));
                
                produtosMaisVendidos.add(item);
            }
            
            // Selecionar tab de produtos
            tabPaneResultados.getSelectionModel().select(1);
            
        } catch (Exception e) {
            throw new RuntimeException("Erro ao gerar relatório de produtos mais vendidos", e);
        }
    }

    private void gerarRelatorioFormasPagamento(LocalDate dataInicial, LocalDate dataFinal, 
                                              String operador, String terminal) {
        try {
            formasPagamento.clear();
            
            // Dados simulados
            String[] formas = {"DINHEIRO", "CARTAO_CREDITO", "PIX", "CARTAO_DEBITO"};
            for (int i = 0; i < formas.length; i++) {
                RelatorioPagamento relatorio = new RelatorioPagamento();
                relatorio.setForma(formas[i]);
                relatorio.setQuantidade((i + 1) * 10);
                relatorio.setValor(new BigDecimal(100.0 * (i + 1)));
                relatorio.setPercentual((25.0 * (i + 1)) + "%");
                
                formasPagamento.add(relatorio);
            }
            
            // Selecionar tab de formas de pagamento
            tabPaneResultados.getSelectionModel().select(2);
            
        } catch (Exception e) {
            throw new RuntimeException("Erro ao gerar relatório de formas de pagamento", e);
        }
    }

    private void gerarRelatorioVendasPorHora(LocalDate dataInicial, LocalDate dataFinal, 
                                            String operador, String terminal) {
        try {
            vendasPorHora.clear();
            
            // Dados simulados por período
            String[] periodos = {"06:00-09:00", "09:00-12:00", "12:00-15:00", "15:00-18:00", "18:00-21:00"};
            for (int i = 0; i < periodos.length; i++) {
                RelatorioHora relatorio = new RelatorioHora();
                relatorio.setPeriodo(periodos[i]);
                relatorio.setQuantidade((i + 1) * 5);
                relatorio.setValor(new BigDecimal(200.0 * (i + 1)));
                relatorio.setPercentual((20.0 * (i + 1)) + "%");
                
                vendasPorHora.add(relatorio);
            }
            
            // Selecionar tab de vendas por hora
            tabPaneResultados.getSelectionModel().select(3);
            
        } catch (Exception e) {
            throw new RuntimeException("Erro ao gerar relatório de vendas por hora", e);
        }
    }

    private void gerarRelatorioDesempenhoOperadores(LocalDate dataInicial, LocalDate dataFinal, 
                                                   String terminal) {
        try {
            desempenhoOperadores.clear();
            
            // Dados simulados
            String[] operadores = {"João Silva", "Maria Santos", "Pedro Oliveira", "Ana Costa"};
            for (int i = 0; i < operadores.length; i++) {
                RelatorioOperador relatorio = new RelatorioOperador();
                relatorio.setNome(operadores[i]);
                relatorio.setVendas((i + 1) * 15);
                relatorio.setValor(new BigDecimal(1000.0 * (i + 1)));
                relatorio.setTicketMedio(new BigDecimal(50.0 + (i * 10)));
                relatorio.setCanceladas(i);
                
                desempenhoOperadores.add(relatorio);
            }
            
            // Selecionar tab de desempenho
            tabPaneResultados.getSelectionModel().select(4);
            
        } catch (Exception e) {
            throw new RuntimeException("Erro ao gerar relatório de desempenho de operadores", e);
        }
    }

    private void gerarRelatorioCompleto(LocalDate dataInicial, LocalDate dataFinal, 
                                       String operador, String terminal, String formaPagamento) {
        // Gerar todos os relatórios
        gerarRelatorioVendasDetalhadas(dataInicial, dataFinal, operador, terminal, formaPagamento);
        gerarRelatorioProdutosMaisVendidos(dataInicial, dataFinal, operador, terminal);
        gerarRelatorioFormasPagamento(dataInicial, dataFinal, operador, terminal);
        gerarRelatorioVendasPorHora(dataInicial, dataFinal, operador, terminal);
        gerarRelatorioDesempenhoOperadores(dataInicial, dataFinal, terminal);
        
        // Selecionar primeira tab
        tabPaneResultados.getSelectionModel().select(0);
    }

    // Métodos Utilitários
    private boolean validarFiltros() {
        if (dpDataInicial.getValue() == null || dpDataFinal.getValue() == null) {
            mostrarAlerta("Selecione as datas inicial e final", Alert.AlertType.WARNING);
            return false;
        }
        
        if (dpDataInicial.getValue().isAfter(dpDataFinal.getValue())) {
            mostrarAlerta("Data inicial não pode ser posterior à data final", Alert.AlertType.WARNING);
            return false;
        }
        
        if (comboTipoRelatorio.getSelectionModel().getSelectedItem() == null) {
            mostrarAlerta("Selecione o tipo de relatório", Alert.AlertType.WARNING);
            return false;
        }
        
        return true;
    }

    private void configurarFiltrosParaTipoRelatorio(String tipoRelatorio) {
        // Configurar filtros específicos baseados no tipo de relatório
        switch (tipoRelatorio) {
            case "FORMAS_PAGAMENTO":
                comboFormaPagamento.getSelectionModel().selectFirst();
                comboFormaPagamento.setDisable(false);
                break;
            case "DESEMPENHO_OPERADORES":
                comboOperador.getSelectionModel().selectFirst();
                comboOperador.setDisable(false);
                break;
            default:
                comboFormaPagamento.setDisable(false);
                comboOperador.setDisable(false);
        }
    }

    private void limparTabelas() {
        vendasDetalhadas.clear();
        produtosMaisVendidos.clear();
        formasPagamento.clear();
        vendasPorHora.clear();
        desempenhoOperadores.clear();
        
        // Limpar resumo
        lblTotalVendas.setText("0");
        lblValorTotal.setText("R$ 0,00");
        lblTicketMedio.setText("R$ 0,00");
        lblVendasCanceladas.setText("0");
    }

    private void atualizarResumo(int totalVendas, BigDecimal valorTotal, BigDecimal canceladas) {
        lblTotalVendas.setText(String.valueOf(totalVendas));
        lblValorTotal.setText(formatarMoeda(valorTotal));
        lblVendasCanceladas.setText(String.valueOf(canceladas.intValue()));
        
        // Calcular ticket médio
        if (totalVendas > 0) {
            BigDecimal ticketMedio = valorTotal.divide(new BigDecimal(totalVendas), 2, RoundingMode.HALF_UP);
            lblTicketMedio.setText(formatarMoeda(ticketMedio));
        } else {
            lblTicketMedio.setText("R$ 0,00");
        }
    }

    private void atualizarStatus(String mensagem) {
        lblStatus.setText(mensagem);
        
        // Atualizar cor do status
        if (mensagem.toLowerCase().contains("erro")) {
            lblStatus.setStyle("-fx-text-fill: #e74c3c; -fx-font-weight: bold;");
        } else if (mensagem.toLowerCase().contains("sucesso") || mensagem.toLowerCase().contains("gerado")) {
            lblStatus.setStyle("-fx-text-fill: #28a745; -fx-font-weight: bold;");
        } else {
            lblStatus.setStyle("-fx-text-fill: #f39c12; -fx-font-weight: bold;");
        }
    }

    private String formatarMoeda(BigDecimal valor) {
        if (valor == null) return "R$ 0,00";
        return String.format("R$ %,.2f", valor).replace('.', ',');
    }

    private void mostrarAlerta(String mensagem, Alert.AlertType tipo) {
        Alert alert = new Alert(tipo);
        alert.setTitle("Relatórios PDV");
        alert.setHeaderText(null);
        alert.setContentText(mensagem);
        alert.showAndWait();
    }

    private void fecharTela() {
        // Fechar a janela atual
        javafx.stage.Stage stage = (javafx.stage.Stage) lblStatus.getScene().getWindow();
        stage.close();
    }
}
