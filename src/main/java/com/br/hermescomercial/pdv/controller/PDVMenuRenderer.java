package com.br.hermescomercial.pdv.controller;

import com.br.hermescomercial.util.SystemLogger;
import javax.swing.*;
import java.awt.*;

/**
 * Classe especializada para renderização de componentes do menu lateral
 * Extrai responsabilidades de UI do PDVMenuLateralElegante
 */
public class PDVMenuRenderer {
    
    // Cores constantes
    private static final Color WHITE = Color.WHITE;
    private static final Color DARK_GRAY = Color.DARK_GRAY;
    private static final Color ACCENT_COLOR = new Color(52, 152, 219);
    private static final Color PRIMARY_COLOR = new Color(52, 152, 219);
    private static final Color SUCCESS_COLOR = new Color(39, 174, 96);
    private static final Color MEDIUM_GRAY = new Color(149, 165, 166);
    
    /**
     * Cria o painel principal do menu lateral
     */
    public JPanel criarMenuLateralPanel(JPanel headerPanel, JPanel menusPanel) {
        try {
            JPanel menuPanel = new JPanel(new BorderLayout());
            menuPanel.setBackground(WHITE);
            menuPanel.setPreferredSize(new Dimension(280, 0));
            menuPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
            
            // Header do menu
            menuPanel.add(headerPanel, BorderLayout.NORTH);
            
            // Painel de menus com submenus integrados
            JPanel scrollContainer = new JPanel(new BorderLayout());
            scrollContainer.setBackground(WHITE);
            scrollContainer.add(menusPanel, BorderLayout.CENTER);
            
            // Scroll pane para menus
            JScrollPane scrollPane = new JScrollPane(scrollContainer);
            scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
            scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
            scrollPane.setBorder(BorderFactory.createEmptyBorder());
            scrollPane.getVerticalScrollBar().setUnitIncrement(16);
            
            menuPanel.add(scrollPane, BorderLayout.CENTER);
            
            SystemLogger.ui("Menu lateral renderizado com sucesso");
            return menuPanel;
            
        } catch (Exception e) {
            SystemLogger.error("Erro ao criar menu lateral", e);
            return criarMenuBasico();
        }
    }
    
    /**
     * Cria o header do menu
     */
    public JPanel criarHeaderMenu(String nomeUsuario) {
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(ACCENT_COLOR);
        headerPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        
        // Logo
        JLabel logoLabel = new JLabel("🏪 Hermes PDV");
        logoLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        logoLabel.setForeground(WHITE);
        
        // Info do usuário
        JLabel userLabel = new JLabel("👤 " + nomeUsuario);
        userLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        userLabel.setForeground(WHITE);
        
        headerPanel.add(logoLabel, BorderLayout.NORTH);
        headerPanel.add(userLabel, BorderLayout.SOUTH);
        
        return headerPanel;
    }
    
    /**
     * Cria um menu com submenus expansíveis
     */
    public void criarMenuComSubmenus(JPanel parentPanel, String menuText, String[] subMenus, 
                                   MenuClickListener clickListener) {
        // Painel principal do menu com layout vertical
        JPanel menuContainer = new JPanel();
        menuContainer.setLayout(new BoxLayout(menuContainer, BoxLayout.Y_AXIS));
        menuContainer.setBackground(WHITE);
        
        // Botão do menu principal
        JPanel menuButtonPanel = new JPanel(new BorderLayout());
        menuButtonPanel.setBackground(WHITE);
        menuButtonPanel.setBorder(BorderFactory.createEmptyBorder(8, 12, 8, 12));
        menuButtonPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        
        JLabel menuLabel = new JLabel(menuText);
        menuLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        menuLabel.setForeground(DARK_GRAY);
        
        // Indicador de expansão
        JLabel expandLabel = new JLabel("▼");
        expandLabel.setFont(new Font("Segoe UI", Font.PLAIN, 10));
        expandLabel.setForeground(DARK_GRAY);
        
        menuButtonPanel.add(menuLabel, BorderLayout.CENTER);
        menuButtonPanel.add(expandLabel, BorderLayout.EAST);
        
        // Painel de submenus (inicialmente oculto)
        JPanel subMenuPanel = new JPanel();
        subMenuPanel.setLayout(new BoxLayout(subMenuPanel, BoxLayout.Y_AXIS));
        subMenuPanel.setBackground(new Color(245, 245, 245));
        subMenuPanel.setBorder(BorderFactory.createEmptyBorder(0, 20, 5, 5));
        subMenuPanel.setVisible(false);
        
        // Adicionar submenus
        for (String subMenu : subMenus) {
            JButton subMenuButton = criarBotaoSubmenu(subMenu, clickListener);
            subMenuPanel.add(subMenuButton);
            subMenuPanel.add(Box.createVerticalStrut(2));
        }
        
        // Evento de clique no menu principal para expandir/recolher
        menuButtonPanel.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                menuButtonPanel.setBackground(new Color(240, 240, 240));
            }
            
            public void mouseExited(java.awt.event.MouseEvent evt) {
                menuButtonPanel.setBackground(WHITE);
            }
            
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                // Alternar visibilidade dos submenus
                boolean isVisible = subMenuPanel.isVisible();
                subMenuPanel.setVisible(!isVisible);
                expandLabel.setText(isVisible ? "▼" : "▲");
                
                // Atualizar o painel pai e o scroll pane
                parentPanel.revalidate();
                parentPanel.repaint();
                parentPanel.getParent().revalidate();
                parentPanel.getParent().repaint();
                
                SystemLogger.ui("Menu " + menuText + " " + (isVisible ? "recolhido" : "expandido"));
            }
        });
        
        // Montar o menu container
        menuContainer.add(menuButtonPanel);
        menuContainer.add(subMenuPanel);
        
        // Adicionar ao painel pai
        parentPanel.add(menuContainer);
        parentPanel.add(Box.createVerticalStrut(3));
    }
    
    /**
     * Cria um botão de submenu
     */
    private JButton criarBotaoSubmenu(String subMenuText, MenuClickListener clickListener) {
        JButton button = new JButton(subMenuText);
        button.setBackground(new Color(245, 245, 245));
        button.setForeground(DARK_GRAY);
        button.setHorizontalAlignment(SwingConstants.LEFT);
        button.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        button.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));
        button.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        // Evento de clique no submenu
        button.addActionListener(e -> {
            if (clickListener != null) {
                clickListener.onSubMenuClick(subMenuText);
            }
        });
        
        // Efeito hover no submenu
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(new Color(230, 230, 230));
            }
            
            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(new Color(245, 245, 245));
            }
        });
        
        return button;
    }
    
    /**
     * Cria um formulário fallback quando as classes especializadas falham
     */
    public JPanel criarFormularioFallback(String itemText, String module, String nomeUsuario, String usuarioAtual) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(WHITE);
        panel.setPreferredSize(new Dimension(800, 600));
        
        // Header
        JPanel headerPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        headerPanel.setBackground(ACCENT_COLOR);
        headerPanel.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));
        
        JLabel titleLabel = new JLabel(itemText);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
        titleLabel.setForeground(WHITE);
        
        JLabel moduleLabel = new JLabel("[" + module + "]");
        moduleLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        moduleLabel.setForeground(WHITE);
        
        headerPanel.add(titleLabel);
        headerPanel.add(Box.createHorizontalStrut(10));
        headerPanel.add(moduleLabel);
        
        // Conteúdo principal
        JPanel contentPanel = new JPanel(new GridBagLayout());
        contentPanel.setBackground(WHITE);
        contentPanel.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.anchor = GridBagConstraints.WEST;
        
        // Mensagem informativa
        JLabel infoLabel = new JLabel("📋 Formulário em Desenvolvimento");
        infoLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        infoLabel.setForeground(PRIMARY_COLOR);
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
        contentPanel.add(infoLabel, gbc);
        
        // Detalhes
        gbc.gridy = 1; gbc.gridwidth = 1;
        JLabel itemLabel = new JLabel("Item: " + itemText);
        itemLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        contentPanel.add(itemLabel, gbc);
        
        gbc.gridy = 2;
        JLabel moduleInfoLabel = new JLabel("Módulo: " + module);
        moduleInfoLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        contentPanel.add(moduleInfoLabel, gbc);
        
        gbc.gridy = 3;
        JLabel userLabel = new JLabel("Usuário: " + nomeUsuario + " (" + usuarioAtual + ")");
        userLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        contentPanel.add(userLabel, gbc);
        
        // Campos de exemplo
        gbc.gridy = 4; gbc.gridwidth = 2;
        JLabel separatorLabel = new JLabel("─");
        separatorLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        separatorLabel.setForeground(MEDIUM_GRAY);
        contentPanel.add(separatorLabel, gbc);
        
        gbc.gridy = 5; gbc.gridwidth = 1;
        JLabel campo1Label = new JLabel("Campo 1:");
        campo1Label.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        contentPanel.add(campo1Label, gbc);
        
        gbc.gridx = 1;
        JTextField campo1Text = new JTextField(20);
        campo1Text.setText("Exemplo de campo");
        contentPanel.add(campo1Text, gbc);
        
        gbc.gridy = 6; gbc.gridx = 0;
        JLabel campo2Label = new JLabel("Campo 2:");
        campo2Label.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        contentPanel.add(campo2Label, gbc);
        
        gbc.gridx = 1;
        JTextField campo2Text = new JTextField(20);
        campo2Text.setText("Outro campo exemplo");
        contentPanel.add(campo2Text, gbc);
        
        // Botões
        gbc.gridy = 7; gbc.gridx = 0; gbc.gridwidth = 2;
        JPanel buttonPanel = new JPanel(new FlowLayout());
        buttonPanel.setBackground(WHITE);
        
        JButton salvarButton = new JButton("💾 Salvar");
        salvarButton.setBackground(SUCCESS_COLOR);
        salvarButton.setForeground(WHITE);
        salvarButton.setFocusPainted(false);
        salvarButton.setBorderPainted(false);
        
        JButton cancelarButton = new JButton("❌ Cancelar");
        cancelarButton.setBackground(MEDIUM_GRAY);
        cancelarButton.setForeground(WHITE);
        cancelarButton.setFocusPainted(false);
        cancelarButton.setBorderPainted(false);
        
        buttonPanel.add(salvarButton);
        buttonPanel.add(Box.createHorizontalStrut(10));
        buttonPanel.add(cancelarButton);
        
        contentPanel.add(buttonPanel, gbc);
        
        // Montar painel
        panel.add(headerPanel, BorderLayout.NORTH);
        panel.add(contentPanel, BorderLayout.CENTER);
        
        SystemLogger.ui("Formulário fallback criado para: " + itemText);
        
        return panel;
    }
    
    /**
     * Cria um menu básico em caso de erro
     */
    public JPanel criarMenuBasico() {
        JPanel menuPanel = new JPanel(new BorderLayout());
        menuPanel.setBackground(WHITE);
        
        JLabel errorLabel = new JLabel("❌ Erro ao carregar menu");
        errorLabel.setFont(new Font("Arial", Font.BOLD, 14));
        errorLabel.setForeground(Color.RED);
        
        menuPanel.add(errorLabel, BorderLayout.CENTER);
        return menuPanel;
    }
    
    /**
     * Interface para callback de cliques em submenus
     */
    public interface MenuClickListener {
        void onSubMenuClick(String subMenuText);
    }
}
