package com.br.hermescomercial.pdv.controller;

import com.br.hermescomercial.util.SystemLogger;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Classe especializada para o formulário de Nova Venda
 * Estrutura: Header → Busca → Formulário → Tabela
 */
public class PDVFormularioNovaVenda {
    
    private JPanel workArea;
    private String usuarioAtual;
    private String nomeUsuario;
    
    // Componentes principais
    private JTextField txtCliente;
    private JTextField txtCPF;
    private JTextField txtProduto;
    private JTextField txtQuantidade;
    private JTextField txtValorUnitario;
    private JTextField txtDesconto;
    private JLabel lblTotal;
    private JLabel lblSubtotal;
    private JLabel lblTotalItens;
    
    // Tabela de itens
    private JTable tabelaItens;
    private DefaultTableModel modelTabela;
    private List<ItemVenda> itensVenda;
    
    // Cores
    private static final Color WHITE = Color.WHITE;
    private static final Color ACCENT_COLOR = new Color(52, 152, 219);
    private static final Color SUCCESS_COLOR = new Color(39, 174, 96);
    private static final Color WARNING_COLOR = new Color(241, 196, 15);
    private static final Color DANGER_COLOR = new Color(231, 76, 60);
    private static final Color GRAY = new Color(149, 165, 166);
    
    public PDVFormularioNovaVenda(JPanel workArea, String usuarioAtual, String nomeUsuario) {
        this.workArea = workArea;
        this.usuarioAtual = usuarioAtual;
        this.nomeUsuario = nomeUsuario;
        this.itensVenda = new ArrayList<>();
    }
    
    /**
     * Cria o formulário completo de Nova Venda
     */
    public JPanel criarFormularioNovaVenda() {
        try {
            SystemLogger.ui("=== CRIANDO FORMULÁRIO NOVA VENDA ===");
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
            
            SystemLogger.ui("Formulário Nova Venda criado com sucesso");
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
        
        // Título
        JLabel titleLabel = new JLabel("📋 Nova Venda");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        titleLabel.setForeground(WHITE);
        
        // Informações
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
        
        // Enter para buscar
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
        
        // Botões de ação
        gbc.gridy = 5; gbc.gridx = 0; gbc.gridwidth = 4; gbc.weightx = 0.0;
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
        
        // Resumo
        gbc.gridy = 6; gbc.gridx = 0; gbc.gridwidth = 4;
        JPanel resumoPanel = new JPanel(new GridLayout(3, 2, 10, 5));
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
        
        formularioPanel.add(resumoPanel, gbc);
        
        // Listeners para cálculo automático
        txtQuantidade.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                calcularTotais();
            }
        });
        
        txtValorUnitario.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                calcularTotais();
            }
        });
        
        txtDesconto.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                calcularTotais();
            }
        });
        
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
        String[] colunas = {"Código", "Produto", "Qtd", "Valor Unit.", "Desconto %", "Total"};
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
        
        // Scroll pane
        JScrollPane scrollPane = new JScrollPane(tabelaItens);
        scrollPane.setPreferredSize(new Dimension(0, 200));
        scrollPane.setBorder(BorderFactory.createLineBorder(GRAY));
        
        // Painel de botões da tabela
        JPanel botoesTabelaPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        botoesTabelaPanel.setBackground(WHITE);
        
        JButton btnRemover = new JButton("❌ Remover Item");
        btnRemover.setBackground(DANGER_COLOR);
        btnRemover.setForeground(WHITE);
        btnRemover.setFocusPainted(false);
        btnRemover.setBorderPainted(false);
        btnRemover.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btnRemover.addActionListener(e -> removerItemSelecionado());
        
        JButton btnFinalizar = new JButton("💰 Finalizar Venda");
        btnFinalizar.setBackground(SUCCESS_COLOR);
        btnFinalizar.setForeground(WHITE);
        btnFinalizar.setFocusPainted(false);
        btnFinalizar.setBorderPainted(false);
        btnFinalizar.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnFinalizar.addActionListener(e -> finalizarVenda());
        
        botoesTabelaPanel.add(btnRemover);
        botoesTabelaPanel.add(Box.createHorizontalStrut(10));
        botoesTabelaPanel.add(btnFinalizar);
        
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
                JOptionPane.showMessageDialog(workArea, "Digite o nome do produto!", "Aviso", JOptionPane.WARNING_MESSAGE);
                return;
            }
            
            int quantidade = Integer.parseInt(txtQuantidade.getText().trim());
            if (quantidade <= 0) {
                JOptionPane.showMessageDialog(workArea, "Quantidade deve ser maior que zero!", "Aviso", JOptionPane.WARNING_MESSAGE);
                return;
            }
            
            BigDecimal valorUnitario = new BigDecimal(txtValorUnitario.getText().trim().replace(",", "."));
            if (valorUnitario.compareTo(BigDecimal.ZERO) <= 0) {
                JOptionPane.showMessageDialog(workArea, "Valor unitário deve ser maior que zero!", "Aviso", JOptionPane.WARNING_MESSAGE);
                return;
            }
            
            BigDecimal desconto = new BigDecimal(txtDesconto.getText().trim().replace(",", "."));
            
            // Calcular total do item
            BigDecimal totalItem = valorUnitario.multiply(new BigDecimal(quantidade));
            BigDecimal valorDesconto = totalItem.multiply(desconto).divide(new BigDecimal(100), 2, RoundingMode.HALF_UP);
            BigDecimal totalComDesconto = totalItem.subtract(valorDesconto);
            
            // Adicionar à lista
            ItemVenda item = new ItemVenda(
                itensVenda.size() + 1,
                produto,
                quantidade,
                valorUnitario,
                desconto,
                totalComDesconto
            );
            itensVenda.add(item);
            
            // Adicionar à tabela
            Object[] rowData = {
                item.getCodigo(),
                item.getProduto(),
                item.getQuantidade(),
                "R$ " + formatarValor(item.getValorUnitario()),
                item.getDesconto() + "%",
                "R$ " + formatarValor(item.getTotal())
            };
            modelTabela.addRow(rowData);
            
            // Limpar campos do produto
            txtProduto.setText("");
            txtQuantidade.setText("1");
            txtValorUnitario.setText("");
            txtDesconto.setText("0");
            txtProduto.requestFocus();
            
            // Atualizar totais
            calcularTotais();
            
            SystemLogger.ui("Item adicionado: " + produto + " - Qtd: " + quantidade);
            
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(workArea, "Verifique os valores digitados!", "Erro", JOptionPane.ERROR_MESSAGE);
        } catch (Exception e) {
            SystemLogger.error("Erro ao adicionar item", e);
            JOptionPane.showMessageDialog(workArea, "Erro ao adicionar item: " + e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    /**
     * Remove o item selecionado da tabela
     */
    private void removerItemSelecionado() {
        int linhaSelecionada = tabelaItens.getSelectedRow();
        if (linhaSelecionada >= 0) {
            itensVenda.remove(linhaSelecionada);
            modelTabela.removeRow(linhaSelecionada);
            
            // Reenumerar códigos
            for (int i = 0; i < itensVenda.size(); i++) {
                itensVenda.get(i).setCodigo(i + 1);
                modelTabela.setValueAt(i + 1, i, 0);
            }
            
            calcularTotais();
            SystemLogger.ui("Item removido da venda");
        } else {
            JOptionPane.showMessageDialog(workArea, "Selecione um item para remover!", "Aviso", JOptionPane.WARNING_MESSAGE);
        }
    }
    
    /**
     * Calcula os totais da venda
     */
    private void calcularTotais() {
        try {
            BigDecimal subtotal = BigDecimal.ZERO;
            int totalItens = 0;
            
            for (ItemVenda item : itensVenda) {
                subtotal = subtotal.add(item.getTotal());
                totalItens += item.getQuantidade();
            }
            
            // Calcular totais do item atual (se estiver preenchido)
            if (!txtProduto.getText().trim().isEmpty()) {
                try {
                    int quantidade = Integer.parseInt(txtQuantidade.getText().trim());
                    BigDecimal valorUnitario = new BigDecimal(txtValorUnitario.getText().trim().replace(",", "."));
                    BigDecimal desconto = new BigDecimal(txtDesconto.getText().trim().replace(",", "."));
                    
                    BigDecimal totalItem = valorUnitario.multiply(new BigDecimal(quantidade));
                    BigDecimal valorDesconto = totalItem.multiply(desconto).divide(new BigDecimal(100), 2, RoundingMode.HALF_UP);
                    BigDecimal totalComDesconto = totalItem.subtract(valorDesconto);
                    
                    subtotal = subtotal.add(totalComDesconto);
                    totalItens += quantidade;
                } catch (Exception e) {
                    // Ignora erros de cálculo do item atual
                }
            }
            
            lblTotalItens.setText(String.valueOf(totalItens));
            lblSubtotal.setText("R$ " + formatarValor(subtotal));
            lblTotal.setText("R$ " + formatarValor(subtotal));
            
        } catch (Exception e) {
            SystemLogger.error("Erro ao calcular totais", e);
        }
    }
    
    /**
     * Limpa os campos do formulário
     */
    private void limparCampos() {
        txtProduto.setText("");
        txtQuantidade.setText("1");
        txtValorUnitario.setText("");
        txtDesconto.setText("0");
        txtProduto.requestFocus();
    }
    
    /**
     * Realiza busca de produtos ou clientes
     */
    private void realizarBusca(String termo) {
        if (termo.isEmpty()) {
            JOptionPane.showMessageDialog(workArea, "Digite um termo para buscar!", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        // TODO: Implementar lógica de busca no banco de dados
        JOptionPane.showMessageDialog(workArea, "Busca por: " + termo + "\n(Implementar busca no banco)", "Busca", JOptionPane.INFORMATION_MESSAGE);
    }
    
    /**
     * Finaliza a venda
     */
    private void finalizarVenda() {
        if (itensVenda.isEmpty()) {
            JOptionPane.showMessageDialog(workArea, "Adicione itens à venda!", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        if (txtCliente.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(workArea, "Informe o nome do cliente!", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        // TODO: Implementar lógica de salvar venda no banco de dados
        BigDecimal total = new BigDecimal(lblTotal.getText().replace("R$ ", "").replace(",", "."));
        
        int resultado = JOptionPane.showConfirmDialog(
            workArea,
            "Deseja finalizar a venda?\n\n" +
            "Cliente: " + txtCliente.getText().trim() + "\n" +
            "Total: " + lblTotal.getText() + "\n" +
            "Itens: " + itensVenda.size(),
            "Finalizar Venda",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.QUESTION_MESSAGE
        );
        
        if (resultado == JOptionPane.YES_OPTION) {
            // Limpar venda
            itensVenda.clear();
            modelTabela.setRowCount(0);
            txtCliente.setText("");
            txtCPF.setText("");
            limparCampos();
            calcularTotais();
            
            JOptionPane.showMessageDialog(workArea, "Venda finalizada com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
            SystemLogger.ui("Venda finalizada - Total: " + lblTotal.getText());
        }
    }
    
    /**
     * Formata valor monetário
     */
    private String formatarValor(BigDecimal valor) {
        return valor.setScale(2, RoundingMode.HALF_UP).toString().replace(".", ",");
    }
    
    /**
     * Cria painel de erro
     */
    private JPanel criarPainelErro() {
        JPanel painelErro = new JPanel(new BorderLayout());
        painelErro.setBackground(WHITE);
        
        JLabel erroLabel = new JLabel("❌ Erro ao carregar formulário");
        erroLabel.setFont(new Font("Arial", Font.BOLD, 16));
        erroLabel.setForeground(Color.RED);
        erroLabel.setHorizontalAlignment(JLabel.CENTER);
        
        painelErro.add(erroLabel, BorderLayout.CENTER);
        return painelErro;
    }
    
    /**
     * Classe interna para representar um item da venda
     */
    private static class ItemVenda {
        private int codigo;
        private String produto;
        private int quantidade;
        private BigDecimal valorUnitario;
        private BigDecimal desconto;
        private BigDecimal total;
        
        public ItemVenda(int codigo, String produto, int quantidade, BigDecimal valorUnitario, BigDecimal desconto, BigDecimal total) {
            this.codigo = codigo;
            this.produto = produto;
            this.quantidade = quantidade;
            this.valorUnitario = valorUnitario;
            this.desconto = desconto;
            this.total = total;
        }
        
        // Getters e Setters
        public int getCodigo() { return codigo; }
        public void setCodigo(int codigo) { this.codigo = codigo; }
        public String getProduto() { return produto; }
        public int getQuantidade() { return quantidade; }
        public BigDecimal getValorUnitario() { return valorUnitario; }
        public BigDecimal getDesconto() { return desconto; }
        public BigDecimal getTotal() { return total; }
    }
}
