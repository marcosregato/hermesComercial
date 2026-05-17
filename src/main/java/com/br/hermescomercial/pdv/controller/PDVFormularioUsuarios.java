package com.br.hermescomercial.pdv.controller;

import com.br.hermescomercial.util.SystemLogger;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Classe especializada para o formulário de Gestão de Usuários
 * Estrutura: Header → Busca → Formulário → Tabela
 */
public class PDVFormularioUsuarios {
    
    private JPanel workArea;
    private String usuarioAtual;
    private String nomeUsuario;
    
    // Componentes do formulário
    private JTextField txtBusca;
    private JTextField txtNome;
    private JTextField txtUsuario;
    private JTextField txtEmail;
    private JTextField txtTelefone;
    private JComboBox<String> comboCargo;
    private JComboBox<String> comboStatus;
    private JPasswordField txtSenha;
    private JPasswordField txtConfirmarSenha;
    
    // Tabela de usuários
    private JTable tabelaUsuarios;
    private DefaultTableModel modelTabela;
    private List<Usuario> usuariosEncontrados;
    
    // Cores
    private static final Color WHITE = Color.WHITE;
    private static final Color ACCENT_COLOR = new Color(52, 152, 219);
    private static final Color SUCCESS_COLOR = new Color(39, 174, 96);
    private static final Color DANGER_COLOR = new Color(231, 76, 60);
    private static final Color WARNING_COLOR = new Color(241, 196, 15);
    private static final Color GRAY = new Color(149, 165, 166);
    
    public PDVFormularioUsuarios(JPanel workArea, String usuarioAtual, String nomeUsuario) {
        this.workArea = workArea;
        this.usuarioAtual = usuarioAtual;
        this.nomeUsuario = nomeUsuario;
        this.usuariosEncontrados = new ArrayList<>();
    }
    
    /**
     * Cria o formulário completo de Gestão de Usuários
     */
    public JPanel criarFormularioUsuarios() {
        try {
            SystemLogger.ui("=== CRIANDO FORMULÁRIO GESTÃO DE USUÁRIOS ===");
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
            
            // Tabela de usuários
            JPanel tabelaPanel = criarPainelTabela();
            painelCentral.add(tabelaPanel, BorderLayout.SOUTH);
            
            painelPrincipal.add(painelCentral, BorderLayout.CENTER);
            
            SystemLogger.ui("Formulário Gestão de Usuários criado com sucesso");
            return painelPrincipal;
            
        } catch (Exception e) {
            SystemLogger.error("Erro ao criar formulário Gestão de Usuários", e);
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
        JLabel titleLabel = new JLabel("👥 Usuários");
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
        txtBusca.setToolTipText("Digite nome, usuário ou email");
        
        JButton btnBuscar = new JButton("🔍 Buscar");
        btnBuscar.setBackground(ACCENT_COLOR);
        btnBuscar.setForeground(WHITE);
        btnBuscar.setFocusPainted(false);
        btnBuscar.setBorderPainted(false);
        btnBuscar.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btnBuscar.addActionListener(e -> buscarUsuarios());
        
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
        
        // Nome
        gbc.gridy = 1; gbc.gridwidth = 1;
        JLabel lblNome = new JLabel("Nome Completo:");
        lblNome.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        formularioPanel.add(lblNome, gbc);
        
        gbc.gridx = 1; gbc.weightx = 1.0;
        txtNome = new JTextField();
        txtNome.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        txtNome.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(GRAY),
            BorderFactory.createEmptyBorder(5, 8, 5, 8)
        ));
        formularioPanel.add(txtNome, gbc);
        
        // Email
        gbc.gridx = 2; gbc.weightx = 0.0;
        JLabel lblEmail = new JLabel("Email:");
        lblEmail.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        formularioPanel.add(lblEmail, gbc);
        
        gbc.gridx = 3; gbc.weightx = 1.0;
        txtEmail = new JTextField();
        txtEmail.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        txtEmail.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(GRAY),
            BorderFactory.createEmptyBorder(5, 8, 5, 8)
        ));
        txtEmail.setToolTipText("email@exemplo.com");
        formularioPanel.add(txtEmail, gbc);
        
        // Telefone
        gbc.gridy = 2; gbc.gridx = 0;
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
        txtTelefone.setToolTipText("(99) 99999-9999");
        formularioPanel.add(txtTelefone, gbc);
        
        // Seção Dados de Acesso
        gbc.gridy = 3; gbc.gridx = 0; gbc.gridwidth = 4; gbc.weightx = 0.0;
        JLabel acessoSectionLabel = new JLabel("🔐 Dados de Acesso");
        acessoSectionLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        acessoSectionLabel.setForeground(ACCENT_COLOR);
        formularioPanel.add(acessoSectionLabel, gbc);
        
        // Usuário
        gbc.gridy = 4; gbc.gridwidth = 1;
        JLabel lblUsuario = new JLabel("Usuário:");
        lblUsuario.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        formularioPanel.add(lblUsuario, gbc);
        
        gbc.gridx = 1; gbc.weightx = 1.0;
        txtUsuario = new JTextField();
        txtUsuario.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        txtUsuario.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(GRAY),
            BorderFactory.createEmptyBorder(5, 8, 5, 8)
        ));
        txtUsuario.setToolTipText("nome.usuario");
        formularioPanel.add(txtUsuario, gbc);
        
        // Cargo
        gbc.gridx = 2; gbc.weightx = 0.0;
        JLabel lblCargo = new JLabel("Cargo:");
        lblCargo.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        formularioPanel.add(lblCargo, gbc);
        
        gbc.gridx = 3; gbc.weightx = 1.0;
        comboCargo = new JComboBox<>(new String[]{"Administrador", "Gerente", "Operador", "Caixa", "Visualizador"});
        comboCargo.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        comboCargo.setBorder(BorderFactory.createLineBorder(GRAY));
        formularioPanel.add(comboCargo, gbc);
        
        // Senha
        gbc.gridy = 5; gbc.gridx = 0;
        JLabel lblSenha = new JLabel("Senha:");
        lblSenha.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        formularioPanel.add(lblSenha, gbc);
        
        gbc.gridx = 1; gbc.weightx = 1.0;
        txtSenha = new JPasswordField();
        txtSenha.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        txtSenha.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(GRAY),
            BorderFactory.createEmptyBorder(5, 8, 5, 8)
        ));
        txtSenha.setToolTipText("Mínimo 6 caracteres");
        formularioPanel.add(txtSenha, gbc);
        
        // Confirmar Senha
        gbc.gridx = 2; gbc.weightx = 0.0;
        JLabel lblConfirmarSenha = new JLabel("Confirmar Senha:");
        lblConfirmarSenha.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        formularioPanel.add(lblConfirmarSenha, gbc);
        
        gbc.gridx = 3; gbc.weightx = 1.0;
        txtConfirmarSenha = new JPasswordField();
        txtConfirmarSenha.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        txtConfirmarSenha.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(GRAY),
            BorderFactory.createEmptyBorder(5, 8, 5, 8)
        ));
        formularioPanel.add(txtConfirmarSenha, gbc);
        
        // Status
        gbc.gridy = 6; gbc.gridx = 0;
        JLabel lblStatus = new JLabel("Status:");
        lblStatus.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        formularioPanel.add(lblStatus, gbc);
        
        gbc.gridx = 1; gbc.weightx = 1.0;
        comboStatus = new JComboBox<>(new String[]{"Ativo", "Inativo", "Bloqueado"});
        comboStatus.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        comboStatus.setBorder(BorderFactory.createLineBorder(GRAY));
        formularioPanel.add(comboStatus, gbc);
        
        // Botões de ação
        gbc.gridy = 7; gbc.gridx = 0; gbc.gridwidth = 4; gbc.weightx = 0.0;
        JPanel botoesPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        botoesPanel.setBackground(WHITE);
        
        JButton btnSalvar = new JButton("💾 Salvar Usuário");
        btnSalvar.setBackground(SUCCESS_COLOR);
        btnSalvar.setForeground(WHITE);
        btnSalvar.setFocusPainted(false);
        btnSalvar.setBorderPainted(false);
        btnSalvar.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btnSalvar.addActionListener(e -> salvarUsuario());
        
        JButton btnLimpar = new JButton("🧹 Limpar");
        btnLimpar.setBackground(GRAY);
        btnLimpar.setForeground(WHITE);
        btnLimpar.setFocusPainted(false);
        btnLimpar.setBorderPainted(false);
        btnLimpar.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btnLimpar.addActionListener(e -> limparCampos());
        
        JButton btnResetarSenha = new JButton("🔑 Resetar Senha");
        btnResetarSenha.setBackground(WARNING_COLOR);
        btnResetarSenha.setForeground(WHITE);
        btnResetarSenha.setFocusPainted(false);
        btnResetarSenha.setBorderPainted(false);
        btnResetarSenha.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btnResetarSenha.addActionListener(e -> resetarSenha());
        
        botoesPanel.add(btnSalvar);
        botoesPanel.add(Box.createHorizontalStrut(10));
        botoesPanel.add(btnLimpar);
        botoesPanel.add(Box.createHorizontalStrut(10));
        botoesPanel.add(btnResetarSenha);
        
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
        
        JLabel tabelaLabel = new JLabel("📋 Usuários Cadastrados");
        tabelaLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        tabelaLabel.setForeground(ACCENT_COLOR);
        
        // Modelo da tabela
        String[] colunas = {"Nome", "Usuário", "Email", "Cargo", "Telefone", "Status", "Último Acesso", "Ações"};
        modelTabela = new DefaultTableModel(colunas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 7; // Apenas a coluna de ações é editável
            }
        };
        
        tabelaUsuarios = new JTable(modelTabela);
        tabelaUsuarios.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        tabelaUsuarios.setRowHeight(25);
        tabelaUsuarios.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 12));
        tabelaUsuarios.getTableHeader().setBackground(ACCENT_COLOR);
        tabelaUsuarios.getTableHeader().setForeground(WHITE);
        
        // Scroll pane
        JScrollPane scrollPane = new JScrollPane(tabelaUsuarios);
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
        btnEditar.addActionListener(e -> editarUsuario());
        
        JButton btnBloquear = new JButton("🔒 Bloquear");
        btnBloquear.setBackground(DANGER_COLOR);
        btnBloquear.setForeground(WHITE);
        btnBloquear.setFocusPainted(false);
        btnBloquear.setBorderPainted(false);
        btnBloquear.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btnBloquear.addActionListener(e -> bloquearUsuario());
        
        botoesTabelaPanel.add(btnEditar);
        botoesTabelaPanel.add(Box.createHorizontalStrut(10));
        botoesTabelaPanel.add(btnBloquear);
        
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
        usuariosEncontrados.clear();
        
        // Dados de exemplo
        Object[][] dadosExemplo = {
            {"João Silva", "joao.silva", "joao@hermes.com", "Administrador", "(11) 9999-8888", "Ativo", "10/05/2026 14:30", "✏️"},
            {"Maria Santos", "maria.santos", "maria@hermes.com", "Gerente", "(11) 9777-6666", "Ativo", "10/05/2026 13:15", "✏️"},
            {"Pedro Costa", "pedro.costa", "pedro@hermes.com", "Operador", "(11) 9555-4444", "Ativo", "10/05/2026 12:45", "✏️"},
            {"Ana Oliveira", "ana.oliveira", "ana@hermes.com", "Caixa", "(11) 9333-2222", "Ativo", "10/05/2026 11:20", "✏️"},
            {"Carlos Ferreira", "carlos.ferreira", "carlos@hermes.com", "Visualizador", "(11) 9111-0000", "Inativo", "09/05/2026 16:30", "✏️"},
            {"Lucas Pereira", "lucas.pereira", "lucas@hermes.com", "Operador", "(11) 9888-7777", "Bloqueado", "08/05/2026 10:15", "✏️"}
        };
        
        for (Object[] dados : dadosExemplo) {
            modelTabela.addRow(dados);
            
            // Adicionar à lista de usuários
            Usuario usuario = new Usuario();
            usuario.setNome((String) dados[0]);
            usuario.setUsuario((String) dados[1]);
            usuario.setEmail((String) dados[2]);
            usuario.setCargo((String) dados[3]);
            usuario.setTelefone((String) dados[4]);
            usuario.setStatus((String) dados[5]);
            usuario.setUltimoAcesso((String) dados[6]);
            usuariosEncontrados.add(usuario);
        }
    }
    
    /**
     * Preenche dados iniciais
     */
    private void preencherDadosIniciais() {
        comboCargo.setSelectedIndex(0);
        comboStatus.setSelectedIndex(0);
    }
    
    /**
     * Busca usuários
     */
    private void buscarUsuarios() {
        String termo = txtBusca.getText().trim();
        if (termo.isEmpty()) {
            JOptionPane.showMessageDialog(workArea, "Digite um termo para buscar!", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        JOptionPane.showMessageDialog(workArea, 
            "Busca realizada para: " + termo + "\n" +
            "Usuários encontrados: " + usuariosEncontrados.size(), 
            "Resultado", JOptionPane.INFORMATION_MESSAGE);
        
        SystemLogger.ui("Busca realizada para: " + termo);
    }
    
    /**
     * Salva usuário
     */
    private void salvarUsuario() {
        try {
            String nome = txtNome.getText().trim();
            String usuario = txtUsuario.getText().trim();
            String email = txtEmail.getText().trim();
            String senha = new String(txtSenha.getPassword());
            String confirmarSenha = new String(txtConfirmarSenha.getPassword());
            
            // Validação do Nome (Obrigatório *)
            if (nome.isEmpty()) {
                JOptionPane.showMessageDialog(workArea, "⚠️ O Nome Completo é obrigatório!\n\nPor favor, informe o nome completo do usuário.", "Validação - Campo Obrigatório", JOptionPane.WARNING_MESSAGE);
                txtNome.requestFocus();
                txtNome.setBorder(BorderFactory.createLineBorder(Color.RED, 2));
                return;
            }
            
            // Validação do Usuário (Obrigatório *)
            if (usuario.isEmpty()) {
                JOptionPane.showMessageDialog(workArea, "⚠️ O Nome de Usuário é obrigatório!\n\nPor favor, informe o nome de usuário para login.", "Validação - Campo Obrigatório", JOptionPane.WARNING_MESSAGE);
                txtUsuario.requestFocus();
                txtUsuario.setBorder(BorderFactory.createLineBorder(Color.RED, 2));
                return;
            }
            
            // Validação do Email (Obrigatório *)
            if (email.isEmpty()) {
                JOptionPane.showMessageDialog(workArea, "⚠️ O Email é obrigatório!\n\nPor favor, informe o email do usuário.", "Validação - Campo Obrigatório", JOptionPane.WARNING_MESSAGE);
                txtEmail.requestFocus();
                txtEmail.setBorder(BorderFactory.createLineBorder(Color.RED, 2));
                return;
            }
            
            // Validação do formato do Email
            if (!email.contains("@") || !email.contains(".")) {
                JOptionPane.showMessageDialog(workArea, "⚠️ Email inválido!\n\nPor favor, informe um email válido (ex: usuario@dominio.com).", "Validação - Campo Obrigatório", JOptionPane.WARNING_MESSAGE);
                txtEmail.requestFocus();
                txtEmail.setBorder(BorderFactory.createLineBorder(Color.RED, 2));
                return;
            }
            
            // Validação da Senha (Obrigatório para novos usuários)
            if (senha.isEmpty()) {
                JOptionPane.showMessageDialog(workArea, "⚠️ A Senha é obrigatória!\n\nPor favor, informe uma senha para o usuário.", "Validação - Campo Obrigatório", JOptionPane.WARNING_MESSAGE);
                txtSenha.requestFocus();
                txtSenha.setBorder(BorderFactory.createLineBorder(Color.RED, 2));
                return;
            }
            
            // Validação da confirmação de senha
            if (!senha.isEmpty() && !senha.equals(confirmarSenha)) {
                JOptionPane.showMessageDialog(workArea, "⚠️ As senhas não conferem!\n\nPor favor, digite a mesma senha nos campos Senha e Confirmar Senha.", "Validação - Campo Obrigatório", JOptionPane.WARNING_MESSAGE);
                txtSenha.requestFocus();
                txtSenha.setBorder(BorderFactory.createLineBorder(Color.RED, 2));
                txtConfirmarSenha.setBorder(BorderFactory.createLineBorder(Color.RED, 2));
                return;
            }
            
            // Validação do tamanho da senha
            if (!senha.isEmpty() && senha.length() < 6) {
                JOptionPane.showMessageDialog(workArea, "⚠️ A senha deve ter no mínimo 6 caracteres!\n\nPor favor, informe uma senha com pelo menos 6 caracteres.", "Validação - Campo Obrigatório", JOptionPane.WARNING_MESSAGE);
                txtSenha.requestFocus();
                txtSenha.setBorder(BorderFactory.createLineBorder(Color.RED, 2));
                return;
            }
            
            // Resetar bordas dos campos válidos
            txtNome.setBorder(BorderFactory.createLineBorder(Color.GRAY));
            txtUsuario.setBorder(BorderFactory.createLineBorder(Color.GRAY));
            txtEmail.setBorder(BorderFactory.createLineBorder(Color.GRAY));
            txtSenha.setBorder(BorderFactory.createLineBorder(Color.GRAY));
            txtConfirmarSenha.setBorder(BorderFactory.createLineBorder(Color.GRAY));
            
            JOptionPane.showMessageDialog(workArea, 
                "Usuário salvo com sucesso!\n\n" +
                "Nome: " + nome + "\n" +
                "Usuário: " + usuario + "\n" +
                "Email: " + email + "\n" +
                "Cargo: " + comboCargo.getSelectedItem() + "\n" +
                "Responsável: " + nomeUsuario, 
                "Sucesso", JOptionPane.INFORMATION_MESSAGE);
            
            SystemLogger.ui("Usuário salvo por: " + nomeUsuario);
            
        } catch (Exception e) {
            SystemLogger.error("Erro ao salvar usuário", e);
            JOptionPane.showMessageDialog(workArea, "Erro ao salvar usuário: " + e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    /**
     * Limpa os campos do formulário
     */
    private void limparCampos() {
        txtBusca.setText("");
        txtNome.setText("");
        txtUsuario.setText("");
        txtEmail.setText("");
        txtTelefone.setText("");
        comboCargo.setSelectedIndex(0);
        comboStatus.setSelectedIndex(0);
        txtSenha.setText("");
        txtConfirmarSenha.setText("");
        
        preencherDadosIniciais();
    }
    
    /**
     * Edita usuário selecionado
     */
    private void editarUsuario() {
        int selectedRow = tabelaUsuarios.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(workArea, "Selecione um usuário para editar!", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        Usuario usuario = usuariosEncontrados.get(selectedRow);
        
        // Preencher formulário com dados selecionados
        txtNome.setText(usuario.getNome());
        txtUsuario.setText(usuario.getUsuario());
        txtEmail.setText(usuario.getEmail());
        txtTelefone.setText(usuario.getTelefone());
        comboCargo.setSelectedItem(usuario.getCargo());
        comboStatus.setSelectedItem(usuario.getStatus());
        
        JOptionPane.showMessageDialog(workArea, 
            "Usuário carregado para edição:\n\n" +
            "Nome: " + usuario.getNome() + "\n" +
            "Usuário: " + usuario.getUsuario() + "\n" +
            "Cargo: " + usuario.getCargo(), 
            "Editar Usuário", JOptionPane.INFORMATION_MESSAGE);
        
        SystemLogger.ui("Usuário carregado para edição: " + usuario.getUsuario());
    }
    
    /**
     * Bloqueia usuário selecionado
     */
    private void bloquearUsuario() {
        int selectedRow = tabelaUsuarios.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(workArea, "Selecione um usuário para bloquear!", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        Usuario usuario = usuariosEncontrados.get(selectedRow);
        
        int confirmacao = JOptionPane.showConfirmDialog(
            workArea,
            "Deseja realmente bloquear o usuário?\n\n" +
            "Nome: " + usuario.getNome() + "\n" +
            "Usuário: " + usuario.getUsuario(),
            "Bloquear Usuário",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.QUESTION_MESSAGE
        );
        
        if (confirmacao == JOptionPane.YES_OPTION) {
            JOptionPane.showMessageDialog(workArea, 
                "Usuário bloqueado com sucesso!\n" +
                "Usuário: " + usuario.getUsuario() + "\n" +
                "Responsável: " + nomeUsuario, 
                "Sucesso", JOptionPane.INFORMATION_MESSAGE);
            
            SystemLogger.ui("Usuário bloqueado: " + usuario.getUsuario());
        }
    }
    
    /**
     * Resetar senha do usuário
     */
    private void resetarSenha() {
        String usuario = txtUsuario.getText().trim();
        
        if (usuario.isEmpty()) {
            JOptionPane.showMessageDialog(workArea, "Digite um usuário para resetar a senha!", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        int confirmacao = JOptionPane.showConfirmDialog(
            workArea,
            "Deseja resetar a senha do usuário?\n\n" +
            "Usuário: " + usuario + "\n" +
            "Nova senha temporária: 123456",
            "Resetar Senha",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.QUESTION_MESSAGE
        );
        
        if (confirmacao == JOptionPane.YES_OPTION) {
            JOptionPane.showMessageDialog(workArea, 
                "Senha resetada com sucesso!\n" +
                "Usuário: " + usuario + "\n" +
                "Nova senha: 123456\n" +
                "Responsável: " + nomeUsuario, 
                "Sucesso", JOptionPane.INFORMATION_MESSAGE);
            
            SystemLogger.ui("Senha resetada para: " + usuario);
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
     * Classe interna para representar um usuário
     */
    private static class Usuario {
        private String nome;
        private String usuario;
        private String email;
        private String cargo;
        private String telefone;
        private String status;
        private String ultimoAcesso;
        
        // Getters e Setters
        public String getNome() { return nome; }
        public void setNome(String nome) { this.nome = nome; }
        
        public String getUsuario() { return usuario; }
        public void setUsuario(String usuario) { this.usuario = usuario; }
        
        public String getEmail() { return email; }
        public void setEmail(String email) { this.email = email; }
        
        public String getCargo() { return cargo; }
        public void setCargo(String cargo) { this.cargo = cargo; }
        
        public String getTelefone() { return telefone; }
        public void setTelefone(String telefone) { this.telefone = telefone; }
        
        public String getStatus() { return status; }
        public void setStatus(String status) { this.status = status; }
        
        @SuppressWarnings("unused")
        public String getUltimoAcesso() { return ultimoAcesso; }
        public void setUltimoAcesso(String ultimoAcesso) { this.ultimoAcesso = ultimoAcesso; }
    }
}
