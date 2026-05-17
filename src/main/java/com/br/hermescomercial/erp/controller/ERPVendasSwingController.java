package com.br.hermescomercial.erp.controller;

import com.br.hermescomercial.ui.layout.LayoutPadrao;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * Controller para tela de gestão de vendas do ERP
 * Versão 2.3.0 - Arquitetura Modular - Tema Padrão Hermes
 */
public class ERPVendasSwingController {
    
    private JFrame frame;
    
    public JFrame getFrame() {
        return frame;
    }
    private JPanel mainPanel;
    private JTable vendasTable;
    private DefaultTableModel tableModel;
    private JTextField txtCliente, txtProduto, txtValor, txtQuantidade;
    private JComboBox<String> cbStatus, cbVendedor, cbFormaPagamento;
    private JFormattedTextField txtDataVenda;
    private JLabel lblTotalVendas, lblMediaVendas, lblVendasMes;
    private DecimalFormat currencyFormat;
    
    public ERPVendasSwingController() {
        inicializarUI();
    }
    
    private void inicializarUI() {
        frame = new JFrame("💰 Gestão de Vendas - ERP");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setSize(1400, 900);
        frame.setLocationRelativeTo(null);
        
        // Configurar formato de moeda e data
        currencyFormat = new DecimalFormat("R$ #,##0.00");
        
        // Aplicar tema padrão
        frame.getContentPane().setBackground(LayoutPadrao.COR_FUNDO_ESCURO);
        
        // Criar painéis de formulário e tabela
        JPanel formularioPanel = createFormPanel();
        JPanel tabelaPanel = createTablePanel();
        
        // Usando layout padrão Header → Busca → Formulário → Tabela
        mainPanel = LayoutPadrao.criarLayoutPadraoGestao(
            false, // isPDV (false para ERP)
            "💰 Gestão de Vendas - ERP",
            "Digite nome do cliente ou produto...",
            formularioPanel,
            tabelaPanel
        );
        
        frame.add(mainPanel);
        
        carregarDadosExemplo();
        frame.setVisible(true);
    }
    
    private JPanel createFormPanel() {
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setOpaque(false);
        formPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.WEST;
        
        // Cliente
        gbc.gridx = 0; gbc.gridy = 0; gbc.weightx = 0.0;
        formPanel.add(createLabel("Cliente:"), gbc);
        
        gbc.gridx = 1; gbc.weightx = 1.0;
        txtCliente = createModernTextField(30);
        formPanel.add(txtCliente, gbc);
        
        // Produto
        gbc.gridx = 2; gbc.weightx = 0.0;
        formPanel.add(createLabel("Produto:"), gbc);
        
        gbc.gridx = 3; gbc.weightx = 1.0;
        txtProduto = createModernTextField(30);
        formPanel.add(txtProduto, gbc);
        
        // Data Venda
        gbc.gridx = 0; gbc.gridy = 1; gbc.weightx = 0.0;
        formPanel.add(createLabel("Data Venda:"), gbc);
        
        gbc.gridx = 1; gbc.weightx = 0.5;
        txtDataVenda = createFormattedTextField();
        txtDataVenda.setText(LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
        formPanel.add(txtDataVenda, gbc);
        
        // Vendedor
        gbc.gridx = 2; gbc.weightx = 0.0;
        formPanel.add(createLabel("Vendedor:"), gbc);
        
        gbc.gridx = 3; gbc.weightx = 0.5;
        cbVendedor = new JComboBox<>(new String[]{"João Silva", "Maria Santos", "Pedro Costa", "Ana Oliveira"});
        cbVendedor.setFont(LayoutPadrao.FONTE_CAMPO);
        formPanel.add(cbVendedor, gbc);
        
        // Valor
        gbc.gridx = 0; gbc.gridy = 2; gbc.weightx = 0.0;
        formPanel.add(createLabel("Valor (R$):"), gbc);
        
        gbc.gridx = 1; gbc.weightx = 0.5;
        txtValor = createModernTextField(15);
        formPanel.add(txtValor, gbc);
        
        // Quantidade
        gbc.gridx = 2; gbc.weightx = 0.0;
        formPanel.add(createLabel("Quantidade:"), gbc);
        
        gbc.gridx = 3; gbc.weightx = 0.5;
        txtQuantidade = createModernTextField(10);
        txtQuantidade.setText("1");
        formPanel.add(txtQuantidade, gbc);
        
        // Forma Pagamento
        gbc.gridx = 0; gbc.gridy = 3; gbc.weightx = 0.0;
        formPanel.add(createLabel("Forma Pagamento:"), gbc);
        
        gbc.gridx = 1; gbc.weightx = 0.5;
        cbFormaPagamento = new JComboBox<>(new String[]{"Dinheiro", "Cartão Crédito", "Cartão Débito", "PIX", "Boleto", "Transferência"});
        cbFormaPagamento.setFont(LayoutPadrao.FONTE_CAMPO);
        formPanel.add(cbFormaPagamento, gbc);
        
        // Status
        gbc.gridx = 2; gbc.weightx = 0.0;
        formPanel.add(createLabel("Status:"), gbc);
        
        gbc.gridx = 3; gbc.weightx = 0.5;
        cbStatus = new JComboBox<>(new String[]{"Concluída", "Pendente", "Cancelada", "Em Processamento"});
        cbStatus.setFont(LayoutPadrao.FONTE_CAMPO);
        formPanel.add(cbStatus, gbc);
        
        // Botões
        gbc.gridx = 0; gbc.gridy = 4; gbc.gridwidth = 4;
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.setOpaque(false);
        
        JButton btnSalvar = LayoutPadrao.criarBotaoSucesso("💾 Salvar");
        btnSalvar.addActionListener(e -> salvarVenda());
        buttonPanel.add(btnSalvar);
        
        JButton btnEditar = LayoutPadrao.criarBotaoPrimario("✏️ Editar");
        btnEditar.addActionListener(e -> editarVenda());
        buttonPanel.add(btnEditar);
        
        JButton btnExcluir = LayoutPadrao.criarBotaoPerigo("🗑️ Excluir");
        btnExcluir.addActionListener(e -> excluirVenda());
        buttonPanel.add(btnExcluir);
        
        JButton btnCancelar = LayoutPadrao.criarBotaoSecundario("❌ Cancelar");
        btnCancelar.addActionListener(e -> limparFormulario());
        buttonPanel.add(btnCancelar);
        
        JButton btnAtualizar = LayoutPadrao.criarBotaoSecundario("🔄 Atualizar");
        btnAtualizar.addActionListener(e -> carregarDadosExemplo());
        buttonPanel.add(btnAtualizar);
        
        JButton btnRelatorio = LayoutPadrao.criarBotaoSecundario("📊 Relatório");
        btnRelatorio.addActionListener(e -> gerarRelatorio());
        buttonPanel.add(btnRelatorio);
        
        formPanel.add(buttonPanel, gbc);
        
        return formPanel;
    }
    
    private JPanel createTablePanel() {
        JPanel tablePanel = new JPanel(new BorderLayout());
        tablePanel.setOpaque(false);
        
        // Painel de estatísticas
        JPanel statsPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        statsPanel.setOpaque(false);
        statsPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        lblTotalVendas = new JLabel("💰 Total Vendas: R$ 0,00");
        lblTotalVendas.setFont(LayoutPadrao.FONTE_TITULO);
        lblTotalVendas.setForeground(new Color(40, 167, 69));
        statsPanel.add(lblTotalVendas);
        
        lblMediaVendas = new JLabel("📊 Média: R$ 0,00");
        lblMediaVendas.setFont(LayoutPadrao.FONTE_SUBTITULO);
        lblMediaVendas.setForeground(LayoutPadrao.COR_PRIMARIA);
        statsPanel.add(lblMediaVendas);
        
        lblVendasMes = new JLabel("📅 Vendas Mês: 0");
        lblVendasMes.setFont(LayoutPadrao.FONTE_SUBTITULO);
        lblVendasMes.setForeground(new Color(52, 152, 219));
        statsPanel.add(lblVendasMes);
        
        tablePanel.add(statsPanel, BorderLayout.NORTH);
        
        // Configurar tabela
        String[] colunas = {"ID", "Data", "Cliente", "Produto", "Valor", "Qtd", "Total", "Vendedor", "Forma Pagamento", "Status"};
        
        tableModel = new DefaultTableModel(colunas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        vendasTable = new JTable(tableModel);
        vendasTable.setFont(LayoutPadrao.FONTE_TEXTO);
        vendasTable.getTableHeader().setFont(LayoutPadrao.FONTE_SUBTITULO);
        vendasTable.setRowHeight(25);
        vendasTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        
        // Ajustar largura das colunas
        vendasTable.getColumnModel().getColumn(0).setPreferredWidth(40);  // ID
        vendasTable.getColumnModel().getColumn(1).setPreferredWidth(80);  // Data
        vendasTable.getColumnModel().getColumn(2).setPreferredWidth(150); // Cliente
        vendasTable.getColumnModel().getColumn(3).setPreferredWidth(150); // Produto
        vendasTable.getColumnModel().getColumn(4).setPreferredWidth(80);  // Valor
        vendasTable.getColumnModel().getColumn(5).setPreferredWidth(50);  // Qtd
        vendasTable.getColumnModel().getColumn(6).setPreferredWidth(80);  // Total
        vendasTable.getColumnModel().getColumn(7).setPreferredWidth(100); // Vendedor
        vendasTable.getColumnModel().getColumn(8).setPreferredWidth(100); // Forma Pagamento
        vendasTable.getColumnModel().getColumn(9).setPreferredWidth(100); // Status
        
        JScrollPane scrollPane = new JScrollPane(vendasTable);
        scrollPane.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        
        tablePanel.add(scrollPane, BorderLayout.CENTER);
        
        return tablePanel;
    }
    
    private JLabel createLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(LayoutPadrao.FONTE_ROTULO);
        label.setForeground(LayoutPadrao.COR_TEXTO);
        return label;
    }
    
    private JTextField createModernTextField(int columns) {
        JTextField field = new JTextField(columns);
        field.setFont(LayoutPadrao.FONTE_CAMPO);
        field.setForeground(LayoutPadrao.COR_TEXTO);
        field.setBackground(Color.WHITE);
        field.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(LayoutPadrao.COR_BORDA, 1),
            BorderFactory.createEmptyBorder(5, 5, 5, 5)
        ));
        return field;
    }
    
    private JFormattedTextField createFormattedTextField() {
        JFormattedTextField field = new JFormattedTextField();
        field.setFont(LayoutPadrao.FONTE_CAMPO);
        field.setForeground(LayoutPadrao.COR_TEXTO);
        field.setBackground(Color.WHITE);
        field.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(LayoutPadrao.COR_BORDA, 1),
            BorderFactory.createEmptyBorder(5, 5, 5, 5)
        ));
        return field;
    }
    
    private void salvarVenda() {
        String cliente = txtCliente.getText().trim();
        String produto = txtProduto.getText().trim();
        String valorStr = txtValor.getText().trim();
        String quantidadeStr = txtQuantidade.getText().trim();
        
        // Validação
        if (cliente.isEmpty()) {
            JOptionPane.showMessageDialog(frame, 
                "⚠️ Cliente é obrigatório!", 
                "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        if (produto.isEmpty()) {
            JOptionPane.showMessageDialog(frame, 
                "⚠️ Produto é obrigatório!", 
                "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        if (valorStr.isEmpty()) {
            JOptionPane.showMessageDialog(frame, 
                "⚠️ Valor é obrigatório!", 
                "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        try {
            BigDecimal valor = new BigDecimal(valorStr.replace("R$", "").replace(",", "."));
            int quantidade = Integer.parseInt(quantidadeStr);
            BigDecimal total = valor.multiply(new BigDecimal(quantidade));
            
            Long novoId = System.currentTimeMillis();
            Object[] novaLinha = {
                novoId, txtDataVenda.getText(), cliente, produto, currencyFormat.format(valor), 
                quantidade, currencyFormat.format(total), cbVendedor.getSelectedItem(), 
                cbFormaPagamento.getSelectedItem(), cbStatus.getSelectedItem()
            };
            tableModel.addRow(novaLinha);
            
            JOptionPane.showMessageDialog(frame, 
                "✅ Venda registrada com sucesso!\n" +
                "Cliente: " + cliente + "\n" +
                "Produto: " + produto + "\n" +
                "Valor: " + currencyFormat.format(total), 
                "Sucesso", JOptionPane.INFORMATION_MESSAGE);
            
            limparFormulario();
            atualizarEstatisticas();
            
        } catch (Exception e) {
            JOptionPane.showMessageDialog(frame, 
                "❌ Erro ao salvar venda: " + e.getMessage(), 
                "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void editarVenda() {
        int linhaSelecionada = vendasTable.getSelectedRow();
        
        if (linhaSelecionada == -1) {
            JOptionPane.showMessageDialog(frame, 
                "⚠️ Nenhuma venda selecionada!\n" +
                "Selecione uma venda na tabela para editar.", 
                "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        // Preencher formulário com dados selecionados
        txtCliente.setText((String) tableModel.getValueAt(linhaSelecionada, 2));
        txtProduto.setText((String) tableModel.getValueAt(linhaSelecionada, 3));
        txtValor.setText(tableModel.getValueAt(linhaSelecionada, 4).toString());
        txtQuantidade.setText(tableModel.getValueAt(linhaSelecionada, 5).toString());
        cbVendedor.setSelectedItem(tableModel.getValueAt(linhaSelecionada, 7));
        cbFormaPagamento.setSelectedItem(tableModel.getValueAt(linhaSelecionada, 8));
        cbStatus.setSelectedItem(tableModel.getValueAt(linhaSelecionada, 9));
        
        txtCliente.requestFocus();
    }
    
    private void excluirVenda() {
        int linhaSelecionada = vendasTable.getSelectedRow();
        
        if (linhaSelecionada == -1) {
            JOptionPane.showMessageDialog(frame, 
                "⚠️ Nenhuma venda selecionada!\n" +
                "Selecione uma venda na tabela para excluir.", 
                "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        String cliente = (String) tableModel.getValueAt(linhaSelecionada, 2);
        
        int confirmacao = JOptionPane.showConfirmDialog(frame, 
            "🗑️ Confirmar Exclusão\n" +
            "Deseja realmente excluir a venda selecionada?\n\n" +
            "Cliente: " + cliente, 
            "Confirmar Exclusão", 
            JOptionPane.YES_NO_OPTION, 
            JOptionPane.QUESTION_MESSAGE);
        
        if (confirmacao == JOptionPane.YES_OPTION) {
            try {
                tableModel.removeRow(linhaSelecionada);
                atualizarEstatisticas();
                
                JOptionPane.showMessageDialog(frame, 
                    "✅ Venda excluída com sucesso!", 
                    "Sucesso", JOptionPane.INFORMATION_MESSAGE);
            } catch (Exception e) {
                JOptionPane.showMessageDialog(frame, 
                    "❌ Erro ao excluir venda: " + e.getMessage(), 
                    "Erro", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    private void limparFormulario() {
        txtCliente.setText("");
        txtProduto.setText("");
        txtValor.setText("");
        txtQuantidade.setText("1");
        cbVendedor.setSelectedIndex(0);
        cbFormaPagamento.setSelectedIndex(0);
        cbStatus.setSelectedIndex(0);
        txtDataVenda.setText(LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
    }
    
    private void gerarRelatorio() {
        JOptionPane.showMessageDialog(frame, 
            "📊 Relatório de Vendas\n\n" +
            "• Relatório de Vendas por Período\n" +
            "• Relatório por Vendedor\n" +
            "• Relatório por Produto\n" +
            "• Relatório por Forma de Pagamento", 
            "Relatórios", 
            JOptionPane.INFORMATION_MESSAGE);
    }
    
    private void atualizarEstatisticas() {
        double totalVendas = 0.0;
        int vendasMes = 0;
        String mesAtual = LocalDate.now().format(DateTimeFormatter.ofPattern("MM/yyyy"));
        
        for (int i = 0; i < tableModel.getRowCount(); i++) {
            String totalStr = tableModel.getValueAt(i, 6).toString();
            double total = Double.parseDouble(totalStr.replace("R$", "").replace(".", "").replace(",", "."));
            totalVendas += total;
            
            String dataVenda = tableModel.getValueAt(i, 1).toString();
            if (dataVenda.contains("/")) {
                String[] partes = dataVenda.split("/");
                if (partes.length >= 2) {
                    String mesAno = partes[1] + "/" + partes[2];
                    if (mesAno.equals(mesAtual)) {
                        vendasMes++;
                    }
                }
            }
        }
        
        double mediaVendas = tableModel.getRowCount() > 0 ? totalVendas / tableModel.getRowCount() : 0.0;
        
        lblTotalVendas.setText("💰 Total Vendas: " + currencyFormat.format(totalVendas));
        lblMediaVendas.setText("📊 Média: " + currencyFormat.format(mediaVendas));
        lblVendasMes.setText("📅 Vendas Mês: " + vendasMes);
    }
    
    private void carregarDadosExemplo() {
        // Limpar tabela
        tableModel.setRowCount(0);
        
        // Dados de exemplo
        Object[][] dados = {
            {1L, "01/05/2024", "João Silva", "Notebook Dell", "R$ 3.500,00", 1, "R$ 3.500,00", "Maria Santos", "Cartão Crédito", "Concluída"},
            {2L, "02/05/2024", "Maria Santos", "Mouse Wireless", "R$ 89,90", 2, "R$ 179,80", "João Silva", "PIX", "Concluída"},
            {3L, "03/05/2024", "Pedro Costa", "Cadeira Executiva", "R$ 450,00", 1, "R$ 450,00", "Ana Oliveira", "Dinheiro", "Concluída"},
            {4L, "04/05/2024", "Ana Oliveira", "Impressora HP", "R$ 1.200,00", 1, "R$ 1.200,00", "Pedro Costa", "Cartão Débito", "Pendente"},
            {5L, "05/05/2024", "Carlos Alberto", "Mesa Escritório", "R$ 320,00", 2, "R$ 640,00", "João Silva", "Transferência", "Concluída"},
            {6L, "06/05/2024", "Fernanda Lima", "Software ERP", "R$ 5.000,00", 1, "R$ 5.000,00", "Maria Santos", "Boleto", "Em Processamento"}
        };
        
        for (Object[] row : dados) {
            tableModel.addRow(row);
        }
        
        atualizarEstatisticas();
    }
}
