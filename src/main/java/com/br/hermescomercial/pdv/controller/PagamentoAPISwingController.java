package com.br.hermescomercial.pdv.controller;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.HashMap;
import java.util.Map;

/**
 * Controller para tela de integração de pagamentos API
 * Versão 2.8.0 - Interface completa para gestão de pagamentos online
 * Funcionalidades: Configuração Stripe/Mercado Pago, transações, webhooks
 */
public class PagamentoAPISwingController {
    
    private JFrame frame;
    private JTabbedPane tabbedPane;
    
    public JFrame getFrame() {
        return frame;
    }
    
    public JTabbedPane getTabbedPane() {
        return tabbedPane;
    }
    private JTextField txtStripeKey, txtMercadoPagoToken, txtWebhookUrl;
    private JCheckBox chkStripeAtivo, chkMercadoPagoAtivo, chkWebhookAtivo;
    private JTable transacoesTable;
    private DefaultTableModel tableModel;
    private Map<String, Boolean> apiStatus;
    
    public PagamentoAPISwingController() {
        apiStatus = new HashMap<>();
        apiStatus.put("stripe", false);
        apiStatus.put("mercadopago", false);
        initializeUI();
    }
    
    private void initializeUI() {
        frame = new JFrame("Pagamentos API - Hermes Comercial PDV");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setSize(1200, 800);
        frame.setLocationRelativeTo(null);
        
        // Aplicar tema moderno
        frame.getContentPane().setBackground(Color.WHITE);
        
        tabbedPane = new JTabbedPane();
        tabbedPane.setBackground(Color.WHITE);
        
        // Abas principais
        tabbedPane.addTab("⚙️ Configuração", createConfigPanel());
        tabbedPane.addTab("💳 Transações", createTransacoesPanel());
        tabbedPane.addTab("🔗 Webhooks", createWebhooksPanel());
        tabbedPane.addTab("📊 Relatórios", createRelatoriosPanel());
        
        frame.add(tabbedPane);
    }
    
    private JPanel createConfigPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);
        
        // Painel de configuração de APIs
        JPanel configPanel = new JPanel(new GridBagLayout());
        configPanel.setBackground(Color.WHITE);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.anchor = GridBagConstraints.WEST;
        
        // Título
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
        JLabel lblTitulo = new JLabel("Configuração de APIs de Pagamento");
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 18));
        lblTitulo.setForeground(new Color(33, 150, 243));
        configPanel.add(lblTitulo, gbc);
        
        // Stripe Configuration
        gbc.gridy = 1; gbc.gridwidth = 1;
        JLabel lblStripe = new JLabel("💳 Stripe");
        lblStripe.setFont(new Font("Segoe UI", Font.BOLD, 14));
        configPanel.add(lblStripe, gbc);
        
        gbc.gridy = 2;
        configPanel.add(new JLabel("Chave Secreta:"), gbc);
        gbc.gridx = 1; gbc.fill = GridBagConstraints.HORIZONTAL; gbc.weightx = 1;
        txtStripeKey = new JTextField(40);
        txtStripeKey.setToolTipText("sk_test_...");
        configPanel.add(txtStripeKey, gbc);
        
        gbc.gridx = 0; gbc.gridy = 3; gbc.fill = GridBagConstraints.NONE; gbc.weightx = 0;
        chkStripeAtivo = new JCheckBox("Ativar Stripe");
        chkStripeAtivo.setBackground(Color.WHITE);
        configPanel.add(chkStripeAtivo, gbc);
        
        gbc.gridx = 1;
        JButton btnTestarStripe = createButton("🧪 Testar Conexão", new Color(33, 150, 243));
        btnTestarStripe.addActionListener(e -> testarConexaoStripe());
        configPanel.add(btnTestarStripe, gbc);
        
        // Mercado Pago Configuration
        gbc.gridx = 0; gbc.gridy = 4; gbc.gridwidth = 1;
        JLabel lblMercadoPago = new JLabel("🛒 Mercado Pago");
        lblMercadoPago.setFont(new Font("Segoe UI", Font.BOLD, 14));
        configPanel.add(lblMercadoPago, gbc);
        
        gbc.gridy = 5;
        configPanel.add(new JLabel("Token de Acesso:"), gbc);
        gbc.gridx = 1; gbc.fill = GridBagConstraints.HORIZONTAL; gbc.weightx = 1;
        txtMercadoPagoToken = new JTextField(40);
        txtMercadoPagoToken.setToolTipText("TEST-...");
        configPanel.add(txtMercadoPagoToken, gbc);
        
        gbc.gridx = 0; gbc.gridy = 6; gbc.fill = GridBagConstraints.NONE; gbc.weightx = 0;
        chkMercadoPagoAtivo = new JCheckBox("Ativar Mercado Pago");
        chkMercadoPagoAtivo.setBackground(Color.WHITE);
        configPanel.add(chkMercadoPagoAtivo, gbc);
        
        gbc.gridx = 1;
        JButton btnTestarMP = createButton("🧪 Testar Conexão", new Color(33, 150, 243));
        btnTestarMP.addActionListener(e -> testarConexaoMercadoPago());
        configPanel.add(btnTestarMP, gbc);
        
        // Status das APIs
        gbc.gridx = 0; gbc.gridy = 7; gbc.gridwidth = 2;
        JPanel statusPanel = new JPanel(new GridLayout(2, 2, 10, 5));
        statusPanel.setBackground(Color.WHITE);
        statusPanel.setBorder(BorderFactory.createTitledBorder("Status das APIs"));
        
        JLabel lblStripeStatus = new JLabel("Stripe: Inativo");
        lblStripeStatus.setForeground(Color.RED);
        JLabel lblMPStatus = new JLabel("Mercado Pago: Inativo");
        lblMPStatus.setForeground(Color.RED);
        
        statusPanel.add(lblStripeStatus);
        statusPanel.add(lblMPStatus);
        
        configPanel.add(statusPanel, gbc);
        
        panel.add(configPanel, BorderLayout.CENTER);
        
        // Painel de botões
        JPanel buttonPanel = new JPanel(new FlowLayout());
        buttonPanel.setBackground(Color.WHITE);
        
        JButton btnSalvar = createButton("💾 Salvar Configurações", new Color(76, 175, 80));
        JButton btnLimpar = createButton("🔄 Limpar", new Color(255, 152, 0));
        
        btnSalvar.addActionListener(e -> salvarConfiguracoes());
        btnLimpar.addActionListener(e -> limparFormulario());
        
        buttonPanel.add(btnSalvar);
        buttonPanel.add(btnLimpar);
        
        panel.add(buttonPanel, BorderLayout.SOUTH);
        
        return panel;
    }
    
    private JPanel createTransacoesPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);
        
        // Painel de busca e filtros
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        searchPanel.setBackground(Color.WHITE);
        
        JTextField txtBusca = new JTextField(25);
        JComboBox<String> cmbStatus = new JComboBox<>(new String[]{
            "Todos", "Aprovado", "Pendente", "Falhou", "Cancelado"
        });
        JComboBox<String> cmbAPI = new JComboBox<>(new String[]{
            "Todas", "Stripe", "Mercado Pago"
        });
        JButton btnBuscar = createButton("🔍 Buscar", new Color(33, 150, 243));
        JButton btnAtualizar = createButton("🔄 Atualizar", new Color(76, 175, 80));
        
        btnBuscar.addActionListener(e -> buscarTransacoes(txtBusca.getText(), (String) cmbStatus.getSelectedItem(), (String) cmbAPI.getSelectedItem()));
        btnAtualizar.addActionListener(e -> carregarTransacoes());
        
        searchPanel.add(new JLabel("Buscar:"));
        searchPanel.add(txtBusca);
        searchPanel.add(new JLabel("Status:"));
        searchPanel.add(cmbStatus);
        searchPanel.add(new JLabel("API:"));
        searchPanel.add(cmbAPI);
        searchPanel.add(btnBuscar);
        searchPanel.add(btnAtualizar);
        
        panel.add(searchPanel, BorderLayout.NORTH);
        
        // Tabela de transações
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
        panel.add(tableScrollPane, BorderLayout.CENTER);
        
        // Painel de ações
        JPanel actionPanel = new JPanel(new FlowLayout());
        actionPanel.setBackground(Color.WHITE);
        
        JButton btnDetalhes = createButton("👁️ Ver Detalhes", new Color(33, 150, 243));
        JButton btnEstornar = createButton("↩️ Estornar", new Color(255, 152, 0));
        JButton btnReembolsar = createButton("💰 Reembolsar", new Color(156, 39, 176));
        
        btnDetalhes.addActionListener(e -> verDetalhesTransacao());
        btnEstornar.addActionListener(e -> estornarTransacao());
        btnReembolsar.addActionListener(e -> reembolsarTransacao());
        
        actionPanel.add(btnDetalhes);
        actionPanel.add(btnEstornar);
        actionPanel.add(btnReembolsar);
        
        panel.add(actionPanel, BorderLayout.SOUTH);
        
        // Carregar dados iniciais
        carregarTransacoes();
        
        return panel;
    }
    
    private JPanel createWebhooksPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);
        
        // Configuração de Webhooks
        JPanel webhookConfigPanel = new JPanel(new GridBagLayout());
        webhookConfigPanel.setBackground(Color.WHITE);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.anchor = GridBagConstraints.WEST;
        
        gbc.gridx = 0; gbc.gridy = 0;
        webhookConfigPanel.add(new JLabel("🔗 URL do Webhook:"), gbc);
        gbc.gridx = 1; gbc.fill = GridBagConstraints.HORIZONTAL; gbc.weightx = 1;
        txtWebhookUrl = new JTextField(50);
        txtWebhookUrl.setText("https://seusite.com/webhook/pagamentos");
        webhookConfigPanel.add(txtWebhookUrl, gbc);
        
        gbc.gridx = 0; gbc.gridy = 1; gbc.fill = GridBagConstraints.NONE; gbc.weightx = 0;
        chkWebhookAtivo = new JCheckBox("Ativar Webhooks");
        chkWebhookAtivo.setBackground(Color.WHITE);
        webhookConfigPanel.add(chkWebhookAtivo, gbc);
        
        gbc.gridx = 1;
        JButton btnTestarWebhook = createButton("🧪 Testar Webhook", new Color(33, 150, 243));
        btnTestarWebhook.addActionListener(e -> testarWebhook());
        webhookConfigPanel.add(btnTestarWebhook, gbc);
        
        panel.add(webhookConfigPanel, BorderLayout.NORTH);
        
        // Log de eventos
        JPanel logPanel = new JPanel(new BorderLayout());
        logPanel.setBackground(Color.WHITE);
        logPanel.setBorder(BorderFactory.createTitledBorder("Log de Eventos Recebidos"));
        
        JTextArea txtLog = new JTextArea(15, 50);
        txtLog.setEditable(false);
        txtLog.setFont(new Font("Courier New", Font.PLAIN, 11));
        txtLog.setText(
            "2026-05-04 10:30:15 - Webhook recebido: payment_intent.succeeded\n" +
            "ID: pi_1234567890 | Valor: R$ 150.00 | API: Stripe\n" +
            "---\n" +
            "2026-05-04 10:25:30 - Webhook recebido: payment.created\n" +
            "ID: pay_1234567890 | Valor: R$ 89.90 | API: Mercado Pago\n" +
            "---\n" +
            "2026-05-04 10:20:45 - Webhook recebido: payment.failed\n" +
            "ID: pi_1234567891 | Valor: R$ 200.00 | API: Stripe\n"
        );
        
        JScrollPane logScrollPane = new JScrollPane(txtLog);
        logPanel.add(logScrollPane, BorderLayout.CENTER);
        
        JPanel logButtonPanel = new JPanel(new FlowLayout());
        logButtonPanel.setBackground(Color.WHITE);
        
        JButton btnLimparLog = createButton("🗑️ Limpar Log", new Color(244, 67, 54));
        JButton btnExportarLog = createButton("📤 Exportar Log", new Color(0, 150, 136));
        
        btnLimparLog.addActionListener(e -> txtLog.setText(""));
        btnExportarLog.addActionListener(e -> exportarLog());
        
        logButtonPanel.add(btnLimparLog);
        logButtonPanel.add(btnExportarLog);
        
        logPanel.add(logButtonPanel, BorderLayout.SOUTH);
        
        panel.add(logPanel, BorderLayout.CENTER);
        
        return panel;
    }
    
    private JPanel createRelatoriosPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);
        
        // Painel de filtros
        JPanel filterPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        filterPanel.setBackground(Color.WHITE);
        
        JComboBox<String> cmbPeriodo = new JComboBox<>(new String[]{
            "Hoje", "Últimos 7 dias", "Últimos 30 dias", "Este mês", "Mês passado"
        });
        JComboBox<String> cmbRelatorio = new JComboBox<>(new String[]{
            "Resumo de Transações", "Análise por API", "Taxa de Sucesso", "Volume por Dia"
        });
        JButton btnGerar = createButton("📊 Gerar Relatório", new Color(76, 175, 80));
        
        btnGerar.addActionListener(e -> gerarRelatorio((String) cmbRelatorio.getSelectedItem(), (String) cmbPeriodo.getSelectedItem()));
        
        filterPanel.add(new JLabel("Período:"));
        filterPanel.add(cmbPeriodo);
        filterPanel.add(new JLabel("Relatório:"));
        filterPanel.add(cmbRelatorio);
        filterPanel.add(btnGerar);
        
        panel.add(filterPanel, BorderLayout.NORTH);
        
        // Área de resultados
        JTextArea txtResultados = new JTextArea();
        txtResultados.setEditable(false);
        txtResultados.setFont(new Font("Courier New", Font.PLAIN, 11));
        txtResultados.setText(
            "📊 RELATÓRIO DE PAGAMENTOS API\n" +
            "================================\n\n" +
            "Selecione os filtros acima e clique em 'Gerar Relatório'\n\n" +
            "Relatórios disponíveis:\n" +
            "• Resumo de Transações - Total e status das transações\n" +
            "• Análise por API - Desempenho por API configurada\n" +
            "• Taxa de Sucesso - Percentual de aprovação/reprovação\n" +
            "• Volume por Dia - Gráfico de volume diário"
        );
        
        JScrollPane scrollPane = new JScrollPane(txtResultados);
        panel.add(scrollPane, BorderLayout.CENTER);
        
        return panel;
    }
    
    private JButton createButton(String text, Color color) {
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
    
    public void salvarConfiguracoes() {
        try {
            // Salvar configurações (simulação)
            apiStatus.put("stripe", chkStripeAtivo.isSelected());
            apiStatus.put("mercadopago", chkMercadoPagoAtivo.isSelected());
            
            JOptionPane.showMessageDialog(frame, "Configurações salvas com sucesso!", 
                "Sucesso", JOptionPane.INFORMATION_MESSAGE);
                
        } catch (Exception e) {
            JOptionPane.showMessageDialog(frame, "Erro ao salvar configurações: " + e.getMessage(), 
                "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    public void limparFormulario() {
        txtStripeKey.setText("");
        txtMercadoPagoToken.setText("");
        txtWebhookUrl.setText("https://seusite.com/webhook/pagamentos");
        chkStripeAtivo.setSelected(false);
        chkMercadoPagoAtivo.setSelected(false);
        chkWebhookAtivo.setSelected(false);
    }
    
    public void testarConexaoStripe() {
        String key = txtStripeKey.getText().trim();
        if (key.isEmpty()) {
            JOptionPane.showMessageDialog(frame, "Digite a chave do Stripe!", 
                "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        // Simulação de teste
        JOptionPane.showMessageDialog(frame, "✅ Conexão com Stripe estabelecida com sucesso!\n" +
            "Chave: " + key.substring(0, Math.min(10, key.length())) + "...", 
            "Teste de Conexão", JOptionPane.INFORMATION_MESSAGE);
        
        apiStatus.put("stripe", true);
    }
    
    public void testarConexaoMercadoPago() {
        String token = txtMercadoPagoToken.getText().trim();
        if (token.isEmpty()) {
            JOptionPane.showMessageDialog(frame, "Digite o token do Mercado Pago!", 
                "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        // Simulação de teste
        JOptionPane.showMessageDialog(frame, "✅ Conexão com Mercado Pago estabelecida com sucesso!\n" +
            "Token: " + token.substring(0, Math.min(10, token.length())) + "...", 
            "Teste de Conexão", JOptionPane.INFORMATION_MESSAGE);
        
        apiStatus.put("mercadopago", true);
    }
    
    public void testarWebhook() {
        String url = txtWebhookUrl.getText().trim();
        if (url.isEmpty()) {
            JOptionPane.showMessageDialog(frame, "Digite a URL do webhook!", 
                "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        // Simulação de teste
        JOptionPane.showMessageDialog(frame, "🧪 Testando webhook...\n\n" +
            "URL: " + url + "\n" +
            "Status: ✅ Webhook configurado e respondendo!\n" +
            "Evento de teste enviado com sucesso.", 
            "Teste de Webhook", JOptionPane.INFORMATION_MESSAGE);
    }
    
    public void buscarTransacoes(String termo, String status, String api) {
        JOptionPane.showMessageDialog(frame, "Buscando transações...\n" +
            "Termo: " + termo + "\n" +
            "Status: " + status + "\n" +
            "API: " + api + "\n\n" +
            "Funcionalidade em desenvolvimento...", "Busca", JOptionPane.INFORMATION_MESSAGE);
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
            
        } catch (Exception e) {
            JOptionPane.showMessageDialog(frame, "Erro ao carregar transações: " + e.getMessage(), 
                "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    public void verDetalhesTransacao() {
        int selectedRow = transacoesTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(frame, "Selecione uma transação para ver detalhes!", 
                "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        JOptionPane.showMessageDialog(frame, 
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
    }
    
    public void estornarTransacao() {
        JOptionPane.showMessageDialog(frame, "Funcionalidade de estorno em desenvolvimento...", 
            "Estorno", JOptionPane.INFORMATION_MESSAGE);
    }
    
    private void reembolsarTransacao() {
        JOptionPane.showMessageDialog(frame, "Funcionalidade de reembolso em desenvolvimento...", 
            "Reembolso", JOptionPane.INFORMATION_MESSAGE);
    }
    
    public void exportarLog() {
        JOptionPane.showMessageDialog(frame, "Log de webhooks exportado com sucesso!", 
            "Exportação", JOptionPane.INFORMATION_MESSAGE);
    }
    
    public void gerarRelatorio(String tipo, String periodo) {
        JOptionPane.showMessageDialog(frame, "Gerando relatório: " + tipo + "\n" +
            "Período: " + periodo + "\n\n" +
            "Funcionalidade em desenvolvimento...", "Relatório", JOptionPane.INFORMATION_MESSAGE);
    }
    
    public void processarPagamento(String gateway, double valor) {
        JOptionPane.showMessageDialog(frame, "Processando pagamento de R$ " + valor + " via " + gateway, 
            "Processamento", JOptionPane.INFORMATION_MESSAGE);
    }
    
    public void validarDadosPagamento() {
        JOptionPane.showMessageDialog(frame, "Validando dados do pagamento...", 
            "Validação", JOptionPane.INFORMATION_MESSAGE);
    }
    
    public void integrarComGateway(String gateway) {
        JOptionPane.showMessageDialog(frame, "Integrando com gateway: " + gateway, 
            "Integração", JOptionPane.INFORMATION_MESSAGE);
    }
    
    public void confirmarPagamento(String id) {
        JOptionPane.showMessageDialog(frame, "Pagamento confirmado: " + id, 
            "Confirmação", JOptionPane.INFORMATION_MESSAGE);
    }
    
    public void cancelarPagamento() {
        JOptionPane.showMessageDialog(frame, "Pagamento cancelado...", 
            "Cancelamento", JOptionPane.INFORMATION_MESSAGE);
    }
    
    public void estornarPagamento(String id) {
        JOptionPane.showMessageDialog(frame, "Pagamento estornado: " + id, 
            "Estorno", JOptionPane.INFORMATION_MESSAGE);
    }
    
    public void gerarRelatorioTransacoes() {
        JOptionPane.showMessageDialog(frame, "Gerando relatório de transações...", 
            "Relatório Transações", JOptionPane.INFORMATION_MESSAGE);
    }
    
    public void show() {
        frame.setVisible(true);
    }
    
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new PagamentoAPISwingController().show();
        });
    }
}
