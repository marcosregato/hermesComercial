package com.br.hermescomercial.pdv.controller;

import com.br.hermescomercial.util.SystemLogger;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.List;

/**
 * Classe especializada para o formulário de Entregas
 * Estrutura: Header → Busca → Formulário → Tabela
 */
public class PDVFormularioEntregas {
    
    private JPanel workArea;
    private String usuarioAtual;
    private String nomeUsuario;
    
    // Componentes do formulário
    private JPanel dateChooserDataEntrega;
    private JTextField txtNumeroEntrega;
    private JTextField txtNumeroVenda;
    private JTextField txtCliente;
    private JTextField txtCPF;
    private JTextField txtEndereco;
    private JTextField txtBairro;
    private JTextField txtCidade;
    private JTextField txtCEP;
    private JTextField txtTelefone;
    private JTextField txtMotorista;
    private JTextField txtVeiculo;
    private JTextField txtPlaca;
    private JComboBox<String> comboStatus;
    private JComboBox<String> comboTipoEntrega;
    private JTextArea txtObservacoes;
    
    // Tabela de entregas
    private JTable tabelaEntregas;
    private DefaultTableModel modelTabela;
    private List<Entrega> entregasEncontradas;
    
    // Cores
    private static final Color WHITE = Color.WHITE;
    private static final Color ACCENT_COLOR = new Color(52, 152, 219);
    private static final Color SUCCESS_COLOR = new Color(39, 174, 96);
    private static final Color WARNING_COLOR = new Color(241, 196, 15);
    private static final Color DANGER_COLOR = new Color(231, 76, 60);
    private static final Color GRAY = new Color(149, 165, 166);
    
    public PDVFormularioEntregas(JPanel workArea, String usuarioAtual, String nomeUsuario) {
        this.workArea = workArea;
        this.usuarioAtual = usuarioAtual;
        this.nomeUsuario = nomeUsuario;
        this.entregasEncontradas = new ArrayList<>();
    }
    
    /**
     * Cria o formulário completo de Entregas
     */
    public JPanel criarFormularioEntregas() {
        try {
            SystemLogger.ui("=== CRIANDO FORMULÁRIO ENTREGAS ===");
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
            
            // Painel do formulário de entrega
            JPanel formularioPanel = criarPainelFormulario();
            painelCentral.add(formularioPanel, BorderLayout.CENTER);
            
            // Tabela de entregas
            JPanel tabelaPanel = criarPainelTabela();
            painelCentral.add(tabelaPanel, BorderLayout.SOUTH);
            
            painelPrincipal.add(painelCentral, BorderLayout.CENTER);
            
            SystemLogger.ui("Formulário Entregas criado com sucesso");
            return painelPrincipal;
            
        } catch (Exception e) {
            SystemLogger.error("Erro ao criar formulário Entregas", e);
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
        JLabel titleLabel = new JLabel("🚚 Entregas");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        titleLabel.setForeground(WHITE);
        
        // Informações
        JPanel infoPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        infoPanel.setOpaque(false);
        
        JLabel usuarioLabel = new JLabel("👤 " + nomeUsuario);
        usuarioLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        usuarioLabel.setForeground(WHITE);
        
        JLabel dataLabel = new JLabel("📅 " + new SimpleDateFormat("dd/MM/yyyy HH:mm").format(new Date()));
        dataLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        dataLabel.setForeground(WHITE);
        
        infoPanel.add(usuarioLabel);
        infoPanel.add(Box.createHorizontalStrut(20));
        infoPanel.add(dataLabel);
        
        headerPanel.add(titleLabel, BorderLayout.WEST);
        headerPanel.add(infoPanel, BorderLayout.EAST);
        
        return headerPanel;
    }
    
    /**
     * Cria o painel de busca rápida
     */
    private JPanel criarPainelBusca() {
        JPanel buscaPanel = new JPanel(new BorderLayout());
        buscaPanel.setBackground(new Color(245, 245, 245));
        buscaPanel.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));
        
        JLabel buscaLabel = new JLabel("🔍 Busca Rápida");
        buscaLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        buscaLabel.setForeground(ACCENT_COLOR);
        
        JPanel buscaContainer = new JPanel(new BorderLayout());
        buscaContainer.setOpaque(false);
        
        JTextField txtBuscaRapida = new JTextField();
        txtBuscaRapida.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        txtBuscaRapida.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(GRAY),
            BorderFactory.createEmptyBorder(8, 12, 8, 12)
        ));
        txtBuscaRapida.setToolTipText("Digite número da entrega, venda ou cliente para busca rápida");
        
        // Enter para buscar
        txtBuscaRapida.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    realizarBuscaRapida(txtBuscaRapida.getText().trim());
                }
            }
        });
        
        JButton btnBuscaRapida = new JButton("🔍 Buscar");
        btnBuscaRapida.setBackground(ACCENT_COLOR);
        btnBuscaRapida.setForeground(WHITE);
        btnBuscaRapida.setFocusPainted(false);
        btnBuscaRapida.setBorderPainted(false);
        btnBuscaRapida.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btnBuscaRapida.addActionListener(e -> realizarBuscaRapida(txtBuscaRapida.getText().trim()));
        
        buscaContainer.add(txtBuscaRapida, BorderLayout.CENTER);
        buscaContainer.add(btnBuscaRapida, BorderLayout.EAST);
        
        buscaPanel.add(buscaLabel, BorderLayout.NORTH);
        buscaPanel.add(buscaContainer, BorderLayout.CENTER);
        
        return buscaPanel;
    }
    
    /**
     * Cria o painel do formulário de entrega
     */
    private JPanel criarPainelFormulario() {
        JPanel formularioPanel = new JPanel(new GridBagLayout());
        formularioPanel.setBackground(WHITE);
        formularioPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
        // Seção Dados da Entrega
        JLabel entregaSectionLabel = new JLabel("🚚 Dados da Entrega");
        entregaSectionLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        entregaSectionLabel.setForeground(ACCENT_COLOR);
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 4;
        formularioPanel.add(entregaSectionLabel, gbc);
        
        gbc.gridy = 1; gbc.gridwidth = 1;
        JLabel lblNumeroEntrega = new JLabel("Nº Entrega:");
        lblNumeroEntrega.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        formularioPanel.add(lblNumeroEntrega, gbc);
        
        gbc.gridx = 1; gbc.weightx = 1.0;
        txtNumeroEntrega = new JTextField(gerarNumeroEntrega());
        txtNumeroEntrega.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        txtNumeroEntrega.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(GRAY),
            BorderFactory.createEmptyBorder(5, 8, 5, 8)
        ));
        txtNumeroEntrega.setEditable(false);
        formularioPanel.add(txtNumeroEntrega, gbc);
        
        gbc.gridx = 2; gbc.weightx = 0.0;
        JLabel lblDataEntrega = new JLabel("Data Entrega:");
        lblDataEntrega.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        formularioPanel.add(lblDataEntrega, gbc);
        
        gbc.gridx = 3; gbc.weightx = 1.0;
        dateChooserDataEntrega = criarDateChooser();
        formularioPanel.add(dateChooserDataEntrega, gbc);
        
        // Seção Venda
        gbc.gridy = 2; gbc.gridx = 0; gbc.gridwidth = 4; gbc.weightx = 0.0;
        JLabel vendaSectionLabel = new JLabel("📋 Venda Original");
        vendaSectionLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        vendaSectionLabel.setForeground(ACCENT_COLOR);
        formularioPanel.add(vendaSectionLabel, gbc);
        
        gbc.gridy = 3; gbc.gridwidth = 1;
        JLabel lblNumeroVenda = new JLabel("Nº Venda:");
        lblNumeroVenda.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        formularioPanel.add(lblNumeroVenda, gbc);
        
        gbc.gridx = 1; gbc.weightx = 1.0;
        txtNumeroVenda = new JTextField();
        txtNumeroVenda.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        txtNumeroVenda.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(GRAY),
            BorderFactory.createEmptyBorder(5, 8, 5, 8)
        ));
        txtNumeroVenda.setToolTipText("Digite o número da venda original");
        formularioPanel.add(txtNumeroVenda, gbc);
        
        gbc.gridx = 2; gbc.weightx = 0.0;
        JLabel lblTipoEntrega = new JLabel("Tipo Entrega:");
        lblTipoEntrega.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        formularioPanel.add(lblTipoEntrega, gbc);
        
        gbc.gridx = 3; gbc.weightx = 1.0;
        comboTipoEntrega = new JComboBox<>(new String[]{"Normal", "Expressa", "Agendada", "Retirada"});
        comboTipoEntrega.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        comboTipoEntrega.setBorder(BorderFactory.createLineBorder(GRAY));
        formularioPanel.add(comboTipoEntrega, gbc);
        
        // Seção Cliente
        gbc.gridy = 4; gbc.gridx = 0; gbc.gridwidth = 4; gbc.weightx = 0.0;
        JLabel clienteSectionLabel = new JLabel("👤 Dados do Cliente");
        clienteSectionLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        clienteSectionLabel.setForeground(ACCENT_COLOR);
        formularioPanel.add(clienteSectionLabel, gbc);
        
        gbc.gridy = 5; gbc.gridwidth = 1;
        JLabel lblCliente = new JLabel("Cliente:");
        lblCliente.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        formularioPanel.add(lblCliente, gbc);
        
        gbc.gridx = 1; gbc.weightx = 1.0;
        txtCliente = new JTextField();
        txtCliente.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        txtCliente.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(GRAY),
            BorderFactory.createEmptyBorder(5, 8, 5, 8)
        ));
        formularioPanel.add(txtCliente, gbc);
        
        gbc.gridx = 2; gbc.weightx = 0.0;
        JLabel lblCPF = new JLabel("CPF:");
        lblCPF.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        formularioPanel.add(lblCPF, gbc);
        
        gbc.gridx = 3; gbc.weightx = 1.0;
        txtCPF = new JTextField();
        txtCPF.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        txtCPF.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(GRAY),
            BorderFactory.createEmptyBorder(5, 8, 5, 8)
        ));
        formularioPanel.add(txtCPF, gbc);
        
        gbc.gridy = 6; gbc.gridx = 0;
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
        
        gbc.gridx = 2; gbc.weightx = 0.0;
        JLabel lblStatus = new JLabel("Status:");
        lblStatus.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        formularioPanel.add(lblStatus, gbc);
        
        gbc.gridx = 3; gbc.weightx = 1.0;
        comboStatus = new JComboBox<>(new String[]{"Pendente", "Em Rota", "Entregue", "Cancelada", "Devolvida"});
        comboStatus.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        comboStatus.setBorder(BorderFactory.createLineBorder(GRAY));
        formularioPanel.add(comboStatus, gbc);
        
        // Seção Endereço
        gbc.gridy = 7; gbc.gridx = 0; gbc.gridwidth = 4; gbc.weightx = 0.0;
        JLabel enderecoSectionLabel = new JLabel("📍 Endereço de Entrega");
        enderecoSectionLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        enderecoSectionLabel.setForeground(ACCENT_COLOR);
        formularioPanel.add(enderecoSectionLabel, gbc);
        
        gbc.gridy = 8; gbc.gridwidth = 2;
        JLabel lblEndereco = new JLabel("Endereço:");
        lblEndereco.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        formularioPanel.add(lblEndereco, gbc);
        
        gbc.gridx = 2; gbc.gridwidth = 2;
        JLabel lblBairro = new JLabel("Bairro:");
        lblBairro.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        formularioPanel.add(lblBairro, gbc);
        
        gbc.gridy = 9; gbc.gridx = 0; gbc.gridwidth = 2; gbc.weightx = 1.0;
        txtEndereco = new JTextField();
        txtEndereco.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        txtEndereco.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(GRAY),
            BorderFactory.createEmptyBorder(5, 8, 5, 8)
        ));
        formularioPanel.add(txtEndereco, gbc);
        
        gbc.gridx = 2; gbc.gridwidth = 2; gbc.weightx = 1.0;
        txtBairro = new JTextField();
        txtBairro.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        txtBairro.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(GRAY),
            BorderFactory.createEmptyBorder(5, 8, 5, 8)
        ));
        formularioPanel.add(txtBairro, gbc);
        
        gbc.gridy = 10; gbc.gridx = 0; gbc.gridwidth = 2; gbc.weightx = 1.0;
        JLabel lblCidade = new JLabel("Cidade:");
        lblCidade.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        formularioPanel.add(lblCidade, gbc);
        
        gbc.gridx = 2; gbc.gridwidth = 2; gbc.weightx = 1.0;
        JLabel lblCEP = new JLabel("CEP:");
        lblCEP.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        formularioPanel.add(lblCEP, gbc);
        
        gbc.gridy = 11; gbc.gridx = 0; gbc.gridwidth = 2; gbc.weightx = 1.0;
        txtCidade = new JTextField();
        txtCidade.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        txtCidade.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(GRAY),
            BorderFactory.createEmptyBorder(5, 8, 5, 8)
        ));
        formularioPanel.add(txtCidade, gbc);
        
        gbc.gridx = 2; gbc.gridwidth = 2; gbc.weightx = 1.0;
        txtCEP = new JTextField();
        txtCEP.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        txtCEP.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(GRAY),
            BorderFactory.createEmptyBorder(5, 8, 5, 8)
        ));
        formularioPanel.add(txtCEP, gbc);
        
        // Seção Transporte
        gbc.gridy = 12; gbc.gridx = 0; gbc.gridwidth = 4; gbc.weightx = 0.0;
        JLabel transporteSectionLabel = new JLabel("🚚 Dados do Transporte");
        transporteSectionLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        transporteSectionLabel.setForeground(ACCENT_COLOR);
        formularioPanel.add(transporteSectionLabel, gbc);
        
        gbc.gridy = 13; gbc.gridwidth = 1;
        JLabel lblMotorista = new JLabel("Motorista:");
        lblMotorista.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        formularioPanel.add(lblMotorista, gbc);
        
        gbc.gridx = 1; gbc.weightx = 1.0;
        txtMotorista = new JTextField();
        txtMotorista.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        txtMotorista.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(GRAY),
            BorderFactory.createEmptyBorder(5, 8, 5, 8)
        ));
        formularioPanel.add(txtMotorista, gbc);
        
        gbc.gridx = 2; gbc.weightx = 0.0;
        JLabel lblVeiculo = new JLabel("Veículo:");
        lblVeiculo.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        formularioPanel.add(lblVeiculo, gbc);
        
        gbc.gridx = 3; gbc.weightx = 1.0;
        txtVeiculo = new JTextField();
        txtVeiculo.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        txtVeiculo.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(GRAY),
            BorderFactory.createEmptyBorder(5, 8, 5, 8)
        ));
        formularioPanel.add(txtVeiculo, gbc);
        
        gbc.gridy = 14; gbc.gridx = 0; gbc.gridwidth = 1; gbc.weightx = 0.0;
        JLabel lblPlaca = new JLabel("Placa:");
        lblPlaca.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        formularioPanel.add(lblPlaca, gbc);
        
        gbc.gridx = 1; gbc.gridwidth = 3; gbc.weightx = 1.0;
        txtPlaca = new JTextField();
        txtPlaca.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        txtPlaca.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(GRAY),
            BorderFactory.createEmptyBorder(5, 8, 5, 8)
        ));
        formularioPanel.add(txtPlaca, gbc);
        
        // Seção Observações
        gbc.gridy = 15; gbc.gridx = 0; gbc.gridwidth = 4; gbc.weightx = 0.0;
        JLabel obsSectionLabel = new JLabel("📄 Observações");
        obsSectionLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        obsSectionLabel.setForeground(ACCENT_COLOR);
        formularioPanel.add(obsSectionLabel, gbc);
        
        gbc.gridy = 16; gbc.gridx = 0; gbc.gridwidth = 4; gbc.weightx = 1.0; gbc.fill = GridBagConstraints.BOTH;
        txtObservacoes = new JTextArea(3, 50);
        txtObservacoes.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        txtObservacoes.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(GRAY),
            BorderFactory.createEmptyBorder(8, 8, 8, 8)
        ));
        txtObservacoes.setLineWrap(true);
        txtObservacoes.setWrapStyleWord(true);
        txtObservacoes.setToolTipText("Observações adicionais sobre a entrega");
        
        JScrollPane scrollObs = new JScrollPane(txtObservacoes);
        scrollObs.setPreferredSize(new Dimension(0, 80));
        formularioPanel.add(scrollObs, gbc);
        
        // Botões de ação
        gbc.gridy = 17; gbc.gridx = 0; gbc.gridwidth = 4; gbc.weightx = 0.0; gbc.fill = GridBagConstraints.HORIZONTAL;
        JPanel botoesPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        botoesPanel.setBackground(WHITE);
        
        JButton btnRegistrar = new JButton("✅ Registrar Entrega");
        btnRegistrar.setBackground(SUCCESS_COLOR);
        btnRegistrar.setForeground(WHITE);
        btnRegistrar.setFocusPainted(false);
        btnRegistrar.setBorderPainted(false);
        btnRegistrar.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btnRegistrar.addActionListener(e -> registrarEntrega());
        
        JButton btnLimpar = new JButton("🗑️ Limpar");
        btnLimpar.setBackground(GRAY);
        btnLimpar.setForeground(WHITE);
        btnLimpar.setFocusPainted(false);
        btnLimpar.setBorderPainted(false);
        btnLimpar.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btnLimpar.addActionListener(e -> limparCampos());
        
        JButton btnConsultar = new JButton("🔍 Consultar");
        btnConsultar.setBackground(ACCENT_COLOR);
        btnConsultar.setForeground(WHITE);
        btnConsultar.setFocusPainted(false);
        btnConsultar.setBorderPainted(false);
        btnConsultar.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btnConsultar.addActionListener(e -> consultarEntregas());
        
        JButton btnRota = new JButton("🗺️ Gerar Rota");
        btnRota.setBackground(WARNING_COLOR);
        btnRota.setForeground(WHITE);
        btnRota.setFocusPainted(false);
        btnRota.setBorderPainted(false);
        btnRota.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btnRota.addActionListener(e -> gerarRota());
        
        botoesPanel.add(btnRegistrar);
        botoesPanel.add(Box.createHorizontalStrut(10));
        botoesPanel.add(btnLimpar);
        botoesPanel.add(Box.createHorizontalStrut(10));
        botoesPanel.add(btnConsultar);
        botoesPanel.add(Box.createHorizontalStrut(10));
        botoesPanel.add(btnRota);
        
        formularioPanel.add(botoesPanel, gbc);
        
        return formularioPanel;
    }
    
    /**
     * Cria um campo de data simplificado
     */
    private JPanel criarDateChooser() {
        JTextField dateField = new JTextField();
        dateField.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        dateField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(GRAY),
            BorderFactory.createEmptyBorder(5, 8, 5, 8)
        ));
        dateField.setText(new SimpleDateFormat("dd/MM/yyyy").format(new Date()));
        dateField.setToolTipText("DD/MM/AAAA");
        
        JPanel datePanel = new JPanel(new BorderLayout());
        datePanel.setOpaque(false);
        datePanel.add(dateField, BorderLayout.CENTER);
        
        JButton btnCalendario = new JButton("📅");
        btnCalendario.setFont(new Font("Segoe UI", Font.PLAIN, 10));
        btnCalendario.setBackground(Color.LIGHT_GRAY);
        btnCalendario.setBorderPainted(false);
        btnCalendario.setFocusPainted(false);
        btnCalendario.setPreferredSize(new Dimension(30, 20));
        
        datePanel.add(btnCalendario, BorderLayout.EAST);
        
        return datePanel;
    }
    
    /**
     * Gera número automático para entrega
     */
    private String gerarNumeroEntrega() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        String data = sdf.format(new Date());
        Random random = new Random();
        int numero = random.nextInt(10000);
        return "ENT-" + data + "-" + String.format("%04d", numero);
    }
    
    /**
     * Cria o painel da tabela de entregas
     */
    private JPanel criarPainelTabela() {
        JPanel tabelaPanel = new JPanel(new BorderLayout());
        tabelaPanel.setBackground(WHITE);
        tabelaPanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 20, 20));
        
        JLabel tabelaLabel = new JLabel("📋 Histórico de Entregas");
        tabelaLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        tabelaLabel.setForeground(ACCENT_COLOR);
        
        // Modelo da tabela
        String[] colunas = {"Nº Entrega", "Data", "Nº Venda", "Cliente", "Telefone", "Endereço Completo", "Motorista", "Veículo", "Placa", "Tipo", "Status", "Data Prevista", "Data Realizada", "Observações", "Ações"};
        modelTabela = new DefaultTableModel(colunas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 8; // Apenas a coluna de ações é editável
            }
        };
        
        tabelaEntregas = new JTable(modelTabela);
        tabelaEntregas.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        tabelaEntregas.setRowHeight(25);
        tabelaEntregas.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 12));
        tabelaEntregas.getTableHeader().setBackground(ACCENT_COLOR);
        tabelaEntregas.getTableHeader().setForeground(WHITE);
        
        // Scroll pane
        JScrollPane scrollPane = new JScrollPane(tabelaEntregas);
        scrollPane.setPreferredSize(new Dimension(0, 250));
        scrollPane.setBorder(BorderFactory.createLineBorder(GRAY));
        
        // Painel de botões da tabela
        JPanel botoesTabelaPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        botoesTabelaPanel.setBackground(WHITE);
        
        JButton btnDetalhes = new JButton("👁️ Ver Detalhes");
        btnDetalhes.setBackground(ACCENT_COLOR);
        btnDetalhes.setForeground(WHITE);
        btnDetalhes.setFocusPainted(false);
        btnDetalhes.setBorderPainted(false);
        btnDetalhes.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btnDetalhes.addActionListener(e -> verDetalhesEntrega());
        
        JButton btnIniciar = new JButton("🚚 Iniciar Entrega");
        btnIniciar.setBackground(SUCCESS_COLOR);
        btnIniciar.setForeground(WHITE);
        btnIniciar.setFocusPainted(false);
        btnIniciar.setBorderPainted(false);
        btnIniciar.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btnIniciar.addActionListener(e -> iniciarEntrega());
        
        JButton btnConcluir = new JButton("✅ Concluir Entrega");
        btnConcluir.setBackground(SUCCESS_COLOR);
        btnConcluir.setForeground(WHITE);
        btnConcluir.setFocusPainted(false);
        btnConcluir.setBorderPainted(false);
        btnConcluir.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btnConcluir.addActionListener(e -> concluirEntrega());
        
        JButton btnCancelar = new JButton("❌ Cancelar");
        btnCancelar.setBackground(DANGER_COLOR);
        btnCancelar.setForeground(WHITE);
        btnCancelar.setFocusPainted(false);
        btnCancelar.setBorderPainted(false);
        btnCancelar.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btnCancelar.addActionListener(e -> cancelarEntrega());
        
        JButton btnImprimir = new JButton("🖨️ Imprimir");
        btnImprimir.setBackground(GRAY);
        btnImprimir.setForeground(WHITE);
        btnImprimir.setFocusPainted(false);
        btnImprimir.setBorderPainted(false);
        btnImprimir.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btnImprimir.addActionListener(e -> imprimirEntrega());
        
        botoesTabelaPanel.add(btnDetalhes);
        botoesTabelaPanel.add(Box.createHorizontalStrut(10));
        botoesTabelaPanel.add(btnIniciar);
        botoesTabelaPanel.add(Box.createHorizontalStrut(10));
        botoesTabelaPanel.add(btnConcluir);
        botoesTabelaPanel.add(Box.createHorizontalStrut(10));
        botoesTabelaPanel.add(btnCancelar);
        botoesTabelaPanel.add(Box.createHorizontalStrut(10));
        botoesTabelaPanel.add(btnImprimir);
        
        tabelaPanel.add(tabelaLabel, BorderLayout.NORTH);
        tabelaPanel.add(scrollPane, BorderLayout.CENTER);
        tabelaPanel.add(botoesTabelaPanel, BorderLayout.SOUTH);
        
        return tabelaPanel;
    }
    
    /**
     * Realiza busca rápida
     */
    private void realizarBuscaRapida(String termo) {
        if (termo.isEmpty()) {
            JOptionPane.showMessageDialog(workArea, "Digite um termo para buscar!", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        // TODO: Implementar lógica de busca rápida no banco de dados
        JOptionPane.showMessageDialog(workArea, "Busca rápida por: " + termo + "\n(Implementar busca no banco)", "Busca Rápida", JOptionPane.INFORMATION_MESSAGE);
    }
    
    /**
     * Registra uma nova entrega
     */
    private void registrarEntrega() {
        try {
            // Validações
            if (txtNumeroVenda.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(workArea, "Informe o número da venda original!", "Aviso", JOptionPane.WARNING_MESSAGE);
                return;
            }
            
            if (txtCliente.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(workArea, "Informe o nome do cliente!", "Aviso", JOptionPane.WARNING_MESSAGE);
                return;
            }
            
            if (txtEndereco.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(workArea, "Informe o endereço de entrega!", "Aviso", JOptionPane.WARNING_MESSAGE);
                return;
            }
            
            if (txtMotorista.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(workArea, "Informe o nome do motorista!", "Aviso", JOptionPane.WARNING_MESSAGE);
                return;
            }
            
            // Coletar dados
            String numeroEntrega = txtNumeroEntrega.getText().trim();
            String dataEntrega = ((JTextField) dateChooserDataEntrega.getComponent(0)).getText();
            String numeroVenda = txtNumeroVenda.getText().trim();
            String cliente = txtCliente.getText().trim();
            String cpf = txtCPF.getText().trim();
            String endereco = txtEndereco.getText().trim();
            String bairro = txtBairro.getText().trim();
            String cidade = txtCidade.getText().trim();
            String cep = txtCEP.getText().trim();
            String telefone = txtTelefone.getText().trim();
            String motorista = txtMotorista.getText().trim();
            String veiculo = txtVeiculo.getText().trim();
            String placa = txtPlaca.getText().trim();
            String tipoEntrega = (String) comboTipoEntrega.getSelectedItem();
            String status = (String) comboStatus.getSelectedItem();
            String observacoes = txtObservacoes.getText().trim();
            
            // TODO: Implementar lógica de salvamento no banco de dados
            
            // Adicionar à tabela
            Object[] rowData = {
                numeroEntrega,
                dataEntrega,
                numeroVenda,
                cliente,
                endereco + ", " + bairro,
                motorista,
                veiculo + " (" + placa + ")",
                status,
                "👁️"
            };
            modelTabela.addRow(rowData);
            
            // Adicionar à lista
            Entrega entrega = new Entrega();
            entrega.setNumero(numeroEntrega);
            entrega.setData(dataEntrega);
            entrega.setNumeroVenda(numeroVenda);
            entrega.setCliente(cliente);
            entrega.setCpf(cpf);
            entrega.setEndereco(endereco);
            entrega.setBairro(bairro);
            entrega.setCidade(cidade);
            entrega.setCep(cep);
            entrega.setTelefone(telefone);
            entrega.setMotorista(motorista);
            entrega.setVeiculo(veiculo);
            entrega.setPlaca(placa);
            entrega.setTipo(tipoEntrega);
            entrega.setStatus(status);
            entrega.setObservacoes(observacoes);
            entregasEncontradas.add(entrega);
            
            // Limpar campos e gerar novo número
            limparCampos();
            txtNumeroEntrega.setText(gerarNumeroEntrega());
            
            JOptionPane.showMessageDialog(workArea, 
                "Entrega registrada com sucesso!\n" +
                "Número: " + numeroEntrega + "\n" +
                "Venda: " + numeroVenda + "\n" +
                "Cliente: " + cliente, 
                "Sucesso", JOptionPane.INFORMATION_MESSAGE);
            
            SystemLogger.ui("Entrega registrada: " + numeroEntrega);
            
        } catch (Exception e) {
            SystemLogger.error("Erro ao registrar entrega", e);
            JOptionPane.showMessageDialog(workArea, "Erro ao registrar entrega: " + e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    /**
     * Consulta entregas existentes
     */
    private void consultarEntregas() {
        try {
            // TODO: Implementar lógica de consulta no banco de dados
            // Por enquanto, vamos adicionar dados de exemplo
            adicionarDadosExemplo();
            
            JOptionPane.showMessageDialog(workArea, 
                "Consulta realizada com sucesso!\n" +
                "Entregas encontradas: " + entregasEncontradas.size(), 
                "Resultado", JOptionPane.INFORMATION_MESSAGE);
            
            SystemLogger.ui("Consulta realizada - " + entregasEncontradas.size() + " entregas encontradas");
            
        } catch (Exception e) {
            SystemLogger.error("Erro ao consultar entregas", e);
            JOptionPane.showMessageDialog(workArea, "Erro ao consultar entregas: " + e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    /**
     * Gera rota de entrega
     */
    private void gerarRota() {
        if (entregasEncontradas.isEmpty()) {
            JOptionPane.showMessageDialog(workArea, "Nenhuma entrega para gerar rota!", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        // TODO: Implementar lógica de geração de rota
        JOptionPane.showMessageDialog(workArea, 
            "Gerando rota otimizada para " + entregasEncontradas.size() + " entregas...\n(Implementar geração de rota)", 
            "Gerar Rota", JOptionPane.INFORMATION_MESSAGE);
        
        SystemLogger.ui("Gerando rota para " + entregasEncontradas.size() + " entregas");
    }
    
    /**
     * Adiciona dados de exemplo à tabela
     */
    private void adicionarDadosExemplo() {
        // Limpar tabela atual
        modelTabela.setRowCount(0);
        entregasEncontradas.clear();
        
        // Dados de exemplo
        Object[][] dadosExemplo = {
            {"ENT-20260509-0001", "09/05/2026", "001", "João Silva", "(11) 9876-5432", "Rua A, 123 - Centro - São Paulo/SP", "Carlos Motorista", "Van ABC-1234", "ABC-1234", "Normal", "Em Rota", "10:30", "--:--", "Entrega urgente", "👁️"},
            {"ENT-20260508-0002", "08/05/2026", "002", "Maria Oliveira", "(11) 9123-4567", "Av B, 456 - Jardim - São Paulo/SP", "Pedro Transporte", "Moto DEF-5678", "DEF-5678", "Expressa", "Entregue", "14:20", "14:15", "Cliente presente", "👁️"},
            {"ENT-20260507-0003", "07/05/2026", "003", "Carlos Alberto", "(11) 8765-4321", "Rua C, 789 - São José - São Paulo/SP", "Ana Motorista", "Carro GHI-9012", "GHI-9012", "Agendada", "Pendente", "09:00", "--:--", "Entrega agendada para amanhã", "👁️"},
            {"ENT-20260506-0004", "06/05/2026", "004", "Fernanda Costa", "(11) 7654-3210", "Av D, 321 - Centro - São Paulo/SP", "Lucas Entregas", "Van JKL-3456", "JKL-3456", "Normal", "Cancelada", "08:30", "--:--", "Cliente cancelou o pedido", "👁️"},
            {"ENT-20260505-0005", "05/05/2026", "005", "Roberto Dias", "(11) 6543-2109", "Rua E, 654 - Industrial - São Paulo/SP", "Marcos Transporte", "Caminhão MNO-7890", "MNO-7890", "Normal", "Entregue", "16:45", "16:30", "Entrega com sucesso", "👁️"}
        };
        
        for (Object[] dados : dadosExemplo) {
            modelTabela.addRow(dados);
            
            // Adicionar à lista de entregas
            Entrega entrega = new Entrega();
            entrega.setNumero((String) dados[0]);
            entrega.setData((String) dados[1]);
            entrega.setNumeroVenda((String) dados[2]);
            entrega.setCliente((String) dados[3]);
            entrega.setEndereco((String) dados[5]); // Endereço completo
            entrega.setMotorista((String) dados[6]);
            entrega.setVeiculo((String) dados[7]);
            entrega.setPlaca((String) dados[8]); // Placa
            entrega.setTipo((String) dados[9]); // Tipo
            entrega.setStatus((String) dados[10]);
            entrega.setDataPrevista((String) dados[11]); // Data prevista
            entrega.setDataRealizada((String) dados[12]); // Data realizada
            entrega.setObservacoes((String) dados[13]); // Observações
            entregasEncontradas.add(entrega);
        }
    }
    
    /**
     * Limpa os campos do formulário
     */
    private void limparCampos() {
        txtNumeroVenda.setText("");
        txtCliente.setText("");
        txtCPF.setText("");
        txtEndereco.setText("");
        txtBairro.setText("");
        txtCidade.setText("");
        txtCEP.setText("");
        txtTelefone.setText("");
        txtMotorista.setText("");
        txtVeiculo.setText("");
        txtPlaca.setText("");
        comboTipoEntrega.setSelectedIndex(0);
        comboStatus.setSelectedIndex(0);
        txtObservacoes.setText("");
        
        SystemLogger.ui("Campos do formulário limpos");
    }
    
    /**
     * Ver detalhes da entrega selecionada
     */
    private void verDetalhesEntrega() {
        int linhaSelecionada = tabelaEntregas.getSelectedRow();
        if (linhaSelecionada >= 0) {
            Entrega entrega = entregasEncontradas.get(linhaSelecionada);
            JOptionPane.showMessageDialog(workArea, 
                "Detalhes da Entrega:\n\n" +
                "Número: " + entrega.getNumero() + "\n" +
                "Data: " + entrega.getData() + "\n" +
                "Venda: " + entrega.getNumeroVenda() + "\n" +
                "Cliente: " + entrega.getCliente() + "\n" +
                "CPF: " + entrega.getCpf() + "\n" +
                "Endereço: " + entrega.getEndereco() + "\n" +
                "Bairro: " + entrega.getBairro() + "\n" +
                "Cidade: " + entrega.getCidade() + "\n" +
                "CEP: " + entrega.getCep() + "\n" +
                "Telefone: " + entrega.getTelefone() + "\n" +
                "Motorista: " + entrega.getMotorista() + "\n" +
                "Veículo: " + entrega.getVeiculo() + "\n" +
                "Placa: " + entrega.getPlaca() + "\n" +
                "Tipo: " + entrega.getTipo() + "\n" +
                "Status: " + entrega.getStatus() + "\n" +
                "Observações: " + entrega.getObservacoes(), 
                "Detalhes da Entrega", JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(workArea, "Selecione uma entrega para ver os detalhes!", "Aviso", JOptionPane.WARNING_MESSAGE);
        }
    }
    
    /**
     * Inicia a entrega selecionada
     */
    private void iniciarEntrega() {
        int linhaSelecionada = tabelaEntregas.getSelectedRow();
        if (linhaSelecionada >= 0) {
            Entrega entrega = entregasEncontradas.get(linhaSelecionada);
            
            if (!"Pendente".equals(entrega.getStatus())) {
                JOptionPane.showMessageDialog(workArea, "Apenas entregas pendentes podem ser iniciadas!", "Aviso", JOptionPane.WARNING_MESSAGE);
                return;
            }
            
            int resultado = JOptionPane.showConfirmDialog(
                workArea,
                "Deseja iniciar a entrega " + entrega.getNumero() + "?\n" +
                "Cliente: " + entrega.getCliente() + "\n" +
                "Motorista: " + entrega.getMotorista(),
                "Iniciar Entrega",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE
            );
            
            if (resultado == JOptionPane.YES_OPTION) {
                // TODO: Implementar lógica de início no banco de dados
                entrega.setStatus("Em Rota");
                modelTabela.setValueAt("Em Rota", linhaSelecionada, 7);
                
                JOptionPane.showMessageDialog(workArea, "Entrega iniciada com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
                SystemLogger.ui("Entrega iniciada: " + entrega.getNumero());
            }
        } else {
            JOptionPane.showMessageDialog(workArea, "Selecione uma entrega para iniciar!", "Aviso", JOptionPane.WARNING_MESSAGE);
        }
    }
    
    /**
     * Conclui a entrega selecionada
     */
    private void concluirEntrega() {
        int linhaSelecionada = tabelaEntregas.getSelectedRow();
        if (linhaSelecionada >= 0) {
            Entrega entrega = entregasEncontradas.get(linhaSelecionada);
            
            if (!"Em Rota".equals(entrega.getStatus())) {
                JOptionPane.showMessageDialog(workArea, "Apenas entregas em rota podem ser concluídas!", "Aviso", JOptionPane.WARNING_MESSAGE);
                return;
            }
            
            int resultado = JOptionPane.showConfirmDialog(
                workArea,
                "Deseja concluir a entrega " + entrega.getNumero() + "?\n" +
                "Cliente: " + entrega.getCliente() + "\n" +
                "Motorista: " + entrega.getMotorista(),
                "Concluir Entrega",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE
            );
            
            if (resultado == JOptionPane.YES_OPTION) {
                // TODO: Implementar lógica de conclusão no banco de dados
                entrega.setStatus("Entregue");
                modelTabela.setValueAt("Entregue", linhaSelecionada, 7);
                
                JOptionPane.showMessageDialog(workArea, "Entrega concluída com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
                SystemLogger.ui("Entrega concluída: " + entrega.getNumero());
            }
        } else {
            JOptionPane.showMessageDialog(workArea, "Selecione uma entrega para concluir!", "Aviso", JOptionPane.WARNING_MESSAGE);
        }
    }
    
    /**
     * Cancela a entrega selecionada
     */
    private void cancelarEntrega() {
        int linhaSelecionada = tabelaEntregas.getSelectedRow();
        if (linhaSelecionada >= 0) {
            Entrega entrega = entregasEncontradas.get(linhaSelecionada);
            
            if ("Cancelada".equals(entrega.getStatus())) {
                JOptionPane.showMessageDialog(workArea, "Esta entrega já está cancelada!", "Aviso", JOptionPane.WARNING_MESSAGE);
                return;
            }
            
            if ("Entregue".equals(entrega.getStatus())) {
                JOptionPane.showMessageDialog(workArea, "Não é possível cancelar entregas já concluídas!", "Aviso", JOptionPane.WARNING_MESSAGE);
                return;
            }
            
            int resultado = JOptionPane.showConfirmDialog(
                workArea,
                "Deseja cancelar a entrega " + entrega.getNumero() + "?\n" +
                "Cliente: " + entrega.getCliente() + "\n" +
                "Motorista: " + entrega.getMotorista(),
                "Cancelar Entrega",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE
            );
            
            if (resultado == JOptionPane.YES_OPTION) {
                // TODO: Implementar lógica de cancelamento no banco de dados
                entrega.setStatus("Cancelada");
                modelTabela.setValueAt("Cancelada", linhaSelecionada, 7);
                
                JOptionPane.showMessageDialog(workArea, "Entrega cancelada com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
                SystemLogger.ui("Entrega cancelada: " + entrega.getNumero());
            }
        } else {
            JOptionPane.showMessageDialog(workArea, "Selecione uma entrega para cancelar!", "Aviso", JOptionPane.WARNING_MESSAGE);
        }
    }
    
    /**
     * Imprime a entrega selecionada
     */
    private void imprimirEntrega() {
        int linhaSelecionada = tabelaEntregas.getSelectedRow();
        if (linhaSelecionada >= 0) {
            Entrega entrega = entregasEncontradas.get(linhaSelecionada);
            JOptionPane.showMessageDialog(workArea, 
                "Imprimindo entrega " + entrega.getNumero() + "...\n(Implementar impressão)", 
                "Imprimir", JOptionPane.INFORMATION_MESSAGE);
            
            SystemLogger.ui("Imprimindo entrega: " + entrega.getNumero());
        } else {
            JOptionPane.showMessageDialog(workArea, "Selecione uma entrega para imprimir!", "Aviso", JOptionPane.WARNING_MESSAGE);
        }
    }
    
    /**
     * Cria painel de erro
     */
    private JPanel criarPainelErro() {
        JPanel painelErro = new JPanel(new BorderLayout());
        painelErro.setBackground(WHITE);
        
        JLabel erroLabel = new JLabel("❌ Erro ao carregar formulário");
        erroLabel.setFont(new Font("Arial", Font.BOLD, 16));
        erroLabel.setForeground(Color.RED);
        erroLabel.setHorizontalAlignment(JLabel.CENTER);
        
        painelErro.add(erroLabel, BorderLayout.CENTER);
        return painelErro;
    }
    
    /**
     * Classe interna para representar uma entrega
     */
    private static class Entrega {
        private String numero;
        private String data;
        private String numeroVenda;
        private String cliente;
        private String cpf;
        private String endereco;
        private String bairro;
        private String cidade;
        private String cep;
        private String telefone;
        private String motorista;
        private String veiculo;
        private String placa;
        private String tipo;
        private String status;
        private String dataPrevista;
        private String dataRealizada;
        private String observacoes;
        
        // Getters e Setters
        public String getNumero() { return numero; }
        public void setNumero(String numero) { this.numero = numero; }
        
        public String getData() { return data; }
        public void setData(String data) { this.data = data; }
        
        public String getNumeroVenda() { return numeroVenda; }
        public void setNumeroVenda(String numeroVenda) { this.numeroVenda = numeroVenda; }
        
        public String getCliente() { return cliente; }
        public void setCliente(String cliente) { this.cliente = cliente; }
        
        public String getCpf() { return cpf; }
        public void setCpf(String cpf) { this.cpf = cpf; }
        
        public String getEndereco() { return endereco; }
        public void setEndereco(String endereco) { this.endereco = endereco; }
        
        public String getBairro() { return bairro; }
        public void setBairro(String bairro) { this.bairro = bairro; }
        
        public String getCidade() { return cidade; }
        public void setCidade(String cidade) { this.cidade = cidade; }
        
        public String getCep() { return cep; }
        public void setCep(String cep) { this.cep = cep; }
        
        public String getDataPrevista() { return dataPrevista; }
        public void setDataPrevista(String dataPrevista) { this.dataPrevista = dataPrevista; }
        
        public String getDataRealizada() { return dataRealizada; }
        public void setDataRealizada(String dataRealizada) { this.dataRealizada = dataRealizada; }
        
        public String getTelefone() { return telefone; }
        public void setTelefone(String telefone) { this.telefone = telefone; }
        
        public String getMotorista() { return motorista; }
        public void setMotorista(String motorista) { this.motorista = motorista; }
        
        public String getVeiculo() { return veiculo; }
        public void setVeiculo(String veiculo) { this.veiculo = veiculo; }
        
        public String getPlaca() { return placa; }
        public void setPlaca(String placa) { this.placa = placa; }
        
        public String getTipo() { return tipo; }
        public void setTipo(String tipo) { this.tipo = tipo; }
        
        public String getStatus() { return status; }
        public void setStatus(String status) { this.status = status; }
        
        public String getObservacoes() { return observacoes; }
        public void setObservacoes(String observacoes) { this.observacoes = observacoes; }
    }
}
