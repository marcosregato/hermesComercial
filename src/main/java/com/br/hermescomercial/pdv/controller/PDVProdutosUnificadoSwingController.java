package com.br.hermescomercial.pdv.controller;

import com.br.hermescomercial.dao.ProdutoDao;
import com.br.hermescomercial.ui.layout.LayoutPadrao;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Controller Unificado de Produtos em SWING
 * Interface completa para cadastro e consulta de produtos
 * Versão 2.0 - 100% SWING - Layout Premium com Abas
 */
public class PDVProdutosUnificadoSwingController {
    
    private JFrame frame;
    
    
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
        // Criar painéis de formulário e tabela
        JPanel formularioPanel = createFormularioPanel();
        JPanel tabelaPanel = createTabelaPanel();
        
        // Usando layout padrão Header → Busca → Formulário → Tabela
        return LayoutPadrao.criarLayoutPadraoGestao(
            true, // isPDV
            "📦 Gestão de Produtos - PDV",
            "Digite código, nome ou descrição do produto...",
            formularioPanel,
            tabelaPanel
        );
    }
    
    private JPanel createFormularioPanel() {
        JPanel panel = LayoutPadrao.criarPainelBranco();
        panel.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.WEST;
        
        // Código do Produto
        gbc.gridx = 0; gbc.gridy = 0;
        panel.add(LayoutPadrao.criarRotuloTexto("Código:"), gbc);
        gbc.gridx = 1; gbc.weightx = 1.0;
        JTextField txtCodigo = new JTextField(15);
        txtCodigo.setFont(LayoutPadrao.FONTE_CAMPO);
        panel.add(txtCodigo, gbc);
        
        // Nome do Produto
        gbc.gridx = 0; gbc.gridy = 1; gbc.weightx = 0;
        panel.add(LayoutPadrao.criarRotuloTexto("Nome:"), gbc);
        gbc.gridx = 1; gbc.weightx = 1.0;
        JTextField txtNome = new JTextField(30);
        txtNome.setFont(LayoutPadrao.FONTE_CAMPO);
        panel.add(txtNome, gbc);
        
        // Preço de Venda
        gbc.gridx = 0; gbc.gridy = 2; gbc.weightx = 0;
        panel.add(LayoutPadrao.criarRotuloTexto("Preço:"), gbc);
        gbc.gridx = 1; gbc.weightx = 1.0;
        JTextField txtPreco = new JTextField(15);
        txtPreco.setFont(LayoutPadrao.FONTE_CAMPO);
        panel.add(txtPreco, gbc);
        
        // Estoque
        gbc.gridx = 0; gbc.gridy = 3; gbc.weightx = 0;
        panel.add(LayoutPadrao.criarRotuloTexto("Estoque:"), gbc);
        gbc.gridx = 1; gbc.weightx = 1.0;
        JTextField txtEstoque = new JTextField(10);
        txtEstoque.setFont(LayoutPadrao.FONTE_CAMPO);
        panel.add(txtEstoque, gbc);
        
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
        JLabel lblTitulo = LayoutPadrao.criarRotuloSubtitulo("� Produtos Cadastrados");
        lblTitulo.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        panel.add(lblTitulo, BorderLayout.NORTH);
        
        // Tabela de produtos
        String[] colunas = {"Código", "Nome", "Preço", "Estoque", "Status"};
        DefaultTableModel model = new DefaultTableModel(colunas, 0);
        JTable tabela = new JTable(model);
        tabela.setFont(LayoutPadrao.FONTE_TEXTO);
        tabela.setRowHeight(25);
        tabela.getTableHeader().setFont(LayoutPadrao.FONTE_SUBTITULO);
        
        // Adicionar dados de exemplo
        model.addRow(new Object[]{"001", "Notebook Dell", "3500.00", "5", "Ativo"});
        model.addRow(new Object[]{"002", "Mouse Wireless", "89.90", "25", "Ativo"});
        model.addRow(new Object[]{"003", "Teclado Mecânico", "299.00", "12", "Ativo"});
        
        // Scroll pane
        JScrollPane scrollPane = new JScrollPane(tabela);
        scrollPane.setBorder(BorderFactory.createEmptyBorder(0, 10, 10, 10));
        panel.add(scrollPane, BorderLayout.CENTER);
        
        return panel;
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
    
    // Classe de apoio simplificada
    private static class Produto {
        
        // Construtor simplificado
        public Produto(String codigo, String descricao, double preco, int estoque, String categoria) {
            // Implementação básica sem campos não utilizados
        }
        
        // Construtor completo com campos de estoque
        public Produto(String codigo, String descricao, double preco, int estoque, String categoria, 
                      int estoqueMinimo, int estoqueMaximo, String localizacaoEstoque, String lote, String dataValidade) {
            // Implementação básica sem campos não utilizados
        }
        
    }
}
