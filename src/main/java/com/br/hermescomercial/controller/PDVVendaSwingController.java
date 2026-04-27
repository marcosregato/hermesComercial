package com.br.hermescomercial.controller;

import com.br.hermescomercial.theme.ModernTheme;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
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
        
        frame = new JFrame("PDV - Nova Venda v2.0.0 - Premium");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setSize(900, 600);
        frame.setLocationRelativeTo(null);
        
        // Configurar fundo elegante
        frame.getContentPane().setBackground(ModernTheme.BACKGROUND_PRIMARY);
        
        frame.add(createMainPanel());
    }
    
    private JPanel createMainPanel() {
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        mainPanel.setBackground(new Color(245, 245, 250));
        
        // Header
        mainPanel.add(createHeaderPanel(), BorderLayout.NORTH);
        
        // Center com layout melhorado
        JPanel centerPanel = new JPanel(new BorderLayout());
        centerPanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
        centerPanel.setBackground(new Color(245, 245, 250));
        
        // Painel superior com tabela
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBorder(BorderFactory.createTitledBorder("Itens da Venda"));
        topPanel.setBackground(new Color(255, 255, 255));
        topPanel.add(createProdutosPanel(), BorderLayout.CENTER);
        
        // Painel inferior com inputs
        JPanel bottomPanel = new JPanel(new BorderLayout());
        bottomPanel.setBorder(BorderFactory.createTitledBorder("Adicionar Produto"));
        bottomPanel.setBackground(new Color(255, 255, 255));
        bottomPanel.add(createInputPanel(), BorderLayout.CENTER);
        
        centerPanel.add(topPanel, BorderLayout.CENTER);
        centerPanel.add(bottomPanel, BorderLayout.SOUTH);
        mainPanel.add(centerPanel, BorderLayout.CENTER);
        
        // Right com resumo
        mainPanel.add(createSummaryPanel(), BorderLayout.EAST);
        
        return mainPanel;
    }
    
    private JPanel createHeaderPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 20, 10));
        panel.setBackground(new Color(41, 128, 185));
        panel.setPreferredSize(new Dimension(0, 80));
        
        // Título central
        JLabel titleLabel = new JLabel("Nova Venda v2.0.0 - Premium", JLabel.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setForeground(Color.WHITE);
        
        // Botão voltar estilizado
        JButton btnVoltar = new JButton("← Voltar");
        btnVoltar.setBackground(new Color(255, 255, 255));
        btnVoltar.setForeground(new Color(41, 128, 185));
        btnVoltar.setFont(new Font("Arial", Font.BOLD, 12));
        btnVoltar.setBorder(BorderFactory.createEmptyBorder(8, 15, 8, 15));
        btnVoltar.setFocusPainted(false);
        btnVoltar.addActionListener(e -> frame.dispose());
        
        // Data e hora atual
        JLabel lblDataHora = new JLabel(java.time.LocalDateTime.now().format(java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")), JLabel.RIGHT);
        lblDataHora.setFont(new Font("Arial", Font.PLAIN, 12));
        lblDataHora.setForeground(Color.WHITE);
        
        panel.add(btnVoltar, BorderLayout.WEST);
        panel.add(titleLabel, BorderLayout.CENTER);
        panel.add(lblDataHora, BorderLayout.EAST);
        
        return panel;
    }
    
    private JPanel createProdutosPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
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
        produtosTable.setRowHeight(25);
        produtosTable.setFont(new Font("Arial", Font.PLAIN, 12));
        produtosTable.getTableHeader().setFont(new Font("Arial", Font.BOLD, 12));
        produtosTable.getTableHeader().setBackground(new Color(41, 128, 185));
        produtosTable.getTableHeader().setForeground(Color.WHITE);
        
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
        panel.add(scrollPane, BorderLayout.CENTER);
        
        // Painel de botões com design moderno
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 10));
        buttonPanel.setBackground(new Color(245, 245, 250));
        
        JButton btnRemover = new JButton("🗑️ Remover Item");
        btnRemover.setBackground(new Color(220, 53, 69));
        btnRemover.setForeground(Color.WHITE);
        btnRemover.setFont(new Font("Arial", Font.BOLD, 12));
        btnRemover.setBorder(BorderFactory.createEmptyBorder(8, 15, 8, 15));
        btnRemover.setFocusPainted(false);
        btnRemover.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnRemover.addActionListener(this::removerItem);
        
        JButton btnLimpar = new JButton("🔄 Limpar Venda");
        btnLimpar.setBackground(new Color(255, 193, 7));
        btnLimpar.setForeground(Color.BLACK);
        btnLimpar.setFont(new Font("Arial", Font.BOLD, 12));
        btnLimpar.setBorder(BorderFactory.createEmptyBorder(8, 15, 8, 15));
        btnLimpar.setFocusPainted(false);
        btnLimpar.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnLimpar.addActionListener(this::limparVenda);
        
        buttonPanel.add(btnRemover);
        buttonPanel.add(btnLimpar);
        
        panel.add(buttonPanel, BorderLayout.SOUTH);
        
        return panel;
    }
    
    private JPanel createInputPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        panel.setBackground(new Color(255, 255, 255));
        
        // Título do painel
        JLabel titleLabel = new JLabel("📦 Adicionar Produto");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 16));
        titleLabel.setForeground(new Color(41, 128, 185));
        
        // Campos de entrada com layout melhorado
        JPanel inputPanel = new JPanel(new GridBagLayout());
        inputPanel.setBackground(new Color(255, 255, 255));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.anchor = GridBagConstraints.WEST;
        
        // Linha 1: Código e Quantidade
        gbc.gridx = 0; gbc.gridy = 0; gbc.fill = GridBagConstraints.NONE; gbc.weightx = 0;
        JLabel lblCodigo = new JLabel("Código:");
        lblCodigo.setFont(new Font("Arial", Font.BOLD, 12));
        inputPanel.add(lblCodigo, gbc);
        
        gbc.gridx = 1; gbc.fill = GridBagConstraints.HORIZONTAL; gbc.weightx = 1.0;
        txtCodigo = new JTextField();
        txtCodigo.setFont(new Font("Arial", Font.PLAIN, 12));
        txtCodigo.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200), 1));
        txtCodigo.setToolTipText("Digite o código do produto e pressione Enter");
        inputPanel.add(txtCodigo, gbc);
        
        gbc.gridx = 2; gbc.fill = GridBagConstraints.NONE; gbc.weightx = 0; gbc.insets = new Insets(8, 15, 8, 8);
        JLabel lblQuantidade = new JLabel("Qtd:");
        lblQuantidade.setFont(new Font("Arial", Font.BOLD, 12));
        inputPanel.add(lblQuantidade, gbc);
        
        gbc.gridx = 3; gbc.fill = GridBagConstraints.HORIZONTAL; gbc.weightx = 0.3; gbc.insets = new Insets(8, 5, 8, 8);
        txtQuantidade = new JTextField("1");
        txtQuantidade.setFont(new Font("Arial", Font.PLAIN, 12));
        txtQuantidade.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200), 1));
        inputPanel.add(txtQuantidade, gbc);
        
        // Linha 2: Descrição (readonly)
        gbc.gridx = 0; gbc.gridy = 1; gbc.fill = GridBagConstraints.NONE; gbc.weightx = 0; gbc.insets = new Insets(8, 8, 8, 8);
        JLabel lblDescricao = new JLabel("Descrição:");
        lblDescricao.setFont(new Font("Arial", Font.BOLD, 12));
        inputPanel.add(lblDescricao, gbc);
        
        gbc.gridx = 1; gbc.gridy = 1; gbc.gridwidth = 3; gbc.fill = GridBagConstraints.HORIZONTAL; gbc.weightx = 1.0;
        txtDescricao = new JTextField();
        txtDescricao.setEditable(true);
        txtDescricao.setBackground(Color.WHITE);
        txtDescricao.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200), 1));
        txtDescricao.setFont(new Font("Arial", Font.PLAIN, 12));
        txtDescricao.setToolTipText("Digite parte da descrição do produto para buscar");
        inputPanel.add(txtDescricao, gbc);
        
        // Adicionar listeners para busca automática
        txtCodigo.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                if (evt.getKeyCode() == java.awt.event.KeyEvent.VK_ENTER) {
                    buscarPorCodigo();
                }
            }
        });
        
        txtDescricao.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                if (evt.getKeyCode() == java.awt.event.KeyEvent.VK_ENTER) {
                    buscarPorDescricao(null);
                }
            }
        });
        
        // Botões de ação
        gbc.gridx = 0; gbc.gridy = 2; gbc.gridwidth = 4; gbc.fill = GridBagConstraints.NONE; gbc.anchor = GridBagConstraints.CENTER; gbc.insets = new Insets(15, 8, 8, 8);
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 0));
        buttonPanel.setBackground(new Color(255, 255, 255));
        
        JButton btnAdicionar = new JButton("➕ Adicionar Item");
        btnAdicionar.setBackground(new Color(40, 167, 69));
        btnAdicionar.setForeground(Color.WHITE);
        btnAdicionar.setFont(new Font("Arial", Font.BOLD, 12));
        btnAdicionar.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        btnAdicionar.setFocusPainted(false);
        btnAdicionar.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnAdicionar.addActionListener(this::adicionarProduto);
        
        JButton btnBuscar = new JButton("🔍 Buscar Produto");
        btnBuscar.setBackground(new Color(23, 162, 184));
        btnBuscar.setForeground(Color.WHITE);
        btnBuscar.setFont(new Font("Arial", Font.BOLD, 12));
        btnBuscar.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        btnBuscar.setFocusPainted(false);
        btnBuscar.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnBuscar.addActionListener(e -> buscarPorDescricao(e));
        
        JButton btnEnter = new JButton("⏎ Enter");
        btnEnter.setBackground(new Color(108, 117, 125));
        btnEnter.setForeground(Color.WHITE);
        btnEnter.setFont(new Font("Arial", Font.BOLD, 12));
        btnEnter.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));
        btnEnter.setFocusPainted(false);
        btnEnter.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnEnter.addActionListener(this::adicionarProduto);
        
        buttonPanel.add(btnAdicionar);
        buttonPanel.add(btnBuscar);
        buttonPanel.add(btnEnter);
        
        inputPanel.add(buttonPanel, gbc);
        
        // Montar painel final
        JPanel titlePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        titlePanel.setBackground(new Color(255, 255, 255));
        titlePanel.add(titleLabel);
        
        panel.add(titlePanel, BorderLayout.NORTH);
        panel.add(inputPanel, BorderLayout.CENTER);
        
        return panel;
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
        JButton btnFinalizar = new JButton("✅ Finalizar Venda");
        btnFinalizar.setBackground(new Color(40, 167, 69));
        btnFinalizar.setForeground(Color.WHITE);
        btnFinalizar.setFont(new Font("Arial", Font.BOLD, 12));
        btnFinalizar.setBorder(BorderFactory.createEmptyBorder(12, 20, 12, 20));
        btnFinalizar.setFocusPainted(false);
        btnFinalizar.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnFinalizar.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnFinalizar.setMaximumSize(new Dimension(Integer.MAX_VALUE, btnFinalizar.getPreferredSize().height));
        btnFinalizar.addActionListener(this::finalizarVenda);
        
        panel.add(btnFinalizar);
        panel.add(Box.createVerticalStrut(10));
        
        JButton btnCancelar = new JButton("❌ Cancelar Venda");
        btnCancelar.setBackground(new Color(220, 53, 69));
        btnCancelar.setForeground(Color.WHITE);
        btnCancelar.setFont(new Font("Arial", Font.BOLD, 12));
        btnCancelar.setBorder(BorderFactory.createEmptyBorder(12, 20, 12, 20));
        btnCancelar.setFocusPainted(false);
        btnCancelar.setCursor(new Cursor(Cursor.HAND_CURSOR));
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
            frame.setTitle("PDV - Nova Venda v2.0.0 - Premium");
            
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
        
        int confirm = JOptionPane.showConfirmDialog(frame, 
            "Deseja remover este item?", "Confirmar", JOptionPane.YES_NO_OPTION);
            
        if (confirm == JOptionPane.YES_OPTION) {
            itens.remove(selectedRow);
            tableModel.removeRow(selectedRow);
            atualizarResumo();
        }
    }
    
    private void limparVenda(ActionEvent e) {
        int confirm = JOptionPane.showConfirmDialog(frame, 
            "Deseja limpar todos os itens da venda?", "Confirmar", JOptionPane.YES_NO_OPTION);
            
        if (confirm == JOptionPane.YES_OPTION) {
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
        
        int confirm = JOptionPane.showConfirmDialog(frame, 
            String.format("Confirmar finalização da venda?\n\nTotal: R$ %.2f", total), 
            "Confirmar Venda", JOptionPane.YES_NO_OPTION);
            
        if (confirm == JOptionPane.YES_OPTION) {
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
        int confirm = JOptionPane.showConfirmDialog(frame, 
            "Deseja cancelar esta venda?", "Cancelar Venda", JOptionPane.YES_NO_OPTION);
            
        if (confirm == JOptionPane.YES_OPTION) {
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
            frame.setTitle("PDV - Nova Venda v2.0.0 - Premium - Produto: " + produto.getDescricao());
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
