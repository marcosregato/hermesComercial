package com.br.hermescomercial.pdv.controller;

import com.br.hermescomercial.util.SystemLogger;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Classe especializada para o formulário de Contas a Pagar
 * Estrutura: Header → Busca → Formulário → Tabela
 */
public class PDVFormularioContasPagar {
    
    private JPanel workArea;
    private String usuarioAtual;
    private String nomeUsuario;
    
    // Componentes do formulário
    private JTextField txtBusca;
    private JTextField txtFornecedor;
    private JTextField txtDocumento;
    private JTextField txtValor;
    private JTextField txtDataVencimento;
    private JTextField txtDataEmissao;
    private JComboBox<String> comboStatus;
    private JComboBox<String> comboCategoria;
    private JTextArea txtObservacoes;
    
    // Tabela de contas a pagar
    private JTable tabelaContas;
    private DefaultTableModel modelTabela;
    private List<ContaPagar> contasEncontradas;
    
    // Cores
    private static final Color WHITE = Color.WHITE;
    private static final Color ACCENT_COLOR = new Color(52, 152, 219);
    private static final Color SUCCESS_COLOR = new Color(39, 174, 96);
    private static final Color DANGER_COLOR = new Color(231, 76, 60);
    private static final Color WARNING_COLOR = new Color(241, 196, 15);
    private static final Color GRAY = new Color(149, 165, 166);
    
    public PDVFormularioContasPagar(JPanel workArea, String usuarioAtual, String nomeUsuario) {
        this.workArea = workArea;
        this.usuarioAtual = usuarioAtual;
        this.nomeUsuario = nomeUsuario;
        this.contasEncontradas = new ArrayList<>();
    }
    
    /**
     * Cria o formulário completo de Contas a Pagar
     */
    public JPanel criarFormularioContasPagar() {
        try {
            SystemLogger.ui("=== CRIANDO FORMULÁRIO CONTAS A PAGAR ===");
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
            
            // Tabela de contas a pagar
            JPanel tabelaPanel = criarPainelTabela();
            painelCentral.add(tabelaPanel, BorderLayout.SOUTH);
            
            painelPrincipal.add(painelCentral, BorderLayout.CENTER);
            
            SystemLogger.ui("Formulário Contas a Pagar criado com sucesso");
            return painelPrincipal;
            
        } catch (Exception e) {
            SystemLogger.error("Erro ao criar formulário Contas a Pagar", e);
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
        JLabel titleLabel = new JLabel("💸 Contas a Pagar");
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
        txtBusca.setToolTipText("Digite fornecedor, documento ou valor");
        
        JButton btnBuscar = new JButton("🔍 Buscar");
        btnBuscar.setBackground(ACCENT_COLOR);
        btnBuscar.setForeground(WHITE);
        btnBuscar.setFocusPainted(false);
        btnBuscar.setBorderPainted(false);
        btnBuscar.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btnBuscar.addActionListener(e -> buscarContas());
        
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
        
        // Seção Dados da Conta
        JLabel dadosSectionLabel = new JLabel("📋 Dados da Conta");
        dadosSectionLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        dadosSectionLabel.setForeground(ACCENT_COLOR);
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 4;
        formularioPanel.add(dadosSectionLabel, gbc);
        
        // Fornecedor
        gbc.gridy = 1; gbc.gridwidth = 1;
        JLabel lblFornecedor = new JLabel("Fornecedor:");
        lblFornecedor.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        formularioPanel.add(lblFornecedor, gbc);
        
        gbc.gridx = 1; gbc.weightx = 1.0;
        txtFornecedor = new JTextField();
        txtFornecedor.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        txtFornecedor.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(GRAY),
            BorderFactory.createEmptyBorder(5, 8, 5, 8)
        ));
        formularioPanel.add(txtFornecedor, gbc);
        
        // Documento
        gbc.gridx = 2; gbc.weightx = 0.0;
        JLabel lblDocumento = new JLabel("Documento:");
        lblDocumento.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        formularioPanel.add(lblDocumento, gbc);
        
        gbc.gridx = 3; gbc.weightx = 1.0;
        txtDocumento = new JTextField();
        txtDocumento.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        txtDocumento.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(GRAY),
            BorderFactory.createEmptyBorder(5, 8, 5, 8)
        ));
        txtDocumento.setToolTipText("Número da fatura ou documento");
        formularioPanel.add(txtDocumento, gbc);
        
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
        txtValor.setToolTipText("R$ 0,00");
        formularioPanel.add(txtValor, gbc);
        
        // Status
        gbc.gridx = 2; gbc.weightx = 0.0;
        JLabel lblStatus = new JLabel("Status:");
        lblStatus.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        formularioPanel.add(lblStatus, gbc);
        
        gbc.gridx = 3; gbc.weightx = 1.0;
        comboStatus = new JComboBox<>(new String[]{"Pendente", "A Vencer", "Vencida", "Pago", "Cancelado"});
        comboStatus.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        comboStatus.setBorder(BorderFactory.createLineBorder(GRAY));
        formularioPanel.add(comboStatus, gbc);
        
        // Data Emissão
        gbc.gridy = 3; gbc.gridx = 0;
        JLabel lblDataEmissao = new JLabel("Data Emissão:");
        lblDataEmissao.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        formularioPanel.add(lblDataEmissao, gbc);
        
        gbc.gridx = 1; gbc.weightx = 1.0;
        txtDataEmissao = new JTextField();
        txtDataEmissao.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        txtDataEmissao.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(GRAY),
            BorderFactory.createEmptyBorder(5, 8, 5, 8)
        ));
        txtDataEmissao.setToolTipText("dd/mm/aaaa");
        formularioPanel.add(txtDataEmissao, gbc);
        
        // Data Vencimento
        gbc.gridx = 2; gbc.weightx = 0.0;
        JLabel lblDataVencimento = new JLabel("Data Vencimento:");
        lblDataVencimento.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        formularioPanel.add(lblDataVencimento, gbc);
        
        gbc.gridx = 3; gbc.weightx = 1.0;
        txtDataVencimento = new JTextField();
        txtDataVencimento.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        txtDataVencimento.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(GRAY),
            BorderFactory.createEmptyBorder(5, 8, 5, 8)
        ));
        txtDataVencimento.setToolTipText("dd/mm/aaaa");
        formularioPanel.add(txtDataVencimento, gbc);
        
        // Categoria
        gbc.gridy = 4; gbc.gridx = 0;
        JLabel lblCategoria = new JLabel("Categoria:");
        lblCategoria.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        formularioPanel.add(lblCategoria, gbc);
        
        gbc.gridx = 1; gbc.gridwidth = 3; gbc.weightx = 1.0;
        comboCategoria = new JComboBox<>(new String[]{"Aluguel", "Água", "Luz", "Telefone", "Internet", "Material", "Serviços", "Impostos", "Outros"});
        comboCategoria.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        comboCategoria.setBorder(BorderFactory.createLineBorder(GRAY));
        formularioPanel.add(comboCategoria, gbc);
        
        // Botões de ação
        gbc.gridy = 5; gbc.gridx = 0; gbc.gridwidth = 4; gbc.weightx = 0.0;
        JPanel botoesPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        botoesPanel.setBackground(WHITE);
        
        JButton btnAdicionar = new JButton("➕ Adicionar Conta");
        btnAdicionar.setBackground(SUCCESS_COLOR);
        btnAdicionar.setForeground(WHITE);
        btnAdicionar.setFocusPainted(false);
        btnAdicionar.setBorderPainted(false);
        btnAdicionar.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btnAdicionar.addActionListener(e -> adicionarConta());
        
        JButton btnPagar = new JButton("💳 Pagar Conta");
        btnPagar.setBackground(ACCENT_COLOR);
        btnPagar.setForeground(WHITE);
        btnPagar.setFocusPainted(false);
        btnPagar.setBorderPainted(false);
        btnPagar.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btnPagar.addActionListener(e -> pagarConta());
        
        JButton btnLimpar = new JButton("🧹 Limpar");
        btnLimpar.setBackground(GRAY);
        btnLimpar.setForeground(WHITE);
        btnLimpar.setFocusPainted(false);
        btnLimpar.setBorderPainted(false);
        btnLimpar.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btnLimpar.addActionListener(e -> limparCampos());
        
        botoesPanel.add(btnAdicionar);
        botoesPanel.add(Box.createHorizontalStrut(10));
        botoesPanel.add(btnPagar);
        botoesPanel.add(Box.createHorizontalStrut(10));
        botoesPanel.add(btnLimpar);
        
        formularioPanel.add(botoesPanel, gbc);
        
        return formularioPanel;
    }
    
    /**
     * Cria o painel da tabela
     */
    private JPanel criarPainelTabela() {
        JPanel tabelaPanel = new JPanel(new BorderLayout());
        tabelaPanel.setBackground(WHITE);
        tabelaPanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 20, 20));
        
        JLabel tabelaLabel = new JLabel("📋 Contas a Pagar");
        tabelaLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        tabelaLabel.setForeground(ACCENT_COLOR);
        
        // Modelo da tabela
        String[] colunas = {"Documento", "Fornecedor", "Valor", "Data Emissão", "Data Vencimento", "Status", "Categoria", "Ações"};
        modelTabela = new DefaultTableModel(colunas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 7; // Apenas a coluna de ações é editável
            }
        };
        
        tabelaContas = new JTable(modelTabela);
        tabelaContas.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        tabelaContas.setRowHeight(25);
        tabelaContas.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 12));
        tabelaContas.getTableHeader().setBackground(ACCENT_COLOR);
        tabelaContas.getTableHeader().setForeground(WHITE);
        
        // Scroll pane
        JScrollPane scrollPane = new JScrollPane(tabelaContas);
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
        btnEditar.addActionListener(e -> editarConta());
        
        JButton btnRelatorio = new JButton("📊 Relatório");
        btnRelatorio.setBackground(WARNING_COLOR);
        btnRelatorio.setForeground(WHITE);
        btnRelatorio.setFocusPainted(false);
        btnRelatorio.setBorderPainted(false);
        btnRelatorio.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btnRelatorio.addActionListener(e -> gerarRelatorio());
        
        botoesTabelaPanel.add(btnEditar);
        botoesTabelaPanel.add(Box.createHorizontalStrut(10));
        botoesTabelaPanel.add(btnRelatorio);
        
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
        contasEncontradas.clear();
        
        // Dados de exemplo
        Object[][] dadosExemplo = {
            {"FAT-001", "Imobiliária ABC", "R$ 1.200,00", "01/05/2026", "05/05/2026", "Pago", "Aluguel", "✏️"},
            {"FAT-002", "Companhia Luz", "R$ 450,00", "03/05/2026", "15/05/2026", "A Vencer", "Luz", "✏️"},
            {"FAT-003", "Saneamento", "R$ 120,00", "25/04/2026", "10/05/2026", "Vencida", "Água", "✏️"},
            {"FAT-004", "Telefonica", "R$ 180,00", "08/05/2026", "20/05/2026", "Pendente", "Telefone", "✏️"},
            {"FAT-005", "Internet Plus", "R$ 150,00", "02/05/2026", "12/05/2026", "Pago", "Internet", "✏️"}
        };
        
        for (Object[] dados : dadosExemplo) {
            modelTabela.addRow(dados);
            
            // Adicionar à lista de contas
            ContaPagar conta = new ContaPagar();
            conta.setDocumento((String) dados[0]);
            conta.setFornecedor((String) dados[1]);
            conta.setValor((String) dados[2]);
            conta.setDataEmissao((String) dados[3]);
            conta.setDataVencimento((String) dados[4]);
            conta.setStatus((String) dados[5]);
            conta.setCategoria((String) dados[6]);
            contasEncontradas.add(conta);
        }
    }
    
    /**
     * Busca contas
     */
    private void buscarContas() {
        String termo = txtBusca.getText().trim();
        if (termo.isEmpty()) {
            JOptionPane.showMessageDialog(workArea, "Digite um termo para buscar!", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        // TODO: Implementar lógica de busca no banco de dados
        JOptionPane.showMessageDialog(workArea, 
            "Busca realizada para: " + termo + "\n" +
            "Contas encontradas: " + contasEncontradas.size(), 
            "Resultado", JOptionPane.INFORMATION_MESSAGE);
        
        SystemLogger.ui("Busca realizada para: " + termo);
    }
    
    /**
     * Adiciona nova conta
     */
    private void adicionarConta() {
        String fornecedor = txtFornecedor.getText().trim();
        String documento = txtDocumento.getText().trim();
        String valor = txtValor.getText().trim();
        String dataEmissao = txtDataEmissao.getText().trim();
        String dataVencimento = txtDataVencimento.getText().trim();
        
        // Validação do Fornecedor (Obrigatório *)
        if (fornecedor.isEmpty()) {
            JOptionPane.showMessageDialog(workArea, "⚠️ O Fornecedor é obrigatório!\n\nPor favor, informe o nome do fornecedor.", "Validação - Campo Obrigatório", JOptionPane.WARNING_MESSAGE);
            txtFornecedor.requestFocus();
            txtFornecedor.setBorder(BorderFactory.createLineBorder(Color.RED, 2));
            return;
        }
        
        // Validação do Documento (Obrigatório *)
        if (documento.isEmpty()) {
            JOptionPane.showMessageDialog(workArea, "⚠️ O Documento é obrigatório!\n\nPor favor, informe o número do documento.", "Validação - Campo Obrigatório", JOptionPane.WARNING_MESSAGE);
            txtDocumento.requestFocus();
            txtDocumento.setBorder(BorderFactory.createLineBorder(Color.RED, 2));
            return;
        }
        
        // Validação do Valor (Obrigatório *)
        if (valor.isEmpty()) {
            JOptionPane.showMessageDialog(workArea, "⚠️ O Valor é obrigatório!\n\nPor favor, informe o valor da conta.", "Validação - Campo Obrigatório", JOptionPane.WARNING_MESSAGE);
            txtValor.requestFocus();
            txtValor.setBorder(BorderFactory.createLineBorder(Color.RED, 2));
            return;
        }
        
        // Validação do formato do Valor
        try {
            double valorNum = Double.parseDouble(valor.replace(",", "."));
            if (valorNum <= 0) {
                JOptionPane.showMessageDialog(workArea, "⚠️ O Valor deve ser maior que zero!\n\nPor favor, informe um valor válido.", "Validação - Campo Obrigatório", JOptionPane.WARNING_MESSAGE);
                txtValor.requestFocus();
                txtValor.setBorder(BorderFactory.createLineBorder(Color.RED, 2));
                return;
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(workArea, "⚠️ Valor inválido!\n\nPor favor, informe um valor numérico válido (ex: 100,50).", "Validação - Campo Obrigatório", JOptionPane.WARNING_MESSAGE);
            txtValor.requestFocus();
            txtValor.setBorder(BorderFactory.createLineBorder(Color.RED, 2));
            return;
        }
        
        // Validação da Data de Vencimento (Obrigatório *)
        if (dataVencimento.isEmpty()) {
            JOptionPane.showMessageDialog(workArea, "⚠️ A Data de Vencimento é obrigatória!\n\nPor favor, informe a data de vencimento.", "Validação - Campo Obrigatório", JOptionPane.WARNING_MESSAGE);
            txtDataVencimento.requestFocus();
            txtDataVencimento.setBorder(BorderFactory.createLineBorder(Color.RED, 2));
            return;
        }
        
        // Resetar bordas dos campos válidos
        txtFornecedor.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        txtDocumento.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        txtValor.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        txtDataVencimento.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        
        try {
            // TODO: Implementar lógica de salvamento no banco de dados
            JOptionPane.showMessageDialog(workArea, 
                "Conta adicionada com sucesso!\n\n" +
                "Fornecedor: " + fornecedor + "\n" +
                "Documento: " + documento + "\n" +
                "Valor: " + valor + "\n" +
                "Data Vencimento: " + dataVencimento + "\n" +
                "Responsável: " + nomeUsuario, 
                "Sucesso", JOptionPane.INFORMATION_MESSAGE);
            
            SystemLogger.ui("Conta adicionada: " + documento + " - " + fornecedor);
            limparCampos();
            adicionarDadosExemplo(); // Atualizar tabela
            
        } catch (Exception e) {
            SystemLogger.error("Erro ao adicionar conta", e);
            JOptionPane.showMessageDialog(workArea, "Erro ao adicionar conta: " + e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    /**
     * Paga conta selecionada
     */
    private void pagarConta() {
        int selectedRow = tabelaContas.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(workArea, "Selecione uma conta para pagar!", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        ContaPagar conta = contasEncontradas.get(selectedRow);
        
        int confirm = JOptionPane.showConfirmDialog(workArea, 
            "Confirmar pagamento da conta:\n\n" +
            "Documento: " + conta.getDocumento() + "\n" +
            "Fornecedor: " + conta.getFornecedor() + "\n" +
            "Valor: " + conta.getValor() + "\n" +
            "Data Vencimento: " + conta.getDataVencimento(), 
            "Confirmar Pagamento", JOptionPane.YES_NO_OPTION);
        
        if (confirm == JOptionPane.YES_OPTION) {
            // TODO: Implementar lógica de pagamento no banco de dados
            JOptionPane.showMessageDialog(workArea, 
                "Conta paga com sucesso!\n" +
                "Documento: " + conta.getDocumento(), 
                "Sucesso", JOptionPane.INFORMATION_MESSAGE);
            
            SystemLogger.ui("Conta paga: " + conta.getDocumento());
            adicionarDadosExemplo(); // Atualizar tabela
        }
    }
    
    /**
     * Edita conta selecionada
     */
    private void editarConta() {
        int selectedRow = tabelaContas.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(workArea, "Selecione uma conta para editar!", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        ContaPagar conta = contasEncontradas.get(selectedRow);
        
        // Preencher formulário com dados da conta
        txtFornecedor.setText(conta.getFornecedor());
        txtDocumento.setText(conta.getDocumento());
        txtValor.setText(conta.getValor());
        txtDataEmissao.setText(conta.getDataEmissao());
        txtDataVencimento.setText(conta.getDataVencimento());
        comboStatus.setSelectedItem(conta.getStatus());
        comboCategoria.setSelectedItem(conta.getCategoria());
        
        JOptionPane.showMessageDialog(workArea, 
            "Conta carregada para edição: " + conta.getDocumento(), 
            "Editar Conta", JOptionPane.INFORMATION_MESSAGE);
        
        SystemLogger.ui("Conta carregada para edição: " + conta.getDocumento());
    }
    
    /**
     * Limpa os campos do formulário
     */
    private void limparCampos() {
        txtBusca.setText("");
        txtFornecedor.setText("");
        txtDocumento.setText("");
        txtValor.setText("");
        txtDataEmissao.setText("");
        txtDataVencimento.setText("");
        comboStatus.setSelectedIndex(0);
        comboCategoria.setSelectedIndex(0);
    }
    
    /**
     * Gera relatório
     */
    private void gerarRelatorio() {
        JOptionPane.showMessageDialog(workArea, 
            "Gerando relatório de " + contasEncontradas.size() + " contas...\n(Implementar geração de relatório PDF/Excel)", 
            "Gerar Relatório", JOptionPane.INFORMATION_MESSAGE);
        
        SystemLogger.ui("Gerando relatório de " + contasEncontradas.size() + " contas");
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
     * Classe interna para representar uma conta a pagar
     */
    private static class ContaPagar {
        private String documento;
        private String fornecedor;
        private String valor;
        private String dataEmissao;
        private String dataVencimento;
        private String status;
        private String categoria;
        
        // Getters e Setters
        public String getDocumento() { return documento; }
        public void setDocumento(String documento) { this.documento = documento; }
        
        public String getFornecedor() { return fornecedor; }
        public void setFornecedor(String fornecedor) { this.fornecedor = fornecedor; }
        
        public String getValor() { return valor; }
        public void setValor(String valor) { this.valor = valor; }
        
        public String getDataEmissao() { return dataEmissao; }
        public void setDataEmissao(String dataEmissao) { this.dataEmissao = dataEmissao; }
        
        public String getDataVencimento() { return dataVencimento; }
        public void setDataVencimento(String dataVencimento) { this.dataVencimento = dataVencimento; }
        
        public String getStatus() { return status; }
        public void setStatus(String status) { this.status = status; }
        
        public String getCategoria() { return categoria; }
        public void setCategoria(String categoria) { this.categoria = categoria; }
    }
}
