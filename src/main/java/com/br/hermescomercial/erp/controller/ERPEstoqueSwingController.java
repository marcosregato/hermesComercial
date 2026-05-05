package com.br.hermescomercial.erp.controller;

import com.br.hermescomercial.ui.layout.LayoutPadrao;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

/**
 * Controller para tela de gestão de estoque do ERP
 * Versão 2.3.0 - Arquitetura Modular - Tema Padrão Hermes
 */
public class ERPEstoqueSwingController {
    
    private JFrame frame;
    private JPanel mainPanel;
    private JTable estoqueTable;
    private DefaultTableModel tableModel;
    private JTextField txtBusca;
    private JComboBox<String> cbTipoBusca;
    
    public ERPEstoqueSwingController() {
        inicializarUI();
    }
    
    private void inicializarUI() {
        frame = new JFrame("📦 Gestão de Estoque - ERP");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setSize(1200, 800);
        frame.setLocationRelativeTo(null);
        
        // Aplicar tema padrão
        frame.getContentPane().setBackground(LayoutPadrao.COR_FUNDO_ESCURO);
        
        // Criar painéis de formulário e tabela
        JPanel formularioPanel = createFormPanel();
        JPanel tabelaPanel = createTablePanel();
        
        // Usando layout padrão Header → Busca → Formulário → Tabela
        mainPanel = LayoutPadrao.criarLayoutPadraoGestao(
            false, // isPDV (false para ERP)
            "📦 Gestão de Estoque - ERP",
            "Buscar produtos por código, nome ou categoria...",
            formularioPanel,
            tabelaPanel
        );
        
        frame.add(mainPanel);
        
        carregarDadosExemplo();
    }
    
    private JPanel createFormPanel() {
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setOpaque(false);
        formPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.WEST;
        
        // Campo de busca
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 0.0;
        formPanel.add(new JLabel("🔍 Busca:"), gbc);
        
        gbc.gridx = 1;
        gbc.weightx = 1.0;
        txtBusca = new JTextField(20);
        txtBusca.setFont(LayoutPadrao.FONTE_CAMPO);
        formPanel.add(txtBusca, gbc);
        
        // Tipo de busca
        gbc.gridx = 2;
        gbc.weightx = 0.0;
        formPanel.add(new JLabel("Tipo:"), gbc);
        
        gbc.gridx = 3;
        cbTipoBusca = new JComboBox<>(new String[]{"Todos", "Nome", "Código", "Categoria"});
        cbTipoBusca.setFont(LayoutPadrao.FONTE_CAMPO);
        formPanel.add(cbTipoBusca, gbc);
        
        // Botões de ação
        gbc.gridx = 4;
        gbc.weightx = 0.0;
        JButton btnBuscar = LayoutPadrao.criarBotaoPrimario("🔍 Buscar");
        btnBuscar.addActionListener(e -> buscarProdutos());
        formPanel.add(btnBuscar, gbc);
        
        gbc.gridx = 5;
        JButton btnLimpar = LayoutPadrao.criarBotaoSecundario("🧹 Limpar");
        btnLimpar.addActionListener(e -> limparBusca());
        formPanel.add(btnLimpar, gbc);
        
        // Botões de operação
        gbc.gridx = 6;
        JButton btnAdicionar = LayoutPadrao.criarBotaoSucesso("➕ Adicionar");
        btnAdicionar.addActionListener(e -> adicionarProduto());
        formPanel.add(btnAdicionar, gbc);
        
        gbc.gridx = 7;
        JButton btnEditar = LayoutPadrao.criarBotaoPrimario("✏️ Editar");
        btnEditar.addActionListener(e -> editarProduto());
        formPanel.add(btnEditar, gbc);
        
        gbc.gridx = 8;
        JButton btnExcluir = LayoutPadrao.criarBotaoPerigo("🗑️ Excluir");
        btnExcluir.addActionListener(e -> excluirProduto());
        formPanel.add(btnExcluir, gbc);
        
        return formPanel;
    }
    
    private JPanel createTablePanel() {
        JPanel tablePanel = new JPanel(new BorderLayout());
        tablePanel.setOpaque(false);
        
        // Configurar tabela
        String[] colunas = {
            "Código", "Produto", "Categoria", "Estoque", "Mínimo", 
            "Máximo", "Localização", "Status", "Valor Unit.", "Valor Total"
        };
        
        tableModel = new DefaultTableModel(colunas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        estoqueTable = new JTable(tableModel);
        estoqueTable.setFont(LayoutPadrao.FONTE_TEXTO);
        estoqueTable.getTableHeader().setFont(LayoutPadrao.FONTE_SUBTITULO);
        estoqueTable.setRowHeight(25);
        estoqueTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        
        // Scroll pane
        JScrollPane scrollPane = new JScrollPane(estoqueTable);
        scrollPane.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        
        tablePanel.add(scrollPane, BorderLayout.CENTER);
        
        // Painel de estatísticas
        JPanel statsPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        statsPanel.setOpaque(false);
        
        JLabel lblTotal = new JLabel("📊 Total: 0 produtos");
        lblTotal.setFont(LayoutPadrao.FONTE_SUBTITULO);
        lblTotal.setForeground(LayoutPadrao.COR_TEXTO);
        statsPanel.add(lblTotal);
        
        JLabel lblEstoqueBaixo = new JLabel("⚠️ Estoque baixo: 0");
        lblEstoqueBaixo.setFont(LayoutPadrao.FONTE_SUBTITULO);
        lblEstoqueBaixo.setForeground(new Color(255, 165, 0));
        statsPanel.add(lblEstoqueBaixo);
        
        JLabel lblSemEstoque = new JLabel("❌ Sem estoque: 0");
        lblSemEstoque.setFont(LayoutPadrao.FONTE_SUBTITULO);
        lblSemEstoque.setForeground(new Color(220, 53, 69));
        statsPanel.add(lblSemEstoque);
        
        tablePanel.add(statsPanel, BorderLayout.NORTH);
        
        return tablePanel;
    }
    
    private void carregarDadosExemplo() {
        // Limpar tabela
        tableModel.setRowCount(0);
        
        // Adicionar dados de exemplo
        Object[][] dados = {
            {"001", "Notebook Dell Inspiron", "Informática", 15, 5, 50, "A-01-01", "✅ Normal", "3.500,00", "52.500,00"},
            {"002", "Mouse USB Logitech", "Informática", 45, 10, 100, "A-01-02", "✅ Normal", "89,90", "4.045,50"},
            {"003", "Cadeira Executiva", "Móveis", 3, 5, 20, "B-02-01", "⚠️ Baixo", "450,00", "1.350,00"},
            {"004", "Impressora HP", "Informática", 0, 2, 10, "A-02-01", "❌ Esgotado", "899,00", "0,00"},
            {"005", "Mesa de Escritório", "Móveis", 8, 3, 15, "B-02-02", "✅ Normal", "320,00", "2.560,00"}
        };
        
        for (Object[] row : dados) {
            tableModel.addRow(row);
        }
        
        atualizarEstatisticas();
    }
    
    private void buscarProdutos() {
        String termo = txtBusca.getText().trim().toLowerCase();
        String tipo = (String) cbTipoBusca.getSelectedItem();
        
        if (termo.isEmpty()) {
            carregarDadosExemplo();
            return;
        }
        
        // Implementar lógica de busca
        JOptionPane.showMessageDialog(frame, 
            "🔍 Busca por: " + termo + "\nTipo: " + tipo, 
            "Busca de Produtos", 
            JOptionPane.INFORMATION_MESSAGE);
    }
    
    private void limparBusca() {
        txtBusca.setText("");
        cbTipoBusca.setSelectedIndex(0);
        carregarDadosExemplo();
    }
    
    private void adicionarProduto() {
        JOptionPane.showMessageDialog(frame, 
            "➕ Adicionar Produto\n\n" +
            "Funcionalidade em desenvolvimento.\n" +
            "Permitirá adicionar novos produtos ao estoque.", 
            "Adicionar Produto", 
            JOptionPane.INFORMATION_MESSAGE);
    }
    
    private void editarProduto() {
        int linhaSelecionada = estoqueTable.getSelectedRow();
        if (linhaSelecionada == -1) {
            JOptionPane.showMessageDialog(frame, 
                "⚠️ Selecione um produto para editar!", 
                "Editar Produto", 
                JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        String produto = (String) tableModel.getValueAt(linhaSelecionada, 1);
        JOptionPane.showMessageDialog(frame, 
            "✏️ Editar Produto: " + produto + "\n\n" +
            "Funcionalidade em desenvolvimento.\n" +
            "Permitirá editar dados do produto selecionado.", 
            "Editar Produto", 
            JOptionPane.INFORMATION_MESSAGE);
    }
    
    private void excluirProduto() {
        int linhaSelecionada = estoqueTable.getSelectedRow();
        if (linhaSelecionada == -1) {
            JOptionPane.showMessageDialog(frame, 
                "⚠️ Selecione um produto para excluir!", 
                "Excluir Produto", 
                JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        String produto = (String) tableModel.getValueAt(linhaSelecionada, 1);
        int confirmacao = JOptionPane.showConfirmDialog(frame, 
            "🗑️ Confirmar Exclusão\n\n" +
            "Deseja realmente excluir o produto:\n" + produto + "?", 
            "Excluir Produto", 
            JOptionPane.YES_NO_OPTION, 
            JOptionPane.QUESTION_MESSAGE);
        
        if (confirmacao == JOptionPane.YES_OPTION) {
            tableModel.removeRow(linhaSelecionada);
            atualizarEstatisticas();
            JOptionPane.showMessageDialog(frame, 
                "✅ Produto excluído com sucesso!", 
                "Sucesso", 
                JOptionPane.INFORMATION_MESSAGE);
        }
    }
    
    private void atualizarEstatisticas() {
        int total = tableModel.getRowCount();
        
        for (int i = 0; i < total; i++) {
            // Contar estatísticas se necessário
        }
        
        // Atualizar labels (implementar se necessário)
    }
}
