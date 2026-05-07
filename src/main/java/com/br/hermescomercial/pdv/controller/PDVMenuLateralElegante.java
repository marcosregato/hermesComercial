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
                
                // Painel de Busca
                gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
                gbc.fill = GridBagConstraints.HORIZONTAL;
                gbc.insets = new Insets(5, 5, 15, 5);
                
                JPanel buscaPanelOrcamentos = new JPanel(new GridBagLayout());
                buscaPanelOrcamentos.setBorder(BorderFactory.createTitledBorder(
                    BorderFactory.createEtchedBorder(), "🏷️ Orçamentos",
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
                gbc.gridx = 0; gbc.gridy = 1;
                gbc.fill = GridBagConstraints.NONE;
                JLabel lblFiltrosOrcamentos = new JLabel("Filtros:");
                lblFiltrosOrcamentos.setFont(new Font("Segoe UI", Font.BOLD, 12));
                lblFiltrosOrcamentos.setForeground(DARK_GRAY);
                panel.add(lblFiltrosOrcamentos, gbc);
                
                gbc.gridx = 1; gbc.gridy = 1;
                gbc.fill = GridBagConstraints.HORIZONTAL;
                JPanel filtrosPanelOrcamentos = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
                
                JLabel lblStatusOrcamento = new JLabel("Status:");
                lblStatusOrcamento.setFont(new Font("Segoe UI", Font.PLAIN, 12));
                filtrosPanelOrcamentos.add(lblStatusOrcamento);
                JComboBox<String> cbStatusOrcamento = new JComboBox<>(new String[]{
                    "Todos", "Aberto", "Aprovado", "Rejeitado", "Fechado"
                });
                cbStatusOrcamento.setFont(new Font("Segoe UI", Font.PLAIN, 12));
                filtrosPanelOrcamentos.add(cbStatusOrcamento);
                
                JLabel lblValidadeOrcamento = new JLabel("Validade:");
                lblValidadeOrcamento.setFont(new Font("Segoe UI", Font.PLAIN, 12));
                filtrosPanelOrcamentos.add(lblValidadeOrcamento);
                JComboBox<String> cbValidadeOrcamento = new JComboBox<>(new String[]{
                    "Todos", "Hoje", "Esta Semana", "Este Mês", "Próximo Mês"
                });
                cbValidadeOrcamento.setFont(new Font("Segoe UI", Font.PLAIN, 12));
                filtrosPanelOrcamentos.add(cbValidadeOrcamento);
                
                panel.add(filtrosPanelOrcamentos, gbc);
                
                // Tabela de Orçamentos
                gbc.gridx = 0; gbc.gridy = 2; gbc.gridwidth = 2;
                gbc.fill = GridBagConstraints.BOTH;
                gbc.weightx = 1.0; gbc.weighty = 1.0;
                
                String[] colunasTabelaOrcamentos = {"Número", "Cliente", "Produto", "Valor", "Validade", "Status", "Vendedor"};
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
