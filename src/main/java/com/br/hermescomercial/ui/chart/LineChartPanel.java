package com.br.hermescomercial.ui.chart;

import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * Gráfico de linhas para o Dashboard
 * @author Hermes Comercial
 * @version 2.8.0
 */
public class LineChartPanel extends ChartPanel {
    
    private static final int POINT_RADIUS = 4;
    private static final int LINE_WIDTH = 2;
    
    public LineChartPanel(String title, List<Map<String, Object>> data) {
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
        
        // Desenhar linha
        drawLine(g2d, chartArea, maxValue);
        
        // Desenhar pontos
        drawPoints(g2d, chartArea, maxValue);
    }
    
    private void drawLine(Graphics2D g2d, Rectangle chartArea, BigDecimal maxValue) {
        if (data.size() < 2) return;
        
        g2d.setColor(PRIMARY_COLOR);
        g2d.setStroke(new BasicStroke(LINE_WIDTH, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
        
        int pointSpacing = chartArea.width / (data.size() - 1);
        
        for (int i = 0; i < data.size() - 1; i++) {
            Map<String, Object> current = data.get(i);
            Map<String, Object> next = data.get(i + 1);
            
            BigDecimal currentValue = (BigDecimal) current.get("valor");
            BigDecimal nextValue = (BigDecimal) next.get("valor");
            
            int x1 = chartArea.x + (i * pointSpacing);
            int y1 = chartArea.y + chartArea.height - 
                     (int) (currentValue.doubleValue() / maxValue.doubleValue() * chartArea.height);
            
            int x2 = chartArea.x + ((i + 1) * pointSpacing);
            int y2 = chartArea.y + chartArea.height - 
                     (int) (nextValue.doubleValue() / maxValue.doubleValue() * chartArea.height);
            
            // Desenhar linha com gradiente
            GradientPaint gradient = new GradientPaint(
                x1, y1, PRIMARY_COLOR,
                x2, y2, SECONDARY_COLOR
            );
            g2d.setPaint(gradient);
            
            Line2D line = new Line2D.Double(x1, y1, x2, y2);
            g2d.draw(line);
        }
        
        g2d.setStroke(new BasicStroke(1));
    }
    
    private void drawPoints(Graphics2D g2d, Rectangle chartArea, BigDecimal maxValue) {
        int pointSpacing = data.size() > 1 ? chartArea.width / (data.size() - 1) : 0;
        
        for (int i = 0; i < data.size(); i++) {
            Map<String, Object> item = data.get(i);
            BigDecimal value = (BigDecimal) item.get("valor");
            
            int x = chartArea.x + (i * pointSpacing);
            int y = chartArea.y + chartArea.height - 
                     (int) (value.doubleValue() / maxValue.doubleValue() * chartArea.height);
            
            // Desenhar ponto com sombra
            drawPoint(g2d, x, y, value);
        }
    }
    
    private void drawPoint(Graphics2D g2d, int x, int y, BigDecimal value) {
        // Desenhar sombra
        g2d.setColor(new Color(0, 0, 0, 50));
        Ellipse2D shadow = new Ellipse2D.Double(
            x - POINT_RADIUS + 2, 
            y - POINT_RADIUS + 2, 
            POINT_RADIUS * 2, 
            POINT_RADIUS * 2
        );
        g2d.fill(shadow);
        
        // Desenhar ponto principal
        g2d.setColor(PRIMARY_COLOR);
        Ellipse2D point = new Ellipse2D.Double(
            x - POINT_RADIUS, 
            y - POINT_RADIUS, 
            POINT_RADIUS * 2, 
            POINT_RADIUS * 2
        );
        g2d.fill(point);
        
        // Desenhar borda do ponto
        g2d.setColor(Color.WHITE);
        g2d.setStroke(new BasicStroke(2));
        g2d.draw(point);
        
        // Desenhar valor acima do ponto
        g2d.setColor(TEXT_COLOR);
        g2d.setFont(valueFont);
        String valueText = formatValue(value);
        FontMetrics fm = g2d.getFontMetrics();
        int textWidth = fm.stringWidth(valueText);
        int textX = x - (textWidth / 2);
        int textY = y - POINT_RADIUS - 5;
        g2d.drawString(valueText, textX, textY);
        
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
        
        int pointSpacing = data.size() > 1 ? chartArea.width / (data.size() - 1) : 0;
        
        for (int i = 0; i < data.size(); i++) {
            int pointX = chartArea.x + (i * pointSpacing);
            
            // Verificar se está próximo do ponto
            int distance = Math.abs(x - pointX);
            if (distance <= POINT_RADIUS + 5) {
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
        
        int pointSpacing = data.size() > 1 ? chartArea.width / (data.size() - 1) : 0;
        
        for (int i = 0; i < data.size(); i++) {
            int pointX = chartArea.x + (i * pointSpacing);
            
            // Verificar se está próximo do ponto
            int distance = Math.abs(x - pointX);
            if (distance <= POINT_RADIUS + 5) {
                return (String) data.get(i).get("data");
            }
        }
        
        return null;
    }
    
    /**
     * Desenha área preenchida sob a linha
     */
    protected void drawArea(Graphics2D g2d, Rectangle chartArea, BigDecimal maxValue) {
        if (data.size() < 2) return;
        
        int pointSpacing = chartArea.width / (data.size() - 1);
        
        // Criar caminho para área
        Polygon area = new Polygon();
        
        // Adicionar pontos da linha
        for (int i = 0; i < data.size(); i++) {
            Map<String, Object> item = data.get(i);
            BigDecimal value = (BigDecimal) item.get("valor");
            
            int x = chartArea.x + (i * pointSpacing);
            int y = chartArea.y + chartArea.height - 
                     (int) (value.doubleValue() / maxValue.doubleValue() * chartArea.height);
            
            area.addPoint(x, y);
        }
        
        // Adicionar pontos inferiores para fechar a área
        area.addPoint(chartArea.x + chartArea.width, chartArea.y + chartArea.height);
        area.addPoint(chartArea.x, chartArea.y + chartArea.height);
        
        // Desenhar área com gradiente transparente
        GradientPaint gradient = new GradientPaint(
            chartArea.x, chartArea.y, new Color(PRIMARY_COLOR.getRed(), PRIMARY_COLOR.getGreen(), PRIMARY_COLOR.getBlue(), 100),
            chartArea.x, chartArea.y + chartArea.height, new Color(PRIMARY_COLOR.getRed(), PRIMARY_COLOR.getGreen(), PRIMARY_COLOR.getBlue(), 20)
        );
        
        g2d.setPaint(gradient);
        g2d.fill(area);
    }
}
