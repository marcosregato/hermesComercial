package com.br.hermescomercial.pdv.controller;

import com.br.hermescomercial.util.SystemLogger;
import javax.swing.*;
import java.awt.*;
import java.time.LocalDateTime;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * Formulário especializado de Dashboard Analytics
 * Estende BaseFormulario para reaproveitar componentes comuns
 * Segue o padrão Header → Busca → Formulário → Tabela
 * @author Hermes Comercial
 * @version 3.1.0
 */
public class PDVFormularioDashboard extends BaseFormulario {
    
    // Campos específicos do formulário
    private JTextField txtReceitaTotal;
    private JTextField txtTotalVendas;
    private JTextField txtTicketMedio;
    private JTextField txtCrescimento;
    private JTextField txtMetas;
    private JTextField txtEstoque;
    private JTextField txtLucroLiquido;
    private JTextField txtMargemLucro;
    private JTextField txtClientesAtivos;
    private JTextField txtProdutosVendidos;
    private JTextField txtDevolucoes;
    private JTextField txtCustoTotal;
    private JTextField txtDespesas;
    private JTextField txtTaxaConversao;
    private JTextField txtValorMedioPedido;
    private JTextField txtMetaMensal;
    private JTextField txtPercentualMeta;
    private JTextArea txtObservacoes;
    
    // Campos de busca específicos
    private JComboBox<String> cbPeriodo;
    private JComboBox<String> cbAno;
    
    // Datas para filtros
    private LocalDate dataInicio;
    private LocalDate dataFim;
    
    public PDVFormularioDashboard(JPanel workArea, String usuario, String nome) {
        super(workArea, usuario, nome);
        SystemLogger.ui("[DASHBOARD] Construtor chamado - workArea: " + (workArea != null ? "OK" : "NULO") + 
            ", usuario: " + usuario + ", nome: " + nome);
        // Inicializar datas para evitar NullPointerException
        this.dataInicio = LocalDate.now().minusMonths(1);
        this.dataFim = LocalDate.now();
        SystemLogger.ui("[DASHBOARD] Construtor finalizado - datas inicializadas");
    }
    
    @Override
    protected String getFormTitle() {
        return "📊 Dashboard Analytics";
    }
    
    @Override
    protected String getFormSubtitle() {
        return "Análise completa de métricas e indicadores de negócio";
    }
    
    @Override
    protected String getTableTitle() {
        return "Histórico de Análises";
    }
    
    @Override
    protected String[] getTableColumns() {
        return new String[]{
            "ID", "Período", "Receita Total", "Total Vendas", "Ticket Médio", "Lucro Líquido", 
            "Margem %", "Crescimento %", "Metas %", "Clientes Ativos", "Produtos Vendidos", 
            "Devoluções", "Estoque", "Data Análise", "Responsável", "Status"
        };
    }
    
    @Override
    protected String[] getTiposBusca() {
        return new String[]{"Todos", "Receita", "Vendas", "Lucro", "Clientes"};
    }
    
    @Override
    protected void addAdditionalSearchFields(JPanel fieldsPanel) {
        // Adicionar campos específicos de busca
        fieldsPanel.add(new JLabel("Período:"));
        String[] periodos = {"Hoje", "Semana", "Mês", "Ano", "Personalizado"};
        cbPeriodo = new JComboBox<>(periodos);
        fieldsPanel.add(cbPeriodo);
        
        fieldsPanel.add(new JLabel("Ano:"));
        String[] anos = {"2024", "2025", "2026", "2027"};
        cbAno = new JComboBox<>(anos);
        cbAno.setSelectedItem("2026");
        fieldsPanel.add(cbAno);
        
        // Eventos dos campos adicionais
        cbPeriodo.addActionListener(e -> performSearch());
        cbAno.addActionListener(e -> performSearch());
    }
    
    @Override
    protected void setupAdditionalEvents() {
        // Eventos adicionais já configurados no addAdditionalSearchFields
    }
    
    @Override
    protected void clearAdditionalFields() {
        cbPeriodo.setSelectedIndex(0);
        cbAno.setSelectedItem("2026");
    }
    
    @Override
    protected void createFormPanel() {
        formPanel = new JPanel(new BorderLayout());
        formPanel.setBackground(Color.WHITE);
        formPanel.setBorder(BorderFactory.createTitledBorder("📊 Métricas de Desempenho"));
        
        // Painel de campos
        JPanel fieldsPanel = new JPanel(new GridLayout(5, 4, 10, 10));
        fieldsPanel.setBackground(Color.WHITE);
        
        // Receita Total
        fieldsPanel.add(new JLabel("💰 Receita Total:"));
        txtReceitaTotal = new JTextField();
        txtReceitaTotal.setEditable(false);
        txtReceitaTotal.setBackground(new Color(240, 255, 240));
        fieldsPanel.add(txtReceitaTotal);
        
        // Total de Vendas
        fieldsPanel.add(new JLabel("🛒 Total Vendas:"));
        txtTotalVendas = new JTextField();
        txtTotalVendas.setEditable(false);
        txtTotalVendas.setBackground(new Color(240, 240, 255));
        fieldsPanel.add(txtTotalVendas);
        
        // Ticket Médio
        fieldsPanel.add(new JLabel("🎫 Ticket Médio:"));
        txtTicketMedio = new JTextField();
        txtTicketMedio.setEditable(false);
        txtTicketMedio.setBackground(new Color(255, 245, 200));
        fieldsPanel.add(txtTicketMedio);
        
        // Crescimento
        fieldsPanel.add(new JLabel("📈 Crescimento (%):"));
        txtCrescimento = new JTextField();
        txtCrescimento.setEditable(false);
        txtCrescimento.setBackground(new Color(220, 255, 220));
        fieldsPanel.add(txtCrescimento);
        
        // Metas
        fieldsPanel.add(new JLabel("🎯 Metas (%):"));
        txtMetas = new JTextField();
        txtMetas.setEditable(false);
        txtMetas.setBackground(new Color(255, 245, 200));
        fieldsPanel.add(txtMetas);
        
        // Estoque
        fieldsPanel.add(new JLabel("📦 Estoque:"));
        txtEstoque = new JTextField();
        txtEstoque.setEditable(false);
        txtEstoque.setBackground(new Color(240, 255, 240));
        fieldsPanel.add(txtEstoque);
        
        // Lucro Líquido
        fieldsPanel.add(new JLabel("💵 Lucro Líquido:"));
        txtLucroLiquido = new JTextField();
        txtLucroLiquido.setEditable(false);
        txtLucroLiquido.setBackground(new Color(220, 255, 220));
        fieldsPanel.add(txtLucroLiquido);
        
        // Margem de Lucro
        fieldsPanel.add(new JLabel("📊 Margem Lucro (%):"));
        txtMargemLucro = new JTextField();
        txtMargemLucro.setEditable(false);
        txtMargemLucro.setBackground(new Color(255, 245, 200));
        fieldsPanel.add(txtMargemLucro);
        
        // Clientes Ativos
        fieldsPanel.add(new JLabel("👥 Clientes Ativos:"));
        txtClientesAtivos = new JTextField();
        txtClientesAtivos.setEditable(false);
        txtClientesAtivos.setBackground(new Color(240, 240, 255));
        fieldsPanel.add(txtClientesAtivos);
        
        // Produtos Vendidos
        fieldsPanel.add(new JLabel("📦 Produtos Vendidos:"));
        txtProdutosVendidos = new JTextField();
        txtProdutosVendidos.setEditable(false);
        txtProdutosVendidos.setBackground(new Color(240, 255, 240));
        fieldsPanel.add(txtProdutosVendidos);
        
        // Devoluções
        fieldsPanel.add(new JLabel("↩️ Devoluções:"));
        txtDevolucoes = new JTextField();
        txtDevolucoes.setEditable(false);
        txtDevolucoes.setBackground(new Color(255, 240, 240));
        fieldsPanel.add(txtDevolucoes);
        
        // Custo Total
        fieldsPanel.add(new JLabel("💸 Custo Total:"));
        txtCustoTotal = new JTextField();
        txtCustoTotal.setEditable(false);
        txtCustoTotal.setBackground(new Color(255, 240, 240));
        fieldsPanel.add(txtCustoTotal);
        
        // Despesas
        fieldsPanel.add(new JLabel("📉 Despesas:"));
        txtDespesas = new JTextField();
        txtDespesas.setEditable(false);
        txtDespesas.setBackground(new Color(255, 240, 240));
        fieldsPanel.add(txtDespesas);
        
        // Taxa de Conversão
        fieldsPanel.add(new JLabel("🔄 Taxa Conversão (%):"));
        txtTaxaConversao = new JTextField();
        txtTaxaConversao.setEditable(false);
        txtTaxaConversao.setBackground(new Color(255, 245, 200));
        fieldsPanel.add(txtTaxaConversao);
        
        // Valor Médio do Pedido
        fieldsPanel.add(new JLabel("💰 Valor Médio Pedido:"));
        txtValorMedioPedido = new JTextField();
        txtValorMedioPedido.setEditable(false);
        txtValorMedioPedido.setBackground(new Color(240, 255, 240));
        fieldsPanel.add(txtValorMedioPedido);
        
        // Meta Mensal
        fieldsPanel.add(new JLabel("🎯 Meta Mensal:"));
        txtMetaMensal = new JTextField();
        txtMetaMensal.setEditable(false);
        txtMetaMensal.setBackground(new Color(255, 245, 200));
        fieldsPanel.add(txtMetaMensal);
        
        // Percentual da Meta
        fieldsPanel.add(new JLabel("📊 % Meta Atingida:"));
        txtPercentualMeta = new JTextField();
        txtPercentualMeta.setEditable(false);
        txtPercentualMeta.setBackground(new Color(220, 255, 220));
        fieldsPanel.add(txtPercentualMeta);
        
        // Observações
        fieldsPanel.add(new JLabel("📝 Observações:"));
        txtObservacoes = new JTextArea(3, 20);
        txtObservacoes.setEditable(true);
        txtObservacoes.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        
        // Painel de observações
        JPanel obsPanel = new JPanel(new BorderLayout());
        obsPanel.setBackground(Color.WHITE);
        obsPanel.add(new JLabel("📝 Observações:"), BorderLayout.NORTH);
        obsPanel.add(new JScrollPane(txtObservacoes), BorderLayout.CENTER);
        
        // Painel principal do formulário
        JPanel mainFormPanel = new JPanel(new BorderLayout());
        mainFormPanel.setBackground(Color.WHITE);
        mainFormPanel.add(fieldsPanel, BorderLayout.CENTER);
        mainFormPanel.add(obsPanel, BorderLayout.SOUTH);
        
        formPanel.add(mainFormPanel, BorderLayout.CENTER);
    }
    
    @Override
    protected void performSearch() {
        SystemLogger.ui("[DASHBOARD] performSearch() chamado - tela ativa");
        
        try {
            String tipoBusca = (String) cbTipoBusca.getSelectedItem();
            String busca = txtBusca.getText().trim();
            String periodo = (String) cbPeriodo.getSelectedItem();
            String ano = (String) cbAno.getSelectedItem();
            
            SystemLogger.ui("[DASHBOARD] Parâmetros de busca - tipo: " + tipoBusca + 
                ", busca: '" + busca + "', período: " + periodo + ", ano: " + ano);
            
            // Atualizar datas
            updateDataRange();
            
            // Implementar lógica de busca específica
            refreshData();
            
            SystemLogger.ui("[DASHBOARD] Busca concluída com sucesso");
            
        } catch (Exception e) {
            SystemLogger.error("[DASHBOARD] Erro ao realizar busca: " + e.getMessage());
            SystemLogger.error("[DASHBOARD] Stack trace: " + e.toString());
        }
    }
    
    @Override
    protected void refreshData() {
        SystemLogger.ui("[DASHBOARD] Iniciando atualização de dados...");
        
        try {
            SystemLogger.ui("[DASHBOARD] Verificando componentes do formulário...");
            
            // Verificar se os componentes estão inicializados
            if (txtReceitaTotal == null) {
                SystemLogger.error("[DASHBOARD] txtReceitaTotal é nulo");
                return;
            }
            if (txtTotalVendas == null) {
                SystemLogger.error("[DASHBOARD] txtTotalVendas é nulo");
                return;
            }
            
            SystemLogger.ui("[DASHBOARD] Atualizando campos do formulário com dados de exemplo...");
            
            // Atualizar campos do formulário com dados de exemplo
            txtReceitaTotal.setText("R$ 45.678,90");
            txtTotalVendas.setText("234");
            txtTicketMedio.setText("R$ 195,20");
            txtCrescimento.setText("15,8");
            txtMetas.setText("87,5");
            txtEstoque.setText("1.234 unidades");
            txtLucroLiquido.setText("R$ 12.345,67");
            txtMargemLucro.setText("27,0");
            txtClientesAtivos.setText("89");
            txtProdutosVendidos.setText("456");
            txtDevolucoes.setText("12");
            txtCustoTotal.setText("R$ 28.456,23");
            txtDespesas.setText("R$ 4.877,00");
            txtTaxaConversao.setText("3,2");
            txtValorMedioPedido.setText("R$ 195,20");
            txtMetaMensal.setText("R$ 50.000,00");
            txtPercentualMeta.setText("91,4");
            
            SystemLogger.ui("[DASHBOARD] Verificando datas para formatação...");
            
            // Verificar se as datas não são nulas antes de formatar
            String periodoInfo = "";
            if (dataInicio != null && dataFim != null) {
                periodoInfo = "\nPeríodo analisado: " + dataInicio.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")) +
                    " a " + dataFim.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
                SystemLogger.ui("[DASHBOARD] Período formatado: " + periodoInfo);
            } else {
                SystemLogger.ui("[DASHBOARD] Datas são nulas, usando período padrão");
            }
            
            if (txtObservacoes != null) {
                txtObservacoes.setText("Dashboard atualizado em " + 
                    LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss")) +
                    periodoInfo);
                SystemLogger.ui("[DASHBOARD] Observações atualizadas");
            } else {
                SystemLogger.error("[DASHBOARD] txtObservações é nulo");
            }
            
            SystemLogger.ui("[DASHBOARD] Dashboard atualizado com sucesso");
            
        } catch (Exception e) {
            SystemLogger.error("[DASHBOARD] Erro ao atualizar dashboard: " + e.getMessage());
            SystemLogger.error("[DASHBOARD] Stack trace: " + e.toString());
            
            if (workArea != null) {
                JOptionPane.showMessageDialog(workArea, 
                    "Erro ao atualizar dashboard: " + e.getMessage(), 
                    "Erro", JOptionPane.ERROR_MESSAGE);
            } else {
                SystemLogger.error("[DASHBOARD] workArea é nulo, não é possível exibir mensagem de erro");
            }
        }
    }
    
    @Override
    protected void loadSampleData() {
        // Carregar dados de exemplo na tabela
        Object[] sampleData1 = {
            "001", "Mensal", "R$ 45.678,90", "234", "R$ 195,20", "R$ 12.345,67",
            "27,0", "15,8", "87,5", "89", "456", "12", "1.234",
            "09/05/2026", "João Silva", "Concluído"
        };
        
        Object[] sampleData2 = {
            "002", "Semanal", "R$ 11.234,50", "58", "R$ 193,70", "R$ 2.890,45",
            "25,7", "8,2", "78,3", "45", "123", "3", "456",
            "02/05/2026", "Maria Santos", "Concluído"
        };
        
        Object[] sampleData3 = {
            "003", "Diário", "R$ 1.545,80", "8", "R$ 193,23", "R$ 415,67",
            "26,9", "5,1", "92,1", "12", "18", "0", "89",
            "09/05/2026", "Pedro Oliveira", "Concluído"
        };
        
        tableModel.addRow(sampleData1);
        tableModel.addRow(sampleData2);
        tableModel.addRow(sampleData3);
    }
    
    @Override
    protected void loadSelectedData() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow >= 0) {
            // Carregar dados da linha selecionada para o formulário
            txtReceitaTotal.setText(tableModel.getValueAt(selectedRow, 2).toString());
            txtTotalVendas.setText(tableModel.getValueAt(selectedRow, 3).toString());
            txtTicketMedio.setText(tableModel.getValueAt(selectedRow, 4).toString());
            txtLucroLiquido.setText(tableModel.getValueAt(selectedRow, 5).toString());
            txtMargemLucro.setText(tableModel.getValueAt(selectedRow, 6).toString());
            txtCrescimento.setText(tableModel.getValueAt(selectedRow, 7).toString());
            txtMetas.setText(tableModel.getValueAt(selectedRow, 8).toString());
            txtClientesAtivos.setText(tableModel.getValueAt(selectedRow, 9).toString());
            txtProdutosVendidos.setText(tableModel.getValueAt(selectedRow, 10).toString());
            txtDevolucoes.setText(tableModel.getValueAt(selectedRow, 11).toString());
            txtEstoque.setText(tableModel.getValueAt(selectedRow, 12).toString());
            
            String dataAnalise = tableModel.getValueAt(selectedRow, 13).toString();
            String responsavel = tableModel.getValueAt(selectedRow, 14).toString();
            
            txtObservacoes.setText("Análise selecionada: " + dataAnalise +
                "\nResponsável: " + responsavel +
                "\nDados carregados da tabela");
        }
    }
    
    @Override
    protected void addNew() {
        SystemLogger.ui("[DASHBOARD] Botão Novo clicado - tela ativa");
        try {
            JOptionPane.showMessageDialog(workArea, 
                "Funcionalidade de adicionar análise em desenvolvimento...", 
                "Nova Análise", JOptionPane.INFORMATION_MESSAGE);
            SystemLogger.ui("[DASHBOARD] Diálogo Novo exibido com sucesso");
        } catch (Exception e) {
            SystemLogger.error("[DASHBOARD] Erro ao exibir diálogo Novo: " + e.getMessage());
        }
    }
    
    @Override
    protected void editSelected() {
        SystemLogger.ui("[DASHBOARD] Botão Editar clicado - tela ativa");
        try {
            int selectedRow = table.getSelectedRow();
            SystemLogger.ui("[DASHBOARD] Linha selecionada: " + selectedRow);
            
            if (selectedRow >= 0) {
                JOptionPane.showMessageDialog(workArea, 
                    "Funcionalidade de editar análise em desenvolvimento...", 
                    "Editar Análise", JOptionPane.INFORMATION_MESSAGE);
                SystemLogger.ui("[DASHBOARD] Diálogo Editar exibido com sucesso");
            } else {
                JOptionPane.showMessageDialog(workArea, 
                    "Selecione uma análise para editar", 
                    "Aviso", JOptionPane.WARNING_MESSAGE);
                SystemLogger.ui("[DASHBOARD] Aviso de seleção exibido");
            }
        } catch (Exception e) {
            SystemLogger.error("[DASHBOARD] Erro ao processar edição: " + e.getMessage());
        }
    }
    
    @Override
    protected void performExport(java.io.File file) throws Exception {
        // Implementar exportação específica para dashboard
        throw new UnsupportedOperationException("Exportação de dashboard em desenvolvimento");
    }
    
    @Override
    protected String getFileName() {
        return "dashboard_analytics_";
    }
    
    /**
     * Atualiza o range de datas com base no período selecionado
     */
    private void updateDataRange() {
        String periodo = (String) cbPeriodo.getSelectedItem();
        LocalDate hoje = LocalDate.now();
        
        switch (periodo) {
            case "Hoje":
                dataInicio = hoje;
                dataFim = hoje;
                break;
            case "Semana":
                dataInicio = hoje.minusDays(7);
                dataFim = hoje;
                break;
            case "Mês":
                dataInicio = hoje.minusMonths(1);
                dataFim = hoje;
                break;
            case "Ano":
                dataInicio = hoje.minusYears(1);
                dataFim = hoje;
                break;
            default:
                dataInicio = hoje.minusMonths(1);
                dataFim = hoje;
                break;
        }
    }
    
    /**
     * Método estático para criar o formulário de dashboard
     * @return JPanel configurado do formulário
     */
    public static JPanel criarFormularioDashboard() {
        SystemLogger.ui("[TELA_ATIVA] Dashboard Analytics - INICIANDO criação");
        SystemLogger.ui("[TELA_ATIVA] Dashboard Analytics - Status: CRIANDO");
        
        try {
            PDVFormularioDashboard dashboard = new PDVFormularioDashboard(
                new JPanel(), "usuario", "Nome Usuario"
            );
            JPanel panel = dashboard.getMainPanel();
            
            if (panel != null) {
                SystemLogger.ui("[TELA_ATIVA] Dashboard Analytics - CRIADO com sucesso");
                SystemLogger.ui("[TELA_ATIVA] Dashboard Analytics - Status: PRONTO PARA USO");
                SystemLogger.ui("[TELA_ATIVA] Dashboard Analytics - Tela ATIVA e visível");
            } else {
                SystemLogger.error("[TELA_ATIVA] Dashboard Analytics - FALHA ao criar painel");
            }
            
            return panel;
        } catch (Exception e) {
            SystemLogger.error("[TELA_ATIVA] Dashboard Analytics - ERRO na criação: " + e.getMessage());
            SystemLogger.error("[TELA_ATIVA] Dashboard Analytics - Status: ERRO");
            SystemLogger.error("[DASHBOARD] Stack trace: " + e.toString());
            throw new RuntimeException("Falha ao criar dashboard", e);
        }
    }
}
