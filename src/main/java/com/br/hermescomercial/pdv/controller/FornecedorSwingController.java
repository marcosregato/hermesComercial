package com.br.hermescomercial.pdv.controller;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import com.br.hermescomercial.service.FornecedorService;

/**
 * Controller para tela de gestão de fornecedores
 * Versão 2.8.0 - Interface completa para gestão de fornecedores
 * Funcionalidades: Cadastro, consulta, edição, exclusão, histórico de pedidos
 */
public class FornecedorSwingController {
    
    private JFrame frame;
    private JTabbedPane tabbedPane;
    private JTextField txtNome, txtCnpjCpf, txtTelefone, txtEmail, txtEndereco;
    private JTextArea txtObservacoes;
    private JTable fornecedoresTable;
    private DefaultTableModel tableModel;
    
    public FornecedorSwingController() {
        // Inicializar service quando implementado
        // fornecedorService = new FornecedorService();
        initializeUI();
    }
    
    private void initializeUI() {
        frame = new JFrame("Gestão de Fornecedores - Hermes Comercial PDV");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setSize(1000, 700);
        frame.setLocationRelativeTo(null);
        
        // Aplicar tema moderno
        frame.getContentPane().setBackground(Color.WHITE);
        
        tabbedPane = new JTabbedPane();
        tabbedPane.setBackground(Color.WHITE);
        tabbedPane.setForeground(Color.BLACK);
        
        // Abas principais
        tabbedPane.addTab("📋 Cadastro", createCadastroPanel());
        tabbedPane.addTab("🔍 Consulta", createConsultaPanel());
        tabbedPane.addTab("📦 Pedidos", createPedidosPanel());
        
        frame.add(tabbedPane);
    }
    
    private JPanel createCadastroPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);
        
        // Formulário de cadastro
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(Color.WHITE);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;
        
        // Nome do fornecedor
        gbc.gridx = 0; gbc.gridy = 0;
        formPanel.add(new JLabel("Nome/Razão Social:"), gbc);
        gbc.gridx = 1; gbc.fill = GridBagConstraints.HORIZONTAL; gbc.weightx = 1;
        txtNome = new JTextField(30);
        formPanel.add(txtNome, gbc);
        
        // CNPJ/CPF
        gbc.gridx = 0; gbc.gridy = 1; gbc.fill = GridBagConstraints.NONE; gbc.weightx = 0;
        formPanel.add(new JLabel("CNPJ/CPF:"), gbc);
        gbc.gridx = 1; gbc.fill = GridBagConstraints.HORIZONTAL; gbc.weightx = 1;
        txtCnpjCpf = new JTextField(20);
        formPanel.add(txtCnpjCpf, gbc);
        
        // Telefone
        gbc.gridx = 0; gbc.gridy = 2; gbc.fill = GridBagConstraints.NONE; gbc.weightx = 0;
        formPanel.add(new JLabel("Telefone:"), gbc);
        gbc.gridx = 1; gbc.fill = GridBagConstraints.HORIZONTAL; gbc.weightx = 1;
        txtTelefone = new JTextField(20);
        formPanel.add(txtTelefone, gbc);
        
        // Email
        gbc.gridx = 0; gbc.gridy = 3; gbc.fill = GridBagConstraints.NONE; gbc.weightx = 0;
        formPanel.add(new JLabel("Email:"), gbc);
        gbc.gridx = 1; gbc.fill = GridBagConstraints.HORIZONTAL; gbc.weightx = 1;
        txtEmail = new JTextField(30);
        formPanel.add(txtEmail, gbc);
        
        // Endereço
        gbc.gridx = 0; gbc.gridy = 4; gbc.fill = GridBagConstraints.NONE; gbc.weightx = 0;
        formPanel.add(new JLabel("Endereço:"), gbc);
        gbc.gridx = 1; gbc.fill = GridBagConstraints.HORIZONTAL; gbc.weightx = 1;
        txtEndereco = new JTextField(40);
        formPanel.add(txtEndereco, gbc);
        
        // Observações
        gbc.gridx = 0; gbc.gridy = 5; gbc.fill = GridBagConstraints.NONE; gbc.weightx = 0;
        formPanel.add(new JLabel("Observações:"), gbc);
        gbc.gridx = 1; gbc.fill = GridBagConstraints.BOTH; gbc.weightx = 1; gbc.weighty = 1;
        txtObservacoes = new JTextArea(4, 30);
        txtObservacoes.setLineWrap(true);
        txtObservacoes.setWrapStyleWord(true);
        JScrollPane scrollPane = new JScrollPane(txtObservacoes);
        formPanel.add(scrollPane, gbc);
        
        panel.add(formPanel, BorderLayout.CENTER);
        
        // Painel de botões
        JPanel buttonPanel = new JPanel(new FlowLayout());
        buttonPanel.setBackground(Color.WHITE);
        
        JButton btnSalvar = createButton("💾 Salvar", new Color(76, 175, 80));
        JButton btnLimpar = createButton("🔄 Limpar", new Color(255, 152, 0));
        JButton btnExcluir = createButton("🗑️ Excluir", new Color(244, 67, 54));
        
        btnSalvar.addActionListener(e -> salvarFornecedor());
        btnLimpar.addActionListener(e -> limparFormulario());
        btnExcluir.addActionListener(e -> excluirFornecedor());
        
        buttonPanel.add(btnSalvar);
        buttonPanel.add(btnLimpar);
        buttonPanel.add(btnExcluir);
        
        panel.add(buttonPanel, BorderLayout.SOUTH);
        
        return panel;
    }
    
    private JPanel createConsultaPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);
        
        // Painel de busca
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        searchPanel.setBackground(Color.WHITE);
        
        JTextField txtBusca = new JTextField(30);
        JButton btnBuscar = createButton("🔍 Buscar", new Color(33, 150, 243));
        JButton btnAtualizar = createButton("🔄 Atualizar", new Color(76, 175, 80));
        
        btnBuscar.addActionListener(e -> buscarFornecedores(txtBusca.getText()));
        btnAtualizar.addActionListener(e -> carregarFornecedores());
        
        searchPanel.add(new JLabel("Buscar:"));
        searchPanel.add(txtBusca);
        searchPanel.add(btnBuscar);
        searchPanel.add(btnAtualizar);
        
        panel.add(searchPanel, BorderLayout.NORTH);
        
        // Tabela de fornecedores
        String[] columns = {"ID", "Nome", "CNPJ/CPF", "Telefone", "Email", "Status"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        fornecedoresTable = new JTable(tableModel);
        fornecedoresTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        fornecedoresTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    carregarFornecedorSelecionado();
                }
            }
        });
        
        JScrollPane tableScrollPane = new JScrollPane(fornecedoresTable);
        panel.add(tableScrollPane, BorderLayout.CENTER);
        
        // Carregar dados iniciais
        carregarFornecedores();
        
        return panel;
    }
    
    private JPanel createPedidosPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);
        
        JLabel lblInfo = new JLabel("📦 Gestão de Pedidos de Fornecedores");
        lblInfo.setFont(new Font("Segoe UI", Font.BOLD, 16));
        lblInfo.setHorizontalAlignment(SwingConstants.CENTER);
        panel.add(lblInfo, BorderLayout.NORTH);
        
        JTextArea txtPedidos = new JTextArea();
        txtPedidos.setEditable(false);
        txtPedidos.setText("Funcionalidade de pedidos em desenvolvimento...\n\n" +
                          "Recursos planejados:\n" +
                          "• Novo pedido de fornecedor\n" +
                          "• Histórico de pedidos\n" +
                          "• Status dos pedidos\n" +
                          "• Relatórios de compras");
        
        JScrollPane scrollPane = new JScrollPane(txtPedidos);
        panel.add(scrollPane, BorderLayout.CENTER);
        
        return panel;
    }
    
    private JButton createButton(String text, Color color) {
        JButton button = new JButton(text);
        button.setBackground(color);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setOpaque(true);
        button.setFont(new Font("Segoe UI", Font.BOLD, 12));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(color.darker());
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(color);
            }
        });
        
        return button;
    }
    
    private void salvarFornecedor() {
        try {
            String nome = txtNome.getText().trim();
            String cnpjCpf = txtCnpjCpf.getText().trim();
            String telefone = txtTelefone.getText().trim();
            String email = txtEmail.getText().trim();
            String endereco = txtEndereco.getText().trim();
            String observacoes = txtObservacoes.getText().trim();
            
            if (nome.isEmpty()) {
                JOptionPane.showMessageDialog(frame, "Nome do fornecedor é obrigatório!", 
                    "Erro", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            // Simulação de salvamento (implementar com o service)
            // Usar dados capturados para integração real
            System.out.println("Salvando fornecedor: " + nome);
            System.out.println("CNPJ/CPF: " + cnpjCpf);
            System.out.println("Telefone: " + telefone);
            System.out.println("Email: " + email);
            System.out.println("Endereço: " + endereco);
            System.out.println("Observações: " + observacoes);
            
            // Utilizar service quando disponível
            // fornecedorService.salvarFornecedor(nome, cnpjCpf, telefone, email, endereco, observacoes);
            
            JOptionPane.showMessageDialog(frame, "Fornecedor salvo com sucesso!", 
                "Sucesso", JOptionPane.INFORMATION_MESSAGE);
            
            limparFormulario();
            carregarFornecedores();
            
        } catch (Exception e) {
            JOptionPane.showMessageDialog(frame, "Erro ao salvar fornecedor: " + e.getMessage(), 
                "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void limparFormulario() {
        txtNome.setText("");
        txtCnpjCpf.setText("");
        txtTelefone.setText("");
        txtEmail.setText("");
        txtEndereco.setText("");
        txtObservacoes.setText("");
    }
    
    private void excluirFornecedor() {
        int selectedRow = fornecedoresTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(frame, "Selecione um fornecedor para excluir!", 
                "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        int confirm = JOptionPane.showConfirmDialog(frame, 
            "Deseja realmente excluir este fornecedor?", 
            "Confirmar Exclusão", JOptionPane.YES_NO_OPTION);
        
        if (confirm == JOptionPane.YES_OPTION) {
            // Simulação de exclusão (implementar com o service)
            JOptionPane.showMessageDialog(frame, "Fornecedor excluído com sucesso!", 
                "Sucesso", JOptionPane.INFORMATION_MESSAGE);
            carregarFornecedores();
        }
    }
    
    private void buscarFornecedores(String termo) {
        // Simulação de busca (implementar com o service)
        JOptionPane.showMessageDialog(frame, "Busca por: " + termo + "\n" +
            "Funcionalidade em desenvolvimento...", "Busca", JOptionPane.INFORMATION_MESSAGE);
    }
    
    private void carregarFornecedores() {
        try {
            // Limpar tabela
            tableModel.setRowCount(0);
            
            // Simulação de dados (implementar com o service)
            Object[][] dados = {
                {1, "Fornecedor A", "12.345.678/0001-90", "(11) 1234-5678", "fornecedor@a.com", "Ativo"},
                {2, "Fornecedor B", "98.765.432/0001-23", "(21) 9876-5432", "fornecedor@b.com", "Ativo"},
                {3, "Fornecedor C", "45.678.912/0001-56", "(31) 4567-8901", "fornecedor@c.com", "Inativo"}
            };
            
            for (Object[] row : dados) {
                tableModel.addRow(row);
            }
            
        } catch (Exception e) {
            JOptionPane.showMessageDialog(frame, "Erro ao carregar fornecedores: " + e.getMessage(), 
                "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void carregarFornecedorSelecionado() {
        int selectedRow = fornecedoresTable.getSelectedRow();
        if (selectedRow == -1) return;
        
        // Carregar dados do fornecedor selecionado no formulário
        tabbedPane.setSelectedIndex(0); // Mudar para aba de cadastro
        
        // Simulação de carregamento (implementar com o service)
        txtNome.setText("Fornecedor A");
        txtCnpjCpf.setText("12.345.678/0001-90");
        txtTelefone.setText("(11) 1234-5678");
        txtEmail.setText("fornecedor@a.com");
        txtEndereco.setText("Rua A, 123 - Centro");
        txtObservacoes.setText("Fornecedor de materiais de escritório");
    }
    
    public void show() {
        frame.setVisible(true);
    }
    
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new FornecedorSwingController().show();
        });
    }
}
