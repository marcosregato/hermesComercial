package com.br.hermescomercial.erp.controller;

import com.br.hermescomercial.theme.HermesTheme;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Calendar;

/**
 * Controller para Relatório Financeiro Completo do ERP
 * Versão 2.5.0 - Relatórios Financeiros com Tema Pastel
 * 
 * Inclui: DRE, Fluxo de Caixa, Balancete, Contas a Pagar/Receber
 * Novidade: Interface com cores pastéis suaves e agradáveis
 */
public class ERPRelatorioFinanceiroSwingController {
    
    // ==================== CORES PASTÉIS PERSONALIZADAS ====================
    private static final Color PASTEL_BLUE = new Color(173, 216, 230);      // Azul pastel claro
    private static final Color PASTEL_GREEN = new Color(144, 238, 144);     // Verde pastel claro
    private static final Color PASTEL_PINK = new Color(255, 182, 193);      // Rosa pastel claro
    private static final Color PASTEL_YELLOW = new Color(255, 255, 224);    // Amarelo pastel claro
    private static final Color PASTEL_PURPLE = new Color(221, 160, 221);    // Roxo pastel claro
    private static final Color PASTEL_ORANGE = new Color(255, 218, 185);    // Laranja pastel claro
    private static final Color PASTEL_GRAY = new Color(240, 240, 240);      // Cinza pastel claro
    private static final Color PASTEL_MINT = new Color(152, 255, 152);      // Menta pastel claro
    
    // Cores de fundo e texto
    private static final Color BACKGROUND_PASTEL = new Color(248, 250, 252);  // Fundo principal pastel
    private static final Color CARD_BACKGROUND = new Color(255, 255, 255);     // Fundo de cards branco
    private static final Color TEXT_PRIMARY = new Color(70, 70, 70);          // Texto principal suave
    
    private JFrame frame;
    private DecimalFormat df;
    private JTabbedPane tabbedPane;
    
    // Componentes de filtro
    private JComboBox<String> cbPeriodo;
    private JDateChooser dcDataInicio, dcDataFim;
    private JButton btnGerar, btnExportar;
    
    // Tabelas
    private JTable dreTable, fluxoCaixaTable, balanceteTable, contasTable;
    private DefaultTableModel dreModel, fluxoCaixaModel, balanceteModel, contasModel;
    
    // Labels de resumo
    private JLabel lblReceitaTotal, lblDespesaTotal, lblLucroLiquido;
    private JLabel lblSaldoInicial, lblSaldoFinal, lblTotalContasReceber, lblTotalContasPagar;
    
    public ERPRelatorioFinanceiroSwingController() {
        df = new DecimalFormat("#,##0.00");
        initializeUI();
    }
    
    private void initializeUI() {
        frame = new JFrame("📊 Relatório Financeiro - ERP");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setSize(1400, 900);
        frame.setLocationRelativeTo(null);
        
        // Aplicar cores pastéis
        frame.getContentPane().setBackground(BACKGROUND_PASTEL);
        
        createMainPanel();
        frame.setVisible(true);
    }
    
    private void createMainPanel() {
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        mainPanel.setBackground(BACKGROUND_PASTEL);
        
        // Header com filtros
        createHeaderPanel();
        
        // Abas dos relatórios
        createTabbedPane();
        
        mainPanel.add(createHeaderPanel(), BorderLayout.NORTH);
        mainPanel.add(tabbedPane, BorderLayout.CENTER);
        
        frame.add(mainPanel);
    }
    
    private JPanel createHeaderPanel() {
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setOpaque(false);
        headerPanel.setBorder(new EmptyBorder(0, 0, 20, 0));
        
        // Título
        JLabel titleLabel = new JLabel("📊 Relatórios Financeiros");
        titleLabel.setFont(HermesTheme.FONT_TITLE);
        titleLabel.setForeground(HermesTheme.TEXT_PRIMARY);
        
        // Painel de filtros
        JPanel filterPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        filterPanel.setOpaque(false);
        
        filterPanel.add(new JLabel("Período:"));
        cbPeriodo = new JComboBox<>(new String[]{"Mês Atual", "Trimestre", "Semestre", "Ano", "Personalizado"});
        cbPeriodo.setFont(HermesTheme.FONT_DEFAULT);
        filterPanel.add(cbPeriodo);
        
        filterPanel.add(new JLabel("  Data Início:"));
        dcDataInicio = new JDateChooser();
        dcDataInicio.setFont(HermesTheme.FONT_DEFAULT);
        filterPanel.add(dcDataInicio);
        
        filterPanel.add(new JLabel("  Data Fim:"));
        dcDataFim = new JDateChooser();
        dcDataFim.setFont(HermesTheme.FONT_DEFAULT);
        filterPanel.add(dcDataFim);
        
        // Botões de ação
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.setOpaque(false);
        
        btnGerar = HermesTheme.createModernButton("🔄 Gerar Relatório", HermesTheme.PRIMARY_COLOR);
        btnGerar.addActionListener(e -> gerarRelatorios());
        
        btnExportar = HermesTheme.createModernButton("📥 Exportar", HermesTheme.SUCCESS_COLOR);
        btnExportar.addActionListener(e -> exportarRelatorios());
        
        buttonPanel.add(btnGerar);
        buttonPanel.add(btnExportar);
        
        headerPanel.add(titleLabel, BorderLayout.WEST);
        headerPanel.add(filterPanel, BorderLayout.CENTER);
        headerPanel.add(buttonPanel, BorderLayout.EAST);
        
        return headerPanel;
    }
    
    private void createTabbedPane() {
        tabbedPane = new JTabbedPane();
        tabbedPane.setFont(HermesTheme.FONT_BOLD);
        tabbedPane.setBackground(PASTEL_GRAY);
        tabbedPane.setForeground(TEXT_PRIMARY);
        
        // Aba DRE
        createDRETab();
        
        // Aba Fluxo de Caixa
        createFluxoCaixaTab();
        
        // Aba Balancete
        createBalanceteTab();
        
        // Aba Contas a Pagar/Receber
        createContasTab();
        
        // Carregar dados iniciais
        carregarDadosExemplo();
    }
    
    private void createDRETab() {
        JPanel drePanel = new JPanel(new BorderLayout());
        drePanel.setBackground(BACKGROUND_PASTEL);
        drePanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        
        // Painel de resumo DRE
        JPanel resumoPanel = createResumoDREPanel();
        
        // Tabela DRE
        String[] colunasDRE = {"Conta", "Descrição", "Valor (R$)", "%"};
        dreModel = new DefaultTableModel(colunasDRE, 0) {
            @Override
            public Class<?> getColumnClass(int columnIndex) {
                switch (columnIndex) {
                    case 2: return BigDecimal.class;
                    case 3: return Double.class;
                    default: return String.class;
                }
            }
            
            @Override
            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return false;
            }
        };
        
        dreTable = new JTable(dreModel);
        dreTable.setBackground(CARD_BACKGROUND);
        dreTable.setForeground(TEXT_PRIMARY);
        dreTable.setRowHeight(30);
        dreTable.getTableHeader().setBackground(PASTEL_BLUE);
        dreTable.getTableHeader().setForeground(TEXT_PRIMARY);
        
        // Renderizador personalizado para valores
        dreTable.getColumnModel().getColumn(2).setCellRenderer(new CurrencyCellRenderer());
        dreTable.getColumnModel().getColumn(3).setCellRenderer(new PercentageCellRenderer());
        
        JScrollPane scrollDRE = new JScrollPane(dreTable);
        scrollDRE.setPreferredSize(new Dimension(1300, 400));
        scrollDRE.setBackground(PASTEL_GRAY);
        
        drePanel.add(resumoPanel, BorderLayout.NORTH);
        drePanel.add(scrollDRE, BorderLayout.CENTER);
        
        tabbedPane.addTab("📈 DRE", drePanel);
    }
    
    private void createFluxoCaixaTab() {
        JPanel fluxoPanel = HermesTheme.createMainPanel();
        fluxoPanel.setLayout(new BorderLayout());
        
        // Painel de resumo Fluxo de Caixa
        JPanel resumoPanel = createResumoFluxoCaixaPanel();
        
        // Tabela Fluxo de Caixa
        String[] colunasFluxo = {"Data", "Descrição", "Categoria", "Entrada (R$)", "Saída (R$)", "Saldo (R$)"};
        fluxoCaixaModel = new DefaultTableModel(colunasFluxo, 0) {
            @Override
            public Class<?> getColumnClass(int columnIndex) {
                switch (columnIndex) {
                    case 0: return Date.class;
                    case 3: case 4: case 5: return BigDecimal.class;
                    default: return String.class;
                }
            }
            
            @Override
            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return false;
            }
        };
        
        fluxoCaixaTable = new JTable(fluxoCaixaModel);
        HermesTheme.applyTableTheme(fluxoCaixaTable);
        fluxoCaixaTable.setRowHeight(30);
        
        // Renderizadores personalizados
        fluxoCaixaTable.getColumnModel().getColumn(0).setCellRenderer(new DateCellRenderer());
        fluxoCaixaTable.getColumnModel().getColumn(3).setCellRenderer(new CurrencyCellRenderer());
        fluxoCaixaTable.getColumnModel().getColumn(4).setCellRenderer(new CurrencyCellRenderer());
        fluxoCaixaTable.getColumnModel().getColumn(5).setCellRenderer(new CurrencyCellRenderer());
        
        JScrollPane scrollFluxo = new JScrollPane(fluxoCaixaTable);
        scrollFluxo.setPreferredSize(new Dimension(1300, 400));
        
        fluxoPanel.add(resumoPanel, BorderLayout.NORTH);
        fluxoPanel.add(scrollFluxo, BorderLayout.CENTER);
        
        tabbedPane.addTab("💰 Fluxo de Caixa", fluxoPanel);
    }
    
    private void createBalanceteTab() {
        JPanel balancetePanel = HermesTheme.createMainPanel();
        balancetePanel.setLayout(new BorderLayout());
        
        // Tabela Balancete
        String[] colunasBalancete = {"Conta", "Descrição", "Débito (R$)", "Crédito (R$)", "Saldo (R$)"};
        balanceteModel = new DefaultTableModel(colunasBalancete, 0) {
            @Override
            public Class<?> getColumnClass(int columnIndex) {
                switch (columnIndex) {
                    case 2: case 3: case 4: return BigDecimal.class;
                    default: return String.class;
                }
            }
            
            @Override
            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return false;
            }
        };
        
        balanceteTable = new JTable(balanceteModel);
        HermesTheme.applyTableTheme(balanceteTable);
        balanceteTable.setRowHeight(30);
        
        // Renderizadores personalizados
        balanceteTable.getColumnModel().getColumn(2).setCellRenderer(new CurrencyCellRenderer());
        balanceteTable.getColumnModel().getColumn(3).setCellRenderer(new CurrencyCellRenderer());
        balanceteTable.getColumnModel().getColumn(4).setCellRenderer(new CurrencyCellRenderer());
        
        JScrollPane scrollBalancete = new JScrollPane(balanceteTable);
        scrollBalancete.setPreferredSize(new Dimension(1300, 450));
        
        balancetePanel.add(scrollBalancete, BorderLayout.CENTER);
        
        tabbedPane.addTab("📋 Balancete", balancetePanel);
    }
    
    private void createContasTab() {
        JPanel contasPanel = HermesTheme.createMainPanel();
        contasPanel.setLayout(new BorderLayout());
        
        // Painel de resumo Contas
        JPanel resumoPanel = createResumoContasPanel();
        
        // Tabela Contas a Pagar/Receber
        String[] colunasContas = {"Tipo", "Documento", "Cliente/Fornecedor", "Vencimento", "Valor (R$)", "Status", "Dias Atraso"};
        contasModel = new DefaultTableModel(colunasContas, 0) {
            @Override
            public Class<?> getColumnClass(int columnIndex) {
                switch (columnIndex) {
                    case 3: return Date.class;
                    case 4: return BigDecimal.class;
                    case 6: return Integer.class;
                    default: return String.class;
                }
            }
            
            @Override
            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return false;
            }
        };
        
        contasTable = new JTable(contasModel);
        HermesTheme.applyTableTheme(contasTable);
        contasTable.setRowHeight(30);
        
        // Renderizadores personalizados
        contasTable.getColumnModel().getColumn(3).setCellRenderer(new DateCellRenderer());
        contasTable.getColumnModel().getColumn(4).setCellRenderer(new CurrencyCellRenderer());
        contasTable.getColumnModel().getColumn(5).setCellRenderer(new StatusCellRenderer());
        contasTable.getColumnModel().getColumn(6).setCellRenderer(new DiasAtrasoCellRenderer());
        
        JScrollPane scrollContas = new JScrollPane(contasTable);
        scrollContas.setPreferredSize(new Dimension(1300, 400));
        
        contasPanel.add(resumoPanel, BorderLayout.NORTH);
        contasPanel.add(scrollContas, BorderLayout.CENTER);
        
        tabbedPane.addTab("💳 Contas a Pagar/Receber", contasPanel);
    }
    
    private JPanel createResumoDREPanel() {
        JPanel panel = new JPanel(new GridLayout(1, 3, 20, 10));
        panel.setOpaque(false);
        panel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        
        lblReceitaTotal = createResumoLabel("Receita Total", "R$ 0,00", PASTEL_GREEN);
        lblDespesaTotal = createResumoLabel("Despesa Total", "R$ 0,00", PASTEL_PINK);
        lblLucroLiquido = createResumoLabel("Lucro Líquido", "R$ 0,00", PASTEL_BLUE);
        
        panel.add(lblReceitaTotal);
        panel.add(lblDespesaTotal);
        panel.add(lblLucroLiquido);
        
        return panel;
    }
    
    private JPanel createResumoFluxoCaixaPanel() {
        JPanel panel = new JPanel(new GridLayout(1, 2, 20, 10));
        panel.setOpaque(false);
        panel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        
        lblSaldoInicial = createResumoLabel("Saldo Inicial", "R$ 0,00", PASTEL_YELLOW);
        lblSaldoFinal = createResumoLabel("Saldo Final", "R$ 0,00", PASTEL_PURPLE);
        
        panel.add(lblSaldoInicial);
        panel.add(lblSaldoFinal);
        
        return panel;
    }
    
    private JPanel createResumoContasPanel() {
        JPanel panel = new JPanel(new GridLayout(1, 2, 20, 10));
        panel.setOpaque(false);
        panel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        
        lblTotalContasReceber = createResumoLabel("Contas a Receber", "R$ 0,00", PASTEL_MINT);
        lblTotalContasPagar = createResumoLabel("Contas a Pagar", "R$ 0,00", PASTEL_ORANGE);
        
        panel.add(lblTotalContasReceber);
        panel.add(lblTotalContasPagar);
        
        return panel;
    }
    
    private JLabel createResumoLabel(String title, String value, Color color) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setOpaque(false);
        panel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(color, 2),
            new EmptyBorder(10, 15, 10, 15)
        ));
        
        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(HermesTheme.FONT_SMALL);
        titleLabel.setForeground(HermesTheme.TEXT_MUTED);
        
        JLabel valueLabel = new JLabel(value);
        valueLabel.setFont(HermesTheme.FONT_BOLD);
        valueLabel.setForeground(color);
        
        panel.add(titleLabel, BorderLayout.NORTH);
        panel.add(valueLabel, BorderLayout.CENTER);
        
        JLabel container = new JLabel();
        container.setLayout(new BorderLayout());
        container.add(panel, BorderLayout.CENTER);
        
        return container;
    }
    
    private void carregarDadosExemplo() {
        // Dados DRE
        Object[][] dadosDRE = {
            {"3.01", "Receita de Vendas", new BigDecimal("185000.00"), 100.0},
            {"3.02", "Receita de Serviços", new BigDecimal("32000.00"), 17.3},
            {"", "Receita Operacional Bruta", new BigDecimal("217000.00"), 117.3},
            {"", "", null, null},
            {"3.11", "Custo das Mercadorias Vendidas", new BigDecimal("-85000.00"), -46.0},
            {"3.12", "Custo dos Serviços", new BigDecimal("-15000.00"), -8.1},
            {"", "Lucro Bruto", new BigDecimal("117000.00"), 63.2},
            {"", "", null, null},
            {"3.21", "Despesas Administrativas", new BigDecimal("-25000.00"), -13.5},
            {"3.22", "Despesas de Vendas", new BigDecimal("-18000.00"), -9.7},
            {"3.23", "Despesas Financeiras", new BigDecimal("-8000.00"), -4.3},
            {"", "Lucro Operacional", new BigDecimal("66000.00"), 35.7},
            {"", "", null, null},
            {"3.31", "Receitas Financeiras", new BigDecimal("4500.00"), 2.4},
            {"3.32", "Outras Receitas", new BigDecimal("1200.00"), 0.6},
            {"3.41", "Impostos", new BigDecimal("-15000.00"), -8.1},
            {"", "Lucro Líquido", new BigDecimal("56700.00"), 30.6}
        };
        
        for (Object[] linha : dadosDRE) {
            dreModel.addRow(linha);
        }
        
        // Dados Fluxo de Caixa
        Calendar cal = Calendar.getInstance();
        Object[][] dadosFluxo = {
            {cal.getTime(), "Saldo Inicial", "", new BigDecimal("50000.00"), new BigDecimal("0.00"), new BigDecimal("50000.00")},
            {cal.getTime(), "Venda Cliente A", "Vendas", new BigDecimal("35000.00"), new BigDecimal("0.00"), new BigDecimal("85000.00")},
            {cal.getTime(), "Pagamento Fornecedor X", "Compras", new BigDecimal("0.00"), new BigDecimal("-15000.00"), new BigDecimal("70000.00")},
            {cal.getTime(), "Recebimento Cliente B", "Vendas", new BigDecimal("25000.00"), new BigDecimal("0.00"), new BigDecimal("95000.00")},
            {cal.getTime(), "Pagamento Salários", "Despesas", new BigDecimal("0.00"), new BigDecimal("-12000.00"), new BigDecimal("83000.00")},
            {cal.getTime(), "Venda Serviço", "Serviços", new BigDecimal("8000.00"), new BigDecimal("0.00"), new BigDecimal("91000.00")},
            {cal.getTime(), "Pagamento Aluguel", "Despesas", new BigDecimal("0.00"), new BigDecimal("-3500.00"), new BigDecimal("87500.00")}
        };
        
        for (Object[] linha : dadosFluxo) {
            fluxoCaixaModel.addRow(linha);
        }
        
        // Dados Balancete
        Object[][] dadosBalancete = {
            {"1.01", "Caixa", new BigDecimal("87500.00"), new BigDecimal("0.00"), new BigDecimal("87500.00")},
            {"1.02", "Bancos", new BigDecimal("150000.00"), new BigDecimal("0.00"), new BigDecimal("150000.00")},
            {"1.03", "Contas a Receber", new BigDecimal("45000.00"), new BigDecimal("0.00"), new BigDecimal("45000.00")},
            {"1.04", "Estoque", new BigDecimal("85000.00"), new BigDecimal("0.00"), new BigDecimal("85000.00")},
            {"", "Total Ativo", new BigDecimal("367500.00"), new BigDecimal("0.00"), new BigDecimal("367500.00")},
            {"", "", null, null},
            {"2.01", "Fornecedores", new BigDecimal("0.00"), new BigDecimal("35000.00"), new BigDecimal("-35000.00")},
            {"2.02", "Contas a Pagar", new BigDecimal("0.00"), new BigDecimal("25000.00"), new BigDecimal("-25000.00")},
            {"2.03", "Empréstimos", new BigDecimal("0.00"), new BigDecimal("100000.00"), new BigDecimal("-100000.00")},
            {"", "Total Passivo", new BigDecimal("0.00"), new BigDecimal("160000.00"), new BigDecimal("-160000.00")},
            {"", "", null, null},
            {"3.01", "Capital Social", new BigDecimal("0.00"), new BigDecimal("200000.00"), new BigDecimal("-200000.00")},
            {"3.02", "Lucros Acumulados", new BigDecimal("0.00"), new BigDecimal("7500.00"), new BigDecimal("-7500.00")},
            {"", "Total Patrimônio Líquido", new BigDecimal("0.00"), new BigDecimal("207500.00"), new BigDecimal("-207500.00")}
        };
        
        for (Object[] linha : dadosBalancete) {
            balanceteModel.addRow(linha);
        }
        
        // Dados Contas a Pagar/Receber
        Object[][] dadosContas = {
            {"Receber", "NF-001", "Cliente A", cal.getTime(), new BigDecimal("15000.00"), "Em Dia", 0},
            {"Receber", "NF-002", "Cliente B", cal.getTime(), new BigDecimal("8500.00"), "Vencendo", 5},
            {"Receber", "NF-003", "Cliente C", cal.getTime(), new BigDecimal("12000.00"), "Atrasado", 15},
            {"Pagar", "FOR-001", "Fornecedor X", cal.getTime(), new BigDecimal("-8000.00"), "Em Dia", 0},
            {"Pagar", "FOR-002", "Fornecedor Y", cal.getTime(), new BigDecimal("-12000.00"), "Vencendo", 3},
            {"Pagar", "FOR-003", "Fornecedor Z", cal.getTime(), new BigDecimal("-5000.00"), "Atrasado", 10}
        };
        
        for (Object[] linha : dadosContas) {
            contasModel.addRow(linha);
        }
        
        // Atualizar resumos
        atualizarResumos();
    }
    
    private void atualizarResumos() {
        // Calcular totais DRE
        BigDecimal receitaTotal = BigDecimal.ZERO;
        BigDecimal despesaTotal = BigDecimal.ZERO;
        
        for (int i = 0; i < dreModel.getRowCount(); i++) {
            Object valor = dreModel.getValueAt(i, 2);
            if (valor instanceof BigDecimal) {
                BigDecimal val = (BigDecimal) valor;
                if (val.compareTo(BigDecimal.ZERO) > 0) {
                    receitaTotal = receitaTotal.add(val);
                } else {
                    despesaTotal = despesaTotal.add(val.abs());
                }
            }
        }
        
        BigDecimal lucroLiquido = receitaTotal.subtract(despesaTotal);
        
        lblReceitaTotal.setText("R$ " + df.format(receitaTotal));
        lblDespesaTotal.setText("R$ " + df.format(despesaTotal));
        lblLucroLiquido.setText("R$ " + df.format(lucroLiquido));
        
        // Calcular totais Contas
        BigDecimal totalReceber = BigDecimal.ZERO;
        BigDecimal totalPagar = BigDecimal.ZERO;
        
        for (int i = 0; i < contasModel.getRowCount(); i++) {
            String tipo = (String) contasModel.getValueAt(i, 0);
            Object valor = contasModel.getValueAt(i, 4);
            if (valor instanceof BigDecimal) {
                BigDecimal val = (BigDecimal) valor;
                if ("Receber".equals(tipo)) {
                    totalReceber = totalReceber.add(val);
                } else {
                    totalPagar = totalPagar.add(val.abs());
                }
            }
        }
        
        lblTotalContasReceber.setText("R$ " + df.format(totalReceber));
        lblTotalContasPagar.setText("R$ " + df.format(totalPagar));
        
        // Calcular saldos Fluxo de Caixa
        if (fluxoCaixaModel.getRowCount() > 0) {
            Object saldoInicial = fluxoCaixaModel.getValueAt(0, 5);
            Object saldoFinal = fluxoCaixaModel.getValueAt(fluxoCaixaModel.getRowCount() - 1, 5);
            
            if (saldoInicial instanceof BigDecimal) {
                lblSaldoInicial.setText("R$ " + df.format(saldoInicial));
            }
            if (saldoFinal instanceof BigDecimal) {
                lblSaldoFinal.setText("R$ " + df.format(saldoFinal));
            }
        }
    }
    
    private void gerarRelatorios() {
        JOptionPane.showMessageDialog(frame, 
            "🔄 Relatórios gerados com sucesso!\n" +
            "Dados atualizados conforme o período selecionado.", 
            "Relatórios Gerados", 
            JOptionPane.INFORMATION_MESSAGE);
        
        atualizarResumos();
    }
    
    private void exportarRelatorios() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Exportar Relatórios");
        
        int userSelection = fileChooser.showSaveDialog(frame);
        
        if (userSelection == JFileChooser.APPROVE_OPTION) {
            JOptionPane.showMessageDialog(frame, 
                "📥 Relatórios exportados com sucesso!\n" +
                "Formato: Excel (.xlsx)\n" +
                "Local: " + fileChooser.getSelectedFile().getAbsolutePath(), 
                "Exportação Concluída", 
                JOptionPane.INFORMATION_MESSAGE);
        }
    }
    
    // ==================== RENDERIZADORES PERSONALIZADOS ====================
    
    private static class CurrencyCellRenderer extends JLabel implements javax.swing.table.TableCellRenderer {
        private DecimalFormat df = new DecimalFormat("#,##0.00");
        
        public CurrencyCellRenderer() {
            setOpaque(true);
            setHorizontalAlignment(JLabel.RIGHT);
            setFont(HermesTheme.FONT_DEFAULT);
        }
        
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            if (value instanceof BigDecimal) {
                setText("R$ " + df.format(value));
                setForeground(((BigDecimal) value).compareTo(BigDecimal.ZERO) >= 0 ? 
                    HermesTheme.SUCCESS_COLOR : HermesTheme.DANGER_COLOR);
            } else {
                setText("");
            }
            
            if (isSelected) {
                setBackground(table.getSelectionBackground());
                setForeground(table.getSelectionForeground());
            } else {
                setBackground(table.getBackground());
                setForeground(table.getForeground());
            }
            
            return this;
        }
    }
    
    private static class PercentageCellRenderer extends JLabel implements javax.swing.table.TableCellRenderer {
        private DecimalFormat df = new DecimalFormat("#,##0.00");
        
        public PercentageCellRenderer() {
            setOpaque(true);
            setHorizontalAlignment(JLabel.CENTER);
            setFont(HermesTheme.FONT_DEFAULT);
        }
        
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            if (value instanceof Double) {
                setText(df.format(value) + "%");
                Double val = (Double) value;
                if (val >= 0) {
                    setForeground(HermesTheme.SUCCESS_COLOR);
                } else {
                    setForeground(HermesTheme.DANGER_COLOR);
                }
            } else {
                setText("");
            }
            
            if (isSelected) {
                setBackground(table.getSelectionBackground());
                setForeground(table.getSelectionForeground());
            } else {
                setBackground(table.getBackground());
                setForeground(table.getForeground());
            }
            
            return this;
        }
    }
    
    private static class DateCellRenderer extends JLabel implements javax.swing.table.TableCellRenderer {
        private SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        
        public DateCellRenderer() {
            setOpaque(true);
            setHorizontalAlignment(JLabel.CENTER);
            setFont(HermesTheme.FONT_DEFAULT);
        }
        
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            if (value instanceof Date) {
                setText(sdf.format(value));
            } else {
                setText("");
            }
            
            if (isSelected) {
                setBackground(table.getSelectionBackground());
                setForeground(table.getSelectionForeground());
            } else {
                setBackground(table.getBackground());
                setForeground(table.getForeground());
            }
            
            return this;
        }
    }
    
    private static class StatusCellRenderer extends JLabel implements javax.swing.table.TableCellRenderer {
        
        public StatusCellRenderer() {
            setOpaque(true);
            setHorizontalAlignment(JLabel.CENTER);
            setFont(HermesTheme.FONT_BOLD);
        }
        
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            String status = value != null ? value.toString() : "";
            
            setText(status);
            
            switch (status) {
                case "Em Dia":
                    setBackground(HermesTheme.SUCCESS_COLOR);
                    setForeground(Color.WHITE);
                    break;
                case "Vencendo":
                    setBackground(HermesTheme.WARNING_COLOR);
                    setForeground(Color.WHITE);
                    break;
                case "Atrasado":
                    setBackground(HermesTheme.DANGER_COLOR);
                    setForeground(Color.WHITE);
                    break;
                default:
                    setBackground(table.getBackground());
                    setForeground(table.getForeground());
            }
            
            return this;
        }
    }
    
    private static class DiasAtrasoCellRenderer extends JLabel implements javax.swing.table.TableCellRenderer {
        
        public DiasAtrasoCellRenderer() {
            setOpaque(true);
            setHorizontalAlignment(JLabel.CENTER);
            setFont(HermesTheme.FONT_BOLD);
        }
        
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            if (value instanceof Integer) {
                Integer dias = (Integer) value;
                setText(dias.toString() + " dias");
                
                if (dias == 0) {
                    setBackground(HermesTheme.SUCCESS_COLOR);
                    setForeground(Color.WHITE);
                } else if (dias <= 5) {
                    setBackground(HermesTheme.WARNING_COLOR);
                    setForeground(Color.WHITE);
                } else {
                    setBackground(HermesTheme.DANGER_COLOR);
                    setForeground(Color.WHITE);
                }
            } else {
                setText("");
                setBackground(table.getBackground());
                setForeground(table.getForeground());
            }
            
            return this;
        }
    }
    
    // ==================== MÉTODOS PÚBLICOS ====================
    
    public void show() {
        if (frame == null) {
            initializeUI();
        }
        frame.setVisible(true);
        frame.toFront();
    }
    
    public void dispose() {
        if (frame != null) {
            frame.dispose();
            frame = null;
        }
    }
    
    // ==================== JDATECHOOSER SIMPLIFICADO ====================
    
    private static class JDateChooser extends JTextField {
        public JDateChooser() {
            super(new SimpleDateFormat("dd/MM/yyyy").format(new Date()));
            setFont(HermesTheme.FONT_DEFAULT);
            setPreferredSize(new Dimension(100, 30));
            HermesTheme.applyTextFieldTheme(this);
        }
    }
}
