package com.br.hermescomercial.pdv.controller;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import com.br.hermescomercial.util.DatabaseConfig;
import com.br.hermescomercial.ui.layout.LayoutPadrao;

/**
 * Controller de Login do Sistema PDV
 * Tela de autenticação de usuários
 * Versão 2.0 - Interface Swing Moderna
 */
public class PDVLoginSwingController {
    
    private JFrame loginFrame;
    private JPanel mainPanel;
    private JTextField txtUsuario;
    private JPasswordField txtSenha;
    private JButton btnLogin;
    private JButton btnSair;
    private JLabel lblStatus;
    private JLabel lblTitulo;
    private JLabel lblUsuario;
    private JLabel lblSenha;
    private JLabel lblIcon;
    
    // Usuário autenticado
    private static String usuarioAutenticado;
    private static String perfilUsuario;
    
    // Variáveis para arrastar janela
    private int mousePressedX;
    private int mousePressedY;
    
    public PDVLoginSwingController() {
        initializeUI();
    }
    
    private void initializeUI() {
        // Frame principal
        loginFrame = new JFrame("Hermes Comercial PDV - Login");
        loginFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        loginFrame.setSize(550, 700);
        loginFrame.setLocationRelativeTo(null);
        loginFrame.setResizable(false);
        loginFrame.setUndecorated(true); // Remove borda padrão
        
        // Painel principal com gradiente moderno
        mainPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                // Gradiente de fundo mais suave
                GradientPaint gradient = new GradientPaint(0, 0, new Color(41, 128, 185), 
                                                         0, getHeight(), new Color(52, 73, 94));
                g2d.setPaint(gradient);
                g2d.fillRect(0, 0, getWidth(), getHeight());
                
                // Adicionar círculo decorativo
                g2d.setColor(new Color(255, 255, 255, 20));
                g2d.fillOval(getWidth() - 150, 50, 200, 200);
                g2d.setColor(new Color(255, 255, 255, 10));
                g2d.fillOval(getWidth() - 100, 100, 150, 150);
            }
        };
        mainPanel.setLayout(new BorderLayout());
        mainPanel.setBorder(BorderFactory.createEmptyBorder(70, 70, 70, 70));
        
        createLoginPanel();
        createBottomPanel();
        
        loginFrame.add(mainPanel);
        
        // Adicionar efeito de sombra
        loginFrame.getRootPane().setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(0, 0, 0, 50), 1),
            BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));
    }
    
    private void createLoginPanel() {
        JPanel loginPanel = new JPanel(new GridBagLayout());
        loginPanel.setOpaque(false);
        GridBagConstraints gbc = new GridBagConstraints();
        
        // Card container para o formulário
        JPanel cardPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                // Fundo elegante com gradiente sutil
                g2d.setColor(new Color(248, 249, 250));
                g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 25, 25);
                
                // Borda elegante
                g2d.setColor(new Color(206, 212, 218));
                g2d.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 25, 25);
            }
        };
        cardPanel.setOpaque(false);
        cardPanel.setBorder(BorderFactory.createEmptyBorder(40, 60, 40, 60));
        cardPanel.setLayout(new GridBagLayout());
        GridBagConstraints cardGbc = new GridBagConstraints();
        
        // Logo da empresa
        try {
            ImageIcon logoIcon = new ImageIcon(getClass().getResource("/img/logo.png"));
            Image scaledImage = logoIcon.getImage().getScaledInstance(100, 100, Image.SCALE_SMOOTH);
            lblIcon = new JLabel(new ImageIcon(scaledImage), SwingConstants.CENTER);
        } catch (Exception e) {
            // Fallback para emoji se não encontrar o logo
            lblIcon = new JLabel("🏪", SwingConstants.CENTER);
            lblIcon.setFont(new Font("Segoe UI", Font.PLAIN, 56));
            lblIcon.setForeground(new Color(41, 128, 185));
        }
        cardGbc.gridx = 0;
        cardGbc.gridy = 0;
        cardGbc.insets = new Insets(0, 0, 25, 0);
        cardPanel.add(lblIcon, cardGbc);
        
        // Título elegante
        lblTitulo = new JLabel("HERMES COMERCIAL", SwingConstants.CENTER);
        lblTitulo.setFont(LayoutPadrao.FONTE_TITULO);
        lblTitulo.setForeground(LayoutPadrao.COR_PRIMARIA);
        lblTitulo.setHorizontalAlignment(SwingConstants.CENTER);
        
        lblUsuario = new JLabel("👤 Usuário");
        lblUsuario.setFont(LayoutPadrao.FONTE_ROTULO);
        lblUsuario.setForeground(LayoutPadrao.COR_TEXTO);
        
        lblSenha = new JLabel("🔒 Senha");
        lblSenha.setFont(LayoutPadrao.FONTE_ROTULO);
        lblSenha.setForeground(LayoutPadrao.COR_TEXTO);
        
        JLabel lblSubtitulo = new JLabel("Sistema de Gestão Comercial", SwingConstants.CENTER);
        lblSubtitulo.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        lblSubtitulo.setForeground(new Color(127, 140, 141)); // Cinza suave
        lblSubtitulo.setOpaque(true);
        cardGbc.gridy = 2;
        cardGbc.insets = new Insets(0, 0, 35, 0);
        cardPanel.add(lblSubtitulo, cardGbc);
        
        // Campo Usuário
        JPanel usuarioPanel = new JPanel(new BorderLayout());
        usuarioPanel.setOpaque(false);
        
        usuarioPanel.add(lblUsuario, BorderLayout.NORTH);
        
        txtUsuario = LayoutPadrao.criarCampoTexto(20);
        usuarioPanel.add(txtUsuario, BorderLayout.CENTER);
        
        cardGbc.gridy = 3;
        cardGbc.insets = new Insets(0, 0, 15, 0);
        cardPanel.add(usuarioPanel, cardGbc);
        
        // Campo Senha
        JPanel senhaPanel = new JPanel(new BorderLayout());
        senhaPanel.setOpaque(false);
        
        senhaPanel.add(lblSenha, BorderLayout.NORTH);
        
        txtSenha = LayoutPadrao.criarCampoSenha(20);
        senhaPanel.add(txtSenha, BorderLayout.CENTER);
        
        cardGbc.gridy = 4;
        cardGbc.insets = new Insets(0, 0, 15, 0);
        cardPanel.add(senhaPanel, cardGbc);
        
        // Botões
        JPanel buttonPanel = new JPanel(new GridLayout(1, 2, 30, 0));
        buttonPanel.setOpaque(false);
        
        btnLogin = LayoutPadrao.criarBotaoSucesso("🔐 Entrar");
        btnSair = LayoutPadrao.criarBotaoPerigo("❌ Sair");
        
        buttonPanel.add(btnLogin);
        buttonPanel.add(btnSair);
        
        cardGbc.gridy = 5;
        cardGbc.insets = new Insets(0, 0, 25, 0);
        cardPanel.add(buttonPanel, cardGbc);
        
        // Status
        lblStatus = LayoutPadrao.criarRotuloTexto("Digite suas credenciais para acessar o sistema");
        lblStatus.setHorizontalAlignment(SwingConstants.CENTER);
        lblStatus.setOpaque(true);
        lblStatus.setBackground(LayoutPadrao.COR_FUNDO_ESCURO);
        lblStatus.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));
        cardGbc.gridy = 6;
        cardGbc.insets = new Insets(15, 0, 0, 0);
        cardPanel.add(lblStatus, cardGbc);
        
        // Adicionar card ao painel principal
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.fill = GridBagConstraints.BOTH;
        loginPanel.add(cardPanel, gbc);
        
        mainPanel.add(loginPanel, BorderLayout.CENTER);
        
        // Eventos
        setupEvents();
    }
    
    private void createBottomPanel() {
        JPanel bottomPanel = new JPanel(new BorderLayout());
        bottomPanel.setOpaque(false);
        bottomPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        // Botão de fechar (X)
        JButton btnClose = new JButton("✕");
        btnClose.setFont(new Font("Segoe UI", Font.BOLD, 16));
        btnClose.setForeground(new Color(255, 255, 255, 150));
        btnClose.setBackground(new Color(255, 255, 255, 0));
        btnClose.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        btnClose.setFocusPainted(false);
        btnClose.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnClose.setOpaque(false);
        btnClose.addActionListener(e -> System.exit(0));
        
        // Informações
        JLabel lblInfo = new JLabel("© 2026 Hermes Comercial PDV v2.0", SwingConstants.CENTER);
        lblInfo.setFont(new Font("Segoe UI", Font.PLAIN, 10));
        lblInfo.setForeground(new Color(255, 255, 255, 120));
        
        // Versão
        JLabel lblVersion = new JLabel("Build 2026.04.26", SwingConstants.CENTER);
        lblVersion.setFont(new Font("Segoe UI", Font.PLAIN, 9));
        lblVersion.setForeground(new Color(255, 255, 255, 80));
        
        JPanel infoPanel = new JPanel(new GridLayout(2, 1, 0, 2));
        infoPanel.setOpaque(false);
        infoPanel.add(lblInfo);
        infoPanel.add(lblVersion);
        
        bottomPanel.add(btnClose, BorderLayout.EAST);
        bottomPanel.add(infoPanel, BorderLayout.CENTER);
        
        mainPanel.add(bottomPanel, BorderLayout.SOUTH);
    }
    
    private void setupEvents() {
        // Botão Login
        btnLogin.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                realizarLogin();
            }
        });
        
        // Botão Sair
        btnSair.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });
        
        // Enter nos campos - SEM VALIDAÇÃO MÍNIMA
        txtUsuario.addKeyListener(new KeyListener() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    txtSenha.requestFocus();
                }
            }
            
            @Override
            public void keyReleased(KeyEvent e) {}
            
            @Override
            public void keyTyped(KeyEvent e) {
                // Permitir digitação livre - sem validação mínima
            }
        });
        
        txtSenha.addKeyListener(new KeyListener() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    realizarLogin();
                }
            }
            
            @Override
            public void keyReleased(KeyEvent e) {}
            
            @Override
            public void keyTyped(KeyEvent e) {}
        });
        
        // Arrastar janela (sem borda)
        mainPanel.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                mousePressedX = evt.getX();
                mousePressedY = evt.getY();
            }
        });
        
        mainPanel.addMouseMotionListener(new java.awt.event.MouseMotionAdapter() {
            public void mouseDragged(java.awt.event.MouseEvent evt) {
                loginFrame.setLocation(
                    loginFrame.getX() + evt.getX() - mousePressedX,
                    loginFrame.getY() + evt.getY() - mousePressedY
                );
            }
        });
        
        // Foco inicial
        txtUsuario.requestFocus();
    }
    
    private void realizarLogin() {
        String usuario = txtUsuario.getText().trim();
        String senha = new String(txtSenha.getPassword());
        
        if (usuario.isEmpty() || senha.isEmpty()) {
            lblStatus.setText("⚠️ Preencha usuário e senha!");
            lblStatus.setForeground(LayoutPadrao.COR_ALERTA);
            return;
        }
        
        if (usuario.length() > 20) {
            lblStatus.setText("⚠️ Usuário deve ter no máximo 20 caracteres!");
            lblStatus.setForeground(LayoutPadrao.COR_ALERTA);
            txtUsuario.requestFocus();
            return;
        }
        
        lblStatus.setText("🔄 Autenticando...");
        lblStatus.setForeground(LayoutPadrao.COR_SUCESSO);
        
        // Validar no banco de dados
        if (autenticarUsuario(usuario, senha)) {
            lblStatus.setText("✅ Login realizado com sucesso!");
            lblStatus.setForeground(LayoutPadrao.COR_SUCESSO);
            
            // Abrir sistema principal
            SwingUtilities.invokeLater(() -> {
                loginFrame.dispose();
                abrirSistemaPrincipal();
            });
        } else {
            lblStatus.setText("❌ Usuário ou senha inválidos!");
            lblStatus.setForeground(LayoutPadrao.COR_PERIGO);
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
            pstmt.setString(2, senha); // Em produção, usar hash
            
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    usuarioAutenticado = rs.getString("nome");
                    perfilUsuario = rs.getString("perfil");
                    System.out.println("=== USUÁRIO AUTENTICADO ===");
                    System.out.println("Nome: " + usuarioAutenticado);
                    System.out.println("Login: " + usuario);
                    System.out.println("Perfil: " + perfilUsuario);
                    return true;
                }
            }
        } catch (SQLException e) {
            System.err.println("Erro ao autenticar usuário: " + e.getMessage());
            lblStatus.setText("❌ Erro de conexão com o banco!");
            lblStatus.setForeground(Color.RED);
        }
        
        return false;
    }
    
    private void abrirSistemaPrincipal() {
        try {
            // Abrir tela principal
            PDVPrincipalSwingController principal = new PDVPrincipalSwingController();
            principal.show();
            
            System.out.println("=== SISTEMA PRINCIPAL ABERTO ===");
            System.out.println("Usuário: " + usuarioAutenticado);
            System.out.println("Perfil: " + perfilUsuario);
            
        } catch (Exception e) {
            System.err.println("Erro ao abrir sistema principal: " + e.getMessage());
            JOptionPane.showMessageDialog(loginFrame, 
                "Erro ao abrir o sistema principal: " + e.getMessage(), 
                "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    public void showFrame() {
        loginFrame.setVisible(true);
    }
    
    // Getters estáticos para informações do usuário
    public static String getUsuarioAutenticado() {
        return usuarioAutenticado;
    }
    
    public static String getPerfilUsuario() {
        return perfilUsuario;
    }
    
    public static boolean isAdmin() {
        return "ADMIN".equals(perfilUsuario);
    }
    
    public static boolean isGerente() {
        return "GERENTE".equals(perfilUsuario) || "ADMIN".equals(perfilUsuario);
    }
    
    // Método para compatibilidade com testes
    public JFrame getFrame() {
        return loginFrame;
    }
    
    // Método main para teste
    public static void main(String[] args) {
        // Look and feel moderno
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        SwingUtilities.invokeLater(() -> {
            PDVLoginSwingController login = new PDVLoginSwingController();
            login.showFrame();
        });
    }
}
