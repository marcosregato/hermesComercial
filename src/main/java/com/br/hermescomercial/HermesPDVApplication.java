package com.br.hermescomercial;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;

public class HermesPDVApplication extends Application {
    private static final Logger logger = LogManager.getLogger(HermesPDVApplication.class.getName());
    
    @Override
    public void start(Stage primaryStage) {
        try {
            logger.info("Iniciando aplicação Hermes PDV");
            
            // Carregar a tela principal do PDV
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/pdv_principal.fxml"));
            Parent root = loader.load();
            
            // Configurar a cena com tamanho responsivo
            Scene scene = new Scene(root);
            scene.getStylesheets().add(getClass().getResource("/css/pdv.css").toExternalForm());
            
            // Configurar o stage principal com redimensionamento responsivo
            primaryStage.setTitle("Hermes Comercial - Sistema PDV");
            primaryStage.setScene(scene);
            
            // Configurar redimensionamento responsivo
            primaryStage.setResizable(true);
            primaryStage.setMinWidth(800);
            primaryStage.setMinHeight(600);
            
            // Tamanho inicial baseado na resolução da tela
            javafx.geometry.Rectangle2D screenBounds = javafx.stage.Screen.getPrimary().getBounds();
            double screenWidth = screenBounds.getWidth();
            double screenHeight = screenBounds.getHeight();
            
            // Definir tamanho inicial responsivo
            if (screenWidth >= 1920) {
                // Full HD ou maior
                primaryStage.setWidth(1400);
                primaryStage.setHeight(900);
            } else if (screenWidth >= 1366) {
                // HD
                primaryStage.setWidth(1200);
                primaryStage.setHeight(800);
            } else {
                // Resoluções menores
                primaryStage.setWidth(screenWidth * 0.9);
                primaryStage.setHeight(screenHeight * 0.8);
            }
            
            // Centralizar janela na tela
            primaryStage.centerOnScreen();
            
            // Tentar carregar ícone da aplicação
            try {
                primaryStage.getIcons().add(new Image(getClass().getResourceAsStream("/img/logo.png")));
            } catch (Exception e) {
                logger.warn("Ícone da aplicação não encontrado: " + e.getMessage());
            }
            
            // Configurar evento de fechamento
            primaryStage.setOnCloseRequest(event -> {
                logger.info("Fechando aplicação Hermes PDV");
                // Aqui você pode adicionar lógica para salvar estado, fechar conexões, etc.
                System.exit(0);
            });
            
            // Exibir a janela
            primaryStage.show();
            
            logger.info("Aplicação Hermes PDV iniciada com sucesso");
            
        } catch (IOException e) {
            logger.error("Erro ao carregar a tela principal: " + e.getMessage(), e);
            mostrarErroFatal("Erro ao iniciar a aplicação", "Não foi possível carregar a interface principal.");
        } catch (Exception e) {
            logger.error("Erro inesperado ao iniciar a aplicação: " + e.getMessage(), e);
            mostrarErroFatal("Erro ao iniciar a aplicação", "Ocorreu um erro inesperado: " + e.getMessage());
        }
    }
    
    private static void mostrarErroFatal(String titulo, String mensagem) {
        javafx.scene.control.Alert alert = new javafx.scene.control.Alert(
            javafx.scene.control.Alert.AlertType.ERROR
        );
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensagem);
        alert.showAndWait();
        System.exit(1);
    }
    
    public static void main(String[] args) {
        // Testar conexão com banco de dados antes de iniciar
        try {
            com.br.hermescomercial.connectionBD.ConnectionBD.testConnection();
        } catch (Exception e) {
            logger.error("Não foi possível conectar ao banco de dados: " + e.getMessage(), e);
            mostrarErroFatal("Erro de Conexão", "Não foi possível conectar ao banco de dados. Verifique se o PostgreSQL está rodando.");
            return;
        }
        
        // Iniciar a aplicação JavaFX
        launch(args);
    }
}
