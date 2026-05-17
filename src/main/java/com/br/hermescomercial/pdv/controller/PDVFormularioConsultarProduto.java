package com.br.hermescomercial.pdv.controller;

import com.br.hermescomercial.util.SystemLogger;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Classe especializada para o formulário de Consultar Produtos
 * Estrutura: Header → Busca → Formulário → Tabela
 */
public class PDVFormularioConsultarProduto {
    
    private JPanel workArea;
    private String usuarioAtual;
    private String nomeUsuario;
    
    // Componentes do formulário
    private JTextField txtBusca;
    private JTextField txtCodigo;
    private JTextField txtNome;
    private JTextField txtCategoria;
    private JTextField txtFornecedor;
    private JTextField txtPrecoMin;
    private JTextField txtPrecoMax;
    private JComboBox<String> comboStatus;
    
    // Tabela de produtos
    private JTable tabelaProdutos;
    private DefaultTableModel modelTabela;
    private List<Produto> produtosEncontrados;
    
    // Cores
    private static final Color WHITE = Color.WHITE;
    private static final Color ACCENT_COLOR = new Color(52, 152, 219);
    private static final Color SUCCESS_COLOR = new Color(39, 174, 96);
    private static final Color DANGER_COLOR = new Color(231, 76, 60);
    private static final Color GRAY = new Color(149, 165, 166);
    
    public PDVFormularioConsultarProduto(JPanel workArea, String usuarioAtual, String nomeUsuario) {
        this.workArea = workArea;
        this.usuarioAtual = usuarioAtual;
        this.nomeUsuario = nomeUsuario;
        this.produtosEncontrados = new ArrayList<>();
    }
    
    /**
     * Cria o formulário completo de Consultar Produtos
     */
    public JPanel criarFormularioConsultarProduto() {
        try {
            SystemLogger.ui("=== CRIANDO FORMULÁRIO CONSULTAR PRODUTOS ===");
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
            
            // Painel do formulário de consulta
            JPanel formularioPanel = criarPainelFormulario();
            painelCentral.add(formularioPanel, BorderLayout.CENTER);
            
            // Tabela de produtos
            JPanel tabelaPanel = criarPainelTabela();
            painelCentral.add(tabelaPanel, BorderLayout.SOUTH);
            
            painelPrincipal.add(painelCentral, BorderLayout.CENTER);
            
            SystemLogger.ui("Formulário Consultar Produtos criado com sucesso");
            return painelPrincipal;
            
        } catch (Exception e) {
            SystemLogger.error("Erro ao criar formulário Consultar Produtos", e);
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
        JLabel titleLabel = new JLabel("🔍 Consultar Produtos");
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
        txtBusca.setToolTipText("Digite código, nome ou categoria do produto");
        
        JButton btnBuscar = new JButton("🔍 Buscar");
        btnBuscar.setBackground(ACCENT_COLOR);
        btnBuscar.setForeground(WHITE);
        btnBuscar.setFocusPainted(false);
        btnBuscar.setBorderPainted(false);
        btnBuscar.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btnBuscar.addActionListener(e -> buscarProdutos());
        
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
        JLabel filtrosSectionLabel = new JLabel("🔍 Filtros de Consulta");
        filtrosSectionLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        filtrosSectionLabel.setForeground(ACCENT_COLOR);
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 4;
        formularioPanel.add(filtrosSectionLabel, gbc);
        
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
        formularioPanel.add(txtCodigo, gbc);
        
        // Nome
        gbc.gridx = 2; gbc.weightx = 0.0;
        JLabel lblNome = new JLabel("Nome:");
        lblNome.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        formularioPanel.add(lblNome, gbc);
        
        gbc.gridx = 3; gbc.weightx = 1.0;
        txtNome = new JTextField();
        txtNome.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        txtNome.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(GRAY),
            BorderFactory.createEmptyBorder(5, 8, 5, 8)
        ));
        formularioPanel.add(txtNome, gbc);
        
        // Categoria
        gbc.gridy = 2; gbc.gridx = 0;
        JLabel lblCategoria = new JLabel("Categoria:");
        lblCategoria.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        formularioPanel.add(lblCategoria, gbc);
        
        gbc.gridx = 1; gbc.weightx = 1.0;
        txtCategoria = new JTextField();
        txtCategoria.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        txtCategoria.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(GRAY),
            BorderFactory.createEmptyBorder(5, 8, 5, 8)
        ));
        formularioPanel.add(txtCategoria, gbc);
        
        // Fornecedor
        gbc.gridx = 2; gbc.weightx = 0.0;
        JLabel lblFornecedor = new JLabel("Fornecedor:");
        lblFornecedor.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        formularioPanel.add(lblFornecedor, gbc);
        
        gbc.gridx = 3; gbc.weightx = 1.0;
        txtFornecedor = new JTextField();
        txtFornecedor.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        txtFornecedor.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(GRAY),
            BorderFactory.createEmptyBorder(5, 8, 5, 8)
        ));
        formularioPanel.add(txtFornecedor, gbc);
        
        // Preço
        gbc.gridy = 3; gbc.gridx = 0;
        JLabel lblPrecoMin = new JLabel("Preço Mínimo:");
        lblPrecoMin.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        formularioPanel.add(lblPrecoMin, gbc);
        
        gbc.gridx = 1; gbc.weightx = 1.0;
        txtPrecoMin = new JTextField();
        txtPrecoMin.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        txtPrecoMin.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(GRAY),
            BorderFactory.createEmptyBorder(5, 8, 5, 8)
        ));
        formularioPanel.add(txtPrecoMin, gbc);
        
        gbc.gridx = 2; gbc.weightx = 0.0;
        JLabel lblPrecoMax = new JLabel("Preço Máximo:");
        lblPrecoMax.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        formularioPanel.add(lblPrecoMax, gbc);
        
        gbc.gridx = 3; gbc.weightx = 1.0;
        txtPrecoMax = new JTextField();
        txtPrecoMax.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        txtPrecoMax.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(GRAY),
            BorderFactory.createEmptyBorder(5, 8, 5, 8)
        ));
        formularioPanel.add(txtPrecoMax, gbc);
        
        // Status
        gbc.gridy = 4; gbc.gridx = 0;
        JLabel lblStatus = new JLabel("Status:");
        lblStatus.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        formularioPanel.add(lblStatus, gbc);
        
        gbc.gridx = 1; gbc.weightx = 1.0;
        comboStatus = new JComboBox<>(new String[]{"Todos", "Ativo", "Inativo", "Em Falta"});
        comboStatus.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        comboStatus.setBorder(BorderFactory.createLineBorder(GRAY));
        formularioPanel.add(comboStatus, gbc);
        
        // Botões de ação
        gbc.gridy = 5; gbc.gridx = 0; gbc.gridwidth = 4; gbc.weightx = 0.0;
        JPanel botoesPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        botoesPanel.setBackground(WHITE);
        
        JButton btnConsultar = new JButton("🔍 Consultar");
        btnConsultar.setBackground(ACCENT_COLOR);
        btnConsultar.setForeground(WHITE);
        btnConsultar.setFocusPainted(false);
        btnConsultar.setBorderPainted(false);
        btnConsultar.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btnConsultar.addActionListener(e -> consultarProdutos());
        
        JButton btnLimpar = new JButton("🧹 Limpar");
        btnLimpar.setBackground(GRAY);
        btnLimpar.setForeground(WHITE);
        btnLimpar.setFocusPainted(false);
        btnLimpar.setBorderPainted(false);
        btnLimpar.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btnLimpar.addActionListener(e -> limparCampos());
        
        botoesPanel.add(btnConsultar);
        botoesPanel.add(Box.createHorizontalStrut(10));
        botoesPanel.add(btnLimpar);
        
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
        
        JLabel tabelaLabel = new JLabel("📋 Resultados da Consulta");
        tabelaLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        tabelaLabel.setForeground(ACCENT_COLOR);
        
        // Modelo da tabela
        String[] colunas = {"Código", "Nome", "Categoria", "Fornecedor", "Preço", "Estoque", "Status", "Ações"};
        modelTabela = new DefaultTableModel(colunas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 7; // Apenas a coluna de ações é editável
            }
        };
        
        tabelaProdutos = new JTable(modelTabela);
        tabelaProdutos.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        tabelaProdutos.setRowHeight(25);
        tabelaProdutos.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 12));
        tabelaProdutos.getTableHeader().setBackground(ACCENT_COLOR);
        tabelaProdutos.getTableHeader().setForeground(WHITE);
        
        // Scroll pane
        JScrollPane scrollPane = new JScrollPane(tabelaProdutos);
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
        btnExportar.setBackground(SUCCESS_COLOR);
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
        produtosEncontrados.clear();
        
        // Dados de exemplo
        Object[][] dadosExemplo = {
            {"PROD-001", "Notebook Dell i7", "Informática", "Dell Brasil", "R$ 4.500,00", "15", "Ativo", "👁️"},
            {"PROD-002", "Mouse Wireless", "Acessórios", "Logitech", "R$ 89,90", "50", "Ativo", "👁️"},
            {"PROD-003", "Monitor 24\"", "Monitores", "LG", "R$ 899,00", "8", "Ativo", "👁️"},
            {"PROD-004", "Teclado Mecânico", "Acessórios", "Razer", "R$ 299,00", "0", "Em Falta", "👁️"},
            {"PROD-005", "Webcam HD", "Periféricos", "Microsoft", "R$ 159,00", "25", "Ativo", "👁️"}
        };
        
        for (Object[] dados : dadosExemplo) {
            modelTabela.addRow(dados);
            
            // Adicionar à lista de produtos
            Produto produto = new Produto();
            produto.setCodigo((String) dados[0]);
            produto.setNome((String) dados[1]);
            produto.setCategoria((String) dados[2]);
            produto.setFornecedor((String) dados[3]);
            produto.setPreco((String) dados[4]);
            produto.setEstoque((String) dados[5]);
            produto.setStatus((String) dados[6]);
            produtosEncontrados.add(produto);
        }
    }
    
    /**
     * Busca produtos
     */
    private void buscarProdutos() {
        String termo = txtBusca.getText().trim();
        if (termo.isEmpty()) {
            JOptionPane.showMessageDialog(workArea, "Digite um termo para buscar!", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        JOptionPane.showMessageDialog(workArea, 
            "Busca realizada para: " + termo + "\n" +
            "Produtos encontrados: " + produtosEncontrados.size(), 
            "Resultado", JOptionPane.INFORMATION_MESSAGE);
        
        SystemLogger.ui("Busca realizada para: " + termo);
    }
    
    /**
     * Consulta produtos com filtros
     */
    private void consultarProdutos() {
        try {
            JOptionPane.showMessageDialog(workArea, 
                "Consulta realizada com sucesso!\n" +
                "Produtos encontrados: " + produtosEncontrados.size(), 
                "Resultado", JOptionPane.INFORMATION_MESSAGE);
            
            SystemLogger.ui("Consulta realizada - " + produtosEncontrados.size() + " produtos encontrados");
            
        } catch (Exception e) {
            SystemLogger.error("Erro ao consultar produtos", e);
            JOptionPane.showMessageDialog(workArea, "Erro ao consultar produtos: " + e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    /**
     * Limpa os campos do formulário
     */
    private void limparCampos() {
        txtBusca.setText("");
        txtCodigo.setText("");
        txtNome.setText("");
        txtCategoria.setText("");
        txtFornecedor.setText("");
        txtPrecoMin.setText("");
        txtPrecoMax.setText("");
        comboStatus.setSelectedIndex(0);
    }
    
    /**
     * Ver detalhes do produto
     */
    private void verDetalhes() {
        int selectedRow = tabelaProdutos.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(workArea, "Selecione um produto para ver detalhes!", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        Produto produto = produtosEncontrados.get(selectedRow);
        JOptionPane.showMessageDialog(workArea, 
            "Detalhes do Produto:\n\n" +
            "Código: " + produto.getCodigo() + "\n" +
            "Nome: " + produto.getNome() + "\n" +
            "Categoria: " + produto.getCategoria() + "\n" +
            "Fornecedor: " + produto.getFornecedor() + "\n" +
            "Preço: " + produto.getPreco() + "\n" +
            "Estoque: " + produto.getEstoque() + "\n" +
            "Status: " + produto.getStatus(), 
            "Detalhes do Produto", JOptionPane.INFORMATION_MESSAGE);
    }
    
    /**
     * Exporta dados da tabela
     */
    private void exportarDados() {
        JOptionPane.showMessageDialog(workArea, 
            "Exportando " + produtosEncontrados.size() + " produtos...\n(Implementar exportação para CSV/Excel)", 
            "Exportar Dados", JOptionPane.INFORMATION_MESSAGE);
        
        SystemLogger.ui("Exportando " + produtosEncontrados.size() + " produtos");
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
     * Classe interna para representar um produto
     */
    private static class Produto {
        private String codigo;
        private String nome;
        private String categoria;
        private String fornecedor;
        private String preco;
        private String estoque;
        private String status;
        
        // Getters e Setters
        public String getCodigo() { return codigo; }
        public void setCodigo(String codigo) { this.codigo = codigo; }
        
        public String getNome() { return nome; }
        public void setNome(String nome) { this.nome = nome; }
        
        public String getCategoria() { return categoria; }
        public void setCategoria(String categoria) { this.categoria = categoria; }
        
        public String getFornecedor() { return fornecedor; }
        public void setFornecedor(String fornecedor) { this.fornecedor = fornecedor; }
        
        public String getPreco() { return preco; }
        public void setPreco(String preco) { this.preco = preco; }
        
        public String getEstoque() { return estoque; }
        public void setEstoque(String estoque) { this.estoque = estoque; }
        
        public String getStatus() { return status; }
        public void setStatus(String status) { this.status = status; }
    }
}
