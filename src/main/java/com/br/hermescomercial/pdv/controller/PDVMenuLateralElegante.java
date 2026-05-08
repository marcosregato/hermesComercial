package com.br.hermescomercial.pdv.controller;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.awt.event.*;

import com.br.hermescomercial.util.SystemLogger;
// Dark Mode removido devido a erros de implementação

/**
 * Menu Lateral com Submenus em Cascade - Versão Elegante
 * Design moderno e elegante com animações suaves e visual profissional
 */
public class PDVMenuLateralElegante {
    
    // Componentes do menu
    private JPanel menuPanel;
    private JPanel workArea;
    private String usuarioAtual;
    private String nomeUsuario;
    
    // Variáveis para controle de tela ativa
    private String telaAtual;
    private String moduloAtual;
    private String tipoTelaAtual;
    private java.util.Date timestampTelaAtual;
    
    // Labels para painel de informações
    private JLabel lblInfoTela;
    private JLabel lblInfoModulo;
    private JLabel lblInfoTipo;
    private JLabel lblInfoUsuario;
    private JLabel lblInfoTimestamp;
    private JLabel lblInfoClasse;
    
    // Cores gerenciadas pelo ThemeManager
    // Removendo cores estáticas para usar ThemeManager dinamicamente
    private static final Color PRIMARY_COLOR = new Color(41, 128, 185);      // Azul elegante
    private static final Color SECONDARY_COLOR = new Color(52, 73, 94);      // Cinza escuro
    private static final Color ACCENT_COLOR = new Color(231, 76, 60);        // Vermelho elegante
    private static final Color SUCCESS_COLOR = new Color(39, 174, 96);       // Verde elegante
    private static final Color WARNING_COLOR = new Color(243, 156, 18);      // Amarelo elegante
    @SuppressWarnings("unused")
    private static final Color LIGHT_GRAY = new Color(245, 247, 250);        // Cinza claro
    private static final Color MEDIUM_GRAY = new Color(189, 195, 199);       // Cinza médio
    private static final Color DARK_GRAY = new Color(127, 140, 141);         // Cinza escuro
    @SuppressWarnings("unused")
    private static final Color BLACK = new Color(44, 62, 80);                // Preto elegante
    @SuppressWarnings("unused")
    private static final Color WHITE = new Color(255, 255, 255);              // Branco elegante
    
    public PDVMenuLateralElegante(String usuario, String nome, JPanel workArea) {
        this.usuarioAtual = usuario;
        this.nomeUsuario = nome;
        this.workArea = workArea;
        SystemLogger.ui("Criando menu lateral elegante para usuário: " + usuario);
    }
    
    public JPanel createMenuPanel() {
        try {
            menuPanel = new JPanel(new BorderLayout()) {
                @Override
                protected void paintComponent(Graphics g) {
                    super.paintComponent(g);
                    Graphics2D g2d = (Graphics2D) g.create();
                    g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                    
                    // Gradiente sutil de fundo com cores dinâmicas
                    Color startColor = false ? 
                        new Color(30, 30, 30) : new Color(250, 252, 255);
                    Color endColor = false ? 
                        new Color(45, 45, 45) : new Color(240, 244, 248);
                    GradientPaint gradient = new GradientPaint(0, 0, startColor, 
                                                            0, getHeight(), endColor);
                    g2d.setPaint(gradient);
                    g2d.fillRect(0, 0, getWidth(), getHeight());
                    g2d.dispose();
                }
            };
            menuPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
            menuPanel.setPreferredSize(new Dimension(300, 0));
            
            // Header elegante do menu
            JPanel headerPanel = createMenuHeaderElegante();
            menuPanel.add(headerPanel, BorderLayout.NORTH);
            
            // Painel de menus com scroll elegante
            JPanel menusPanel = new JPanel();
            menusPanel.setLayout(new BoxLayout(menusPanel, BoxLayout.Y_AXIS));
            menusPanel.setOpaque(false);
            menusPanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));
            
            // Criar menus principais elegantes
            createMenuPDVElegante(menusPanel);
            createMenuVendasElegante(menusPanel);
            createMenuProdutosElegante(menusPanel);
            createMenuClientesElegante(menusPanel);
            createMenuEstoqueElegante(menusPanel);
            createMenuFinanceiroElegante(menusPanel);
            createMenuRelatoriosElegante(menusPanel);
            createMenuConfiguracoesElegante(menusPanel);
            
            // Adicionar espaço no final
            menusPanel.add(Box.createVerticalStrut(20));
            
            // Scroll elegante para menus
            JScrollPane scrollPane = new JScrollPane(menusPanel) {
                @Override
                protected void paintComponent(Graphics g) {
                    super.paintComponent(g);
                    Graphics2D g2d = (Graphics2D) g.create();
                    g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                    
                    // Borda arredondada sutil
                    g2d.setColor(new Color(200, 200, 200, 50));
                    g2d.drawRoundRect(0, 0, getWidth()-1, getHeight()-1, 8, 8);
                    g2d.dispose();
                }
            };
            scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
            scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
            scrollPane.setBorder(BorderFactory.createEmptyBorder());
            scrollPane.setOpaque(false);
            scrollPane.getViewport().setOpaque(false);
            scrollPane.getVerticalScrollBar().setUnitIncrement(16);
            scrollPane.getVerticalScrollBar().setBlockIncrement(16);
            scrollPane.getVerticalScrollBar().setUI(new ElegantScrollBarUI());
            
            menuPanel.add(scrollPane, BorderLayout.CENTER);
            
            SystemLogger.ui("Menu lateral elegante criado com 8 menus principais");
            return menuPanel;
            
        } catch (Exception e) {
            SystemLogger.error("Erro ao criar menu lateral elegante", e);
            // Retornar painel básico em caso de erro
            JPanel errorPanel = new JPanel(new BorderLayout());
            errorPanel.setBackground(WHITE);
            JLabel errorLabel = new JLabel("❌ Erro no menu", JLabel.CENTER);
            errorPanel.add(errorLabel, BorderLayout.CENTER);
            return errorPanel;
        }
    }
    
    /**
     * Header elegante do menu
     */
    private JPanel createMenuHeaderElegante() {
        JPanel header = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                // Gradiente elegante
                GradientPaint gradient = new GradientPaint(0, 0, PRIMARY_COLOR, 
                                                        0, getHeight(), SECONDARY_COLOR);
                g2d.setPaint(gradient);
                g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 15, 15);
                g2d.dispose();
            }
        };
        header.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        header.setPreferredSize(new Dimension(0, 100));
        header.setOpaque(false);
        
        // Logo e título elegante
        JPanel logoPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        logoPanel.setOpaque(false);
        
        JLabel logoLabel = new JLabel("🏪") {
            @Override
            public void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2d.setFont(new Font("Segoe UI", Font.BOLD, 32));
                g2d.setColor(WHITE);
                g2d.drawString(getText(), 5, 35);
                g2d.dispose();
            }
        };
        
        JLabel titleLabel = new JLabel("MENU") {
            @Override
            public void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2d.setFont(new Font("Segoe UI", Font.BOLD, 20));
                g2d.setColor(WHITE);
                g2d.drawString(getText(), 5, 35);
                g2d.dispose();
            }
        };
        
        logoPanel.add(logoLabel);
        logoPanel.add(Box.createHorizontalStrut(10));
        logoPanel.add(titleLabel);
        
        // Informações do usuário elegante
        JPanel userPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        userPanel.setOpaque(false);
        
        JPanel userCard = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                // Fundo semi-transparente
                g2d.setColor(new Color(255, 255, 255, 30));
                g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 8, 8);
                g2d.dispose();
            }
        };
        userCard.setBorder(BorderFactory.createEmptyBorder(8, 12, 8, 12));
        userCard.setOpaque(false);
        
        JLabel userIcon = new JLabel("👤");
        userIcon.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        userIcon.setForeground(WHITE);
        
        JLabel userLabel = new JLabel(nomeUsuario);
        userLabel.setFont(new Font("Segoe UI", Font.BOLD, 13));
        userLabel.setForeground(WHITE);
        
        userCard.add(userIcon, BorderLayout.WEST);
        userCard.add(userLabel, BorderLayout.CENTER);
        
        userPanel.add(userCard);
        
        header.add(logoPanel, BorderLayout.WEST);
        header.add(userPanel, BorderLayout.EAST);
        
        return header;
    }
    
    /**
     * Cria menus PDV elegantes
     */
    private void createMenuPDVElegante(JPanel parent) {
        createCascadeMenuElegante(parent, "🏪 PDV", new String[]{
            "🛒 Ponto de Venda",
            "💳 Pagamentos",
            "🧾 Cupom Fiscal",
            "📱 Terminal Venda",
            "🎯 Venda Rápida"
        }, "PDV", PRIMARY_COLOR);
    }
    
    /**
     * Cria menus Vendas elegantes
     */
    private void createMenuVendasElegante(JPanel parent) {
        createCascadeMenuElegante(parent, "💰 Vendas", new String[]{
            "📋 Nova Venda",
            "🔍 Consultar Vendas",
            "↩️ Devoluções",
            "📊 Resumo Diário",
            "📈 Relatório de Vendas",
            "🏷️ Orçamentos",
            "🚚 Entregas"
        }, "VENDAS", SUCCESS_COLOR);
    }
    
    /**
     * Cria menus Produtos elegantes
     */
    private void createMenuProdutosElegante(JPanel parent) {
        createCascadeMenuElegante(parent, "📦 Produtos", new String[]{
            "🔧 Gestão de Produto",
            "📂 Categorias",
            "🏷️ Fornecedores",
            "📊 Estoque Mínimo",
            "🏷️ Códigos de Barras"
        }, "PRODUTOS", WARNING_COLOR);
    }
    
    /**
     * Cria menus Clientes elegantes
     */
    private void createMenuClientesElegante(JPanel parent) {
        createCascadeMenuElegante(parent, "👥 Clientes", new String[]{
            "🔧 Gestão de Cliente",
            "📊 Histórico de Compras",
            "💳 Carteira Fidelidade",
            "📍 Endereços",
            "📞 Contatos"
        }, "CLIENTES", new Color(155, 89, 182));
    }
    
    /**
     * Cria menus Estoque elegantes
     */
    private void createMenuEstoqueElegante(JPanel parent) {
        createCascadeMenuElegante(parent, "📊 Estoque", new String[]{
            "🔧 Gestão de Estoque",
            "➕ Entrada de Mercadoria",
            "➖ Saída de Mercadoria",
            "🔄 Transferência",
            "📊 Inventário",
            "⚠️ Estoque Baixo",
            "📈 Movimentações"
        }, "ESTOQUE", new Color(26, 188, 156));
    }
    
    /**
     * Cria menus Financeiro elegantes
     */
    private void createMenuFinanceiroElegante(JPanel parent) {
        createCascadeMenuElegante(parent, "🏦 Financeiro", new String[]{
            "🔧 Gestão de Contas",
            "🏦 Fluxo de Caixa",
            "📊 Fechamento de Caixa",
            "💳 Conciliação Bancária",
            "💵 Cobranças"
        }, "FINANCEIRO", new Color(230, 126, 34));
    }
    
    /**
     * Cria menus Relatórios elegantes
     */
    private void createMenuRelatoriosElegante(JPanel parent) {
        createCascadeMenuElegante(parent, "📈 Relatórios", new String[]{
            "🔧 Gestão de Relatórios",
            "📊 Dashboard",
            "🎯 Análise de Vendas"
        }, "RELATORIOS", new Color(52, 152, 219));
    }
    
    /**
     * Cria menus Configurações elegantes
     */
    private void createMenuConfiguracoesElegante(JPanel parent) {
        createCascadeMenuElegante(parent, "⚙️ Configurações", new String[]{
            "👤 Usuários e Permissões",
            "🏢 Empresa",
            "🖥️ Sistema",
            "🖨️ Impressoras",
            "💳 Formas de Pagamento",
            "🌐 Integrações",
            "🔧 Parâmetros",
            "💾 Backup"
        }, "CONFIGURACOES", DARK_GRAY);
    }
    
    /**
     * Cria menu cascade elegante
     */
    private void createCascadeMenuElegante(JPanel parent, String menuTitle, String[] subMenus, String module, Color accentColor) {
        try {
            // Painel principal do menu elegante
            JPanel menuPanel = new JPanel(new BorderLayout()) {
                @Override
                protected void paintComponent(Graphics g) {
                    super.paintComponent(g);
                    Graphics2D g2d = (Graphics2D) g.create();
                    g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                    
                    // Fundo elegante com borda arredondada
                    g2d.setColor(WHITE);
                    g2d.fillRoundRect(2, 2, getWidth()-4, getHeight()-4, 12, 12);
                    
                    // Borda sutil
                    g2d.setColor(new Color(200, 200, 200));
                    g2d.drawRoundRect(2, 2, getWidth()-4, getHeight()-4, 12, 12);
                    
                    // Sombra sutil
                    g2d.setColor(new Color(0, 0, 0, 20));
                    g2d.fillRoundRect(4, 4, getWidth()-4, getHeight()-4, 12, 12);
                    g2d.dispose();
                }
            };
            menuPanel.setBorder(BorderFactory.createEmptyBorder(8, 12, 8, 12));
            menuPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 60));
            menuPanel.setMinimumSize(new Dimension(0, 60));
            menuPanel.setPreferredSize(new Dimension(0, 60));
            menuPanel.setOpaque(false);
            
            // Botão principal elegante
            ElegantButton mainButton = new ElegantButton(menuTitle, accentColor, true);
            mainButton.setHorizontalAlignment(SwingConstants.LEFT);
            mainButton.setFont(new Font("Segoe UI", Font.BOLD, 15));
            
            // Painel de submenus elegante (inicialmente oculto)
            JPanel subMenuPanel = new JPanel();
            subMenuPanel.setLayout(new BoxLayout(subMenuPanel, BoxLayout.Y_AXIS));
            subMenuPanel.setOpaque(false);
            subMenuPanel.setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));
            subMenuPanel.setVisible(false);
            
            // Criar botões de submenu elegantes
            for (String subMenu : subMenus) {
                ElegantButton subButton = createSubMenuButtonElegante(subMenu, module, accentColor);
                subMenuPanel.add(subButton);
                subMenuPanel.add(Box.createVerticalStrut(4));
            }
            
            // Adicionar componentes ao painel principal
            menuPanel.add(mainButton, BorderLayout.NORTH);
            menuPanel.add(subMenuPanel, BorderLayout.CENTER);
            
            // VARIÁVEL FINAL PARA CAPTURA CORRETA
            final JPanel finalSubMenuPanel = subMenuPanel;
            final ElegantButton finalMainButton = mainButton;
            final String finalMenuTitle = menuTitle;
            
            // Ação do botão principal (toggle submenu) otimizado para performance
            mainButton.addActionListener(e -> {
                try {
                    // Verificar estado atual
                    boolean isVisible = finalSubMenuPanel.isVisible();
                    
                    // Toggle visibility imediato (sem logging excessivo para performance)
                    finalSubMenuPanel.setVisible(!isVisible);
                    
                    // Atualizar texto do botão com ícone imediato
                    if (isVisible) {
                        finalMainButton.setText(finalMenuTitle.replace("▼", "▶"));
                    } else {
                        finalMainButton.setText(finalMenuTitle.replace("▶", "▼"));
                    }
                    
                    // Atualização do layout otimizada - sem SwingUtilities.invokeLater para performance
                    try {
                        // Atualizar tamanho do painel principal imediatamente
                        if (!isVisible) {
                            int newHeight = 60 + (subMenus.length * 42);
                            menuPanel.setPreferredSize(new Dimension(menuPanel.getPreferredSize().width, newHeight));
                            menuPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, newHeight));
                        } else {
                            menuPanel.setPreferredSize(new Dimension(menuPanel.getPreferredSize().width, 60));
                            menuPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 60));
                        }
                        
                        // Revalidação única e eficiente
                        menuPanel.revalidate();
                        parent.revalidate();
                        
                        // Repaint mínimo necessário
                        menuPanel.repaint();
                        
                    } catch (Exception ex) {
                        // Tratamento silencioso para não afetar performance
                    }
                    
                } catch (Exception ex) {
                    SystemLogger.error("Erro ao toggle menu elegante: " + menuTitle, ex);
                }
            });
            
            // Adicionar espaçamento elegante entre menus
            parent.add(Box.createVerticalStrut(8));
            parent.add(menuPanel);
            parent.add(Box.createVerticalStrut(8));
            
            // Forçar revalidação imediata
            parent.revalidate();
            parent.repaint();
            
        } catch (Exception e) {
            SystemLogger.error("Erro ao criar menu cascade elegante: " + menuTitle, e);
            // Adicionar menu simples em caso de erro
            ElegantButton errorButton = new ElegantButton("❌ " + menuTitle, ACCENT_COLOR, false);
            parent.add(errorButton);
            parent.add(Box.createVerticalStrut(8));
        }
    }
    
    /**
     * Cria botão de submenu elegante
     */
    private ElegantButton createSubMenuButtonElegante(String text, String module, Color accentColor) {
        ElegantButton button = new ElegantButton(text, accentColor.brighter(), false);
        button.setHorizontalAlignment(SwingConstants.LEFT);
        button.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        button.setMaximumSize(new Dimension(Integer.MAX_VALUE, 38));
        button.setMinimumSize(new Dimension(0, 38));
        button.setPreferredSize(new Dimension(0, 38));
        
        // Ação do submenu
        button.addActionListener(e -> {
            try {
                SystemLogger.operation("SUBMENU_CLICK", module, 
                    "Usuário: " + usuarioAtual + " acessou: " + text);
                
                // Tratar clique em "Gestão de Cliente" para criar submenu
                if (text.equals("🔧 Gestão de Cliente")) {
                    criarSubmenuGestaoCliente(module, accentColor);
                    return;
                }
                
                // Tratar clique em "Gestão de Estoque" para criar submenu
                if (text.equals("🔧 Gestão de Estoque")) {
                    criarSubmenuGestaoEstoque(module, accentColor);
                    return;
                }
                
                // Tratar clique em "Gestão de Produto" para criar submenu
                if (text.equals("🔧 Gestão de Produto")) {
                    criarSubmenuGestaoProduto(module, accentColor);
                    return;
                }
                
                // Log específico para Nova Venda
                if (text.equals("📋 Nova Venda")) {
                    SystemLogger.ui("🎯 DETECTADO CLIQUE EM NOVA VENDA!");
                    SystemLogger.ui("📋 NOVA VENDA - Iniciando fluxo de abertura");
                    SystemLogger.ui("📋 NOVA VENDA - Módulo identificado: " + module);
                    SystemLogger.ui("📋 NOVA VENDA - Usuário: " + usuarioAtual);
                    SystemLogger.ui("📋 NOVA VENDA - Timestamp: " + new java.util.Date());
                    SystemLogger.ui("📋 NOVA VENDA - Thread: " + Thread.currentThread().getName());
                    SystemLogger.info("NOVA VENDA - Clique detectado, iniciando abertura da tela");
                }
                
                // Abrir tela completa elegante
                SystemLogger.ui("🚀 ABRINDO TELA COMPLETA ELEGANTE: " + text);
                SystemLogger.info("TELA - Abrindo: " + text + " | Módulo: " + module);
                abrirTelaCompletaElegante(text, module);
                    
            } catch (Exception ex) {
                SystemLogger.error("Erro ao executar ação do submenu elegante: " + text, ex);
                JOptionPane.showMessageDialog(menuPanel, 
                    "❌ Erro ao executar: " + text + "\n\n" + ex.getMessage(), 
                    "Erro", JOptionPane.ERROR_MESSAGE);
            }
        });
        
        return button;
    }
    
    /**
     * Cria submenu de Gestão de Cliente
     */
    private void criarSubmenuGestaoCliente(String module, Color accentColor) {
        try {
            // Criar diálogo para opções de gestão de cliente
            JDialog dialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(workArea), 
                "🔧 Gestão de Cliente", true);
            dialog.setSize(300, 250);
            dialog.setLocationRelativeTo(workArea);
            dialog.setLayout(new BorderLayout());
            
            // Painel de opções
            JPanel optionsPanel = new JPanel(new GridLayout(3, 1, 10, 10));
            optionsPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
            optionsPanel.setBackground(WHITE);
            
            // Botão Novo Cliente
            ElegantButton btnNovoCliente = new ElegantButton("➕ Novo Cliente", new Color(39, 174, 96), false);
            btnNovoCliente.setForeground(WHITE);
            btnNovoCliente.setFont(new Font("Segoe UI", Font.BOLD, 14));
            btnNovoCliente.addActionListener(e -> {
                dialog.dispose();
                abrirTelaCompletaElegante("➕ Novo Cliente", module);
            });
            optionsPanel.add(btnNovoCliente);
            
            // Botão Editar Cliente
            ElegantButton btnEditarCliente = new ElegantButton("📝 Editar Cliente", new Color(230, 126, 34), false);
            btnEditarCliente.setForeground(WHITE);
            btnEditarCliente.setFont(new Font("Segoe UI", Font.BOLD, 14));
            btnEditarCliente.addActionListener(e -> {
                dialog.dispose();
                abrirTelaCompletaElegante("📝 Editar Cliente", module);
            });
            optionsPanel.add(btnEditarCliente);
            
            // Botão Consultar Cliente
            ElegantButton btnConsultarCliente = new ElegantButton("🔍 Consultar Cliente", new Color(52, 152, 219), false);
            btnConsultarCliente.setForeground(WHITE);
            btnConsultarCliente.setFont(new Font("Segoe UI", Font.BOLD, 14));
            btnConsultarCliente.addActionListener(e -> {
                dialog.dispose();
                abrirTelaCompletaElegante("🔍 Consultar Cliente", module);
            });
            optionsPanel.add(btnConsultarCliente);
            
            // Painel de botões de fechamento
            JPanel closePanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
            closePanel.setBackground(WHITE);
            closePanel.setBorder(BorderFactory.createEmptyBorder(0, 20, 10, 20));
            
            ElegantButton btnFechar = new ElegantButton("❌ Fechar", new Color(149, 165, 166), false);
            btnFechar.setForeground(WHITE);
            btnFechar.addActionListener(e -> dialog.dispose());
            closePanel.add(btnFechar);
            
            dialog.add(optionsPanel, BorderLayout.CENTER);
            dialog.add(closePanel, BorderLayout.SOUTH);
            
            // Estilizar diálogo
            dialog.getRootPane().setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(accentColor, 2),
                BorderFactory.createEmptyBorder(5, 5, 5, 5)
            ));
            
            dialog.setVisible(true);
            
        } catch (Exception ex) {
            SystemLogger.error("Erro ao criar submenu Gestão de Cliente", ex);
            JOptionPane.showMessageDialog(workArea, 
                "❌ Erro ao abrir opções de gestão de cliente", 
                "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    /**
     * Cria submenu de Gestão de Estoque
     */
    private void criarSubmenuGestaoEstoque(String module, Color accentColor) {
        try {
            // Criar diálogo para opções de gestão de estoque
            JDialog dialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(workArea), 
                "🔧 Gestão de Estoque", true);
            dialog.setSize(350, 350);
            dialog.setLocationRelativeTo(workArea);
            dialog.setLayout(new BorderLayout());
            
            // Painel de opções
            JPanel optionsPanel = new JPanel(new GridLayout(4, 1, 10, 10));
            optionsPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
            optionsPanel.setBackground(WHITE);
            
            // Botão Estoque Mínimo
            ElegantButton btnEstoqueMinimo = new ElegantButton("📊 Estoque Mínimo", new Color(241, 196, 15), false);
            btnEstoqueMinimo.setForeground(WHITE);
            btnEstoqueMinimo.setFont(new Font("Segoe UI", Font.BOLD, 14));
            btnEstoqueMinimo.addActionListener(e -> {
                dialog.dispose();
                abrirTelaCompletaElegante("📊 Estoque Mínimo", module);
            });
            optionsPanel.add(btnEstoqueMinimo);
            
            // Botão Consultar Estoque
            ElegantButton btnConsultarEstoque = new ElegantButton("📋 Consultar Estoque", new Color(52, 152, 219), false);
            btnConsultarEstoque.setForeground(WHITE);
            btnConsultarEstoque.setFont(new Font("Segoe UI", Font.BOLD, 14));
            btnConsultarEstoque.addActionListener(e -> {
                dialog.dispose();
                abrirTelaCompletaElegante("📋 Consultar Estoque", module);
            });
            optionsPanel.add(btnConsultarEstoque);
            
            // Botão Ajuste de Estoque
            ElegantButton btnAjusteEstoque = new ElegantButton("🔄 Ajuste de Estoque", new Color(155, 89, 182), false);
            btnAjusteEstoque.setForeground(WHITE);
            btnAjusteEstoque.setFont(new Font("Segoe UI", Font.BOLD, 14));
            btnAjusteEstoque.addActionListener(e -> {
                dialog.dispose();
                abrirTelaCompletaElegante("🔄 Ajuste de Estoque", module);
            });
            optionsPanel.add(btnAjusteEstoque);
            
            // Botão Relatório de Estoque
            ElegantButton btnRelatorioEstoque = new ElegantButton("📈 Relatório de Estoque", new Color(46, 204, 113), false);
            btnRelatorioEstoque.setForeground(WHITE);
            btnRelatorioEstoque.setFont(new Font("Segoe UI", Font.BOLD, 14));
            btnRelatorioEstoque.addActionListener(e -> {
                dialog.dispose();
                abrirTelaCompletaElegante("📈 Relatório de Estoque", module);
            });
            optionsPanel.add(btnRelatorioEstoque);
            
            // Painel de botões de fechamento
            JPanel closePanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
            closePanel.setBackground(WHITE);
            closePanel.setBorder(BorderFactory.createEmptyBorder(0, 20, 10, 20));
            
            ElegantButton btnFechar = new ElegantButton("❌ Fechar", new Color(149, 165, 166), false);
            btnFechar.setForeground(WHITE);
            btnFechar.addActionListener(e -> dialog.dispose());
            closePanel.add(btnFechar);
            
            dialog.add(optionsPanel, BorderLayout.CENTER);
            dialog.add(closePanel, BorderLayout.SOUTH);
            
            // Estilizar diálogo
            dialog.getRootPane().setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(accentColor, 2),
                BorderFactory.createEmptyBorder(5, 5, 5, 5)
            ));
            
            dialog.setVisible(true);
            
        } catch (Exception ex) {
            SystemLogger.error("Erro ao criar submenu Gestão de Estoque", ex);
            JOptionPane.showMessageDialog(workArea, 
                "❌ Erro ao abrir opções de gestão de estoque", 
                "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    /**
     * Cria submenu de Gestão de Produto
     */
    private void criarSubmenuGestaoProduto(String module, Color accentColor) {
        try {
            // Criar diálogo para opções de gestão de produto
            JDialog dialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(workArea), 
                "🔧 Gestão de Produto", true);
            dialog.setSize(320, 280);
            dialog.setLocationRelativeTo(workArea);
            dialog.setLayout(new BorderLayout());
            
            // Painel de opções
            JPanel optionsPanel = new JPanel(new GridLayout(3, 1, 10, 10));
            optionsPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
            optionsPanel.setBackground(WHITE);
            
            // Botão Cadastrar Produto
            ElegantButton btnCadastrarProduto = new ElegantButton("➕ Cadastrar Produto", new Color(39, 174, 96), false);
            btnCadastrarProduto.setForeground(WHITE);
            btnCadastrarProduto.setFont(new Font("Segoe UI", Font.BOLD, 14));
            btnCadastrarProduto.addActionListener(e -> {
                dialog.dispose();
                abrirTelaCompletaElegante("➕ Cadastrar Produto", module);
            });
            optionsPanel.add(btnCadastrarProduto);
            
            // Botão Editar Produto
            ElegantButton btnEditarProduto = new ElegantButton("📝 Editar Produto", new Color(230, 126, 34), false);
            btnEditarProduto.setForeground(WHITE);
            btnEditarProduto.setFont(new Font("Segoe UI", Font.BOLD, 14));
            btnEditarProduto.addActionListener(e -> {
                dialog.dispose();
                abrirTelaCompletaElegante("📝 Editar Produto", module);
            });
            optionsPanel.add(btnEditarProduto);
            
            // Botão Consultar Produto
            ElegantButton btnConsultarProduto = new ElegantButton("🔍 Consultar Produto", new Color(52, 152, 219), false);
            btnConsultarProduto.setForeground(WHITE);
            btnConsultarProduto.setFont(new Font("Segoe UI", Font.BOLD, 14));
            btnConsultarProduto.addActionListener(e -> {
                dialog.dispose();
                abrirTelaCompletaElegante("🔍 Consultar Produto", module);
            });
            optionsPanel.add(btnConsultarProduto);
            
            // Painel de botões de fechamento
            JPanel closePanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
            closePanel.setBackground(WHITE);
            closePanel.setBorder(BorderFactory.createEmptyBorder(0, 20, 10, 20));
            
            ElegantButton btnFechar = new ElegantButton("❌ Fechar", new Color(149, 165, 166), false);
            btnFechar.setForeground(WHITE);
            btnFechar.addActionListener(e -> dialog.dispose());
            closePanel.add(btnFechar);
            
            dialog.add(optionsPanel, BorderLayout.CENTER);
            dialog.add(closePanel, BorderLayout.SOUTH);
            
            // Estilizar diálogo
            dialog.getRootPane().setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(accentColor, 2),
                BorderFactory.createEmptyBorder(5, 5, 5, 5)
            ));
            
            dialog.setVisible(true);
            
        } catch (Exception ex) {
            SystemLogger.error("Erro ao criar submenu Gestão de Produto", ex);
            JOptionPane.showMessageDialog(workArea, 
                "❌ Erro ao abrir opções de gestão de produto", 
                "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    /**
     * Verifica e exibe qual tela está ativa no sistema (genérico para todos os submenus)
     */
    private void verificarTelaAtiva(String item, String module) {
        // Armazenar informações da tela atual
        this.telaAtual = item;
        this.moduloAtual = module;
        this.tipoTelaAtual = identificarTipoTela(item, module);
        this.timestampTelaAtual = new java.util.Date();
        
        SystemLogger.ui("=== VERIFICAÇÃO DE TELA ATIVA - SISTEMA COMPLETO ===");
        SystemLogger.ui("🔍 TELA ATUAL IDENTIFICADA:");
        SystemLogger.ui("   • Classe: " + this.getClass().getSimpleName());
        SystemLogger.ui("   • Arquivo: PDVMenuLateralElegante.java");
        SystemLogger.ui("   • Método: abrirTelaCompletaElegante()");
        SystemLogger.ui("   • Item: " + item);
        SystemLogger.ui("   • Módulo: " + module);
        SystemLogger.ui("   • Tipo de Tela: " + this.tipoTelaAtual);
        SystemLogger.ui("   • Usuário: " + usuarioAtual);
        SystemLogger.ui("   • Timestamp: " + this.timestampTelaAtual);
        SystemLogger.ui("   • Thread: " + Thread.currentThread().getName());
        SystemLogger.ui("   • PID: " + ProcessHandle.current().pid());
        
        // Log específico para Nova Venda
        if (item.equals("📋 Nova Venda")) {
            SystemLogger.ui("✅ TELA NOVA VENDA ATIVA - ÚNICA IMPLEMENTAÇÃO");
            SystemLogger.info("VERIFICAÇÃO - Tela Nova Venda ativa: " + this.getClass().getSimpleName());
        } else {
            SystemLogger.ui("✅ TELA ATIVA: " + item + " - Módulo: " + module);
            SystemLogger.info("VERIFICAÇÃO - Tela ativa: " + item + " | Classe: " + this.getClass().getSimpleName());
        }
        
        // Atualizar painel de informações em tempo real
        atualizarPainelInformacoesTela();
    }
    
    /**
     * Cria um painel de informações em tempo real sobre a tela ativa
     */
    private JPanel criarPainelInformacoesTela() {
        JPanel infoPanel = new JPanel(new BorderLayout());
        infoPanel.setOpaque(false);
        infoPanel.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createEtchedBorder(), "📊 Informações da Tela Ativa",
            TitledBorder.LEFT, TitledBorder.TOP, new Font("Segoe UI", Font.BOLD, 11),
            DARK_GRAY));
        
        // Painel de informações
        JPanel dadosPanel = new JPanel(new GridLayout(0, 2, 5, 3));
        dadosPanel.setOpaque(false);
        
        lblInfoTela = new JLabel("Tela: Nenhuma");
        lblInfoModulo = new JLabel("Módulo: N/A");
        lblInfoTipo = new JLabel("Tipo: N/A");
        lblInfoUsuario = new JLabel("Usuário: " + usuarioAtual);
        lblInfoTimestamp = new JLabel("Atualizado: N/A");
        lblInfoClasse = new JLabel("Classe: " + this.getClass().getSimpleName());
        
        // Configurar labels
        Font fontInfo = new Font("Segoe UI", Font.PLAIN, 10);
        lblInfoTela.setFont(fontInfo);
        lblInfoModulo.setFont(fontInfo);
        lblInfoTipo.setFont(fontInfo);
        lblInfoUsuario.setFont(fontInfo);
        lblInfoTimestamp.setFont(fontInfo);
        lblInfoClasse.setFont(fontInfo);
        
        dadosPanel.add(new JLabel("Tela Ativa:"));
        dadosPanel.add(lblInfoTela);
        dadosPanel.add(new JLabel("Módulo:"));
        dadosPanel.add(lblInfoModulo);
        dadosPanel.add(new JLabel("Tipo:"));
        dadosPanel.add(lblInfoTipo);
        dadosPanel.add(new JLabel("Usuário:"));
        dadosPanel.add(lblInfoUsuario);
        dadosPanel.add(new JLabel("Atualizado:"));
        dadosPanel.add(lblInfoTimestamp);
        dadosPanel.add(new JLabel("Classe:"));
        dadosPanel.add(lblInfoClasse);
        
        infoPanel.add(dadosPanel, BorderLayout.CENTER);
        
        return infoPanel;
    }
    
    /**
     * Atualiza o painel de informações da tela
     */
    private void atualizarPainelInformacoesTela() {
        if (lblInfoTela != null && telaAtual != null) {
            lblInfoTela.setText("Tela: " + telaAtual);
            lblInfoModulo.setText("Módulo: " + moduloAtual);
            lblInfoTipo.setText("Tipo: " + tipoTelaAtual);
            lblInfoTimestamp.setText("Atualizado: " + new java.text.SimpleDateFormat("HH:mm:ss").format(timestampTelaAtual));
            
            // Destaque especial para Nova Venda
            if (telaAtual.equals("📋 Nova Venda")) {
                lblInfoTela.setForeground(PRIMARY_COLOR);
                lblInfoModulo.setForeground(PRIMARY_COLOR);
                lblInfoTipo.setForeground(PRIMARY_COLOR);
            } else {
                lblInfoTela.setForeground(DARK_GRAY);
                lblInfoModulo.setForeground(DARK_GRAY);
                lblInfoTipo.setForeground(DARK_GRAY);
            }
        }
    }
    
    /**
     * Identifica o tipo de tela baseado no item e módulo
     */
    private String identificarTipoTela(String item, String module) {
        switch (module) {
            case "VENDAS":
                if (item.equals("📋 Nova Venda")) return "FORMULÁRIO DE VENDAS";
                if (item.equals("🔍 Consultar Vendas")) return "CONSULTA DE VENDAS";
                if (item.equals("↩️ Devoluções")) return "FORMULÁRIO DE DEVOLUÇÕES";
                if (item.equals("📊 Resumo Diário")) return "RELATÓRIO DIÁRIO";
                if (item.equals("📈 Relatório de Vendas")) return "RELATÓRIO DE VENDAS";
                if (item.equals("🏷️ Orçamentos")) return "FORMULÁRIO DE ORÇAMENTOS";
                if (item.equals("🚚 Entregas")) return "FORMULÁRIO DE ENTREGAS";
                break;
            case "PRODUTOS":
                if (item.contains("Cadastrar")) return "FORMULÁRIO DE CADASTRO";
                if (item.contains("Editar")) return "FORMULÁRIO DE EDIÇÃO";
                if (item.contains("Consultar")) return "CONSULTA DE PRODUTOS";
                if (item.contains("Categorias")) return "GERENCIAMENTO DE CATEGORIAS";
                if (item.contains("Fornecedores")) return "GERENCIAMENTO DE FORNECEDORES";
                if (item.contains("Estoque")) return "CONTROLE DE ESTOQUE";
                if (item.contains("Códigos")) return "GERENCIAMENTO DE CÓDIGOS";
                break;
            case "CLIENTES":
                if (item.contains("Cadastrar")) return "FORMULÁRIO DE CADASTRO";
                if (item.contains("Editar")) return "FORMULÁRIO DE EDIÇÃO";
                if (item.contains("Consultar")) return "CONSULTA DE CLIENTES";
                break;
            case "ESTOQUE":
                if (item.contains("Entrada")) return "FORMULÁRIO DE ENTRADA";
                if (item.contains("Saída")) return "FORMULÁRIO DE SAÍDA";
                if (item.contains("Consultar")) return "CONSULTA DE ESTOQUE";
                if (item.contains("Relatório")) return "RELATÓRIO DE ESTOQUE";
                break;
            case "FINANCEIRO":
                if (item.contains("Contas")) return "GERENCIAMENTO DE CONTAS";
                if (item.contains("Fluxo")) return "FLUXO DE CAIXA";
                if (item.contains("Relatório")) return "RELATÓRIO FINANCEIRO";
                break;
            case "RELATÓRIOS":
                return "RELATÓRIO SISTEMA";
            case "CONFIGURAÇÕES":
                if (item.contains("Usuários")) return "GERENCIAMENTO DE USUÁRIOS";
                if (item.contains("Sistema")) return "CONFIGURAÇÕES DO SISTEMA";
                if (item.contains("Backup")) return "BACKUP/RESTAURAÇÃO";
                break;
        }
        return "TELA NÃO IDENTIFICADA";
    }
    
    /**
     * Verifica e exibe qual tela Nova Venda está ativa (método legacy)
     */
    private void verificarTelaNovaVendaAtiva(String item, String module) {
        verificarTelaAtiva(item, module);
    }
    
    /**
     * Abre tela completa elegante
     */
    private void abrirTelaCompletaElegante(String item, String module) {
        try {
            SystemLogger.ui("Abrindo tela completa elegante: " + item);
            
            // Verificar e identificar qual tela está ativa (sistema completo)
            verificarTelaAtiva(item, module);
            
            // Limpar área de trabalho
            workArea.removeAll();
            
            // Criar conteúdo elegante
            JPanel conteudoTela = criarTelaCompletaElegante(item, module);
            workArea.add(conteudoTela, BorderLayout.CENTER);
            
            // Única atualização otimizada
            SwingUtilities.invokeLater(() -> {
                workArea.revalidate();
                workArea.repaint();
            });
            
            // Log de sucesso
            SystemLogger.ui("Tela completa elegante " + item + " aberta na área de trabalho principal para usuário: " + usuarioAtual);
            
        } catch (Exception e) {
            SystemLogger.error("Erro ao abrir tela completa elegante: " + item, e);
            JOptionPane.showMessageDialog(workArea, 
                "❌ Erro ao abrir tela: " + item + "\n\n" + e.getMessage(), 
                "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    /**
     * Cria tela completa elegante
     */
    private JPanel criarTelaCompletaElegante(String item, String module) {
        JPanel painelPrincipal = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                // Gradiente sutil de fundo
                GradientPaint gradient = new GradientPaint(0, 0, new Color(248, 251, 255), 
                                                        0, getHeight(), new Color(240, 244, 248));
                g2d.setPaint(gradient);
                g2d.fillRect(0, 0, getWidth(), getHeight());
                g2d.dispose();
            }
        };
        
        // Header elegante da tela
        JPanel header = criarHeaderTelaElegante(item);
        painelPrincipal.add(header, BorderLayout.NORTH);
        
        // Conteúdo principal com scroll elegante
        JScrollPane scrollPane = new JScrollPane() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                // Borda arredondada sutil
                g2d.setColor(new Color(200, 200, 200, 30));
                g2d.drawRoundRect(0, 0, getWidth()-1, getHeight()-1, 8, 8);
                g2d.dispose();
            }
        };
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.setOpaque(false);
        scrollPane.getViewport().setOpaque(false);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        scrollPane.getVerticalScrollBar().setUI(new ElegantScrollBarUI());
        
        JPanel conteudoPrincipal = new JPanel();
        conteudoPrincipal.setLayout(new BoxLayout(conteudoPrincipal, BoxLayout.Y_AXIS));
        conteudoPrincipal.setOpaque(false);
        conteudoPrincipal.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        // Verificar se há tela ativa
        if (item == null || item.trim().isEmpty()) {
            // Tela inicial elegante quando não houver tela ativa
            JPanel painelInicial = criarTelaInicialElegante();
            conteudoPrincipal.add(painelInicial);
        } else {
            // Painel de busca elegante
            JPanel buscaPanel = criarPainelBuscaElegante(item, module);
            conteudoPrincipal.add(buscaPanel);
            conteudoPrincipal.add(Box.createVerticalStrut(15));
            
            // Painel de formulário elegante
            JPanel formularioPanel = criarPainelFormularioElegante(item, module);
            conteudoPrincipal.add(formularioPanel);
            conteudoPrincipal.add(Box.createVerticalStrut(15));
            
            // Painel de tabela elegante
            JPanel tabelaPanel = criarPainelTabelaElegante(item, module);
            tabelaPanel.setVisible(true);
            conteudoPrincipal.add(tabelaPanel);
            conteudoPrincipal.add(Box.createVerticalStrut(15));
            
            // Painel de botões elegante
            JPanel botoesPanel = criarPainelBotoesElegante(item);
            conteudoPrincipal.add(botoesPanel);
        }
        
        scrollPane.setViewportView(conteudoPrincipal);
        painelPrincipal.add(scrollPane, BorderLayout.CENTER);
        
        // Forçar revalidação e repaint
        conteudoPrincipal.revalidate();
        conteudoPrincipal.repaint();
        scrollPane.revalidate();
        scrollPane.repaint();
        
        // Status bar elegante
        JPanel statusBar = criarStatusBarTelaElegante(item);
        painelPrincipal.add(statusBar, BorderLayout.SOUTH);
        
        return painelPrincipal;
    }
    
    /**
     * Cria header elegante da tela com cor do módulo
     */
    private JPanel criarHeaderTelaElegante(String titulo) {
        // Determinar a cor do módulo baseado no título
        Color moduleColor = getModuleColor(titulo);
        Color moduleColorDark = moduleColor.darker();
        
        JPanel header = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                // Gradiente elegante com cor do módulo
                Color primaryColor = false ? 
                    new Color(75, 85, 99) : new Color(41, 128, 185);
                Color secondaryColor = false ? 
                    new Color(45, 45, 45) : new Color(52, 73, 94);
                GradientPaint gradient = new GradientPaint(0, 0, primaryColor, 
                                                        0, getHeight(), secondaryColor);
                g2d.setPaint(gradient);
                g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 15, 15);
                g2d.dispose();
            }
        };
        header.setBorder(BorderFactory.createEmptyBorder(20, 25, 20, 25));
        header.setPreferredSize(new Dimension(0, 80));
        header.setOpaque(false);
        
        // Breadcrumb elegante
        JPanel breadcrumbPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        breadcrumbPanel.setOpaque(false);
        
        ElegantButton homeButton = new ElegantButton("🏠 Início", new Color(255, 255, 255, 100), false);
        homeButton.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        homeButton.setForeground(WHITE);
        homeButton.setBorderPainted(false);
        homeButton.setContentAreaFilled(false);
        homeButton.addActionListener(e -> voltarTelaInicial());
        
        JLabel separatorLabel = new JLabel(">");
        separatorLabel.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        separatorLabel.setForeground(new Color(255, 255, 255, 150));
        
        JLabel titleLabel = new JLabel(titulo);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 20));
        titleLabel.setForeground(WHITE);
        
        breadcrumbPanel.add(homeButton);
        breadcrumbPanel.add(Box.createHorizontalStrut(8));
        breadcrumbPanel.add(separatorLabel);
        breadcrumbPanel.add(Box.createHorizontalStrut(8));
        breadcrumbPanel.add(titleLabel);
        
        // Informações do usuário elegante
        JPanel userPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        userPanel.setOpaque(false);
        
        JPanel userCard = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                // Fundo semi-transparente com cor do módulo
                g2d.setColor(new Color(moduleColor.getRed(), moduleColor.getGreen(), moduleColor.getBlue(), 30));
                g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 8, 8);
                g2d.dispose();
            }
        };
        userCard.setBorder(BorderFactory.createEmptyBorder(8, 15, 8, 15));
        userCard.setOpaque(false);
        
        JLabel userIcon = new JLabel("👤");
        userIcon.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        userIcon.setForeground(WHITE);
        
        JLabel userInfo = new JLabel(nomeUsuario + " (" + usuarioAtual + ")");
        userInfo.setFont(new Font("Segoe UI", Font.BOLD, 13));
        userInfo.setForeground(WHITE);
        
        userCard.add(userIcon, BorderLayout.WEST);
        userCard.add(userInfo, BorderLayout.CENTER);
        
        userPanel.add(userCard);
        
        header.add(breadcrumbPanel, BorderLayout.WEST);
        header.add(userPanel, BorderLayout.EAST);
        
        return header;
    }
    
    /**
     * Obtém a cor do módulo baseado no título do submenu
     */
    private Color getModuleColor(String titulo) {
        // Módulo PDV
        if (titulo.contains("Ponto de Venda") || titulo.contains("Pagamentos") || 
            titulo.contains("Cupom Fiscal") || titulo.contains("Terminal Venda") || 
            titulo.contains("Venda Rápida")) {
            return PRIMARY_COLOR; // Azul
        }
        
        // Módulo Vendas
        if (titulo.contains("Nova Venda") || titulo.contains("Consultar Vendas") || 
            titulo.contains("Devoluções") || titulo.contains("Resumo Diário") || 
            titulo.contains("Relatório de Vendas") || titulo.contains("Orçamentos") || 
            titulo.contains("Entregas")) {
            return SUCCESS_COLOR; // Verde
        }
        
        // Módulo Produtos
        if (titulo.contains("Cadastrar Produto") || titulo.contains("Editar Produto") || 
            titulo.contains("Consultar Produto") || titulo.contains("Categorias") || 
            titulo.contains("Fornecedores") || titulo.contains("Estoque Mínimo") || 
            titulo.contains("Códigos de Barras")) {
            return WARNING_COLOR; // Amarelo
        }
        
        // Módulo Clientes
        if (titulo.contains("Novo Cliente") || titulo.contains("Editar Cliente") || 
            titulo.contains("Consultar Cliente") || titulo.contains("Histórico de Compras") || 
            titulo.contains("Carteira Fidelidade") || titulo.contains("Endereços") || 
            titulo.contains("Contatos")) {
            return new Color(155, 89, 182); // Roxo
        }
        
        // Módulo Estoque
        if (titulo.contains("Consultar Estoque") || titulo.contains("Entrada de Mercadoria") || 
            titulo.contains("Saída de Mercadoria") || titulo.contains("Transferência") || 
            titulo.contains("Inventário") || titulo.contains("Estoque Baixo") || 
            titulo.contains("Movimentações")) {
            return new Color(26, 188, 156); // Verde água
        }
        
        // Módulo Financeiro
        if (titulo.contains("Contas a Receber") || titulo.contains("Contas a Pagar") || 
            titulo.contains("Fluxo de Caixa") || titulo.contains("Fechamento de Caixa") || 
            titulo.contains("Conciliação Bancária") || titulo.contains("Cobranças")) {
            return new Color(230, 126, 34); // Laranja
        }
        
        // Módulo Relatórios
        if (titulo.contains("Relatório de Vendas") || titulo.contains("Relatório de Produtos") || 
            titulo.contains("Relatório de Clientes") || titulo.contains("Relatório Financeiro") || 
            titulo.contains("Dashboard") || titulo.contains("Relatório de Estoque") || 
            titulo.contains("Análise de Vendas")) {
            return new Color(52, 152, 219); // Azul claro
        }
        
        // Módulo Configurações
        if (titulo.contains("Usuários e Permissões") || titulo.contains("Empresa") || 
            titulo.contains("Sistema") || titulo.contains("Impressoras") || 
            titulo.contains("Formas de Pagamento") || titulo.contains("Integrações") || 
            titulo.contains("Parâmetros") || titulo.contains("Backup")) {
            return DARK_GRAY; // Cinza escuro
        }
        
        // Cor padrão para outros casos
        return PRIMARY_COLOR;
    }
    
    /**
     * Cria painel de busca elegante
     */
    private JPanel criarPainelBuscaElegante(String item, String module) {
        JPanel buscaPanel = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                // Fundo elegante
                g2d.setColor(WHITE);
                g2d.fillRoundRect(2, 2, getWidth()-4, getHeight()-4, 12, 12);
                
                // Borda elegante
                g2d.setColor(MEDIUM_GRAY);
                g2d.drawRoundRect(2, 2, getWidth()-4, getHeight()-4, 12, 12);
                
                // Sombra sutil
                g2d.setColor(new Color(0, 0, 0, 15));
                g2d.fillRoundRect(4, 4, getWidth()-4, getHeight()-4, 12, 12);
                g2d.dispose();
            }
        };
        buscaPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        buscaPanel.setOpaque(false);
        
        // Título elegante
        JLabel buscaLabel = new JLabel("🔍 Busca e Filtros") {
            @Override
            public void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2d.setFont(new Font("Segoe UI", Font.BOLD, 18));
                g2d.setColor(PRIMARY_COLOR);
                g2d.drawString(getText(), 5, 25);
                g2d.dispose();
            }
        };
        
        // Campos de busca específicos elegantes
        JPanel camposPanel = criarCamposBuscaElegante(item, module);
        
        buscaPanel.add(buscaLabel, BorderLayout.NORTH);
        buscaPanel.add(camposPanel, BorderLayout.CENTER);
        
        return buscaPanel;
    }
    
    /**
     * Cria campos de busca elegantes
     */
    private JPanel criarCamposBuscaElegante(String item, String module) {
        JPanel camposPanel = new JPanel(new GridBagLayout());
        camposPanel.setOpaque(false);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.anchor = GridBagConstraints.WEST;
        
        // Implementação similar à versão completa, mas com componentes elegantes
        // Por simplicidade, vamos criar uma versão básica elegante
        
        // Campo de busca geral elegante
        ElegantTextField txtBusca = new ElegantTextField(30);
        txtBusca.setPlaceholder("Digite sua busca aqui...");
        
        ElegantButton btnBuscar = new ElegantButton("🔍 Buscar", PRIMARY_COLOR, false);
        btnBuscar.setForeground(WHITE);
        
        ElegantButton btnLimpar = new ElegantButton("🔄 Limpar", MEDIUM_GRAY, false);
        btnLimpar.setForeground(WHITE);
        
        gbc.gridx = 0; gbc.gridy = 0;
        camposPanel.add(new JLabel("Buscar:"), gbc);
        gbc.gridx = 1; gbc.gridy = 0;
        camposPanel.add(txtBusca, gbc);
        gbc.gridx = 2; gbc.gridy = 0;
        camposPanel.add(btnBuscar, gbc);
        gbc.gridx = 3; gbc.gridy = 0;
        camposPanel.add(btnLimpar, gbc);
        
        return camposPanel;
    }
    
    /**
     * Cria painel de formulário elegante
     */
    private JPanel criarPainelFormularioElegante(String item, String module) {
        JPanel formularioPanel = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                // Fundo elegante
                g2d.setColor(WHITE);
                g2d.fillRoundRect(2, 2, getWidth()-4, getHeight()-4, 12, 12);
                
                // Borda elegante
                g2d.setColor(MEDIUM_GRAY);
                g2d.drawRoundRect(2, 2, getWidth()-4, getHeight()-4, 12, 12);
                
                // Sombra sutil
                g2d.setColor(new Color(0, 0, 0, 15));
                g2d.fillRoundRect(4, 4, getWidth()-4, getHeight()-4, 12, 12);
                g2d.dispose();
            }
        };
        formularioPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        formularioPanel.setOpaque(false);
        
        // Título elegante
        JLabel formularioLabel = new JLabel("📝 Formulário de Cadastro") {
            @Override
            public void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2d.setFont(new Font("Segoe UI", Font.BOLD, 18));
                g2d.setColor(SUCCESS_COLOR);
                g2d.drawString(getText(), 5, 25);
                g2d.dispose();
            }
        };
        
        // Formulário específico elegante - Campos detalhados por módulo
        JPanel camposPanel = criarFormularioPorItemElegante(item, module);
        
        // Painel de botões de ação elegante
        JPanel botoesPanel = criarPainelBotoesAcaoElegante(item, module);
        
        // Adicionar painel de botões ao formulário
        JPanel formularioCompleto = new JPanel(new BorderLayout());
        formularioCompleto.setOpaque(false);
        formularioCompleto.add(formularioLabel, BorderLayout.NORTH);
        formularioCompleto.add(camposPanel, BorderLayout.CENTER);
        formularioCompleto.add(botoesPanel, BorderLayout.SOUTH);
        
        // Adicionar formulário completo ao painel principal
        formularioPanel.add(formularioCompleto, BorderLayout.CENTER);
        
        return formularioPanel;
    }
    
    /**
     * Cria o formulário específico para cada item
     */
    private JPanel criarFormularioPorItemElegante(String item, String module) {
        JPanel camposPanel = new JPanel(new GridBagLayout());
        camposPanel.setOpaque(false);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.anchor = GridBagConstraints.WEST;
        
        switch (module) {
            case "PDV":
                return criarFormularioPDVElegante(item, gbc);
            case "VENDAS":
                return criarFormularioVendasElegante(item, gbc);
            case "PRODUTOS":
                return criarFormularioProdutosElegante(item, gbc);
            case "CLIENTES":
                return criarFormularioClientesElegante(item, gbc);
            case "ESTOQUE":
                return criarFormularioEstoqueElegante(item, gbc);
            case "FINANCEIRO":
                return criarFormularioFinanceiroElegante(item, gbc);
            case "RELATORIOS":
                return criarFormularioRelatoriosElegante(item, gbc);
            case "CONFIGURACOES":
                return criarFormularioConfiguracoesElegante(item, gbc);
            default:
                return criarFormularioPadraoElegante(item, module, gbc);
        }
    }
    
    /**
     * Formulários PDV elegantes
     */
    private JPanel criarFormularioPDVElegante(String item, GridBagConstraints gbc) {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setOpaque(false);
        
        // Logs para identificação do formulário PDV
        SystemLogger.ui("=== CRIANDO FORMULÁRIO PDV ELEGANTE ===");
        SystemLogger.ui("🔧 FORMULÁRIO PDV IDENTIFICADO:");
        SystemLogger.ui("   • Classe: " + this.getClass().getSimpleName());
        SystemLogger.ui("   • Método: criarFormularioPDVElegante()");
        SystemLogger.ui("   • Arquivo: PDVMenuLateralElegante.java");
        SystemLogger.ui("   • Item: " + item);
        SystemLogger.ui("   • Usuário: " + usuarioAtual);
        SystemLogger.ui("   • Timestamp: " + new java.util.Date());
        SystemLogger.ui("   • Thread: " + Thread.currentThread().getName());
        SystemLogger.ui("✅ FORMULÁRIO PDV SENDO CRIADO");
        SystemLogger.info("Formulário PDV - Item: " + item + " | Usuário: " + usuarioAtual);
        
        switch (item) {
            case "🛒 Ponto de Venda":
                // Log específico para Ponto de Venda
                SystemLogger.ui("🏷️ CRIANDO FORMULÁRIO PONTO DE VENDA");
                SystemLogger.info("PDV - Inicializando campos para Ponto de Venda");
                
                // Código do produto
                SystemLogger.ui("📝 PDV - Criando campo: Código do Produto");
                gbc.gridx = 0; gbc.gridy = 0; gbc.anchor = GridBagConstraints.WEST;
                gbc.insets = new Insets(5, 5, 5, 10);
                JLabel lblCodigo = new JLabel("Código do Produto:");
                lblCodigo.setFont(new Font("Segoe UI", Font.BOLD, 12));
                lblCodigo.setForeground(DARK_GRAY);
                panel.add(lblCodigo, gbc);
                gbc.gridx = 1; gbc.gridy = 0; gbc.anchor = GridBagConstraints.WEST;
                gbc.insets = new Insets(5, 0, 5, 5);
                gbc.fill = GridBagConstraints.HORIZONTAL;
                ElegantTextField txtCodigo = new ElegantTextField(15);
                panel.add(txtCodigo, gbc);
                SystemLogger.info("PDV - Campo 'Código do Produto' criado");
                
                // Descrição
                SystemLogger.ui("📝 PDV - Criando campo: Descrição");
                gbc.gridx = 0; gbc.gridy = 1; gbc.anchor = GridBagConstraints.WEST;
                gbc.insets = new Insets(5, 5, 5, 10);
                gbc.fill = GridBagConstraints.NONE;
                JLabel lblDescricao = new JLabel("Descrição:");
                lblDescricao.setFont(new Font("Segoe UI", Font.BOLD, 12));
                lblDescricao.setForeground(DARK_GRAY);
                panel.add(lblDescricao, gbc);
                gbc.gridx = 1; gbc.gridy = 1; gbc.anchor = GridBagConstraints.WEST;
                gbc.insets = new Insets(5, 0, 5, 5);
                gbc.fill = GridBagConstraints.HORIZONTAL;
                ElegantTextField txtDescricao = new ElegantTextField(30);
                txtDescricao.setEditable(false);
                panel.add(txtDescricao, gbc);
                SystemLogger.info("PDV - Campo 'Descrição' criado (readonly)");
                
                // Quantidade
                SystemLogger.ui("📝 PDV - Criando campo: Quantidade");
                gbc.gridx = 0; gbc.gridy = 2; gbc.anchor = GridBagConstraints.WEST;
                gbc.insets = new Insets(5, 5, 5, 10);
                gbc.fill = GridBagConstraints.NONE;
                JLabel lblQuantidade = new JLabel("Quantidade:");
                lblQuantidade.setFont(new Font("Segoe UI", Font.BOLD, 12));
                lblQuantidade.setForeground(DARK_GRAY);
                panel.add(lblQuantidade, gbc);
                gbc.gridx = 1; gbc.gridy = 2; gbc.anchor = GridBagConstraints.WEST;
                gbc.insets = new Insets(5, 0, 5, 5);
                gbc.fill = GridBagConstraints.HORIZONTAL;
                ElegantTextField txtQuantidade = new ElegantTextField(10);
                panel.add(txtQuantidade, gbc);
                SystemLogger.info("PDV - Campo 'Quantidade' criado");
                
                // Valor Unitário
                SystemLogger.ui("📝 PDV - Criando campo: Valor Unitário");
                gbc.gridx = 0; gbc.gridy = 3; gbc.anchor = GridBagConstraints.WEST;
                gbc.insets = new Insets(5, 5, 5, 10);
                gbc.fill = GridBagConstraints.NONE;
                JLabel lblValorUnitario = new JLabel("Valor Unitário:");
                lblValorUnitario.setFont(new Font("Segoe UI", Font.BOLD, 12));
                lblValorUnitario.setForeground(DARK_GRAY);
                panel.add(lblValorUnitario, gbc);
                gbc.gridx = 1; gbc.gridy = 3; gbc.anchor = GridBagConstraints.WEST;
                gbc.insets = new Insets(5, 0, 5, 5);
                gbc.fill = GridBagConstraints.HORIZONTAL;
                ElegantTextField txtValorUnitario = new ElegantTextField(15);
                txtValorUnitario.setEditable(false);
                panel.add(txtValorUnitario, gbc);
                SystemLogger.info("PDV - Campo 'Valor Unitário' criado (readonly)");
                
                // Valor Total
                SystemLogger.ui("📝 PDV - Criando campo: Valor Total");
                gbc.gridx = 0; gbc.gridy = 4; gbc.anchor = GridBagConstraints.WEST;
                gbc.insets = new Insets(5, 5, 5, 10);
                gbc.fill = GridBagConstraints.NONE;
                JLabel lblValorTotal = new JLabel("Valor Total:");
                lblValorTotal.setFont(new Font("Segoe UI", Font.BOLD, 12));
                lblValorTotal.setForeground(DARK_GRAY);
                panel.add(lblValorTotal, gbc);
                gbc.gridx = 1; gbc.gridy = 4; gbc.anchor = GridBagConstraints.WEST;
                gbc.insets = new Insets(5, 0, 5, 5);
                gbc.fill = GridBagConstraints.HORIZONTAL;
                ElegantTextField txtValorTotal = new ElegantTextField(15);
                txtValorTotal.setEditable(false);
                panel.add(txtValorTotal, gbc);
                SystemLogger.info("PDV - Campo 'Valor Total' criado (readonly)");
                
                // Forma de Pagamento
                SystemLogger.ui("📝 PDV - Criando campo: Forma Pagamento");
                gbc.gridx = 0; gbc.gridy = 5; gbc.anchor = GridBagConstraints.WEST;
                gbc.insets = new Insets(5, 5, 5, 10);
                gbc.fill = GridBagConstraints.NONE;
                JLabel lblFormaPagamento = new JLabel("Forma Pagamento:");
                lblFormaPagamento.setFont(new Font("Segoe UI", Font.BOLD, 12));
                lblFormaPagamento.setForeground(DARK_GRAY);
                panel.add(lblFormaPagamento, gbc);
                gbc.gridx = 1; gbc.gridy = 5; gbc.anchor = GridBagConstraints.WEST;
                gbc.insets = new Insets(5, 0, 5, 5);
                gbc.fill = GridBagConstraints.HORIZONTAL;
                JComboBox<String> cbFormaPagamento = new JComboBox<>(new String[]{"Dinheiro", "Cartão Débito", "Cartão Crédito", "PIX"});
                cbFormaPagamento.setFont(new Font("Segoe UI", Font.PLAIN, 12));
                panel.add(cbFormaPagamento, gbc);
                SystemLogger.info("PDV - Campo 'Forma Pagamento' criado (ComboBox)");
                break;
                
            case "💳 Pagamentos":
                // Log específico para Pagamentos
                SystemLogger.ui("💳 CRIANDO FORMULÁRIO PAGAMENTOS");
                SystemLogger.info("PDV - Inicializando campos para Pagamentos");
                
                // Cliente
                SystemLogger.ui("📝 PDV - Criando campo: Cliente (Pagamentos)");
                gbc.gridx = 0; gbc.gridy = 0; gbc.anchor = GridBagConstraints.WEST;
                gbc.insets = new Insets(5, 5, 5, 10);
                JLabel lblCliente = new JLabel("Cliente:");
                lblCliente.setFont(new Font("Segoe UI", Font.BOLD, 12));
                lblCliente.setForeground(DARK_GRAY);
                panel.add(lblCliente, gbc);
                gbc.gridx = 1; gbc.gridy = 0; gbc.anchor = GridBagConstraints.WEST;
                gbc.insets = new Insets(5, 0, 5, 5);
                gbc.fill = GridBagConstraints.HORIZONTAL;
                ElegantTextField txtCliente = new ElegantTextField(30);
                panel.add(txtCliente, gbc);
                SystemLogger.info("PDV - Campo 'Cliente' criado (Pagamentos)");
                
                // Valor do Pagamento
                gbc.gridx = 0; gbc.gridy = 1; gbc.anchor = GridBagConstraints.WEST;
                gbc.insets = new Insets(5, 5, 5, 10);
                gbc.fill = GridBagConstraints.NONE;
                JLabel lblValor = new JLabel("Valor:");
                lblValor.setFont(new Font("Segoe UI", Font.BOLD, 12));
                lblValor.setForeground(DARK_GRAY);
                panel.add(lblValor, gbc);
                gbc.gridx = 1; gbc.gridy = 1; gbc.anchor = GridBagConstraints.WEST;
                gbc.insets = new Insets(5, 0, 5, 5);
                gbc.fill = GridBagConstraints.HORIZONTAL;
                ElegantTextField txtValor = new ElegantTextField(15);
                panel.add(txtValor, gbc);
                
                // Forma de Pagamento
                gbc.gridx = 0; gbc.gridy = 2; gbc.anchor = GridBagConstraints.WEST;
                gbc.insets = new Insets(5, 5, 5, 10);
                gbc.fill = GridBagConstraints.NONE;
                JLabel lblPagamento = new JLabel("Forma Pagamento:");
                lblPagamento.setFont(new Font("Segoe UI", Font.BOLD, 12));
                lblPagamento.setForeground(DARK_GRAY);
                panel.add(lblPagamento, gbc);
                gbc.gridx = 1; gbc.gridy = 2; gbc.anchor = GridBagConstraints.WEST;
                gbc.insets = new Insets(5, 0, 5, 5);
                gbc.fill = GridBagConstraints.HORIZONTAL;
                JComboBox<String> cbPagamento = new JComboBox<>(new String[]{"Dinheiro", "Cartão Débito", "Cartão Crédito", "PIX", "Boleto"});
                cbPagamento.setFont(new Font("Segoe UI", Font.PLAIN, 12));
                panel.add(cbPagamento, gbc);
                
                // Data do Pagamento
                gbc.gridx = 0; gbc.gridy = 3; gbc.anchor = GridBagConstraints.WEST;
                gbc.insets = new Insets(5, 5, 5, 10);
                gbc.fill = GridBagConstraints.NONE;
                JLabel lblData = new JLabel("Data:");
                lblData.setFont(new Font("Segoe UI", Font.BOLD, 12));
                lblData.setForeground(DARK_GRAY);
                panel.add(lblData, gbc);
                gbc.gridx = 1; gbc.gridy = 3; gbc.anchor = GridBagConstraints.WEST;
                gbc.insets = new Insets(5, 0, 5, 5);
                gbc.fill = GridBagConstraints.HORIZONTAL;
                ElegantTextField txtData = new ElegantTextField(10);
                panel.add(txtData, gbc);
                break;
                
            default:
                // Log para formulário padrão PDV
                SystemLogger.ui("📋 FORMULÁRIO PADRÃO PDV - Item não reconhecido: " + item);
                SystemLogger.info("PDV - Criando formulário padrão para: " + item);
                return criarFormularioPadraoElegante(item, "PDV", gbc);
        }
        
        // Log de conclusão do formulário PDV
        SystemLogger.ui("✅ FORMULÁRIO PDV CONCLUÍDO - " + item);
        SystemLogger.info("PDV - Formulário criado com sucesso: " + item);
        
        return panel;
    }
    
    /**
     * Formulários Vendas elegantes
     */
    private JPanel criarFormularioVendasElegante(String item, GridBagConstraints gbc) {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setOpaque(false);
        
        // Logs gerais para identificação do formulário Vendas
        SystemLogger.ui("=== CRIANDO FORMULÁRIO VENDAS ELEGANTE ===");
        SystemLogger.ui("💼 FORMULÁRIO VENDAS IDENTIFICADO:");
        SystemLogger.ui("   • Classe: " + this.getClass().getSimpleName());
        SystemLogger.ui("   • Método: criarFormularioVendasElegante()");
        SystemLogger.ui("   • Arquivo: PDVMenuLateralElegante.java");
        SystemLogger.ui("   • Item: " + item);
        SystemLogger.ui("   • Usuário: " + usuarioAtual);
        SystemLogger.ui("   • Timestamp: " + new java.util.Date());
        SystemLogger.ui("   • Thread: " + Thread.currentThread().getName());
        SystemLogger.ui("✅ FORMULÁRIO VENDAS SENDO CRIADO");
        SystemLogger.info("Formulário Vendas - Item: " + item + " | Usuário: " + usuarioAtual);
        
        // Log específico para Nova Venda com identificação completa
        if (item.equals("📋 Nova Venda")) {
            SystemLogger.ui("=== FORMULÁRIO NOVA VENDA IDENTIFICADO ===");
            SystemLogger.ui("Classe do Formulário: " + this.getClass().getSimpleName());
            SystemLogger.ui("Método: criarFormularioVendasElegante()");
            SystemLogger.ui("Arquivo Fonte: PDVMenuLateralElegante.java");
            SystemLogger.ui("Item: " + item);
            SystemLogger.ui("Thread: " + Thread.currentThread().getName());
            SystemLogger.ui("Formulário Nova Venda sendo criado por classe única e exclusiva");
            SystemLogger.info("Formulário Nova Venda - Classe: " + this.getClass().getSimpleName());
        }
        
        switch (item) {
            case "📋 Nova Venda":
                SystemLogger.ui("📋 VENDAS - Criando formulário Nova Venda");
                
                // Cliente
                SystemLogger.ui("📝 VENDAS - Criando campo: Cliente");
                gbc.gridx = 0; gbc.gridy = 0;
                JLabel lblCliente = new JLabel("Cliente:");
                lblCliente.setFont(new Font("Segoe UI", Font.BOLD, 12));
                lblCliente.setForeground(DARK_GRAY);
                panel.add(lblCliente, gbc);
                gbc.gridx = 1; gbc.gridy = 0;
                ElegantTextField txtCliente = new ElegantTextField(30);
                panel.add(txtCliente, gbc);
                SystemLogger.info("VENDAS - Campo 'Cliente' criado");
                
                // Data da Venda
                SystemLogger.ui("📝 VENDAS - Criando campo: Data");
                gbc.gridx = 0; gbc.gridy = 1;
                JLabel lblData = new JLabel("Data:");
                lblData.setFont(new Font("Segoe UI", Font.BOLD, 12));
                lblData.setForeground(DARK_GRAY);
                panel.add(lblData, gbc);
                gbc.gridx = 1; gbc.gridy = 1;
                ElegantTextField txtData = new ElegantTextField(10);
                panel.add(txtData, gbc);
                SystemLogger.info("VENDAS - Campo 'Data' criado");
                
                // Vendedor
                SystemLogger.ui("📝 VENDAS - Criando campo: Vendedor");
                gbc.gridx = 0; gbc.gridy = 2;
                JLabel lblVendedor = new JLabel("Vendedor:");
                lblVendedor.setFont(new Font("Segoe UI", Font.BOLD, 12));
                lblVendedor.setForeground(DARK_GRAY);
                panel.add(lblVendedor, gbc);
                gbc.gridx = 1; gbc.gridy = 2;
                ElegantTextField txtVendedor = new ElegantTextField(20);
                txtVendedor.setEditable(false);
                panel.add(txtVendedor, gbc);
                SystemLogger.info("VENDAS - Campo 'Vendedor' criado (readonly)");
                
                // Condição de Pagamento
                SystemLogger.ui("📝 VENDAS - Criando campo: Condição Pagamento");
                gbc.gridx = 0; gbc.gridy = 3;
                JLabel lblCondicao = new JLabel("Condição Pagamento:");
                lblCondicao.setFont(new Font("Segoe UI", Font.BOLD, 12));
                lblCondicao.setForeground(DARK_GRAY);
                panel.add(lblCondicao, gbc);
                gbc.gridx = 1; gbc.gridy = 3;
                JComboBox<String> cbCondicao = new JComboBox<>(new String[]{"À Vista", "30 Dias", "60 Dias", "90 Dias"});
                cbCondicao.setFont(new Font("Segoe UI", Font.PLAIN, 12));
                panel.add(cbCondicao, gbc);
                SystemLogger.info("VENDAS - Campo 'Condição Pagamento' criado (ComboBox)");
                
                // Painel de Busca de Produtos
                gbc.gridx = 0; gbc.gridy = 4; gbc.gridwidth = 2;
                gbc.fill = GridBagConstraints.HORIZONTAL;
                gbc.insets = new Insets(10, 5, 10, 5);
                gbc.weighty = 0.0;
                
                // Log de criação do painel de busca
                SystemLogger.ui("CRIANDO PAINEL DE BUSCA - Nova Venda");
                SystemLogger.info("Painel Busca Nova Venda - Inicializando componentes de busca");
                
                JPanel buscaPanelProdutosVenda = new JPanel(new GridBagLayout());
                buscaPanelProdutosVenda.setBorder(BorderFactory.createTitledBorder(
                    BorderFactory.createEtchedBorder(), "🔍 Buscar Produtos",
                    TitledBorder.LEFT, TitledBorder.TOP, new Font("Segoe UI", Font.BOLD, 12),
                    DARK_GRAY));
                
                GridBagConstraints gbcBuscaProdutos = new GridBagConstraints();
                gbcBuscaProdutos.insets = new Insets(5, 5, 5, 5);
                gbcBuscaProdutos.anchor = GridBagConstraints.WEST;
                
                // Campo de busca de produtos
                gbcBuscaProdutos.gridx = 0; gbcBuscaProdutos.gridy = 0;
                gbcBuscaProdutos.fill = GridBagConstraints.NONE;
                gbcBuscaProdutos.weightx = 0.0;
                JLabel lblBuscaProdutos = new JLabel("Buscar:");
                lblBuscaProdutos.setFont(new Font("Segoe UI", Font.BOLD, 12));
                lblBuscaProdutos.setForeground(DARK_GRAY);
                buscaPanelProdutosVenda.add(lblBuscaProdutos, gbcBuscaProdutos);
                
                gbcBuscaProdutos.gridx = 1; gbcBuscaProdutos.gridy = 0;
                gbcBuscaProdutos.fill = GridBagConstraints.HORIZONTAL;
                gbcBuscaProdutos.weightx = 1.0;
                
                // Log de criação do campo de busca
                SystemLogger.ui("CRIANDO CAMPO DE BUSCA - Nova Venda");
                SystemLogger.info("Campo Busca Nova Venda - Configurando ElegantTextField");
                
                ElegantTextField txtBuscaProdutos = new ElegantTextField(30);
                txtBuscaProdutos.setToolTipText("Digite código, nome ou descrição do produto");
                buscaPanelProdutosVenda.add(txtBuscaProdutos, gbcBuscaProdutos);
                
                // Botões de busca de produtos
                gbcBuscaProdutos.gridx = 2; gbcBuscaProdutos.gridy = 0;
                gbcBuscaProdutos.fill = GridBagConstraints.NONE;
                gbcBuscaProdutos.weightx = 0.0;
                gbcBuscaProdutos.insets = new Insets(5, 10, 5, 5);
                
                // Log de criação do botão buscar
                SystemLogger.ui("CRIANDO BOTÃO BUSCAR - Nova Venda");
                SystemLogger.info("Botão Buscar Nova Venda - Configurando ElegantButton com PRIMARY_COLOR");
                
                ElegantButton btnBuscarProdutos = new ElegantButton("🔍 Buscar", PRIMARY_COLOR, false);
                btnBuscarProdutos.setForeground(WHITE);
                btnBuscarProdutos.setPreferredSize(new Dimension(100, 30));
                buscaPanelProdutosVenda.add(btnBuscarProdutos, gbcBuscaProdutos);
                
                gbcBuscaProdutos.gridx = 3; gbcBuscaProdutos.gridy = 0;
                gbcBuscaProdutos.insets = new Insets(5, 5, 5, 5);
                
                // Log de criação do botão limpar
                SystemLogger.ui("CRIANDO BOTÃO LIMPAR - Nova Venda");
                SystemLogger.info("Botão Limpar Nova Venda - Configurando ElegantButton com MEDIUM_GRAY");
                
                ElegantButton btnLimparBuscaProdutos = new ElegantButton("🔄 Limpar", MEDIUM_GRAY, false);
                btnLimparBuscaProdutos.setForeground(WHITE);
                btnLimparBuscaProdutos.setPreferredSize(new Dimension(100, 30));
                buscaPanelProdutosVenda.add(btnLimparBuscaProdutos, gbcBuscaProdutos);
                
                // Log de adição do painel de busca ao formulário
                SystemLogger.ui("ADICIONANDO PAINEL BUSCA AO FORMULÁRIO - Nova Venda");
                SystemLogger.info("Painel Busca Nova Venda - Adicionado ao formulário principal");
                
                panel.add(buscaPanelProdutosVenda, gbc);
                
                // Resetar gridwidth para campos abaixo
                gbc.gridwidth = 1;
                gbc.insets = new Insets(5, 5, 5, 5);
                
                // Tabela de Produtos Encontrados
                gbc.gridx = 0; gbc.gridy = 5; gbc.gridwidth = 2;
                gbc.fill = GridBagConstraints.HORIZONTAL;
                gbc.weightx = 1.0; gbc.weighty = 0.1;
                
                String[] colunasTabelaProdutosVenda = {"Código", "Descrição", "Preço", "Estoque", "Categoria"};
                Object[][] dadosTabelaProdutosVenda = {};
                
                JTable tabelaProdutosVenda = new JTable(dadosTabelaProdutosVenda, colunasTabelaProdutosVenda);
                tabelaProdutosVenda.setFont(new Font("Segoe UI", Font.PLAIN, 11));
                tabelaProdutosVenda.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 12));
                tabelaProdutosVenda.getTableHeader().setBackground(new Color(70, 130, 180));
                tabelaProdutosVenda.getTableHeader().setForeground(WHITE);
                tabelaProdutosVenda.setRowHeight(25);
                tabelaProdutosVenda.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
                tabelaProdutosVenda.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
                
                JScrollPane scrollPaneTabelaProdutosVenda = new JScrollPane(tabelaProdutosVenda);
                scrollPaneTabelaProdutosVenda.setPreferredSize(new Dimension(700, 200));
                scrollPaneTabelaProdutosVenda.setBorder(BorderFactory.createTitledBorder(
                    BorderFactory.createEtchedBorder(), "Produtos Encontrados",
                    TitledBorder.LEFT, TitledBorder.TOP, new Font("Segoe UI", Font.BOLD, 12),
                    DARK_GRAY));
                panel.add(scrollPaneTabelaProdutosVenda, gbc);
                break;
                
            case "↩️ Devoluções":
                SystemLogger.ui("🔄 VENDAS - Criando formulário Devoluções");
                
                // Painel de Busca
                gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
                gbc.fill = GridBagConstraints.HORIZONTAL;
                gbc.insets = new Insets(5, 5, 15, 5);
                
                JPanel buscaPanel = new JPanel(new GridBagLayout());
                buscaPanel.setBorder(BorderFactory.createTitledBorder(
                    BorderFactory.createEtchedBorder(), "🔍 Buscar Devoluções",
                    TitledBorder.LEFT, TitledBorder.TOP, new Font("Segoe UI", Font.BOLD, 12),
                    DARK_GRAY));
                
                GridBagConstraints gbcBusca = new GridBagConstraints();
                gbcBusca.insets = new Insets(5, 5, 5, 5);
                
                // Campo de busca
                gbcBusca.gridx = 0; gbcBusca.gridy = 0;
                gbcBusca.fill = GridBagConstraints.HORIZONTAL;
                gbcBusca.weightx = 1.0;
                JLabel lblBusca = new JLabel("Buscar:");
                lblBusca.setFont(new Font("Segoe UI", Font.BOLD, 12));
                lblBusca.setForeground(DARK_GRAY);
                buscaPanel.add(lblBusca, gbcBusca);
                
                gbcBusca.gridx = 1; gbcBusca.gridy = 0;
                gbcBusca.fill = GridBagConstraints.HORIZONTAL;
                gbcBusca.weightx = 2.0;
                ElegantTextField txtBuscaDevolucao = new ElegantTextField(30);
                txtBuscaDevolucao.setToolTipText("Digite número da venda, cliente ou motivo");
                buscaPanel.add(txtBuscaDevolucao, gbcBusca);
                
                // Botões de busca
                gbcBusca.gridx = 2; gbcBusca.gridy = 0;
                gbcBusca.fill = GridBagConstraints.NONE;
                gbcBusca.weightx = 0;
                ElegantButton btnBuscarDevolucao = new ElegantButton("🔍 Buscar", PRIMARY_COLOR, false);
                btnBuscarDevolucao.setForeground(WHITE);
                buscaPanel.add(btnBuscarDevolucao, gbcBusca);
                
                gbcBusca.gridx = 3; gbcBusca.gridy = 0;
                ElegantButton btnLimparBuscaDevolucao = new ElegantButton("🔄 Limpar", MEDIUM_GRAY, false);
                btnLimparBuscaDevolucao.setForeground(WHITE);
                buscaPanel.add(btnLimparBuscaDevolucao, gbcBusca);
                
                panel.add(buscaPanel, gbc);
                
                // Resetar gridwidth para campos abaixo
                gbc.gridwidth = 1;
                gbc.insets = new Insets(5, 5, 5, 5);
                
                // Número da Venda Original
                SystemLogger.ui("📝 VENDAS - Criando campo: Nº Venda Original");
                gbc.gridx = 0; gbc.gridy = 1;
                gbc.fill = GridBagConstraints.NONE;
                JLabel lblNumeroVendaOriginal = new JLabel("Nº Venda Original:");
                lblNumeroVendaOriginal.setFont(new Font("Segoe UI", Font.BOLD, 12));
                lblNumeroVendaOriginal.setForeground(DARK_GRAY);
                panel.add(lblNumeroVendaOriginal, gbc);
                gbc.gridx = 1; gbc.gridy = 1;
                gbc.fill = GridBagConstraints.HORIZONTAL;
                ElegantTextField txtNumeroVendaOriginal = new ElegantTextField(15);
                txtNumeroVendaOriginal.setToolTipText("Número da venda a ser devolvida");
                panel.add(txtNumeroVendaOriginal, gbc);
                SystemLogger.info("VENDAS - Campo 'Nº Venda Original' criado");
                
                // Cliente
                SystemLogger.ui("📝 VENDAS - Criando campo: Cliente (Devoluções)");
                gbc.gridx = 0; gbc.gridy = 2;
                gbc.fill = GridBagConstraints.NONE;
                JLabel lblClienteDevolucao = new JLabel("Cliente:");
                lblClienteDevolucao.setFont(new Font("Segoe UI", Font.BOLD, 12));
                lblClienteDevolucao.setForeground(DARK_GRAY);
                panel.add(lblClienteDevolucao, gbc);
                gbc.gridx = 1; gbc.gridy = 2;
                gbc.fill = GridBagConstraints.HORIZONTAL;
                ElegantTextField txtClienteDevolucao = new ElegantTextField(40);
                txtClienteDevolucao.setEditable(false);
                txtClienteDevolucao.setToolTipText("Cliente da venda original (preenchido automaticamente)");
                panel.add(txtClienteDevolucao, gbc);
                SystemLogger.info("VENDAS - Campo 'Cliente' criado (Devoluções - readonly)");
                
                // Data da Devolução
                SystemLogger.ui("📝 VENDAS - Criando campo: Data Devolução");
                gbc.gridx = 0; gbc.gridy = 3;
                gbc.fill = GridBagConstraints.NONE;
                JLabel lblDataDevolucao = new JLabel("Data Devolução:");
                lblDataDevolucao.setFont(new Font("Segoe UI", Font.BOLD, 12));
                lblDataDevolucao.setForeground(DARK_GRAY);
                panel.add(lblDataDevolucao, gbc);
                gbc.gridx = 1; gbc.gridy = 3;
                gbc.fill = GridBagConstraints.HORIZONTAL;
                ElegantTextField txtDataDevolucao = new ElegantTextField(10);
                txtDataDevolucao.setText(new java.text.SimpleDateFormat("dd/MM/yyyy").format(new java.util.Date()));
                txtDataDevolucao.setEditable(false);
                panel.add(txtDataDevolucao, gbc);
                SystemLogger.info("VENDAS - Campo 'Data Devolução' criado (readonly)");
                
                // Motivo da Devolução
                SystemLogger.ui("📝 VENDAS - Criando campo: Motivo Devolução");
                gbc.gridx = 0; gbc.gridy = 4;
                gbc.fill = GridBagConstraints.NONE;
                JLabel lblMotivoDevolucao = new JLabel("Motivo Devolução:");
                lblMotivoDevolucao.setFont(new Font("Segoe UI", Font.BOLD, 12));
                lblMotivoDevolucao.setForeground(DARK_GRAY);
                panel.add(lblMotivoDevolucao, gbc);
                gbc.gridx = 1; gbc.gridy = 4;
                gbc.fill = GridBagConstraints.HORIZONTAL;
                JComboBox<String> cbMotivoDevolucao = new JComboBox<>(new String[]{
                    "Produto com defeito", "Produto diferente do pedido", "Arrependimento", 
                    "Não recebeu o produto", "Outro"
                });
                cbMotivoDevolucao.setFont(new Font("Segoe UI", Font.PLAIN, 12));
                panel.add(cbMotivoDevolucao, gbc);
                SystemLogger.info("VENDAS - Campo 'Motivo Devolução' criado");
                
                // Tabela de Produtos para Devolução
                gbc.gridx = 0; gbc.gridy = 5; gbc.gridwidth = 2;
                gbc.fill = GridBagConstraints.BOTH;
                gbc.weightx = 1.0; gbc.weighty = 1.0;
                
                String[] colunasTabelaDevolucao = {"Código", "Produto", "Qtde", "Valor Unit.", "Valor Total", "Motivo"};
                Object[][] dadosTabelaDevolucao = {};
                
                JTable tabelaDevolucao = new JTable(dadosTabelaDevolucao, colunasTabelaDevolucao);
                tabelaDevolucao.setFont(new Font("Segoe UI", Font.PLAIN, 11));
                tabelaDevolucao.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 12));
                tabelaDevolucao.getTableHeader().setBackground(new Color(70, 130, 180));
                tabelaDevolucao.getTableHeader().setForeground(WHITE);
                tabelaDevolucao.setRowHeight(25);
                tabelaDevolucao.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
                tabelaDevolucao.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
                
                JScrollPane scrollPaneTabela = new JScrollPane(tabelaDevolucao);
                scrollPaneTabela.setPreferredSize(new Dimension(600, 200));
                scrollPaneTabela.setBorder(BorderFactory.createTitledBorder(
                    BorderFactory.createEtchedBorder(), "Produtos para Devolução",
                    TitledBorder.LEFT, TitledBorder.TOP, new Font("Segoe UI", Font.BOLD, 12),
                    DARK_GRAY));
                panel.add(scrollPaneTabela, gbc);
                
                // Valor Total da Devolução
                gbc.gridx = 0; gbc.gridy = 5; gbc.gridwidth = 1;
                gbc.fill = GridBagConstraints.NONE;
                gbc.weightx = 0; gbc.weighty = 0;
                JLabel lblValorTotalDevolucao = new JLabel("Valor Total:");
                lblValorTotalDevolucao.setFont(new Font("Segoe UI", Font.BOLD, 14));
                lblValorTotalDevolucao.setForeground(DARK_GRAY);
                panel.add(lblValorTotalDevolucao, gbc);
                gbc.gridx = 1; gbc.gridy = 5;
                gbc.fill = GridBagConstraints.HORIZONTAL;
                ElegantTextField txtValorTotalDevolucao = new ElegantTextField(15);
                txtValorTotalDevolucao.setText("R$ 0,00");
                txtValorTotalDevolucao.setEditable(false);
                txtValorTotalDevolucao.setFont(new Font("Segoe UI", Font.BOLD, 14));
                txtValorTotalDevolucao.setHorizontalAlignment(JTextField.RIGHT);
                panel.add(txtValorTotalDevolucao, gbc);
                break;
                
            case "🚚 Entregas":
                SystemLogger.ui("🚚 VENDAS - Criando formulário Entregas");
                
                // Painel de Busca
                gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
                gbc.fill = GridBagConstraints.HORIZONTAL;
                gbc.insets = new Insets(5, 5, 15, 5);
                
                JPanel buscaPanelEntrega = new JPanel(new GridBagLayout());
                buscaPanelEntrega.setBorder(BorderFactory.createTitledBorder(
                    BorderFactory.createEtchedBorder(), "🔍 Buscar Entregas",
                    TitledBorder.LEFT, TitledBorder.TOP, new Font("Segoe UI", Font.BOLD, 12),
                    DARK_GRAY));
                
                GridBagConstraints gbcBuscaEntrega = new GridBagConstraints();
                gbcBuscaEntrega.insets = new Insets(5, 5, 5, 5);
                
                // Campo de busca
                gbcBuscaEntrega.gridx = 0; gbcBuscaEntrega.gridy = 0;
                gbcBuscaEntrega.fill = GridBagConstraints.HORIZONTAL;
                gbcBuscaEntrega.weightx = 1.0;
                JLabel lblBuscaEntrega = new JLabel("Buscar:");
                lblBuscaEntrega.setFont(new Font("Segoe UI", Font.BOLD, 12));
                lblBuscaEntrega.setForeground(DARK_GRAY);
                buscaPanelEntrega.add(lblBuscaEntrega, gbcBuscaEntrega);
                
                gbcBuscaEntrega.gridx = 1; gbcBuscaEntrega.gridy = 0;
                gbcBuscaEntrega.fill = GridBagConstraints.HORIZONTAL;
                gbcBuscaEntrega.weightx = 2.0;
                ElegantTextField txtBuscaEntrega = new ElegantTextField(30);
                txtBuscaEntrega.setToolTipText("Digite número do pedido, cliente ou endereço");
                buscaPanelEntrega.add(txtBuscaEntrega, gbcBuscaEntrega);
                
                // Botões de busca
                gbcBuscaEntrega.gridx = 2; gbcBuscaEntrega.gridy = 0;
                gbcBuscaEntrega.fill = GridBagConstraints.NONE;
                gbcBuscaEntrega.weightx = 0;
                ElegantButton btnBuscarEntrega = new ElegantButton("🔍 Buscar", PRIMARY_COLOR, false);
                btnBuscarEntrega.setForeground(WHITE);
                buscaPanelEntrega.add(btnBuscarEntrega, gbcBuscaEntrega);
                
                gbcBuscaEntrega.gridx = 3; gbcBuscaEntrega.gridy = 0;
                ElegantButton btnLimparBuscaEntrega = new ElegantButton("🔄 Limpar", MEDIUM_GRAY, false);
                btnLimparBuscaEntrega.setForeground(WHITE);
                buscaPanelEntrega.add(btnLimparBuscaEntrega, gbcBuscaEntrega);
                
                panel.add(buscaPanelEntrega, gbc);
                
                // Resetar gridwidth para campos abaixo
                gbc.gridwidth = 1;
                gbc.insets = new Insets(5, 5, 5, 5);
                
                // Número do Pedido
                gbc.gridx = 0; gbc.gridy = 1;
                gbc.fill = GridBagConstraints.NONE;
                JLabel lblNumeroPedido = new JLabel("Nº Pedido:");
                lblNumeroPedido.setFont(new Font("Segoe UI", Font.BOLD, 12));
                lblNumeroPedido.setForeground(DARK_GRAY);
                panel.add(lblNumeroPedido, gbc);
                gbc.gridx = 1; gbc.gridy = 1;
                gbc.fill = GridBagConstraints.HORIZONTAL;
                ElegantTextField txtNumeroPedido = new ElegantTextField(15);
                txtNumeroPedido.setToolTipText("Número do pedido para entrega");
                panel.add(txtNumeroPedido, gbc);
                
                // Cliente
                gbc.gridx = 0; gbc.gridy = 2;
                gbc.fill = GridBagConstraints.NONE;
                JLabel lblClienteEntrega = new JLabel("Cliente:");
                lblClienteEntrega.setFont(new Font("Segoe UI", Font.BOLD, 12));
                lblClienteEntrega.setForeground(DARK_GRAY);
                panel.add(lblClienteEntrega, gbc);
                gbc.gridx = 1; gbc.gridy = 2;
                gbc.fill = GridBagConstraints.HORIZONTAL;
                ElegantTextField txtClienteEntrega = new ElegantTextField(40);
                txtClienteEntrega.setToolTipText("Nome do cliente");
                panel.add(txtClienteEntrega, gbc);
                
                // Data de Entrega
                gbc.gridx = 0; gbc.gridy = 3;
                gbc.fill = GridBagConstraints.NONE;
                JLabel lblDataEntrega = new JLabel("Data Entrega:");
                lblDataEntrega.setFont(new Font("Segoe UI", Font.BOLD, 12));
                lblDataEntrega.setForeground(DARK_GRAY);
                panel.add(lblDataEntrega, gbc);
                gbc.gridx = 1; gbc.gridy = 3;
                gbc.fill = GridBagConstraints.HORIZONTAL;
                ElegantTextField txtDataEntrega = new ElegantTextField(10);
                txtDataEntrega.setToolTipText("Data prevista para entrega");
                panel.add(txtDataEntrega, gbc);
                
                // Endereço de Entrega
                gbc.gridx = 0; gbc.gridy = 4;
                gbc.fill = GridBagConstraints.NONE;
                JLabel lblEnderecoEntrega = new JLabel("Endereço:");
                lblEnderecoEntrega.setFont(new Font("Segoe UI", Font.BOLD, 12));
                lblEnderecoEntrega.setForeground(DARK_GRAY);
                panel.add(lblEnderecoEntrega, gbc);
                gbc.gridx = 1; gbc.gridy = 4;
                gbc.fill = GridBagConstraints.HORIZONTAL;
                ElegantTextField txtEnderecoEntrega = new ElegantTextField(50);
                txtEnderecoEntrega.setToolTipText("Endereço completo de entrega");
                panel.add(txtEnderecoEntrega, gbc);
                
                // Status da Entrega
                gbc.gridx = 0; gbc.gridy = 5;
                gbc.fill = GridBagConstraints.NONE;
                JLabel lblStatusEntrega = new JLabel("Status:");
                lblStatusEntrega.setFont(new Font("Segoe UI", Font.BOLD, 12));
                lblStatusEntrega.setForeground(DARK_GRAY);
                panel.add(lblStatusEntrega, gbc);
                gbc.gridx = 1; gbc.gridy = 5;
                gbc.fill = GridBagConstraints.HORIZONTAL;
                JComboBox<String> cbStatusEntrega = new JComboBox<>(new String[]{
                    "Pendente", "Em Transporte", "Entregue", "Cancelado", "Devolvido"
                });
                cbStatusEntrega.setFont(new Font("Segoe UI", Font.PLAIN, 12));
                panel.add(cbStatusEntrega, gbc);
                
                // Motorista
                gbc.gridx = 0; gbc.gridy = 6;
                gbc.fill = GridBagConstraints.NONE;
                JLabel lblMotorista = new JLabel("Motorista:");
                lblMotorista.setFont(new Font("Segoe UI", Font.BOLD, 12));
                lblMotorista.setForeground(DARK_GRAY);
                panel.add(lblMotorista, gbc);
                gbc.gridx = 1; gbc.gridy = 6;
                gbc.fill = GridBagConstraints.HORIZONTAL;
                JComboBox<String> cbMotorista = new JComboBox<>(new String[]{
                    "Selecione", "João Silva", "Maria Santos", "Carlos Oliveira", "Ana Costa"
                });
                cbMotorista.setFont(new Font("Segoe UI", Font.PLAIN, 12));
                panel.add(cbMotorista, gbc);
                
                // Tabela de Produtos para Entrega
                gbc.gridx = 0; gbc.gridy = 7; gbc.gridwidth = 2;
                gbc.fill = GridBagConstraints.BOTH;
                gbc.weightx = 1.0; gbc.weighty = 1.0;
                
                String[] colunasTabelaEntrega = {"Código", "Produto", "Qtde", "Status Entrega", "Data Prevista", "Observações"};
                Object[][] dadosTabelaEntrega = {};
                JTable tabelaEntrega = new JTable(dadosTabelaEntrega, colunasTabelaEntrega);
                tabelaEntrega.setFont(new Font("Segoe UI", Font.PLAIN, 11));
                tabelaEntrega.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 12));
                tabelaEntrega.getTableHeader().setBackground(new Color(70, 130, 180));
                tabelaEntrega.getTableHeader().setForeground(WHITE); 
                tabelaEntrega.setRowHeight(25);
                tabelaEntrega.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
                tabelaEntrega.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
                
                JScrollPane scrollPaneTabelaEntrega = new JScrollPane(tabelaEntrega);
                scrollPaneTabelaEntrega.setPreferredSize(new Dimension(700, 200));
                scrollPaneTabelaEntrega.setBorder(BorderFactory.createTitledBorder(
                    BorderFactory.createEtchedBorder(), "Produtos para Entrega",
                    TitledBorder.LEFT, TitledBorder.TOP, new Font("Segoe UI", Font.BOLD, 12),
                    DARK_GRAY));
                panel.add(scrollPaneTabelaEntrega, gbc);
                break;
                
            case "🔍 Consultar Vendas":
                SystemLogger.ui("🔍 VENDAS - Criando formulário Consultar Vendas");
                
                // Painel de Busca
                gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
                gbc.fill = GridBagConstraints.HORIZONTAL;
                gbc.insets = new Insets(5, 5, 15, 5);
                
                JPanel buscaPanelVendas = new JPanel(new GridBagLayout());
                buscaPanelVendas.setBorder(BorderFactory.createTitledBorder(
                    BorderFactory.createEtchedBorder(), "🔍 Consultar Vendas",
                    TitledBorder.LEFT, TitledBorder.TOP, new Font("Segoe UI", Font.BOLD, 12),
                    DARK_GRAY));
                
                GridBagConstraints gbcBuscaVendas = new GridBagConstraints();
                gbcBuscaVendas.insets = new Insets(5, 5, 5, 5);
                
                // Campo de busca
                gbcBuscaVendas.gridx = 0; gbcBuscaVendas.gridy = 0;
                gbcBuscaVendas.fill = GridBagConstraints.HORIZONTAL;
                gbcBuscaVendas.weightx = 1.0;
                JLabel lblBuscaVendas = new JLabel("Buscar:");
                lblBuscaVendas.setFont(new Font("Segoe UI", Font.BOLD, 12));
                lblBuscaVendas.setForeground(DARK_GRAY);
                buscaPanelVendas.add(lblBuscaVendas, gbcBuscaVendas);
                
                gbcBuscaVendas.gridx = 1; gbcBuscaVendas.gridy = 0;
                gbcBuscaVendas.fill = GridBagConstraints.HORIZONTAL;
                gbcBuscaVendas.weightx = 2.0;
                ElegantTextField txtBuscaVendas = new ElegantTextField(30);
                txtBuscaVendas.setToolTipText("Digite número da venda, cliente ou produto");
                buscaPanelVendas.add(txtBuscaVendas, gbcBuscaVendas);
                
                // Botões de busca
                gbcBuscaVendas.gridx = 2; gbcBuscaVendas.gridy = 0;
                gbcBuscaVendas.fill = GridBagConstraints.NONE;
                gbcBuscaVendas.weightx = 0;
                ElegantButton btnBuscarVendas = new ElegantButton("🔍 Buscar", PRIMARY_COLOR, false);
                btnBuscarVendas.setForeground(WHITE);
                buscaPanelVendas.add(btnBuscarVendas, gbcBuscaVendas);
                
                gbcBuscaVendas.gridx = 3; gbcBuscaVendas.gridy = 0;
                ElegantButton btnLimparBuscaVendas = new ElegantButton("🔄 Limpar", MEDIUM_GRAY, false);
                btnLimparBuscaVendas.setForeground(WHITE);
                buscaPanelVendas.add(btnLimparBuscaVendas, gbcBuscaVendas);
                
                panel.add(buscaPanelVendas, gbc);
                
                // Resetar gridwidth para campos abaixo
                gbc.gridwidth = 1;
                gbc.insets = new Insets(5, 5, 5, 5);
                
                // Período de consulta
                gbc.gridx = 0; gbc.gridy = 1;
                gbc.fill = GridBagConstraints.NONE;
                JLabel lblPeriodo = new JLabel("Período:");
                lblPeriodo.setFont(new Font("Segoe UI", Font.BOLD, 12));
                lblPeriodo.setForeground(DARK_GRAY);
                panel.add(lblPeriodo, gbc);
                
                gbc.gridx = 1; gbc.gridy = 1;
                gbc.fill = GridBagConstraints.HORIZONTAL;
                JPanel periodoPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
                
                JLabel lblDe = new JLabel("De:");
                lblDe.setFont(new Font("Segoe UI", Font.PLAIN, 12));
                periodoPanel.add(lblDe);
                ElegantTextField txtDataInicio = new ElegantTextField(10);
                txtDataInicio.setToolTipText("Data inicial");
                periodoPanel.add(txtDataInicio);
                
                JLabel lblAte = new JLabel("Até:");
                lblAte.setFont(new Font("Segoe UI", Font.PLAIN, 12));
                periodoPanel.add(lblAte);
                ElegantTextField txtDataFim = new ElegantTextField(10);
                txtDataFim.setToolTipText("Data final");
                periodoPanel.add(txtDataFim);
                
                panel.add(periodoPanel, gbc);
                
                // Status da Venda
                gbc.gridx = 0; gbc.gridy = 2;
                gbc.fill = GridBagConstraints.NONE;
                JLabel lblStatusVenda = new JLabel("Status:");
                lblStatusVenda.setFont(new Font("Segoe UI", Font.BOLD, 12));
                lblStatusVenda.setForeground(DARK_GRAY);
                panel.add(lblStatusVenda, gbc);
                gbc.gridx = 1; gbc.gridy = 2;
                gbc.fill = GridBagConstraints.HORIZONTAL;
                JComboBox<String> cbStatusVenda = new JComboBox<>(new String[]{
                    "Todos", "Concluída", "Cancelada", "Pendente", "Em Andamento"
                });
                cbStatusVenda.setFont(new Font("Segoe UI", Font.PLAIN, 12));
                panel.add(cbStatusVenda, gbc);
                
                // Tabela de Vendas
                gbc.gridx = 0; gbc.gridy = 3; gbc.gridwidth = 2;
                gbc.fill = GridBagConstraints.BOTH;
                gbc.weightx = 1.0; gbc.weighty = 1.0;
                
                String[] colunasTabelaVendas = {"Nº Venda", "Cliente", "Data", "Valor Total", "Status", "Vendedor"};
                Object[][] dadosTabelaVendas = {};
                
                JTable tabelaVendas = new JTable(dadosTabelaVendas, colunasTabelaVendas);
                tabelaVendas.setFont(new Font("Segoe UI", Font.PLAIN, 11));
                tabelaVendas.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 12));
                tabelaVendas.getTableHeader().setBackground(new Color(70, 130, 180));
                tabelaVendas.getTableHeader().setForeground(WHITE);
                tabelaVendas.setRowHeight(25);
                tabelaVendas.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
                tabelaVendas.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
                
                JScrollPane scrollPaneTabelaVendas = new JScrollPane(tabelaVendas);
                scrollPaneTabelaVendas.setPreferredSize(new Dimension(700, 200));
                scrollPaneTabelaVendas.setBorder(BorderFactory.createTitledBorder(
                    BorderFactory.createEtchedBorder(), "Vendas Encontradas",
                    TitledBorder.LEFT, TitledBorder.TOP, new Font("Segoe UI", Font.BOLD, 12),
                    DARK_GRAY));
                panel.add(scrollPaneTabelaVendas, gbc);
                break;
                
            case "📊 Resumo Diário":
                SystemLogger.ui("📊 VENDAS - Criando formulário Resumo Diário");
                
                // Painel de Busca
                gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
                gbc.fill = GridBagConstraints.HORIZONTAL;
                gbc.insets = new Insets(5, 5, 15, 5);
                
                JPanel buscaPanelResumo = new JPanel(new GridBagLayout());
                buscaPanelResumo.setBorder(BorderFactory.createTitledBorder(
                    BorderFactory.createEtchedBorder(), "📊 Resumo Diário",
                    TitledBorder.LEFT, TitledBorder.TOP, new Font("Segoe UI", Font.BOLD, 12),
                    DARK_GRAY));
                
                GridBagConstraints gbcBuscaResumo = new GridBagConstraints();
                gbcBuscaResumo.insets = new Insets(5, 5, 5, 5);
                
                // Campo de busca
                gbcBuscaResumo.gridx = 0; gbcBuscaResumo.gridy = 0;
                gbcBuscaResumo.fill = GridBagConstraints.HORIZONTAL;
                gbcBuscaResumo.weightx = 1.0;
                JLabel lblBuscaResumo = new JLabel("Buscar:");
                lblBuscaResumo.setFont(new Font("Segoe UI", Font.BOLD, 12));
                lblBuscaResumo.setForeground(DARK_GRAY);
                buscaPanelResumo.add(lblBuscaResumo, gbcBuscaResumo);
                
                gbcBuscaResumo.gridx = 1; gbcBuscaResumo.gridy = 0;
                gbcBuscaResumo.fill = GridBagConstraints.HORIZONTAL;
                gbcBuscaResumo.weightx = 2.0;
                ElegantTextField txtBuscaResumo = new ElegantTextField(30);
                txtBuscaResumo.setToolTipText("Digite data, vendedor ou forma de pagamento");
                buscaPanelResumo.add(txtBuscaResumo, gbcBuscaResumo);
                
                // Botões de busca
                gbcBuscaResumo.gridx = 2; gbcBuscaResumo.gridy = 0;
                gbcBuscaResumo.fill = GridBagConstraints.NONE;
                gbcBuscaResumo.weightx = 0;
                ElegantButton btnBuscarResumo = new ElegantButton("🔍 Buscar", PRIMARY_COLOR, false);
                btnBuscarResumo.setForeground(WHITE);
                buscaPanelResumo.add(btnBuscarResumo, gbcBuscaResumo);
                
                gbcBuscaResumo.gridx = 3; gbcBuscaResumo.gridy = 0;
                ElegantButton btnLimparBuscaResumo = new ElegantButton("🔄 Limpar", MEDIUM_GRAY, false);
                btnLimparBuscaResumo.setForeground(WHITE);
                buscaPanelResumo.add(btnLimparBuscaResumo, gbcBuscaResumo);
                
                panel.add(buscaPanelResumo, gbc);
                
                // Resetar gridwidth para campos abaixo
                gbc.gridwidth = 1;
                gbc.insets = new Insets(5, 5, 5, 5);
                
                // Filtros de resumo
                gbc.gridx = 0; gbc.gridy = 1;
                gbc.fill = GridBagConstraints.NONE;
                JLabel lblFiltrosResumo = new JLabel("Filtros:");
                lblFiltrosResumo.setFont(new Font("Segoe UI", Font.BOLD, 12));
                lblFiltrosResumo.setForeground(DARK_GRAY);
                panel.add(lblFiltrosResumo, gbc);
                
                gbc.gridx = 1; gbc.gridy = 1;
                gbc.fill = GridBagConstraints.HORIZONTAL;
                JPanel filtrosPanelResumo = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
                
                JLabel lblDataResumo = new JLabel("Data:");
                lblDataResumo.setFont(new Font("Segoe UI", Font.PLAIN, 12));
                filtrosPanelResumo.add(lblDataResumo);
                ElegantTextField txtDataResumo = new ElegantTextField(10);
                txtDataResumo.setToolTipText("Data do resumo");
                filtrosPanelResumo.add(txtDataResumo);
                
                JLabel lblVendedorResumo = new JLabel("Vendedor:");
                lblVendedorResumo.setFont(new Font("Segoe UI", Font.PLAIN, 12));
                filtrosPanelResumo.add(lblVendedorResumo);
                JComboBox<String> cbVendedorResumo = new JComboBox<>(new String[]{
                    "Todos", "Administrador", "Vendedor 1", "Vendedor 2", "Caixa 1"
                });
                cbVendedorResumo.setFont(new Font("Segoe UI", Font.PLAIN, 12));
                filtrosPanelResumo.add(cbVendedorResumo);
                
                panel.add(filtrosPanelResumo, gbc);
                
                // Tabela de Resumo Diário
                gbc.gridx = 0; gbc.gridy = 2; gbc.gridwidth = 2;
                gbc.fill = GridBagConstraints.BOTH;
                gbc.weightx = 1.0; gbc.weighty = 1.0;
                
                String[] colunasTabelaResumo = {"Data", "Vendedor", "Total Vendas", "Total Vendido", "Forma Pagamento", "Comissão"};
                Object[][] dadosTabelaResumo = {};
                
                JTable tabelaResumo = new JTable(dadosTabelaResumo, colunasTabelaResumo);
                tabelaResumo.setFont(new Font("Segoe UI", Font.PLAIN, 11));
                tabelaResumo.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 12));
                tabelaResumo.getTableHeader().setBackground(new Color(70, 130, 180));
                tabelaResumo.getTableHeader().setForeground(WHITE);
                tabelaResumo.setRowHeight(25);
                tabelaResumo.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
                tabelaResumo.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
                
                JScrollPane scrollPaneTabelaResumo = new JScrollPane(tabelaResumo);
                scrollPaneTabelaResumo.setPreferredSize(new Dimension(700, 200));
                scrollPaneTabelaResumo.setBorder(BorderFactory.createTitledBorder(
                    BorderFactory.createEtchedBorder(), "Resumo Diário",
                    TitledBorder.LEFT, TitledBorder.TOP, new Font("Segoe UI", Font.BOLD, 12),
                    DARK_GRAY));
                panel.add(scrollPaneTabelaResumo, gbc);
                break;
                
            case "📈 Relatório de Vendas":
                // Painel de Busca
                gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
                gbc.fill = GridBagConstraints.HORIZONTAL;
                gbc.insets = new Insets(5, 5, 15, 5);
                
                JPanel buscaPanelRelatorio = new JPanel(new GridBagLayout());
                buscaPanelRelatorio.setBorder(BorderFactory.createTitledBorder(
                    BorderFactory.createEtchedBorder(), "📈 Relatório de Vendas",
                    TitledBorder.LEFT, TitledBorder.TOP, new Font("Segoe UI", Font.BOLD, 12),
                    DARK_GRAY));
                
                GridBagConstraints gbcBuscaRelatorio = new GridBagConstraints();
                gbcBuscaRelatorio.insets = new Insets(5, 5, 5, 5);
                
                // Campo de busca
                gbcBuscaRelatorio.gridx = 0; gbcBuscaRelatorio.gridy = 0;
                gbcBuscaRelatorio.fill = GridBagConstraints.HORIZONTAL;
                gbcBuscaRelatorio.weightx = 1.0;
                JLabel lblBuscaRelatorio = new JLabel("Buscar:");
                lblBuscaRelatorio.setFont(new Font("Segoe UI", Font.BOLD, 12));
                lblBuscaRelatorio.setForeground(DARK_GRAY);
                buscaPanelRelatorio.add(lblBuscaRelatorio, gbcBuscaRelatorio);
                
                gbcBuscaRelatorio.gridx = 1; gbcBuscaRelatorio.gridy = 0;
                gbcBuscaRelatorio.fill = GridBagConstraints.HORIZONTAL;
                gbcBuscaRelatorio.weightx = 2.0;
                ElegantTextField txtBuscaRelatorio = new ElegantTextField(30);
                txtBuscaRelatorio.setToolTipText("Digite período, cliente ou produto");
                buscaPanelRelatorio.add(txtBuscaRelatorio, gbcBuscaRelatorio);
                
                // Botões de busca
                gbcBuscaRelatorio.gridx = 2; gbcBuscaRelatorio.gridy = 0;
                gbcBuscaRelatorio.fill = GridBagConstraints.NONE;
                gbcBuscaRelatorio.weightx = 0;
                ElegantButton btnBuscarRelatorio = new ElegantButton("🔍 Buscar", PRIMARY_COLOR, false);
                btnBuscarRelatorio.setForeground(WHITE);
                buscaPanelRelatorio.add(btnBuscarRelatorio, gbcBuscaRelatorio);
                
                gbcBuscaRelatorio.gridx = 3; gbcBuscaRelatorio.gridy = 0;
                ElegantButton btnLimparBuscaRelatorio = new ElegantButton("🔄 Limpar", MEDIUM_GRAY, false);
                btnLimparBuscaRelatorio.setForeground(WHITE);
                buscaPanelRelatorio.add(btnLimparBuscaRelatorio, gbcBuscaRelatorio);
                
                panel.add(buscaPanelRelatorio, gbc);
                
                // Resetar gridwidth para campos abaixo
                gbc.gridwidth = 1;
                gbc.insets = new Insets(5, 5, 5, 5);
                
                // Filtros de relatório
                gbc.gridx = 0; gbc.gridy = 1;
                gbc.fill = GridBagConstraints.NONE;
                JLabel lblFiltrosRelatorio = new JLabel("Filtros:");
                lblFiltrosRelatorio.setFont(new Font("Segoe UI", Font.BOLD, 12));
                lblFiltrosRelatorio.setForeground(DARK_GRAY);
                panel.add(lblFiltrosRelatorio, gbc);
                
                gbc.gridx = 1; gbc.gridy = 1;
                gbc.fill = GridBagConstraints.HORIZONTAL;
                JPanel filtrosPanelRelatorio = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
                
                JLabel lblPeriodoRelatorio = new JLabel("Período:");
                lblPeriodoRelatorio.setFont(new Font("Segoe UI", Font.PLAIN, 12));
                filtrosPanelRelatorio.add(lblPeriodoRelatorio);
                JPanel periodoPanelRelatorio = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
                
                JLabel lblDeRelatorio = new JLabel("De:");
                lblDeRelatorio.setFont(new Font("Segoe UI", Font.PLAIN, 12));
                periodoPanelRelatorio.add(lblDeRelatorio);
                ElegantTextField txtDataInicioRelatorio = new ElegantTextField(10);
                txtDataInicioRelatorio.setToolTipText("Data inicial");
                periodoPanelRelatorio.add(txtDataInicioRelatorio);
                
                JLabel lblAteRelatorio = new JLabel("Até:");
                lblAteRelatorio.setFont(new Font("Segoe UI", Font.PLAIN, 12));
                periodoPanelRelatorio.add(lblAteRelatorio);
                ElegantTextField txtDataFimRelatorio = new ElegantTextField(10);
                txtDataFimRelatorio.setToolTipText("Data final");
                periodoPanelRelatorio.add(txtDataFimRelatorio);
                
                filtrosPanelRelatorio.add(periodoPanelRelatorio);
                
                JLabel lblTipoRelatorio = new JLabel("Tipo:");
                lblTipoRelatorio.setFont(new Font("Segoe UI", Font.PLAIN, 12));
                filtrosPanelRelatorio.add(lblTipoRelatorio);
                JComboBox<String> cbTipoRelatorio = new JComboBox<>(new String[]{
                    "Todos", "Vendas", "Comissões", "Cancelamentos"
                });
                cbTipoRelatorio.setFont(new Font("Segoe UI", Font.PLAIN, 12));
                filtrosPanelRelatorio.add(cbTipoRelatorio);
                
                panel.add(filtrosPanelRelatorio, gbc);
                
                // Tabela de Relatório de Vendas
                gbc.gridx = 0; gbc.gridy = 2; gbc.gridwidth = 2;
                gbc.fill = GridBagConstraints.BOTH;
                gbc.weightx = 1.0; gbc.weighty = 1.0;
                
                String[] colunasTabelaRelatorio = {"Data", "Cliente", "Produto", "Quantidade", "Valor", "Status", "Vendedor"};
                Object[][] dadosTabelaRelatorio = {};
                
                JTable tabelaRelatorio = new JTable(dadosTabelaRelatorio, colunasTabelaRelatorio);
                tabelaRelatorio.setFont(new Font("Segoe UI", Font.PLAIN, 11));
                tabelaRelatorio.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 12));
                tabelaRelatorio.getTableHeader().setBackground(new Color(70, 130, 180));
                tabelaRelatorio.getTableHeader().setForeground(WHITE);
                tabelaRelatorio.setRowHeight(25);
                tabelaRelatorio.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
                tabelaRelatorio.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
                
                JScrollPane scrollPaneTabelaRelatorio = new JScrollPane(tabelaRelatorio);
                scrollPaneTabelaRelatorio.setPreferredSize(new Dimension(700, 200));
                scrollPaneTabelaRelatorio.setBorder(BorderFactory.createTitledBorder(
                    BorderFactory.createEtchedBorder(), "Relatório de Vendas",
                    TitledBorder.LEFT, TitledBorder.TOP, new Font("Segoe UI", Font.BOLD, 12),
                    DARK_GRAY));
                panel.add(scrollPaneTabelaRelatorio, gbc);
                break;
                
            case "🏷️ Orçamentos":
                SystemLogger.ui("🏷️ VENDAS - Criando formulário Orçamentos");
                
                // Painel de Dados do Orçamento
                gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
                gbc.fill = GridBagConstraints.HORIZONTAL;
                gbc.insets = new Insets(5, 5, 10, 5);
                
                JPanel dadosPanelOrcamentos = new JPanel(new GridBagLayout());
                dadosPanelOrcamentos.setBorder(BorderFactory.createTitledBorder(
                    BorderFactory.createEtchedBorder(), "📋 Dados do Orçamento",
                    TitledBorder.LEFT, TitledBorder.TOP, new Font("Segoe UI", Font.BOLD, 12),
                    DARK_GRAY));
                
                GridBagConstraints gbcDados = new GridBagConstraints();
                gbcDados.insets = new Insets(3, 5, 3, 5);
                
                // Número do Orçamento
                gbcDados.gridx = 0; gbcDados.gridy = 0;
                gbcDados.fill = GridBagConstraints.NONE;
                gbcDados.anchor = GridBagConstraints.WEST;
                JLabel lblNumeroOrcamento = new JLabel("Nº Orçamento:");
                lblNumeroOrcamento.setFont(new Font("Segoe UI", Font.BOLD, 12));
                lblNumeroOrcamento.setForeground(DARK_GRAY);
                dadosPanelOrcamentos.add(lblNumeroOrcamento, gbcDados);
                
                gbcDados.gridx = 1; gbcDados.gridy = 0;
                gbcDados.fill = GridBagConstraints.HORIZONTAL;
                gbcDados.weightx = 1.0;
                ElegantTextField txtNumeroOrcamento = new ElegantTextField(15);
                txtNumeroOrcamento.setEditable(false);
                txtNumeroOrcamento.setText("AUTO");
                dadosPanelOrcamentos.add(txtNumeroOrcamento, gbcDados);
                
                // Data do Orçamento
                gbcDados.gridx = 2; gbcDados.gridy = 0;
                gbcDados.fill = GridBagConstraints.NONE;
                gbcDados.weightx = 0;
                JLabel lblDataOrcamento = new JLabel("Data:");
                lblDataOrcamento.setFont(new Font("Segoe UI", Font.BOLD, 12));
                lblDataOrcamento.setForeground(DARK_GRAY);
                dadosPanelOrcamentos.add(lblDataOrcamento, gbcDados);
                
                gbcDados.gridx = 3; gbcDados.gridy = 0;
                gbcDados.fill = GridBagConstraints.HORIZONTAL;
                gbcDados.weightx = 1.0;
                ElegantTextField txtDataOrcamento = new ElegantTextField(12);
                txtDataOrcamento.setEditable(false);
                dadosPanelOrcamentos.add(txtDataOrcamento, gbcDados);
                
                // Cliente
                gbcDados.gridx = 0; gbcDados.gridy = 1;
                gbcDados.fill = GridBagConstraints.NONE;
                gbcDados.weightx = 0;
                JLabel lblClienteOrcamento = new JLabel("Cliente:");
                lblClienteOrcamento.setFont(new Font("Segoe UI", Font.BOLD, 12));
                lblClienteOrcamento.setForeground(DARK_GRAY);
                dadosPanelOrcamentos.add(lblClienteOrcamento, gbcDados);
                
                gbcDados.gridx = 1; gbcDados.gridy = 1;
                gbcDados.fill = GridBagConstraints.HORIZONTAL;
                gbcDados.weightx = 2.0;
                ElegantTextField txtClienteOrcamento = new ElegantTextField(30);
                txtClienteOrcamento.setToolTipText("Nome do cliente");
                dadosPanelOrcamentos.add(txtClienteOrcamento, gbcDados);
                
                // Vendedor
                gbcDados.gridx = 2; gbcDados.gridy = 1;
                gbcDados.fill = GridBagConstraints.NONE;
                gbcDados.weightx = 0;
                JLabel lblVendedorOrcamento = new JLabel("Vendedor:");
                lblVendedorOrcamento.setFont(new Font("Segoe UI", Font.BOLD, 12));
                lblVendedorOrcamento.setForeground(DARK_GRAY);
                dadosPanelOrcamentos.add(lblVendedorOrcamento, gbcDados);
                
                gbcDados.gridx = 3; gbcDados.gridy = 1;
                gbcDados.fill = GridBagConstraints.HORIZONTAL;
                gbcDados.weightx = 1.0;
                ElegantTextField txtVendedorOrcamento = new ElegantTextField(20);
                txtVendedorOrcamento.setEditable(false);
                txtVendedorOrcamento.setText(usuarioAtual);
                dadosPanelOrcamentos.add(txtVendedorOrcamento, gbcDados);
                
                // Validade
                gbcDados.gridx = 0; gbcDados.gridy = 2;
                gbcDados.fill = GridBagConstraints.NONE;
                gbcDados.weightx = 0;
                JLabel lblValidadeOrcamento = new JLabel("Validade:");
                lblValidadeOrcamento.setFont(new Font("Segoe UI", Font.BOLD, 12));
                lblValidadeOrcamento.setForeground(DARK_GRAY);
                dadosPanelOrcamentos.add(lblValidadeOrcamento, gbcDados);
                
                gbcDados.gridx = 1; gbcDados.gridy = 2;
                gbcDados.fill = GridBagConstraints.HORIZONTAL;
                gbcDados.weightx = 1.0;
                ElegantTextField txtValidadeOrcamento = new ElegantTextField(12);
                txtValidadeOrcamento.setToolTipText("Data de validade do orçamento");
                dadosPanelOrcamentos.add(txtValidadeOrcamento, gbcDados);
                
                // Status
                gbcDados.gridx = 2; gbcDados.gridy = 2;
                gbcDados.fill = GridBagConstraints.NONE;
                gbcDados.weightx = 0;
                JLabel lblStatusOrcamento = new JLabel("Status:");
                lblStatusOrcamento.setFont(new Font("Segoe UI", Font.BOLD, 12));
                lblStatusOrcamento.setForeground(DARK_GRAY);
                dadosPanelOrcamentos.add(lblStatusOrcamento, gbcDados);
                
                gbcDados.gridx = 3; gbcDados.gridy = 2;
                gbcDados.fill = GridBagConstraints.HORIZONTAL;
                gbcDados.weightx = 1.0;
                JComboBox<String> cbStatusOrcamento = new JComboBox<>(new String[]{
                    "Aberto", "Aprovado", "Rejeitado", "Fechado"
                });
                cbStatusOrcamento.setFont(new Font("Segoe UI", Font.PLAIN, 12));
                dadosPanelOrcamentos.add(cbStatusOrcamento, gbcDados);
                
                // Valor Total
                gbcDados.gridx = 0; gbcDados.gridy = 3;
                gbcDados.fill = GridBagConstraints.NONE;
                gbcDados.weightx = 0;
                JLabel lblValorTotalOrcamento = new JLabel("Valor Total:");
                lblValorTotalOrcamento.setFont(new Font("Segoe UI", Font.BOLD, 12));
                lblValorTotalOrcamento.setForeground(DARK_GRAY);
                dadosPanelOrcamentos.add(lblValorTotalOrcamento, gbcDados);
                
                gbcDados.gridx = 1; gbcDados.gridy = 3;
                gbcDados.fill = GridBagConstraints.HORIZONTAL;
                gbcDados.weightx = 1.0;
                ElegantTextField txtValorTotalOrcamento = new ElegantTextField(15);
                txtValorTotalOrcamento.setToolTipText("Valor total do orçamento");
                dadosPanelOrcamentos.add(txtValorTotalOrcamento, gbcDados);
                
                // Condições de Pagamento
                gbcDados.gridx = 2; gbcDados.gridy = 3;
                gbcDados.fill = GridBagConstraints.NONE;
                gbcDados.weightx = 0;
                JLabel lblCondPagamentoOrcamento = new JLabel("Condições:");
                lblCondPagamentoOrcamento.setFont(new Font("Segoe UI", Font.BOLD, 12));
                lblCondPagamentoOrcamento.setForeground(DARK_GRAY);
                dadosPanelOrcamentos.add(lblCondPagamentoOrcamento, gbcDados);
                
                gbcDados.gridx = 3; gbcDados.gridy = 3;
                gbcDados.fill = GridBagConstraints.HORIZONTAL;
                gbcDados.weightx = 1.0;
                JComboBox<String> cbCondPagamentoOrcamento = new JComboBox<>(new String[]{
                    "À Vista", "7 Dias", "15 Dias", "30 Dias", "45 Dias", "60 Dias"
                });
                cbCondPagamentoOrcamento.setFont(new Font("Segoe UI", Font.PLAIN, 12));
                dadosPanelOrcamentos.add(cbCondPagamentoOrcamento, gbcDados);
                
                // Observações
                gbcDados.gridx = 0; gbcDados.gridy = 4; gbc.gridwidth = 4;
                gbcDados.fill = GridBagConstraints.HORIZONTAL;
                gbcDados.weightx = 1.0;
                JLabel lblObservacoesOrcamento = new JLabel("Observações:");
                lblObservacoesOrcamento.setFont(new Font("Segoe UI", Font.BOLD, 12));
                lblObservacoesOrcamento.setForeground(DARK_GRAY);
                dadosPanelOrcamentos.add(lblObservacoesOrcamento, gbcDados);
                
                gbcDados.gridx = 0; gbcDados.gridy = 5;
                gbcDados.fill = GridBagConstraints.HORIZONTAL;
                JTextArea txtObservacoesOrcamento = new JTextArea(3, 50);
                txtObservacoesOrcamento.setToolTipText("Observações adicionais do orçamento");
                txtObservacoesOrcamento.setFont(new Font("Segoe UI", Font.PLAIN, 12));
                txtObservacoesOrcamento.setLineWrap(true);
                txtObservacoesOrcamento.setWrapStyleWord(true);
                txtObservacoesOrcamento.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(MEDIUM_GRAY, 1),
                    BorderFactory.createEmptyBorder(5, 5, 5, 5)
                ));
                JScrollPane scrollObservacoes = new JScrollPane(txtObservacoesOrcamento);
                scrollObservacoes.setBorder(BorderFactory.createLineBorder(MEDIUM_GRAY, 1));
                dadosPanelOrcamentos.add(scrollObservacoes, gbcDados);
                
                panel.add(dadosPanelOrcamentos, gbc);
                
                // Painel de Busca
                gbc.gridx = 0; gbc.gridy = 1; gbc.gridwidth = 2;
                gbc.fill = GridBagConstraints.HORIZONTAL;
                gbc.insets = new Insets(5, 5, 15, 5);
                
                JPanel buscaPanelOrcamentos = new JPanel(new GridBagLayout());
                buscaPanelOrcamentos.setBorder(BorderFactory.createTitledBorder(
                    BorderFactory.createEtchedBorder(), "🔍 Consulta de Orçamentos",
                    TitledBorder.LEFT, TitledBorder.TOP, new Font("Segoe UI", Font.BOLD, 12),
                    DARK_GRAY));
                
                GridBagConstraints gbcBuscaOrcamentos = new GridBagConstraints();
                gbcBuscaOrcamentos.insets = new Insets(5, 5, 5, 5);
                
                // Campo de busca
                gbcBuscaOrcamentos.gridx = 0; gbcBuscaOrcamentos.gridy = 0;
                gbcBuscaOrcamentos.fill = GridBagConstraints.HORIZONTAL;
                gbcBuscaOrcamentos.weightx = 1.0;
                JLabel lblBuscaOrcamentos = new JLabel("Buscar:");
                lblBuscaOrcamentos.setFont(new Font("Segoe UI", Font.BOLD, 12));
                lblBuscaOrcamentos.setForeground(DARK_GRAY);
                buscaPanelOrcamentos.add(lblBuscaOrcamentos, gbcBuscaOrcamentos);
                
                gbcBuscaOrcamentos.gridx = 1; gbcBuscaOrcamentos.gridy = 0;
                gbcBuscaOrcamentos.fill = GridBagConstraints.HORIZONTAL;
                gbcBuscaOrcamentos.weightx = 2.0;
                ElegantTextField txtBuscaOrcamentos = new ElegantTextField(30);
                txtBuscaOrcamentos.setToolTipText("Digite número, cliente ou produto");
                buscaPanelOrcamentos.add(txtBuscaOrcamentos, gbcBuscaOrcamentos);
                
                // Botões de busca
                gbcBuscaOrcamentos.gridx = 2; gbcBuscaOrcamentos.gridy = 0;
                gbcBuscaOrcamentos.fill = GridBagConstraints.NONE;
                gbcBuscaOrcamentos.weightx = 0;
                ElegantButton btnBuscarOrcamentos = new ElegantButton("🔍 Buscar", PRIMARY_COLOR, false);
                btnBuscarOrcamentos.setForeground(WHITE);
                buscaPanelOrcamentos.add(btnBuscarOrcamentos, gbcBuscaOrcamentos);
                
                gbcBuscaOrcamentos.gridx = 3; gbcBuscaOrcamentos.gridy = 0;
                ElegantButton btnLimparBuscaOrcamentos = new ElegantButton("🔄 Limpar", MEDIUM_GRAY, false);
                btnLimparBuscaOrcamentos.setForeground(WHITE);
                buscaPanelOrcamentos.add(btnLimparBuscaOrcamentos, gbcBuscaOrcamentos);
                
                panel.add(buscaPanelOrcamentos, gbc);
                
                // Resetar gridwidth para campos abaixo
                gbc.gridwidth = 1;
                gbc.insets = new Insets(5, 5, 5, 5);
                
                // Filtros de orçamentos
                gbc.gridx = 0; gbc.gridy = 2;
                gbc.fill = GridBagConstraints.NONE;
                JLabel lblFiltrosOrcamentos = new JLabel("Filtros:");
                lblFiltrosOrcamentos.setFont(new Font("Segoe UI", Font.BOLD, 12));
                lblFiltrosOrcamentos.setForeground(DARK_GRAY);
                panel.add(lblFiltrosOrcamentos, gbc);
                
                gbc.gridx = 1; gbc.gridy = 2;
                gbc.fill = GridBagConstraints.HORIZONTAL;
                JPanel filtrosPanelOrcamentos = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
                
                JLabel lblStatusFiltro = new JLabel("Status:");
                lblStatusFiltro.setFont(new Font("Segoe UI", Font.PLAIN, 12));
                filtrosPanelOrcamentos.add(lblStatusFiltro);
                JComboBox<String> cbStatusFiltro = new JComboBox<>(new String[]{
                    "Todos", "Aberto", "Aprovado", "Rejeitado", "Fechado"
                });
                cbStatusFiltro.setFont(new Font("Segoe UI", Font.PLAIN, 12));
                filtrosPanelOrcamentos.add(cbStatusFiltro);
                
                JLabel lblValidadeFiltro = new JLabel("Validade:");
                lblValidadeFiltro.setFont(new Font("Segoe UI", Font.PLAIN, 12));
                filtrosPanelOrcamentos.add(lblValidadeFiltro);
                JComboBox<String> cbValidadeFiltro = new JComboBox<>(new String[]{
                    "Todos", "Hoje", "Esta Semana", "Este Mês", "Próximo Mês"
                });
                cbValidadeFiltro.setFont(new Font("Segoe UI", Font.PLAIN, 12));
                filtrosPanelOrcamentos.add(cbValidadeFiltro);
                
                panel.add(filtrosPanelOrcamentos, gbc);
                
                // Tabela de Orçamentos
                gbc.gridx = 0; gbc.gridy = 3; gbc.gridwidth = 2;
                gbc.fill = GridBagConstraints.BOTH;
                gbc.weightx = 1.0; gbc.weighty = 1.0;
                
                String[] colunasTabelaOrcamentos = {"Nº Orçamento", "Cliente", "Data", "Validade", "Valor Total", "Status", "Vendedor", "Condições"};
                Object[][] dadosTabelaOrcamentos = {};
                
                JTable tabelaOrcamentos = new JTable(dadosTabelaOrcamentos, colunasTabelaOrcamentos);
                tabelaOrcamentos.setFont(new Font("Segoe UI", Font.PLAIN, 11));
                tabelaOrcamentos.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 12));
                tabelaOrcamentos.getTableHeader().setBackground(new Color(70, 130, 180));
                tabelaOrcamentos.getTableHeader().setForeground(WHITE);
                tabelaOrcamentos.setRowHeight(25);
                tabelaOrcamentos.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
                tabelaOrcamentos.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
                
                JScrollPane scrollPaneTabelaOrcamentos = new JScrollPane(tabelaOrcamentos);
                scrollPaneTabelaOrcamentos.setPreferredSize(new Dimension(700, 200));
                scrollPaneTabelaOrcamentos.setBorder(BorderFactory.createTitledBorder(
                    BorderFactory.createEtchedBorder(), "Orçamentos Encontrados",
                    TitledBorder.LEFT, TitledBorder.TOP, new Font("Segoe UI", Font.BOLD, 12),
                    DARK_GRAY));
                panel.add(scrollPaneTabelaOrcamentos, gbc);
                break;
                
            case "🛒 Ponto de Venda":
                // Painel de Dados do Ponto de Venda
                gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
                gbc.fill = GridBagConstraints.HORIZONTAL;
                gbc.insets = new Insets(5, 5, 15, 5);
                
                JPanel dadosPanelPDV = new JPanel(new GridBagLayout());
                dadosPanelPDV.setBorder(BorderFactory.createTitledBorder(
                    BorderFactory.createEtchedBorder(), "🛒 Dados do Ponto de Venda",
                    TitledBorder.LEFT, TitledBorder.TOP, new Font("Segoe UI", Font.BOLD, 12),
                    DARK_GRAY));
                
                GridBagConstraints gbcDadosPDV = new GridBagConstraints();
                gbcDadosPDV.insets = new Insets(5, 5, 5, 5);
                
                // ID da Venda
                gbcDadosPDV.gridx = 0; gbcDadosPDV.gridy = 0;
                gbcDadosPDV.fill = GridBagConstraints.NONE;
                gbcDadosPDV.weightx = 0.3;
                JLabel lblIdVendaPDV = new JLabel("ID Venda:");
                lblIdVendaPDV.setFont(new Font("Segoe UI", Font.BOLD, 12));
                lblIdVendaPDV.setForeground(DARK_GRAY);
                dadosPanelPDV.add(lblIdVendaPDV, gbcDadosPDV);
                gbcDadosPDV.gridx = 1; gbcDadosPDV.gridy = 0;
                gbcDadosPDV.fill = GridBagConstraints.HORIZONTAL;
                gbcDadosPDV.weightx = 0.7;
                ElegantTextField txtIdVendaPDV = new ElegantTextField(15);
                txtIdVendaPDV.setEditable(false);
                txtIdVendaPDV.setText("AUTO");
                txtIdVendaPDV.setToolTipText("ID automático da venda");
                dadosPanelPDV.add(txtIdVendaPDV, gbcDadosPDV);
                
                // Data da Venda
                gbcDadosPDV.gridx = 2; gbcDadosPDV.gridy = 0;
                gbcDadosPDV.fill = GridBagConstraints.NONE;
                gbcDadosPDV.weightx = 0.3;
                JLabel lblDataVendaPDV = new JLabel("Data:");
                lblDataVendaPDV.setFont(new Font("Segoe UI", Font.BOLD, 12));
                lblDataVendaPDV.setForeground(DARK_GRAY);
                dadosPanelPDV.add(lblDataVendaPDV, gbcDadosPDV);
                gbcDadosPDV.gridx = 3; gbcDadosPDV.gridy = 0;
                gbcDadosPDV.fill = GridBagConstraints.HORIZONTAL;
                gbcDadosPDV.weightx = 0.7;
                ElegantTextField txtDataVendaPDV = new ElegantTextField(12);
                txtDataVendaPDV.setEditable(false);
                txtDataVendaPDV.setToolTipText("Data da venda");
                dadosPanelPDV.add(txtDataVendaPDV, gbcDadosPDV);
                
                // Cliente
                gbcDadosPDV.gridx = 0; gbcDadosPDV.gridy = 1;
                gbcDadosPDV.fill = GridBagConstraints.NONE;
                gbcDadosPDV.weightx = 0.3;
                JLabel lblClientePDV = new JLabel("Cliente:");
                lblClientePDV.setFont(new Font("Segoe UI", Font.BOLD, 12));
                lblClientePDV.setForeground(DARK_GRAY);
                dadosPanelPDV.add(lblClientePDV, gbcDadosPDV);
                gbcDadosPDV.gridx = 1; gbcDadosPDV.gridy = 1;
                gbcDadosPDV.fill = GridBagConstraints.HORIZONTAL;
                gbcDadosPDV.weightx = 1.7;
                ElegantTextField txtClientePDV = new ElegantTextField(40);
                txtClientePDV.setToolTipText("Nome do cliente");
                dadosPanelPDV.add(txtClientePDV, gbcDadosPDV);
                gbcDadosPDV.gridx = 2; gbcDadosPDV.gridy = 1;
                gbcDadosPDV.fill = GridBagConstraints.NONE;
                gbcDadosPDV.weightx = 0.3;
                ElegantButton btnBuscarClientePDV = new ElegantButton("🔍", PRIMARY_COLOR, false);
                btnBuscarClientePDV.setForeground(WHITE);
                btnBuscarClientePDV.setToolTipText("Buscar cliente");
                dadosPanelPDV.add(btnBuscarClientePDV, gbcDadosPDV);
                gbcDadosPDV.gridx = 3; gbcDadosPDV.gridy = 1;
                gbcDadosPDV.fill = GridBagConstraints.NONE;
                gbcDadosPDV.weightx = 0.7;
                ElegantButton btnNovoClientePDV = new ElegantButton("👤", SUCCESS_COLOR, false);
                btnNovoClientePDV.setForeground(WHITE);
                btnNovoClientePDV.setToolTipText("Novo cliente");
                dadosPanelPDV.add(btnNovoClientePDV, gbcDadosPDV);
                
                // Operador
                gbcDadosPDV.gridx = 0; gbcDadosPDV.gridy = 2;
                gbcDadosPDV.fill = GridBagConstraints.NONE;
                gbcDadosPDV.weightx = 0.3;
                JLabel lblOperadorPDV = new JLabel("Operador:");
                lblOperadorPDV.setFont(new Font("Segoe UI", Font.BOLD, 12));
                lblOperadorPDV.setForeground(DARK_GRAY);
                dadosPanelPDV.add(lblOperadorPDV, gbcDadosPDV);
                gbcDadosPDV.gridx = 1; gbcDadosPDV.gridy = 2;
                gbcDadosPDV.fill = GridBagConstraints.HORIZONTAL;
                gbcDadosPDV.weightx = 0.7;
                ElegantTextField txtOperadorPDV = new ElegantTextField(20);
                txtOperadorPDV.setEditable(false);
                txtOperadorPDV.setText(usuarioAtual);
                txtOperadorPDV.setToolTipText("Operador atual");
                dadosPanelPDV.add(txtOperadorPDV, gbcDadosPDV);
                
                // Terminal
                gbcDadosPDV.gridx = 2; gbcDadosPDV.gridy = 2;
                gbcDadosPDV.fill = GridBagConstraints.NONE;
                gbcDadosPDV.weightx = 0.3;
                JLabel lblTerminalPDV = new JLabel("Terminal:");
                lblTerminalPDV.setFont(new Font("Segoe UI", Font.BOLD, 12));
                lblTerminalPDV.setForeground(DARK_GRAY);
                dadosPanelPDV.add(lblTerminalPDV, gbcDadosPDV);
                gbcDadosPDV.gridx = 3; gbcDadosPDV.gridy = 2;
                gbcDadosPDV.fill = GridBagConstraints.HORIZONTAL;
                gbcDadosPDV.weightx = 0.7;
                JComboBox<String> cbTerminalPDV = new JComboBox<>(new String[]{
                    "Caixa Principal", "Balcão 01", "Balcão 02", "Delivery", 
                    "Self-Service", "Drive-Thru", "Quiosque 01", "Quiosque 02"
                });
                cbTerminalPDV.setFont(new Font("Segoe UI", Font.PLAIN, 12));
                cbTerminalPDV.setToolTipText("Selecione o terminal");
                dadosPanelPDV.add(cbTerminalPDV, gbcDadosPDV);
                
                // Status da Venda
                gbcDadosPDV.gridx = 0; gbcDadosPDV.gridy = 3;
                gbcDadosPDV.fill = GridBagConstraints.NONE;
                gbcDadosPDV.weightx = 0.3;
                JLabel lblStatusVendaPDV = new JLabel("Status:");
                lblStatusVendaPDV.setFont(new Font("Segoe UI", Font.BOLD, 12));
                lblStatusVendaPDV.setForeground(DARK_GRAY);
                dadosPanelPDV.add(lblStatusVendaPDV, gbcDadosPDV);
                gbcDadosPDV.gridx = 1; gbcDadosPDV.gridy = 3;
                gbcDadosPDV.fill = GridBagConstraints.HORIZONTAL;
                gbcDadosPDV.weightx = 0.7;
                JComboBox<String> cbStatusVendaPDV = new JComboBox<>(new String[]{
                    "Aberta", "Em Andamento", "Pausada", "Finalizada", "Cancelada"
                });
                cbStatusVendaPDV.setFont(new Font("Segoe UI", Font.PLAIN, 12));
                cbStatusVendaPDV.setToolTipText("Status da venda");
                dadosPanelPDV.add(cbStatusVendaPDV, gbcDadosPDV);
                
                // Tipo de Venda
                gbcDadosPDV.gridx = 2; gbcDadosPDV.gridy = 3;
                gbcDadosPDV.fill = GridBagConstraints.NONE;
                gbcDadosPDV.weightx = 0.3;
                JLabel lblTipoVendaPDV = new JLabel("Tipo:");
                lblTipoVendaPDV.setFont(new Font("Segoe UI", Font.BOLD, 12));
                lblTipoVendaPDV.setForeground(DARK_GRAY);
                dadosPanelPDV.add(lblTipoVendaPDV, gbcDadosPDV);
                gbcDadosPDV.gridx = 3; gbcDadosPDV.gridy = 3;
                gbcDadosPDV.fill = GridBagConstraints.HORIZONTAL;
                gbcDadosPDV.weightx = 0.7;
                JComboBox<String> cbTipoVendaPDV = new JComboBox<>(new String[]{
                    "Venda Direta", "Orçamento", "Delivery", "Balção", 
                    "Drive-Thru", "Self-Service", "Televendas"
                });
                cbTipoVendaPDV.setFont(new Font("Segoe UI", Font.PLAIN, 12));
                cbTipoVendaPDV.setToolTipText("Tipo de venda");
                dadosPanelPDV.add(cbTipoVendaPDV, gbcDadosPDV);
                
                panel.add(dadosPanelPDV, gbc);
                
                // Painel de Produtos
                gbc.gridx = 0; gbc.gridy = 1; gbc.gridwidth = 2;
                gbc.fill = GridBagConstraints.HORIZONTAL;
                gbc.insets = new Insets(5, 5, 15, 5);
                
                JPanel produtosPanelPDV = new JPanel(new GridBagLayout());
                produtosPanelPDV.setBorder(BorderFactory.createTitledBorder(
                    BorderFactory.createEtchedBorder(), "📦 Produtos do PDV",
                    TitledBorder.LEFT, TitledBorder.TOP, new Font("Segoe UI", Font.BOLD, 12),
                    DARK_GRAY));
                
                GridBagConstraints gbcProdutosPDV = new GridBagConstraints();
                gbcProdutosPDV.insets = new Insets(5, 5, 5, 5);
                
                // Código do Produto
                gbcProdutosPDV.gridx = 0; gbcProdutosPDV.gridy = 0;
                gbcProdutosPDV.fill = GridBagConstraints.NONE;
                gbcProdutosPDV.weightx = 0.3;
                JLabel lblCodigoProdutoPDV = new JLabel("Código:");
                lblCodigoProdutoPDV.setFont(new Font("Segoe UI", Font.BOLD, 12));
                lblCodigoProdutoPDV.setForeground(DARK_GRAY);
                produtosPanelPDV.add(lblCodigoProdutoPDV, gbcProdutosPDV);
                gbcProdutosPDV.gridx = 1; gbcProdutosPDV.gridy = 0;
                gbcProdutosPDV.fill = GridBagConstraints.HORIZONTAL;
                gbcProdutosPDV.weightx = 1.0;
                ElegantTextField txtCodigoProdutoPDV = new ElegantTextField(15);
                txtCodigoProdutoPDV.setToolTipText("Digite o código do produto");
                produtosPanelPDV.add(txtCodigoProdutoPDV, gbcProdutosPDV);
                
                // Quantidade
                gbcProdutosPDV.gridx = 2; gbcProdutosPDV.gridy = 0;
                gbcProdutosPDV.fill = GridBagConstraints.NONE;
                gbcProdutosPDV.weightx = 0.3;
                JLabel lblQuantidadePDV = new JLabel("Qtd:");
                lblQuantidadePDV.setFont(new Font("Segoe UI", Font.BOLD, 12));
                lblQuantidadePDV.setForeground(DARK_GRAY);
                produtosPanelPDV.add(lblQuantidadePDV, gbcProdutosPDV);
                gbcProdutosPDV.gridx = 3; gbcProdutosPDV.gridy = 0;
                gbcProdutosPDV.fill = GridBagConstraints.HORIZONTAL;
                gbcProdutosPDV.weightx = 0.7;
                ElegantTextField txtQuantidadePDV = new ElegantTextField(8);
                txtQuantidadePDV.setText("1");
                txtQuantidadePDV.setToolTipText("Quantidade do produto");
                produtosPanelPDV.add(txtQuantidadePDV, gbcProdutosPDV);
                
                // Botões
                gbcProdutosPDV.gridx = 4; gbcProdutosPDV.gridy = 0;
                gbcProdutosPDV.fill = GridBagConstraints.NONE;
                gbcProdutosPDV.weightx = 0;
                ElegantButton btnAdicionarProdutoPDV = new ElegantButton("➕ Adicionar", SUCCESS_COLOR, false);
                btnAdicionarProdutoPDV.setForeground(WHITE);
                btnAdicionarProdutoPDV.setToolTipText("Adicionar produto à venda");
                produtosPanelPDV.add(btnAdicionarProdutoPDV, gbcProdutosPDV);
                
                // Leitor de Código de Barras
                gbcProdutosPDV.gridx = 0; gbcProdutosPDV.gridy = 1;
                gbcProdutosPDV.fill = GridBagConstraints.NONE;
                gbcProdutosPDV.weightx = 0.3;
                JLabel lblLeitorBarrasPDV = new JLabel("Leitor:");
                lblLeitorBarrasPDV.setFont(new Font("Segoe UI", Font.BOLD, 12));
                lblLeitorBarrasPDV.setForeground(DARK_GRAY);
                produtosPanelPDV.add(lblLeitorBarrasPDV, gbcProdutosPDV);
                gbcProdutosPDV.gridx = 1; gbcProdutosPDV.gridy = 1;
                gbcProdutosPDV.fill = GridBagConstraints.HORIZONTAL;
                gbcProdutosPDV.weightx = 2.0;
                ElegantTextField txtLeitorBarrasPDV = new ElegantTextField(30);
                txtLeitorBarrasPDV.setToolTipText("Aguardando leitura do código de barras...");
                produtosPanelPDV.add(txtLeitorBarrasPDV, gbcProdutosPDV);
                gbcProdutosPDV.gridx = 2; gbcProdutosPDV.gridy = 1;
                gbcProdutosPDV.fill = GridBagConstraints.NONE;
                gbcProdutosPDV.weightx = 0;
                ElegantButton btnEscanearPDV = new ElegantButton("📷", WARNING_COLOR, false);
                btnEscanearPDV.setForeground(WHITE);
                btnEscanearPDV.setToolTipText("Escanear código de barras");
                produtosPanelPDV.add(btnEscanearPDV, gbcProdutosPDV);
                gbcProdutosPDV.gridx = 3; gbcProdutosPDV.gridy = 1;
                gbcProdutosPDV.fill = GridBagConstraints.NONE;
                gbcProdutosPDV.weightx = 0;
                ElegantButton btnPesquisarProdutoPDV = new ElegantButton("🔍", PRIMARY_COLOR, false);
                btnPesquisarProdutoPDV.setForeground(WHITE);
                btnPesquisarProdutoPDV.setToolTipText("Pesquisar produto");
                produtosPanelPDV.add(btnPesquisarProdutoPDV, gbcProdutosPDV);
                
                panel.add(produtosPanelPDV, gbc);
                
                // Painel de Totais
                gbc.gridx = 0; gbc.gridy = 2; gbc.gridwidth = 2;
                gbc.fill = GridBagConstraints.HORIZONTAL;
                gbc.insets = new Insets(5, 5, 15, 5);
                
                JPanel totaisPanelPDV = new JPanel(new GridBagLayout());
                totaisPanelPDV.setBorder(BorderFactory.createTitledBorder(
                    BorderFactory.createEtchedBorder(), "💰 Totais da Venda",
                    TitledBorder.LEFT, TitledBorder.TOP, new Font("Segoe UI", Font.BOLD, 12),
                    DARK_GRAY));
                
                GridBagConstraints gbcTotaisPDV = new GridBagConstraints();
                gbcTotaisPDV.insets = new Insets(5, 5, 5, 5);
                
                // Subtotal
                gbcTotaisPDV.gridx = 0; gbcTotaisPDV.gridy = 0;
                gbcTotaisPDV.fill = GridBagConstraints.NONE;
                gbcTotaisPDV.weightx = 0.3;
                JLabel lblSubtotalPDV = new JLabel("Subtotal:");
                lblSubtotalPDV.setFont(new Font("Segoe UI", Font.BOLD, 12));
                lblSubtotalPDV.setForeground(DARK_GRAY);
                totaisPanelPDV.add(lblSubtotalPDV, gbcTotaisPDV);
                gbcTotaisPDV.gridx = 1; gbcTotaisPDV.gridy = 0;
                gbcTotaisPDV.fill = GridBagConstraints.HORIZONTAL;
                gbcTotaisPDV.weightx = 0.7;
                ElegantTextField txtSubtotalPDV = new ElegantTextField(15);
                txtSubtotalPDV.setEditable(false);
                txtSubtotalPDV.setText("R$ 0,00");
                txtSubtotalPDV.setToolTipText("Valor subtotal dos produtos");
                totaisPanelPDV.add(txtSubtotalPDV, gbcTotaisPDV);
                
                // Desconto
                gbcTotaisPDV.gridx = 2; gbcTotaisPDV.gridy = 0;
                gbcTotaisPDV.fill = GridBagConstraints.NONE;
                gbcTotaisPDV.weightx = 0.3;
                JLabel lblDescontoPDV = new JLabel("Desconto:");
                lblDescontoPDV.setFont(new Font("Segoe UI", Font.BOLD, 12));
                lblDescontoPDV.setForeground(DARK_GRAY);
                totaisPanelPDV.add(lblDescontoPDV, gbcTotaisPDV);
                gbcTotaisPDV.gridx = 3; gbcTotaisPDV.gridy = 0;
                gbcTotaisPDV.fill = GridBagConstraints.HORIZONTAL;
                gbcTotaisPDV.weightx = 0.7;
                ElegantTextField txtDescontoPDV = new ElegantTextField(10);
                txtDescontoPDV.setText("0,00");
                txtDescontoPDV.setToolTipText("Valor do desconto");
                totaisPanelPDV.add(txtDescontoPDV, gbcTotaisPDV);
                
                // Taxa de Serviço
                gbcTotaisPDV.gridx = 0; gbcTotaisPDV.gridy = 1;
                gbcTotaisPDV.fill = GridBagConstraints.NONE;
                gbcTotaisPDV.weightx = 0.3;
                JLabel lblTaxaServico = new JLabel("Taxa Serviço:");
                lblTaxaServico.setFont(new Font("Segoe UI", Font.BOLD, 12));
                lblTaxaServico.setForeground(DARK_GRAY);
                totaisPanelPDV.add(lblTaxaServico, gbcTotaisPDV);
                gbcTotaisPDV.gridx = 1; gbcTotaisPDV.gridy = 1;
                gbcTotaisPDV.fill = GridBagConstraints.HORIZONTAL;
                gbcTotaisPDV.weightx = 0.7;
                ElegantTextField txtTaxaServico = new ElegantTextField(10);
                txtTaxaServico.setText("0,00");
                txtTaxaServico.setToolTipText("Taxa de serviço (10%)");
                totaisPanelPDV.add(txtTaxaServico, gbcTotaisPDV);
                
                // Total
                gbcTotaisPDV.gridx = 2; gbcTotaisPDV.gridy = 1;
                gbcTotaisPDV.fill = GridBagConstraints.NONE;
                gbcTotaisPDV.weightx = 0.3;
                JLabel lblTotalPDV = new JLabel("Total:");
                lblTotalPDV.setFont(new Font("Segoe UI", Font.BOLD, 14));
                lblTotalPDV.setForeground(new Color(0, 128, 0));
                totaisPanelPDV.add(lblTotalPDV, gbcTotaisPDV);
                gbcTotaisPDV.gridx = 3; gbcTotaisPDV.gridy = 1;
                gbcTotaisPDV.fill = GridBagConstraints.HORIZONTAL;
                gbcTotaisPDV.weightx = 0.7;
                ElegantTextField txtTotalPDV = new ElegantTextField(15);
                txtTotalPDV.setEditable(false);
                txtTotalPDV.setText("R$ 0,00");
                txtTotalPDV.setFont(new Font("Segoe UI", Font.BOLD, 14));
                txtTotalPDV.setForeground(new Color(0, 128, 0));
                txtTotalPDV.setToolTipText("Valor total da venda");
                totaisPanelPDV.add(txtTotalPDV, gbcTotaisPDV);
                
                panel.add(totaisPanelPDV, gbc);
                
                // Tabela de Vendas
                gbc.gridx = 0; gbc.gridy = 3; gbc.gridwidth = 2;
                gbc.fill = GridBagConstraints.BOTH;
                gbc.weightx = 1.0; gbc.weighty = 1.0;
                
                String[] colunasTabelaPDV = {"ID", "Data", "Cliente", "Operador", "Terminal", "Valor Total", "Status", "Tipo", "Ações"};
                Object[][] dadosTabelaPDV = {};
                
                JTable tabelaPDV = new JTable(dadosTabelaPDV, colunasTabelaPDV);
                tabelaPDV.setFont(new Font("Segoe UI", Font.PLAIN, 11));
                tabelaPDV.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 12));
                tabelaPDV.getTableHeader().setBackground(new Color(70, 130, 180));
                tabelaPDV.getTableHeader().setForeground(WHITE);
                tabelaPDV.setRowHeight(25);
                tabelaPDV.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
                tabelaPDV.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
                
                JScrollPane scrollPaneTabelaPDV = new JScrollPane(tabelaPDV);
                scrollPaneTabelaPDV.setPreferredSize(new Dimension(900, 250));
                scrollPaneTabelaPDV.setBorder(BorderFactory.createTitledBorder(
                    BorderFactory.createEtchedBorder(), "🛒 Histórico de Vendas",
                    TitledBorder.LEFT, TitledBorder.TOP, new Font("Segoe UI", Font.BOLD, 12),
                    DARK_GRAY));
                panel.add(scrollPaneTabelaPDV, gbc);
                
                // Painel de Ações
                gbc.gridx = 0; gbc.gridy = 4; gbc.gridwidth = 2;
                gbc.fill = GridBagConstraints.HORIZONTAL;
                gbc.weightx = 1.0; gbc.weighty = 0;
                gbc.insets = new Insets(10, 5, 5, 5);
                
                JPanel acoesPanelPDV = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 5));
                acoesPanelPDV.setOpaque(false);
                
                ElegantButton btnNovaVendaPDV = new ElegantButton("🛒 Nova Venda", SUCCESS_COLOR, false);
                btnNovaVendaPDV.setForeground(WHITE);
                btnNovaVendaPDV.setFont(new Font("Segoe UI", Font.BOLD, 14));
                btnNovaVendaPDV.setPreferredSize(new Dimension(140, 40));
                acoesPanelPDV.add(btnNovaVendaPDV);
                
                ElegantButton btnFinalizarVendaPDV = new ElegantButton("✅ Finalizar", new Color(0, 150, 0), false);
                btnFinalizarVendaPDV.setForeground(WHITE);
                btnFinalizarVendaPDV.setFont(new Font("Segoe UI", Font.BOLD, 14));
                btnFinalizarVendaPDV.setPreferredSize(new Dimension(120, 40));
                acoesPanelPDV.add(btnFinalizarVendaPDV);
                
                ElegantButton btnPausarVendaPDV = new ElegantButton("⏸️ Pausar", WARNING_COLOR, false);
                btnPausarVendaPDV.setForeground(WHITE);
                btnPausarVendaPDV.setFont(new Font("Segoe UI", Font.BOLD, 14));
                btnPausarVendaPDV.setPreferredSize(new Dimension(120, 40));
                acoesPanelPDV.add(btnPausarVendaPDV);
                
                ElegantButton btnCancelarVendaPDV = new ElegantButton("❌ Cancelar", new Color(200, 50, 50), false);
                btnCancelarVendaPDV.setForeground(WHITE);
                btnCancelarVendaPDV.setFont(new Font("Segoe UI", Font.BOLD, 14));
                btnCancelarVendaPDV.setPreferredSize(new Dimension(120, 40));
                acoesPanelPDV.add(btnCancelarVendaPDV);
                
                ElegantButton btnImprimirCupomPDV = new ElegantButton("🖨️ Cupom", new Color(70, 130, 180), false);
                btnImprimirCupomPDV.setForeground(WHITE);
                btnImprimirCupomPDV.setFont(new Font("Segoe UI", Font.BOLD, 14));
                btnImprimirCupomPDV.setPreferredSize(new Dimension(120, 40));
                acoesPanelPDV.add(btnImprimirCupomPDV);
                
                panel.add(acoesPanelPDV, gbc);
                break;
                
            case "💳 Pagamentos":
                // Painel de Dados do Pagamento
                gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
                gbc.fill = GridBagConstraints.HORIZONTAL;
                gbc.insets = new Insets(5, 5, 15, 5);
                
                JPanel dadosPanelPagamento = new JPanel(new GridBagLayout());
                dadosPanelPagamento.setBorder(BorderFactory.createTitledBorder(
                    BorderFactory.createEtchedBorder(), "💳 Dados do Pagamento",
                    TitledBorder.LEFT, TitledBorder.TOP, new Font("Segoe UI", Font.BOLD, 12),
                    DARK_GRAY));
                
                GridBagConstraints gbcDadosPagamento = new GridBagConstraints();
                gbcDadosPagamento.insets = new Insets(5, 5, 5, 5);
                
                // ID do Pagamento
                gbcDadosPagamento.gridx = 0; gbcDadosPagamento.gridy = 0;
                gbcDadosPagamento.fill = GridBagConstraints.NONE;
                gbcDadosPagamento.weightx = 0.3;
                JLabel lblIdPagamento = new JLabel("ID Pagamento:");
                lblIdPagamento.setFont(new Font("Segoe UI", Font.BOLD, 12));
                lblIdPagamento.setForeground(DARK_GRAY);
                dadosPanelPagamento.add(lblIdPagamento, gbcDadosPagamento);
                gbcDadosPagamento.gridx = 1; gbcDadosPagamento.gridy = 0;
                gbcDadosPagamento.fill = GridBagConstraints.HORIZONTAL;
                gbcDadosPagamento.weightx = 0.7;
                ElegantTextField txtIdPagamento = new ElegantTextField(15);
                txtIdPagamento.setEditable(false);
                txtIdPagamento.setText("AUTO");
                txtIdPagamento.setToolTipText("ID automático do pagamento");
                dadosPanelPagamento.add(txtIdPagamento, gbcDadosPagamento);
                
                // ID da Venda
                gbcDadosPagamento.gridx = 2; gbcDadosPagamento.gridy = 0;
                gbcDadosPagamento.fill = GridBagConstraints.NONE;
                gbcDadosPagamento.weightx = 0.3;
                JLabel lblIdVenda = new JLabel("ID Venda:");
                lblIdVenda.setFont(new Font("Segoe UI", Font.BOLD, 12));
                lblIdVenda.setForeground(DARK_GRAY);
                dadosPanelPagamento.add(lblIdVenda, gbcDadosPagamento);
                gbcDadosPagamento.gridx = 3; gbcDadosPagamento.gridy = 0;
                gbcDadosPagamento.fill = GridBagConstraints.HORIZONTAL;
                gbcDadosPagamento.weightx = 0.7;
                ElegantTextField txtIdVenda = new ElegantTextField(15);
                txtIdVenda.setToolTipText("ID da venda associada");
                dadosPanelPagamento.add(txtIdVenda, gbcDadosPagamento);
                
                // Cliente
                gbcDadosPagamento.gridx = 0; gbcDadosPagamento.gridy = 1;
                gbcDadosPagamento.fill = GridBagConstraints.NONE;
                gbcDadosPagamento.weightx = 0.3;
                JLabel lblClientePagamento = new JLabel("Cliente:");
                lblClientePagamento.setFont(new Font("Segoe UI", Font.BOLD, 12));
                lblClientePagamento.setForeground(DARK_GRAY);
                dadosPanelPagamento.add(lblClientePagamento, gbcDadosPagamento);
                gbcDadosPagamento.gridx = 1; gbcDadosPagamento.gridy = 1;
                gbcDadosPagamento.fill = GridBagConstraints.HORIZONTAL;
                gbcDadosPagamento.weightx = 1.7;
                ElegantTextField txtClientePagamento = new ElegantTextField(40);
                txtClientePagamento.setToolTipText("Nome do cliente");
                dadosPanelPagamento.add(txtClientePagamento, gbcDadosPagamento);
                gbcDadosPagamento.gridx = 2; gbcDadosPagamento.gridy = 1;
                gbcDadosPagamento.fill = GridBagConstraints.NONE;
                gbcDadosPagamento.weightx = 0.3;
                ElegantButton btnBuscarClientePagamento = new ElegantButton("🔍", PRIMARY_COLOR, false);
                btnBuscarClientePagamento.setForeground(WHITE);
                btnBuscarClientePagamento.setToolTipText("Buscar cliente");
                dadosPanelPagamento.add(btnBuscarClientePagamento, gbcDadosPagamento);
                gbcDadosPagamento.gridx = 3; gbcDadosPagamento.gridy = 1;
                gbcDadosPagamento.fill = GridBagConstraints.NONE;
                gbcDadosPagamento.weightx = 0.7;
                ElegantButton btnNovaVenda = new ElegantButton("🛒", SUCCESS_COLOR, false);
                btnNovaVenda.setForeground(WHITE);
                btnNovaVenda.setToolTipText("Nova venda");
                dadosPanelPagamento.add(btnNovaVenda, gbcDadosPagamento);
                
                // Data do Pagamento
                gbcDadosPagamento.gridx = 0; gbcDadosPagamento.gridy = 2;
                gbcDadosPagamento.fill = GridBagConstraints.NONE;
                gbcDadosPagamento.weightx = 0.3;
                JLabel lblDataPagamento = new JLabel("Data Pagamento:");
                lblDataPagamento.setFont(new Font("Segoe UI", Font.BOLD, 12));
                lblDataPagamento.setForeground(DARK_GRAY);
                dadosPanelPagamento.add(lblDataPagamento, gbcDadosPagamento);
                gbcDadosPagamento.gridx = 1; gbcDadosPagamento.gridy = 2;
                gbcDadosPagamento.fill = GridBagConstraints.HORIZONTAL;
                gbcDadosPagamento.weightx = 0.7;
                ElegantTextField txtDataPagamento = new ElegantTextField(12);
                txtDataPagamento.setEditable(false);
                txtDataPagamento.setToolTipText("Data do pagamento");
                dadosPanelPagamento.add(txtDataPagamento, gbcDadosPagamento);
                
                // Hora do Pagamento
                gbcDadosPagamento.gridx = 2; gbcDadosPagamento.gridy = 2;
                gbcDadosPagamento.fill = GridBagConstraints.NONE;
                gbcDadosPagamento.weightx = 0.3;
                JLabel lblHoraPagamento = new JLabel("Hora:");
                lblHoraPagamento.setFont(new Font("Segoe UI", Font.BOLD, 12));
                lblHoraPagamento.setForeground(DARK_GRAY);
                dadosPanelPagamento.add(lblHoraPagamento, gbcDadosPagamento);
                gbcDadosPagamento.gridx = 3; gbcDadosPagamento.gridy = 2;
                gbcDadosPagamento.fill = GridBagConstraints.HORIZONTAL;
                gbcDadosPagamento.weightx = 0.7;
                ElegantTextField txtHoraPagamento = new ElegantTextField(10);
                txtHoraPagamento.setEditable(false);
                txtHoraPagamento.setToolTipText("Hora do pagamento");
                dadosPanelPagamento.add(txtHoraPagamento, gbcDadosPagamento);
                
                // Operador
                gbcDadosPagamento.gridx = 0; gbcDadosPagamento.gridy = 3;
                gbcDadosPagamento.fill = GridBagConstraints.NONE;
                gbcDadosPagamento.weightx = 0.3;
                JLabel lblOperadorPagamento = new JLabel("Operador:");
                lblOperadorPagamento.setFont(new Font("Segoe UI", Font.BOLD, 12));
                lblOperadorPagamento.setForeground(DARK_GRAY);
                dadosPanelPagamento.add(lblOperadorPagamento, gbcDadosPagamento);
                gbcDadosPagamento.gridx = 1; gbcDadosPagamento.gridy = 3;
                gbcDadosPagamento.fill = GridBagConstraints.HORIZONTAL;
                gbcDadosPagamento.weightx = 0.7;
                ElegantTextField txtOperadorPagamento = new ElegantTextField(20);
                txtOperadorPagamento.setEditable(false);
                txtOperadorPagamento.setText(usuarioAtual);
                txtOperadorPagamento.setToolTipText("Operador atual");
                dadosPanelPagamento.add(txtOperadorPagamento, gbcDadosPagamento);
                
                // Status do Pagamento
                gbcDadosPagamento.gridx = 2; gbcDadosPagamento.gridy = 3;
                gbcDadosPagamento.fill = GridBagConstraints.NONE;
                gbcDadosPagamento.weightx = 0.3;
                JLabel lblStatusPagamento = new JLabel("Status:");
                lblStatusPagamento.setFont(new Font("Segoe UI", Font.BOLD, 12));
                lblStatusPagamento.setForeground(DARK_GRAY);
                dadosPanelPagamento.add(lblStatusPagamento, gbcDadosPagamento);
                gbcDadosPagamento.gridx = 3; gbcDadosPagamento.gridy = 3;
                gbcDadosPagamento.fill = GridBagConstraints.HORIZONTAL;
                gbcDadosPagamento.weightx = 0.7;
                JComboBox<String> cbStatusPagamento = new JComboBox<>(new String[]{
                    "Pendente", "Processando", "Aprovado", "Rejeitado", "Cancelado", "Estornado"
                });
                cbStatusPagamento.setFont(new Font("Segoe UI", Font.PLAIN, 12));
                cbStatusPagamento.setToolTipText("Status do pagamento");
                dadosPanelPagamento.add(cbStatusPagamento, gbcDadosPagamento);
                
                panel.add(dadosPanelPagamento, gbc);
                
                // Painel de Valores
                gbc.gridx = 0; gbc.gridy = 1; gbc.gridwidth = 2;
                gbc.fill = GridBagConstraints.HORIZONTAL;
                gbc.insets = new Insets(5, 5, 15, 5);
                
                JPanel valoresPanel = new JPanel(new GridBagLayout());
                valoresPanel.setBorder(BorderFactory.createTitledBorder(
                    BorderFactory.createEtchedBorder(), "💰 Valores do Pagamento",
                    TitledBorder.LEFT, TitledBorder.TOP, new Font("Segoe UI", Font.BOLD, 12),
                    DARK_GRAY));
                
                GridBagConstraints gbcValores = new GridBagConstraints();
                gbcValores.insets = new Insets(5, 5, 5, 5);
                
                // Valor Total da Venda
                gbcValores.gridx = 0; gbcValores.gridy = 0;
                gbcValores.fill = GridBagConstraints.NONE;
                gbcValores.weightx = 0.3;
                JLabel lblValorTotal = new JLabel("Valor Total:");
                lblValorTotal.setFont(new Font("Segoe UI", Font.BOLD, 12));
                lblValorTotal.setForeground(DARK_GRAY);
                valoresPanel.add(lblValorTotal, gbcValores);
                gbcValores.gridx = 1; gbcValores.gridy = 0;
                gbcValores.fill = GridBagConstraints.HORIZONTAL;
                gbcValores.weightx = 0.7;
                ElegantTextField txtValorTotal = new ElegantTextField(15);
                txtValorTotal.setEditable(false);
                txtValorTotal.setText("R$ 0,00");
                txtValorTotal.setToolTipText("Valor total da venda");
                valoresPanel.add(txtValorTotal, gbcValores);
                
                // Valor Pago
                gbcValores.gridx = 2; gbcValores.gridy = 0;
                gbcValores.fill = GridBagConstraints.NONE;
                gbcValores.weightx = 0.3;
                JLabel lblValorPago = new JLabel("Valor Pago:");
                lblValorPago.setFont(new Font("Segoe UI", Font.BOLD, 12));
                lblValorPago.setForeground(DARK_GRAY);
                valoresPanel.add(lblValorPago, gbcValores);
                gbcValores.gridx = 3; gbcValores.gridy = 0;
                gbcValores.fill = GridBagConstraints.HORIZONTAL;
                gbcValores.weightx = 0.7;
                ElegantTextField txtValorPago = new ElegantTextField(15);
                txtValorPago.setText("R$ 0,00");
                txtValorPago.setToolTipText("Valor pago pelo cliente");
                valoresPanel.add(txtValorPago, gbcValores);
                
                // Desconto
                gbcValores.gridx = 0; gbcValores.gridy = 1;
                gbcValores.fill = GridBagConstraints.NONE;
                gbcValores.weightx = 0.3;
                JLabel lblDescontoPagamento = new JLabel("Desconto:");
                lblDescontoPagamento.setFont(new Font("Segoe UI", Font.BOLD, 12));
                lblDescontoPagamento.setForeground(DARK_GRAY);
                valoresPanel.add(lblDescontoPagamento, gbcValores);
                gbcValores.gridx = 1; gbcValores.gridy = 1;
                gbcValores.fill = GridBagConstraints.HORIZONTAL;
                gbcValores.weightx = 0.7;
                ElegantTextField txtDescontoPagamento = new ElegantTextField(10);
                txtDescontoPagamento.setText("0,00");
                txtDescontoPagamento.setToolTipText("Valor do desconto");
                valoresPanel.add(txtDescontoPagamento, gbcValores);
                
                // Taxa
                gbcValores.gridx = 2; gbcValores.gridy = 1;
                gbcValores.fill = GridBagConstraints.NONE;
                gbcValores.weightx = 0.3;
                JLabel lblTaxa = new JLabel("Taxa:");
                lblTaxa.setFont(new Font("Segoe UI", Font.BOLD, 12));
                lblTaxa.setForeground(DARK_GRAY);
                valoresPanel.add(lblTaxa, gbcValores);
                gbcValores.gridx = 3; gbcValores.gridy = 1;
                gbcValores.fill = GridBagConstraints.HORIZONTAL;
                gbcValores.weightx = 0.7;
                ElegantTextField txtTaxa = new ElegantTextField(10);
                txtTaxa.setText("0,00");
                txtTaxa.setToolTipText("Taxa da operadora");
                valoresPanel.add(txtTaxa, gbcValores);
                
                // Troco
                gbcValores.gridx = 0; gbcValores.gridy = 2;
                gbcValores.fill = GridBagConstraints.NONE;
                gbcValores.weightx = 0.3;
                JLabel lblTrocoPagamento = new JLabel("Troco:");
                lblTrocoPagamento.setFont(new Font("Segoe UI", Font.BOLD, 12));
                lblTrocoPagamento.setForeground(DARK_GRAY);
                valoresPanel.add(lblTrocoPagamento, gbcValores);
                gbcValores.gridx = 1; gbcValores.gridy = 2;
                gbcValores.fill = GridBagConstraints.HORIZONTAL;
                gbcValores.weightx = 0.7;
                ElegantTextField txtTrocoPagamento = new ElegantTextField(15);
                txtTrocoPagamento.setEditable(false);
                txtTrocoPagamento.setText("R$ 0,00");
                txtTrocoPagamento.setToolTipText("Valor de troco");
                valoresPanel.add(txtTrocoPagamento, gbcValores);
                
                // Saldo Devedor
                gbcValores.gridx = 2; gbcValores.gridy = 2;
                gbcValores.fill = GridBagConstraints.NONE;
                gbcValores.weightx = 0.3;
                JLabel lblSaldoDevedor = new JLabel("Saldo Devedor:");
                lblSaldoDevedor.setFont(new Font("Segoe UI", Font.BOLD, 12));
                lblSaldoDevedor.setForeground(DARK_GRAY);
                valoresPanel.add(lblSaldoDevedor, gbcValores);
                gbcValores.gridx = 3; gbcValores.gridy = 2;
                gbcValores.fill = GridBagConstraints.HORIZONTAL;
                gbcValores.weightx = 0.7;
                ElegantTextField txtSaldoDevedor = new ElegantTextField(15);
                txtSaldoDevedor.setEditable(false);
                txtSaldoDevedor.setText("R$ 0,00");
                txtSaldoDevedor.setToolTipText("Valor restante a pagar");
                valoresPanel.add(txtSaldoDevedor, gbcValores);
                
                panel.add(valoresPanel, gbc);
                
                // Painel de Formas de Pagamento
                gbc.gridx = 0; gbc.gridy = 2; gbc.gridwidth = 2;
                gbc.fill = GridBagConstraints.HORIZONTAL;
                gbc.insets = new Insets(5, 5, 15, 5);
                
                JPanel formasPanel = new JPanel(new GridBagLayout());
                formasPanel.setBorder(BorderFactory.createTitledBorder(
                    BorderFactory.createEtchedBorder(), "💳 Formas de Pagamento",
                    TitledBorder.LEFT, TitledBorder.TOP, new Font("Segoe UI", Font.BOLD, 12),
                    DARK_GRAY));
                
                GridBagConstraints gbcFormas = new GridBagConstraints();
                gbcFormas.insets = new Insets(5, 5, 5, 5);
                
                // Forma de Pagamento Principal
                gbcFormas.gridx = 0; gbcFormas.gridy = 0;
                gbcFormas.fill = GridBagConstraints.NONE;
                gbcFormas.weightx = 0.3;
                JLabel lblFormaPagamentoPrincipal = new JLabel("Forma Principal:");
                lblFormaPagamentoPrincipal.setFont(new Font("Segoe UI", Font.BOLD, 12));
                lblFormaPagamentoPrincipal.setForeground(DARK_GRAY);
                formasPanel.add(lblFormaPagamentoPrincipal, gbcFormas);
                gbcFormas.gridx = 1; gbcFormas.gridy = 0;
                gbcFormas.fill = GridBagConstraints.HORIZONTAL;
                gbcFormas.weightx = 1.7;
                JComboBox<String> cbFormaPagamentoPrincipal = new JComboBox<>(new String[]{
                    "Dinheiro", "Cartão de Débito", "Cartão de Crédito", 
                    "PIX", "Vale Alimentação", "Vale Refeição", "Crediário", 
                    "Boleto", "Transferência Bancária", "Cheque"
                });
                cbFormaPagamentoPrincipal.setFont(new Font("Segoe UI", Font.PLAIN, 12));
                cbFormaPagamentoPrincipal.setToolTipText("Selecione a forma de pagamento principal");
                formasPanel.add(cbFormaPagamentoPrincipal, gbcFormas);
                
                // Número de Parcelas
                gbcFormas.gridx = 2; gbcFormas.gridy = 0;
                gbcFormas.fill = GridBagConstraints.NONE;
                gbcFormas.weightx = 0.3;
                JLabel lblParcelas = new JLabel("Parcelas:");
                lblParcelas.setFont(new Font("Segoe UI", Font.BOLD, 12));
                lblParcelas.setForeground(DARK_GRAY);
                formasPanel.add(lblParcelas, gbcFormas);
                gbcFormas.gridx = 3; gbcFormas.gridy = 0;
                gbcFormas.fill = GridBagConstraints.HORIZONTAL;
                gbcFormas.weightx = 0.7;
                JComboBox<String> cbParcelas = new JComboBox<>(new String[]{
                    "1x", "2x", "3x", "4x", "5x", "6x", "7x", "8x", "9x", "10x", "11x", "12x"
                });
                cbParcelas.setFont(new Font("Segoe UI", Font.PLAIN, 12));
                cbParcelas.setToolTipText("Número de parcelas");
                formasPanel.add(cbParcelas, gbcFormas);
                
                // Bandeira do Cartão
                gbcFormas.gridx = 0; gbcFormas.gridy = 1;
                gbcFormas.fill = GridBagConstraints.NONE;
                gbcFormas.weightx = 0.3;
                JLabel lblBandeira = new JLabel("Bandeira:");
                lblBandeira.setFont(new Font("Segoe UI", Font.BOLD, 12));
                lblBandeira.setForeground(DARK_GRAY);
                formasPanel.add(lblBandeira, gbcFormas);
                gbcFormas.gridx = 1; gbcFormas.gridy = 1;
                gbcFormas.fill = GridBagConstraints.HORIZONTAL;
                gbcFormas.weightx = 1.0;
                JComboBox<String> cbBandeira = new JComboBox<>(new String[]{
                    "Visa", "Mastercard", "Elo", "American Express", "Hipercard", 
                    "Aura", "Cabal", "Mais", "Good Card", "Sorocred"
                });
                cbBandeira.setFont(new Font("Segoe UI", Font.PLAIN, 12));
                cbBandeira.setToolTipText("Bandeira do cartão");
                formasPanel.add(cbBandeira, gbcFormas);
                
                // Autorização
                gbcFormas.gridx = 2; gbcFormas.gridy = 1;
                gbcFormas.fill = GridBagConstraints.NONE;
                gbcFormas.weightx = 0.3;
                JLabel lblAutorizacao = new JLabel("Autorização:");
                lblAutorizacao.setFont(new Font("Segoe UI", Font.BOLD, 12));
                lblAutorizacao.setForeground(DARK_GRAY);
                formasPanel.add(lblAutorizacao, gbcFormas);
                gbcFormas.gridx = 3; gbcFormas.gridy = 1;
                gbcFormas.fill = GridBagConstraints.HORIZONTAL;
                gbcFormas.weightx = 0.7;
                ElegantTextField txtAutorizacao = new ElegantTextField(15);
                txtAutorizacao.setToolTipText("Código de autorização");
                formasPanel.add(txtAutorizacao, gbcFormas);
                
                panel.add(formasPanel, gbc);
                
                // Tabela de Pagamentos
                gbc.gridx = 0; gbc.gridy = 3; gbc.gridwidth = 2;
                gbc.fill = GridBagConstraints.BOTH;
                gbc.weightx = 1.0; gbc.weighty = 1.0;
                
                String[] colunasTabelaPagamento = {"ID", "Venda ID", "Cliente", "Valor Total", "Valor Pago", "Forma", "Data", "Status", "Ações"};
                Object[][] dadosTabelaPagamento = {};
                
                JTable tabelaPagamento = new JTable(dadosTabelaPagamento, colunasTabelaPagamento);
                tabelaPagamento.setFont(new Font("Segoe UI", Font.PLAIN, 11));
                tabelaPagamento.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 12));
                tabelaPagamento.getTableHeader().setBackground(new Color(70, 130, 180));
                tabelaPagamento.getTableHeader().setForeground(WHITE);
                tabelaPagamento.setRowHeight(25);
                tabelaPagamento.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
                tabelaPagamento.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
                
                JScrollPane scrollPaneTabelaPagamento = new JScrollPane(tabelaPagamento);
                scrollPaneTabelaPagamento.setPreferredSize(new Dimension(900, 250));
                scrollPaneTabelaPagamento.setBorder(BorderFactory.createTitledBorder(
                    BorderFactory.createEtchedBorder(), "💳 Histórico de Pagamentos",
                    TitledBorder.LEFT, TitledBorder.TOP, new Font("Segoe UI", Font.BOLD, 12),
                    DARK_GRAY));
                panel.add(scrollPaneTabelaPagamento, gbc);
                
                // Painel de Ações
                gbc.gridx = 0; gbc.gridy = 4; gbc.gridwidth = 2;
                gbc.fill = GridBagConstraints.HORIZONTAL;
                gbc.weightx = 1.0; gbc.weighty = 0;
                gbc.insets = new Insets(10, 5, 5, 5);
                
                JPanel acoesPanelPagamento = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 5));
                acoesPanelPagamento.setOpaque(false);
                
                ElegantButton btnProcessarPagamento = new ElegantButton("💳 Processar", new Color(0, 150, 0), false);
                btnProcessarPagamento.setForeground(WHITE);
                btnProcessarPagamento.setFont(new Font("Segoe UI", Font.BOLD, 14));
                btnProcessarPagamento.setPreferredSize(new Dimension(120, 40));
                acoesPanelPagamento.add(btnProcessarPagamento);
                
                ElegantButton btnConfirmarPagamento = new ElegantButton("✅ Confirmar", SUCCESS_COLOR, false);
                btnConfirmarPagamento.setForeground(WHITE);
                btnConfirmarPagamento.setFont(new Font("Segoe UI", Font.BOLD, 14));
                btnConfirmarPagamento.setPreferredSize(new Dimension(120, 40));
                acoesPanelPagamento.add(btnConfirmarPagamento);
                
                ElegantButton btnCancelarPagamento = new ElegantButton("❌ Cancelar", new Color(200, 50, 50), false);
                btnCancelarPagamento.setForeground(WHITE);
                btnCancelarPagamento.setFont(new Font("Segoe UI", Font.BOLD, 14));
                btnCancelarPagamento.setPreferredSize(new Dimension(120, 40));
                acoesPanelPagamento.add(btnCancelarPagamento);
                
                ElegantButton btnEstornar = new ElegantButton("↩️ Estornar", WARNING_COLOR, false);
                btnEstornar.setForeground(WHITE);
                btnEstornar.setFont(new Font("Segoe UI", Font.BOLD, 14));
                btnEstornar.setPreferredSize(new Dimension(120, 40));
                acoesPanelPagamento.add(btnEstornar);
                
                ElegantButton btnImprimirComprovante = new ElegantButton("🖨️ Comprovante", new Color(70, 130, 180), false);
                btnImprimirComprovante.setForeground(WHITE);
                btnImprimirComprovante.setFont(new Font("Segoe UI", Font.BOLD, 14));
                btnImprimirComprovante.setPreferredSize(new Dimension(140, 40));
                acoesPanelPagamento.add(btnImprimirComprovante);
                
                panel.add(acoesPanelPagamento, gbc);
                break;
                
            case "🧾 Cupom Fiscal":
                // Painel de Dados do Cupom Fiscal
                gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
                gbc.fill = GridBagConstraints.HORIZONTAL;
                gbc.insets = new Insets(5, 5, 15, 5);
                
                JPanel dadosPanelCupom = new JPanel(new GridBagLayout());
                dadosPanelCupom.setBorder(BorderFactory.createTitledBorder(
                    BorderFactory.createEtchedBorder(), "🧾 Dados do Cupom Fiscal",
                    TitledBorder.LEFT, TitledBorder.TOP, new Font("Segoe UI", Font.BOLD, 12),
                    DARK_GRAY));
                
                GridBagConstraints gbcDadosCupom = new GridBagConstraints();
                gbcDadosCupom.insets = new Insets(5, 5, 5, 5);
                
                // Número do Cupom
                gbcDadosCupom.gridx = 0; gbcDadosCupom.gridy = 0;
                gbcDadosCupom.fill = GridBagConstraints.NONE;
                gbcDadosCupom.weightx = 0.3;
                JLabel lblNumeroCupom = new JLabel("Nº Cupom:");
                lblNumeroCupom.setFont(new Font("Segoe UI", Font.BOLD, 12));
                lblNumeroCupom.setForeground(DARK_GRAY);
                dadosPanelCupom.add(lblNumeroCupom, gbcDadosCupom);
                gbcDadosCupom.gridx = 1; gbcDadosCupom.gridy = 0;
                gbcDadosCupom.fill = GridBagConstraints.HORIZONTAL;
                gbcDadosCupom.weightx = 0.7;
                ElegantTextField txtNumeroCupom = new ElegantTextField(15);
                txtNumeroCupom.setEditable(false);
                txtNumeroCupom.setText("AUTO");
                txtNumeroCupom.setToolTipText("Número automático do cupom");
                dadosPanelCupom.add(txtNumeroCupom, gbcDadosCupom);
                
                // Data e Hora Emissão
                gbcDadosCupom.gridx = 2; gbcDadosCupom.gridy = 0;
                gbcDadosCupom.fill = GridBagConstraints.NONE;
                gbcDadosCupom.weightx = 0.3;
                JLabel lblDataEmissao = new JLabel("Data Emissão:");
                lblDataEmissao.setFont(new Font("Segoe UI", Font.BOLD, 12));
                lblDataEmissao.setForeground(DARK_GRAY);
                dadosPanelCupom.add(lblDataEmissao, gbcDadosCupom);
                gbcDadosCupom.gridx = 3; gbcDadosCupom.gridy = 0;
                gbcDadosCupom.fill = GridBagConstraints.HORIZONTAL;
                gbcDadosCupom.weightx = 0.7;
                ElegantTextField txtDataEmissao = new ElegantTextField(16);
                txtDataEmissao.setEditable(false);
                txtDataEmissao.setToolTipText("Data e hora de emissão");
                dadosPanelCupom.add(txtDataEmissao, gbcDadosCupom);
                
                // CPF/CNPJ Cliente
                gbcDadosCupom.gridx = 0; gbcDadosCupom.gridy = 1;
                gbcDadosCupom.fill = GridBagConstraints.NONE;
                gbcDadosCupom.weightx = 0.3;
                JLabel lblCpfCnpj = new JLabel("CPF/CNPJ:");
                lblCpfCnpj.setFont(new Font("Segoe UI", Font.BOLD, 12));
                lblCpfCnpj.setForeground(DARK_GRAY);
                dadosPanelCupom.add(lblCpfCnpj, gbcDadosCupom);
                gbcDadosCupom.gridx = 1; gbcDadosCupom.gridy = 1;
                gbcDadosCupom.fill = GridBagConstraints.HORIZONTAL;
                gbcDadosCupom.weightx = 1.7;
                ElegantTextField txtCpfCnpj = new ElegantTextField(20);
                txtCpfCnpj.setToolTipText("CPF ou CNPJ do cliente");
                dadosPanelCupom.add(txtCpfCnpj, gbcDadosCupom);
                gbcDadosCupom.gridx = 2; gbcDadosCupom.gridy = 1;
                gbcDadosCupom.fill = GridBagConstraints.NONE;
                gbcDadosCupom.weightx = 0.3;
                ElegantButton btnValidarCpf = new ElegantButton("✓", PRIMARY_COLOR, false);
                btnValidarCpf.setForeground(WHITE);
                btnValidarCpf.setToolTipText("Validar CPF/CNPJ");
                dadosPanelCupom.add(btnValidarCpf, gbcDadosCupom);
                gbcDadosCupom.gridx = 3; gbcDadosCupom.gridy = 1;
                gbcDadosCupom.fill = GridBagConstraints.NONE;
                gbcDadosCupom.weightx = 0.7;
                ElegantButton btnBuscarClienteCupom = new ElegantButton("🔍", SUCCESS_COLOR, false);
                btnBuscarClienteCupom.setForeground(WHITE);
                btnBuscarClienteCupom.setToolTipText("Buscar cliente");
                dadosPanelCupom.add(btnBuscarClienteCupom, gbcDadosCupom);
                
                // Nome Cliente
                gbcDadosCupom.gridx = 0; gbcDadosCupom.gridy = 2;
                gbcDadosCupom.fill = GridBagConstraints.NONE;
                gbcDadosCupom.weightx = 0.3;
                JLabel lblNomeClienteCupom = new JLabel("Nome Cliente:");
                lblNomeClienteCupom.setFont(new Font("Segoe UI", Font.BOLD, 12));
                lblNomeClienteCupom.setForeground(DARK_GRAY);
                dadosPanelCupom.add(lblNomeClienteCupom, gbcDadosCupom);
                gbcDadosCupom.gridx = 1; gbcDadosCupom.gridy = 2;
                gbcDadosCupom.fill = GridBagConstraints.HORIZONTAL;
                gbcDadosCupom.weightx = 2.7;
                ElegantTextField txtNomeClienteCupom = new ElegantTextField(40);
                txtNomeClienteCupom.setToolTipText("Nome completo do cliente");
                dadosPanelCupom.add(txtNomeClienteCupom, gbcDadosCupom);
                
                // Operador
                gbcDadosCupom.gridx = 0; gbcDadosCupom.gridy = 3;
                gbcDadosCupom.fill = GridBagConstraints.NONE;
                gbcDadosCupom.weightx = 0.3;
                JLabel lblOperadorCupom = new JLabel("Operador:");
                lblOperadorCupom.setFont(new Font("Segoe UI", Font.BOLD, 12));
                lblOperadorCupom.setForeground(DARK_GRAY);
                dadosPanelCupom.add(lblOperadorCupom, gbcDadosCupom);
                gbcDadosCupom.gridx = 1; gbcDadosCupom.gridy = 3;
                gbcDadosCupom.fill = GridBagConstraints.HORIZONTAL;
                gbcDadosCupom.weightx = 0.7;
                ElegantTextField txtOperadorCupom = new ElegantTextField(20);
                txtOperadorCupom.setEditable(false);
                txtOperadorCupom.setText(usuarioAtual);
                txtOperadorCupom.setToolTipText("Operador atual");
                dadosPanelCupom.add(txtOperadorCupom, gbcDadosCupom);
                
                // Forma de Pagamento
                gbcDadosCupom.gridx = 2; gbcDadosCupom.gridy = 3;
                gbcDadosCupom.fill = GridBagConstraints.NONE;
                gbcDadosCupom.weightx = 0.3;
                JLabel lblFormaPagamentoCupom = new JLabel("Pagamento:");
                lblFormaPagamentoCupom.setFont(new Font("Segoe UI", Font.BOLD, 12));
                lblFormaPagamentoCupom.setForeground(DARK_GRAY);
                dadosPanelCupom.add(lblFormaPagamentoCupom, gbcDadosCupom);
                gbcDadosCupom.gridx = 3; gbcDadosCupom.gridy = 3;
                gbcDadosCupom.fill = GridBagConstraints.HORIZONTAL;
                gbcDadosCupom.weightx = 0.7;
                JComboBox<String> cbFormaPagamentoCupom = new JComboBox<>(new String[]{
                    "Dinheiro", "Cartão de Débito", "Cartão de Crédito", 
                    "PIX", "Vale Alimentação", "Vale Refeição", "Crediário"
                });
                cbFormaPagamentoCupom.setFont(new Font("Segoe UI", Font.PLAIN, 12));
                cbFormaPagamentoCupom.setToolTipText("Selecione a forma de pagamento");
                dadosPanelCupom.add(cbFormaPagamentoCupom, gbcDadosCupom);
                
                panel.add(dadosPanelCupom, gbc);
                
                // Painel de Itens do Cupom
                gbc.gridx = 0; gbc.gridy = 1; gbc.gridwidth = 2;
                gbc.fill = GridBagConstraints.HORIZONTAL;
                gbc.insets = new Insets(5, 5, 15, 5);
                
                JPanel itensPanel = new JPanel(new GridBagLayout());
                itensPanel.setBorder(BorderFactory.createTitledBorder(
                    BorderFactory.createEtchedBorder(), "🛒 Itens do Cupom",
                    TitledBorder.LEFT, TitledBorder.TOP, new Font("Segoe UI", Font.BOLD, 12),
                    DARK_GRAY));
                
                GridBagConstraints gbcItens = new GridBagConstraints();
                gbcItens.insets = new Insets(5, 5, 5, 5);
                
                // Código do Item
                gbcItens.gridx = 0; gbcItens.gridy = 0;
                gbcItens.fill = GridBagConstraints.NONE;
                gbcItens.weightx = 0.3;
                JLabel lblCodigoItem = new JLabel("Código:");
                lblCodigoItem.setFont(new Font("Segoe UI", Font.BOLD, 12));
                lblCodigoItem.setForeground(DARK_GRAY);
                itensPanel.add(lblCodigoItem, gbcItens);
                gbcItens.gridx = 1; gbcItens.gridy = 0;
                gbcItens.fill = GridBagConstraints.HORIZONTAL;
                gbcItens.weightx = 1.0;
                ElegantTextField txtCodigoItem = new ElegantTextField(15);
                txtCodigoItem.setToolTipText("Digite o código do item");
                itensPanel.add(txtCodigoItem, gbcItens);
                
                // Quantidade
                gbcItens.gridx = 2; gbcItens.gridy = 0;
                gbcItens.fill = GridBagConstraints.NONE;
                gbcItens.weightx = 0.3;
                JLabel lblQuantidadeItem = new JLabel("Qtd:");
                lblQuantidadeItem.setFont(new Font("Segoe UI", Font.BOLD, 12));
                lblQuantidadeItem.setForeground(DARK_GRAY);
                itensPanel.add(lblQuantidadeItem, gbcItens);
                gbcItens.gridx = 3; gbcItens.gridy = 0;
                gbcItens.fill = GridBagConstraints.HORIZONTAL;
                gbcItens.weightx = 0.7;
                ElegantTextField txtQuantidadeItem = new ElegantTextField(8);
                txtQuantidadeItem.setText("1");
                txtQuantidadeItem.setToolTipText("Quantidade do item");
                itensPanel.add(txtQuantidadeItem, gbcItens);
                
                // Botões
                gbcItens.gridx = 4; gbcItens.gridy = 0;
                gbcItens.fill = GridBagConstraints.NONE;
                gbcItens.weightx = 0;
                ElegantButton btnAdicionarItem = new ElegantButton("➕ Adicionar", SUCCESS_COLOR, false);
                btnAdicionarItem.setForeground(WHITE);
                btnAdicionarItem.setToolTipText("Adicionar item ao cupom");
                itensPanel.add(btnAdicionarItem, gbcItens);
                
                // Leitor de Código de Barras
                gbcItens.gridx = 0; gbcItens.gridy = 1;
                gbcItens.fill = GridBagConstraints.NONE;
                gbcItens.weightx = 0.3;
                JLabel lblLeitorBarrasCupom = new JLabel("Leitor:");
                lblLeitorBarrasCupom.setFont(new Font("Segoe UI", Font.BOLD, 12));
                lblLeitorBarrasCupom.setForeground(DARK_GRAY);
                itensPanel.add(lblLeitorBarrasCupom, gbcItens);
                gbcItens.gridx = 1; gbcItens.gridy = 1;
                gbcItens.fill = GridBagConstraints.HORIZONTAL;
                gbcItens.weightx = 2.0;
                ElegantTextField txtLeitorBarrasCupom = new ElegantTextField(30);
                txtLeitorBarrasCupom.setToolTipText("Aguardando leitura do código de barras...");
                itensPanel.add(txtLeitorBarrasCupom, gbcItens);
                gbcItens.gridx = 2; gbcItens.gridy = 1;
                gbcItens.fill = GridBagConstraints.NONE;
                gbcItens.weightx = 0;
                ElegantButton btnEscanearCupom = new ElegantButton("📷", WARNING_COLOR, false);
                btnEscanearCupom.setForeground(WHITE);
                btnEscanearCupom.setToolTipText("Escanear código de barras");
                itensPanel.add(btnEscanearCupom, gbcItens);
                
                panel.add(itensPanel, gbc);
                
                // Painel de Totais
                gbc.gridx = 0; gbc.gridy = 2; gbc.gridwidth = 2;
                gbc.fill = GridBagConstraints.HORIZONTAL;
                gbc.insets = new Insets(5, 5, 15, 5);
                
                JPanel totaisPanel = new JPanel(new GridBagLayout());
                totaisPanel.setBorder(BorderFactory.createTitledBorder(
                    BorderFactory.createEtchedBorder(), "💰 Totais do Cupom",
                    TitledBorder.LEFT, TitledBorder.TOP, new Font("Segoe UI", Font.BOLD, 12),
                    DARK_GRAY));
                
                GridBagConstraints gbcTotais = new GridBagConstraints();
                gbcTotais.insets = new Insets(5, 5, 5, 5);
                
                // Subtotal
                gbcTotais.gridx = 0; gbcTotais.gridy = 0;
                gbcTotais.fill = GridBagConstraints.NONE;
                gbcTotais.weightx = 0.3;
                JLabel lblSubtotalCupom = new JLabel("Subtotal:");
                lblSubtotalCupom.setFont(new Font("Segoe UI", Font.BOLD, 12));
                lblSubtotalCupom.setForeground(DARK_GRAY);
                totaisPanel.add(lblSubtotalCupom, gbcTotais);
                gbcTotais.gridx = 1; gbcTotais.gridy = 0;
                gbcTotais.fill = GridBagConstraints.HORIZONTAL;
                gbcTotais.weightx = 0.7;
                ElegantTextField txtSubtotalCupom = new ElegantTextField(15);
                txtSubtotalCupom.setEditable(false);
                txtSubtotalCupom.setText("R$ 0,00");
                txtSubtotalCupom.setToolTipText("Valor subtotal dos itens");
                totaisPanel.add(txtSubtotalCupom, gbcTotais);
                
                // Desconto
                gbcTotais.gridx = 2; gbcTotais.gridy = 0;
                gbcTotais.fill = GridBagConstraints.NONE;
                gbcTotais.weightx = 0.3;
                JLabel lblDescontoCupom = new JLabel("Desconto:");
                lblDescontoCupom.setFont(new Font("Segoe UI", Font.BOLD, 12));
                lblDescontoCupom.setForeground(DARK_GRAY);
                totaisPanel.add(lblDescontoCupom, gbcTotais);
                gbcTotais.gridx = 3; gbcTotais.gridy = 0;
                gbcTotais.fill = GridBagConstraints.HORIZONTAL;
                gbcTotais.weightx = 0.7;
                ElegantTextField txtDescontoCupom = new ElegantTextField(10);
                txtDescontoCupom.setText("0,00");
                txtDescontoCupom.setToolTipText("Valor do desconto");
                totaisPanel.add(txtDescontoCupom, gbcTotais);
                
                // Acréscimo
                gbcTotais.gridx = 0; gbcTotais.gridy = 1;
                gbcTotais.fill = GridBagConstraints.NONE;
                gbcTotais.weightx = 0.3;
                JLabel lblAcrescimo = new JLabel("Acréscimo:");
                lblAcrescimo.setFont(new Font("Segoe UI", Font.BOLD, 12));
                lblAcrescimo.setForeground(DARK_GRAY);
                totaisPanel.add(lblAcrescimo, gbcTotais);
                gbcTotais.gridx = 1; gbcTotais.gridy = 1;
                gbcTotais.fill = GridBagConstraints.HORIZONTAL;
                gbcTotais.weightx = 0.7;
                ElegantTextField txtAcrescimo = new ElegantTextField(10);
                txtAcrescimo.setText("0,00");
                txtAcrescimo.setToolTipText("Valor do acréscimo");
                totaisPanel.add(txtAcrescimo, gbcTotais);
                
                // Total
                gbcTotais.gridx = 2; gbcTotais.gridy = 1;
                gbcTotais.fill = GridBagConstraints.NONE;
                gbcTotais.weightx = 0.3;
                JLabel lblTotalCupom = new JLabel("Total:");
                lblTotalCupom.setFont(new Font("Segoe UI", Font.BOLD, 14));
                lblTotalCupom.setForeground(new Color(0, 128, 0));
                totaisPanel.add(lblTotalCupom, gbcTotais);
                gbcTotais.gridx = 3; gbcTotais.gridy = 1;
                gbcTotais.fill = GridBagConstraints.HORIZONTAL;
                gbcTotais.weightx = 0.7;
                ElegantTextField txtTotalCupom = new ElegantTextField(15);
                txtTotalCupom.setEditable(false);
                txtTotalCupom.setText("R$ 0,00");
                txtTotalCupom.setFont(new Font("Segoe UI", Font.BOLD, 14));
                txtTotalCupom.setForeground(new Color(0, 128, 0));
                txtTotalCupom.setToolTipText("Valor total do cupom");
                totaisPanel.add(txtTotalCupom, gbcTotais);
                
                // Valor Recebido
                gbcTotais.gridx = 0; gbcTotais.gridy = 2;
                gbcTotais.fill = GridBagConstraints.NONE;
                gbcTotais.weightx = 0.3;
                JLabel lblValorRecebidoCupom = new JLabel("Recebido:");
                lblValorRecebidoCupom.setFont(new Font("Segoe UI", Font.BOLD, 12));
                lblValorRecebidoCupom.setForeground(DARK_GRAY);
                totaisPanel.add(lblValorRecebidoCupom, gbcTotais);
                gbcTotais.gridx = 1; gbcTotais.gridy = 2;
                gbcTotais.fill = GridBagConstraints.HORIZONTAL;
                gbcTotais.weightx = 0.7;
                ElegantTextField txtValorRecebidoCupom = new ElegantTextField(15);
                txtValorRecebidoCupom.setToolTipText("Valor recebido do cliente");
                totaisPanel.add(txtValorRecebidoCupom, gbcTotais);
                
                // Troco
                gbcTotais.gridx = 2; gbcTotais.gridy = 2;
                gbcTotais.fill = GridBagConstraints.NONE;
                gbcTotais.weightx = 0.3;
                JLabel lblTrocoCupom = new JLabel("Troco:");
                lblTrocoCupom.setFont(new Font("Segoe UI", Font.BOLD, 12));
                lblTrocoCupom.setForeground(DARK_GRAY);
                totaisPanel.add(lblTrocoCupom, gbcTotais);
                gbcTotais.gridx = 3; gbcTotais.gridy = 2;
                gbcTotais.fill = GridBagConstraints.HORIZONTAL;
                gbcTotais.weightx = 0.7;
                ElegantTextField txtTrocoCupom = new ElegantTextField(15);
                txtTrocoCupom.setEditable(false);
                txtTrocoCupom.setText("R$ 0,00");
                txtTrocoCupom.setToolTipText("Valor de troco para o cliente");
                totaisPanel.add(txtTrocoCupom, gbcTotais);
                
                panel.add(totaisPanel, gbc);
                
                // Tabela de Itens do Cupom
                gbc.gridx = 0; gbc.gridy = 3; gbc.gridwidth = 2;
                gbc.fill = GridBagConstraints.BOTH;
                gbc.weightx = 1.0; gbc.weighty = 1.0;
                
                String[] colunasTabelaCupom = {"Código", "Descrição", "UN", "Qtd", "Unitário", "Total", "ICMS", "Ações"};
                Object[][] dadosTabelaCupom = {};
                
                JTable tabelaCupom = new JTable(dadosTabelaCupom, colunasTabelaCupom);
                tabelaCupom.setFont(new Font("Segoe UI", Font.PLAIN, 11));
                tabelaCupom.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 12));
                tabelaCupom.getTableHeader().setBackground(new Color(70, 130, 180));
                tabelaCupom.getTableHeader().setForeground(WHITE);
                tabelaCupom.setRowHeight(25);
                tabelaCupom.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
                tabelaCupom.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
                
                JScrollPane scrollPaneTabelaCupom = new JScrollPane(tabelaCupom);
                scrollPaneTabelaCupom.setPreferredSize(new Dimension(900, 250));
                scrollPaneTabelaCupom.setBorder(BorderFactory.createTitledBorder(
                    BorderFactory.createEtchedBorder(), "🧾 Itens do Cupom Fiscal",
                    TitledBorder.LEFT, TitledBorder.TOP, new Font("Segoe UI", Font.BOLD, 12),
                    DARK_GRAY));
                panel.add(scrollPaneTabelaCupom, gbc);
                
                // Painel de Ações
                gbc.gridx = 0; gbc.gridy = 4; gbc.gridwidth = 2;
                gbc.fill = GridBagConstraints.HORIZONTAL;
                gbc.weightx = 1.0; gbc.weighty = 0;
                gbc.insets = new Insets(10, 5, 5, 5);
                
                JPanel acoesPanelCupom = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 5));
                acoesPanelCupom.setOpaque(false);
                
                ElegantButton btnEmitirCupom = new ElegantButton("🧾 Emitir Cupom", new Color(0, 100, 200), false);
                btnEmitirCupom.setForeground(WHITE);
                btnEmitirCupom.setFont(new Font("Segoe UI", Font.BOLD, 14));
                btnEmitirCupom.setPreferredSize(new Dimension(150, 40));
                acoesPanelCupom.add(btnEmitirCupom);
                
                ElegantButton btnCancelarCupom = new ElegantButton("❌ Cancelar", new Color(200, 50, 50), false);
                btnCancelarCupom.setForeground(WHITE);
                btnCancelarCupom.setFont(new Font("Segoe UI", Font.BOLD, 14));
                btnCancelarCupom.setPreferredSize(new Dimension(120, 40));
                acoesPanelCupom.add(btnCancelarCupom);
                
                ElegantButton btnPausarCupom = new ElegantButton("⏸️ Pausar", WARNING_COLOR, false);
                btnPausarCupom.setForeground(WHITE);
                btnPausarCupom.setFont(new Font("Segoe UI", Font.BOLD, 14));
                btnPausarCupom.setPreferredSize(new Dimension(120, 40));
                acoesPanelCupom.add(btnPausarCupom);
                
                ElegantButton btnImprimirCupomFiscal = new ElegantButton("🖨️ Imprimir", new Color(70, 130, 180), false);
                btnImprimirCupomFiscal.setForeground(WHITE);
                btnImprimirCupomFiscal.setFont(new Font("Segoe UI", Font.BOLD, 14));
                btnImprimirCupomFiscal.setPreferredSize(new Dimension(120, 40));
                acoesPanelCupom.add(btnImprimirCupomFiscal);
                
                ElegantButton btnEnviarEmail = new ElegantButton("📧 Email", new Color(52, 152, 219), false);
                btnEnviarEmail.setForeground(WHITE);
                btnEnviarEmail.setFont(new Font("Segoe UI", Font.BOLD, 14));
                btnEnviarEmail.setPreferredSize(new Dimension(100, 40));
                acoesPanelCupom.add(btnEnviarEmail);
                
                panel.add(acoesPanelCupom, gbc);
                break;
                
            case "🎯 Venda Rápida":
                // Painel de Dados da Venda Rápida
                gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
                gbc.fill = GridBagConstraints.HORIZONTAL;
                gbc.insets = new Insets(5, 5, 15, 5);
                
                JPanel dadosPanelVendaRapida = new JPanel(new GridBagLayout());
                dadosPanelVendaRapida.setBorder(BorderFactory.createTitledBorder(
                    BorderFactory.createEtchedBorder(), "⚡ Terminal de Venda Rápida",
                    TitledBorder.LEFT, TitledBorder.TOP, new Font("Segoe UI", Font.BOLD, 12),
                    DARK_GRAY));
                
                GridBagConstraints gbcDadosVendaRapida = new GridBagConstraints();
                gbcDadosVendaRapida.insets = new Insets(5, 5, 5, 5);
                
                // Número da Venda
                gbcDadosVendaRapida.gridx = 0; gbcDadosVendaRapida.gridy = 0;
                gbcDadosVendaRapida.fill = GridBagConstraints.NONE;
                gbcDadosVendaRapida.weightx = 0.3;
                JLabel lblNumeroVenda = new JLabel("Nº Venda:");
                lblNumeroVenda.setFont(new Font("Segoe UI", Font.BOLD, 12));
                lblNumeroVenda.setForeground(DARK_GRAY);
                dadosPanelVendaRapida.add(lblNumeroVenda, gbcDadosVendaRapida);
                gbcDadosVendaRapida.gridx = 1; gbcDadosVendaRapida.gridy = 0;
                gbcDadosVendaRapida.fill = GridBagConstraints.HORIZONTAL;
                gbcDadosVendaRapida.weightx = 0.7;
                ElegantTextField txtNumeroVenda = new ElegantTextField(15);
                txtNumeroVenda.setEditable(false);
                txtNumeroVenda.setText("AUTO");
                txtNumeroVenda.setToolTipText("Número automático da venda");
                dadosPanelVendaRapida.add(txtNumeroVenda, gbcDadosVendaRapida);
                
                // Data e Hora
                gbcDadosVendaRapida.gridx = 2; gbcDadosVendaRapida.gridy = 0;
                gbcDadosVendaRapida.fill = GridBagConstraints.NONE;
                gbcDadosVendaRapida.weightx = 0.3;
                JLabel lblDataHora = new JLabel("Data/Hora:");
                lblDataHora.setFont(new Font("Segoe UI", Font.BOLD, 12));
                lblDataHora.setForeground(DARK_GRAY);
                dadosPanelVendaRapida.add(lblDataHora, gbcDadosVendaRapida);
                gbcDadosVendaRapida.gridx = 3; gbcDadosVendaRapida.gridy = 0;
                gbcDadosVendaRapida.fill = GridBagConstraints.HORIZONTAL;
                gbcDadosVendaRapida.weightx = 0.7;
                ElegantTextField txtDataHora = new ElegantTextField(16);
                txtDataHora.setEditable(false);
                txtDataHora.setToolTipText("Data e hora atual");
                dadosPanelVendaRapida.add(txtDataHora, gbcDadosVendaRapida);
                
                // Cliente
                gbcDadosVendaRapida.gridx = 0; gbcDadosVendaRapida.gridy = 1;
                gbcDadosVendaRapida.fill = GridBagConstraints.NONE;
                gbcDadosVendaRapida.weightx = 0.3;
                JLabel lblClienteVR = new JLabel("Cliente:");
                lblClienteVR.setFont(new Font("Segoe UI", Font.BOLD, 12));
                lblClienteVR.setForeground(DARK_GRAY);
                dadosPanelVendaRapida.add(lblClienteVR, gbcDadosVendaRapida);
                gbcDadosVendaRapida.gridx = 1; gbcDadosVendaRapida.gridy = 1;
                gbcDadosVendaRapida.fill = GridBagConstraints.HORIZONTAL;
                gbcDadosVendaRapida.weightx = 1.7;
                ElegantTextField txtClienteVR = new ElegantTextField(40);
                txtClienteVR.setToolTipText("Nome do cliente (opcional para venda rápida)");
                dadosPanelVendaRapida.add(txtClienteVR, gbcDadosVendaRapida);
                gbcDadosVendaRapida.gridx = 2; gbcDadosVendaRapida.gridy = 1;
                gbcDadosVendaRapida.fill = GridBagConstraints.NONE;
                gbcDadosVendaRapida.weightx = 0.3;
                ElegantButton btnBuscarCliente = new ElegantButton("🔍", PRIMARY_COLOR, false);
                btnBuscarCliente.setForeground(WHITE);
                btnBuscarCliente.setToolTipText("Buscar cliente");
                dadosPanelVendaRapida.add(btnBuscarCliente, gbcDadosVendaRapida);
                gbcDadosVendaRapida.gridx = 3; gbcDadosVendaRapida.gridy = 1;
                gbcDadosVendaRapida.fill = GridBagConstraints.NONE;
                gbcDadosVendaRapida.weightx = 0.7;
                ElegantButton btnNovoCliente = new ElegantButton("➕", SUCCESS_COLOR, false);
                btnNovoCliente.setForeground(WHITE);
                btnNovoCliente.setToolTipText("Cadastrar novo cliente");
                dadosPanelVendaRapida.add(btnNovoCliente, gbcDadosVendaRapida);
                
                // Operador
                gbcDadosVendaRapida.gridx = 0; gbcDadosVendaRapida.gridy = 2;
                gbcDadosVendaRapida.fill = GridBagConstraints.NONE;
                gbcDadosVendaRapida.weightx = 0.3;
                JLabel lblOperador = new JLabel("Operador:");
                lblOperador.setFont(new Font("Segoe UI", Font.BOLD, 12));
                lblOperador.setForeground(DARK_GRAY);
                dadosPanelVendaRapida.add(lblOperador, gbcDadosVendaRapida);
                gbcDadosVendaRapida.gridx = 1; gbcDadosVendaRapida.gridy = 2;
                gbcDadosVendaRapida.fill = GridBagConstraints.HORIZONTAL;
                gbcDadosVendaRapida.weightx = 0.7;
                ElegantTextField txtOperador = new ElegantTextField(20);
                txtOperador.setEditable(false);
                txtOperador.setText(usuarioAtual);
                txtOperador.setToolTipText("Operador atual");
                dadosPanelVendaRapida.add(txtOperador, gbcDadosVendaRapida);
                
                // Terminal
                gbcDadosVendaRapida.gridx = 2; gbcDadosVendaRapida.gridy = 2;
                gbcDadosVendaRapida.fill = GridBagConstraints.NONE;
                gbcDadosVendaRapida.weightx = 0.3;
                JLabel lblTerminal = new JLabel("Terminal:");
                lblTerminal.setFont(new Font("Segoe UI", Font.BOLD, 12));
                lblTerminal.setForeground(DARK_GRAY);
                dadosPanelVendaRapida.add(lblTerminal, gbcDadosVendaRapida);
                gbcDadosVendaRapida.gridx = 3; gbcDadosVendaRapida.gridy = 2;
                gbcDadosVendaRapida.fill = GridBagConstraints.HORIZONTAL;
                gbcDadosVendaRapida.weightx = 0.7;
                JComboBox<String> cbTerminal = new JComboBox<>(new String[]{
                    "Terminal 01 - Caixa Principal", "Terminal 02 - Balcão", 
                    "Terminal 03 - Delivery", "Terminal 04 - Self-Service"
                });
                cbTerminal.setFont(new Font("Segoe UI", Font.PLAIN, 12));
                cbTerminal.setToolTipText("Selecione o terminal");
                dadosPanelVendaRapida.add(cbTerminal, gbcDadosVendaRapida);
                
                panel.add(dadosPanelVendaRapida, gbc);
                
                // Painel de Produtos
                gbc.gridx = 0; gbc.gridy = 1; gbc.gridwidth = 2;
                gbc.fill = GridBagConstraints.HORIZONTAL;
                gbc.insets = new Insets(5, 5, 15, 5);
                
                JPanel produtosPanel = new JPanel(new GridBagLayout());
                produtosPanel.setBorder(BorderFactory.createTitledBorder(
                    BorderFactory.createEtchedBorder(), "🛒 Adicionar Produtos",
                    TitledBorder.LEFT, TitledBorder.TOP, new Font("Segoe UI", Font.BOLD, 12),
                    DARK_GRAY));
                
                GridBagConstraints gbcProdutos = new GridBagConstraints();
                gbcProdutos.insets = new Insets(5, 5, 5, 5);
                
                // Código do Produto
                gbcProdutos.gridx = 0; gbcProdutos.gridy = 0;
                gbcProdutos.fill = GridBagConstraints.NONE;
                gbcProdutos.weightx = 0.3;
                JLabel lblCodigoProduto = new JLabel("Código:");
                lblCodigoProduto.setFont(new Font("Segoe UI", Font.BOLD, 12));
                lblCodigoProduto.setForeground(DARK_GRAY);
                produtosPanel.add(lblCodigoProduto, gbcProdutos);
                gbcProdutos.gridx = 1; gbcProdutos.gridy = 0;
                gbcProdutos.fill = GridBagConstraints.HORIZONTAL;
                gbcProdutos.weightx = 1.0;
                ElegantTextField txtCodigoProduto = new ElegantTextField(15);
                txtCodigoProduto.setToolTipText("Digite o código ou use o leitor");
                produtosPanel.add(txtCodigoProduto, gbcProdutos);
                
                // Quantidade
                gbcProdutos.gridx = 2; gbcProdutos.gridy = 0;
                gbcProdutos.fill = GridBagConstraints.NONE;
                gbcProdutos.weightx = 0.3;
                JLabel lblQuantidade = new JLabel("Qtd:");
                lblQuantidade.setFont(new Font("Segoe UI", Font.BOLD, 12));
                lblQuantidade.setForeground(DARK_GRAY);
                produtosPanel.add(lblQuantidade, gbcProdutos);
                gbcProdutos.gridx = 3; gbcProdutos.gridy = 0;
                gbcProdutos.fill = GridBagConstraints.HORIZONTAL;
                gbcProdutos.weightx = 0.7;
                ElegantTextField txtQuantidade = new ElegantTextField(8);
                txtQuantidade.setText("1");
                txtQuantidade.setToolTipText("Quantidade do produto");
                produtosPanel.add(txtQuantidade, gbcProdutos);
                
                // Botões
                gbcProdutos.gridx = 4; gbcProdutos.gridy = 0;
                gbcProdutos.fill = GridBagConstraints.NONE;
                gbcProdutos.weightx = 0;
                ElegantButton btnAdicionarProduto = new ElegantButton("➕ Adicionar", SUCCESS_COLOR, false);
                btnAdicionarProduto.setForeground(WHITE);
                btnAdicionarProduto.setToolTipText("Adicionar produto à venda");
                produtosPanel.add(btnAdicionarProduto, gbcProdutos);
                
                // Leitor de Código de Barras
                gbcProdutos.gridx = 0; gbcProdutos.gridy = 1;
                gbcProdutos.fill = GridBagConstraints.NONE;
                gbcProdutos.weightx = 0.3;
                JLabel lblLeitorBarras = new JLabel("Leitor:");
                lblLeitorBarras.setFont(new Font("Segoe UI", Font.BOLD, 12));
                lblLeitorBarras.setForeground(DARK_GRAY);
                produtosPanel.add(lblLeitorBarras, gbcProdutos);
                gbcProdutos.gridx = 1; gbcProdutos.gridy = 1;
                gbcProdutos.fill = GridBagConstraints.HORIZONTAL;
                gbcProdutos.weightx = 2.0;
                ElegantTextField txtLeitorBarras = new ElegantTextField(30);
                txtLeitorBarras.setToolTipText("Aguardando leitura do código de barras...");
                produtosPanel.add(txtLeitorBarras, gbcProdutos);
                gbcProdutos.gridx = 2; gbcProdutos.gridy = 1;
                gbcProdutos.fill = GridBagConstraints.NONE;
                gbcProdutos.weightx = 0;
                ElegantButton btnEscanear = new ElegantButton("📷", WARNING_COLOR, false);
                btnEscanear.setForeground(WHITE);
                btnEscanear.setToolTipText("Escanear código de barras");
                produtosPanel.add(btnEscanear, gbcProdutos);
                
                panel.add(produtosPanel, gbc);
                
                // Painel de Pagamento
                gbc.gridx = 0; gbc.gridy = 2; gbc.gridwidth = 2;
                gbc.fill = GridBagConstraints.HORIZONTAL;
                gbc.insets = new Insets(5, 5, 15, 5);
                
                JPanel pagamentoPanel = new JPanel(new GridBagLayout());
                pagamentoPanel.setBorder(BorderFactory.createTitledBorder(
                    BorderFactory.createEtchedBorder(), "💳 Pagamento",
                    TitledBorder.LEFT, TitledBorder.TOP, new Font("Segoe UI", Font.BOLD, 12),
                    DARK_GRAY));
                
                GridBagConstraints gbcPagamento = new GridBagConstraints();
                gbcPagamento.insets = new Insets(5, 5, 5, 5);
                
                // Subtotal
                gbcPagamento.gridx = 0; gbcPagamento.gridy = 0;
                gbcPagamento.fill = GridBagConstraints.NONE;
                gbcPagamento.weightx = 0.3;
                JLabel lblSubtotal = new JLabel("Subtotal:");
                lblSubtotal.setFont(new Font("Segoe UI", Font.BOLD, 12));
                lblSubtotal.setForeground(DARK_GRAY);
                pagamentoPanel.add(lblSubtotal, gbcPagamento);
                gbcPagamento.gridx = 1; gbcPagamento.gridy = 0;
                gbcPagamento.fill = GridBagConstraints.HORIZONTAL;
                gbcPagamento.weightx = 0.7;
                ElegantTextField txtSubtotal = new ElegantTextField(15);
                txtSubtotal.setEditable(false);
                txtSubtotal.setText("R$ 0,00");
                txtSubtotal.setToolTipText("Valor subtotal dos produtos");
                pagamentoPanel.add(txtSubtotal, gbcPagamento);
                
                // Desconto
                gbcPagamento.gridx = 2; gbcPagamento.gridy = 0;
                gbcPagamento.fill = GridBagConstraints.NONE;
                gbcPagamento.weightx = 0.3;
                JLabel lblDesconto = new JLabel("Desconto:");
                lblDesconto.setFont(new Font("Segoe UI", Font.BOLD, 12));
                lblDesconto.setForeground(DARK_GRAY);
                pagamentoPanel.add(lblDesconto, gbcPagamento);
                gbcPagamento.gridx = 3; gbcPagamento.gridy = 0;
                gbcPagamento.fill = GridBagConstraints.HORIZONTAL;
                gbcPagamento.weightx = 0.7;
                ElegantTextField txtDesconto = new ElegantTextField(10);
                txtDesconto.setText("0,00");
                txtDesconto.setToolTipText("Valor do desconto");
                pagamentoPanel.add(txtDesconto, gbcPagamento);
                
                // Total
                gbcPagamento.gridx = 0; gbcPagamento.gridy = 1;
                gbcPagamento.fill = GridBagConstraints.NONE;
                gbcPagamento.weightx = 0.3;
                JLabel lblTotal = new JLabel("Total:");
                lblTotal.setFont(new Font("Segoe UI", Font.BOLD, 14));
                lblTotal.setForeground(new Color(0, 128, 0));
                pagamentoPanel.add(lblTotal, gbcPagamento);
                gbcPagamento.gridx = 1; gbcPagamento.gridy = 1;
                gbcPagamento.fill = GridBagConstraints.HORIZONTAL;
                gbcPagamento.weightx = 0.7;
                ElegantTextField txtTotal = new ElegantTextField(15);
                txtTotal.setEditable(false);
                txtTotal.setText("R$ 0,00");
                txtTotal.setFont(new Font("Segoe UI", Font.BOLD, 14));
                txtTotal.setForeground(new Color(0, 128, 0));
                txtTotal.setToolTipText("Valor total da venda");
                pagamentoPanel.add(txtTotal, gbcPagamento);
                
                // Forma de Pagamento
                gbcPagamento.gridx = 2; gbcPagamento.gridy = 1;
                gbcPagamento.fill = GridBagConstraints.NONE;
                gbcPagamento.weightx = 0.3;
                JLabel lblFormaPagamento = new JLabel("Pagamento:");
                lblFormaPagamento.setFont(new Font("Segoe UI", Font.BOLD, 12));
                lblFormaPagamento.setForeground(DARK_GRAY);
                pagamentoPanel.add(lblFormaPagamento, gbcPagamento);
                gbcPagamento.gridx = 3; gbcPagamento.gridy = 1;
                gbcPagamento.fill = GridBagConstraints.HORIZONTAL;
                gbcPagamento.weightx = 0.7;
                JComboBox<String> cbFormaPagamento = new JComboBox<>(new String[]{
                    "Dinheiro", "Cartão de Débito", "Cartão de Crédito", 
                    "PIX", "Vale Alimentação", "Vale Refeição", "Crediário"
                });
                cbFormaPagamento.setFont(new Font("Segoe UI", Font.PLAIN, 12));
                cbFormaPagamento.setToolTipText("Selecione a forma de pagamento");
                pagamentoPanel.add(cbFormaPagamento, gbcPagamento);
                
                // Valor Recebido
                gbcPagamento.gridx = 0; gbcPagamento.gridy = 2;
                gbcPagamento.fill = GridBagConstraints.NONE;
                gbcPagamento.weightx = 0.3;
                JLabel lblValorRecebido = new JLabel("Recebido:");
                lblValorRecebido.setFont(new Font("Segoe UI", Font.BOLD, 12));
                lblValorRecebido.setForeground(DARK_GRAY);
                pagamentoPanel.add(lblValorRecebido, gbcPagamento);
                gbcPagamento.gridx = 1; gbcPagamento.gridy = 2;
                gbcPagamento.fill = GridBagConstraints.HORIZONTAL;
                gbcPagamento.weightx = 0.7;
                ElegantTextField txtValorRecebido = new ElegantTextField(15);
                txtValorRecebido.setToolTipText("Valor recebido do cliente");
                pagamentoPanel.add(txtValorRecebido, gbcPagamento);
                
                // Troco
                gbcPagamento.gridx = 2; gbcPagamento.gridy = 2;
                gbcPagamento.fill = GridBagConstraints.NONE;
                gbcPagamento.weightx = 0.3;
                JLabel lblTroco = new JLabel("Troco:");
                lblTroco.setFont(new Font("Segoe UI", Font.BOLD, 12));
                lblTroco.setForeground(DARK_GRAY);
                pagamentoPanel.add(lblTroco, gbcPagamento);
                gbcPagamento.gridx = 3; gbcPagamento.gridy = 2;
                gbcPagamento.fill = GridBagConstraints.HORIZONTAL;
                gbcPagamento.weightx = 0.7;
                ElegantTextField txtTroco = new ElegantTextField(15);
                txtTroco.setEditable(false);
                txtTroco.setText("R$ 0,00");
                txtTroco.setToolTipText("Valor de troco para o cliente");
                pagamentoPanel.add(txtTroco, gbcPagamento);
                
                panel.add(pagamentoPanel, gbc);
                
                // Tabela de Itens da Venda
                gbc.gridx = 0; gbc.gridy = 3; gbc.gridwidth = 2;
                gbc.fill = GridBagConstraints.BOTH;
                gbc.weightx = 1.0; gbc.weighty = 1.0;
                
                String[] colunasTabelaVendaRapida = {"Código", "Descrição", "Qtd", "Unitário", "Total", "Ações"};
                Object[][] dadosTabelaVendaRapida = {};
                
                JTable tabelaVendaRapida = new JTable(dadosTabelaVendaRapida, colunasTabelaVendaRapida);
                tabelaVendaRapida.setFont(new Font("Segoe UI", Font.PLAIN, 11));
                tabelaVendaRapida.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 12));
                tabelaVendaRapida.getTableHeader().setBackground(new Color(70, 130, 180));
                tabelaVendaRapida.getTableHeader().setForeground(WHITE);
                tabelaVendaRapida.setRowHeight(25);
                tabelaVendaRapida.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
                tabelaVendaRapida.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
                
                JScrollPane scrollPaneTabelaVendaRapida = new JScrollPane(tabelaVendaRapida);
                scrollPaneTabelaVendaRapida.setPreferredSize(new Dimension(800, 250));
                scrollPaneTabelaVendaRapida.setBorder(BorderFactory.createTitledBorder(
                    BorderFactory.createEtchedBorder(), "🛒 Itens da Venda",
                    TitledBorder.LEFT, TitledBorder.TOP, new Font("Segoe UI", Font.BOLD, 12),
                    DARK_GRAY));
                panel.add(scrollPaneTabelaVendaRapida, gbc);
                
                // Painel de Ações
                gbc.gridx = 0; gbc.gridy = 4; gbc.gridwidth = 2;
                gbc.fill = GridBagConstraints.HORIZONTAL;
                gbc.weightx = 1.0; gbc.weighty = 0;
                gbc.insets = new Insets(10, 5, 5, 5);
                
                JPanel acoesPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 5));
                acoesPanel.setOpaque(false);
                
                ElegantButton btnFinalizarVenda = new ElegantButton("✅ Finalizar Venda", new Color(0, 150, 0), false);
                btnFinalizarVenda.setForeground(WHITE);
                btnFinalizarVenda.setFont(new Font("Segoe UI", Font.BOLD, 14));
                btnFinalizarVenda.setPreferredSize(new Dimension(150, 40));
                acoesPanel.add(btnFinalizarVenda);
                
                ElegantButton btnCancelarVenda = new ElegantButton("❌ Cancelar", new Color(200, 50, 50), false);
                btnCancelarVenda.setForeground(WHITE);
                btnCancelarVenda.setFont(new Font("Segoe UI", Font.BOLD, 14));
                btnCancelarVenda.setPreferredSize(new Dimension(120, 40));
                acoesPanel.add(btnCancelarVenda);
                
                ElegantButton btnPausarVenda = new ElegantButton("⏸️ Pausar", WARNING_COLOR, false);
                btnPausarVenda.setForeground(WHITE);
                btnPausarVenda.setFont(new Font("Segoe UI", Font.BOLD, 14));
                btnPausarVenda.setPreferredSize(new Dimension(120, 40));
                acoesPanel.add(btnPausarVenda);
                
                ElegantButton btnImprimirCupom = new ElegantButton("🖨️ Cupom", new Color(70, 130, 180), false);
                btnImprimirCupom.setForeground(WHITE);
                btnImprimirCupom.setFont(new Font("Segoe UI", Font.BOLD, 14));
                btnImprimirCupom.setPreferredSize(new Dimension(120, 40));
                acoesPanel.add(btnImprimirCupom);
                
                panel.add(acoesPanel, gbc);
                break;
                
            default:
                return criarFormularioPadraoElegante(item, "VENDAS", gbc);
        }
        
        return panel;
    }
    
    /**
     * Formulários Produtos elegantes
     */
    private JPanel criarFormularioProdutosElegante(String item, GridBagConstraints gbc) {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setOpaque(false);
        
        switch (item) {
            case "➕ Cadastrar Produto":
                // Código
                gbc.gridx = 0; gbc.gridy = 0;
                JLabel lblCodigo = new JLabel("Código:");
                lblCodigo.setFont(new Font("Segoe UI", Font.BOLD, 12));
                lblCodigo.setForeground(DARK_GRAY);
                panel.add(lblCodigo, gbc);
                gbc.gridx = 1; gbc.gridy = 0;
                ElegantTextField txtCodigo = new ElegantTextField(15);
                panel.add(txtCodigo, gbc);
                
                // Nome do Produto
                gbc.gridx = 0; gbc.gridy = 1;
                JLabel lblNome = new JLabel("Nome do Produto:");
                lblNome.setFont(new Font("Segoe UI", Font.BOLD, 12));
                lblNome.setForeground(DARK_GRAY);
                panel.add(lblNome, gbc);
                gbc.gridx = 1; gbc.gridy = 1;
                ElegantTextField txtNome = new ElegantTextField(40);
                panel.add(txtNome, gbc);
                
                // Descrição
                gbc.gridx = 0; gbc.gridy = 2;
                JLabel lblDescricao = new JLabel("Descrição:");
                lblDescricao.setFont(new Font("Segoe UI", Font.BOLD, 12));
                lblDescricao.setForeground(DARK_GRAY);
                panel.add(lblDescricao, gbc);
                gbc.gridx = 1; gbc.gridy = 2;
                JTextArea txtDescricaoArea = new JTextArea(3, 40);
                txtDescricaoArea.setFont(new Font("Segoe UI", Font.PLAIN, 12));
                txtDescricaoArea.setLineWrap(true);
                txtDescricaoArea.setWrapStyleWord(true);
                txtDescricaoArea.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(MEDIUM_GRAY, 1),
                    BorderFactory.createEmptyBorder(5, 8, 5, 8)
                ));
                JScrollPane scrollDescricao = new JScrollPane(txtDescricaoArea);
                scrollDescricao.setBorder(BorderFactory.createEmptyBorder());
                scrollDescricao.setOpaque(false);
                scrollDescricao.getViewport().setOpaque(false);
                panel.add(scrollDescricao, gbc);
                
                // Categoria
                gbc.gridx = 0; gbc.gridy = 3;
                JLabel lblCategoria = new JLabel("Categoria:");
                lblCategoria.setFont(new Font("Segoe UI", Font.BOLD, 12));
                lblCategoria.setForeground(DARK_GRAY);
                panel.add(lblCategoria, gbc);
                gbc.gridx = 1; gbc.gridy = 3;
                JComboBox<String> cbCategoria = new JComboBox<>(new String[]{"Eletrônicos", "Roupas", "Alimentos", "Móveis", "Outros"});
                cbCategoria.setFont(new Font("Segoe UI", Font.PLAIN, 12));
                panel.add(cbCategoria, gbc);
                
                // Preço de Custo
                gbc.gridx = 0; gbc.gridy = 4;
                JLabel lblPrecoCusto = new JLabel("Preço de Custo:");
                lblPrecoCusto.setFont(new Font("Segoe UI", Font.BOLD, 12));
                lblPrecoCusto.setForeground(DARK_GRAY);
                panel.add(lblPrecoCusto, gbc);
                gbc.gridx = 1; gbc.gridy = 4;
                ElegantTextField txtPrecoCusto = new ElegantTextField(15);
                panel.add(txtPrecoCusto, gbc);
                
                // Preço de Venda
                gbc.gridx = 0; gbc.gridy = 5;
                JLabel lblPrecoVenda = new JLabel("Preço de Venda:");
                lblPrecoVenda.setFont(new Font("Segoe UI", Font.BOLD, 12));
                lblPrecoVenda.setForeground(DARK_GRAY);
                panel.add(lblPrecoVenda, gbc);
                gbc.gridx = 1; gbc.gridy = 5;
                ElegantTextField txtPrecoVenda = new ElegantTextField(15);
                panel.add(txtPrecoVenda, gbc);
                
                // Estoque Mínimo
                gbc.gridx = 0; gbc.gridy = 6;
                JLabel lblEstoqueMinimo = new JLabel("Estoque Mínimo:");
                lblEstoqueMinimo.setFont(new Font("Segoe UI", Font.BOLD, 12));
                lblEstoqueMinimo.setForeground(DARK_GRAY);
                panel.add(lblEstoqueMinimo, gbc);
                gbc.gridx = 1; gbc.gridy = 6;
                ElegantTextField txtEstoqueMinimo = new ElegantTextField(10);
                panel.add(txtEstoqueMinimo, gbc);
                
                // Estoque Atual
                gbc.gridx = 0; gbc.gridy = 7;
                JLabel lblEstoqueAtual = new JLabel("Estoque Atual:");
                lblEstoqueAtual.setFont(new Font("Segoe UI", Font.BOLD, 12));
                lblEstoqueAtual.setForeground(DARK_GRAY);
                panel.add(lblEstoqueAtual, gbc);
                gbc.gridx = 1; gbc.gridy = 7;
                ElegantTextField txtEstoqueAtual = new ElegantTextField(10);
                panel.add(txtEstoqueAtual, gbc);
                
                // Unidade de Medida
                gbc.gridx = 0; gbc.gridy = 8;
                JLabel lblUnidade = new JLabel("Unidade:");
                lblUnidade.setFont(new Font("Segoe UI", Font.BOLD, 12));
                lblUnidade.setForeground(DARK_GRAY);
                panel.add(lblUnidade, gbc);
                gbc.gridx = 1; gbc.gridy = 8;
                JComboBox<String> cbUnidade = new JComboBox<>(new String[]{"UN", "KG", "LT", "M", "CX"});
                cbUnidade.setFont(new Font("Segoe UI", Font.PLAIN, 12));
                panel.add(cbUnidade, gbc);
                break;
                
            case "🔍 Consultar Produto":
                // Painel de Busca
                gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
                gbc.fill = GridBagConstraints.HORIZONTAL;
                gbc.insets = new Insets(5, 5, 15, 5);
                
                JPanel buscaPanelProduto = new JPanel(new GridBagLayout());
                buscaPanelProduto.setBorder(BorderFactory.createTitledBorder(
                    BorderFactory.createEtchedBorder(), "🔍 Consultar Produtos",
                    TitledBorder.LEFT, TitledBorder.TOP, new Font("Segoe UI", Font.BOLD, 12),
                    DARK_GRAY));
                
                GridBagConstraints gbcBuscaProduto = new GridBagConstraints();
                gbcBuscaProduto.insets = new Insets(5, 5, 5, 5);
                
                // Campo de busca
                gbcBuscaProduto.gridx = 0; gbcBuscaProduto.gridy = 0;
                gbcBuscaProduto.fill = GridBagConstraints.HORIZONTAL;
                gbcBuscaProduto.weightx = 1.0;
                JLabel lblBuscaProduto = new JLabel("Buscar:");
                lblBuscaProduto.setFont(new Font("Segoe UI", Font.BOLD, 12));
                lblBuscaProduto.setForeground(DARK_GRAY);
                buscaPanelProduto.add(lblBuscaProduto, gbcBuscaProduto);
                
                gbcBuscaProduto.gridx = 1; gbcBuscaProduto.gridy = 0;
                gbcBuscaProduto.fill = GridBagConstraints.HORIZONTAL;
                gbcBuscaProduto.weightx = 2.0;
                ElegantTextField txtBuscaProduto = new ElegantTextField(30);
                txtBuscaProduto.setToolTipText("Digite código, nome ou descrição do produto");
                buscaPanelProduto.add(txtBuscaProduto, gbcBuscaProduto);
                
                // Botões de busca
                gbcBuscaProduto.gridx = 2; gbcBuscaProduto.gridy = 0;
                gbcBuscaProduto.fill = GridBagConstraints.NONE;
                gbcBuscaProduto.weightx = 0;
                ElegantButton btnBuscarProduto = new ElegantButton("🔍 Buscar", PRIMARY_COLOR, false);
                btnBuscarProduto.setForeground(WHITE);
                buscaPanelProduto.add(btnBuscarProduto, gbcBuscaProduto);
                
                gbcBuscaProduto.gridx = 3; gbcBuscaProduto.gridy = 0;
                ElegantButton btnLimparBuscaProduto = new ElegantButton("🔄 Limpar", MEDIUM_GRAY, false);
                btnLimparBuscaProduto.setForeground(WHITE);
                buscaPanelProduto.add(btnLimparBuscaProduto, gbcBuscaProduto);
                
                panel.add(buscaPanelProduto, gbc);
                
                // Resetar gridwidth para campos abaixo
                gbc.gridwidth = 1;
                gbc.insets = new Insets(5, 5, 5, 5);
                
                // Filtros adicionais
                gbc.gridx = 0; gbc.gridy = 1;
                gbc.fill = GridBagConstraints.NONE;
                JLabel lblFiltrosProduto = new JLabel("Filtros:");
                lblFiltrosProduto.setFont(new Font("Segoe UI", Font.BOLD, 12));
                lblFiltrosProduto.setForeground(DARK_GRAY);
                panel.add(lblFiltrosProduto, gbc);
                
                gbc.gridx = 1; gbc.gridy = 1;
                gbc.fill = GridBagConstraints.HORIZONTAL;
                JPanel filtrosPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
                
                JLabel lblCategoriaProduto = new JLabel("Categoria:");
                lblCategoriaProduto.setFont(new Font("Segoe UI", Font.PLAIN, 12));
                filtrosPanel.add(lblCategoriaProduto);
                JComboBox<String> cbCategoriaProduto = new JComboBox<>(new String[]{
                    "Todas", "Informática", "Periféricos", "Monitores"
                });
                cbCategoriaProduto.setFont(new Font("Segoe UI", Font.PLAIN, 12));
                filtrosPanel.add(cbCategoriaProduto);
                
                JLabel lblEstoqueMin = new JLabel("Estoque:");
                lblEstoqueMin.setFont(new Font("Segoe UI", Font.PLAIN, 12));
                filtrosPanel.add(lblEstoqueMin);
                JComboBox<String> cbEstoqueMin = new JComboBox<>(new String[]{
                    "Todos", "Acima do Mínimo", "Abaixo do Mínimo", "Zerado"
                });
                cbEstoqueMin.setFont(new Font("Segoe UI", Font.PLAIN, 12));
                filtrosPanel.add(cbEstoqueMin);
                
                panel.add(filtrosPanel, gbc);
                
                // Tabela de Produtos
                gbc.gridx = 0; gbc.gridy = 2; gbc.gridwidth = 2;
                gbc.fill = GridBagConstraints.BOTH;
                gbc.weightx = 1.0; gbc.weighty = 1.0;
                
                String[] colunasTabelaProduto = {"Código", "Descrição", "Categoria", "Preço", "Estoque", "Status"};
                Object[][] dadosTabelaProduto = {};
                
                JTable tabelaProduto = new JTable(dadosTabelaProduto, colunasTabelaProduto);
                tabelaProduto.setFont(new Font("Segoe UI", Font.PLAIN, 11));
                tabelaProduto.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 12));
                tabelaProduto.getTableHeader().setBackground(new Color(70, 130, 180));
                tabelaProduto.getTableHeader().setForeground(WHITE);
                tabelaProduto.setRowHeight(25);
                tabelaProduto.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
                tabelaProduto.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
                
                JScrollPane scrollPaneTabelaProduto = new JScrollPane(tabelaProduto);
                scrollPaneTabelaProduto.setPreferredSize(new Dimension(700, 200));
                scrollPaneTabelaProduto.setBorder(BorderFactory.createTitledBorder(
                    BorderFactory.createEtchedBorder(), "Produtos Encontrados",
                    TitledBorder.LEFT, TitledBorder.TOP, new Font("Segoe UI", Font.BOLD, 12),
                    DARK_GRAY));
                panel.add(scrollPaneTabelaProduto, gbc);
                break;
                
            case "🏷️ Fornecedores":
                // Painel de Dados do Fornecedor
                gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
                gbc.fill = GridBagConstraints.HORIZONTAL;
                gbc.insets = new Insets(5, 5, 15, 5);
                
                JPanel dadosPanelFornecedor = new JPanel(new GridBagLayout());
                dadosPanelFornecedor.setBorder(BorderFactory.createTitledBorder(
                    BorderFactory.createEtchedBorder(), "📋 Dados do Fornecedor",
                    TitledBorder.LEFT, TitledBorder.TOP, new Font("Segoe UI", Font.BOLD, 12),
                    DARK_GRAY));
                
                GridBagConstraints gbcDados = new GridBagConstraints();
                gbcDados.insets = new Insets(5, 5, 5, 5);
                
                // Código do Fornecedor
                gbcDados.gridx = 0; gbcDados.gridy = 0;
                gbcDados.fill = GridBagConstraints.HORIZONTAL;
                gbcDados.weightx = 0.3;
                JLabel lblCodigoFornecedor = new JLabel("Código:");
                lblCodigoFornecedor.setFont(new Font("Segoe UI", Font.BOLD, 12));
                lblCodigoFornecedor.setForeground(DARK_GRAY);
                dadosPanelFornecedor.add(lblCodigoFornecedor, gbcDados);
                gbcDados.gridx = 1; gbcDados.gridy = 0;
                gbcDados.weightx = 0.7;
                ElegantTextField txtCodigoFornecedor = new ElegantTextField(15);
                txtCodigoFornecedor.setToolTipText("Código único do fornecedor");
                dadosPanelFornecedor.add(txtCodigoFornecedor, gbcDados);
                
                // Nome/Razão Social
                gbcDados.gridx = 0; gbcDados.gridy = 1;
                gbcDados.weightx = 0.3;
                JLabel lblNomeFornecedor = new JLabel("Razão Social:");
                lblNomeFornecedor.setFont(new Font("Segoe UI", Font.BOLD, 12));
                lblNomeFornecedor.setForeground(DARK_GRAY);
                dadosPanelFornecedor.add(lblNomeFornecedor, gbcDados);
                gbcDados.gridx = 1; gbcDados.gridy = 1;
                gbcDados.weightx = 0.7;
                ElegantTextField txtNomeFornecedor = new ElegantTextField(40);
                txtNomeFornecedor.setToolTipText("Nome completo ou razão social do fornecedor");
                dadosPanelFornecedor.add(txtNomeFornecedor, gbcDados);
                
                // Nome Fantasia
                gbcDados.gridx = 0; gbcDados.gridy = 2;
                gbcDados.weightx = 0.3;
                JLabel lblNomeFantasia = new JLabel("Nome Fantasia:");
                lblNomeFantasia.setFont(new Font("Segoe UI", Font.BOLD, 12));
                lblNomeFantasia.setForeground(DARK_GRAY);
                dadosPanelFornecedor.add(lblNomeFantasia, gbcDados);
                gbcDados.gridx = 1; gbcDados.gridy = 2;
                gbcDados.weightx = 0.7;
                ElegantTextField txtNomeFantasia = new ElegantTextField(40);
                txtNomeFantasia.setToolTipText("Nome fantasia (opcional)");
                dadosPanelFornecedor.add(txtNomeFantasia, gbcDados);
                
                // CNPJ
                gbcDados.gridx = 0; gbcDados.gridy = 3;
                gbcDados.weightx = 0.3;
                JLabel lblCnpjFornecedor = new JLabel("CNPJ:");
                lblCnpjFornecedor.setFont(new Font("Segoe UI", Font.BOLD, 12));
                lblCnpjFornecedor.setForeground(DARK_GRAY);
                dadosPanelFornecedor.add(lblCnpjFornecedor, gbcDados);
                gbcDados.gridx = 1; gbcDados.gridy = 3;
                gbcDados.weightx = 0.7;
                ElegantTextField txtCnpjFornecedor = new ElegantTextField(20);
                txtCnpjFornecedor.setToolTipText("CNPJ do fornecedor (XX.XXX.XXX/XXXX-XX)");
                dadosPanelFornecedor.add(txtCnpjFornecedor, gbcDados);
                
                // Inscrição Estadual
                gbcDados.gridx = 0; gbcDados.gridy = 4;
                gbcDados.weightx = 0.3;
                JLabel lblInscricaoEstadual = new JLabel("Inscrição Estadual:");
                lblInscricaoEstadual.setFont(new Font("Segoe UI", Font.BOLD, 12));
                lblInscricaoEstadual.setForeground(DARK_GRAY);
                dadosPanelFornecedor.add(lblInscricaoEstadual, gbcDados);
                gbcDados.gridx = 1; gbcDados.gridy = 4;
                gbcDados.weightx = 0.7;
                ElegantTextField txtInscricaoEstadual = new ElegantTextField(20);
                txtInscricaoEstadual.setToolTipText("Inscrição estadual (Isento se aplicável)");
                dadosPanelFornecedor.add(txtInscricaoEstadual, gbcDados);
                
                // Telefone
                gbcDados.gridx = 0; gbcDados.gridy = 5;
                gbcDados.weightx = 0.3;
                JLabel lblTelefoneFornecedor = new JLabel("Telefone:");
                lblTelefoneFornecedor.setFont(new Font("Segoe UI", Font.BOLD, 12));
                lblTelefoneFornecedor.setForeground(DARK_GRAY);
                dadosPanelFornecedor.add(lblTelefoneFornecedor, gbcDados);
                gbcDados.gridx = 1; gbcDados.gridy = 5;
                gbcDados.weightx = 0.7;
                ElegantTextField txtTelefoneFornecedor = new ElegantTextField(20);
                txtTelefoneFornecedor.setToolTipText("Telefone principal com DDD");
                dadosPanelFornecedor.add(txtTelefoneFornecedor, gbcDados);
                
                // Email
                gbcDados.gridx = 0; gbcDados.gridy = 6;
                gbcDados.weightx = 0.3;
                JLabel lblEmailFornecedor = new JLabel("Email:");
                lblEmailFornecedor.setFont(new Font("Segoe UI", Font.BOLD, 12));
                lblEmailFornecedor.setForeground(DARK_GRAY);
                dadosPanelFornecedor.add(lblEmailFornecedor, gbcDados);
                gbcDados.gridx = 1; gbcDados.gridy = 6;
                gbcDados.weightx = 0.7;
                ElegantTextField txtEmailFornecedor = new ElegantTextField(40);
                txtEmailFornecedor.setToolTipText("Email para contato e envio de pedidos");
                dadosPanelFornecedor.add(txtEmailFornecedor, gbcDados);
                
                // Endereço
                gbcDados.gridx = 0; gbcDados.gridy = 7;
                gbcDados.weightx = 0.3;
                JLabel lblEnderecoFornecedor = new JLabel("Endereço:");
                lblEnderecoFornecedor.setFont(new Font("Segoe UI", Font.BOLD, 12));
                lblEnderecoFornecedor.setForeground(DARK_GRAY);
                dadosPanelFornecedor.add(lblEnderecoFornecedor, gbcDados);
                gbcDados.gridx = 1; gbcDados.gridy = 7;
                gbcDados.weightx = 0.7;
                ElegantTextField txtEnderecoFornecedor = new ElegantTextField(40);
                txtEnderecoFornecedor.setToolTipText("Endereço completo");
                dadosPanelFornecedor.add(txtEnderecoFornecedor, gbcDados);
                
                // Cidade
                gbcDados.gridx = 0; gbcDados.gridy = 8;
                gbcDados.weightx = 0.3;
                JLabel lblCidadeFornecedor = new JLabel("Cidade:");
                lblCidadeFornecedor.setFont(new Font("Segoe UI", Font.BOLD, 12));
                lblCidadeFornecedor.setForeground(DARK_GRAY);
                dadosPanelFornecedor.add(lblCidadeFornecedor, gbcDados);
                gbcDados.gridx = 1; gbcDados.gridy = 8;
                gbcDados.weightx = 0.7;
                ElegantTextField txtCidadeFornecedor = new ElegantTextField(30);
                txtCidadeFornecedor.setToolTipText("Cidade");
                dadosPanelFornecedor.add(txtCidadeFornecedor, gbcDados);
                
                // Estado
                gbcDados.gridx = 0; gbcDados.gridy = 9;
                gbcDados.weightx = 0.3;
                JLabel lblEstadoFornecedor = new JLabel("Estado:");
                lblEstadoFornecedor.setFont(new Font("Segoe UI", Font.BOLD, 12));
                lblEstadoFornecedor.setForeground(DARK_GRAY);
                dadosPanelFornecedor.add(lblEstadoFornecedor, gbcDados);
                gbcDados.gridx = 1; gbcDados.gridy = 9;
                gbcDados.weightx = 0.7;
                JComboBox<String> cbEstadoFornecedor = new JComboBox<>(new String[]{
                    "AC", "AL", "AP", "AM", "BA", "CE", "DF", "ES", "GO", "MA", 
                    "MT", "MS", "MG", "PA", "PB", "PR", "PE", "PI", "RJ", "RN", 
                    "RS", "RO", "RR", "SC", "SP", "SE", "TO"
                });
                cbEstadoFornecedor.setFont(new Font("Segoe UI", Font.PLAIN, 12));
                dadosPanelFornecedor.add(cbEstadoFornecedor, gbcDados);
                
                // CEP
                gbcDados.gridx = 0; gbcDados.gridy = 10;
                gbcDados.weightx = 0.3;
                JLabel lblCepFornecedor = new JLabel("CEP:");
                lblCepFornecedor.setFont(new Font("Segoe UI", Font.BOLD, 12));
                lblCepFornecedor.setForeground(DARK_GRAY);
                dadosPanelFornecedor.add(lblCepFornecedor, gbcDados);
                gbcDados.gridx = 1; gbcDados.gridy = 10;
                gbcDados.weightx = 0.7;
                ElegantTextField txtCepFornecedor = new ElegantTextField(10);
                txtCepFornecedor.setToolTipText("CEP (XXXXX-XXX)");
                dadosPanelFornecedor.add(txtCepFornecedor, gbcDados);
                
                // Contato
                gbcDados.gridx = 0; gbcDados.gridy = 11;
                gbcDados.weightx = 0.3;
                JLabel lblContatoFornecedor = new JLabel("Contato:");
                lblContatoFornecedor.setFont(new Font("Segoe UI", Font.BOLD, 12));
                lblContatoFornecedor.setForeground(DARK_GRAY);
                dadosPanelFornecedor.add(lblContatoFornecedor, gbcDados);
                gbcDados.gridx = 1; gbcDados.gridy = 11;
                gbcDados.weightx = 0.7;
                ElegantTextField txtContatoFornecedor = new ElegantTextField(40);
                txtContatoFornecedor.setToolTipText("Nome da pessoa de contato");
                dadosPanelFornecedor.add(txtContatoFornecedor, gbcDados);
                
                // Condições de Pagamento
                gbcDados.gridx = 0; gbcDados.gridy = 12;
                gbcDados.weightx = 0.3;
                JLabel lblCondicoesPagamento = new JLabel("Condições de Pagamento:");
                lblCondicoesPagamento.setFont(new Font("Segoe UI", Font.BOLD, 12));
                lblCondicoesPagamento.setForeground(DARK_GRAY);
                dadosPanelFornecedor.add(lblCondicoesPagamento, gbcDados);
                gbcDados.gridx = 1; gbcDados.gridy = 12;
                gbcDados.weightx = 0.7;
                ElegantTextField txtCondicoesPagamento = new ElegantTextField(30);
                txtCondicoesPagamento.setToolTipText("Ex: 30/60/90 dias");
                dadosPanelFornecedor.add(txtCondicoesPagamento, gbcDados);
                
                // Observações
                gbcDados.gridx = 0; gbcDados.gridy = 13;
                gbcDados.gridwidth = 2;
                gbcDados.fill = GridBagConstraints.HORIZONTAL;
                gbcDados.weightx = 1.0;
                JLabel lblObservacoesFornecedor = new JLabel("Observações:");
                lblObservacoesFornecedor.setFont(new Font("Segoe UI", Font.BOLD, 12));
                lblObservacoesFornecedor.setForeground(DARK_GRAY);
                dadosPanelFornecedor.add(lblObservacoesFornecedor, gbcDados);
                
                gbcDados.gridx = 0; gbcDados.gridy = 14;
                gbcDados.fill = GridBagConstraints.HORIZONTAL;
                JTextArea txtObservacoesFornecedor = new JTextArea(3, 50);
                txtObservacoesFornecedor.setToolTipText("Observações adicionais sobre o fornecedor");
                txtObservacoesFornecedor.setFont(new Font("Segoe UI", Font.PLAIN, 12));
                txtObservacoesFornecedor.setLineWrap(true);
                txtObservacoesFornecedor.setWrapStyleWord(true);
                txtObservacoesFornecedor.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(MEDIUM_GRAY, 1),
                    BorderFactory.createEmptyBorder(5, 5, 5, 5)
                ));
                JScrollPane scrollObservacoesFornecedor = new JScrollPane(txtObservacoesFornecedor);
                scrollObservacoesFornecedor.setBorder(BorderFactory.createLineBorder(MEDIUM_GRAY, 1));
                dadosPanelFornecedor.add(scrollObservacoesFornecedor, gbcDados);
                
                panel.add(dadosPanelFornecedor, gbc);
                
                // Painel de Busca
                gbc.gridx = 0; gbc.gridy = 1; gbc.gridwidth = 2;
                gbc.fill = GridBagConstraints.HORIZONTAL;
                gbc.insets = new Insets(5, 5, 15, 5);
                
                JPanel buscaPanelFornecedor = new JPanel(new GridBagLayout());
                buscaPanelFornecedor.setBorder(BorderFactory.createTitledBorder(
                    BorderFactory.createEtchedBorder(), "🔍 Consultar Fornecedores",
                    TitledBorder.LEFT, TitledBorder.TOP, new Font("Segoe UI", Font.BOLD, 12),
                    DARK_GRAY));
                
                GridBagConstraints gbcBuscaFornecedor = new GridBagConstraints();
                gbcBuscaFornecedor.insets = new Insets(5, 5, 5, 5);
                
                // Campo de busca
                gbcBuscaFornecedor.gridx = 0; gbcBuscaFornecedor.gridy = 0;
                gbcBuscaFornecedor.fill = GridBagConstraints.HORIZONTAL;
                gbcBuscaFornecedor.weightx = 1.0;
                JLabel lblBuscaFornecedor = new JLabel("Buscar:");
                lblBuscaFornecedor.setFont(new Font("Segoe UI", Font.BOLD, 12));
                lblBuscaFornecedor.setForeground(DARK_GRAY);
                buscaPanelFornecedor.add(lblBuscaFornecedor, gbcBuscaFornecedor);
                
                gbcBuscaFornecedor.gridx = 1; gbcBuscaFornecedor.gridy = 0;
                gbcBuscaFornecedor.fill = GridBagConstraints.HORIZONTAL;
                gbcBuscaFornecedor.weightx = 2.0;
                ElegantTextField txtBuscaFornecedor = new ElegantTextField(30);
                txtBuscaFornecedor.setToolTipText("Digite código, nome ou CNPJ do fornecedor");
                buscaPanelFornecedor.add(txtBuscaFornecedor, gbcBuscaFornecedor);
                
                // Botões de busca
                gbcBuscaFornecedor.gridx = 2; gbcBuscaFornecedor.gridy = 0;
                gbcBuscaFornecedor.fill = GridBagConstraints.NONE;
                gbcBuscaFornecedor.weightx = 0;
                ElegantButton btnBuscarFornecedor = new ElegantButton("🔍 Buscar", PRIMARY_COLOR, false);
                btnBuscarFornecedor.setForeground(WHITE);
                buscaPanelFornecedor.add(btnBuscarFornecedor, gbcBuscaFornecedor);
                
                gbcBuscaFornecedor.gridx = 3; gbcBuscaFornecedor.gridy = 0;
                ElegantButton btnLimparBuscaFornecedor = new ElegantButton("🔄 Limpar", MEDIUM_GRAY, false);
                btnLimparBuscaFornecedor.setForeground(WHITE);
                buscaPanelFornecedor.add(btnLimparBuscaFornecedor, gbcBuscaFornecedor);
                
                panel.add(buscaPanelFornecedor, gbc);
                
                // Resetar gridwidth para campos abaixo
                gbc.gridwidth = 1;
                gbc.insets = new Insets(5, 5, 5, 5);
                
                // Filtros adicionais
                gbc.gridx = 0; gbc.gridy = 2;
                gbc.fill = GridBagConstraints.NONE;
                JLabel lblFiltrosFornecedor = new JLabel("Filtros:");
                lblFiltrosFornecedor.setFont(new Font("Segoe UI", Font.BOLD, 12));
                lblFiltrosFornecedor.setForeground(DARK_GRAY);
                panel.add(lblFiltrosFornecedor, gbc);
                
                gbc.gridx = 1; gbc.gridy = 2;
                gbc.fill = GridBagConstraints.HORIZONTAL;
                JPanel filtrosPanelFornecedor = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
                
                JLabel lblEstadoFiltro = new JLabel("Estado:");
                lblEstadoFiltro.setFont(new Font("Segoe UI", Font.PLAIN, 12));
                filtrosPanelFornecedor.add(lblEstadoFiltro);
                JComboBox<String> cbEstadoFiltro = new JComboBox<>(new String[]{
                    "Todos", "SP", "RJ", "MG", "RS", "PR", "SC", "Outros"
                });
                cbEstadoFiltro.setFont(new Font("Segoe UI", Font.PLAIN, 12));
                filtrosPanelFornecedor.add(cbEstadoFiltro);
                
                JLabel lblStatusFornecedor = new JLabel("Status:");
                lblStatusFornecedor.setFont(new Font("Segoe UI", Font.PLAIN, 12));
                filtrosPanelFornecedor.add(lblStatusFornecedor);
                JComboBox<String> cbStatusFornecedor = new JComboBox<>(new String[]{
                    "Todos", "Ativo", "Inativo", "Bloqueado"
                });
                cbStatusFornecedor.setFont(new Font("Segoe UI", Font.PLAIN, 12));
                filtrosPanelFornecedor.add(cbStatusFornecedor);
                
                panel.add(filtrosPanelFornecedor, gbc);
                
                // Tabela de Fornecedores
                gbc.gridx = 0; gbc.gridy = 3; gbc.gridwidth = 2;
                gbc.fill = GridBagConstraints.BOTH;
                gbc.weightx = 1.0; gbc.weighty = 1.0;
                
                String[] colunasTabelaFornecedor = {"Código", "Razão Social", "CNPJ", "Telefone", "Cidade", "Estado", "Status"};
                Object[][] dadosTabelaFornecedor = {};
                
                JTable tabelaFornecedor = new JTable(dadosTabelaFornecedor, colunasTabelaFornecedor);
                tabelaFornecedor.setFont(new Font("Segoe UI", Font.PLAIN, 11));
                tabelaFornecedor.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 12));
                tabelaFornecedor.getTableHeader().setBackground(new Color(70, 130, 180));
                tabelaFornecedor.getTableHeader().setForeground(WHITE);
                tabelaFornecedor.setRowHeight(25);
                tabelaFornecedor.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
                tabelaFornecedor.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
                
                JScrollPane scrollPaneTabelaFornecedor = new JScrollPane(tabelaFornecedor);
                scrollPaneTabelaFornecedor.setPreferredSize(new Dimension(700, 200));
                scrollPaneTabelaFornecedor.setBorder(BorderFactory.createTitledBorder(
                    BorderFactory.createEtchedBorder(), "Fornecedores Encontrados",
                    TitledBorder.LEFT, TitledBorder.TOP, new Font("Segoe UI", Font.BOLD, 12),
                    DARK_GRAY));
                panel.add(scrollPaneTabelaFornecedor, gbc);
                break;
                
            case "📂 Categorias":
                // Painel de Dados da Categoria
                gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
                gbc.fill = GridBagConstraints.HORIZONTAL;
                gbc.insets = new Insets(5, 5, 15, 5);
                
                JPanel dadosPanelCategoria = new JPanel(new GridBagLayout());
                dadosPanelCategoria.setBorder(BorderFactory.createTitledBorder(
                    BorderFactory.createEtchedBorder(), "📂 Dados da Categoria",
                    TitledBorder.LEFT, TitledBorder.TOP, new Font("Segoe UI", Font.BOLD, 12),
                    DARK_GRAY));
                
                GridBagConstraints gbcDadosCategoria = new GridBagConstraints();
                gbcDadosCategoria.insets = new Insets(5, 5, 5, 5);
                
                // Código da Categoria
                gbcDadosCategoria.gridx = 0; gbcDadosCategoria.gridy = 0;
                gbcDadosCategoria.fill = GridBagConstraints.HORIZONTAL;
                gbcDadosCategoria.weightx = 0.3;
                JLabel lblCodigoCategoria = new JLabel("Código:");
                lblCodigoCategoria.setFont(new Font("Segoe UI", Font.BOLD, 12));
                lblCodigoCategoria.setForeground(DARK_GRAY);
                dadosPanelCategoria.add(lblCodigoCategoria, gbcDadosCategoria);
                gbcDadosCategoria.gridx = 1; gbcDadosCategoria.gridy = 0;
                gbcDadosCategoria.weightx = 0.7;
                ElegantTextField txtCodigoCategoria = new ElegantTextField(15);
                txtCodigoCategoria.setToolTipText("Código único da categoria");
                dadosPanelCategoria.add(txtCodigoCategoria, gbcDadosCategoria);
                
                // Nome da Categoria
                gbcDadosCategoria.gridx = 0; gbcDadosCategoria.gridy = 1;
                gbcDadosCategoria.weightx = 0.3;
                JLabel lblNomeCategoria = new JLabel("Nome da Categoria:");
                lblNomeCategoria.setFont(new Font("Segoe UI", Font.BOLD, 12));
                lblNomeCategoria.setForeground(DARK_GRAY);
                dadosPanelCategoria.add(lblNomeCategoria, gbcDadosCategoria);
                gbcDadosCategoria.gridx = 1; gbcDadosCategoria.gridy = 1;
                gbcDadosCategoria.weightx = 0.7;
                ElegantTextField txtNomeCategoria = new ElegantTextField(40);
                txtNomeCategoria.setToolTipText("Nome completo da categoria");
                dadosPanelCategoria.add(txtNomeCategoria, gbcDadosCategoria);
                
                // Descrição
                gbcDadosCategoria.gridx = 0; gbcDadosCategoria.gridy = 2;
                gbcDadosCategoria.weightx = 0.3;
                JLabel lblDescricaoCategoria = new JLabel("Descrição:");
                lblDescricaoCategoria.setFont(new Font("Segoe UI", Font.BOLD, 12));
                lblDescricaoCategoria.setForeground(DARK_GRAY);
                dadosPanelCategoria.add(lblDescricaoCategoria, gbcDadosCategoria);
                gbcDadosCategoria.gridx = 1; gbcDadosCategoria.gridy = 2;
                gbcDadosCategoria.weightx = 0.7;
                JTextArea txtDescricaoAreaCategoria = new JTextArea(3, 40);
                txtDescricaoAreaCategoria.setToolTipText("Descrição detalhada da categoria");
                txtDescricaoAreaCategoria.setFont(new Font("Segoe UI", Font.PLAIN, 12));
                txtDescricaoAreaCategoria.setLineWrap(true);
                txtDescricaoAreaCategoria.setWrapStyleWord(true);
                txtDescricaoAreaCategoria.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(MEDIUM_GRAY, 1),
                    BorderFactory.createEmptyBorder(5, 8, 5, 8)
                ));
                JScrollPane scrollDescricaoCategoria = new JScrollPane(txtDescricaoAreaCategoria);
                scrollDescricaoCategoria.setBorder(BorderFactory.createEmptyBorder());
                scrollDescricaoCategoria.setOpaque(false);
                scrollDescricaoCategoria.getViewport().setOpaque(false);
                dadosPanelCategoria.add(scrollDescricaoCategoria, gbcDadosCategoria);
                
                // Categoria Pai
                gbcDadosCategoria.gridx = 0; gbcDadosCategoria.gridy = 3;
                gbcDadosCategoria.weightx = 0.3;
                JLabel lblCategoriaPai = new JLabel("Categoria Pai:");
                lblCategoriaPai.setFont(new Font("Segoe UI", Font.BOLD, 12));
                lblCategoriaPai.setForeground(DARK_GRAY);
                dadosPanelCategoria.add(lblCategoriaPai, gbcDadosCategoria);
                gbcDadosCategoria.gridx = 1; gbcDadosCategoria.gridy = 3;
                gbcDadosCategoria.weightx = 0.7;
                JComboBox<String> cbCategoriaPai = new JComboBox<>(new String[]{
                    "(Nenhuma)", "Eletrônicos", "Roupas", "Alimentos", "Móveis", "Livros", "Brinquedos"
                });
                cbCategoriaPai.setFont(new Font("Segoe UI", Font.PLAIN, 12));
                cbCategoriaPai.setToolTipText("Categoria pai (para subcategorias)");
                dadosPanelCategoria.add(cbCategoriaPai, gbcDadosCategoria);
                
                // Status
                gbcDadosCategoria.gridx = 0; gbcDadosCategoria.gridy = 4;
                gbcDadosCategoria.weightx = 0.3;
                JLabel lblStatusCategoria = new JLabel("Status:");
                lblStatusCategoria.setFont(new Font("Segoe UI", Font.BOLD, 12));
                lblStatusCategoria.setForeground(DARK_GRAY);
                dadosPanelCategoria.add(lblStatusCategoria, gbcDadosCategoria);
                gbcDadosCategoria.gridx = 1; gbcDadosCategoria.gridy = 4;
                gbcDadosCategoria.weightx = 0.7;
                JComboBox<String> cbStatusCategoria = new JComboBox<>(new String[]{
                    "Ativa", "Inativa", "Em Manutenção"
                });
                cbStatusCategoria.setFont(new Font("Segoe UI", Font.PLAIN, 12));
                cbStatusCategoria.setToolTipText("Status atual da categoria");
                dadosPanelCategoria.add(cbStatusCategoria, gbcDadosCategoria);
                
                // Margem de Lucro
                gbcDadosCategoria.gridx = 0; gbcDadosCategoria.gridy = 5;
                gbcDadosCategoria.weightx = 0.3;
                JLabel lblMargemLucro = new JLabel("Margem de Lucro (%):");
                lblMargemLucro.setFont(new Font("Segoe UI", Font.BOLD, 12));
                lblMargemLucro.setForeground(DARK_GRAY);
                dadosPanelCategoria.add(lblMargemLucro, gbcDadosCategoria);
                gbcDadosCategoria.gridx = 1; gbcDadosCategoria.gridy = 5;
                gbcDadosCategoria.weightx = 0.7;
                ElegantTextField txtMargemLucro = new ElegantTextField(10);
                txtMargemLucro.setToolTipText("Margem de lucro padrão para produtos desta categoria");
                dadosPanelCategoria.add(txtMargemLucro, gbcDadosCategoria);
                
                // Cor de Identificação
                gbcDadosCategoria.gridx = 0; gbcDadosCategoria.gridy = 6;
                gbcDadosCategoria.weightx = 0.3;
                JLabel lblCorIdentificacao = new JLabel("Cor de Identificação:");
                lblCorIdentificacao.setFont(new Font("Segoe UI", Font.BOLD, 12));
                lblCorIdentificacao.setForeground(DARK_GRAY);
                dadosPanelCategoria.add(lblCorIdentificacao, gbcDadosCategoria);
                gbcDadosCategoria.gridx = 1; gbcDadosCategoria.gridy = 6;
                gbcDadosCategoria.weightx = 0.7;
                JPanel corPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
                ElegantTextField txtCorHex = new ElegantTextField(8);
                txtCorHex.setToolTipText("Código hexadecimal da cor (ex: #FF5733)");
                txtCorHex.setText("#FF5733");
                ElegantButton btnCor = new ElegantButton("    ", new Color(255, 87, 51), false);
                btnCor.setToolTipText("Clique para selecionar uma cor");
                btnCor.addActionListener(e -> {
                    // Simulação de seletor de cores
                    String[] cores = {"#FF5733", "#33FF57", "#3357FF", "#FF33F5", "#F5FF33", "#33FFF5"};
                    String corSelecionada = cores[(int)(Math.random() * cores.length)];
                    txtCorHex.setText(corSelecionada);
                    try {
                        btnCor.setBackground(Color.decode(corSelecionada));
                    } catch (Exception ex) {
                        SystemLogger.error("Erro ao definir cor", ex);
                    }
                });
                corPanel.add(txtCorHex);
                corPanel.add(btnCor);
                dadosPanelCategoria.add(corPanel, gbcDadosCategoria);
                
                panel.add(dadosPanelCategoria, gbc);
                
                // Painel de Busca
                gbc.gridx = 0; gbc.gridy = 1; gbc.gridwidth = 2;
                gbc.fill = GridBagConstraints.HORIZONTAL;
                gbc.insets = new Insets(5, 5, 15, 5);
                
                JPanel buscaPanelCategoria = new JPanel(new GridBagLayout());
                buscaPanelCategoria.setBorder(BorderFactory.createTitledBorder(
                    BorderFactory.createEtchedBorder(), "🔍 Consultar Categorias",
                    TitledBorder.LEFT, TitledBorder.TOP, new Font("Segoe UI", Font.BOLD, 12),
                    DARK_GRAY));
                
                GridBagConstraints gbcBuscaCategoria = new GridBagConstraints();
                gbcBuscaCategoria.insets = new Insets(5, 5, 5, 5);
                
                // Campo de busca
                gbcBuscaCategoria.gridx = 0; gbcBuscaCategoria.gridy = 0;
                gbcBuscaCategoria.fill = GridBagConstraints.HORIZONTAL;
                gbcBuscaCategoria.weightx = 1.0;
                JLabel lblBuscaCategoria = new JLabel("Buscar:");
                lblBuscaCategoria.setFont(new Font("Segoe UI", Font.BOLD, 12));
                lblBuscaCategoria.setForeground(DARK_GRAY);
                buscaPanelCategoria.add(lblBuscaCategoria, gbcBuscaCategoria);
                
                gbcBuscaCategoria.gridx = 1; gbcBuscaCategoria.gridy = 0;
                gbcBuscaCategoria.fill = GridBagConstraints.HORIZONTAL;
                gbcBuscaCategoria.weightx = 2.0;
                ElegantTextField txtBuscaCategoria = new ElegantTextField(30);
                txtBuscaCategoria.setToolTipText("Digite código, nome ou descrição da categoria");
                buscaPanelCategoria.add(txtBuscaCategoria, gbcBuscaCategoria);
                
                // Botões de busca
                gbcBuscaCategoria.gridx = 2; gbcBuscaCategoria.gridy = 0;
                gbcBuscaCategoria.fill = GridBagConstraints.NONE;
                gbcBuscaCategoria.weightx = 0;
                ElegantButton btnBuscarCategoria = new ElegantButton("🔍 Buscar", PRIMARY_COLOR, false);
                btnBuscarCategoria.setForeground(WHITE);
                buscaPanelCategoria.add(btnBuscarCategoria, gbcBuscaCategoria);
                
                gbcBuscaCategoria.gridx = 3; gbcBuscaCategoria.gridy = 0;
                ElegantButton btnLimparBuscaCategoria = new ElegantButton("🔄 Limpar", MEDIUM_GRAY, false);
                btnLimparBuscaCategoria.setForeground(WHITE);
                buscaPanelCategoria.add(btnLimparBuscaCategoria, gbcBuscaCategoria);
                
                panel.add(buscaPanelCategoria, gbc);
                
                // Resetar gridwidth para campos abaixo
                gbc.gridwidth = 1;
                gbc.insets = new Insets(5, 5, 5, 5);
                
                // Filtros adicionais
                gbc.gridx = 0; gbc.gridy = 2;
                gbc.fill = GridBagConstraints.NONE;
                JLabel lblFiltrosCategoria = new JLabel("Filtros:");
                lblFiltrosCategoria.setFont(new Font("Segoe UI", Font.BOLD, 12));
                lblFiltrosCategoria.setForeground(DARK_GRAY);
                panel.add(lblFiltrosCategoria, gbc);
                
                gbc.gridx = 1; gbc.gridy = 2;
                gbc.fill = GridBagConstraints.HORIZONTAL;
                JPanel filtrosPanelCategoria = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
                
                JLabel lblStatusFiltro = new JLabel("Status:");
                lblStatusFiltro.setFont(new Font("Segoe UI", Font.PLAIN, 12));
                filtrosPanelCategoria.add(lblStatusFiltro);
                JComboBox<String> cbStatusFiltro = new JComboBox<>(new String[]{
                    "Todos", "Ativa", "Inativa", "Em Manutenção"
                });
                cbStatusFiltro.setFont(new Font("Segoe UI", Font.PLAIN, 12));
                filtrosPanelCategoria.add(cbStatusFiltro);
                
                JLabel lblTipoFiltro = new JLabel("Tipo:");
                lblTipoFiltro.setFont(new Font("Segoe UI", Font.PLAIN, 12));
                filtrosPanelCategoria.add(lblTipoFiltro);
                JComboBox<String> cbTipoFiltro = new JComboBox<>(new String[]{
                    "Todas", "Principais", "Subcategorias"
                });
                cbTipoFiltro.setFont(new Font("Segoe UI", Font.PLAIN, 12));
                filtrosPanelCategoria.add(cbTipoFiltro);
                
                panel.add(filtrosPanelCategoria, gbc);
                
                // Tabela de Categorias
                gbc.gridx = 0; gbc.gridy = 3; gbc.gridwidth = 2;
                gbc.fill = GridBagConstraints.BOTH;
                gbc.weightx = 1.0; gbc.weighty = 1.0;
                
                String[] colunasTabelaCategoria = {"Código", "Nome", "Categoria Pai", "Margem Lucro", "Status", "Produtos"};
                Object[][] dadosTabelaCategoria = {};
                
                JTable tabelaCategoria = new JTable(dadosTabelaCategoria, colunasTabelaCategoria);
                tabelaCategoria.setFont(new Font("Segoe UI", Font.PLAIN, 11));
                tabelaCategoria.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 12));
                tabelaCategoria.getTableHeader().setBackground(new Color(70, 130, 180));
                tabelaCategoria.getTableHeader().setForeground(WHITE);
                tabelaCategoria.setRowHeight(25);
                tabelaCategoria.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
                tabelaCategoria.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
                
                JScrollPane scrollPaneTabelaCategoria = new JScrollPane(tabelaCategoria);
                scrollPaneTabelaCategoria.setPreferredSize(new Dimension(700, 200));
                scrollPaneTabelaCategoria.setBorder(BorderFactory.createTitledBorder(
                    BorderFactory.createEtchedBorder(), "Categorias Encontradas",
                    TitledBorder.LEFT, TitledBorder.TOP, new Font("Segoe UI", Font.BOLD, 12),
                    DARK_GRAY));
                panel.add(scrollPaneTabelaCategoria, gbc);
                break;
                
            default:
                // Log para formulário padrão Vendas
                SystemLogger.ui("📋 FORMULÁRIO PADRÃO VENDAS - Item não reconhecido: " + item);
                SystemLogger.info("VENDAS - Criando formulário padrão para: " + item);
                return criarFormularioPadraoElegante(item, "PRODUTOS", gbc);
        }
        
        // Log de conclusão do formulário Vendas
        SystemLogger.ui("✅ FORMULÁRIO VENDAS CONCLUÍDO - " + item);
        SystemLogger.info("VENDAS - Formulário criado com sucesso: " + item);
        
        return panel;
    }
    
    /**
     * Formulários Clientes elegantes
     */
    private JPanel criarFormularioClientesElegante(String item, GridBagConstraints gbc) {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setOpaque(false);
        
        switch (item) {
            case "➕ Novo Cliente":
                // Nome
                gbc.gridx = 0; gbc.gridy = 0;
                JLabel lblNome = new JLabel("Nome Completo:");
                lblNome.setFont(new Font("Segoe UI", Font.BOLD, 12));
                lblNome.setForeground(DARK_GRAY);
                panel.add(lblNome, gbc);
                gbc.gridx = 1; gbc.gridy = 0;
                ElegantTextField txtNome = new ElegantTextField(40);
                panel.add(txtNome, gbc);
                
                // CPF/CNPJ
                gbc.gridx = 0; gbc.gridy = 1;
                JLabel lblCpfCnpj = new JLabel("CPF/CNPJ:");
                lblCpfCnpj.setFont(new Font("Segoe UI", Font.BOLD, 12));
                lblCpfCnpj.setForeground(DARK_GRAY);
                panel.add(lblCpfCnpj, gbc);
                gbc.gridx = 1; gbc.gridy = 1;
                ElegantTextField txtCpfCnpj = new ElegantTextField(20);
                panel.add(txtCpfCnpj, gbc);
                
                // Tipo de Pessoa
                gbc.gridx = 0; gbc.gridy = 2;
                JLabel lblTipo = new JLabel("Tipo:");
                lblTipo.setFont(new Font("Segoe UI", Font.BOLD, 12));
                lblTipo.setForeground(DARK_GRAY);
                panel.add(lblTipo, gbc);
                gbc.gridx = 1; gbc.gridy = 2;
                JComboBox<String> cbTipo = new JComboBox<>(new String[]{"Pessoa Física", "Pessoa Jurídica"});
                cbTipo.setFont(new Font("Segoe UI", Font.PLAIN, 12));
                panel.add(cbTipo, gbc);
                
                // Email
                gbc.gridx = 0; gbc.gridy = 3;
                JLabel lblEmail = new JLabel("Email:");
                lblEmail.setFont(new Font("Segoe UI", Font.BOLD, 12));
                lblEmail.setForeground(DARK_GRAY);
                panel.add(lblEmail, gbc);
                gbc.gridx = 1; gbc.gridy = 3;
                ElegantTextField txtEmail = new ElegantTextField(40);
                panel.add(txtEmail, gbc);
                
                // Telefone
                gbc.gridx = 0; gbc.gridy = 4;
                JLabel lblTelefone = new JLabel("Telefone:");
                lblTelefone.setFont(new Font("Segoe UI", Font.BOLD, 12));
                lblTelefone.setForeground(DARK_GRAY);
                panel.add(lblTelefone, gbc);
                gbc.gridx = 1; gbc.gridy = 4;
                ElegantTextField txtTelefone = new ElegantTextField(20);
                panel.add(txtTelefone, gbc);
                
                // Endereço
                gbc.gridx = 0; gbc.gridy = 5;
                JLabel lblEndereco = new JLabel("Endereço:");
                lblEndereco.setFont(new Font("Segoe UI", Font.BOLD, 12));
                lblEndereco.setForeground(DARK_GRAY);
                panel.add(lblEndereco, gbc);
                gbc.gridx = 1; gbc.gridy = 5;
                ElegantTextField txtEndereco = new ElegantTextField(40);
                panel.add(txtEndereco, gbc);
                
                // Bairro
                gbc.gridx = 0; gbc.gridy = 6;
                JLabel lblBairro = new JLabel("Bairro:");
                lblBairro.setFont(new Font("Segoe UI", Font.BOLD, 12));
                lblBairro.setForeground(DARK_GRAY);
                panel.add(lblBairro, gbc);
                gbc.gridx = 1; gbc.gridy = 6;
                ElegantTextField txtBairro = new ElegantTextField(30);
                panel.add(txtBairro, gbc);
                
                // Cidade
                gbc.gridx = 0; gbc.gridy = 7;
                JLabel lblCidade = new JLabel("Cidade:");
                lblCidade.setFont(new Font("Segoe UI", Font.BOLD, 12));
                lblCidade.setForeground(DARK_GRAY);
                panel.add(lblCidade, gbc);
                gbc.gridx = 1; gbc.gridy = 7;
                ElegantTextField txtCidade = new ElegantTextField(30);
                panel.add(txtCidade, gbc);
                
                // Estado
                gbc.gridx = 0; gbc.gridy = 8;
                JLabel lblEstado = new JLabel("Estado:");
                lblEstado.setFont(new Font("Segoe UI", Font.BOLD, 12));
                lblEstado.setForeground(DARK_GRAY);
                panel.add(lblEstado, gbc);
                gbc.gridx = 1; gbc.gridy = 8;
                JComboBox<String> cbEstado = new JComboBox<>(new String[]{"AC", "AL", "AP", "AM", "BA", "CE", "DF", "ES", "GO", "MA", "MT", "MS", "MG", "PA", "PB", "PR", "PE", "PI", "RJ", "RN", "RS", "RO", "RR", "SC", "SP", "SE", "TO"});
                cbEstado.setFont(new Font("Segoe UI", Font.PLAIN, 12));
                panel.add(cbEstado, gbc);
                
                // CEP
                gbc.gridx = 0; gbc.gridy = 9;
                JLabel lblCep = new JLabel("CEP:");
                lblCep.setFont(new Font("Segoe UI", Font.BOLD, 12));
                lblCep.setForeground(DARK_GRAY);
                panel.add(lblCep, gbc);
                gbc.gridx = 1; gbc.gridy = 9;
                ElegantTextField txtCep = new ElegantTextField(10);
                panel.add(txtCep, gbc);
                break;
                
            case "🔍 Consultar Cliente":
                // Painel de Busca
                gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
                gbc.fill = GridBagConstraints.HORIZONTAL;
                gbc.insets = new Insets(5, 5, 15, 5);
                
                JPanel buscaPanelCliente = new JPanel(new GridBagLayout());
                buscaPanelCliente.setBorder(BorderFactory.createTitledBorder(
                    BorderFactory.createEtchedBorder(), "🔍 Consultar Clientes",
                    TitledBorder.LEFT, TitledBorder.TOP, new Font("Segoe UI", Font.BOLD, 12),
                    DARK_GRAY));
                
                GridBagConstraints gbcBuscaCliente = new GridBagConstraints();
                gbcBuscaCliente.insets = new Insets(5, 5, 5, 5);
                
                // Campo de busca
                gbcBuscaCliente.gridx = 0; gbcBuscaCliente.gridy = 0;
                gbcBuscaCliente.fill = GridBagConstraints.HORIZONTAL;
                gbcBuscaCliente.weightx = 1.0;
                JLabel lblBuscaCliente = new JLabel("Buscar:");
                lblBuscaCliente.setFont(new Font("Segoe UI", Font.BOLD, 12));
                lblBuscaCliente.setForeground(DARK_GRAY);
                buscaPanelCliente.add(lblBuscaCliente, gbcBuscaCliente);
                
                gbcBuscaCliente.gridx = 1; gbcBuscaCliente.gridy = 0;
                gbcBuscaCliente.fill = GridBagConstraints.HORIZONTAL;
                gbcBuscaCliente.weightx = 2.0;
                ElegantTextField txtBuscaCliente = new ElegantTextField(30);
                txtBuscaCliente.setToolTipText("Digite nome, CPF/CNPJ ou email do cliente");
                buscaPanelCliente.add(txtBuscaCliente, gbcBuscaCliente);
                
                // Botões de busca
                gbcBuscaCliente.gridx = 2; gbcBuscaCliente.gridy = 0;
                gbcBuscaCliente.fill = GridBagConstraints.NONE;
                gbcBuscaCliente.weightx = 0;
                ElegantButton btnBuscarCliente = new ElegantButton("🔍 Buscar", PRIMARY_COLOR, false);
                btnBuscarCliente.setForeground(WHITE);
                buscaPanelCliente.add(btnBuscarCliente, gbcBuscaCliente);
                
                gbcBuscaCliente.gridx = 3; gbcBuscaCliente.gridy = 0;
                ElegantButton btnLimparBuscaCliente = new ElegantButton("🔄 Limpar", MEDIUM_GRAY, false);
                btnLimparBuscaCliente.setForeground(WHITE);
                buscaPanelCliente.add(btnLimparBuscaCliente, gbcBuscaCliente);
                
                panel.add(buscaPanelCliente, gbc);
                
                // Resetar gridwidth para campos abaixo
                gbc.gridwidth = 1;
                gbc.insets = new Insets(5, 5, 5, 5);
                
                // Filtros de busca
                gbc.gridx = 0; gbc.gridy = 1;
                gbc.fill = GridBagConstraints.NONE;
                JLabel lblFiltrosCliente = new JLabel("Filtros:");
                lblFiltrosCliente.setFont(new Font("Segoe UI", Font.BOLD, 12));
                lblFiltrosCliente.setForeground(DARK_GRAY);
                panel.add(lblFiltrosCliente, gbc);
                
                gbc.gridx = 1; gbc.gridy = 1;
                gbc.fill = GridBagConstraints.HORIZONTAL;
                JPanel filtrosPanelCliente = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
                
                JLabel lblStatusCliente = new JLabel("Status:");
                lblStatusCliente.setFont(new Font("Segoe UI", Font.PLAIN, 12));
                filtrosPanelCliente.add(lblStatusCliente);
                JComboBox<String> cbStatusCliente = new JComboBox<>(new String[]{
                    "Todos", "Ativo", "Inativo", "Bloqueado"
                });
                cbStatusCliente.setFont(new Font("Segoe UI", Font.PLAIN, 12));
                filtrosPanelCliente.add(cbStatusCliente);
                
                JLabel lblTipoCliente = new JLabel("Tipo:");
                lblTipoCliente.setFont(new Font("Segoe UI", Font.PLAIN, 12));
                filtrosPanelCliente.add(lblTipoCliente);
                JComboBox<String> cbTipoCliente = new JComboBox<>(new String[]{
                    "Todos", "Pessoa Física", "Pessoa Jurídica"
                });
                cbTipoCliente.setFont(new Font("Segoe UI", Font.PLAIN, 12));
                filtrosPanelCliente.add(cbTipoCliente);
                
                panel.add(filtrosPanelCliente, gbc);
                
                // Tabela de Clientes
                gbc.gridx = 0; gbc.gridy = 2; gbc.gridwidth = 2;
                gbc.fill = GridBagConstraints.BOTH;
                gbc.weightx = 1.0; gbc.weighty = 1.0;
                
                String[] colunasTabelaCliente = {"Código", "Nome", "CPF/CNPJ", "Telefone", "Email", "Status", "Cadastro"};
                Object[][] dadosTabelaCliente = {};
                
                JTable tabelaCliente = new JTable(dadosTabelaCliente, colunasTabelaCliente);
                tabelaCliente.setFont(new Font("Segoe UI", Font.PLAIN, 11));
                tabelaCliente.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 12));
                tabelaCliente.getTableHeader().setBackground(new Color(70, 130, 180));
                tabelaCliente.getTableHeader().setForeground(WHITE);
                tabelaCliente.setRowHeight(25);
                tabelaCliente.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
                tabelaCliente.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
                
                JScrollPane scrollPaneTabelaCliente = new JScrollPane(tabelaCliente);
                scrollPaneTabelaCliente.setPreferredSize(new Dimension(700, 200));
                scrollPaneTabelaCliente.setBorder(BorderFactory.createTitledBorder(
                    BorderFactory.createEtchedBorder(), "Clientes Encontrados",
                    TitledBorder.LEFT, TitledBorder.TOP, new Font("Segoe UI", Font.BOLD, 12),
                    DARK_GRAY));
                panel.add(scrollPaneTabelaCliente, gbc);
                break;
                
            default:
                return criarFormularioPadraoElegante(item, "CLIENTES", gbc);
        }
        
        return panel;
    }
    
    /**
     * Formulários Estoque elegantes
     */
    private JPanel criarFormularioEstoqueElegante(String item, GridBagConstraints gbc) {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setOpaque(false);
        
        switch (item) {
            case "➕ Entrada de Mercadoria":
                // Produto
                gbc.gridx = 0; gbc.gridy = 0;
                JLabel lblProduto = new JLabel("Produto:");
                lblProduto.setFont(new Font("Segoe UI", Font.BOLD, 12));
                lblProduto.setForeground(DARK_GRAY);
                panel.add(lblProduto, gbc);
                gbc.gridx = 1; gbc.gridy = 0;
                ElegantTextField txtProduto = new ElegantTextField(30);
                panel.add(txtProduto, gbc);
                
                // Quantidade
                gbc.gridx = 0; gbc.gridy = 1;
                JLabel lblQuantidade = new JLabel("Quantidade:");
                lblQuantidade.setFont(new Font("Segoe UI", Font.BOLD, 12));
                lblQuantidade.setForeground(DARK_GRAY);
                panel.add(lblQuantidade, gbc);
                gbc.gridx = 1; gbc.gridy = 1;
                ElegantTextField txtQuantidade = new ElegantTextField(10);
                panel.add(txtQuantidade, gbc);
                
                // Data da Entrada
                gbc.gridx = 0; gbc.gridy = 2;
                JLabel lblData = new JLabel("Data:");
                lblData.setFont(new Font("Segoe UI", Font.BOLD, 12));
                lblData.setForeground(DARK_GRAY);
                panel.add(lblData, gbc);
                gbc.gridx = 1; gbc.gridy = 2;
                ElegantTextField txtData = new ElegantTextField(10);
                panel.add(txtData, gbc);
                
                // Fornecedor
                gbc.gridx = 0; gbc.gridy = 3;
                JLabel lblFornecedor = new JLabel("Fornecedor:");
                lblFornecedor.setFont(new Font("Segoe UI", Font.BOLD, 12));
                lblFornecedor.setForeground(DARK_GRAY);
                panel.add(lblFornecedor, gbc);
                gbc.gridx = 1; gbc.gridy = 3;
                ElegantTextField txtFornecedor = new ElegantTextField(30);
                panel.add(txtFornecedor, gbc);
                
                // Nota Fiscal
                gbc.gridx = 0; gbc.gridy = 4;
                JLabel lblNotaFiscal = new JLabel("Nota Fiscal:");
                lblNotaFiscal.setFont(new Font("Segoe UI", Font.BOLD, 12));
                lblNotaFiscal.setForeground(DARK_GRAY);
                panel.add(lblNotaFiscal, gbc);
                gbc.gridx = 1; gbc.gridy = 4;
                ElegantTextField txtNotaFiscal = new ElegantTextField(15);
                panel.add(txtNotaFiscal, gbc);
                break;
                
            case "📋 Consultar Estoque":
                // Painel de Busca
                gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
                gbc.fill = GridBagConstraints.HORIZONTAL;
                gbc.insets = new Insets(5, 5, 15, 5);
                
                JPanel buscaPanelEstoque = new JPanel(new GridBagLayout());
                buscaPanelEstoque.setBorder(BorderFactory.createTitledBorder(
                    BorderFactory.createEtchedBorder(), "📋 Consultar Estoque",
                    TitledBorder.LEFT, TitledBorder.TOP, new Font("Segoe UI", Font.BOLD, 12),
                    DARK_GRAY));
                
                GridBagConstraints gbcBuscaEstoque = new GridBagConstraints();
                gbcBuscaEstoque.insets = new Insets(5, 5, 5, 5);
                
                // Campo de busca
                gbcBuscaEstoque.gridx = 0; gbcBuscaEstoque.gridy = 0;
                gbcBuscaEstoque.fill = GridBagConstraints.HORIZONTAL;
                gbcBuscaEstoque.weightx = 1.0;
                JLabel lblBuscaEstoque = new JLabel("Buscar:");
                lblBuscaEstoque.setFont(new Font("Segoe UI", Font.BOLD, 12));
                lblBuscaEstoque.setForeground(DARK_GRAY);
                buscaPanelEstoque.add(lblBuscaEstoque, gbcBuscaEstoque);
                
                gbcBuscaEstoque.gridx = 1; gbcBuscaEstoque.gridy = 0;
                gbcBuscaEstoque.fill = GridBagConstraints.HORIZONTAL;
                gbcBuscaEstoque.weightx = 2.0;
                ElegantTextField txtBuscaEstoque = new ElegantTextField(30);
                txtBuscaEstoque.setToolTipText("Digite código, nome ou categoria do produto");
                buscaPanelEstoque.add(txtBuscaEstoque, gbcBuscaEstoque);
                
                // Botões de busca
                gbcBuscaEstoque.gridx = 2; gbcBuscaEstoque.gridy = 0;
                gbcBuscaEstoque.fill = GridBagConstraints.NONE;
                gbcBuscaEstoque.weightx = 0;
                ElegantButton btnBuscarEstoque = new ElegantButton("🔍 Buscar", PRIMARY_COLOR, false);
                btnBuscarEstoque.setForeground(WHITE);
                buscaPanelEstoque.add(btnBuscarEstoque, gbcBuscaEstoque);
                
                gbcBuscaEstoque.gridx = 3; gbcBuscaEstoque.gridy = 0;
                ElegantButton btnLimparBuscaEstoque = new ElegantButton("🔄 Limpar", MEDIUM_GRAY, false);
                btnLimparBuscaEstoque.setForeground(WHITE);
                buscaPanelEstoque.add(btnLimparBuscaEstoque, gbcBuscaEstoque);
                
                panel.add(buscaPanelEstoque, gbc);
                
                // Resetar gridwidth para campos abaixo
                gbc.gridwidth = 1;
                gbc.insets = new Insets(5, 5, 5, 5);
                
                // Filtros adicionais
                gbc.gridx = 0; gbc.gridy = 1;
                gbc.fill = GridBagConstraints.NONE;
                JLabel lblFiltrosEstoque = new JLabel("Filtros:");
                lblFiltrosEstoque.setFont(new Font("Segoe UI", Font.BOLD, 12));
                lblFiltrosEstoque.setForeground(DARK_GRAY);
                panel.add(lblFiltrosEstoque, gbc);
                
                gbc.gridx = 1; gbc.gridy = 1;
                gbc.fill = GridBagConstraints.HORIZONTAL;
                JPanel filtrosPanelEstoque = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
                
                JLabel lblCategoriaEstoque = new JLabel("Categoria:");
                lblCategoriaEstoque.setFont(new Font("Segoe UI", Font.PLAIN, 12));
                filtrosPanelEstoque.add(lblCategoriaEstoque);
                JComboBox<String> cbCategoriaEstoque = new JComboBox<>(new String[]{
                    "Todas", "Informática", "Periféricos", "Monitores"
                });
                cbCategoriaEstoque.setFont(new Font("Segoe UI", Font.PLAIN, 12));
                filtrosPanelEstoque.add(cbCategoriaEstoque);
                
                JLabel lblEstoqueMinimo = new JLabel("Estoque:");
                lblEstoqueMinimo.setFont(new Font("Segoe UI", Font.PLAIN, 12));
                filtrosPanelEstoque.add(lblEstoqueMinimo);
                JComboBox<String> cbEstoqueMinimo = new JComboBox<>(new String[]{
                    "Todos", "Acima do Mínimo", "Abaixo do Mínimo", "Zerado"
                });
                cbEstoqueMinimo.setFont(new Font("Segoe UI", Font.PLAIN, 12));
                filtrosPanelEstoque.add(cbEstoqueMinimo);
                
                panel.add(filtrosPanelEstoque, gbc);
                
                // Tabela de Estoque
                gbc.gridx = 0; gbc.gridy = 2; gbc.gridwidth = 2;
                gbc.fill = GridBagConstraints.BOTH;
                gbc.weightx = 1.0; gbc.weighty = 1.0;
                
                String[] colunasTabelaEstoque = {"Código", "Descrição", "Categoria", "Estoque Atual", "Estoque Mínimo", "Status"};
                Object[][] dadosTabelaEstoque = {};
                
                JTable tabelaEstoque = new JTable(dadosTabelaEstoque, colunasTabelaEstoque);
                tabelaEstoque.setFont(new Font("Segoe UI", Font.PLAIN, 11));
                tabelaEstoque.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 12));
                tabelaEstoque.getTableHeader().setBackground(new Color(70, 130, 180));
                tabelaEstoque.getTableHeader().setForeground(WHITE);
                tabelaEstoque.setRowHeight(25);
                tabelaEstoque.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
                tabelaEstoque.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
                
                JScrollPane scrollPaneTabelaEstoque = new JScrollPane(tabelaEstoque);
                scrollPaneTabelaEstoque.setPreferredSize(new Dimension(700, 200));
                scrollPaneTabelaEstoque.setBorder(BorderFactory.createTitledBorder(
                    BorderFactory.createEtchedBorder(), "Estoque Encontrado",
                    TitledBorder.LEFT, TitledBorder.TOP, new Font("Segoe UI", Font.BOLD, 12),
                    DARK_GRAY));
                panel.add(scrollPaneTabelaEstoque, gbc);
                break;
                
            case "⚠️ Estoque Baixo":
                // Painel de Filtros de Estoque Baixo
                gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
                gbc.fill = GridBagConstraints.HORIZONTAL;
                gbc.insets = new Insets(5, 5, 15, 5);
                
                JPanel filtrosPanelEstoqueBaixo = new JPanel(new GridBagLayout());
                filtrosPanelEstoqueBaixo.setBorder(BorderFactory.createTitledBorder(
                    BorderFactory.createEtchedBorder(), "⚠️ Filtros de Estoque Baixo",
                    TitledBorder.LEFT, TitledBorder.TOP, new Font("Segoe UI", Font.BOLD, 12),
                    DARK_GRAY));
                
                GridBagConstraints gbcFiltrosEstoqueBaixo = new GridBagConstraints();
                gbcFiltrosEstoqueBaixo.insets = new Insets(5, 5, 5, 5);
                
                // Categoria
                gbcFiltrosEstoqueBaixo.gridx = 0; gbcFiltrosEstoqueBaixo.gridy = 0;
                gbcFiltrosEstoqueBaixo.fill = GridBagConstraints.NONE;
                gbcFiltrosEstoqueBaixo.weightx = 0.3;
                JLabel lblCategoriaEstoqueBaixo = new JLabel("Categoria:");
                lblCategoriaEstoqueBaixo.setFont(new Font("Segoe UI", Font.BOLD, 12));
                lblCategoriaEstoqueBaixo.setForeground(DARK_GRAY);
                filtrosPanelEstoqueBaixo.add(lblCategoriaEstoqueBaixo, gbcFiltrosEstoqueBaixo);
                gbcFiltrosEstoqueBaixo.gridx = 1; gbcFiltrosEstoqueBaixo.gridy = 0;
                gbcFiltrosEstoqueBaixo.fill = GridBagConstraints.HORIZONTAL;
                gbcFiltrosEstoqueBaixo.weightx = 0.7;
                JComboBox<String> cbCategoriaEstoqueBaixo = new JComboBox<>(new String[]{
                    "Todas", "Eletrônicos", "Roupas", "Alimentos", "Móveis", "Informática", "Periféricos"
                });
                cbCategoriaEstoqueBaixo.setFont(new Font("Segoe UI", Font.PLAIN, 12));
                cbCategoriaEstoqueBaixo.setToolTipText("Filtrar por categoria");
                filtrosPanelEstoqueBaixo.add(cbCategoriaEstoqueBaixo, gbcFiltrosEstoqueBaixo);
                
                // Nível Crítico
                gbcFiltrosEstoqueBaixo.gridx = 2; gbcFiltrosEstoqueBaixo.gridy = 0;
                gbcFiltrosEstoqueBaixo.fill = GridBagConstraints.NONE;
                gbcFiltrosEstoqueBaixo.weightx = 0.3;
                JLabel lblNivelCritico = new JLabel("Nível Crítico:");
                lblNivelCritico.setFont(new Font("Segoe UI", Font.BOLD, 12));
                lblNivelCritico.setForeground(DARK_GRAY);
                filtrosPanelEstoqueBaixo.add(lblNivelCritico, gbcFiltrosEstoqueBaixo);
                gbcFiltrosEstoqueBaixo.gridx = 3; gbcFiltrosEstoqueBaixo.gridy = 0;
                gbcFiltrosEstoqueBaixo.fill = GridBagConstraints.HORIZONTAL;
                gbcFiltrosEstoqueBaixo.weightx = 0.7;
                JComboBox<String> cbNivelCritico = new JComboBox<>(new String[]{
                    "Todos", "Crítico", "Médio", "Baixo"
                });
                cbNivelCritico.setFont(new Font("Segoe UI", Font.PLAIN, 12));
                cbNivelCritico.setToolTipText("Nível de criticidade do estoque");
                filtrosPanelEstoqueBaixo.add(cbNivelCritico, gbcFiltrosEstoqueBaixo);
                
                // Busca
                gbcFiltrosEstoqueBaixo.gridx = 0; gbcFiltrosEstoqueBaixo.gridy = 1;
                gbcFiltrosEstoqueBaixo.fill = GridBagConstraints.NONE;
                gbcFiltrosEstoqueBaixo.weightx = 0.3;
                JLabel lblBuscaEstoqueBaixo = new JLabel("Buscar:");
                lblBuscaEstoqueBaixo.setFont(new Font("Segoe UI", Font.BOLD, 12));
                lblBuscaEstoqueBaixo.setForeground(DARK_GRAY);
                filtrosPanelEstoqueBaixo.add(lblBuscaEstoqueBaixo, gbcFiltrosEstoqueBaixo);
                gbcFiltrosEstoqueBaixo.gridx = 1; gbcFiltrosEstoqueBaixo.gridy = 1;
                gbcFiltrosEstoqueBaixo.fill = GridBagConstraints.HORIZONTAL;
                gbcFiltrosEstoqueBaixo.weightx = 2.7;
                gbcFiltrosEstoqueBaixo.gridwidth = 3;
                ElegantTextField txtBuscaEstoqueBaixo = new ElegantTextField(40);
                txtBuscaEstoqueBaixo.setToolTipText("Buscar por código, nome ou descrição do produto");
                filtrosPanelEstoqueBaixo.add(txtBuscaEstoqueBaixo, gbcFiltrosEstoqueBaixo);
                
                panel.add(filtrosPanelEstoqueBaixo, gbc);
                
                // Tabela de Estoque Baixo
                gbc.gridx = 0; gbc.gridy = 1; gbc.gridwidth = 2;
                gbc.fill = GridBagConstraints.BOTH;
                gbc.weightx = 1.0; gbc.weighty = 1.0;
                
                String[] colunasTabelaEstoqueBaixo = {"Código", "Produto", "Categoria", "Estoque Atual", "Estoque Mínimo", "Status", "Última Compra", "Preço", "Fornecedor"};
                Object[][] dadosTabelaEstoqueBaixo = {};
                
                JTable tabelaEstoqueBaixo = new JTable(dadosTabelaEstoqueBaixo, colunasTabelaEstoqueBaixo);
                tabelaEstoqueBaixo.setFont(new Font("Segoe UI", Font.PLAIN, 11));
                tabelaEstoqueBaixo.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 12));
                tabelaEstoqueBaixo.getTableHeader().setBackground(new Color(26, 188, 156));
                tabelaEstoqueBaixo.getTableHeader().setForeground(WHITE);
                tabelaEstoqueBaixo.setRowHeight(25);
                tabelaEstoqueBaixo.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
                tabelaEstoqueBaixo.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
                
                JScrollPane scrollPaneTabelaEstoqueBaixo = new JScrollPane(tabelaEstoqueBaixo);
                scrollPaneTabelaEstoqueBaixo.setPreferredSize(new Dimension(1200, 300));
                scrollPaneTabelaEstoqueBaixo.setBorder(BorderFactory.createTitledBorder(
                    BorderFactory.createEtchedBorder(), "⚠️ Produtos com Estoque Baixo",
                    TitledBorder.LEFT, TitledBorder.TOP, new Font("Segoe UI", Font.BOLD, 12),
                    DARK_GRAY));
                panel.add(scrollPaneTabelaEstoqueBaixo, gbc);
                
                // Painel de Ações
                gbc.gridx = 0; gbc.gridy = 2; gbc.gridwidth = 2;
                gbc.fill = GridBagConstraints.HORIZONTAL;
                gbc.weightx = 1.0; gbc.weighty = 0;
                gbc.insets = new Insets(10, 5, 5, 5);
                
                JPanel acoesPanelEstoqueBaixo = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 5));
                acoesPanelEstoqueBaixo.setOpaque(false);
                
                ElegantButton btnAtualizarEstoqueBaixo = new ElegantButton("🔄 Atualizar", PRIMARY_COLOR, false);
                btnAtualizarEstoqueBaixo.setForeground(WHITE);
                btnAtualizarEstoqueBaixo.setFont(new Font("Segoe UI", Font.BOLD, 14));
                btnAtualizarEstoqueBaixo.setPreferredSize(new Dimension(120, 40));
                btnAtualizarEstoqueBaixo.setToolTipText("Atualizar lista de produtos");
                acoesPanelEstoqueBaixo.add(btnAtualizarEstoqueBaixo);
                
                ElegantButton btnComprarEstoqueBaixo = new ElegantButton("🛒 Comprar", SUCCESS_COLOR, false);
                btnComprarEstoqueBaixo.setForeground(WHITE);
                btnComprarEstoqueBaixo.setFont(new Font("Segoe UI", Font.BOLD, 14));
                btnComprarEstoqueBaixo.setPreferredSize(new Dimension(120, 40));
                btnComprarEstoqueBaixo.setToolTipText("Gerar ordem de compra");
                acoesPanelEstoqueBaixo.add(btnComprarEstoqueBaixo);
                
                ElegantButton btnTransferirEstoqueBaixo = new ElegantButton("🔄 Transferir", WARNING_COLOR, false);
                btnTransferirEstoqueBaixo.setForeground(WHITE);
                btnTransferirEstoqueBaixo.setFont(new Font("Segoe UI", Font.BOLD, 14));
                btnTransferirEstoqueBaixo.setPreferredSize(new Dimension(120, 40));
                btnTransferirEstoqueBaixo.setToolTipText("Transferir entre lojas");
                acoesPanelEstoqueBaixo.add(btnTransferirEstoqueBaixo);
                
                ElegantButton btnAlertarEstoqueBaixo = new ElegantButton("⚠️ Alertar", new Color(255, 152, 0), false);
                btnAlertarEstoqueBaixo.setForeground(WHITE);
                btnAlertarEstoqueBaixo.setFont(new Font("Segoe UI", Font.BOLD, 14));
                btnAlertarEstoqueBaixo.setPreferredSize(new Dimension(120, 40));
                btnAlertarEstoqueBaixo.setToolTipText("Enviar alerta automático");
                acoesPanelEstoqueBaixo.add(btnAlertarEstoqueBaixo);
                
                ElegantButton btnExportarEstoqueBaixo = new ElegantButton("📊 Exportar", new Color(0, 120, 215), false);
                btnExportarEstoqueBaixo.setForeground(WHITE);
                btnExportarEstoqueBaixo.setFont(new Font("Segoe UI", Font.BOLD, 14));
                btnExportarEstoqueBaixo.setPreferredSize(new Dimension(120, 40));
                btnExportarEstoqueBaixo.setToolTipText("Exportar relatório");
                acoesPanelEstoqueBaixo.add(btnExportarEstoqueBaixo);
                
                ElegantButton btnEmailEstoqueBaixo = new ElegantButton("📧 Email", new Color(70, 130, 180), false);
                btnEmailEstoqueBaixo.setForeground(WHITE);
                btnEmailEstoqueBaixo.setFont(new Font("Segoe UI", Font.BOLD, 14));
                btnEmailEstoqueBaixo.setPreferredSize(new Dimension(120, 40));
                btnEmailEstoqueBaixo.setToolTipText("Enviar relatório por email");
                acoesPanelEstoqueBaixo.add(btnEmailEstoqueBaixo);
                
                panel.add(acoesPanelEstoqueBaixo, gbc);
                break;
                
            case "📊 Inventário":
                // Painel de Informações do Inventário
                gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
                gbc.fill = GridBagConstraints.HORIZONTAL;
                gbc.insets = new Insets(5, 5, 15, 5);
                
                JPanel infoPanelInventario = new JPanel(new GridBagLayout());
                infoPanelInventario.setBorder(BorderFactory.createTitledBorder(
                    BorderFactory.createEtchedBorder(), "📊 Informações do Inventário",
                    TitledBorder.LEFT, TitledBorder.TOP, new Font("Segoe UI", Font.BOLD, 12),
                    DARK_GRAY));
                
                GridBagConstraints gbcInfoInventario = new GridBagConstraints();
                gbcInfoInventario.insets = new Insets(5, 5, 5, 5);
                
                // ID do Inventário
                gbcInfoInventario.gridx = 0; gbcInfoInventario.gridy = 0;
                gbcInfoInventario.fill = GridBagConstraints.NONE;
                gbcInfoInventario.weightx = 0.3;
                JLabel lblIdInventario = new JLabel("ID Inventário:");
                lblIdInventario.setFont(new Font("Segoe UI", Font.BOLD, 12));
                lblIdInventario.setForeground(DARK_GRAY);
                infoPanelInventario.add(lblIdInventario, gbcInfoInventario);
                gbcInfoInventario.gridx = 1; gbcInfoInventario.gridy = 0;
                gbcInfoInventario.fill = GridBagConstraints.HORIZONTAL;
                gbcInfoInventario.weightx = 0.7;
                ElegantTextField txtIdInventario = new ElegantTextField(15);
                txtIdInventario.setEditable(false);
                txtIdInventario.setText("AUTO");
                txtIdInventario.setToolTipText("ID automático do inventário");
                infoPanelInventario.add(txtIdInventario, gbcInfoInventario);
                
                // Data do Inventário
                gbcInfoInventario.gridx = 2; gbcInfoInventario.gridy = 0;
                gbcInfoInventario.fill = GridBagConstraints.NONE;
                gbcInfoInventario.weightx = 0.3;
                JLabel lblDataInventario = new JLabel("Data:");
                lblDataInventario.setFont(new Font("Segoe UI", Font.BOLD, 12));
                lblDataInventario.setForeground(DARK_GRAY);
                infoPanelInventario.add(lblDataInventario, gbcInfoInventario);
                gbcInfoInventario.gridx = 3; gbcInfoInventario.gridy = 0;
                gbcInfoInventario.fill = GridBagConstraints.HORIZONTAL;
                gbcInfoInventario.weightx = 0.7;
                ElegantTextField txtDataInventario = new ElegantTextField(10);
                txtDataInventario.setToolTipText("Data do inventário");
                infoPanelInventario.add(txtDataInventario, gbcInfoInventario);
                
                // Tipo de Inventário
                gbcInfoInventario.gridx = 0; gbcInfoInventario.gridy = 1;
                gbcInfoInventario.fill = GridBagConstraints.NONE;
                gbcInfoInventario.weightx = 0.3;
                JLabel lblTipoInventario = new JLabel("Tipo:");
                lblTipoInventario.setFont(new Font("Segoe UI", Font.BOLD, 12));
                lblTipoInventario.setForeground(DARK_GRAY);
                infoPanelInventario.add(lblTipoInventario, gbcInfoInventario);
                gbcInfoInventario.gridx = 1; gbcInfoInventario.gridy = 1;
                gbcInfoInventario.fill = GridBagConstraints.HORIZONTAL;
                gbcInfoInventario.weightx = 0.7;
                JComboBox<String> cbTipoInventario = new JComboBox<>(new String[]{
                    "Completo", "Parcial", "Rotativo", "Por Categoria", "Por Local"
                });
                cbTipoInventario.setFont(new Font("Segoe UI", Font.PLAIN, 12));
                cbTipoInventario.setToolTipText("Tipo de inventário");
                infoPanelInventario.add(cbTipoInventario, gbcInfoInventario);
                
                // Responsável
                gbcInfoInventario.gridx = 2; gbcInfoInventario.gridy = 1;
                gbcInfoInventario.fill = GridBagConstraints.NONE;
                gbcInfoInventario.weightx = 0.3;
                JLabel lblResponsavelInventario = new JLabel("Responsável:");
                lblResponsavelInventario.setFont(new Font("Segoe UI", Font.BOLD, 12));
                lblResponsavelInventario.setForeground(DARK_GRAY);
                infoPanelInventario.add(lblResponsavelInventario, gbcInfoInventario);
                gbcInfoInventario.gridx = 3; gbcInfoInventario.gridy = 1;
                gbcInfoInventario.fill = GridBagConstraints.HORIZONTAL;
                gbcInfoInventario.weightx = 0.7;
                ElegantTextField txtResponsavelInventario = new ElegantTextField(25);
                txtResponsavelInventario.setToolTipText("Responsável pelo inventário");
                infoPanelInventario.add(txtResponsavelInventario, gbcInfoInventario);
                
                // Observações
                gbcInfoInventario.gridx = 0; gbcInfoInventario.gridy = 2;
                gbcInfoInventario.fill = GridBagConstraints.NONE;
                gbcInfoInventario.weightx = 0.3;
                JLabel lblObservacoesInventario = new JLabel("Observações:");
                lblObservacoesInventario.setFont(new Font("Segoe UI", Font.BOLD, 12));
                lblObservacoesInventario.setForeground(DARK_GRAY);
                infoPanelInventario.add(lblObservacoesInventario, gbcInfoInventario);
                gbcInfoInventario.gridx = 1; gbcInfoInventario.gridy = 2;
                gbcInfoInventario.fill = GridBagConstraints.HORIZONTAL;
                gbcInfoInventario.weightx = 2.7;
                gbcInfoInventario.gridwidth = 3;
                ElegantTextField txtObservacoesInventario = new ElegantTextField(50);
                txtObservacoesInventario.setToolTipText("Observações sobre o inventário");
                infoPanelInventario.add(txtObservacoesInventario, gbcInfoInventario);
                
                panel.add(infoPanelInventario, gbc);
                
                // Painel de Filtros
                gbc.gridx = 0; gbc.gridy = 1; gbc.gridwidth = 2;
                gbc.fill = GridBagConstraints.HORIZONTAL;
                gbc.insets = new Insets(5, 5, 15, 5);
                
                JPanel filtrosPanelInventario = new JPanel(new GridBagLayout());
                filtrosPanelInventario.setBorder(BorderFactory.createTitledBorder(
                    BorderFactory.createEtchedBorder(), "🔍 Filtros de Pesquisa",
                    TitledBorder.LEFT, TitledBorder.TOP, new Font("Segoe UI", Font.BOLD, 12),
                    DARK_GRAY));
                
                GridBagConstraints gbcFiltrosInventario = new GridBagConstraints();
                gbcFiltrosInventario.insets = new Insets(5, 5, 5, 5);
                
                // Categoria
                gbcFiltrosInventario.gridx = 0; gbcFiltrosInventario.gridy = 0;
                gbcFiltrosInventario.fill = GridBagConstraints.NONE;
                gbcFiltrosInventario.weightx = 0.3;
                JLabel lblCategoriaInventario = new JLabel("Categoria:");
                lblCategoriaInventario.setFont(new Font("Segoe UI", Font.BOLD, 12));
                lblCategoriaInventario.setForeground(DARK_GRAY);
                filtrosPanelInventario.add(lblCategoriaInventario, gbcFiltrosInventario);
                gbcFiltrosInventario.gridx = 1; gbcFiltrosInventario.gridy = 0;
                gbcFiltrosInventario.fill = GridBagConstraints.HORIZONTAL;
                gbcFiltrosInventario.weightx = 0.7;
                JComboBox<String> cbCategoriaInventario = new JComboBox<>(new String[]{
                    "Todas", "Eletrônicos", "Roupas", "Alimentos", "Móveis", "Informática"
                });
                cbCategoriaInventario.setFont(new Font("Segoe UI", Font.PLAIN, 12));
                cbCategoriaInventario.setToolTipText("Filtrar por categoria");
                filtrosPanelInventario.add(cbCategoriaInventario, gbcFiltrosInventario);
                
                // Local
                gbcFiltrosInventario.gridx = 2; gbcFiltrosInventario.gridy = 0;
                gbcFiltrosInventario.fill = GridBagConstraints.NONE;
                gbcFiltrosInventario.weightx = 0.3;
                JLabel lblLocalInventario = new JLabel("Local:");
                lblLocalInventario.setFont(new Font("Segoe UI", Font.BOLD, 12));
                lblLocalInventario.setForeground(DARK_GRAY);
                filtrosPanelInventario.add(lblLocalInventario, gbcFiltrosInventario);
                gbcFiltrosInventario.gridx = 3; gbcFiltrosInventario.gridy = 0;
                gbcFiltrosInventario.fill = GridBagConstraints.HORIZONTAL;
                gbcFiltrosInventario.weightx = 0.7;
                JComboBox<String> cbLocalInventario = new JComboBox<>(new String[]{
                    "Todos", "Almoxarifado", "Loja Principal", "Depósito", "Área de Venda"
                });
                cbLocalInventario.setFont(new Font("Segoe UI", Font.PLAIN, 12));
                cbLocalInventario.setToolTipText("Local do produto");
                filtrosPanelInventario.add(cbLocalInventario, gbcFiltrosInventario);
                
                // Busca
                gbcFiltrosInventario.gridx = 0; gbcFiltrosInventario.gridy = 1;
                gbcFiltrosInventario.fill = GridBagConstraints.NONE;
                gbcFiltrosInventario.weightx = 0.3;
                JLabel lblBuscaInventario = new JLabel("Buscar:");
                lblBuscaInventario.setFont(new Font("Segoe UI", Font.BOLD, 12));
                lblBuscaInventario.setForeground(DARK_GRAY);
                filtrosPanelInventario.add(lblBuscaInventario, gbcFiltrosInventario);
                gbcFiltrosInventario.gridx = 1; gbcFiltrosInventario.gridy = 1;
                gbcFiltrosInventario.fill = GridBagConstraints.HORIZONTAL;
                gbcFiltrosInventario.weightx = 2.7;
                gbcFiltrosInventario.gridwidth = 3;
                ElegantTextField txtBuscaInventario = new ElegantTextField(40);
                txtBuscaInventario.setToolTipText("Buscar por código, nome ou descrição");
                filtrosPanelInventario.add(txtBuscaInventario, gbcFiltrosInventario);
                
                panel.add(filtrosPanelInventario, gbc);
                
                // Tabela de Itens do Inventário
                gbc.gridx = 0; gbc.gridy = 2; gbc.gridwidth = 2;
                gbc.fill = GridBagConstraints.BOTH;
                gbc.weightx = 1.0; gbc.weighty = 1.0;
                
                String[] colunasTabelaInventario = {"Código", "Produto", "Categoria", "Local", "Estoque Sistema", "Estoque Contado", "Diferença", "Valor Unitário", "Valor Total", "Status"};
                Object[][] dadosTabelaInventario = {};
                
                JTable tabelaInventario = new JTable(dadosTabelaInventario, colunasTabelaInventario);
                tabelaInventario.setFont(new Font("Segoe UI", Font.PLAIN, 11));
                tabelaInventario.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 12));
                tabelaInventario.getTableHeader().setBackground(new Color(26, 188, 156));
                tabelaInventario.getTableHeader().setForeground(WHITE);
                tabelaInventario.setRowHeight(25);
                tabelaInventario.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
                tabelaInventario.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
                
                JScrollPane scrollPaneTabelaInventario = new JScrollPane(tabelaInventario);
                scrollPaneTabelaInventario.setPreferredSize(new Dimension(1400, 300));
                scrollPaneTabelaInventario.setBorder(BorderFactory.createTitledBorder(
                    BorderFactory.createEtchedBorder(), "📋 Itens do Inventário",
                    TitledBorder.LEFT, TitledBorder.TOP, new Font("Segoe UI", Font.BOLD, 12),
                    DARK_GRAY));
                panel.add(scrollPaneTabelaInventario, gbc);
                
                // Painel de Resumo
                gbc.gridx = 0; gbc.gridy = 3; gbc.gridwidth = 2;
                gbc.fill = GridBagConstraints.HORIZONTAL;
                gbc.weightx = 1.0; gbc.weighty = 0;
                gbc.insets = new Insets(5, 5, 15, 5);
                
                JPanel resumoPanelInventario = new JPanel(new GridBagLayout());
                resumoPanelInventario.setBorder(BorderFactory.createTitledBorder(
                    BorderFactory.createEtchedBorder(), "📊 Resumo do Inventário",
                    TitledBorder.LEFT, TitledBorder.TOP, new Font("Segoe UI", Font.BOLD, 12),
                    DARK_GRAY));
                
                GridBagConstraints gbcResumoInventario = new GridBagConstraints();
                gbcResumoInventario.insets = new Insets(5, 5, 5, 5);
                
                // Total de Itens
                gbcResumoInventario.gridx = 0; gbcResumoInventario.gridy = 0;
                gbcResumoInventario.fill = GridBagConstraints.NONE;
                gbcResumoInventario.weightx = 0.3;
                JLabel lblTotalItens = new JLabel("Total de Itens:");
                lblTotalItens.setFont(new Font("Segoe UI", Font.BOLD, 12));
                lblTotalItens.setForeground(DARK_GRAY);
                resumoPanelInventario.add(lblTotalItens, gbcResumoInventario);
                gbcResumoInventario.gridx = 1; gbcResumoInventario.gridy = 0;
                gbcResumoInventario.fill = GridBagConstraints.HORIZONTAL;
                gbcResumoInventario.weightx = 0.7;
                ElegantTextField txtTotalItens = new ElegantTextField(15);
                txtTotalItens.setEditable(false);
                txtTotalItens.setText("0");
                txtTotalItens.setFont(new Font("Segoe UI", Font.BOLD, 12));
                txtTotalItens.setForeground(new Color(0, 100, 200));
                resumoPanelInventario.add(txtTotalItens, gbcResumoInventario);
                
                // Valor Total
                gbcResumoInventario.gridx = 2; gbcResumoInventario.gridy = 0;
                gbcResumoInventario.fill = GridBagConstraints.NONE;
                gbcResumoInventario.weightx = 0.3;
                JLabel lblValorTotalInventario = new JLabel("Valor Total:");
                lblValorTotalInventario.setFont(new Font("Segoe UI", Font.BOLD, 12));
                lblValorTotalInventario.setForeground(DARK_GRAY);
                resumoPanelInventario.add(lblValorTotalInventario, gbcResumoInventario);
                gbcResumoInventario.gridx = 3; gbcResumoInventario.gridy = 0;
                gbcResumoInventario.fill = GridBagConstraints.HORIZONTAL;
                gbcResumoInventario.weightx = 0.7;
                ElegantTextField txtValorTotalInventario = new ElegantTextField(15);
                txtValorTotalInventario.setEditable(false);
                txtValorTotalInventario.setText("R$ 0,00");
                txtValorTotalInventario.setFont(new Font("Segoe UI", Font.BOLD, 12));
                txtValorTotalInventario.setForeground(new Color(0, 150, 0));
                resumoPanelInventario.add(txtValorTotalInventario, gbcResumoInventario);
                
                // Diferença Total
                gbcResumoInventario.gridx = 0; gbcResumoInventario.gridy = 1;
                gbcResumoInventario.fill = GridBagConstraints.NONE;
                gbcResumoInventario.weightx = 0.3;
                JLabel lblDiferencaTotal = new JLabel("Diferença Total:");
                lblDiferencaTotal.setFont(new Font("Segoe UI", Font.BOLD, 12));
                lblDiferencaTotal.setForeground(DARK_GRAY);
                resumoPanelInventario.add(lblDiferencaTotal, gbcResumoInventario);
                gbcResumoInventario.gridx = 1; gbcResumoInventario.gridy = 1;
                gbcResumoInventario.fill = GridBagConstraints.HORIZONTAL;
                gbcResumoInventario.weightx = 0.7;
                ElegantTextField txtDiferencaTotal = new ElegantTextField(15);
                txtDiferencaTotal.setEditable(false);
                txtDiferencaTotal.setText("R$ 0,00");
                txtDiferencaTotal.setFont(new Font("Segoe UI", Font.BOLD, 12));
                txtDiferencaTotal.setForeground(new Color(200, 50, 50));
                resumoPanelInventario.add(txtDiferencaTotal, gbcResumoInventario);
                
                // Status do Inventário
                gbcResumoInventario.gridx = 2; gbcResumoInventario.gridy = 1;
                gbcResumoInventario.fill = GridBagConstraints.NONE;
                gbcResumoInventario.weightx = 0.3;
                JLabel lblStatusInventario = new JLabel("Status:");
                lblStatusInventario.setFont(new Font("Segoe UI", Font.BOLD, 12));
                lblStatusInventario.setForeground(DARK_GRAY);
                resumoPanelInventario.add(lblStatusInventario, gbcResumoInventario);
                gbcResumoInventario.gridx = 3; gbcResumoInventario.gridy = 1;
                gbcResumoInventario.fill = GridBagConstraints.HORIZONTAL;
                gbcResumoInventario.weightx = 0.7;
                JComboBox<String> cbStatusInventario = new JComboBox<>(new String[]{
                    "Em Andamento", "Concluído", "Pendente", "Cancelado"
                });
                cbStatusInventario.setFont(new Font("Segoe UI", Font.PLAIN, 12));
                cbStatusInventario.setToolTipText("Status do inventário");
                resumoPanelInventario.add(cbStatusInventario, gbcResumoInventario);
                
                panel.add(resumoPanelInventario, gbc);
                
                // Painel de Ações
                gbc.gridx = 0; gbc.gridy = 4; gbc.gridwidth = 2;
                gbc.fill = GridBagConstraints.HORIZONTAL;
                gbc.weightx = 1.0; gbc.weighty = 0;
                gbc.insets = new Insets(10, 5, 5, 5);
                
                JPanel acoesPanelInventario = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 5));
                acoesPanelInventario.setOpaque(false);
                
                ElegantButton btnNovoInventario = new ElegantButton("🆕 Novo", SUCCESS_COLOR, false);
                btnNovoInventario.setForeground(WHITE);
                btnNovoInventario.setFont(new Font("Segoe UI", Font.BOLD, 14));
                btnNovoInventario.setPreferredSize(new Dimension(120, 40));
                btnNovoInventario.setToolTipText("Iniciar novo inventário");
                acoesPanelInventario.add(btnNovoInventario);
                
                ElegantButton btnImportarInventario = new ElegantButton("📥 Importar", PRIMARY_COLOR, false);
                btnImportarInventario.setForeground(WHITE);
                btnImportarInventario.setFont(new Font("Segoe UI", Font.BOLD, 14));
                btnImportarInventario.setPreferredSize(new Dimension(120, 40));
                btnImportarInventario.setToolTipText("Importar dados do sistema");
                acoesPanelInventario.add(btnImportarInventario);
                
                ElegantButton btnContarInventario = new ElegantButton("🔢 Contar", WARNING_COLOR, false);
                btnContarInventario.setForeground(WHITE);
                btnContarInventario.setFont(new Font("Segoe UI", Font.BOLD, 14));
                btnContarInventario.setPreferredSize(new Dimension(120, 40));
                btnContarInventario.setToolTipText("Iniciar contagem");
                acoesPanelInventario.add(btnContarInventario);
                
                ElegantButton btnCompararInventario = new ElegantButton("⚖️ Comparar", new Color(0, 120, 215), false);
                btnCompararInventario.setForeground(WHITE);
                btnCompararInventario.setFont(new Font("Segoe UI", Font.BOLD, 14));
                btnCompararInventario.setPreferredSize(new Dimension(120, 40));
                btnCompararInventario.setToolTipText("Comparar com estoque sistema");
                acoesPanelInventario.add(btnCompararInventario);
                
                ElegantButton btnRelatorioInventario = new ElegantButton("📄 Relatório", new Color(100, 100, 100), false);
                btnRelatorioInventario.setForeground(WHITE);
                btnRelatorioInventario.setFont(new Font("Segoe UI", Font.BOLD, 14));
                btnRelatorioInventario.setPreferredSize(new Dimension(120, 40));
                btnRelatorioInventario.setToolTipText("Gerar relatório");
                acoesPanelInventario.add(btnRelatorioInventario);
                
                ElegantButton btnExportarInventario = new ElegantButton("📊 Exportar", new Color(70, 130, 180), false);
                btnExportarInventario.setForeground(WHITE);
                btnExportarInventario.setFont(new Font("Segoe UI", Font.BOLD, 14));
                btnExportarInventario.setPreferredSize(new Dimension(120, 40));
                btnExportarInventario.setToolTipText("Exportar para Excel/PDF");
                acoesPanelInventario.add(btnExportarInventario);
                
                ElegantButton btnFinalizarInventario = new ElegantButton("✅ Finalizar", new Color(0, 150, 0), false);
                btnFinalizarInventario.setForeground(WHITE);
                btnFinalizarInventario.setFont(new Font("Segoe UI", Font.BOLD, 14));
                btnFinalizarInventario.setPreferredSize(new Dimension(120, 40));
                btnFinalizarInventario.setToolTipText("Finalizar inventário");
                acoesPanelInventario.add(btnFinalizarInventario);
                
                panel.add(acoesPanelInventario, gbc);
                break;
                
            default:
                return criarFormularioPadraoElegante(item, "ESTOQUE", gbc);
        }
        
        return panel;
    }
    
    /**
     * Formulários Financeiro elegantes
     */
    private JPanel criarFormularioFinanceiroElegante(String item, GridBagConstraints gbc) {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setOpaque(false);
        
        switch (item) {
            case "🔧 Gestão de Contas":
                // Painel principal com abas para Contas a Receber e Contas a Pagar
                gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
                gbc.fill = GridBagConstraints.BOTH;
                gbc.weightx = 1.0; gbc.weighty = 1.0;
                gbc.insets = new Insets(5, 5, 5, 5);
                
                // Criar painel de abas
                JTabbedPane tabbedPaneContas = new JTabbedPane();
                tabbedPaneContas.setFont(new Font("Segoe UI", Font.BOLD, 12));
                tabbedPaneContas.setBackground(WHITE);
                
                // Aba 1: Contas a Receber
                JPanel panelContasReceber = new JPanel(new GridBagLayout());
                panelContasReceber.setOpaque(false);
                
                GridBagConstraints gbcReceber = new GridBagConstraints();
                gbcReceber.insets = new Insets(5, 5, 5, 5);
                
                // Filtros Contas a Receber
                gbcReceber.gridx = 0; gbcReceber.gridy = 0;
                gbcReceber.fill = GridBagConstraints.NONE;
                gbcReceber.weightx = 0.2;
                JLabel lblClienteReceber = new JLabel("Cliente:");
                lblClienteReceber.setFont(new Font("Segoe UI", Font.BOLD, 12));
                lblClienteReceber.setForeground(DARK_GRAY);
                panelContasReceber.add(lblClienteReceber, gbcReceber);
                gbcReceber.gridx = 1; gbcReceber.gridy = 0;
                gbcReceber.fill = GridBagConstraints.HORIZONTAL;
                gbcReceber.weightx = 0.3;
                ElegantTextField txtClienteReceber = new ElegantTextField(30);
                txtClienteReceber.setToolTipText("Filtrar por cliente");
                panelContasReceber.add(txtClienteReceber, gbcReceber);
                
                gbcReceber.gridx = 2; gbcReceber.gridy = 0;
                gbcReceber.fill = GridBagConstraints.NONE;
                gbcReceber.weightx = 0.2;
                JLabel lblDocumentoReceber = new JLabel("Documento:");
                lblDocumentoReceber.setFont(new Font("Segoe UI", Font.BOLD, 12));
                lblDocumentoReceber.setForeground(DARK_GRAY);
                panelContasReceber.add(lblDocumentoReceber, gbcReceber);
                gbcReceber.gridx = 3; gbcReceber.gridy = 0;
                gbcReceber.fill = GridBagConstraints.HORIZONTAL;
                gbcReceber.weightx = 0.3;
                ElegantTextField txtDocumentoReceber = new ElegantTextField(20);
                txtDocumentoReceber.setToolTipText("Filtrar por documento");
                panelContasReceber.add(txtDocumentoReceber, gbcReceber);
                
                gbcReceber.gridx = 0; gbcReceber.gridy = 1;
                gbcReceber.fill = GridBagConstraints.NONE;
                gbcReceber.weightx = 0.2;
                JLabel lblStatusReceber = new JLabel("Status:");
                lblStatusReceber.setFont(new Font("Segoe UI", Font.BOLD, 12));
                lblStatusReceber.setForeground(DARK_GRAY);
                panelContasReceber.add(lblStatusReceber, gbcReceber);
                gbcReceber.gridx = 1; gbcReceber.gridy = 1;
                gbcReceber.fill = GridBagConstraints.HORIZONTAL;
                gbcReceber.weightx = 0.3;
                JComboBox<String> cbStatusReceber = new JComboBox<>(new String[]{
                    "Todas", "Abertas", "Vencidas", "Pagas", "Canceladas", "Renegociadas"
                });
                cbStatusReceber.setFont(new Font("Segoe UI", Font.PLAIN, 12));
                panelContasReceber.add(cbStatusReceber, gbcReceber);
                
                // Período e Valor Contas a Receber
                gbcReceber.gridx = 0; gbcReceber.gridy = 2;
                gbcReceber.fill = GridBagConstraints.NONE;
                gbcReceber.weightx = 0.2;
                JLabel lblPeriodoReceber = new JLabel("Período:");
                lblPeriodoReceber.setFont(new Font("Segoe UI", Font.BOLD, 12));
                lblPeriodoReceber.setForeground(DARK_GRAY);
                panelContasReceber.add(lblPeriodoReceber, gbcReceber);
                gbcReceber.gridx = 1; gbcReceber.gridy = 2;
                gbcReceber.fill = GridBagConstraints.HORIZONTAL;
                gbcReceber.weightx = 0.3;
                JPanel periodoPanelReceber = new JPanel(new FlowLayout());
                periodoPanelReceber.setOpaque(false);
                ElegantTextField txtDataInicioReceber = new ElegantTextField(10);
                ElegantTextField txtDataFimReceber = new ElegantTextField(10);
                periodoPanelReceber.add(txtDataInicioReceber);
                periodoPanelReceber.add(new JLabel(" até "));
                periodoPanelReceber.add(txtDataFimReceber);
                panelContasReceber.add(periodoPanelReceber, gbcReceber);
                
                gbcReceber.gridx = 2; gbcReceber.gridy = 2;
                gbcReceber.fill = GridBagConstraints.NONE;
                gbcReceber.weightx = 0.2;
                JLabel lblValorMinimoReceber = new JLabel("Valor Mínimo:");
                lblValorMinimoReceber.setFont(new Font("Segoe UI", Font.BOLD, 12));
                lblValorMinimoReceber.setForeground(DARK_GRAY);
                panelContasReceber.add(lblValorMinimoReceber, gbcReceber);
                gbcReceber.gridx = 3; gbcReceber.gridy = 2;
                gbcReceber.fill = GridBagConstraints.HORIZONTAL;
                gbcReceber.weightx = 0.3;
                ElegantTextField txtValorMinimoReceber = new ElegantTextField(15);
                txtValorMinimoReceber.setToolTipText("Filtrar por valor mínimo");
                panelContasReceber.add(txtValorMinimoReceber, gbcReceber);
                
                // Tabela Contas a Receber
                gbcReceber.gridx = 0; gbcReceber.gridy = 3; gbcReceber.gridwidth = 4;
                gbcReceber.fill = GridBagConstraints.BOTH;
                gbcReceber.weightx = 1.0; gbcReceber.weighty = 1.0;
                gbcReceber.insets = new Insets(10, 5, 5, 5);
                
                String[] colunasReceber = {"ID", "Cliente", "CPF/CNPJ", "Documento", "Emissão", "Vencimento", "Valor", "Juros", "Multa", "Total", "Status", "Forma Pagto", "Data Pagto", "Observações"};
                Object[][] dadosReceber = {
                    {"001", "João Silva", "123.456.789-00", "Boleto 123", "01/05/2026", "15/05/2026", "R$ 1.500,00", "R$ 0,00", "R$ 0,00", "R$ 1.500,00", "Aberta", "Boleto", "", ""},
                    {"002", "Maria Santos", "987.654.321-00", "Duplicata 456", "05/05/2026", "20/05/2026", "R$ 2.300,00", "R$ 0,00", "R$ 0,00", "R$ 2.300,00", "Aberta", "TED", "", ""},
                    {"003", "Pedro Oliveira", "456.789.123-00", "Cheque 789", "25/04/2026", "10/05/2026", "R$ 800,00", "R$ 40,00", "R$ 20,00", "R$ 860,00", "Vencida", "Cheque", "", "Atraso 5 dias"},
                    {"004", "Ana Costa", "789.123.456-00", "Promissória 012", "10/05/2026", "25/05/2026", "R$ 3.200,00", "R$ 0,00", "R$ 0,00", "R$ 3.200,00", "Aberta", "Depósito", "", ""},
                    {"005", "Carlos Ferreira", "321.654.987-00", "Boleto 345", "20/04/2026", "08/05/2026", "R$ 1.100,00", "R$ 0,00", "R$ 0,00", "R$ 1.100,00", "Paga", "Boleto", "07/05/2026", "Pago antes do vencimento"}
                };
                
                JTable tabelaReceber = new JTable(dadosReceber, colunasReceber);
                tabelaReceber.setFont(new Font("Segoe UI", Font.PLAIN, 11));
                tabelaReceber.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 12));
                tabelaReceber.getTableHeader().setBackground(new Color(230, 126, 34));
                tabelaReceber.getTableHeader().setForeground(WHITE);
                tabelaReceber.setRowHeight(25);
                tabelaReceber.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
                
                JScrollPane scrollReceber = new JScrollPane(tabelaReceber);
                scrollReceber.setPreferredSize(new Dimension(800, 200));
                panelContasReceber.add(scrollReceber, gbcReceber);
                
                // Botões de Ação Contas a Receber
                gbcReceber.gridx = 0; gbcReceber.gridy = 4; gbcReceber.gridwidth = 4;
                gbcReceber.fill = GridBagConstraints.HORIZONTAL;
                gbcReceber.weightx = 1.0; gbcReceber.weighty = 0;
                gbcReceber.insets = new Insets(5, 5, 5, 5);
                
                JPanel botoesReceber = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 5));
                botoesReceber.setOpaque(false);
                
                ElegantButton btnNovoReceber = new ElegantButton("🆕 Nova", SUCCESS_COLOR, false);
                btnNovoReceber.setForeground(WHITE);
                btnNovoReceber.setFont(new Font("Segoe UI", Font.BOLD, 12));
                btnNovoReceber.setPreferredSize(new Dimension(80, 35));
                botoesReceber.add(btnNovoReceber);
                
                ElegantButton btnEditarReceber = new ElegantButton("✏️ Editar", PRIMARY_COLOR, false);
                btnEditarReceber.setForeground(WHITE);
                btnEditarReceber.setFont(new Font("Segoe UI", Font.BOLD, 12));
                btnEditarReceber.setPreferredSize(new Dimension(80, 35));
                botoesReceber.add(btnEditarReceber);
                
                ElegantButton btnBaixarReceber = new ElegantButton("✅ Baixar", new Color(0, 150, 0), false);
                btnBaixarReceber.setForeground(WHITE);
                btnBaixarReceber.setFont(new Font("Segoe UI", Font.BOLD, 12));
                btnBaixarReceber.setPreferredSize(new Dimension(80, 35));
                botoesReceber.add(btnBaixarReceber);
                
                ElegantButton btnRenegociarReceber = new ElegantButton("🔄 Renegociar", new Color(255, 140, 0), false);
                btnRenegociarReceber.setForeground(WHITE);
                btnRenegociarReceber.setFont(new Font("Segoe UI", Font.BOLD, 12));
                btnRenegociarReceber.setPreferredSize(new Dimension(100, 35));
                botoesReceber.add(btnRenegociarReceber);
                
                ElegantButton btnEstornarReceber = new ElegantButton("↩️ Estornar", new Color(220, 20, 60), false);
                btnEstornarReceber.setForeground(WHITE);
                btnEstornarReceber.setFont(new Font("Segoe UI", Font.BOLD, 12));
                btnEstornarReceber.setPreferredSize(new Dimension(90, 35));
                botoesReceber.add(btnEstornarReceber);
                
                ElegantButton btnImprimirReceber = new ElegantButton("🖨️ Imprimir", new Color(100, 100, 100), false);
                btnImprimirReceber.setForeground(WHITE);
                btnImprimirReceber.setFont(new Font("Segoe UI", Font.BOLD, 12));
                btnImprimirReceber.setPreferredSize(new Dimension(90, 35));
                botoesReceber.add(btnImprimirReceber);
                
                ElegantButton btnExportarReceber = new ElegantButton("📊 Exportar", new Color(0, 120, 215), false);
                btnExportarReceber.setForeground(WHITE);
                btnExportarReceber.setFont(new Font("Segoe UI", Font.BOLD, 12));
                btnExportarReceber.setPreferredSize(new Dimension(90, 35));
                botoesReceber.add(btnExportarReceber);
                
                panelContasReceber.add(botoesReceber, gbcReceber);
                tabbedPaneContas.addTab("💰 Contas a Receber", panelContasReceber);
                
                // Aba 2: Contas a Pagar
                JPanel panelContasPagar = new JPanel(new GridBagLayout());
                panelContasPagar.setOpaque(false);
                
                GridBagConstraints gbcPagar = new GridBagConstraints();
                gbcPagar.insets = new Insets(5, 5, 5, 5);
                
                // Filtros Contas a Pagar
                gbcPagar.gridx = 0; gbcPagar.gridy = 0;
                gbcPagar.fill = GridBagConstraints.NONE;
                gbcPagar.weightx = 0.2;
                JLabel lblFornecedorPagar = new JLabel("Fornecedor:");
                lblFornecedorPagar.setFont(new Font("Segoe UI", Font.BOLD, 12));
                lblFornecedorPagar.setForeground(DARK_GRAY);
                panelContasPagar.add(lblFornecedorPagar, gbcPagar);
                gbcPagar.gridx = 1; gbcPagar.gridy = 0;
                gbcPagar.fill = GridBagConstraints.HORIZONTAL;
                gbcPagar.weightx = 0.3;
                ElegantTextField txtFornecedorPagar = new ElegantTextField(30);
                txtFornecedorPagar.setToolTipText("Filtrar por fornecedor");
                panelContasPagar.add(txtFornecedorPagar, gbcPagar);
                
                gbcPagar.gridx = 2; gbcPagar.gridy = 0;
                gbcPagar.fill = GridBagConstraints.NONE;
                gbcPagar.weightx = 0.2;
                JLabel lblDocumentoPagarTab = new JLabel("Documento:");
                lblDocumentoPagarTab.setFont(new Font("Segoe UI", Font.BOLD, 12));
                lblDocumentoPagarTab.setForeground(DARK_GRAY);
                panelContasPagar.add(lblDocumentoPagarTab, gbcPagar);
                gbcPagar.gridx = 3; gbcPagar.gridy = 0;
                gbcPagar.fill = GridBagConstraints.HORIZONTAL;
                gbcPagar.weightx = 0.3;
                ElegantTextField txtDocumentoPagarTab = new ElegantTextField(20);
                txtDocumentoPagarTab.setToolTipText("Filtrar por documento");
                panelContasPagar.add(txtDocumentoPagarTab, gbcPagar);
                
                gbcPagar.gridx = 0; gbcPagar.gridy = 1;
                gbcPagar.fill = GridBagConstraints.NONE;
                gbcPagar.weightx = 0.2;
                JLabel lblStatusPagar = new JLabel("Status:");
                lblStatusPagar.setFont(new Font("Segoe UI", Font.BOLD, 12));
                lblStatusPagar.setForeground(DARK_GRAY);
                panelContasPagar.add(lblStatusPagar, gbcPagar);
                gbcPagar.gridx = 1; gbcPagar.gridy = 1;
                gbcPagar.fill = GridBagConstraints.HORIZONTAL;
                gbcPagar.weightx = 0.3;
                JComboBox<String> cbStatusPagar = new JComboBox<>(new String[]{
                    "Todas", "Abertas", "Vencidas", "Pagas", "Canceladas", "Renegociadas"
                });
                cbStatusPagar.setFont(new Font("Segoe UI", Font.PLAIN, 12));
                panelContasPagar.add(cbStatusPagar, gbcPagar);
                
                // Período e Valor Contas a Pagar
                gbcPagar.gridx = 0; gbcPagar.gridy = 2;
                gbcPagar.fill = GridBagConstraints.NONE;
                gbcPagar.weightx = 0.2;
                JLabel lblPeriodoPagar = new JLabel("Período:");
                lblPeriodoPagar.setFont(new Font("Segoe UI", Font.BOLD, 12));
                lblPeriodoPagar.setForeground(DARK_GRAY);
                panelContasPagar.add(lblPeriodoPagar, gbcPagar);
                gbcPagar.gridx = 1; gbcPagar.gridy = 2;
                gbcPagar.fill = GridBagConstraints.HORIZONTAL;
                gbcPagar.weightx = 0.3;
                JPanel periodoPanelPagar = new JPanel(new FlowLayout());
                periodoPanelPagar.setOpaque(false);
                ElegantTextField txtDataInicioPagar = new ElegantTextField(10);
                ElegantTextField txtDataFimPagar = new ElegantTextField(10);
                periodoPanelPagar.add(txtDataInicioPagar);
                periodoPanelPagar.add(new JLabel(" até "));
                periodoPanelPagar.add(txtDataFimPagar);
                panelContasPagar.add(periodoPanelPagar, gbcPagar);
                
                gbcPagar.gridx = 2; gbcPagar.gridy = 2;
                gbcPagar.fill = GridBagConstraints.NONE;
                gbcPagar.weightx = 0.2;
                JLabel lblValorMinimoPagar = new JLabel("Valor Mínimo:");
                lblValorMinimoPagar.setFont(new Font("Segoe UI", Font.BOLD, 12));
                lblValorMinimoPagar.setForeground(DARK_GRAY);
                panelContasPagar.add(lblValorMinimoPagar, gbcPagar);
                gbcPagar.gridx = 3; gbcPagar.gridy = 2;
                gbcPagar.fill = GridBagConstraints.HORIZONTAL;
                gbcPagar.weightx = 0.3;
                ElegantTextField txtValorMinimoPagar = new ElegantTextField(15);
                txtValorMinimoPagar.setToolTipText("Filtrar por valor mínimo");
                panelContasPagar.add(txtValorMinimoPagar, gbcPagar);
                
                // Tabela Contas a Pagar
                gbcPagar.gridx = 0; gbcPagar.gridy = 3; gbcPagar.gridwidth = 4;
                gbcPagar.fill = GridBagConstraints.BOTH;
                gbcPagar.weightx = 1.0; gbcPagar.weighty = 1.0;
                gbcPagar.insets = new Insets(10, 5, 5, 5);
                
                String[] colunasPagar = {"ID", "Fornecedor", "CNPJ", "Documento", "Emissão", "Vencimento", "Valor", "Juros", "Multa", "Total", "Status", "Forma Pagto", "Data Pagto", "Observações"};
                Object[][] dadosPagar = {
                    {"001", "Tech Solutions", "12.345.678/0001-90", "Boleto 987", "01/05/2026", "12/05/2026", "R$ 2.500,00", "R$ 0,00", "R$ 0,00", "R$ 2.500,00", "Aberta", "Boleto", "", ""},
                    {"002", "Office Supply", "23.456.789/0001-23", "Duplicata 654", "03/05/2026", "18/05/2026", "R$ 1.800,00", "R$ 0,00", "R$ 0,00", "R$ 1.800,00", "Aberta", "TED", "", ""},
                    {"003", "Equipamentos Ltda", "34.567.890/0001-45", "Cheque 321", "20/04/2026", "05/05/2026", "R$ 4.200,00", "R$ 210,00", "R$ 105,00", "R$ 4.515,00", "Vencida", "Cheque", "", "Atraso 7 dias"},
                    {"004", "Software S.A.", "45.678.901/0001-67", "Fatura 147", "08/05/2026", "22/05/2026", "R$ 990,00", "R$ 0,00", "R$ 0,00", "R$ 990,00", "Aberta", "Cartão", "", ""},
                    {"005", "Material Construção", "56.789.012/0001-89", "Boleto 258", "25/04/2026", "06/05/2026", "R$ 3.600,00", "R$ 0,00", "R$ 0,00", "R$ 3.600,00", "Paga", "Boleto", "05/05/2026", "Pago antes do vencimento"}
                };
                
                JTable tabelaPagar = new JTable(dadosPagar, colunasPagar);
                tabelaPagar.setFont(new Font("Segoe UI", Font.PLAIN, 11));
                tabelaPagar.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 12));
                tabelaPagar.getTableHeader().setBackground(new Color(230, 126, 34));
                tabelaPagar.getTableHeader().setForeground(WHITE);
                tabelaPagar.setRowHeight(25);
                tabelaPagar.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
                
                JScrollPane scrollPagar = new JScrollPane(tabelaPagar);
                scrollPagar.setPreferredSize(new Dimension(800, 200));
                panelContasPagar.add(scrollPagar, gbcPagar);
                
                // Botões de Ação Contas a Pagar
                gbcPagar.gridx = 0; gbcPagar.gridy = 4; gbcPagar.gridwidth = 4;
                gbcPagar.fill = GridBagConstraints.HORIZONTAL;
                gbcPagar.weightx = 1.0; gbcPagar.weighty = 0;
                gbcPagar.insets = new Insets(5, 5, 5, 5);
                
                JPanel botoesPagar = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 5));
                botoesPagar.setOpaque(false);
                
                ElegantButton btnNovoPagar = new ElegantButton("🆕 Nova", SUCCESS_COLOR, false);
                btnNovoPagar.setForeground(WHITE);
                btnNovoPagar.setFont(new Font("Segoe UI", Font.BOLD, 12));
                btnNovoPagar.setPreferredSize(new Dimension(80, 35));
                botoesPagar.add(btnNovoPagar);
                
                ElegantButton btnEditarPagar = new ElegantButton("✏️ Editar", PRIMARY_COLOR, false);
                btnEditarPagar.setForeground(WHITE);
                btnEditarPagar.setFont(new Font("Segoe UI", Font.BOLD, 12));
                btnEditarPagar.setPreferredSize(new Dimension(80, 35));
                botoesPagar.add(btnEditarPagar);
                
                ElegantButton btnPagarPagar = new ElegantButton("💳 Pagar", new Color(0, 150, 0), false);
                btnPagarPagar.setForeground(WHITE);
                btnPagarPagar.setFont(new Font("Segoe UI", Font.BOLD, 12));
                btnPagarPagar.setPreferredSize(new Dimension(80, 35));
                botoesPagar.add(btnPagarPagar);
                
                ElegantButton btnRenegociarPagar = new ElegantButton("🔄 Renegociar", new Color(255, 140, 0), false);
                btnRenegociarPagar.setForeground(WHITE);
                btnRenegociarPagar.setFont(new Font("Segoe UI", Font.BOLD, 12));
                btnRenegociarPagar.setPreferredSize(new Dimension(100, 35));
                botoesPagar.add(btnRenegociarPagar);
                
                ElegantButton btnCancelarPagarTab = new ElegantButton("❌ Cancelar", new Color(220, 20, 60), false);
                btnCancelarPagarTab.setForeground(WHITE);
                btnCancelarPagarTab.setFont(new Font("Segoe UI", Font.BOLD, 12));
                btnCancelarPagarTab.setPreferredSize(new Dimension(90, 35));
                botoesPagar.add(btnCancelarPagarTab);
                
                ElegantButton btnImprimirPagarTab = new ElegantButton("🖨️ Imprimir", new Color(100, 100, 100), false);
                btnImprimirPagarTab.setForeground(WHITE);
                btnImprimirPagarTab.setFont(new Font("Segoe UI", Font.BOLD, 12));
                btnImprimirPagarTab.setPreferredSize(new Dimension(90, 35));
                botoesPagar.add(btnImprimirPagarTab);
                
                ElegantButton btnExportarPagar = new ElegantButton("📊 Exportar", new Color(0, 120, 215), false);
                btnExportarPagar.setForeground(WHITE);
                btnExportarPagar.setFont(new Font("Segoe UI", Font.BOLD, 12));
                btnExportarPagar.setPreferredSize(new Dimension(90, 35));
                botoesPagar.add(btnExportarPagar);
                
                panelContasPagar.add(botoesPagar, gbcPagar);
                tabbedPaneContas.addTab("💸 Contas a Pagar", panelContasPagar);
                
                panel.add(tabbedPaneContas, gbc);
                break;
                
            case "💰 Contas a Receber":
                // Cliente
                gbc.gridx = 0; gbc.gridy = 0;
                JLabel lblCliente = new JLabel("Cliente:");
                lblCliente.setFont(new Font("Segoe UI", Font.BOLD, 12));
                lblCliente.setForeground(DARK_GRAY);
                panel.add(lblCliente, gbc);
                gbc.gridx = 1; gbc.gridy = 0;
                ElegantTextField txtCliente = new ElegantTextField(30);
                panel.add(txtCliente, gbc);
                
                // Valor
                gbc.gridx = 0; gbc.gridy = 1;
                JLabel lblValor = new JLabel("Valor:");
                lblValor.setFont(new Font("Segoe UI", Font.BOLD, 12));
                lblValor.setForeground(DARK_GRAY);
                panel.add(lblValor, gbc);
                gbc.gridx = 1; gbc.gridy = 1;
                ElegantTextField txtValor = new ElegantTextField(15);
                panel.add(txtValor, gbc);
                
                // Data de Vencimento
                gbc.gridx = 0; gbc.gridy = 2;
                JLabel lblVencimento = new JLabel("Vencimento:");
                lblVencimento.setFont(new Font("Segoe UI", Font.BOLD, 12));
                lblVencimento.setForeground(DARK_GRAY);
                panel.add(lblVencimento, gbc);
                gbc.gridx = 1; gbc.gridy = 2;
                ElegantTextField txtVencimento = new ElegantTextField(10);
                panel.add(txtVencimento, gbc);
                
                // Tipo de Documento
                gbc.gridx = 0; gbc.gridy = 3;
                JLabel lblTipoDoc = new JLabel("Tipo Doc:");
                lblTipoDoc.setFont(new Font("Segoe UI", Font.BOLD, 12));
                lblTipoDoc.setForeground(DARK_GRAY);
                panel.add(lblTipoDoc, gbc);
                gbc.gridx = 1; gbc.gridy = 3;
                JComboBox<String> cbTipoDoc = new JComboBox<>(new String[]{"Boleto", "Duplicata", "Cheque", "Promissória"});
                cbTipoDoc.setFont(new Font("Segoe UI", Font.PLAIN, 12));
                panel.add(cbTipoDoc, gbc);
                break;
                
            case "💸 Contas a Pagar":
                // Painel de Dados da Conta a Pagar
                gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
                gbc.fill = GridBagConstraints.HORIZONTAL;
                gbc.insets = new Insets(5, 5, 15, 5);
                
                JPanel dadosPanel = new JPanel(new GridBagLayout());
                dadosPanel.setBorder(BorderFactory.createTitledBorder(
                    BorderFactory.createEtchedBorder(), "💸 Dados da Conta a Pagar",
                    TitledBorder.LEFT, TitledBorder.TOP, new Font("Segoe UI", Font.BOLD, 12),
                    DARK_GRAY));
                
                GridBagConstraints gbcDados = new GridBagConstraints();
                gbcDados.insets = new Insets(5, 5, 5, 5);
                
                // ID da Conta
                gbcDados.gridx = 0; gbcDados.gridy = 0;
                gbcDados.fill = GridBagConstraints.NONE;
                gbcDados.weightx = 0.3;
                JLabel lblIdConta = new JLabel("ID Conta:");
                lblIdConta.setFont(new Font("Segoe UI", Font.BOLD, 12));
                lblIdConta.setForeground(DARK_GRAY);
                dadosPanel.add(lblIdConta, gbcDados);
                gbcDados.gridx = 1; gbcDados.gridy = 0;
                gbcDados.fill = GridBagConstraints.HORIZONTAL;
                gbcDados.weightx = 0.7;
                ElegantTextField txtIdConta = new ElegantTextField(15);
                txtIdConta.setEditable(false);
                txtIdConta.setText("AUTO");
                txtIdConta.setToolTipText("ID automático da conta");
                dadosPanel.add(txtIdConta, gbcDados);
                
                // Fornecedor
                gbcDados.gridx = 2; gbcDados.gridy = 0;
                gbcDados.fill = GridBagConstraints.NONE;
                gbcDados.weightx = 0.3;
                JLabel lblFornecedor = new JLabel("Fornecedor:");
                lblFornecedor.setFont(new Font("Segoe UI", Font.BOLD, 12));
                lblFornecedor.setForeground(DARK_GRAY);
                dadosPanel.add(lblFornecedor, gbcDados);
                gbcDados.gridx = 3; gbcDados.gridy = 0;
                gbcDados.fill = GridBagConstraints.HORIZONTAL;
                gbcDados.weightx = 0.7;
                ElegantTextField txtFornecedor = new ElegantTextField(30);
                txtFornecedor.setToolTipText("Nome do fornecedor");
                dadosPanel.add(txtFornecedor, gbcDados);
                
                // Documento
                gbcDados.gridx = 0; gbcDados.gridy = 1;
                gbcDados.fill = GridBagConstraints.NONE;
                gbcDados.weightx = 0.3;
                JLabel lblDocumentoPagar = new JLabel("Documento:");
                lblDocumentoPagar.setFont(new Font("Segoe UI", Font.BOLD, 12));
                lblDocumentoPagar.setForeground(DARK_GRAY);
                dadosPanel.add(lblDocumentoPagar, gbcDados);
                gbcDados.gridx = 1; gbcDados.gridy = 1;
                gbcDados.fill = GridBagConstraints.HORIZONTAL;
                gbcDados.weightx = 0.7;
                ElegantTextField txtDocumentoPagar = new ElegantTextField(20);
                txtDocumentoPagar.setToolTipText("Número do documento");
                dadosPanel.add(txtDocumentoPagar, gbcDados);
                
                // Tipo de Documento
                gbcDados.gridx = 2; gbcDados.gridy = 1;
                gbcDados.fill = GridBagConstraints.NONE;
                gbcDados.weightx = 0.3;
                JLabel lblTipoDocumentoPagar = new JLabel("Tipo Doc:");
                lblTipoDocumentoPagar.setFont(new Font("Segoe UI", Font.BOLD, 12));
                lblTipoDocumentoPagar.setForeground(DARK_GRAY);
                dadosPanel.add(lblTipoDocumentoPagar, gbcDados);
                gbcDados.gridx = 3; gbcDados.gridy = 1;
                gbcDados.fill = GridBagConstraints.HORIZONTAL;
                gbcDados.weightx = 0.7;
                JComboBox<String> cbTipoDocumentoPagar = new JComboBox<>(new String[]{
                    "Boleto", "Duplicata", "Cheque", "Promissória", "Nota Fiscal", "Fatura", "Recibo"
                });
                cbTipoDocumentoPagar.setFont(new Font("Segoe UI", Font.PLAIN, 12));
                cbTipoDocumentoPagar.setToolTipText("Tipo de documento");
                dadosPanel.add(cbTipoDocumentoPagar, gbcDados);
                
                // Data de Emissão
                gbcDados.gridx = 0; gbcDados.gridy = 2;
                gbcDados.fill = GridBagConstraints.NONE;
                gbcDados.weightx = 0.3;
                JLabel lblDataEmissaoPagar = new JLabel("Data Emissão:");
                lblDataEmissaoPagar.setFont(new Font("Segoe UI", Font.BOLD, 12));
                lblDataEmissaoPagar.setForeground(DARK_GRAY);
                dadosPanel.add(lblDataEmissaoPagar, gbcDados);
                gbcDados.gridx = 1; gbcDados.gridy = 2;
                gbcDados.fill = GridBagConstraints.HORIZONTAL;
                gbcDados.weightx = 0.7;
                ElegantTextField txtDataEmissaoPagar = new ElegantTextField(10);
                txtDataEmissaoPagar.setToolTipText("Data de emissão do documento");
                dadosPanel.add(txtDataEmissaoPagar, gbcDados);
                
                // Data de Vencimento
                gbcDados.gridx = 2; gbcDados.gridy = 2;
                gbcDados.fill = GridBagConstraints.NONE;
                gbcDados.weightx = 0.3;
                JLabel lblDataVencimentoPagar = new JLabel("Data Vencimento:");
                lblDataVencimentoPagar.setFont(new Font("Segoe UI", Font.BOLD, 12));
                lblDataVencimentoPagar.setForeground(DARK_GRAY);
                dadosPanel.add(lblDataVencimentoPagar, gbcDados);
                gbcDados.gridx = 3; gbcDados.gridy = 2;
                gbcDados.fill = GridBagConstraints.HORIZONTAL;
                gbcDados.weightx = 0.7;
                ElegantTextField txtDataVencimentoPagar = new ElegantTextField(10);
                txtDataVencimentoPagar.setToolTipText("Data de vencimento");
                dadosPanel.add(txtDataVencimentoPagar, gbcDados);
                
                // Valor Original
                gbcDados.gridx = 0; gbcDados.gridy = 3;
                gbcDados.fill = GridBagConstraints.NONE;
                gbcDados.weightx = 0.3;
                JLabel lblValorOriginalPagar = new JLabel("Valor Original:");
                lblValorOriginalPagar.setFont(new Font("Segoe UI", Font.BOLD, 12));
                lblValorOriginalPagar.setForeground(DARK_GRAY);
                dadosPanel.add(lblValorOriginalPagar, gbcDados);
                gbcDados.gridx = 1; gbcDados.gridy = 3;
                gbcDados.fill = GridBagConstraints.HORIZONTAL;
                gbcDados.weightx = 0.7;
                ElegantTextField txtValorOriginalPagar = new ElegantTextField(15);
                txtValorOriginalPagar.setToolTipText("Valor original da conta");
                dadosPanel.add(txtValorOriginalPagar, gbcDados);
                
                // Valor Pago
                gbcDados.gridx = 2; gbcDados.gridy = 3;
                gbcDados.fill = GridBagConstraints.NONE;
                gbcDados.weightx = 0.3;
                JLabel lblValorPagoPagar = new JLabel("Valor Pago:");
                lblValorPagoPagar.setFont(new Font("Segoe UI", Font.BOLD, 12));
                lblValorPagoPagar.setForeground(DARK_GRAY);
                dadosPanel.add(lblValorPagoPagar, gbcDados);
                gbcDados.gridx = 3; gbcDados.gridy = 3;
                gbcDados.fill = GridBagConstraints.HORIZONTAL;
                gbcDados.weightx = 0.7;
                ElegantTextField txtValorPagoPagar = new ElegantTextField(15);
                txtValorPagoPagar.setEditable(false);
                txtValorPagoPagar.setText("R$ 0,00");
                txtValorPagoPagar.setToolTipText("Valor já pago");
                txtValorPagoPagar.setFont(new Font("Segoe UI", Font.BOLD, 12));
                txtValorPagoPagar.setForeground(new Color(0, 150, 0));
                dadosPanel.add(txtValorPagoPagar, gbcDados);
                
                // Saldo Devedor
                gbcDados.gridx = 0; gbcDados.gridy = 4;
                gbcDados.fill = GridBagConstraints.NONE;
                gbcDados.weightx = 0.3;
                JLabel lblSaldoDevedorPagar = new JLabel("Saldo Devedor:");
                lblSaldoDevedorPagar.setFont(new Font("Segoe UI", Font.BOLD, 12));
                lblSaldoDevedorPagar.setForeground(DARK_GRAY);
                dadosPanel.add(lblSaldoDevedorPagar, gbcDados);
                gbcDados.gridx = 1; gbcDados.gridy = 4;
                gbcDados.fill = GridBagConstraints.HORIZONTAL;
                gbcDados.weightx = 0.7;
                ElegantTextField txtSaldoDevedorPagar = new ElegantTextField(15);
                txtSaldoDevedorPagar.setEditable(false);
                txtSaldoDevedorPagar.setText("R$ 0,00");
                txtSaldoDevedorPagar.setToolTipText("Saldo devedor atual");
                txtSaldoDevedorPagar.setFont(new Font("Segoe UI", Font.BOLD, 12));
                txtSaldoDevedorPagar.setForeground(new Color(200, 50, 50));
                dadosPanel.add(txtSaldoDevedorPagar, gbcDados);
                
                // Categoria
                gbcDados.gridx = 2; gbcDados.gridy = 4;
                gbcDados.fill = GridBagConstraints.NONE;
                gbcDados.weightx = 0.3;
                JLabel lblCategoria = new JLabel("Categoria:");
                lblCategoria.setFont(new Font("Segoe UI", Font.BOLD, 12));
                lblCategoria.setForeground(DARK_GRAY);
                dadosPanel.add(lblCategoria, gbcDados);
                gbcDados.gridx = 3; gbcDados.gridy = 4;
                gbcDados.fill = GridBagConstraints.HORIZONTAL;
                gbcDados.weightx = 0.7;
                JComboBox<String> cbCategoria = new JComboBox<>(new String[]{
                    "Fornecedores", "Aluguel", "Água", "Luz", "Telefone", "Internet", 
                    "Material de Escritório", "Equipamentos", "Serviços", "Impostos", 
                    "Marketing", "Transporte", "Outros"
                });
                cbCategoria.setFont(new Font("Segoe UI", Font.PLAIN, 12));
                cbCategoria.setToolTipText("Categoria da despesa");
                dadosPanel.add(cbCategoria, gbcDados);
                
                // Status
                gbcDados.gridx = 0; gbcDados.gridy = 5;
                gbcDados.fill = GridBagConstraints.NONE;
                gbcDados.weightx = 0.3;
                JLabel lblStatus = new JLabel("Status:");
                lblStatus.setFont(new Font("Segoe UI", Font.BOLD, 12));
                lblStatus.setForeground(DARK_GRAY);
                dadosPanel.add(lblStatus, gbcDados);
                gbcDados.gridx = 1; gbcDados.gridy = 5;
                gbcDados.fill = GridBagConstraints.HORIZONTAL;
                gbcDados.weightx = 0.7;
                JComboBox<String> cbStatus = new JComboBox<>(new String[]{
                    "Aberta", "Paga", "Parcialmente Paga", "Vencida", "Cancelada", "Renegociada"
                });
                cbStatus.setFont(new Font("Segoe UI", Font.PLAIN, 12));
                cbStatus.setToolTipText("Status da conta");
                dadosPanel.add(cbStatus, gbcDados);
                
                // Forma de Pagamento
                gbcDados.gridx = 2; gbcDados.gridy = 5;
                gbcDados.fill = GridBagConstraints.NONE;
                gbcDados.weightx = 0.3;
                JLabel lblFormaPagamentoPagar = new JLabel("Forma Pagto:");
                lblFormaPagamentoPagar.setFont(new Font("Segoe UI", Font.BOLD, 12));
                lblFormaPagamentoPagar.setForeground(DARK_GRAY);
                dadosPanel.add(lblFormaPagamentoPagar, gbcDados);
                gbcDados.gridx = 3; gbcDados.gridy = 5;
                gbcDados.fill = GridBagConstraints.HORIZONTAL;
                gbcDados.weightx = 0.7;
                JComboBox<String> cbFormaPagamentoPagar = new JComboBox<>(new String[]{
                    "Dinheiro", "Transferência", "TED", "DOC", "Boleto", "Cheque", 
                    "Cartão de Crédito", "Cartão de Débito", "PIX", "Outros"
                });
                cbFormaPagamentoPagar.setFont(new Font("Segoe UI", Font.PLAIN, 12));
                cbFormaPagamentoPagar.setToolTipText("Forma de pagamento");
                dadosPanel.add(cbFormaPagamentoPagar, gbcDados);
                
                // Observações
                gbcDados.gridx = 0; gbcDados.gridy = 6;
                gbcDados.fill = GridBagConstraints.NONE;
                gbcDados.weightx = 0.3;
                JLabel lblObservacoesPagar = new JLabel("Observações:");
                lblObservacoesPagar.setFont(new Font("Segoe UI", Font.BOLD, 12));
                lblObservacoesPagar.setForeground(DARK_GRAY);
                dadosPanel.add(lblObservacoesPagar, gbcDados);
                gbcDados.gridx = 1; gbcDados.gridy = 6;
                gbcDados.fill = GridBagConstraints.HORIZONTAL;
                gbcDados.weightx = 2.7;
                gbcDados.gridwidth = 3;
                ElegantTextField txtObservacoesPagar = new ElegantTextField(60);
                txtObservacoesPagar.setToolTipText("Observações adicionais");
                dadosPanel.add(txtObservacoesPagar, gbcDados);
                
                panel.add(dadosPanel, gbc);
                
                // Painel de Histórico de Pagamentos
                gbc.gridx = 0; gbc.gridy = 1; gbc.gridwidth = 2;
                gbc.fill = GridBagConstraints.BOTH;
                gbc.weightx = 1.0; gbc.weighty = 1.0;
                
                String[] colunasTabelaPagamentos = {"Data", "Forma Pagto", "Valor", "Observações", "Operador"};
                Object[][] dadosTabelaPagamentos = {};
                
                JTable tabelaPagamentos = new JTable(dadosTabelaPagamentos, colunasTabelaPagamentos);
                tabelaPagamentos.setFont(new Font("Segoe UI", Font.PLAIN, 11));
                tabelaPagamentos.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 12));
                tabelaPagamentos.getTableHeader().setBackground(new Color(230, 126, 34));
                tabelaPagamentos.getTableHeader().setForeground(WHITE);
                tabelaPagamentos.setRowHeight(25);
                tabelaPagamentos.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
                tabelaPagamentos.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
                
                JScrollPane scrollPaneTabelaPagamentos = new JScrollPane(tabelaPagamentos);
                scrollPaneTabelaPagamentos.setPreferredSize(new Dimension(1200, 200));
                scrollPaneTabelaPagamentos.setBorder(BorderFactory.createTitledBorder(
                    BorderFactory.createEtchedBorder(), "💳 Histórico de Pagamentos",
                    TitledBorder.LEFT, TitledBorder.TOP, new Font("Segoe UI", Font.BOLD, 12),
                    DARK_GRAY));
                panel.add(scrollPaneTabelaPagamentos, gbc);
                
                // Painel de Ações
                gbc.gridx = 0; gbc.gridy = 2; gbc.gridwidth = 2;
                gbc.fill = GridBagConstraints.HORIZONTAL;
                gbc.weightx = 1.0; gbc.weighty = 0;
                gbc.insets = new Insets(10, 5, 5, 5);
                
                JPanel acoesPanelPagar = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 5));
                acoesPanelPagar.setOpaque(false);
                
                ElegantButton btnNovaConta = new ElegantButton("🆕 Nova", SUCCESS_COLOR, false);
                btnNovaConta.setForeground(WHITE);
                btnNovaConta.setFont(new Font("Segoe UI", Font.BOLD, 14));
                btnNovaConta.setPreferredSize(new Dimension(100, 40));
                btnNovaConta.setToolTipText("Criar nova conta a pagar");
                acoesPanelPagar.add(btnNovaConta);
                
                ElegantButton btnPagar = new ElegantButton("💳 Pagar", PRIMARY_COLOR, false);
                btnPagar.setForeground(WHITE);
                btnPagar.setFont(new Font("Segoe UI", Font.BOLD, 14));
                btnPagar.setPreferredSize(new Dimension(100, 40));
                btnPagar.setToolTipText("Registrar pagamento");
                acoesPanelPagar.add(btnPagar);
                
                ElegantButton btnEstornar = new ElegantButton("↩️ Estornar", WARNING_COLOR, false);
                btnEstornar.setForeground(WHITE);
                btnEstornar.setFont(new Font("Segoe UI", Font.BOLD, 14));
                btnEstornar.setPreferredSize(new Dimension(100, 40));
                btnEstornar.setToolTipText("Estornar pagamento");
                acoesPanelPagar.add(btnEstornar);
                
                ElegantButton btnImprimirPagar = new ElegantButton("🖨️ Imprimir", new Color(100, 100, 100), false);
                btnImprimirPagar.setForeground(WHITE);
                btnImprimirPagar.setFont(new Font("Segoe UI", Font.BOLD, 14));
                btnImprimirPagar.setPreferredSize(new Dimension(100, 40));
                btnImprimirPagar.setToolTipText("Imprimir conta");
                acoesPanelPagar.add(btnImprimirPagar);
                
                ElegantButton btnEmail = new ElegantButton("📧 Email", new Color(0, 120, 215), false);
                btnEmail.setForeground(WHITE);
                btnEmail.setFont(new Font("Segoe UI", Font.BOLD, 14));
                btnEmail.setPreferredSize(new Dimension(100, 40));
                btnEmail.setToolTipText("Enviar por email");
                acoesPanelPagar.add(btnEmail);
                
                ElegantButton btnCancelarPagar = new ElegantButton("❌ Cancelar", new Color(200, 50, 50), false);
                btnCancelarPagar.setForeground(WHITE);
                btnCancelarPagar.setFont(new Font("Segoe UI", Font.BOLD, 14));
                btnCancelarPagar.setPreferredSize(new Dimension(100, 40));
                btnCancelarPagar.setToolTipText("Cancelar conta");
                acoesPanelPagar.add(btnCancelarPagar);
                
                panel.add(acoesPanelPagar, gbc);
                break;
                
            case "🏦 Fluxo de Caixa":
                // Painel de Dados da Movimentação
                gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
                gbc.fill = GridBagConstraints.HORIZONTAL;
                gbc.insets = new Insets(5, 5, 15, 5);
                
                JPanel dadosPanelMovimentacao = new JPanel(new GridBagLayout());
                dadosPanelMovimentacao.setBorder(BorderFactory.createTitledBorder(
                    BorderFactory.createEtchedBorder(), "🏦 Dados da Movimentação",
                    TitledBorder.LEFT, TitledBorder.TOP, new Font("Segoe UI", Font.BOLD, 12),
                    DARK_GRAY));
                
                GridBagConstraints gbcDadosMovimentacao = new GridBagConstraints();
                gbcDadosMovimentacao.insets = new Insets(5, 5, 5, 5);
                
                // ID da Movimentação
                gbcDadosMovimentacao.gridx = 0; gbcDadosMovimentacao.gridy = 0;
                gbcDadosMovimentacao.fill = GridBagConstraints.NONE;
                gbcDadosMovimentacao.weightx = 0.3;
                JLabel lblIdMovimentacao = new JLabel("ID Movimentação:");
                lblIdMovimentacao.setFont(new Font("Segoe UI", Font.BOLD, 12));
                lblIdMovimentacao.setForeground(DARK_GRAY);
                dadosPanelMovimentacao.add(lblIdMovimentacao, gbcDadosMovimentacao);
                gbcDadosMovimentacao.gridx = 1; gbcDadosMovimentacao.gridy = 0;
                gbcDadosMovimentacao.fill = GridBagConstraints.HORIZONTAL;
                gbcDadosMovimentacao.weightx = 0.7;
                ElegantTextField txtIdMovimentacao = new ElegantTextField(15);
                txtIdMovimentacao.setEditable(false);
                txtIdMovimentacao.setText("AUTO");
                txtIdMovimentacao.setToolTipText("ID automático da movimentação");
                dadosPanelMovimentacao.add(txtIdMovimentacao, gbcDadosMovimentacao);
                
                // Data da Movimentação
                gbcDadosMovimentacao.gridx = 2; gbcDadosMovimentacao.gridy = 0;
                gbcDadosMovimentacao.fill = GridBagConstraints.NONE;
                gbcDadosMovimentacao.weightx = 0.3;
                JLabel lblDataMovimentacao = new JLabel("Data Movimentação:");
                lblDataMovimentacao.setFont(new Font("Segoe UI", Font.BOLD, 12));
                lblDataMovimentacao.setForeground(DARK_GRAY);
                dadosPanelMovimentacao.add(lblDataMovimentacao, gbcDadosMovimentacao);
                gbcDadosMovimentacao.gridx = 3; gbcDadosMovimentacao.gridy = 0;
                gbcDadosMovimentacao.fill = GridBagConstraints.HORIZONTAL;
                gbcDadosMovimentacao.weightx = 0.7;
                ElegantTextField txtDataMovimentacao = new ElegantTextField(10);
                txtDataMovimentacao.setToolTipText("Data da movimentação");
                dadosPanelMovimentacao.add(txtDataMovimentacao, gbcDadosMovimentacao);
                
                // Tipo de Movimentação
                gbcDadosMovimentacao.gridx = 0; gbcDadosMovimentacao.gridy = 1;
                gbcDadosMovimentacao.fill = GridBagConstraints.NONE;
                gbcDadosMovimentacao.weightx = 0.3;
                JLabel lblTipoMovimentacao = new JLabel("Tipo Movimentação:");
                lblTipoMovimentacao.setFont(new Font("Segoe UI", Font.BOLD, 12));
                lblTipoMovimentacao.setForeground(DARK_GRAY);
                dadosPanelMovimentacao.add(lblTipoMovimentacao, gbcDadosMovimentacao);
                gbcDadosMovimentacao.gridx = 1; gbcDadosMovimentacao.gridy = 1;
                gbcDadosMovimentacao.fill = GridBagConstraints.HORIZONTAL;
                gbcDadosMovimentacao.weightx = 0.7;
                JComboBox<String> cbTipoMovimentacao = new JComboBox<>(new String[]{
                    "Entrada", "Saída", "Transferência", "Sangria", "Suprimento", "Ajuste"
                });
                cbTipoMovimentacao.setFont(new Font("Segoe UI", Font.PLAIN, 12));
                cbTipoMovimentacao.setToolTipText("Tipo de movimentação");
                dadosPanelMovimentacao.add(cbTipoMovimentacao, gbcDadosMovimentacao);
                
                // Categoria
                gbcDadosMovimentacao.gridx = 2; gbcDadosMovimentacao.gridy = 1;
                gbcDadosMovimentacao.fill = GridBagConstraints.NONE;
                gbcDadosMovimentacao.weightx = 0.3;
                JLabel lblCategoriaMovimentacao = new JLabel("Categoria:");
                lblCategoriaMovimentacao.setFont(new Font("Segoe UI", Font.BOLD, 12));
                lblCategoriaMovimentacao.setForeground(DARK_GRAY);
                dadosPanelMovimentacao.add(lblCategoriaMovimentacao, gbcDadosMovimentacao);
                gbcDadosMovimentacao.gridx = 3; gbcDadosMovimentacao.gridy = 1;
                gbcDadosMovimentacao.fill = GridBagConstraints.HORIZONTAL;
                gbcDadosMovimentacao.weightx = 0.7;
                JComboBox<String> cbCategoriaMovimentacao = new JComboBox<>(new String[]{
                    "Vendas", "Compras", "Serviços", "Aluguel", "Água", "Luz", "Telefone", 
                    "Internet", "Material de Escritório", "Equipamentos", "Impostos", 
                    "Marketing", "Transporte", "Salários", "Outros"
                });
                cbCategoriaMovimentacao.setFont(new Font("Segoe UI", Font.PLAIN, 12));
                cbCategoriaMovimentacao.setToolTipText("Categoria da movimentação");
                dadosPanelMovimentacao.add(cbCategoriaMovimentacao, gbcDadosMovimentacao);
                
                // Descrição
                gbcDadosMovimentacao.gridx = 0; gbcDadosMovimentacao.gridy = 2;
                gbcDadosMovimentacao.fill = GridBagConstraints.NONE;
                gbcDadosMovimentacao.weightx = 0.3;
                JLabel lblDescricao = new JLabel("Descrição:");
                lblDescricao.setFont(new Font("Segoe UI", Font.BOLD, 12));
                lblDescricao.setForeground(DARK_GRAY);
                dadosPanelMovimentacao.add(lblDescricao, gbcDadosMovimentacao);
                gbcDadosMovimentacao.gridx = 1; gbcDadosMovimentacao.gridy = 2;
                gbcDadosMovimentacao.fill = GridBagConstraints.HORIZONTAL;
                gbcDadosMovimentacao.weightx = 2.7;
                gbcDadosMovimentacao.gridwidth = 3;
                ElegantTextField txtDescricao = new ElegantTextField(60);
                txtDescricao.setToolTipText("Descrição detalhada da movimentação");
                dadosPanelMovimentacao.add(txtDescricao, gbcDadosMovimentacao);
                
                // Valor
                gbcDadosMovimentacao.gridx = 0; gbcDadosMovimentacao.gridy = 3;
                gbcDadosMovimentacao.fill = GridBagConstraints.NONE;
                gbcDadosMovimentacao.weightx = 0.3;
                gbcDadosMovimentacao.gridwidth = 1;
                JLabel lblValorMovimentacao = new JLabel("Valor:");
                lblValorMovimentacao.setFont(new Font("Segoe UI", Font.BOLD, 12));
                lblValorMovimentacao.setForeground(DARK_GRAY);
                dadosPanelMovimentacao.add(lblValorMovimentacao, gbcDadosMovimentacao);
                gbcDadosMovimentacao.gridx = 1; gbcDadosMovimentacao.gridy = 3;
                gbcDadosMovimentacao.fill = GridBagConstraints.HORIZONTAL;
                gbcDadosMovimentacao.weightx = 0.7;
                gbcDadosMovimentacao.gridwidth = 1;
                ElegantTextField txtValorMovimentacao = new ElegantTextField(15);
                txtValorMovimentacao.setToolTipText("Valor da movimentação");
                dadosPanelMovimentacao.add(txtValorMovimentacao, gbcDadosMovimentacao);
                
                // Forma de Pagamento
                gbcDadosMovimentacao.gridx = 2; gbcDadosMovimentacao.gridy = 3;
                gbcDadosMovimentacao.fill = GridBagConstraints.NONE;
                gbcDadosMovimentacao.weightx = 0.3;
                gbcDadosMovimentacao.gridwidth = 1;
                JLabel lblFormaPagamentoMovimentacao = new JLabel("Forma Pagto:");
                lblFormaPagamentoMovimentacao.setFont(new Font("Segoe UI", Font.BOLD, 12));
                lblFormaPagamentoMovimentacao.setForeground(DARK_GRAY);
                dadosPanelMovimentacao.add(lblFormaPagamentoMovimentacao, gbcDadosMovimentacao);
                gbcDadosMovimentacao.gridx = 3; gbcDadosMovimentacao.gridy = 3;
                gbcDadosMovimentacao.fill = GridBagConstraints.HORIZONTAL;
                gbcDadosMovimentacao.weightx = 0.7;
                gbcDadosMovimentacao.gridwidth = 1;
                JComboBox<String> cbFormaPagamentoMovimentacao = new JComboBox<>(new String[]{
                    "Dinheiro", "Transferência", "TED", "DOC", "Boleto", "Cheque", 
                    "Cartão de Crédito", "Cartão de Débito", "PIX", "Outros"
                });
                cbFormaPagamentoMovimentacao.setFont(new Font("Segoe UI", Font.PLAIN, 12));
                cbFormaPagamentoMovimentacao.setToolTipText("Forma de pagamento");
                dadosPanelMovimentacao.add(cbFormaPagamentoMovimentacao, gbcDadosMovimentacao);
                
                // Conta/Caixa
                gbcDadosMovimentacao.gridx = 0; gbcDadosMovimentacao.gridy = 4;
                gbcDadosMovimentacao.fill = GridBagConstraints.NONE;
                gbcDadosMovimentacao.weightx = 0.3;
                gbcDadosMovimentacao.gridwidth = 1;
                JLabel lblConta = new JLabel("Conta/Caixa:");
                lblConta.setFont(new Font("Segoe UI", Font.BOLD, 12));
                lblConta.setForeground(DARK_GRAY);
                dadosPanelMovimentacao.add(lblConta, gbcDadosMovimentacao);
                gbcDadosMovimentacao.gridx = 1; gbcDadosMovimentacao.gridy = 4;
                gbcDadosMovimentacao.fill = GridBagConstraints.HORIZONTAL;
                gbcDadosMovimentacao.weightx = 0.7;
                gbcDadosMovimentacao.gridwidth = 1;
                ElegantTextField txtConta = new ElegantTextField(20);
                txtConta.setToolTipText("Conta ou caixa de origem/destino");
                dadosPanelMovimentacao.add(txtConta, gbcDadosMovimentacao);
                
                // Documento Referência
                gbcDadosMovimentacao.gridx = 2; gbcDadosMovimentacao.gridy = 4;
                gbcDadosMovimentacao.fill = GridBagConstraints.NONE;
                gbcDadosMovimentacao.weightx = 0.3;
                gbcDadosMovimentacao.gridwidth = 1;
                JLabel lblDocumentoMovimentacao = new JLabel("Doc. Ref:");
                lblDocumentoMovimentacao.setFont(new Font("Segoe UI", Font.BOLD, 12));
                lblDocumentoMovimentacao.setForeground(DARK_GRAY);
                dadosPanelMovimentacao.add(lblDocumentoMovimentacao, gbcDadosMovimentacao);
                gbcDadosMovimentacao.gridx = 3; gbcDadosMovimentacao.gridy = 4;
                gbcDadosMovimentacao.fill = GridBagConstraints.HORIZONTAL;
                gbcDadosMovimentacao.weightx = 0.7;
                gbcDadosMovimentacao.gridwidth = 1;
                ElegantTextField txtDocumentoMovimentacao = new ElegantTextField(20);
                txtDocumentoMovimentacao.setToolTipText("Documento de referência");
                dadosPanelMovimentacao.add(txtDocumentoMovimentacao, gbcDadosMovimentacao);
                
                // Responsável
                gbcDadosMovimentacao.gridx = 0; gbcDadosMovimentacao.gridy = 5;
                gbcDadosMovimentacao.fill = GridBagConstraints.NONE;
                gbcDadosMovimentacao.weightx = 0.3;
                gbcDadosMovimentacao.gridwidth = 1;
                JLabel lblResponsavel = new JLabel("Responsável:");
                lblResponsavel.setFont(new Font("Segoe UI", Font.BOLD, 12));
                lblResponsavel.setForeground(DARK_GRAY);
                dadosPanelMovimentacao.add(lblResponsavel, gbcDadosMovimentacao);
                gbcDadosMovimentacao.gridx = 1; gbcDadosMovimentacao.gridy = 5;
                gbcDadosMovimentacao.fill = GridBagConstraints.HORIZONTAL;
                gbcDadosMovimentacao.weightx = 0.7;
                gbcDadosMovimentacao.gridwidth = 1;
                ElegantTextField txtResponsavel = new ElegantTextField(30);
                txtResponsavel.setToolTipText("Responsável pela movimentação");
                dadosPanelMovimentacao.add(txtResponsavel, gbcDadosMovimentacao);
                
                // Status
                gbcDadosMovimentacao.gridx = 2; gbcDadosMovimentacao.gridy = 5;
                gbcDadosMovimentacao.fill = GridBagConstraints.NONE;
                gbcDadosMovimentacao.weightx = 0.3;
                gbcDadosMovimentacao.gridwidth = 1;
                JLabel lblStatusMovimentacao = new JLabel("Status:");
                lblStatusMovimentacao.setFont(new Font("Segoe UI", Font.BOLD, 12));
                lblStatusMovimentacao.setForeground(DARK_GRAY);
                dadosPanelMovimentacao.add(lblStatusMovimentacao, gbcDadosMovimentacao);
                gbcDadosMovimentacao.gridx = 3; gbcDadosMovimentacao.gridy = 5;
                gbcDadosMovimentacao.fill = GridBagConstraints.HORIZONTAL;
                gbcDadosMovimentacao.weightx = 0.7;
                gbcDadosMovimentacao.gridwidth = 1;
                JComboBox<String> cbStatusMovimentacao = new JComboBox<>(new String[]{
                    "Pendente", "Confirmado", "Cancelado", "Processando"
                });
                cbStatusMovimentacao.setFont(new Font("Segoe UI", Font.PLAIN, 12));
                cbStatusMovimentacao.setToolTipText("Status da movimentação");
                dadosPanelMovimentacao.add(cbStatusMovimentacao, gbcDadosMovimentacao);
                
                panel.add(dadosPanelMovimentacao, gbc);
                
                // Painel de Histórico de Movimentações
                gbc.gridx = 0; gbc.gridy = 1; gbc.gridwidth = 2;
                gbc.fill = GridBagConstraints.BOTH;
                gbc.weightx = 1.0; gbc.weighty = 1.0;
                
                String[] colunasTabelaMovimentacoes = {"ID", "Data", "Tipo", "Categoria", "Descrição", "Valor", "Forma Pagto", "Conta", "Responsável", "Status"};
                Object[][] dadosTabelaMovimentacoes = {};
                
                JTable tabelaMovimentacoes = new JTable(dadosTabelaMovimentacoes, colunasTabelaMovimentacoes);
                tabelaMovimentacoes.setFont(new Font("Segoe UI", Font.PLAIN, 11));
                tabelaMovimentacoes.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 12));
                tabelaMovimentacoes.getTableHeader().setBackground(new Color(230, 126, 34));
                tabelaMovimentacoes.getTableHeader().setForeground(WHITE);
                tabelaMovimentacoes.setRowHeight(25);
                tabelaMovimentacoes.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
                tabelaMovimentacoes.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
                
                JScrollPane scrollPaneTabelaMovimentacoes = new JScrollPane(tabelaMovimentacoes);
                scrollPaneTabelaMovimentacoes.setPreferredSize(new Dimension(1200, 250));
                scrollPaneTabelaMovimentacoes.setBorder(BorderFactory.createTitledBorder(
                    BorderFactory.createEtchedBorder(), "📋 Histórico de Movimentações",
                    TitledBorder.LEFT, TitledBorder.TOP, new Font("Segoe UI", Font.BOLD, 12),
                    DARK_GRAY));
                panel.add(scrollPaneTabelaMovimentacoes, gbc);
                
                // Painel de Filtros
                gbc.gridx = 0; gbc.gridy = 2; gbc.gridwidth = 2;
                gbc.fill = GridBagConstraints.HORIZONTAL;
                gbc.weightx = 1.0; gbc.weighty = 0;
                gbc.insets = new Insets(10, 5, 5, 5);
                
                JPanel filtrosPanel = new JPanel(new GridBagLayout());
                filtrosPanel.setBorder(BorderFactory.createTitledBorder(
                    BorderFactory.createEtchedBorder(), "🔍 Filtros de Pesquisa",
                    TitledBorder.LEFT, TitledBorder.TOP, new Font("Segoe UI", Font.BOLD, 12),
                    DARK_GRAY));
                
                GridBagConstraints gbcFiltros = new GridBagConstraints();
                gbcFiltros.insets = new Insets(5, 5, 5, 5);
                
                // Período
                gbcFiltros.gridx = 0; gbcFiltros.gridy = 0;
                gbcFiltros.fill = GridBagConstraints.NONE;
                gbcFiltros.weightx = 0.2;
                JLabel lblPeriodo = new JLabel("Período:");
                lblPeriodo.setFont(new Font("Segoe UI", Font.BOLD, 12));
                lblPeriodo.setForeground(DARK_GRAY);
                filtrosPanel.add(lblPeriodo, gbcFiltros);
                gbcFiltros.gridx = 1; gbcFiltros.gridy = 0;
                gbcFiltros.fill = GridBagConstraints.HORIZONTAL;
                gbcFiltros.weightx = 0.3;
                JPanel periodoPanel = new JPanel(new FlowLayout());
                periodoPanel.setOpaque(false);
                ElegantTextField txtDataInicio = new ElegantTextField(10);
                ElegantTextField txtDataFim = new ElegantTextField(10);
                periodoPanel.add(txtDataInicio);
                periodoPanel.add(new JLabel(" até "));
                periodoPanel.add(txtDataFim);
                filtrosPanel.add(periodoPanel, gbcFiltros);
                
                // Tipo de Movimentação Filtro
                gbcFiltros.gridx = 2; gbcFiltros.gridy = 0;
                gbcFiltros.fill = GridBagConstraints.NONE;
                gbcFiltros.weightx = 0.2;
                JLabel lblTipoFiltro = new JLabel("Tipo:");
                lblTipoFiltro.setFont(new Font("Segoe UI", Font.BOLD, 12));
                lblTipoFiltro.setForeground(DARK_GRAY);
                filtrosPanel.add(lblTipoFiltro, gbcFiltros);
                gbcFiltros.gridx = 3; gbcFiltros.gridy = 0;
                gbcFiltros.fill = GridBagConstraints.HORIZONTAL;
                gbcFiltros.weightx = 0.3;
                JComboBox<String> cbTipoFiltro = new JComboBox<>(new String[]{
                    "Todos", "Entrada", "Saída", "Transferência", "Sangria", "Suprimento", "Ajuste"
                });
                cbTipoFiltro.setFont(new Font("Segoe UI", Font.PLAIN, 12));
                filtrosPanel.add(cbTipoFiltro, gbcFiltros);
                
                // Busca
                gbcFiltros.gridx = 0; gbcFiltros.gridy = 1;
                gbcFiltros.fill = GridBagConstraints.NONE;
                gbcFiltros.weightx = 0.2;
                JLabel lblBusca = new JLabel("Busca:");
                lblBusca.setFont(new Font("Segoe UI", Font.BOLD, 12));
                lblBusca.setForeground(DARK_GRAY);
                filtrosPanel.add(lblBusca, gbcFiltros);
                gbcFiltros.gridx = 1; gbcFiltros.gridy = 1;
                gbcFiltros.fill = GridBagConstraints.HORIZONTAL;
                gbcFiltros.weightx = 0.8;
                gbcFiltros.gridwidth = 3;
                ElegantTextField txtBusca = new ElegantTextField(40);
                txtBusca.setToolTipText("Buscar por descrição, documento ou responsável");
                filtrosPanel.add(txtBusca, gbcFiltros);
                
                panel.add(filtrosPanel, gbc);
                
                // Painel de Ações
                gbc.gridx = 0; gbc.gridy = 3; gbc.gridwidth = 2;
                gbc.fill = GridBagConstraints.HORIZONTAL;
                gbc.weightx = 1.0; gbc.weighty = 0;
                gbc.insets = new Insets(10, 5, 5, 5);
                
                JPanel acoesPanelMovimentacao = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 5));
                acoesPanelMovimentacao.setOpaque(false);
                
                ElegantButton btnNovaMovimentacao = new ElegantButton("🆕 Nova", SUCCESS_COLOR, false);
                btnNovaMovimentacao.setForeground(WHITE);
                btnNovaMovimentacao.setFont(new Font("Segoe UI", Font.BOLD, 14));
                btnNovaMovimentacao.setPreferredSize(new Dimension(100, 40));
                btnNovaMovimentacao.setToolTipText("Criar nova movimentação");
                acoesPanelMovimentacao.add(btnNovaMovimentacao);
                
                ElegantButton btnConfirmarMovimentacao = new ElegantButton("✅ Confirmar", PRIMARY_COLOR, false);
                btnConfirmarMovimentacao.setForeground(WHITE);
                btnConfirmarMovimentacao.setFont(new Font("Segoe UI", Font.BOLD, 14));
                btnConfirmarMovimentacao.setPreferredSize(new Dimension(100, 40));
                btnConfirmarMovimentacao.setToolTipText("Confirmar movimentação");
                acoesPanelMovimentacao.add(btnConfirmarMovimentacao);
                
                ElegantButton btnEstornarMovimentacao = new ElegantButton("↩️ Estornar", WARNING_COLOR, false);
                btnEstornarMovimentacao.setForeground(WHITE);
                btnEstornarMovimentacao.setFont(new Font("Segoe UI", Font.BOLD, 14));
                btnEstornarMovimentacao.setPreferredSize(new Dimension(100, 40));
                btnEstornarMovimentacao.setToolTipText("Estornar movimentação");
                acoesPanelMovimentacao.add(btnEstornarMovimentacao);
                
                ElegantButton btnImprimirMovimentacao = new ElegantButton("🖨️ Imprimir", new Color(100, 100, 100), false);
                btnImprimirMovimentacao.setForeground(WHITE);
                btnImprimirMovimentacao.setFont(new Font("Segoe UI", Font.BOLD, 14));
                btnImprimirMovimentacao.setPreferredSize(new Dimension(100, 40));
                btnImprimirMovimentacao.setToolTipText("Imprimir relatório");
                acoesPanelMovimentacao.add(btnImprimirMovimentacao);
                
                ElegantButton btnExportarMovimentacao = new ElegantButton("📊 Exportar", new Color(0, 120, 215), false);
                btnExportarMovimentacao.setForeground(WHITE);
                btnExportarMovimentacao.setFont(new Font("Segoe UI", Font.BOLD, 14));
                btnExportarMovimentacao.setPreferredSize(new Dimension(100, 40));
                btnExportarMovimentacao.setToolTipText("Exportar para Excel/PDF");
                acoesPanelMovimentacao.add(btnExportarMovimentacao);
                
                ElegantButton btnCancelarMovimentacao = new ElegantButton("❌ Cancelar", new Color(200, 50, 50), false);
                btnCancelarMovimentacao.setForeground(WHITE);
                btnCancelarMovimentacao.setFont(new Font("Segoe UI", Font.BOLD, 14));
                btnCancelarMovimentacao.setPreferredSize(new Dimension(100, 40));
                btnCancelarMovimentacao.setToolTipText("Cancelar movimentação");
                acoesPanelMovimentacao.add(btnCancelarMovimentacao);
                
                panel.add(acoesPanelMovimentacao, gbc);
                break;
                
            case "📊 Fechamento de Caixa":
                // Painel de Informações do Fechamento
                gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
                gbc.fill = GridBagConstraints.HORIZONTAL;
                gbc.insets = new Insets(5, 5, 15, 5);
                
                JPanel infoPanel = new JPanel(new GridBagLayout());
                infoPanel.setBorder(BorderFactory.createTitledBorder(
                    BorderFactory.createEtchedBorder(), "📊 Informações do Fechamento",
                    TitledBorder.LEFT, TitledBorder.TOP, new Font("Segoe UI", Font.BOLD, 12),
                    DARK_GRAY));
                
                GridBagConstraints gbcInfo = new GridBagConstraints();
                gbcInfo.insets = new Insets(5, 5, 5, 5);
                
                // ID do Fechamento
                gbcInfo.gridx = 0; gbcInfo.gridy = 0;
                gbcInfo.fill = GridBagConstraints.NONE;
                gbcInfo.weightx = 0.3;
                JLabel lblIdFechamento = new JLabel("ID Fechamento:");
                lblIdFechamento.setFont(new Font("Segoe UI", Font.BOLD, 12));
                lblIdFechamento.setForeground(DARK_GRAY);
                infoPanel.add(lblIdFechamento, gbcInfo);
                gbcInfo.gridx = 1; gbcInfo.gridy = 0;
                gbcInfo.fill = GridBagConstraints.HORIZONTAL;
                gbcInfo.weightx = 0.7;
                ElegantTextField txtIdFechamento = new ElegantTextField(15);
                txtIdFechamento.setEditable(false);
                txtIdFechamento.setText("AUTO");
                txtIdFechamento.setToolTipText("ID automático do fechamento");
                infoPanel.add(txtIdFechamento, gbcInfo);
                
                // Data do Fechamento
                gbcInfo.gridx = 2; gbcInfo.gridy = 0;
                gbcInfo.fill = GridBagConstraints.NONE;
                gbcInfo.weightx = 0.3;
                JLabel lblDataFechamento = new JLabel("Data Fechamento:");
                lblDataFechamento.setFont(new Font("Segoe UI", Font.BOLD, 12));
                lblDataFechamento.setForeground(DARK_GRAY);
                infoPanel.add(lblDataFechamento, gbcInfo);
                gbcInfo.gridx = 3; gbcInfo.gridy = 0;
                gbcInfo.fill = GridBagConstraints.HORIZONTAL;
                gbcInfo.weightx = 0.7;
                ElegantTextField txtDataFechamento = new ElegantTextField(10);
                txtDataFechamento.setToolTipText("Data do fechamento");
                infoPanel.add(txtDataFechamento, gbcInfo);
                
                // Operador
                gbcInfo.gridx = 0; gbcInfo.gridy = 1;
                gbcInfo.fill = GridBagConstraints.NONE;
                gbcInfo.weightx = 0.3;
                JLabel lblOperador = new JLabel("Operador:");
                lblOperador.setFont(new Font("Segoe UI", Font.BOLD, 12));
                lblOperador.setForeground(DARK_GRAY);
                infoPanel.add(lblOperador, gbcInfo);
                gbcInfo.gridx = 1; gbcInfo.gridy = 1;
                gbcInfo.fill = GridBagConstraints.HORIZONTAL;
                gbcInfo.weightx = 0.7;
                ElegantTextField txtOperador = new ElegantTextField(25);
                txtOperador.setToolTipText("Nome do operador responsável");
                infoPanel.add(txtOperador, gbcInfo);
                
                // Turno
                gbcInfo.gridx = 2; gbcInfo.gridy = 1;
                gbcInfo.fill = GridBagConstraints.NONE;
                gbcInfo.weightx = 0.3;
                JLabel lblTurno = new JLabel("Turno:");
                lblTurno.setFont(new Font("Segoe UI", Font.BOLD, 12));
                lblTurno.setForeground(DARK_GRAY);
                infoPanel.add(lblTurno, gbcInfo);
                gbcInfo.gridx = 3; gbcInfo.gridy = 1;
                gbcInfo.fill = GridBagConstraints.HORIZONTAL;
                gbcInfo.weightx = 0.7;
                JComboBox<String> cbTurno = new JComboBox<>(new String[]{"Manhã", "Tarde", "Noite", "Integral"});
                cbTurno.setFont(new Font("Segoe UI", Font.PLAIN, 12));
                cbTurno.setToolTipText("Turno de trabalho");
                infoPanel.add(cbTurno, gbcInfo);
                
                panel.add(infoPanel, gbc);
                
                // Painel de Valores
                gbc.gridx = 0; gbc.gridy = 1; gbc.gridwidth = 2;
                gbc.fill = GridBagConstraints.HORIZONTAL;
                gbc.insets = new Insets(5, 5, 15, 5);
                
                JPanel valoresPanel = new JPanel(new GridBagLayout());
                valoresPanel.setBorder(BorderFactory.createTitledBorder(
                    BorderFactory.createEtchedBorder(), "💰 Valores do Caixa",
                    TitledBorder.LEFT, TitledBorder.TOP, new Font("Segoe UI", Font.BOLD, 12),
                    DARK_GRAY));
                
                GridBagConstraints gbcValores = new GridBagConstraints();
                gbcValores.insets = new Insets(5, 5, 5, 5);
                
                // Saldo Inicial
                gbcValores.gridx = 0; gbcValores.gridy = 0;
                gbcValores.fill = GridBagConstraints.NONE;
                gbcValores.weightx = 0.3;
                JLabel lblSaldoInicial = new JLabel("Saldo Inicial:");
                lblSaldoInicial.setFont(new Font("Segoe UI", Font.BOLD, 12));
                lblSaldoInicial.setForeground(DARK_GRAY);
                valoresPanel.add(lblSaldoInicial, gbcValores);
                gbcValores.gridx = 1; gbcValores.gridy = 0;
                gbcValores.fill = GridBagConstraints.HORIZONTAL;
                gbcValores.weightx = 0.7;
                ElegantTextField txtSaldoInicial = new ElegantTextField(15);
                txtSaldoInicial.setToolTipText("Saldo inicial do caixa");
                valoresPanel.add(txtSaldoInicial, gbcValores);
                
                // Total Vendas
                gbcValores.gridx = 2; gbcValores.gridy = 0;
                gbcValores.fill = GridBagConstraints.NONE;
                gbcValores.weightx = 0.3;
                JLabel lblTotalVendas = new JLabel("Total Vendas:");
                lblTotalVendas.setFont(new Font("Segoe UI", Font.BOLD, 12));
                lblTotalVendas.setForeground(DARK_GRAY);
                valoresPanel.add(lblTotalVendas, gbcValores);
                gbcValores.gridx = 3; gbcValores.gridy = 0;
                gbcValores.fill = GridBagConstraints.HORIZONTAL;
                gbcValores.weightx = 0.7;
                ElegantTextField txtTotalVendas = new ElegantTextField(15);
                txtTotalVendas.setToolTipText("Total de vendas do período");
                valoresPanel.add(txtTotalVendas, gbcValores);
                
                // Recebimentos
                gbcValores.gridx = 0; gbcValores.gridy = 1;
                gbcValores.fill = GridBagConstraints.NONE;
                gbcValores.weightx = 0.3;
                JLabel lblRecebimentos = new JLabel("Recebimentos:");
                lblRecebimentos.setFont(new Font("Segoe UI", Font.BOLD, 12));
                lblRecebimentos.setForeground(DARK_GRAY);
                valoresPanel.add(lblRecebimentos, gbcValores);
                gbcValores.gridx = 1; gbcValores.gridy = 1;
                gbcValores.fill = GridBagConstraints.HORIZONTAL;
                gbcValores.weightx = 0.7;
                ElegantTextField txtRecebimentos = new ElegantTextField(15);
                txtRecebimentos.setToolTipText("Total de recebimentos");
                valoresPanel.add(txtRecebimentos, gbcValores);
                
                // Sangrias
                gbcValores.gridx = 2; gbcValores.gridy = 1;
                gbcValores.fill = GridBagConstraints.NONE;
                gbcValores.weightx = 0.3;
                JLabel lblSangrias = new JLabel("Sangrias:");
                lblSangrias.setFont(new Font("Segoe UI", Font.BOLD, 12));
                lblSangrias.setForeground(DARK_GRAY);
                valoresPanel.add(lblSangrias, gbcValores);
                gbcValores.gridx = 3; gbcValores.gridy = 1;
                gbcValores.fill = GridBagConstraints.HORIZONTAL;
                gbcValores.weightx = 0.7;
                ElegantTextField txtSangrias = new ElegantTextField(15);
                txtSangrias.setToolTipText("Total de sangrias");
                valoresPanel.add(txtSangrias, gbcValores);
                
                // Suprimentos
                gbcValores.gridx = 0; gbcValores.gridy = 2;
                gbcValores.fill = GridBagConstraints.NONE;
                gbcValores.weightx = 0.3;
                JLabel lblSuprimentos = new JLabel("Suprimentos:");
                lblSuprimentos.setFont(new Font("Segoe UI", Font.BOLD, 12));
                lblSuprimentos.setForeground(DARK_GRAY);
                valoresPanel.add(lblSuprimentos, gbcValores);
                gbcValores.gridx = 1; gbcValores.gridy = 2;
                gbcValores.fill = GridBagConstraints.HORIZONTAL;
                gbcValores.weightx = 0.7;
                ElegantTextField txtSuprimentos = new ElegantTextField(15);
                txtSuprimentos.setToolTipText("Total de suprimentos");
                valoresPanel.add(txtSuprimentos, gbcValores);
                
                // Despesas
                gbcValores.gridx = 2; gbcValores.gridy = 2;
                gbcValores.fill = GridBagConstraints.NONE;
                gbcValores.weightx = 0.3;
                JLabel lblDespesas = new JLabel("Despesas:");
                lblDespesas.setFont(new Font("Segoe UI", Font.BOLD, 12));
                lblDespesas.setForeground(DARK_GRAY);
                valoresPanel.add(lblDespesas, gbcValores);
                gbcValores.gridx = 3; gbcValores.gridy = 2;
                gbcValores.fill = GridBagConstraints.HORIZONTAL;
                gbcValores.weightx = 0.7;
                ElegantTextField txtDespesas = new ElegantTextField(15);
                txtDespesas.setToolTipText("Total de despesas");
                valoresPanel.add(txtDespesas, gbcValores);
                
                // Saldo Calculado
                gbcValores.gridx = 0; gbcValores.gridy = 3;
                gbcValores.fill = GridBagConstraints.NONE;
                gbcValores.weightx = 0.3;
                JLabel lblSaldoCalculado = new JLabel("Saldo Calculado:");
                lblSaldoCalculado.setFont(new Font("Segoe UI", Font.BOLD, 12));
                lblSaldoCalculado.setForeground(DARK_GRAY);
                valoresPanel.add(lblSaldoCalculado, gbcValores);
                gbcValores.gridx = 1; gbcValores.gridy = 3;
                gbcValores.fill = GridBagConstraints.HORIZONTAL;
                gbcValores.weightx = 0.7;
                ElegantTextField txtSaldoCalculado = new ElegantTextField(15);
                txtSaldoCalculado.setEditable(false);
                txtSaldoCalculado.setText("R$ 0,00");
                txtSaldoCalculado.setToolTipText("Saldo calculado automaticamente");
                txtSaldoCalculado.setFont(new Font("Segoe UI", Font.BOLD, 12));
                txtSaldoCalculado.setForeground(new Color(0, 100, 200));
                valoresPanel.add(txtSaldoCalculado, gbcValores);
                
                // Saldo Físico
                gbcValores.gridx = 2; gbcValores.gridy = 3;
                gbcValores.fill = GridBagConstraints.NONE;
                gbcValores.weightx = 0.3;
                JLabel lblSaldoFisico = new JLabel("Saldo Físico:");
                lblSaldoFisico.setFont(new Font("Segoe UI", Font.BOLD, 12));
                lblSaldoFisico.setForeground(DARK_GRAY);
                valoresPanel.add(lblSaldoFisico, gbcValores);
                gbcValores.gridx = 3; gbcValores.gridy = 3;
                gbcValores.fill = GridBagConstraints.HORIZONTAL;
                gbcValores.weightx = 0.7;
                ElegantTextField txtSaldoFisico = new ElegantTextField(15);
                txtSaldoFisico.setToolTipText("Saldo físico contado");
                valoresPanel.add(txtSaldoFisico, gbcValores);
                
                // Diferença
                gbcValores.gridx = 0; gbcValores.gridy = 4;
                gbcValores.fill = GridBagConstraints.NONE;
                gbcValores.weightx = 0.3;
                JLabel lblDiferenca = new JLabel("Diferença:");
                lblDiferenca.setFont(new Font("Segoe UI", Font.BOLD, 12));
                lblDiferenca.setForeground(DARK_GRAY);
                valoresPanel.add(lblDiferenca, gbcValores);
                gbcValores.gridx = 1; gbcValores.gridy = 4;
                gbcValores.fill = GridBagConstraints.HORIZONTAL;
                gbcValores.weightx = 0.7;
                ElegantTextField txtDiferenca = new ElegantTextField(15);
                txtDiferenca.setEditable(false);
                txtDiferenca.setText("R$ 0,00");
                txtDiferenca.setToolTipText("Diferença entre saldo calculado e físico");
                txtDiferenca.setFont(new Font("Segoe UI", Font.BOLD, 12));
                txtDiferenca.setForeground(new Color(200, 50, 50));
                valoresPanel.add(txtDiferenca, gbcValores);
                
                // Status do Fechamento
                gbcValores.gridx = 2; gbcValores.gridy = 4;
                gbcValores.fill = GridBagConstraints.NONE;
                gbcValores.weightx = 0.3;
                JLabel lblStatusFechamento = new JLabel("Status:");
                lblStatusFechamento.setFont(new Font("Segoe UI", Font.BOLD, 12));
                lblStatusFechamento.setForeground(DARK_GRAY);
                valoresPanel.add(lblStatusFechamento, gbcValores);
                gbcValores.gridx = 3; gbcValores.gridy = 4;
                gbcValores.fill = GridBagConstraints.HORIZONTAL;
                gbcValores.weightx = 0.7;
                JComboBox<String> cbStatusFechamento = new JComboBox<>(new String[]{"Aberto", "Fechado", "Cancelado", "Pendente"});
                cbStatusFechamento.setFont(new Font("Segoe UI", Font.PLAIN, 12));
                cbStatusFechamento.setToolTipText("Status do fechamento");
                valoresPanel.add(cbStatusFechamento, gbcValores);
                
                panel.add(valoresPanel, gbc);
                
                // Painel de Movimentações
                gbc.gridx = 0; gbc.gridy = 2; gbc.gridwidth = 2;
                gbc.fill = GridBagConstraints.BOTH;
                gbc.weightx = 1.0; gbc.weighty = 1.0;
                
                String[] colunasTabelaFechamento = {"ID", "Data/Hora", "Tipo", "Descrição", "Forma Pagto", "Valor", "Operador", "Status"};
                Object[][] dadosTabelaFechamento = {};
                
                JTable tabelaFechamento = new JTable(dadosTabelaFechamento, colunasTabelaFechamento);
                tabelaFechamento.setFont(new Font("Segoe UI", Font.PLAIN, 11));
                tabelaFechamento.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 12));
                tabelaFechamento.getTableHeader().setBackground(new Color(230, 126, 34));
                tabelaFechamento.getTableHeader().setForeground(WHITE);
                tabelaFechamento.setRowHeight(25);
                tabelaFechamento.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
                tabelaFechamento.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
                
                JScrollPane scrollPaneTabelaFechamento = new JScrollPane(tabelaFechamento);
                scrollPaneTabelaFechamento.setPreferredSize(new Dimension(1200, 250));
                scrollPaneTabelaFechamento.setBorder(BorderFactory.createTitledBorder(
                    BorderFactory.createEtchedBorder(), "📋 Movimentações do Caixa",
                    TitledBorder.LEFT, TitledBorder.TOP, new Font("Segoe UI", Font.BOLD, 12),
                    DARK_GRAY));
                panel.add(scrollPaneTabelaFechamento, gbc);
                
                // Painel de Ações
                gbc.gridx = 0; gbc.gridy = 3; gbc.gridwidth = 2;
                gbc.fill = GridBagConstraints.HORIZONTAL;
                gbc.weightx = 1.0; gbc.weighty = 0;
                gbc.insets = new Insets(10, 5, 5, 5);
                
                JPanel acoesPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 5));
                acoesPanel.setOpaque(false);
                
                ElegantButton btnNovoFechamento = new ElegantButton("🆕 Novo", SUCCESS_COLOR, false);
                btnNovoFechamento.setForeground(WHITE);
                btnNovoFechamento.setFont(new Font("Segoe UI", Font.BOLD, 14));
                btnNovoFechamento.setPreferredSize(new Dimension(100, 40));
                btnNovoFechamento.setToolTipText("Iniciar novo fechamento");
                acoesPanel.add(btnNovoFechamento);
                
                ElegantButton btnCalcular = new ElegantButton("🧮 Calcular", PRIMARY_COLOR, false);
                btnCalcular.setForeground(WHITE);
                btnCalcular.setFont(new Font("Segoe UI", Font.BOLD, 14));
                btnCalcular.setPreferredSize(new Dimension(100, 40));
                btnCalcular.setToolTipText("Calcular valores automaticamente");
                acoesPanel.add(btnCalcular);
                
                ElegantButton btnImprimir = new ElegantButton("🖨️ Imprimir", new Color(100, 100, 100), false);
                btnImprimir.setForeground(WHITE);
                btnImprimir.setFont(new Font("Segoe UI", Font.BOLD, 14));
                btnImprimir.setPreferredSize(new Dimension(100, 40));
                btnImprimir.setToolTipText("Imprimir relatório de fechamento");
                acoesPanel.add(btnImprimir);
                
                ElegantButton btnExportar = new ElegantButton("📊 Exportar", new Color(0, 120, 215), false);
                btnExportar.setForeground(WHITE);
                btnExportar.setFont(new Font("Segoe UI", Font.BOLD, 14));
                btnExportar.setPreferredSize(new Dimension(100, 40));
                btnExportar.setToolTipText("Exportar para Excel/PDF");
                acoesPanel.add(btnExportar);
                
                ElegantButton btnConfirmar = new ElegantButton("✅ Confirmar", new Color(0, 150, 0), false);
                btnConfirmar.setForeground(WHITE);
                btnConfirmar.setFont(new Font("Segoe UI", Font.BOLD, 14));
                btnConfirmar.setPreferredSize(new Dimension(100, 40));
                btnConfirmar.setToolTipText("Confirmar fechamento");
                acoesPanel.add(btnConfirmar);
                
                ElegantButton btnCancelar = new ElegantButton("❌ Cancelar", new Color(200, 50, 50), false);
                btnCancelar.setForeground(WHITE);
                btnCancelar.setFont(new Font("Segoe UI", Font.BOLD, 14));
                btnCancelar.setPreferredSize(new Dimension(100, 40));
                btnCancelar.setToolTipText("Cancelar fechamento");
                acoesPanel.add(btnCancelar);
                
                panel.add(acoesPanel, gbc);
                break;
                
            case "💳 Conciliação Bancária":
                // Painel principal de Conciliação Bancária
                gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
                gbc.fill = GridBagConstraints.BOTH;
                gbc.weightx = 1.0; gbc.weighty = 1.0;
                gbc.insets = new Insets(5, 5, 5, 5);
                
                // Painel de Dados da Conciliação
                JPanel dadosConciliacaoPanel = new JPanel(new GridBagLayout());
                dadosConciliacaoPanel.setBorder(BorderFactory.createTitledBorder(
                    BorderFactory.createEtchedBorder(), "💳 Dados da Conciliação Bancária",
                    TitledBorder.LEFT, TitledBorder.TOP, new Font("Segoe UI", Font.BOLD, 12),
                    DARK_GRAY));
                dadosConciliacaoPanel.setOpaque(false);
                
                GridBagConstraints gbcDadosConciliacao = new GridBagConstraints();
                gbcDadosConciliacao.insets = new Insets(5, 5, 5, 5);
                
                // ID da Conciliação
                gbcDadosConciliacao.gridx = 0; gbcDadosConciliacao.gridy = 0;
                gbcDadosConciliacao.fill = GridBagConstraints.NONE;
                gbcDadosConciliacao.weightx = 0.3;
                JLabel lblIdConciliacao = new JLabel("ID Conciliação:");
                lblIdConciliacao.setFont(new Font("Segoe UI", Font.BOLD, 12));
                lblIdConciliacao.setForeground(DARK_GRAY);
                dadosConciliacaoPanel.add(lblIdConciliacao, gbcDadosConciliacao);
                gbcDadosConciliacao.gridx = 1; gbcDadosConciliacao.gridy = 0;
                gbcDadosConciliacao.fill = GridBagConstraints.HORIZONTAL;
                gbcDadosConciliacao.weightx = 0.7;
                ElegantTextField txtIdConciliacao = new ElegantTextField(15);
                txtIdConciliacao.setEditable(false);
                txtIdConciliacao.setText("AUTO");
                txtIdConciliacao.setToolTipText("ID automático da conciliação");
                dadosConciliacaoPanel.add(txtIdConciliacao, gbcDadosConciliacao);
                
                // Banco
                gbcDadosConciliacao.gridx = 2; gbcDadosConciliacao.gridy = 0;
                gbcDadosConciliacao.fill = GridBagConstraints.NONE;
                gbcDadosConciliacao.weightx = 0.3;
                JLabel lblBanco = new JLabel("Banco:");
                lblBanco.setFont(new Font("Segoe UI", Font.BOLD, 12));
                lblBanco.setForeground(DARK_GRAY);
                dadosConciliacaoPanel.add(lblBanco, gbcDadosConciliacao);
                gbcDadosConciliacao.gridx = 3; gbcDadosConciliacao.gridy = 0;
                gbcDadosConciliacao.fill = GridBagConstraints.HORIZONTAL;
                gbcDadosConciliacao.weightx = 0.7;
                JComboBox<String> cbBanco = new JComboBox<>(new String[]{
                    "Banco do Brasil", "Caixa Econômica", "Itaú", "Bradesco", "Santander", "HSBC", "Outros"
                });
                cbBanco.setFont(new Font("Segoe UI", Font.PLAIN, 12));
                cbBanco.setToolTipText("Selecione o banco");
                dadosConciliacaoPanel.add(cbBanco, gbcDadosConciliacao);
                
                // Agência
                gbcDadosConciliacao.gridx = 0; gbcDadosConciliacao.gridy = 1;
                gbcDadosConciliacao.fill = GridBagConstraints.NONE;
                gbcDadosConciliacao.weightx = 0.3;
                JLabel lblAgencia = new JLabel("Agência:");
                lblAgencia.setFont(new Font("Segoe UI", Font.BOLD, 12));
                lblAgencia.setForeground(DARK_GRAY);
                dadosConciliacaoPanel.add(lblAgencia, gbcDadosConciliacao);
                gbcDadosConciliacao.gridx = 1; gbcDadosConciliacao.gridy = 1;
                gbcDadosConciliacao.fill = GridBagConstraints.HORIZONTAL;
                gbcDadosConciliacao.weightx = 0.7;
                ElegantTextField txtAgencia = new ElegantTextField(10);
                txtAgencia.setToolTipText("Número da agência");
                dadosConciliacaoPanel.add(txtAgencia, gbcDadosConciliacao);
                
                // Conta Corrente
                gbcDadosConciliacao.gridx = 2; gbcDadosConciliacao.gridy = 1;
                gbcDadosConciliacao.fill = GridBagConstraints.NONE;
                gbcDadosConciliacao.weightx = 0.3;
                JLabel lblContaCorrente = new JLabel("Conta Corrente:");
                lblContaCorrente.setFont(new Font("Segoe UI", Font.BOLD, 12));
                lblContaCorrente.setForeground(DARK_GRAY);
                dadosConciliacaoPanel.add(lblContaCorrente, gbcDadosConciliacao);
                gbcDadosConciliacao.gridx = 3; gbcDadosConciliacao.gridy = 1;
                gbcDadosConciliacao.fill = GridBagConstraints.HORIZONTAL;
                gbcDadosConciliacao.weightx = 0.7;
                ElegantTextField txtContaCorrente = new ElegantTextField(15);
                txtContaCorrente.setToolTipText("Número da conta corrente");
                dadosConciliacaoPanel.add(txtContaCorrente, gbcDadosConciliacao);
                
                // Período Inicial
                gbcDadosConciliacao.gridx = 0; gbcDadosConciliacao.gridy = 2;
                gbcDadosConciliacao.fill = GridBagConstraints.NONE;
                gbcDadosConciliacao.weightx = 0.3;
                JLabel lblPeriodoInicial = new JLabel("Período Inicial:");
                lblPeriodoInicial.setFont(new Font("Segoe UI", Font.BOLD, 12));
                lblPeriodoInicial.setForeground(DARK_GRAY);
                dadosConciliacaoPanel.add(lblPeriodoInicial, gbcDadosConciliacao);
                gbcDadosConciliacao.gridx = 1; gbcDadosConciliacao.gridy = 2;
                gbcDadosConciliacao.fill = GridBagConstraints.HORIZONTAL;
                gbcDadosConciliacao.weightx = 0.7;
                ElegantTextField txtPeriodoInicial = new ElegantTextField(10);
                txtPeriodoInicial.setToolTipText("Data inicial do período de conciliação");
                dadosConciliacaoPanel.add(txtPeriodoInicial, gbcDadosConciliacao);
                
                // Período Final
                gbcDadosConciliacao.gridx = 2; gbcDadosConciliacao.gridy = 2;
                gbcDadosConciliacao.fill = GridBagConstraints.NONE;
                gbcDadosConciliacao.weightx = 0.3;
                JLabel lblPeriodoFinal = new JLabel("Período Final:");
                lblPeriodoFinal.setFont(new Font("Segoe UI", Font.BOLD, 12));
                lblPeriodoFinal.setForeground(DARK_GRAY);
                dadosConciliacaoPanel.add(lblPeriodoFinal, gbcDadosConciliacao);
                gbcDadosConciliacao.gridx = 3; gbcDadosConciliacao.gridy = 2;
                gbcDadosConciliacao.fill = GridBagConstraints.HORIZONTAL;
                gbcDadosConciliacao.weightx = 0.7;
                ElegantTextField txtPeriodoFinal = new ElegantTextField(10);
                txtPeriodoFinal.setToolTipText("Data final do período de conciliação");
                dadosConciliacaoPanel.add(txtPeriodoFinal, gbcDadosConciliacao);
                
                // Saldo Inicial Extrato
                gbcDadosConciliacao.gridx = 0; gbcDadosConciliacao.gridy = 3;
                gbcDadosConciliacao.fill = GridBagConstraints.NONE;
                gbcDadosConciliacao.weightx = 0.3;
                JLabel lblSaldoInicialExtrato = new JLabel("Saldo Inicial Extrato:");
                lblSaldoInicialExtrato.setFont(new Font("Segoe UI", Font.BOLD, 12));
                lblSaldoInicialExtrato.setForeground(DARK_GRAY);
                dadosConciliacaoPanel.add(lblSaldoInicialExtrato, gbcDadosConciliacao);
                gbcDadosConciliacao.gridx = 1; gbcDadosConciliacao.gridy = 3;
                gbcDadosConciliacao.fill = GridBagConstraints.HORIZONTAL;
                gbcDadosConciliacao.weightx = 0.7;
                ElegantTextField txtSaldoInicialExtrato = new ElegantTextField(15);
                txtSaldoInicialExtrato.setEditable(false);
                txtSaldoInicialExtrato.setText("R$ 0,00");
                txtSaldoInicialExtrato.setToolTipText("Saldo inicial do extrato bancário");
                dadosConciliacaoPanel.add(txtSaldoInicialExtrato, gbcDadosConciliacao);
                
                // Saldo Final Extrato
                gbcDadosConciliacao.gridx = 2; gbcDadosConciliacao.gridy = 3;
                gbcDadosConciliacao.fill = GridBagConstraints.NONE;
                gbcDadosConciliacao.weightx = 0.3;
                JLabel lblSaldoFinalExtrato = new JLabel("Saldo Final Extrato:");
                lblSaldoFinalExtrato.setFont(new Font("Segoe UI", Font.BOLD, 12));
                lblSaldoFinalExtrato.setForeground(DARK_GRAY);
                dadosConciliacaoPanel.add(lblSaldoFinalExtrato, gbcDadosConciliacao);
                gbcDadosConciliacao.gridx = 3; gbcDadosConciliacao.gridy = 3;
                gbcDadosConciliacao.fill = GridBagConstraints.HORIZONTAL;
                gbcDadosConciliacao.weightx = 0.7;
                ElegantTextField txtSaldoFinalExtrato = new ElegantTextField(15);
                txtSaldoFinalExtrato.setEditable(false);
                txtSaldoFinalExtrato.setText("R$ 0,00");
                txtSaldoFinalExtrato.setToolTipText("Saldo final do extrato bancário");
                dadosConciliacaoPanel.add(txtSaldoFinalExtrato, gbcDadosConciliacao);
                
                panel.add(dadosConciliacaoPanel, gbc);
                
                // Painel de Valores de Conciliação
                gbc.gridx = 0; gbc.gridy = 1; gbc.gridwidth = 2;
                gbc.fill = GridBagConstraints.HORIZONTAL;
                gbc.insets = new Insets(5, 5, 15, 5);
                
                JPanel valoresConciliacaoPanel = new JPanel(new GridBagLayout());
                valoresConciliacaoPanel.setBorder(BorderFactory.createTitledBorder(
                    BorderFactory.createEtchedBorder(), "💰 Valores da Conciliação",
                    TitledBorder.LEFT, TitledBorder.TOP, new Font("Segoe UI", Font.BOLD, 12),
                    DARK_GRAY));
                valoresConciliacaoPanel.setOpaque(false);
                
                GridBagConstraints gbcValoresConciliacao = new GridBagConstraints();
                gbcValoresConciliacao.insets = new Insets(5, 5, 5, 5);
                
                // Total de Créditos
                gbcValoresConciliacao.gridx = 0; gbcValoresConciliacao.gridy = 0;
                gbcValoresConciliacao.fill = GridBagConstraints.NONE;
                gbcValoresConciliacao.weightx = 0.3;
                JLabel lblTotalCreditos = new JLabel("Total Créditos:");
                lblTotalCreditos.setFont(new Font("Segoe UI", Font.BOLD, 12));
                lblTotalCreditos.setForeground(DARK_GRAY);
                valoresConciliacaoPanel.add(lblTotalCreditos, gbcValoresConciliacao);
                gbcValoresConciliacao.gridx = 1; gbcValoresConciliacao.gridy = 0;
                gbcValoresConciliacao.fill = GridBagConstraints.HORIZONTAL;
                gbcValoresConciliacao.weightx = 0.7;
                ElegantTextField txtTotalCreditos = new ElegantTextField(15);
                txtTotalCreditos.setEditable(false);
                txtTotalCreditos.setText("R$ 0,00");
                txtTotalCreditos.setToolTipText("Total de créditos do período");
                txtTotalCreditos.setFont(new Font("Segoe UI", Font.BOLD, 12));
                txtTotalCreditos.setForeground(new Color(0, 150, 0));
                valoresConciliacaoPanel.add(txtTotalCreditos, gbcValoresConciliacao);
                
                // Total de Débitos
                gbcValoresConciliacao.gridx = 2; gbcValoresConciliacao.gridy = 0;
                gbcValoresConciliacao.fill = GridBagConstraints.NONE;
                gbcValoresConciliacao.weightx = 0.3;
                JLabel lblTotalDebitos = new JLabel("Total Débitos:");
                lblTotalDebitos.setFont(new Font("Segoe UI", Font.BOLD, 12));
                lblTotalDebitos.setForeground(DARK_GRAY);
                valoresConciliacaoPanel.add(lblTotalDebitos, gbcValoresConciliacao);
                gbcValoresConciliacao.gridx = 3; gbcValoresConciliacao.gridy = 0;
                gbcValoresConciliacao.fill = GridBagConstraints.HORIZONTAL;
                gbcValoresConciliacao.weightx = 0.7;
                ElegantTextField txtTotalDebitos = new ElegantTextField(15);
                txtTotalDebitos.setEditable(false);
                txtTotalDebitos.setText("R$ 0,00");
                txtTotalDebitos.setToolTipText("Total de débitos do período");
                txtTotalDebitos.setFont(new Font("Segoe UI", Font.BOLD, 12));
                txtTotalDebitos.setForeground(new Color(200, 50, 50));
                valoresConciliacaoPanel.add(txtTotalDebitos, gbcValoresConciliacao);
                
                // Saldo Conciliado
                gbcValoresConciliacao.gridx = 0; gbcValoresConciliacao.gridy = 1;
                gbcValoresConciliacao.fill = GridBagConstraints.NONE;
                gbcValoresConciliacao.weightx = 0.3;
                JLabel lblSaldoConciliado = new JLabel("Saldo Conciliado:");
                lblSaldoConciliado.setFont(new Font("Segoe UI", Font.BOLD, 12));
                lblSaldoConciliado.setForeground(DARK_GRAY);
                valoresConciliacaoPanel.add(lblSaldoConciliado, gbcValoresConciliacao);
                gbcValoresConciliacao.gridx = 1; gbcValoresConciliacao.gridy = 1;
                gbcValoresConciliacao.fill = GridBagConstraints.HORIZONTAL;
                gbcValoresConciliacao.weightx = 0.7;
                ElegantTextField txtSaldoConciliado = new ElegantTextField(15);
                txtSaldoConciliado.setEditable(false);
                txtSaldoConciliado.setText("R$ 0,00");
                txtSaldoConciliado.setToolTipText("Saldo conciliado calculado");
                txtSaldoConciliado.setFont(new Font("Segoe UI", Font.BOLD, 12));
                txtSaldoConciliado.setForeground(new Color(0, 100, 200));
                valoresConciliacaoPanel.add(txtSaldoConciliado, gbcValoresConciliacao);
                
                // Diferença de Conciliação
                gbcValoresConciliacao.gridx = 2; gbcValoresConciliacao.gridy = 1;
                gbcValoresConciliacao.fill = GridBagConstraints.NONE;
                gbcValoresConciliacao.weightx = 0.3;
                JLabel lblDiferencaConciliacao = new JLabel("Diferença:");
                lblDiferencaConciliacao.setFont(new Font("Segoe UI", Font.BOLD, 12));
                lblDiferencaConciliacao.setForeground(DARK_GRAY);
                valoresConciliacaoPanel.add(lblDiferencaConciliacao, gbcValoresConciliacao);
                gbcValoresConciliacao.gridx = 3; gbcValoresConciliacao.gridy = 1;
                gbcValoresConciliacao.fill = GridBagConstraints.HORIZONTAL;
                gbcValoresConciliacao.weightx = 0.7;
                ElegantTextField txtDiferencaConciliacao = new ElegantTextField(15);
                txtDiferencaConciliacao.setEditable(false);
                txtDiferencaConciliacao.setText("R$ 0,00");
                txtDiferencaConciliacao.setToolTipText("Diferença entre extrato e sistema");
                txtDiferencaConciliacao.setFont(new Font("Segoe UI", Font.BOLD, 12));
                txtDiferencaConciliacao.setForeground(new Color(200, 50, 50));
                valoresConciliacaoPanel.add(txtDiferencaConciliacao, gbcValoresConciliacao);
                
                // Status da Conciliação
                gbcValoresConciliacao.gridx = 0; gbcValoresConciliacao.gridy = 2;
                gbcValoresConciliacao.fill = GridBagConstraints.NONE;
                gbcValoresConciliacao.weightx = 0.3;
                JLabel lblStatusConciliacao = new JLabel("Status:");
                lblStatusConciliacao.setFont(new Font("Segoe UI", Font.BOLD, 12));
                lblStatusConciliacao.setForeground(DARK_GRAY);
                valoresConciliacaoPanel.add(lblStatusConciliacao, gbcValoresConciliacao);
                gbcValoresConciliacao.gridx = 1; gbcValoresConciliacao.gridy = 2;
                gbcValoresConciliacao.fill = GridBagConstraints.HORIZONTAL;
                gbcValoresConciliacao.weightx = 0.7;
                JComboBox<String> cbStatusConciliacao = new JComboBox<>(new String[]{
                    "Em Andamento", "Conciliado", "Pendente", "Com Diferença", "Cancelado"
                });
                cbStatusConciliacao.setFont(new Font("Segoe UI", Font.PLAIN, 12));
                cbStatusConciliacao.setToolTipText("Status da conciliação");
                valoresConciliacaoPanel.add(cbStatusConciliacao, gbcValoresConciliacao);
                
                // Responsável
                gbcValoresConciliacao.gridx = 2; gbcValoresConciliacao.gridy = 2;
                gbcValoresConciliacao.fill = GridBagConstraints.NONE;
                gbcValoresConciliacao.weightx = 0.3;
                JLabel lblResponsavelConciliacao = new JLabel("Responsável:");
                lblResponsavelConciliacao.setFont(new Font("Segoe UI", Font.BOLD, 12));
                lblResponsavelConciliacao.setForeground(DARK_GRAY);
                valoresConciliacaoPanel.add(lblResponsavelConciliacao, gbcValoresConciliacao);
                gbcValoresConciliacao.gridx = 3; gbcValoresConciliacao.gridy = 2;
                gbcValoresConciliacao.fill = GridBagConstraints.HORIZONTAL;
                gbcValoresConciliacao.weightx = 0.7;
                ElegantTextField txtResponsavelConciliacao = new ElegantTextField(25);
                txtResponsavelConciliacao.setToolTipText("Responsável pela conciliação");
                valoresConciliacaoPanel.add(txtResponsavelConciliacao, gbcValoresConciliacao);
                
                panel.add(valoresConciliacaoPanel, gbc);
                
                // Tabela de Lançamentos da Conciliação
                gbc.gridx = 0; gbc.gridy = 2; gbc.gridwidth = 2;
                gbc.fill = GridBagConstraints.BOTH;
                gbc.weightx = 1.0; gbc.weighty = 1.0;
                
                String[] colunasConciliacao = {
                    "Data", "Tipo", "Descrição", "Documento", "Valor Sistema", "Valor Extrato", 
                    "Diferença", "Status", "Conciliado por", "Data Conciliação"
                };
                Object[][] dadosConciliacao = {
                    {"08/05/2026", "Crédito", "Venda PDV", "NFCe 123", "R$ 1.500,00", "R$ 1.500,00", "R$ 0,00", "Conciliado", "João Silva", "08/05/2026"},
                    {"08/05/2026", "Débito", "Pagamento Fornecedor", "Boleto 456", "R$ 800,00", "R$ 800,00", "R$ 0,00", "Conciliado", "Maria Santos", "08/05/2026"},
                    {"07/05/2026", "Crédito", "Recebimento Cliente", "TED 789", "R$ 2.300,00", "R$ 2.300,00", "R$ 0,00", "Conciliado", "Pedro Oliveira", "07/05/2026"},
                    {"07/05/2026", "Débito", "Aluguel", "Débito Auto", "R$ 1.200,00", "R$ 1.200,00", "R$ 0,00", "Conciliado", "Ana Costa", "07/05/2026"},
                    {"06/05/2026", "Crédito", "Transferência", "DOC 012", "R$ 500,00", "R$ 500,00", "R$ 0,00", "Conciliado", "Carlos Ferreira", "06/05/2026"}
                };
                
                JTable tabelaConciliacao = new JTable(dadosConciliacao, colunasConciliacao);
                tabelaConciliacao.setFont(new Font("Segoe UI", Font.PLAIN, 11));
                tabelaConciliacao.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 12));
                tabelaConciliacao.getTableHeader().setBackground(new Color(230, 126, 34));
                tabelaConciliacao.getTableHeader().setForeground(WHITE);
                tabelaConciliacao.setRowHeight(25);
                tabelaConciliacao.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
                tabelaConciliacao.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
                
                JScrollPane scrollPaneConciliacao = new JScrollPane(tabelaConciliacao);
                scrollPaneConciliacao.setPreferredSize(new Dimension(1200, 300));
                scrollPaneConciliacao.setBorder(BorderFactory.createTitledBorder(
                    BorderFactory.createEtchedBorder(), "📋 Lançamentos da Conciliação Bancária",
                    TitledBorder.LEFT, TitledBorder.TOP, new Font("Segoe UI", Font.BOLD, 12),
                    DARK_GRAY));
                panel.add(scrollPaneConciliacao, gbc);
                
                // Painel de Ações da Conciliação
                gbc.gridx = 0; gbc.gridy = 3; gbc.gridwidth = 2;
                gbc.fill = GridBagConstraints.HORIZONTAL;
                gbc.weightx = 1.0; gbc.weighty = 0;
                gbc.insets = new Insets(10, 5, 5, 5);
                
                JPanel acoesConciliacaoPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 5));
                acoesConciliacaoPanel.setOpaque(false);
                
                ElegantButton btnImportarExtrato = new ElegantButton("📥 Importar Extrato", PRIMARY_COLOR, false);
                btnImportarExtrato.setForeground(WHITE);
                btnImportarExtrato.setFont(new Font("Segoe UI", Font.BOLD, 14));
                btnImportarExtrato.setPreferredSize(new Dimension(140, 40));
                btnImportarExtrato.setToolTipText("Importar extrato bancário");
                acoesConciliacaoPanel.add(btnImportarExtrato);
                
                ElegantButton btnConciliar = new ElegantButton("✅ Conciliar", SUCCESS_COLOR, false);
                btnConciliar.setForeground(WHITE);
                btnConciliar.setFont(new Font("Segoe UI", Font.BOLD, 14));
                btnConciliar.setPreferredSize(new Dimension(120, 40));
                btnConciliar.setToolTipText("Realizar conciliação automática");
                acoesConciliacaoPanel.add(btnConciliar);
                
                ElegantButton btnAjustar = new ElegantButton("🔧 Ajustar", WARNING_COLOR, false);
                btnAjustar.setForeground(WHITE);
                btnAjustar.setFont(new Font("Segoe UI", Font.BOLD, 14));
                btnAjustar.setPreferredSize(new Dimension(100, 40));
                btnAjustar.setToolTipText("Ajustar lançamentos");
                acoesConciliacaoPanel.add(btnAjustar);
                
                ElegantButton btnExportarConciliacao = new ElegantButton("📊 Exportar", new Color(0, 120, 215), false);
                btnExportarConciliacao.setForeground(WHITE);
                btnExportarConciliacao.setFont(new Font("Segoe UI", Font.BOLD, 14));
                btnExportarConciliacao.setPreferredSize(new Dimension(100, 40));
                btnExportarConciliacao.setToolTipText("Exportar conciliação");
                acoesConciliacaoPanel.add(btnExportarConciliacao);
                
                ElegantButton btnImprimirConciliacao = new ElegantButton("🖨️ Imprimir", new Color(100, 100, 100), false);
                btnImprimirConciliacao.setForeground(WHITE);
                btnImprimirConciliacao.setFont(new Font("Segoe UI", Font.BOLD, 14));
                btnImprimirConciliacao.setPreferredSize(new Dimension(100, 40));
                btnImprimirConciliacao.setToolTipText("Imprimir relatório");
                acoesConciliacaoPanel.add(btnImprimirConciliacao);
                
                ElegantButton btnFinalizar = new ElegantButton("🏁 Finalizar", new Color(0, 150, 0), false);
                btnFinalizar.setForeground(WHITE);
                btnFinalizar.setFont(new Font("Segoe UI", Font.BOLD, 14));
                btnFinalizar.setPreferredSize(new Dimension(100, 40));
                btnFinalizar.setToolTipText("Finalizar conciliação");
                acoesConciliacaoPanel.add(btnFinalizar);
                
                panel.add(acoesConciliacaoPanel, gbc);
                break;
                
            case "💵 Cobranças":
                // Painel de Dados da Cobrança
                gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
                gbc.fill = GridBagConstraints.HORIZONTAL;
                gbc.insets = new Insets(5, 5, 15, 5);
                
                JPanel dadosPanelCobranca = new JPanel(new GridBagLayout());
                dadosPanelCobranca.setBorder(BorderFactory.createTitledBorder(
                    BorderFactory.createEtchedBorder(), "💵 Dados da Cobrança",
                    TitledBorder.LEFT, TitledBorder.TOP, new Font("Segoe UI", Font.BOLD, 12),
                    DARK_GRAY));
                
                GridBagConstraints gbcDadosCobranca = new GridBagConstraints();
                gbcDadosCobranca.insets = new Insets(5, 5, 5, 5);
                
                // ID da Cobrança
                gbcDadosCobranca.gridx = 0; gbcDadosCobranca.gridy = 0;
                gbcDadosCobranca.fill = GridBagConstraints.NONE;
                gbcDadosCobranca.weightx = 0.3;
                JLabel lblIdCobranca = new JLabel("ID Cobrança:");
                lblIdCobranca.setFont(new Font("Segoe UI", Font.BOLD, 12));
                lblIdCobranca.setForeground(DARK_GRAY);
                dadosPanelCobranca.add(lblIdCobranca, gbcDadosCobranca);
                gbcDadosCobranca.gridx = 1; gbcDadosCobranca.gridy = 0;
                gbcDadosCobranca.fill = GridBagConstraints.HORIZONTAL;
                gbcDadosCobranca.weightx = 0.7;
                ElegantTextField txtIdCobranca = new ElegantTextField(15);
                txtIdCobranca.setEditable(false);
                txtIdCobranca.setText("AUTO");
                txtIdCobranca.setToolTipText("ID automático da cobrança");
                dadosPanelCobranca.add(txtIdCobranca, gbcDadosCobranca);
                
                // Cliente
                gbcDadosCobranca.gridx = 2; gbcDadosCobranca.gridy = 0;
                gbcDadosCobranca.fill = GridBagConstraints.NONE;
                gbcDadosCobranca.weightx = 0.3;
                JLabel lblClienteCobranca = new JLabel("Cliente:");
                lblClienteCobranca.setFont(new Font("Segoe UI", Font.BOLD, 12));
                lblClienteCobranca.setForeground(DARK_GRAY);
                dadosPanelCobranca.add(lblClienteCobranca, gbcDadosCobranca);
                gbcDadosCobranca.gridx = 3; gbcDadosCobranca.gridy = 0;
                gbcDadosCobranca.fill = GridBagConstraints.HORIZONTAL;
                gbcDadosCobranca.weightx = 0.7;
                ElegantTextField txtClienteCobranca = new ElegantTextField(30);
                txtClienteCobranca.setToolTipText("Nome do cliente");
                dadosPanelCobranca.add(txtClienteCobranca, gbcDadosCobranca);
                
                // Documento
                gbcDadosCobranca.gridx = 0; gbcDadosCobranca.gridy = 1;
                gbcDadosCobranca.fill = GridBagConstraints.NONE;
                gbcDadosCobranca.weightx = 0.3;
                JLabel lblDocumento = new JLabel("Documento:");
                lblDocumento.setFont(new Font("Segoe UI", Font.BOLD, 12));
                lblDocumento.setForeground(DARK_GRAY);
                dadosPanelCobranca.add(lblDocumento, gbcDadosCobranca);
                gbcDadosCobranca.gridx = 1; gbcDadosCobranca.gridy = 1;
                gbcDadosCobranca.fill = GridBagConstraints.HORIZONTAL;
                gbcDadosCobranca.weightx = 0.7;
                ElegantTextField txtDocumento = new ElegantTextField(20);
                txtDocumento.setToolTipText("Número do documento");
                dadosPanelCobranca.add(txtDocumento, gbcDadosCobranca);
                
                // Tipo de Documento
                gbcDadosCobranca.gridx = 2; gbcDadosCobranca.gridy = 1;
                gbcDadosCobranca.fill = GridBagConstraints.NONE;
                gbcDadosCobranca.weightx = 0.3;
                JLabel lblTipoDocCobranca = new JLabel("Tipo:");
                lblTipoDocCobranca.setFont(new Font("Segoe UI", Font.BOLD, 12));
                lblTipoDocCobranca.setForeground(DARK_GRAY);
                dadosPanelCobranca.add(lblTipoDocCobranca, gbcDadosCobranca);
                gbcDadosCobranca.gridx = 3; gbcDadosCobranca.gridy = 1;
                gbcDadosCobranca.fill = GridBagConstraints.HORIZONTAL;
                gbcDadosCobranca.weightx = 0.7;
                JComboBox<String> cbTipoDocCobranca = new JComboBox<>(new String[]{
                    "Boleto", "Duplicata", "Cheque", "Promissória", "Fatura", "Cartão", "PIX"
                });
                cbTipoDocCobranca.setFont(new Font("Segoe UI", Font.PLAIN, 12));
                cbTipoDocCobranca.setToolTipText("Tipo de documento");
                dadosPanelCobranca.add(cbTipoDocCobranca, gbcDadosCobranca);
                
                // Data de Emissão
                gbcDadosCobranca.gridx = 0; gbcDadosCobranca.gridy = 2;
                gbcDadosCobranca.fill = GridBagConstraints.NONE;
                gbcDadosCobranca.weightx = 0.3;
                JLabel lblDataEmissao = new JLabel("Emissão:");
                lblDataEmissao.setFont(new Font("Segoe UI", Font.BOLD, 12));
                lblDataEmissao.setForeground(DARK_GRAY);
                dadosPanelCobranca.add(lblDataEmissao, gbcDadosCobranca);
                gbcDadosCobranca.gridx = 1; gbcDadosCobranca.gridy = 2;
                gbcDadosCobranca.fill = GridBagConstraints.HORIZONTAL;
                gbcDadosCobranca.weightx = 0.7;
                ElegantTextField txtDataEmissao = new ElegantTextField(12);
                txtDataEmissao.setEditable(false);
                txtDataEmissao.setToolTipText("Data de emissão");
                dadosPanelCobranca.add(txtDataEmissao, gbcDadosCobranca);
                
                // Data de Vencimento
                gbcDadosCobranca.gridx = 2; gbcDadosCobranca.gridy = 2;
                gbcDadosCobranca.fill = GridBagConstraints.NONE;
                gbcDadosCobranca.weightx = 0.3;
                JLabel lblDataVencimento = new JLabel("Vencimento:");
                lblDataVencimento.setFont(new Font("Segoe UI", Font.BOLD, 12));
                lblDataVencimento.setForeground(DARK_GRAY);
                dadosPanelCobranca.add(lblDataVencimento, gbcDadosCobranca);
                gbcDadosCobranca.gridx = 3; gbcDadosCobranca.gridy = 2;
                gbcDadosCobranca.fill = GridBagConstraints.HORIZONTAL;
                gbcDadosCobranca.weightx = 0.7;
                ElegantTextField txtDataVencimento = new ElegantTextField(12);
                txtDataVencimento.setToolTipText("Data de vencimento");
                dadosPanelCobranca.add(txtDataVencimento, gbcDadosCobranca);
                
                panel.add(dadosPanelCobranca, gbc);
                
                // Painel de Valores
                gbc.gridx = 0; gbc.gridy = 1; gbc.gridwidth = 2;
                gbc.fill = GridBagConstraints.HORIZONTAL;
                gbc.insets = new Insets(5, 5, 15, 5);
                
                JPanel valoresPanelCobranca = new JPanel(new GridBagLayout());
                valoresPanelCobranca.setBorder(BorderFactory.createTitledBorder(
                    BorderFactory.createEtchedBorder(), "💰 Valores da Cobrança",
                    TitledBorder.LEFT, TitledBorder.TOP, new Font("Segoe UI", Font.BOLD, 12),
                    DARK_GRAY));
                
                GridBagConstraints gbcValoresCobranca = new GridBagConstraints();
                gbcValoresCobranca.insets = new Insets(5, 5, 5, 5);
                
                // Valor Original
                gbcValoresCobranca.gridx = 0; gbcValoresCobranca.gridy = 0;
                gbcValoresCobranca.fill = GridBagConstraints.NONE;
                gbcValoresCobranca.weightx = 0.3;
                JLabel lblValorOriginal = new JLabel("Valor Original:");
                lblValorOriginal.setFont(new Font("Segoe UI", Font.BOLD, 12));
                lblValorOriginal.setForeground(DARK_GRAY);
                valoresPanelCobranca.add(lblValorOriginal, gbcValoresCobranca);
                gbcValoresCobranca.gridx = 1; gbcValoresCobranca.gridy = 0;
                gbcValoresCobranca.fill = GridBagConstraints.HORIZONTAL;
                gbcValoresCobranca.weightx = 0.7;
                ElegantTextField txtValorOriginal = new ElegantTextField(15);
                txtValorOriginal.setToolTipText("Valor original da cobrança");
                valoresPanelCobranca.add(txtValorOriginal, gbcValoresCobranca);
                
                // Juros/Multa
                gbcValoresCobranca.gridx = 2; gbcValoresCobranca.gridy = 0;
                gbcValoresCobranca.fill = GridBagConstraints.NONE;
                gbcValoresCobranca.weightx = 0.3;
                JLabel lblJurosMulta = new JLabel("Juros/Multa:");
                lblJurosMulta.setFont(new Font("Segoe UI", Font.BOLD, 12));
                lblJurosMulta.setForeground(DARK_GRAY);
                valoresPanelCobranca.add(lblJurosMulta, gbcValoresCobranca);
                gbcValoresCobranca.gridx = 3; gbcValoresCobranca.gridy = 0;
                gbcValoresCobranca.fill = GridBagConstraints.HORIZONTAL;
                gbcValoresCobranca.weightx = 0.7;
                ElegantTextField txtJurosMulta = new ElegantTextField(15);
                txtJurosMulta.setText("0,00");
                txtJurosMulta.setToolTipText("Valor de juros e multa");
                valoresPanelCobranca.add(txtJurosMulta, gbcValoresCobranca);
                
                // Valor Corrigido
                gbcValoresCobranca.gridx = 0; gbcValoresCobranca.gridy = 1;
                gbcValoresCobranca.fill = GridBagConstraints.NONE;
                gbcValoresCobranca.weightx = 0.3;
                JLabel lblValorCorrigido = new JLabel("Valor Corrigido:");
                lblValorCorrigido.setFont(new Font("Segoe UI", Font.BOLD, 12));
                lblValorCorrigido.setForeground(DARK_GRAY);
                valoresPanelCobranca.add(lblValorCorrigido, gbcValoresCobranca);
                gbcValoresCobranca.gridx = 1; gbcValoresCobranca.gridy = 1;
                gbcValoresCobranca.fill = GridBagConstraints.HORIZONTAL;
                gbcValoresCobranca.weightx = 0.7;
                ElegantTextField txtValorCorrigido = new ElegantTextField(15);
                txtValorCorrigido.setEditable(false);
                txtValorCorrigido.setText("R$ 0,00");
                txtValorCorrigido.setToolTipText("Valor corrigido com juros e multa");
                valoresPanelCobranca.add(txtValorCorrigido, gbcValoresCobranca);
                
                // Valor Pago
                gbcValoresCobranca.gridx = 2; gbcValoresCobranca.gridy = 1;
                gbcValoresCobranca.fill = GridBagConstraints.NONE;
                gbcValoresCobranca.weightx = 0.3;
                JLabel lblValorPago = new JLabel("Valor Pago:");
                lblValorPago.setFont(new Font("Segoe UI", Font.BOLD, 12));
                lblValorPago.setForeground(DARK_GRAY);
                valoresPanelCobranca.add(lblValorPago, gbcValoresCobranca);
                gbcValoresCobranca.gridx = 3; gbcValoresCobranca.gridy = 1;
                gbcValoresCobranca.fill = GridBagConstraints.HORIZONTAL;
                gbcValoresCobranca.weightx = 0.7;
                ElegantTextField txtValorPago = new ElegantTextField(15);
                txtValorPago.setText("0,00");
                txtValorPago.setToolTipText("Valor já pago");
                valoresPanelCobranca.add(txtValorPago, gbcValoresCobranca);
                
                // Saldo Devedor
                gbcValoresCobranca.gridx = 1; gbcValoresCobranca.gridy = 2;
                gbcValoresCobranca.fill = GridBagConstraints.NONE;
                gbcValoresCobranca.weightx = 0.3;
                JLabel lblSaldoDevedor = new JLabel("Saldo Devedor:");
                lblSaldoDevedor.setFont(new Font("Segoe UI", Font.BOLD, 14));
                lblSaldoDevedor.setForeground(new Color(200, 50, 50));
                valoresPanelCobranca.add(lblSaldoDevedor, gbcValoresCobranca);
                gbcValoresCobranca.gridx = 2; gbcValoresCobranca.gridy = 2;
                gbcValoresCobranca.fill = GridBagConstraints.HORIZONTAL;
                gbcValoresCobranca.weightx = 0.7;
                ElegantTextField txtSaldoDevedor = new ElegantTextField(15);
                txtSaldoDevedor.setEditable(false);
                txtSaldoDevedor.setText("R$ 0,00");
                txtSaldoDevedor.setFont(new Font("Segoe UI", Font.BOLD, 14));
                txtSaldoDevedor.setForeground(new Color(200, 50, 50));
                txtSaldoDevedor.setToolTipText("Valor restante a pagar");
                valoresPanelCobranca.add(txtSaldoDevedor, gbcValoresCobranca);
                
                panel.add(valoresPanelCobranca, gbc);
                
                // Painel de Status
                gbc.gridx = 0; gbc.gridy = 2; gbc.gridwidth = 2;
                gbc.fill = GridBagConstraints.HORIZONTAL;
                gbc.insets = new Insets(5, 5, 15, 5);
                
                JPanel statusPanelCobranca = new JPanel(new GridBagLayout());
                statusPanelCobranca.setBorder(BorderFactory.createTitledBorder(
                    BorderFactory.createEtchedBorder(), "📊 Status da Cobrança",
                    TitledBorder.LEFT, TitledBorder.TOP, new Font("Segoe UI", Font.BOLD, 12),
                    DARK_GRAY));
                
                GridBagConstraints gbcStatusCobranca = new GridBagConstraints();
                gbcStatusCobranca.insets = new Insets(5, 5, 5, 5);
                
                // Status da Cobrança
                gbcStatusCobranca.gridx = 0; gbcStatusCobranca.gridy = 0;
                gbcStatusCobranca.fill = GridBagConstraints.NONE;
                gbcStatusCobranca.weightx = 0.3;
                JLabel lblStatusCobranca = new JLabel("Status:");
                lblStatusCobranca.setFont(new Font("Segoe UI", Font.BOLD, 12));
                lblStatusCobranca.setForeground(DARK_GRAY);
                statusPanelCobranca.add(lblStatusCobranca, gbcStatusCobranca);
                gbcStatusCobranca.gridx = 1; gbcStatusCobranca.gridy = 0;
                gbcStatusCobranca.fill = GridBagConstraints.HORIZONTAL;
                gbcStatusCobranca.weightx = 0.7;
                JComboBox<String> cbStatusCobranca = new JComboBox<>(new String[]{
                    "Pendente", "Enviada", "Atrasada", "Negociada", "Paga", "Cancelada", "Protestada"
                });
                cbStatusCobranca.setFont(new Font("Segoe UI", Font.PLAIN, 12));
                cbStatusCobranca.setToolTipText("Status da cobrança");
                statusPanelCobranca.add(cbStatusCobranca, gbcStatusCobranca);
                
                // Forma de Pagamento
                gbcStatusCobranca.gridx = 2; gbcStatusCobranca.gridy = 0;
                gbcStatusCobranca.fill = GridBagConstraints.NONE;
                gbcStatusCobranca.weightx = 0.3;
                JLabel lblFormaPagamento = new JLabel("Forma Pagto:");
                lblFormaPagamento.setFont(new Font("Segoe UI", Font.BOLD, 12));
                lblFormaPagamento.setForeground(DARK_GRAY);
                statusPanelCobranca.add(lblFormaPagamento, gbcStatusCobranca);
                gbcStatusCobranca.gridx = 3; gbcStatusCobranca.gridy = 0;
                gbcStatusCobranca.fill = GridBagConstraints.HORIZONTAL;
                gbcStatusCobranca.weightx = 0.7;
                JComboBox<String> cbFormaPagamento = new JComboBox<>(new String[]{
                    "Dinheiro", "Cartão Débito", "Cartão Crédito", "PIX", "Boleto", "Cheque", "Transferência"
                });
                cbFormaPagamento.setFont(new Font("Segoe UI", Font.PLAIN, 12));
                cbFormaPagamento.setToolTipText("Forma de pagamento");
                statusPanelCobranca.add(cbFormaPagamento, gbcStatusCobranca);
                
                // Data de Pagamento
                gbcStatusCobranca.gridx = 0; gbcStatusCobranca.gridy = 1;
                gbcStatusCobranca.fill = GridBagConstraints.NONE;
                gbcStatusCobranca.weightx = 0.3;
                JLabel lblDataPagamento = new JLabel("Data Pagto:");
                lblDataPagamento.setFont(new Font("Segoe UI", Font.BOLD, 12));
                lblDataPagamento.setForeground(DARK_GRAY);
                statusPanelCobranca.add(lblDataPagamento, gbcStatusCobranca);
                gbcStatusCobranca.gridx = 1; gbcStatusCobranca.gridy = 1;
                gbcStatusCobranca.fill = GridBagConstraints.HORIZONTAL;
                gbcStatusCobranca.weightx = 0.7;
                ElegantTextField txtDataPagamento = new ElegantTextField(12);
                txtDataPagamento.setToolTipText("Data do pagamento");
                statusPanelCobranca.add(txtDataPagamento, gbcStatusCobranca);
                
                // Observações
                gbcStatusCobranca.gridx = 2; gbcStatusCobranca.gridy = 1;
                gbcStatusCobranca.fill = GridBagConstraints.NONE;
                gbcStatusCobranca.weightx = 0.3;
                JLabel lblObservacoes = new JLabel("Observações:");
                lblObservacoes.setFont(new Font("Segoe UI", Font.BOLD, 12));
                lblObservacoes.setForeground(DARK_GRAY);
                statusPanelCobranca.add(lblObservacoes, gbcStatusCobranca);
                gbcStatusCobranca.gridx = 3; gbcStatusCobranca.gridy = 1;
                gbcStatusCobranca.fill = GridBagConstraints.HORIZONTAL;
                gbcStatusCobranca.weightx = 0.7;
                ElegantTextField txtObservacoes = new ElegantTextField(25);
                txtObservacoes.setToolTipText("Observações sobre a cobrança");
                statusPanelCobranca.add(txtObservacoes, gbcStatusCobranca);
                
                panel.add(statusPanelCobranca, gbc);
                
                // Tabela de Cobranças
                gbc.gridx = 0; gbc.gridy = 3; gbc.gridwidth = 2;
                gbc.fill = GridBagConstraints.BOTH;
                gbc.weightx = 1.0; gbc.weighty = 1.0;
                
                String[] colunasTabelaCobranca = {"ID", "Cliente", "Documento", "Tipo", "Emissão", "Vencimento", "Valor Original", "Valor Pago", "Saldo", "Status", "Ações"};
                Object[][] dadosTabelaCobranca = {};
                
                JTable tabelaCobranca = new JTable(dadosTabelaCobranca, colunasTabelaCobranca);
                tabelaCobranca.setFont(new Font("Segoe UI", Font.PLAIN, 11));
                tabelaCobranca.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 12));
                tabelaCobranca.getTableHeader().setBackground(new Color(230, 126, 34));
                tabelaCobranca.getTableHeader().setForeground(WHITE);
                tabelaCobranca.setRowHeight(25);
                tabelaCobranca.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
                tabelaCobranca.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
                
                JScrollPane scrollPaneTabelaCobranca = new JScrollPane(tabelaCobranca);
                scrollPaneTabelaCobranca.setPreferredSize(new Dimension(1000, 300));
                scrollPaneTabelaCobranca.setBorder(BorderFactory.createTitledBorder(
                    BorderFactory.createEtchedBorder(), "💵 Histórico de Cobranças",
                    TitledBorder.LEFT, TitledBorder.TOP, new Font("Segoe UI", Font.BOLD, 12),
                    DARK_GRAY));
                panel.add(scrollPaneTabelaCobranca, gbc);
                
                // Painel de Ações
                gbc.gridx = 0; gbc.gridy = 4; gbc.gridwidth = 2;
                gbc.fill = GridBagConstraints.HORIZONTAL;
                gbc.weightx = 1.0; gbc.weighty = 0;
                gbc.insets = new Insets(10, 5, 5, 5);
                
                JPanel acoesPanelCobranca = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 5));
                acoesPanelCobranca.setOpaque(false);
                
                ElegantButton btnNovaCobranca = new ElegantButton("💵 Nova Cobrança", SUCCESS_COLOR, false);
                btnNovaCobranca.setForeground(WHITE);
                btnNovaCobranca.setFont(new Font("Segoe UI", Font.BOLD, 14));
                btnNovaCobranca.setPreferredSize(new Dimension(150, 40));
                acoesPanelCobranca.add(btnNovaCobranca);
                
                ElegantButton btnEnviarCobranca = new ElegantButton("📧 Enviar", WARNING_COLOR, false);
                btnEnviarCobranca.setForeground(WHITE);
                btnEnviarCobranca.setFont(new Font("Segoe UI", Font.BOLD, 14));
                btnEnviarCobranca.setPreferredSize(new Dimension(120, 40));
                acoesPanelCobranca.add(btnEnviarCobranca);
                
                ElegantButton btnNegociar = new ElegantButton("🤝 Negociar", new Color(70, 130, 180), false);
                btnNegociar.setForeground(WHITE);
                btnNegociar.setFont(new Font("Segoe UI", Font.BOLD, 14));
                btnNegociar.setPreferredSize(new Dimension(120, 40));
                acoesPanelCobranca.add(btnNegociar);
                
                ElegantButton btnReceber = new ElegantButton("💰 Receber", new Color(0, 150, 0), false);
                btnReceber.setForeground(WHITE);
                btnReceber.setFont(new Font("Segoe UI", Font.BOLD, 14));
                btnReceber.setPreferredSize(new Dimension(120, 40));
                acoesPanelCobranca.add(btnReceber);
                
                ElegantButton btnCancelarCobranca = new ElegantButton("❌ Cancelar", new Color(200, 50, 50), false);
                btnCancelarCobranca.setForeground(WHITE);
                btnCancelarCobranca.setFont(new Font("Segoe UI", Font.BOLD, 14));
                btnCancelarCobranca.setPreferredSize(new Dimension(120, 40));
                acoesPanelCobranca.add(btnCancelarCobranca);
                
                ElegantButton btnImprimirBoleto = new ElegantButton("🖨️ Boleto", new Color(100, 100, 100), false);
                btnImprimirBoleto.setForeground(WHITE);
                btnImprimirBoleto.setFont(new Font("Segoe UI", Font.BOLD, 14));
                btnImprimirBoleto.setPreferredSize(new Dimension(120, 40));
                acoesPanelCobranca.add(btnImprimirBoleto);
                
                panel.add(acoesPanelCobranca, gbc);
                break;
                
            default:
                return criarFormularioPadraoElegante(item, "FINANCEIRO", gbc);
        }
        
        return panel;
    }
    
    /**
     * Formulários Relatórios elegantes
     */
    private JPanel criarFormularioRelatoriosElegante(String item, GridBagConstraints gbc) {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setOpaque(false);
        
        switch (item) {
            case "📊 Relatório de Vendas":
                // Período
                gbc.gridx = 0; gbc.gridy = 0;
                JLabel lblPeriodo = new JLabel("Período:");
                lblPeriodo.setFont(new Font("Segoe UI", Font.BOLD, 12));
                lblPeriodo.setForeground(DARK_GRAY);
                panel.add(lblPeriodo, gbc);
                gbc.gridx = 1; gbc.gridy = 0;
                JPanel periodoPanel = new JPanel(new FlowLayout());
                periodoPanel.setOpaque(false);
                ElegantTextField txtDataInicio = new ElegantTextField(10);
                ElegantTextField txtDataFim = new ElegantTextField(10);
                periodoPanel.add(txtDataInicio);
                periodoPanel.add(new JLabel(" até "));
                periodoPanel.add(txtDataFim);
                panel.add(periodoPanel, gbc);
                
                // Tipo de Relatório
                gbc.gridx = 0; gbc.gridy = 1;
                JLabel lblTipo = new JLabel("Tipo:");
                lblTipo.setFont(new Font("Segoe UI", Font.BOLD, 12));
                lblTipo.setForeground(DARK_GRAY);
                panel.add(lblTipo, gbc);
                gbc.gridx = 1; gbc.gridy = 1;
                JComboBox<String> cbTipo = new JComboBox<>(new String[]{"Resumo", "Detalhado", "Por Cliente", "Por Produto"});
                cbTipo.setFont(new Font("Segoe UI", Font.PLAIN, 12));
                panel.add(cbTipo, gbc);
                
                // Formato
                gbc.gridx = 0; gbc.gridy = 2;
                JLabel lblFormato = new JLabel("Formato:");
                lblFormato.setFont(new Font("Segoe UI", Font.BOLD, 12));
                lblFormato.setForeground(DARK_GRAY);
                panel.add(lblFormato, gbc);
                gbc.gridx = 1; gbc.gridy = 2;
                JComboBox<String> cbFormato = new JComboBox<>(new String[]{"PDF", "Excel", "Tela"});
                cbFormato.setFont(new Font("Segoe UI", Font.PLAIN, 12));
                panel.add(cbFormato, gbc);
                break;
                
            case "🔧 Gestão de Relatórios":
                // Painel principal com abas
                gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
                gbc.fill = GridBagConstraints.BOTH;
                gbc.weightx = 1.0; gbc.weighty = 1.0;
                gbc.insets = new Insets(5, 5, 5, 5);
                
                // Criar painel com abas
                JTabbedPane tabbedPaneRelatorios = new JTabbedPane();
                tabbedPaneRelatorios.setFont(new Font("Segoe UI", Font.PLAIN, 12));
                tabbedPaneRelatorios.setBackground(WHITE);
                
                // Aba 1: Relatório de Vendas
                JPanel panelVendas = new JPanel(new GridBagLayout());
                panelVendas.setOpaque(false);
                
                GridBagConstraints gbcVendas = new GridBagConstraints();
                gbcVendas.insets = new Insets(5, 5, 5, 5);
                
                // Período de Vendas
                gbcVendas.gridx = 0; gbcVendas.gridy = 0;
                gbcVendas.fill = GridBagConstraints.NONE;
                gbcVendas.weightx = 0.3;
                JLabel lblPeriodoVendas = new JLabel("Período:");
                lblPeriodoVendas.setFont(new Font("Segoe UI", Font.BOLD, 12));
                lblPeriodoVendas.setForeground(DARK_GRAY);
                panelVendas.add(lblPeriodoVendas, gbcVendas);
                gbcVendas.gridx = 1; gbcVendas.gridy = 0;
                gbcVendas.fill = GridBagConstraints.HORIZONTAL;
                gbcVendas.weightx = 0.7;
                JPanel periodoPanelVendas = new JPanel(new FlowLayout());
                periodoPanelVendas.setOpaque(false);
                ElegantTextField txtDataInicioVendas = new ElegantTextField(10);
                ElegantTextField txtDataFimVendas = new ElegantTextField(10);
                periodoPanelVendas.add(txtDataInicioVendas);
                periodoPanelVendas.add(new JLabel(" até "));
                periodoPanelVendas.add(txtDataFimVendas);
                panelVendas.add(periodoPanelVendas, gbcVendas);
                
                // Tipo de Relatório de Vendas
                gbcVendas.gridx = 0; gbcVendas.gridy = 1;
                gbcVendas.fill = GridBagConstraints.NONE;
                gbcVendas.weightx = 0.3;
                JLabel lblTipoVendas = new JLabel("Tipo:");
                lblTipoVendas.setFont(new Font("Segoe UI", Font.BOLD, 12));
                lblTipoVendas.setForeground(DARK_GRAY);
                panelVendas.add(lblTipoVendas, gbcVendas);
                gbcVendas.gridx = 1; gbcVendas.gridy = 1;
                gbcVendas.fill = GridBagConstraints.HORIZONTAL;
                gbcVendas.weightx = 0.7;
                JComboBox<String> cbTipoVendas = new JComboBox<>(new String[]{"Resumo", "Detalhado", "Por Cliente", "Por Produto"});
                cbTipoVendas.setFont(new Font("Segoe UI", Font.PLAIN, 12));
                panelVendas.add(cbTipoVendas, gbcVendas);
                
                // Tabela de Vendas
                gbcVendas.gridx = 0; gbcVendas.gridy = 2; gbcVendas.gridwidth = 2;
                gbcVendas.fill = GridBagConstraints.BOTH;
                gbcVendas.weightx = 1.0; gbcVendas.weighty = 1.0;
                gbcVendas.insets = new Insets(10, 5, 5, 5);
                
                String[] colunasVendas = {"ID", "Data", "Cliente", "Produto", "Quantidade", "Valor", "Vendedor"};
                Object[][] dadosVendas = {};
                
                JTable tabelaVendas = new JTable(dadosVendas, colunasVendas);
                tabelaVendas.setFont(new Font("Segoe UI", Font.PLAIN, 11));
                tabelaVendas.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 12));
                tabelaVendas.getTableHeader().setBackground(new Color(52, 152, 219));
                tabelaVendas.getTableHeader().setForeground(WHITE);
                tabelaVendas.setRowHeight(25);
                tabelaVendas.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
                
                JScrollPane scrollVendas = new JScrollPane(tabelaVendas);
                scrollVendas.setPreferredSize(new Dimension(800, 200));
                panelVendas.add(scrollVendas, gbcVendas);
                
                // Botões de Ação Vendas
                gbcVendas.gridx = 0; gbcVendas.gridy = 3; gbcVendas.gridwidth = 2;
                gbcVendas.fill = GridBagConstraints.HORIZONTAL;
                gbcVendas.weightx = 1.0; gbcVendas.weighty = 0;
                gbcVendas.insets = new Insets(5, 5, 5, 5);
                
                JPanel botoesVendas = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 5));
                botoesVendas.setOpaque(false);
                
                ElegantButton btnGerarVendas = new ElegantButton("📊 Gerar", SUCCESS_COLOR, false);
                btnGerarVendas.setForeground(WHITE);
                btnGerarVendas.setFont(new Font("Segoe UI", Font.BOLD, 12));
                btnGerarVendas.setPreferredSize(new Dimension(100, 35));
                botoesVendas.add(btnGerarVendas);
                
                ElegantButton btnExportarVendas = new ElegantButton("📥 Exportar", PRIMARY_COLOR, false);
                btnExportarVendas.setForeground(WHITE);
                btnExportarVendas.setFont(new Font("Segoe UI", Font.BOLD, 12));
                btnExportarVendas.setPreferredSize(new Dimension(100, 35));
                botoesVendas.add(btnExportarVendas);
                
                panelVendas.add(botoesVendas, gbcVendas);
                tabbedPaneRelatorios.addTab("📊 Vendas", panelVendas);
                
                // Aba 2: Relatório de Produtos
                JPanel panelProdutos = new JPanel(new GridBagLayout());
                panelProdutos.setOpaque(false);
                
                GridBagConstraints gbcProdutos = new GridBagConstraints();
                gbcProdutos.insets = new Insets(5, 5, 5, 5);
                
                // Categoria de Produtos
                gbcProdutos.gridx = 0; gbcProdutos.gridy = 0;
                gbcProdutos.fill = GridBagConstraints.NONE;
                gbcProdutos.weightx = 0.3;
                JLabel lblCategoriaProdutos = new JLabel("Categoria:");
                lblCategoriaProdutos.setFont(new Font("Segoe UI", Font.BOLD, 12));
                lblCategoriaProdutos.setForeground(DARK_GRAY);
                panelProdutos.add(lblCategoriaProdutos, gbcProdutos);
                gbcProdutos.gridx = 1; gbcProdutos.gridy = 0;
                gbcProdutos.fill = GridBagConstraints.HORIZONTAL;
                gbcProdutos.weightx = 0.7;
                JComboBox<String> cbCategoriaProdutos = new JComboBox<>(new String[]{"Todas", "Eletrônicos", "Roupas", "Alimentos"});
                cbCategoriaProdutos.setFont(new Font("Segoe UI", Font.PLAIN, 12));
                panelProdutos.add(cbCategoriaProdutos, gbcProdutos);
                
                // Ordenação
                gbcProdutos.gridx = 0; gbcProdutos.gridy = 1;
                gbcProdutos.fill = GridBagConstraints.NONE;
                gbcProdutos.weightx = 0.3;
                JLabel lblOrdenacao = new JLabel("Ordenar por:");
                lblOrdenacao.setFont(new Font("Segoe UI", Font.BOLD, 12));
                lblOrdenacao.setForeground(DARK_GRAY);
                panelProdutos.add(lblOrdenacao, gbcProdutos);
                gbcProdutos.gridx = 1; gbcProdutos.gridy = 1;
                gbcProdutos.fill = GridBagConstraints.HORIZONTAL;
                gbcProdutos.weightx = 0.7;
                JComboBox<String> cbOrdenacao = new JComboBox<>(new String[]{"Mais Vendidos", "Menos Vendidos", "Nome", "Estoque"});
                cbOrdenacao.setFont(new Font("Segoe UI", Font.PLAIN, 12));
                panelProdutos.add(cbOrdenacao, gbcProdutos);
                
                // Tabela de Produtos
                gbcProdutos.gridx = 0; gbcProdutos.gridy = 2; gbcProdutos.gridwidth = 2;
                gbcProdutos.fill = GridBagConstraints.BOTH;
                gbcProdutos.weightx = 1.0; gbcProdutos.weighty = 1.0;
                gbcProdutos.insets = new Insets(10, 5, 5, 5);
                
                String[] colunasProdutos = {"Código", "Produto", "Categoria", "Estoque", "Preço", "Vendidos", "Faturamento"};
                Object[][] dadosProdutos = {};
                
                JTable tabelaProdutos = new JTable(dadosProdutos, colunasProdutos);
                tabelaProdutos.setFont(new Font("Segoe UI", Font.PLAIN, 11));
                tabelaProdutos.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 12));
                tabelaProdutos.getTableHeader().setBackground(new Color(52, 152, 219));
                tabelaProdutos.getTableHeader().setForeground(WHITE);
                tabelaProdutos.setRowHeight(25);
                tabelaProdutos.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
                
                JScrollPane scrollProdutos = new JScrollPane(tabelaProdutos);
                scrollProdutos.setPreferredSize(new Dimension(800, 200));
                panelProdutos.add(scrollProdutos, gbcProdutos);
                
                // Botões de Ação Produtos
                gbcProdutos.gridx = 0; gbcProdutos.gridy = 3; gbcProdutos.gridwidth = 2;
                gbcProdutos.fill = GridBagConstraints.HORIZONTAL;
                gbcProdutos.weightx = 1.0; gbcProdutos.weighty = 0;
                gbcProdutos.insets = new Insets(5, 5, 5, 5);
                
                JPanel botoesProdutos = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 5));
                botoesProdutos.setOpaque(false);
                
                ElegantButton btnGerarProdutos = new ElegantButton("📦 Gerar", SUCCESS_COLOR, false);
                btnGerarProdutos.setForeground(WHITE);
                btnGerarProdutos.setFont(new Font("Segoe UI", Font.BOLD, 12));
                btnGerarProdutos.setPreferredSize(new Dimension(100, 35));
                botoesProdutos.add(btnGerarProdutos);
                
                ElegantButton btnExportarProdutos = new ElegantButton("📥 Exportar", PRIMARY_COLOR, false);
                btnExportarProdutos.setForeground(WHITE);
                btnExportarProdutos.setFont(new Font("Segoe UI", Font.BOLD, 12));
                btnExportarProdutos.setPreferredSize(new Dimension(100, 35));
                botoesProdutos.add(btnExportarProdutos);
                
                panelProdutos.add(botoesProdutos, gbcProdutos);
                tabbedPaneRelatorios.addTab("📦 Produtos", panelProdutos);
                
                // Aba 3: Relatório de Clientes
                JPanel panelClientes = new JPanel(new GridBagLayout());
                panelClientes.setOpaque(false);
                
                GridBagConstraints gbcClientes = new GridBagConstraints();
                gbcClientes.insets = new Insets(5, 5, 5, 5);
                
                // Tipo de Cliente
                gbcClientes.gridx = 0; gbcClientes.gridy = 0;
                gbcClientes.fill = GridBagConstraints.NONE;
                gbcClientes.weightx = 0.3;
                JLabel lblTipoCliente = new JLabel("Tipo:");
                lblTipoCliente.setFont(new Font("Segoe UI", Font.BOLD, 12));
                lblTipoCliente.setForeground(DARK_GRAY);
                panelClientes.add(lblTipoCliente, gbcClientes);
                gbcClientes.gridx = 1; gbcClientes.gridy = 0;
                gbcClientes.fill = GridBagConstraints.HORIZONTAL;
                gbcClientes.weightx = 0.7;
                JComboBox<String> cbTipoCliente = new JComboBox<>(new String[]{"Todos", "Ativos", "Inativos", "Novos"});
                cbTipoCliente.setFont(new Font("Segoe UI", Font.PLAIN, 12));
                panelClientes.add(cbTipoCliente, gbcClientes);
                
                // Período de Clientes
                gbcClientes.gridx = 0; gbcClientes.gridy = 1;
                gbcClientes.fill = GridBagConstraints.NONE;
                gbcClientes.weightx = 0.3;
                JLabel lblPeriodoClientes = new JLabel("Período:");
                lblPeriodoClientes.setFont(new Font("Segoe UI", Font.BOLD, 12));
                lblPeriodoClientes.setForeground(DARK_GRAY);
                panelClientes.add(lblPeriodoClientes, gbcClientes);
                gbcClientes.gridx = 1; gbcClientes.gridy = 1;
                gbcClientes.fill = GridBagConstraints.HORIZONTAL;
                gbcClientes.weightx = 0.7;
                JPanel periodoPanelClientes = new JPanel(new FlowLayout());
                periodoPanelClientes.setOpaque(false);
                ElegantTextField txtDataInicioClientes = new ElegantTextField(10);
                ElegantTextField txtDataFimClientes = new ElegantTextField(10);
                periodoPanelClientes.add(txtDataInicioClientes);
                periodoPanelClientes.add(new JLabel(" até "));
                periodoPanelClientes.add(txtDataFimClientes);
                panelClientes.add(periodoPanelClientes, gbcClientes);
                
                // Tabela de Clientes
                gbcClientes.gridx = 0; gbcClientes.gridy = 2; gbcClientes.gridwidth = 2;
                gbcClientes.fill = GridBagConstraints.BOTH;
                gbcClientes.weightx = 1.0; gbcClientes.weighty = 1.0;
                gbcClientes.insets = new Insets(10, 5, 5, 5);
                
                String[] colunasClientes = {"ID", "Nome", "CPF/CNPJ", "Telefone", "Compras", "Total Gasto", "Última Compra"};
                Object[][] dadosClientes = {};
                
                JTable tabelaClientes = new JTable(dadosClientes, colunasClientes);
                tabelaClientes.setFont(new Font("Segoe UI", Font.PLAIN, 11));
                tabelaClientes.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 12));
                tabelaClientes.getTableHeader().setBackground(new Color(52, 152, 219));
                tabelaClientes.getTableHeader().setForeground(WHITE);
                tabelaClientes.setRowHeight(25);
                tabelaClientes.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
                
                JScrollPane scrollClientes = new JScrollPane(tabelaClientes);
                scrollClientes.setPreferredSize(new Dimension(800, 200));
                panelClientes.add(scrollClientes, gbcClientes);
                
                // Botões de Ação Clientes
                gbcClientes.gridx = 0; gbcClientes.gridy = 3; gbcClientes.gridwidth = 2;
                gbcClientes.fill = GridBagConstraints.HORIZONTAL;
                gbcClientes.weightx = 1.0; gbcClientes.weighty = 0;
                gbcClientes.insets = new Insets(5, 5, 5, 5);
                
                JPanel botoesClientes = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 5));
                botoesClientes.setOpaque(false);
                
                ElegantButton btnGerarClientes = new ElegantButton("👥 Gerar", SUCCESS_COLOR, false);
                btnGerarClientes.setForeground(WHITE);
                btnGerarClientes.setFont(new Font("Segoe UI", Font.BOLD, 12));
                btnGerarClientes.setPreferredSize(new Dimension(100, 35));
                botoesClientes.add(btnGerarClientes);
                
                ElegantButton btnExportarClientes = new ElegantButton("📥 Exportar", PRIMARY_COLOR, false);
                btnExportarClientes.setForeground(WHITE);
                btnExportarClientes.setFont(new Font("Segoe UI", Font.BOLD, 12));
                btnExportarClientes.setPreferredSize(new Dimension(100, 35));
                botoesClientes.add(btnExportarClientes);
                
                panelClientes.add(botoesClientes, gbcClientes);
                tabbedPaneRelatorios.addTab("👥 Clientes", panelClientes);
                
                // Aba 4: Relatório Financeiro
                JPanel panelFinanceiro = new JPanel(new GridBagLayout());
                panelFinanceiro.setOpaque(false);
                
                GridBagConstraints gbcFinanceiro = new GridBagConstraints();
                gbcFinanceiro.insets = new Insets(5, 5, 5, 5);
                
                // Tipo Financeiro
                gbcFinanceiro.gridx = 0; gbcFinanceiro.gridy = 0;
                gbcFinanceiro.fill = GridBagConstraints.NONE;
                gbcFinanceiro.weightx = 0.3;
                JLabel lblTipoFinanceiro = new JLabel("Tipo:");
                lblTipoFinanceiro.setFont(new Font("Segoe UI", Font.BOLD, 12));
                lblTipoFinanceiro.setForeground(DARK_GRAY);
                panelFinanceiro.add(lblTipoFinanceiro, gbcFinanceiro);
                gbcFinanceiro.gridx = 1; gbcFinanceiro.gridy = 0;
                gbcFinanceiro.fill = GridBagConstraints.HORIZONTAL;
                gbcFinanceiro.weightx = 0.7;
                JComboBox<String> cbTipoFinanceiro = new JComboBox<>(new String[]{"Receitas", "Despesas", "Fluxo Caixa", "Lucratividade"});
                cbTipoFinanceiro.setFont(new Font("Segoe UI", Font.PLAIN, 12));
                panelFinanceiro.add(cbTipoFinanceiro, gbcFinanceiro);
                
                // Período Financeiro
                gbcFinanceiro.gridx = 0; gbcFinanceiro.gridy = 1;
                gbcFinanceiro.fill = GridBagConstraints.NONE;
                gbcFinanceiro.weightx = 0.3;
                JLabel lblPeriodoFinanceiro = new JLabel("Período:");
                lblPeriodoFinanceiro.setFont(new Font("Segoe UI", Font.BOLD, 12));
                lblPeriodoFinanceiro.setForeground(DARK_GRAY);
                panelFinanceiro.add(lblPeriodoFinanceiro, gbcFinanceiro);
                gbcFinanceiro.gridx = 1; gbcFinanceiro.gridy = 1;
                gbcFinanceiro.fill = GridBagConstraints.HORIZONTAL;
                gbcFinanceiro.weightx = 0.7;
                JPanel periodoPanelFinanceiro = new JPanel(new FlowLayout());
                periodoPanelFinanceiro.setOpaque(false);
                ElegantTextField txtDataInicioFinanceiro = new ElegantTextField(10);
                ElegantTextField txtDataFimFinanceiro = new ElegantTextField(10);
                periodoPanelFinanceiro.add(txtDataInicioFinanceiro);
                periodoPanelFinanceiro.add(new JLabel(" até "));
                periodoPanelFinanceiro.add(txtDataFimFinanceiro);
                panelFinanceiro.add(periodoPanelFinanceiro, gbcFinanceiro);
                
                // Resumo Financeiro
                gbcFinanceiro.gridx = 0; gbcFinanceiro.gridy = 2; gbcFinanceiro.gridwidth = 2;
                gbcFinanceiro.fill = GridBagConstraints.HORIZONTAL;
                gbcFinanceiro.weightx = 1.0; gbcFinanceiro.weighty = 0;
                gbcFinanceiro.insets = new Insets(10, 5, 5, 5);
                
                JPanel resumoFinanceiro = new JPanel(new GridBagLayout());
                resumoFinanceiro.setBorder(BorderFactory.createTitledBorder(
                    BorderFactory.createEtchedBorder(), "📊 Resumo Financeiro",
                    TitledBorder.LEFT, TitledBorder.TOP, new Font("Segoe UI", Font.BOLD, 12),
                    DARK_GRAY));
                resumoFinanceiro.setOpaque(false);
                
                GridBagConstraints gbcResumo = new GridBagConstraints();
                gbcResumo.insets = new Insets(5, 5, 5, 5);
                
                // Total Receitas
                gbcResumo.gridx = 0; gbcResumo.gridy = 0;
                gbcResumo.fill = GridBagConstraints.NONE;
                gbcResumo.weightx = 0.3;
                JLabel lblTotalReceitas = new JLabel("Total Receitas:");
                lblTotalReceitas.setFont(new Font("Segoe UI", Font.BOLD, 12));
                lblTotalReceitas.setForeground(DARK_GRAY);
                resumoFinanceiro.add(lblTotalReceitas, gbcResumo);
                gbcResumo.gridx = 1; gbcResumo.gridy = 0;
                gbcResumo.fill = GridBagConstraints.HORIZONTAL;
                gbcResumo.weightx = 0.7;
                ElegantTextField txtTotalReceitas = new ElegantTextField(15);
                txtTotalReceitas.setEditable(false);
                txtTotalReceitas.setText("R$ 0,00");
                txtTotalReceitas.setForeground(new Color(0, 150, 0));
                resumoFinanceiro.add(txtTotalReceitas, gbcResumo);
                
                // Total Despesas
                gbcResumo.gridx = 2; gbcResumo.gridy = 0;
                gbcResumo.fill = GridBagConstraints.NONE;
                gbcResumo.weightx = 0.3;
                JLabel lblTotalDespesas = new JLabel("Total Despesas:");
                lblTotalDespesas.setFont(new Font("Segoe UI", Font.BOLD, 12));
                lblTotalDespesas.setForeground(DARK_GRAY);
                resumoFinanceiro.add(lblTotalDespesas, gbcResumo);
                gbcResumo.gridx = 3; gbcResumo.gridy = 0;
                gbcResumo.fill = GridBagConstraints.HORIZONTAL;
                gbcResumo.weightx = 0.7;
                ElegantTextField txtTotalDespesas = new ElegantTextField(15);
                txtTotalDespesas.setEditable(false);
                txtTotalDespesas.setText("R$ 0,00");
                txtTotalDespesas.setForeground(new Color(200, 50, 50));
                resumoFinanceiro.add(txtTotalDespesas, gbcResumo);
                
                // Saldo
                gbcResumo.gridx = 0; gbcResumo.gridy = 1;
                gbcResumo.fill = GridBagConstraints.NONE;
                gbcResumo.weightx = 0.3;
                JLabel lblSaldo = new JLabel("Saldo:");
                lblSaldo.setFont(new Font("Segoe UI", Font.BOLD, 12));
                lblSaldo.setForeground(DARK_GRAY);
                resumoFinanceiro.add(lblSaldo, gbcResumo);
                gbcResumo.gridx = 1; gbcResumo.gridy = 1;
                gbcResumo.fill = GridBagConstraints.HORIZONTAL;
                gbcResumo.weightx = 0.7;
                ElegantTextField txtSaldo = new ElegantTextField(15);
                txtSaldo.setEditable(false);
                txtSaldo.setText("R$ 0,00");
                txtSaldo.setForeground(new Color(0, 100, 200));
                resumoFinanceiro.add(txtSaldo, gbcResumo);
                
                panelFinanceiro.add(resumoFinanceiro, gbcFinanceiro);
                
                // Tabela Financeira
                gbcFinanceiro.gridx = 0; gbcFinanceiro.gridy = 3; gbcFinanceiro.gridwidth = 2;
                gbcFinanceiro.fill = GridBagConstraints.BOTH;
                gbcFinanceiro.weightx = 1.0; gbcFinanceiro.weighty = 1.0;
                gbcFinanceiro.insets = new Insets(5, 5, 5, 5);
                
                String[] colunasFinanceiro = {"Data", "Descrição", "Categoria", "Tipo", "Valor", "Forma Pagto"};
                Object[][] dadosFinanceiro = {};
                
                JTable tabelaFinanceiroRelatorio = new JTable(dadosFinanceiro, colunasFinanceiro);
                tabelaFinanceiroRelatorio.setFont(new Font("Segoe UI", Font.PLAIN, 11));
                tabelaFinanceiroRelatorio.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 12));
                tabelaFinanceiroRelatorio.getTableHeader().setBackground(new Color(52, 152, 219));
                tabelaFinanceiroRelatorio.getTableHeader().setForeground(WHITE);
                tabelaFinanceiroRelatorio.setRowHeight(25);
                tabelaFinanceiroRelatorio.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
                
                JScrollPane scrollFinanceiroRelatorio = new JScrollPane(tabelaFinanceiroRelatorio);
                scrollFinanceiroRelatorio.setPreferredSize(new Dimension(800, 200));
                panelFinanceiro.add(scrollFinanceiroRelatorio, gbcFinanceiro);
                
                // Botões de Ação Financeiro
                gbcFinanceiro.gridx = 0; gbcFinanceiro.gridy = 4; gbcFinanceiro.gridwidth = 2;
                gbcFinanceiro.fill = GridBagConstraints.HORIZONTAL;
                gbcFinanceiro.weightx = 1.0; gbcFinanceiro.weighty = 0;
                gbcFinanceiro.insets = new Insets(5, 5, 5, 5);
                
                JPanel botoesFinanceiro = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 5));
                botoesFinanceiro.setOpaque(false);
                
                ElegantButton btnGerarFinanceiro = new ElegantButton("💰 Gerar", SUCCESS_COLOR, false);
                btnGerarFinanceiro.setForeground(WHITE);
                btnGerarFinanceiro.setFont(new Font("Segoe UI", Font.BOLD, 12));
                btnGerarFinanceiro.setPreferredSize(new Dimension(100, 35));
                botoesFinanceiro.add(btnGerarFinanceiro);
                
                ElegantButton btnExportarFinanceiro = new ElegantButton("📥 Exportar", PRIMARY_COLOR, false);
                btnExportarFinanceiro.setForeground(WHITE);
                btnExportarFinanceiro.setFont(new Font("Segoe UI", Font.BOLD, 12));
                btnExportarFinanceiro.setPreferredSize(new Dimension(100, 35));
                botoesFinanceiro.add(btnExportarFinanceiro);
                
                panelFinanceiro.add(botoesFinanceiro, gbcFinanceiro);
                tabbedPaneRelatorios.addTab("💰 Financeiro", panelFinanceiro);
                
                // Aba 5: Relatório de Estoque
                JPanel panelEstoque = new JPanel(new GridBagLayout());
                panelEstoque.setOpaque(false);
                
                GridBagConstraints gbcEstoque = new GridBagConstraints();
                gbcEstoque.insets = new Insets(5, 5, 5, 5);
                
                // Categoria Estoque
                gbcEstoque.gridx = 0; gbcEstoque.gridy = 0;
                gbcEstoque.fill = GridBagConstraints.NONE;
                gbcEstoque.weightx = 0.3;
                JLabel lblCategoriaEstoqueRelatorio = new JLabel("Categoria:");
                lblCategoriaEstoqueRelatorio.setFont(new Font("Segoe UI", Font.BOLD, 12));
                lblCategoriaEstoqueRelatorio.setForeground(DARK_GRAY);
                panelEstoque.add(lblCategoriaEstoqueRelatorio, gbcEstoque);
                gbcEstoque.gridx = 1; gbcEstoque.gridy = 0;
                gbcEstoque.fill = GridBagConstraints.HORIZONTAL;
                gbcEstoque.weightx = 0.7;
                JComboBox<String> cbCategoriaEstoqueRelatorio = new JComboBox<>(new String[]{"Todas", "Eletrônicos", "Roupas", "Alimentos"});
                cbCategoriaEstoqueRelatorio.setFont(new Font("Segoe UI", Font.PLAIN, 12));
                panelEstoque.add(cbCategoriaEstoqueRelatorio, gbcEstoque);
                
                // Status Estoque
                gbcEstoque.gridx = 0; gbcEstoque.gridy = 1;
                gbcEstoque.fill = GridBagConstraints.NONE;
                gbcEstoque.weightx = 0.3;
                JLabel lblStatusEstoqueRelatorio = new JLabel("Status:");
                lblStatusEstoqueRelatorio.setFont(new Font("Segoe UI", Font.BOLD, 12));
                lblStatusEstoqueRelatorio.setForeground(DARK_GRAY);
                panelEstoque.add(lblStatusEstoqueRelatorio, gbcEstoque);
                gbcEstoque.gridx = 1; gbcEstoque.gridy = 1;
                gbcEstoque.fill = GridBagConstraints.HORIZONTAL;
                gbcEstoque.weightx = 0.7;
                JComboBox<String> cbStatusEstoqueRelatorio = new JComboBox<>(new String[]{"Todos", "Estoque Baixo", "Normal", "Excedente"});
                cbStatusEstoqueRelatorio.setFont(new Font("Segoe UI", Font.PLAIN, 12));
                panelEstoque.add(cbStatusEstoqueRelatorio, gbcEstoque);
                
                // Tabela de Estoque
                gbcEstoque.gridx = 0; gbcEstoque.gridy = 2; gbcEstoque.gridwidth = 2;
                gbcEstoque.fill = GridBagConstraints.BOTH;
                gbcEstoque.weightx = 1.0; gbcEstoque.weighty = 1.0;
                gbcEstoque.insets = new Insets(10, 5, 5, 5);
                
                String[] colunasEstoque = {"Código", "Produto", "Categoria", "Estoque Atual", "Estoque Mínimo", "Valor Unitário", "Valor Total", "Status"};
                Object[][] dadosEstoque = {};
                
                JTable tabelaEstoqueRelatorio = new JTable(dadosEstoque, colunasEstoque);
                tabelaEstoqueRelatorio.setFont(new Font("Segoe UI", Font.PLAIN, 11));
                tabelaEstoqueRelatorio.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 12));
                tabelaEstoqueRelatorio.getTableHeader().setBackground(new Color(52, 152, 219));
                tabelaEstoqueRelatorio.getTableHeader().setForeground(WHITE);
                tabelaEstoqueRelatorio.setRowHeight(25);
                tabelaEstoqueRelatorio.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
                
                JScrollPane scrollEstoqueRelatorio = new JScrollPane(tabelaEstoqueRelatorio);
                scrollEstoqueRelatorio.setPreferredSize(new Dimension(800, 200));
                panelEstoque.add(scrollEstoqueRelatorio, gbcEstoque);
                
                // Botões de Ação Estoque
                gbcEstoque.gridx = 0; gbcEstoque.gridy = 3; gbcEstoque.gridwidth = 2;
                gbcEstoque.fill = GridBagConstraints.HORIZONTAL;
                gbcEstoque.weightx = 1.0; gbcEstoque.weighty = 0;
                gbcEstoque.insets = new Insets(5, 5, 5, 5);
                
                JPanel botoesEstoque = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 5));
                botoesEstoque.setOpaque(false);
                
                ElegantButton btnGerarEstoque = new ElegantButton("📋 Gerar", SUCCESS_COLOR, false);
                btnGerarEstoque.setForeground(WHITE);
                btnGerarEstoque.setFont(new Font("Segoe UI", Font.BOLD, 12));
                btnGerarEstoque.setPreferredSize(new Dimension(100, 35));
                botoesEstoque.add(btnGerarEstoque);
                
                ElegantButton btnExportarEstoque = new ElegantButton("📥 Exportar", PRIMARY_COLOR, false);
                btnExportarEstoque.setForeground(WHITE);
                btnExportarEstoque.setFont(new Font("Segoe UI", Font.BOLD, 12));
                btnExportarEstoque.setPreferredSize(new Dimension(100, 35));
                botoesEstoque.add(btnExportarEstoque);
                
                panelEstoque.add(botoesEstoque, gbcEstoque);
                tabbedPaneRelatorios.addTab("📋 Estoque", panelEstoque);
                
                panel.add(tabbedPaneRelatorios, gbc);
                break;
                
                            
            case "📊 Dashboard":
                // Painel principal do Dashboard
                gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
                gbc.fill = GridBagConstraints.BOTH;
                gbc.weightx = 1.0; gbc.weighty = 1.0;
                gbc.insets = new Insets(5, 5, 5, 5);
                
                // Painel de KPIs e Métricas
                JPanel kpisPanel = new JPanel(new GridBagLayout());
                kpisPanel.setBorder(BorderFactory.createTitledBorder(
                    BorderFactory.createEtchedBorder(), "📊 Indicadores Chave de Performance",
                    TitledBorder.LEFT, TitledBorder.TOP, new Font("Segoe UI", Font.BOLD, 12),
                    DARK_GRAY));
                kpisPanel.setOpaque(false);
                
                GridBagConstraints gbcKpis = new GridBagConstraints();
                gbcKpis.insets = new Insets(5, 5, 5, 5);
                
                // Vendas do Dia
                gbcKpis.gridx = 0; gbcKpis.gridy = 0;
                gbcKpis.fill = GridBagConstraints.BOTH;
                gbcKpis.weightx = 0.25; gbcKpis.weighty = 1.0;
                
                JPanel vendasDiaPanel = new JPanel(new BorderLayout());
                vendasDiaPanel.setBorder(BorderFactory.createLineBorder(new Color(52, 152, 219), 1));
                vendasDiaPanel.setOpaque(false);
                
                JLabel lblVendasDiaTitulo = new JLabel("📈 Vendas do Dia", SwingConstants.CENTER);
                lblVendasDiaTitulo.setFont(new Font("Segoe UI", Font.BOLD, 12));
                lblVendasDiaTitulo.setForeground(new Color(52, 152, 219));
                vendasDiaPanel.add(lblVendasDiaTitulo, BorderLayout.NORTH);
                
                ElegantTextField txtVendasDia = new ElegantTextField(15);
                txtVendasDia.setEditable(false);
                txtVendasDia.setText("R$ 0,00");
                txtVendasDia.setFont(new Font("Segoe UI", Font.BOLD, 16));
                txtVendasDia.setHorizontalAlignment(JTextField.CENTER);
                txtVendasDia.setForeground(new Color(0, 150, 0));
                vendasDiaPanel.add(txtVendasDia, BorderLayout.CENTER);
                
                kpisPanel.add(vendasDiaPanel, gbcKpis);
                
                // Pedidos do Dia
                gbcKpis.gridx = 1; gbcKpis.gridy = 0;
                gbcKpis.fill = GridBagConstraints.BOTH;
                gbcKpis.weightx = 0.25; gbcKpis.weighty = 1.0;
                
                JPanel pedidosDiaPanel = new JPanel(new BorderLayout());
                pedidosDiaPanel.setBorder(BorderFactory.createLineBorder(new Color(155, 89, 182), 1));
                pedidosDiaPanel.setOpaque(false);
                
                JLabel lblPedidosDiaTitulo = new JLabel("🛒 Pedidos do Dia", SwingConstants.CENTER);
                lblPedidosDiaTitulo.setFont(new Font("Segoe UI", Font.BOLD, 12));
                lblPedidosDiaTitulo.setForeground(new Color(155, 89, 182));
                pedidosDiaPanel.add(lblPedidosDiaTitulo, BorderLayout.NORTH);
                
                ElegantTextField txtPedidosDia = new ElegantTextField(15);
                txtPedidosDia.setEditable(false);
                txtPedidosDia.setText("0");
                txtPedidosDia.setFont(new Font("Segoe UI", Font.BOLD, 16));
                txtPedidosDia.setHorizontalAlignment(JTextField.CENTER);
                txtPedidosDia.setForeground(new Color(155, 89, 182));
                pedidosDiaPanel.add(txtPedidosDia, BorderLayout.CENTER);
                
                kpisPanel.add(pedidosDiaPanel, gbcKpis);
                
                // Clientes Ativos
                gbcKpis.gridx = 2; gbcKpis.gridy = 0;
                gbcKpis.fill = GridBagConstraints.BOTH;
                gbcKpis.weightx = 0.25; gbcKpis.weighty = 1.0;
                
                JPanel clientesAtivosPanel = new JPanel(new BorderLayout());
                clientesAtivosPanel.setBorder(BorderFactory.createLineBorder(new Color(230, 126, 34), 1));
                clientesAtivosPanel.setOpaque(false);
                
                JLabel lblClientesAtivosTitulo = new JLabel("👥 Clientes Ativos", SwingConstants.CENTER);
                lblClientesAtivosTitulo.setFont(new Font("Segoe UI", Font.BOLD, 12));
                lblClientesAtivosTitulo.setForeground(new Color(230, 126, 34));
                clientesAtivosPanel.add(lblClientesAtivosTitulo, BorderLayout.NORTH);
                
                ElegantTextField txtClientesAtivos = new ElegantTextField(15);
                txtClientesAtivos.setEditable(false);
                txtClientesAtivos.setText("0");
                txtClientesAtivos.setFont(new Font("Segoe UI", Font.BOLD, 16));
                txtClientesAtivos.setHorizontalAlignment(JTextField.CENTER);
                txtClientesAtivos.setForeground(new Color(230, 126, 34));
                clientesAtivosPanel.add(txtClientesAtivos, BorderLayout.CENTER);
                
                kpisPanel.add(clientesAtivosPanel, gbcKpis);
                
                // Produtos em Estoque
                gbcKpis.gridx = 3; gbcKpis.gridy = 0;
                gbcKpis.fill = GridBagConstraints.BOTH;
                gbcKpis.weightx = 0.25; gbcKpis.weighty = 1.0;
                
                JPanel produtosEstoquePanel = new JPanel(new BorderLayout());
                produtosEstoquePanel.setBorder(BorderFactory.createLineBorder(new Color(26, 188, 156), 1));
                produtosEstoquePanel.setOpaque(false);
                
                JLabel lblProdutosEstoqueTitulo = new JLabel("📦 Produtos em Estoque", SwingConstants.CENTER);
                lblProdutosEstoqueTitulo.setFont(new Font("Segoe UI", Font.BOLD, 12));
                lblProdutosEstoqueTitulo.setForeground(new Color(26, 188, 156));
                produtosEstoquePanel.add(lblProdutosEstoqueTitulo, BorderLayout.NORTH);
                
                ElegantTextField txtProdutosEstoque = new ElegantTextField(15);
                txtProdutosEstoque.setEditable(false);
                txtProdutosEstoque.setText("0");
                txtProdutosEstoque.setFont(new Font("Segoe UI", Font.BOLD, 16));
                txtProdutosEstoque.setHorizontalAlignment(JTextField.CENTER);
                txtProdutosEstoque.setForeground(new Color(26, 188, 156));
                produtosEstoquePanel.add(txtProdutosEstoque, BorderLayout.CENTER);
                
                kpisPanel.add(produtosEstoquePanel, gbcKpis);
                
                // Adicionar painel de KPIs ao painel principal
                gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
                gbc.fill = GridBagConstraints.HORIZONTAL;
                gbc.weightx = 1.0; gbc.weighty = 0;
                gbc.insets = new Insets(5, 5, 10, 5);
                panel.add(kpisPanel, gbc);
                
                // Painel de Filtros do Dashboard
                JPanel filtrosDashboardPanel = new JPanel(new GridBagLayout());
                filtrosDashboardPanel.setBorder(BorderFactory.createTitledBorder(
                    BorderFactory.createEtchedBorder(), "🔍 Filtros do Dashboard",
                    TitledBorder.LEFT, TitledBorder.TOP, new Font("Segoe UI", Font.BOLD, 12),
                    DARK_GRAY));
                filtrosDashboardPanel.setOpaque(false);
                
                GridBagConstraints gbcFiltrosDashboard = new GridBagConstraints();
                gbcFiltrosDashboard.insets = new Insets(5, 5, 5, 5);
                
                // Período
                gbcFiltrosDashboard.gridx = 0; gbcFiltrosDashboard.gridy = 0;
                gbcFiltrosDashboard.fill = GridBagConstraints.NONE;
                gbcFiltrosDashboard.weightx = 0.2;
                JLabel lblPeriodoDashboard = new JLabel("Período:");
                lblPeriodoDashboard.setFont(new Font("Segoe UI", Font.BOLD, 12));
                lblPeriodoDashboard.setForeground(DARK_GRAY);
                filtrosDashboardPanel.add(lblPeriodoDashboard, gbcFiltrosDashboard);
                gbcFiltrosDashboard.gridx = 1; gbcFiltrosDashboard.gridy = 0;
                gbcFiltrosDashboard.fill = GridBagConstraints.HORIZONTAL;
                gbcFiltrosDashboard.weightx = 0.3;
                JComboBox<String> cbPeriodoDashboard = new JComboBox<>(new String[]{"Hoje", "Esta Semana", "Este Mês", "Este Ano", "Personalizado"});
                cbPeriodoDashboard.setFont(new Font("Segoe UI", Font.PLAIN, 12));
                filtrosDashboardPanel.add(cbPeriodoDashboard, gbcFiltrosDashboard);
                
                // Loja/Filial
                gbcFiltrosDashboard.gridx = 2; gbcFiltrosDashboard.gridy = 0;
                gbcFiltrosDashboard.fill = GridBagConstraints.NONE;
                gbcFiltrosDashboard.weightx = 0.2;
                JLabel lblLojaDashboard = new JLabel("Loja:");
                lblLojaDashboard.setFont(new Font("Segoe UI", Font.BOLD, 12));
                lblLojaDashboard.setForeground(DARK_GRAY);
                filtrosDashboardPanel.add(lblLojaDashboard, gbcFiltrosDashboard);
                gbcFiltrosDashboard.gridx = 3; gbcFiltrosDashboard.gridy = 0;
                gbcFiltrosDashboard.fill = GridBagConstraints.HORIZONTAL;
                gbcFiltrosDashboard.weightx = 0.3;
                JComboBox<String> cbLojaDashboard = new JComboBox<>(new String[]{"Todas", "Matriz", "Filial 1", "Filial 2"});
                cbLojaDashboard.setFont(new Font("Segoe UI", Font.PLAIN, 12));
                filtrosDashboardPanel.add(cbLojaDashboard, gbcFiltrosDashboard);
                
                // Adicionar painel de filtros
                gbc.gridx = 0; gbc.gridy = 1; gbc.gridwidth = 2;
                gbc.fill = GridBagConstraints.HORIZONTAL;
                gbc.weightx = 1.0; gbc.weighty = 0;
                gbc.insets = new Insets(5, 5, 10, 5);
                panel.add(filtrosDashboardPanel, gbc);
                
                // Tabela de Atividades Recentes
                gbc.gridx = 0; gbc.gridy = 2; gbc.gridwidth = 2;
                gbc.fill = GridBagConstraints.BOTH;
                gbc.weightx = 1.0; gbc.weighty = 1.0;
                gbc.insets = new Insets(5, 5, 5, 5);
                
                String[] colunasDashboard = {"Data/Hora", "Tipo", "Descrição", "Usuário", "Valor", "Status"};
                Object[][] dadosDashboard = {
                    {"08/05/2026 14:30", "Venda", "Notebook Dell", "João", "R$ 3.500,00", "Concluída"},
                    {"08/05/2026 14:25", "Entrada", "Mouse Wireless", "Maria", "R$ 150,00", "Processada"},
                    {"08/05/2026 14:20", "Venda", "Monitor LG 24\"", "Pedro", "R$ 890,00", "Concluída"},
                    {"08/05/2026 14:15", "Orçamento", "Teclado Mecânico", "Ana", "R$ 320,00", "Pendente"},
                    {"08/05/2026 14:10", "Venda", "Webcam HD", "Carlos", "R$ 180,00", "Concluída"}
                };
                
                JTable tabelaDashboard = new JTable(dadosDashboard, colunasDashboard);
                tabelaDashboard.setFont(new Font("Segoe UI", Font.PLAIN, 11));
                tabelaDashboard.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 12));
                tabelaDashboard.getTableHeader().setBackground(new Color(52, 152, 219));
                tabelaDashboard.getTableHeader().setForeground(WHITE);
                tabelaDashboard.setRowHeight(25);
                tabelaDashboard.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
                tabelaDashboard.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
                
                JScrollPane scrollPaneDashboard = new JScrollPane(tabelaDashboard);
                scrollPaneDashboard.setPreferredSize(new Dimension(1200, 300));
                scrollPaneDashboard.setBorder(BorderFactory.createTitledBorder(
                    BorderFactory.createEtchedBorder(), "📋 Atividades Recentes",
                    TitledBorder.LEFT, TitledBorder.TOP, new Font("Segoe UI", Font.BOLD, 12),
                    DARK_GRAY));
                panel.add(scrollPaneDashboard, gbc);
                
                // Painel de Ações do Dashboard
                gbc.gridx = 0; gbc.gridy = 3; gbc.gridwidth = 2;
                gbc.fill = GridBagConstraints.HORIZONTAL;
                gbc.weightx = 1.0; gbc.weighty = 0;
                gbc.insets = new Insets(10, 5, 5, 5);
                
                JPanel acoesDashboardPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 5));
                acoesDashboardPanel.setOpaque(false);
                
                ElegantButton btnAtualizarDashboard = new ElegantButton("🔄 Atualizar", PRIMARY_COLOR, false);
                btnAtualizarDashboard.setForeground(WHITE);
                btnAtualizarDashboard.setFont(new Font("Segoe UI", Font.BOLD, 14));
                btnAtualizarDashboard.setPreferredSize(new Dimension(120, 40));
                btnAtualizarDashboard.setToolTipText("Atualizar dados do dashboard");
                acoesDashboardPanel.add(btnAtualizarDashboard);
                
                ElegantButton btnExportarDashboard = new ElegantButton("📊 Exportar", SUCCESS_COLOR, false);
                btnExportarDashboard.setForeground(WHITE);
                btnExportarDashboard.setFont(new Font("Segoe UI", Font.BOLD, 14));
                btnExportarDashboard.setPreferredSize(new Dimension(120, 40));
                btnExportarDashboard.setToolTipText("Exportar dashboard");
                acoesDashboardPanel.add(btnExportarDashboard);
                
                ElegantButton btnImprimirDashboard = new ElegantButton("🖨️ Imprimir", new Color(100, 100, 100), false);
                btnImprimirDashboard.setForeground(WHITE);
                btnImprimirDashboard.setFont(new Font("Segoe UI", Font.BOLD, 14));
                btnImprimirDashboard.setPreferredSize(new Dimension(120, 40));
                btnImprimirDashboard.setToolTipText("Imprimir dashboard");
                acoesDashboardPanel.add(btnImprimirDashboard);
                
                ElegantButton btnConfigurarDashboard = new ElegantButton("⚙️ Configurar", WARNING_COLOR, false);
                btnConfigurarDashboard.setForeground(WHITE);
                btnConfigurarDashboard.setFont(new Font("Segoe UI", Font.BOLD, 14));
                btnConfigurarDashboard.setPreferredSize(new Dimension(120, 40));
                btnConfigurarDashboard.setToolTipText("Configurar dashboard");
                acoesDashboardPanel.add(btnConfigurarDashboard);
                
                panel.add(acoesDashboardPanel, gbc);
                break;
                
            case "🎯 Análise de Vendas":
                // Painel principal de Análise de Vendas
                gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
                gbc.fill = GridBagConstraints.BOTH;
                gbc.weightx = 1.0; gbc.weighty = 1.0;
                gbc.insets = new Insets(5, 5, 5, 5);
                
                // Painel de Filtros de Análise
                JPanel filtrosAnalisePanel = new JPanel(new GridBagLayout());
                filtrosAnalisePanel.setBorder(BorderFactory.createTitledBorder(
                    BorderFactory.createEtchedBorder(), "🔍 Filtros de Análise de Vendas",
                    TitledBorder.LEFT, TitledBorder.TOP, new Font("Segoe UI", Font.BOLD, 12),
                    DARK_GRAY));
                filtrosAnalisePanel.setOpaque(false);
                
                GridBagConstraints gbcFiltrosAnalise = new GridBagConstraints();
                gbcFiltrosAnalise.insets = new Insets(5, 5, 5, 5);
                
                // Período Inicial
                gbcFiltrosAnalise.gridx = 0; gbcFiltrosAnalise.gridy = 0;
                gbcFiltrosAnalise.fill = GridBagConstraints.NONE;
                gbcFiltrosAnalise.weightx = 0.2;
                JLabel lblPeriodoInicial = new JLabel("Data Inicial:");
                lblPeriodoInicial.setFont(new Font("Segoe UI", Font.BOLD, 12));
                lblPeriodoInicial.setForeground(DARK_GRAY);
                filtrosAnalisePanel.add(lblPeriodoInicial, gbcFiltrosAnalise);
                gbcFiltrosAnalise.gridx = 1; gbcFiltrosAnalise.gridy = 0;
                gbcFiltrosAnalise.fill = GridBagConstraints.HORIZONTAL;
                gbcFiltrosAnalise.weightx = 0.3;
                ElegantTextField txtDataInicial = new ElegantTextField(15);
                txtDataInicial.setToolTipText("Data inicial do período de análise");
                filtrosAnalisePanel.add(txtDataInicial, gbcFiltrosAnalise);
                
                // Período Final
                gbcFiltrosAnalise.gridx = 2; gbcFiltrosAnalise.gridy = 0;
                gbcFiltrosAnalise.fill = GridBagConstraints.NONE;
                gbcFiltrosAnalise.weightx = 0.2;
                JLabel lblPeriodoFinal = new JLabel("Data Final:");
                lblPeriodoFinal.setFont(new Font("Segoe UI", Font.BOLD, 12));
                lblPeriodoFinal.setForeground(DARK_GRAY);
                filtrosAnalisePanel.add(lblPeriodoFinal, gbcFiltrosAnalise);
                gbcFiltrosAnalise.gridx = 3; gbcFiltrosAnalise.gridy = 0;
                gbcFiltrosAnalise.fill = GridBagConstraints.HORIZONTAL;
                gbcFiltrosAnalise.weightx = 0.3;
                ElegantTextField txtDataFinal = new ElegantTextField(15);
                txtDataFinal.setToolTipText("Data final do período de análise");
                filtrosAnalisePanel.add(txtDataFinal, gbcFiltrosAnalise);
                
                // Categoria de Produto
                gbcFiltrosAnalise.gridx = 0; gbcFiltrosAnalise.gridy = 1;
                gbcFiltrosAnalise.fill = GridBagConstraints.NONE;
                gbcFiltrosAnalise.weightx = 0.2;
                JLabel lblCategoriaProduto = new JLabel("Categoria:");
                lblCategoriaProduto.setFont(new Font("Segoe UI", Font.BOLD, 12));
                lblCategoriaProduto.setForeground(DARK_GRAY);
                filtrosAnalisePanel.add(lblCategoriaProduto, gbcFiltrosAnalise);
                gbcFiltrosAnalise.gridx = 1; gbcFiltrosAnalise.gridy = 1;
                gbcFiltrosAnalise.fill = GridBagConstraints.HORIZONTAL;
                gbcFiltrosAnalise.weightx = 0.3;
                JComboBox<String> cbCategoriaProduto = new JComboBox<>(new String[]{
                    "Todas", "Eletrônicos", "Informática", "Periféricos", "Acessórios", "Software"
                });
                cbCategoriaProduto.setFont(new Font("Segoe UI", Font.PLAIN, 12));
                cbCategoriaProduto.setToolTipText("Filtrar por categoria de produto");
                filtrosAnalisePanel.add(cbCategoriaProduto, gbcFiltrosAnalise);
                
                // Tipo de Análise
                gbcFiltrosAnalise.gridx = 2; gbcFiltrosAnalise.gridy = 1;
                gbcFiltrosAnalise.fill = GridBagConstraints.NONE;
                gbcFiltrosAnalise.weightx = 0.2;
                JLabel lblTipoAnalise = new JLabel("Análise:");
                lblTipoAnalise.setFont(new Font("Segoe UI", Font.BOLD, 12));
                lblTipoAnalise.setForeground(DARK_GRAY);
                filtrosAnalisePanel.add(lblTipoAnalise, gbcFiltrosAnalise);
                gbcFiltrosAnalise.gridx = 3; gbcFiltrosAnalise.gridy = 1;
                gbcFiltrosAnalise.fill = GridBagConstraints.HORIZONTAL;
                gbcFiltrosAnalise.weightx = 0.3;
                JComboBox<String> cbTipoAnalise = new JComboBox<>(new String[]{
                    "Vendas por Produto", "Vendas por Cliente", "Vendas por Período", "Rentabilidade", "Tendências"
                });
                cbTipoAnalise.setFont(new Font("Segoe UI", Font.PLAIN, 12));
                cbTipoAnalise.setToolTipText("Tipo de análise a ser realizada");
                filtrosAnalisePanel.add(cbTipoAnalise, gbcFiltrosAnalise);
                
                // Vendedor
                gbcFiltrosAnalise.gridx = 0; gbcFiltrosAnalise.gridy = 2;
                gbcFiltrosAnalise.fill = GridBagConstraints.NONE;
                gbcFiltrosAnalise.weightx = 0.2;
                JLabel lblVendedor = new JLabel("Vendedor:");
                lblVendedor.setFont(new Font("Segoe UI", Font.BOLD, 12));
                lblVendedor.setForeground(DARK_GRAY);
                filtrosAnalisePanel.add(lblVendedor, gbcFiltrosAnalise);
                gbcFiltrosAnalise.gridx = 1; gbcFiltrosAnalise.gridy = 2;
                gbcFiltrosAnalise.fill = GridBagConstraints.HORIZONTAL;
                gbcFiltrosAnalise.weightx = 0.3;
                JComboBox<String> cbVendedor = new JComboBox<>(new String[]{
                    "Todos", "João Silva", "Maria Santos", "Pedro Oliveira", "Ana Costa", "Carlos Ferreira"
                });
                cbVendedor.setFont(new Font("Segoe UI", Font.PLAIN, 12));
                cbVendedor.setToolTipText("Filtrar por vendedor");
                filtrosAnalisePanel.add(cbVendedor, gbcFiltrosAnalise);
                
                // Região/Loja
                gbcFiltrosAnalise.gridx = 2; gbcFiltrosAnalise.gridy = 2;
                gbcFiltrosAnalise.fill = GridBagConstraints.NONE;
                gbcFiltrosAnalise.weightx = 0.2;
                JLabel lblRegiao = new JLabel("Região:");
                lblRegiao.setFont(new Font("Segoe UI", Font.BOLD, 12));
                lblRegiao.setForeground(DARK_GRAY);
                filtrosAnalisePanel.add(lblRegiao, gbcFiltrosAnalise);
                gbcFiltrosAnalise.gridx = 3; gbcFiltrosAnalise.gridy = 2;
                gbcFiltrosAnalise.fill = GridBagConstraints.HORIZONTAL;
                gbcFiltrosAnalise.weightx = 0.3;
                JComboBox<String> cbRegiao = new JComboBox<>(new String[]{
                    "Todas", "São Paulo", "Rio de Janeiro", "Minas Gerais", "Brasília", "Online"
                });
                cbRegiao.setFont(new Font("Segoe UI", Font.PLAIN, 12));
                cbRegiao.setToolTipText("Filtrar por região/loja");
                filtrosAnalisePanel.add(cbRegiao, gbcFiltrosAnalise);
                
                // Adicionar painel de filtros
                gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
                gbc.fill = GridBagConstraints.HORIZONTAL;
                gbc.weightx = 1.0; gbc.weighty = 0;
                gbc.insets = new Insets(5, 5, 10, 5);
                panel.add(filtrosAnalisePanel, gbc);
                
                // Painel de Métricas de Análise
                JPanel metricasAnalisePanel = new JPanel(new GridBagLayout());
                metricasAnalisePanel.setBorder(BorderFactory.createTitledBorder(
                    BorderFactory.createEtchedBorder(), "📊 Métricas de Análise",
                    TitledBorder.LEFT, TitledBorder.TOP, new Font("Segoe UI", Font.BOLD, 12),
                    DARK_GRAY));
                metricasAnalisePanel.setOpaque(false);
                
                GridBagConstraints gbcMetricas = new GridBagConstraints();
                gbcMetricas.insets = new Insets(5, 5, 5, 5);
                
                // Total de Vendas
                gbcMetricas.gridx = 0; gbcMetricas.gridy = 0;
                gbcMetricas.fill = GridBagConstraints.BOTH;
                gbcMetricas.weightx = 0.25; gbcMetricas.weighty = 1.0;
                
                JPanel totalVendasPanel = new JPanel(new BorderLayout());
                totalVendasPanel.setBorder(BorderFactory.createLineBorder(new Color(0, 150, 0), 1));
                totalVendasPanel.setOpaque(false);
                
                JLabel lblTotalVendasTitulo = new JLabel("💰 Total Vendas", SwingConstants.CENTER);
                lblTotalVendasTitulo.setFont(new Font("Segoe UI", Font.BOLD, 12));
                lblTotalVendasTitulo.setForeground(new Color(0, 150, 0));
                totalVendasPanel.add(lblTotalVendasTitulo, BorderLayout.NORTH);
                
                ElegantTextField txtTotalVendas = new ElegantTextField(15);
                txtTotalVendas.setEditable(false);
                txtTotalVendas.setText("R$ 0,00");
                txtTotalVendas.setFont(new Font("Segoe UI", Font.BOLD, 16));
                txtTotalVendas.setHorizontalAlignment(JTextField.CENTER);
                txtTotalVendas.setForeground(new Color(0, 150, 0));
                totalVendasPanel.add(txtTotalVendas, BorderLayout.CENTER);
                
                metricasAnalisePanel.add(totalVendasPanel, gbcMetricas);
                
                // Média de Vendas
                gbcMetricas.gridx = 1; gbcMetricas.gridy = 0;
                gbcMetricas.fill = GridBagConstraints.BOTH;
                gbcMetricas.weightx = 0.25; gbcMetricas.weighty = 1.0;
                
                JPanel mediaVendasPanel = new JPanel(new BorderLayout());
                mediaVendasPanel.setBorder(BorderFactory.createLineBorder(new Color(52, 152, 219), 1));
                mediaVendasPanel.setOpaque(false);
                
                JLabel lblMediaVendasTitulo = new JLabel("📈 Média Vendas", SwingConstants.CENTER);
                lblMediaVendasTitulo.setFont(new Font("Segoe UI", Font.BOLD, 12));
                lblMediaVendasTitulo.setForeground(new Color(52, 152, 219));
                mediaVendasPanel.add(lblMediaVendasTitulo, BorderLayout.NORTH);
                
                ElegantTextField txtMediaVendas = new ElegantTextField(15);
                txtMediaVendas.setEditable(false);
                txtMediaVendas.setText("R$ 0,00");
                txtMediaVendas.setFont(new Font("Segoe UI", Font.BOLD, 16));
                txtMediaVendas.setHorizontalAlignment(JTextField.CENTER);
                txtMediaVendas.setForeground(new Color(52, 152, 219));
                mediaVendasPanel.add(txtMediaVendas, BorderLayout.CENTER);
                
                metricasAnalisePanel.add(mediaVendasPanel, gbcMetricas);
                
                // Quantidade de Vendas
                gbcMetricas.gridx = 2; gbcMetricas.gridy = 0;
                gbcMetricas.fill = GridBagConstraints.BOTH;
                gbcMetricas.weightx = 0.25; gbcMetricas.weighty = 1.0;
                
                JPanel qtdVendasPanel = new JPanel(new BorderLayout());
                qtdVendasPanel.setBorder(BorderFactory.createLineBorder(new Color(155, 89, 182), 1));
                qtdVendasPanel.setOpaque(false);
                
                JLabel lblQtdVendasTitulo = new JLabel("🛒 Qtd. Vendas", SwingConstants.CENTER);
                lblQtdVendasTitulo.setFont(new Font("Segoe UI", Font.BOLD, 12));
                lblQtdVendasTitulo.setForeground(new Color(155, 89, 182));
                qtdVendasPanel.add(lblQtdVendasTitulo, BorderLayout.NORTH);
                
                ElegantTextField txtQtdVendas = new ElegantTextField(15);
                txtQtdVendas.setEditable(false);
                txtQtdVendas.setText("0");
                txtQtdVendas.setFont(new Font("Segoe UI", Font.BOLD, 16));
                txtQtdVendas.setHorizontalAlignment(JTextField.CENTER);
                txtQtdVendas.setForeground(new Color(155, 89, 182));
                qtdVendasPanel.add(txtQtdVendas, BorderLayout.CENTER);
                
                metricasAnalisePanel.add(qtdVendasPanel, gbcMetricas);
                
                // Taxa de Crescimento
                gbcMetricas.gridx = 3; gbcMetricas.gridy = 0;
                gbcMetricas.fill = GridBagConstraints.BOTH;
                gbcMetricas.weightx = 0.25; gbcMetricas.weighty = 1.0;
                
                JPanel taxaCrescimentoPanel = new JPanel(new BorderLayout());
                taxaCrescimentoPanel.setBorder(BorderFactory.createLineBorder(new Color(230, 126, 34), 1));
                taxaCrescimentoPanel.setOpaque(false);
                
                JLabel lblTaxaCrescimentoTitulo = new JLabel("📊 Crescimento", SwingConstants.CENTER);
                lblTaxaCrescimentoTitulo.setFont(new Font("Segoe UI", Font.BOLD, 12));
                lblTaxaCrescimentoTitulo.setForeground(new Color(230, 126, 34));
                taxaCrescimentoPanel.add(lblTaxaCrescimentoTitulo, BorderLayout.NORTH);
                
                ElegantTextField txtTaxaCrescimento = new ElegantTextField(15);
                txtTaxaCrescimento.setEditable(false);
                txtTaxaCrescimento.setText("0,0%");
                txtTaxaCrescimento.setFont(new Font("Segoe UI", Font.BOLD, 16));
                txtTaxaCrescimento.setHorizontalAlignment(JTextField.CENTER);
                txtTaxaCrescimento.setForeground(new Color(230, 126, 34));
                taxaCrescimentoPanel.add(txtTaxaCrescimento, BorderLayout.CENTER);
                
                metricasAnalisePanel.add(taxaCrescimentoPanel, gbcMetricas);
                
                // Adicionar painel de métricas
                gbc.gridx = 0; gbc.gridy = 1; gbc.gridwidth = 2;
                gbc.fill = GridBagConstraints.HORIZONTAL;
                gbc.weightx = 1.0; gbc.weighty = 0;
                gbc.insets = new Insets(5, 5, 10, 5);
                panel.add(metricasAnalisePanel, gbc);
                
                // Tabela de Análise de Vendas
                gbc.gridx = 0; gbc.gridy = 2; gbc.gridwidth = 2;
                gbc.fill = GridBagConstraints.BOTH;
                gbc.weightx = 1.0; gbc.weighty = 1.0;
                gbc.insets = new Insets(5, 5, 5, 5);
                
                String[] colunasAnalise = {"Data", "Produto", "Categoria", "Cliente", "Vendedor", "Quantidade", "Valor Unitário", "Valor Total", "Comissão", "Margem %", "Região"};
                Object[][] dadosAnalise = {
                    {"08/05/2026", "Notebook Dell", "Informática", "Tech Solutions Ltda", "João Silva", 2, "R$ 3.500,00", "R$ 7.000,00", "R$ 350,00", "25,0%", "São Paulo"},
                    {"08/05/2026", "Monitor LG 24\"", "Eletrônicos", "Digital Corp", "Maria Santos", 3, "R$ 890,00", "R$ 2.670,00", "R$ 160,20", "18,0%", "Rio de Janeiro"},
                    {"08/05/2026", "Mouse Wireless", "Periféricos", "Startup Tech", "Pedro Oliveira", 5, "R$ 150,00", "R$ 750,00", "R$ 37,50", "20,0%", "Minas Gerais"},
                    {"07/05/2026", "Teclado Mecânico", "Periféricos", "Gaming Store", "Ana Costa", 4, "R$ 320,00", "R$ 1.280,00", "R$ 76,80", "22,0%", "Online"},
                    {"07/05/2026", "Webcam HD", "Acessórios", "Home Office", "Carlos Ferreira", 6, "R$ 180,00", "R$ 1.080,00", "R$ 64,80", "21,0%", "Brasília"}
                };
                
                JTable tabelaAnalise = new JTable(dadosAnalise, colunasAnalise);
                tabelaAnalise.setFont(new Font("Segoe UI", Font.PLAIN, 11));
                tabelaAnalise.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 12));
                tabelaAnalise.getTableHeader().setBackground(new Color(52, 152, 219));
                tabelaAnalise.getTableHeader().setForeground(WHITE);
                tabelaAnalise.setRowHeight(25);
                tabelaAnalise.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
                tabelaAnalise.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
                
                JScrollPane scrollPaneAnalise = new JScrollPane(tabelaAnalise);
                scrollPaneAnalise.setPreferredSize(new Dimension(1200, 300));
                scrollPaneAnalise.setBorder(BorderFactory.createTitledBorder(
                    BorderFactory.createEtchedBorder(), "📋 Análise Detalhada de Vendas",
                    TitledBorder.LEFT, TitledBorder.TOP, new Font("Segoe UI", Font.BOLD, 12),
                    DARK_GRAY));
                panel.add(scrollPaneAnalise, gbc);
                
                // Painel de Ações da Análise
                gbc.gridx = 0; gbc.gridy = 3; gbc.gridwidth = 2;
                gbc.fill = GridBagConstraints.HORIZONTAL;
                gbc.weightx = 1.0; gbc.weighty = 0;
                gbc.insets = new Insets(10, 5, 5, 5);
                
                JPanel acoesAnalisePanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 5));
                acoesAnalisePanel.setOpaque(false);
                
                ElegantButton btnGerarAnalise = new ElegantButton("📊 Gerar Análise", SUCCESS_COLOR, false);
                btnGerarAnalise.setForeground(WHITE);
                btnGerarAnalise.setFont(new Font("Segoe UI", Font.BOLD, 14));
                btnGerarAnalise.setPreferredSize(new Dimension(140, 40));
                btnGerarAnalise.setToolTipText("Gerar análise de vendas");
                acoesAnalisePanel.add(btnGerarAnalise);
                
                ElegantButton btnExportarExcelAnalise = new ElegantButton("📊 Excel", new Color(0, 120, 215), false);
                btnExportarExcelAnalise.setForeground(WHITE);
                btnExportarExcelAnalise.setFont(new Font("Segoe UI", Font.BOLD, 14));
                btnExportarExcelAnalise.setPreferredSize(new Dimension(100, 40));
                btnExportarExcelAnalise.setToolTipText("Exportar para Excel");
                acoesAnalisePanel.add(btnExportarExcelAnalise);
                
                ElegantButton btnExportarPDFAnalise = new ElegantButton("📄 PDF", new Color(200, 50, 50), false);
                btnExportarPDFAnalise.setForeground(WHITE);
                btnExportarPDFAnalise.setFont(new Font("Segoe UI", Font.BOLD, 14));
                btnExportarPDFAnalise.setPreferredSize(new Dimension(80, 40));
                btnExportarPDFAnalise.setToolTipText("Exportar para PDF");
                acoesAnalisePanel.add(btnExportarPDFAnalise);
                
                ElegantButton btnImprimirAnalise = new ElegantButton("🖨️ Imprimir", new Color(100, 100, 100), false);
                btnImprimirAnalise.setForeground(WHITE);
                btnImprimirAnalise.setFont(new Font("Segoe UI", Font.BOLD, 14));
                btnImprimirAnalise.setPreferredSize(new Dimension(100, 40));
                btnImprimirAnalise.setToolTipText("Imprimir análise");
                acoesAnalisePanel.add(btnImprimirAnalise);
                
                ElegantButton btnGraficos = new ElegantButton("📈 Gráficos", new Color(155, 89, 182), false);
                btnGraficos.setForeground(WHITE);
                btnGraficos.setFont(new Font("Segoe UI", Font.BOLD, 14));
                btnGraficos.setPreferredSize(new Dimension(100, 40));
                btnGraficos.setToolTipText("Visualizar gráficos");
                acoesAnalisePanel.add(btnGraficos);
                
                ElegantButton btnComparar = new ElegantButton("⚖️ Comparar", WARNING_COLOR, false);
                btnComparar.setForeground(WHITE);
                btnComparar.setFont(new Font("Segoe UI", Font.BOLD, 14));
                btnComparar.setPreferredSize(new Dimension(100, 40));
                btnComparar.setToolTipText("Comparar períodos");
                acoesAnalisePanel.add(btnComparar);
                
                panel.add(acoesAnalisePanel, gbc);
                break;
                
            default:
                return criarFormularioPadraoElegante(item, "RELATORIOS", gbc);
        }
        
        return panel;
    }
    
    /**
     * Formulários Configurações elegantes
     */
    private JPanel criarFormularioConfiguracoesElegante(String item, GridBagConstraints gbc) {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setOpaque(false);
        
        switch (item) {
            case "👤 Usuários e Permissões":
                // Nome do Usuário
                gbc.gridx = 0; gbc.gridy = 0;
                JLabel lblNome = new JLabel("Nome:");
                lblNome.setFont(new Font("Segoe UI", Font.BOLD, 12));
                lblNome.setForeground(DARK_GRAY);
                panel.add(lblNome, gbc);
                gbc.gridx = 1; gbc.gridy = 0;
                ElegantTextField txtNome = new ElegantTextField(30);
                panel.add(txtNome, gbc);
                
                // Login
                gbc.gridx = 0; gbc.gridy = 1;
                JLabel lblLogin = new JLabel("Login:");
                lblLogin.setFont(new Font("Segoe UI", Font.BOLD, 12));
                lblLogin.setForeground(DARK_GRAY);
                panel.add(lblLogin, gbc);
                gbc.gridx = 1; gbc.gridy = 1;
                ElegantTextField txtLogin = new ElegantTextField(20);
                panel.add(txtLogin, gbc);
                
                // Senha
                gbc.gridx = 0; gbc.gridy = 2;
                JLabel lblSenha = new JLabel("Senha:");
                lblSenha.setFont(new Font("Segoe UI", Font.BOLD, 12));
                lblSenha.setForeground(DARK_GRAY);
                panel.add(lblSenha, gbc);
                gbc.gridx = 1; gbc.gridy = 2;
                JPasswordField txtSenha = new JPasswordField(20);
                txtSenha.setFont(new Font("Segoe UI", Font.PLAIN, 12));
                panel.add(txtSenha, gbc);
                
                // Perfil
                gbc.gridx = 0; gbc.gridy = 3;
                JLabel lblPerfil = new JLabel("Perfil:");
                lblPerfil.setFont(new Font("Segoe UI", Font.BOLD, 12));
                lblPerfil.setForeground(DARK_GRAY);
                panel.add(lblPerfil, gbc);
                gbc.gridx = 1; gbc.gridy = 3;
                JComboBox<String> cbPerfil = new JComboBox<>(new String[]{"Administrador", "Gerente", "Operador", "Visualizador"});
                cbPerfil.setFont(new Font("Segoe UI", Font.PLAIN, 12));
                panel.add(cbPerfil, gbc);
                
                // Status
                gbc.gridx = 0; gbc.gridy = 4;
                JLabel lblStatus = new JLabel("Status:");
                lblStatus.setFont(new Font("Segoe UI", Font.BOLD, 12));
                lblStatus.setForeground(DARK_GRAY);
                panel.add(lblStatus, gbc);
                gbc.gridx = 1; gbc.gridy = 4;
                JComboBox<String> cbStatus = new JComboBox<>(new String[]{"Ativo", "Inativo"});
                cbStatus.setFont(new Font("Segoe UI", Font.PLAIN, 12));
                panel.add(cbStatus, gbc);
                break;
                
            default:
                return criarFormularioPadraoElegante(item, "CONFIGURACOES", gbc);
        }
        
        return panel;
    }
    
    /**
     * Formulário padrão elegante
     */
    private JPanel criarFormularioPadraoElegante(String item, String module, GridBagConstraints gbc) {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setOpaque(false);
        
        // Informações básicas
        gbc.gridx = 0; gbc.gridy = 0;
        JLabel lblModulo = new JLabel("Módulo:");
        lblModulo.setFont(new Font("Segoe UI", Font.BOLD, 12));
        lblModulo.setForeground(DARK_GRAY);
        panel.add(lblModulo, gbc);
        gbc.gridx = 1; gbc.gridy = 0;
        ElegantTextField txtModulo = new ElegantTextField(20);
        txtModulo.setEditable(false);
        panel.add(txtModulo, gbc);
        
        gbc.gridx = 0; gbc.gridy = 1;
        JLabel lblFuncionalidade = new JLabel("Funcionalidade:");
        lblFuncionalidade.setFont(new Font("Segoe UI", Font.BOLD, 12));
        lblFuncionalidade.setForeground(DARK_GRAY);
        panel.add(lblFuncionalidade, gbc);
        gbc.gridx = 1; gbc.gridy = 1;
        ElegantTextField txtFuncionalidade = new ElegantTextField(30);
        txtFuncionalidade.setEditable(false);
        panel.add(txtFuncionalidade, gbc);
        
        gbc.gridx = 0; gbc.gridy = 2;
        JLabel lblUsuario = new JLabel("Usuário:");
        lblUsuario.setFont(new Font("Segoe UI", Font.BOLD, 12));
        lblUsuario.setForeground(DARK_GRAY);
        panel.add(lblUsuario, gbc);
        gbc.gridx = 1; gbc.gridy = 2;
        ElegantTextField txtUsuario = new ElegantTextField(20);
        txtUsuario.setEditable(false);
        panel.add(txtUsuario, gbc);
        
        gbc.gridx = 0; gbc.gridy = 3;
        JLabel lblData = new JLabel("Data:");
        lblData.setFont(new Font("Segoe UI", Font.BOLD, 12));
        lblData.setForeground(DARK_GRAY);
        panel.add(lblData, gbc);
        gbc.gridx = 1; gbc.gridy = 3;
        ElegantTextField txtData = new ElegantTextField(20);
        txtData.setEditable(false);
        panel.add(txtData, gbc);
        
        return panel;
    }
    
    /**
     * Cria painel de botões de ação elegante
     */
    private JPanel criarPainelBotoesAcaoElegante(String item, String module) {
        JPanel botoesPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        botoesPanel.setOpaque(false);
        botoesPanel.setBorder(BorderFactory.createEmptyBorder(15, 0, 5, 0));
        
        // Determinar cor do módulo
        @SuppressWarnings("unused")
        Color moduleColor = getModuleColor(item);
        
        // Botão Novo - Gradiente Azul Vibrante
        ElegantButton btnNovo = new ElegantButton("➕ Novo", new Color(41, 128, 185), false);
        btnNovo.setForeground(WHITE);
        btnNovo.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btnNovo.setPreferredSize(new Dimension(80, 35));
        btnNovo.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(52, 152, 219), 1),
            BorderFactory.createEmptyBorder(5, 15, 5, 15)
        ));
        btnNovo.addActionListener(e -> {
            try {
                SystemLogger.operation("BOTAO_NOVO_CLICK", module, 
                    "Usuário: " + usuarioAtual + " clicou em Novo para: " + item);
                JOptionPane.showMessageDialog(workArea.getParent(), 
                    "🆕 Funcionalidade 'Novo' para " + item + "\n\nEm desenvolvimento...", 
                    "Novo " + item, JOptionPane.INFORMATION_MESSAGE);
            } catch (Exception ex) {
                SystemLogger.error("Erro ao clicar em Novo", ex);
            }
        });
        
        // Botão Editar - Gradiente Laranja Vibrante
        ElegantButton btnEditar = new ElegantButton("✏️ Editar", new Color(230, 126, 34), false);
        btnEditar.setForeground(WHITE);
        btnEditar.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btnEditar.setPreferredSize(new Dimension(80, 35));
        btnEditar.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(241, 196, 15), 1),
            BorderFactory.createEmptyBorder(5, 15, 5, 15)
        ));
        btnEditar.addActionListener(e -> {
            try {
                SystemLogger.operation("BOTAO_EDITAR_CLICK", module, 
                    "Usuário: " + usuarioAtual + " clicou em Editar para: " + item);
                JOptionPane.showMessageDialog(workArea.getParent(), 
                    "✏️ Funcionalidade 'Editar' para " + item + "\n\nEm desenvolvimento...", 
                    "Editar " + item, JOptionPane.INFORMATION_MESSAGE);
            } catch (Exception ex) {
                SystemLogger.error("Erro ao clicar em Editar", ex);
            }
        });
        
        // Botão Excluir - Gradiente Vermelho Vibrante
        ElegantButton btnExcluir = new ElegantButton("🗑️ Excluir", new Color(231, 76, 60), false);
        btnExcluir.setForeground(WHITE);
        btnExcluir.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btnExcluir.setPreferredSize(new Dimension(80, 35));
        btnExcluir.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(192, 57, 43), 1),
            BorderFactory.createEmptyBorder(5, 15, 5, 15)
        ));
        btnExcluir.addActionListener(e -> {
            try {
                SystemLogger.operation("BOTAO_EXCLUIR_CLICK", module, 
                    "Usuário: " + usuarioAtual + " clicou em Excluir para: " + item);
                int result = JOptionPane.showConfirmDialog(workArea.getParent(), 
                    "🗑️ Confirmar exclusão do item selecionado em " + item + "?", 
                    "Excluir " + item, JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
                if (result == JOptionPane.YES_OPTION) {
                    JOptionPane.showMessageDialog(workArea.getParent(), 
                        "✅ Item excluído com sucesso!", 
                        "Sucesso", JOptionPane.INFORMATION_MESSAGE);
                    SystemLogger.operation("ITEM_EXCLUIDO", module, 
                        "Usuário: " + usuarioAtual + " excluiu item em: " + item);
                }
            } catch (Exception ex) {
                SystemLogger.error("Erro ao clicar em Excluir", ex);
            }
        });
        
        // Botão Salvar - Gradiente Verde Vibrante
        ElegantButton btnSalvar = new ElegantButton("💾 Salvar", new Color(39, 174, 96), false);
        btnSalvar.setForeground(WHITE);
        btnSalvar.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btnSalvar.setPreferredSize(new Dimension(80, 35));
        btnSalvar.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(46, 204, 113), 1),
            BorderFactory.createEmptyBorder(5, 15, 5, 15)
        ));
        btnSalvar.addActionListener(e -> {
            try {
                SystemLogger.operation("BOTAO_SALVAR_CLICK", module, 
                    "Usuário: " + usuarioAtual + " clicou em Salvar para: " + item);
                JOptionPane.showMessageDialog(workArea.getParent(), 
                    "💾 Dados salvos com sucesso em " + item + "!", 
                    "Salvar " + item, JOptionPane.INFORMATION_MESSAGE);
                SystemLogger.operation("DADOS_SALVOS", module, 
                    "Usuário: " + usuarioAtual + " salvou dados em: " + item);
            } catch (Exception ex) {
                SystemLogger.error("Erro ao clicar em Salvar", ex);
                JOptionPane.showMessageDialog(workArea.getParent(), 
                    "❌ Erro ao salvar dados em " + item + "!", 
                    "Erro", JOptionPane.ERROR_MESSAGE);
            }
        });
        
        // Botão Cancelar - Gradiente Cinza Elegante
        ElegantButton btnCancelar = new ElegantButton("❌ Cancelar", new Color(127, 140, 141), false);
        btnCancelar.setForeground(WHITE);
        btnCancelar.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btnCancelar.setPreferredSize(new Dimension(90, 35));
        btnCancelar.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(149, 165, 166), 1),
            BorderFactory.createEmptyBorder(5, 15, 5, 15)
        ));
        btnCancelar.addActionListener(e -> {
            try {
                SystemLogger.operation("BOTAO_CANCELAR_CLICK", module, 
                    "Usuário: " + usuarioAtual + " clicou em Cancelar para: " + item);
                int result = JOptionPane.showConfirmDialog(workArea.getParent(), 
                    "❌ Deseja cancelar as alterações em " + item + "?", 
                    "Cancelar " + item, JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                if (result == JOptionPane.YES_OPTION) {
                    JOptionPane.showMessageDialog(workArea.getParent(), 
                        "🔄 Alterações canceladas!", 
                        "Cancelado", JOptionPane.INFORMATION_MESSAGE);
                    SystemLogger.operation("ALTERACOES_CANCELADAS", module, 
                        "Usuário: " + usuarioAtual + " cancelou alterações em: " + item);
                }
            } catch (Exception ex) {
                SystemLogger.error("Erro ao clicar em Cancelar", ex);
            }
        });
        
        // Adicionar botões ao painel
        botoesPanel.add(btnNovo);
        botoesPanel.add(Box.createHorizontalStrut(5));
        botoesPanel.add(btnEditar);
        botoesPanel.add(Box.createHorizontalStrut(5));
        botoesPanel.add(btnExcluir);
        botoesPanel.add(Box.createHorizontalStrut(5));
        botoesPanel.add(btnSalvar);
        botoesPanel.add(Box.createHorizontalStrut(5));
        botoesPanel.add(btnCancelar);
        
        return botoesPanel;
    }
    
    /**
     * Cria painel de tabela elegante
     */
    private JPanel criarPainelTabelaElegante(String item, String module) {
        // Determinar cor do módulo para a tabela
        Color moduleColor = getModuleColor(item);
        Color moduleColorLight = new Color(
            Math.min(255, moduleColor.getRed() + 40),
            Math.min(255, moduleColor.getGreen() + 40),
            Math.min(255, moduleColor.getBlue() + 40)
        );
        
        JPanel tabelaPanel = new JPanel(new BorderLayout());
        tabelaPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createEmptyBorder(10, 10, 10, 10),
            BorderFactory.createLineBorder(MEDIUM_GRAY, 1)
        ));
        tabelaPanel.setOpaque(true);
        tabelaPanel.setBackground(WHITE);
        
        // Título elegante
        JLabel tabelaLabel = new JLabel("📊 Resultados da Consulta");
        tabelaLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
        tabelaLabel.setForeground(WARNING_COLOR);
        tabelaLabel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        // Título elegante
        tabelaPanel.add(tabelaLabel, BorderLayout.NORTH);
        
        // Tabela elegante - Dados reais do PostgreSQL
        DefaultTableModel model = carregarDadosTabela(item, module);
        JTable tabela = new JTable(model) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Desabilitar edição temporariamente
            }
        };
        
        // Configuração básica para garantir visibilidade
        tabela.setRowHeight(25);
        tabela.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tabela.getTableHeader().setReorderingAllowed(false);
        tabela.getTableHeader().setResizingAllowed(true);
        tabela.setOpaque(true);
        tabela.setBackground(WHITE);
        tabela.setVisible(true);
        
        // Fonte simples e visível
        tabela.setFont(new Font("Arial", Font.PLAIN, 12));
        tabela.getTableHeader().setFont(new Font("Arial", Font.BOLD, 12));
        
        // Grid lines visíveis
        tabela.setShowHorizontalLines(true);
        tabela.setShowVerticalLines(true);
        tabela.setGridColor(Color.GRAY);
        
        // Header simples
        JTableHeader header = tabela.getTableHeader();
        header.setBackground(new Color(70, 130, 180));
        header.setForeground(WHITE);
        header.setOpaque(true);
        
        // Renderer simples sem customização complexa
        tabela.setDefaultRenderer(Object.class, new javax.swing.table.DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                if (c instanceof JLabel) {
                    JLabel label = (JLabel) c;
                    label.setOpaque(true);
                    if (isSelected) {
                        label.setBackground(new Color(70, 130, 180));
                        label.setForeground(WHITE);
                    } else {
                        label.setBackground(row % 2 == 0 ? WHITE : new Color(240, 240, 240));
                        label.setForeground(Color.BLACK);
                    }
                }
                return c;
            }
        });
        
        JScrollPane scrollPane = new JScrollPane(tabela);
        scrollPane.setPreferredSize(new Dimension(800, 300));
        scrollPane.setBorder(BorderFactory.createLineBorder(MEDIUM_GRAY, 2));
        scrollPane.setOpaque(true);
        scrollPane.getViewport().setOpaque(true);
        scrollPane.setVisible(true);
        
        // Forçar background branco no viewport
        scrollPane.getViewport().setBackground(WHITE);
        
        // Log para debug
        SystemLogger.ui("Tabela criada com " + model.getRowCount() + " linhas e " + model.getColumnCount() + " colunas");
        SystemLogger.ui("ScrollPane dimensions: " + scrollPane.getPreferredSize());
        SystemLogger.ui("Tabela dimensions: " + tabela.getPreferredSize());
        SystemLogger.ui("Tabela visible: " + tabela.isVisible());
        SystemLogger.ui("ScrollPane visible: " + scrollPane.isVisible());
        
        tabelaPanel.add(scrollPane, BorderLayout.CENTER);
        tabelaPanel.setVisible(true);
        
        return tabelaPanel;
    }
    
    /**
     * Configura tabela elegante
     */
    private void configurarTabelaElegante(JTable tabela) {
        tabela.setRowHeight(30);
        tabela.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tabela.getTableHeader().setReorderingAllowed(false);
        tabela.getTableHeader().setResizingAllowed(true);
        tabela.setOpaque(true);
        tabela.setBackground(WHITE);
        tabela.setVisible(true);
        ((JComponent) tabela.getDefaultRenderer(Object.class)).setOpaque(true);
        
        // Habilitar edição na tabela
        tabela.setEnabled(true);
        tabela.setModel(new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int column) {
                // Permitir edição em todas as células
                return true;
            }
            
            @Override
            public void setValueAt(Object aValue, int row, int column) {
                // Log de edição na tabela
                SystemLogger.ui("Tabela - Editando célula [" + row + "," + column + "]: " + aValue);
                super.setValueAt(aValue, row, column);
            }
            
            @Override
            public Object getValueAt(int row, int column) {
                // Log de leitura na tabela
                Object value = super.getValueAt(row, column);
                SystemLogger.ui("Tabela - Lendo célula [" + row + "," + column + "]: " + value);
                return value;
            }
            
            @Override
            public void fireTableChanged(javax.swing.event.TableModelEvent e) {
                // Log de mudanças na tabela mais detalhado
                String eventType = switch (e.getType()) {
                    case 0 -> "UPDATE";
                    case 1 -> "INSERT";
                    case 2 -> "DELETE";
                    case -1 -> "HEADER_CHANGE";
                    default -> "UNKNOWN";
                };
                SystemLogger.ui("Tabela - Evento: " + eventType + " | Linhas: " + e.getFirstRow() + "-" + e.getLastRow() + " | Coluna: " + e.getColumn());
                super.fireTableChanged(e);
            }
            
            @Override
            public void fireTableCellUpdated(int row, int column) {
                // Log de atualização de célula específica
                SystemLogger.ui("Tabela - Célula atualizada [" + row + "," + column + "]");
                super.fireTableCellUpdated(row, column);
            }
            
            @Override
            public void fireTableRowsInserted(int firstRow, int lastRow) {
                // Log de inserção de linhas
                SystemLogger.ui("Tabela - Linhas inseridas [" + firstRow + " a " + lastRow + "]");
                super.fireTableRowsInserted(firstRow, lastRow);
            }
            
            @Override
            public void fireTableRowsDeleted(int firstRow, int lastRow) {
                // Log de exclusão de linhas
                SystemLogger.ui("Tabela - Linhas excluídas [" + firstRow + " a " + lastRow + "]");
                super.fireTableRowsDeleted(firstRow, lastRow);
            }
        });
        
        // Header elegante com cor dinâmica
        JTableHeader header = tabela.getTableHeader();
        header.setFont(new Font("Segoe UI", Font.BOLD, 13));
        
        // Obter cor do módulo atual (usar a cor da tabela como referência)
        Color headerColor = new Color(52, 152, 219); // Azul vibrante padrão
        if (tabela.getParent() != null && tabela.getParent().getParent() instanceof JPanel) {
            // Tentar obter cor do painel pai
            JPanel parentPanel = (JPanel) tabela.getParent().getParent();
            if (parentPanel.getBackground() != null) {
                headerColor = parentPanel.getBackground().darker();
            }
        }
        
        header.setBackground(headerColor);
        header.setForeground(WHITE);
        header.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        header.setOpaque(true);
        
        // Células elegantes
        tabela.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        tabela.setGridColor(new Color(230, 230, 230));
        tabela.setShowHorizontalLines(true);
        tabela.setShowVerticalLines(true);
        
        // Simplificar renderização para garantir visibilidade
        final Color evenRowColor = new Color(248, 249, 250); // Azul muito claro
        final Color oddRowColor = new Color(233, 236, 239); // Azul cinza claro
        final Color selectionColor = headerColor != null ? headerColor : PRIMARY_COLOR;
        
        tabela.setDefaultRenderer(Object.class, new javax.swing.table.DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                
                if (!isSelected) {
                    if (row % 2 == 0) {
                        c.setBackground(evenRowColor);
                    } else {
                        c.setBackground(oddRowColor);
                    }
                } else {
                    c.setBackground(selectionColor);
                }
                
                // Forçar visibilidade do texto
                if (c instanceof JLabel) {
                    ((JLabel) c).setForeground(BLACK);
                }
                
                return c;
            }
        });
    }
    
    /**
     * Cria painel de botões elegante
     */
    private JPanel criarPainelBotoesElegante(String item) {
        JPanel botoesPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 12, 12));
        botoesPanel.setOpaque(false);
        botoesPanel.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));
        
        ElegantButton btnNovo = new ElegantButton("➕ Novo", SUCCESS_COLOR, false);
        btnNovo.setForeground(WHITE);
        btnNovo.addActionListener(e -> {
            SystemLogger.audit("OPERATION: BOTAO_NOVO_CLICK | MODULE: " + getModuleName(item) + " | DETAILS: Usuário: " + usuarioAtual + " clicou em Novo para: " + item);
            adicionarNovoItem(item);
        });
        
        ElegantButton btnEditar = new ElegantButton("📝 Editar", PRIMARY_COLOR, false);
        btnEditar.setForeground(WHITE);
        btnEditar.addActionListener(e -> {
            SystemLogger.audit("OPERATION: BOTAO_EDITAR_CLICK | MODULE: " + getModuleName(item) + " | DETAILS: Usuário: " + usuarioAtual + " clicou em Editar para: " + item);
            editarItemSelecionado(item);
        });
        
        ElegantButton btnExcluir = new ElegantButton("🗑️ Excluir", ACCENT_COLOR, false);
        btnExcluir.setForeground(WHITE);
        btnExcluir.addActionListener(e -> {
            SystemLogger.audit("OPERATION: BOTAO_EXCLUIR_CLICK | MODULE: " + getModuleName(item) + " | DETAILS: Usuário: " + usuarioAtual + " clicou em Excluir para: " + item);
            excluirItemSelecionado(item);
        });
        
        ElegantButton btnSalvar = new ElegantButton("💾 Salvar", SUCCESS_COLOR, false);
        btnSalvar.setForeground(WHITE);
        btnSalvar.addActionListener(e -> {
            SystemLogger.audit("OPERATION: BOTAO_SALVAR_CLICK | MODULE: " + getModuleName(item) + " | DETAILS: Usuário: " + usuarioAtual + " clicou em Salvar para: " + item);
            salvarDadosFormulario(item);
        });
        
        ElegantButton btnVoltar = new ElegantButton("🔙 Voltar", DARK_GRAY, false);
        btnVoltar.setForeground(WHITE);
        btnVoltar.addActionListener(e -> voltarTelaInicial());
        
        botoesPanel.add(btnNovo);
        botoesPanel.add(btnEditar);
        botoesPanel.add(btnExcluir);
        botoesPanel.add(btnSalvar);
        botoesPanel.add(btnVoltar);
        
        return botoesPanel;
    }
    
    /**
     * Cria status bar elegante
     */
    private JPanel criarStatusBarTelaElegante(String item) {
        JPanel statusBar = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                // Gradiente elegante
                GradientPaint gradient = new GradientPaint(0, 0, MEDIUM_GRAY, 
                                                        0, getHeight(), DARK_GRAY);
                g2d.setPaint(gradient);
                g2d.fillRect(0, 0, getWidth(), getHeight());
                g2d.dispose();
            }
        };
        statusBar.setBorder(BorderFactory.createEmptyBorder(8, 15, 8, 15));
        statusBar.setPreferredSize(new Dimension(0, 35));
        statusBar.setOpaque(false);
        
        JLabel statusLabel = new JLabel("🟢 Sistema Online | Tela: " + item + " | Usuário: " + usuarioAtual + " | " + 
            java.time.LocalDateTime.now().format(java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss")) + 
            " | 🗄️ Banco: PostgreSQL");
        statusBar.add(statusLabel);
        
        return statusBar;
    }
    
    /**
     * Adiciona novo item à tabela
     */
    private void adicionarNovoItem(String item) {
        try {
            SystemLogger.ui("➕ INÍCIO - Adicionando novo item para: " + item);
            
            // Obter a tabela atual da tela ativa
            Component[] components = workArea.getComponents();
            JTable tabelaAtual = null;
            
            for (Component comp : components) {
                if (comp instanceof JPanel) {
                    JPanel panel = (JPanel) comp;
                    encontrarTabelaNoPainel(panel, item);
                }
            }
            
            // Simulação de adição - em produção buscaria dados do banco
            SystemLogger.ui("➕ Mostrando diálogo de confirmação para: " + item);
            
            // Mostrar diálogo de confirmação com parent correto
            int result = JOptionPane.showConfirmDialog(null, 
                "Deseja adicionar um novo registro em " + item + "?",
                "Novo Registro", 
                JOptionPane.YES_NO_OPTION);
            
            SystemLogger.ui("➕ Resultado do diálogo: " + result + " (YES=" + JOptionPane.YES_OPTION + ")");
            
            if (result == JOptionPane.YES_OPTION) {
                SystemLogger.audit("ITEM_ADICIONADO | MODULE: " + getModuleName(item) + " | DETAILS: Usuário: " + usuarioAtual + " adicionou item em: " + item);
                
                // Mostrar mensagem de sucesso
                SystemLogger.ui("➕ Mostrando mensagem de sucesso para: " + item);
                JOptionPane.showMessageDialog(null, 
                    "✅ Novo item adicionado com sucesso em " + item + "!",
                    "Sucesso", JOptionPane.INFORMATION_MESSAGE);
                
                SystemLogger.ui("➕ Recarregando tela para: " + item);
                // Recarregar a tela para atualizar a tabela
                criarTelaCompletaElegante(item, getModuleName(item));
                SystemLogger.ui("➕ Tela recarregada com sucesso para: " + item);
            } else {
                SystemLogger.ui("➕ Usuário cancelou a operação para: " + item);
            }
        } catch (Exception e) {
            SystemLogger.error("❌ Erro ao adicionar item: " + e.getMessage());
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, 
                "❌ Erro ao adicionar item: " + e.getMessage(),
                "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    /**
     * Edita item selecionado na tabela
     */
    private void editarItemSelecionado(String item) {
        try {
            SystemLogger.ui("📝 INÍCIO - Editando item selecionado para: " + item);
            
            // Simulação de edição - em produção buscaria dados do banco
            SystemLogger.ui("📝 Mostrando diálogo de confirmação para editar: " + item);
            int result = JOptionPane.showConfirmDialog(null, 
                "Deseja editar o registro selecionado em " + item + "?",
                "Editar Registro", 
                JOptionPane.YES_NO_OPTION);
            
            SystemLogger.ui("📝 Resultado do diálogo de edição: " + result + " (YES=" + JOptionPane.YES_OPTION + ")");
            
            if (result == JOptionPane.YES_OPTION) {
                SystemLogger.audit("ITEM_EDITADO | MODULE: " + getModuleName(item) + " | DETAILS: Usuário: " + usuarioAtual + " editou item em: " + item);
                
                SystemLogger.ui("📝 Mostrando mensagem de sucesso para edição: " + item);
                JOptionPane.showMessageDialog(null, 
                    "✅ Item editado com sucesso em " + item + "!",
                    "Sucesso", JOptionPane.INFORMATION_MESSAGE);
            } else {
                SystemLogger.ui("📝 Usuário cancelou a edição para: " + item);
            }
        } catch (Exception e) {
            SystemLogger.error("❌ Erro ao editar item: " + e.getMessage());
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, 
                "❌ Erro ao editar item: " + e.getMessage(),
                "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    /**
     * Exclui item selecionado na tabela
     */
    private void excluirItemSelecionado(String item) {
        try {
            SystemLogger.ui("🗑️ INÍCIO - Excluindo item selecionado para: " + item);
            
            // Simulação de exclusão - em produção buscaria dados do banco
            SystemLogger.ui("🗑️ Mostrando diálogo de confirmação para excluir: " + item);
            int result = JOptionPane.showConfirmDialog(null, 
                "⚠️ Tem certeza que deseja excluir o registro selecionado em " + item + "?",
                "Excluir Registro", 
                JOptionPane.YES_NO_OPTION);
            
            SystemLogger.ui("🗑️ Resultado do diálogo de exclusão: " + result + " (YES=" + JOptionPane.YES_OPTION + ")");
            
            if (result == JOptionPane.YES_OPTION) {
                SystemLogger.audit("ITEM_EXCLUIDO | MODULE: " + getModuleName(item) + " | DETAILS: Usuário: " + usuarioAtual + " excluiu item em: " + item);
                
                SystemLogger.ui("🗑️ Mostrando mensagem de sucesso para exclusão: " + item);
                JOptionPane.showMessageDialog(null, 
                    "✅ Item excluído com sucesso em " + item + "!",
                    "Sucesso", JOptionPane.INFORMATION_MESSAGE);
                
                SystemLogger.ui("🗑️ Recarregando tela após exclusão: " + item);
                // Recarregar a tela para atualizar a tabela
                criarTelaCompletaElegante(item, getModuleName(item));
                SystemLogger.ui("🗑️ Tela recarregada com sucesso após exclusão: " + item);
            } else {
                SystemLogger.ui("🗑️ Usuário cancelou a exclusão para: " + item);
            }
        } catch (Exception e) {
            SystemLogger.error("❌ Erro ao excluir item: " + e.getMessage());
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, 
                "❌ Erro ao excluir item: " + e.getMessage(),
                "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    /**
     * Salva dados do formulário
     */
    private void salvarDadosFormulario(String item) {
        try {
            SystemLogger.ui("💾 INÍCIO - Salvando dados do formulário para: " + item);
            
            // Simulação de salvamento - em produção salvaria no banco
            SystemLogger.ui("💾 Mostrando diálogo de confirmação para salvar: " + item);
            int result = JOptionPane.showConfirmDialog(null, 
                "Deseja salvar as alterações em " + item + "?",
                "Salvar Dados", 
                JOptionPane.YES_NO_OPTION);
            
            SystemLogger.ui("💾 Resultado do diálogo de salvamento: " + result + " (YES=" + JOptionPane.YES_OPTION + ")");
            
            if (result == JOptionPane.YES_OPTION) {
                SystemLogger.audit("DADOS_SALVOS | MODULE: " + getModuleName(item) + " | DETAILS: Usuário: " + usuarioAtual + " salvou dados em: " + item);
                
                SystemLogger.ui("💾 Mostrando mensagem de sucesso para salvamento: " + item);
                JOptionPane.showMessageDialog(null, 
                    "✅ Dados salvos com sucesso em " + item + "!",
                    "Sucesso", JOptionPane.INFORMATION_MESSAGE);
            } else {
                SystemLogger.ui("💾 Usuário cancelou o salvamento para: " + item);
            }
        } catch (Exception e) {
            SystemLogger.error("❌ Erro ao salvar dados: " + e.getMessage());
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, 
                "❌ Erro ao salvar dados: " + e.getMessage(),
                "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    /**
     * Encontra tabela em um painel recursivamente
     */
    private void encontrarTabelaNoPainel(JPanel panel, String item) {
        for (Component comp : panel.getComponents()) {
            if (comp instanceof JScrollPane) {
                JScrollPane scrollPane = (JScrollPane) comp;
                Component viewportView = scrollPane.getViewport().getView();
                if (viewportView instanceof JTable) {
                    JTable tabela = (JTable) viewportView;
                    SystemLogger.ui("Tabela encontrada para " + item + ": " + tabela.getRowCount() + " linhas");
                }
            } else if (comp instanceof JPanel) {
                encontrarTabelaNoPainel((JPanel) comp, item);
            }
        }
    }
    
    /**
     * Obtém nome do módulo baseado no item
     */
    private String getModuleName(String item) {
        if (item.contains("Ponto de Venda") || item.contains("Pagamentos") || item.contains("Cupom")) {
            return "PDV";
        } else if (item.contains("Nova Venda") || item.contains("Venda") || item.contains("Orçamento") || item.contains("Entrega")) {
            return "VENDAS";
        } else if (item.contains("Produto") || item.contains("Categoria") || item.contains("Fornecedor") || item.contains("Estoque")) {
            return "PRODUTOS";
        } else if (item.contains("Cliente") || item.contains("Fornecedor")) {
            return "CLIENTES";
        } else if (item.contains("Relatório") || item.contains("Resumo")) {
            return "RELATORIOS";
        } else if (item.contains("Usuário") || item.contains("Permissão") || item.contains("Backup")) {
            return "CONFIG";
        }
        return "UNKNOWN";
    }
    
    /**
     * Carrega dados da tabela do PostgreSQL baseado no módulo
     */
    private DefaultTableModel carregarDadosTabela(String item, String module) {
        SystemLogger.ui("📊 Carregando dados do PostgreSQL para: " + item + " (Módulo: " + module + ")");
        
        try {
            // Importar classes DAO
            if (module.equals("PRODUTOS")) {
                return carregarDadosProdutos();
            } else if (module.equals("VENDAS")) {
                return carregarDadosVendas();
            } else if (module.equals("CLIENTES")) {
                return carregarDadosClientes();
            } else if (module.equals("PDV")) {
                return carregarDadosPDV(item);
            } else {
                // Módulos não implementados - tabela vazia (sem dados simulados)
                DefaultTableModel emptyModel = new DefaultTableModel(
                    new Object[]{"ID", "Descrição", "Status"}, 0);
                SystemLogger.ui("📊 Módulo '" + module + "' não implementado - tabela vazia");
                return emptyModel;
            }
        } catch (Exception e) {
            SystemLogger.error("Erro ao carregar dados do PostgreSQL: " + e.getMessage());
            e.printStackTrace();
            SystemLogger.error("❌ ERRO CRÍTICO: Falha ao carregar dados do PostgreSQL - Nenhum fallback disponível");
            // Retornar tabela vazia em vez de dados simulados
            DefaultTableModel emptyModel = new DefaultTableModel(
                new Object[]{"ID", "Descrição", "Status"}, 0);
            return emptyModel;
        }
    }
    
    /**
     * Carrega dados de produtos do PostgreSQL
     */
    private DefaultTableModel carregarDadosProdutos() {
        try {
            // Importar classes dinamicamente para evitar dependência circular
            Class<?> produtoDAOClass = Class.forName("com.br.hermescomercial.pdv.dao.ProdutoDAO");
            Object produtoDAO = produtoDAOClass.getDeclaredConstructor().newInstance();
            
            // Buscar produtos
            java.lang.reflect.Method buscarTodos = produtoDAOClass.getMethod("buscarTodos");
            java.util.List<?> produtos = (java.util.List<?>) buscarTodos.invoke(produtoDAO);
            
            // Preparar modelo da tabela
            String[] colunas = {"ID", "Nome", "Código Barras", "Preço Venda", "Estoque", "Categoria", "Status"};
            DefaultTableModel model = new DefaultTableModel(colunas, 0);
            
            // Preencher dados
            for (Object produtoObj : produtos) {
                java.lang.reflect.Method getId = produtoObj.getClass().getMethod("getId");
                java.lang.reflect.Method getNome = produtoObj.getClass().getMethod("getNome");
                java.lang.reflect.Method getCodigoBarras = produtoObj.getClass().getMethod("getCodigoBarras");
                java.lang.reflect.Method getPrecoVenda = produtoObj.getClass().getMethod("getPrecoVenda");
                java.lang.reflect.Method getEstoqueAtual = produtoObj.getClass().getMethod("getEstoqueAtual");
                java.lang.reflect.Method getCategoriaNome = produtoObj.getClass().getMethod("getCategoriaNome");
                java.lang.reflect.Method getStatusEstoque = produtoObj.getClass().getMethod("getStatusEstoque");
                
                Object[] row = {
                    getId.invoke(produtoObj),
                    getNome.invoke(produtoObj),
                    getCodigoBarras.invoke(produtoObj),
                    "R$ " + getPrecoVenda.invoke(produtoObj),
                    getEstoqueAtual.invoke(produtoObj),
                    getCategoriaNome.invoke(produtoObj),
                    getStatusEstoque.invoke(produtoObj)
                };
                model.addRow(row);
            }
            
            SystemLogger.ui("📊 Carregados " + produtos.size() + " produtos do PostgreSQL");
            return model;
            
        } catch (Exception e) {
            SystemLogger.error("Erro ao carregar produtos: " + e.getMessage());
            SystemLogger.error("❌ ERRO CRÍTICO: Falha ao carregar dados do PostgreSQL - Nenhum fallback disponível");
            // Retornar tabela vazia em vez de dados simulados
            DefaultTableModel emptyModel = new DefaultTableModel(
                new Object[]{"ID", "Nome", "Código", "Preço", "Estoque", "Categoria", "Status"}, 0);
            return emptyModel;
        }
    }
    
    /**
     * Carrega dados de vendas do PostgreSQL
     */
    private DefaultTableModel carregarDadosVendas() {
        try {
            // Importar classes dinamicamente
            Class<?> vendaDAOClass = Class.forName("com.br.hermescomercial.pdv.dao.VendaDAO");
            Object vendaDAO = vendaDAOClass.getDeclaredConstructor().newInstance();
            
            // Buscar vendas
            java.lang.reflect.Method buscarTodas = vendaDAOClass.getMethod("buscarTodas");
            java.util.List<?> vendas = (java.util.List<?>) buscarTodas.invoke(vendaDAO);
            
            // Preparar modelo da tabela
            String[] colunas = {"ID", "Número", "Cliente", "Valor Total", "Forma Pagto", "Status", "Data"};
            DefaultTableModel model = new DefaultTableModel(colunas, 0);
            
            // Preencher dados
            for (Object vendaObj : vendas) {
                java.lang.reflect.Method getId = vendaObj.getClass().getMethod("getId");
                java.lang.reflect.Method getNumeroVenda = vendaObj.getClass().getMethod("getNumeroVenda");
                java.lang.reflect.Method getClienteNome = vendaObj.getClass().getMethod("getClienteNome");
                java.lang.reflect.Method getValorFinal = vendaObj.getClass().getMethod("getValorFinal");
                java.lang.reflect.Method getFormaPagamento = vendaObj.getClass().getMethod("getFormaPagamento");
                java.lang.reflect.Method getStatus = vendaObj.getClass().getMethod("getStatus");
                java.lang.reflect.Method getDataVenda = vendaObj.getClass().getMethod("getDataVenda");
                
                Object dataVenda = getDataVenda.invoke(vendaObj);
                String dataFormatada = "N/A";
                if (dataVenda != null) {
                    dataFormatada = new java.text.SimpleDateFormat("dd/MM/yyyy HH:mm")
                        .format(new java.util.Date(((java.sql.Timestamp) dataVenda).getTime()));
                }
                
                Object[] row = {
                    getId.invoke(vendaObj),
                    getNumeroVenda.invoke(vendaObj),
                    getClienteNome.invoke(vendaObj),
                    "R$ " + getValorFinal.invoke(vendaObj),
                    getFormaPagamento.invoke(vendaObj),
                    getStatus.invoke(vendaObj),
                    dataFormatada
                };
                model.addRow(row);
            }
            
            SystemLogger.ui("📊 Carregadas " + vendas.size() + " vendas do PostgreSQL");
            return model;
            
        } catch (Exception e) {
            SystemLogger.error("Erro ao carregar vendas: " + e.getMessage());
            SystemLogger.error("❌ ERRO CRÍTICO: Falha ao carregar dados do PostgreSQL - Nenhum fallback disponível");
            // Retornar tabela vazia em vez de dados simulados
            DefaultTableModel emptyModel = new DefaultTableModel(
                new Object[]{"ID", "Número", "Cliente", "Valor", "Pagamento", "Status", "Data"}, 0);
            return emptyModel;
        }
    }
    
    /**
     * Carrega dados de clientes do PostgreSQL
     */
    private DefaultTableModel carregarDadosClientes() {
        try {
            SystemLogger.ui("📊 Carregando dados do PostgreSQL para: Clientes");
            
            // Por enquanto, tabela vazia até DAO de clientes ser implementado
            DefaultTableModel emptyModel = new DefaultTableModel(
                new Object[]{"ID", "Nome", "Email", "Telefone", "Status"}, 0);
            
            SystemLogger.ui("📊 DAO de clientes não implementado - tabela vazia");
            return emptyModel;
            
        } catch (Exception e) {
            SystemLogger.error("Erro ao carregar clientes: " + e.getMessage());
            SystemLogger.error("❌ ERRO CRÍTICO: Falha ao carregar dados do PostgreSQL - Nenhum fallback disponível");
            DefaultTableModel emptyModel = new DefaultTableModel(
                new Object[]{"ID", "Nome", "Email", "Telefone", "Status"}, 0);
            return emptyModel;
        }
    }
    
    /**
     * Carrega dados do módulo PDV
     */
    private DefaultTableModel carregarDadosPDV(String item) {
        try {
            if (item.contains("Produto")) {
                return carregarDadosProdutos();
            } else if (item.contains("Venda")) {
                return carregarDadosVendas();
            } else if (item.contains("Pagamento")) {
                // DAO de pagamentos não implementado - tabela vazia
                DefaultTableModel emptyModel = new DefaultTableModel(
                    new Object[]{"ID", "Venda ID", "Valor", "Forma", "Data", "Status"}, 0);
                SystemLogger.ui("📊 DAO de pagamentos não implementado - tabela vazia");
                return emptyModel;
            } else {
                // Para outros itens não implementados, tabela vazia
                DefaultTableModel emptyModel = new DefaultTableModel(
                    new Object[]{"ID", "Descrição", "Status"}, 0);
                SystemLogger.ui("📊 Módulo não implementado - tabela vazia");
                return emptyModel;
            }
        } catch (Exception e) {
            SystemLogger.error("Erro ao carregar dados PDV: " + e.getMessage());
            SystemLogger.error("❌ ERRO CRÍTICO: Falha ao carregar dados do PostgreSQL - Nenhum fallback disponível");
            DefaultTableModel emptyModel = new DefaultTableModel(
                new Object[]{"ID", "Descrição", "Status"}, 0);
            return emptyModel;
        }
    }
    
    /**
     * MÉTODO REMOVIDO: Dados simulados não são mais permitidos
     * Sistema agora usa exclusivamente dados do PostgreSQL
     */
    @Deprecated
    private DefaultTableModel carregarDadosSimulados(String item) {
        SystemLogger.error("❌ MÉTODO OBSOLETO: Dados simulados foram removidos do sistema");
        SystemLogger.error("❌ ERRO: Tentativa de usar dados simulados para: " + item);
        
        // Retornar tabela vazia forçadamente
        DefaultTableModel emptyModel = new DefaultTableModel(
            new Object[]{"ID", "Descrição", "Status"}, 0);
        return emptyModel;
    }
    
    /**
     * Volta para a tela inicial
     */
    private void voltarTelaInicial() {
        try {
            SystemLogger.ui("Voltando para tela inicial");
            
            // Limpar área de trabalho
            workArea.removeAll();
            workArea.revalidate();
            workArea.repaint();
            
            // Criar tela inicial elegante
            JPanel welcomePanel = new JPanel(new BorderLayout()) {
                @Override
                protected void paintComponent(Graphics g) {
                    super.paintComponent(g);
                    Graphics2D g2d = (Graphics2D) g.create();
                    g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                    
                    // Gradiente de boas-vindas
                    GradientPaint gradient = new GradientPaint(0, 0, new Color(52, 152, 219), 
                                                            0, getHeight(), new Color(41, 128, 185));
                    g2d.setPaint(gradient);
                    g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 20, 20);
                    g2d.dispose();
                }
            };
            welcomePanel.setBorder(BorderFactory.createEmptyBorder(50, 50, 50, 50));
            welcomePanel.setOpaque(false);
            
            JLabel welcomeLabel = new JLabel("🎉 Bem-vindo ao Hermes Comercial PDV!", JLabel.CENTER) {
                @Override
                public void paintComponent(Graphics g) {
                    super.paintComponent(g);
                    Graphics2D g2d = (Graphics2D) g.create();
                    g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                    g2d.setFont(new Font("Segoe UI", Font.BOLD, 28));
                    g2d.setColor(WHITE);
                    g2d.drawString(getText(), 5, 35);
                    g2d.dispose();
                }
            };
            
            welcomePanel.add(welcomeLabel, BorderLayout.CENTER);
            workArea.add(welcomePanel, BorderLayout.CENTER);
            workArea.revalidate();
            workArea.repaint();
            
            SystemLogger.ui("Tela inicial elegante restaurada para usuário: " + usuarioAtual);
            
        } catch (Exception e) {
            SystemLogger.error("Erro ao voltar para tela inicial elegante", e);
        }
    }
    
    /**
     * Animação de redimensionamento suave (otimizada para performance)
     */
    @SuppressWarnings("unused")
    private void animatePanelResize(JPanel panel, int targetHeight) {
        Timer timer = new Timer(5, new ActionListener() {
            private int currentHeight = panel.getHeight();
            private final int step = 8; // Passo maior para animação mais rápida
            
            @Override
            public void actionPerformed(ActionEvent e) {
                if (Math.abs(currentHeight - targetHeight) < step) {
                    currentHeight = targetHeight;
                    panel.setPreferredSize(new Dimension(panel.getPreferredSize().width, currentHeight));
                    panel.revalidate();
                    ((Timer)e.getSource()).stop();
                } else {
                    currentHeight += (targetHeight > currentHeight) ? step : -step;
                    panel.setPreferredSize(new Dimension(panel.getPreferredSize().width, currentHeight));
                    panel.revalidate();
                }
            }
        });
        timer.start();
    }
    
    public JPanel getMenuPanel() {
        return menuPanel;
    }
    
    /**
     * Botão elegante personalizado
     */
    private class ElegantButton extends JButton {
        private Color accentColor;
        private boolean isMainButton;
        private float animationScale = 1.0f;
        
        public ElegantButton(String text, Color accentColor, boolean isMainButton) {
            super(text);
            this.accentColor = accentColor;
            this.isMainButton = isMainButton;
            
            setContentAreaFilled(false);
            setBorderPainted(false);
            setFocusPainted(false);
            setOpaque(false);
            
            if (isMainButton) {
                setFont(new Font("Segoe UI", Font.BOLD, 15));
                setForeground(accentColor);
            } else {
                setFont(new Font("Segoe UI", Font.PLAIN, 13));
                setForeground(DARK_GRAY);
            }
            
            setCursor(new Cursor(Cursor.HAND_CURSOR));
            
            // Efeitos hover elegantes
            addMouseListener(new MouseAdapter() {
                @Override
                public void mouseEntered(MouseEvent e) {
                    animateHoverIn();
                }
                
                @Override
                public void mouseExited(MouseEvent e) {
                    animateHoverOut();
                }
            });
        }
        
        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2d = (Graphics2D) g.create();
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            
            int width = getWidth();
            int height = getHeight();
            
            // Aplicar escala de animação
            if (animationScale != 1.0f) {
                int scaledWidth = (int) (width * animationScale);
                int scaledHeight = (int) (height * animationScale);
                int x = (width - scaledWidth) / 2;
                int y = (height - scaledHeight) / 2;
                
                g2d.translate(x, y);
                width = scaledWidth;
                height = scaledHeight;
            }
            
            if (isMainButton) {
                // Botão principal elegante
                g2d.setColor(new Color(accentColor.getRed(), accentColor.getGreen(), accentColor.getBlue(), 30));
                g2d.fillRoundRect(0, 0, width, height, 10, 10);
                
                g2d.setColor(accentColor);
                g2d.setStroke(new BasicStroke(2));
                g2d.drawRoundRect(0, 0, width, height, 10, 10);
            } else {
                // Botão de formulário elegante com cor vibrante
                if (getModel().isRollover()) {
                    g2d.setColor(new Color(accentColor.getRed(), accentColor.getGreen(), accentColor.getBlue(), 40));
                    g2d.fillRoundRect(0, 0, width, height, 8, 8);
                    
                    g2d.setColor(accentColor);
                    g2d.setStroke(new BasicStroke(1));
                    g2d.drawRoundRect(0, 0, width, height, 8, 8);
                } else {
                    // Fundo sólido com cor vibrante mesmo sem hover
                    g2d.setColor(new Color(accentColor.getRed(), accentColor.getGreen(), accentColor.getBlue(), 200));
                    g2d.fillRoundRect(0, 0, width, height, 8, 8);
                    
                    g2d.setColor(accentColor.darker());
                    g2d.setStroke(new BasicStroke(1));
                    g2d.drawRoundRect(0, 0, width, height, 8, 8);
                }
            }
            
            // Texto elegante
            FontMetrics fm = g2d.getFontMetrics();
            int textWidth = fm.stringWidth(getText());
            int textHeight = fm.getHeight();
            
            g2d.setColor(getForeground());
            g2d.drawString(getText(), (width - textWidth) / 2, (height + textHeight) / 2 - 2);
            
            g2d.dispose();
        }
        
        private void animateHoverIn() {
            // Animação mais rápida para melhor performance
            Timer timer = new Timer(10, new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    if (animationScale < 1.03f) {
                        animationScale += 0.02f;
                        repaint();
                    } else {
                        ((Timer)e.getSource()).stop();
                    }
                }
            });
            timer.start();
        }
        
        private void animateHoverOut() {
            // Animação mais rápida para melhor performance
            Timer timer = new Timer(10, new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    if (animationScale > 1.0f) {
                        animationScale -= 0.02f;
                        repaint();
                    } else {
                        ((Timer)e.getSource()).stop();
                    }
                }
            });
            timer.start();
        }
        
        @SuppressWarnings("unused")
        public void animateExpand() {
            // Animação mais rápida para melhor performance
            Timer timer = new Timer(15, new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    if (animationScale < 1.05f) {
                        animationScale += 0.03f;
                        repaint();
                    } else {
                        ((Timer)e.getSource()).stop();
                    }
                }
            });
            timer.start();
        }
        
        @SuppressWarnings("unused")
        public void animateCollapse() {
            // Animação mais rápida para melhor performance
            Timer timer = new Timer(15, new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    if (animationScale > 1.0f) {
                        animationScale -= 0.03f;
                        repaint();
                    } else {
                        ((Timer)e.getSource()).stop();
                    }
                }
            });
            timer.start();
        }
    }
    
    /**
     * Campo de texto elegante
     */
    private class ElegantTextField extends JTextField {
        private String placeholder;
        
        public ElegantTextField(int columns) {
            super(columns);
            setOpaque(true);
            setBackground(WHITE);
            setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(MEDIUM_GRAY, 1),
                BorderFactory.createEmptyBorder(8, 12, 8, 12)
            ));
            setFont(new Font("Segoe UI", Font.PLAIN, 14));
            setForeground(DARK_GRAY);
        }
        
        public void setPlaceholder(String placeholder) {
            this.placeholder = placeholder;
        }
        
        @Override
        protected void paintComponent(Graphics g) {
            // Chamar o paintComponent do pai para garantir que o texto seja renderizado
            super.paintComponent(g);
            
            // Apenas desenhar o placeholder se o campo estiver vazio
            if (getText().isEmpty() && placeholder != null) {
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                g2d.setColor(MEDIUM_GRAY);
                g2d.setFont(getFont().deriveFont(Font.ITALIC));
                FontMetrics fm = g2d.getFontMetrics();
                @SuppressWarnings("unused")
                int textWidth = fm.stringWidth(placeholder);
                g2d.drawString(placeholder, 15, (getHeight() + fm.getAscent()) / 2 - 2);
                
                g2d.dispose();
            }
        }
    }
    
    /**
     * ScrollBar elegante personalizado
     */
    private class ElegantScrollBarUI extends javax.swing.plaf.basic.BasicScrollBarUI {
        @Override
        protected void paintThumb(Graphics g, JComponent c, Rectangle thumbBounds) {
            Graphics2D g2d = (Graphics2D) g.create();
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            
            g2d.setColor(new Color(150, 150, 150, 150));
            g2d.fillRoundRect(thumbBounds.x, thumbBounds.y, thumbBounds.width, thumbBounds.height, 8, 8);
            
            g2d.dispose();
        }
        
        @Override
        protected void paintTrack(Graphics g, JComponent c, Rectangle trackBounds) {
            // Não pintar o track para um visual mais limpo
        }
        
        @Override
        protected JButton createDecreaseButton(int orientation) {
            return createEmptyButton();
        }
        
        @Override
        protected JButton createIncreaseButton(int orientation) {
            return createEmptyButton();
        }
        
        private JButton createEmptyButton() {
            JButton button = new JButton();
            button.setPreferredSize(new Dimension(0, 0));
            button.setMinimumSize(new Dimension(0, 0));
            button.setMaximumSize(new Dimension(0, 0));
            return button;
        }
    }
    
    /**
     * Cria tela inicial elegante quando não houver tela ativa
     */
    private JPanel criarTelaInicialElegante() {
        JPanel painelInicial = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                // Gradiente elegante
                GradientPaint gradient = new GradientPaint(0, 0, PRIMARY_COLOR, 
                                                        0, getHeight(), SECONDARY_COLOR);
                g2d.setPaint(gradient);
                g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 15, 15);
                g2d.dispose();
            }
        };
        painelInicial.setOpaque(false);
        
        // Conteúdo da tela inicial
        JPanel conteudoInicial = new JPanel(new BorderLayout());
        conteudoInicial.setOpaque(false);
        
        // Logo e título
        JPanel logoPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        logoPanel.setOpaque(false);
        
        JLabel logoLabel = new JLabel("🏪");
        logoLabel.setFont(new Font("Segoe UI", Font.PLAIN, 48));
        logoLabel.setForeground(WHITE);
        
        JLabel titleLabel = new JLabel("Hermes Comercial PDV");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        titleLabel.setForeground(WHITE);
        
        logoPanel.add(logoLabel);
        logoPanel.add(Box.createHorizontalStrut(20));
        logoPanel.add(titleLabel);
        
        // Painel de informações do sistema
        JPanel infoPanel = new JPanel(new GridLayout(2, 2, 20, 10));
        infoPanel.setOpaque(false);
        
        // Informações do sistema
        JLabel usuarioLabel = new JLabel("👤 Usuário: admin");
        usuarioLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        usuarioLabel.setForeground(WHITE);
        
        JLabel dataLabel = new JLabel("📅 Data: " + java.time.LocalDate.now().format(java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy")));
        dataLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        dataLabel.setForeground(WHITE);
        
        JLabel horaLabel = new JLabel("🕐 Hora: " + java.time.LocalTime.now().format(java.time.format.DateTimeFormatter.ofPattern("HH:mm:ss")));
        horaLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        horaLabel.setForeground(WHITE);
        
        JLabel versaoLabel = new JLabel("🔧 Versão: 3.2.0");
        versaoLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        versaoLabel.setForeground(WHITE);
        
        infoPanel.add(usuarioLabel);
        infoPanel.add(dataLabel);
        infoPanel.add(horaLabel);
        infoPanel.add(versaoLabel);
        
        // Mensagem de boas-vindas
        JPanel mensagemPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        mensagemPanel.setOpaque(false);
        
        JLabel mensagemLabel = new JLabel("🌟 Bem-vindo ao Sistema Hermes Comercial PDV 🌟");
        mensagemLabel.setFont(new Font("Segoe UI", Font.BOLD, 20));
        mensagemLabel.setForeground(WHITE);
        
        mensagemPanel.add(mensagemLabel);
        
        // Botões de acesso rápido
        JPanel botoesPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        botoesPanel.setOpaque(false);
        
        ElegantButton btnVendas = new ElegantButton("📋 Vendas", SUCCESS_COLOR, false);
        btnVendas.setForeground(WHITE);
        btnVendas.addActionListener(e -> {
            // Log de acesso ao submenu Nova Venda
            SystemLogger.ui("Usuário acessando submenu Nova Venda do menu Vendas");
            SystemLogger.info("Acesso ao submenu Nova Venda - Usuário: " + usuarioAtual);
            
            // Abrir tela de vendas
            abrirTelaCompletaElegante("📋 Nova Venda", "VENDAS");
        });
        
        ElegantButton btnProdutos = new ElegantButton("📦 Produtos", WARNING_COLOR, false);
        btnProdutos.setForeground(WHITE);
        btnProdutos.addActionListener(e -> {
            // Abrir tela de produtos
            abrirTelaCompletaElegante("➕ Cadastrar Produto", "PRODUTOS");
        });
        
        ElegantButton btnClientes = new ElegantButton("👥 Clientes", PRIMARY_COLOR, false);
        btnClientes.setForeground(WHITE);
        btnClientes.addActionListener(e -> {
            // Abrir tela de clientes
            abrirTelaCompletaElegante("👤 Novo Cliente", "CLIENTES");
        });
        
        botoesPanel.add(btnVendas);
        botoesPanel.add(btnProdutos);
        botoesPanel.add(btnClientes);
        
        // Montar conteúdo
        JPanel centroPanel = new JPanel(new BorderLayout());
        centroPanel.setOpaque(false);
        centroPanel.add(logoPanel, BorderLayout.NORTH);
        centroPanel.add(infoPanel, BorderLayout.CENTER);
        centroPanel.add(mensagemPanel, BorderLayout.SOUTH);
        
        conteudoInicial.add(centroPanel, BorderLayout.CENTER);
        conteudoInicial.add(botoesPanel, BorderLayout.SOUTH);
        
        painelInicial.add(conteudoInicial, BorderLayout.CENTER);
        
        return painelInicial;
    }
    
    // Dark Mode removido completamente
}
