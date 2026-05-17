package com.br.hermescomercial.pdv.controller;

import com.br.hermescomercial.pdv.model.FluxoCaixa;
import com.br.hermescomercial.util.SystemLogger;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Classe especializada para o formulário de Fluxo de Caixa
 * Estrutura: Header → Busca → Formulário → Tabela
 */
public class PDVFormularioFluxoCaixa {
    
    private JPanel workArea;
    private String usuarioAtual;
    private String nomeUsuario;
    
    // Componentes do formulário
    private JTextField txtBusca;
    private JTextField txtDataInicio;
    private JTextField txtDataFim;
    private JComboBox<String> comboPeriodo;
    private JComboBox<String> comboTipoFluxo;
    private JTextField txtSaldoInicial;
    private JTextField txtTotalEntradas;
    private JTextField txtTotalSaidas;
    private JTextField txtSaldoFinal;
    
    // Tabela de fluxo de caixa
    private JTable tabelaFluxo;
    private DefaultTableModel modelTabela;
    private List<FluxoCaixa> fluxosEncontrados;
    
    // Cores
    private static final Color WHITE = Color.WHITE;
    private static final Color ACCENT_COLOR = new Color(52, 152, 219);
    private static final Color SUCCESS_COLOR = new Color(39, 174, 96);
    private static final Color DANGER_COLOR = new Color(231, 76, 60);
    private static final Color WARNING_COLOR = new Color(241, 196, 15);
    private static final Color GRAY = new Color(149, 165, 166);
    
    public PDVFormularioFluxoCaixa(JPanel workArea, String usuarioAtual, String nomeUsuario) {
        this.workArea = workArea;
        this.usuarioAtual = usuarioAtual;
        this.nomeUsuario = nomeUsuario;
        this.fluxosEncontrados = new ArrayList<>();
    }
    
    /**
     * Cria o formulário completo de Fluxo de Caixa
     */
    public JPanel criarFormularioFluxoCaixa() {
        try {
            SystemLogger.ui("=== CRIANDO FORMULÁRIO FLUXO CAIXA ===");
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
            
            // Tabela de fluxo de caixa
            JPanel tabelaPanel = criarPainelTabela();
            painelCentral.add(tabelaPanel, BorderLayout.SOUTH);
            
            painelPrincipal.add(painelCentral, BorderLayout.CENTER);
            
            SystemLogger.ui("Formulário Fluxo Caixa criado com sucesso");
            return painelPrincipal;
            
        } catch (Exception e) {
            SystemLogger.error("Erro ao criar formulário Fluxo Caixa", e);
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
        JLabel titleLabel = new JLabel("📊 Fluxo de Caixa");
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
        btnBuscar.addActionListener(e -> buscarFluxo());
        
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
        
        // Tipo Fluxo
        gbc.gridx = 2; gbc.weightx = 0.0;
        JLabel lblTipoFluxo = new JLabel("Tipo Fluxo:");
        lblTipoFluxo.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        formularioPanel.add(lblTipoFluxo, gbc);
        
        gbc.gridx = 3; gbc.weightx = 1.0;
        comboTipoFluxo = new JComboBox<>(new String[]{"Todos", "Entradas", "Saídas", "Saldo"});
        comboTipoFluxo.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        comboTipoFluxo.setBorder(BorderFactory.createLineBorder(GRAY));
        formularioPanel.add(comboTipoFluxo, gbc);
        
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
        
        // Seção Resumo do Fluxo
        gbc.gridy = 3; gbc.gridx = 0; gbc.gridwidth = 4; gbc.weightx = 0.0;
        JLabel resumoSectionLabel = new JLabel("💰 Resumo do Fluxo");
        resumoSectionLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        resumoSectionLabel.setForeground(ACCENT_COLOR);
        formularioPanel.add(resumoSectionLabel, gbc);
        
        // Saldo Inicial
        gbc.gridy = 4; gbc.gridwidth = 1;
        JLabel lblSaldoInicial = new JLabel("Saldo Inicial:");
        lblSaldoInicial.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        formularioPanel.add(lblSaldoInicial, gbc);
        
        gbc.gridx = 1; gbc.weightx = 1.0;
        txtSaldoInicial = new JTextField();
        txtSaldoInicial.setFont(new Font("Segoe UI", Font.BOLD, 12));
        txtSaldoInicial.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(ACCENT_COLOR),
            BorderFactory.createEmptyBorder(5, 8, 5, 8)
        ));
        txtSaldoInicial.setForeground(ACCENT_COLOR);
        txtSaldoInicial.setEditable(false);
        formularioPanel.add(txtSaldoInicial, gbc);
        
        // Total Entradas
        gbc.gridx = 2; gbc.weightx = 0.0;
        JLabel lblTotalEntradas = new JLabel("Total Entradas:");
        lblTotalEntradas.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        formularioPanel.add(lblTotalEntradas, gbc);
        
        gbc.gridx = 3; gbc.weightx = 1.0;
        txtTotalEntradas = new JTextField();
        txtTotalEntradas.setFont(new Font("Segoe UI", Font.BOLD, 12));
        txtTotalEntradas.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(SUCCESS_COLOR),
            BorderFactory.createEmptyBorder(5, 8, 5, 8)
        ));
        txtTotalEntradas.setForeground(SUCCESS_COLOR);
        txtTotalEntradas.setEditable(false);
        formularioPanel.add(txtTotalEntradas, gbc);
        
        // Total Saídas
        gbc.gridy = 5; gbc.gridx = 0;
        JLabel lblTotalSaidas = new JLabel("Total Saídas:");
        lblTotalSaidas.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        formularioPanel.add(lblTotalSaidas, gbc);
        
        gbc.gridx = 1; gbc.weightx = 1.0;
        txtTotalSaidas = new JTextField();
        txtTotalSaidas.setFont(new Font("Segoe UI", Font.BOLD, 12));
        txtTotalSaidas.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(DANGER_COLOR),
            BorderFactory.createEmptyBorder(5, 8, 5, 8)
        ));
        txtTotalSaidas.setForeground(DANGER_COLOR);
        txtTotalSaidas.setEditable(false);
        formularioPanel.add(txtTotalSaidas, gbc);
        
        // Saldo Final
        gbc.gridx = 2; gbc.weightx = 0.0;
        JLabel lblSaldoFinal = new JLabel("Saldo Final:");
        lblSaldoFinal.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        formularioPanel.add(lblSaldoFinal, gbc);
        
        gbc.gridx = 3; gbc.weightx = 1.0;
        txtSaldoFinal = new JTextField();
        txtSaldoFinal.setFont(new Font("Segoe UI", Font.BOLD, 12));
        txtSaldoFinal.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(SUCCESS_COLOR),
            BorderFactory.createEmptyBorder(5, 8, 5, 8)
        ));
        txtSaldoFinal.setForeground(SUCCESS_COLOR);
        txtSaldoFinal.setEditable(false);
        formularioPanel.add(txtSaldoFinal, gbc);
        
        // Botões de ação
        gbc.gridy = 6; gbc.gridx = 0; gbc.gridwidth = 4; gbc.weightx = 0.0;
        JPanel botoesPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        botoesPanel.setBackground(WHITE);
        
        JButton btnGerar = new JButton("📊 Gerar Fluxo");
        btnGerar.setBackground(ACCENT_COLOR);
        btnGerar.setForeground(WHITE);
        btnGerar.setFocusPainted(false);
        btnGerar.setBorderPainted(false);
        btnGerar.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btnGerar.addActionListener(e -> gerarFluxo());
        
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
        
        JLabel tabelaLabel = new JLabel("📋 Movimentações do Fluxo");
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
        
        tabelaFluxo = new JTable(modelTabela);
        tabelaFluxo.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        tabelaFluxo.setRowHeight(25);
        tabelaFluxo.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 12));
        tabelaFluxo.getTableHeader().setBackground(ACCENT_COLOR);
        tabelaFluxo.getTableHeader().setForeground(WHITE);
        
        // Scroll pane
        JScrollPane scrollPane = new JScrollPane(tabelaFluxo);
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
        fluxosEncontrados.clear();
        
        // Dados de exemplo
        Object[][] dadosExemplo = {
            {"10/05/2026", "Venda Notebook", "Vendas", "Entrada", "R$ 3.500,00", "Cartão", "Concluído", "👁️"},
            {"10/05/2026", "Pagamento Aluguel", "Despesas Fixas", "Saída", "R$ 1.200,00", "Transferência", "Pago", "👁️"},
            {"09/05/2026", "Venda Mouse", "Vendas", "Entrada", "R$ 450,00", "Dinheiro", "Concluído", "👁️"},
            {"09/05/2026", "Conta Luz", "Despesas Fixas", "Saída", "R$ 280,00", "PIX", "Pago", "👁️"},
            {"08/05/2026", "Serviço Técnico", "Serviços", "Entrada", "R$ 800,00", "Dinheiro", "Concluído", "👁️"}
        };
        
        for (Object[] dados : dadosExemplo) {
            modelTabela.addRow(dados);
            
            // Adicionar à lista de fluxos
            FluxoCaixa fluxo = new FluxoCaixa();
            fluxo.setData((String) dados[0]);
            fluxo.setDescricao((String) dados[1]);
            fluxo.setCategoria((String) dados[2]);
            fluxo.setTipo((String) dados[3]);
            fluxo.setValor((String) dados[4]);
            fluxo.setFormaPagamento((String) dados[5]);
            fluxo.setStatus((String) dados[6]);
            fluxosEncontrados.add(fluxo);
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
        double saldoInicial = 5000.00;
        double totalEntradas = 4750.00;
        double totalSaidas = 1480.00;
        double saldoFinal = saldoInicial + totalEntradas - totalSaidas;
        
        txtSaldoInicial.setText(String.format("R$ %,.2f", saldoInicial));
        txtTotalEntradas.setText(String.format("R$ %,.2f", totalEntradas));
        txtTotalSaidas.setText(String.format("R$ %,.2f", totalSaidas));
        txtSaldoFinal.setText(String.format("R$ %,.2f", saldoFinal));
        
        // Preencher datas
        atualizarCamposPeriodo();
    }
    
    /**
     * Busca fluxo
     */
    private void buscarFluxo() {
        String termo = txtBusca.getText().trim();
        if (termo.isEmpty()) {
            JOptionPane.showMessageDialog(workArea, "Digite um termo para buscar!", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        JOptionPane.showMessageDialog(workArea, 
            "Busca realizada para: " + termo + "\n" +
            "Registros encontrados: " + fluxosEncontrados.size(), 
            "Resultado", JOptionPane.INFORMATION_MESSAGE);
        
        SystemLogger.ui("Busca realizada para: " + termo);
    }
    
    /**
     * Gera fluxo
     */
    private void gerarFluxo() {
        try {
            JOptionPane.showMessageDialog(workArea, 
                "Fluxo de caixa gerado com sucesso!\n" +
                "Período: " + comboPeriodo.getSelectedItem() + "\n" +
                "Saldo Inicial: " + txtSaldoInicial.getText() + "\n" +
                "Total Entradas: " + txtTotalEntradas.getText() + "\n" +
                "Total Saídas: " + txtTotalSaidas.getText() + "\n" +
                "Saldo Final: " + txtSaldoFinal.getText(), 
                "Sucesso", JOptionPane.INFORMATION_MESSAGE);
            
            SystemLogger.ui("Fluxo de caixa gerado - " + fluxosEncontrados.size() + " registros");
            
        } catch (Exception e) {
            SystemLogger.error("Erro ao gerar fluxo", e);
            JOptionPane.showMessageDialog(workArea, "Erro ao gerar fluxo: " + e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    /**
     * Limpa os campos do formulário
     */
    private void limparCampos() {
        txtBusca.setText("");
        comboPeriodo.setSelectedIndex(0);
        comboTipoFluxo.setSelectedIndex(0);
        txtDataInicio.setText("");
        txtDataFim.setText("");
        txtSaldoInicial.setText("");
        txtTotalEntradas.setText("");
        txtTotalSaidas.setText("");
        txtSaldoFinal.setText("");
        
        atualizarCamposPeriodo();
        preencherDadosIniciais();
    }
    
    /**
     * Ver detalhes do fluxo
     */
    private void verDetalhes() {
        int selectedRow = tabelaFluxo.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(workArea, "Selecione um registro para ver detalhes!", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        FluxoCaixa fluxo = fluxosEncontrados.get(selectedRow);
        
        JOptionPane.showMessageDialog(workArea, 
            "Detalhes do Fluxo:\n\n" +
            "Data: " + fluxo.getData() + "\n" +
            "Descrição: " + fluxo.getDescricao() + "\n" +
            "Categoria: " + fluxo.getCategoria() + "\n" +
            "Tipo: " + fluxo.getTipo() + "\n" +
            "Valor: " + fluxo.getValor() + "\n" +
            "Forma Pagamento: " + fluxo.getFormaPagamento() + "\n" +
            "Status: " + fluxo.getStatus(), 
            "Detalhes do Fluxo", JOptionPane.INFORMATION_MESSAGE);
        
        SystemLogger.ui("Detalhes visualizados: " + fluxo.getDescricao());
    }
    
    /**
     * Gera relatório completo
     */
    private void gerarRelatorioCompleto() {
        JOptionPane.showMessageDialog(workArea, 
            "Gerando relatório completo de fluxo de caixa...\n" +
            "Período: " + comboPeriodo.getSelectedItem() + "\n" +
            "Registros: " + fluxosEncontrados.size() + "\n" +
            "(Implementar geração de relatório PDF/Excel)", 
            "Gerar Relatório", JOptionPane.INFORMATION_MESSAGE);
        
        SystemLogger.ui("Gerando relatório completo - " + fluxosEncontrados.size() + " registros");
    }
    
    /**
     * Exporta dados da tabela
     */
    private void exportarDados() {
        JOptionPane.showMessageDialog(workArea, 
            "Exportando " + fluxosEncontrados.size() + " registros...\n(Implementar exportação para CSV/Excel)", 
            "Exportar Dados", JOptionPane.INFORMATION_MESSAGE);
        
        SystemLogger.ui("Exportando " + fluxosEncontrados.size() + " registros");
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
}
