package com.br.hermescomercial.theme;

import javax.swing.*;
import java.awt.*;

/**
 * Tema Visual Moderno do Sistema Hermes Comercial PDV
 * Versão 2.4.0 - Tema Moderno e Atrativo
 * 
 * Design moderno com cores vibrantes, gradientes e visual profissional
 * inspirado nas melhores interfaces contemporâneas.
 */
public class HermesTheme {
    
    // ==================== CORES PRINCIPAIS (TEMA MODERNO) ====================
    
    // Cores de Botões - Paleta Moderna Vibrante
    public static final Color PRIMARY_COLOR = new Color(59, 130, 246);        // Azul moderno (Tailwind blue-500)
    public static final Color SUCCESS_COLOR = new Color(34, 197, 94);         // Verde vibrante (Tailwind green-500)
    public static final Color WARNING_COLOR = new Color(251, 146, 60);        // Laranja moderno (Tailwind orange-400)
    public static final Color DANGER_COLOR = new Color(239, 68, 68);          // Vermelho vibrante (Tailwind red-500)
    public static final Color SECONDARY_COLOR = new Color(107, 114, 128);    // Cinza moderno (Tailwind gray-500)
    public static final Color INFO_COLOR = new Color(14, 165, 233);          // Azul info (Tailwind sky-500)
    public static final Color PURPLE_COLOR = new Color(139, 92, 246);         // Roxo moderno (Tailwind violet-500)
    
    // Cores de Fundo - Gradientes e Modernos
    public static final Color BACKGROUND_PRIMARY = new Color(248, 250, 252);   // Slate-50 (cinza muito claro)
    public static final Color BACKGROUND_SECONDARY = new Color(241, 245, 249); // Slate-100 (fundo suave)
    public static final Color BACKGROUND_CARD = new Color(255, 255, 255);     // Branco puro para cards
    public static final Color BACKGROUND_DARK = new Color(15, 23, 42);         // Slate-900 (fundo escuro moderno)
    public static final Color GRADIENT_START = new Color(99, 102, 241);      // Indigo-500
    public static final Color GRADIENT_END = new Color(139, 92, 246);        // Violet-500
    
    // Cores de Texto - Hierarquia Moderna
    public static final Color TEXT_PRIMARY = new Color(15, 23, 42);            // Slate-900 (texto principal)
    public static final Color TEXT_SECONDARY = new Color(71, 85, 105);         // Slate-700 (texto secundário)
    public static final Color TEXT_WHITE = new Color(255, 255, 255);          // Texto branco
    public static final Color TEXT_MUTED = new Color(148, 163, 184);          // Slate-400 (texto suave)
    public static final Color TEXT_ACCENT = new Color(99, 102, 241);          // Indigo-500 (texto de destaque)
    
    // Cores de Borda - Design Moderno
    public static final Color BORDER_LIGHT = new Color(226, 232, 240);         // Slate-200 (borda suave)
    public static final Color BORDER_DARK = new Color(148, 163, 184);          // Slate-400 (borda média)
    public static final Color BORDER_FOCUS = new Color(99, 102, 241);          // Indigo-500 (foco)
    public static final Color BORDER_HOVER = new Color(71, 85, 105);           // Slate-700 (hover)
    public static final Color BORDER_CARD = new Color(203, 213, 225);          // Slate-300 (borda de cards)
    
    // ==================== FONTES (PADRÃO REAL) ====================
    
    // Fontes Padrão do Sistema
    public static final Font FONT_DEFAULT = new Font("Segoe UI", Font.PLAIN, 12);
    public static final Font FONT_BOLD = new Font("Segoe UI", Font.BOLD, 12);
    public static final Font FONT_HEADER = new Font("Segoe UI", Font.BOLD, 12);
    public static final Font FONT_TITLE = new Font("Segoe UI", Font.BOLD, 22);
    public static final Font FONT_BUTTON = new Font("Segoe UI", Font.BOLD, 12);
    public static final Font FONT_MONO = new Font("Consolas", Font.PLAIN, 12);
    public static final Font FONT_SMALL = new Font("Segoe UI", Font.PLAIN, 11);
    
    // ==================== CONSTANTES DE LAYOUT ====================
    
    // Espaçamentos Padrão
    public static final int SPACING_XS = 4;
    public static final int SPACING_SM = 8;
    public static final int SPACING_MD = 12;
    public static final int SPACING_LG = 20;
    public static final int SPACING_XL = 30;
    
    // Dimensões de Componentes
    public static final int BUTTON_HEIGHT = 40;
    public static final int FIELD_HEIGHT = 30;
    public static final int TABLE_ROW_HEIGHT = 25;
    public static final int BORDER_RADIUS = 8;
    
    // ==================== MÉTODOS UTILITÁRIOS ====================
    
    /**
     * Aplica tema moderno a um botão com sombras e efeitos
     */
    public static void applyButtonTheme(JButton button, Color bgColor) {
        button.setBackground(bgColor);
        button.setForeground(TEXT_WHITE);
        button.setFont(FONT_BUTTON);
        button.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(BORDER_CARD, 1),
            BorderFactory.createEmptyBorder(12, 24, 12, 24)
        ));
        button.setFocusPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setOpaque(true);
        
        // Efeitos modernos de hover e foco
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(bgColor.brighter());
                button.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(BORDER_FOCUS, 2),
                    BorderFactory.createEmptyBorder(12, 24, 12, 24)
                ));
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(bgColor);
                button.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(BORDER_CARD, 1),
                    BorderFactory.createEmptyBorder(12, 24, 12, 24)
                ));
            }
        });
    }
    
    /**
     * Cria um botão moderno com estilo aprimorado
     */
    public static JButton createModernButton(String text, Color bgColor) {
        JButton button = new JButton(text);
        applyButtonTheme(button, bgColor);
        return button;
    }
    
    /**
     * Aplica tema moderno a um campo de texto
     */
    public static void applyTextFieldTheme(JTextField field) {
        field.setFont(FONT_DEFAULT);
        field.setBackground(BACKGROUND_CARD);
        field.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(BORDER_LIGHT, 1),
            BorderFactory.createEmptyBorder(8, 12, 8, 12)
        ));
        field.setForeground(TEXT_PRIMARY);
    }
    
    /**
     * Aplica tema moderno a um label
     */
    public static void applyLabelTheme(JLabel label) {
        label.setFont(FONT_DEFAULT);
        label.setForeground(TEXT_PRIMARY);
    }
    
    /**
     * Cria um card moderno com sombra e bordas suaves
     */
    public static JPanel createModernCard() {
        JPanel panel = new JPanel();
        panel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(BORDER_CARD, 1),
            BorderFactory.createEmptyBorder(SPACING_LG, SPACING_LG, SPACING_LG, SPACING_LG)
        ));
        panel.setBackground(BACKGROUND_CARD);
        return panel;
    }
    
    /**
     * Cria um painel principal com fundo moderno
     */
    public static JPanel createMainPanel() {
        JPanel panel = new JPanel();
        panel.setBorder(BorderFactory.createEmptyBorder(SPACING_XL, SPACING_XL, SPACING_XL, SPACING_XL));
        panel.setBackground(BACKGROUND_PRIMARY);
        return panel;
    }
    
    /**
     * Cria um painel com gradiente moderno
     */
    public static JPanel createGradientPanel() {
        JPanel panel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                GradientPaint gradient = new GradientPaint(
                    0, 0, GRADIENT_START,
                    getWidth(), getHeight(), GRADIENT_END
                );
                g2d.setPaint(gradient);
                g2d.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        panel.setOpaque(true);
        return panel;
    }
    
    /**
     * Aplica o tema padrão a uma tabela
     */
    public static void applyTableTheme(JTable table) {
        table.setFont(FONT_DEFAULT);
        table.setRowHeight(TABLE_ROW_HEIGHT);
        table.getTableHeader().setFont(FONT_HEADER);
        table.getTableHeader().setBackground(PRIMARY_COLOR);
        table.getTableHeader().setForeground(TEXT_WHITE);
    }
    
    /**
     * Aplica o tema padrão a um JComboBox
     */
    public static void applyComboBoxTheme(JComboBox<?> comboBox) {
        comboBox.setFont(FONT_DEFAULT);
        comboBox.setBackground(Color.WHITE);
    }
    
    /**
     * Retorna a cor padrão para botões baseada no tipo
     */
    public static Color getButtonColor(String type) {
        switch (type.toLowerCase()) {
            case "primary":
            case "blue":
                return PRIMARY_COLOR;
            case "success":
            case "green":
                return SUCCESS_COLOR;
            case "warning":
            case "yellow":
                return WARNING_COLOR;
            case "danger":
            case "red":
                return DANGER_COLOR;
            case "secondary":
            case "gray":
            default:
                return SECONDARY_COLOR;
        }
    }
}
