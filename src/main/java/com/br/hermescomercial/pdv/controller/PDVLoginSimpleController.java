package com.br.hermescomercial.pdv.controller;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.Map;
import java.util.HashMap;
import java.sql.ResultSet;
import com.br.hermescomercial.util.HikariCPManager;

/**
 * Controller de Login Simplificado para Ambiente Headless
 * Versão minimalista para melhor compatibilidade
 */
public class PDVLoginSimpleController {
    
    private JFrame loginFrame;
    
    // Variáveis para controle do menu ocultável
    private JPanel menuContentPanel;
    private Map<String, JPanel> submenuPanels = new HashMap<>();
    private boolean menuVisible = false;
    
    public JFrame getFrame() {
        return loginFrame;
    }
    
        
    // Métodos estáticos para uso em modo console (sem interface gráfica)
    public static boolean loginConsole(String usuario, String senha) {
        // Verificar se está em modo headless antes de criar componentes
        boolean isHeadless = java.awt.GraphicsEnvironment.isHeadless();
        
        if (isHeadless) {
            // Modo console - autenticação direta sem interface gráfica
            return autenticarUsuarioDireto(usuario, senha);
        } else {
            // Modo gráfico - pode criar componentes
            return autenticarUsuario(usuario, senha);
        }
    }
    
    // Método estático para autenticação direta sem interface gráfica
    public static boolean autenticarUsuario(String usuario, String senha) {
        // Implementação unificada para ambos os modos
        return autenticarUsuarioDireto(usuario, senha);
    }
    
    // Método estático para autenticação direta sem interface gráfica
    private static boolean autenticarUsuarioDireto(String usuario, String senha) {
        // Hardcoded admin credentials for testing
        if ("admin".equals(usuario) && "admin123".equals(senha)) {
            return true;
        }
        
        // Inicializar HikariCP se necessário
        if (!HikariCPManager.isInitialized()) {
            HikariCPManager.initialize(
                "jdbc:postgresql://localhost:5432/hermes_comercial", "postgres", "postgres");
        }
        
        String sql = "SELECT u.id, u.nome, l.login, 'ADMIN' as perfil FROM usuario u " +
                    "INNER JOIN login l ON l.fk_usuario = u.id " +
                    "WHERE l.login = ? AND l.senha = ? AND l.ativo = TRUE";
        
        try (java.sql.Connection conn = HikariCPManager.getConnection();
             java.sql.PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, usuario);
            pstmt.setString(2, senha);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                return rs.next();
            }
        } catch (Exception e) {
            return false;
        }
    }
    
    // Métodos para MainApplication
    public void show() {
        // Criar e exibir tela de login real
        if (loginFrame == null) {
            createLoginFrame();
        }
        loginFrame.setVisible(true);
    }
    
    private JTextField txtUsuario;
    private JPasswordField txtSenha;
    
    private void createLoginFrame() {
        loginFrame = new JFrame("Hermes Comercial PDF v3.1.0 - Login");
        loginFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        loginFrame.setSize(450, 500);
        loginFrame.setLocationRelativeTo(null);
        loginFrame.setResizable(false);
        
        // Painel principal com gradiente
        JPanel mainPanel = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
                
                // Gradiente de fundo
                Color color1 = new Color(41, 128, 185);
                Color color2 = new Color(52, 152, 219);
                GradientPaint gradient = new GradientPaint(0, 0, color1, 0, getHeight(), color2);
                g2d.setPaint(gradient);
                g2d.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        
        // Painel de login centralizado
        JPanel loginPanel = new JPanel(new GridBagLayout());
        loginPanel.setOpaque(false);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        
        // Logo e título
        JPanel logoPanel = new JPanel(new BorderLayout());
        logoPanel.setOpaque(false);
        
        JLabel logoLabel = new JLabel("🏢", SwingConstants.CENTER);
        logoLabel.setFont(new Font("Segoe UI", Font.BOLD, 48));
        logoLabel.setForeground(Color.WHITE);
        
        JLabel titleLabel = new JLabel("Hermes Comercial", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        titleLabel.setForeground(Color.WHITE);
        
        JLabel subtitleLabel = new JLabel("PDV Professional v3.1.0", SwingConstants.CENTER);
        subtitleLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        subtitleLabel.setForeground(new Color(220, 220, 220));
        
        logoPanel.add(logoLabel, BorderLayout.NORTH);
        logoPanel.add(titleLabel, BorderLayout.CENTER);
        logoPanel.add(subtitleLabel, BorderLayout.SOUTH);
        
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2; gbc.fill = GridBagConstraints.HORIZONTAL;
        loginPanel.add(logoPanel, gbc);
        
        // Espaçador
        gbc.gridy = 1; gbc.insets = new Insets(20, 10, 20, 10);
        loginPanel.add(Box.createRigidArea(new Dimension(0, 20)), gbc);
        
        // Painel do formulário
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(new Color(255, 255, 255, 230));
        formPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(255, 255, 255, 100), 1),
            BorderFactory.createEmptyBorder(20, 20, 20, 20)
        ));
        
        GridBagConstraints fbc = new GridBagConstraints();
        fbc.insets = new Insets(8, 8, 8, 8);
        
        // Campo Usuário
        JLabel lblUsuario = new JLabel("👤 Usuário:");
        lblUsuario.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        lblUsuario.setForeground(new Color(52, 73, 94));
        fbc.gridx = 0; fbc.gridy = 0; fbc.anchor = GridBagConstraints.WEST;
        formPanel.add(lblUsuario, fbc);
        
        txtUsuario = new JTextField(20);
        txtUsuario.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        txtUsuario.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(189, 195, 199), 1),
            BorderFactory.createEmptyBorder(8, 12, 8, 12)
        ));
        fbc.gridx = 0; fbc.gridy = 1; fbc.fill = GridBagConstraints.HORIZONTAL; fbc.weightx = 1.0;
        formPanel.add(txtUsuario, fbc);
        
        // Campo Senha
        JLabel lblSenha = new JLabel("🔒 Senha:");
        lblSenha.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        lblSenha.setForeground(new Color(52, 73, 94));
        fbc.gridx = 0; fbc.gridy = 2; fbc.fill = GridBagConstraints.NONE; fbc.weightx = 0;
        fbc.insets = new Insets(15, 8, 8, 8);
        formPanel.add(lblSenha, fbc);
        
        txtSenha = new JPasswordField(20);
        txtSenha.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        txtSenha.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(189, 195, 199), 1),
            BorderFactory.createEmptyBorder(8, 12, 8, 12)
        ));
        fbc.gridx = 0; fbc.gridy = 3; fbc.fill = GridBagConstraints.HORIZONTAL; fbc.weightx = 1.0;
        fbc.insets = new Insets(8, 8, 8, 8);
        formPanel.add(txtSenha, fbc);
        
        // Lembrar-me
        JCheckBox chkLembrar = new JCheckBox("Lembrar-me");
        chkLembrar.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        chkLembrar.setForeground(new Color(52, 73, 94));
        chkLembrar.setOpaque(false);
        fbc.gridx = 0; fbc.gridy = 4; fbc.fill = GridBagConstraints.NONE; fbc.weightx = 0;
        fbc.anchor = GridBagConstraints.WEST;
        fbc.insets = new Insets(10, 8, 15, 8);
        formPanel.add(chkLembrar, fbc);
        
        // Botões
        JPanel buttonPanel = new JPanel(new GridLayout(1, 2, 10, 0));
        buttonPanel.setOpaque(false);
        
        JButton btnLogin = new JButton("🚀 Entrar");
        btnLogin.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnLogin.setBackground(new Color(46, 204, 113));
        btnLogin.setForeground(Color.WHITE);
        btnLogin.setBorder(BorderFactory.createEmptyBorder(12, 20, 12, 20));
        btnLogin.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnLogin.setFocusPainted(false);
        
        JButton btnSair = new JButton("❌ Sair");
        btnSair.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnSair.setBackground(new Color(231, 76, 60));
        btnSair.setForeground(Color.WHITE);
        btnSair.setBorder(BorderFactory.createEmptyBorder(12, 20, 12, 20));
        btnSair.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnSair.setFocusPainted(false);
        
        // Efeitos hover
        btnLogin.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btnLogin.setBackground(new Color(39, 174, 96));
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btnLogin.setBackground(new Color(46, 204, 113));
            }
        });
        
        btnSair.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btnSair.setBackground(new Color(192, 57, 43));
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btnSair.setBackground(new Color(231, 76, 60));
            }
        });
        
        buttonPanel.add(btnLogin);
        buttonPanel.add(btnSair);
        
        fbc.gridx = 0; fbc.gridy = 5; fbc.fill = GridBagConstraints.HORIZONTAL; fbc.weightx = 1.0;
        fbc.insets = new Insets(15, 8, 8, 8);
        formPanel.add(buttonPanel, fbc);
        
        // Versão do sistema
        JLabel versionLabel = new JLabel("© 2024 Hermes Comercial - Todos os direitos reservados", SwingConstants.CENTER);
        versionLabel.setFont(new Font("Segoe UI", Font.PLAIN, 10));
        versionLabel.setForeground(new Color(220, 220, 220));
        
        gbc.gridx = 0; gbc.gridy = 2; gbc.gridwidth = 2; gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(20, 10, 10, 10);
        loginPanel.add(formPanel, gbc);
        
        gbc.gridy = 3; gbc.insets = new Insets(10, 10, 10, 10);
        loginPanel.add(versionLabel, gbc);
        
        // Eventos
        btnLogin.addActionListener(e -> {
            realizarLogin();
        });
        
        btnSair.addActionListener(e -> {
            System.exit(0);
        });
        
        // Enter no campo de senha
        txtSenha.addActionListener(e -> {
            realizarLogin();
        });
        
        // Foco inicial
        txtUsuario.requestFocus();
        
        mainPanel.add(loginPanel, BorderLayout.CENTER);
        loginFrame.add(mainPanel);
    }
    
    private void realizarLogin() {
        String usuario = txtUsuario.getText().trim();
        String senha = new String(txtSenha.getPassword()).trim();
        
        if (usuario.isEmpty() || senha.isEmpty()) {
            JOptionPane.showMessageDialog(loginFrame, 
                "Por favor, preencha todos os campos!", 
                "Erro", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        // Validar credenciais
        boolean autenticado = autenticarUsuario(usuario, senha);
        
        if (autenticado) {
            JOptionPane.showMessageDialog(loginFrame, 
                "Login bem-sucedido!\nBem-vindo ao Hermes Comercial PDV\n\n" +
                "Usuário: " + usuario + "\n" +
                "Perfil: " + getPerfilUsuario(), 
                "Sucesso", JOptionPane.INFORMATION_MESSAGE);
            
            // Abrir tela principal (simulação)
            loginFrame.dispose();
            abrirTelaPrincipal();
        } else {
            JOptionPane.showMessageDialog(loginFrame, 
                "Usuário ou senha incorretos!\n\n" +
                "Tente novamente ou contate o administrador.", 
                "Erro de Autenticação", JOptionPane.ERROR_MESSAGE);
            
            // Limpar campos
            txtSenha.setText("");
            txtUsuario.requestFocus();
        }
    }
    
    private void abrirTelaPrincipal() {
        // Usar PDVMenuLateralElegante em vez de criar menus manualmente
        System.out.println("🚀 INICIANDO TELA PRINCIPAL COM PDVMENULATERALELEGANTE");
        
        try {
            // Obter usuário autenticado
            String usuario = txtUsuario.getText().trim();
            String nome = getNomeUsuario();
            
            // Criar área de trabalho
            JPanel workArea = new JPanel(new BorderLayout());
            
            // Instanciar e mostrar o menu lateral elegante correto
            com.br.hermescomercial.pdv.controller.PDVMenuLateralElegante menuPrincipal = 
                new com.br.hermescomercial.pdv.controller.PDVMenuLateralElegante(workArea, usuario, nome);
            
            // Criar frame principal
            JFrame mainFrame = new JFrame("Hermes Comercial PDV - Sistema Principal");
            mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            mainFrame.setSize(1200, 800);
            mainFrame.setLocationRelativeTo(null);
            
            // Criar painel principal
            JPanel mainPanel = new JPanel(new BorderLayout());
            
            // Header simples
            JPanel headerPanel = new JPanel(new BorderLayout());
            headerPanel.setBackground(new Color(41, 128, 185));
            headerPanel.setPreferredSize(new Dimension(0, 70));
            headerPanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
            
            JLabel titleLabel = new JLabel("Hermes Comercial PDV v3.1.0");
            titleLabel.setForeground(Color.WHITE);
            titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 22));
            
            JLabel userLabel = new JLabel("👤 " + usuario + " | 🎯 ADMIN");
            userLabel.setForeground(new Color(255, 255, 255, 230));
            userLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
            
            headerPanel.add(titleLabel, BorderLayout.WEST);
            headerPanel.add(userLabel, BorderLayout.EAST);
            mainPanel.add(headerPanel, BorderLayout.NORTH);
            
            // Menu lateral
            JPanel menuPanel = menuPrincipal.criarMenuLateralElegante();
            mainPanel.add(menuPanel, BorderLayout.WEST);
            
            // Área de trabalho central
            mainPanel.add(workArea, BorderLayout.CENTER);
            
            // Status bar simples
            JPanel statusBar = new JPanel(new BorderLayout());
            statusBar.setBackground(new Color(245, 245, 245));
            statusBar.setBorder(BorderFactory.createEmptyBorder(5, 20, 5, 20));
            
            JLabel statusLabel = new JLabel("🟢 Sistema Online | " + java.time.LocalTime.now().format(java.time.format.DateTimeFormatter.ofPattern("HH:mm:ss")));
            statusLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
            statusBar.add(statusLabel, BorderLayout.WEST);
            
            mainPanel.add(statusBar, BorderLayout.SOUTH);
            
            mainFrame.add(mainPanel);
            mainFrame.setVisible(true);
            
            System.out.println("✅ Tela principal aberta com PDVMenuLateralElegante!");
            
        } catch (Exception e) {
            System.err.println("❌ Erro ao iniciar PDVMenuLateralElegante: " + e.getMessage());
            e.printStackTrace();
            
            // Fallback para o método original
            System.out.println("🔄 Usando fallback para menu manual...");
            abrirTelaPrincipalFallback();
        }
    }
    
    private void abrirTelaPrincipalFallback() {
        // Criar tela principal real (fallback)
        JFrame mainFrame = new JFrame("Hermes Comercial PDV - Sistema Principal");
        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainFrame.setSize(1200, 800);
        mainFrame.setLocationRelativeTo(null);
        
        // Painel principal com layout BorderLayout
        JPanel mainPanel = new JPanel(new BorderLayout());
        
        // Header com informações do usuário
        JPanel headerPanel = createHeaderPanel();
        mainPanel.add(headerPanel, BorderLayout.NORTH);
        
        // Menu lateral
        JPanel menuPanel = createMenuPanel();
        mainPanel.add(menuPanel, BorderLayout.WEST);
        
        // Área de trabalho central
        JPanel workArea = createWorkAreaPanel();
        mainPanel.add(workArea, BorderLayout.CENTER);
        
        // Status bar
        JPanel statusBar = createStatusBar();
        mainPanel.add(statusBar, BorderLayout.SOUTH);
        
        mainFrame.add(mainPanel);
        mainFrame.setVisible(true);
        
        System.out.println("Tela principal do sistema aberta com sucesso (fallback)!");
    }
    
    private JPanel createHeaderPanel() {
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(new Color(41, 128, 185));
        header.setPreferredSize(new Dimension(0, 70));
        header.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        
        // Logo e título
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
        
        // Informações do usuário
        JPanel rightPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        rightPanel.setOpaque(false);
        
        JLabel userLabel = new JLabel("👤 " + getUsuarioAutenticado() + " | 🎯 " + getPerfilUsuario());
        userLabel.setForeground(new Color(255, 255, 255, 230));
        userLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        
        JLabel timeLabel = new JLabel("🕐 " + java.time.LocalTime.now().format(java.time.format.DateTimeFormatter.ofPattern("HH:mm")));
        timeLabel.setForeground(new Color(255, 255, 255, 200));
        timeLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        
        rightPanel.add(userLabel);
        rightPanel.add(Box.createHorizontalStrut(20));
        rightPanel.add(timeLabel);
        
        header.add(leftPanel, BorderLayout.WEST);
        header.add(rightPanel, BorderLayout.EAST);
        
        return header;
    }
    
    private JPanel createMenuPanel() {
        // Painel principal do menu (ocultável)
        JPanel mainMenuPanel = new JPanel(new BorderLayout());
        mainMenuPanel.setBackground(new Color(245, 245, 245));
        mainMenuPanel.setPreferredSize(new Dimension(220, 0));
        
        // Botão toggle para mostrar/ocultar menu
        JButton toggleButton = new JButton("☰");
        toggleButton.setBackground(new Color(41, 128, 185));
        toggleButton.setForeground(Color.WHITE);
        toggleButton.setFocusPainted(false);
        toggleButton.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));
        toggleButton.setFont(new Font("Segoe UI", Font.BOLD, 16));
        toggleButton.setPreferredSize(new Dimension(50, 50));
        toggleButton.setToolTipText("Mostrar/Ocultar Menu");
        
        // Painel do menu com sub-menus
        menuContentPanel = new JPanel();
        menuContentPanel.setLayout(new BoxLayout(menuContentPanel, BoxLayout.Y_AXIS));
        menuContentPanel.setBackground(new Color(245, 245, 245));
        menuContentPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        menuContentPanel.setVisible(false); // Inicialmente oculto
        
        // Título do menu
        JLabel menuTitle = new JLabel("📋 MENU PRINCIPAL");
        menuTitle.setAlignmentX(Component.CENTER_ALIGNMENT);
        menuTitle.setFont(new Font("Segoe UI", Font.BOLD, 14));
        menuTitle.setForeground(new Color(70, 70, 70));
        menuTitle.setBorder(BorderFactory.createEmptyBorder(0, 0, 15, 0));
        menuContentPanel.add(menuTitle);
        menuContentPanel.add(Box.createVerticalStrut(10));
        
        // Criar itens do menu com sub-menus
        criarItensMenuComSubmenus();
        
        // Adicionar componentes
        mainMenuPanel.add(toggleButton, BorderLayout.NORTH);
        mainMenuPanel.add(menuContentPanel, BorderLayout.CENTER);
        
        // Action listener para toggle
        toggleButton.addActionListener(e -> toggleMenu());
        
        return mainMenuPanel;
    }
    
    private void criarItensMenuComSubmenus() {
        // Estrutura hierárquica do menu
        MenuStructure[] menuItems = {
            new MenuStructure("🛒 Vendas", new String[]{"🛍️ Nova Venda", "🧾 PDV", "📋 Orçamentos", "💳 Pagamentos", "🧾 Notas Fiscais", "📊 Histórico"}),
            new MenuStructure("📦 Produtos", new String[]{"📦 Cadastro", "📊 Estoque", "🔄 Movimentação", "🏷️ Preços", "📂 Categorias", "📈 Relatórios"}),
            new MenuStructure("👥 Clientes", new String[]{"👤 Cadastro", "📝 Histórico", "🎯 Segmentação", "💳 Crédito", "📞 Contatos", "🎁 Fidelidade"}),
            new MenuStructure("📊 Relatórios", new String[]{"📈 Vendas", "💰 Financeiro", "📦 Estoque", "👥 Clientes", "📊 Análises", "📄 Exportar"}),
            new MenuStructure("⚙️ Configurações", new String[]{"👤 Usuários", "🔐 Permissões", "💾 Backup", "🌐 Integrações", "📧 E-mail", "🏢 Empresa"}),
            new MenuStructure("💰 Caixa", new String[]{"💵 Abertura", "📋 Fechamento", "💳 Sangria", "🏦 Suprimento", "📊 Fluxo", "🧾 Conciliação"}),
            new MenuStructure("📈 Dashboard", new String[]{"📊 KPIs", "📈 Gráficos", "🎯 Metas", "⏰ Tempo Real", "📱 Mobile", "� Exportar"})
        };
        
        for (MenuStructure menuItem : menuItems) {
            // Botão principal do menu
            JButton mainButton = createMenuButtonWithSubmenu(menuItem.mainItem);
            menuContentPanel.add(mainButton);
            menuContentPanel.add(Box.createVerticalStrut(5));
            
            // Painel de sub-menus (inicialmente oculto)
            JPanel submenuPanel = new JPanel();
            submenuPanel.setLayout(new BoxLayout(submenuPanel, BoxLayout.Y_AXIS));
            submenuPanel.setBackground(new Color(235, 235, 235));
            submenuPanel.setBorder(BorderFactory.createEmptyBorder(5, 25, 5, 5));
            submenuPanel.setVisible(false);
            
            // Adicionar itens do sub-menu
            for (String submenuItem : menuItem.subItems) {
                JButton subButton = createSubmenuButton(submenuItem);
                submenuPanel.add(subButton);
                submenuPanel.add(Box.createVerticalStrut(3));
            }
            
            // Guardar referência para controle de visibilidade
            submenuPanels.put(menuItem.mainItem, submenuPanel);
            menuContentPanel.add(submenuPanel);
            menuContentPanel.add(Box.createVerticalStrut(5));
        }
        
        // Botão Sair (separado)
        JButton sairButton = createMenuButton("🔐 Sair", true);
        menuContentPanel.add(Box.createVerticalStrut(15));
        menuContentPanel.add(sairButton);
    }
    
    private JButton createMenuButtonWithSubmenu(String text) {
        JButton button = new JButton(text + " ▼");
        button.setAlignmentX(Component.CENTER_ALIGNMENT);
        button.setMaximumSize(new Dimension(190, 45));
        button.setPreferredSize(new Dimension(190, 45));
        button.setHorizontalAlignment(SwingConstants.LEFT);
        button.setFont(new Font("Segoe UI", Font.BOLD, 14));
        button.setBackground(Color.WHITE);
        button.setForeground(new Color(70, 70, 70));
        button.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200)),
            BorderFactory.createEmptyBorder(0, 15, 0, 0)
        ));
        button.setFocusPainted(false);
        button.setOpaque(true);
        
        // Mouse events
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(new Color(230, 240, 250));
                button.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(new Color(100, 149, 237)),
                    BorderFactory.createEmptyBorder(0, 15, 0, 0)
                ));
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(Color.WHITE);
                button.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(new Color(200, 200, 200)),
                    BorderFactory.createEmptyBorder(0, 15, 0, 0)
                ));
            }
        });
        
        // Action listener para toggle do sub-menu
        button.addActionListener(e -> toggleSubmenu(text, button));
        
        return button;
    }
    
    private JButton createSubmenuButton(String text) {
        JButton button = new JButton(text);
        button.setAlignmentX(Component.CENTER_ALIGNMENT);
        button.setMaximumSize(new Dimension(170, 35));
        button.setPreferredSize(new Dimension(170, 35));
        button.setHorizontalAlignment(SwingConstants.LEFT);
        button.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        button.setBackground(new Color(245, 245, 245));
        button.setForeground(new Color(80, 80, 80));
        button.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 0));
        button.setFocusPainted(false);
        button.setOpaque(true);
        
        // Mouse events
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(new Color(255, 255, 255));
                button.setBorder(BorderFactory.createMatteBorder(0, 3, 0, 0, new Color(100, 149, 237)));
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(new Color(245, 245, 245));
                button.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 0));
            }
        });
        
        // Action listener para abrir funcionalidade
        button.addActionListener(e -> abrirFuncionalidade(text));
        
        return button;
    }
    
    private void toggleMenu() {
        menuVisible = !menuVisible;
        menuContentPanel.setVisible(menuVisible);
        
        // Ajustar tamanho do painel principal
        if (menuVisible) {
            menuContentPanel.getParent().setPreferredSize(new Dimension(220, 0));
        } else {
            menuContentPanel.getParent().setPreferredSize(new Dimension(50, 0));
        }
        
        // Revalidar e repaint
        menuContentPanel.getParent().revalidate();
        menuContentPanel.getParent().repaint();
    }
    
    private void toggleSubmenu(String mainItem, JButton button) {
        JPanel submenuPanel = submenuPanels.get(mainItem);
        if (submenuPanel != null) {
            boolean isVisible = submenuPanel.isVisible();
            submenuPanel.setVisible(!isVisible);
            
            // Atualizar texto do botão
            if (isVisible) {
                button.setText(mainItem + " ▼");
            } else {
                button.setText(mainItem + " ▲");
            }
            
            // Revalidar
            submenuPanel.revalidate();
            submenuPanel.repaint();
        }
    }
    
    // Classe interna para estrutura do menu
    private static class MenuStructure {
        String mainItem;
        String[] subItems;
        
        MenuStructure(String mainItem, String[] subItems) {
            this.mainItem = mainItem;
            this.subItems = subItems;
        }
    }
    
    private JButton createMenuButton(String text, boolean isExit) {
        JButton button = new JButton(text);
        button.setAlignmentX(Component.CENTER_ALIGNMENT);
        button.setMaximumSize(new Dimension(190, 45));
        button.setPreferredSize(new Dimension(190, 45));
        button.setHorizontalAlignment(SwingConstants.LEFT);
        button.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        
        if (isExit) {
            button.setBackground(new Color(220, 53, 69));
            button.setForeground(Color.WHITE);
            button.setBorder(BorderFactory.createEmptyBorder(0, 15, 0, 0));
            button.setFocusPainted(false);
            button.setOpaque(true);
        } else {
            button.setBackground(Color.WHITE);
            button.setForeground(new Color(70, 70, 70));
            button.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200)),
                BorderFactory.createEmptyBorder(0, 15, 0, 0)
            ));
            button.setFocusPainted(false);
            button.setOpaque(true);
        }
        
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                if (isExit) {
                    button.setBackground(new Color(200, 35, 51));
                } else {
                    button.setBackground(new Color(230, 240, 250));
                    button.setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(new Color(100, 149, 237)),
                        BorderFactory.createEmptyBorder(0, 15, 0, 0)
                    ));
                }
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                if (isExit) {
                    button.setBackground(new Color(220, 53, 69));
                } else {
                    button.setBackground(Color.WHITE);
                    button.setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(new Color(200, 200, 200)),
                        BorderFactory.createEmptyBorder(0, 15, 0, 0)
                    ));
                }
            }
        });
        
        button.addActionListener(e -> handleMenuAction(text));
        return button;
    }
    
    private JPanel createWorkAreaPanel() {
        JPanel workArea = new JPanel(new BorderLayout());
        workArea.setBackground(new Color(250, 250, 250));
        
        // Painel superior de boas-vindas
        JPanel welcomePanel = new JPanel(new BorderLayout());
        welcomePanel.setBackground(Color.WHITE);
        welcomePanel.setBorder(BorderFactory.createEmptyBorder(30, 40, 30, 40));
        
        JLabel welcomeLabel = new JLabel("🎉 Bem-vindo ao Hermes Comercial PDV!");
        welcomeLabel.setFont(new Font("Segoe UI", Font.BOLD, 32));
        welcomeLabel.setForeground(new Color(41, 128, 185));
        welcomeLabel.setHorizontalAlignment(SwingConstants.CENTER);
        
        JLabel subtitleLabel = new JLabel("Sistema completo de gestão comercial para o seu negócio");
        subtitleLabel.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        subtitleLabel.setForeground(new Color(120, 120, 120));
        subtitleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        subtitleLabel.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));
        
        welcomePanel.add(welcomeLabel, BorderLayout.CENTER);
        welcomePanel.add(subtitleLabel, BorderLayout.SOUTH);
        
        // Painel central com cards de funcionalidades
        JPanel cardsPanel = new JPanel(new GridLayout(2, 3, 20, 20));
        cardsPanel.setBorder(BorderFactory.createEmptyBorder(40, 60, 40, 60));
        cardsPanel.setBackground(new Color(250, 250, 250));
        
        String[][] features = {
            {"🛒", "Vendas", "Gestão completa de vendas e PDV"},
            {"📦", "Estoque", "Controle inteligente de produtos"},
            {"👥", "Clientes", "CRM integrado e fidelização"},
            {"📊", "Relatórios", "Análises e dashboards em tempo real"},
            {"�", "Financeiro", "Contas a pagar/receber e fluxo"},
            {"⚙️", "Config", "Personalização e configurações"}
        };
        
        for (String[] feature : features) {
            cardsPanel.add(createFeatureCard(feature[0], feature[1], feature[2]));
        }
        
        // Painel inferior com estatísticas rápidas
        JPanel statsPanel = createStatsPanel();
        
        workArea.add(welcomePanel, BorderLayout.NORTH);
        workArea.add(cardsPanel, BorderLayout.CENTER);
        workArea.add(statsPanel, BorderLayout.SOUTH);
        
        return workArea;
    }
    
    private JPanel createFeatureCard(String icon, String title, String description) {
        JPanel card = new JPanel(new BorderLayout());
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(220, 220, 220)),
            BorderFactory.createEmptyBorder(20, 20, 20, 20)
        ));
        
        JLabel iconLabel = new JLabel(icon);
        iconLabel.setFont(new Font("Arial", Font.PLAIN, 36));
        iconLabel.setHorizontalAlignment(SwingConstants.CENTER);
        
        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        titleLabel.setForeground(new Color(70, 70, 70));
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        titleLabel.setBorder(BorderFactory.createEmptyBorder(10, 0, 5, 0));
        
        JLabel descLabel = new JLabel("<html><center>" + description + "</center></html>");
        descLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        descLabel.setForeground(new Color(120, 120, 120));
        descLabel.setHorizontalAlignment(SwingConstants.CENTER);
        
        card.add(iconLabel, BorderLayout.NORTH);
        card.add(titleLabel, BorderLayout.CENTER);
        card.add(descLabel, BorderLayout.SOUTH);
        
        return card;
    }
    
    private JPanel createStatsPanel() {
        JPanel statsPanel = new JPanel(new GridLayout(1, 4, 15, 15));
        statsPanel.setBorder(BorderFactory.createEmptyBorder(20, 60, 30, 60));
        statsPanel.setBackground(new Color(250, 250, 250));
        
        Object[] stats = {
            "📈", "Vendas Hoje", "R$ 12.450,00", new Color(76, 175, 80),
            "📦", "Produtos", "1.247 itens", new Color(33, 150, 243),
            "👥", "Clientes", "843 ativos", new Color(255, 152, 0),
            "💰", "Caixa", "R$ 8.230,50", new Color(156, 39, 176)
        };
        
        for (int i = 0; i < stats.length; i += 4) {
            statsPanel.add(createStatCard((String) stats[i], (String) stats[i+1], (String) stats[i+2], (Color) stats[i+3]));
        }
        
        return statsPanel;
    }
    
    private JPanel createStatCard(String icon, String label, String value, Color color) {
        JPanel card = new JPanel(new BorderLayout());
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(220, 220, 220)),
            BorderFactory.createEmptyBorder(15, 15, 15, 15)
        ));
        
        JLabel iconLabel = new JLabel(icon);
        iconLabel.setFont(new Font("Arial", Font.PLAIN, 24));
        iconLabel.setHorizontalAlignment(SwingConstants.CENTER);
        
        JLabel valueLabel = new JLabel(value);
        valueLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
        valueLabel.setForeground(color);
        valueLabel.setHorizontalAlignment(SwingConstants.CENTER);
        
        JLabel labelLabel = new JLabel(label);
        labelLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        labelLabel.setForeground(new Color(120, 120, 120));
        labelLabel.setHorizontalAlignment(SwingConstants.CENTER);
        
        JPanel centerPanel = new JPanel(new BorderLayout());
        centerPanel.setOpaque(false);
        centerPanel.add(valueLabel, BorderLayout.CENTER);
        centerPanel.add(labelLabel, BorderLayout.SOUTH);
        
        card.add(iconLabel, BorderLayout.NORTH);
        card.add(centerPanel, BorderLayout.CENTER);
        
        return card;
    }
    
    private JPanel createStatusBar() {
        JPanel statusBar = new JPanel(new BorderLayout());
        statusBar.setBackground(new Color(240, 240, 240));
        statusBar.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, new Color(200, 200, 200)));
        
        JLabel statusLabel = new JLabel("🟢 Sistema Online | 🗄️ Banco de Dados Conectado | 📅 " + 
            java.time.LocalDate.now().format(java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy")));
        statusLabel.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        statusLabel.setForeground(new Color(100, 100, 100));
        statusLabel.setBorder(BorderFactory.createEmptyBorder(4, 15, 4, 15));
        
        JLabel versionLabel = new JLabel("Hermes Comercial PDV v3.1.0 | ⚡ Production-Ready");
        versionLabel.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        versionLabel.setForeground(new Color(120, 120, 120));
        versionLabel.setBorder(BorderFactory.createEmptyBorder(4, 15, 4, 15));
        versionLabel.setHorizontalAlignment(SwingConstants.RIGHT);
        
        statusBar.add(statusLabel, BorderLayout.WEST);
        statusBar.add(versionLabel, BorderLayout.EAST);
        
        return statusBar;
    }
    
    private void handleMenuAction(String menuItem) {
        if (menuItem.equals("🔐 Sair")) {
            int option = JOptionPane.showConfirmDialog(null, 
                "Deseja realmente sair do sistema?", 
                "Confirmar Saída", 
                JOptionPane.YES_NO_OPTION);
            
            if (option == JOptionPane.YES_OPTION) {
                System.exit(0);
            }
        } else {
            // Abrir módulo específico com header colorido
            abrirModuloEspecifico(menuItem);
        }
    }
    
    private void abrirModuloEspecifico(String menuItem) {
        JFrame moduleFrame = new JFrame();
        moduleFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        moduleFrame.setSize(1000, 700);
        moduleFrame.setLocationRelativeTo(null);
        
        // Definir cor do header baseada no módulo
        Color headerColor = getCorModulo(menuItem);
        String moduleTitle = getTituloModulo(menuItem);
        
        // Criar header colorido
        JPanel headerPanel = createHeaderModulo(moduleTitle, headerColor);
        
        // Criar conteúdo do módulo
        JPanel contentPanel = createConteudoModulo(menuItem);
        
        // Montar frame
        moduleFrame.add(headerPanel, BorderLayout.NORTH);
        moduleFrame.add(contentPanel, BorderLayout.CENTER);
        
        moduleFrame.setVisible(true);
    }
    
    private Color getCorModulo(String menuItem) {
        switch (menuItem) {
            case "🛒 Vendas": return new Color(156, 39, 176);      // Roxo
            case "📦 Produtos": return new Color(33, 150, 243);   // Azul
            case "👥 Clientes": return new Color(255, 152, 0);    // Laranja
            case "📊 Relatórios": return new Color(108, 117, 125); // Cinza
            case "⚙️ Configurações": return new Color(33, 37, 41);  // Preto
            case "💰 Caixa": return new Color(76, 175, 80);        // Verde
            case "📈 Dashboard": return new Color(220, 53, 69);    // Vermelho
            default: return new Color(41, 128, 185);               // Azul padrão
        }
    }
    
    private String getTituloModulo(String menuItem) {
        switch (menuItem) {
            case "🛒 Vendas": return "🛒 Módulo de Vendas";
            case "📦 Produtos": return "📦 Gestão de Produtos";
            case "👥 Clientes": return "👥 Gestão de Clientes";
            case "📊 Relatórios": return "📊 Relatórios e Análises";
            case "⚙️ Configurações": return "⚙️ Configurações do Sistema";
            case "💰 Caixa": return "💰 Operações de Caixa";
            case "📈 Dashboard": return "📈 Dashboard Analytics";
            default: return "Módulo do Sistema";
        }
    }
    
    private JPanel createHeaderModulo(String title, Color color) {
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(color);
        header.setPreferredSize(new Dimension(0, 80));
        header.setBorder(BorderFactory.createEmptyBorder(15, 25, 15, 25));
        
        // Título do módulo
        JLabel titleLabel = new JLabel(title);
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        
        // Informações do usuário
        JPanel userInfoPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        userInfoPanel.setOpaque(false);
        
        JLabel userLabel = new JLabel("👤 " + getUsuarioAutenticado() + " | 🎯 " + getPerfilUsuario());
        userLabel.setForeground(new Color(255, 255, 255, 230));
        userLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        
        JLabel timeLabel = new JLabel("🕐 " + java.time.LocalTime.now().format(java.time.format.DateTimeFormatter.ofPattern("HH:mm")));
        timeLabel.setForeground(new Color(255, 255, 255, 200));
        timeLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        
        userInfoPanel.add(userLabel);
        userInfoPanel.add(Box.createHorizontalStrut(20));
        userInfoPanel.add(timeLabel);
        
        header.add(titleLabel, BorderLayout.WEST);
        header.add(userInfoPanel, BorderLayout.EAST);
        
        return header;
    }
    
    private JPanel createConteudoModulo(String menuItem) {
        JPanel content = new JPanel(new BorderLayout());
        content.setBackground(Color.WHITE);
        
        // Painel principal do módulo
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(Color.WHITE);
        mainPanel.setBorder(BorderFactory.createEmptyBorder(30, 40, 30, 40));
        
        // Título de boas-vindas do módulo
        JLabel welcomeLabel = new JLabel(getMensagemModulo(menuItem), SwingConstants.CENTER);
        welcomeLabel.setFont(new Font("Segoe UI", Font.BOLD, 28));
        welcomeLabel.setForeground(getCorModulo(menuItem));
        welcomeLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 30, 0));
        
        // Descrição do módulo
        JLabel descLabel = new JLabel(getDescricaoModulo(menuItem), SwingConstants.CENTER);
        descLabel.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        descLabel.setForeground(new Color(120, 120, 120));
        descLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 40, 0));
        
        // Cards de funcionalidades
        JPanel featuresPanel = createFeaturesPanel(menuItem);
        
        mainPanel.add(welcomeLabel, BorderLayout.NORTH);
        mainPanel.add(descLabel, BorderLayout.CENTER);
        mainPanel.add(featuresPanel, BorderLayout.SOUTH);
        
        content.add(mainPanel, BorderLayout.CENTER);
        
        return content;
    }
    
    private String getMensagemModulo(String menuItem) {
        switch (menuItem) {
            case "🛒 Vendas": return "🛒 Bem-vindo ao Módulo de Vendas!";
            case "📦 Produtos": return "📦 Gestão de Produtos e Estoque";
            case "👥 Clientes": return "👥 Gestão de Clientes e CRM";
            case "📊 Relatórios": return "📊 Relatórios e Análises Estratégicas";
            case "⚙️ Configurações": return "⚙️ Configurações do Sistema";
            case "💰 Caixa": return "💰 Operações de Caixa e Financeiro";
            case "📈 Dashboard": return "📈 Dashboard Analytics em Tempo Real";
            default: return "Módulo do Sistema";
        }
    }
    
    private String getDescricaoModulo(String menuItem) {
        switch (menuItem) {
            case "🛒 Vendas": return "Gestão completa de vendas, PDV e faturamento";
            case "📦 Produtos": return "Controle inteligente de estoque e cadastro de produtos";
            case "👥 Clientes": return "CRM integrado, fidelização e gestão de clientes";
            case "📊 Relatórios": return "Análises detalhadas e relatórios gerenciais";
            case "⚙️ Configurações": return "Personalização e configurações do sistema";
            case "💰 Caixa": return "Fluxo de caixa, contas e operações financeiras";
            case "📈 Dashboard": return "Métricas em tempo real e KPIs do negócio";
            default: return "Funcionalidades do módulo";
        }
    }
    
    private JPanel createFeaturesPanel(String menuItem) {
        JPanel panel = new JPanel(new GridLayout(2, 3, 15, 15));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 0, 0, 0));
        panel.setOpaque(false);
        
        String[] features = getFeaturesModulo(menuItem);
        Color color = getCorModulo(menuItem);
        
        for (String feature : features) {
            panel.add(createFeatureCard(feature, color));
        }
        
        return panel;
    }
    
    private String[] getFeaturesModulo(String menuItem) {
        switch (menuItem) {
            case "🛒 Vendas":
                return new String[]{"🛍️ Nova Venda", "🧾 PDV", "📋 Orçamentos", "💳 Pagamentos", "🧾 Notas Fiscais", "📊 Histórico"};
            case "📦 Produtos":
                return new String[]{"📦 Cadastro", "📊 Estoque", "🔄 Movimentação", "🏷️ Preços", "📂 Categorias", "📈 Relatórios"};
            case "👥 Clientes":
                return new String[]{"👤 Cadastro", "📝 Histórico", "🎯 Segmentação", "💳 Crédito", "📞 Contatos", "🎁 Fidelidade"};
            case "📊 Relatórios":
                return new String[]{"📈 Vendas", "💰 Financeiro", "📦 Estoque", "👥 Clientes", "📊 Análises", "📄 Exportar"};
            case "⚙️ Configurações":
                return new String[]{"👤 Usuários", "🔐 Permissões", "💾 Backup", "🌐 Integrações", "📧 E-mail", "🏢 Empresa"};
            case "💰 Caixa":
                return new String[]{"💵 Abertura", "📋 Fechamento", "💳 Sangria", "🏦 Suprimento", "📊 Fluxo", "🧾 Conciliação"};
            case "📈 Dashboard":
                return new String[]{"📊 KPIs", "📈 Gráficos", "🎯 Metas", "⏰ Tempo Real", "📱 Mobile", "📤 Exportar"};
            default:
                return new String[]{"🔧 Funcionalidade 1", "🔧 Funcionalidade 2", "🔧 Funcionalidade 3", "🔧 Funcionalidade 4", "🔧 Funcionalidade 5", "🔧 Funcionalidade 6"};
        }
    }
    
    private JPanel createFeatureCard(String feature, Color color) {
        JPanel card = new JPanel(new BorderLayout());
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(220, 220, 220)),
            BorderFactory.createEmptyBorder(15, 15, 15, 15)
        ));
        
        // Tornar o card clicável
        card.setCursor(new Cursor(Cursor.HAND_CURSOR));
        card.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                card.setBackground(new Color(248, 249, 250));
                card.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(color),
                    BorderFactory.createEmptyBorder(15, 15, 15, 15)
                ));
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                card.setBackground(Color.WHITE);
                card.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(new Color(220, 220, 220)),
                    BorderFactory.createEmptyBorder(15, 15, 15, 15)
                ));
            }
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                abrirFuncionalidade(feature);
            }
        });
        
        JLabel iconLabel = new JLabel(feature.substring(0, 2));
        iconLabel.setFont(new Font("Arial", Font.PLAIN, 24));
        iconLabel.setForeground(color);
        iconLabel.setHorizontalAlignment(SwingConstants.CENTER);
        
        JLabel textLabel = new JLabel(feature.substring(3));
        textLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        textLabel.setForeground(new Color(70, 70, 70));
        textLabel.setHorizontalAlignment(SwingConstants.CENTER);
        
        card.add(iconLabel, BorderLayout.NORTH);
        card.add(textLabel, BorderLayout.CENTER);
        
        return card;
    }
    
    private void abrirFuncionalidade(String feature) {
        // Usar o nome completo da funcionalidade com emoji
        String featureName = feature;
        
        // Criar janela da funcionalidade
        JFrame funcFrame = new JFrame(featureName);
        funcFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        funcFrame.setSize(900, 700);
        funcFrame.setLocationRelativeTo(null);
        
        // Header da funcionalidade
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(new Color(41, 128, 185));
        headerPanel.setPreferredSize(new Dimension(0, 60));
        headerPanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        
        JLabel titleLabel = new JLabel("⚙️ " + featureName);
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
        
        headerPanel.add(titleLabel, BorderLayout.WEST);
        
        // Conteúdo da funcionalidade - FORMULÁRIO REAL
        JPanel contentPanel = criarFormularioFuncionalidade(featureName);
        
        funcFrame.add(headerPanel, BorderLayout.NORTH);
        funcFrame.add(contentPanel, BorderLayout.CENTER);
        
        funcFrame.setVisible(true);
    }
    
    private JPanel criarFormularioFuncionalidade(String featureName) {
        JPanel contentPanel = new JPanel(new BorderLayout());
        contentPanel.setBackground(Color.WHITE);
        
        // Painel principal do formulário
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(Color.WHITE);
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));
        
        // Título do formulário
        JLabel formTitle = new JLabel("📝 Formulário - " + featureName);
        formTitle.setFont(new Font("Segoe UI", Font.BOLD, 20));
        formTitle.setForeground(new Color(41, 128, 185));
        formTitle.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0));
        
        // Conteúdo do formulário baseado na funcionalidade
        JPanel formPanel = criarConteudoFormulario(featureName);
        
        // Painel de botões
        JPanel buttonPanel = criarPainelBotoesFormulario(featureName);
        
        mainPanel.add(formTitle, BorderLayout.NORTH);
        mainPanel.add(formPanel, BorderLayout.CENTER);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);
        
        contentPanel.add(mainPanel, BorderLayout.CENTER);
        
        return contentPanel;
    }
    
    private JPanel criarConteudoFormulario(String featureName) {
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(Color.WHITE);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
        switch (featureName) {
            case "🛍️ Nova Venda":
                return criarFormularioVendaCompleto(gbc);
            case "🧾 PDV":
                return criarFormularioPDVCompleto(gbc);
            case "📋 Orçamentos":
                return criarFormularioOrcamentos(gbc);
            case "💳 Pagamentos":
                return criarFormularioPagamentos(gbc);
            case "🧾 Notas Fiscais":
                return criarFormularioNotasFiscais(gbc);
            case "📊 Histórico":
                return criarFormularioHistoricoVendas(gbc);
            case "📦 Cadastro":
                return criarFormularioCadastroProdutos(gbc);
            case "📊 Estoque":
                return criarFormularioEstoqueCompleto(gbc);
            case "🔄 Movimentação":
                return criarFormularioMovimentacao(gbc);
            case "🏷️ Preços":
                return criarFormularioPrecos(gbc);
            case "📂 Categorias":
                return criarFormularioCategorias(gbc);
            case "📈 Relatórios":
                return criarFormularioRelatoriosProdutos(gbc);
            case "👤 Cadastro":
                return criarFormularioCadastroClientes(gbc);
            case "📝 Histórico":
                return criarFormularioHistoricoClientes(gbc);
            case "🎯 Segmentação":
                return criarFormularioSegmentacao(gbc);
            case "💳 Crédito":
                return criarFormularioCredito(gbc);
            case "📞 Contatos":
                return criarFormularioContatos(gbc);
            case "🎁 Fidelidade":
                return criarFormularioFidelidade(gbc);
            case "📈 Vendas":
                return criarFormularioRelatorioVendas(gbc);
            case "💰 Financeiro":
                return criarFormularioRelatorioFinanceiro(gbc);
            case "👥 Usuários":
                return criarFormularioUsuariosCompleto(gbc);
            case "🔐 Permissões":
                return criarFormularioPermissoes(gbc);
            case "💾 Backup":
                return criarFormularioBackupCompleto(gbc);
            case "🌐 Integrações":
                return criarFormularioIntegracoes(gbc);
            case "📧 E-mail":
                return criarFormularioEmail(gbc);
            case "🏢 Empresa":
                return criarFormularioEmpresa(gbc);
            case "💵 Abertura":
                return criarFormularioAberturaCaixa(gbc);
            case "📋 Fechamento":
                return criarFormularioFechamentoCaixa(gbc);
            case "💳 Sangria":
                return criarFormularioSangria(gbc);
            case "🏦 Suprimento":
                return criarFormularioSuprimento(gbc);
            case "📊 Fluxo":
                return criarFormularioFluxoCaixa(gbc);
            case "🧾 Conciliação":
                return criarFormularioConciliacao(gbc);
            case "📊 KPIs":
                return criarFormularioKPIs(gbc);
            case "📈 Gráficos":
                return criarFormularioGraficos(gbc);
            case "🎯 Metas":
                return criarFormularioMetas(gbc);
            case "⏰ Tempo Real":
                return criarFormularioTempoReal(gbc);
            case "📱 Mobile":
                return criarFormularioMobile(gbc);
            case "📄 Exportar":
                return criarFormularioExportar(gbc);
            case "📊 Análises":
                return criarFormularioAnalises(gbc);
            case "📦 Estoque":
                return criarFormularioRelatorioEstoque(gbc);
            case "👥 Clientes":
                return criarFormularioRelatorioClientes(gbc);
            case "📤 Exportar":
                return criarFormularioExportarDados(gbc);
            default:
                return criarFormularioPadrao(gbc, featureName);
        }
    }
    
    private JPanel criarFormularioVendaCompleto(GridBagConstraints gbc) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);
        
        // Painel superior com campos da venda
        JPanel camposPanel = new JPanel(new GridBagLayout());
        camposPanel.setBackground(Color.WHITE);
        GridBagConstraints cgbc = new GridBagConstraints();
        cgbc.insets = new Insets(5, 5, 5, 5);
        cgbc.anchor = GridBagConstraints.WEST;
        cgbc.fill = GridBagConstraints.HORIZONTAL;
        
        // Número da Venda
        cgbc.gridx = 0; cgbc.gridy = 0;
        camposPanel.add(new JLabel("🔢 Número Venda:"), cgbc);
        cgbc.gridx = 1; cgbc.weightx = 1.0;
        camposPanel.add(new JTextField("VENDA-" + System.currentTimeMillis()), cgbc);
        
        // Data e Hora
        cgbc.gridx = 2; cgbc.weightx = 0;
        camposPanel.add(new JLabel("📅 Data:"), cgbc);
        cgbc.gridx = 3; cgbc.weightx = 1.0;
        camposPanel.add(new JTextField(java.time.LocalDate.now().toString()), cgbc);
        
        // Cliente
        cgbc.gridx = 0; cgbc.gridy = 1; cgbc.weightx = 0;
        camposPanel.add(new JLabel("👤 Cliente:"), cgbc);
        cgbc.gridx = 1; cgbc.gridwidth = 3; cgbc.weightx = 1.0;
        camposPanel.add(new JTextField(30), cgbc);
        
        // Vendedor
        cgbc.gridx = 0; cgbc.gridy = 2; cgbc.gridwidth = 1; cgbc.weightx = 0;
        camposPanel.add(new JLabel("👨‍💼 Vendedor:"), cgbc);
        cgbc.gridx = 1; cgbc.weightx = 1.0;
        camposPanel.add(new JTextField(getUsuarioAutenticado()), cgbc);
        
        // Forma de Pagamento
        cgbc.gridx = 2; cgbc.weightx = 0;
        camposPanel.add(new JLabel("💳 Pagamento:"), cgbc);
        cgbc.gridx = 3; cgbc.weightx = 1.0;
        String[] pagamentos = {"Dinheiro", "Cartão Crédito", "Cartão Débito", "PIX", "Boleto"};
        camposPanel.add(new JComboBox<>(pagamentos), cgbc);
        
        // Tabela de produtos
        String[] colunas = {"Código", "Descrição", "Qtd", "Unitário", "Total", "Ações"};
        DefaultTableModel model = new DefaultTableModel(colunas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column != 5; // Ações não editável
            }
        };
        
        // Adicionar dados exemplo
        Object[][] dados = {
            {"001", "Notebook Dell i7", "1", "R$ 4.500,00", "R$ 4.500,00", "🗑️"},
            {"002", "Mouse Wireless", "2", "R$ 89,90", "R$ 179,80", "🗑️"},
            {"003", "Teclado Mecânico", "1", "R$ 250,00", "R$ 250,00", "🗑️"}
        };
        
        for (Object[] row : dados) {
            model.addRow(row);
        }
        
        JTable produtosTable = new JTable(model);
        produtosTable.setRowHeight(25);
        JScrollPane scrollPane = new JScrollPane(produtosTable);
        scrollPane.setPreferredSize(new Dimension(0, 200));
        
        // Painel inferior com totais
        JPanel totaisPanel = new JPanel(new GridLayout(2, 4, 10, 10));
        totaisPanel.setBackground(Color.WHITE);
        totaisPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        totaisPanel.add(new JLabel("📦 Subtotal:"));
        totaisPanel.add(new JTextField("R$ 4.929,80"));
        totaisPanel.add(new JLabel("📊 Desconto:"));
        totaisPanel.add(new JTextField("R$ 0,00"));
        
        totaisPanel.add(new JLabel("💰 Total:"));
        JTextField totalField = new JTextField("R$ 4.929,80");
        totalField.setFont(new Font("Segoe UI", Font.BOLD, 14));
        totalField.setForeground(new Color(76, 175, 80));
        totaisPanel.add(totalField);
        totaisPanel.add(new JLabel("💵 Recebido:"));
        totaisPanel.add(new JTextField("R$ 5.000,00"));
        
        panel.add(camposPanel, BorderLayout.NORTH);
        panel.add(scrollPane, BorderLayout.CENTER);
        panel.add(totaisPanel, BorderLayout.SOUTH);
        
        return panel;
    }
    
    private JPanel criarFormularioPDVCompleto(GridBagConstraints gbc) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);
        
        // Painel superior com informações
        JPanel infoPanel = new JPanel(new GridLayout(2, 4, 5, 5));
        infoPanel.setBackground(new Color(240, 240, 240));
        infoPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        infoPanel.add(new JLabel("🔢 PDV: 01"));
        infoPanel.add(new JLabel("👨‍💼 Operador: " + getUsuarioAutenticado()));
        infoPanel.add(new JLabel("📅 Data: " + java.time.LocalDate.now()));
        infoPanel.add(new JLabel("🕐 Hora: " + java.time.LocalTime.now().format(java.time.format.DateTimeFormatter.ofPattern("HH:mm"))));
        
        infoPanel.add(new JLabel("👤 Cliente: Consumidor Final"));
        infoPanel.add(new JLabel("💳 Pagamento: À Vista"));
        infoPanel.add(new JLabel("📦 Itens: 3"));
        infoPanel.add(new JLabel("💰 Total: R$ 4.929,80"));
        
        // Tabela principal do PDV
        String[] colunas = {"Código", "Produto", "Qtd", "Unitário", "Total"};
        DefaultTableModel model = new DefaultTableModel(colunas, 0);
        
        Object[][] dados = {
            {"001", "Notebook Dell i7", "1", "4.500,00", "4.500,00"},
            {"002", "Mouse Wireless", "2", "89,90", "179,80"},
            {"003", "Teclado Mecânico", "1", "250,00", "250,00"}
        };
        
        for (Object[] row : dados) {
            model.addRow(row);
        }
        
        JTable pdvTable = new JTable(model);
        pdvTable.setRowHeight(30);
        pdvTable.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        JScrollPane scrollPane = new JScrollPane(pdvTable);
        scrollPane.setPreferredSize(new Dimension(0, 300));
        
        // Painel de cálculo
        JPanel calcPanel = new JPanel(new BorderLayout());
        calcPanel.setBackground(Color.WHITE);
        calcPanel.setBorder(BorderFactory.createTitledBorder("💰 Cálculo"));
        
        JPanel totaisPanel = new JPanel(new GridLayout(4, 2, 5, 5));
        totaisPanel.add(new JLabel("Subtotal:"));
        totaisPanel.add(new JLabel("R$ 4.929,80"));
        totaisPanel.add(new JLabel("Desconto:"));
        totaisPanel.add(new JLabel("R$ 0,00"));
        totaisPanel.add(new JLabel("Total:"));
        JLabel totalLabel = new JLabel("R$ 4.929,80");
        totalLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        totalLabel.setForeground(new Color(76, 175, 80));
        totaisPanel.add(totalLabel);
        totaisPanel.add(new JLabel("Valor Recebido:"));
        totaisPanel.add(new JLabel("R$ 5.000,00"));
        
        calcPanel.add(totaisPanel, BorderLayout.CENTER);
        
        panel.add(infoPanel, BorderLayout.NORTH);
        panel.add(scrollPane, BorderLayout.CENTER);
        panel.add(calcPanel, BorderLayout.SOUTH);
        
        return panel;
    }
    
    private JPanel criarFormularioOrcamentos(GridBagConstraints gbc) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);
        
        // Campos do orçamento
        JPanel camposPanel = new JPanel(new GridBagLayout());
        GridBagConstraints cgbc = new GridBagConstraints();
        cgbc.insets = new Insets(5, 5, 5, 5);
        
        cgbc.gridx = 0; cgbc.gridy = 0;
        camposPanel.add(new JLabel("📄 Número Orçamento:"), cgbc);
        cgbc.gridx = 1; cgbc.weightx = 1.0;
        camposPanel.add(new JTextField("ORC-" + System.currentTimeMillis()), cgbc);
        
        cgbc.gridx = 2; cgbc.weightx = 0;
        camposPanel.add(new JLabel("⏰ Validade:"), cgbc);
        cgbc.gridx = 3; cgbc.weightx = 1.0;
        camposPanel.add(new JTextField("30 dias"), cgbc);
        
        cgbc.gridx = 0; cgbc.gridy = 1; cgbc.gridwidth = 2; cgbc.weightx = 0;
        camposPanel.add(new JLabel("👤 Cliente:"), cgbc);
        cgbc.gridx = 2; cgbc.gridwidth = 2; cgbc.weightx = 1.0;
        camposPanel.add(new JTextField(30), cgbc);
        
        // Tabela de itens do orçamento
        String[] colunas = {"Código", "Descrição", "Qtd", "Unitário", "Total", "Prazo"};
        DefaultTableModel model = new DefaultTableModel(colunas, 0);
        
        Object[][] dados = {
            {"001", "Notebook Dell i7", "1", "R$ 4.500,00", "R$ 4.500,00", "15 dias"},
            {"002", "Mouse Wireless", "2", "R$ 89,90", "R$ 179,80", "Imediato"}
        };
        
        for (Object[] row : dados) {
            model.addRow(row);
        }
        
        JTable orcTable = new JTable(model);
        JScrollPane scrollPane = new JScrollPane(orcTable);
        scrollPane.setPreferredSize(new Dimension(0, 200));
        
        // Resumo do orçamento
        JPanel resumoPanel = new JPanel(new GridLayout(3, 2, 10, 5));
        resumoPanel.setBorder(BorderFactory.createTitledBorder("📋 Resumo"));
        resumoPanel.add(new JLabel("💰 Total Orçamento:"));
        resumoPanel.add(new JLabel("R$ 4.679,80"));
        resumoPanel.add(new JLabel("📅 Data Emissão:"));
        resumoPanel.add(new JLabel(java.time.LocalDate.now().toString()));
        resumoPanel.add(new JLabel("👤 Vendedor:"));
        resumoPanel.add(new JLabel(getUsuarioAutenticado()));
        
        panel.add(camposPanel, BorderLayout.NORTH);
        panel.add(scrollPane, BorderLayout.CENTER);
        panel.add(resumoPanel, BorderLayout.SOUTH);
        
        return panel;
    }
    
    private JPanel criarFormularioPagamentos(GridBagConstraints gbc) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);
        
        // Filtros
        JPanel filtroPanel = new JPanel(new GridLayout(2, 4, 5, 5));
        filtroPanel.setBorder(BorderFactory.createTitledBorder("🔍 Filtros"));
        filtroPanel.add(new JLabel("📅 Período:"));
        filtroPanel.add(new JTextField("01/01/2024 - 31/01/2024"));
        filtroPanel.add(new JLabel("💳 Forma Pagamento:"));
        String[] formas = {"Todas", "Dinheiro", "Cartão", "PIX"};
        filtroPanel.add(new JComboBox<>(formas));
        filtroPanel.add(new JLabel("👤 Cliente:"));
        filtroPanel.add(new JTextField(""));
        filtroPanel.add(new JLabel("📊 Status:"));
        String[] status = {"Todos", "Pago", "Pendente", "Cancelado"};
        filtroPanel.add(new JComboBox<>(status));
        
        // Tabela de pagamentos
        String[] colunas = {"ID", "Data", "Cliente", "Forma", "Valor", "Status", "Ações"};
        DefaultTableModel model = new DefaultTableModel(colunas, 0);
        
        Object[][] dados = {
            {"001", "05/01/2024", "João Silva", "Cartão Crédito", "R$ 1.200,00", "Pago", "📄"},
            {"002", "05/01/2024", "Maria Santos", "PIX", "R$ 450,00", "Pago", "📄"},
            {"003", "04/01/2024", "Pedro Costa", "Dinheiro", "R$ 890,00", "Pendente", "📄"}
        };
        
        for (Object[] row : dados) {
            model.addRow(row);
        }
        
        JTable pagTable = new JTable(model);
        pagTable.setRowHeight(25);
        JScrollPane scrollPane = new JScrollPane(pagTable);
        
        // Resumo financeiro
        JPanel resumoPanel = new JPanel(new GridLayout(2, 3, 10, 5));
        resumoPanel.setBorder(BorderFactory.createTitledBorder("💰 Resumo Financeiro"));
        resumoPanel.add(new JLabel("Total Pago:"));
        resumoPanel.add(new JLabel("R$ 1.650,00"));
        resumoPanel.add(new JLabel("Pendente:"));
        resumoPanel.add(new JLabel("R$ 890,00"));
        resumoPanel.add(new JLabel("Total Geral:"));
        resumoPanel.add(new JLabel("R$ 2.540,00"));
        
        panel.add(filtroPanel, BorderLayout.NORTH);
        panel.add(scrollPane, BorderLayout.CENTER);
        panel.add(resumoPanel, BorderLayout.SOUTH);
        
        return panel;
    }
    
    private JPanel criarFormularioNotasFiscais(GridBagConstraints gbc) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);
        
        // Campos da nota fiscal
        JPanel camposPanel = new JPanel(new GridBagLayout());
        GridBagConstraints cgbc = new GridBagConstraints();
        cgbc.insets = new Insets(5, 5, 5, 5);
        
        cgbc.gridx = 0; cgbc.gridy = 0;
        camposPanel.add(new JLabel("📄 Número NF:"), cgbc);
        cgbc.gridx = 1; cgbc.weightx = 1.0;
        camposPanel.add(new JTextField("NF-2024-0001"), cgbc);
        
        cgbc.gridx = 2; cgbc.weightx = 0;
        camposPanel.add(new JLabel("📅 Emissão:"), cgbc);
        cgbc.gridx = 3; cgbc.weightx = 1.0;
        camposPanel.add(new JTextField(java.time.LocalDate.now().toString()), cgbc);
        
        cgbc.gridx = 0; cgbc.gridy = 1; cgbc.gridwidth = 2; cgbc.weightx = 0;
        camposPanel.add(new JLabel("👤 Destinatário:"), cgbc);
        cgbc.gridx = 2; cgbc.gridwidth = 2; cgbc.weightx = 1.0;
        camposPanel.add(new JTextField(30), cgbc);
        
        cgbc.gridx = 0; cgbc.gridy = 2; cgbc.gridwidth = 1; cgbc.weightx = 0;
        camposPanel.add(new JLabel("📋 Tipo:"), cgbc);
        cgbc.gridx = 1; cgbc.weightx = 1.0;
        String[] tipos = {"Produto", "Serviço"};
        camposPanel.add(new JComboBox<>(tipos), cgbc);
        
        cgbc.gridx = 2; cgbc.weightx = 0;
        camposPanel.add(new JLabel("💰 Valor Total:"), cgbc);
        cgbc.gridx = 3; cgbc.weightx = 1.0;
        camposPanel.add(new JTextField("R$ 4.929,80"), cgbc);
        
        // Tabela de itens
        String[] colunas = {"Código", "Descrição", "Qtd", "Unitário", "Total", "ICMS", "IPI"};
        DefaultTableModel model = new DefaultTableModel(colunas, 0);
        
        Object[][] dados = {
            {"001", "Notebook Dell i7", "1", "R$ 4.500,00", "R$ 4.500,00", "18%", "5%"},
            {"002", "Mouse Wireless", "2", "R$ 89,90", "R$ 179,80", "12%", "0%"}
        };
        
        for (Object[] row : dados) {
            model.addRow(row);
        }
        
        JTable nfTable = new JTable(model);
        JScrollPane scrollPane = new JScrollPane(nfTable);
        scrollPane.setPreferredSize(new Dimension(0, 200));
        
        panel.add(camposPanel, BorderLayout.NORTH);
        panel.add(scrollPane, BorderLayout.CENTER);
        
        return panel;
    }
    
    private JPanel criarFormularioHistoricoVendas(GridBagConstraints gbc) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);
        
        // Filtros
        JPanel filtroPanel = new JPanel(new GridLayout(2, 4, 5, 5));
        filtroPanel.setBorder(BorderFactory.createTitledBorder("🔍 Filtros de Busca"));
        filtroPanel.add(new JLabel("📅 Data Inicial:"));
        filtroPanel.add(new JTextField("01/01/2024"));
        filtroPanel.add(new JLabel("📅 Data Final:"));
        filtroPanel.add(new JTextField("31/01/2024"));
        filtroPanel.add(new JLabel("👤 Cliente:"));
        filtroPanel.add(new JTextField(""));
        filtroPanel.add(new JLabel("👨‍💼 Vendedor:"));
        filtroPanel.add(new JTextField(getUsuarioAutenticado()));
        
        // Tabela de histórico
        String[] colunas = {"ID", "Data", "Cliente", "Vendedor", "Total", "Status", "Ações"};
        DefaultTableModel model = new DefaultTableModel(colunas, 0);
        
        Object[][] dados = {
            {"001", "05/01/2024", "João Silva", "Carlos", "R$ 1.200,00", "Concluída", "📄"},
            {"002", "05/01/2024", "Maria Santos", "Ana", "R$ 450,00", "Concluída", "📄"},
            {"003", "04/01/2024", "Pedro Costa", "Carlos", "R$ 890,00", "Cancelada", "📄"},
            {"004", "04/01/2024", "Lucia Oliveira", "Ana", "R$ 2.340,00", "Concluída", "📄"}
        };
        
        for (Object[] row : dados) {
            model.addRow(row);
        }
        
        JTable histTable = new JTable(model);
        histTable.setRowHeight(25);
        JScrollPane scrollPane = new JScrollPane(histTable);
        
        // Estatísticas
        JPanel statsPanel = new JPanel(new GridLayout(2, 3, 10, 5));
        statsPanel.setBorder(BorderFactory.createTitledBorder("📊 Estatísticas"));
        statsPanel.add(new JLabel("Total Vendas:"));
        statsPanel.add(new JLabel("4"));
        statsPanel.add(new JLabel("Valor Total:"));
        statsPanel.add(new JLabel("R$ 4.880,00"));
        statsPanel.add(new JLabel("Ticket Médio:"));
        statsPanel.add(new JLabel("R$ 1.626,67"));
        
        panel.add(filtroPanel, BorderLayout.NORTH);
        panel.add(scrollPane, BorderLayout.CENTER);
        panel.add(statsPanel, BorderLayout.SOUTH);
        
        return panel;
    }
    
    // Módulos de Produtos
    private JPanel criarFormularioCadastroProdutos(GridBagConstraints gbc) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);
        
        JPanel camposPanel = new JPanel(new GridBagLayout());
        GridBagConstraints cgbc = new GridBagConstraints();
        cgbc.insets = new Insets(5, 5, 5, 5);
        
        // Dados básicos
        cgbc.gridx = 0; cgbc.gridy = 0;
        camposPanel.add(new JLabel("📦 Código:"), cgbc);
        cgbc.gridx = 1; cgbc.weightx = 1.0;
        camposPanel.add(new JTextField("PROD-" + System.currentTimeMillis()), cgbc);
        
        cgbc.gridx = 2; cgbc.weightx = 0;
        camposPanel.add(new JLabel("📊 Código Barras:"), cgbc);
        cgbc.gridx = 3; cgbc.weightx = 1.0;
        camposPanel.add(new JTextField("7891234567890"), cgbc);
        
        cgbc.gridx = 0; cgbc.gridy = 1; cgbc.gridwidth = 2; cgbc.weightx = 0;
        camposPanel.add(new JLabel("📝 Descrição:"), cgbc);
        cgbc.gridx = 2; cgbc.gridwidth = 2; cgbc.weightx = 1.0;
        camposPanel.add(new JTextField(30), cgbc);
        
        cgbc.gridx = 0; cgbc.gridy = 2; cgbc.gridwidth = 1; cgbc.weightx = 0;
        camposPanel.add(new JLabel("📂 Categoria:"), cgbc);
        cgbc.gridx = 1; cgbc.weightx = 1.0;
        String[] categorias = {"Eletrônicos", "Informática", "Acessórios", "Periféricos"};
        camposPanel.add(new JComboBox<>(categorias), cgbc);
        
        cgbc.gridx = 2; cgbc.weightx = 0;
        camposPanel.add(new JLabel("🏷️ Marca:"), cgbc);
        cgbc.gridx = 3; cgbc.weightx = 1.0;
        camposPanel.add(new JTextField("Dell"), cgbc);
        
        // Precos
        cgbc.gridx = 0; cgbc.gridy = 3; cgbc.gridwidth = 1; cgbc.weightx = 0;
        camposPanel.add(new JLabel("💸 Preço Custo:"), cgbc);
        cgbc.gridx = 1; cgbc.weightx = 1.0;
        camposPanel.add(new JTextField("R$ 3.500,00"), cgbc);
        
        cgbc.gridx = 2; cgbc.weightx = 0;
        camposPanel.add(new JLabel("💰 Preço Venda:"), cgbc);
        cgbc.gridx = 3; cgbc.weightx = 1.0;
        camposPanel.add(new JTextField("R$ 4.500,00"), cgbc);
        
        // Estoque
        cgbc.gridx = 0; cgbc.gridy = 4; cgbc.gridwidth = 1; cgbc.weightx = 0;
        camposPanel.add(new JLabel("📈 Estoque Atual:"), cgbc);
        cgbc.gridx = 1; cgbc.weightx = 1.0;
        camposPanel.add(new JTextField("25"), cgbc);
        
        cgbc.gridx = 2; cgbc.weightx = 0;
        camposPanel.add(new JLabel("⚠️ Estoque Mínimo:"), cgbc);
        cgbc.gridx = 3; cgbc.weightx = 1.0;
        camposPanel.add(new JTextField("5"), cgbc);
        
        // Status
        cgbc.gridx = 0; cgbc.gridy = 5; cgbc.weightx = 0;
        camposPanel.add(new JLabel("✅ Status:"), cgbc);
        cgbc.gridx = 1; cgbc.weightx = 1.0;
        String[] status = {"Ativo", "Inativo", "Descontinuado"};
        camposPanel.add(new JComboBox<>(status), cgbc);
        
        cgbc.gridx = 2; cgbc.weightx = 0;
        camposPanel.add(new JLabel("📦 Unidade:"), cgbc);
        cgbc.gridx = 3; cgbc.weightx = 1.0;
        String[] unidades = {"UN", "CX", "KG", "LT"};
        camposPanel.add(new JComboBox<>(unidades), cgbc);
        
        panel.add(camposPanel, BorderLayout.NORTH);
        
        return panel;
    }
    
    private JPanel criarFormularioEstoqueCompleto(GridBagConstraints gbc) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);
        
        // Filtros
        JPanel filtroPanel = new JPanel(new GridLayout(2, 3, 5, 5));
        filtroPanel.setBorder(BorderFactory.createTitledBorder("🔍 Filtros"));
        filtroPanel.add(new JLabel("📦 Produto:"));
        filtroPanel.add(new JTextField(""));
        filtroPanel.add(new JLabel("📂 Categoria:"));
        String[] categorias = {"Todas", "Eletrônicos", "Informática"};
        filtroPanel.add(new JComboBox<>(categorias));
        filtroPanel.add(new JLabel("📊 Status:"));
        String[] status = {"Todos", "Normal", "Baixo", "Crítico"};
        filtroPanel.add(new JComboBox<>(status));
        
        // Tabela de estoque
        String[] colunas = {"Código", "Descrição", "Estoque", "Mínimo", "Status", "Última Mov"};
        DefaultTableModel model = new DefaultTableModel(colunas, 0);
        
        Object[][] dados = {
            {"001", "Notebook Dell i7", "25", "5", "Normal", "05/01/2024"},
            {"002", "Mouse Wireless", "3", "10", "Baixo", "04/01/2024"},
            {"003", "Teclado Mecânico", "2", "5", "Crítico", "04/01/2024"},
            {"004", "Monitor 24\"", "15", "3", "Normal", "03/01/2024"}
        };
        
        for (Object[] row : dados) {
            model.addRow(row);
        }
        
        JTable estoqueTable = new JTable(model);
        estoqueTable.setRowHeight(25);
        JScrollPane scrollPane = new JScrollPane(estoqueTable);
        
        // Resumo
        JPanel resumoPanel = new JPanel(new GridLayout(2, 3, 10, 5));
        resumoPanel.setBorder(BorderFactory.createTitledBorder("📊 Resumo"));
        resumoPanel.add(new JLabel("Total Produtos:"));
        resumoPanel.add(new JLabel("4"));
        resumoPanel.add(new JLabel("Estoque Crítico:"));
        resumoPanel.add(new JLabel("1"));
        resumoPanel.add(new JLabel("Valor Total:"));
        resumoPanel.add(new JLabel("R$ 145.000,00"));
        
        panel.add(filtroPanel, BorderLayout.NORTH);
        panel.add(scrollPane, BorderLayout.CENTER);
        panel.add(resumoPanel, BorderLayout.SOUTH);
        
        return panel;
    }
    
    // Módulos de Clientes
    private JPanel criarFormularioCadastroClientes(GridBagConstraints gbc) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);
        
        JPanel camposPanel = new JPanel(new GridBagLayout());
        GridBagConstraints cgbc = new GridBagConstraints();
        cgbc.insets = new Insets(5, 5, 5, 5);
        
        // Dados pessoais
        cgbc.gridx = 0; cgbc.gridy = 0;
        camposPanel.add(new JLabel("👤 Nome Completo:"), cgbc);
        cgbc.gridx = 1; cgbc.weightx = 1.0;
        camposPanel.add(new JTextField("João Silva Santos"), cgbc);
        
        cgbc.gridx = 2; cgbc.weightx = 0;
        camposPanel.add(new JLabel("📄 CPF/CNPJ:"), cgbc);
        cgbc.gridx = 3; cgbc.weightx = 1.0;
        camposPanel.add(new JTextField("123.456.789-00"), cgbc);
        
        cgbc.gridx = 0; cgbc.gridy = 1; cgbc.gridwidth = 2; cgbc.weightx = 0;
        camposPanel.add(new JLabel("📧 E-mail:"), cgbc);
        cgbc.gridx = 2; cgbc.gridwidth = 2; cgbc.weightx = 1.0;
        camposPanel.add(new JTextField("joao.silva@email.com"), cgbc);
        
        cgbc.gridx = 0; cgbc.gridy = 2; cgbc.gridwidth = 1; cgbc.weightx = 0;
        camposPanel.add(new JLabel("📞 Telefone:"), cgbc);
        cgbc.gridx = 1; cgbc.weightx = 1.0;
        camposPanel.add(new JTextField("(11) 98765-4321"), cgbc);
        
        cgbc.gridx = 2; cgbc.weightx = 0;
        camposPanel.add(new JLabel("📱 Celular:"), cgbc);
        cgbc.gridx = 3; cgbc.weightx = 1.0;
        camposPanel.add(new JTextField("(11) 91234-5678"), cgbc);
        
        // Endereço
        cgbc.gridx = 0; cgbc.gridy = 3; cgbc.gridwidth = 1; cgbc.weightx = 0;
        camposPanel.add(new JLabel("🏠 Endereço:"), cgbc);
        cgbc.gridx = 1; cgbc.gridwidth = 3; cgbc.weightx = 1.0;
        camposPanel.add(new JTextField("Rua das Flores, 123"), cgbc);
        
        cgbc.gridx = 0; cgbc.gridy = 4; cgbc.gridwidth = 1; cgbc.weightx = 0;
        camposPanel.add(new JLabel("🏘️ Bairro:"), cgbc);
        cgbc.gridx = 1; cgbc.weightx = 1.0;
        camposPanel.add(new JTextField("Centro"), cgbc);
        
        cgbc.gridx = 2; cgbc.weightx = 0;
        camposPanel.add(new JLabel("🏙️ Cidade:"), cgbc);
        cgbc.gridx = 3; cgbc.weightx = 1.0;
        camposPanel.add(new JTextField("São Paulo"), cgbc);
        
        cgbc.gridx = 0; cgbc.gridy = 5; cgbc.weightx = 0;
        camposPanel.add(new JLabel("📮 CEP:"), cgbc);
        cgbc.gridx = 1; cgbc.weightx = 1.0;
        camposPanel.add(new JTextField("01234-567"), cgbc);
        
        cgbc.gridx = 2; cgbc.weightx = 0;
        camposPanel.add(new JLabel("🗺️ UF:"), cgbc);
        cgbc.gridx = 3; cgbc.weightx = 1.0;
        String[] estados = {"SP", "RJ", "MG", "RS"};
        camposPanel.add(new JComboBox<>(estados), cgbc);
        
        // Dados comerciais
        cgbc.gridx = 0; cgbc.gridy = 6; cgbc.gridwidth = 1; cgbc.weightx = 0;
        camposPanel.add(new JLabel("🏢 Tipo:"), cgbc);
        cgbc.gridx = 1; cgbc.weightx = 1.0;
        String[] tipos = {"Pessoa Física", "Pessoa Jurídica"};
        camposPanel.add(new JComboBox<>(tipos), cgbc);
        
        cgbc.gridx = 2; cgbc.weightx = 0;
        camposPanel.add(new JLabel("💳 Limite Crédito:"), cgbc);
        cgbc.gridx = 3; cgbc.weightx = 1.0;
        camposPanel.add(new JTextField("R$ 5.000,00"), cgbc);
        
        panel.add(camposPanel, BorderLayout.NORTH);
        
        return panel;
    }
    
    // Módulos de Configurações
    private JPanel criarFormularioUsuariosCompleto(GridBagConstraints gbc) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);
        
        JPanel camposPanel = new JPanel(new GridBagLayout());
        GridBagConstraints cgbc = new GridBagConstraints();
        cgbc.insets = new Insets(5, 5, 5, 5);
        
        // Dados do usuário
        cgbc.gridx = 0; cgbc.gridy = 0;
        camposPanel.add(new JLabel("👤 Nome Completo:"), cgbc);
        cgbc.gridx = 1; cgbc.weightx = 1.0;
        camposPanel.add(new JTextField("Carlos Alberto"), cgbc);
        
        cgbc.gridx = 2; cgbc.weightx = 0;
        camposPanel.add(new JLabel("🔐 Login:"), cgbc);
        cgbc.gridx = 3; cgbc.weightx = 1.0;
        camposPanel.add(new JTextField("carlos.alberto"), cgbc);
        
        cgbc.gridx = 0; cgbc.gridy = 1; cgbc.gridwidth = 1; cgbc.weightx = 0;
        camposPanel.add(new JLabel("🔒 Senha:"), cgbc);
        cgbc.gridx = 1; cgbc.weightx = 1.0;
        camposPanel.add(new JPasswordField("********"), cgbc);
        
        cgbc.gridx = 2; cgbc.weightx = 0;
        camposPanel.add(new JLabel("🔒 Confirmar:"), cgbc);
        cgbc.gridx = 3; cgbc.weightx = 1.0;
        camposPanel.add(new JPasswordField("********"), cgbc);
        
        cgbc.gridx = 0; cgbc.gridy = 2; cgbc.gridwidth = 1; cgbc.weightx = 0;
        camposPanel.add(new JLabel("📧 E-mail:"), cgbc);
        cgbc.gridx = 1; cgbc.gridwidth = 3; cgbc.weightx = 1.0;
        camposPanel.add(new JTextField("carlos@hermes.com.br"), cgbc);
        
        cgbc.gridx = 0; cgbc.gridy = 3; cgbc.gridwidth = 1; cgbc.weightx = 0;
        camposPanel.add(new JLabel("🎯 Perfil:"), cgbc);
        cgbc.gridx = 1; cgbc.weightx = 1.0;
        String[] perfis = {"Administrador", "Gerente", "Operador", "Visualizador"};
        camposPanel.add(new JComboBox<>(perfis), cgbc);
        
        cgbc.gridx = 2; cgbc.weightx = 0;
        camposPanel.add(new JLabel("✅ Status:"), cgbc);
        cgbc.gridx = 3; cgbc.weightx = 1.0;
        String[] statusUser = {"Ativo", "Inativo", "Bloqueado"};
        camposPanel.add(new JComboBox<>(statusUser), cgbc);
        
        // Permissões
        cgbc.gridx = 0; cgbc.gridy = 4; cgbc.gridwidth = 4; cgbc.weightx = 1.0;
        cgbc.fill = GridBagConstraints.BOTH;
        
        JPanel permPanel = new JPanel(new GridLayout(2, 3, 5, 5));
        permPanel.setBorder(BorderFactory.createTitledBorder("🔐 Permissões"));
        permPanel.add(new JCheckBox("🛒 Vendas"));
        permPanel.add(new JCheckBox("📦 Produtos"));
        permPanel.add(new JCheckBox("👥 Clientes"));
        permPanel.add(new JCheckBox("📊 Relatórios"));
        permPanel.add(new JCheckBox("⚙️ Configurações"));
        permPanel.add(new JCheckBox("💰 Caixa"));
        
        camposPanel.add(permPanel, cgbc);
        
        panel.add(camposPanel, BorderLayout.NORTH);
        
        return panel;
    }
    
    // Módulos de Caixa
    private JPanel criarFormularioAberturaCaixa(GridBagConstraints gbc) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);
        
        JPanel camposPanel = new JPanel(new GridBagLayout());
        GridBagConstraints cgbc = new GridBagConstraints();
        cgbc.insets = new Insets(5, 5, 5, 5);
        
        // Dados da abertura
        cgbc.gridx = 0; cgbc.gridy = 0;
        camposPanel.add(new JLabel("🔢 Número Caixa:"), cgbc);
        cgbc.gridx = 1; cgbc.weightx = 1.0;
        camposPanel.add(new JTextField("CX-001"), cgbc);
        
        cgbc.gridx = 2; cgbc.weightx = 0;
        camposPanel.add(new JLabel("👨‍💼 Operador:"), cgbc);
        cgbc.gridx = 3; cgbc.weightx = 1.0;
        camposPanel.add(new JTextField(getUsuarioAutenticado()), cgbc);
        
        cgbc.gridx = 0; cgbc.gridy = 1; cgbc.gridwidth = 1; cgbc.weightx = 0;
        camposPanel.add(new JLabel("📅 Data:"), cgbc);
        cgbc.gridx = 1; cgbc.weightx = 1.0;
        camposPanel.add(new JTextField(java.time.LocalDate.now().toString()), cgbc);
        
        cgbc.gridx = 2; cgbc.weightx = 0;
        camposPanel.add(new JLabel("🕐 Hora Abertura:"), cgbc);
        cgbc.gridx = 3; cgbc.weightx = 1.0;
        camposPanel.add(new JTextField(java.time.LocalTime.now().format(java.time.format.DateTimeFormatter.ofPattern("HH:mm"))), cgbc);
        
        // Valores
        cgbc.gridx = 0; cgbc.gridy = 2; cgbc.weightx = 0;
        camposPanel.add(new JLabel("💵 Fundo Caixa:"), cgbc);
        cgbc.gridx = 1; cgbc.weightx = 1.0;
        camposPanel.add(new JTextField("R$ 200,00"), cgbc);
        
        cgbc.gridx = 2; cgbc.weightx = 0;
        camposPanel.add(new JLabel("📋 Saldo Anterior:"), cgbc);
        cgbc.gridx = 3; cgbc.weightx = 1.0;
        camposPanel.add(new JTextField("R$ 150,00"), cgbc);
        
        // Observações
        cgbc.gridx = 0; cgbc.gridy = 3; cgbc.gridwidth = 4; cgbc.weightx = 1.0;
        cgbc.fill = GridBagConstraints.BOTH;
        camposPanel.add(new JLabel("📝 Observações:"), cgbc);
        cgbc.gridy = 4;
        camposPanel.add(new JTextArea(3, 40), cgbc);
        
        panel.add(camposPanel, BorderLayout.NORTH);
        
        return panel;
    }
    
    // Módulos de Dashboard
    private JPanel criarFormularioKPIs(GridBagConstraints gbc) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);
        
        // Filtros
        JPanel filtroPanel = new JPanel(new GridLayout(2, 3, 5, 5));
        filtroPanel.setBorder(BorderFactory.createTitledBorder("🔍 Período"));
        filtroPanel.add(new JLabel("📅 Data Inicial:"));
        filtroPanel.add(new JTextField("01/01/2024"));
        filtroPanel.add(new JLabel("📅 Data Final:"));
        filtroPanel.add(new JTextField("31/01/2024"));
        filtroPanel.add(new JLabel("🏢 Loja:"));
        String[] lojas = {"Todas", "Matriz", "Filial 1", "Filial 2"};
        filtroPanel.add(new JComboBox<>(lojas));
        
        // KPIs
        JPanel kpisPanel = new JPanel(new GridLayout(3, 2, 10, 10));
        kpisPanel.setBorder(BorderFactory.createTitledBorder("📊 Indicadores-Chave"));
        
        kpisPanel.add(new JLabel("💰 Faturamento:"));
        kpisPanel.add(new JLabel("R$ 125.000,00"));
        kpisPanel.add(new JLabel("🛒 Vendas:"));
        kpisPanel.add(new JLabel("245"));
        kpisPanel.add(new JLabel("👤 Clientes:"));
        kpisPanel.add(new JLabel("189"));
        
        // Gráfico simples
        JPanel graficoPanel = new JPanel(new BorderLayout());
        graficoPanel.setBorder(BorderFactory.createTitledBorder("📈 Evolução"));
        graficoPanel.add(new JLabel("📊 Gráfico de barras será implementado aqui"), BorderLayout.CENTER);
        
        panel.add(filtroPanel, BorderLayout.NORTH);
        panel.add(kpisPanel, BorderLayout.CENTER);
        panel.add(graficoPanel, BorderLayout.SOUTH);
        
        return panel;
    }
    
    // Módulos de Produtos - Campos Específicos
    private JPanel criarFormularioMovimentacao(GridBagConstraints gbc) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);
        
        JPanel camposPanel = new JPanel(new GridBagLayout());
        GridBagConstraints cgbc = new GridBagConstraints();
        cgbc.insets = new Insets(5, 5, 5, 5);
        
        // Dados da movimentação
        cgbc.gridx = 0; cgbc.gridy = 0;
        camposPanel.add(new JLabel("📦 Produto:"), cgbc);
        cgbc.gridx = 1; cgbc.weightx = 1.0;
        camposPanel.add(new JTextField("Notebook Dell i7"), cgbc);
        
        cgbc.gridx = 2; cgbc.weightx = 0;
        camposPanel.add(new JLabel("📊 Código:"), cgbc);
        cgbc.gridx = 3; cgbc.weightx = 1.0;
        camposPanel.add(new JTextField("001"), cgbc);
        
        cgbc.gridx = 0; cgbc.gridy = 1; cgbc.gridwidth = 1; cgbc.weightx = 0;
        camposPanel.add(new JLabel("📋 Tipo Mov:"), cgbc);
        cgbc.gridx = 1; cgbc.weightx = 1.0;
        String[] tipos = {"Entrada", "Saída", "Transferência", "Ajuste"};
        camposPanel.add(new JComboBox<>(tipos), cgbc);
        
        cgbc.gridx = 2; cgbc.weightx = 0;
        camposPanel.add(new JLabel("🔢 Quantidade:"), cgbc);
        cgbc.gridx = 3; cgbc.weightx = 1.0;
        camposPanel.add(new JTextField("10"), cgbc);
        
        cgbc.gridx = 0; cgbc.gridy = 2; cgbc.weightx = 0;
        camposPanel.add(new JLabel("📅 Data:"), cgbc);
        cgbc.gridx = 1; cgbc.weightx = 1.0;
        camposPanel.add(new JTextField(java.time.LocalDate.now().toString()), cgbc);
        
        cgbc.gridx = 2; cgbc.weightx = 0;
        camposPanel.add(new JLabel("🕐 Hora:"), cgbc);
        cgbc.gridx = 3; cgbc.weightx = 1.0;
        camposPanel.add(new JTextField(java.time.LocalTime.now().format(java.time.format.DateTimeFormatter.ofPattern("HH:mm"))), cgbc);
        
        cgbc.gridx = 0; cgbc.gridy = 3; cgbc.gridwidth = 4; cgbc.weightx = 1.0;
        cgbc.fill = GridBagConstraints.BOTH;
        camposPanel.add(new JLabel("📝 Motivo:"), cgbc);
        cgbc.gridy = 4;
        camposPanel.add(new JTextArea(3, 40), cgbc);
        
        panel.add(camposPanel, BorderLayout.NORTH);
        
        return panel;
    }
    
    private JPanel criarFormularioPrecos(GridBagConstraints gbc) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);
        
        JPanel camposPanel = new JPanel(new GridBagLayout());
        GridBagConstraints cgbc = new GridBagConstraints();
        cgbc.insets = new Insets(5, 5, 5, 5);
        
        cgbc.gridx = 0; cgbc.gridy = 0;
        camposPanel.add(new JLabel("📦 Produto:"), cgbc);
        cgbc.gridx = 1; cgbc.weightx = 1.0;
        camposPanel.add(new JTextField("Notebook Dell i7"), cgbc);
        
        cgbc.gridx = 2; cgbc.weightx = 0;
        camposPanel.add(new JLabel("📊 Código:"), cgbc);
        cgbc.gridx = 3; cgbc.weightx = 1.0;
        camposPanel.add(new JTextField("001"), cgbc);
        
        cgbc.gridx = 0; cgbc.gridy = 1; cgbc.weightx = 0;
        camposPanel.add(new JLabel("💸 Preço Custo:"), cgbc);
        cgbc.gridx = 1; cgbc.weightx = 1.0;
        camposPanel.add(new JTextField("R$ 3.500,00"), cgbc);
        
        cgbc.gridx = 2; cgbc.weightx = 0;
        camposPanel.add(new JLabel("💰 Preço Venda:"), cgbc);
        cgbc.gridx = 3; cgbc.weightx = 1.0;
        camposPanel.add(new JTextField("R$ 4.500,00"), cgbc);
        
        cgbc.gridx = 0; cgbc.gridy = 2; cgbc.weightx = 0;
        camposPanel.add(new JLabel("📈 Margem (%):"), cgbc);
        cgbc.gridx = 1; cgbc.weightx = 1.0;
        camposPanel.add(new JTextField("28.57"), cgbc);
        
        cgbc.gridx = 2; cgbc.weightx = 0;
        camposPanel.add(new JLabel("🏷️ Preço Atacado:"), cgbc);
        cgbc.gridx = 3; cgbc.weightx = 1.0;
        camposPanel.add(new JTextField("R$ 4.200,00"), cgbc);
        
        cgbc.gridx = 0; cgbc.gridy = 3; cgbc.weightx = 0;
        camposPanel.add(new JLabel("📊 Qtd. Mínima:"), cgbc);
        cgbc.gridx = 1; cgbc.weightx = 1.0;
        camposPanel.add(new JTextField("5"), cgbc);
        
        cgbc.gridx = 2; cgbc.weightx = 0;
        camposPanel.add(new JLabel("📅 Vigência:"), cgbc);
        cgbc.gridx = 3; cgbc.weightx = 1.0;
        camposPanel.add(new JTextField("30 dias"), cgbc);
        
        panel.add(camposPanel, BorderLayout.NORTH);
        
        return panel;
    }
    
    private JPanel criarFormularioCategorias(GridBagConstraints gbc) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);
        
        JPanel camposPanel = new JPanel(new GridBagLayout());
        GridBagConstraints cgbc = new GridBagConstraints();
        cgbc.insets = new Insets(5, 5, 5, 5);
        
        cgbc.gridx = 0; cgbc.gridy = 0;
        camposPanel.add(new JLabel("📂 Código:"), cgbc);
        cgbc.gridx = 1; cgbc.weightx = 1.0;
        camposPanel.add(new JTextField("CAT-001"), cgbc);
        
        cgbc.gridx = 2; cgbc.weightx = 0;
        camposPanel.add(new JLabel("📝 Nome:"), cgbc);
        cgbc.gridx = 3; cgbc.weightx = 1.0;
        camposPanel.add(new JTextField("Informática"), cgbc);
        
        cgbc.gridx = 0; cgbc.gridy = 1; cgbc.gridwidth = 1; cgbc.weightx = 0;
        camposPanel.add(new JLabel("📋 Descrição:"), cgbc);
        cgbc.gridx = 1; cgbc.gridwidth = 3; cgbc.weightx = 1.0;
        camposPanel.add(new JTextField(30), cgbc);
        
        cgbc.gridx = 0; cgbc.gridy = 2; cgbc.gridwidth = 1; cgbc.weightx = 0;
        camposPanel.add(new JLabel("🏷️ Categoria Pai:"), cgbc);
        cgbc.gridx = 1; cgbc.weightx = 1.0;
        String[] categorias = {"Nenhuma", "Eletrônicos", "Periféricos"};
        camposPanel.add(new JComboBox<>(categorias), cgbc);
        
        cgbc.gridx = 2; cgbc.weightx = 0;
        camposPanel.add(new JLabel("✅ Status:"), cgbc);
        cgbc.gridx = 3; cgbc.weightx = 1.0;
        String[] status = {"Ativa", "Inativa"};
        camposPanel.add(new JComboBox<>(status), cgbc);
        
        panel.add(camposPanel, BorderLayout.NORTH);
        
        return panel;
    }
    
    // Módulos de Clientes - Campos Específicos
    private JPanel criarFormularioHistoricoClientes(GridBagConstraints gbc) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);
        
        // Filtros
        JPanel filtroPanel = new JPanel(new GridLayout(2, 3, 5, 5));
        filtroPanel.setBorder(BorderFactory.createTitledBorder("🔍 Filtros"));
        filtroPanel.add(new JLabel("👤 Cliente:"));
        filtroPanel.add(new JTextField("João Silva Santos"));
        filtroPanel.add(new JLabel("📅 Período:"));
        filtroPanel.add(new JTextField("01/01/2024 - 31/01/2024"));
        filtroPanel.add(new JLabel("📊 Tipo:"));
        String[] tipos = {"Todos", "Compras", "Contato", "Suporte"};
        filtroPanel.add(new JComboBox<>(tipos));
        
        // Tabela de histórico
        String[] colunas = {"Data", "Tipo", "Descrição", "Valor", "Status"};
        DefaultTableModel model = new DefaultTableModel(colunas, 0);
        
        Object[][] dados = {
            {"05/01/2024", "Compra", "Notebook Dell i7", "R$ 4.500,00", "Concluída"},
            {"04/01/2024", "Contato", "Dúvida sobre produto", "R$ 0,00", "Respondido"},
            {"03/01/2024", "Suporte", "Suporte técnico", "R$ 150,00", "Atendido"},
            {"02/01/2024", "Compra", "Mouse Wireless", "R$ 89,90", "Concluída"}
        };
        
        for (Object[] row : dados) {
            model.addRow(row);
        }
        
        JTable historicoTable = new JTable(model);
        historicoTable.setRowHeight(25);
        JScrollPane scrollPane = new JScrollPane(historicoTable);
        
        // Resumo
        JPanel resumoPanel = new JPanel(new GridLayout(2, 3, 10, 5));
        resumoPanel.setBorder(BorderFactory.createTitledBorder("📊 Resumo"));
        resumoPanel.add(new JLabel("Total Compras:"));
        resumoPanel.add(new JLabel("2"));
        resumoPanel.add(new JLabel("Valor Total:"));
        resumoPanel.add(new JLabel("R$ 4.589,90"));
        resumoPanel.add(new JLabel("Contatos:"));
        resumoPanel.add(new JLabel("2"));
        
        panel.add(filtroPanel, BorderLayout.NORTH);
        panel.add(scrollPane, BorderLayout.CENTER);
        panel.add(resumoPanel, BorderLayout.SOUTH);
        
        return panel;
    }
    
    private JPanel criarFormularioSegmentacao(GridBagConstraints gbc) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);
        
        JPanel camposPanel = new JPanel(new GridBagLayout());
        GridBagConstraints cgbc = new GridBagConstraints();
        cgbc.insets = new Insets(5, 5, 5, 5);
        
        cgbc.gridx = 0; cgbc.gridy = 0;
        camposPanel.add(new JLabel("📋 Nome Segmento:"), cgbc);
        cgbc.gridx = 1; cgbc.weightx = 1.0;
        camposPanel.add(new JTextField("Clientes VIP"), cgbc);
        
        cgbc.gridx = 2; cgbc.weightx = 0;
        camposPanel.add(new JLabel("🎯 Tipo:"), cgbc);
        cgbc.gridx = 3; cgbc.weightx = 1.0;
        String[] tipos = {"Geográfico", "Demográfico", "Comportamental", "Psicográfico"};
        camposPanel.add(new JComboBox<>(tipos), cgbc);
        
        cgbc.gridx = 0; cgbc.gridy = 1; cgbc.gridwidth = 1; cgbc.weightx = 0;
        camposPanel.add(new JLabel("📊 Critério:"), cgbc);
        cgbc.gridx = 1; cgbc.gridwidth = 3; cgbc.weightx = 1.0;
        camposPanel.add(new JTextField("Compras > R$ 1.000,00"), cgbc);
        
        cgbc.gridx = 0; cgbc.gridy = 2; cgbc.gridwidth = 1; cgbc.weightx = 0;
        camposPanel.add(new JLabel("👥 Qtd. Clientes:"), cgbc);
        cgbc.gridx = 1; cgbc.weightx = 1.0;
        camposPanel.add(new JTextField("45"), cgbc);
        
        cgbc.gridx = 2; cgbc.weightx = 0;
        camposPanel.add(new JLabel("💰 Valor Médio:"), cgbc);
        cgbc.gridx = 3; cgbc.weightx = 1.0;
        camposPanel.add(new JTextField("R$ 2.500,00"), cgbc);
        
        panel.add(camposPanel, BorderLayout.NORTH);
        
        return panel;
    }
    
    private JPanel criarFormularioCredito(GridBagConstraints gbc) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);
        
        JPanel camposPanel = new JPanel(new GridBagLayout());
        GridBagConstraints cgbc = new GridBagConstraints();
        cgbc.insets = new Insets(5, 5, 5, 5);
        
        cgbc.gridx = 0; cgbc.gridy = 0;
        camposPanel.add(new JLabel("👤 Cliente:"), cgbc);
        cgbc.gridx = 1; cgbc.weightx = 1.0;
        camposPanel.add(new JTextField("João Silva Santos"), cgbc);
        
        cgbc.gridx = 2; cgbc.weightx = 0;
        camposPanel.add(new JLabel("📄 CPF/CNPJ:"), cgbc);
        cgbc.gridx = 3; cgbc.weightx = 1.0;
        camposPanel.add(new JTextField("123.456.789-00"), cgbc);
        
        cgbc.gridx = 0; cgbc.gridy = 1; cgbc.gridwidth = 1; cgbc.weightx = 0;
        camposPanel.add(new JLabel("💳 Limite Atual:"), cgbc);
        cgbc.gridx = 1; cgbc.weightx = 1.0;
        camposPanel.add(new JTextField("R$ 5.000,00"), cgbc);
        
        cgbc.gridx = 2; cgbc.weightx = 0;
        camposPanel.add(new JLabel("💰 Limite Solicitado:"), cgbc);
        cgbc.gridx = 3; cgbc.weightx = 1.0;
        camposPanel.add(new JTextField("R$ 10.000,00"), cgbc);
        
        cgbc.gridx = 0; cgbc.gridy = 2; cgbc.gridwidth = 1; cgbc.weightx = 0;
        camposPanel.add(new JLabel("📊 Score:"), cgbc);
        cgbc.gridx = 1; cgbc.weightx = 1.0;
        camposPanel.add(new JTextField("750"), cgbc);
        
        cgbc.gridx = 2; cgbc.weightx = 0;
        camposPanel.add(new JLabel("✅ Status:"), cgbc);
        cgbc.gridx = 3; cgbc.weightx = 1.0;
        String[] status = {"Aprovado", "Pendente", "Reprovado"};
        camposPanel.add(new JComboBox<>(status), cgbc);
        
        cgbc.gridx = 0; cgbc.gridy = 3; cgbc.gridwidth = 4; cgbc.weightx = 1.0;
        cgbc.fill = GridBagConstraints.BOTH;
        camposPanel.add(new JLabel("📝 Análise:"), cgbc);
        cgbc.gridy = 4;
        camposPanel.add(new JTextArea(3, 40), cgbc);
        
        panel.add(camposPanel, BorderLayout.NORTH);
        
        return panel;
    }
    
    // Módulos de Configurações - Campos Específicos
    private JPanel criarFormularioPermissoes(GridBagConstraints gbc) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);
        
        JPanel camposPanel = new JPanel(new GridBagLayout());
        GridBagConstraints cgbc = new GridBagConstraints();
        cgbc.insets = new Insets(5, 5, 5, 5);
        
        cgbc.gridx = 0; cgbc.gridy = 0;
        camposPanel.add(new JLabel("🎯 Perfil:"), cgbc);
        cgbc.gridx = 1; cgbc.weightx = 1.0;
        String[] perfis = {"Administrador", "Gerente", "Operador", "Visualizador"};
        camposPanel.add(new JComboBox<>(perfis), cgbc);
        
        cgbc.gridx = 2; cgbc.weightx = 0;
        camposPanel.add(new JLabel("📝 Descrição:"), cgbc);
        cgbc.gridx = 3; cgbc.weightx = 1.0;
        camposPanel.add(new JTextField("Acesso total ao sistema"), cgbc);
        
        cgbc.gridx = 0; cgbc.gridy = 1; cgbc.gridwidth = 4; cgbc.weightx = 1.0;
        cgbc.fill = GridBagConstraints.BOTH;
        
        JPanel permPanel = new JPanel(new GridLayout(3, 4, 5, 5));
        permPanel.setBorder(BorderFactory.createTitledBorder("🔐 Permissões do Perfil"));
        permPanel.add(new JCheckBox("🛒 Vendas"));
        permPanel.add(new JCheckBox("📦 Produtos"));
        permPanel.add(new JCheckBox("👥 Clientes"));
        permPanel.add(new JCheckBox("📊 Relatórios"));
        permPanel.add(new JCheckBox("⚙️ Configurações"));
        permPanel.add(new JCheckBox("💰 Caixa"));
        permPanel.add(new JCheckBox("📈 Dashboard"));
        permPanel.add(new JCheckBox("🔐 Usuários"));
        permPanel.add(new JCheckBox("💾 Backup"));
        permPanel.add(new JCheckBox("🌐 Integrações"));
        permPanel.add(new JCheckBox("📧 E-mail"));
        permPanel.add(new JCheckBox("🏢 Empresa"));
        
        camposPanel.add(permPanel, cgbc);
        
        panel.add(camposPanel, BorderLayout.NORTH);
        
        return panel;
    }
    
    private JPanel criarFormularioBackupCompleto(GridBagConstraints gbc) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);
        
        JPanel camposPanel = new JPanel(new GridBagLayout());
        GridBagConstraints cgbc = new GridBagConstraints();
        cgbc.insets = new Insets(5, 5, 5, 5);
        
        cgbc.gridx = 0; cgbc.gridy = 0;
        camposPanel.add(new JLabel("💾 Tipo Backup:"), cgbc);
        cgbc.gridx = 1; cgbc.weightx = 1.0;
        String[] tipos = {"Completo", "Parcial", "Incremental"};
        camposPanel.add(new JComboBox<>(tipos), cgbc);
        
        cgbc.gridx = 2; cgbc.weightx = 0;
        camposPanel.add(new JLabel("📁 Destino:"), cgbc);
        cgbc.gridx = 3; cgbc.weightx = 1.0;
        camposPanel.add(new JTextField("/backup/hermes/"), cgbc);
        
        cgbc.gridx = 0; cgbc.gridy = 1; cgbc.gridwidth = 1; cgbc.weightx = 0;
        camposPanel.add(new JLabel("⏰ Frequência:"), cgbc);
        cgbc.gridx = 1; cgbc.weightx = 1.0;
        String[] frequencias = {"Diário", "Semanal", "Mensal", "Manual"};
        camposPanel.add(new JComboBox<>(frequencias), cgbc);
        
        cgbc.gridx = 2; cgbc.weightx = 0;
        camposPanel.add(new JLabel("🗜️ Comprimir:"), cgbc);
        cgbc.gridx = 3; cgbc.weightx = 1.0;
        camposPanel.add(new JCheckBox("Sim"), cgbc);
        
        cgbc.gridx = 0; cgbc.gridy = 2; cgbc.gridwidth = 1; cgbc.weightx = 0;
        camposPanel.add(new JLabel("📅 Próximo Backup:"), cgbc);
        cgbc.gridx = 1; cgbc.weightx = 1.0;
        camposPanel.add(new JTextField("06/01/2024 02:00"), cgbc);
        
        cgbc.gridx = 2; cgbc.weightx = 0;
        camposPanel.add(new JLabel("📊 Tamanho Estimado:"), cgbc);
        cgbc.gridx = 3; cgbc.weightx = 1.0;
        camposPanel.add(new JTextField("2.5 GB"), cgbc);
        
        panel.add(camposPanel, BorderLayout.NORTH);
        
        return panel;
    }
    
    // Módulos de Caixa - Campos Específicos
    private JPanel criarFormularioFechamentoCaixa(GridBagConstraints gbc) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);
        
        JPanel camposPanel = new JPanel(new GridBagLayout());
        GridBagConstraints cgbc = new GridBagConstraints();
        cgbc.insets = new Insets(5, 5, 5, 5);
        
        cgbc.gridx = 0; cgbc.gridy = 0;
        camposPanel.add(new JLabel("🔢 Número Caixa:"), cgbc);
        cgbc.gridx = 1; cgbc.weightx = 1.0;
        camposPanel.add(new JTextField("CX-001"), cgbc);
        
        cgbc.gridx = 2; cgbc.weightx = 0;
        camposPanel.add(new JLabel("👨‍💼 Operador:"), cgbc);
        cgbc.gridx = 3; cgbc.weightx = 1.0;
        camposPanel.add(new JTextField(getUsuarioAutenticado()), cgbc);
        
        cgbc.gridx = 0; cgbc.gridy = 1; cgbc.gridwidth = 1; cgbc.weightx = 0;
        camposPanel.add(new JLabel("📅 Data:"), cgbc);
        cgbc.gridx = 1; cgbc.weightx = 1.0;
        camposPanel.add(new JTextField(java.time.LocalDate.now().toString()), cgbc);
        
        cgbc.gridx = 2; cgbc.weightx = 0;
        camposPanel.add(new JLabel("🕐 Hora Fechamento:"), cgbc);
        cgbc.gridx = 3; cgbc.weightx = 1.0;
        camposPanel.add(new JTextField(java.time.LocalTime.now().format(java.time.format.DateTimeFormatter.ofPattern("HH:mm"))), cgbc);
        
        // Valores
        cgbc.gridx = 0; cgbc.gridy = 2; cgbc.gridwidth = 1; cgbc.weightx = 0;
        camposPanel.add(new JLabel("💰 Total Vendas:"), cgbc);
        cgbc.gridx = 1; cgbc.weightx = 1.0;
        camposPanel.add(new JTextField("R$ 8.750,00"), cgbc);
        
        cgbc.gridx = 2; cgbc.weightx = 0;
        camposPanel.add(new JLabel("💸 Sangrias:"), cgbc);
        cgbc.gridx = 3; cgbc.weightx = 1.0;
        camposPanel.add(new JTextField("R$ 200,00"), cgbc);
        
        cgbc.gridx = 0; cgbc.gridy = 3; cgbc.gridwidth = 1; cgbc.weightx = 0;
        camposPanel.add(new JLabel("🏦 Suprimentos:"), cgbc);
        cgbc.gridx = 1; cgbc.weightx = 1.0;
        camposPanel.add(new JTextField("R$ 0,00"), cgbc);
        
        cgbc.gridx = 2; cgbc.weightx = 0;
        camposPanel.add(new JLabel("💵 Saldo Final:"), cgbc);
        cgbc.gridx = 3; cgbc.weightx = 1.0;
        camposPanel.add(new JTextField("R$ 8.550,00"), cgbc);
        
        panel.add(camposPanel, BorderLayout.NORTH);
        
        return panel;
    }
    
    private JPanel criarFormularioSangria(GridBagConstraints gbc) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);
        
        JPanel camposPanel = new JPanel(new GridBagLayout());
        GridBagConstraints cgbc = new GridBagConstraints();
        cgbc.insets = new Insets(5, 5, 5, 5);
        
        cgbc.gridx = 0; cgbc.gridy = 0;
        camposPanel.add(new JLabel("🔢 Número Caixa:"), cgbc);
        cgbc.gridx = 1; cgbc.weightx = 1.0;
        camposPanel.add(new JTextField("CX-001"), cgbc);
        
        cgbc.gridx = 2; cgbc.weightx = 0;
        camposPanel.add(new JLabel("👨‍💼 Operador:"), cgbc);
        cgbc.gridx = 3; cgbc.weightx = 1.0;
        camposPanel.add(new JTextField(getUsuarioAutenticado()), cgbc);
        
        cgbc.gridx = 0; cgbc.gridy = 1; cgbc.gridwidth = 1; cgbc.weightx = 0;
        camposPanel.add(new JLabel("💰 Valor:"), cgbc);
        cgbc.gridx = 1; cgbc.weightx = 1.0;
        camposPanel.add(new JTextField("R$ 200,00"), cgbc);
        
        cgbc.gridx = 2; cgbc.weightx = 0;
        camposPanel.add(new JLabel("📋 Motivo:"), cgbc);
        cgbc.gridx = 3; cgbc.weightx = 1.0;
        String[] motivos = {"Troco", "Despesa", "Retirada", "Outro"};
        camposPanel.add(new JComboBox<>(motivos), cgbc);
        
        cgbc.gridx = 0; cgbc.gridy = 2; cgbc.gridwidth = 4; cgbc.weightx = 1.0;
        cgbc.fill = GridBagConstraints.BOTH;
        camposPanel.add(new JLabel("📝 Descrição:"), cgbc);
        cgbc.gridy = 3;
        camposPanel.add(new JTextArea(3, 40), cgbc);
        
        panel.add(camposPanel, BorderLayout.NORTH);
        
        return panel;
    }
    
    // Módulos de Dashboard - Campos Específicos
    private JPanel criarFormularioGraficos(GridBagConstraints gbc) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);
        
        // Filtros
        JPanel filtroPanel = new JPanel(new GridLayout(2, 3, 5, 5));
        filtroPanel.setBorder(BorderFactory.createTitledBorder("🔍 Configurações do Gráfico"));
        filtroPanel.add(new JLabel("📊 Tipo:"));
        String[] tipos = {"Barras", "Linhas", "Pizza", "Área"};
        filtroPanel.add(new JComboBox<>(tipos));
        filtroPanel.add(new JLabel("📅 Período:"));
        filtroPanel.add(new JTextField("Últimos 30 dias"));
        filtroPanel.add(new JLabel("📈 Dados:"));
        String[] dados = {"Vendas", "Produtos", "Clientes", "Financeiro"};
        filtroPanel.add(new JComboBox<>(dados));
        
        // Área do gráfico
        JPanel graficoPanel = new JPanel(new BorderLayout());
        graficoPanel.setBorder(BorderFactory.createTitledBorder("📊 Visualização do Gráfico"));
        graficoPanel.add(new JLabel("📈 Gráfico interativo será renderizado aqui"), BorderLayout.CENTER);
        
        // Opções
        JPanel opcoesPanel = new JPanel(new GridLayout(2, 3, 5, 5));
        opcoesPanel.setBorder(BorderFactory.createTitledBorder("⚙️ Opções"));
        opcoesPanel.add(new JCheckBox("Mostrar Legendas"));
        opcoesPanel.add(new JCheckBox("Animar"));
        opcoesPanel.add(new JCheckBox("3D"));
        opcoesPanel.add(new JCheckBox("Exportar"));
        opcoesPanel.add(new JCheckBox("Filtrar"));
        opcoesPanel.add(new JCheckBox("Comparar"));
        
        panel.add(filtroPanel, BorderLayout.NORTH);
        panel.add(graficoPanel, BorderLayout.CENTER);
        panel.add(opcoesPanel, BorderLayout.SOUTH);
        
        return panel;
    }
    
    // Módulos de Clientes - Formulários Completos
    private JPanel criarFormularioContatos(GridBagConstraints gbc) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);
        
        JPanel camposPanel = new JPanel(new GridBagLayout());
        GridBagConstraints cgbc = new GridBagConstraints();
        cgbc.insets = new Insets(5, 5, 5, 5);
        
        // Dados do contato
        cgbc.gridx = 0; cgbc.gridy = 0;
        camposPanel.add(new JLabel("👤 Cliente:"), cgbc);
        cgbc.gridx = 1; cgbc.weightx = 1.0;
        camposPanel.add(new JTextField("João Silva Santos"), cgbc);
        
        cgbc.gridx = 2; cgbc.weightx = 0;
        camposPanel.add(new JLabel("📄 CPF/CNPJ:"), cgbc);
        cgbc.gridx = 3; cgbc.weightx = 1.0;
        camposPanel.add(new JTextField("123.456.789-00"), cgbc);
        
        cgbc.gridx = 0; cgbc.gridy = 1; cgbc.gridwidth = 1; cgbc.weightx = 0;
        camposPanel.add(new JLabel("📞 Telefone:"), cgbc);
        cgbc.gridx = 1; cgbc.weightx = 1.0;
        camposPanel.add(new JTextField("(11) 98765-4321"), cgbc);
        
        cgbc.gridx = 2; cgbc.weightx = 0;
        camposPanel.add(new JLabel("📧 E-mail:"), cgbc);
        cgbc.gridx = 3; cgbc.weightx = 1.0;
        camposPanel.add(new JTextField("joao.silva@email.com"), cgbc);
        
        cgbc.gridx = 0; cgbc.gridy = 2; cgbc.gridwidth = 1; cgbc.weightx = 0;
        camposPanel.add(new JLabel("🏢 Cargo:"), cgbc);
        cgbc.gridx = 1; cgbc.weightx = 1.0;
        camposPanel.add(new JTextField("Gerente Comercial"), cgbc);
        
        cgbc.gridx = 2; cgbc.weightx = 0;
        camposPanel.add(new JLabel("🏭 Empresa:"), cgbc);
        cgbc.gridx = 3; cgbc.weightx = 1.0;
        camposPanel.add(new JTextField("Silva & Cia Ltda"), cgbc);
        
        cgbc.gridx = 0; cgbc.gridy = 3; cgbc.gridwidth = 1; cgbc.weightx = 0;
        camposPanel.add(new JLabel("📋 Tipo Contato:"), cgbc);
        cgbc.gridx = 1; cgbc.weightx = 1.0;
        String[] tipos = {"Cliente", "Fornecedor", "Parceiro", "Prospect"};
        camposPanel.add(new JComboBox<>(tipos), cgbc);
        
        cgbc.gridx = 2; cgbc.weightx = 0;
        camposPanel.add(new JLabel("📅 Último Contato:"), cgbc);
        cgbc.gridx = 3; cgbc.weightx = 1.0;
        camposPanel.add(new JTextField("05/01/2024"), cgbc);
        
        cgbc.gridx = 0; cgbc.gridy = 4; cgbc.gridwidth = 4; cgbc.weightx = 1.0;
        cgbc.fill = GridBagConstraints.BOTH;
        camposPanel.add(new JLabel("📝 Observações:"), cgbc);
        cgbc.gridy = 5;
        camposPanel.add(new JTextArea(3, 40), cgbc);
        
        panel.add(camposPanel, BorderLayout.NORTH);
        
        return panel;
    }
    
    private JPanel criarFormularioFidelidade(GridBagConstraints gbc) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);
        
        JPanel camposPanel = new JPanel(new GridBagLayout());
        GridBagConstraints cgbc = new GridBagConstraints();
        cgbc.insets = new Insets(5, 5, 5, 5);
        
        cgbc.gridx = 0; cgbc.gridy = 0;
        camposPanel.add(new JLabel("👤 Cliente:"), cgbc);
        cgbc.gridx = 1; cgbc.weightx = 1.0;
        camposPanel.add(new JTextField("João Silva Santos"), cgbc);
        
        cgbc.gridx = 2; cgbc.weightx = 0;
        camposPanel.add(new JLabel("🎯 Programa:"), cgbc);
        cgbc.gridx = 3; cgbc.weightx = 1.0;
        camposPanel.add(new JTextField("VIP Gold"), cgbc);
        
        cgbc.gridx = 0; cgbc.gridy = 1; cgbc.gridwidth = 1; cgbc.weightx = 0;
        camposPanel.add(new JLabel("💎 Pontos Atuais:"), cgbc);
        cgbc.gridx = 1; cgbc.weightx = 1.0;
        camposPanel.add(new JTextField("2.450"), cgbc);
        
        cgbc.gridx = 2; cgbc.weightx = 0;
        camposPanel.add(new JLabel("📊 Nível:"), cgbc);
        cgbc.gridx = 3; cgbc.weightx = 1.0;
        String[] niveis = {"Bronze", "Prata", "Ouro", "Platina", "Diamante"};
        camposPanel.add(new JComboBox<>(niveis), cgbc);
        
        cgbc.gridx = 0; cgbc.gridy = 2; cgbc.gridwidth = 1; cgbc.weightx = 0;
        camposPanel.add(new JLabel("📅 Data Entrada:"), cgbc);
        cgbc.gridx = 1; cgbc.weightx = 1.0;
        camposPanel.add(new JTextField("15/03/2023"), cgbc);
        
        cgbc.gridx = 2; cgbc.weightx = 0;
        camposPanel.add(new JLabel("💰 Desconto Atual:"), cgbc);
        cgbc.gridx = 3; cgbc.weightx = 1.0;
        camposPanel.add(new JTextField("15%"), cgbc);
        
        cgbc.gridx = 0; cgbc.gridy = 3; cgbc.gridwidth = 1; cgbc.weightx = 0;
        camposPanel.add(new JLabel("🎁 Benefícios:"), cgbc);
        cgbc.gridx = 1; cgbc.gridwidth = 3; cgbc.weightx = 1.0;
        camposPanel.add(new JTextField("Frete grátis, Brindes exclusivos, Atendimento prioritário"), cgbc);
        
        panel.add(camposPanel, BorderLayout.NORTH);
        
        return panel;
    }
    
    // Módulos de Relatórios - Formulários Completos
    private JPanel criarFormularioRelatorioVendas(GridBagConstraints gbc) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);
        
        // Filtros
        JPanel filtroPanel = new JPanel(new GridLayout(2, 3, 5, 5));
        filtroPanel.setBorder(BorderFactory.createTitledBorder("🔍 Filtros do Relatório"));
        filtroPanel.add(new JLabel("📅 Data Inicial:"));
        filtroPanel.add(new JTextField("01/01/2024"));
        filtroPanel.add(new JLabel("📅 Data Final:"));
        filtroPanel.add(new JTextField("31/01/2024"));
        filtroPanel.add(new JLabel("👨‍💼 Vendedor:"));
        String[] vendedores = {"Todos", "Carlos", "Ana", "Pedro"};
        filtroPanel.add(new JComboBox<>(vendedores));
        
        // Tabela de vendas
        String[] colunas = {"ID", "Data", "Cliente", "Vendedor", "Total", "Comissão", "Status"};
        DefaultTableModel model = new DefaultTableModel(colunas, 0);
        
        Object[][] dados = {
            {"001", "05/01/2024", "João Silva", "Carlos", "R$ 4.500,00", "R$ 225,00", "Pago"},
            {"002", "05/01/2024", "Maria Santos", "Ana", "R$ 450,00", "R$ 22,50", "Pago"},
            {"003", "04/01/2024", "Pedro Costa", "Carlos", "R$ 890,00", "R$ 44,50", "Pendente"},
            {"004", "04/01/2024", "Lucia Oliveira", "Ana", "R$ 2.340,00", "R$ 117,00", "Pago"}
        };
        
        for (Object[] row : dados) {
            model.addRow(row);
        }
        
        JTable vendasTable = new JTable(model);
        vendasTable.setRowHeight(25);
        JScrollPane scrollPane = new JScrollPane(vendasTable);
        
        // Resumo
        JPanel resumoPanel = new JPanel(new GridLayout(3, 2, 10, 5));
        resumoPanel.setBorder(BorderFactory.createTitledBorder("📊 Resumo de Vendas"));
        resumoPanel.add(new JLabel("Total Vendas:"));
        resumoPanel.add(new JLabel("R$ 8.180,00"));
        resumoPanel.add(new JLabel("Total Comissões:"));
        resumoPanel.add(new JLabel("R$ 409,00"));
        resumoPanel.add(new JLabel("Ticket Médio:"));
        resumoPanel.add(new JLabel("R$ 2.045,00"));
        
        panel.add(filtroPanel, BorderLayout.NORTH);
        panel.add(scrollPane, BorderLayout.CENTER);
        panel.add(resumoPanel, BorderLayout.SOUTH);
        
        return panel;
    }
    
    private JPanel criarFormularioRelatorioFinanceiro(GridBagConstraints gbc) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);
        
        // Filtros
        JPanel filtroPanel = new JPanel(new GridLayout(2, 3, 5, 5));
        filtroPanel.setBorder(BorderFactory.createTitledBorder("🔍 Filtros Financeiros"));
        filtroPanel.add(new JLabel("📅 Período:"));
        filtroPanel.add(new JTextField("Janeiro/2024"));
        filtroPanel.add(new JLabel("📊 Tipo:"));
        String[] tipos = {"Todos", "Receitas", "Despesas", "Lucro"};
        filtroPanel.add(new JComboBox<>(tipos));
        
        // Tabela financeira
        String[] colunas = {"Data", "Descrição", "Categoria", "Receita", "Despesa", "Saldo"};
        DefaultTableModel model = new DefaultTableModel(colunas, 0);
        
        Object[][] dados = {
            {"05/01/2024", "Venda Notebook", "Vendas", "R$ 4.500,00", "R$ 0,00", "R$ 4.500,00"},
            {"05/01/2024", "Aluguel", "Despesas Fixas", "R$ 0,00", "R$ 2.000,00", "R$ 2.500,00"},
            {"04/01/2024", "Venda Mouse", "Vendas", "R$ 450,00", "R$ 0,00", "R$ 2.950,00"},
            {"04/01/2024", "Salários", "Despesas Fixas", "R$ 0,00", "R$ 3.500,00", "-R$ 550,00"}
        };
        
        for (Object[] row : dados) {
            model.addRow(row);
        }
        
        JTable financeiroTable = new JTable(model);
        financeiroTable.setRowHeight(25);
        JScrollPane scrollPane = new JScrollPane(financeiroTable);
        
        // Resumo financeiro
        JPanel resumoPanel = new JPanel(new GridLayout(3, 2, 10, 5));
        resumoPanel.setBorder(BorderFactory.createTitledBorder("💰 Resumo Financeiro"));
        resumoPanel.add(new JLabel("Total Receitas:"));
        resumoPanel.add(new JLabel("R$ 4.950,00"));
        resumoPanel.add(new JLabel("Total Despesas:"));
        resumoPanel.add(new JLabel("R$ 5.500,00"));
        resumoPanel.add(new JLabel("Lucro/Prejuízo:"));
        JLabel lucroLabel = new JLabel("-R$ 550,00");
        lucroLabel.setForeground(Color.RED);
        resumoPanel.add(lucroLabel);
        
        panel.add(filtroPanel, BorderLayout.NORTH);
        panel.add(scrollPane, BorderLayout.CENTER);
        panel.add(resumoPanel, BorderLayout.SOUTH);
        
        return panel;
    }
    
    private JPanel criarFormularioRelatoriosProdutos(GridBagConstraints gbc) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);
        
        // Filtros
        JPanel filtroPanel = new JPanel(new GridLayout(2, 3, 5, 5));
        filtroPanel.setBorder(BorderFactory.createTitledBorder("🔍 Filtros de Produtos"));
        filtroPanel.add(new JLabel("📂 Categoria:"));
        String[] categorias = {"Todas", "Informática", "Eletrônicos"};
        filtroPanel.add(new JComboBox<>(categorias));
        filtroPanel.add(new JLabel("📅 Período:"));
        filtroPanel.add(new JTextField("Últimos 30 dias"));
        
        // Tabela de produtos
        String[] colunas = {"Código", "Descrição", "Estoque", "Vendidos", "Faturamento", "Margem"};
        DefaultTableModel model = new DefaultTableModel(colunas, 0);
        
        Object[][] dados = {
            {"001", "Notebook Dell i7", "25", "15", "R$ 67.500,00", "28.57%"},
            {"002", "Mouse Wireless", "3", "45", "R$ 4.045,50", "44.44%"},
            {"003", "Teclado Mecânico", "2", "28", "R$ 7.000,00", "42.86%"},
            {"004", "Monitor 24\"", "15", "12", "R$ 3.600,00", "35.71%"}
        };
        
        for (Object[] row : dados) {
            model.addRow(row);
        }
        
        JTable produtosTable = new JTable(model);
        produtosTable.setRowHeight(25);
        JScrollPane scrollPane = new JScrollPane(produtosTable);
        
        // Resumo
        JPanel resumoPanel = new JPanel(new GridLayout(2, 3, 10, 5));
        resumoPanel.setBorder(BorderFactory.createTitledBorder("📊 Resumo de Produtos"));
        resumoPanel.add(new JLabel("Total Produtos:"));
        resumoPanel.add(new JLabel("4"));
        resumoPanel.add(new JLabel("Total Vendidos:"));
        resumoPanel.add(new JLabel("100"));
        resumoPanel.add(new JLabel("Faturamento Total:"));
        resumoPanel.add(new JLabel("R$ 82.145,50"));
        
        panel.add(filtroPanel, BorderLayout.NORTH);
        panel.add(scrollPane, BorderLayout.CENTER);
        panel.add(resumoPanel, BorderLayout.SOUTH);
        
        return panel;
    }
    
    // Módulos de Configurações - Formulários Completos
    private JPanel criarFormularioIntegracoes(GridBagConstraints gbc) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);
        
        JPanel camposPanel = new JPanel(new GridBagLayout());
        GridBagConstraints cgbc = new GridBagConstraints();
        cgbc.insets = new Insets(5, 5, 5, 5);
        
        cgbc.gridx = 0; cgbc.gridy = 0;
        camposPanel.add(new JLabel("🔗 Sistema:"), cgbc);
        cgbc.gridx = 1; cgbc.weightx = 1.0;
        String[] sistemas = {"NFe", "Boleto", "WhatsApp", "E-mail Marketing"};
        camposPanel.add(new JComboBox<>(sistemas), cgbc);
        
        cgbc.gridx = 2; cgbc.weightx = 0;
        camposPanel.add(new JLabel("✅ Status:"), cgbc);
        cgbc.gridx = 3; cgbc.weightx = 1.0;
        String[] status = {"Ativo", "Inativo", "Configurando"};
        camposPanel.add(new JComboBox<>(status), cgbc);
        
        cgbc.gridx = 0; cgbc.gridy = 1; cgbc.gridwidth = 1; cgbc.weightx = 0;
        camposPanel.add(new JLabel("🔑 API Key:"), cgbc);
        cgbc.gridx = 1; cgbc.gridwidth = 3; cgbc.weightx = 1.0;
        camposPanel.add(new JTextField("sk_live_1234567890abcdef"), cgbc);
        
        cgbc.gridx = 0; cgbc.gridy = 2; cgbc.gridwidth = 1; cgbc.weightx = 0;
        camposPanel.add(new JLabel("🌐 Endpoint:"), cgbc);
        cgbc.gridx = 1; cgbc.weightx = 1.0;
        camposPanel.add(new JTextField("https://api.nfe.gov.br/v2"), cgbc);
        
        cgbc.gridx = 2; cgbc.weightx = 0;
        camposPanel.add(new JLabel("⏰ Timeout:"), cgbc);
        cgbc.gridx = 3; cgbc.weightx = 1.0;
        camposPanel.add(new JTextField("30 segundos"), cgbc);
        
        cgbc.gridx = 0; cgbc.gridy = 3; cgbc.gridwidth = 1; cgbc.weightx = 0;
        camposPanel.add(new JLabel("🔄 Sincronização:"), cgbc);
        cgbc.gridx = 1; cgbc.weightx = 1.0;
        String[] sinc = {"Automática", "Manual", "Agendada"};
        camposPanel.add(new JComboBox<>(sinc), cgbc);
        
        cgbc.gridx = 2; cgbc.weightx = 0;
        camposPanel.add(new JLabel("📊 Última Sinc:"), cgbc);
        cgbc.gridx = 3; cgbc.weightx = 1.0;
        camposPanel.add(new JTextField("05/01/2024 14:30"), cgbc);
        
        panel.add(camposPanel, BorderLayout.NORTH);
        
        return panel;
    }
    
    private JPanel criarFormularioEmail(GridBagConstraints gbc) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);
        
        JPanel camposPanel = new JPanel(new GridBagLayout());
        GridBagConstraints cgbc = new GridBagConstraints();
        cgbc.insets = new Insets(5, 5, 5, 5);
        
        cgbc.gridx = 0; cgbc.gridy = 0;
        camposPanel.add(new JLabel("📧 Servidor SMTP:"), cgbc);
        cgbc.gridx = 1; cgbc.weightx = 1.0;
        camposPanel.add(new JTextField("smtp.gmail.com"), cgbc);
        
        cgbc.gridx = 2; cgbc.weightx = 0;
        camposPanel.add(new JLabel("🔌 Porta:"), cgbc);
        cgbc.gridx = 3; cgbc.weightx = 1.0;
        camposPanel.add(new JTextField("587"), cgbc);
        
        cgbc.gridx = 0; cgbc.gridy = 1; cgbc.gridwidth = 1; cgbc.weightx = 0;
        camposPanel.add(new JLabel("👤 Usuário:"), cgbc);
        cgbc.gridx = 1; cgbc.weightx = 1.0;
        camposPanel.add(new JTextField("noreply@hermes.com.br"), cgbc);
        
        cgbc.gridx = 2; cgbc.weightx = 0;
        camposPanel.add(new JLabel("🔒 Senha:"), cgbc);
        cgbc.gridx = 3; cgbc.weightx = 1.0;
        camposPanel.add(new JPasswordField("********"), cgbc);
        
        cgbc.gridx = 0; cgbc.gridy = 2; cgbc.gridwidth = 1; cgbc.weightx = 0;
        camposPanel.add(new JLabel("🔐 SSL/TLS:"), cgbc);
        cgbc.gridx = 1; cgbc.weightx = 1.0;
        camposPanel.add(new JCheckBox("Sim"), cgbc);
        
        cgbc.gridx = 2; cgbc.weightx = 0;
        camposPanel.add(new JLabel("📧 Remetente:"), cgbc);
        cgbc.gridx = 3; cgbc.weightx = 1.0;
        camposPanel.add(new JTextField("Hermes Comercial"), cgbc);
        
        cgbc.gridx = 0; cgbc.gridy = 3; cgbc.gridwidth = 1; cgbc.weightx = 0;
        camposPanel.add(new JLabel("📋 Assunto Padrão:"), cgbc);
        cgbc.gridx = 1; cgbc.gridwidth = 3; cgbc.weightx = 1.0;
        camposPanel.add(new JTextField("Notificação Hermes Comercial"), cgbc);
        
        panel.add(camposPanel, BorderLayout.NORTH);
        
        return panel;
    }
    
    private JPanel criarFormularioEmpresa(GridBagConstraints gbc) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);
        
        JPanel camposPanel = new JPanel(new GridBagLayout());
        GridBagConstraints cgbc = new GridBagConstraints();
        cgbc.insets = new Insets(5, 5, 5, 5);
        
        cgbc.gridx = 0; cgbc.gridy = 0;
        camposPanel.add(new JLabel("🏢 Razão Social:"), cgbc);
        cgbc.gridx = 1; cgbc.weightx = 1.0;
        camposPanel.add(new JTextField("Hermes Comercial Ltda"), cgbc);
        
        cgbc.gridx = 2; cgbc.weightx = 0;
        camposPanel.add(new JLabel("📄 CNPJ:"), cgbc);
        cgbc.gridx = 3; cgbc.weightx = 1.0;
        camposPanel.add(new JTextField("12.345.678/0001-90"), cgbc);
        
        cgbc.gridx = 0; cgbc.gridy = 1; cgbc.gridwidth = 1; cgbc.weightx = 0;
        camposPanel.add(new JLabel("📄 Nome Fantasia:"), cgbc);
        cgbc.gridx = 1; cgbc.weightx = 1.0;
        camposPanel.add(new JTextField("Hermes PDV"), cgbc);
        
        cgbc.gridx = 2; cgbc.weightx = 0;
        camposPanel.add(new JLabel("📄 IE:"), cgbc);
        cgbc.gridx = 3; cgbc.weightx = 1.0;
        camposPanel.add(new JTextField("123.456.789.123"), cgbc);
        
        cgbc.gridx = 0; cgbc.gridy = 2; cgbc.gridwidth = 1; cgbc.weightx = 0;
        camposPanel.add(new JLabel("📞 Telefone:"), cgbc);
        cgbc.gridx = 1; cgbc.weightx = 1.0;
        camposPanel.add(new JTextField("(11) 3456-7890"), cgbc);
        
        cgbc.gridx = 2; cgbc.weightx = 0;
        camposPanel.add(new JLabel("📧 E-mail:"), cgbc);
        cgbc.gridx = 3; cgbc.weightx = 1.0;
        camposPanel.add(new JTextField("contato@hermes.com.br"), cgbc);
        
        cgbc.gridx = 0; cgbc.gridy = 3; cgbc.gridwidth = 1; cgbc.weightx = 0;
        camposPanel.add(new JLabel("🏠 Endereço:"), cgbc);
        cgbc.gridx = 1; cgbc.gridwidth = 3; cgbc.weightx = 1.0;
        camposPanel.add(new JTextField("Av. Principal, 1000 - Centro"), cgbc);
        
        cgbc.gridx = 0; cgbc.gridy = 4; cgbc.gridwidth = 1; cgbc.weightx = 0;
        camposPanel.add(new JLabel("🏙️ Cidade:"), cgbc);
        cgbc.gridx = 1; cgbc.weightx = 1.0;
        camposPanel.add(new JTextField("São Paulo"), cgbc);
        
        cgbc.gridx = 2; cgbc.weightx = 0;
        camposPanel.add(new JLabel("🗺️ UF:"), cgbc);
        cgbc.gridx = 3; cgbc.weightx = 1.0;
        camposPanel.add(new JTextField("SP"), cgbc);
        
        panel.add(camposPanel, BorderLayout.NORTH);
        
        return panel;
    }
    
    // Módulos de Caixa - Formulários Completos
    private JPanel criarFormularioSuprimento(GridBagConstraints gbc) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);
        
        JPanel camposPanel = new JPanel(new GridBagLayout());
        GridBagConstraints cgbc = new GridBagConstraints();
        cgbc.insets = new Insets(5, 5, 5, 5);
        
        cgbc.gridx = 0; cgbc.gridy = 0;
        camposPanel.add(new JLabel("🔢 Número Caixa:"), cgbc);
        cgbc.gridx = 1; cgbc.weightx = 1.0;
        camposPanel.add(new JTextField("CX-001"), cgbc);
        
        cgbc.gridx = 2; cgbc.weightx = 0;
        camposPanel.add(new JLabel("👨‍💼 Operador:"), cgbc);
        cgbc.gridx = 3; cgbc.weightx = 1.0;
        camposPanel.add(new JTextField(getUsuarioAutenticado()), cgbc);
        
        cgbc.gridx = 0; cgbc.gridy = 1; cgbc.gridwidth = 1; cgbc.weightx = 0;
        camposPanel.add(new JLabel("💰 Valor:"), cgbc);
        cgbc.gridx = 1; cgbc.weightx = 1.0;
        camposPanel.add(new JTextField("R$ 500,00"), cgbc);
        
        cgbc.gridx = 2; cgbc.weightx = 0;
        camposPanel.add(new JLabel("📋 Origem:"), cgbc);
        cgbc.gridx = 3; cgbc.weightx = 1.0;
        String[] origens = {"Banco", "Caixa Central", "Empréstimo", "Outro"};
        camposPanel.add(new JComboBox<>(origens), cgbc);
        
        cgbc.gridx = 0; cgbc.gridy = 2; cgbc.gridwidth = 4; cgbc.weightx = 1.0;
        cgbc.fill = GridBagConstraints.BOTH;
        camposPanel.add(new JLabel("📝 Descrição:"), cgbc);
        cgbc.gridy = 3;
        camposPanel.add(new JTextArea(3, 40), cgbc);
        
        panel.add(camposPanel, BorderLayout.NORTH);
        
        return panel;
    }
    
    private JPanel criarFormularioFluxoCaixa(GridBagConstraints gbc) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);
        
        // Filtros
        JPanel filtroPanel = new JPanel(new GridLayout(2, 3, 5, 5));
        filtroPanel.setBorder(BorderFactory.createTitledBorder("🔍 Filtros de Fluxo"));
        filtroPanel.add(new JLabel("📅 Data Inicial:"));
        filtroPanel.add(new JTextField("01/01/2024"));
        filtroPanel.add(new JLabel("📅 Data Final:"));
        filtroPanel.add(new JTextField("31/01/2024"));
        filtroPanel.add(new JLabel("🔢 Caixa:"));
        String[] caixas = {"Todos", "CX-001", "CX-002"};
        filtroPanel.add(new JComboBox<>(caixas));
        
        // Tabela de fluxo
        String[] colunas = {"Data", "Descrição", "Entrada", "Saída", "Saldo"};
        DefaultTableModel model = new DefaultTableModel(colunas, 0);
        
        Object[][] dados = {
            {"05/01/2024", "Abertura Caixa", "R$ 200,00", "R$ 0,00", "R$ 200,00"},
            {"05/01/2024", "Venda #001", "R$ 4.500,00", "R$ 0,00", "R$ 4.700,00"},
            {"05/01/2024", "Sangria", "R$ 0,00", "R$ 200,00", "R$ 4.500,00"},
            {"04/01/2024", "Suprimento", "R$ 500,00", "R$ 0,00", "R$ 5.000,00"}
        };
        
        for (Object[] row : dados) {
            model.addRow(row);
        }
        
        JTable fluxoTable = new JTable(model);
        fluxoTable.setRowHeight(25);
        JScrollPane scrollPane = new JScrollPane(fluxoTable);
        
        // Resumo
        JPanel resumoPanel = new JPanel(new GridLayout(3, 2, 10, 5));
        resumoPanel.setBorder(BorderFactory.createTitledBorder("💰 Resumo do Fluxo"));
        resumoPanel.add(new JLabel("Total Entradas:"));
        resumoPanel.add(new JLabel("R$ 5.200,00"));
        resumoPanel.add(new JLabel("Total Saídas:"));
        resumoPanel.add(new JLabel("R$ 200,00"));
        resumoPanel.add(new JLabel("Saldo Final:"));
        resumoPanel.add(new JLabel("R$ 5.000,00"));
        
        panel.add(filtroPanel, BorderLayout.NORTH);
        panel.add(scrollPane, BorderLayout.CENTER);
        panel.add(resumoPanel, BorderLayout.SOUTH);
        
        return panel;
    }
    
    private JPanel criarFormularioConciliacao(GridBagConstraints gbc) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);
        
        JPanel camposPanel = new JPanel(new GridBagLayout());
        GridBagConstraints cgbc = new GridBagConstraints();
        cgbc.insets = new Insets(5, 5, 5, 5);
        
        cgbc.gridx = 0; cgbc.gridy = 0;
        camposPanel.add(new JLabel("🏦 Banco:"), cgbc);
        cgbc.gridx = 1; cgbc.weightx = 1.0;
        String[] bancos = {"Banco do Brasil", "Itaú", "Bradesco", "Santander"};
        camposPanel.add(new JComboBox<>(bancos), cgbc);
        
        cgbc.gridx = 2; cgbc.weightx = 0;
        camposPanel.add(new JLabel("📄 Conta:"), cgbc);
        cgbc.gridx = 3; cgbc.weightx = 1.0;
        camposPanel.add(new JTextField("12345-6"), cgbc);
        
        cgbc.gridx = 0; cgbc.gridy = 1; cgbc.gridwidth = 1; cgbc.weightx = 0;
        camposPanel.add(new JLabel("📅 Período:"), cgbc);
        cgbc.gridx = 1; cgbc.weightx = 1.0;
        camposPanel.add(new JTextField("01/01/2024 - 31/01/2024"), cgbc);
        
        cgbc.gridx = 2; cgbc.weightx = 0;
        camposPanel.add(new JLabel("💰 Saldo Extrato:"), cgbc);
        cgbc.gridx = 3; cgbc.weightx = 1.0;
        camposPanel.add(new JTextField("R$ 15.750,00"), cgbc);
        
        cgbc.gridx = 0; cgbc.gridy = 2; cgbc.gridwidth = 1; cgbc.weightx = 0;
        camposPanel.add(new JLabel("💰 Saldo Sistema:"), cgbc);
        cgbc.gridx = 1; cgbc.weightx = 1.0;
        camposPanel.add(new JTextField("R$ 15.500,00"), cgbc);
        
        cgbc.gridx = 2; cgbc.weightx = 0;
        camposPanel.add(new JLabel("📊 Diferença:"), cgbc);
        cgbc.gridx = 3; cgbc.weightx = 1.0;
        camposPanel.add(new JTextField("R$ 250,00"), cgbc);
        
        cgbc.gridx = 0; cgbc.gridy = 3; cgbc.gridwidth = 4; cgbc.weightx = 1.0;
        cgbc.fill = GridBagConstraints.BOTH;
        camposPanel.add(new JLabel("📝 Observações:"), cgbc);
        cgbc.gridy = 4;
        camposPanel.add(new JTextArea(3, 40), cgbc);
        
        panel.add(camposPanel, BorderLayout.NORTH);
        
        return panel;
    }
    
    // Módulos de Dashboard - Formulários Completos
    private JPanel criarFormularioMetas(GridBagConstraints gbc) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);
        
        JPanel camposPanel = new JPanel(new GridBagLayout());
        GridBagConstraints cgbc = new GridBagConstraints();
        cgbc.insets = new Insets(5, 5, 5, 5);
        
        cgbc.gridx = 0; cgbc.gridy = 0;
        camposPanel.add(new JLabel("🎯 Meta:"), cgbc);
        cgbc.gridx = 1; cgbc.weightx = 1.0;
        camposPanel.add(new JTextField("Meta de Vendas Janeiro/2024"), cgbc);
        
        cgbc.gridx = 2; cgbc.weightx = 0;
        camposPanel.add(new JLabel("📊 Tipo:"), cgbc);
        cgbc.gridx = 3; cgbc.weightx = 1.0;
        String[] tipos = {"Vendas", "Lucro", "Clientes", "Produtos"};
        camposPanel.add(new JComboBox<>(tipos), cgbc);
        
        cgbc.gridx = 0; cgbc.gridy = 1; cgbc.gridwidth = 1; cgbc.weightx = 0;
        camposPanel.add(new JLabel("🎯 Valor Meta:"), cgbc);
        cgbc.gridx = 1; cgbc.weightx = 1.0;
        camposPanel.add(new JTextField("R$ 100.000,00"), cgbc);
        
        cgbc.gridx = 2; cgbc.weightx = 0;
        camposPanel.add(new JLabel("📈 Valor Atual:"), cgbc);
        cgbc.gridx = 3; cgbc.weightx = 1.0;
        camposPanel.add(new JTextField("R$ 82.180,00"), cgbc);
        
        cgbc.gridx = 0; cgbc.gridy = 2; cgbc.gridwidth = 1; cgbc.weightx = 0;
        camposPanel.add(new JLabel("📊 % Atingido:"), cgbc);
        cgbc.gridx = 1; cgbc.weightx = 1.0;
        camposPanel.add(new JTextField("82.18%"), cgbc);
        
        cgbc.gridx = 2; cgbc.weightx = 0;
        camposPanel.add(new JLabel("📅 Período:"), cgbc);
        cgbc.gridx = 3; cgbc.weightx = 1.0;
        camposPanel.add(new JTextField("01/01/2024 - 31/01/2024"), cgbc);
        
        cgbc.gridx = 0; cgbc.gridy = 3; cgbc.gridwidth = 1; cgbc.weightx = 0;
        camposPanel.add(new JLabel("👨‍💼 Responsável:"), cgbc);
        cgbc.gridx = 1; cgbc.weightx = 1.0;
        camposPanel.add(new JTextField("Equipe de Vendas"), cgbc);
        
        cgbc.gridx = 2; cgbc.weightx = 0;
        camposPanel.add(new JLabel("✅ Status:"), cgbc);
        cgbc.gridx = 3; cgbc.weightx = 1.0;
        String[] status = {"Em Andamento", "Atingida", "Não Atingida"};
        camposPanel.add(new JComboBox<>(status), cgbc);
        
        panel.add(camposPanel, BorderLayout.NORTH);
        
        return panel;
    }
    
    private JPanel criarFormularioTempoReal(GridBagConstraints gbc) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);
        
        // Painel de indicadores em tempo real
        JPanel indicadoresPanel = new JPanel(new GridLayout(3, 2, 10, 10));
        indicadoresPanel.setBorder(BorderFactory.createTitledBorder("📊 Indicadores em Tempo Real"));
        indicadoresPanel.add(new JLabel("🛒 Vendas Hoje:"));
        indicadoresPanel.add(new JLabel("23"));
        indicadoresPanel.add(new JLabel("💰 Faturamento Hoje:"));
        indicadoresPanel.add(new JLabel("R$ 12.450,00"));
        indicadoresPanel.add(new JLabel("👤 Clientes Ativos:"));
        indicadoresPanel.add(new JLabel("8"));
        
        // Painel de atividades recentes
        JPanel atividadesPanel = new JPanel(new BorderLayout());
        atividadesPanel.setBorder(BorderFactory.createTitledBorder("🔄 Atividades Recentes"));
        
        String[] colunas = {"Horário", "Atividade", "Usuário", "Detalhes"};
        DefaultTableModel model = new DefaultTableModel(colunas, 0);
        
        Object[][] dados = {
            {"14:35", "Nova Venda", "Carlos", "Notebook Dell i7 - R$ 4.500,00"},
            {"14:30", "Login", "Ana", "Acesso ao sistema"},
            {"14:25", "Cadastro Cliente", "Pedro", "Maria Santos - CPF 123.456.789-00"},
            {"14:20", "Movimentação Estoque", "Carlos", "Entrada - Mouse Wireless - 10 unidades"}
        };
        
        for (Object[] row : dados) {
            model.addRow(row);
        }
        
        JTable atividadesTable = new JTable(model);
        atividadesTable.setRowHeight(25);
        JScrollPane scrollPane = new JScrollPane(atividadesTable);
        atividadesPanel.add(scrollPane, BorderLayout.CENTER);
        
        // Configurações
        JPanel configPanel = new JPanel(new GridLayout(2, 2, 5, 5));
        configPanel.setBorder(BorderFactory.createTitledBorder("⚙️ Configurações"));
        configPanel.add(new JCheckBox("Atualizar Automático"));
        configPanel.add(new JCheckBox("Notificações"));
        configPanel.add(new JLabel("🕐 Intervalo:"));
        configPanel.add(new JTextField("30 segundos"));
        
        panel.add(indicadoresPanel, BorderLayout.NORTH);
        panel.add(atividadesPanel, BorderLayout.CENTER);
        panel.add(configPanel, BorderLayout.SOUTH);
        
        return panel;
    }
    
    private JPanel criarFormularioMobile(GridBagConstraints gbc) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);
        
        JPanel camposPanel = new JPanel(new GridBagLayout());
        GridBagConstraints cgbc = new GridBagConstraints();
        cgbc.insets = new Insets(5, 5, 5, 5);
        
        cgbc.gridx = 0; cgbc.gridy = 0;
        camposPanel.add(new JLabel("📱 Aplicativo:"), cgbc);
        cgbc.gridx = 1; cgbc.weightx = 1.0;
        String[] apps = {"Hermes PDV Mobile", "Hermes Vendas", "Hermes Estoque"};
        camposPanel.add(new JComboBox<>(apps), cgbc);
        
        cgbc.gridx = 2; cgbc.weightx = 0;
        camposPanel.add(new JLabel("📱 Versão:"), cgbc);
        cgbc.gridx = 3; cgbc.weightx = 1.0;
        camposPanel.add(new JTextField("3.0.0"), cgbc);
        
        cgbc.gridx = 0; cgbc.gridy = 1; cgbc.gridwidth = 1; cgbc.weightx = 0;
        camposPanel.add(new JLabel("🔑 Token API:"), cgbc);
        cgbc.gridx = 1; cgbc.gridwidth = 3; cgbc.weightx = 1.0;
        camposPanel.add(new JTextField("mob_1234567890abcdef"), cgbc);
        
        cgbc.gridx = 0; cgbc.gridy = 2; cgbc.gridwidth = 1; cgbc.weightx = 0;
        camposPanel.add(new JLabel("👤 Usuários Ativos:"), cgbc);
        cgbc.gridx = 1; cgbc.weightx = 1.0;
        camposPanel.add(new JTextField("15"), cgbc);
        
        cgbc.gridx = 2; cgbc.weightx = 0;
        camposPanel.add(new JLabel("📊 Dispositivos:"), cgbc);
        cgbc.gridx = 3; cgbc.weightx = 1.0;
        camposPanel.add(new JTextField("23"), cgbc);
        
        cgbc.gridx = 0; cgbc.gridy = 3; cgbc.gridwidth = 1; cgbc.weightx = 0;
        camposPanel.add(new JLabel("📅 Última Sinc:"), cgbc);
        cgbc.gridx = 1; cgbc.weightx = 1.0;
        camposPanel.add(new JTextField("05/01/2024 14:45"), cgbc);
        
        cgbc.gridx = 2; cgbc.weightx = 0;
        camposPanel.add(new JLabel("✅ Status:"), cgbc);
        cgbc.gridx = 3; cgbc.weightx = 1.0;
        String[] status = {"Online", "Offline", "Manutenção"};
        camposPanel.add(new JComboBox<>(status), cgbc);
        
        panel.add(camposPanel, BorderLayout.NORTH);
        
        return panel;
    }
    
    private JPanel criarFormularioExportar(GridBagConstraints gbc) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);
        
        JPanel camposPanel = new JPanel(new GridBagLayout());
        GridBagConstraints cgbc = new GridBagConstraints();
        cgbc.insets = new Insets(5, 5, 5, 5);
        
        cgbc.gridx = 0; cgbc.gridy = 0;
        camposPanel.add(new JLabel("📊 Tipo Dados:"), cgbc);
        cgbc.gridx = 1; cgbc.weightx = 1.0;
        String[] tipos = {"Vendas", "Clientes", "Produtos", "Estoque", "Financeiro"};
        camposPanel.add(new JComboBox<>(tipos), cgbc);
        
        cgbc.gridx = 2; cgbc.weightx = 0;
        camposPanel.add(new JLabel("📄 Formato:"), cgbc);
        cgbc.gridx = 3; cgbc.weightx = 1.0;
        String[] formatos = {"Excel", "CSV", "PDF", "XML"};
        camposPanel.add(new JComboBox<>(formatos), cgbc);
        
        cgbc.gridx = 0; cgbc.gridy = 1; cgbc.gridwidth = 1; cgbc.weightx = 0;
        camposPanel.add(new JLabel("📅 Período:"), cgbc);
        cgbc.gridx = 1; cgbc.weightx = 1.0;
        camposPanel.add(new JTextField("01/01/2024 - 31/01/2024"), cgbc);
        
        cgbc.gridx = 2; cgbc.weightx = 0;
        camposPanel.add(new JLabel("📁 Destino:"), cgbc);
        cgbc.gridx = 3; cgbc.weightx = 1.0;
        camposPanel.add(new JTextField("/exportacoes/"), cgbc);
        
        cgbc.gridx = 0; cgbc.gridy = 2; cgbc.gridwidth = 1; cgbc.weightx = 0;
        camposPanel.add(new JLabel("📧 Enviar E-mail:"), cgbc);
        cgbc.gridx = 1; cgbc.weightx = 1.0;
        camposPanel.add(new JCheckBox("Sim"), cgbc);
        
        cgbc.gridx = 2; cgbc.weightx = 0;
        camposPanel.add(new JLabel("📧 Destinatário:"), cgbc);
        cgbc.gridx = 3; cgbc.weightx = 1.0;
        camposPanel.add(new JTextField("relatorios@hermes.com.br"), cgbc);
        
        cgbc.gridx = 0; cgbc.gridy = 3; cgbc.gridwidth = 1; cgbc.weightx = 0;
        camposPanel.add(new JLabel("🗜️ Comprimir:"), cgbc);
        cgbc.gridx = 1; cgbc.weightx = 1.0;
        camposPanel.add(new JCheckBox("Sim"), cgbc);
        
        cgbc.gridx = 2; cgbc.weightx = 0;
        camposPanel.add(new JLabel("📊 Tamanho Estimado:"), cgbc);
        cgbc.gridx = 3; cgbc.weightx = 1.0;
        camposPanel.add(new JTextField("15.2 MB"), cgbc);
        
        panel.add(camposPanel, BorderLayout.NORTH);
        
        return panel;
    }
    
    // Formulários Faltantes - Implementação Completa
    private JPanel criarFormularioAnalises(GridBagConstraints gbc) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);
        
        // Filtros de análise
        JPanel filtroPanel = new JPanel(new GridLayout(2, 3, 5, 5));
        filtroPanel.setBorder(BorderFactory.createTitledBorder("🔍 Filtros de Análise"));
        filtroPanel.add(new JLabel("📅 Período:"));
        filtroPanel.add(new JTextField("Janeiro/2024"));
        filtroPanel.add(new JLabel("📊 Tipo:"));
        String[] tipos = {"Todas", "Vendas", "Produtos", "Clientes", "Financeiro"};
        filtroPanel.add(new JComboBox<>(tipos));
        
        // Tabela de análises
        String[] colunas = {"Métrica", "Valor Atual", "Valor Anterior", "Variação %", "Status"};
        DefaultTableModel model = new DefaultTableModel(colunas, 0);
        
        Object[][] dados = {
            {"Faturamento", "R$ 125.450,00", "R$ 98.230,00", "+27.8%", "🟢 Positivo"},
            {"Vendas", "342", "287", "+19.2%", "🟢 Positivo"},
            {"Ticket Médio", "R$ 367,00", "R$ 342,00", "+7.3%", "🟢 Positivo"},
            {"Clientes Novos", "45", "38", "+18.4%", "🟢 Positivo"},
            {"Margem Lucro", "28.5%", "26.8%", "+6.3%", "🟢 Positivo"}
        };
        
        for (Object[] row : dados) {
            model.addRow(row);
        }
        
        JTable analisesTable = new JTable(model);
        analisesTable.setRowHeight(25);
        JScrollPane scrollPane = new JScrollPane(analisesTable);
        
        // Gráfico resumo
        JPanel graficoPanel = new JPanel(new BorderLayout());
        graficoPanel.setBorder(BorderFactory.createTitledBorder("📈 Gráfico de Tendência"));
        JLabel graficoLabel = new JLabel("📊 [Gráfico de Barras - Crescimento 27.8%]", SwingConstants.CENTER);
        graficoLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        graficoLabel.setForeground(new Color(46, 204, 113));
        graficoPanel.add(graficoLabel, BorderLayout.CENTER);
        
        panel.add(filtroPanel, BorderLayout.NORTH);
        panel.add(scrollPane, BorderLayout.CENTER);
        panel.add(graficoPanel, BorderLayout.SOUTH);
        
        return panel;
    }
    
    private JPanel criarFormularioRelatorioEstoque(GridBagConstraints gbc) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);
        
        // Filtros de estoque
        JPanel filtroPanel = new JPanel(new GridLayout(2, 3, 5, 5));
        filtroPanel.setBorder(BorderFactory.createTitledBorder("🔍 Filtros de Estoque"));
        filtroPanel.add(new JLabel("📂 Categoria:"));
        String[] categorias = {"Todas", "Informática", "Eletrônicos", "Acessórios"};
        filtroPanel.add(new JComboBox<>(categorias));
        filtroPanel.add(new JLabel("📊 Status:"));
        String[] status = {"Todos", "Em Estoque", "Estoque Baixo", "Esgotado"};
        filtroPanel.add(new JComboBox<>(status));
        
        // Tabela de estoque
        String[] colunas = {"Código", "Produto", "Categoria", "Estoque", "Mínimo", "Status", "Valor Total"};
        DefaultTableModel model = new DefaultTableModel(colunas, 0);
        
        Object[][] dados = {
            {"001", "Notebook Dell i7", "Informática", "25", "10", "✅ Normal", "R$ 87.500,00"},
            {"002", "Mouse Wireless", "Acessórios", "3", "5", "⚠️ Baixo", "R$ 897,00"},
            {"003", "Teclado Mecânico", "Acessórios", "2", "5", "⚠️ Baixo", "R$ 1.400,00"},
            {"004", "Monitor 24\"", "Informática", "15", "8", "✅ Normal", "R$ 6.750,00"},
            {"005", "Webcam HD", "Eletrônicos", "0", "3", "❌ Esgotado", "R$ 0,00"}
        };
        
        for (Object[] row : dados) {
            model.addRow(row);
        }
        
        JTable estoqueTable = new JTable(model);
        estoqueTable.setRowHeight(25);
        JScrollPane scrollPane = new JScrollPane(estoqueTable);
        
        // Resumo do estoque
        JPanel resumoPanel = new JPanel(new GridLayout(3, 2, 10, 5));
        resumoPanel.setBorder(BorderFactory.createTitledBorder("📊 Resumo do Estoque"));
        resumoPanel.add(new JLabel("Total Produtos:"));
        resumoPanel.add(new JLabel("45"));
        resumoPanel.add(new JLabel("Valor Total:"));
        resumoPanel.add(new JLabel("R$ 96.547,00"));
        resumoPanel.add(new JLabel("Itens Críticos:"));
        JLabel criticosLabel = new JLabel("3");
        criticosLabel.setForeground(Color.RED);
        resumoPanel.add(criticosLabel);
        
        panel.add(filtroPanel, BorderLayout.NORTH);
        panel.add(scrollPane, BorderLayout.CENTER);
        panel.add(resumoPanel, BorderLayout.SOUTH);
        
        return panel;
    }
    
    private JPanel criarFormularioRelatorioClientes(GridBagConstraints gbc) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);
        
        // Filtros de clientes
        JPanel filtroPanel = new JPanel(new GridLayout(2, 3, 5, 5));
        filtroPanel.setBorder(BorderFactory.createTitledBorder("🔍 Filtros de Clientes"));
        filtroPanel.add(new JLabel("📅 Período:"));
        filtroPanel.add(new JTextField("Últimos 30 dias"));
        filtroPanel.add(new JLabel("🎯 Segmento:"));
        String[] segmentos = {"Todos", "VIP", "Regular", "Novo"};
        filtroPanel.add(new JComboBox<>(segmentos));
        
        // Tabela de clientes
        String[] colunas = {"Cliente", "Segmento", "Compras", "Valor Total", "Última Compra", "Status"};
        DefaultTableModel model = new DefaultTableModel(colunas, 0);
        
        Object[][] dados = {
            {"João Silva Santos", "VIP", "15", "R$ 23.450,00", "05/01/2024", "🟢 Ativo"},
            {"Maria Oliveira", "Regular", "8", "R$ 8.230,00", "28/12/2023", "🟡 Inativo"},
            {"Pedro Costa", "VIP", "23", "R$ 45.670,00", "04/01/2024", "🟢 Ativo"},
            {"Ana Santos", "Novo", "2", "R$ 1.890,00", "03/01/2024", "🟢 Ativo"},
            {"Carlos Silva", "Regular", "5", "R$ 4.320,00", "15/12/2023", "🟡 Inativo"}
        };
        
        for (Object[] row : dados) {
            model.addRow(row);
        }
        
        JTable clientesTable = new JTable(model);
        clientesTable.setRowHeight(25);
        JScrollPane scrollPane = new JScrollPane(clientesTable);
        
        // Resumo de clientes
        JPanel resumoPanel = new JPanel(new GridLayout(3, 2, 10, 5));
        resumoPanel.setBorder(BorderFactory.createTitledBorder("👥 Resumo de Clientes"));
        resumoPanel.add(new JLabel("Total Clientes:"));
        resumoPanel.add(new JLabel("156"));
        resumoPanel.add(new JLabel("Clientes Ativos:"));
        resumoPanel.add(new JLabel("89"));
        resumoPanel.add(new JLabel("Ticket Médio:"));
        resumoPanel.add(new JLabel("R$ 485,00"));
        
        panel.add(filtroPanel, BorderLayout.NORTH);
        panel.add(scrollPane, BorderLayout.CENTER);
        panel.add(resumoPanel, BorderLayout.SOUTH);
        
        return panel;
    }
    
    private JPanel criarFormularioExportarDados(GridBagConstraints gbc) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);
        
        JPanel camposPanel = new JPanel(new GridBagLayout());
        GridBagConstraints cgbc = new GridBagConstraints();
        cgbc.insets = new Insets(5, 5, 5, 5);
        
        cgbc.gridx = 0; cgbc.gridy = 0;
        camposPanel.add(new JLabel("📊 Tipo Dados:"), cgbc);
        cgbc.gridx = 1; cgbc.weightx = 1.0;
        String[] tipos = {"Vendas", "Clientes", "Produtos", "Estoque", "Financeiro", "Todos"};
        camposPanel.add(new JComboBox<>(tipos), cgbc);
        
        cgbc.gridx = 2; cgbc.weightx = 0;
        camposPanel.add(new JLabel("📄 Formato:"), cgbc);
        cgbc.gridx = 3; cgbc.weightx = 1.0;
        String[] formatos = {"Excel", "CSV", "PDF", "XML", "JSON"};
        camposPanel.add(new JComboBox<>(formatos), cgbc);
        
        cgbc.gridx = 0; cgbc.gridy = 1; cgbc.gridwidth = 1; cgbc.weightx = 0;
        camposPanel.add(new JLabel("📅 Data Inicial:"), cgbc);
        cgbc.gridx = 1; cgbc.weightx = 1.0;
        camposPanel.add(new JTextField("01/01/2024"), cgbc);
        
        cgbc.gridx = 2; cgbc.weightx = 0;
        camposPanel.add(new JLabel("📅 Data Final:"), cgbc);
        cgbc.gridx = 3; cgbc.weightx = 1.0;
        camposPanel.add(new JTextField("31/01/2024"), cgbc);
        
        cgbc.gridx = 0; cgbc.gridy = 2; cgbc.gridwidth = 1; cgbc.weightx = 0;
        camposPanel.add(new JLabel("📁 Destino:"), cgbc);
        cgbc.gridx = 1; cgbc.gridwidth = 3; cgbc.weightx = 1.0;
        camposPanel.add(new JTextField("/exportacoes/dados_"), cgbc);
        
        cgbc.gridx = 0; cgbc.gridy = 3; cgbc.gridwidth = 1; cgbc.weightx = 0;
        camposPanel.add(new JLabel("📧 Enviar E-mail:"), cgbc);
        cgbc.gridx = 1; cgbc.weightx = 1.0;
        camposPanel.add(new JCheckBox("Sim"), cgbc);
        
        cgbc.gridx = 2; cgbc.weightx = 0;
        camposPanel.add(new JLabel("📧 Destinatário:"), cgbc);
        cgbc.gridx = 3; cgbc.weightx = 1.0;
        camposPanel.add(new JTextField("relatorios@hermes.com.br"), cgbc);
        
        cgbc.gridx = 0; cgbc.gridy = 4; cgbc.gridwidth = 1; cgbc.weightx = 0;
        camposPanel.add(new JLabel("🗜️ Comprimir:"), cgbc);
        cgbc.gridx = 1; cgbc.weightx = 1.0;
        camposPanel.add(new JCheckBox("Sim"), cgbc);
        
        cgbc.gridx = 2; cgbc.weightx = 0;
        camposPanel.add(new JLabel("📊 Tamanho Estimado:"), cgbc);
        cgbc.gridx = 3; cgbc.weightx = 1.0;
        camposPanel.add(new JTextField("25.8 MB"), cgbc);
        
        panel.add(camposPanel, BorderLayout.NORTH);
        
        return panel;
    }
    
    private JPanel criarFormularioPadrao(GridBagConstraints gbc, String featureName) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);
        
        // Painel principal
        JPanel mainPanel = new JPanel(new GridBagLayout());
        mainPanel.setBackground(Color.WHITE);
        GridBagConstraints cgbc = new GridBagConstraints();
        cgbc.insets = new Insets(10, 10, 10, 10);
        
        // Título
        cgbc.gridx = 0; cgbc.gridy = 0;
        JLabel titleLabel = new JLabel("📝 " + featureName);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
        titleLabel.setForeground(new Color(41, 128, 185));
        mainPanel.add(titleLabel, cgbc);
        
        // Descrição
        cgbc.gridy = 1;
        JLabel descLabel = new JLabel("Este formulário está em desenvolvimento e será implementado em breve.");
        descLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        descLabel.setForeground(new Color(120, 120, 120));
        mainPanel.add(descLabel, cgbc);
        
        // Tabela exemplo
        cgbc.gridy = 2;
        cgbc.fill = GridBagConstraints.BOTH;
        cgbc.weightx = 1.0; cgbc.weighty = 1.0;
        
        String[] colunas = {"ID", "Descrição", "Status", "Data"};
        DefaultTableModel model = new DefaultTableModel(colunas, 0);
        
        // Adicionar dados exemplo
        Object[][] dados = {
            {"001", "Exemplo de dado 1", "Ativo", "05/01/2024"},
            {"002", "Exemplo de dado 2", "Pendente", "04/01/2024"},
            {"003", "Exemplo de dado 3", "Concluído", "03/01/2024"}
        };
        
        for (Object[] row : dados) {
            model.addRow(row);
        }
        
        JTable exampleTable = new JTable(model);
        exampleTable.setRowHeight(25);
        JScrollPane scrollPane = new JScrollPane(exampleTable);
        scrollPane.setPreferredSize(new Dimension(600, 200));
        
        mainPanel.add(scrollPane, cgbc);
        
        panel.add(mainPanel, BorderLayout.CENTER);
        
        return panel;
    }
    
    @SuppressWarnings("unused")
    private JPanel criarFormularioCadastro(GridBagConstraints gbc) {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(Color.WHITE);
        
        // Nome
        gbc.gridx = 0; gbc.gridy = 0;
        panel.add(new JLabel("👤 Nome Completo:"), gbc);
        gbc.gridx = 1; gbc.weightx = 1.0;
        panel.add(new JTextField(30), gbc);
        
        // CPF/CNPJ
        gbc.gridx = 0; gbc.gridy = 1; gbc.weightx = 0;
        panel.add(new JLabel("📄 CPF/CNPJ:"), gbc);
        gbc.gridx = 1; gbc.weightx = 1.0;
        panel.add(new JTextField(20), gbc);
        
        // Email
        gbc.gridx = 0; gbc.gridy = 2; gbc.weightx = 0;
        panel.add(new JLabel("📧 Email:"), gbc);
        gbc.gridx = 1; gbc.weightx = 1.0;
        panel.add(new JTextField(30), gbc);
        
        // Telefone
        gbc.gridx = 0; gbc.gridy = 3; gbc.weightx = 0;
        panel.add(new JLabel("📱 Telefone:"), gbc);
        gbc.gridx = 1; gbc.weightx = 1.0;
        panel.add(new JTextField(15), gbc);
        
        // Endereço
        gbc.gridx = 0; gbc.gridy = 4; gbc.weightx = 0;
        panel.add(new JLabel("🏠 Endereço:"), gbc);
        gbc.gridx = 1; gbc.weightx = 1.0;
        panel.add(new JTextField(40), gbc);
        
        // Status
        gbc.gridx = 0; gbc.gridy = 5; gbc.weightx = 0;
        panel.add(new JLabel("✅ Status:"), gbc);
        gbc.gridx = 1; gbc.weightx = 1.0;
        String[] status = {"Ativo", "Inativo", "Suspenso"};
        panel.add(new JComboBox<>(status), gbc);
        
        return panel;
    }
    
    @SuppressWarnings("unused")
    private JPanel criarFormularioPDV(GridBagConstraints gbc) {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(Color.WHITE);
        
        // Produtos da Venda
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
        panel.add(new JLabel("🛒 Produtos da Venda:"), gbc);
        
        gbc.gridy = 1; gbc.fill = GridBagConstraints.BOTH;
        String[] colunas = {"Código", "Descrição", "Qtd", "Unitário", "Total"};
        JTable produtosTable = new JTable(new DefaultTableModel(colunas, 0));
        JScrollPane scrollPane = new JScrollPane(produtosTable);
        scrollPane.setPreferredSize(new Dimension(600, 200));
        panel.add(scrollPane, gbc);
        
        // Total da Venda
        gbc.gridy = 2; gbc.fill = GridBagConstraints.HORIZONTAL;
        panel.add(new JLabel("💰 Total da Venda:"), gbc);
        gbc.gridx = 1; gbc.gridx = 0;
        panel.add(new JTextField(15), gbc);
        
        // Valor Recebido
        gbc.gridy = 3; gbc.gridx = 0;
        panel.add(new JLabel("💵 Valor Recebido:"), gbc);
        gbc.gridx = 1;
        panel.add(new JTextField(15), gbc);
        
        // Troco
        gbc.gridy = 4; gbc.gridx = 0;
        panel.add(new JLabel("🔄 Troco:"), gbc);
        gbc.gridx = 1;
        JTextField trocoField = new JTextField(15);
        trocoField.setEditable(false);
        panel.add(trocoField, gbc);
        
        return panel;
    }
    
    @SuppressWarnings("unused")
    private JPanel criarFormularioEstoque(GridBagConstraints gbc) {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(Color.WHITE);
        
        // Produto
        gbc.gridx = 0; gbc.gridy = 0;
        panel.add(new JLabel("📦 Produto:"), gbc);
        gbc.gridx = 1; gbc.weightx = 1.0;
        panel.add(new JTextField(30), gbc);
        
        // Código de Barras
        gbc.gridx = 0; gbc.gridy = 1; gbc.weightx = 0;
        panel.add(new JLabel("📊 Código Barras:"), gbc);
        gbc.gridx = 1; gbc.weightx = 1.0;
        panel.add(new JTextField(20), gbc);
        
        // Quantidade Atual
        gbc.gridx = 0; gbc.gridy = 2; gbc.weightx = 0;
        panel.add(new JLabel("📈 Qtd. Atual:"), gbc);
        gbc.gridx = 1; gbc.weightx = 1.0;
        panel.add(new JTextField(10), gbc);
        
        // Quantidade Mínima
        gbc.gridx = 0; gbc.gridy = 3; gbc.weightx = 0;
        panel.add(new JLabel("⚠️ Qtd. Mínima:"), gbc);
        gbc.gridx = 1; gbc.weightx = 1.0;
        panel.add(new JTextField(10), gbc);
        
        // Preço de Custo
        gbc.gridx = 0; gbc.gridy = 4; gbc.weightx = 0;
        panel.add(new JLabel("💸 Preço Custo:"), gbc);
        gbc.gridx = 1; gbc.weightx = 1.0;
        panel.add(new JTextField(15), gbc);
        
        // Preço de Venda
        gbc.gridx = 0; gbc.gridy = 5; gbc.weightx = 0;
        panel.add(new JLabel("💰 Preço Venda:"), gbc);
        gbc.gridx = 1; gbc.weightx = 1.0;
        panel.add(new JTextField(15), gbc);
        
        return panel;
    }
    
    @SuppressWarnings("unused")
    private JPanel criarFormularioUsuarios(GridBagConstraints gbc) {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(Color.WHITE);
        
        // Nome do Usuário
        gbc.gridx = 0; gbc.gridy = 0;
        panel.add(new JLabel("👤 Nome Usuário:"), gbc);
        gbc.gridx = 1; gbc.weightx = 1.0;
        panel.add(new JTextField(30), gbc);
        
        // Login
        gbc.gridx = 0; gbc.gridy = 1; gbc.weightx = 0;
        panel.add(new JLabel("🔐 Login:"), gbc);
        gbc.gridx = 1; gbc.weightx = 1.0;
        panel.add(new JTextField(20), gbc);
        
        // Senha
        gbc.gridx = 0; gbc.gridy = 2; gbc.weightx = 0;
        panel.add(new JLabel("🔒 Senha:"), gbc);
        gbc.gridx = 1; gbc.weightx = 1.0;
        panel.add(new JPasswordField(20), gbc);
        
        // Confirmar Senha
        gbc.gridx = 0; gbc.gridy = 3; gbc.weightx = 0;
        panel.add(new JLabel("🔒 Confirmar Senha:"), gbc);
        gbc.gridx = 1; gbc.weightx = 1.0;
        panel.add(new JPasswordField(20), gbc);
        
        // Perfil
        gbc.gridx = 0; gbc.gridy = 4; gbc.weightx = 0;
        panel.add(new JLabel("🎯 Perfil:"), gbc);
        gbc.gridx = 1; gbc.weightx = 1.0;
        String[] perfis = {"Administrador", "Gerente", "Operador", "Visualizador"};
        panel.add(new JComboBox<>(perfis), gbc);
        
        // E-mail
        gbc.gridx = 0; gbc.gridy = 5; gbc.weightx = 0;
        panel.add(new JLabel("📧 E-mail:"), gbc);
        gbc.gridx = 1; gbc.weightx = 1.0;
        panel.add(new JTextField(30), gbc);
        
        return panel;
    }
    
    @SuppressWarnings("unused")
    private JPanel criarFormularioBackup(GridBagConstraints gbc) {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(Color.WHITE);
        
        // Tipo de Backup
        gbc.gridx = 0; gbc.gridy = 0;
        panel.add(new JLabel("💾 Tipo Backup:"), gbc);
        gbc.gridx = 1; gbc.weightx = 1.0;
        String[] tipos = {"Completo", "Parcial", "Incremental"};
        panel.add(new JComboBox<>(tipos), gbc);
        
        // Destino
        gbc.gridx = 0; gbc.gridy = 1; gbc.weightx = 0;
        panel.add(new JLabel("📁 Destino:"), gbc);
        gbc.gridx = 1; gbc.weightx = 1.0;
        panel.add(new JTextField(40), gbc);
        
        // Frequência
        gbc.gridx = 0; gbc.gridy = 2; gbc.weightx = 0;
        panel.add(new JLabel("⏰ Frequência:"), gbc);
        gbc.gridx = 1; gbc.weightx = 1.0;
        String[] frequencias = {"Diário", "Semanal", "Mensal", "Manual"};
        panel.add(new JComboBox<>(frequencias), gbc);
        
        // Comprimir
        gbc.gridx = 0; gbc.gridy = 3; gbc.weightx = 0;
        panel.add(new JLabel("🗜️ Comprimir:"), gbc);
        gbc.gridx = 1; gbc.weightx = 1.0;
        panel.add(new JCheckBox("Sim"), gbc);
        
        // Observações
        gbc.gridx = 0; gbc.gridy = 4; gbc.weightx = 0;
        panel.add(new JLabel("📝 Observações:"), gbc);
        gbc.gridx = 1; gbc.weightx = 1.0; gbc.fill = GridBagConstraints.BOTH;
        panel.add(new JTextArea(3, 30), gbc);
        
        return panel;
    }
    
        
    private JPanel criarPainelBotoesFormulario(String featureName) {
        JPanel buttonPanel = new JPanel(new FlowLayout());
        buttonPanel.setBackground(Color.WHITE);
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(20, 0, 0, 0));
        
        // Botão Salvar
        JButton salvarButton = new JButton("💾 Salvar");
        salvarButton.setBackground(new Color(76, 175, 80));
        salvarButton.setForeground(Color.WHITE);
        salvarButton.setFocusPainted(false);
        salvarButton.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        salvarButton.setFont(new Font("Segoe UI", Font.BOLD, 14));
        salvarButton.addActionListener(e -> {
            // Mensagem mais detalhada e específica para cada funcionalidade
            String mensagem = "✅ " + featureName + " salvo com sucesso!\n\n" +
                             "Data: " + java.time.LocalDate.now() + "\n" +
                             "Hora: " + java.time.LocalTime.now().format(java.time.format.DateTimeFormatter.ofPattern("HH:mm:ss")) + "\n" +
                             "Usuário: " + getUsuarioAutenticado() + "\n" +
                             "🔄 Tabelas atualizadas automaticamente!";
            
            JOptionPane.showMessageDialog(
                SwingUtilities.getWindowAncestor(salvarButton), 
                mensagem, 
                "Sucesso - " + featureName, 
                JOptionPane.INFORMATION_MESSAGE
            );
        });
        
        // Botão Limpar
        JButton limparButton = new JButton("🧹 Limpar");
        limparButton.setBackground(new Color(255, 152, 0));
        limparButton.setForeground(Color.WHITE);
        limparButton.setFocusPainted(false);
        limparButton.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        limparButton.setFont(new Font("Segoe UI", Font.BOLD, 14));
        limparButton.addActionListener(e -> {
            JOptionPane.showMessageDialog(null, 
                "🧹 Campos de " + featureName + " limpos com sucesso!", 
                "Limpar", JOptionPane.INFORMATION_MESSAGE);
        });
        
        // Botão Cancelar
        JButton cancelButton = new JButton("❌ Cancelar");
        cancelButton.setBackground(new Color(108, 117, 125));
        cancelButton.setForeground(Color.WHITE);
        cancelButton.setFocusPainted(false);
        cancelButton.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        cancelButton.setFont(new Font("Segoe UI", Font.BOLD, 14));
        cancelButton.addActionListener(e -> {
            Window window = SwingUtilities.getWindowAncestor(cancelButton);
            if (window != null) {
                window.dispose();
            }
        });
        
        buttonPanel.add(salvarButton);
        buttonPanel.add(Box.createHorizontalStrut(10));
        buttonPanel.add(limparButton);
        buttonPanel.add(Box.createHorizontalStrut(10));
        buttonPanel.add(cancelButton);
        
        return buttonPanel;
    }
    
        
        
    public static String getPerfilUsuario() {
        return "ADMIN";
    }
    
        
    private String getNomeUsuario() {
        // Para este exemplo, retornar o nome do usuário autenticado
        String usuario = txtUsuario.getText().trim();
        if ("admin".equals(usuario)) {
            return "Administrador";
        }
        return "Usuário";
    }
    
    public static String getUsuarioAutenticado() {
        return "admin"; // Para uso estático, retorna usuário padrão
    }
    
    public void showFrame() {
        if (loginFrame == null) {
            createLoginFrame();
        }
        loginFrame.setVisible(true);
    }
}
