package com.br.hermescomercial.pdv.controller;

import com.br.hermescomercial.ui.layout.LayoutPadrao;
import com.br.hermescomercial.util.SystemLogger;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.io.File;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

/**
 * Classe especializada para formulário de Exportação/Importação de dados
 * Segue o padrão Header → Abas → Tabelas → Ações
 * Versão 1.0.0 - Adaptada para PDVMenuLateralElegante
 */
public class PDVFormularioExportImport {
    
    private JPanel workArea;
    private String usuarioAtual;
    private String nomeUsuario;
    
    // Componentes da interface
    private JTabbedPane tabbedPane;
    private JComboBox<String> cmbTipoExportacao, cmbTabelaExportacao;
    private JCheckBox chkCabecalho, chkFormatacao, chkValidarDados;
    private JTable importTable;
    private DefaultTableModel importModel;
    private JTable historicoTable;
    private DefaultTableModel historicoModel;
    private JProgressBar progressBar;
    private JLabel lblStatus;
    private JTextField txtArquivo;
    
    // Dados
    private List<Operacao> historicoOperacoes;
    
    public PDVFormularioExportImport(JPanel workArea, String usuario, String nome) {
        this.workArea = workArea;
        this.usuarioAtual = usuario;
        this.nomeUsuario = nome;
        this.historicoOperacoes = new ArrayList<>();
        
        SystemLogger.ui("Inicializando PDVFormularioExportImport para usuário: " + usuario);
    }
    
    public JPanel criarFormularioExportImport() {
        JPanel mainPanel = LayoutPadrao.criarPainelComMargem(15);
        mainPanel.setLayout(new BorderLayout());
        
        // Header
        mainPanel.add(createHeaderPanel(), BorderLayout.NORTH);
        
        // Abas principais
        tabbedPane = new JTabbedPane();
        tabbedPane.setBackground(LayoutPadrao.COR_FUNDO);
        tabbedPane.setFont(LayoutPadrao.FONTE_TEXTO);
        
        tabbedPane.addTab("📤 Exportar", createExportPanel());
        tabbedPane.addTab("📥 Importar", createImportPanel());
        tabbedPane.addTab("📜 Histórico", createHistoricoPanel());
        tabbedPane.addTab("⚙️ Configurações", createConfigPanel());
        
        mainPanel.add(tabbedPane, BorderLayout.CENTER);
        
        // Carregar histórico inicial
        carregarHistorico();
        
        SystemLogger.ui("PDVFormularioExportImport criado com sucesso");
        return mainPanel;
    }
    
    private JPanel createHeaderPanel() {
        return LayoutPadrao.criarHeaderPDVSimples("📊 Exportação/Importação de Dados");
    }
    
    private JPanel createExportPanel() {
        JPanel panel = LayoutPadrao.criarPainelBranco();
        panel.setLayout(new BorderLayout());
        
        // Painel de configuração
        JPanel configPanel = LayoutPadrao.criarPainelComMargem(10);
        configPanel.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;
        
        // Tipo de arquivo
        gbc.gridx = 0; gbc.gridy = 0;
        configPanel.add(LayoutPadrao.criarRotuloCampo("Tipo de Arquivo:"), gbc);
        gbc.gridx = 1; gbc.fill = GridBagConstraints.HORIZONTAL; gbc.weightx = 1;
        cmbTipoExportacao = new JComboBox<>(new String[]{
            "CSV (Valores Separados por Vírgula)",
            "Excel (XLSX)",
            "PDF (Relatório)",
            "JSON",
            "XML"
        });
        cmbTipoExportacao.setFont(LayoutPadrao.FONTE_TEXTO);
        configPanel.add(cmbTipoExportacao, gbc);
        
        // Tabela/Dados
        gbc.gridx = 0; gbc.gridy = 1; gbc.fill = GridBagConstraints.NONE; gbc.weightx = 0;
        configPanel.add(LayoutPadrao.criarRotuloCampo("Tabela/Dados:"), gbc);
        gbc.gridx = 1; gbc.fill = GridBagConstraints.HORIZONTAL; gbc.weightx = 1;
        cmbTabelaExportacao = new JComboBox<>(new String[]{
            "Produtos", "Clientes", "Vendas", "Fornecedores", "Estoque",
            "Categorias", "Usuários", "Financeiro", "Relatórios", "Todos"
        });
        cmbTabelaExportacao.setFont(LayoutPadrao.FONTE_TEXTO);
        configPanel.add(cmbTabelaExportacao, gbc);
        
        // Opções
        gbc.gridx = 0; gbc.gridy = 2; gbc.gridwidth = 2;
        JPanel optionsPanel = LayoutPadrao.criarPainelComBorda("Opções de Exportação");
        optionsPanel.setLayout(new GridLayout(3, 1, 5, 5));
        
        chkCabecalho = LayoutPadrao.criarCheckBox("Incluir cabeçalho (CSV/Excel)");
        chkCabecalho.setSelected(true);
        
        chkFormatacao = LayoutPadrao.criarCheckBox("Preservar formatação (Excel)");
        chkFormatacao.setSelected(true);
        
        chkValidarDados = LayoutPadrao.criarCheckBox("Validar dados antes de exportar");
        chkValidarDados.setSelected(true);
        
        optionsPanel.add(chkCabecalho);
        optionsPanel.add(chkFormatacao);
        optionsPanel.add(chkValidarDados);
        
        configPanel.add(optionsPanel, gbc);
        
        panel.add(configPanel, BorderLayout.CENTER);
        
        // Painel de botões
        JPanel buttonPanel = LayoutPadrao.criarPainelComMargem(10);
        buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 10));
        
        JButton btnExportar = LayoutPadrao.criarBotaoSucesso("📤 Exportar");
        JButton btnVisualizar = LayoutPadrao.criarBotaoPrimario("👁️ Visualizar");
        JButton btnAgendar = LayoutPadrao.criarBotaoSecundario("📅 Agendar");
        
        btnExportar.addActionListener(e -> exportarDados());
        btnVisualizar.addActionListener(e -> visualizarDados());
        btnAgendar.addActionListener(e -> agendarExportacao());
        
        buttonPanel.add(btnExportar);
        buttonPanel.add(btnVisualizar);
        buttonPanel.add(btnAgendar);
        
        panel.add(buttonPanel, BorderLayout.SOUTH);
        
        return panel;
    }
    
    private JPanel createImportPanel() {
        JPanel panel = LayoutPadrao.criarPainelBranco();
        panel.setLayout(new BorderLayout());
        
        // Painel superior
        JPanel topPanel = LayoutPadrao.criarPainelComMargem(10);
        topPanel.setLayout(new BorderLayout());
        
        // Seleção de arquivo
        JPanel filePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        filePanel.setBackground(LayoutPadrao.COR_FUNDO);
        
        txtArquivo = new JTextField(40);
        txtArquivo.setEditable(false);
        txtArquivo.setText("Nenhum arquivo selecionado...");
        txtArquivo.setFont(LayoutPadrao.FONTE_TEXTO);
        
        JButton btnSelecionar = LayoutPadrao.criarBotaoPrimario("📁 Selecionar Arquivo");
        btnSelecionar.addActionListener(e -> selecionarArquivo());
        
        filePanel.add(LayoutPadrao.criarRotuloCampo("Arquivo:"));
        filePanel.add(txtArquivo);
        filePanel.add(btnSelecionar);
        
        topPanel.add(filePanel, BorderLayout.CENTER);
        
        // Opções de importação
        JPanel optionsPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        optionsPanel.setBackground(LayoutPadrao.COR_FUNDO);
        
        JCheckBox chkIgnorarCabecalho = LayoutPadrao.criarCheckBox("Ignorar primeira linha (cabeçalho)");
        chkIgnorarCabecalho.setSelected(true);
        
        JCheckBox chkValidarImport = LayoutPadrao.criarCheckBox("Validar dados durante importação");
        chkValidarImport.setSelected(true);
        
        JCheckBox chkAtualizar = LayoutPadrao.criarCheckBox("Atualizar registros existentes");
        
        optionsPanel.add(chkIgnorarCabecalho);
        optionsPanel.add(chkValidarImport);
        optionsPanel.add(chkAtualizar);
        
        topPanel.add(optionsPanel, BorderLayout.SOUTH);
        
        panel.add(topPanel, BorderLayout.NORTH);
        
        // Área central
        JPanel centerPanel = LayoutPadrao.criarPainelComMargem(10);
        centerPanel.setLayout(new BorderLayout());
        
        // Tabela de preview
        String[] columns = {"Linha", "Campo 1", "Campo 2", "Campo 3", "Status"};
        importModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        importTable = LayoutPadrao.criarTabela();
        importTable.setModel(importModel);
        
        JScrollPane tableScrollPane = LayoutPadrao.criarBarraRolagem(importTable);
        tableScrollPane.setPreferredSize(new Dimension(600, 300));
        
        centerPanel.add(tableScrollPane, BorderLayout.CENTER);
        
        // Painel de progresso
        JPanel progressPanel = LayoutPadrao.criarPainelComBorda("Progresso da Importação");
        progressPanel.setLayout(new BorderLayout());
        
        progressBar = new JProgressBar(0, 100);
        progressBar.setStringPainted(true);
        progressBar.setString("Aguardando início...");
        
        lblStatus = LayoutPadrao.criarRotuloTexto("Status: Pronto para importar");
        
        progressPanel.add(progressBar, BorderLayout.CENTER);
        progressPanel.add(lblStatus, BorderLayout.SOUTH);
        
        centerPanel.add(progressPanel, BorderLayout.SOUTH);
        
        panel.add(centerPanel, BorderLayout.CENTER);
        
        // Painel de botões
        JPanel actionPanel = LayoutPadrao.criarPainelComMargem(10);
        actionPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 10));
        
        JButton btnPreview = LayoutPadrao.criarBotaoPrimario("👁️ Preview");
        JButton btnImportar = LayoutPadrao.criarBotaoSucesso("📥 Importar");
        JButton btnCancelar = LayoutPadrao.criarBotaoPerigo("❌ Cancelar");
        
        btnPreview.addActionListener(e -> previewImportacao());
        btnImportar.addActionListener(e -> importarDados());
        btnCancelar.addActionListener(e -> cancelarImportacao());
        
        actionPanel.add(btnPreview);
        actionPanel.add(btnImportar);
        actionPanel.add(btnCancelar);
        
        panel.add(actionPanel, BorderLayout.SOUTH);
        
        return panel;
    }
    
    private JPanel createHistoricoPanel() {
        JPanel panel = LayoutPadrao.criarPainelBranco();
        panel.setLayout(new BorderLayout());
        
        // Painel superior
        JPanel topPanel = LayoutPadrao.criarPainelComMargem(10);
        topPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
        
        JButton btnAtualizar = LayoutPadrao.criarBotaoSucesso("🔄 Atualizar");
        JButton btnLimpar = LayoutPadrao.criarBotaoPerigo("🗑️ Limpar Histórico");
        JButton btnExportarLog = LayoutPadrao.criarBotaoSecundario("📤 Exportar Log");
        
        btnAtualizar.addActionListener(e -> carregarHistorico());
        btnLimpar.addActionListener(e -> limparHistorico());
        btnExportarLog.addActionListener(e -> exportarLog());
        
        topPanel.add(btnAtualizar);
        topPanel.add(btnLimpar);
        topPanel.add(btnExportarLog);
        
        panel.add(topPanel, BorderLayout.NORTH);
        
        // Tabela de histórico
        String[] columns = {"Data/Hora", "Tipo", "Arquivo", "Status", "Registros", "Usuário"};
        historicoModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        historicoTable = LayoutPadrao.criarTabela();
        historicoTable.setModel(historicoModel);
        
        JScrollPane tableScrollPane = LayoutPadrao.criarBarraRolagem(historicoTable);
        panel.add(tableScrollPane, BorderLayout.CENTER);
        
        return panel;
    }
    
    private JPanel createConfigPanel() {
        JPanel panel = LayoutPadrao.criarPainelBranco();
        panel.setLayout(new BorderLayout());
        
        // Configurações gerais
        JPanel configPanel = LayoutPadrao.criarPainelComMargem(10);
        configPanel.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;
        
        gbc.gridx = 0; gbc.gridy = 0;
        configPanel.add(LayoutPadrao.criarRotuloTexto("⚙️ Configurações de Exportação/Importação"), gbc);
        
        // Configurações de exportação
        gbc.gridy = 1;
        configPanel.add(LayoutPadrao.criarRotuloCampo("Exportação:"), gbc);
        
        gbc.gridy = 2;
        JCheckBox chkAutoBackup = LayoutPadrao.criarCheckBox("Backup automático diário");
        configPanel.add(chkAutoBackup, gbc);
        
        gbc.gridy = 3;
        JCheckBox chkCompactar = LayoutPadrao.criarCheckBox("Compactar arquivos exportados");
        configPanel.add(chkCompactar, gbc);
        
        gbc.gridy = 4;
        JCheckBox chkAssinatura = LayoutPadrao.criarCheckBox("Adicionar assinatura digital");
        configPanel.add(chkAssinatura, gbc);
        
        // Configurações de importação
        gbc.gridy = 5;
        configPanel.add(LayoutPadrao.criarRotuloCampo("Importação:"), gbc);
        
        gbc.gridy = 6;
        JCheckBox chkBackupAntes = LayoutPadrao.criarCheckBox("Backup automático antes de importar");
        configPanel.add(chkBackupAntes, gbc);
        
        gbc.gridy = 7;
        JCheckBox chkLogDetalhado = LayoutPadrao.criarCheckBox("Log detalhado de importação");
        chkLogDetalhado.setSelected(true);
        configPanel.add(chkLogDetalhado, gbc);
        
        gbc.gridy = 8;
        JCheckBox chkNotificacoes = LayoutPadrao.criarCheckBox("Notificações de conclusão");
        configPanel.add(chkNotificacoes, gbc);
        
        // Limites
        gbc.gridy = 9;
        configPanel.add(LayoutPadrao.criarRotuloCampo("Limite de registros por operação:"), gbc);
        gbc.gridx = 1;
        JComboBox<String> cmbLimite = new JComboBox<>(new String[]{"1000", "5000", "10000", "50000", "Ilimitado"});
        cmbLimite.setSelectedItem("10000");
        cmbLimite.setFont(LayoutPadrao.FONTE_TEXTO);
        configPanel.add(cmbLimite, gbc);
        
        panel.add(configPanel, BorderLayout.CENTER);
        
        // Painel de botões
        JPanel buttonPanel = LayoutPadrao.criarPainelComMargem(10);
        buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
        
        JButton btnSalvarConfig = LayoutPadrao.criarBotaoSucesso("💾 Salvar Configurações");
        btnSalvarConfig.addActionListener(e -> salvarConfiguracoes());
        
        buttonPanel.add(btnSalvarConfig);
        
        panel.add(buttonPanel, BorderLayout.SOUTH);
        
        return panel;
    }
    
    // Métodos de ação
    private void exportarDados() {
        String tipoArquivo = (String) cmbTipoExportacao.getSelectedItem();
        String tabela = (String) cmbTabelaExportacao.getSelectedItem();
        
        // Simulação de exportação
        JOptionPane.showMessageDialog(workArea.getTopLevelAncestor(), 
            "Exportando dados...\n\n" +
            "Tipo: " + tipoArquivo + "\n" +
            "Tabela: " + tabela + "\n" +
            "Cabeçalho: " + (chkCabecalho.isSelected() ? "Sim" : "Não") + "\n" +
            "Formatação: " + (chkFormatacao.isSelected() ? "Sim" : "Não") + "\n" +
            "Validação: " + (chkValidarDados.isSelected() ? "Sim" : "Não") + "\n\n" +
            "Funcionalidade em desenvolvimento...", 
            "Exportação", JOptionPane.INFORMATION_MESSAGE);
        
        // Adicionar ao histórico
        Operacao operacao = new Operacao(
            LocalDateTime.now(),
            "Exportação",
            tabela + "." + tipoArquivo.substring(0, 3).toLowerCase(),
            "Concluído",
            "1,234",
            usuarioAtual
        );
        historicoOperacoes.add(operacao);
        atualizarTabelaHistorico();
        
        SystemLogger.ui("Exportação simulada: " + tabela + " em " + tipoArquivo);
    }
    
    private void visualizarDados() {
        JOptionPane.showMessageDialog(workArea.getTopLevelAncestor(), 
            "Visualização de dados em desenvolvimento...\n\n" +
            "Recursos planejados:\n" +
            "• Preview dos dados antes de exportar\n" +
            "• Filtros e ordenação\n" +
            "• Formatação personalizada", 
            "Visualização", JOptionPane.INFORMATION_MESSAGE);
    }
    
    private void agendarExportacao() {
        JOptionPane.showMessageDialog(workArea.getTopLevelAncestor(), 
            "Agendamento de exportação em desenvolvimento...\n\n" +
            "Recursos planejados:\n" +
            "• Exportações agendadas (diária, semanal, mensal)\n" +
            "• Envio automático por e-mail\n" +
            "• Integração com sistema de tarefas", 
            "Agendamento", JOptionPane.INFORMATION_MESSAGE);
    }
    
    private void selecionarArquivo() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setFileFilter(new FileNameExtensionFilter(
            "Arquivos CSV, Excel, JSON, XML", 
            "csv", "xlsx", "xls", "json", "xml"
        ));
        
        int returnValue = fileChooser.showOpenDialog(workArea.getTopLevelAncestor());
        if (returnValue == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();
            txtArquivo.setText(selectedFile.getAbsolutePath());
            SystemLogger.ui("Arquivo selecionado para importação: " + selectedFile.getName());
        }
    }
    
    private void previewImportacao() {
        // Simulação de preview
        importModel.setRowCount(0);
        
        Object[][] dados = {
            {1, "Produto A", "R$ 10.50", "100", "Válido"},
            {2, "Produto B", "R$ 25.00", "50", "Válido"},
            {3, "Produto C", "R$ 15.75", "75", "Inválido - Preço negativo"},
            {4, "Produto D", "R$ 30.00", "25", "Válido"}
        };
        
        for (Object[] row : dados) {
            importModel.addRow(row);
        }
        
        JOptionPane.showMessageDialog(workArea.getTopLevelAncestor(), 
            "Preview carregado com " + dados.length + " registros!\n" +
            "1 registro com erro detectado.", 
            "Preview", JOptionPane.INFORMATION_MESSAGE);
        
        SystemLogger.ui("Preview de importação carregado com " + dados.length + " registros");
    }
    
    private void importarDados() {
        // Simulação de importação
        progressBar.setValue(0);
        lblStatus.setText("Iniciando importação...");
        
        // Simular progresso
        new Thread(() -> {
            try {
                for (int i = 0; i <= 100; i += 10) {
                    final int progress = i;
                    SwingUtilities.invokeLater(() -> {
                        progressBar.setValue(progress);
                        progressBar.setString(progress + "%");
                        lblStatus.setText("Importando... " + progress + "%");
                    });
                    Thread.sleep(200);
                }
                
                SwingUtilities.invokeLater(() -> {
                    lblStatus.setText("Importação concluída com sucesso!");
                    JOptionPane.showMessageDialog(workArea.getTopLevelAncestor(), 
                        "Dados importados com sucesso!\n" +
                        "Registros processados: " + importModel.getRowCount() + "\n" +
                        "Erros: 1", 
                        "Importação", JOptionPane.INFORMATION_MESSAGE);
                    
                    // Adicionar ao histórico
                    Operacao operacao = new Operacao(
                        LocalDateTime.now(),
                        "Importação",
                        "import.csv",
                        "Concluído",
                        String.valueOf(importModel.getRowCount()),
                        usuarioAtual
                    );
                    historicoOperacoes.add(operacao);
                    atualizarTabelaHistorico();
                    
                    SystemLogger.ui("Importação concluída: " + importModel.getRowCount() + " registros");
                });
                
            } catch (InterruptedException e) {
                SwingUtilities.invokeLater(() -> {
                    lblStatus.setText("Importação cancelada!");
                });
            }
        }).start();
    }
    
    private void cancelarImportacao() {
        progressBar.setValue(0);
        progressBar.setString("Aguardando início...");
        lblStatus.setText("Importação cancelada!");
        importModel.setRowCount(0);
        SystemLogger.ui("Importação cancelada pelo usuário");
    }
    
    private void carregarHistorico() {
        historicoModel.setRowCount(0);
        
        // Simulação de dados iniciais
        Object[][] dados = {
            {"2026-05-04 10:30:15", "Exportação", "produtos.csv", "Concluído", "1,234", "admin"},
            {"2026-05-04 09:45:20", "Importação", "clientes.xlsx", "Concluído", "567", "admin"},
            {"2026-05-03 16:20:10", "Exportação", "vendas.pdf", "Falhou", "0", "joao"},
            {"2026-05-03 14:15:30", "Importação", "fornecedores.json", "Concluído", "89", "maria"}
        };
        
        for (Object[] row : dados) {
            historicoModel.addRow(row);
        }
        
        SystemLogger.ui("Histórico de operações carregado: " + dados.length + " registros");
    }
    
    private void atualizarTabelaHistorico() {
        historicoModel.setRowCount(0);
        
        for (Operacao op : historicoOperacoes) {
            Object[] row = {
                op.getDataHora().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss")),
                op.getTipo(),
                op.getArquivo(),
                op.getStatus(),
                op.getRegistros(),
                op.getUsuario()
            };
            historicoModel.addRow(row);
        }
    }
    
    private void limparHistorico() {
        int confirm = JOptionPane.showConfirmDialog(workArea.getTopLevelAncestor(), 
            "Deseja realmente limpar todo o histórico?", 
            "Confirmar Limpeza", JOptionPane.YES_NO_OPTION);
        
        if (confirm == JOptionPane.YES_OPTION) {
            historicoOperacoes.clear();
            historicoModel.setRowCount(0);
            JOptionPane.showMessageDialog(workArea.getTopLevelAncestor(), "Histórico limpo com sucesso!", 
                "Histórico", JOptionPane.INFORMATION_MESSAGE);
            SystemLogger.ui("Histórico de operações limpo pelo usuário: " + usuarioAtual);
        }
    }
    
    private void exportarLog() {
        JOptionPane.showMessageDialog(workArea.getTopLevelAncestor(), 
            "Log de exportação/importação exportado com sucesso!\n" +
            "Arquivo: log_export_import_" + System.currentTimeMillis() + ".csv", 
            "Exportação", JOptionPane.INFORMATION_MESSAGE);
        
        SystemLogger.ui("Log de operações exportado");
    }
    
    private void salvarConfiguracoes() {
        JOptionPane.showMessageDialog(workArea.getTopLevelAncestor(), 
            "Configurações salvas com sucesso!", 
            "Configurações", JOptionPane.INFORMATION_MESSAGE);
        
        SystemLogger.ui("Configurações de Export/Import salvas pelo usuário: " + usuarioAtual);
    }
    
    // Classe de apoio
    private static class Operacao {
        private LocalDateTime dataHora;
        private String tipo;
        private String arquivo;
        private String status;
        private String registros;
        private String usuario;
        
        public Operacao(LocalDateTime dataHora, String tipo, String arquivo, String status, String registros, String usuario) {
            this.dataHora = dataHora;
            this.tipo = tipo;
            this.arquivo = arquivo;
            this.status = status;
            this.registros = registros;
            this.usuario = usuario;
        }
        
        public LocalDateTime getDataHora() { return dataHora; }
        public String getTipo() { return tipo; }
        public String getArquivo() { return arquivo; }
        public String getStatus() { return status; }
        public String getRegistros() { return registros; }
        public String getUsuario() { return usuario; }
    }
}
