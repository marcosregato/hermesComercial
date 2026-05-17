package com.br.hermescomercial.pdv.controller;

import com.br.hermescomercial.util.SystemLogger;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Classe especializada para o formulário de Configurações de Pagamentos
 * Estrutura: Header → Busca → Formulário → Tabela
 */
public class PDVFormularioPagamentos {
    
    private JPanel workArea;
    private String usuarioAtual;
    private String nomeUsuario;
    
    // Componentes do formulário
    private JTextField txtBusca;
    private JTextField txtNomePagamento;
    private JComboBox<String> comboTipoPagamento;
    private JTextField txtTaxa;
    private JTextField txtTaxaFixa;
    private JTextField txtMinimo;
    private JTextField txtMaximo;
    private JComboBox<String> comboStatus;
    
    // Tabela de configurações de pagamento
    private JTable tabelaPagamentos;
    private DefaultTableModel modelTabela;
    private List<ConfiguracaoPagamento> pagamentosEncontrados;
    
    // Cores
    private static final Color WHITE = Color.WHITE;
    private static final Color ACCENT_COLOR = new Color(52, 152, 219);
    private static final Color SUCCESS_COLOR = new Color(39, 174, 96);
    private static final Color DANGER_COLOR = new Color(231, 76, 60);
    private static final Color GRAY = new Color(149, 165, 166);
    
    public PDVFormularioPagamentos(JPanel workArea, String usuarioAtual, String nomeUsuario) {
        this.workArea = workArea;
        this.usuarioAtual = usuarioAtual;
        this.nomeUsuario = nomeUsuario;
        this.pagamentosEncontrados = new ArrayList<>();
    }
    
    /**
     * Cria o formulário completo de Configurações de Pagamentos
     */
    public JPanel criarFormularioPagamentos() {
        try {
            SystemLogger.ui("=== CRIANDO FORMULÁRIO CONFIGURAÇÕES DE PAGAMENTOS ===");
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
            
            // Tabela de configurações de pagamento
            JPanel tabelaPanel = criarPainelTabela();
            painelCentral.add(tabelaPanel, BorderLayout.SOUTH);
            
            painelPrincipal.add(painelCentral, BorderLayout.CENTER);
            
            SystemLogger.ui("Formulário Configurações de Pagamentos criado com sucesso");
            return painelPrincipal;
            
        } catch (Exception e) {
            SystemLogger.error("Erro ao criar formulário Configurações de Pagamentos", e);
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
        JLabel titleLabel = new JLabel("💳 Pagamentos");
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
        txtBusca.setToolTipText("Digite nome ou tipo de pagamento");
        
        JButton btnBuscar = new JButton("🔍 Buscar");
        btnBuscar.setBackground(ACCENT_COLOR);
        btnBuscar.setForeground(WHITE);
        btnBuscar.setFocusPainted(false);
        btnBuscar.setBorderPainted(false);
        btnBuscar.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btnBuscar.addActionListener(e -> buscarPagamentos());
        
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
        
        // Seção Dados do Pagamento
        JLabel dadosSectionLabel = new JLabel("💳 Dados do Pagamento");
        dadosSectionLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        dadosSectionLabel.setForeground(ACCENT_COLOR);
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 4;
        formularioPanel.add(dadosSectionLabel, gbc);
        
        // Nome Pagamento
        gbc.gridy = 1; gbc.gridwidth = 1;
        JLabel lblNomePagamento = new JLabel("Nome do Pagamento:");
        lblNomePagamento.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        formularioPanel.add(lblNomePagamento, gbc);
        
        gbc.gridx = 1; gbc.weightx = 1.0;
        txtNomePagamento = new JTextField();
        txtNomePagamento.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        txtNomePagamento.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(GRAY),
            BorderFactory.createEmptyBorder(5, 8, 5, 8)
        ));
        formularioPanel.add(txtNomePagamento, gbc);
        
        // Tipo Pagamento
        gbc.gridx = 2; gbc.weightx = 0.0;
        JLabel lblTipoPagamento = new JLabel("Tipo:");
        lblTipoPagamento.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        formularioPanel.add(lblTipoPagamento, gbc);
        
        gbc.gridx = 3; gbc.weightx = 1.0;
        comboTipoPagamento = new JComboBox<>(new String[]{"Cartão Crédito", "Cartão Débito", "PIX", "Dinheiro", "Transferência", "Boleto"});
        comboTipoPagamento.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        comboTipoPagamento.setBorder(BorderFactory.createLineBorder(GRAY));
        formularioPanel.add(comboTipoPagamento, gbc);
        
        // Seção Taxas e Limites
        gbc.gridy = 2; gbc.gridx = 0; gbc.gridwidth = 4; gbc.weightx = 0.0;
        JLabel taxasSectionLabel = new JLabel("💰 Taxas e Limites");
        taxasSectionLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        taxasSectionLabel.setForeground(ACCENT_COLOR);
        formularioPanel.add(taxasSectionLabel, gbc);
        
        // Taxa Percentual
        gbc.gridy = 3; gbc.gridwidth = 1;
        JLabel lblTaxa = new JLabel("Taxa (%):");
        lblTaxa.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        formularioPanel.add(lblTaxa, gbc);
        
        gbc.gridx = 1; gbc.weightx = 1.0;
        txtTaxa = new JTextField();
        txtTaxa.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        txtTaxa.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(GRAY),
            BorderFactory.createEmptyBorder(5, 8, 5, 8)
        ));
        txtTaxa.setToolTipText("Ex: 2.5 para 2.5%");
        formularioPanel.add(txtTaxa, gbc);
        
        // Taxa Fixa
        gbc.gridx = 2; gbc.weightx = 0.0;
        JLabel lblTaxaFixa = new JLabel("Taxa Fixa (R$):");
        lblTaxaFixa.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        formularioPanel.add(lblTaxaFixa, gbc);
        
        gbc.gridx = 3; gbc.weightx = 1.0;
        txtTaxaFixa = new JTextField();
        txtTaxaFixa.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        txtTaxaFixa.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(GRAY),
            BorderFactory.createEmptyBorder(5, 8, 5, 8)
        ));
        txtTaxaFixa.setToolTipText("Ex: 0.50 para R$ 0,50");
        formularioPanel.add(txtTaxaFixa, gbc);
        
        // Valor Mínimo
        gbc.gridy = 4; gbc.gridx = 0;
        JLabel lblMinimo = new JLabel("Valor Mínimo (R$):");
        lblMinimo.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        formularioPanel.add(lblMinimo, gbc);
        
        gbc.gridx = 1; gbc.weightx = 1.0;
        txtMinimo = new JTextField();
        txtMinimo.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        txtMinimo.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(GRAY),
            BorderFactory.createEmptyBorder(5, 8, 5, 8)
        ));
        txtMinimo.setToolTipText("Valor mínimo para transação");
        formularioPanel.add(txtMinimo, gbc);
        
        // Valor Máximo
        gbc.gridx = 2; gbc.weightx = 0.0;
        JLabel lblMaximo = new JLabel("Valor Máximo (R$):");
        lblMaximo.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        formularioPanel.add(lblMaximo, gbc);
        
        gbc.gridx = 3; gbc.weightx = 1.0;
        txtMaximo = new JTextField();
        txtMaximo.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        txtMaximo.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(GRAY),
            BorderFactory.createEmptyBorder(5, 8, 5, 8)
        ));
        txtMaximo.setToolTipText("Valor máximo para transação");
        formularioPanel.add(txtMaximo, gbc);
        
        // Status
        gbc.gridy = 5; gbc.gridx = 0;
        JLabel lblStatus = new JLabel("Status:");
        lblStatus.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        formularioPanel.add(lblStatus, gbc);
        
        gbc.gridx = 1; gbc.weightx = 1.0;
        comboStatus = new JComboBox<>(new String[]{"Ativo", "Inativo", "Manutenção"});
        comboStatus.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        comboStatus.setBorder(BorderFactory.createLineBorder(GRAY));
        formularioPanel.add(comboStatus, gbc);
        
        // Botões de ação
        gbc.gridy = 6; gbc.gridx = 0; gbc.gridwidth = 4; gbc.weightx = 0.0;
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
        
        JButton btnTestar = new JButton("🧪 Testar Conexão");
        btnTestar.setBackground(ACCENT_COLOR);
        btnTestar.setForeground(WHITE);
        btnTestar.setFocusPainted(false);
        btnTestar.setBorderPainted(false);
        btnTestar.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btnTestar.addActionListener(e -> testarConexao());
        
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
        
        JLabel tabelaLabel = new JLabel("📋 Configurações de Pagamentos");
        tabelaLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        tabelaLabel.setForeground(ACCENT_COLOR);
        
        // Modelo da tabela
        String[] colunas = {"Nome", "Tipo", "Taxa", "Taxa Fixa", "Mínimo", "Máximo", "Status", "Ações"};
        modelTabela = new DefaultTableModel(colunas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 7; // Apenas a coluna de ações é editável
            }
        };
        
        tabelaPagamentos = new JTable(modelTabela);
        tabelaPagamentos.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        tabelaPagamentos.setRowHeight(25);
        tabelaPagamentos.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 12));
        tabelaPagamentos.getTableHeader().setBackground(ACCENT_COLOR);
        tabelaPagamentos.getTableHeader().setForeground(WHITE);
        
        // Scroll pane
        JScrollPane scrollPane = new JScrollPane(tabelaPagamentos);
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
        
        JButton btnExportar = new JButton("📄 Exportar");
        btnExportar.setBackground(SUCCESS_COLOR);
        btnExportar.setForeground(WHITE);
        btnExportar.setFocusPainted(false);
        btnExportar.setBorderPainted(false);
        btnExportar.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btnExportar.addActionListener(e -> exportarConfiguracoes());
        
        botoesTabelaPanel.add(btnEditar);
        botoesTabelaPanel.add(Box.createHorizontalStrut(10));
        botoesTabelaPanel.add(btnExportar);
        
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
        pagamentosEncontrados.clear();
        
        // Dados de exemplo
        Object[][] dadosExemplo = {
            {"Visa Crédito", "Cartão Crédito", "2.5%", "R$ 0,50", "R$ 5,00", "R$ 5.000,00", "Ativo", "✏️"},
            {"Master Débito", "Cartão Débito", "1.8%", "R$ 0,30", "R$ 1,00", "R$ 3.000,00", "Ativo", "✏️"},
            {"PIX Instantâneo", "PIX", "0.0%", "R$ 0,00", "R$ 0,01", "R$ 10.000,00", "Ativo", "✏️"},
            {"Dinheiro", "Dinheiro", "0.0%", "R$ 0,00", "R$ 0,01", "R$ 50.000,00", "Ativo", "✏️"},
            {"TED/DOC", "Transferência", "2.0%", "R$ 5,00", "R$ 10,00", "R$ 100.000,00", "Ativo", "✏️"},
            {"Boleto Bancário", "Boleto", "1.5%", "R$ 2,50", "R$ 20,00", "R$ 20.000,00", "Ativo", "✏️"}
        };
        
        for (Object[] dados : dadosExemplo) {
            modelTabela.addRow(dados);
            
            // Adicionar à lista de pagamentos
            ConfiguracaoPagamento pagamento = new ConfiguracaoPagamento();
            pagamento.setNome((String) dados[0]);
            pagamento.setTipo((String) dados[1]);
            pagamento.setTaxa((String) dados[2]);
            pagamento.setTaxaFixa((String) dados[3]);
            pagamento.setMinimo((String) dados[4]);
            pagamento.setMaximo((String) dados[5]);
            pagamento.setStatus((String) dados[6]);
            pagamentosEncontrados.add(pagamento);
        }
    }
    
    /**
     * Preenche dados iniciais
     */
    private void preencherDadosIniciais() {
        comboTipoPagamento.setSelectedIndex(0);
        comboStatus.setSelectedIndex(0);
        txtTaxa.setText("2.5");
        txtTaxaFixa.setText("0.50");
        txtMinimo.setText("5.00");
        txtMaximo.setText("5000.00");
    }
    
    /**
     * Busca configurações de pagamento
     */
    private void buscarPagamentos() {
        String termo = txtBusca.getText().trim();
        if (termo.isEmpty()) {
            JOptionPane.showMessageDialog(workArea, "Digite um termo para buscar!", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        JOptionPane.showMessageDialog(workArea, 
            "Busca realizada para: " + termo + "\n" +
            "Configurações encontradas: " + pagamentosEncontrados.size(), 
            "Resultado", JOptionPane.INFORMATION_MESSAGE);
        
        SystemLogger.ui("Busca realizada para: " + termo);
    }
    
    /**
     * Salva configuração de pagamento
     */
    private void salvarConfiguracao() {
        try {
            String nomePagamento = txtNomePagamento.getText().trim();
            String tipoPagamento = (String) comboTipoPagamento.getSelectedItem();
            String taxa = txtTaxa.getText().trim();
            String taxaFixa = txtTaxaFixa.getText().trim();
            
            if (nomePagamento.isEmpty()) {
                JOptionPane.showMessageDialog(workArea, "Digite o nome do pagamento!", "Aviso", JOptionPane.WARNING_MESSAGE);
                return;
            }
            
            if (taxa.isEmpty()) {
                JOptionPane.showMessageDialog(workArea, "Digite a taxa percentual!", "Aviso", JOptionPane.WARNING_MESSAGE);
                return;
            }
            
            JOptionPane.showMessageDialog(workArea, 
                "Configuração de pagamento salva com sucesso!\n\n" +
                "Nome: " + nomePagamento + "\n" +
                "Tipo: " + tipoPagamento + "\n" +
                "Taxa: " + taxa + "%\n" +
                "Taxa Fixa: R$ " + taxaFixa + "\n" +
                "Responsável: " + nomeUsuario, 
                "Sucesso", JOptionPane.INFORMATION_MESSAGE);
            
            SystemLogger.ui("Configuração de pagamento salva por: " + nomeUsuario);
            
        } catch (Exception e) {
            SystemLogger.error("Erro ao salvar configuração de pagamento", e);
            JOptionPane.showMessageDialog(workArea, "Erro ao salvar configuração: " + e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    /**
     * Limpa os campos do formulário
     */
    private void limparCampos() {
        txtBusca.setText("");
        txtNomePagamento.setText("");
        comboTipoPagamento.setSelectedIndex(0);
        txtTaxa.setText("");
        txtTaxaFixa.setText("");
        txtMinimo.setText("");
        txtMaximo.setText("");
        comboStatus.setSelectedIndex(0);
        
        preencherDadosIniciais();
    }
    
    /**
     * Edita configuração selecionada
     */
    private void editarConfiguracao() {
        int selectedRow = tabelaPagamentos.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(workArea, "Selecione uma configuração para editar!", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        ConfiguracaoPagamento pagamento = pagamentosEncontrados.get(selectedRow);
        
        // Preencher formulário com dados selecionados
        txtNomePagamento.setText(pagamento.getNome());
        comboTipoPagamento.setSelectedItem(pagamento.getTipo());
        txtTaxa.setText(pagamento.getTaxa().replace("%", ""));
        txtTaxaFixa.setText(pagamento.getTaxaFixa().replace("R$ ", "").replace(",", "."));
        txtMinimo.setText(pagamento.getMinimo().replace("R$ ", "").replace(",", "."));
        txtMaximo.setText(pagamento.getMaximo().replace("R$ ", "").replace(",", "."));
        comboStatus.setSelectedItem(pagamento.getStatus());
        
        JOptionPane.showMessageDialog(workArea, 
            "Configuração carregada para edição:\n\n" +
            "Nome: " + pagamento.getNome() + "\n" +
            "Tipo: " + pagamento.getTipo() + "\n" +
            "Taxa: " + pagamento.getTaxa(), 
            "Editar Configuração", JOptionPane.INFORMATION_MESSAGE);
        
        SystemLogger.ui("Configuração carregada para edição: " + pagamento.getNome());
    }
    
    /**
     * Testa conexão com gateway de pagamento
     */
    private void testarConexao() {
        String nomePagamento = txtNomePagamento.getText().trim();
        
        if (nomePagamento.isEmpty()) {
            JOptionPane.showMessageDialog(workArea, "Digite um nome para a configuração!", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        JOptionPane.showMessageDialog(workArea, 
            "Testando conexão com gateway de pagamento...\n" +
            "Configuração: " + nomePagamento + "\n" +
            "Verificando autenticação...\n" +
            "Validando taxas e limites...\n" +
            "(Implementar testes de conexão)", 
            "Testar Conexão", JOptionPane.INFORMATION_MESSAGE);
        
        SystemLogger.ui("Testando conexão para: " + nomePagamento);
    }
    
    /**
     * Exporta configurações
     */
    private void exportarConfiguracoes() {
        JOptionPane.showMessageDialog(workArea, 
            "Exportando " + pagamentosEncontrados.size() + " configurações...\n(Implementar exportação para JSON/XML)", 
            "Exportar Configurações", JOptionPane.INFORMATION_MESSAGE);
        
        SystemLogger.ui("Exportando " + pagamentosEncontrados.size() + " configurações de pagamento");
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
     * Classe interna para representar uma configuração de pagamento
     */
    private static class ConfiguracaoPagamento {
        private String nome;
        private String tipo;
        private String taxa;
        private String taxaFixa;
        private String minimo;
        private String maximo;
        private String status;
        
        // Getters e Setters
        public String getNome() { return nome; }
        public void setNome(String nome) { this.nome = nome; }
        
        public String getTipo() { return tipo; }
        public void setTipo(String tipo) { this.tipo = tipo; }
        
        public String getTaxa() { return taxa; }
        public void setTaxa(String taxa) { this.taxa = taxa; }
        
        public String getTaxaFixa() { return taxaFixa; }
        public void setTaxaFixa(String taxaFixa) { this.taxaFixa = taxaFixa; }
        
        public String getMinimo() { return minimo; }
        public void setMinimo(String minimo) { this.minimo = minimo; }
        
        public String getMaximo() { return maximo; }
        public void setMaximo(String maximo) { this.maximo = maximo; }
        
        public String getStatus() { return status; }
        public void setStatus(String status) { this.status = status; }
    }
}
