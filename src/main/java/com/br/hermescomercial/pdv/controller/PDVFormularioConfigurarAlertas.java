package com.br.hermescomercial.pdv.controller;

import com.br.hermescomercial.util.SystemLogger;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Classe especializada para o formulário de Configurar Alertas
 * Estrutura: Header → Busca → Formulário → Tabela
 */
public class PDVFormularioConfigurarAlertas {
    
    private JPanel workArea;
    private String usuarioAtual;
    private String nomeUsuario;
    
    // Componentes do formulário
    private JTextField txtBusca;
    private JTextField txtNomeAlerta;
    private JTextField txtDescricao;
    private JComboBox<String> comboTipo;
    private JComboBox<String> comboPrioridade;
    private JComboBox<String> comboStatus;
    private JComboBox<String> comboDisparo;
    private JComboBox<String> comboCondicao;
    private JTextField txtValor;
    private JComboBox<String> comboAcao;
    private JTextArea txtMensagem;
    
    // Tabela de alertas configurados
    private JTable tabelaAlertas;
    private DefaultTableModel modelTabela;
    private List<Alerta> alertasEncontrados;
    
    // Cores
    private static final Color WHITE = Color.WHITE;
    private static final Color ACCENT_COLOR = new Color(52, 152, 219);
    private static final Color SUCCESS_COLOR = new Color(39, 174, 96);
    private static final Color DANGER_COLOR = new Color(231, 76, 60);
    private static final Color GRAY = new Color(149, 165, 166);
    
    public PDVFormularioConfigurarAlertas(JPanel workArea, String usuarioAtual, String nomeUsuario) {
        this.workArea = workArea;
        this.usuarioAtual = usuarioAtual;
        this.nomeUsuario = nomeUsuario;
        this.alertasEncontrados = new ArrayList<>();
    }
    
    /**
     * Cria o formulário completo de Configurar Alertas
     */
    public JPanel criarFormularioConfigurarAlertas() {
        try {
            SystemLogger.ui("=== CRIANDO FORMULÁRIO CONFIGURAR ALERTAS ===");
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
            
            // Tabela de alertas configurados
            JPanel tabelaPanel = criarPainelTabela();
            painelCentral.add(tabelaPanel, BorderLayout.SOUTH);
            
            painelPrincipal.add(painelCentral, BorderLayout.CENTER);
            
            SystemLogger.ui("Formulário Configurar Alertas criado com sucesso");
            return painelPrincipal;
            
        } catch (Exception e) {
            SystemLogger.error("Erro ao criar formulário Configurar Alertas", e);
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
        JLabel titleLabel = new JLabel("⚙️ Configurar Alertas");
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
        txtBusca.setToolTipText("Digite nome ou tipo do alerta");
        
        JButton btnBuscar = new JButton("🔍 Buscar");
        btnBuscar.setBackground(ACCENT_COLOR);
        btnBuscar.setForeground(WHITE);
        btnBuscar.setFocusPainted(false);
        btnBuscar.setBorderPainted(false);
        btnBuscar.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btnBuscar.addActionListener(e -> buscarAlertas());
        
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
        
        // Seção Dados do Alerta
        JLabel dadosSectionLabel = new JLabel("📝 Dados do Alerta");
        dadosSectionLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        dadosSectionLabel.setForeground(ACCENT_COLOR);
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 4;
        formularioPanel.add(dadosSectionLabel, gbc);
        
        // Nome Alerta
        gbc.gridy = 1; gbc.gridwidth = 1;
        JLabel lblNomeAlerta = new JLabel("Nome do Alerta:");
        lblNomeAlerta.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        formularioPanel.add(lblNomeAlerta, gbc);
        
        gbc.gridx = 1; gbc.weightx = 1.0;
        txtNomeAlerta = new JTextField();
        txtNomeAlerta.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        txtNomeAlerta.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(GRAY),
            BorderFactory.createEmptyBorder(5, 8, 5, 8)
        ));
        formularioPanel.add(txtNomeAlerta, gbc);
        
        // Tipo
        gbc.gridx = 2; gbc.weightx = 0.0;
        JLabel lblTipo = new JLabel("Tipo:");
        lblTipo.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        formularioPanel.add(lblTipo, gbc);
        
        gbc.gridx = 3; gbc.weightx = 1.0;
        comboTipo = new JComboBox<>(new String[]{"Estoque", "Vendas", "Financeiro", "Sistema", "Segurança", "Performance"});
        comboTipo.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        comboTipo.setBorder(BorderFactory.createLineBorder(GRAY));
        comboTipo.addActionListener(e -> atualizarCondicoes());
        formularioPanel.add(comboTipo, gbc);
        
        // Descrição
        gbc.gridy = 2; gbc.gridx = 0;
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
        formularioPanel.add(txtDescricao, gbc);
        
        // Seção Configuração do Alerta
        gbc.gridy = 3; gbc.gridx = 0; gbc.gridwidth = 4; gbc.weightx = 0.0;
        JLabel configSectionLabel = new JLabel("⚙️ Configuração do Alerta");
        configSectionLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        configSectionLabel.setForeground(ACCENT_COLOR);
        formularioPanel.add(configSectionLabel, gbc);
        
        // Prioridade
        gbc.gridy = 4; gbc.gridwidth = 1;
        JLabel lblPrioridade = new JLabel("Prioridade:");
        lblPrioridade.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        formularioPanel.add(lblPrioridade, gbc);
        
        gbc.gridx = 1; gbc.weightx = 1.0;
        comboPrioridade = new JComboBox<>(new String[]{"Alta", "Média", "Baixa"});
        comboPrioridade.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        comboPrioridade.setBorder(BorderFactory.createLineBorder(GRAY));
        formularioPanel.add(comboPrioridade, gbc);
        
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
        
        // Disparo
        gbc.gridy = 5; gbc.gridx = 0;
        JLabel lblDisparo = new JLabel("Disparo:");
        lblDisparo.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        formularioPanel.add(lblDisparo, gbc);
        
        gbc.gridx = 1; gbc.weightx = 1.0;
        comboDisparo = new JComboBox<>(new String[]{"Imediato", "Diário", "Semanal", "Mensal", "Personalizado"});
        comboDisparo.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        comboDisparo.setBorder(BorderFactory.createLineBorder(GRAY));
        formularioPanel.add(comboDisparo, gbc);
        
        // Condição
        gbc.gridx = 2; gbc.weightx = 0.0;
        JLabel lblCondicao = new JLabel("Condição:");
        lblCondicao.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        formularioPanel.add(lblCondicao, gbc);
        
        gbc.gridx = 3; gbc.weightx = 1.0;
        comboCondicao = new JComboBox<>(new String[]{"Igual a", "Maior que", "Menor que", "Diferente de", "Contém"});
        comboCondicao.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        comboCondicao.setBorder(BorderFactory.createLineBorder(GRAY));
        formularioPanel.add(comboCondicao, gbc);
        
        // Valor
        gbc.gridy = 6; gbc.gridx = 0;
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
        formularioPanel.add(txtValor, gbc);
        
        // Ação
        gbc.gridx = 2; gbc.weightx = 0.0;
        JLabel lblAcao = new JLabel("Ação:");
        lblAcao.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        formularioPanel.add(lblAcao, gbc);
        
        gbc.gridx = 3; gbc.weightx = 1.0;
        comboAcao = new JComboBox<>(new String[]{"Notificar", "Email", "SMS", "Bloquear Operação", "Registrar Log"});
        comboAcao.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        comboAcao.setBorder(BorderFactory.createLineBorder(GRAY));
        formularioPanel.add(comboAcao, gbc);
        
        // Mensagem
        gbc.gridy = 7; gbc.gridx = 0;
        JLabel lblMensagem = new JLabel("Mensagem:");
        lblMensagem.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        formularioPanel.add(lblMensagem, gbc);
        
        gbc.gridx = 1; gbc.gridwidth = 3; gbc.weightx = 1.0;
        txtMensagem = new JTextArea(3, 30);
        txtMensagem.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        txtMensagem.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(GRAY),
            BorderFactory.createEmptyBorder(5, 8, 5, 8)
        ));
        txtMensagem.setLineWrap(true);
        txtMensagem.setWrapStyleWord(true);
        JScrollPane scrollMensagem = new JScrollPane(txtMensagem);
        formularioPanel.add(scrollMensagem, gbc);
        
        // Botões de ação
        gbc.gridy = 8; gbc.gridx = 0; gbc.gridwidth = 4; gbc.weightx = 0.0;
        JPanel botoesPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        botoesPanel.setBackground(WHITE);
        
        JButton btnSalvar = new JButton("💾 Salvar Alerta");
        btnSalvar.setBackground(SUCCESS_COLOR);
        btnSalvar.setForeground(WHITE);
        btnSalvar.setFocusPainted(false);
        btnSalvar.setBorderPainted(false);
        btnSalvar.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btnSalvar.addActionListener(e -> salvarAlerta());
        
        JButton btnLimpar = new JButton("🧹 Limpar");
        btnLimpar.setBackground(GRAY);
        btnLimpar.setForeground(WHITE);
        btnLimpar.setFocusPainted(false);
        btnLimpar.setBorderPainted(false);
        btnLimpar.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btnLimpar.addActionListener(e -> limparCampos());
        
        JButton btnTestar = new JButton("🧪 Testar Alerta");
        btnTestar.setBackground(ACCENT_COLOR);
        btnTestar.setForeground(WHITE);
        btnTestar.setFocusPainted(false);
        btnTestar.setBorderPainted(false);
        btnTestar.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btnTestar.addActionListener(e -> testarAlerta());
        
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
        
        JLabel tabelaLabel = new JLabel("📋 Alertas Configurados");
        tabelaLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        tabelaLabel.setForeground(ACCENT_COLOR);
        
        // Modelo da tabela
        String[] colunas = {"Nome", "Tipo", "Prioridade", "Status", "Condição", "Valor", "Ação", "Último Disparo", "Ações"};
        modelTabela = new DefaultTableModel(colunas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 8; // Apenas a coluna de ações é editável
            }
        };
        
        tabelaAlertas = new JTable(modelTabela);
        tabelaAlertas.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        tabelaAlertas.setRowHeight(25);
        tabelaAlertas.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 12));
        tabelaAlertas.getTableHeader().setBackground(ACCENT_COLOR);
        tabelaAlertas.getTableHeader().setForeground(WHITE);
        
        // Scroll pane
        JScrollPane scrollPane = new JScrollPane(tabelaAlertas);
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
        btnEditar.addActionListener(e -> editarAlerta());
        
        JButton btnAtivarDesativar = new JButton("🔄 Ativar/Desativar");
        btnAtivarDesativar.setBackground(ACCENT_COLOR);
        btnAtivarDesativar.setForeground(WHITE);
        btnAtivarDesativar.setFocusPainted(false);
        btnAtivarDesativar.setBorderPainted(false);
        btnAtivarDesativar.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btnAtivarDesativar.addActionListener(e -> ativarDesativarAlerta());
        
        JButton btnExcluir = new JButton("🗑️ Excluir");
        btnExcluir.setBackground(DANGER_COLOR);
        btnExcluir.setForeground(WHITE);
        btnExcluir.setFocusPainted(false);
        btnExcluir.setBorderPainted(false);
        btnExcluir.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btnExcluir.addActionListener(e -> excluirAlerta());
        
        botoesTabelaPanel.add(btnEditar);
        botoesTabelaPanel.add(Box.createHorizontalStrut(10));
        botoesTabelaPanel.add(btnAtivarDesativar);
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
        alertasEncontrados.clear();
        
        // Dados de exemplo
        Object[][] dadosExemplo = {
            {"Estoque Baixo", "Estoque", "Alta", "Ativo", "Menor que", "10", "Notificar", "10/05/2026 15:30", "✏️"},
            {"Meta de Vendas", "Vendas", "Média", "Ativo", "Maior que", "5000", "Email", "10/05/2026 18:45", "✏️"},
            {"Backup Falhou", "Sistema", "Alta", "Ativo", "Igual a", "Falha", "SMS", "09/05/2026 22:00", "✏️"},
            {"Caixa Aberto", "Financeiro", "Baixa", "Inativo", "Diferente de", "Fechado", "Registrar Log", "09/05/2026 08:00", "✏️"},
            {"Acesso Suspeito", "Segurança", "Alta", "Ativo", "Contém", "Falha", "Bloquear Operação", "08/05/2026 14:20", "✏️"},
            {"CPU Alta", "Performance", "Média", "Ativo", "Maior que", "80%", "Notificar", "08/05/2026 16:30", "✏️"},
            {"Venda Cancelada", "Vendas", "Baixa", "Inativo", "Igual a", "Cancelado", "Email", "07/05/2026 11:15", "✏️"},
            {"Memória Baixa", "Sistema", "Média", "Ativo", "Menor que", "500MB", "Registrar Log", "07/05/2026 09:45", "✏️"}
        };
        
        for (Object[] dados : dadosExemplo) {
            modelTabela.addRow(dados);
            
            // Adicionar à lista de alertas
            Alerta alerta = new Alerta();
            alerta.setNome((String) dados[0]);
            alerta.setTipo((String) dados[1]);
            alerta.setPrioridade((String) dados[2]);
            alerta.setStatus((String) dados[3]);
            alerta.setCondicao((String) dados[4]);
            alerta.setValor((String) dados[5]);
            alerta.setAcao((String) dados[6]);
            alerta.setUltimoDisparo((String) dados[7]);
            alertasEncontrados.add(alerta);
        }
    }
    
    /**
     * Preenche dados iniciais
     */
    private void preencherDadosIniciais() {
        comboTipo.setSelectedIndex(0);
        comboPrioridade.setSelectedIndex(1);
        comboStatus.setSelectedIndex(0);
        comboDisparo.setSelectedIndex(0);
        comboAcao.setSelectedIndex(0);
    }
    
    /**
     * Atualiza condições baseado no tipo
     */
    private void atualizarCondicoes() {
        String tipo = (String) comboTipo.getSelectedItem();
        
        // Limpar combo de condição
        comboCondicao.removeAllItems();
        
        // Adicionar condições baseadas no tipo
        switch (tipo) {
            case "Estoque":
                comboCondicao.addItem("Menor que");
                comboCondicao.addItem("Igual a");
                comboCondicao.addItem("Maior que");
                break;
            case "Vendas":
                comboCondicao.addItem("Maior que");
                comboCondicao.addItem("Menor que");
                comboCondicao.addItem("Igual a");
                break;
            case "Financeiro":
                comboCondicao.addItem("Diferente de");
                comboCondicao.addItem("Igual a");
                comboCondicao.addItem("Contém");
                break;
            case "Sistema":
                comboCondicao.addItem("Igual a");
                comboCondicao.addItem("Diferente de");
                comboCondicao.addItem("Contém");
                break;
            case "Segurança":
                comboCondicao.addItem("Contém");
                comboCondicao.addItem("Igual a");
                break;
            case "Performance":
                comboCondicao.addItem("Maior que");
                comboCondicao.addItem("Menor que");
                break;
        }
    }
    
    /**
     * Busca alertas
     */
    private void buscarAlertas() {
        String termo = txtBusca.getText().trim();
        if (termo.isEmpty()) {
            JOptionPane.showMessageDialog(workArea, "Digite um termo para buscar!", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        JOptionPane.showMessageDialog(workArea, 
            "Busca realizada para: " + termo + "\n" +
            "Alertas encontrados: " + alertasEncontrados.size(), 
            "Resultado", JOptionPane.INFORMATION_MESSAGE);
        
        SystemLogger.ui("Busca realizada para: " + termo);
    }
    
    /**
     * Salva alerta
     */
    private void salvarAlerta() {
        try {
            String nomeAlerta = txtNomeAlerta.getText().trim();
            String tipo = (String) comboTipo.getSelectedItem();
            String valor = txtValor.getText().trim();
            String mensagem = txtMensagem.getText().trim();
            
            if (nomeAlerta.isEmpty()) {
                JOptionPane.showMessageDialog(workArea, "Digite o nome do alerta!", "Aviso", JOptionPane.WARNING_MESSAGE);
                return;
            }
            
            if (valor.isEmpty()) {
                JOptionPane.showMessageDialog(workArea, "Digite o valor para a condição!", "Aviso", JOptionPane.WARNING_MESSAGE);
                return;
            }
            
            if (mensagem.isEmpty()) {
                JOptionPane.showMessageDialog(workArea, "Digite a mensagem do alerta!", "Aviso", JOptionPane.WARNING_MESSAGE);
                return;
            }
            
            JOptionPane.showMessageDialog(workArea, 
                "Alerta salvo com sucesso!\n\n" +
                "Nome: " + nomeAlerta + "\n" +
                "Tipo: " + tipo + "\n" +
                "Prioridade: " + comboPrioridade.getSelectedItem() + "\n" +
                "Condição: " + comboCondicao.getSelectedItem() + " " + valor + "\n" +
                "Ação: " + comboAcao.getSelectedItem() + "\n" +
                "Responsável: " + nomeUsuario, 
                "Sucesso", JOptionPane.INFORMATION_MESSAGE);
            
            SystemLogger.ui("Alerta salvo por: " + nomeUsuario);
            
        } catch (Exception e) {
            SystemLogger.error("Erro ao salvar alerta", e);
            JOptionPane.showMessageDialog(workArea, "Erro ao salvar alerta: " + e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    /**
     * Limpa os campos do formulário
     */
    private void limparCampos() {
        txtBusca.setText("");
        txtNomeAlerta.setText("");
        txtDescricao.setText("");
        txtValor.setText("");
        txtMensagem.setText("");
        
        preencherDadosIniciais();
    }
    
    /**
     * Edita alerta selecionado
     */
    private void editarAlerta() {
        int selectedRow = tabelaAlertas.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(workArea, "Selecione um alerta para editar!", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        Alerta alerta = alertasEncontrados.get(selectedRow);
        
        // Preencher formulário com dados selecionados
        txtNomeAlerta.setText(alerta.getNome());
        comboTipo.setSelectedItem(alerta.getTipo());
        comboPrioridade.setSelectedItem(alerta.getPrioridade());
        comboStatus.setSelectedItem(alerta.getStatus());
        txtValor.setText(alerta.getValor());
        comboAcao.setSelectedItem(alerta.getAcao());
        
        JOptionPane.showMessageDialog(workArea, 
            "Alerta carregado para edição:\n\n" +
            "Nome: " + alerta.getNome() + "\n" +
            "Tipo: " + alerta.getTipo() + "\n" +
            "Condição: " + alerta.getCondicao() + " " + alerta.getValor(), 
            "Editar Alerta", JOptionPane.INFORMATION_MESSAGE);
        
        SystemLogger.ui("Alerta carregado para edição: " + alerta.getNome());
    }
    
    /**
     * Ativa ou desativa alerta selecionado
     */
    private void ativarDesativarAlerta() {
        int selectedRow = tabelaAlertas.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(workArea, "Selecione um alerta para ativar/desativar!", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        Alerta alerta = alertasEncontrados.get(selectedRow);
        String novoStatus = "Ativo".equals(alerta.getStatus()) ? "Inativo" : "Ativo";
        
        
        // Atualizar status na tabela
        modelTabela.setValueAt(novoStatus, selectedRow, 3);
        alerta.setStatus(novoStatus);
        
        JOptionPane.showMessageDialog(workArea, 
            "Alerta " + novoStatus.toLowerCase() + " com sucesso!\n" +
            "Nome: " + alerta.getNome(), 
            "Sucesso", JOptionPane.INFORMATION_MESSAGE);
        
        SystemLogger.ui("Alerta " + novoStatus.toLowerCase() + ": " + alerta.getNome());
    }
    
    /**
     * Exclui alerta selecionado
     */
    private void excluirAlerta() {
        int selectedRow = tabelaAlertas.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(workArea, "Selecione um alerta para excluir!", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        Alerta alerta = alertasEncontrados.get(selectedRow);
        
        int confirmacao = JOptionPane.showConfirmDialog(
            workArea,
            "Deseja realmente excluir este alerta?\n\n" +
            "Nome: " + alerta.getNome() + "\n" +
            "Esta ação não pode ser desfeita.",
            "Excluir Alerta",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.QUESTION_MESSAGE
        );
        
        if (confirmacao == JOptionPane.YES_OPTION) {
            
            alertasEncontrados.remove(selectedRow);
            modelTabela.removeRow(selectedRow);
            
            JOptionPane.showMessageDialog(workArea, 
                "Alerta excluído com sucesso!", 
                "Sucesso", JOptionPane.INFORMATION_MESSAGE);
            
            SystemLogger.ui("Alerta excluído: " + alerta.getNome());
        }
    }
    
    /**
     * Testa alerta configurado
     */
    private void testarAlerta() {
        String nomeAlerta = txtNomeAlerta.getText().trim();
        String tipo = (String) comboTipo.getSelectedItem();
        String condicao = (String) comboCondicao.getSelectedItem();
        String valor = txtValor.getText().trim();
        
        if (nomeAlerta.isEmpty()) {
            JOptionPane.showMessageDialog(workArea, "Digite o nome do alerta!", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        if (valor.isEmpty()) {
            JOptionPane.showMessageDialog(workArea, "Digite o valor para testar!", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        JOptionPane.showMessageDialog(workArea, 
            "Testando alerta...\n\n" +
            "Nome: " + nomeAlerta + "\n" +
            "Tipo: " + tipo + "\n" +
            "Condição: " + condicao + " " + valor + "\n" +
            "Ação: " + comboAcao.getSelectedItem() + "\n\n" +
            "Simulação: Condição satisfeita!\n" +
            "Alerta seria disparado com sucesso.", 
            "Testar Alerta", JOptionPane.INFORMATION_MESSAGE);
        
        SystemLogger.ui("Alerta testado: " + nomeAlerta);
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
     * Classe interna para representar um alerta
     */
    private static class Alerta {
        private String nome;
        private String tipo;
        private String prioridade;
        private String status;
        private String condicao;
        private String valor;
        private String acao;
        private String ultimoDisparo;
        
        // Getters e Setters
        public String getNome() { return nome; }
        public void setNome(String nome) { this.nome = nome; }
        
        public String getTipo() { return tipo; }
        public void setTipo(String tipo) { this.tipo = tipo; }
        
        public String getPrioridade() { return prioridade; }
        public void setPrioridade(String prioridade) { this.prioridade = prioridade; }
        
        public String getStatus() { return status; }
        public void setStatus(String status) { this.status = status; }
        
        public String getCondicao() { return condicao; }
        public void setCondicao(String condicao) { this.condicao = condicao; }
        
        public String getValor() { return valor; }
        public void setValor(String valor) { this.valor = valor; }
        
        public String getAcao() { return acao; }
        public void setAcao(String acao) { this.acao = acao; }
        
        @SuppressWarnings("unused")
        public String getUltimoDisparo() { return ultimoDisparo; }
        public void setUltimoDisparo(String ultimoDisparo) { this.ultimoDisparo = ultimoDisparo; }
    }
}
