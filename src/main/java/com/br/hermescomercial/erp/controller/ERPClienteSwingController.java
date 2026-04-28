package com.br.hermescomercial.erp.controller;

import com.br.hermescomercial.theme.ModernTheme;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Controller para tela de gestão de clientes no ERP
 * Versão 2.3.0 - Arquitetura Modular
 */
public class ERPClienteSwingController {
    
    private JFrame frame;
    private JTable clientesTable;
    private DefaultTableModel tableModel;
    private JTextField txtBusca, txtNome, txtCpfCnpj, txtTelefone, txtEmail;
    private JButton btnNovo, btnEditar, btnExcluir, btnSalvar, btnCancelar;
    
    public ERPClienteSwingController() {
        initializeUI();
    }
    
    private void initializeUI() {
        frame = new JFrame("👥 Gestão de Clientes - ERP");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setSize(1200, 800);
        frame.setLocationRelativeTo(null);
        
        // Aplicar tema moderno
        frame.getContentPane().setBackground(ModernTheme.BACKGROUND_PRIMARY);
        
        createMainPanel();
        frame.setVisible(true);
    }
    
    private void createMainPanel() {
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setOpaque(false);
        mainPanel.setBorder(new EmptyBorder(20, 20, 20, 20));
        
        // Header
        createHeaderPanel();
        
        // Painel de formulário e tabela
        JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
        splitPane.setDividerLocation(300);
        
        JPanel formPanel = createFormPanel();
        JPanel tablePanel = createTablePanel();
        
        splitPane.setTopComponent(formPanel);
        splitPane.setBottomComponent(tablePanel);
        
        mainPanel.add(createHeaderPanel(), BorderLayout.NORTH);
        mainPanel.add(splitPane, BorderLayout.CENTER);
        
        frame.add(mainPanel);
    }
    
    private JPanel createHeaderPanel() {
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setOpaque(false);
        headerPanel.setBorder(new EmptyBorder(0, 0, 20, 0));
        
        // Título
        JLabel titleLabel = new JLabel("👥 Gestão de Clientes");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        titleLabel.setForeground(ModernTheme.TEXT_PRIMARY);
        
        // Botões de ação
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.setOpaque(false);
        
        JButton btnImportar = createModernButton("📥 Importar do PDV", ModernTheme.SUCCESS_COLOR);
        btnImportar.addActionListener(e -> importarClientesPDV());
        
        JButton btnExportar = createModernButton("📤 Exportar para PDV", ModernTheme.PRIMARY_COLOR);
        btnExportar.addActionListener(e -> exportarClientesPDV());
        
        buttonPanel.add(btnImportar);
        buttonPanel.add(btnExportar);
        
        headerPanel.add(titleLabel, BorderLayout.WEST);
        headerPanel.add(buttonPanel, BorderLayout.EAST);
        
        return headerPanel;
    }
    
    private JPanel createFormPanel() {
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setOpaque(false);
        formPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.weightx = 0.0;
        
        // Campo de busca
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        formPanel.add(new JLabel("🔍 Busca:"), gbc);
        
        txtBusca = new JTextField(20);
        txtBusca.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        txtBusca.setPreferredSize(new Dimension(200, 30));
        gbc.gridx = 2;
        gbc.weightx = 1.0;
        formPanel.add(txtBusca, gbc);
        
        // Dados do cliente
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 1;
        gbc.weightx = 0.0;
        formPanel.add(new JLabel("Nome:"), gbc);
        
        txtNome = new JTextField(20);
        txtNome.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        gbc.gridx = 1;
        gbc.weightx = 1.0;
        formPanel.add(txtNome, gbc);
        
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.weightx = 0.0;
        formPanel.add(new JLabel("CPF/CNPJ:"), gbc);
        
        txtCpfCnpj = new JTextField(20);
        txtCpfCnpj.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        gbc.gridx = 1;
        gbc.weightx = 1.0;
        formPanel.add(txtCpfCnpj, gbc);
        
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.weightx = 0.0;
        formPanel.add(new JLabel("Telefone:"), gbc);
        
        txtTelefone = new JTextField(20);
        txtTelefone.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        gbc.gridx = 1;
        gbc.weightx = 1.0;
        formPanel.add(txtTelefone, gbc);
        
        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.weightx = 0.0;
        formPanel.add(new JLabel("E-mail:"), gbc);
        
        txtEmail = new JTextField(20);
        txtEmail.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        gbc.gridx = 1;
        gbc.weightx = 1.0;
        formPanel.add(txtEmail, gbc);
        
        // Botões de ação
        gbc.gridx = 0;
        gbc.gridy = 5;
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.NONE;
        gbc.anchor = GridBagConstraints.CENTER;
        
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.setOpaque(false);
        
        btnNovo = createModernButton("➕ Novo", ModernTheme.SUCCESS_COLOR);
        btnNovo.addActionListener(e -> limparFormulario());
        
        btnSalvar = createModernButton("💾 Salvar", ModernTheme.PRIMARY_COLOR);
        btnSalvar.addActionListener(e -> salvarCliente());
        
        btnCancelar = createModernButton("❌ Cancelar", ModernTheme.DANGER_COLOR);
        btnCancelar.addActionListener(e -> limparFormulario());
        
        buttonPanel.add(btnNovo);
        buttonPanel.add(btnSalvar);
        buttonPanel.add(btnCancelar);
        
        formPanel.add(buttonPanel, gbc);
        
        return formPanel;
    }
    
    private JPanel createTablePanel() {
        JPanel tablePanel = new JPanel(new BorderLayout());
        tablePanel.setOpaque(false);
        tablePanel.setBorder(new EmptyBorder(10, 10, 10, 10));
        
        // Título da tabela
        JLabel titleLabel = new JLabel("📋 Lista de Clientes");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        titleLabel.setForeground(ModernTheme.TEXT_PRIMARY);
        
        // Botões de ação da tabela
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        buttonPanel.setOpaque(false);
        
        btnEditar = createModernButton("✏️ Editar", ModernTheme.WARNING_COLOR);
        btnEditar.addActionListener(e -> editarCliente());
        
        btnExcluir = createModernButton("🗑️ Excluir", ModernTheme.DANGER_COLOR);
        btnExcluir.addActionListener(e -> excluirCliente());
        
        buttonPanel.add(btnEditar);
        buttonPanel.add(btnExcluir);
        
        // Criar tabela
        String[] colunas = {"Código", "Nome", "CPF/CNPJ", "Telefone", "E-mail", "Data Cadastro", "Status"};
        tableModel = new DefaultTableModel(colunas, 0) {
            @Override
            public Class<?> getColumnClass(int columnIndex) {
                switch (columnIndex) {
                    case 0: return Long.class; // Código
                    case 1: return String.class; // Nome
                    case 2: return String.class; // CPF/CNPJ
                    case 3: return String.class; // Telefone
                    case 4: return String.class; // E-mail
                    case 5: return String.class; // Data Cadastro (formatada)
                    case 6: return String.class; // Status
                    default: return String.class;
                }
            }
            
            @Override
            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return false; // Tabela não editável diretamente
            }
        };
        
        clientesTable = new JTable(tableModel);
        clientesTable.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        clientesTable.setRowHeight(25);
        clientesTable.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 12));
        clientesTable.getTableHeader().setBackground(ModernTheme.PRIMARY_COLOR);
        clientesTable.getTableHeader().setForeground(Color.WHITE);
        
        // Adicionar dados de exemplo
        carregarDadosExemplo();
        
        // Evento de duplo clique
        clientesTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    editarCliente();
                }
            }
        });
        
        JScrollPane scrollPane = new JScrollPane(clientesTable);
        scrollPane.setPreferredSize(new Dimension(1150, 350));
        scrollPane.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(ModernTheme.BORDER_LIGHT, 1),
            new EmptyBorder(5, 5, 5, 5)
        ));
        
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setOpaque(false);
        topPanel.add(titleLabel, BorderLayout.WEST);
        topPanel.add(buttonPanel, BorderLayout.EAST);
        
        tablePanel.add(topPanel, BorderLayout.NORTH);
        tablePanel.add(scrollPane, BorderLayout.CENTER);
        
        return tablePanel;
    }
    
    private JButton createModernButton(String text, Color bgColor) {
        JButton button = new JButton(text);
        button.setFont(new Font("Segoe UI", Font.BOLD, 12));
        button.setBackground(bgColor);
        button.setForeground(Color.WHITE);
        button.setBorderPainted(false);
        button.setFocusPainted(false);
        button.setPreferredSize(new Dimension(120, 35));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        // Efeito hover
        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                button.setBackground(bgColor.darker());
            }
            
            @Override
            public void mouseExited(MouseEvent e) {
                button.setBackground(bgColor);
            }
        });
        
        return button;
    }
    
    private void carregarDadosExemplo() {
        // Limpar tabela
        tableModel.setRowCount(0);
        
        // Dados de exemplo
        Object[][] dados = {
            {1L, "João Silva", "123.456.789-00", "(11) 98765-4321", "joao@email.com", new Date(), "Ativo"},
            {2L, "Maria Santos", "987.654.321-00", "(11) 123.456.789-01", "maria@email.com", new Date(), "Ativo"},
            {3L, "Empresa ABC Ltda", "12.345.678/0001-95", "(16) 345.678.9012-34", "contato@empresaabc.com", new Date(), "Ativo"},
            {4L, "Pedro Oliveira", "456.789.012-34", "(11) 567.890-123-45", "pedro@email.com", new Date(), "Inativo"},
            {5L, "Ana Costa", "789.012.345-67", "(11) 234.567.890-12", "ana@email.com", new Date(), "Ativo"}
        };
        
        // Formatar data
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        
        // Adicionar dados na tabela
        for (Object[] linha : dados) {
            Object[] linhaFormatada = linha.clone();
            linhaFormatada[5] = sdf.format((Date) linha[5]);
            tableModel.addRow(linhaFormatada);
        }
    }
    
    // ==================== MÉTODOS DE AÇÃO ====================
    
    private void importarClientesPDV() {
        JOptionPane.showMessageDialog(frame, 
            "📥 Importar Clientes do PDV\n" +
            "Clientes do módulo PDV serão importados para o ERP.\n" +
            "Funcionalidade em desenvolvimento.", 
            "Importar", JOptionPane.INFORMATION_MESSAGE);
    }
    
    private void exportarClientesPDV() {
        JOptionPane.showMessageDialog(frame, 
            "📤 Exportar Clientes para PDV\n" +
            "Clientes do ERP serão exportados para o módulo PDV.\n" +
            "Funcionalidade em desenvolvimento.", 
            "Exportar", JOptionPane.INFORMATION_MESSAGE);
    }
    
    private void limparFormulario() {
        txtNome.setText("");
        txtCpfCnpj.setText("");
        txtTelefone.setText("");
        txtEmail.setText("");
        txtBusca.setText("");
        btnSalvar.setText("💾 Salvar");
        btnCancelar.setText("❌ Cancelar");
    }
    
    private void salvarCliente() {
        String nome = txtNome.getText().trim();
        String cpfCnpj = txtCpfCnpj.getText().trim();
        
        if (nome.isEmpty()) {
            JOptionPane.showMessageDialog(frame, 
                "⚠️ Nome é obrigatório!", 
                "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        if (cpfCnpj.isEmpty()) {
            JOptionPane.showMessageDialog(frame, 
                "⚠️ CPF/CNPJ é obrigatório!", 
                "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        // Salvar cliente no banco de dados
        try {
            // TODO: Implementar conexão com banco de dados
            // Salvar dados do cliente incluindo telefone e email
            JOptionPane.showMessageDialog(frame, 
                "✅ Cliente salvo com sucesso!\n" +
                "Nome: " + nome + "\n" +
                "CPF/CNPJ: " + cpfCnpj + "\n" +
                "Telefone: " + txtTelefone.getText().trim() + "\n" +
                "Email: " + txtEmail.getText().trim(), 
                "Sucesso", JOptionPane.INFORMATION_MESSAGE);
            
            limparFormulario();
            carregarDadosExemplo(); // Recarregar dados
        } catch (Exception e) {
            JOptionPane.showMessageDialog(frame, 
                "❌ Erro ao salvar cliente: " + e.getMessage(), 
                "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void editarCliente() {
        int linhaSelecionada = clientesTable.getSelectedRow();
        
        if (linhaSelecionada == -1) {
            JOptionPane.showMessageDialog(frame, 
                "⚠️ Nenhum cliente selecionado!\n" +
                "Selecione um cliente na tabela para editar.", 
                "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        // Preencher formulário com dados selecionados
        String nome = (String) tableModel.getValueAt(linhaSelecionada, 1);
        String cpfCnpj = (String) tableModel.getValueAt(linhaSelecionada, 2);
        String telefone = (String) tableModel.getValueAt(linhaSelecionada, 3);
        String email = (String) tableModel.getValueAt(linhaSelecionada, 4);
        
        txtNome.setText(nome != null ? nome : "");
        txtCpfCnpj.setText(cpfCnpj != null ? cpfCnpj : "");
        txtTelefone.setText(telefone != null ? telefone : "");
        txtEmail.setText(email != null ? email : "");
        
        btnSalvar.setText("💾 Atualizar");
        btnCancelar.setText("❌ Cancelar Edição");
        
        // TODO: Implementar atualização no banco de dados
        JOptionPane.showMessageDialog(frame, 
            "📝 Cliente carregado para edição.\n" +
            "Modifique os dados e clique em 'Atualizar'.", 
            "Informação", JOptionPane.INFORMATION_MESSAGE);
    }
    
    private void excluirCliente() {
        int linhaSelecionada = clientesTable.getSelectedRow();
        
        if (linhaSelecionada == -1) {
            JOptionPane.showMessageDialog(frame, 
                "⚠️ Nenhum cliente selecionado!\n" +
                "Selecione um cliente na tabela para excluir.", 
                "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        String nome = (String) tableModel.getValueAt(linhaSelecionada, 1);
        
        int confirmacao = JOptionPane.showConfirmDialog(frame, 
            "🗑️ Confirmar Exclusão\n" +
            "Deseja realmente excluir o cliente selecionado?\n\n" +
            "Nome: " + nome, 
            "Confirmar Exclusão", 
            JOptionPane.YES_NO_OPTION, 
            JOptionPane.QUESTION_MESSAGE);
        
        if (confirmacao == JOptionPane.YES_OPTION) {
            try {
                // TODO: Implementar exclusão no banco de dados
                tableModel.removeRow(linhaSelecionada);
                
                JOptionPane.showMessageDialog(frame, 
                    "✅ Cliente excluído com sucesso!", 
                    "Sucesso", JOptionPane.INFORMATION_MESSAGE);
            } catch (Exception e) {
                JOptionPane.showMessageDialog(frame, 
                    "❌ Erro ao excluir cliente: " + e.getMessage(), 
                    "Erro", JOptionPane.ERROR_MESSAGE);
            }
        }
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
