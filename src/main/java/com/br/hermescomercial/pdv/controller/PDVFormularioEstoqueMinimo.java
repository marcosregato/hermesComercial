package com.br.hermescomercial.pdv.controller;

import com.br.hermescomercial.util.SystemLogger;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Classe especializada para o formulário de Estoque Mínimo
 * Estrutura: Header → Busca → Formulário → Tabela
 */
public class PDVFormularioEstoqueMinimo {
    
    private JPanel workArea;
    private String usuarioAtual;
    private String nomeUsuario;
    
    // Componentes do formulário
    private JTextField txtBusca;
    private JTextField txtCodigo;
    private JTextField txtProduto;
    private JComboBox<String> comboCategoria;
    private JTextField txtEstoqueMinimo;
    private JTextField txtEstoqueAtual;
    private JTextField txtPontoReposicao;
    private JComboBox<String> comboStatus;
    
    // Tabela de estoque mínimo
    private JTable tabelaEstoqueMinimo;
    private DefaultTableModel modelTabela;
    private List<ItemEstoqueMinimo> itensEncontrados;
    
    // Cores
    private static final Color WHITE = Color.WHITE;
    private static final Color ACCENT_COLOR = new Color(52, 152, 219);
    private static final Color SUCCESS_COLOR = new Color(39, 174, 96);
    private static final Color DANGER_COLOR = new Color(231, 76, 60);
    private static final Color WARNING_COLOR = new Color(241, 196, 15);
    private static final Color GRAY = new Color(149, 165, 166);
    
    public PDVFormularioEstoqueMinimo(JPanel workArea, String usuarioAtual, String nomeUsuario) {
        this.workArea = workArea;
        this.usuarioAtual = usuarioAtual;
        this.nomeUsuario = nomeUsuario;
        this.itensEncontrados = new ArrayList<>();
    }
    
    /**
     * Cria o formulário completo de Estoque Mínimo
     */
    public JPanel criarFormularioEstoqueMinimo() {
        try {
            SystemLogger.ui("=== CRIANDO FORMULÁRIO ESTOQUE MÍNIMO ===");
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
            
            // Tabela de estoque mínimo
            JPanel tabelaPanel = criarPainelTabela();
            painelCentral.add(tabelaPanel, BorderLayout.SOUTH);
            
            painelPrincipal.add(painelCentral, BorderLayout.CENTER);
            
            SystemLogger.ui("Formulário Estoque Mínimo criado com sucesso");
            return painelPrincipal;
            
        } catch (Exception e) {
            SystemLogger.error("Erro ao criar formulário Estoque Mínimo", e);
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
        JLabel titleLabel = new JLabel("📦 Estoque Mínimo");
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
        txtBusca.setToolTipText("Digite código, nome do produto ou categoria");
        
        JButton btnBuscar = new JButton("🔍 Buscar");
        btnBuscar.setBackground(ACCENT_COLOR);
        btnBuscar.setForeground(WHITE);
        btnBuscar.setFocusPainted(false);
        btnBuscar.setBorderPainted(false);
        btnBuscar.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btnBuscar.addActionListener(e -> buscarEstoqueMinimo());
        
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
        
        // Seção Configuração de Estoque Mínimo
        JLabel configSectionLabel = new JLabel("⚙️ Configuração de Estoque Mínimo");
        configSectionLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        configSectionLabel.setForeground(ACCENT_COLOR);
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 4;
        formularioPanel.add(configSectionLabel, gbc);
        
        // Código
        gbc.gridy = 1; gbc.gridwidth = 1;
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
        txtCodigo.setEditable(false);
        formularioPanel.add(txtCodigo, gbc);
        
        // Produto
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
        
        // Categoria
        gbc.gridy = 2; gbc.gridx = 0;
        JLabel lblCategoria = new JLabel("Categoria:");
        lblCategoria.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        formularioPanel.add(lblCategoria, gbc);
        
        gbc.gridx = 1; gbc.weightx = 1.0;
        comboCategoria = new JComboBox<>(new String[]{"Todas", "Informática", "Eletrônicos", "Móveis", "Livros", "Vestuário"});
        comboCategoria.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        comboCategoria.setBorder(BorderFactory.createLineBorder(GRAY));
        formularioPanel.add(comboCategoria, gbc);
        
        // Status
        gbc.gridx = 2; gbc.weightx = 0.0;
        JLabel lblStatus = new JLabel("Status:");
        lblStatus.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        formularioPanel.add(lblStatus, gbc);
        
        gbc.gridx = 3; gbc.weightx = 1.0;
        comboStatus = new JComboBox<>(new String[]{"Todos", "Abaixo do Mínimo", "No Ponto de Reposição", "Acima do Mínimo"});
        comboStatus.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        comboStatus.setBorder(BorderFactory.createLineBorder(GRAY));
        formularioPanel.add(comboStatus, gbc);
        
        // Seção Controle de Estoque
        gbc.gridy = 3; gbc.gridx = 0; gbc.gridwidth = 4; gbc.weightx = 0.0;
        JLabel controleSectionLabel = new JLabel("📊 Controle de Estoque");
        controleSectionLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        controleSectionLabel.setForeground(ACCENT_COLOR);
        formularioPanel.add(controleSectionLabel, gbc);
        
        // Estoque Mínimo
        gbc.gridy = 4; gbc.gridwidth = 1;
        JLabel lblEstoqueMinimo = new JLabel("Estoque Mínimo:");
        lblEstoqueMinimo.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        formularioPanel.add(lblEstoqueMinimo, gbc);
        
        gbc.gridx = 1; gbc.weightx = 1.0;
        txtEstoqueMinimo = new JTextField();
        txtEstoqueMinimo.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        txtEstoqueMinimo.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(GRAY),
            BorderFactory.createEmptyBorder(5, 8, 5, 8)
        ));
        formularioPanel.add(txtEstoqueMinimo, gbc);
        
        // Estoque Atual
        gbc.gridx = 2; gbc.weightx = 0.0;
        JLabel lblEstoqueAtual = new JLabel("Estoque Atual:");
        lblEstoqueAtual.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        formularioPanel.add(lblEstoqueAtual, gbc);
        
        gbc.gridx = 3; gbc.weightx = 1.0;
        txtEstoqueAtual = new JTextField();
        txtEstoqueAtual.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        txtEstoqueAtual.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(GRAY),
            BorderFactory.createEmptyBorder(5, 8, 5, 8)
        ));
        txtEstoqueAtual.setEditable(false);
        formularioPanel.add(txtEstoqueAtual, gbc);
        
        // Ponto de Reposição
        gbc.gridy = 5; gbc.gridx = 0;
        JLabel lblPontoReposicao = new JLabel("Ponto de Reposição:");
        lblPontoReposicao.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        formularioPanel.add(lblPontoReposicao, gbc);
        
        gbc.gridx = 1; gbc.gridwidth = 3; gbc.weightx = 1.0;
        txtPontoReposicao = new JTextField();
        txtPontoReposicao.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        txtPontoReposicao.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(GRAY),
            BorderFactory.createEmptyBorder(5, 8, 5, 8)
        ));
        txtPontoReposicao.setToolTipText("Quantidade para acionar alerta de reposição");
        formularioPanel.add(txtPontoReposicao, gbc);
        
        // Botões de ação
        gbc.gridy = 6; gbc.gridx = 0; gbc.gridwidth = 4; gbc.weightx = 0.0;
        JPanel botoesPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        botoesPanel.setBackground(WHITE);
        
        JButton btnSalvar = new JButton("💾 Salvar Configuração");
        btnSalvar.setBackground(SUCCESS_COLOR);
        btnSalvar.setForeground(WHITE);
        btnSalvar.setFocusPainted(false);
        btnSalvar.setBorderPainted(false);
        btnSalvar.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btnSalvar.addActionListener(e -> salvarConfiguracao());
        
        JButton btnLimpar = new JButton("🧹 Limpar");
        btnLimpar.setBackground(GRAY);
        btnLimpar.setForeground(WHITE);
        btnLimpar.setFocusPainted(false);
        btnLimpar.setBorderPainted(false);
        btnLimpar.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btnLimpar.addActionListener(e -> limparCampos());
        
        JButton btnGerarAlertas = new JButton("🚨 Gerar Alertas");
        btnGerarAlertas.setBackground(WARNING_COLOR);
        btnGerarAlertas.setForeground(WHITE);
        btnGerarAlertas.setFocusPainted(false);
        btnGerarAlertas.setBorderPainted(false);
        btnGerarAlertas.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btnGerarAlertas.addActionListener(e -> gerarAlertas());
        
        botoesPanel.add(btnSalvar);
        botoesPanel.add(Box.createHorizontalStrut(10));
        botoesPanel.add(btnLimpar);
        botoesPanel.add(Box.createHorizontalStrut(10));
        botoesPanel.add(btnGerarAlertas);
        
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
        
        JLabel tabelaLabel = new JLabel("📋 Itens com Estoque Mínimo");
        tabelaLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        tabelaLabel.setForeground(ACCENT_COLOR);
        
        // Modelo da tabela
        String[] colunas = {"Código", "Produto", "Categoria", "Estoque Mínimo", "Estoque Atual", "Status", "Ponto Reposição", "Última Verificação", "Ações"};
        modelTabela = new DefaultTableModel(colunas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 8; // Apenas a coluna de ações é editável
            }
        };
        
        tabelaEstoqueMinimo = new JTable(modelTabela);
        tabelaEstoqueMinimo.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        tabelaEstoqueMinimo.setRowHeight(25);
        tabelaEstoqueMinimo.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 12));
        tabelaEstoqueMinimo.getTableHeader().setBackground(ACCENT_COLOR);
        tabelaEstoqueMinimo.getTableHeader().setForeground(WHITE);
        
        // Scroll pane
        JScrollPane scrollPane = new JScrollPane(tabelaEstoqueMinimo);
        scrollPane.setPreferredSize(new Dimension(0, 250));
        scrollPane.setBorder(BorderFactory.createLineBorder(GRAY));
        
        // Painel de botões da tabela
        JPanel botoesTabelaPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        botoesTabelaPanel.setBackground(WHITE);
        
        JButton btnEditar = new JButton("✏️ Editar");
        btnEditar.setBackground(ACCENT_COLOR);
        btnEditar.setForeground(WHITE);
        btnEditar.setFocusPainted(false);
        btnEditar.setBorderPainted(false);
        btnEditar.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btnEditar.addActionListener(e -> editarItem());
        
        JButton btnRelatorio = new JButton("📊 Relatório");
        btnRelatorio.setBackground(SUCCESS_COLOR);
        btnRelatorio.setForeground(WHITE);
        btnRelatorio.setFocusPainted(false);
        btnRelatorio.setBorderPainted(false);
        btnRelatorio.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btnRelatorio.addActionListener(e -> gerarRelatorio());
        
        botoesTabelaPanel.add(btnEditar);
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
        itensEncontrados.clear();
        
        // Dados de exemplo
        Object[][] dadosExemplo = {
            {"PRD-001", "Notebook Dell", "Informática", "10", "3", "Abaixo do Mínimo", "8", "10/05/2026", "✏️"},
            {"PRD-002", "Mouse Wireless", "Informática", "15", "15", "No Ponto de Reposição", "12", "09/05/2026", "✏️"},
            {"PRD-003", "Monitor 24\"", "Eletrônicos", "5", "0", "Abaixo do Mínimo", "3", "08/05/2026", "✏️"},
            {"PRD-004", "Cadeira Executiva", "Móveis", "20", "25", "Acima do Mínimo", "15", "07/05/2026", "✏️"},
            {"PRD-005", "Livro Java", "Livros", "8", "2", "Abaixo do Mínimo", "5", "06/05/2026", "✏️"}
        };
        
        for (Object[] dados : dadosExemplo) {
            modelTabela.addRow(dados);
            
            // Adicionar à lista de itens
            ItemEstoqueMinimo item = new ItemEstoqueMinimo();
            item.setCodigo((String) dados[0]);
            item.setProduto((String) dados[1]);
            item.setCategoria((String) dados[2]);
            item.setEstoqueMinimo((String) dados[3]);
            item.setEstoqueAtual((String) dados[4]);
            item.setStatus((String) dados[5]);
            item.setPontoReposicao((String) dados[6]);
            item.setUltimaVerificacao((String) dados[7]);
            itensEncontrados.add(item);
        }
    }
    
    /**
     * Busca estoque mínimo
     */
    private void buscarEstoqueMinimo() {
        String termo = txtBusca.getText().trim();
        if (termo.isEmpty()) {
            JOptionPane.showMessageDialog(workArea, "Digite um termo para buscar!", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        JOptionPane.showMessageDialog(workArea, 
            "Busca realizada para: " + termo + "\n" +
            "Itens encontrados: " + itensEncontrados.size(), 
            "Resultado", JOptionPane.INFORMATION_MESSAGE);
        
        SystemLogger.ui("Busca realizada para: " + termo);
    }
    
    /**
     * Salva configuração
     */
    private void salvarConfiguracao() {
        String produto = txtProduto.getText().trim();
        String estoqueMinimo = txtEstoqueMinimo.getText().trim();
        String pontoReposicao = txtPontoReposicao.getText().trim();
        
        if (produto.isEmpty()) {
            JOptionPane.showMessageDialog(workArea, "Digite o nome do produto!", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        if (estoqueMinimo.isEmpty()) {
            JOptionPane.showMessageDialog(workArea, "Digite o estoque mínimo!", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        JOptionPane.showMessageDialog(workArea, 
            "Configuração salva com sucesso!\n" +
            "Produto: " + produto + "\n" +
            "Estoque Mínimo: " + estoqueMinimo + "\n" +
            "Ponto de Reposição: " + pontoReposicao, 
            "Sucesso", JOptionPane.INFORMATION_MESSAGE);
        
        SystemLogger.ui("Configuração salva: " + produto);
        limparCampos();
        adicionarDadosExemplo(); // Atualizar tabela
    }
    
    /**
     * Limpa os campos do formulário
     */
    private void limparCampos() {
        txtBusca.setText("");
        txtCodigo.setText("");
        txtProduto.setText("");
        comboCategoria.setSelectedIndex(0);
        txtEstoqueMinimo.setText("");
        txtEstoqueAtual.setText("");
        txtPontoReposicao.setText("");
        comboStatus.setSelectedIndex(0);
    }
    
    /**
     * Edita item selecionado
     */
    private void editarItem() {
        int selectedRow = tabelaEstoqueMinimo.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(workArea, "Selecione um item para editar!", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        ItemEstoqueMinimo item = itensEncontrados.get(selectedRow);
        
        // Preencher formulário com dados do item
        txtCodigo.setText(item.getCodigo());
        txtProduto.setText(item.getProduto());
        comboCategoria.setSelectedItem(item.getCategoria());
        txtEstoqueMinimo.setText(item.getEstoqueMinimo());
        txtEstoqueAtual.setText(item.getEstoqueAtual());
        txtPontoReposicao.setText(item.getPontoReposicao());
        comboStatus.setSelectedItem(item.getStatus());
        
        JOptionPane.showMessageDialog(workArea, 
            "Item carregado para edição: " + item.getProduto(), 
            "Editar Item", JOptionPane.INFORMATION_MESSAGE);
        
        SystemLogger.ui("Item carregado para edição: " + item.getProduto());
    }
    
    /**
     * Gera alertas
     */
    private void gerarAlertas() {
        int itensAbaixoMinimo = 0;
        for (ItemEstoqueMinimo item : itensEncontrados) {
            if ("Abaixo do Mínimo".equals(item.getStatus())) {
                itensAbaixoMinimo++;
            }
        }
        
        JOptionPane.showMessageDialog(workArea, 
            "🚨 ALERTAS GERADOS!\n\n" +
            "Itens abaixo do mínimo: " + itensAbaixoMinimo + "\n" +
            "Itens no ponto de reposição: " + itensEncontrados.stream().filter(i -> "No Ponto de Reposição".equals(i.getStatus())).count() + "\n" +
            "Total de itens monitorados: " + itensEncontrados.size(), 
            "Alertas de Estoque", JOptionPane.WARNING_MESSAGE);
        
        SystemLogger.ui("Alertas gerados - " + itensAbaixoMinimo + " itens abaixo do mínimo");
    }
    
    /**
     * Gera relatório
     */
    private void gerarRelatorio() {
        JOptionPane.showMessageDialog(workArea, 
            "Gerando relatório de " + itensEncontrados.size() + " itens...\n(Implementar geração de relatório PDF/Excel)", 
            "Gerar Relatório", JOptionPane.INFORMATION_MESSAGE);
        
        SystemLogger.ui("Gerando relatório de " + itensEncontrados.size() + " itens");
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
     * Classe interna para representar um item de estoque mínimo
     */
    private static class ItemEstoqueMinimo {
        private String codigo;
        private String produto;
        private String categoria;
        private String estoqueMinimo;
        private String estoqueAtual;
        private String status;
        private String pontoReposicao;
        private String ultimaVerificacao;
        
        // Getters e Setters
        public String getCodigo() { return codigo; }
        public void setCodigo(String codigo) { this.codigo = codigo; }
        
        public String getProduto() { return produto; }
        public void setProduto(String produto) { this.produto = produto; }
        
        public String getCategoria() { return categoria; }
        public void setCategoria(String categoria) { this.categoria = categoria; }
        
        public String getEstoqueMinimo() { return estoqueMinimo; }
        public void setEstoqueMinimo(String estoqueMinimo) { this.estoqueMinimo = estoqueMinimo; }
        
        public String getEstoqueAtual() { return estoqueAtual; }
        public void setEstoqueAtual(String estoqueAtual) { this.estoqueAtual = estoqueAtual; }
        
        public String getStatus() { return status; }
        public void setStatus(String status) { this.status = status; }
        
        public String getPontoReposicao() { return pontoReposicao; }
        public void setPontoReposicao(String pontoReposicao) { this.pontoReposicao = pontoReposicao; }
        
        @SuppressWarnings("unused")
        public String getUltimaVerificacao() { return ultimaVerificacao; }
        public void setUltimaVerificacao(String ultimaVerificacao) { this.ultimaVerificacao = ultimaVerificacao; }
    }
}
