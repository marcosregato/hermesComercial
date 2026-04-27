package com.br.hermescomercial.controller;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
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
    private JComboBox<String> txtCategoria;
    private JTextArea txtObservacoes;
    
    public PDVProdutoCadastroSwingController() {
        carregarCategorias();
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
        txtPreco = new JTextField() {
            @Override
            public void processKeyEvent(KeyEvent e) {
                char c = e.getKeyChar();
                
                // Permitir apenas números, vírgula, ponto, backspace, delete, e teclas de controle
                if (Character.isDigit(c) || c == ',' || c == '.' || 
                    c == KeyEvent.VK_BACK_SPACE || c == KeyEvent.VK_DELETE ||
                    c == KeyEvent.VK_LEFT || c == KeyEvent.VK_RIGHT ||
                    c == KeyEvent.VK_HOME || c == KeyEvent.VK_END) {
                    super.processKeyEvent(e);
                } else {
                    e.consume(); // Ignorar outros caracteres
                }
            }
        };
        txtPreco.setFont(new Font("Arial", Font.PLAIN, 12));
        txtPreco.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200), 1));
        txtPreco.setToolTipText("Digite o preço do produto (ex: 10,50 ou 100,00)");
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
        txtCategoria = new JComboBox<>();
        txtCategoria.setFont(new Font("Arial", Font.PLAIN, 12));
        txtCategoria.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200), 1));
        txtCategoria.setToolTipText("Selecione a categoria do produto");
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
        
        JButton btnLimpar = com.br.hermescomercial.theme.ModernTheme.createPastelButton("🔄 Limpar", com.br.hermescomercial.theme.ModernTheme.PASTEL_YELLOW, com.br.hermescomercial.theme.ModernTheme.TEXT_PRIMARY);
        JButton btnSalvar = com.br.hermescomercial.theme.ModernTheme.createPastelButton("💾 Salvar", com.br.hermescomercial.theme.ModernTheme.PASTEL_GREEN, com.br.hermescomercial.theme.ModernTheme.TEXT_PRIMARY);
        JButton btnCancelar = com.br.hermescomercial.theme.ModernTheme.createPastelButton("❌ Cancelar", com.br.hermescomercial.theme.ModernTheme.PASTEL_CORAL, com.br.hermescomercial.theme.ModernTheme.TEXT_PRIMARY);
        
        btnLimpar.addActionListener(e -> limparCampos());
        btnSalvar.addActionListener(this::salvarProduto);
        btnCancelar.addActionListener(e -> frame.dispose());
        
        buttonPanel.add(btnLimpar);
        buttonPanel.add(btnSalvar);
        buttonPanel.add(btnCancelar);
        
        return buttonPanel;
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
            String categoria = (String) txtCategoria.getSelectedItem();
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
            
            // Validar formato monetário (apenas números e vírgula decimal)
            if (!precoStr.matches("^\\d+(\\.\\d{1,2})?$")) {
                JOptionPane.showMessageDialog(frame, 
                    "O preço deve conter apenas números e vírgula decimal!\nEx: 10,50 ou 100,00",
                    "Preço Inválido", JOptionPane.WARNING_MESSAGE);
                txtPreco.requestFocus();
                return false;
            }
            
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
        if (txtCategoria.getSelectedItem() == null || ((String) txtCategoria.getSelectedItem()).trim().isEmpty()) {
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
        txtCategoria.setSelectedIndex(0);
        txtObservacoes.setText("");
        txtCodigo.requestFocus();
    }
    
    public void show() {
        frame.setVisible(true);
    }
    
    /**
     * Carrega as categorias cadastradas no banco de dados
     */
    private void carregarCategorias() {
        try {
            System.out.println("=== INICIANDO CARREGAMENTO DE CATEGORIAS ===");
            
            // Limpar categorias existentes
            txtCategoria.removeAllItems();
            System.out.println("✅ Categorias existentes removidas");
            
            // Buscar categorias únicas dos produtos cadastrados
            java.util.Set<String> categoriasUnicas = new java.util.TreeSet<>();
            
            try {
                System.out.println("📊 Buscando produtos no banco de dados...");
                
                // Usar ProdutoService para buscar categorias dos produtos
                com.br.hermescomercial.service.ProdutoService produtoService = new com.br.hermescomercial.service.ProdutoService();
                java.util.List<com.br.hermescomercial.model.Produto> produtos = produtoService.listar();
                
                System.out.println("📦 Encontrados " + produtos.size() + " produtos no banco");
                
                // Extrair categorias únicas dos produtos
                for (com.br.hermescomercial.model.Produto produto : produtos) {
                    String categoria = produto.getCategoria();
                    if (categoria != null && !categoria.trim().isEmpty()) {
                        categoriasUnicas.add(categoria.trim());
                        System.out.println("📋 Categoria encontrada: " + categoria.trim());
                    }
                }
                
                System.out.println("🔢 Total de categorias únicas encontradas: " + categoriasUnicas.size());
                
            } catch (Exception e) {
                System.err.println("❌ Erro ao buscar categorias do banco: " + e.getMessage());
                e.printStackTrace();
            }
            
            // Se não encontrou categorias no banco, adicionar categorias padrão
            if (categoriasUnicas.isEmpty()) {
                System.out.println("⚠️ Nenhuma categoria encontrada no banco, usando categorias padrão...");
                String[] categoriasPadrao = {
                    "Informática", "Periféricos", "Monitores", "Acessórios",
                    "Móveis", "Rede", "Armazenamento", "Software",
                    "Impressão", "Gamer"
                };
                for (String categoria : categoriasPadrao) {
                    categoriasUnicas.add(categoria);
                    System.out.println("📋 Categoria padrão adicionada: " + categoria);
                }
            }
            
            // Popular o JComboBox com as categorias
            System.out.println("🔄 Populando JComboBox com categorias...");
            for (String categoria : categoriasUnicas) {
                txtCategoria.addItem(categoria);
                System.out.println("✅ Categoria adicionada ao combo: " + categoria);
            }
            
            // Selecionar primeira categoria por padrão
            if (txtCategoria.getItemCount() > 0) {
                txtCategoria.setSelectedIndex(0);
                System.out.println("🎯 Primeira categoria selecionada: " + txtCategoria.getSelectedItem());
                System.out.println("📊 Total de categorias no JComboBox: " + txtCategoria.getItemCount());
            } else {
                System.out.println("❌ Nenhuma categoria foi adicionada ao JComboBox!");
            }
            
            System.out.println("=== CARREGAMENTO DE CATEGORIAS CONCLUÍDO ===");
            
        } catch (Exception e) {
            System.err.println("❌ Erro ao carregar categorias: " + e.getMessage());
            e.printStackTrace();
            // Adicionar categoria padrão em caso de erro
            txtCategoria.addItem("Geral");
            txtCategoria.setSelectedIndex(0);
            System.out.println("🔄 Categoria de fallback 'Geral' adicionada devido a erro");
        }
    }
}
