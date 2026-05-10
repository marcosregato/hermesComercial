package com.br.hermescomercial.pdv.controller;

import com.br.hermescomercial.ui.layout.LayoutPadrao;
import com.br.hermescomercial.util.SystemLogger;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

/**
 * Classe especializada para formulário de configurações do sistema
 * Segue o padrão Header → Busca → Formulário → Tabela
 * Versão 1.0.0 - Adaptada para PDVMenuLateralElegante
 */
public class PDVFormularioConfiguracoes {
    
    private JPanel workArea;
    private String usuarioAtual;
    private String nomeUsuario;
    
    // Abas do formulário
    private JTabbedPane tabbedPane;
    
    // Componentes da aba Empresa
    private JTextField txtNomeEmpresa;
    private JTextField txtCnpj;
    private JTextField txtEndereco;
    private JTextField txtTelefone;
    private JTextField txtEmail;
    
    // Componentes da aba Financeiro
    private JTextField txtTaxaJuros;
    private JTextField txtTaxaMultas;
    private JTextField txtLimiteCredito;
    private JComboBox<String> cbCondicaoPagamento;
    private JTextArea txtCondicoes;
    
    // Componentes da aba Sistema
    private JCheckBox cbImpressoraAutomatica;
    private JCheckBox cbBackupAutomatico;
    private JCheckBox cbLogAtividades;
    private JComboBox<String> cbTema;
    private JComboBox<String> cbIdioma;
    private JComboBox<String> cbBancoDados;
    
    // Componentes da aba Usuários
    private JTable usuariosTable;
    private DefaultTableModel usuariosModel;
    private List<Usuario> usuarios;
    
    // Cores
    private static final Color SUCCESS_COLOR = new Color(76, 175, 80);
    private static final Color DANGER_COLOR = new Color(244, 67, 54);
    private static final Color WARNING_COLOR = new Color(255, 193, 7);
    private static final Color PRIMARY_COLOR = new Color(33, 150, 243);
    
    public PDVFormularioConfiguracoes(JPanel workArea, String usuario, String nome) {
        this.workArea = workArea;
        this.usuarioAtual = usuario;
        this.nomeUsuario = nome;
        this.usuarios = new ArrayList<>();
        
        // Inicializar o usuariosModel antes de carregar dados
        String[] colunas = {"ID", "Nome", "Login", "Nível", "Status", "Último Acesso"};
        this.usuariosModel = new DefaultTableModel(colunas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        carregarDadosExemplo();
    }
    
    public JPanel criarFormularioConfiguracoes() {
        SystemLogger.ui("=== CRIANDO FORMULÁRIO CONFIGURAÇÕES ===");
        SystemLogger.ui("Usuário: " + usuarioAtual);
        
        JPanel painelPrincipal = new JPanel(new BorderLayout());
        painelPrincipal.setBackground(Color.WHITE);
        
        // Header
        painelPrincipal.add(criarHeader(), BorderLayout.NORTH);
        
        // Painel central com busca, formulário e tabela
        JPanel painelCentral = new JPanel(new BorderLayout());
        painelCentral.setBackground(Color.WHITE);
        
        // Painel de busca
        JPanel buscaPanel = criarPainelBusca();
        painelCentral.add(buscaPanel, BorderLayout.NORTH);
        
        // Painel do formulário
        JPanel formularioPanel = criarPainelFormulario();
        painelCentral.add(formularioPanel, BorderLayout.CENTER);
        
        // Tabela de configurações
        JPanel tabelaPanel = criarPainelTabela();
        painelCentral.add(tabelaPanel, BorderLayout.SOUTH);
        
        painelPrincipal.add(painelCentral, BorderLayout.CENTER);
        
        SystemLogger.ui("Formulário Configurações criado com sucesso");
        return painelPrincipal;
    }
    
    private JPanel criarHeader() {
        JPanel header = LayoutPadrao.criarHeaderPanel("⚙️ Configurações do Sistema");
        
        // Informações do usuário
        JPanel userInfo = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        userInfo.setBackground(new Color(250, 250, 250));
        userInfo.add(new JLabel("👤 " + nomeUsuario));
        userInfo.add(new JLabel(" | "));
        userInfo.add(new JLabel("📅 " + LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))));
        
        header.add(userInfo, BorderLayout.EAST);
        return header;
    }
    
    private JPanel criarPainelBusca() {
        JPanel painelBusca = new JPanel(new BorderLayout());
        painelBusca.setBackground(Color.WHITE);
        painelBusca.setBorder(BorderFactory.createTitledBorder("🔍 Busca Rápida de Configurações"));
        
        // Campos de busca
        JPanel camposBusca = new JPanel(new FlowLayout(FlowLayout.LEFT));
        camposBusca.setBackground(Color.WHITE);
        
        camposBusca.add(new JLabel("Pesquisar:"));
        JTextField txtBusca = new JTextField(20);
        camposBusca.add(txtBusca);
        
        camposBusca.add(new JLabel("Categoria:"));
        JComboBox<String> cbCategoria = new JComboBox<>(new String[]{
            "Todas", "Empresa", "Financeiro", "Sistema", "Usuários", "Banco de Dados"
        });
        camposBusca.add(cbCategoria);
        
        JButton btnBuscar = new JButton("🔍 Buscar");
        btnBuscar.setBackground(PRIMARY_COLOR);
        btnBuscar.setForeground(Color.WHITE);
        btnBuscar.addActionListener(e -> buscarConfiguracoes(txtBusca.getText(), (String) cbCategoria.getSelectedItem()));
        camposBusca.add(btnBuscar);
        
        JButton btnLimpar = new JButton("🗑️ Limpar");
        btnLimpar.addActionListener(e -> {
            txtBusca.setText("");
            cbCategoria.setSelectedIndex(0);
        });
        camposBusca.add(btnLimpar);
        
        painelBusca.add(camposBusca, BorderLayout.CENTER);
        
        return painelBusca;
    }
    
    private JPanel criarPainelFormulario() {
        JPanel painelFormulario = new JPanel(new BorderLayout());
        painelFormulario.setBackground(Color.WHITE);
        painelFormulario.setBorder(BorderFactory.createTitledBorder("📝 Configurações do Sistema"));
        
        // Manter as abas existentes como formulário principal
        painelFormulario.add(criarAbas(), BorderLayout.CENTER);
        
        return painelFormulario;
    }
    
    private JPanel criarPainelTabela() {
        JPanel painelTabela = new JPanel(new BorderLayout());
        painelTabela.setBackground(Color.WHITE);
        painelTabela.setBorder(BorderFactory.createTitledBorder("📋 Histórico de Alterações"));
        
        // Tabela de histórico
        String[] colunas = {"Data/Hora", "Usuário", "Configuração", "Valor Anterior", "Valor Novo", "Status"};
        DefaultTableModel tableModel = new DefaultTableModel(colunas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        JTable tabela = new JTable(tableModel);
        tabela.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tabela.getTableHeader().setReorderingAllowed(false);
        
        // Configurar larguras das colunas
        tabela.getColumnModel().getColumn(0).setPreferredWidth(150);
        tabela.getColumnModel().getColumn(1).setPreferredWidth(100);
        tabela.getColumnModel().getColumn(2).setPreferredWidth(150);
        tabela.getColumnModel().getColumn(3).setPreferredWidth(120);
        tabela.getColumnModel().getColumn(4).setPreferredWidth(120);
        tabela.getColumnModel().getColumn(5).setPreferredWidth(80);
        
        JScrollPane scrollPane = new JScrollPane(tabela);
        scrollPane.setPreferredSize(new Dimension(0, 200));
        
        painelTabela.add(scrollPane, BorderLayout.CENTER);
        
        // Carregar dados de exemplo
        carregarHistoricoExemplo(tableModel);
        
        return painelTabela;
    }
    
    private JTabbedPane criarAbas() {
        tabbedPane = new JTabbedPane();
        
        // Aba Empresa
        tabbedPane.addTab("🏢 Empresa", criarAbaEmpresa());
        
        // Aba Financeiro
        tabbedPane.addTab("💰 Financeiro", criarAbaFinanceiro());
        
        // Aba Sistema
        tabbedPane.addTab("⚙️ Sistema", criarAbaSistema());
        
        // Aba Usuários
        tabbedPane.addTab("👥 Usuários", criarAbaUsuarios());
        
        return tabbedPane;
    }
    
    private JPanel criarAbaEmpresa() {
        JPanel painel = new JPanel(new GridBagLayout());
        painel.setBackground(Color.WHITE);
        painel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.anchor = GridBagConstraints.WEST;
        
        // Nome da Empresa
        gbc.gridx = 0; gbc.gridy = 0;
        painel.add(new JLabel("Nome da Empresa*:"), gbc);
        gbc.gridx = 1; gbc.fill = GridBagConstraints.HORIZONTAL; gbc.weightx = 1.0;
        txtNomeEmpresa = new JTextField(30);
        painel.add(txtNomeEmpresa, gbc);
        
        // CNPJ
        gbc.gridx = 0; gbc.gridy = 1; gbc.fill = GridBagConstraints.NONE; gbc.weightx = 0;
        painel.add(new JLabel("CNPJ*:"), gbc);
        gbc.gridx = 1; gbc.fill = GridBagConstraints.HORIZONTAL; gbc.weightx = 1.0;
        txtCnpj = new JTextField(20);
        painel.add(txtCnpj, gbc);
        
        // Endereço
        gbc.gridx = 0; gbc.gridy = 2; gbc.fill = GridBagConstraints.NONE; gbc.weightx = 0;
        painel.add(new JLabel("Endereço:"), gbc);
        gbc.gridx = 1; gbc.fill = GridBagConstraints.HORIZONTAL; gbc.weightx = 1.0;
        txtEndereco = new JTextField(40);
        painel.add(txtEndereco, gbc);
        
        // Telefone
        gbc.gridx = 0; gbc.gridy = 3; gbc.fill = GridBagConstraints.NONE; gbc.weightx = 0;
        painel.add(new JLabel("Telefone*:"), gbc);
        gbc.gridx = 1; gbc.fill = GridBagConstraints.HORIZONTAL; gbc.weightx = 1.0;
        txtTelefone = new JTextField(20);
        painel.add(txtTelefone, gbc);
        
        // Email
        gbc.gridx = 0; gbc.gridy = 4; gbc.fill = GridBagConstraints.NONE; gbc.weightx = 0;
        painel.add(new JLabel("Email:"), gbc);
        gbc.gridx = 1; gbc.fill = GridBagConstraints.HORIZONTAL; gbc.weightx = 1.0;
        txtEmail = new JTextField(30);
        painel.add(txtEmail, gbc);
        
        // Logo
        gbc.gridx = 0; gbc.gridy = 5; gbc.gridwidth = 2;
        painel.add(new JLabel("Logo da Empresa:"), gbc);
        gbc.gridy = 6;
        JButton btnLogo = LayoutPadrao.criarBotaoSecundario("📷 Selecionar Logo");
        btnLogo.addActionListener(e -> selecionarLogo());
        painel.add(btnLogo, gbc);
        
        return painel;
    }
    
    private JPanel criarAbaFinanceiro() {
        JPanel painel = new JPanel(new GridBagLayout());
        painel.setBackground(Color.WHITE);
        painel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.anchor = GridBagConstraints.WEST;
        
        // Taxa de Juros
        gbc.gridx = 0; gbc.gridy = 0;
        painel.add(new JLabel("Taxa de Juros (%):"), gbc);
        gbc.gridx = 1; gbc.fill = GridBagConstraints.HORIZONTAL; gbc.weightx = 1.0;
        txtTaxaJuros = new JTextField("1.0");
        painel.add(txtTaxaJuros, gbc);
        
        // Taxa de Multas
        gbc.gridx = 0; gbc.gridy = 1; gbc.fill = GridBagConstraints.NONE; gbc.weightx = 0;
        painel.add(new JLabel("Taxa de Multas (%):"), gbc);
        gbc.gridx = 1; gbc.fill = GridBagConstraints.HORIZONTAL; gbc.weightx = 1.0;
        txtTaxaMultas = new JTextField("2.0");
        painel.add(txtTaxaMultas, gbc);
        
        // Limite de Crédito
        gbc.gridx = 0; gbc.gridy = 2; gbc.fill = GridBagConstraints.NONE; gbc.weightx = 0;
        painel.add(new JLabel("Limite de Crédito Padrão:"), gbc);
        gbc.gridx = 1; gbc.fill = GridBagConstraints.HORIZONTAL; gbc.weightx = 1.0;
        txtLimiteCredito = new JTextField("1000.00");
        painel.add(txtLimiteCredito, gbc);
        
        // Condições de Pagamento
        gbc.gridx = 0; gbc.gridy = 3; gbc.gridwidth = 2;
        painel.add(new JLabel("Condições de Pagamento:"), gbc);
        gbc.gridy = 4; gbc.gridwidth = 1; gbc.fill = GridBagConstraints.NONE; gbc.weightx = 0;
        painel.add(new JLabel("Forma Padrão:"), gbc);
        gbc.gridx = 1; gbc.fill = GridBagConstraints.HORIZONTAL; gbc.weightx = 1.0;
        cbCondicaoPagamento = new JComboBox<>(new String[]{
            "À Vista (5% desconto)",
            "30 dias (sem juros)", 
            "60 dias (2% juros)",
            "90 dias (3% juros)",
            "Personalizado"
        });
        painel.add(cbCondicaoPagamento, gbc);
        
        // Condições Adicionais
        gbc.gridx = 0; gbc.gridy = 5; gbc.gridwidth = 2; gbc.fill = GridBagConstraints.NONE; gbc.weightx = 0;
        painel.add(new JLabel("Condições Adicionais:"), gbc);
        gbc.gridy = 6; gbc.fill = GridBagConstraints.BOTH; gbc.weightx = 1.0; gbc.weighty = 0.2;
        txtCondicoes = new JTextArea(4, 40);
        txtCondicoes.setText("1. Pagamento à vista: 5% de desconto\n" +
                           "2. Pagamento em 30 dias: sem juros\n" +
                           "3. Pagamento em 60 dias: juros de 2%");
        JScrollPane scrollCondicoes = new JScrollPane(txtCondicoes);
        painel.add(scrollCondicoes, gbc);
        
        return painel;
    }
    
    private JPanel criarAbaSistema() {
        JPanel painel = new JPanel(new GridBagLayout());
        painel.setBackground(Color.WHITE);
        painel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.anchor = GridBagConstraints.WEST;
        
        // Configurações Gerais
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
        painel.add(new JLabel("Configurações Gerais:"), gbc);
        
        gbc.gridy = 1; gbc.gridwidth = 1;
        cbImpressoraAutomatica = new JCheckBox("Impressão automática de cupom");
        painel.add(cbImpressoraAutomatica, gbc);
        
        gbc.gridy = 2;
        cbBackupAutomatico = new JCheckBox("Backup automático diário");
        painel.add(cbBackupAutomatico, gbc);
        
        gbc.gridy = 3;
        cbLogAtividades = new JCheckBox("Registrar atividades do sistema");
        painel.add(cbLogAtividades, gbc);
        
        // Tema
        gbc.gridx = 0; gbc.gridy = 4;
        painel.add(new JLabel("Tema da Interface:"), gbc);
        gbc.gridx = 1;
        cbTema = new JComboBox<>(new String[]{"Claro", "Escuro", "Azul", "Verde"});
        painel.add(cbTema, gbc);
        
        // Idioma
        gbc.gridx = 0; gbc.gridy = 5;
        painel.add(new JLabel("Idioma:"), gbc);
        gbc.gridx = 1;
        cbIdioma = new JComboBox<>(new String[]{"Português", "Inglês", "Espanhol"});
        painel.add(cbIdioma, gbc);
        
        // Banco de Dados
        gbc.gridx = 0; gbc.gridy = 6; gbc.gridwidth = 2;
        painel.add(new JLabel("Banco de Dados:"), gbc);
        gbc.gridy = 7; gbc.gridwidth = 1; gbc.fill = GridBagConstraints.NONE; gbc.weightx = 0;
        painel.add(new JLabel("Tipo de Banco:"), gbc);
        gbc.gridx = 1; gbc.fill = GridBagConstraints.HORIZONTAL; gbc.weightx = 1.0;
        cbBancoDados = new JComboBox<>(new String[]{
            "MySQL", "PostgreSQL", "SQLite", "H2 Database", "Oracle", "SQL Server"
        });
        painel.add(cbBancoDados, gbc);
        
        // Botões de configuração
        gbc.gridx = 0; gbc.gridy = 8; gbc.gridwidth = 2; gbc.fill = GridBagConstraints.NONE; gbc.weightx = 0;
        JButton btnConfigDB = LayoutPadrao.criarBotaoPrimario("⚙️ Configurar Conexão");
        btnConfigDB.addActionListener(e -> configurarBancoDados());
        painel.add(btnConfigDB, gbc);
        
        gbc.gridy = 9;
        JButton btnPastaBackup = LayoutPadrao.criarBotaoSecundario("📁 Selecionar Pasta Backup");
        btnPastaBackup.addActionListener(e -> selecionarPastaBackup());
        painel.add(btnPastaBackup, gbc);
        
        return painel;
    }
    
    private JPanel criarAbaUsuarios() {
        JPanel painel = new JPanel(new BorderLayout());
        painel.setBackground(Color.WHITE);
        painel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        // Usar o usuariosModel já inicializado no construtor
        usuariosTable = new JTable(usuariosModel);
        usuariosTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        usuariosTable.getTableHeader().setReorderingAllowed(false);
        
        JScrollPane scrollPane = new JScrollPane(usuariosTable);
        painel.add(scrollPane, BorderLayout.CENTER);
        
        // Painel de botões
        JPanel buttonPanel = new JPanel(new FlowLayout());
        buttonPanel.setBorder(BorderFactory.createTitledBorder("Ações"));
        
        JButton btnNovo = LayoutPadrao.criarBotaoSucesso("👤 Novo Usuário");
        btnNovo.addActionListener(e -> novoUsuario());
        buttonPanel.add(btnNovo);
        
        JButton btnEditar = LayoutPadrao.criarBotaoPrimario("✏️ Editar Usuário");
        btnEditar.addActionListener(e -> editarUsuario());
        buttonPanel.add(btnEditar);
        
        JButton btnExcluir = LayoutPadrao.criarBotaoPerigo("🗑️ Excluir Usuário");
        btnExcluir.addActionListener(e -> excluirUsuario());
        buttonPanel.add(btnExcluir);
        
        JButton btnResetar = LayoutPadrao.criarBotaoAlerta("🔄 Resetar Senha");
        btnResetar.addActionListener(e -> resetarSenha());
        buttonPanel.add(btnResetar);
        
        painel.add(buttonPanel, BorderLayout.SOUTH);
        
        return painel;
    }
    
    private JPanel criarPainelAcoes() {
        JPanel painel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        painel.setBackground(Color.WHITE);
        painel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        JButton btnSalvar = LayoutPadrao.criarBotaoSucesso("💾 Salvar Configurações");
        btnSalvar.addActionListener(e -> salvarConfiguracoes());
        
        JButton btnRestaurar = LayoutPadrao.criarBotaoAlerta("🔄 Restaurar Padrão");
        btnRestaurar.addActionListener(e -> restaurarPadrao());
        
        JButton btnImportar = LayoutPadrao.criarBotaoSecundario("📥 Importar Config");
        btnImportar.addActionListener(e -> importarConfiguracoes());
        
        JButton btnExportar = LayoutPadrao.criarBotaoSecundario("📤 Exportar Config");
        btnExportar.addActionListener(e -> exportarConfiguracoes());
        
        JButton btnTestarDB = LayoutPadrao.criarBotaoPrimario("🔧 Testar Conexão DB");
        btnTestarDB.addActionListener(e -> testarConexaoDB());
        
        painel.add(btnSalvar);
        painel.add(btnRestaurar);
        painel.add(btnImportar);
        painel.add(btnExportar);
        painel.add(Box.createHorizontalStrut(10));
        painel.add(btnTestarDB);
        
        return painel;
    }
    
    // Métodos de ação
    private void selecionarLogo() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setFileFilter(new javax.swing.filechooser.FileNameExtensionFilter(
            "Imagens", "jpg", "jpeg", "png", "gif"));
        
        int result = fileChooser.showOpenDialog(workArea.getTopLevelAncestor());
        if (result == JFileChooser.APPROVE_OPTION) {
            JOptionPane.showMessageDialog(workArea, 
                "Logo selecionado: " + fileChooser.getSelectedFile().getName(),
                "Logo", JOptionPane.INFORMATION_MESSAGE);
        }
    }
    
    private void configurarBancoDados() {
        JOptionPane.showMessageDialog(workArea, 
            "Configuração de banco de dados em desenvolvimento...\n" +
            "Funcionalidade será implementada em breve!",
            "Configuração DB", JOptionPane.INFORMATION_MESSAGE);
        
        SystemLogger.ui("Configuração de banco de dados acessada por " + usuarioAtual);
    }
    
    private void selecionarPastaBackup() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        
        int result = fileChooser.showOpenDialog(workArea.getTopLevelAncestor());
        if (result == JFileChooser.APPROVE_OPTION) {
            JOptionPane.showMessageDialog(workArea, 
                "Pasta de backup selecionada: " + fileChooser.getSelectedFile().getAbsolutePath(),
                "Backup", JOptionPane.INFORMATION_MESSAGE);
        }
    }
    
    private void novoUsuario() {
        // Criar diálogo para novo usuário
        JDialog dialog = new JDialog((JFrame) workArea.getTopLevelAncestor(), "Novo Usuário", true);
        dialog.setSize(400, 350);
        dialog.setLocationRelativeTo(workArea.getTopLevelAncestor());
        
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.anchor = GridBagConstraints.WEST;
        
        // Nome do usuário
        gbc.gridx = 0; gbc.gridy = 0;
        panel.add(new JLabel("Nome:"), gbc);
        gbc.gridx = 1; gbc.fill = GridBagConstraints.HORIZONTAL; gbc.weightx = 1.0;
        JTextField txtNome = new JTextField(20);
        panel.add(txtNome, gbc);
        
        // Login
        gbc.gridx = 0; gbc.gridy = 1; gbc.fill = GridBagConstraints.NONE; gbc.weightx = 0;
        panel.add(new JLabel("Login:"), gbc);
        gbc.gridx = 1; gbc.fill = GridBagConstraints.HORIZONTAL; gbc.weightx = 1.0;
        JTextField txtLogin = new JTextField(20);
        panel.add(txtLogin, gbc);
        
        // Senha
        gbc.gridx = 0; gbc.gridy = 2; gbc.fill = GridBagConstraints.NONE; gbc.weightx = 0;
        panel.add(new JLabel("Senha:"), gbc);
        gbc.gridx = 1; gbc.fill = GridBagConstraints.HORIZONTAL; gbc.weightx = 1.0;
        JPasswordField txtSenha = new JPasswordField(20);
        panel.add(txtSenha, gbc);
        
        // Nível de acesso
        gbc.gridx = 0; gbc.gridy = 3; gbc.fill = GridBagConstraints.NONE; gbc.weightx = 0;
        panel.add(new JLabel("Nível:"), gbc);
        gbc.gridx = 1; gbc.fill = GridBagConstraints.HORIZONTAL; gbc.weightx = 1.0;
        JComboBox<String> cbNivel = new JComboBox<>(new String[]{"Administrador", "Operador", "Consultor"});
        panel.add(cbNivel, gbc);
        
        // Botões
        JPanel buttonPanel = new JPanel(new FlowLayout());
        JButton btnSalvar = LayoutPadrao.criarBotaoSucesso("💾 Salvar");
        btnSalvar.addActionListener(ev -> {
            String nome = txtNome.getText().trim();
            String login = txtLogin.getText().trim();
            String senha = new String(txtSenha.getPassword());
            String nivel = (String) cbNivel.getSelectedItem();
            
            if (nome.isEmpty() || login.isEmpty() || senha.isEmpty()) {
                JOptionPane.showMessageDialog(dialog, "Preencha todos os campos!", 
                    "Erro", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            // Adicionar novo usuário
            Usuario novoUsuario = new Usuario(
                usuarios.size() + 1,
                nome,
                login,
                nivel,
                "Ativo",
                LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"))
            );
            
            usuarios.add(novoUsuario);
            
            // Adicionar à tabela
            Object[] row = {
                novoUsuario.getId(),
                novoUsuario.getNome(),
                novoUsuario.getLogin(),
                novoUsuario.getNivel(),
                novoUsuario.getStatus(),
                novoUsuario.getUltimoAcesso()
            };
            usuariosModel.addRow(row);
            
            JOptionPane.showMessageDialog(dialog, "Usuário criado com sucesso!", 
                "Sucesso", JOptionPane.INFORMATION_MESSAGE);
            dialog.dispose();
        });
        
        JButton btnCancelar = LayoutPadrao.criarBotaoPerigo("❌ Cancelar");
        btnCancelar.addActionListener(ev -> dialog.dispose());
        
        buttonPanel.add(btnSalvar);
        buttonPanel.add(btnCancelar);
        
        gbc.gridx = 0; gbc.gridy = 4; gbc.gridwidth = 2; gbc.fill = GridBagConstraints.HORIZONTAL; gbc.weightx = 0;
        panel.add(buttonPanel, gbc);
        
        dialog.add(panel);
        dialog.setVisible(true);
    }
    
    private void editarUsuario() {
        int selectedRow = usuariosTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(workArea, "Selecione um usuário para editar!", 
                "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        JOptionPane.showMessageDialog(workArea, 
            "Edição de usuários em desenvolvimento...\n" +
            "Funcionalidade será implementada em breve!",
            "Editar Usuário", JOptionPane.INFORMATION_MESSAGE);
    }
    
    private void excluirUsuario() {
        int selectedRow = usuariosTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(workArea, "Selecione um usuário para excluir!", 
                "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        int confirmacao = JOptionPane.showConfirmDialog(workArea, 
            "Deseja realmente excluir este usuário?", "Confirmar Exclusão", 
            JOptionPane.YES_NO_OPTION);
        
        if (confirmacao == JOptionPane.YES_OPTION) {
            usuariosModel.removeRow(selectedRow);
            JOptionPane.showMessageDialog(workArea, "Usuário excluído com sucesso!", 
                "Sucesso", JOptionPane.INFORMATION_MESSAGE);
        }
    }
    
    private void resetarSenha() {
        int selectedRow = usuariosTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(workArea, "Selecione um usuário para resetar a senha!", 
                "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        JOptionPane.showMessageDialog(workArea, 
            "Reset de senha em desenvolvimento...\n" +
            "Funcionalidade será implementada em breve!",
            "Resetar Senha", JOptionPane.INFORMATION_MESSAGE);
    }
    
    private void salvarConfiguracoes() {
        // Validar campos obrigatórios
        if (txtNomeEmpresa.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(workArea, "Informe o nome da empresa!", 
                "Validação", JOptionPane.WARNING_MESSAGE);
            tabbedPane.setSelectedIndex(0); // Ir para aba Empresa
            txtNomeEmpresa.requestFocus();
            return;
        }
        
        if (txtCnpj.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(workArea, "Informe o CNPJ!", 
                "Validação", JOptionPane.WARNING_MESSAGE);
            tabbedPane.setSelectedIndex(0); // Ir para aba Empresa
            txtCnpj.requestFocus();
            return;
        }
        
        if (txtTelefone.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(workArea, "Informe o telefone!", 
                "Validação", JOptionPane.WARNING_MESSAGE);
            tabbedPane.setSelectedIndex(0); // Ir para aba Empresa
            txtTelefone.requestFocus();
            return;
        }
        
        // Simular salvamento
        JOptionPane.showMessageDialog(workArea, 
            "Configurações salvas com sucesso!\n\n" +
            "Empresa: " + txtNomeEmpresa.getText() + "\n" +
            "CNPJ: " + txtCnpj.getText() + "\n" +
            "Tema: " + cbTema.getSelectedItem() + "\n" +
            "Idioma: " + cbIdioma.getSelectedItem() + "\n\n" +
            "As configurações serão aplicadas ao reiniciar o sistema.",
            "Sucesso", JOptionPane.INFORMATION_MESSAGE);
        
        SystemLogger.ui("Configurações salvas por " + usuarioAtual);
    }
    
    private void restaurarPadrao() {
        int confirmacao = JOptionPane.showConfirmDialog(workArea, 
            "Deseja restaurar as configurações padrão?\n" +
            "Todas as configurações atuais serão perdidas!", 
            "Confirmar Restauração", JOptionPane.YES_NO_OPTION);
        
        if (confirmacao == JOptionPane.YES_OPTION) {
            // Restaurar valores padrão
            txtNomeEmpresa.setText("Hermes Comercial PDV");
            txtCnpj.setText("00.000.000/0000-00");
            txtEndereco.setText("Rua Principal, 123 - Centro");
            txtTelefone.setText("(00) 0000-0000");
            txtEmail.setText("contato@hermescomercial.com.br");
            
            txtTaxaJuros.setText("1.0");
            txtTaxaMultas.setText("2.0");
            txtLimiteCredito.setText("1000.00");
            cbCondicaoPagamento.setSelectedIndex(0);
            txtCondicoes.setText("1. Pagamento à vista: 5% de desconto\n2. Pagamento em 30 dias: sem juros");
            
            cbImpressoraAutomatica.setSelected(true);
            cbBackupAutomatico.setSelected(true);
            cbLogAtividades.setSelected(true);
            cbTema.setSelectedIndex(0);
            cbIdioma.setSelectedIndex(0);
            cbBancoDados.setSelectedIndex(0);
            
            JOptionPane.showMessageDialog(workArea, 
                "Configurações restauradas com sucesso!", 
                "Sucesso", JOptionPane.INFORMATION_MESSAGE);
            
            SystemLogger.ui("Configurações restauradas para o padrão por " + usuarioAtual);
        }
    }
    
    private void importarConfiguracoes() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setFileFilter(new javax.swing.filechooser.FileNameExtensionFilter(
            "Arquivos de Configuração", "json", "xml", "properties"));
        
        int result = fileChooser.showOpenDialog(workArea.getTopLevelAncestor());
        if (result == JFileChooser.APPROVE_OPTION) {
            JOptionPane.showMessageDialog(workArea, 
                "Importação de configurações em desenvolvimento...\n" +
                "Arquivo selecionado: " + fileChooser.getSelectedFile().getName(),
                "Importar", JOptionPane.INFORMATION_MESSAGE);
        }
    }
    
    private void exportarConfiguracoes() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setFileFilter(new javax.swing.filechooser.FileNameExtensionFilter(
            "Arquivos de Configuração", "json", "xml", "properties"));
        
        int result = fileChooser.showSaveDialog(workArea.getTopLevelAncestor());
        if (result == JFileChooser.APPROVE_OPTION) {
            JOptionPane.showMessageDialog(workArea, 
                "Exportação de configurações em desenvolvimento...\n" +
                "Arquivo: " + fileChooser.getSelectedFile().getName(),
                "Exportar", JOptionPane.INFORMATION_MESSAGE);
        }
    }
    
    private void buscarConfiguracoes(String termo, String categoria) {
        SystemLogger.ui("Buscando configurações - Termo: " + termo + ", Categoria: " + categoria);
        
        // Lógica de busca simulada
        if (termo.isEmpty() && "Todas".equals(categoria)) {
            JOptionPane.showMessageDialog(workArea, 
                "Por favor, informe um termo para buscar ou selecione uma categoria específica.",
                "Busca", JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        
        // Simulação de busca
        String mensagem = "Resultados encontrados para '" + termo + "' em " + categoria + ":\n\n";
        mensagem += "• Configuração de empresa atualizada\n";
        mensagem += "• Parâmetros de sistema modificados\n";
        mensagem += "• Usuários cadastrados no sistema";
        
        JOptionPane.showMessageDialog(workArea, mensagem, 
            "Resultados da Busca", JOptionPane.INFORMATION_MESSAGE);
        
        SystemLogger.ui("Busca realizada por " + usuarioAtual + ": " + termo + " em " + categoria);
    }
    
    private void carregarHistoricoExemplo(DefaultTableModel model) {
        // Adicionar dados de exemplo ao histórico
        Object[] row1 = {
            "08/05/2026 14:30:15", "admin", "Nome da Empresa", "Hermes PDV", "Hermes Comercial", "✅"
        };
        model.addRow(row1);
        
        Object[] row2 = {
            "08/05/2026 14:25:30", "admin", "Taxa de Juros", "1.5%", "1.0%", "✅"
        };
        model.addRow(row2);
        
        Object[] row3 = {
            "08/05/2026 14:20:45", "admin", "Backup Automático", "Desativado", "Ativado", "✅"
        };
        model.addRow(row3);
        
        Object[] row4 = {
            "08/05/2026 14:15:20", "admin", "Tema do Sistema", "Claro", "Escuro", "✅"
        };
        model.addRow(row4);
        
        Object[] row5 = {
            "08/05/2026 14:10:10", "admin", "Banco de Dados", "MySQL", "PostgreSQL", "✅"
        };
        model.addRow(row5);
        
        SystemLogger.ui("Histórico de configurações carregado com " + model.getRowCount() + " registros");
    }
    
    private void testarConexaoDB() {
        JOptionPane.showMessageDialog(workArea, 
            "Teste de conexão em desenvolvimento...\n" +
            "Funcionalidade será implementada em breve!",
            "Testar Conexão", JOptionPane.INFORMATION_MESSAGE);
    }
    
    private void carregarDadosExemplo() {
        // Carregar dados de exemplo para usuários
        Usuario usuario1 = new Usuario(1, "Administrador", "admin", "Administrador", "Ativo", 
            LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")));
        usuarios.add(usuario1);
        
        Usuario usuario2 = new Usuario(2, "Operador PDV", "operador", "Operador", "Ativo", 
            LocalDateTime.now().minusHours(2).format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")));
        usuarios.add(usuario2);
        
        Usuario usuario3 = new Usuario(3, "Consultor", "consultor", "Consultor", "Inativo", 
            LocalDateTime.now().minusDays(1).format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")));
        usuarios.add(usuario3);
        
        // Adicionar à tabela
        for (Usuario usuario : usuarios) {
            Object[] row = {
                usuario.getId(),
                usuario.getNome(),
                usuario.getLogin(),
                usuario.getNivel(),
                usuario.getStatus(),
                usuario.getUltimoAcesso()
            };
            usuariosModel.addRow(row);
        }
        
        // Carregar configurações de exemplo
        txtNomeEmpresa.setText("Hermes Comercial PDV");
        txtCnpj.setText("00.000.000/0000-00");
        txtEndereco.setText("Rua Principal, 123 - Centro");
        txtTelefone.setText("(00) 0000-0000");
        txtEmail.setText("contato@hermescomercial.com.br");
        
        cbImpressoraAutomatica.setSelected(true);
        cbBackupAutomatico.setSelected(true);
        cbLogAtividades.setSelected(true);
    }
    
    // Classe interna para representar usuários
    private static class Usuario {
        private Integer id;
        private String nome;
        private String login;
        private String nivel;
        private String status;
        private String ultimoAcesso;
        
        public Usuario(Integer id, String nome, String login, String nivel, String status, String ultimoAcesso) {
            this.id = id;
            this.nome = nome;
            this.login = login;
            this.nivel = nivel;
            this.status = status;
            this.ultimoAcesso = ultimoAcesso;
        }
        
        // Getters
        public Integer getId() { return id; }
        public String getNome() { return nome; }
        public String getLogin() { return login; }
        public String getNivel() { return nivel; }
        public String getStatus() { return status; }
        public String getUltimoAcesso() { return ultimoAcesso; }
    }
}
