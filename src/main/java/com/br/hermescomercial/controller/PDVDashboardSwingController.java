package com.br.hermescomercial.controller;

import com.br.hermescomercial.model.DashboardMetric;
import com.br.hermescomercial.service.DashboardService;
import com.br.hermescomercial.theme.ModernTheme;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.List;

/**
 * Controller para tela de dashboard analítico
 * Versão 2.3.0 - Dashboard com KPIs e gráficos
 */
public class PDVDashboardSwingController {
    
    private JFrame frame;
    private JPanel mainPanel;
    private JPanel kpisPanel;
    private JPanel graficosPanel;
    private JPanel resumoPanel;
    private DashboardService dashboardService;
    private DecimalFormat currencyFormat;
    private DecimalFormat percentFormat;
    
    public PDVDashboardSwingController() {
        this.dashboardService = new DashboardService();
        this.currencyFormat = new DecimalFormat("R$ #,##0.00");
        this.percentFormat = new DecimalFormat("#,##0.00%");
        
        // Inicializar tabela e gerar dados de exemplo
        try {
            dashboardService.inicializarTabela();
            // Tentar gerar métricas apenas se a tabela foi criada com sucesso
            try {
                dashboardService.gerarMetricasExemplo();
            } catch (Exception metricEx) {
                System.err.println("Aviso: Não foi possível gerar métricas de exemplo: " + metricEx.getMessage());
                // Continuar mesmo sem métricas de exemplo
            }
        } catch (Exception e) {
            System.err.println("Erro ao inicializar dashboard: " + e.getMessage());
            // Continuar mesmo com erro de inicialização
        }
        
        initializeUI();
        atualizarDashboard();
    }
    
    private void initializeUI() {
        // Aplicar tema moderno
        ModernTheme.applyModernTheme();
        
        frame = new JFrame("📊 Dashboard Analítico - Hermes Comercial PDV");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setSize(1200, 800);
        frame.setLocationRelativeTo(null);
        
        // Configurar fundo
        frame.getContentPane().setBackground(ModernTheme.BACKGROUND_PRIMARY);
        
        createMainPanel();
        frame.add(mainPanel);
    }
    
    private void createMainPanel() {
        mainPanel = new JPanel(new BorderLayout());
        mainPanel.setOpaque(false);
        
        // Header
        createHeaderPanel();
        
        // Painel central com abas
        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.setFont(new Font("Segoe UI", Font.BOLD, 12));
        tabbedPane.setBackground(ModernTheme.BACKGROUND_PRIMARY);
        
        // Aba KPIs
        createKPIsPanel();
        tabbedPane.addTab("📈 KPIs Principais", kpisPanel);
        
        // Aba Gráficos
        createGraficosPanel();
        tabbedPane.addTab("📊 Gráficos", graficosPanel);
        
        // Aba Resumo
        createResumoPanel();
        tabbedPane.addTab("💰 Resumo Financeiro", resumoPanel);
        
        mainPanel.add(tabbedPane, BorderLayout.CENTER);
    }
    
    private void createHeaderPanel() {
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setOpaque(false);
        headerPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 10, 20));
        
        // Título
        JLabel titleLabel = new JLabel("📊 Dashboard Analítico");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 28));
        titleLabel.setForeground(ModernTheme.TEXT_PRIMARY);
        
        // Botão atualizar
        JButton btnAtualizar = createModernButton("🔄 Atualizar", ModernTheme.PRIMARY_COLOR);
        btnAtualizar.addActionListener(e -> atualizarDashboard());
        
        // Data de atualização
        JLabel dataLabel = new JLabel("Última atualização: " + java.time.LocalDateTime.now().format(
            java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")));
        dataLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        dataLabel.setForeground(ModernTheme.TEXT_SECONDARY);
        
        JPanel rightPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        rightPanel.setOpaque(false);
        rightPanel.add(dataLabel);
        rightPanel.add(Box.createHorizontalStrut(10));
        rightPanel.add(btnAtualizar);
        
        headerPanel.add(titleLabel, BorderLayout.WEST);
        headerPanel.add(rightPanel, BorderLayout.EAST);
        
        mainPanel.add(headerPanel, BorderLayout.NORTH);
    }
    
    private void createKPIsPanel() {
        kpisPanel = new JPanel(new GridBagLayout());
        kpisPanel.setOpaque(false);
        kpisPanel.setBorder(new EmptyBorder(20, 20, 20, 20));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
    }
    
    private void createGraficosPanel() {
        graficosPanel = new JPanel(new BorderLayout());
        graficosPanel.setOpaque(false);
        graficosPanel.setBorder(new EmptyBorder(20, 20, 20, 20));
        
        // Placeholder para gráficos
        JLabel placeholderLabel = new JLabel("📊 Gráficos em desenvolvimento", SwingConstants.CENTER);
        placeholderLabel.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        placeholderLabel.setForeground(ModernTheme.TEXT_SECONDARY);
        
        graficosPanel.add(placeholderLabel, BorderLayout.CENTER);
    }
    
    private void createResumoPanel() {
        resumoPanel = new JPanel(new GridBagLayout());
        resumoPanel.setOpaque(false);
        resumoPanel.setBorder(new EmptyBorder(20, 20, 20, 20));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
    }
    
    private void atualizarDashboard() {
        try {
            // Limpar painéis
            kpisPanel.removeAll();
            resumoPanel.removeAll();
            
            // Carregar KPIs
            List<DashboardMetric> kpis = dashboardService.gerarKPIs();
            GridBagConstraints gbc = new GridBagConstraints();
            gbc.insets = new Insets(10, 10, 10, 10);
            gbc.fill = GridBagConstraints.BOTH;
            gbc.weightx = 1.0;
            gbc.weighty = 1.0;
            
            int col = 0, row = 0;
            for (DashboardMetric kpi : kpis) {
                gbc.gridx = col;
                gbc.gridy = row;
                kpisPanel.add(createKPICard(kpi), gbc);
                
                col++;
                if (col >= 3) {
                    col = 0;
                    row++;
                }
            }
            
            // Carregar resumo financeiro
            List<DashboardMetric> resumo = dashboardService.getResumoFinanceiro();
            gbc.gridx = 0;
            gbc.gridy = 0;
            gbc.gridwidth = 2;
            resumoPanel.add(createResumoCard(resumo), gbc);
            
            // Adicionar métricas calculadas
            gbc.gridy = 1;
            resumoPanel.add(createMetricasCalculadasCard(), gbc);
            
            kpisPanel.revalidate();
            kpisPanel.repaint();
            resumoPanel.revalidate();
            resumoPanel.repaint();
            
        } catch (Exception e) {
            JOptionPane.showMessageDialog(frame, "Erro ao atualizar dashboard: " + e.getMessage(), 
                                          "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private JPanel createKPICard(DashboardMetric metric) {
        JPanel card = new JPanel(new BorderLayout());
        card.setBackground(ModernTheme.BACKGROUND_CARD);
        card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(ModernTheme.BORDER_LIGHT, 1),
            new EmptyBorder(15, 15, 15, 15)
        ));
        
        // Título
        JLabel titleLabel = new JLabel(metric.getNome());
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        titleLabel.setForeground(ModernTheme.TEXT_PRIMARY);
        
        // Valor
        JLabel valorLabel = new JLabel(metric.getValorFormatado());
        valorLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        valorLabel.setForeground(metric.isPositiva() ? ModernTheme.SUCCESS_COLOR : ModernTheme.DANGER_COLOR);
        
        // Variação
        JLabel variacaoLabel = new JLabel(metric.getVariacaoFormatada());
        variacaoLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        variacaoLabel.setForeground(metric.isPositiva() ? ModernTheme.SUCCESS_COLOR : ModernTheme.DANGER_COLOR);
        
        // Status
        JLabel statusLabel = new JLabel(metric.getStatus().getDescricao());
        statusLabel.setFont(new Font("Segoe UI", Font.PLAIN, 10));
        statusLabel.setForeground(metric.getStatus().getCor());
        
        // Painel superior
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setOpaque(false);
        topPanel.add(titleLabel, BorderLayout.WEST);
        topPanel.add(variacaoLabel, BorderLayout.EAST);
        
        // Painel central
        JPanel centerPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        centerPanel.setOpaque(false);
        centerPanel.add(valorLabel);
        
        // Painel inferior
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        bottomPanel.setOpaque(false);
        bottomPanel.add(statusLabel);
        
        card.add(topPanel, BorderLayout.NORTH);
        card.add(centerPanel, BorderLayout.CENTER);
        card.add(bottomPanel, BorderLayout.SOUTH);
        
        // Efeito hover
        card.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                card.setBackground(ModernTheme.BACKGROUND_HOVER);
            }
            
            @Override
            public void mouseExited(MouseEvent e) {
                card.setBackground(ModernTheme.BACKGROUND_CARD);
            }
        });
        
        return card;
    }
    
    private JPanel createResumoCard(List<DashboardMetric> resumo) {
        JPanel card = new JPanel(new GridBagLayout());
        card.setBackground(ModernTheme.BACKGROUND_CARD);
        card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(ModernTheme.BORDER_LIGHT, 1),
            new EmptyBorder(15, 15, 15, 15)
        ));
        
        card.setBorder(new TitledBorder(
            BorderFactory.createLineBorder(ModernTheme.BORDER_LIGHT, 1),
            "💰 Resumo Financeiro Mensal",
            TitledBorder.LEFT,
            TitledBorder.TOP,
            new Font("Segoe UI", Font.BOLD, 14),
            ModernTheme.TEXT_PRIMARY
        ));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;
        
        int row = 0;
        for (DashboardMetric metric : resumo) {
            gbc.gridy = row;
            gbc.gridx = 0;
            JLabel nameLabel = new JLabel(metric.getNome() + ":");
            nameLabel.setFont(new Font("Segoe UI", Font.BOLD, 12));
            nameLabel.setForeground(ModernTheme.TEXT_PRIMARY);
            card.add(nameLabel, gbc);
            
            gbc.gridx = 1;
            JLabel valueLabel = new JLabel(metric.getValorFormatado());
            valueLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
            valueLabel.setForeground(ModernTheme.TEXT_SECONDARY);
            card.add(valueLabel, gbc);
            
            row++;
        }
        
        return card;
    }
    
    private JPanel createMetricasCalculadasCard() {
        JPanel card = new JPanel(new GridBagLayout());
        card.setBackground(ModernTheme.BACKGROUND_CARD);
        card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(ModernTheme.BORDER_LIGHT, 1),
            new EmptyBorder(15, 15, 15, 15)
        ));
        
        card.setBorder(new TitledBorder(
            BorderFactory.createLineBorder(ModernTheme.BORDER_LIGHT, 1),
            "📊 Métricas Calculadas",
            TitledBorder.LEFT,
            TitledBorder.TOP,
            new Font("Segoe UI", Font.BOLD, 14),
            ModernTheme.TEXT_PRIMARY
        ));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;
        
        // Ticket Médio
        BigDecimal ticketMedio = dashboardService.calcularTicketMedio();
        gbc.gridy = 0;
        gbc.gridx = 0;
        JLabel ticketLabel = new JLabel("Ticket Médio:");
        ticketLabel.setFont(new Font("Segoe UI", Font.BOLD, 12));
        ticketLabel.setForeground(ModernTheme.TEXT_PRIMARY);
        card.add(ticketLabel, gbc);
        
        gbc.gridx = 1;
        JLabel ticketValueLabel = new JLabel(currencyFormat.format(ticketMedio));
        ticketValueLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        ticketValueLabel.setForeground(ModernTheme.TEXT_SECONDARY);
        card.add(ticketValueLabel, gbc);
        
        // Margem de Lucro
        BigDecimal margemLucro = dashboardService.calcularMargemLucro();
        gbc.gridy = 1;
        gbc.gridx = 0;
        JLabel margemLabel = new JLabel("Margem de Lucro:");
        margemLabel.setFont(new Font("Segoe UI", Font.BOLD, 12));
        margemLabel.setForeground(ModernTheme.TEXT_PRIMARY);
        card.add(margemLabel, gbc);
        
        gbc.gridx = 1;
        JLabel margemValueLabel = new JLabel(percentFormat.format(margemLucro.divide(new BigDecimal("100"))));
        margemValueLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        margemValueLabel.setForeground(margemLucro.compareTo(BigDecimal.ZERO) >= 0 ? 
                                     ModernTheme.SUCCESS_COLOR : ModernTheme.DANGER_COLOR);
        card.add(margemValueLabel, gbc);
        
        return card;
    }
    
    private JButton createModernButton(String text, Color bgColor) {
        JButton button = new JButton(text);
        button.setFont(new Font("Segoe UI", Font.BOLD, 12));
        button.setBackground(bgColor);
        button.setForeground(Color.WHITE);
        button.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        button.setFocusPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        // Efeito hover
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(bgColor.brighter());
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(bgColor);
            }
        });
        
        return button;
    }
    
    public void show() {
        frame.setVisible(true);
    }
    
    // Método estático para fácil acesso
    public static void mostrarDashboard() {
        SwingUtilities.invokeLater(() -> {
            PDVDashboardSwingController controller = new PDVDashboardSwingController();
            controller.show();
        });
    }
}
