package com.br.hermescomercial.pdv.controller;

import com.br.hermescomercial.util.SystemLogger;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * Classe base abstrata para formulários especializados
 * Contém todos os componentes comuns para a estrutura Header → Busca → Formulário → Tabela
 * @author Hermes Comercial
 * @version 3.0.0
 */
public abstract class BaseFormulario {
    
    // Componentes principais comuns a todos os formulários
    protected JPanel workArea;
    protected String usuarioAtual;
    protected String nomeUsuario;
    
    protected JPanel mainPanel;
    protected JPanel headerPanel;
    protected JPanel searchPanel;
    protected JPanel formPanel;
    protected JPanel tablePanel;
    
    // Componentes de busca comuns
    protected JTextField txtBusca;
    protected JComboBox<String> cbTipoBusca;
    protected JButton btnBuscar;
    protected JButton btnLimpar;
    
    // Componentes da tabela comuns
    protected JTable table;
    protected DefaultTableModel tableModel;
    protected JScrollPane scrollPane;
    
    // Botões de ação comuns
    protected JButton btnNovo;
    protected JButton btnEditar;
    protected JButton btnExcluir;
    protected JButton btnAtualizar;
    protected JButton btnExportar;
    
    // Cores padrão
    protected static final Color PRIMARY_COLOR = new Color(33, 150, 243);
    protected static final Color SUCCESS_COLOR = new Color(76, 175, 80);
    protected static final Color WARNING_COLOR = new Color(255, 193, 7);
    protected static final Color DANGER_COLOR = new Color(244, 67, 54);
    protected static final Color WHITE = Color.WHITE;
    protected static final Color LIGHT_GRAY = new Color(240, 240, 240);
    
    /**
     * Construtor base para todos os formulários
     * @param workArea Área de trabalho onde o formulário será exibido
     * @param usuario Usuário atual
     * @param nome Nome do usuário
     */
    public BaseFormulario(JPanel workArea, String usuario, String nome) {
        this.workArea = workArea;
        this.usuarioAtual = usuario;
        this.nomeUsuario = nome;
        
        initializeComponents();
        setupLayout();
        setupEvents();
        loadInitialData();
    }
    
    /**
     * Inicializa os componentes comuns
     */
    protected void initializeComponents() {
        SystemLogger.ui("[BASE] Inicializando componentes base do formulário: " + getFormTitle());
        
        try {
            // Painel principal
            SystemLogger.ui("[BASE] Criando painel principal...");
            mainPanel = new JPanel(new BorderLayout());
            mainPanel.setBackground(WHITE);
            mainPanel.setBorder(new EmptyBorder(20, 20, 20, 20));
            SystemLogger.ui("[BASE] Painel principal criado com sucesso");
            
            // Adicionar listener de foco e visibilidade ao painel principal
            addActivityListeners(mainPanel);
            
            // Header
            SystemLogger.ui("[BASE] Criando header...");
            createHeaderPanel();
            SystemLogger.ui("[BASE] Header criado com sucesso");
            
            // Painel de busca
            SystemLogger.ui("[BASE] Criando painel de busca...");
            createSearchPanel();
            SystemLogger.ui("[BASE] Painel de busca criado com sucesso");
            
            // Painel de formulário (implementado pelas classes filhas)
            SystemLogger.ui("[BASE] Criando painel de formulário...");
            createFormPanel();
            SystemLogger.ui("[BASE] Painel de formulário criado com sucesso");
            
            // Painel de tabela
            SystemLogger.ui("[BASE] Criando painel de tabela...");
            createTablePanel();
            SystemLogger.ui("[BASE] Painel de tabela criado com sucesso");
            
            SystemLogger.ui("[BASE] Componentes base inicializados com sucesso");
            
        } catch (Exception e) {
            SystemLogger.error("[BASE] Erro ao inicializar componentes: " + e.getMessage());
            SystemLogger.error("[BASE] Stack trace: " + e.toString());
            throw new RuntimeException("Falha ao inicializar componentes base", e);
        }
    }
    
    /**
     * Adiciona listeners para monitorar atividade da tela
     */
    private void addActivityListeners(JPanel panel) {
        try {
            SystemLogger.ui("[BASE] Adicionando listeners de atividade ao painel: " + getFormTitle());
            
            // Listener de foco
            panel.addFocusListener(new java.awt.event.FocusAdapter() {
                public void focusGained(java.awt.event.FocusEvent e) {
                    SystemLogger.ui("[ATIVIDADE] [" + getFormTitle() + "] Foco GANHO - tela está ATIVA");
                    logScreenStatus("ATIVA");
                }
                
                public void focusLost(java.awt.event.FocusEvent e) {
                    SystemLogger.ui("[ATIVIDADE] [" + getFormTitle() + "] Foco PERDIDO - tela está INATIVA");
                    logScreenStatus("INATIVA");
                }
            });
            
            // Listener de componente mostrado/escondido
            panel.addComponentListener(new java.awt.event.ComponentAdapter() {
                public void componentShown(java.awt.event.ComponentEvent e) {
                    SystemLogger.ui("[ATIVIDADE] [" + getFormTitle() + "] Componente MOSTRADO - tela VISÍVEL");
                    logScreenStatus("VISÍVEL");
                }
                
                public void componentHidden(java.awt.event.ComponentEvent e) {
                    SystemLogger.ui("[ATIVIDADE] [" + getFormTitle() + "] Componente ESCONDIDO - tela INVISÍVEL");
                    logScreenStatus("INVISÍVEL");
                }
            });
            
            // Listener de mouse para detectar interação
            panel.addMouseListener(new java.awt.event.MouseAdapter() {
                public void mouseEntered(java.awt.event.MouseEvent e) {
                    SystemLogger.ui("[ATIVIDADE] [" + getFormTitle() + "] Mouse ENTROU - usuário interagindo");
                }
                
                public void mouseClicked(java.awt.event.MouseEvent e) {
                    SystemLogger.ui("[ATIVIDADE] [" + getFormTitle() + "] Mouse CLICADO - tela ATIVA");
                    logScreenStatus("ATIVA");
                }
            });
            
            SystemLogger.ui("[BASE] Listeners de atividade adicionados com sucesso");
            
        } catch (Exception e) {
            SystemLogger.error("[BASE] Erro ao adicionar listeners de atividade: " + e.getMessage());
        }
    }
    
    /**
     * Registra o status atual da tela
     */
    private void logScreenStatus(String status) {
        String timestamp = java.time.LocalDateTime.now().format(
            java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss"));
        SystemLogger.ui("[STATUS] [" + getFormTitle() + "] Status: " + status + " - Timestamp: " + timestamp);
        
        // Verificar se o workArea está acessível
        if (workArea != null && workArea.isVisible()) {
            SystemLogger.ui("[STATUS] [" + getFormTitle() + "] WorkArea está visível e acessível");
        } else {
            SystemLogger.ui("[STATUS] [" + getFormTitle() + "] WorkArea não está visível ou é nulo");
        }
    }
    
    /**
     * Cria o painel de header padrão
     */
    protected void createHeaderPanel() {
        headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(PRIMARY_COLOR);
        headerPanel.setBorder(new EmptyBorder(15, 20, 15, 20));
        headerPanel.setPreferredSize(new Dimension(0, 80));
        
        // Título
        JLabel titleLabel = new JLabel(getFormTitle());
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setForeground(WHITE);
        
        // Subtítulo
        JLabel subtitleLabel = new JLabel(getFormSubtitle());
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
        btnAtualizar.setForeground(WHITE);
        btnAtualizar.setFocusPainted(false);
        btnAtualizar.setBorder(BorderFactory.createEmptyBorder(8, 16, 8, 16));
        
        btnExportar = new JButton("📥 Exportar");
        btnExportar.setBackground(PRIMARY_COLOR);
        btnExportar.setForeground(WHITE);
        btnExportar.setFocusPainted(false);
        btnExportar.setBorder(BorderFactory.createEmptyBorder(8, 16, 8, 16));
        
        buttonPanel.add(btnAtualizar);
        buttonPanel.add(btnExportar);
        
        headerPanel.add(titlePanel, BorderLayout.WEST);
        headerPanel.add(buttonPanel, BorderLayout.EAST);
    }
    
    /**
     * Cria o painel de busca padrão
     */
    protected void createSearchPanel() {
        searchPanel = new JPanel(new BorderLayout());
        searchPanel.setBackground(LIGHT_GRAY);
        searchPanel.setBorder(BorderFactory.createTitledBorder("🔍 Filtros de Busca"));
        
        // Painel de campos
        JPanel fieldsPanel = new JPanel(new GridLayout(2, 3, 10, 10));
        fieldsPanel.setBackground(LIGHT_GRAY);
        
        // Tipo de busca
        fieldsPanel.add(new JLabel("Tipo de Busca:"));
        String[] tiposBusca = getTiposBusca();
        cbTipoBusca = new JComboBox<>(tiposBusca);
        fieldsPanel.add(cbTipoBusca);
        
        // Campo de busca
        fieldsPanel.add(new JLabel("Buscar:"));
        txtBusca = new JTextField();
        fieldsPanel.add(txtBusca);
        
        // Campos adicionais (implementados pelas classes filhas)
        addAdditionalSearchFields(fieldsPanel);
        
        // Painel de botões
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        buttonPanel.setBackground(LIGHT_GRAY);
        
        btnBuscar = new JButton("🔍 Buscar");
        btnBuscar.setBackground(PRIMARY_COLOR);
        btnBuscar.setForeground(WHITE);
        btnBuscar.setFocusPainted(false);
        
        btnLimpar = new JButton("🧹 Limpar");
        btnLimpar.setBackground(WARNING_COLOR);
        btnLimpar.setForeground(WHITE);
        btnLimpar.setFocusPainted(false);
        
        buttonPanel.add(btnBuscar);
        buttonPanel.add(btnLimpar);
        
        searchPanel.add(fieldsPanel, BorderLayout.CENTER);
        searchPanel.add(buttonPanel, BorderLayout.SOUTH);
    }
    
    /**
     * Cria o painel de tabela padrão
     */
    protected void createTablePanel() {
        tablePanel = new JPanel(new BorderLayout());
        tablePanel.setBackground(WHITE);
        tablePanel.setBorder(BorderFactory.createTitledBorder("📋 " + getTableTitle()));
        
        // Modelo da tabela
        String[] colunas = getTableColumns();
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
        table.getTableHeader().setForeground(WHITE);
        table.setRowHeight(25);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        
        scrollPane = new JScrollPane(table);
        scrollPane.setPreferredSize(new Dimension(0, 300));
        
        // Painel de botões da tabela
        JPanel tableButtonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        tableButtonPanel.setBackground(WHITE);
        
        btnNovo = new JButton("➕ Novo");
        btnNovo.setBackground(SUCCESS_COLOR);
        btnNovo.setForeground(WHITE);
        btnNovo.setFocusPainted(false);
        
        btnEditar = new JButton("✏️ Editar");
        btnEditar.setBackground(PRIMARY_COLOR);
        btnEditar.setForeground(WHITE);
        btnEditar.setFocusPainted(false);
        
        btnExcluir = new JButton("🗑️ Excluir");
        btnExcluir.setBackground(DANGER_COLOR);
        btnExcluir.setForeground(WHITE);
        btnExcluir.setFocusPainted(false);
        
        tableButtonPanel.add(btnNovo);
        tableButtonPanel.add(btnEditar);
        tableButtonPanel.add(btnExcluir);
        
        tablePanel.add(tableButtonPanel, BorderLayout.NORTH);
        tablePanel.add(scrollPane, BorderLayout.CENTER);
    }
    
    /**
     * Configura o layout padrão Header → Busca → Formulário → Tabela
     */
    protected void setupLayout() {
        // Estrutura Header → Busca → Formulário → Tabela
        mainPanel.add(headerPanel, BorderLayout.NORTH);
        
        // Painel para Busca + Formulário
        JPanel centerPanel = new JPanel(new BorderLayout());
        centerPanel.setBackground(WHITE);
        centerPanel.add(searchPanel, BorderLayout.NORTH);
        centerPanel.add(formPanel, BorderLayout.CENTER);
        
        mainPanel.add(centerPanel, BorderLayout.CENTER);
        mainPanel.add(tablePanel, BorderLayout.SOUTH);
    }
    
    /**
     * Configura os eventos padrão
     */
    protected void setupEvents() {
        // Evento do botão buscar
        btnBuscar.addActionListener(e -> performSearch());
        
        // Evento do botão limpar
        btnLimpar.addActionListener(e -> clearFields());
        
        // Evento do botão atualizar
        btnAtualizar.addActionListener(e -> refreshData());
        
        // Evento do botão exportar
        btnExportar.addActionListener(e -> exportData());
        
        // Evento do botão novo
        btnNovo.addActionListener(e -> addNew());
        
        // Evento do botão editar
        btnEditar.addActionListener(e -> editSelected());
        
        // Evento do botão excluir
        btnExcluir.addActionListener(e -> deleteSelected());
        
        // Evento de seleção na tabela
        table.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                loadSelectedData();
            }
        });
        
        // Eventos adicionais (implementados pelas classes filhas)
        setupAdditionalEvents();
    }
    
    /**
     * Carrega os dados iniciais
     */
    protected void loadInitialData() {
        SystemLogger.ui("Carregando dados iniciais do formulário: " + getFormTitle());
        refreshData();
        loadSampleData();
        SystemLogger.ui("Dados iniciais carregados com sucesso");
    }
    
    /**
     * Limpa os campos de busca
     */
    protected void clearFields() {
        txtBusca.setText("");
        cbTipoBusca.setSelectedIndex(0);
        clearAdditionalFields();
        refreshData();
    }
    
    /**
     * Exporta os dados para CSV
     */
    protected void exportData() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Exportar " + getFormTitle());
        fileChooser.setSelectedFile(new java.io.File(getFileName() + 
            LocalDate.now().format(DateTimeFormatter.ofPattern("dd_MM_yyyy")) + ".csv"));
        
        int userSelection = fileChooser.showSaveDialog(workArea);
        
        if (userSelection == JFileChooser.APPROVE_OPTION) {
            java.io.File fileToSave = fileChooser.getSelectedFile();
            
            try {
                performExport(fileToSave);
                JOptionPane.showMessageDialog(workArea, 
                    "Exportação concluída: " + fileToSave.getAbsolutePath(), 
                    "Exportação Concluída", 
                    JOptionPane.INFORMATION_MESSAGE);
                    
                SystemLogger.ui("Dados exportados para: " + fileToSave.getAbsolutePath());
                
            } catch (Exception e) {
                SystemLogger.error("Erro ao exportar: " + e.getMessage());
                JOptionPane.showMessageDialog(workArea, 
                    "Erro ao exportar: " + e.getMessage(), 
                    "Erro de Exportação", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    /**
     * Exclui o item selecionado
     */
    protected void deleteSelected() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow >= 0) {
            int confirmacao = JOptionPane.showConfirmDialog(workArea, 
                "Deseja excluir este registro?", 
                "Confirmar Exclusão", JOptionPane.YES_NO_OPTION);
            
            if (confirmacao == JOptionPane.YES_OPTION) {
                tableModel.removeRow(selectedRow);
                JOptionPane.showMessageDialog(workArea, "Registro excluído com sucesso!", 
                    "Sucesso", JOptionPane.INFORMATION_MESSAGE);
                refreshData();
            }
        } else {
            JOptionPane.showMessageDialog(workArea, 
                "Selecione um registro para excluir", 
                "Aviso", JOptionPane.WARNING_MESSAGE);
        }
    }
    
    // Métodos abstratos que devem ser implementados pelas classes filhas
    
    /**
     * Retorna o título do formulário
     */
    protected abstract String getFormTitle();
    
    /**
     * Retorna o subtítulo do formulário
     */
    protected abstract String getFormSubtitle();
    
    /**
     * Retorna o título da tabela
     */
    protected abstract String getTableTitle();
    
    /**
     * Retorna as colunas da tabela
     */
    protected abstract String[] getTableColumns();
    
    /**
     * Cria o painel de formulário específico
     */
    protected abstract void createFormPanel();
    
    /**
     * Retorna os tipos de busca disponíveis
     */
    protected abstract String[] getTiposBusca();
    
    /**
     * Adiciona campos adicionais de busca (opcional)
     */
    protected void addAdditionalSearchFields(JPanel fieldsPanel) {
        // Implementação padrão vazia
    }
    
    /**
     * Configura eventos adicionais (opcional)
     */
    protected void setupAdditionalEvents() {
        // Implementação padrão vazia
    }
    
    /**
     * Limpa campos adicionais (opcional)
     */
    protected void clearAdditionalFields() {
        // Implementação padrão vazia
    }
    
    /**
     * Realiza a busca específica
     */
    protected abstract void performSearch();
    
    /**
     * Atualiza os dados específicos
     */
    protected abstract void refreshData();
    
    /**
     * Carrega os dados de exemplo
     */
    protected abstract void loadSampleData();
    
    /**
     * Carrega os dados do item selecionado
     */
    protected abstract void loadSelectedData();
    
    /**
     * Adiciona um novo registro
     */
    protected abstract void addNew();
    
    /**
     * Edita o registro selecionado
     */
    protected abstract void editSelected();
    
    /**
     * Realiza a exportação específica
     */
    protected abstract void performExport(java.io.File file) throws Exception;
    
    /**
     * Retorna o nome base para exportação
     */
    protected abstract String getFileName();
    
    /**
     * Retorna o painel principal do formulário
     */
    public JPanel getMainPanel() {
        return mainPanel;
    }
}
