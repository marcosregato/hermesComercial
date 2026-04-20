package com.br.hermescomercial.responsividade;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.scene.layout.HBox;
import javafx.scene.layout.BorderPane;
import javafx.stage.Screen;
import javafx.stage.Stage;

/**
 * Testes manuais para verificar a responsividade do Sistema PDV Hermes
 * Este arquivo pode ser executado diretamente para testes visuais
 */
public class ResponsividadeManualTest extends Application {

    private Stage primaryStage;
    private Scene scene;
    private BorderPane root;
    private Label statusLabel;

    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;
        
        // Configurar janela de teste
        primaryStage.setTitle("Teste de Responsividade - PDV Hermes");
        
        // Criar layout de teste
        criarLayoutTeste();
        
        // Configurar cena
        scene = new Scene(root);
        scene.getStylesheets().add(getClass().getResource("/css/pdv.css").toExternalForm());
        
        primaryStage.setScene(scene);
        
        // Aplicar configurações responsivas
        configurarJanelaResponsiva();
        
        primaryStage.show();
        
        // Iniciar testes automáticos
        new Thread(() -> {
            try {
                Thread.sleep(2000); // Aguardar carregamento
                executarTestesAutomaticos();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();
    }

    private void criarLayoutTeste() {
        root = new BorderPane();
        
        // Header responsivo
        VBox header = new VBox();
        header.getStyleClass().add("header-box");
        header.setStyle("-fx-background-color: #2c3e50; -fx-padding: 10;");
        
        HBox headerRow = new HBox(10);
        headerRow.getStyleClass().add("hbox-responsive");
        
        Label titulo = new Label("TESTE DE RESPONSIVIDADE");
        titulo.getStyleClass().addAll("header-title");
        titulo.setStyle("-fx-text-fill: white; -fx-font-weight: bold;");
        
        Label status = new Label("Status: Testando...");
        status.getStyleClass().addAll("header-label");
        status.setStyle("-fx-text-fill: white;");
        
        headerRow.getChildren().addAll(titulo, status);
        header.getChildren().add(headerRow);
        
        // Conteúdo principal
        VBox content = new VBox(20);
        content.getStyleClass().add("content-vbox");
        content.setStyle("-fx-padding: 20;");
        
        // Área de componentes responsivos
        VBox componentesArea = new VBox(15);
        componentesArea.getStyleClass().add("vbox-responsive");
        
        // Botões responsivos
        HBox botoesBox = new HBox(10);
        botoesBox.getStyleClass().add("hbox-responsive");
        
        Button btn1 = new Button("Botão 1");
        btn1.getStyleClass().addAll("button-primary", "button-responsive");
        
        Button btn2 = new Button("Botão 2");
        btn2.getStyleClass().addAll("button-success", "button-responsive");
        
        Button btn3 = new Button("Botão 3");
        btn3.getStyleClass().addAll("button-danger", "button-responsive");
        
        botoesBox.getChildren().addAll(btn1, btn2, btn3);
        
        // Campos responsivos
        HBox camposBox = new HBox(10);
        camposBox.getStyleClass().add("hbox-responsive");
        
        TextField campo1 = new TextField();
        campo1.getStyleClass().addAll("text-field-search", "text-field-search-responsive");
        campo1.setPromptText("Campo de busca responsivo");
        
        TextField campo2 = new TextField();
        campo2.getStyleClass().addAll("text-field-responsive");
        campo2.setPromptText("Campo normal responsivo");
        
        camposBox.getChildren().addAll(campo1, campo2);
        
        // Tabela responsiva
        TableView<String> tabela = new TableView<>();
        tabela.getStyleClass().addAll("table-view", "table-view-responsive");
        tabela.setPrefHeight(200);
        
        // Labels responsivos
        HBox labelsBox = new HBox(10);
        labelsBox.getStyleClass().add("hbox-responsive");
        
        Label label1 = new Label("Label responsivo 1");
        label1.getStyleClass().addAll("responsive-label");
        
        Label label2 = new Label("Label responsivo 2");
        label2.getStyleClass().addAll("responsive-label");
        
        labelsBox.getChildren().addAll(label1, label2);
        
        // Botões grandes responsivos
        HBox botoesGrandesBox = new HBox(15);
        botoesGrandesBox.getStyleClass().add("hbox-responsive");
        
        Button btnGrande1 = new Button("Grande 1");
        btnGrande1.getStyleClass().addAll("button-warning", "responsive-button-large");
        
        Button btnGrande2 = new Button("Grande 2");
        btnGrande2.getStyleClass().addAll("button-info", "responsive-button-large");
        
        botoesGrandesBox.getChildren().addAll(btnGrande1, btnGrande2);
        
        componentesArea.getChildren().addAll(
            new Label("=== BOTÕES RESPONSIVOS ==="),
            botoesBox,
            new Label("=== CAMPOS RESPONSIVOS ==="),
            camposBox,
            new Label("=== TABELA RESPONSIVA ==="),
            tabela,
            new Label("=== LABELS RESPONSIVOS ==="),
            labelsBox,
            new Label("=== BOTÕES GRANDES RESPONSIVOS ==="),
            botoesGrandesBox
        );
        
        content.getChildren().add(componentesArea);
        
        // Status panel
        VBox statusPanel = new VBox(10);
        statusPanel.getStyleClass().add("status-panel");
        statusPanel.setStyle("-fx-background-color: #f8f9fa; -fx-padding: 10; -fx-border-color: #dee2e6;");
        
        Label infoLabel = new Label("Informações de Teste:");
        infoLabel.setStyle("-fx-font-weight: bold;");
        
        statusLabel = new Label("Status: Inicializado");
        Label tamanhoLabel = new Label("Tamanho: 1200x800");
        Label screenSizeLabel = new Label("Tela: Detectando...");
        Label testeLabel = new Label("Teste: Aguardando...");
        
        statusPanel.getChildren().addAll(infoLabel, statusLabel, tamanhoLabel, screenSizeLabel, testeLabel);
        
        // Montar layout
        root.setTop(header);
        root.setCenter(content);
        root.setBottom(statusPanel);
        
        // Salvar referências para testes - corrigindo bindings duplicados
        // statusLabel já está disponível
        tamanhoLabel.textProperty().bind(tamanhoLabel.textProperty());
        screenSizeLabel.textProperty().bind(screenSizeLabel.textProperty());
        testeLabel.textProperty().bind(testeLabel.textProperty());
    }

    private void configurarJanelaResponsiva() {
        // Configurar redimensionamento responsivo
        primaryStage.setResizable(true);
        primaryStage.setMinWidth(800);
        primaryStage.setMinHeight(600);
        
        // Tamanho inicial baseado na resolução da tela
        Rectangle2D screenBounds = Screen.getPrimary().getBounds();
        double screenWidth = screenBounds.getWidth();
        double screenHeight = screenBounds.getHeight();
        
        // Definir tamanho inicial responsivo
        if (screenWidth >= 1920) {
            primaryStage.setWidth(1400);
            primaryStage.setHeight(900);
        } else if (screenWidth >= 1366) {
            primaryStage.setWidth(1200);
            primaryStage.setHeight(800);
        } else {
            primaryStage.setWidth(screenWidth * 0.9);
            primaryStage.setHeight(screenHeight * 0.8);
        }
        
        // Centralizar janela na tela
        primaryStage.centerOnScreen();
        
        // Adicionar listener para redimensionamento
        primaryStage.widthProperty().addListener((obs, oldVal, newVal) -> {
            atualizarInfoTela();
        });
        
        primaryStage.heightProperty().addListener((obs, oldVal, newVal) -> {
            atualizarInfoTela();
        });
    }

    private void atualizarInfoTela() {
        Platform.runLater(() -> {
            Rectangle2D screenBounds = Screen.getPrimary().getBounds();
            
            // Encontrar labels no layout
            encontrarEAtualizarLabels(
                primaryStage.getWidth() + "x" + primaryStage.getHeight(),
                screenBounds.getWidth() + "x" + screenBounds.getHeight()
            );
        });
    }

    private void encontrarEAtualizarLabels(String tamanho, String screenSize) {
        // Buscar labels no painel de status
        root.getChildren().stream()
            .filter(node -> node instanceof VBox)
            .map(node -> (VBox) node)
            .flatMap(vbox -> vbox.getChildren().stream())
            .filter(node -> node instanceof Label)
            .map(node -> (Label) node)
            .forEach(label -> {
                String text = label.getText();
                if (text.startsWith("Tamanho:")) {
                    label.setText("Tamanho: " + tamanho);
                } else if (text.startsWith("Tela:")) {
                    label.setText("Tela: " + screenSize);
                }
            });
    }

    private void executarTestesAutomaticos() {
        Platform.runLater(() -> {
            try {
                // Teste 1: Redimensionamento automático
                atualizarStatus("Teste 1: Redimensionamento automático");
                testarRedimensionamentoAutomatico();
                
                Thread.sleep(3000);
                
                // Teste 2: Limites mínimos
                atualizarStatus("Teste 2: Limites mínimos");
                testarLimitesMinimos();
                
                Thread.sleep(2000);
                
                // Teste 3: Tamanhos diferentes
                atualizarStatus("Teste 3: Tamanhos diferentes");
                testarTamanhosDiferentes();
                
                Thread.sleep(3000);
                
                // Teste 4: Tela cheia
                atualizarStatus("Teste 4: Tela cheia");
                testarTelaCheia();
                
                Thread.sleep(2000);
                
                // Concluir testes
                atualizarStatus("Testes concluídos com sucesso!");
                
            } catch (Exception e) {
                atualizarStatus("Erro nos testes: " + e.getMessage());
            }
        });
    }

    private void atualizarStatus(String mensagem) {
        root.getChildren().stream()
            .filter(node -> node instanceof VBox)
            .map(node -> (VBox) node)
            .flatMap(vbox -> vbox.getChildren().stream())
            .filter(node -> node instanceof Label)
            .map(node -> (Label) node)
            .forEach(label -> {
                String text = label.getText();
                if (text.startsWith("Teste:") || text.startsWith("Status:")) {
                    label.setText("Status: " + mensagem);
                }
            });
    }

    private void testarRedimensionamentoAutomatico() {
        // Redimensionar gradualmente
        for (int i = 800; i <= 1400; i += 200) {
            primaryStage.setWidth(i);
            primaryStage.setHeight(i * 0.75);
            
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                break;
            }
        }
    }

    private void testarLimitesMinimos() {
        // Tentar definir abaixo do mínimo
        primaryStage.setWidth(700);
        primaryStage.setHeight(500);
        
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            // Ignorar
        }
        
        // Verificar se respeitou os limites
        double larguraAtual = primaryStage.getWidth();
        double alturaAtual = primaryStage.getHeight();
        
        if (larguraAtual >= 800 && alturaAtual >= 600) {
            atualizarStatus("Limites mínimos: OK (800x600 respeitados)");
        } else {
            atualizarStatus("Limites mínimos: FALHOU");
        }
    }

    private void testarTamanhosDiferentes() {
        double[][] tamanhos = {
            {800, 600},
            {1024, 768},
            {1200, 800},
            {1400, 900},
            {1600, 1000}
        };
        
        for (double[] tamanho : tamanhos) {
            primaryStage.setWidth(tamanho[0]);
            primaryStage.setHeight(tamanho[1]);
            
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                break;
            }
        }
    }

    private void testarTelaCheia() {
        // Entrar em tela cheia
        primaryStage.setFullScreen(true);
        
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            // Ignorar
        }
        
        // Sair da tela cheia
        primaryStage.setFullScreen(false);
    }

    /**
     * Método principal para executar testes
     */
    public static void main(String[] args) {
        System.out.println("=== INICIANDO TESTES MANUAIS DE RESPONSIVIDADE ===");
        System.out.println("Esta janela irá testar automaticamente a responsividade do sistema.");
        System.out.println("Observe o comportamento dos componentes durante os testes.");
        
        // Obter informações da tela
        Rectangle2D screenBounds = Screen.getPrimary().getBounds();
        System.out.println("Resolução detectada: " + screenBounds.getWidth() + "x" + screenBounds.getHeight());
        System.out.println("Tamanho mínimo da janela: 800x600");
        System.out.println("Breakpoints CSS: 1200px, 1024px, 800px");
        
        System.out.println("\n=== INICIANDO INTERFACE DE TESTE ===");
        
        // Iniciar aplicação de teste
        launch(args);
    }
}
