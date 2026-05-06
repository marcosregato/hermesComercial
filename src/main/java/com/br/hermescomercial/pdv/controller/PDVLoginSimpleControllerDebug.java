package com.br.hermescomercial.pdv.controller;

import javax.swing.*;
import java.awt.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

/**
 * Controller de Login com Debug para verificar problemas de autenticação
 */
public class PDVLoginSimpleControllerDebug {
    
    private JFrame loginFrame;
    private JTextField txtUsuario;
    private JPasswordField txtSenha;
    
    public void show() {
        if (loginFrame == null) {
            createLoginFrame();
        }
        loginFrame.setVisible(true);
    }
    
    private void createLoginFrame() {
        loginFrame = new JFrame("Hermes Comercial PDV v3.1.0 - Login (DEBUG)");
        loginFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        loginFrame.setSize(500, 600);
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
        
        JLabel subtitleLabel = new JLabel("Sistema de Gestão Comercial", JLabel.CENTER);
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
        txtUsuario.setText("admin"); // Valor padrão para teste
        loginPanel.add(txtUsuario, gbc);
        
        gbc.gridx = 0; gbc.gridy = 3; gbc.weightx = 0;
        JLabel lblSenha = new JLabel("🔐 Senha:");
        lblSenha.setForeground(Color.WHITE);
        lblSenha.setFont(new Font("Segoe UI", Font.BOLD, 14));
        loginPanel.add(lblSenha, gbc);
        
        gbc.gridx = 1; gbc.weightx = 1.0;
        txtSenha = new JPasswordField();
        txtSenha.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        txtSenha.setText("admin123"); // Valor padrão para teste
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
        btnLogin.setPreferredSize(new Dimension(120, 40));
        btnLogin.addActionListener(e -> realizarLoginComDebug());
        buttonPanel.add(btnLogin);
        
        JButton btnTestarBD = new JButton("🔗 Testar BD");
        btnTestarBD.setBackground(new Color(52, 152, 219));
        btnTestarBD.setForeground(Color.WHITE);
        btnTestarBD.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnTestarBD.setPreferredSize(new Dimension(120, 40));
        btnTestarBD.addActionListener(e -> testarConexaoBD());
        buttonPanel.add(btnTestarBD);
        
        JButton btnCriarUsuario = new JButton("➕ Criar Admin");
        btnCriarUsuario.setBackground(new Color(230, 126, 34));
        btnCriarUsuario.setForeground(Color.WHITE);
        btnCriarUsuario.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnCriarUsuario.setPreferredSize(new Dimension(120, 40));
        btnCriarUsuario.addActionListener(e -> criarUsuarioAdmin());
        buttonPanel.add(btnCriarUsuario);
        
        loginPanel.add(buttonPanel, gbc);
        
        // Área de debug
        gbc.gridy = 5; gbc.fill = GridBagConstraints.HORIZONTAL; gbc.anchor = GridBagConstraints.WEST;
        JTextArea debugArea = new JTextArea(8, 40);
        debugArea.setFont(new Font("Monospaced", Font.PLAIN, 11));
        debugArea.setBackground(new Color(255, 255, 255, 200));
        debugArea.setBorder(BorderFactory.createLineBorder(Color.WHITE));
        JScrollPane scrollPane = new JScrollPane(debugArea);
        loginPanel.add(scrollPane, gbc);
        
        // ActionListeners
        txtUsuario.addActionListener(e -> realizarLoginComDebug());
        txtSenha.addActionListener(e -> realizarLoginComDebug());
        
        txtUsuario.requestFocus();
        
        mainPanel.add(loginPanel, BorderLayout.CENTER);
        loginFrame.add(mainPanel);
        
        // Mensagem inicial de debug
        debugArea.setText("🔍 DEBUG: Sistema de Login Iniciado\n");
        debugArea.append("📋 Use admin/admin123 para teste\n");
        debugArea.append("🔗 Clique em 'Testar BD' para verificar conexão\n");
        debugArea.append("➕ Clique em 'Criar Admin' se necessário\n\n");
    }
    
    private void realizarLoginComDebug() {
        String usuario = txtUsuario.getText().trim();
        String senha = new String(txtSenha.getPassword()).trim();
        
        JTextArea debugArea = getDebugArea();
        debugArea.append("🔍 Tentando login...\n");
        debugArea.append("👤 Usuário: '" + usuario + "'\n");
        debugArea.append("🔐 Senha: '" + senha + "'\n");
        
        if (usuario.isEmpty() || senha.isEmpty()) {
            debugArea.append("❌ Erro: Campos vazios!\n\n");
            return;
        }
        
        try {
            debugArea.append("🔗 Testando conexão com banco...\n");
            Connection conn = com.br.hermescomercial.util.DatabaseConfig.getConnection();
            if (conn != null && !conn.isClosed()) {
                debugArea.append("✅ Conexão OK!\n");
                
                String sql = "SELECT u.id, u.nome, l.login, 'ADMIN' as perfil FROM usuario u " +
                            "INNER JOIN login l ON l.fk_usuario = u.id " +
                            "WHERE l.login = ? AND l.senha = ? AND l.ativo = TRUE";
                
                debugArea.append("🔍 Executando SQL...\n");
                debugArea.append("📋 SQL: " + sql + "\n");
                
                try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                    pstmt.setString(1, usuario);
                    pstmt.setString(2, senha);
                    
                    try (ResultSet rs = pstmt.executeQuery()) {
                        if (rs.next()) {
                            debugArea.append("✅ Login bem-sucedido!\n");
                            debugArea.append("👤 ID: " + rs.getInt("id") + "\n");
                            debugArea.append("📝 Nome: " + rs.getString("nome") + "\n");
                            debugArea.append("🔐 Login: " + rs.getString("login") + "\n");
                            debugArea.append("🎯 Perfil: " + rs.getString("perfil") + "\n");
                            debugArea.append("🚀 Abrindo tela principal...\n\n");
                            
                            JOptionPane.showMessageDialog(loginFrame, 
                                "✅ Login bem-sucedido!\n\n" +
                                "👤 Usuário: " + usuario + "\n" +
                                "📝 Nome: " + rs.getString("nome") + "\n" +
                                "🎯 Perfil: ADMIN\n" +
                                "🚀 Abrindo sistema...", 
                                "Sucesso", JOptionPane.INFORMATION_MESSAGE);
                            
                            loginFrame.dispose();
                            abrirTelaPrincipal(usuario, rs.getString("nome"));
                        } else {
                            debugArea.append("❌ Falha: Usuário não encontrado!\n");
                            debugArea.append("💡 Verifique se o usuário existe no banco de dados\n");
                            debugArea.append("💡 Use 'Criar Admin' para criar o usuário\n\n");
                            
                            JOptionPane.showMessageDialog(loginFrame, 
                                "❌ Usuário ou senha incorretos!\n\n" +
                                "💡 Dicas:\n" +
                                "• Use 'Testar BD' para verificar conexão\n" +
                                "• Use 'Criar Admin' para criar usuário\n" +
                                "• Verifique se admin/admin123 existe no BD", 
                                "Erro de Autenticação", JOptionPane.ERROR_MESSAGE);
                        }
                    }
                }
                
                conn.close();
            } else {
                debugArea.append("❌ Falha na conexão com banco!\n\n");
            }
        } catch (Exception e) {
            debugArea.append("❌ Erro: " + e.getMessage() + "\n\n");
            e.printStackTrace();
        }
    }
    
    private void testarConexaoBD() {
        JTextArea debugArea = getDebugArea();
        debugArea.append("🔗 Testando conexão com banco...\n");
        
        try {
            Connection conn = com.br.hermescomercial.util.DatabaseConfig.getConnection();
            if (conn != null && !conn.isClosed()) {
                debugArea.append("✅ Conexão estabelecida!\n");
                debugArea.append("🗄️  Banco: " + conn.getMetaData().getDatabaseProductName() + "\n");
                debugArea.append("🔗 URL: " + conn.getMetaData().getURL() + "\n");
                
                // Testar consulta simples
                try (ResultSet rs = conn.createStatement().executeQuery("SELECT COUNT(*) as total FROM usuario")) {
                    if (rs.next()) {
                        debugArea.append("👥 Total usuários: " + rs.getInt("total") + "\n");
                    }
                }
                
                conn.close();
                debugArea.append("✅ Teste concluído com sucesso!\n\n");
                
                JOptionPane.showMessageDialog(loginFrame, 
                    "✅ Conexão com banco OK!\n\n" +
                    "🗄️  Banco: PostgreSQL\n" +
                    "👥 Usuários encontrados", 
                    "Conexão OK", JOptionPane.INFORMATION_MESSAGE);
            } else {
                debugArea.append("❌ Falha na conexão!\n\n");
                JOptionPane.showMessageDialog(loginFrame, 
                    "❌ Falha na conexão com banco de dados!", 
                    "Erro", JOptionPane.ERROR_MESSAGE);
            }
        } catch (Exception e) {
            debugArea.append("❌ Erro: " + e.getMessage() + "\n\n");
            JOptionPane.showMessageDialog(loginFrame, 
                "❌ Erro na conexão:\n\n" + e.getMessage(), 
                "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void criarUsuarioAdmin() {
        JTextArea debugArea = getDebugArea();
        debugArea.append("➕ Criando usuário ADMIN...\n");
        
        try {
            Connection conn = com.br.hermescomercial.util.DatabaseConfig.getConnection();
            conn.setAutoCommit(false);
            
            // Verificar se usuário já existe
            String checkSql = "SELECT COUNT(*) as total FROM usuario WHERE nome = 'Administrador'";
            try (PreparedStatement checkPstmt = conn.prepareStatement(checkSql);
                 ResultSet checkRs = checkPstmt.executeQuery()) {
                
                if (checkRs.next() && checkRs.getInt("total") > 0) {
                    debugArea.append("⚠️ Usuário Administrador já existe!\n\n");
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
                
                debugArea.append("✅ Usuário ADMIN criado com sucesso!\n");
                debugArea.append("👤 ID: " + usuarioId + "\n");
                debugArea.append("🔐 Login: admin\n");
                debugArea.append("🔑 Senha: admin123\n");
                debugArea.append("✅ Status: Ativo\n\n");
                
                JOptionPane.showMessageDialog(loginFrame, 
                    "✅ Usuário ADMIN criado com sucesso!\n\n" +
                    "👤 Nome: Administrador\n" +
                    "🔐 Login: admin\n" +
                    "🔑 Senha: admin123\n\n" +
                    "💡 Agora você pode fazer login!", 
                    "Usuário Criado", JOptionPane.INFORMATION_MESSAGE);
            }
            
            conn.close();
        } catch (Exception e) {
            debugArea.append("❌ Erro: " + e.getMessage() + "\n\n");
            JOptionPane.showMessageDialog(loginFrame, 
                "❌ Erro ao criar usuário:\n\n" + e.getMessage(), 
                "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private JTextArea getDebugArea() {
        // Encontrar a área de debug no painel
        for (Component comp : loginFrame.getContentPane().getComponents()) {
            if (comp instanceof JPanel) {
                JPanel mainPanel = (JPanel) comp;
                for (Component subComp : mainPanel.getComponents()) {
                    if (subComp instanceof JPanel) {
                        JPanel loginPanel = (JPanel) subComp;
                        for (Component innerComp : loginPanel.getComponents()) {
                            if (innerComp instanceof JScrollPane) {
                                JScrollPane scrollPane = (JScrollPane) innerComp;
                                return (JTextArea) scrollPane.getViewport().getView();
                            }
                        }
                    }
                }
            }
        }
        return null;
    }
    
    private void abrirTelaPrincipal(String usuario, String nome) {
        JFrame mainFrame = new JFrame("Hermes Comercial PDV - Sistema Principal");
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
            mainFrame.dispose();
            System.exit(0);
        });
        
        rightPanel.add(userLabel);
        rightPanel.add(btnSair);
        
        header.add(leftPanel, BorderLayout.WEST);
        header.add(rightPanel, BorderLayout.EAST);
        
        // Menu lateral
        JPanel menuPanel = new JPanel(new GridLayout(0, 1, 5, 5));
        menuPanel.setBackground(new Color(236, 240, 241));
        menuPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        menuPanel.setPreferredSize(new Dimension(250, 0));
        
        String[] menuItems = {
            "🏪 PDV", "📦 Produtos", "👥 Clientes", "💰 Vendas", 
            "📊 Estoque", "🏦 Financeiro", "📈 Relatórios", "⚙️ Configurações"
        };
        
        for (String item : menuItems) {
            JButton btn = new JButton(item);
            btn.setBackground(Color.WHITE);
            btn.setForeground(new Color(52, 73, 94));
            btn.setFont(new Font("Segoe UI", Font.PLAIN, 14));
            btn.setHorizontalAlignment(SwingConstants.LEFT);
            btn.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));
            btn.addActionListener(e -> {
                JOptionPane.showMessageDialog(mainFrame, 
                    "🚀 Módulo: " + item + "\n\n" +
                    "✅ Sistema funcionando!\n" +
                    "👤 Usuário: " + nome + "\n" +
                    "🔐 Login: " + usuario, 
                    item, JOptionPane.INFORMATION_MESSAGE);
            });
            menuPanel.add(btn);
        }
        
        // Área de trabalho
        JPanel workArea = new JPanel(new BorderLayout());
        workArea.setBackground(Color.WHITE);
        
        JLabel welcomeLabel = new JLabel("🎉 Bem-vindo ao Hermes Comercial PDV!", JLabel.CENTER);
        welcomeLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        welcomeLabel.setForeground(new Color(52, 73, 94));
        welcomeLabel.setBorder(BorderFactory.createEmptyBorder(50, 50, 50, 50));
        
        workArea.add(welcomeLabel, BorderLayout.CENTER);
        
        // Status bar
        JPanel statusBar = new JPanel(new BorderLayout());
        statusBar.setBackground(new Color(189, 195, 199));
        statusBar.setPreferredSize(new Dimension(0, 30));
        statusBar.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        
        JLabel statusLabel = new JLabel("🟢 Sistema Online | Usuário: " + usuario + " | " + 
            java.time.LocalDateTime.now().format(java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss")));
        statusLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        statusLabel.setForeground(new Color(52, 73, 94));
        
        statusBar.add(statusLabel, BorderLayout.WEST);
        
        mainPanel.add(header, BorderLayout.NORTH);
        mainPanel.add(menuPanel, BorderLayout.WEST);
        mainPanel.add(workArea, BorderLayout.CENTER);
        mainPanel.add(statusBar, BorderLayout.SOUTH);
        
        mainFrame.add(mainPanel);
        mainFrame.setVisible(true);
        
        System.out.println("✅ Tela principal aberta com sucesso para usuário: " + usuario);
    }
    
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new PDVLoginSimpleControllerDebug().show();
        });
    }
}
