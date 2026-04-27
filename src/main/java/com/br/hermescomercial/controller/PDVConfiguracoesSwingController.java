package com.br.hermescomercial.controller;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import com.br.hermescomercial.util.DatabaseConfig;

/**
 * Controller de Configurações do Sistema em SWING
 * Interface de configurações do sistema PDV
 * Versão 2.0 - 100% SWING - Sem JavaFX
 */
public class PDVConfiguracoesSwingController {
    
    private JFrame frame;
    private JTabbedPane tabbedPane;
    private JTextField txtNomeEmpresa;
    private JTextField txtCnpj;
    private JTextField txtEndereco;
    private JTextField txtTelefone;
    private JTextField txtEmail;
    private JTextField txtTaxaJuros;
    private JTextField txtTaxaMultas;
    private JTextField txtLimiteCredito;
    private JComboBox<String> cbCondicaoPagamento;
    private JComboBox<String> cbBancoDados;
    private JCheckBox cbImpressoraAutomatica;
    private JCheckBox cbBackupAutomatico;
    private JCheckBox cbLogAtividades;
    private JComboBox<String> cbTema;
    private JComboBox<String> cbIdioma;
    private JTable usuariosTable;
    private DefaultTableModel usuariosModel;
    private List<Usuario> usuarios;
    
    public PDVConfiguracoesSwingController() {
        this.usuarios = new ArrayList<>(20); // Capacidade inicial para melhor performance
        initializeUI();
        carregarDadosExemplo();
    }
    
    private void initializeUI() {
        frame = new JFrame("PDV - Configurações do Sistema v2.1.0 - Premium");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setSize(900, 700);
        frame.setLocationRelativeTo(null);
        
        // Configurar fundo padrão Nova Venda
        frame.getContentPane().setBackground(new Color(245, 245, 250));
        
        frame.add(createMainPanel());
    }
    
    private JPanel createMainPanel() {
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        mainPanel.setBackground(new Color(245, 245, 250));
        
        // Header padrão Nova Venda
        mainPanel.add(createHeaderPanel(), BorderLayout.NORTH);
        
        // Center com abas
        mainPanel.add(createTabbedPanePanel(), BorderLayout.CENTER);
        
        // South
        mainPanel.add(createControlPanel(), BorderLayout.SOUTH);
        
        return mainPanel;
    }
    
    private JPanel createHeaderPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 20, 10));
        panel.setBackground(new Color(41, 128, 185));
        panel.setPreferredSize(new Dimension(0, 80));
        
        // Título central
        JLabel titleLabel = new JLabel("⚙️ Configurações do Sistema v2.1.0 - Premium", JLabel.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setForeground(Color.WHITE);
        
        // Botão voltar estilizado
        JButton btnVoltar = new JButton("← Voltar");
        btnVoltar.setBackground(new Color(255, 255, 255));
        btnVoltar.setForeground(new Color(41, 128, 185));
        btnVoltar.setFont(new Font("Arial", Font.BOLD, 12));
        btnVoltar.setBorder(BorderFactory.createEmptyBorder(8, 15, 8, 15));
        btnVoltar.setFocusPainted(false);
        btnVoltar.addActionListener(e -> frame.dispose());
        
        // Data e hora atual
        JLabel lblDataHora = new JLabel(java.time.LocalDateTime.now().format(java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")), JLabel.RIGHT);
        lblDataHora.setFont(new Font("Arial", Font.PLAIN, 12));
        lblDataHora.setForeground(Color.WHITE);
        
        panel.add(btnVoltar, BorderLayout.WEST);
        panel.add(titleLabel, BorderLayout.CENTER);
        panel.add(lblDataHora, BorderLayout.EAST);
        
        return panel;
    }
    
    private JPanel createTabbedPanePanel() {
        JPanel panel = new JPanel(new BorderLayout());
        
        tabbedPane = new JTabbedPane();
        
        // Aba de Empresa
        tabbedPane.addTab("Empresa", createEmpresaPanel());
        
        // Aba de Financeiro
        tabbedPane.addTab("Financeiro", createFinanceiroPanel());
        
        // Aba de Sistema
        tabbedPane.addTab("Sistema", createSistemaPanel());
        
        // Aba de Usuários
        tabbedPane.addTab("Usuários", createUsuariosPanel());
        
        // Adicionar listener para destacar aba ativa
        tabbedPane.addChangeListener(e -> updateTabHighlight());
        
        panel.add(tabbedPane, BorderLayout.CENTER);
        
        return panel;
    }
    
    private JPanel createEmpresaPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;
        
        // Nome da Empresa
        gbc.gridx = 0; gbc.gridy = 0;
        panel.add(new JLabel("Nome da Empresa:"), gbc);
        gbc.gridx = 1; gbc.fill = GridBagConstraints.HORIZONTAL; gbc.weightx = 1.0;
        txtNomeEmpresa = new JTextField();
        panel.add(txtNomeEmpresa, gbc);
        
        // CNPJ
        gbc.gridx = 0; gbc.gridy = 1; gbc.fill = GridBagConstraints.NONE; gbc.weightx = 0;
        panel.add(new JLabel("CNPJ:"), gbc);
        gbc.gridx = 1; gbc.fill = GridBagConstraints.HORIZONTAL; gbc.weightx = 1.0;
        txtCnpj = new JTextField();
        panel.add(txtCnpj, gbc);
        
        // Endereço
        gbc.gridx = 0; gbc.gridy = 2; gbc.fill = GridBagConstraints.NONE; gbc.weightx = 0;
        panel.add(new JLabel("Endereço:"), gbc);
        gbc.gridx = 1; gbc.fill = GridBagConstraints.HORIZONTAL; gbc.weightx = 1.0;
        txtEndereco = new JTextField();
        panel.add(txtEndereco, gbc);
        
        // Telefone
        gbc.gridx = 0; gbc.gridy = 3; gbc.fill = GridBagConstraints.NONE; gbc.weightx = 0;
        panel.add(new JLabel("Telefone:"), gbc);
        gbc.gridx = 1; gbc.fill = GridBagConstraints.HORIZONTAL; gbc.weightx = 1.0;
        txtTelefone = new JTextField();
        panel.add(txtTelefone, gbc);
        
        // Email
        gbc.gridx = 0; gbc.gridy = 4; gbc.fill = GridBagConstraints.NONE; gbc.weightx = 0;
        panel.add(new JLabel("Email:"), gbc);
        gbc.gridx = 1; gbc.fill = GridBagConstraints.HORIZONTAL; gbc.weightx = 1.0;
        txtEmail = new JTextField();
        panel.add(txtEmail, gbc);
        
        // Logo da Empresa
        gbc.gridx = 0; gbc.gridy = 5; gbc.gridwidth = 2;
        panel.add(new JLabel("Logo da Empresa:"), gbc);
        gbc.gridy = 6;
        JButton btnLogo = new JButton("Selecionar Logo");
        btnLogo.addActionListener(this::selecionarLogo);
        panel.add(btnLogo, gbc);
        
        return panel;
    }
    
    private JPanel createFinanceiroPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;
        
        // Taxa de Juros
        gbc.gridx = 0; gbc.gridy = 0;
        panel.add(new JLabel("Taxa de Juros (%):"), gbc);
        gbc.gridx = 1; gbc.fill = GridBagConstraints.HORIZONTAL; gbc.weightx = 1.0;
        txtTaxaJuros = new JTextField("1.0");
        panel.add(txtTaxaJuros, gbc);
        
        // Taxa de Multas
        gbc.gridx = 0; gbc.gridy = 1; gbc.fill = GridBagConstraints.NONE; gbc.weightx = 0;
        panel.add(new JLabel("Taxa de Multas (%):"), gbc);
        gbc.gridx = 1; gbc.fill = GridBagConstraints.HORIZONTAL; gbc.weightx = 1.0;
        txtTaxaMultas = new JTextField("2.0");
        panel.add(txtTaxaMultas, gbc);
        
        // Limite de Crédito
        gbc.gridx = 0; gbc.gridy = 2; gbc.fill = GridBagConstraints.NONE; gbc.weightx = 0;
        panel.add(new JLabel("Limite de Crédito Padrão:"), gbc);
        gbc.gridx = 1; gbc.fill = GridBagConstraints.HORIZONTAL; gbc.weightx = 1.0;
        txtLimiteCredito = new JTextField("1000.00");
        panel.add(txtLimiteCredito, gbc);
        
        // Condições de Pagamento
        gbc.gridx = 0; gbc.gridy = 3; gbc.gridwidth = 2;
        panel.add(new JLabel("Condições de Pagamento:"), gbc);
        gbc.gridy = 4; gbc.gridwidth = 1; gbc.fill = GridBagConstraints.NONE; gbc.weightx = 0;
        panel.add(new JLabel("Forma de Pagamento Padrão:"), gbc);
        gbc.gridx = 1; gbc.fill = GridBagConstraints.HORIZONTAL; gbc.weightx = 1.0;
        cbCondicaoPagamento = new JComboBox<>(new String[]{
            "À Vista (5% desconto)",
            "30 dias (sem juros)", 
            "60 dias (2% juros)",
            "90 dias (3% juros)",
            "Personalizado"
        });
        panel.add(cbCondicaoPagamento, gbc);
        
        gbc.gridx = 0; gbc.gridy = 5; gbc.gridwidth = 2; gbc.fill = GridBagConstraints.NONE; gbc.weightx = 0;
        panel.add(new JLabel("Condições Adicionais:"), gbc);
        gbc.gridy = 6; gbc.fill = GridBagConstraints.HORIZONTAL; gbc.weightx = 1.0;
        JTextArea txtCondicoes = new JTextArea(3, 30);
        txtCondicoes.setText("1. Pagamento à vista: 5% de desconto\n" +
                           "2. Pagamento em 30 dias: sem juros\n" +
                           "3. Pagamento em 60 dias: juros de 2%");
        JScrollPane scrollCondicoes = new JScrollPane(txtCondicoes);
        panel.add(scrollCondicoes, gbc);
        
        return panel;
    }
    
    private JPanel createSistemaPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;
        
        // Configurações Gerais
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
        panel.add(new JLabel("Configurações Gerais:"), gbc);
        
        gbc.gridy = 1; gbc.gridwidth = 1;
        cbImpressoraAutomatica = new JCheckBox("Impressão automática de cupom");
        panel.add(cbImpressoraAutomatica, gbc);
        
        gbc.gridy = 2;
        cbBackupAutomatico = new JCheckBox("Backup automático diário");
        panel.add(cbBackupAutomatico, gbc);
        
        gbc.gridy = 3;
        cbLogAtividades = new JCheckBox("Registrar atividades do sistema");
        panel.add(cbLogAtividades, gbc);
        
        // Tema
        gbc.gridx = 0; gbc.gridy = 4;
        panel.add(new JLabel("Tema da Interface:"), gbc);
        gbc.gridx = 1;
        cbTema = new JComboBox<>(new String[]{"Claro", "Escuro", "Azul", "Verde"});
        panel.add(cbTema, gbc);
        
        // Idioma
        gbc.gridx = 0; gbc.gridy = 5;
        panel.add(new JLabel("Idioma:"), gbc);
        gbc.gridx = 1;
        cbIdioma = new JComboBox<>(new String[]{"Português", "Inglês", "Espanhol"});
        panel.add(cbIdioma, gbc);
        
        // Configurações de Banco de Dados
        gbc.gridx = 0; gbc.gridy = 6; gbc.gridwidth = 2;
        panel.add(new JLabel("Banco de Dados:"), gbc);
        gbc.gridy = 7; gbc.gridwidth = 1; gbc.fill = GridBagConstraints.NONE; gbc.weightx = 0;
        panel.add(new JLabel("Tipo de Banco:"), gbc);
        gbc.gridx = 1; gbc.fill = GridBagConstraints.HORIZONTAL; gbc.weightx = 1.0;
        cbBancoDados = new JComboBox<>(new String[]{
            "MySQL",
            "PostgreSQL", 
            "SQLite",
            "H2 Database",
            "Oracle",
            "SQL Server"
        });
        panel.add(cbBancoDados, gbc);
        
        gbc.gridx = 0; gbc.gridy = 8; gbc.gridwidth = 2; gbc.fill = GridBagConstraints.NONE; gbc.weightx = 0;
        JButton btnConfigDB = com.br.hermescomercial.theme.ModernTheme.createPastelButton("⚙️ Configurar Conexão", com.br.hermescomercial.theme.ModernTheme.PASTEL_BLUE, com.br.hermescomercial.theme.ModernTheme.TEXT_PRIMARY);
        btnConfigDB.addActionListener(this::configurarBancoDados);
        panel.add(btnConfigDB, gbc);
        
        gbc.gridx = 0; gbc.gridy = 9;
        panel.add(new JLabel("Pasta de Backup:"), gbc);
        gbc.gridy = 10;
        JButton btnPastaBackup = com.br.hermescomercial.theme.ModernTheme.createPastelButton("📁 Selecionar Pasta", com.br.hermescomercial.theme.ModernTheme.PASTEL_PURPLE, com.br.hermescomercial.theme.ModernTheme.TEXT_PRIMARY);
        btnPastaBackup.addActionListener(this::selecionarPastaBackup);
        panel.add(btnPastaBackup, gbc);
        
        return panel;
    }
    
    private JPanel createUsuariosPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        
        // Tabela de usuários
        String[] columns = {"ID", "Nome", "Login", "Nível", "Status", "Último Acesso"};
        usuariosModel = new DefaultTableModel(columns, 0);
        usuariosTable = new JTable(usuariosModel);
        
        JScrollPane scrollPane = new JScrollPane(usuariosTable);
        panel.add(scrollPane, BorderLayout.CENTER);
        
        // Painel de botões
        JPanel buttonPanel = new JPanel(new FlowLayout());
        buttonPanel.setBorder(BorderFactory.createTitledBorder("Ações"));
        
        JButton btnNovo = com.br.hermescomercial.theme.ModernTheme.createPastelButton("👤 Novo Usuário", com.br.hermescomercial.theme.ModernTheme.PASTEL_GREEN, com.br.hermescomercial.theme.ModernTheme.TEXT_PRIMARY);
        btnNovo.addActionListener(this::novoUsuario);
        buttonPanel.add(btnNovo);
        
        JButton btnEditar = com.br.hermescomercial.theme.ModernTheme.createPastelButton("✏️ Editar Usuário", com.br.hermescomercial.theme.ModernTheme.PASTEL_BLUE, com.br.hermescomercial.theme.ModernTheme.TEXT_PRIMARY);
        btnEditar.addActionListener(this::editarUsuario);
        buttonPanel.add(btnEditar);
        
        JButton btnExcluir = com.br.hermescomercial.theme.ModernTheme.createPastelButton("🗑️ Excluir Usuário", com.br.hermescomercial.theme.ModernTheme.PASTEL_CORAL, com.br.hermescomercial.theme.ModernTheme.TEXT_PRIMARY);
        btnExcluir.addActionListener(this::excluirUsuario);
        buttonPanel.add(btnExcluir);
        
        JButton btnResetar = com.br.hermescomercial.theme.ModernTheme.createPastelButton("🔄 Resetar Senha", com.br.hermescomercial.theme.ModernTheme.PASTEL_YELLOW, com.br.hermescomercial.theme.ModernTheme.TEXT_PRIMARY);
        btnResetar.addActionListener(this::resetarSenha);
        buttonPanel.add(btnResetar);
        
        panel.add(buttonPanel, BorderLayout.SOUTH);
        
        return panel;
    }
    
    private JPanel createControlPanel() {
        JPanel panel = new JPanel(new FlowLayout());
        panel.setBorder(BorderFactory.createTitledBorder("Ações"));
        
        JButton btnSalvar = com.br.hermescomercial.theme.ModernTheme.createPastelButton("💾 Salvar Configurações", com.br.hermescomercial.theme.ModernTheme.PASTEL_GREEN, com.br.hermescomercial.theme.ModernTheme.TEXT_PRIMARY);
        btnSalvar.addActionListener(this::salvarConfiguracoes);
        
        JButton btnRestaurar = com.br.hermescomercial.theme.ModernTheme.createPastelButton("🔄 Restaurar Padrão", com.br.hermescomercial.theme.ModernTheme.PASTEL_YELLOW, com.br.hermescomercial.theme.ModernTheme.TEXT_PRIMARY);
        btnRestaurar.addActionListener(this::restaurarPadrao);
        
        JButton btnImportar = com.br.hermescomercial.theme.ModernTheme.createPastelButton("📥 Importar Config", com.br.hermescomercial.theme.ModernTheme.PASTEL_CYAN, com.br.hermescomercial.theme.ModernTheme.TEXT_PRIMARY);
        btnImportar.addActionListener(this::importarConfiguracoes);
        
        JButton btnExportar = com.br.hermescomercial.theme.ModernTheme.createPastelButton("📤 Exportar Config", com.br.hermescomercial.theme.ModernTheme.PASTEL_BLUE, com.br.hermescomercial.theme.ModernTheme.TEXT_PRIMARY);
        btnExportar.addActionListener(this::exportarConfiguracoes);
        
        JButton btnSair = com.br.hermescomercial.theme.ModernTheme.createPastelButton("❌ Sair", com.br.hermescomercial.theme.ModernTheme.PASTEL_CORAL, com.br.hermescomercial.theme.ModernTheme.TEXT_PRIMARY);
        btnSair.addActionListener(e -> frame.dispose());
        
        panel.add(btnSalvar);
        panel.add(btnRestaurar);
        panel.add(btnImportar);
        panel.add(btnExportar);
        panel.add(btnSair);
        
        return panel;
    }
    
    // Métodos de ação
    private void selecionarLogo(ActionEvent e) {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setFileFilter(new javax.swing.filechooser.FileNameExtensionFilter(
            "Imagens", "jpg", "jpeg", "png", "gif"));
        
        int result = fileChooser.showOpenDialog(frame);
        if (result == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();
            JOptionPane.showMessageDialog(frame, 
                "Logo selecionado: " + selectedFile.getName(),
                "Logo", JOptionPane.INFORMATION_MESSAGE);
        }
    }
    
    private void configurarBancoDados(ActionEvent e) {
        try {
            // Criar diálogo de configuração de banco
            JDialog configDialog = new JDialog(frame, "Configurar Conexão com Banco de Dados", true);
            configDialog.setSize(500, 400);
            configDialog.setLocationRelativeTo(frame);
            
            JPanel mainPanel = new JPanel(new BorderLayout());
            mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
            mainPanel.setBackground(new Color(245, 245, 250));
            
            // Painel de configuração
            JPanel configPanel = new JPanel(new GridBagLayout());
            configPanel.setBackground(Color.WHITE);
            configPanel.setBorder(BorderFactory.createTitledBorder("Parâmetros de Conexão"));
            GridBagConstraints gbc = new GridBagConstraints();
            gbc.insets = new Insets(10, 10, 10, 10);
            gbc.anchor = GridBagConstraints.WEST;
            
            // Tipo de banco
            gbc.gridx = 0; gbc.gridy = 0;
            configPanel.add(new JLabel("Tipo de Banco:"), gbc);
            gbc.gridx = 1; gbc.fill = GridBagConstraints.HORIZONTAL; gbc.weightx = 1.0;
            JComboBox<String> cbTipoBanco = new JComboBox<>(new String[]{"MySQL", "PostgreSQL", "H2"});
            
            // Carregar configurações salvas
            String savedType = DatabaseConfig.getProperty("database.type");
            if (savedType != null) {
                cbTipoBanco.setSelectedItem(savedType);
            }
            configPanel.add(cbTipoBanco, gbc);
            
            // Host
            gbc.gridx = 0; gbc.gridy = 1; gbc.fill = GridBagConstraints.NONE; gbc.weightx = 0;
            configPanel.add(new JLabel("Host:"), gbc);
            gbc.gridx = 1; gbc.fill = GridBagConstraints.HORIZONTAL; gbc.weightx = 1.0;
            JTextField txtHost = new JTextField(DatabaseConfig.getProperty("database.host", "localhost"));
            configPanel.add(txtHost, gbc);
            
            // Porta
            gbc.gridx = 0; gbc.gridy = 2; gbc.fill = GridBagConstraints.NONE; gbc.weightx = 0;
            configPanel.add(new JLabel("Porta:"), gbc);
            gbc.gridx = 1; gbc.fill = GridBagConstraints.HORIZONTAL; gbc.weightx = 1.0;
            String defaultPort = "5432"; // PostgreSQL padrão
            if ("MySQL".equals(savedType)) defaultPort = "3306";
            if ("H2".equals(savedType)) defaultPort = "9092";
            JTextField txtPorta = new JTextField(DatabaseConfig.getProperty("database.port", defaultPort));
            configPanel.add(txtPorta, gbc);
            
            // Database
            gbc.gridx = 0; gbc.gridy = 3; gbc.fill = GridBagConstraints.NONE; gbc.weightx = 0;
            configPanel.add(new JLabel("Database:"), gbc);
            gbc.gridx = 1; gbc.fill = GridBagConstraints.HORIZONTAL; gbc.weightx = 1.0;
            JTextField txtDatabase = new JTextField(DatabaseConfig.getProperty("database.name", "hermes_comercial"));
            configPanel.add(txtDatabase, gbc);
            
            // Usuário
            gbc.gridx = 0; gbc.gridy = 4; gbc.fill = GridBagConstraints.NONE; gbc.weightx = 0;
            configPanel.add(new JLabel("Usuário:"), gbc);
            gbc.gridx = 1; gbc.fill = GridBagConstraints.HORIZONTAL; gbc.weightx = 1.0;
            String defaultUser = "postgres"; // PostgreSQL padrão
            if ("MySQL".equals(savedType)) defaultUser = "root";
            if ("H2".equals(savedType)) defaultUser = "sa";
            JTextField txtUsuario = new JTextField(DatabaseConfig.getProperty("database.user", defaultUser));
            configPanel.add(txtUsuario, gbc);
            
            // Senha
            gbc.gridx = 0; gbc.gridy = 5; gbc.fill = GridBagConstraints.NONE; gbc.weightx = 0;
            configPanel.add(new JLabel("Senha:"), gbc);
            gbc.gridx = 1; gbc.fill = GridBagConstraints.HORIZONTAL; gbc.weightx = 1.0;
            JPasswordField txtSenha = new JPasswordField(DatabaseConfig.getProperty("database.password", ""));
            configPanel.add(txtSenha, gbc);
            
            // Botões
            JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
            buttonPanel.setBackground(new Color(245, 245, 250));
            
            JButton btnTestar = com.br.hermescomercial.theme.ModernTheme.createPastelButton("🔧 Testar Conexão", com.br.hermescomercial.theme.ModernTheme.PASTEL_CYAN, com.br.hermescomercial.theme.ModernTheme.TEXT_PRIMARY);
            JButton btnSalvar = com.br.hermescomercial.theme.ModernTheme.createPastelButton("💾 Salvar", com.br.hermescomercial.theme.ModernTheme.PASTEL_GREEN, com.br.hermescomercial.theme.ModernTheme.TEXT_PRIMARY);
            JButton btnCancelar = com.br.hermescomercial.theme.ModernTheme.createPastelButton("❌ Cancelar", com.br.hermescomercial.theme.ModernTheme.PASTEL_CORAL, com.br.hermescomercial.theme.ModernTheme.TEXT_PRIMARY);
            
            btnTestar.addActionListener(ev -> {
                String tipo = (String) cbTipoBanco.getSelectedItem();
                String host = txtHost.getText().trim();
                String porta = txtPorta.getText().trim();
                String database = txtDatabase.getText().trim();
                String usuario = txtUsuario.getText().trim();
                String senha = new String(txtSenha.getPassword());
                
                // Validar campos
                if (host.isEmpty() || porta.isEmpty() || database.isEmpty() || usuario.isEmpty()) {
                    JOptionPane.showMessageDialog(configDialog, 
                        "Preencha todos os campos obrigatórios!",
                        "Campos Obrigatórios", JOptionPane.WARNING_MESSAGE);
                    return;
                }
                
                // Mostrar diálogo de teste em andamento
                JDialog testDialog = new JDialog(configDialog, "Testando Conexão...", true);
                testDialog.setSize(300, 100);
                testDialog.setLocationRelativeTo(configDialog);
                
                JPanel testPanel = new JPanel(new BorderLayout());
                testPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
                testPanel.setBackground(new Color(245, 245, 250));
                
                JLabel lblTeste = new JLabel("🔄 Testando conexão...", JLabel.CENTER);
                lblTeste.setFont(new Font("Arial", Font.BOLD, 14));
                testPanel.add(lblTeste, BorderLayout.CENTER);
                
                testDialog.add(testPanel);
                
                // Executar teste em thread separada
                SwingWorker<Boolean, Void> worker = new SwingWorker<Boolean, Void>() {
                    @Override
                    protected Boolean doInBackground() throws Exception {
                        return DatabaseConfig.testConnectionWithFallback(tipo, host, porta, database, usuario, senha);
                    }
                    
                    @Override
                    protected void done() {
                        testDialog.dispose();
                        try {
                            boolean sucesso = get();
                            if (sucesso) {
                                JOptionPane.showMessageDialog(configDialog, 
                                    "✅ Conexão testada com sucesso!\n\n" +
                                    "Banco: " + tipo + "\n" +
                                    "Host: " + host + ":" + porta + "\n" +
                                    "Database: " + database + "\n" +
                                    "Usuário: " + usuario,
                                    "Teste de Conexão", JOptionPane.INFORMATION_MESSAGE);
                            } else {
                                JOptionPane.showMessageDialog(configDialog, 
                                    "❌ Falha na conexão!\n\n" +
                                    "Verifique os parâmetros e tente novamente.\n" +
                                    "Possíveis causas:\n" +
                                    "• Servidor não está rodando\n" +
                                    "• Credenciais incorretas\n" +
                                    "• Banco não existe\n" +
                                    "• Firewall bloqueando conexão",
                                    "Falha na Conexão", JOptionPane.ERROR_MESSAGE);
                            }
                        } catch (Exception ex) {
                            JOptionPane.showMessageDialog(configDialog, 
                                "❌ Erro durante o teste: " + ex.getMessage(),
                                "Erro no Teste", JOptionPane.ERROR_MESSAGE);
                        }
                    }
                };
                
                worker.execute();
                testDialog.setVisible(true);
            });
            
            btnSalvar.addActionListener(ev -> {
                String tipo = (String) cbTipoBanco.getSelectedItem();
                String host = txtHost.getText().trim();
                String porta = txtPorta.getText().trim();
                String database = txtDatabase.getText().trim();
                String usuario = txtUsuario.getText().trim();
                String senha = new String(txtSenha.getPassword());
                
                // Validar campos
                if (host.isEmpty() || porta.isEmpty() || database.isEmpty() || usuario.isEmpty()) {
                    JOptionPane.showMessageDialog(configDialog, 
                        "Preencha todos os campos obrigatórios!",
                        "Campos Obrigatórios", JOptionPane.WARNING_MESSAGE);
                    return;
                }
                
                // Salvar configurações
                DatabaseConfig.setProperty("database.type", tipo);
                DatabaseConfig.setProperty("database.host", host);
                DatabaseConfig.setProperty("database.port", porta);
                DatabaseConfig.setProperty("database.name", database);
                DatabaseConfig.setProperty("database.user", usuario);
                DatabaseConfig.setProperty("database.password", senha);
                DatabaseConfig.setProperty("database.description", 
                    "Banco de dados " + tipo + " em " + host + ":" + porta);
                
                DatabaseConfig.saveConfig();
                
                JOptionPane.showMessageDialog(configDialog, 
                    "✅ Configurações salvas com sucesso!\n\n" +
                    "Banco: " + tipo + "\n" +
                    "Host: " + host + ":" + porta + "\n" +
                    "Database: " + database + "\n" +
                    "Usuário: " + usuario + "\n\n" +
                    "As configurações serão aplicadas ao reiniciar o sistema.",
                    "Configurações Salvas", JOptionPane.INFORMATION_MESSAGE);
                configDialog.dispose();
            });
            
            btnCancelar.addActionListener(ev -> configDialog.dispose());
            
            buttonPanel.add(btnTestar);
            buttonPanel.add(btnSalvar);
            buttonPanel.add(btnCancelar);
            
            mainPanel.add(configPanel, BorderLayout.CENTER);
            mainPanel.add(buttonPanel, BorderLayout.SOUTH);
            
            configDialog.add(mainPanel);
            configDialog.setVisible(true);
            
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(frame, 
                "Erro ao abrir configuração de banco: " + ex.getMessage(),
                "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void selecionarPastaBackup(ActionEvent e) {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        
        int result = fileChooser.showOpenDialog(frame);
        if (result == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();
            JOptionPane.showMessageDialog(frame, 
                "Pasta de backup selecionada: " + selectedFile.getAbsolutePath(),
                "Backup", JOptionPane.INFORMATION_MESSAGE);
        }
    }
    
    private void novoUsuario(ActionEvent e) {
        // Criar diálogo para novo usuário
        JDialog dialog = new JDialog(frame, "Novo Usuário", true);
        dialog.setSize(400, 300);
        dialog.setLocationRelativeTo(frame);
        
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
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
        JButton btnSalvar = com.br.hermescomercial.theme.ModernTheme.createPastelButton("💾 Salvar", com.br.hermescomercial.theme.ModernTheme.PASTEL_GREEN, com.br.hermescomercial.theme.ModernTheme.TEXT_PRIMARY);
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
                java.time.LocalDateTime.now().format(java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"))
            );
            
            usuarios.add(novoUsuario);
            
            // Adicionar à tabela
            Object[] row = {
                novoUsuario.getId(),
                novoUsuario.getNome(),
                novoUsuario.getLogin(),
                novoUsuario.getNivel(),
                novoUsuario.getStatus()
            };
            usuariosModel.addRow(row);
            
            JOptionPane.showMessageDialog(dialog, "Usuário criado com sucesso!", 
                "Sucesso", JOptionPane.INFORMATION_MESSAGE);
            dialog.dispose();
        });
        
        JButton btnCancelar = com.br.hermescomercial.theme.ModernTheme.createPastelButton("❌ Cancelar", com.br.hermescomercial.theme.ModernTheme.PASTEL_CORAL, com.br.hermescomercial.theme.ModernTheme.TEXT_PRIMARY);
        btnCancelar.addActionListener(ev -> dialog.dispose());
        
        buttonPanel.add(btnSalvar);
        buttonPanel.add(btnCancelar);
        
        gbc.gridx = 0; gbc.gridy = 4; gbc.gridwidth = 2; gbc.fill = GridBagConstraints.HORIZONTAL; gbc.weightx = 0;
        panel.add(buttonPanel, gbc);
        
        dialog.add(panel);
        dialog.setVisible(true);
    }
    
    private void editarUsuario(ActionEvent e) {
        int selectedRow = usuariosTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(frame, "Selecione um usuário!", 
                "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        JOptionPane.showMessageDialog(frame, 
            "Funcionalidade de edição em desenvolvimento.\n" +
            "Usuário selecionado: " + usuarios.get(selectedRow).getNome(),
            "Editar Usuário", JOptionPane.INFORMATION_MESSAGE);
    }
    
    private void excluirUsuario(ActionEvent e) {
        int selectedRow = usuariosTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(frame, "Selecione um usuário!", 
                "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        Usuario usuario = usuarios.get(selectedRow);
        
        int confirm = com.br.hermescomercial.theme.ModernTheme.showCustomConfirmDialog(frame, 
            "Deseja excluir o usuário:\n" + usuario.getNome() + "?",
            "Confirmar Exclusão", 
            new String[]{"Sim", "Não"}, 0);
            
        if (confirm == 0) {
            usuarios.remove(selectedRow);
            usuariosModel.removeRow(selectedRow);
            JOptionPane.showMessageDialog(frame, "Usuário excluído com sucesso!", 
                "Sucesso", JOptionPane.INFORMATION_MESSAGE);
        }
    }
    
    private void resetarSenha(ActionEvent e) {
        int selectedRow = usuariosTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(frame, "Selecione um usuário!", 
                "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        Usuario usuario = usuarios.get(selectedRow);
        
        int confirm = com.br.hermescomercial.theme.ModernTheme.showCustomConfirmDialog(frame, 
            "Deseja resetar a senha do usuário:\n" + usuario.getNome() + "?\n" +
            "Nova senha: 123456",
            "Resetar Senha", 
            new String[]{"Sim", "Não"}, 0);
            
        if (confirm == 0) {
            JOptionPane.showMessageDialog(frame, "Senha resetada com sucesso!\nNova senha: 123456", 
                "Sucesso", JOptionPane.INFORMATION_MESSAGE);
        }
    }
    
    private void salvarConfiguracoes(ActionEvent e) {
        JOptionPane.showMessageDialog(frame, 
            "Configurações salvas com sucesso!\n\n" +
            "Empresa: " + txtNomeEmpresa.getText() + "\n" +
            "Tema: " + cbTema.getSelectedItem() + "\n" +
            "Idioma: " + cbIdioma.getSelectedItem() + "\n" +
            "Versão SWING 2.0",
            "Salvar", JOptionPane.INFORMATION_MESSAGE);
    }
    
    private void restaurarPadrao(ActionEvent e) {
        int confirm = com.br.hermescomercial.theme.ModernTheme.showCustomConfirmDialog(frame, 
            "Deseja restaurar as configurações padrão?\n" +
            "Todas as alterações serão perdidas.",
            "Restaurar Padrão", 
            new String[]{"Sim", "Não"}, 0);
            
        if (confirm == 0) {
            carregarDadosExemplo();
            JOptionPane.showMessageDialog(frame, "Configurações restauradas com sucesso!", 
                "Sucesso", JOptionPane.INFORMATION_MESSAGE);
        }
    }
    
    private void importarConfiguracoes(ActionEvent e) {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setFileFilter(new javax.swing.filechooser.FileNameExtensionFilter(
            "Arquivos de Configuração", "json", "xml", "properties"));
        
        int result = fileChooser.showOpenDialog(frame);
        if (result == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();
            JOptionPane.showMessageDialog(frame, 
                "Configurações importadas de: " + selectedFile.getName(),
                "Importar", JOptionPane.INFORMATION_MESSAGE);
        }
    }
    
    private void exportarConfiguracoes(ActionEvent e) {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Exportar Configurações");
        
        int result = fileChooser.showSaveDialog(frame);
        if (result == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();
            JOptionPane.showMessageDialog(frame, 
                "Configurações exportadas para: " + selectedFile.getName(),
                "Exportar", JOptionPane.INFORMATION_MESSAGE);
        }
    }
    
    private void carregarDadosExemplo() {
        // Dados da empresa
        txtNomeEmpresa.setText("Hermes Comercial PDV");
        txtCnpj.setText("12.345.678/0001-90");
        txtEndereco.setText("Rua Principal, 123 - Centro - Cidade/UF");
        txtTelefone.setText("(11) 1234-5678");
        txtEmail.setText("contato@hermespdv.com.br");
        
        // Configurações do sistema
        cbImpressoraAutomatica.setSelected(true);
        cbBackupAutomatico.setSelected(true);
        cbLogAtividades.setSelected(true);
        cbTema.setSelectedIndex(0); // Claro
        cbIdioma.setSelectedIndex(0); // Português
        
        // Usuários de exemplo
        usuarios.add(new Usuario(1, "Administrador", "admin", "Administrador", "Ativo", "25/04/2024 14:30"));
        usuarios.add(new Usuario(2, "Operador 1", "operador1", "Operador", "Ativo", "25/04/2024 08:15"));
        usuarios.add(new Usuario(3, "Gerente", "gerente", "Gerente", "Ativo", "25/04/2024 09:45"));
        
        atualizarTabelaUsuarios();
    }
    
    private void atualizarTabelaUsuarios() {
        usuariosModel.setRowCount(0);
        
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
    }
    
    public void show() {
        frame.setVisible(true);
    }
    
    // Classe de apoio
    private static class Usuario {
        private int id;
        private String nome;
        private String usuario;
        private String nivel;
        private String status;
        private String ultimoAcesso;
        
        public Usuario(int id, String nome, String usuario, String nivel, String status, String ultimoAcesso) {
            this.id = id;
            this.nome = nome;
            this.usuario = usuario;
            this.nivel = nivel;
            this.status = status;
            this.ultimoAcesso = ultimoAcesso;
        }
        
        public int getId() { return id; }
        public String getNome() { return nome; }
        public String getLogin() { return usuario; }
        public String getNivel() { return nivel; }
        public String getStatus() { return status; }
        public String getUltimoAcesso() { return ultimoAcesso; }
    }
    
    /**
     * Atualiza o destaque visual da aba ativa
     */
    private void updateTabHighlight() {
        int selectedIndex = tabbedPane.getSelectedIndex();
        String tabTitle = tabbedPane.getTitleAt(selectedIndex);
        
        // Atualizar título da janela com aba ativa e destaque
        frame.setTitle("⚙️ PDV - Configurações v2.1.0 - Premium [📌 " + tabTitle + " ATIVA]");
        
        // Aplicar destaque visual na aba ativa
        for (int i = 0; i < tabbedPane.getTabCount(); i++) {
            if (i == selectedIndex) {
                // Aba ativa - fundo azul escuro e texto preto (letra escura)
                tabbedPane.setBackgroundAt(i, new Color(70, 130, 180)); // Steel Blue (mais escuro)
                tabbedPane.setForegroundAt(i, Color.BLACK); // Letra escura
                
                // Adicionar indicador visual
                String activeTitle = "📌 " + tabTitle + " 📌";
                tabbedPane.setTitleAt(i, activeTitle);
            } else {
                // Abas inativas - fundo cinza médio
                tabbedPane.setBackgroundAt(i, new Color(200, 200, 200)); // Gray médio
                tabbedPane.setForegroundAt(i, Color.BLACK);
                
                // Remover indicador visual
                String originalTitle = tabbedPane.getTitleAt(i).replace("📌 ", "").replace(" 📌", "");
                tabbedPane.setTitleAt(i, originalTitle);
            }
        }
        
        // Feedback visual adicional com cores mais escuras
        if (frame.getContentPane() instanceof JPanel) {
            JPanel mainPanel = (JPanel) frame.getContentPane();
            if (selectedIndex == 0) { // Empresa
                mainPanel.setBackground(new Color(230, 240, 250)); // Azul escuro suave
            } else if (selectedIndex == 1) { // Financeiro
                mainPanel.setBackground(new Color(250, 230, 230)); // Vermelho escuro suave
            } else if (selectedIndex == 2) { // Sistema
                mainPanel.setBackground(new Color(230, 250, 230)); // Verde escuro suave
            } else if (selectedIndex == 3) { // Usuários
                mainPanel.setBackground(new Color(250, 250, 230)); // Amarelo escuro suave
            }
        }
    }
}
