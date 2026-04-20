package com.br.hermescomercial.controller.pdv;

import com.br.hermescomercial.business.pdv.PDVManager;
import com.br.hermescomercial.model.Usuario;
import com.br.hermescomercial.model.MovimentacaoCaixa;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
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

public class PDVCaixaAbertoController implements Initializable {

    private PDVManager pdvManager;
    private Usuario operadorAtual;
    private ObservableList<MovimentacaoCaixa> movimentacoes;
    private Timer timer;
    private DateTimeFormatter formatadorDataHora = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
    // private DateTimeFormatter formatadorHora = DateTimeFormatter.ofPattern("HH:mm:ss"); - não utilizado

    // Componentes FXML
    @FXML private Button btnVoltar;
    @FXML private Button btnAtualizar;
    @FXML private Button btnSuprimento;
    @FXML private Button btnSangria;
    @FXML private Button btnFecharCaixa;
    
    @FXML private Label lblDataHora;
    @FXML private Label lblTerminal;
    @FXML private Label lblOperador;
    @FXML private Label lblDataAbertura;
    @FXML private Label lblStatus;
    @FXML private Label lblTerminalInfo;
    
    @FXML private Label lblSaldoInicial;
    @FXML private Label lblVendasDia;
    @FXML private Label lblEntradas;
    @FXML private Label lblSaidas;
    @FXML private Label lblSaldoAtual;
    
    @FXML private TableView<MovimentacaoCaixa> tblMovimentacoes;
    @FXML private TableColumn<MovimentacaoCaixa, String> colHora;
    @FXML private TableColumn<MovimentacaoCaixa, String> colTipo;
    @FXML private TableColumn<MovimentacaoCaixa, String> colDescricao;
    @FXML private TableColumn<MovimentacaoCaixa, BigDecimal> colValor;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        inicializarComponentes();
        inicializarTabela();
        inicializarTimer();
        carregarDadosCaixa();
    }

    private void inicializarComponentes() {
        this.movimentacoes = FXCollections.observableArrayList();
        
        // Configurar dados iniciais (simulados)
        // Inicializar PDVManager usando Singleton
        this.pdvManager = PDVManager.getInstance();
        this.operadorAtual = new Usuario();
        operadorAtual.setNome("João Silva");
        operadorAtual.setId(1L);
    }

    private void inicializarTabela() {
        colHora.setCellValueFactory(new PropertyValueFactory<>("horaFormatada"));
        colTipo.setCellValueFactory(new PropertyValueFactory<>("tipoMovimentacao"));
        colDescricao.setCellValueFactory(new PropertyValueFactory<>("descricao"));
        colValor.setCellValueFactory(new PropertyValueFactory<>("valorFormatado"));
        
        tblMovimentacoes.setItems(movimentacoes);
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

    private void carregarDadosCaixa() {
        try {
            // Informações do caixa
            lblTerminal.setText(pdvManager.getNumeroTerminal());
            lblTerminalInfo.setText(pdvManager.getNumeroTerminal());
            lblOperador.setText(operadorAtual.getNome());
            lblDataAbertura.setText(LocalDateTime.now().format(formatadorDataHora));
            
            if (pdvManager.isCaixaAberto()) {
                lblStatus.setText("ABERTO");
                lblStatus.setStyle("-fx-text-fill: #27ae60; -fx-font-weight: bold;");
            } else {
                lblStatus.setText("FECHADO");
                lblStatus.setStyle("-fx-text-fill: #e74c3c; -fx-font-weight: bold;");
            }
            
            // Valores (simulados)
            BigDecimal saldoInicial = new BigDecimal("100.00");
            BigDecimal vendasDia = new BigDecimal("1250.50");
            BigDecimal entradas = new BigDecimal("200.00");
            BigDecimal saidas = new BigDecimal("50.00");
            BigDecimal saldoAtual = saldoInicial.add(vendasDia).add(entradas).subtract(saidas);
            
            lblSaldoInicial.setText(formatarMoeda(saldoInicial));
            lblVendasDia.setText(formatarMoeda(vendasDia));
            lblEntradas.setText(formatarMoeda(entradas));
            lblSaidas.setText(formatarMoeda(saidas));
            lblSaldoAtual.setText(formatarMoeda(saldoAtual));
            
            // Carregar movimentações simuladas
            carregarMovimentacoes();
            
        } catch (Exception e) {
            System.err.println("Erro ao carregar dados do caixa: " + e.getMessage());
        }
    }

    private void carregarMovimentacoes() {
        movimentacoes.clear();
        
        // Adicionar movimentações simuladas
        movimentacoes.add(new MovimentacaoCaixa(
            LocalDateTime.now().minusHours(4),
            "ABERTURA",
            "Abertura do caixa",
            new BigDecimal("100.00")
        ));
        
        movimentacoes.add(new MovimentacaoCaixa(
            LocalDateTime.now().minusHours(3),
            "VENDA",
            "Venda #001 - Cliente: João",
            new BigDecimal("150.00")
        ));
        
        movimentacoes.add(new MovimentacaoCaixa(
            LocalDateTime.now().minusHours(2),
            "SUPRIMENTO",
            "Suprimento de caixa",
            new BigDecimal("200.00")
        ));
        
        movimentacoes.add(new MovimentacaoCaixa(
            LocalDateTime.now().minusHours(1),
            "VENDA",
            "Venda #002 - Cliente: Maria",
            new BigDecimal("85.50")
        ));
        
        movimentacoes.add(new MovimentacaoCaixa(
            LocalDateTime.now().minusMinutes(30),
            "SANGRIA",
            "Sangria para despesas",
            new BigDecimal("50.00")
        ));
    }

    // Métodos de Ação
    @FXML
    private void onVoltar() {
        try {
            timer.cancel();
            javafx.stage.Stage stage = (javafx.stage.Stage) btnVoltar.getScene().getWindow();
            stage.close();
        } catch (Exception e) {
            System.err.println("Erro ao voltar: " + e.getMessage());
        }
    }

    @FXML
    private void onAtualizar() {
        carregarDadosCaixa();
        carregarMovimentacoes();
    }

    @FXML
    private void onSuprimento() {
        try {
            // Criar diálogo para suprimento
            TextInputDialog dialog = new TextInputDialog();
            dialog.setTitle("Suprimento de Caixa");
            dialog.setHeaderText("Informar valor do suprimento:");
            dialog.setContentText("Valor (R$):");
            
            dialog.showAndWait().ifPresent(valorStr -> {
                try {
                    BigDecimal valor = new BigDecimal(valorStr.replace(",", "."));
                    if (valor.compareTo(BigDecimal.ZERO) > 0) {
                        // Adicionar movimentação
                        MovimentacaoCaixa movimentacao = new MovimentacaoCaixa(
                            LocalDateTime.now(),
                            "SUPRIMENTO",
                            "Suprimento manual",
                            valor
                        );
                        movimentacoes.add(0, movimentacao);
                        
                        // Atualizar saldo
                        BigDecimal saldoAtual = new BigDecimal(lblSaldoAtual.getText().replace("R$ ", "").replace(",", "."));
                        BigDecimal novoSaldo = saldoAtual.add(valor);
                        lblSaldoAtual.setText(formatarMoeda(novoSaldo));
                        
                        // Atualizar entradas
                        BigDecimal entradas = new BigDecimal(lblEntradas.getText().replace("R$ ", "").replace(",", "."));
                        BigDecimal novasEntradas = entradas.add(valor);
                        lblEntradas.setText(formatarMoeda(novasEntradas));
                        
                        Alert alert = new Alert(Alert.AlertType.INFORMATION);
                        alert.setTitle("Sucesso");
                        alert.setHeaderText(null);
                        alert.setContentText("Suprimento de R$ " + formatarMoeda(valor) + " registrado com sucesso!");
                        alert.showAndWait();
                    }
                } catch (NumberFormatException e) {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Erro");
                    alert.setHeaderText(null);
                    alert.setContentText("Valor inválido. Digite um número válido.");
                    alert.showAndWait();
                }
            });
        } catch (Exception e) {
            System.err.println("Erro ao realizar suprimento: " + e.getMessage());
        }
    }

    @FXML
    private void onSangria() {
        try {
            // Criar diálogo para sangria
            TextInputDialog dialog = new TextInputDialog();
            dialog.setTitle("Sangria de Caixa");
            dialog.setHeaderText("Informar valor da sangria:");
            dialog.setContentText("Valor (R$):");
            
            dialog.showAndWait().ifPresent(valorStr -> {
                try {
                    BigDecimal valor = new BigDecimal(valorStr.replace(",", "."));
                    if (valor.compareTo(BigDecimal.ZERO) > 0) {
                        // Verificar se há saldo suficiente
                        BigDecimal saldoAtual = new BigDecimal(lblSaldoAtual.getText().replace("R$ ", "").replace(",", "."));
                        if (valor.compareTo(saldoAtual) <= 0) {
                            // Adicionar movimentação
                            MovimentacaoCaixa movimentacao = new MovimentacaoCaixa(
                                LocalDateTime.now(),
                                "SANGRIA",
                                "Sangria manual",
                                valor.negate()
                            );
                            movimentacoes.add(0, movimentacao);
                            
                            // Atualizar saldo
                            BigDecimal novoSaldo = saldoAtual.subtract(valor);
                            lblSaldoAtual.setText(formatarMoeda(novoSaldo));
                            
                            // Atualizar saídas
                            BigDecimal saidas = new BigDecimal(lblSaidas.getText().replace("R$ ", "").replace(",", "."));
                            BigDecimal novasSaidas = saidas.add(valor);
                            lblSaidas.setText(formatarMoeda(novasSaidas));
                            
                            Alert alert = new Alert(Alert.AlertType.INFORMATION);
                            alert.setTitle("Sucesso");
                            alert.setHeaderText(null);
                            alert.setContentText("Sangria de R$ " + formatarMoeda(valor) + " registrada com sucesso!");
                            alert.showAndWait();
                        } else {
                            Alert alert = new Alert(Alert.AlertType.WARNING);
                            alert.setTitle("Saldo Insuficiente");
                            alert.setHeaderText(null);
                            alert.setContentText("Saldo em caixa insuficiente para esta sangria.");
                            alert.showAndWait();
                        }
                    }
                } catch (NumberFormatException e) {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Erro");
                    alert.setHeaderText(null);
                    alert.setContentText("Valor inválido. Digite um número válido.");
                    alert.showAndWait();
                }
            });
        } catch (Exception e) {
            System.err.println("Erro ao realizar sangria: " + e.getMessage());
        }
    }

    @FXML
    private void onFecharCaixa() {
        try {
            if (!pdvManager.isCaixaAberto()) {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Caixa Fechado");
                alert.setHeaderText(null);
                alert.setContentText("O caixa já está fechado.");
                alert.showAndWait();
                return;
            }
            
            // Confirmar fechamento
            Alert confirmDialog = new Alert(Alert.AlertType.CONFIRMATION);
            confirmDialog.setTitle("Fechar Caixa");
            confirmDialog.setHeaderText("Confirmar fechamento do caixa");
            confirmDialog.setContentText("Deseja realmente fechar o caixa? Esta ação não poderá ser desfeita.");
            
            if (confirmDialog.showAndWait().get() == ButtonType.OK) {
                if (pdvManager.fecharCaixa()) {
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Sucesso");
                    alert.setHeaderText(null);
                    alert.setContentText("Caixa fechado com sucesso!");
                    alert.showAndWait();
                    
                    // Atualizar status
                    lblStatus.setText("FECHADO");
                    lblStatus.setStyle("-fx-text-fill: #e74c3c; -fx-font-weight: bold;");
                    
                    // Fechar janela após alguns segundos
                    Timer timerFechar = new Timer();
                    timerFechar.schedule(new TimerTask() {
                        @Override
                        public void run() {
                            javafx.application.Platform.runLater(() -> {
                                onVoltar();
                            });
                        }
                    }, 2000);
                } else {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Erro");
                    alert.setHeaderText(null);
                    alert.setContentText("Erro ao fechar o caixa. Tente novamente.");
                    alert.showAndWait();
                }
            }
        } catch (Exception e) {
            System.err.println("Erro ao fechar caixa: " + e.getMessage());
        }
    }

    // Métodos Utilitários
    private String formatarMoeda(BigDecimal valor) {
        if (valor == null) return "R$ 0,00";
        return String.format("R$ %,.2f", valor).replace('.', ',');
    }

    // Getters e Setters
    public void setPdvManager(PDVManager pdvManager) {
        this.pdvManager = pdvManager;
    }

    public void setOperadorAtual(Usuario operadorAtual) {
        this.operadorAtual = operadorAtual;
    }
}
