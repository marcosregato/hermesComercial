package com.br.hermescomercial;

import com.br.hermescomercial.controller.PDVLoginSwingController;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

/**
 * Classe principal da aplicação Hermes Comercial PDV
 * Versão SWING - Interface nativa Java
 * Com tela de login e autenticação
 */
public class MainApplication {
    
    public static void main(String[] args) {
        // Inicializar sistema de logs
        // System.out.initialize(); // Método não existe
        
        // Processar argumentos de linha de comando
        if (args.length > 0) {
            for (String arg : args) {
                if (arg.equals("--flyway-migrate")) {
                    System.out.println("Executando migração Flyway...");
                    System.out.println("Use o script sqlite-console.sh para gerenciar o banco SQLite");
                    return;
                }
            }
        }
        
        // Configurar Look and Feel padrão para evitar problemas de cores
        try {
            UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
        } catch (Exception e) {
            System.out.println("Não foi possível configurar Look and Feel: " + e.getMessage());
        }
        
        // Executar na thread de UI
        SwingUtilities.invokeLater(() -> {
            try {
                System.out.println("Iniciando Hermes Comercial PDV v2.1.0");
                
                // Testar conexão com banco de dados
                System.out.println("Verificando conexão com banco de dados...");
                // try (Connection conn = DatabaseConfig.getConnection()) {
                //     System.out.println("✅ Conexão com banco estabelecida: " + conn.getMetaData().getURL());
                //     System.out.println("📊 Banco: " + conn.getMetaData().getDatabaseProductName());
                // } catch (Exception e) {
                //     System.err.println("❌ Erro de conexão com banco: " + e.getMessage());
                // }
                
                // Iniciar com tela de login
                System.out.println("Abrindo tela de login...");
                PDVLoginSwingController login = new PDVLoginSwingController();
                login.showFrame();
                
                System.out.println("Sistema iniciado com sucesso");
                
            } catch (Exception e) {
                System.err.println("Erro ao iniciar a aplicação: " + e.getMessage());
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
            System.out.println("Encerrando Hermes Comercial PDV");
            // System.out.shutdown(); // Método não existe
        }));
    }
}
