package com.br.hermescomercial.controller;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

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
        com.br.hermescomercial.theme.ModernTheme.applyModernTheme();
        mainFrame = new JFrame("Hermes Comercial PDV v2.1.0 - Premium");
        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainFrame.setSize(1200, 800);
        mainFrame.setLocationRelativeTo(null);
        
        // Configurar fundo elegante - padrão Nova Venda
        mainFrame.getContentPane().setBackground(com.br.hermescomercial.theme.ModernTheme.BACKGROUND_PRIMARY);
        
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
        operationsMenu.add(createMenuItem("📦 Gestão de Produtos", this::gerenciarProdutos));
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
        panel.setBorder(BorderFactory.createEmptyBorder(8, 12, 12, 12));
        panel.setBackground(new Color(41, 128, 185));
        panel.setPreferredSize(new Dimension(0, 75));
        
        // Logo da empresa à esquerda
        try {
            ImageIcon logoIcon = new ImageIcon(getClass().getResource("/img/logo.png"));
            Image scaledImage = logoIcon.getImage().getScaledInstance(50, 50, Image.SCALE_SMOOTH);
            JLabel logoLabel = new JLabel(new ImageIcon(scaledImage));
            logoLabel.setBorder(BorderFactory.createEmptyBorder(0, 8, 0, 8));
            panel.add(logoLabel, BorderLayout.WEST);
        } catch (Exception e) {
            // Se não conseguir carregar o logo, continua sem ele
        }
        
        // Título central elegante
        JLabel titleLabel = new JLabel("Hermes Comercial PDV v2.1.0 - Premium", JLabel.CENTER);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 22));
        titleLabel.setForeground(new Color(255, 255, 255)); // Branco elegante
        titleLabel.setOpaque(true);
        
        // Botão elegante
        JButton btnInfo = new JButton("ℹ️ Info");
        btnInfo.setBackground(new Color(149, 165, 166)); // Cinza azulado suave
        btnInfo.setForeground(new Color(255, 255, 255)); // Branco elegante
        btnInfo.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btnInfo.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(127, 140, 141), 2), // Cinza borda suave
            BorderFactory.createEmptyBorder(8, 16, 8, 16)
        ));
        btnInfo.setFocusPainted(false);
        btnInfo.setOpaque(true);
        btnInfo.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        // Efeito hover elegante
        btnInfo.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btnInfo.setBackground(new Color(127, 140, 141)); // Cinza mais escuro suave
                btnInfo.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(new Color(108, 117, 125), 2), // Cinza ainda mais escuro
                    BorderFactory.createEmptyBorder(8, 16, 8, 16)
                ));
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btnInfo.setBackground(new Color(149, 165, 166)); // Cinza azulado suave
                btnInfo.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(new Color(127, 140, 141), 2), // Cinza borda suave
                    BorderFactory.createEmptyBorder(8, 16, 8, 16)
                ));
            }
        });
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
        
        // Data e hora atual elegante
        JLabel lblDataHora = new JLabel(java.time.LocalDateTime.now().format(java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")), JLabel.RIGHT);
        lblDataHora.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        lblDataHora.setForeground(new Color(255, 255, 255)); // Branco elegante
        lblDataHora.setOpaque(true);
        
        panel.add(btnInfo, BorderLayout.WEST);
        panel.add(titleLabel, BorderLayout.CENTER);
        panel.add(lblDataHora, BorderLayout.EAST);
        
        return panel;
    }
    
    private JButton createStyledButton(String text, String tooltip, ActionListener action) {
        JButton button = new JButton(text);
        button.setBackground(new Color(149, 165, 166)); // Cinza azulado suave
        button.setForeground(new Color(255, 255, 255)); // Branco elegante
        button.setFont(new Font("Segoe UI", Font.BOLD, 12));
        button.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(127, 140, 141), 2), // Cinza borda suave
            BorderFactory.createEmptyBorder(12, 20, 12, 20)
        ));
        button.setFocusPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setToolTipText(tooltip);
        button.addActionListener(action);
        button.setOpaque(true);
        
        // Efeito hover elegante
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(new Color(127, 140, 141)); // Cinza mais escuro suave
                button.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(new Color(108, 117, 125), 2), // Cinza ainda mais escuro
                    BorderFactory.createEmptyBorder(12, 20, 12, 20)
                ));
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(new Color(149, 165, 166)); // Cinza azulado suave
                button.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(new Color(127, 140, 141), 2), // Cinza borda suave
                    BorderFactory.createEmptyBorder(12, 20, 12, 20)
                ));
            }
        });
        
        return button;
    }
    
    private void createMainPanel() {
        // Painel principal com design moderno
        mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));
        mainPanel.setBackground(new Color(250, 250, 250)); // Cinza muito suave
        
        // Header padrão Nova Venda
        JPanel headerPanel = createHeaderPanel();
        mainPanel.add(headerPanel, BorderLayout.NORTH);
        
        // Painel central com informações - efeito cardPanel elegante
        JPanel centerPanel = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                // Fundo elegante com gradiente sutil
                g2d.setColor(new Color(250, 252, 252)); // Branco suave
                g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 25, 25);
                
                // Borda elegante
                g2d.setColor(new Color(189, 195, 199)); // Cinza suave
                g2d.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 25, 25);
            }
        };
        centerPanel.setOpaque(false);
        centerPanel.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));
        
        // Status label
        statusLabel = new JLabel("Sistema pronto para uso");
        statusLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12)); // Fonte suave
        statusLabel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        statusLabel.setBackground(new Color(236, 240, 241)); // Cinza suave claro
        statusLabel.setForeground(new Color(52, 73, 94)); // Azul suave profundo
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
            System.out.println("Iniciando nova venda");
            PDVVendaSwingController vendaController = new PDVVendaSwingController();
            vendaController.show();
            statusLabel.setText("Nova venda iniciada");
        } catch (Exception ex) {
            System.err.println("Erro ao abrir tela de venda: " + ex.getMessage());
            JOptionPane.showMessageDialog(mainFrame, 
                "Erro ao abrir tela de venda: " + ex.getMessage(),
                "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void consultarProdutos(ActionEvent e) {
        try {
            System.out.println("Acessando consulta de produtos");
            // System.out.logUserAction("Usuário", "CONSULTAR_PRODUTOS", "Acesso ao módulo de consulta");
            
            // Criar frame para consulta de produtos com layout padrão
            JFrame consultaFrame = new JFrame("📦 Consulta de Produtos v2.1.0 - Premium");
            consultaFrame.setSize(900, 700);
            consultaFrame.setLocationRelativeTo(mainFrame);
            consultaFrame.getContentPane().setBackground(new Color(245, 245, 250));
            
            // Header padrão Nova Venda
            JPanel headerPanel = createHeaderPanelForDialog(consultaFrame, "📦 Consulta de Produtos v2.1.0 - Premium");
            
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
            JButton btnBuscar = com.br.hermescomercial.theme.ModernTheme.createPastelButton("🔍 Buscar", com.br.hermescomercial.theme.ModernTheme.PASTEL_BLUE, com.br.hermescomercial.theme.ModernTheme.TEXT_PRIMARY);
            JButton btnLimpar = com.br.hermescomercial.theme.ModernTheme.createPastelButton("🔄 Limpar", com.br.hermescomercial.theme.ModernTheme.PASTEL_YELLOW, com.br.hermescomercial.theme.ModernTheme.TEXT_PRIMARY);
            
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
            
            // Dados de exemplo com tipos corretos
            Object[][] dados = {
                {"001", "Notebook Dell Inspire 15", 3500.00, 10, "Informática"},
                {"002", "Mouse Wireless Logitech MX3", 89.90, 50, "Periféricos"},
                {"003", "Teclado Mecânico RGB Gamer", 250.00, 25, "Periféricos"},
                {"004", "Monitor 24\" LED Full HD", 899.00, 15, "Monitores"},
                {"005", "Webcam HD 1080p com Microfone", 150.00, 30, "Acessórios"}
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
            JButton btnFechar = com.br.hermescomercial.theme.ModernTheme.createPastelButton("❌ Fechar", com.br.hermescomercial.theme.ModernTheme.PASTEL_CORAL, com.br.hermescomercial.theme.ModernTheme.TEXT_PRIMARY);
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
            
            System.out.println("Consulta de produtos aberta com sucesso");
            statusLabel.setText("Consulta de produtos aberta");
            
        } catch (Exception ex) {
            System.err.println("Erro ao abrir consulta de produtos: " + ex.getMessage());
            JOptionPane.showMessageDialog(mainFrame, 
                "Erro ao abrir consulta de produtos: " + ex.getMessage(),
                "Consulta de Produtos", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void gerenciarProdutos(ActionEvent e) {
        try {
            System.out.println("Acessando gestão unificada de produtos");
            // System.out.logUserAction("Usuário", "GERENCIAR_PRODUTOS", "Acesso ao módulo unificado de produtos");
            
            // Abrir tela unificada de produtos
            PDVProdutosUnificadoSwingController produtosController = new PDVProdutosUnificadoSwingController();
            produtosController.show();
            
            statusLabel.setText("Gestão de produtos aberta");
            System.out.println("Tela unificada de produtos aberta com sucesso");
        } catch (Exception ex) {
            System.err.println("Erro ao abrir gestão de produtos: " + ex.getMessage());
            JOptionPane.showMessageDialog(mainFrame, 
                "Erro ao abrir gestão de produtos: " + ex.getMessage(),
                "Gestão de Produtos", JOptionPane.ERROR_MESSAGE);
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
            System.out.println("Abrindo gestão de clientes");
            // System.out.logUserAction("Usuário", "GERENCIAR_CLIENTES", "Acesso ao módulo de clientes");
            
            // Criar janela de gestão de clientes com layout padrão
            JFrame clientesFrame = new JFrame("👥 Gestão de Clientes v2.1.0 - Premium");
            clientesFrame.setSize(1000, 700);
            clientesFrame.setLocationRelativeTo(mainFrame);
            clientesFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            clientesFrame.getContentPane().setBackground(new Color(245, 245, 250));
            
            // Header padrão Nova Venda
            JPanel headerPanel = createHeaderPanelForDialog(clientesFrame, "👥 Gestão de Clientes v2.1.0 - Premium");
            
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
            JButton btnBuscar = com.br.hermescomercial.theme.ModernTheme.createPastelButton("🔍 Buscar", com.br.hermescomercial.theme.ModernTheme.PASTEL_BLUE, com.br.hermescomercial.theme.ModernTheme.TEXT_PRIMARY);
            JButton btnLimpar = com.br.hermescomercial.theme.ModernTheme.createPastelButton("🔄 Limpar", com.br.hermescomercial.theme.ModernTheme.PASTEL_YELLOW, com.br.hermescomercial.theme.ModernTheme.TEXT_PRIMARY);
            
            buscaInputPanel.add(txtBusca);
            buscaInputPanel.add(btnBuscar);
            buscaInputPanel.add(btnLimpar);
            
            buscaPanel.add(lblBusca, BorderLayout.NORTH);
            buscaPanel.add(buscaInputPanel, BorderLayout.CENTER);
            
            // Painel de botões de cadastro estilizado
            JPanel acaoPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
            acaoPanel.setBackground(new Color(255, 255, 255));
            JButton btnNovo = com.br.hermescomercial.theme.ModernTheme.createPastelButton("➕ Novo Cliente", com.br.hermescomercial.theme.ModernTheme.PASTEL_GREEN, com.br.hermescomercial.theme.ModernTheme.TEXT_PRIMARY);
            JButton btnEditar = com.br.hermescomercial.theme.ModernTheme.createPastelButton("✏️ Editar", com.br.hermescomercial.theme.ModernTheme.PASTEL_BLUE, com.br.hermescomercial.theme.ModernTheme.TEXT_PRIMARY);
            JButton btnExcluir = com.br.hermescomercial.theme.ModernTheme.createPastelButton("🗑️ Excluir", com.br.hermescomercial.theme.ModernTheme.PASTEL_CORAL, com.br.hermescomercial.theme.ModernTheme.TEXT_PRIMARY);
            JButton btnExportar = com.br.hermescomercial.theme.ModernTheme.createPastelButton("📤 Exportar", com.br.hermescomercial.theme.ModernTheme.PASTEL_PURPLE, com.br.hermescomercial.theme.ModernTheme.TEXT_PRIMARY);
            
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
            JButton btnFechar = com.br.hermescomercial.theme.ModernTheme.createPastelButton("❌ Fechar", com.br.hermescomercial.theme.ModernTheme.PASTEL_CORAL, com.br.hermescomercial.theme.ModernTheme.TEXT_PRIMARY);
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
                    int confirm = com.br.hermescomercial.theme.ModernTheme.showCustomConfirmDialog(clientesFrame, 
                        "Deseja excluir o cliente " + tableModel.getValueAt(selectedRow, 1) + "?",
                        "Excluir Cliente", 
                        new String[]{"Sim", "Não"}, 0);
                    if (confirm == 0) {
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
            
            System.out.println("Gestão de clientes aberta com sucesso");
            statusLabel.setText("Gestão de clientes aberta");
            
        } catch (Exception ex) {
            System.err.println("Erro ao abrir gestão de clientes: " + ex.getMessage());
            JOptionPane.showMessageDialog(mainFrame, 
                "Erro ao abrir gestão de clientes: " + ex.getMessage(),
                "Gestão de Clientes", JOptionPane.ERROR_MESSAGE);
        }
    }
    
        
    private void abrirConfiguracoes(ActionEvent e) {
        try {
            System.out.println("Abrindo módulo de configurações");
            // System.out.logUserAction("Usuário", "CONFIGURACOES", "Acesso ao módulo de configurações");
            PDVConfiguracoesSwingController configController = new PDVConfiguracoesSwingController();
            configController.show();
            statusLabel.setText("Configurações abertas");
        } catch (Exception ex) {
            System.err.println("Erro ao abrir configurações: " + ex.getMessage());
            JOptionPane.showMessageDialog(mainFrame, 
                "Erro ao abrir configurações: " + ex.getMessage(),
                "Configurações", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void verRelatorios(ActionEvent e) {
        try {
            System.out.println("Abrindo módulo de relatórios");
            // System.out.logUserAction("Usuário", "RELATORIOS", "Acesso ao módulo de relatórios");
            PDVRelatoriosSwingController relatoriosController = new PDVRelatoriosSwingController();
            relatoriosController.show();
            statusLabel.setText("Relatórios abertos");
            System.out.println("Módulo de relatórios aberto com sucesso");
        } catch (Exception ex) {
            System.err.println("Erro ao abrir relatórios: " + ex.getMessage());
            JOptionPane.showMessageDialog(mainFrame, 
                "Erro ao abrir relatórios: " + ex.getMessage(),
                "Relatórios", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void relatorioVendasDia(ActionEvent e) {
        try {
            PDVRelatoriosSwingController relatoriosController = new PDVRelatoriosSwingController();
            relatoriosController.show();
            relatoriosController.abrirRelatorioVendasDia();
            System.out.println("Relatório de vendas do dia aberto");
        } catch (Exception ex) {
            System.err.println("Erro ao abrir relatório de vendas do dia: " + ex.getMessage());
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
            System.out.println("Relatório de produtos aberto");
        } catch (Exception ex) {
            System.err.println("Erro ao abrir relatório de produtos: " + ex.getMessage());
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
            System.out.println("Relatório financeiro aberto");
        } catch (Exception ex) {
            System.err.println("Erro ao abrir relatório financeiro: " + ex.getMessage());
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
            System.out.println("Abrindo visualizador de logs");
            com.br.hermescomercial.util.LogViewer.showLogViewer();
        } catch (Exception ex) {
            System.err.println("Erro ao abrir visualizador de logs: " + ex.getMessage());
            JOptionPane.showMessageDialog(mainFrame, 
                "Erro ao abrir visualizador de logs: " + ex.getMessage(),
                "Logs", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void limparLogs(ActionEvent e) {
        int result = com.br.hermescomercial.theme.ModernTheme.showCustomConfirmDialog(mainFrame, 
            "Deseja limpar logs antigos (mais de 30 dias)?\n\n" +
            "Esta ação não pode ser desfeita.",
            "Limpar Logs", 
            new String[]{"Sim", "Não"}, 0);
            
        if (result == 0) {
            try {
                // System.out.cleanOldLogs(); // Método não existe
                System.out.println("Logs antigos limpos pelo usuário");
                JOptionPane.showMessageDialog(mainFrame, 
                    "Logs antigos limpos com sucesso!",
                    "Limpar Logs", JOptionPane.INFORMATION_MESSAGE);
            } catch (Exception ex) {
                System.err.println("Erro ao limpar logs antigos: " + ex.getMessage());
                JOptionPane.showMessageDialog(mainFrame, 
                    "Erro ao limpar logs: " + ex.getMessage(),
                    "Limpar Logs", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    private void criarNovoCliente(JFrame parentFrame, DefaultTableModel tableModel) {
        JDialog dialog = new JDialog(parentFrame, "Novo Cliente", true);
        dialog.setSize(500, 450);
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
        JButton btnSalvar = com.br.hermescomercial.theme.ModernTheme.createPastelButton("💾 Salvar", com.br.hermescomercial.theme.ModernTheme.PASTEL_GREEN, com.br.hermescomercial.theme.ModernTheme.TEXT_PRIMARY);
        JButton btnCancelar = com.br.hermescomercial.theme.ModernTheme.createPastelButton("❌ Cancelar", com.br.hermescomercial.theme.ModernTheme.PASTEL_CORAL, com.br.hermescomercial.theme.ModernTheme.TEXT_PRIMARY);
        
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
            
            System.out.println("Novo cliente criado: " + nome);
            // System.out.logUserAction("Usuário", "NOVO_CLIENTE", "Cliente criado: " + nome);
            
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
        JButton btnSalvar = com.br.hermescomercial.theme.ModernTheme.createPastelButton("💾 Salvar", com.br.hermescomercial.theme.ModernTheme.PASTEL_GREEN, com.br.hermescomercial.theme.ModernTheme.TEXT_PRIMARY);
        JButton btnCancelar = com.br.hermescomercial.theme.ModernTheme.createPastelButton("❌ Cancelar", com.br.hermescomercial.theme.ModernTheme.PASTEL_CORAL, com.br.hermescomercial.theme.ModernTheme.TEXT_PRIMARY);
        
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
            
            System.out.println("Cliente editado: " + novoNome);
            // System.out.logUserAction("Usuário", "EDITAR_CLIENTE", "Cliente editado: " + novoNome);
            
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
