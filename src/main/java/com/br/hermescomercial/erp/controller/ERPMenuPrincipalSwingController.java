package com.br.hermescomercial.erp.controller;

import com.br.hermescomercial.ui.layout.LayoutPadrao;
import com.br.hermescomercial.pdv.controller.PDVDashboardSwingController;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

/**
 * Controller para menu principal de integração do sistema ERP
 * Versão 2.3.0 - Arquitetura Modular - Tema Padrão Hermes - Design Responsivo
 */
public class ERPMenuPrincipalSwingController {
    
    private JFrame frame;
    private JPanel mainPanel;
    private JLabel lblUsuario, lblDataHora;
    private JPanel menuPanel; // Referência para atualização responsiva
    
    public ERPMenuPrincipalSwingController() {
        inicializarUI();
    }
    
    private void inicializarUI() {
        frame = new JFrame("🏢 Hermes Comercial ERP - Menu Principal");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        
        // Configuração responsiva
        frame.setMinimumSize(new Dimension(800, 600));
        frame.setPreferredSize(new Dimension(1000, 700));
        frame.setSize(1000, 700);
        frame.setLocationRelativeTo(null);
        
        // Habilitar redimensionamento responsivo
        frame.addComponentListener(new java.awt.event.ComponentAdapter() {
            @Override
            public void componentResized(java.awt.event.ComponentEvent evt) {
                ajustarLayoutResponsivo();
            }
        });
        
        // Aplicar tema padrão
        frame.getContentPane().setBackground(LayoutPadrao.COR_FUNDO_ESCURO);
        
        mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        mainPanel.setBackground(LayoutPadrao.COR_FUNDO_ESCURO);
        
        frame.add(mainPanel);
        mainPanel.add(createHeaderPanel(), BorderLayout.NORTH);
        menuPanel = createMenuPanel();
        mainPanel.add(menuPanel, BorderLayout.CENTER);
        mainPanel.add(createFooterPanel(), BorderLayout.SOUTH);
        
        atualizarDataHora();
    }
    
    private void ajustarLayoutResponsivo() {
        // Ajustar layout baseado no tamanho da janela
        Dimension size = frame.getSize();
        int width = size.width;
        
        // Ajustar margens baseado no tamanho
        int margem = Math.max(10, Math.min(30, width / 40));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(margem, margem, margem, margem));
        
        // Recrear painel do menu com layout responsivo
        mainPanel.remove(menuPanel);
        menuPanel = createMenuPanel();
        mainPanel.add(menuPanel, BorderLayout.CENTER);
        
        mainPanel.revalidate();
        mainPanel.repaint();
    }
    
    private JPanel createHeaderPanel() {
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setOpaque(false);
        headerPanel.setBorder(BorderFactory.createEmptyBorder(0, 10, 10, 10));
        
        // Título principal
        JPanel titlePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        titlePanel.setOpaque(false);
        
        JLabel titleLabel = new JLabel("🏢 Hermes Comercial ERP");
        titleLabel.setFont(LayoutPadrao.FONTE_TITULO);
        titleLabel.setForeground(LayoutPadrao.COR_PRIMARIA);
        
        JLabel subtitleLabel = new JLabel("Sistema Integrado de Gestão Empresarial");
        subtitleLabel.setFont(LayoutPadrao.FONTE_TEXTO);
        subtitleLabel.setForeground(LayoutPadrao.COR_TEXTO_CLARO);
        
        titlePanel.add(titleLabel);
        
        // Painel do usuário
        JPanel userPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        userPanel.setOpaque(false);
        
        lblUsuario = new JLabel("👤 Administrador");
        lblUsuario.setFont(LayoutPadrao.FONTE_TEXTO);
        lblUsuario.setForeground(LayoutPadrao.COR_PRIMARIA);
        
        userPanel.add(lblUsuario);
        
        headerPanel.add(titlePanel, BorderLayout.WEST);
        headerPanel.add(userPanel, BorderLayout.EAST);
        
        JPanel subtitleContainer = new JPanel(new FlowLayout(FlowLayout.LEFT));
        subtitleContainer.setOpaque(false);
        subtitleContainer.add(subtitleLabel);
        
        JPanel completeHeader = new JPanel(new BorderLayout());
        completeHeader.setOpaque(false);
        completeHeader.add(headerPanel, BorderLayout.NORTH);
        completeHeader.add(subtitleContainer, BorderLayout.CENTER);
        
        return completeHeader;
    }
    
    private JPanel createMenuPanel() {
        // Painel principal com layout vertical para categorias
        JPanel menuPanel = new JPanel(new BorderLayout());
        menuPanel.setOpaque(false);
        
        // Criar categorias organizadas
        JPanel categoriasPanel = new JPanel(new GridBagLayout());
        categoriasPanel.setOpaque(false);
        GridBagConstraints gbc = new GridBagConstraints();
        
        // Categoria: Operações Principais
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2; gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(0, 0, 20, 0);
        JButton btnProdutos = LayoutPadrao.criarBotaoPrimario("🏢 Gestão de Produtos");
        btnProdutos.addActionListener(e -> abrirProdutos());
        JButton btnEstoque = LayoutPadrao.criarBotaoAlerta("📊 Gestão de Estoque");
        btnEstoque.addActionListener(e -> abrirEstoque());
        JButton btnFinanceiro = LayoutPadrao.criarBotaoSucesso("💰 Gestão Financeira");
        btnFinanceiro.addActionListener(e -> abrirFinanceiro());
        JButton btnDashboard = LayoutPadrao.criarBotaoPrimario(" Dashboard");
        btnDashboard.addActionListener(e -> abrirDashboard());
        
        categoriasPanel.add(createCategoriaPanel("🏢 OPERAÇÕES PRINCIPAIS", 
            new JButton[]{btnProdutos, btnEstoque, btnFinanceiro, btnDashboard}), gbc);
        
        // Categoria: Administração
        gbc.gridy = 1;
        JButton btnUsuarios = LayoutPadrao.criarBotaoSecundario("👥 Gestão de Usuários");
        btnUsuarios.addActionListener(e -> abrirUsuarios());
        JButton btnConfiguracoes = LayoutPadrao.criarBotaoPerigo("⚙️ Configurações");
        btnConfiguracoes.addActionListener(e -> abrirConfiguracao());
        
        categoriasPanel.add(createCategoriaPanel("⚙️ ADMINISTRAÇÃO", 
            new JButton[]{btnUsuarios, btnConfiguracoes}), gbc);
        
        // Categoria: Relatórios
        gbc.gridy = 2;
        JButton btnRelatorios = LayoutPadrao.criarBotaoAlerta("📊 Relatórios");
        btnRelatorios.addActionListener(e -> abrirRelatorios());
        JButton btnRelatorioFinanceiro = LayoutPadrao.criarBotaoPrimario("📈 Relatório Financeiro");
        btnRelatorioFinanceiro.addActionListener(e -> abrirRelatorioFinanceiro());
        
        categoriasPanel.add(createCategoriaPanel("📊 RELATÓRIOS E ANÁLISES", 
            new JButton[]{btnRelatorios, btnRelatorioFinanceiro}), gbc);
        
        menuPanel.add(categoriasPanel, BorderLayout.CENTER);
        
        return menuPanel;
    }
    
    private JPanel createCategoriaPanel(String titulo, JButton[] botoes) {
        JPanel categoriaPanel = new JPanel(new BorderLayout());
        categoriaPanel.setOpaque(false);
        
        // Título da categoria
        JLabel tituloLabel = new JLabel(titulo);
        tituloLabel.setFont(LayoutPadrao.FONTE_SUBTITULO);
        tituloLabel.setForeground(LayoutPadrao.COR_PRIMARIA);
        tituloLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));
        
        // Painel de botões da categoria
        JPanel botoesPanel = new JPanel(new GridBagLayout());
        botoesPanel.setOpaque(false);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        
        // Layout responsivo baseado no tamanho da tela
        Dimension size = frame.getSize();
        int width = size.width;
        
        if (width < 900) {
            // Layout 1xN para telas pequenas
            for (int i = 0; i < botoes.length; i++) {
                gbc.gridx = 0; gbc.gridy = i;
                botoesPanel.add(botoes[i], gbc);
            }
        } else if (width < 1200) {
            // Layout 2xN para telas médias
            for (int i = 0; i < botoes.length; i++) {
                gbc.gridx = i % 2; gbc.gridy = i / 2;
                botoesPanel.add(botoes[i], gbc);
            }
        } else {
            // Layout 3xN para telas grandes
            for (int i = 0; i < botoes.length; i++) {
                gbc.gridx = i % 3; gbc.gridy = i / 3;
                botoesPanel.add(botoes[i], gbc);
            }
        }
        
        // Painel com borda e fundo sutil
        JPanel contentPanel = new JPanel(new BorderLayout());
        contentPanel.setOpaque(false);
        contentPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(LayoutPadrao.COR_BORDA, 1),
            BorderFactory.createEmptyBorder(15, 15, 15, 15)
        ));
        contentPanel.setBackground(LayoutPadrao.COR_FUNDO);
        
        contentPanel.add(tituloLabel, BorderLayout.NORTH);
        contentPanel.add(botoesPanel, BorderLayout.CENTER);
        
        categoriaPanel.add(contentPanel, BorderLayout.CENTER);
        
        return categoriaPanel;
    }
    
    
    private JPanel createFooterPanel() {
        JPanel footerPanel = new JPanel(new BorderLayout());
        footerPanel.setOpaque(false);
        footerPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 0, 10));
        
        JPanel dateTimePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        dateTimePanel.setOpaque(false);
        
        lblDataHora = new JLabel("📅 " + java.time.LocalDateTime.now().format(
            java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss")));
        lblDataHora.setFont(LayoutPadrao.FONTE_PEQUENA);
        lblDataHora.setForeground(LayoutPadrao.COR_TEXTO_CLARO);
        
        dateTimePanel.add(lblDataHora);
        
        JPanel systemPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        systemPanel.setOpaque(false);
        
        JButton btnSair = LayoutPadrao.criarBotaoPerigo("🚪 Sair");
        btnSair.addActionListener(e -> sairSistema());
        JButton btnSobre = LayoutPadrao.criarBotaoSecundario("ℹ️ Sobre");
        btnSobre.addActionListener(e -> mostrarSobre());
        
        systemPanel.add(btnSobre);
        systemPanel.add(btnSair);
        
        footerPanel.add(dateTimePanel, BorderLayout.WEST);
        footerPanel.add(systemPanel, BorderLayout.EAST);
        
        return footerPanel;
    }
    
    
    private void atualizarDataHora() {
        javax.swing.Timer timer = new javax.swing.Timer(1000, e -> {
            lblDataHora.setText("📅 " + java.time.LocalDateTime.now().format(
                java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss")));
        });
        timer.start();
    }
    
    private void abrirProdutos() {
        ERPProdutoSwingController produtos = new ERPProdutoSwingController();
        produtos.show();
    }
    
    private void abrirFinanceiro() {
        ERPFinanceiroSwingController financeiro = new ERPFinanceiroSwingController();
        financeiro.show();
    }
    
    private void abrirRelatorios() {
        ERPRelatorioSwingController relatorios = new ERPRelatorioSwingController();
        relatorios.show();
    }
    
    private void abrirUsuarios() {
        ERPUsuarioSwingController usuarios = new ERPUsuarioSwingController();
        usuarios.showFrame();
    }
    
    private void abrirConfiguracao() {
        ERPConfiguracaoSwingController configuracao = new ERPConfiguracaoSwingController();
        configuracao.showFrame();
    }
    
    private void abrirDashboard() {
        PDVDashboardSwingController dashboard = new PDVDashboardSwingController();
        dashboard.show();
    }
    
    private void abrirEstoque() {
        new ERPEstoqueSwingController();
    }
    
    private void abrirRelatorioFinanceiro() {
        new ERPRelatorioFinanceiroSwingController();
    }
    
    private void sairSistema() {
        int opcao = JOptionPane.showConfirmDialog(frame,
            "🚪 Deseja realmente sair do sistema ERP?",
            "Confirmar Saída",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.QUESTION_MESSAGE);
            
        if (opcao == JOptionPane.YES_OPTION) {
            frame.dispose();
        }
    }
    
    private void mostrarSobre() {
        JOptionPane.showMessageDialog(frame,
            "🏢 Hermes Comercial ERP v2.3.0\n\n" +
            "Sistema Integrado de Gestão Empresarial\n" +
            "Design Responsivo e Tema Padrão Hermes\n\n" +
            "Módulos Disponíveis:\n" +
            "• 📦 Gestão de Produtos\n" +
            "• 💰 Gestão Financeira\n" +
            "• 📊 Relatórios\n" +
            "• 👥 Gestão de Usuários\n" +
            "• ⚙️ Configurações\n" +
            "• 📈 Dashboard\n\n" +
            "© 2026 Hermes Comercial - Todos os direitos reservados",
            "Sobre o Sistema",
            JOptionPane.INFORMATION_MESSAGE);
    }
    
    public void show() {
        frame.setVisible(true);
        frame.toFront();
    }
}
