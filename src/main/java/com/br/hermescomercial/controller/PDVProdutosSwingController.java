package com.br.hermescomercial.controller;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * Controller de Consulta de Produtos em SWING
 * Permite buscar, consultar e gerenciar produtos
 * Versão 2.0 - 100% SWING - Sem JavaFX
 */
public class PDVProdutosSwingController {
    
    private JFrame frame;
    private JTable produtosTable;
    private DefaultTableModel tableModel;
    private JTextField txtBusca;
    private JComboBox<String> cbCategoria;
    private JComboBox<String> cbTipoBusca;
    private List<Produto> produtos;
    
    public PDVProdutosSwingController() {
        this.produtos = new ArrayList<>();
        initializeUI();
        carregarProdutosExemplo();
    }
    
    private void initializeUI() {
        frame = new JFrame("PDV - Consulta de Produtos v2.0");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setSize(900, 600);
        frame.setLocationRelativeTo(null);
        
        frame.add(createMainPanel());
    }
    
    private JPanel createMainPanel() {
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        // Header
        mainPanel.add(createHeaderPanel(), BorderLayout.NORTH);
        
        // Center
        mainPanel.add(createProdutosPanel(), BorderLayout.CENTER);
        
        // South
        mainPanel.add(createControlPanel(), BorderLayout.SOUTH);
        
        return mainPanel;
    }
    
    private JPanel createHeaderPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(5, 5, 15, 5));
        
        JLabel titleLabel = new JLabel("Consulta de Produtos", JLabel.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        
        JButton btnVoltar = new JButton("← Voltar");
        btnVoltar.addActionListener(e -> frame.dispose());
        
        panel.add(btnVoltar, BorderLayout.WEST);
        panel.add(titleLabel, BorderLayout.CENTER);
        
        return panel;
    }
    
    private JPanel createProdutosPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createTitledBorder("Produtos"));
        
        // Tabela de produtos
        String[] columns = {"Código", "Descrição", "Categoria", "Preço", "Estoque", "Status"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        produtosTable = new JTable(tableModel);
        produtosTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        produtosTable.getTableHeader().setReorderingAllowed(false);
        
        // Configurar larguras das colunas
        produtosTable.getColumnModel().getColumn(0).setPreferredWidth(80);
        produtosTable.getColumnModel().getColumn(1).setPreferredWidth(250);
        produtosTable.getColumnModel().getColumn(2).setPreferredWidth(120);
        produtosTable.getColumnModel().getColumn(3).setPreferredWidth(80);
        produtosTable.getColumnModel().getColumn(4).setPreferredWidth(80);
        produtosTable.getColumnModel().getColumn(5).setPreferredWidth(100);
        
        JScrollPane scrollPane = new JScrollPane(produtosTable);
        panel.add(scrollPane, BorderLayout.CENTER);
        
        return panel;
    }
    
    private JPanel createControlPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createTitledBorder("Filtros e Busca"));
        
        // Painel de filtros
        JPanel filterPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;
        
        // Tipo de busca
        gbc.gridx = 0; gbc.gridy = 0;
        filterPanel.add(new JLabel("Buscar por:"), gbc);
        gbc.gridx = 1; gbc.fill = GridBagConstraints.HORIZONTAL; gbc.weightx = 1.0;
        cbTipoBusca = new JComboBox<>(new String[]{"Todos", "Código", "Descrição", "Categoria"});
        filterPanel.add(cbTipoBusca, gbc);
        
        // Campo de busca
        gbc.gridx = 2; gbc.gridy = 0; gbc.fill = GridBagConstraints.NONE; gbc.weightx = 0;
        filterPanel.add(new JLabel("Termo:"), gbc);
        gbc.gridx = 3; gbc.fill = GridBagConstraints.HORIZONTAL; gbc.weightx = 1.0;
        txtBusca = new JTextField();
        filterPanel.add(txtBusca, gbc);
        
        // Categoria
        gbc.gridx = 4; gbc.gridy = 0; gbc.fill = GridBagConstraints.NONE; gbc.weightx = 0;
        filterPanel.add(new JLabel("Categoria:"), gbc);
        gbc.gridx = 5; gbc.fill = GridBagConstraints.HORIZONTAL; gbc.weightx = 1.0;
        cbCategoria = new JComboBox<>(new String[]{"Todas", "Alimentos", "Bebidas", "Limpeza", "Outros"});
        filterPanel.add(cbCategoria, gbc);
        
        // Botões
        gbc.gridx = 6; gbc.gridy = 0; gbc.gridheight = 2;
        JButton btnBuscar = new JButton("Buscar");
        btnBuscar.setBackground(new Color(40, 167, 69));
        btnBuscar.setForeground(Color.WHITE);
        btnBuscar.addActionListener(this::buscarProdutos);
        filterPanel.add(btnBuscar, gbc);
        
        gbc.gridx = 7; gbc.gridy = 0; gbc.gridheight = 1;
        JButton btnLimpar = new JButton("Limpar");
        btnLimpar.setBackground(new Color(255, 193, 7));
        btnLimpar.setForeground(Color.BLACK);
        btnLimpar.addActionListener(this::limparFiltros);
        filterPanel.add(btnLimpar, gbc);
        
        gbc.gridx = 7; gbc.gridy = 1;
        JButton btnRecarregar = new JButton("Recarregar");
        btnRecarregar.setBackground(new Color(23, 162, 184));
        btnRecarregar.setForeground(Color.WHITE);
        btnRecarregar.addActionListener(this::recarregarProdutos);
        filterPanel.add(btnRecarregar, gbc);
        
        panel.add(filterPanel, BorderLayout.CENTER);
        
        // Painel de ações
        JPanel actionPanel = new JPanel(new FlowLayout());
        
        JButton btnDetalhes = new JButton("Ver Detalhes");
        btnDetalhes.setBackground(new Color(0, 123, 255));
        btnDetalhes.setForeground(Color.WHITE);
        btnDetalhes.addActionListener(this::verDetalhes);
        
        JButton btnEditar = new JButton("Editar");
        btnEditar.setBackground(new Color(255, 193, 7));
        btnEditar.setForeground(Color.BLACK);
        btnEditar.addActionListener(this::editarProduto);
        
        JButton btnExcluir = new JButton("Excluir");
        btnExcluir.setBackground(new Color(220, 53, 69));
        btnExcluir.setForeground(Color.WHITE);
        btnExcluir.addActionListener(this::excluirProduto);
        
        JButton btnNovo = new JButton("Novo Produto");
        btnNovo.setBackground(new Color(40, 167, 69));
        btnNovo.setForeground(Color.WHITE);
        btnNovo.addActionListener(this::novoProduto);
        
        actionPanel.add(btnNovo);
        actionPanel.add(btnDetalhes);
        actionPanel.add(btnEditar);
        actionPanel.add(btnExcluir);
        
        panel.add(actionPanel, BorderLayout.SOUTH);
        
        return panel;
    }
    
    private void buscarProdutos(ActionEvent e) {
        String termo = txtBusca.getText().trim().toLowerCase();
        String tipoBusca = (String) cbTipoBusca.getSelectedItem();
        String categoria = (String) cbCategoria.getSelectedItem();
        
        tableModel.setRowCount(0);
        
        for (Produto produto : produtos) {
            boolean matches = true;
            
            // Filtrar por categoria
            if (!"Todas".equals(categoria) && !categoria.equals(produto.getCategoria())) {
                matches = false;
            }
            
            // Filtrar por termo
            if (!termo.isEmpty()) {
                switch (tipoBusca) {
                    case "Código":
                        matches = produto.getCodigo().toLowerCase().contains(termo);
                        break;
                    case "Descrição":
                        matches = produto.getDescricao().toLowerCase().contains(termo);
                        break;
                    case "Categoria":
                        matches = produto.getCategoria().toLowerCase().contains(termo);
                        break;
                    case "Todos":
                        matches = produto.getCodigo().toLowerCase().contains(termo) ||
                                produto.getDescricao().toLowerCase().contains(termo) ||
                                produto.getCategoria().toLowerCase().contains(termo);
                        break;
                }
            }
            
            if (matches) {
                Object[] row = {
                    produto.getCodigo(),
                    produto.getDescricao(),
                    produto.getCategoria(),
                    String.format("R$ %.2f", produto.getPreco()),
                    produto.getEstoque(),
                    getEstoqueStatus(produto.getEstoque())
                };
                tableModel.addRow(row);
            }
        }
        
        if (tableModel.getRowCount() == 0) {
            JOptionPane.showMessageDialog(frame, "Nenhum produto encontrado!", 
                "Busca", JOptionPane.INFORMATION_MESSAGE);
        }
    }
    
    private void limparFiltros(ActionEvent e) {
        txtBusca.setText("");
        cbTipoBusca.setSelectedIndex(0);
        cbCategoria.setSelectedIndex(0);
        recarregarProdutos(null);
    }
    
    private void recarregarProdutos(ActionEvent e) {
        tableModel.setRowCount(0);
        
        for (Produto produto : produtos) {
            Object[] row = {
                produto.getCodigo(),
                produto.getDescricao(),
                produto.getCategoria(),
                String.format("R$ %.2f", produto.getPreco()),
                produto.getEstoque(),
                getEstoqueStatus(produto.getEstoque())
            };
            tableModel.addRow(row);
        }
    }
    
    private void verDetalhes(ActionEvent e) {
        int selectedRow = produtosTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(frame, "Selecione um produto!", 
                "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        Produto produto = produtos.get(selectedRow);
        
        String detalhes = "Código: " + produto.getCodigo() + "\n" +
                         "Descrição: " + produto.getDescricao() + "\n" +
                         "Categoria: " + produto.getCategoria() + "\n" +
                         "Preço: R$ " + String.format("%.2f", produto.getPreco()) + "\n" +
                         "Estoque: " + produto.getEstoque() + "\n" +
                         "Status: " + getEstoqueStatus(produto.getEstoque());
        
        JOptionPane.showMessageDialog(frame, detalhes, 
            "Detalhes do Produto", JOptionPane.INFORMATION_MESSAGE);
    }
    
    private void editarProduto(ActionEvent e) {
        int selectedRow = produtosTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(frame, "Selecione um produto!", 
                "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        JOptionPane.showMessageDialog(frame, 
            "Funcionalidade de edição em desenvolvimento.\n" +
            "Produto selecionado: " + produtos.get(selectedRow).getDescricao(),
            "Editar Produto", JOptionPane.INFORMATION_MESSAGE);
    }
    
    private void excluirProduto(ActionEvent e) {
        int selectedRow = produtosTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(frame, "Selecione um produto!", 
                "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        Produto produto = produtos.get(selectedRow);
        
        int confirm = JOptionPane.showConfirmDialog(frame, 
            "Deseja excluir o produto:\n" + produto.getDescricao() + "?",
            "Confirmar Exclusão", JOptionPane.YES_NO_OPTION);
            
        if (confirm == JOptionPane.YES_OPTION) {
            produtos.remove(selectedRow);
            tableModel.removeRow(selectedRow);
            JOptionPane.showMessageDialog(frame, "Produto excluído com sucesso!", 
                "Sucesso", JOptionPane.INFORMATION_MESSAGE);
        }
    }
    
    private void novoProduto(ActionEvent e) {
        JOptionPane.showMessageDialog(frame, 
            "Funcionalidade de cadastro em desenvolvimento.",
            "Novo Produto", JOptionPane.INFORMATION_MESSAGE);
    }
    
    private String getEstoqueStatus(int estoque) {
        if (estoque == 0) {
            return "Sem Estoque";
        } else if (estoque < 10) {
            return "Estoque Baixo";
        } else {
            return "Disponível";
        }
    }
    
    private void carregarProdutosExemplo() {
        produtos.add(new Produto("001", "Arroz 5kg", "Alimentos", new BigDecimal("25.99"), 50));
        produtos.add(new Produto("002", "Feijão 1kg", "Alimentos", new BigDecimal("8.50"), 30));
        produtos.add(new Produto("003", "Óleo de Soja 900ml", "Alimentos", new BigDecimal("7.20"), 5));
        produtos.add(new Produto("004", "Refrigerante 2L", "Bebidas", new BigDecimal("12.00"), 20));
        produtos.add(new Produto("005", "Água Mineral 500ml", "Bebidas", new BigDecimal("2.50"), 0));
        produtos.add(new Produto("006", "Detergente 500ml", "Limpeza", new BigDecimal("4.30"), 15));
        produtos.add(new Produto("007", "Sabão em Pó 1kg", "Limpeza", new BigDecimal("18.90"), 8));
        produtos.add(new Produto("008", "Papel Higiênico 4 unidades", "Outros", new BigDecimal("15.00"), 25));
        
        recarregarProdutos(null);
    }
    
    public void show() {
        frame.setVisible(true);
    }
    
    // Classe de apoio
    private static class Produto {
        private String codigo;
        private String descricao;
        private String categoria;
        private BigDecimal preco;
        private int estoque;
        
        public Produto(String codigo, String descricao, String categoria, BigDecimal preco, int estoque) {
            this.codigo = codigo;
            this.descricao = descricao;
            this.categoria = categoria;
            this.preco = preco;
            this.estoque = estoque;
        }
        
        public String getCodigo() { return codigo; }
        public String getDescricao() { return descricao; }
        public String getCategoria() { return categoria; }
        public BigDecimal getPreco() { return preco; }
        public int getEstoque() { return estoque; }
    }
}
