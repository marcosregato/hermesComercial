package com.br.hermescomercial.controller;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import com.br.hermescomercial.util.LoggerUtil;
import com.br.hermescomercial.util.LogViewer;

/**
 * Controller principal do PDV em SWING
 * Interface principal do sistema PDV
 * Versão 2.0 - 100% SWING - Sem JavaFX
 */
public class PDVPrincipalSwingController {
    
    private JFrame mainFrame;
    private JPanel mainPanel;
    private JLabel statusLabel;
    private JTextArea produtosArea;
    private JButton btnNovaVenda;
    private JButton btnConsultarProduto;
    private JButton btnClientes;
    private JButton btnCaixa;
    private JButton btnRelatorios;
    private JButton btnConfiguracoes;
    
    public PDVPrincipalSwingController() {
        initializeUI();
    }
    
    private void initializeUI() {
        // Aplicar tema moderno
        mainFrame = new JFrame("Hermes Comercial PDV v2.0.0 - Premium");
        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainFrame.setSize(1200, 800);
        mainFrame.setLocationRelativeTo(null);
        
        // Configurar fundo elegante - padrão Nova Venda
        mainFrame.getContentPane().setBackground(new Color(245, 245, 250));
        
        createMenuBar();
        createMainPanel();
        mainFrame.add(mainPanel);
    }
    
    private void createMenuBar() {
        JMenuBar menuBar = new JMenuBar();
        
        // Menu Arquivo
        JMenu fileMenu = new JMenu("Arquivo");
        fileMenu.add(createMenuItem("Nova Venda", this::abrirNovaVenda));
        fileMenu.add(createMenuItem("Abrir Caixa", e -> {
            try {
                PDVCaixaSwingController caixaController = new PDVCaixaSwingController();
                caixaController.show();
                statusLabel.setText("Caixa aberto");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(mainFrame, 
                    "Erro ao abrir caixa: " + ex.getMessage(),
                    "Caixa", JOptionPane.ERROR_MESSAGE);
            }
        }));
        fileMenu.addSeparator();
        fileMenu.add(createMenuItem("Sair", e -> System.exit(0)));
        
        // Menu Operações
        JMenu operationsMenu = new JMenu("Operações");
        operationsMenu.add(createMenuItem("Consultar Produtos", this::consultarProdutos));
        operationsMenu.add(createMenuItem("Cadastrar Produto", this::cadastrarProduto));
        operationsMenu.add(createMenuItem("Clientes", this::gerenciarClientes));
        
        // Menu Relatórios
        JMenu reportsMenu = new JMenu("Relatórios");
        reportsMenu.add(createMenuItem("Vendas do Dia", this::relatorioVendasDia));
        reportsMenu.add(createMenuItem("Produtos", this::relatorioProdutos));
        reportsMenu.add(createMenuItem("Financeiro", this::relatorioFinanceiro));
        
        // Menu Ferramentas
        JMenu toolsMenu = new JMenu("Ferramentas");
        toolsMenu.add(createMenuItem("Visualizar Logs", e -> verLogs(e)));
        toolsMenu.add(createMenuItem("Limpar Logs Antigos", e -> limparLogs(e)));
        
        // Menu Ajuda
        JMenu helpMenu = new JMenu("Ajuda");
        helpMenu.add(createMenuItem("Sobre", this::mostrarSobre));
        
        menuBar.add(fileMenu);
        menuBar.add(operationsMenu);
        menuBar.add(reportsMenu);
        menuBar.add(toolsMenu);
        menuBar.add(helpMenu);
        
        mainFrame.setJMenuBar(menuBar);
    }
    
    private JMenuItem createMenuItem(String text, ActionListener action) {
        JMenuItem item = new JMenuItem(text);
        item.addActionListener(action);
        return item;
    }
    
    private JPanel createHeaderPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 20, 10));
        panel.setBackground(new Color(41, 128, 185));
        panel.setPreferredSize(new Dimension(0, 80));
        
        // Título central
        JLabel titleLabel = new JLabel("Hermes Comercial PDV v2.0.0 - Premium", JLabel.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setForeground(Color.WHITE);
        
        // Botão voltar estilizado (não necessário na tela principal, mas mantido para consistência)
        JButton btnInfo = new JButton("ℹ️ Info");
        btnInfo.setBackground(new Color(255, 255, 255));
        btnInfo.setForeground(new Color(41, 128, 185));
        btnInfo.setFont(new Font("Arial", Font.BOLD, 12));
        btnInfo.setBorder(BorderFactory.createEmptyBorder(8, 15, 8, 15));
        btnInfo.setFocusPainted(false);
        btnInfo.addActionListener(e -> {
            JOptionPane.showMessageDialog(mainFrame, 
                "Hermes Comercial PDV v2.0 - Premium\n" +
                "Sistema de Ponto de Venda Completo\n\n" +
                "Funcionalidades:\n" +
                "• Gestão de Vendas\n" +
                "• Controle de Caixa\n" +
                "• Gestão de Clientes\n" +
                "• Relatórios\n" +
                "• Configurações",
                "Sobre", JOptionPane.INFORMATION_MESSAGE);
        });
        
        // Data e hora atual
        JLabel lblDataHora = new JLabel(java.time.LocalDateTime.now().format(java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")), JLabel.RIGHT);
        lblDataHora.setFont(new Font("Arial", Font.PLAIN, 12));
        lblDataHora.setForeground(Color.WHITE);
        
        panel.add(btnInfo, BorderLayout.WEST);
        panel.add(titleLabel, BorderLayout.CENTER);
        panel.add(lblDataHora, BorderLayout.EAST);
        
        return panel;
    }
    
    private JButton createStyledButton(String text, String tooltip, ActionListener action) {
        JButton button = new JButton(text);
        button.setBackground(new Color(41, 128, 185));
        button.setForeground(Color.WHITE);
        button.setFont(new Font("Arial", Font.BOLD, 12));
        button.setBorder(BorderFactory.createEmptyBorder(12, 20, 12, 20));
        button.setFocusPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setToolTipText(tooltip);
        button.addActionListener(action);
        
        // Efeito hover
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(new Color(52, 152, 219));
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(new Color(41, 128, 185));
            }
        });
        
        return button;
    }
    
    private void createMainPanel() {
        // Painel principal com design moderno
        mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        mainPanel.setBackground(new Color(245, 245, 250));
        
        // Header padrão Nova Venda
        JPanel headerPanel = createHeaderPanel();
        mainPanel.add(headerPanel, BorderLayout.NORTH);
        
        // Painel central com informações
        JPanel centerPanel = new JPanel(new BorderLayout());
        centerPanel.setBorder(BorderFactory.createTitledBorder("Painel de Controle"));
        
        // Status label
        statusLabel = new JLabel("Sistema pronto para uso");
        statusLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        statusLabel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        statusLabel.setBackground(Color.LIGHT_GRAY);
        statusLabel.setOpaque(true);
        centerPanel.add(statusLabel, BorderLayout.NORTH);
        
        produtosArea = new JTextArea();
        produtosArea.setEditable(false);
        produtosArea.setBackground(Color.WHITE);
        produtosArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
        JScrollPane scrollPane = new JScrollPane(produtosArea);
        centerPanel.add(scrollPane, BorderLayout.CENTER);
        
        mainPanel.add(centerPanel, BorderLayout.CENTER);
        
        // Painel de botões principais com design moderno
        JPanel buttonPanel = new JPanel(new GridLayout(3, 3, 20, 20));
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));
        buttonPanel.setBackground(new Color(245, 245, 250));
        
        // Botões principais com design padrão Nova Venda
        btnNovaVenda = createStyledButton("🛒 Nova Venda", "Iniciar nova venda", this::abrirNovaVenda);
        btnConsultarProduto = createStyledButton("📦 Consultar Produtos", "Consultar produtos", this::consultarProdutos);
        btnClientes = createStyledButton("👥 Clientes", "Gerenciar clientes", this::gerenciarClientes);
        btnCaixa = createStyledButton("💰 Caixa", "Operações de caixa", e -> {
            try {
                PDVCaixaSwingController caixaController = new PDVCaixaSwingController();
                caixaController.show();
                statusLabel.setText("Caixa aberto");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(mainFrame, 
                    "Erro ao abrir caixa: " + ex.getMessage(),
                    "Caixa", JOptionPane.ERROR_MESSAGE);
            }
        });
        btnRelatorios = createStyledButton("📊 Relatórios", "Gerar relatórios", this::verRelatorios);
        btnConfiguracoes = createStyledButton("⚙️ Configurações", "Configurar sistema", this::abrirConfiguracoes);
        
        buttonPanel.add(btnNovaVenda);
        buttonPanel.add(btnConsultarProduto);
        buttonPanel.add(btnClientes);
        buttonPanel.add(btnCaixa);
        buttonPanel.add(btnRelatorios);
        buttonPanel.add(btnConfiguracoes);
        
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);
    }
    
        
    // Métodos de ação
    private void abrirNovaVenda(ActionEvent e) {
        try {
            LoggerUtil.info("Iniciando nova venda");
            LoggerUtil.logUserAction("Usuário", "NOVA_VENDA", "Tela de vendas aberta");
            PDVVendaSwingController vendaController = new PDVVendaSwingController();
            vendaController.show();
            statusLabel.setText("Nova venda iniciada");
        } catch (Exception ex) {
            LoggerUtil.error("Erro ao abrir tela de venda", ex);
            JOptionPane.showMessageDialog(mainFrame, 
                "Erro ao abrir tela de venda: " + ex.getMessage(),
                "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void consultarProdutos(ActionEvent e) {
        try {
            LoggerUtil.info("Acessando consulta de produtos");
            LoggerUtil.logUserAction("Usuário", "CONSULTAR_PRODUTOS", "Acesso ao módulo de consulta");
            
            // Criar frame para consulta de produtos com layout padrão
            JFrame consultaFrame = new JFrame("📦 Consulta de Produtos v2.0.0 - Premium");
            consultaFrame.setSize(900, 700);
            consultaFrame.setLocationRelativeTo(mainFrame);
            consultaFrame.getContentPane().setBackground(new Color(245, 245, 250));
            
            // Header padrão Nova Venda
            JPanel headerPanel = createHeaderPanelForDialog(consultaFrame, "📦 Consulta de Produtos v2.0.0 - Premium");
            
            // Painel principal com fundo padrão
            JPanel mainPanel = new JPanel(new BorderLayout());
            mainPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
            mainPanel.setBackground(new Color(245, 245, 250));
            
            // Painel de busca estilizado
            JPanel buscaPanel = new JPanel(new BorderLayout());
            buscaPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
            buscaPanel.setBackground(new Color(255, 255, 255));
            
            JLabel lblBusca = new JLabel("🔍 Buscar Produto:");
            lblBusca.setFont(new Font("Arial", Font.BOLD, 14));
            lblBusca.setForeground(new Color(41, 128, 185));
            
            JPanel buscaInputPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
            buscaInputPanel.setBackground(new Color(255, 255, 255));
            JTextField txtBusca = new JTextField(25);
            txtBusca.setFont(new Font("Arial", Font.PLAIN, 12));
            txtBusca.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200), 1));
            JButton btnBuscar = createStyledButton("🔍 Buscar", "Buscar produtos", null);
            JButton btnLimpar = createStyledButton("🔄 Limpar", "Limpar busca", null);
            
            buscaInputPanel.add(txtBusca);
            buscaInputPanel.add(btnBuscar);
            buscaInputPanel.add(btnLimpar);
            
            buscaPanel.add(lblBusca, BorderLayout.NORTH);
            buscaPanel.add(buscaInputPanel, BorderLayout.CENTER);
            
            // Tabela de produtos com design melhorado
            String[] columns = {"Código", "Descrição", "Preço", "Estoque", "Categoria"};
            DefaultTableModel tableModel = new DefaultTableModel(columns, 0) {
                @Override
                public Class<?> getColumnClass(int columnIndex) {
                    if (columnIndex == 2) return Double.class; // Preço
                    if (columnIndex == 3) return Integer.class; // Estoque
                    return String.class;
                }
            };
            
            JTable produtosTable = new JTable(tableModel);
            produtosTable.setRowHeight(25);
            produtosTable.setFont(new Font("Arial", Font.PLAIN, 12));
            produtosTable.getTableHeader().setFont(new Font("Arial", Font.BOLD, 12));
            produtosTable.getTableHeader().setBackground(new Color(41, 128, 185));
            produtosTable.getTableHeader().setForeground(Color.WHITE);
            
            // Configurar larguras das colunas
            produtosTable.getColumnModel().getColumn(0).setPreferredWidth(80);
            produtosTable.getColumnModel().getColumn(1).setPreferredWidth(350);
            produtosTable.getColumnModel().getColumn(2).setPreferredWidth(100);
            produtosTable.getColumnModel().getColumn(3).setPreferredWidth(80);
            produtosTable.getColumnModel().getColumn(4).setPreferredWidth(120);
            
            // Dados de exemplo
            Object[][] dados = {
                {"001", "Notebook Dell Inspire 15", "3500.00", "10", "Informática"},
                {"002", "Mouse Wireless Logitech MX3", "89.90", "50", "Periféricos"},
                {"003", "Teclado Mecânico RGB Gamer", "250.00", "25", "Periféricos"},
                {"004", "Monitor 24\" LED Full HD", "899.00", "15", "Monitores"},
                {"005", "Webcam HD 1080p com Microfone", "150.00", "30", "Acessórios"}
            };
            
            // Adicionar dados à tabela
            for (Object[] row : dados) {
                tableModel.addRow(row);
            }
            
            JScrollPane scrollPane = new JScrollPane(produtosTable);
            scrollPane.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200), 1));
            scrollPane.setBackground(Color.WHITE);
            
            // Painel de botões estilizado
            JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 10));
            buttonPanel.setBackground(new Color(245, 245, 250));
            JButton btnFechar = createStyledButton("❌ Fechar", "Fechar consulta", ev -> consultaFrame.dispose());
            buttonPanel.add(btnFechar);
            
            // Montar layout
            JPanel contentPanel = new JPanel(new BorderLayout());
            contentPanel.setBackground(new Color(255, 255, 255));
            contentPanel.setBorder(BorderFactory.createTitledBorder("Lista de Produtos"));
            contentPanel.add(scrollPane, BorderLayout.CENTER);
            
            mainPanel.add(buscaPanel, BorderLayout.NORTH);
            mainPanel.add(contentPanel, BorderLayout.CENTER);
            mainPanel.add(buttonPanel, BorderLayout.SOUTH);
            
            // Ação do botão buscar
            btnBuscar.addActionListener(ev -> {
                String textoBusca = txtBusca.getText().toLowerCase();
                if (textoBusca.isEmpty()) {
                    // Restaurar todos os dados
                    tableModel.setRowCount(0);
                    for (Object[] row : dados) {
                        tableModel.addRow(row);
                    }
                } else {
                    // Filtrar dados
                    tableModel.setRowCount(0);
                    for (Object[] row : dados) {
                        for (Object cell : row) {
                            if (cell.toString().toLowerCase().contains(textoBusca)) {
                                tableModel.addRow(row);
                                break;
                            }
                        }
                    }
                }
            });
            
            // Ação do botão limpar
            btnLimpar.addActionListener(ev -> {
                txtBusca.setText("");
                tableModel.setRowCount(0);
                for (Object[] row : dados) {
                    tableModel.addRow(row);
                }
            });
            
            consultaFrame.add(headerPanel, BorderLayout.NORTH);
            consultaFrame.add(mainPanel, BorderLayout.CENTER);
            consultaFrame.setVisible(true);
            
            LoggerUtil.info("Consulta de produtos aberta com sucesso");
            statusLabel.setText("Consulta de produtos aberta");
            
        } catch (Exception ex) {
            LoggerUtil.error("Erro ao abrir consulta de produtos", ex);
            JOptionPane.showMessageDialog(mainFrame, 
                "Erro ao abrir consulta de produtos: " + ex.getMessage(),
                "Consulta de Produtos", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void cadastrarProduto(ActionEvent e) {
        try {
            LoggerUtil.info("Acessando cadastro de produtos");
            LoggerUtil.logUserAction("Usuário", "CADASTRAR_PRODUTO", "Acesso ao módulo de produtos");
            JOptionPane.showMessageDialog(mainFrame, 
                "Funcionalidade em desenvolvimento.",
                "Cadastrar Produto", JOptionPane.INFORMATION_MESSAGE);
        } catch (Exception ex) {
            LoggerUtil.error("Erro ao acessar cadastro de produtos", ex);
        }
    }
    
    private JPanel createHeaderPanelForDialog(JFrame frame, String title) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 20, 10));
        panel.setBackground(new Color(41, 128, 185));
        panel.setPreferredSize(new Dimension(0, 80));
        
        // Título central
        JLabel titleLabel = new JLabel(title, JLabel.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setForeground(Color.WHITE);
        
        // Botão voltar estilizado
        JButton btnVoltar = new JButton("← Voltar");
        btnVoltar.setBackground(new Color(255, 255, 255));
        btnVoltar.setForeground(new Color(41, 128, 185));
        btnVoltar.setFont(new Font("Arial", Font.BOLD, 12));
        btnVoltar.setBorder(BorderFactory.createEmptyBorder(8, 15, 8, 15));
        btnVoltar.setFocusPainted(false);
        btnVoltar.addActionListener(e -> frame.dispose());
        
        // Data e hora atual
        JLabel lblDataHora = new JLabel(java.time.LocalDateTime.now().format(java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")), JLabel.RIGHT);
        lblDataHora.setFont(new Font("Arial", Font.PLAIN, 12));
        lblDataHora.setForeground(Color.WHITE);
        
        panel.add(btnVoltar, BorderLayout.WEST);
        panel.add(titleLabel, BorderLayout.CENTER);
        panel.add(lblDataHora, BorderLayout.EAST);
        
        return panel;
    }
    
    private void gerenciarClientes(ActionEvent e) {
        try {
            LoggerUtil.info("Abrindo gestão de clientes");
            LoggerUtil.logUserAction("Usuário", "GERENCIAR_CLIENTES", "Acesso ao módulo de clientes");
            
            // Criar janela de gestão de clientes com layout padrão
            JFrame clientesFrame = new JFrame("👥 Gestão de Clientes v2.0.0 - Premium");
            clientesFrame.setSize(1000, 700);
            clientesFrame.setLocationRelativeTo(mainFrame);
            clientesFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            clientesFrame.getContentPane().setBackground(new Color(245, 245, 250));
            
            // Header padrão Nova Venda
            JPanel headerPanel = createHeaderPanelForDialog(clientesFrame, "👥 Gestão de Clientes v2.0.0 - Premium");
            
            // Painel principal com fundo padrão
            JPanel mainPanel = new JPanel(new BorderLayout());
            mainPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
            mainPanel.setBackground(new Color(245, 245, 250));
            
            // Painel de busca e cadastro
            JPanel topPanel = new JPanel(new BorderLayout());
            
            // Painel de busca estilizado
            JPanel buscaPanel = new JPanel(new BorderLayout());
            buscaPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
            buscaPanel.setBackground(new Color(255, 255, 255));
            
            JLabel lblBusca = new JLabel("🔍 Buscar Cliente:");
            lblBusca.setFont(new Font("Arial", Font.BOLD, 14));
            lblBusca.setForeground(new Color(41, 128, 185));
            
            JPanel buscaInputPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
            buscaInputPanel.setBackground(new Color(255, 255, 255));
            JTextField txtBusca = new JTextField(25);
            txtBusca.setFont(new Font("Arial", Font.PLAIN, 12));
            txtBusca.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200), 1));
            JButton btnBuscar = createStyledButton("🔍 Buscar", "Buscar clientes", null);
            JButton btnLimpar = createStyledButton("🔄 Limpar", "Limpar busca", null);
            
            buscaInputPanel.add(txtBusca);
            buscaInputPanel.add(btnBuscar);
            buscaInputPanel.add(btnLimpar);
            
            buscaPanel.add(lblBusca, BorderLayout.NORTH);
            buscaPanel.add(buscaInputPanel, BorderLayout.CENTER);
            
            // Painel de botões de cadastro estilizado
            JPanel acaoPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
            acaoPanel.setBackground(new Color(255, 255, 255));
            JButton btnNovo = createStyledButton("➕ Novo Cliente", "Cadastrar novo cliente", null);
            JButton btnEditar = createStyledButton("✏️ Editar", "Editar cliente selecionado", null);
            JButton btnExcluir = createStyledButton("🗑️ Excluir", "Excluir cliente selecionado", null);
            JButton btnExportar = createStyledButton("📤 Exportar", "Exportar lista de clientes", null);
            
            acaoPanel.add(btnNovo);
            acaoPanel.add(btnEditar);
            acaoPanel.add(btnExcluir);
            acaoPanel.add(btnExportar);
            
            topPanel.add(buscaPanel, BorderLayout.NORTH);
            topPanel.add(acaoPanel, BorderLayout.SOUTH);
            
            // Tabela de clientes com design melhorado
            String[] columns = {"ID", "Nome", "CPF/CNPJ", "Telefone", "Email", "Status"};
            DefaultTableModel tableModel = new DefaultTableModel(columns, 0);
            JTable clientesTable = new JTable(tableModel);
            clientesTable.setRowHeight(25);
            clientesTable.setFont(new Font("Arial", Font.PLAIN, 12));
            clientesTable.getTableHeader().setFont(new Font("Arial", Font.BOLD, 12));
            clientesTable.getTableHeader().setBackground(new Color(41, 128, 185));
            clientesTable.getTableHeader().setForeground(Color.WHITE);
            
            // Configurar larguras das colunas
            clientesTable.getColumnModel().getColumn(0).setPreferredWidth(60);
            clientesTable.getColumnModel().getColumn(1).setPreferredWidth(250);
            clientesTable.getColumnModel().getColumn(2).setPreferredWidth(120);
            clientesTable.getColumnModel().getColumn(3).setPreferredWidth(120);
            clientesTable.getColumnModel().getColumn(4).setPreferredWidth(200);
            clientesTable.getColumnModel().getColumn(5).setPreferredWidth(80);
            
            // Adicionar dados de exemplo
            Object[][] dados = {
                {"001", "João Silva", "123.456.789-00", "(11) 9876-5432", "joao@email.com", "Ativo"},
                {"002", "Maria Santos", "987.654.321-00", "(11) 9123-4567", "maria@email.com", "Ativo"},
                {"003", "Pedro Costa", "456.789.123-00", "(11) 9888-7777", "pedro@email.com", "Inativo"},
                {"004", "Ana Oliveira", "789.123.456-00", "(11) 9555-4444", "ana@email.com", "Ativo"},
                {"005", "Carlos Ferreira", "321.654.987-00", "(11) 9333-2222", "carlos@email.com", "Ativo"}
            };
            
            for (Object[] row : dados) {
                tableModel.addRow(row);
            }
            
            JScrollPane scrollPane = new JScrollPane(clientesTable);
            scrollPane.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200), 1));
            scrollPane.setBackground(Color.WHITE);
            
            // Painel de botões inferiores estilizado
            JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 10));
            buttonPanel.setBackground(new Color(245, 245, 250));
            JButton btnFechar = createStyledButton("❌ Fechar", "Fechar gestão de clientes", ev -> clientesFrame.dispose());
            buttonPanel.add(btnFechar);
            
            // Montar layout
            JPanel contentPanel = new JPanel(new BorderLayout());
            contentPanel.setBackground(new Color(255, 255, 255));
            contentPanel.setBorder(BorderFactory.createTitledBorder("Lista de Clientes"));
            contentPanel.add(scrollPane, BorderLayout.CENTER);
            
            mainPanel.add(topPanel, BorderLayout.NORTH);
            mainPanel.add(contentPanel, BorderLayout.CENTER);
            mainPanel.add(buttonPanel, BorderLayout.SOUTH);
            
            clientesFrame.add(headerPanel, BorderLayout.NORTH);
            clientesFrame.add(mainPanel, BorderLayout.CENTER);
            clientesFrame.setVisible(true);
            
            // Ação do botão buscar
            btnBuscar.addActionListener(ev -> {
                String textoBusca = txtBusca.getText().toLowerCase();
                if (textoBusca.isEmpty()) {
                    // Restaurar todos os dados
                    tableModel.setRowCount(0);
                    for (Object[] row : dados) {
                        tableModel.addRow(row);
                    }
                } else {
                    // Filtrar dados
                    tableModel.setRowCount(0);
                    for (Object[] row : dados) {
                        for (Object cell : row) {
                            if (cell.toString().toLowerCase().contains(textoBusca)) {
                                tableModel.addRow(row);
                                break;
                            }
                        }
                    }
                }
            });
            
            // Ação do botão limpar
            btnLimpar.addActionListener(ev -> {
                txtBusca.setText("");
                tableModel.setRowCount(0);
                for (Object[] row : dados) {
                    tableModel.addRow(row);
                }
            });
            
            // Ação do botão novo cliente
            btnNovo.addActionListener(ev -> {
                criarNovoCliente(clientesFrame, tableModel);
            });
            
            // Ação do botão editar
            btnEditar.addActionListener(ev -> {
                int selectedRow = clientesTable.getSelectedRow();
                if (selectedRow >= 0) {
                    editarCliente(clientesFrame, tableModel, selectedRow);
                } else {
                    JOptionPane.showMessageDialog(clientesFrame, 
                        "Selecione um cliente para editar.",
                        "Editar Cliente", JOptionPane.WARNING_MESSAGE);
                }
            });
            
            // Ação do botão excluir
            btnExcluir.addActionListener(ev -> {
                int selectedRow = clientesTable.getSelectedRow();
                if (selectedRow >= 0) {
                    int confirm = JOptionPane.showConfirmDialog(clientesFrame, 
                        "Deseja excluir o cliente " + tableModel.getValueAt(selectedRow, 1) + "?",
                        "Excluir Cliente", JOptionPane.YES_NO_OPTION);
                    if (confirm == JOptionPane.YES_OPTION) {
                        tableModel.removeRow(selectedRow);
                        JOptionPane.showMessageDialog(clientesFrame, 
                            "Cliente excluído com sucesso!",
                            "Sucesso", JOptionPane.INFORMATION_MESSAGE);
                    }
                } else {
                    JOptionPane.showMessageDialog(clientesFrame, 
                        "Selecione um cliente para excluir.",
                        "Excluir Cliente", JOptionPane.WARNING_MESSAGE);
                }
            });
            
            // Ação do botão exportar
            btnExportar.addActionListener(ev -> {
                StringBuilder export = new StringBuilder();
                export.append("ID;Nome;CPF/CNPJ;Telefone;Email;Status\n");
                for (int i = 0; i < tableModel.getRowCount(); i++) {
                    for (int j = 0; j < tableModel.getColumnCount(); j++) {
                        export.append(tableModel.getValueAt(i, j));
                        if (j < tableModel.getColumnCount() - 1) {
                            export.append(";");
                        }
                    }
                    export.append("\n");
                }
                
                JOptionPane.showMessageDialog(clientesFrame, 
                    "Lista de clientes exportada com sucesso!\n\n" +
                    "Total de clientes: " + tableModel.getRowCount(),
                    "Exportar", JOptionPane.INFORMATION_MESSAGE);
            });
            
            LoggerUtil.info("Gestão de clientes aberta com sucesso");
            statusLabel.setText("Gestão de clientes aberta");
            
        } catch (Exception ex) {
            LoggerUtil.error("Erro ao abrir gestão de clientes", ex);
            JOptionPane.showMessageDialog(mainFrame, 
                "Erro ao abrir gestão de clientes: " + ex.getMessage(),
                "Gestão de Clientes", JOptionPane.ERROR_MESSAGE);
        }
    }
    
        
    private void abrirConfiguracoes(ActionEvent e) {
        try {
            LoggerUtil.info("Abrindo módulo de configurações");
            LoggerUtil.logUserAction("Usuário", "CONFIGURACOES", "Acesso ao módulo de configurações");
            PDVConfiguracoesSwingController configController = new PDVConfiguracoesSwingController();
            configController.show();
            statusLabel.setText("Configurações abertas");
        } catch (Exception ex) {
            LoggerUtil.error("Erro ao abrir configurações", ex);
            JOptionPane.showMessageDialog(mainFrame, 
                "Erro ao abrir configurações: " + ex.getMessage(),
                "Configurações", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void verRelatorios(ActionEvent e) {
        try {
            LoggerUtil.info("Abrindo módulo de relatórios");
            LoggerUtil.logUserAction("Usuário", "RELATORIOS", "Acesso ao módulo de relatórios");
            JOptionPane.showMessageDialog(mainFrame, 
                "Módulo de relatórios disponível no menu Relatórios",
                "Relatórios", JOptionPane.INFORMATION_MESSAGE);
        } catch (Exception ex) {
            LoggerUtil.error("Erro ao acessar relatórios", ex);
        }
    }
    
    private void relatorioVendasDia(ActionEvent e) {
        try {
            PDVRelatoriosSwingController relatoriosController = new PDVRelatoriosSwingController();
            relatoriosController.show();
            relatoriosController.abrirRelatorioVendasDia();
            LoggerUtil.info("Relatório de vendas do dia aberto");
        } catch (Exception ex) {
            LoggerUtil.error("Erro ao abrir relatório de vendas do dia", ex);
            JOptionPane.showMessageDialog(mainFrame, 
                "Erro ao abrir relatório: " + ex.getMessage(),
                "Relatórios", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void relatorioProdutos(ActionEvent e) {
        try {
            PDVRelatoriosSwingController relatoriosController = new PDVRelatoriosSwingController();
            relatoriosController.show();
            relatoriosController.abrirRelatorioProdutos();
            LoggerUtil.info("Relatório de produtos aberto");
        } catch (Exception ex) {
            LoggerUtil.error("Erro ao abrir relatório de produtos", ex);
            JOptionPane.showMessageDialog(mainFrame, 
                "Erro ao abrir relatório: " + ex.getMessage(),
                "Relatórios", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void relatorioFinanceiro(ActionEvent e) {
        try {
            PDVRelatoriosSwingController relatoriosController = new PDVRelatoriosSwingController();
            relatoriosController.show();
            relatoriosController.abrirRelatorioFinanceiro();
            LoggerUtil.info("Relatório financeiro aberto");
        } catch (Exception ex) {
            LoggerUtil.error("Erro ao abrir relatório financeiro", ex);
            JOptionPane.showMessageDialog(mainFrame, 
                "Erro ao abrir relatório: " + ex.getMessage(),
                "Relatórios", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void mostrarSobre(ActionEvent e) {
        JOptionPane.showMessageDialog(mainFrame, 
            "Hermes Comercial PDV v2.0\n" +
            "Sistema de Ponto de Venda completo\n" +
            "Desenvolvido em Java Swing\n" +
            "Versão Premium com Design Moderno",
            "Sobre", JOptionPane.INFORMATION_MESSAGE);
    }
    
    private void verLogs(ActionEvent e) {
        try {
            LoggerUtil.info("Abrindo visualizador de logs");
            LogViewer.showLogViewer();
        } catch (Exception ex) {
            LoggerUtil.error("Erro ao abrir visualizador de logs", ex);
            JOptionPane.showMessageDialog(mainFrame, 
                "Erro ao abrir visualizador de logs: " + ex.getMessage(),
                "Logs", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void limparLogs(ActionEvent e) {
        int result = JOptionPane.showConfirmDialog(mainFrame, 
            "Deseja limpar logs antigos (mais de 30 dias)?\n\n" +
            "Esta ação não pode ser desfeita.",
            "Limpar Logs", 
            JOptionPane.YES_NO_OPTION,
            JOptionPane.QUESTION_MESSAGE);
            
        if (result == JOptionPane.YES_OPTION) {
            try {
                LoggerUtil.cleanOldLogs();
                LoggerUtil.info("Logs antigos limpos pelo usuário");
                JOptionPane.showMessageDialog(mainFrame, 
                    "Logs antigos limpos com sucesso!",
                    "Limpar Logs", JOptionPane.INFORMATION_MESSAGE);
            } catch (Exception ex) {
                LoggerUtil.error("Erro ao limpar logs antigos", ex);
                JOptionPane.showMessageDialog(mainFrame, 
                    "Erro ao limpar logs: " + ex.getMessage(),
                    "Limpar Logs", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    private void criarNovoCliente(JFrame parentFrame, DefaultTableModel tableModel) {
        JDialog dialog = new JDialog(parentFrame, "Novo Cliente", true);
        dialog.setSize(400, 350);
        dialog.setLocationRelativeTo(parentFrame);
        
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
        // Campos do formulário
        gbc.gridx = 0; gbc.gridy = 0;
        panel.add(new JLabel("Nome:"), gbc);
        gbc.gridx = 1;
        JTextField txtNome = new JTextField(20);
        panel.add(txtNome, gbc);
        
        gbc.gridx = 0; gbc.gridy = 1;
        panel.add(new JLabel("CPF/CNPJ:"), gbc);
        gbc.gridx = 1;
        JTextField txtCpfCnpj = new JTextField(20);
        panel.add(txtCpfCnpj, gbc);
        
        gbc.gridx = 0; gbc.gridy = 2;
        panel.add(new JLabel("Telefone:"), gbc);
        gbc.gridx = 1;
        JTextField txtTelefone = new JTextField(20);
        panel.add(txtTelefone, gbc);
        
        gbc.gridx = 0; gbc.gridy = 3;
        panel.add(new JLabel("Email:"), gbc);
        gbc.gridx = 1;
        JTextField txtEmail = new JTextField(20);
        panel.add(txtEmail, gbc);
        
        gbc.gridx = 0; gbc.gridy = 4;
        panel.add(new JLabel("Status:"), gbc);
        gbc.gridx = 1;
        JComboBox<String> cbStatus = new JComboBox<>(new String[]{"Ativo", "Inativo"});
        panel.add(cbStatus, gbc);
        
        // Botões
        gbc.gridx = 0; gbc.gridy = 5; gbc.gridwidth = 2;
        JPanel buttonPanel = new JPanel(new FlowLayout());
        JButton btnSalvar = new JButton("Salvar");
        JButton btnCancelar = new JButton("Cancelar");
        
        btnSalvar.addActionListener(e -> {
            String nome = txtNome.getText().trim();
            String cpfCnpj = txtCpfCnpj.getText().trim();
            String telefone = txtTelefone.getText().trim();
            String email = txtEmail.getText().trim();
            String status = (String) cbStatus.getSelectedItem();
            
            // Validação básica
            if (nome.isEmpty()) {
                JOptionPane.showMessageDialog(dialog, "Nome é obrigatório!", "Erro", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            if (cpfCnpj.isEmpty()) {
                JOptionPane.showMessageDialog(dialog, "CPF/CNPJ é obrigatório!", "Erro", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            // Gerar ID sequencial
            int newId = tableModel.getRowCount() + 1;
            String idStr = String.format("%03d", newId);
            
            // Adicionar na tabela
            Object[] newRow = {idStr, nome, cpfCnpj, telefone, email, status};
            tableModel.addRow(newRow);
            
            LoggerUtil.info("Novo cliente criado: " + nome);
            LoggerUtil.logUserAction("Usuário", "NOVO_CLIENTE", "Cliente criado: " + nome);
            
            JOptionPane.showMessageDialog(dialog, "Cliente criado com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
            dialog.dispose();
        });
        
        btnCancelar.addActionListener(e -> dialog.dispose());
        
        buttonPanel.add(btnSalvar);
        buttonPanel.add(btnCancelar);
        panel.add(buttonPanel, gbc);
        
        dialog.add(panel);
        dialog.setVisible(true);
    }
    
    private void editarCliente(JFrame parentFrame, DefaultTableModel tableModel, int selectedRow) {
        JDialog dialog = new JDialog(parentFrame, "Editar Cliente", true);
        dialog.setSize(400, 350);
        dialog.setLocationRelativeTo(parentFrame);
        
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
        // Obter dados do cliente selecionado
        String id = (String) tableModel.getValueAt(selectedRow, 0);
        String nome = (String) tableModel.getValueAt(selectedRow, 1);
        String cpfCnpj = (String) tableModel.getValueAt(selectedRow, 2);
        String telefone = (String) tableModel.getValueAt(selectedRow, 3);
        String email = (String) tableModel.getValueAt(selectedRow, 4);
        String status = (String) tableModel.getValueAt(selectedRow, 5);
        
        // Campos do formulário com dados existentes
        gbc.gridx = 0; gbc.gridy = 0;
        panel.add(new JLabel("ID:"), gbc);
        gbc.gridx = 1;
        JLabel lblId = new JLabel(id);
        panel.add(lblId, gbc);
        
        gbc.gridx = 0; gbc.gridy = 1;
        panel.add(new JLabel("Nome:"), gbc);
        gbc.gridx = 1;
        JTextField txtNome = new JTextField(nome, 20);
        panel.add(txtNome, gbc);
        
        gbc.gridx = 0; gbc.gridy = 2;
        panel.add(new JLabel("CPF/CNPJ:"), gbc);
        gbc.gridx = 1;
        JTextField txtCpfCnpj = new JTextField(cpfCnpj, 20);
        panel.add(txtCpfCnpj, gbc);
        
        gbc.gridx = 0; gbc.gridy = 3;
        panel.add(new JLabel("Telefone:"), gbc);
        gbc.gridx = 1;
        JTextField txtTelefone = new JTextField(telefone, 20);
        panel.add(txtTelefone, gbc);
        
        gbc.gridx = 0; gbc.gridy = 4;
        panel.add(new JLabel("Email:"), gbc);
        gbc.gridx = 1;
        JTextField txtEmail = new JTextField(email, 20);
        panel.add(txtEmail, gbc);
        
        gbc.gridx = 0; gbc.gridy = 5;
        panel.add(new JLabel("Status:"), gbc);
        gbc.gridx = 1;
        JComboBox<String> cbStatus = new JComboBox<>(new String[]{"Ativo", "Inativo"});
        cbStatus.setSelectedItem(status);
        panel.add(cbStatus, gbc);
        
        // Botões
        gbc.gridx = 0; gbc.gridy = 6; gbc.gridwidth = 2;
        JPanel buttonPanel = new JPanel(new FlowLayout());
        JButton btnSalvar = new JButton("Salvar");
        JButton btnCancelar = new JButton("Cancelar");
        
        btnSalvar.addActionListener(e -> {
            String novoNome = txtNome.getText().trim();
            String novoCpfCnpj = txtCpfCnpj.getText().trim();
            String novoTelefone = txtTelefone.getText().trim();
            String novoEmail = txtEmail.getText().trim();
            String novoStatus = (String) cbStatus.getSelectedItem();
            
            // Validação básica
            if (novoNome.isEmpty()) {
                JOptionPane.showMessageDialog(dialog, "Nome é obrigatório!", "Erro", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            if (novoCpfCnpj.isEmpty()) {
                JOptionPane.showMessageDialog(dialog, "CPF/CNPJ é obrigatório!", "Erro", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            // Atualizar na tabela
            tableModel.setValueAt(novoNome, selectedRow, 1);
            tableModel.setValueAt(novoCpfCnpj, selectedRow, 2);
            tableModel.setValueAt(novoTelefone, selectedRow, 3);
            tableModel.setValueAt(novoEmail, selectedRow, 4);
            tableModel.setValueAt(novoStatus, selectedRow, 5);
            
            LoggerUtil.info("Cliente editado: " + novoNome);
            LoggerUtil.logUserAction("Usuário", "EDITAR_CLIENTE", "Cliente editado: " + novoNome);
            
            JOptionPane.showMessageDialog(dialog, "Cliente atualizado com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
            dialog.dispose();
        });
        
        btnCancelar.addActionListener(e -> dialog.dispose());
        
        buttonPanel.add(btnSalvar);
        buttonPanel.add(btnCancelar);
        panel.add(buttonPanel, gbc);
        
        dialog.add(panel);
        dialog.setVisible(true);
    }
    
    public void show() {
        mainFrame.setVisible(true);
    }
}
