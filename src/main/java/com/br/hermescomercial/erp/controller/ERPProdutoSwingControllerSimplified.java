package com.br.hermescomercial.erp.controller;

import com.br.hermescomercial.ui.layout.LayoutPadrao;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.logging.Logger;
import java.util.logging.Level;

/**
 * Controller para tela de gestão de produtos do ERP - Versão Simplificada
 * Focado em corrigir erros de compilação e funcionalidade básica
 */
public class ERPProdutoSwingControllerSimplified {
    
    private static final Logger logger = Logger.getLogger(ERPProdutoSwingControllerSimplified.class.getName());
    
    // UI Components
    private JFrame frame;
    private JPanel mainPanel;
    private JTable produtosTable;
    private DefaultTableModel tableModel;
    private JTextField txtNome, txtCodigo, txtDescricao, txtPreco, txtEstoque, txtCategoria;
    private JButton btnSalvar, btnEditar, btnExcluir, btnCancelar, btnAtualizar;
    private JComboBox<String> cbUnidade, cbStatus;
    private JLabel lblStatus, lblTotalProdutos;
    
    // Estado do controller
    private boolean editMode = false;
    private Long produtoEditId = null;
    
    public ERPProdutoSwingControllerSimplified() {
        initializeUI();
    }
    
    /**
     * Inicializa a interface do usuário
     */
    private void initializeUI() {
        frame = new JFrame("📦 Gestão de Produtos - ERP v2.0");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setSize(1200, 800);
        frame.setLocationRelativeTo(null);
        
        // Aplicar tema padrão
        frame.getContentPane().setBackground(LayoutPadrao.COR_FUNDO_ESCURO);
        
        // Criar painéis
        JPanel formularioPanel = createFormPanel();
        JPanel tabelaPanel = createTablePanel();
        
        // Layout padrão
        mainPanel = LayoutPadrao.criarLayoutPadraoGestao(
            false, // isPDV
            "📦 Gestão de Produtos - ERP",
            "Digite nome, código ou categoria do produto...",
            formularioPanel,
            tabelaPanel
        );
        
        frame.add(mainPanel);
        
        carregarDadosExemplo();
        frame.setVisible(true);
    }
    
    /**
     * Cria painel de formulário
     */
    private JPanel createFormPanel() {
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setOpaque(false);
        formPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.WEST;
        
        // Código
        gbc.gridx = 0; gbc.gridy = 0; gbc.weightx = 0.0;
        formPanel.add(createLabel("Código:"), gbc);
        
        gbc.gridx = 1; gbc.weightx = 0.2;
        txtCodigo = createModernTextField(10);
        txtCodigo.setEditable(false);
        formPanel.add(txtCodigo, gbc);
        
        // Nome
        gbc.gridx = 2; gbc.weightx = 0.0;
        formPanel.add(createLabel("Nome:"), gbc);
        
        gbc.gridx = 3; gbc.weightx = 1.0;
        txtNome = createModernTextField(30);
        formPanel.add(txtNome, gbc);
        
        // Categoria
        gbc.gridx = 0; gbc.gridy = 1; gbc.weightx = 0.0;
        formPanel.add(createLabel("Categoria:"), gbc);
        
        gbc.gridx = 1; gbc.weightx = 0.5;
        txtCategoria = createModernTextField(15);
        formPanel.add(txtCategoria, gbc);
        
        // Unidade
        gbc.gridx = 2; gbc.weightx = 0.0;
        formPanel.add(createLabel("Unidade:"), gbc);
        
        gbc.gridx = 3; gbc.weightx = 0.3;
        cbUnidade = new JComboBox<>(new String[]{"UN", "KG", "LT", "CX", "DZ"});
        cbUnidade.setFont(LayoutPadrao.FONTE_CAMPO);
        formPanel.add(cbUnidade, gbc);
        
        // Preço
        gbc.gridx = 0; gbc.gridy = 2; gbc.weightx = 0.0;
        formPanel.add(createLabel("Preço:"), gbc);
        
        gbc.gridx = 1; gbc.weightx = 0.5;
        txtPreco = createModernTextField(15);
        formPanel.add(txtPreco, gbc);
        
        // Estoque
        gbc.gridx = 2; gbc.weightx = 0.0;
        formPanel.add(createLabel("Estoque:"), gbc);
        
        gbc.gridx = 3; gbc.weightx = 0.3;
        txtEstoque = createModernTextField(10);
        formPanel.add(txtEstoque, gbc);
        
        // Descrição
        gbc.gridx = 0; gbc.gridy = 3; gbc.weightx = 0.0;
        formPanel.add(createLabel("Descrição:"), gbc);
        
        gbc.gridx = 1; gbc.gridwidth = 3; gbc.weightx = 1.0;
        txtDescricao = createModernTextField(50);
        formPanel.add(txtDescricao, gbc);
        
        // Status
        gbc.gridx = 0; gbc.gridy = 4; gbc.gridwidth = 1; gbc.weightx = 0.0;
        formPanel.add(createLabel("Status:"), gbc);
        
        gbc.gridx = 1; gbc.weightx = 0.5;
        cbStatus = new JComboBox<>(new String[]{"Ativo", "Inativo", "Descontinuado"});
        cbStatus.setFont(LayoutPadrao.FONTE_CAMPO);
        formPanel.add(cbStatus, gbc);
        
        // Painel de botões
        gbc.gridx = 0; gbc.gridy = 5; gbc.gridwidth = 4;
        JPanel buttonPanel = createButtonPanel();
        formPanel.add(buttonPanel, gbc);
        
        // Painel de status
        gbc.gridy = 6;
        JPanel statusPanel = createStatusPanel();
        formPanel.add(statusPanel, gbc);
        
        return formPanel;
    }
    
    /**
     * Cria painel de botões
     */
    private JPanel createButtonPanel() {
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.setOpaque(false);
        
        btnSalvar = LayoutPadrao.criarBotaoSucesso("💾 Salvar");
        btnSalvar.addActionListener(this::onSalvar);
        buttonPanel.add(btnSalvar);
        
        btnEditar = LayoutPadrao.criarBotaoPrimario("✏️ Editar");
        btnEditar.addActionListener(this::onEditar);
        buttonPanel.add(btnEditar);
        
        btnExcluir = LayoutPadrao.criarBotaoPerigo("🗑️ Excluir");
        btnExcluir.addActionListener(this::onExcluir);
        buttonPanel.add(btnExcluir);
        
        btnCancelar = LayoutPadrao.criarBotaoSecundario("❌ Cancelar");
        btnCancelar.addActionListener(this::onCancelar);
        buttonPanel.add(btnCancelar);
        
        btnAtualizar = LayoutPadrao.criarBotaoSecundario("🔄 Atualizar");
        btnAtualizar.addActionListener(this::onAtualizar);
        buttonPanel.add(btnAtualizar);
        
        return buttonPanel;
    }
    
    /**
     * Cria painel de status
     */
    private JPanel createStatusPanel() {
        JPanel statusPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        statusPanel.setOpaque(false);
        
        lblStatus = new JLabel("🟢 Pronto");
        lblStatus.setFont(LayoutPadrao.FONTE_TEXTO);
        lblStatus.setForeground(new Color(40, 167, 69));
        statusPanel.add(lblStatus);
        
        lblTotalProdutos = new JLabel("📊 Total: 0");
        lblTotalProdutos.setFont(LayoutPadrao.FONTE_TEXTO);
        lblTotalProdutos.setForeground(LayoutPadrao.COR_TEXTO);
        statusPanel.add(lblTotalProdutos);
        
        return statusPanel;
    }
    
    /**
     * Cria painel de tabela
     */
    private JPanel createTablePanel() {
        JPanel tablePanel = new JPanel(new BorderLayout());
        tablePanel.setOpaque(false);
        
        // Configurar tabela
        String[] colunas = {"ID", "Código", "Nome", "Categoria", "Preço", "Estoque", "Unidade", "Status"};
        
        tableModel = new DefaultTableModel(colunas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        produtosTable = new JTable(tableModel);
        produtosTable.setFont(LayoutPadrao.FONTE_TEXTO);
        produtosTable.getTableHeader().setFont(LayoutPadrao.FONTE_SUBTITULO);
        produtosTable.setRowHeight(25);
        produtosTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        
        // Ajustar colunas
        ajustarLarguraColunas();
        
        JScrollPane scrollPane = new JScrollPane(produtosTable);
        scrollPane.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        
        tablePanel.add(scrollPane, BorderLayout.CENTER);
        
        return tablePanel;
    }
    
    /**
     * Ajusta largura das colunas da tabela
     */
    private void ajustarLarguraColunas() {
        produtosTable.getColumnModel().getColumn(0).setPreferredWidth(50);  // ID
        produtosTable.getColumnModel().getColumn(1).setPreferredWidth(80);  // Código
        produtosTable.getColumnModel().getColumn(2).setPreferredWidth(200); // Nome
        produtosTable.getColumnModel().getColumn(3).setPreferredWidth(120); // Categoria
        produtosTable.getColumnModel().getColumn(4).setPreferredWidth(100); // Preço
        produtosTable.getColumnModel().getColumn(5).setPreferredWidth(80);  // Estoque
        produtosTable.getColumnModel().getColumn(6).setPreferredWidth(60);  // Unidade
        produtosTable.getColumnModel().getColumn(7).setPreferredWidth(80);  // Status
    }
    
    /**
     * Event handlers
     */
    private void onSalvar(ActionEvent e) {
        try {
            String nome = txtNome.getText().trim();
            String categoria = txtCategoria.getText().trim();
            String precoStr = txtPreco.getText().trim();
            String estoqueStr = txtEstoque.getText().trim();
            
            // Validação básica
            if (nome.isEmpty()) {
                showMessage("Nome do produto é obrigatório", "Aviso", JOptionPane.WARNING_MESSAGE);
                return;
            }
            
            if (categoria.isEmpty()) {
                showMessage("Categoria do produto é obrigatória", "Aviso", JOptionPane.WARNING_MESSAGE);
                return;
            }
            
            if (precoStr.isEmpty()) {
                showMessage("Preço do produto é obrigatório", "Aviso", JOptionPane.WARNING_MESSAGE);
                return;
            }
            
            // Converter dados
            double preco = Double.parseDouble(precoStr.replace("R$", "").replace(",", "."));
            int estoque = Integer.parseInt(estoqueStr);
            
            if (editMode) {
                // Atualizar produto existente
                int linha = produtosTable.getSelectedRow();
                if (linha >= 0) {
                    tableModel.setValueAt(txtCodigo.getText(), linha, 1);
                    tableModel.setValueAt(nome, linha, 2);
                    tableModel.setValueAt(categoria, linha, 3);
                    tableModel.setValueAt(formatarMoeda(preco), linha, 4);
                    tableModel.setValueAt(estoque, linha, 5);
                    tableModel.setValueAt(cbUnidade.getSelectedItem(), linha, 6);
                    tableModel.setValueAt(cbStatus.getSelectedItem(), linha, 7);
                    
                    showMessage("Produto atualizado com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
                }
            } else {
                // Adicionar novo produto
                Long novoId = System.currentTimeMillis();
                Object[] novaLinha = {
                    novoId, txtCodigo.getText(), nome, categoria, formatarMoeda(preco), 
                    estoque, cbUnidade.getSelectedItem(), cbStatus.getSelectedItem()
                };
                tableModel.addRow(novaLinha);
                
                showMessage("Produto salvo com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
            }
            
            limparFormulario();
            atualizarEstatisticas();
            
        } catch (NumberFormatException ex) {
            showMessage("Valores inválidos. Verifique preço e estoque.", "Erro", JOptionPane.ERROR_MESSAGE);
        } catch (Exception ex) {
            logger.log(Level.SEVERE, "Erro ao salvar produto", ex);
            showMessage("Erro ao salvar produto: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void onEditar(ActionEvent e) {
        int linhaSelecionada = produtosTable.getSelectedRow();
        if (linhaSelecionada == -1) {
            showMessage("Selecione um produto para editar", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        // Preencher formulário com dados selecionados
        produtoEditId = (Long) tableModel.getValueAt(linhaSelecionada, 0);
        txtCodigo.setText((String) tableModel.getValueAt(linhaSelecionada, 1));
        txtNome.setText((String) tableModel.getValueAt(linhaSelecionada, 2));
        txtCategoria.setText((String) tableModel.getValueAt(linhaSelecionada, 3));
        txtPreco.setText(tableModel.getValueAt(linhaSelecionada, 4).toString());
        txtEstoque.setText(tableModel.getValueAt(linhaSelecionada, 5).toString());
        cbUnidade.setSelectedItem((String) tableModel.getValueAt(linhaSelecionada, 6));
        cbStatus.setSelectedItem((String) tableModel.getValueAt(linhaSelecionada, 7));
        
        editMode = true;
        btnSalvar.setText("💾 Atualizar");
        txtNome.requestFocus();
    }
    
    private void onExcluir(ActionEvent e) {
        int linhaSelecionada = produtosTable.getSelectedRow();
        if (linhaSelecionada == -1) {
            showMessage("Selecione um produto para excluir", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        String nome = (String) tableModel.getValueAt(linhaSelecionada, 2);
        
        int confirmacao = JOptionPane.showConfirmDialog(frame,
            "Deseja realmente excluir o produto?\n\n" + nome,
            "Confirmar Exclusão",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.QUESTION_MESSAGE);
        
        if (confirmacao == JOptionPane.YES_OPTION) {
            tableModel.removeRow(linhaSelecionada);
            showMessage("Produto excluído com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
            atualizarEstatisticas();
        }
    }
    
    private void onCancelar(ActionEvent e) {
        limparFormulario();
    }
    
    private void onAtualizar(ActionEvent e) {
        carregarDadosExemplo();
    }
    
    /**
     * Carrega dados de exemplo
     */
    private void carregarDadosExemplo() {
        tableModel.setRowCount(0);
        
        Object[][] dados = {
            {1L, "001", "Notebook Dell Inspiron", "Informática", "R$ 3.500,00", 15, "UN", "Ativo"},
            {2L, "002", "Mouse Wireless Logitech", "Informática", "R$ 89,90", 45, "UN", "Ativo"},
            {3L, "003", "Cadeira Executiva", "Escritório", "R$ 450,00", 8, "UN", "Ativo"},
            {4L, "004", "Impressora HP LaserJet", "Informática", "R$ 1.200,00", 5, "UN", "Ativo"},
            {5L, "005", "Mesa de Escritório", "Escritório", "R$ 320,00", 12, "UN", "Ativo"}
        };
        
        for (Object[] row : dados) {
            tableModel.addRow(row);
        }
        
        atualizarEstatisticas();
    }
    
    /**
     * Limpa formulário
     */
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
    
    /**
     * Atualiza estatísticas
     */
    private void atualizarEstatisticas() {
        lblTotalProdutos.setText("📊 Total: " + tableModel.getRowCount());
    }
    
    /**
     * Mostra mensagem ao usuário
     */
    private void showMessage(String message, String title, int messageType) {
        JOptionPane.showMessageDialog(frame, message, title, messageType);
    }
    
    // Métodos utilitários de UI
    private JLabel createLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(LayoutPadrao.FONTE_ROTULO);
        label.setForeground(LayoutPadrao.COR_TEXTO);
        return label;
    }
    
    private JTextField createModernTextField(int columns) {
        JTextField field = new JTextField(columns);
        field.setFont(LayoutPadrao.FONTE_CAMPO);
        field.setForeground(LayoutPadrao.COR_TEXTO);
        field.setBackground(Color.WHITE);
        field.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(LayoutPadrao.COR_BORDA, 1),
            BorderFactory.createEmptyBorder(5, 5, 5, 5)
        ));
        return field;
    }
    
    private String formatarMoeda(double valor) {
        return String.format("R$ %.2f", valor);
    }
    
    /**
     * Exibe a janela
     */
    public void show() {
        frame.setVisible(true);
    }
    
    /**
     * Obtém o frame
     */
    public JFrame getFrame() {
        return frame;
    }
    
    /**
     * Dispose do controller
     */
    public void dispose() {
        if (frame != null) {
            frame.dispose();
        }
    }
}
