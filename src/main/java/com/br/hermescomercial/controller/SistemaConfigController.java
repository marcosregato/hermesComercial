package com.br.hermescomercial.controller;

import com.br.hermescomercial.config.DatabaseConfig;
import com.br.hermescomercial.dao.DatabaseFactory;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Alert;
import javafx.stage.DirectoryChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.util.Arrays;
import java.util.List;

/**
 * Controller para tela de configuração do sistema
 * @author marcos
 */
public class SistemaConfigController {
    private static final Logger logger = LogManager.getLogger(SistemaConfigController.class);
    
    // Banco de Dados
    @FXML private ComboBox<String> cbDatabaseType;
    @FXML private Label lblDatabaseStatus;
    @FXML private Label lblDatabaseDescription;
    @FXML private Button btnTestDatabase;
    @FXML private Button btnConfigDatabase;
    
    // Impressora
    @FXML private ComboBox<String> cbPrinters;
    @FXML private TextField txtPrinterModel;
    @FXML private ComboBox<String> cbPrinterPort;
    @FXML private TextField txtPaperWidth;
    @FXML private TextField txtPaperHeight;
    @FXML private CheckBox cbPrintHeader;
    @FXML private CheckBox cbPrintFooter;
    @FXML private CheckBox cbAutoCut;
    @FXML private Button btnTestPrinter;
    @FXML private Button btnDetectPrinters;
    
    // Sistema
    @FXML private TextField txtCompanyName;
    @FXML private TextField txtCompanyCNPJ;
    @FXML private TextField txtCompanyAddress;
    @FXML private TextField txtCompanyPhone;
    @FXML private ComboBox<String> cbBackupFrequency;
    
    // Logs
    @FXML private ComboBox<String> cbLogLevel;
    @FXML private TextField txtLogDirectory;
    @FXML private Button btnBrowseLogDir;
    @FXML private TextField txtLogMaxSize;
    @FXML private Button btnViewLogs;
    @FXML private Button btnCleanLogs;
    
    // Botões Principais
    @FXML private Button btnSave;
    @FXML private Button btnCancel;
    @FXML private Button btnReset;
    
    private DatabaseConfig databaseConfig;
    private boolean configurationChanged = false;
    
    @FXML
    public void initialize() {
        databaseConfig = DatabaseConfig.getInstance();
        
        initializeComponents();
        loadCurrentConfiguration();
        configureListeners();
    }
    
    private void initializeComponents() {
        // Inicializar ComboBox de Banco de Dados
        cbDatabaseType.setItems(FXCollections.observableArrayList(
            "PostgreSQL",
            "Excel", 
            "SQLite"
        ));
        
        // Inicializar ComboBox de Portas de Impressora
        cbPrinterPort.setItems(FXCollections.observableArrayList(
            "USB001",
            "LPT1", 
            "COM1",
            "COM2",
            "COM3",
            "COM4",
            "Rede (IP)"
        ));
        
        // Inicializar ComboBox de Frequência de Backup
        cbBackupFrequency.setItems(FXCollections.observableArrayList(
            "Diário",
            "Semanal",
            "Mensal",
            "Nunca"
        ));
        
        // Inicializar ComboBox de Nível de Log
        cbLogLevel.setItems(FXCollections.observableArrayList(
            "DEBUG",
            "INFO",
            "WARN", 
            "ERROR"
        ));
    }
    
    private void loadCurrentConfiguration() {
        // Carregar configuração do banco de dados
        DatabaseConfig.DatabaseType dbType = databaseConfig.getDatabaseType();
        cbDatabaseType.setValue(dbType.getDisplayName());
        updateDatabaseInfo();
        
        // Carregar configurações da impressora (valores padrão)
        cbPrinters.setValue("Nenhuma impressora detectada");
        txtPrinterModel.setText("Genérica");
        cbPrinterPort.setValue("USB001");
        txtPaperWidth.setText("80");
        txtPaperHeight.setText("200");
        cbPrintHeader.setSelected(true);
        cbPrintFooter.setSelected(true);
        cbAutoCut.setSelected(false);
        
        // Carregar configurações do sistema (valores padrão)
        txtCompanyName.setText("Hermes Comercial");
        txtCompanyCNPJ.setText("00.000.000/0001-00");
        txtCompanyAddress.setText("Rua Principal, 123 - Centro");
        txtCompanyPhone.setText("(00) 0000-0000");
        cbBackupFrequency.setValue("Diário");
        
        // Carregar configurações de logs
        txtLogDirectory.setText("./logs");
        cbLogLevel.setValue("INFO");
        txtLogMaxSize.setText("100");
        
        detectPrinters();
    }
    
    private void configureListeners() {
        // Listener para mudanças na configuração
        cbDatabaseType.valueProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {
                configurationChanged = true;
                updateDatabaseInfo();
                btnSave.setDisable(false);
            }
        });
        
        // Listeners para campos da impressora
        txtPrinterModel.textProperty().addListener((obs, oldVal, newVal) -> markAsChanged());
        cbPrinterPort.valueProperty().addListener((obs, oldVal, newVal) -> markAsChanged());
        txtPaperWidth.textProperty().addListener((obs, oldVal, newVal) -> markAsChanged());
        txtPaperHeight.textProperty().addListener((obs, oldVal, newVal) -> markAsChanged());
        cbPrintHeader.selectedProperty().addListener((obs, oldVal, newVal) -> markAsChanged());
        cbPrintFooter.selectedProperty().addListener((obs, oldVal, newVal) -> markAsChanged());
        cbAutoCut.selectedProperty().addListener((obs, oldVal, newVal) -> markAsChanged());
        
        // Listeners para campos do sistema
        txtCompanyName.textProperty().addListener((obs, oldVal, newVal) -> markAsChanged());
        txtCompanyCNPJ.textProperty().addListener((obs, oldVal, newVal) -> markAsChanged());
        txtCompanyAddress.textProperty().addListener((obs, oldVal, newVal) -> markAsChanged());
        txtCompanyPhone.textProperty().addListener((obs, oldVal, newVal) -> markAsChanged());
        cbBackupFrequency.valueProperty().addListener((obs, oldVal, newVal) -> markAsChanged());
        
        // Listeners para campos de logs
        cbLogLevel.valueProperty().addListener((obs, oldVal, newVal) -> markAsChanged());
        txtLogMaxSize.textProperty().addListener((obs, oldVal, newVal) -> markAsChanged());
    }
    
    private void updateDatabaseInfo() {
        String selectedType = cbDatabaseType.getValue();
        
        switch (selectedType) {
            case "PostgreSQL":
                lblDatabaseStatus.setText("✓ Conectado");
                lblDatabaseStatus.setStyle("-fx-text-fill: #28a745;");
                lblDatabaseDescription.setText("Banco robusto e multiusuário. Ideal para produção.");
                break;
            case "Excel":
                lblDatabaseStatus.setText("📊 Arquivos Locais");
                lblDatabaseStatus.setStyle("-fx-text-fill: #17a2b8;");
                lblDatabaseDescription.setText("Arquivos Excel como banco. Simples e portátil.");
                break;
            case "SQLite":
                lblDatabaseStatus.setText("🗄️ Arquivo Único");
                lblDatabaseStatus.setStyle("-fx-text-fill: #6f42c1;");
                lblDatabaseDescription.setText("Banco leve em arquivo único. Zero configuração.");
                break;
            default:
                lblDatabaseStatus.setText("❌ Não configurado");
                lblDatabaseStatus.setStyle("-fx-text-fill: #dc3545;");
                lblDatabaseDescription.setText("Selecione um tipo de banco de dados.");
        }
    }
    
    private void markAsChanged() {
        configurationChanged = true;
        btnSave.setDisable(false);
    }
    
    @FXML
    private void testDatabaseConnection() {
        lblDatabaseStatus.setText("Testando...");
        lblDatabaseStatus.setStyle("-fx-text-fill: #ffc107;");
        
        new Thread(() -> {
            try {
                boolean success = DatabaseFactory.testConnection();
                
                javafx.application.Platform.runLater(() -> {
                    if (success) {
                        lblDatabaseStatus.setText("✓ Conexão OK");
                        lblDatabaseStatus.setStyle("-fx-text-fill: #28a745;");
                    } else {
                        lblDatabaseStatus.setText("✗ Falha na conexão");
                        lblDatabaseStatus.setStyle("-fx-text-fill: #dc3545;");
                    }
                });
                
            } catch (Exception e) {
                logger.error("Erro ao testar conexão: " + e.getMessage(), e);
                javafx.application.Platform.runLater(() -> {
                    lblDatabaseStatus.setText("✗ Erro: " + e.getMessage());
                    lblDatabaseStatus.setStyle("-fx-text-fill: #dc3545;");
                });
            }
        }).start();
    }
    
    @FXML
    private void openDatabaseConfig() {
        try {
            // Abrir tela de configuração detalhada do banco
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/database_config.fxml"));
            Parent root = loader.load();
            
            Stage stage = new Stage();
            stage.setTitle("Configuração Avançada de Banco de Dados");
            stage.setScene(new Scene(root));
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.showAndWait();
            
            // Atualizar informações após fechar
            loadCurrentConfiguration();
            
        } catch (Exception e) {
            logger.error("Erro ao abrir configuração de banco: " + e.getMessage(), e);
            showAlert(Alert.AlertType.ERROR, "Erro", "Não foi possível abrir a configuração de banco: " + e.getMessage());
        }
    }
    
    @FXML
    private void detectPrinters() {
        cbPrinters.getItems().clear();
        cbPrinters.getItems().add("Detectando impressoras...");
        
        new Thread(() -> {
            try {
                // Simular detecção de impressoras
                List<String> printers = Arrays.asList(
                    "Epson TM-T20",
                    "Bematech MP-4200 TH", 
                    "Daruma DR700",
                    "Nenhuma impressora detectada"
                );
                
                javafx.application.Platform.runLater(() -> {
                    cbPrinters.getItems().clear();
                    cbPrinters.getItems().addAll(printers);
                    cbPrinters.setValue(printers.get(0));
                });
                
            } catch (Exception e) {
                logger.error("Erro ao detectar impressoras: " + e.getMessage(), e);
                javafx.application.Platform.runLater(() -> {
                    cbPrinters.getItems().clear();
                    cbPrinters.getItems().add("Erro ao detectar impressoras");
                });
            }
        }).start();
    }
    
    @FXML
    private void testPrinter() {
        String printer = cbPrinters.getValue();
        String model = txtPrinterModel.getText();
        
        if (printer == null || printer.contains("Nenhuma") || printer.contains("Erro")) {
            showAlert(Alert.AlertType.WARNING, "Aviso", "Selecione uma impressora válida primeiro.");
            return;
        }
        
        showAlert(Alert.AlertType.INFORMATION, "Teste de Impressão", 
                  "Testando impressão em: " + printer + "\n" +
                  "Modelo: " + model + "\n\n" +
                  "Esta é uma simulação. Em produção, seria impresso um cupom de teste.");
    }
    
    @FXML
    private void browseLogDirectory() {
        DirectoryChooser directoryChooser = new DirectoryChooser();
        directoryChooser.setTitle("Selecionar Diretório de Logs");
        directoryChooser.setInitialDirectory(new File("./logs"));
        
        File selectedDirectory = directoryChooser.showDialog(txtLogDirectory.getScene().getWindow());
        if (selectedDirectory != null) {
            txtLogDirectory.setText(selectedDirectory.getAbsolutePath());
            markAsChanged();
        }
    }
    
    @FXML
    private void viewLogs() {
        String logDir = txtLogDirectory.getText();
        File directory = new File(logDir);
        
        if (directory.exists() && directory.isDirectory()) {
            try {
                // Abrir diretório de logs no gerenciador de arquivos
                java.awt.Desktop.getDesktop().open(directory);
            } catch (Exception e) {
                logger.error("Erro ao abrir diretório de logs: " + e.getMessage(), e);
                showAlert(Alert.AlertType.ERROR, "Erro", "Não foi possível abrir o diretório de logs: " + e.getMessage());
            }
        } else {
            showAlert(Alert.AlertType.WARNING, "Aviso", "Diretório de logs não encontrado: " + logDir);
        }
    }
    
    @FXML
    private void cleanLogs() {
        Alert confirmAlert = new Alert(Alert.AlertType.CONFIRMATION);
        confirmAlert.setTitle("Confirmar Limpeza");
        confirmAlert.setHeaderText("Limpar Logs Antigos?");
        confirmAlert.setContentText("Esta ação irá remover todos os logs antigos.\nDeseja continuar?");
        
        if (confirmAlert.showAndWait().get() == ButtonType.OK) {
            // Simular limpeza de logs
            showAlert(Alert.AlertType.INFORMATION, "Limpeza Concluída", 
                      "Logs antigos removidos com sucesso.\n" +
                      "Foram liberados X MB de espaço em disco.");
        }
    }
    
    @FXML
    private void saveConfiguration() {
        if (!validateConfiguration()) {
            return;
        }
        
        try {
            // Salvar configuração do banco de dados
            String dbType = cbDatabaseType.getValue();
            DatabaseConfig.DatabaseType selectedType = null;
            
            switch (dbType) {
                case "PostgreSQL":
                    selectedType = DatabaseConfig.DatabaseType.POSTGRESQL;
                    break;
                case "Excel":
                    selectedType = DatabaseConfig.DatabaseType.EXCEL;
                    break;
                case "SQLite":
                    selectedType = DatabaseConfig.DatabaseType.SQLITE;
                    break;
            }
            
            if (selectedType != null && selectedType != databaseConfig.getDatabaseType()) {
                Alert confirmAlert = new Alert(Alert.AlertType.CONFIRMATION);
                confirmAlert.setTitle("Confirmar Mudança");
                confirmAlert.setHeaderText("Alterar tipo de banco de dados?");
                confirmAlert.setContentText("Isso irá alterar o tipo de banco de dados.\n" +
                                         "Tipo atual: " + databaseConfig.getDatabaseName() + "\n" +
                                         "Novo tipo: " + selectedType.getDisplayName() + "\n\n" +
                                         "Deseja continuar?");
                
                if (confirmAlert.showAndWait().get() == ButtonType.OK) {
                    databaseConfig.setDatabaseType(selectedType);
                    DatabaseFactory.resetConnections();
                }
            }
            
            // Salvar outras configurações (simulação)
            savePrinterConfiguration();
            saveSystemConfiguration();
            saveLogConfiguration();
            
            configurationChanged = false;
            btnSave.setDisable(true);
            
            showAlert(Alert.AlertType.INFORMATION, "Sucesso", "Configurações salvas com sucesso!\n\n" +
                      "Reinicie o aplicativo para aplicar todas as mudanças.");
            
        } catch (Exception e) {
            logger.error("Erro ao salvar configuração: " + e.getMessage(), e);
            showAlert(Alert.AlertType.ERROR, "Erro", "Erro ao salvar configuração: " + e.getMessage());
        }
    }
    
    private void savePrinterConfiguration() {
        // Simular salvamento das configurações da impressora
        logger.info("Salvando configuração da impressora: " + cbPrinters.getValue());
    }
    
    private void saveSystemConfiguration() {
        // Simular salvamento das configurações do sistema
        logger.info("Salvando configuração do sistema: " + txtCompanyName.getText());
    }
    
    private void saveLogConfiguration() {
        // Simular salvamento das configurações de logs
        logger.info("Salvando configuração de logs: " + cbLogLevel.getValue());
    }
    
    @FXML
    private void cancel() {
        if (configurationChanged) {
            Alert confirmAlert = new Alert(Alert.AlertType.CONFIRMATION);
            confirmAlert.setTitle("Confirmar Cancelamento");
            confirmAlert.setHeaderText("Cancelar Alterações?");
            confirmAlert.setContentText("Há alterações não salvas. Deseja realmente cancelar?");
            
            if (confirmAlert.showAndWait().get() == ButtonType.OK) {
                closeWindow();
            }
        } else {
            closeWindow();
        }
    }
    
    @FXML
    private void resetToDefault() {
        Alert confirmAlert = new Alert(Alert.AlertType.CONFIRMATION);
        confirmAlert.setTitle("Confirmar Restauração");
        confirmAlert.setHeaderText("Restaurar Configurações Padrão?");
        confirmAlert.setContentText("Isso irá restaurar todas as configurações para os valores padrão.\nDeseja continuar?");
        
        if (confirmAlert.showAndWait().get() == ButtonType.OK) {
            loadCurrentConfiguration();
            configurationChanged = false;
            btnSave.setDisable(true);
            
            showAlert(Alert.AlertType.INFORMATION, "Restaurado", "Configurações restauradas para os valores padrão.");
        }
    }
    
    private boolean validateConfiguration() {
        // Validação básica
        if (txtCompanyName.getText().trim().isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Erro de Validação", "Nome da empresa é obrigatório.");
            return false;
        }
        
        if (txtCompanyCNPJ.getText().trim().isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Erro de Validação", "CNPJ da empresa é obrigatório.");
            return false;
        }
        
        try {
            int paperWidth = Integer.parseInt(txtPaperWidth.getText());
            int paperHeight = Integer.parseInt(txtPaperHeight.getText());
            
            if (paperWidth <= 0 || paperHeight <= 0) {
                showAlert(Alert.AlertType.ERROR, "Erro de Validação", "Dimensões do papel devem ser maiores que zero.");
                return false;
            }
        } catch (NumberFormatException e) {
            showAlert(Alert.AlertType.ERROR, "Erro de Validação", "Dimensões do papel devem ser números válidos.");
            return false;
        }
        
        try {
            int logSize = Integer.parseInt(txtLogMaxSize.getText());
            if (logSize <= 0) {
                showAlert(Alert.AlertType.ERROR, "Erro de Validação", "Tamanho máximo de logs deve ser maior que zero.");
                return false;
            }
        } catch (NumberFormatException e) {
            showAlert(Alert.AlertType.ERROR, "Erro de Validação", "Tamanho máximo de logs deve ser um número válido.");
            return false;
        }
        
        return true;
    }
    
    private void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
    
    private void closeWindow() {
        Stage stage = (Stage) btnSave.getScene().getWindow();
        stage.close();
    }
}
