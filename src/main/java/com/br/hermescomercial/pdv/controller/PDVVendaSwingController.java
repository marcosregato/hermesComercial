package com.br.hermescomercial.pdv.controller;

import com.br.hermescomercial.theme.ModernTheme;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * Controller de Venda em SWING
 * Versão 2.0 - 100% SWING - Sem JavaFX
 */
public class PDVVendaSwingController {
    
    private JFrame frame;
    private JTable produtosTable;
    private DefaultTableModel tableModel;
    private JTextField txtCodigo;
    private JTextField txtQuantidade;
    private JTextField txtDescricao;
    private JLabel lblTotal;
    private JLabel lblItens;
    private List<ItemVenda> itens;
    
    public PDVVendaSwingController() {
        this.itens = new ArrayList<>();
        initializeUI();
    }
    
    private void initializeUI() {
        // Aplicar tema moderno
        ModernTheme.applyModernTheme();
        
        frame = new JFrame("PDV - Nova Venda v2.1.0 - Premium");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setSize(900, 600);
        frame.setLocationRelativeTo(null);
        
        // Configurar fundo elegante
        frame.getContentPane().setBackground(ModernTheme.BACKGROUND_PRIMARY);
        
        frame.add(createMainPanel());
    }
    
    private JPanel createMainPanel() {
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        mainPanel.setBackground(new Color(248, 249, 250)); // Fundo moderno
        
        // Header melhorado
        mainPanel.add(createHeaderPanel(), BorderLayout.NORTH);
        
        // Painel central com layout otimizado
        JPanel centerPanel = new JPanel(new BorderLayout());
        centerPanel.setBorder(BorderFactory.createEmptyBorder(15, 0, 15, 0));
        centerPanel.setOpaque(false);
        
        // Painel de produtos com design moderno
        JPanel produtosPanel = createEnhancedProdutosPanel();
        centerPanel.add(produtosPanel, BorderLayout.CENTER);
        
        // Painel de entrada com design melhorado
        JPanel inputPanel = createEnhancedInputPanel();
        centerPanel.add(inputPanel, BorderLayout.SOUTH);
        
        mainPanel.add(centerPanel, BorderLayout.CENTER);
        
        // Painel lateral com resumo e ações
        JPanel sidePanel = createEnhancedSidePanel();
        mainPanel.add(sidePanel, BorderLayout.EAST);
        
        return mainPanel;
    }
    
    private JPanel createHeaderPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));
        panel.setBackground(new Color(41, 128, 185));
        panel.setPreferredSize(new Dimension(0, 90));
        
        // Painel esquerdo com botão voltar
        JPanel leftPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        leftPanel.setOpaque(false);
        
        JButton btnVoltar = new JButton("← Voltar");
        btnVoltar.setBackground(new Color(255, 255, 255));
        btnVoltar.setForeground(new Color(41, 128, 185));
        btnVoltar.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btnVoltar.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        btnVoltar.setFocusPainted(false);
        btnVoltar.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnVoltar.addActionListener(e -> frame.dispose());
        
        leftPanel.add(btnVoltar);
        
        // Painel central com título e subtítulo
        JPanel centerPanel = new JPanel(new BorderLayout());
        centerPanel.setOpaque(false);
        
        JLabel titleLabel = new JLabel("🛒 Nova Venda", JLabel.CENTER);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        titleLabel.setForeground(Color.WHITE);
        
        JLabel subtitleLabel = new JLabel("Sistema de Ponto de Venda", JLabel.CENTER);
        subtitleLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        subtitleLabel.setForeground(new Color(200, 220, 240));
        
        centerPanel.add(titleLabel, BorderLayout.NORTH);
        centerPanel.add(subtitleLabel, BorderLayout.CENTER);
        
        // Painel direito com data e hora
        JPanel rightPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        rightPanel.setOpaque(false);
        
        JLabel lblDataHora = new JLabel("🕒 " + java.time.LocalDateTime.now().format(
            java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")));
        lblDataHora.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lblDataHora.setForeground(Color.WHITE);
        
        rightPanel.add(lblDataHora);
        
        panel.add(leftPanel, BorderLayout.WEST);
        panel.add(centerPanel, BorderLayout.CENTER);
        panel.add(rightPanel, BorderLayout.EAST);
        
        return panel;
    }
    
    private JPanel createEnhancedProdutosPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createTitledBorder("📦 Itens da Venda"),
            BorderFactory.createEmptyBorder(15, 15, 15, 15)
        ));
        panel.setBackground(new Color(255, 255, 255));
        
        // Tabela de produtos com design melhorado
        String[] columns = {"Código", "Descrição", "Qtd", "Unitário", "Total"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
            
            @Override
            public Class<?> getColumnClass(int columnIndex) {
                if (columnIndex == 2) return Integer.class; // Quantidade
                if (columnIndex == 3 || columnIndex == 4) return Double.class; // Valores
                return String.class;
            }
        };
        
        produtosTable = new JTable(tableModel);
        produtosTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        produtosTable.getTableHeader().setReorderingAllowed(false);
        produtosTable.setRowHeight(28);
        produtosTable.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        produtosTable.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 12));
        produtosTable.getTableHeader().setBackground(new Color(41, 128, 185));
        produtosTable.getTableHeader().setForeground(Color.WHITE);
        produtosTable.getTableHeader().setPreferredSize(new Dimension(0, 35));
        
        // Desabilitar edição da tabela
        produtosTable.setDefaultEditor(Object.class, null);
        produtosTable.setEnabled(false);
        
        // Configurar larguras das colunas
        produtosTable.getColumnModel().getColumn(0).setPreferredWidth(80);
        produtosTable.getColumnModel().getColumn(1).setPreferredWidth(350);
        produtosTable.getColumnModel().getColumn(2).setPreferredWidth(60);
        produtosTable.getColumnModel().getColumn(3).setPreferredWidth(100);
        produtosTable.getColumnModel().getColumn(4).setPreferredWidth(100);
        
        // Scroll pane com bordas elegantes
        JScrollPane scrollPane = new JScrollPane(produtosTable);
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200), 1));
        scrollPane.setBackground(Color.WHITE);
        scrollPane.setPreferredSize(new Dimension(0, 250));
        panel.add(scrollPane, BorderLayout.CENTER);
        
        // Painel de botões com design moderno
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 10));
        buttonPanel.setBackground(new Color(248, 249, 250));
        
        JButton btnRemover = createActionButton("🗑️ Remover Item", new Color(231, 76, 60), this::removerItem);
        JButton btnLimpar = createActionButton("🔄 Limpar Venda", new Color(241, 196, 15), this::limparVenda);
        
        buttonPanel.add(btnRemover);
        buttonPanel.add(btnLimpar);
        
        panel.add(buttonPanel, BorderLayout.SOUTH);
        
        return panel;
    }
    
    private JPanel createEnhancedInputPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createTitledBorder("➕ Adicionar Produto"),
            BorderFactory.createEmptyBorder(15, 15, 15, 15)
        ));
        panel.setBackground(new Color(255, 255, 255));
        
        // Campos de entrada com layout melhorado
        JPanel inputPanel = new JPanel(new GridBagLayout());
        inputPanel.setBackground(new Color(255, 255, 255));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
        // Linha 1: Código e Quantidade
        gbc.gridx = 0; gbc.gridy = 0; gbc.weightx = 0.3;
        inputPanel.add(createLabel("Código:"), gbc);
        
        gbc.gridx = 1; gbc.weightx = 0.7;
        txtCodigo = createTextField(15);
        inputPanel.add(txtCodigo, gbc);
        
        gbc.gridx = 2; gbc.weightx = 0.3;
        inputPanel.add(createLabel("Quantidade:"), gbc);
        
        gbc.gridx = 3; gbc.weightx = 0.7;
        txtQuantidade = createTextField(10);
        txtQuantidade.setText("1");
        inputPanel.add(txtQuantidade, gbc);
        
        // Linha 2: Descrição (readonly)
        gbc.gridx = 0; gbc.gridy = 1; gbc.weightx = 0.3;
        inputPanel.add(createLabel("Descrição:"), gbc);
        
        gbc.gridx = 1; gbc.gridwidth = 3; gbc.weightx = 2.0;
        txtDescricao = createTextField(40);
        txtDescricao.setEditable(false);
        txtDescricao.setBackground(new Color(240, 240, 240));
        inputPanel.add(txtDescricao, gbc);
        
        // Painel de botões de ação
        JPanel actionPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 10));
        actionPanel.setBackground(new Color(248, 249, 250));
        
        JButton btnAdicionar = createActionButton("➕ Adicionar", new Color(46, 204, 113), this::adicionarProduto);
        JButton btnBuscar = createActionButton("🔍 Buscar", new Color(52, 152, 219), e -> buscarProdutoPorCodigo(txtCodigo.getText().trim()));
        
        actionPanel.add(btnBuscar);
        actionPanel.add(btnAdicionar);
        
        panel.add(inputPanel, BorderLayout.CENTER);
        panel.add(actionPanel, BorderLayout.SOUTH);
        
        return panel;
    }
    
    private JPanel createEnhancedSidePanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(0, 20, 0, 0));
        panel.setOpaque(false);
        panel.setPreferredSize(new Dimension(300, 0));
        
        // Painel de resumo
        JPanel summaryPanel = createSummaryPanel();
        panel.add(summaryPanel, BorderLayout.CENTER);
        
        return panel;
    }
    
    // Métodos auxiliares para criação de componentes
    private JButton createActionButton(String text, Color color, ActionListener action) {
        JButton button = new JButton(text);
        button.setBackground(color);
        button.setForeground(Color.WHITE);
        button.setFont(new Font("Segoe UI", Font.BOLD, 12));
        button.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(color.darker(), 1),
            BorderFactory.createEmptyBorder(10, 20, 10, 20)
        ));
        button.setFocusPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.addActionListener(action);
        button.setOpaque(true);
        
        // Efeito hover
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(color.darker());
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(color);
            }
        });
        
        return button;
    }
    
    private JLabel createLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        label.setForeground(new Color(52, 73, 94));
        label.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        return label;
    }
    
    private JTextField createTextField(int columns) {
        JTextField textField = new JTextField(columns);
        textField.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        textField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200), 1),
            BorderFactory.createEmptyBorder(8, 10, 8, 10)
        ));
        textField.setBackground(Color.WHITE);
        textField.setForeground(new Color(44, 62, 80));
        return textField;
    }
    
        
    private JPanel createSummaryPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        panel.setBackground(new Color(255, 255, 255));
        panel.setPreferredSize(new Dimension(250, 0));
        
        // Título do resumo
        JLabel titleLabel = new JLabel("💰 Resumo da Venda");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 16));
        titleLabel.setForeground(new Color(41, 128, 185));
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(titleLabel);
        panel.add(Box.createVerticalStrut(20));
        
        // Painel de informações
        JPanel infoPanel = new JPanel();
        infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));
        infoPanel.setBackground(new Color(245, 245, 250));
        infoPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        
        // Itens
        JPanel itensPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        itensPanel.setBackground(new Color(245, 245, 250));
        JLabel lblItensIcon = new JLabel("📊");
        lblItensIcon.setFont(new Font("Arial", Font.PLAIN, 14));
        lblItens = new JLabel("0 itens");
        lblItens.setFont(new Font("Arial", Font.BOLD, 14));
        lblItens.setForeground(new Color(108, 117, 125));
        itensPanel.add(lblItensIcon);
        itensPanel.add(lblItens);
        infoPanel.add(itensPanel);
        
        infoPanel.add(Box.createVerticalStrut(10));
        
        // Separador
        JSeparator separator = new JSeparator();
        separator.setForeground(new Color(200, 200, 200));
        infoPanel.add(separator);
        
        infoPanel.add(Box.createVerticalStrut(10));
        
        // Total
        JPanel totalPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        totalPanel.setBackground(new Color(245, 245, 250));
        JLabel lblTotalIcon = new JLabel("💵");
        lblTotalIcon.setFont(new Font("Arial", Font.PLAIN, 16));
        lblTotal = new JLabel("R$ 0,00");
        lblTotal.setFont(new Font("Arial", Font.BOLD, 18));
        lblTotal.setForeground(new Color(40, 167, 69));
        totalPanel.add(lblTotalIcon);
        totalPanel.add(lblTotal);
        infoPanel.add(totalPanel);
        
        panel.add(infoPanel);
        panel.add(Box.createVerticalStrut(25));
        
        // Botões de ação
        JButton btnFinalizar = com.br.hermescomercial.theme.ModernTheme.createPastelButton("✅ Finalizar Venda", com.br.hermescomercial.theme.ModernTheme.PASTEL_GREEN, com.br.hermescomercial.theme.ModernTheme.TEXT_PRIMARY);
        btnFinalizar.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnFinalizar.setMaximumSize(new Dimension(Integer.MAX_VALUE, btnFinalizar.getPreferredSize().height));
        btnFinalizar.addActionListener(this::finalizarVenda);
        
        panel.add(btnFinalizar);
        panel.add(Box.createVerticalStrut(10));
        
        JButton btnCancelar = com.br.hermescomercial.theme.ModernTheme.createPastelButton("❌ Cancelar Venda", com.br.hermescomercial.theme.ModernTheme.PASTEL_CORAL, com.br.hermescomercial.theme.ModernTheme.TEXT_PRIMARY);
        btnCancelar.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnCancelar.setMaximumSize(new Dimension(Integer.MAX_VALUE, btnCancelar.getPreferredSize().height));
        btnCancelar.addActionListener(this::cancelarVenda);
        
        panel.add(btnCancelar);
        
        return panel;
    }
    
    private void adicionarProduto(ActionEvent e) {
        String codigo = txtCodigo.getText().trim();
        String quantidadeStr = txtQuantidade.getText().trim();
        
        if (codigo.isEmpty()) {
            JOptionPane.showMessageDialog(frame, "Informe o código do produto!", "Erro", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        try {
            int quantidade = Integer.parseInt(quantidadeStr);
            if (quantidade <= 0) {
                JOptionPane.showMessageDialog(frame, "Quantidade deve ser maior que zero!", "Erro", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            // Simulação de busca de produto (em implementação real, buscaria do banco)
            Produto produto = buscarProdutoPorCodigo(codigo);
            if (produto == null) {
                JOptionPane.showMessageDialog(frame, "Produto não encontrado!", "Erro", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            // Adicionar item
            ItemVenda item = new ItemVenda(produto, quantidade);
            itens.add(item);
            
            // Atualizar tabela
            Object[] row = {
                produto.getCodigo(),
                produto.getDescricao(),
                quantidade,
                String.format("R$ %.2f", produto.getPreco()),
                String.format("R$ %.2f", item.getSubtotal())
            };
            
            tableModel.addRow(row);
            
            // Limpar campos e resetar título
            txtCodigo.setText("");
            txtDescricao.setText("");
            txtQuantidade.setText("1");
            txtCodigo.requestFocus();
            frame.setTitle("PDV - Nova Venda v2.1.0 - Premium");
            
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(frame, "Quantidade inválida!", "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void removerItem(ActionEvent e) {
        int selectedRow = produtosTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(frame, "Selecione um item para remover!", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        int confirm = com.br.hermescomercial.theme.ModernTheme.showCustomConfirmDialog(frame, 
            "Deseja remover este item?", "Confirmar", 
            new String[]{"Sim", "Não"}, 0);
            
        if (confirm == 0) {
            itens.remove(selectedRow);
            tableModel.removeRow(selectedRow);
            atualizarResumo();
        }
    }
    
    private void limparVenda(ActionEvent e) {
        int confirm = com.br.hermescomercial.theme.ModernTheme.showCustomConfirmDialog(frame, 
            "Deseja limpar todos os itens da venda?", "Confirmar", 
            new String[]{"Sim", "Não"}, 0);
            
        if (confirm == 0) {
            itens.clear();
            tableModel.setRowCount(0);
            atualizarResumo();
            txtCodigo.setText("");
            txtQuantidade.setText("1");
        }
    }
    
    private void finalizarVenda(ActionEvent e) {
        if (itens.isEmpty()) {
            JOptionPane.showMessageDialog(frame, "Adicione produtos à venda!", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        BigDecimal total = calcularTotal();
        
        int confirm = com.br.hermescomercial.theme.ModernTheme.showCustomConfirmDialog(frame, 
            String.format("Confirmar finalização da venda?\n\nTotal: R$ %.2f", total), 
            "Confirmar Venda", 
            new String[]{"Sim", "Não"}, 0);
            
        if (confirm == 0) {
            JOptionPane.showMessageDialog(frame, 
                "Venda finalizada com sucesso!\n\n" +
                "Total: R$ " + String.format("%.2f", total) + "\n" +
                "Itens: " + itens.size() + "\n" +
                "Versão SWING 2.0",
                "Venda Concluída", JOptionPane.INFORMATION_MESSAGE);
            
            // Limpar para nova venda
            limparVenda(null);
        }
    }
    
    private void cancelarVenda(ActionEvent e) {
        int confirm = com.br.hermescomercial.theme.ModernTheme.showCustomConfirmDialog(frame, 
            "Deseja cancelar esta venda?", "Cancelar Venda", 
            new String[]{"Sim", "Não"}, 0);
            
        if (confirm == 0) {
            frame.dispose();
        }
    }
    
    private void atualizarResumo() {
        lblItens.setText("Itens: " + itens.size());
        lblTotal.setText("Total: R$ " + String.format("%.2f", calcularTotal()));
    }
    
    private BigDecimal calcularTotal() {
        return itens.stream()
            .map(ItemVenda::getSubtotal)
            .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
    
    // Simulação de busca de produto (em implementação real, buscaria do banco)
    private Produto buscarProdutoPorCodigo(String codigo) {
        // Produtos de exemplo
        if ("001".equals(codigo)) {
            return new Produto("001", "Produto Exemplo 1", new BigDecimal("10.50"));
        } else if ("002".equals(codigo)) {
            return new Produto("002", "Produto Exemplo 2", new BigDecimal("25.99"));
        } else if ("003".equals(codigo)) {
            return new Produto("003", "Produto Exemplo 3", new BigDecimal("5.75"));
        }
        return null;
    }
    
    private Produto buscarProdutoPorDescricao(String descricao) {
        // Busca case-insensitive por descrição
        descricao = descricao.toLowerCase();
        
        if ("produto exemplo 1".contains(descricao) || "exemplo 1".contains(descricao)) {
            return new Produto("001", "Produto Exemplo 1", new BigDecimal("10.50"));
        } else if ("produto exemplo 2".contains(descricao) || "exemplo 2".contains(descricao)) {
            return new Produto("002", "Produto Exemplo 2", new BigDecimal("25.99"));
        } else if ("produto exemplo 3".contains(descricao) || "exemplo 3".contains(descricao)) {
            return new Produto("003", "Produto Exemplo 3", new BigDecimal("5.75"));
        }
        
        return null;
    }
    
    // Busca por código (automática ao pressionar Enter)
    private void buscarPorCodigo() {
        String codigo = txtCodigo.getText().trim();
        
        if (codigo.isEmpty()) {
            return;
        }
        
        // Buscar produto por código (simulação)
        Produto produto = buscarProdutoPorCodigo(codigo);
        
        if (produto != null) {
            txtDescricao.setText(produto.getDescricao());
            txtQuantidade.requestFocus();
            txtQuantidade.selectAll();
            
            // Feedback visual sutil
            frame.setTitle("PDV - Nova Venda v2.1.0 - Premium - Produto: " + produto.getDescricao());
        } else {
            JOptionPane.showMessageDialog(frame, 
                "Produto não encontrado com o código: " + codigo,
                "Produto Não Encontrado", JOptionPane.WARNING_MESSAGE);
            txtCodigo.selectAll();
            txtCodigo.requestFocus();
        }
    }
    
    // Busca por descrição
    private void buscarPorDescricao(ActionEvent e) {
        String descricao = txtDescricao.getText().trim();
        
        if (descricao.isEmpty()) {
            JOptionPane.showMessageDialog(frame, 
                "Digite uma descrição para buscar o produto!",
                "Busca de Produto", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        // Buscar produto por descrição (simulação)
        Produto produto = buscarProdutoPorDescricao(descricao);
        
        if (produto != null) {
            txtCodigo.setText(produto.getCodigo());
            txtDescricao.setText(produto.getDescricao());
            txtQuantidade.requestFocus();
            txtQuantidade.selectAll();
            
            // Feedback visual
            JOptionPane.showMessageDialog(frame, 
                "Produto encontrado: " + produto.getDescricao() + " (Código: " + produto.getCodigo() + ")",
                "Produto Encontrado", JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(frame, 
                "Nenhum produto encontrado com a descrição: " + descricao,
                "Produto Não Encontrado", JOptionPane.WARNING_MESSAGE);
        }
    }
    
    public void show() {
        frame.setVisible(true);
    }
    
    // Classes de apoio
    private static class Produto {
        private String codigo;
        private String descricao;
        private BigDecimal preco;
        
        public Produto(String codigo, String descricao, BigDecimal preco) {
            this.codigo = codigo;
            this.descricao = descricao;
            this.preco = preco;
        }
        
        public String getCodigo() { return codigo; }
        public String getDescricao() { return descricao; }
        public BigDecimal getPreco() { return preco; }
    }
    
    private static class ItemVenda {
        private Produto produto;
        private int quantidade;
        
        public ItemVenda(Produto produto, int quantidade) {
            this.produto = produto;
            this.quantidade = quantidade;
        }
        
        public BigDecimal getSubtotal() {
            return produto.getPreco().multiply(new BigDecimal(quantidade));
        }
    }
}
