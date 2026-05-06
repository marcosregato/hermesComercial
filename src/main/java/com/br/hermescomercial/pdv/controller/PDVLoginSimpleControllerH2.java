package com.br.hermescomercial.pdv.controller;

import javax.swing.*;
import java.awt.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import com.br.hermescomercial.util.SystemLogger;

/**
 * Controller de Login com Banco de Dados H2 Embutido
 * Versão simplificada para garantir funcionamento do login
 */
public class PDVLoginSimpleControllerH2 {
    
    private JFrame loginFrame;
    private JTextField txtUsuario;
    private JPasswordField txtSenha;
    
    public void show() {
        SystemLogger.ui("Tela de login H2 sendo exibida");
        if (loginFrame == null) {
            createLoginFrame();
        }
        loginFrame.setVisible(true);
    }
    
    private void createLoginFrame() {
        loginFrame = new JFrame("Hermes Comercial PDV v3.1.0 - Login");
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
        
        JButton btnCriarBD = new JButton("🗄️ Criar BD");
        btnCriarBD.setBackground(new Color(52, 152, 219));
        btnCriarBD.setForeground(Color.WHITE);
        btnCriarBD.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnCriarBD.setPreferredSize(new Dimension(100, 40));
        btnCriarBD.addActionListener(e -> criarBancoDados());
        buttonPanel.add(btnCriarBD);
        
        loginPanel.add(buttonPanel, gbc);
        
        // Informações
        gbc.gridy = 5; gbc.fill = GridBagConstraints.HORIZONTAL; gbc.anchor = GridBagConstraints.WEST;
        JLabel infoLabel = new JLabel("<html><div style='color: white; font-size: 11px;'>" +
            "📋 Credenciais padrão: admin / admin123<br>" +
            "🗄️  Use 'Criar BD' na primeira execução<br>" +
            "💡 Banco H2 em modo arquivo (hermes.db)</div></html>");
        loginPanel.add(infoLabel, gbc);
        
        // ActionListeners
        txtUsuario.addActionListener(e -> realizarLogin());
        txtSenha.addActionListener(e -> realizarLogin());
        
        txtUsuario.requestFocus();
        
        mainPanel.add(loginPanel, BorderLayout.CENTER);
        loginFrame.add(mainPanel);
        
        // Tentar criar banco automaticamente
        SwingUtilities.invokeLater(() -> {
            try {
                if (!bancoExiste()) {
                    SystemLogger.database("Banco H2 não encontrado, criando automaticamente");
                    criarBancoDados();
                } else {
                    SystemLogger.database("Banco H2 já existe");
                }
            } catch (Exception e) {
                SystemLogger.error("Erro ao verificar banco H2", e);
            }
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
            Connection conn = getH2Connection();
            SystemLogger.database("Conexão H2 estabelecida para login");
            
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
                            "• Clique em 'Criar BD' se for primeira execução", 
                            "Erro de Autenticação", JOptionPane.ERROR_MESSAGE);
                        
                        txtSenha.setText("");
                        txtUsuario.requestFocus();
                    }
                }
            }
            
            conn.close();
            SystemLogger.database("Conexão H2 fechada após login");
        } catch (Exception e) {
            SystemLogger.error("Erro durante processo de login para usuário: " + usuario, e);
            JOptionPane.showMessageDialog(loginFrame, 
                "❌ Erro no login:\n\n" + e.getMessage() + "\n\n" +
                "💡 Tente clicar em 'Criar BD' primeiro", 
                "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void criarBancoDados() {
        SystemLogger.database("Iniciando criação do banco de dados H2");
        try {
            long startTime = System.currentTimeMillis();
            Connection conn = getH2Connection();
            SystemLogger.database("Conexão H2 estabelecida para criação do banco");
            
            // Criar tabela usuario
            String createUsuario = "CREATE TABLE IF NOT EXISTS usuario (" +
                "id BIGINT AUTO_INCREMENT PRIMARY KEY, " +
                "nome VARCHAR(100) NOT NULL, " +
                "email VARCHAR(100), " +
                "telefone VARCHAR(20), " +
                "cpf_cnpj VARCHAR(20), " +
                "data_cadastro DATE DEFAULT CURRENT_DATE" +
                ")";
            
            // Criar tabela login
            String createLogin = "CREATE TABLE IF NOT EXISTS login (" +
                "id BIGINT AUTO_INCREMENT PRIMARY KEY, " +
                "fk_usuario BIGINT, " +
                "login VARCHAR(50) NOT NULL, " +
                "senha VARCHAR(100) NOT NULL, " +
                "ativo BOOLEAN DEFAULT TRUE, " +
                "FOREIGN KEY (fk_usuario) REFERENCES usuario(id)" +
                ")";
            
            try (Statement stmt = conn.createStatement()) {
                SystemLogger.database("Criando tabela usuario");
                stmt.execute(createUsuario);
                SystemLogger.database("Criando tabela login");
                stmt.execute(createLogin);
                
                // Verificar se usuário admin já existe
                SystemLogger.database("Verificando se usuário admin já existe");
                ResultSet rs = stmt.executeQuery("SELECT COUNT(*) as total FROM usuario WHERE nome = 'Administrador'");
                if (rs.next() && rs.getInt("total") == 0) {
                    SystemLogger.database("Usuário admin não encontrado, criando...");
                    
                    // Inserir usuário administrador
                    stmt.executeUpdate("INSERT INTO usuario (nome, email, telefone, cpf_cnpj) " +
                        "VALUES ('Administrador', 'admin@hermescomercial.com', '00000000000', '00000000000')");
                    
                    ResultSet generatedKeys = stmt.getGeneratedKeys();
                    long usuarioId = 0;
                    if (generatedKeys.next()) {
                        usuarioId = generatedKeys.getLong(1);
                    }
                    
                    // Inserir login
                    stmt.executeUpdate("INSERT INTO login (fk_usuario, login, senha, ativo) " +
                        "VALUES (" + usuarioId + ", 'admin', 'admin123', TRUE)");
                    
                    long creationTime = System.currentTimeMillis() - startTime;
                    SystemLogger.database("Banco de dados criado em " + creationTime + "ms");
                    SystemLogger.audit("DATABASE_CREATED - Usuário admin criado com ID: " + usuarioId);
                    
                    JOptionPane.showMessageDialog(loginFrame, 
                        "✅ Banco de dados criado com sucesso!\n\n" +
                        "👤 Usuário: admin\n" +
                        "🔐 Senha: admin123\n" +
                        "🗄️  Banco: hermes.db (H2)\n\n" +
                        "💡 Agora você pode fazer login!", 
                        "Banco Criado", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    SystemLogger.database("Usuário admin já existe no banco");
                    JOptionPane.showMessageDialog(loginFrame, 
                        "✅ Banco de dados já configurado!\n\n" +
                        "👤 Use admin/admin123 para login", 
                        "Banco OK", JOptionPane.INFORMATION_MESSAGE);
                }
            }
            
            conn.close();
            SystemLogger.database("Conexão H2 fechada após criação do banco");
        } catch (Exception e) {
            SystemLogger.error("Erro ao criar banco de dados H2", e);
            JOptionPane.showMessageDialog(loginFrame, 
                "❌ Erro ao criar banco:\n\n" + e.getMessage(), 
                "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private boolean bancoExiste() {
        try {
            SystemLogger.database("Verificando se banco de dados existe");
            Connection conn = getH2Connection();
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT COUNT(*) as total FROM usuario");
            boolean existe = rs.next() && rs.getInt("total") > 0;
            conn.close();
            SystemLogger.database("Banco de dados " + (existe ? "existe" : "não existe"));
            return existe;
        } catch (Exception e) {
            SystemLogger.database("Erro ao verificar existência do banco: " + e.getMessage());
            return false;
        }
    }
    
    private Connection getH2Connection() throws Exception {
        String url = "jdbc:h2:file:./hermes;AUTO_SERVER=TRUE";
        String user = "sa";
        String password = "";
        
        SystemLogger.database("Obtendo conexão H2: " + url);
        Class.forName("org.h2.Driver");
        Connection conn = DriverManager.getConnection(url, user, password);
        SystemLogger.database("Conexão H2 obtida com sucesso");
        return conn;
    }
    
    private void abrirTelaPrincipal(String usuario, String nome) {
        SystemLogger.ui("Abrindo tela principal para usuário: " + usuario);
        SystemLogger.pdv("Tela principal do PDV iniciada");
        
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
            SystemLogger.audit("USER_LOGOUT: " + usuario);
            SystemLogger.ui("Usuário " + usuario + " fez logout");
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
                SystemLogger.operation("MENU_CLICK", item, "Usuário: " + usuario + " acessou módulo: " + item);
                JOptionPane.showMessageDialog(mainFrame, 
                    "🚀 Módulo: " + item + "\n\n" +
                    "✅ Sistema funcionando!\n" +
                    "👤 Usuário: " + nome + "\n" +
                    "🔐 Login: " + usuario + "\n" +
                    "🗄️  Banco: H2 Embed", 
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
            java.time.LocalDateTime.now().format(java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss")) + 
            " | 🗄️  Banco: H2 Embed");
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
        SystemLogger.pdv("Sistema PDV totalmente operacional");
        System.out.println("✅ Tela principal aberta com sucesso para usuário: " + usuario);
    }
    
    public static void main(String[] args) {
        SystemLogger.systemStartup();
        SystemLogger.info("Iniciando PDVLoginSimpleControllerH2");
        SwingUtilities.invokeLater(() -> {
            new PDVLoginSimpleControllerH2().show();
        });
    }
}
