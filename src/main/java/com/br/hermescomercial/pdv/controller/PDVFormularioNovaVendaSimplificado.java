package com.br.hermescomercial.pdv.controller;

import com.br.hermescomercial.pdv.model.ItemVenda;
import com.br.hermescomercial.pdv.service.VendaManager;
import com.br.hermescomercial.util.SystemLogger;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Formulário simplificado de Nova Venda
 * Estrutura: Header → Busca → Formulário → Tabela
 * @author Hermes Comercial
 * @version 3.2.0
 */
public class PDVFormularioNovaVendaSimplificado {
    
    private JPanel workArea;
    private String usuarioAtual;
    private String nomeUsuario;
    private VendaManager vendaManager;
    
    // Componentes principais
    private JTextField txtCliente;
    private JTextField txtCPF;
    private JTextField txtProduto;
    private JTextField txtQuantidade;
    private JTextField txtValorUnitario;
    private JTextField txtDesconto;
    private JComboBox<String> cbFormaPagamento;
    private JTextArea txtObservacoes;
    
    // Labels de resumo
    private JLabel lblTotalItens;
    private JLabel lblSubtotal;
    private JLabel lblTotal;
    private JLabel lblTroco;
    
    // Tabela
    private JTable tabelaItens;
    private DefaultTableModel modelTabela;
    
    // Cores
    private static final Color WHITE = Color.WHITE;
    private static final Color ACCENT_COLOR = new Color(52, 152, 219);
    private static final Color SUCCESS_COLOR = new Color(39, 174, 96);
    private static final Color GRAY = new Color(149, 165, 166);
    
    public PDVFormularioNovaVendaSimplificado(JPanel workArea, String usuarioAtual, String nomeUsuario) {
        this.workArea = workArea;
        this.usuarioAtual = usuarioAtual;
        this.nomeUsuario = nomeUsuario;
        this.vendaManager = new VendaManager();
    }
    
    /**
     * Cria o formulário completo de Nova Venda
     */
    public JPanel criarFormularioNovaVenda() {
        try {
            SystemLogger.ui("=== CRIANDO FORMULÁRIO NOVA VENDA SIMPLIFICADO ===");
            SystemLogger.ui("Usuário: " + usuarioAtual);
            
            JPanel painelPrincipal = new JPanel(new BorderLayout());
            painelPrincipal.setBackground(WHITE);
            
            // Header
            JPanel headerPanel = criarHeader();
            painelPrincipal.add(headerPanel, BorderLayout.NORTH);
            
            // Painel central com busca, formulário e tabela
            JPanel painelCentral = new JPanel(new BorderLayout());
            painelCentral.setBackground(WHITE);
            
            // Painel de busca
            JPanel buscaPanel = criarPainelBusca();
            painelCentral.add(buscaPanel, BorderLayout.NORTH);
            
            // Painel do formulário
            JPanel formularioPanel = criarPainelFormulario();
            painelCentral.add(formularioPanel, BorderLayout.CENTER);
            
            // Tabela de itens
            JPanel tabelaPanel = criarPainelTabela();
            painelCentral.add(tabelaPanel, BorderLayout.SOUTH);
            
            painelPrincipal.add(painelCentral, BorderLayout.CENTER);
            
            SystemLogger.ui("Formulário Nova Venda simplificado criado com sucesso");
            return painelPrincipal;
            
        } catch (Exception e) {
            SystemLogger.error("Erro ao criar formulário Nova Venda", e);
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
        
        JLabel titleLabel = new JLabel("📋 Nova Venda");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        titleLabel.setForeground(WHITE);
        
        JPanel infoPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        infoPanel.setOpaque(false);
        
        JLabel usuarioLabel = new JLabel("👤 " + nomeUsuario);
        usuarioLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        usuarioLabel.setForeground(WHITE);
        
        JLabel dataLabel = new JLabel("📅 " + new SimpleDateFormat("dd/MM/yyyy HH:mm").format(new Date()));
        dataLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        dataLabel.setForeground(WHITE);
        
        infoPanel.add(usuarioLabel);
        infoPanel.add(Box.createHorizontalStrut(20));
        infoPanel.add(dataLabel);
        
        headerPanel.add(titleLabel, BorderLayout.WEST);
        headerPanel.add(infoPanel, BorderLayout.EAST);
        
        return headerPanel;
    }
    
    /**
     * Cria o painel de busca
     */
    private JPanel criarPainelBusca() {
        JPanel buscaPanel = new JPanel(new BorderLayout());
        buscaPanel.setBackground(new Color(245, 245, 245));
        buscaPanel.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));
        
        JLabel buscaLabel = new JLabel("🔍 Busca Rápida");
        buscaLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        buscaLabel.setForeground(ACCENT_COLOR);
        
        JTextField txtBusca = new JTextField();
        txtBusca.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        txtBusca.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(GRAY),
            BorderFactory.createEmptyBorder(8, 12, 8, 12)
        ));
        txtBusca.setToolTipText("Digite para buscar produtos, clientes ou CPF");
        
        txtBusca.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    realizarBusca(txtBusca.getText().trim());
                }
            }
        });
        
        buscaPanel.add(buscaLabel, BorderLayout.NORTH);
        buscaPanel.add(txtBusca, BorderLayout.CENTER);
        
        return buscaPanel;
    }
    
    /**
     * Cria o painel do formulário principal
     */
    private JPanel criarPainelFormulario() {
        JPanel formularioPanel = new JPanel(new GridBagLayout());
        formularioPanel.setBackground(WHITE);
        formularioPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
        // Seção Cliente
        JLabel clienteSectionLabel = new JLabel("👤 Dados do Cliente");
        clienteSectionLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        clienteSectionLabel.setForeground(ACCENT_COLOR);
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 4;
        formularioPanel.add(clienteSectionLabel, gbc);
        
        gbc.gridy = 1; gbc.gridwidth = 1;
        JLabel lblCliente = new JLabel("Cliente:");
        lblCliente.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        formularioPanel.add(lblCliente, gbc);
        
        gbc.gridx = 1; gbc.weightx = 1.0;
        txtCliente = new JTextField();
        txtCliente.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        txtCliente.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(GRAY),
            BorderFactory.createEmptyBorder(5, 8, 5, 8)
        ));
        formularioPanel.add(txtCliente, gbc);
        
        gbc.gridx = 2; gbc.weightx = 0.0;
        JLabel lblCPF = new JLabel("CPF:");
        lblCPF.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        formularioPanel.add(lblCPF, gbc);
        
        gbc.gridx = 3; gbc.weightx = 0.5;
        txtCPF = new JTextField();
        txtCPF.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        txtCPF.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(GRAY),
            BorderFactory.createEmptyBorder(5, 8, 5, 8)
        ));
        formularioPanel.add(txtCPF, gbc);
        
        // Seção Produto
        gbc.gridy = 2; gbc.gridx = 0; gbc.gridwidth = 4; gbc.weightx = 0.0;
        JLabel produtoSectionLabel = new JLabel("📦 Dados do Produto");
        produtoSectionLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        produtoSectionLabel.setForeground(ACCENT_COLOR);
        formularioPanel.add(produtoSectionLabel, gbc);
        
        gbc.gridy = 3; gbc.gridwidth = 1;
        JLabel lblProduto = new JLabel("Produto:");
        lblProduto.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        formularioPanel.add(lblProduto, gbc);
        
        gbc.gridx = 1; gbc.weightx = 1.0;
        txtProduto = new JTextField();
        txtProduto.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        txtProduto.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(GRAY),
            BorderFactory.createEmptyBorder(5, 8, 5, 8)
        ));
        formularioPanel.add(txtProduto, gbc);
        
        gbc.gridx = 2; gbc.weightx = 0.0;
        JLabel lblQuantidade = new JLabel("Qtd:");
        lblQuantidade.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        formularioPanel.add(lblQuantidade, gbc);
        
        gbc.gridx = 3; gbc.weightx = 0.3;
        txtQuantidade = new JTextField("1");
        txtQuantidade.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        txtQuantidade.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(GRAY),
            BorderFactory.createEmptyBorder(5, 8, 5, 8)
        ));
        txtQuantidade.setHorizontalAlignment(JTextField.RIGHT);
        formularioPanel.add(txtQuantidade, gbc);
        
        gbc.gridy = 4; gbc.gridx = 0; gbc.gridwidth = 1; gbc.weightx = 0.0;
        JLabel lblValorUnitario = new JLabel("Valor Unit.:");
        lblValorUnitario.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        formularioPanel.add(lblValorUnitario, gbc);
        
        gbc.gridx = 1; gbc.weightx = 0.5;
        txtValorUnitario = new JTextField();
        txtValorUnitario.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        txtValorUnitario.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(GRAY),
            BorderFactory.createEmptyBorder(5, 8, 5, 8)
        ));
        txtValorUnitario.setHorizontalAlignment(JTextField.RIGHT);
        formularioPanel.add(txtValorUnitario, gbc);
        
        gbc.gridx = 2; gbc.weightx = 0.0;
        JLabel lblDesconto = new JLabel("Desconto %:");
        lblDesconto.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        formularioPanel.add(lblDesconto, gbc);
        
        gbc.gridx = 3; gbc.weightx = 0.3;
        txtDesconto = new JTextField("0");
        txtDesconto.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        txtDesconto.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(GRAY),
            BorderFactory.createEmptyBorder(5, 8, 5, 8)
        ));
        txtDesconto.setHorizontalAlignment(JTextField.RIGHT);
        formularioPanel.add(txtDesconto, gbc);
        
        // Forma de pagamento
        gbc.gridy = 5; gbc.gridx = 0; gbc.gridwidth = 1; gbc.weightx = 0.0;
        JLabel lblFormaPagamento = new JLabel("Forma Pagamento:");
        lblFormaPagamento.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        formularioPanel.add(lblFormaPagamento, gbc);
        
        gbc.gridx = 1; gbc.weightx = 0.5;
        String[] formasPagamento = {"Dinheiro", "Cartão Crédito", "Cartão Débito", "PIX", "Outros"};
        cbFormaPagamento = new JComboBox<>(formasPagamento);
        cbFormaPagamento.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        cbFormaPagamento.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(GRAY),
            BorderFactory.createEmptyBorder(5, 8, 5, 8)
        ));
        formularioPanel.add(cbFormaPagamento, gbc);
        
        // Botões de ação
        gbc.gridx = 2; gbc.gridwidth = 2; gbc.weightx = 0.0;
        JPanel botoesPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        botoesPanel.setBackground(WHITE);
        
        JButton btnAdicionar = new JButton("➕ Adicionar Item");
        btnAdicionar.setBackground(SUCCESS_COLOR);
        btnAdicionar.setForeground(WHITE);
        btnAdicionar.setFocusPainted(false);
        btnAdicionar.setBorderPainted(false);
        btnAdicionar.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btnAdicionar.addActionListener(e -> adicionarItem());
        
        JButton btnLimpar = new JButton("🗑️ Limpar");
        btnLimpar.setBackground(GRAY);
        btnLimpar.setForeground(WHITE);
        btnLimpar.setFocusPainted(false);
        btnLimpar.setBorderPainted(false);
        btnLimpar.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btnLimpar.addActionListener(e -> limparCampos());
        
        botoesPanel.add(btnAdicionar);
        botoesPanel.add(Box.createHorizontalStrut(10));
        botoesPanel.add(btnLimpar);
        
        formularioPanel.add(botoesPanel, gbc);
        
        // Observações
        gbc.gridy = 6; gbc.gridx = 0; gbc.gridwidth = 4; gbc.weightx = 0.0;
        JLabel lblObservacoes = new JLabel("📝 Observações:");
        lblObservacoes.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        formularioPanel.add(lblObservacoes, gbc);
        
        gbc.gridy = 7; gbc.gridx = 0; gbc.gridwidth = 4; gbc.weightx = 1.0;
        txtObservacoes = new JTextArea(2, 4);
        txtObservacoes.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        txtObservacoes.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(GRAY),
            BorderFactory.createEmptyBorder(5, 8, 5, 8)
        ));
        txtObservacoes.setToolTipText("Observações sobre a venda");
        formularioPanel.add(new JScrollPane(txtObservacoes), gbc);
        
        // Resumo
        gbc.gridy = 8; gbc.gridx = 0; gbc.gridwidth = 4; gbc.weightx = 0.0;
        JPanel resumoPanel = new JPanel(new GridLayout(4, 2, 10, 5));
        resumoPanel.setBackground(new Color(245, 245, 245));
        resumoPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        
        resumoPanel.add(new JLabel("Total Itens:"));
        lblTotalItens = new JLabel("0");
        lblTotalItens.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lblTotalItens.setHorizontalAlignment(JLabel.RIGHT);
        resumoPanel.add(lblTotalItens);
        
        resumoPanel.add(new JLabel("Subtotal:"));
        lblSubtotal = new JLabel("R$ 0,00");
        lblSubtotal.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lblSubtotal.setHorizontalAlignment(JLabel.RIGHT);
        resumoPanel.add(lblSubtotal);
        
        resumoPanel.add(new JLabel("TOTAL:"));
        lblTotal = new JLabel("R$ 0,00");
        lblTotal.setFont(new Font("Segoe UI", Font.BOLD, 18));
        lblTotal.setForeground(SUCCESS_COLOR);
        lblTotal.setHorizontalAlignment(JLabel.RIGHT);
        resumoPanel.add(lblTotal);
        
        resumoPanel.add(new JLabel("Troco:"));
        lblTroco = new JLabel("R$ 0,00");
        lblTroco.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lblTroco.setForeground(GRAY);
        lblTroco.setHorizontalAlignment(JLabel.RIGHT);
        resumoPanel.add(lblTroco);
        
        formularioPanel.add(resumoPanel, gbc);
        
        // Listeners
        txtQuantidade.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                atualizarResumo();
            }
        });
        
        txtValorUnitario.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                atualizarResumo();
            }
        });
        
        txtDesconto.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                atualizarResumo();
            }
        });
        
        cbFormaPagamento.addActionListener(e -> atualizarTroco());
        
        return formularioPanel;
    }
    
    /**
     * Cria o painel da tabela de itens
     */
    private JPanel criarPainelTabela() {
        JPanel tabelaPanel = new JPanel(new BorderLayout());
        tabelaPanel.setBackground(WHITE);
        tabelaPanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 20, 20));
        
        JLabel tabelaLabel = new JLabel("📋 Itens da Venda");
        tabelaLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        tabelaLabel.setForeground(ACCENT_COLOR);
        
        // Modelo da tabela
        String[] colunas = {"Código", "Produto", "Descrição", "Qtd", "Valor Unit.", "Desconto %", "Subtotal", "Forma Pagamento", "Observações"};
        modelTabela = new DefaultTableModel(colunas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        tabelaItens = new JTable(modelTabela);
        tabelaItens.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        tabelaItens.setRowHeight(25);
        tabelaItens.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 12));
        tabelaItens.getTableHeader().setBackground(ACCENT_COLOR);
        tabelaItens.getTableHeader().setForeground(WHITE);
        
        JScrollPane scrollPane = new JScrollPane(tabelaItens);
        scrollPane.setPreferredSize(new Dimension(0, 200));
        scrollPane.setBorder(BorderFactory.createLineBorder(GRAY));
        
        JPanel botoesTabelaPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        botoesTabelaPanel.setBackground(WHITE);
        
        JButton btnFinalizar = new JButton("💰 Finalizar Venda");
        btnFinalizar.setBackground(SUCCESS_COLOR);
        btnFinalizar.setForeground(WHITE);
        btnFinalizar.setFocusPainted(false);
        btnFinalizar.setBorderPainted(false);
        btnFinalizar.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btnFinalizar.addActionListener(e -> finalizarVenda());
        
        JButton btnCancelar = new JButton("❌ Cancelar Venda");
        btnCancelar.setBackground(Color.RED);
        btnCancelar.setForeground(WHITE);
        btnCancelar.setFocusPainted(false);
        btnCancelar.setBorderPainted(false);
        btnCancelar.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btnCancelar.addActionListener(e -> cancelarVenda());
        
        botoesTabelaPanel.add(btnFinalizar);
        botoesTabelaPanel.add(Box.createHorizontalStrut(10));
        botoesTabelaPanel.add(btnCancelar);
        
        tabelaPanel.add(tabelaLabel, BorderLayout.NORTH);
        tabelaPanel.add(scrollPane, BorderLayout.CENTER);
        tabelaPanel.add(botoesTabelaPanel, BorderLayout.SOUTH);
        
        return tabelaPanel;
    }
    
    /**
     * Adiciona um item à venda
     */
    private void adicionarItem() {
        try {
            String produto = txtProduto.getText().trim();
            if (produto.isEmpty()) {
                JOptionPane.showMessageDialog(workArea, "Informe o nome do produto!", "Aviso", JOptionPane.WARNING_MESSAGE);
                return;
            }
            
            int quantidade = Integer.parseInt(txtQuantidade.getText().trim());
            if (quantidade <= 0) {
                JOptionPane.showMessageDialog(workArea, "Quantidade deve ser maior que zero!", "Aviso", JOptionPane.WARNING_MESSAGE);
                return;
            }
            
            ItemVenda item = new ItemVenda();
            item.setCodigo(String.valueOf(modelTabela.getRowCount() + 1));
            item.setProduto(produto);
            item.setQuantidade(quantidade);
            item.setFormaPagamento((String) cbFormaPagamento.getSelectedItem());
            item.setObservacoes(txtObservacoes.getText().trim());
            
            try {
                item.setValorUnitario(new java.math.BigDecimal(txtValorUnitario.getText().replace(",", ".")));
                item.setDescontoPercentual(new java.math.BigDecimal(txtDesconto.getText().replace(",", ".")));
                item.calcularValorTotal();
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(workArea, "Valores inválidos!", "Erro", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            vendaManager.adicionarItem(item);
            modelTabela.addRow(item.toArray());
            
            limparCamposItem();
            atualizarResumo();
            
            SystemLogger.ui("Item adicionado: " + item.toString());
            
        } catch (Exception e) {
            SystemLogger.error("Erro ao adicionar item", e);
            JOptionPane.showMessageDialog(workArea, "Erro ao adicionar item: " + e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    /**
     * Limpa os campos do item
     */
    private void limparCamposItem() {
        txtProduto.setText("");
        txtQuantidade.setText("1");
        txtValorUnitario.setText("");
        txtDesconto.setText("0");
        txtProduto.requestFocus();
    }
    
    /**
     * Limpa todos os campos do formulário
     */
    private void limparCampos() {
        txtCliente.setText("");
        txtCPF.setText("");
        txtProduto.setText("");
        txtQuantidade.setText("1");
        txtValorUnitario.setText("");
        txtDesconto.setText("0");
        cbFormaPagamento.setSelectedIndex(0);
        txtObservacoes.setText("");
        txtProduto.requestFocus();
    }
    
    /**
     * Atualiza o resumo da venda
     */
    private void atualizarResumo() {
        vendaManager.setCliente(txtCliente.getText().trim());
        vendaManager.setCpf(txtCPF.getText().trim());
        vendaManager.setObservacoes(txtObservacoes.getText().trim());
        
        lblTotalItens.setText(String.valueOf(vendaManager.getTotalItens()));
        lblSubtotal.setText("R$ " + formatarValor(vendaManager.getSubtotal()));
        lblTotal.setText("R$ " + formatarValor(vendaManager.getTotal()));
    }
    
    /**
     * Atualiza o campo de troco
     */
    private void atualizarTroco() {
        String formaPagamento = (String) cbFormaPagamento.getSelectedItem();
        if ("Dinheiro".equals(formaPagamento)) {
            lblTroco.setText("R$ 0,00");
        } else {
            lblTroco.setText("-");
        }
    }
    
    /**
     * Realiza busca de produtos ou clientes
     */
    private void realizarBusca(String termo) {
        if (termo.isEmpty()) {
            JOptionPane.showMessageDialog(workArea, "Digite um termo para buscar!", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        JOptionPane.showMessageDialog(workArea, "Busca por: " + termo + "\n(Implementar busca no banco)", "Busca", JOptionPane.INFORMATION_MESSAGE);
    }
    
    /**
     * Finaliza a venda
     */
    private void finalizarVenda() {
        if (vendaManager.isVendaVazia()) {
            JOptionPane.showMessageDialog(workArea, "Adicione itens à venda!", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        if (txtCliente.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(workArea, "Informe o nome do cliente!", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        vendaManager.setFormaPagamento((String) cbFormaPagamento.getSelectedItem());
        
        JOptionPane.showMessageDialog(workArea, 
            "Venda finalizada com sucesso!\n\n" + vendaManager.getResumo(), 
            "Venda Concluída", 
            JOptionPane.INFORMATION_MESSAGE);
        
        cancelarVenda();
    }
    
    /**
     * Cancela a venda atual
     */
    private void cancelarVenda() {
        vendaManager.limparItens();
        modelTabela.setRowCount(0);
        limparCampos();
        atualizarResumo();
        
        SystemLogger.ui("Venda cancelada e limpa");
    }
    
    /**
     * Formata valor monetário
     */
    private String formatarValor(java.math.BigDecimal valor) {
        if (valor == null) return "0,00";
        return valor.setScale(2, java.math.RoundingMode.HALF_UP).toString().replace(".", ",");
    }
    
    /**
     * Cria painel de erro
     */
    private JPanel criarPainelErro() {
        JPanel painelErro = new JPanel(new BorderLayout());
        painelErro.setBackground(Color.WHITE);
        
        JLabel erroLabel = new JLabel("❌ Erro ao carregar formulário", JLabel.CENTER);
        erroLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        erroLabel.setForeground(Color.RED);
        
        painelErro.add(erroLabel, BorderLayout.CENTER);
        return painelErro;
    }
}
