package com.br.hermescomercial.erp.controller;

import com.br.hermescomercial.ui.layout.LayoutPadrao;
import com.br.hermescomercial.ui.layout.MenuColors;
// import com.br.hermescomercial.pdv.controller.PDVDashboardSwingController; // Removido - usando PDVFormularioDashboard
import javax.swing.*;
import java.awt.*;

/**
 * Controller para menu principal de integração do sistema ERP
 * Versão 2.3.0 - Arquitetura Modular - Tema Padrão Hermes - Design Responsivo
 */
public class ERPMenuPrincipalSwingController {
    
    public JFrame frame;
    public JPanel mainPanel;
    public JLabel lblDataHora;
    public JPanel menuPanel; // Referência para atualização responsiva
    
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
        // Usando header ERP padrão do LayoutPadrao
        return LayoutPadrao.criarHeaderERPSimples("🏢 Hermes Comercial ERP - Sistema Principal");
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
        // Botão Dashboard removido - usar PDVFormularioDashboard integrado ao menu lateral PDV
        // JButton btnDashboard = LayoutPadrao.criarBotaoPrimario(" Dashboard");
        // btnDashboard.addActionListener(e -> abrirDashboard());
        
        categoriasPanel.add(createCategoriaPanel("🏢 OPERAÇÕES PRINCIPAIS", 
            new JButton[]{btnProdutos, btnEstoque, btnFinanceiro}), gbc);
        
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
        
        // Header colorido baseado no setor
        String setor = identificarSetorPorTitulo(titulo);
        JLabel tituloLabel = MenuColors.criarHeaderMenu(setor, titulo);
        tituloLabel.setFont(LayoutPadrao.FONTE_SUBTITULO);
        tituloLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));
        
        categoriaPanel.add(tituloLabel, BorderLayout.NORTH);
        
        // Painel de botões com cores do setor
        JPanel botoesPanel = new JPanel(new GridLayout(0, 2, 10, 10));
        botoesPanel.setOpaque(false);
        
        for (JButton botao : botoes) {
            // Aplicar cores específicas do setor
            JButton botaoColorido = MenuColors.criarBotaoSetor(setor, botao.getText());
            botoesPanel.add(botaoColorido);
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
    
    /**
     * Identifica o setor baseado no título do menu
     * @param titulo Título do menu
     * @return Setor identificado
     */
    private String identificarSetorPorTitulo(String titulo) {
        if (titulo.contains("PRODUTOS")) {
            return "produtos";
        }
        return "erp";
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
        new ERPProdutoSwingController();
    }
    
    private void abrirFinanceiro() {
        new ERPGestaoFinanceiraSwingController();
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
        configuracao.getFrame().setVisible(true);
    }
    
    // Método removido - usar PDVFormularioDashboard integrado ao menu lateral
    // private void abrirDashboard() {
    //     PDVDashboardSwingController dashboard = new PDVDashboardSwingController();
    //     dashboard.show();
    // }
    
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
