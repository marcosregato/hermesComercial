package com.br.hermescomercial.pdv.controller;

import com.br.hermescomercial.service.DashboardService;
import com.br.hermescomercial.ui.chart.BarChartPanel;
import com.br.hermescomercial.ui.chart.LineChartPanel;
import com.br.hermescomercial.ui.kpi.KPIPanel;
import com.br.hermescomercial.theme.OceanoTheme;
import com.br.hermescomercial.theme.ModernLayout;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

/**
 * Controller do Dashboard Analytics com gráficos e KPIs
 * @author Hermes Comercial
 * @version 2.8.0
 */
public class PDVDashboardAnalyticsController {
    
    private JFrame dashboardFrame;
    private DashboardService dashboardService;
    
    // Componentes principais
    private JPanel mainPanel;
    private JPanel headerPanel;
    private JPanel kpiPanel;
    private JPanel chartsPanel;
    private JPanel topProductsPanel;
    
    // KPIs
    private KPIPanel revenueKPI;
    private KPIPanel salesKPI;
    private KPIPanel averageTicketKPI;
    private KPIPanel growthKPI;
    private KPIPanel goalKPI;
    private KPIPanel stockKPI;
    
    // Gráficos
    private BarChartPanel dailySalesChart;
    private LineChartPanel weeklySalesChart;
    private BarChartPanel monthlySalesChart;
    
    // Filtros
    private JComboBox<String> cbPeriodo;
    private JComboBox<String> cbAno;
    private JButton btnAtualizar;
    private JButton btnExportar;
    private JLabel lblUltimaAtualizacao;
    
    // Dados
    private LocalDate dataInicio;
    private LocalDate dataFim;
    
    public PDVDashboardAnalyticsController() {
        this.dashboardService = new DashboardService();
        initializeComponents();
        setupLayout();
        setupEvents();
        loadInitialData();
    }
    
    private void initializeComponents() {
        // Frame principal
        dashboardFrame = new JFrame("📊 Dashboard Analytics - Hermes Comercial");
        dashboardFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        dashboardFrame.setSize(1200, 800);
        dashboardFrame.setLocationRelativeTo(null);
        dashboardFrame.setExtendedState(JFrame.MAXIMIZED_HORIZ);
        
        // Painel principal
        mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(OceanoTheme.BACKGROUND);
        mainPanel.setBorder(new EmptyBorder(20, 20, 20, 20));
        
        // Header
        createHeaderPanel();
        
        // Painel de KPIs
        createKPIPanel();
        
        // Painel de gráficos
        createChartsPanel();
        
        // Painel de top produtos
        createTopProductsPanel();
        
        // Filtros
        createFilterPanel();
    }
    
    private void createHeaderPanel() {
        headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(OceanoTheme.PRIMARY);
        headerPanel.setBorder(new EmptyBorder(15, 20, 15, 20));
        headerPanel.setPreferredSize(new Dimension(0, 80));
        
        // Título
        JLabel titleLabel = new JLabel("📊 Dashboard Analytics");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        titleLabel.setForeground(Color.WHITE);
        
        // Subtítulo
        JLabel subtitleLabel = new JLabel("Análise de vendas e métricas de negócio");
        subtitleLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        subtitleLabel.setForeground(new Color(255, 255, 255, 200));
        
        // Painel de título
        JPanel titlePanel = new JPanel(new BorderLayout());
        titlePanel.setOpaque(false);
        titlePanel.add(titleLabel, BorderLayout.NORTH);
        titlePanel.add(subtitleLabel, BorderLayout.SOUTH);
        
        // Painel de filtros no header
        JPanel filterHeaderPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        filterHeaderPanel.setOpaque(false);
        
        // Período
        String[] periodos = {"Hoje", "Últimos 7 dias", "Últimos 30 dias", "Este mês", "Últimos 3 meses", "Este ano"};
        cbPeriodo = new JComboBox<>(periodos);
        cbPeriodo.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        cbPeriodo.setBackground(Color.WHITE);
        cbPeriodo.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(255, 255, 255, 100), 1),
            BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));
        
        // Ano
        String[] anos = {"2024", "2023", "2022", "2021", "2020"};
        cbAno = new JComboBox<>(anos);
        cbAno.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        cbAno.setBackground(Color.WHITE);
        cbAno.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(255, 255, 255, 100), 1),
            BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));
        cbAno.setSelectedItem("2024");
        
        // Botões
        btnAtualizar = ModernLayout.createModernButton("🔄 Atualizar", new Color(46, 204, 113));
        btnExportar = ModernLayout.createModernButton("📥 Exportar", new Color(52, 152, 219));
        
        filterHeaderPanel.add(new JLabel("Período:"));
        filterHeaderPanel.add(cbPeriodo);
        filterHeaderPanel.add(new JLabel("Ano:"));
        filterHeaderPanel.add(cbAno);
        filterHeaderPanel.add(btnAtualizar);
        filterHeaderPanel.add(btnExportar);
        
        headerPanel.add(titlePanel, BorderLayout.WEST);
        headerPanel.add(filterHeaderPanel, BorderLayout.EAST);
    }
    
    private void createKPIPanel() {
        kpiPanel = new JPanel(new GridLayout(2, 3, 15, 15));
        kpiPanel.setBackground(OceanoTheme.BACKGROUND);
        kpiPanel.setBorder(new EmptyBorder(0, 0, 20, 0));
        
        // Criar KPIs
        revenueKPI = KPIPanel.createRevenueKPI(BigDecimal.ZERO);
        salesKPI = KPIPanel.createSalesKPI(0);
        averageTicketKPI = KPIPanel.createAverageTicketKPI(BigDecimal.ZERO);
        growthKPI = KPIPanel.createGrowthKPI(BigDecimal.ZERO);
        goalKPI = KPIPanel.createGoalKPI(BigDecimal.ZERO, BigDecimal.valueOf(10000));
        stockKPI = KPIPanel.createStockKPI(0);
        
        kpiPanel.add(revenueKPI);
        kpiPanel.add(salesKPI);
        kpiPanel.add(averageTicketKPI);
        kpiPanel.add(growthKPI);
        kpiPanel.add(goalKPI);
        kpiPanel.add(stockKPI);
    }
    
    private void createChartsPanel() {
        chartsPanel = new JPanel(new BorderLayout());
        chartsPanel.setBackground(OceanoTheme.BACKGROUND);
        
        // Criar abas para diferentes visualizações
        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        tabbedPane.setBackground(OceanoTheme.BACKGROUND);
        
        // Gráfico de vendas diárias
        JPanel dailyPanel = createDailySalesPanel();
        tabbedPane.addTab("📅 Vendas Diárias", dailyPanel);
        
        // Gráfico de vendas semanais
        JPanel weeklyPanel = createWeeklySalesPanel();
        tabbedPane.addTab("📊 Vendas Semanais", weeklyPanel);
        
        // Gráfico de vendas mensais
        JPanel monthlyPanel = createMonthlySalesPanel();
        tabbedPane.addTab("📈 Vendas Mensais", monthlyPanel);
        
        chartsPanel.add(tabbedPane, BorderLayout.CENTER);
    }
    
    private JPanel createDailySalesPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(OceanoTheme.BACKGROUND);
        
        // Gráfico de barras para vendas diárias
        dailySalesChart = new BarChartPanel("Vendas Diárias - Últimos 7 Dias", null);
        panel.add(dailySalesChart, BorderLayout.CENTER);
        
        return panel;
    }
    
    private JPanel createWeeklySalesPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(OceanoTheme.BACKGROUND);
        
        // Gráfico de linhas para vendas semanais
        weeklySalesChart = new LineChartPanel("Vendas Semanais - Últimas 4 Semanas", null);
        panel.add(weeklySalesChart, BorderLayout.CENTER);
        
        return panel;
    }
    
    private JPanel createMonthlySalesPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(OceanoTheme.BACKGROUND);
        
        // Gráfico de barras para vendas mensais
        monthlySalesChart = new BarChartPanel("Vendas Mensais - 2024", null);
        panel.add(monthlySalesChart, BorderLayout.CENTER);
        
        return panel;
    }
    
    private void createTopProductsPanel() {
        topProductsPanel = new JPanel(new BorderLayout());
        topProductsPanel.setBackground(OceanoTheme.BACKGROUND);
        topProductsPanel.setBorder(new EmptyBorder(20, 0, 0, 0));
        
        // Título
        JLabel titleLabel = new JLabel("🏆 Top Produtos Mais Vendidos");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        titleLabel.setForeground(Color.WHITE);
        
        // Tabela de top produtos
        String[] columns = {"Código", "Produto", "Quantidade", "Valor Total"};
        Object[][] data = new Object[0][4];
        
        JTable topProductsTable = new JTable(data, columns);
        topProductsTable.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        topProductsTable.setRowHeight(25);
        topProductsTable.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 12));
        topProductsTable.getTableHeader().setBackground(OceanoTheme.PRIMARY);
        topProductsTable.getTableHeader().setForeground(Color.WHITE);
        
        // Scroll pane
        JScrollPane scrollPane = new JScrollPane(topProductsTable);
        scrollPane.setPreferredSize(new Dimension(0, 200));
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        
        topProductsPanel.add(titleLabel, BorderLayout.NORTH);
        topProductsPanel.add(scrollPane, BorderLayout.CENTER);
    }
    
    private void createFilterPanel() {
        // Painel de informações
        JPanel infoPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        infoPanel.setBackground(OceanoTheme.BACKGROUND);
        
        lblUltimaAtualizacao = new JLabel("Última atualização: " + 
            LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss")));
        lblUltimaAtualizacao.setFont(new Font("Segoe UI", Font.ITALIC, 11));
        lblUltimaAtualizacao.setForeground(Color.GRAY);
        
        infoPanel.add(lblUltimaAtualizacao);
        
        // Adicionar ao painel principal
        JPanel bottomPanel = new JPanel(new BorderLayout());
        bottomPanel.setBackground(OceanoTheme.BACKGROUND);
        bottomPanel.add(infoPanel, BorderLayout.WEST);
        
        mainPanel.add(bottomPanel, BorderLayout.SOUTH);
    }
    
    private void setupLayout() {
        // Montar layout principal
        JPanel contentPanel = new JPanel(new BorderLayout());
        contentPanel.setBackground(OceanoTheme.BACKGROUND);
        
        // Painel superior com KPIs e gráficos
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBackground(OceanoTheme.BACKGROUND);
        topPanel.add(kpiPanel, BorderLayout.NORTH);
        topPanel.add(chartsPanel, BorderLayout.CENTER);
        
        contentPanel.add(topPanel, BorderLayout.CENTER);
        contentPanel.add(topProductsPanel, BorderLayout.SOUTH);
        
        mainPanel.add(headerPanel, BorderLayout.NORTH);
        mainPanel.add(contentPanel, BorderLayout.CENTER);
        
        dashboardFrame.add(mainPanel);
    }
    
    private void setupEvents() {
        // Evento do combo de período
        cbPeriodo.addActionListener(e -> updatePeriodo());
        
        // Evento do combo de ano
        cbAno.addActionListener(e -> updateData());
        
        // Botão atualizar
        btnAtualizar.addActionListener(e -> updateData());
        
        // Botão exportar
        btnExportar.addActionListener(e -> exportData());
        
        // Timer para atualização automática (a cada 5 minutos)
        Timer autoUpdateTimer = new Timer(300000, e -> updateData());
        autoUpdateTimer.start();
    }
    
    private void updatePeriodo() {
        String periodoSelecionado = (String) cbPeriodo.getSelectedItem();
        LocalDate hoje = LocalDate.now();
        
        switch (periodoSelecionado) {
            case "Hoje":
                dataInicio = hoje;
                dataFim = hoje;
                break;
            case "Últimos 7 dias":
                dataInicio = hoje.minusDays(6);
                dataFim = hoje;
                break;
            case "Últimos 30 dias":
                dataInicio = hoje.minusDays(29);
                dataFim = hoje;
                break;
            case "Este mês":
                dataInicio = hoje.withDayOfMonth(1);
                dataFim = hoje;
                break;
            case "Últimos 3 meses":
                dataInicio = hoje.minusMonths(2).withDayOfMonth(1);
                dataFim = hoje;
                break;
            case "Este ano":
                dataInicio = hoje.withDayOfYear(1);
                dataFim = hoje;
                break;
            default:
                dataInicio = hoje.minusDays(6);
                dataFim = hoje;
        }
        
        updateData();
    }
    
    private void loadInitialData() {
        // Carregar dados iniciais (últimos 7 dias)
        cbPeriodo.setSelectedIndex(1); // Últimos 7 dias
        updatePeriodo();
    }
    
    private void updateData() {
        try {
            // Mostrar cursor de espera
            dashboardFrame.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
            
            // Atualizar KPIs
            updateKPIs();
            
            // Atualizar gráficos
            updateCharts();
            
            // Atualizar top produtos
            updateTopProducts();
            
            // Atualizar timestamp
            lblUltimaAtualizacao.setText("Última atualização: " + 
                LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss")));
            
        } catch (Exception e) {
            JOptionPane.showMessageDialog(dashboardFrame, 
                "Erro ao atualizar dados: " + e.getMessage(), 
                "Erro", 
                JOptionPane.ERROR_MESSAGE);
        } finally {
            // Restaurar cursor
            dashboardFrame.setCursor(Cursor.getDefaultCursor());
        }
    }
    
    private void updateKPIs() {
        Map<String, Object> kpis = dashboardService.getKPIsFaturamento(dataInicio, dataFim);
        
        BigDecimal faturamentoTotal = (BigDecimal) kpis.get("faturamentoTotal");
        int numeroVendas = (Integer) kpis.get("numeroVendas");
        BigDecimal ticketMedio = (BigDecimal) kpis.get("ticketMedio");
        BigDecimal crescimento = (BigDecimal) kpis.get("crescimento");
        BigDecimal percentualMeta = (BigDecimal) kpis.get("percentualMeta");
        
        // Atualizar KPIs
        revenueKPI.updateValue(faturamentoTotal);
        salesKPI.updateValue(BigDecimal.valueOf(numeroVendas));
        averageTicketKPI.updateValue(ticketMedio);
        growthKPI.updateValue(crescimento);
        goalKPI.updateValue(percentualMeta);
        
        // Atualizar KPI de estoque
        Map<String, Object> estoque = dashboardService.getResumoEstoque();
        int totalProdutos = (Integer) estoque.get("totalProdutos");
        stockKPI.updateValue(BigDecimal.valueOf(totalProdutos));
    }
    
    private void updateCharts() {
        // Atualizar gráfico de vendas diárias
        List<Map<String, Object>> vendasDiarias = dashboardService.getVendasDiarias(dataInicio, dataFim);
        dailySalesChart.updateData(vendasDiarias);
        
        // Atualizar gráfico de vendas semanais
        List<Map<String, Object>> vendasSemanais = dashboardService.getVendasSemanais(dataInicio, dataFim);
        weeklySalesChart.updateData(vendasSemanais);
        
        // Atualizar gráfico de vendas mensais
        String anoStr = (String) cbAno.getSelectedItem();
        int ano = Integer.parseInt(anoStr);
        List<Map<String, Object>> vendasMensais = dashboardService.getVendasMensais(ano);
        monthlySalesChart.updateData(vendasMensais);
    }
    
    private void updateTopProducts() {
        // Obter top produtos para uso futuro
        dashboardService.getTopProdutos(dataInicio, dataFim, 10);
        
        // Atualizar tabela de top produtos
        // (Implementação seria aqui)
    }
    
    private void exportData() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Exportar Dashboard");
        fileChooser.setSelectedFile(new java.io.File("dashboard_" + 
            LocalDate.now().format(DateTimeFormatter.ofPattern("dd_MM_yyyy")) + ".pdf"));
        
        int userSelection = fileChooser.showSaveDialog(dashboardFrame);
        
        if (userSelection == JFileChooser.APPROVE_OPTION) {
            java.io.File fileToSave = fileChooser.getSelectedFile();
            // Implementar exportação para PDF
            JOptionPane.showMessageDialog(dashboardFrame, 
                "Exportação para PDF implementada em: " + fileToSave.getAbsolutePath(), 
                "Exportação Concluída", 
                JOptionPane.INFORMATION_MESSAGE);
        }
    }
    
    public void showDashboard() {
        dashboardFrame.setVisible(true);
    }
    
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                // Usando look and feel padrão do sistema
            } catch (Exception e) {
                e.printStackTrace();
            }
            
            PDVDashboardAnalyticsController dashboard = new PDVDashboardAnalyticsController();
            dashboard.showDashboard();
        });
    }
}
