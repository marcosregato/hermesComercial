package com.br.hermescomercial.erp.controller;

import com.br.hermescomercial.ui.layout.LayoutPadrao;
import com.br.hermescomercial.ui.layout.MenuColors;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.math.BigDecimal;

/**
 * Controller para tela de Contas a Receber no ERP
 * Versão 3.0.0 - Interface com headers coloridos
 */
public class ERPContasReceberSwingController {
    
    private JFrame frame;
    
    public JFrame getFrame() {
        return frame;
    }
    private JPanel mainPanel;
    private JTable contasTable;
    private DefaultTableModel tableModel;
    private JTextField txtCliente;
    private JComboBox<String> cbStatus, cbCategoria;
    private JButton btnNovo, btnEditar, btnExcluir, btnFiltrar, btnReceber;
    
    public ERPContasReceberSwingController() {
        initializeUI();
        carregarDadosExemplo();
    }
    
    private void initializeUI() {
        frame = new JFrame("💵 Contas a Receber - ERP");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setSize(1200, 800);
        frame.setLocationRelativeTo(null);
        
        frame.getContentPane().setBackground(LayoutPadrao.COR_FUNDO_ESCURO);
        
        createMainPanel();
        frame.add(mainPanel);
        frame.setVisible(true);
    }
    
    private void createMainPanel() {
        mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(LayoutPadrao.COR_FUNDO_ESCURO);
        mainPanel.setBorder(new EmptyBorder(20, 20, 20, 20));
        
        // Header colorido
        mainPanel.add(createHeaderPanel(), BorderLayout.NORTH);
        
        // Painel central
        JPanel centerPanel = new JPanel(new BorderLayout());
        centerPanel.setOpaque(false);
        
        // Painel de filtros
        centerPanel.add(createFiltroPanel(), BorderLayout.NORTH);
        
        // Painel de resumo
        centerPanel.add(createResumoPanel(), BorderLayout.CENTER);
        
        // Tabela
        centerPanel.add(createTablePanel(), BorderLayout.SOUTH);
        
        mainPanel.add(centerPanel, BorderLayout.CENTER);
        
        // Painel de botões
        mainPanel.add(createButtonPanel(), BorderLayout.SOUTH);
    }
    
    private JPanel createHeaderPanel() {
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setOpaque(false);
        headerPanel.setBorder(new EmptyBorder(0, 0, 20, 0));
        
        // Header colorido para o setor financeiro
        JPanel headerColorido = MenuColors.criarHeaderSetor("financeiro", "💵 Contas a Receber");
        
        JButton btnVoltar = LayoutPadrao.criarBotaoSecundario("← Voltar");
        btnVoltar.addActionListener(e -> frame.dispose());
        
        headerPanel.add(btnVoltar, BorderLayout.WEST);
        headerPanel.add(headerColorido, BorderLayout.CENTER);
        
        return headerPanel;
    }
    
    private JPanel createFiltroPanel() {
        JPanel filtroPanel = new JPanel(new GridBagLayout());
        filtroPanel.setOpaque(false);
        filtroPanel.setBorder(BorderFactory.createTitledBorder("🔍 Filtros"));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
        // Linha 1
        gbc.gridx = 0; gbc.gridy = 0;
        filtroPanel.add(new JLabel("Cliente:"), gbc);
        
        gbc.gridx = 1;
        txtCliente = new JTextField(15);
        filtroPanel.add(txtCliente, gbc);
        
        gbc.gridx = 2;
        filtroPanel.add(new JLabel("Status:"), gbc);
        
        gbc.gridx = 3;
        cbStatus = new JComboBox<>(new String[]{"Todos", "Pendente", "Recebido", "Atrasado", "Cancelado"});
        filtroPanel.add(cbStatus, gbc);
        
        gbc.gridx = 4;
        filtroPanel.add(new JLabel("Categoria:"), gbc);
        
        gbc.gridx = 5;
        cbCategoria = new JComboBox<>(new String[]{"Todas", "Vendas", "Serviços", "Aluguéis", "Outros"});
        filtroPanel.add(cbCategoria, gbc);
        
        gbc.gridx = 6;
        btnFiltrar = LayoutPadrao.criarBotaoPrimario("🔍 Filtrar");
        btnFiltrar.addActionListener(e -> filtrarContas());
        filtroPanel.add(btnFiltrar, gbc);
        
        return filtroPanel;
    }
    
    private JPanel createResumoPanel() {
        JPanel resumoPanel = new JPanel(new GridLayout(1, 3, 10, 10));
        resumoPanel.setOpaque(false);
        resumoPanel.setBorder(new EmptyBorder(10, 0, 10, 0));
        
        // Total a Receber
        JPanel panelReceber = criarPainelResumo("Total a Receber", "R$ 125.680,00", LayoutPadrao.COR_SUCESSO);
        resumoPanel.add(panelReceber);
        
        // Total Recebido
        JPanel panelRecebido = criarPainelResumo("Total Recebido (Mês)", "R$ 87.450,00", LayoutPadrao.COR_PRIMARIA);
        resumoPanel.add(panelRecebido);
        
        // Total em Atraso
        JPanel panelAtraso = criarPainelResumo("Total em Atraso", "R$ 23.120,00", LayoutPadrao.COR_PERIGO);
        resumoPanel.add(panelAtraso);
        
        return resumoPanel;
    }
    
    private JPanel criarPainelResumo(String titulo, String valor, Color cor) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(LayoutPadrao.COR_FUNDO_ESCURO);
        panel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(cor, 2),
            BorderFactory.createEmptyBorder(15, 15, 15, 15)
        ));
        
        JLabel lblTitulo = new JLabel(titulo);
        lblTitulo.setFont(LayoutPadrao.FONTE_SUBTITULO);
        lblTitulo.setForeground(LayoutPadrao.COR_TEXTO_CLARO);
        
        JLabel lblValor = new JLabel(valor);
        lblValor.setFont(new Font("Arial", Font.BOLD, 18));
        lblValor.setForeground(cor);
        lblValor.setHorizontalAlignment(SwingConstants.CENTER);
        
        panel.add(lblTitulo, BorderLayout.NORTH);
        panel.add(lblValor, BorderLayout.CENTER);
        
        return panel;
    }
    
    private JPanel createTablePanel() {
        JPanel tablePanel = new JPanel(new BorderLayout());
        tablePanel.setOpaque(false);
        tablePanel.setBorder(BorderFactory.createTitledBorder("📋 Contas a Receber"));
        
        // Modelo da tabela
        String[] colunas = {"ID", "Cliente", "Descrição", "Valor", "Vencimento", "Status", "Categoria"};
        tableModel = new DefaultTableModel(colunas, 0) {
            @Override
            public Class<?> getColumnClass(int columnIndex) {
                return String.class;
            }
            
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        contasTable = new JTable(tableModel);
        contasTable.setFont(LayoutPadrao.FONTE_TEXTO);
        contasTable.setRowHeight(25);
        contasTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        contasTable.getTableHeader().setFont(LayoutPadrao.FONTE_SUBTITULO);
        
        // Scroll pane
        JScrollPane scrollPane = new JScrollPane(contasTable);
        scrollPane.setPreferredSize(new Dimension(0, 300));
        
        tablePanel.add(scrollPane, BorderLayout.CENTER);
        
        return tablePanel;
    }
    
    private JPanel createButtonPanel() {
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.setOpaque(false);
        buttonPanel.setBorder(new EmptyBorder(10, 0, 0, 0));
        
        btnNovo = LayoutPadrao.criarBotaoPrimario("➕ Nova Conta");
        btnNovo.addActionListener(e -> novaConta());
        
        btnEditar = LayoutPadrao.criarBotaoSecundario("✏️ Editar");
        btnEditar.addActionListener(e -> editarConta());
        
        btnReceber = LayoutPadrao.criarBotaoSucesso("💰 Receber");
        btnReceber.addActionListener(e -> receberConta());
        
        btnExcluir = LayoutPadrao.criarBotaoPerigo("🗑️ Excluir");
        btnExcluir.addActionListener(e -> excluirConta());
        
        buttonPanel.add(btnNovo);
        buttonPanel.add(btnEditar);
        buttonPanel.add(btnReceber);
        buttonPanel.add(btnExcluir);
        
        return buttonPanel;
    }
    
    private void carregarDadosExemplo() {
        // Adicionar dados de exemplo
        Object[][] dados = {
            {1, "Cliente A", "Venda de produtos", new BigDecimal("8500.00"), "15/05/2024", "Pendente", "Vendas"},
            {2, "Cliente B", "Serviço de consultoria", new BigDecimal("3200.00"), "10/05/2024", "Recebido", "Serviços"},
            {3, "Cliente C", "Aluguel imóvel", new BigDecimal("2500.00"), "05/05/2024", "Atrasado", "Aluguéis"},
            {4, "Cliente D", "Projeto personalizado", new BigDecimal("12000.00"), "20/05/2024", "Pendente", "Serviços"},
            {5, "Cliente E", "Venda equipamentos", new BigDecimal("6800.00"), "25/05/2024", "Pendente", "Vendas"}
        };
        
        for (Object[] linha : dados) {
            Object[] dadosFormatados = {
                linha[0], // ID
                linha[1], // Cliente
                linha[2], // Descrição
                String.format("R$ %.2f", linha[3]), // Valor formatado
                linha[4], // Vencimento
                linha[5], // Status
                linha[6]  // Categoria
            };
            tableModel.addRow(dadosFormatados);
        }
    }
    
    private void novaConta() {
        JOptionPane.showMessageDialog(frame, "Funcionalidade de nova conta em desenvolvimento", 
            "Informação", JOptionPane.INFORMATION_MESSAGE);
    }
    
    private void editarConta() {
        int linhaSelecionada = contasTable.getSelectedRow();
        if (linhaSelecionada == -1) {
            JOptionPane.showMessageDialog(frame, "Selecione uma conta para editar", 
                "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }
        JOptionPane.showMessageDialog(frame, "Funcionalidade de edição em desenvolvimento", 
            "Informação", JOptionPane.INFORMATION_MESSAGE);
    }
    
    private void receberConta() {
        int linhaSelecionada = contasTable.getSelectedRow();
        if (linhaSelecionada == -1) {
            JOptionPane.showMessageDialog(frame, "Selecione uma conta para receber", 
                "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        String status = (String) tableModel.getValueAt(linhaSelecionada, 5);
        if ("Recebido".equals(status)) {
            JOptionPane.showMessageDialog(frame, "Esta conta já foi recebida", 
                "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        int resultado = JOptionPane.showConfirmDialog(frame, 
            "Confirmar recebimento desta conta?", "Confirmar Recebimento", 
            JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
        
        if (resultado == JOptionPane.YES_OPTION) {
            tableModel.setValueAt("Recebido", linhaSelecionada, 5);
            JOptionPane.showMessageDialog(frame, "Conta recebida com sucesso!", 
                "Sucesso", JOptionPane.INFORMATION_MESSAGE);
        }
    }
    
    private void excluirConta() {
        int linhaSelecionada = contasTable.getSelectedRow();
        if (linhaSelecionada == -1) {
            JOptionPane.showMessageDialog(frame, "Selecione uma conta para excluir", 
                "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        int resultado = JOptionPane.showConfirmDialog(frame, 
            "Deseja realmente excluir esta conta?", "Confirmar Exclusão", 
            JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
        
        if (resultado == JOptionPane.YES_OPTION) {
            tableModel.removeRow(linhaSelecionada);
            JOptionPane.showMessageDialog(frame, "Conta excluída com sucesso!", 
                "Sucesso", JOptionPane.INFORMATION_MESSAGE);
        }
    }
    
    private void filtrarContas() {
        JOptionPane.showMessageDialog(frame, "Funcionalidade de filtro em desenvolvimento", 
            "Informação", JOptionPane.INFORMATION_MESSAGE);
    }
    
    public void show() {
        frame.setVisible(true);
    }
    
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new ERPContasReceberSwingController();
        });
    }
}
