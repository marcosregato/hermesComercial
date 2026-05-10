package com.br.hermescomercial.pdv.controller;

import com.br.hermescomercial.util.SystemLogger;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Classe especializada para o formulário de Gestão de Permissões
 * Estrutura: Header → Busca → Formulário → Tabela
 */
public class PDVFormularioPermissoes {
    
    private JPanel workArea;
    private String usuarioAtual;
    private String nomeUsuario;
    
    // Componentes do formulário
    private JTextField txtBusca;
    private JTextField txtNomePermissao;
    private JTextField txtDescricao;
    private JComboBox<String> comboCargo;
    private JComboBox<String> comboModulo;
    private JComboBox<String> comboStatus;
    private JTextArea txtObservacoes;
    
    // Checkboxes de permissões
    private JCheckBox chkVisualizar;
    private JCheckBox chkCriar;
    private JCheckBox chkEditar;
    private JCheckBox chkExcluir;
    private JCheckBox chkImprimir;
    private JCheckBox chkExportar;
    
    // Tabela de permissões
    private JTable tabelaPermissoes;
    private DefaultTableModel modelTabela;
    private List<Permissao> permissoesEncontrados;
    
    // Cores
    private static final Color WHITE = Color.WHITE;
    private static final Color ACCENT_COLOR = new Color(52, 152, 219);
    private static final Color SUCCESS_COLOR = new Color(39, 174, 96);
    private static final Color DANGER_COLOR = new Color(231, 76, 60);
    private static final Color WARNING_COLOR = new Color(241, 196, 15);
    private static final Color GRAY = new Color(149, 165, 166);
    
    public PDVFormularioPermissoes(JPanel workArea, String usuarioAtual, String nomeUsuario) {
        this.workArea = workArea;
        this.usuarioAtual = usuarioAtual;
        this.nomeUsuario = nomeUsuario;
        this.permissoesEncontrados = new ArrayList<>();
    }
    
    /**
     * Cria o formulário completo de Gestão de Permissões
     */
    public JPanel criarFormularioPermissoes() {
        try {
            SystemLogger.ui("=== CRIANDO FORMULÁRIO GESTÃO DE PERMISSÕES ===");
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
            
            // Tabela de permissões
            JPanel tabelaPanel = criarPainelTabela();
            painelCentral.add(tabelaPanel, BorderLayout.SOUTH);
            
            painelPrincipal.add(painelCentral, BorderLayout.CENTER);
            
            SystemLogger.ui("Formulário Gestão de Permissões criado com sucesso");
            return painelPrincipal;
            
        } catch (Exception e) {
            SystemLogger.error("Erro ao criar formulário Gestão de Permissões", e);
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
        JLabel titleLabel = new JLabel("🔐 Permissões");
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
        txtBusca.setToolTipText("Digite nome, cargo ou módulo");
        
        JButton btnBuscar = new JButton("🔍 Buscar");
        btnBuscar.setBackground(ACCENT_COLOR);
        btnBuscar.setForeground(WHITE);
        btnBuscar.setFocusPainted(false);
        btnBuscar.setBorderPainted(false);
        btnBuscar.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btnBuscar.addActionListener(e -> buscarPermissoes());
        
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
        
        // Seção Dados da Permissão
        JLabel dadosSectionLabel = new JLabel("🔐 Dados da Permissão");
        dadosSectionLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        dadosSectionLabel.setForeground(ACCENT_COLOR);
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 4;
        formularioPanel.add(dadosSectionLabel, gbc);
        
        // Nome Permissão
        gbc.gridy = 1; gbc.gridwidth = 1;
        JLabel lblNomePermissao = new JLabel("Nome da Permissão:");
        lblNomePermissao.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        formularioPanel.add(lblNomePermissao, gbc);
        
        gbc.gridx = 1; gbc.weightx = 1.0;
        txtNomePermissao = new JTextField();
        txtNomePermissao.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        txtNomePermissao.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(GRAY),
            BorderFactory.createEmptyBorder(5, 8, 5, 8)
        ));
        formularioPanel.add(txtNomePermissao, gbc);
        
        // Cargo
        gbc.gridx = 2; gbc.weightx = 0.0;
        JLabel lblCargo = new JLabel("Cargo:");
        lblCargo.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        formularioPanel.add(lblCargo, gbc);
        
        gbc.gridx = 3; gbc.weightx = 1.0;
        comboCargo = new JComboBox<>(new String[]{"Administrador", "Gerente", "Operador", "Caixa", "Visualizador"});
        comboCargo.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        comboCargo.setBorder(BorderFactory.createLineBorder(GRAY));
        comboCargo.addActionListener(e -> atualizarPermissoesPadrao());
        formularioPanel.add(comboCargo, gbc);
        
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
        
        // Módulo
        gbc.gridy = 3; gbc.gridx = 0; gbc.gridwidth = 1;
        JLabel lblModulo = new JLabel("Módulo:");
        lblModulo.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        formularioPanel.add(lblModulo, gbc);
        
        gbc.gridx = 1; gbc.weightx = 1.0;
        comboModulo = new JComboBox<>(new String[]{"Todos", "Vendas", "Estoque", "Financeiro", "Relatórios", "Configurações", "Clientes", "Produtos"});
        comboModulo.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        comboModulo.setBorder(BorderFactory.createLineBorder(GRAY));
        formularioPanel.add(comboModulo, gbc);
        
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
        
        // Seção Permissões Específicas
        gbc.gridy = 4; gbc.gridx = 0; gbc.gridwidth = 4; gbc.weightx = 0.0;
        JLabel permissoesSectionLabel = new JLabel("⚙️ Permissões Específicas");
        permissoesSectionLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        permissoesSectionLabel.setForeground(ACCENT_COLOR);
        formularioPanel.add(permissoesSectionLabel, gbc);
        
        // Painel de checkboxes
        JPanel checkboxesPanel = new JPanel(new GridLayout(2, 3, 10, 10));
        checkboxesPanel.setBackground(WHITE);
        
        chkVisualizar = new JCheckBox("Visualizar");
        chkVisualizar.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        chkVisualizar.setBackground(WHITE);
        
        chkCriar = new JCheckBox("Criar");
        chkCriar.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        chkCriar.setBackground(WHITE);
        
        chkEditar = new JCheckBox("Editar");
        chkEditar.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        chkEditar.setBackground(WHITE);
        
        chkExcluir = new JCheckBox("Excluir");
        chkExcluir.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        chkExcluir.setBackground(WHITE);
        
        chkImprimir = new JCheckBox("Imprimir");
        chkImprimir.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        chkImprimir.setBackground(WHITE);
        
        chkExportar = new JCheckBox("Exportar");
        chkExportar.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        chkExportar.setBackground(WHITE);
        
        checkboxesPanel.add(chkVisualizar);
        checkboxesPanel.add(chkCriar);
        checkboxesPanel.add(chkEditar);
        checkboxesPanel.add(chkExcluir);
        checkboxesPanel.add(chkImprimir);
        checkboxesPanel.add(chkExportar);
        
        gbc.gridy = 5; gbc.gridx = 0; gbc.gridwidth = 4;
        formularioPanel.add(checkboxesPanel, gbc);
        
        // Botões de ação
        gbc.gridy = 6; gbc.gridx = 0; gbc.gridwidth = 4; gbc.weightx = 0.0;
        JPanel botoesPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        botoesPanel.setBackground(WHITE);
        
        JButton btnSalvar = new JButton("💾 Salvar Permissão");
        btnSalvar.setBackground(SUCCESS_COLOR);
        btnSalvar.setForeground(WHITE);
        btnSalvar.setFocusPainted(false);
        btnSalvar.setBorderPainted(false);
        btnSalvar.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btnSalvar.addActionListener(e -> salvarPermissao());
        
        JButton btnLimpar = new JButton("🧹 Limpar");
        btnLimpar.setBackground(GRAY);
        btnLimpar.setForeground(WHITE);
        btnLimpar.setFocusPainted(false);
        btnLimpar.setBorderPainted(false);
        btnLimpar.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btnLimpar.addActionListener(e -> limparCampos());
        
        JButton btnAplicarTodos = new JButton("🔄 Aplicar a Todos");
        btnAplicarTodos.setBackground(ACCENT_COLOR);
        btnAplicarTodos.setForeground(WHITE);
        btnAplicarTodos.setFocusPainted(false);
        btnAplicarTodos.setBorderPainted(false);
        btnAplicarTodos.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btnAplicarTodos.addActionListener(e -> aplicarATodos());
        
        botoesPanel.add(btnSalvar);
        botoesPanel.add(Box.createHorizontalStrut(10));
        botoesPanel.add(btnLimpar);
        botoesPanel.add(Box.createHorizontalStrut(10));
        botoesPanel.add(btnAplicarTodos);
        
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
        
        JLabel tabelaLabel = new JLabel("📋 Permissões Configuradas");
        tabelaLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        tabelaLabel.setForeground(ACCENT_COLOR);
        
        // Modelo da tabela
        String[] colunas = {"Nome", "Cargo", "Módulo", "Visualizar", "Criar", "Editar", "Excluir", "Imprimir", "Exportar", "Status", "Ações"};
        modelTabela = new DefaultTableModel(colunas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 10; // Apenas a coluna de ações é editável
            }
            
            @Override
            public Class<?> getColumnClass(int columnIndex) {
                if (columnIndex >= 3 && columnIndex <= 8) {
                    return Boolean.class; // Colunas de permissões como Boolean
                }
                return String.class;
            }
        };
        
        tabelaPermissoes = new JTable(modelTabela);
        tabelaPermissoes.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        tabelaPermissoes.setRowHeight(25);
        tabelaPermissoes.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 12));
        tabelaPermissoes.getTableHeader().setBackground(ACCENT_COLOR);
        tabelaPermissoes.getTableHeader().setForeground(WHITE);
        
        // Scroll pane
        JScrollPane scrollPane = new JScrollPane(tabelaPermissoes);
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
        btnEditar.addActionListener(e -> editarPermissao());
        
        JButton btnClonar = new JButton("📋 Clonar");
        btnClonar.setBackground(SUCCESS_COLOR);
        btnClonar.setForeground(WHITE);
        btnClonar.setFocusPainted(false);
        btnClonar.setBorderPainted(false);
        btnClonar.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btnClonar.addActionListener(e -> clonarPermissao());
        
        botoesTabelaPanel.add(btnEditar);
        botoesTabelaPanel.add(Box.createHorizontalStrut(10));
        botoesTabelaPanel.add(btnClonar);
        
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
        permissoesEncontrados.clear();
        
        // Dados de exemplo
        Object[][] dadosExemplo = {
            {"Acesso Completo", "Administrador", "Todos", true, true, true, true, true, true, "Ativo", "✏️"},
            {"Gestão de Vendas", "Gerente", "Vendas", true, true, true, false, true, true, "Ativo", "✏️"},
            {"Operações de Caixa", "Operador", "Vendas", true, true, false, false, true, false, "Ativo", "✏️"},
            {"Consulta de Dados", "Caixa", "Todos", true, false, false, false, true, false, "Ativo", "✏️"},
            {"Visualização Apenas", "Visualizador", "Todos", true, false, false, false, true, false, "Ativo", "✏️"},
            {"Relatórios Financeiros", "Gerente", "Financeiro", true, false, false, false, true, true, "Ativo", "✏️"}
        };
        
        for (Object[] dados : dadosExemplo) {
            modelTabela.addRow(dados);
            
            // Adicionar à lista de permissões
            Permissao permissao = new Permissao();
            permissao.setNome((String) dados[0]);
            permissao.setCargo((String) dados[1]);
            permissao.setModulo((String) dados[2]);
            permissao.setVisualizar((Boolean) dados[3]);
            permissao.setCriar((Boolean) dados[4]);
            permissao.setEditar((Boolean) dados[5]);
            permissao.setExcluir((Boolean) dados[6]);
            permissao.setImprimir((Boolean) dados[7]);
            permissao.setExportar((Boolean) dados[8]);
            permissao.setStatus((String) dados[9]);
            permissoesEncontrados.add(permissao);
        }
    }
    
    /**
     * Preenche dados iniciais
     */
    private void preencherDadosIniciais() {
        comboCargo.setSelectedIndex(0);
        comboModulo.setSelectedIndex(0);
        comboStatus.setSelectedIndex(0);
        
        // Marcar permissões padrão para Administrador
        atualizarPermissoesPadrao();
    }
    
    /**
     * Atualiza permissões padrão baseado no cargo
     */
    private void atualizarPermissoesPadrao() {
        String cargo = (String) comboCargo.getSelectedItem();
        
        // Limpar todas as permissões
        chkVisualizar.setSelected(false);
        chkCriar.setSelected(false);
        chkEditar.setSelected(false);
        chkExcluir.setSelected(false);
        chkImprimir.setSelected(false);
        chkExportar.setSelected(false);
        
        // Aplicar permissões padrão baseado no cargo
        switch (cargo) {
            case "Administrador":
                chkVisualizar.setSelected(true);
                chkCriar.setSelected(true);
                chkEditar.setSelected(true);
                chkExcluir.setSelected(true);
                chkImprimir.setSelected(true);
                chkExportar.setSelected(true);
                break;
            case "Gerente":
                chkVisualizar.setSelected(true);
                chkCriar.setSelected(true);
                chkEditar.setSelected(true);
                chkExcluir.setSelected(false);
                chkImprimir.setSelected(true);
                chkExportar.setSelected(true);
                break;
            case "Operador":
                chkVisualizar.setSelected(true);
                chkCriar.setSelected(true);
                chkEditar.setSelected(false);
                chkExcluir.setSelected(false);
                chkImprimir.setSelected(true);
                chkExportar.setSelected(false);
                break;
            case "Caixa":
                chkVisualizar.setSelected(true);
                chkCriar.setSelected(false);
                chkEditar.setSelected(false);
                chkExcluir.setSelected(false);
                chkImprimir.setSelected(true);
                chkExportar.setSelected(false);
                break;
            case "Visualizador":
                chkVisualizar.setSelected(true);
                chkCriar.setSelected(false);
                chkEditar.setSelected(false);
                chkExcluir.setSelected(false);
                chkImprimir.setSelected(true);
                chkExportar.setSelected(false);
                break;
        }
    }
    
    /**
     * Busca permissões
     */
    private void buscarPermissoes() {
        String termo = txtBusca.getText().trim();
        if (termo.isEmpty()) {
            JOptionPane.showMessageDialog(workArea, "Digite um termo para buscar!", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        // TODO: Implementar lógica de busca no banco de dados
        JOptionPane.showMessageDialog(workArea, 
            "Busca realizada para: " + termo + "\n" +
            "Permissões encontradas: " + permissoesEncontrados.size(), 
            "Resultado", JOptionPane.INFORMATION_MESSAGE);
        
        SystemLogger.ui("Busca realizada para: " + termo);
    }
    
    /**
     * Salva permissão
     */
    private void salvarPermissao() {
        try {
            String nomePermissao = txtNomePermissao.getText().trim();
            String cargo = (String) comboCargo.getSelectedItem();
            String modulo = (String) comboModulo.getSelectedItem();
            
            if (nomePermissao.isEmpty()) {
                JOptionPane.showMessageDialog(workArea, "Digite o nome da permissão!", "Aviso", JOptionPane.WARNING_MESSAGE);
                return;
            }
            
            if (!chkVisualizar.isSelected()) {
                JOptionPane.showMessageDialog(workArea, "A permissão de visualização é obrigatória!", "Aviso", JOptionPane.WARNING_MESSAGE);
                return;
            }
            
            // TODO: Implementar lógica de salvamento no banco de dados
            JOptionPane.showMessageDialog(workArea, 
                "Permissão salva com sucesso!\n\n" +
                "Nome: " + nomePermissao + "\n" +
                "Cargo: " + cargo + "\n" +
                "Módulo: " + modulo + "\n" +
                "Visualizar: " + chkVisualizar.isSelected() + "\n" +
                "Criar: " + chkCriar.isSelected() + "\n" +
                "Editar: " + chkEditar.isSelected() + "\n" +
                "Excluir: " + chkExcluir.isSelected() + "\n" +
                "Responsável: " + nomeUsuario, 
                "Sucesso", JOptionPane.INFORMATION_MESSAGE);
            
            SystemLogger.ui("Permissão salva por: " + nomeUsuario);
            
        } catch (Exception e) {
            SystemLogger.error("Erro ao salvar permissão", e);
            JOptionPane.showMessageDialog(workArea, "Erro ao salvar permissão: " + e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    /**
     * Limpa os campos do formulário
     */
    private void limparCampos() {
        txtBusca.setText("");
        txtNomePermissao.setText("");
        txtDescricao.setText("");
        comboCargo.setSelectedIndex(0);
        comboModulo.setSelectedIndex(0);
        comboStatus.setSelectedIndex(0);
        
        preencherDadosIniciais();
    }
    
    /**
     * Edita permissão selecionada
     */
    private void editarPermissao() {
        int selectedRow = tabelaPermissoes.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(workArea, "Selecione uma permissão para editar!", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        Permissao permissao = permissoesEncontrados.get(selectedRow);
        
        // Preencher formulário com dados selecionados
        txtNomePermissao.setText(permissao.getNome());
        comboCargo.setSelectedItem(permissao.getCargo());
        comboModulo.setSelectedItem(permissao.getModulo());
        comboStatus.setSelectedItem(permissao.getStatus());
        
        // Marcar checkboxes
        chkVisualizar.setSelected(permissao.isVisualizar());
        chkCriar.setSelected(permissao.isCriar());
        chkEditar.setSelected(permissao.isEditar());
        chkExcluir.setSelected(permissao.isExcluir());
        chkImprimir.setSelected(permissao.isImprimir());
        chkExportar.setSelected(permissao.isExportar());
        
        JOptionPane.showMessageDialog(workArea, 
            "Permissão carregada para edição:\n\n" +
            "Nome: " + permissao.getNome() + "\n" +
            "Cargo: " + permissao.getCargo() + "\n" +
            "Módulo: " + permissao.getModulo(), 
            "Editar Permissão", JOptionPane.INFORMATION_MESSAGE);
        
        SystemLogger.ui("Permissão carregada para edição: " + permissao.getNome());
    }
    
    /**
     * Clona permissão selecionada
     */
    private void clonarPermissao() {
        int selectedRow = tabelaPermissoes.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(workArea, "Selecione uma permissão para clonar!", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        Permissao permissao = permissoesEncontrados.get(selectedRow);
        
        // Preencher formulário com dados selecionados (exceto nome)
        txtNomePermissao.setText(permissao.getNome() + " - Cópia");
        comboCargo.setSelectedItem(permissao.getCargo());
        comboModulo.setSelectedItem(permissao.getModulo());
        comboStatus.setSelectedItem(permissao.getStatus());
        
        // Marcar checkboxes
        chkVisualizar.setSelected(permissao.isVisualizar());
        chkCriar.setSelected(permissao.isCriar());
        chkEditar.setSelected(permissao.isEditar());
        chkExcluir.setSelected(permissao.isExcluir());
        chkImprimir.setSelected(permissao.isImprimir());
        chkExportar.setSelected(permissao.isExportar());
        
        JOptionPane.showMessageDialog(workArea, 
            "Permissão clonada com sucesso!\n" +
            "Nome: " + permissao.getNome() + " - Cópia\n" +
            "Edite os dados necessários e salve.", 
            "Clonar Permissão", JOptionPane.INFORMATION_MESSAGE);
        
        SystemLogger.ui("Permissão clonada: " + permissao.getNome());
    }
    
    /**
     * Aplica permissão a todos os módulos
     */
    private void aplicarATodos() {
        String nomePermissao = txtNomePermissao.getText().trim();
        
        if (nomePermissao.isEmpty()) {
            JOptionPane.showMessageDialog(workArea, "Digite o nome da permissão!", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        comboModulo.setSelectedIndex(0); // Selecionar "Todos"
        
        JOptionPane.showMessageDialog(workArea, 
            "Permissão aplicada a todos os módulos!\n" +
            "Nome: " + nomePermissao + "\n" +
            "Cargo: " + comboCargo.getSelectedItem(), 
            "Aplicar a Todos", JOptionPane.INFORMATION_MESSAGE);
        
        SystemLogger.ui("Permissão aplicada a todos os módulos: " + nomePermissao);
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
     * Classe interna para representar uma permissão
     */
    private static class Permissao {
        private String nome;
        private String cargo;
        private String modulo;
        private boolean visualizar;
        private boolean criar;
        private boolean editar;
        private boolean excluir;
        private boolean imprimir;
        private boolean exportar;
        private String status;
        
        // Getters e Setters
        public String getNome() { return nome; }
        public void setNome(String nome) { this.nome = nome; }
        
        public String getCargo() { return cargo; }
        public void setCargo(String cargo) { this.cargo = cargo; }
        
        public String getModulo() { return modulo; }
        public void setModulo(String modulo) { this.modulo = modulo; }
        
        public boolean isVisualizar() { return visualizar; }
        public void setVisualizar(boolean visualizar) { this.visualizar = visualizar; }
        
        public boolean isCriar() { return criar; }
        public void setCriar(boolean criar) { this.criar = criar; }
        
        public boolean isEditar() { return editar; }
        public void setEditar(boolean editar) { this.editar = editar; }
        
        public boolean isExcluir() { return excluir; }
        public void setExcluir(boolean excluir) { this.excluir = excluir; }
        
        public boolean isImprimir() { return imprimir; }
        public void setImprimir(boolean imprimir) { this.imprimir = imprimir; }
        
        public boolean isExportar() { return exportar; }
        public void setExportar(boolean exportar) { this.exportar = exportar; }
        
        public String getStatus() { return status; }
        public void setStatus(String status) { this.status = status; }
    }
}
