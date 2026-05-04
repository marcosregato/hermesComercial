package com.br.hermescomercial.pdv.controller;

import com.br.hermescomercial.ui.layout.LayoutPadrao;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import com.br.hermescomercial.business.impressao.ImpressaoNaoFiscalService;
import com.br.hermescomercial.service.EstoqueService;
import com.br.hermescomercial.service.VendaService;

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
    private ImpressaoNaoFiscalService impressaoService;
    private EstoqueService estoqueService;
    private VendaService vendaService;  
    
    public PDVVendaSwingController() {
        this.itens = new ArrayList<>();
        this.impressaoService = new ImpressaoNaoFiscalService();
        this.estoqueService = new EstoqueService();
        this.vendaService = new VendaService();
        initializeUI();
    }
    
    private void initializeUI() {
        // Usar LayoutPadrao - tema consistente
        
        frame = new JFrame("PDV - Nova Venda v2.8.3 - LayoutPadrao");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setSize(900, 600);
        frame.setLocationRelativeTo(null);
        
        // Configurar fundo com LayoutPadrao
        frame.getContentPane().setBackground(LayoutPadrao.COR_FUNDO_ESCURO);
        
        frame.add(createMainPanel());
    }
    
    private JPanel createMainPanel() {
        JPanel mainPanel = LayoutPadrao.criarPainelComMargem(20);
        mainPanel.setBackground(LayoutPadrao.COR_FUNDO_ESCURO);
        
        // Header com LayoutPadrao
        mainPanel.add(createHeaderPanel(), BorderLayout.NORTH);
        
        // Painel central com layout otimizado
        JPanel centerPanel = new JPanel(new BorderLayout());
        centerPanel.setBorder(BorderFactory.createEmptyBorder(15, 0, 15, 0));
        centerPanel.setOpaque(false);
        
        // Painel de produtos com design moderno
        JPanel produtosPanel = createEnhancedProdutosPanel();
        centerPanel.add(produtosPanel, BorderLayout.CENTER);
        
        // Painel de entrada com design melhorado
        JPanel inputPanel = createEnhancedInputPanel();
        centerPanel.add(inputPanel, BorderLayout.SOUTH);
        
        mainPanel.add(centerPanel, BorderLayout.CENTER);
        
        // Painel lateral com resumo e ações
        JPanel sidePanel = createEnhancedSidePanel();
        mainPanel.add(sidePanel, BorderLayout.EAST);
        
        return mainPanel;
    }
    
    private JPanel createHeaderPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(LayoutPadrao.COR_PRIMARIA);
        panel.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));
        
        // Botão voltar com LayoutPadrao
        JPanel leftPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        leftPanel.setOpaque(false);
        
        JButton btnVoltar = LayoutPadrao.criarBotaoSecundario("⬅️ Voltar");
        btnVoltar.addActionListener(e -> frame.dispose());
        
        leftPanel.add(btnVoltar);
        
        // Painel central com título e subtítulo
        JPanel centerPanel = new JPanel(new BorderLayout());
        centerPanel.setOpaque(false);
        
        JLabel titleLabel = LayoutPadrao.criarRotuloTitulo("🛒 Nova Venda");
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        
        JLabel subtitleLabel = LayoutPadrao.criarRotuloTexto("Sistema de Ponto de Venda");
        subtitleLabel.setForeground(new Color(200, 220, 240));
        subtitleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        
        centerPanel.add(titleLabel, BorderLayout.NORTH);
        centerPanel.add(subtitleLabel, BorderLayout.CENTER);
        
        // Painel direito com data e hora
        JPanel rightPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        rightPanel.setOpaque(false);
        
        JLabel lblDataHora = LayoutPadrao.criarRotuloTexto("🕒 " + 
            java.time.LocalDateTime.now().format(
            java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")));
        lblDataHora.setForeground(Color.WHITE);
        
        rightPanel.add(lblDataHora);
        
        panel.add(leftPanel, BorderLayout.WEST);
        panel.add(centerPanel, BorderLayout.CENTER);
        panel.add(rightPanel, BorderLayout.EAST);
        
        return panel;
    }
    
    private JPanel createEnhancedProdutosPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createTitledBorder("📦 Itens da Venda"),
            BorderFactory.createEmptyBorder(15, 15, 15, 15)
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
        
        produtosTable = LayoutPadrao.criarTabela();
        produtosTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        produtosTable.getTableHeader().setReorderingAllowed(false);
        produtosTable.setRowHeight(28);
        produtosTable.setDefaultEditor(Object.class, null);
        produtosTable.setEnabled(false);
        
        // Configurar larguras das colunas
        produtosTable.getColumnModel().getColumn(0).setPreferredWidth(80);
        produtosTable.getColumnModel().getColumn(1).setPreferredWidth(350);
        produtosTable.getColumnModel().getColumn(2).setPreferredWidth(60);
        produtosTable.getColumnModel().getColumn(3).setPreferredWidth(100);
        produtosTable.getColumnModel().getColumn(4).setPreferredWidth(100);
        
        // Scroll pane com LayoutPadrao
        JScrollPane scrollPane = LayoutPadrao.criarBarraRolagem(produtosTable);
        scrollPane.setPreferredSize(new Dimension(0, 250));
        panel.add(scrollPane, BorderLayout.CENTER);
        
        // Painel de botões com LayoutPadrao
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 10));
        buttonPanel.setBackground(LayoutPadrao.COR_FUNDO_ESCURO);
        
        JButton btnRemover = LayoutPadrao.criarBotaoPerigo("🗑️ Remover Item");
        JButton btnLimpar = LayoutPadrao.criarBotaoAlerta("🔄 Limpar Venda");
        
        btnRemover.addActionListener(this::removerItem);
        btnLimpar.addActionListener(this::limparVenda);
        
        buttonPanel.add(btnRemover);
        buttonPanel.add(btnLimpar);
        
        panel.add(buttonPanel, BorderLayout.SOUTH);
        
        return panel;
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
        gbc.gridx = 0; gbc.gridy = 0; gbc.weightx = 0.3;
        inputPanel.add(LayoutPadrao.criarRotuloCampo("Código:"), gbc);
        
        gbc.gridx = 1; gbc.weightx = 0.7;
        txtCodigo = LayoutPadrao.criarCampoTexto(15);
        inputPanel.add(txtCodigo, gbc);
        
        gbc.gridx = 2; gbc.weightx = 0.3;
        inputPanel.add(LayoutPadrao.criarRotuloCampo("Quantidade:"), gbc);
        
        gbc.gridx = 3; gbc.weightx = 0.7;
        txtQuantidade = LayoutPadrao.criarCampoTexto(10);
        txtQuantidade.setText("1");
        inputPanel.add(txtQuantidade, gbc);
        
        // Linha 2: Descrição (readonly)
        gbc.gridx = 0; gbc.gridy = 1; gbc.weightx = 0.3;
        inputPanel.add(LayoutPadrao.criarRotuloCampo("Descrição:"), gbc);
        
        gbc.gridx = 1; gbc.gridwidth = 3; gbc.weightx = 2.0;
        txtDescricao = LayoutPadrao.criarCampoTexto(40);
        txtDescricao.setEditable(false);
        txtDescricao.setBackground(LayoutPadrao.COR_FUNDO_ESCURO);
        inputPanel.add(txtDescricao, gbc);
        
        // Painel de botões de ação
        JPanel actionPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 10));
        actionPanel.setBackground(LayoutPadrao.COR_FUNDO_ESCURO);
        
        JButton btnAdicionar = LayoutPadrao.criarBotaoSucesso("➕ Adicionar");
        JButton btnBuscar = LayoutPadrao.criarBotaoPrimario("🔍 Buscar");
        
        btnAdicionar.addActionListener(this::adicionarProduto);
        btnBuscar.addActionListener(e -> buscarProdutoPorCodigo(txtCodigo.getText().trim()));
        
        actionPanel.add(btnBuscar);
        actionPanel.add(btnAdicionar);
        
        panel.add(inputPanel, BorderLayout.CENTER);
        panel.add(actionPanel, BorderLayout.SOUTH);
        
        return panel;
    }
    
    private JPanel createEnhancedSidePanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(0, 20, 0, 0));
        panel.setOpaque(false);
        panel.setPreferredSize(new Dimension(300, 0));
        
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
    
    private void adicionarProduto(ActionEvent e) {
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
    
    private void removerItem(ActionEvent e) {
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
    
    private void limparVenda(ActionEvent e) {
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
    
    private void finalizarVenda(ActionEvent e) {
        if (itens.isEmpty()) {
            JOptionPane.showMessageDialog(frame, "Adicione produtos à venda!", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        BigDecimal total = calcularTotal();
        
        // VALIDAÇÕES DE NEGÓCIO ANTES DE FINALIZAR
        Map<String, VendaService.ResultadoValidacao> validacoes = vendaService.validarVendaCompleta(
            total, 
            BigDecimal.ZERO, // Sem desconto por enquanto
            VendaService.TipoDesconto.VALOR_FIXO,
            itens.size(),
            java.time.LocalDateTime.now()
        );
        
        // Verificar se há validações reprovadas
        StringBuilder errosValidacao = new StringBuilder();
        boolean precisaAutorizacao = false;
        
        for (Map.Entry<String, VendaService.ResultadoValidacao> entry : validacoes.entrySet()) {
            if (entry.getValue() != VendaService.ResultadoValidacao.OK) {
                errosValidacao.append("• ").append(vendaService.getMensagemErro(entry.getValue())).append("\n\n");
                if (vendaService.precisaAutorizacaoGerente(entry.getValue())) {
                    precisaAutorizacao = true;
                }
            }
        }
        
        if (errosValidacao.length() > 0) {
            String titulo = precisaAutorizacao ? "⚠️ REGRAS DE NEGÓCIO - AUTORIZAÇÃO NECESSÁRIA" : "🚫 REGRAS DE NEGÓCIO - VENDA BLOQUEADA";
            
            if (precisaAutorizacao) {
                errosValidacao.insert(0, "📋 VALIDAÇÕES ENCONTRADAS:\n\n");
                errosValidacao.append("━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━\n\n");
                errosValidacao.append("👤 CONTATE O GERENTE PARA AUTORIZAÇÃO\n");
                
                JOptionPane.showMessageDialog(frame, errosValidacao.toString(), titulo, JOptionPane.WARNING_MESSAGE);
                return;
            } else {
                JOptionPane.showMessageDialog(frame, errosValidacao.toString(), titulo, JOptionPane.ERROR_MESSAGE);
                return;
            }
        }
        
        // Adicionar informações de risco na confirmação
        String nivelRisco = vendaService.getNivelRisco(total, itens.size());
        String mensagemConfirmacao = String.format(
            "Confirmar finalização da venda?\n\n" +
            "📊 Total: R$ %.2f\n" +
            "🛒 Itens: %d\n" +
            "⚠️ Nível de Risco: %s\n\n" +
            "━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━\n" +
            "✅ Todas as validações aprovadas",
            total, itens.size(), nivelRisco
        );
        
        int confirm = JOptionPane.showConfirmDialog(frame, 
            mensagemConfirmacao, 
            "Confirmar Venda", 
            JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
            
        if (confirm == 0) {
            // PASSO 4: SUBTRAIR ESTOQUE AUTOMATICAMENTE AO FINALIZAR VENDA
            StringBuilder estoqueAtualizado = new StringBuilder();
            
            for (ItemVenda item : itens) {
                Produto produto = item.getProduto();
                int quantidadeVendida = item.getQuantidade();
                int estoqueAnterior = produto.getEstoque();
                int novoEstoque = estoqueAnterior - quantidadeVendida;
                
                // Atualizar estoque do produto
                produto.setEstoque(novoEstoque);
                
                // Registrar atualização para exibir ao usuário
                estoqueAtualizado.append(String.format(
                    "• %s: %d → %d (vendido: %d)\n",
                    produto.getDescricao(),
                    estoqueAnterior,
                    novoEstoque,
                    quantidadeVendida
                ));
            }
            
            // EMITIR CUPOM NÃO FISCAL AUTOMATICAMENTE
            try {
                // Criar VendaPDV para impressão
                com.br.hermescomercial.pdv.model.VendaPDV vendaPDV = new com.br.hermescomercial.pdv.model.VendaPDV();
                vendaPDV.setNumeroCupom("CUP" + System.currentTimeMillis());
                vendaPDV.setValorTotal(total);
                vendaPDV.setStatus("CONCLUIDA");
                
                // Imprimir cupom não fiscal
                boolean impressaoSucesso = impressaoService.imprimirCupomVenda(vendaPDV);
                
                JOptionPane.showMessageDialog(frame, 
                    "🏪 VENDA FINALIZADA COM SUCESSO!\n\n" +
                    "💰 Total: R$ " + String.format("%.2f", total) + "\n" +
                    "📦 Itens: " + itens.size() + "\n" +
                    "📉 ESTOQUE ATUALIZADO:\n" + estoqueAtualizado.toString() + "\n" +
                    "🧾 Cupom não fiscal impresso: " + (impressaoSucesso ? "✅" : "⚠️") + "\n" +
                    "✅ Versão SWING 2.4.0 - Gestão de Estoque Completa",
                    "Venda Concluída", JOptionPane.INFORMATION_MESSAGE);
                
            } catch (Exception ex) {
                // Se falhar a impressão, ainda finaliza a venda
                JOptionPane.showMessageDialog(frame, 
                    "🏪 VENDA FINALIZADA COM SUCESSO!\n\n" +
                    "💰 Total: R$ " + String.format("%.2f", total) + "\n" +
                    "📦 Itens: " + itens.size() + "\n" +
                    "📉 ESTOQUE ATUALIZADO:\n" + estoqueAtualizado.toString() + "\n" +
                    "⚠️ Cupom não fiscal: Falha na impressão\n" +
                    "✅ Versão SWING 2.4.0 - Gestão de Estoque Completa",
                    "Venda Concluída", JOptionPane.INFORMATION_MESSAGE);
            }
            
            // Limpar para nova venda
            limparVenda(null);
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
            this.estoqueMaximo = 100;
            this.localizacaoEstoque = "A-01-01";
            this.lote = "L001";
            this.dataValidade = "31/12/2025";
        }
        
        public String getCodigo() { return codigo; }
        public String getDescricao() { return descricao; }
        public BigDecimal getPreco() { return preco; }
        public int getEstoque() { return estoque; }
        public void setEstoque(int estoque) { this.estoque = estoque; }
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
