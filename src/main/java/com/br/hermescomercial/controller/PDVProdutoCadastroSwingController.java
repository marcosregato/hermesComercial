package com.br.hermescomercial.controller;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.math.BigDecimal;

/**
 * Controller de Cadastro de Produtos em SWING
 * Interface para cadastro de novos produtos no sistema PDV
 * Versão 2.0 - 100% SWING - Layout Premium
 */
public class PDVProdutoCadastroSwingController {
    private JFrame frame;
    private JTextField txtCodigo;
    private JTextField txtDescricao;
    private JTextField txtPreco;
    private JTextField txtEstoque;
    private JTextField txtCategoria;
    private JTextArea txtObservacoes;
    
    public PDVProdutoCadastroSwingController() {
        initializeUI();
    }
    
    private void initializeUI() {
        frame = new JFrame("📦 Cadastro de Produtos v2.1.0 - Premium");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setSize(700, 600);
        frame.setLocationRelativeTo(null);
        
        // Configurar fundo padrão Nova Venda
        frame.getContentPane().setBackground(new Color(245, 245, 250));
        
        frame.add(createMainPanel());
    }
    
    private JPanel createMainPanel() {
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        mainPanel.setBackground(new Color(245, 245, 250));
        
        mainPanel.add(createHeaderPanel(), BorderLayout.NORTH);
        mainPanel.add(createFormPanel(), BorderLayout.CENTER);
        mainPanel.add(createButtonPanel(), BorderLayout.SOUTH);
        
        return mainPanel;
    }
    
    private JPanel createHeaderPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 20, 10));
        panel.setBackground(new Color(41, 128, 185));
        panel.setPreferredSize(new Dimension(0, 80));
        
        // Título central
        JLabel titleLabel = new JLabel("📦 Cadastro de Produtos v2.1.0 - Premium", JLabel.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setForeground(Color.WHITE);
        
        // Botão voltar estilizado
        JButton btnVoltar = new JButton("← Voltar");
        btnVoltar.setBackground(new Color(255, 255, 255));
        btnVoltar.setForeground(new Color(41, 128, 185));
        btnVoltar.setFont(new Font("Arial", Font.BOLD, 12));
        btnVoltar.setBorder(BorderFactory.createEmptyBorder(8, 15, 8, 15));
        btnVoltar.setFocusPainted(false);
        btnVoltar.addActionListener(e -> frame.dispose());
        
        // Data e hora atual
        JLabel lblDataHora = new JLabel(java.time.LocalDateTime.now().format(java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")), JLabel.RIGHT);
        lblDataHora.setFont(new Font("Arial", Font.PLAIN, 12));
        lblDataHora.setForeground(Color.WHITE);
        
        panel.add(btnVoltar, BorderLayout.WEST);
        panel.add(titleLabel, BorderLayout.CENTER);
        panel.add(lblDataHora, BorderLayout.EAST);
        
        return panel;
    }
    
    private JPanel createFormPanel() {
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(Color.WHITE);
        formPanel.setBorder(BorderFactory.createTitledBorder("Dados do Produto"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
        // Código
        gbc.gridx = 0; gbc.gridy = 0; gbc.weightx = 0;
        formPanel.add(new JLabel("Código:"), gbc);
        gbc.gridx = 1; gbc.weightx = 1.0;
        txtCodigo = new JTextField();
        txtCodigo.setFont(new Font("Arial", Font.PLAIN, 12));
        txtCodigo.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200), 1));
        txtCodigo.setToolTipText("Digite o código do produto");
        formPanel.add(txtCodigo, gbc);
        
        // Descrição
        gbc.gridx = 0; gbc.gridy = 1; gbc.weightx = 0;
        formPanel.add(new JLabel("Descrição:"), gbc);
        gbc.gridx = 1; gbc.weightx = 1.0;
        txtDescricao = new JTextField();
        txtDescricao.setFont(new Font("Arial", Font.PLAIN, 12));
        txtDescricao.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200), 1));
        txtDescricao.setToolTipText("Digite a descrição do produto");
        formPanel.add(txtDescricao, gbc);
        
        // Preço
        gbc.gridx = 0; gbc.gridy = 2; gbc.weightx = 0;
        formPanel.add(new JLabel("Preço (R$):"), gbc);
        gbc.gridx = 1; gbc.weightx = 1.0;
        txtPreco = new JTextField();
        txtPreco.setFont(new Font("Arial", Font.PLAIN, 12));
        txtPreco.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200), 1));
        txtPreco.setToolTipText("Digite o preço do produto");
        formPanel.add(txtPreco, gbc);
        
        // Estoque
        gbc.gridx = 0; gbc.gridy = 3; gbc.weightx = 0;
        formPanel.add(new JLabel("Estoque:"), gbc);
        gbc.gridx = 1; gbc.weightx = 1.0;
        txtEstoque = new JTextField();
        txtEstoque.setFont(new Font("Arial", Font.PLAIN, 12));
        txtEstoque.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200), 1));
        txtEstoque.setToolTipText("Digite a quantidade em estoque");
        formPanel.add(txtEstoque, gbc);
        
        // Categoria
        gbc.gridx = 0; gbc.gridy = 4; gbc.weightx = 0;
        formPanel.add(new JLabel("Categoria:"), gbc);
        gbc.gridx = 1; gbc.weightx = 1.0;
        txtCategoria = new JTextField();
        txtCategoria.setFont(new Font("Arial", Font.PLAIN, 12));
        txtCategoria.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200), 1));
        txtCategoria.setToolTipText("Digite a categoria do produto");
        formPanel.add(txtCategoria, gbc);
        
        // Observações
        gbc.gridx = 0; gbc.gridy = 5; gbc.weightx = 0;
        gbc.anchor = GridBagConstraints.NORTHWEST;
        formPanel.add(new JLabel("Observações:"), gbc);
        gbc.gridx = 1; gbc.gridy = 5; gbc.weightx = 1.0; gbc.weighty = 1.0;
        gbc.fill = GridBagConstraints.BOTH;
        txtObservacoes = new JTextArea(3, 20);
        txtObservacoes.setFont(new Font("Arial", Font.PLAIN, 12));
        txtObservacoes.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200), 1));
        txtObservacoes.setBackground(Color.WHITE);
        txtObservacoes.setToolTipText("Digite observações adicionais (opcional)");
        JScrollPane scrollPane = new JScrollPane(txtObservacoes);
        formPanel.add(scrollPane, gbc);
        
        return formPanel;
    }
    
    private JPanel createButtonPanel() {
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 10));
        buttonPanel.setBackground(new Color(245, 245, 250));
        
        JButton btnLimpar = createStyledButton("🔄 Limpar", "Limpar campos", null);
        JButton btnSalvar = createStyledButton("💾 Salvar", "Salvar produto", this::salvarProduto);
        JButton btnCancelar = createStyledButton("❌ Cancelar", "Cancelar cadastro", e -> frame.dispose());
        
        btnLimpar.addActionListener(e -> limparCampos());
        
        buttonPanel.add(btnLimpar);
        buttonPanel.add(btnSalvar);
        buttonPanel.add(btnCancelar);
        
        return buttonPanel;
    }
    
    private JButton createStyledButton(String text, String tooltip, ActionListener action) {
        JButton button = new JButton(text);
        button.setBackground(new Color(41, 128, 185));
        button.setForeground(Color.WHITE);
        button.setFont(new Font("Arial", Font.BOLD, 12));
        button.setBorder(BorderFactory.createEmptyBorder(12, 20, 12, 20));
        button.setFocusPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setToolTipText(tooltip);
        if (action != null) {
            button.addActionListener(action);
        }
        
        // Efeito hover
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(new Color(52, 152, 219));
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(new Color(41, 128, 185));
            }
        });
        
        return button;
    }
    
    private void salvarProduto(ActionEvent e) {
        try {
            // Validar campos
            if (!validarCampos()) {
                return;
            }
            
            // Coletar dados
            String codigo = txtCodigo.getText().trim();
            String descricao = txtDescricao.getText().trim();
            BigDecimal preco = new BigDecimal(txtPreco.getText().trim().replace(",", "."));
            int estoque = Integer.parseInt(txtEstoque.getText().trim());
            String categoria = txtCategoria.getText().trim();
            String observacoes = txtObservacoes.getText().trim();
            
            // Simulação de salvamento (em implementação real, salvaria no banco)
            JOptionPane.showMessageDialog(frame, 
                "Produto cadastrado com sucesso!\n\n" +
                "Código: " + codigo + "\n" +
                "Descrição: " + descricao + "\n" +
                "Preço: R$ " + String.format("%.2f", preco) + "\n" +
                "Estoque: " + estoque + "\n" +
                "Categoria: " + categoria + "\n" +
                "Observações: " + (observacoes.isEmpty() ? "Nenhuma" : observacoes),
                "Produto Cadastrado", JOptionPane.INFORMATION_MESSAGE);
            
            // Limpar campos após salvar
            limparCampos();
            txtCodigo.requestFocus();
            
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(frame, 
                "Verifique os campos numéricos (Preço e Estoque)!",
                "Erro de Formato", JOptionPane.ERROR_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(frame, 
                "Erro ao cadastrar produto: " + ex.getMessage(),
                "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private boolean validarCampos() {
        // Validar código
        if (txtCodigo.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(frame, 
                "O código do produto é obrigatório!",
                "Campo Obrigatório", JOptionPane.WARNING_MESSAGE);
            txtCodigo.requestFocus();
            return false;
        }
        
        // Validar descrição
        if (txtDescricao.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(frame, 
                "A descrição do produto é obrigatória!",
                "Campo Obrigatório", JOptionPane.WARNING_MESSAGE);
            txtDescricao.requestFocus();
            return false;
        }
        
        // Validar preço
        try {
            String precoStr = txtPreco.getText().trim().replace(",", ".");
            BigDecimal preco = new BigDecimal(precoStr);
            if (preco.compareTo(BigDecimal.ZERO) <= 0) {
                JOptionPane.showMessageDialog(frame, 
                    "O preço deve ser maior que zero!",
                    "Preço Inválido", JOptionPane.WARNING_MESSAGE);
                txtPreco.requestFocus();
                return false;
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(frame, 
                "O preço informado é inválido!",
                "Preço Inválido", JOptionPane.WARNING_MESSAGE);
            txtPreco.requestFocus();
            return false;
        }
        
        // Validar estoque
        try {
            int estoque = Integer.parseInt(txtEstoque.getText().trim());
            if (estoque < 0) {
                JOptionPane.showMessageDialog(frame, 
                    "O estoque não pode ser negativo!",
                    "Estoque Inválido", JOptionPane.WARNING_MESSAGE);
                txtEstoque.requestFocus();
                return false;
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(frame, 
                "O estoque informado é inválido!",
                "Estoque Inválido", JOptionPane.WARNING_MESSAGE);
            txtEstoque.requestFocus();
            return false;
        }
        
        // Validar categoria
        if (txtCategoria.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(frame, 
                "A categoria do produto é obrigatória!",
                "Campo Obrigatório", JOptionPane.WARNING_MESSAGE);
            txtCategoria.requestFocus();
            return false;
        }
        
        return true;
    }
    
    private void limparCampos() {
        txtCodigo.setText("");
        txtDescricao.setText("");
        txtPreco.setText("");
        txtEstoque.setText("");
        txtCategoria.setText("");
        txtObservacoes.setText("");
        txtCodigo.requestFocus();
    }
    
    public void show() {
        frame.setVisible(true);
    }
}
