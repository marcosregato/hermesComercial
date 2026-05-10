package com.br.hermescomercial.pdv.controller;

import com.br.hermescomercial.util.SystemLogger;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Classe especializada para o formulário de Histórico de Compras
 * Estrutura: Header → Busca → Formulário → Tabela
 */
public class PDVFormularioHistoricoCompras {
    
    private JPanel workArea;
    private String usuarioAtual;
    private String nomeUsuario;
    
    // Componentes do formulário
    private JTextField txtBusca;
    private JTextField txtCliente;
    private JTextField txtCPF;
    private JComboBox<String> comboPeriodo;
    private JTextField txtDataInicio;
    private JTextField txtDataFim;
    private JComboBox<String> comboStatus;
    private JComboBox<String> comboTipoVenda;
    private JTextArea txtObservacoes;
    
    // Tabela de histórico
    private JTable tabelaHistorico;
    private DefaultTableModel modelTabela;
    private List<HistoricoCompra> historicosEncontrados;
    
    // Cores
    private static final Color WHITE = Color.WHITE;
    private static final Color ACCENT_COLOR = new Color(52, 152, 219);
    private static final Color SUCCESS_COLOR = new Color(39, 174, 96);
    private static final Color DANGER_COLOR = new Color(231, 76, 60);
    private static final Color WARNING_COLOR = new Color(241, 196, 15);
    private static final Color GRAY = new Color(149, 165, 166);
    
    public PDVFormularioHistoricoCompras(JPanel workArea, String usuarioAtual, String nomeUsuario) {
        this.workArea = workArea;
        this.usuarioAtual = usuarioAtual;
        this.nomeUsuario = nomeUsuario;
        this.historicosEncontrados = new ArrayList<>();
    }
    
    /**
     * Cria o formulário completo de Histórico de Compras
     */
    public JPanel criarFormularioHistoricoCompras() {
        try {
            SystemLogger.ui("=== CRIANDO FORMULÁRIO HISTÓRICO DE COMPRAS ===");
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
            
            // Tabela de histórico
            JPanel tabelaPanel = criarPainelTabela();
            painelCentral.add(tabelaPanel, BorderLayout.SOUTH);
            
            painelPrincipal.add(painelCentral, BorderLayout.CENTER);
            
            SystemLogger.ui("Formulário Histórico de Compras criado com sucesso");
            return painelPrincipal;
            
        } catch (Exception e) {
            SystemLogger.error("Erro ao criar formulário Histórico de Compras", e);
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
        JLabel titleLabel = new JLabel("📊 Histórico de Compras");
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
        txtBusca.setToolTipText("Digite código, nome do cliente ou CPF");
        
        JButton btnBuscar = new JButton("🔍 Buscar");
        btnBuscar.setBackground(ACCENT_COLOR);
        btnBuscar.setForeground(WHITE);
        btnBuscar.setFocusPainted(false);
        btnBuscar.setBorderPainted(false);
        btnBuscar.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btnBuscar.addActionListener(e -> buscarHistorico());
        
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
        
        // Seção Filtros de Busca
        JLabel filtrosSectionLabel = new JLabel("🔍 Filtros de Busca");
        filtrosSectionLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        filtrosSectionLabel.setForeground(ACCENT_COLOR);
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 4;
        formularioPanel.add(filtrosSectionLabel, gbc);
        
        // Cliente
        gbc.gridy = 1; gbc.gridwidth = 1;
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
        
        // CPF
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
        
        // Período
        gbc.gridy = 2; gbc.gridx = 0;
        JLabel lblPeriodo = new JLabel("Período:");
        lblPeriodo.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        formularioPanel.add(lblPeriodo, gbc);
        
        gbc.gridx = 1; gbc.weightx = 1.0;
        comboPeriodo = new JComboBox<>(new String[]{"Todos", "Hoje", "Esta Semana", "Este Mês", "Personalizado"});
        comboPeriodo.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        comboPeriodo.setBorder(BorderFactory.createLineBorder(GRAY));
        comboPeriodo.addActionListener(e -> atualizarCamposPeriodo());
        formularioPanel.add(comboPeriodo, gbc);
        
        // Data Início
        gbc.gridx = 2; gbc.weightx = 0.0;
        JLabel lblDataInicio = new JLabel("Data Início:");
        lblDataInicio.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        formularioPanel.add(lblDataInicio, gbc);
        
        gbc.gridx = 3; gbc.weightx = 1.0;
        txtDataInicio = new JTextField();
        txtDataInicio.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        txtDataInicio.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(GRAY),
            BorderFactory.createEmptyBorder(5, 8, 5, 8)
        ));
        txtDataInicio.setToolTipText("dd/mm/aaaa");
        formularioPanel.add(txtDataInicio, gbc);
        
        // Data Fim
        gbc.gridy = 3; gbc.gridx = 0;
        JLabel lblDataFim = new JLabel("Data Fim:");
        lblDataFim.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        formularioPanel.add(lblDataFim, gbc);
        
        gbc.gridx = 1; gbc.weightx = 1.0;
        txtDataFim = new JTextField();
        txtDataFim.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        txtDataFim.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(GRAY),
            BorderFactory.createEmptyBorder(5, 8, 5, 8)
        ));
        txtDataFim.setToolTipText("dd/mm/aaaa");
        formularioPanel.add(txtDataFim, gbc);
        
        // Status
        gbc.gridx = 2; gbc.weightx = 0.0;
        JLabel lblStatus = new JLabel("Status:");
        lblStatus.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        formularioPanel.add(lblStatus, gbc);
        
        gbc.gridx = 3; gbc.weightx = 1.0;
        comboStatus = new JComboBox<>(new String[]{"Todos", "Concluída", "Cancelada", "Pendente"});
        comboStatus.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        comboStatus.setBorder(BorderFactory.createLineBorder(GRAY));
        formularioPanel.add(comboStatus, gbc);
        
        // Tipo Venda
        gbc.gridy = 4; gbc.gridx = 0;
        JLabel lblTipoVenda = new JLabel("Tipo Venda:");
        lblTipoVenda.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        formularioPanel.add(lblTipoVenda, gbc);
        
        gbc.gridx = 1; gbc.weightx = 1.0;
        comboTipoVenda = new JComboBox<>(new String[]{"Todos", "Venda", "Orçamento", "Devolução"});
        comboTipoVenda.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        comboTipoVenda.setBorder(BorderFactory.createLineBorder(GRAY));
        formularioPanel.add(comboTipoVenda, gbc);
        
        // Botões de ação
        gbc.gridy = 5; gbc.gridx = 0; gbc.gridwidth = 4; gbc.weightx = 0.0;
        JPanel botoesPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        botoesPanel.setBackground(WHITE);
        
        JButton btnFiltrar = new JButton("🔍 Filtrar");
        btnFiltrar.setBackground(ACCENT_COLOR);
        btnFiltrar.setForeground(WHITE);
        btnFiltrar.setFocusPainted(false);
        btnFiltrar.setBorderPainted(false);
        btnFiltrar.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btnFiltrar.addActionListener(e -> filtrarHistorico());
        
        JButton btnLimpar = new JButton("🧹 Limpar");
        btnLimpar.setBackground(GRAY);
        btnLimpar.setForeground(WHITE);
        btnLimpar.setFocusPainted(false);
        btnLimpar.setBorderPainted(false);
        btnLimpar.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btnLimpar.addActionListener(e -> limparCampos());
        
        JButton btnExportar = new JButton("📄 Exportar");
        btnExportar.setBackground(SUCCESS_COLOR);
        btnExportar.setForeground(WHITE);
        btnExportar.setFocusPainted(false);
        btnExportar.setBorderPainted(false);
        btnExportar.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btnExportar.addActionListener(e -> exportarDados());
        
        botoesPanel.add(btnFiltrar);
        botoesPanel.add(Box.createHorizontalStrut(10));
        botoesPanel.add(btnLimpar);
        botoesPanel.add(Box.createHorizontalStrut(10));
        botoesPanel.add(btnExportar);
        
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
        
        JLabel tabelaLabel = new JLabel("📋 Histórico de Compras");
        tabelaLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        tabelaLabel.setForeground(ACCENT_COLOR);
        
        // Modelo da tabela
        String[] colunas = {"Código", "Data", "Cliente", "CPF", "Tipo", "Status", "Total", "Forma Pagamento", "Ações"};
        modelTabela = new DefaultTableModel(colunas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 8; // Apenas a coluna de ações é editável
            }
        };
        
        tabelaHistorico = new JTable(modelTabela);
        tabelaHistorico.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        tabelaHistorico.setRowHeight(25);
        tabelaHistorico.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 12));
        tabelaHistorico.getTableHeader().setBackground(ACCENT_COLOR);
        tabelaHistorico.getTableHeader().setForeground(WHITE);
        
        // Scroll pane
        JScrollPane scrollPane = new JScrollPane(tabelaHistorico);
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
        btnDetalhes.addActionListener(e -> verDetalhes());
        
        JButton btnRelatorio = new JButton("📊 Relatório");
        btnRelatorio.setBackground(WARNING_COLOR);
        btnRelatorio.setForeground(WHITE);
        btnRelatorio.setFocusPainted(false);
        btnRelatorio.setBorderPainted(false);
        btnRelatorio.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btnRelatorio.addActionListener(e -> gerarRelatorio());
        
        botoesTabelaPanel.add(btnDetalhes);
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
        historicosEncontrados.clear();
        
        // Dados de exemplo
        Object[][] dadosExemplo = {
            {"VND-001", "10/05/2026", "João Silva", "123.456.789-00", "Venda", "Concluída", "R$ 1.250,00", "Cartão Crédito", "👁️"},
            {"VND-002", "09/05/2026", "Maria Oliveira", "987.654.321-00", "Venda", "Concluída", "R$ 890,50", "Dinheiro", "👁️"},
            {"VND-003", "08/05/2026", "Carlos Alberto", "456.789.123-00", "Orçamento", "Pendente", "R$ 2.340,00", "Pix", "👁️"},
            {"VND-004", "07/05/2026", "Fernanda Costa", "789.123.456-00", "Venda", "Cancelada", "R$ 567,80", "Cartão Débito", "👁️"},
            {"VND-005", "06/05/2026", "Roberto Dias", "321.654.987-00", "Devolução", "Concluída", "R$ -234,00", "Transferência", "👁️"}
        };
        
        for (Object[] dados : dadosExemplo) {
            modelTabela.addRow(dados);
            
            // Adicionar à lista de históricos
            HistoricoCompra historico = new HistoricoCompra();
            historico.setCodigo((String) dados[0]);
            historico.setData((String) dados[1]);
            historico.setCliente((String) dados[2]);
            historico.setCpf((String) dados[3]);
            historico.setTipo((String) dados[4]);
            historico.setStatus((String) dados[5]);
            historico.setTotal((String) dados[6]);
            historico.setFormaPagamento((String) dados[7]);
            historicosEncontrados.add(historico);
        }
    }
    
    /**
     * Atualiza campos de período
     */
    private void atualizarCamposPeriodo() {
        String periodo = (String) comboPeriodo.getSelectedItem();
        
        if ("Personalizado".equals(periodo)) {
            txtDataInicio.setEditable(true);
            txtDataFim.setEditable(true);
        } else {
            txtDataInicio.setEditable(false);
            txtDataFim.setEditable(false);
            
            // Preencher datas automáticas
            java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("dd/MM/yyyy");
            String hoje = sdf.format(new java.util.Date());
            
            switch (periodo) {
                case "Hoje":
                    txtDataInicio.setText(hoje);
                    txtDataFim.setText(hoje);
                    break;
                case "Esta Semana":
                    // Lógica para calcular início e fim da semana
                    txtDataInicio.setText("06/05/2026");
                    txtDataFim.setText("12/05/2026");
                    break;
                case "Este Mês":
                    txtDataInicio.setText("01/05/2026");
                    txtDataFim.setText("31/05/2026");
                    break;
                default:
                    txtDataInicio.setText("");
                    txtDataFim.setText("");
            }
        }
    }
    
    /**
     * Busca histórico
     */
    private void buscarHistorico() {
        String termo = txtBusca.getText().trim();
        if (termo.isEmpty()) {
            JOptionPane.showMessageDialog(workArea, "Digite um termo para buscar!", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        // TODO: Implementar lógica de busca no banco de dados
        JOptionPane.showMessageDialog(workArea, 
            "Busca realizada para: " + termo + "\n" +
            "Históricos encontrados: " + historicosEncontrados.size(), 
            "Resultado", JOptionPane.INFORMATION_MESSAGE);
        
        SystemLogger.ui("Busca realizada para: " + termo);
    }
    
    /**
     * Filtra histórico
     */
    private void filtrarHistorico() {
        try {
            // TODO: Implementar lógica de filtro no banco de dados
            JOptionPane.showMessageDialog(workArea, 
                "Filtro aplicado com sucesso!\n" +
                "Históricos encontrados: " + historicosEncontrados.size(), 
                "Resultado", JOptionPane.INFORMATION_MESSAGE);
            
            SystemLogger.ui("Filtro aplicado - " + historicosEncontrados.size() + " históricos encontrados");
            
        } catch (Exception e) {
            SystemLogger.error("Erro ao filtrar histórico", e);
            JOptionPane.showMessageDialog(workArea, "Erro ao filtrar histórico: " + e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    /**
     * Limpa os campos do formulário
     */
    private void limparCampos() {
        txtBusca.setText("");
        txtCliente.setText("");
        txtCPF.setText("");
        comboPeriodo.setSelectedIndex(0);
        txtDataInicio.setText("");
        txtDataFim.setText("");
        comboStatus.setSelectedIndex(0);
        comboTipoVenda.setSelectedIndex(0);
        
        atualizarCamposPeriodo();
    }
    
    /**
     * Ver detalhes do histórico
     */
    private void verDetalhes() {
        int selectedRow = tabelaHistorico.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(workArea, "Selecione um histórico para ver detalhes!", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        HistoricoCompra historico = historicosEncontrados.get(selectedRow);
        JOptionPane.showMessageDialog(workArea, 
            "Detalhes da Compra:\n\n" +
            "Código: " + historico.getCodigo() + "\n" +
            "Data: " + historico.getData() + "\n" +
            "Cliente: " + historico.getCliente() + "\n" +
            "CPF: " + historico.getCpf() + "\n" +
            "Tipo: " + historico.getTipo() + "\n" +
            "Status: " + historico.getStatus() + "\n" +
            "Total: " + historico.getTotal() + "\n" +
            "Forma Pagamento: " + historico.getFormaPagamento(), 
            "Detalhes da Compra", JOptionPane.INFORMATION_MESSAGE);
    }
    
    /**
     * Gera relatório
     */
    private void gerarRelatorio() {
        JOptionPane.showMessageDialog(workArea, 
            "Gerando relatório de " + historicosEncontrados.size() + " históricos...\n(Implementar geração de relatório PDF/Excel)", 
            "Gerar Relatório", JOptionPane.INFORMATION_MESSAGE);
        
        SystemLogger.ui("Gerando relatório de " + historicosEncontrados.size() + " históricos");
    }
    
    /**
     * Exporta dados da tabela
     */
    private void exportarDados() {
        JOptionPane.showMessageDialog(workArea, 
            "Exportando " + historicosEncontrados.size() + " históricos...\n(Implementar exportação para CSV/Excel)", 
            "Exportar Dados", JOptionPane.INFORMATION_MESSAGE);
        
        SystemLogger.ui("Exportando " + historicosEncontrados.size() + " históricos");
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
     * Classe interna para representar um histórico de compra
     */
    private static class HistoricoCompra {
        private String codigo;
        private String data;
        private String cliente;
        private String cpf;
        private String tipo;
        private String status;
        private String total;
        private String formaPagamento;
        
        // Getters e Setters
        public String getCodigo() { return codigo; }
        public void setCodigo(String codigo) { this.codigo = codigo; }
        
        public String getData() { return data; }
        public void setData(String data) { this.data = data; }
        
        public String getCliente() { return cliente; }
        public void setCliente(String cliente) { this.cliente = cliente; }
        
        public String getCpf() { return cpf; }
        public void setCpf(String cpf) { this.cpf = cpf; }
        
        public String getTipo() { return tipo; }
        public void setTipo(String tipo) { this.tipo = tipo; }
        
        public String getStatus() { return status; }
        public void setStatus(String status) { this.status = status; }
        
        public String getTotal() { return total; }
        public void setTotal(String total) { this.total = total; }
        
        public String getFormaPagamento() { return formaPagamento; }
        public void setFormaPagamento(String formaPagamento) { this.formaPagamento = formaPagamento; }
    }
}
