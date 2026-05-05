package com.br.hermescomercial.erp.controller;

import com.br.hermescomercial.ui.layout.LayoutPadrao;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

/**
 * Controller para tela de gestão de clientes no ERP
 * Versão 2.3.0 - Arquitetura Modular
 */
public class ERPClienteSwingController {
    
    private JFrame frame;
    
    public ERPClienteSwingController() {
        initializeUI();
    }
    
    private void initializeUI() {
        frame = new JFrame("👥 Gestão de Clientes v2.8.3 - LayoutPadrao");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setSize(1000, 700);
        frame.setLocationRelativeTo(null);
        
        // Configurar fundo com LayoutPadrao
        frame.getContentPane().setBackground(LayoutPadrao.COR_FUNDO_ESCURO);
        
        createMainPanel();
        frame.setVisible(true);
    }
    
    private void createMainPanel() {
        // Criar painéis de formulário e tabela
        JPanel formularioPanel = createFormularioPanel();
        JPanel tabelaPanel = createTabelaPanel();
        
        // Usando layout padrão Header → Busca → Formulário → Tabela
        JPanel mainPanel = LayoutPadrao.criarLayoutPadraoGestao(
            false, // isPDV (false para ERP)
            "👥 Gestão de Clientes - ERP",
            "Digite nome, CPF ou CNPJ do cliente...",
            formularioPanel,
            tabelaPanel
        );
        
        frame.add(mainPanel);
    }
    
    private JPanel createFormularioPanel() {
        JPanel panel = LayoutPadrao.criarPainelBranco();
        panel.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.WEST;
        
        // Nome do Cliente
        gbc.gridx = 0; gbc.gridy = 0;
        panel.add(LayoutPadrao.criarRotuloTexto("Nome:"), gbc);
        gbc.gridx = 1; gbc.weightx = 1.0;
        JTextField txtNome = new JTextField(30);
        txtNome.setFont(LayoutPadrao.FONTE_CAMPO);
        panel.add(txtNome, gbc);
        
        // CPF/CNPJ
        gbc.gridx = 0; gbc.gridy = 1; gbc.weightx = 0;
        panel.add(LayoutPadrao.criarRotuloTexto("CPF/CNPJ:"), gbc);
        gbc.gridx = 1; gbc.weightx = 1.0;
        JTextField txtCpfCnpj = new JTextField(20);
        txtCpfCnpj.setFont(LayoutPadrao.FONTE_CAMPO);
        panel.add(txtCpfCnpj, gbc);
        
        // Telefone
        gbc.gridx = 0; gbc.gridy = 2; gbc.weightx = 0;
        panel.add(LayoutPadrao.criarRotuloTexto("Telefone:"), gbc);
        gbc.gridx = 1; gbc.weightx = 1.0;
        JTextField txtTelefone = new JTextField(15);
        txtTelefone.setFont(LayoutPadrao.FONTE_CAMPO);
        panel.add(txtTelefone, gbc);
        
        // Email
        gbc.gridx = 0; gbc.gridy = 3; gbc.weightx = 0;
        panel.add(LayoutPadrao.criarRotuloTexto("Email:"), gbc);
        gbc.gridx = 1; gbc.weightx = 1.0;
        JTextField txtEmail = new JTextField(25);
        txtEmail.setFont(LayoutPadrao.FONTE_CAMPO);
        panel.add(txtEmail, gbc);
        
        // Botões
        gbc.gridx = 0; gbc.gridy = 4; gbc.gridwidth = 2; gbc.weightx = 0;
        JPanel botoesPanel = new JPanel(new FlowLayout());
        botoesPanel.setOpaque(false);
        
        JButton btnSalvar = LayoutPadrao.criarBotaoSucesso("💾 Salvar");
        JButton btnLimpar = LayoutPadrao.criarBotaoSecundario("🔄 Limpar");
        JButton btnExcluir = LayoutPadrao.criarBotaoPerigo("🗑️ Excluir");
        
        botoesPanel.add(btnSalvar);
        botoesPanel.add(btnLimpar);
        botoesPanel.add(btnExcluir);
        
        panel.add(botoesPanel, gbc);
        
        return panel;
    }
    
    private JPanel createTabelaPanel() {
        JPanel panel = LayoutPadrao.criarPainelBranco();
        panel.setLayout(new BorderLayout());
        
        // Título da tabela
        JLabel lblTitulo = LayoutPadrao.criarRotuloSubtitulo("👥 Clientes Cadastrados");
        lblTitulo.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        panel.add(lblTitulo, BorderLayout.NORTH);
        
        // Tabela de clientes
        String[] colunas = {"ID", "Nome", "CPF/CNPJ", "Telefone", "Status"};
        DefaultTableModel model = new DefaultTableModel(colunas, 0);
        JTable tabela = new JTable(model);
        tabela.setFont(LayoutPadrao.FONTE_TEXTO);
        tabela.setRowHeight(25);
        tabela.getTableHeader().setFont(LayoutPadrao.FONTE_SUBTITULO);
        
        // Adicionar dados de exemplo
        model.addRow(new Object[]{"001", "João Silva", "123.456.789-00", "(11) 98765-4321", "Ativo"});
        model.addRow(new Object[]{"002", "Maria Santos", "987.654.321-00", "(11) 91234-5678", "Ativo"});
        model.addRow(new Object[]{"003", "Empresa ABC Ltda", "12.345.678/0001-90", "(11) 3456-7890", "Ativo"});
        
        // Scroll pane
        JScrollPane scrollPane = new JScrollPane(tabela);
        scrollPane.setBorder(BorderFactory.createEmptyBorder(0, 10, 10, 10));
        panel.add(scrollPane, BorderLayout.CENTER);
        
        return panel;
    }
    
        
    // ==================== MÉTODOS PÚBLICOS ====================
    
    public void show() {
        if (frame == null) {
            initializeUI();
        }
        frame.setVisible(true);
        frame.toFront();
    }
    
    public void dispose() {
        if (frame != null) {
            frame.dispose();
            frame = null;
        }
    }
}
