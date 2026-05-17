package com.br.hermescomercial.pdv.controller;

import com.br.hermescomercial.util.SystemLogger;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Classe especializada para o formulário de Notificações Não Lidas
 * Estrutura: Header → Busca → Formulário → Tabela
 */
public class PDVFormularioNaoLidas {
    
    private JPanel workArea;
    private String usuarioAtual;
    private String nomeUsuario;
    
    // Componentes do formulário
    private JTextField txtBusca;
    private JComboBox<String> comboTipo;
    private JComboBox<String> comboPrioridade;
    private JComboBox<String> comboStatus;
    
    // Tabela de notificações não lidas
    private JTable tabelaNotificacoes;
    private DefaultTableModel modelTabela;
    private List<Notificacao> notificacoesEncontradas;
    
    // Cores
    private static final Color WHITE = Color.WHITE;
    private static final Color ACCENT_COLOR = new Color(52, 152, 219);
    private static final Color SUCCESS_COLOR = new Color(39, 174, 96);
    private static final Color DANGER_COLOR = new Color(231, 76, 60);
    private static final Color GRAY = new Color(149, 165, 166);
    
    public PDVFormularioNaoLidas(JPanel workArea, String usuarioAtual, String nomeUsuario) {
        this.workArea = workArea;
        this.usuarioAtual = usuarioAtual;
        this.nomeUsuario = nomeUsuario;
        this.notificacoesEncontradas = new ArrayList<>();
    }
    
    /**
     * Cria o formulário completo de Notificações Não Lidas
     */
    public JPanel criarFormularioNaoLidas() {
        try {
            SystemLogger.ui("=== CRIANDO FORMULÁRIO NOTIFICAÇÕES NÃO LIDAS ===");
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
            
            // Painel do formulário (com filtros)
            JPanel formularioPanel = criarPainelFormulario();
            painelCentral.add(formularioPanel, BorderLayout.CENTER);
            
            // Tabela de notificações não lidas
            JPanel tabelaPanel = criarPainelTabela();
            painelCentral.add(tabelaPanel, BorderLayout.SOUTH);
            
            painelPrincipal.add(painelCentral, BorderLayout.CENTER);
            
            SystemLogger.ui("Formulário Notificações Não Lidas criado com sucesso");
            return painelPrincipal;
            
        } catch (Exception e) {
            SystemLogger.error("Erro ao criar formulário Notificações Não Lidas", e);
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
        JLabel titleLabel = new JLabel("📬 Não Lidas");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        titleLabel.setForeground(WHITE);
        
        // Contador de não lidas
        JLabel contadorLabel = new JLabel("📊 0 não lidas");
        contadorLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        contadorLabel.setForeground(WHITE);
        
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
        userInfoPanel.add(Box.createHorizontalStrut(20));
        userInfoPanel.add(contadorLabel);
        
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
        txtBusca.setToolTipText("Digite título ou mensagem da notificação");
        
        JButton btnBuscar = new JButton("🔍 Buscar");
        btnBuscar.setBackground(ACCENT_COLOR);
        btnBuscar.setForeground(WHITE);
        btnBuscar.setFocusPainted(false);
        btnBuscar.setBorderPainted(false);
        btnBuscar.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btnBuscar.addActionListener(e -> buscarNotificacoes());
        
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
     * Cria o painel do formulário com filtros
     */
    private JPanel criarPainelFormulario() {
        JPanel formularioPanel = new JPanel(new GridBagLayout());
        formularioPanel.setBackground(WHITE);
        formularioPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
        // Seção Filtros
        JLabel filtrosSectionLabel = new JLabel("🔍 Filtros de Notificações");
        filtrosSectionLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        filtrosSectionLabel.setForeground(ACCENT_COLOR);
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 4;
        formularioPanel.add(filtrosSectionLabel, gbc);
        
        // Tipo
        gbc.gridy = 1; gbc.gridwidth = 1;
        JLabel lblTipo = new JLabel("Tipo:");
        lblTipo.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        formularioPanel.add(lblTipo, gbc);
        
        gbc.gridx = 1; gbc.weightx = 1.0;
        comboTipo = new JComboBox<>(new String[]{"Todos", "Sistema", "Vendas", "Estoque", "Financeiro", "Usuário", "Alerta"});
        comboTipo.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        comboTipo.setBorder(BorderFactory.createLineBorder(GRAY));
        formularioPanel.add(comboTipo, gbc);
        
        // Prioridade
        gbc.gridx = 2; gbc.weightx = 0.0;
        JLabel lblPrioridade = new JLabel("Prioridade:");
        lblPrioridade.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        formularioPanel.add(lblPrioridade, gbc);
        
        gbc.gridx = 3; gbc.weightx = 1.0;
        comboPrioridade = new JComboBox<>(new String[]{"Todas", "Alta", "Média", "Baixa"});
        comboPrioridade.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        comboPrioridade.setBorder(BorderFactory.createLineBorder(GRAY));
        formularioPanel.add(comboPrioridade, gbc);
        
        // Status (fixo como Não Lida)
        gbc.gridy = 2; gbc.gridx = 0;
        JLabel lblStatus = new JLabel("Status:");
        lblStatus.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        formularioPanel.add(lblStatus, gbc);
        
        gbc.gridx = 1; gbc.weightx = 1.0;
        comboStatus = new JComboBox<>(new String[]{"Não Lida"});
        comboStatus.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        comboStatus.setBorder(BorderFactory.createLineBorder(GRAY));
        comboStatus.setEnabled(false); // Fixo como Não Lida
        formularioPanel.add(comboStatus, gbc);
        
        // Botões de ação
        gbc.gridy = 3; gbc.gridx = 0; gbc.gridwidth = 4; gbc.weightx = 0.0;
        JPanel botoesPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        botoesPanel.setBackground(WHITE);
        
        JButton btnMarcarLidas = new JButton("✅ Marcar como Lidas");
        btnMarcarLidas.setBackground(SUCCESS_COLOR);
        btnMarcarLidas.setForeground(WHITE);
        btnMarcarLidas.setFocusPainted(false);
        btnMarcarLidas.setBorderPainted(false);
        btnMarcarLidas.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btnMarcarLidas.addActionListener(e -> marcarComoLidas());
        
        JButton btnLimpar = new JButton("🧹 Limpar Filtros");
        btnLimpar.setBackground(GRAY);
        btnLimpar.setForeground(WHITE);
        btnLimpar.setFocusPainted(false);
        btnLimpar.setBorderPainted(false);
        btnLimpar.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btnLimpar.addActionListener(e -> limparFiltros());
        
        JButton btnAtualizar = new JButton("🔄 Atualizar");
        btnAtualizar.setBackground(ACCENT_COLOR);
        btnAtualizar.setForeground(WHITE);
        btnAtualizar.setFocusPainted(false);
        btnAtualizar.setBorderPainted(false);
        btnAtualizar.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btnAtualizar.addActionListener(e -> atualizarNotificacoes());
        
        botoesPanel.add(btnMarcarLidas);
        botoesPanel.add(Box.createHorizontalStrut(10));
        botoesPanel.add(btnLimpar);
        botoesPanel.add(Box.createHorizontalStrut(10));
        botoesPanel.add(btnAtualizar);
        
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
        
        JLabel tabelaLabel = new JLabel("📋 Notificações Não Lidas");
        tabelaLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        tabelaLabel.setForeground(ACCENT_COLOR);
        
        // Modelo da tabela
        String[] colunas = {"Título", "Tipo", "Prioridade", "Data/Hora", "Remetente", "Ações"};
        modelTabela = new DefaultTableModel(colunas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 5; // Apenas a coluna de ações é editável
            }
        };
        
        tabelaNotificacoes = new JTable(modelTabela);
        tabelaNotificacoes.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        tabelaNotificacoes.setRowHeight(25);
        tabelaNotificacoes.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 12));
        tabelaNotificacoes.getTableHeader().setBackground(ACCENT_COLOR);
        tabelaNotificacoes.getTableHeader().setForeground(WHITE);
        
        // Scroll pane
        JScrollPane scrollPane = new JScrollPane(tabelaNotificacoes);
        scrollPane.setPreferredSize(new Dimension(0, 250));
        scrollPane.setBorder(BorderFactory.createLineBorder(GRAY));
        
        // Painel de botões da tabela
        JPanel botoesTabelaPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        botoesTabelaPanel.setBackground(WHITE);
        
        JButton btnVisualizar = new JButton("👁️ Visualizar");
        btnVisualizar.setBackground(ACCENT_COLOR);
        btnVisualizar.setForeground(WHITE);
        btnVisualizar.setFocusPainted(false);
        btnVisualizar.setBorderPainted(false);
        btnVisualizar.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btnVisualizar.addActionListener(e -> visualizarNotificacao());
        
        JButton btnExcluir = new JButton("🗑️ Excluir");
        btnExcluir.setBackground(DANGER_COLOR);
        btnExcluir.setForeground(WHITE);
        btnExcluir.setFocusPainted(false);
        btnExcluir.setBorderPainted(false);
        btnExcluir.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btnExcluir.addActionListener(e -> excluirNotificacao());
        
        botoesTabelaPanel.add(btnVisualizar);
        botoesTabelaPanel.add(Box.createHorizontalStrut(10));
        botoesTabelaPanel.add(btnExcluir);
        
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
        notificacoesEncontradas.clear();
        
        // Dados de exemplo
        Object[][] dadosExemplo = {
            {"Nova Venda Realizada", "Vendas", "Média", "10/05/2026 16:30", "Sistema", "👁️"},
            {"Estoque Baixo - Produto X", "Estoque", "Alta", "10/05/2026 15:45", "Sistema", "👁️"},
            {"Backup Concluído", "Sistema", "Baixa", "10/05/2026 14:20", "Sistema", "👁️"},
            {"Novo Usuário Cadastrado", "Usuário", "Média", "10/05/2026 13:15", "Admin", "👁️"},
            {"Relatório Financeiro Disponível", "Financeiro", "Média", "10/05/2026 12:00", "Sistema", "👁️"},
            {"Manutenção Programada", "Alerta", "Alta", "10/05/2026 11:30", "Sistema", "👁️"},
            {"Atualização do Sistema", "Sistema", "Baixa", "10/05/2026 10:15", "Admin", "👁️"},
            {"Erro de Conexão", "Sistema", "Alta", "10/05/2026 09:45", "Sistema", "👁️"}
        };
        
        for (Object[] dados : dadosExemplo) {
            modelTabela.addRow(dados);
            
            // Adicionar à lista de notificações
            Notificacao notificacao = new Notificacao();
            notificacao.setTitulo((String) dados[0]);
            notificacao.setTipo((String) dados[1]);
            notificacao.setPrioridade((String) dados[2]);
            notificacao.setDataHora((String) dados[3]);
            notificacao.setRemetente((String) dados[4]);
            notificacoesEncontradas.add(notificacao);
        }
    }
    
    /**
     * Preenche dados iniciais
     */
    private void preencherDadosIniciais() {
        comboTipo.setSelectedIndex(0);
        comboPrioridade.setSelectedIndex(0);
        comboStatus.setSelectedIndex(0);
    }
    
    /**
     * Busca notificações
     */
    private void buscarNotificacoes() {
        String termo = txtBusca.getText().trim();
        if (termo.isEmpty()) {
            JOptionPane.showMessageDialog(workArea, "Digite um termo para buscar!", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        JOptionPane.showMessageDialog(workArea, 
            "Busca realizada para: " + termo + "\n" +
            "Notificações encontradas: " + notificacoesEncontradas.size(), 
            "Resultado", JOptionPane.INFORMATION_MESSAGE);
        
        SystemLogger.ui("Busca realizada para: " + termo);
    }
    
    /**
     * Marca notificações selecionadas como lidas
     */
    private void marcarComoLidas() {
        int[] selectedRows = tabelaNotificacoes.getSelectedRows();
        if (selectedRows.length == 0) {
            JOptionPane.showMessageDialog(workArea, "Selecione notificações para marcar como lidas!", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        int confirmacao = JOptionPane.showConfirmDialog(
            workArea,
            "Deseja marcar " + selectedRows.length + " notificação(ões) como lida(s)?",
            "Marcar como Lidas",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.QUESTION_MESSAGE
        );
        
        if (confirmacao == JOptionPane.YES_OPTION) {
            
            // Remover linhas selecionadas da tabela
            for (int i = selectedRows.length - 1; i >= 0; i--) {
                int row = selectedRows[i];
                notificacoesEncontradas.remove(row);
                modelTabela.removeRow(row);
            }
            
            JOptionPane.showMessageDialog(workArea, 
                selectedRows.length + " notificação(ões) marcada(s) como lida(s) com sucesso!", 
                "Sucesso", JOptionPane.INFORMATION_MESSAGE);
            
            SystemLogger.ui(selectedRows.length + " notificações marcadas como lidas por: " + nomeUsuario);
        }
    }
    
    /**
     * Limpa filtros
     */
    private void limparFiltros() {
        txtBusca.setText("");
        comboTipo.setSelectedIndex(0);
        comboPrioridade.setSelectedIndex(0);
        
        preencherDadosIniciais();
    }
    
    /**
     * Atualiza notificações
     */
    private void atualizarNotificacoes() {
        JOptionPane.showMessageDialog(workArea, 
            "Atualizando lista de notificações...\n(Implementar atualização do banco de dados)", 
            "Atualizar", JOptionPane.INFORMATION_MESSAGE);
        
        adicionarDadosExemplo();
        SystemLogger.ui("Notificações atualizadas por: " + nomeUsuario);
    }
    
    /**
     * Visualiza notificação selecionada
     */
    private void visualizarNotificacao() {
        int selectedRow = tabelaNotificacoes.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(workArea, "Selecione uma notificação para visualizar!", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        Notificacao notificacao = notificacoesEncontradas.get(selectedRow);
        
        JOptionPane.showMessageDialog(workArea, 
            "Detalhes da Notificação:\n\n" +
            "Título: " + notificacao.getTitulo() + "\n" +
            "Tipo: " + notificacao.getTipo() + "\n" +
            "Prioridade: " + notificacao.getPrioridade() + "\n" +
            "Data/Hora: " + notificacao.getDataHora() + "\n" +
            "Remetente: " + notificacao.getRemetente() + "\n\n" +
            "Mensagem: Esta é uma mensagem de exemplo da notificação selecionada.", 
            "Visualizar Notificação", JOptionPane.INFORMATION_MESSAGE);
        
        SystemLogger.ui("Notificação visualizada: " + notificacao.getTitulo());
    }
    
    /**
     * Exclui notificação selecionada
     */
    private void excluirNotificacao() {
        int selectedRow = tabelaNotificacoes.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(workArea, "Selecione uma notificação para excluir!", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        Notificacao notificacao = notificacoesEncontradas.get(selectedRow);
        
        int confirmacao = JOptionPane.showConfirmDialog(
            workArea,
            "Deseja realmente excluir esta notificação?\n\n" +
            "Título: " + notificacao.getTitulo() + "\n" +
            "Esta ação não pode ser desfeita.",
            "Excluir Notificação",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.QUESTION_MESSAGE
        );
        
        if (confirmacao == JOptionPane.YES_OPTION) {
            
            notificacoesEncontradas.remove(selectedRow);
            modelTabela.removeRow(selectedRow);
            
            JOptionPane.showMessageDialog(workArea, 
                "Notificação excluída com sucesso!", 
                "Sucesso", JOptionPane.INFORMATION_MESSAGE);
            
            SystemLogger.ui("Notificação excluída: " + notificacao.getTitulo());
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
     * Classe interna para representar uma notificação
     */
    private static class Notificacao {
        private String titulo;
        private String tipo;
        private String prioridade;
        private String dataHora;
        private String remetente;
        
        // Getters e Setters
        public String getTitulo() { return titulo; }
        public void setTitulo(String titulo) { this.titulo = titulo; }
        
        public String getTipo() { return tipo; }
        public void setTipo(String tipo) { this.tipo = tipo; }
        
        public String getPrioridade() { return prioridade; }
        public void setPrioridade(String prioridade) { this.prioridade = prioridade; }
        
        public String getDataHora() { return dataHora; }
        public void setDataHora(String dataHora) { this.dataHora = dataHora; }
        
        public String getRemetente() { return remetente; }
        public void setRemetente(String remetente) { this.remetente = remetente; }
    }
}
