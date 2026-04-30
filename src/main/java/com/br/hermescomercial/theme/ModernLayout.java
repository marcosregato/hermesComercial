package com.br.hermescomercial.theme;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;

/**
 * Layout Moderno para todas as telas do sistema
 * Componentes visuais modernos com cards, sombras e animações
 * 
 * @version 2.8.0
 * @author Hermes Comercial
 */
public class ModernLayout {
    
    // === CORES PARA LAYOUT MODERNO ===
    public static final Color CARD_BACKGROUND = Color.WHITE;
    public static final Color CARD_SHADOW = new Color(0, 0, 0, 30);
    public static final Color CARD_BORDER = new Color(229, 231, 235);
    public static final Color CARD_HOVER_SHADOW = new Color(0, 0, 0, 50);
    
    // === BORDAS MODERNAS ===
    public static final Border CARD_BORDER_ROUNDED = BorderFactory.createCompoundBorder(
        BorderFactory.createLineBorder(CARD_BORDER, 1),
        BorderFactory.createEmptyBorder(15, 20, 15, 20)
    );
    
    public static final Border CARD_BORDER_ROUNDED_COLORED = BorderFactory.createCompoundBorder(
        BorderFactory.createLineBorder(OceanoTheme.PRIMARY, 2),
        BorderFactory.createEmptyBorder(15, 20, 15, 20)
    );
    
    public static final Border INPUT_BORDER_MODERN = BorderFactory.createCompoundBorder(
        BorderFactory.createLineBorder(OceanoTheme.TABLE_GRID, 1),
        BorderFactory.createEmptyBorder(12, 15, 12, 15)
    );
    
    public static final Border INPUT_BORDER_FOCUSED = BorderFactory.createCompoundBorder(
        BorderFactory.createLineBorder(OceanoTheme.PRIMARY, 2),
        BorderFactory.createEmptyBorder(12, 15, 12, 15)
    );
    
    /**
     * Cria um painel com estilo de card moderno
     */
    public static JPanel createModernCard() {
        JPanel card = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
                
                // Desenhar sombra suave
                int shadowOffset = 3;
                g2d.setColor(CARD_SHADOW);
                g2d.fillRoundRect(
                    shadowOffset, shadowOffset, 
                    getWidth() - shadowOffset * 2, 
                    getHeight() - shadowOffset * 2, 
                    15, 15
                );
                
                // Desenhar fundo do card
                g2d.setColor(CARD_BACKGROUND);
                g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 15, 15);
                
                // Desenhar borda
                g2d.setColor(CARD_BORDER);
                g2d.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 15, 15);
                
                g2d.dispose();
            }
        };
        card.setOpaque(false);
        card.setBackground(new Color(0, 0, 0, 0));
        return card;
    }
    
    /**
     * Cria um painel com estilo de card colorido
     */
    public static JPanel createColoredCard(Color accentColor) {
        JPanel card = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
                
                // Desenhar sombra suave
                int shadowOffset = 4;
                g2d.setColor(CARD_HOVER_SHADOW);
                g2d.fillRoundRect(
                    shadowOffset, shadowOffset, 
                    getWidth() - shadowOffset * 2, 
                    getHeight() - shadowOffset * 2, 
                    15, 15
                );
                
                // Desenhar fundo do card
                g2d.setColor(CARD_BACKGROUND);
                g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 15, 15);
                
                // Desenhar borda colorida
                g2d.setColor(accentColor);
                g2d.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 15, 15);
                
                g2d.dispose();
            }
        };
        card.setOpaque(false);
        card.setBackground(new Color(0, 0, 0, 0));
        return card;
    }
    
    /**
     * Cria um campo de texto com estilo moderno
     */
    public static JTextField createModernTextField(String placeholder) {
        JTextField textField = new JTextField(placeholder) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                if (getText().isEmpty() && ! (FocusManager.getCurrentKeyboardFocusManager().getFocusOwner() == this)) {
                    Graphics2D g2d = (Graphics2D) g.create();
                    g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                    g2d.setColor(Color.GRAY);
                    g2d.setFont(getFont().deriveFont(Font.ITALIC));
                    Insets insets = getInsets();
                    g2d.drawString(placeholder, insets.left + 5, getHeight() / 2 + getFont().getSize() / 4);
                    g2d.dispose();
                }
            }
        };
        
        textField.setBackground(CARD_BACKGROUND);
        textField.setForeground(OceanoTheme.TEXT_PRIMARY);
        textField.setFont(OceanoTheme.FONT_BODY);
        textField.setBorder(INPUT_BORDER_MODERN);
        textField.setOpaque(false);
        
        // Efeito de foco
        textField.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent e) {
                textField.setBorder(INPUT_BORDER_FOCUSED);
                textField.setBackground(CARD_BACKGROUND);
            }
            
            public void focusLost(java.awt.event.FocusEvent e) {
                textField.setBorder(INPUT_BORDER_MODERN);
                textField.setBackground(CARD_BACKGROUND);
            }
        });
        
        return textField;
    }
    
    /**
     * Cria um campo de senha com estilo moderno
     */
    public static JPasswordField createModernPasswordField() {
        JPasswordField passwordField = new JPasswordField() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                if (getPassword().length == 0 && ! (FocusManager.getCurrentKeyboardFocusManager().getFocusOwner() == this)) {
                    Graphics2D g2d = (Graphics2D) g.create();
                    g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                    g2d.setColor(Color.GRAY);
                    g2d.setFont(getFont().deriveFont(Font.ITALIC));
                    Insets insets = getInsets();
                    g2d.drawString("Digite sua senha", insets.left + 5, getHeight() / 2 + getFont().getSize() / 4);
                    g2d.dispose();
                }
            }
        };
        
        passwordField.setBackground(CARD_BACKGROUND);
        passwordField.setForeground(OceanoTheme.TEXT_PRIMARY);
        passwordField.setFont(OceanoTheme.FONT_BODY);
        passwordField.setBorder(INPUT_BORDER_MODERN);
        passwordField.setOpaque(false);
        
        // Efeito de foco
        passwordField.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent e) {
                passwordField.setBorder(INPUT_BORDER_FOCUSED);
                passwordField.setBackground(CARD_BACKGROUND);
            }
            
            public void focusLost(java.awt.event.FocusEvent e) {
                passwordField.setBorder(INPUT_BORDER_MODERN);
                passwordField.setBackground(CARD_BACKGROUND);
            }
        });
        
        return passwordField;
    }
    
    /**
     * Cria um botão com estilo moderno e efeito hover
     */
    public static JButton createModernButton(String text, Color backgroundColor) {
        JButton button = new JButton(text) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
                
                // Desenhar sombra
                if (getModel().isPressed()) {
                    g2d.setColor(new Color(0, 0, 0, 40));
                    g2d.fillRoundRect(3, 3, getWidth() - 6, getHeight() - 6, 10, 10);
                } else if (getModel().isRollover()) {
                    g2d.setColor(new Color(0, 0, 0, 60));
                    g2d.fillRoundRect(4, 4, getWidth() - 8, getHeight() - 8, 10, 10);
                }
                
                // Desenhar fundo
                Color bgColor = getModel().isPressed() ? 
                    backgroundColor.darker() : 
                    getModel().isRollover() ? backgroundColor.brighter() : 
                    backgroundColor;
                g2d.setColor(bgColor);
                g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 10, 10);
                
                // Desenhar texto
                g2d.setColor(Color.WHITE);
                g2d.setFont(getFont());
                FontMetrics fm = g2d.getFontMetrics();
                int x = (getWidth() - fm.stringWidth(getText())) / 2;
                int y = (getHeight() - fm.getHeight()) / 2 + fm.getAscent();
                g2d.drawString(getText(), x, y);
                
                g2d.dispose();
            }
        };
        
        button.setBackground(new Color(0, 0, 0, 0));
        button.setForeground(Color.WHITE);
        button.setFont(OceanoTheme.FONT_BUTTON);
        button.setBorder(BorderFactory.createEmptyBorder(12, 25, 12, 25));
        button.setContentAreaFilled(false);
        button.setFocusPainted(false);
        button.setOpaque(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        return button;
    }
    
    /**
     * Cria um painel com espaçamento moderno
     */
    public static JPanel createModernPanel(int spacing) {
        JPanel panel = new JPanel();
        panel.setBackground(OceanoTheme.BACKGROUND);
        panel.setBorder(BorderFactory.createEmptyBorder(spacing, spacing, spacing, spacing));
        return panel;
    }
    
    /**
     * Cria um painel com layout em grade moderna
     */
    public static JPanel createModernGridPanel(int rows, int cols, int hgap, int vgap) {
        JPanel panel = new JPanel(new GridLayout(rows, cols, hgap, vgap));
        panel.setBackground(OceanoTheme.BACKGROUND);
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        return panel;
    }
    
    /**
     * Cria um separador moderno
     */
    public static JSeparator createModernSeparator() {
        JSeparator separator = new JSeparator();
        separator.setForeground(OceanoTheme.TABLE_GRID);
        separator.setBackground(OceanoTheme.TABLE_GRID);
        return separator;
    }
    
    /**
     * Cria um título moderno
     */
    public static JLabel createModernTitle(String text) {
        JLabel title = new JLabel(text);
        title.setFont(OceanoTheme.FONT_TITLE);
        title.setForeground(OceanoTheme.PRIMARY);
        title.setBorder(BorderFactory.createEmptyBorder(10, 0, 20, 0));
        return title;
    }
    
    /**
     * Cria um subtítulo moderno
     */
    public static JLabel createModernSubtitle(String text) {
        JLabel subtitle = new JLabel(text);
        subtitle.setFont(OceanoTheme.FONT_HEADER);
        subtitle.setForeground(OceanoTheme.TEXT_SECONDARY);
        subtitle.setBorder(BorderFactory.createEmptyBorder(5, 0, 15, 0));
        return subtitle;
    }
    
    /**
     * Adiciona espaçamento vertical
     */
    public static Component createVerticalSpace(int height) {
        return Box.createRigidArea(new Dimension(0, height));
    }
    
    /**
     * Adiciona espaçamento horizontal
     */
    public static Component createHorizontalSpace(int width) {
        return Box.createRigidArea(new Dimension(width, 0));
    }
    
    /**
     * Cria um wrapper com scroll moderno
     */
    public static JScrollPane createModernScrollPane(Component component) {
        JScrollPane scrollPane = new JScrollPane(component);
        scrollPane.setBackground(OceanoTheme.BACKGROUND);
        scrollPane.getViewport().setBackground(OceanoTheme.BACKGROUND);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        
        // Customizar scrollbar
        JScrollBar verticalScrollBar = scrollPane.getVerticalScrollBar();
        verticalScrollBar.setPreferredSize(new Dimension(8, 0));
        verticalScrollBar.setOpaque(false);
        verticalScrollBar.setBackground(OceanoTheme.BACKGROUND);
        
        return scrollPane;
    }
    
    /**
     * Aplica efeitos de hover a um componente
     */
    public static void addHoverEffect(JComponent component) {
        component.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                component.setCursor(new Cursor(Cursor.HAND_CURSOR));
            }
            
            public void mouseExited(java.awt.event.MouseEvent evt) {
                component.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
            }
        });
    }
    
    /**
     * Informações sobre o layout moderno
     */
    public static String getLayoutInfo() {
        return """
        🎨 LAYOUT MODERNO - HERMES COMERCIAL
        ====================================
        • Cards com sombras suaves e bordas arredondadas
        • Campos de texto com placeholders e efeitos de foco
        • Botões com animações hover e press
        • Espaçamento consistente e alinhamento preciso
        • Design responsivo e acessível
        • Cores consistentes com tema Oceano
        """;
    }
}
