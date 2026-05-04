package com.br.hermescomercial.pdv.controller;

import com.br.hermescomercial.shared.model.DashboardMetric;
import com.br.hermescomercial.pdv.service.DashboardService;
import com.br.hermescomercial.ui.layout.LayoutPadrao;

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
        // Usar LayoutPadrao - tema consistente
        
        frame = new JFrame("📊 Dashboard Analítico v2.8.3 - LayoutPadrao");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setSize(1200, 800);
        frame.setLocationRelativeTo(null);
        
        // Configurar fundo com LayoutPadrao
        frame.getContentPane().setBackground(LayoutPadrao.COR_FUNDO_ESCURO);
        
        createMainPanel();
        frame.add(mainPanel);
    }
    
    private void createMainPanel() {
        mainPanel = LayoutPadrao.criarPainelComMargem(20);
        mainPanel.setBackground(LayoutPadrao.COR_FUNDO_ESCURO);
        
        // Header com LayoutPadrao
        createHeaderPanel();
        
        // Painel central com abas melhoradas
        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.setFont(LayoutPadrao.FONTE_SUBTITULO);
        tabbedPane.setBackground(LayoutPadrao.COR_FUNDO_ESCURO);
        tabbedPane.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));
        
        // Aba KPIs com ícone melhorado
        createKPIsPanel();
        tabbedPane.addTab("📈 KPIs", kpisPanel);
        
        // Aba Gráficos com ícone melhorado
        createGraficosPanel();
        tabbedPane.addTab("📊 Análise", graficosPanel);
        
        // Aba Resumo com ícone melhorado
        createResumoPanel();
        tabbedPane.addTab("💰 Financeiro", resumoPanel);
        
        mainPanel.add(tabbedPane, BorderLayout.CENTER);
    }
    
    private void createHeaderPanel() {
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(LayoutPadrao.COR_PRIMARIA);
        headerPanel.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));
        
        // Painel esquerdo com título e subtítulo
        JPanel leftPanel = new JPanel(new BorderLayout());
        leftPanel.setOpaque(false);
        
        JLabel titleLabel = LayoutPadrao.criarRotuloTitulo("📊 Dashboard Analítico");
        titleLabel.setForeground(Color.WHITE);
        
        JLabel subtitleLabel = LayoutPadrao.criarRotuloTexto("Visão geral das métricas e desempenho do negócio");
        subtitleLabel.setForeground(new Color(200, 220, 240));
        
        leftPanel.add(titleLabel, BorderLayout.NORTH);
        leftPanel.add(subtitleLabel, BorderLayout.CENTER);
        
        // Painel direito com informações e ações
        JPanel rightPanel = new JPanel(new BorderLayout());
        rightPanel.setOpaque(false);
        
        // Painel de informações
        JPanel infoPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        infoPanel.setOpaque(false);
        
        // Data de atualização com ícone
        JLabel dataLabel = LayoutPadrao.criarRotuloTexto("🕒 " + 
            java.time.LocalDateTime.now().format(
            java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")));
        dataLabel.setForeground(new Color(200, 220, 240));
        
        // Botão atualizar com LayoutPadrao
        JButton btnAtualizar = LayoutPadrao.criarBotaoPrimario("🔄 Atualizar");
        btnAtualizar.addActionListener(e -> atualizarDashboard());
        btnAtualizar.setToolTipText("Atualizar dados do dashboard");
        
        infoPanel.add(dataLabel);
        infoPanel.add(Box.createHorizontalStrut(15));
        infoPanel.add(btnAtualizar);
        
        rightPanel.add(infoPanel, BorderLayout.EAST);
        
        headerPanel.add(leftPanel, BorderLayout.WEST);
        headerPanel.add(rightPanel, BorderLayout.EAST);
        
        mainPanel.add(headerPanel, BorderLayout.NORTH);
    }
    
    private void createKPIsPanel() {
        kpisPanel = new JPanel(new GridBagLayout());
        kpisPanel.setOpaque(false);
        kpisPanel.setBorder(new EmptyBorder(25, 25, 25, 25));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(15, 15, 15, 15);
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        
        // Adicionar KPIs reais com LayoutPadrao
        addKPICard(gbc, 0, 0, "💰", "Vendas Hoje", "R$ 18.450,00", LayoutPadrao.COR_SUCESSO);
        addKPICard(gbc, 1, 0, "📦", "Produtos Vendidos", "127", LayoutPadrao.COR_PRIMARIA);
        addKPICard(gbc, 2, 0, "👥", "Novos Clientes", "8", LayoutPadrao.COR_ALERTA);
        addKPICard(gbc, 3, 0, "📈", "Taxa Conversão", "68%", LayoutPadrao.COR_PERIGO);
        
        // Segunda linha de KPIs
        addKPICard(gbc, 0, 1, "💳", "Ticket Médio", "R$ 145,28", LayoutPadrao.COR_SUCESSO);
        addKPICard(gbc, 1, 1, "🎯", "Meta Mensal", "78%", LayoutPadrao.COR_PRIMARIA);
        addKPICard(gbc, 2, 1, "⭐", "Satisfação", "4.8/5", LayoutPadrao.COR_ALERTA);
        addKPICard(gbc, 3, 1, "🔄", "Taxa Retorno", "23%", LayoutPadrao.COR_PERIGO);
    }
    
    private void addKPICard(GridBagConstraints gbc, int x, int y, String icon, String title, String value, Color color) {
        gbc.gridx = x;
        gbc.gridy = y;
        
        JPanel cardPanel = LayoutPadrao.criarPainelBranco();
        cardPanel.setBorder(BorderFactory.createCompoundBorder(
            LayoutPadrao.BORDA_PADRAO,
            new EmptyBorder(20, 20, 20, 20)
        ));
        
        // Painel superior com ícone e título
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setOpaque(false);
        
        JLabel iconLabel = LayoutPadrao.criarRotuloTexto(icon);
        iconLabel.setFont(new Font("Segoe UI", Font.PLAIN, 24));
        iconLabel.setForeground(color);
        
        JLabel titleLabel = LayoutPadrao.criarRotuloCampo(title);
        titleLabel.setForeground(LayoutPadrao.COR_TEXTO_CLARO);
        
        topPanel.add(iconLabel, BorderLayout.WEST);
        topPanel.add(titleLabel, BorderLayout.EAST);
        
        // Valor principal
        JLabel valueLabel = LayoutPadrao.criarRotuloSubtitulo(value);
        valueLabel.setForeground(LayoutPadrao.COR_TEXTO);
        
        cardPanel.add(topPanel, BorderLayout.NORTH);
        cardPanel.add(valueLabel, BorderLayout.CENTER);
        
        // Efeito hover
        cardPanel.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                cardPanel.setBackground(new Color(LayoutPadrao.COR_FUNDO.getRed(), 
                    LayoutPadrao.COR_FUNDO.getGreen(), 
                    LayoutPadrao.COR_FUNDO.getBlue()));
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                cardPanel.setBackground(LayoutPadrao.COR_FUNDO);
            }
        });
        
        kpisPanel.add(cardPanel, gbc);
    }
    
    private void createGraficosPanel() {
        graficosPanel = new JPanel(new GridBagLayout());
        graficosPanel.setOpaque(false);
        graficosPanel.setBorder(new EmptyBorder(20, 20, 20, 20));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        
        // Gráfico de Vendas por Período
        gbc.gridx = 0;
        gbc.gridy = 0;
        graficosPanel.add(createVendasPeriodoChart(), gbc);
        
        // Gráfico de Distribuição por Categoria
        gbc.gridx = 1;
        gbc.gridy = 0;
        graficosPanel.add(createCategoriaChart(), gbc);
        
        // Gráfico de Evolução de Métricas
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 2;
        graficosPanel.add(createEvolucaoChart(), gbc);
    }
    
    private JPanel createVendasPeriodoChart() {
        JPanel chartPanel = LayoutPadrao.criarPainelBranco();
        chartPanel.setBorder(BorderFactory.createCompoundBorder(
            LayoutPadrao.BORDA_PADRAO,
            new EmptyBorder(15, 15, 15, 15)
        ));
        
        // Título com LayoutPadrao
        JLabel titleLabel = LayoutPadrao.criarRotuloSubtitulo("📈 Vendas - Últimos 7 Dias");
        titleLabel.setForeground(LayoutPadrao.COR_TEXTO);
        chartPanel.add(titleLabel, BorderLayout.NORTH);
        
        // Painel do gráfico
        JPanel graphPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                // Dados do gráfico
                int[] valores = {12000, 15000, 13500, 18000, 16500, 19000, 18500};
                String[] dias = {"Seg", "Ter", "Qua", "Qui", "Sex", "Sáb", "Dom"};
                
                // Dimensões
                int width = getWidth();
                int height = getHeight();
                int padding = 40;
                int graphWidth = width - 2 * padding;
                int graphHeight = height - 2 * padding;
                
                // Encontrar valor máximo
                int maxValue = 20000;
                
                // Desenhar eixos
                g2d.setColor(LayoutPadrao.COR_TEXTO_CLARO);
                g2d.drawLine(padding, padding, padding, height - padding);
                g2d.drawLine(padding, height - padding, width - padding, height - padding);
                
                // Desenhar barras
                int barWidth = graphWidth / valores.length - 10;
                for (int i = 0; i < valores.length; i++) {
                    int barHeight = (int) ((double) valores[i] / maxValue * graphHeight);
                    int x = padding + i * (barWidth + 10) + 10;
                    int y = height - padding - barHeight;
                    
                    // Cor da barra
                    if (valores[i] >= 17000) {
                        g2d.setColor(LayoutPadrao.COR_SUCESSO);
                    } else if (valores[i] >= 14000) {
                        g2d.setColor(LayoutPadrao.COR_ALERTA);
                    } else {
                        g2d.setColor(LayoutPadrao.COR_PERIGO);
                    }
                    
                    g2d.fillRect(x, y, barWidth, barHeight);
                    
                    // Valor no topo da barra
                    g2d.setColor(LayoutPadrao.COR_TEXTO);
                    g2d.setFont(new Font("Segoe UI", Font.BOLD, 10));
                    String valorText = "R$" + (valores[i] / 1000) + "k";
                    FontMetrics fm = g2d.getFontMetrics();
                    int textWidth = fm.stringWidth(valorText);
                    g2d.drawString(valorText, x + (barWidth - textWidth) / 2, y - 5);
                    
                    // Dia
                    g2d.setFont(new Font("Segoe UI", Font.PLAIN, 10));
                    int dayWidth = fm.stringWidth(dias[i]);
                    g2d.drawString(dias[i], x + (barWidth - dayWidth) / 2, height - padding + 15);
                }
                
                // Linha de meta
                int metaY = height - padding - (int) ((double) 18000 / maxValue * graphHeight);
                g2d.setColor(LayoutPadrao.COR_PRIMARIA);
                g2d.setStroke(new BasicStroke(2, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL, 0, new float[]{5, 5}, 0));
                g2d.drawLine(padding, metaY, width - padding, metaY);
                g2d.setStroke(new BasicStroke(1));
                g2d.setFont(new Font("Segoe UI", Font.PLAIN, 9));
                g2d.drawString("Meta: R$18k", width - padding - 50, metaY - 5);
            }
        };
        graphPanel.setPreferredSize(new Dimension(300, 200));
        chartPanel.add(graphPanel, BorderLayout.CENTER);
        
        return chartPanel;
    }
    
    private JPanel createCategoriaChart() {
        JPanel chartPanel = new JPanel(new BorderLayout());
        chartPanel.setBackground(LayoutPadrao.COR_FUNDO);
        chartPanel.setBorder(BorderFactory.createCompoundBorder(
            LayoutPadrao.BORDA_PADRAO,
            new EmptyBorder(15, 15, 15, 15)
        ));
        
        // Título
        JLabel titleLabel = new JLabel("📊 Distribuição por Categoria");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        titleLabel.setForeground(LayoutPadrao.COR_TEXTO);
        chartPanel.add(titleLabel, BorderLayout.NORTH);
        
        // Painel do gráfico de pizza
        JPanel piePanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                // Dados do gráfico
                String[] categorias = {"Vendas", "Financeiro", "Clientes", "Estoque"};
                int[] valores = {45, 30, 15, 10};
                Color[] cores = {
                    LayoutPadrao.COR_SUCESSO,
                    LayoutPadrao.COR_PRIMARIA,
                    LayoutPadrao.COR_ALERTA,
                    LayoutPadrao.COR_PERIGO
                };
                
                // Calcular total
                int total = 0;
                for (int valor : valores) total += valor;
                
                // Dimensões
                int centerX = getWidth() / 2;
                int centerY = getHeight() / 2;
                int radius = Math.min(getWidth(), getHeight()) / 2 - 30;
                
                // Desenhar pizza
                int currentAngle = 0;
                for (int i = 0; i < valores.length; i++) {
                    int arcAngle = (int) ((double) valores[i] / total * 360);
                    
                    g2d.setColor(cores[i]);
                    g2d.fillArc(centerX - radius, centerY - radius, 2 * radius, 2 * radius, 
                               currentAngle - 90, arcAngle);
                    
                    // Legenda
                    int labelX = centerX + (int) (Math.cos(Math.toRadians(currentAngle + arcAngle / 2 - 90)) * (radius + 20));
                    int labelY = centerY + (int) (Math.sin(Math.toRadians(currentAngle + arcAngle / 2 - 90)) * (radius + 20));
                    
                    g2d.setColor(LayoutPadrao.COR_TEXTO);
                    g2d.setFont(new Font("Segoe UI", Font.PLAIN, 10));
                    String label = categorias[i] + " (" + valores[i] + "%)";
                    FontMetrics fm = g2d.getFontMetrics();
                    int labelWidth = fm.stringWidth(label);
                    g2d.drawString(label, labelX - labelWidth / 2, labelY);
                    
                    currentAngle += arcAngle;
                }
            }
        };
        piePanel.setPreferredSize(new Dimension(300, 200));
        chartPanel.add(piePanel, BorderLayout.CENTER);
        
        return chartPanel;
    }
    
    private JPanel createEvolucaoChart() {
        JPanel chartPanel = new JPanel(new BorderLayout());
        chartPanel.setBackground(LayoutPadrao.COR_FUNDO);
        chartPanel.setBorder(BorderFactory.createCompoundBorder(
            LayoutPadrao.BORDA_PADRAO,
            new EmptyBorder(15, 15, 15, 15)
        ));
        
        // Título
        JLabel titleLabel = new JLabel("📈 Evolução de Métricas - Últimos 30 Dias");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        titleLabel.setForeground(LayoutPadrao.COR_TEXTO);
        chartPanel.add(titleLabel, BorderLayout.NORTH);
        
        // Painel do gráfico de linhas
        JPanel linePanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                // Dados das métricas
                int[] vendas = {12000, 13500, 14000, 15500, 16000, 17500, 18500};
                int[] clientes = {1200, 1220, 1250, 1280, 1300, 1325, 1350};
                int[] produtos = {450, 470, 480, 490, 500, 510, 520};
                
                // Dimensões
                int width = getWidth();
                int height = getHeight();
                int padding = 40;
                int graphWidth = width - 2 * padding;
                int graphHeight = height - 2 * padding;
                
                // Encontrar valores máximos
                int maxVendas = 20000;
                int maxClientes = 1500;
                int maxProdutos = 600;
                
                // Desenhar eixos
                g2d.setColor(LayoutPadrao.COR_TEXTO_CLARO);
                g2d.drawLine(padding, padding, padding, height - padding);
                g2d.drawLine(padding, height - padding, width - padding, height - padding);
                
                // Desenhar linhas
                drawLine(g2d, vendas, maxVendas, graphWidth, graphHeight, padding, height, LayoutPadrao.COR_SUCESSO, "Vendas");
                drawLine(g2d, clientes, maxClientes, graphWidth, graphHeight, padding, height, LayoutPadrao.COR_PRIMARIA, "Clientes");
                drawLine(g2d, produtos, maxProdutos, graphWidth, graphHeight, padding, height, LayoutPadrao.COR_ALERTA, "Produtos");
                
                // Legenda
                g2d.setFont(new Font("Segoe UI", Font.PLAIN, 10));
                int legendY = padding + 20;
                g2d.setColor(LayoutPadrao.COR_SUCESSO);
                g2d.drawString("━━ Vendas (R$)", width - 100, legendY);
                g2d.setColor(LayoutPadrao.COR_PRIMARIA);
                g2d.drawString("━━ Clientes", width - 100, legendY + 15);
                g2d.setColor(LayoutPadrao.COR_ALERTA);
                g2d.drawString("━━ Produtos", width - 100, legendY + 30);
            }
            
            private void drawLine(Graphics2D g2d, int[] valores, int maxValue, int graphWidth, int graphHeight, int padding, int height, Color color, String label) {
                g2d.setColor(color);
                g2d.setStroke(new BasicStroke(2));
                
                int[] xPoints = new int[valores.length];
                int[] yPoints = new int[valores.length];
                
                for (int i = 0; i < valores.length; i++) {
                    xPoints[i] = padding + i * (graphWidth / (valores.length - 1));
                    yPoints[i] = height - padding - (int) ((double) valores[i] / maxValue * graphHeight);
                }
                
                // Desenhar linha
                for (int i = 0; i < valores.length - 1; i++) {
                    g2d.drawLine(xPoints[i], yPoints[i], xPoints[i + 1], yPoints[i + 1]);
                }
                
                // Desenhar pontos
                for (int i = 0; i < valores.length; i++) {
                    g2d.fillOval(xPoints[i] - 3, yPoints[i] - 3, 6, 6);
                }
            }
        };
        linePanel.setPreferredSize(new Dimension(620, 200));
        chartPanel.add(linePanel, BorderLayout.CENTER);
        
        return chartPanel;
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
        JPanel card = LayoutPadrao.criarPainelBranco();
        card.setBorder(BorderFactory.createCompoundBorder(
            LayoutPadrao.BORDA_PADRAO,
            new EmptyBorder(15, 15, 15, 15)
        ));
        
        // Título com LayoutPadrao
        JLabel titleLabel = LayoutPadrao.criarRotuloSubtitulo(metric.getNome());
        titleLabel.setForeground(LayoutPadrao.COR_TEXTO);
        
        // Valor com LayoutPadrao
        JLabel valorLabel = LayoutPadrao.criarRotuloTexto(metric.getValorFormatado());
        valorLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        valorLabel.setForeground(metric.isPositiva() ? LayoutPadrao.COR_SUCESSO : LayoutPadrao.COR_PERIGO);
        
        // Variação com LayoutPadrao
        JLabel variacaoLabel = LayoutPadrao.criarRotuloTexto(metric.getVariacaoFormatada());
        variacaoLabel.setForeground(metric.isPositiva() ? LayoutPadrao.COR_SUCESSO : LayoutPadrao.COR_PERIGO);
        
        // Status com LayoutPadrao
        JLabel statusLabel = LayoutPadrao.criarRotuloTexto(metric.getStatus().getDescricao());
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
                card.setBackground(LayoutPadrao.COR_FUNDO_ESCURO);
            }
            
            @Override
            public void mouseExited(MouseEvent e) {
                card.setBackground(LayoutPadrao.COR_FUNDO);
            }
        });
        
        return card;
    }
    
    private JPanel createResumoCard(List<DashboardMetric> resumo) {
        JPanel card = new JPanel(new GridBagLayout());
        card.setBackground(LayoutPadrao.COR_FUNDO);
        card.setBorder(BorderFactory.createCompoundBorder(
            LayoutPadrao.BORDA_PADRAO,
            new EmptyBorder(15, 15, 15, 15)
        ));
        
        card.setBorder(new TitledBorder(
            LayoutPadrao.BORDA_PADRAO,
            "💰 Resumo Financeiro Mensal",
            TitledBorder.LEFT,
            TitledBorder.TOP,
            new Font("Segoe UI", Font.BOLD, 14),
            LayoutPadrao.COR_TEXTO
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
            nameLabel.setForeground(LayoutPadrao.COR_TEXTO);
            card.add(nameLabel, gbc);
            
            gbc.gridx = 1;
            JLabel valueLabel = new JLabel(metric.getValorFormatado());
            valueLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
            valueLabel.setForeground(LayoutPadrao.COR_TEXTO_CLARO);
            card.add(valueLabel, gbc);
            
            row++;
        }
        
        return card;
    }
    
    private JPanel createMetricasCalculadasCard() {
        JPanel card = LayoutPadrao.criarPainelBranco();
        card.setBorder(BorderFactory.createCompoundBorder(
            LayoutPadrao.BORDA_PADRAO,
            new EmptyBorder(15, 15, 15, 15)
        ));
        
        card.setBorder(new TitledBorder(
            LayoutPadrao.BORDA_PADRAO,
            "📊 Métricas Calculadas",
            TitledBorder.LEFT,
            TitledBorder.TOP,
            LayoutPadrao.FONTE_SUBTITULO,
            LayoutPadrao.COR_TEXTO
        ));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;
        
        // Ticket Médio
        BigDecimal ticketMedio = dashboardService.calcularTicketMedio();
        gbc.gridy = 0;
        gbc.gridx = 0;
        JLabel ticketLabel = LayoutPadrao.criarRotuloCampo("Ticket Médio:");
        card.add(ticketLabel, gbc);
        
        gbc.gridx = 1;
        JLabel ticketValueLabel = LayoutPadrao.criarRotuloTexto(currencyFormat.format(ticketMedio));
        card.add(ticketValueLabel, gbc);
        
        // Margem de Lucro
        BigDecimal margemLucro = dashboardService.calcularMargemLucro();
        gbc.gridy = 1;
        gbc.gridx = 0;
        JLabel margemLabel = LayoutPadrao.criarRotuloCampo("Margem de Lucro:");
        card.add(margemLabel, gbc);
        
        gbc.gridx = 1;
        JLabel margemValueLabel = LayoutPadrao.criarRotuloTexto(percentFormat.format(margemLucro.divide(new BigDecimal("100"))));
        margemValueLabel.setForeground(margemLucro.compareTo(BigDecimal.ZERO) >= 0 ? 
                                     LayoutPadrao.COR_SUCESSO : LayoutPadrao.COR_PERIGO);
        
        card.add(margemValueLabel, gbc);
        
        return card;
    }
    
    public void show() {
        frame.setVisible(true);
    }
    
    public void dispose() {
        frame.dispose();
    }
}
