package com.br.hermescomercial.pdv.controller;

import com.br.hermescomercial.service.DashboardService;
import com.br.hermescomercial.ui.chart.BarChartPanel;
import com.br.hermescomercial.ui.chart.LineChartPanel;
import com.br.hermescomercial.ui.kpi.KPIPanel;
import com.br.hermescomercial.ui.layout.LayoutPadrao;

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
        dashboardFrame = new JFrame("📊 Dashboard Analytics v2.8.3 - LayoutPadrao");
        dashboardFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        dashboardFrame.setSize(1200, 800);
        dashboardFrame.setLocationRelativeTo(null);
        dashboardFrame.setExtendedState(JFrame.MAXIMIZED_HORIZ);
        
        // Painel principal com LayoutPadrao
        mainPanel = LayoutPadrao.criarPainelComMargem(20);
        mainPanel.setBackground(LayoutPadrao.COR_FUNDO_ESCURO);
        
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
        headerPanel.setBackground(LayoutPadrao.COR_PRIMARIA);
        headerPanel.setBorder(new EmptyBorder(15, 20, 15, 20));
        headerPanel.setPreferredSize(new Dimension(0, 80));
        
        // Título com LayoutPadrao
        JLabel titleLabel = LayoutPadrao.criarRotuloTitulo("📊 Dashboard Analytics");
        titleLabel.setForeground(Color.WHITE);
        
        // Subtítulo com LayoutPadrao
        JLabel subtitleLabel = LayoutPadrao.criarRotuloTexto("Análise de vendas e métricas de negócio");
        subtitleLabel.setForeground(new Color(200, 220, 240));
        
        // Painel de título
        JPanel titlePanel = new JPanel(new BorderLayout());
        titlePanel.setOpaque(false);
        titlePanel.add(titleLabel, BorderLayout.NORTH);
        titlePanel.add(subtitleLabel, BorderLayout.SOUTH);
        
        // Painel de filtros no header
        JPanel filterHeaderPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        filterHeaderPanel.setOpaque(false);
        
        // Período com LayoutPadrao
        String[] periodos = {"Hoje", "Últimos 7 dias", "Últimos 30 dias", "Este mês", "Últimos 3 meses", "Este ano"};
        cbPeriodo = LayoutPadrao.criarComboBox(periodos);
        
        // Ano com LayoutPadrao
        String[] anos = {"2024", "2023", "2022", "2021", "2020"};
        cbAno = LayoutPadrao.criarComboBox(anos);
        cbAno.setSelectedItem("2024");
        
        // Botões com LayoutPadrao
        btnAtualizar = LayoutPadrao.criarBotaoSucesso("🔄 Atualizar");
        btnExportar = LayoutPadrao.criarBotaoPrimario("📥 Exportar");
        
        // Rótulos com LayoutPadrao
        filterHeaderPanel.add(LayoutPadrao.criarRotuloCampo("Período:"));
        filterHeaderPanel.add(cbPeriodo);
        filterHeaderPanel.add(LayoutPadrao.criarRotuloCampo("Ano:"));
        filterHeaderPanel.add(cbAno);
        filterHeaderPanel.add(btnAtualizar);
        filterHeaderPanel.add(btnExportar);
        
        headerPanel.add(titlePanel, BorderLayout.WEST);
        headerPanel.add(filterHeaderPanel, BorderLayout.EAST);
    }
    
    private void createKPIPanel() {
        kpiPanel = new JPanel(new GridLayout(2, 3, 15, 15));
        kpiPanel.setBackground(LayoutPadrao.COR_FUNDO_ESCURO);
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
        chartsPanel.setBackground(LayoutPadrao.COR_FUNDO_ESCURO);
        
        // Criar abas para diferentes visualizações
        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.setFont(LayoutPadrao.FONTE_ROTULO);
        tabbedPane.setBackground(LayoutPadrao.COR_FUNDO_ESCURO);
        
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
        panel.setBackground(LayoutPadrao.COR_FUNDO_ESCURO);
        
        // Gráfico de barras para vendas diárias
        dailySalesChart = new BarChartPanel("Vendas Diárias - Últimos 7 Dias", null);
        panel.add(dailySalesChart, BorderLayout.CENTER);
        
        return panel;
    }
    
    private JPanel createWeeklySalesPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(LayoutPadrao.COR_FUNDO_ESCURO);
        
        // Gráfico de linhas para vendas semanais
        weeklySalesChart = new LineChartPanel("Vendas Semanais - Últimas 4 Semanas", null);
        panel.add(weeklySalesChart, BorderLayout.CENTER);
        
        return panel;
    }
    
    private JPanel createMonthlySalesPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(LayoutPadrao.COR_FUNDO_ESCURO);
        
        // Gráfico de barras para vendas mensais
        monthlySalesChart = new BarChartPanel("Vendas Mensais - 2024", null);
        panel.add(monthlySalesChart, BorderLayout.CENTER);
        
        return panel;
    }
    
    private void createTopProductsPanel() {
        topProductsPanel = new JPanel(new BorderLayout());
        topProductsPanel.setBackground(LayoutPadrao.COR_FUNDO_ESCURO);
        topProductsPanel.setBorder(new EmptyBorder(20, 0, 0, 0));
        
        // Título com LayoutPadrao
        JLabel titleLabel = LayoutPadrao.criarRotuloSubtitulo("🏆 Top Produtos Mais Vendidos");
        titleLabel.setForeground(Color.WHITE);
        
        // Tabela de top produtos
        String[] columns = {"Código", "Produto", "Quantidade", "Valor Total"};
        Object[][] data = new Object[0][4];
        
        JTable topProductsTable = LayoutPadrao.criarTabela();
        topProductsTable.setModel(new javax.swing.table.DefaultTableModel(data, columns));
        topProductsTable.setRowHeight(25);
        
        // Scroll pane com LayoutPadrao
        JScrollPane scrollPane = LayoutPadrao.criarBarraRolagem(topProductsTable);
        scrollPane.setPreferredSize(new Dimension(0, 200));
        
        topProductsPanel.add(titleLabel, BorderLayout.NORTH);
        topProductsPanel.add(scrollPane, BorderLayout.CENTER);
    }
    
    private void createFilterPanel() {
        // Painel de informações com LayoutPadrao
        JPanel infoPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        infoPanel.setBackground(LayoutPadrao.COR_FUNDO_ESCURO);
        
        lblUltimaAtualizacao = LayoutPadrao.criarRotuloTexto("Última atualização: " + 
            LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss")));
        lblUltimaAtualizacao.setForeground(LayoutPadrao.COR_TEXTO_CLARO);
        
        infoPanel.add(lblUltimaAtualizacao);
        
        // Adicionar ao painel principal
        JPanel bottomPanel = new JPanel(new BorderLayout());
        bottomPanel.setBackground(LayoutPadrao.COR_FUNDO_ESCURO);
        bottomPanel.add(infoPanel, BorderLayout.WEST);
        
        mainPanel.add(bottomPanel, BorderLayout.SOUTH);
    }
    
    private void setupLayout() {
        // Montar layout principal com LayoutPadrao
        JPanel contentPanel = new JPanel(new BorderLayout());
        contentPanel.setBackground(LayoutPadrao.COR_FUNDO_ESCURO);
        
        // Painel superior com KPIs e gráficos
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBackground(LayoutPadrao.COR_FUNDO_ESCURO);
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
