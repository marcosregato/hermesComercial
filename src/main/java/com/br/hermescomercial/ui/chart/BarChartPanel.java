package com.br.hermescomercial.ui.chart;

import java.awt.*;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * Gráfico de barras para o Dashboard
 * @author Hermes Comercial
 * @version 2.8.0
 */
public class BarChartPanel extends ChartPanel {
    
    private static final int BAR_WIDTH = 30;
        
    public BarChartPanel(String title, List<Map<String, Object>> data) {
        super(title, data);
    }
    
    @Override
    protected void drawChart(Graphics2D g2d) {
        if (data == null || data.isEmpty()) {
            drawNoDataMessage(g2d);
            return;
        }
        
        Rectangle chartArea = getChartArea();
        BigDecimal maxValue = getMaxValue("valor");
        List<String> labels = getXLabels("data");
        
        // Desenhar grade
        drawGrid(g2d, chartArea, 5);
        
        // Desenhar eixos
        drawYAxis(g2d, chartArea, BigDecimal.ZERO, maxValue, 5);
        drawXAxis(g2d, chartArea, labels);
        
        // Desenhar barras
        drawBars(g2d, chartArea, maxValue);
    }
    
    private void drawBars(Graphics2D g2d, Rectangle chartArea, BigDecimal maxValue) {
        int barCount = data.size();
        if (barCount == 0) return;
        
        int availableWidth = chartArea.width;
        int calculatedSpacing = (availableWidth - (BAR_WIDTH * barCount)) / (barCount + 1);
        
        for (int i = 0; i < data.size(); i++) {
            Map<String, Object> item = data.get(i);
            BigDecimal value = (BigDecimal) item.get("valor");
            
            // Calcular altura da barra
            int barHeight = (int) (value.doubleValue() / maxValue.doubleValue() * chartArea.height);
            
            // Calcular posição da barra
            int x = chartArea.x + calculatedSpacing + (i * (BAR_WIDTH + calculatedSpacing));
            int y = chartArea.y + chartArea.height - barHeight;
            
            // Desenhar barra com gradiente
            drawBar(g2d, x, y, BAR_WIDTH, barHeight, value);
        }
    }
    
    private void drawBar(Graphics2D g2d, int x, int y, int width, int height, BigDecimal value) {
        // Criar gradiente
        GradientPaint gradient = new GradientPaint(
            x, y, PRIMARY_COLOR,
            x, y + height, SECONDARY_COLOR
        );
        
        // Desenhar barra
        g2d.setPaint(gradient);
        g2d.fillRoundRect(x, y, width, height, 5, 5);
        
        // Desenhar borda
        g2d.setColor(PRIMARY_COLOR);
        g2d.setStroke(new BasicStroke(2));
        g2d.drawRoundRect(x, y, width, height, 5, 5);
        
        // Desenhar valor no topo da barra
        g2d.setColor(TEXT_COLOR);
        g2d.setFont(valueFont);
        String valueText = formatValue(value);
        FontMetrics fm = g2d.getFontMetrics();
        int textWidth = fm.stringWidth(valueText);
        int textX = x + (width - textWidth) / 2;
        int textY = y - 5;
        g2d.drawString(valueText, textX, textY);
        
        // Resetar stroke
        g2d.setStroke(new BasicStroke(1));
    }
    
    private void drawNoDataMessage(Graphics2D g2d) {
        g2d.setColor(TEXT_COLOR);
        g2d.setFont(new Font("Segoe UI", Font.ITALIC, 14));
        FontMetrics fm = g2d.getFontMetrics();
        String message = "Nenhum dado disponível";
        int messageWidth = fm.stringWidth(message);
        int messageX = (getWidth() - messageWidth) / 2;
        int messageY = getHeight() / 2;
        g2d.drawString(message, messageX, messageY);
    }
    
    /**
     * Obtém valor em determinada posição (para tooltips)
     */
    public BigDecimal getValueAtPosition(int x, int y) {
        if (data == null || data.isEmpty()) return null;
        
        Rectangle chartArea = getChartArea();
        if (!chartArea.contains(x, y)) return null;
        
        int barCount = data.size();
        int availableWidth = chartArea.width;
        int calculatedSpacing = (availableWidth - (BAR_WIDTH * barCount)) / (barCount + 1);
        
        for (int i = 0; i < data.size(); i++) {
            int barX = chartArea.x + calculatedSpacing + (i * (BAR_WIDTH + calculatedSpacing));
            
            if (x >= barX && x <= barX + BAR_WIDTH) {
                return (BigDecimal) data.get(i).get("valor");
            }
        }
        
        return null;
    }
    
    /**
     * Obtém label em determinada posição
     */
    public String getLabelAtPosition(int x, int y) {
        if (data == null || data.isEmpty()) return null;
        
        Rectangle chartArea = getChartArea();
        if (!chartArea.contains(x, y)) return null;
        
        int barCount = data.size();
        int availableWidth = chartArea.width;
        int calculatedSpacing = (availableWidth - (BAR_WIDTH * barCount)) / (barCount + 1);
        
        for (int i = 0; i < data.size(); i++) {
            int barX = chartArea.x + calculatedSpacing + (i * (BAR_WIDTH + calculatedSpacing));
            
            if (x >= barX && x <= barX + BAR_WIDTH) {
                return (String) data.get(i).get("data");
            }
        }
        
        return null;
    }
}
