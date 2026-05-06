package com.br.hermescomercial.pdv.controller;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import com.br.hermescomercial.util.SystemLogger;

/**
 * Menu Lateral com Submenus em Cascade - Versão Integrada
 * Telas abrem na área de trabalho principal em vez de janelas separadas
 */
public class PDVMenuLateralIntegrated {
    
    private JPanel menuPanel;
    private String usuarioAtual;
    private String nomeUsuario;
    private JPanel workArea; // Área de trabalho principal
    
    public PDVMenuLateralIntegrated(String usuario, String nome, JPanel workArea) {
        this.usuarioAtual = usuario;
        this.nomeUsuario = nome;
        this.workArea = workArea;
        SystemLogger.ui("Criando menu lateral integrado para usuário: " + usuario);
    }
    
    public JPanel createMenuPanel() {
        try {
            menuPanel = new JPanel(new BorderLayout());
            menuPanel.setBackground(new Color(236, 240, 241));
            menuPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
            menuPanel.setPreferredSize(new Dimension(280, 0));
            
            // Header do menu
            JPanel headerPanel = createMenuHeader();
            menuPanel.add(headerPanel, BorderLayout.NORTH);
            
            // Painel de menus
            JPanel menusPanel = new JPanel();
            menusPanel.setLayout(new BoxLayout(menusPanel, BoxLayout.Y_AXIS));
            menusPanel.setBackground(new Color(236, 240, 241));
            
            // Criar menus principais
            createMenuPDV(menusPanel);
            createMenuVendas(menusPanel);
            createMenuProdutos(menusPanel);
            createMenuClientes(menusPanel);
            createMenuEstoque(menusPanel);
            createMenuFinanceiro(menusPanel);
            createMenuRelatorios(menusPanel);
            createMenuConfiguracoes(menusPanel);
            
            // Adicionar espaço no final
            menusPanel.add(Box.createVerticalStrut(20));
            
            // Scroll para menus
            JScrollPane scrollPane = new JScrollPane(menusPanel);
            scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
            scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
            scrollPane.setBorder(null);
            scrollPane.getVerticalScrollBar().setUnitIncrement(16);
            scrollPane.getVerticalScrollBar().setBlockIncrement(16);
            
            menuPanel.add(scrollPane, BorderLayout.CENTER);
            
            SystemLogger.ui("Menu lateral integrado criado com 8 menus principais");
            return menuPanel;
            
        } catch (Exception e) {
            SystemLogger.error("Erro ao criar menu lateral integrado", e);
            // Retornar painel básico em caso de erro
            JPanel errorPanel = new JPanel(new BorderLayout());
            errorPanel.setBackground(Color.WHITE);
            JLabel errorLabel = new JLabel("❌ Erro no menu", JLabel.CENTER);
            errorPanel.add(errorLabel, BorderLayout.CENTER);
            return errorPanel;
        }
    }
    
    private JPanel createMenuHeader() {
        try {
            JPanel header = new JPanel(new BorderLayout());
            header.setBackground(new Color(52, 73, 94));
            header.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
            header.setPreferredSize(new Dimension(0, 80));
            
            // Logo e título
            JPanel logoPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
            logoPanel.setOpaque(false);
            
            JLabel logoLabel = new JLabel("🏪");
            logoLabel.setFont(new Font("Arial", Font.BOLD, 24));
            logoLabel.setForeground(Color.WHITE);
            
            JLabel titleLabel = new JLabel("MENU");
            titleLabel.setForeground(Color.WHITE);
            titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
            
            logoPanel.add(logoLabel);
            logoPanel.add(Box.createHorizontalStrut(10));
            logoPanel.add(titleLabel);
            
            // Informações do usuário
            JPanel userPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
            userPanel.setOpaque(false);
            
            JLabel userLabel = new JLabel("👤 " + nomeUsuario);
            userLabel.setForeground(Color.WHITE);
            userLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
            
            userPanel.add(userLabel);
            
            header.add(logoPanel, BorderLayout.WEST);
            header.add(userPanel, BorderLayout.EAST);
            
            return header;
        } catch (Exception e) {
            SystemLogger.error("Erro ao criar header do menu", e);
            JPanel errorHeader = new JPanel();
            errorHeader.setBackground(Color.RED);
            errorHeader.add(new JLabel("❌"));
            return errorHeader;
        }
    }
    
    private void createMenuPDV(JPanel parent) {
        createCascadeMenu(parent, "🏪 PDV", new String[]{
            "🛒 Ponto de Venda",
            "💳 Pagamentos",
            "🧾 Cupom Fiscal",
            "📱 Terminal Venda",
            "🎯 Venda Rápida"
        }, "PDV");
    }
    
    private void createMenuVendas(JPanel parent) {
        createCascadeMenu(parent, "💰 Vendas", new String[]{
            "📋 Nova Venda",
            "🔍 Consultar Vendas",
            "↩️ Devoluções",
            "📊 Resumo Diário",
            "📈 Relatório de Vendas",
            "🏷️ Orçamentos",
            "🚚 Entregas"
        }, "VENDAS");
    }
    
    private void createMenuProdutos(JPanel parent) {
        createCascadeMenu(parent, "📦 Produtos", new String[]{
            "➕ Cadastrar Produto",
            "📝 Editar Produto",
            "🔍 Consultar Produto",
            "📂 Categorias",
            "🏷️ Fornecedores",
            "📊 Estoque Mínimo",
            "🏷️ Códigos de Barras"
        }, "PRODUTOS");
    }
    
    private void createMenuClientes(JPanel parent) {
        createCascadeMenu(parent, "👥 Clientes", new String[]{
            "➕ Novo Cliente",
            "📝 Editar Cliente",
            "🔍 Consultar Cliente",
            "📊 Histórico de Compras",
            "💳 Carteira Fidelidade",
            "📍 Endereços",
            "📞 Contatos"
        }, "CLIENTES");
    }
    
    private void createMenuEstoque(JPanel parent) {
        createCascadeMenu(parent, "📊 Estoque", new String[]{
            "📋 Consultar Estoque",
            "➕ Entrada de Mercadoria",
            "➖ Saída de Mercadoria",
            "🔄 Transferência",
            "📊 Inventário",
            "⚠️ Estoque Baixo",
            "📈 Movimentações"
        }, "ESTOQUE");
    }
    
    private void createMenuFinanceiro(JPanel parent) {
        createCascadeMenu(parent, "🏦 Financeiro", new String[]{
            "💰 Contas a Receber",
            "💸 Contas a Pagar",
            "🏦 Fluxo de Caixa",
            "📊 Fechamento de Caixa",
            "💳 Conciliação Bancária",
            "📈 Relatórios Financeiros",
            "💵 Cobranças"
        }, "FINANCEIRO");
    }
    
    private void createMenuRelatorios(JPanel parent) {
        createCascadeMenu(parent, "📈 Relatórios", new String[]{
            "📊 Relatório de Vendas",
            "📦 Relatório de Produtos",
            "👥 Relatório de Clientes",
            "💰 Relatório Financeiro",
            "📊 Dashboard",
            "📋 Relatório de Estoque",
            "🎯 Análise de Vendas"
        }, "RELATORIOS");
    }
    
    private void createMenuConfiguracoes(JPanel parent) {
        createCascadeMenu(parent, "⚙️ Configurações", new String[]{
            "👤 Usuários e Permissões",
            "🏢 Empresa",
            "🖥️ Sistema",
            "🖨️ Impressoras",
            "💳 Formas de Pagamento",
            "🌐 Integrações",
            "🔧 Parâmetros",
            "💾 Backup"
        }, "CONFIGURACOES");
    }
    
    private void createCascadeMenu(JPanel parent, String menuTitle, String[] subMenus, String module) {
        try {
            // Painel principal do menu
            JPanel menuPanel = new JPanel(new BorderLayout());
            menuPanel.setBackground(Color.WHITE);
            menuPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(189, 195, 199)),
                BorderFactory.createEmptyBorder(5, 10, 5, 10)
            ));
            menuPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 50));
            menuPanel.setMinimumSize(new Dimension(0, 50));
            menuPanel.setPreferredSize(new Dimension(0, 50));
            
            // Botão principal do menu
            JButton mainButton = new JButton(menuTitle);
            mainButton.setBackground(Color.WHITE);
            mainButton.setForeground(new Color(52, 73, 94));
            mainButton.setFont(new Font("Segoe UI", Font.BOLD, 14));
            mainButton.setHorizontalAlignment(SwingConstants.LEFT);
            mainButton.setBorderPainted(false);
            mainButton.setFocusPainted(false);
            mainButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
            mainButton.setOpaque(true);
            mainButton.setContentAreaFilled(true);
            
            // Painel de submenus (inicialmente oculto)
            JPanel subMenuPanel = new JPanel();
            subMenuPanel.setLayout(new BoxLayout(subMenuPanel, BoxLayout.Y_AXIS));
            subMenuPanel.setBackground(new Color(248, 249, 250));
            subMenuPanel.setBorder(BorderFactory.createEmptyBorder(5, 20, 5, 10));
            subMenuPanel.setVisible(false);
            
            // Criar botões de submenu
            for (String subMenu : subMenus) {
                JButton subButton = createSubMenuButton(subMenu, module);
                subMenuPanel.add(subButton);
                subMenuPanel.add(Box.createVerticalStrut(3));
            }
            
            // Adicionar componentes ao painel principal
            menuPanel.add(mainButton, BorderLayout.NORTH);
            menuPanel.add(subMenuPanel, BorderLayout.CENTER);
            
            // VARIÁVEL FINAL PARA CAPTURA CORRETA
            final JPanel finalSubMenuPanel = subMenuPanel;
            final JButton finalMainButton = mainButton;
            final String finalMenuTitle = menuTitle;
            
            // Ação do botão principal (toggle submenu)
            mainButton.addActionListener(e -> {
                try {
                    // Verificar estado atual
                    boolean isVisible = finalSubMenuPanel.isVisible();
                    
                    // Toggle visibility
                    finalSubMenuPanel.setVisible(!isVisible);
                    
                    // Atualizar texto do botão com ícone
                    if (isVisible) {
                        finalMainButton.setText(finalMenuTitle.replace("▼", "▶"));
                    } else {
                        finalMainButton.setText(finalMenuTitle.replace("▶", "▼"));
                    }
                    
                    // Log da ação
                    SystemLogger.operation("MENU_TOGGLE", module, 
                        "Usuário: " + usuarioAtual + " toggle menu: " + finalMenuTitle + " (visible: " + !isVisible + ")");
                    
                    // FORÇAR ATUALIZAÇÃO DO LAYOUT
                    SwingUtilities.invokeLater(() -> {
                        // Revalidar todos os componentes
                        finalSubMenuPanel.revalidate();
                        finalSubMenuPanel.repaint();
                        menuPanel.revalidate();
                        menuPanel.repaint();
                        parent.revalidate();
                        parent.repaint();
                        
                        // Forçar atualização do tamanho
                        menuPanel.setPreferredSize(new Dimension(0, 
                            isVisible ? 50 : 50 + (subMenus.length * 40)));
                        menuPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 
                            isVisible ? 50 : 50 + (subMenus.length * 40)));
                        menuPanel.revalidate();
                    });
                    
                } catch (Exception ex) {
                    SystemLogger.error("Erro ao toggle menu: " + menuTitle, ex);
                    ex.printStackTrace();
                }
            });
            
            // Adicionar ao painel pai
            parent.add(menuPanel);
            parent.add(Box.createVerticalStrut(5));
            
        } catch (Exception e) {
            SystemLogger.error("Erro ao criar menu cascade: " + menuTitle, e);
            // Adicionar menu simples em caso de erro
            JButton errorButton = new JButton("❌ " + menuTitle);
            errorButton.setBackground(Color.RED);
            errorButton.setForeground(Color.WHITE);
            parent.add(errorButton);
            parent.add(Box.createVerticalStrut(5));
        }
    }
    
    private JButton createSubMenuButton(String text, String module) {
        try {
            JButton button = new JButton(text);
            button.setBackground(new Color(248, 249, 250));
            button.setForeground(new Color(73, 80, 87));
            button.setFont(new Font("Segoe UI", Font.PLAIN, 12));
            button.setHorizontalAlignment(SwingConstants.LEFT);
            button.setBorderPainted(false);
            button.setFocusPainted(false);
            button.setCursor(new Cursor(Cursor.HAND_CURSOR));
            button.setMaximumSize(new Dimension(Integer.MAX_VALUE, 35));
            button.setMinimumSize(new Dimension(0, 35));
            button.setPreferredSize(new Dimension(0, 35));
            button.setOpaque(true);
            button.setContentAreaFilled(true);
            button.setBorder(BorderFactory.createEmptyBorder(8, 15, 8, 15));
            
            // Efeito hover
            button.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseEntered(MouseEvent e) {
                    try {
                        button.setBackground(new Color(236, 240, 241));
                        button.setForeground(new Color(52, 73, 94));
                    } catch (Exception ex) {
                        SystemLogger.error("Erro no hover do submenu", ex);
                    }
                }
                
                @Override
                public void mouseExited(MouseEvent e) {
                    try {
                        button.setBackground(new Color(248, 249, 250));
                        button.setForeground(new Color(73, 80, 87));
                    } catch (Exception ex) {
                        SystemLogger.error("Erro ao sair do hover do submenu", ex);
                    }
                }
            });
            
            // Ação do submenu - ABRIR TELA NA ÁREA DE TRABALHO PRINCIPAL
            button.addActionListener(e -> {
                try {
                    SystemLogger.operation("SUBMENU_CLICK", module, 
                        "Usuário: " + usuarioAtual + " acessou: " + text);
                    
                    // Abrir tela na área de trabalho principal
                    abrirTelaNaAreaTrabalho(text, module);
                        
                } catch (Exception ex) {
                    SystemLogger.error("Erro ao executar ação do submenu: " + text, ex);
                    JOptionPane.showMessageDialog(menuPanel, 
                        "❌ Erro ao executar: " + text + "\n\n" + ex.getMessage(), 
                        "Erro", JOptionPane.ERROR_MESSAGE);
                }
            });
            
            return button;
        } catch (Exception e) {
            SystemLogger.error("Erro ao criar botão de submenu: " + text, e);
            JButton errorButton = new JButton("❌ " + text);
            errorButton.setBackground(Color.RED);
            errorButton.setForeground(Color.WHITE);
            return errorButton;
        }
    }
    
    /**
     * Abre a tela correspondente na área de trabalho principal
     */
    private void abrirTelaNaAreaTrabalho(String item, String module) {
        try {
            SystemLogger.ui("Abrindo tela na área de trabalho principal: " + item);
            
            // Limpar área de trabalho
            workArea.removeAll();
            workArea.revalidate();
            workArea.repaint();
            
            // Criar conteúdo da tela na área de trabalho
            JPanel conteudoTela = criarConteudoTelaIntegrado(item, module);
            workArea.add(conteudoTela, BorderLayout.CENTER);
            
            // Atualizar área de trabalho
            workArea.revalidate();
            workArea.repaint();
            
            // Log de sucesso
            SystemLogger.ui("Tela " + item + " aberta na área de trabalho principal para usuário: " + usuarioAtual);
            
        } catch (Exception e) {
            SystemLogger.error("Erro ao abrir tela na área de trabalho: " + item, e);
            JOptionPane.showMessageDialog(workArea, 
                "❌ Erro ao abrir tela: " + item + "\n\n" + e.getMessage(), 
                "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    /**
     * Cria o conteúdo da tela integrada na área de trabalho principal
     */
    private JPanel criarConteudoTelaIntegrado(String item, String module) {
        JPanel painelPrincipal = new JPanel(new BorderLayout());
        painelPrincipal.setBackground(Color.WHITE);
        
        // Header da tela
        JPanel header = criarHeaderTelaIntegrado(item);
        painelPrincipal.add(header, BorderLayout.NORTH);
        
        // Conteúdo principal
        JPanel conteudo = criarConteudoPrincipalIntegrado(item, module);
        painelPrincipal.add(conteudo, BorderLayout.CENTER);
        
        // Status bar
        JPanel statusBar = criarStatusBarIntegrado(item);
        painelPrincipal.add(statusBar, BorderLayout.SOUTH);
        
        return painelPrincipal;
    }
    
    /**
     * Cria o header da tela integrada
     */
    private JPanel criarHeaderTelaIntegrado(String titulo) {
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(new Color(52, 73, 94));
        header.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));
        header.setPreferredSize(new Dimension(0, 70));
        
        // Título e breadcrumb
        JPanel breadcrumbPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        breadcrumbPanel.setOpaque(false);
        
        JLabel homeLabel = new JLabel("🏠 Início");
        homeLabel.setForeground(Color.WHITE);
        homeLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        homeLabel.setCursor(new Cursor(Cursor.HAND_CURSOR));
        homeLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                voltarTelaInicial();
            }
        });
        
        JLabel separatorLabel = new JLabel(" > ");
        separatorLabel.setForeground(Color.WHITE);
        separatorLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        
        JLabel titleLabel = new JLabel(titulo);
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
        
        breadcrumbPanel.add(homeLabel);
        breadcrumbPanel.add(separatorLabel);
        breadcrumbPanel.add(titleLabel);
        
        // Informações do usuário
        JPanel userPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        userPanel.setOpaque(false);
        
        JLabel usuarioLabel = new JLabel("👤 " + nomeUsuario + " (" + usuarioAtual + ")");
        usuarioLabel.setForeground(Color.WHITE);
        usuarioLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        
        userPanel.add(usuarioLabel);
        
        header.add(breadcrumbPanel, BorderLayout.WEST);
        header.add(userPanel, BorderLayout.EAST);
        
        return header;
    }
    
    /**
     * Cria o conteúdo principal da tela integrada
     */
    private JPanel criarConteudoPrincipalIntegrado(String item, String module) {
        JPanel conteudo = new JPanel(new BorderLayout());
        conteudo.setBackground(Color.WHITE);
        conteudo.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        // Painel de informações
        JPanel infoPanel = new JPanel(new GridLayout(0, 1, 10, 10));
        infoPanel.setBackground(Color.WHITE);
        infoPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(189, 195, 199)),
            BorderFactory.createEmptyBorder(20, 20, 20, 20)
        ));
        
        // Informações do módulo
        String[] informacoes = getInformacoesModulo(item, module);
        for (String info : informacoes) {
            JLabel infoLabel = new JLabel(info);
            infoLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
            infoLabel.setForeground(new Color(73, 80, 87));
            infoPanel.add(infoLabel);
        }
        
        // Painel de ações
        JPanel acoesPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        acoesPanel.setBackground(Color.WHITE);
        acoesPanel.setBorder(BorderFactory.createEmptyBorder(20, 0, 0, 0));
        
        JButton btnNovo = new JButton("➕ Novo");
        btnNovo.setBackground(new Color(46, 204, 113));
        btnNovo.setForeground(Color.WHITE);
        btnNovo.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnNovo.addActionListener(e -> {
            JOptionPane.showMessageDialog(conteudo, 
                "🚀 Funcionalidade 'Novo' em desenvolvimento para " + item, 
                "Novo", JOptionPane.INFORMATION_MESSAGE);
        });
        
        JButton btnEditar = new JButton("📝 Editar");
        btnEditar.setBackground(new Color(52, 152, 219));
        btnEditar.setForeground(Color.WHITE);
        btnEditar.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnEditar.addActionListener(e -> {
            JOptionPane.showMessageDialog(conteudo, 
                "🚀 Funcionalidade 'Editar' em desenvolvimento para " + item, 
                "Editar", JOptionPane.INFORMATION_MESSAGE);
        });
        
        JButton btnExcluir = new JButton("🗑️ Excluir");
        btnExcluir.setBackground(new Color(231, 76, 60));
        btnExcluir.setForeground(Color.WHITE);
        btnExcluir.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnExcluir.addActionListener(e -> {
            JOptionPane.showMessageDialog(conteudo, 
                "🚀 Funcionalidade 'Excluir' em desenvolvimento para " + item, 
                "Excluir", JOptionPane.INFORMATION_MESSAGE);
        });
        
        JButton btnVoltar = new JButton("🔙 Voltar");
        btnVoltar.setBackground(new Color(149, 165, 166));
        btnVoltar.setForeground(Color.WHITE);
        btnVoltar.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnVoltar.addActionListener(e -> {
            voltarTelaInicial();
        });
        
        acoesPanel.add(btnNovo);
        acoesPanel.add(btnEditar);
        acoesPanel.add(btnExcluir);
        acoesPanel.add(btnVoltar);
        
        // Adicionar componentes ao conteúdo
        conteudo.add(infoPanel, BorderLayout.CENTER);
        conteudo.add(acoesPanel, BorderLayout.SOUTH);
        
        return conteudo;
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
            
            // Criar tela inicial
            JLabel welcomeLabel = new JLabel("🎉 Bem-vindo ao Hermes Comercial PDV!", JLabel.CENTER);
            welcomeLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
            welcomeLabel.setForeground(new Color(52, 73, 94));
            welcomeLabel.setBorder(BorderFactory.createEmptyBorder(50, 50, 50, 50));
            
            workArea.add(welcomeLabel, BorderLayout.CENTER);
            workArea.revalidate();
            workArea.repaint();
            
            SystemLogger.ui("Tela inicial restaurada para usuário: " + usuarioAtual);
            
        } catch (Exception e) {
            SystemLogger.error("Erro ao voltar para tela inicial", e);
        }
    }
    
    /**
     * Retorna informações específicas do módulo
     */
    private String[] getInformacoesModulo(String item, String module) {
        switch (module) {
            case "PDV":
                return new String[]{
                    "📊 Módulo: Ponto de Venda",
                    "👤 Usuário: " + nomeUsuario,
                    "🔐 Login: " + usuarioAtual,
                    "🗄️ Banco: PostgreSQL",
                    "📅 Data: " + java.time.LocalDate.now().toString(),
                    "⏰ Hora: " + java.time.LocalTime.now().toString(),
                    "📋 Status: Operacional",
                    "🎯 Função: " + item
                };
            case "VENDAS":
                return new String[]{
                    "💰 Módulo: Vendas",
                    "👤 Usuário: " + nomeUsuario,
                    "🔐 Login: " + usuarioAtual,
                    "🗄️ Banco: PostgreSQL",
                    "📅 Data: " + java.time.LocalDate.now().toString(),
                    "⏰ Hora: " + java.time.LocalTime.now().toString(),
                    "📋 Status: Operacional",
                    "🎯 Função: " + item
                };
            case "PRODUTOS":
                return new String[]{
                    "📦 Módulo: Produtos",
                    "👤 Usuário: " + nomeUsuario,
                    "🔐 Login: " + usuarioAtual,
                    "🗄️ Banco: PostgreSQL",
                    "📅 Data: " + java.time.LocalDate.now().toString(),
                    "⏰ Hora: " + java.time.LocalTime.now().toString(),
                    "📋 Status: Operacional",
                    "🎯 Função: " + item
                };
            case "CLIENTES":
                return new String[]{
                    "👥 Módulo: Clientes",
                    "👤 Usuário: " + nomeUsuario,
                    "🔐 Login: " + usuarioAtual,
                    "🗄️ Banco: PostgreSQL",
                    "📅 Data: " + java.time.LocalDate.now().toString(),
                    "⏰ Hora: " + java.time.LocalTime.now().toString(),
                    "📋 Status: Operacional",
                    "🎯 Função: " + item
                };
            case "ESTOQUE":
                return new String[]{
                    "📊 Módulo: Estoque",
                    "👤 Usuário: " + nomeUsuario,
                    "🔐 Login: " + usuarioAtual,
                    "🗄️ Banco: PostgreSQL",
                    "📅 Data: " + java.time.LocalDate.now().toString(),
                    "⏰ Hora: " + java.time.LocalTime.now().toString(),
                    "📋 Status: Operacional",
                    "🎯 Função: " + item
                };
            case "FINANCEIRO":
                return new String[]{
                    "🏦 Módulo: Financeiro",
                    "👤 Usuário: " + nomeUsuario,
                    "🔐 Login: " + usuarioAtual,
                    "🗄️ Banco: PostgreSQL",
                    "📅 Data: " + java.time.LocalDate.now().toString(),
                    "⏰ Hora: " + java.time.LocalTime.now().toString(),
                    "📋 Status: Operacional",
                    "🎯 Função: " + item
                };
            case "RELATORIOS":
                return new String[]{
                    "📈 Módulo: Relatórios",
                    "👤 Usuário: " + nomeUsuario,
                    "🔐 Login: " + usuarioAtual,
                    "🗄️ Banco: PostgreSQL",
                    "📅 Data: " + java.time.LocalDate.now().toString(),
                    "⏰ Hora: " + java.time.LocalTime.now().toString(),
                    "📋 Status: Operacional",
                    "🎯 Função: " + item
                };
            case "CONFIGURACOES":
                return new String[]{
                    "⚙️ Módulo: Configurações",
                    "👤 Usuário: " + nomeUsuario,
                    "🔐 Login: " + usuarioAtual,
                    "🗄️ Banco: PostgreSQL",
                    "📅 Data: " + java.time.LocalDate.now().toString(),
                    "⏰ Hora: " + java.time.LocalTime.now().toString(),
                    "📋 Status: Operacional",
                    "🎯 Função: " + item
                };
            default:
                return new String[]{
                    "📊 Módulo: " + module,
                    "👤 Usuário: " + nomeUsuario,
                    "🔐 Login: " + usuarioAtual,
                    "🗄️ Banco: PostgreSQL",
                    "📅 Data: " + java.time.LocalDate.now().toString(),
                    "⏰ Hora: " + java.time.LocalTime.now().toString(),
                    "📋 Status: Operacional",
                    "🎯 Função: " + item
                };
        }
    }
    
    /**
     * Cria a status bar integrada
     */
    private JPanel criarStatusBarIntegrado(String item) {
        JPanel statusBar = new JPanel(new BorderLayout());
        statusBar.setBackground(new Color(189, 195, 199));
        statusBar.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        statusBar.setPreferredSize(new Dimension(0, 30));
        
        JLabel statusLabel = new JLabel("🟢 Sistema Online | Módulo: " + item + " | Usuário: " + usuarioAtual + " | " + 
            java.time.LocalDateTime.now().format(java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss")) + 
            " | 🗄️ Banco: PostgreSQL");
        statusLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        statusLabel.setForeground(new Color(52, 73, 94));
        
        statusBar.add(statusLabel, BorderLayout.WEST);
        
        return statusBar;
    }
    
    public JPanel getMenuPanel() {
        return menuPanel;
    }
}
