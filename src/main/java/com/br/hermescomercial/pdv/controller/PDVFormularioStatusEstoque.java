package com.br.hermescomercial.pdv.controller;

import com.br.hermescomercial.util.SystemLogger;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Classe especializada para o formulário de Status do Estoque
 * Estrutura: Header → Busca → Formulário → Tabela
 */
public class PDVFormularioStatusEstoque {
    
    private JPanel workArea;
    private String usuarioAtual;
    private String nomeUsuario;
    
    // Componentes do formulário
    private JTextField txtBusca;
    private JTextField txtProduto;
    private JTextField txtCodigo;
    private JComboBox<String> comboCategoria;
    private JComboBox<String> comboStatus;
    private JTextField txtQuantidadeAtual;
    private JTextField txtQuantidadeMinima;
    private JTextField txtLocalizacao;
    private JTextArea txtObservacoes;
    
    // Tabela de estoque
    private JTable tabelaEstoque;
    private DefaultTableModel modelTabela;
    private List<ItemEstoque> itensEncontrados;
    
    // Cores
    private static final Color WHITE = Color.WHITE;
    private static final Color ACCENT_COLOR = new Color(52, 152, 219);
    private static final Color SUCCESS_COLOR = new Color(39, 174, 96);
    private static final Color DANGER_COLOR = new Color(231, 76, 60);
    private static final Color WARNING_COLOR = new Color(241, 196, 15);
    private static final Color GRAY = new Color(149, 165, 166);
    
    public PDVFormularioStatusEstoque(JPanel workArea, String usuarioAtual, String nomeUsuario) {
        this.workArea = workArea;
        this.usuarioAtual = usuarioAtual;
        this.nomeUsuario = nomeUsuario;
        this.itensEncontrados = new ArrayList<>();
    }
    
    /**
     * Cria o formulário completo de Status do Estoque
     */
    public JPanel criarFormularioStatusEstoque() {
        try {
            SystemLogger.ui("=== CRIANDO FORMULÁRIO STATUS ESTOQUE ===");
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
            
            // Tabela de estoque
            JPanel tabelaPanel = criarPainelTabela();
            painelCentral.add(tabelaPanel, BorderLayout.SOUTH);
            
            painelPrincipal.add(painelCentral, BorderLayout.CENTER);
            
            SystemLogger.ui("Formulário Status Estoque criado com sucesso");
            return painelPrincipal;
            
        } catch (Exception e) {
            SystemLogger.error("Erro ao criar formulário Status Estoque", e);
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
        JLabel titleLabel = new JLabel("📊 Status do Estoque");
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
        btnBuscar.addActionListener(e -> buscarEstoque());
        
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
        
        // Seção Dados do Produto
        JLabel dadosSectionLabel = new JLabel("📦 Dados do Produto");
        dadosSectionLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        dadosSectionLabel.setForeground(ACCENT_COLOR);
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 4;
        formularioPanel.add(dadosSectionLabel, gbc);
        
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
        comboStatus = new JComboBox<>(new String[]{"Todos", "Em Estoque", "Estoque Baixo", "Sem Estoque"});
        comboStatus.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        comboStatus.setBorder(BorderFactory.createLineBorder(GRAY));
        formularioPanel.add(comboStatus, gbc);
        
        // Seção Controle de Estoque
        gbc.gridy = 3; gbc.gridx = 0; gbc.gridwidth = 4; gbc.weightx = 0.0;
        JLabel controleSectionLabel = new JLabel("📊 Controle de Estoque");
        controleSectionLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        controleSectionLabel.setForeground(ACCENT_COLOR);
        formularioPanel.add(controleSectionLabel, gbc);
        
        // Quantidade Atual
        gbc.gridy = 4; gbc.gridwidth = 1;
        JLabel lblQuantidadeAtual = new JLabel("Qtde. Atual:");
        lblQuantidadeAtual.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        formularioPanel.add(lblQuantidadeAtual, gbc);
        
        gbc.gridx = 1; gbc.weightx = 1.0;
        txtQuantidadeAtual = new JTextField();
        txtQuantidadeAtual.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        txtQuantidadeAtual.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(GRAY),
            BorderFactory.createEmptyBorder(5, 8, 5, 8)
        ));
        txtQuantidadeAtual.setEditable(false);
        formularioPanel.add(txtQuantidadeAtual, gbc);
        
        // Quantidade Mínima
        gbc.gridx = 2; gbc.weightx = 0.0;
        JLabel lblQuantidadeMinima = new JLabel("Qtde. Mínima:");
        lblQuantidadeMinima.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        formularioPanel.add(lblQuantidadeMinima, gbc);
        
        gbc.gridx = 3; gbc.weightx = 1.0;
        txtQuantidadeMinima = new JTextField();
        txtQuantidadeMinima.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        txtQuantidadeMinima.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(GRAY),
            BorderFactory.createEmptyBorder(5, 8, 5, 8)
        ));
        formularioPanel.add(txtQuantidadeMinima, gbc);
        
        // Localização
        gbc.gridy = 5; gbc.gridx = 0;
        JLabel lblLocalizacao = new JLabel("Localização:");
        lblLocalizacao.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        formularioPanel.add(lblLocalizacao, gbc);
        
        gbc.gridx = 1; gbc.gridwidth = 3; gbc.weightx = 1.0;
        txtLocalizacao = new JTextField();
        txtLocalizacao.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        txtLocalizacao.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(GRAY),
            BorderFactory.createEmptyBorder(5, 8, 5, 8)
        ));
        formularioPanel.add(txtLocalizacao, gbc);
        
        // Botões de ação
        gbc.gridy = 6; gbc.gridx = 0; gbc.gridwidth = 4; gbc.weightx = 0.0;
        JPanel botoesPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        botoesPanel.setBackground(WHITE);
        
        JButton btnAtualizar = new JButton("🔄 Atualizar");
        btnAtualizar.setBackground(ACCENT_COLOR);
        btnAtualizar.setForeground(WHITE);
        btnAtualizar.setFocusPainted(false);
        btnAtualizar.setBorderPainted(false);
        btnAtualizar.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btnAtualizar.addActionListener(e -> atualizarEstoque());
        
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
        
        botoesPanel.add(btnAtualizar);
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
        
        JLabel tabelaLabel = new JLabel("📋 Status do Estoque");
        tabelaLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        tabelaLabel.setForeground(ACCENT_COLOR);
        
        // Modelo da tabela
        String[] colunas = {"Código", "Produto", "Categoria", "Qtde. Atual", "Qtde. Mínima", "Status", "Localização", "Última Atualização", "Ações"};
        modelTabela = new DefaultTableModel(colunas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 8; // Apenas a coluna de ações é editável
            }
        };
        
        tabelaEstoque = new JTable(modelTabela);
        tabelaEstoque.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        tabelaEstoque.setRowHeight(25);
        tabelaEstoque.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 12));
        tabelaEstoque.getTableHeader().setBackground(ACCENT_COLOR);
        tabelaEstoque.getTableHeader().setForeground(WHITE);
        
        // Scroll pane
        JScrollPane scrollPane = new JScrollPane(tabelaEstoque);
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
        itensEncontrados.clear();
        
        // Dados de exemplo
        Object[][] dadosExemplo = {
            {"PRD-001", "Notebook Dell", "Informática", "15", "5", "Em Estoque", "A1-B2", "10/05/2026", "👁️"},
            {"PRD-002", "Mouse Wireless", "Informática", "3", "10", "Estoque Baixo", "A1-C3", "09/05/2026", "👁️"},
            {"PRD-003", "Monitor 24\"", "Eletrônicos", "0", "8", "Sem Estoque", "B2-A1", "08/05/2026", "👁️"},
            {"PRD-004", "Cadeira Executiva", "Móveis", "25", "10", "Em Estoque", "C3-D4", "07/05/2026", "👁️"},
            {"PRD-005", "Livro Java", "Livros", "12", "15", "Estoque Baixo", "D1-E2", "06/05/2026", "👁️"}
        };
        
        for (Object[] dados : dadosExemplo) {
            modelTabela.addRow(dados);
            
            // Adicionar à lista de itens
            ItemEstoque item = new ItemEstoque();
            item.setCodigo((String) dados[0]);
            item.setProduto((String) dados[1]);
            item.setCategoria((String) dados[2]);
            item.setQuantidadeAtual((String) dados[3]);
            item.setQuantidadeMinima((String) dados[4]);
            item.setStatus((String) dados[5]);
            item.setLocalizacao((String) dados[6]);
            item.setUltimaAtualizacao((String) dados[7]);
            itensEncontrados.add(item);
        }
    }
    
    /**
     * Busca estoque
     */
    private void buscarEstoque() {
        String termo = txtBusca.getText().trim();
        if (termo.isEmpty()) {
            JOptionPane.showMessageDialog(workArea, "Digite um termo para buscar!", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        // TODO: Implementar lógica de busca no banco de dados
        JOptionPane.showMessageDialog(workArea, 
            "Busca realizada para: " + termo + "\n" +
            "Itens encontrados: " + itensEncontrados.size(), 
            "Resultado", JOptionPane.INFORMATION_MESSAGE);
        
        SystemLogger.ui("Busca realizada para: " + termo);
    }
    
    /**
     * Atualiza estoque
     */
    private void atualizarEstoque() {
        try {
            // TODO: Implementar lógica de atualização no banco de dados
            JOptionPane.showMessageDialog(workArea, 
                "Estoque atualizado com sucesso!\n" +
                "Itens atualizados: " + itensEncontrados.size(), 
                "Sucesso", JOptionPane.INFORMATION_MESSAGE);
            
            SystemLogger.ui("Estoque atualizado - " + itensEncontrados.size() + " itens");
            adicionarDadosExemplo(); // Atualizar tabela
            
        } catch (Exception e) {
            SystemLogger.error("Erro ao atualizar estoque", e);
            JOptionPane.showMessageDialog(workArea, "Erro ao atualizar estoque: " + e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    /**
     * Limpa os campos do formulário
     */
    private void limparCampos() {
        txtBusca.setText("");
        txtCodigo.setText("");
        txtProduto.setText("");
        comboCategoria.setSelectedIndex(0);
        comboStatus.setSelectedIndex(0);
        txtQuantidadeAtual.setText("");
        txtQuantidadeMinima.setText("");
        txtLocalizacao.setText("");
    }
    
    /**
     * Ver detalhes do item
     */
    private void verDetalhes() {
        int selectedRow = tabelaEstoque.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(workArea, "Selecione um item para ver detalhes!", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        ItemEstoque item = itensEncontrados.get(selectedRow);
        
        // Preencher formulário com dados do item
        txtCodigo.setText(item.getCodigo());
        txtProduto.setText(item.getProduto());
        comboCategoria.setSelectedItem(item.getCategoria());
        comboStatus.setSelectedItem(item.getStatus());
        txtQuantidadeAtual.setText(item.getQuantidadeAtual());
        txtQuantidadeMinima.setText(item.getQuantidadeMinima());
        txtLocalizacao.setText(item.getLocalizacao());
        
        JOptionPane.showMessageDialog(workArea, 
            "Detalhes do Item:\n\n" +
            "Código: " + item.getCodigo() + "\n" +
            "Produto: " + item.getProduto() + "\n" +
            "Categoria: " + item.getCategoria() + "\n" +
            "Status: " + item.getStatus() + "\n" +
            "Quantidade: " + item.getQuantidadeAtual() + "/" + item.getQuantidadeMinima(), 
            "Detalhes do Estoque", JOptionPane.INFORMATION_MESSAGE);
        
        SystemLogger.ui("Detalhes visualizados: " + item.getProduto());
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
     * Exporta dados da tabela
     */
    private void exportarDados() {
        JOptionPane.showMessageDialog(workArea, 
            "Exportando " + itensEncontrados.size() + " itens...\n(Implementar exportação para CSV/Excel)", 
            "Exportar Dados", JOptionPane.INFORMATION_MESSAGE);
        
        SystemLogger.ui("Exportando " + itensEncontrados.size() + " itens");
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
     * Classe interna para representar um item de estoque
     */
    private static class ItemEstoque {
        private String codigo;
        private String produto;
        private String categoria;
        private String quantidadeAtual;
        private String quantidadeMinima;
        private String status;
        private String localizacao;
        private String ultimaAtualizacao;
        
        // Getters e Setters
        public String getCodigo() { return codigo; }
        public void setCodigo(String codigo) { this.codigo = codigo; }
        
        public String getProduto() { return produto; }
        public void setProduto(String produto) { this.produto = produto; }
        
        public String getCategoria() { return categoria; }
        public void setCategoria(String categoria) { this.categoria = categoria; }
        
        public String getQuantidadeAtual() { return quantidadeAtual; }
        public void setQuantidadeAtual(String quantidadeAtual) { this.quantidadeAtual = quantidadeAtual; }
        
        public String getQuantidadeMinima() { return quantidadeMinima; }
        public void setQuantidadeMinima(String quantidadeMinima) { this.quantidadeMinima = quantidadeMinima; }
        
        public String getStatus() { return status; }
        public void setStatus(String status) { this.status = status; }
        
        public String getLocalizacao() { return localizacao; }
        public void setLocalizacao(String localizacao) { this.localizacao = localizacao; }
        
        public String getUltimaAtualizacao() { return ultimaAtualizacao; }
        public void setUltimaAtualizacao(String ultimaAtualizacao) { this.ultimaAtualizacao = ultimaAtualizacao; }
    }
}
