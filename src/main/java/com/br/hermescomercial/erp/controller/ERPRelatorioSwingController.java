package com.br.hermescomercial.erp.controller;

import com.br.hermescomercial.theme.HermesTheme;
import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.ActionListener;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Controller para tela de relatórios no ERP
 * Versão 2.3.0 - Arquitetura Modular - Tema Padrão Hermes
 */
public class ERPRelatorioSwingController {
    
    private JFrame frame;
    private JPanel mainPanel;
    private JComboBox<String> cbTipoRelatorio;
    private JTextField txtDataInicio, txtDataFim;
    private JCheckBox ckResumido, ckDetalhado, ckGraficos;
    private JButton btnGerar, btnExportar, btnVisualizar, btnLimpar;
    private JTextArea txtPreview;
    private JLabel lblStatus;
    
    public ERPRelatorioSwingController() {
        initializeUI();
    }
    
    private void initializeUI() {
        frame = new JFrame("📊 Relatórios - ERP");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setSize(1000, 700);
        frame.setLocationRelativeTo(null);
        
        // Aplicar tema padrão Hermes
        frame.getContentPane().setBackground(HermesTheme.BACKGROUND_PRIMARY);
        
        // Painel principal
        mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(HermesTheme.SPACING_XL, HermesTheme.SPACING_XL, HermesTheme.SPACING_XL, HermesTheme.SPACING_XL));
        mainPanel.setBackground(HermesTheme.BACKGROUND_PRIMARY);
        
        // Painel superior com opções
        JPanel opcoesPanel = criarPainelOpcoes();
        
        // Painel de preview
        JPanel previewPanel = criarPainelPreview();
        
        // Painel de status
        JPanel statusPanel = criarPainelStatus();
        
        // Adicionar componentes ao painel principal
        mainPanel.add(opcoesPanel, BorderLayout.NORTH);
        mainPanel.add(previewPanel, BorderLayout.CENTER);
        mainPanel.add(statusPanel, BorderLayout.SOUTH);
        
        frame.add(mainPanel);
        frame.setVisible(true);
    }
    
    private JPanel criarPainelOpcoes() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createEtchedBorder(), 
            "⚙️ Opções do Relatório", 
            TitledBorder.LEFT, 
            TitledBorder.TOP, 
            HermesTheme.FONT_HEADER, 
            HermesTheme.TEXT_PRIMARY));
        panel.setBackground(HermesTheme.BACKGROUND_SECONDARY);
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
        // Tipo de Relatório
        gbc.gridx = 0; gbc.gridy = 0; gbc.weightx = 0;
        panel.add(createLabel("Tipo de Relatório:"), gbc);
        gbc.gridx = 1; gbc.weightx = 1.0;
        cbTipoRelatorio = new JComboBox<>(new String[]{
            "Vendas por Período",
            "Estoque Atual",
            "Contas a Receber",
            "Contas a Pagar",
            "Fluxo de Caixa",
            "DRE (Demonstrativo de Resultados)",
            "Clientes por Cidade",
            "Produtos Mais Vendidos",
            "Faturamento Mensal",
            "Análise de Rentabilidade"
        });
        cbTipoRelatorio.setFont(HermesTheme.FONT_DEFAULT);
        cbTipoRelatorio.setBackground(Color.WHITE);
        panel.add(cbTipoRelatorio, gbc);
        
        // Período - Data Início
        gbc.gridx = 0; gbc.gridy = 1; gbc.weightx = 0;
        panel.add(createLabel("Data Início:"), gbc);
        gbc.gridx = 1; gbc.weightx = 1.0;
        txtDataInicio = new JTextField(15);
        txtDataInicio.setFont(HermesTheme.FONT_DEFAULT);
        txtDataInicio.setBackground(Color.WHITE);
        txtDataInicio.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(HermesTheme.BORDER_LIGHT),
            BorderFactory.createEmptyBorder(5, 5, 5, 5)
        ));
        txtDataInicio.setText(new SimpleDateFormat("dd/MM/yyyy").format(new Date()));
        panel.add(txtDataInicio, gbc);
        
        // Período - Data Fim
        gbc.gridx = 0; gbc.gridy = 2; gbc.weightx = 0;
        panel.add(createLabel("Data Fim:"), gbc);
        gbc.gridx = 1; gbc.weightx = 1.0;
        txtDataFim = new JTextField(15);
        txtDataFim.setFont(HermesTheme.FONT_DEFAULT);
        txtDataFim.setBackground(Color.WHITE);
        txtDataFim.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(HermesTheme.BORDER_LIGHT),
            BorderFactory.createEmptyBorder(5, 5, 5, 5)
        ));
        txtDataFim.setText(new SimpleDateFormat("dd/MM/yyyy").format(new Date()));
        panel.add(txtDataFim, gbc);
        
        // Opções de Formato
        gbc.gridx = 0; gbc.gridy = 3; gbc.weightx = 0;
        panel.add(createLabel("Formato:"), gbc);
        gbc.gridx = 1; gbc.weightx = 1.0;
        JPanel formatosPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        formatosPanel.setBackground(HermesTheme.BACKGROUND_SECONDARY);
        
        ckResumido = new JCheckBox("Resumido");
        ckResumido.setFont(HermesTheme.FONT_DEFAULT);
        ckResumido.setBackground(HermesTheme.BACKGROUND_SECONDARY);
        ckResumido.setSelected(true);
        
        ckDetalhado = new JCheckBox("Detalhado");
        ckDetalhado.setFont(HermesTheme.FONT_DEFAULT);
        ckDetalhado.setBackground(HermesTheme.BACKGROUND_SECONDARY);
        
        ckGraficos = new JCheckBox("Com Gráficos");
        ckGraficos.setFont(HermesTheme.FONT_DEFAULT);
        ckGraficos.setBackground(HermesTheme.BACKGROUND_SECONDARY);
        
        formatosPanel.add(ckResumido);
        formatosPanel.add(ckDetalhado);
        formatosPanel.add(ckGraficos);
        
        panel.add(formatosPanel, gbc);
        
        // Botões de Ação
        gbc.gridx = 0; gbc.gridy = 4; gbc.gridwidth = 2; gbc.weightx = 1.0;
        JPanel botoesPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        botoesPanel.setBackground(HermesTheme.BACKGROUND_SECONDARY);
        
        btnGerar = createStyledButton("🔄 Gerar Relatório", HermesTheme.PRIMARY_COLOR, e -> gerarRelatorio());
        
        btnExportar = createStyledButton("📥 Exportar", HermesTheme.SUCCESS_COLOR, e -> exportarRelatorio());
        
        btnVisualizar = createStyledButton("👁️ Visualizar", HermesTheme.SECONDARY_COLOR, e -> visualizarRelatorio());
        
        btnLimpar = createStyledButton("🗑️ Limpar", HermesTheme.WARNING_COLOR, e -> limparCampos());
        
        botoesPanel.add(btnGerar);
        botoesPanel.add(btnExportar);
        botoesPanel.add(btnVisualizar);
        botoesPanel.add(btnLimpar);
        
        panel.add(botoesPanel, gbc);
        
        return panel;
    }
    
    private JPanel criarPainelPreview() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createEtchedBorder(), 
            "📋 Pré-visualização do Relatório", 
            TitledBorder.LEFT, 
            TitledBorder.TOP, 
            HermesTheme.FONT_HEADER, 
            HermesTheme.TEXT_PRIMARY));
        panel.setBackground(HermesTheme.BACKGROUND_SECONDARY);
        
        // Área de preview
        txtPreview = new JTextArea();
        txtPreview.setFont(new Font("Consolas", Font.PLAIN, 12));
        txtPreview.setBackground(Color.WHITE);
        txtPreview.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(HermesTheme.BORDER_LIGHT),
            BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));
        txtPreview.setEditable(false);
        
        JScrollPane scrollPane = new JScrollPane(txtPreview);
        scrollPane.setPreferredSize(new Dimension(800, 400));
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        
        panel.add(scrollPane, BorderLayout.CENTER);
        
        return panel;
    }
    
    private JPanel criarPainelStatus() {
        JPanel panel = new JPanel(new BorderLayout(10, 0));
        panel.setBackground(HermesTheme.BACKGROUND_SECONDARY);
        
        lblStatus = new JLabel("🔍 Pronto para gerar relatórios");
        lblStatus.setFont(HermesTheme.FONT_DEFAULT);
        lblStatus.setForeground(HermesTheme.TEXT_SECONDARY);
        
        panel.add(lblStatus, BorderLayout.CENTER);
        
        return panel;
    }
    
    private JLabel createLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(HermesTheme.FONT_DEFAULT);
        label.setForeground(HermesTheme.TEXT_PRIMARY);
        return label;
    }
    
    private JButton createStyledButton(String text, Color bgColor, ActionListener action) {
        JButton button = new JButton(text);
        button.setFont(HermesTheme.FONT_BUTTON);
        button.setBackground(bgColor);
        button.setForeground(Color.WHITE);
        button.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(bgColor.darker()),
            BorderFactory.createEmptyBorder(8, 16, 8, 16)
        ));
        button.setFocusPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.addActionListener(action);
        return button;
    }
    
    private void gerarRelatorio() {
        String tipoRelatorio = (String) cbTipoRelatorio.getSelectedItem();
        String dataInicio = txtDataInicio.getText().trim();
        String dataFim = txtDataFim.getText().trim();
        
        if (dataInicio.isEmpty() || dataFim.isEmpty()) {
            JOptionPane.showMessageDialog(frame, 
                "⚠️ Preencha o período para o relatório!", 
                "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        lblStatus.setText("⏳ Gerando relatório...");
        lblStatus.setForeground(HermesTheme.WARNING_COLOR);
        
        // Simular geração do relatório
        SwingUtilities.invokeLater(() -> {
            try {
                Thread.sleep(1500); // Simular processamento
                
                String relatorio = gerarRelatorioExemplo(tipoRelatorio, dataInicio, dataFim);
                
                txtPreview.setText(relatorio);
                txtPreview.setCaretPosition(0);
                
                lblStatus.setText("✅ Relatório gerado com sucesso!");
                lblStatus.setForeground(HermesTheme.SUCCESS_COLOR);
                
            } catch (Exception e) {
                lblStatus.setText("❌ Erro ao gerar relatório: " + e.getMessage());
                lblStatus.setForeground(HermesTheme.DANGER_COLOR);
            }
        });
    }
    
    private String gerarRelatorioExemplo(String tipo, String dataInicio, String dataFim) {
        StringBuilder sb = new StringBuilder();
        
        sb.append("=".repeat(80)).append("\n");
        sb.append("RELATÓRIO: ").append(tipo.toUpperCase()).append("\n");
        sb.append("PERÍODO: ").append(dataInicio).append(" a ").append(dataFim).append("\n");
        sb.append("GERADO EM: ").append(new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(new Date())).append("\n");
        sb.append("=".repeat(80)).append("\n\n");
        
        switch (tipo) {
            case "Vendas por Período":
                sb.append("RESUMO DE VENDAS\n");
                sb.append("-".repeat(40)).append("\n");
                sb.append("Total de Vendas: R$ 125.450,00\n");
                sb.append("Quantidade de Pedidos: 234\n");
                sb.append("Ticket Médio: R$ 536,32\n");
                sb.append("Maior Venda: R$ 12.300,00\n");
                sb.append("Menor Venda: R$ 45,50\n\n");
                
                sb.append("TOP 5 PRODUTOS MAIS VENDIDOS\n");
                sb.append("-".repeat(40)).append("\n");
                sb.append("1. Notebook Dell Inspiron - 45 unidades - R$ 157.500,00\n");
                sb.append("2. Mouse Wireless Logitech - 120 unidades - R$ 10.788,00\n");
                sb.append("3. Teclado Mecânico - 89 unidades - R$ 16.020,00\n");
                sb.append("4. Monitor LG 24\" - 67 unidades - R$ 59.630,00\n");
                sb.append("5. Impressora HP LaserJet - 23 unidades - R$ 27.600,00\n");
                break;
                
            case "Estoque Atual":
                sb.append("RESUMO DE ESTOQUE\n");
                sb.append("-".repeat(40)).append("\n");
                sb.append("Total de Produtos: 1.245\n");
                sb.append("Valor Total em Estoque: R$ 485.320,00\n");
                sb.append("Produtos com Estoque Baixo: 23\n");
                sb.append("Produtos sem Estoque: 8\n\n");
                
                sb.append("PRODUTOS COM ESTOQUE CRÍTICO\n");
                sb.append("-".repeat(40)).append("\n");
                sb.append("1. Papel A4 Resma - 5 unidades\n");
                sb.append("2. Tinta Impressora Preta - 3 unidades\n");
                sb.append("3. Mouse Wireless - 2 unidades\n");
                break;
                
            case "Fluxo de Caixa":
                sb.append("RESUMO DO FLUXO DE CAIXA\n");
                sb.append("-".repeat(40)).append("\n");
                sb.append("Saldo Inicial: R$ 45.230,00\n");
                sb.append("Entradas: R$ 125.450,00\n");
                sb.append("Saídas: R$ 89.760,00\n");
                sb.append("Saldo Final: R$ 80.920,00\n\n");
                
                sb.append("PRINCIPAIS ENTRADAS\n");
                sb.append("-".repeat(40)).append("\n");
                sb.append("Vendas: R$ 118.900,00\n");
                sb.append("Recebimentos: R$ 6.550,00\n\n");
                
                sb.append("PRINCIPAIS SAÍDAS\n");
                sb.append("-".repeat(40)).append("\n");
                sb.append("Compras: R$ 67.200,00\n");
                sb.append("Despesas: R$ 15.890,00\n");
                sb.append("Salários: R$ 6.670,00\n");
                break;
                
            default:
                sb.append("Dados do relatório em desenvolvimento.\n");
                sb.append("Este tipo de relatório será implementado em breve.\n");
        }
        
        sb.append("\n" + "=".repeat(80) + "\n");
        sb.append("FIM DO RELATÓRIO\n");
        
        return sb.toString();
    }
    
    private void exportarRelatorio() {
        if (txtPreview.getText().isEmpty()) {
            JOptionPane.showMessageDialog(frame, 
                "⚠️ Gere um relatório antes de exportar!", 
                "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Salvar Relatório");
        fileChooser.setFileFilter(new javax.swing.filechooser.FileNameExtensionFilter(
            "Arquivo de Texto (*.txt)", "txt"));
        
        String tipoRelatorio = (String) cbTipoRelatorio.getSelectedItem();
        String nomeArquivo = tipoRelatorio.replace(" ", "_").toLowerCase() + "_" + 
                           new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date()) + ".txt";
        fileChooser.setSelectedFile(new File(nomeArquivo));
        
        int userSelection = fileChooser.showSaveDialog(frame);
        
        if (userSelection == JFileChooser.APPROVE_OPTION) {
            try {
                File fileToSave = fileChooser.getSelectedFile();
                java.io.FileWriter writer = new java.io.FileWriter(fileToSave);
                writer.write(txtPreview.getText());
                writer.close();
                
                JOptionPane.showMessageDialog(frame, 
                    "✅ Relatório exportado com sucesso!\n" +
                    "Arquivo salvo em: " + fileToSave.getAbsolutePath(), 
                    "Sucesso", JOptionPane.INFORMATION_MESSAGE);
                
                lblStatus.setText("📁 Relatório exportado: " + fileToSave.getName());
                lblStatus.setForeground(HermesTheme.SUCCESS_COLOR);
                
            } catch (Exception e) {
                JOptionPane.showMessageDialog(frame, 
                    "❌ Erro ao exportar relatório: " + e.getMessage(), 
                    "Erro", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    private void visualizarRelatorio() {
        if (txtPreview.getText().isEmpty()) {
            JOptionPane.showMessageDialog(frame, 
                "⚠️ Gere um relatório antes de visualizar!", 
                "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        // Criar janela de visualização em tela cheia
        JFrame visualizacaoFrame = new JFrame("👁️ Visualização do Relatório");
        visualizacaoFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        visualizacaoFrame.setSize(800, 600);
        visualizacaoFrame.setLocationRelativeTo(frame);
        
        JTextArea visualizacaoArea = new JTextArea(txtPreview.getText());
        visualizacaoArea.setFont(new Font("Consolas", Font.PLAIN, 12));
        visualizacaoArea.setEditable(false);
        visualizacaoArea.setBackground(Color.WHITE);
        
        JScrollPane scrollPane = new JScrollPane(visualizacaoArea);
        visualizacaoFrame.add(scrollPane);
        
        // Botão de impressão
        JPanel botoesPanel = new JPanel(new FlowLayout());
        JButton btnImprimir = createStyledButton("🖨️ Imprimir", HermesTheme.PRIMARY_COLOR, e -> imprimirRelatorio(txtPreview.getText()));
        botoesPanel.add(btnImprimir);
        
        visualizacaoFrame.add(botoesPanel, BorderLayout.SOUTH);
        visualizacaoFrame.setVisible(true);
    }
    
    private void imprimirRelatorio(String conteudo) {
        try {
            boolean imprime = java.awt.Desktop.isDesktopSupported() && 
                           java.awt.Desktop.getDesktop().isSupported(java.awt.Desktop.Action.PRINT);
            
            if (imprime) {
                JOptionPane.showMessageDialog(frame, 
                    "🖨️ Enviando relatório para impressão...\n" +
                    "Verifique sua impressora padrão.", 
                    "Imprimir", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(frame, 
                    "⚠️ Impressão não suportada neste sistema.\n" +
                    "Exporte o relatório e imprima manualmente.", 
                    "Aviso", JOptionPane.WARNING_MESSAGE);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(frame, 
                "❌ Erro ao preparar impressão: " + e.getMessage(), 
                "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void limparCampos() {
        cbTipoRelatorio.setSelectedIndex(0);
        txtDataInicio.setText(new SimpleDateFormat("dd/MM/yyyy").format(new Date()));
        txtDataFim.setText(new SimpleDateFormat("dd/MM/yyyy").format(new Date()));
        ckResumido.setSelected(true);
        ckDetalhado.setSelected(false);
        ckGraficos.setSelected(false);
        txtPreview.setText("");
        
        lblStatus.setText("🔍 Pronto para gerar relatórios");
        lblStatus.setForeground(HermesTheme.TEXT_SECONDARY);
    }
    
    // ==================== MÉTODOS PÚBLICOS ====================
    
    public void show() {
        if (frame == null) {
            initializeUI();
        }
        frame.setVisible(true);
        frame.toFront();
    }
    
    public void dispose() {
        if (frame != null) {
            frame.dispose();
            frame = null;
        }
    }
}
