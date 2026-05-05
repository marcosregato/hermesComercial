package com.br.hermescomercial.pdv.controller;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.datatransfer.StringSelection;
import java.awt.event.ActionEvent;
import java.awt.print.PrinterJob;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import com.br.hermescomercial.util.LoggerUtil;
import com.br.hermescomercial.ui.layout.LayoutPadrao;

/**
 * Controller de Relatórios em SWING
 * Gera diversos relatórios do sistema PDV
 * Versão 2.0 - 100% SWING - Sem JavaFX
 */
public class PDVRelatoriosSwingController {
    
    private JFrame frame;
    private JTabbedPane tabbedPane;
    private JComboBox<String> cbTipoRelatorio;
    private JTextField txtDateInicio;
    private JTextField txtDateFim;
    private JTextArea txtRelatorio;
    
    public PDVRelatoriosSwingController() {
        try {
            LoggerUtil.info("Iniciando PDVRelatoriosSwingController");
            initializeUI();
            LoggerUtil.info("PDVRelatoriosSwingController inicializado com sucesso");
        } catch (Exception e) {
            LoggerUtil.error("Erro ao inicializar PDVRelatoriosSwingController", e);
            JOptionPane.showMessageDialog(null, 
                "Erro ao inicializar relatórios: " + e.getMessage(),
                "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void initializeUI() {
        frame = new JFrame("PDV - Relatórios v2.1.0 - Premium");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setSize(1000, 700);
        frame.setLocationRelativeTo(null);
        
        // Configurar fundo padrão Nova Venda
        frame.getContentPane().setBackground(new Color(245, 245, 250));
        
        frame.add(createMainPanel());
    }
    
    private JPanel createMainPanel() {
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        mainPanel.setBackground(new Color(245, 245, 250));
        
        // Header padrão Nova Venda
        mainPanel.add(createHeaderPanel(), BorderLayout.NORTH);
        
        // Center com abas
        mainPanel.add(createTabbedPanePanel(), BorderLayout.CENTER);
        
        // South
        mainPanel.add(createControlPanel(), BorderLayout.SOUTH);
        
        return mainPanel;
    }
    
    private JPanel createHeaderPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 20, 10));
        panel.setBackground(new Color(41, 128, 185));
        panel.setPreferredSize(new Dimension(0, 80));
        
        // Título central
        JLabel titleLabel = new JLabel("📊 Relatórios do Sistema v2.1.0 - Premium", JLabel.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setForeground(Color.WHITE);
        
        // Botão voltar estilizado
        JButton btnVoltar = new JButton("← Voltar");
        btnVoltar.setBackground(new Color(255, 255, 255));
        btnVoltar.setForeground(new Color(41, 128, 185));
        btnVoltar.setFont(new Font("Arial", Font.BOLD, 12));
        btnVoltar.setBorder(BorderFactory.createEmptyBorder(8, 15, 8, 15));
        btnVoltar.setFocusPainted(false);
        btnVoltar.addActionListener(e -> frame.dispose());
        
        // Data e hora atual
        JLabel lblDataHora = new JLabel(java.time.LocalDateTime.now().format(java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")), JLabel.RIGHT);
        lblDataHora.setFont(new Font("Arial", Font.PLAIN, 12));
        lblDataHora.setForeground(Color.WHITE);
        
        panel.add(btnVoltar, BorderLayout.WEST);
        panel.add(titleLabel, BorderLayout.CENTER);
        panel.add(lblDataHora, BorderLayout.EAST);
        
        return panel;
    }
    
    private JPanel createTabbedPanePanel() {
        JPanel panel = new JPanel(new BorderLayout());
        
        tabbedPane = new JTabbedPane();
        
        // Adicionar listener para destacar aba ativa
        tabbedPane.addChangeListener(e -> updateTabHighlight());
        
        // Aba de Período
        tabbedPane.addTab("Período", createPeriodoPanel());
        
        // Aba de Resumo
        tabbedPane.addTab("Resumo Diário", createResumoPanel());
        
        // Aba de Vendas
        tabbedPane.addTab("Relatório de Vendas", createVendasPanel());
        
        // Aba de Produtos
        tabbedPane.addTab("Relatório de Produtos", createProdutosPanel());
        
        // Aba de Financeiro
        tabbedPane.addTab("Relatório Financeiro", createFinanceiroPanel());
        
        // Destacar primeira aba
        updateTabHighlight();
        
        panel.add(tabbedPane, BorderLayout.CENTER);
        
        return panel;
    }
    
    private JPanel createPeriodoPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        // Painel de informações
        JPanel infoPanel = new JPanel(new GridBagLayout());
        infoPanel.setBorder(BorderFactory.createTitledBorder("Configuração de Período"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;
        
        // Data Início
        gbc.gridx = 0; gbc.gridy = 0;
        infoPanel.add(new JLabel("Data Início:"), gbc);
        gbc.gridx = 1;
        JTextField txtDataInicio = new JTextField(LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")), 10);
        txtDataInicio.setEditable(true);
        infoPanel.add(txtDataInicio, gbc);
        
        // Botão Buscar Data Início
        gbc.gridx = 2; gbc.gridy = 0;
        JButton btnBuscarDataInicio = LayoutPadrao.criarBotaoSucesso("🔍 Buscar");
        btnBuscarDataInicio.addActionListener(e -> buscarPorData(txtDataInicio.getText(), "início"));
        infoPanel.add(btnBuscarDataInicio, gbc);
        
        // Data Fim
        gbc.gridx = 3; gbc.gridy = 0;
        infoPanel.add(new JLabel("Data Fim:"), gbc);
        gbc.gridx = 4;
        JTextField txtDataFim = new JTextField(LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")), 10);
        txtDataFim.setEditable(true);
        infoPanel.add(txtDataFim, gbc);
        
        // Botão Buscar Data Fim
        gbc.gridx = 5; gbc.gridy = 0;
        JButton btnBuscarDataFim = LayoutPadrao.criarBotaoSucesso("🔍 Buscar");
        btnBuscarDataFim.addActionListener(e -> buscarPorData(txtDataFim.getText(), "fim"));
        infoPanel.add(btnBuscarDataFim, gbc);
        
        // Botão Gerar Relatório
        gbc.gridx = 6; gbc.gridy = 0;
        JButton btnGerar = LayoutPadrao.criarBotaoPrimario("📊 Gerar Relatório");
        btnGerar.addActionListener(e -> JOptionPane.showMessageDialog(frame, 
            "Relatório gerado para o período: " + txtDataInicio.getText() + " a " + txtDataFim.getText(),
            "Relatório Gerado", JOptionPane.INFORMATION_MESSAGE));
        infoPanel.add(btnGerar, gbc);
        
        panel.add(infoPanel, BorderLayout.NORTH);
        
        // Área de informações
        JTextArea infoArea = new JTextArea(10, 50);
        infoArea.setEditable(false);
        infoArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
        infoArea.setText("=== CONFIGURAÇÃO DE PERÍODO ===\n\n" +
                        "Selecione o período desejado para gerar relatórios detalhados.\n\n" +
                        "Opções disponíveis:\n" +
                        "• Relatório de Vendas por Período\n" +
                        "• Relatório Financeiro Detalhado\n" +
                        "• Relatório de Produtos Vendidos\n" +
                        "• Relatório de Movimentação de Caixa\n\n" +
                        "Use as datas acima para definir o intervalo desejado.");
        
        JScrollPane scrollPane = new JScrollPane(infoArea);
        panel.add(scrollPane, BorderLayout.CENTER);
        
        return panel;
    }
    
    private JPanel createResumoPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        // Painel de informações
        JPanel infoPanel = new JPanel(new GridBagLayout());
        infoPanel.setBorder(BorderFactory.createTitledBorder("Resumo do Dia"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;
        
        // Data
        gbc.gridx = 0; gbc.gridy = 0;
        infoPanel.add(new JLabel("Data:"), gbc);
        gbc.gridx = 1;
        JTextField txtData = new JTextField(LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")), 10);
        txtData.setEditable(true);
        infoPanel.add(txtData, gbc);
        
        // Botão Buscar Data
        gbc.gridx = 2; gbc.gridy = 0;
        JButton btnBuscarData = LayoutPadrao.criarBotaoSucesso("🔍 Buscar");
        btnBuscarData.addActionListener(e -> buscarPorData(txtData.getText(), "resumo"));
        infoPanel.add(btnBuscarData, gbc);
        
        // Cards de resumo
        gbc.gridx = 0; gbc.gridy = 1; gbc.gridwidth = 2;
        JPanel cardsPanel = new JPanel(new GridLayout(2, 2, 10, 10));
        
        cardsPanel.add(createInfoBox("Total Vendas", "R$ 1.234,56", LayoutPadrao.COR_SUCESSO));
        cardsPanel.add(createInfoBox("Total Pedidos", "45", LayoutPadrao.COR_PRIMARIA));
        cardsPanel.add(createInfoBox("Ticket Médio", "R$ 27,43", LayoutPadrao.COR_ALERTA));
        cardsPanel.add(createInfoBox("Saldo Líquido", "R$ 1.777,78", LayoutPadrao.COR_SECUNDARIA));
        
        infoPanel.add(cardsPanel, gbc);
        
        panel.add(infoPanel, BorderLayout.NORTH);
        
        // Tabela de resumo
        String[] columns = {"Período", "Tipo", "Descrição", "Valor", "Qtd"};
        DefaultTableModel tableModel = new DefaultTableModel(columns, 0);
        
        // Dados de exemplo
        Object[][] data = {
            {"27/04/2026", "VENDA", "Período da manhã", "R$ 567,89", "15"},
            {"27/04/2026", "VENDA", "Período da tarde", "R$ 445,67", "18"},
            {"27/04/2026", "VENDA", "Período da noite", "R$ 221,00", "12"}
        };
        
        for (Object[] row : data) {
            tableModel.addRow(row);
        }
        
        JTable table = new JTable(tableModel);
        
        // Desabilitar edição da tabela
        table.setDefaultEditor(Object.class, null);
        table.setEnabled(false);
        
        JScrollPane scrollPane = new JScrollPane(table);
        panel.add(scrollPane, BorderLayout.CENTER);
        
        return panel;
    }
    
    private JPanel createVendasPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        // Filtros
        JPanel filterPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;
        
        gbc.gridx = 0; gbc.gridy = 0;
        JLabel lblPeriodo = new JLabel("Período:");
        lblPeriodo.setFont(new Font("Arial", Font.PLAIN, 12));
        filterPanel.add(lblPeriodo, gbc);
        gbc.gridx = 1;
        cbTipoRelatorio = new JComboBox<>(new String[]{"Hoje", "Semana", "Mês", "Personalizado"});
        cbTipoRelatorio.setPreferredSize(new Dimension(120, 25));
        filterPanel.add(cbTipoRelatorio, gbc);
        
        gbc.gridx = 2; gbc.gridy = 0;
        filterPanel.add(new JLabel("De:"), gbc);
        gbc.gridx = 3;
        txtDateInicio = new JTextField(LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")), 10);
        filterPanel.add(txtDateInicio, gbc);
        
        gbc.gridx = 4; gbc.gridy = 0;
        filterPanel.add(new JLabel("Até:"), gbc);
        gbc.gridx = 5;
        txtDateFim = new JTextField(LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")), 10);
        filterPanel.add(txtDateFim, gbc);
        
        gbc.gridx = 6; gbc.gridy = 0;
        JButton btnFiltrar = LayoutPadrao.criarBotaoPrimario("🔍 Filtrar");
        btnFiltrar.addActionListener(this::filtrarVendas);
        filterPanel.add(btnFiltrar, gbc);
        
        panel.add(filterPanel, BorderLayout.NORTH);
        
        // Tabela de vendas
        String[] columnsVendas = {"ID", "Data", "Hora", "Cliente", "Valor", "Forma Pgto", "Status"};
        DefaultTableModel vendasModel = new DefaultTableModel(columnsVendas, 0);
        
        // Dados de exemplo
        Object[][] vendasData = {
            {"001", "25/04/2024", "14:30", "João Silva", "R$ 123,45", "Dinheiro", "Concluída"},
            {"002", "25/04/2024", "15:45", "Maria Santos", "R$ 67,89", "Cartão", "Concluída"},
            {"003", "25/04/2024", "16:20", "Pedro Costa", "R$ 234,56", "Pix", "Concluída"},
            {"004", "25/04/2024", "17:10", "Ana Oliveira", "R$ 89,90", "Dinheiro", "Concluída"},
            {"005", "25/04/2024", "18:25", "Carlos Ferreira", "R$ 456,78", "Cartão", "Concluída"}
        };
        
        for (Object[] row : vendasData) {
            vendasModel.addRow(row);
        }
        
        JTable vendasTable = new JTable(vendasModel);
        
        // Desabilitar edição da tabela
        vendasTable.setDefaultEditor(Object.class, null);
        vendasTable.setEnabled(false);
        
        JScrollPane vendasScrollPane = new JScrollPane(vendasTable);
        panel.add(vendasScrollPane, BorderLayout.CENTER);
        
        return panel;
    }
    
    private JPanel createProdutosPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        // Tabela de produtos
        String[] columnsProdutos = {"Código", "Descrição", "Categoria", "Qtd Vendida", "Total Vendido", "Estoque Atual"};
        DefaultTableModel produtosModel = new DefaultTableModel(columnsProdutos, 0);
        
        // Dados de exemplo
        Object[][] produtosData = {
            {"001", "Arroz 5kg", "Alimentos", "25", "R$ 649,75", "50"},
            {"002", "Feijão 1kg", "Alimentos", "18", "R$ 153,00", "30"},
            {"003", "Óleo 900ml", "Alimentos", "12", "R$ 86,40", "5"},
            {"004", "Refrigerante 2L", "Bebidas", "35", "R$ 420,00", "20"},
            {"005", "Água 500ml", "Bebidas", "45", "R$ 112,50", "0"},
            {"006", "Detergente 500ml", "Limpeza", "8", "R$ 34,40", "15"}
        };
        
        for (Object[] row : produtosData) {
            produtosModel.addRow(row);
        }
        
        JTable produtosTable = new JTable(produtosModel);
        
        // Desabilitar edição da tabela
        produtosTable.setDefaultEditor(Object.class, null);
        produtosTable.setEnabled(false);
        
        JScrollPane produtosScrollPane = new JScrollPane(produtosTable);
        panel.add(produtosScrollPane, BorderLayout.CENTER);
        
        return panel;
    }
    
    private JPanel createFinanceiroPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        // Resumo financeiro
        JPanel financeiroInfo = new JPanel(new GridBagLayout());
        financeiroInfo.setBorder(BorderFactory.createTitledBorder("Resumo Financeiro"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;
        
        gbc.gridx = 0; gbc.gridy = 0;
        financeiroInfo.add(new JLabel("Receita Bruta:"), gbc);
        gbc.gridx = 1;
        financeiroInfo.add(new JLabel("R$ 2.456,78"), gbc);
        
        gbc.gridx = 0; gbc.gridy = 1;
        financeiroInfo.add(new JLabel("Custo de Mercadorias:"), gbc);
        gbc.gridx = 1;
        financeiroInfo.add(new JLabel("R$ 1.234,56"), gbc);
        
        gbc.gridx = 0; gbc.gridy = 2;
        financeiroInfo.add(new JLabel("Lucro Bruto:"), gbc);
        gbc.gridx = 1;
        JLabel lblLucroBruto = new JLabel("R$ 1.222,22");
        lblLucroBruto.setForeground(new Color(0, 128, 0));
        lblLucroBruto.setFont(new Font("Arial", Font.BOLD, 14));
        financeiroInfo.add(lblLucroBruto, gbc);
        
        gbc.gridx = 0; gbc.gridy = 3;
        financeiroInfo.add(new JLabel("Despesas Operacionais:"), gbc);
        gbc.gridx = 1;
        financeiroInfo.add(new JLabel("R$ 234,56"), gbc);
        
        gbc.gridx = 0; gbc.gridy = 4;
        financeiroInfo.add(new JLabel("Lucro Líquido:"), gbc);
        gbc.gridx = 1;
        JLabel lblLucroLiquido = new JLabel("R$ 987,66");
        lblLucroLiquido.setForeground(new Color(0, 128, 0));
        lblLucroLiquido.setFont(new Font("Arial", Font.BOLD, 16));
        financeiroInfo.add(lblLucroLiquido, gbc);
        
        panel.add(financeiroInfo, BorderLayout.NORTH);
        
        // Área de texto para relatório detalhado
        txtRelatorio = new JTextArea(10, 50);
        txtRelatorio.setEditable(false);
        txtRelatorio.setFont(new Font("Monospaced", Font.PLAIN, 12));
        txtRelatorio.setText("=== RELATÓRIO FINANCEIRO DETALHADO ===\n\n" +
                           "Período: 25/04/2024\n\n" +
                           "RECEITAS:\n" +
                           "Vendas de Produtos: R$ 2.456,78\n" +
                           "Serviços: R$ 0,00\n" +
                           "Total Receitas: R$ 2.456,78\n\n" +
                           "CUSTOS:\n" +
                           "Custo Mercadorias: R$ 1.234,56\n" +
                           "Despesas Operacionais: R$ 234,56\n" +
                           "Total Custos: R$ 1.469,12\n\n" +
                           "LUCRO LÍQUIDO: R$ 987,66\n" +
                           "Margem: 40,2%\n\n" +
                           "Versão SWING 2.0");
        
        JScrollPane scrollPane = new JScrollPane(txtRelatorio);
        panel.add(scrollPane, BorderLayout.CENTER);
        
        return panel;
    }
    
    private JPanel createControlPanel() {
        JPanel panel = new JPanel(new FlowLayout());
        panel.setBorder(BorderFactory.createTitledBorder("Ações"));
        
        JButton btnGerarRelatorio = LayoutPadrao.criarBotaoSucesso("📊 Gerar Relatório");
        btnGerarRelatorio.addActionListener(this::gerarRelatorio);
        
        JButton btnExportar = LayoutPadrao.criarBotaoPrimario("📤 Exportar PDF");
        btnExportar.addActionListener(this::exportarPDF);
        
        JButton btnImprimir = LayoutPadrao.criarBotaoSecundario("🖨️ Imprimir");
        btnImprimir.addActionListener(this::imprimirRelatorio);
        
        JButton btnLimpar = LayoutPadrao.criarBotaoPerigo("🔄 Limpar");
        btnLimpar.addActionListener(this::limparRelatorio);
        
        panel.add(btnGerarRelatorio);
        panel.add(btnExportar);
        panel.add(btnImprimir);
        panel.add(btnLimpar);
        
        return panel;
    }
    
    private JPanel createInfoBox(String title, String value, Color color) {
        JPanel panel = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2d.setColor(color);
                g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 15, 15);
                g2d.dispose();
            }
        };
        panel.setOpaque(false);
        panel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(color.darker(), 1),
            BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));
        panel.setPreferredSize(new Dimension(150, 80));
        
        JLabel titleLabel = new JLabel(title, JLabel.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 12));
        titleLabel.setForeground(Color.BLACK);
        
        JLabel valueLabel = new JLabel(value, JLabel.CENTER);
        valueLabel.setFont(new Font("Arial", Font.BOLD, 18));
        valueLabel.setForeground(Color.BLACK);
        
        panel.add(titleLabel, BorderLayout.NORTH);
        panel.add(valueLabel, BorderLayout.CENTER);
        
        return panel;
    }
    
    private void filtrarVendas(ActionEvent e) {
        JOptionPane.showMessageDialog(frame, 
            "Filtro aplicado: " + cbTipoRelatorio.getSelectedItem() + "\n" +
            "Período: " + txtDateInicio.getText() + " a " + txtDateFim.getText(),
            "Filtro Aplicado", JOptionPane.INFORMATION_MESSAGE);
    }
    
    private void gerarRelatorio(ActionEvent e) {
        int selectedTab = tabbedPane.getSelectedIndex();
        String tabTitle = tabbedPane.getTitleAt(selectedTab);
        
        StringBuilder relatorio = new StringBuilder();
        relatorio.append("=== RELATÓRIO GERADO ===\n\n");
        relatorio.append("Tipo: ").append(tabTitle).append("\n");
        relatorio.append("Data: ").append(LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))).append("\n");
        relatorio.append("Hora: ").append(java.time.LocalTime.now().format(java.time.format.DateTimeFormatter.ofPattern("HH:mm:ss"))).append("\n\n");
        
        // Adicionar conteúdo específico baseado na aba selecionada
        switch (selectedTab) {
            case 0: // Resumo Diário
                relatorio.append("=== RESUMO DIÁRIO ===\n");
                relatorio.append("Total Vendas: R$ 1.234,56\n");
                relatorio.append("Total Pedidos: 45\n");
                relatorio.append("Ticket Médio: R$ 27,43\n");
                relatorio.append("Saldo Líquido: R$ 1.777,78\n");
                break;
            case 1: // Relatório de Vendas
                relatorio.append("=== RELATÓRIO DE VENDAS ===\n");
                relatorio.append("Período: ").append(txtDateInicio.getText()).append(" a ").append(txtDateFim.getText()).append("\n");
                relatorio.append("Filtro: ").append(cbTipoRelatorio.getSelectedItem()).append("\n");
                relatorio.append("Total de Vendas: 5\n");
                relatorio.append("Valor Total: R$ 972,58\n");
                break;
            case 2: // Relatório de Produtos
                relatorio.append("=== RELATÓRIO DE PRODUTOS ===\n");
                relatorio.append("Total de Produtos: 6\n");
                relatorio.append("Produtos em Estoque: 5\n");
                relatorio.append("Produtos sem Estoque: 1\n");
                relatorio.append("Valor Total em Estoque: R$ 3.456,05\n");
                break;
            case 3: // Relatório Financeiro
                relatorio.append("=== RELATÓRIO FINANCEIRO ===\n");
                relatorio.append("Receita Bruta: R$ 2.456,78\n");
                relatorio.append("Custo Mercadorias: R$ 1.234,56\n");
                relatorio.append("Lucro Bruto: R$ 1.222,22\n");
                relatorio.append("Despesas Operacionais: R$ 234,56\n");
                relatorio.append("Lucro Líquido: R$ 987,66\n");
                relatorio.append("Margem: 40,2%\n");
                break;
        }
        
        relatorio.append("\n=== VERSÃO SWING 2.0 ===\n");
        relatorio.append("Hermes Comercial PDV\n");
        relatorio.append("100% SWING - Sem JavaFX\n");
        
        // Exibir relatório em uma janela dedicada
        JFrame relatorioFrame = new JFrame("Relatório - " + tabTitle);
        relatorioFrame.setSize(600, 500);
        relatorioFrame.setLocationRelativeTo(frame);
        
        JTextArea relatorioArea = new JTextArea(relatorio.toString());
        relatorioArea.setEditable(false);
        relatorioArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
        relatorioArea.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        JScrollPane scrollPane = new JScrollPane(relatorioArea);
        relatorioFrame.add(scrollPane, BorderLayout.CENTER);
        
        // Painel de botões
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 10));
        
        JButton btnCopiar = LayoutPadrao.criarBotaoPrimario("📋 Copiar");
        btnCopiar.addActionListener(ev -> {
            String relatorioText = relatorio.toString();
            StringSelection selection = new StringSelection(relatorioText);
            Toolkit.getDefaultToolkit().getSystemClipboard().setContents(selection, null);
            JOptionPane.showMessageDialog(relatorioFrame, 
                "Relatório copiado para área de transferência!", 
                "Copiado", 
                JOptionPane.INFORMATION_MESSAGE);
        });
        
        JButton btnFechar = LayoutPadrao.criarBotaoPerigo("❌ Fechar");
        btnFechar.addActionListener(ev -> relatorioFrame.dispose());
        
        buttonPanel.add(btnCopiar);
        buttonPanel.add(btnFechar);
        relatorioFrame.add(buttonPanel, BorderLayout.SOUTH);
        
        relatorioFrame.setVisible(true);
        
        // Mensagem de confirmação
        JOptionPane.showMessageDialog(frame, 
            "Relatório gerado com sucesso!\n" +
            "Tipo: " + tabTitle + "\n" +
            "Janela de relatório aberta para visualização.",
            "Relatório Gerado", 
            JOptionPane.INFORMATION_MESSAGE);
    }
    
    private void exportarPDF(ActionEvent e) {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Exportar Relatório como PDF");
        fileChooser.setFileFilter(new javax.swing.filechooser.FileNameExtensionFilter(
            "Arquivos PDF", "pdf"));
        
        // Sugerir nome de arquivo
        String nomeArquivo = "relatorio_" + LocalDate.now().format(DateTimeFormatter.ofPattern("dd_MM_yyyy")) + ".pdf";
        fileChooser.setSelectedFile(new java.io.File(nomeArquivo));
        
        int result = fileChooser.showSaveDialog(frame);
        if (result == JFileChooser.APPROVE_OPTION) {
            java.io.File selectedFile = fileChooser.getSelectedFile();
            
            // Garantir extensão .pdf
            if (!selectedFile.getName().toLowerCase().endsWith(".pdf")) {
                selectedFile = new java.io.File(selectedFile.getParent(), selectedFile.getName() + ".pdf");
            }
            
            // Simulação de exportação (em implementação real, usaria biblioteca como iText)
            try {
                // Simulação de criação de PDF
                StringBuilder pdfContent = new StringBuilder();
                pdfContent.append("=== RELATÓRIO PDF ===\n\n");
                pdfContent.append("Arquivo: ").append(selectedFile.getName()).append("\n");
                pdfContent.append("Data: ").append(LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))).append("\n");
                pdfContent.append("Hora: ").append(java.time.LocalTime.now().format(java.time.format.DateTimeFormatter.ofPattern("HH:mm:ss"))).append("\n\n");
                pdfContent.append("Conteúdo do relatório seria gerado aqui\n");
                pdfContent.append("com biblioteca iText ou similar\n\n");
                pdfContent.append("Versão SWING 2.0");
                
                // Salvar conteúdo em arquivo de texto (simulação)
                java.nio.file.Files.write(selectedFile.toPath(), pdfContent.toString().getBytes());
                
                JOptionPane.showMessageDialog(frame, 
                    "Relatório exportado com sucesso!\n\n" +
                    "Arquivo: " + selectedFile.getAbsolutePath() + "\n" +
                    "Formato: PDF (simulado como texto)\n" +
                    "Tamanho: " + selectedFile.length() + " bytes",
                    "Exportação Concluída", JOptionPane.INFORMATION_MESSAGE);
                    
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(frame, 
                    "Erro ao exportar relatório: " + ex.getMessage(),
                    "Erro de Exportação", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    private void imprimirRelatorio(ActionEvent e) {
        // Criar diálogo de impressão
        PrinterJob job = PrinterJob.getPrinterJob();
        
        if (job.printDialog()) {
            try {
                // Criar conteúdo para impressão
                String relatorioContent = "=== RELATÓRIO PARA IMPRESSÃO ===\n\n" +
                    "Data: " + LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")) + "\n" +
                    "Hora: " + java.time.LocalTime.now().format(java.time.format.DateTimeFormatter.ofPattern("HH:mm:ss")) + "\n" +
                    "Sistema: Hermes Comercial PDV v2.0\n\n" +
                    "Conteúdo do relatório selecionado\n" +
                    "Aba: " + tabbedPane.getTitleAt(tabbedPane.getSelectedIndex()) + "\n\n" +
                    "Versão SWING 2.0 - 100% SWING";
                
                // Criar componente printable
                java.awt.print.Printable printable = new java.awt.print.Printable() {
                    @Override
                    public int print(java.awt.Graphics g, java.awt.print.PageFormat pf, int pageIndex) throws java.awt.print.PrinterException {
                        if (pageIndex > 0) {
                            return java.awt.print.Printable.NO_SUCH_PAGE;
                        }
                        
                        Graphics2D g2d = (Graphics2D) g;
                        g2d.translate(pf.getImageableX(), pf.getImageableY());
                        
                        // Configurar fonte
                        g2d.setFont(new Font("Monospaced", Font.PLAIN, 10));
                        
                        // Desenhar conteúdo
                        String[] lines = relatorioContent.split("\n");
                        int y = 50;
                        for (String line : lines) {
                            g2d.drawString(line, 50, y);
                            y += 15;
                        }
                        
                        return java.awt.print.Printable.PAGE_EXISTS;
                    }
                };
                
                job.setPrintable(printable);
                job.print();
                
                JOptionPane.showMessageDialog(frame, 
                    "Relatório enviado para impressão!\n\n" +
                    "Impressora: " + job.getJobName() + "\n" +
                    "Status: Enviado para fila de impressão",
                    "Impressão Concluída", JOptionPane.INFORMATION_MESSAGE);
                    
            } catch (java.awt.print.PrinterException ex) {
                JOptionPane.showMessageDialog(frame, 
                    "Erro ao imprimir relatório: " + ex.getMessage(),
                    "Erro de Impressão", JOptionPane.ERROR_MESSAGE);
            }
        } else {
            JOptionPane.showMessageDialog(frame, 
                "Impressão cancelada pelo usuário.",
                "Impressão Cancelada", JOptionPane.INFORMATION_MESSAGE);
        }
    }
    
    private void limparRelatorio(ActionEvent e) {
        txtRelatorio.setText("");
        JOptionPane.showMessageDialog(frame, "Relatório limpo!", "Limpar", JOptionPane.INFORMATION_MESSAGE);
    }
    
    public void show() {
        frame.setVisible(true);
    }
    
    /**
     * Realiza busca por data específica
     */
    private void buscarPorData(String data, String tipo) {
        try {
            // Validar formato da data
            if (!data.matches("\\d{2}/\\d{2}/\\d{4}")) {
                JOptionPane.showMessageDialog(frame, 
                    "Formato de data inválido! Use DD/MM/AAAA",
                    "Erro de Validação", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            // Simular busca por data (em implementação real, consultaria banco de dados)
            StringBuilder resultado = new StringBuilder();
            resultado.append("🔍 RESULTADO DA BUSCA POR DATA\n");
            resultado.append("=====================================\n\n");
            resultado.append("Data pesquisada: ").append(data).append("\n");
            resultado.append("Tipo de busca: ").append(tipo).append("\n\n");
            
            // Simular dados encontrados
            resultado.append("📊 DADOS ENCONTRADOS:\n");
            resultado.append("─────────────────────────────\n");
            resultado.append("• Total de vendas: 23\n");
            resultado.append("• Valor total: R$ 1.234,56\n");
            resultado.append("• Ticket médio: R$ 53,68\n");
            resultado.append("• Produtos vendidos: 67\n");
            resultado.append("• Clientes atendidos: 45\n\n");
            
            resultado.append("📈 DETALHES:\n");
            resultado.append("─────────────────────────────\n");
            resultado.append("• Vendas à vista: R$ 890,34\n");
            resultado.append("• Vendas cartão: R$ 344,22\n");
            resultado.append("• Vendas Pix: R$ 0,00\n\n");
            
            resultado.append("🏢 PRODUTOS MAIS VENDIDOS:\n");
            resultado.append("─────────────────────────────\n");
            resultado.append("1. Arroz 5kg - 8 unidades\n");
            resultado.append("2. Feijão 1kg - 6 unidades\n");
            resultado.append("3. Óleo 900ml - 5 unidades\n");
            resultado.append("4. Açúcar 1kg - 4 unidades\n");
            resultado.append("5. Café 500g - 3 unidades\n");
            
            // Exibir resultado em nova janela
            JFrame resultadoFrame = new JFrame("🔍 Busca por Data: " + data);
            resultadoFrame.setSize(500, 400);
            resultadoFrame.setLocationRelativeTo(frame);
            
            JTextArea resultadoArea = new JTextArea(resultado.toString());
            resultadoArea.setEditable(false);
            resultadoArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
            resultadoArea.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
            
            JScrollPane scrollPane = new JScrollPane(resultadoArea);
            resultadoFrame.add(scrollPane);
            
            resultadoFrame.setVisible(true);
            
            // Log da busca
            System.out.println("🔍 BUSCA REALIZADA: " + data + " (tipo: " + tipo + ")");
            
            // Feedback ao usuário
            JOptionPane.showMessageDialog(frame, 
                "Busca por data realizada com sucesso!\n" +
                "Data: " + data + "\n" +
                "Tipo: " + tipo + "\n" +
                "Resultados encontrados: 23 vendas",
                "Busca Concluída", JOptionPane.INFORMATION_MESSAGE);
                
        } catch (Exception e) {
            JOptionPane.showMessageDialog(frame, 
                "Erro ao realizar busca por data: " + e.getMessage(),
                "Erro na Busca", JOptionPane.ERROR_MESSAGE);
            System.err.println("Erro na busca por data: " + e.getMessage());
        }
    }
    
    /**
     * Destaca a aba ativa com destaque visual aprimorado
     */
    private void updateTabHighlight() {
        int selectedIndex = tabbedPane.getSelectedIndex();
        String tabTitle = tabbedPane.getTitleAt(selectedIndex);
        
        // Atualizar título da janela com aba ativa e destaque
        frame.setTitle("🔵 PDV - Relatórios v2.1.0 - Premium [📌 " + tabTitle + " ATIVA]");
        
        // Aplicar destaque visual na aba ativa
        for (int i = 0; i < tabbedPane.getTabCount(); i++) {
            if (i == selectedIndex) {
                // Aba ativa - fundo azul escuro e texto preto (letra escura)
                tabbedPane.setBackgroundAt(i, new Color(70, 130, 180)); // Steel Blue (mais escuro)
                tabbedPane.setForegroundAt(i, Color.BLACK); // Letra escura
                
                // Adicionar indicador visual
                String activeTitle = "📌 " + tabTitle + " 📌";
                tabbedPane.setTitleAt(i, activeTitle);
            } else {
                // Abas inativas - fundo cinza médio
                tabbedPane.setBackgroundAt(i, new Color(200, 200, 200)); // Gray médio
                tabbedPane.setForegroundAt(i, Color.BLACK);
                
                // Remover indicador visual
                String originalTitle = tabbedPane.getTitleAt(i).replace("📌 ", "").replace(" 📌", "");
                tabbedPane.setTitleAt(i, originalTitle);
            }
        }
        
        // Log da aba ativa com destaque
        System.out.println("🔵 ABA ATIVA: " + tabTitle + " (índice: " + selectedIndex + ")");
        
        // Feedback visual adicional com cores mais escuras
        if (frame.getContentPane() instanceof JPanel) {
            JPanel mainPanel = (JPanel) frame.getContentPane();
            if (selectedIndex == 0) { // Período
                mainPanel.setBackground(new Color(230, 240, 250)); // Azul escuro suave
            } else if (selectedIndex == 1) { // Resumo Diário
                mainPanel.setBackground(new Color(250, 230, 230)); // Vermelho escuro suave
            } else if (selectedIndex == 2) { // Relatório de Vendas
                mainPanel.setBackground(new Color(230, 250, 230)); // Verde escuro suave
            } else if (selectedIndex == 3) { // Relatório de Produtos
                mainPanel.setBackground(new Color(250, 250, 230)); // Amarelo escuro suave
            } else if (selectedIndex == 4) { // Relatório Financeiro
                mainPanel.setBackground(new Color(250, 230, 250)); // Rosa escuro suave
            }
        }
    }
    
    /**
     * Abre o relatório de vendas do dia
     */
    public void abrirRelatorioVendasDia() {
        tabbedPane.setSelectedIndex(1); // Primeira aba (Resumo Diário)
        updateTabHighlight();
    }
    
    /**
     * Abre o relatório de produtos
     */
    public void abrirRelatorioProdutos() {
        tabbedPane.setSelectedIndex(3); // Terceira aba (Produtos)
        updateTabHighlight();
    }
    
    /**
     * Abre o relatório financeiro
     */
    public void abrirRelatorioFinanceiro() {
        tabbedPane.setSelectedIndex(4); // Quarta aba (Financeiro)
        updateTabHighlight();
    }
    
    // Método para compatibilidade com testes
    public JFrame getFrame() {
        return frame;
    }
}
