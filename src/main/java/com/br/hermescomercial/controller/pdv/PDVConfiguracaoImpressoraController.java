package com.br.hermescomercial.controller.pdv;

import com.br.hermescomercial.business.impressao.ImpressaoNaoFiscalService;
import com.br.hermescomercial.model.ImpressoraNaoFiscal;
import com.br.hermescomercial.model.Usuario;
import com.br.hermescomercial.model.VendaPDV;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.net.URL;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;
import java.util.Timer;
import java.util.TimerTask;

public class PDVConfiguracaoImpressoraController implements Initializable {

    private ImpressaoNaoFiscalService impressaoService;
    private Usuario operadorAtual;
    private Timer timer;
    private DateTimeFormatter formatadorDataHora = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");

    // Componentes FXML - Status
    @FXML private Label lblNomeImpressora;
    @FXML private Label lblStatus;
    @FXML private Label lblUltimaVerificacao;
    @FXML private Label lblMensagemStatus;
    @FXML private Label lblDataHora;
    @FXML private Label lblOperador;

    // Componentes FXML - Botões de Ação
    @FXML private Button btnVoltar;
    @FXML private Button btnVerificarStatus;
    @FXML private Button btnTestarImpressao;
    @FXML private Button btnAbrirGaveta;
    @FXML private Button btnConectar;
    @FXML private Button btnDesconectar;

    // Componentes FXML - Configurações Básicas
    @FXML private TextField txtNome;
    @FXML private ComboBox<String> cmbFabricante;
    @FXML private TextField txtModelo;
    @FXML private ComboBox<String> cmbTipoConexao;
    @FXML private ComboBox<String> cmbDriver;
    @FXML private ComboBox<Integer> cmbColunas;
    @FXML private ComboBox<String> cmbCodificacao;

    // Componentes FXML - Configurações de Conexão
    @FXML private TextField txtPortaUSB;
    @FXML private ComboBox<String> cmbPortaSerial;
    @FXML private ComboBox<Integer> cmbBaudRate;
    @FXML private TextField txtIP;
    @FXML private TextField txtPortaRede;
    @FXML private ComboBox<String> cmbDispositivoBT;

    // Componentes FXML - Recursos Adicionais
    @FXML private CheckBox chkCortaPapel;
    @FXML private CheckBox chkGavetaDinheiro;
    @FXML private CheckBox chkImpressoraCupom;
    @FXML private ComboBox<String> cmbDisplayCliente;

    // Componentes FXML - Configurações Avançadas
    @FXML private TextArea txtConfiguracaoAvancada;

    // Componentes FXML - Botões de Configuração
    @FXML private Button btnSalvar;
    @FXML private Button btnRestaurar;
    @FXML private Button btnTestarConexao;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        inicializarComponentes();
        inicializarComboBoxes();
        inicializarTimer();
        carregarConfiguracao();
    }

    private void inicializarComponentes() {
        this.impressaoService = new ImpressaoNaoFiscalService();
        atualizarStatus();
    }

    private void inicializarComboBoxes() {
        // Fabricantes
        ObservableList<String> fabricantes = FXCollections.observableArrayList(
            "Epson", "Bematech", "Elgin", "Daruma", "Citizen", "Custom", "Outro"
        );
        cmbFabricante.setItems(fabricantes);
        cmbFabricante.getSelectionModel().select("Epson");

        // Tipo de Conexão
        ObservableList<String> tiposConexao = FXCollections.observableArrayList(
            "USB", "SERIAL", "REDE", "BLUETOOTH"
        );
        cmbTipoConexao.setItems(tiposConexao);
        cmbTipoConexao.getSelectionModel().select("USB");

        // Drivers
        ObservableList<String> drivers = FXCollections.observableArrayList(
            "ESCPOS", "EPSON", "CUSTOM"
        );
        cmbDriver.setItems(drivers);
        cmbDriver.getSelectionModel().select("ESCPOS");

        // Colunas
        ObservableList<Integer> colunas = FXCollections.observableArrayList(
            40, 48, 80
        );
        cmbColunas.setItems(colunas);
        cmbColunas.getSelectionModel().select(48);

        // Codificação
        ObservableList<String> codificacoes = FXCollections.observableArrayList(
            "UTF-8", "ISO-8859-1", "CP850", "ASCII"
        );
        cmbCodificacao.setItems(codificacoes);
        cmbCodificacao.getSelectionModel().select("UTF-8");

        // Portas Seriais
        ObservableList<String> portasSeriais = FXCollections.observableArrayList(
            "COM1", "COM2", "COM3", "COM4", "COM5", "COM6", "COM7", "COM8",
            "/dev/ttyS0", "/dev/ttyS1", "/dev/ttyUSB0", "/dev/ttyUSB1"
        );
        cmbPortaSerial.setItems(portasSeriais);
        cmbPortaSerial.getSelectionModel().select("COM1");

        // Baud Rate
        ObservableList<Integer> baudRates = FXCollections.observableArrayList(
            9600, 19200, 38400, 57600, 115200
        );
        cmbBaudRate.setItems(baudRates);
        cmbBaudRate.getSelectionModel().select(9600);

        // Dispositivos Bluetooth
        ObservableList<String> dispositivosBT = FXCollections.observableArrayList(
            "BT001", "BT002", "BT003", "Personalizar..."
        );
        cmbDispositivoBT.setItems(dispositivosBT);

        // Display Cliente
        ObservableList<String> displays = FXCollections.observableArrayList(
            "NONE", "LCD_16x2", "LCD_20x4"
        );
        cmbDisplayCliente.setItems(displays);
        cmbDisplayCliente.getSelectionModel().select("NONE");

        // Listeners para habilitar/desabilitar campos
        cmbTipoConexao.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            atualizarCamposConexao();
        });
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

    private void atualizarCamposConexao() {
        String tipoConexao = cmbTipoConexao.getSelectionModel().getSelectedItem();
        
        // Habilitar/desabilitar campos conforme o tipo de conexão
        boolean isUSB = "USB".equals(tipoConexao);
        boolean isSerial = "SERIAL".equals(tipoConexao);
        boolean isRede = "REDE".equals(tipoConexao);
        boolean isBluetooth = "BLUETOOTH".equals(tipoConexao);

        txtPortaUSB.setDisable(!isUSB);
        cmbPortaSerial.setDisable(!isSerial);
        cmbBaudRate.setDisable(!isSerial);
        txtIP.setDisable(!isRede);
        txtPortaRede.setDisable(!isRede);
        cmbDispositivoBT.setDisable(!isBluetooth);
    }

    private void carregarConfiguracao() {
        ImpressoraNaoFiscal impressora = impressaoService.getImpressoraConfigurada();
        if (impressora != null) {
            // Configurações básicas
            txtNome.setText(impressora.getNome() != null ? impressora.getNome() : "");
            txtModelo.setText(impressora.getModelo() != null ? impressora.getModelo() : "");
            
            if (impressora.getFabricante() != null) {
                cmbFabricante.getSelectionModel().select(impressora.getFabricante());
            }
            
            if (impressora.getTipoConexao() != null) {
                cmbTipoConexao.getSelectionModel().select(impressora.getTipoConexao());
            }
            
            if (impressora.getDriver() != null) {
                cmbDriver.getSelectionModel().select(impressora.getDriver());
            }
            
            if (impressora.getColunas() != null) {
                cmbColunas.getSelectionModel().select(impressora.getColunas());
            }
            
            if (impressora.getCodificacao() != null) {
                cmbCodificacao.getSelectionModel().select(impressora.getCodificacao());
            }

            // Configurações de conexão
            txtPortaUSB.setText(impressora.getPorta() != null ? impressora.getPorta() : "");
            txtIP.setText(impressora.getIp() != null ? impressora.getIp() : "");
            
            if (impressora.getPortaRede() != null) {
                txtPortaRede.setText(impressora.getPortaRede().toString());
            }
            
            if (impressora.getBaudRate() != null) {
                cmbBaudRate.getSelectionModel().select(impressora.getBaudRate());
            }

            // Recursos adicionais
            chkCortaPapel.setSelected(impressora.isCortaPapel());
            chkGavetaDinheiro.setSelected(impressora.isGavetaDinheiro());
            chkImpressoraCupom.setSelected(impressora.isImpressoraCupom());
            
            if (impressora.getDisplayCliente() != null) {
                cmbDisplayCliente.getSelectionModel().select(impressora.getDisplayCliente());
            }

            // Configurações avançadas
            txtConfiguracaoAvancada.setText(impressora.getConfiguracaoAvancada() != null ? 
                impressora.getConfiguracaoAvancada() : "{\n  \"timeout\": 5000,\n  \"retry_count\": 3,\n  \"buffer_size\": 1024\n}");

            atualizarCamposConexao();
        }
    }

    private void salvarConfiguracao() {
        ImpressoraNaoFiscal impressora = new ImpressoraNaoFiscal();
        
        // Configurações básicas
        impressora.setNome(txtNome.getText());
        impressora.setModelo(txtModelo.getText());
        impressora.setFabricante(cmbFabricante.getSelectionModel().getSelectedItem());
        impressora.setTipoConexao(cmbTipoConexao.getSelectionModel().getSelectedItem());
        impressora.setDriver(cmbDriver.getSelectionModel().getSelectedItem());
        impressora.setColunas(cmbColunas.getSelectionModel().getSelectedItem());
        impressora.setCodificacao(cmbCodificacao.getSelectionModel().getSelectedItem());

        // Configurações de conexão
        impressora.setPorta(txtPortaUSB.getText());
        impressora.setIp(txtIP.getText());
        
        try {
            String portaRedeStr = txtPortaRede.getText();
            if (!portaRedeStr.isEmpty()) {
                impressora.setPortaRede(Integer.parseInt(portaRedeStr));
            }
        } catch (NumberFormatException e) {
            impressora.setPortaRede(9100);
        }
        
        impressora.setBaudRate(cmbBaudRate.getSelectionModel().getSelectedItem());
        impressora.setDataBits(8);
        impressora.setStopBits(1);
        impressora.setParidade("NONE");

        // Recursos adicionais
        impressora.setCortaPapel(chkCortaPapel.isSelected());
        impressora.setGavetaDinheiro(chkGavetaDinheiro.isSelected());
        impressora.setImpressoraCupom(chkImpressoraCupom.isSelected());
        impressora.setDisplayCliente(cmbDisplayCliente.getSelectionModel().getSelectedItem());

        // Configurações avançadas
        impressora.setConfiguracaoAvancada(txtConfiguracaoAvancada.getText());

        // Atualizar serviço
        impressaoService.setImpressoraConfigurada(impressora);
        
        mostrarAlerta("Configuração salva com sucesso!", Alert.AlertType.INFORMATION);
    }

    private void atualizarStatus() {
        ImpressoraNaoFiscal impressora = impressaoService.getImpressoraConfigurada();
        if (impressora != null) {
            lblNomeImpressora.setText(impressora.getDescricaoCompleta());
            
            if (impressora.isStatusOnline()) {
                lblStatus.setText("Online");
                lblStatus.setStyle("-fx-text-fill: #27ae60; -fx-font-weight: bold;");
            } else {
                lblStatus.setText("Offline");
                lblStatus.setStyle("-fx-text-fill: #e74c3c; -fx-font-weight: bold;");
            }
            
            if (impressora.getUltimaVerificacao() != null) {
                lblUltimaVerificacao.setText(impressora.getUltimaVerificacao().format(formatadorDataHora));
            } else {
                lblUltimaVerificacao.setText("Nunca");
            }
            
            lblMensagemStatus.setText(impressora.getMensagemStatus() != null ? impressora.getMensagemStatus() : "Não verificado");
        }
    }

    // Métodos de Ação
    @FXML
    private void onVoltar() {
        try {
            timer.cancel();
            impressaoService.desconectar();
            javafx.stage.Stage stage = (javafx.stage.Stage) btnVoltar.getScene().getWindow();
            stage.close();
        } catch (Exception e) {
            System.err.println("Erro ao voltar: " + e.getMessage());
        }
    }

    @FXML
    private void onVerificarStatus() {
        try {
            boolean status = impressaoService.verificarStatus();
            atualizarStatus();
            
            if (status) {
                mostrarAlerta("Impressora online e operacional!", Alert.AlertType.INFORMATION);
            } else {
                mostrarAlerta("Impressora offline ou com erro!", Alert.AlertType.WARNING);
            }
        } catch (Exception e) {
            mostrarAlerta("Erro ao verificar status: " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    @FXML
    private void onTestarImpressao() {
        try {
            // Criar uma venda simulada para teste
            VendaPDV vendaTeste = criarVendaTeste();
            
            boolean sucesso = impressaoService.imprimirCupomVenda(vendaTeste);
            
            if (sucesso) {
                mostrarAlerta("Teste de impressão realizado com sucesso!", Alert.AlertType.INFORMATION);
            } else {
                mostrarAlerta("Falha no teste de impressão!", Alert.AlertType.ERROR);
            }
        } catch (Exception e) {
            mostrarAlerta("Erro no teste de impressão: " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    @FXML
    private void onAbrirGaveta() {
        try {
            boolean sucesso = impressaoService.abrirGaveta();
            
            if (sucesso) {
                mostrarAlerta("Gaveta aberta com sucesso!", Alert.AlertType.INFORMATION);
            } else {
                mostrarAlerta("Falha ao abrir gaveta!", Alert.AlertType.ERROR);
            }
        } catch (Exception e) {
            mostrarAlerta("Erro ao abrir gaveta: " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    @FXML
    private void onConectar() {
        try {
            boolean sucesso = impressaoService.conectar();
            
            if (sucesso) {
                atualizarStatus();
                mostrarAlerta("Conectado à impressora com sucesso!", Alert.AlertType.INFORMATION);
            } else {
                mostrarAlerta("Falha na conexão com a impressora!", Alert.AlertType.ERROR);
            }
        } catch (Exception e) {
            mostrarAlerta("Erro na conexão: " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    @FXML
    private void onDesconectar() {
        try {
            impressaoService.desconectar();
            atualizarStatus();
            mostrarAlerta("Desconectado da impressora!", Alert.AlertType.INFORMATION);
        } catch (Exception e) {
            mostrarAlerta("Erro ao desconectar: " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    @FXML
    private void onSalvar() {
        try {
            salvarConfiguracao();
            atualizarStatus();
        } catch (Exception e) {
            mostrarAlerta("Erro ao salvar configuração: " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    @FXML
    private void onRestaurar() {
        try {
            // Restaurar configuração padrão
            ImpressoraNaoFiscal impressoraPadrao = new ImpressoraNaoFiscal();
            impressaoService.setImpressoraConfigurada(impressoraPadrao);
            
            // Recarregar campos
            carregarConfiguracao();
            atualizarStatus();
            
            mostrarAlerta("Configuração restaurada para o padrão!", Alert.AlertType.INFORMATION);
        } catch (Exception e) {
            mostrarAlerta("Erro ao restaurar configuração: " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    @FXML
    private void onTestarConexao() {
        try {
            // Salvar configuração atual
            salvarConfiguracao();
            
            // Tentar conectar
            boolean sucesso = impressaoService.conectar();
            
            if (sucesso) {
                mostrarAlerta("Conexão testada com sucesso!", Alert.AlertType.INFORMATION);
                impressaoService.desconectar();
            } else {
                mostrarAlerta("Falha no teste de conexão!", Alert.AlertType.ERROR);
            }
            
            atualizarStatus();
        } catch (Exception e) {
            mostrarAlerta("Erro no teste de conexão: " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    private VendaPDV criarVendaTeste() {
        VendaPDV venda = new VendaPDV();
        venda.setNumeroCupom("TESTE_" + System.currentTimeMillis());
        venda.setDataVenda(LocalDateTime.now());
        venda.setOperador(operadorAtual);
        
        // Criar um item de teste
        com.br.hermescomercial.model.Produto produto = new com.br.hermescomercial.model.Produto();
        produto.setNome("PRODUTO DE TESTE");
        produto.setPrecoVenda(new java.math.BigDecimal("10.00"));
        
        com.br.hermescomercial.model.ItemVenda item = new com.br.hermescomercial.model.ItemVenda();
        item.setProduto(produto);
        item.setQuantidade(1);
        item.setValorUnitario(new java.math.BigDecimal("10.00"));
        item.setDesconto(java.math.BigDecimal.ZERO);
        
        java.util.List<com.br.hermescomercial.model.ItemVenda> itens = new java.util.ArrayList<>();
        itens.add(item);
        venda.setItens(itens);
        
        venda.setValorTotal(new java.math.BigDecimal("10.00"));
        venda.setValorFinal(new java.math.BigDecimal("10.00"));
        
        com.br.hermescomercial.model.Pagamento pagamento = new com.br.hermescomercial.model.Pagamento();
        pagamento.setTipoPagamento("DINHEIRO");
        pagamento.setValorPago(new java.math.BigDecimal("10.00"));
        venda.setPagamento(pagamento);
        
        return venda;
    }

    private void mostrarAlerta(String mensagem, Alert.AlertType tipo) {
        Alert alert = new Alert(tipo);
        alert.setTitle("Config. Impressora");
        alert.setHeaderText(null);
        alert.setContentText(mensagem);
        alert.showAndWait();
    }

    public void setOperadorAtual(Usuario operadorAtual) {
        this.operadorAtual = operadorAtual;
        if (lblOperador != null) {
            lblOperador.setText(operadorAtual.getNome());
        }
    }
}
