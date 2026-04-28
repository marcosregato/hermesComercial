package com.br.hermescomercial.erp.controller;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;
import java.math.BigDecimal;
import java.text.DecimalFormat;

/**
 * Controller para tela de gestão de produtos no ERP
 * Versão 2.3.0 - Arquitetura Modular
 */
public class ERPProdutoSwingController {
    
    private JFrame frame;
    private JPanel mainPanel;
    private JTable produtosTable;
    private DefaultTableModel tableModel;
    private JTextField txtBusca, txtNome, txtCodigo, txtDescricao, txtPreco, txtEstoque, txtCategoria;
    private JButton btnNovo, btnEditar, btnExcluir, btnSalvar, btnCancelar, btnAtualizar, btnSincronizar;
    private JComboBox<String> cbUnidade, cbStatus;
    private boolean editMode = false;
    private Long produtoEditId = null;
    private DecimalFormat currencyFormat;
    
    public ERPProdutoSwingController() {
        initializeUI();
    }
    
    private void initializeUI() {
        frame = new JFrame("🏢 Gestão de Produtos - ERP");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setSize(1200, 700);
        frame.setLocationRelativeTo(null);
        
        // Formato para moeda
        currencyFormat = new DecimalFormat("R$ #,##0.00");
        
        // Aplicar tema padrão das telas antigas
        frame.getContentPane().setBackground(new Color(250, 250, 250)); // Cinza muito suave
        
        // Painel principal
        mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));
        mainPanel.setBackground(new Color(250, 250, 250)); // Cinza muito suave
        
        // Painel superior com busca e botões
        JPanel topPanel = new JPanel(new BorderLayout(10, 0));
        topPanel.setBackground(new Color(250, 250, 250)); // Cinza muito suave
        
        // Painel de busca
        JPanel buscaPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        buscaPanel.setBackground(new Color(250, 250, 250)); // Cinza muito suave
        
        JLabel lblBusca = new JLabel("🔍 Buscar:");
        lblBusca.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lblBusca.setForeground(new Color(52, 73, 94)); // Azul suave profundo
        
        txtBusca = createModernTextField(20);
        txtBusca.setToolTipText("Digite para buscar produtos por nome, código ou categoria");
        
        JButton btnBuscar = createModernButton("Buscar", new Color(41, 128, 185)); // Azul padrão
        btnBuscar.addActionListener(e -> buscarProdutos());
        
        JButton btnLimparBusca = createModernButton("Limpar", new Color(149, 165, 166)); // Cinza padrão
        btnLimparBusca.addActionListener(e -> limparBusca());
        
        buscaPanel.add(lblBusca);
        buscaPanel.add(txtBusca);
        buscaPanel.add(btnBuscar);
        buscaPanel.add(btnLimparBusca);
        
        // Painel de botões de ação
        JPanel acoesPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        acoesPanel.setBackground(new Color(250, 250, 250)); // Cinza muito suave
        
        btnNovo = createModernButton("➕ Novo", new Color(40, 167, 69)); // Verde padrão
        btnNovo.addActionListener(e -> novoProduto());
        
        btnAtualizar = createModernButton("🔄 Atualizar", new Color(149, 165, 166)); // Cinza padrão
        btnAtualizar.addActionListener(e -> carregarDadosExemplo());
        
        btnSincronizar = createModernButton("📡 Sincronizar", new Color(255, 193, 7)); // Amarelo padrão
        btnSincronizar.addActionListener(e -> sincronizarEstoque());
        
        acoesPanel.add(btnNovo);
        acoesPanel.add(btnAtualizar);
        acoesPanel.add(btnSincronizar);
        
        topPanel.add(buscaPanel, BorderLayout.CENTER);
        topPanel.add(acoesPanel, BorderLayout.EAST);
        
        // Painel do meio com formulário e tabela
        JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
        splitPane.setDividerLocation(250);
        splitPane.setResizeWeight(0.3);
        
        // Painel de formulário
        JPanel formularioPanel = criarPainelFormulario();
        
        // Painel da tabela
        JPanel tabelaPanel = criarPainelTabela();
        
        splitPane.setTopComponent(formularioPanel);
        splitPane.setBottomComponent(tabelaPanel);
        
        // Adicionar componentes ao painel principal
        mainPanel.add(topPanel, BorderLayout.NORTH);
        mainPanel.add(splitPane, BorderLayout.CENTER);
        
        frame.add(mainPanel);
        frame.setVisible(true);
    }
    
    private JPanel criarPainelFormulario() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createEtchedBorder(), 
            "📝 Cadastro de Produtos", 
            TitledBorder.LEFT, 
            TitledBorder.TOP, 
            new Font("Segoe UI", Font.BOLD, 12), 
            new Color(52, 73, 94))); // Azul suave profundo
        panel.setBackground(new Color(250, 252, 252)); // Branco suave
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
        // Código
        gbc.gridx = 0; gbc.gridy = 0;
        panel.add(createLabel("Código:"), gbc);
        gbc.gridx = 1; gbc.weightx = 1.0;
        txtCodigo = createModernTextField(15);
        txtCodigo.setEditable(false);
        panel.add(txtCodigo, gbc);
        
        // Nome
        gbc.gridx = 0; gbc.gridy = 1; gbc.weightx = 0;
        panel.add(createLabel("Nome*:"), gbc);
        gbc.gridx = 1; gbc.weightx = 1.0;
        txtNome = createModernTextField(30);
        panel.add(txtNome, gbc);
        
        // Descrição
        gbc.gridx = 0; gbc.gridy = 2; gbc.weightx = 0;
        panel.add(createLabel("Descrição:"), gbc);
        gbc.gridx = 1; gbc.weightx = 1.0;
        txtDescricao = createModernTextField(30);
        panel.add(txtDescricao, gbc);
        
        // Categoria
        gbc.gridx = 0; gbc.gridy = 3; gbc.weightx = 0;
        panel.add(createLabel("Categoria:"), gbc);
        gbc.gridx = 1; gbc.weightx = 1.0;
        txtCategoria = createModernTextField(20);
        panel.add(txtCategoria, gbc);
        
        // Preço
        gbc.gridx = 0; gbc.gridy = 4; gbc.weightx = 0;
        panel.add(createLabel("Preço*:"), gbc);
        gbc.gridx = 1; gbc.weightx = 1.0;
        txtPreco = createModernTextField(15);
        panel.add(txtPreco, gbc);
        
        // Estoque
        gbc.gridx = 0; gbc.gridy = 5; gbc.weightx = 0;
        panel.add(createLabel("Estoque:"), gbc);
        gbc.gridx = 1; gbc.weightx = 1.0;
        txtEstoque = createModernTextField(10);
        panel.add(txtEstoque, gbc);
        
        // Unidade
        gbc.gridx = 0; gbc.gridy = 6; gbc.weightx = 0;
        panel.add(createLabel("Unidade:"), gbc);
        gbc.gridx = 1; gbc.weightx = 1.0;
        cbUnidade = new JComboBox<>(new String[]{"UN", "KG", "LT", "CX", "PCT", "M", "CM"});
        cbUnidade.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        cbUnidade.setBackground(Color.WHITE);
        panel.add(cbUnidade, gbc);
        
        // Status
        gbc.gridx = 0; gbc.gridy = 7; gbc.weightx = 0;
        panel.add(createLabel("Status:"), gbc);
        gbc.gridx = 1; gbc.weightx = 1.0;
        cbStatus = new JComboBox<>(new String[]{"Ativo", "Inativo", "Descontinuado"});
        cbStatus.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        cbStatus.setBackground(Color.WHITE);
        panel.add(cbStatus, gbc);
        
        // Botões de ação do formulário
        JPanel formButtonsPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        formButtonsPanel.setBackground(new Color(250, 252, 252)); // Branco suave
        
        btnSalvar = createModernButton("💾 Salvar", new Color(40, 167, 69)); // Verde padrão
        btnSalvar.addActionListener(e -> salvarProduto());
        
        btnCancelar = createModernButton("❌ Cancelar", new Color(220, 53, 69)); // Vermelho padrão
        btnCancelar.addActionListener(e -> cancelarEdicao());
        
        formButtonsPanel.add(btnSalvar);
        formButtonsPanel.add(btnCancelar);
        
        gbc.gridx = 0; gbc.gridy = 8; gbc.gridwidth = 2; gbc.weightx = 1.0;
        panel.add(formButtonsPanel, gbc);
        
        return panel;
    }
    
    private JPanel criarPainelTabela() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createEtchedBorder(), 
            "📋 Lista de Produtos", 
            TitledBorder.LEFT, 
            TitledBorder.TOP, 
            new Font("Segoe UI", Font.BOLD, 12), 
            new Color(52, 73, 94))); // Azul suave profundo
        panel.setBackground(new Color(250, 252, 252)); // Branco suave
        
        // Criar tabela
        String[] colunas = {"Código", "Nome", "Categoria", "Preço", "Estoque", "Unidade", "Status"};
        tableModel = new DefaultTableModel(colunas, 0) {
            @Override
            public Class<?> getColumnClass(int columnIndex) {
                switch (columnIndex) {
                    case 0: return Long.class; // Código
                    case 1: return String.class; // Nome
                    case 2: return String.class; // Categoria
                    case 3: return BigDecimal.class; // Preço
                    case 4: return Integer.class; // Estoque
                    case 5: return String.class; // Unidade
                    case 6: return String.class; // Status
                    default: return String.class;
                }
            }
            
            @Override
            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return false; // Tabela não editável diretamente
            }
        };
        
        produtosTable = new JTable(tableModel);
        produtosTable.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        produtosTable.setRowHeight(25);
        produtosTable.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 12));
        produtosTable.getTableHeader().setBackground(new Color(41, 128, 185)); // Azul padrão
        produtosTable.getTableHeader().setForeground(Color.WHITE);
        
        // Formatar coluna de preço
        produtosTable.getColumnModel().getColumn(3).setCellRenderer(new CurrencyRenderer());
        
        // Scroll da tabela
        JScrollPane scrollPane = new JScrollPane(produtosTable);
        scrollPane.setPreferredSize(new Dimension(800, 300));
        
        panel.add(scrollPane, BorderLayout.CENTER);
        
        // Painel de botões da tabela
        JPanel tableButtonsPanel = new JPanel(new FlowLayout());
        tableButtonsPanel.setBackground(new Color(250, 252, 252)); // Branco suave
        
        btnEditar = createModernButton("✏️ Editar", new Color(255, 193, 7)); // Amarelo padrão
        btnEditar.addActionListener(e -> editarProduto());
        
        btnExcluir = createModernButton("🗑️ Excluir", new Color(220, 53, 69)); // Vermelho padrão
        btnExcluir.addActionListener(e -> excluirProduto());
        
        tableButtonsPanel.add(btnEditar);
        tableButtonsPanel.add(btnExcluir);
        
        panel.add(tableButtonsPanel, BorderLayout.SOUTH);
        
        return panel;
    }
    
    private JLabel createLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        label.setForeground(new Color(52, 73, 94)); // Azul suave profundo
        return label;
    }
    
    private JTextField createModernTextField(int columns) {
        JTextField field = new JTextField(columns);
        field.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        field.setBackground(Color.WHITE);
        field.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(189, 195, 199)), // Cinza suave
            BorderFactory.createEmptyBorder(5, 5, 5, 5)
        ));
        return field;
    }
    
    private JButton createModernButton(String text, Color bgColor) {
        JButton button = new JButton(text);
        button.setBackground(new Color(149, 165, 166)); // Cinza azulado suave (padrão)
        button.setForeground(new Color(255, 255, 255)); // Branco elegante
        button.setFont(new Font("Segoe UI", Font.BOLD, 12));
        button.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(127, 140, 141), 2), // Cinza borda suave
            BorderFactory.createEmptyBorder(12, 20, 12, 20)
        ));
        button.setFocusPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setOpaque(true);
        
        // Efeito hover elegante (padrão das telas antigas)
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(new Color(127, 140, 141)); // Cinza mais escuro suave
                button.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(new Color(108, 117, 125), 2), // Cinza ainda mais escuro
                    BorderFactory.createEmptyBorder(12, 20, 12, 20)
                ));
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(new Color(149, 165, 166)); // Cinza azulado suave
                button.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(new Color(127, 140, 141), 2), // Cinza borda suave
                    BorderFactory.createEmptyBorder(12, 20, 12, 20)
                ));
            }
        });
        
        return button;
    }
    
    private void novoProduto() {
        limparFormulario();
        editMode = false;
        produtoEditId = null;
        txtNome.requestFocus();
    }
    
    private void editarProduto() {
        int linhaSelecionada = produtosTable.getSelectedRow();
        
        if (linhaSelecionada == -1) {
            JOptionPane.showMessageDialog(frame, 
                "⚠️ Nenhum produto selecionado!\n" +
                "Selecione um produto na tabela para editar.", 
                "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        // Preencher formulário com dados selecionados
        produtoEditId = (Long) tableModel.getValueAt(linhaSelecionada, 0);
        txtCodigo.setText(produtoEditId.toString());
        txtNome.setText((String) tableModel.getValueAt(linhaSelecionada, 1));
        txtCategoria.setText((String) tableModel.getValueAt(linhaSelecionada, 2));
        txtPreco.setText(tableModel.getValueAt(linhaSelecionada, 3).toString());
        txtEstoque.setText(tableModel.getValueAt(linhaSelecionada, 4).toString());
        cbUnidade.setSelectedItem((String) tableModel.getValueAt(linhaSelecionada, 5));
        cbStatus.setSelectedItem((String) tableModel.getValueAt(linhaSelecionada, 6));
        
        editMode = true;
        btnSalvar.setText("💾 Atualizar");
        txtNome.requestFocus();
    }
    
    private void salvarProduto() {
        String nome = txtNome.getText().trim();
        String descricao = txtDescricao.getText().trim();
        String categoria = txtCategoria.getText().trim();
        String precoText = txtPreco.getText().trim();
        String estoqueText = txtEstoque.getText().trim();
        
        // Validações
        if (nome.isEmpty()) {
            JOptionPane.showMessageDialog(frame, 
                "⚠️ Nome é obrigatório!", 
                "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        if (categoria.isEmpty()) {
            JOptionPane.showMessageDialog(frame, 
                "⚠️ Categoria é obrigatória!", 
                "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        try {
            BigDecimal preco = new BigDecimal(precoText.replace("R$", "").replace(",", "."));
            int estoque = Integer.parseInt(estoqueText);
            
            if (editMode) {
                // TODO: Implementar atualização no banco de dados
                JOptionPane.showMessageDialog(frame, 
                    "✅ Produto atualizado com sucesso!\n" +
                    "Nome: " + nome + "\n" +
                    "Descrição: " + descricao + "\n" +
                    "Preço: " + currencyFormat.format(preco), 
                    "Sucesso", JOptionPane.INFORMATION_MESSAGE);
            } else {
                // TODO: Implementar salvamento no banco de dados
                Long novoId = System.currentTimeMillis();
                Object[] novaLinha = {
                    novoId, nome, categoria, preco, estoque, 
                    cbUnidade.getSelectedItem(), cbStatus.getSelectedItem()
                };
                tableModel.addRow(novaLinha);
                
                JOptionPane.showMessageDialog(frame, 
                    "✅ Produto salvo com sucesso!\n" +
                    "Nome: " + nome + "\n" +
                    "Descrição: " + descricao + "\n" +
                    "Preço: " + currencyFormat.format(preco), 
                    "Sucesso", JOptionPane.INFORMATION_MESSAGE);
            }
            
            limparFormulario();
            
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(frame, 
                "⚠️ Preço e estoque devem ser valores numéricos!", 
                "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void excluirProduto() {
        int linhaSelecionada = produtosTable.getSelectedRow();
        
        if (linhaSelecionada == -1) {
            JOptionPane.showMessageDialog(frame, 
                "⚠️ Nenhum produto selecionado!\n" +
                "Selecione um produto na tabela para excluir.", 
                "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        String nome = (String) tableModel.getValueAt(linhaSelecionada, 1);
        
        int confirmacao = JOptionPane.showConfirmDialog(frame, 
            "🗑️ Confirmar Exclusão\n" +
            "Deseja realmente excluir o produto selecionado?\n\n" +
            "Nome: " + nome, 
            "Confirmar Exclusão", 
            JOptionPane.YES_NO_OPTION, 
            JOptionPane.QUESTION_MESSAGE);
        
        if (confirmacao == JOptionPane.YES_OPTION) {
            try {
                // TODO: Implementar exclusão no banco de dados
                tableModel.removeRow(linhaSelecionada);
                
                JOptionPane.showMessageDialog(frame, 
                    "✅ Produto excluído com sucesso!", 
                    "Sucesso", JOptionPane.INFORMATION_MESSAGE);
            } catch (Exception e) {
                JOptionPane.showMessageDialog(frame, 
                    "❌ Erro ao excluir produto: " + e.getMessage(), 
                    "Erro", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    private void buscarProdutos() {
        String textoBusca = txtBusca.getText().trim().toLowerCase();
        
        if (textoBusca.isEmpty()) {
            carregarDadosExemplo();
            return;
        }
        
        // TODO: Implementar busca no banco de dados
        JOptionPane.showMessageDialog(frame, 
            "🔍 Funcionalidade de busca em desenvolvimento.\n" +
            "Buscando por: " + textoBusca, 
            "Informação", JOptionPane.INFORMATION_MESSAGE);
    }
    
    private void limparBusca() {
        txtBusca.setText("");
        carregarDadosExemplo();
    }
    
    private void limparFormulario() {
        txtCodigo.setText("");
        txtNome.setText("");
        txtDescricao.setText("");
        txtCategoria.setText("");
        txtPreco.setText("");
        txtEstoque.setText("");
        cbUnidade.setSelectedIndex(0);
        cbStatus.setSelectedIndex(0);
        btnSalvar.setText("💾 Salvar");
        editMode = false;
        produtoEditId = null;
    }
    
    private void cancelarEdicao() {
        limparFormulario();
    }
    
    private void sincronizarEstoque() {
        // TODO: Implementar sincronização com estoque
        JOptionPane.showMessageDialog(frame, 
            "📡 Sincronização de estoque em desenvolvimento.\n" +
            "Esta função irá sincronizar produtos com o sistema de estoque.", 
            "Informação", JOptionPane.INFORMATION_MESSAGE);
    }
    
    private void carregarDadosExemplo() {
        // Limpar tabela
        tableModel.setRowCount(0);
        
        // Dados de exemplo
        Object[][] dados = {
            {1L, "Notebook Dell Inspiron", "Informática", new BigDecimal("3500.00"), 15, "UN", "Ativo"},
            {2L, "Mouse Wireless Logitech", "Informática", new BigDecimal("89.90"), 45, "UN", "Ativo"},
            {3L, "Cadeira Executiva", "Escritório", new BigDecimal("450.00"), 8, "UN", "Ativo"},
            {4L, "Impressora HP LaserJet", "Informática", new BigDecimal("1200.00"), 5, "UN", "Ativo"},
            {5L, "Papel A4 Resma", "Escritório", new BigDecimal("25.50"), 200, "UN", "Ativo"},
            {6L, "Teclado Mecânico", "Informática", new BigDecimal("180.00"), 22, "UN", "Ativo"},
            {7L, "Monitor LG 24\"", "Informática", new BigDecimal("890.00"), 12, "UN", "Ativo"},
            {8L, "Mesa de Reunião", "Mobiliário", new BigDecimal("1200.00"), 3, "UN", "Inativo"},
            {9L, "Ar Condicionado 12k BTU", "Climatização", new BigDecimal("2800.00"), 6, "UN", "Ativo"},
            {10L, "Café Pilão 1kg", "Alimentação", new BigDecimal("18.90"), 50, "UN", "Ativo"}
        };
        
        // Adicionar dados na tabela
        for (Object[] linha : dados) {
            tableModel.addRow(linha);
        }
    }
    
    // Renderer para moeda
    private class CurrencyRenderer extends DefaultTableCellRenderer {
        private final DecimalFormat format = new DecimalFormat("R$ #,##0.00");
        
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value,
                boolean isSelected, boolean hasFocus, int row, int column) {
            
            if (value == null) {
                value = BigDecimal.ZERO;
            }
            
            setText(format.format(value));
            setHorizontalAlignment(JLabel.RIGHT);
            
            if (isSelected) {
                setBackground(table.getSelectionBackground());
                setForeground(table.getSelectionForeground());
            } else {
                setBackground(table.getBackground());
                setForeground(table.getForeground());
            }
            
            return this;
        }
    }
    
    // ==================== MÉTODOS PÚBLICOS ====================
    
    public void show() {
        if (frame == null) {
            initializeUI();
        }
        frame.setVisible(true);
        frame.toFront();
    }
    
    public void dispose() {
        if (frame != null) {
            frame.dispose();
            frame = null;
        }
    }
}
