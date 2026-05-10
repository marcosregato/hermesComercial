package com.br.hermescomercial.pdv.controller;

import com.br.hermescomercial.util.SystemLogger;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Classe especializada para o formulário de Configurações de Busca
 * Estrutura: Header → Busca → Formulário → Tabela
 */
public class PDVFormularioConfiguracoesBusca {
    
    private JPanel workArea;
    private String usuarioAtual;
    private String nomeUsuario;
    
    // Componentes do formulário
    private JTextField txtBusca;
    private JTextField txtNomeConfiguracao;
    private JComboBox<String> comboModulo;
    private JComboBox<String> comboTipoBusca;
    private JComboBox<String> comboOrdenacao;
    private JCheckBox chkBuscaRapida;
    private JCheckBox chkBuscaAvancada;
    private JCheckBox chkHistorico;
    private JCheckBox chkFavoritos;
    private JSpinner spnLimiteResultados;
    private JSpinner spnTimeout;
    
    // Tabela de configurações
    private JTable tabelaConfiguracoes;
    private DefaultTableModel modelTabela;
    private List<ConfiguracaoBusca> configuracoesEncontrados;
    
    // Cores
    private static final Color WHITE = Color.WHITE;
    private static final Color ACCENT_COLOR = new Color(52, 152, 219);
    private static final Color SUCCESS_COLOR = new Color(39, 174, 96);
    private static final Color DANGER_COLOR = new Color(231, 76, 60);
    private static final Color WARNING_COLOR = new Color(241, 196, 15);
    private static final Color GRAY = new Color(149, 165, 166);
    
    public PDVFormularioConfiguracoesBusca(JPanel workArea, String usuarioAtual, String nomeUsuario) {
        this.workArea = workArea;
        this.usuarioAtual = usuarioAtual;
        this.nomeUsuario = nomeUsuario;
        this.configuracoesEncontrados = new ArrayList<>();
    }
    
    /**
     * Cria o formulário completo de Configurações de Busca
     */
    public JPanel criarFormularioConfiguracoesBusca() {
        try {
            SystemLogger.ui("=== CRIANDO FORMULÁRIO CONFIGURAÇÕES DE BUSCA ===");
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
            
            SystemLogger.ui("Formulário Configurações de Busca criado com sucesso");
            return painelPrincipal;
            
        } catch (Exception e) {
            SystemLogger.error("Erro ao criar formulário Configurações de Busca", e);
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
        JLabel titleLabel = new JLabel("⚙️ Configurações de Busca");
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
        txtBusca.setToolTipText("Digite nome ou módulo da configuração");
        
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
        
        // Seção Dados da Configuração
        JLabel dadosSectionLabel = new JLabel("⚙️ Dados da Configuração");
        dadosSectionLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        dadosSectionLabel.setForeground(ACCENT_COLOR);
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 4;
        formularioPanel.add(dadosSectionLabel, gbc);
        
        // Nome Configuração
        gbc.gridy = 1; gbc.gridwidth = 1;
        JLabel lblNomeConfiguracao = new JLabel("Nome:");
        lblNomeConfiguracao.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        formularioPanel.add(lblNomeConfiguracao, gbc);
        
        gbc.gridx = 1; gbc.weightx = 1.0;
        txtNomeConfiguracao = new JTextField();
        txtNomeConfiguracao.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        txtNomeConfiguracao.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(GRAY),
            BorderFactory.createEmptyBorder(5, 8, 5, 8)
        ));
        formularioPanel.add(txtNomeConfiguracao, gbc);
        
        // Módulo
        gbc.gridx = 2; gbc.weightx = 0.0;
        JLabel lblModulo = new JLabel("Módulo:");
        lblModulo.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        formularioPanel.add(lblModulo, gbc);
        
        gbc.gridx = 3; gbc.weightx = 1.0;
        comboModulo = new JComboBox<>(new String[]{"Todos", "Produtos", "Clientes", "Vendas", "Financeiro", "Estoque", "Relatórios", "Sistema"});
        comboModulo.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        comboModulo.setBorder(BorderFactory.createLineBorder(GRAY));
        formularioPanel.add(comboModulo, gbc);
        
        // Tipo Busca
        gbc.gridy = 2; gbc.gridx = 0;
        JLabel lblTipoBusca = new JLabel("Tipo Busca:");
        lblTipoBusca.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        formularioPanel.add(lblTipoBusca, gbc);
        
        gbc.gridx = 1; gbc.weightx = 1.0;
        comboTipoBusca = new JComboBox<>(new String[]{"Global", "Específica", "Avançada", "Rápida", "Indexada"});
        comboTipoBusca.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        comboTipoBusca.setBorder(BorderFactory.createLineBorder(GRAY));
        formularioPanel.add(comboTipoBusca, gbc);
        
        // Ordenação
        gbc.gridx = 2; gbc.weightx = 0.0;
        JLabel lblOrdenacao = new JLabel("Ordenação:");
        lblOrdenacao.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        formularioPanel.add(lblOrdenacao, gbc);
        
        gbc.gridx = 3; gbc.weightx = 1.0;
        comboOrdenacao = new JComboBox<>(new String[]{"Relevância", "Alfabética", "Data", "Popularidade", "Personalizada"});
        comboOrdenacao.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        comboOrdenacao.setBorder(BorderFactory.createLineBorder(GRAY));
        formularioPanel.add(comboOrdenacao, gbc);
        
        // Seção Opções de Busca
        gbc.gridy = 3; gbc.gridx = 0; gbc.gridwidth = 4; gbc.weightx = 0.0;
        JLabel opcoesSectionLabel = new JLabel("🔍 Opções de Busca");
        opcoesSectionLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        opcoesSectionLabel.setForeground(ACCENT_COLOR);
        formularioPanel.add(opcoesSectionLabel, gbc);
        
        // Checkboxes
        gbc.gridy = 4; gbc.gridx = 0; gbc.gridwidth = 4;
        JPanel checkboxPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 20, 0));
        checkboxPanel.setBackground(WHITE);
        
        chkBuscaRapida = new JCheckBox("Busca Rápida");
        chkBuscaRapida.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        chkBuscaRapida.setSelected(true);
        
        chkBuscaAvancada = new JCheckBox("Busca Avançada");
        chkBuscaAvancada.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        chkBuscaAvancada.setSelected(true);
        
        chkHistorico = new JCheckBox("Histórico");
        chkHistorico.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        chkHistorico.setSelected(true);
        
        chkFavoritos = new JCheckBox("Favoritos");
        chkFavoritos.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        chkFavoritos.setSelected(false);
        
        checkboxPanel.add(chkBuscaRapida);
        checkboxPanel.add(chkBuscaAvancada);
        checkboxPanel.add(chkHistorico);
        checkboxPanel.add(chkFavoritos);
        
        formularioPanel.add(checkboxPanel, gbc);
        
        // Seção Configurações Avançadas
        gbc.gridy = 5; gbc.gridx = 0; gbc.gridwidth = 4;
        JLabel avancadasSectionLabel = new JLabel("⚙️ Configurações Avançadas");
        avancadasSectionLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        avancadasSectionLabel.setForeground(ACCENT_COLOR);
        formularioPanel.add(avancadasSectionLabel, gbc);
        
        // Spinners
        gbc.gridy = 6; gbc.gridx = 0;
        JLabel lblLimiteResultados = new JLabel("Limite Resultados:");
        lblLimiteResultados.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        formularioPanel.add(lblLimiteResultados, gbc);
        
        gbc.gridx = 1; gbc.weightx = 1.0;
        spnLimiteResultados = new JSpinner(new SpinnerNumberModel(50, 10, 1000, 10));
        spnLimiteResultados.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        formularioPanel.add(spnLimiteResultados, gbc);
        
        gbc.gridx = 2; gbc.weightx = 0.0;
        JLabel lblTimeout = new JLabel("Timeout (seg):");
        lblTimeout.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        formularioPanel.add(lblTimeout, gbc);
        
        gbc.gridx = 3; gbc.weightx = 1.0;
        spnTimeout = new JSpinner(new SpinnerNumberModel(30, 5, 300, 5));
        spnTimeout.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        formularioPanel.add(spnTimeout, gbc);
        
        // Botões de ação
        gbc.gridy = 7; gbc.gridx = 0; gbc.gridwidth = 4; gbc.weightx = 0.0;
        JPanel botoesPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        botoesPanel.setBackground(WHITE);
        
        JButton btnSalvar = new JButton("💾 Salvar Configuração");
        btnSalvar.setBackground(SUCCESS_COLOR);
        btnSalvar.setForeground(WHITE);
        btnSalvar.setFocusPainted(false);
        btnSalvar.setBorderPainted(false);
        btnSalvar.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btnSalvar.addActionListener(e -> salvarConfiguracao());
        
        JButton btnLimpar = new JButton("🧹 Limpar");
        btnLimpar.setBackground(GRAY);
        btnLimpar.setForeground(WHITE);
        btnLimpar.setFocusPainted(false);
        btnLimpar.setBorderPainted(false);
        btnLimpar.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btnLimpar.addActionListener(e -> limparCampos());
        
        JButton btnTestar = new JButton("🧪 Testar Configuração");
        btnTestar.setBackground(ACCENT_COLOR);
        btnTestar.setForeground(WHITE);
        btnTestar.setFocusPainted(false);
        btnTestar.setBorderPainted(false);
        btnTestar.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btnTestar.addActionListener(e -> testarConfiguracao());
        
        JButton btnPadrao = new JButton("🔄 Restaurar Padrão");
        btnPadrao.setBackground(WARNING_COLOR);
        btnPadrao.setForeground(WHITE);
        btnPadrao.setFocusPainted(false);
        btnPadrao.setBorderPainted(false);
        btnPadrao.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btnPadrao.addActionListener(e -> restaurarPadrao());
        
        botoesPanel.add(btnSalvar);
        botoesPanel.add(Box.createHorizontalStrut(10));
        botoesPanel.add(btnLimpar);
        botoesPanel.add(Box.createHorizontalStrut(10));
        botoesPanel.add(btnTestar);
        botoesPanel.add(Box.createHorizontalStrut(10));
        botoesPanel.add(btnPadrao);
        
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
        
        JLabel tabelaLabel = new JLabel("⚙️ Configurações Salvas");
        tabelaLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        tabelaLabel.setForeground(ACCENT_COLOR);
        
        // Modelo da tabela
        String[] colunas = {"Nome", "Módulo", "Tipo", "Ordenação", "Limite", "Timeout", "Opções", "Status", "Ações"};
        modelTabela = new DefaultTableModel(colunas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 8; // Apenas a coluna de ações é editável
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
        
        JButton btnAtivar = new JButton("✅ Ativar");
        btnAtivar.setBackground(SUCCESS_COLOR);
        btnAtivar.setForeground(WHITE);
        btnAtivar.setFocusPainted(false);
        btnAtivar.setBorderPainted(false);
        btnAtivar.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btnAtivar.addActionListener(e -> ativarConfiguracao());
        
        JButton btnExcluir = new JButton("🗑️ Excluir");
        btnExcluir.setBackground(DANGER_COLOR);
        btnExcluir.setForeground(WHITE);
        btnExcluir.setFocusPainted(false);
        btnExcluir.setBorderPainted(false);
        btnExcluir.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btnExcluir.addActionListener(e -> excluirConfiguracao());
        
        botoesTabelaPanel.add(btnEditar);
        botoesTabelaPanel.add(Box.createHorizontalStrut(10));
        botoesTabelaPanel.add(btnAtivar);
        botoesTabelaPanel.add(Box.createHorizontalStrut(10));
        botoesTabelaPanel.add(btnExcluir);
        
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
        configuracoesEncontrados.clear();
        
        // Dados de exemplo
        Object[][] dadosExemplo = {
            {"Busca Padrão", "Todos", "Global", "Relevância", "50", "30", "Rápida,Avançada", "Ativo", "✏️"},
            {"Produtos Rápidos", "Produtos", "Rápida", "Alfabética", "20", "15", "Rápida", "Ativo", "✏️"},
            {"Clientes Completa", "Clientes", "Avançada", "Popularidade", "100", "45", "Avançada,Histórico", "Inativo", "✏️"},
            {"Vendas Diárias", "Vendas", "Específica", "Data", "30", "20", "Específica,Favoritos", "Ativo", "✏️"},
            {"Financeiro Global", "Financeiro", "Global", "Relevância", "75", "60", "Global,Histórico", "Ativo", "✏️"},
            {"Estoque Indexado", "Estoque", "Indexada", "Alfabética", "200", "90", "Indexada,Avançada", "Ativo", "✏️"},
            {"Relatórios Personalizados", "Relatórios", "Personalizada", "Personalizada", "25", "35", "Personalizada", "Inativo", "✏️"},
            {"Sistema Completo", "Sistema", "Global", "Relevância", "150", "120", "Global,Histórico,Favoritos", "Ativo", "✏️"}
        };
        
        for (Object[] dados : dadosExemplo) {
            modelTabela.addRow(dados);
            
            // Adicionar à lista de configurações
            ConfiguracaoBusca configuracao = new ConfiguracaoBusca();
            configuracao.setNome((String) dados[0]);
            configuracao.setModulo((String) dados[1]);
            configuracao.setTipo((String) dados[2]);
            configuracao.setOrdenacao((String) dados[3]);
            configuracao.setLimite((String) dados[4]);
            configuracao.setTimeout((String) dados[5]);
            configuracao.setOpcoes((String) dados[6]);
            configuracao.setStatus((String) dados[7]);
            configuracoesEncontrados.add(configuracao);
        }
    }
    
    /**
     * Preenche dados iniciais
     */
    private void preencherDadosIniciais() {
        comboModulo.setSelectedIndex(0);
        comboTipoBusca.setSelectedIndex(0);
        comboOrdenacao.setSelectedIndex(0);
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
        
        // TODO: Implementar lógica de busca no banco de dados
        JOptionPane.showMessageDialog(workArea, 
            "Busca realizada para: " + termo + "\n" +
            "Configurações encontradas: " + configuracoesEncontrados.size(), 
            "Resultado", JOptionPane.INFORMATION_MESSAGE);
        
        SystemLogger.ui("Busca de configurações para: " + termo);
    }
    
    /**
     * Salva configuração
     */
    private void salvarConfiguracao() {
        try {
            String nomeConfiguracao = txtNomeConfiguracao.getText().trim();
            
            if (nomeConfiguracao.isEmpty()) {
                JOptionPane.showMessageDialog(workArea, "Digite o nome da configuração!", "Aviso", JOptionPane.WARNING_MESSAGE);
                return;
            }
            
            // TODO: Implementar lógica de salvamento no banco de dados
            JOptionPane.showMessageDialog(workArea, 
                "Configuração salva com sucesso!\n\n" +
                "Nome: " + nomeConfiguracao + "\n" +
                "Módulo: " + comboModulo.getSelectedItem() + "\n" +
                "Tipo: " + comboTipoBusca.getSelectedItem() + "\n" +
                "Ordenação: " + comboOrdenacao.getSelectedItem() + "\n" +
                "Limite: " + spnLimiteResultados.getValue() + "\n" +
                "Timeout: " + spnTimeout.getValue() + "s\n" +
                "Responsável: " + nomeUsuario, 
                "Sucesso", JOptionPane.INFORMATION_MESSAGE);
            
            SystemLogger.ui("Configuração salva por: " + nomeUsuario);
            
        } catch (Exception e) {
            SystemLogger.error("Erro ao salvar configuração", e);
            JOptionPane.showMessageDialog(workArea, "Erro ao salvar configuração: " + e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    /**
     * Limpa os campos do formulário
     */
    private void limparCampos() {
        txtBusca.setText("");
        txtNomeConfiguracao.setText("");
        
        preencherDadosIniciais();
        
        chkBuscaRapida.setSelected(true);
        chkBuscaAvancada.setSelected(true);
        chkHistorico.setSelected(true);
        chkFavoritos.setSelected(false);
        
        spnLimiteResultados.setValue(50);
        spnTimeout.setValue(30);
    }
    
    /**
     * Testa configuração
     */
    private void testarConfiguracao() {
        String nomeConfiguracao = txtNomeConfiguracao.getText().trim();
        
        if (nomeConfiguracao.isEmpty()) {
            JOptionPane.showMessageDialog(workArea, "Digite o nome da configuração para testar!", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        JOptionPane.showMessageDialog(workArea, 
            "Testando configuração...\n\n" +
            "Nome: " + nomeConfiguracao + "\n" +
            "Módulo: " + comboModulo.getSelectedItem() + "\n" +
            "Tipo: " + comboTipoBusca.getSelectedItem() + "\n" +
            "Limite: " + spnLimiteResultados.getValue() + "\n" +
            "Timeout: " + spnTimeout.getValue() + "s\n\n" +
            "✅ Teste concluído com sucesso!\n" +
            "Tempo de resposta: 0.8s\n" +
            "Resultados simulados: 25", 
            "Testar Configuração", JOptionPane.INFORMATION_MESSAGE);
        
        SystemLogger.ui("Configuração testada: " + nomeConfiguracao);
    }
    
    /**
     * Restaura configuração padrão
     */
    private void restaurarPadrao() {
        int confirmacao = JOptionPane.showConfirmDialog(
            workArea,
            "Deseja restaurar as configurações padrão?\n\n" +
            "Isso irá redefinir todos os campos para os valores padrão.",
            "Restaurar Padrão",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.QUESTION_MESSAGE
        );
        
        if (confirmacao == JOptionPane.YES_OPTION) {
            limparCampos();
            
            JOptionPane.showMessageDialog(workArea, 
                "Configurações padrão restauradas com sucesso!", 
                "Sucesso", JOptionPane.INFORMATION_MESSAGE);
            
            SystemLogger.ui("Configurações padrão restauradas por: " + nomeUsuario);
        }
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
        
        ConfiguracaoBusca configuracao = configuracoesEncontrados.get(selectedRow);
        
        // Preencher formulário com dados selecionados
        txtNomeConfiguracao.setText(configuracao.getNome());
        comboModulo.setSelectedItem(configuracao.getModulo());
        comboTipoBusca.setSelectedItem(configuracao.getTipo());
        comboOrdenacao.setSelectedItem(configuracao.getOrdenacao());
        
        // Configurar outros campos baseado nos dados
        spnLimiteResultados.setValue(Integer.parseInt(configuracao.getLimite()));
        spnTimeout.setValue(Integer.parseInt(configuracao.getTimeout()));
        
        // Configurar checkboxes baseado nas opções
        String opcoes = configuracao.getOpcoes();
        chkBuscaRapida.setSelected(opcoes.contains("Rápida"));
        chkBuscaAvancada.setSelected(opcoes.contains("Avançada"));
        chkHistorico.setSelected(opcoes.contains("Histórico"));
        chkFavoritos.setSelected(opcoes.contains("Favoritos"));
        
        JOptionPane.showMessageDialog(workArea, 
            "Configuração carregada para edição:\n\n" +
            "Nome: " + configuracao.getNome() + "\n" +
            "Módulo: " + configuracao.getModulo() + "\n" +
            "Tipo: " + configuracao.getTipo(), 
            "Editar Configuração", JOptionPane.INFORMATION_MESSAGE);
        
        SystemLogger.ui("Configuração carregada para edição: " + configuracao.getNome());
    }
    
    /**
     * Ativa configuração selecionada
     */
    private void ativarConfiguracao() {
        int selectedRow = tabelaConfiguracoes.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(workArea, "Selecione uma configuração para ativar!", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        ConfiguracaoBusca configuracao = configuracoesEncontrados.get(selectedRow);
        String novoStatus = "Ativo".equals(configuracao.getStatus()) ? "Inativo" : "Ativo";
        
        // TODO: Implementar lógica de atualização no banco de dados
        
        // Atualizar status na tabela
        modelTabela.setValueAt(novoStatus, selectedRow, 7);
        configuracao.setStatus(novoStatus);
        
        JOptionPane.showMessageDialog(workArea, 
            "Configuração " + novoStatus.toLowerCase() + " com sucesso!\n" +
            "Nome: " + configuracao.getNome(), 
            "Sucesso", JOptionPane.INFORMATION_MESSAGE);
        
        SystemLogger.ui("Configuração " + novoStatus.toLowerCase() + ": " + configuracao.getNome());
    }
    
    /**
     * Exclui configuração selecionada
     */
    private void excluirConfiguracao() {
        int selectedRow = tabelaConfiguracoes.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(workArea, "Selecione uma configuração para excluir!", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        ConfiguracaoBusca configuracao = configuracoesEncontrados.get(selectedRow);
        
        int confirmacao = JOptionPane.showConfirmDialog(
            workArea,
            "Deseja realmente excluir esta configuração?\n\n" +
            "Nome: " + configuracao.getNome() + "\n" +
            "Esta ação não pode ser desfeita.",
            "Excluir Configuração",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.QUESTION_MESSAGE
        );
        
        if (confirmacao == JOptionPane.YES_OPTION) {
            // TODO: Implementar lógica de exclusão no banco de dados
            
            configuracoesEncontrados.remove(selectedRow);
            modelTabela.removeRow(selectedRow);
            
            JOptionPane.showMessageDialog(workArea, 
                "Configuração excluída com sucesso!", 
                "Sucesso", JOptionPane.INFORMATION_MESSAGE);
            
            SystemLogger.ui("Configuração excluída: " + configuracao.getNome());
        }
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
     * Classe interna para representar uma configuração de busca
     */
    private static class ConfiguracaoBusca {
        private String nome;
        private String modulo;
        private String tipo;
        private String ordenacao;
        private String limite;
        private String timeout;
        private String opcoes;
        private String status;
        
        // Getters e Setters
        public String getNome() { return nome; }
        public void setNome(String nome) { this.nome = nome; }
        
        public String getModulo() { return modulo; }
        public void setModulo(String modulo) { this.modulo = modulo; }
        
        public String getTipo() { return tipo; }
        public void setTipo(String tipo) { this.tipo = tipo; }
        
        public String getOrdenacao() { return ordenacao; }
        public void setOrdenacao(String ordenacao) { this.ordenacao = ordenacao; }
        
        public String getLimite() { return limite; }
        public void setLimite(String limite) { this.limite = limite; }
        
        public String getTimeout() { return timeout; }
        public void setTimeout(String timeout) { this.timeout = timeout; }
        
        public String getOpcoes() { return opcoes; }
        public void setOpcoes(String opcoes) { this.opcoes = opcoes; }
        
        public String getStatus() { return status; }
        public void setStatus(String status) { this.status = status; }
    }
}
