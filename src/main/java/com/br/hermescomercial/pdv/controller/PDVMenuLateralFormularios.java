package com.br.hermescomercial.pdv.controller;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import com.br.hermescomercial.util.SystemLogger;

/**
 * Menu Lateral com Submenus em Cascade - Versão com Formulários
 * Telas com formulários específicos para cada módulo
 */
public class PDVMenuLateralFormularios {
    
    private JPanel menuPanel;
    private String usuarioAtual;
    private String nomeUsuario;
    private JPanel workArea; // Área de trabalho principal
    
    public PDVMenuLateralFormularios(String usuario, String nome, JPanel workArea) {
        this.usuarioAtual = usuario;
        this.nomeUsuario = nome;
        this.workArea = workArea;
        SystemLogger.ui("Criando menu lateral com formulários para usuário: " + usuario);
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
            
            SystemLogger.ui("Menu lateral com formulários criado com 8 menus principais");
            return menuPanel;
            
        } catch (Exception e) {
            SystemLogger.error("Erro ao criar menu lateral com formulários", e);
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
            
            // Ação do submenu - ABRIR FORMULÁRIO ESPECÍFICO
            button.addActionListener(e -> {
                try {
                    SystemLogger.operation("SUBMENU_CLICK", module, 
                        "Usuário: " + usuarioAtual + " acessou: " + text);
                    
                    // Abrir formulário específico na área de trabalho principal
                    abrirFormularioEspecifico(text, module);
                        
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
     * Abre o formulário específico na área de trabalho principal
     */
    private void abrirFormularioEspecifico(String item, String module) {
        try {
            SystemLogger.ui("Abrindo formulário específico: " + item);
            
            // Limpar área de trabalho
            workArea.removeAll();
            workArea.revalidate();
            workArea.repaint();
            
            // Criar conteúdo do formulário específico
            JPanel conteudoFormulario = criarFormularioEspecifico(item, module);
            workArea.add(conteudoFormulario, BorderLayout.CENTER);
            
            // Atualizar área de trabalho
            workArea.revalidate();
            workArea.repaint();
            
            // Log de sucesso
            SystemLogger.ui("Formulário " + item + " aberto na área de trabalho principal para usuário: " + usuarioAtual);
            
        } catch (Exception e) {
            SystemLogger.error("Erro ao abrir formulário específico: " + item, e);
            JOptionPane.showMessageDialog(workArea, 
                "❌ Erro ao abrir formulário: " + item + "\n\n" + e.getMessage(), 
                "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    /**
     * Cria o formulário específico para cada item
     */
    private JPanel criarFormularioEspecifico(String item, String module) {
        JPanel painelPrincipal = new JPanel(new BorderLayout());
        painelPrincipal.setBackground(Color.WHITE);
        
        // Header do formulário
        JPanel header = criarHeaderFormulario(item);
        painelPrincipal.add(header, BorderLayout.NORTH);
        
        // Conteúdo do formulário
        JPanel conteudo = criarConteudoFormulario(item, module);
        painelPrincipal.add(conteudo, BorderLayout.CENTER);
        
        // Status bar
        JPanel statusBar = criarStatusBarFormulario(item);
        painelPrincipal.add(statusBar, BorderLayout.SOUTH);
        
        return painelPrincipal;
    }
    
    /**
     * Cria o header do formulário
     */
    private JPanel criarHeaderFormulario(String titulo) {
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
     * Cria o conteúdo do formulário específico
     */
    private JPanel criarConteudoFormulario(String item, String module) {
        JPanel conteudo = new JPanel(new BorderLayout());
        conteudo.setBackground(Color.WHITE);
        conteudo.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        // Painel de formulário
        JPanel formularioPanel = criarFormularioPorItem(item, module);
        
        // Painel de botões
        JPanel botoesPanel = criarPainelBotoes(item);
        
        conteudo.add(formularioPanel, BorderLayout.CENTER);
        conteudo.add(botoesPanel, BorderLayout.SOUTH);
        
        return conteudo;
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
                
            case "🔍 Consultar Vendas":
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
                gbc.gridx = 0; gbc.gridy = 1;
                panel.add(new JLabel("Cliente:"), gbc);
                gbc.gridx = 1; gbc.gridy = 1;
                JTextField txtClienteConsulta = new JTextField(30);
                panel.add(txtClienteConsulta, gbc);
                
                // Status
                gbc.gridx = 0; gbc.gridy = 2;
                panel.add(new JLabel("Status:"), gbc);
                gbc.gridx = 1; gbc.gridy = 2;
                JComboBox<String> cbStatus = new JComboBox<>(new String[]{"Todos", "Abertas", "Fechadas", "Canceladas"});
                panel.add(cbStatus, gbc);
                
                // Botão Consultar
                gbc.gridx = 1; gbc.gridy = 3;
                JButton btnConsultar = new JButton("🔍 Consultar");
                btnConsultar.setBackground(new Color(52, 152, 219));
                btnConsultar.setForeground(Color.WHITE);
                panel.add(btnConsultar, gbc);
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
                
            case "📝 Editar Produto":
                // Código do Produto
                gbc.gridx = 0; gbc.gridy = 0;
                panel.add(new JLabel("Código do Produto:"), gbc);
                gbc.gridx = 1; gbc.gridy = 0;
                JPanel codigoPanel = new JPanel(new FlowLayout());
                codigoPanel.setBackground(Color.WHITE);
                JTextField txtCodigoEdit = new JTextField(15);
                JButton btnBuscar = new JButton("🔍 Buscar");
                btnBuscar.setBackground(new Color(52, 152, 219));
                btnBuscar.setForeground(Color.WHITE);
                codigoPanel.add(txtCodigoEdit);
                codigoPanel.add(btnBuscar);
                panel.add(codigoPanel, gbc);
                
                // Demais campos (similares ao cadastro)
                gbc.gridx = 0; gbc.gridy = 1;
                panel.add(new JLabel("Nome do Produto:"), gbc);
                gbc.gridx = 1; gbc.gridy = 1;
                JTextField txtNomeEdit = new JTextField(40);
                panel.add(txtNomeEdit, gbc);
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
     * Cria painel de botões
     */
    private JPanel criarPainelBotoes(String item) {
        JPanel botoesPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        botoesPanel.setBackground(Color.WHITE);
        botoesPanel.setBorder(BorderFactory.createEmptyBorder(20, 0, 0, 0));
        
        JButton btnSalvar = new JButton("💾 Salvar");
        btnSalvar.setBackground(new Color(46, 204, 113));
        btnSalvar.setForeground(Color.WHITE);
        btnSalvar.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnSalvar.addActionListener(e -> {
            JOptionPane.showMessageDialog(workArea, 
                "🚀 Dados salvos com sucesso para " + item, 
                "Sucesso", JOptionPane.INFORMATION_MESSAGE);
        });
        
        JButton btnLimpar = new JButton("🔄 Limpar");
        btnLimpar.setBackground(new Color(149, 165, 166));
        btnLimpar.setForeground(Color.WHITE);
        btnLimpar.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnLimpar.addActionListener(e -> {
            JOptionPane.showMessageDialog(workArea, 
                "🔄 Formulário limpo", 
                "Limpar", JOptionPane.INFORMATION_MESSAGE);
        });
        
        JButton btnCancelar = new JButton("❌ Cancelar");
        btnCancelar.setBackground(new Color(231, 76, 60));
        btnCancelar.setForeground(Color.WHITE);
        btnCancelar.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnCancelar.addActionListener(e -> {
            voltarTelaInicial();
        });
        
        JButton btnVoltar = new JButton("🔙 Voltar");
        btnVoltar.setBackground(new Color(52, 152, 219));
        btnVoltar.setForeground(Color.WHITE);
        btnVoltar.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnVoltar.addActionListener(e -> {
            voltarTelaInicial();
        });
        
        botoesPanel.add(btnSalvar);
        botoesPanel.add(btnLimpar);
        botoesPanel.add(btnCancelar);
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
     * Cria a status bar do formulário
     */
    private JPanel criarStatusBarFormulario(String item) {
        JPanel statusBar = new JPanel(new BorderLayout());
        statusBar.setBackground(new Color(189, 195, 199));
        statusBar.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        statusBar.setPreferredSize(new Dimension(0, 30));
        
        JLabel statusLabel = new JLabel("🟢 Sistema Online | Formulário: " + item + " | Usuário: " + usuarioAtual + " | " + 
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
