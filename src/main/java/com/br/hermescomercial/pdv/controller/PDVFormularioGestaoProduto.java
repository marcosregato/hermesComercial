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
 * Classe especializada para o formulário de Gestão de Produtos
 * Estrutura: Header → Busca → Formulário → Tabela
 * Unifica Cadastrar Produto e Editar Produto em uma só tela
 */
public class PDVFormularioGestaoProduto {
    
    private JPanel workArea;
    private String usuarioAtual;
    private String nomeUsuario;
    
    // Componentes do formulário
    private JTextField txtCodigo;
    private JTextField txtNome;
    private JTextArea txtDescricao;
    private JTextField txtCategoria;
    private JTextField txtMarca;
    private JTextField txtModelo;
    private JTextField txtCor;
    private JTextField txtTamanho;
    private JTextField txtQuantidade;
    private JTextField txtQuantidadeMinima;
    private JTextField txtPrecoCusto;
    private JTextField txtPrecoVenda;
    private JTextField txtPrecoAtacado;
    private JTextField txtFornecedor;
    private JTextField txtCNPJFornecedor;
    private JTextField txtLocalizacao;
    private JComboBox<String> comboUnidade;
    private JComboBox<String> comboStatus;
    private JTextArea txtObservacoes;
    
    // Tabela de produtos
    private JTable tabelaProdutos;
    private DefaultTableModel modelTabela;
    private List<Produto> produtosEncontrados;
    
    // Cores
    private static final Color WHITE = Color.WHITE;
    private static final Color ACCENT_COLOR = new Color(52, 152, 219);
    private static final Color SUCCESS_COLOR = new Color(39, 174, 96);
    private static final Color WARNING_COLOR = new Color(241, 196, 15);
    private static final Color DANGER_COLOR = new Color(231, 76, 60);
    private static final Color GRAY = new Color(149, 165, 166);
    
    public PDVFormularioGestaoProduto(JPanel workArea, String usuarioAtual, String nomeUsuario) {
        this.workArea = workArea;
        this.usuarioAtual = usuarioAtual;
        this.nomeUsuario = nomeUsuario;
        this.produtosEncontrados = new ArrayList<>();
    }
    
    /**
     * Cria o formulário completo de Gestão de Produtos
     */
    public JPanel criarFormularioGestaoProduto() {
        try {
            SystemLogger.ui("=== CRIANDO FORMULÁRIO GESTÃO PRODUTOS ===");
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
            
            // Painel do formulário de produto
            JPanel formularioPanel = criarPainelFormulario();
            painelCentral.add(formularioPanel, BorderLayout.CENTER);
            
            // Tabela de produtos
            JPanel tabelaPanel = criarPainelTabela();
            painelCentral.add(tabelaPanel, BorderLayout.SOUTH);
            
            painelPrincipal.add(painelCentral, BorderLayout.CENTER);
            
            SystemLogger.ui("Formulário Gestão Produtos criado com sucesso");
            return painelPrincipal;
            
        } catch (Exception e) {
            SystemLogger.error("Erro ao criar formulário Gestão Produtos", e);
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
        JLabel titleLabel = new JLabel("📦 Gestão de Produtos");
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
        txtBuscaRapida.setToolTipText("Digite código, nome, categoria ou marca para busca rápida");
        
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
     * Cria o painel do formulário de produto
     */
    private JPanel criarPainelFormulario() {
        JPanel formularioPanel = new JPanel(new GridBagLayout());
        formularioPanel.setBackground(WHITE);
        formularioPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
        // Seção Identificação
        JLabel identificacaoSectionLabel = new JLabel("📋 Identificação do Produto");
        identificacaoSectionLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        identificacaoSectionLabel.setForeground(ACCENT_COLOR);
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 4;
        formularioPanel.add(identificacaoSectionLabel, gbc);
        
        gbc.gridy = 1; gbc.gridwidth = 1;
        JLabel lblCodigo = new JLabel("Código:");
        lblCodigo.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        formularioPanel.add(lblCodigo, gbc);
        
        gbc.gridx = 1; gbc.weightx = 1.0;
        txtCodigo = new JTextField();
        txtCodigo.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        txtCodigo.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(GRAY),
            BorderFactory.createEmptyBorder(5, 8, 5, 8)
        ));
        txtCodigo.setToolTipText("Código único do produto");
        formularioPanel.add(txtCodigo, gbc);
        
        gbc.gridx = 2; gbc.weightx = 0.0;
        JLabel lblStatus = new JLabel("Status:");
        lblStatus.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        formularioPanel.add(lblStatus, gbc);
        
        gbc.gridx = 3; gbc.weightx = 1.0;
        comboStatus = new JComboBox<>(new String[]{"Ativo", "Inativo", "Descontinuado"});
        comboStatus.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        comboStatus.setBorder(BorderFactory.createLineBorder(GRAY));
        formularioPanel.add(comboStatus, gbc);
        
        gbc.gridy = 2; gbc.gridx = 0; gbc.gridwidth = 4; gbc.weightx = 0.0;
        JLabel nomeSectionLabel = new JLabel("📝 Dados Básicos");
        nomeSectionLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        nomeSectionLabel.setForeground(ACCENT_COLOR);
        formularioPanel.add(nomeSectionLabel, gbc);
        
        gbc.gridy = 3; gbc.gridwidth = 2;
        JLabel lblNome = new JLabel("Nome do Produto:");
        lblNome.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        formularioPanel.add(lblNome, gbc);
        
        gbc.gridx = 2; gbc.gridwidth = 2;
        JLabel lblCategoria = new JLabel("Categoria:");
        lblCategoria.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        formularioPanel.add(lblCategoria, gbc);
        
        gbc.gridy = 4; gbc.gridx = 0; gbc.gridwidth = 2; gbc.weightx = 1.0;
        txtNome = new JTextField();
        txtNome.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        txtNome.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(GRAY),
            BorderFactory.createEmptyBorder(5, 8, 5, 8)
        ));
        formularioPanel.add(txtNome, gbc);
        
        gbc.gridx = 2; gbc.gridwidth = 2; gbc.weightx = 1.0;
        txtCategoria = new JTextField();
        txtCategoria.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        txtCategoria.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(GRAY),
            BorderFactory.createEmptyBorder(5, 8, 5, 8)
        ));
        formularioPanel.add(txtCategoria, gbc);
        
        gbc.gridy = 5; gbc.gridx = 0; gbc.gridwidth = 4; gbc.weightx = 0.0;
        JLabel descricaoSectionLabel = new JLabel("📄 Descrição");
        descricaoSectionLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        descricaoSectionLabel.setForeground(ACCENT_COLOR);
        formularioPanel.add(descricaoSectionLabel, gbc);
        
        gbc.gridy = 6; gbc.gridx = 0; gbc.gridwidth = 4; gbc.weightx = 1.0; gbc.fill = GridBagConstraints.BOTH;
        txtDescricao = new JTextArea(3, 50);
        txtDescricao.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        txtDescricao.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(GRAY),
            BorderFactory.createEmptyBorder(8, 8, 8, 8)
        ));
        txtDescricao.setLineWrap(true);
        txtDescricao.setWrapStyleWord(true);
        txtDescricao.setToolTipText("Descrição detalhada do produto");
        
        JScrollPane scrollDescricao = new JScrollPane(txtDescricao);
        scrollDescricao.setPreferredSize(new Dimension(0, 80));
        formularioPanel.add(scrollDescricao, gbc);
        
        // Seção Especificações
        gbc.gridy = 7; gbc.gridx = 0; gbc.gridwidth = 4; gbc.weightx = 0.0; gbc.fill = GridBagConstraints.HORIZONTAL;
        JLabel especificacoesSectionLabel = new JLabel("⚙️ Especificações");
        especificacoesSectionLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        especificacoesSectionLabel.setForeground(ACCENT_COLOR);
        formularioPanel.add(especificacoesSectionLabel, gbc);
        
        gbc.gridy = 8; gbc.gridwidth = 1;
        JLabel lblMarca = new JLabel("Marca:");
        lblMarca.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        formularioPanel.add(lblMarca, gbc);
        
        gbc.gridx = 1; gbc.weightx = 1.0;
        txtMarca = new JTextField();
        txtMarca.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        txtMarca.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(GRAY),
            BorderFactory.createEmptyBorder(5, 8, 5, 8)
        ));
        formularioPanel.add(txtMarca, gbc);
        
        gbc.gridx = 2; gbc.weightx = 0.0;
        JLabel lblModelo = new JLabel("Modelo:");
        lblModelo.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        formularioPanel.add(lblModelo, gbc);
        
        gbc.gridx = 3; gbc.weightx = 1.0;
        txtModelo = new JTextField();
        txtModelo.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        txtModelo.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(GRAY),
            BorderFactory.createEmptyBorder(5, 8, 5, 8)
        ));
        formularioPanel.add(txtModelo, gbc);
        
        gbc.gridy = 9; gbc.gridx = 0;
        JLabel lblCor = new JLabel("Cor:");
        lblCor.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        formularioPanel.add(lblCor, gbc);
        
        gbc.gridx = 1; gbc.weightx = 1.0;
        txtCor = new JTextField();
        txtCor.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        txtCor.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(GRAY),
            BorderFactory.createEmptyBorder(5, 8, 5, 8)
        ));
        formularioPanel.add(txtCor, gbc);
        
        gbc.gridx = 2; gbc.weightx = 0.0;
        JLabel lblTamanho = new JLabel("Tamanho:");
        lblTamanho.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        formularioPanel.add(lblTamanho, gbc);
        
        gbc.gridx = 3; gbc.weightx = 1.0;
        txtTamanho = new JTextField();
        txtTamanho.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        txtTamanho.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(GRAY),
            BorderFactory.createEmptyBorder(5, 8, 5, 8)
        ));
        formularioPanel.add(txtTamanho, gbc);
        
        gbc.gridy = 10; gbc.gridx = 0;
        JLabel lblUnidade = new JLabel("Unidade:");
        lblUnidade.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        formularioPanel.add(lblUnidade, gbc);
        
        gbc.gridx = 1; gbc.weightx = 1.0;
        comboUnidade = new JComboBox<>(new String[]{"UN", "KG", "L", "CX", "PCT", "MT", "M²", "M³"});
        comboUnidade.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        comboUnidade.setBorder(BorderFactory.createLineBorder(GRAY));
        formularioPanel.add(comboUnidade, gbc);
        
        gbc.gridx = 2; gbc.weightx = 0.0;
        JLabel lblLocalizacao = new JLabel("Localização:");
        lblLocalizacao.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        formularioPanel.add(lblLocalizacao, gbc);
        
        gbc.gridx = 3; gbc.weightx = 1.0;
        txtLocalizacao = new JTextField();
        txtLocalizacao.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        txtLocalizacao.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(GRAY),
            BorderFactory.createEmptyBorder(5, 8, 5, 8)
        ));
        formularioPanel.add(txtLocalizacao, gbc);
        
        // Seção Estoque
        gbc.gridy = 11; gbc.gridx = 0; gbc.gridwidth = 4; gbc.weightx = 0.0;
        JLabel estoqueSectionLabel = new JLabel("📦 Estoque");
        estoqueSectionLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        estoqueSectionLabel.setForeground(ACCENT_COLOR);
        formularioPanel.add(estoqueSectionLabel, gbc);
        
        gbc.gridy = 12; gbc.gridwidth = 1;
        JLabel lblQuantidade = new JLabel("Quantidade:");
        lblQuantidade.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        formularioPanel.add(lblQuantidade, gbc);
        
        gbc.gridx = 1; gbc.weightx = 1.0;
        txtQuantidade = new JTextField("0");
        txtQuantidade.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        txtQuantidade.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(GRAY),
            BorderFactory.createEmptyBorder(5, 8, 5, 8)
        ));
        txtQuantidade.setHorizontalAlignment(JTextField.RIGHT);
        formularioPanel.add(txtQuantidade, gbc);
        
        gbc.gridx = 2; gbc.weightx = 0.0;
        JLabel lblQuantidadeMinima = new JLabel("Estoque Mínimo:");
        lblQuantidadeMinima.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        formularioPanel.add(lblQuantidadeMinima, gbc);
        
        gbc.gridx = 3; gbc.weightx = 1.0;
        txtQuantidadeMinima = new JTextField("0");
        txtQuantidadeMinima.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        txtQuantidadeMinima.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(GRAY),
            BorderFactory.createEmptyBorder(5, 8, 5, 8)
        ));
        txtQuantidadeMinima.setHorizontalAlignment(JTextField.RIGHT);
        formularioPanel.add(txtQuantidadeMinima, gbc);
        
        // Seção Preços
        gbc.gridy = 13; gbc.gridx = 0; gbc.gridwidth = 4; gbc.weightx = 0.0;
        JLabel precosSectionLabel = new JLabel("💰 Preços");
        precosSectionLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        precosSectionLabel.setForeground(ACCENT_COLOR);
        formularioPanel.add(precosSectionLabel, gbc);
        
        gbc.gridy = 14; gbc.gridwidth = 1;
        JLabel lblPrecoCusto = new JLabel("Preço Custo:");
        lblPrecoCusto.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        formularioPanel.add(lblPrecoCusto, gbc);
        
        gbc.gridx = 1; gbc.weightx = 1.0;
        txtPrecoCusto = new JTextField("0,00");
        txtPrecoCusto.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        txtPrecoCusto.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(GRAY),
            BorderFactory.createEmptyBorder(5, 8, 5, 8)
        ));
        txtPrecoCusto.setHorizontalAlignment(JTextField.RIGHT);
        formularioPanel.add(txtPrecoCusto, gbc);
        
        gbc.gridx = 2; gbc.weightx = 0.0;
        JLabel lblPrecoVenda = new JLabel("Preço Venda:");
        lblPrecoVenda.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        formularioPanel.add(lblPrecoVenda, gbc);
        
        gbc.gridx = 3; gbc.weightx = 1.0;
        txtPrecoVenda = new JTextField("0,00");
        txtPrecoVenda.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        txtPrecoVenda.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(GRAY),
            BorderFactory.createEmptyBorder(5, 8, 5, 8)
        ));
        txtPrecoVenda.setHorizontalAlignment(JTextField.RIGHT);
        formularioPanel.add(txtPrecoVenda, gbc);
        
        gbc.gridy = 15; gbc.gridx = 0;
        JLabel lblPrecoAtacado = new JLabel("Preço Atacado:");
        lblPrecoAtacado.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        formularioPanel.add(lblPrecoAtacado, gbc);
        
        gbc.gridx = 1; gbc.weightx = 1.0;
        txtPrecoAtacado = new JTextField("0,00");
        txtPrecoAtacado.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        txtPrecoAtacado.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(GRAY),
            BorderFactory.createEmptyBorder(5, 8, 5, 8)
        ));
        txtPrecoAtacado.setHorizontalAlignment(JTextField.RIGHT);
        formularioPanel.add(txtPrecoAtacado, gbc);
        
        // Seção Fornecedor
        gbc.gridy = 16; gbc.gridx = 0; gbc.gridwidth = 4; gbc.weightx = 0.0;
        JLabel fornecedorSectionLabel = new JLabel("🏢 Fornecedor");
        fornecedorSectionLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        fornecedorSectionLabel.setForeground(ACCENT_COLOR);
        formularioPanel.add(fornecedorSectionLabel, gbc);
        
        gbc.gridy = 17; gbc.gridwidth = 1;
        JLabel lblFornecedor = new JLabel("Nome:");
        lblFornecedor.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        formularioPanel.add(lblFornecedor, gbc);
        
        gbc.gridx = 1; gbc.weightx = 1.0;
        txtFornecedor = new JTextField();
        txtFornecedor.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        txtFornecedor.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(GRAY),
            BorderFactory.createEmptyBorder(5, 8, 5, 8)
        ));
        formularioPanel.add(txtFornecedor, gbc);
        
        gbc.gridx = 2; gbc.weightx = 0.0;
        JLabel lblCNPJFornecedor = new JLabel("CNPJ:");
        lblCNPJFornecedor.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        formularioPanel.add(lblCNPJFornecedor, gbc);
        
        gbc.gridx = 3; gbc.weightx = 1.0;
        txtCNPJFornecedor = new JTextField();
        txtCNPJFornecedor.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        txtCNPJFornecedor.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(GRAY),
            BorderFactory.createEmptyBorder(5, 8, 5, 8)
        ));
        formularioPanel.add(txtCNPJFornecedor, gbc);
        
        // Seção Observações
        gbc.gridy = 18; gbc.gridx = 0; gbc.gridwidth = 4; gbc.weightx = 0.0;
        JLabel obsSectionLabel = new JLabel("📄 Observações Adicionais");
        obsSectionLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        obsSectionLabel.setForeground(ACCENT_COLOR);
        formularioPanel.add(obsSectionLabel, gbc);
        
        gbc.gridy = 19; gbc.gridx = 0; gbc.gridwidth = 4; gbc.weightx = 1.0; gbc.fill = GridBagConstraints.BOTH;
        txtObservacoes = new JTextArea(3, 50);
        txtObservacoes.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        txtObservacoes.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(GRAY),
            BorderFactory.createEmptyBorder(8, 8, 8, 8)
        ));
        txtObservacoes.setLineWrap(true);
        txtObservacoes.setWrapStyleWord(true);
        txtObservacoes.setToolTipText("Observações adicionais sobre o produto");
        
        JScrollPane scrollObs = new JScrollPane(txtObservacoes);
        scrollObs.setPreferredSize(new Dimension(0, 80));
        formularioPanel.add(scrollObs, gbc);
        
        // Botões de ação
        gbc.gridy = 20; gbc.gridx = 0; gbc.gridwidth = 4; gbc.weightx = 0.0; gbc.fill = GridBagConstraints.HORIZONTAL;
        JPanel botoesPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        botoesPanel.setBackground(WHITE);
        
        JButton btnSalvar = new JButton("💾 Salvar Produto");
        btnSalvar.setBackground(SUCCESS_COLOR);
        btnSalvar.setForeground(WHITE);
        btnSalvar.setFocusPainted(false);
        btnSalvar.setBorderPainted(false);
        btnSalvar.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btnSalvar.addActionListener(e -> salvarProduto());
        
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
        btnConsultar.addActionListener(e -> consultarProdutos());
        
        JButton btnExcluir = new JButton("❌ Excluir");
        btnExcluir.setBackground(DANGER_COLOR);
        btnExcluir.setForeground(WHITE);
        btnExcluir.setFocusPainted(false);
        btnExcluir.setBorderPainted(false);
        btnExcluir.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btnExcluir.addActionListener(e -> excluirProduto());
        
        botoesPanel.add(btnSalvar);
        botoesPanel.add(Box.createHorizontalStrut(10));
        botoesPanel.add(btnLimpar);
        botoesPanel.add(Box.createHorizontalStrut(10));
        botoesPanel.add(btnConsultar);
        botoesPanel.add(Box.createHorizontalStrut(10));
        botoesPanel.add(btnExcluir);
        
        formularioPanel.add(botoesPanel, gbc);
        
        return formularioPanel;
    }
    
    /**
     * Cria o painel da tabela de produtos
     */
    private JPanel criarPainelTabela() {
        JPanel tabelaPanel = new JPanel(new BorderLayout());
        tabelaPanel.setBackground(WHITE);
        tabelaPanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 20, 20));
        
        JLabel tabelaLabel = new JLabel("📋 Produtos Cadastrados");
        tabelaLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        tabelaLabel.setForeground(ACCENT_COLOR);
        
        // Modelo da tabela
        String[] colunas = {"Código", "Nome", "Categoria", "Marca", "Quantidade", "Preço Venda", "Status", "Ações"};
        modelTabela = new DefaultTableModel(colunas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 7; // Apenas a coluna de ações é editável
            }
        };
        
        tabelaProdutos = new JTable(modelTabela);
        tabelaProdutos.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        tabelaProdutos.setRowHeight(25);
        tabelaProdutos.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 12));
        tabelaProdutos.getTableHeader().setBackground(ACCENT_COLOR);
        tabelaProdutos.getTableHeader().setForeground(WHITE);
        
        // Scroll pane
        JScrollPane scrollPane = new JScrollPane(tabelaProdutos);
        scrollPane.setPreferredSize(new Dimension(0, 250));
        scrollPane.setBorder(BorderFactory.createLineBorder(GRAY));
        
        // Painel de botões da tabela
        JPanel botoesTabelaPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        botoesTabelaPanel.setBackground(WHITE);
        
        JButton btnEditar = new JButton("✏️ Editar");
        btnEditar.setBackground(ACCENT_COLOR);
        btnEditar.setForeground(WHITE);
        btnEditar.setFocusPainted(false);
        btnEditar.setBorderPainted(false);
        btnEditar.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btnEditar.addActionListener(e -> editarProduto());
        
        JButton btnAtivar = new JButton("✅ Ativar");
        btnAtivar.setBackground(SUCCESS_COLOR);
        btnAtivar.setForeground(WHITE);
        btnAtivar.setFocusPainted(false);
        btnAtivar.setBorderPainted(false);
        btnAtivar.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btnAtivar.addActionListener(e -> ativarProduto());
        
        JButton btnInativar = new JButton("⏸️ Inativar");
        btnInativar.setBackground(WARNING_COLOR);
        btnInativar.setForeground(WHITE);
        btnInativar.setFocusPainted(false);
        btnInativar.setBorderPainted(false);
        btnInativar.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btnInativar.addActionListener(e -> inativarProduto());
        
        JButton btnImprimir = new JButton("🖨️ Imprimir");
        btnImprimir.setBackground(GRAY);
        btnImprimir.setForeground(WHITE);
        btnImprimir.setFocusPainted(false);
        btnImprimir.setBorderPainted(false);
        btnImprimir.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btnImprimir.addActionListener(e -> imprimirEtiqueta());
        
        botoesTabelaPanel.add(btnEditar);
        botoesTabelaPanel.add(Box.createHorizontalStrut(10));
        botoesTabelaPanel.add(btnAtivar);
        botoesTabelaPanel.add(Box.createHorizontalStrut(10));
        botoesTabelaPanel.add(btnInativar);
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
        
        // TODO: Implementar lógica de busca rápida no banco de dados
        JOptionPane.showMessageDialog(workArea, "Busca rápida por: " + termo + "\n(Implementar busca no banco)", "Busca Rápida", JOptionPane.INFORMATION_MESSAGE);
    }
    
    /**
     * Salva um produto (novo ou editado)
     */
    private void salvarProduto() {
        try {
            // Validações
            if (txtNome.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(workArea, "Informe o nome do produto!", "Aviso", JOptionPane.WARNING_MESSAGE);
                return;
            }
            
            if (txtCategoria.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(workArea, "Informe a categoria do produto!", "Aviso", JOptionPane.WARNING_MESSAGE);
                return;
            }
            
            if (txtPrecoVenda.getText().trim().isEmpty() || txtPrecoVenda.getText().trim().equals("0,00")) {
                JOptionPane.showMessageDialog(workArea, "Informe o preço de venda!", "Aviso", JOptionPane.WARNING_MESSAGE);
                return;
            }
            
            // Coletar dados
            String codigo = txtCodigo.getText().trim().isEmpty() ? gerarCodigo() : txtCodigo.getText().trim();
            String nome = txtNome.getText().trim();
            String descricao = txtDescricao.getText().trim();
            String categoria = txtCategoria.getText().trim();
            String marca = txtMarca.getText().trim();
            String modelo = txtModelo.getText().trim();
            String cor = txtCor.getText().trim();
            String tamanho = txtTamanho.getText().trim();
            String unidade = (String) comboUnidade.getSelectedItem();
            String localizacao = txtLocalizacao.getText().trim();
            int quantidade = Integer.parseInt(txtQuantidade.getText().trim().isEmpty() ? "0" : txtQuantidade.getText().trim());
            int quantidadeMinima = Integer.parseInt(txtQuantidadeMinima.getText().trim().isEmpty() ? "0" : txtQuantidadeMinima.getText().trim());
            BigDecimal precoCusto = new BigDecimal(txtPrecoCusto.getText().trim().replace(",", "."));
            BigDecimal precoVenda = new BigDecimal(txtPrecoVenda.getText().trim().replace(",", "."));
            BigDecimal precoAtacado = new BigDecimal(txtPrecoAtacado.getText().trim().replace(",", "."));
            String fornecedor = txtFornecedor.getText().trim();
            String cnpjFornecedor = txtCNPJFornecedor.getText().trim();
            String status = (String) comboStatus.getSelectedItem();
            String observacoes = txtObservacoes.getText().trim();
            
            // TODO: Implementar lógica de salvamento no banco de dados
            
            // Adicionar à tabela
            Object[] rowData = {
                codigo,
                nome,
                categoria,
                marca,
                quantidade,
                "R$ " + formatarValor(precoVenda),
                status,
                "👁️"
            };
            modelTabela.addRow(rowData);
            
            // Adicionar à lista
            Produto produto = new Produto();
            produto.setCodigo(codigo);
            produto.setNome(nome);
            produto.setDescricao(descricao);
            produto.setCategoria(categoria);
            produto.setMarca(marca);
            produto.setModelo(modelo);
            produto.setCor(cor);
            produto.setTamanho(tamanho);
            produto.setUnidade(unidade);
            produto.setLocalizacao(localizacao);
            produto.setQuantidade(quantidade);
            produto.setQuantidadeMinima(quantidadeMinima);
            produto.setPrecoCusto(precoCusto);
            produto.setPrecoVenda(precoVenda);
            produto.setPrecoAtacado(precoAtacado);
            produto.setFornecedor(fornecedor);
            produto.setCnpjFornecedor(cnpjFornecedor);
            produto.setStatus(status);
            produto.setObservacoes(observacoes);
            produtosEncontrados.add(produto);
            
            // Limpar formulário
            limparCampos();
            
            JOptionPane.showMessageDialog(workArea, 
                "Produto salvo com sucesso!\n" +
                "Código: " + codigo + "\n" +
                "Nome: " + nome + "\n" +
                "Categoria: " + categoria, 
                "Sucesso", JOptionPane.INFORMATION_MESSAGE);
            
            SystemLogger.ui("Produto salvo: " + codigo);
            
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(workArea, "Verifique os valores numéricos!", "Erro", JOptionPane.ERROR_MESSAGE);
        } catch (Exception e) {
            SystemLogger.error("Erro ao salvar produto", e);
            JOptionPane.showMessageDialog(workArea, "Erro ao salvar produto: " + e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    /**
     * Gera código automático para produto
     */
    private String gerarCodigo() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        String data = sdf.format(new Date());
        Random random = new Random();
        int numero = random.nextInt(10000);
        return "PROD-" + data + "-" + String.format("%04d", numero);
    }
    
    /**
     * Formata valor monetário
     */
    private String formatarValor(BigDecimal valor) {
        return valor.setScale(2, RoundingMode.HALF_UP).toString().replace(".", ",");
    }
    
    /**
     * Consulta produtos existentes
     */
    private void consultarProdutos() {
        try {
            // TODO: Implementar lógica de consulta no banco de dados
            // Por enquanto, vamos adicionar dados de exemplo
            adicionarDadosExemplo();
            
            JOptionPane.showMessageDialog(workArea, 
                "Consulta realizada com sucesso!\n" +
                "Produtos encontrados: " + produtosEncontrados.size(), 
                "Resultado", JOptionPane.INFORMATION_MESSAGE);
            
            SystemLogger.ui("Consulta realizada - " + produtosEncontrados.size() + " produtos encontrados");
            
        } catch (Exception e) {
            SystemLogger.error("Erro ao consultar produtos", e);
            JOptionPane.showMessageDialog(workArea, "Erro ao consultar produtos: " + e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    /**
     * Limpa os campos do formulário
     */
    private void limparCampos() {
        txtCodigo.setText("");
        txtNome.setText("");
        txtDescricao.setText("");
        txtCategoria.setText("");
        txtMarca.setText("");
        txtModelo.setText("");
        txtCor.setText("");
        txtTamanho.setText("");
        txtQuantidade.setText("0");
        txtQuantidadeMinima.setText("0");
        txtPrecoCusto.setText("0,00");
        txtPrecoVenda.setText("0,00");
        txtPrecoAtacado.setText("0,00");
        txtFornecedor.setText("");
        txtCNPJFornecedor.setText("");
        txtLocalizacao.setText("");
        comboUnidade.setSelectedIndex(0);
        comboStatus.setSelectedIndex(0);
        txtObservacoes.setText("");
        
        SystemLogger.ui("Campos do formulário limpos");
    }
    
    /**
     * Edita o produto selecionado
     */
    private void editarProduto() {
        int linhaSelecionada = tabelaProdutos.getSelectedRow();
        if (linhaSelecionada >= 0) {
            Produto produto = produtosEncontrados.get(linhaSelecionada);
            
            // Preencher formulário com dados do produto
            txtCodigo.setText(produto.getCodigo());
            txtNome.setText(produto.getNome());
            txtDescricao.setText(produto.getDescricao());
            txtCategoria.setText(produto.getCategoria());
            txtMarca.setText(produto.getMarca());
            txtModelo.setText(produto.getModelo());
            txtCor.setText(produto.getCor());
            txtTamanho.setText(produto.getTamanho());
            comboUnidade.setSelectedItem(produto.getUnidade());
            txtLocalizacao.setText(produto.getLocalizacao());
            txtQuantidade.setText(String.valueOf(produto.getQuantidade()));
            txtQuantidadeMinima.setText(String.valueOf(produto.getQuantidadeMinima()));
            txtPrecoCusto.setText(formatarValor(produto.getPrecoCusto()));
            txtPrecoVenda.setText(formatarValor(produto.getPrecoVenda()));
            txtPrecoAtacado.setText(formatarValor(produto.getPrecoAtacado()));
            txtFornecedor.setText(produto.getFornecedor());
            txtCNPJFornecedor.setText(produto.getCnpjFornecedor());
            comboStatus.setSelectedItem(produto.getStatus());
            txtObservacoes.setText(produto.getObservacoes());
            
            JOptionPane.showMessageDialog(workArea, "Produto carregado para edição!", "Editar Produto", JOptionPane.INFORMATION_MESSAGE);
            SystemLogger.ui("Produto carregado para edição: " + produto.getCodigo());
        } else {
            JOptionPane.showMessageDialog(workArea, "Selecione um produto para editar!", "Aviso", JOptionPane.WARNING_MESSAGE);
        }
    }
    
    /**
     * Exclui o produto selecionado
     */
    private void excluirProduto() {
        int linhaSelecionada = tabelaProdutos.getSelectedRow();
        if (linhaSelecionada >= 0) {
            Produto produto = produtosEncontrados.get(linhaSelecionada);
            
            int resultado = JOptionPane.showConfirmDialog(
                workArea,
                "Deseja excluir o produto " + produto.getCodigo() + "?\n" +
                "Nome: " + produto.getNome() + "\n" +
                "Categoria: " + produto.getCategoria(),
                "Excluir Produto",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE
            );
            
            if (resultado == JOptionPane.YES_OPTION) {
                // TODO: Implementar lógica de exclusão no banco de dados
                produtosEncontrados.remove(linhaSelecionada);
                modelTabela.removeRow(linhaSelecionada);
                
                JOptionPane.showMessageDialog(workArea, "Produto excluído com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
                SystemLogger.ui("Produto excluído: " + produto.getCodigo());
            }
        } else {
            JOptionPane.showMessageDialog(workArea, "Selecione um produto para excluir!", "Aviso", JOptionPane.WARNING_MESSAGE);
        }
    }
    
    /**
     * Ativa o produto selecionado
     */
    private void ativarProduto() {
        int linhaSelecionada = tabelaProdutos.getSelectedRow();
        if (linhaSelecionada >= 0) {
            Produto produto = produtosEncontrados.get(linhaSelecionada);
            
            if ("Ativo".equals(produto.getStatus())) {
                JOptionPane.showMessageDialog(workArea, "Este produto já está ativo!", "Aviso", JOptionPane.WARNING_MESSAGE);
                return;
            }
            
            // TODO: Implementar lógica de ativação no banco de dados
            produto.setStatus("Ativo");
            modelTabela.setValueAt("Ativo", linhaSelecionada, 6);
            
            JOptionPane.showMessageDialog(workArea, "Produto ativado com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
            SystemLogger.ui("Produto ativado: " + produto.getCodigo());
        } else {
            JOptionPane.showMessageDialog(workArea, "Selecione um produto para ativar!", "Aviso", JOptionPane.WARNING_MESSAGE);
        }
    }
    
    /**
     * Inativa o produto selecionado
     */
    private void inativarProduto() {
        int linhaSelecionada = tabelaProdutos.getSelectedRow();
        if (linhaSelecionada >= 0) {
            Produto produto = produtosEncontrados.get(linhaSelecionada);
            
            if ("Inativo".equals(produto.getStatus())) {
                JOptionPane.showMessageDialog(workArea, "Este produto já está inativo!", "Aviso", JOptionPane.WARNING_MESSAGE);
                return;
            }
            
            // TODO: Implementar lógica de inativação no banco de dados
            produto.setStatus("Inativo");
            modelTabela.setValueAt("Inativo", linhaSelecionada, 6);
            
            JOptionPane.showMessageDialog(workArea, "Produto inativado com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
            SystemLogger.ui("Produto inativado: " + produto.getCodigo());
        } else {
            JOptionPane.showMessageDialog(workArea, "Selecione um produto para inativar!", "Aviso", JOptionPane.WARNING_MESSAGE);
        }
    }
    
    /**
     * Imprime etiqueta do produto selecionado
     */
    private void imprimirEtiqueta() {
        int linhaSelecionada = tabelaProdutos.getSelectedRow();
        if (linhaSelecionada >= 0) {
            Produto produto = produtosEncontrados.get(linhaSelecionada);
            JOptionPane.showMessageDialog(workArea, 
                "Imprimindo etiqueta do produto " + produto.getCodigo() + "...\n(Implementar impressão)", 
                "Imprimir Etiqueta", JOptionPane.INFORMATION_MESSAGE);
            
            SystemLogger.ui("Imprimindo etiqueta: " + produto.getCodigo());
        } else {
            JOptionPane.showMessageDialog(workArea, "Selecione um produto para imprimir!", "Aviso", JOptionPane.WARNING_MESSAGE);
        }
    }
    
    /**
     * Adiciona dados de exemplo à tabela
     */
    private void adicionarDadosExemplo() {
        // Limpar tabela atual
        modelTabela.setRowCount(0);
        produtosEncontrados.clear();
        
        // Dados de exemplo
        Object[][] dadosExemplo = {
            {"PROD-20260509-0001", "Notebook Dell Inspiron", "Informática", "Dell", "15", "R$ 3.500,00", "Ativo", "👁️"},
            {"PROD-20260508-0002", "Mouse Wireless Logitech", "Informática", "Logitech", "50", "R$ 89,90", "Ativo", "👁️"},
            {"PROD-20260507-0003", "Cadeira Executiva", "Móveis", "Office", "8", "R$ 450,00", "Ativo", "👁️"},
            {"PROD-20260506-0004", "Impressora HP DeskJet", "Informática", "HP", "12", "R$ 680,00", "Inativo", "👁️"},
            {"PROD-20260505-0005", "Smartphone Samsung Galaxy", "Celulares", "Samsung", "25", "R$ 1.200,00", "Ativo", "👁️"}
        };
        
        for (Object[] dados : dadosExemplo) {
            modelTabela.addRow(dados);
            
            // Adicionar à lista de produtos
            Produto produto = new Produto();
            produto.setCodigo((String) dados[0]);
            produto.setNome((String) dados[1]);
            produto.setCategoria((String) dados[2]);
            produto.setMarca((String) dados[3]);
            produto.setQuantidade(Integer.parseInt((String) dados[4]));
            produto.setPrecoVenda(new BigDecimal(((String) dados[5]).replace("R$ ", "").replace(",", ".")));
            produto.setStatus((String) dados[6]);
            produtosEncontrados.add(produto);
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
     * Classe interna para representar um produto
     */
    private static class Produto {
        private String codigo;
        private String nome;
        private String descricao;
        private String categoria;
        private String marca;
        private String modelo;
        private String cor;
        private String tamanho;
        private String unidade;
        private String localizacao;
        private int quantidade;
        private int quantidadeMinima;
        private BigDecimal precoCusto;
        private BigDecimal precoVenda;
        private BigDecimal precoAtacado;
        private String fornecedor;
        private String cnpjFornecedor;
        private String status;
        private String observacoes;
        
        // Getters e Setters
        public String getCodigo() { return codigo; }
        public void setCodigo(String codigo) { this.codigo = codigo; }
        
        public String getNome() { return nome; }
        public void setNome(String nome) { this.nome = nome; }
        
        public String getDescricao() { return descricao; }
        public void setDescricao(String descricao) { this.descricao = descricao; }
        
        public String getCategoria() { return categoria; }
        public void setCategoria(String categoria) { this.categoria = categoria; }
        
        public String getMarca() { return marca; }
        public void setMarca(String marca) { this.marca = marca; }
        
        public String getModelo() { return modelo; }
        public void setModelo(String modelo) { this.modelo = modelo; }
        
        public String getCor() { return cor; }
        public void setCor(String cor) { this.cor = cor; }
        
        public String getTamanho() { return tamanho; }
        public void setTamanho(String tamanho) { this.tamanho = tamanho; }
        
        public String getUnidade() { return unidade; }
        public void setUnidade(String unidade) { this.unidade = unidade; }
        
        public String getLocalizacao() { return localizacao; }
        public void setLocalizacao(String localizacao) { this.localizacao = localizacao; }
        
        public int getQuantidade() { return quantidade; }
        public void setQuantidade(int quantidade) { this.quantidade = quantidade; }
        
        public int getQuantidadeMinima() { return quantidadeMinima; }
        public void setQuantidadeMinima(int quantidadeMinima) { this.quantidadeMinima = quantidadeMinima; }
        
        public BigDecimal getPrecoCusto() { return precoCusto; }
        public void setPrecoCusto(BigDecimal precoCusto) { this.precoCusto = precoCusto; }
        
        public BigDecimal getPrecoVenda() { return precoVenda; }
        public void setPrecoVenda(BigDecimal precoVenda) { this.precoVenda = precoVenda; }
        
        public BigDecimal getPrecoAtacado() { return precoAtacado; }
        public void setPrecoAtacado(BigDecimal precoAtacado) { this.precoAtacado = precoAtacado; }
        
        public String getFornecedor() { return fornecedor; }
        public void setFornecedor(String fornecedor) { this.fornecedor = fornecedor; }
        
        public String getCnpjFornecedor() { return cnpjFornecedor; }
        public void setCnpjFornecedor(String cnpjFornecedor) { this.cnpjFornecedor = cnpjFornecedor; }
        
        public String getStatus() { return status; }
        public void setStatus(String status) { this.status = status; }
        
        public String getObservacoes() { return observacoes; }
        public void setObservacoes(String observacoes) { this.observacoes = observacoes; }
    }
}
