package com.br.hermescomercial.pdv.controller;

import com.br.hermescomercial.util.SystemLogger;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.List;

/**
 * Classe especializada para o formulário de Resumo Diário
 * Estrutura: Header → Busca → Formulário → Tabela
 */
public class PDVFormularioResumoDiario {
    
    private JPanel workArea;
    private String usuarioAtual;
    private String nomeUsuario;
    
    // Componentes do formulário
    private JPanel dateChooserData;
    private JComboBox<String> comboPeriodo;
    private JComboBox<String> comboTipoRelatorio;
    private JCheckBox chkVendas;
    private JCheckBox chkDevolucoes;
    private JCheckBox chkDespesas;
    private JCheckBox chkFormasPagamento;
    
    // Labels de resumo
    private JLabel lblTotalVendas;
    private JLabel lblTotalDevolucoes;
    private JLabel lblTotalDespesas;
    private JLabel lblSaldoLiquido;
    private JLabel lblTotalTransacoes;
    private JLabel lblTicketMedio;
    
    // Tabela de resumo
    private JTable tabelaResumo;
    private DefaultTableModel modelTabela;
    private List<TransacaoDiaria> transacoesEncontradas;
    
    // Cores
    private static final Color WHITE = Color.WHITE;
    private static final Color ACCENT_COLOR = new Color(52, 152, 219);
    private static final Color SUCCESS_COLOR = new Color(39, 174, 96);
    private static final Color WARNING_COLOR = new Color(241, 196, 15);
    private static final Color DANGER_COLOR = new Color(231, 76, 60);
    private static final Color GRAY = new Color(149, 165, 166);
    
    public PDVFormularioResumoDiario(JPanel workArea, String usuarioAtual, String nomeUsuario) {
        this.workArea = workArea;
        this.usuarioAtual = usuarioAtual;
        this.nomeUsuario = nomeUsuario;
        this.transacoesEncontradas = new ArrayList<>();
    }
    
    /**
     * Cria o formulário completo de Resumo Diário
     */
    public JPanel criarFormularioResumoDiario() {
        try {
            SystemLogger.ui("=== CRIANDO FORMULÁRIO RESUMO DIÁRIO ===");
            SystemLogger.ui("Usuário: " + usuarioAtual);
            
            JPanel painelPrincipal = new JPanel(new BorderLayout());
            painelPrincipal.setBackground(WHITE);
            
            // Header
            JPanel headerPanel = criarHeader();
            painelPrincipal.add(headerPanel, BorderLayout.NORTH);
            
            // Painel central com busca, formulário e tabela
            JPanel painelCentral = new JPanel(new BorderLayout());
            painelCentral.setBackground(WHITE);
            
            // Painel de busca e filtros
            JPanel buscaPanel = criarPainelBusca();
            painelCentral.add(buscaPanel, BorderLayout.NORTH);
            
            // Painel do formulário de filtros
            JPanel formularioPanel = criarPainelFormulario();
            painelCentral.add(formularioPanel, BorderLayout.CENTER);
            
            // Tabela de resumo
            JPanel tabelaPanel = criarPainelTabela();
            painelCentral.add(tabelaPanel, BorderLayout.SOUTH);
            
            painelPrincipal.add(painelCentral, BorderLayout.CENTER);
            
            SystemLogger.ui("Formulário Resumo Diário criado com sucesso");
            return painelPrincipal;
            
        } catch (Exception e) {
            SystemLogger.error("Erro ao criar formulário Resumo Diário", e);
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
        JLabel titleLabel = new JLabel("📊 Resumo Diário");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        titleLabel.setForeground(WHITE);
        
        // Informações
        JPanel infoPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        infoPanel.setOpaque(false);
        
        JLabel usuarioLabel = new JLabel("👤 " + nomeUsuario);
        usuarioLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        usuarioLabel.setForeground(WHITE);
        
        JLabel dataLabel = new JLabel("📅 " + new SimpleDateFormat("dd/MM/yyyy HH:mm").format(new Date()));
        dataLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        dataLabel.setForeground(WHITE);
        
        infoPanel.add(usuarioLabel);
        infoPanel.add(Box.createHorizontalStrut(20));
        infoPanel.add(dataLabel);
        
        headerPanel.add(titleLabel, BorderLayout.WEST);
        headerPanel.add(infoPanel, BorderLayout.EAST);
        
        return headerPanel;
    }
    
    /**
     * Cria o painel de busca e filtros
     */
    private JPanel criarPainelBusca() {
        JPanel buscaPanel = new JPanel(new BorderLayout());
        buscaPanel.setBackground(new Color(245, 245, 245));
        buscaPanel.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));
        
        JLabel buscaLabel = new JLabel("🔍 Filtros do Relatório");
        buscaLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        buscaLabel.setForeground(ACCENT_COLOR);
        
        // Painel de filtros
        JPanel filtrosPanel = new JPanel(new GridBagLayout());
        filtrosPanel.setOpaque(false);
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
        // Data
        gbc.gridx = 0; gbc.gridy = 0;
        JLabel lblData = new JLabel("Data:");
        lblData.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        filtrosPanel.add(lblData, gbc);
        
        gbc.gridx = 1; gbc.weightx = 1.0;
        dateChooserData = criarDateChooser();
        filtrosPanel.add(dateChooserData, gbc);
        
        // Período
        gbc.gridx = 2; gbc.weightx = 0.0;
        JLabel lblPeriodo = new JLabel("Período:");
        lblPeriodo.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        filtrosPanel.add(lblPeriodo, gbc);
        
        gbc.gridx = 3; gbc.weightx = 1.0;
        comboPeriodo = new JComboBox<>(new String[]{"Hoje", "Ontem", "Esta Semana", "Semana Passada", "Este Mês", "Mês Passado", "Personalizado"});
        comboPeriodo.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        comboPeriodo.setBorder(BorderFactory.createLineBorder(GRAY));
        filtrosPanel.add(comboPeriodo, gbc);
        
        // Tipo de Relatório
        gbc.gridx = 0; gbc.gridy = 1;
        JLabel lblTipoRelatorio = new JLabel("Tipo:");
        lblTipoRelatorio.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        filtrosPanel.add(lblTipoRelatorio, gbc);
        
        gbc.gridx = 1; gbc.weightx = 1.0;
        comboTipoRelatorio = new JComboBox<>(new String[]{"Completo", "Vendas", "Financeiro", "Produtos", "Clientes"});
        comboTipoRelatorio.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        comboTipoRelatorio.setBorder(BorderFactory.createLineBorder(GRAY));
        filtrosPanel.add(comboTipoRelatorio, gbc);
        
        // Botões
        gbc.gridx = 2; gbc.gridy = 1; gbc.gridwidth = 2; gbc.weightx = 0.0;
        JPanel botoesFiltroPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        botoesFiltroPanel.setOpaque(false);
        
        JButton btnGerar = new JButton("📊 Gerar Relatório");
        btnGerar.setBackground(SUCCESS_COLOR);
        btnGerar.setForeground(WHITE);
        btnGerar.setFocusPainted(false);
        btnGerar.setBorderPainted(false);
        btnGerar.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btnGerar.addActionListener(e -> gerarRelatorio());
        
        JButton btnExportar = new JButton("📥 Exportar");
        btnExportar.setBackground(ACCENT_COLOR);
        btnExportar.setForeground(WHITE);
        btnExportar.setFocusPainted(false);
        btnExportar.setBorderPainted(false);
        btnExportar.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btnExportar.addActionListener(e -> exportarRelatorio());
        
        botoesFiltroPanel.add(btnGerar);
        botoesFiltroPanel.add(Box.createHorizontalStrut(10));
        botoesFiltroPanel.add(btnExportar);
        
        filtrosPanel.add(botoesFiltroPanel, gbc);
        
        buscaPanel.add(buscaLabel, BorderLayout.NORTH);
        buscaPanel.add(filtrosPanel, BorderLayout.CENTER);
        
        return buscaPanel;
    }
    
    /**
     * Cria o painel do formulário de resumo
     */
    private JPanel criarPainelFormulario() {
        JPanel formularioPanel = new JPanel(new BorderLayout());
        formularioPanel.setBackground(WHITE);
        formularioPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        // Painel de opções
        JPanel opcoesPanel = new JPanel(new GridBagLayout());
        opcoesPanel.setBackground(WHITE);
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
        // Seção de opções
        JLabel opcoesSectionLabel = new JLabel("⚙️ Opções do Relatório");
        opcoesSectionLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        opcoesSectionLabel.setForeground(ACCENT_COLOR);
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 4;
        opcoesPanel.add(opcoesSectionLabel, gbc);
        
        // Checkboxes
        gbc.gridy = 1; gbc.gridwidth = 1; gbc.weightx = 0.0;
        chkVendas = new JCheckBox("Incluir Vendas");
        chkVendas.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        chkVendas.setSelected(true);
        opcoesPanel.add(chkVendas, gbc);
        
        gbc.gridx = 1;
        chkDevolucoes = new JCheckBox("Incluir Devoluções");
        chkDevolucoes.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        chkDevolucoes.setSelected(true);
        opcoesPanel.add(chkDevolucoes, gbc);
        
        gbc.gridx = 2;
        chkDespesas = new JCheckBox("Incluir Despesas");
        chkDespesas.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        chkDespesas.setSelected(false);
        opcoesPanel.add(chkDespesas, gbc);
        
        gbc.gridx = 3;
        chkFormasPagamento = new JCheckBox("Detalhar Formas Pagto");
        chkFormasPagamento.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        chkFormasPagamento.setSelected(false);
        opcoesPanel.add(chkFormasPagamento, gbc);
        
        // Painel de resumo financeiro
        JPanel resumoPanel = new JPanel(new GridBagLayout());
        resumoPanel.setBackground(new Color(240, 240, 240));
        resumoPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        GridBagConstraints gbcResumo = new GridBagConstraints();
        gbcResumo.insets = new Insets(10, 10, 10, 10);
        gbcResumo.anchor = GridBagConstraints.WEST;
        
        // Título do resumo
        JLabel resumoTitleLabel = new JLabel("💰 Resumo Financeiro");
        resumoTitleLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        resumoTitleLabel.setForeground(ACCENT_COLOR);
        gbcResumo.gridx = 0; gbcResumo.gridy = 0; gbcResumo.gridwidth = 4;
        resumoPanel.add(resumoTitleLabel, gbcResumo);
        
        // Valores
        gbcResumo.gridy = 1; gbcResumo.gridwidth = 1;
        resumoPanel.add(new JLabel("Total Vendas:"), gbcResumo);
        gbcResumo.gridx = 1; gbcResumo.anchor = GridBagConstraints.EAST;
        lblTotalVendas = new JLabel("R$ 0,00");
        lblTotalVendas.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lblTotalVendas.setForeground(SUCCESS_COLOR);
        resumoPanel.add(lblTotalVendas, gbcResumo);
        
        gbcResumo.gridy = 2; gbcResumo.gridx = 0; gbcResumo.anchor = GridBagConstraints.WEST;
        resumoPanel.add(new JLabel("Total Devoluções:"), gbcResumo);
        gbcResumo.gridx = 1; gbcResumo.anchor = GridBagConstraints.EAST;
        lblTotalDevolucoes = new JLabel("R$ 0,00");
        lblTotalDevolucoes.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lblTotalDevolucoes.setForeground(DANGER_COLOR);
        resumoPanel.add(lblTotalDevolucoes, gbcResumo);
        
        gbcResumo.gridy = 3; gbcResumo.gridx = 0; gbcResumo.anchor = GridBagConstraints.WEST;
        resumoPanel.add(new JLabel("Total Despesas:"), gbcResumo);
        gbcResumo.gridx = 1; gbcResumo.anchor = GridBagConstraints.EAST;
        lblTotalDespesas = new JLabel("R$ 0,00");
        lblTotalDespesas.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lblTotalDespesas.setForeground(WARNING_COLOR);
        resumoPanel.add(lblTotalDespesas, gbcResumo);
        
        gbcResumo.gridy = 4; gbcResumo.gridx = 0; gbcResumo.anchor = GridBagConstraints.WEST;
        JLabel lblSaldoLabel = new JLabel("SALDO LÍQUIDO:");
        lblSaldoLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lblSaldoLabel.setForeground(ACCENT_COLOR);
        resumoPanel.add(lblSaldoLabel, gbcResumo);
        gbcResumo.gridx = 1; gbcResumo.anchor = GridBagConstraints.EAST;
        lblSaldoLiquido = new JLabel("R$ 0,00");
        lblSaldoLiquido.setFont(new Font("Segoe UI", Font.BOLD, 18));
        lblSaldoLiquido.setForeground(SUCCESS_COLOR);
        resumoPanel.add(lblSaldoLiquido, gbcResumo);
        
        // Estatísticas
        gbcResumo.gridy = 5; gbcResumo.gridx = 0; gbcResumo.anchor = GridBagConstraints.WEST;
        resumoPanel.add(new JLabel("Total Transações:"), gbcResumo);
        gbcResumo.gridx = 1; gbcResumo.anchor = GridBagConstraints.EAST;
        lblTotalTransacoes = new JLabel("0");
        lblTotalTransacoes.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lblTotalTransacoes.setForeground(GRAY);
        resumoPanel.add(lblTotalTransacoes, gbcResumo);
        
        gbcResumo.gridy = 6; gbcResumo.gridx = 0; gbcResumo.anchor = GridBagConstraints.WEST;
        resumoPanel.add(new JLabel("Ticket Médio:"), gbcResumo);
        gbcResumo.gridx = 1; gbcResumo.anchor = GridBagConstraints.EAST;
        lblTicketMedio = new JLabel("R$ 0,00");
        lblTicketMedio.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lblTicketMedio.setForeground(GRAY);
        resumoPanel.add(lblTicketMedio, gbcResumo);
        
        // Montar painel principal
        formularioPanel.add(opcoesPanel, BorderLayout.NORTH);
        formularioPanel.add(resumoPanel, BorderLayout.CENTER);
        
        return formularioPanel;
    }
    
    /**
     * Cria um campo de data simplificado
     */
    private JPanel criarDateChooser() {
        JTextField dateField = new JTextField();
        dateField.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        dateField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(GRAY),
            BorderFactory.createEmptyBorder(5, 8, 5, 8)
        ));
        dateField.setText(new SimpleDateFormat("dd/MM/yyyy").format(new Date()));
        dateField.setToolTipText("DD/MM/AAAA");
        
        JPanel datePanel = new JPanel(new BorderLayout());
        datePanel.setOpaque(false);
        datePanel.add(dateField, BorderLayout.CENTER);
        
        JButton btnCalendario = new JButton("📅");
        btnCalendario.setFont(new Font("Segoe UI", Font.PLAIN, 10));
        btnCalendario.setBackground(Color.LIGHT_GRAY);
        btnCalendario.setBorderPainted(false);
        btnCalendario.setFocusPainted(false);
        btnCalendario.setPreferredSize(new Dimension(30, 20));
        
        datePanel.add(btnCalendario, BorderLayout.EAST);
        
        return datePanel;
    }
    
    /**
     * Cria o painel da tabela de resumo
     */
    private JPanel criarPainelTabela() {
        JPanel tabelaPanel = new JPanel(new BorderLayout());
        tabelaPanel.setBackground(WHITE);
        tabelaPanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 20, 20));
        
        JLabel tabelaLabel = new JLabel("📋 Detalhes das Transações");
        tabelaLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        tabelaLabel.setForeground(ACCENT_COLOR);
        
        // Modelo da tabela
        String[] colunas = {"Data/Hora", "Tipo", "Nº Documento", "Cliente", "Valor", "Forma Pagto", "Vendedor", "Status"};
        modelTabela = new DefaultTableModel(colunas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        tabelaResumo = new JTable(modelTabela);
        tabelaResumo.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        tabelaResumo.setRowHeight(25);
        tabelaResumo.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 12));
        tabelaResumo.getTableHeader().setBackground(ACCENT_COLOR);
        tabelaResumo.getTableHeader().setForeground(WHITE);
        
        // Scroll pane
        JScrollPane scrollPane = new JScrollPane(tabelaResumo);
        scrollPane.setPreferredSize(new Dimension(0, 300));
        scrollPane.setBorder(BorderFactory.createLineBorder(GRAY));
        
        // Painel de botões da tabela
        JPanel botoesTabelaPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        botoesTabelaPanel.setBackground(WHITE);
        
        JButton btnDetalhes = new JButton("👁️ Ver Detalhes");
        btnDetalhes.setBackground(ACCENT_COLOR);
        btnDetalhes.setForeground(WHITE);
        btnDetalhes.setFocusPainted(false);
        btnDetalhes.setBorderPainted(false);
        btnDetalhes.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btnDetalhes.addActionListener(e -> verDetalhesTransacao());
        
        JButton btnImprimir = new JButton("🖨️ Imprimir Relatório");
        btnImprimir.setBackground(SUCCESS_COLOR);
        btnImprimir.setForeground(WHITE);
        btnImprimir.setFocusPainted(false);
        btnImprimir.setBorderPainted(false);
        btnImprimir.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btnImprimir.addActionListener(e -> imprimirRelatorio());
        
        JButton btnAtualizar = new JButton("🔄 Atualizar");
        btnAtualizar.setBackground(WARNING_COLOR);
        btnAtualizar.setForeground(WHITE);
        btnAtualizar.setFocusPainted(false);
        btnAtualizar.setBorderPainted(false);
        btnAtualizar.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btnAtualizar.addActionListener(e -> atualizarDados());
        
        botoesTabelaPanel.add(btnDetalhes);
        botoesTabelaPanel.add(Box.createHorizontalStrut(10));
        botoesTabelaPanel.add(btnImprimir);
        botoesTabelaPanel.add(Box.createHorizontalStrut(10));
        botoesTabelaPanel.add(btnAtualizar);
        
        tabelaPanel.add(tabelaLabel, BorderLayout.NORTH);
        tabelaPanel.add(scrollPane, BorderLayout.CENTER);
        tabelaPanel.add(botoesTabelaPanel, BorderLayout.SOUTH);
        
        return tabelaPanel;
    }
    
    /**
     * Gera o relatório com base nos filtros
     */
    private void gerarRelatorio() {
        try {
            SystemLogger.ui("=== GERANDO RELATÓRIO DIÁRIO ===");
            
            // Coletar dados dos filtros
            String data = ((JTextField) dateChooserData.getComponent(0)).getText();
            String periodo = (String) comboPeriodo.getSelectedItem();
            String tipoRelatorio = (String) comboTipoRelatorio.getSelectedItem();
            boolean incluirVendas = chkVendas.isSelected();
            boolean incluirDevolucoes = chkDevolucoes.isSelected();
            boolean incluirDespesas = chkDespesas.isSelected();
            boolean detalharFormasPagamento = chkFormasPagamento.isSelected();
            
            // TODO: Implementar lógica de geração de relatório no banco de dados
            // Por enquanto, vamos adicionar dados de exemplo
            adicionarDadosExemplo();
            
            // Calcular totais
            calcularTotais();
            
            JOptionPane.showMessageDialog(workArea, 
                "Relatório gerado com sucesso!\n" +
                "Data: " + data + "\n" +
                "Período: " + periodo + "\n" +
                "Tipo: " + tipoRelatorio + "\n" +
                "Transações: " + transacoesEncontradas.size(), 
                "Relatório Gerado", JOptionPane.INFORMATION_MESSAGE);
            
            SystemLogger.ui("Relatório gerado - " + transacoesEncontradas.size() + " transações");
            
        } catch (Exception e) {
            SystemLogger.error("Erro ao gerar relatório", e);
            JOptionPane.showMessageDialog(workArea, "Erro ao gerar relatório: " + e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    /**
     * Exporta o relatório
     */
    private void exportarRelatorio() {
        if (transacoesEncontradas.isEmpty()) {
            JOptionPane.showMessageDialog(workArea, "Nenhuma transação para exportar!", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        // TODO: Implementar lógica de exportação (Excel, PDF, etc.)
        JOptionPane.showMessageDialog(workArea, 
            "Exportando " + transacoesEncontradas.size() + " transações...\n(Implementar exportação)", 
            "Exportar", JOptionPane.INFORMATION_MESSAGE);
        
        SystemLogger.ui("Exportando " + transacoesEncontradas.size() + " transações");
    }
    
    /**
     * Adiciona dados de exemplo à tabela
     */
    private void adicionarDadosExemplo() {
        // Limpar tabela atual
        modelTabela.setRowCount(0);
        transacoesEncontradas.clear();
        
        // Dados de exemplo
        Object[][] dadosExemplo = {
            {"09/05/2026 08:30", "Venda", "001", "João Silva", "R$ 150,00", "Dinheiro", "Maria Santos", "Finalizada"},
            {"09/05/2026 09:15", "Venda", "002", "Maria Oliveira", "R$ 89,50", "Cartão", "João Pedro", "Finalizada"},
            {"09/05/2026 10:20", "Devolução", "DEV-001", "Carlos Alberto", "-R$ 45,00", "Estorno", "Ana Maria", "Aprovada"},
            {"09/05/2026 11:45", "Venda", "003", "Fernanda Costa", "R$ 234,75", "PIX", "Pedro Henrique", "Finalizada"},
            {"09/05/2026 13:10", "Venda", "004", "Roberto Dias", "R$ 67,80", "Boleto", "Luciana Silva", "Finalizada"},
            {"09/05/2026 14:25", "Despesa", "DES-001", "Fornecedor A", "-R$ 120,00", "Transferência", "Admin", "Paga"},
            {"09/05/2026 15:30", "Venda", "005", "Juliana Santos", "R$ 445,00", "Cartão", "Carlos Alberto", "Finalizada"},
            {"09/05/2026 16:45", "Venda", "006", "Marcos Pereira", "R$ 178,90", "Dinheiro", "Maria Santos", "Finalizada"},
            {"09/05/2026 17:20", "Devolução", "DEV-002", "Patricia Lima", "-R$ 89,50", "Estorno", "João Pedro", "Aprovada"},
            {"09/05/2026 18:15", "Venda", "007", "Ricardo Souza", "R$ 299,99", "PIX", "Ana Maria", "Finalizada"}
        };
        
        for (Object[] dados : dadosExemplo) {
            modelTabela.addRow(dados);
            
            // Adicionar à lista de transações
            TransacaoDiaria transacao = new TransacaoDiaria();
            transacao.setDataHora((String) dados[0]);
            transacao.setTipo((String) dados[1]);
            transacao.setNumeroDocumento((String) dados[2]);
            transacao.setCliente((String) dados[3]);
            transacao.setValor((String) dados[4]);
            transacao.setFormaPagamento((String) dados[5]);
            transacao.setVendedor((String) dados[6]);
            transacao.setStatus((String) dados[7]);
            transacoesEncontradas.add(transacao);
        }
    }
    
    /**
     * Calcula os totais do resumo
     */
    private void calcularTotais() {
        double totalVendas = 0.0;
        double totalDevolucoes = 0.0;
        double totalDespesas = 0.0;
        int totalTransacoes = 0;
        int vendasCount = 0;
        
        for (TransacaoDiaria transacao : transacoesEncontradas) {
            String valorStr = transacao.getValor().replace("R$ ", "").replace(",", ".");
            double valor = Double.parseDouble(valorStr);
            
            if ("Venda".equals(transacao.getTipo())) {
                totalVendas += valor;
                vendasCount++;
            } else if ("Devolução".equals(transacao.getTipo())) {
                totalDevolucoes += Math.abs(valor);
            } else if ("Despesa".equals(transacao.getTipo())) {
                totalDespesas += Math.abs(valor);
            }
            
            totalTransacoes++;
        }
        
        double saldoLiquido = totalVendas - totalDevolucoes - totalDespesas;
        double ticketMedio = vendasCount > 0 ? totalVendas / vendasCount : 0.0;
        
        // Atualizar labels
        lblTotalVendas.setText("R$ " + String.format("%.2f", totalVendas).replace(".", ","));
        lblTotalDevolucoes.setText("R$ " + String.format("%.2f", totalDevolucoes).replace(".", ","));
        lblTotalDespesas.setText("R$ " + String.format("%.2f", totalDespesas).replace(".", ","));
        lblSaldoLiquido.setText("R$ " + String.format("%.2f", saldoLiquido).replace(".", ","));
        lblTotalTransacoes.setText(String.valueOf(totalTransacoes));
        lblTicketMedio.setText("R$ " + String.format("%.2f", ticketMedio).replace(".", ","));
        
        // Cor do saldo líquido
        if (saldoLiquido >= 0) {
            lblSaldoLiquido.setForeground(SUCCESS_COLOR);
        } else {
            lblSaldoLiquido.setForeground(DANGER_COLOR);
        }
    }
    
    /**
     * Ver detalhes da transação selecionada
     */
    private void verDetalhesTransacao() {
        int linhaSelecionada = tabelaResumo.getSelectedRow();
        if (linhaSelecionada >= 0) {
            TransacaoDiaria transacao = transacoesEncontradas.get(linhaSelecionada);
            JOptionPane.showMessageDialog(workArea, 
                "Detalhes da Transação:\n\n" +
                "Data/Hora: " + transacao.getDataHora() + "\n" +
                "Tipo: " + transacao.getTipo() + "\n" +
                "Nº Documento: " + transacao.getNumeroDocumento() + "\n" +
                "Cliente: " + transacao.getCliente() + "\n" +
                "Valor: " + transacao.getValor() + "\n" +
                "Forma Pagamento: " + transacao.getFormaPagamento() + "\n" +
                "Vendedor: " + transacao.getVendedor() + "\n" +
                "Status: " + transacao.getStatus(), 
                "Detalhes da Transação", JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(workArea, "Selecione uma transação para ver os detalhes!", "Aviso", JOptionPane.WARNING_MESSAGE);
        }
    }
    
    /**
     * Imprime o relatório
     */
    private void imprimirRelatorio() {
        if (transacoesEncontradas.isEmpty()) {
            JOptionPane.showMessageDialog(workArea, "Nenhuma transação para imprimir!", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        JOptionPane.showMessageDialog(workArea, 
            "Imprimindo relatório com " + transacoesEncontradas.size() + " transações...\n(Implementar impressão)", 
            "Imprimir", JOptionPane.INFORMATION_MESSAGE);
        
        SystemLogger.ui("Imprimindo relatório com " + transacoesEncontradas.size() + " transações");
    }
    
    /**
     * Atualiza os dados
     */
    private void atualizarDados() {
        gerarRelatorio();
    }
    
    /**
     * Cria painel de erro
     */
    private JPanel criarPainelErro() {
        JPanel painelErro = new JPanel(new BorderLayout());
        painelErro.setBackground(WHITE);
        
        JLabel erroLabel = new JLabel("❌ Erro ao carregar formulário");
        erroLabel.setFont(new Font("Arial", Font.BOLD, 16));
        erroLabel.setForeground(Color.RED);
        erroLabel.setHorizontalAlignment(JLabel.CENTER);
        
        painelErro.add(erroLabel, BorderLayout.CENTER);
        return painelErro;
    }
    
    /**
     * Classe interna para representar uma transação diária
     */
    private static class TransacaoDiaria {
        private String dataHora;
        private String tipo;
        private String numeroDocumento;
        private String cliente;
        private String valor;
        private String formaPagamento;
        private String vendedor;
        private String status;
        
        // Getters e Setters
        public String getDataHora() { return dataHora; }
        public void setDataHora(String dataHora) { this.dataHora = dataHora; }
        
        public String getTipo() { return tipo; }
        public void setTipo(String tipo) { this.tipo = tipo; }
        
        public String getNumeroDocumento() { return numeroDocumento; }
        public void setNumeroDocumento(String numeroDocumento) { this.numeroDocumento = numeroDocumento; }
        
        public String getCliente() { return cliente; }
        public void setCliente(String cliente) { this.cliente = cliente; }
        
        public String getValor() { return valor; }
        public void setValor(String valor) { this.valor = valor; }
        
        public String getFormaPagamento() { return formaPagamento; }
        public void setFormaPagamento(String formaPagamento) { this.formaPagamento = formaPagamento; }
        
        public String getVendedor() { return vendedor; }
        public void setVendedor(String vendedor) { this.vendedor = vendedor; }
        
        public String getStatus() { return status; }
        public void setStatus(String status) { this.status = status; }
    }
}
