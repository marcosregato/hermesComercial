package com.br.hermescomercial.pdv.controller;

import com.br.hermescomercial.dao.ProdutoDao;
import com.br.hermescomercial.util.DatabaseConfig;
import com.br.hermescomercial.ui.layout.LayoutPadrao;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Controller Unificado de Produtos em SWING
 * Interface completa para cadastro e consulta de produtos
 * Versão 2.0 - 100% SWING - Layout Premium com Abas
 */
public class PDVProdutosUnificadoSwingController {
    
    private JFrame frame;
    private JTabbedPane tabbedPane;
    
    // Componentes da aba de Consulta
    private JTextField txtBuscaConsulta;
    private JTable produtosTable;
    private DefaultTableModel tableModel;
    
    // Componentes da aba de Cadastro
    private JTextField txtCodigo;
    private JTextField txtDescricao;
    private JTextField txtPreco;
    private JTextField txtEstoque;
    private JComboBox<String> txtCategoria;
    private JTextArea txtObservacoes;
    
    // Lista de produtos (simulação)
    private List<Produto> produtos;
    
    public PDVProdutosUnificadoSwingController() {
        produtos = new ArrayList<>(100); // Capacidade inicial para melhor performance
        carregarProdutosDoBanco();
        initializeUI();
    }
    
    private void initializeUI() {
        frame = new JFrame("📦 Gestão de Produtos v2.8.3 - LayoutPadrao");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setSize(950, 650);
        frame.setLocationRelativeTo(null);
        
        // Configurar fundo com LayoutPadrao
        frame.getContentPane().setBackground(LayoutPadrao.COR_FUNDO_ESCURO);
        
        frame.add(createMainPanel());
    }
    
    private JPanel createMainPanel() {
        // Main panel com LayoutPadrao
        JPanel mainPanel = LayoutPadrao.criarPainelComMargem(15);
        mainPanel.setBackground(LayoutPadrao.COR_FUNDO_ESCURO);
        
        mainPanel.add(createHeaderPanel(), BorderLayout.NORTH);
        mainPanel.add(createTabbedPane(), BorderLayout.CENTER);
        
        return mainPanel;
    }
    
    private JPanel createHeaderPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));
        panel.setBackground(new Color(26, 188, 156)); // Azul Turquesa igual Nova Venda
        panel.setPreferredSize(new Dimension(0, 90)); // Altura igual Nova Venda
        
        // Painel esquerdo com botão voltar
        JPanel leftPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        leftPanel.setOpaque(false);
        
        JButton btnVoltar = new JButton("← Voltar");
        btnVoltar.setBackground(new Color(255, 255, 255));
        btnVoltar.setForeground(new Color(41, 128, 185));
        btnVoltar.setFont(new Font("Segoe UI", Font.BOLD, 12)); // Fonte igual Nova Venda
        btnVoltar.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20)); // Borda igual Nova Venda
        btnVoltar.setFocusPainted(false);
        btnVoltar.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnVoltar.addActionListener(e -> frame.dispose());
        
        leftPanel.add(btnVoltar);
        
        // Painel central com título e subtítulo
        JPanel centerPanel = new JPanel(new BorderLayout());
        centerPanel.setOpaque(false);
        
        JLabel titleLabel = new JLabel("📦 Gestão de Produtos v2.8.0 - Premium", JLabel.CENTER);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 24)); // Fonte igual Nova Venda
        titleLabel.setForeground(Color.WHITE);
        
        // Data e hora atual
        JLabel lblDataHora = new JLabel(java.time.LocalDateTime.now().format(java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")), JLabel.RIGHT);
        lblDataHora.setFont(new Font("Arial", Font.PLAIN, 12));
        lblDataHora.setForeground(Color.WHITE);
        
        panel.add(btnVoltar, BorderLayout.WEST);
        panel.add(titleLabel, BorderLayout.CENTER);
        panel.add(lblDataHora, BorderLayout.EAST);
        
        return panel;
    }
    
    private JTabbedPane createTabbedPane() {
        tabbedPane = new JTabbedPane();
        tabbedPane.setFont(new Font("Arial", Font.BOLD, 14));
        tabbedPane.setBackground(new Color(245, 245, 250));
        
        // Aba de Consulta
        JPanel consultaPanel = createConsultaPanel();
        tabbedPane.addTab("🔍 Consultar Produtos", consultaPanel);
        
        // Aba de Cadastro
        JPanel cadastroPanel = createCadastroPanel();
        tabbedPane.addTab("➕ Cadastrar Produto", cadastroPanel);
        
        // Adicionar listener para destacar aba ativa
        tabbedPane.addChangeListener(e -> updateTabHighlight());
        
        return tabbedPane;
    }
    
    private JPanel createConsultaPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(new Color(250, 250, 250)); // Cinza muito suave
        
        // Painel de busca - efeito cardPanel elegante
        JPanel buscaPanel = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                // Fundo elegante com gradiente sutil
                g2d.setColor(new Color(250, 252, 252)); // Branco suave
                g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 25, 25);
                
                // Borda elegante
                g2d.setColor(new Color(189, 195, 199)); // Cinza suave
                g2d.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 25, 25);
            }
        };
        buscaPanel.setOpaque(false);
        buscaPanel.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));
        
        JLabel lblBusca = LayoutPadrao.criarRotuloCampo("🔍 Buscar Produto:");
        lblBusca.setFont(LayoutPadrao.FONTE_SUBTITULO);
        
        JPanel buscaInputPanel = LayoutPadrao.criarPainelBranco();
        buscaInputPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        txtBuscaConsulta = LayoutPadrao.criarCampoTexto(30);
        JButton btnBuscar = LayoutPadrao.criarBotaoPrimario("🔍 Buscar");
        JButton btnLimpar = LayoutPadrao.criarBotaoAlerta("🔄 Limpar");
        JButton btnAtualizar = LayoutPadrao.criarBotaoSecundario("🔄 Atualizar");
        
        buscaInputPanel.add(txtBuscaConsulta);
        buscaInputPanel.add(btnBuscar);
        buscaInputPanel.add(btnLimpar);
        buscaInputPanel.add(btnAtualizar);
        
        buscaPanel.add(lblBusca, BorderLayout.NORTH);
        buscaPanel.add(buscaInputPanel, BorderLayout.CENTER);
        
        // Tabela de produtos com campos de estoque completos
        String[] columns = {"Código", "Descrição", "Preço", "Estoque", "Estoque Mín", "Estoque Max", "Localização", "Lote", "Validade", "Categoria"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public Class<?> getColumnClass(int columnIndex) {
                if (columnIndex == 2) return Double.class; // Preço
                if (columnIndex == 3) return Integer.class; // Estoque
                return String.class;
            }
        };
        
        produtosTable = LayoutPadrao.criarTabela();
        produtosTable.setRowHeight(28);
        produtosTable.setDefaultEditor(Object.class, null);
        produtosTable.setEnabled(false);
        
        // Configurar larguras das colunas
        produtosTable.getColumnModel().getColumn(0).setPreferredWidth(80);   // Código
        produtosTable.getColumnModel().getColumn(1).setPreferredWidth(250);  // Descrição
        produtosTable.getColumnModel().getColumn(2).setPreferredWidth(80);   // Preço
        produtosTable.getColumnModel().getColumn(3).setPreferredWidth(70);   // Estoque
        produtosTable.getColumnModel().getColumn(4).setPreferredWidth(70);   // Estoque Mín
        produtosTable.getColumnModel().getColumn(5).setPreferredWidth(70);   // Estoque Max
        produtosTable.getColumnModel().getColumn(6).setPreferredWidth(90);   // Localização
        produtosTable.getColumnModel().getColumn(7).setPreferredWidth(80);   // Lote
        produtosTable.getColumnModel().getColumn(8).setPreferredWidth(80);   // Validade
        produtosTable.getColumnModel().getColumn(9).setPreferredWidth(100);  // Categoria
        
        JScrollPane scrollPane = LayoutPadrao.criarBarraRolagem(produtosTable);
        scrollPane.setPreferredSize(new Dimension(scrollPane.getPreferredSize().width, 300));
        
        // Painel de botões com LayoutPadrao
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 10));
        buttonPanel.setBackground(LayoutPadrao.COR_FUNDO_ESCURO);
        
        JButton btnEditar = LayoutPadrao.criarBotaoPrimario("✏️ Editar Produto");
        JButton btnExcluir = LayoutPadrao.criarBotaoPerigo("🗑️ Excluir Produto");
        JButton btnExportar = LayoutPadrao.criarBotaoSecundario("📤 Exportar Lista");
        
        btnEditar.addActionListener(e -> editarProduto());
        btnExcluir.addActionListener(e -> excluirProduto());
        btnExportar.addActionListener(e -> exportarProdutos());
        
        buttonPanel.add(btnEditar);
        buttonPanel.add(btnExcluir);
        buttonPanel.add(btnExportar);
        
        panel.add(buscaPanel, BorderLayout.NORTH);
        panel.add(scrollPane, BorderLayout.CENTER);
        panel.add(buttonPanel, BorderLayout.SOUTH);
        
        // Carregar dados na tabela
        atualizarTabela();
        
        // Ações dos botões
        btnBuscar.addActionListener(e -> buscarProdutos());
        btnLimpar.addActionListener(e -> limparBusca());
        btnAtualizar.addActionListener(e -> atualizarTabelaComFeedback());
        
        return panel;
    }
    
    private JPanel createCadastroPanel() {
        JPanel formPanel = new JPanel(new GridBagLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                // Fundo elegante com gradiente sutil
                g2d.setColor(new Color(248, 249, 250)); // Branco suave
                g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 25, 25);
                
                // Borda elegante
                g2d.setColor(new Color(206, 212, 218)); // Cinza suave
                g2d.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 25, 25);
            }
        };
        formPanel.setOpaque(false);
        formPanel.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
        // Código
        gbc.gridx = 0; gbc.gridy = 0; gbc.weightx = 0;
        formPanel.add(LayoutPadrao.criarRotuloCampo("Código:"), gbc);
        gbc.gridx = 1; gbc.weightx = 1.0;
        txtCodigo = LayoutPadrao.criarCampoTexto(20);
        txtCodigo.setToolTipText("Digite o código do produto");
        formPanel.add(txtCodigo, gbc);
        
        // Descrição
        gbc.gridx = 0; gbc.gridy = 1; gbc.weightx = 0;
        formPanel.add(LayoutPadrao.criarRotuloCampo("Descrição:"), gbc);
        gbc.gridx = 1; gbc.weightx = 1.0;
        txtDescricao = LayoutPadrao.criarCampoTexto(30);
        txtDescricao.setToolTipText("Digite a descrição do produto");
        formPanel.add(txtDescricao, gbc);
        
        // Preço
        gbc.gridx = 0; gbc.gridy = 2; gbc.weightx = 0;
        formPanel.add(LayoutPadrao.criarRotuloCampo("Preço:"), gbc);
        gbc.gridx = 1; gbc.weightx = 1.0;
        txtPreco = LayoutPadrao.criarCampoTexto(15);
        txtPreco.setToolTipText("Digite o preço do produto (ex: 10,50 ou 100,00)");
        formPanel.add(txtPreco, gbc);
        
        // Estoque
        gbc.gridx = 0; gbc.gridy = 3; gbc.weightx = 0;
        formPanel.add(LayoutPadrao.criarRotuloCampo("Estoque:"), gbc);
        gbc.gridx = 1; gbc.weightx = 1.0;
        txtEstoque = LayoutPadrao.criarCampoTexto(10);
        txtEstoque.setToolTipText("Digite a quantidade em estoque");
        formPanel.add(txtEstoque, gbc);
        
        // Categoria
        gbc.gridx = 0; gbc.gridy = 4; gbc.weightx = 0;
        formPanel.add(LayoutPadrao.criarRotuloCampo("Categoria:"), gbc);
        gbc.gridx = 1; gbc.weightx = 1.0;
        txtCategoria = LayoutPadrao.criarComboBox(new String[]{"Selecione..."});
        txtCategoria.setToolTipText("Selecione a categoria do produto");
        formPanel.add(txtCategoria, gbc);
        
        // Carregar categorias do banco de dados
        carregarCategorias();
        
        // Observações
        gbc.gridx = 0; gbc.gridy = 5; gbc.weightx = 0;
        gbc.anchor = GridBagConstraints.NORTHWEST;
        formPanel.add(LayoutPadrao.criarRotuloCampo("Observações:"), gbc);
        gbc.gridx = 1; gbc.gridy = 5; gbc.weightx = 1.0; gbc.weighty = 1.0;
        gbc.fill = GridBagConstraints.BOTH;
        txtObservacoes = LayoutPadrao.criarAreaTexto(3, 20);
        txtObservacoes.setToolTipText("Digite observações adicionais (opcional)");
        JScrollPane scrollPane = LayoutPadrao.criarBarraRolagem(txtObservacoes);
        formPanel.add(scrollPane, gbc);
        
        // Painel de botões com LayoutPadrao
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 10));
        buttonPanel.setBackground(LayoutPadrao.COR_FUNDO_ESCURO);
        
        JButton btnLimpar = LayoutPadrao.criarBotaoAlerta("🔄 Limpar");
        JButton btnSalvar = LayoutPadrao.criarBotaoSucesso("💾 Salvar");
        JButton btnCancelar = LayoutPadrao.criarBotaoPerigo("❌ Fechar");
        
        btnLimpar.addActionListener(e -> limparCampos());
        btnSalvar.addActionListener(this::salvarProduto);
        btnCancelar.addActionListener(e -> frame.dispose());
        
        buttonPanel.add(btnLimpar);
        buttonPanel.add(btnSalvar);
        buttonPanel.add(btnCancelar);
        
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(new Color(245, 245, 250));
        mainPanel.add(formPanel, BorderLayout.CENTER);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);
        
        return mainPanel;
    }
    
        
    private void carregarProdutosDoBanco() {
        produtos.clear();
        
        System.out.println("=== CARREGANDO PRODUTOS USANDO PADRÃO DAO ===");
        
        try {
            // Usar padrão DAO para carregar produtos
            ProdutoDao produtoDao = new ProdutoDao();
            List<com.br.hermescomercial.model.Produto> produtosDoBanco = produtoDao.listar();
            
            System.out.println("   ✅ Produtos carregados via DAO: " + produtosDoBanco.size());
            
            // Converter para o formato usado na interface com campos de estoque completos
            for (com.br.hermescomercial.model.Produto produtoModel : produtosDoBanco) {
                // Tratar preco nulo
                double preco = 0.0;
                if (produtoModel.getPrecoVenda() != null) {
                    preco = produtoModel.getPrecoVenda().doubleValue();
                } else if (produtoModel.getPreco() != null) {
                    preco = produtoModel.getPreco().doubleValue();
                }
                
                // Tratar nome/descricao
                String descricao = produtoModel.getNome() != null ? produtoModel.getNome() : "Sem descrição";
                
                Produto produto = new Produto(
                    produtoModel.getCodigo(),
                    descricao,
                    preco,
                    produtoModel.getEstoque(),
                    produtoModel.getCategoria(),
                    produtoModel.getEstoqueMinimo(),
                    produtoModel.getEstoqueMaximo(),
                    produtoModel.getLocalizacaoEstoque(),
                    produtoModel.getLote(),
                    produtoModel.getDataValidade() != null ? produtoModel.getDataValidade().toString() : ""
                );
                produtos.add(produto);
            }
            
            System.out.println("   ✅ Conversão para lista local concluída");
            
        } catch (Exception e) {
            System.err.println("   ❌ Erro ao carregar produtos via DAO: " + e.getMessage());
            e.printStackTrace();
            
            // Fallback para método antigo se DAO falhar
            System.out.println("   🔄 Tentando método fallback...");
            carregarDadosOffline();
        }
    }
    
        
    private void atualizarTabela() {
        tableModel.setRowCount(0);
        for (Produto produto : produtos) {
            Object[] row = {
                produto.getCodigo(),
                produto.getDescricao(),
                produto.getPreco(),
                produto.getEstoque(),
                produto.getEstoqueMinimo(),
                produto.getEstoqueMaximo(),
                produto.getLocalizacaoEstoque(),
                produto.getLote(),
                produto.getDataValidade(),
                produto.getCategoria()
            };
            tableModel.addRow(row);
        }
    }
    
    private void atualizarTabelaComFeedback() {
        atualizarTabela();
        
        // Feedback visual
        JOptionPane.showMessageDialog(frame, 
            "Lista de produtos atualizada!\nTotal de produtos: " + produtos.size(),
            "Lista Atualizada", JOptionPane.INFORMATION_MESSAGE);
    }
    
    private void buscarProdutos() {
        String textoBusca = txtBuscaConsulta.getText().toLowerCase().trim();
        
        if (textoBusca.isEmpty()) {
            atualizarTabela();
            return;
        }
        
        tableModel.setRowCount(0);
        for (Produto produto : produtos) {
            if (produto.getCodigo().toLowerCase().contains(textoBusca) ||
                produto.getDescricao().toLowerCase().contains(textoBusca) ||
                produto.getCategoria().toLowerCase().contains(textoBusca)) {
                Object[] row = {
                    produto.getCodigo(),
                    produto.getDescricao(),
                    produto.getPreco(),
                    produto.getEstoque(),
                    produto.getCategoria()
                };
                tableModel.addRow(row);
            }
        }
    }
    
    private void limparBusca() {
        txtBuscaConsulta.setText("");
        atualizarTabela();
        
        // Feedback visual
        JOptionPane.showMessageDialog(frame, 
            "Campo de busca limpo e lista atualizada!",
            "Busca Limpa", JOptionPane.INFORMATION_MESSAGE);
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
            // String observacoes = txtObservacoes.getText().trim(); // Não utilizado no momento
            
            // Verificar se código já existe (apenas para novos produtos)
            boolean isEdicao = !txtCodigo.isEditable();
            if (!isEdicao) {
                for (Produto p : produtos) {
                    if (p.getCodigo().equals(codigo)) {
                        JOptionPane.showMessageDialog(frame, 
                            "Já existe um produto com este código!",
                            "Código Duplicado", JOptionPane.WARNING_MESSAGE);
                        return;
                    }
                }
            }
            
            // Salvar ou atualizar produto (banco ou offline)
            boolean sucessoBanco = false;
            boolean isModoOffline = false;
            
            if (isEdicao) {
                sucessoBanco = atualizarProdutoNoBanco(codigo, descricao, preco.doubleValue(), estoque, categoria);
                if (sucessoBanco) {
                    // Atualizar na lista local
                    for (int i = 0; i < produtos.size(); i++) {
                        if (produtos.get(i).getCodigo().equals(codigo)) {
                            produtos.set(i, new Produto(codigo, descricao, preco.doubleValue(), estoque, categoria));
                            break;
                        }
                    }
                    JOptionPane.showMessageDialog(frame, 
                        "Produto atualizado com sucesso!",
                        "Produto Atualizado", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    isModoOffline = true;
                }
            } else {
                sucessoBanco = salvarProdutoNoBanco(codigo, descricao, preco.doubleValue(), estoque, categoria);
                if (sucessoBanco) {
                    // Adicionar na lista local
                    Produto novoProduto = new Produto(codigo, descricao, preco.doubleValue(), estoque, categoria);
                    produtos.add(novoProduto);
                    JOptionPane.showMessageDialog(frame, 
                        "Produto cadastrado com sucesso!",
                        "Produto Cadastrado", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    isModoOffline = true;
                }
            }
            
            // Fallback para modo offline
            if (isModoOffline) {
                System.out.println("Salvando produto em modo offline...");
                
                if (isEdicao) {
                    // Atualizar na lista local
                    for (int i = 0; i < produtos.size(); i++) {
                        if (produtos.get(i).getCodigo().equals(codigo)) {
                            produtos.set(i, new Produto(codigo, descricao, preco.doubleValue(), estoque, categoria));
                            break;
                        }
                    }
                    JOptionPane.showMessageDialog(frame, 
                        "Produto atualizado em modo offline!\n(Alterações não serão persistidas no banco)",
                        "Modo Offline", JOptionPane.WARNING_MESSAGE);
                } else {
                    // Adicionar na lista local
                    Produto novoProduto = new Produto(codigo, descricao, preco.doubleValue(), estoque, categoria);
                    produtos.add(novoProduto);
                    JOptionPane.showMessageDialog(frame, 
                        "Produto cadastrado em modo offline!\n(Dados não serão persistidos no banco)",
                        "Modo Offline", JOptionPane.WARNING_MESSAGE);
                }
            }
            
            // Atualizar tabela
            atualizarTabela();
            
            // Limpar campos e mudar para aba de consulta
            limparCampos();
            txtCodigo.setEditable(true); // Reabilitar edição de código
            tabbedPane.setSelectedIndex(0);
            
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
    
    private void editarProduto() {
        int selectedRow = produtosTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(frame, 
                "Selecione um produto para editar!",
                "Editar Produto", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        // Obter produto selecionado
        String codigo = (String) tableModel.getValueAt(selectedRow, 0);
        Produto produtoSelecionado = null;
        
        for (Produto p : produtos) {
            if (p.getCodigo().equals(codigo)) {
                produtoSelecionado = p;
                break;
            }
        }
        
        if (produtoSelecionado != null) {
            // Mudar para aba de cadastro e preencher campos
            tabbedPane.setSelectedIndex(1);
            txtCodigo.setText(produtoSelecionado.getCodigo());
            txtCodigo.setEditable(false); // Não permitir editar código
            txtDescricao.setText(produtoSelecionado.getDescricao());
            txtPreco.setText(String.valueOf(produtoSelecionado.getPreco()));
            txtEstoque.setText(String.valueOf(produtoSelecionado.getEstoque()));
            txtCategoria.setSelectedItem(produtoSelecionado.getCategoria());
            txtDescricao.requestFocus();
            
            JOptionPane.showMessageDialog(frame, 
                "Produto carregado para edição!\n" +
                "Faça as alterações desejadas e clique em Salvar.",
                "Editar Produto", JOptionPane.INFORMATION_MESSAGE);
        }
    }
    
    private void excluirProduto() {
        int selectedRow = produtosTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(frame, 
                "Selecione um produto para excluir!",
                "Excluir Produto", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        // Obter produto selecionado
        String codigo = (String) tableModel.getValueAt(selectedRow, 0);
        String descricao = (String) tableModel.getValueAt(selectedRow, 1);
        
        int confirm = com.br.hermescomercial.theme.ModernTheme.showCustomConfirmDialog(frame, 
            "Deseja realmente excluir o produto?\n\n" +
            "Código: " + codigo + "\n" +
            "Descrição: " + descricao,
            "Confirmar Exclusão", 
            new String[]{"Sim", "Não"}, 0);
        
        if (confirm == 0) {
            // Tentar excluir do banco de dados
            boolean sucessoBanco = excluirProdutoDoBanco(codigo);
            
            if (sucessoBanco) {
                // Remover da lista local
                produtos.removeIf(p -> p.getCodigo().equals(codigo));
                
                // Atualizar tabela
                atualizarTabela();
                
                JOptionPane.showMessageDialog(frame, 
                    "Produto excluído com sucesso!",
                    "Produto Excluído", JOptionPane.INFORMATION_MESSAGE);
            } else {
                // Fallback para modo offline
                System.out.println("Excluindo produto em modo offline...");
                
                // Remover da lista local
                produtos.removeIf(p -> p.getCodigo().equals(codigo));
                
                // Atualizar tabela
                atualizarTabela();
                
                JOptionPane.showMessageDialog(frame, 
                    "Produto excluído em modo offline!\n(Alteração não será persistida no banco)",
                    "Modo Offline", JOptionPane.WARNING_MESSAGE);
            }
        }
    }
    
    private void exportarProdutos() {
        StringBuilder export = new StringBuilder();
        export.append("Código;Descrição;Preço;Estoque;Categoria\n");
        
        for (Produto p : produtos) {
            export.append(p.getCodigo()).append(";");
            export.append(p.getDescricao()).append(";");
            export.append(String.format("%.2f", p.getPreco())).append(";");
            export.append(p.getEstoque()).append(";");
            export.append(p.getCategoria()).append("\n");
        }
        
        // Simulação de exportação (em implementação real, salvaria em arquivo)
        JOptionPane.showMessageDialog(frame, 
            "Lista de produtos exportada com sucesso!\n\n" +
            "Total de produtos: " + produtos.size() + "\n" +
            "Formato: CSV (separado por ;)\n\n" +
            "Em implementação real, seria salvo em arquivo.",
            "Exportação Concluída", JOptionPane.INFORMATION_MESSAGE);
        
        // Log da exportação
        System.out.println("=== EXPORTAÇÃO DE PRODUTOS ===");
        System.out.println(export.toString());
    }
    
    private boolean salvarProdutoNoBanco(String codigo, String descricao, double preco, int estoque, String categoria) {
        String sql = "INSERT INTO produto (codigo, descricao, preco, estoque, categoria, observacoes) VALUES (?, ?, ?, ?, ?, ?)";
        
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, codigo);
            pstmt.setString(2, descricao);
            pstmt.setDouble(3, preco);
            pstmt.setInt(4, estoque);
            pstmt.setString(5, categoria);
            pstmt.setString(6, "Produto cadastrado via PDV");
            
            int result = pstmt.executeUpdate();
            System.out.println("Produto salvo no banco: " + codigo + " - Linhas afetadas: " + result);
            return result > 0;
            
        } catch (SQLException e) {
            System.err.println("Erro ao salvar produto: " + e.getMessage());
            return false;
        }
    }
    
    private boolean atualizarProdutoNoBanco(String codigo, String descricao, double preco, int estoque, String categoria) {
        String sql = "UPDATE produto SET descricao = ?, preco = ?, estoque = ?, categoria = ?, data_atualizacao = CURRENT_TIMESTAMP WHERE codigo = ?";
        
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, descricao);
            pstmt.setDouble(2, preco);
            pstmt.setInt(3, estoque);
            pstmt.setString(4, categoria);
            pstmt.setString(5, codigo);
            
            int result = pstmt.executeUpdate();
            System.out.println("Produto atualizado no banco: " + codigo + " - Linhas afetadas: " + result);
            return result > 0;
            
        } catch (SQLException e) {
            System.err.println("Erro ao atualizar produto: " + e.getMessage());
            return false;
        }
    }
    
    private boolean excluirProdutoDoBanco(String codigo) {
        String sql = "DELETE FROM produto WHERE codigo = ?";
        
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, codigo);
            
            int result = pstmt.executeUpdate();
            System.out.println("Produto excluído do banco: " + codigo + " - Linhas afetadas: " + result);
            return result > 0;
            
        } catch (SQLException e) {
            System.err.println("Erro ao excluir produto: " + e.getMessage());
            return false;
        }
    }
    
    private void carregarDadosOffline() {
        System.out.println("Carregando produtos em modo offline...");
        
        produtos.add(new Produto("001", "Notebook Dell Inspire 15", 3500.0, 10, "Informática"));
        produtos.add(new Produto("002", "Mouse Wireless Logitech MX3", 89.9, 50, "Periféricos"));
        produtos.add(new Produto("003", "Teclado Mecânico RGB Gamer", 250.0, 25, "Periféricos"));
        produtos.add(new Produto("004", "Monitor 24\" LED Full HD", 899.0, 15, "Monitores"));
        produtos.add(new Produto("005", "Webcam HD 1080p com Microfone", 150.0, 30, "Acessórios"));
        
        System.out.println("Carregados " + produtos.size() + " produtos em modo offline");
    }
    
    public void show() {
        frame.setVisible(true);
    }
    
    // Classe de apoio com campos de estoque completos
    private static class Produto {
        private String codigo;
        private String descricao;
        private double preco;
        private int estoque;
        private int estoqueMinimo;
        private int estoqueMaximo;
        private String localizacaoEstoque;
        private String lote;
        private String dataValidade;
        private String categoria;
        
        // Construtor antigo para compatibilidade
        public Produto(String codigo, String descricao, double preco, int estoque, String categoria) {
            this.codigo = codigo;
            this.descricao = descricao;
            this.preco = preco;
            this.estoque = estoque;
            this.categoria = categoria;
            this.estoqueMinimo = 5;
            this.estoqueMaximo = 100;
            this.localizacaoEstoque = "A-01-01";
            this.lote = "";
            this.dataValidade = "";
        }
        
        // Construtor completo com campos de estoque
        public Produto(String codigo, String descricao, double preco, int estoque, String categoria, 
                      int estoqueMinimo, int estoqueMaximo, String localizacaoEstoque, String lote, String dataValidade) {
            this.codigo = codigo;
            this.descricao = descricao;
            this.preco = preco;
            this.estoque = estoque;
            this.categoria = categoria;
            this.estoqueMinimo = estoqueMinimo;
            this.estoqueMaximo = estoqueMaximo;
            this.localizacaoEstoque = localizacaoEstoque;
            this.lote = lote;
            this.dataValidade = dataValidade;
        }
        
        public String getCodigo() { return codigo; }
        public String getDescricao() { return descricao; }
        public double getPreco() { return preco; }
        public int getEstoque() { return estoque; }
        public int getEstoqueMinimo() { return estoqueMinimo; }
        public int getEstoqueMaximo() { return estoqueMaximo; }
        public String getLocalizacaoEstoque() { return localizacaoEstoque; }
        public String getLote() { return lote; }
        public String getDataValidade() { return dataValidade; }
        public String getCategoria() { return categoria; }
    }
    
    /**
     * Atualiza o destaque visual da aba ativa
     */
    private void updateTabHighlight() {
        int selectedIndex = tabbedPane.getSelectedIndex();
        String tabTitle = tabbedPane.getTitleAt(selectedIndex);
        
        // Atualizar título da janela com aba ativa e destaque
        frame.setTitle("📦 PDV - Gestão de Produtos v2.1.0 - Premium [📌 " + tabTitle + " ATIVA]");
        
        // Aplicar destaque visual na aba ativa
        for (int i = 0; i < tabbedPane.getTabCount(); i++) {
            if (i == selectedIndex) {
                // Aba ativa - fundo azul escuro e texto preto (letra escura)
                tabbedPane.setBackgroundAt(i, new Color(70, 130, 180)); // Steel Blue (mais escuro)
                tabbedPane.setForegroundAt(i, Color.BLACK); // Letra escura
                
                // Adicionar indicador visual
                String activeTitle = "📌 " + tabTitle + " 📌";
                tabbedPane.setTitleAt(i, activeTitle);
            } else {
                // Abas inativas - fundo cinza médio
                tabbedPane.setBackgroundAt(i, new Color(200, 200, 200)); // Gray médio
                tabbedPane.setForegroundAt(i, Color.BLACK);
                
                // Remover indicador visual
                String originalTitle = tabbedPane.getTitleAt(i).replace("📌 ", "").replace(" 📌", "");
                tabbedPane.setTitleAt(i, originalTitle);
            }
        }
        
        // Feedback visual adicional com cores mais escuras
        if (frame.getContentPane() instanceof JPanel) {
            JPanel mainPanel = (JPanel) frame.getContentPane();
            if (selectedIndex == 0) { // Consultar Produtos
                mainPanel.setBackground(new Color(230, 240, 250)); // Azul escuro suave
            } else if (selectedIndex == 1) { // Cadastrar Produto
                mainPanel.setBackground(new Color(250, 230, 230)); // Vermelho escuro suave
            }
        }
    }
    
    /**
     * Carrega as categorias cadastradas no banco de dados
     */
    private void carregarCategorias() {
        try {
            System.out.println("=== INICIANDO CARREGAMENTO DE CATEGORIAS EM GESTÃO DE PRODUTOS ===");
            
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
            
            System.out.println("=== CARREGAMENTO DE CATEGORIAS CONCLUÍDO EM GESTÃO DE PRODUTOS ===");
            
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
