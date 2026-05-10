package com.br.hermescomercial.pdv.controller;

import com.br.hermescomercial.util.SystemLogger;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Classe especializada para o formulário de Relatório Financeiro
 * Estrutura: Header → Busca → Formulário → Tabela
 */
public class PDVFormularioRelatorioFinanceiro {
    
    private JPanel workArea;
    private String usuarioAtual;
    private String nomeUsuario;
    
    // Componentes do formulário
    private JTextField txtBusca;
    private JTextField txtDataInicio;
    private JTextField txtDataFim;
    private JComboBox<String> comboPeriodo;
    private JComboBox<String> comboTipoRelatorio;
    private JTextField txtTotalReceitas;
    private JTextField txtTotalDespesas;
    private JTextField txtSaldoLiquido;
    private JTextField txtMargemLucro;
    private JTextArea txtObservacoes;
    
    // Tabela de relatório financeiro
    private JTable tabelaRelatorio;
    private DefaultTableModel modelTabela;
    private List<ItemRelatorio> itensRelatorio;
    
    // Cores
    private static final Color WHITE = Color.WHITE;
    private static final Color ACCENT_COLOR = new Color(52, 152, 219);
    private static final Color SUCCESS_COLOR = new Color(39, 174, 96);
    private static final Color DANGER_COLOR = new Color(231, 76, 60);
    private static final Color WARNING_COLOR = new Color(241, 196, 15);
    private static final Color GRAY = new Color(149, 165, 166);
    
    public PDVFormularioRelatorioFinanceiro(JPanel workArea, String usuarioAtual, String nomeUsuario) {
        this.workArea = workArea;
        this.usuarioAtual = usuarioAtual;
        this.nomeUsuario = nomeUsuario;
        this.itensRelatorio = new ArrayList<>();
    }
    
    /**
     * Cria o formulário completo de Relatório Financeiro
     */
    public JPanel criarFormularioRelatorioFinanceiro() {
        try {
            SystemLogger.ui("=== CRIANDO FORMULÁRIO RELATÓRIO FINANCEIRO ===");
            SystemLogger.ui("Usuário: " + usuarioAtual);
            
            JPanel painelPrincipal = new JPanel(new BorderLayout());
            painelPrincipal.setBackground(WHITE);
            
            // Header
            JPanel headerPanel = criarHeader();
            painelPrincipal.add(headerPanel, BorderLayout.NORTH);
            
            // Painel central com busca, formulário e tabela
            JPanel painelCentral = new JPanel(new BorderLayout());
            painelCentral.setBackground(WHITE);
            
            // Painel de busca rápida
            JPanel buscaPanel = criarPainelBusca();
            painelCentral.add(buscaPanel, BorderLayout.NORTH);
            
            // Painel do formulário
            JPanel formularioPanel = criarPainelFormulario();
            painelCentral.add(formularioPanel, BorderLayout.CENTER);
            
            // Tabela de relatório financeiro
            JPanel tabelaPanel = criarPainelTabela();
            painelCentral.add(tabelaPanel, BorderLayout.SOUTH);
            
            painelPrincipal.add(painelCentral, BorderLayout.CENTER);
            
            SystemLogger.ui("Formulário Relatório Financeiro criado com sucesso");
            return painelPrincipal;
            
        } catch (Exception e) {
            SystemLogger.error("Erro ao criar formulário Relatório Financeiro", e);
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
        JLabel titleLabel = new JLabel(" Relatório Financeiro");
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
     * Cria o painel de busca rápida
     */
    private JPanel criarPainelBusca() {
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
        txtBusca.setToolTipText("Digite descrição, categoria ou tipo");
        
        JButton btnBuscar = new JButton("🔍 Buscar");
        btnBuscar.setBackground(ACCENT_COLOR);
        btnBuscar.setForeground(WHITE);
        btnBuscar.setFocusPainted(false);
        btnBuscar.setBorderPainted(false);
        btnBuscar.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btnBuscar.addActionListener(e -> buscarRelatorio());
        
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
     * Cria o painel do formulário
     */
    private JPanel criarPainelFormulario() {
        JPanel formularioPanel = new JPanel(new GridBagLayout());
        formularioPanel.setBackground(WHITE);
        formularioPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
        // Seção Filtros
        JLabel filtrosSectionLabel = new JLabel("🔍 Filtros do Relatório");
        filtrosSectionLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        filtrosSectionLabel.setForeground(ACCENT_COLOR);
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 4;
        formularioPanel.add(filtrosSectionLabel, gbc);
        
        // Data Início
        gbc.gridy = 1; gbc.gridwidth = 1;
        JLabel lblDataInicio = new JLabel("Data Início:");
        lblDataInicio.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        formularioPanel.add(lblDataInicio, gbc);
        
        gbc.gridx = 1; gbc.weightx = 1.0;
        txtDataInicio = new JTextField();
        txtDataInicio.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        txtDataInicio.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(GRAY),
            BorderFactory.createEmptyBorder(5, 8, 5, 8)
        ));
        txtDataInicio.setToolTipText("dd/mm/aaaa");
        formularioPanel.add(txtDataInicio, gbc);
        
        // Data Fim
        gbc.gridx = 2; gbc.weightx = 0.0;
        JLabel lblDataFim = new JLabel("Data Fim:");
        lblDataFim.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        formularioPanel.add(lblDataFim, gbc);
        
        gbc.gridx = 3; gbc.weightx = 1.0;
        txtDataFim = new JTextField();
        txtDataFim.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        txtDataFim.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(GRAY),
            BorderFactory.createEmptyBorder(5, 8, 5, 8)
        ));
        txtDataFim.setToolTipText("dd/mm/aaaa");
        formularioPanel.add(txtDataFim, gbc);
        
        // Período
        gbc.gridy = 2; gbc.gridx = 0;
        JLabel lblPeriodo = new JLabel("Período:");
        lblPeriodo.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        formularioPanel.add(lblPeriodo, gbc);
        
        gbc.gridx = 1; gbc.weightx = 1.0;
        comboPeriodo = new JComboBox<>(new String[]{"Hoje", "Esta Semana", "Este Mês", "Personalizado"});
        comboPeriodo.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        comboPeriodo.setBorder(BorderFactory.createLineBorder(GRAY));
        comboPeriodo.addActionListener(e -> atualizarDatasPorPeriodo());
        formularioPanel.add(comboPeriodo, gbc);
        
        // Tipo Relatório
        gbc.gridx = 2; gbc.weightx = 0.0;
        JLabel lblTipoRelatorio = new JLabel("Tipo Relatório:");
        lblTipoRelatorio.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        formularioPanel.add(lblTipoRelatorio, gbc);
        
        gbc.gridx = 3; gbc.weightx = 1.0;
        comboTipoRelatorio = new JComboBox<>(new String[]{"Completo", "Receitas", "Despesas", "Fluxo Caixa", "Lucratividade"});
        comboTipoRelatorio.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        comboTipoRelatorio.setBorder(BorderFactory.createLineBorder(GRAY));
        formularioPanel.add(comboTipoRelatorio, gbc);
        
        // Seção Resumo Financeiro
        gbc.gridy = 3; gbc.gridx = 0; gbc.gridwidth = 4; gbc.weightx = 0.0;
        JLabel resumoSectionLabel = new JLabel("💰 Resumo Financeiro");
        resumoSectionLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        resumoSectionLabel.setForeground(ACCENT_COLOR);
        formularioPanel.add(resumoSectionLabel, gbc);
        
        // Total Receitas
        gbc.gridy = 4; gbc.gridwidth = 1;
        JLabel lblTotalReceitas = new JLabel("Total Receitas:");
        lblTotalReceitas.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        formularioPanel.add(lblTotalReceitas, gbc);
        
        gbc.gridx = 1; gbc.weightx = 1.0;
        txtTotalReceitas = new JTextField();
        txtTotalReceitas.setFont(new Font("Segoe UI", Font.BOLD, 12));
        txtTotalReceitas.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(SUCCESS_COLOR),
            BorderFactory.createEmptyBorder(5, 8, 5, 8)
        ));
        txtTotalReceitas.setForeground(SUCCESS_COLOR);
        txtTotalReceitas.setEditable(false);
        formularioPanel.add(txtTotalReceitas, gbc);
        
        // Total Despesas
        gbc.gridx = 2; gbc.weightx = 0.0;
        JLabel lblTotalDespesas = new JLabel("Total Despesas:");
        lblTotalDespesas.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        formularioPanel.add(lblTotalDespesas, gbc);
        
        gbc.gridx = 3; gbc.weightx = 1.0;
        txtTotalDespesas = new JTextField();
        txtTotalDespesas.setFont(new Font("Segoe UI", Font.BOLD, 12));
        txtTotalDespesas.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(DANGER_COLOR),
            BorderFactory.createEmptyBorder(5, 8, 5, 8)
        ));
        txtTotalDespesas.setForeground(DANGER_COLOR);
        txtTotalDespesas.setEditable(false);
        formularioPanel.add(txtTotalDespesas, gbc);
        
        // Saldo Líquido
        gbc.gridy = 5; gbc.gridx = 0;
        JLabel lblSaldoLiquido = new JLabel("Saldo Líquido:");
        lblSaldoLiquido.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        formularioPanel.add(lblSaldoLiquido, gbc);
        
        gbc.gridx = 1; gbc.weightx = 1.0;
        txtSaldoLiquido = new JTextField();
        txtSaldoLiquido.setFont(new Font("Segoe UI", Font.BOLD, 12));
        txtSaldoLiquido.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(ACCENT_COLOR),
            BorderFactory.createEmptyBorder(5, 8, 5, 8)
        ));
        txtSaldoLiquido.setForeground(ACCENT_COLOR);
        txtSaldoLiquido.setEditable(false);
        formularioPanel.add(txtSaldoLiquido, gbc);
        
        // Margem Lucro
        gbc.gridx = 2; gbc.weightx = 0.0;
        JLabel lblMargemLucro = new JLabel("Margem Lucro:");
        lblMargemLucro.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        formularioPanel.add(lblMargemLucro, gbc);
        
        gbc.gridx = 3; gbc.weightx = 1.0;
        txtMargemLucro = new JTextField();
        txtMargemLucro.setFont(new Font("Segoe UI", Font.BOLD, 12));
        txtMargemLucro.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(SUCCESS_COLOR),
            BorderFactory.createEmptyBorder(5, 8, 5, 8)
        ));
        txtMargemLucro.setForeground(SUCCESS_COLOR);
        txtMargemLucro.setEditable(false);
        formularioPanel.add(txtMargemLucro, gbc);
        
        // Botões de ação
        gbc.gridy = 6; gbc.gridx = 0; gbc.gridwidth = 4; gbc.weightx = 0.0;
        JPanel botoesPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        botoesPanel.setBackground(WHITE);
        
        JButton btnGerar = new JButton("📊 Gerar Relatório");
        btnGerar.setBackground(ACCENT_COLOR);
        btnGerar.setForeground(WHITE);
        btnGerar.setFocusPainted(false);
        btnGerar.setBorderPainted(false);
        btnGerar.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btnGerar.addActionListener(e -> gerarRelatorio());
        
        JButton btnLimpar = new JButton("🧹 Limpar");
        btnLimpar.setBackground(GRAY);
        btnLimpar.setForeground(WHITE);
        btnLimpar.setFocusPainted(false);
        btnLimpar.setBorderPainted(false);
        btnLimpar.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btnLimpar.addActionListener(e -> limparCampos());
        
        JButton btnExportar = new JButton("📄 Exportar");
        btnExportar.setBackground(SUCCESS_COLOR);
        btnExportar.setForeground(WHITE);
        btnExportar.setFocusPainted(false);
        btnExportar.setBorderPainted(false);
        btnExportar.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btnExportar.addActionListener(e -> exportarDados());
        
        botoesPanel.add(btnGerar);
        botoesPanel.add(Box.createHorizontalStrut(10));
        botoesPanel.add(btnLimpar);
        botoesPanel.add(Box.createHorizontalStrut(10));
        botoesPanel.add(btnExportar);
        
        formularioPanel.add(botoesPanel, gbc);
        
        // Preencher dados iniciais
        preencherDadosIniciais();
        
        return formularioPanel;
    }
    
    /**
     * Cria o painel da tabela
     */
    private JPanel criarPainelTabela() {
        JPanel tabelaPanel = new JPanel(new BorderLayout());
        tabelaPanel.setBackground(WHITE);
        tabelaPanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 20, 20));
        
        JLabel tabelaLabel = new JLabel("📋 Itens do Relatório Financeiro");
        tabelaLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        tabelaLabel.setForeground(ACCENT_COLOR);
        
        // Modelo da tabela
        String[] colunas = {"Data", "Descrição", "Categoria", "Tipo", "Valor", "Forma Pagamento", "Status", "Ações"};
        modelTabela = new DefaultTableModel(colunas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 7; // Apenas a coluna de ações é editável
            }
        };
        
        tabelaRelatorio = new JTable(modelTabela);
        tabelaRelatorio.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        tabelaRelatorio.setRowHeight(25);
        tabelaRelatorio.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 12));
        tabelaRelatorio.getTableHeader().setBackground(ACCENT_COLOR);
        tabelaRelatorio.getTableHeader().setForeground(WHITE);
        
        // Scroll pane
        JScrollPane scrollPane = new JScrollPane(tabelaRelatorio);
        scrollPane.setPreferredSize(new Dimension(0, 250));
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
        btnDetalhes.addActionListener(e -> verDetalhes());
        
        JButton btnImprimir = new JButton("🖨️ Imprimir");
        btnImprimir.setBackground(WARNING_COLOR);
        btnImprimir.setForeground(WHITE);
        btnImprimir.setFocusPainted(false);
        btnImprimir.setBorderPainted(false);
        btnImprimir.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btnImprimir.addActionListener(e -> imprimirRelatorio());
        
        botoesTabelaPanel.add(btnDetalhes);
        botoesTabelaPanel.add(Box.createHorizontalStrut(10));
        botoesTabelaPanel.add(btnImprimir);
        
        tabelaPanel.add(tabelaLabel, BorderLayout.NORTH);
        tabelaPanel.add(scrollPane, BorderLayout.CENTER);
        tabelaPanel.add(botoesTabelaPanel, BorderLayout.SOUTH);
        
        // Adicionar dados de exemplo
        adicionarDadosExemplo();
        
        return tabelaPanel;
    }
    
    /**
     * Adiciona dados de exemplo à tabela
     */
    private void adicionarDadosExemplo() {
        // Limpar tabela atual
        modelTabela.setRowCount(0);
        itensRelatorio.clear();
        
        // Dados de exemplo
        Object[][] dadosExemplo = {
            {"10/05/2026", "Venda Notebook Dell", "Vendas", "Receita", "R$ 3.500,00", "Cartão Crédito", "Concluído", "👁️"},
            {"10/05/2026", "Venda Mouse Logitech", "Vendas", "Receita", "R$ 450,00", "PIX", "Concluído", "👁️"},
            {"09/05/2026", "Pagamento Aluguel", "Despesas Fixas", "Despesa", "R$ 1.200,00", "Transferência", "Pago", "👁️"},
            {"09/05/2026", "Conta Luz", "Despesas Fixas", "Despesa", "R$ 280,00", "PIX", "Pago", "👁️"},
            {"08/05/2026", "Venda Teclado Mecânico", "Vendas", "Receita", "R$ 320,00", "Dinheiro", "Concluído", "👁️"},
            {"08/05/2026", "Internet Plus", "Despesas Fixas", "Despesa", "R$ 150,00", "Boleto", "Pago", "👁️"},
            {"07/05/2026", "Serviço Técnico", "Serviços", "Receita", "R$ 800,00", "Dinheiro", "Concluído", "👁️"},
            {"07/05/2026", "Material Escritório", "Suprimentos", "Despesa", "R$ 120,00", "Dinheiro", "Pago", "👁️"}
        };
        
        for (Object[] dados : dadosExemplo) {
            modelTabela.addRow(dados);
            
            // Adicionar à lista de itens
            ItemRelatorio item = new ItemRelatorio();
            item.setData((String) dados[0]);
            item.setDescricao((String) dados[1]);
            item.setCategoria((String) dados[2]);
            item.setTipo((String) dados[3]);
            item.setValor((String) dados[4]);
            item.setFormaPagamento((String) dados[5]);
            item.setStatus((String) dados[6]);
            itensRelatorio.add(item);
        }
    }
    
    /**
     * Atualiza datas por período
     */
    private void atualizarDatasPorPeriodo() {
        String periodo = (String) comboPeriodo.getSelectedItem();
        
        if ("Personalizado".equals(periodo)) {
            txtDataInicio.setEditable(true);
            txtDataFim.setEditable(true);
        } else {
            txtDataInicio.setEditable(false);
            txtDataFim.setEditable(false);
            
            // Preencher datas automáticas
            java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("dd/MM/yyyy");
            String hoje = sdf.format(new java.util.Date());
            
            switch (periodo) {
                case "Hoje":
                    txtDataInicio.setText(hoje);
                    txtDataFim.setText(hoje);
                    break;
                case "Esta Semana":
                    txtDataInicio.setText("06/05/2026");
                    txtDataFim.setText("12/05/2026");
                    break;
                case "Este Mês":
                    txtDataInicio.setText("01/05/2026");
                    txtDataFim.setText("31/05/2026");
                    break;
                default:
                    txtDataInicio.setText("");
                    txtDataFim.setText("");
            }
        }
    }
    
    /**
     * Preenche dados iniciais
     */
    private void preencherDadosIniciais() {
        // Simular cálculos
        double totalReceitas = 5070.00;
        double totalDespesas = 1750.00;
        double saldoLiquido = totalReceitas - totalDespesas;
        double margemLucro = (saldoLiquido / totalReceitas) * 100;
        
        txtTotalReceitas.setText(String.format("R$ %,.2f", totalReceitas));
        txtTotalDespesas.setText(String.format("R$ %,.2f", totalDespesas));
        txtSaldoLiquido.setText(String.format("R$ %,.2f", saldoLiquido));
        txtMargemLucro.setText(String.format("%.1f%%", margemLucro));
        
        // Preencher datas
        atualizarDatasPorPeriodo();
    }
    
    /**
     * Busca relatório
     */
    private void buscarRelatorio() {
        String termo = txtBusca.getText().trim();
        if (termo.isEmpty()) {
            JOptionPane.showMessageDialog(workArea, "Digite um termo para buscar!", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        // TODO: Implementar lógica de busca no banco de dados
        JOptionPane.showMessageDialog(workArea, 
            "Busca realizada para: " + termo + "\n" +
            "Itens encontrados: " + itensRelatorio.size(), 
            "Resultado", JOptionPane.INFORMATION_MESSAGE);
        
        SystemLogger.ui("Busca realizada para: " + termo);
    }
    
    /**
     * Gera relatório
     */
    private void gerarRelatorio() {
        try {
            // TODO: Implementar lógica de geração no banco de dados
            JOptionPane.showMessageDialog(workArea, 
                "Relatório financeiro gerado com sucesso!\n\n" +
                "Período: " + comboPeriodo.getSelectedItem() + "\n" +
                "Tipo: " + comboTipoRelatorio.getSelectedItem() + "\n" +
                "Total Receitas: " + txtTotalReceitas.getText() + "\n" +
                "Total Despesas: " + txtTotalDespesas.getText() + "\n" +
                "Saldo Líquido: " + txtSaldoLiquido.getText() + "\n" +
                "Margem Lucro: " + txtMargemLucro.getText(), 
                "Sucesso", JOptionPane.INFORMATION_MESSAGE);
            
            SystemLogger.ui("Relatório gerado - " + itensRelatorio.size() + " itens");
            
        } catch (Exception e) {
            SystemLogger.error("Erro ao gerar relatório", e);
            JOptionPane.showMessageDialog(workArea, "Erro ao gerar relatório: " + e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    /**
     * Limpa os campos do formulário
     */
    private void limparCampos() {
        txtBusca.setText("");
        comboPeriodo.setSelectedIndex(0);
        comboTipoRelatorio.setSelectedIndex(0);
        txtDataInicio.setText("");
        txtDataFim.setText("");
        txtTotalReceitas.setText("");
        txtTotalDespesas.setText("");
        txtSaldoLiquido.setText("");
        txtMargemLucro.setText("");
        
        atualizarDatasPorPeriodo();
        preencherDadosIniciais();
    }
    
    /**
     * Ver detalhes do item
     */
    private void verDetalhes() {
        int selectedRow = tabelaRelatorio.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(workArea, "Selecione um item para ver detalhes!", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        ItemRelatorio item = itensRelatorio.get(selectedRow);
        
        JOptionPane.showMessageDialog(workArea, 
            "Detalhes do Item:\n\n" +
            "Data: " + item.getData() + "\n" +
            "Descrição: " + item.getDescricao() + "\n" +
            "Categoria: " + item.getCategoria() + "\n" +
            "Tipo: " + item.getTipo() + "\n" +
            "Valor: " + item.getValor() + "\n" +
            "Forma Pagamento: " + item.getFormaPagamento() + "\n" +
            "Status: " + item.getStatus(), 
            "Detalhes do Item", JOptionPane.INFORMATION_MESSAGE);
        
        SystemLogger.ui("Detalhes visualizados: " + item.getDescricao());
    }
    
    /**
     * Imprime relatório
     */
    private void imprimirRelatorio() {
        JOptionPane.showMessageDialog(workArea, 
            "Imprimindo relatório financeiro...\n" +
            "Período: " + comboPeriodo.getSelectedItem() + "\n" +
            "Registros: " + itensRelatorio.size() + "\n" +
            "(Implementar impressão em impressora fiscal)", 
            "Imprimir Relatório", JOptionPane.INFORMATION_MESSAGE);
        
        SystemLogger.ui("Imprimindo relatório - " + itensRelatorio.size() + " itens");
    }
    
    /**
     * Exporta dados da tabela
     */
    private void exportarDados() {
        JOptionPane.showMessageDialog(workArea, 
            "Exportando " + itensRelatorio.size() + " itens...\n(Implementar exportação para CSV/Excel)", 
            "Exportar Dados", JOptionPane.INFORMATION_MESSAGE);
        
        SystemLogger.ui("Exportando " + itensRelatorio.size() + " itens");
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
     * Classe interna para representar um item do relatório financeiro
     */
    private static class ItemRelatorio {
        private String data;
        private String descricao;
        private String categoria;
        private String tipo;
        private String valor;
        private String formaPagamento;
        private String status;
        
        // Getters e Setters
        public String getData() { return data; }
        public void setData(String data) { this.data = data; }
        
        public String getDescricao() { return descricao; }
        public void setDescricao(String descricao) { this.descricao = descricao; }
        
        public String getCategoria() { return categoria; }
        public void setCategoria(String categoria) { this.categoria = categoria; }
        
        public String getTipo() { return tipo; }
        public void setTipo(String tipo) { this.tipo = tipo; }
        
        public String getValor() { return valor; }
        public void setValor(String valor) { this.valor = valor; }
        
        public String getFormaPagamento() { return formaPagamento; }
        public void setFormaPagamento(String formaPagamento) { this.formaPagamento = formaPagamento; }
        
        public String getStatus() { return status; }
        public void setStatus(String status) { this.status = status; }
    }
}
