package com.br.hermescomercial.pdv.controller;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.awt.event.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import com.br.hermescomercial.util.SystemLogger;

/**
 * Menu Lateral com Submenus em Cascade - Versão Completa
 * Layout organizado: Header → Busca → Formulário → Tabela
 */
public class PDVMenuLateralCompleto {
    
    private JPanel menuPanel;
    private String usuarioAtual;
    private String nomeUsuario;
    private JPanel workArea; // Área de trabalho principal
    
    public PDVMenuLateralCompleto(String usuario, String nome, JPanel workArea) {
        this.usuarioAtual = usuario;
        this.nomeUsuario = nome;
        this.workArea = workArea;
        SystemLogger.ui("Criando menu lateral completo para usuário: " + usuario);
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
            
            SystemLogger.ui("Menu lateral completo criado com 8 menus principais");
            return menuPanel;
            
        } catch (Exception e) {
            SystemLogger.error("Erro ao criar menu lateral completo", e);
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
            
            // Ação do submenu - ABRIR TELA COMPLETA
            button.addActionListener(e -> {
                try {
                    SystemLogger.operation("SUBMENU_CLICK", module, 
                        "Usuário: " + usuarioAtual + " acessou: " + text);
                    
                    // Abrir tela completa com layout organizado
                    abrirTelaCompleta(text, module);
                        
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
     * Abre a tela completa com layout organizado: Header → Busca → Formulário → Tabela
     */
    private void abrirTelaCompleta(String item, String module) {
        try {
            SystemLogger.ui("Abrindo tela completa: " + item);
            
            // Limpar área de trabalho
            workArea.removeAll();
            workArea.revalidate();
            workArea.repaint();
            
            // Criar conteúdo completo
            JPanel conteudoTela = criarTelaCompleta(item, module);
            workArea.add(conteudoTela, BorderLayout.CENTER);
            
            // Atualizar área de trabalho
            workArea.revalidate();
            workArea.repaint();
            
            // Log de sucesso
            SystemLogger.ui("Tela completa " + item + " aberta na área de trabalho principal para usuário: " + usuarioAtual);
            
        } catch (Exception e) {
            SystemLogger.error("Erro ao abrir tela completa: " + item, e);
            JOptionPane.showMessageDialog(workArea, 
                "❌ Erro ao abrir tela: " + item + "\n\n" + e.getMessage(), 
                "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    /**
     * Cria a tela completa com layout organizado
     */
    private JPanel criarTelaCompleta(String item, String module) {
        JPanel painelPrincipal = new JPanel(new BorderLayout());
        painelPrincipal.setBackground(Color.WHITE);
        
        // Header da tela
        JPanel header = criarHeaderTela(item);
        painelPrincipal.add(header, BorderLayout.NORTH);
        
        // Conteúdo principal com scroll
        JScrollPane scrollPane = new JScrollPane();
        scrollPane.setBorder(null);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        
        JPanel conteudoPrincipal = new JPanel();
        conteudoPrincipal.setLayout(new BoxLayout(conteudoPrincipal, BoxLayout.Y_AXIS));
        conteudoPrincipal.setBackground(Color.WHITE);
        
        // Painel de busca
        JPanel buscaPanel = criarPainelBusca(item, module);
        conteudoPrincipal.add(buscaPanel);
        
        // Painel de formulário
        JPanel formularioPanel = criarPainelFormulario(item, module);
        conteudoPrincipal.add(formularioPanel);
        
        // Painel de tabela
        JPanel tabelaPanel = criarPainelTabela(item, module);
        conteudoPrincipal.add(tabelaPanel);
        
        // Painel de botões
        JPanel botoesPanel = criarPainelBotoes(item);
        conteudoPrincipal.add(botoesPanel);
        
        scrollPane.setViewportView(conteudoPrincipal);
        painelPrincipal.add(scrollPane, BorderLayout.CENTER);
        
        // Status bar
        JPanel statusBar = criarStatusBarTela(item);
        painelPrincipal.add(statusBar, BorderLayout.SOUTH);
        
        return painelPrincipal;
    }
    
    /**
     * Cria o header da tela
     */
    private JPanel criarHeaderTela(String titulo) {
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
     * Cria o painel de busca
     */
    private JPanel criarPainelBusca(String item, String module) {
        JPanel buscaPanel = new JPanel(new BorderLayout());
        buscaPanel.setBackground(Color.WHITE);
        buscaPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(189, 195, 199)),
            BorderFactory.createEmptyBorder(15, 15, 15, 15)
        ));
        
        // Título do painel de busca
        JLabel buscaLabel = new JLabel("🔍 Busca e Filtros");
        buscaLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        buscaLabel.setForeground(new Color(52, 73, 94));
        
        // Campos de busca específicos
        JPanel camposPanel = criarCamposBusca(item, module);
        
        buscaPanel.add(buscaLabel, BorderLayout.NORTH);
        buscaPanel.add(camposPanel, BorderLayout.CENTER);
        
        return buscaPanel;
    }
    
    /**
     * Cria os campos de busca específicos para cada módulo
     */
    private JPanel criarCamposBusca(String item, String module) {
        JPanel camposPanel = new JPanel(new GridBagLayout());
        camposPanel.setBackground(Color.WHITE);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;
        
        switch (module) {
            case "PDV":
                return criarCamposBuscaPDV(gbc);
            case "VENDAS":
                return criarCamposBuscaVendas(gbc);
            case "PRODUTOS":
                return criarCamposBuscaProdutos(gbc);
            case "CLIENTES":
                return criarCamposBuscaClientes(gbc);
            case "ESTOQUE":
                return criarCamposBuscaEstoque(gbc);
            case "FINANCEIRO":
                return criarCamposBuscaFinanceiro(gbc);
            case "RELATORIOS":
                return criarCamposBuscaRelatorios(gbc);
            case "CONFIGURACOES":
                return criarCamposBuscaConfiguracoes(gbc);
            default:
                return criarCamposBuscaPadrao(item, module, gbc);
        }
    }
    
    /**
     * Campos de busca para PDV
     */
    private JPanel criarCamposBuscaPDV(GridBagConstraints gbc) {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(Color.WHITE);
        
        // Código do produto
        gbc.gridx = 0; gbc.gridy = 0;
        panel.add(new JLabel("Código:"), gbc);
        gbc.gridx = 1; gbc.gridy = 0;
        JTextField txtCodigo = new JTextField(15);
        panel.add(txtCodigo, gbc);
        
        // Nome do produto
        gbc.gridx = 2; gbc.gridy = 0;
        panel.add(new JLabel("Nome:"), gbc);
        gbc.gridx = 3; gbc.gridy = 0;
        JTextField txtNome = new JTextField(20);
        panel.add(txtNome, gbc);
        
        // Data
        gbc.gridx = 4; gbc.gridy = 0;
        panel.add(new JLabel("Data:"), gbc);
        gbc.gridx = 5; gbc.gridy = 0;
        JTextField txtData = new JTextField(new SimpleDateFormat("dd/MM/yyyy").format(new Date()));
        panel.add(txtData, gbc);
        
        // Botões de busca
        gbc.gridx = 6; gbc.gridy = 0;
        JButton btnBuscar = new JButton("🔍 Buscar");
        btnBuscar.setBackground(new Color(52, 152, 219));
        btnBuscar.setForeground(Color.WHITE);
        panel.add(btnBuscar, gbc);
        
        gbc.gridx = 7; gbc.gridy = 0;
        JButton btnLimpar = new JButton("🔄 Limpar");
        btnLimpar.setBackground(new Color(149, 165, 166));
        btnLimpar.setForeground(Color.WHITE);
        panel.add(btnLimpar, gbc);
        
        return panel;
    }
    
    /**
     * Campos de busca para Vendas
     */
    private JPanel criarCamposBuscaVendas(GridBagConstraints gbc) {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(Color.WHITE);
        
        // Período
        gbc.gridx = 0; gbc.gridy = 0;
        panel.add(new JLabel("Período:"), gbc);
        gbc.gridx = 1; gbc.gridy = 0;
        JPanel periodoPanel = new JPanel(new FlowLayout());
        periodoPanel.setBackground(Color.WHITE);
        JTextField txtDataInicio = new JTextField(10);
        JTextField txtDataFim = new JTextField(10);
        periodoPanel.add(txtDataInicio);
        periodoPanel.add(new JLabel(" até "));
        periodoPanel.add(txtDataFim);
        panel.add(periodoPanel, gbc);
        
        // Cliente
        gbc.gridx = 2; gbc.gridy = 0;
        panel.add(new JLabel("Cliente:"), gbc);
        gbc.gridx = 3; gbc.gridy = 0;
        JTextField txtCliente = new JTextField(20);
        panel.add(txtCliente, gbc);
        
        // Status
        gbc.gridx = 4; gbc.gridy = 0;
        panel.add(new JLabel("Status:"), gbc);
        gbc.gridx = 5; gbc.gridy = 0;
        JComboBox<String> cbStatus = new JComboBox<>(new String[]{"Todos", "Abertas", "Fechadas", "Canceladas"});
        panel.add(cbStatus, gbc);
        
        // Botões
        gbc.gridx = 6; gbc.gridy = 0;
        JButton btnBuscar = new JButton("🔍 Buscar");
        btnBuscar.setBackground(new Color(52, 152, 219));
        btnBuscar.setForeground(Color.WHITE);
        panel.add(btnBuscar, gbc);
        
        gbc.gridx = 7; gbc.gridy = 0;
        JButton btnLimpar = new JButton("🔄 Limpar");
        btnLimpar.setBackground(new Color(149, 165, 166));
        btnLimpar.setForeground(Color.WHITE);
        panel.add(btnLimpar, gbc);
        
        return panel;
    }
    
    /**
     * Campos de busca para Produtos
     */
    private JPanel criarCamposBuscaProdutos(GridBagConstraints gbc) {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(Color.WHITE);
        
        // Código
        gbc.gridx = 0; gbc.gridy = 0;
        panel.add(new JLabel("Código:"), gbc);
        gbc.gridx = 1; gbc.gridy = 0;
        JTextField txtCodigo = new JTextField(15);
        panel.add(txtCodigo, gbc);
        
        // Nome
        gbc.gridx = 2; gbc.gridy = 0;
        panel.add(new JLabel("Nome:"), gbc);
        gbc.gridx = 3; gbc.gridy = 0;
        JTextField txtNome = new JTextField(25);
        panel.add(txtNome, gbc);
        
        // Categoria
        gbc.gridx = 4; gbc.gridy = 0;
        panel.add(new JLabel("Categoria:"), gbc);
        gbc.gridx = 5; gbc.gridy = 0;
        JComboBox<String> cbCategoria = new JComboBox<>(new String[]{"Todas", "Eletrônicos", "Roupas", "Alimentos", "Móveis"});
        panel.add(cbCategoria, gbc);
        
        // Botões
        gbc.gridx = 6; gbc.gridy = 0;
        JButton btnBuscar = new JButton("🔍 Buscar");
        btnBuscar.setBackground(new Color(52, 152, 219));
        btnBuscar.setForeground(Color.WHITE);
        panel.add(btnBuscar, gbc);
        
        gbc.gridx = 7; gbc.gridy = 0;
        JButton btnLimpar = new JButton("🔄 Limpar");
        btnLimpar.setBackground(new Color(149, 165, 166));
        btnLimpar.setForeground(Color.WHITE);
        panel.add(btnLimpar, gbc);
        
        return panel;
    }
    
    /**
     * Campos de busca para Clientes
     */
    private JPanel criarCamposBuscaClientes(GridBagConstraints gbc) {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(Color.WHITE);
        
        // CPF/CNPJ
        gbc.gridx = 0; gbc.gridy = 0;
        panel.add(new JLabel("CPF/CNPJ:"), gbc);
        gbc.gridx = 1; gbc.gridy = 0;
        JTextField txtCpfCnpj = new JTextField(18);
        panel.add(txtCpfCnpj, gbc);
        
        // Nome
        gbc.gridx = 2; gbc.gridy = 0;
        panel.add(new JLabel("Nome:"), gbc);
        gbc.gridx = 3; gbc.gridy = 0;
        JTextField txtNome = new JTextField(25);
        panel.add(txtNome, gbc);
        
        // Tipo
        gbc.gridx = 4; gbc.gridy = 0;
        panel.add(new JLabel("Tipo:"), gbc);
        gbc.gridx = 5; gbc.gridy = 0;
        JComboBox<String> cbTipo = new JComboBox<>(new String[]{"Todos", "Pessoa Física", "Pessoa Jurídica"});
        panel.add(cbTipo, gbc);
        
        // Botões
        gbc.gridx = 6; gbc.gridy = 0;
        JButton btnBuscar = new JButton("🔍 Buscar");
        btnBuscar.setBackground(new Color(52, 152, 219));
        btnBuscar.setForeground(Color.WHITE);
        panel.add(btnBuscar, gbc);
        
        gbc.gridx = 7; gbc.gridy = 0;
        JButton btnLimpar = new JButton("🔄 Limpar");
        btnLimpar.setBackground(new Color(149, 165, 166));
        btnLimpar.setForeground(Color.WHITE);
        panel.add(btnLimpar, gbc);
        
        return panel;
    }
    
    /**
     * Campos de busca para Estoque
     */
    private JPanel criarCamposBuscaEstoque(GridBagConstraints gbc) {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(Color.WHITE);
        
        // Produto
        gbc.gridx = 0; gbc.gridy = 0;
        panel.add(new JLabel("Produto:"), gbc);
        gbc.gridx = 1; gbc.gridy = 0;
        JTextField txtProduto = new JTextField(25);
        panel.add(txtProduto, gbc);
        
        // Categoria
        gbc.gridx = 2; gbc.gridy = 0;
        panel.add(new JLabel("Categoria:"), gbc);
        gbc.gridx = 3; gbc.gridy = 0;
        JComboBox<String> cbCategoria = new JComboBox<>(new String[]{"Todas", "Eletrônicos", "Roupas", "Alimentos"});
        panel.add(cbCategoria, gbc);
        
        // Estoque
        gbc.gridx = 4; gbc.gridy = 0;
        panel.add(new JLabel("Estoque:"), gbc);
        gbc.gridx = 5; gbc.gridy = 0;
        JComboBox<String> cbEstoque = new JComboBox<>(new String[]{"Todos", "Acima do Mínimo", "Abaixo do Mínimo", "Zerado"});
        panel.add(cbEstoque, gbc);
        
        // Botões
        gbc.gridx = 6; gbc.gridy = 0;
        JButton btnBuscar = new JButton("🔍 Buscar");
        btnBuscar.setBackground(new Color(52, 152, 219));
        btnBuscar.setForeground(Color.WHITE);
        panel.add(btnBuscar, gbc);
        
        gbc.gridx = 7; gbc.gridy = 0;
        JButton btnLimpar = new JButton("🔄 Limpar");
        btnLimpar.setBackground(new Color(149, 165, 166));
        btnLimpar.setForeground(Color.WHITE);
        panel.add(btnLimpar, gbc);
        
        return panel;
    }
    
    /**
     * Campos de busca para Financeiro
     */
    private JPanel criarCamposBuscaFinanceiro(GridBagConstraints gbc) {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(Color.WHITE);
        
        // Período
        gbc.gridx = 0; gbc.gridy = 0;
        panel.add(new JLabel("Período:"), gbc);
        gbc.gridx = 1; gbc.gridy = 0;
        JPanel periodoPanel = new JPanel(new FlowLayout());
        periodoPanel.setBackground(Color.WHITE);
        JTextField txtDataInicio = new JTextField(10);
        JTextField txtDataFim = new JTextField(10);
        periodoPanel.add(txtDataInicio);
        periodoPanel.add(new JLabel(" até "));
        periodoPanel.add(txtDataFim);
        panel.add(periodoPanel, gbc);
        
        // Tipo
        gbc.gridx = 2; gbc.gridy = 0;
        panel.add(new JLabel("Tipo:"), gbc);
        gbc.gridx = 3; gbc.gridy = 0;
        JComboBox<String> cbTipo = new JComboBox<>(new String[]{"Todos", "Contas a Receber", "Contas a Pagar"});
        panel.add(cbTipo, gbc);
        
        // Status
        gbc.gridx = 4; gbc.gridy = 0;
        panel.add(new JLabel("Status:"), gbc);
        gbc.gridx = 5; gbc.gridy = 0;
        JComboBox<String> cbStatus = new JComboBox<>(new String[]{"Todos", "Abertas", "Pagas", "Vencidas"});
        panel.add(cbStatus, gbc);
        
        // Botões
        gbc.gridx = 6; gbc.gridy = 0;
        JButton btnBuscar = new JButton("🔍 Buscar");
        btnBuscar.setBackground(new Color(52, 152, 219));
        btnBuscar.setForeground(Color.WHITE);
        panel.add(btnBuscar, gbc);
        
        gbc.gridx = 7; gbc.gridy = 0;
        JButton btnLimpar = new JButton("🔄 Limpar");
        btnLimpar.setBackground(new Color(149, 165, 166));
        btnLimpar.setForeground(Color.WHITE);
        panel.add(btnLimpar, gbc);
        
        return panel;
    }
    
    /**
     * Campos de busca para Relatórios
     */
    private JPanel criarCamposBuscaRelatorios(GridBagConstraints gbc) {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(Color.WHITE);
        
        // Período
        gbc.gridx = 0; gbc.gridy = 0;
        panel.add(new JLabel("Período:"), gbc);
        gbc.gridx = 1; gbc.gridy = 0;
        JPanel periodoPanel = new JPanel(new FlowLayout());
        periodoPanel.setBackground(Color.WHITE);
        JTextField txtDataInicio = new JTextField(10);
        JTextField txtDataFim = new JTextField(10);
        periodoPanel.add(txtDataInicio);
        periodoPanel.add(new JLabel(" até "));
        periodoPanel.add(txtDataFim);
        panel.add(periodoPanel, gbc);
        
        // Tipo
        gbc.gridx = 2; gbc.gridy = 0;
        panel.add(new JLabel("Tipo:"), gbc);
        gbc.gridx = 3; gbc.gridy = 0;
        JComboBox<String> cbTipo = new JComboBox<>(new String[]{"Todos", "Vendas", "Produtos", "Clientes", "Financeiro"});
        panel.add(cbTipo, gbc);
        
        // Formato
        gbc.gridx = 4; gbc.gridy = 0;
        panel.add(new JLabel("Formato:"), gbc);
        gbc.gridx = 5; gbc.gridy = 0;
        JComboBox<String> cbFormato = new JComboBox<>(new String[]{"PDF", "Excel", "Tela"});
        panel.add(cbFormato, gbc);
        
        // Botões
        gbc.gridx = 6; gbc.gridy = 0;
        JButton btnGerar = new JButton("📊 Gerar");
        btnGerar.setBackground(new Color(46, 204, 113));
        btnGerar.setForeground(Color.WHITE);
        panel.add(btnGerar, gbc);
        
        gbc.gridx = 7; gbc.gridy = 0;
        JButton btnLimpar = new JButton("🔄 Limpar");
        btnLimpar.setBackground(new Color(149, 165, 166));
        btnLimpar.setForeground(Color.WHITE);
        panel.add(btnLimpar, gbc);
        
        return panel;
    }
    
    /**
     * Campos de busca para Configurações
     */
    private JPanel criarCamposBuscaConfiguracoes(GridBagConstraints gbc) {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(Color.WHITE);
        
        // Nome
        gbc.gridx = 0; gbc.gridy = 0;
        panel.add(new JLabel("Nome:"), gbc);
        gbc.gridx = 1; gbc.gridy = 0;
        JTextField txtNome = new JTextField(25);
        panel.add(txtNome, gbc);
        
        // Tipo
        gbc.gridx = 2; gbc.gridy = 0;
        panel.add(new JLabel("Tipo:"), gbc);
        gbc.gridx = 3; gbc.gridy = 0;
        JComboBox<String> cbTipo = new JComboBox<>(new String[]{"Todos", "Usuários", "Empresa", "Sistema", "Impressoras"});
        panel.add(cbTipo, gbc);
        
        // Status
        gbc.gridx = 4; gbc.gridy = 0;
        panel.add(new JLabel("Status:"), gbc);
        gbc.gridx = 5; gbc.gridy = 0;
        JComboBox<String> cbStatus = new JComboBox<>(new String[]{"Todos", "Ativos", "Inativos"});
        panel.add(cbStatus, gbc);
        
        // Botões
        gbc.gridx = 6; gbc.gridy = 0;
        JButton btnBuscar = new JButton("🔍 Buscar");
        btnBuscar.setBackground(new Color(52, 152, 219));
        btnBuscar.setForeground(Color.WHITE);
        panel.add(btnBuscar, gbc);
        
        gbc.gridx = 7; gbc.gridy = 0;
        JButton btnLimpar = new JButton("🔄 Limpar");
        btnLimpar.setBackground(new Color(149, 165, 166));
        btnLimpar.setForeground(Color.WHITE);
        panel.add(btnLimpar, gbc);
        
        return panel;
    }
    
    /**
     * Campos de busca padrão
     */
    private JPanel criarCamposBuscaPadrao(String item, String module, GridBagConstraints gbc) {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(Color.WHITE);
        
        // Campo de busca geral
        gbc.gridx = 0; gbc.gridy = 0;
        panel.add(new JLabel("Buscar:"), gbc);
        gbc.gridx = 1; gbc.gridy = 0;
        JTextField txtBusca = new JTextField(30);
        panel.add(txtBusca, gbc);
        
        // Botões
        gbc.gridx = 2; gbc.gridy = 0;
        JButton btnBuscar = new JButton("🔍 Buscar");
        btnBuscar.setBackground(new Color(52, 152, 219));
        btnBuscar.setForeground(Color.WHITE);
        panel.add(btnBuscar, gbc);
        
        gbc.gridx = 3; gbc.gridy = 0;
        JButton btnLimpar = new JButton("🔄 Limpar");
        btnLimpar.setBackground(new Color(149, 165, 166));
        btnLimpar.setForeground(Color.WHITE);
        panel.add(btnLimpar, gbc);
        
        return panel;
    }
    
    /**
     * Cria o painel de formulário
     */
    private JPanel criarPainelFormulario(String item, String module) {
        JPanel formularioPanel = new JPanel(new BorderLayout());
        formularioPanel.setBackground(Color.WHITE);
        formularioPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(189, 195, 199)),
            BorderFactory.createEmptyBorder(15, 15, 15, 15)
        ));
        
        // Título do painel de formulário
        JLabel formularioLabel = new JLabel("📝 Formulário de Cadastro");
        formularioLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        formularioLabel.setForeground(new Color(52, 73, 94));
        
        // Formulário específico
        JPanel camposPanel = criarFormularioPorItem(item, module);
        
        formularioPanel.add(formularioLabel, BorderLayout.NORTH);
        formularioPanel.add(camposPanel, BorderLayout.CENTER);
        
        return formularioPanel;
    }
    
    /**
     * Cria o formulário específico para cada item
     */
    private JPanel criarFormularioPorItem(String item, String module) {
        switch (module) {
            case "PDV":
                return criarFormularioPDV(item);
            case "VENDAS":
                return criarFormularioVendas(item);
            case "PRODUTOS":
                return criarFormularioProdutos(item);
            case "CLIENTES":
                return criarFormularioClientes(item);
            case "ESTOQUE":
                return criarFormularioEstoque(item);
            case "FINANCEIRO":
                return criarFormularioFinanceiro(item);
            case "RELATORIOS":
                return criarFormularioRelatorios(item);
            case "CONFIGURACOES":
                return criarFormularioConfiguracoes(item);
            default:
                return criarFormularioPadrao(item, module);
        }
    }
    
    /**
     * Formulários PDV
     */
    private JPanel criarFormularioPDV(String item) {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(Color.WHITE);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;
        
        switch (item) {
            case "🛒 Ponto de Venda":
                // Código do produto
                gbc.gridx = 0; gbc.gridy = 0;
                panel.add(new JLabel("Código do Produto:"), gbc);
                gbc.gridx = 1; gbc.gridy = 0;
                JTextField txtCodigo = new JTextField(15);
                panel.add(txtCodigo, gbc);
                
                // Descrição
                gbc.gridx = 0; gbc.gridy = 1;
                panel.add(new JLabel("Descrição:"), gbc);
                gbc.gridx = 1; gbc.gridy = 1;
                JTextField txtDescricao = new JTextField(30);
                txtDescricao.setEditable(false);
                panel.add(txtDescricao, gbc);
                
                // Quantidade
                gbc.gridx = 0; gbc.gridy = 2;
                panel.add(new JLabel("Quantidade:"), gbc);
                gbc.gridx = 1; gbc.gridy = 2;
                JTextField txtQuantidade = new JTextField(10);
                panel.add(txtQuantidade, gbc);
                
                // Valor Unitário
                gbc.gridx = 0; gbc.gridy = 3;
                panel.add(new JLabel("Valor Unitário:"), gbc);
                gbc.gridx = 1; gbc.gridy = 3;
                JTextField txtValorUnitario = new JTextField(15);
                txtValorUnitario.setEditable(false);
                panel.add(txtValorUnitario, gbc);
                
                // Valor Total
                gbc.gridx = 0; gbc.gridy = 4;
                panel.add(new JLabel("Valor Total:"), gbc);
                gbc.gridx = 1; gbc.gridy = 4;
                JTextField txtValorTotal = new JTextField(15);
                txtValorTotal.setEditable(false);
                panel.add(txtValorTotal, gbc);
                
                // Forma de Pagamento
                gbc.gridx = 0; gbc.gridy = 5;
                panel.add(new JLabel("Forma Pagamento:"), gbc);
                gbc.gridx = 1; gbc.gridy = 5;
                JComboBox<String> cbFormaPagamento = new JComboBox<>(new String[]{"Dinheiro", "Cartão Débito", "Cartão Crédito", "PIX"});
                panel.add(cbFormaPagamento, gbc);
                break;
                
            case "💳 Pagamentos":
                // Cliente
                gbc.gridx = 0; gbc.gridy = 0;
                panel.add(new JLabel("Cliente:"), gbc);
                gbc.gridx = 1; gbc.gridy = 0;
                JTextField txtCliente = new JTextField(30);
                panel.add(txtCliente, gbc);
                
                // Valor do Pagamento
                gbc.gridx = 0; gbc.gridy = 1;
                panel.add(new JLabel("Valor:"), gbc);
                gbc.gridx = 1; gbc.gridy = 1;
                JTextField txtValor = new JTextField(15);
                panel.add(txtValor, gbc);
                
                // Forma de Pagamento
                gbc.gridx = 0; gbc.gridy = 2;
                panel.add(new JLabel("Forma Pagamento:"), gbc);
                gbc.gridx = 1; gbc.gridy = 2;
                JComboBox<String> cbPagamento = new JComboBox<>(new String[]{"Dinheiro", "Cartão Débito", "Cartão Crédito", "PIX", "Boleto"});
                panel.add(cbPagamento, gbc);
                
                // Data do Pagamento
                gbc.gridx = 0; gbc.gridy = 3;
                panel.add(new JLabel("Data:"), gbc);
                gbc.gridx = 1; gbc.gridy = 3;
                JTextField txtData = new JTextField(new SimpleDateFormat("dd/MM/yyyy").format(new Date()));
                panel.add(txtData, gbc);
                break;
                
            default:
                return criarFormularioPadrao(item, "PDV");
        }
        
        return panel;
    }
    
    /**
     * Formulários Vendas
     */
    private JPanel criarFormularioVendas(String item) {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(Color.WHITE);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;
        
        switch (item) {
            case "📋 Nova Venda":
                // Cliente
                gbc.gridx = 0; gbc.gridy = 0;
                panel.add(new JLabel("Cliente:"), gbc);
                gbc.gridx = 1; gbc.gridy = 0;
                JTextField txtCliente = new JTextField(30);
                panel.add(txtCliente, gbc);
                
                // Data da Venda
                gbc.gridx = 0; gbc.gridy = 1;
                panel.add(new JLabel("Data:"), gbc);
                gbc.gridx = 1; gbc.gridy = 1;
                JTextField txtData = new JTextField(new SimpleDateFormat("dd/MM/yyyy").format(new Date()));
                panel.add(txtData, gbc);
                
                // Vendedor
                gbc.gridx = 0; gbc.gridy = 2;
                panel.add(new JLabel("Vendedor:"), gbc);
                gbc.gridx = 1; gbc.gridy = 2;
                JTextField txtVendedor = new JTextField(nomeUsuario);
                txtVendedor.setEditable(false);
                panel.add(txtVendedor, gbc);
                
                // Condição de Pagamento
                gbc.gridx = 0; gbc.gridy = 3;
                panel.add(new JLabel("Condição Pagamento:"), gbc);
                gbc.gridx = 1; gbc.gridy = 3;
                JComboBox<String> cbCondicao = new JComboBox<>(new String[]{"À Vista", "30 Dias", "60 Dias", "90 Dias"});
                panel.add(cbCondicao, gbc);
                break;
                
            default:
                return criarFormularioPadrao(item, "VENDAS");
        }
        
        return panel;
    }
    
    /**
     * Formulários Produtos
     */
    private JPanel criarFormularioProdutos(String item) {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(Color.WHITE);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;
        
        switch (item) {
            case "➕ Cadastrar Produto":
                // Código
                gbc.gridx = 0; gbc.gridy = 0;
                panel.add(new JLabel("Código:"), gbc);
                gbc.gridx = 1; gbc.gridy = 0;
                JTextField txtCodigo = new JTextField(15);
                panel.add(txtCodigo, gbc);
                
                // Nome do Produto
                gbc.gridx = 0; gbc.gridy = 1;
                panel.add(new JLabel("Nome do Produto:"), gbc);
                gbc.gridx = 1; gbc.gridy = 1;
                JTextField txtNome = new JTextField(40);
                panel.add(txtNome, gbc);
                
                // Descrição
                gbc.gridx = 0; gbc.gridy = 2;
                panel.add(new JLabel("Descrição:"), gbc);
                gbc.gridx = 1; gbc.gridy = 2;
                JTextArea txtDescricao = new JTextArea(3, 40);
                JScrollPane scrollDescricao = new JScrollPane(txtDescricao);
                panel.add(scrollDescricao, gbc);
                
                // Categoria
                gbc.gridx = 0; gbc.gridy = 3;
                panel.add(new JLabel("Categoria:"), gbc);
                gbc.gridx = 1; gbc.gridy = 3;
                JComboBox<String> cbCategoria = new JComboBox<>(new String[]{"Eletrônicos", "Roupas", "Alimentos", "Móveis", "Outros"});
                panel.add(cbCategoria, gbc);
                
                // Preço de Custo
                gbc.gridx = 0; gbc.gridy = 4;
                panel.add(new JLabel("Preço de Custo:"), gbc);
                gbc.gridx = 1; gbc.gridy = 4;
                JTextField txtPrecoCusto = new JTextField(15);
                panel.add(txtPrecoCusto, gbc);
                
                // Preço de Venda
                gbc.gridx = 0; gbc.gridy = 5;
                panel.add(new JLabel("Preço de Venda:"), gbc);
                gbc.gridx = 1; gbc.gridy = 5;
                JTextField txtPrecoVenda = new JTextField(15);
                panel.add(txtPrecoVenda, gbc);
                
                // Estoque Mínimo
                gbc.gridx = 0; gbc.gridy = 6;
                panel.add(new JLabel("Estoque Mínimo:"), gbc);
                gbc.gridx = 1; gbc.gridy = 6;
                JTextField txtEstoqueMinimo = new JTextField(10);
                panel.add(txtEstoqueMinimo, gbc);
                
                // Estoque Atual
                gbc.gridx = 0; gbc.gridy = 7;
                panel.add(new JLabel("Estoque Atual:"), gbc);
                gbc.gridx = 1; gbc.gridy = 7;
                JTextField txtEstoqueAtual = new JTextField(10);
                panel.add(txtEstoqueAtual, gbc);
                
                // Unidade de Medida
                gbc.gridx = 0; gbc.gridy = 8;
                panel.add(new JLabel("Unidade:"), gbc);
                gbc.gridx = 1; gbc.gridy = 8;
                JComboBox<String> cbUnidade = new JComboBox<>(new String[]{"UN", "KG", "LT", "M", "CX"});
                panel.add(cbUnidade, gbc);
                break;
                
            default:
                return criarFormularioPadrao(item, "PRODUTOS");
        }
        
        return panel;
    }
    
    /**
     * Formulários Clientes
     */
    private JPanel criarFormularioClientes(String item) {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(Color.WHITE);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;
        
        switch (item) {
            case "➕ Novo Cliente":
                // Nome
                gbc.gridx = 0; gbc.gridy = 0;
                panel.add(new JLabel("Nome Completo:"), gbc);
                gbc.gridx = 1; gbc.gridy = 0;
                JTextField txtNome = new JTextField(40);
                panel.add(txtNome, gbc);
                
                // CPF/CNPJ
                gbc.gridx = 0; gbc.gridy = 1;
                panel.add(new JLabel("CPF/CNPJ:"), gbc);
                gbc.gridx = 1; gbc.gridy = 1;
                JTextField txtCpfCnpj = new JTextField(20);
                panel.add(txtCpfCnpj, gbc);
                
                // Tipo de Pessoa
                gbc.gridx = 0; gbc.gridy = 2;
                panel.add(new JLabel("Tipo:"), gbc);
                gbc.gridx = 1; gbc.gridy = 2;
                JComboBox<String> cbTipo = new JComboBox<>(new String[]{"Pessoa Física", "Pessoa Jurídica"});
                panel.add(cbTipo, gbc);
                
                // Email
                gbc.gridx = 0; gbc.gridy = 3;
                panel.add(new JLabel("Email:"), gbc);
                gbc.gridx = 1; gbc.gridy = 3;
                JTextField txtEmail = new JTextField(40);
                panel.add(txtEmail, gbc);
                
                // Telefone
                gbc.gridx = 0; gbc.gridy = 4;
                panel.add(new JLabel("Telefone:"), gbc);
                gbc.gridx = 1; gbc.gridy = 4;
                JTextField txtTelefone = new JTextField(20);
                panel.add(txtTelefone, gbc);
                
                // Endereço
                gbc.gridx = 0; gbc.gridy = 5;
                panel.add(new JLabel("Endereço:"), gbc);
                gbc.gridx = 1; gbc.gridy = 5;
                JTextField txtEndereco = new JTextField(40);
                panel.add(txtEndereco, gbc);
                
                // Bairro
                gbc.gridx = 0; gbc.gridy = 6;
                panel.add(new JLabel("Bairro:"), gbc);
                gbc.gridx = 1; gbc.gridy = 6;
                JTextField txtBairro = new JTextField(30);
                panel.add(txtBairro, gbc);
                
                // Cidade
                gbc.gridx = 0; gbc.gridy = 7;
                panel.add(new JLabel("Cidade:"), gbc);
                gbc.gridx = 1; gbc.gridy = 7;
                JTextField txtCidade = new JTextField(30);
                panel.add(txtCidade, gbc);
                
                // Estado
                gbc.gridx = 0; gbc.gridy = 8;
                panel.add(new JLabel("Estado:"), gbc);
                gbc.gridx = 1; gbc.gridy = 8;
                JComboBox<String> cbEstado = new JComboBox<>(new String[]{"AC", "AL", "AP", "AM", "BA", "CE", "DF", "ES", "GO", "MA", "MT", "MS", "MG", "PA", "PB", "PR", "PE", "PI", "RJ", "RN", "RS", "RO", "RR", "SC", "SP", "SE", "TO"});
                panel.add(cbEstado, gbc);
                
                // CEP
                gbc.gridx = 0; gbc.gridy = 9;
                panel.add(new JLabel("CEP:"), gbc);
                gbc.gridx = 1; gbc.gridy = 9;
                JTextField txtCep = new JTextField(10);
                panel.add(txtCep, gbc);
                break;
                
            default:
                return criarFormularioPadrao(item, "CLIENTES");
        }
        
        return panel;
    }
    
    /**
     * Formulários Estoque
     */
    private JPanel criarFormularioEstoque(String item) {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(Color.WHITE);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;
        
        switch (item) {
            case "➕ Entrada de Mercadoria":
                // Produto
                gbc.gridx = 0; gbc.gridy = 0;
                panel.add(new JLabel("Produto:"), gbc);
                gbc.gridx = 1; gbc.gridy = 0;
                JTextField txtProduto = new JTextField(30);
                panel.add(txtProduto, gbc);
                
                // Quantidade
                gbc.gridx = 0; gbc.gridy = 1;
                panel.add(new JLabel("Quantidade:"), gbc);
                gbc.gridx = 1; gbc.gridy = 1;
                JTextField txtQuantidade = new JTextField(10);
                panel.add(txtQuantidade, gbc);
                
                // Data da Entrada
                gbc.gridx = 0; gbc.gridy = 2;
                panel.add(new JLabel("Data:"), gbc);
                gbc.gridx = 1; gbc.gridy = 2;
                JTextField txtData = new JTextField(new SimpleDateFormat("dd/MM/yyyy").format(new Date()));
                panel.add(txtData, gbc);
                
                // Fornecedor
                gbc.gridx = 0; gbc.gridy = 3;
                panel.add(new JLabel("Fornecedor:"), gbc);
                gbc.gridx = 1; gbc.gridy = 3;
                JTextField txtFornecedor = new JTextField(30);
                panel.add(txtFornecedor, gbc);
                
                // Nota Fiscal
                gbc.gridx = 0; gbc.gridy = 4;
                panel.add(new JLabel("Nota Fiscal:"), gbc);
                gbc.gridx = 1; gbc.gridy = 4;
                JTextField txtNotaFiscal = new JTextField(15);
                panel.add(txtNotaFiscal, gbc);
                break;
                
            default:
                return criarFormularioPadrao(item, "ESTOQUE");
        }
        
        return panel;
    }
    
    /**
     * Formulários Financeiro
     */
    private JPanel criarFormularioFinanceiro(String item) {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(Color.WHITE);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;
        
        switch (item) {
            case "💰 Contas a Receber":
                // Cliente
                gbc.gridx = 0; gbc.gridy = 0;
                panel.add(new JLabel("Cliente:"), gbc);
                gbc.gridx = 1; gbc.gridy = 0;
                JTextField txtCliente = new JTextField(30);
                panel.add(txtCliente, gbc);
                
                // Valor
                gbc.gridx = 0; gbc.gridy = 1;
                panel.add(new JLabel("Valor:"), gbc);
                gbc.gridx = 1; gbc.gridy = 1;
                JTextField txtValor = new JTextField(15);
                panel.add(txtValor, gbc);
                
                // Data de Vencimento
                gbc.gridx = 0; gbc.gridy = 2;
                panel.add(new JLabel("Vencimento:"), gbc);
                gbc.gridx = 1; gbc.gridy = 2;
                JTextField txtVencimento = new JTextField(10);
                panel.add(txtVencimento, gbc);
                
                // Tipo de Documento
                gbc.gridx = 0; gbc.gridy = 3;
                panel.add(new JLabel("Tipo Doc:"), gbc);
                gbc.gridx = 1; gbc.gridy = 3;
                JComboBox<String> cbTipoDoc = new JComboBox<>(new String[]{"Boleto", "Duplicata", "Cheque", "Promissória"});
                panel.add(cbTipoDoc, gbc);
                break;
                
            default:
                return criarFormularioPadrao(item, "FINANCEIRO");
        }
        
        return panel;
    }
    
    /**
     * Formulários Relatórios
     */
    private JPanel criarFormularioRelatorios(String item) {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(Color.WHITE);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;
        
        switch (item) {
            case "📊 Relatório de Vendas":
                // Período
                gbc.gridx = 0; gbc.gridy = 0;
                panel.add(new JLabel("Período:"), gbc);
                gbc.gridx = 1; gbc.gridy = 0;
                JPanel periodoPanel = new JPanel(new FlowLayout());
                periodoPanel.setBackground(Color.WHITE);
                JTextField txtDataInicio = new JTextField(10);
                JTextField txtDataFim = new JTextField(10);
                periodoPanel.add(txtDataInicio);
                periodoPanel.add(new JLabel(" até "));
                periodoPanel.add(txtDataFim);
                panel.add(periodoPanel, gbc);
                
                // Tipo de Relatório
                gbc.gridx = 0; gbc.gridy = 1;
                panel.add(new JLabel("Tipo:"), gbc);
                gbc.gridx = 1; gbc.gridy = 1;
                JComboBox<String> cbTipo = new JComboBox<>(new String[]{"Resumo", "Detalhado", "Por Cliente", "Por Produto"});
                panel.add(cbTipo, gbc);
                
                // Formato
                gbc.gridx = 0; gbc.gridy = 2;
                panel.add(new JLabel("Formato:"), gbc);
                gbc.gridx = 1; gbc.gridy = 2;
                JComboBox<String> cbFormato = new JComboBox<>(new String[]{"PDF", "Excel", "Tela"});
                panel.add(cbFormato, gbc);
                break;
                
            default:
                return criarFormularioPadrao(item, "RELATORIOS");
        }
        
        return panel;
    }
    
    /**
     * Formulários Configurações
     */
    private JPanel criarFormularioConfiguracoes(String item) {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(Color.WHITE);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;
        
        switch (item) {
            case "👤 Usuários e Permissões":
                // Nome do Usuário
                gbc.gridx = 0; gbc.gridy = 0;
                panel.add(new JLabel("Nome:"), gbc);
                gbc.gridx = 1; gbc.gridy = 0;
                JTextField txtNome = new JTextField(30);
                panel.add(txtNome, gbc);
                
                // Login
                gbc.gridx = 0; gbc.gridy = 1;
                panel.add(new JLabel("Login:"), gbc);
                gbc.gridx = 1; gbc.gridy = 1;
                JTextField txtLogin = new JTextField(20);
                panel.add(txtLogin, gbc);
                
                // Senha
                gbc.gridx = 0; gbc.gridy = 2;
                panel.add(new JLabel("Senha:"), gbc);
                gbc.gridx = 1; gbc.gridy = 2;
                JPasswordField txtSenha = new JPasswordField(20);
                panel.add(txtSenha, gbc);
                
                // Perfil
                gbc.gridx = 0; gbc.gridy = 3;
                panel.add(new JLabel("Perfil:"), gbc);
                gbc.gridx = 1; gbc.gridy = 3;
                JComboBox<String> cbPerfil = new JComboBox<>(new String[]{"Administrador", "Gerente", "Operador", "Visualizador"});
                panel.add(cbPerfil, gbc);
                
                // Status
                gbc.gridx = 0; gbc.gridy = 4;
                panel.add(new JLabel("Status:"), gbc);
                gbc.gridx = 1; gbc.gridy = 4;
                JComboBox<String> cbStatus = new JComboBox<>(new String[]{"Ativo", "Inativo"});
                panel.add(cbStatus, gbc);
                break;
                
            default:
                return criarFormularioPadrao(item, "CONFIGURACOES");
        }
        
        return panel;
    }
    
    /**
     * Formulário Padrão
     */
    private JPanel criarFormularioPadrao(String item, String module) {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(Color.WHITE);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;
        
        // Informações básicas
        gbc.gridx = 0; gbc.gridy = 0;
        panel.add(new JLabel("Módulo:"), gbc);
        gbc.gridx = 1; gbc.gridy = 0;
        JTextField txtModulo = new JTextField(module);
        txtModulo.setEditable(false);
        panel.add(txtModulo, gbc);
        
        gbc.gridx = 0; gbc.gridy = 1;
        panel.add(new JLabel("Funcionalidade:"), gbc);
        gbc.gridx = 1; gbc.gridy = 1;
        JTextField txtFuncionalidade = new JTextField(item);
        txtFuncionalidade.setEditable(false);
        panel.add(txtFuncionalidade, gbc);
        
        gbc.gridx = 0; gbc.gridy = 2;
        panel.add(new JLabel("Usuário:"), gbc);
        gbc.gridx = 1; gbc.gridy = 2;
        JTextField txtUsuario = new JTextField(nomeUsuario);
        txtUsuario.setEditable(false);
        panel.add(txtUsuario, gbc);
        
        gbc.gridx = 0; gbc.gridy = 3;
        panel.add(new JLabel("Data:"), gbc);
        gbc.gridx = 1; gbc.gridy = 3;
        JTextField txtData = new JTextField(new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(new Date()));
        txtData.setEditable(false);
        panel.add(txtData, gbc);
        
        return panel;
    }
    
    /**
     * Cria o painel de tabela
     */
    private JPanel criarPainelTabela(String item, String module) {
        JPanel tabelaPanel = new JPanel(new BorderLayout());
        tabelaPanel.setBackground(Color.WHITE);
        tabelaPanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));
        
        // Título da tabela
        JLabel tabelaLabel = new JLabel("📊 Resultados da Consulta");
        tabelaLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        tabelaLabel.setForeground(new Color(52, 73, 94));
        tabelaLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));
        
        // Tabela de dados
        JTable tabela = criarTabelaDados(item, module);
        JScrollPane scrollPane = new JScrollPane(tabela);
        scrollPane.setPreferredSize(new Dimension(0, 300));
        
        tabelaPanel.add(tabelaLabel, BorderLayout.NORTH);
        tabelaPanel.add(scrollPane, BorderLayout.CENTER);
        
        return tabelaPanel;
    }
    
    /**
     * Cria a tabela de dados específica para cada módulo
     */
    private JTable criarTabelaDados(String item, String module) {
        switch (module) {
            case "PDV":
                return criarTabelaPDV(item);
            case "VENDAS":
                return criarTabelaVendas(item);
            case "PRODUTOS":
                return criarTabelaProdutos(item);
            case "CLIENTES":
                return criarTabelaClientes(item);
            case "ESTOQUE":
                return criarTabelaEstoque(item);
            case "FINANCEIRO":
                return criarTabelaFinanceiro(item);
            case "RELATORIOS":
                return criarTabelaRelatorios(item);
            case "CONFIGURACOES":
                return criarTabelaConfiguracoes(item);
            default:
                return criarTabelaPadrao(item, module);
        }
    }
    
    /**
     * Tabela para PDV
     */
    private JTable criarTabelaPDV(String item) {
        String[] colunas = {"ID", "Código", "Produto", "Quantidade", "Valor Unit.", "Valor Total", "Data", "Status"};
        Object[][] dados = {
            {1, "001", "Notebook Dell", 1, "R$ 3.500,00", "R$ 3.500,00", "06/05/2026", "Concluído"},
            {2, "002", "Mouse Wireless", 2, "R$ 89,90", "R$ 179,80", "06/05/2026", "Concluído"},
            {3, "003", "Teclado USB", 1, "R$ 120,00", "R$ 120,00", "06/05/2026", "Pendente"},
            {4, "004", "Monitor 24\"", 1, "R$ 890,00", "R$ 890,00", "05/05/2026", "Concluído"},
            {5, "005", "Webcam HD", 3, "R$ 150,00", "R$ 450,00", "05/05/2026", "Concluído"}
        };
        
        DefaultTableModel model = new DefaultTableModel(dados, colunas);
        JTable tabela = new JTable(model);
        
        // Configurações da tabela
        configurarTabela(tabela);
        
        return tabela;
    }
    
    /**
     * Tabela para Vendas
     */
    private JTable criarTabelaVendas(String item) {
        String[] colunas = {"ID", "Cliente", "Data", "Valor Total", "Vendedor", "Status", "Forma Pagto"};
        Object[][] dados = {
            {1, "João Silva", "06/05/2026", "R$ 3.679,80", "Admin", "Concluída", "Cartão Crédito"},
            {2, "Maria Santos", "06/05/2026", "R$ 1.340,00", "Admin", "Concluída", "Dinheiro"},
            {3, "Pedro Costa", "05/05/2026", "R$ 890,00", "Admin", "Concluída", "PIX"},
            {4, "Ana Oliveira", "05/05/2026", "R$ 450,00", "Admin", "Pendente", "Boleto"},
            {5, "Carlos Souza", "04/05/2026", "R$ 2.100,00", "Admin", "Cancelada", "Cartão Débito"}
        };
        
        DefaultTableModel model = new DefaultTableModel(dados, colunas);
        JTable tabela = new JTable(model);
        
        configurarTabela(tabela);
        
        return tabela;
    }
    
    /**
     * Tabela para Produtos
     */
    private JTable criarTabelaProdutos(String item) {
        String[] colunas = {"ID", "Código", "Nome", "Categoria", "Preço Venda", "Estoque", "Status"};
        Object[][] dados = {
            {1, "001", "Notebook Dell", "Eletrônicos", "R$ 3.500,00", 15, "Ativo"},
            {2, "002", "Mouse Wireless", "Eletrônicos", "R$ 89,90", 45, "Ativo"},
            {3, "003", "Teclado USB", "Eletrônicos", "R$ 120,00", 28, "Ativo"},
            {4, "004", "Monitor 24\"", "Eletrônicos", "R$ 890,00", 8, "Ativo"},
            {5, "005", "Webcam HD", "Eletrônicos", "R$ 150,00", 22, "Inativo"},
            {6, "006", "Camisa Social", "Roupas", "R$ 89,90", 35, "Ativo"},
            {7, "007", "Calça Jeans", "Roupas", "R$ 159,90", 18, "Ativo"}
        };
        
        DefaultTableModel model = new DefaultTableModel(dados, colunas);
        JTable tabela = new JTable(model);
        
        configurarTabela(tabela);
        
        return tabela;
    }
    
    /**
     * Tabela para Clientes
     */
    private JTable criarTabelaClientes(String item) {
        String[] colunas = {"ID", "Nome", "CPF/CNPJ", "Tipo", "Telefone", "Email", "Status"};
        Object[][] dados = {
            {1, "João Silva", "123.456.789-00", "Pessoa Física", "(11) 9876-5432", "joao@email.com", "Ativo"},
            {2, "Maria Santos", "987.654.321-00", "Pessoa Física", "(11) 9123-4567", "maria@email.com", "Ativo"},
            {3, "Pedro Costa", "456.789.123-00", "Pessoa Física", "(11) 9876-1234", "pedro@email.com", "Ativo"},
            {4, "Ana Oliveira", "789.123.456-00", "Pessoa Física", "(11) 9234-5678", "ana@email.com", "Inativo"},
            {5, "Carlos Souza", "12.345.678/0001-90", "Pessoa Jurídica", "(11) 3456-7890", "carlos@empresa.com", "Ativo"},
            {6, "Empresa ABC Ltda", "23.456.789/0001-23", "Pessoa Jurídica", "(11) 4567-8901", "contato@abc.com", "Ativo"}
        };
        
        DefaultTableModel model = new DefaultTableModel(dados, colunas);
        JTable tabela = new JTable(model);
        
        configurarTabela(tabela);
        
        return tabela;
    }
    
    /**
     * Tabela para Estoque
     */
    private JTable criarTabelaEstoque(String item) {
        String[] colunas = {"ID", "Produto", "Código", "Categoria", "Estoque Atual", "Estoque Mínimo", "Status"};
        Object[][] dados = {
            {1, "Notebook Dell", "001", "Eletrônicos", 15, 10, "Normal"},
            {2, "Mouse Wireless", "002", "Eletrônicos", 45, 20, "Normal"},
            {3, "Teclado USB", "003", "Eletrônicos", 28, 15, "Normal"},
            {4, "Monitor 24\"", "004", "Eletrônicos", 8, 10, "Baixo"},
            {5, "Webcam HD", "005", "Eletrônicos", 22, 25, "Baixo"},
            {6, "Camisa Social", "006", "Roupas", 35, 30, "Normal"},
            {7, "Calça Jeans", "007", "Roupas", 18, 20, "Baixo"},
            {8, "Smartphone", "008", "Eletrônicos", 0, 5, "Zerado"}
        };
        
        DefaultTableModel model = new DefaultTableModel(dados, colunas);
        JTable tabela = new JTable(model);
        
        configurarTabela(tabela);
        
        // Colorir linhas baseado no status
        colorirLinhasEstoque(tabela);
        
        return tabela;
    }
    
    /**
     * Tabela para Financeiro
     */
    private JTable criarTabelaFinanceiro(String item) {
        String[] colunas = {"ID", "Tipo", "Cliente/Fornecedor", "Valor", "Vencimento", "Status", "Documento"};
        Object[][] dados = {
            {1, "Contas a Receber", "João Silva", "R$ 3.679,80", "15/05/2026", "Aberta", "Boleto 001"},
            {2, "Contas a Pagar", "Fornecedor ABC", "R$ 1.200,00", "10/05/2026", "Paga", "NF 123"},
            {3, "Contas a Receber", "Maria Santos", "R$ 1.340,00", "20/05/2026", "Aberta", "Boleto 002"},
            {4, "Contas a Pagar", "Fornecedor XYZ", "R$ 890,00", "05/05/2026", "Vencida", "NF 124"},
            {5, "Contas a Receber", "Pedro Costa", "R$ 2.100,00", "08/05/2026", "Vencida", "Boleto 003"}
        };
        
        DefaultTableModel model = new DefaultTableModel(dados, colunas);
        JTable tabela = new JTable(model);
        
        configurarTabela(tabela);
        
        return tabela;
    }
    
    /**
     * Tabela para Relatórios
     */
    private JTable criarTabelaRelatorios(String item) {
        String[] colunas = {"ID", "Nome Relatório", "Tipo", "Data Geração", "Formato", "Status", "Usuário"};
        Object[][] dados = {
            {1, "Relatório de Vendas Diário", "Vendas", "06/05/2026", "PDF", "Concluído", "Admin"},
            {2, "Relatório de Estoque", "Estoque", "05/05/2026", "Excel", "Concluído", "Admin"},
            {3, "Relatório de Clientes", "Clientes", "05/05/2026", "PDF", "Concluído", "Admin"},
            {4, "Dashboard Financeiro", "Financeiro", "04/05/2026", "Tela", "Concluído", "Admin"},
            {5, "Relatório de Produtos", "Produtos", "03/05/2026", "PDF", "Erro", "Admin"}
        };
        
        DefaultTableModel model = new DefaultTableModel(dados, colunas);
        JTable tabela = new JTable(model);
        
        configurarTabela(tabela);
        
        return tabela;
    }
    
    /**
     * Tabela para Configurações
     */
    private JTable criarTabelaConfiguracoes(String item) {
        String[] colunas = {"ID", "Nome", "Tipo", "Valor", "Status", "Data Alteração", "Usuário"};
        Object[][] dados = {
            {1, "Nome da Empresa", "Empresa", "Hermes Comercial", "Ativo", "06/05/2026", "Admin"},
            {2, "CNPJ", "Empresa", "12.345.678/0001-90", "Ativo", "06/05/2026", "Admin"},
            {3, "Impressora Padrão", "Impressoras", "HP LaserJet", "Ativo", "05/05/2026", "Admin"},
            {4, "Admin", "Usuários", "Administrador", "Ativo", "04/05/2026", "Admin"},
            {5, "Operador", "Usuários", "Operador", "Ativo", "04/05/2026", "Admin"},
            {6, "Backup Diário", "Sistema", "22:00", "Ativo", "03/05/2026", "Admin"}
        };
        
        DefaultTableModel model = new DefaultTableModel(dados, colunas);
        JTable tabela = new JTable(model);
        
        configurarTabela(tabela);
        
        return tabela;
    }
    
    /**
     * Tabela padrão
     */
    private JTable criarTabelaPadrao(String item, String module) {
        String[] colunas = {"ID", "Descrição", "Valor", "Data", "Status"};
        Object[][] dados = {
            {1, "Item 1", "R$ 100,00", "06/05/2026", "Ativo"},
            {2, "Item 2", "R$ 200,00", "05/05/2026", "Inativo"},
            {3, "Item 3", "R$ 150,00", "04/05/2026", "Ativo"}
        };
        
        DefaultTableModel model = new DefaultTableModel(dados, colunas);
        JTable tabela = new JTable(model);
        
        configurarTabela(tabela);
        
        return tabela;
    }
    
    /**
     * Configurações padrão para todas as tabelas
     */
    private void configurarTabela(JTable tabela) {
        // Configurações da tabela
        tabela.setRowHeight(25);
        tabela.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tabela.getTableHeader().setReorderingAllowed(false);
        tabela.getTableHeader().setResizingAllowed(true);
        
        // Configurações do header
        JTableHeader header = tabela.getTableHeader();
        header.setFont(new Font("Segoe UI", Font.BOLD, 12));
        header.setBackground(new Color(52, 73, 94));
        header.setForeground(Color.WHITE);
        header.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        
        // Configurações das células
        tabela.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        tabela.setGridColor(new Color(189, 195, 199));
        tabela.setShowHorizontalLines(true);
        tabela.setShowVerticalLines(true);
        
        // Alternar cores das linhas
        tabela.setDefaultRenderer(Object.class, new javax.swing.table.DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                
                if (!isSelected) {
                    if (row % 2 == 0) {
                        c.setBackground(Color.WHITE);
                    } else {
                        c.setBackground(new Color(248, 249, 250));
                    }
                } else {
                    c.setBackground(new Color(52, 152, 219));
                    c.setForeground(Color.WHITE);
                }
                
                return c;
            }
        });
    }
    
    /**
     * Colorir linhas da tabela de estoque baseado no status
     */
    private void colorirLinhasEstoque(JTable tabela) {
        tabela.setDefaultRenderer(Object.class, new javax.swing.table.DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                
                if (!isSelected) {
                    String status = (String) table.getValueAt(row, 6); // Coluna Status
                    
                    switch (status) {
                        case "Normal":
                            c.setBackground(row % 2 == 0 ? Color.WHITE : new Color(248, 249, 250));
                            break;
                        case "Baixo":
                            c.setBackground(new Color(255, 235, 59)); // Amarelo
                            break;
                        case "Zerado":
                            c.setBackground(new Color(255, 152, 0)); // Laranja
                            break;
                    }
                    
                    c.setForeground(Color.BLACK);
                } else {
                    c.setBackground(new Color(52, 152, 219));
                    c.setForeground(Color.WHITE);
                }
                
                return c;
            }
        });
    }
    
    /**
     * Cria o painel de botões
     */
    private JPanel criarPainelBotoes(String item) {
        JPanel botoesPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        botoesPanel.setBackground(Color.WHITE);
        botoesPanel.setBorder(BorderFactory.createEmptyBorder(20, 0, 0, 0));
        
        JButton btnNovo = new JButton("➕ Novo");
        btnNovo.setBackground(new Color(46, 204, 113));
        btnNovo.setForeground(Color.WHITE);
        btnNovo.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnNovo.addActionListener(e -> {
            JOptionPane.showMessageDialog(workArea, 
                "🚀 Funcionalidade 'Novo' em desenvolvimento para " + item, 
                "Novo", JOptionPane.INFORMATION_MESSAGE);
        });
        
        JButton btnEditar = new JButton("📝 Editar");
        btnEditar.setBackground(new Color(52, 152, 219));
        btnEditar.setForeground(Color.WHITE);
        btnEditar.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnEditar.addActionListener(e -> {
            JOptionPane.showMessageDialog(workArea, 
                "🚀 Funcionalidade 'Editar' em desenvolvimento para " + item, 
                "Editar", JOptionPane.INFORMATION_MESSAGE);
        });
        
        JButton btnExcluir = new JButton("🗑️ Excluir");
        btnExcluir.setBackground(new Color(231, 76, 60));
        btnExcluir.setForeground(Color.WHITE);
        btnExcluir.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnExcluir.addActionListener(e -> {
            JOptionPane.showMessageDialog(workArea, 
                "🚀 Funcionalidade 'Excluir' em desenvolvimento para " + item, 
                "Excluir", JOptionPane.INFORMATION_MESSAGE);
        });
        
        JButton btnExportar = new JButton("📊 Exportar");
        btnExportar.setBackground(new Color(155, 89, 182));
        btnExportar.setForeground(Color.WHITE);
        btnExportar.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnExportar.addActionListener(e -> {
            JOptionPane.showMessageDialog(workArea, 
                "🚀 Funcionalidade 'Exportar' em desenvolvimento para " + item, 
                "Exportar", JOptionPane.INFORMATION_MESSAGE);
        });
        
        JButton btnImprimir = new JButton("🖨️ Imprimir");
        btnImprimir.setBackground(new Color(230, 126, 34));
        btnImprimir.setForeground(Color.WHITE);
        btnImprimir.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnImprimir.addActionListener(e -> {
            JOptionPane.showMessageDialog(workArea, 
                "🚀 Funcionalidade 'Imprimir' em desenvolvimento para " + item, 
                "Imprimir", JOptionPane.INFORMATION_MESSAGE);
        });
        
        JButton btnSalvar = new JButton("💾 Salvar");
        btnSalvar.setBackground(new Color(52, 73, 94));
        btnSalvar.setForeground(Color.WHITE);
        btnSalvar.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnSalvar.addActionListener(e -> {
            JOptionPane.showMessageDialog(workArea, 
                "🚀 Dados salvos com sucesso para " + item, 
                "Sucesso", JOptionPane.INFORMATION_MESSAGE);
        });
        
        JButton btnVoltar = new JButton("🔙 Voltar");
        btnVoltar.setBackground(new Color(149, 165, 166));
        btnVoltar.setForeground(Color.WHITE);
        btnVoltar.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnVoltar.addActionListener(e -> {
            voltarTelaInicial();
        });
        
        botoesPanel.add(btnNovo);
        botoesPanel.add(btnEditar);
        botoesPanel.add(btnExcluir);
        botoesPanel.add(btnExportar);
        botoesPanel.add(btnImprimir);
        botoesPanel.add(btnSalvar);
        botoesPanel.add(btnVoltar);
        
        return botoesPanel;
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
     * Cria a status bar da tela
     */
    private JPanel criarStatusBarTela(String item) {
        JPanel statusBar = new JPanel(new BorderLayout());
        statusBar.setBackground(new Color(189, 195, 199));
        statusBar.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        statusBar.setPreferredSize(new Dimension(0, 30));
        
        JLabel statusLabel = new JLabel("🟢 Sistema Online | Tela: " + item + " | Usuário: " + usuarioAtual + " | " + 
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
