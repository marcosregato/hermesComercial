package com.br.hermescomercial.erp.controller;

import com.br.hermescomercial.ui.layout.LayoutPadrao;
import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
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
        frame.getContentPane().setBackground(LayoutPadrao.COR_FUNDO_ESCURO);
        
        // Painel principal
        mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        mainPanel.setBackground(LayoutPadrao.COR_FUNDO_ESCURO);
        
        // Header ERP padrão
        JPanel headerPanel = LayoutPadrao.criarHeaderERPComBotoes(
            "📊 Sistema de Relatórios", 
            "Geração e visualização de relatórios empresariais",
            "Administrador", 
            "Gerente",
            new JButton[]{
                LayoutPadrao.criarBotaoSucesso("📄 Gerar"),
                LayoutPadrao.criarBotaoPrimario("📤 Exportar"),
                LayoutPadrao.criarBotaoSecundario("🖨️ Imprimir")
            }
        );
        
        // Configurar ações dos botões do header
        JPanel buttonPanel = (JPanel) headerPanel.getComponent(2);
        JButton btnGerar = (JButton) buttonPanel.getComponent(0);
        JButton btnExportar = (JButton) buttonPanel.getComponent(1);
        JButton btnImprimir = (JButton) buttonPanel.getComponent(2);
        
        btnGerar.addActionListener(e -> gerarRelatorio());
        btnExportar.addActionListener(e -> exportarRelatorio());
        btnImprimir.addActionListener(e -> imprimirRelatorio("Relatório do Sistema Hermes Comercial"));
        
        // Painel superior com opções
        JPanel opcoesPanel = criarPainelOpcoes();
        
        // Painel de preview
        JPanel previewPanel = criarPainelPreview();
        
        // Painel de status
        JPanel statusPanel = criarPainelStatus();
        
        // Adicionar componentes ao painel principal
        mainPanel.add(headerPanel, BorderLayout.NORTH);
        mainPanel.add(opcoesPanel, BorderLayout.CENTER);
        
        // Criar painel para conteúdo principal
        JPanel contentPanel = new JPanel(new BorderLayout());
        contentPanel.add(previewPanel, BorderLayout.CENTER);
        contentPanel.add(statusPanel, BorderLayout.SOUTH);
        contentPanel.setOpaque(false);
        
        mainPanel.add(contentPanel, BorderLayout.SOUTH);
        
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
            LayoutPadrao.FONTE_SUBTITULO, 
            LayoutPadrao.COR_PRIMARIA));
        panel.setBackground(LayoutPadrao.COR_FUNDO);
        
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
        cbTipoRelatorio.setFont(LayoutPadrao.FONTE_CAMPO);
        cbTipoRelatorio.setBackground(Color.WHITE);
        panel.add(cbTipoRelatorio, gbc);
        
        // Período - Data Início
        gbc.gridx = 0; gbc.gridy = 1; gbc.weightx = 0;
        panel.add(createLabel("Data Início:"), gbc);
        gbc.gridx = 1; gbc.weightx = 1.0;
        txtDataInicio = new JTextField(15);
        txtDataInicio.setFont(LayoutPadrao.FONTE_CAMPO);
        txtDataInicio.setBackground(Color.WHITE);
        txtDataInicio.setBorder(LayoutPadrao.BORDA_CAMPO);
        txtDataInicio.setText(new SimpleDateFormat("dd/MM/yyyy").format(new Date()));
        panel.add(txtDataInicio, gbc);
        
        // Período - Data Fim
        gbc.gridx = 0; gbc.gridy = 2; gbc.weightx = 0;
        panel.add(createLabel("Data Fim:"), gbc);
        gbc.gridx = 1; gbc.weightx = 1.0;
        txtDataFim = new JTextField(15);
        txtDataFim.setFont(LayoutPadrao.FONTE_CAMPO);
        txtDataFim.setBackground(Color.WHITE);
        txtDataFim.setBorder(LayoutPadrao.BORDA_CAMPO);
        txtDataFim.setText(new SimpleDateFormat("dd/MM/yyyy").format(new Date()));
        panel.add(txtDataFim, gbc);
        
        // Opções de Formato
        gbc.gridx = 0; gbc.gridy = 3; gbc.weightx = 0;
        panel.add(createLabel("Formato:"), gbc);
        gbc.gridx = 1; gbc.weightx = 1.0;
        JPanel formatosPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        formatosPanel.setBackground(LayoutPadrao.COR_FUNDO);
        
        ckResumido = new JCheckBox("Resumido");
        ckResumido.setFont(LayoutPadrao.FONTE_CAMPO);
        ckResumido.setBackground(LayoutPadrao.COR_FUNDO);
        ckResumido.setSelected(true);
        
        ckDetalhado = new JCheckBox("Detalhado");
        ckDetalhado.setFont(LayoutPadrao.FONTE_CAMPO);
        ckDetalhado.setBackground(LayoutPadrao.COR_FUNDO);
        
        ckGraficos = new JCheckBox("Com Gráficos");
        ckGraficos.setFont(LayoutPadrao.FONTE_CAMPO);
        ckGraficos.setBackground(LayoutPadrao.COR_FUNDO);
        
        formatosPanel.add(ckResumido);
        formatosPanel.add(ckDetalhado);
        formatosPanel.add(ckGraficos);
        
        panel.add(formatosPanel, gbc);
        
        // Botões de Ação
        gbc.gridx = 0; gbc.gridy = 4; gbc.gridwidth = 2; gbc.weightx = 1.0;
        JPanel botoesPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        botoesPanel.setBackground(LayoutPadrao.COR_FUNDO);
        
        btnGerar = LayoutPadrao.criarBotaoPrimario("🔄 Gerar Relatório");
        btnGerar.addActionListener(e -> gerarRelatorio());
        
        btnExportar = LayoutPadrao.criarBotaoSucesso("📥 Exportar");
        btnExportar.addActionListener(e -> exportarRelatorio());
        
        btnVisualizar = LayoutPadrao.criarBotaoSecundario("👁️ Visualizar");
        btnVisualizar.addActionListener(e -> visualizarRelatorio());
        
        btnLimpar = LayoutPadrao.criarBotaoAlerta("🗑️ Limpar");
        btnLimpar.addActionListener(e -> limparCampos());
        
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
            LayoutPadrao.FONTE_SUBTITULO, 
            LayoutPadrao.COR_PRIMARIA));
        panel.setBackground(LayoutPadrao.COR_FUNDO);
        
        // Área de preview
        txtPreview = new JTextArea();
        txtPreview.setFont(new Font("Consolas", Font.PLAIN, 12));
        txtPreview.setBackground(Color.WHITE);
        txtPreview.setBorder(LayoutPadrao.BORDA_CAMPO);
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
        panel.setBackground(LayoutPadrao.COR_FUNDO);
        
        lblStatus = new JLabel("🔍 Pronto para gerar relatórios");
        lblStatus.setFont(LayoutPadrao.FONTE_TEXTO);
        lblStatus.setForeground(LayoutPadrao.COR_TEXTO_CLARO);
        
        panel.add(lblStatus, BorderLayout.CENTER);
        
        return panel;
    }
    
    private JLabel createLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(LayoutPadrao.FONTE_TEXTO);
        label.setForeground(LayoutPadrao.COR_TEXTO);
        return label;
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
        lblStatus.setForeground(LayoutPadrao.COR_ALERTA);
        
        // Simular geração do relatório
        SwingUtilities.invokeLater(() -> {
            try {
                Thread.sleep(1500); // Simular processamento
                
                String relatorio = gerarRelatorioExemplo(tipoRelatorio, dataInicio, dataFim);
                
                txtPreview.setText(relatorio);
                txtPreview.setCaretPosition(0);
                
                lblStatus.setText("✅ Relatório gerado com sucesso!");
                lblStatus.setForeground(LayoutPadrao.COR_SUCESSO);
                
            } catch (Exception e) {
                lblStatus.setText("❌ Erro ao gerar relatório: " + e.getMessage());
                lblStatus.setForeground(LayoutPadrao.COR_PERIGO);
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
                lblStatus.setForeground(LayoutPadrao.COR_SUCESSO);
                
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
        JButton btnImprimir = LayoutPadrao.criarBotaoPrimario("🖨️ Imprimir");
        btnImprimir.addActionListener(e -> imprimirRelatorio(txtPreview.getText()));
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
        lblStatus.setForeground(LayoutPadrao.COR_TEXTO_CLARO);
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
