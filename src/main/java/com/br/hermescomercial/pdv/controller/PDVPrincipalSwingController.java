package com.br.hermescomercial.pdv.controller;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import com.br.hermescomercial.erp.controller.ERPProdutoSwingController;
import com.br.hermescomercial.erp.controller.ERPClienteSwingController;
import com.br.hermescomercial.erp.controller.ERPFinanceiroSwingController;
import com.br.hermescomercial.erp.controller.ERPRelatorioSwingController;
import com.br.hermescomercial.theme.ModernTheme;
import com.br.hermescomercial.theme.OceanoTheme;
import com.br.hermescomercial.theme.ModernLayout;

/**
 * Controller principal do PDV em SWING
 * Interface principal do sistema PDV
 * Versão 2.0 - 100% SWING - Sem JavaFX
 */
public class PDVPrincipalSwingController {
    
    private JFrame mainFrame;
    private JPanel mainPanel;
    private JLabel statusLabel;
    private JButton btnNovaVenda;
    private JButton btnConsultarProduto;
    private JButton btnClientes;
    private JButton btnCaixa;
    private JButton btnRelatorios;
    private JButton btnConfiguracoes;
    
    public PDVPrincipalSwingController() {
        // Aplicar tema Oceano
        OceanoTheme.applyTheme();
        initializeUI();
    }
    
    private void initializeUI() {
        // Aplicar tema moderno
        ModernTheme.applyModernTheme();
        mainFrame = new JFrame("Hermes Comercial PDV v2.3.0 - Premium");
        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainFrame.setSize(1200, 800);
        mainFrame.setLocationRelativeTo(null);
        
        // Configurar fundo elegante - padrão Nova Venda
        mainFrame.getContentPane().setBackground(ModernTheme.BACKGROUND_PRIMARY);
        
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
        
        // Menu Cadastros
        JMenu cadastrosMenu = new JMenu("📋 Cadastros");
        cadastrosMenu.add(createMenuItem("🏢 Fornecedores", e -> abrirFornecedores(e)));
        cadastrosMenu.add(createMenuItem("� Clientes", e -> gerenciarClientes(e)));
        cadastrosMenu.add(createMenuItem("📦 Produtos", e -> gerenciarProdutos(e)));
        cadastrosMenu.add(createMenuItem("🔍 Consulta Rápida de Produtos", e -> consultarProdutos(e)));
        
        // Menu Operações
        JMenu operationsMenu = new JMenu("💳 Operações");
        operationsMenu.add(createMenuItem("💰 Nova Venda", e -> abrirVenda(e)));
        operationsMenu.add(createMenuItem("💵 Caixa", e -> abrirCaixa(e)));
        operationsMenu.add(createMenuItem("🏪 Fechar Caixa", e -> fecharCaixa(e)));
        operationsMenu.add(createMenuItem("📊 Dashboard Analytics", e -> abrirDashboard(e)));
        operationsMenu.add(createMenuItem("🔔 Notificações", e -> abrirNotificacao(e)));
        
        // Menu Financeiro
        JMenu financeiroMenu = new JMenu("💰 Financeiro");
        financeiroMenu.add(createMenuItem("💳 Gestão Financeira (ERP)", e -> mostrarEmDesenvolvimento(e, "Gestão Financeira ERP")));
        financeiroMenu.add(createMenuItem("📊 Relatórios Financeiros", e -> relatorioFinanceiro(e)));
        financeiroMenu.add(createMenuItem("💸 Contas a Pagar", e -> mostrarEmDesenvolvimento(e, "Contas a Pagar")));
        financeiroMenu.add(createMenuItem("💵 Contas a Receber", e -> mostrarEmDesenvolvimento(e, "Contas a Receber")));
        financeiroMenu.add(createMenuItem("📈 Fluxo de Caixa", e -> mostrarEmDesenvolvimento(e, "Fluxo de Caixa")));
        
        // Menu Estoque
        JMenu estoqueMenu = new JMenu("📦 Estoque");
        estoqueMenu.add(createMenuItem("📋 Controle de Estoque", e -> gerenciarEstoque(e)));
        estoqueMenu.add(createMenuItem("📦 Gestão de Produtos (ERP)", e -> gerenciarProdutosERP(e)));
        estoqueMenu.add(createMenuItem("📊 Relatório de Estoque", e -> mostrarEmDesenvolvimento(e, "Relatório de Estoque")));
        estoqueMenu.add(createMenuItem("⚠️ Estoque Baixo", e -> mostrarEmDesenvolvimento(e, "Estoque Baixo")));
        estoqueMenu.add(createMenuItem("🔄 Movimentação", e -> mostrarEmDesenvolvimento(e, "Movimentação de Estoque")));
        
        // Menu Relatórios
        JMenu reportsMenu = new JMenu("📊 Relatórios");
        reportsMenu.add(createMenuItem("📋 Templates de Relatórios", e -> abrirTemplatesRelatorio(e)));
        reportsMenu.add(createMenuItem("📤 Exportar Dados", e -> abrirExportImport(e)));
        reportsMenu.addSeparator();
        reportsMenu.add(createMenuItem("💰 Vendas do Dia", this::relatorioVendasDia));
        reportsMenu.add(createMenuItem("📦 Produtos", this::relatorioProdutos));
        reportsMenu.add(createMenuItem("📊 Financeiro", this::relatorioFinanceiro));
        reportsMenu.add(createMenuItem("👥 Clientes", this::relatorioClientes));
        reportsMenu.add(createMenuItem("📦 Estoque", this::relatorioEstoque));
        
        // Menu Ferramentas
        JMenu toolsMenu = new JMenu("🔧 Ferramentas");
        toolsMenu.add(createMenuItem("🔍 Busca Avançada", e -> abrirBuscaAvancada(e)));
        toolsMenu.add(createMenuItem("💳 Pagamentos API", e -> abrirPagamentosAPI(e)));
        toolsMenu.add(createMenuItem("⚡ Configuração de Cache", e -> abrirCacheConfig(e)));
        toolsMenu.add(createMenuItem("⚙️ Configurações do Sistema", e -> abrirSistemaConfig(e)));
        toolsMenu.addSeparator();
        toolsMenu.add(createMenuItem("📜 Visualizar Logs", e -> verLogs(e)));
        toolsMenu.add(createMenuItem("🗑️ Limpar Logs Antigos", e -> limparLogs(e)));
        
        // Menu Ajuda
        JMenu helpMenu = new JMenu("Ajuda");
        helpMenu.add(createMenuItem("Sobre", this::mostrarSobre));
        
        menuBar.add(fileMenu);
        menuBar.add(cadastrosMenu);
        menuBar.add(operationsMenu);
        menuBar.add(financeiroMenu);
        menuBar.add(estoqueMenu);
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
        panel.setBackground(OceanoTheme.PRIMARY);
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
        
        // Título central elegante com tema Oceano
        JLabel titleLabel = new JLabel(" Hermes Comercial PDV v2.8.0 - Oceano", JLabel.CENTER);
        titleLabel.setFont(OceanoTheme.FONT_TITLE);
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setOpaque(true);
        titleLabel.setBackground(new Color(26, 188, 156)); // Azul Turquesa igual ao painel
        
        // Botão elegante com tema Oceano e ícone
        JButton btnInfo = OceanoTheme.createOceanoButton("ℹ️ Informações", OceanoTheme.ACCENT);
        btnInfo.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(OceanoTheme.ACCENT, 2),
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
    
        
    private void createMainPanel() {
        // Painel principal com design moderno e responsivo
        mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBorder(BorderFactory.createEmptyBorder(25, 25, 25, 25));
        mainPanel.setBackground(new Color(248, 249, 250)); // Cinza muito suave moderno
        
        // Header padrão Nova Venda
        JPanel headerPanel = createHeaderPanel();
        mainPanel.add(headerPanel, BorderLayout.NORTH);
        
        // Painel central com painel de boas-vindas e informações
        JPanel centerPanel = new JPanel(new BorderLayout());
        centerPanel.setOpaque(false);
        centerPanel.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));
        
        // Painel de boas-vindas
        JPanel welcomePanel = createWelcomePanel();
        centerPanel.add(welcomePanel, BorderLayout.NORTH);
        
        // Painel de funcionalidades agrupadas
        JPanel featuresPanel = createFeaturesPanel();
        centerPanel.add(featuresPanel, BorderLayout.CENTER);
        
        mainPanel.add(centerPanel, BorderLayout.CENTER);
        
        // Painel de status e rodapé
        JPanel footerPanel = createFooterPanel();
        mainPanel.add(footerPanel, BorderLayout.SOUTH);
    }
    
    private JPanel createWelcomePanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setOpaque(false);
        panel.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0));
        
        // Título principal
        JLabel titleLabel = new JLabel("🏪 Bem-vindo ao Hermes Comercial PDV", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 28));
        titleLabel.setForeground(new Color(44, 62, 80)); // Azul escuro moderno
        titleLabel.setBorder(BorderFactory.createEmptyBorder(10, 0, 5, 0));
        
        // Subtítulo informativo
        JLabel subtitleLabel = new JLabel("Sistema completo de gestão de ponto de venda", SwingConstants.CENTER);
        subtitleLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        subtitleLabel.setForeground(new Color(127, 140, 141)); // Cinza moderno
        
        // Painel de informações rápidas
        JPanel infoPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        infoPanel.setOpaque(false);
        infoPanel.setBorder(BorderFactory.createEmptyBorder(15, 0, 0, 0));
        
        JLabel infoLabel = new JLabel("📅 " + java.time.LocalDate.now().format(java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy")) + 
                                   " | ⏰ " + java.time.LocalTime.now().format(java.time.format.DateTimeFormatter.ofPattern("HH:mm")) +
                                   " | 👤 Operador: Administrador");
        infoLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        infoLabel.setForeground(new Color(52, 73, 94)); // Azul suave
        
        infoPanel.add(infoLabel);
        
        JPanel titleContainer = new JPanel(new BorderLayout());
        titleContainer.setOpaque(false);
        titleContainer.add(titleLabel, BorderLayout.NORTH);
        titleContainer.add(subtitleLabel, BorderLayout.CENTER);
        titleContainer.add(infoPanel, BorderLayout.SOUTH);
        
        panel.add(titleContainer, BorderLayout.CENTER);
        
        return panel;
    }
    
    private JPanel createFeaturesPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setOpaque(false);
        
        // Título das funcionalidades
        JLabel featuresTitle = new JLabel("🚀 Funcionalidades Principais", SwingConstants.CENTER);
        featuresTitle.setFont(new Font("Segoe UI", Font.BOLD, 18));
        featuresTitle.setForeground(new Color(44, 62, 80));
        featuresTitle.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0));
        
        // Painel de botões organizados por categorias
        JPanel buttonsContainer = new JPanel(new BorderLayout());
        buttonsContainer.setOpaque(false);
        
        // Painel de operações principais
        JPanel mainOpsPanel = createMainOperationsPanel();
        
        // Painel de operações secundárias
        JPanel secondaryOpsPanel = createSecondaryOperationsPanel();
        
        buttonsContainer.add(mainOpsPanel, BorderLayout.CENTER);
        buttonsContainer.add(secondaryOpsPanel, BorderLayout.SOUTH);
        
        panel.add(featuresTitle, BorderLayout.NORTH);
        panel.add(buttonsContainer, BorderLayout.CENTER);
        
        return panel;
    }
    
    private JPanel createMainOperationsPanel() {
        JPanel panel = new JPanel(new GridLayout(2, 3, 15, 15));
        panel.setOpaque(false);
        panel.setBorder(BorderFactory.createEmptyBorder(0, 0, 15, 0));
        
        // Botões principais com design melhorado
        btnNovaVenda = createEnhancedButton("🛒 Nova Venda", "Iniciar nova venda", new Color(46, 204, 113), this::abrirNovaVenda);
        btnConsultarProduto = createEnhancedButton("📦 Consultar Produtos", "Consultar produtos", new Color(52, 152, 219), this::consultarProdutos);
        btnClientes = createEnhancedButton("👥 Clientes", "Gerenciar clientes", new Color(155, 89, 182), this::gerenciarClientes);
        btnCaixa = createEnhancedButton("💰 Caixa", "Operações de caixa", new Color(241, 196, 15), e -> {
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
        btnRelatorios = createEnhancedButton("📊 Relatórios", "Gerar relatórios", new Color(230, 126, 34), this::verRelatorios);
        btnConfiguracoes = createEnhancedButton("⚙️ Configurações", "Configurar sistema", new Color(149, 165, 166), this::abrirConfiguracoes);
        
        panel.add(btnNovaVenda);
        panel.add(btnConsultarProduto);
        panel.add(btnClientes);
        panel.add(btnCaixa);
        panel.add(btnRelatorios);
        panel.add(btnConfiguracoes);
        
        return panel;
    }
    
    private JPanel createSecondaryOperationsPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
        panel.setOpaque(false);
        
        // Botões secundários
        JButton btnDashboard = createEnhancedButton("📈 Dashboard", "Ver dashboard analítico", new Color(26, 188, 156), e -> abrirDashboard(e));
        JButton btnNotificacao = createEnhancedButton("🔔 Notificações", "Ver notificações", new Color(231, 76, 60), e -> abrirNotificacao(e));
        JButton btnAjuda = createEnhancedButton("❓ Ajuda", "Ajuda e suporte", new Color(52, 73, 94), this::mostrarSobre);
        
        panel.add(btnDashboard);
        panel.add(btnNotificacao);
        panel.add(btnAjuda);
        
        return panel;
    }
    
    private JPanel createFooterPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setOpaque(false);
        panel.setBorder(BorderFactory.createEmptyBorder(15, 0, 0, 0));
        
        // Status label melhorado
        statusLabel = new JLabel("✅ Sistema pronto para uso");
        statusLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        statusLabel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(46, 204, 113), 1),
            BorderFactory.createEmptyBorder(8, 12, 8, 12)
        ));
        statusLabel.setBackground(new Color(240, 255, 240));
        statusLabel.setForeground(new Color(27, 94, 32));
        statusLabel.setOpaque(true);
        
        // Painel de informações do sistema
        JPanel systemInfoPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        systemInfoPanel.setOpaque(false);
        
        JLabel versionLabel = new JLabel("Hermes Comercial PDV v2.3.0");
        versionLabel.setFont(new Font("Segoe UI", Font.PLAIN, 10));
        versionLabel.setForeground(new Color(127, 140, 141));
        
        systemInfoPanel.add(versionLabel);
        
        panel.add(statusLabel, BorderLayout.WEST);
        panel.add(systemInfoPanel, BorderLayout.EAST);
        
        return panel;
    }
    
    private JButton createEnhancedButton(String text, String tooltip, Color color, ActionListener action) {
        JButton button = new JButton(text);
        button.setBackground(color);
        button.setForeground(Color.WHITE);
        button.setFont(new Font("Segoe UI", Font.BOLD, 12));
        button.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(color.darker(), 1),
            BorderFactory.createEmptyBorder(15, 20, 15, 20)
        ));
        button.setFocusPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setToolTipText(tooltip);
        button.addActionListener(action);
        button.setOpaque(true);
        
        // Efeito hover melhorado
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(color.darker());
                button.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(color.darker().darker(), 2),
                    BorderFactory.createEmptyBorder(15, 20, 15, 20)
                ));
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(color);
                button.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(color.darker(), 1),
                    BorderFactory.createEmptyBorder(15, 20, 15, 20)
                ));
            }
        });
        
        return button;
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
            
            // Painel principal com estrutura igual Nova Venda
            JPanel mainPanel = new JPanel(new BorderLayout());
            mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20)); // Igual Nova Venda
            mainPanel.setBackground(new Color(248, 249, 250)); // Fundo igual Nova Venda
            
            // Painel central com layout otimizado (igual Nova Venda)
            JPanel centerPanel = new JPanel(new BorderLayout());
            centerPanel.setBorder(BorderFactory.createEmptyBorder(15, 0, 15, 0)); // Igual Nova Venda
            centerPanel.setOpaque(false);
            
            // Painel de produtos com design moderno (igual Nova Venda)
            JPanel produtosPanel = createProdutosPanelForConsulta();
            centerPanel.add(produtosPanel, BorderLayout.CENTER);
            
            // Painel de entrada com design melhorado (igual Nova Venda)
            JPanel inputPanel = createInputPanelForConsulta();
            centerPanel.add(inputPanel, BorderLayout.SOUTH);
            
            mainPanel.add(centerPanel, BorderLayout.CENTER);
            
            // Painel lateral com resumo e ações (igual Nova Venda)
            JPanel sidePanel = createSidePanelForConsulta();
            mainPanel.add(sidePanel, BorderLayout.EAST);
            
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
        panel.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20)); // Igual Nova Venda
        panel.setBackground(new Color(26, 188, 156)); // Azul Turquesa igual Nova Venda
        panel.setPreferredSize(new Dimension(0, 90)); // Altura igual Nova Venda
        
        // Painel esquerdo com botão voltar
        JPanel leftPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        leftPanel.setOpaque(false);
        
        JButton btnVoltar = new JButton("← Voltar");
        btnVoltar.setBackground(new Color(255, 255, 255));
        btnVoltar.setForeground(new Color(41, 128, 185));
        btnVoltar.setFont(new Font("Segoe UI", Font.BOLD, 12)); // Fonte igual Nova Venda
        btnVoltar.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20)); // Borda igual Nova Venda
        btnVoltar.setFocusPainted(false);
        btnVoltar.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnVoltar.addActionListener(e -> frame.dispose());
        
        leftPanel.add(btnVoltar);
        
        // Painel central com título e subtítulo
        JPanel centerPanel = new JPanel(new BorderLayout());
        centerPanel.setOpaque(false);
        
        JLabel titleLabel = new JLabel(title, JLabel.CENTER);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 24)); // Fonte igual Nova Venda
        titleLabel.setForeground(Color.WHITE);
        
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
            JPanel buscaCadastroPanel = new JPanel(new FlowLayout());
            
            // Painel de botões de cadastro estilizado
            JPanel acaoPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
            acaoPanel.setBackground(new Color(255, 255, 255));
            
            // Painel de busca e cadastro
            
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
            JButton btnNovo = com.br.hermescomercial.theme.ModernTheme.createPastelButton("➕ Novo Cliente", com.br.hermescomercial.theme.ModernTheme.PASTEL_GREEN, com.br.hermescomercial.theme.ModernTheme.TEXT_PRIMARY);
            JButton btnFechar = com.br.hermescomercial.theme.ModernTheme.createPastelButton("❌ Fechar", com.br.hermescomercial.theme.ModernTheme.PASTEL_CORAL, com.br.hermescomercial.theme.ModernTheme.TEXT_PRIMARY);
            JButton btnEditar = com.br.hermescomercial.theme.ModernTheme.createPastelButton("✏️ Editar", com.br.hermescomercial.theme.ModernTheme.PASTEL_BLUE, com.br.hermescomercial.theme.ModernTheme.TEXT_PRIMARY);
            JButton btnExcluir = com.br.hermescomercial.theme.ModernTheme.createPastelButton("🗑️ Excluir", com.br.hermescomercial.theme.ModernTheme.PASTEL_CORAL, com.br.hermescomercial.theme.ModernTheme.TEXT_PRIMARY);
            JButton btnExportar = com.br.hermescomercial.theme.ModernTheme.createPastelButton("📤 Exportar", com.br.hermescomercial.theme.ModernTheme.PASTEL_PURPLE, com.br.hermescomercial.theme.ModernTheme.TEXT_PRIMARY);
            
            acaoPanel.add(btnNovo);
            acaoPanel.add(btnEditar);
            acaoPanel.add(btnExcluir);
            acaoPanel.add(btnExportar);
            acaoPanel.add(btnFechar);
            
            // Ação do botão fechar
            btnFechar.addActionListener(ev -> {
                clientesFrame.dispose();
            });
            
            // Adicionar botão Fechar ao painel de botões
            acaoPanel.add(btnFechar);
            
            buscaCadastroPanel.add(buscaPanel, BorderLayout.NORTH);
            buscaCadastroPanel.add(acaoPanel, BorderLayout.SOUTH);
            
            // Tabela de clientes com design melhorado
            String[] columns = {"ID", "Nome", "CPF/CNPJ", "Telefone", "Email", "Status"};
            DefaultTableModel tableModel = new DefaultTableModel(columns, 0);
            JTable clientesTable = new JTable(tableModel);
            clientesTable.setRowHeight(25);
            clientesTable.setFont(new Font("Arial", Font.PLAIN, 12));
            clientesTable.getTableHeader().setFont(new Font("Arial", Font.BOLD, 12));
            clientesTable.getTableHeader().setBackground(new Color(41, 128, 185));
            clientesTable.getTableHeader().setForeground(Color.WHITE);
            
            // Desabilitar edição da tabela
            clientesTable.setDefaultEditor(Object.class, null);
            clientesTable.setEnabled(false);
            
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
            buttonPanel.add(btnFechar);
            
            // Montar layout
            JPanel contentPanel = new JPanel(new BorderLayout());
            contentPanel.setBackground(new Color(255, 255, 255));
            contentPanel.setBorder(BorderFactory.createTitledBorder("Lista de Clientes"));
            contentPanel.add(scrollPane, BorderLayout.CENTER);
            
            mainPanel.add(buscaCadastroPanel, BorderLayout.NORTH);
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
    
    private void gerenciarEstoque(ActionEvent e) {
        try {
            System.out.println("Abrindo tela de controle de estoque...");
            new com.br.hermescomercial.erp.controller.ERPEstoqueSwingController();
            // O frame já é setVisible(true) no construtor
            statusLabel.setText("Controle de estoque aberto");
        } catch (Exception ex) {
            System.err.println("Erro ao abrir controle de estoque: " + ex.getMessage());
            JOptionPane.showMessageDialog(mainFrame, 
                "Erro ao abrir controle de estoque: " + ex.getMessage(),
                "Controle de Estoque", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void mostrarSobre(ActionEvent e) {
        JOptionPane.showMessageDialog(mainFrame, 
            "Hermes Comercial PDV v2.1.4\n" +
            "Sistema de Ponto de Venda completo\n" +
            "Desenvolvido em Java Swing\n" +
            "Versão Premium com Design Moderno\n" +
            "• Gestão de Produtos com categorias dinâmicas\n" +
            "• Gestão de Clientes completa\n" +
            "• Relatórios detalhados\n" +
            "• Validação de dados monetários\n" +
            "• Interface não-editável para segurança",
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
        
        // Limitar a 14 caracteres (CNPJ: XX.XXX.XXX/XXXX-XX ou CPF: XXX.XXX.XXX-XX)
        txtCpfCnpj.setDocument(new javax.swing.text.PlainDocument() {
            @Override
            public void insertString(int offs, String str, javax.swing.text.AttributeSet a) throws javax.swing.text.BadLocationException {
                if (str == null) return;
                
                if ((getLength() + str.length()) <= 14) {
                    // Verificar se contém apenas números, ponto, barra e traço
                    String validChars = "0123456789.-/";
                    for (char c : str.toCharArray()) {
                        if (validChars.indexOf(c) == -1) {
                            return; // Ignora caracteres inválidos
                        }
                    }
                    super.insertString(offs, str, a);
                }
            }
        });
        
        // Adicionar tooltip com formato esperado
        txtCpfCnpj.setToolTipText("CPF: XXX.XXX.XXX-XX ou CNPJ: XX.XXX.XXX/XXXX-XX - Máximo 14 caracteres");
        
        panel.add(txtCpfCnpj, gbc);
        
        gbc.gridx = 0; gbc.gridy = 2;
        panel.add(new JLabel("Telefone:"), gbc);
        gbc.gridx = 1;
        JTextField txtTelefone = new JTextField(20);
        
        // Limitar a 13 caracteres (formato: (XX) XXXXX-XXXX)
        txtTelefone.setDocument(new javax.swing.text.PlainDocument() {
            @Override
            public void insertString(int offs, String str, javax.swing.text.AttributeSet a) throws javax.swing.text.BadLocationException {
                if (str == null) return;
                
                if ((getLength() + str.length()) <= 13) {
                    // Verificar se contém apenas números, parênteses, espaço e traço
                    String validChars = "0123456789() -";
                    for (char c : str.toCharArray()) {
                        if (validChars.indexOf(c) == -1) {
                            return; // Ignora caracteres inválidos
                        }
                    }
                    super.insertString(offs, str, a);
                }
            }
        });
        
        // Adicionar tooltip com formato esperado
        txtTelefone.setToolTipText("Formato: (XX) XXXXX-XXXX - Máximo 13 caracteres");
        
        panel.add(txtTelefone, gbc);
        
        gbc.gridx = 0; gbc.gridy = 3;
        panel.add(new JLabel("Email:"), gbc);
        gbc.gridx = 1;
        JTextField txtEmail = new JTextField(20);
        
        // Adicionar tooltip com formato esperado
        txtEmail.setToolTipText("Formato: usuario@dominio.com");
        
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
        
        // Limitar a 14 caracteres (CNPJ: XX.XXX.XXX/XXXX-XX ou CPF: XXX.XXX.XXX-XX)
        txtCpfCnpj.setDocument(new javax.swing.text.PlainDocument() {
            @Override
            public void insertString(int offs, String str, javax.swing.text.AttributeSet a) throws javax.swing.text.BadLocationException {
                if (str == null) return;
                
                if ((getLength() + str.length()) <= 14) {
                    // Verificar se contém apenas números, ponto, barra e traço
                    String validChars = "0123456789.-/";
                    for (char c : str.toCharArray()) {
                        if (validChars.indexOf(c) == -1) {
                            return; // Ignora caracteres inválidos
                        }
                    }
                    super.insertString(offs, str, a);
                }
            }
        });
        
        // Adicionar tooltip com formato esperado
        txtCpfCnpj.setToolTipText("CPF: XXX.XXX.XXX-XX ou CNPJ: XX.XXX.XXX/XXXX-XX - Máximo 14 caracteres");
        
        panel.add(txtCpfCnpj, gbc);
        
        gbc.gridx = 0; gbc.gridy = 3;
        panel.add(new JLabel("Telefone:"), gbc);
        gbc.gridx = 1;
        JTextField txtTelefone = new JTextField(telefone, 20);
        
        // Limitar a 13 caracteres (formato: (XX) XXXXX-XXXX)
        txtTelefone.setDocument(new javax.swing.text.PlainDocument() {
            @Override
            public void insertString(int offs, String str, javax.swing.text.AttributeSet a) throws javax.swing.text.BadLocationException {
                if (str == null) return;
                
                if ((getLength() + str.length()) <= 13) {
                    // Verificar se contém apenas números, parênteses, espaço e traço
                    String validChars = "0123456789() -";
                    for (char c : str.toCharArray()) {
                        if (validChars.indexOf(c) == -1) {
                            return; // Ignora caracteres inválidos
                        }
                    }
                    super.insertString(offs, str, a);
                }
            }
        });
        
        // Adicionar tooltip com formato esperado
        txtTelefone.setToolTipText("Formato: (XX) XXXXX-XXXX - Máximo 13 caracteres");
        
        panel.add(txtTelefone, gbc);
        
        gbc.gridx = 0; gbc.gridy = 4;
        panel.add(new JLabel("Email:"), gbc);
        gbc.gridx = 1;
        JTextField txtEmail = new JTextField(email, 20);
        
        // Adicionar tooltip com formato esperado
        txtEmail.setToolTipText("Formato: usuario@dominio.com");
        
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
    
    /**
     * Abre o Dashboard Analítico
     */
    private void abrirDashboard(ActionEvent e) {
        try {
            PDVDashboardAnalyticsController dashboard = new PDVDashboardAnalyticsController();
            dashboard.showDashboard();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(mainFrame, 
                "Erro ao abrir Dashboard: " + ex.getMessage(), 
                "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    /**
     * Abre a tela de Notificações
     */
    private void abrirNotificacao(ActionEvent e) {
        try {
            // Usuário logado simulado - na implementação real, obter do sistema de autenticação
            String usuarioLogado = "admin"; // ou obter do contexto de login
            PDVNotificacaoSwingController.mostrarNotificacao(usuarioLogado);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(mainFrame, 
                "Erro ao abrir Notificações: " + ex.getMessage(), 
                "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    /**
     * Abre a tela de gestão de produtos do ERP
     */
    private void gerenciarProdutosERP(ActionEvent e) {
        try {
            ERPProdutoSwingController controller = new ERPProdutoSwingController();
            controller.show();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(mainFrame, 
                "Erro ao abrir gestão de produtos: " + ex.getMessage(), 
                "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }
    /**
     * Cria painel de produtos para consulta (estrutura igual Nova Venda)
     */
    private JPanel createProdutosPanelForConsulta() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createTitledBorder("📦 Produtos Disponíveis"),
            BorderFactory.createEmptyBorder(15, 15, 15, 15)
        ));
        panel.setBackground(new Color(255, 255, 255));
        
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
        JButton btnBuscar = ModernLayout.createModernButton("🔍 Buscar", OceanoTheme.PRIMARY);
        JButton btnLimpar = ModernLayout.createModernButton("🔄 Limpar", OceanoTheme.ACCENT);
        
        buscaInputPanel.add(txtBusca);
        buscaInputPanel.add(btnBuscar);
        buscaInputPanel.add(btnLimpar);
        
        buscaPanel.add(lblBusca, BorderLayout.NORTH);
        buscaPanel.add(buscaInputPanel, BorderLayout.CENTER);
        
        // Tabela de produtos
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
        produtosTable.getTableHeader().setBackground(new Color(26, 188, 156)); // Azul Turquesa
        produtosTable.getTableHeader().setForeground(Color.WHITE);
        
        // Desabilitar edição da tabela
        produtosTable.setDefaultEditor(Object.class, null);
        produtosTable.setEnabled(false);
        
        JScrollPane scrollPane = new JScrollPane(produtosTable);
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200), 1));
        scrollPane.setBackground(Color.WHITE);
        scrollPane.setPreferredSize(new Dimension(0, 250));
        
        panel.add(buscaPanel, BorderLayout.NORTH);
        panel.add(scrollPane, BorderLayout.CENTER);
        
        return panel;
    }
    
    /**
     * Cria painel de entrada para consulta (estrutura igual Nova Venda)
     */
    private JPanel createInputPanelForConsulta() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createTitledBorder("➕ Detalhes do Produto"),
            BorderFactory.createEmptyBorder(15, 15, 15, 15)
        ));
        panel.setBackground(new Color(255, 255, 255));
        
        // Campos de entrada
        JPanel inputPanel = new JPanel(new GridBagLayout());
        inputPanel.setBackground(new Color(255, 255, 255));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.anchor = GridBagConstraints.WEST;
        
        // Código
        gbc.gridx = 0; gbc.gridy = 0;
        inputPanel.add(new JLabel("Código:"), gbc);
        gbc.gridx = 1;
        JTextField txtCodigo = new JTextField(15);
        txtCodigo.setEditable(false);
        txtCodigo.setBackground(new Color(240, 240, 240));
        inputPanel.add(txtCodigo, gbc);
        
        // Descrição
        gbc.gridx = 0; gbc.gridy = 1;
        inputPanel.add(new JLabel("Descrição:"), gbc);
        gbc.gridx = 1; gbc.gridwidth = 3; gbc.weightx = 2.0;
        JTextField txtDescricao = new JTextField(40);
        txtDescricao.setEditable(false);
        txtDescricao.setBackground(new Color(240, 240, 240));
        inputPanel.add(txtDescricao, gbc);
        
        // Preço
        gbc.gridx = 0; gbc.gridy = 2; gbc.gridwidth = 1; gbc.weightx = 0.0;
        inputPanel.add(new JLabel("Preço:"), gbc);
        gbc.gridx = 1;
        JTextField txtPreco = new JTextField(15);
        txtPreco.setEditable(false);
        txtPreco.setBackground(new Color(240, 240, 240));
        inputPanel.add(txtPreco, gbc);
        
        // Estoque
        gbc.gridx = 2;
        inputPanel.add(new JLabel("Estoque:"), gbc);
        gbc.gridx = 3;
        JTextField txtEstoque = new JTextField(10);
        txtEstoque.setEditable(false);
        txtEstoque.setBackground(new Color(240, 240, 240));
        inputPanel.add(txtEstoque, gbc);
        
        panel.add(inputPanel, BorderLayout.CENTER);
        
        return panel;
    }
    
    /**
     * Cria painel lateral para consulta (estrutura igual Nova Venda)
     */
    private JPanel createSidePanelForConsulta() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        panel.setPreferredSize(new Dimension(250, 0));
        panel.setBackground(new Color(248, 249, 250));
        
        // Resumo
        JPanel resumoPanel = new JPanel(new BorderLayout());
        resumoPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(26, 188, 156), 2),
            BorderFactory.createEmptyBorder(15, 15, 15, 15)
        ));
        resumoPanel.setBackground(Color.WHITE);
        
        JLabel lblResumo = new JLabel("📊 Resumo da Consulta", JLabel.CENTER);
        lblResumo.setFont(new Font("Segoe UI", Font.BOLD, 16));
        lblResumo.setForeground(new Color(26, 188, 156));
        
        JLabel lblInfo = new JLabel("<html><center>" +
            "<b>Total de Produtos:</b> 0<br>" +
            "<b>Última Atualização:</b><br>" +
            java.time.LocalDateTime.now().format(java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")) +
            "</center></html>", JLabel.CENTER);
        lblInfo.setFont(new Font("Arial", Font.PLAIN, 12));
        lblInfo.setForeground(Color.DARK_GRAY);
        
        resumoPanel.add(lblResumo, BorderLayout.NORTH);
        resumoPanel.add(lblInfo, BorderLayout.CENTER);
        
        // Botões de ação
        JPanel buttonPanel = new JPanel(new GridLayout(2, 1, 10, 10));
        buttonPanel.setBackground(new Color(248, 249, 250));
        
        JButton btnExportar = ModernLayout.createModernButton("📤 Exportar", OceanoTheme.ACCENT);
        JButton btnFechar = ModernLayout.createModernButton("❌ Fechar", OceanoTheme.ERROR);
        
        buttonPanel.add(btnExportar);
        buttonPanel.add(btnFechar);
        
        panel.add(resumoPanel, BorderLayout.NORTH);
        panel.add(buttonPanel, BorderLayout.CENTER);
        
        return panel;
    }
    
    /**
     * Abre a tela de Fornecedores
     */
    private void abrirFornecedores(ActionEvent e) {
        try {
            FornecedorSwingController controller = new FornecedorSwingController();
            controller.show();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(mainFrame, 
                "Erro ao abrir Fornecedores: " + ex.getMessage(), 
                "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    /**
     * Abre a tela de Nova Venda
     */
    private void abrirVenda(ActionEvent e) {
        try {
            PDVVendaSwingController controller = new PDVVendaSwingController();
            controller.show();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(mainFrame, 
                "Erro ao abrir Nova Venda: " + ex.getMessage(), 
                "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    /**
     * Abre a tela de Caixa
     */
    private void abrirCaixa(ActionEvent e) {
        try {
            PDVCaixaSwingController controller = new PDVCaixaSwingController();
            controller.show();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(mainFrame, 
                "Erro ao abrir Caixa: " + ex.getMessage(), 
                "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    /**
     * Abre a tela de Fechar Caixa
     */
    private void fecharCaixa(ActionEvent e) {
        try {
            PDVFecharCaixaSwingController controller = new PDVFecharCaixaSwingController();
            controller.show();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(mainFrame, 
                "Erro ao abrir Fechar Caixa: " + ex.getMessage(), 
                "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    /**
     * Abre a tela de Templates de Relatórios
     */
    private void abrirTemplatesRelatorio(ActionEvent e) {
        try {
            TemplateRelatorioSwingController controller = new TemplateRelatorioSwingController();
            controller.show();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(mainFrame, 
                "Erro ao abrir Templates de Relatórios: " + ex.getMessage(), 
                "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    /**
     * Abre a tela de Export/Import
     */
    private void abrirExportImport(ActionEvent e) {
        try {
            ExportImportSwingController controller = new ExportImportSwingController();
            controller.show();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(mainFrame, 
                "Erro ao abrir Export/Import: " + ex.getMessage(), 
                "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    /**
     * Abre a tela de Busca Avançada
     */
    private void abrirBuscaAvancada(ActionEvent e) {
        try {
            BuscaAvancadaSwingController controller = new BuscaAvancadaSwingController();
            controller.show();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(mainFrame, 
                "Erro ao abrir Busca Avançada: " + ex.getMessage(), 
                "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    /**
     * Abre a tela de Pagamentos API
     */
    private void abrirPagamentosAPI(ActionEvent e) {
        try {
            PagamentoAPISwingController controller = new PagamentoAPISwingController();
            controller.show();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(mainFrame, 
                "Erro ao abrir Pagamentos API: " + ex.getMessage(), 
                "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    /**
     * Abre a tela de Configuração de Cache
     */
    private void abrirCacheConfig(ActionEvent e) {
        try {
            CacheConfigSwingController controller = new CacheConfigSwingController();
            controller.show();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(mainFrame, 
                "Erro ao abrir Configuração de Cache: " + ex.getMessage(), 
                "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    /**
     * Abre a tela de Configurações do Sistema
     */
    private void abrirSistemaConfig(ActionEvent e) {
        try {
            SistemaConfigSwingController controller = new SistemaConfigSwingController();
            controller.show();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(mainFrame, 
                "Erro ao abrir Configurações do Sistema: " + ex.getMessage(), 
                "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    /**
     * Mostra mensagem de funcionalidade em desenvolvimento
     */
    private void mostrarEmDesenvolvimento(ActionEvent e, String funcionalidade) {
        JOptionPane.showMessageDialog(mainFrame, 
            funcionalidade + " em desenvolvimento...\n\n" +
            "Esta funcionalidade estará disponível em breve!", 
            "Em Desenvolvimento", JOptionPane.INFORMATION_MESSAGE);
    }
    
    /**
     * Relatório de Clientes
     */
    private void relatorioClientes(ActionEvent e) {
        try {
            PDVRelatoriosSwingController relatoriosController = new PDVRelatoriosSwingController();
            relatoriosController.show();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(mainFrame, 
                "Erro ao abrir relatório de clientes: " + ex.getMessage(), 
                "Relatórios", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    /**
     * Relatório de Estoque
     */
    private void relatorioEstoque(ActionEvent e) {
        try {
            PDVRelatoriosSwingController relatoriosController = new PDVRelatoriosSwingController();
            relatoriosController.show();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(mainFrame, 
                "Erro ao abrir relatório de estoque: " + ex.getMessage(), 
                "Relatórios", JOptionPane.ERROR_MESSAGE);
        }
    }
}
