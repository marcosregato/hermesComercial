package com.br.hermescomercial.ui.chart;

import javax.swing.*;
import java.awt.*;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.NumberFormat;
import java.util.Locale;
import java.util.List;
import java.util.Map;

/**
 * Painel de gráfico genérico para o Dashboard
 * @author Hermes Comercial
 * @version 2.8.0
 */
public abstract class ChartPanel extends JPanel {
    
    protected static final Color PRIMARY_COLOR = new Color(26, 188, 156); // Azul Turquesa
    protected static final Color SECONDARY_COLOR = new Color(52, 152, 219); // Azul
    protected static final Color SUCCESS_COLOR = new Color(46, 204, 113); // Verde
    protected static final Color WARNING_COLOR = new Color(241, 196, 15); // Amarelo
    protected static final Color DANGER_COLOR = new Color(231, 76, 60); // Vermelho
    protected static final Color BACKGROUND_COLOR = new Color(245, 247, 250);
    protected static final Color GRID_COLOR = new Color(200, 200, 200);
    protected static final Color TEXT_COLOR = new Color(52, 73, 94);
    
    protected String title;
    protected List<Map<String, Object>> data;
    protected Font titleFont = new Font("Segoe UI", Font.BOLD, 14);
    protected Font labelFont = new Font("Segoe UI", Font.PLAIN, 11);
    protected Font valueFont = new Font("Segoe UI", Font.BOLD, 12);
    protected DecimalFormat currencyFormat = new DecimalFormat("R$ #,##0.00");
    
    public ChartPanel(String title, List<Map<String, Object>> data) {
        this.title = title;
        this.data = data;
        setBackground(BACKGROUND_COLOR);
        setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
    }
    
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g.create();
        
        // Habilitar anti-aliasing
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        
        // Desenhar título
        drawTitle(g2d);
        
        // Desenhar gráfico
        drawChart(g2d);
        
        g2d.dispose();
    }
    
    protected void drawTitle(Graphics2D g2d) {
        g2d.setColor(TEXT_COLOR);
        g2d.setFont(titleFont);
        FontMetrics fm = g2d.getFontMetrics();
        int titleWidth = fm.stringWidth(title);
        int titleX = (getWidth() - titleWidth) / 2;
        g2d.drawString(title, titleX, 20);
    }
    
    protected abstract void drawChart(Graphics2D g2d);
    
    /**
     * Desenha grade no gráfico
     */
    protected void drawGrid(Graphics2D g2d, Rectangle chartArea, int horizontalLines) {
        g2d.setColor(GRID_COLOR);
        g2d.setStroke(new BasicStroke(1, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL, 0, new float[]{2, 2}, 0));
        
        // Linhas horizontais
        for (int i = 0; i <= horizontalLines; i++) {
            int y = chartArea.y + (chartArea.height * i / horizontalLines);
            g2d.drawLine(chartArea.x, y, chartArea.x + chartArea.width, y);
        }
        
        g2d.setStroke(new BasicStroke(1));
    }
    
    /**
     * Desenha eixo Y com valores
     */
    protected void drawYAxis(Graphics2D g2d, Rectangle chartArea, BigDecimal minValue, BigDecimal maxValue, int labels) {
        g2d.setColor(TEXT_COLOR);
        g2d.setFont(labelFont);
        
        BigDecimal range = maxValue.subtract(minValue);
        BigDecimal stepSize = range.divide(BigDecimal.valueOf(labels - 1), 2, RoundingMode.HALF_UP);
        
        for (int i = 0; i <= labels; i++) {
            BigDecimal value = minValue.add(stepSize.multiply(BigDecimal.valueOf(i)));
            String label = formatValue(value);
            
            int y = chartArea.y + chartArea.height - (chartArea.height * i / labels);
            g2d.drawString(label, 5, y + 5);
        }
    }
    
    /**
     * Desenha eixo X com labels
     */
    protected void drawXAxis(Graphics2D g2d, Rectangle chartArea, List<String> labels) {
        g2d.setColor(TEXT_COLOR);
        g2d.setFont(labelFont);
        
        int labelSpacing = chartArea.width / labels.size();
        for (int i = 0; i < labels.size(); i++) {
            String label = labels.get(i);
            int x = chartArea.x + (labelSpacing * i) + (labelSpacing / 2);
            int y = chartArea.y + chartArea.height + 15;
            
            // Centralizar label
            FontMetrics fm = g2d.getFontMetrics();
            int labelWidth = fm.stringWidth(label);
            g2d.drawString(label, x - (labelWidth / 2), y);
        }
    }
    
    /**
     * Formata valor para exibição
     */
    protected String formatValue(BigDecimal value) {
        if (value.compareTo(BigDecimal.valueOf(1000)) < 0) {
            return currencyFormat.format(value);
        } else if (value.compareTo(BigDecimal.valueOf(1000000)) < 0) {
            return "R$ " + value.divide(BigDecimal.valueOf(1000), 1, RoundingMode.HALF_UP) + "K";
        } else {
            return "R$ " + value.divide(BigDecimal.valueOf(1000000), 1, RoundingMode.HALF_UP) + "M";
        }
    }
    
    /**
     * Obtém valor máximo dos dados
     */
    protected BigDecimal getMaxValue(String valueKey) {
        BigDecimal max = BigDecimal.ZERO;
        for (Map<String, Object> item : data) {
            Object value = item.get(valueKey);
            if (value instanceof BigDecimal) {
                BigDecimal decimalValue = (BigDecimal) value;
                if (decimalValue.compareTo(max) > 0) {
                    max = decimalValue;
                }
            }
        }
        // Adicionar margem de 10%
        return max.multiply(BigDecimal.valueOf(1.1));
    }
    
    /**
     * Obtém labels do eixo X
     */
    protected List<String> getXLabels(String labelKey) {
        return data.stream()
            .map(item -> (String) item.get(labelKey))
            .collect(java.util.stream.Collectors.toList());
    }
    
    /**
     * Desenha tooltip
     */
    protected void drawTooltip(Graphics2D g2d, int x, int y, String text) {
        FontMetrics fm = g2d.getFontMetrics();
        int textWidth = fm.stringWidth(text);
        int textHeight = fm.getHeight();
        
        int padding = 8;
        int tooltipWidth = textWidth + (padding * 2);
        int tooltipHeight = textHeight + (padding * 2);
        
        // Ajustar posição para não sair da tela
        int tooltipX = Math.min(x, getWidth() - tooltipWidth - 10);
        int tooltipY = Math.max(y - tooltipHeight - 10, 10);
        
        // Desenhar fundo
        g2d.setColor(new Color(0, 0, 0, 200));
        g2d.fillRoundRect(tooltipX, tooltipY, tooltipWidth, tooltipHeight, 5, 5);
        
        // Desenhar texto
        g2d.setColor(Color.WHITE);
        g2d.setFont(labelFont);
        g2d.drawString(text, tooltipX + padding, tooltipY + padding + fm.getAscent());
    }
    
    /**
     * Atualiza dados do gráfico
     */
    public void updateData(List<Map<String, Object>> newData) {
        this.data = newData;
        repaint();
    }
    
    /**
     * Obtém área do gráfico (excluindo margens)
     */
    protected Rectangle getChartArea() {
        int margin = 40;
        int topMargin = 40;
        int bottomMargin = 60;
        
        return new Rectangle(
            margin,
            topMargin,
            getWidth() - (margin * 2),
            getHeight() - topMargin - bottomMargin
        );
    }
}
