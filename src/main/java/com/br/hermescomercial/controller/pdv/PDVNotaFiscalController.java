package com.br.hermescomercial.controller.pdv;

import com.br.hermescomercial.business.notafiscal.NotaFiscalService;
import com.br.hermescomercial.model.NotaFiscal;
import com.br.hermescomercial.model.NotaFiscalItem;
import com.br.hermescomercial.model.NotaFiscalPagamentoItem;
import com.br.hermescomercial.model.VendaPDV;
import com.br.hermescomercial.model.Usuario;
// import com.br.hermescomercial.model.Produto; - não utilizado
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
import java.util.Timer;
import java.util.TimerTask;

public class PDVNotaFiscalController implements Initializable {

    private NotaFiscalService notaFiscalService;
    private NotaFiscal notaFiscal;
    private VendaPDV venda;
    private Usuario operadorAtual;
    private ObservableList<NotaFiscalItem> itensNotaFiscal;
    private ObservableList<NotaFiscalPagamentoItem> pagamentosNotaFiscal;
    private Timer timer;
    private DateTimeFormatter formatadorDataHora = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");

    // Componentes FXML
    @FXML private Button btnVoltar;
    @FXML private Button btnAdicionarItem;
    @FXML private Button btnValidar;
    @FXML private Button btnEmitir;
    @FXML private Button btnImprimir;
    @FXML private Button btnCancelar;
    @FXML private Button btnConsultar;
    
    @FXML private Label lblDataHora;
    @FXML private Label lblNumero;
    @FXML private Label lblSerie;
    @FXML private Label lblModelo;
    @FXML private Label lblStatus;
    @FXML private Label lblDataEmissao;
    @FXML private Label lblChaveAcesso;
    
    @FXML private Label lblEmitenteNome;
    @FXML private Label lblEmitenteCnpj;
    @FXML private Label lblEmitenteEndereco;
    @FXML private Label lblEmitenteTelefone;
    
    @FXML private Label lblDestinatarioNome;
    @FXML private Label lblDestinatarioCpfCnpj;
    @FXML private Label lblDestinatarioEndereco;
    @FXML private Label lblDestinatarioTelefone;
    
    @FXML private Label lblTotalProdutos;
    @FXML private Label lblDesconto;
    @FXML private Label lblFrete;
    @FXML private Label lblValorTotalNf;
    
    @FXML private Label lblBaseIcms;
    @FXML private Label lblValorIcms;
    @FXML private Label lblValorIpi;
    @FXML private Label lblValorPis;
    @FXML private Label lblValorCofins;
    
    @FXML private Label lblOperador;
    
    @FXML private TableView<NotaFiscalItem> tblItens;
    @FXML private TableColumn<NotaFiscalItem, String> colCodigo;
    @FXML private TableColumn<NotaFiscalItem, String> colDescricao;
    @FXML private TableColumn<NotaFiscalItem, String> colQuantidade;
    @FXML private TableColumn<NotaFiscalItem, String> colValorUnitario;
    @FXML private TableColumn<NotaFiscalItem, String> colValorTotal;
    @FXML private TableColumn<NotaFiscalItem, String> colAcoes;
    
    @FXML private TableView<NotaFiscalPagamentoItem> tblPagamentos;
    @FXML private TableColumn<NotaFiscalPagamentoItem, String> colFormaPagamento;
    @FXML private TableColumn<NotaFiscalPagamentoItem, String> colValorPagamento;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        inicializarComponentes();
        inicializarTabelas();
        inicializarTimer();
    }

    private void inicializarComponentes() {
        this.itensNotaFiscal = FXCollections.observableArrayList();
        this.pagamentosNotaFiscal = FXCollections.observableArrayList();
        this.notaFiscalService = new NotaFiscalService();
    }

    private void inicializarTabelas() {
        // Configurar tabela de itens
        colCodigo.setCellValueFactory(new PropertyValueFactory<>("codigoProduto"));
        colDescricao.setCellValueFactory(new PropertyValueFactory<>("descricao"));
        colQuantidade.setCellValueFactory(new PropertyValueFactory<>("quantidadeFormatada"));
        colValorUnitario.setCellValueFactory(new PropertyValueFactory<>("valorUnitarioFormatado"));
        colValorTotal.setCellValueFactory(new PropertyValueFactory<>("valorFormatado"));
        colAcoes.setCellValueFactory(new PropertyValueFactory<>("acoes"));
        
        tblItens.setItems(itensNotaFiscal);
        
        // Configurar tabela de pagamentos
        colFormaPagamento.setCellValueFactory(new PropertyValueFactory<>("formaPagamentoDescricao"));
        colValorPagamento.setCellValueFactory(new PropertyValueFactory<>("valorFormatado"));
        
        tblPagamentos.setItems(pagamentosNotaFiscal);
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

    public void setDadosNotaFiscal(VendaPDV venda, Usuario operador) {
        this.venda = venda;
        this.operadorAtual = operador;
        this.lblOperador.setText(operador.getNome());
        
        // Gerar nota fiscal a partir da venda
        gerarNotaFiscal();
    }

    private void gerarNotaFiscal() {
        try {
            notaFiscal = notaFiscalService.gerarNotaFiscal(venda, operadorAtual);
            
            if (notaFiscal != null) {
                atualizarInterfaceNotaFiscal();
                carregarItensNotaFiscal();
                carregarPagamentosNotaFiscal();
                atualizarTotais();
            }
        } catch (Exception e) {
            mostrarAlerta("Erro ao gerar nota fiscal: " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    private void atualizarInterfaceNotaFiscal() {
        if (notaFiscal != null) {
            // Dados básicos
            lblNumero.setText(notaFiscal.getNumero());
            lblSerie.setText(notaFiscal.getSerie());
            lblModelo.setText("65 - NFC-e");
            lblStatus.setText(notaFiscal.getStatus());
            lblDataEmissao.setText(notaFiscal.getDataEmissao().format(formatadorDataHora));
            lblChaveAcesso.setText(notaFiscal.getChaveAcesso());
            
            // Atualizar cor do status
            switch (notaFiscal.getStatus()) {
                case "EM_ELABORACAO":
                    lblStatus.setStyle("-fx-text-fill: #f39c12; -fx-font-weight: bold;");
                    break;
                case "AUTORIZADA":
                    lblStatus.setStyle("-fx-text-fill: #27ae60; -fx-font-weight: bold;");
                    break;
                case "CANCELADA":
                    lblStatus.setStyle("-fx-text-fill: #e74c3c; -fx-font-weight: bold;");
                    break;
                case "REJEITADA":
                    lblStatus.setStyle("-fx-text-fill: #e74c3c; -fx-font-weight: bold;");
                    break;
                default:
                    lblStatus.setStyle("-fx-text-fill: #2c3e50; -fx-font-weight: bold;");
            }
            
            // Emitente
            lblEmitenteNome.setText(notaFiscal.getEmitenteNome());
            lblEmitenteCnpj.setText(notaFiscal.getEmitenteCnpj());
            lblEmitenteEndereco.setText(notaFiscal.getEmitenteEndereco() + " - " + notaFiscal.getEmitenteMunicipio() + "/" + notaFiscal.getEmitenteUf());
            lblEmitenteTelefone.setText(notaFiscal.getEmitenteTelefone());
            
            // Destinatário
            lblDestinatarioNome.setText(notaFiscal.getDestinatarioNome());
            lblDestinatarioCpfCnpj.setText(notaFiscal.getDestinatarioCnpjCpf());
            if (notaFiscal.getDestinatarioEndereco() != null) {
                lblDestinatarioEndereco.setText(notaFiscal.getDestinatarioEndereco() + " - " + notaFiscal.getDestinatarioMunicipio() + "/" + notaFiscal.getDestinatarioUf());
            }
            lblDestinatarioTelefone.setText(notaFiscal.getDestinatarioTelefone());
            
            // Atualizar botões
            atualizarBotoes();
        }
    }

    private void carregarItensNotaFiscal() {
        itensNotaFiscal.clear();
        
        if (notaFiscal != null && notaFiscal.getItens() != null) {
            itensNotaFiscal.addAll(notaFiscal.getItens());
        }
    }

    private void carregarPagamentosNotaFiscal() {
        pagamentosNotaFiscal.clear();
        
        if (notaFiscal != null && notaFiscal.getPagamentos() != null && notaFiscal.getPagamentos().getPagamentos() != null) {
            pagamentosNotaFiscal.addAll(notaFiscal.getPagamentos().getPagamentos());
        }
    }

    private void atualizarTotais() {
        if (notaFiscal != null) {
            lblTotalProdutos.setText(formatarMoeda(notaFiscal.getValorTotalProdutos()));
            lblDesconto.setText(formatarMoeda(notaFiscal.getValorDesconto()));
            lblFrete.setText(formatarMoeda(notaFiscal.getValorFrete()));
            lblValorTotalNf.setText(formatarMoeda(notaFiscal.getValorTotalNota()));
            
            // Impostos
            lblBaseIcms.setText(formatarMoeda(notaFiscal.getBaseCalculoIcms()));
            lblValorIcms.setText(formatarMoeda(notaFiscal.getValorIcms()));
            lblValorIpi.setText(formatarMoeda(notaFiscal.getValorIpi()));
            lblValorPis.setText(formatarMoeda(notaFiscal.getValorPis()));
            lblValorCofins.setText(formatarMoeda(notaFiscal.getValorCofins()));
        }
    }

    private void atualizarBotoes() {
        if (notaFiscal != null) {
            switch (notaFiscal.getStatus()) {
                case "EM_ELABORACAO":
                    btnValidar.setDisable(false);
                    btnEmitir.setDisable(false);
                    btnImprimir.setDisable(true);
                    btnCancelar.setDisable(true);
                    btnConsultar.setDisable(true);
                    break;
                case "AUTORIZADA":
                    btnValidar.setDisable(true);
                    btnEmitir.setDisable(true);
                    btnImprimir.setDisable(false);
                    btnCancelar.setDisable(false);
                    btnConsultar.setDisable(false);
                    break;
                case "CANCELADA":
                case "REJEITADA":
                    btnValidar.setDisable(true);
                    btnEmitir.setDisable(true);
                    btnImprimir.setDisable(true);
                    btnCancelar.setDisable(true);
                    btnConsultar.setDisable(false);
                    break;
            }
        }
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
    private void onAdicionarItem() {
        // Implementar diálogo para adicionar item
        mostrarAlerta("Funcionalidade de adicionar item em desenvolvimento", Alert.AlertType.INFORMATION);
    }

    @FXML
    private void onValidar() {
        try {
            if (notaFiscalService.validarNotaFiscal(notaFiscal)) {
                mostrarAlerta("Nota fiscal validada com sucesso!", Alert.AlertType.INFORMATION);
            } else {
                mostrarAlerta("Nota fiscal inválida. Verifique os dados.", Alert.AlertType.ERROR);
            }
        } catch (Exception e) {
            mostrarAlerta("Erro na validação: " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    @FXML
    private void onEmitir() {
        try {
            // Confirmar emissão
            Alert confirmDialog = new Alert(Alert.AlertType.CONFIRMATION);
            confirmDialog.setTitle("Emitir Nota Fiscal");
            confirmDialog.setHeaderText("Confirmar emissão da nota fiscal");
            confirmDialog.setContentText("Deseja realmente emitir esta nota fiscal?");
            
            if (confirmDialog.showAndWait().get() == ButtonType.OK) {
                // Mostrar progresso
                mostrarAlerta("Enviando nota fiscal para autorização...", Alert.AlertType.INFORMATION);
                
                // Emitir em thread separada para não bloquear UI
                new Thread(() -> {
                    boolean sucesso = notaFiscalService.autorizarNotaFiscal(notaFiscal);
                    
                    javafx.application.Platform.runLater(() -> {
                        if (sucesso) {
                            atualizarInterfaceNotaFiscal();
                            mostrarAlerta("Nota fiscal autorizada com sucesso!\nProtocolo: " + notaFiscal.getProtocoloAutorizacao(), Alert.AlertType.INFORMATION);
                        } else {
                            mostrarAlerta("Erro na autorização da nota fiscal", Alert.AlertType.ERROR);
                        }
                    });
                }).start();
            }
        } catch (Exception e) {
            mostrarAlerta("Erro ao emitir nota fiscal: " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    @FXML
    private void onImprimir() {
        try {
            if (notaFiscal != null && notaFiscal.isAutorizada()) {
                // Implementar impressão
                mostrarAlerta("Funcionalidade de impressão em desenvolvimento", Alert.AlertType.INFORMATION);
            } else {
                mostrarAlerta("Nota fiscal não autorizada para impressão", Alert.AlertType.WARNING);
            }
        } catch (Exception e) {
            mostrarAlerta("Erro ao imprimir nota fiscal: " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    @FXML
    private void onCancelar() {
        try {
            if (notaFiscal != null && notaFiscal.podeCancelar()) {
                // Dialog para motivo do cancelamento
                TextInputDialog dialog = new TextInputDialog();
                dialog.setTitle("Cancelar Nota Fiscal");
                dialog.setHeaderText("Informar motivo do cancelamento:");
                dialog.setContentText("Motivo:");
                
                dialog.showAndWait().ifPresent(motivo -> {
                    if (!motivo.trim().isEmpty()) {
                        new Thread(() -> {
                            boolean sucesso = notaFiscalService.cancelarNotaFiscal(notaFiscal, motivo);
                            
                            javafx.application.Platform.runLater(() -> {
                                if (sucesso) {
                                    atualizarInterfaceNotaFiscal();
                                    atualizarBotoes();
                                    mostrarAlerta("Nota fiscal cancelada com sucesso", Alert.AlertType.INFORMATION);
                                } else {
                                    mostrarAlerta("Erro ao cancelar nota fiscal", Alert.AlertType.ERROR);
                                }
                            });
                        }).start();
                    }
                });
            } else {
                mostrarAlerta("Nota fiscal não pode ser cancelada", Alert.AlertType.WARNING);
            }
        } catch (Exception e) {
            mostrarAlerta("Erro ao cancelar nota fiscal: " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    @FXML
    private void onConsultar() {
        try {
            if (notaFiscal != null) {
                StringBuilder info = new StringBuilder();
                info.append("Status: ").append(notaFiscal.getStatus()).append("\n");
                
                if (notaFiscal.getDataHoraAutorizacao() != null) {
                    info.append("Data Autorização: ").append(notaFiscal.getDataHoraAutorizacao().format(formatadorDataHora)).append("\n");
                }
                
                if (notaFiscal.getProtocoloAutorizacao() != null) {
                    info.append("Protocolo: ").append(notaFiscal.getProtocoloAutorizacao()).append("\n");
                }
                
                if (notaFiscal.getChaveAcesso() != null) {
                    info.append("Chave: ").append(notaFiscal.getChaveAcesso()).append("\n");
                }
                
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Consulta de Nota Fiscal");
                alert.setHeaderText("Informações da Nota Fiscal");
                alert.setContentText(info.toString());
                alert.showAndWait();
            }
        } catch (Exception e) {
            mostrarAlerta("Erro na consulta: " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    // Métodos Utilitários
    private String formatarMoeda(BigDecimal valor) {
        if (valor == null) return "R$ 0,00";
        return String.format("R$ %,.2f", valor).replace('.', ',');
    }

    private void mostrarAlerta(String mensagem, Alert.AlertType tipo) {
        Alert alert = new Alert(tipo);
        alert.setTitle("Nota Fiscal - Hermes Comercial");
        alert.setHeaderText(null);
        alert.setContentText(mensagem);
        alert.showAndWait();
    }

    // Getters e Setters
    public NotaFiscal getNotaFiscal() {
        return notaFiscal;
    }

    public void setNotaFiscal(NotaFiscal notaFiscal) {
        this.notaFiscal = notaFiscal;
    }
}
