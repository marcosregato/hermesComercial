package com.br.hermescomercial.pdv.controller;

import com.br.hermescomercial.pdv.model.VendaConsulta;
import com.br.hermescomercial.pdv.service.VendaConsultaService;
import com.br.hermescomercial.util.SystemLogger;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.List;

/**
 * Classe especializada para o formulário de Consultar Vendas
 * Estrutura: Header → Busca → Formulário → Tabela
 */
public class PDVFormularioConsultarVendas {
    
    private JPanel workArea;
    private String usuarioAtual;
    private String nomeUsuario;
    
    // Componentes do formulário
    private JPanel dateChooserDataInicio;
    private JPanel dateChooserDataFim;
    private JTextField txtCliente;
    private JTextField txtCPF;
    private JTextField txtVendedor;
    private JTextField txtNumeroVenda;
    private JTextField txtValorMinimo;
    private JTextField txtValorMaximo;
    private JComboBox<String> comboStatus;
    private JComboBox<String> comboFormaPagamento;
    private JComboBox<String> comboTipoVenda;
    private JCheckBox chkAtivas;
    private JCheckBox chkCanceladas;
    
    // Tabela de resultados
    private JTable tabelaVendas;
    private DefaultTableModel modelTabela;
    private List<VendaConsulta> vendasEncontradas;
    
    // Serviço de consulta
    private final VendaConsultaService vendaService = new VendaConsultaService();
    
    // Cores
    private static final Color WHITE = Color.WHITE;
    private static final Color ACCENT_COLOR = new Color(52, 152, 219);
    private static final Color SUCCESS_COLOR = new Color(39, 174, 96);
    private static final Color DANGER_COLOR = new Color(231, 76, 60);
    private static final Color GRAY = new Color(149, 165, 166);
    
    public PDVFormularioConsultarVendas(JPanel workArea, String usuarioAtual, String nomeUsuario) {
        this.workArea = workArea;
        this.usuarioAtual = usuarioAtual;
        this.nomeUsuario = nomeUsuario;
        this.vendasEncontradas = new ArrayList<>();
    }
    
    /**
     * Cria o formulário completo de Consultar Vendas
     */
    public JPanel criarFormularioConsultarVendas() {
        try {
            SystemLogger.ui("=== CRIANDO FORMULÁRIO CONSULTAR VENDAS ===");
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
            
            // Painel do formulário de filtros
            JPanel formularioPanel = criarPainelFormulario();
            painelCentral.add(formularioPanel, BorderLayout.CENTER);
            
            // Tabela de resultados
            JPanel tabelaPanel = criarPainelTabela();
            painelCentral.add(tabelaPanel, BorderLayout.SOUTH);
            
            painelPrincipal.add(painelCentral, BorderLayout.CENTER);
            
            SystemLogger.ui("Formulário Consultar Vendas criado com sucesso");
            return painelPrincipal;
            
        } catch (Exception e) {
            SystemLogger.error("Erro ao criar formulário Consultar Vendas", e);
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
        JLabel titleLabel = new JLabel("🔍 Consultar Vendas");
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
        txtBuscaRapida.setToolTipText("Digite número da venda, cliente ou CPF para busca rápida");
        
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
     * Cria o painel do formulário de filtros
     */
    private JPanel criarPainelFormulario() {
        JPanel formularioPanel = new JPanel(new GridBagLayout());
        formularioPanel.setBackground(WHITE);
        formularioPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
        // Seção Período
        JLabel periodoSectionLabel = new JLabel("📅 Período da Consulta");
        periodoSectionLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        periodoSectionLabel.setForeground(ACCENT_COLOR);
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 4;
        formularioPanel.add(periodoSectionLabel, gbc);
        
        gbc.gridy = 1; gbc.gridwidth = 1;
        JLabel lblDataInicio = new JLabel("Data Início:");
        lblDataInicio.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        formularioPanel.add(lblDataInicio, gbc);
        
        gbc.gridx = 1; gbc.weightx = 1.0;
        dateChooserDataInicio = criarDateChooser();
        formularioPanel.add(dateChooserDataInicio, gbc);
        
        gbc.gridx = 2; gbc.weightx = 0.0;
        JLabel lblDataFim = new JLabel("Data Fim:");
        lblDataFim.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        formularioPanel.add(lblDataFim, gbc);
        
        gbc.gridx = 3; gbc.weightx = 1.0;
        dateChooserDataFim = criarDateChooser();
        formularioPanel.add(dateChooserDataFim, gbc);
        
        // Seção Cliente
        gbc.gridy = 2; gbc.gridx = 0; gbc.gridwidth = 4; gbc.weightx = 0.0;
        JLabel clienteSectionLabel = new JLabel("👤 Dados do Cliente");
        clienteSectionLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        clienteSectionLabel.setForeground(ACCENT_COLOR);
        formularioPanel.add(clienteSectionLabel, gbc);
        
        gbc.gridy = 3; gbc.gridwidth = 1;
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
        
        // Seção Venda
        gbc.gridy = 4; gbc.gridx = 0; gbc.gridwidth = 4; gbc.weightx = 0.0;
        JLabel vendaSectionLabel = new JLabel("📋 Dados da Venda");
        vendaSectionLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        vendaSectionLabel.setForeground(ACCENT_COLOR);
        formularioPanel.add(vendaSectionLabel, gbc);
        
        gbc.gridy = 5; gbc.gridwidth = 1;
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
        formularioPanel.add(txtNumeroVenda, gbc);
        
        gbc.gridx = 2; gbc.weightx = 0.0;
        JLabel lblVendedor = new JLabel("Vendedor:");
        lblVendedor.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        formularioPanel.add(lblVendedor, gbc);
        
        gbc.gridx = 3; gbc.weightx = 1.0;
        txtVendedor = new JTextField();
        txtVendedor.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        txtVendedor.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(GRAY),
            BorderFactory.createEmptyBorder(5, 8, 5, 8)
        ));
        formularioPanel.add(txtVendedor, gbc);
        
        // Seção Filtros
        gbc.gridy = 6; gbc.gridx = 0; gbc.gridwidth = 4; gbc.weightx = 0.0;
        JLabel filtrosSectionLabel = new JLabel("⚙️ Filtros Adicionais");
        filtrosSectionLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        filtrosSectionLabel.setForeground(ACCENT_COLOR);
        formularioPanel.add(filtrosSectionLabel, gbc);
        
        gbc.gridy = 7; gbc.gridwidth = 1;
        JLabel lblStatus = new JLabel("Status:");
        lblStatus.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        formularioPanel.add(lblStatus, gbc);
        
        gbc.gridx = 1; gbc.weightx = 1.0;
        comboStatus = new JComboBox<>(new String[]{"Todos", "Ativa", "Finalizada", "Cancelada"});
        comboStatus.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        comboStatus.setBorder(BorderFactory.createLineBorder(GRAY));
        formularioPanel.add(comboStatus, gbc);
        
        gbc.gridx = 2; gbc.weightx = 0.0;
        JLabel lblFormaPagamento = new JLabel("Forma Pagto:");
        lblFormaPagamento.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        formularioPanel.add(lblFormaPagamento, gbc);
        
        gbc.gridx = 3; gbc.weightx = 1.0;
        comboFormaPagamento = new JComboBox<>(new String[]{"Todas", "Dinheiro", "Cartão", "PIX", "Boleto", "Outra"});
        comboFormaPagamento.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        comboFormaPagamento.setBorder(BorderFactory.createLineBorder(GRAY));
        formularioPanel.add(comboFormaPagamento, gbc);
        
        // Segunda linha de filtros
        gbc.gridy = 8; gbc.gridx = 0; gbc.gridwidth = 1; gbc.weightx = 0.0;
        JLabel lblValorMinimo = new JLabel("Valor Mínimo:");
        lblValorMinimo.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        formularioPanel.add(lblValorMinimo, gbc);
        
        gbc.gridx = 1; gbc.weightx = 1.0;
        txtValorMinimo = new JTextField();
        txtValorMinimo.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        txtValorMinimo.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(GRAY),
            BorderFactory.createEmptyBorder(5, 8, 5, 8)
        ));
        txtValorMinimo.setHorizontalAlignment(JTextField.RIGHT);
        txtValorMinimo.setToolTipText("Valor mínimo da venda (ex: 100,00)");
        formularioPanel.add(txtValorMinimo, gbc);
        
        gbc.gridx = 2; gbc.weightx = 0.0;
        JLabel lblValorMaximo = new JLabel("Valor Máximo:");
        lblValorMaximo.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        formularioPanel.add(lblValorMaximo, gbc);
        
        gbc.gridx = 3; gbc.weightx = 1.0;
        txtValorMaximo = new JTextField();
        txtValorMaximo.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        txtValorMaximo.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(GRAY),
            BorderFactory.createEmptyBorder(5, 8, 5, 8)
        ));
        txtValorMaximo.setHorizontalAlignment(JTextField.RIGHT);
        txtValorMaximo.setToolTipText("Valor máximo da venda (ex: 5000,00)");
        formularioPanel.add(txtValorMaximo, gbc);
        
        // Terceira linha de filtros
        gbc.gridy = 9; gbc.gridx = 0; gbc.gridwidth = 1; gbc.weightx = 0.0;
        JLabel lblTipoVenda = new JLabel("Tipo Venda:");
        lblTipoVenda.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        formularioPanel.add(lblTipoVenda, gbc);
        
        gbc.gridx = 1; gbc.weightx = 1.0;
        comboTipoVenda = new JComboBox<>(new String[]{"Todos", "Venda Balcão", "Delivery", "Televendas", "E-commerce", "Atacado"});
        comboTipoVenda.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        comboTipoVenda.setBorder(BorderFactory.createLineBorder(GRAY));
        formularioPanel.add(comboTipoVenda, gbc);
        
        // Checkboxes
        gbc.gridx = 2; gbc.gridwidth = 2; gbc.weightx = 0.0;
        chkAtivas = new JCheckBox("Incluir Ativas");
        chkAtivas.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        chkAtivas.setSelected(true);
        formularioPanel.add(chkAtivas, gbc);
        
        gbc.gridy = 10; gbc.gridx = 2; gbc.gridwidth = 2;
        chkCanceladas = new JCheckBox("Incluir Canceladas");
        chkCanceladas.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        chkCanceladas.setSelected(false);
        formularioPanel.add(chkCanceladas, gbc);
        
        // Botões de ação
        gbc.gridy = 11; gbc.gridx = 0; gbc.gridwidth = 4; gbc.weightx = 0.0;
        JPanel botoesPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        botoesPanel.setBackground(WHITE);
        
        JButton btnConsultar = new JButton("🔍 Consultar");
        btnConsultar.setBackground(SUCCESS_COLOR);
        btnConsultar.setForeground(WHITE);
        btnConsultar.setFocusPainted(false);
        btnConsultar.setBorderPainted(false);
        btnConsultar.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btnConsultar.addActionListener(e -> consultarVendas());
        
        JButton btnLimpar = new JButton("🗑️ Limpar");
        btnLimpar.setBackground(GRAY);
        btnLimpar.setForeground(WHITE);
        btnLimpar.setFocusPainted(false);
        btnLimpar.setBorderPainted(false);
        btnLimpar.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btnLimpar.addActionListener(e -> limparCampos());
        
        JButton btnExportar = new JButton("📊 Exportar");
        btnExportar.setBackground(ACCENT_COLOR);
        btnExportar.setForeground(WHITE);
        btnExportar.setFocusPainted(false);
        btnExportar.setBorderPainted(false);
        btnExportar.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btnExportar.addActionListener(e -> exportarResultados());
        
        botoesPanel.add(btnConsultar);
        botoesPanel.add(Box.createHorizontalStrut(10));
        botoesPanel.add(btnLimpar);
        botoesPanel.add(Box.createHorizontalStrut(10));
        botoesPanel.add(btnExportar);
        
        formularioPanel.add(botoesPanel, gbc);
        
        return formularioPanel;
    }
    
    /**
     * Cria um campo de data simplificado
     */
    private JPanel criarDateChooser() {
        // Como não temos JDateChooser disponível, vamos usar JTextField com formatação
        JTextField dateField = new JTextField();
        dateField.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        dateField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(GRAY),
            BorderFactory.createEmptyBorder(5, 8, 5, 8)
        ));
        dateField.setText(new SimpleDateFormat("dd/MM/yyyy").format(new Date()));
        dateField.setToolTipText("DD/MM/AAAA");
        
        // Criar um painel wrapper para simular JDateChooser
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
     * Cria o painel da tabela de resultados
     */
    private JPanel criarPainelTabela() {
        JPanel tabelaPanel = new JPanel(new BorderLayout());
        tabelaPanel.setBackground(WHITE);
        tabelaPanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 20, 20));
        
        JLabel tabelaLabel = new JLabel("📋 Resultados da Consulta");
        tabelaLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        tabelaLabel.setForeground(ACCENT_COLOR);
        
        // Modelo da tabela
        String[] colunas = {"Nº Venda", "Data", "Hora", "Cliente", "CPF", "Vendedor", "Tipo Venda", "Itens", "Valor Total", "Status", "Forma Pagto", "Ações"};
        modelTabela = new DefaultTableModel(colunas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 11; // Apenas a coluna de ações é editável
            }
        };
        
        tabelaVendas = new JTable(modelTabela);
        tabelaVendas.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        tabelaVendas.setRowHeight(25);
        tabelaVendas.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 12));
        tabelaVendas.getTableHeader().setBackground(ACCENT_COLOR);
        tabelaVendas.getTableHeader().setForeground(WHITE);
        
        // Scroll pane
        JScrollPane scrollPane = new JScrollPane(tabelaVendas);
        scrollPane.setPreferredSize(new Dimension(0, 300));
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
        btnDetalhes.addActionListener(e -> verDetalhesVenda());
        
        JButton btnImprimir = new JButton("🖨️ Imprimir");
        btnImprimir.setBackground(SUCCESS_COLOR);
        btnImprimir.setForeground(WHITE);
        btnImprimir.setFocusPainted(false);
        btnImprimir.setBorderPainted(false);
        btnImprimir.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btnImprimir.addActionListener(e -> imprimirVenda());
        
        JButton btnCancelar = new JButton("❌ Cancelar");
        btnCancelar.setBackground(DANGER_COLOR);
        btnCancelar.setForeground(WHITE);
        btnCancelar.setFocusPainted(false);
        btnCancelar.setBorderPainted(false);
        btnCancelar.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btnCancelar.addActionListener(e -> cancelarVenda());
        
        botoesTabelaPanel.add(btnDetalhes);
        botoesTabelaPanel.add(Box.createHorizontalStrut(10));
        botoesTabelaPanel.add(btnImprimir);
        botoesTabelaPanel.add(Box.createHorizontalStrut(10));
        botoesTabelaPanel.add(btnCancelar);
        
        // Label de resultados
        JLabel resultadosLabel = new JLabel("Nenhuma venda encontrada");
        resultadosLabel.setFont(new Font("Segoe UI", Font.ITALIC, 12));
        resultadosLabel.setForeground(GRAY);
        
        tabelaPanel.add(tabelaLabel, BorderLayout.NORTH);
        tabelaPanel.add(scrollPane, BorderLayout.CENTER);
        tabelaPanel.add(botoesTabelaPanel, BorderLayout.SOUTH);
        
        return tabelaPanel;
    }
    
    /**
     * Realiza busca rápida
     */
    private void realizarBuscaRapida(String termo) {
        try {
            SystemLogger.ui("=== BUSCA RÁPIDA ===");
            SystemLogger.ui("Termo de busca: " + termo);
            
            List<VendaConsulta> resultados = vendaService.buscarVendasRapida(termo, vendasEncontradas);
            
            // Atualizar tabela com resultados
            modelTabela.setRowCount(0);
            for (VendaConsulta venda : resultados) {
                Object[] row = {
                    venda.getNumero(),
                    venda.getData(),
                    venda.getCliente(),
                    venda.getCpf(),
                    venda.getVendedor(),
                    venda.getValorTotal(),
                    venda.getStatus(),
                    venda.getFormaPagamento(),
                    "👁️"
                };
                modelTabela.addRow(row);
            }
            
            JOptionPane.showMessageDialog(workArea, 
                "Busca realizada com sucesso!\n" +
                "Termo: " + termo + "\n" +
                "Resultados: " + resultados.size(), 
                "Resultado", JOptionPane.INFORMATION_MESSAGE);
            
            SystemLogger.ui("Busca rápida concluída - " + resultados.size() + " resultados");
            
        } catch (Exception e) {
            SystemLogger.error("Erro ao realizar busca rápida", e);
            JOptionPane.showMessageDialog(workArea, "Erro ao realizar busca: " + e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    /**
     * Adiciona dados de exemplo à tabela
     */
    private void adicionarDadosExemplo() {
        vendasEncontradas = vendaService.adicionarDadosExemplo(modelTabela);
    }
    
    /**
     * Consulta vendas com base nos filtros
     */
    private void consultarVendas() {
        try {
            SystemLogger.ui("=== CONSULTANDO VENDAS ===");
            
            // Coletar dados dos filtros
            String dataInicio = ((JTextField) dateChooserDataInicio.getComponent(0)).getText();
            String dataFim = ((JTextField) dateChooserDataFim.getComponent(0)).getText();
            
            // Por enquanto, vamos adicionar dados de exemplo
            adicionarDadosExemplo();
            
            JOptionPane.showMessageDialog(workArea, 
                "Consulta realizada com sucesso!\n" +
                "Período: " + dataInicio + " a " + dataFim + "\n" +
                "Vendas encontradas: " + vendasEncontradas.size(), 
                "Resultado", JOptionPane.INFORMATION_MESSAGE);
            
            SystemLogger.ui("Consulta concluída - " + vendasEncontradas.size() + " vendas");
            
        } catch (Exception e) {
            SystemLogger.error("Erro ao consultar vendas", e);
            JOptionPane.showMessageDialog(workArea, "Erro ao consultar vendas: " + e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    /**
     * Limpa os campos do formulário
     */
    private void limparCampos() {
        txtCliente.setText("");
        txtCPF.setText("");
        txtNumeroVenda.setText("");
        txtVendedor.setText("");
        txtValorMinimo.setText("");
        txtValorMaximo.setText("");
        comboStatus.setSelectedIndex(0);
        comboFormaPagamento.setSelectedIndex(0);
        comboTipoVenda.setSelectedIndex(0);
        chkAtivas.setSelected(true);
        chkCanceladas.setSelected(false);
        
        // Limpar tabela
        modelTabela.setRowCount(0);
        vendasEncontradas.clear();
        
        SystemLogger.ui("Campos do formulário limpos");
    }
    
    /**
     * Exporta resultados
     */
    private void exportarResultados() {
        if (vendasEncontradas.isEmpty()) {
            JOptionPane.showMessageDialog(workArea, "Nenhuma venda para exportar!", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        JOptionPane.showMessageDialog(workArea, 
            "Exportando " + vendasEncontradas.size() + " vendas...\n(Implementar exportação)", 
            "Exportar", JOptionPane.INFORMATION_MESSAGE);
        
        SystemLogger.ui("Exportando " + vendasEncontradas.size() + " vendas");
    }
    
    /**
     * Ver detalhes da venda selecionada
     */
    private void verDetalhesVenda() {
        int linhaSelecionada = tabelaVendas.getSelectedRow();
        if (linhaSelecionada >= 0) {
            VendaConsulta venda = vendasEncontradas.get(linhaSelecionada);
            JOptionPane.showMessageDialog(workArea, 
                "Detalhes da Venda:\n\n" +
                "Número: " + venda.getNumero() + "\n" +
                "Data: " + venda.getData() + "\n" +
                "Cliente: " + venda.getCliente() + "\n" +
                "CPF: " + venda.getCpf() + "\n" +
                "Vendedor: " + venda.getVendedor() + "\n" +
                "Valor Total: " + venda.getValorTotal() + "\n" +
                "Status: " + venda.getStatus() + "\n" +
                "Forma Pagamento: " + venda.getFormaPagamento(), 
                "Detalhes da Venda", JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(workArea, "Selecione uma venda para ver os detalhes!", "Aviso", JOptionPane.WARNING_MESSAGE);
        }
    }
    
    /**
     * Imprime a venda selecionada
     */
    private void imprimirVenda() {
        int linhaSelecionada = tabelaVendas.getSelectedRow();
        if (linhaSelecionada >= 0) {
            VendaConsulta venda = vendasEncontradas.get(linhaSelecionada);
            JOptionPane.showMessageDialog(workArea, 
                "Imprimindo venda " + venda.getNumero() + "...\n(Implementar impressão)", 
                "Imprimir", JOptionPane.INFORMATION_MESSAGE);
            
            SystemLogger.ui("Imprimindo venda: " + venda.getNumero());
        } else {
            JOptionPane.showMessageDialog(workArea, "Selecione uma venda para imprimir!", "Aviso", JOptionPane.WARNING_MESSAGE);
        }
    }
    
    /**
     * Cancela a venda selecionada
     */
    private void cancelarVenda() {
        int linhaSelecionada = tabelaVendas.getSelectedRow();
        if (linhaSelecionada >= 0) {
            VendaConsulta venda = vendasEncontradas.get(linhaSelecionada);
            
            if ("Cancelada".equals(venda.getStatus())) {
                JOptionPane.showMessageDialog(workArea, "Esta venda já está cancelada!", "Aviso", JOptionPane.WARNING_MESSAGE);
                return;
            }
            
            int resultado = JOptionPane.showConfirmDialog(
                workArea,
                "Deseja cancelar a venda " + venda.getNumero() + "?\n" +
                "Cliente: " + venda.getCliente() + "\n" +
                "Valor: " + venda.getValorTotal(),
                "Cancelar Venda",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE
            );
            
            if (resultado == JOptionPane.YES_OPTION) {
                venda.setStatus("Cancelada");
                modelTabela.setValueAt("Cancelada", linhaSelecionada, 6);
                
                JOptionPane.showMessageDialog(workArea, "Venda cancelada com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
                SystemLogger.ui("Venda cancelada: " + venda.getNumero());
            }
        } else {
            JOptionPane.showMessageDialog(workArea, "Selecione uma venda para cancelar!", "Aviso", JOptionPane.WARNING_MESSAGE);
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
}
