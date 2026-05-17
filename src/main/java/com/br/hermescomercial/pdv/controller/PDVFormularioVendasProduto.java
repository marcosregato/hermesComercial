package com.br.hermescomercial.pdv.controller;

import com.br.hermescomercial.util.SystemLogger;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Classe especializada para o formulário de Vendas por Produto
 * Estrutura: Header → Busca → Formulário → Tabela
 */
public class PDVFormularioVendasProduto {
    
    private JPanel workArea;
    private String usuarioAtual;
    private String nomeUsuario;
    
    // Componentes do formulário
    private JTextField txtBusca;
    private JTextField txtProduto;
    private JTextField txtCodigo;
    private JComboBox<String> comboPeriodo;
    private JComboBox<String> comboCategoria;
    private JTextField txtTotalVendido;
    private JTextField txtTotalValor;
    private JTextField txtMediaPreco;
    private JTextField txtEstoqueAtual;
    
    // Tabela de vendas por produto
    private JTable tabelaVendas;
    private DefaultTableModel modelTabela;
    private List<VendaProduto> vendasEncontradas;
    
    // Cores
    private static final Color WHITE = Color.WHITE;
    private static final Color ACCENT_COLOR = new Color(52, 152, 219);
    private static final Color SUCCESS_COLOR = new Color(39, 174, 96);
    private static final Color DANGER_COLOR = new Color(231, 76, 60);
    private static final Color WARNING_COLOR = new Color(241, 196, 15);
    private static final Color GRAY = new Color(149, 165, 166);
    
    public PDVFormularioVendasProduto(JPanel workArea, String usuarioAtual, String nomeUsuario) {
        this.workArea = workArea;
        this.usuarioAtual = usuarioAtual;
        this.nomeUsuario = nomeUsuario;
        this.vendasEncontradas = new ArrayList<>();
    }
    
    /**
     * Cria o formulário completo de Vendas por Produto
     */
    public JPanel criarFormularioVendasProduto() {
        try {
            SystemLogger.ui("=== CRIANDO FORMULÁRIO VENDAS POR PRODUTO ===");
            SystemLogger.ui("Usuário: " + usuarioAtual);
            
            JPanel painelPrincipal = new JPanel(new BorderLayout());
            painelPrincipal.setBackground(WHITE);
            
            // Header
            JPanel headerPanel = criarHeader();
            painelPrincipal.add(headerPanel, BorderLayout.NORTH);
            
            // Painel central com busca, formulário e tabela
            JPanel painelCentral = new JPanel(new BorderLayout());
            painelCentral.setBackground(WHITE);
            
            // Painel de busca rápida
            JPanel buscaPanel = criarPainelBusca();
            painelCentral.add(buscaPanel, BorderLayout.NORTH);
            
            // Painel do formulário
            JPanel formularioPanel = criarPainelFormulario();
            painelCentral.add(formularioPanel, BorderLayout.CENTER);
            
            // Tabela de vendas por produto
            JPanel tabelaPanel = criarPainelTabela();
            painelCentral.add(tabelaPanel, BorderLayout.SOUTH);
            
            painelPrincipal.add(painelCentral, BorderLayout.CENTER);
            
            SystemLogger.ui("Formulário Vendas por Produto criado com sucesso");
            return painelPrincipal;
            
        } catch (Exception e) {
            SystemLogger.error("Erro ao criar formulário Vendas por Produto", e);
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
        JLabel titleLabel = new JLabel("📦 Vendas por Produto");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        titleLabel.setForeground(WHITE);
        
        // Informações do usuário
        JPanel userInfoPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        userInfoPanel.setBackground(ACCENT_COLOR);
        
        JLabel userLabel = new JLabel("👤 " + nomeUsuario);
        userLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        userLabel.setForeground(WHITE);
        
        JLabel dateLabel = new JLabel("📅 " + new java.text.SimpleDateFormat("dd/MM/yyyy HH:mm").format(new java.util.Date()));
        dateLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        dateLabel.setForeground(WHITE);
        
        userInfoPanel.add(userLabel);
        userInfoPanel.add(Box.createHorizontalStrut(20));
        userInfoPanel.add(dateLabel);
        
        headerPanel.add(titleLabel, BorderLayout.WEST);
        headerPanel.add(userInfoPanel, BorderLayout.EAST);
        
        return headerPanel;
    }
    
    /**
     * Cria o painel de busca rápida
     */
    private JPanel criarPainelBusca() {
        JPanel buscaPanel = new JPanel(new BorderLayout());
        buscaPanel.setBackground(WHITE);
        buscaPanel.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));
        
        JLabel buscaLabel = new JLabel("🔍 Busca Rápida:");
        buscaLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        buscaLabel.setForeground(ACCENT_COLOR);
        
        txtBusca = new JTextField();
        txtBusca.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        txtBusca.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(GRAY),
            BorderFactory.createEmptyBorder(8, 12, 8, 12)
        ));
        txtBusca.setToolTipText("Digite nome, código ou categoria do produto");
        
        JButton btnBuscar = new JButton("🔍 Buscar");
        btnBuscar.setBackground(ACCENT_COLOR);
        btnBuscar.setForeground(WHITE);
        btnBuscar.setFocusPainted(false);
        btnBuscar.setBorderPainted(false);
        btnBuscar.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btnBuscar.addActionListener(e -> buscarVendas());
        
        JPanel buscaInputPanel = new JPanel(new BorderLayout());
        buscaInputPanel.setBackground(WHITE);
        buscaInputPanel.add(txtBusca, BorderLayout.CENTER);
        buscaInputPanel.add(btnBuscar, BorderLayout.EAST);
        
        buscaPanel.add(buscaLabel, BorderLayout.WEST);
        buscaPanel.add(Box.createHorizontalStrut(10), BorderLayout.CENTER);
        buscaPanel.add(buscaInputPanel, BorderLayout.CENTER);
        
        return buscaPanel;
    }
    
    /**
     * Cria o painel do formulário
     */
    private JPanel criarPainelFormulario() {
        JPanel formularioPanel = new JPanel(new GridBagLayout());
        formularioPanel.setBackground(WHITE);
        formularioPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
        // Seção Filtros
        JLabel filtrosSectionLabel = new JLabel("🔍 Filtros do Relatório");
        filtrosSectionLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        filtrosSectionLabel.setForeground(ACCENT_COLOR);
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 4;
        formularioPanel.add(filtrosSectionLabel, gbc);
        
        // Produto
        gbc.gridy = 1; gbc.gridwidth = 1;
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
        
        // Código
        gbc.gridx = 2; gbc.weightx = 0.0;
        JLabel lblCodigo = new JLabel("Código:");
        lblCodigo.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        formularioPanel.add(lblCodigo, gbc);
        
        gbc.gridx = 3; gbc.weightx = 1.0;
        txtCodigo = new JTextField();
        txtCodigo.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        txtCodigo.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(GRAY),
            BorderFactory.createEmptyBorder(5, 8, 5, 8)
        ));
        formularioPanel.add(txtCodigo, gbc);
        
        // Período
        gbc.gridy = 2; gbc.gridx = 0;
        JLabel lblPeriodo = new JLabel("Período:");
        lblPeriodo.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        formularioPanel.add(lblPeriodo, gbc);
        
        gbc.gridx = 1; gbc.weightx = 1.0;
        comboPeriodo = new JComboBox<>(new String[]{"Hoje", "Esta Semana", "Este Mês", "Personalizado"});
        comboPeriodo.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        comboPeriodo.setBorder(BorderFactory.createLineBorder(GRAY));
        formularioPanel.add(comboPeriodo, gbc);
        
        // Categoria
        gbc.gridx = 2; gbc.weightx = 0.0;
        JLabel lblCategoria = new JLabel("Categoria:");
        lblCategoria.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        formularioPanel.add(lblCategoria, gbc);
        
        gbc.gridx = 3; gbc.weightx = 1.0;
        comboCategoria = new JComboBox<>(new String[]{"Todas", "Eletrônicos", "Informática", "Acessórios", "Periféricos", "Outros"});
        comboCategoria.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        comboCategoria.setBorder(BorderFactory.createLineBorder(GRAY));
        formularioPanel.add(comboCategoria, gbc);
        
        // Seção Resumo do Produto
        gbc.gridy = 3; gbc.gridx = 0; gbc.gridwidth = 4; gbc.weightx = 0.0;
        JLabel resumoSectionLabel = new JLabel("📈 Resumo do Produto");
        resumoSectionLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        resumoSectionLabel.setForeground(ACCENT_COLOR);
        formularioPanel.add(resumoSectionLabel, gbc);
        
        // Total Vendido
        gbc.gridy = 4; gbc.gridwidth = 1;
        JLabel lblTotalVendido = new JLabel("Total Vendido:");
        lblTotalVendido.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        formularioPanel.add(lblTotalVendido, gbc);
        
        gbc.gridx = 1; gbc.weightx = 1.0;
        txtTotalVendido = new JTextField();
        txtTotalVendido.setFont(new Font("Segoe UI", Font.BOLD, 12));
        txtTotalVendido.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(SUCCESS_COLOR),
            BorderFactory.createEmptyBorder(5, 8, 5, 8)
        ));
        txtTotalVendido.setForeground(SUCCESS_COLOR);
        txtTotalVendido.setEditable(false);
        formularioPanel.add(txtTotalVendido, gbc);
        
        // Total Valor
        gbc.gridx = 2; gbc.weightx = 0.0;
        JLabel lblTotalValor = new JLabel("Total Valor:");
        lblTotalValor.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        formularioPanel.add(lblTotalValor, gbc);
        
        gbc.gridx = 3; gbc.weightx = 1.0;
        txtTotalValor = new JTextField();
        txtTotalValor.setFont(new Font("Segoe UI", Font.BOLD, 12));
        txtTotalValor.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(ACCENT_COLOR),
            BorderFactory.createEmptyBorder(5, 8, 5, 8)
        ));
        txtTotalValor.setForeground(ACCENT_COLOR);
        txtTotalValor.setEditable(false);
        formularioPanel.add(txtTotalValor, gbc);
        
        // Média Preço
        gbc.gridy = 5; gbc.gridx = 0;
        JLabel lblMediaPreco = new JLabel("Média Preço:");
        lblMediaPreco.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        formularioPanel.add(lblMediaPreco, gbc);
        
        gbc.gridx = 1; gbc.weightx = 1.0;
        txtMediaPreco = new JTextField();
        txtMediaPreco.setFont(new Font("Segoe UI", Font.BOLD, 12));
        txtMediaPreco.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(GRAY),
            BorderFactory.createEmptyBorder(5, 8, 5, 8)
        ));
        txtMediaPreco.setForeground(GRAY);
        txtMediaPreco.setEditable(false);
        formularioPanel.add(txtMediaPreco, gbc);
        
        // Estoque Atual
        gbc.gridx = 2; gbc.weightx = 0.0;
        JLabel lblEstoqueAtual = new JLabel("Estoque Atual:");
        lblEstoqueAtual.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        formularioPanel.add(lblEstoqueAtual, gbc);
        
        gbc.gridx = 3; gbc.weightx = 1.0;
        txtEstoqueAtual = new JTextField();
        txtEstoqueAtual.setFont(new Font("Segoe UI", Font.BOLD, 12));
        txtEstoqueAtual.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(WARNING_COLOR),
            BorderFactory.createEmptyBorder(5, 8, 5, 8)
        ));
        txtEstoqueAtual.setForeground(WARNING_COLOR);
        txtEstoqueAtual.setEditable(false);
        formularioPanel.add(txtEstoqueAtual, gbc);
        
        // Botões de ação
        gbc.gridy = 6; gbc.gridx = 0; gbc.gridwidth = 4; gbc.weightx = 0.0;
        JPanel botoesPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        botoesPanel.setBackground(WHITE);
        
        JButton btnGerar = new JButton("📊 Gerar Relatório");
        btnGerar.setBackground(ACCENT_COLOR);
        btnGerar.setForeground(WHITE);
        btnGerar.setFocusPainted(false);
        btnGerar.setBorderPainted(false);
        btnGerar.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btnGerar.addActionListener(e -> gerarRelatorio());
        
        JButton btnLimpar = new JButton("🧹 Limpar");
        btnLimpar.setBackground(GRAY);
        btnLimpar.setForeground(WHITE);
        btnLimpar.setFocusPainted(false);
        btnLimpar.setBorderPainted(false);
        btnLimpar.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btnLimpar.addActionListener(e -> limparCampos());
        
        JButton btnExportar = new JButton("📄 Exportar");
        btnExportar.setBackground(SUCCESS_COLOR);
        btnExportar.setForeground(WHITE);
        btnExportar.setFocusPainted(false);
        btnExportar.setBorderPainted(false);
        btnExportar.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btnExportar.addActionListener(e -> exportarDados());
        
        botoesPanel.add(btnGerar);
        botoesPanel.add(Box.createHorizontalStrut(10));
        botoesPanel.add(btnLimpar);
        botoesPanel.add(Box.createHorizontalStrut(10));
        botoesPanel.add(btnExportar);
        
        formularioPanel.add(botoesPanel, gbc);
        
        // Preencher dados iniciais
        preencherDadosIniciais();
        
        return formularioPanel;
    }
    
    /**
     * Cria o painel da tabela
     */
    private JPanel criarPainelTabela() {
        JPanel tabelaPanel = new JPanel(new BorderLayout());
        tabelaPanel.setBackground(WHITE);
        tabelaPanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 20, 20));
        
        JLabel tabelaLabel = new JLabel("📋 Vendas por Produto");
        tabelaLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        tabelaLabel.setForeground(ACCENT_COLOR);
        
        // Modelo da tabela
        String[] colunas = {"Data", "Código", "Produto", "Cliente", "Quantidade", "Valor Unit", "Valor Total", "Ações"};
        modelTabela = new DefaultTableModel(colunas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 7; // Apenas a coluna de ações é editável
            }
        };
        
        tabelaVendas = new JTable(modelTabela);
        tabelaVendas.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        tabelaVendas.setRowHeight(25);
        tabelaVendas.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 12));
        tabelaVendas.getTableHeader().setBackground(ACCENT_COLOR);
        tabelaVendas.getTableHeader().setForeground(WHITE);
        
        // Scroll pane
        JScrollPane scrollPane = new JScrollPane(tabelaVendas);
        scrollPane.setPreferredSize(new Dimension(0, 250));
        scrollPane.setBorder(BorderFactory.createLineBorder(GRAY));
        
        // Painel de botões da tabela
        JPanel botoesTabelaPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        botoesTabelaPanel.setBackground(WHITE);
        
        JButton btnDetalhes = new JButton("👁️ Ver Detalhes");
        btnDetalhes.setBackground(ACCENT_COLOR);
        btnDetalhes.setForeground(WHITE);
        btnDetalhes.setFocusPainted(false);
        btnDetalhes.setBorderPainted(false);
        btnDetalhes.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btnDetalhes.addActionListener(e -> verDetalhes());
        
        JButton btnImprimir = new JButton("🖨️ Imprimir");
        btnImprimir.setBackground(WARNING_COLOR);
        btnImprimir.setForeground(WHITE);
        btnImprimir.setFocusPainted(false);
        btnImprimir.setBorderPainted(false);
        btnImprimir.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btnImprimir.addActionListener(e -> imprimirRelatorio());
        
        botoesTabelaPanel.add(btnDetalhes);
        botoesTabelaPanel.add(Box.createHorizontalStrut(10));
        botoesTabelaPanel.add(btnImprimir);
        
        tabelaPanel.add(tabelaLabel, BorderLayout.NORTH);
        tabelaPanel.add(scrollPane, BorderLayout.CENTER);
        tabelaPanel.add(botoesTabelaPanel, BorderLayout.SOUTH);
        
        // Adicionar dados de exemplo
        adicionarDadosExemplo();
        
        return tabelaPanel;
    }
    
    /**
     * Adiciona dados de exemplo à tabela
     */
    private void adicionarDadosExemplo() {
        // Limpar tabela atual
        modelTabela.setRowCount(0);
        vendasEncontradas.clear();
        
        // Dados de exemplo
        Object[][] dadosExemplo = {
            {"10/05/2026", "NB001", "Notebook Dell", "João Silva", "1", "R$ 3.500,00", "R$ 3.500,00", "👁️"},
            {"09/05/2026", "MS001", "Mouse Logitech", "Maria Santos", "2", "R$ 225,00", "R$ 450,00", "👁️"},
            {"08/05/2026", "NB001", "Notebook Dell", "Pedro Costa", "1", "R$ 3.500,00", "R$ 3.500,00", "👁️"},
            {"07/05/2026", "KB001", "Teclado Mecânico", "Ana Oliveira", "1", "R$ 320,00", "R$ 320,00", "👁️"},
            {"06/05/2026", "MS001", "Mouse Logitech", "Carlos Ferreira", "3", "R$ 225,00", "R$ 675,00", "👁️"},
            {"05/05/2026", "NB001", "Notebook Dell", "Lucas Pereira", "1", "R$ 3.500,00", "R$ 3.500,00", "👁️"}
        };
        
        for (Object[] dados : dadosExemplo) {
            modelTabela.addRow(dados);
            
            // Adicionar à lista de vendas
            VendaProduto venda = new VendaProduto();
            venda.setData((String) dados[0]);
            venda.setCodigo((String) dados[1]);
            venda.setProduto((String) dados[2]);
            venda.setCliente((String) dados[3]);
            venda.setQuantidade((String) dados[4]);
            venda.setValorUnitario((String) dados[5]);
            venda.setValorTotal((String) dados[6]);
            vendasEncontradas.add(venda);
        }
    }
    
    /**
     * Preenche dados iniciais
     */
    private void preencherDadosIniciais() {
        // Simular cálculos
        int totalVendido = 9;
        double totalValor = 11945.00;
        double mediaPreco = totalValor / totalVendido;
        String estoqueAtual = "15";
        
        txtTotalVendido.setText(String.valueOf(totalVendido));
        txtTotalValor.setText(String.format("R$ %,.2f", totalValor));
        txtMediaPreco.setText(String.format("R$ %,.2f", mediaPreco));
        txtEstoqueAtual.setText(estoqueAtual);
    }
    
    /**
     * Busca vendas
     */
    private void buscarVendas() {
        String termo = txtBusca.getText().trim();
        if (termo.isEmpty()) {
            JOptionPane.showMessageDialog(workArea, "Digite um termo para buscar!", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        JOptionPane.showMessageDialog(workArea, 
            "Busca realizada para: " + termo + "\n" +
            "Vendas encontradas: " + vendasEncontradas.size(), 
            "Resultado", JOptionPane.INFORMATION_MESSAGE);
        
        SystemLogger.ui("Busca realizada para: " + termo);
    }
    
    /**
     * Gera relatório
     */
    private void gerarRelatorio() {
        try {
            JOptionPane.showMessageDialog(workArea, 
                "Relatório de vendas por produto gerado com sucesso!\n\n" +
                "Produto: " + txtProduto.getText() + "\n" +
                "Código: " + txtCodigo.getText() + "\n" +
                "Total Vendido: " + txtTotalVendido.getText() + "\n" +
                "Total Valor: " + txtTotalValor.getText() + "\n" +
                "Média Preço: " + txtMediaPreco.getText(), 
                "Sucesso", JOptionPane.INFORMATION_MESSAGE);
            
            SystemLogger.ui("Relatório gerado - " + vendasEncontradas.size() + " vendas");
            
        } catch (Exception e) {
            SystemLogger.error("Erro ao gerar relatório", e);
            JOptionPane.showMessageDialog(workArea, "Erro ao gerar relatório: " + e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    /**
     * Limpa os campos do formulário
     */
    private void limparCampos() {
        txtBusca.setText("");
        txtProduto.setText("");
        txtCodigo.setText("");
        comboPeriodo.setSelectedIndex(0);
        comboCategoria.setSelectedIndex(0);
        txtTotalVendido.setText("");
        txtTotalValor.setText("");
        txtMediaPreco.setText("");
        txtEstoqueAtual.setText("");
        
        preencherDadosIniciais();
    }
    
    /**
     * Ver detalhes da venda
     */
    private void verDetalhes() {
        int selectedRow = tabelaVendas.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(workArea, "Selecione uma venda para ver detalhes!", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        VendaProduto venda = vendasEncontradas.get(selectedRow);
        
        JOptionPane.showMessageDialog(workArea, 
            "Detalhes da Venda:\n\n" +
            "Data: " + venda.getData() + "\n" +
            "Código: " + venda.getCodigo() + "\n" +
            "Produto: " + venda.getProduto() + "\n" +
            "Cliente: " + venda.getCliente() + "\n" +
            "Quantidade: " + venda.getQuantidade() + "\n" +
            "Valor Unitário: " + venda.getValorUnitario() + "\n" +
            "Valor Total: " + venda.getValorTotal(), 
            "Detalhes da Venda", JOptionPane.INFORMATION_MESSAGE);
        
        SystemLogger.ui("Detalhes visualizados: " + venda.getCodigo());
    }
    
    /**
     * Imprime relatório
     */
    private void imprimirRelatorio() {
        JOptionPane.showMessageDialog(workArea, 
            "Imprimindo relatório de vendas por produto...\n" +
            "Produto: " + txtProduto.getText() + "\n" +
            "Registros: " + vendasEncontradas.size() + "\n" +
            "(Implementar impressão em impressora fiscal)", 
            "Imprimir Relatório", JOptionPane.INFORMATION_MESSAGE);
        
        SystemLogger.ui("Imprimindo relatório - " + vendasEncontradas.size() + " vendas");
    }
    
    /**
     * Exporta dados da tabela
     */
    private void exportarDados() {
        JOptionPane.showMessageDialog(workArea, 
            "Exportando " + vendasEncontradas.size() + " vendas...\n(Implementar exportação para CSV/Excel)", 
            "Exportar Dados", JOptionPane.INFORMATION_MESSAGE);
        
        SystemLogger.ui("Exportando " + vendasEncontradas.size() + " vendas");
    }
    
    /**
     * Cria painel de erro
     */
    private JPanel criarPainelErro() {
        JPanel painelErro = new JPanel(new BorderLayout());
        painelErro.setBackground(WHITE);
        
        JLabel erroLabel = new JLabel("❌ Erro ao carregar formulário");
        erroLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
        erroLabel.setForeground(DANGER_COLOR);
        erroLabel.setHorizontalAlignment(SwingConstants.CENTER);
        
        painelErro.add(erroLabel, BorderLayout.CENTER);
        return painelErro;
    }
    
    /**
     * Classe interna para representar uma venda por produto
     */
    private static class VendaProduto {
        private String data;
        private String codigo;
        private String produto;
        private String cliente;
        private String quantidade;
        private String valorUnitario;
        private String valorTotal;
        
        // Getters e Setters
        public String getData() { return data; }
        public void setData(String data) { this.data = data; }
        
        public String getCodigo() { return codigo; }
        public void setCodigo(String codigo) { this.codigo = codigo; }
        
        public String getProduto() { return produto; }
        public void setProduto(String produto) { this.produto = produto; }
        
        public String getCliente() { return cliente; }
        public void setCliente(String cliente) { this.cliente = cliente; }
        
        public String getQuantidade() { return quantidade; }
        public void setQuantidade(String quantidade) { this.quantidade = quantidade; }
        
        public String getValorUnitario() { return valorUnitario; }
        public void setValorUnitario(String valorUnitario) { this.valorUnitario = valorUnitario; }
        
        public String getValorTotal() { return valorTotal; }
        public void setValorTotal(String valorTotal) { this.valorTotal = valorTotal; }
    }
}
