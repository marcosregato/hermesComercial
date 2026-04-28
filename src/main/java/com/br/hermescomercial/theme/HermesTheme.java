package com.br.hermescomercial.theme;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;

/**
 * Tema Visual Padrão Oficial do Sistema Hermes Comercial PDV
 * Versão 2.3.0 - Tema Corporativo Profissional
 * 
 * Este é o tema REALMENTE utilizado nas telas do sistema,
 * baseado nas cores e padrões visuais das telas antigas.
 */
public class HermesTheme {
    
    // ==================== CORES PRINCIPAIS (PADRÃO REAL) ====================
    
    // Cores de Botões - Padrão Corporativo
    public static final Color PRIMARY_COLOR = new Color(41, 128, 185);      // Azul corporativo
    public static final Color SUCCESS_COLOR = new Color(40, 167, 69);        // Verde profissional
    public static final Color WARNING_COLOR = new Color(255, 193, 7);        // Amarelo padrão
    public static final Color DANGER_COLOR = new Color(220, 53, 69);         // Vermelho corporativo
    public static final Color SECONDARY_COLOR = new Color(149, 165, 166);   // Cinza azulado suave
    
    // Cores de Fundo - Padrão Real das Telas
    public static final Color BACKGROUND_PRIMARY = new Color(250, 250, 250);   // Cinza muito suave
    public static final Color BACKGROUND_SECONDARY = new Color(250, 252, 252); // Branco suave
    public static final Color BACKGROUND_CARD = new Color(255, 255, 255);     // Branco puro
    public static final Color BACKGROUND_DARK = new Color(33, 37, 41);         // Cinza escuro profundo
    
    // Cores de Texto - Padrão Real das Telas
    public static final Color TEXT_PRIMARY = new Color(52, 73, 94);           // Azul suave profundo
    public static final Color TEXT_SECONDARY = new Color(108, 117, 125);     // Texto secundário
    public static final Color TEXT_WHITE = new Color(255, 255, 255);         // Texto branco
    public static final Color TEXT_MUTED = new Color(173, 181, 189);         // Texto suave
    
    // Cores de Borda - Padrão Real das Telas
    public static final Color BORDER_LIGHT = new Color(189, 195, 199);         // Cinza suave
    public static final Color BORDER_DARK = new Color(127, 140, 141);         // Cinza borda suave
    public static final Color BORDER_FOCUS = new Color(108, 117, 125);         // Cinza foco
    public static final Color BORDER_HOVER = new Color(127, 140, 141);        // Cinza hover
    
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
     * Aplica o tema padrão Hermes a um botão
     */
    public static void applyButtonTheme(JButton button, Color bgColor) {
        button.setBackground(new Color(149, 165, 166)); // Cinza azulado suave (padrão)
        button.setForeground(new Color(255, 255, 255)); // Branco elegante
        button.setFont(FONT_BUTTON);
        button.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(127, 140, 141), 2), // Cinza borda suave
            BorderFactory.createEmptyBorder(12, 20, 12, 20)
        ));
        button.setFocusPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setOpaque(true);
        
        // Efeito hover elegante (padrão das telas antigas)
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(new Color(127, 140, 141)); // Cinza mais escuro suave
                button.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(new Color(108, 117, 125), 2), // Cinza ainda mais escuro
                    BorderFactory.createEmptyBorder(12, 20, 12, 20)
                ));
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(new Color(149, 165, 166)); // Cinza azulado suave
                button.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(new Color(127, 140, 141), 2), // Cinza borda suave
                    BorderFactory.createEmptyBorder(12, 20, 12, 20)
                ));
            }
        });
    }
    
    /**
     * Aplica o tema padrão Hermes a um campo de texto
     */
    public static void applyTextFieldTheme(JTextField field) {
        field.setFont(FONT_DEFAULT);
        field.setBackground(Color.WHITE);
        field.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(BORDER_LIGHT),
            BorderFactory.createEmptyBorder(5, 5, 5, 5)
        ));
    }
    
    /**
     * Aplica o tema padrão Hermes a um label
     */
    public static void applyLabelTheme(JLabel label) {
        label.setFont(FONT_DEFAULT);
        label.setForeground(TEXT_PRIMARY);
    }
    
    /**
     * Cria um painel com borda padrão
     */
    public static JPanel createBorderedPanel(String title) {
        JPanel panel = new JPanel();
        panel.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createEtchedBorder(), 
            title, 
            TitledBorder.LEFT, 
            TitledBorder.TOP, 
            FONT_HEADER, 
            TEXT_PRIMARY));
        panel.setBackground(BACKGROUND_SECONDARY);
        return panel;
    }
    
    /**
     * Cria um painel principal com fundo padrão
     */
    public static JPanel createMainPanel() {
        JPanel panel = new JPanel();
        panel.setBorder(BorderFactory.createEmptyBorder(SPACING_XL, SPACING_XL, SPACING_XL, SPACING_XL));
        panel.setBackground(BACKGROUND_PRIMARY);
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
