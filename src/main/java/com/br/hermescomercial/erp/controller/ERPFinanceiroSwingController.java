package com.br.hermescomercial.erp.controller;

import com.br.hermescomercial.ui.layout.LayoutPadrao;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.Date;

/**
 * Controller para tela de gestão financeira no ERP
 * Versão 2.3.0 - Arquitetura Modular
 */
public class ERPFinanceiroSwingController {
    
    private JFrame frame;
    private DecimalFormat df;
    private JTable lancamentosTable;
    private DefaultTableModel tableModel;
    private JTextField txtBusca, txtDescricao, txtValor;
    private JComboBox<String> cbTipo, cbCategoria;
    private JButton btnNovo, btnEditar, btnExcluir, btnSalvar, btnCancelar;
    
    public ERPFinanceiroSwingController() {
        df = new DecimalFormat("#,##0.00");
        initializeUI();
    }
    
    private void initializeUI() {
        frame = new JFrame("💰 Gestão Financeira - ERP");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setSize(1200, 800);
        frame.setLocationRelativeTo(null);
        
        // Aplicar tema moderno
        frame.getContentPane().setBackground(LayoutPadrao.COR_FUNDO_ESCURO);
        
        createMainPanel();
        frame.setVisible(true);
    }
    
    private void createMainPanel() {
        // Criar painéis de formulário e tabela
        JPanel formularioPanel = createFormPanel();
        JPanel tabelaPanel = createTablePanel();
        
        // Usando layout padrão Header → Busca → Formulário → Tabela
        JPanel mainPanel = LayoutPadrao.criarLayoutPadraoGestao(
            false, // isPDV (false para ERP)
            "💰 Gestão Financeira - ERP",
            "Digite descrição, tipo ou categoria do lançamento...",
            formularioPanel,
            tabelaPanel
        );
        
        frame.add(mainPanel);
    }
    
    // Header agora é criado automaticamente pelo LayoutPadrao.criarLayoutPadraoGestao()
    
    
    private JPanel createFormPanel() {
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setOpaque(false);
        formPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.weightx = 0.0;
        
        // Campo de busca
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        formPanel.add(new JLabel("🔍 Busca:"), gbc);
        
        txtBusca = new JTextField(20);
        txtBusca.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        txtBusca.setPreferredSize(new Dimension(200, 30));
        gbc.gridx = 2;
        gbc.weightx = 1.0;
        formPanel.add(txtBusca, gbc);
        
        // Tipo de lançamento
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.weightx = 0.0;
        formPanel.add(new JLabel("Tipo:"), gbc);
        
        cbTipo = new JComboBox<>(new String[]{"ENTRADA", "SAIDA"});
        cbTipo.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        cbTipo.setPreferredSize(new Dimension(150, 30));
        gbc.gridx = 1;
        gbc.weightx = 1.0;
        formPanel.add(cbTipo, gbc);
        
        // Descrição
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.weightx = 0.0;
        formPanel.add(new JLabel("Descrição:"), gbc);
        
        txtDescricao = new JTextField(20);
        txtDescricao.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        txtDescricao.setPreferredSize(new Dimension(200, 30));
        gbc.gridx = 1;
        gbc.weightx = 1.0;
        formPanel.add(txtDescricao, gbc);
        
        // Valor
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.weightx = 0.0;
        formPanel.add(new JLabel("Valor (R$):"), gbc);
        
        txtValor = new JTextField(20);
        txtValor.setFont(new Font("Segoe UI", Font.BOLD, 14));
        txtValor.setPreferredSize(new Dimension(150, 30));
        gbc.gridx = 1;
        gbc.weightx = 1.0;
        formPanel.add(txtValor, gbc);
        
        // Categoria
        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.weightx = 0.0;
        formPanel.add(new JLabel("Categoria:"), gbc);
        
        cbCategoria = new JComboBox<>(new String[]{"Vendas", "Compras", "Despesas", "Salários", "Impostos", "Outros"});
        cbCategoria.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        cbCategoria.setPreferredSize(new Dimension(150, 30));
        gbc.gridx = 1;
        gbc.weightx = 1.0;
        formPanel.add(cbCategoria, gbc);
        
        // Botões
        gbc.gridx = 0;
        gbc.gridy = 5;
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.NONE;
        gbc.anchor = GridBagConstraints.CENTER;
        
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.setOpaque(false);
        
        btnNovo = LayoutPadrao.criarBotaoSucesso("➕ Novo");
        btnNovo.addActionListener(e -> limparFormulario());
        
        btnSalvar = LayoutPadrao.criarBotaoPrimario("💾 Salvar");
        btnSalvar.addActionListener(e -> salvarLancamento());
        
        btnCancelar = LayoutPadrao.criarBotaoPerigo("❌ Cancelar");
        btnCancelar.addActionListener(e -> limparFormulario());
        
        buttonPanel.add(btnNovo);
        buttonPanel.add(btnSalvar);
        buttonPanel.add(btnCancelar);
        
        formPanel.add(buttonPanel, gbc);
        
        return formPanel;
    }
    
    private JPanel createTablePanel() {
        JPanel tablePanel = new JPanel(new BorderLayout());
        tablePanel.setOpaque(false);
        tablePanel.setBorder(new EmptyBorder(10, 10, 10, 10));
        
        // Título da tabela
        JLabel titleLabel = new JLabel("📋 Lançamentos Financeiros");
        titleLabel.setFont(LayoutPadrao.FONTE_SUBTITULO);
        titleLabel.setForeground(LayoutPadrao.COR_PRIMARIA);
        
        // Botões de ação da tabela
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        buttonPanel.setOpaque(false);
        
        btnEditar = LayoutPadrao.criarBotaoAlerta("✏️ Editar");
        btnEditar.addActionListener(e -> editarLancamento());
        
        btnExcluir = LayoutPadrao.criarBotaoPerigo("🗑️ Excluir");
        btnExcluir.addActionListener(e -> excluirLancamento());
        
        buttonPanel.add(btnEditar);
        buttonPanel.add(btnExcluir);
        
        // Criar tabela
        String[] colunas = {"ID", "Data", "Tipo", "Descrição", "Categoria", "Valor", "Saldo"};
        tableModel = new DefaultTableModel(colunas, 0) {
            @Override
            public Class<?> getColumnClass(int columnIndex) {
                switch (columnIndex) {
                    case 0: return Long.class; // ID
                    case 1: return Date.class; // Data
                    case 2: return String.class; // Tipo
                    case 3: return String.class; // Descrição
                    case 4: return String.class; // Categoria
                    case 5: return BigDecimal.class; // Valor
                    case 6: return BigDecimal.class; // Saldo
                    default: return String.class;
                }
            }
            
            @Override
            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return false; // Tabela não editável diretamente
            }
        };
        
        lancamentosTable = new JTable(tableModel);
        lancamentosTable.setFont(LayoutPadrao.FONTE_TEXTO);
        lancamentosTable.setRowHeight(25);
        lancamentosTable.getTableHeader().setFont(LayoutPadrao.FONTE_TEXTO);
        lancamentosTable.getTableHeader().setBackground(LayoutPadrao.COR_PRIMARIA);
        lancamentosTable.getTableHeader().setForeground(Color.WHITE);
        
        // Adicionar dados de exemplo
        carregarDadosExemplo();
        
        // Evento de duplo clique para editar
        lancamentosTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    editarLancamento();
                }
            }
        });
        
        JScrollPane scrollPane = new JScrollPane(lancamentosTable);
        scrollPane.setPreferredSize(new Dimension(1150, 400));
        scrollPane.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(LayoutPadrao.COR_BORDA, 1),
            new EmptyBorder(5, 5, 5, 5)
        ));
        
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setOpaque(false);
        topPanel.add(titleLabel, BorderLayout.WEST);
        topPanel.add(buttonPanel, BorderLayout.EAST);
        
        tablePanel.add(topPanel, BorderLayout.NORTH);
        tablePanel.add(scrollPane, BorderLayout.CENTER);
        
        return tablePanel;
    }
    
    
    private void carregarDadosExemplo() {
        // Limpar tabela
        tableModel.setRowCount(0);
        
        // Dados de exemplo
        Object[][] dados = {
            {1L, new Date(), "ENTRADA", "Venda de produtos", "Vendas", new BigDecimal("18500.00"), new BigDecimal("18500.00")},
            {2L, new Date(), "SAIDA", "Pagamento de fornecedor", "Compras", new BigDecimal("-2500.00"), new BigDecimal("16000.00")},
            {3L, new Date(), "ENTRADA", "Recebimento de cliente", "Vendas", new BigDecimal("5000.00"), new BigDecimal("21000.00")},
            {4L, new Date(), "SAIDA", "Aluguel do escritório", "Despesas", new BigDecimal("-3500.00"), new BigDecimal("17500.00")},
            {5L, new Date(), "ENTRADA", "Venda de serviços", "Vendas", new BigDecimal("3200.00"), new BigDecimal("20700.00")},
            {6L, new Date(), "SAIDA", "Pagamento de salários", "Despesas", new BigDecimal("-8000.00"), new BigDecimal("12700.00")},
            {7L, new Date(), "ENTRADA", "Recebimento de juros", "Financeiro", new BigDecimal("450.00"), new BigDecimal("13150.00")},
            {8L, new Date(), "SAIDA", "Pagamento de impostos", "Despesas", new BigDecimal("-1200.00"), new BigDecimal("11950.00")}
        };
        
        // Formatar valores
        DecimalFormat df = new DecimalFormat("#,##0.00");
        
        // Adicionar dados na tabela
        for (Object[] linha : dados) {
            Object[] linhaFormatada = linha.clone();
            linhaFormatada[5] = df.format(linha[5]); // Valor
            linhaFormatada[6] = df.format(linha[6]); // Saldo
            tableModel.addRow(linhaFormatada);
        }
    }
    
    // ==================== MÉTODOS DE AÇÃO ====================
    
    private void limparFormulario() {
        txtBusca.setText("");
        txtDescricao.setText("");
        txtValor.setText("");
        cbTipo.setSelectedIndex(0);
        cbCategoria.setSelectedIndex(0);
        btnSalvar.setText("💾 Salvar");
        btnCancelar.setText("❌ Cancelar");
    }
    
    private void salvarLancamento() {
        String tipo = (String) cbTipo.getSelectedItem();
        String descricao = txtDescricao.getText().trim();
        String valorStr = txtValor.getText().trim();
        String categoria = (String) cbCategoria.getSelectedItem();
        
        if (descricao.isEmpty()) {
            JOptionPane.showMessageDialog(frame, 
                "⚠️ Descrição é obrigatória!", 
                "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        if (valorStr.isEmpty()) {
            JOptionPane.showMessageDialog(frame, 
                "⚠️ Valor é obrigatório!", 
                "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        try {
            BigDecimal valor = new BigDecimal(valorStr.replace(",", "."));
            
                JOptionPane.showMessageDialog(frame, 
                "✅ Lançamento salvo com sucesso!\n" +
                "Tipo: " + tipo + "\n" +
                "Descrição: " + descricao + "\n" +
                "Valor: R$ " + df.format(valor) + "\n" +
                "Categoria: " + categoria + "\n" +
                "Funcionalidade em desenvolvimento.", 
                "Sucesso", JOptionPane.INFORMATION_MESSAGE);
            
            limparFormulario();
            carregarDadosExemplo(); // Recarregar dados
            
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(frame, 
                "⚠️ Valor inválido!\n" +
                "Digite um valor numérico válido.", 
                "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void editarLancamento() {
        int linhaSelecionada = lancamentosTable.getSelectedRow();
        
        if (linhaSelecionada == -1) {
            JOptionPane.showMessageDialog(frame, 
                "⚠️ Nenhum lançamento selecionado!\n" +
                "Selecione um lançamento na tabela para editar.", 
                "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        Long id = (Long) tableModel.getValueAt(linhaSelecionada, 0);
        String tipo = (String) tableModel.getValueAt(linhaSelecionada, 2);
        String descricao = (String) tableModel.getValueAt(linhaSelecionada, 3);
        BigDecimal valor = (BigDecimal) tableModel.getValueAt(linhaSelecionada, 5);
        String categoria = (String) tableModel.getValueAt(linhaSelecionada, 4);
        
        // Preencher formulário
        txtBusca.setText(id.toString());
        cbTipo.setSelectedItem(tipo);
        txtDescricao.setText(descricao);
        txtValor.setText(valor.toString());
        cbCategoria.setSelectedItem(categoria);
        
        btnSalvar.setText("💾 Atualizar");
        btnCancelar.setText("❌ Cancelar Edição");
    }
    
    private void excluirLancamento() {
        int linhaSelecionada = lancamentosTable.getSelectedRow();
        
        if (linhaSelecionada == -1) {
            JOptionPane.showMessageDialog(frame, 
                "⚠️ Nenhum lançamento selecionado!\n" +
                "Selecione um lançamento na tabela para excluir.", 
                "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        String descricao = (String) tableModel.getValueAt(linhaSelecionada, 3);
        
        int confirmacao = JOptionPane.showConfirmDialog(frame, 
            "🗑️ Confirmar Exclusão\n" +
            "Deseja realmente excluir o lançamento selecionado?\n\n" +
            "Descrição: " + descricao, 
            "Confirmar Exclusão", 
            JOptionPane.YES_NO_OPTION, 
            JOptionPane.QUESTION_MESSAGE);
        
        if (confirmacao == JOptionPane.YES_OPTION) {
            try {
                        tableModel.removeRow(linhaSelecionada);
                
                JOptionPane.showMessageDialog(frame, 
                "✅ Lançamento excluído com sucesso!\n" +
                "Funcionalidade em desenvolvimento.", 
                "Sucesso", JOptionPane.INFORMATION_MESSAGE);
            } catch (Exception e) {
                JOptionPane.showMessageDialog(frame, 
                    "❌ Erro ao excluir lançamento: " + e.getMessage(), 
                    "Erro", JOptionPane.ERROR_MESSAGE);
            }
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
