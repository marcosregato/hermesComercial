package com.br.hermescomercial.pdv.controller;

import com.br.hermescomercial.util.SystemLogger;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Classe especializada para o formulário de Movimentações de Estoque
 * Estrutura: Header → Busca → Formulário → Tabela
 */
public class PDVFormularioMovimentacoes {
    
    private JPanel workArea;
    private String usuarioAtual;
    private String nomeUsuario;
    
    // Componentes do formulário
    private JTextField txtBusca;
    private JTextField txtProduto;
    private JTextField txtCodigo;
    private JComboBox<String> comboTipoMovimentacao;
    private JComboBox<String> comboCategoria;
    private JTextField txtDataInicio;
    private JTextField txtDataFim;
    private JComboBox<String> comboOrigem;
    private JComboBox<String> comboDestino;
    private JTextArea txtObservacoes;
    
    // Tabela de movimentações
    private JTable tabelaMovimentacoes;
    private DefaultTableModel modelTabela;
    private List<MovimentacaoEstoque> movimentacoesEncontradas;
    
    // Cores
    private static final Color WHITE = Color.WHITE;
    private static final Color ACCENT_COLOR = new Color(52, 152, 219);
    private static final Color SUCCESS_COLOR = new Color(39, 174, 96);
    private static final Color DANGER_COLOR = new Color(231, 76, 60);
    private static final Color WARNING_COLOR = new Color(241, 196, 15);
    private static final Color GRAY = new Color(149, 165, 166);
    
    public PDVFormularioMovimentacoes(JPanel workArea, String usuarioAtual, String nomeUsuario) {
        this.workArea = workArea;
        this.usuarioAtual = usuarioAtual;
        this.nomeUsuario = nomeUsuario;
        this.movimentacoesEncontradas = new ArrayList<>();
    }
    
    /**
     * Cria o formulário completo de Movimentações de Estoque
     */
    public JPanel criarFormularioMovimentacoes() {
        try {
            SystemLogger.ui("=== CRIANDO FORMULÁRIO MOVIMENTAÇÕES ESTOQUE ===");
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
            
            // Tabela de movimentações
            JPanel tabelaPanel = criarPainelTabela();
            painelCentral.add(tabelaPanel, BorderLayout.SOUTH);
            
            painelPrincipal.add(painelCentral, BorderLayout.CENTER);
            
            SystemLogger.ui("Formulário Movimentações Estoque criado com sucesso");
            return painelPrincipal;
            
        } catch (Exception e) {
            SystemLogger.error("Erro ao criar formulário Movimentações Estoque", e);
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
        JLabel titleLabel = new JLabel("📦 Movimentações de Estoque");
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
        txtBusca.setToolTipText("Digite código, nome do produto ou tipo de movimentação");
        
        JButton btnBuscar = new JButton("🔍 Buscar");
        btnBuscar.setBackground(ACCENT_COLOR);
        btnBuscar.setForeground(WHITE);
        btnBuscar.setFocusPainted(false);
        btnBuscar.setBorderPainted(false);
        btnBuscar.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btnBuscar.addActionListener(e -> buscarMovimentacoes());
        
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
        
        // Seção Filtros de Movimentação
        JLabel filtrosSectionLabel = new JLabel("🔍 Filtros de Movimentação");
        filtrosSectionLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        filtrosSectionLabel.setForeground(ACCENT_COLOR);
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 4;
        formularioPanel.add(filtrosSectionLabel, gbc);
        
        // Produto
        gbc.gridy = 1; gbc.gridwidth = 1;
        JLabel lblProduto = new JLabel("Produto:");
        lblProduto.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        formularioPanel.add(lblProduto, gbc);
        
        gbc.gridx = 1; gbc.weightx = 1.0;
        txtProduto = new JTextField();
        txtProduto.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        txtProduto.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(GRAY),
            BorderFactory.createEmptyBorder(5, 8, 5, 8)
        ));
        formularioPanel.add(txtProduto, gbc);
        
        // Tipo de Movimentação
        gbc.gridx = 2; gbc.weightx = 0.0;
        JLabel lblTipoMovimentacao = new JLabel("Tipo Movimentação:");
        lblTipoMovimentacao.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        formularioPanel.add(lblTipoMovimentacao, gbc);
        
        gbc.gridx = 3; gbc.weightx = 1.0;
        comboTipoMovimentacao = new JComboBox<>(new String[]{"Todos", "Entrada", "Saída", "Transferência", "Devolução", "Perda", "Ajuste"});
        comboTipoMovimentacao.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        comboTipoMovimentacao.setBorder(BorderFactory.createLineBorder(GRAY));
        formularioPanel.add(comboTipoMovimentacao, gbc);
        
        // Código
        gbc.gridy = 2; gbc.gridx = 0;
        JLabel lblCodigo = new JLabel("Código:");
        lblCodigo.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        formularioPanel.add(lblCodigo, gbc);
        
        gbc.gridx = 1; gbc.weightx = 1.0;
        txtCodigo = new JTextField();
        txtCodigo.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        txtCodigo.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(GRAY),
            BorderFactory.createEmptyBorder(5, 8, 5, 8)
        ));
        formularioPanel.add(txtCodigo, gbc);
        
        // Categoria
        gbc.gridx = 2; gbc.weightx = 0.0;
        JLabel lblCategoria = new JLabel("Categoria:");
        lblCategoria.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        formularioPanel.add(lblCategoria, gbc);
        
        gbc.gridx = 3; gbc.weightx = 1.0;
        comboCategoria = new JComboBox<>(new String[]{"Todas", "Informática", "Eletrônicos", "Móveis", "Livros", "Vestuário"});
        comboCategoria.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        comboCategoria.setBorder(BorderFactory.createLineBorder(GRAY));
        formularioPanel.add(comboCategoria, gbc);
        
        // Seção Período
        gbc.gridy = 3; gbc.gridx = 0; gbc.gridwidth = 4; gbc.weightx = 0.0;
        JLabel periodoSectionLabel = new JLabel("📅 Período");
        periodoSectionLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        periodoSectionLabel.setForeground(ACCENT_COLOR);
        formularioPanel.add(periodoSectionLabel, gbc);
        
        // Data Início
        gbc.gridy = 4; gbc.gridwidth = 1;
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
        
        // Seção Localização
        gbc.gridy = 5; gbc.gridx = 0; gbc.gridwidth = 4; gbc.weightx = 0.0;
        JLabel localizacaoSectionLabel = new JLabel("📍 Localização");
        localizacaoSectionLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        localizacaoSectionLabel.setForeground(ACCENT_COLOR);
        formularioPanel.add(localizacaoSectionLabel, gbc);
        
        // Origem
        gbc.gridy = 6; gbc.gridwidth = 1;
        JLabel lblOrigem = new JLabel("Origem:");
        lblOrigem.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        formularioPanel.add(lblOrigem, gbc);
        
        gbc.gridx = 1; gbc.weightx = 1.0;
        comboOrigem = new JComboBox<>(new String[]{"Todas", "Almoxarifado", "Loja A", "Loja B", "Depósito", "Fornecedor"});
        comboOrigem.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        comboOrigem.setBorder(BorderFactory.createLineBorder(GRAY));
        formularioPanel.add(comboOrigem, gbc);
        
        // Destino
        gbc.gridx = 2; gbc.weightx = 0.0;
        JLabel lblDestino = new JLabel("Destino:");
        lblDestino.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        formularioPanel.add(lblDestino, gbc);
        
        gbc.gridx = 3; gbc.weightx = 1.0;
        comboDestino = new JComboBox<>(new String[]{"Todas", "Almoxarifado", "Loja A", "Loja B", "Depósito", "Cliente"});
        comboDestino.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        comboDestino.setBorder(BorderFactory.createLineBorder(GRAY));
        formularioPanel.add(comboDestino, gbc);
        
        // Botões de ação
        gbc.gridy = 7; gbc.gridx = 0; gbc.gridwidth = 4; gbc.weightx = 0.0;
        JPanel botoesPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        botoesPanel.setBackground(WHITE);
        
        JButton btnFiltrar = new JButton("🔍 Filtrar");
        btnFiltrar.setBackground(ACCENT_COLOR);
        btnFiltrar.setForeground(WHITE);
        btnFiltrar.setFocusPainted(false);
        btnFiltrar.setBorderPainted(false);
        btnFiltrar.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btnFiltrar.addActionListener(e -> filtrarMovimentacoes());
        
        JButton btnLimpar = new JButton("🧹 Limpar");
        btnLimpar.setBackground(GRAY);
        btnLimpar.setForeground(WHITE);
        btnLimpar.setFocusPainted(false);
        btnLimpar.setBorderPainted(false);
        btnLimpar.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btnLimpar.addActionListener(e -> limparCampos());
        
        JButton btnRelatorio = new JButton("📊 Relatório");
        btnRelatorio.setBackground(SUCCESS_COLOR);
        btnRelatorio.setForeground(WHITE);
        btnRelatorio.setFocusPainted(false);
        btnRelatorio.setBorderPainted(false);
        btnRelatorio.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btnRelatorio.addActionListener(e -> gerarRelatorio());
        
        botoesPanel.add(btnFiltrar);
        botoesPanel.add(Box.createHorizontalStrut(10));
        botoesPanel.add(btnLimpar);
        botoesPanel.add(Box.createHorizontalStrut(10));
        botoesPanel.add(btnRelatorio);
        
        formularioPanel.add(botoesPanel, gbc);
        
        return formularioPanel;
    }
    
    /**
     * Cria o painel da tabela
     */
    private JPanel criarPainelTabela() {
        JPanel tabelaPanel = new JPanel(new BorderLayout());
        tabelaPanel.setBackground(WHITE);
        tabelaPanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 20, 20));
        
        JLabel tabelaLabel = new JLabel("📋 Histórico de Movimentações");
        tabelaLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        tabelaLabel.setForeground(ACCENT_COLOR);
        
        // Modelo da tabela
        String[] colunas = {"Código", "Data/Hora", "Produto", "Tipo", "Quantidade", "Origem", "Destino", "Responsável", "Status", "Ações"};
        modelTabela = new DefaultTableModel(colunas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 9; // Apenas a coluna de ações é editável
            }
        };
        
        tabelaMovimentacoes = new JTable(modelTabela);
        tabelaMovimentacoes.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        tabelaMovimentacoes.setRowHeight(25);
        tabelaMovimentacoes.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 12));
        tabelaMovimentacoes.getTableHeader().setBackground(ACCENT_COLOR);
        tabelaMovimentacoes.getTableHeader().setForeground(WHITE);
        
        // Scroll pane
        JScrollPane scrollPane = new JScrollPane(tabelaMovimentacoes);
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
        
        JButton btnExportar = new JButton("📄 Exportar");
        btnExportar.setBackground(WARNING_COLOR);
        btnExportar.setForeground(WHITE);
        btnExportar.setFocusPainted(false);
        btnExportar.setBorderPainted(false);
        btnExportar.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btnExportar.addActionListener(e -> exportarDados());
        
        botoesTabelaPanel.add(btnDetalhes);
        botoesTabelaPanel.add(Box.createHorizontalStrut(10));
        botoesTabelaPanel.add(btnExportar);
        
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
        movimentacoesEncontradas.clear();
        
        // Dados de exemplo
        Object[][] dadosExemplo = {
            {"MOV-001", "10/05/2026 14:30", "Notebook Dell", "Entrada", "5", "Fornecedor", "Almoxarifado", "admin", "Concluída", "👁️"},
            {"MOV-002", "09/05/2026 10:15", "Mouse Wireless", "Saída", "2", "Almoxarifado", "Loja A", "joao", "Concluída", "👁️"},
            {"MOV-003", "08/05/2026 16:45", "Monitor 24\"", "Transferência", "3", "Almoxarifado", "Loja B", "maria", "Concluída", "👁️"},
            {"MOV-004", "07/05/2026 09:20", "Cadeira Executiva", "Devolução", "1", "Loja A", "Almoxarifado", "pedro", "Processando", "👁️"},
            {"MOV-005", "06/05/2026 13:00", "Livro Java", "Perda", "1", "Loja B", "Baixa", "ana", "Concluída", "👁️"}
        };
        
        for (Object[] dados : dadosExemplo) {
            modelTabela.addRow(dados);
            
            // Adicionar à lista de movimentações
            MovimentacaoEstoque movimentacao = new MovimentacaoEstoque();
            movimentacao.setCodigo((String) dados[0]);
            movimentacao.setDataHora((String) dados[1]);
            movimentacao.setProduto((String) dados[2]);
            movimentacao.setTipo((String) dados[3]);
            movimentacao.setQuantidade((String) dados[4]);
            movimentacao.setOrigem((String) dados[5]);
            movimentacao.setDestino((String) dados[6]);
            movimentacao.setResponsavel((String) dados[7]);
            movimentacao.setStatus((String) dados[8]);
            movimentacoesEncontradas.add(movimentacao);
        }
    }
    
    /**
     * Busca movimentações
     */
    private void buscarMovimentacoes() {
        String termo = txtBusca.getText().trim();
        if (termo.isEmpty()) {
            JOptionPane.showMessageDialog(workArea, "Digite um termo para buscar!", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        // TODO: Implementar lógica de busca no banco de dados
        JOptionPane.showMessageDialog(workArea, 
            "Busca realizada para: " + termo + "\n" +
            "Movimentações encontradas: " + movimentacoesEncontradas.size(), 
            "Resultado", JOptionPane.INFORMATION_MESSAGE);
        
        SystemLogger.ui("Busca realizada para: " + termo);
    }
    
    /**
     * Filtra movimentações
     */
    private void filtrarMovimentacoes() {
        try {
            // TODO: Implementar lógica de filtro no banco de dados
            JOptionPane.showMessageDialog(workArea, 
                "Filtro aplicado com sucesso!\n" +
                "Movimentações encontradas: " + movimentacoesEncontradas.size(), 
                "Resultado", JOptionPane.INFORMATION_MESSAGE);
            
            SystemLogger.ui("Filtro aplicado - " + movimentacoesEncontradas.size() + " movimentações");
            
        } catch (Exception e) {
            SystemLogger.error("Erro ao filtrar movimentações", e);
            JOptionPane.showMessageDialog(workArea, "Erro ao filtrar movimentações: " + e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    /**
     * Limpa os campos do formulário
     */
    private void limparCampos() {
        txtBusca.setText("");
        txtProduto.setText("");
        txtCodigo.setText("");
        comboTipoMovimentacao.setSelectedIndex(0);
        comboCategoria.setSelectedIndex(0);
        txtDataInicio.setText("");
        txtDataFim.setText("");
        comboOrigem.setSelectedIndex(0);
        comboDestino.setSelectedIndex(0);
    }
    
    /**
     * Ver detalhes da movimentação
     */
    private void verDetalhes() {
        int selectedRow = tabelaMovimentacoes.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(workArea, "Selecione uma movimentação para ver detalhes!", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        MovimentacaoEstoque movimentacao = movimentacoesEncontradas.get(selectedRow);
        
        JOptionPane.showMessageDialog(workArea, 
            "Detalhes da Movimentação:\n\n" +
            "Código: " + movimentacao.getCodigo() + "\n" +
            "Data/Hora: " + movimentacao.getDataHora() + "\n" +
            "Produto: " + movimentacao.getProduto() + "\n" +
            "Tipo: " + movimentacao.getTipo() + "\n" +
            "Quantidade: " + movimentacao.getQuantidade() + "\n" +
            "Origem: " + movimentacao.getOrigem() + "\n" +
            "Destino: " + movimentacao.getDestino() + "\n" +
            "Responsável: " + movimentacao.getResponsavel() + "\n" +
            "Status: " + movimentacao.getStatus(), 
            "Detalhes da Movimentação", JOptionPane.INFORMATION_MESSAGE);
        
        SystemLogger.ui("Detalhes visualizados: " + movimentacao.getCodigo());
    }
    
    /**
     * Gera relatório
     */
    private void gerarRelatorio() {
        JOptionPane.showMessageDialog(workArea, 
            "Gerando relatório de " + movimentacoesEncontradas.size() + " movimentações...\n(Implementar geração de relatório PDF/Excel)", 
            "Gerar Relatório", JOptionPane.INFORMATION_MESSAGE);
        
        SystemLogger.ui("Gerando relatório de " + movimentacoesEncontradas.size() + " movimentações");
    }
    
    /**
     * Exporta dados da tabela
     */
    private void exportarDados() {
        JOptionPane.showMessageDialog(workArea, 
            "Exportando " + movimentacoesEncontradas.size() + " movimentações...\n(Implementar exportação para CSV/Excel)", 
            "Exportar Dados", JOptionPane.INFORMATION_MESSAGE);
        
        SystemLogger.ui("Exportando " + movimentacoesEncontradas.size() + " movimentações");
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
     * Classe interna para representar uma movimentação de estoque
     */
    private static class MovimentacaoEstoque {
        private String codigo;
        private String dataHora;
        private String produto;
        private String tipo;
        private String quantidade;
        private String origem;
        private String destino;
        private String responsavel;
        private String status;
        
        // Getters e Setters
        public String getCodigo() { return codigo; }
        public void setCodigo(String codigo) { this.codigo = codigo; }
        
        public String getDataHora() { return dataHora; }
        public void setDataHora(String dataHora) { this.dataHora = dataHora; }
        
        public String getProduto() { return produto; }
        public void setProduto(String produto) { this.produto = produto; }
        
        public String getTipo() { return tipo; }
        public void setTipo(String tipo) { this.tipo = tipo; }
        
        public String getQuantidade() { return quantidade; }
        public void setQuantidade(String quantidade) { this.quantidade = quantidade; }
        
        public String getOrigem() { return origem; }
        public void setOrigem(String origem) { this.origem = origem; }
        
        public String getDestino() { return destino; }
        public void setDestino(String destino) { this.destino = destino; }
        
        public String getResponsavel() { return responsavel; }
        public void setResponsavel(String responsavel) { this.responsavel = responsavel; }
        
        public String getStatus() { return status; }
        public void setStatus(String status) { this.status = status; }
    }
}
