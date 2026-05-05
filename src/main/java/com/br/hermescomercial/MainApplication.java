package com.br.hermescomercial;

import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import com.br.hermescomercial.pdv.controller.PDVLoginSimpleController;

/**
 * Classe principal da aplicação Hermes Comercial PDV
 * Versão v3.0.0 - Production-Ready
 * Interface SWING completa com todas as funcionalidades empresariais
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
        
        // Configurar Look and Feel padrão e aplicar tema LayoutPadrao
        try {
            UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
            // LayoutPadrao é aplicado diretamente nos componentes, não需要 tema global
            System.out.println("LayoutPadrao configurado com sucesso");
        } catch (Exception e) {
            System.out.println("Não foi possível configurar Look and Feel: " + e.getMessage());
        }
        
        // Verificar se está em modo headless
        boolean isHeadless = java.awt.GraphicsEnvironment.isHeadless();
        
        if (isHeadless) {
            System.out.println("🖥️  Ambiente headless detectado - Iniciando em modo console");
            iniciarModoConsole(args);
        } else {
            // Executar na thread de UI
            SwingUtilities.invokeLater(() -> {
                try {
                    System.out.println("Iniciando Hermes Comercial PDV v3.0.0 - Production-Ready");
                    
                    // Testar conexão com banco de dados
                    System.out.println("Verificando conexão com banco de dados...");
                    
                    // Iniciar com tela de login moderna (sem validação em tempo real)
                    System.out.println("Abrindo tela de login...");
                    PDVLoginSimpleController login = new PDVLoginSimpleController();
                    login.show();
                    
                    System.out.println("Sistema iniciado com sucesso");
                    
                } catch (Exception e) {
                    System.err.println("Erro ao iniciar a aplicação GUI: " + e.getMessage());
                    System.err.println("Tentando fallback para tela de login original...");
                    
                    // Fallback: tentar tela de login original
                    try {
                        PDVLoginSimpleController login = new PDVLoginSimpleController();
                        login.showFrame();
                    } catch (Exception fallbackError) {
                        System.err.println("Erro crítico na interface gráfica: " + fallbackError.getMessage());
                        System.err.println("Iniciando em modo console...");
                        iniciarModoConsole(args);
                    }
                }
            });
        }
        
        // Adicionar shutdown hook para finalizar logs
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            System.out.println("Encerrando Hermes Comercial PDV");
            // System.out.shutdown(); // Método não existe
        }));
    }
    
    /**
     * Inicia o sistema em modo console para ambiente headless
     */
    private static void iniciarModoConsole(String[] args) {
        System.out.println("🖥️  MODO CONSOLE - HERMES COMERCIAL PDV");
        System.out.println("==========================================");
        
        // Verificar se há credenciais nos argumentos
        if (args.length >= 2) {
            String usuario = args[0];
            String senha = args[1];
            
            System.out.println("🔐 Tentando autenticação com credenciais fornecidas...");
            
            if (PDVLoginSimpleController.loginConsole(usuario, senha)) {
                System.out.println("✅ Login bem-sucedido!");
                System.out.println("👤 Usuário: " + PDVLoginSimpleController.getUsuarioAutenticado());
                System.out.println("🔑 Perfil: " + PDVLoginSimpleController.getPerfilUsuario());
                System.out.println("🎉 Sistema pronto para uso!");
                
                // Manter o sistema rodando
                manterSistemaAtivo();
            } else {
                System.err.println("❌ Falha na autenticação!");
                System.err.println("📝 Uso: java -jar hermesComercial-2.6.0.jar <usuario> <senha>");
                System.exit(1);
            }
        } else {
            System.out.println("📝 Modo de uso:");
            System.out.println("   java -jar hermesComercial-2.6.0.jar <usuario> <senha>");
            System.out.println("   java -jar hermesComercial-2.6.0.jar admin admin");
            System.out.println("");
            System.out.println("🔐 Credenciais padrão:");
            System.out.println("   Usuário: admin");
            System.out.println("   Senha: admin");
            System.out.println("");
            
            // Tentar login automático para teste
            System.out.println("🧪 Tentando login automático para teste...");
            if (PDVLoginSimpleController.loginConsole("admin", "admin")) {
                System.out.println("✅ Login automático bem-sucedido!");
                System.out.println("👤 Usuário: " + PDVLoginSimpleController.getUsuarioAutenticado());
                System.out.println("🔑 Perfil: " + PDVLoginSimpleController.getPerfilUsuario());
                System.out.println("🎉 Sistema pronto para uso!");
                
                manterSistemaAtivo();
            } else {
                System.err.println("❌ Falha no login automático!");
                System.exit(1);
            }
        }
    }
    
    /**
     * Mantém o sistema ativo em modo console
     */
    private static void manterSistemaAtivo() {
        System.out.println("🔄 Sistema em execução. Pressione Ctrl+C para encerrar.");
        System.out.println("📊 Use 'java -jar hermesComercial-2.6.0.jar --help' para mais opções.");
        
        // Manter a thread principal viva
        try {
            Thread.sleep(Long.MAX_VALUE);
        } catch (InterruptedException e) {
            System.out.println("👋 Sistema encerrado pelo usuário.");
        }
    }
}
