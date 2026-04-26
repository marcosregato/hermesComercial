package com.br.hermescomercial.theme;

import javax.swing.*;
import java.awt.*;

/**
 * Tema Visual Moderno e Elegante para o Sistema Hermes Comercial PDV
 * Versão 2.0 - Design Premium
 */
public class ModernTheme {
    
    // Paleta de Cores Principal
    public static final Color PRIMARY_COLOR = new Color(41, 128, 185);      // Azul profissional
    public static final Color SECONDARY_COLOR = new Color(52, 152, 219);     // Azul claro
    public static final Color ACCENT_COLOR = new Color(231, 76, 60);          // Vermelho acento
    public static final Color SUCCESS_COLOR = new Color(39, 174, 96);        // Verde sucesso
    public static final Color WARNING_COLOR = new Color(241, 196, 15);        // Amarelo aviso
    public static final Color DANGER_COLOR = new Color(231, 76, 60);          // Vermelho perigo
    
    // Cores de Fundo
    public static final Color BACKGROUND_PRIMARY = new Color(245, 247, 250);   // Cinza muito claro
    public static final Color BACKGROUND_SECONDARY = new Color(255, 255, 255); // Branco puro
    public static final Color BACKGROUND_DARK = new Color(44, 62, 80);         // Cinza escuro
    public static final Color BACKGROUND_CARD = new Color(255, 255, 255);     // Branco para cards
    
    // Cores de Texto
    public static final Color TEXT_PRIMARY = new Color(44, 62, 80);           // Texto principal
    public static final Color TEXT_SECONDARY = new Color(127, 140, 141);     // Texto secundário
    public static final Color TEXT_WHITE = new Color(255, 255, 255);         // Texto branco
    public static final Color TEXT_MUTED = new Color(149, 165, 166);         // Texto suave
    
    // Cores de Borda
    public static final Color BORDER_LIGHT = new Color(189, 195, 199);       // Borda clara
    public static final Color BORDER_DARK = new Color(52, 73, 94);           // Borda escura
    public static final Color BORDER_ACCENT = new Color(41, 128, 185);        // Borda acento
    
    // Gradientes
    public static final Color[] GRADIENT_PRIMARY = {
        new Color(41, 128, 185),
        new Color(52, 152, 219)
    };
    
    public static final Color[] GRADIENT_SUCCESS = {
        new Color(39, 174, 96),
        new Color(46, 204, 113)
    };
    
    public static final Color[] GRADIENT_DANGER = {
        new Color(231, 76, 60),
        new Color(192, 57, 43)
    };
    
    // Fontes
    public static final Font FONT_TITLE = new Font("Segoe UI", Font.BOLD, 24);
    public static final Font FONT_HEADER = new Font("Segoe UI", Font.BOLD, 18);
    public static final Font FONT_SUBHEADER = new Font("Segoe UI", Font.BOLD, 14);
    public static final Font FONT_BODY = new Font("Segoe UI", Font.PLAIN, 12);
    public static final Font FONT_CAPTION = new Font("Segoe UI", Font.PLAIN, 10);
    public static final Font FONT_BUTTON = new Font("Segoe UI", Font.BOLD, 12);
    public static final Font FONT_MONO = new Font("Consolas", Font.PLAIN, 12);
    
    // Sombras
    public static final Color SHADOW_LIGHT = new Color(0, 0, 0, 30);
    public static final Color SHADOW_MEDIUM = new Color(0, 0, 0, 60);
    public static final Color SHADOW_DARK = new Color(0, 0, 0, 90);
    
    // Bordas Arredondadas
    public static final int BORDER_RADIUS_SMALL = 4;
    public static final int BORDER_RADIUS_MEDIUM = 8;
    public static final int BORDER_RADIUS_LARGE = 12;
    
    /**
     * Aplica o tema visual moderno ao Look and Feel
     */
    public static void applyModernTheme() {
        try {
            // Configurar UIManager para cores modernas
            UIManager.put("Panel.background", BACKGROUND_PRIMARY);
            UIManager.put("OptionPane.background", BACKGROUND_SECONDARY);
            UIManager.put("OptionPane.messageForeground", TEXT_PRIMARY);
            UIManager.put("OptionPane.buttonBackground", PRIMARY_COLOR);
            UIManager.put("OptionPane.buttonForeground", TEXT_WHITE);
            
            // Botões
            UIManager.put("Button.background", PRIMARY_COLOR);
            UIManager.put("Button.foreground", TEXT_WHITE);
            UIManager.put("Button.focus", new Color(0, 0, 0, 0));
            UIManager.put("Button.border", BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(BORDER_LIGHT),
                BorderFactory.createEmptyBorder(8, 16, 8, 16)
            ));
            UIManager.put("Button.font", FONT_BUTTON);
            UIManager.put("Button.select", SECONDARY_COLOR);
            
            // Labels
            UIManager.put("Label.foreground", TEXT_PRIMARY);
            UIManager.put("Label.font", FONT_BODY);
            
            // Text Fields
            UIManager.put("TextField.background", BACKGROUND_SECONDARY);
            UIManager.put("TextField.foreground", TEXT_PRIMARY);
            UIManager.put("TextField.border", BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(BORDER_LIGHT),
                BorderFactory.createEmptyBorder(8, 12, 8, 12)
            ));
            UIManager.put("TextField.font", FONT_BODY);
            UIManager.put("TextField.selectionBackground", SECONDARY_COLOR);
            UIManager.put("TextField.selectionForeground", TEXT_WHITE);
            
            // ComboBox
            UIManager.put("ComboBox.background", BACKGROUND_SECONDARY);
            UIManager.put("ComboBox.foreground", TEXT_PRIMARY);
            UIManager.put("ComboBox.border", BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(BORDER_LIGHT),
                BorderFactory.createEmptyBorder(6, 10, 6, 10)
            ));
            UIManager.put("ComboBox.font", FONT_BODY);
            
            // Table
            UIManager.put("Table.background", BACKGROUND_SECONDARY);
            UIManager.put("Table.foreground", TEXT_PRIMARY);
            UIManager.put("Table.selectionBackground", SECONDARY_COLOR);
            UIManager.put("Table.selectionForeground", TEXT_WHITE);
            UIManager.put("Table.gridColor", BORDER_LIGHT);
            UIManager.put("TableHeader.background", BACKGROUND_PRIMARY);
            UIManager.put("TableHeader.foreground", TEXT_PRIMARY);
            UIManager.put("TableHeader.font", FONT_SUBHEADER);
            
            // ScrollPane
            UIManager.put("ScrollPane.background", BACKGROUND_PRIMARY);
            UIManager.put("ScrollPane.border", BorderFactory.createEmptyBorder());
            
            // TabbedPane
            UIManager.put("TabbedPane.background", BACKGROUND_PRIMARY);
            UIManager.put("TabbedPane.selected", BACKGROUND_SECONDARY);
            UIManager.put("TabbedPane.foreground", TEXT_PRIMARY);
            UIManager.put("TabbedPane.border", BorderFactory.createEmptyBorder());
            UIManager.put("TabbedPane.font", FONT_SUBHEADER);
            
            // Menu
            UIManager.put("Menu.background", BACKGROUND_DARK);
            UIManager.put("Menu.foreground", TEXT_WHITE);
            UIManager.put("Menu.font", FONT_BODY);
            UIManager.put("MenuItem.background", BACKGROUND_DARK);
            UIManager.put("MenuItem.foreground", TEXT_WHITE);
            UIManager.put("MenuItem.font", FONT_BODY);
            
            // TitledBorder
            UIManager.put("TitledBorder.titleColor", TEXT_PRIMARY);
            UIManager.put("TitledBorder.font", FONT_SUBHEADER);
            UIManager.put("TitledBorder.border", BorderFactory.createLineBorder(BORDER_LIGHT));
            
        } catch (Exception e) {
            System.err.println("Erro ao aplicar tema: " + e.getMessage());
        }
    }
    
    /**
     * Cria um botão moderno com estilo elegante
     */
    public static JButton createModernButton(String text, Color backgroundColor, Color textColor) {
        JButton button = new JButton(text) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
                
                // Preenchimento com gradiente
                GradientPaint gradient = new GradientPaint(
                    0, 0, backgroundColor.brighter(),
                    0, getHeight(), backgroundColor.darker()
                );
                g2d.setPaint(gradient);
                g2d.fillRoundRect(0, 0, getWidth(), getHeight(), BORDER_RADIUS_MEDIUM, BORDER_RADIUS_MEDIUM);
                
                // Texto
                g2d.setColor(textColor);
                g2d.setFont(FONT_BUTTON);
                FontMetrics fm = g2d.getFontMetrics();
                int x = (getWidth() - fm.stringWidth(getText())) / 2;
                int y = (getHeight() - fm.getHeight()) / 2 + fm.getAscent();
                g2d.drawString(getText(), x, y);
                
                g2d.dispose();
            }
        };
        
        button.setContentAreaFilled(false);
        button.setBorderPainted(false);
        button.setFocusPainted(false);
        button.setOpaque(false);
        button.setFont(FONT_BUTTON);
        button.setForeground(textColor);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        // Efeito hover
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent e) {
                button.setBackground(backgroundColor.brighter());
                button.repaint();
            }
            
            @Override
            public void mouseExited(java.awt.event.MouseEvent e) {
                button.setBackground(backgroundColor);
                button.repaint();
            }
        });
        
        return button;
    }
    
    /**
     * Cria um painel com borda arredondada e sombra
     */
    public static JPanel createModernCardPanel() {
        JPanel panel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                // Sombra
                g2d.setColor(SHADOW_LIGHT);
                g2d.fillRoundRect(4, 4, getWidth() - 8, getHeight() - 8, BORDER_RADIUS_MEDIUM, BORDER_RADIUS_MEDIUM);
                
                // Fundo
                g2d.setColor(BACKGROUND_CARD);
                g2d.fillRoundRect(0, 0, getWidth() - 4, getHeight() - 4, BORDER_RADIUS_MEDIUM, BORDER_RADIUS_MEDIUM);
                
                g2d.dispose();
            }
        };
        
        panel.setOpaque(false);
        panel.setBorder(BorderFactory.createEmptyBorder(16, 16, 16, 16));
        
        return panel;
    }
    
    /**
     * Cria um título elegante
     */
    public static JLabel createModernTitle(String text) {
        JLabel label = new JLabel(text) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
                
                // Sombra no texto
                g2d.setColor(SHADOW_LIGHT);
                g2d.setFont(FONT_TITLE);
                FontMetrics fm = g2d.getFontMetrics();
                int x = (getWidth() - fm.stringWidth(getText())) / 2;
                int y = (getHeight() - fm.getHeight()) / 2 + fm.getAscent() + 2;
                g2d.drawString(getText(), x + 1, y + 1);
                
                // Texto principal
                g2d.setColor(PRIMARY_COLOR);
                g2d.drawString(getText(), x, y);
                
                g2d.dispose();
            }
        };
        
        label.setFont(FONT_TITLE);
        label.setForeground(PRIMARY_COLOR);
        label.setHorizontalAlignment(SwingConstants.CENTER);
        label.setVerticalAlignment(SwingConstants.CENTER);
        
        return label;
    }
    
    /**
     * Cria um separador elegante
     */
    public static JSeparator createModernSeparator() {
        JSeparator separator = new JSeparator() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                g2d.setColor(BORDER_LIGHT);
                g2d.drawLine(0, getHeight() / 2, getWidth(), getHeight() / 2);
                
                g2d.dispose();
            }
        };
        
        separator.setForeground(BORDER_LIGHT);
        
        return separator;
    }
}
