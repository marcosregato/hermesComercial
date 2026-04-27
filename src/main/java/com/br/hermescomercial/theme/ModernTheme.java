package com.br.hermescomercial.theme;

import javax.swing.*;
import java.awt.*;

/**
 * Tema Visual Moderno e Elegante para o Sistema Hermes Comercial PDV
 * Versão 2.0 - Design Premium
 */
public class ModernTheme {
    
    // Paleta de Cores Principal - Melhorada para PDV
    public static final Color PRIMARY_COLOR = new Color(30, 144, 255);      // Azul vibrante (DodgerBlue)
    public static final Color SECONDARY_COLOR = new Color(70, 130, 180);     // Azul aço (SteelBlue)
    public static final Color ACCENT_COLOR = new Color(255, 69, 0);          // Laranja vibrante (OrangeRed)
    public static final Color SUCCESS_COLOR = new Color(0, 200, 83);          // Verde vibrante (Emerald)
    public static final Color WARNING_COLOR = new Color(255, 193, 7);          // Amarelo dourado (Golden)
    public static final Color DANGER_COLOR = new Color(220, 53, 69);          // Vermelho vibrante (Crimson)
    
    // Cores de Fundo - Melhoradas para PDV
    public static final Color BACKGROUND_PRIMARY = new Color(248, 249, 250);   // Cinza ultra claro (GhostWhite)
    public static final Color BACKGROUND_SECONDARY = new Color(255, 255, 255); // Branco puro
    public static final Color BACKGROUND_DARK = new Color(33, 37, 41);         // Cinza escuro profundo
    public static final Color BACKGROUND_CARD = new Color(255, 255, 255);     // Branco para cards
    public static final Color BACKGROUND_HOVER = new Color(240, 244, 248);   // Hover sutil
    
    // Cores de Texto - Melhoradas para PDV
    public static final Color TEXT_PRIMARY = new Color(33, 37, 41);           // Texto principal (contraste alto)
    public static final Color TEXT_SECONDARY = new Color(108, 117, 125);     // Texto secundário
    public static final Color TEXT_WHITE = new Color(255, 255, 255);         // Texto branco
    public static final Color TEXT_MUTED = new Color(173, 181, 189);         // Texto suave
    public static final Color TEXT_HOVER = new Color(0, 123, 255);           // Texto hover (azul)
    
    // Cores de Borda - Melhoradas para PDV
    public static final Color BORDER_LIGHT = new Color(222, 226, 230);       // Borda clara
    public static final Color BORDER_DARK = new Color(52, 58, 64);           // Borda escura
    public static final Color BORDER_ACCENT = new Color(30, 144, 255);        // Borda acento
    public static final Color BORDER_FOCUS = new Color(0, 123, 255);           // Borda foco
    
    // Gradientes - Atualizados com cores vibrantes
    public static final Color[] GRADIENT_PRIMARY = {
        new Color(30, 144, 255),
        new Color(70, 130, 180)
    };
    
    public static final Color[] GRADIENT_SUCCESS = {
        new Color(0, 200, 83),
        new Color(40, 167, 69)
    };
    
    public static final Color[] GRADIENT_DANGER = {
        new Color(220, 53, 69),
        new Color(255, 69, 0)
    };
    
    public static final Color[] GRADIENT_WARNING = {
        new Color(255, 193, 7),
        new Color(255, 152, 0)
    };
    
    // Fontes - Otimizadas para PDV
    public static final Font FONT_TITLE = new Font("Segoe UI", Font.BOLD, 26);
    public static final Font FONT_HEADER = new Font("Segoe UI", Font.BOLD, 20);
    public static final Font FONT_SUBHEADER = new Font("Segoe UI", Font.BOLD, 16);
    public static final Font FONT_BODY = new Font("Segoe UI", Font.PLAIN, 13);
    public static final Font FONT_CAPTION = new Font("Segoe UI", Font.PLAIN, 11);
    public static final Font FONT_BUTTON = new Font("Segoe UI", Font.BOLD, 13);
    public static final Font FONT_MONO = new Font("Consolas", Font.PLAIN, 12);
    public static final Font FONT_LARGE = new Font("Segoe UI", Font.BOLD, 15);
    public static final Font FONT_SMALL = new Font("Segoe UI", Font.PLAIN, 11);
    
    // Sombras - Melhoradas para PDV
    public static final Color SHADOW_LIGHT = new Color(0, 0, 0, 20);
    public static final Color SHADOW_MEDIUM = new Color(0, 0, 0, 40);
    public static final Color SHADOW_DARK = new Color(0, 0, 0, 60);
    public static final Color SHADOW_BUTTON = new Color(0, 0, 0, 25);
    public static final Color SHADOW_CARD = new Color(0, 0, 0, 15);
    
    // Bordas Arredondadas - Otimizadas para PDV
    public static final int BORDER_RADIUS_SMALL = 6;
    public static final int BORDER_RADIUS_MEDIUM = 10;
    public static final int BORDER_RADIUS_LARGE = 15;
    
    // Espaçamentos - Constantes para PDV
    public static final int SPACING_XS = 4;
    public static final int SPACING_SM = 8;
    public static final int SPACING_MD = 16;
    public static final int SPACING_LG = 24;
    public static final int SPACING_XL = 32;
    
    // Dimensões de componentes - Otimizadas para PDV
    public static final int BUTTON_HEIGHT = 40;
    public static final int BUTTON_HEIGHT_SMALL = 32;
    public static final int BUTTON_HEIGHT_LARGE = 48;
    public static final int FIELD_HEIGHT = 40;
    public static final int ICON_SIZE = 20;
    public static final int ICON_SIZE_LARGE = 24;
    
    // Tema Dark Mode - Cores alternativas
    public static final Color DARK_PRIMARY = new Color(13, 110, 253);        // Azul vibrante escuro
    public static final Color DARK_SECONDARY = new Color(108, 117, 125);      // Cinza azulado
    public static final Color DARK_BACKGROUND = new Color(33, 37, 41);         // Fundo escuro
    public static final Color DARK_SURFACE = new Color(52, 58, 64);           // Superfície escura
    public static final Color DARK_CARD = new Color(44, 48, 51);              // Card escuro
    public static final Color DARK_TEXT = new Color(255, 255, 255);           // Texto branco
    public static final Color DARK_TEXT_MUTED = new Color(173, 181, 189);     // Texto suave
    public static final Color DARK_BORDER = new Color(75, 81, 87);            // Borda escura
    public static final Color DARK_SUCCESS = new Color(25, 135, 84);           // Verde escuro
    public static final Color DANGER_WARNING = new Color(255, 193, 7);         // Amarelo escuro
    public static final Color DARK_DANGER = new Color(220, 53, 69);           // Vermelho escuro
    
    // Animações e Transições
    public static final int ANIMATION_DURATION_FAST = 150;   // ms
    public static final int ANIMATION_DURATION_NORMAL = 300;  // ms
    public static final int ANIMATION_DURATION_SLOW = 500;    // ms
    
    // Estados de componente
    public static final Color STATE_DEFAULT = BACKGROUND_SECONDARY;
    public static final Color STATE_HOVER = BACKGROUND_HOVER;
    public static final Color STATE_FOCUSED = PRIMARY_COLOR;
    public static final Color STATE_PRESSED = SECONDARY_COLOR;
    public static final Color STATE_DISABLED = new Color(233, 236, 239);
    public static final Color STATE_SUCCESS = new Color(213, 239, 215);
    public static final Color STATE_WARNING = new Color(255, 243, 205);
    public static final Color STATE_ERROR = new Color(248, 215, 218);
    
    // Paleta de Cores Pastéis para Botões
    public static final Color PASTEL_BLUE = new Color(173, 216, 230);        // Azul bebê
    public static final Color PASTEL_PINK = new Color(255, 182, 193);        // Rosa bebê
    public static final Color PASTEL_GREEN = new Color(144, 238, 144);       // Verde menta
    public static final Color PASTEL_YELLOW = new Color(255, 255, 224);       // Amarelo claro
    public static final Color PASTEL_PURPLE = new Color(221, 160, 221);       // Lavanda
    public static final Color PASTEL_ORANGE = new Color(255, 218, 185);       // Pêssego
    public static final Color PASTEL_CYAN = new Color(224, 255, 255);          // Ciano claro
    public static final Color PASTEL_CORAL = new Color(255, 214, 214);        // Coral claro
    public static final Color PASTEL_MINT = new Color(152, 255, 152);        // Menta fresca
    public static final Color PASTEL_LAVENDER = new Color(230, 230, 250);     // Lavanda suave
    
    /**
     * Aplica o tema visual moderno ao Look and Feel
     */
    public static void applyModernTheme() {
        try {
            // Configurar UIManager para cores modernas - Melhoradas para PDV
            UIManager.put("Panel.background", BACKGROUND_PRIMARY);
            UIManager.put("OptionPane.background", BACKGROUND_SECONDARY);
            UIManager.put("OptionPane.messageForeground", TEXT_PRIMARY);
            UIManager.put("OptionPane.buttonBackground", PRIMARY_COLOR);
            UIManager.put("OptionPane.buttonForeground", TEXT_WHITE);
            
            // Botões - Melhorados
            UIManager.put("Button.background", PRIMARY_COLOR);
            UIManager.put("Button.foreground", TEXT_WHITE);
            UIManager.put("Button.focus", BORDER_FOCUS);
            UIManager.put("Button.border", BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(BORDER_LIGHT),
                BorderFactory.createEmptyBorder(10, 20, 10, 20)
            ));
            UIManager.put("Button.font", FONT_BUTTON);
            UIManager.put("Button.select", SECONDARY_COLOR);
            
            // Labels - Melhorados
            UIManager.put("Label.foreground", TEXT_PRIMARY);
            UIManager.put("Label.font", FONT_BODY);
            UIManager.put("Label.disabledForeground", TEXT_MUTED);
            
            // Text Fields - Melhorados
            UIManager.put("TextField.background", BACKGROUND_SECONDARY);
            UIManager.put("TextField.foreground", TEXT_PRIMARY);
            UIManager.put("TextField.border", BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(BORDER_LIGHT),
                BorderFactory.createEmptyBorder(10, 15, 10, 15)
            ));
            UIManager.put("TextField.font", FONT_BODY);
            UIManager.put("TextField.selectionBackground", PRIMARY_COLOR);
            UIManager.put("TextField.selectionForeground", TEXT_WHITE);
            UIManager.put("TextField.inactiveForeground", TEXT_MUTED);
            
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
            UIManager.put("Menu.foreground", TEXT_PRIMARY);
            UIManager.put("Menu.font", FONT_BODY);
            UIManager.put("MenuItem.background", Color.WHITE);
            UIManager.put("MenuItem.foreground", TEXT_PRIMARY);
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
     * Aplica o tema Dark Mode ideal para PDV
     */
    public static void applyDarkTheme() {
        try {
            // Configurar UIManager para Dark Mode
            UIManager.put("Panel.background", DARK_BACKGROUND);
            UIManager.put("OptionPane.background", DARK_SURFACE);
            UIManager.put("OptionPane.messageForeground", DARK_TEXT);
            UIManager.put("OptionPane.buttonBackground", DARK_PRIMARY);
            UIManager.put("OptionPane.buttonForeground", DARK_TEXT);
            
            // Botões - Dark Mode
            UIManager.put("Button.background", DARK_PRIMARY);
            UIManager.put("Button.foreground", DARK_TEXT);
            UIManager.put("Button.focus", DARK_SUCCESS);
            UIManager.put("Button.border", BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(DARK_BORDER),
                BorderFactory.createEmptyBorder(10, 20, 10, 20)
            ));
            UIManager.put("Button.font", FONT_BUTTON);
            UIManager.put("Button.select", DARK_SECONDARY);
            
            // Labels - Dark Mode
            UIManager.put("Label.foreground", DARK_TEXT);
            UIManager.put("Label.font", FONT_BODY);
            UIManager.put("Label.disabledForeground", DARK_TEXT_MUTED);
            
            // Text Fields - Dark Mode
            UIManager.put("TextField.background", DARK_SURFACE);
            UIManager.put("TextField.foreground", DARK_TEXT);
            UIManager.put("TextField.border", BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(DARK_BORDER),
                BorderFactory.createEmptyBorder(10, 15, 10, 15)
            ));
            UIManager.put("TextField.font", FONT_BODY);
            UIManager.put("TextField.selectionBackground", DARK_PRIMARY);
            UIManager.put("TextField.selectionForeground", DARK_TEXT);
            UIManager.put("TextField.inactiveForeground", DARK_TEXT_MUTED);
            
            // ComboBox - Dark Mode
            UIManager.put("ComboBox.background", DARK_SURFACE);
            UIManager.put("ComboBox.foreground", DARK_TEXT);
            UIManager.put("ComboBox.border", BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(DARK_BORDER),
                BorderFactory.createEmptyBorder(6, 10, 6, 10)
            ));
            UIManager.put("ComboBox.font", FONT_BODY);
            
            // Table - Dark Mode
            UIManager.put("Table.background", DARK_SURFACE);
            UIManager.put("Table.foreground", DARK_TEXT);
            UIManager.put("Table.selectionBackground", DARK_PRIMARY);
            UIManager.put("Table.selectionForeground", DARK_TEXT);
            UIManager.put("Table.gridColor", DARK_BORDER);
            UIManager.put("TableHeader.background", DARK_CARD);
            UIManager.put("TableHeader.foreground", DARK_TEXT);
            UIManager.put("TableHeader.font", FONT_SUBHEADER);
            
            // ScrollPane - Dark Mode
            UIManager.put("ScrollPane.background", DARK_BACKGROUND);
            UIManager.put("ScrollPane.border", BorderFactory.createEmptyBorder());
            
            // TabbedPane - Dark Mode
            UIManager.put("TabbedPane.background", DARK_BACKGROUND);
            UIManager.put("TabbedPane.selected", DARK_SURFACE);
            UIManager.put("TabbedPane.foreground", DARK_TEXT);
            UIManager.put("TabbedPane.border", BorderFactory.createEmptyBorder());
            UIManager.put("TabbedPane.font", FONT_SUBHEADER);
            
            // Menu - Dark Mode
            UIManager.put("Menu.background", DARK_CARD);
            UIManager.put("Menu.foreground", DARK_TEXT);
            UIManager.put("Menu.font", FONT_BODY);
            UIManager.put("MenuItem.background", Color.WHITE);
            UIManager.put("MenuItem.foreground", DARK_TEXT);
            UIManager.put("MenuItem.font", FONT_BODY);
            
            // TitledBorder - Dark Mode
            UIManager.put("TitledBorder.titleColor", DARK_TEXT);
            UIManager.put("TitledBorder.font", FONT_SUBHEADER);
            UIManager.put("TitledBorder.border", BorderFactory.createLineBorder(DARK_BORDER));
            
            System.out.println("Tema Dark Mode aplicado com sucesso");
            
        } catch (Exception e) {
            System.err.println("Erro ao aplicar Dark Mode: " + e.getMessage());
        }
    }
    
    /**
     * Cria um botão com animação suave
     */
    public static JButton createAnimatedButton(String text, Color backgroundColor, Color textColor) {
        JButton button = new JButton(text) {
            private float alpha = 0.0f;
            private Timer animationTimer;
            
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
                
                // Aplicar transparência animada
                g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha));
                
                // Preenchimento com gradiente animado
                GradientPaint gradient = new GradientPaint(
                    0, 0, backgroundColor.brighter(),
                    0, getHeight(), backgroundColor.darker()
                );
                g2d.setPaint(gradient);
                g2d.fillRoundRect(0, 0, getWidth(), getHeight(), BORDER_RADIUS_MEDIUM, BORDER_RADIUS_MEDIUM);
                
                // Resetar alpha para texto
                g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1.0f));
                
                // Texto
                g2d.setColor(textColor);
                g2d.setFont(FONT_BUTTON);
                FontMetrics fm = g2d.getFontMetrics();
                int x = (getWidth() - fm.stringWidth(getText())) / 2;
                int y = (getHeight() - fm.getHeight()) / 2 + fm.getAscent();
                g2d.drawString(getText(), x, y);
                
                g2d.dispose();
            }
            
            // Inicializar animação
            {
                alpha = 0.0f;
                animationTimer = new Timer(20, e -> {
                    alpha += 0.05f;
                    if (alpha >= 1.0f) {
                        alpha = 1.0f;
                        animationTimer.stop();
                    }
                    repaint();
                });
                animationTimer.start();
            }
        };
        
        button.setContentAreaFilled(false);
        button.setBorderPainted(false);
        button.setFocusPainted(false);
        button.setOpaque(false);
        button.setFont(FONT_BUTTON);
        button.setForeground(textColor);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        return button;
    }
    
    /**
     * Cria um painel com efeito glass (translúcido)
     */
    public static JPanel createGlassPanel() {
        return new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                // Preenchimento com efeito glass
                g2d.setColor(new Color(255, 255, 255, 180)); // Branco semi-transparente
                g2d.fillRoundRect(0, 0, getWidth(), getHeight(), BORDER_RADIUS_LARGE, BORDER_RADIUS_LARGE);
                
                // Borda sutil
                g2d.setColor(new Color(255, 255, 255, 100));
                g2d.setStroke(new BasicStroke(1.0f));
                g2d.drawRoundRect(0, 0, getWidth(), getHeight(), BORDER_RADIUS_LARGE, BORDER_RADIUS_LARGE);
                
                g2d.dispose();
            }
        };
    }
    
    /**
     * Cria um painel com gradiente moderno
     */
    public static JPanel createGradientPanel(Color[] colors) {
        return new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                // Gradiente
                GradientPaint gradient = new GradientPaint(
                    0, 0, colors[0],
                    getWidth(), getHeight(), colors[1]
                );
                g2d.setPaint(gradient);
                g2d.fillRoundRect(0, 0, getWidth(), getHeight(), BORDER_RADIUS_MEDIUM, BORDER_RADIUS_MEDIUM);
                
                g2d.dispose();
            }
        };
    }
    
    /**
     * Cria um botão com cores pastéis e bordas arredondadas
     */
    public static JButton createPastelButton(String text, Color pastelColor, Color textColor) {
        JButton button = new JButton(text) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
                
                // Fundo com cor pastel e bordas arredondadas
                g2d.setColor(pastelColor);
                g2d.fillRoundRect(0, 0, getWidth(), getHeight(), BORDER_RADIUS_MEDIUM, BORDER_RADIUS_MEDIUM);
                
                // Borda sutil arredondada
                g2d.setColor(pastelColor.darker());
                g2d.setStroke(new BasicStroke(1.5f));
                g2d.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, BORDER_RADIUS_MEDIUM, BORDER_RADIUS_MEDIUM);
                
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
        
        // Efeito hover suave
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(pastelColor.brighter());
                button.setCursor(new Cursor(Cursor.HAND_CURSOR));
            }
            
            @Override
            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(pastelColor);
                button.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
            }
        });
        
        return button;
    }
    
    /**
     * Cria um painel de opções personalizado para JOptionPane com cores pastéis
     */
    public static int showCustomConfirmDialog(Component parent, Object message, String title, 
            String[] options, int defaultOption) {
        
        // Criar painel personalizado
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        // Mensagem
        if (message instanceof String) {
            JLabel messageLabel = new JLabel((String) message);
            messageLabel.setFont(new Font("Arial", Font.PLAIN, 14));
            panel.add(messageLabel, BorderLayout.CENTER);
        } else {
            panel.add((Component) message, BorderLayout.CENTER);
        }
        
        // Painel de botões
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));
        
        // Cores pastéis para diferentes tipos de botões
        Color[] buttonColors = {
            PASTEL_GREEN,  // Sim/OK
            PASTEL_CORAL,  // Não/Cancelar
            PASTEL_BLUE,   // Outras opções
            PASTEL_YELLOW,
            PASTEL_PURPLE
        };
        
        JButton[] buttons = new JButton[options.length];
        for (int i = 0; i < options.length; i++) {
            Color buttonColor = buttonColors[i % buttonColors.length];
            buttons[i] = createPastelButton(options[i], buttonColor, TEXT_PRIMARY);
            buttonPanel.add(buttons[i]);
        }
        
        panel.add(buttonPanel, BorderLayout.SOUTH);
        
        // Criar diálogo personalizado
        JDialog dialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(parent), title, true);
        dialog.setContentPane(panel);
        dialog.pack();
        dialog.setLocationRelativeTo(parent);
        
        // Adicionar ações aos botões
        final int[] result = {-1};
        for (int i = 0; i < buttons.length; i++) {
            final int index = i;
            buttons[i].addActionListener(e -> {
                result[0] = index;
                dialog.dispose();
            });
        }
        
        // Definir botão padrão
        if (defaultOption >= 0 && defaultOption < buttons.length) {
            dialog.getRootPane().setDefaultButton(buttons[defaultOption]);
        }
        
        dialog.setVisible(true);
        return result[0];
    }
    
    /**
     * Aplica o tema moderno à UI
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
