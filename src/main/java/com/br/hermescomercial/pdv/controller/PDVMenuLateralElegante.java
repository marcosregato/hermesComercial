package com.br.hermescomercial.pdv.controller;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.RoundRectangle2D;
import java.text.SimpleDateFormat;
import java.util.Date;
import com.br.hermescomercial.util.SystemLogger;

/**
 * Menu Lateral com Submenus em Cascade - Versão Elegante
 * Design moderno e elegante com animações suaves e visual profissional
 */
public class PDVMenuLateralElegante {
    
    private JPanel menuPanel;
    private String usuarioAtual;
    private String nomeUsuario;
    private JPanel workArea; // Área de trabalho principal
    
    // Cores elegantes
    private static final Color PRIMARY_COLOR = new Color(41, 128, 185);      // Azul elegante
    private static final Color SECONDARY_COLOR = new Color(52, 73, 94);      // Cinza escuro
    private static final Color ACCENT_COLOR = new Color(231, 76, 60);        // Vermelho elegante
    private static final Color SUCCESS_COLOR = new Color(39, 174, 96);       // Verde elegante
    private static final Color WARNING_COLOR = new Color(243, 156, 18);      // Amarelo elegante
    private static final Color LIGHT_GRAY = new Color(245, 247, 250);        // Cinza claro
    private static final Color MEDIUM_GRAY = new Color(189, 195, 199);       // Cinza médio
    private static final Color DARK_GRAY = new Color(127, 140, 141);         // Cinza escuro
    private static final Color WHITE = Color.WHITE;
    private static final Color BLACK = new Color(44, 62, 80);                // Preto elegante
    
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
                    
                    // Gradiente sutil de fundo
                    GradientPaint gradient = new GradientPaint(0, 0, new Color(250, 252, 255), 
                                                            0, getHeight(), new Color(240, 244, 248));
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
            "➕ Cadastrar Produto",
            "📝 Editar Produto",
            "🔍 Consultar Produto",
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
            "➕ Novo Cliente",
            "📝 Editar Cliente",
            "🔍 Consultar Cliente",
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
            "📋 Consultar Estoque",
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
            "💰 Contas a Receber",
            "💸 Contas a Pagar",
            "🏦 Fluxo de Caixa",
            "📊 Fechamento de Caixa",
            "💳 Conciliação Bancária",
            "📈 Relatórios Financeiros",
            "💵 Cobranças"
        }, "FINANCEIRO", new Color(230, 126, 34));
    }
    
    /**
     * Cria menus Relatórios elegantes
     */
    private void createMenuRelatoriosElegante(JPanel parent) {
        createCascadeMenuElegante(parent, "📈 Relatórios", new String[]{
            "📊 Relatório de Vendas",
            "📦 Relatório de Produtos",
            "👥 Relatório de Clientes",
            "💰 Relatório Financeiro",
            "📊 Dashboard",
            "📋 Relatório de Estoque",
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
                
                // Abrir tela completa elegante
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
     * Abre tela completa elegante
     */
    private void abrirTelaCompletaElegante(String item, String module) {
        try {
            SystemLogger.ui("Abrindo tela completa elegante: " + item);
            
            // Limpar área de trabalho
            workArea.removeAll();
            workArea.revalidate();
            workArea.repaint();
            
            // Criar conteúdo elegante
            JPanel conteudoTela = criarTelaCompletaElegante(item, module);
            workArea.add(conteudoTela, BorderLayout.CENTER);
            
            // Atualizar área de trabalho
            workArea.revalidate();
            workArea.repaint();
            
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
        conteudoPrincipal.add(tabelaPanel);
        conteudoPrincipal.add(Box.createVerticalStrut(15));
        
        // Painel de botões elegante
        JPanel botoesPanel = criarPainelBotoesElegante(item);
        conteudoPrincipal.add(botoesPanel);
        
        scrollPane.setViewportView(conteudoPrincipal);
        painelPrincipal.add(scrollPane, BorderLayout.CENTER);
        
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
                GradientPaint gradient = new GradientPaint(0, 0, moduleColor, 
                                                        0, getHeight(), moduleColorDark);
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
            titulo.contains("Conciliação Bancária") || titulo.contains("Relatórios Financeiros") || 
            titulo.contains("Cobranças")) {
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
        
        switch (item) {
            case "🛒 Ponto de Venda":
                // Código do produto
                gbc.gridx = 0; gbc.gridy = 0;
                JLabel lblCodigo = new JLabel("Código do Produto:");
                lblCodigo.setFont(new Font("Segoe UI", Font.BOLD, 12));
                lblCodigo.setForeground(DARK_GRAY);
                panel.add(lblCodigo, gbc);
                gbc.gridx = 1; gbc.gridy = 0;
                ElegantTextField txtCodigo = new ElegantTextField(15);
                panel.add(txtCodigo, gbc);
                
                // Descrição
                gbc.gridx = 0; gbc.gridy = 1;
                JLabel lblDescricao = new JLabel("Descrição:");
                lblDescricao.setFont(new Font("Segoe UI", Font.BOLD, 12));
                lblDescricao.setForeground(DARK_GRAY);
                panel.add(lblDescricao, gbc);
                gbc.gridx = 1; gbc.gridy = 1;
                ElegantTextField txtDescricao = new ElegantTextField(30);
                txtDescricao.setEditable(false);
                panel.add(txtDescricao, gbc);
                
                // Quantidade
                gbc.gridx = 0; gbc.gridy = 2;
                JLabel lblQuantidade = new JLabel("Quantidade:");
                lblQuantidade.setFont(new Font("Segoe UI", Font.BOLD, 12));
                lblQuantidade.setForeground(DARK_GRAY);
                panel.add(lblQuantidade, gbc);
                gbc.gridx = 1; gbc.gridy = 2;
                ElegantTextField txtQuantidade = new ElegantTextField(10);
                panel.add(txtQuantidade, gbc);
                
                // Valor Unitário
                gbc.gridx = 0; gbc.gridy = 3;
                JLabel lblValorUnitario = new JLabel("Valor Unitário:");
                lblValorUnitario.setFont(new Font("Segoe UI", Font.BOLD, 12));
                lblValorUnitario.setForeground(DARK_GRAY);
                panel.add(lblValorUnitario, gbc);
                gbc.gridx = 1; gbc.gridy = 3;
                ElegantTextField txtValorUnitario = new ElegantTextField(15);
                txtValorUnitario.setEditable(false);
                panel.add(txtValorUnitario, gbc);
                
                // Valor Total
                gbc.gridx = 0; gbc.gridy = 4;
                JLabel lblValorTotal = new JLabel("Valor Total:");
                lblValorTotal.setFont(new Font("Segoe UI", Font.BOLD, 12));
                lblValorTotal.setForeground(DARK_GRAY);
                panel.add(lblValorTotal, gbc);
                gbc.gridx = 1; gbc.gridy = 4;
                ElegantTextField txtValorTotal = new ElegantTextField(15);
                txtValorTotal.setEditable(false);
                panel.add(txtValorTotal, gbc);
                
                // Forma de Pagamento
                gbc.gridx = 0; gbc.gridy = 5;
                JLabel lblFormaPagamento = new JLabel("Forma Pagamento:");
                lblFormaPagamento.setFont(new Font("Segoe UI", Font.BOLD, 12));
                lblFormaPagamento.setForeground(DARK_GRAY);
                panel.add(lblFormaPagamento, gbc);
                gbc.gridx = 1; gbc.gridy = 5;
                JComboBox<String> cbFormaPagamento = new JComboBox<>(new String[]{"Dinheiro", "Cartão Débito", "Cartão Crédito", "PIX"});
                cbFormaPagamento.setFont(new Font("Segoe UI", Font.PLAIN, 12));
                panel.add(cbFormaPagamento, gbc);
                break;
                
            case "💳 Pagamentos":
                // Cliente
                gbc.gridx = 0; gbc.gridy = 0;
                JLabel lblCliente = new JLabel("Cliente:");
                lblCliente.setFont(new Font("Segoe UI", Font.BOLD, 12));
                lblCliente.setForeground(DARK_GRAY);
                panel.add(lblCliente, gbc);
                gbc.gridx = 1; gbc.gridy = 0;
                ElegantTextField txtCliente = new ElegantTextField(30);
                panel.add(txtCliente, gbc);
                
                // Valor do Pagamento
                gbc.gridx = 0; gbc.gridy = 1;
                JLabel lblValor = new JLabel("Valor:");
                lblValor.setFont(new Font("Segoe UI", Font.BOLD, 12));
                lblValor.setForeground(DARK_GRAY);
                panel.add(lblValor, gbc);
                gbc.gridx = 1; gbc.gridy = 1;
                ElegantTextField txtValor = new ElegantTextField(15);
                panel.add(txtValor, gbc);
                
                // Forma de Pagamento
                gbc.gridx = 0; gbc.gridy = 2;
                JLabel lblPagamento = new JLabel("Forma Pagamento:");
                lblPagamento.setFont(new Font("Segoe UI", Font.BOLD, 12));
                lblPagamento.setForeground(DARK_GRAY);
                panel.add(lblPagamento, gbc);
                gbc.gridx = 1; gbc.gridy = 2;
                JComboBox<String> cbPagamento = new JComboBox<>(new String[]{"Dinheiro", "Cartão Débito", "Cartão Crédito", "PIX", "Boleto"});
                cbPagamento.setFont(new Font("Segoe UI", Font.PLAIN, 12));
                panel.add(cbPagamento, gbc);
                
                // Data do Pagamento
                gbc.gridx = 0; gbc.gridy = 3;
                JLabel lblData = new JLabel("Data:");
                lblData.setFont(new Font("Segoe UI", Font.BOLD, 12));
                lblData.setForeground(DARK_GRAY);
                panel.add(lblData, gbc);
                gbc.gridx = 1; gbc.gridy = 3;
                ElegantTextField txtData = new ElegantTextField(10);
                panel.add(txtData, gbc);
                break;
                
            default:
                return criarFormularioPadraoElegante(item, "PDV", gbc);
        }
        
        return panel;
    }
    
    /**
     * Formulários Vendas elegantes
     */
    private JPanel criarFormularioVendasElegante(String item, GridBagConstraints gbc) {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setOpaque(false);
        
        switch (item) {
            case "📋 Nova Venda":
                // Cliente
                gbc.gridx = 0; gbc.gridy = 0;
                JLabel lblCliente = new JLabel("Cliente:");
                lblCliente.setFont(new Font("Segoe UI", Font.BOLD, 12));
                lblCliente.setForeground(DARK_GRAY);
                panel.add(lblCliente, gbc);
                gbc.gridx = 1; gbc.gridy = 0;
                ElegantTextField txtCliente = new ElegantTextField(30);
                panel.add(txtCliente, gbc);
                
                // Data da Venda
                gbc.gridx = 0; gbc.gridy = 1;
                JLabel lblData = new JLabel("Data:");
                lblData.setFont(new Font("Segoe UI", Font.BOLD, 12));
                lblData.setForeground(DARK_GRAY);
                panel.add(lblData, gbc);
                gbc.gridx = 1; gbc.gridy = 1;
                ElegantTextField txtData = new ElegantTextField(10);
                panel.add(txtData, gbc);
                
                // Vendedor
                gbc.gridx = 0; gbc.gridy = 2;
                JLabel lblVendedor = new JLabel("Vendedor:");
                lblVendedor.setFont(new Font("Segoe UI", Font.BOLD, 12));
                lblVendedor.setForeground(DARK_GRAY);
                panel.add(lblVendedor, gbc);
                gbc.gridx = 1; gbc.gridy = 2;
                ElegantTextField txtVendedor = new ElegantTextField(20);
                txtVendedor.setEditable(false);
                panel.add(txtVendedor, gbc);
                
                // Condição de Pagamento
                gbc.gridx = 0; gbc.gridy = 3;
                JLabel lblCondicao = new JLabel("Condição Pagamento:");
                lblCondicao.setFont(new Font("Segoe UI", Font.BOLD, 12));
                lblCondicao.setForeground(DARK_GRAY);
                panel.add(lblCondicao, gbc);
                gbc.gridx = 1; gbc.gridy = 3;
                JComboBox<String> cbCondicao = new JComboBox<>(new String[]{"À Vista", "30 Dias", "60 Dias", "90 Dias"});
                cbCondicao.setFont(new Font("Segoe UI", Font.PLAIN, 12));
                panel.add(cbCondicao, gbc);
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
                
            default:
                return criarFormularioPadraoElegante(item, "PRODUTOS", gbc);
        }
        
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
        JPanel tabelaPanel = new JPanel(new BorderLayout()) {
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
        tabelaPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        tabelaPanel.setOpaque(false);
        
        // Título elegante
        JLabel tabelaLabel = new JLabel("📊 Resultados da Consulta") {
            @Override
            public void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2d.setFont(new Font("Segoe UI", Font.BOLD, 18));
                g2d.setColor(WARNING_COLOR);
                g2d.drawString(getText(), 5, 25);
                g2d.dispose();
            }
        };
        
        // Tabela elegante
        String[] colunas = {"ID", "Descrição", "Valor", "Data", "Status"};
        Object[][] dados = {
            {1, "Item 1", "R$ 100,00", "06/05/2026", "Ativo"},
            {2, "Item 2", "R$ 200,00", "05/05/2026", "Inativo"},
            {3, "Item 3", "R$ 150,00", "04/05/2026", "Ativo"}
        };
        
        DefaultTableModel model = new DefaultTableModel(dados, colunas);
        JTable tabela = new JTable(model);
        configurarTabelaElegante(tabela);
        
        JScrollPane scrollPane = new JScrollPane(tabela);
        scrollPane.setPreferredSize(new Dimension(0, 250));
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.setOpaque(false);
        scrollPane.getViewport().setOpaque(false);
        
        tabelaPanel.add(tabelaLabel, BorderLayout.NORTH);
        tabelaPanel.add(scrollPane, BorderLayout.CENTER);
        
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
        tabela.setOpaque(false);
        ((JComponent) tabela.getDefaultRenderer(Object.class)).setOpaque(false);
        
        // Header elegante
        JTableHeader header = tabela.getTableHeader();
        header.setFont(new Font("Segoe UI", Font.BOLD, 13));
        header.setBackground(PRIMARY_COLOR);
        header.setForeground(WHITE);
        header.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        header.setOpaque(true);
        
        // Células elegantes
        tabela.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        tabela.setGridColor(new Color(230, 230, 230));
        tabela.setShowHorizontalLines(true);
        tabela.setShowVerticalLines(false);
        
        // Alternar cores das linhas elegantemente
        tabela.setDefaultRenderer(Object.class, new javax.swing.table.DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                
                if (!isSelected) {
                    if (row % 2 == 0) {
                        c.setBackground(WHITE);
                    } else {
                        c.setBackground(LIGHT_GRAY);
                    }
                } else {
                    c.setBackground(PRIMARY_COLOR);
                    c.setForeground(WHITE);
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
            JOptionPane.showMessageDialog(workArea, 
                "🚀 Funcionalidade 'Novo' em desenvolvimento para " + item, 
                "Novo", JOptionPane.INFORMATION_MESSAGE);
        });
        
        ElegantButton btnEditar = new ElegantButton("📝 Editar", PRIMARY_COLOR, false);
        btnEditar.setForeground(WHITE);
        btnEditar.addActionListener(e -> {
            JOptionPane.showMessageDialog(workArea, 
                "🚀 Funcionalidade 'Editar' em desenvolvimento para " + item, 
                "Editar", JOptionPane.INFORMATION_MESSAGE);
        });
        
        ElegantButton btnExcluir = new ElegantButton("🗑️ Excluir", ACCENT_COLOR, false);
        btnExcluir.setForeground(WHITE);
        btnExcluir.addActionListener(e -> {
            JOptionPane.showMessageDialog(workArea, 
                "🚀 Funcionalidade 'Excluir' em desenvolvimento para " + item, 
                "Excluir", JOptionPane.INFORMATION_MESSAGE);
        });
        
        ElegantButton btnSalvar = new ElegantButton("💾 Salvar", SUCCESS_COLOR, false);
        btnSalvar.setForeground(WHITE);
        btnSalvar.addActionListener(e -> {
            JOptionPane.showMessageDialog(workArea, 
                "🚀 Dados salvos com sucesso para " + item, 
                "Sucesso", JOptionPane.INFORMATION_MESSAGE);
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
        statusLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        statusLabel.setForeground(WHITE);
        
        statusBar.add(statusLabel, BorderLayout.WEST);
        
        return statusBar;
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
            setOpaque(false);
            setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(MEDIUM_GRAY, 1),
                BorderFactory.createEmptyBorder(8, 12, 8, 12)
            ));
            setBackground(WHITE);
            setFont(new Font("Segoe UI", Font.PLAIN, 14));
            setForeground(DARK_GRAY);
        }
        
        public void setPlaceholder(String placeholder) {
            this.placeholder = placeholder;
        }
        
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2d = (Graphics2D) g.create();
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            
            // Fundo elegante
            g2d.setColor(WHITE);
            g2d.fillRoundRect(1, 1, getWidth()-2, getHeight()-2, 6, 6);
            
            // Placeholder
            if (getText().isEmpty() && placeholder != null) {
                g2d.setColor(MEDIUM_GRAY);
                g2d.setFont(getFont().deriveFont(Font.ITALIC));
                FontMetrics fm = g2d.getFontMetrics();
                int textWidth = fm.stringWidth(placeholder);
                g2d.drawString(placeholder, 15, (getHeight() + fm.getAscent()) / 2 - 2);
            }
            
            g2d.dispose();
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
}
