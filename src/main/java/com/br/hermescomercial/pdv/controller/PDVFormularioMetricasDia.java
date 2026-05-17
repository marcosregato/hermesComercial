package com.br.hermescomercial.pdv.controller;

import com.br.hermescomercial.util.SystemLogger;
import javax.swing.*;
import java.awt.*;
import java.time.LocalDateTime;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * Formulário especializado de Métricas do Dia
 * Estende BaseFormulario para reaproveitar componentes comuns
 * Segue o padrão Header → Busca → Formulário → Tabela
 * @author Hermes Comercial
 * @version 3.1.0
 */
public class PDVFormularioMetricasDia extends BaseFormulario {
    
    // Campos específicos do formulário
    private JTextField txtVendasDia;
    private JTextField txtReceitaDia;
    private JTextField txtTicketMedio;
    private JTextField txtClientesAtendidos;
    private JTextField txtProdutosVendidos;
    private JTextField txtDevolucoes;
    private JTextField txtTaxaConversao;
    private JTextField txtMetaDiaria;
    private JTextField txtPercentualMeta;
    private JTextField txtLucroLiquido;
    private JTextField txtCustoTotal;
    private JTextField txtDespesasDia;
    private JTextField txtMargemLucro;
    private JTextField txtHorasPico;
    private JTextField txtFormasPagamento;
    private JTextArea txtObservacoes;
    
    // Campos de busca específicos
    private JComboBox<String> cbPeriodo;
    private JComboBox<String> cbAno;
    
    // Datas para filtros
    private LocalDate dataInicio;
    private LocalDate dataFim;
    
    public PDVFormularioMetricasDia(JPanel workArea, String usuario, String nome) {
        super(workArea, usuario, nome);
        SystemLogger.ui("[METRICAS] Construtor chamado - workArea: " + (workArea != null ? "OK" : "NULO") + 
            ", usuario: " + usuario + ", nome: " + nome);
        // Inicializar datas para evitar NullPointerException
        this.dataInicio = LocalDate.now().minusMonths(1);
        this.dataFim = LocalDate.now();
        SystemLogger.ui("[METRICAS] Construtor finalizado - datas inicializadas");
    }
    
    @Override
    protected String getFormTitle() {
        return "📈 Métricas do Dia";
    }
    
    @Override
    protected String getFormSubtitle() {
        return "Análise detalhada das métricas diárias de negócio";
    }
    
    @Override
    protected String getTableTitle() {
        return "Histórico de Métricas Diárias";
    }
    
    @Override
    protected String[] getTableColumns() {
        return new String[]{
            "ID", "Data", "Vendas", "Receita", "Ticket Médio", "Clientes", 
            "Produtos", "Devoluções", "Taxa Conv. %", "Lucro Líquido", "Custo Total",
            "Despesas", "Margem %", "Meta Diária", "% Meta", "Horas Pico", 
            "Formas Pagto", "Responsável", "Status"
        };
    }
    
    @Override
    protected String[] getTiposBusca() {
        return new String[]{"Todos", "Vendas", "Receita", "Clientes", "Produtos"};
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
        formPanel.setBorder(BorderFactory.createTitledBorder("📊 Resumo de Métricas Diárias"));
        
        // Painel de campos
        JPanel fieldsPanel = new JPanel(new GridLayout(4, 4, 10, 10));
        fieldsPanel.setBackground(Color.WHITE);
        
        // Vendas do Dia
        fieldsPanel.add(new JLabel("💰 Vendas do Dia:"));
        txtVendasDia = new JTextField();
        txtVendasDia.setEditable(false);
        txtVendasDia.setBackground(new Color(240, 255, 240));
        fieldsPanel.add(txtVendasDia);
        
        // Receita do Dia
        fieldsPanel.add(new JLabel("💵 Receita do Dia:"));
        txtReceitaDia = new JTextField();
        txtReceitaDia.setEditable(false);
        txtReceitaDia.setBackground(new Color(240, 255, 240));
        fieldsPanel.add(txtReceitaDia);
        
        // Ticket Médio
        fieldsPanel.add(new JLabel("🎫 Ticket Médio:"));
        txtTicketMedio = new JTextField();
        txtTicketMedio.setEditable(false);
        txtTicketMedio.setBackground(new Color(255, 245, 200));
        fieldsPanel.add(txtTicketMedio);
        
        // Clientes Atendidos
        fieldsPanel.add(new JLabel("👥 Clientes Atendidos:"));
        txtClientesAtendidos = new JTextField();
        txtClientesAtendidos.setEditable(false);
        txtClientesAtendidos.setBackground(new Color(240, 240, 255));
        fieldsPanel.add(txtClientesAtendidos);
        
        // Produtos Vendidos
        fieldsPanel.add(new JLabel("🛍️ Produtos Vendidos:"));
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
        
        // Taxa de Conversão
        fieldsPanel.add(new JLabel("🔄 Taxa Conversão (%):"));
        txtTaxaConversao = new JTextField();
        txtTaxaConversao.setEditable(false);
        txtTaxaConversao.setBackground(new Color(255, 245, 200));
        fieldsPanel.add(txtTaxaConversao);
        
        // Meta Diária
        fieldsPanel.add(new JLabel("🎯 Meta Diária:"));
        txtMetaDiaria = new JTextField();
        txtMetaDiaria.setEditable(false);
        txtMetaDiaria.setBackground(new Color(255, 245, 200));
        fieldsPanel.add(txtMetaDiaria);
        
        // Percentual da Meta
        fieldsPanel.add(new JLabel("📊 % Meta Atingida:"));
        txtPercentualMeta = new JTextField();
        txtPercentualMeta.setEditable(false);
        txtPercentualMeta.setBackground(new Color(220, 255, 220));
        fieldsPanel.add(txtPercentualMeta);
        
        // Lucro Líquido
        fieldsPanel.add(new JLabel("💵 Lucro Líquido:"));
        txtLucroLiquido = new JTextField();
        txtLucroLiquido.setEditable(false);
        txtLucroLiquido.setBackground(new Color(220, 255, 220));
        fieldsPanel.add(txtLucroLiquido);
        
        // Custo Total
        fieldsPanel.add(new JLabel("💸 Custo Total:"));
        txtCustoTotal = new JTextField();
        txtCustoTotal.setEditable(false);
        txtCustoTotal.setBackground(new Color(255, 240, 240));
        fieldsPanel.add(txtCustoTotal);
        
        // Despesas do Dia
        fieldsPanel.add(new JLabel("📉 Despesas do Dia:"));
        txtDespesasDia = new JTextField();
        txtDespesasDia.setEditable(false);
        txtDespesasDia.setBackground(new Color(255, 240, 240));
        fieldsPanel.add(txtDespesasDia);
        
        // Margem de Lucro
        fieldsPanel.add(new JLabel("📊 Margem Lucro (%):"));
        txtMargemLucro = new JTextField();
        txtMargemLucro.setEditable(false);
        txtMargemLucro.setBackground(new Color(255, 245, 200));
        fieldsPanel.add(txtMargemLucro);
        
        // Horas de Pico
        fieldsPanel.add(new JLabel("⏰ Horas de Pico:"));
        txtHorasPico = new JTextField();
        txtHorasPico.setEditable(false);
        txtHorasPico.setBackground(new Color(240, 240, 255));
        fieldsPanel.add(txtHorasPico);
        
        // Formas de Pagamento
        fieldsPanel.add(new JLabel("💳 Formas Pagamento:"));
        txtFormasPagamento = new JTextField();
        txtFormasPagamento.setEditable(false);
        txtFormasPagamento.setBackground(new Color(240, 255, 240));
        fieldsPanel.add(txtFormasPagamento);
        
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
        String tipoBusca = (String) cbTipoBusca.getSelectedItem();
        String busca = txtBusca.getText().trim();
        String periodo = (String) cbPeriodo.getSelectedItem();
        String ano = (String) cbAno.getSelectedItem();
        
        SystemLogger.ui("Realizando busca: " + tipoBusca + " - " + busca + " - " + periodo + " - " + ano);
        
        // Atualizar datas
        updateDataRange();
        
        // Implementar lógica de busca específica
        refreshData();
    }
    
    @Override
    protected void refreshData() {
        SystemLogger.ui("[METRICAS] Iniciando atualização de dados...");
        
        try {
            SystemLogger.ui("[METRICAS] Verificando componentes do formulário...");
            
            // Verificar se os componentes estão inicializados
            if (txtVendasDia == null) {
                SystemLogger.error("[METRICAS] txtVendasDia é nulo");
                return;
            }
            if (txtReceitaDia == null) {
                SystemLogger.error("[METRICAS] txtReceitaDia é nulo");
                return;
            }
            
            SystemLogger.ui("[METRICAS] Atualizando campos do formulário com dados de exemplo...");
            
            // Atualizar campos do formulário com dados de exemplo
            txtVendasDia.setText("145");
            txtReceitaDia.setText("R$ 12.545,00");
            txtTicketMedio.setText("R$ 86,52");
            txtClientesAtendidos.setText("89");
            txtProdutosVendidos.setText("312");
            txtDevolucoes.setText("3");
            txtTaxaConversao.setText("12,5");
            txtMetaDiaria.setText("R$ 15.000,00");
            txtPercentualMeta.setText("83,6");
            txtLucroLiquido.setText("R$ 3.125,00");
            txtCustoTotal.setText("R$ 8.420,00");
            txtDespesasDia.setText("R$ 1.000,00");
            txtMargemLucro.setText("24,9");
            txtHorasPico.setText("12:00-14:00");
            txtFormasPagamento.setText("4");
            
            SystemLogger.ui("[METRICAS] Verificando datas para formatação...");
            
            // Verificar se as datas não são nulas antes de formatar
            String periodoInfo = "";
            if (dataInicio != null && dataFim != null) {
                periodoInfo = "\nPeríodo analisado: " + dataInicio.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")) +
                    " a " + dataFim.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
                SystemLogger.ui("[METRICAS] Período formatado: " + periodoInfo);
            } else {
                SystemLogger.ui("[METRICAS] Datas são nulas, usando período padrão");
            }
            
            SystemLogger.ui("[METRICAS] Verificando componente de observações...");
            
            if (txtObservacoes != null) {
                txtObservacoes.setText("Métricas atualizadas em " + 
                    LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss")) +
                    periodoInfo);
                SystemLogger.ui("[METRICAS] Observações atualizadas");
            } else {
                SystemLogger.error("[METRICAS] txtObservacoes é nulo");
            }
            
            SystemLogger.ui("[METRICAS] Métricas do dia atualizadas com sucesso");
            
        } catch (Exception e) {
            SystemLogger.error("[METRICAS] Erro ao atualizar métricas do dia: " + e.getMessage());
            SystemLogger.error("[METRICAS] Stack trace: " + e.toString());
            
            if (workArea != null) {
                JOptionPane.showMessageDialog(workArea, 
                    "Erro ao atualizar métricas: " + e.getMessage(), 
                    "Erro", JOptionPane.ERROR_MESSAGE);
            } else {
                SystemLogger.error("[METRICAS] workArea é nulo, não é possível exibir mensagem de erro");
            }
        }
    }
    
    @Override
    protected void loadSampleData() {
        // Carregar dados de exemplo na tabela
        Object[] sampleData1 = {
            "001", "09/05/2026", "145", "R$ 12.545,00", "R$ 86,52", "89",
            "312", "3", "12,5", "R$ 3.125,00", "R$ 8.420,00", "R$ 1.000,00", "24,9",
            "R$ 15.000,00", "83,6", "12:00-14:00", "4", "João Silva", "Concluído"
        };
        
        Object[] sampleData2 = {
            "002", "08/05/2026", "132", "R$ 11.234,00", "R$ 85,11", "78",
            "298", "5", "11,8", "R$ 2.808,00", "R$ 7.560,00", "R$ 866,00", "25,0",
            "R$ 15.000,00", "74,9", "11:00-13:00", "3", "Maria Santos", "Concluído"
        };
        
        Object[] sampleData3 = {
            "003", "07/05/2026", "156", "R$ 13.890,00", "R$ 89,04", "95",
            "341", "2", "13,2", "R$ 3.473,00", "R$ 9.342,00", "R$ 1.075,00", "25,0",
            "R$ 15.000,00", "92,6", "13:00-15:00", "5", "Pedro Oliveira", "Concluído"
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
            txtVendasDia.setText(tableModel.getValueAt(selectedRow, 2).toString());
            txtReceitaDia.setText(tableModel.getValueAt(selectedRow, 3).toString());
            txtTicketMedio.setText(tableModel.getValueAt(selectedRow, 4).toString());
            txtClientesAtendidos.setText(tableModel.getValueAt(selectedRow, 5).toString());
            txtProdutosVendidos.setText(tableModel.getValueAt(selectedRow, 6).toString());
            txtDevolucoes.setText(tableModel.getValueAt(selectedRow, 7).toString());
            txtTaxaConversao.setText(tableModel.getValueAt(selectedRow, 8).toString());
            txtLucroLiquido.setText(tableModel.getValueAt(selectedRow, 9).toString());
            txtCustoTotal.setText(tableModel.getValueAt(selectedRow, 10).toString());
            txtDespesasDia.setText(tableModel.getValueAt(selectedRow, 11).toString());
            txtMargemLucro.setText(tableModel.getValueAt(selectedRow, 12).toString());
            txtMetaDiaria.setText(tableModel.getValueAt(selectedRow, 13).toString());
            txtPercentualMeta.setText(tableModel.getValueAt(selectedRow, 14).toString());
            txtHorasPico.setText(tableModel.getValueAt(selectedRow, 15).toString());
            txtFormasPagamento.setText(tableModel.getValueAt(selectedRow, 16).toString());
            
            String data = tableModel.getValueAt(selectedRow, 1).toString();
            String responsavel = tableModel.getValueAt(selectedRow, 17).toString();
            
            txtObservacoes.setText("Métricas selecionadas: " + data +
                "\nResponsável: " + responsavel +
                "\nDados carregados da tabela");
        }
    }
    
    @Override
    protected void addNew() {
        JOptionPane.showMessageDialog(workArea, 
            "Funcionalidade de adicionar métrica em desenvolvimento...", 
            "Nova Métrica", JOptionPane.INFORMATION_MESSAGE);
    }
    
    @Override
    protected void editSelected() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow >= 0) {
            JOptionPane.showMessageDialog(workArea, 
                "Funcionalidade de editar métrica em desenvolvimento...", 
                "Editar Métrica", JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(workArea, 
                "Selecione uma métrica para editar", 
                "Aviso", JOptionPane.WARNING_MESSAGE);
        }
    }
    
    @Override
    protected void performExport(java.io.File file) throws Exception {
        // Implementar exportação específica para métricas
        throw new UnsupportedOperationException("Exportação de métricas em desenvolvimento");
    }
    
    @Override
    protected String getFileName() {
        return "metricas_dia_";
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
     * Método estático para criar o formulário de métricas do dia
     * @return JPanel configurado do formulário
     */
    public static JPanel criarFormularioMetricasDia() {
        PDVFormularioMetricasDia metricasDia = new PDVFormularioMetricasDia(
            new JPanel(), "usuario", "Nome Usuario"
        );
        return metricasDia.getMainPanel();
    }
    
    /**
     * Método para criar o formulário de métricas do dia
     * @return JPanel configurado do formulário
     */
    public JPanel criarFormularioMetricasDiaInstance() {
        SystemLogger.ui("[TELA_ATIVA] Métricas do Dia - RETORNANDO instância ativa");
        SystemLogger.ui("[TELA_ATIVA] Métricas do Dia - Status: ATIVO E FUNCIONANDO");
        
        if (mainPanel != null) {
            SystemLogger.ui("[TELA_ATIVA] Métricas do Dia - Painel principal está ATIVO");
        } else {
            SystemLogger.error("[TELA_ATIVA] Métricas do Dia - Painel principal é NULO");
        }
        
        return mainPanel;
    }
}
