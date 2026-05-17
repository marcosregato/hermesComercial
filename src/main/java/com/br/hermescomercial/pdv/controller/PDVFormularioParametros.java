package com.br.hermescomercial.pdv.controller;

import com.br.hermescomercial.util.SystemLogger;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Classe especializada para o formulário de Parâmetros do Sistema
 * Estrutura: Header → Busca → Formulário → Tabela
 */
public class PDVFormularioParametros {
    
    private JPanel workArea;
    private String usuarioAtual;
    private String nomeUsuario;
    
    // Componentes do formulário
    private JTextField txtBusca;
    private JTextField txtNomeParametro;
    private JTextField txtValor;
    private JTextField txtDescricao;
    private JComboBox<String> comboCategoria;
    private JComboBox<String> comboTipoDado;
    private JComboBox<String> comboStatus;
    
    // Tabela de parâmetros
    private JTable tabelaParametros;
    private DefaultTableModel modelTabela;
    private List<Parametro> parametrosEncontrados;
    
    // Cores
    private static final Color WHITE = Color.WHITE;
    private static final Color ACCENT_COLOR = new Color(52, 152, 219);
    private static final Color SUCCESS_COLOR = new Color(39, 174, 96);
    private static final Color DANGER_COLOR = new Color(231, 76, 60);
    private static final Color WARNING_COLOR = new Color(241, 196, 15);
    private static final Color GRAY = new Color(149, 165, 166);
    
    public PDVFormularioParametros(JPanel workArea, String usuarioAtual, String nomeUsuario) {
        this.workArea = workArea;
        this.usuarioAtual = usuarioAtual;
        this.nomeUsuario = nomeUsuario;
        this.parametrosEncontrados = new ArrayList<>();
    }
    
    /**
     * Cria o formulário completo de Parâmetros do Sistema
     */
    public JPanel criarFormularioParametros() {
        try {
            SystemLogger.ui("=== CRIANDO FORMULÁRIO PARÂMETROS DO SISTEMA ===");
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
            
            // Tabela de parâmetros
            JPanel tabelaPanel = criarPainelTabela();
            painelCentral.add(tabelaPanel, BorderLayout.SOUTH);
            
            painelPrincipal.add(painelCentral, BorderLayout.CENTER);
            
            SystemLogger.ui("Formulário Parâmetros do Sistema criado com sucesso");
            return painelPrincipal;
            
        } catch (Exception e) {
            SystemLogger.error("Erro ao criar formulário Parâmetros do Sistema", e);
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
        JLabel titleLabel = new JLabel("🔧 Parâmetros");
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
        txtBusca.setToolTipText("Digite nome, categoria ou valor");
        
        JButton btnBuscar = new JButton("🔍 Buscar");
        btnBuscar.setBackground(ACCENT_COLOR);
        btnBuscar.setForeground(WHITE);
        btnBuscar.setFocusPainted(false);
        btnBuscar.setBorderPainted(false);
        btnBuscar.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btnBuscar.addActionListener(e -> buscarParametros());
        
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
        
        // Seção Dados do Parâmetro
        JLabel dadosSectionLabel = new JLabel("⚙️ Dados do Parâmetro");
        dadosSectionLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        dadosSectionLabel.setForeground(ACCENT_COLOR);
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 4;
        formularioPanel.add(dadosSectionLabel, gbc);
        
        // Nome Parâmetro
        gbc.gridy = 1; gbc.gridwidth = 1;
        JLabel lblNomeParametro = new JLabel("Nome do Parâmetro:");
        lblNomeParametro.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        formularioPanel.add(lblNomeParametro, gbc);
        
        gbc.gridx = 1; gbc.weightx = 1.0;
        txtNomeParametro = new JTextField();
        txtNomeParametro.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        txtNomeParametro.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(GRAY),
            BorderFactory.createEmptyBorder(5, 8, 5, 8)
        ));
        formularioPanel.add(txtNomeParametro, gbc);
        
        // Categoria
        gbc.gridx = 2; gbc.weightx = 0.0;
        JLabel lblCategoria = new JLabel("Categoria:");
        lblCategoria.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        formularioPanel.add(lblCategoria, gbc);
        
        gbc.gridx = 3; gbc.weightx = 1.0;
        comboCategoria = new JComboBox<>(new String[]{"Sistema", "Impressão", "Banco de Dados", "Interface", "Segurança", "Backup", "Integração"});
        comboCategoria.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        comboCategoria.setBorder(BorderFactory.createLineBorder(GRAY));
        formularioPanel.add(comboCategoria, gbc);
        
        // Valor
        gbc.gridy = 2; gbc.gridx = 0;
        JLabel lblValor = new JLabel("Valor:");
        lblValor.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        formularioPanel.add(lblValor, gbc);
        
        gbc.gridx = 1; gbc.weightx = 1.0;
        txtValor = new JTextField();
        txtValor.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        txtValor.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(GRAY),
            BorderFactory.createEmptyBorder(5, 8, 5, 8)
        ));
        txtValor.setToolTipText("Valor do parâmetro");
        formularioPanel.add(txtValor, gbc);
        
        // Tipo Dado
        gbc.gridx = 2; gbc.weightx = 0.0;
        JLabel lblTipoDado = new JLabel("Tipo de Dado:");
        lblTipoDado.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        formularioPanel.add(lblTipoDado, gbc);
        
        gbc.gridx = 3; gbc.weightx = 1.0;
        comboTipoDado = new JComboBox<>(new String[]{"Texto", "Número", "Booleano", "Data", "Caminho", "URL"});
        comboTipoDado.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        comboTipoDado.setBorder(BorderFactory.createLineBorder(GRAY));
        comboTipoDado.addActionListener(e -> validarTipoDado());
        formularioPanel.add(comboTipoDado, gbc);
        
        // Descrição
        gbc.gridy = 3; gbc.gridx = 0;
        JLabel lblDescricao = new JLabel("Descrição:");
        lblDescricao.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        formularioPanel.add(lblDescricao, gbc);
        
        gbc.gridx = 1; gbc.gridwidth = 3; gbc.weightx = 1.0;
        txtDescricao = new JTextField();
        txtDescricao.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        txtDescricao.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(GRAY),
            BorderFactory.createEmptyBorder(5, 8, 5, 8)
        ));
        txtDescricao.setToolTipText("Descrição detalhada do parâmetro");
        formularioPanel.add(txtDescricao, gbc);
        
        // Status
        gbc.gridy = 4; gbc.gridx = 0;
        JLabel lblStatus = new JLabel("Status:");
        lblStatus.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        formularioPanel.add(lblStatus, gbc);
        
        gbc.gridx = 1; gbc.weightx = 1.0;
        comboStatus = new JComboBox<>(new String[]{"Ativo", "Inativo"});
        comboStatus.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        comboStatus.setBorder(BorderFactory.createLineBorder(GRAY));
        formularioPanel.add(comboStatus, gbc);
        
        // Botões de ação
        gbc.gridy = 5; gbc.gridx = 0; gbc.gridwidth = 4; gbc.weightx = 0.0;
        JPanel botoesPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        botoesPanel.setBackground(WHITE);
        
        JButton btnSalvar = new JButton("💾 Salvar Parâmetro");
        btnSalvar.setBackground(SUCCESS_COLOR);
        btnSalvar.setForeground(WHITE);
        btnSalvar.setFocusPainted(false);
        btnSalvar.setBorderPainted(false);
        btnSalvar.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btnSalvar.addActionListener(e -> salvarParametro());
        
        JButton btnLimpar = new JButton("🧹 Limpar");
        btnLimpar.setBackground(GRAY);
        btnLimpar.setForeground(WHITE);
        btnLimpar.setFocusPainted(false);
        btnLimpar.setBorderPainted(false);
        btnLimpar.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btnLimpar.addActionListener(e -> limparCampos());
        
        JButton btnTestar = new JButton("🧪 Testar Parâmetro");
        btnTestar.setBackground(ACCENT_COLOR);
        btnTestar.setForeground(WHITE);
        btnTestar.setFocusPainted(false);
        btnTestar.setBorderPainted(false);
        btnTestar.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btnTestar.addActionListener(e -> testarParametro());
        
        JButton btnRestaurar = new JButton("🔄 Restaurar Padrão");
        btnRestaurar.setBackground(WARNING_COLOR);
        btnRestaurar.setForeground(WHITE);
        btnRestaurar.setFocusPainted(false);
        btnRestaurar.setBorderPainted(false);
        btnRestaurar.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btnRestaurar.addActionListener(e -> restaurarPadrao());
        
        botoesPanel.add(btnSalvar);
        botoesPanel.add(Box.createHorizontalStrut(10));
        botoesPanel.add(btnLimpar);
        botoesPanel.add(Box.createHorizontalStrut(10));
        botoesPanel.add(btnTestar);
        botoesPanel.add(Box.createHorizontalStrut(10));
        botoesPanel.add(btnRestaurar);
        
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
        
        JLabel tabelaLabel = new JLabel("📋 Parâmetros do Sistema");
        tabelaLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        tabelaLabel.setForeground(ACCENT_COLOR);
        
        // Modelo da tabela
        String[] colunas = {"Nome", "Categoria", "Valor", "Tipo", "Descrição", "Status", "Última Alteração", "Ações"};
        modelTabela = new DefaultTableModel(colunas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 7; // Apenas a coluna de ações é editável
            }
        };
        
        tabelaParametros = new JTable(modelTabela);
        tabelaParametros.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        tabelaParametros.setRowHeight(25);
        tabelaParametros.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 12));
        tabelaParametros.getTableHeader().setBackground(ACCENT_COLOR);
        tabelaParametros.getTableHeader().setForeground(WHITE);
        
        // Scroll pane
        JScrollPane scrollPane = new JScrollPane(tabelaParametros);
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
        btnEditar.addActionListener(e -> editarParametro());
        
        JButton btnExportar = new JButton("📄 Exportar Configuração");
        btnExportar.setBackground(SUCCESS_COLOR);
        btnExportar.setForeground(WHITE);
        btnExportar.setFocusPainted(false);
        btnExportar.setBorderPainted(false);
        btnExportar.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btnExportar.addActionListener(e -> exportarConfiguracao());
        
        JButton btnImportar = new JButton("📥 Importar Configuração");
        btnImportar.setBackground(ACCENT_COLOR);
        btnImportar.setForeground(WHITE);
        btnImportar.setFocusPainted(false);
        btnImportar.setBorderPainted(false);
        btnImportar.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btnImportar.addActionListener(e -> importarConfiguracao());
        
        botoesTabelaPanel.add(btnEditar);
        botoesTabelaPanel.add(Box.createHorizontalStrut(10));
        botoesTabelaPanel.add(btnExportar);
        botoesTabelaPanel.add(Box.createHorizontalStrut(10));
        botoesTabelaPanel.add(btnImportar);
        
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
        parametrosEncontrados.clear();
        
        // Dados de exemplo
        Object[][] dadosExemplo = {
            {"nome_empresa", "Sistema", "Hermes Comercial PDV", "Texto", "Nome da empresa no sistema", "Ativo", "10/05/2026 14:30", "✏️"},
            {"timeout_conexao", "Banco de Dados", "30", "Número", "Timeout de conexão em segundos", "Ativo", "10/05/2026 14:25", "✏️"},
            {"habilitar_backup", "Backup", "true", "Booleano", "Habilitar backup automático", "Ativo", "09/05/2026 16:20", "✏️"},
            {"caminho_backup", "Backup", "/backup/hermes", "Caminho", "Caminho para backup", "Ativo", "09/05/2026 16:18", "✏️"},
            {"maximo_tentativas_login", "Segurança", "3", "Número", "Máximo de tentativas de login", "Ativo", "08/05/2026 10:15", "✏️"},
            {"habilitar_log", "Sistema", "true", "Booleano", "Habilitar log do sistema", "Ativo", "08/05/2026 09:30", "✏️"},
            {"tamanho_fonte", "Interface", "12", "Número", "Tamanho da fonte da interface", "Ativo", "07/05/2026 15:45", "✏️"},
            {"url_api_pagamento", "Integração", "https://api.pagamento.com", "URL", "URL da API de pagamento", "Inativo", "07/05/2026 14:20", "✏️"}
        };
        
        for (Object[] dados : dadosExemplo) {
            modelTabela.addRow(dados);
            
            // Adicionar à lista de parâmetros
            Parametro parametro = new Parametro();
            parametro.setNome((String) dados[0]);
            parametro.setCategoria((String) dados[1]);
            parametro.setValor((String) dados[2]);
            parametro.setTipo((String) dados[3]);
            parametro.setDescricao((String) dados[4]);
            parametro.setStatus((String) dados[5]);
            parametro.setUltimaAlteracao((String) dados[6]);
            parametrosEncontrados.add(parametro);
        }
    }
    
    /**
     * Preenche dados iniciais
     */
    private void preencherDadosIniciais() {
        comboCategoria.setSelectedIndex(0);
        comboTipoDado.setSelectedIndex(0);
        comboStatus.setSelectedIndex(0);
    }
    
    /**
     * Valida tipo de dado
     */
    private void validarTipoDado() {
        String tipoDado = (String) comboTipoDado.getSelectedItem();
        String valor = txtValor.getText().trim();
        
        // Validação básica baseada no tipo
        switch (tipoDado) {
            case "Número":
                if (!valor.isEmpty() && !valor.matches("-?\\d+(\\.\\d+)?")) {
                    txtValor.setBackground(new Color(255, 230, 230));
                } else {
                    txtValor.setBackground(WHITE);
                }
                break;
            case "Booleano":
                if (!valor.isEmpty() && !valor.toLowerCase().matches("true|false|1|0")) {
                    txtValor.setBackground(new Color(255, 230, 230));
                } else {
                    txtValor.setBackground(WHITE);
                }
                break;
            case "URL":
                if (!valor.isEmpty() && !valor.toLowerCase().matches("^(http|https)://.*")) {
                    txtValor.setBackground(new Color(255, 230, 230));
                } else {
                    txtValor.setBackground(WHITE);
                }
                break;
            default:
                txtValor.setBackground(WHITE);
        }
    }
    
    /**
     * Busca parâmetros
     */
    private void buscarParametros() {
        String termo = txtBusca.getText().trim();
        if (termo.isEmpty()) {
            JOptionPane.showMessageDialog(workArea, "Digite um termo para buscar!", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        JOptionPane.showMessageDialog(workArea, 
            "Busca realizada para: " + termo + "\n" +
            "Parâmetros encontrados: " + parametrosEncontrados.size(), 
            "Resultado", JOptionPane.INFORMATION_MESSAGE);
        
        SystemLogger.ui("Busca realizada para: " + termo);
    }
    
    /**
     * Salva parâmetro
     */
    private void salvarParametro() {
        try {
            String nomeParametro = txtNomeParametro.getText().trim();
            String valor = txtValor.getText().trim();
            String categoria = (String) comboCategoria.getSelectedItem();
            String tipoDado = (String) comboTipoDado.getSelectedItem();
            
            if (nomeParametro.isEmpty()) {
                JOptionPane.showMessageDialog(workArea, "Digite o nome do parâmetro!", "Aviso", JOptionPane.WARNING_MESSAGE);
                return;
            }
            
            if (valor.isEmpty()) {
                JOptionPane.showMessageDialog(workArea, "Digite o valor do parâmetro!", "Aviso", JOptionPane.WARNING_MESSAGE);
                return;
            }
            
            // Validar valor baseado no tipo
            if (!validarValorPorTipo(valor, tipoDado)) {
                JOptionPane.showMessageDialog(workArea, "Valor inválido para o tipo " + tipoDado + "!", "Aviso", JOptionPane.WARNING_MESSAGE);
                return;
            }
            
            JOptionPane.showMessageDialog(workArea, 
                "Parâmetro salvo com sucesso!\n\n" +
                "Nome: " + nomeParametro + "\n" +
                "Valor: " + valor + "\n" +
                "Categoria: " + categoria + "\n" +
                "Tipo: " + tipoDado + "\n" +
                "Responsável: " + nomeUsuario, 
                "Sucesso", JOptionPane.INFORMATION_MESSAGE);
            
            SystemLogger.ui("Parâmetro salvo por: " + nomeUsuario);
            
        } catch (Exception e) {
            SystemLogger.error("Erro ao salvar parâmetro", e);
            JOptionPane.showMessageDialog(workArea, "Erro ao salvar parâmetro: " + e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    /**
     * Valida valor por tipo
     */
    private boolean validarValorPorTipo(String valor, String tipo) {
        switch (tipo) {
            case "Número":
                return valor.matches("-?\\d+(\\.\\d+)?");
            case "Booleano":
                return valor.toLowerCase().matches("true|false|1|0");
            case "URL":
                return valor.toLowerCase().matches("^(http|https)://.*") || valor.isEmpty();
            default:
                return true;
        }
    }
    
    /**
     * Limpa os campos do formulário
     */
    private void limparCampos() {
        txtBusca.setText("");
        txtNomeParametro.setText("");
        txtValor.setText("");
        txtDescricao.setText("");
        comboCategoria.setSelectedIndex(0);
        comboTipoDado.setSelectedIndex(0);
        comboStatus.setSelectedIndex(0);
        
        preencherDadosIniciais();
    }
    
    /**
     * Edita parâmetro selecionado
     */
    private void editarParametro() {
        int selectedRow = tabelaParametros.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(workArea, "Selecione um parâmetro para editar!", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        Parametro parametro = parametrosEncontrados.get(selectedRow);
        
        // Preencher formulário com dados selecionados
        txtNomeParametro.setText(parametro.getNome());
        txtValor.setText(parametro.getValor());
        txtDescricao.setText(parametro.getDescricao());
        comboCategoria.setSelectedItem(parametro.getCategoria());
        comboTipoDado.setSelectedItem(parametro.getTipo());
        comboStatus.setSelectedItem(parametro.getStatus());
        
        JOptionPane.showMessageDialog(workArea, 
            "Parâmetro carregado para edição:\n\n" +
            "Nome: " + parametro.getNome() + "\n" +
            "Categoria: " + parametro.getCategoria() + "\n" +
            "Valor: " + parametro.getValor(), 
            "Editar Parâmetro", JOptionPane.INFORMATION_MESSAGE);
        
        SystemLogger.ui("Parâmetro carregado para edição: " + parametro.getNome());
    }
    
    /**
     * Testa parâmetro
     */
    private void testarParametro() {
        String nomeParametro = txtNomeParametro.getText().trim();
        String valor = txtValor.getText().trim();
        String tipoDado = (String) comboTipoDado.getSelectedItem();
        
        if (nomeParametro.isEmpty()) {
            JOptionPane.showMessageDialog(workArea, "Digite o nome do parâmetro!", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        if (valor.isEmpty()) {
            JOptionPane.showMessageDialog(workArea, "Digite o valor do parâmetro!", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        // Validar valor
        if (!validarValorPorTipo(valor, tipoDado)) {
            JOptionPane.showMessageDialog(workArea, "Valor inválido para o tipo " + tipoDado + "!", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        JOptionPane.showMessageDialog(workArea, 
            "Testando parâmetro...\n\n" +
            "Nome: " + nomeParametro + "\n" +
            "Valor: " + valor + "\n" +
            "Tipo: " + tipoDado + "\n" +
            "Validação: OK\n" +
            "(Implementar testes específicos)", 
            "Testar Parâmetro", JOptionPane.INFORMATION_MESSAGE);
        
        SystemLogger.ui("Parâmetro testado: " + nomeParametro);
    }
    
    /**
     * Restaura valor padrão
     */
    private void restaurarPadrao() {
        String nomeParametro = txtNomeParametro.getText().trim();
        
        if (nomeParametro.isEmpty()) {
            JOptionPane.showMessageDialog(workArea, "Digite o nome do parâmetro!", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        int confirmacao = JOptionPane.showConfirmDialog(
            workArea,
            "Deseja restaurar o valor padrão do parâmetro?\n\n" +
            "Parâmetro: " + nomeParametro + "\n" +
            "Esta ação não pode ser desfeita.",
            "Restaurar Padrão",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.QUESTION_MESSAGE
        );
        
        if (confirmacao == JOptionPane.YES_OPTION) {
            JOptionPane.showMessageDialog(workArea, 
                "Valor padrão restaurado com sucesso!\n" +
                "Parâmetro: " + nomeParametro + "\n" +
                "Responsável: " + nomeUsuario, 
                "Sucesso", JOptionPane.INFORMATION_MESSAGE);
            
            SystemLogger.ui("Valor padrão restaurado para: " + nomeParametro);
        }
    }
    
    /**
     * Exporta configuração
     */
    private void exportarConfiguracao() {
        JOptionPane.showMessageDialog(workArea, 
            "Exportando " + parametrosEncontrados.size() + " parâmetros...\n(Implementar exportação para JSON/Properties)", 
            "Exportar Configuração", JOptionPane.INFORMATION_MESSAGE);
        
        SystemLogger.ui("Exportando " + parametrosEncontrados.size() + " parâmetros");
    }
    
    /**
     * Importa configuração
     */
    private void importarConfiguracao() {
        JOptionPane.showMessageDialog(workArea, 
            "Importando configuração...\n" +
            "Selecione o arquivo de configuração\n" +
            "(Implementar importação de JSON/Properties)", 
            "Importar Configuração", JOptionPane.INFORMATION_MESSAGE);
        
        SystemLogger.ui("Importando configuração");
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
     * Classe interna para representar um parâmetro
     */
    private static class Parametro {
        private String nome;
        private String categoria;
        private String valor;
        private String tipo;
        private String descricao;
        private String status;
        private String ultimaAlteracao;
        
        // Getters e Setters
        public String getNome() { return nome; }
        public void setNome(String nome) { this.nome = nome; }
        
        public String getCategoria() { return categoria; }
        public void setCategoria(String categoria) { this.categoria = categoria; }
        
        public String getValor() { return valor; }
        public void setValor(String valor) { this.valor = valor; }
        
        public String getTipo() { return tipo; }
        public void setTipo(String tipo) { this.tipo = tipo; }
        
        public String getDescricao() { return descricao; }
        public void setDescricao(String descricao) { this.descricao = descricao; }
        
        public String getStatus() { return status; }
        public void setStatus(String status) { this.status = status; }
        
        @SuppressWarnings("unused")
        public String getUltimaAlteracao() { return ultimaAlteracao; }
        public void setUltimaAlteracao(String ultimaAlteracao) { this.ultimaAlteracao = ultimaAlteracao; }
    }
}
