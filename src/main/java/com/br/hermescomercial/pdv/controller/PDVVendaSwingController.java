package com.br.hermescomercial.pdv.controller;

import com.br.hermescomercial.ui.layout.LayoutPadrao;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import com.br.hermescomercial.service.EstoqueService;

/**
 * Controller de Venda em SWING
 * Versão 2.0 - 100% SWING - Sem JavaFX
 */
public class PDVVendaSwingController {
    
    private JFrame frame;
    private JTable produtosTable;
    private DefaultTableModel tableModel;
    private JTextField txtCodigo;
    private JTextField txtQuantidade;
    private JTextField txtDescricao;
    private JLabel lblTotal;
    private JLabel lblItens;
    private List<ItemVenda> itens;
    private EstoqueService estoqueService;
  
    
    public PDVVendaSwingController() {
        this.itens = new ArrayList<>();
        this.estoqueService = new EstoqueService();
        initializeUI();
    }
    
    private void initializeUI() {
        // Usar LayoutPadrao - tema consistente
        
        frame = new JFrame("PDV - Nova Venda v2.8.3 - LayoutPadrao");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setSize(900, 650);
        frame.setLocationRelativeTo(null);
        
        // Configurar fundo com LayoutPadrao
        frame.getContentPane().setBackground(LayoutPadrao.COR_FUNDO_ESCURO);
        
        frame.add(createMainPanel());
    }
    
    private JPanel createMainPanel() {
        // Usando apenas fallback para evitar erro "0 >= 0"
        return createFallbackMainPanel();
    }
    
    private JPanel createFallbackMainPanel() {
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(Color.WHITE);
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        // Header simples
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(new Color(41, 128, 185));
        headerPanel.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));
        headerPanel.setPreferredSize(new Dimension(800, 60));
        
        JLabel titleLabel = new JLabel(" Nova Venda - PDV");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
        titleLabel.setForeground(Color.WHITE);
        
        headerPanel.add(titleLabel, BorderLayout.WEST);
        mainPanel.add(headerPanel, BorderLayout.NORTH);
        
        // Painel central
        JPanel centerPanel = new JPanel(new BorderLayout());
        centerPanel.setBackground(Color.WHITE);
        centerPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        
        // Tabela de produtos
        centerPanel.add(createEnhancedProdutosPanel(), BorderLayout.CENTER);
        
        // Painel de entrada
        centerPanel.add(createEnhancedInputPanel(), BorderLayout.SOUTH);
        
        mainPanel.add(centerPanel, BorderLayout.CENTER);
        
        // Painel lateral
        mainPanel.add(createEnhancedSidePanel(), BorderLayout.EAST);
        
        return mainPanel;
    }
    
    private JPanel createEnhancedInputPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createTitledBorder("➕ Adicionar Produto"),
            BorderFactory.createEmptyBorder(15, 15, 15, 15)
        ));
        panel.setBackground(new Color(255, 255, 255));
        
        // Campos de entrada com LayoutPadrao
        JPanel inputPanel = LayoutPadrao.criarPainelBranco();
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
        // Linha 1: Código e Quantidade
        gbc.gridx = 0; gbc.gridy = 0; gbc.weightx = 1.0;
        inputPanel.add(LayoutPadrao.criarRotuloCampo("Código:"), gbc);
        
        gbc.gridx = 1; gbc.weightx = 0.7;
        txtCodigo = LayoutPadrao.criarCampoTexto(15);
        inputPanel.add(txtCodigo, gbc);
        
        gbc.gridx = 2; gbc.weightx = 1.0;
        inputPanel.add(LayoutPadrao.criarRotuloCampo("Quantidade:"), gbc);
        
        gbc.gridx = 3; gbc.weightx = 0.7;
        txtQuantidade = LayoutPadrao.criarCampoTexto(10);
        txtQuantidade.setText("1");
        inputPanel.add(txtQuantidade, gbc);
        
        // Linha 2: Descrição (readonly)
        gbc.gridx = 0; gbc.gridy = 1; gbc.weightx = 1.0;
        inputPanel.add(LayoutPadrao.criarRotuloCampo("Descrição:"), gbc);
        
        gbc.gridx = 1; gbc.gridwidth = 3; gbc.weightx = 2.0;
        txtDescricao = LayoutPadrao.criarCampoTexto(40);
        txtDescricao.setEditable(false);
        txtDescricao.setBackground(LayoutPadrao.COR_FUNDO_ESCURO);
        inputPanel.add(txtDescricao, gbc);
        
        // Painel de botões de ação
        JPanel actionPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 5));
        actionPanel.setBackground(LayoutPadrao.COR_FUNDO_ESCURO);
        
        JButton btnAdicionar = LayoutPadrao.criarBotaoSucesso("➕ Adicionar");
        JButton btnBuscar = LayoutPadrao.criarBotaoPrimario("🔍 Buscar");
        
        btnAdicionar.addActionListener(e -> adicionarProduto());
        btnBuscar.addActionListener(e -> buscarProdutoPorCodigo(txtCodigo.getText().trim()));
        
        actionPanel.add(btnBuscar);
        actionPanel.add(btnAdicionar);
        
        panel.add(inputPanel, BorderLayout.CENTER);
        panel.add(actionPanel, BorderLayout.SOUTH);
        
        return panel;
    }
    
    private JPanel createEnhancedProdutosPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createTitledBorder(" Itens da Venda"),
            BorderFactory.createEmptyBorder(15, 15, 15, 5)
        ));
        panel.setBackground(new Color(255, 255, 255));
        
        // Tabela de produtos com design melhorado
        String[] columns = {"Código", "Descrição", "Qtd", "Unitário", "Total"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
            
            @Override
            public Class<?> getColumnClass(int columnIndex) {
                if (columnIndex == 2) return Integer.class; // Quantidade
                if (columnIndex == 3 || columnIndex == 4) return Double.class; // Valores
                return String.class;
            }
        };
        
        produtosTable = new JTable(tableModel);
        produtosTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        produtosTable.getTableHeader().setReorderingAllowed(false);
        produtosTable.setRowHeight(28);
        produtosTable.setDefaultEditor(Object.class, null);
        produtosTable.setEnabled(false);
        produtosTable.setBackground(Color.WHITE);
        
        // Configurar larguras das colunas
        produtosTable.getColumnModel().getColumn(0).setPreferredWidth(80);
        produtosTable.getColumnModel().getColumn(1).setPreferredWidth(350);
        produtosTable.getColumnModel().getColumn(2).setPreferredWidth(60);
        produtosTable.getColumnModel().getColumn(3).setPreferredWidth(100);
        produtosTable.getColumnModel().getColumn(4).setPreferredWidth(100);
        
        // Scroll pane simples
        JScrollPane scrollPane = new JScrollPane(produtosTable);
        scrollPane.setPreferredSize(new Dimension(600, 250));
        panel.add(scrollPane, BorderLayout.CENTER);
        
        // Painel de botões simples
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 5));
        buttonPanel.setBackground(Color.WHITE);
        
        JButton btnRemover = new JButton("🗑️ Remover Item");
        btnRemover.setBackground(new Color(231, 76, 60));
        btnRemover.setForeground(Color.WHITE);
        btnRemover.setFocusPainted(false);
        
        JButton btnLimpar = new JButton("🔄 Limpar Venda");
        btnLimpar.setBackground(new Color(230, 126, 34));
        btnLimpar.setForeground(Color.WHITE);
        btnLimpar.setFocusPainted(false);
        
        btnRemover.addActionListener(e -> removerItem());
        btnLimpar.addActionListener(e -> limparVenda());
        
        buttonPanel.add(btnRemover);
        buttonPanel.add(btnLimpar);
        
        panel.add(buttonPanel, BorderLayout.SOUTH);
        
        return panel;
    }
    
    private JPanel createEnhancedSidePanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(5, 20, 5, 5));
        panel.setOpaque(false);
        panel.setPreferredSize(new Dimension(300, 200));
        
        // Painel de resumo
        JPanel summaryPanel = LayoutPadrao.criarPainelBranco();
        summaryPanel.setLayout(new BorderLayout());
        summaryPanel.setBorder(BorderFactory.createTitledBorder("Resumo da Venda"));
        
        JLabel resumoLabel = new JLabel("<html><center>" +
            "<b>Total: R$ 0,00</b><br>" +
            "<small>0 itens</small>" +
            "</center></html>", SwingConstants.CENTER);
        resumoLabel.setFont(new Font("Arial", Font.BOLD, 16));
        resumoLabel.setForeground(LayoutPadrao.COR_PRIMARIA);
        
        summaryPanel.add(resumoLabel, BorderLayout.CENTER);
        panel.add(summaryPanel, BorderLayout.CENTER);
        
        return panel;
    }
    
    // Métodos auxiliares removidos - usando LayoutPadrao
    // LayoutPadrao já fornece métodos para criar componentes estilizados
    
    private void adicionarProduto() {
        String codigo = txtCodigo.getText().trim();
        String quantidadeStr = txtQuantidade.getText().trim();
        
        if (codigo.isEmpty()) {
            JOptionPane.showMessageDialog(frame, "Digite o código do produto!", "Aviso", JOptionPane.WARNING_MESSAGE);
            txtCodigo.requestFocus();
            return;
        }
        
        int quantidade;
        try {
            quantidade = Integer.parseInt(quantidadeStr);
            if (quantidade <= 0) {
                JOptionPane.showMessageDialog(frame, "Quantidade deve ser maior que zero!", "Aviso", JOptionPane.WARNING_MESSAGE);
                txtQuantidade.requestFocus();
                return;
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(frame, "Quantidade inválida!", "Erro", JOptionPane.ERROR_MESSAGE);
            txtQuantidade.requestFocus();
            return;
        }
        
        Produto produto = buscarProdutoPorCodigo(codigo);
        if (produto == null) {
            JOptionPane.showMessageDialog(frame, "Produto não encontrado!", "Erro", JOptionPane.ERROR_MESSAGE);
            txtCodigo.selectAll();
            txtCodigo.requestFocus();
            return;
        }
        
        // VALIDAÇÃO DE ESTOQUE COM SERVIÇO ESPECIALIZADO
        EstoqueService.ResultadoValidacao validacaoEstoque = estoqueService.validarVenda(codigo, quantidade);
        
        switch (validacaoEstoque) {
            case ESTOQUE_INSUFICIENTE:
                EstoqueService.StatusEstoque status = estoqueService.getStatusEstoque(codigo);
                String mensagemAlerta = estoqueService.getMensagemAlerta(status);
                
                JOptionPane.showMessageDialog(frame, 
                    mensagemAlerta + "!\n\n" +
                    "Produto: " + produto.getDescricao() + "\n" +
                    "Estoque disponível: " + produto.getEstoque() + "\n" +
                    "Quantidade solicitada: " + quantidade + "\n" +
                    "━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━\n" +
                    "📍 Localização: " + produto.getLocalizacaoEstoque() + "\n" +
                    "🏷️ Lote: " + produto.getLote() + "\n" +
                    "📅 Validade: " + produto.getDataValidade() + "\n\n" +
                    "💡 Sugestão de reposição: " + estoqueService.calcularSugestaoReposicao(codigo, 30) + " unidades",
                    "⚠️ ESTOQUE INSUFICIENTE", 
                    JOptionPane.WARNING_MESSAGE);
                txtQuantidade.selectAll();
                txtQuantidade.requestFocus();
                return;
                
            case PRODUTO_INEXISTENTE:
                JOptionPane.showMessageDialog(frame, "Produto não encontrado no sistema!", "Erro", JOptionPane.ERROR_MESSAGE);
                txtCodigo.selectAll();
                txtCodigo.requestFocus();
                return;
                
            case QUANTIDADE_INVALIDA:
                JOptionPane.showMessageDialog(frame, "Quantidade deve ser maior que zero!", "Aviso", JOptionPane.WARNING_MESSAGE);
                txtQuantidade.selectAll();
                txtQuantidade.requestFocus();
                return;
                
            case OK:
                // Continuar com a venda
                break;
        }
        
        // VERIFICAÇÃO ADICIONAL (LEGACY)
        if (produto.getEstoque() < quantidade) {
            JOptionPane.showMessageDialog(frame, 
                "📦 ESTOQUE INSUFICIENTE!\n\n" +
                "Produto: " + produto.getDescricao() + "\n" +
                "Estoque disponível: " + produto.getEstoque() + "\n" +
                "Quantidade solicitada: " + quantidade + "\n" +
                "━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━\n" +
                "📍 Localização: " + produto.getLocalizacaoEstoque() + "\n" +
                "🏷️ Lote: " + produto.getLote() + "\n" +
                "📅 Validade: " + produto.getDataValidade(),
                "Estoque Insuficiente", JOptionPane.WARNING_MESSAGE);
            txtQuantidade.requestFocus();
            txtQuantidade.selectAll();
            return;
        }
        
        // VERIFICAÇÃO DE ESTOQUE MÍNIMO
        if (produto.estaAbaixoEstoqueMinimo()) {
            JOptionPane.showMessageDialog(frame, 
                "⚠️ ATENÇÃO: ESTOQUE MÍNIMO!\n\n" +
                "Produto: " + produto.getDescricao() + "\n" +
                "Estoque atual: " + produto.getEstoque() + "\n" +
                "Estoque mínimo: " + produto.getEstoqueMinimo() + "\n" +
                "━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━\n" +
                "📍 Localização: " + produto.getLocalizacaoEstoque() + "\n" +
                "🏷️ Lote: " + produto.getLote() + "\n" +
                "📅 Validade: " + produto.getDataValidade() + "\n\n" +
                "⚠️ É necessário repor este produto em breve!",
                "Alerta de Estoque Mínimo", JOptionPane.WARNING_MESSAGE);
        }
        
        // VERIFICAÇÃO DE ESTOQUE MÁXIMO (caso esteja vendendo e ultrapassar o limite)
        if (produto.estaAcimaEstoqueMaximo()) {
            JOptionPane.showMessageDialog(frame, 
                "📊 ATENÇÃO: ESTOQUE ACIMA DO MÁXIMO!\n\n" +
                "Produto: " + produto.getDescricao() + "\n" +
                "Estoque atual: " + produto.getEstoque() + "\n" +
                "Estoque máximo: " + produto.getEstoqueMaximo() + "\n" +
                "━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━\n" +
                "📍 Localização: " + produto.getLocalizacaoEstoque() + "\n" +
                "🏷️ Lote: " + produto.getLote() + "\n" +
                "📅 Validade: " + produto.getDataValidade() + "\n\n" +
                "📊 Estoque acima do nível máximo recomendado!",
                "Alerta de Estoque Máximo", JOptionPane.INFORMATION_MESSAGE);
        }
        
        // Verificar se produto já está no carrinho
        for (ItemVenda item : itens) {
            if (item.getProduto().getCodigo().equals(codigo)) {
                int novaQuantidade = item.getQuantidade() + quantidade;
                if (produto.getEstoque() < novaQuantidade) {
                    JOptionPane.showMessageDialog(frame, 
                        "Estoque insuficiente para adicionar mais unidades!\n" +
                        "Estoque disponível: " + produto.getEstoque() + "\n" +
                        "Quantidade total no carrinho: " + novaQuantidade,
                        "Estoque Insuficiente", JOptionPane.WARNING_MESSAGE);
                    txtQuantidade.requestFocus();
                    return;
                }
                item.setQuantidade(novaQuantidade);
                atualizarTabela();
                atualizarResumo();
                limparCampos();
                return;
            }
        }
        
        // Adicionar novo item
        ItemVenda item = new ItemVenda(produto, quantidade);
        itens.add(item);
        
        // Adicionar à tabela
        Object[] rowData = {
            produto.getCodigo(),
            produto.getDescricao(),
            quantidade,
            produto.getPreco(),
            produto.getPreco().multiply(new BigDecimal(quantidade))
        };
        tableModel.addRow(rowData);
        
        atualizarResumo();
        atualizarTabela();
        limparCampos();
    }
    
    private void atualizarTabela() {
        tableModel.setRowCount(0);
        for (ItemVenda item : itens) {
            Produto produto = item.getProduto();
            Object[] rowData = {
                produto.getCodigo(),
                produto.getDescricao(),
                item.getQuantidade(),
                produto.getPreco(),
                produto.getPreco().multiply(new BigDecimal(item.getQuantidade()))
            };
            tableModel.addRow(rowData);
        }
    }
    
    private void limparCampos() {
        txtCodigo.setText("");
        txtQuantidade.setText("1");
        txtCodigo.requestFocus();
    }
    
    private void removerItem() {
        int selectedRow = produtosTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(frame, "Selecione um item para remover!", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        int confirm = JOptionPane.showConfirmDialog(frame, 
            "Deseja remover este item?", "Confirmar", 
            JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
            
        if (confirm == 0) {
            itens.remove(selectedRow);
            tableModel.removeRow(selectedRow);
            atualizarResumo();
        }
    }
    
    private void limparVenda() {
        int confirm = JOptionPane.showConfirmDialog(frame, 
            "Deseja limpar todos os itens da venda?", "Confirmar", 
            JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
            
        if (confirm == 0) {
            itens.clear();
            tableModel.setRowCount(0);
            atualizarResumo();
            txtCodigo.setText("");
            txtQuantidade.setText("1");
        }
    }
    
        
    private void atualizarResumo() {
        lblItens.setText("Itens: " + itens.size());
        lblTotal.setText("Total: R$ " + String.format("%.2f", calcularTotal()));
    }
    
    private BigDecimal calcularTotal() {
        return itens.stream()
            .map(ItemVenda::getSubtotal)
            .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
    
    // Simulação de busca de produto com controle de estoque completo
    private Produto buscarProdutoPorCodigo(String codigo) {
        // Produtos de exemplo com controle completo de estoque
        if ("001".equals(codigo)) {
            Produto produto = new Produto("001", "Produto Exemplo 1", new BigDecimal("10.50"), 50);
            produto.setEstoqueMinimo(10);
            produto.setEstoqueMaximo(200);
            produto.setLocalizacaoEstoque("A-01-01");
            produto.setLote("L2024001");
            produto.setDataValidade("31/12/2025");
            return produto;
        } else if ("002".equals(codigo)) {
            Produto produto = new Produto("002", "Produto Exemplo 2", new BigDecimal("25.99"), 30);
            produto.setEstoqueMinimo(5);
            produto.setEstoqueMaximo(100);
            produto.setLocalizacaoEstoque("B-02-03");
            produto.setLote("L2024002");
            produto.setDataValidade("30/06/2025");
            return produto;
        } else if ("003".equals(codigo)) {
            Produto produto = new Produto("003", "Produto Exemplo 3", new BigDecimal("5.75"), 100);
            produto.setEstoqueMinimo(20);
            produto.setEstoqueMaximo(500);
            produto.setLocalizacaoEstoque("C-03-02");
            produto.setLote("L2024003");
            produto.setDataValidade("15/09/2025");
            return produto;
        } else if ("004".equals(codigo)) {
            // Produto com estoque baixo para demonstrar alerta
            Produto produto = new Produto("004", "Produto Estoque Baixo", new BigDecimal("15.00"), 3);
            produto.setEstoqueMinimo(10);
            produto.setEstoqueMaximo(50);
            produto.setLocalizacaoEstoque("D-04-01");
            produto.setLote("L2024004");
            produto.setDataValidade("30/11/2024");
            return produto;
        }
        return null;
    }
    
        
        
    public void show() {
        frame.setVisible(true);
    }
    
    // Classes de apoio
    private static class Produto {
        private String codigo;
        private String descricao;
        private BigDecimal preco;
        private int estoque;
        private int estoqueMinimo;
        private int estoqueMaximo;
        private String localizacaoEstoque;
        private String lote;
        private String dataValidade; // Simplificado como String para exemplo
        
        public Produto(String codigo, String descricao, BigDecimal preco, int estoque) {
            this.codigo = codigo;
            this.descricao = descricao;
            this.preco = preco;
            this.estoque = estoque;
            this.estoqueMinimo = 5;
            this.estoqueMaximo = 1000; // Corrigido: valor maior que estoque inicial
            this.localizacaoEstoque = "A-01-01";
            this.lote = "L001";
            this.dataValidade = "31/12/2025";
        }
        
        public String getCodigo() { return codigo; }
        public String getDescricao() { return descricao; }
        public BigDecimal getPreco() { return preco; }
        public int getEstoque() { return estoque; }
        public int getEstoqueMinimo() { return estoqueMinimo; }
        public int getEstoqueMaximo() { return estoqueMaximo; }
        public String getLocalizacaoEstoque() { return localizacaoEstoque; }
        public String getLote() { return lote; }
        public String getDataValidade() { return dataValidade; }
        
        public void setEstoqueMinimo(int estoqueMinimo) { this.estoqueMinimo = estoqueMinimo; }
        public void setEstoqueMaximo(int estoqueMaximo) { this.estoqueMaximo = estoqueMaximo; }
        public void setLocalizacaoEstoque(String localizacaoEstoque) { this.localizacaoEstoque = localizacaoEstoque; }
        public void setLote(String lote) { this.lote = lote; }
        public void setDataValidade(String dataValidade) { this.dataValidade = dataValidade; }
        
        // Método para verificar se está abaixo do estoque mínimo
        public boolean estaAbaixoEstoqueMinimo() {
            return estoque <= estoqueMinimo;
        }
        
        // Método para verificar se está acima do estoque máximo
        public boolean estaAcimaEstoqueMaximo() {
            return estoque >= estoqueMaximo;
        }
    }
    
    private static class ItemVenda {
        private Produto produto;
        private int quantidade;
        
        public ItemVenda(Produto produto, int quantidade) {
            this.produto = produto;
            this.quantidade = quantidade;
        }
        
        public Produto getProduto() { return produto; }
        public int getQuantidade() { return quantidade; }
        public void setQuantidade(int quantidade) { this.quantidade = quantidade; }
        
        public BigDecimal getSubtotal() {
            return produto.getPreco().multiply(new BigDecimal(quantidade));
        }
    }
}
