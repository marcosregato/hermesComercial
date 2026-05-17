package com.br.hermescomercial.pdv.controller;

import com.br.hermescomercial.util.SystemLogger;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Classe especializada para o formulário unificado de Gestão de Clientes
 * Estrutura: Header → Busca → Formulário → Tabela
 * Funcionalidades: Novo, Editar, Consultar em uma única tela
 */
public class PDVFormularioGestaoCliente {
    
    private JPanel workArea;
    private String usuarioAtual;
    private String nomeUsuario;
    
    // Componentes do formulário
    private JTextField txtBusca;
    private JTextField txtCodigo;
    private JTextField txtNome;
    private JTextField txtCPF;
    private JTextField txtRG;
    private JTextField txtTelefone;
    private JTextField txtCelular;
    private JTextField txtEmail;
    private JTextField txtCep;
    private JTextField txtEndereco;
    private JTextField txtNumero;
    private JTextField txtBairro;
    private JTextField txtCidade;
    private JTextField txtEstado;
    private JComboBox<String> comboStatus;
    private JTextArea txtObservacoes;
    
    // Tabela de clientes
    private JTable tabelaClientes;
    private DefaultTableModel modelTabela;
    private List<Cliente> clientesEncontrados = new ArrayList<>();
    
    // Modo de operação
    private enum ModoOperacao { NOVO, EDITAR, CONSULTAR }
    private ModoOperacao modoAtual = ModoOperacao.NOVO;
    
    // Cores
    private static final Color WHITE = Color.WHITE;
    private static final Color ACCENT_COLOR = new Color(52, 152, 219);
    private static final Color SUCCESS_COLOR = new Color(39, 174, 96);
    private static final Color DANGER_COLOR = new Color(231, 76, 60);
    private static final Color WARNING_COLOR = new Color(241, 196, 15);
    private static final Color GRAY = new Color(149, 165, 166);
    
    public PDVFormularioGestaoCliente(JPanel workArea, String usuarioAtual, String nomeUsuario) {
        this.workArea = workArea;
        this.usuarioAtual = usuarioAtual;
        this.nomeUsuario = nomeUsuario;
        this.clientesEncontrados = new ArrayList<>();
    }
    
    /**
     * Cria o formulário completo de Gestão de Clientes
     */
    public JPanel criarFormularioGestaoCliente() {
        try {
            SystemLogger.ui("=== CRIANDO FORMULÁRIO GESTÃO DE CLIENTES ===");
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
            
            // Tabela de clientes
            JPanel tabelaPanel = criarPainelTabela();
            painelCentral.add(tabelaPanel, BorderLayout.SOUTH);
            
            painelPrincipal.add(painelCentral, BorderLayout.CENTER);
            
            SystemLogger.ui("Formulário Gestão de Clientes criado com sucesso");
            return painelPrincipal;
            
        } catch (Exception e) {
            SystemLogger.error("Erro ao criar formulário Gestão de Clientes", e);
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
        JLabel titleLabel = new JLabel("👥 Gestão de Clientes");
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
        txtBusca.setToolTipText("Digite nome, CPF ou código do cliente");
        
        JButton btnBuscar = new JButton("🔍 Buscar");
        btnBuscar.setBackground(ACCENT_COLOR);
        btnBuscar.setForeground(WHITE);
        btnBuscar.setFocusPainted(false);
        btnBuscar.setBorderPainted(false);
        btnBuscar.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btnBuscar.addActionListener(e -> buscarClientes());
        
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
        
        // Seção Dados Pessoais
        JLabel pessoaisSectionLabel = new JLabel("👤 Dados Pessoais");
        pessoaisSectionLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        pessoaisSectionLabel.setForeground(ACCENT_COLOR);
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 4;
        formularioPanel.add(pessoaisSectionLabel, gbc);
        
        // Código
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
        txtCodigo.setToolTipText("Código automático do cliente");
        txtCodigo.setEditable(false);
        formularioPanel.add(txtCodigo, gbc);
        
        // Nome
        gbc.gridx = 2; gbc.weightx = 0.0;
        JLabel lblNome = new JLabel("Nome:");
        lblNome.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        formularioPanel.add(lblNome, gbc);
        
        gbc.gridx = 3; gbc.weightx = 1.0;
        txtNome = new JTextField();
        txtNome.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        txtNome.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(GRAY),
            BorderFactory.createEmptyBorder(5, 8, 5, 8)
        ));
        formularioPanel.add(txtNome, gbc);
        
        // CPF
        gbc.gridy = 2; gbc.gridx = 0;
        JLabel lblCPF = new JLabel("CPF:");
        lblCPF.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        formularioPanel.add(lblCPF, gbc);
        
        gbc.gridx = 1; gbc.weightx = 1.0;
        txtCPF = new JTextField();
        txtCPF.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        txtCPF.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(GRAY),
            BorderFactory.createEmptyBorder(5, 8, 5, 8)
        ));
        formularioPanel.add(txtCPF, gbc);
        
        // RG
        gbc.gridx = 2; gbc.weightx = 0.0;
        JLabel lblRG = new JLabel("RG:");
        lblRG.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        formularioPanel.add(lblRG, gbc);
        
        gbc.gridx = 3; gbc.weightx = 1.0;
        txtRG = new JTextField();
        txtRG.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        txtRG.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(GRAY),
            BorderFactory.createEmptyBorder(5, 8, 5, 8)
        ));
        formularioPanel.add(txtRG, gbc);
        
        // Seção Contato
        gbc.gridy = 3; gbc.gridx = 0; gbc.gridwidth = 4; gbc.weightx = 0.0;
        JLabel contatoSectionLabel = new JLabel("📞 Contato");
        contatoSectionLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        contatoSectionLabel.setForeground(ACCENT_COLOR);
        formularioPanel.add(contatoSectionLabel, gbc);
        
        // Telefone
        gbc.gridy = 4; gbc.gridwidth = 1;
        JLabel lblTelefone = new JLabel("Telefone:");
        lblTelefone.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        formularioPanel.add(lblTelefone, gbc);
        
        gbc.gridx = 1; gbc.weightx = 1.0;
        txtTelefone = new JTextField();
        txtTelefone.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        txtTelefone.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(GRAY),
            BorderFactory.createEmptyBorder(5, 8, 5, 8)
        ));
        formularioPanel.add(txtTelefone, gbc);
        
        // Celular
        gbc.gridx = 2; gbc.weightx = 0.0;
        JLabel lblCelular = new JLabel("Celular:");
        lblCelular.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        formularioPanel.add(lblCelular, gbc);
        
        gbc.gridx = 3; gbc.weightx = 1.0;
        txtCelular = new JTextField();
        txtCelular.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        txtCelular.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(GRAY),
            BorderFactory.createEmptyBorder(5, 8, 5, 8)
        ));
        formularioPanel.add(txtCelular, gbc);
        
        // Email
        gbc.gridy = 5; gbc.gridx = 0;
        JLabel lblEmail = new JLabel("Email:");
        lblEmail.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        formularioPanel.add(lblEmail, gbc);
        
        gbc.gridx = 1; gbc.gridwidth = 3; gbc.weightx = 1.0;
        txtEmail = new JTextField();
        txtEmail.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        txtEmail.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(GRAY),
            BorderFactory.createEmptyBorder(5, 8, 5, 8)
        ));
        formularioPanel.add(txtEmail, gbc);
        
        // Seção Endereço
        gbc.gridy = 6; gbc.gridx = 0; gbc.gridwidth = 4; gbc.weightx = 0.0;
        JLabel enderecoSectionLabel = new JLabel("📍 Endereço");
        enderecoSectionLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        enderecoSectionLabel.setForeground(ACCENT_COLOR);
        formularioPanel.add(enderecoSectionLabel, gbc);
        
        // CEP
        gbc.gridy = 7; gbc.gridwidth = 1;
        JLabel lblCEP = new JLabel("CEP:");
        lblCEP.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        formularioPanel.add(lblCEP, gbc);
        
        gbc.gridx = 1; gbc.weightx = 1.0;
        txtCep = new JTextField();
        txtCep.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        txtCep.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(GRAY),
            BorderFactory.createEmptyBorder(5, 8, 5, 8)
        ));
        formularioPanel.add(txtCep, gbc);
        
        // Status
        gbc.gridx = 2; gbc.weightx = 0.0;
        JLabel lblStatus = new JLabel("Status:");
        lblStatus.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        formularioPanel.add(lblStatus, gbc);
        
        gbc.gridx = 3; gbc.weightx = 1.0;
        comboStatus = new JComboBox<>(new String[]{"Ativo", "Inativo"});
        comboStatus.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        comboStatus.setBorder(BorderFactory.createLineBorder(GRAY));
        formularioPanel.add(comboStatus, gbc);
        
        // Endereço
        gbc.gridy = 8; gbc.gridx = 0;
        JLabel lblEndereco = new JLabel("Endereço:");
        lblEndereco.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        formularioPanel.add(lblEndereco, gbc);
        
        gbc.gridx = 1; gbc.gridwidth = 3; gbc.weightx = 1.0;
        txtEndereco = new JTextField();
        txtEndereco.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        txtEndereco.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(GRAY),
            BorderFactory.createEmptyBorder(5, 8, 5, 8)
        ));
        formularioPanel.add(txtEndereco, gbc);
        
        // Número
        gbc.gridy = 9; gbc.gridwidth = 1;
        JLabel lblNumero = new JLabel("Número:");
        lblNumero.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        formularioPanel.add(lblNumero, gbc);
        
        gbc.gridx = 1; gbc.weightx = 1.0;
        txtNumero = new JTextField();
        txtNumero.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        txtNumero.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(GRAY),
            BorderFactory.createEmptyBorder(5, 8, 5, 8)
        ));
        formularioPanel.add(txtNumero, gbc);
        
        // Bairro
        gbc.gridx = 2; gbc.weightx = 0.0;
        JLabel lblBairro = new JLabel("Bairro:");
        lblBairro.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        formularioPanel.add(lblBairro, gbc);
        
        gbc.gridx = 3; gbc.weightx = 1.0;
        txtBairro = new JTextField();
        txtBairro.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        txtBairro.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(GRAY),
            BorderFactory.createEmptyBorder(5, 8, 5, 8)
        ));
        formularioPanel.add(txtBairro, gbc);
        
        // Cidade
        gbc.gridy = 10; gbc.gridx = 0;
        JLabel lblCidade = new JLabel("Cidade:");
        lblCidade.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        formularioPanel.add(lblCidade, gbc);
        
        gbc.gridx = 1; gbc.weightx = 1.0;
        txtCidade = new JTextField();
        txtCidade.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        txtCidade.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(GRAY),
            BorderFactory.createEmptyBorder(5, 8, 5, 8)
        ));
        formularioPanel.add(txtCidade, gbc);
        
        // Estado
        gbc.gridx = 2; gbc.weightx = 0.0;
        JLabel lblEstado = new JLabel("Estado:");
        lblEstado.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        formularioPanel.add(lblEstado, gbc);
        
        gbc.gridx = 3; gbc.weightx = 1.0;
        txtEstado = new JTextField();
        txtEstado.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        txtEstado.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(GRAY),
            BorderFactory.createEmptyBorder(5, 8, 5, 8)
        ));
        formularioPanel.add(txtEstado, gbc);
        
        // Observações
        gbc.gridy = 11; gbc.gridx = 0;
        JLabel lblObservacoes = new JLabel("Observações:");
        lblObservacoes.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        formularioPanel.add(lblObservacoes, gbc);
        
        gbc.gridx = 1; gbc.gridwidth = 3; gbc.weightx = 1.0;
        txtObservacoes = new JTextArea(3, 30);
        txtObservacoes.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        txtObservacoes.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(GRAY),
            BorderFactory.createEmptyBorder(5, 8, 5, 8)
        ));
        txtObservacoes.setLineWrap(true);
        txtObservacoes.setWrapStyleWord(true);
        JScrollPane scrollObservacoes = new JScrollPane(txtObservacoes);
        formularioPanel.add(scrollObservacoes, gbc);
        
        // Botões de ação
        gbc.gridy = 12; gbc.gridx = 0; gbc.gridwidth = 4; gbc.weightx = 0.0;
        JPanel botoesPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        botoesPanel.setBackground(WHITE);
        
        JButton btnNovo = new JButton("➕ Novo");
        btnNovo.setBackground(SUCCESS_COLOR);
        btnNovo.setForeground(WHITE);
        btnNovo.setFocusPainted(false);
        btnNovo.setBorderPainted(false);
        btnNovo.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btnNovo.addActionListener(e -> novoCliente());
        
        JButton btnSalvar = new JButton("💾 Salvar");
        btnSalvar.setBackground(ACCENT_COLOR);
        btnSalvar.setForeground(WHITE);
        btnSalvar.setFocusPainted(false);
        btnSalvar.setBorderPainted(false);
        btnSalvar.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btnSalvar.addActionListener(e -> salvarCliente());
        
        JButton btnLimpar = new JButton("🧹 Limpar");
        btnLimpar.setBackground(GRAY);
        btnLimpar.setForeground(WHITE);
        btnLimpar.setFocusPainted(false);
        btnLimpar.setBorderPainted(false);
        btnLimpar.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btnLimpar.addActionListener(e -> limparCampos());
        
        JButton btnExcluir = new JButton("🗑️ Excluir");
        btnExcluir.setBackground(DANGER_COLOR);
        btnExcluir.setForeground(WHITE);
        btnExcluir.setFocusPainted(false);
        btnExcluir.setBorderPainted(false);
        btnExcluir.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btnExcluir.addActionListener(e -> excluirCliente());
        
        botoesPanel.add(btnNovo);
        botoesPanel.add(Box.createHorizontalStrut(10));
        botoesPanel.add(btnSalvar);
        botoesPanel.add(Box.createHorizontalStrut(10));
        botoesPanel.add(btnLimpar);
        botoesPanel.add(Box.createHorizontalStrut(10));
        botoesPanel.add(btnExcluir);
        
        formularioPanel.add(botoesPanel, gbc);
        
        return formularioPanel;
    }
    
    /**
     * Cria o painel da tabela
     */
    private JPanel criarPainelTabela() {
        JPanel tabelaPanel = new JPanel(new BorderLayout());
        tabelaPanel.setBackground(WHITE);
        tabelaPanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 20, 20));
        
        JLabel tabelaLabel = new JLabel("📋 Clientes Cadastrados");
        tabelaLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        tabelaLabel.setForeground(ACCENT_COLOR);
        
        // Modelo da tabela
        String[] colunas = {"Código", "Nome", "CPF", "Telefone", "Celular", "Status", "Data Cadastro", "Ações"};
        modelTabela = new DefaultTableModel(colunas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 7; // Apenas a coluna de ações é editável
            }
        };
        
        tabelaClientes = new JTable(modelTabela);
        tabelaClientes.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        tabelaClientes.setRowHeight(25);
        tabelaClientes.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 12));
        tabelaClientes.getTableHeader().setBackground(ACCENT_COLOR);
        tabelaClientes.getTableHeader().setForeground(WHITE);
        
        // Scroll pane
        JScrollPane scrollPane = new JScrollPane(tabelaClientes);
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
        btnEditar.addActionListener(e -> editarClienteSelecionado());
        
        JButton btnConsultar = new JButton("👁️ Consultar");
        btnConsultar.setBackground(SUCCESS_COLOR);
        btnConsultar.setForeground(WHITE);
        btnConsultar.setFocusPainted(false);
        btnConsultar.setBorderPainted(false);
        btnConsultar.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btnConsultar.addActionListener(e -> consultarClienteSelecionado());
        
        JButton btnExportar = new JButton("📄 Exportar");
        btnExportar.setBackground(WARNING_COLOR);
        btnExportar.setForeground(WHITE);
        btnExportar.setFocusPainted(false);
        btnExportar.setBorderPainted(false);
        btnExportar.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btnExportar.addActionListener(e -> exportarDados());
        
        botoesTabelaPanel.add(btnEditar);
        botoesTabelaPanel.add(Box.createHorizontalStrut(10));
        botoesTabelaPanel.add(btnConsultar);
        botoesTabelaPanel.add(Box.createHorizontalStrut(10));
        botoesTabelaPanel.add(btnExportar);
        
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
        clientesEncontrados.clear();
        
        // Dados de exemplo
        Object[][] dadosExemplo = {
            {"CLI-001", "João Silva", "123.456.789-00", "(11) 3333-4444", "(11) 98765-4321", "Ativo", "10/05/2026", "✏️"},
            {"CLI-002", "Maria Oliveira", "987.654.321-00", "(11) 5555-6666", "(11) 91234-5678", "Ativo", "09/05/2026", "✏️"},
            {"CLI-003", "Carlos Alberto", "456.789.123-00", "(11) 7777-8888", "(11) 98765-1234", "Inativo", "08/05/2026", "✏️"},
            {"CLI-004", "Fernanda Costa", "789.123.456-00", "(11) 9999-0000", "(11) 97654-3210", "Ativo", "07/05/2026", "✏️"},
            {"CLI-005", "Roberto Dias", "321.654.987-00", "(11) 2222-3333", "(11) 96543-2109", "Ativo", "06/05/2026", "✏️"}
        };
        
        for (Object[] dados : dadosExemplo) {
            modelTabela.addRow(dados);
            
            // Adicionar à lista de clientes
            Cliente cliente = new Cliente();
            cliente.setCodigo((String) dados[0]);
            cliente.setNome((String) dados[1]);
            cliente.setCpf((String) dados[2]);
            cliente.setTelefone((String) dados[3]);
            cliente.setCelular((String) dados[4]);
            cliente.setStatus((String) dados[5]);
            cliente.setDataCadastro((String) dados[6]);
            clientesEncontrados.add(cliente);
        }
    }
    
    /**
     * Busca clientes
     */
    private void buscarClientes() {
        String termo = txtBusca.getText().trim();
        if (termo.isEmpty()) {
            JOptionPane.showMessageDialog(workArea, "Digite um termo para buscar!", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        JOptionPane.showMessageDialog(workArea, 
            "Busca realizada para: " + termo + "\n" +
            "Clientes encontrados: " + clientesEncontrados.size(), 
            "Resultado", JOptionPane.INFORMATION_MESSAGE);
        
        SystemLogger.ui("Busca realizada para: " + termo);
    }
    
    /**
     * Prepara para novo cliente
     */
    private void novoCliente() {
        modoAtual = ModoOperacao.NOVO;
        limparCampos();
        txtCodigo.setText("CLI-" + String.format("%03d", clientesEncontrados.size() + 1));
        comboStatus.setSelectedItem("Ativo");
        
        JOptionPane.showMessageDialog(workArea, 
            "Formulário preparado para novo cliente!", 
            "Novo Cliente", JOptionPane.INFORMATION_MESSAGE);
        
        SystemLogger.ui("Preparando novo cliente");
    }
    
    /**
     * Salva cliente
     */
    private void salvarCliente() {
        String nome = txtNome.getText().trim();
        String cpf = txtCPF.getText().trim();
        
        if (nome.isEmpty()) {
            JOptionPane.showMessageDialog(workArea, "Digite o nome do cliente!", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        if (cpf.isEmpty()) {
            JOptionPane.showMessageDialog(workArea, "Digite o CPF do cliente!", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        String mensagem = (modoAtual == ModoOperacao.NOVO) ? 
            "Cliente cadastrado com sucesso!" : "Cliente atualizado com sucesso!";
        
        JOptionPane.showMessageDialog(workArea, 
            mensagem + "\n" +
            "Nome: " + nome + "\n" +
            "CPF: " + cpf, 
            "Sucesso", JOptionPane.INFORMATION_MESSAGE);
        
        SystemLogger.ui("Cliente salvo: " + nome);
        limparCampos();
        adicionarDadosExemplo(); // Atualizar tabela
    }
    
    /**
     * Limpa os campos do formulário
     */
    private void limparCampos() {
        txtBusca.setText("");
        txtCodigo.setText("");
        txtNome.setText("");
        txtCPF.setText("");
        txtRG.setText("");
        txtTelefone.setText("");
        txtCelular.setText("");
        txtEmail.setText("");
        txtCep.setText("");
        txtEndereco.setText("");
        txtNumero.setText("");
        txtBairro.setText("");
        txtCidade.setText("");
        txtEstado.setText("");
        txtObservacoes.setText("");
        comboStatus.setSelectedIndex(0);
    }
    
    /**
     * Edita cliente selecionado na tabela
     */
    private void editarClienteSelecionado() {
        int selectedRow = tabelaClientes.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(workArea, "Selecione um cliente para editar!", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        Cliente cliente = clientesEncontrados.get(selectedRow);
        
        // Preencher formulário com dados do cliente
        txtCodigo.setText(cliente.getCodigo());
        txtNome.setText(cliente.getNome());
        txtCPF.setText(cliente.getCpf());
        txtTelefone.setText(cliente.getTelefone());
        txtCelular.setText(cliente.getCelular());
        comboStatus.setSelectedItem(cliente.getStatus());
        
        modoAtual = ModoOperacao.EDITAR;
        
        JOptionPane.showMessageDialog(workArea, 
            "Cliente carregado para edição: " + cliente.getNome(), 
            "Editar Cliente", JOptionPane.INFORMATION_MESSAGE);
        
        SystemLogger.ui("Cliente carregado para edição: " + cliente.getNome());
    }
    
    /**
     * Consulta cliente selecionado na tabela
     */
    private void consultarClienteSelecionado() {
        int selectedRow = tabelaClientes.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(workArea, "Selecione um cliente para consultar!", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        Cliente cliente = clientesEncontrados.get(selectedRow);
        
        // Preencher formulário com dados do cliente (modo somente leitura)
        txtCodigo.setText(cliente.getCodigo());
        txtNome.setText(cliente.getNome());
        txtCPF.setText(cliente.getCpf());
        txtTelefone.setText(cliente.getTelefone());
        txtCelular.setText(cliente.getCelular());
        comboStatus.setSelectedItem(cliente.getStatus());
        
        // Desabilitar campos para consulta
        desabilitarCamposParaConsulta();
        
        modoAtual = ModoOperacao.CONSULTAR;
        
        JOptionPane.showMessageDialog(workArea, 
            "Cliente em modo de consulta: " + cliente.getNome(), 
            "Consultar Cliente", JOptionPane.INFORMATION_MESSAGE);
        
        SystemLogger.ui("Cliente em consulta: " + cliente.getNome());
    }
    
    /**
     * Desabilita campos para modo consulta
     */
    private void desabilitarCamposParaConsulta() {
        txtNome.setEditable(false);
        txtCPF.setEditable(false);
        txtRG.setEditable(false);
        txtTelefone.setEditable(false);
        txtCelular.setEditable(false);
        txtEmail.setEditable(false);
        txtCep.setEditable(false);
        txtEndereco.setEditable(false);
        txtNumero.setEditable(false);
        txtBairro.setEditable(false);
        txtCidade.setEditable(false);
        txtEstado.setEditable(false);
        txtObservacoes.setEditable(false);
        comboStatus.setEnabled(false);
    }
    
    /**
     * Habilita campos para modo edição
     */
    @SuppressWarnings("unused")
    private void habilitarCamposParaEdicao() {
        txtNome.setEditable(true);
        txtCPF.setEditable(true);
        txtRG.setEditable(true);
        txtTelefone.setEditable(true);
        txtCelular.setEditable(true);
        txtEmail.setEditable(true);
        txtCep.setEditable(true);
        txtEndereco.setEditable(true);
        txtNumero.setEditable(true);
        txtBairro.setEditable(true);
        txtCidade.setEditable(true);
        txtEstado.setEditable(true);
        txtObservacoes.setEditable(true);
        comboStatus.setEnabled(true);
    }
    
    /**
     * Exclui cliente selecionado
     */
    private void excluirCliente() {
        String nome = txtNome.getText().trim();
        if (nome.isEmpty()) {
            JOptionPane.showMessageDialog(workArea, "Selecione um cliente para excluir!", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        int confirm = JOptionPane.showConfirmDialog(workArea, 
            "Deseja realmente excluir o cliente '" + nome + "'?", 
            "Confirmar Exclusão", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
        
        if (confirm == JOptionPane.YES_OPTION) {
            JOptionPane.showMessageDialog(workArea, 
                "Cliente excluído com sucesso!", 
                "Sucesso", JOptionPane.INFORMATION_MESSAGE);
            
            SystemLogger.ui("Cliente excluído: " + nome);
            limparCampos();
            adicionarDadosExemplo(); // Atualizar tabela
        }
    }
    
    /**
     * Exporta dados da tabela
     */
    private void exportarDados() {
        JOptionPane.showMessageDialog(workArea, 
            "Exportando " + clientesEncontrados.size() + " clientes...\n(Implementar exportação para CSV/Excel)", 
            "Exportar Dados", JOptionPane.INFORMATION_MESSAGE);
        
        SystemLogger.ui("Exportando " + clientesEncontrados.size() + " clientes");
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
     * Classe interna para representar um cliente
     */
    private static class Cliente {
        private String codigo;
        private String nome;
        private String cpf;
        private String rg;
        private String telefone;
        private String celular;
        private String email;
        private String cep;
        private String endereco;
        private String numero;
        private String bairro;
        private String cidade;
        private String estado;
        private String status;
        private String dataCadastro;
        private String observacoes;
        
        // Getters e Setters
        public String getCodigo() { return codigo; }
        public void setCodigo(String codigo) { this.codigo = codigo; }
        
        public String getNome() { return nome; }
        public void setNome(String nome) { this.nome = nome; }
        
        public String getCpf() { return cpf; }
        public void setCpf(String cpf) { this.cpf = cpf; }
        
        @SuppressWarnings("unused")
        public String getRg() { return rg; }
        @SuppressWarnings("unused")
        public void setRg(String rg) { this.rg = rg; }
        
        public String getTelefone() { return telefone; }
        public void setTelefone(String telefone) { this.telefone = telefone; }
        
        public String getCelular() { return celular; }
        public void setCelular(String celular) { this.celular = celular; }
        
        @SuppressWarnings("unused")
        public String getEmail() { return email; }
        @SuppressWarnings("unused")
        public void setEmail(String email) { this.email = email; }
        
        @SuppressWarnings("unused")
        public String getCep() { return cep; }
        @SuppressWarnings("unused")
        public void setCep(String cep) { this.cep = cep; }
        
        @SuppressWarnings("unused")
        public String getEndereco() { return endereco; }
        @SuppressWarnings("unused")
        public void setEndereco(String endereco) { this.endereco = endereco; }
        
        @SuppressWarnings("unused")
        public String getNumero() { return numero; }
        @SuppressWarnings("unused")
        public void setNumero(String numero) { this.numero = numero; }
        
        @SuppressWarnings("unused")
        public String getBairro() { return bairro; }
        @SuppressWarnings("unused")
        public void setBairro(String bairro) { this.bairro = bairro; }
        
        @SuppressWarnings("unused")
        public String getCidade() { return cidade; }
        @SuppressWarnings("unused")
        public void setCidade(String cidade) { this.cidade = cidade; }
        
        @SuppressWarnings("unused")
        public String getEstado() { return estado; }
        @SuppressWarnings("unused")
        public void setEstado(String estado) { this.estado = estado; }
        
        public String getStatus() { return status; }
        public void setStatus(String status) { this.status = status; }
        
        @SuppressWarnings("unused")
        public String getDataCadastro() { return dataCadastro; }
        public void setDataCadastro(String dataCadastro) { this.dataCadastro = dataCadastro; }
        
        @SuppressWarnings("unused")
        public String getObservacoes() { return observacoes; }
        @SuppressWarnings("unused")
        public void setObservacoes(String observacoes) { this.observacoes = observacoes; }
    }
}
