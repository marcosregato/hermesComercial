package com.br.hermescomercial.pdv.controller;

import com.br.hermescomercial.util.SystemLogger;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Classe especializada para o formulário de Resumo Financeiro
 * Estrutura: Header → Busca → Formulário → Tabela
 */
public class PDVFormularioResumoFinanceiro {
    
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
    private JTextField txtSaldo;
    private JTextField txtLucro;
    private JTextArea txtObservacoes;
    
    // Tabela de resumo
    private JTable tabelaResumo;
    private DefaultTableModel modelTabela;
    private List<ResumoFinanceiro> resumosEncontrados;
    
    // Cores
    private static final Color WHITE = Color.WHITE;
    private static final Color ACCENT_COLOR = new Color(52, 152, 219);
    private static final Color SUCCESS_COLOR = new Color(39, 174, 96);
    private static final Color DANGER_COLOR = new Color(231, 76, 60);
    private static final Color WARNING_COLOR = new Color(241, 196, 15);
    private static final Color GRAY = new Color(149, 165, 166);
    
    public PDVFormularioResumoFinanceiro(JPanel workArea, String usuarioAtual, String nomeUsuario) {
        this.workArea = workArea;
        this.usuarioAtual = usuarioAtual;
        this.nomeUsuario = nomeUsuario;
        this.resumosEncontrados = new ArrayList<>();
    }
    
    /**
     * Cria o formulário completo de Resumo Financeiro
     */
    public JPanel criarFormularioResumoFinanceiro() {
        try {
            SystemLogger.ui("=== CRIANDO FORMULÁRIO RESUMO FINANCEIRO ===");
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
            
            // Tabela de resumo
            JPanel tabelaPanel = criarPainelTabela();
            painelCentral.add(tabelaPanel, BorderLayout.SOUTH);
            
            painelPrincipal.add(painelCentral, BorderLayout.CENTER);
            
            SystemLogger.ui("Formulário Resumo Financeiro criado com sucesso");
            return painelPrincipal;
            
        } catch (Exception e) {
            SystemLogger.error("Erro ao criar formulário Resumo Financeiro", e);
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
        JLabel titleLabel = new JLabel("💰 Resumo Financeiro");
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
        txtBusca.setToolTipText("Digite descrição ou categoria");
        
        JButton btnBuscar = new JButton("🔍 Buscar");
        btnBuscar.setBackground(ACCENT_COLOR);
        btnBuscar.setForeground(WHITE);
        btnBuscar.setFocusPainted(false);
        btnBuscar.setBorderPainted(false);
        btnBuscar.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btnBuscar.addActionListener(e -> buscarResumo());
        
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
        
        // Seção Filtros de Período
        JLabel filtrosSectionLabel = new JLabel("📅 Filtros de Período");
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
        comboPeriodo = new JComboBox<>(new String[]{"Hoje", "Esta Semana", "Este Mês", "Personalizado"});
        comboPeriodo.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        comboPeriodo.setBorder(BorderFactory.createLineBorder(GRAY));
        comboPeriodo.addActionListener(e -> atualizarCamposPeriodo());
        formularioPanel.add(comboPeriodo, gbc);
        
        // Tipo Relatório
        gbc.gridx = 2; gbc.weightx = 0.0;
        JLabel lblTipoRelatorio = new JLabel("Tipo Relatório:");
        lblTipoRelatorio.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        formularioPanel.add(lblTipoRelatorio, gbc);
        
        gbc.gridx = 3; gbc.weightx = 1.0;
        comboTipoRelatorio = new JComboBox<>(new String[]{"Completo", "Receitas", "Despesas", "Lucro/Prejuízo"});
        comboTipoRelatorio.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        comboTipoRelatorio.setBorder(BorderFactory.createLineBorder(GRAY));
        formularioPanel.add(comboTipoRelatorio, gbc);
        
        // Data Início
        gbc.gridy = 2; gbc.gridx = 0;
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
        
        // Saldo
        gbc.gridy = 5; gbc.gridx = 0;
        JLabel lblSaldo = new JLabel("Saldo:");
        lblSaldo.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        formularioPanel.add(lblSaldo, gbc);
        
        gbc.gridx = 1; gbc.weightx = 1.0;
        txtSaldo = new JTextField();
        txtSaldo.setFont(new Font("Segoe UI", Font.BOLD, 12));
        txtSaldo.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(ACCENT_COLOR),
            BorderFactory.createEmptyBorder(5, 8, 5, 8)
        ));
        txtSaldo.setForeground(ACCENT_COLOR);
        txtSaldo.setEditable(false);
        formularioPanel.add(txtSaldo, gbc);
        
        // Lucro
        gbc.gridx = 2; gbc.weightx = 0.0;
        JLabel lblLucro = new JLabel("Lucro:");
        lblLucro.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        formularioPanel.add(lblLucro, gbc);
        
        gbc.gridx = 3; gbc.weightx = 1.0;
        txtLucro = new JTextField();
        txtLucro.setFont(new Font("Segoe UI", Font.BOLD, 12));
        txtLucro.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(SUCCESS_COLOR),
            BorderFactory.createEmptyBorder(5, 8, 5, 8)
        ));
        txtLucro.setForeground(SUCCESS_COLOR);
        txtLucro.setEditable(false);
        formularioPanel.add(txtLucro, gbc);
        
        // Botões de ação
        gbc.gridy = 6; gbc.gridx = 0; gbc.gridwidth = 4; gbc.weightx = 0.0;
        JPanel botoesPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        botoesPanel.setBackground(WHITE);
        
        JButton btnGerar = new JButton("📊 Gerar Resumo");
        btnGerar.setBackground(ACCENT_COLOR);
        btnGerar.setForeground(WHITE);
        btnGerar.setFocusPainted(false);
        btnGerar.setBorderPainted(false);
        btnGerar.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btnGerar.addActionListener(e -> gerarResumo());
        
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
        
        JLabel tabelaLabel = new JLabel("📋 Detalhes Financeiros");
        tabelaLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        tabelaLabel.setForeground(ACCENT_COLOR);
        
        // Modelo da tabela
        String[] colunas = {"Data", "Descrição", "Categoria", "Tipo", "Valor", "Status", "Ações"};
        modelTabela = new DefaultTableModel(colunas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 6; // Apenas a coluna de ações é editável
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
        
        JButton btnRelatorio = new JButton("📊 Relatório Completo");
        btnRelatorio.setBackground(WARNING_COLOR);
        btnRelatorio.setForeground(WHITE);
        btnRelatorio.setFocusPainted(false);
        btnRelatorio.setBorderPainted(false);
        btnRelatorio.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btnRelatorio.addActionListener(e -> gerarRelatorioCompleto());
        
        botoesTabelaPanel.add(btnDetalhes);
        botoesTabelaPanel.add(Box.createHorizontalStrut(10));
        botoesTabelaPanel.add(btnRelatorio);
        
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
        resumosEncontrados.clear();
        
        // Dados de exemplo
        Object[][] dadosExemplo = {
            {"10/05/2026", "Venda Notebook", "Vendas", "Receita", "R$ 3.500,00", "Concluído", "👁️"},
            {"10/05/2026", "Aluguel Loja", "Despesas Fixas", "Despesa", "R$ 1.200,00", "Pago", "👁️"},
            {"09/05/2026", "Venda Mouse", "Vendas", "Receita", "R$ 450,00", "Concluído", "👁️"},
            {"09/05/2026", "Telefone", "Despesas Fixas", "Despesa", "R$ 150,00", "Pago", "👁️"},
            {"08/05/2026", "Serviço Técnico", "Serviços", "Receita", "R$ 800,00", "Concluído", "👁️"}
        };
        
        for (Object[] dados : dadosExemplo) {
            modelTabela.addRow(dados);
            
            // Adicionar à lista de resumos
            ResumoFinanceiro resumo = new ResumoFinanceiro();
            resumo.setData((String) dados[0]);
            resumo.setDescricao((String) dados[1]);
            resumo.setCategoria((String) dados[2]);
            resumo.setTipo((String) dados[3]);
            resumo.setValor((String) dados[4]);
            resumo.setStatus((String) dados[5]);
            resumosEncontrados.add(resumo);
        }
    }
    
    /**
     * Atualiza campos de período
     */
    private void atualizarCamposPeriodo() {
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
        double totalReceitas = 4750.00;
        double totalDespesas = 1350.00;
        double saldo = totalReceitas - totalDespesas;
        double lucro = saldo * 0.85; // Após impostos
        
        txtTotalReceitas.setText(String.format("R$ %,.2f", totalReceitas));
        txtTotalDespesas.setText(String.format("R$ %,.2f", totalDespesas));
        txtSaldo.setText(String.format("R$ %,.2f", saldo));
        txtLucro.setText(String.format("R$ %,.2f", lucro));
        
        // Preencher datas
        atualizarCamposPeriodo();
    }
    
    /**
     * Busca resumo
     */
    private void buscarResumo() {
        String termo = txtBusca.getText().trim();
        if (termo.isEmpty()) {
            JOptionPane.showMessageDialog(workArea, "Digite um termo para buscar!", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        // TODO: Implementar lógica de busca no banco de dados
        JOptionPane.showMessageDialog(workArea, 
            "Busca realizada para: " + termo + "\n" +
            "Registros encontrados: " + resumosEncontrados.size(), 
            "Resultado", JOptionPane.INFORMATION_MESSAGE);
        
        SystemLogger.ui("Busca realizada para: " + termo);
    }
    
    /**
     * Gera resumo
     */
    private void gerarResumo() {
        try {
            // TODO: Implementar lógica de geração no banco de dados
            JOptionPane.showMessageDialog(workArea, 
                "Resumo financeiro gerado com sucesso!\n" +
                "Período: " + comboPeriodo.getSelectedItem() + "\n" +
                "Total Receitas: " + txtTotalReceitas.getText() + "\n" +
                "Total Despesas: " + txtTotalDespesas.getText() + "\n" +
                "Saldo: " + txtSaldo.getText(), 
                "Sucesso", JOptionPane.INFORMATION_MESSAGE);
            
            SystemLogger.ui("Resumo financeiro gerado - " + resumosEncontrados.size() + " registros");
            
        } catch (Exception e) {
            SystemLogger.error("Erro ao gerar resumo", e);
            JOptionPane.showMessageDialog(workArea, "Erro ao gerar resumo: " + e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
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
        txtSaldo.setText("");
        txtLucro.setText("");
        
        atualizarCamposPeriodo();
        preencherDadosIniciais();
    }
    
    /**
     * Ver detalhes do resumo
     */
    private void verDetalhes() {
        int selectedRow = tabelaResumo.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(workArea, "Selecione um registro para ver detalhes!", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        ResumoFinanceiro resumo = resumosEncontrados.get(selectedRow);
        
        JOptionPane.showMessageDialog(workArea, 
            "Detalhes Financeiros:\n\n" +
            "Data: " + resumo.getData() + "\n" +
            "Descrição: " + resumo.getDescricao() + "\n" +
            "Categoria: " + resumo.getCategoria() + "\n" +
            "Tipo: " + resumo.getTipo() + "\n" +
            "Valor: " + resumo.getValor() + "\n" +
            "Status: " + resumo.getStatus(), 
            "Detalhes Financeiros", JOptionPane.INFORMATION_MESSAGE);
        
        SystemLogger.ui("Detalhes visualizados: " + resumo.getDescricao());
    }
    
    /**
     * Gera relatório completo
     */
    private void gerarRelatorioCompleto() {
        JOptionPane.showMessageDialog(workArea, 
            "Gerando relatório financeiro completo...\n" +
            "Período: " + comboPeriodo.getSelectedItem() + "\n" +
            "Registros: " + resumosEncontrados.size() + "\n" +
            "(Implementar geração de relatório PDF/Excel)", 
            "Gerar Relatório", JOptionPane.INFORMATION_MESSAGE);
        
        SystemLogger.ui("Gerando relatório completo - " + resumosEncontrados.size() + " registros");
    }
    
    /**
     * Exporta dados da tabela
     */
    private void exportarDados() {
        JOptionPane.showMessageDialog(workArea, 
            "Exportando " + resumosEncontrados.size() + " registros...\n(Implementar exportação para CSV/Excel)", 
            "Exportar Dados", JOptionPane.INFORMATION_MESSAGE);
        
        SystemLogger.ui("Exportando " + resumosEncontrados.size() + " registros");
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
     * Classe interna para representar um resumo financeiro
     */
    private static class ResumoFinanceiro {
        private String data;
        private String descricao;
        private String categoria;
        private String tipo;
        private String valor;
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
        
        public String getStatus() { return status; }
        public void setStatus(String status) { this.status = status; }
    }
}
