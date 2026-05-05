package com.br.hermescomercial.erp.controller;

import com.br.hermescomercial.ui.layout.LayoutPadrao;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * Controller para tela de gestão financeira do ERP
 * Versão 2.3.0 - Arquitetura Modular - Tema Padrão Hermes
 */
public class ERPGestaoFinanceiraSwingController {
    
    private JFrame frame;
    private JPanel mainPanel;
    private JTable tabelaLancamentos;
    private DefaultTableModel tableModel;
    private JTextField txtBusca, txtDescricao, txtValor;
    private JComboBox<String> cbTipo, cbCategoria, cbStatus, cbPeriodo;
    private JFormattedTextField txtDataVencimento;
    private JLabel lblTotalReceber, lblTotalPagar, lblSaldo;
    private DecimalFormat df;
    
    public ERPGestaoFinanceiraSwingController() {
        inicializarUI();
    }
    
    private void inicializarUI() {
        frame = new JFrame("💰 Gestão Financeira - ERP");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setSize(1400, 900);
        frame.setLocationRelativeTo(null);
        
        // Configurar formato decimal
        df = (DecimalFormat) NumberFormat.getNumberInstance();
        df.applyPattern("#,##0.00");
        
        // Aplicar tema padrão
        frame.getContentPane().setBackground(LayoutPadrao.COR_FUNDO_ESCURO);
        
        // Criar painéis de formulário e tabela
        JPanel formularioPanel = createFormPanel();
        JPanel tabelaPanel = createTablePanel();
        
        // Usando layout padrão Header → Busca → Formulário → Tabela
        mainPanel = LayoutPadrao.criarLayoutPadraoGestao(
            false, // isPDV (false para ERP)
            "💰 Gestão Financeira - ERP",
            "Digite descrição, tipo ou categoria do lançamento...",
            formularioPanel,
            tabelaPanel
        );
        
        frame.add(mainPanel);
        
        carregarDadosExemplo();
    }
    
    private JPanel createFormPanel() {
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setOpaque(false);
        formPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.WEST;
        
        // Campo de busca
        gbc.gridx = 0; gbc.gridy = 0; gbc.weightx = 0.0;
        formPanel.add(new JLabel("🔍 Busca:"), gbc);
        
        gbc.gridx = 1; gbc.weightx = 1.0;
        txtBusca = new JTextField(20);
        txtBusca.setFont(LayoutPadrao.FONTE_CAMPO);
        formPanel.add(txtBusca, gbc);
        
        // Filtros
        gbc.gridx = 2; gbc.weightx = 0.0;
        formPanel.add(new JLabel("Período:"), gbc);
        
        gbc.gridx = 3;
        cbPeriodo = new JComboBox<>(new String[]{"Todos", "Este Mês", "Mês Passado", "Este Ano"});
        cbPeriodo.setFont(LayoutPadrao.FONTE_CAMPO);
        formPanel.add(cbPeriodo, gbc);
        
        gbc.gridx = 4;
        formPanel.add(new JLabel("Status:"), gbc);
        
        gbc.gridx = 5;
        cbStatus = new JComboBox<>(new String[]{"Todos", "Pendente", "Pago", "Vencido"});
        cbStatus.setFont(LayoutPadrao.FONTE_CAMPO);
        formPanel.add(cbStatus, gbc);
        
        // Botões de ação
        gbc.gridx = 6;
        JButton btnBuscar = LayoutPadrao.criarBotaoPrimario("🔍 Buscar");
        btnBuscar.addActionListener(e -> buscarLancamentos());
        formPanel.add(btnBuscar, gbc);
        
        gbc.gridx = 7;
        JButton btnLimpar = LayoutPadrao.criarBotaoSecundario("🧹 Limpar");
        btnLimpar.addActionListener(e -> limparBusca());
        formPanel.add(btnLimpar, gbc);
        
        // Formulário de lançamento
        gbc.gridx = 0; gbc.gridy = 1; gbc.gridwidth = 8;
        JSeparator separator = new JSeparator();
        separator.setForeground(LayoutPadrao.COR_BORDA);
        formPanel.add(separator, gbc);
        
        // Campos do formulário
        gbc.gridy = 2; gbc.gridwidth = 1;
        
        // Tipo
        gbc.gridx = 0; gbc.weightx = 0.0;
        formPanel.add(new JLabel("Tipo:"), gbc);
        
        gbc.gridx = 1; gbc.weightx = 1.0;
        cbTipo = new JComboBox<>(new String[]{"Receita", "Despesa"});
        cbTipo.setFont(LayoutPadrao.FONTE_CAMPO);
        formPanel.add(cbTipo, gbc);
        
        // Descrição
        gbc.gridx = 2; gbc.weightx = 0.0;
        formPanel.add(new JLabel("Descrição:"), gbc);
        
        gbc.gridx = 3; gbc.gridwidth = 3; gbc.weightx = 1.0;
        txtDescricao = new JTextField(30);
        txtDescricao.setFont(LayoutPadrao.FONTE_CAMPO);
        formPanel.add(txtDescricao, gbc);
        
        // Categoria
        gbc.gridx = 6; gbc.gridwidth = 1; gbc.weightx = 0.0;
        formPanel.add(new JLabel("Categoria:"), gbc);
        
        gbc.gridx = 7; gbc.weightx = 1.0;
        cbCategoria = new JComboBox<>(new String[]{
            "Aluguel", "Salários", "Fornecedores", "Impostos", "Vendas", 
            "Serviços", "Outros"
        });
        cbCategoria.setFont(LayoutPadrao.FONTE_CAMPO);
        formPanel.add(cbCategoria, gbc);
        
        // Valor e Data
        gbc.gridy = 3;
        
        gbc.gridx = 0; gbc.weightx = 0.0;
        formPanel.add(new JLabel("Valor:"), gbc);
        
        gbc.gridx = 1; gbc.weightx = 1.0;
        txtValor = new JTextField(15);
        txtValor.setFont(LayoutPadrao.FONTE_CAMPO);
        formPanel.add(txtValor, gbc);
        
        gbc.gridx = 2; gbc.weightx = 0.0;
        formPanel.add(new JLabel("Vencimento:"), gbc);
        
        gbc.gridx = 3; gbc.weightx = 1.0;
        txtDataVencimento = new JFormattedTextField();
        txtDataVencimento.setFont(LayoutPadrao.FONTE_CAMPO);
        txtDataVencimento.setText(LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
        formPanel.add(txtDataVencimento, gbc);
        
        // Botões de operação
        gbc.gridx = 4; gbc.gridwidth = 4;
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.setOpaque(false);
        
        JButton btnSalvar = LayoutPadrao.criarBotaoSucesso("💾 Salvar");
        btnSalvar.addActionListener(e -> salvarLancamento());
        buttonPanel.add(btnSalvar);
        
        JButton btnEditar = LayoutPadrao.criarBotaoPrimario("✏️ Editar");
        btnEditar.addActionListener(e -> editarLancamento());
        buttonPanel.add(btnEditar);
        
        JButton btnExcluir = LayoutPadrao.criarBotaoPerigo("🗑️ Excluir");
        btnExcluir.addActionListener(e -> excluirLancamento());
        buttonPanel.add(btnExcluir);
        
        JButton btnRelatorio = LayoutPadrao.criarBotaoSecundario("📊 Relatório");
        btnRelatorio.addActionListener(e -> gerarRelatorio());
        buttonPanel.add(btnRelatorio);
        
        JButton btnFluxoCaixa = LayoutPadrao.criarBotaoSecundario("💰 Fluxo");
        btnFluxoCaixa.addActionListener(e -> abrirFluxoCaixa());
        buttonPanel.add(btnFluxoCaixa);
        
        formPanel.add(buttonPanel, gbc);
        
        return formPanel;
    }
    
    private JPanel createTablePanel() {
        JPanel tablePanel = new JPanel(new BorderLayout());
        tablePanel.setOpaque(false);
        
        // Configurar tabela
        String[] colunas = {"ID", "Tipo", "Descrição", "Categoria", "Valor", "Vencimento", "Status"};
        
        tableModel = new DefaultTableModel(colunas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        tabelaLancamentos = new JTable(tableModel);
        tabelaLancamentos.setFont(LayoutPadrao.FONTE_TEXTO);
        tabelaLancamentos.getTableHeader().setFont(LayoutPadrao.FONTE_SUBTITULO);
        tabelaLancamentos.setRowHeight(25);
        tabelaLancamentos.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        
        // Scroll pane
        JScrollPane scrollPane = new JScrollPane(tabelaLancamentos);
        scrollPane.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        
        tablePanel.add(scrollPane, BorderLayout.CENTER);
        
        // Painel de resumo financeiro
        JPanel resumoPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        resumoPanel.setOpaque(false);
        resumoPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        lblTotalReceber = new JLabel("💰 A Receber: R$ 0,00");
        lblTotalReceber.setFont(LayoutPadrao.FONTE_SUBTITULO);
        lblTotalReceber.setForeground(new Color(40, 167, 69));
        resumoPanel.add(lblTotalReceber);
        
        lblTotalPagar = new JLabel("💸 A Pagar: R$ 0,00");
        lblTotalPagar.setFont(LayoutPadrao.FONTE_SUBTITULO);
        lblTotalPagar.setForeground(new Color(220, 53, 69));
        resumoPanel.add(lblTotalPagar);
        
        lblSaldo = new JLabel("⚖️ Saldo: R$ 0,00");
        lblSaldo.setFont(LayoutPadrao.FONTE_TITULO);
        lblSaldo.setForeground(LayoutPadrao.COR_PRIMARIA);
        resumoPanel.add(lblSaldo);
        
        tablePanel.add(resumoPanel, BorderLayout.NORTH);
        
        return tablePanel;
    }
    
    private void carregarDadosExemplo() {
        // Limpar tabela
        tableModel.setRowCount(0);
        
        // Adicionar dados de exemplo
        Object[][] dados = {
            {1, "Receita", "Venda de Produtos", "Vendas", 15000.00, "15/05/2024", "Recebido"},
            {2, "Despesa", "Aluguel do Escritório", "Aluguel", 3500.00, "10/05/2024", "Pago"},
            {3, "Despesa", "Salários Funcionários", "Salários", 8500.00, "05/05/2024", "Pago"},
            {4, "Receita", "Serviços de Consultoria", "Serviços", 3200.00, "20/05/2024", "Pendente"},
            {5, "Despesa", "Fornecedor de Material", "Fornecedores", 1200.00, "25/05/2024", "Pendente"},
            {6, "Receita", "Venda de Software", "Vendas", 4500.00, "18/05/2024", "Recebido"},
            {7, "Despesa", "Impostos Municipais", "Impostos", 800.00, "30/05/2024", "Vencido"}
        };
        
        for (Object[] row : dados) {
            tableModel.addRow(row);
        }
        
        atualizarTabela();
        atualizarResumo();
    }
    
    private void buscarLancamentos() {
        String termo = txtBusca.getText().trim().toLowerCase();
        String periodo = (String) cbPeriodo.getSelectedItem();
        String status = (String) cbStatus.getSelectedItem();
        
        if (termo.isEmpty() && periodo.equals("Todos") && status.equals("Todos")) {
            carregarDadosExemplo();
            return;
        }
        
        // Simulação de busca - na implementação real, filtraria os dados
        JOptionPane.showMessageDialog(frame, 
            "🔍 Busca executada\n" +
            "Termo: " + termo + "\n" +
            "Período: " + periodo + "\n" +
            "Status: " + status, 
            "Busca de Lançamentos", 
            JOptionPane.INFORMATION_MESSAGE);
    }
    
    private void limparBusca() {
        txtBusca.setText("");
        cbPeriodo.setSelectedIndex(0);
        cbStatus.setSelectedIndex(0);
        carregarDadosExemplo();
    }
    
    private void salvarLancamento() {
        String tipo = (String) cbTipo.getSelectedItem();
        String descricao = txtDescricao.getText().trim();
        String categoria = (String) cbCategoria.getSelectedItem();
        String valorStr = txtValor.getText().trim();
        String dataVencimento = txtDataVencimento.getText().trim();
        
        // Validação
        if (descricao.isEmpty()) {
            JOptionPane.showMessageDialog(frame, 
                "⚠️ Descrição é obrigatória!", 
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
            BigDecimal valor = new BigDecimal(valorStr.replace(",", "."));
            
            // Adicionar à tabela
            int id = tableModel.getRowCount() + 1;
            String status = LocalDate.parse(dataVencimento, DateTimeFormatter.ofPattern("dd/MM/yyyy"))
                              .isBefore(LocalDate.now()) ? "Vencido" : "Pendente";
            
            Object[] newRow = {id, tipo, descricao, categoria, valor, dataVencimento, status};
            tableModel.addRow(newRow);
            
            JOptionPane.showMessageDialog(frame, 
                "✅ Lançamento salvo com sucesso!\n" +
                "Tipo: " + tipo + "\n" +
                "Descrição: " + descricao + "\n" +
                "Valor: R$ " + df.format(valor) + "\n" +
                "Categoria: " + categoria, 
                "Sucesso", JOptionPane.INFORMATION_MESSAGE);
            
            limparFormulario();
            atualizarTabela();
            atualizarResumo();
            
        } catch (Exception e) {
            JOptionPane.showMessageDialog(frame, 
                "❌ Erro ao salvar lançamento: " + e.getMessage(), 
                "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void editarLancamento() {
        int linhaSelecionada = tabelaLancamentos.getSelectedRow();
        if (linhaSelecionada == -1) {
            JOptionPane.showMessageDialog(frame, 
                "⚠️ Selecione um lançamento para editar!", 
                "Editar Lançamento", 
                JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        // Preencher formulário com dados selecionados
        cbTipo.setSelectedItem(tableModel.getValueAt(linhaSelecionada, 1));
        txtDescricao.setText(tableModel.getValueAt(linhaSelecionada, 2).toString());
        cbCategoria.setSelectedItem(tableModel.getValueAt(linhaSelecionada, 3));
        txtValor.setText(df.format(tableModel.getValueAt(linhaSelecionada, 4)));
        txtDataVencimento.setText(tableModel.getValueAt(linhaSelecionada, 5).toString());
        
        JOptionPane.showMessageDialog(frame, 
            "✏️ Preencha o formulário com os novos dados e clique em Salvar", 
            "Editar Lançamento", 
            JOptionPane.INFORMATION_MESSAGE);
    }
    
    private void excluirLancamento() {
        int linhaSelecionada = tabelaLancamentos.getSelectedRow();
        if (linhaSelecionada == -1) {
            JOptionPane.showMessageDialog(frame, 
                "⚠️ Selecione um lançamento para excluir!", 
                "Excluir Lançamento", 
                JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        String descricao = tableModel.getValueAt(linhaSelecionada, 2).toString();
        int confirmacao = JOptionPane.showConfirmDialog(frame, 
            "🗑️ Confirmar Exclusão\n\n" +
            "Deseja realmente excluir o lançamento:\n" + descricao + "?", 
            "Excluir Lançamento", 
            JOptionPane.YES_NO_OPTION, 
            JOptionPane.QUESTION_MESSAGE);
        
        if (confirmacao == JOptionPane.YES_OPTION) {
            tableModel.removeRow(linhaSelecionada);
            atualizarTabela();
            atualizarResumo();
            
            JOptionPane.showMessageDialog(frame, 
                "✅ Lançamento excluído com sucesso!", 
                "Sucesso", JOptionPane.INFORMATION_MESSAGE);
        }
    }
    
    private void gerarRelatorio() {
        JOptionPane.showMessageDialog(frame, 
            "📊 Relatórios Financeiros\n\n" +
            "• Relatório de Contas a Pagar\n" +
            "• Relatório de Contas a Receber\n" +
            "• Fluxo de Caixa\n" +
            "• Análise de Rentabilidade", 
            "Relatórios", 
            JOptionPane.INFORMATION_MESSAGE);
    }
    
    private void abrirFluxoCaixa() {
        JOptionPane.showMessageDialog(frame, 
            "💰 Fluxo de Caixa\n\n" +
            "• Entradas e Saídas por Período\n" +
            "• Análise de Fluxo\n" +
            "• Projeções Financeiras", 
            "Fluxo de Caixa", 
            JOptionPane.INFORMATION_MESSAGE);
    }
    
    private void limparFormulario() {
        txtDescricao.setText("");
        txtValor.setText("");
        txtDataVencimento.setText(LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
        cbTipo.setSelectedIndex(0);
        cbCategoria.setSelectedIndex(0);
    }
    
    private void atualizarTabela() {
        // Método para atualizar a tabela (se necessário)
    }
    
    private void atualizarResumo() {
        double totalReceber = 0.0;
        double totalPagar = 0.0;
        
        for (int i = 0; i < tableModel.getRowCount(); i++) {
            String tipo = tableModel.getValueAt(i, 1).toString();
            double valor = Double.parseDouble(tableModel.getValueAt(i, 4).toString());
            
            if (tipo.equals("Receita")) {
                totalReceber += valor;
            } else {
                totalPagar += valor;
            }
        }
        
        double saldo = totalReceber - totalPagar;
        
        lblTotalReceber.setText("💰 A Receber: R$ " + df.format(totalReceber));
        lblTotalPagar.setText("💸 A Pagar: R$ " + df.format(totalPagar));
        lblSaldo.setText("⚖️ Saldo: R$ " + df.format(saldo));
    }
}
