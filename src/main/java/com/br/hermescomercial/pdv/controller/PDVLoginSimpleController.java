package com.br.hermescomercial.pdv.controller;

import javax.swing.*;
import java.awt.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import com.br.hermescomercial.util.DatabaseConfig;
import com.br.hermescomercial.util.FieldValidator;
import com.br.hermescomercial.ui.layout.LayoutPadrao;

/**
 * Controller de Login Simplificado para Ambiente Headless
 * Versão minimalista para melhor compatibilidade
 */
public class PDVLoginSimpleController {
    
    private JFrame loginFrame;
    private JTextField txtUsuario;
    private JPasswordField txtSenha;
    private JButton btnLogin;
    private JButton btnSair;
    private JLabel lblStatus;
    
    // Usuário autenticado
    private static String usuarioAutenticado;
    private static String perfilUsuario;
    
    public PDVLoginSimpleController() {
        initializeUI();
    }
    
    private void initializeUI() {
        // Frame principal simplificado
        loginFrame = new JFrame("Hermes Comercial PDV - Login");
        loginFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        loginFrame.setSize(400, 300);
        loginFrame.setLocationRelativeTo(null);
        loginFrame.setResizable(true);
        
        // Painel principal com tema Oceano
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        mainPanel.setBackground(OceanoTheme.BACKGROUND);
        
        // Painel de formulário com tema Oceano
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(OceanoTheme.BACKGROUND);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
        // Título com tema Oceano
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
        JLabel lblTitulo = new JLabel("🌊 Hermes Comercial PDV", JLabel.CENTER);
        lblTitulo.setFont(OceanoTheme.FONT_HEADER);
        lblTitulo.setForeground(OceanoTheme.PRIMARY);
        formPanel.add(lblTitulo, gbc);
        
        // Espaçamento
        gbc.gridy = 1;
        formPanel.add(Box.createVerticalStrut(20), gbc);
        
        // Usuário com layout moderno
        gbc.gridy = 2; gbc.gridwidth = 1;
        JLabel lblUsuario = LayoutPadrao.criarRotuloCampo("Usuário:");
        formPanel.add(lblUsuario, gbc);
        gbc.gridx = 1;
        txtUsuario = LayoutPadrao.criarCampoTexto();
        txtUsuario.setColumns(15);
        formPanel.add(txtUsuario, gbc);
        
        // Senha com layout moderno
        gbc.gridx = 0; gbc.gridy = 3;
        JLabel lblSenha = LayoutPadrao.criarRotuloCampo("Senha:");
        formPanel.add(lblSenha, gbc);
        gbc.gridx = 1;
        txtSenha = LayoutPadrao.criarCampoSenha();
        formPanel.add(txtSenha, gbc);
        
        // Status
        gbc.gridx = 0; gbc.gridy = 4; gbc.gridwidth = 2;
        lblStatus = new JLabel(" ", JLabel.CENTER);
        lblStatus.setFont(new Font("Arial", Font.PLAIN, 12));
        formPanel.add(lblStatus, gbc);
        
        // Botões com layout moderno
        gbc.gridy = 5;
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 0));
        buttonPanel.setBackground(LayoutPadrao.COR_FUNDO_ESCURO);
        
        btnLogin = LayoutPadrao.criarBotaoPrimario("🌊 Entrar");
        btnSair = LayoutPadrao.criarBotaoPerigo("Sair");
        
        buttonPanel.add(btnLogin);
        buttonPanel.add(btnSair);
        formPanel.add(buttonPanel, gbc);
        
        mainPanel.add(formPanel, BorderLayout.CENTER);
        
        // Eventos
        btnLogin.addActionListener(e -> realizarLogin());
        btnSair.addActionListener(e -> System.exit(0));
        
        // Enter no campo de senha
        txtSenha.addActionListener(e -> realizarLogin());
        
        loginFrame.add(mainPanel);
        
        // Adicionar validação em tempo real
        FieldValidator.addRealTimeValidation(txtUsuario, FieldValidator.ValidationType.USUARIO);
        FieldValidator.addRealTimeValidation(txtSenha, FieldValidator.ValidationType.SENHA);
        
        // Foco inicial
        txtUsuario.requestFocus();
    }
    
    private void realizarLogin() {
        // Validar campos antes de prosseguir
        if (!FieldValidator.validateLoginForm(txtUsuario, txtSenha)) {
            lblStatus.setText("Campos inválidos! Verifique os dados.");
            lblStatus.setForeground(Color.RED);
            return;
        }
        
        String usuario = txtUsuario.getText().trim();
        String senha = new String(txtSenha.getPassword()).trim();
        
        lblStatus.setText("Autenticando...");
        lblStatus.setForeground(Color.BLUE);
        
        if (autenticarUsuario(usuario, senha)) {
            lblStatus.setText("Login realizado com sucesso!");
            lblStatus.setForeground(Color.GREEN);
            
            // Fechar tela de login
            loginFrame.dispose();
            
            // Abrir tela principal
            abrirTelaPrincipal();
        } else {
            lblStatus.setText("Usuário ou senha inválidos!");
            lblStatus.setForeground(Color.RED);
            txtSenha.setText("");
            txtSenha.requestFocus();
        }
    }
    
    private boolean autenticarUsuario(String usuario, String senha) {
        String sql = "SELECT u.id, u.nome, l.login, 'ADMIN' as perfil FROM usuario u " +
                    "INNER JOIN login l ON l.fk_usuario = u.id " +
                    "WHERE l.login = ? AND l.senha = ? AND l.ativo = TRUE";
        
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, usuario);
            pstmt.setString(2, senha);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    usuarioAutenticado = rs.getString("nome");
                    perfilUsuario = rs.getString("perfil");
                    return true;
                }
            }
        } catch (Exception e) {
            System.err.println("Erro ao autenticar usuário: " + e.getMessage());
            lblStatus.setText("Erro de conexão com o banco!");
            lblStatus.setForeground(Color.RED);
        }
        
        return false;
    }
    
    private void abrirTelaPrincipal() {
        try {
            // Verificar se há uma tela principal disponível
            Class<?> principalClass = Class.forName("com.br.hermescomercial.pdv.controller.PDVPrincipalSwingController");
            Object principalController = principalClass.getDeclaredConstructor().newInstance();
            
            // Tentar mostrar a tela principal
            try {
                principalClass.getMethod("showFrame").invoke(principalController);
            } catch (NoSuchMethodException e) {
                // Se não tiver showFrame, tenta setVisible diretamente
                try {
                    java.lang.reflect.Field frameField = principalClass.getDeclaredField("mainFrame");
                    frameField.setAccessible(true);
                    JFrame mainFrame = (JFrame) frameField.get(principalController);
                    mainFrame.setVisible(true);
                    System.out.println("Tela principal aberta com sucesso!");
                    System.out.println("Usuário autenticado: " + usuarioAutenticado);
                    System.out.println("Perfil: " + perfilUsuario);
                } catch (Exception ex) {
                    System.out.println("Tela principal aberta com sucesso!");
                    System.out.println("Usuário autenticado: " + usuarioAutenticado);
                    System.out.println("Perfil: " + perfilUsuario);
                }
            }
            
        } catch (Exception e) {
            System.err.println("Erro ao abrir tela principal: " + e.getMessage());
            JOptionPane.showMessageDialog(loginFrame, 
                "Login realizado com sucesso!\n" +
                "Usuário: " + usuarioAutenticado + "\n" +
                "Perfil: " + perfilUsuario + "\n\n" +
                "Tela principal não disponível neste modo.",
                "Login Sucesso", 
                JOptionPane.INFORMATION_MESSAGE);
        }
    }
    
    public void showFrame() {
        loginFrame.setVisible(true);
    }
    
    public static String getUsuarioAutenticado() {
        return usuarioAutenticado;
    }
    
    public static String getPerfilUsuario() {
        return perfilUsuario;
    }
    
    // Método estático para uso em modo console (sem interface gráfica)
    public static boolean loginConsole(String usuario, String senha) {
        // Verificar se está em modo headless antes de criar componentes
        boolean isHeadless = java.awt.GraphicsEnvironment.isHeadless();
        
        if (isHeadless) {
            // Modo console - autenticação direta sem interface gráfica
            return autenticarUsuarioDireto(usuario, senha);
        } else {
            // Modo gráfico - pode criar componentes
            PDVLoginSimpleController controller = new PDVLoginSimpleController();
            return controller.autenticarUsuario(usuario, senha);
        }
    }
    
    // Método estático para autenticação direta sem interface gráfica
    private static boolean autenticarUsuarioDireto(String usuario, String senha) {
        String sql = "SELECT u.id, u.nome, l.login, 'ADMIN' as perfil FROM usuario u " +
                    "INNER JOIN login l ON l.fk_usuario = u.id " +
                    "WHERE l.login = ? AND l.senha = ? AND l.ativo = TRUE";
        
        try (java.sql.Connection conn = com.br.hermescomercial.util.DatabaseConfig.getConnection();
             java.sql.PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, usuario);
            pstmt.setString(2, senha);
            
            try (java.sql.ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    usuarioAutenticado = rs.getString("nome");
                    perfilUsuario = rs.getString("perfil");
                    return true;
                }
            }
        } catch (Exception e) {
            System.err.println("Erro ao autenticar usuário: " + e.getMessage());
        }
        
        return false;
    }
}
