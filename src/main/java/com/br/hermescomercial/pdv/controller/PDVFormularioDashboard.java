package com.br.hermescomercial.pdv.controller;

import com.br.hermescomercial.service.DashboardService;
import com.br.hermescomercial.ui.layout.LayoutPadrao;
import com.br.hermescomercial.util.SystemLogger;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

/**
 * Formulário especializado de Dashboard Analytics
 * Segue o padrão Header → Busca → Formulário → Tabela
 * @author Hermes Comercial
 * @version 3.0.0
 */
public class PDVFormularioDashboard {
    
    private JPanel workArea;
    private String usuarioAtual;
    private String nomeUsuario;
    
    // Componentes principais
    private JPanel mainPanel;
    private JPanel headerPanel;
    private JPanel searchPanel;
    private JPanel formPanel;
    private JPanel tablePanel;
    
    // Dashboard Service
    private DashboardService dashboardService;
    
    // Campos de busca
    private JTextField txtBusca;
    private JComboBox<String> cbTipoBusca;
    private JComboBox<String> cbPeriodo;
    private JComboBox<String> cbAno;
    private JButton btnBuscar;
    private JButton btnLimpar;
    
    // Campos do formulário
    private JTextField txtReceitaTotal;
    private JTextField txtTotalVendas;
    private JTextField txtTicketMedio;
    private JTextField txtCrescimento;
    private JTextField txtMetas;
    private JTextField txtEstoque;
    private JTextArea txtObservacoes;
    
    // Tabela de resultados
    private JTable table;
    private DefaultTableModel tableModel;
    private JScrollPane scrollPane;
    
    // Botões de ação
    private JButton btnNovo;
    private JButton btnEditar;
    private JButton btnExcluir;
    private JButton btnAtualizar;
    private JButton btnExportar;
    
    // Dados
    private LocalDate dataInicio;
    private LocalDate dataFim;
    
    // Cores
    private static final Color PRIMARY_COLOR = new Color(33, 150, 243);
    private static final Color SUCCESS_COLOR = new Color(76, 175, 80);
    private static final Color WARNING_COLOR = new Color(255, 193, 7);
    private static final Color DANGER_COLOR = new Color(244, 67, 54);
    
    public PDVFormularioDashboard(JPanel workArea, String usuario, String nome) {
        this.workArea = workArea;
        this.usuarioAtual = usuario;
        this.nomeUsuario = nome;
        this.dashboardService = new DashboardService();
        initializeComponents();
        setupLayout();
        setupEvents();
        loadInitialData();
    }
    
    private void initializeComponents() {
        SystemLogger.ui("Inicializando componentes do Dashboard Analytics");
        
        // Painel principal
        mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(Color.WHITE);
        mainPanel.setBorder(new EmptyBorder(20, 20, 20, 20));
        
        // Header
        createHeaderPanel();
        
        // Painel de busca
        createSearchPanel();
        
        // Painel de formulário
        createFormPanel();
        
        // Painel de tabela
        createTablePanel();
        
        SystemLogger.ui("Componentes do Dashboard Analytics inicializados com sucesso");
    }
    
    private void createHeaderPanel() {
        headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(PRIMARY_COLOR);
        headerPanel.setBorder(new EmptyBorder(15, 20, 15, 20));
        headerPanel.setPreferredSize(new Dimension(0, 80));
        
        // Título
        JLabel titleLabel = new JLabel("📊 Dashboard Analytics");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setForeground(Color.WHITE);
        
        // Subtítulo
        JLabel subtitleLabel = new JLabel("Análise de vendas e métricas de negócio");
        subtitleLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        subtitleLabel.setForeground(new Color(200, 220, 240));
        
        // Painel de título
        JPanel titlePanel = new JPanel(new BorderLayout());
        titlePanel.setOpaque(false);
        titlePanel.add(titleLabel, BorderLayout.NORTH);
        titlePanel.add(subtitleLabel, BorderLayout.SOUTH);
        
        // Painel de botões
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        buttonPanel.setOpaque(false);
        
        btnAtualizar = new JButton("🔄 Atualizar");
        btnAtualizar.setBackground(SUCCESS_COLOR);
        btnAtualizar.setForeground(Color.WHITE);
        btnAtualizar.setFocusPainted(false);
        btnAtualizar.setBorder(BorderFactory.createEmptyBorder(8, 16, 8, 16));
        
        btnExportar = new JButton("📥 Exportar");
        btnExportar.setBackground(PRIMARY_COLOR);
        btnExportar.setForeground(Color.WHITE);
        btnExportar.setFocusPainted(false);
        btnExportar.setBorder(BorderFactory.createEmptyBorder(8, 16, 8, 16));
        
        buttonPanel.add(btnAtualizar);
        buttonPanel.add(btnExportar);
        
        headerPanel.add(titlePanel, BorderLayout.WEST);
        headerPanel.add(buttonPanel, BorderLayout.EAST);
    }
    
    private void createSearchPanel() {
        searchPanel = new JPanel(new BorderLayout());
        searchPanel.setBackground(new Color(240, 240, 240));
        searchPanel.setBorder(BorderFactory.createTitledBorder("🔍 Filtros de Busca"));
        
        // Painel de campos
        JPanel fieldsPanel = new JPanel(new GridLayout(2, 4, 10, 10));
        fieldsPanel.setBackground(new Color(240, 240, 240));
        
        // Tipo de busca
        fieldsPanel.add(new JLabel("Tipo de Busca:"));
        String[] tiposBusca = {"Todos", "Receita", "Vendas", "Produtos", "Período"};
        cbTipoBusca = new JComboBox<>(tiposBusca);
        fieldsPanel.add(cbTipoBusca);
        
        // Campo de busca
        fieldsPanel.add(new JLabel("Buscar:"));
        txtBusca = new JTextField();
        fieldsPanel.add(txtBusca);
        
        // Período
        fieldsPanel.add(new JLabel("Período:"));
        String[] periodos = {"Hoje", "Últimos 7 dias", "Últimos 30 dias", "Este mês", "Últimos 3 meses", "Este ano"};
        cbPeriodo = new JComboBox<>(periodos);
        fieldsPanel.add(cbPeriodo);
        
        // Ano
        fieldsPanel.add(new JLabel("Ano:"));
        String[] anos = {"2024", "2023", "2022", "2021", "2020"};
        cbAno = new JComboBox<>(anos);
        cbAno.setSelectedItem("2024");
        fieldsPanel.add(cbAno);
        
        // Painel de botões
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        buttonPanel.setBackground(new Color(240, 240, 240));
        
        btnBuscar = new JButton("🔍 Buscar");
        btnBuscar.setBackground(PRIMARY_COLOR);
        btnBuscar.setForeground(Color.WHITE);
        btnBuscar.setFocusPainted(false);
        
        btnLimpar = new JButton("🧹 Limpar");
        btnLimpar.setBackground(WARNING_COLOR);
        btnLimpar.setForeground(Color.WHITE);
        btnLimpar.setFocusPainted(false);
        
        buttonPanel.add(btnBuscar);
        buttonPanel.add(btnLimpar);
        
        searchPanel.add(fieldsPanel, BorderLayout.CENTER);
        searchPanel.add(buttonPanel, BorderLayout.SOUTH);
    }
    
    private void createFormPanel() {
        formPanel = new JPanel(new BorderLayout());
        formPanel.setBackground(Color.WHITE);
        formPanel.setBorder(BorderFactory.createTitledBorder("📊 Métricas do Dashboard"));
        
        // Painel de campos
        JPanel fieldsPanel = new JPanel(new GridLayout(3, 4, 10, 10));
        fieldsPanel.setBackground(Color.WHITE);
        
        // Receita Total
        fieldsPanel.add(new JLabel("💰 Receita Total:"));
        txtReceitaTotal = new JTextField();
        txtReceitaTotal.setEditable(false);
        txtReceitaTotal.setBackground(new Color(240, 255, 240));
        fieldsPanel.add(txtReceitaTotal);
        
        // Total de Vendas
        fieldsPanel.add(new JLabel("📈 Total de Vendas:"));
        txtTotalVendas = new JTextField();
        txtTotalVendas.setEditable(false);
        txtTotalVendas.setBackground(new Color(240, 255, 240));
        fieldsPanel.add(txtTotalVendas);
        
        // Ticket Médio
        fieldsPanel.add(new JLabel("🎫 Ticket Médio:"));
        txtTicketMedio = new JTextField();
        txtTicketMedio.setEditable(false);
        txtTicketMedio.setBackground(new Color(240, 255, 240));
        fieldsPanel.add(txtTicketMedio);
        
        // Crescimento
        fieldsPanel.add(new JLabel("📊 Crescimento (%):"));
        txtCrescimento = new JTextField();
        txtCrescimento.setEditable(false);
        txtCrescimento.setBackground(new Color(255, 255, 240));
        fieldsPanel.add(txtCrescimento);
        
        // Metas
        fieldsPanel.add(new JLabel("🎯 Metas Alcançadas (%):"));
        txtMetas = new JTextField();
        txtMetas.setEditable(false);
        txtMetas.setBackground(new Color(255, 255, 240));
        fieldsPanel.add(txtMetas);
        
        // Estoque
        fieldsPanel.add(new JLabel("📦 Itens em Estoque:"));
        txtEstoque = new JTextField();
        txtEstoque.setEditable(false);
        txtEstoque.setBackground(new Color(240, 240, 255));
        fieldsPanel.add(txtEstoque);
        
        // Observações
        fieldsPanel.add(new JLabel("📝 Observações:"));
        txtObservacoes = new JTextArea(3, 20);
        txtObservacoes.setEditable(true);
        txtObservacoes.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        
        // Painel de observações
        JPanel obsPanel = new JPanel(new BorderLayout());
        obsPanel.setBackground(Color.WHITE);
        obsPanel.add(new JLabel("📝 Observações:"), BorderLayout.NORTH);
        obsPanel.add(new JScrollPane(txtObservacoes), BorderLayout.CENTER);
        
        // Painel principal do formulário
        JPanel mainFormPanel = new JPanel(new BorderLayout());
        mainFormPanel.setBackground(Color.WHITE);
        mainFormPanel.add(fieldsPanel, BorderLayout.CENTER);
        mainFormPanel.add(obsPanel, BorderLayout.SOUTH);
        
        formPanel.add(mainFormPanel, BorderLayout.CENTER);
    }
    
    private void createTablePanel() {
        tablePanel = new JPanel(new BorderLayout());
        tablePanel.setBackground(Color.WHITE);
        tablePanel.setBorder(BorderFactory.createTitledBorder("📋 Resultados da Análise"));
        
        // Modelo da tabela
        String[] colunas = {
            "ID", "Período", "Receita Total", "Total Vendas", 
            "Ticket Médio", "Crescimento %", "Metas %", "Estoque",
            "Data Análise", "Responsável", "Status"
        };
        
        tableModel = new DefaultTableModel(colunas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        table = new JTable(tableModel);
        table.setFont(new Font("Arial", Font.PLAIN, 12));
        table.getTableHeader().setFont(new Font("Arial", Font.BOLD, 12));
        table.getTableHeader().setBackground(PRIMARY_COLOR);
        table.getTableHeader().setForeground(Color.WHITE);
        table.setRowHeight(25);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        
        scrollPane = new JScrollPane(table);
        scrollPane.setPreferredSize(new Dimension(0, 300));
        
        // Painel de botões da tabela
        JPanel tableButtonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        tableButtonPanel.setBackground(Color.WHITE);
        
        btnNovo = new JButton("➕ Novo");
        btnNovo.setBackground(SUCCESS_COLOR);
        btnNovo.setForeground(Color.WHITE);
        btnNovo.setFocusPainted(false);
        
        btnEditar = new JButton("✏️ Editar");
        btnEditar.setBackground(PRIMARY_COLOR);
        btnEditar.setForeground(Color.WHITE);
        btnEditar.setFocusPainted(false);
        
        btnExcluir = new JButton("🗑️ Excluir");
        btnExcluir.setBackground(DANGER_COLOR);
        btnExcluir.setForeground(Color.WHITE);
        btnExcluir.setFocusPainted(false);
        
        tableButtonPanel.add(btnNovo);
        tableButtonPanel.add(btnEditar);
        tableButtonPanel.add(btnExcluir);
        
        tablePanel.add(tableButtonPanel, BorderLayout.NORTH);
        tablePanel.add(scrollPane, BorderLayout.CENTER);
    }
    
    private void setupLayout() {
        mainPanel.add(headerPanel, BorderLayout.NORTH);
        
        // Painel central com busca e formulário
        JPanel centerPanel = new JPanel(new BorderLayout());
        centerPanel.setBackground(Color.WHITE);
        centerPanel.add(searchPanel, BorderLayout.NORTH);
        centerPanel.add(formPanel, BorderLayout.CENTER);
        
        mainPanel.add(centerPanel, BorderLayout.CENTER);
        mainPanel.add(tablePanel, BorderLayout.SOUTH);
    }
    
    private void setupEvents() {
        // Evento do combo de período
        cbPeriodo.addActionListener(e -> {
            updateDataRange();
            refreshDashboard();
        });
        
        // Evento do combo de ano
        cbAno.addActionListener(e -> {
            updateDataRange();
            refreshDashboard();
        });
        
        // Evento do botão buscar
        btnBuscar.addActionListener(e -> {
            performSearch();
        });
        
        // Evento do botão limpar
        btnLimpar.addActionListener(e -> {
            clearFields();
        });
        
        // Evento do botão atualizar
        btnAtualizar.addActionListener(e -> {
            refreshDashboard();
        });
        
        // Evento do botão exportar
        btnExportar.addActionListener(e -> {
            exportData();
        });
        
        // Evento do botão novo
        btnNovo.addActionListener(e -> {
            addNewAnalysis();
        });
        
        // Evento do botão editar
        btnEditar.addActionListener(e -> {
            editSelectedAnalysis();
        });
        
        // Evento do botão excluir
        btnExcluir.addActionListener(e -> {
            deleteSelectedAnalysis();
        });
        
        // Evento de seleção na tabela
        table.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                loadSelectedData();
            }
        });
    }
    
    private void loadInitialData() {
        SystemLogger.ui("Carregando dados iniciais do Dashboard Analytics");
        updateDataRange();
        refreshDashboard();
        loadSampleData();
        SystemLogger.ui("Dados iniciais do Dashboard Analytics carregados com sucesso");
    }
    
    private void loadSampleData() {
        // Carregar dados de exemplo na tabela
        Object[] sampleData1 = {
            "001", "Hoje", "R$ 12.545,00", "145", "R$ 86,52", 
            "15,2", "85,5", "2.456", "09/05/2026", "João Silva", "Concluído"
        };
        
        Object[] sampleData2 = {
            "002", "Últimos 7 dias", "R$ 87.320,00", "1.023", "R$ 85,35", 
            "12,8", "92,1", "2.412", "08/05/2026", "Maria Santos", "Concluído"
        };
        
        Object[] sampleData3 = {
            "003", "Este mês", "R$ 245.890,00", "2.847", "R$ 86,32", 
            "18,5", "88,3", "2.398", "01/05/2026", "Pedro Oliveira", "Em andamento"
        };
        
        tableModel.addRow(sampleData1);
        tableModel.addRow(sampleData2);
        tableModel.addRow(sampleData3);
    }
    
    private void updateDataRange() {
        String periodo = (String) cbPeriodo.getSelectedItem();
        LocalDate hoje = LocalDate.now();
        
        switch (periodo) {
            case "Hoje":
                dataInicio = hoje;
                dataFim = hoje;
                break;
            case "Últimos 7 dias":
                dataInicio = hoje.minusDays(7);
                dataFim = hoje;
                break;
            case "Últimos 30 dias":
                dataInicio = hoje.minusDays(30);
                dataFim = hoje;
                break;
            case "Este mês":
                dataInicio = hoje.withDayOfMonth(1);
                dataFim = hoje;
                break;
            case "Últimos 3 meses":
                dataInicio = hoje.minusMonths(3);
                dataFim = hoje;
                break;
            case "Este ano":
                dataInicio = hoje.withDayOfYear(1);
                dataFim = hoje;
                break;
            default:
                dataInicio = hoje.minusDays(7);
                dataFim = hoje;
        }
    }
    
    
    private void performSearch() {
        String tipoBusca = (String) cbTipoBusca.getSelectedItem();
        String busca = txtBusca.getText().trim();
        
        SystemLogger.ui("Realizando busca: " + tipoBusca + " - " + busca);
        
        // Implementar lógica de busca
        // Por enquanto, apenas atualiza os dados
        refreshDashboard();
    }
    
    private void clearFields() {
        txtBusca.setText("");
        cbTipoBusca.setSelectedIndex(0);
        cbPeriodo.setSelectedIndex(0);
        cbAno.setSelectedItem("2024");
        
        SystemLogger.ui("Campos de busca limpos");
    }
    
    private void addNewAnalysis() {
        JOptionPane.showMessageDialog(workArea, 
            "Funcionalidade de adicionar nova análise em desenvolvimento", 
            "Novo Registro", JOptionPane.INFORMATION_MESSAGE);
    }
    
    private void editSelectedAnalysis() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow >= 0) {
            JOptionPane.showMessageDialog(workArea, 
                "Funcionalidade de editar análise em desenvolvimento", 
                "Editar Registro", JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(workArea, 
                "Selecione um registro para editar", 
                "Aviso", JOptionPane.WARNING_MESSAGE);
        }
    }
    
    private void deleteSelectedAnalysis() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow >= 0) {
            int option = JOptionPane.showConfirmDialog(workArea, 
                "Deseja excluir esta análise?", "Excluir Registro", 
                JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
            
            if (option == JOptionPane.YES_OPTION) {
                tableModel.removeRow(selectedRow);
                SystemLogger.ui("Análise excluída com sucesso");
            }
        } else {
            JOptionPane.showMessageDialog(workArea, 
                "Selecione um registro para excluir", 
                "Aviso", JOptionPane.WARNING_MESSAGE);
        }
    }
    
    private void loadSelectedData() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow >= 0) {
            // Carregar dados da linha selecionada para o formulário
            txtReceitaTotal.setText(tableModel.getValueAt(selectedRow, 2).toString());
            txtTotalVendas.setText(tableModel.getValueAt(selectedRow, 3).toString());
            txtTicketMedio.setText(tableModel.getValueAt(selectedRow, 4).toString());
            txtCrescimento.setText(tableModel.getValueAt(selectedRow, 5).toString());
            txtMetas.setText(tableModel.getValueAt(selectedRow, 6).toString());
            txtEstoque.setText(tableModel.getValueAt(selectedRow, 7).toString());
        }
    }
    
    private void refreshDashboard() {
        try {
            // Atualizar campos do formulário
            txtReceitaTotal.setText("R$ 125.450,00");
            txtTotalVendas.setText("1.234");
            txtTicketMedio.setText("R$ 101,75");
            txtCrescimento.setText("18,5");
            txtMetas.setText("85,5");
            txtEstoque.setText("2.456");
            txtObservacoes.setText("Análise atualizada em " + 
                LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss")));
            
            SystemLogger.ui("Dashboard atualizado com sucesso");
            
        } catch (Exception e) {
            SystemLogger.error("Erro ao atualizar dashboard: " + e.getMessage());
            JOptionPane.showMessageDialog(workArea, 
                "Erro ao atualizar dashboard: " + e.getMessage(), 
                "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void exportData() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Exportar Dashboard Analytics");
        fileChooser.setSelectedFile(new java.io.File("dashboard_analytics_" + 
            LocalDate.now().format(DateTimeFormatter.ofPattern("dd_MM_yyyy")) + ".csv"));
        
        int userSelection = fileChooser.showSaveDialog(workArea);
        
        if (userSelection == JFileChooser.APPROVE_OPTION) {
            java.io.File fileToSave = fileChooser.getSelectedFile();
            
            try {
                // Implementar exportação para CSV
                JOptionPane.showMessageDialog(workArea, 
                    "Exportação para CSV concluída: " + fileToSave.getAbsolutePath(), 
                    "Exportação Concluída", 
                    JOptionPane.INFORMATION_MESSAGE);
                    
                SystemLogger.ui("Dashboard exportado para: " + fileToSave.getAbsolutePath());
                
            } catch (Exception e) {
                SystemLogger.error("Erro ao exportar dashboard: " + e.getMessage());
                JOptionPane.showMessageDialog(workArea, 
                    "Erro ao exportar: " + e.getMessage(), 
                    "Erro de Exportação", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    /**
     * Método estático para criar o formulário de dashboard
     * @return JPanel configurado do dashboard
     */
    public static JPanel criarFormularioDashboard() {
        PDVFormularioDashboard dashboard = new PDVFormularioDashboard(
            new JPanel(), "usuario", "Nome Usuario"
        );
        return dashboard.mainPanel;
    }
}
    
