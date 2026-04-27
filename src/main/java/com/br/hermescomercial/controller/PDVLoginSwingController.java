package com.br.hermescomercial.controller;

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
        loginFrame.setSize(480, 650);
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
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 28));
        lblTitulo.setForeground(new Color(52, 73, 94)); // Azul suave profundo
        lblTitulo.setOpaque(true);
        cardGbc.gridy = 1;
        cardGbc.insets = new Insets(0, 0, 6, 0);
        cardPanel.add(lblTitulo, cardGbc);
        
        // Subtítulo elegante
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
        
        lblUsuario = new JLabel("👤 Usuário");
        lblUsuario.setFont(new Font("Segoe UI", Font.BOLD, 12));
        lblUsuario.setForeground(new Color(149, 165, 166)); // Cinza azulado suave
        lblUsuario.setOpaque(true);
        usuarioPanel.add(lblUsuario, BorderLayout.NORTH);
        
        txtUsuario = new JTextField() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                if (getText().isEmpty()) {
                    Graphics2D g2d = (Graphics2D) g;
                    g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                    g2d.setColor(new Color(189, 195, 199));
                    g2d.setFont(getFont().deriveFont(Font.ITALIC));
                    g2d.drawString("Digite seu usuário", 8, getHeight() / 2 + 5);
                }
            }
        };
        txtUsuario.setFont(new Font("Segoe UI", Font.PLAIN, 18));
        txtUsuario.setPreferredSize(new Dimension(450, 45));
        txtUsuario.setBackground(Color.WHITE);
        txtUsuario.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(149, 165, 166), 2), // Cinza azulado suave
            BorderFactory.createEmptyBorder(15, 20, 15, 20)
        ));
        txtUsuario.setOpaque(true);
        usuarioPanel.add(txtUsuario, BorderLayout.CENTER);
        
        cardGbc.gridy = 3;
        cardGbc.insets = new Insets(0, 0, 15, 0);
        cardPanel.add(usuarioPanel, cardGbc);
        
        // Campo Senha
        JPanel senhaPanel = new JPanel(new BorderLayout());
        senhaPanel.setOpaque(false);
        
        lblSenha = new JLabel("🔒 Senha");
        lblSenha.setFont(new Font("Segoe UI", Font.BOLD, 12));
        lblSenha.setForeground(new Color(149, 165, 166)); // Cinza azulado suave
        lblSenha.setOpaque(true);
        senhaPanel.add(lblSenha, BorderLayout.NORTH);
        
        txtSenha = new JPasswordField() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                if (getPassword().length == 0) {
                    Graphics2D g2d = (Graphics2D) g;
                    g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                    g2d.setColor(new Color(189, 195, 199));
                    g2d.setFont(getFont().deriveFont(Font.ITALIC));
                    g2d.drawString("Digite sua senha", 8, getHeight() / 2 + 5);
                }
            }
        };
        txtSenha.setFont(new Font("Segoe UI", Font.PLAIN, 18));
        txtSenha.setPreferredSize(new Dimension(450, 45));
        txtSenha.setBackground(Color.WHITE);
        txtSenha.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(149, 165, 166), 2), // Cinza azulado suave
            BorderFactory.createEmptyBorder(15, 20, 15, 20)
        ));
        txtSenha.setOpaque(true);
        senhaPanel.add(txtSenha, BorderLayout.CENTER);
        
        cardGbc.gridy = 4;
        cardGbc.insets = new Insets(0, 0, 15, 0);
        cardPanel.add(senhaPanel, cardGbc);
        
        // Botões
        JPanel buttonPanel = new JPanel(new GridLayout(1, 2, 30, 0));
        buttonPanel.setOpaque(false);
        
        btnLogin = new JButton("🚀 Entrar");
        btnLogin.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnLogin.setBackground(new Color(106, 176, 76)); // Verde suave
        btnLogin.setForeground(new Color(255, 255, 255)); // Branco elegante
        btnLogin.setFocusPainted(false);
        btnLogin.setOpaque(true);
        btnLogin.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(85, 150, 60), 2), // Verde borda suave
            BorderFactory.createEmptyBorder(12, 25, 12, 25)
        ));
        btnLogin.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnLogin.setOpaque(true);
        
        // Efeito hover elegante
        btnLogin.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btnLogin.setBackground(new Color(85, 150, 60)); // Verde mais escuro suave
                btnLogin.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(new Color(70, 130, 45), 2), // Verde ainda mais escuro
                    BorderFactory.createEmptyBorder(12, 25, 12, 25)
                ));
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btnLogin.setBackground(new Color(106, 176, 76)); // Verde suave
                btnLogin.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(new Color(85, 150, 60), 2), // Verde borda suave
                    BorderFactory.createEmptyBorder(12, 25, 12, 25)
                ));
            }
        });
        
        btnSair = com.br.hermescomercial.theme.ModernTheme.createPastelButton("❌ Sair", com.br.hermescomercial.theme.ModernTheme.PASTEL_CORAL, com.br.hermescomercial.theme.ModernTheme.TEXT_PRIMARY);
        
        buttonPanel.add(btnLogin);
        buttonPanel.add(btnSair);
        
        cardGbc.gridy = 5;
        cardGbc.insets = new Insets(0, 0, 25, 0);
        cardPanel.add(buttonPanel, cardGbc);
        
        // Status
        lblStatus = new JLabel("Digite suas credenciais para acessar o sistema", SwingConstants.CENTER);
        lblStatus.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        lblStatus.setForeground(new Color(127, 140, 141)); // Cinza suave
        lblStatus.setOpaque(true);
        lblStatus.setBackground(new Color(240, 240, 240)); // Cinza claro
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
        
        // Enter nos campos
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
            public void keyTyped(KeyEvent e) {}
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
            lblStatus.setForeground(Color.YELLOW);
            return;
        }
        
        lblStatus.setText("🔄 Autenticando...");
        lblStatus.setForeground(Color.WHITE);
        
        // Validar no banco de dados
        if (autenticarUsuario(usuario, senha)) {
            lblStatus.setText("✅ Login realizado com sucesso!");
            lblStatus.setForeground(new Color(76, 175, 80));
            
            // Abrir sistema principal
            SwingUtilities.invokeLater(() -> {
                loginFrame.dispose();
                abrirSistemaPrincipal();
            });
        } else {
            lblStatus.setText("❌ Usuário ou senha inválidos!");
            lblStatus.setForeground(Color.RED);
            txtSenha.setText("");
            txtSenha.requestFocus();
        }
    }
    
    private boolean autenticarUsuario(String usuario, String senha) {
        String sql = "SELECT id, nome, login, perfil FROM usuario WHERE login = ? AND senha = ? AND ativo = TRUE";
        
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
