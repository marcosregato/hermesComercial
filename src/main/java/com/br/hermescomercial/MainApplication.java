package com.br.hermescomercial;

import com.br.hermescomercial.controller.PDVLoginSwingController;
import com.br.hermescomercial.controller.PDVPrincipalSwingController;
import com.br.hermescomercial.util.LoggerUtil;
import com.br.hermescomercial.util.DatabaseConfig;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import java.sql.Connection;

/**
 * Classe principal da aplicação Hermes Comercial PDV
 * Versão SWING - Interface nativa Java
 * Com tela de login e autenticação
 */
public class MainApplication {
    
    public static void main(String[] args) {
        // Inicializar sistema de logs
        LoggerUtil.initialize();
        
        // Processar argumentos de linha de comando
        if (args.length > 0) {
            for (String arg : args) {
                if (arg.equals("--flyway-migrate")) {
                    LoggerUtil.info("Executando migração Flyway...");
                    LoggerUtil.info("Use o script sqlite-console.sh para gerenciar o banco SQLite");
                    return;
                }
            }
        }
        
        // Configurar Look and Feel padrão para evitar problemas de cores
        try {
            UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
        } catch (Exception e) {
            LoggerUtil.info("Não foi possível configurar Look and Feel: " + e.getMessage());
        }
        
        // Executar na thread de UI
        SwingUtilities.invokeLater(() -> {
            try {
                LoggerUtil.info("Iniciando Hermes Comercial PDV v2.0");
                
                // Testar conexão com banco de dados
                LoggerUtil.info("Verificando conexão com banco de dados...");
                try (Connection conn = DatabaseConfig.getConnection()) {
                    LoggerUtil.info("✅ Conexão com banco estabelecida: " + conn.getMetaData().getURL());
                    LoggerUtil.info("📊 Banco: " + conn.getMetaData().getDatabaseProductName());
                } catch (Exception e) {
                    LoggerUtil.error("❌ Erro de conexão com banco: " + e.getMessage());
                }
                
                // Iniciar com tela de login
                LoggerUtil.info("Abrindo tela de login...");
                PDVLoginSwingController login = new PDVLoginSwingController();
                login.showFrame();
                
                LoggerUtil.info("Sistema iniciado com sucesso");
                
            } catch (Exception e) {
                LoggerUtil.error("Erro ao iniciar a aplicação", e);
                System.err.println("Erro ao iniciar a aplicação: " + e.getMessage());
                
                // Fallback: mostrar tela de login direto
                try {
                    PDVLoginSwingController login = new PDVLoginSwingController();
                    login.showFrame();
                } catch (Exception fallbackError) {
                    System.err.println("Erro crítico: " + fallbackError.getMessage());
                    System.exit(1);
                }
            }
        });
        
        // Adicionar shutdown hook para finalizar logs
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            LoggerUtil.info("Encerrando Hermes Comercial PDV");
            LoggerUtil.shutdown();
        }));
    }
}
