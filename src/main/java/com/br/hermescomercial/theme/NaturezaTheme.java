package com.br.hermescomercial.theme;

import java.awt.Color;
import java.awt.Font;
import javax.swing.BorderFactory;
import javax.swing.UIManager;

/**
 * Tema Natureza - Verde Floresta
 * Paleta de cores orgânica e agradável para o sistema PDV/ERP
 * Ideal para produtos naturais, farmácias, lojas de alimentos
 * 
 * @version 2.8.0
 * @author Hermes Comercial
 */
public class NaturezaTheme {
    
    // === CORES PRIMÁRIAS ===
    public static final Color PRIMARY_DARK = new Color(39, 174, 96);      // Verde floresta escuro
    public static final Color PRIMARY = new Color(46, 204, 113);           // Verde floresta principal
    public static final Color PRIMARY_LIGHT = new Color(82, 190, 128);    // Verde floresta claro
    
    // === CORES SECUNDÁRIAS ===
    public static final Color SECONDARY = new Color(248, 249, 250);        // Bege claro
    public static final Color SECONDARY_DARK = new Color(236, 240, 241);   // Bege médio
    public static final Color ACCENT = new Color(230, 126, 34);            // Laranja para destaque
    
    // === CORES NEUTRAS ===
    public static final Color BACKGROUND = new Color(248, 249, 250);       // Fundo bege claro
    public static final Color BACKGROUND_DARK = new Color(236, 240, 241);  // Fundo bege médio
    public static final Color SURFACE = new Color(255, 255, 255);         // Branco para cards
    public static final Color TEXT_PRIMARY = new Color(44, 62, 80);        // Texto principal (cinza escuro)
    public static final Color TEXT_SECONDARY = new Color(127, 140, 141);  // Texto secundário
    public static final Color TEXT_DISABLED = new Color(189, 195, 199);   // Texto desabilitado
    
    // === CORES DE STATUS ===
    public static final Color SUCCESS = new Color(39, 174, 96);            // Verde para sucesso
    public static final Color WARNING = new Color(243, 156, 18);           // Laranja para alerta
    public static final Color ERROR = new Color(231, 76, 60);              // Vermelho para erro
    public static final Color INFO = new Color(52, 152, 219);              // Azul para informação
    
    // === CORES PARA TABELAS ===
    public static final Color TABLE_HEADER = new Color(46, 204, 113);      // Cabeçalho verde
    public static final Color TABLE_ROW_EVEN = new Color(255, 255, 255);    // Linhas pares brancas
    public static final Color TABLE_ROW_ODD = new Color(248, 249, 250);     // Linhas ímpares bege
    public static final Color TABLE_SELECTED = new Color(82, 190, 128);     // Seleção verde claro
    public static final Color TABLE_GRID = new Color(236, 240, 241);        // Grade bege
    
    // === CORES PARA BOTÕES ===
    public static final Color BUTTON_PRIMARY = new Color(46, 204, 113);     // Botão principal verde
    public static final Color BUTTON_HOVER = new Color(39, 174, 96);        // Hover verde escuro
    public static final Color BUTTON_SECONDARY = new Color(248, 249, 250);  // Botão secundário bege
    public static final Color BUTTON_DANGER = new Color(231, 76, 60);       // Botão perigo vermelho
    
    // === FONTES ===
    public static final Font FONT_TITLE = new Font("Segoe UI", Font.BOLD, 24);
    public static final Font FONT_HEADER = new Font("Segoe UI", Font.BOLD, 16);
    public static final Font FONT_BODY = new Font("Segoe UI", Font.PLAIN, 14);
    public static final Font FONT_SMALL = new Font("Segoe UI", Font.PLAIN, 12);
    public static final Font FONT_BUTTON = new Font("Segoe UI", Font.BOLD, 14);
    
    // === BORDAS ===
    public static final javax.swing.border.Border BORDER_DEFAULT = 
        BorderFactory.createLineBorder(new Color(236, 240, 241), 1);
    public static final javax.swing.border.Border BORDER_FOCUSED = 
        BorderFactory.createLineBorder(new Color(46, 204, 113), 2);
    public static final javax.swing.border.Border BORDER_ERROR = 
        BorderFactory.createLineBorder(new Color(231, 76, 60), 2);
    
    /**
     * Aplica o tema Natureza ao Look and Feel do sistema
     */
    public static void applyTheme() {
        try {
            // Configurar UIManager com cores do tema
            UIManager.put("Panel.background", BACKGROUND);
            UIManager.put("OptionPane.background", BACKGROUND);
            UIManager.put("OptionPane.messageForeground", TEXT_PRIMARY);
            UIManager.put("OptionPane.buttonBackground", BUTTON_PRIMARY);
            UIManager.put("OptionPane.buttonForeground", Color.WHITE);
            
            // Tabelas
            UIManager.put("Table.background", TABLE_ROW_EVEN);
            UIManager.put("Table.foreground", TEXT_PRIMARY);
            UIManager.put("Table.selectionBackground", TABLE_SELECTED);
            UIManager.put("Table.selectionForeground", Color.WHITE);
            UIManager.put("Table.gridColor", TABLE_GRID);
            UIManager.put("TableHeader.background", TABLE_HEADER);
            UIManager.put("TableHeader.foreground", Color.WHITE);
            
            // Botões
            UIManager.put("Button.background", BUTTON_PRIMARY);
            UIManager.put("Button.foreground", Color.WHITE);
            UIManager.put("Button.select", BUTTON_HOVER);
            UIManager.put("Button.focus", PRIMARY);
            
            // Campos de texto
            UIManager.put("TextField.background", Color.WHITE);
            UIManager.put("TextField.foreground", TEXT_PRIMARY);
            UIManager.put("TextField.border", BORDER_DEFAULT);
            UIManager.put("TextField.selectionBackground", PRIMARY_LIGHT);
            
            // Labels
            UIManager.put("Label.foreground", TEXT_PRIMARY);
            
            // Menus
            UIManager.put("Menu.background", BACKGROUND);
            UIManager.put("Menu.foreground", TEXT_PRIMARY);
            UIManager.put("MenuItem.background", BACKGROUND);
            UIManager.put("MenuItem.foreground", TEXT_PRIMARY);
            UIManager.put("MenuItem.selectionBackground", PRIMARY_LIGHT);
            
            System.out.println("🌿 Tema Natureza (Verde Floresta) aplicado com sucesso!");
            
        } catch (Exception e) {
            System.err.println("❌ Erro ao aplicar tema Natureza: " + e.getMessage());
        }
    }
    
    /**
     * Cria um botão com estilo do tema Natureza
     */
    public static javax.swing.JButton createNaturezaButton(String text, Color backgroundColor) {
        javax.swing.JButton button = new javax.swing.JButton(text);
        button.setBackground(backgroundColor);
        button.setForeground(backgroundColor == BUTTON_SECONDARY ? TEXT_PRIMARY : Color.WHITE);
        button.setFont(FONT_BUTTON);
        button.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(backgroundColor, 2),
            BorderFactory.createEmptyBorder(10, 20, 10, 20)
        ));
        button.setFocusPainted(false);
        button.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        
        // Efeito hover
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(backgroundColor.brighter());
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(backgroundColor);
            }
        });
        
        return button;
    }
    
    /**
     * Cria um painel com estilo do tema Natureza
     */
    public static javax.swing.JPanel createNaturezaPanel() {
        javax.swing.JPanel panel = new javax.swing.JPanel();
        panel.setBackground(BACKGROUND);
        panel.setBorder(BORDER_DEFAULT);
        return panel;
    }
    
    /**
     * Cria um campo de texto com estilo do tema Natureza
     */
    public static javax.swing.JTextField createNaturezaTextField() {
        javax.swing.JTextField textField = new javax.swing.JTextField();
        textField.setBackground(Color.WHITE);
        textField.setForeground(TEXT_PRIMARY);
        textField.setFont(FONT_BODY);
        textField.setBorder(BORDER_DEFAULT);
        // textField.setFocusBorder(BORDER_FOCUSED); // Removido por compatibilidade
        return textField;
    }
    
    /**
     * Cria uma label com estilo do tema Natureza
     */
    public static javax.swing.JLabel createNaturezaLabel(String text, int style) {
        javax.swing.JLabel label = new javax.swing.JLabel(text);
        label.setForeground(TEXT_PRIMARY);
        label.setFont(style == Font.BOLD ? FONT_HEADER : FONT_BODY);
        return label;
    }
    
    /**
     * Retorna informações sobre o tema
     */
    public static String getThemeInfo() {
        return """
        🌿 TEMA NATUREZA - VERDE FLORESTA
        ==================================
        • Cores orgânicas e agradáveis
        • Ideal para produtos naturais e alimentos
        • Design limpo e profissional
        • Alta legibilidade e acessibilidade
        • Paleta: Verde floresta + Bege + Laranja
        """;
    }
}
