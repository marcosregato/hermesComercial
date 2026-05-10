package com.br.hermescomercial.pdv.controller;

import com.br.hermescomercial.util.SystemLogger;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Classe especializada para Relatório de Vendas unificado
 * Estrutura: Header → Busca → Formulário → Tabela
 * Une: Vendas do Dia, Vendas do Mês, Vendas por Produto
 */
public class PDVFormularioRelatorioVendas {
    
    private JPanel workArea;
    private String usuarioAtual;
    private String nomeUsuario;
    
    // Componentes do formulário
    private JTextField txtBusca;
    private JComboBox<String> comboTipoRelatorio;
    private JTextField txtDataInicio;
    private JTextField txtDataFim;
    private JTextField txtProduto;
    private JComboBox<String> comboStatus;
    private JComboBox<String> comboVendedor;
    private JComboBox<String> comboFormaPagamento;
    
    // Tabelas para diferentes tipos de relatório
    private JTable tabelaVendas;
    private DefaultTableModel modelTabela;
    private List<Venda> vendasEncontradas;
    
    // Painéis de resumo
    private JPanel painelResumoDia;
    private JPanel painelResumoMes;
    private JPanel painelResumoProduto;
    
    // Cores
    private static final Color WHITE = Color.WHITE;
    private static final Color ACCENT_COLOR = new Color(52, 152, 219);
    private static final Color SUCCESS_COLOR = new Color(39, 174, 96);
    private static final Color DANGER_COLOR = new Color(231, 76, 60);
    private static final Color WARNING_COLOR = new Color(241, 196, 15);
    private static final Color GRAY = new Color(149, 165, 166);
    
    // Formatação
    private DecimalFormat currencyFormat = new DecimalFormat("R$ #,##0.00");
    
    public PDVFormularioRelatorioVendas(JPanel workArea, String usuarioAtual, String nomeUsuario) {
        this.workArea = workArea;
        this.usuarioAtual = usuarioAtual;
        this.nomeUsuario = nomeUsuario;
        this.vendasEncontradas = new ArrayList<>();
    }
    
    /**
     * Cria o formulário completo de Relatório de Vendas
     */
    public JPanel criarFormularioRelatorioVendas() {
        try {
            SystemLogger.ui("=== CRIANDO FORMULÁRIO RELATÓRIO DE VENDAS ===");
            SystemLogger.ui("Usuário: " + usuarioAtual);
            
            JPanel painelPrincipal = new JPanel(new BorderLayout());
            painelPrincipal.setBackground(WHITE);
            
            // Header
            JPanel headerPanel = criarHeader();
            painelPrincipal.add(headerPanel, BorderLayout.NORTH);
            
            // Abas para diferentes tipos de relatório
            JTabbedPane tabbedPane = new JTabbedPane();
            tabbedPane.setBackground(WHITE);
            
            // Aba Vendas do Dia
            JPanel painelDia = criarAbaVendasDia();
            tabbedPane.addTab("📊 Vendas do Dia", painelDia);
            
            // Aba Vendas do Mês
            JPanel painelMes = criarAbaVendasMes();
            tabbedPane.addTab("📅 Vendas do Mês", painelMes);
            
            // Aba Vendas por Produto
            JPanel painelProduto = criarAbaVendasProduto();
            tabbedPane.addTab("📦 Vendas por Produto", painelProduto);
            
            painelPrincipal.add(tabbedPane, BorderLayout.CENTER);
            
            SystemLogger.ui("Formulário Gestão de Vendas criado com sucesso");
            return painelPrincipal;
            
        } catch (Exception e) {
            SystemLogger.error("Erro ao criar formulário Gestão de Vendas", e);
            return criarPainelErro();
        }
    }
    
    /**
     * Cria o header do formulário
     */
    private JPanel criarHeader() {
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(ACCENT_COLOR);
        headerPanel.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));
        headerPanel.setPreferredSize(new Dimension(0, 80));
        
        // Título
        JLabel titleLabel = new JLabel("💰 Relatório de Vendas");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        titleLabel.setForeground(WHITE);
        
        // Informações do usuário
        JPanel userInfoPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        userInfoPanel.setBackground(ACCENT_COLOR);
        
        JLabel userLabel = new JLabel("👤 " + nomeUsuario);
        userLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        userLabel.setForeground(WHITE);
        
        JLabel dateLabel = new JLabel("📅 " + new java.text.SimpleDateFormat("dd/MM/yyyy HH:mm").format(new java.util.Date()));
        dateLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        dateLabel.setForeground(WHITE);
        
        userInfoPanel.add(userLabel);
        userInfoPanel.add(Box.createHorizontalStrut(20));
        userInfoPanel.add(dateLabel);
        
        headerPanel.add(titleLabel, BorderLayout.WEST);
        headerPanel.add(userInfoPanel, BorderLayout.EAST);
        
        return headerPanel;
    }
    
    /**
     * Cria aba de Vendas do Dia
     */
    private JPanel criarAbaVendasDia() {
        JPanel painelDia = new JPanel(new BorderLayout());
        painelDia.setBackground(WHITE);
        
        // Painel de resumo do dia
        painelResumoDia = criarPainelResumoDia();
        painelDia.add(painelResumoDia, BorderLayout.NORTH);
        
        // Painel central com busca, formulário e tabela
        JPanel painelCentral = new JPanel(new BorderLayout());
        painelCentral.setBackground(WHITE);
        
        // Painel de busca rápida
        JPanel buscaPanel = criarPainelBusca("Dia");
        painelCentral.add(buscaPanel, BorderLayout.NORTH);
        
        // Painel do formulário
        JPanel formularioPanel = criarPainelFormulario("Dia");
        painelCentral.add(formularioPanel, BorderLayout.CENTER);
        
        // Tabela de vendas do dia
        JPanel tabelaPanel = criarPainelTabela("Vendas do Dia");
        painelCentral.add(tabelaPanel, BorderLayout.SOUTH);
        
        painelDia.add(painelCentral, BorderLayout.CENTER);
        
        return painelDia;
    }
    
    /**
     * Cria aba de Vendas do Mês
     */
    private JPanel criarAbaVendasMes() {
        JPanel painelMes = new JPanel(new BorderLayout());
        painelMes.setBackground(WHITE);
        
        // Painel de resumo do mês
        painelResumoMes = criarPainelResumoMes();
        painelMes.add(painelResumoMes, BorderLayout.NORTH);
        
        // Painel central com busca, formulário e tabela
        JPanel painelCentral = new JPanel(new BorderLayout());
        painelCentral.setBackground(WHITE);
        
        // Painel de busca rápida
        JPanel buscaPanel = criarPainelBusca("Mês");
        painelCentral.add(buscaPanel, BorderLayout.NORTH);
        
        // Painel do formulário
        JPanel formularioPanel = criarPainelFormulario("Mês");
        painelCentral.add(formularioPanel, BorderLayout.CENTER);
        
        // Tabela de vendas do mês
        JPanel tabelaPanel = criarPainelTabela("Vendas do Mês");
        painelCentral.add(tabelaPanel, BorderLayout.SOUTH);
        
        painelMes.add(painelCentral, BorderLayout.CENTER);
        
        return painelMes;
    }
    
    /**
     * Cria aba de Vendas por Produto
     */
    private JPanel criarAbaVendasProduto() {
        JPanel painelProduto = new JPanel(new BorderLayout());
        painelProduto.setBackground(WHITE);
        
        // Painel de resumo por produto
        painelResumoProduto = criarPainelResumoProduto();
        painelProduto.add(painelResumoProduto, BorderLayout.NORTH);
        
        // Painel central com busca, formulário e tabela
        JPanel painelCentral = new JPanel(new BorderLayout());
        painelCentral.setBackground(WHITE);
        
        // Painel de busca rápida
        JPanel buscaPanel = criarPainelBusca("Produto");
        painelCentral.add(buscaPanel, BorderLayout.NORTH);
        
        // Painel do formulário
        JPanel formularioPanel = criarPainelFormulario("Produto");
        painelCentral.add(formularioPanel, BorderLayout.CENTER);
        
        // Tabela de vendas por produto
        JPanel tabelaPanel = criarPainelTabela("Vendas por Produto");
        painelCentral.add(tabelaPanel, BorderLayout.SOUTH);
        
        painelProduto.add(painelCentral, BorderLayout.CENTER);
        
        return painelProduto;
    }
    
    /**
     * Cria painel de resumo do dia
     */
    private JPanel criarPainelResumoDia() {
        JPanel resumoPanel = new JPanel(new GridLayout(1, 4, 10, 10));
        resumoPanel.setBackground(WHITE);
        resumoPanel.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));
        
        // Total de Vendas
        JPanel totalPanel = criarResumoBox("💰 Total do Dia", "R$ 12.543,67", SUCCESS_COLOR);
        resumoPanel.add(totalPanel);
        
        // Quantidade de Vendas
        JPanel qtdPanel = criarResumoBox("📊 Qtd. Vendas", "47", ACCENT_COLOR);
        resumoPanel.add(qtdPanel);
        
        // Ticket Médio
        JPanel ticketPanel = criarResumoBox("🎫 Ticket Médio", "R$ 267,10", WARNING_COLOR);
        resumoPanel.add(ticketPanel);
        
        // Meta do Dia
        JPanel metaPanel = criarResumoBox("🎯 Meta do Dia", "78%", SUCCESS_COLOR);
        resumoPanel.add(metaPanel);
        
        return resumoPanel;
    }
    
    /**
     * Cria painel de resumo do mês
     */
    private JPanel criarPainelResumoMes() {
        JPanel resumoPanel = new JPanel(new GridLayout(1, 4, 10, 10));
        resumoPanel.setBackground(WHITE);
        resumoPanel.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));
        
        // Total do Mês
        JPanel totalPanel = criarResumoBox("💰 Total do Mês", "R$ 387.234,89", SUCCESS_COLOR);
        resumoPanel.add(totalPanel);
        
        // Quantidade de Vendas
        JPanel qtdPanel = criarResumoBox("📊 Qtd. Vendas", "1.247", ACCENT_COLOR);
        resumoPanel.add(qtdPanel);
        
        // Ticket Médio
        JPanel ticketPanel = criarResumoBox("🎫 Ticket Médio", "R$ 310,60", WARNING_COLOR);
        resumoPanel.add(ticketPanel);
        
        // Crescimento
        JPanel crescimentoPanel = criarResumoBox("📈 Crescimento", "+15,3%", SUCCESS_COLOR);
        resumoPanel.add(crescimentoPanel);
        
        return resumoPanel;
    }
    
    /**
     * Cria painel de resumo por produto
     */
    private JPanel criarPainelResumoProduto() {
        JPanel resumoPanel = new JPanel(new GridLayout(1, 4, 10, 10));
        resumoPanel.setBackground(WHITE);
        resumoPanel.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));
        
        // Produtos Vendidos
        JPanel produtosPanel = criarResumoBox("📦 Produtos Vendidos", "3.456", ACCENT_COLOR);
        resumoPanel.add(produtosPanel);
        
        // Mais Vendido
        JPanel maisVendidoPanel = criarResumoBox("🏆 Mais Vendido", "Notebook Dell", SUCCESS_COLOR);
        resumoPanel.add(maisVendidoPanel);
        
        // Estoque Baixo
        JPanel estoquePanel = criarResumoBox("⚠️ Estoque Baixo", "8 produtos", DANGER_COLOR);
        resumoPanel.add(estoquePanel);
        
        // Margem Média
        JPanel margemPanel = criarResumoBox("💹 Margem Média", "32,5%", WARNING_COLOR);
        resumoPanel.add(margemPanel);
        
        return resumoPanel;
    }
    
    /**
     * Cria um box de resumo
     */
    private JPanel criarResumoBox(String titulo, String valor, Color cor) {
        JPanel box = new JPanel(new BorderLayout());
        box.setBackground(WHITE);
        box.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(cor, 2),
            BorderFactory.createEmptyBorder(10, 15, 10, 15)
        ));
        
        JLabel tituloLabel = new JLabel(titulo);
        tituloLabel.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        tituloLabel.setForeground(GRAY);
        
        JLabel valorLabel = new JLabel(valor);
        valorLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        valorLabel.setForeground(cor);
        
        box.add(tituloLabel, BorderLayout.NORTH);
        box.add(valorLabel, BorderLayout.CENTER);
        
        return box;
    }
    
    /**
     * Cria painel de busca rápida
     */
    private JPanel criarPainelBusca(String tipo) {
        JPanel buscaPanel = new JPanel(new BorderLayout());
        buscaPanel.setBackground(WHITE);
        buscaPanel.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));
        
        JLabel buscaLabel = new JLabel("🔍 Busca Rápida:");
        buscaLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        buscaLabel.setForeground(ACCENT_COLOR);
        
        txtBusca = new JTextField();
        txtBusca.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        txtBusca.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(GRAY),
            BorderFactory.createEmptyBorder(8, 12, 8, 12)
        ));
        txtBusca.setToolTipText("Digite código, produto, cliente ou vendedor");
        
        JButton btnBuscar = new JButton("🔍 Buscar");
        btnBuscar.setBackground(ACCENT_COLOR);
        btnBuscar.setForeground(WHITE);
        btnBuscar.setFocusPainted(false);
        btnBuscar.setBorderPainted(false);
        btnBuscar.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btnBuscar.addActionListener(e -> buscarVendas(tipo));
        
        JPanel buscaInputPanel = new JPanel(new BorderLayout());
        buscaInputPanel.setBackground(WHITE);
        buscaInputPanel.add(txtBusca, BorderLayout.CENTER);
        buscaInputPanel.add(btnBuscar, BorderLayout.EAST);
        
        buscaPanel.add(buscaLabel, BorderLayout.WEST);
        buscaPanel.add(Box.createHorizontalStrut(10), BorderLayout.CENTER);
        buscaPanel.add(buscaInputPanel, BorderLayout.CENTER);
        
        return buscaPanel;
    }
    
    /**
     * Cria painel do formulário
     */
    private JPanel criarPainelFormulario(String tipo) {
        JPanel formularioPanel = new JPanel(new GridBagLayout());
        formularioPanel.setBackground(WHITE);
        formularioPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
        // Seção Filtros
        JLabel filtrosSectionLabel = new JLabel("🔍 Filtros de " + tipo);
        filtrosSectionLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        filtrosSectionLabel.setForeground(ACCENT_COLOR);
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 4;
        formularioPanel.add(filtrosSectionLabel, gbc);
        
        // Período
        gbc.gridy = 1; gbc.gridwidth = 1;
        JLabel lblPeriodo = new JLabel("Período:");
        lblPeriodo.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        formularioPanel.add(lblPeriodo, gbc);
        
        gbc.gridx = 1; gbc.weightx = 1.0;
        JPanel periodoPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
        periodoPanel.setBackground(WHITE);
        
        txtDataInicio = new JTextField(8);
        txtDataInicio.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        txtDataInicio.setBorder(BorderFactory.createLineBorder(GRAY));
        txtDataInicio.setToolTipText("dd/mm/aaaa");
        
        JLabel ateLabel = new JLabel("até");
        ateLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        
        txtDataFim = new JTextField(8);
        txtDataFim.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        txtDataFim.setBorder(BorderFactory.createLineBorder(GRAY));
        txtDataFim.setToolTipText("dd/mm/aaaa");
        
        periodoPanel.add(txtDataInicio);
        periodoPanel.add(ateLabel);
        periodoPanel.add(txtDataFim);
        
        formularioPanel.add(periodoPanel, gbc);
        
        // Produto (específico para aba Produto)
        if ("Produto".equals(tipo)) {
            gbc.gridx = 2; gbc.weightx = 0.0;
            JLabel lblProduto = new JLabel("Produto:");
            lblProduto.setFont(new Font("Segoe UI", Font.PLAIN, 12));
            formularioPanel.add(lblProduto, gbc);
            
            gbc.gridx = 3; gbc.weightx = 1.0;
            txtProduto = new JTextField();
            txtProduto.setFont(new Font("Segoe UI", Font.PLAIN, 12));
            txtProduto.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(GRAY),
                BorderFactory.createEmptyBorder(5, 8, 5, 8)
            ));
            formularioPanel.add(txtProduto, gbc);
        }
        
        // Status
        gbc.gridy = 2; gbc.gridx = 0;
        JLabel lblStatus = new JLabel("Status:");
        lblStatus.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        formularioPanel.add(lblStatus, gbc);
        
        gbc.gridx = 1; gbc.weightx = 1.0;
        comboStatus = new JComboBox<>(new String[]{"Todos", "Concluídas", "Canceladas", "Em Andamento"});
        comboStatus.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        comboStatus.setBorder(BorderFactory.createLineBorder(GRAY));
        formularioPanel.add(comboStatus, gbc);
        
        // Vendedor
        gbc.gridx = 2; gbc.weightx = 0.0;
        JLabel lblVendedor = new JLabel("Vendedor:");
        lblVendedor.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        formularioPanel.add(lblVendedor, gbc);
        
        gbc.gridx = 3; gbc.weightx = 1.0;
        comboVendedor = new JComboBox<>(new String[]{"Todos", "Maria", "João", "Ana", "Carlos"});
        comboVendedor.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        comboVendedor.setBorder(BorderFactory.createLineBorder(GRAY));
        formularioPanel.add(comboVendedor, gbc);
        
        // Forma de Pagamento
        gbc.gridy = 3; gbc.gridx = 0;
        JLabel lblFormaPagamento = new JLabel("Forma Pagto:");
        lblFormaPagamento.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        formularioPanel.add(lblFormaPagamento, gbc);
        
        gbc.gridx = 1; gbc.weightx = 1.0;
        comboFormaPagamento = new JComboBox<>(new String[]{"Todas", "Dinheiro", "Cartão Crédito", "Cartão Débito", "PIX", "Boleto"});
        comboFormaPagamento.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        comboFormaPagamento.setBorder(BorderFactory.createLineBorder(GRAY));
        formularioPanel.add(comboFormaPagamento, gbc);
        
        // Botões de ação
        gbc.gridy = 4; gbc.gridx = 0; gbc.gridwidth = 4; gbc.weightx = 0.0;
        JPanel botoesPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        botoesPanel.setBackground(WHITE);
        
        JButton btnFiltrar = new JButton("🔍 Aplicar Filtros");
        btnFiltrar.setBackground(ACCENT_COLOR);
        btnFiltrar.setForeground(WHITE);
        btnFiltrar.setFocusPainted(false);
        btnFiltrar.setBorderPainted(false);
        btnFiltrar.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btnFiltrar.addActionListener(e -> aplicarFiltros(tipo));
        
        JButton btnLimpar = new JButton("🧹 Limpar Filtros");
        btnLimpar.setBackground(GRAY);
        btnLimpar.setForeground(WHITE);
        btnLimpar.setFocusPainted(false);
        btnLimpar.setBorderPainted(false);
        btnLimpar.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btnLimpar.addActionListener(e -> limparFiltros());
        
        JButton btnExportar = new JButton("📄 Exportar");
        btnExportar.setBackground(SUCCESS_COLOR);
        btnExportar.setForeground(WHITE);
        btnExportar.setFocusPainted(false);
        btnExportar.setBorderPainted(false);
        btnExportar.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btnExportar.addActionListener(e -> exportarRelatorio(tipo));
        
        JButton btnImprimir = new JButton("🖨️ Imprimir");
        btnImprimir.setBackground(WARNING_COLOR);
        btnImprimir.setForeground(WHITE);
        btnImprimir.setFocusPainted(false);
        btnImprimir.setBorderPainted(false);
        btnImprimir.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btnImprimir.addActionListener(e -> imprimirRelatorio(tipo));
        
        botoesPanel.add(btnFiltrar);
        botoesPanel.add(Box.createHorizontalStrut(10));
        botoesPanel.add(btnLimpar);
        botoesPanel.add(Box.createHorizontalStrut(10));
        botoesPanel.add(btnExportar);
        botoesPanel.add(Box.createHorizontalStrut(10));
        botoesPanel.add(btnImprimir);
        
        formularioPanel.add(botoesPanel, gbc);
        
        // Preencher dados iniciais
        preencherDadosIniciais(tipo);
        
        return formularioPanel;
    }
    
    /**
     * Cria painel da tabela
     */
    private JPanel criarPainelTabela(String tipoTabela) {
        JPanel tabelaPanel = new JPanel(new BorderLayout());
        tabelaPanel.setBackground(WHITE);
        tabelaPanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 20, 20));
        
        JLabel tabelaLabel = new JLabel("📋 " + tipoTabela);
        tabelaLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        tabelaLabel.setForeground(ACCENT_COLOR);
        
        // Modelo da tabela
        String[] colunas;
        if ("Vendas por Produto".equals(tipoTabela)) {
            colunas = new String[]{"Produto", "Código", "Quantidade", "Valor Unit.", "Total", "Margem", "Estoque", "Ações"};
        } else {
            colunas = new String[]{"ID", "Data/Hora", "Cliente", "Produto", "Quantidade", "Valor Total", "Vendedor", "Forma Pagto", "Status", "Ações"};
        }
        
        modelTabela = new DefaultTableModel(colunas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == colunas.length - 1; // Apenas a coluna de ações é editável
            }
        };
        
        tabelaVendas = new JTable(modelTabela);
        tabelaVendas.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        tabelaVendas.setRowHeight(25);
        tabelaVendas.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 12));
        tabelaVendas.getTableHeader().setBackground(ACCENT_COLOR);
        tabelaVendas.getTableHeader().setForeground(WHITE);
        
        // Scroll pane
        JScrollPane scrollPane = new JScrollPane(tabelaVendas);
        scrollPane.setPreferredSize(new Dimension(0, 250));
        scrollPane.setBorder(BorderFactory.createLineBorder(GRAY));
        
        // Painel de botões da tabela
        JPanel botoesTabelaPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        botoesTabelaPanel.setBackground(WHITE);
        
        JButton btnDetalhes = new JButton("📋 Ver Detalhes");
        btnDetalhes.setBackground(ACCENT_COLOR);
        btnDetalhes.setForeground(WHITE);
        btnDetalhes.setFocusPainted(false);
        btnDetalhes.setBorderPainted(false);
        btnDetalhes.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btnDetalhes.addActionListener(e -> verDetalhes());
        
        JButton btnEditar = new JButton("✏️ Editar");
        btnEditar.setBackground(SUCCESS_COLOR);
        btnEditar.setForeground(WHITE);
        btnEditar.setFocusPainted(false);
        btnEditar.setBorderPainted(false);
        btnEditar.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btnEditar.addActionListener(e -> editarVenda());
        
        JButton btnCancelar = new JButton("❌ Cancelar");
        btnCancelar.setBackground(DANGER_COLOR);
        btnCancelar.setForeground(WHITE);
        btnCancelar.setFocusPainted(false);
        btnCancelar.setBorderPainted(false);
        btnCancelar.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btnCancelar.addActionListener(e -> cancelarVenda());
        
        botoesTabelaPanel.add(btnDetalhes);
        botoesTabelaPanel.add(Box.createHorizontalStrut(10));
        botoesTabelaPanel.add(btnEditar);
        botoesTabelaPanel.add(Box.createHorizontalStrut(10));
        botoesTabelaPanel.add(btnCancelar);
        
        tabelaPanel.add(tabelaLabel, BorderLayout.NORTH);
        tabelaPanel.add(scrollPane, BorderLayout.CENTER);
        tabelaPanel.add(botoesTabelaPanel, BorderLayout.SOUTH);
        
        // Adicionar dados de exemplo
        adicionarDadosExemplo(tipoTabela);
        
        return tabelaPanel;
    }
    
    /**
     * Adiciona dados de exemplo à tabela
     */
    private void adicionarDadosExemplo(String tipoTabela) {
        // Limpar tabela atual
        modelTabela.setRowCount(0);
        vendasEncontradas.clear();
        
        Object[][] dadosExemplo;
        
        if ("Vendas por Produto".equals(tipoTabela)) {
            dadosExemplo = new Object[][]{
                {"Notebook Dell Inspiron", "NB001", "15", "R$ 3.200,00", "R$ 48.000,00", "25%", "5", "✏️"},
                {"Mouse Logitech", "MS002", "45", "R$ 89,90", "R$ 4.045,50", "35%", "120", "✏️"},
                {"Teclado Mecânico", "KB003", "28", "R$ 245,00", "R$ 6.860,00", "30%", "15", "✏️"},
                {"Monitor LG 24\"", "MN004", "12", "R$ 890,00", "R$ 10.680,00", "28%", "8", "✏️"},
                {"Webcam HD", "WC005", "67", "R$ 156,00", "R$ 10.452,00", "40%", "200", "✏️"},
                {"Headset Bluetooth", "HS006", "34", "R$ 234,00", "R$ 7.956,00", "32%", "25", "✏️"}
            };
        } else {
            dadosExemplo = new Object[][]{
                {"#001", "10/05/2026 09:15", "João Silva", "Notebook Dell", "1", "R$ 3.200,00", "Maria", "Cartão Crédito", "Concluída", "✏️"},
                {"#002", "10/05/2026 09:45", "Maria Santos", "Mouse Logitech", "2", "R$ 179,80", "João", "PIX", "Concluída", "✏️"},
                {"#003", "10/05/2026 10:20", "Carlos Oliveira", "Teclado Mecânico", "1", "R$ 245,00", "Ana", "Dinheiro", "Concluída", "✏️"},
                {"#004", "10/05/2026 11:00", "Ana Costa", "Monitor LG 24\"", "1", "R$ 890,00", "Maria", "Cartão Débito", "Concluída", "✏️"},
                {"#005", "10/05/2026 14:30", "Pedro Lima", "Webcam HD", "3", "R$ 468,00", "João", "PIX", "Concluída", "✏️"},
                {"#006", "10/05/2026 15:15", "Lucia Fernandes", "Headset Bluetooth", "2", "R$ 468,00", "Carlos", "Cartão Crédito", "Em Andamento", "✏️"}
            };
        }
        
        for (Object[] dados : dadosExemplo) {
            modelTabela.addRow(dados);
            
            // Adicionar à lista de vendas
            Venda venda = new Venda();
            if ("Vendas por Produto".equals(tipoTabela)) {
                venda.setProduto((String) dados[0]);
                venda.setCodigo((String) dados[1]);
                venda.setQuantidade((String) dados[2]);
                venda.setValorUnitario((String) dados[3]);
                venda.setValorTotal((String) dados[4]);
            } else {
                venda.setId((String) dados[0]);
                venda.setDataHora((String) dados[1]);
                venda.setCliente((String) dados[2]);
                venda.setProduto((String) dados[3]);
                venda.setQuantidade((String) dados[4]);
                venda.setValorTotal((String) dados[5]);
                venda.setVendedor((String) dados[6]);
                venda.setFormaPagamento((String) dados[7]);
                venda.setStatus((String) dados[8]);
            }
            vendasEncontradas.add(venda);
        }
    }
    
    /**
     * Preenche dados iniciais
     */
    private void preencherDadosIniciais(String tipo) {
        comboStatus.setSelectedIndex(0);
        comboVendedor.setSelectedIndex(0);
        comboFormaPagamento.setSelectedIndex(0);
        
        // Data atual
        String dataAtual = new java.text.SimpleDateFormat("dd/MM/yyyy").format(new java.util.Date());
        txtDataInicio.setText(dataAtual);
        txtDataFim.setText(dataAtual);
    }
    
    /**
     * Busca vendas
     */
    private void buscarVendas(String tipo) {
        String termo = txtBusca.getText().trim();
        if (termo.isEmpty()) {
            JOptionPane.showMessageDialog(workArea, "Digite um termo para buscar!", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        // TODO: Implementar lógica de busca no banco de dados
        JOptionPane.showMessageDialog(workArea, 
            "Busca realizada para: " + termo + "\n" +
            "Vendas encontradas: " + vendasEncontradas.size(), 
            "Resultado", JOptionPane.INFORMATION_MESSAGE);
        
        SystemLogger.ui("Busca de vendas para: " + termo);
    }
    
    /**
     * Aplica filtros
     */
    private void aplicarFiltros(String tipo) {
        String dataInicio = txtDataInicio.getText().trim();
        String dataFim = txtDataFim.getText().trim();
        String status = (String) comboStatus.getSelectedItem();
        String vendedor = (String) comboVendedor.getSelectedItem();
        String formaPagamento = (String) comboFormaPagamento.getSelectedItem();
        
        // TODO: Implementar lógica de filtragem no banco de dados
        JOptionPane.showMessageDialog(workArea, 
            "Filtros aplicados para " + tipo + ":\n" +
            "Período: " + dataInicio + " até " + dataFim + "\n" +
            "Status: " + status + "\n" +
            "Vendedor: " + vendedor + "\n" +
            "Forma Pagamento: " + formaPagamento + "\n" +
            "Resultados: " + vendasEncontradas.size(), 
            "Filtros Aplicados", JOptionPane.INFORMATION_MESSAGE);
        
        SystemLogger.ui("Filtros aplicados para " + tipo + " por: " + nomeUsuario);
    }
    
    /**
     * Limpa filtros
     */
    private void limparFiltros() {
        txtBusca.setText("");
        comboStatus.setSelectedIndex(0);
        comboVendedor.setSelectedIndex(0);
        comboFormaPagamento.setSelectedIndex(0);
        
        // Resetar datas para hoje
        String dataAtual = new java.text.SimpleDateFormat("dd/MM/yyyy").format(new java.util.Date());
        txtDataInicio.setText(dataAtual);
        txtDataFim.setText(dataAtual);
    }
    
    /**
     * Exporta relatório
     */
    private void exportarRelatorio(String tipo) {
        JOptionPane.showMessageDialog(workArea, 
            "Exportando relatório de " + tipo + "...\n" +
            "Total de registros: " + vendasEncontradas.size() + "\n" +
            "(Implementar exportação para Excel/PDF)", 
            "Exportar", JOptionPane.INFORMATION_MESSAGE);
        
        SystemLogger.ui("Exportando relatório de " + tipo);
    }
    
    /**
     * Imprime relatório
     */
    private void imprimirRelatorio(String tipo) {
        JOptionPane.showMessageDialog(workArea, 
            "Preparando impressão de " + tipo + "...\n" +
            "Total de registros: " + vendasEncontradas.size() + "\n" +
            "(Implementar impressão)", 
            "Imprimir", JOptionPane.INFORMATION_MESSAGE);
        
        SystemLogger.ui("Imprimindo relatório de " + tipo);
    }
    
    /**
     * Ver detalhes da venda
     */
    private void verDetalhes() {
        int selectedRow = tabelaVendas.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(workArea, "Selecione uma venda para ver detalhes!", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        Venda venda = vendasEncontradas.get(selectedRow);
        
        JOptionPane.showMessageDialog(workArea, 
            "Detalhes da Venda:\n\n" +
            "ID: " + venda.getId() + "\n" +
            "Data/Hora: " + venda.getDataHora() + "\n" +
            "Cliente: " + venda.getCliente() + "\n" +
            "Produto: " + venda.getProduto() + "\n" +
            "Quantidade: " + venda.getQuantidade() + "\n" +
            "Valor Total: " + venda.getValorTotal() + "\n" +
            "Vendedor: " + venda.getVendedor() + "\n" +
            "Forma Pagamento: " + venda.getFormaPagamento() + "\n" +
            "Status: " + venda.getStatus(), 
            "Detalhes da Venda", JOptionPane.INFORMATION_MESSAGE);
        
        SystemLogger.ui("Detalhes visualizados para venda: " + venda.getId());
    }
    
    /**
     * Edita venda
     */
    private void editarVenda() {
        int selectedRow = tabelaVendas.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(workArea, "Selecione uma venda para editar!", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        Venda venda = vendasEncontradas.get(selectedRow);
        
        JOptionPane.showMessageDialog(workArea, 
            "Editando venda...\n\n" +
            "ID: " + venda.getId() + "\n" +
            "Cliente: " + venda.getCliente() + "\n" +
            "Valor: " + venda.getValorTotal() + "\n\n" +
            "(Implementar edição de venda)", 
            "Editar Venda", JOptionPane.INFORMATION_MESSAGE);
        
        SystemLogger.ui("Editando venda: " + venda.getId());
    }
    
    /**
     * Cancela venda
     */
    private void cancelarVenda() {
        int selectedRow = tabelaVendas.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(workArea, "Selecione uma venda para cancelar!", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        Venda venda = vendasEncontradas.get(selectedRow);
        
        int confirmacao = JOptionPane.showConfirmDialog(
            workArea,
            "Deseja realmente cancelar esta venda?\n\n" +
            "ID: " + venda.getId() + "\n" +
            "Cliente: " + venda.getCliente() + "\n" +
            "Valor: " + venda.getValorTotal() + "\n" +
            "Esta ação não pode ser desfeita.",
            "Cancelar Venda",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.QUESTION_MESSAGE
        );
        
        if (confirmacao == JOptionPane.YES_OPTION) {
            // TODO: Implementar lógica de cancelamento no banco de dados
            
            // Atualizar status na tabela
            modelTabela.setValueAt("Cancelada", selectedRow, 8);
            venda.setStatus("Cancelada");
            
            JOptionPane.showMessageDialog(workArea, 
                "Venda cancelada com sucesso!", 
                "Sucesso", JOptionPane.INFORMATION_MESSAGE);
            
            SystemLogger.ui("Venda cancelada: " + venda.getId());
        }
    }
    
    /**
     * Cria painel de erro
     */
    private JPanel criarPainelErro() {
        JPanel painelErro = new JPanel(new BorderLayout());
        painelErro.setBackground(WHITE);
        
        JLabel erroLabel = new JLabel("❌ Erro ao carregar formulário");
        erroLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
        erroLabel.setForeground(DANGER_COLOR);
        erroLabel.setHorizontalAlignment(SwingConstants.CENTER);
        
        painelErro.add(erroLabel, BorderLayout.CENTER);
        return painelErro;
    }
    
    /**
     * Classe interna para representar uma venda
     */
    private static class Venda {
        private String id;
        private String dataHora;
        private String cliente;
        private String produto;
        private String codigo;
        private String quantidade;
        private String valorUnitario;
        private String valorTotal;
        private String vendedor;
        private String formaPagamento;
        private String status;
        
        // Getters e Setters
        public String getId() { return id; }
        public void setId(String id) { this.id = id; }
        
        public String getDataHora() { return dataHora; }
        public void setDataHora(String dataHora) { this.dataHora = dataHora; }
        
        public String getCliente() { return cliente; }
        public void setCliente(String cliente) { this.cliente = cliente; }
        
        public String getProduto() { return produto; }
        public void setProduto(String produto) { this.produto = produto; }
        
        public String getCodigo() { return codigo; }
        public void setCodigo(String codigo) { this.codigo = codigo; }
        
        public String getQuantidade() { return quantidade; }
        public void setQuantidade(String quantidade) { this.quantidade = quantidade; }
        
        public String getValorUnitario() { return valorUnitario; }
        public void setValorUnitario(String valorUnitario) { this.valorUnitario = valorUnitario; }
        
        public String getValorTotal() { return valorTotal; }
        public void setValorTotal(String valorTotal) { this.valorTotal = valorTotal; }
        
        public String getVendedor() { return vendedor; }
        public void setVendedor(String vendedor) { this.vendedor = vendedor; }
        
        public String getFormaPagamento() { return formaPagamento; }
        public void setFormaPagamento(String formaPagamento) { this.formaPagamento = formaPagamento; }
        
        public String getStatus() { return status; }
        public void setStatus(String status) { this.status = status; }
    }
}
