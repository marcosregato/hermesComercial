package com.br.hermescomercial.pdv.controller;

import com.br.hermescomercial.util.SystemLogger;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.List;

/**
 * Classe especializada para o formulário de Orçamentos
 * Estrutura: Header → Busca → Formulário → Tabela
 */
public class PDVFormularioOrcamentos {
    
    private JPanel workArea;
    private String usuarioAtual;
    private String nomeUsuario;
    
    // Componentes do formulário
    private JTextField txtNumeroOrcamento;
    private JTextField txtCliente;
    private JTextField txtCPF;
    private JTextField txtProduto;
    private JTextField txtQuantidade;
    private JTextField txtValorUnitario;
    private JTextField txtDesconto;
    private JTextField txtValidade;
    private JComboBox<String> comboStatus;
    private JComboBox<String> comboPrioridade;
    private JTextArea txtObservacoes;
    
    // Labels de resumo
    private JLabel lblTotalItens;
    private JLabel lblSubtotal;
    private JLabel lblTotalDesconto;
    private JLabel lblTotalOrcamento;
    
    // Tabela de orçamentos
    private JTable tabelaOrcamentos;
    private DefaultTableModel modelTabela;
    private List<Orcamento> orcamentosEncontrados;
    private List<ItemOrcamento> itensOrcamentoAtual;
    
    // Cores
    private static final Color WHITE = Color.WHITE;
    private static final Color ACCENT_COLOR = new Color(52, 152, 219);
    private static final Color SUCCESS_COLOR = new Color(39, 174, 96);
    private static final Color WARNING_COLOR = new Color(241, 196, 15);
    private static final Color DANGER_COLOR = new Color(231, 76, 60);
    private static final Color GRAY = new Color(149, 165, 166);
    
    public PDVFormularioOrcamentos(JPanel workArea, String usuarioAtual, String nomeUsuario) {
        this.workArea = workArea;
        this.usuarioAtual = usuarioAtual;
        this.nomeUsuario = nomeUsuario;
        this.orcamentosEncontrados = new ArrayList<>();
        this.itensOrcamentoAtual = new ArrayList<>();
    }
    
    /**
     * Cria o formulário completo de Orçamentos
     */
    public JPanel criarFormularioOrcamentos() {
        try {
            SystemLogger.ui("=== CRIANDO FORMULÁRIO ORÇAMENTOS ===");
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
            
            // Painel do formulário de orçamento
            JPanel formularioPanel = criarPainelFormulario();
            painelCentral.add(formularioPanel, BorderLayout.CENTER);
            
            // Tabela de orçamentos
            JPanel tabelaPanel = criarPainelTabela();
            painelCentral.add(tabelaPanel, BorderLayout.SOUTH);
            
            painelPrincipal.add(painelCentral, BorderLayout.CENTER);
            
            SystemLogger.ui("Formulário Orçamentos criado com sucesso");
            return painelPrincipal;
            
        } catch (Exception e) {
            SystemLogger.error("Erro ao criar formulário Orçamentos", e);
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
        JLabel titleLabel = new JLabel("🏷️ Orçamentos");
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
        txtBuscaRapida.setToolTipText("Digite número do orçamento, cliente ou CPF para busca rápida");
        
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
     * Cria o painel do formulário de orçamento
     */
    private JPanel criarPainelFormulario() {
        JPanel formularioPanel = new JPanel(new GridBagLayout());
        formularioPanel.setBackground(WHITE);
        formularioPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
        // Seção Dados do Orçamento
        JLabel orcamentoSectionLabel = new JLabel("🏷️ Dados do Orçamento");
        orcamentoSectionLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        orcamentoSectionLabel.setForeground(ACCENT_COLOR);
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 4;
        formularioPanel.add(orcamentoSectionLabel, gbc);
        
        gbc.gridy = 1; gbc.gridwidth = 1;
        JLabel lblNumeroOrcamento = new JLabel("Nº Orçamento:");
        lblNumeroOrcamento.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        formularioPanel.add(lblNumeroOrcamento, gbc);
        
        gbc.gridx = 1; gbc.weightx = 1.0;
        txtNumeroOrcamento = new JTextField(gerarNumeroOrcamento());
        txtNumeroOrcamento.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        txtNumeroOrcamento.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(GRAY),
            BorderFactory.createEmptyBorder(5, 8, 5, 8)
        ));
        txtNumeroOrcamento.setEditable(false);
        formularioPanel.add(txtNumeroOrcamento, gbc);
        
        gbc.gridx = 2; gbc.weightx = 0.0;
        JLabel lblValidade = new JLabel("Validade (dias):");
        lblValidade.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        formularioPanel.add(lblValidade, gbc);
        
        gbc.gridx = 3; gbc.weightx = 1.0;
        txtValidade = new JTextField("7");
        txtValidade.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        txtValidade.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(GRAY),
            BorderFactory.createEmptyBorder(5, 8, 5, 8)
        ));
        txtValidade.setHorizontalAlignment(JTextField.RIGHT);
        formularioPanel.add(txtValidade, gbc);
        
        // Seção Cliente
        gbc.gridy = 2; gbc.gridx = 0; gbc.gridwidth = 4; gbc.weightx = 0.0;
        JLabel clienteSectionLabel = new JLabel("👤 Dados do Cliente");
        clienteSectionLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        clienteSectionLabel.setForeground(ACCENT_COLOR);
        formularioPanel.add(clienteSectionLabel, gbc);
        
        gbc.gridy = 3; gbc.gridwidth = 1;
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
        gbc.gridy = 4; gbc.gridx = 0; gbc.gridwidth = 4; gbc.weightx = 0.0;
        JLabel produtoSectionLabel = new JLabel("📦 Dados do Produto");
        produtoSectionLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        produtoSectionLabel.setForeground(ACCENT_COLOR);
        formularioPanel.add(produtoSectionLabel, gbc);
        
        gbc.gridy = 5; gbc.gridwidth = 1;
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
        
        gbc.gridy = 6; gbc.gridx = 0;
        JLabel lblValorUnitario = new JLabel("Valor Unit.:");
        lblValorUnitario.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        formularioPanel.add(lblValorUnitario, gbc);
        
        gbc.gridx = 1; gbc.weightx = 1.0;
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
        
        gbc.gridx = 3; gbc.weightx = 1.0;
        txtDesconto = new JTextField("0");
        txtDesconto.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        txtDesconto.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(GRAY),
            BorderFactory.createEmptyBorder(5, 8, 5, 8)
        ));
        txtDesconto.setHorizontalAlignment(JTextField.RIGHT);
        formularioPanel.add(txtDesconto, gbc);
        
        // Seção Status e Prioridade
        gbc.gridy = 7; gbc.gridx = 0; gbc.gridwidth = 4; gbc.weightx = 0.0;
        JLabel statusSectionLabel = new JLabel("⚙️ Status e Prioridade");
        statusSectionLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        statusSectionLabel.setForeground(ACCENT_COLOR);
        formularioPanel.add(statusSectionLabel, gbc);
        
        gbc.gridy = 8; gbc.gridwidth = 1;
        JLabel lblStatus = new JLabel("Status:");
        lblStatus.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        formularioPanel.add(lblStatus, gbc);
        
        gbc.gridx = 1; gbc.weightx = 1.0;
        comboStatus = new JComboBox<>(new String[]{"Em Elaboração", "Enviado", "Aprovado", "Rejeitado", "Expirado"});
        comboStatus.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        comboStatus.setBorder(BorderFactory.createLineBorder(GRAY));
        formularioPanel.add(comboStatus, gbc);
        
        gbc.gridx = 2; gbc.weightx = 0.0;
        JLabel lblPrioridade = new JLabel("Prioridade:");
        lblPrioridade.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        formularioPanel.add(lblPrioridade, gbc);
        
        gbc.gridx = 3; gbc.weightx = 1.0;
        comboPrioridade = new JComboBox<>(new String[]{"Baixa", "Normal", "Alta", "Urgente"});
        comboPrioridade.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        comboPrioridade.setBorder(BorderFactory.createLineBorder(GRAY));
        formularioPanel.add(comboPrioridade, gbc);
        
        // Seção Observações
        gbc.gridy = 9; gbc.gridx = 0; gbc.gridwidth = 4; gbc.weightx = 0.0;
        JLabel obsSectionLabel = new JLabel("📄 Observações");
        obsSectionLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        obsSectionLabel.setForeground(ACCENT_COLOR);
        formularioPanel.add(obsSectionLabel, gbc);
        
        gbc.gridy = 10; gbc.gridx = 0; gbc.gridwidth = 4; gbc.weightx = 1.0; gbc.fill = GridBagConstraints.BOTH;
        txtObservacoes = new JTextArea(3, 50);
        txtObservacoes.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        txtObservacoes.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(GRAY),
            BorderFactory.createEmptyBorder(8, 8, 8, 8)
        ));
        txtObservacoes.setLineWrap(true);
        txtObservacoes.setWrapStyleWord(true);
        txtObservacoes.setToolTipText("Observações adicionais sobre o orçamento");
        
        JScrollPane scrollObs = new JScrollPane(txtObservacoes);
        scrollObs.setPreferredSize(new Dimension(0, 80));
        formularioPanel.add(scrollObs, gbc);
        
        // Botões de ação
        gbc.gridy = 11; gbc.gridx = 0; gbc.gridwidth = 4; gbc.weightx = 0.0; gbc.fill = GridBagConstraints.HORIZONTAL;
        JPanel botoesPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        botoesPanel.setBackground(WHITE);
        
        JButton btnAdicionarItem = new JButton("➕ Adicionar Item");
        btnAdicionarItem.setBackground(SUCCESS_COLOR);
        btnAdicionarItem.setForeground(WHITE);
        btnAdicionarItem.setFocusPainted(false);
        btnAdicionarItem.setBorderPainted(false);
        btnAdicionarItem.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btnAdicionarItem.addActionListener(e -> adicionarItem());
        
        JButton btnLimpar = new JButton("🗑️ Limpar");
        btnLimpar.setBackground(GRAY);
        btnLimpar.setForeground(WHITE);
        btnLimpar.setFocusPainted(false);
        btnLimpar.setBorderPainted(false);
        btnLimpar.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btnLimpar.addActionListener(e -> limparCampos());
        
        JButton btnSalvar = new JButton("💾 Salvar Orçamento");
        btnSalvar.setBackground(ACCENT_COLOR);
        btnSalvar.setForeground(WHITE);
        btnSalvar.setFocusPainted(false);
        btnSalvar.setBorderPainted(false);
        btnSalvar.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btnSalvar.addActionListener(e -> salvarOrcamento());
        
        JButton btnConsultar = new JButton("🔍 Consultar");
        btnConsultar.setBackground(WARNING_COLOR);
        btnConsultar.setForeground(WHITE);
        btnConsultar.setFocusPainted(false);
        btnConsultar.setBorderPainted(false);
        btnConsultar.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btnConsultar.addActionListener(e -> consultarOrcamentos());
        
        botoesPanel.add(btnAdicionarItem);
        botoesPanel.add(Box.createHorizontalStrut(10));
        botoesPanel.add(btnLimpar);
        botoesPanel.add(Box.createHorizontalStrut(10));
        botoesPanel.add(btnSalvar);
        botoesPanel.add(Box.createHorizontalStrut(10));
        botoesPanel.add(btnConsultar);
        
        formularioPanel.add(botoesPanel, gbc);
        
        // Painel de resumo
        gbc.gridy = 12; gbc.gridx = 0; gbc.gridwidth = 4; gbc.weightx = 0.0; gbc.fill = GridBagConstraints.HORIZONTAL;
        JPanel resumoPanel = new JPanel(new GridLayout(4, 2, 10, 5));
        resumoPanel.setBackground(new Color(240, 240, 240));
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
        
        resumoPanel.add(new JLabel("Total Desconto:"));
        lblTotalDesconto = new JLabel("R$ 0,00");
        lblTotalDesconto.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lblTotalDesconto.setHorizontalAlignment(JLabel.RIGHT);
        resumoPanel.add(lblTotalDesconto);
        
        resumoPanel.add(new JLabel("TOTAL ORÇAMENTO:"));
        lblTotalOrcamento = new JLabel("R$ 0,00");
        lblTotalOrcamento.setFont(new Font("Segoe UI", Font.BOLD, 18));
        lblTotalOrcamento.setForeground(SUCCESS_COLOR);
        lblTotalOrcamento.setHorizontalAlignment(JLabel.RIGHT);
        resumoPanel.add(lblTotalOrcamento);
        
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
     * Gera número automático para orçamento
     */
    private String gerarNumeroOrcamento() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        String data = sdf.format(new Date());
        Random random = new Random();
        int numero = random.nextInt(10000);
        return "ORC-" + data + "-" + String.format("%04d", numero);
    }
    
    /**
     * Cria o painel da tabela de orçamentos
     */
    private JPanel criarPainelTabela() {
        JPanel tabelaPanel = new JPanel(new BorderLayout());
        tabelaPanel.setBackground(WHITE);
        tabelaPanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 20, 20));
        
        JLabel tabelaLabel = new JLabel("📋 Histórico de Orçamentos");
        tabelaLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        tabelaLabel.setForeground(ACCENT_COLOR);
        
        // Modelo da tabela
        String[] colunas = {"Nº Orçamento", "Data", "Cliente", "CPF", "Total", "Status", "Prioridade", "Validade", "Ações"};
        modelTabela = new DefaultTableModel(colunas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 8; // Apenas a coluna de ações é editável
            }
        };
        
        tabelaOrcamentos = new JTable(modelTabela);
        tabelaOrcamentos.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        tabelaOrcamentos.setRowHeight(25);
        tabelaOrcamentos.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 12));
        tabelaOrcamentos.getTableHeader().setBackground(ACCENT_COLOR);
        tabelaOrcamentos.getTableHeader().setForeground(WHITE);
        
        // Scroll pane
        JScrollPane scrollPane = new JScrollPane(tabelaOrcamentos);
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
        btnDetalhes.addActionListener(e -> verDetalhesOrcamento());
        
        JButton btnAprovar = new JButton("✅ Aprovar");
        btnAprovar.setBackground(SUCCESS_COLOR);
        btnAprovar.setForeground(WHITE);
        btnAprovar.setFocusPainted(false);
        btnAprovar.setBorderPainted(false);
        btnAprovar.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btnAprovar.addActionListener(e -> aprovarOrcamento());
        
        JButton btnRejeitar = new JButton("❌ Rejeitar");
        btnRejeitar.setBackground(DANGER_COLOR);
        btnRejeitar.setForeground(WHITE);
        btnRejeitar.setFocusPainted(false);
        btnRejeitar.setBorderPainted(false);
        btnRejeitar.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btnRejeitar.addActionListener(e -> rejeitarOrcamento());
        
        JButton btnConverter = new JButton("🔄 Converter em Venda");
        btnConverter.setBackground(WARNING_COLOR);
        btnConverter.setForeground(WHITE);
        btnConverter.setFocusPainted(false);
        btnConverter.setBorderPainted(false);
        btnConverter.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btnConverter.addActionListener(e -> converterEmVenda());
        
        JButton btnImprimir = new JButton("🖨️ Imprimir");
        btnImprimir.setBackground(GRAY);
        btnImprimir.setForeground(WHITE);
        btnImprimir.setFocusPainted(false);
        btnImprimir.setBorderPainted(false);
        btnImprimir.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btnImprimir.addActionListener(e -> imprimirOrcamento());
        
        botoesTabelaPanel.add(btnDetalhes);
        botoesTabelaPanel.add(Box.createHorizontalStrut(10));
        botoesTabelaPanel.add(btnAprovar);
        botoesTabelaPanel.add(Box.createHorizontalStrut(10));
        botoesTabelaPanel.add(btnRejeitar);
        botoesTabelaPanel.add(Box.createHorizontalStrut(10));
        botoesTabelaPanel.add(btnConverter);
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
     * Adiciona um item ao orçamento
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
            ItemOrcamento item = new ItemOrcamento(
                itensOrcamentoAtual.size() + 1,
                produto,
                quantidade,
                valorUnitario,
                desconto,
                totalComDesconto
            );
            itensOrcamentoAtual.add(item);
            
            // Atualizar totais
            calcularTotais();
            
            // Limpar campos do produto
            txtProduto.setText("");
            txtQuantidade.setText("1");
            txtValorUnitario.setText("");
            txtDesconto.setText("0");
            txtProduto.requestFocus();
            
            SystemLogger.ui("Item adicionado ao orçamento: " + produto + " - Qtd: " + quantidade);
            
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(workArea, "Verifique os valores digitados!", "Erro", JOptionPane.ERROR_MESSAGE);
        } catch (Exception e) {
            SystemLogger.error("Erro ao adicionar item", e);
            JOptionPane.showMessageDialog(workArea, "Erro ao adicionar item: " + e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    /**
     * Calcula os totais do orçamento
     */
    private void calcularTotais() {
        try {
            BigDecimal subtotal = BigDecimal.ZERO;
            BigDecimal totalDesconto = BigDecimal.ZERO;
            int totalItens = 0;
            
            for (ItemOrcamento item : itensOrcamentoAtual) {
                subtotal = subtotal.add(item.getTotal());
                totalDesconto = totalDesconto.add(item.getValorUnitario()
                    .multiply(new BigDecimal(item.getQuantidade()))
                    .multiply(item.getDesconto()).divide(new BigDecimal(100), 2, RoundingMode.HALF_UP));
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
                    totalDesconto = totalDesconto.add(valorDesconto);
                    totalItens += quantidade;
                } catch (Exception e) {
                    // Ignora erros de cálculo do item atual
                }
            }
            
            BigDecimal totalOrcamento = subtotal.subtract(totalDesconto);
            
            // Atualizar labels
            lblTotalItens.setText(String.valueOf(totalItens));
            lblSubtotal.setText("R$ " + formatarValor(subtotal));
            lblTotalDesconto.setText("R$ " + formatarValor(totalDesconto));
            lblTotalOrcamento.setText("R$ " + formatarValor(totalOrcamento));
            
        } catch (Exception e) {
            SystemLogger.error("Erro ao calcular totais", e);
        }
    }
    
    /**
     * Formata valor monetário
     */
    private String formatarValor(BigDecimal valor) {
        return valor.setScale(2, RoundingMode.HALF_UP).toString().replace(".", ",");
    }
    
    /**
     * Limpa os campos do formulário
     */
    private void limparCampos() {
        txtNumeroOrcamento.setText(gerarNumeroOrcamento());
        txtCliente.setText("");
        txtCPF.setText("");
        txtProduto.setText("");
        txtQuantidade.setText("1");
        txtValorUnitario.setText("");
        txtDesconto.setText("0");
        txtValidade.setText("7");
        comboStatus.setSelectedIndex(0);
        comboPrioridade.setSelectedIndex(1);
        txtObservacoes.setText("");
        
        // Limpar itens
        itensOrcamentoAtual.clear();
        calcularTotais();
        
        SystemLogger.ui("Campos do formulário limpos");
    }
    
    /**
     * Salva o orçamento
     */
    private void salvarOrcamento() {
        try {
            // Validações
            if (txtCliente.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(workArea, "Informe o nome do cliente!", "Aviso", JOptionPane.WARNING_MESSAGE);
                return;
            }
            
            if (itensOrcamentoAtual.isEmpty()) {
                JOptionPane.showMessageDialog(workArea, "Adicione itens ao orçamento!", "Aviso", JOptionPane.WARNING_MESSAGE);
                return;
            }
            
            // Coletar dados
            String numeroOrcamento = txtNumeroOrcamento.getText().trim();
            String cliente = txtCliente.getText().trim();
            String cpf = txtCPF.getText().trim();
            String validade = txtValidade.getText().trim();
            String status = (String) comboStatus.getSelectedItem();
            String prioridade = (String) comboPrioridade.getSelectedItem();
            String observacoes = txtObservacoes.getText().trim();
            String total = lblTotalOrcamento.getText();
            
            
            // Adicionar à tabela
            Object[] rowData = {
                numeroOrcamento,
                new SimpleDateFormat("dd/MM/yyyy").format(new Date()),
                cliente,
                cpf,
                total,
                status,
                prioridade,
                validade + " dias",
                "👁️"
            };
            modelTabela.addRow(rowData);
            
            // Adicionar à lista
            Orcamento orcamento = new Orcamento();
            orcamento.setNumero(numeroOrcamento);
            orcamento.setData(new SimpleDateFormat("dd/MM/yyyy").format(new Date()));
            orcamento.setCliente(cliente);
            orcamento.setCpf(cpf);
            orcamento.setTotal(total);
            orcamento.setStatus(status);
            orcamento.setPrioridade(prioridade);
            orcamento.setValidade(validade);
            orcamento.setObservacoes(observacoes);
            orcamento.setItens(new ArrayList<>(itensOrcamentoAtual));
            orcamentosEncontrados.add(orcamento);
            
            // Limpar formulário
            limparCampos();
            
            JOptionPane.showMessageDialog(workArea, 
                "Orçamento salvo com sucesso!\n" +
                "Número: " + numeroOrcamento + "\n" +
                "Cliente: " + cliente + "\n" +
                "Total: " + total, 
                "Sucesso", JOptionPane.INFORMATION_MESSAGE);
            
            SystemLogger.ui("Orçamento salvo: " + numeroOrcamento);
            
        } catch (Exception e) {
            SystemLogger.error("Erro ao salvar orçamento", e);
            JOptionPane.showMessageDialog(workArea, "Erro ao salvar orçamento: " + e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    /**
     * Consulta orçamentos existentes
     */
    private void consultarOrcamentos() {
        try {
            // Por enquanto, vamos adicionar dados de exemplo
            adicionarDadosExemplo();
            
            JOptionPane.showMessageDialog(workArea, 
                "Consulta realizada com sucesso!\n" +
                "Orçamentos encontrados: " + orcamentosEncontrados.size(), 
                "Resultado", JOptionPane.INFORMATION_MESSAGE);
            
            SystemLogger.ui("Consulta realizada - " + orcamentosEncontrados.size() + " orçamentos encontrados");
            
        } catch (Exception e) {
            SystemLogger.error("Erro ao consultar orçamentos", e);
            JOptionPane.showMessageDialog(workArea, "Erro ao consultar orçamentos: " + e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    /**
     * Adiciona dados de exemplo à tabela
     */
    private void adicionarDadosExemplo() {
        // Limpar tabela atual
        modelTabela.setRowCount(0);
        orcamentosEncontrados.clear();
        
        // Dados de exemplo
        Object[][] dadosExemplo = {
            {"ORC-20260509-0001", "09/05/2026", "João Silva", "123.456.789-00", "R$ 1.250,00", "Aprovado", "Normal", "7 dias", "👁️"},
            {"ORC-20260508-0002", "08/05/2026", "Maria Oliveira", "987.654.321-00", "R$ 3.450,00", "Enviado", "Alta", "15 dias", "👁️"},
            {"ORC-20260507-0003", "07/05/2026", "Carlos Alberto", "456.789.123-00", "R$ 890,00", "Expirado", "Baixa", "5 dias", "👁️"},
            {"ORC-20260506-0004", "06/05/2026", "Fernanda Costa", "789.123.456-00", "R$ 2.100,00", "Aprovado", "Urgente", "10 dias", "👁️"},
            {"ORC-20260505-0005", "05/05/2026", "Roberto Dias", "321.654.987-00", "R$ 4.780,00", "Em Elaboração", "Normal", "30 dias", "👁️"}
        };
        
        for (Object[] dados : dadosExemplo) {
            modelTabela.addRow(dados);
            
            // Adicionar à lista de orçamentos
            Orcamento orcamento = new Orcamento();
            orcamento.setNumero((String) dados[0]);
            orcamento.setData((String) dados[1]);
            orcamento.setCliente((String) dados[2]);
            orcamento.setCpf((String) dados[3]);
            orcamento.setTotal((String) dados[4]);
            orcamento.setStatus((String) dados[5]);
            orcamento.setPrioridade((String) dados[6]);
            orcamento.setValidade((String) dados[7]);
            orcamentosEncontrados.add(orcamento);
        }
    }
    
    /**
     * Ver detalhes do orçamento selecionado
     */
    private void verDetalhesOrcamento() {
        int linhaSelecionada = tabelaOrcamentos.getSelectedRow();
        if (linhaSelecionada >= 0) {
            Orcamento orcamento = orcamentosEncontrados.get(linhaSelecionada);
            JOptionPane.showMessageDialog(workArea, 
                "Detalhes do Orçamento:\n\n" +
                "Número: " + orcamento.getNumero() + "\n" +
                "Data: " + orcamento.getData() + "\n" +
                "Cliente: " + orcamento.getCliente() + "\n" +
                "CPF: " + orcamento.getCpf() + "\n" +
                "Total: " + orcamento.getTotal() + "\n" +
                "Status: " + orcamento.getStatus() + "\n" +
                "Prioridade: " + orcamento.getPrioridade() + "\n" +
                "Validade: " + orcamento.getValidade() + "\n" +
                "Observações: " + orcamento.getObservacoes(), 
                "Detalhes do Orçamento", JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(workArea, "Selecione um orçamento para ver os detalhes!", "Aviso", JOptionPane.WARNING_MESSAGE);
        }
    }
    
    /**
     * Aprova o orçamento selecionado
     */
    private void aprovarOrcamento() {
        int linhaSelecionada = tabelaOrcamentos.getSelectedRow();
        if (linhaSelecionada >= 0) {
            Orcamento orcamento = orcamentosEncontrados.get(linhaSelecionada);
            
            if ("Aprovado".equals(orcamento.getStatus())) {
                JOptionPane.showMessageDialog(workArea, "Este orçamento já está aprovado!", "Aviso", JOptionPane.WARNING_MESSAGE);
                return;
            }
            
            int resultado = JOptionPane.showConfirmDialog(
                workArea,
                "Deseja aprovar o orçamento " + orcamento.getNumero() + "?\n" +
                "Cliente: " + orcamento.getCliente() + "\n" +
                "Total: " + orcamento.getTotal(),
                "Aprovar Orçamento",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE
            );
            
            if (resultado == JOptionPane.YES_OPTION) {
                orcamento.setStatus("Aprovado");
                modelTabela.setValueAt("Aprovado", linhaSelecionada, 5);
                
                JOptionPane.showMessageDialog(workArea, "Orçamento aprovado com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
                SystemLogger.ui("Orçamento aprovado: " + orcamento.getNumero());
            }
        } else {
            JOptionPane.showMessageDialog(workArea, "Selecione um orçamento para aprovar!", "Aviso", JOptionPane.WARNING_MESSAGE);
        }
    }
    
    /**
     * Rejeita o orçamento selecionado
     */
    private void rejeitarOrcamento() {
        int linhaSelecionada = tabelaOrcamentos.getSelectedRow();
        if (linhaSelecionada >= 0) {
            Orcamento orcamento = orcamentosEncontrados.get(linhaSelecionada);
            
            if ("Rejeitado".equals(orcamento.getStatus())) {
                JOptionPane.showMessageDialog(workArea, "Este orçamento já está rejeitado!", "Aviso", JOptionPane.WARNING_MESSAGE);
                return;
            }
            
            int resultado = JOptionPane.showConfirmDialog(
                workArea,
                "Deseja rejeitar o orçamento " + orcamento.getNumero() + "?\n" +
                "Cliente: " + orcamento.getCliente() + "\n" +
                "Total: " + orcamento.getTotal(),
                "Rejeitar Orçamento",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE
            );
            
            if (resultado == JOptionPane.YES_OPTION) {
                orcamento.setStatus("Rejeitado");
                modelTabela.setValueAt("Rejeitado", linhaSelecionada, 5);
                
                JOptionPane.showMessageDialog(workArea, "Orçamento rejeitado com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
                SystemLogger.ui("Orçamento rejeitado: " + orcamento.getNumero());
            }
        } else {
            JOptionPane.showMessageDialog(workArea, "Selecione um orçamento para rejeitar!", "Aviso", JOptionPane.WARNING_MESSAGE);
        }
    }
    
    /**
     * Converte o orçamento em venda
     */
    private void converterEmVenda() {
        int linhaSelecionada = tabelaOrcamentos.getSelectedRow();
        if (linhaSelecionada >= 0) {
            Orcamento orcamento = orcamentosEncontrados.get(linhaSelecionada);
            
            if (!"Aprovado".equals(orcamento.getStatus())) {
                JOptionPane.showMessageDialog(workArea, "Apenas orçamentos aprovados podem ser convertidos em venda!", "Aviso", JOptionPane.WARNING_MESSAGE);
                return;
            }
            
            int resultado = JOptionPane.showConfirmDialog(
                workArea,
                "Deseja converter o orçamento " + orcamento.getNumero() + " em venda?\n" +
                "Cliente: " + orcamento.getCliente() + "\n" +
                "Total: " + orcamento.getTotal(),
                "Converter em Venda",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE
            );
            
            if (resultado == JOptionPane.YES_OPTION) {
                orcamento.setStatus("Convertido");
                modelTabela.setValueAt("Convertido", linhaSelecionada, 5);
                
                JOptionPane.showMessageDialog(workArea, "Orçamento convertido em venda com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
                SystemLogger.ui("Orçamento convertido em venda: " + orcamento.getNumero());
            }
        } else {
            JOptionPane.showMessageDialog(workArea, "Selecione um orçamento para converter!", "Aviso", JOptionPane.WARNING_MESSAGE);
        }
    }
    
    /**
     * Imprime o orçamento selecionado
     */
    private void imprimirOrcamento() {
        int linhaSelecionada = tabelaOrcamentos.getSelectedRow();
        if (linhaSelecionada >= 0) {
            Orcamento orcamento = orcamentosEncontrados.get(linhaSelecionada);
            JOptionPane.showMessageDialog(workArea, 
                "Imprimindo orçamento " + orcamento.getNumero() + "...\n(Implementar impressão)", 
                "Imprimir", JOptionPane.INFORMATION_MESSAGE);
            
            SystemLogger.ui("Imprimindo orçamento: " + orcamento.getNumero());
        } else {
            JOptionPane.showMessageDialog(workArea, "Selecione um orçamento para imprimir!", "Aviso", JOptionPane.WARNING_MESSAGE);
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
     * Classe interna para representar um orçamento
     */
    private static class Orcamento {
        private String numero;
        private String data;
        private String cliente;
        private String cpf;
        private String total;
        private String status;
        private String prioridade;
        private String validade;
        private String observacoes;
        private List<ItemOrcamento> itens;
        
        // Getters e Setters
        public String getNumero() { return numero; }
        public void setNumero(String numero) { this.numero = numero; }
        
        public String getData() { return data; }
        public void setData(String data) { this.data = data; }
        
        public String getCliente() { return cliente; }
        public void setCliente(String cliente) { this.cliente = cliente; }
        
        public String getCpf() { return cpf; }
        public void setCpf(String cpf) { this.cpf = cpf; }
        
        public String getTotal() { return total; }
        public void setTotal(String total) { this.total = total; }
        
        public String getStatus() { return status; }
        public void setStatus(String status) { this.status = status; }
        
        public String getPrioridade() { return prioridade; }
        public void setPrioridade(String prioridade) { this.prioridade = prioridade; }
        
        public String getValidade() { return validade; }
        public void setValidade(String validade) { this.validade = validade; }
        
        public String getObservacoes() { return observacoes; }
        public void setObservacoes(String observacoes) { this.observacoes = observacoes; }
        
        @SuppressWarnings("unused")
        public List<ItemOrcamento> getItens() { return itens; }
        public void setItens(List<ItemOrcamento> itens) { this.itens = itens; }
    }
    
    /**
     * Classe interna para representar um item do orçamento
     */
    private static class ItemOrcamento {
        private int codigo;
        private String produto;
        private int quantidade;
        private BigDecimal valorUnitario;
        private BigDecimal desconto;
        private BigDecimal total;
        
        public ItemOrcamento(int codigo, String produto, int quantidade, BigDecimal valorUnitario, BigDecimal desconto, BigDecimal total) {
            this.codigo = codigo;
            this.produto = produto;
            this.quantidade = quantidade;
            this.valorUnitario = valorUnitario;
            this.desconto = desconto;
            this.total = total;
        }
        
        // Getters e Setters
        @SuppressWarnings("unused")
        public int getCodigo() { return codigo; }
        @SuppressWarnings("unused")
        public void setCodigo(int codigo) { this.codigo = codigo; }
        
        @SuppressWarnings("unused")
        public String getProduto() { return produto; }
        @SuppressWarnings("unused")
        public void setProduto(String produto) { this.produto = produto; }
        
        public int getQuantidade() { return quantidade; }
        @SuppressWarnings("unused")
        public void setQuantidade(int quantidade) { this.quantidade = quantidade; }
        
        public BigDecimal getValorUnitario() { return valorUnitario; }
        @SuppressWarnings("unused")
        public void setValorUnitario(BigDecimal valorUnitario) { this.valorUnitario = valorUnitario; }
        
        public BigDecimal getDesconto() { return desconto; }
        @SuppressWarnings("unused")
        public void setDesconto(BigDecimal desconto) { this.desconto = desconto; }
        
        public BigDecimal getTotal() { return total; }
        @SuppressWarnings("unused")
        public void setTotal(BigDecimal total) { this.total = total; }
    }
}
