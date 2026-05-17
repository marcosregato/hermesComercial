package com.br.hermescomercial.pdv.controller;

import com.br.hermescomercial.util.SystemLogger;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.List;

/**
 * Classe especializada para o formulário de Devoluções
 * Estrutura: Header → Busca → Formulário → Tabela
 */
public class PDVFormularioDevolucoes {
    
    private JPanel workArea;
    private String usuarioAtual;
    private String nomeUsuario;
    
    // Componentes do formulário
    private JPanel dateChooserDataDevolucao;
    private JTextField txtNumeroVendaOriginal;
    private JTextField txtNumeroDevolucao;
    private JTextField txtCliente;
    private JTextField txtCPF;
    private JTextField txtProduto;
    private JTextField txtQuantidade;
    private JTextField txtMotivo;
    private JComboBox<String> comboTipoDevolucao;
    private JComboBox<String> comboStatus;
    private JTextArea txtObservacoes;
    
    // Tabela de devoluções
    private JTable tabelaDevolucoes;
    private DefaultTableModel modelTabela;
    private List<Devolucao> devolucoesEncontradas;
    
    // Cores
    private static final Color WHITE = Color.WHITE;
    private static final Color ACCENT_COLOR = new Color(52, 152, 219);
    private static final Color SUCCESS_COLOR = new Color(39, 174, 96);
    private static final Color DANGER_COLOR = new Color(231, 76, 60);
    private static final Color GRAY = new Color(149, 165, 166);
    
    public PDVFormularioDevolucoes(JPanel workArea, String usuarioAtual, String nomeUsuario) {
        this.workArea = workArea;
        this.usuarioAtual = usuarioAtual;
        this.nomeUsuario = nomeUsuario;
        this.devolucoesEncontradas = new ArrayList<>();
    }
    
    /**
     * Cria o formulário completo de Devoluções
     */
    public JPanel criarFormularioDevolucoes() {
        try {
            SystemLogger.ui("=== CRIANDO FORMULÁRIO DEVOLUÇÕES ===");
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
            
            // Painel do formulário de devolução
            JPanel formularioPanel = criarPainelFormulario();
            painelCentral.add(formularioPanel, BorderLayout.CENTER);
            
            // Tabela de devoluções
            JPanel tabelaPanel = criarPainelTabela();
            painelCentral.add(tabelaPanel, BorderLayout.SOUTH);
            
            painelPrincipal.add(painelCentral, BorderLayout.CENTER);
            
            SystemLogger.ui("Formulário Devoluções criado com sucesso");
            return painelPrincipal;
            
        } catch (Exception e) {
            SystemLogger.error("Erro ao criar formulário Devoluções", e);
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
        JLabel titleLabel = new JLabel("↩️ Devoluções");
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
     * Cria o painel de busca rápida
     */
    private JPanel criarPainelBusca() {
        JPanel buscaPanel = new JPanel(new BorderLayout());
        buscaPanel.setBackground(new Color(245, 245, 245));
        buscaPanel.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));
        
        JLabel buscaLabel = new JLabel("🔍 Busca Rápida");
        buscaLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        buscaLabel.setForeground(ACCENT_COLOR);
        
        JPanel buscaContainer = new JPanel(new BorderLayout());
        buscaContainer.setOpaque(false);
        
        JTextField txtBuscaRapida = new JTextField();
        txtBuscaRapida.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        txtBuscaRapida.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(GRAY),
            BorderFactory.createEmptyBorder(8, 12, 8, 12)
        ));
        txtBuscaRapida.setToolTipText("Digite número da devolução, venda original ou cliente para busca rápida");
        
        // Enter para buscar
        txtBuscaRapida.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    realizarBuscaRapida(txtBuscaRapida.getText().trim());
                }
            }
        });
        
        JButton btnBuscaRapida = new JButton("🔍 Buscar");
        btnBuscaRapida.setBackground(ACCENT_COLOR);
        btnBuscaRapida.setForeground(WHITE);
        btnBuscaRapida.setFocusPainted(false);
        btnBuscaRapida.setBorderPainted(false);
        btnBuscaRapida.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btnBuscaRapida.addActionListener(e -> realizarBuscaRapida(txtBuscaRapida.getText().trim()));
        
        buscaContainer.add(txtBuscaRapida, BorderLayout.CENTER);
        buscaContainer.add(btnBuscaRapida, BorderLayout.EAST);
        
        buscaPanel.add(buscaLabel, BorderLayout.NORTH);
        buscaPanel.add(buscaContainer, BorderLayout.CENTER);
        
        return buscaPanel;
    }
    
    /**
     * Cria o painel do formulário de devolução
     */
    private JPanel criarPainelFormulario() {
        JPanel formularioPanel = new JPanel(new GridBagLayout());
        formularioPanel.setBackground(WHITE);
        formularioPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
        // Seção Dados da Devolução
        JLabel devolucaoSectionLabel = new JLabel("↩️ Dados da Devolução");
        devolucaoSectionLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        devolucaoSectionLabel.setForeground(ACCENT_COLOR);
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 4;
        formularioPanel.add(devolucaoSectionLabel, gbc);
        
        gbc.gridy = 1; gbc.gridwidth = 1;
        JLabel lblNumeroDevolucao = new JLabel("Nº Devolução:");
        lblNumeroDevolucao.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        formularioPanel.add(lblNumeroDevolucao, gbc);
        
        gbc.gridx = 1; gbc.weightx = 1.0;
        txtNumeroDevolucao = new JTextField(gerarNumeroDevolucao());
        txtNumeroDevolucao.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        txtNumeroDevolucao.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(GRAY),
            BorderFactory.createEmptyBorder(5, 8, 5, 8)
        ));
        txtNumeroDevolucao.setEditable(false);
        formularioPanel.add(txtNumeroDevolucao, gbc);
        
        gbc.gridx = 2; gbc.weightx = 0.0;
        JLabel lblDataDevolucao = new JLabel("Data Devolução:");
        lblDataDevolucao.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        formularioPanel.add(lblDataDevolucao, gbc);
        
        gbc.gridx = 3; gbc.weightx = 1.0;
        dateChooserDataDevolucao = criarDateChooser();
        formularioPanel.add(dateChooserDataDevolucao, gbc);
        
        // Seção Venda Original
        gbc.gridy = 2; gbc.gridx = 0; gbc.gridwidth = 4; gbc.weightx = 0.0;
        JLabel vendaSectionLabel = new JLabel("📋 Venda Original");
        vendaSectionLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        vendaSectionLabel.setForeground(ACCENT_COLOR);
        formularioPanel.add(vendaSectionLabel, gbc);
        
        gbc.gridy = 3; gbc.gridwidth = 1;
        JLabel lblNumeroVendaOriginal = new JLabel("Nº Venda Original:");
        lblNumeroVendaOriginal.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        formularioPanel.add(lblNumeroVendaOriginal, gbc);
        
        gbc.gridx = 1; gbc.weightx = 1.0;
        txtNumeroVendaOriginal = new JTextField();
        txtNumeroVendaOriginal.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        txtNumeroVendaOriginal.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(GRAY),
            BorderFactory.createEmptyBorder(5, 8, 5, 8)
        ));
        txtNumeroVendaOriginal.setToolTipText("Digite o número da venda original");
        formularioPanel.add(txtNumeroVendaOriginal, gbc);
        
        gbc.gridx = 2; gbc.weightx = 0.0;
        JLabel lblTipoDevolucao = new JLabel("Tipo Devolução:");
        lblTipoDevolucao.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        formularioPanel.add(lblTipoDevolucao, gbc);
        
        gbc.gridx = 3; gbc.weightx = 1.0;
        comboTipoDevolucao = new JComboBox<>(new String[]{"Total", "Parcial", "Troca"});
        comboTipoDevolucao.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        comboTipoDevolucao.setBorder(BorderFactory.createLineBorder(GRAY));
        formularioPanel.add(comboTipoDevolucao, gbc);
        
        // Seção Cliente
        gbc.gridy = 4; gbc.gridx = 0; gbc.gridwidth = 4; gbc.weightx = 0.0;
        JLabel clienteSectionLabel = new JLabel("👤 Dados do Cliente");
        clienteSectionLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        clienteSectionLabel.setForeground(ACCENT_COLOR);
        formularioPanel.add(clienteSectionLabel, gbc);
        
        gbc.gridy = 5; gbc.gridwidth = 1;
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
        
        gbc.gridx = 3; gbc.weightx = 1.0;
        txtCPF = new JTextField();
        txtCPF.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        txtCPF.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(GRAY),
            BorderFactory.createEmptyBorder(5, 8, 5, 8)
        ));
        formularioPanel.add(txtCPF, gbc);
        
        // Seção Produto
        gbc.gridy = 6; gbc.gridx = 0; gbc.gridwidth = 4; gbc.weightx = 0.0;
        JLabel produtoSectionLabel = new JLabel("📦 Dados do Produto");
        produtoSectionLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        produtoSectionLabel.setForeground(ACCENT_COLOR);
        formularioPanel.add(produtoSectionLabel, gbc);
        
        gbc.gridy = 7; gbc.gridwidth = 1;
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
        JLabel lblQuantidade = new JLabel("Quantidade:");
        lblQuantidade.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        formularioPanel.add(lblQuantidade, gbc);
        
        gbc.gridx = 3; gbc.weightx = 1.0;
        txtQuantidade = new JTextField("1");
        txtQuantidade.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        txtQuantidade.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(GRAY),
            BorderFactory.createEmptyBorder(5, 8, 5, 8)
        ));
        txtQuantidade.setHorizontalAlignment(JTextField.RIGHT);
        formularioPanel.add(txtQuantidade, gbc);
        
        // Seção Motivo
        gbc.gridy = 8; gbc.gridx = 0; gbc.gridwidth = 4; gbc.weightx = 0.0;
        JLabel motivoSectionLabel = new JLabel("📝 Motivo da Devolução");
        motivoSectionLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        motivoSectionLabel.setForeground(ACCENT_COLOR);
        formularioPanel.add(motivoSectionLabel, gbc);
        
        gbc.gridy = 9; gbc.gridwidth = 1;
        JLabel lblMotivo = new JLabel("Motivo:");
        lblMotivo.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        formularioPanel.add(lblMotivo, gbc);
        
        gbc.gridx = 1; gbc.weightx = 1.0;
        txtMotivo = new JTextField();
        txtMotivo.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        txtMotivo.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(GRAY),
            BorderFactory.createEmptyBorder(5, 8, 5, 8)
        ));
        txtMotivo.setToolTipText("Descreva o motivo da devolução");
        formularioPanel.add(txtMotivo, gbc);
        
        gbc.gridx = 2; gbc.weightx = 0.0;
        JLabel lblStatus = new JLabel("Status:");
        lblStatus.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        formularioPanel.add(lblStatus, gbc);
        
        gbc.gridx = 3; gbc.weightx = 1.0;
        comboStatus = new JComboBox<>(new String[]{"Pendente", "Aprovada", "Rejeitada", "Concluída"});
        comboStatus.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        comboStatus.setBorder(BorderFactory.createLineBorder(GRAY));
        formularioPanel.add(comboStatus, gbc);
        
        // Seção Observações
        gbc.gridy = 10; gbc.gridx = 0; gbc.gridwidth = 4; gbc.weightx = 0.0;
        JLabel obsSectionLabel = new JLabel("📄 Observações");
        obsSectionLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        obsSectionLabel.setForeground(ACCENT_COLOR);
        formularioPanel.add(obsSectionLabel, gbc);
        
        gbc.gridy = 11; gbc.gridx = 0; gbc.gridwidth = 4; gbc.weightx = 1.0; gbc.fill = GridBagConstraints.BOTH;
        txtObservacoes = new JTextArea(3, 50);
        txtObservacoes.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        txtObservacoes.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(GRAY),
            BorderFactory.createEmptyBorder(8, 8, 8, 8)
        ));
        txtObservacoes.setLineWrap(true);
        txtObservacoes.setWrapStyleWord(true);
        txtObservacoes.setToolTipText("Observações adicionais sobre a devolução");
        
        JScrollPane scrollObs = new JScrollPane(txtObservacoes);
        scrollObs.setPreferredSize(new Dimension(0, 80));
        formularioPanel.add(scrollObs, gbc);
        
        // Botões de ação
        gbc.gridy = 12; gbc.gridx = 0; gbc.gridwidth = 4; gbc.weightx = 0.0; gbc.fill = GridBagConstraints.HORIZONTAL;
        JPanel botoesPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        botoesPanel.setBackground(WHITE);
        
        JButton btnRegistrar = new JButton("✅ Registrar Devolução");
        btnRegistrar.setBackground(SUCCESS_COLOR);
        btnRegistrar.setForeground(WHITE);
        btnRegistrar.setFocusPainted(false);
        btnRegistrar.setBorderPainted(false);
        btnRegistrar.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btnRegistrar.addActionListener(e -> registrarDevolucao());
        
        JButton btnLimpar = new JButton("🗑️ Limpar");
        btnLimpar.setBackground(GRAY);
        btnLimpar.setForeground(WHITE);
        btnLimpar.setFocusPainted(false);
        btnLimpar.setBorderPainted(false);
        btnLimpar.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btnLimpar.addActionListener(e -> limparCampos());
        
        JButton btnConsultar = new JButton("🔍 Consultar");
        btnConsultar.setBackground(ACCENT_COLOR);
        btnConsultar.setForeground(WHITE);
        btnConsultar.setFocusPainted(false);
        btnConsultar.setBorderPainted(false);
        btnConsultar.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btnConsultar.addActionListener(e -> consultarDevolucoes());
        
        botoesPanel.add(btnRegistrar);
        botoesPanel.add(Box.createHorizontalStrut(10));
        botoesPanel.add(btnLimpar);
        botoesPanel.add(Box.createHorizontalStrut(10));
        botoesPanel.add(btnConsultar);
        
        formularioPanel.add(botoesPanel, gbc);
        
        return formularioPanel;
    }
    
    /**
     * Cria um campo de data simplificado
     */
    private JPanel criarDateChooser() {
        JTextField dateField = new JTextField();
        dateField.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        dateField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(GRAY),
            BorderFactory.createEmptyBorder(5, 8, 5, 8)
        ));
        dateField.setText(new SimpleDateFormat("dd/MM/yyyy").format(new Date()));
        dateField.setToolTipText("DD/MM/AAAA");
        
        JPanel datePanel = new JPanel(new BorderLayout());
        datePanel.setOpaque(false);
        datePanel.add(dateField, BorderLayout.CENTER);
        
        JButton btnCalendario = new JButton("📅");
        btnCalendario.setFont(new Font("Segoe UI", Font.PLAIN, 10));
        btnCalendario.setBackground(Color.LIGHT_GRAY);
        btnCalendario.setBorderPainted(false);
        btnCalendario.setFocusPainted(false);
        btnCalendario.setPreferredSize(new Dimension(30, 20));
        
        datePanel.add(btnCalendario, BorderLayout.EAST);
        
        return datePanel;
    }
    
    /**
     * Gera número automático para devolução
     */
    private String gerarNumeroDevolucao() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        String data = sdf.format(new Date());
        Random random = new Random();
        int numero = random.nextInt(10000);
        return "DEV-" + data + "-" + String.format("%04d", numero);
    }
    
    /**
     * Cria o painel da tabela de devoluções
     */
    private JPanel criarPainelTabela() {
        JPanel tabelaPanel = new JPanel(new BorderLayout());
        tabelaPanel.setBackground(WHITE);
        tabelaPanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 20, 20));
        
        JLabel tabelaLabel = new JLabel("📋 Histórico de Devoluções");
        tabelaLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        tabelaLabel.setForeground(ACCENT_COLOR);
        
        // Modelo da tabela
        String[] colunas = {"Nº Devolução", "Data", "Nº Venda Original", "Cliente", "Produto", "Qtd", "Tipo", "Motivo", "Status", "Ações"};
        modelTabela = new DefaultTableModel(colunas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 9; // Apenas a coluna de ações é editável
            }
        };
        
        tabelaDevolucoes = new JTable(modelTabela);
        tabelaDevolucoes.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        tabelaDevolucoes.setRowHeight(25);
        tabelaDevolucoes.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 12));
        tabelaDevolucoes.getTableHeader().setBackground(ACCENT_COLOR);
        tabelaDevolucoes.getTableHeader().setForeground(WHITE);
        
        // Scroll pane
        JScrollPane scrollPane = new JScrollPane(tabelaDevolucoes);
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
        btnDetalhes.addActionListener(e -> verDetalhesDevolucao());
        
        JButton btnAprovar = new JButton("✅ Aprovar");
        btnAprovar.setBackground(SUCCESS_COLOR);
        btnAprovar.setForeground(WHITE);
        btnAprovar.setFocusPainted(false);
        btnAprovar.setBorderPainted(false);
        btnAprovar.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btnAprovar.addActionListener(e -> aprovarDevolucao());
        
        JButton btnRejeitar = new JButton("❌ Rejeitar");
        btnRejeitar.setBackground(DANGER_COLOR);
        btnRejeitar.setForeground(WHITE);
        btnRejeitar.setFocusPainted(false);
        btnRejeitar.setBorderPainted(false);
        btnRejeitar.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btnRejeitar.addActionListener(e -> rejeitarDevolucao());
        
        JButton btnImprimir = new JButton("🖨️ Imprimir");
        btnImprimir.setBackground(ACCENT_COLOR);
        btnImprimir.setForeground(WHITE);
        btnImprimir.setFocusPainted(false);
        btnImprimir.setBorderPainted(false);
        btnImprimir.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btnImprimir.addActionListener(e -> imprimirDevolucao());
        
        botoesTabelaPanel.add(btnDetalhes);
        botoesTabelaPanel.add(Box.createHorizontalStrut(10));
        botoesTabelaPanel.add(btnAprovar);
        botoesTabelaPanel.add(Box.createHorizontalStrut(10));
        botoesTabelaPanel.add(btnRejeitar);
        botoesTabelaPanel.add(Box.createHorizontalStrut(10));
        botoesTabelaPanel.add(btnImprimir);
        
        tabelaPanel.add(tabelaLabel, BorderLayout.NORTH);
        tabelaPanel.add(scrollPane, BorderLayout.CENTER);
        tabelaPanel.add(botoesTabelaPanel, BorderLayout.SOUTH);
        
        return tabelaPanel;
    }
    
    /**
     * Realiza busca rápida
     */
    private void realizarBuscaRapida(String termo) {
        if (termo.isEmpty()) {
            JOptionPane.showMessageDialog(workArea, "Digite um termo para buscar!", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        JOptionPane.showMessageDialog(workArea, "Busca rápida por: " + termo + "\n(Implementar busca no banco)", "Busca Rápida", JOptionPane.INFORMATION_MESSAGE);
    }
    
    /**
     * Registra uma nova devolução
     */
    private void registrarDevolucao() {
        try {
            // Validações
            if (txtNumeroVendaOriginal.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(workArea, "Informe o número da venda original!", "Aviso", JOptionPane.WARNING_MESSAGE);
                return;
            }
            
            if (txtCliente.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(workArea, "Informe o nome do cliente!", "Aviso", JOptionPane.WARNING_MESSAGE);
                return;
            }
            
            if (txtProduto.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(workArea, "Informe o nome do produto!", "Aviso", JOptionPane.WARNING_MESSAGE);
                return;
            }
            
            if (txtMotivo.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(workArea, "Informe o motivo da devolução!", "Aviso", JOptionPane.WARNING_MESSAGE);
                return;
            }
            
            // Coletar dados
            String numeroDevolucao = txtNumeroDevolucao.getText().trim();
            String dataDevolucao = ((JTextField) dateChooserDataDevolucao.getComponent(0)).getText();
            String numeroVendaOriginal = txtNumeroVendaOriginal.getText().trim();
            String cliente = txtCliente.getText().trim();
            String cpf = txtCPF.getText().trim();
            String produto = txtProduto.getText().trim();
            String quantidade = txtQuantidade.getText().trim();
            String tipoDevolucao = (String) comboTipoDevolucao.getSelectedItem();
            String motivo = txtMotivo.getText().trim();
            String status = (String) comboStatus.getSelectedItem();
            String observacoes = txtObservacoes.getText().trim();
            
            
            // Adicionar à tabela
            Object[] rowData = {
                numeroDevolucao,
                dataDevolucao,
                numeroVendaOriginal,
                cliente,
                produto,
                quantidade,
                tipoDevolucao,
                motivo,
                status,
                "👁️"
            };
            modelTabela.addRow(rowData);
            
            // Adicionar à lista
            Devolucao devolucao = new Devolucao();
            devolucao.setNumero(numeroDevolucao);
            devolucao.setData(dataDevolucao);
            devolucao.setNumeroVendaOriginal(numeroVendaOriginal);
            devolucao.setCliente(cliente);
            devolucao.setCpf(cpf);
            devolucao.setProduto(produto);
            devolucao.setQuantidade(quantidade);
            devolucao.setTipo(tipoDevolucao);
            devolucao.setMotivo(motivo);
            devolucao.setStatus(status);
            devolucao.setObservacoes(observacoes);
            devolucoesEncontradas.add(devolucao);
            
            // Limpar campos e gerar novo número
            limparCampos();
            txtNumeroDevolucao.setText(gerarNumeroDevolucao());
            
            JOptionPane.showMessageDialog(workArea, 
                "Devolução registrada com sucesso!\n" +
                "Número: " + numeroDevolucao + "\n" +
                "Venda Original: " + numeroVendaOriginal, 
                "Sucesso", JOptionPane.INFORMATION_MESSAGE);
            
            SystemLogger.ui("Devolução registrada: " + numeroDevolucao);
            
        } catch (Exception e) {
            SystemLogger.error("Erro ao registrar devolução", e);
            JOptionPane.showMessageDialog(workArea, "Erro ao registrar devolução: " + e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    /**
     * Consulta devoluções existentes
     */
    private void consultarDevolucoes() {
        try {
            // Por enquanto, vamos adicionar dados de exemplo
            adicionarDadosExemplo();
            
            JOptionPane.showMessageDialog(workArea, 
                "Consulta realizada com sucesso!\n" +
                "Devoluções encontradas: " + devolucoesEncontradas.size(), 
                "Resultado", JOptionPane.INFORMATION_MESSAGE);
            
            SystemLogger.ui("Consulta realizada - " + devolucoesEncontradas.size() + " devoluções encontradas");
            
        } catch (Exception e) {
            SystemLogger.error("Erro ao consultar devoluções", e);
            JOptionPane.showMessageDialog(workArea, "Erro ao consultar devoluções: " + e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    /**
     * Adiciona dados de exemplo à tabela
     */
    private void adicionarDadosExemplo() {
        // Limpar tabela atual
        modelTabela.setRowCount(0);
        devolucoesEncontradas.clear();
        
        // Dados de exemplo
        Object[][] dadosExemplo = {
            {"DEV-20260509-0001", "09/05/2026", "001", "João Silva", "123.456.789-00", "Produto A", "1", "Total", "Defeito", "Aprovada", "👁️"},
            {"DEV-20260509-0002", "08/05/2026", "002", "Maria Oliveira", "987.654.321-00", "Produto B", "2", "Parcial", "Tamanho errado", "Pendente", "👁️"},
            {"DEV-20260508-0003", "07/05/2026", "003", "Carlos Alberto", "456.789.123-00", "Produto C", "1", "Troca", "Não gostou", "Concluída", "👁️"},
            {"DEV-20260508-0004", "06/05/2026", "004", "Fernanda Costa", "789.123.456-00", "Produto D", "3", "Total", "Avariado", "Rejeitada", "👁️"},
            {"DEV-20260507-0005", "05/05/2026", "005", "Roberto Dias", "321.654.987-00", "Produto E", "1", "Parcial", "Troca", "Aprovada", "👁️"}
        };
        
        for (Object[] dados : dadosExemplo) {
            modelTabela.addRow(dados);
            
            // Adicionar à lista de devoluções
            Devolucao devolucao = new Devolucao();
            devolucao.setNumero((String) dados[0]);
            devolucao.setData((String) dados[1]);
            devolucao.setNumeroVendaOriginal((String) dados[2]);
            devolucao.setCliente((String) dados[3]);
            devolucao.setCpf((String) dados[4]);
            devolucao.setProduto((String) dados[5]);
            devolucao.setQuantidade((String) dados[6]);
            devolucao.setTipo((String) dados[7]);
            devolucao.setMotivo((String) dados[8]);
            devolucao.setStatus((String) dados[9]);
            devolucoesEncontradas.add(devolucao);
        }
    }
    
    /**
     * Limpa os campos do formulário
     */
    private void limparCampos() {
        txtNumeroVendaOriginal.setText("");
        txtCliente.setText("");
        txtCPF.setText("");
        txtProduto.setText("");
        txtQuantidade.setText("1");
        txtMotivo.setText("");
        comboTipoDevolucao.setSelectedIndex(0);
        comboStatus.setSelectedIndex(0);
        txtObservacoes.setText("");
        
        SystemLogger.ui("Campos do formulário limpos");
    }
    
    /**
     * Ver detalhes da devolução selecionada
     */
    private void verDetalhesDevolucao() {
        int linhaSelecionada = tabelaDevolucoes.getSelectedRow();
        if (linhaSelecionada >= 0) {
            Devolucao devolucao = devolucoesEncontradas.get(linhaSelecionada);
            JOptionPane.showMessageDialog(workArea, 
                "Detalhes da Devolução:\n\n" +
                "Número: " + devolucao.getNumero() + "\n" +
                "Data: " + devolucao.getData() + "\n" +
                "Venda Original: " + devolucao.getNumeroVendaOriginal() + "\n" +
                "Cliente: " + devolucao.getCliente() + "\n" +
                "CPF: " + devolucao.getCpf() + "\n" +
                "Produto: " + devolucao.getProduto() + "\n" +
                "Quantidade: " + devolucao.getQuantidade() + "\n" +
                "Tipo: " + devolucao.getTipo() + "\n" +
                "Motivo: " + devolucao.getMotivo() + "\n" +
                "Status: " + devolucao.getStatus() + "\n" +
                "Observações: " + devolucao.getObservacoes(), 
                "Detalhes da Devolução", JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(workArea, "Selecione uma devolução para ver os detalhes!", "Aviso", JOptionPane.WARNING_MESSAGE);
        }
    }
    
    /**
     * Aprova a devolução selecionada
     */
    private void aprovarDevolucao() {
        int linhaSelecionada = tabelaDevolucoes.getSelectedRow();
        if (linhaSelecionada >= 0) {
            Devolucao devolucao = devolucoesEncontradas.get(linhaSelecionada);
            
            if ("Aprovada".equals(devolucao.getStatus()) || "Concluída".equals(devolucao.getStatus())) {
                JOptionPane.showMessageDialog(workArea, "Esta devolução já está aprovada ou concluída!", "Aviso", JOptionPane.WARNING_MESSAGE);
                return;
            }
            
            int resultado = JOptionPane.showConfirmDialog(
                workArea,
                "Deseja aprovar a devolução " + devolucao.getNumero() + "?\n" +
                "Cliente: " + devolucao.getCliente() + "\n" +
                "Produto: " + devolucao.getProduto(),
                "Aprovar Devolução",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE
            );
            
            if (resultado == JOptionPane.YES_OPTION) {
                devolucao.setStatus("Aprovada");
                modelTabela.setValueAt("Aprovada", linhaSelecionada, 8);
                
                JOptionPane.showMessageDialog(workArea, "Devolução aprovada com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
                SystemLogger.ui("Devolução aprovada: " + devolucao.getNumero());
            }
        } else {
            JOptionPane.showMessageDialog(workArea, "Selecione uma devolução para aprovar!", "Aviso", JOptionPane.WARNING_MESSAGE);
        }
    }
    
    /**
     * Rejeita a devolução selecionada
     */
    private void rejeitarDevolucao() {
        int linhaSelecionada = tabelaDevolucoes.getSelectedRow();
        if (linhaSelecionada >= 0) {
            Devolucao devolucao = devolucoesEncontradas.get(linhaSelecionada);
            
            if ("Rejeitada".equals(devolucao.getStatus())) {
                JOptionPane.showMessageDialog(workArea, "Esta devolução já está rejeitada!", "Aviso", JOptionPane.WARNING_MESSAGE);
                return;
            }
            
            int resultado = JOptionPane.showConfirmDialog(
                workArea,
                "Deseja rejeitar a devolução " + devolucao.getNumero() + "?\n" +
                "Cliente: " + devolucao.getCliente() + "\n" +
                "Produto: " + devolucao.getProduto(),
                "Rejeitar Devolução",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE
            );
            
            if (resultado == JOptionPane.YES_OPTION) {
                devolucao.setStatus("Rejeitada");
                modelTabela.setValueAt("Rejeitada", linhaSelecionada, 8);
                
                JOptionPane.showMessageDialog(workArea, "Devolução rejeitada com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
                SystemLogger.ui("Devolução rejeitada: " + devolucao.getNumero());
            }
        } else {
            JOptionPane.showMessageDialog(workArea, "Selecione uma devolução para rejeitar!", "Aviso", JOptionPane.WARNING_MESSAGE);
        }
    }
    
    /**
     * Imprime a devolução selecionada
     */
    private void imprimirDevolucao() {
        int linhaSelecionada = tabelaDevolucoes.getSelectedRow();
        if (linhaSelecionada >= 0) {
            Devolucao devolucao = devolucoesEncontradas.get(linhaSelecionada);
            JOptionPane.showMessageDialog(workArea, 
                "Imprimindo devolução " + devolucao.getNumero() + "...\n(Implementar impressão)", 
                "Imprimir", JOptionPane.INFORMATION_MESSAGE);
            
            SystemLogger.ui("Imprimindo devolução: " + devolucao.getNumero());
        } else {
            JOptionPane.showMessageDialog(workArea, "Selecione uma devolução para imprimir!", "Aviso", JOptionPane.WARNING_MESSAGE);
        }
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
     * Classe interna para representar uma devolução
     */
    private static class Devolucao {
        private String numero;
        private String data;
        private String numeroVendaOriginal;
        private String cliente;
        private String cpf;
        private String produto;
        private String quantidade;
        private String tipo;
        private String motivo;
        private String status;
        private String observacoes;
        
        // Getters e Setters
        public String getNumero() { return numero; }
        public void setNumero(String numero) { this.numero = numero; }
        
        public String getData() { return data; }
        public void setData(String data) { this.data = data; }
        
        public String getNumeroVendaOriginal() { return numeroVendaOriginal; }
        public void setNumeroVendaOriginal(String numeroVendaOriginal) { this.numeroVendaOriginal = numeroVendaOriginal; }
        
        public String getCliente() { return cliente; }
        public void setCliente(String cliente) { this.cliente = cliente; }
        
        public String getCpf() { return cpf; }
        public void setCpf(String cpf) { this.cpf = cpf; }
        
        public String getProduto() { return produto; }
        public void setProduto(String produto) { this.produto = produto; }
        
        public String getQuantidade() { return quantidade; }
        public void setQuantidade(String quantidade) { this.quantidade = quantidade; }
        
        public String getTipo() { return tipo; }
        public void setTipo(String tipo) { this.tipo = tipo; }
        
        public String getMotivo() { return motivo; }
        public void setMotivo(String motivo) { this.motivo = motivo; }
        
        public String getStatus() { return status; }
        public void setStatus(String status) { this.status = status; }
        
        public String getObservacoes() { return observacoes; }
        public void setObservacoes(String observacoes) { this.observacoes = observacoes; }
    }
}
