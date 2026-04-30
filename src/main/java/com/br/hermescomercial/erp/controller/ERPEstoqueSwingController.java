package com.br.hermescomercial.erp.controller;

import com.br.hermescomercial.shared.model.Produto;
import com.br.hermescomercial.shared.model.MetodoControleEstoque;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

/**
 * Controller para tela de gestão de estoque no ERP
 * Versão 2.4.0 - Controle completo com PEPS/UEPS/Custo Médio
 */
public class ERPEstoqueSwingController {
    
    private JFrame frame;
    private JPanel mainPanel;
    private JTable estoqueTable;
    private DefaultTableModel tableModel;
    private JTextField txtBusca, txtProduto, txtQuantidade, txtCusto, txtLote;
    private JTextArea txtObservacoes;
    private JButton btnEntrada, btnSaida, btnAjuste, btnRelatorio, btnAlertas, btnLotes, btnAtualizar;
    private JComboBox<String> cbTipoBusca;
    private JLabel lblTotalProdutos, lblValorTotalEstoque, lblAlertasEstoque, lblEstoqueBaixo;
    private DecimalFormat currencyFormat;
    private List<Produto> produtos;
    
    public ERPEstoqueSwingController() {
        this.produtos = new ArrayList<>();
        initializeUI();
        carregarDadosExemplo();
        atualizarEstatisticas();
    }
    
    private void initializeUI() {
        frame = new JFrame("📦 Gestão de Estoque - ERP");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setSize(1400, 800);
        frame.setLocationRelativeTo(null);
        
        // Formato para moeda
        currencyFormat = new DecimalFormat("R$ #,##0.00");
        
        // Aplicar tema padrão das telas antigas
        frame.getContentPane().setBackground(new Color(250, 250, 250));
        
        // Painel principal
        mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        mainPanel.setBackground(new Color(250, 250, 250));
        
        // Painel superior com busca e estatísticas
        JPanel topPanel = criarPainelSuperior();
        
        // Painel central com tabela
        JPanel centerPanel = criarPainelCentral();
        
        // Painel inferior com botões de operação
        JPanel bottomPanel = criarPainelInferior();
        
        mainPanel.add(topPanel, BorderLayout.NORTH);
        mainPanel.add(centerPanel, BorderLayout.CENTER);
        mainPanel.add(bottomPanel, BorderLayout.SOUTH);
        
        frame.add(mainPanel);
        frame.setVisible(true);
    }
    
    private JPanel criarPainelSuperior() {
        JPanel topPanel = new JPanel(new BorderLayout(10, 10));
        topPanel.setBackground(new Color(250, 250, 250));
        
        // Painel de busca
        JPanel buscaPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        buscaPanel.setBackground(new Color(250, 250, 250));
        
        buscaPanel.add(new JLabel("🔍 Buscar:"));
        txtBusca = new JTextField(20);
        buscaPanel.add(txtBusca);
        
        cbTipoBusca = new JComboBox<>(new String[]{"Todos", "Nome", "Código", "Categoria", "Localização"});
        buscaPanel.add(cbTipoBusca);
        
        JButton btnBuscar = new JButton("🔍 Buscar");
        btnBuscar.setBackground(new Color(70, 130, 180));
        btnBuscar.setForeground(Color.WHITE);
        btnBuscar.setFont(new Font("Arial", Font.BOLD, 12));
        btnBuscar.addActionListener(this::buscarProdutos);
        buscaPanel.add(btnBuscar);
        
        JButton btnLimparBusca = new JButton("🧹 Limpar");
        btnLimparBusca.setBackground(new Color(108, 117, 125));
        btnLimparBusca.setForeground(Color.WHITE);
        btnLimparBusca.setFont(new Font("Arial", Font.BOLD, 12));
        btnLimparBusca.addActionListener(e -> {
            txtBusca.setText("");
            cbTipoBusca.setSelectedIndex(0);
            carregarDadosExemplo();
        });
        buscaPanel.add(btnLimparBusca);
        
        // Painel de estatísticas
        JPanel statsPanel = new JPanel(new GridLayout(2, 2, 10, 5));
        statsPanel.setBackground(new Color(250, 250, 250));
        statsPanel.setBorder(BorderFactory.createTitledBorder("📊 Estatísticas do Estoque"));
        
        lblTotalProdutos = new JLabel("📦 Total de Produtos: 0");
        lblTotalProdutos.setFont(new Font("Arial", Font.BOLD, 14));
        lblTotalProdutos.setForeground(new Color(0, 123, 255));
        statsPanel.add(lblTotalProdutos);
        
        lblValorTotalEstoque = new JLabel("💰 Valor Total: R$ 0,00");
        lblValorTotalEstoque.setFont(new Font("Arial", Font.BOLD, 14));
        lblValorTotalEstoque.setForeground(new Color(40, 167, 69));
        statsPanel.add(lblValorTotalEstoque);
        
        lblAlertasEstoque = new JLabel("⚠️ Alertas: 0");
        lblAlertasEstoque.setFont(new Font("Arial", Font.BOLD, 14));
        lblAlertasEstoque.setForeground(new Color(255, 193, 7));
        statsPanel.add(lblAlertasEstoque);
        
        lblEstoqueBaixo = new JLabel("🔴 Estoque Baixo: 0");
        lblEstoqueBaixo.setFont(new Font("Arial", Font.BOLD, 14));
        lblEstoqueBaixo.setForeground(new Color(220, 53, 69));
        statsPanel.add(lblEstoqueBaixo);
        
        topPanel.add(buscaPanel, BorderLayout.NORTH);
        topPanel.add(statsPanel, BorderLayout.CENTER);
        
        return topPanel;
    }
    
    private JPanel criarPainelCentral() {
        JPanel centerPanel = new JPanel(new BorderLayout());
        centerPanel.setBackground(new Color(250, 250, 250));
        
        // Tabela de estoque
        String[] colunas = {"Código", "Produto", "Categoria", "Estoque Atual", "Estoque Mínimo", 
                           "Máximo", "Localização", "Método", "Status", "Valor Unitário", "Valor Total"};
        tableModel = new DefaultTableModel(colunas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        estoqueTable = new JTable(tableModel);
        estoqueTable.setRowHeight(25);
        estoqueTable.setFont(new Font("Arial", Font.PLAIN, 12));
        estoqueTable.getTableHeader().setFont(new Font("Arial", Font.BOLD, 12));
        estoqueTable.getTableHeader().setBackground(new Color(108, 117, 125));
        estoqueTable.getTableHeader().setForeground(Color.WHITE);
        
        // Customizar renderização da coluna Status
        estoqueTable.getColumnModel().getColumn(8).setCellRenderer(new StatusCellRenderer());
        
        // Customizar renderização da coluna Valor
        estoqueTable.getColumnModel().getColumn(9).setCellRenderer(new CurrencyCellRenderer());
        estoqueTable.getColumnModel().getColumn(10).setCellRenderer(new CurrencyCellRenderer());
        
        // Ajustar largura das colunas
        estoqueTable.getColumnModel().getColumn(0).setPreferredWidth(80);  // Código
        estoqueTable.getColumnModel().getColumn(1).setPreferredWidth(200); // Produto
        estoqueTable.getColumnModel().getColumn(2).setPreferredWidth(120); // Categoria
        estoqueTable.getColumnModel().getColumn(3).setPreferredWidth(80);  // Estoque Atual
        estoqueTable.getColumnModel().getColumn(4).setPreferredWidth(80);  // Mínimo
        estoqueTable.getColumnModel().getColumn(5).setPreferredWidth(80);  // Máximo
        estoqueTable.getColumnModel().getColumn(6).setPreferredWidth(100); // Localização
        estoqueTable.getColumnModel().getColumn(7).setPreferredWidth(100); // Método
        estoqueTable.getColumnModel().getColumn(8).setPreferredWidth(80);  // Status
        estoqueTable.getColumnModel().getColumn(9).setPreferredWidth(100); // Valor Unitário
        estoqueTable.getColumnModel().getColumn(10).setPreferredWidth(100); // Valor Total
        
        JScrollPane scrollPane = new JScrollPane(estoqueTable);
        scrollPane.setBorder(BorderFactory.createTitledBorder("📋 Produtos em Estoque"));
        
        centerPanel.add(scrollPane, BorderLayout.CENTER);
        
        return centerPanel;
    }
    
    private JPanel criarPainelInferior() {
        JPanel bottomPanel = new JPanel(new BorderLayout(10, 10));
        bottomPanel.setBackground(new Color(250, 250, 250));
        
        // Painel de operações
        JPanel operacoesPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        operacoesPanel.setBackground(new Color(250, 250, 250));
        operacoesPanel.setBorder(BorderFactory.createTitledBorder("🔧 Operações de Estoque"));
        
        btnEntrada = new JButton("📥 Entrada de Mercadorias");
        btnEntrada.setBackground(new Color(40, 167, 69));
        btnEntrada.setForeground(Color.WHITE);
        btnEntrada.setFont(new Font("Arial", Font.BOLD, 12));
        btnEntrada.setPreferredSize(new Dimension(180, 35));
        btnEntrada.addActionListener(this::entradaMercadorias);
        operacoesPanel.add(btnEntrada);
        
        btnSaida = new JButton("📤 Saída de Mercadorias");
        btnSaida.setBackground(new Color(220, 53, 69));
        btnSaida.setForeground(Color.WHITE);
        btnSaida.setFont(new Font("Arial", Font.BOLD, 12));
        btnSaida.setPreferredSize(new Dimension(180, 35));
        btnSaida.addActionListener(this::saidaMercadorias);
        operacoesPanel.add(btnSaida);
        
        btnAjuste = new JButton("⚙️ Ajuste Manual");
        btnAjuste.setBackground(new Color(255, 193, 7));
        btnAjuste.setForeground(Color.BLACK);
        btnAjuste.setFont(new Font("Arial", Font.BOLD, 12));
        btnAjuste.setPreferredSize(new Dimension(150, 35));
        btnAjuste.addActionListener(this::ajusteManual);
        operacoesPanel.add(btnAjuste);
        
        // Painel de relatórios e ferramentas
        JPanel ferramentasPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        ferramentasPanel.setBackground(new Color(250, 250, 250));
        ferramentasPanel.setBorder(BorderFactory.createTitledBorder("📊 Relatórios e Ferramentas"));
        
        btnRelatorio = new JButton("📈 Relatório de Valorização");
        btnRelatorio.setBackground(new Color(23, 162, 184));
        btnRelatorio.setForeground(Color.WHITE);
        btnRelatorio.setFont(new Font("Arial", Font.BOLD, 12));
        btnRelatorio.setPreferredSize(new Dimension(180, 35));
        btnRelatorio.addActionListener(this::gerarRelatorioValorizacao);
        ferramentasPanel.add(btnRelatorio);
        
        btnAlertas = new JButton("🔔 Gerenciar Alertas");
        btnAlertas.setBackground(new Color(255, 127, 80));
        btnAlertas.setForeground(Color.WHITE);
        btnAlertas.setFont(new Font("Arial", Font.BOLD, 12));
        btnAlertas.setPreferredSize(new Dimension(150, 35));
        btnAlertas.addActionListener(this::gerenciarAlertas);
        ferramentasPanel.add(btnAlertas);
        
        btnLotes = new JButton("📦 Controle de Lotes");
        btnLotes.setBackground(new Color(123, 104, 238));
        btnLotes.setForeground(Color.WHITE);
        btnLotes.setFont(new Font("Arial", Font.BOLD, 12));
        btnLotes.setPreferredSize(new Dimension(150, 35));
        btnLotes.addActionListener(this::controleLotes);
        ferramentasPanel.add(btnLotes);
        
        btnAtualizar = new JButton("🔄 Atualizar");
        btnAtualizar.setBackground(new Color(108, 117, 125));
        btnAtualizar.setForeground(Color.WHITE);
        btnAtualizar.setFont(new Font("Arial", Font.BOLD, 12));
        btnAtualizar.setPreferredSize(new Dimension(100, 35));
        btnAtualizar.addActionListener(e -> {
            carregarDadosExemplo();
            atualizarEstatisticas();
        });
        ferramentasPanel.add(btnAtualizar);
        
        JPanel combinedPanel = new JPanel(new GridLayout(2, 1, 0, 10));
        combinedPanel.setBackground(new Color(250, 250, 250));
        combinedPanel.add(operacoesPanel);
        combinedPanel.add(ferramentasPanel);
        
        bottomPanel.add(combinedPanel, BorderLayout.CENTER);
        
        return bottomPanel;
    }
    
    private void carregarDadosExemplo() {
        // Limpar tabela
        tableModel.setRowCount(0);
        
        // Dados de exemplo para demonstração
        produtos.clear();
        
        // Produto 1 - Estoque Normal
        Produto p1 = new Produto();
        p1.setId(1L);
        p1.setNome("Notebook Dell Inspiron");
        p1.setCategoria("Informática");
        p1.setEstoque(25);
        p1.setEstoqueMinimo(10);
        p1.setEstoqueMaximo(50);
        p1.setLocalizacaoEstoque("A-01-B");
        p1.setLote("LOT2024001");
        p1.setDataValidade(LocalDate.now().plusMonths(6));
        p1.setPrecoVenda(new BigDecimal("3500.00"));
        p1.setMetodoControleEstoque(MetodoControleEstoque.CUSTO_MEDIO);
        produtos.add(p1);
        
        // Produto 2 - Estoque Baixo
        Produto p2 = new Produto();
        p2.setId(2L);
        p2.setNome("Mouse Wireless Logitech");
        p2.setCategoria("Informática");
        p2.setEstoque(5);
        p2.setEstoqueMinimo(15);
        p2.setEstoqueMaximo(100);
        p2.setLocalizacaoEstoque("B-02-A");
        p2.setLote("LOT2024002");
        p2.setDataValidade(LocalDate.now().plusMonths(12));
        p2.setPrecoVenda(new BigDecimal("150.00"));
        p2.setMetodoControleEstoque(MetodoControleEstoque.PEPS);
        produtos.add(p2);
        
        // Produto 3 - Estoque Zerado
        Produto p3 = new Produto();
        p3.setId(3L);
        p3.setNome("Teclado Mecânico RGB");
        p3.setCategoria("Informática");
        p3.setEstoque(0);
        p3.setEstoqueMinimo(20);
        p3.setEstoqueMaximo(80);
        p3.setLocalizacaoEstoque("C-03-C");
        p3.setLote("LOT2024003");
        p3.setDataValidade(LocalDate.now().plusMonths(8));
        p3.setPrecoVenda(new BigDecimal("280.00"));
        p3.setMetodoControleEstoque(MetodoControleEstoque.UEPS);
        produtos.add(p3);
        
        // Produto 4 - Estoque Excedente
        Produto p4 = new Produto();
        p4.setId(4L);
        p4.setNome("Monitor LED 24\"");
        p4.setCategoria("Informática");
        p4.setEstoque(45);
        p4.setEstoqueMinimo(10);
        p4.setEstoqueMaximo(30);
        p4.setLocalizacaoEstoque("D-01-A");
        p4.setLote("LOT2024004");
        p4.setDataValidade(LocalDate.now().plusMonths(18));
        p4.setPrecoVenda(new BigDecimal("890.00"));
        p4.setMetodoControleEstoque(MetodoControleEstoque.CUSTO_MEDIO);
        produtos.add(p4);
        
        // Produto 5 - Próximo ao Vencimento
        Produto p5 = new Produto();
        p5.setId(5L);
        p5.setNome("Cartucho de Tinta HP");
        p5.setCategoria("Informática");
        p5.setEstoque(30);
        p5.setEstoqueMinimo(25);
        p5.setEstoqueMaximo(60);
        p5.setLocalizacaoEstoque("E-02-B");
        p5.setLote("LOT2024005");
        p5.setDataValidade(LocalDate.now().plusDays(15)); // Próximo ao vencimento
        p5.setPrecoVenda(new BigDecimal("120.00"));
        p5.setMetodoControleEstoque(MetodoControleEstoque.PEPS);
        produtos.add(p5);
        
        // Adicionar produtos à tabela
        for (Produto produto : produtos) {
            adicionarProdutoTabela(produto);
        }
    }
    
    private void adicionarProdutoTabela(Produto produto) {
        String status = getStatusEstoque(produto);
        BigDecimal valorTotal = produto.getPrecoVenda().multiply(new BigDecimal(produto.getEstoque()));
        
        Object[] rowData = {
            produto.getId(),
            produto.getNome(),
            produto.getCategoria(),
            produto.getEstoque(),
            produto.getEstoqueMinimo(),
            produto.getEstoqueMaximo(),
            produto.getLocalizacaoEstoque(),
            produto.getMetodoControleEstoque().getSigla(),
            status,
            currencyFormat.format(produto.getPrecoVenda()),
            currencyFormat.format(valorTotal)
        };
        
        tableModel.addRow(rowData);
    }
    
    private String getStatusEstoque(Produto produto) {
        int estoque = produto.getEstoque();
        int minimo = produto.getEstoqueMinimo();
        int maximo = produto.getEstoqueMaximo();
        
        if (estoque == 0) {
            return "🔴 ZERADO";
        } else if (estoque <= minimo) {
            return "🟡 BAIXO";
        } else if (estoque >= maximo) {
            return "🔵 EXCEDENTE";
        } else {
            return "🟢 NORMAL";
        }
    }
    
    private void atualizarEstatisticas() {
        int totalProdutos = produtos.size();
        int alertas = 0;
        int estoqueBaixo = 0;
        BigDecimal valorTotal = BigDecimal.ZERO;
        
        for (Produto produto : produtos) {
            valorTotal = valorTotal.add(produto.getPrecoVenda().multiply(new BigDecimal(produto.getEstoque())));
            
            if (produto.getEstoque() <= produto.getEstoqueMinimo()) {
                alertas++;
            }
            
            if (produto.getEstoque() < produto.getEstoqueMinimo()) {
                estoqueBaixo++;
            }
        }
        
        lblTotalProdutos.setText("📦 Total de Produtos: " + totalProdutos);
        lblValorTotalEstoque.setText("💰 Valor Total: " + currencyFormat.format(valorTotal));
        lblAlertasEstoque.setText("⚠️ Alertas: " + alertas);
        lblEstoqueBaixo.setText("🔴 Estoque Baixo: " + estoqueBaixo);
    }
    
    private void buscarProdutos(ActionEvent e) {
        String textoBusca = txtBusca.getText().toLowerCase().trim();
        String tipoBusca = (String) cbTipoBusca.getSelectedItem();
        
        if (textoBusca.isEmpty()) {
            carregarDadosExemplo();
            return;
        }
        
        tableModel.setRowCount(0);
        
        for (Produto produto : produtos) {
            boolean encontrado = false;
            
            switch (tipoBusca) {
                case "Nome":
                    encontrado = produto.getNome().toLowerCase().contains(textoBusca);
                    break;
                case "Código":
                    encontrado = produto.getId().toString().contains(textoBusca);
                    break;
                case "Categoria":
                    encontrado = produto.getCategoria().toLowerCase().contains(textoBusca);
                    break;
                case "Localização":
                    encontrado = produto.getLocalizacaoEstoque().toLowerCase().contains(textoBusca);
                    break;
                default: // Todos
                    encontrado = produto.getNome().toLowerCase().contains(textoBusca) ||
                              produto.getCategoria().toLowerCase().contains(textoBusca) ||
                              produto.getLocalizacaoEstoque().toLowerCase().contains(textoBusca);
            }
            
            if (encontrado) {
                adicionarProdutoTabela(produto);
            }
        }
    }
    
    private void entradaMercadorias(ActionEvent e) {
        JDialog dialog = new JDialog(frame, "📥 Entrada de Mercadorias", true);
        dialog.setSize(500, 400);
        dialog.setLocationRelativeTo(frame);
        
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        // Formulário
        JPanel formPanel = new JPanel(new GridLayout(6, 2, 10, 10));
        
        formPanel.add(new JLabel("📦 Produto:"));
        txtProduto = new JTextField();
        formPanel.add(txtProduto);
        
        formPanel.add(new JLabel("🔢 Quantidade:"));
        txtQuantidade = new JTextField();
        formPanel.add(txtQuantidade);
        
        formPanel.add(new JLabel("💰 Custo Unitário:"));
        txtCusto = new JTextField();
        formPanel.add(txtCusto);
        
        formPanel.add(new JLabel("📋 Número do Lote:"));
        txtLote = new JTextField();
        formPanel.add(txtLote);
        
        formPanel.add(new JLabel("📝 Observações:"));
        txtObservacoes = new JTextArea(3, 20);
        formPanel.add(new JScrollPane(txtObservacoes));
        
        // Botões
        JPanel buttonPanel = new JPanel(new FlowLayout());
        
        JButton btnConfirmar = new JButton("✅ Confirmar Entrada");
        btnConfirmar.setBackground(new Color(40, 167, 69));
        btnConfirmar.setForeground(Color.WHITE);
        btnConfirmar.addActionListener(event -> {
            try {
                int quantidade = Integer.parseInt(txtQuantidade.getText());
                BigDecimal custo = new BigDecimal(txtCusto.getText());
                
                JOptionPane.showMessageDialog(dialog, 
                    "✅ Entrada registrada com sucesso!\n\n" +
                    "📦 Produto: " + txtProduto.getText() + "\n" +
                    "🔢 Quantidade: " + quantidade + "\n" +
                    "💰 Custo Total: " + currencyFormat.format(custo.multiply(new BigDecimal(quantidade))) + "\n" +
                    "📋 Lote: " + txtLote.getText(),
                    "Entrada Confirmada", JOptionPane.INFORMATION_MESSAGE);
                
                dialog.dispose();
                carregarDadosExemplo();
                atualizarEstatisticas();
                
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(dialog, 
                    "❌ Preencha todos os campos corretamente!", 
                    "Erro de Validação", JOptionPane.ERROR_MESSAGE);
            }
        });
        buttonPanel.add(btnConfirmar);
        
        JButton btnCancelar = new JButton("❌ Cancelar");
        btnCancelar.setBackground(new Color(220, 53, 69));
        btnCancelar.setForeground(Color.WHITE);
        btnCancelar.addActionListener(event -> dialog.dispose());
        buttonPanel.add(btnCancelar);
        
        panel.add(formPanel, BorderLayout.CENTER);
        panel.add(buttonPanel, BorderLayout.SOUTH);
        
        dialog.add(panel);
        dialog.setVisible(true);
    }
    
    private void saidaMercadorias(ActionEvent e) {
        JDialog dialog = new JDialog(frame, "📤 Saída de Mercadorias", true);
        dialog.setSize(500, 350);
        dialog.setLocationRelativeTo(frame);
        
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        // Formulário
        JPanel formPanel = new JPanel(new GridLayout(4, 2, 10, 10));
        
        formPanel.add(new JLabel("📦 Produto:"));
        txtProduto = new JTextField();
        formPanel.add(txtProduto);
        
        formPanel.add(new JLabel("🔢 Quantidade:"));
        txtQuantidade = new JTextField();
        formPanel.add(txtQuantidade);
        
        formPanel.add(new JLabel("📝 Motivo da Saída:"));
        txtObservacoes = new JTextArea(2, 20);
        formPanel.add(new JScrollPane(txtObservacoes));
        
        // Botões
        JPanel buttonPanel = new JPanel(new FlowLayout());
        
        JButton btnConfirmar = new JButton("✅ Confirmar Saída");
        btnConfirmar.setBackground(new Color(220, 53, 69));
        btnConfirmar.setForeground(Color.WHITE);
        btnConfirmar.addActionListener(event -> {
            try {
                int quantidade = Integer.parseInt(txtQuantidade.getText());
                
                JOptionPane.showMessageDialog(dialog, 
                    "✅ Saída registrada com sucesso!\n\n" +
                    "📦 Produto: " + txtProduto.getText() + "\n" +
                    "🔢 Quantidade: " + quantidade + "\n" +
                    "📝 Motivo: " + txtObservacoes.getText(),
                    "Saída Confirmada", JOptionPane.INFORMATION_MESSAGE);
                
                dialog.dispose();
                carregarDadosExemplo();
                atualizarEstatisticas();
                
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(dialog, 
                    "❌ Preencha todos os campos corretamente!", 
                    "Erro de Validação", JOptionPane.ERROR_MESSAGE);
            }
        });
        buttonPanel.add(btnConfirmar);
        
        JButton btnCancelar = new JButton("❌ Cancelar");
        btnCancelar.setBackground(new Color(108, 117, 125));
        btnCancelar.setForeground(Color.WHITE);
        btnCancelar.addActionListener(event -> dialog.dispose());
        buttonPanel.add(btnCancelar);
        
        panel.add(formPanel, BorderLayout.CENTER);
        panel.add(buttonPanel, BorderLayout.SOUTH);
        
        dialog.add(panel);
        dialog.setVisible(true);
    }
    
    private void ajusteManual(ActionEvent e) {
        JOptionPane.showMessageDialog(frame, 
            "⚙️ Funcionalidade de Ajuste Manual em desenvolvimento!\n\n" +
            "Permitirá ajustes manuais de estoque com:\n" +
            "• Justificativa obrigatória\n" +
            "• Aprovação de supervisor\n" +
            "• Registro de auditoria\n" +
            "• Relatório de ajustes",
            "Ajuste Manual", JOptionPane.INFORMATION_MESSAGE);
    }
    
    private void gerarRelatorioValorizacao(ActionEvent e) {
        StringBuilder relatorio = new StringBuilder();
        relatorio.append("📈 RELATÓRIO DE VALORIZAÇÃO DE ESTOQUE\n");
        relatorio.append("=" .repeat(50) + "\n\n");
        
        BigDecimal valorTotal = BigDecimal.ZERO;
        int quantidadeTotal = 0;
        
        relatorio.append("📋 DETALHAMENTO POR PRODUTO:\n\n");
        
        for (Produto produto : produtos) {
            BigDecimal valorProduto = produto.getPrecoVenda().multiply(new BigDecimal(produto.getEstoque()));
            valorTotal = valorTotal.add(valorProduto);
            quantidadeTotal += produto.getEstoque();
            
            relatorio.append(String.format("• %s\n", produto.getNome()));
            relatorio.append(String.format("  📦 Quantidade: %d\n", produto.getEstoque()));
            relatorio.append(String.format("  💰 Valor Unitário: %s\n", currencyFormat.format(produto.getPrecoVenda())));
            relatorio.append(String.format("  💵 Valor Total: %s\n", currencyFormat.format(valorProduto)));
            relatorio.append(String.format("  📍 Localização: %s\n", produto.getLocalizacaoEstoque()));
            relatorio.append(String.format("  🔄 Método: %s\n\n", produto.getMetodoControleEstoque().getDescricao()));
        }
        
        relatorio.append("=" .repeat(50) + "\n");
        relatorio.append("📊 RESUMO GERAL:\n\n");
        relatorio.append(String.format("📦 Total de Itens: %d\n", quantidadeTotal));
        relatorio.append(String.format("💰 Valor Total do Estoque: %s\n", currencyFormat.format(valorTotal)));
        relatorio.append(String.format("📅 Data do Relatório: %s\n", LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))));
        
        JTextArea textArea = new JTextArea(relatorio.toString());
        textArea.setEditable(false);
        textArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
        
        JScrollPane scrollPane = new JScrollPane(textArea);
        scrollPane.setPreferredSize(new Dimension(600, 400));
        
        JOptionPane.showMessageDialog(frame, scrollPane, 
            "📈 Relatório de Valorização", JOptionPane.INFORMATION_MESSAGE);
    }
    
    private void gerenciarAlertas(ActionEvent e) {
        StringBuilder alertas = new StringBuilder();
        alertas.append("🔔 GERENCIAMENTO DE ALERTAS DE ESTOQUE\n");
        alertas.append("=" .repeat(50) + "\n\n");
        
        int totalAlertas = 0;
        
        for (Produto produto : produtos) {
            if (produto.getEstoque() <= produto.getEstoqueMinimo()) {
                totalAlertas++;
                alertas.append(String.format("⚠️ %s\n", produto.getNome()));
                alertas.append(String.format("   📦 Estoque Atual: %d\n", produto.getEstoque()));
                alertas.append(String.format("   📉 Estoque Mínimo: %d\n", produto.getEstoqueMinimo()));
                
                if (produto.getEstoque() == 0) {
                    alertas.append("   🔴 STATUS: ESTOQUE ZERADO - URGENTE!\n");
                } else {
                    alertas.append("   🟡 STATUS: ESTOQUE BAIXO - ATENÇÃO!\n");
                }
                
                alertas.append(String.format("   📍 Localização: %s\n", produto.getLocalizacaoEstoque()));
                alertas.append("\n");
            }
            
            // Verificar validade
            if (produto.getDataValidade() != null) {
                long diasParaVencimento = java.time.temporal.ChronoUnit.DAYS.between(LocalDate.now(), produto.getDataValidade());
                if (diasParaVencimento <= 30) {
                    totalAlertas++;
                    alertas.append(String.format("⏰ %s - PRÓXIMO AO VENCIMENTO\n", produto.getNome()));
                    alertas.append(String.format("   📅 Data de Validade: %s\n", 
                        produto.getDataValidade().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))));
                    alertas.append(String.format("   ⏳ Dias para vencer: %d\n", diasParaVencimento));
                    alertas.append("\n");
                }
            }
        }
        
        if (totalAlertas == 0) {
            alertas.append("✅ Nenhum alerta ativo no momento!\n");
            alertas.append("🎉 Todos os produtos estão com estoque adequado.");
        } else {
            alertas.append(String.format("📊 Total de Alertas Ativos: %d\n", totalAlertas));
        }
        
        JTextArea textArea = new JTextArea(alertas.toString());
        textArea.setEditable(false);
        textArea.setFont(new Font("Arial", Font.PLAIN, 12));
        
        JScrollPane scrollPane = new JScrollPane(textArea);
        scrollPane.setPreferredSize(new Dimension(500, 350));
        
        JOptionPane.showMessageDialog(frame, scrollPane, 
            "🔔 Gerenciamento de Alertas", JOptionPane.INFORMATION_MESSAGE);
    }
    
    private void controleLotes(ActionEvent e) {
        StringBuilder lotes = new StringBuilder();
        lotes.append("📦 CONTROLE DE LOTES\n");
        lotes.append("=" .repeat(50) + "\n\n");
        
        for (Produto produto : produtos) {
            lotes.append(String.format("📦 %s\n", produto.getNome()));
            lotes.append(String.format("   📋 Lote: %s\n", produto.getLote()));
            lotes.append(String.format("   📅 Data de Validade: %s\n", 
                produto.getDataValidade() != null ? 
                produto.getDataValidade().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")) : "N/A"));
            lotes.append(String.format("   🔄 Método Controle: %s\n", produto.getMetodoControleEstoque().getDescricao()));
            lotes.append(String.format("   📍 Localização: %s\n", produto.getLocalizacaoEstoque()));
            lotes.append(String.format("   📦 Quantidade: %d\n\n", produto.getEstoque()));
        }
        
        JTextArea textArea = new JTextArea(lotes.toString());
        textArea.setEditable(false);
        textArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
        
        JScrollPane scrollPane = new JScrollPane(textArea);
        scrollPane.setPreferredSize(new Dimension(600, 400));
        
        JOptionPane.showMessageDialog(frame, scrollPane, 
            "📦 Controle de Lotes", JOptionPane.INFORMATION_MESSAGE);
    }
    
    // Custom cell renderer para status
    private static class StatusCellRenderer extends DefaultTableCellRenderer {
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value,
                boolean isSelected, boolean hasFocus, int row, int column) {
            
            Component component = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
            
            if (component instanceof JLabel) {
                JLabel label = (JLabel) component;
                label.setHorizontalAlignment(SwingConstants.CENTER);
                label.setFont(new Font("Arial", Font.BOLD, 11));
                
                String status = (String) value;
                if (status.contains("ZERADO")) {
                    label.setForeground(Color.RED);
                    label.setBackground(new Color(255, 200, 200));
                } else if (status.contains("BAIXO")) {
                    label.setForeground(Color.ORANGE);
                    label.setBackground(new Color(255, 230, 200));
                } else if (status.contains("EXCEDENTE")) {
                    label.setForeground(Color.BLUE);
                    label.setBackground(new Color(200, 220, 255));
                } else {
                    label.setForeground(new Color(0, 128, 0));
                    label.setBackground(new Color(200, 255, 200));
                }
                
                if (isSelected) {
                    label.setForeground(Color.WHITE);
                }
            }
            
            return component;
        }
    }
    
    // Custom cell renderer para valores monetários
    private static class CurrencyCellRenderer extends DefaultTableCellRenderer {
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value,
                boolean isSelected, boolean hasFocus, int row, int column) {
            
            Component component = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
            
            if (component instanceof JLabel) {
                JLabel label = (JLabel) component;
                label.setHorizontalAlignment(SwingConstants.RIGHT);
                label.setFont(new Font("Arial", Font.BOLD, 12));
                
                if (isSelected) {
                    label.setForeground(Color.WHITE);
                } else {
                    label.setForeground(new Color(0, 100, 0));
                }
            }
            
            return component;
        }
    }
}
