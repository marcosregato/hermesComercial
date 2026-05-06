package com.br.hermescomercial.pdv.controller;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import com.br.hermescomercial.util.SystemLogger;

/**
 * Menu Lateral com Submenus em Cascade - Versão Final Corrigida
 * Problema resolvido: nomes dos setores não aparecendo
 */
public class PDVMenuLateralFinal {
    
    private JPanel menuPanel;
    private String usuarioAtual;
    private String nomeUsuario;
    
    public PDVMenuLateralFinal(String usuario, String nome) {
        this.usuarioAtual = usuario;
        this.nomeUsuario = nome;
        SystemLogger.ui("Criando menu lateral final corrigido para usuário: " + usuario);
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
            
            SystemLogger.ui("Menu lateral final corrigido criado com 8 menus principais");
            return menuPanel;
            
        } catch (Exception e) {
            SystemLogger.error("Erro ao criar menu lateral final", e);
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
            menuPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, menuPanel.getPreferredSize().height));
            
            // Botão principal do menu - FORÇAR EXIBIÇÃO DO TEXTO
            JButton mainButton = new JButton(menuTitle);
            mainButton.setBackground(Color.WHITE);
            mainButton.setForeground(new Color(52, 73, 94));
            mainButton.setFont(new Font("Segoe UI", Font.BOLD, 14));
            mainButton.setHorizontalAlignment(SwingConstants.LEFT);
            mainButton.setBorderPainted(false);
            mainButton.setFocusPainted(false);
            mainButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
            mainButton.setText(menuTitle); // FORÇAR EXIBIÇÃO DO TEXTO
            
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
            
            // Ação do botão principal (toggle submenu)
            mainButton.addActionListener(e -> {
                try {
                    boolean isVisible = subMenuPanel.isVisible();
                    subMenuPanel.setVisible(!isVisible);
                    
                    // Atualizar ícone - MANTER TEXTO VISÍVEL
                    if (isVisible) {
                        mainButton.setText(menuTitle.replace("▼", "▶"));
                    } else {
                        mainButton.setText(menuTitle.replace("▶", "▼"));
                    }
                    
                    // Log da ação
                    SystemLogger.operation("MENU_TOGGLE", module, 
                        "Usuário: " + usuarioAtual + " toggle menu: " + menuTitle);
                    
                    // Revalidar e repaint
                    SwingUtilities.invokeLater(() -> {
                        menuPanel.revalidate();
                        menuPanel.repaint();
                        parent.revalidate();
                        parent.repaint();
                    });
                    
                } catch (Exception ex) {
                    SystemLogger.error("Erro ao toggle menu: " + menuTitle, ex);
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
            button.setMaximumSize(new Dimension(Integer.MAX_VALUE, button.getPreferredSize().height));
            button.setText(text); // FORÇAR EXIBIÇÃO DO TEXTO
            
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
            
            // Ação do submenu
            button.addActionListener(e -> {
                try {
                    SystemLogger.operation("SUBMENU_CLICK", module, 
                        "Usuário: " + usuarioAtual + " acessou: " + text);
                    
                    // Exibir mensagem de exemplo
                    JOptionPane.showMessageDialog(menuPanel, 
                        "🚀 Módulo: " + text + "\n\n" +
                        "✅ Sistema funcionando!\n" +
                        "👤 Usuário: " + nomeUsuario + "\n" +
                        "🔐 Login: " + usuarioAtual + "\n" +
                        "🗄️  Banco: PostgreSQL\n" +
                        "📊 Status: Operacional", 
                        text, JOptionPane.INFORMATION_MESSAGE);
                        
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
    
    public JPanel getMenuPanel() {
        return menuPanel;
    }
}
