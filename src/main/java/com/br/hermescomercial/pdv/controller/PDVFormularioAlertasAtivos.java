package com.br.hermescomercial.pdv.controller;

import com.br.hermescomercial.util.SystemLogger;
import javax.swing.*;
import java.awt.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Formulário especializado de Alertas Ativos
 * Estende BaseFormulario para reaproveitar componentes comuns
 * Segue o padrão Header → Busca → Formulário → Tabela
 * @author Hermes Comercial
 * @version 3.1.0
 */
public class PDVFormularioAlertasAtivos extends BaseFormulario {
    
    // Campos específicos do formulário
    private JTextField txtTotalAlertas;
    private JTextField txtAlertasCriticos;
    private JTextField txtAlertasMedios;
    private JTextField txtAlertasBaixos;
    private JTextField txtAlertasHoje;
    private JTextField txtAlertasPendentes;
    private JTextField txtAlertasResolvidos;
    private JTextField txtAlertasAtrasados;
    private JTextField txtAlertasEstaSemana;
    private JTextField txtTaxaResolucao;
    private JTextField txtTempoMedioResolucao;
    private JTextField txtAlertasPorSetor;
    private JTextArea txtObservacoes;
    
    // Campos de busca específicos
    private JComboBox<String> cbPrioridade;
    private JComboBox<String> cbStatus;
    
    public PDVFormularioAlertasAtivos(JPanel workArea, String usuario, String nome) {
        super(workArea, usuario, nome);
        SystemLogger.ui("[ALERTAS] Construtor chamado - workArea: " + (workArea != null ? "OK" : "NULO") + 
            ", usuario: " + usuario + ", nome: " + nome);
        SystemLogger.ui("[ALERTAS] Construtor finalizado - formulário pronto");
    }
    
    
    @Override
    protected void createFormPanel() {
        formPanel = new JPanel(new BorderLayout());
        formPanel.setBackground(Color.WHITE);
        formPanel.setBorder(BorderFactory.createTitledBorder("📊 Resumo de Alertas"));
        
        // Painel de campos
        JPanel fieldsPanel = new JPanel(new GridLayout(4, 4, 10, 10));
        fieldsPanel.setBackground(Color.WHITE);
        
        // Total de Alertas
        fieldsPanel.add(new JLabel("🔔 Total de Alertas:"));
        txtTotalAlertas = new JTextField();
        txtTotalAlertas.setEditable(false);
        txtTotalAlertas.setBackground(new Color(240, 255, 240));
        fieldsPanel.add(txtTotalAlertas);
        
        // Alertas Críticos
        fieldsPanel.add(new JLabel("🚨 Alertas Críticos:"));
        txtAlertasCriticos = new JTextField();
        txtAlertasCriticos.setEditable(false);
        txtAlertasCriticos.setBackground(new Color(255, 240, 240));
        fieldsPanel.add(txtAlertasCriticos);
        
        // Alertas Médios
        fieldsPanel.add(new JLabel("⚠️ Alertas Médios:"));
        txtAlertasMedios = new JTextField();
        txtAlertasMedios.setEditable(false);
        txtAlertasMedios.setBackground(new Color(255, 255, 240));
        fieldsPanel.add(txtAlertasMedios);
        
        // Alertas Baixos
        fieldsPanel.add(new JLabel("ℹ️ Alertas Baixos:"));
        txtAlertasBaixos = new JTextField();
        txtAlertasBaixos.setEditable(false);
        txtAlertasBaixos.setBackground(new Color(240, 240, 255));
        fieldsPanel.add(txtAlertasBaixos);
        
        // Alertas de Hoje
        fieldsPanel.add(new JLabel("📅 Alertas de Hoje:"));
        txtAlertasHoje = new JTextField();
        txtAlertasHoje.setEditable(false);
        txtAlertasHoje.setBackground(new Color(255, 245, 200));
        fieldsPanel.add(txtAlertasHoje);
        
        // Alertas Pendentes
        fieldsPanel.add(new JLabel("⏳ Alertas Pendentes:"));
        txtAlertasPendentes = new JTextField();
        txtAlertasPendentes.setEditable(false);
        txtAlertasPendentes.setBackground(new Color(255, 220, 220));
        fieldsPanel.add(txtAlertasPendentes);
        
        // Alertas Resolvidos
        fieldsPanel.add(new JLabel("✅ Alertas Resolvidos:"));
        txtAlertasResolvidos = new JTextField();
        txtAlertasResolvidos.setEditable(false);
        txtAlertasResolvidos.setBackground(new Color(220, 255, 220));
        fieldsPanel.add(txtAlertasResolvidos);
        
        // Alertas Atrasados
        fieldsPanel.add(new JLabel("⏰ Alertas Atrasados:"));
        txtAlertasAtrasados = new JTextField();
        txtAlertasAtrasados.setEditable(false);
        txtAlertasAtrasados.setBackground(new Color(255, 200, 200));
        fieldsPanel.add(txtAlertasAtrasados);
        
        // Alertas Esta Semana
        fieldsPanel.add(new JLabel("📅 Alertas Esta Semana:"));
        txtAlertasEstaSemana = new JTextField();
        txtAlertasEstaSemana.setEditable(false);
        txtAlertasEstaSemana.setBackground(new Color(255, 245, 200));
        fieldsPanel.add(txtAlertasEstaSemana);
        
        // Taxa de Resolução
        fieldsPanel.add(new JLabel("📊 Taxa Resolução (%):"));
        txtTaxaResolucao = new JTextField();
        txtTaxaResolucao.setEditable(false);
        txtTaxaResolucao.setBackground(new Color(220, 255, 220));
        fieldsPanel.add(txtTaxaResolucao);
        
        // Tempo Médio de Resolução
        fieldsPanel.add(new JLabel("⏱️ Tempo Médio Resolução:"));
        txtTempoMedioResolucao = new JTextField();
        txtTempoMedioResolucao.setEditable(false);
        txtTempoMedioResolucao.setBackground(new Color(240, 240, 255));
        fieldsPanel.add(txtTempoMedioResolucao);
        
        // Alertas por Setor
        fieldsPanel.add(new JLabel("🏢 Alertas por Setor:"));
        txtAlertasPorSetor = new JTextField();
        txtAlertasPorSetor.setEditable(false);
        txtAlertasPorSetor.setBackground(new Color(240, 255, 240));
        fieldsPanel.add(txtAlertasPorSetor);
        
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
    
    
    // Métodos abstratos implementados da BaseFormulario
    
    @Override
    protected String getFormTitle() {
        return "🔔 Alertas Ativos";
    }
    
    @Override
    protected String getFormSubtitle() {
        return "Gerenciamento de alertas e notificações do sistema";
    }
    
    @Override
    protected String getTableTitle() {
        return "Lista de Alertas Ativos";
    }
    
    @Override
    protected String[] getTableColumns() {
        return new String[]{
            "ID", "Tipo", "Descrição", "Prioridade", "Status", "Data Criação", 
            "Data Limite", "Data Resolução", "Responsável", "Setor", "Impacto", 
            "Tempo Resolução", "Custo Estimado", "Ações Tomadas"
        };
    }
    
    @Override
    protected String[] getTiposBusca() {
        return new String[]{"Todos", "Estoque", "Vendas", "Financeiro", "Sistema"};
    }
    
    @Override
    protected void addAdditionalSearchFields(JPanel fieldsPanel) {
        // Adicionar campos específicos de busca
        fieldsPanel.add(new JLabel("Prioridade:"));
        String[] prioridades = {"Todas", "Crítica", "Média", "Baixa"};
        cbPrioridade = new JComboBox<>(prioridades);
        fieldsPanel.add(cbPrioridade);
        
        fieldsPanel.add(new JLabel("Status:"));
        String[] status = {"Todos", "Ativo", "Resolvido", "Pendente"};
        cbStatus = new JComboBox<>(status);
        fieldsPanel.add(cbStatus);
    }
    
    @Override
    protected void setupAdditionalEvents() {
        // Evento do combo de prioridade
        cbPrioridade.addActionListener(e -> performSearch());
        
        // Evento do combo de status
        cbStatus.addActionListener(e -> performSearch());
    }
    
    @Override
    protected void clearAdditionalFields() {
        cbPrioridade.setSelectedIndex(0);
        cbStatus.setSelectedIndex(0);
    }
    
    @Override
    protected void performSearch() {
        String tipoBusca = (String) cbTipoBusca.getSelectedItem();
        String busca = txtBusca.getText().trim();
        String prioridade = (String) cbPrioridade.getSelectedItem();
        String status = (String) cbStatus.getSelectedItem();
        
        SystemLogger.ui("Realizando busca: " + tipoBusca + " - " + busca + " - " + prioridade + " - " + status);
        
        // Implementar lógica de busca específica
        refreshData();
    }
    
    @Override
    protected void refreshData() {
        SystemLogger.ui("[ALERTAS] Iniciando atualização de dados...");
        
        try {
            SystemLogger.ui("[ALERTAS] Verificando componentes do formulário...");
            
            // Verificar se os componentes estão inicializados
            if (txtTotalAlertas == null) {
                SystemLogger.error("[ALERTAS] txtTotalAlertas é nulo");
                return;
            }
            if (txtAlertasCriticos == null) {
                SystemLogger.error("[ALERTAS] txtAlertasCriticos é nulo");
                return;
            }
            
            SystemLogger.ui("[ALERTAS] Atualizando campos do formulário com dados de exemplo...");
            
            // Atualizar campos do formulário com dados de exemplo
            txtTotalAlertas.setText("4");
            txtAlertasCriticos.setText("2");
            txtAlertasMedios.setText("1");
            txtAlertasBaixos.setText("1");
            txtAlertasHoje.setText("2");
            txtAlertasPendentes.setText("3");
            txtAlertasResolvidos.setText("1");
            txtAlertasAtrasados.setText("1");
            txtAlertasEstaSemana.setText("4");
            txtTaxaResolucao.setText("25,0");
            txtTempoMedioResolucao.setText("48 horas");
            txtAlertasPorSetor.setText("2,7");
            
            SystemLogger.ui("[ALERTAS] Verificando componente de observações...");
            
            if (txtObservacoes != null) {
                txtObservacoes.setText("Alertas atualizados em " + 
                    LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss")));
                SystemLogger.ui("[ALERTAS] Observações atualizadas");
            } else {
                SystemLogger.error("[ALERTAS] txtObservacoes é nulo");
            }
            
            SystemLogger.ui("[ALERTAS] Alertas atualizados com sucesso");
            
        } catch (Exception e) {
            SystemLogger.error("[ALERTAS] Erro ao atualizar alertas: " + e.getMessage());
            SystemLogger.error("[ALERTAS] Stack trace: " + e.toString());
            
            if (workArea != null) {
                JOptionPane.showMessageDialog(workArea, 
                    "Erro ao atualizar alertas: " + e.getMessage(), 
                    "Erro", JOptionPane.ERROR_MESSAGE);
            } else {
                SystemLogger.error("[ALERTAS] workArea é nulo, não é possível exibir mensagem de erro");
            }
        }
    }
    
    @Override
    protected void loadSampleData() {
        // Carregar dados de exemplo na tabela
        Object[] sampleData1 = {
            "001", "Estoque", "Estoque mínimo atingido - Produto XYZ", "Crítica", "Ativo",
            "09/05/2026", "10/05/2026", "-", "João Silva", "Estoque", "Alto",
            "24 horas", "R$ 500,00", "Repor estoque imediatamente"
        };
        
        Object[] sampleData2 = {
            "002", "Vendas", "Meta diária não alcançada - Vendas 15% abaixo do esperado", "Média", "Ativo",
            "09/05/2026", "09/05/2026", "-", "Maria Santos", "Vendas", "Médio",
            "12 horas", "R$ 200,00", "Acompanhar vendas e oferecer suporte"
        };
        
        Object[] sampleData3 = {
            "003", "Financeiro", "Pagamento em atraso - Fornecedor ABC", "Crítica", "Pendente",
            "08/05/2026", "12/05/2026", "-", "Pedro Oliveira", "Financeiro", "Crítico",
            "48 horas", "R$ 1.200,00", "Contatar fornecedor e negociar prazo"
        };
        
        Object[] sampleData4 = {
            "004", "Sistema", "Backup não realizado - Risco de perda de dados", "Baixa", "Resolvido",
            "08/05/2026", "09/05/2026", "09/05/2026 14:30", "Carlos Silva", "TI", "Baixo",
            "4 horas", "R$ 50,00", "Backup realizado e sistema verificado"
        };
        
        Object[] sampleData5 = {
            "005", "Clientes", "Reclamação frequente - Produto com defeito", "Média", "Ativo",
            "09/05/2026", "11/05/2026", "-", "Ana Costa", "Atendimento", "Médio",
            "36 horas", "R$ 150,00", "Trocar produto e oferecer desconto"
        };
        
        tableModel.addRow(sampleData1);
        tableModel.addRow(sampleData2);
        tableModel.addRow(sampleData3);
        tableModel.addRow(sampleData4);
        tableModel.addRow(sampleData5);
    }
    
    @Override
    protected void loadSelectedData() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow >= 0) {
            // Carregar dados da linha selecionada para observações
            String descricao = tableModel.getValueAt(selectedRow, 2).toString();
            String responsavel = tableModel.getValueAt(selectedRow, 8).toString();
            String setor = tableModel.getValueAt(selectedRow, 9).toString();
            
            txtObservacoes.setText("Alerta selecionado: " + descricao + 
                "\nResponsável: " + responsavel + 
                "\nSetor: " + setor);
        }
    }
    
    @Override
    protected void addNew() {
        JOptionPane.showMessageDialog(workArea, 
            "Funcionalidade de adicionar alerta em desenvolvimento...", 
            "Novo Alerta", JOptionPane.INFORMATION_MESSAGE);
    }
    
    @Override
    protected void editSelected() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow >= 0) {
            JOptionPane.showMessageDialog(workArea, 
                "Funcionalidade de editar alerta em desenvolvimento...", 
                "Editar Alerta", JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(workArea, 
                "Selecione um alerta para editar", 
                "Aviso", JOptionPane.WARNING_MESSAGE);
        }
    }
    
    @Override
    protected void performExport(java.io.File file) throws Exception {
        // Implementar exportação específica para alertas
        throw new UnsupportedOperationException("Exportação de alertas em desenvolvimento");
    }
    
    @Override
    protected String getFileName() {
        return "alertas_ativos_";
    }
    
    /**
     * Método estático para criar o formulário de alertas ativos
     * @return JPanel configurado do formulário
     */
    public static JPanel criarFormularioAlertasAtivos() {
        PDVFormularioAlertasAtivos alertasAtivos = new PDVFormularioAlertasAtivos(
            new JPanel(), "usuario", "Nome Usuario"
        );
        return alertasAtivos.getMainPanel();
    }
    
    /**
     * Método para criar o formulário de alertas ativos
     * @return JPanel configurado do formulário
     */
    public JPanel criarFormularioAlertasAtivosInstance() {
        SystemLogger.ui("[TELA_ATIVA] Alertas Ativos - RETORNANDO instância ativa");
        SystemLogger.ui("[TELA_ATIVA] Alertas Ativos - Status: ATIVO E FUNCIONANDO");
        
        if (mainPanel != null) {
            SystemLogger.ui("[TELA_ATIVA] Alertas Ativos - Painel principal está ATIVO");
        } else {
            SystemLogger.error("[TELA_ATIVA] Alertas Ativos - Painel principal é NULO");
        }
        
        return mainPanel;
    }
}
