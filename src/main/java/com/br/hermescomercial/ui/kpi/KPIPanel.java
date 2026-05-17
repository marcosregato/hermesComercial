package com.br.hermescomercial.ui.kpi;

import javax.swing.*;
import java.awt.*;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;

/**
 * Painel de KPI (Key Performance Indicator) para o Dashboard
 * @author Hermes Comercial
 * @version 2.8.0
 */
public class KPIPanel extends JPanel {
    
    private static final Color BACKGROUND_COLOR = new Color(255, 255, 255);
    private static final Color PRIMARY_COLOR = new Color(26, 188, 156); // Azul Turquesa
    private static final Color SUCCESS_COLOR = new Color(46, 204, 113); // Verde
    private static final Color WARNING_COLOR = new Color(241, 196, 15); // Amarelo
    private static final Color DANGER_COLOR = new Color(231, 76, 60); // Vermelho
    private static final Color TEXT_COLOR = new Color(52, 73, 94);
    private static final Color SUBTITLE_COLOR = new Color(127, 140, 141);
    
    private String title;
    private String subtitle;
    private BigDecimal value;
    private KPIType type;
    
    private Font titleFont = new Font("Segoe UI", Font.BOLD, 14);
    private Font valueFont = new Font("Segoe UI", Font.BOLD, 24);
    private Font subtitleFont = new Font("Segoe UI", Font.PLAIN, 11);
    private DecimalFormat currencyFormat = new DecimalFormat("R$ #,##0.00");
    private DecimalFormat percentFormat = new DecimalFormat("#,##0.0");
    private DecimalFormat numberFormat = new DecimalFormat("#,##0");
    
    public enum KPIType {
        CURRENCY, PERCENT, NUMBER, GROWTH
    }
    
    public KPIPanel(String title, String subtitle, BigDecimal value, KPIType type) {
        this.title = title;
        this.subtitle = subtitle;
        this.value = value;
        this.type = type;
        
        setBackground(BACKGROUND_COLOR);
        setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        setPreferredSize(new Dimension(200, 120));
    }
    
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g.create();
        
        // Habilitar anti-aliasing
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        
        // Desenhar fundo com sombra
        drawBackground(g2d);
        
        // Desenhar conteúdo
        drawContent(g2d);
        
        g2d.dispose();
    }
    
    private void drawBackground(Graphics2D g2d) {
        // Desenhar sombra
        g2d.setColor(new Color(0, 0, 0, 30));
        g2d.fillRoundRect(3, 3, getWidth() - 6, getHeight() - 6, 10, 10);
        
        // Desenhar fundo principal
        g2d.setColor(BACKGROUND_COLOR);
        g2d.fillRoundRect(0, 0, getWidth() - 6, getHeight() - 6, 10, 10);
        
        // Desenhar borda
        g2d.setColor(new Color(200, 200, 200));
        g2d.setStroke(new BasicStroke(1));
        g2d.drawRoundRect(0, 0, getWidth() - 6, getHeight() - 6, 10, 10);
    }
    
    private void drawContent(Graphics2D g2d) {
        int padding = 15;
        int contentWidth = getWidth() - (padding * 2);
        
        // Desenhar título
        g2d.setColor(TEXT_COLOR);
        g2d.setFont(titleFont);
        g2d.drawString(title, padding, 25);
        
        // Desenhar valor principal
        String valueText = formatValue();
        g2d.setColor(getValueColor());
        g2d.setFont(valueFont);
        FontMetrics valueFm = g2d.getFontMetrics();
        int valueWidth = valueFm.stringWidth(valueText);
        int valueX = padding + (contentWidth - valueWidth) / 2;
        g2d.drawString(valueText, valueX, 55);
        
        // Desenhar subtítulo
        if (subtitle != null && !subtitle.isEmpty()) {
            g2d.setColor(SUBTITLE_COLOR);
            g2d.setFont(subtitleFont);
            FontMetrics subtitleFm = g2d.getFontMetrics();
            int subtitleWidth = subtitleFm.stringWidth(subtitle);
            int subtitleX = padding + (contentWidth - subtitleWidth) / 2;
            g2d.drawString(subtitle, subtitleX, 75);
        }
        
        // Desenhar indicador de tendência (se for crescimento)
        if (type == KPIType.GROWTH) {
            drawTrendIndicator(g2d);
        }
    }
    
    private void drawTrendIndicator(Graphics2D g2d) {
        int iconSize = 16;
        int iconX = getWidth() - 15 - iconSize - 5;
        int iconY = 20;
        
        // Desenhar seta para cima ou para baixo
        if (value.compareTo(BigDecimal.ZERO) > 0) {
            // Seta para cima (crescimento positivo)
            drawUpArrow(g2d, iconX, iconY, iconSize, SUCCESS_COLOR);
        } else if (value.compareTo(BigDecimal.ZERO) < 0) {
            // Seta para baixo (crescimento negativo)
            drawDownArrow(g2d, iconX, iconY, iconSize, DANGER_COLOR);
        } else {
            // Linha horizontal (estável)
            drawHorizontalLine(g2d, iconX, iconY, iconSize, WARNING_COLOR);
        }
    }
    
    private void drawUpArrow(Graphics2D g2d, int x, int y, int size, Color color) {
        g2d.setColor(color);
        g2d.setStroke(new BasicStroke(2));
        
        // Desenhar seta
        Polygon arrow = new Polygon();
        arrow.addPoint(x + size/2, y);
        arrow.addPoint(x + size, y + size/2);
        arrow.addPoint(x + size/2, y + size);
        arrow.addPoint(x + size/2, y + size/3);
        arrow.addPoint(x, y + size/2);
        arrow.addPoint(x + size/2, y + size/3);
        
        g2d.fillPolygon(arrow);
    }
    
    private void drawDownArrow(Graphics2D g2d, int x, int y, int size, Color color) {
        g2d.setColor(color);
        g2d.setStroke(new BasicStroke(2));
        
        // Desenhar seta para baixo
        Polygon arrow = new Polygon();
        arrow.addPoint(x + size/2, y + size);
        arrow.addPoint(x + size, y + size/2);
        arrow.addPoint(x + size/2, y);
        arrow.addPoint(x + size/2, y + 2*size/3);
        arrow.addPoint(x, y + size/2);
        arrow.addPoint(x + size/2, y + 2*size/3);
        
        g2d.fillPolygon(arrow);
    }
    
    private void drawHorizontalLine(Graphics2D g2d, int x, int y, int size, Color color) {
        g2d.setColor(color);
        g2d.setStroke(new BasicStroke(2));
        g2d.drawLine(x, y + size/2, x + size, y + size/2);
    }
    
    private String formatValue() {
        switch (type) {
            case CURRENCY:
                return currencyFormat.format(value);
            case PERCENT:
                return percentFormat.format(value) + "%";
            case NUMBER:
                return numberFormat.format(value);
            case GROWTH:
                return percentFormat.format(value) + "%";
            default:
                return value.toString();
        }
    }
    
    private Color getValueColor() {
        switch (type) {
            case CURRENCY:
                return PRIMARY_COLOR;
            case GROWTH:
                if (value.compareTo(BigDecimal.ZERO) > 0) {
                    return SUCCESS_COLOR;
                } else if (value.compareTo(BigDecimal.ZERO) < 0) {
                    return DANGER_COLOR;
                } else {
                    return WARNING_COLOR;
                }
            default:
                return TEXT_COLOR;
        }
    }
    
    /**
     * Atualiza valores do KPI
     */
    public void updateValue(BigDecimal newValue) {
        this.value = newValue;
        repaint();
    }
    
    /**
     * Atualiza subtítulo
     */
    public void updateSubtitle(String newSubtitle) {
        this.subtitle = newSubtitle;
        repaint();
    }
    
    /**
     * Cria KPI de faturamento
     */
    public static KPIPanel createRevenueKPI(BigDecimal value) {
        return new KPIPanel("Faturamento", "Total do período", value, KPIType.CURRENCY);
    }
    
    /**
     * Cria KPI de vendas
     */
    public static KPIPanel createSalesKPI(int value) {
        return new KPIPanel("Vendas", "Total de vendas", BigDecimal.valueOf(value), KPIType.NUMBER);
    }
    
    /**
     * Cria KPI de ticket médio
     */
    public static KPIPanel createAverageTicketKPI(BigDecimal value) {
        return new KPIPanel("Ticket Médio", "Valor médio por venda", value, KPIType.CURRENCY);
    }
    
    /**
     * Cria KPI de crescimento
     */
    public static KPIPanel createGrowthKPI(BigDecimal value) {
        String subtitle = value.compareTo(BigDecimal.ZERO) >= 0 ? "vs. período anterior" : "vs. período anterior";
        return new KPIPanel("Crescimento", subtitle, value, KPIType.GROWTH);
    }
    
    /**
     * Cria KPI de meta
     */
    public static KPIPanel createGoalKPI(BigDecimal current, BigDecimal goal) {
        BigDecimal percentage = goal.compareTo(BigDecimal.ZERO) > 0 ? 
            current.divide(goal, 2, RoundingMode.HALF_UP).multiply(BigDecimal.valueOf(100)) : 
            BigDecimal.ZERO;
        
        String subtitle = "Meta: " + new DecimalFormat("R$ #,##0.00").format(goal);
        return new KPIPanel("% da Meta", subtitle, percentage, KPIType.PERCENT);
    }
    
    /**
     * Cria KPI de estoque
     */
    public static KPIPanel createStockKPI(int value) {
        return new KPIPanel("Estoque", "Produtos em estoque", BigDecimal.valueOf(value), KPIType.NUMBER);
    }
    
    /**
     * Cria KPI de alertas
     */
    public static KPIPanel createAlertKPI(int value) {
        return new KPIPanel("Alertas", "Estoque baixo", BigDecimal.valueOf(value), KPIType.NUMBER);
    }
}
