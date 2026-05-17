package com.br.hermescomercial.pdv.controller;

import com.br.hermescomercial.ui.layout.LayoutPadrao;
import com.br.hermescomercial.util.SystemLogger;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.print.PrinterJob;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * Classe especializada para formulário de relatórios do sistema
 * Segue o padrão Header → Busca → Tabela → Ações
 * Versão 1.0.0 - Adaptada para PDVMenuLateralElegante
 */
public class PDVFormularioRelatorios {
    
    private JPanel workArea;
    private String usuarioAtual;
    private String nomeUsuario;
    
    // Componentes do formulário
    private JTabbedPane tabbedPane;
    private JComboBox<String> cbTipoRelatorio;
    private JTextField txtDateInicio;
    private JTextField txtDateFim;
    private JTextArea txtRelatorio;
    
    // Cores
    private static final Color SUCCESS_COLOR = new Color(76, 175, 80);
    private static final Color DANGER_COLOR = new Color(244, 67, 54);
    private static final Color WARNING_COLOR = new Color(255, 193, 7);
    private static final Color PRIMARY_COLOR = new Color(33, 150, 243);
    
    public PDVFormularioRelatorios(JPanel workArea, String usuario, String nome) {
        this.workArea = workArea;
        this.usuarioAtual = usuario;
        this.nomeUsuario = nome;
    }
    
    public JPanel criarFormularioRelatorios() {
        SystemLogger.ui("=== CRIANDO FORMULÁRIO RELATÓRIOS ===");
        SystemLogger.ui("Usuário: " + usuarioAtual);
        
        JPanel painelPrincipal = new JPanel(new BorderLayout());
        painelPrincipal.setBackground(Color.WHITE);
        
        // Header
        painelPrincipal.add(criarHeader(), BorderLayout.NORTH);
        
        // Conteúdo principal com abas
        JPanel conteudo = new JPanel(new BorderLayout());
        conteudo.setBackground(Color.WHITE);
        
        // Abas
        conteudo.add(criarAbas(), BorderLayout.CENTER);
        
        // Painel de ações
        conteudo.add(criarPainelAcoes(), BorderLayout.SOUTH);
        
        painelPrincipal.add(conteudo, BorderLayout.CENTER);
        
        SystemLogger.ui("Formulário Relatórios criado com sucesso");
        return painelPrincipal;
    }
    
    private JPanel criarHeader() {
        JPanel header = LayoutPadrao.criarHeaderPanel("📊 Relatórios e Análises");
        
        // Informações do usuário
        JPanel userInfo = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        userInfo.setBackground(new Color(250, 250, 250));
        userInfo.add(new JLabel("👤 " + nomeUsuario));
        userInfo.add(new JLabel(" | "));
        userInfo.add(new JLabel("📅 " + LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))));
        
        header.add(userInfo, BorderLayout.EAST);
        return header;
    }
    
    private JTabbedPane criarAbas() {
        tabbedPane = new JTabbedPane();
        
        // Aba de Vendas do Dia
        tabbedPane.addTab("📊 Vendas do Dia", criarAbaVendasDia());
        
        // Aba de Vendas do Mês
        tabbedPane.addTab("📅 Vendas do Mês", criarAbaVendasMes());
        
        // Aba de Vendas por Produto
        tabbedPane.addTab("📦 Vendas por Produto", criarAbaVendasProduto());
        
        // Aba de Vendas por Vendedor
        tabbedPane.addTab("👤 Vendas por Vendedor", criarAbaVendasVendedor());
        
        // Aba de Relatório Financeiro
        tabbedPane.addTab("💰 Relatório Financeiro", criarAbaRelatorioFinanceiro());
        
        // Aba de Relatório de Estoque
        tabbedPane.addTab("📦 Relatório de Estoque", criarAbaRelatorioEstoque());
        
        return tabbedPane;
    }
    
    private JPanel criarAbaVendasDia() {
        JPanel painel = new JPanel(new BorderLayout());
        painel.setBackground(Color.WHITE);
        painel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        // Painel de filtros
        JPanel filtroPanel = new JPanel(new GridBagLayout());
        filtroPanel.setBackground(Color.WHITE);
        filtroPanel.setBorder(BorderFactory.createTitledBorder("🔍 Filtros"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.anchor = GridBagConstraints.WEST;
        
        // Data
        gbc.gridx = 0; gbc.gridy = 0;
        filtroPanel.add(new JLabel("Data:"), gbc);
        gbc.gridx = 1; gbc.fill = GridBagConstraints.HORIZONTAL; gbc.weightx = 1.0;
        JTextField txtData = new JTextField(LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")), 15);
        filtroPanel.add(txtData, gbc);
        
        // Botões
        gbc.gridx = 2; gbc.gridy = 0; gbc.fill = GridBagConstraints.NONE; gbc.weightx = 0;
        JButton btnBuscar = LayoutPadrao.criarBotaoPrimario("🔍 Buscar");
        btnBuscar.addActionListener(e -> buscarVendasDia(txtData.getText()));
        filtroPanel.add(btnBuscar, gbc);
        
        gbc.gridx = 3;
        JButton btnLimpar = LayoutPadrao.criarBotaoSecundario("🔄 Limpar");
        btnLimpar.addActionListener(e -> limparFiltros());
        filtroPanel.add(btnLimpar, gbc);
        
        painel.add(filtroPanel, BorderLayout.NORTH);
        
        // Tabela de vendas
        String[] colunas = {"ID", "Hora", "Cliente", "Valor", "Forma Pgto", "Status", "Vendedor"};
        DefaultTableModel model = new DefaultTableModel(colunas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        // Dados de exemplo
        Object[][] dados = {
            {"001", "08:30", "João Silva", "R$ 123,45", "Dinheiro", "Concluída", "Maria"},
            {"002", "09:15", "Maria Santos", "R$ 67,89", "Cartão", "Concluída", "João"},
            {"003", "10:20", "Pedro Costa", "R$ 234,56", "Pix", "Concluída", "Ana"},
            {"004", "11:10", "Ana Oliveira", "R$ 89,90", "Dinheiro", "Concluída", "Carlos"},
            {"005", "14:25", "Carlos Ferreira", "R$ 456,78", "Cartão", "Concluída", "Maria"}
        };
        
        for (Object[] row : dados) {
            model.addRow(row);
        }
        
        JTable tabela = new JTable(model);
        tabela.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tabela.getTableHeader().setReorderingAllowed(false);
        tabela.setRowHeight(25);
        
        JScrollPane scrollPane = new JScrollPane(tabela);
        scrollPane.setPreferredSize(new Dimension(0, 300));
        
        painel.add(scrollPane, BorderLayout.CENTER);
        
        // Painel de resumo
        JPanel resumoPanel = new JPanel(new GridLayout(1, 4, 10, 10));
        resumoPanel.setBackground(Color.WHITE);
        resumoPanel.setBorder(BorderFactory.createTitledBorder("📊 Resumo do Dia"));
        
        resumoPanel.add(criarInfoBox("Total Vendas", "R$ 972,58", SUCCESS_COLOR));
        resumoPanel.add(criarInfoBox("Total Pedidos", "5", PRIMARY_COLOR));
        resumoPanel.add(criarInfoBox("Ticket Médio", "R$ 194,52", WARNING_COLOR));
        resumoPanel.add(criarInfoBox("Meta Atingida", "65%", DANGER_COLOR));
        
        painel.add(resumoPanel, BorderLayout.SOUTH);
        
        return painel;
    }
    
    private JPanel criarAbaVendasMes() {
        JPanel painel = new JPanel(new BorderLayout());
        painel.setBackground(Color.WHITE);
        painel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        // Painel de filtros
        JPanel filtroPanel = new JPanel(new GridBagLayout());
        filtroPanel.setBackground(Color.WHITE);
        filtroPanel.setBorder(BorderFactory.createTitledBorder("🔍 Filtros do Mês"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.anchor = GridBagConstraints.WEST;
        
        // Mês/Ano
        gbc.gridx = 0; gbc.gridy = 0;
        filtroPanel.add(new JLabel("Mês/Ano:"), gbc);
        gbc.gridx = 1; gbc.fill = GridBagConstraints.HORIZONTAL; gbc.weightx = 1.0;
        JTextField txtMesAno = new JTextField(LocalDate.now().format(DateTimeFormatter.ofPattern("MM/yyyy")), 10);
        filtroPanel.add(txtMesAno, gbc);
        
        // Vendedor
        gbc.gridx = 2; gbc.gridy = 0; gbc.fill = GridBagConstraints.NONE; gbc.weightx = 0;
        filtroPanel.add(new JLabel("Vendedor:"), gbc);
        gbc.gridx = 3; gbc.fill = GridBagConstraints.HORIZONTAL; gbc.weightx = 1.0;
        JComboBox<String> cbVendedor = new JComboBox<>(new String[]{"Todos", "Maria", "João", "Ana", "Carlos"});
        filtroPanel.add(cbVendedor, gbc);
        
        // Botões
        gbc.gridx = 4;
        JButton btnBuscar = LayoutPadrao.criarBotaoPrimario("🔍 Buscar");
        btnBuscar.addActionListener(e -> buscarVendasMes(txtMesAno.getText(), (String) cbVendedor.getSelectedItem()));
        filtroPanel.add(btnBuscar, gbc);
        
        gbc.gridx = 5;
        JButton btnLimpar = LayoutPadrao.criarBotaoSecundario("🔄 Limpar");
        btnLimpar.addActionListener(e -> limparFiltros());
        filtroPanel.add(btnLimpar, gbc);
        
        painel.add(filtroPanel, BorderLayout.NORTH);
        
        // Tabela de resumo mensal
        String[] colunas = {"Dia", "Total Vendas", "Qtd Pedidos", "Ticket Médio", "Meta", "% Atingido"};
        DefaultTableModel model = new DefaultTableModel(colunas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        // Dados de exemplo
        Object[][] dados = {
            {"01/05", "R$ 1.234,56", "23", "R$ 53,68", "R$ 1.200,00", "103%"},
            {"02/05", "R$ 987,45", "18", "R$ 54,86", "R$ 1.200,00", "82%"},
            {"03/05", "R$ 1.456,78", "31", "R$ 47,02", "R$ 1.200,00", "121%"},
            {"04/05", "R$ 876,23", "15", "R$ 58,41", "R$ 1.200,00", "73%"},
            {"05/05", "R$ 1.123,89", "25", "R$ 44,96", "R$ 1.200,00", "94%"}
        };
        
        for (Object[] row : dados) {
            model.addRow(row);
        }
        
        JTable tabela = new JTable(model);
        tabela.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tabela.getTableHeader().setReorderingAllowed(false);
        tabela.setRowHeight(25);
        
        JScrollPane scrollPane = new JScrollPane(tabela);
        scrollPane.setPreferredSize(new Dimension(0, 300));
        
        painel.add(scrollPane, BorderLayout.CENTER);
        
        return painel;
    }
    
    private JPanel criarAbaVendasProduto() {
        JPanel painel = new JPanel(new BorderLayout());
        painel.setBackground(Color.WHITE);
        painel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        // Painel de filtros
        JPanel filtroPanel = new JPanel(new GridBagLayout());
        filtroPanel.setBackground(Color.WHITE);
        filtroPanel.setBorder(BorderFactory.createTitledBorder("🔍 Filtros de Produtos"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.anchor = GridBagConstraints.WEST;
        
        // Período
        gbc.gridx = 0; gbc.gridy = 0;
        filtroPanel.add(new JLabel("Período:"), gbc);
        gbc.gridx = 1; gbc.fill = GridBagConstraints.HORIZONTAL; gbc.weightx = 1.0;
        cbTipoRelatorio = new JComboBox<>(new String[]{"Hoje", "Semana", "Mês", "Personalizado"});
        filtroPanel.add(cbTipoRelatorio, gbc);
        
        // Categoria
        gbc.gridx = 2; gbc.gridy = 0; gbc.fill = GridBagConstraints.NONE; gbc.weightx = 0;
        filtroPanel.add(new JLabel("Categoria:"), gbc);
        gbc.gridx = 3; gbc.fill = GridBagConstraints.HORIZONTAL; gbc.weightx = 1.0;
        JComboBox<String> cbCategoria = new JComboBox<>(new String[]{"Todas", "Alimentos", "Bebidas", "Limpeza", "Outros"});
        filtroPanel.add(cbCategoria, gbc);
        
        // Botões
        gbc.gridx = 4;
        JButton btnBuscar = LayoutPadrao.criarBotaoPrimario("🔍 Buscar");
        btnBuscar.addActionListener(e -> buscarVendasProduto());
        filtroPanel.add(btnBuscar, gbc);
        
        gbc.gridx = 5;
        JButton btnLimpar = LayoutPadrao.criarBotaoSecundario("🔄 Limpar");
        btnLimpar.addActionListener(e -> limparFiltros());
        filtroPanel.add(btnLimpar, gbc);
        
        painel.add(filtroPanel, BorderLayout.NORTH);
        
        // Tabela de produtos
        String[] colunas = {"Código", "Descrição", "Categoria", "Qtd Vendida", "Total Vendido", "Estoque Atual"};
        DefaultTableModel model = new DefaultTableModel(colunas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        // Dados de exemplo
        Object[][] dados = {
            {"001", "Arroz 5kg", "Alimentos", "25", "R$ 649,75", "50"},
            {"002", "Feijão 1kg", "Alimentos", "18", "R$ 153,00", "30"},
            {"003", "Óleo 900ml", "Alimentos", "12", "R$ 86,40", "5"},
            {"004", "Refrigerante 2L", "Bebidas", "35", "R$ 420,00", "20"},
            {"005", "Água 500ml", "Bebidas", "45", "R$ 112,50", "0"},
            {"006", "Detergente 500ml", "Limpeza", "8", "R$ 34,40", "15"}
        };
        
        for (Object[] row : dados) {
            model.addRow(row);
        }
        
        JTable tabela = new JTable(model);
        tabela.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tabela.getTableHeader().setReorderingAllowed(false);
        tabela.setRowHeight(25);
        
        JScrollPane scrollPane = new JScrollPane(tabela);
        scrollPane.setPreferredSize(new Dimension(0, 300));
        
        painel.add(scrollPane, BorderLayout.CENTER);
        
        return painel;
    }
    
    private JPanel criarAbaVendasVendedor() {
        JPanel painel = new JPanel(new BorderLayout());
        painel.setBackground(Color.WHITE);
        painel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        // Painel de filtros
        JPanel filtroPanel = new JPanel(new GridBagLayout());
        filtroPanel.setBackground(Color.WHITE);
        filtroPanel.setBorder(BorderFactory.createTitledBorder("🔍 Filtros por Vendedor"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.anchor = GridBagConstraints.WEST;
        
        // Vendedor
        gbc.gridx = 0; gbc.gridy = 0;
        filtroPanel.add(new JLabel("Vendedor:"), gbc);
        gbc.gridx = 1; gbc.fill = GridBagConstraints.HORIZONTAL; gbc.weightx = 1.0;
        JComboBox<String> cbVendedor = new JComboBox<>(new String[]{"Maria", "João", "Ana", "Carlos"});
        filtroPanel.add(cbVendedor, gbc);
        
        // Período
        gbc.gridx = 2; gbc.gridy = 0; gbc.fill = GridBagConstraints.NONE; gbc.weightx = 0;
        filtroPanel.add(new JLabel("Período:"), gbc);
        gbc.gridx = 3; gbc.fill = GridBagConstraints.HORIZONTAL; gbc.weightx = 1.0;
        JComboBox<String> cbPeriodo = new JComboBox<>(new String[]{"Hoje", "Semana", "Mês"});
        filtroPanel.add(cbPeriodo, gbc);
        
        // Botões
        gbc.gridx = 4;
        JButton btnBuscar = LayoutPadrao.criarBotaoPrimario("🔍 Buscar");
        btnBuscar.addActionListener(e -> buscarVendasVendedor());
        filtroPanel.add(btnBuscar, gbc);
        
        gbc.gridx = 5;
        JButton btnLimpar = LayoutPadrao.criarBotaoSecundario("🔄 Limpar");
        btnLimpar.addActionListener(e -> limparFiltros());
        filtroPanel.add(btnLimpar, gbc);
        
        painel.add(filtroPanel, BorderLayout.NORTH);
        
        // Tabela de vendas por vendedor
        String[] colunas = {"Data", "ID Venda", "Cliente", "Valor", "Forma Pgto", "Comissão", "Status"};
        DefaultTableModel model = new DefaultTableModel(colunas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        // Dados de exemplo
        Object[][] dados = {
            {"09/05", "001", "João Silva", "R$ 123,45", "Dinheiro", "R$ 6,17", "Concluída"},
            {"09/05", "002", "Maria Santos", "R$ 67,89", "Cartão", "R$ 3,39", "Concluída"},
            {"09/05", "003", "Pedro Costa", "R$ 234,56", "Pix", "R$ 11,73", "Concluída"},
            {"09/05", "004", "Ana Oliveira", "R$ 89,90", "Dinheiro", "R$ 4,50", "Concluída"},
            {"09/05", "005", "Carlos Ferreira", "R$ 456,78", "Cartão", "R$ 22,84", "Concluída"}
        };
        
        for (Object[] row : dados) {
            model.addRow(row);
        }
        
        JTable tabela = new JTable(model);
        tabela.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tabela.getTableHeader().setReorderingAllowed(false);
        tabela.setRowHeight(25);
        
        JScrollPane scrollPane = new JScrollPane(tabela);
        scrollPane.setPreferredSize(new Dimension(0, 300));
        
        painel.add(scrollPane, BorderLayout.CENTER);
        
        // Painel de resumo do vendedor
        JPanel resumoPanel = new JPanel(new GridLayout(1, 4, 10, 10));
        resumoPanel.setBackground(Color.WHITE);
        resumoPanel.setBorder(BorderFactory.createTitledBorder("📊 Resumo do Vendedor"));
        
        resumoPanel.add(criarInfoBox("Total Vendas", "R$ 972,58", SUCCESS_COLOR));
        resumoPanel.add(criarInfoBox("Total Comissões", "R$ 48,63", PRIMARY_COLOR));
        resumoPanel.add(criarInfoBox("Qtd Pedidos", "5", WARNING_COLOR));
        resumoPanel.add(criarInfoBox("Ticket Médio", "R$ 194,52", DANGER_COLOR));
        
        painel.add(resumoPanel, BorderLayout.SOUTH);
        
        return painel;
    }
    
    private JPanel criarAbaRelatorioFinanceiro() {
        JPanel painel = new JPanel(new BorderLayout());
        painel.setBackground(Color.WHITE);
        painel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        // Painel de filtros
        JPanel filtroPanel = new JPanel(new GridBagLayout());
        filtroPanel.setBackground(Color.WHITE);
        filtroPanel.setBorder(BorderFactory.createTitledBorder("🔍 Filtros Financeiros"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.anchor = GridBagConstraints.WEST;
        
        // Data Início
        gbc.gridx = 0; gbc.gridy = 0;
        filtroPanel.add(new JLabel("Data Início:"), gbc);
        gbc.gridx = 1; gbc.fill = GridBagConstraints.HORIZONTAL; gbc.weightx = 1.0;
        txtDateInicio = new JTextField(LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")), 10);
        filtroPanel.add(txtDateInicio, gbc);
        
        // Data Fim
        gbc.gridx = 2; gbc.gridy = 0; gbc.fill = GridBagConstraints.NONE; gbc.weightx = 0;
        filtroPanel.add(new JLabel("Data Fim:"), gbc);
        gbc.gridx = 3; gbc.fill = GridBagConstraints.HORIZONTAL; gbc.weightx = 1.0;
        txtDateFim = new JTextField(LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")), 10);
        filtroPanel.add(txtDateFim, gbc);
        
        // Tipo
        gbc.gridx = 4;
        filtroPanel.add(new JLabel("Tipo:"), gbc);
        gbc.gridx = 5; gbc.fill = GridBagConstraints.HORIZONTAL; gbc.weightx = 1.0;
        JComboBox<String> cbTipo = new JComboBox<>(new String[]{"Todos", "Receitas", "Despesas", "Lucro"});
        filtroPanel.add(cbTipo, gbc);
        
        // Botões
        gbc.gridx = 6;
        JButton btnBuscar = LayoutPadrao.criarBotaoPrimario("🔍 Buscar");
        btnBuscar.addActionListener(e -> buscarRelatorioFinanceiro());
        filtroPanel.add(btnBuscar, gbc);
        
        gbc.gridx = 7;
        JButton btnLimpar = LayoutPadrao.criarBotaoSecundario("🔄 Limpar");
        btnLimpar.addActionListener(e -> limparFiltros());
        filtroPanel.add(btnLimpar, gbc);
        
        painel.add(filtroPanel, BorderLayout.NORTH);
        
        // Área de texto para relatório detalhado
        txtRelatorio = new JTextArea(15, 50);
        txtRelatorio.setEditable(false);
        txtRelatorio.setFont(new Font("Monospaced", Font.PLAIN, 12));
        txtRelatorio.setText("=== RELATÓRIO FINANCEIRO DETALHADO ===\n\n" +
                           "Período: " + txtDateInicio.getText() + " a " + txtDateFim.getText() + "\n\n" +
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
                           "Versão PDVMenuLateralElegante");
        
        JScrollPane scrollPane = new JScrollPane(txtRelatorio);
        painel.add(scrollPane, BorderLayout.CENTER);
        
        return painel;
    }
    
    private JPanel criarAbaRelatorioEstoque() {
        JPanel painel = new JPanel(new BorderLayout());
        painel.setBackground(Color.WHITE);
        painel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        // Painel de filtros
        JPanel filtroPanel = new JPanel(new GridBagLayout());
        filtroPanel.setBackground(Color.WHITE);
        filtroPanel.setBorder(BorderFactory.createTitledBorder("🔍 Filtros de Estoque"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.anchor = GridBagConstraints.WEST;
        
        // Categoria
        gbc.gridx = 0; gbc.gridy = 0;
        filtroPanel.add(new JLabel("Categoria:"), gbc);
        gbc.gridx = 1; gbc.fill = GridBagConstraints.HORIZONTAL; gbc.weightx = 1.0;
        JComboBox<String> cbCategoria = new JComboBox<>(new String[]{"Todas", "Alimentos", "Bebidas", "Limpeza", "Outros"});
        filtroPanel.add(cbCategoria, gbc);
        
        // Status
        gbc.gridx = 2; gbc.gridy = 0; gbc.fill = GridBagConstraints.NONE; gbc.weightx = 0;
        filtroPanel.add(new JLabel("Status:"), gbc);
        gbc.gridx = 3; gbc.fill = GridBagConstraints.HORIZONTAL; gbc.weightx = 1.0;
        JComboBox<String> cbStatus = new JComboBox<>(new String[]{"Todos", "Em Estoque", "Estoque Baixo", "Sem Estoque"});
        filtroPanel.add(cbStatus, gbc);
        
        // Botões
        gbc.gridx = 4;
        JButton btnBuscar = LayoutPadrao.criarBotaoPrimario("🔍 Buscar");
        btnBuscar.addActionListener(e -> buscarRelatorioEstoque());
        filtroPanel.add(btnBuscar, gbc);
        
        gbc.gridx = 5;
        JButton btnLimpar = LayoutPadrao.criarBotaoSecundario("🔄 Limpar");
        btnLimpar.addActionListener(e -> limparFiltros());
        filtroPanel.add(btnLimpar, gbc);
        
        painel.add(filtroPanel, BorderLayout.NORTH);
        
        // Tabela de estoque
        String[] colunas = {"Código", "Descrição", "Categoria", "Estoque Atual", "Estoque Mínimo", "Status", "Valor Total"};
        DefaultTableModel model = new DefaultTableModel(colunas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        // Dados de exemplo
        Object[][] dados = {
            {"001", "Arroz 5kg", "Alimentos", "50", "20", "✅ Normal", "R$ 1.299,50"},
            {"002", "Feijão 1kg", "Alimentos", "30", "25", "⚠️ Baixo", "R$ 255,00"},
            {"003", "Óleo 900ml", "Alimentos", "5", "10", "❌ Crítico", "R$ 36,00"},
            {"004", "Refrigerante 2L", "Bebidas", "20", "15", "✅ Normal", "R$ 240,00"},
            {"005", "Água 500ml", "Bebidas", "0", "20", "❌ Sem Estoque", "R$ 0,00"},
            {"006", "Detergente 500ml", "Limpeza", "15", "10", "✅ Normal", "R$ 64,50"}
        };
        
        for (Object[] row : dados) {
            model.addRow(row);
        }
        
        JTable tabela = new JTable(model);
        tabela.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tabela.getTableHeader().setReorderingAllowed(false);
        tabela.setRowHeight(25);
        
        JScrollPane scrollPane = new JScrollPane(tabela);
        scrollPane.setPreferredSize(new Dimension(0, 300));
        
        painel.add(scrollPane, BorderLayout.CENTER);
        
        return painel;
    }
    
    private JPanel criarPainelAcoes() {
        JPanel painelAcoes = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        painelAcoes.setBackground(Color.WHITE);
        painelAcoes.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        JButton btnGerarRelatorio = LayoutPadrao.criarBotaoSucesso("📊 Gerar Relatório");
        btnGerarRelatorio.addActionListener(e -> gerarRelatorio());
        
        JButton btnExportarPDF = LayoutPadrao.criarBotaoPrimario("📤 Exportar PDF");
        btnExportarPDF.addActionListener(e -> exportarPDF());
        
        JButton btnImprimir = LayoutPadrao.criarBotaoSecundario("🖨️ Imprimir");
        btnImprimir.addActionListener(e -> imprimirRelatorio());
        
        JButton btnCopiar = LayoutPadrao.criarBotaoAlerta("📋 Copiar");
        btnCopiar.addActionListener(e -> copiarRelatorio());
        
        JButton btnLimpar = LayoutPadrao.criarBotaoPerigo("🔄 Limpar");
        btnLimpar.addActionListener(e -> limparRelatorio());
        
        painelAcoes.add(btnGerarRelatorio);
        painelAcoes.add(btnExportarPDF);
        painelAcoes.add(btnImprimir);
        painelAcoes.add(btnCopiar);
        painelAcoes.add(btnLimpar);
        
        return painelAcoes;
    }
    
    // Métodos de ação
    private JPanel criarInfoBox(String title, String value, Color color) {
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
    
    private void buscarVendasDia(String data) {
        JOptionPane.showMessageDialog(workArea, 
            "Busca de vendas do dia " + data + " executada!\n\n" +
            "Resultados encontrados:\n" +
            "• Total de vendas: 5\n" +
            "• Valor total: R$ 972,58\n" +
            "• Ticket médio: R$ 194,52",
            "Busca Concluída", JOptionPane.INFORMATION_MESSAGE);
        
        SystemLogger.ui("Busca de vendas do dia executada por " + usuarioAtual);
    }
    
    private void buscarVendasMes(String mesAno, String vendedor) {
        JOptionPane.showMessageDialog(workArea, 
            "Busca de vendas do mês " + mesAno + " executada!\n" +
            "Vendedor: " + vendedor + "\n\n" +
            "Resultados encontrados:\n" +
            "• Total de vendas: 23\n" +
            "• Valor total: R$ 5.678,91\n" +
            "• Ticket médio: R$ 246,91",
            "Busca Concluída", JOptionPane.INFORMATION_MESSAGE);
        
        SystemLogger.ui("Busca de vendas do mês executada por " + usuarioAtual);
    }
    
    private void buscarVendasProduto() {
        JOptionPane.showMessageDialog(workArea, 
            "Busca de vendas por produto executada!\n\n" +
            "Período: " + cbTipoRelatorio.getSelectedItem() + "\n" +
            "Resultados encontrados:\n" +
            "• Total de produtos: 6\n" +
            "• Mais vendido: Refrigerante 2L (35 unidades)\n" +
            "• Maior faturamento: Arroz 5kg (R$ 649,75)",
            "Busca Concluída", JOptionPane.INFORMATION_MESSAGE);
        
        SystemLogger.ui("Busca de vendas por produto executada por " + usuarioAtual);
    }
    
    private void buscarVendasVendedor() {
        JOptionPane.showMessageDialog(workArea, 
            "Busca de vendas por vendedor executada!\n\n" +
            "Resultados encontrados:\n" +
            "• Total de vendas: R$ 972,58\n" +
            "• Total de comissões: R$ 48,63\n" +
            "• Quantidade de pedidos: 5",
            "Busca Concluída", JOptionPane.INFORMATION_MESSAGE);
        
        SystemLogger.ui("Busca de vendas por vendedor executada por " + usuarioAtual);
    }
    
    private void buscarRelatorioFinanceiro() {
        JOptionPane.showMessageDialog(workArea, 
            "Relatório financeiro gerado!\n\n" +
            "Período: " + txtDateInicio.getText() + " a " + txtDateFim.getText() + "\n" +
            "Receita Bruta: R$ 2.456,78\n" +
            "Custo Mercadorias: R$ 1.234,56\n" +
            "Lucro Líquido: R$ 987,66\n" +
            "Margem: 40,2%",
            "Relatório Financeiro", JOptionPane.INFORMATION_MESSAGE);
        
        SystemLogger.ui("Relatório financeiro gerado por " + usuarioAtual);
    }
    
    private void buscarRelatorioEstoque() {
        JOptionPane.showMessageDialog(workArea, 
            "Relatório de estoque gerado!\n\n" +
            "Resultados encontrados:\n" +
            "• Total de produtos: 6\n" +
            "• Produtos em estoque normal: 3\n" +
            "• Produtos com estoque baixo: 2\n" +
            "• Produtos sem estoque: 1\n" +
            "• Valor total em estoque: R$ 1.895,00",
            "Relatório de Estoque", JOptionPane.INFORMATION_MESSAGE);
        
        SystemLogger.ui("Relatório de estoque gerado por " + usuarioAtual);
    }
    
    private void limparFiltros() {
        // Limpar campos de filtro
        txtDateInicio.setText(LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
        txtDateFim.setText(LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
        cbTipoRelatorio.setSelectedIndex(0);
        
        JOptionPane.showMessageDialog(workArea, 
            "Filtros limpos com sucesso!", 
            "Limpar Filtros", JOptionPane.INFORMATION_MESSAGE);
        
        SystemLogger.ui("Filtros limpos por " + usuarioAtual);
    }
    
    private void gerarRelatorio() {
        int selectedTab = tabbedPane.getSelectedIndex();
        String tabTitle = tabbedPane.getTitleAt(selectedTab);
        
        StringBuilder relatorio = new StringBuilder();
        relatorio.append("=== RELATÓRIO GERADO ===\n\n");
        relatorio.append("Tipo: ").append(tabTitle).append("\n");
        relatorio.append("Data: ").append(LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))).append("\n");
        relatorio.append("Hora: ").append(java.time.LocalTime.now().format(java.time.format.DateTimeFormatter.ofPattern("HH:mm:ss"))).append("\n");
        relatorio.append("Usuário: ").append(usuarioAtual).append("\n\n");
        
        // Adicionar conteúdo específico baseado na aba selecionada
        switch (selectedTab) {
            case 0: // Vendas do Dia
                relatorio.append("=== VENDAS DO DIA ===\n");
                relatorio.append("Total Vendas: R$ 972,58\n");
                relatorio.append("Total Pedidos: 5\n");
                relatorio.append("Ticket Médio: R$ 194,52\n");
                break;
            case 1: // Vendas do Mês
                relatorio.append("=== VENDAS DO MÊS ===\n");
                relatorio.append("Total Vendas: R$ 5.678,91\n");
                relatorio.append("Total Pedidos: 112\n");
                relatorio.append("Ticket Médio: R$ 50,70\n");
                break;
            case 2: // Vendas por Produto
                relatorio.append("=== VENDAS POR PRODUTO ===\n");
                relatorio.append("Total de Produtos: 6\n");
                relatorio.append("Mais Vendido: Refrigerante 2L\n");
                relatorio.append("Valor Total: R$ 1.456,05\n");
                break;
            case 3: // Vendas por Vendedor
                relatorio.append("=== VENDAS POR VENDEDOR ===\n");
                relatorio.append("Total Vendas: R$ 972,58\n");
                relatorio.append("Total Comissões: R$ 48,63\n");
                relatorio.append("Qtd Pedidos: 5\n");
                break;
            case 4: // Relatório Financeiro
                relatorio.append("=== RELATÓRIO FINANCEIRO ===\n");
                relatorio.append("Receita Bruta: R$ 2.456,78\n");
                relatorio.append("Custo Mercadorias: R$ 1.234,56\n");
                relatorio.append("Lucro Líquido: R$ 987,66\n");
                relatorio.append("Margem: 40,2%\n");
                break;
            case 5: // Relatório de Estoque
                relatorio.append("=== RELATÓRIO DE ESTOQUE ===\n");
                relatorio.append("Total de Produtos: 6\n");
                relatorio.append("Valor em Estoque: R$ 1.895,00\n");
                relatorio.append("Produtos Críticos: 1\n");
                break;
        }
        
        relatorio.append("\n=== VERSÃO PDVMENULATERALELEGANTE ===\n");
        relatorio.append("Hermes Comercial PDV\n");
        relatorio.append("Sistema Integrado de Gestão\n");
        
        // Exibir relatório em uma janela dedicada
        JFrame relatorioFrame = new JFrame("Relatório - " + tabTitle);
        relatorioFrame.setSize(600, 500);
        relatorioFrame.setLocationRelativeTo(workArea.getTopLevelAncestor());
        
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
            java.awt.datatransfer.StringSelection selection = new java.awt.datatransfer.StringSelection(relatorioText);
            java.awt.Toolkit.getDefaultToolkit().getSystemClipboard().setContents(selection, null);
            JOptionPane.showMessageDialog(relatorioFrame, 
                "Relatório copiado para área de transferência!", 
                "Copiado", JOptionPane.INFORMATION_MESSAGE);
        });
        
        JButton btnFechar = LayoutPadrao.criarBotaoPerigo("❌ Fechar");
        btnFechar.addActionListener(ev -> relatorioFrame.dispose());
        
        buttonPanel.add(btnCopiar);
        buttonPanel.add(btnFechar);
        relatorioFrame.add(buttonPanel, BorderLayout.SOUTH);
        
        relatorioFrame.setVisible(true);
        
        SystemLogger.ui("Relatório " + tabTitle + " gerado por " + usuarioAtual);
    }
    
    private void exportarPDF() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Exportar Relatório como PDF");
        fileChooser.setFileFilter(new javax.swing.filechooser.FileNameExtensionFilter(
            "Arquivos PDF", "pdf"));
        
        // Sugerir nome de arquivo
        String nomeArquivo = "relatorio_" + LocalDate.now().format(DateTimeFormatter.ofPattern("dd_MM_yyyy")) + ".pdf";
        fileChooser.setSelectedFile(new java.io.File(nomeArquivo));
        
        int result = fileChooser.showSaveDialog(workArea.getTopLevelAncestor());
        if (result == JFileChooser.APPROVE_OPTION) {
            java.io.File selectedFile = fileChooser.getSelectedFile();
            
            // Garantir extensão .pdf
            if (!selectedFile.getName().toLowerCase().endsWith(".pdf")) {
                selectedFile = new java.io.File(selectedFile.getParent(), selectedFile.getName() + ".pdf");
            }
            
            // Simulação de exportação
            try {
                StringBuilder pdfContent = new StringBuilder();
                pdfContent.append("=== RELATÓRIO PDF ===\n\n");
                pdfContent.append("Arquivo: ").append(selectedFile.getName()).append("\n");
                pdfContent.append("Data: ").append(LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))).append("\n");
                pdfContent.append("Usuário: ").append(usuarioAtual).append("\n\n");
                pdfContent.append("Conteúdo do relatório seria gerado aqui\n");
                pdfContent.append("com biblioteca iText ou similar\n\n");
                pdfContent.append("Versão PDVMenuLateralElegante");
                
                // Salvar conteúdo em arquivo de texto (simulação)
                java.nio.file.Files.write(selectedFile.toPath(), pdfContent.toString().getBytes());
                
                JOptionPane.showMessageDialog(workArea, 
                    "Relatório exportado com sucesso!\n\n" +
                    "Arquivo: " + selectedFile.getAbsolutePath() + "\n" +
                    "Formato: PDF (simulado como texto)\n" +
                    "Tamanho: " + selectedFile.length() + " bytes",
                    "Exportação Concluída", JOptionPane.INFORMATION_MESSAGE);
                
                SystemLogger.ui("Relatório exportado como PDF por " + usuarioAtual);
                    
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(workArea, 
                    "Erro ao exportar relatório: " + ex.getMessage(),
                    "Erro de Exportação", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    private void imprimirRelatorio() {
        // Criar diálogo de impressão
        PrinterJob job = PrinterJob.getPrinterJob();
        
        if (job.printDialog()) {
            try {
                // Criar conteúdo para impressão
                String relatorioContent = "=== RELATÓRIO PARA IMPRESSÃO ===\n\n" +
                    "Data: " + LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")) + "\n" +
                    "Hora: " + java.time.LocalTime.now().format(java.time.format.DateTimeFormatter.ofPattern("HH:mm:ss")) + "\n" +
                    "Usuário: " + usuarioAtual + "\n" +
                    "Sistema: Hermes Comercial PDV - PDVMenuLateralElegante\n\n" +
                    "Conteúdo do relatório selecionado\n" +
                    "Aba: " + tabbedPane.getTitleAt(tabbedPane.getSelectedIndex()) + "\n\n" +
                    "Versão Integrada";
                
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
                
                JOptionPane.showMessageDialog(workArea, 
                    "Relatório enviado para impressão!\n\n" +
                    "Impressora: " + job.getJobName() + "\n" +
                    "Status: Enviado para fila de impressão",
                    "Impressão Concluída", JOptionPane.INFORMATION_MESSAGE);
                
                SystemLogger.ui("Relatório impresso por " + usuarioAtual);
                    
            } catch (java.awt.print.PrinterException ex) {
                JOptionPane.showMessageDialog(workArea, 
                    "Erro ao imprimir relatório: " + ex.getMessage(),
                    "Erro de Impressão", JOptionPane.ERROR_MESSAGE);
            }
        } else {
            JOptionPane.showMessageDialog(workArea, 
                "Impressão cancelada pelo usuário.",
                "Impressão Cancelada", JOptionPane.INFORMATION_MESSAGE);
        }
    }
    
    private void copiarRelatorio() {
        int selectedTab = tabbedPane.getSelectedIndex();
        String tabTitle = tabbedPane.getTitleAt(selectedTab);
        
        String relatorioText = "=== RELATÓRIO " + tabTitle.toUpperCase() + " ===\n\n" +
            "Data: " + LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")) + "\n" +
            "Usuário: " + usuarioAtual + "\n" +
            "Sistema: Hermes Comercial PDV - PDVMenuLateralElegante\n\n" +
            "Relatório copiado para área de transferência!";
        
        java.awt.datatransfer.StringSelection selection = new java.awt.datatransfer.StringSelection(relatorioText);
        java.awt.Toolkit.getDefaultToolkit().getSystemClipboard().setContents(selection, null);
        
        JOptionPane.showMessageDialog(workArea, 
            "Relatório copiado para área de transferência!\n\n" +
            "Tipo: " + tabTitle + "\n" +
            "Data: " + LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")),
            "Copiado", JOptionPane.INFORMATION_MESSAGE);
        
        SystemLogger.ui("Relatório " + tabTitle + " copiado por " + usuarioAtual);
    }
    
    private void limparRelatorio() {
        if (txtRelatorio != null) {
            txtRelatorio.setText("");
        }
        
        JOptionPane.showMessageDialog(workArea, 
            "Relatório limpo com sucesso!", 
            "Limpar", JOptionPane.INFORMATION_MESSAGE);
        
        SystemLogger.ui("Relatório limpo por " + usuarioAtual);
    }
}
