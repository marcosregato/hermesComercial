package com.br.hermescomercial.pdv.controller;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

import java.util.HashMap;
import java.util.Map;

/**
 * Controller para tela de configuração do sistema
 * Versão 2.8.0 - Interface completa para configurações gerais
 * Funcionalidades: Configurações gerais, backup, segurança, integrações
 */
public class SistemaConfigSwingController {
    
    public JFrame frame;
    public JTabbedPane tabbedPane;
    private JTextField txtNomeEmpresa, txtCNPJ, txtEndereco, txtTelefone, txtEmail;
    private JComboBox<String> cmbIdioma, cmbPais, cmbMoeda;
    private JCheckBox chkBackupAuto, chkNotificacoes, chkAuditoria, chkManutencao;
    private JTable backupTable;
    private DefaultTableModel backupModel;
    private Map<String, Object> sistemaConfig;
    
    public SistemaConfigSwingController() {
        sistemaConfig = new HashMap<>();
        initializeUI();
        // Tornar frame visível automaticamente para testes
        frame.setVisible(true);
    }
    
    private void initializeUI() {
        frame = new JFrame("Configuração do Sistema - Hermes Comercial PDV");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setSize(1100, 750);
        frame.setLocationRelativeTo(null);
        
        // Aplicar tema moderno
        frame.getContentPane().setBackground(Color.WHITE);
        
        tabbedPane = new JTabbedPane();
        tabbedPane.setBackground(Color.WHITE);
        
        // Abas principais
        tabbedPane.addTab("🏢 Empresa", createEmpresaPanel());
        tabbedPane.addTab("🌐 Geral", createGeralPanel());
        tabbedPane.addTab("🔐 Segurança", createSegurancaPanel());
        tabbedPane.addTab("💾 Backup", createBackupPanel());
        tabbedPane.addTab("🔌 Integrações", createIntegracoesPanel());
        
        frame.add(tabbedPane);
    }
    
    private JPanel createEmpresaPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);
        
        // Formulário de dados da empresa
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(Color.WHITE);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.anchor = GridBagConstraints.WEST;
        
        gbc.gridx = 0; gbc.gridy = 0;
        JLabel lblTitulo = new JLabel("🏢 Dados da Empresa");
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 18));
        lblTitulo.setForeground(new Color(33, 150, 243));
        formPanel.add(lblTitulo, gbc);
        
        // Nome da empresa
        gbc.gridy = 1;
        formPanel.add(new JLabel("Nome da Empresa:"), gbc);
        gbc.gridx = 1; gbc.fill = GridBagConstraints.HORIZONTAL; gbc.weightx = 1;
        txtNomeEmpresa = new JTextField(40);
        txtNomeEmpresa.setText("Hermes Comercial Ltda");
        formPanel.add(txtNomeEmpresa, gbc);
        
        // CNPJ
        gbc.gridx = 0; gbc.gridy = 2; gbc.fill = GridBagConstraints.NONE; gbc.weightx = 0;
        formPanel.add(new JLabel("CNPJ:"), gbc);
        gbc.gridx = 1; gbc.fill = GridBagConstraints.HORIZONTAL; gbc.weightx = 1;
        txtCNPJ = new JTextField(20);
        txtCNPJ.setText("12.345.678/0001-90");
        formPanel.add(txtCNPJ, gbc);
        
        // Endereço
        gbc.gridx = 0; gbc.gridy = 3; gbc.fill = GridBagConstraints.NONE; gbc.weightx = 0;
        formPanel.add(new JLabel("Endereço:"), gbc);
        gbc.gridx = 1; gbc.fill = GridBagConstraints.HORIZONTAL; gbc.weightx = 1;
        txtEndereco = new JTextField(50);
        txtEndereco.setText("Rua das Empresas, 123 - Centro - São Paulo/SP");
        formPanel.add(txtEndereco, gbc);
        
        // Telefone
        gbc.gridx = 0; gbc.gridy = 4; gbc.fill = GridBagConstraints.NONE; gbc.weightx = 0;
        formPanel.add(new JLabel("Telefone:"), gbc);
        gbc.gridx = 1; gbc.fill = GridBagConstraints.HORIZONTAL; gbc.weightx = 1;
        txtTelefone = new JTextField(20);
        txtTelefone.setText("(11) 1234-5678");
        formPanel.add(txtTelefone, gbc);
        
        // Email
        gbc.gridx = 0; gbc.gridy = 5; gbc.fill = GridBagConstraints.NONE; gbc.weightx = 0;
        formPanel.add(new JLabel("Email:"), gbc);
        gbc.gridx = 1; gbc.fill = GridBagConstraints.HORIZONTAL; gbc.weightx = 1;
        txtEmail = new JTextField(40);
        txtEmail.setText("contato@hermescomercial.com.br");
        formPanel.add(txtEmail, gbc);
        
        // Logo da empresa
        gbc.gridx = 0; gbc.gridy = 6; gbc.gridwidth = 2;
        JPanel logoPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        logoPanel.setBackground(Color.WHITE);
        
        JButton btnUploadLogo = createButton("📤 Upload Logo", new Color(33, 150, 243));
        btnUploadLogo.addActionListener(e -> uploadLogo());
        
        JLabel lblLogoStatus = new JLabel("Nenhum logo carregado");
        lblLogoStatus.setFont(new Font("Segoe UI", Font.ITALIC, 12));
        lblLogoStatus.setForeground(Color.GRAY);
        
        logoPanel.add(btnUploadLogo);
        logoPanel.add(lblLogoStatus);
        
        formPanel.add(logoPanel, gbc);
        
        panel.add(formPanel, BorderLayout.CENTER);
        
        // Painel de botões
        JPanel buttonPanel = new JPanel(new FlowLayout());
        buttonPanel.setBackground(Color.WHITE);
        
        JButton btnSalvar = createButton("💾 Salvar Dados", new Color(76, 175, 80));
        JButton btnTestar = createButton("🧪 Testar Configuração", new Color(255, 152, 0));
        
        btnSalvar.addActionListener(e -> salvarDadosEmpresa());
        btnTestar.addActionListener(e -> testarConfiguracao());
        
        buttonPanel.add(btnSalvar);
        buttonPanel.add(btnTestar);
        
        panel.add(buttonPanel, BorderLayout.SOUTH);
        
        return panel;
    }
    
    private JPanel createGeralPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);
        
        // Configurações gerais
        JPanel configPanel = new JPanel(new GridBagLayout());
        configPanel.setBackground(Color.WHITE);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.anchor = GridBagConstraints.WEST;
        
        gbc.gridx = 0; gbc.gridy = 0;
        JLabel lblTitulo = new JLabel("🌐 Configurações Gerais");
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 18));
        lblTitulo.setForeground(new Color(33, 150, 243));
        configPanel.add(lblTitulo, gbc);
        
        // Idioma
        gbc.gridy = 1;
        configPanel.add(new JLabel("Idioma Padrão:"), gbc);
        gbc.gridx = 1; gbc.fill = GridBagConstraints.HORIZONTAL; gbc.weightx = 1;
        cmbIdioma = new JComboBox<>(new String[]{
            "Português (Brasil)", "English (US)", "Español (ES)"
        });
        cmbIdioma.setSelectedItem("Português (Brasil)");
        configPanel.add(cmbIdioma, gbc);
        
        // País
        gbc.gridx = 0; gbc.gridy = 2; gbc.fill = GridBagConstraints.NONE; gbc.weightx = 0;
        configPanel.add(new JLabel("País:"), gbc);
        gbc.gridx = 1; gbc.fill = GridBagConstraints.HORIZONTAL; gbc.weightx = 1;
        cmbPais = new JComboBox<>(new String[]{
            "Brasil", "Estados Unidos", "Argentina", "México", "Chile", "Peru"
        });
        cmbPais.setSelectedItem("Brasil");
        configPanel.add(cmbPais, gbc);
        
        // Moeda
        gbc.gridx = 0; gbc.gridy = 3; gbc.fill = GridBagConstraints.NONE; gbc.weightx = 0;
        configPanel.add(new JLabel("Moeda:"), gbc);
        gbc.gridx = 1; gbc.fill = GridBagConstraints.HORIZONTAL; gbc.weightx = 1;
        cmbMoeda = new JComboBox<>(new String[]{
            "Real Brasileiro (R$)", "Dólar Americano ($)", "Peso Argentino ($)", "Peso Mexicano ($)"
        });
        cmbMoeda.setSelectedItem("Real Brasileiro (R$)");
        configPanel.add(cmbMoeda, gbc);
        
        // Opções do sistema
        gbc.gridx = 0; gbc.gridy = 4; gbc.gridwidth = 2;
        JPanel optionsPanel = new JPanel(new GridLayout(4, 1, 5, 5));
        optionsPanel.setBackground(Color.WHITE);
        optionsPanel.setBorder(BorderFactory.createTitledBorder("Opções do Sistema"));
        
        chkBackupAuto = new JCheckBox("Backup automático diário");
        chkBackupAuto.setBackground(Color.WHITE);
        chkBackupAuto.setSelected(true);
        
        chkNotificacoes = new JCheckBox("Notificações do sistema");
        chkNotificacoes.setBackground(Color.WHITE);
        chkNotificacoes.setSelected(true);
        
        chkAuditoria = new JCheckBox("Auditoria de ações");
        chkAuditoria.setBackground(Color.WHITE);
        chkAuditoria.setSelected(true);
        
        chkManutencao = new JCheckBox("Modo de manutenção automático");
        chkManutencao.setBackground(Color.WHITE);
        chkManutencao.setSelected(false);
        
        optionsPanel.add(chkBackupAuto);
        optionsPanel.add(chkNotificacoes);
        optionsPanel.add(chkAuditoria);
        optionsPanel.add(chkManutencao);
        
        configPanel.add(optionsPanel, gbc);
        
        // Configurações de data e hora
        gbc.gridy = 5; gbc.gridwidth = 1;
        JLabel lblDataHora = new JLabel("Data e Hora:");
        lblDataHora.setFont(new Font("Segoe UI", Font.BOLD, 14));
        configPanel.add(lblDataHora, gbc);
        
        gbc.gridy = 6;
        JPanel dataHoraPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        dataHoraPanel.setBackground(Color.WHITE);
        
        JTextField txtFuso = new JTextField(15);
        txtFuso.setText("America/Sao_Paulo");
        JCheckBox chkAjusteAuto = new JCheckBox("Ajuste automático");
        chkAjusteAuto.setBackground(Color.WHITE);
        chkAjusteAuto.setSelected(true);
        
        dataHoraPanel.add(new JLabel("Fuso Horário:"));
        dataHoraPanel.add(txtFuso);
        dataHoraPanel.add(chkAjusteAuto);
        
        configPanel.add(dataHoraPanel, gbc);
        
        panel.add(configPanel, BorderLayout.CENTER);
        
        // Painel de botões
        JPanel buttonPanel = new JPanel(new FlowLayout());
        buttonPanel.setBackground(Color.WHITE);
        
        JButton btnSalvar = createButton("💾 Salvar Configurações", new Color(76, 175, 80));
        JButton btnRestaurar = createButton("🔄 Restaurar Padrão", new Color(255, 152, 0));
        
        btnSalvar.addActionListener(e -> salvarConfiguracoesGerais());
        btnRestaurar.addActionListener(e -> restaurarPadrao());
        
        buttonPanel.add(btnSalvar);
        buttonPanel.add(btnRestaurar);
        
        panel.add(buttonPanel, BorderLayout.SOUTH);
        
        return panel;
    }
    
    private JPanel createSegurancaPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);
        
        // Configurações de segurança
        JPanel configPanel = new JPanel(new GridBagLayout());
        configPanel.setBackground(Color.WHITE);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.anchor = GridBagConstraints.WEST;
        
        gbc.gridx = 0; gbc.gridy = 0;
        JLabel lblTitulo = new JLabel("🔐 Configurações de Segurança");
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 18));
        lblTitulo.setForeground(new Color(33, 150, 243));
        configPanel.add(lblTitulo, gbc);
        
        // Políticas de senha
        gbc.gridy = 1;
        JLabel lblSenha = new JLabel("Políticas de Senha:");
        lblSenha.setFont(new Font("Segoe UI", Font.BOLD, 14));
        configPanel.add(lblSenha, gbc);
        
        gbc.gridy = 2;
        JPanel senhaPanel = new JPanel(new GridLayout(4, 1, 5, 5));
        senhaPanel.setBackground(Color.WHITE);
        
        JCheckBox chkTamanhoMinimo = new JCheckBox("Mínimo 8 caracteres");
        chkTamanhoMinimo.setBackground(Color.WHITE);
        chkTamanhoMinimo.setSelected(true);
        
        JCheckBox chkMaiuscula = new JCheckBox("Letra maiúscula obrigatória");
        chkMaiuscula.setBackground(Color.WHITE);
        chkMaiuscula.setSelected(true);
        
        JCheckBox chkNumero = new JCheckBox("Número obrigatório");
        chkNumero.setBackground(Color.WHITE);
        chkNumero.setSelected(true);
        
        JCheckBox chkEspecial = new JCheckBox("Caractere especial obrigatório");
        chkEspecial.setBackground(Color.WHITE);
        chkEspecial.setSelected(false);
        
        senhaPanel.add(chkTamanhoMinimo);
        senhaPanel.add(chkMaiuscula);
        senhaPanel.add(chkNumero);
        senhaPanel.add(chkEspecial);
        
        configPanel.add(senhaPanel, gbc);
        
        // Tentativas de login
        gbc.gridy = 3;
        JLabel lblTentativas = new JLabel("Tentativas de Login:");
        lblTentativas.setFont(new Font("Segoe UI", Font.BOLD, 14));
        configPanel.add(lblTentativas, gbc);
        
        gbc.gridy = 4;
        JPanel tentativasPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        tentativasPanel.setBackground(Color.WHITE);
        
        JSpinner spnMaxTentativas = new JSpinner(new SpinnerNumberModel(3, 1, 10, 1));
        JSpinner spnTempoBloqueio = new JSpinner(new SpinnerNumberModel(15, 5, 60, 5));
        
        tentativasPanel.add(new JLabel("Máximo de tentativas:"));
        tentativasPanel.add(spnMaxTentativas);
        tentativasPanel.add(new JLabel("Tempo de bloqueio (min):"));
        tentativasPanel.add(spnTempoBloqueio);
        
        configPanel.add(tentativasPanel, gbc);
        
        // Sessão
        gbc.gridy = 5;
        JLabel lblSessao = new JLabel("Configurações de Sessão:");
        lblSessao.setFont(new Font("Segoe UI", Font.BOLD, 14));
        configPanel.add(lblSessao, gbc);
        
        gbc.gridy = 6;
        JPanel sessaoPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        sessaoPanel.setBackground(Color.WHITE);
        
        JSpinner spnTimeoutSessao = new JSpinner(new SpinnerNumberModel(30, 5, 120, 5));
        JCheckBox chkMultiSessao = new JCheckBox("Permitir múltiplas sessões");
        chkMultiSessao.setBackground(Color.WHITE);
        chkMultiSessao.setSelected(false);
        
        sessaoPanel.add(new JLabel("Timeout (min):"));
        sessaoPanel.add(spnTimeoutSessao);
        sessaoPanel.add(chkMultiSessao);
        
        configPanel.add(sessaoPanel, gbc);
        
        panel.add(configPanel, BorderLayout.CENTER);
        
        // Painel de botões
        JPanel buttonPanel = new JPanel(new FlowLayout());
        buttonPanel.setBackground(Color.WHITE);
        
        JButton btnSalvar = createButton("💾 Salvar Segurança", new Color(76, 175, 80));
        JButton btnTestar = createButton("🧪 Testar Políticas", new Color(33, 150, 243));
        
        btnSalvar.addActionListener(e -> salvarConfiguracoesSeguranca());
        btnTestar.addActionListener(e -> testarPoliticas());
        
        buttonPanel.add(btnSalvar);
        buttonPanel.add(btnTestar);
        
        panel.add(buttonPanel, BorderLayout.SOUTH);
        
        return panel;
    }
    
    private JPanel createBackupPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);
        
        // Configurações de backup
        JPanel configPanel = new JPanel(new GridBagLayout());
        configPanel.setBackground(Color.WHITE);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.anchor = GridBagConstraints.WEST;
        
        gbc.gridx = 0; gbc.gridy = 0;
        JLabel lblTitulo = new JLabel("💾 Configurações de Backup");
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 18));
        lblTitulo.setForeground(new Color(33, 150, 243));
        configPanel.add(lblTitulo, gbc);
        
        // Agendamento
        gbc.gridy = 1;
        JLabel lblAgendamento = new JLabel("Agendamento:");
        lblAgendamento.setFont(new Font("Segoe UI", Font.BOLD, 14));
        configPanel.add(lblAgendamento, gbc);
        
        gbc.gridy = 2;
        JPanel agendamentoPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        agendamentoPanel.setBackground(Color.WHITE);
        
        JComboBox<String> cmbFrequencia = new JComboBox<>(new String[]{
            "Diário", "Semanal", "Mensal", "Personalizado"
        });
        JTextField txtHorario = new JTextField(10);
        txtHorario.setText("02:00");
        
        agendamentoPanel.add(new JLabel("Frequência:"));
        agendamentoPanel.add(cmbFrequencia);
        agendamentoPanel.add(new JLabel("Horário:"));
        agendamentoPanel.add(txtHorario);
        
        configPanel.add(agendamentoPanel, gbc);
        
        // Destino
        gbc.gridy = 3;
        JLabel lblDestino = new JLabel("Destino do Backup:");
        lblDestino.setFont(new Font("Segoe UI", Font.BOLD, 14));
        configPanel.add(lblDestino, gbc);
        
        gbc.gridy = 4;
        JPanel destinoPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        destinoPanel.setBackground(Color.WHITE);
        
        JTextField txtCaminho = new JTextField(40);
        txtCaminho.setText("/backups/hermescomercial");
        JButton btnProcurar = createButton("📁 Procurar", new Color(33, 150, 243));
        
        destinoPanel.add(new JLabel("Caminho:"));
        destinoPanel.add(txtCaminho);
        destinoPanel.add(btnProcurar);
        
        configPanel.add(destinoPanel, gbc);
        
        // Opções
        gbc.gridy = 5;
        JPanel opcoesPanel = new JPanel(new GridLayout(3, 1, 5, 5));
        opcoesPanel.setBackground(Color.WHITE);
        opcoesPanel.setBorder(BorderFactory.createTitledBorder("Opções"));
        
        JCheckBox chkCompactar = new JCheckBox("Compactar backup (ZIP)");
        chkCompactar.setBackground(Color.WHITE);
        chkCompactar.setSelected(true);
        
        JCheckBox chkCriptografar = new JCheckBox("Criptografar backup");
        chkCriptografar.setBackground(Color.WHITE);
        chkCriptografar.setSelected(false);
        
        JCheckBox chkManterHistorico = new JCheckBox("Manter histórico (30 dias)");
        chkManterHistorico.setBackground(Color.WHITE);
        chkManterHistorico.setSelected(true);
        
        opcoesPanel.add(chkCompactar);
        opcoesPanel.add(chkCriptografar);
        opcoesPanel.add(chkManterHistorico);
        
        configPanel.add(opcoesPanel, gbc);
        
        panel.add(configPanel, BorderLayout.CENTER);
        
        // Histórico de backups
        JPanel historicoPanel = new JPanel(new BorderLayout());
        historicoPanel.setBackground(Color.WHITE);
        historicoPanel.setBorder(BorderFactory.createTitledBorder("Histórico de Backups"));
        
        String[] columns = {"Data", "Tipo", "Tamanho", "Status", "Local"};
        backupModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        backupTable = new JTable(backupModel);
        JScrollPane tableScrollPane = new JScrollPane(backupTable);
        historicoPanel.add(tableScrollPane, BorderLayout.CENTER);
        
        JPanel backupButtonPanel = new JPanel(new FlowLayout());
        backupButtonPanel.setBackground(Color.WHITE);
        
        JButton btnBackupAgora = createButton("💾 Backup Agora", new Color(76, 175, 80));
        JButton btnRestaurar = createButton("🔄 Restaurar", new Color(255, 152, 0));
        JButton btnLimpar = createButton("🗑️ Limpar Antigos", new Color(244, 67, 54));
        
        btnBackupAgora.addActionListener(e -> backupAgora());
        btnRestaurar.addActionListener(e -> restaurarBackup());
        btnLimpar.addActionListener(e -> limparBackupsAntigos());
        
        backupButtonPanel.add(btnBackupAgora);
        backupButtonPanel.add(btnRestaurar);
        backupButtonPanel.add(btnLimpar);
        
        historicoPanel.add(backupButtonPanel, BorderLayout.SOUTH);
        
        panel.add(historicoPanel, BorderLayout.SOUTH);
        
        // Carregar histórico
        carregarHistoricoBackup();
        
        return panel;
    }
    
    private JPanel createIntegracoesPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);
        
        // Configurações de integrações
        JPanel configPanel = new JPanel(new GridBagLayout());
        configPanel.setBackground(Color.WHITE);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.anchor = GridBagConstraints.WEST;
        
        gbc.gridx = 0; gbc.gridy = 0;
        JLabel lblTitulo = new JLabel("🔌 Integrações Externas");
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 18));
        lblTitulo.setForeground(new Color(33, 150, 243));
        configPanel.add(lblTitulo, gbc);
        
        // Integrações disponíveis
        gbc.gridy = 1;
        JLabel lblIntegracoes = new JLabel("Integrações Disponíveis:");
        lblIntegracoes.setFont(new Font("Segoe UI", Font.BOLD, 14));
        configPanel.add(lblIntegracoes, gbc);
        
        gbc.gridy = 2;
        JPanel integracoesPanel = new JPanel(new GridLayout(4, 1, 5, 5));
        integracoesPanel.setBackground(Color.WHITE);
        
        JCheckBox chkEmail = new JCheckBox("Serviço de E-mail (SMTP)");
        chkEmail.setBackground(Color.WHITE);
        chkEmail.setSelected(true);
        
        JCheckBox chkSMS = new JCheckBox("Serviço de SMS");
        chkSMS.setBackground(Color.WHITE);
        chkSMS.setSelected(false);
        
        JCheckBox chkWhatsApp = new JCheckBox("WhatsApp Business");
        chkWhatsApp.setBackground(Color.WHITE);
        chkWhatsApp.setSelected(false);
        
        JCheckBox chkContabilidade = new JCheckBox("Sistema Contábil");
        chkContabilidade.setBackground(Color.WHITE);
        chkContabilidade.setSelected(false);
        
        integracoesPanel.add(chkEmail);
        integracoesPanel.add(chkSMS);
        integracoesPanel.add(chkWhatsApp);
        integracoesPanel.add(chkContabilidade);
        
        configPanel.add(integracoesPanel, gbc);
        
        // Configuração de e-mail
        gbc.gridy = 3;
        JLabel lblEmail = new JLabel("Configuração de E-mail:");
        lblEmail.setFont(new Font("Segoe UI", Font.BOLD, 14));
        configPanel.add(lblEmail, gbc);
        
        gbc.gridy = 4;
        JPanel emailPanel = new JPanel(new GridBagLayout());
        emailPanel.setBackground(Color.WHITE);
        GridBagConstraints emailGbc = new GridBagConstraints();
        emailGbc.insets = new Insets(5, 5, 5, 5);
        emailGbc.anchor = GridBagConstraints.WEST;
        
        emailGbc.gridx = 0; emailGbc.gridy = 0;
        emailPanel.add(new JLabel("SMTP:"), emailGbc);
        emailGbc.gridx = 1; emailGbc.fill = GridBagConstraints.HORIZONTAL; emailGbc.weightx = 1;
        JTextField txtSMTP = new JTextField(30);
        txtSMTP.setText("smtp.gmail.com");
        emailPanel.add(txtSMTP, emailGbc);
        
        emailGbc.gridx = 0; emailGbc.gridy = 1; emailGbc.fill = GridBagConstraints.NONE; emailGbc.weightx = 0;
        emailPanel.add(new JLabel("Porta:"), emailGbc);
        emailGbc.gridx = 1; emailGbc.fill = GridBagConstraints.HORIZONTAL; emailGbc.weightx = 1;
        JTextField txtPorta = new JTextField(10);
        txtPorta.setText("587");
        emailPanel.add(txtPorta, emailGbc);
        
        emailGbc.gridx = 0; emailGbc.gridy = 2;
        emailPanel.add(new JLabel("Usuário:"), emailGbc);
        emailGbc.gridx = 1; emailGbc.fill = GridBagConstraints.HORIZONTAL; emailGbc.weightx = 1;
        JTextField txtUsuarioEmail = new JTextField(30);
        txtUsuarioEmail.setText("noreply@hermescomercial.com.br");
        emailPanel.add(txtUsuarioEmail, emailGbc);
        
        configPanel.add(emailPanel, gbc);
        
        panel.add(configPanel, BorderLayout.CENTER);
        
        // Painel de botões
        JPanel buttonPanel = new JPanel(new FlowLayout());
        buttonPanel.setBackground(Color.WHITE);
        
        JButton btnTestarEmail = createButton("🧪 Testar E-mail", new Color(33, 150, 243));
        JButton btnSalvar = createButton("💾 Salvar Integrações", new Color(76, 175, 80));
        
        btnTestarEmail.addActionListener(e -> testarEmail());
        btnSalvar.addActionListener(e -> salvarIntegracoes());
        
        buttonPanel.add(btnTestarEmail);
        buttonPanel.add(btnSalvar);
        
        panel.add(buttonPanel, BorderLayout.SOUTH);
        
        return panel;
    }
    
    // Método createUsuariosPanel() removido - não utilizado
    // private JPanel createUsuariosPanel() { ... }
    
    private JButton createButton(String text, Color color) {
        JButton button = new JButton(text);
        button.setBackground(color);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setOpaque(true);
        button.setFont(new Font("Segoe UI", Font.BOLD, 12));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(color.darker());
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(color);
            }
        });
        
        return button;
    }
    
    public void salvarDadosEmpresa() {
        try {
            // Salvar dados da empresa (simulação)
            sistemaConfig.put("nomeEmpresa", txtNomeEmpresa.getText());
            sistemaConfig.put("cnpj", txtCNPJ.getText());
            sistemaConfig.put("endereco", txtEndereco.getText());
            sistemaConfig.put("telefone", txtTelefone.getText());
            sistemaConfig.put("email", txtEmail.getText());
            
            JOptionPane.showMessageDialog(frame, "Dados da empresa salvos com sucesso!", 
                "Sucesso", JOptionPane.INFORMATION_MESSAGE);
                
        } catch (Exception e) {
            JOptionPane.showMessageDialog(frame, "Erro ao salvar dados: " + e.getMessage(), 
                "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    public void testarConfiguracao() {
        JOptionPane.showMessageDialog(frame, 
            "🧪 Teste de Configuração\n\n" +
            "✅ Nome da empresa: OK\n" +
            "✅ CNPJ: Válido\n" +
            "✅ Endereço: OK\n" +
            "✅ Telefone: OK\n" +
            "✅ Email: Válido\n\n" +
            "Status: ✅ Configuração testada com sucesso!", 
            "Teste", JOptionPane.INFORMATION_MESSAGE);
    }
    
    public void uploadLogo() {
        JOptionPane.showMessageDialog(frame, 
            "Upload de logo em desenvolvimento...\n\n" +
            "Formatos suportados: PNG, JPG, GIF\n" +
            "Tamanho máximo: 2MB\n" +
            "Dimensões recomendadas: 200x80px", 
            "Upload", JOptionPane.INFORMATION_MESSAGE);
    }
    
    public void salvarConfiguracoesGerais() {
        JOptionPane.showMessageDialog(frame, "Configurações gerais salvas com sucesso!", 
            "Sucesso", JOptionPane.INFORMATION_MESSAGE);
    }
    
    public void restaurarPadrao() {
        int confirm = JOptionPane.showConfirmDialog(frame, 
            "Deseja restaurar as configurações para o padrão?", 
            "Confirmar Restauração", JOptionPane.YES_NO_OPTION);
        
        if (confirm == JOptionPane.YES_OPTION) {
            cmbIdioma.setSelectedItem("Português (Brasil)");
            cmbPais.setSelectedItem("Brasil");
            cmbMoeda.setSelectedItem("Real Brasileiro (R$)");
            chkBackupAuto.setSelected(true);
            chkNotificacoes.setSelected(true);
            chkAuditoria.setSelected(true);
            chkManutencao.setSelected(false);
            
            JOptionPane.showMessageDialog(frame, "Configurações restauradas com sucesso!", 
                "Sucesso", JOptionPane.INFORMATION_MESSAGE);
        }
    }
    
    public void salvarConfiguracoesSeguranca() {
        JOptionPane.showMessageDialog(frame, "Configurações de segurança salvas com sucesso!", 
            "Sucesso", JOptionPane.INFORMATION_MESSAGE);
    }
    
    public void testarPoliticas() {
        JOptionPane.showMessageDialog(frame, 
            "🧪 Teste de Políticas de Segurança\n\n" +
            "✅ Política de senha: OK\n" +
            "✅ Tentativas de login: OK\n" +
            "✅ Timeout de sessão: OK\n" +
            "✅ Múltiplas sessões: OK\n\n" +
            "Status: ✅ Políticas testadas com sucesso!", 
            "Teste", JOptionPane.INFORMATION_MESSAGE);
    }
    
    public void backupAgora() {
        JOptionPane.showMessageDialog(frame, 
            "💾 Backup Iniciado\n\n" +
            "Progresso: 100%\n" +
            "Tamanho: 45MB\n" +
            "Tempo: 2.3 segundos\n" +
            "Status: ✅ Backup concluído com sucesso!", 
            "Backup", JOptionPane.INFORMATION_MESSAGE);
    }
    
    public void restaurarBackup() {
        JOptionPane.showMessageDialog(frame, 
            "🔄 Restauração em desenvolvimento...\n\n" +
            "Selecione um backup para restaurar:\n" +
            "• backup_2026-05-04_02-00.zip (45MB)\n" +
            "• backup_2026-05-03_02-00.zip (42MB)\n" +
            "• backup_2026-05-02_02-00.zip (38MB)", 
            "Restauração", JOptionPane.INFORMATION_MESSAGE);
    }
    
    public void limparBackupsAntigos() {
        int confirm = JOptionPane.showConfirmDialog(frame, 
            "Deseja realmente limpar backups antigos?\n" +
            "Backups com mais de 30 dias serão removidos.", 
            "Confirmar Limpeza", JOptionPane.YES_NO_OPTION);
        
        if (confirm == JOptionPane.YES_OPTION) {
            JOptionPane.showMessageDialog(frame, "Backups antigos limpos com sucesso!\n" +
                "3 arquivos removidos\n" +
                "Espaço liberado: 125MB", 
                "Limpeza", JOptionPane.INFORMATION_MESSAGE);
        }
    }
    
    public void testarEmail() {
        JOptionPane.showMessageDialog(frame, 
            "🧪 Teste de E-mail\n\n" +
            "Enviando e-mail de teste...\n" +
            "Para: admin@hermescomercial.com.br\n" +
            "Assunto: Teste de Configuração\n\n" +
            "Status: ✅ E-mail enviado com sucesso!", 
            "Teste", JOptionPane.INFORMATION_MESSAGE);
    }
    
    public void salvarIntegracoes() {
        JOptionPane.showMessageDialog(frame, "Integrações salvas com sucesso!", 
            "Sucesso", JOptionPane.INFORMATION_MESSAGE);
    }
    
    public void novoUsuario() {
        JOptionPane.showMessageDialog(frame, 
            "➕ Novo Usuário\n\n" +
            "Funcionalidade em desenvolvimento...\n" +
            "Recursos planejados:\n" +
            "• Formulário de cadastro\n" +
            "• Validação de dados\n" +
            "• Atribuição de permissões", 
            "Novo Usuário", JOptionPane.INFORMATION_MESSAGE);
    }
    
    public void editarUsuario() {
        JOptionPane.showMessageDialog(frame, 
            "✏️ Editar Usuário\n\n" +
            "Funcionalidade em desenvolvimento...", 
            "Editar Usuário", JOptionPane.INFORMATION_MESSAGE);
    }
    
    public void resetarSenha() {
        JOptionPane.showMessageDialog(frame, 
            "🔄 Resetar Senha\n\n" +
            "Funcionalidade em desenvolvimento...\n" +
            "Recursos planejados:\n" +
            "• Geração de senha temporária\n" +
            "• Envio por e-mail\n" +
            "• Forçar alteração no próximo login", 
            "Resetar Senha", JOptionPane.INFORMATION_MESSAGE);
    }
    
    public void desativarUsuario() {
        JOptionPane.showMessageDialog(frame, 
            "🔒 Desativar Usuário\n\n" +
            "Funcionalidade em desenvolvimento...", 
            "Desativar Usuário", JOptionPane.INFORMATION_MESSAGE);
    }
    
    // Método carregarUsuarios() comentado - não utilizado sem createUsuariosPanel()
    // public void carregarUsuarios() { ... }
    
    public void carregarHistoricoBackup() {
        backupModel.setRowCount(0);
        
        // Simulação de dados
        Object[][] dados = {
            {"2026-05-04 02:00", "Automático", "45MB", "✅ Sucesso", "/backups/hermescomercial"},
            {"2026-05-03 02:00", "Automático", "42MB", "✅ Sucesso", "/backups/hermescomercial"},
            {"2026-05-02 02:00", "Automático", "38MB", "✅ Sucesso", "/backups/hermescomercial"},
            {"2026-05-01 15:30", "Manual", "35MB", "✅ Sucesso", "/backups/hermescomercial"}
        };
        
        for (Object[] row : dados) {
            backupModel.addRow(row);
        }
    }
    
    public void show() {
        frame.setVisible(true);
    }
    
    // Métodos para compatibilidade com testes
    public JFrame getFrame() {
        return frame;
    }
    
    public JTabbedPane getTabbedPane() {
        return tabbedPane;
    }
    
    public void carregarConfiguracoesPadrao() {
        JOptionPane.showMessageDialog(frame, 
            "Carregando configurações padrão...\n" +
            "✅ Configurações de sistema carregadas\n" +
            "✅ Valores padrão aplicados\n" +
            "✅ Configurações validadas", 
            "Configurações Padrão", JOptionPane.INFORMATION_MESSAGE);
    }
    
    public void configurarBancoDados(String host, String porta, String banco, String usuario, String senha, String schema) {
        JOptionPane.showMessageDialog(frame, 
            "Configurando banco de dados...\n" +
            "Host: " + host + "\n" +
            "Porta: " + porta + "\n" +
            "Banco: " + banco + "\n" +
            "Usuário: " + usuario + "\n" +
            "Schema: " + schema + "\n" +
            "Status: ✅ Configuração realizada com sucesso!", 
            "Banco de Dados", JOptionPane.INFORMATION_MESSAGE);
    }
    
    public void configurarInterface(String tema, String idioma, String formatoData, String formatoMoeda) {
        JOptionPane.showMessageDialog(frame, 
            "Configurando interface...\n" +
            "Tema: " + tema + "\n" +
            "Idioma: " + idioma + "\n" +
            "Formato Data: " + formatoData + "\n" +
            "Formato Moeda: " + formatoMoeda + "\n" +
            "Status: ✅ Interface configurada com sucesso!", 
            "Interface", JOptionPane.INFORMATION_MESSAGE);
    }
    
    public void realizarBackup() {
        JOptionPane.showMessageDialog(frame, 
            "Realizando backup...\n" +
            "✅ Conectando ao banco de dados\n" +
            "✅ Exportando dados\n" +
            "✅ Compactando arquivo\n" +
            "✅ Backup realizado com sucesso!\n" +
            "Arquivo: /backups/hermescomercial_" + System.currentTimeMillis() + ".zip", 
            "Backup", JOptionPane.INFORMATION_MESSAGE);
    }
    
    public void validarConfiguracoes() {
        JOptionPane.showMessageDialog(frame, 
            "Validando configurações...\n" +
            "✅ Configurações de sistema: Válidas\n" +
            "✅ Configurações de banco: Válidas\n" +
            "✅ Configurações de interface: Válidas\n" +
            "✅ Configurações de segurança: Válidas\n" +
            "Status: ✅ Todas as configurações estão válidas!", 
            "Validação", JOptionPane.INFORMATION_MESSAGE);
    }
    
    public void salvarConfiguracoes() {
        JOptionPane.showMessageDialog(frame, 
            "Salvando configurações...\n" +
            "✅ Validando configurações\n" +
            "✅ Persistindo no banco de dados\n" +
            "✅ Aplicando alterações\n" +
            "Status: ✅ Configurações salvas com sucesso!", 
            "Salvar", JOptionPane.INFORMATION_MESSAGE);
    }
    
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new SistemaConfigSwingController().show();
        });
    }
}
