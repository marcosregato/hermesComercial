package com.br.hermescomercial.pdv.controller;

import com.br.hermescomercial.util.SystemLogger;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Classe especializada para o formulário de Configurações da Loja
 * Estrutura: Header → Busca → Formulário → Tabela
 */
public class PDVFormularioLoja {
    
    private JPanel workArea;
    private String usuarioAtual;
    private String nomeUsuario;
    
    // Componentes do formulário
    private JTextField txtBusca;
    private JTextField txtNomeLoja;
    private JTextField txtCnpj;
    private JTextField txtInscricaoEstadual;
    private JTextField txtTelefone;
    private JTextField txtEmail;
    private JTextField txtEndereco;
    private JTextField txtCidade;
    private JTextField txtEstado;
    private JTextField txtCep;
    
    // Tabela de configurações
    private JTable tabelaConfiguracoes;
    private DefaultTableModel modelTabela;
    private List<ConfiguracaoLoja> configuracoesEncontradas;
    
    // Cores
    private static final Color WHITE = Color.WHITE;
    private static final Color ACCENT_COLOR = new Color(52, 152, 219);
    private static final Color SUCCESS_COLOR = new Color(39, 174, 96);
    private static final Color DANGER_COLOR = new Color(231, 76, 60);
    private static final Color GRAY = new Color(149, 165, 166);
    
    public PDVFormularioLoja(JPanel workArea, String usuarioAtual, String nomeUsuario) {
        this.workArea = workArea;
        this.usuarioAtual = usuarioAtual;
        this.nomeUsuario = nomeUsuario;
        this.configuracoesEncontradas = new ArrayList<>();
    }
    
    /**
     * Cria o formulário completo de Configurações da Loja
     */
    public JPanel criarFormularioLoja() {
        try {
            SystemLogger.ui("=== CRIANDO FORMULÁRIO CONFIGURAÇÕES DA LOJA ===");
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
            
            // Tabela de configurações
            JPanel tabelaPanel = criarPainelTabela();
            painelCentral.add(tabelaPanel, BorderLayout.SOUTH);
            
            painelPrincipal.add(painelCentral, BorderLayout.CENTER);
            
            SystemLogger.ui("Formulário Configurações da Loja criado com sucesso");
            return painelPrincipal;
            
        } catch (Exception e) {
            SystemLogger.error("Erro ao criar formulário Configurações da Loja", e);
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
        JLabel titleLabel = new JLabel("🏪 Loja");
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
        txtBusca.setToolTipText("Digite nome, CNPJ ou configuração");
        
        JButton btnBuscar = new JButton("🔍 Buscar");
        btnBuscar.setBackground(ACCENT_COLOR);
        btnBuscar.setForeground(WHITE);
        btnBuscar.setFocusPainted(false);
        btnBuscar.setBorderPainted(false);
        btnBuscar.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btnBuscar.addActionListener(e -> buscarConfiguracoes());
        
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
        
        // Seção Dados da Loja
        JLabel dadosSectionLabel = new JLabel("📋 Dados da Loja");
        dadosSectionLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        dadosSectionLabel.setForeground(ACCENT_COLOR);
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 4;
        formularioPanel.add(dadosSectionLabel, gbc);
        
        // Nome Loja
        gbc.gridy = 1; gbc.gridwidth = 1;
        JLabel lblNomeLoja = new JLabel("Nome da Loja:");
        lblNomeLoja.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        formularioPanel.add(lblNomeLoja, gbc);
        
        gbc.gridx = 1; gbc.weightx = 1.0;
        txtNomeLoja = new JTextField();
        txtNomeLoja.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        txtNomeLoja.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(GRAY),
            BorderFactory.createEmptyBorder(5, 8, 5, 8)
        ));
        formularioPanel.add(txtNomeLoja, gbc);
        
        // CNPJ
        gbc.gridx = 2; gbc.weightx = 0.0;
        JLabel lblCnpj = new JLabel("CNPJ:");
        lblCnpj.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        formularioPanel.add(lblCnpj, gbc);
        
        gbc.gridx = 3; gbc.weightx = 1.0;
        txtCnpj = new JTextField();
        txtCnpj.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        txtCnpj.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(GRAY),
            BorderFactory.createEmptyBorder(5, 8, 5, 8)
        ));
        txtCnpj.setToolTipText("99.999.999/9999-99");
        formularioPanel.add(txtCnpj, gbc);
        
        // Inscrição Estadual
        gbc.gridy = 2; gbc.gridx = 0;
        JLabel lblInscricaoEstadual = new JLabel("Inscrição Estadual:");
        lblInscricaoEstadual.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        formularioPanel.add(lblInscricaoEstadual, gbc);
        
        gbc.gridx = 1; gbc.weightx = 1.0;
        txtInscricaoEstadual = new JTextField();
        txtInscricaoEstadual.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        txtInscricaoEstadual.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(GRAY),
            BorderFactory.createEmptyBorder(5, 8, 5, 8)
        ));
        formularioPanel.add(txtInscricaoEstadual, gbc);
        
        // Telefone
        gbc.gridx = 2; gbc.weightx = 0.0;
        JLabel lblTelefone = new JLabel("Telefone:");
        lblTelefone.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        formularioPanel.add(lblTelefone, gbc);
        
        gbc.gridx = 3; gbc.weightx = 1.0;
        txtTelefone = new JTextField();
        txtTelefone.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        txtTelefone.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(GRAY),
            BorderFactory.createEmptyBorder(5, 8, 5, 8)
        ));
        txtTelefone.setToolTipText("(99) 99999-9999");
        formularioPanel.add(txtTelefone, gbc);
        
        // Email
        gbc.gridy = 3; gbc.gridx = 0;
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
        txtEmail.setToolTipText("email@exemplo.com");
        formularioPanel.add(txtEmail, gbc);
        
        // Seção Endereço
        gbc.gridy = 4; gbc.gridx = 0; gbc.gridwidth = 4; gbc.weightx = 0.0;
        JLabel enderecoSectionLabel = new JLabel("📍 Endereço");
        enderecoSectionLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        enderecoSectionLabel.setForeground(ACCENT_COLOR);
        formularioPanel.add(enderecoSectionLabel, gbc);
        
        // Endereço
        gbc.gridy = 5; gbc.gridwidth = 1;
        JLabel lblEndereco = new JLabel("Endereço:");
        lblEndereco.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        formularioPanel.add(lblEndereco, gbc);
        
        gbc.gridx = 1; gbc.weightx = 1.0;
        txtEndereco = new JTextField();
        txtEndereco.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        txtEndereco.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(GRAY),
            BorderFactory.createEmptyBorder(5, 8, 5, 8)
        ));
        formularioPanel.add(txtEndereco, gbc);
        
        // Cidade
        gbc.gridx = 2; gbc.weightx = 0.0;
        JLabel lblCidade = new JLabel("Cidade:");
        lblCidade.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        formularioPanel.add(lblCidade, gbc);
        
        gbc.gridx = 3; gbc.weightx = 1.0;
        txtCidade = new JTextField();
        txtCidade.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        txtCidade.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(GRAY),
            BorderFactory.createEmptyBorder(5, 8, 5, 8)
        ));
        formularioPanel.add(txtCidade, gbc);
        
        // Estado
        gbc.gridy = 6; gbc.gridx = 0;
        JLabel lblEstado = new JLabel("Estado:");
        lblEstado.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        formularioPanel.add(lblEstado, gbc);
        
        gbc.gridx = 1; gbc.weightx = 1.0;
        txtEstado = new JTextField();
        txtEstado.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        txtEstado.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(GRAY),
            BorderFactory.createEmptyBorder(5, 8, 5, 8)
        ));
        txtEstado.setToolTipText("UF");
        formularioPanel.add(txtEstado, gbc);
        
        // CEP
        gbc.gridx = 2; gbc.weightx = 0.0;
        JLabel lblCep = new JLabel("CEP:");
        lblCep.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        formularioPanel.add(lblCep, gbc);
        
        gbc.gridx = 3; gbc.weightx = 1.0;
        txtCep = new JTextField();
        txtCep.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        txtCep.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(GRAY),
            BorderFactory.createEmptyBorder(5, 8, 5, 8)
        ));
        txtCep.setToolTipText("99999-999");
        formularioPanel.add(txtCep, gbc);
        
        // Botões de ação
        gbc.gridy = 7; gbc.gridx = 0; gbc.gridwidth = 4; gbc.weightx = 0.0;
        JPanel botoesPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        botoesPanel.setBackground(WHITE);
        
        JButton btnSalvar = new JButton("💾 Salvar Configurações");
        btnSalvar.setBackground(SUCCESS_COLOR);
        btnSalvar.setForeground(WHITE);
        btnSalvar.setFocusPainted(false);
        btnSalvar.setBorderPainted(false);
        btnSalvar.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btnSalvar.addActionListener(e -> salvarConfiguracoes());
        
        JButton btnLimpar = new JButton("🧹 Limpar");
        btnLimpar.setBackground(GRAY);
        btnLimpar.setForeground(WHITE);
        btnLimpar.setFocusPainted(false);
        btnLimpar.setBorderPainted(false);
        btnLimpar.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btnLimpar.addActionListener(e -> limparCampos());
        
        JButton btnTestar = new JButton("🧪 Testar Configurações");
        btnTestar.setBackground(ACCENT_COLOR);
        btnTestar.setForeground(WHITE);
        btnTestar.setFocusPainted(false);
        btnTestar.setBorderPainted(false);
        btnTestar.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btnTestar.addActionListener(e -> testarConfiguracoes());
        
        botoesPanel.add(btnSalvar);
        botoesPanel.add(Box.createHorizontalStrut(10));
        botoesPanel.add(btnLimpar);
        botoesPanel.add(Box.createHorizontalStrut(10));
        botoesPanel.add(btnTestar);
        
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
        
        JLabel tabelaLabel = new JLabel("📋 Configurações da Loja");
        tabelaLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        tabelaLabel.setForeground(ACCENT_COLOR);
        
        // Modelo da tabela
        String[] colunas = {"Configuração", "Valor", "Descrição", "Status", "Ações"};
        modelTabela = new DefaultTableModel(colunas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 4; // Apenas a coluna de ações é editável
            }
        };
        
        tabelaConfiguracoes = new JTable(modelTabela);
        tabelaConfiguracoes.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        tabelaConfiguracoes.setRowHeight(25);
        tabelaConfiguracoes.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 12));
        tabelaConfiguracoes.getTableHeader().setBackground(ACCENT_COLOR);
        tabelaConfiguracoes.getTableHeader().setForeground(WHITE);
        
        // Scroll pane
        JScrollPane scrollPane = new JScrollPane(tabelaConfiguracoes);
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
        btnEditar.addActionListener(e -> editarConfiguracao());
        
        JButton btnExportar = new JButton("📄 Exportar");
        btnExportar.setBackground(SUCCESS_COLOR);
        btnExportar.setForeground(WHITE);
        btnExportar.setFocusPainted(false);
        btnExportar.setBorderPainted(false);
        btnExportar.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btnExportar.addActionListener(e -> exportarConfiguracoes());
        
        botoesTabelaPanel.add(btnEditar);
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
        configuracoesEncontradas.clear();
        
        // Dados de exemplo
        Object[][] dadosExemplo = {
            {"Nome da Loja", "Hermes Comercial PDV", "Nome fantasia da loja", "Ativo", "✏️"},
            {"CNPJ", "12.345.678/0001-99", "CNPJ da empresa", "Ativo", "✏️"},
            {"Telefone", "(11) 9999-8888", "Telefone de contato", "Ativo", "✏️"},
            {"Email", "contato@hermescomercial.com", "Email principal", "Ativo", "✏️"},
            {"Endereço", "Rua das Flores, 123", "Endereço da loja", "Ativo", "✏️"},
            {"Cidade", "São Paulo", "Cidade da loja", "Ativo", "✏️"},
            {"Estado", "SP", "Estado da loja", "Ativo", "✏️"},
            {"CEP", "01234-567", "CEP da loja", "Ativo", "✏️"}
        };
        
        for (Object[] dados : dadosExemplo) {
            modelTabela.addRow(dados);
            
            // Adicionar à lista de configurações
            ConfiguracaoLoja config = new ConfiguracaoLoja();
            config.setConfiguracao((String) dados[0]);
            config.setValor((String) dados[1]);
            config.setDescricao((String) dados[2]);
            config.setStatus((String) dados[3]);
            configuracoesEncontradas.add(config);
        }
    }
    
    /**
     * Preenche dados iniciais
     */
    private void preencherDadosIniciais() {
        txtNomeLoja.setText("Hermes Comercial PDV");
        txtCnpj.setText("12.345.678/0001-99");
        txtInscricaoEstadual.setText("123456789");
        txtTelefone.setText("(11) 9999-8888");
        txtEmail.setText("contato@hermescomercial.com");
        txtEndereco.setText("Rua das Flores, 123");
        txtCidade.setText("São Paulo");
        txtEstado.setText("SP");
        txtCep.setText("01234-567");
    }
    
    /**
     * Busca configurações
     */
    private void buscarConfiguracoes() {
        String termo = txtBusca.getText().trim();
        if (termo.isEmpty()) {
            JOptionPane.showMessageDialog(workArea, "Digite um termo para buscar!", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        JOptionPane.showMessageDialog(workArea, 
            "Busca realizada para: " + termo + "\n" +
            "Configurações encontradas: " + configuracoesEncontradas.size(), 
            "Resultado", JOptionPane.INFORMATION_MESSAGE);
        
        SystemLogger.ui("Busca realizada para: " + termo);
    }
    
    /**
     * Salva configurações
     */
    private void salvarConfiguracoes() {
        try {
            String nomeLoja = txtNomeLoja.getText().trim();
            String cnpj = txtCnpj.getText().trim();
            String email = txtEmail.getText().trim();
            
            if (nomeLoja.isEmpty()) {
                JOptionPane.showMessageDialog(workArea, "Digite o nome da loja!", "Aviso", JOptionPane.WARNING_MESSAGE);
                return;
            }
            
            if (cnpj.isEmpty()) {
                JOptionPane.showMessageDialog(workArea, "Digite o CNPJ!", "Aviso", JOptionPane.WARNING_MESSAGE);
                return;
            }
            
            if (email.isEmpty()) {
                JOptionPane.showMessageDialog(workArea, "Digite o email!", "Aviso", JOptionPane.WARNING_MESSAGE);
                return;
            }
            
            JOptionPane.showMessageDialog(workArea, 
                "Configurações da loja salvas com sucesso!\n\n" +
                "Nome: " + nomeLoja + "\n" +
                "CNPJ: " + cnpj + "\n" +
                "Email: " + email + "\n" +
                "Responsável: " + nomeUsuario, 
                "Sucesso", JOptionPane.INFORMATION_MESSAGE);
            
            SystemLogger.ui("Configurações da loja salvas por: " + nomeUsuario);
            
        } catch (Exception e) {
            SystemLogger.error("Erro ao salvar configurações", e);
            JOptionPane.showMessageDialog(workArea, "Erro ao salvar configurações: " + e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    /**
     * Limpa os campos do formulário
     */
    private void limparCampos() {
        txtBusca.setText("");
        txtNomeLoja.setText("");
        txtCnpj.setText("");
        txtInscricaoEstadual.setText("");
        txtTelefone.setText("");
        txtEmail.setText("");
        txtEndereco.setText("");
        txtCidade.setText("");
        txtEstado.setText("");
        txtCep.setText("");
    }
    
    /**
     * Edita configuração selecionada
     */
    private void editarConfiguracao() {
        int selectedRow = tabelaConfiguracoes.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(workArea, "Selecione uma configuração para editar!", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        ConfiguracaoLoja config = configuracoesEncontradas.get(selectedRow);
        
        JOptionPane.showMessageDialog(workArea, 
            "Detalhes da Configuração:\n\n" +
            "Configuração: " + config.getConfiguracao() + "\n" +
            "Valor: " + config.getValor() + "\n" +
            "Descrição: " + config.getDescricao() + "\n" +
            "Status: " + config.getStatus(), 
            "Editar Configuração", JOptionPane.INFORMATION_MESSAGE);
        
        SystemLogger.ui("Configuração visualizada: " + config.getConfiguracao());
    }
    
    /**
     * Testa configurações
     */
    private void testarConfiguracoes() {
        JOptionPane.showMessageDialog(workArea, 
            "Testando configurações da loja...\n" +
            "Validando dados de contato e endereço...\n" +
            "Verificando CNPJ e inscrição estadual...\n" +
            "(Implementar testes de validação)", 
            "Testar Configurações", JOptionPane.INFORMATION_MESSAGE);
        
        SystemLogger.ui("Testando configurações da loja");
    }
    
    /**
     * Exporta configurações
     */
    private void exportarConfiguracoes() {
        JOptionPane.showMessageDialog(workArea, 
            "Exportando " + configuracoesEncontradas.size() + " configurações...\n(Implementar exportação para JSON/XML)", 
            "Exportar Configurações", JOptionPane.INFORMATION_MESSAGE);
        
        SystemLogger.ui("Exportando " + configuracoesEncontradas.size() + " configurações");
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
     * Classe interna para representar uma configuração da loja
     */
    private static class ConfiguracaoLoja {
        private String configuracao;
        private String valor;
        private String descricao;
        private String status;
        
        // Getters e Setters
        public String getConfiguracao() { return configuracao; }
        public void setConfiguracao(String configuracao) { this.configuracao = configuracao; }
        
        public String getValor() { return valor; }
        public void setValor(String valor) { this.valor = valor; }
        
        public String getDescricao() { return descricao; }
        public void setDescricao(String descricao) { this.descricao = descricao; }
        
        public String getStatus() { return status; }
        public void setStatus(String status) { this.status = status; }
    }
}
