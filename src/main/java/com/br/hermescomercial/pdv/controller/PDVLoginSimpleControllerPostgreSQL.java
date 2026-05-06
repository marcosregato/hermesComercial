package com.br.hermescomercial.pdv.controller;

import javax.swing.*;
import java.awt.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import com.br.hermescomercial.util.SystemLogger;

/**
 * Controller de Login com Banco de Dados PostgreSQL
 * Versão com logging completo e suporte a PostgreSQL
 */
public class PDVLoginSimpleControllerPostgreSQL {
    
    private JFrame loginFrame;
    private JTextField txtUsuario;
    private JPasswordField txtSenha;
    
    public void show() {
        SystemLogger.ui("Tela de login PostgreSQL sendo exibida");
        if (loginFrame == null) {
            createLoginFrame();
        }
        loginFrame.setVisible(true);
    }
    
    private void createLoginFrame() {
        loginFrame = new JFrame("Hermes Comercial PDV v3.1.0 - Login (PostgreSQL)");
        loginFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        loginFrame.setSize(450, 500);
        loginFrame.setLocationRelativeTo(null);
        loginFrame.setResizable(false);
        
        JPanel mainPanel = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
                
                Color color1 = new Color(41, 128, 185);
                Color color2 = new Color(52, 152, 219);
                GradientPaint gradient = new GradientPaint(0, 0, color1, 0, getHeight(), color2);
                g2d.setPaint(gradient);
                g2d.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        
        JPanel loginPanel = new JPanel(new GridBagLayout());
        loginPanel.setOpaque(false);
        loginPanel.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
        // Título
        JLabel titleLabel = new JLabel("🏪 Hermes Comercial PDV", JLabel.CENTER);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        titleLabel.setForeground(Color.WHITE);
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
        loginPanel.add(titleLabel, gbc);
        
        JLabel subtitleLabel = new JLabel("Sistema de Gestão Comercial - PostgreSQL", JLabel.CENTER);
        subtitleLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        subtitleLabel.setForeground(Color.WHITE);
        gbc.gridy = 1;
        loginPanel.add(subtitleLabel, gbc);
        
        // Campos de login
        gbc.gridy = 2; gbc.gridwidth = 1;
        JLabel lblUsuario = new JLabel("👤 Usuário:");
        lblUsuario.setForeground(Color.WHITE);
        lblUsuario.setFont(new Font("Segoe UI", Font.BOLD, 14));
        loginPanel.add(lblUsuario, gbc);
        
        gbc.gridx = 1; gbc.weightx = 1.0;
        txtUsuario = new JTextField();
        txtUsuario.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        txtUsuario.setText("admin");
        loginPanel.add(txtUsuario, gbc);
        
        gbc.gridx = 0; gbc.gridy = 3; gbc.weightx = 0;
        JLabel lblSenha = new JLabel("🔐 Senha:");
        lblSenha.setForeground(Color.WHITE);
        lblSenha.setFont(new Font("Segoe UI", Font.BOLD, 14));
        loginPanel.add(lblSenha, gbc);
        
        gbc.gridx = 1; gbc.weightx = 1.0;
        txtSenha = new JPasswordField();
        txtSenha.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        txtSenha.setText("admin123");
        loginPanel.add(txtSenha, gbc);
        
        // Botões
        gbc.gridx = 0; gbc.gridy = 4; gbc.gridwidth = 2; gbc.weightx = 0;
        gbc.fill = GridBagConstraints.NONE;
        gbc.anchor = GridBagConstraints.CENTER;
        
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
        buttonPanel.setOpaque(false);
        
        JButton btnLogin = new JButton("🔐 Entrar");
        btnLogin.setBackground(new Color(46, 204, 113));
        btnLogin.setForeground(Color.WHITE);
        btnLogin.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnLogin.setPreferredSize(new Dimension(100, 40));
        btnLogin.addActionListener(e -> realizarLogin());
        buttonPanel.add(btnLogin);
        
        JButton btnTestarBD = new JButton("🔗 Testar BD");
        btnTestarBD.setBackground(new Color(52, 152, 219));
        btnTestarBD.setForeground(Color.WHITE);
        btnTestarBD.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnTestarBD.setPreferredSize(new Dimension(100, 40));
        btnTestarBD.addActionListener(e -> testarConexaoBD());
        buttonPanel.add(btnTestarBD);
        
        JButton btnCriarUsuario = new JButton("➕ Criar Admin");
        btnCriarUsuario.setBackground(new Color(230, 126, 34));
        btnCriarUsuario.setForeground(Color.WHITE);
        btnCriarUsuario.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnCriarUsuario.setPreferredSize(new Dimension(100, 40));
        btnCriarUsuario.addActionListener(e -> criarUsuarioAdmin());
        buttonPanel.add(btnCriarUsuario);
        
        loginPanel.add(buttonPanel, gbc);
        
        // Informações
        gbc.gridy = 5; gbc.fill = GridBagConstraints.HORIZONTAL; gbc.anchor = GridBagConstraints.WEST;
        JLabel infoLabel = new JLabel("<html><div style='color: white; font-size: 11px;'>" +
            "📋 Credenciais padrão: admin / admin123<br>" +
            "🗄️  Banco: PostgreSQL<br>" +
            "💡 Use 'Testar BD' para verificar conexão<br>" +
            "➕ Use 'Criar Admin' para criar usuário</div></html>");
        loginPanel.add(infoLabel, gbc);
        
        // ActionListeners
        txtUsuario.addActionListener(e -> realizarLogin());
        txtSenha.addActionListener(e -> realizarLogin());
        
        txtUsuario.requestFocus();
        
        mainPanel.add(loginPanel, BorderLayout.CENTER);
        loginFrame.add(mainPanel);
        
        // Testar conexão automaticamente
        SwingUtilities.invokeLater(() -> {
            testarConexaoBD();
        });
    }
    
    private void realizarLogin() {
        String usuario = txtUsuario.getText().trim();
        String senha = new String(txtSenha.getPassword()).trim();
        
        SystemLogger.auth("Tentativa de login - Usuário: " + usuario);
        
        if (usuario.isEmpty() || senha.isEmpty()) {
            SystemLogger.auth("Login falhou - Campos vazios para usuário: " + usuario);
            JOptionPane.showMessageDialog(loginFrame, 
                "Por favor, preencha todos os campos!", 
                "Erro", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        try {
            long startTime = System.currentTimeMillis();
            Connection conn = getPostgreSQLConnection();
            SystemLogger.database("Conexão PostgreSQL estabelecida para login");
            
            String sql = "SELECT u.id, u.nome, l.login, 'ADMIN' as perfil FROM usuario u " +
                        "INNER JOIN login l ON l.fk_usuario = u.id " +
                        "WHERE l.login = ? AND l.senha = ? AND l.ativo = TRUE";
            
            SystemLogger.database("Executando query de autenticação para usuário: " + usuario);
            
            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setString(1, usuario);
                pstmt.setString(2, senha);
                
                try (ResultSet rs = pstmt.executeQuery()) {
                    long queryTime = System.currentTimeMillis() - startTime;
                    SystemLogger.database("Query de autenticação executada em " + queryTime + "ms");
                    
                    if (rs.next()) {
                        SystemLogger.loginSuccess(usuario);
                        SystemLogger.auth("Login bem-sucedido - Usuário: " + usuario + 
                            ", Nome: " + rs.getString("nome"));
                        
                        JOptionPane.showMessageDialog(loginFrame, 
                            "✅ Login bem-sucedido!\n\n" +
                            "👤 Usuário: " + usuario + "\n" +
                            "📝 Nome: " + rs.getString("nome") + "\n" +
                            "🎯 Perfil: ADMIN\n" +
                            "🚀 Abrindo sistema...", 
                            "Sucesso", JOptionPane.INFORMATION_MESSAGE);
                        
                        loginFrame.dispose();
                        SystemLogger.ui("Fechando tela de login, abrindo tela principal");
                        abrirTelaPrincipal(usuario, rs.getString("nome"));
                    } else {
                        SystemLogger.loginFailure(usuario, "Credenciais inválidas");
                        SystemLogger.auth("Login falhou - Usuário não encontrado ou senha incorreta: " + usuario);
                        
                        JOptionPane.showMessageDialog(loginFrame, 
                            "❌ Usuário ou senha incorretos!\n\n" +
                            "💡 Dicas:\n" +
                            "• Use admin/admin123\n" +
                            "• Clique em 'Criar Admin' se necessário\n" +
                            "• Verifique conexão com PostgreSQL", 
                            "Erro de Autenticação", JOptionPane.ERROR_MESSAGE);
                        
                        txtSenha.setText("");
                        txtUsuario.requestFocus();
                    }
                }
            }
            
            conn.close();
            SystemLogger.database("Conexão PostgreSQL fechada após login");
        } catch (Exception e) {
            SystemLogger.error("Erro durante processo de login para usuário: " + usuario, e);
            JOptionPane.showMessageDialog(loginFrame, 
                "❌ Erro no login:\n\n" + e.getMessage() + "\n\n" +
                "💡 Verifique:\n" +
                "• PostgreSQL está rodando\n" +
                "• Configuração do banco\n" +
                "• Use 'Testar BD' para diagnóstico", 
                "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void testarConexaoBD() {
        SystemLogger.database("Testando conexão com PostgreSQL");
        try {
            long startTime = System.currentTimeMillis();
            Connection conn = getPostgreSQLConnection();
            
            if (conn != null && !conn.isClosed()) {
                long connectionTime = System.currentTimeMillis() - startTime;
                SystemLogger.database("Conexão PostgreSQL estabelecida em " + connectionTime + "ms");
                
                // Testar consulta simples
                try (ResultSet rs = conn.createStatement().executeQuery("SELECT COUNT(*) as total FROM usuario")) {
                    if (rs.next()) {
                        int totalUsuarios = rs.getInt("total");
                        SystemLogger.database("Total de usuários no PostgreSQL: " + totalUsuarios);
                        
                        JOptionPane.showMessageDialog(loginFrame, 
                            "✅ Conexão PostgreSQL OK!\n\n" +
                            "🗄️  Banco: PostgreSQL\n" +
                            "👥 Usuários: " + totalUsuarios + "\n" +
                            "⏱️  Tempo: " + connectionTime + "ms", 
                            "Conexão OK", JOptionPane.INFORMATION_MESSAGE);
                    }
                }
                
                conn.close();
                SystemLogger.database("Teste de conexão PostgreSQL concluído com sucesso");
            } else {
                SystemLogger.database("Falha na conexão com PostgreSQL");
                JOptionPane.showMessageDialog(loginFrame, 
                    "❌ Falha na conexão com PostgreSQL!", 
                    "Erro", JOptionPane.ERROR_MESSAGE);
            }
        } catch (Exception e) {
            SystemLogger.error("Erro ao testar conexão PostgreSQL", e);
            JOptionPane.showMessageDialog(loginFrame, 
                "❌ Erro na conexão PostgreSQL:\n\n" + e.getMessage() + "\n\n" +
                "💡 Verifique:\n" +
                "• PostgreSQL está instalado e rodando\n" +
                "• Configuração em database-config.properties\n" +
                "• Driver PostgreSQL disponível", 
                "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void criarUsuarioAdmin() {
        SystemLogger.database("Iniciando criação de usuário admin no PostgreSQL");
        try {
            long startTime = System.currentTimeMillis();
            Connection conn = getPostgreSQLConnection();
            conn.setAutoCommit(false);
            SystemLogger.database("Conexão PostgreSQL estabelecida para criação de usuário");
            
            // Verificar se usuário já existe
            String checkSql = "SELECT COUNT(*) as total FROM usuario WHERE nome = 'Administrador'";
            try (PreparedStatement checkPstmt = conn.prepareStatement(checkSql);
                 ResultSet checkRs = checkPstmt.executeQuery()) {
                
                if (checkRs.next() && checkRs.getInt("total") > 0) {
                    SystemLogger.database("Usuário Administrador já existe no PostgreSQL");
                    JOptionPane.showMessageDialog(loginFrame, 
                        "⚠️ Usuário Administrador já existe!", 
                        "Aviso", JOptionPane.WARNING_MESSAGE);
                    conn.close();
                    return;
                }
            }
            
            // Inserir usuário
            String insertUsuarioSql = "INSERT INTO usuario (nome, email, telefone, cpf_cnpj, data_cadastro) " +
                                    "VALUES ('Administrador', 'admin@hermescomercial.com', '00000000000', '00000000000', CURRENT_DATE)";
            
            try (PreparedStatement usuarioPstmt = conn.prepareStatement(insertUsuarioSql, PreparedStatement.RETURN_GENERATED_KEYS)) {
                usuarioPstmt.executeUpdate();
                
                ResultSet generatedKeys = usuarioPstmt.getGeneratedKeys();
                long usuarioId = 0;
                if (generatedKeys.next()) {
                    usuarioId = generatedKeys.getLong(1);
                }
                
                // Inserir login
                String insertLoginSql = "INSERT INTO login (fk_usuario, login, senha, ativo) " +
                                       "VALUES (?, 'admin', 'admin123', TRUE)";
                
                try (PreparedStatement loginPstmt = conn.prepareStatement(insertLoginSql)) {
                    loginPstmt.setLong(1, usuarioId);
                    loginPstmt.executeUpdate();
                }
                
                conn.commit();
                
                long creationTime = System.currentTimeMillis() - startTime;
                SystemLogger.database("Usuário admin criado no PostgreSQL em " + creationTime + "ms");
                SystemLogger.audit("DATABASE_USER_CREATED - Usuário admin criado com ID: " + usuarioId);
                
                JOptionPane.showMessageDialog(loginFrame, 
                    "✅ Usuário ADMIN criado com sucesso!\n\n" +
                    "👤 Nome: Administrador\n" +
                    "🔐 Login: admin\n" +
                    "🔑 Senha: admin123\n" +
                    "🗄️  Banco: PostgreSQL\n" +
                    "⏱️  Tempo: " + creationTime + "ms\n\n" +
                    "💡 Agora você pode fazer login!", 
                    "Usuário Criado", JOptionPane.INFORMATION_MESSAGE);
            }
            
            conn.close();
            SystemLogger.database("Conexão PostgreSQL fechada após criação de usuário");
        } catch (Exception e) {
            SystemLogger.error("Erro ao criar usuário admin no PostgreSQL", e);
            JOptionPane.showMessageDialog(loginFrame, 
                "❌ Erro ao criar usuário:\n\n" + e.getMessage(), 
                "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private Connection getPostgreSQLConnection() throws Exception {
        SystemLogger.database("Obtendo conexão PostgreSQL usando DatabaseConfig");
        return com.br.hermescomercial.util.DatabaseConfig.getConnection();
    }
    
    private void abrirTelaPrincipal(String usuario, String nome) {
        SystemLogger.ui("Abrindo tela principal para usuário: " + usuario);
        SystemLogger.pdv("Tela principal do PDV iniciada");
        
        JFrame mainFrame = new JFrame("Hermes Comercial PDV - Sistema Principal (PostgreSQL)");
        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainFrame.setSize(1200, 800);
        mainFrame.setLocationRelativeTo(null);
        
        JPanel mainPanel = new JPanel(new BorderLayout());
        
        // Header
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(new Color(41, 128, 185));
        header.setPreferredSize(new Dimension(0, 70));
        header.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        
        JPanel leftPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        leftPanel.setOpaque(false);
        
        JLabel logoLabel = new JLabel("🏪");
        logoLabel.setFont(new Font("Arial", Font.PLAIN, 28));
        logoLabel.setForeground(Color.WHITE);
        
        JLabel titleLabel = new JLabel("Hermes Comercial PDV v3.1.0");
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 22));
        
        leftPanel.add(logoLabel);
        leftPanel.add(titleLabel);
        
        JPanel rightPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        rightPanel.setOpaque(false);
        
        JLabel userLabel = new JLabel("👤 " + nome + " (" + usuario + ")");
        userLabel.setForeground(Color.WHITE);
        userLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        
        JButton btnSair = new JButton("🚪 Sair");
        btnSair.setBackground(new Color(231, 76, 60));
        btnSair.setForeground(Color.WHITE);
        btnSair.addActionListener(e -> {
            SystemLogger.audit("USER_LOGOUT: " + usuario);
            SystemLogger.ui("Usuário " + usuario + " fez logout");
            mainFrame.dispose();
            System.exit(0);
        });
        
        rightPanel.add(userLabel);
        rightPanel.add(btnSair);
        
        header.add(leftPanel, BorderLayout.WEST);
        header.add(rightPanel, BorderLayout.EAST);
        
        // Área de trabalho
        JPanel workArea = new JPanel(new BorderLayout());
        workArea.setBackground(Color.WHITE);
        
        JLabel welcomeLabel = new JLabel("🎉 Bem-vindo ao Hermes Comercial PDV!", JLabel.CENTER);
        welcomeLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        welcomeLabel.setForeground(new Color(52, 73, 94));
        welcomeLabel.setBorder(BorderFactory.createEmptyBorder(50, 50, 50, 50));
        
        workArea.add(welcomeLabel, BorderLayout.CENTER);
        
        // Menu lateral com submenus em cascade (versão elegante)
        PDVMenuLateralElegante menuCascade = new PDVMenuLateralElegante(usuario, nome, workArea);
        JPanel menuPanel = menuCascade.createMenuPanel();
        
        // Status bar
        JPanel statusBar = new JPanel(new BorderLayout());
        statusBar.setBackground(new Color(189, 195, 199));
        statusBar.setPreferredSize(new Dimension(0, 30));
        statusBar.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        
        JLabel statusLabel = new JLabel("🟢 Sistema Online | Usuário: " + usuario + " | " + 
            java.time.LocalDateTime.now().format(java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss")) + 
            " | 🗄️  Banco: PostgreSQL");
        statusLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        statusLabel.setForeground(new Color(52, 73, 94));
        
        statusBar.add(statusLabel, BorderLayout.WEST);
        
        mainPanel.add(header, BorderLayout.NORTH);
        mainPanel.add(menuPanel, BorderLayout.WEST);
        mainPanel.add(workArea, BorderLayout.CENTER);
        mainPanel.add(statusBar, BorderLayout.SOUTH);
        
        mainFrame.add(mainPanel);
        mainFrame.setVisible(true);
        
        SystemLogger.ui("Tela principal exibida com sucesso para usuário: " + usuario);
        SystemLogger.pdv("Sistema PDV totalmente operacional com PostgreSQL");
        System.out.println("✅ Tela principal aberta com sucesso para usuário: " + usuario);
    }
    
    public static void main(String[] args) {
        SystemLogger.systemStartup();
        SystemLogger.info("Iniciando PDVLoginSimpleControllerPostgreSQL");
        SwingUtilities.invokeLater(() -> {
            new PDVLoginSimpleControllerPostgreSQL().show();
        });
    }
}
