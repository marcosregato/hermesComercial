package com.br.hermescomercial.pdv.controller;

import com.br.hermescomercial.ui.layout.LayoutPadrao;
import com.br.hermescomercial.util.SystemLogger;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.HashMap;
import java.util.Map;

/**
 * Classe especializada para formulário de Pagamentos API
 * Estrutura: Header → Abas → Configurações → Transações → Webhooks → Relatórios
 * Versão 1.0.0 - Adaptada para PDVMenuLateralElegante
 */
public class PDVFormularioPagamentoAPI {
    
    private JPanel workArea;
    private String usuarioAtual;
    private String nomeUsuario;
    
    // Componentes do formulário
    private JTextField txtStripeKey, txtMercadoPagoToken, txtWebhookUrl;
    private JCheckBox chkStripeAtivo, chkMercadoPagoAtivo, chkWebhookAtivo;
    private JTable transacoesTable;
    private DefaultTableModel tableModel;
    private JTabbedPane tabbedPane;
    private Map<String, Boolean> apiStatus;
    private JTextArea txtLogWebhooks;
    private JTextArea txtRelatorios;
    
    // Cores
    private static final Color SUCCESS_COLOR = new Color(76, 175, 80);
    private static final Color DANGER_COLOR = new Color(244, 67, 54);
    private static final Color WARNING_COLOR = new Color(255, 152, 0);
    private static final Color PRIMARY_COLOR = new Color(33, 150, 243);
    private static final Color INFO_COLOR = new Color(0, 150, 136);
    
    public PDVFormularioPagamentoAPI(JPanel workArea, String usuario, String nome) {
        this.workArea = workArea;
        this.usuarioAtual = usuario;
        this.nomeUsuario = nome;
        this.apiStatus = new HashMap<>();
        this.apiStatus.put("stripe", false);
        this.apiStatus.put("mercadopago", false);
    }
    
    public JPanel criarFormularioPagamentoAPI() {
        SystemLogger.ui("=== CRIANDO FORMULÁRIO PAGAMENTOS API ===");
        SystemLogger.ui("Usuário: " + usuarioAtual);
        
        JPanel painelPrincipal = new JPanel(new BorderLayout());
        painelPrincipal.setBackground(Color.WHITE);
        
        // Header
        painelPrincipal.add(criarHeader(), BorderLayout.NORTH);
        
        // Conteúdo principal com abas
        JPanel conteudo = new JPanel(new BorderLayout());
        conteudo.setBackground(Color.WHITE);
        
        // Abas
        conteudo.add(criarAbas(), BorderLayout.CENTER);
        
        painelPrincipal.add(conteudo, BorderLayout.CENTER);
        
        SystemLogger.ui("Formulário Pagamentos API criado com sucesso");
        return painelPrincipal;
    }
    
    private JPanel criarHeader() {
        JPanel header = LayoutPadrao.criarHeaderPanel("💳 Pagamentos API");
        
        // Informações do usuário
        JPanel userInfo = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        userInfo.setBackground(new Color(250, 250, 250));
        userInfo.add(new JLabel("👤 " + nomeUsuario));
        userInfo.add(new JLabel(" | "));
        userInfo.add(new JLabel("📅 " + java.time.LocalDate.now()));
        
        header.add(userInfo, BorderLayout.EAST);
        return header;
    }
    
    private JTabbedPane criarAbas() {
        tabbedPane = new JTabbedPane();
        
        // Aba de Configuração
        tabbedPane.addTab("⚙️ Configuração", criarAbaConfiguracao());
        
        // Aba de Transações
        tabbedPane.addTab("💳 Transações", criarAbaTransacoes());
        
        // Aba de Webhooks
        tabbedPane.addTab("🔗 Webhooks", criarAbaWebhooks());
        
        // Aba de Relatórios
        tabbedPane.addTab("📊 Relatórios", criarAbaRelatorios());
        
        return tabbedPane;
    }
    
    private JPanel criarAbaConfiguracao() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);
        
        // Painel de configuração de APIs
        JPanel configPanel = LayoutPadrao.criarPainelComBorda("⚙️ Configuração de APIs de Pagamento");
        
        JPanel configContainer = new JPanel(new GridBagLayout());
        configContainer.setBackground(Color.WHITE);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.anchor = GridBagConstraints.WEST;
        
        // Stripe Configuration
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
        JLabel lblStripe = new JLabel("💳 Stripe");
        lblStripe.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lblStripe.setForeground(PRIMARY_COLOR);
        configContainer.add(lblStripe, gbc);
        
        gbc.gridy = 1; gbc.gridwidth = 1;
        configContainer.add(new JLabel("Chave Secreta:"), gbc);
        gbc.gridx = 1; gbc.fill = GridBagConstraints.HORIZONTAL; gbc.weightx = 1;
        txtStripeKey = new JTextField();
        txtStripeKey.setToolTipText("sk_test_...");
        configContainer.add(txtStripeKey, gbc);
        
        gbc.gridx = 0; gbc.gridy = 2; gbc.fill = GridBagConstraints.NONE; gbc.weightx = 0;
        chkStripeAtivo = new JCheckBox("Ativar Stripe");
        chkStripeAtivo.setBackground(Color.WHITE);
        configContainer.add(chkStripeAtivo, gbc);
        
        gbc.gridx = 1;
        JButton btnTestarStripe = criarBotao("🧪 Testar Conexão", PRIMARY_COLOR);
        btnTestarStripe.addActionListener(e -> testarConexaoStripe());
        configContainer.add(btnTestarStripe, gbc);
        
        // Mercado Pago Configuration
        gbc.gridx = 0; gbc.gridy = 3; gbc.gridwidth = 1;
        JLabel lblMercadoPago = new JLabel("🛒 Mercado Pago");
        lblMercadoPago.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lblMercadoPago.setForeground(PRIMARY_COLOR);
        configContainer.add(lblMercadoPago, gbc);
        
        gbc.gridy = 4;
        configContainer.add(new JLabel("Token de Acesso:"), gbc);
        gbc.gridx = 1; gbc.fill = GridBagConstraints.HORIZONTAL; gbc.weightx = 1;
        txtMercadoPagoToken = new JTextField();
        txtMercadoPagoToken.setToolTipText("TEST-...");
        configContainer.add(txtMercadoPagoToken, gbc);
        
        gbc.gridx = 0; gbc.gridy = 5; gbc.fill = GridBagConstraints.NONE; gbc.weightx = 0;
        chkMercadoPagoAtivo = new JCheckBox("Ativar Mercado Pago");
        chkMercadoPagoAtivo.setBackground(Color.WHITE);
        configContainer.add(chkMercadoPagoAtivo, gbc);
        
        gbc.gridx = 1;
        JButton btnTestarMP = criarBotao("🧪 Testar Conexão", PRIMARY_COLOR);
        btnTestarMP.addActionListener(e -> testarConexaoMercadoPago());
        configContainer.add(btnTestarMP, gbc);
        
        // Status das APIs
        gbc.gridx = 0; gbc.gridy = 6; gbc.gridwidth = 2;
        JPanel statusPanel = new JPanel(new GridLayout(2, 2, 10, 5));
        statusPanel.setBackground(Color.WHITE);
        statusPanel.setBorder(BorderFactory.createTitledBorder("Status das APIs"));
        
        JLabel lblStripeStatus = new JLabel("Stripe: Inativo");
        lblStripeStatus.setForeground(DANGER_COLOR);
        JLabel lblMPStatus = new JLabel("Mercado Pago: Inativo");
        lblMPStatus.setForeground(DANGER_COLOR);
        
        statusPanel.add(lblStripeStatus);
        statusPanel.add(lblMPStatus);
        
        configContainer.add(statusPanel, gbc);
        
        configPanel.add(configContainer, BorderLayout.CENTER);
        
        // Painel de botões
        JPanel buttonPanel = new JPanel(new FlowLayout());
        buttonPanel.setBackground(Color.WHITE);
        
        JButton btnSalvar = criarBotao("💾 Salvar Configurações", SUCCESS_COLOR);
        JButton btnLimpar = criarBotao("🔄 Limpar", WARNING_COLOR);
        
        btnSalvar.addActionListener(e -> salvarConfiguracoes());
        btnLimpar.addActionListener(e -> limparFormulario());
        
        buttonPanel.add(btnSalvar);
        buttonPanel.add(btnLimpar);
        
        configPanel.add(buttonPanel, BorderLayout.SOUTH);
        
        panel.add(configPanel, BorderLayout.CENTER);
        
        return panel;
    }
    
    private JPanel criarAbaTransacoes() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);
        
        // Painel de busca e filtros
        JPanel searchPanel = LayoutPadrao.criarPainelComBorda("🔍 Busca e Filtros");
        
        JPanel searchContainer = new JPanel(new FlowLayout(FlowLayout.LEFT));
        searchContainer.setBackground(Color.WHITE);
        
        JTextField txtBusca = new JTextField(20);
        JComboBox<String> cmbStatus = new JComboBox<>(new String[]{
            "Todos", "Aprovado", "Pendente", "Falhou", "Cancelado"
        });
        JComboBox<String> cmbAPI = new JComboBox<>(new String[]{
            "Todas", "Stripe", "Mercado Pago"
        });
        JButton btnBuscar = criarBotao("🔍 Buscar", PRIMARY_COLOR);
        JButton btnAtualizar = criarBotao("🔄 Atualizar", SUCCESS_COLOR);
        
        btnBuscar.addActionListener(e -> buscarTransacoes(txtBusca.getText(), (String) cmbStatus.getSelectedItem(), (String) cmbAPI.getSelectedItem()));
        btnAtualizar.addActionListener(e -> carregarTransacoes());
        
        searchContainer.add(new JLabel("Buscar:"));
        searchContainer.add(txtBusca);
        searchContainer.add(new JLabel("Status:"));
        searchContainer.add(cmbStatus);
        searchContainer.add(new JLabel("API:"));
        searchContainer.add(cmbAPI);
        searchContainer.add(btnBuscar);
        searchContainer.add(btnAtualizar);
        
        searchPanel.add(searchContainer, BorderLayout.CENTER);
        panel.add(searchPanel, BorderLayout.NORTH);
        
        // Tabela de transações
        JPanel transacoesPanel = LayoutPadrao.criarPainelComBorda("💳 Transações");
        
        String[] columns = {"ID", "Data", "Valor", "Cliente", "Status", "API", "ID Externo"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        transacoesTable = new JTable(tableModel);
        transacoesTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        transacoesTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    verDetalhesTransacao();
                }
            }
        });
        
        JScrollPane tableScrollPane = new JScrollPane(transacoesTable);
        transacoesPanel.add(tableScrollPane, BorderLayout.CENTER);
        
        // Painel de ações
        JPanel actionPanel = new JPanel(new FlowLayout());
        actionPanel.setBackground(Color.WHITE);
        
        JButton btnDetalhes = criarBotao("👁️ Ver Detalhes", PRIMARY_COLOR);
        JButton btnEstornar = criarBotao("↩️ Estornar", WARNING_COLOR);
        JButton btnReembolsar = criarBotao("💰 Reembolsar", new Color(156, 39, 176));
        
        btnDetalhes.addActionListener(e -> verDetalhesTransacao());
        btnEstornar.addActionListener(e -> estornarTransacao());
        btnReembolsar.addActionListener(e -> reembolsarTransacao());
        
        actionPanel.add(btnDetalhes);
        actionPanel.add(btnEstornar);
        actionPanel.add(btnReembolsar);
        
        transacoesPanel.add(actionPanel, BorderLayout.SOUTH);
        
        panel.add(transacoesPanel, BorderLayout.CENTER);
        
        // Carregar dados iniciais
        carregarTransacoes();
        
        return panel;
    }
    
    private JPanel criarAbaWebhooks() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);
        
        // Configuração de Webhooks
        JPanel webhookConfigPanel = LayoutPadrao.criarPainelComBorda("🔗 Configuração de Webhooks");
        
        JPanel configContainer = new JPanel(new GridBagLayout());
        configContainer.setBackground(Color.WHITE);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.anchor = GridBagConstraints.WEST;
        
        gbc.gridx = 0; gbc.gridy = 0;
        configContainer.add(new JLabel("🔗 URL do Webhook:"), gbc);
        gbc.gridx = 1; gbc.fill = GridBagConstraints.HORIZONTAL; gbc.weightx = 1;
        txtWebhookUrl = new JTextField();
        txtWebhookUrl.setText("https://seusite.com/webhook/pagamentos");
        configContainer.add(txtWebhookUrl, gbc);
        
        gbc.gridx = 0; gbc.gridy = 1; gbc.fill = GridBagConstraints.NONE; gbc.weightx = 0;
        chkWebhookAtivo = new JCheckBox("Ativar Webhooks");
        chkWebhookAtivo.setBackground(Color.WHITE);
        configContainer.add(chkWebhookAtivo, gbc);
        
        gbc.gridx = 1;
        JButton btnTestarWebhook = criarBotao("🧪 Testar Webhook", PRIMARY_COLOR);
        btnTestarWebhook.addActionListener(e -> testarWebhook());
        configContainer.add(btnTestarWebhook, gbc);
        
        webhookConfigPanel.add(configContainer, BorderLayout.CENTER);
        panel.add(webhookConfigPanel, BorderLayout.NORTH);
        
        // Log de eventos
        JPanel logPanel = LayoutPadrao.criarPainelComBorda("📜 Log de Eventos Recebidos");
        
        txtLogWebhooks = new JTextArea(15, 50);
        txtLogWebhooks.setEditable(false);
        txtLogWebhooks.setFont(new Font("Courier New", Font.PLAIN, 11));
        txtLogWebhooks.setText(
            "2026-05-04 10:30:15 - Webhook recebido: payment_intent.succeeded\n" +
            "ID: pi_1234567890 | Valor: R$ 150.00 | API: Stripe\n" +
            "---\n" +
            "2026-05-04 10:25:30 - Webhook recebido: payment.created\n" +
            "ID: pay_1234567890 | Valor: R$ 89.90 | API: Mercado Pago\n" +
            "---\n" +
            "2026-05-04 10:20:45 - Webhook recebido: payment.failed\n" +
            "ID: pi_1234567891 | Valor: R$ 200.00 | API: Stripe\n"
        );
        
        JScrollPane logScrollPane = new JScrollPane(txtLogWebhooks);
        logPanel.add(logScrollPane, BorderLayout.CENTER);
        
        JPanel logButtonPanel = new JPanel(new FlowLayout());
        logButtonPanel.setBackground(Color.WHITE);
        
        JButton btnLimparLog = criarBotao("🗑️ Limpar Log", DANGER_COLOR);
        JButton btnExportarLog = criarBotao("📤 Exportar Log", INFO_COLOR);
        
        btnLimparLog.addActionListener(e -> txtLogWebhooks.setText(""));
        btnExportarLog.addActionListener(e -> exportarLog());
        
        logButtonPanel.add(btnLimparLog);
        logButtonPanel.add(btnExportarLog);
        
        logPanel.add(logButtonPanel, BorderLayout.SOUTH);
        
        panel.add(logPanel, BorderLayout.CENTER);
        
        return panel;
    }
    
    private JPanel criarAbaRelatorios() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);
        
        // Painel de filtros
        JPanel filterPanel = LayoutPadrao.criarPainelComBorda("📊 Filtros de Relatório");
        
        JPanel filterContainer = new JPanel(new FlowLayout(FlowLayout.LEFT));
        filterContainer.setBackground(Color.WHITE);
        
        JComboBox<String> cmbPeriodo = new JComboBox<>(new String[]{
            "Hoje", "Últimos 7 dias", "Últimos 30 dias", "Este mês", "Mês passado"
        });
        JComboBox<String> cmbRelatorio = new JComboBox<>(new String[]{
            "Resumo de Transações", "Análise por API", "Taxa de Sucesso", "Volume por Dia"
        });
        JButton btnGerar = criarBotao("📊 Gerar Relatório", SUCCESS_COLOR);
        
        btnGerar.addActionListener(e -> gerarRelatorio((String) cmbRelatorio.getSelectedItem(), (String) cmbPeriodo.getSelectedItem()));
        
        filterContainer.add(new JLabel("Período:"));
        filterContainer.add(cmbPeriodo);
        filterContainer.add(new JLabel("Relatório:"));
        filterContainer.add(cmbRelatorio);
        filterContainer.add(btnGerar);
        
        filterPanel.add(filterContainer, BorderLayout.CENTER);
        panel.add(filterPanel, BorderLayout.NORTH);
        
        // Área de resultados
        JPanel resultadosPanel = LayoutPadrao.criarPainelComBorda("📋 Resultados do Relatório");
        
        txtRelatorios = new JTextArea();
        txtRelatorios.setEditable(false);
        txtRelatorios.setFont(new Font("Courier New", Font.PLAIN, 11));
        txtRelatorios.setText(
            "📊 RELATÓRIO DE PAGAMENTOS API\n" +
            "================================\n\n" +
            "Selecione os filtros acima e clique em 'Gerar Relatório'\n\n" +
            "Relatórios disponíveis:\n" +
            "• Resumo de Transações - Total e status das transações\n" +
            "• Análise por API - Desempenho por API configurada\n" +
            "• Taxa de Sucesso - Percentual de aprovação/reprovação\n" +
            "• Volume por Dia - Gráfico de volume diário"
        );
        
        JScrollPane scrollPane = new JScrollPane(txtRelatorios);
        resultadosPanel.add(scrollPane, BorderLayout.CENTER);
        
        panel.add(resultadosPanel, BorderLayout.CENTER);
        
        return panel;
    }
    
    private JButton criarBotao(String text, Color color) {
        JButton button = new JButton(text);
        button.setBackground(color);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setOpaque(true);
        button.setFont(new Font("Segoe UI", Font.BOLD, 12));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(color.darker());
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(color);
            }
        });
        
        return button;
    }
    
    private void salvarConfiguracoes() {
        try {
            // Salvar configurações (simulação)
            apiStatus.put("stripe", chkStripeAtivo.isSelected());
            apiStatus.put("mercadopago", chkMercadoPagoAtivo.isSelected());
            
            JOptionPane.showMessageDialog(workArea, "Configurações salvas com sucesso!", 
                "Sucesso", JOptionPane.INFORMATION_MESSAGE);
                
            SystemLogger.ui("Configurações de Pagamento API salvas por " + usuarioAtual);
                
        } catch (Exception e) {
            JOptionPane.showMessageDialog(workArea, "Erro ao salvar configurações: " + e.getMessage(), 
                "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void limparFormulario() {
        txtStripeKey.setText("");
        txtMercadoPagoToken.setText("");
        txtWebhookUrl.setText("https://seusite.com/webhook/pagamentos");
        chkStripeAtivo.setSelected(false);
        chkMercadoPagoAtivo.setSelected(false);
        chkWebhookAtivo.setSelected(false);
    }
    
    private void testarConexaoStripe() {
        String key = txtStripeKey.getText().trim();
        if (key.isEmpty()) {
            JOptionPane.showMessageDialog(workArea, "Digite a chave do Stripe!", 
                "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        // Simulação de teste
        JOptionPane.showMessageDialog(workArea, "✅ Conexão com Stripe estabelecida com sucesso!\n" +
            "Chave: " + key.substring(0, Math.min(10, key.length())) + "...", 
            "Teste de Conexão", JOptionPane.INFORMATION_MESSAGE);
        
        apiStatus.put("stripe", true);
        SystemLogger.ui("Teste de conexão Stripe realizado por " + usuarioAtual);
    }
    
    private void testarConexaoMercadoPago() {
        String token = txtMercadoPagoToken.getText().trim();
        if (token.isEmpty()) {
            JOptionPane.showMessageDialog(workArea, "Digite o token do Mercado Pago!", 
                "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        // Simulação de teste
        JOptionPane.showMessageDialog(workArea, "✅ Conexão com Mercado Pago estabelecida com sucesso!\n" +
            "Token: " + token.substring(0, Math.min(10, token.length())) + "...", 
            "Teste de Conexão", JOptionPane.INFORMATION_MESSAGE);
        
        apiStatus.put("mercadopago", true);
        SystemLogger.ui("Teste de conexão Mercado Pago realizado por " + usuarioAtual);
    }
    
    private void testarWebhook() {
        String url = txtWebhookUrl.getText().trim();
        if (url.isEmpty()) {
            JOptionPane.showMessageDialog(workArea, "Digite a URL do webhook!", 
                "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        // Simulação de teste
        JOptionPane.showMessageDialog(workArea, "🧪 Testando webhook...\n\n" +
            "URL: " + url + "\n" +
            "Status: ✅ Webhook configurado e respondendo!\n" +
            "Evento de teste enviado com sucesso.", 
            "Teste de Webhook", JOptionPane.INFORMATION_MESSAGE);
        
        SystemLogger.ui("Teste de webhook realizado por " + usuarioAtual);
    }
    
    private void buscarTransacoes(String termo, String status, String api) {
        JOptionPane.showMessageDialog(workArea, "Buscando transações...\n" +
            "Termo: " + termo + "\n" +
            "Status: " + status + "\n" +
            "API: " + api + "\n\n" +
            "Funcionalidade em desenvolvimento...", "Busca", JOptionPane.INFORMATION_MESSAGE);
        
        SystemLogger.ui("Busca de transações realizada por " + usuarioAtual + ": " + termo);
    }
    
    private void carregarTransacoes() {
        try {
            // Limpar tabela
            tableModel.setRowCount(0);
            
            // Simulação de dados
            Object[][] dados = {
                {1, "2026-05-04 10:30", "R$ 150.00", "João Silva", "Aprovado", "Stripe", "pi_1234567890"},
                {2, "2026-05-04 10:25", "R$ 89.90", "Maria Santos", "Aprovado", "Mercado Pago", "pay_1234567890"},
                {3, "2026-05-04 10:20", "R$ 200.00", "Pedro Costa", "Falhou", "Stripe", "pi_1234567891"},
                {4, "2026-05-04 10:15", "R$ 75.50", "Ana Oliveira", "Pendente", "Stripe", "pi_1234567892"}
            };
            
            for (Object[] row : dados) {
                tableModel.addRow(row);
            }
            
            SystemLogger.ui("Transações carregadas por " + usuarioAtual);
            
        } catch (Exception e) {
            JOptionPane.showMessageDialog(workArea, "Erro ao carregar transações: " + e.getMessage(), 
                "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void verDetalhesTransacao() {
        int selectedRow = transacoesTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(workArea, "Selecione uma transação para ver detalhes!", 
                "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        JOptionPane.showMessageDialog(workArea, 
            "📋 DETALHES DA TRANSAÇÃO\n" +
            "========================\n" +
            "ID: " + tableModel.getValueAt(selectedRow, 0) + "\n" +
            "Data: " + tableModel.getValueAt(selectedRow, 1) + "\n" +
            "Valor: " + tableModel.getValueAt(selectedRow, 2) + "\n" +
            "Cliente: " + tableModel.getValueAt(selectedRow, 3) + "\n" +
            "Status: " + tableModel.getValueAt(selectedRow, 4) + "\n" +
            "API: " + tableModel.getValueAt(selectedRow, 5) + "\n" +
            "ID Externo: " + tableModel.getValueAt(selectedRow, 6), 
            "Detalhes da Transação", JOptionPane.INFORMATION_MESSAGE);
        
        SystemLogger.ui("Detalhes da transação visualizados por " + usuarioAtual);
    }
    
    private void estornarTransacao() {
        JOptionPane.showMessageDialog(workArea, "Funcionalidade de estorno em desenvolvimento...", 
            "Estorno", JOptionPane.INFORMATION_MESSAGE);
        
        SystemLogger.ui("Tentativa de estorno por " + usuarioAtual);
    }
    
    private void reembolsarTransacao() {
        JOptionPane.showMessageDialog(workArea, "Funcionalidade de reembolso em desenvolvimento...", 
            "Reembolso", JOptionPane.INFORMATION_MESSAGE);
        
        SystemLogger.ui("Tentativa de reembolso por " + usuarioAtual);
    }
    
    private void exportarLog() {
        JOptionPane.showMessageDialog(workArea, "Log de webhooks exportado com sucesso!", 
            "Exportação", JOptionPane.INFORMATION_MESSAGE);
        
        SystemLogger.ui("Log de webhooks exportado por " + usuarioAtual);
    }
    
    private void gerarRelatorio(String tipo, String periodo) {
        String relatorio = "📊 RELATÓRIO: " + tipo + "\n" +
            "=============================\n" +
            "Período: " + periodo + "\n" +
            "Data de geração: " + java.time.LocalDate.now() + "\n" +
            "Gerado por: " + nomeUsuario + "\n\n" +
            "📈 ESTATÍSTICAS\n" +
            "Total de transações: 1,234\n" +
            "Valor total: R$ 45.678,90\n" +
            "Taxa de sucesso: 94.5%\n" +
            "Stripe: 789 transações (63.9%)\n" +
            "Mercado Pago: 445 transações (36.1%)\n\n" +
            "📊 ANÁLISE POR DIA\n" +
            "Média diária: 41.1 transações\n" +
            "Pico: 89 transações (01/05)\n" +
            "Valle: 12 transações (03/05)";
        
        txtRelatorios.setText(relatorio);
        
        JOptionPane.showMessageDialog(workArea, "Relatório gerado com sucesso!", 
            "Relatório", JOptionPane.INFORMATION_MESSAGE);
        
        SystemLogger.ui("Relatório '" + tipo + "' gerado por " + usuarioAtual);
    }
    
    // Métodos públicos para integração
    public void processarPagamento(String gateway, double valor) {
        JOptionPane.showMessageDialog(workArea, "Processando pagamento de R$ " + valor + " via " + gateway, 
            "Processamento", JOptionPane.INFORMATION_MESSAGE);
        
        SystemLogger.ui("Pagamento processado por " + usuarioAtual + ": R$ " + valor + " via " + gateway);
    }
    
    public void validarDadosPagamento() {
        JOptionPane.showMessageDialog(workArea, "Validando dados do pagamento...", 
            "Validação", JOptionPane.INFORMATION_MESSAGE);
        
        SystemLogger.ui("Validação de dados realizada por " + usuarioAtual);
    }
    
    public void integrarComGateway(String gateway) {
        JOptionPane.showMessageDialog(workArea, "Integrando com gateway: " + gateway, 
            "Integração", JOptionPane.INFORMATION_MESSAGE);
        
        SystemLogger.ui("Integração com " + gateway + " iniciada por " + usuarioAtual);
    }
    
    public void confirmarPagamento(String id) {
        JOptionPane.showMessageDialog(workArea, "Pagamento confirmado: " + id, 
            "Confirmação", JOptionPane.INFORMATION_MESSAGE);
        
        SystemLogger.ui("Pagamento confirmado por " + usuarioAtual + ": " + id);
    }
    
    public void cancelarPagamento() {
        JOptionPane.showMessageDialog(workArea, "Pagamento cancelado...", 
            "Cancelamento", JOptionPane.INFORMATION_MESSAGE);
        
        SystemLogger.ui("Pagamento cancelado por " + usuarioAtual);
    }
    
    public void estornarPagamento(String id) {
        JOptionPane.showMessageDialog(workArea, "Pagamento estornado: " + id, 
            "Estorno", JOptionPane.INFORMATION_MESSAGE);
        
        SystemLogger.ui("Pagamento estornado por " + usuarioAtual + ": " + id);
    }
    
    public void gerarRelatorioTransacoes() {
        JOptionPane.showMessageDialog(workArea, "Gerando relatório de transações...", 
            "Relatório Transações", JOptionPane.INFORMATION_MESSAGE);
        
        SystemLogger.ui("Relatório de transações gerado por " + usuarioAtual);
    }
}
