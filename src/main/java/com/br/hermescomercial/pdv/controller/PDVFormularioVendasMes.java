package com.br.hermescomercial.pdv.controller;

import com.br.hermescomercial.util.SystemLogger;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Classe especializada para o formulário de Vendas do Mês
 * Estrutura: Header → Busca → Formulário → Tabela
 */
public class PDVFormularioVendasMes {
    
    private JPanel workArea;
    private String usuarioAtual;
    private String nomeUsuario;
    
    // Componentes do formulário
    private JTextField txtBusca;
    private JTextField txtMes;
    private JTextField txtAno;
    private JComboBox<String> comboPeriodo;
    private JComboBox<String> comboStatus;
    private JComboBox<String> comboFormaPagamento;
    private JTextField txtTotalVendas;
    private JTextField txtTotalValor;
    private JTextField txtMediaDiaria;
    private JTextField txtMelhorDia;
    private JTextArea txtObservacoes;
    
    // Tabela de vendas
    private JTable tabelaVendas;
    private DefaultTableModel modelTabela;
    private List<VendaMes> vendasEncontradas;
    
    // Cores
    private static final Color WHITE = Color.WHITE;
    private static final Color ACCENT_COLOR = new Color(52, 152, 219);
    private static final Color SUCCESS_COLOR = new Color(39, 174, 96);
    private static final Color DANGER_COLOR = new Color(231, 76, 60);
    private static final Color WARNING_COLOR = new Color(241, 196, 15);
    private static final Color GRAY = new Color(149, 165, 166);
    
    public PDVFormularioVendasMes(JPanel workArea, String usuarioAtual, String nomeUsuario) {
        this.workArea = workArea;
        this.usuarioAtual = usuarioAtual;
        this.nomeUsuario = nomeUsuario;
        this.vendasEncontradas = new ArrayList<>();
    }
    
    /**
     * Cria o formulário completo de Vendas do Mês
     */
    public JPanel criarFormularioVendasMes() {
        try {
            SystemLogger.ui("=== CRIANDO FORMULÁRIO VENDAS DO MÊS ===");
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
            
            // Tabela de vendas
            JPanel tabelaPanel = criarPainelTabela();
            painelCentral.add(tabelaPanel, BorderLayout.SOUTH);
            
            painelPrincipal.add(painelCentral, BorderLayout.CENTER);
            
            SystemLogger.ui("Formulário Vendas do Mês criado com sucesso");
            return painelPrincipal;
            
        } catch (Exception e) {
            SystemLogger.error("Erro ao criar formulário Vendas do Mês", e);
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
        JLabel titleLabel = new JLabel("📅 Vendas do Mês");
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
        txtBusca.setToolTipText("Digite código, cliente ou produto");
        
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
        
        // Mês
        gbc.gridy = 1; gbc.gridwidth = 1;
        JLabel lblMes = new JLabel("Mês:");
        lblMes.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        formularioPanel.add(lblMes, gbc);
        
        gbc.gridx = 1; gbc.weightx = 1.0;
        txtMes = new JTextField();
        txtMes.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        txtMes.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(GRAY),
            BorderFactory.createEmptyBorder(5, 8, 5, 8)
        ));
        txtMes.setToolTipText("mm");
        formularioPanel.add(txtMes, gbc);
        
        // Ano
        gbc.gridx = 2; gbc.weightx = 0.0;
        JLabel lblAno = new JLabel("Ano:");
        lblAno.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        formularioPanel.add(lblAno, gbc);
        
        gbc.gridx = 3; gbc.weightx = 1.0;
        txtAno = new JTextField();
        txtAno.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        txtAno.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(GRAY),
            BorderFactory.createEmptyBorder(5, 8, 5, 8)
        ));
        txtAno.setToolTipText("aaaa");
        formularioPanel.add(txtAno, gbc);
        
        // Período
        gbc.gridy = 2; gbc.gridx = 0;
        JLabel lblPeriodo = new JLabel("Período:");
        lblPeriodo.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        formularioPanel.add(lblPeriodo, gbc);
        
        gbc.gridx = 1; gbc.weightx = 1.0;
        comboPeriodo = new JComboBox<>(new String[]{"Este Mês", "Mês Anterior", "Últimos 3 Meses", "Personalizado"});
        comboPeriodo.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        comboPeriodo.setBorder(BorderFactory.createLineBorder(GRAY));
        comboPeriodo.addActionListener(e -> atualizarPeriodo());
        formularioPanel.add(comboPeriodo, gbc);
        
        // Status
        gbc.gridx = 2; gbc.weightx = 0.0;
        JLabel lblStatus = new JLabel("Status:");
        lblStatus.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        formularioPanel.add(lblStatus, gbc);
        
        gbc.gridx = 3; gbc.weightx = 1.0;
        comboStatus = new JComboBox<>(new String[]{"Todos", "Concluídas", "Canceladas", "Em Andamento"});
        comboStatus.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        comboStatus.setBorder(BorderFactory.createLineBorder(GRAY));
        formularioPanel.add(comboStatus, gbc);
        
        // Forma Pagamento
        gbc.gridy = 3; gbc.gridx = 0;
        JLabel lblFormaPagamento = new JLabel("Forma Pagamento:");
        lblFormaPagamento.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        formularioPanel.add(lblFormaPagamento, gbc);
        
        gbc.gridx = 1; gbc.gridwidth = 3; gbc.weightx = 1.0;
        comboFormaPagamento = new JComboBox<>(new String[]{"Todas", "Dinheiro", "Cartão Crédito", "Cartão Débito", "PIX", "Transferência"});
        comboFormaPagamento.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        comboFormaPagamento.setBorder(BorderFactory.createLineBorder(GRAY));
        formularioPanel.add(comboFormaPagamento, gbc);
        
        // Seção Resumo Mensal
        gbc.gridy = 4; gbc.gridx = 0; gbc.gridwidth = 4; gbc.weightx = 0.0;
        JLabel resumoSectionLabel = new JLabel("📈 Resumo Mensal");
        resumoSectionLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        resumoSectionLabel.setForeground(ACCENT_COLOR);
        formularioPanel.add(resumoSectionLabel, gbc);
        
        // Total Vendas
        gbc.gridy = 5; gbc.gridwidth = 1;
        JLabel lblTotalVendas = new JLabel("Total Vendas:");
        lblTotalVendas.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        formularioPanel.add(lblTotalVendas, gbc);
        
        gbc.gridx = 1; gbc.weightx = 1.0;
        txtTotalVendas = new JTextField();
        txtTotalVendas.setFont(new Font("Segoe UI", Font.BOLD, 12));
        txtTotalVendas.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(SUCCESS_COLOR),
            BorderFactory.createEmptyBorder(5, 8, 5, 8)
        ));
        txtTotalVendas.setForeground(SUCCESS_COLOR);
        txtTotalVendas.setEditable(false);
        formularioPanel.add(txtTotalVendas, gbc);
        
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
        
        // Média Diária
        gbc.gridy = 6; gbc.gridx = 0;
        JLabel lblMediaDiaria = new JLabel("Média Diária:");
        lblMediaDiaria.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        formularioPanel.add(lblMediaDiaria, gbc);
        
        gbc.gridx = 1; gbc.weightx = 1.0;
        txtMediaDiaria = new JTextField();
        txtMediaDiaria.setFont(new Font("Segoe UI", Font.BOLD, 12));
        txtMediaDiaria.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(GRAY),
            BorderFactory.createEmptyBorder(5, 8, 5, 8)
        ));
        txtMediaDiaria.setForeground(GRAY);
        txtMediaDiaria.setEditable(false);
        formularioPanel.add(txtMediaDiaria, gbc);
        
        // Melhor Dia
        gbc.gridx = 2; gbc.weightx = 0.0;
        JLabel lblMelhorDia = new JLabel("Melhor Dia:");
        lblMelhorDia.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        formularioPanel.add(lblMelhorDia, gbc);
        
        gbc.gridx = 3; gbc.weightx = 1.0;
        txtMelhorDia = new JTextField();
        txtMelhorDia.setFont(new Font("Segoe UI", Font.BOLD, 12));
        txtMelhorDia.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(SUCCESS_COLOR),
            BorderFactory.createEmptyBorder(5, 8, 5, 8)
        ));
        txtMelhorDia.setForeground(SUCCESS_COLOR);
        txtMelhorDia.setEditable(false);
        formularioPanel.add(txtMelhorDia, gbc);
        
        // Botões de ação
        gbc.gridy = 7; gbc.gridx = 0; gbc.gridwidth = 4; gbc.weightx = 0.0;
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
        
        JLabel tabelaLabel = new JLabel("📋 Vendas do Mês");
        tabelaLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        tabelaLabel.setForeground(ACCENT_COLOR);
        
        // Modelo da tabela
        String[] colunas = {"Data", "Código", "Cliente", "Forma Pagamento", "Valor", "Status", "Itens", "Ações"};
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
            {"01/05/2026", "V001", "João Silva", "Cartão Crédito", "R$ 1.200,00", "Concluída", "3", "👁️"},
            {"02/05/2026", "V002", "Maria Santos", "PIX", "R$ 450,00", "Concluída", "2", "👁️"},
            {"03/05/2026", "V003", "Pedro Costa", "Dinheiro", "R$ 850,00", "Concluída", "1", "👁️"},
            {"04/05/2026", "V004", "Ana Oliveira", "Cartão Débito", "R$ 320,00", "Concluída", "4", "👁️"},
            {"05/05/2026", "V005", "Carlos Ferreira", "Transferência", "R$ 2.100,00", "Concluída", "5", "👁️"},
            {"06/05/2026", "V006", "Lucas Pereira", "Cartão Crédito", "R$ 750,00", "Concluída", "2", "👁️"},
            {"07/05/2026", "V007", "Juliana Costa", "PIX", "R$ 1.800,00", "Concluída", "3", "👁️"},
            {"08/05/2026", "V008", "Marcos Silva", "Dinheiro", "R$ 420,00", "Concluída", "1", "👁️"}
        };
        
        for (Object[] dados : dadosExemplo) {
            modelTabela.addRow(dados);
            
            // Adicionar à lista de vendas
            VendaMes venda = new VendaMes();
            venda.setData((String) dados[0]);
            venda.setCodigo((String) dados[1]);
            venda.setCliente((String) dados[2]);
            venda.setFormaPagamento((String) dados[3]);
            venda.setValor((String) dados[4]);
            venda.setStatus((String) dados[5]);
            venda.setItens((String) dados[6]);
            vendasEncontradas.add(venda);
        }
    }
    
    /**
     * Atualiza período
     */
    private void atualizarPeriodo() {
        String periodo = (String) comboPeriodo.getSelectedItem();
        
        if ("Personalizado".equals(periodo)) {
            txtMes.setEditable(true);
            txtAno.setEditable(true);
        } else {
            txtMes.setEditable(false);
            txtAno.setEditable(false);
            
            // Preencher datas automáticas
            java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("MM/yyyy");
            String atual = sdf.format(new java.util.Date());
            
            switch (periodo) {
                case "Este Mês":
                    txtMes.setText("05");
                    txtAno.setText("2026");
                    break;
                case "Mês Anterior":
                    txtMes.setText("04");
                    txtAno.setText("2026");
                    break;
                case "Últimos 3 Meses":
                    txtMes.setText("03-05");
                    txtAno.setText("2026");
                    break;
                default:
                    txtMes.setText("");
                    txtAno.setText("");
            }
        }
    }
    
    /**
     * Preenche dados iniciais
     */
    private void preencherDadosIniciais() {
        // Simular cálculos
        int totalVendas = vendasEncontradas.size();
        double totalValor = 8890.00;
        int diasMes = 31;
        double mediaDiaria = totalValor / diasMes;
        String melhorDia = "05/05/2026 - R$ 2.100,00";
        
        txtTotalVendas.setText(String.valueOf(totalVendas));
        txtTotalValor.setText(String.format("R$ %,.2f", totalValor));
        txtMediaDiaria.setText(String.format("R$ %,.2f", mediaDiaria));
        txtMelhorDia.setText(melhorDia);
        
        // Preencher período
        atualizarPeriodo();
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
        
        // TODO: Implementar lógica de busca no banco de dados
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
            // TODO: Implementar lógica de geração no banco de dados
            JOptionPane.showMessageDialog(workArea, 
                "Relatório mensal gerado com sucesso!\n\n" +
                "Período: " + comboPeriodo.getSelectedItem() + "\n" +
                "Total Vendas: " + txtTotalVendas.getText() + "\n" +
                "Total Valor: " + txtTotalValor.getText() + "\n" +
                "Média Diária: " + txtMediaDiaria.getText(), 
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
        comboPeriodo.setSelectedIndex(0);
        comboStatus.setSelectedIndex(0);
        comboFormaPagamento.setSelectedIndex(0);
        txtTotalVendas.setText("");
        txtTotalValor.setText("");
        txtMediaDiaria.setText("");
        txtMelhorDia.setText("");
        
        atualizarPeriodo();
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
        
        VendaMes venda = vendasEncontradas.get(selectedRow);
        
        JOptionPane.showMessageDialog(workArea, 
            "Detalhes da Venda:\n\n" +
            "Data: " + venda.getData() + "\n" +
            "Código: " + venda.getCodigo() + "\n" +
            "Cliente: " + venda.getCliente() + "\n" +
            "Forma Pagamento: " + venda.getFormaPagamento() + "\n" +
            "Valor: " + venda.getValor() + "\n" +
            "Status: " + venda.getStatus() + "\n" +
            "Itens: " + venda.getItens(), 
            "Detalhes da Venda", JOptionPane.INFORMATION_MESSAGE);
        
        SystemLogger.ui("Detalhes visualizados: " + venda.getCodigo());
    }
    
    /**
     * Imprime relatório
     */
    private void imprimirRelatorio() {
        JOptionPane.showMessageDialog(workArea, 
            "Imprimindo relatório mensal...\n" +
            "Período: " + comboPeriodo.getSelectedItem() + "\n" +
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
     * Classe interna para representar uma venda do mês
     */
    private static class VendaMes {
        private String data;
        private String codigo;
        private String cliente;
        private String formaPagamento;
        private String valor;
        private String status;
        private String itens;
        
        // Getters e Setters
        public String getData() { return data; }
        public void setData(String data) { this.data = data; }
        
        public String getCodigo() { return codigo; }
        public void setCodigo(String codigo) { this.codigo = codigo; }
        
        public String getCliente() { return cliente; }
        public void setCliente(String cliente) { this.cliente = cliente; }
        
        public String getFormaPagamento() { return formaPagamento; }
        public void setFormaPagamento(String formaPagamento) { this.formaPagamento = formaPagamento; }
        
        public String getValor() { return valor; }
        public void setValor(String valor) { this.valor = valor; }
        
        public String getStatus() { return status; }
        public void setStatus(String status) { this.status = status; }
        
        public String getItens() { return itens; }
        public void setItens(String itens) { this.itens = itens; }
    }
}
