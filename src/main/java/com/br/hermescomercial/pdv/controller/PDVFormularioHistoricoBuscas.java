package com.br.hermescomercial.pdv.controller;

import com.br.hermescomercial.util.SystemLogger;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Classe especializada para o formulário de Histórico de Buscas
 * Estrutura: Header → Busca → Formulário → Tabela
 */
public class PDVFormularioHistoricoBuscas {
    
    private JPanel workArea;
    private String usuarioAtual;
    private String nomeUsuario;
    
    // Componentes do formulário
    private JTextField txtBusca;
    private JTextField txtTermo;
    private JComboBox<String> comboTipoBusca;
    private JComboBox<String> comboCategoria;
    private JComboBox<String> comboStatus;
    private JTextField txtDataInicio;
    private JTextField txtDataFim;
    
    // Tabela de histórico de buscas
    private JTable tabelaHistorico;
    private DefaultTableModel modelTabela;
    private List<Busca> buscasEncontrados;
    
    // Cores
    private static final Color WHITE = Color.WHITE;
    private static final Color ACCENT_COLOR = new Color(52, 152, 219);
    private static final Color SUCCESS_COLOR = new Color(39, 174, 96);
    private static final Color DANGER_COLOR = new Color(231, 76, 60);
    private static final Color GRAY = new Color(149, 165, 166);
    
    public PDVFormularioHistoricoBuscas(JPanel workArea, String usuarioAtual, String nomeUsuario) {
        this.workArea = workArea;
        this.usuarioAtual = usuarioAtual;
        this.nomeUsuario = nomeUsuario;
        this.buscasEncontrados = new ArrayList<>();
    }
    
    /**
     * Cria o formulário completo de Histórico de Buscas
     */
    public JPanel criarFormularioHistoricoBuscas() {
        try {
            SystemLogger.ui("=== CRIANDO FORMULÁRIO HISTÓRICO DE BUSCAS ===");
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
            
            // Tabela de histórico de buscas
            JPanel tabelaPanel = criarPainelTabela();
            painelCentral.add(tabelaPanel, BorderLayout.SOUTH);
            
            painelPrincipal.add(painelCentral, BorderLayout.CENTER);
            
            SystemLogger.ui("Formulário Histórico de Buscas criado com sucesso");
            return painelPrincipal;
            
        } catch (Exception e) {
            SystemLogger.error("Erro ao criar formulário Histórico de Buscas", e);
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
        JLabel titleLabel = new JLabel("📜 Histórico de Buscas");
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
        txtBusca.setToolTipText("Digite termo ou usuário");
        
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
        
        // Seção Filtros do Histórico
        JLabel filtrosSectionLabel = new JLabel("🔍 Filtros do Histórico");
        filtrosSectionLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        filtrosSectionLabel.setForeground(ACCENT_COLOR);
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 4;
        formularioPanel.add(filtrosSectionLabel, gbc);
        
        // Termo
        gbc.gridy = 1; gbc.gridwidth = 1;
        JLabel lblTermo = new JLabel("Termo:");
        lblTermo.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        formularioPanel.add(lblTermo, gbc);
        
        gbc.gridx = 1; gbc.weightx = 1.0;
        txtTermo = new JTextField();
        txtTermo.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        txtTermo.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(GRAY),
            BorderFactory.createEmptyBorder(5, 8, 5, 8)
        ));
        formularioPanel.add(txtTermo, gbc);
        
        // Tipo Busca
        gbc.gridx = 2; gbc.weightx = 0.0;
        JLabel lblTipoBusca = new JLabel("Tipo:");
        lblTipoBusca.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        formularioPanel.add(lblTipoBusca, gbc);
        
        gbc.gridx = 3; gbc.weightx = 1.0;
        comboTipoBusca = new JComboBox<>(new String[]{"Todos", "Produto", "Cliente", "Venda", "Fornecedor", "Relatório", "Sistema"});
        comboTipoBusca.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        comboTipoBusca.setBorder(BorderFactory.createLineBorder(GRAY));
        formularioPanel.add(comboTipoBusca, gbc);
        
        // Categoria
        gbc.gridy = 2; gbc.gridx = 0;
        JLabel lblCategoria = new JLabel("Categoria:");
        lblCategoria.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        formularioPanel.add(lblCategoria, gbc);
        
        gbc.gridx = 1; gbc.weightx = 1.0;
        comboCategoria = new JComboBox<>(new String[]{"Todas", "Rápida", "Avançada", "Específica", "Global"});
        comboCategoria.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        comboCategoria.setBorder(BorderFactory.createLineBorder(GRAY));
        formularioPanel.add(comboCategoria, gbc);
        
        // Status
        gbc.gridx = 2; gbc.weightx = 0.0;
        JLabel lblStatus = new JLabel("Status:");
        lblStatus.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        formularioPanel.add(lblStatus, gbc);
        
        gbc.gridx = 3; gbc.weightx = 1.0;
        comboStatus = new JComboBox<>(new String[]{"Todos", "Sucesso", "Sem Resultados", "Erro", "Cancelada"});
        comboStatus.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        comboStatus.setBorder(BorderFactory.createLineBorder(GRAY));
        formularioPanel.add(comboStatus, gbc);
        
        // Período
        gbc.gridy = 3; gbc.gridx = 0;
        JLabel lblPeriodo = new JLabel("Período:");
        lblPeriodo.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        formularioPanel.add(lblPeriodo, gbc);
        
        gbc.gridx = 1; gbc.weightx = 1.0;
        JPanel periodoPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
        periodoPanel.setBackground(WHITE);
        
        txtDataInicio = new JTextField(8);
        txtDataInicio.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        txtDataInicio.setBorder(BorderFactory.createLineBorder(GRAY));
        txtDataInicio.setToolTipText("dd/mm/aaaa");
        
        JLabel ateLabel = new JLabel("até");
        ateLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        
        txtDataFim = new JTextField(8);
        txtDataFim.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        txtDataFim.setBorder(BorderFactory.createLineBorder(GRAY));
        txtDataFim.setToolTipText("dd/mm/aaaa");
        
        periodoPanel.add(txtDataInicio);
        periodoPanel.add(ateLabel);
        periodoPanel.add(txtDataFim);
        
        formularioPanel.add(periodoPanel, gbc);
        
        // Botões de ação
        gbc.gridy = 4; gbc.gridx = 0; gbc.gridwidth = 4; gbc.weightx = 0.0;
        JPanel botoesPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        botoesPanel.setBackground(WHITE);
        
        JButton btnFiltrar = new JButton("🔍 Aplicar Filtros");
        btnFiltrar.setBackground(ACCENT_COLOR);
        btnFiltrar.setForeground(WHITE);
        btnFiltrar.setFocusPainted(false);
        btnFiltrar.setBorderPainted(false);
        btnFiltrar.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btnFiltrar.addActionListener(e -> aplicarFiltros());
        
        JButton btnLimpar = new JButton("🧹 Limpar Filtros");
        btnLimpar.setBackground(GRAY);
        btnLimpar.setForeground(WHITE);
        btnLimpar.setFocusPainted(false);
        btnLimpar.setBorderPainted(false);
        btnLimpar.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btnLimpar.addActionListener(e -> limparFiltros());
        
        JButton btnExportar = new JButton("📄 Exportar");
        btnExportar.setBackground(SUCCESS_COLOR);
        btnExportar.setForeground(WHITE);
        btnExportar.setFocusPainted(false);
        btnExportar.setBorderPainted(false);
        btnExportar.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btnExportar.addActionListener(e -> exportarHistorico());
        
        JButton btnLimparHistorico = new JButton("🗑️ Limpar Histórico");
        btnLimparHistorico.setBackground(DANGER_COLOR);
        btnLimparHistorico.setForeground(WHITE);
        btnLimparHistorico.setFocusPainted(false);
        btnLimparHistorico.setBorderPainted(false);
        btnLimparHistorico.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btnLimparHistorico.addActionListener(e -> limparHistoricoCompleto());
        
        botoesPanel.add(btnFiltrar);
        botoesPanel.add(Box.createHorizontalStrut(10));
        botoesPanel.add(btnLimpar);
        botoesPanel.add(Box.createHorizontalStrut(10));
        botoesPanel.add(btnExportar);
        botoesPanel.add(Box.createHorizontalStrut(10));
        botoesPanel.add(btnLimparHistorico);
        
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
        
        JLabel tabelaLabel = new JLabel("📜 Histórico de Buscas Realizadas");
        tabelaLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        tabelaLabel.setForeground(ACCENT_COLOR);
        
        // Modelo da tabela
        String[] colunas = {"Data/Hora", "Termo", "Tipo", "Categoria", "Status", "Resultados", "Usuário", "Duração", "Ações"};
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
        
        JButton btnRelatorio = new JButton("");
        btnRelatorio.setBackground(ACCENT_COLOR);
        btnRelatorio.setForeground(WHITE);
        btnRelatorio.setFocusPainted(false);
        btnRelatorio.setBorderPainted(false);
        btnRelatorio.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btnRelatorio.addActionListener(e -> repetirBusca());
        
        JButton btnDetalhes = new JButton("");
        btnDetalhes.setBackground(ACCENT_COLOR);
        btnDetalhes.setForeground(WHITE);
        btnDetalhes.setFocusPainted(false);
        btnDetalhes.setBorderPainted(false);
        btnDetalhes.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btnDetalhes.addActionListener(e -> verDetalhes());
        
        JButton btnExcluir = new JButton("🗑️ Excluir");
        btnExcluir.setBackground(DANGER_COLOR);
        btnExcluir.setForeground(WHITE);
        btnExcluir.setFocusPainted(false);
        btnExcluir.setBorderPainted(false);
        btnExcluir.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btnExcluir.addActionListener(e -> excluirBusca());
        
        botoesTabelaPanel.add(btnRelatorio);
        botoesTabelaPanel.add(Box.createHorizontalStrut(10));
        botoesTabelaPanel.add(btnDetalhes);
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
        buscasEncontrados.clear();
        
        // Dados de exemplo
        Object[][] dadosExemplo = {
            {"10/05/2026 16:45", "Notebook Dell", "Produto", "Rápida", "Sucesso", "3", "admin", "0.5s", "🔄"},
            {"10/05/2026 16:30", "João Silva", "Cliente", "Específica", "Sucesso", "1", "admin", "1.2s", "🔄"},
            {"10/05/2026 16:15", "Vendas do dia", "Relatório", "Global", "Sucesso", "15", "maria", "2.1s", "🔄"},
            {"10/05/2026 15:50", "Mouse USB", "Produto", "Rápida", "Sem Resultados", "0", "joao", "0.8s", "🔄"},
            {"10/05/2026 15:30", "Maria Santos", "Cliente", "Específica", "Sucesso", "1", "admin", "0.9s", "🔄"},
            {"10/05/2026 15:15", "Fornecedor ABC", "Fornecedor", "Avançada", "Sucesso", "5", "maria", "3.2s", "🔄"},
            {"10/05/2026 14:45", "Relatório financeiro", "Relatório", "Global", "Erro", "0", "admin", "1.5s", "🔄"},
            {"10/05/2026 14:30", "Papel A4", "Produto", "Rápida", "Cancelada", "0", "joao", "0.3s", "🔄"},
            {"10/05/2026 14:15", "Carlos Oliveira", "Cliente", "Específica", "Sucesso", "1", "admin", "0.7s", "🔄"},
            {"10/05/2026 13:45", "Estoque baixo", "Sistema", "Global", "Sucesso", "8", "maria", "1.8s", "🔄"}
        };
        
        for (Object[] dados : dadosExemplo) {
            modelTabela.addRow(dados);
            
            // Adicionar à lista de buscas
            Busca busca = new Busca();
            busca.setDataHora((String) dados[0]);
            busca.setTermo((String) dados[1]);
            busca.setTipo((String) dados[2]);
            busca.setCategoria((String) dados[3]);
            busca.setStatus((String) dados[4]);
            busca.setResultados((String) dados[5]);
            busca.setUsuario((String) dados[6]);
            busca.setDuracao((String) dados[7]);
            buscasEncontrados.add(busca);
        }
    }
    
    /**
     * Preenche dados iniciais
     */
    private void preencherDadosIniciais() {
        comboTipoBusca.setSelectedIndex(0);
        comboCategoria.setSelectedIndex(0);
        comboStatus.setSelectedIndex(0);
        
        // Data atual
        String dataAtual = new java.text.SimpleDateFormat("dd/MM/yyyy").format(new java.util.Date());
        txtDataInicio.setText(dataAtual);
        txtDataFim.setText(dataAtual);
    }
    
    /**
     * Busca no histórico
     */
    private void buscarHistorico() {
        String termo = txtBusca.getText().trim();
        if (termo.isEmpty()) {
            JOptionPane.showMessageDialog(workArea, "Digite um termo para buscar!", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        JOptionPane.showMessageDialog(workArea, 
            "Busca realizada para: " + termo + "\n" +
            "Buscas encontradas: " + buscasEncontrados.size(), 
            "Resultado", JOptionPane.INFORMATION_MESSAGE);
        
        SystemLogger.ui("Busca no histórico para: " + termo);
    }
    
    /**
     * Aplica filtros
     */
    private void aplicarFiltros() {
        String termo = txtTermo.getText().trim();
        String tipo = (String) comboTipoBusca.getSelectedItem();
        String categoria = (String) comboCategoria.getSelectedItem();
        String status = (String) comboStatus.getSelectedItem();
        String dataInicio = txtDataInicio.getText().trim();
        String dataFim = txtDataFim.getText().trim();
        
        JOptionPane.showMessageDialog(workArea, 
            "Filtros aplicados:\n" +
            "Termo: " + termo + "\n" +
            "Tipo: " + tipo + "\n" +
            "Categoria: " + categoria + "\n" +
            "Status: " + status + "\n" +
            "Período: " + dataInicio + " até " + dataFim + "\n" +
            "Resultados: " + buscasEncontrados.size(), 
            "Filtros Aplicados", JOptionPane.INFORMATION_MESSAGE);
        
        SystemLogger.ui("Filtros aplicados por: " + nomeUsuario);
    }
    
    /**
     * Limpa filtros
     */
    private void limparFiltros() {
        txtBusca.setText("");
        txtTermo.setText("");
        comboTipoBusca.setSelectedIndex(0);
        comboCategoria.setSelectedIndex(0);
        comboStatus.setSelectedIndex(0);
        
        preencherDadosIniciais();
    }
    
    /**
     * Exporta histórico
     */
    private void exportarHistorico() {
        JOptionPane.showMessageDialog(workArea, 
            "Exportando " + buscasEncontrados.size() + " buscas...\n(Implementar exportação para CSV/Excel)", 
            "Exportar", JOptionPane.INFORMATION_MESSAGE);
        
        SystemLogger.ui("Exportando " + buscasEncontrados.size() + " buscas do histórico");
    }
    
    /**
     * Limpa histórico completo
     */
    private void limparHistoricoCompleto() {
        int confirmacao = JOptionPane.showConfirmDialog(
            workArea,
            "Deseja realmente limpar todo o histórico de buscas?\n\n" +
            "Esta ação não pode ser desfeita e removerá " + buscasEncontrados.size() + " registros.",
            "Limpar Histórico",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.QUESTION_MESSAGE
        );
        
        if (confirmacao == JOptionPane.YES_OPTION) {
            
            modelTabela.setRowCount(0);
            buscasEncontrados.clear();
            
            JOptionPane.showMessageDialog(workArea, 
                "Histórico limpo com sucesso!", 
                "Sucesso", JOptionPane.INFORMATION_MESSAGE);
            
            SystemLogger.ui("Histórico de buscas limpo por: " + nomeUsuario);
        }
    }
    
    /**
     * Repete busca selecionada
     */
    private void repetirBusca() {
        int selectedRow = tabelaHistorico.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(workArea, "Selecione uma busca para repetir!", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        Busca busca = buscasEncontrados.get(selectedRow);
        
        JOptionPane.showMessageDialog(workArea, 
            "Repetindo busca...\n\n" +
            "Termo: " + busca.getTermo() + "\n" +
            "Tipo: " + busca.getTipo() + "\n" +
            "Categoria: " + busca.getCategoria() + "\n\n" +
            "(Implementar repetição da busca)", 
            "Repetir Busca", JOptionPane.INFORMATION_MESSAGE);
        
        SystemLogger.ui("Repetindo busca: " + busca.getTermo());
    }
    
    /**
     * Ver detalhes da busca
     */
    private void verDetalhes() {
        int selectedRow = tabelaHistorico.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(workArea, "Selecione uma busca para ver detalhes!", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        Busca busca = buscasEncontrados.get(selectedRow);
        
        JOptionPane.showMessageDialog(workArea, 
            "Detalhes da Busca:\n\n" +
            "Data/Hora: " + busca.getDataHora() + "\n" +
            "Termo: " + busca.getTermo() + "\n" +
            "Tipo: " + busca.getTipo() + "\n" +
            "Categoria: " + busca.getCategoria() + "\n" +
            "Status: " + busca.getStatus() + "\n" +
            "Resultados: " + busca.getResultados() + "\n" +
            "Usuário: " + busca.getUsuario() + "\n" +
            "Duração: " + busca.getDuracao() + "\n\n" +
            "Parâmetros adicionais disponíveis na busca completa.", 
            "Detalhes da Busca", JOptionPane.INFORMATION_MESSAGE);
        
        SystemLogger.ui("Detalhes visualizados para: " + busca.getTermo());
    }
    
    /**
     * Exclui busca selecionada
     */
    private void excluirBusca() {
        int selectedRow = tabelaHistorico.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(workArea, "Selecione uma busca para excluir!", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        Busca busca = buscasEncontrados.get(selectedRow);
        
        int confirmacao = JOptionPane.showConfirmDialog(
            workArea,
            "Deseja realmente excluir esta busca do histórico?\n\n" +
            "Termo: " + busca.getTermo() + "\n" +
            "Data: " + busca.getDataHora() + "\n" +
            "Esta ação não pode ser desfeita.",
            "Excluir Busca",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.QUESTION_MESSAGE
        );
        
        if (confirmacao == JOptionPane.YES_OPTION) {
            
            buscasEncontrados.remove(selectedRow);
            modelTabela.removeRow(selectedRow);
            
            JOptionPane.showMessageDialog(workArea, 
                "Busca excluída com sucesso!", 
                "Sucesso", JOptionPane.INFORMATION_MESSAGE);
            
            SystemLogger.ui("Busca excluída do histórico: " + busca.getTermo());
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
     * Classe interna para representar uma busca
     */
    private static class Busca {
        private String dataHora;
        private String termo;
        private String tipo;
        private String categoria;
        private String status;
        private String resultados;
        private String usuario;
        private String duracao;
        
        // Getters e Setters
        public String getDataHora() { return dataHora; }
        public void setDataHora(String dataHora) { this.dataHora = dataHora; }
        
        public String getTermo() { return termo; }
        public void setTermo(String termo) { this.termo = termo; }
        
        public String getTipo() { return tipo; }
        public void setTipo(String tipo) { this.tipo = tipo; }
        
        public String getCategoria() { return categoria; }
        public void setCategoria(String categoria) { this.categoria = categoria; }
        
        public String getStatus() { return status; }
        public void setStatus(String status) { this.status = status; }
        
        public String getResultados() { return resultados; }
        public void setResultados(String resultados) { this.resultados = resultados; }
        
        public String getUsuario() { return usuario; }
        public void setUsuario(String usuario) { this.usuario = usuario; }
        
        public String getDuracao() { return duracao; }
        public void setDuracao(String duracao) { this.duracao = duracao; }
    }
}
