package com.br.hermescomercial;

import com.br.hermescomercial.controller.PDVPrincipalSwingController;
import com.br.hermescomercial.util.LoggerUtil;
import javax.swing.SwingUtilities;

/**
 * Classe principal da aplicação Hermes Comercial PDV
 * Versão SWING - Interface nativa Java
 */
public class MainApplication {
    
    public static void main(String[] args) {
        // Inicializar sistema de logs
        LoggerUtil.initialize();
        
        // Executar na thread de UI
        SwingUtilities.invokeLater(() -> {
            try {
                LoggerUtil.info("Iniciando interface principal do sistema");
                
                // Criar e exibir o controller principal
                PDVPrincipalSwingController controller = new PDVPrincipalSwingController();
                controller.show();
                
                LoggerUtil.info("Interface principal iniciada com sucesso");
                
            } catch (Exception e) {
                LoggerUtil.error("Erro ao iniciar a aplicação", e);
                System.err.println("Erro ao iniciar a aplicação: " + e.getMessage());
                System.exit(1);
            }
        });
        
        // Adicionar shutdown hook para finalizar logs
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            LoggerUtil.shutdown();
        }));
    }
}
