package com.br.hermescomercial.pdv.controller;


import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.math.BigDecimal;


/**
 * Controller simplificado para a tela de Nova Venda do PDV
 * Versão alternativa sem LayoutPadrao para isolar o problema "0 >= 0"
 */
public class PDVVendaSwingControllerSimple {
    
    private JFrame frame;
    private JTable produtosTable;
    private DefaultTableModel tableModel;
    private JTextField txtCodigo;
    private JTextField txtQuantidade;
    private JTextField txtDescricao;
    
    public PDVVendaSwingControllerSimple(String usuario) {
        initializeUI();
    }
    
    private void initializeUI() {
        frame = new JFrame("PDV - Nova Venda v2.8.3 - Simples");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setSize(900, 650);
        frame.setLocationRelativeTo(null);
        
        // Configurar fundo simples
        frame.getContentPane().setBackground(Color.WHITE);
        
        frame.add(createMainPanel());
    }
    
    private JPanel createMainPanel() {
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(Color.WHITE);
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        // Header simples
        mainPanel.add(createHeaderPanel(), BorderLayout.NORTH);
        
        // Painel central
        JPanel centerPanel = new JPanel(new BorderLayout());
        centerPanel.setBackground(Color.WHITE);
        centerPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        
        // Tabela de produtos
        JPanel produtosPanel = createProdutosPanel();
        centerPanel.add(produtosPanel, BorderLayout.CENTER);
        
        // Painel de entrada
        JPanel inputPanel = createInputPanel();
        centerPanel.add(inputPanel, BorderLayout.SOUTH);
        
        mainPanel.add(centerPanel, BorderLayout.CENTER);
        
        return mainPanel;
    }
    
    private JPanel createHeaderPanel() {
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(new Color(41, 128, 185));
        headerPanel.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));
        headerPanel.setPreferredSize(new Dimension(800, 60));
        
        JLabel lblTitulo = new JLabel("💳 Nova Venda - PDV");
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 18));
        lblTitulo.setForeground(Color.WHITE);
        
        headerPanel.add(lblTitulo, BorderLayout.WEST);
        
        return headerPanel;
    }
    
    private JPanel createProdutosPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createTitledBorder("📦 Itens da Venda"));
        panel.setBackground(Color.WHITE);
        
        // Tabela de produtos
        String[] columns = {"Código", "Descrição", "Qtd", "Unitário", "Total"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        produtosTable = new JTable(tableModel);
        produtosTable.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        produtosTable.setRowHeight(25);
        produtosTable.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 12));
        
        JScrollPane scrollPane = new JScrollPane(produtosTable);
        scrollPane.setPreferredSize(new Dimension(600, 250));
        
        panel.add(scrollPane, BorderLayout.CENTER);
        
        return panel;
    }
    
    private JPanel createInputPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createTitledBorder("➕ Adicionar Produto"));
        panel.setBackground(Color.WHITE);
        
        // Campos de entrada
        JPanel inputFieldsPanel = new JPanel(new GridBagLayout());
        inputFieldsPanel.setBackground(Color.WHITE);
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
        // Código
        gbc.gridx = 0; gbc.gridy = 0;
        inputFieldsPanel.add(new JLabel("Código:"), gbc);
        
        gbc.gridx = 1;
        txtCodigo = new JTextField(15);
        inputFieldsPanel.add(txtCodigo, gbc);
        
        // Quantidade
        gbc.gridx = 2;
        inputFieldsPanel.add(new JLabel("Quantidade:"), gbc);
        
        gbc.gridx = 3;
        txtQuantidade = new JTextField(10);
        txtQuantidade.setText("1");
        inputFieldsPanel.add(txtQuantidade, gbc);
        
        // Descrição
        gbc.gridx = 0; gbc.gridy = 1;
        inputFieldsPanel.add(new JLabel("Descrição:"), gbc);
        
        gbc.gridx = 1; gbc.gridwidth = 3;
        txtDescricao = new JTextField(40);
        txtDescricao.setEditable(false);
        txtDescricao.setBackground(Color.LIGHT_GRAY);
        inputFieldsPanel.add(txtDescricao, gbc);
        
        panel.add(inputFieldsPanel, BorderLayout.CENTER);
        
        // Botões
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.setBackground(Color.WHITE);
        
        JButton btnAdicionar = new JButton("Adicionar");
        btnAdicionar.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btnAdicionar.setBackground(new Color(46, 204, 113));
        btnAdicionar.setForeground(Color.WHITE);
        btnAdicionar.setFocusPainted(false);
        
        JButton btnFinalizar = new JButton("Finalizar Venda");
        btnFinalizar.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btnFinalizar.setBackground(new Color(39, 174, 96));
        btnFinalizar.setForeground(Color.WHITE);
        btnFinalizar.setFocusPainted(false);
        
        buttonPanel.add(btnAdicionar);
        buttonPanel.add(btnFinalizar);
        
        panel.add(buttonPanel, BorderLayout.SOUTH);
        
        return panel;
    }
    
    public void show() {
        frame.setVisible(true);
    }
    
    public void dispose() {
        if (frame != null) {
            frame.dispose();
        }
    }
    
    // Classe interna para itens da venda
    public static class ItemVenda {
        private String codigo;
        private String descricao;
        private int quantidade;
        private BigDecimal valorUnitario;
        private BigDecimal valorTotal;
        
        public ItemVenda(String codigo, String descricao, int quantidade, BigDecimal valorUnitario) {
            this.codigo = codigo;
            this.descricao = descricao;
            this.quantidade = quantidade;
            this.valorUnitario = valorUnitario;
            this.valorTotal = valorUnitario.multiply(new BigDecimal(quantidade));
        }
        
        // Getters
        public String getCodigo() { return codigo; }
        public String getDescricao() { return descricao; }
        public int getQuantidade() { return quantidade; }
        public BigDecimal getValorUnitario() { return valorUnitario; }
        public BigDecimal getValorTotal() { return valorTotal; }
    }
}
