package com.br.hermescomercial.controller;

import com.br.hermescomercial.config.DatabaseConfig;
import com.br.hermescomercial.dao.DatabaseFactory;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Controller para tela de configuração de banco de dados
 * @author marcos
 */
public class DatabaseConfigController {
    private static final Logger logger = LogManager.getLogger(DatabaseConfigController.class);
    
    @FXML private Label lblCurrentDatabase;
    @FXML private Label lblDatabaseDescription;
    @FXML private RadioButton rbPostgreSQL;
    @FXML private RadioButton rbExcel;
    @FXML private RadioButton rbSQLite;
    @FXML private ToggleGroup databaseGroup;
    @FXML private VBox infoBox;
    @FXML private Label lblInfoTitle;
    @FXML private Label lblInfoContent;
    @FXML private Button btnTestConnection;
    @FXML private Button btnSave;
    @FXML private Button btnCancel;
    @FXML private Label lblStatus;
    
    private DatabaseConfig config;
        
    @FXML
    public void initialize() {
        config = DatabaseConfig.getInstance();
        
        // Carregar configuração atual
        loadCurrentConfiguration();
        
        // Configurar listeners
        configureListeners();
        
        // Atualizar interface
        updateUI();
    }
    
    private void loadCurrentConfiguration() {
        DatabaseConfig.DatabaseType currentType = config.getDatabaseType();
        
        switch (currentType) {
            case POSTGRESQL:
                rbPostgreSQL.setSelected(true);
                break;
            case EXCEL:
                rbExcel.setSelected(true);
                break;
            case SQLITE:
                rbSQLite.setSelected(true);
                break;
        }
    }
    
    private void configureListeners() {
        databaseGroup.selectedToggleProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                updateInfoBox();
                btnSave.setDisable(false);
            }
        });
        
        rbPostgreSQL.selectedProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal) {
                updateInfoBox();
            }
        });
        
        rbExcel.selectedProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal) {
                updateInfoBox();
            }
        });
        
        rbSQLite.selectedProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal) {
                updateInfoBox();
            }
        });
    }
    
    private void updateUI() {
        lblCurrentDatabase.setText(config.getDatabaseName());
        lblDatabaseDescription.setText(config.getDatabaseDescription());
        
        updateInfoBox();
        btnSave.setDisable(false);
    }
    
    private void updateInfoBox() {
        if (rbPostgreSQL.isSelected()) {
            showInfoBox("PostgreSQL", 
                "Banco de dados robusto e multiusuário.\n\n" +
                "Requisitos:\n" +
                "• PostgreSQL instalado localmente\n" +
                "• Banco: hermescomercialdb\n" +
                "• Usuário: hermesuser\n" +
                "• Porta: 5432\n\n" +
                "Vantagens:\n" +
                "• Alta performance\n" +
                "• Suporte a múltiplos usuários\n" +
                "• Transações ACID\n" +
                "• Backup e recuperação avançados");
                
        } else if (rbExcel.isSelected()) {
            showInfoBox("Excel", 
                "Arquivos Excel como banco de dados.\n\n" +
                "Requisitos:\n" +
                "• Nenhum (apenas o sistema)\n" +
                "• Diretório ./data/excel/\n\n" +
                "Vantagens:\n" +
                "• Simples de usar\n" +
                "• Portátil\n" +
                "• Fácil backup (copiar arquivos)\n" +
                "• Visualização no Excel\n\n" +
                "Limitações:\n" +
                "• Indicado para pequenos volumes\n" +
                "• Performance limitada\n" +
                "• Acesso simultâneo limitado");
                
        } else if (rbSQLite.isSelected()) {
            showInfoBox("SQLite", 
                "Banco de dados em arquivo único.\n\n" +
                "Requisitos:\n" +
                "• Nenhum (apenas o sistema)\n" +
                "• Arquivo ./hermes_comercial.db\n\n" +
                "Vantagens:\n" +
                "• Leve e rápido\n" +
                "• Servidor embutido\n" +
                "• Portátil\n" +
                "• Zero configuração\n\n" +
                "Limitações:\n" +
                "• Acesso simultâneo limitado\n" +
                "• Menos recursos que PostgreSQL");
        }
    }
    
    private void showInfoBox(String title, String content) {
        lblInfoTitle.setText(title);
        lblInfoContent.setText(content);
        infoBox.setVisible(true);
    }
    
    @FXML
    private void testConnection() {
        lblStatus.setText("Testando conexão...");
        btnTestConnection.setDisable(true);
        
        // Testar em thread separada para não travar UI
        new Thread(() -> {
            try {
                boolean success = DatabaseFactory.testConnection();
                
                javafx.application.Platform.runLater(() -> {
                    if (success) {
                        lblStatus.setText("✓ Conexão testada com sucesso!");
                        lblStatus.setStyle("-fx-text-fill: #4caf50;");
                    } else {
                        lblStatus.setText("✗ Falha na conexão. Verifique a configuração.");
                        lblStatus.setStyle("-fx-text-fill: #f44336;");
                    }
                    btnTestConnection.setDisable(false);
                });
                
            } catch (Exception e) {
                logger.error("Erro ao testar conexão: " + e.getMessage(), e);
                javafx.application.Platform.runLater(() -> {
                    lblStatus.setText("✗ Erro ao testar conexão: " + e.getMessage());
                    lblStatus.setStyle("-fx-text-fill: #f44336;");
                    btnTestConnection.setDisable(false);
                });
            }
        }).start();
    }
    
    @FXML
    private void saveConfiguration() {
        DatabaseConfig.DatabaseType selectedType = null;
        
        if (rbPostgreSQL.isSelected()) {
            selectedType = DatabaseConfig.DatabaseType.POSTGRESQL;
        } else if (rbExcel.isSelected()) {
            selectedType = DatabaseConfig.DatabaseType.EXCEL;
        } else if (rbSQLite.isSelected()) {
            selectedType = DatabaseConfig.DatabaseType.SQLITE;
        }
        
        if (selectedType != null && selectedType != config.getDatabaseType()) {
            // Confirmar mudança
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Confirmar Mudança");
            alert.setHeaderText("Alterar tipo de banco de dados?");
            alert.setContentText("Isso irá alterar o tipo de banco de dados usado pelo sistema.\n\n" +
                               "Tipo atual: " + config.getDatabaseName() + "\n" +
                               "Novo tipo: " + selectedType.getDisplayName() + "\n\n" +
                               "Deseja continuar?");
            
            if (alert.showAndWait().get() == ButtonType.OK) {
                try {
                    // Salvar nova configuração
                    config.setDatabaseType(selectedType);
                    
                    // Resetar conexões
                    DatabaseFactory.resetConnections();
                    
                    // Atualizar UI
                    updateUI();
                    
                    lblStatus.setText("✓ Configuração salva com sucesso!");
                    lblStatus.setStyle("-fx-text-fill: #4caf50;");
                    
                    logger.info("Configuração de banco alterada para: " + selectedType.getDisplayName());
                    
                    // Mostrar mensagem de sucesso
                    Alert successAlert = new Alert(Alert.AlertType.INFORMATION);
                    successAlert.setTitle("Configuração Salva");
                    successAlert.setHeaderText("Configuração salva com sucesso!");
                    successAlert.setContentText("O sistema usará " + selectedType.getDisplayName() + 
                                             " como banco de dados.\n\n" +
                                             "Reinicie o aplicativo para aplicar as mudanças.");
                    successAlert.showAndWait();
                    
                } catch (Exception e) {
                    logger.error("Erro ao salvar configuração: " + e.getMessage(), e);
                    lblStatus.setText("✗ Erro ao salvar configuração: " + e.getMessage());
                    lblStatus.setStyle("-fx-text-fill: #f44336;");
                }
            }
        } else {
            lblStatus.setText("Nenhuma alteração necessária.");
            lblStatus.setStyle("-fx-text-fill: #ff9800;");
        }
    }
    
    @FXML
    private void cancel() {
        // Fechar janela
        lblStatus.getScene().getWindow().hide();
    }
}
