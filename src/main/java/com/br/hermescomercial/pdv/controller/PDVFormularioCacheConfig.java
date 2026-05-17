package com.br.hermescomercial.pdv.controller;

import com.br.hermescomercial.ui.layout.LayoutPadrao;
import com.br.hermescomercial.util.SystemLogger;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Classe especializada para formulário de configuração de cache
 * Segue o padrão Header → Busca → Formulário → Tabela
 * Versão 1.0.0 - Adaptada para PDVMenuLateralElegante
 */
public class PDVFormularioCacheConfig {
    
    private JPanel workArea;
    private String usuarioAtual;
    private String nomeUsuario;
    
    // Componentes da interface
    private JSpinner spnTamanhoMaximo;
    private JSpinner spnTempoExpiracao;
    private JComboBox<String> comboPoliticaEvicao;
    private JCheckBox chkHabilitarCache;
    private JCheckBox chkHabilitarEstatisticas;
    private JCheckBox chkHabilitarPersistencia;
    private JTextArea txtConfiguracoesAvancadas;
    private JTable tblCacheStats;
    private DefaultTableModel tableModel;
    
    // Dados de configuração
    private Map<String, Object> configuracoes;
    private List<CacheStats> estatisticas;
    
    // Cores
    private static final Color SUCCESS_COLOR = new Color(76, 175, 80);
    
    public PDVFormularioCacheConfig(JPanel workArea, String usuario, String nome) {
        this.workArea = workArea;
        this.usuarioAtual = usuario;
        this.nomeUsuario = nome;
        this.configuracoes = new HashMap<>();
        this.estatisticas = new ArrayList<>();
        carregarConfiguracoesPadrao();
        carregarEstatisticasExemplo();
    }
    
    public JPanel criarFormularioCacheConfig() {
        SystemLogger.ui("=== CRIANDO FORMULÁRIO CONFIGURAÇÃO CACHE ===");
        SystemLogger.ui("Usuário: " + usuarioAtual);
        
        JPanel painelPrincipal = new JPanel(new BorderLayout());
        painelPrincipal.setBackground(Color.WHITE);
        
        // Header
        painelPrincipal.add(criarHeader(), BorderLayout.NORTH);
        
        // Conteúdo principal
        JPanel conteudo = new JPanel(new BorderLayout());
        conteudo.setBackground(Color.WHITE);
        
        // Painel de configurações
        JPanel painelCentral = new JPanel(new BorderLayout());
        painelCentral.setBackground(Color.WHITE);
        
        // Formulário de configurações
        painelCentral.add(criarPainelConfiguracoes(), BorderLayout.NORTH);
        
        // Tabela de estatísticas
        painelCentral.add(criarPainelEstatisticas(), BorderLayout.CENTER);
        
        conteudo.add(painelCentral, BorderLayout.CENTER);
        
        // Painel de ações
        conteudo.add(criarPainelAcoes(), BorderLayout.SOUTH);
        
        painelPrincipal.add(conteudo, BorderLayout.CENTER);
        
        SystemLogger.ui("Formulário Configuração Cache criado com sucesso");
        return painelPrincipal;
    }
    
    private JPanel criarHeader() {
        JPanel header = LayoutPadrao.criarHeaderPanel("⚙️ Configuração de Cache");
        
        // Informações do usuário
        JPanel userInfo = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        userInfo.setBackground(new Color(250, 250, 250));
        userInfo.add(new JLabel("👤 " + nomeUsuario));
        userInfo.add(new JLabel(" | "));
        userInfo.add(new JLabel("🔧 Sistema"));
        
        header.add(userInfo, BorderLayout.EAST);
        return header;
    }
    
    private JPanel criarPainelConfiguracoes() {
        JPanel painel = new JPanel(new GridBagLayout());
        painel.setBackground(Color.WHITE);
        painel.setBorder(BorderFactory.createTitledBorder("🔧 Configurações do Cache"));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
        // Habilitar cache
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
        chkHabilitarCache = new JCheckBox("Habilitar Cache");
        chkHabilitarCache.setFont(new Font("Arial", Font.BOLD, 14));
        chkHabilitarCache.setSelected(true);
        painel.add(chkHabilitarCache, gbc);
        
        // Habilitar estatísticas
        gbc.gridy = 1;
        chkHabilitarEstatisticas = new JCheckBox("Habilitar Estatísticas");
        chkHabilitarEstatisticas.setSelected(true);
        painel.add(chkHabilitarEstatisticas, gbc);
        
        // Habilitar persistência
        gbc.gridy = 2;
        chkHabilitarPersistencia = new JCheckBox("Habilitar Persistência em Disco");
        chkHabilitarPersistencia.setSelected(false);
        painel.add(chkHabilitarPersistencia, gbc);
        
        // Tamanho máximo
        gbc.gridy = 3; gbc.gridx = 0; gbc.gridwidth = 1;
        painel.add(new JLabel("Tamanho Máximo (MB):"), gbc);
        gbc.gridx = 1; gbc.weightx = 1.0;
        spnTamanhoMaximo = new JSpinner(new SpinnerNumberModel(100, 1, 1000, 10));
        painel.add(spnTamanhoMaximo, gbc);
        
        // Tempo de expiração
        gbc.gridy = 4; gbc.gridx = 0; gbc.weightx = 0;
        painel.add(new JLabel("Tempo Expiração (min):"), gbc);
        gbc.gridx = 1; gbc.weightx = 1.0;
        spnTempoExpiracao = new JSpinner(new SpinnerNumberModel(30, 1, 1440, 5));
        painel.add(spnTempoExpiracao, gbc);
        
        // Política de evição
        gbc.gridy = 5; gbc.gridx = 0; gbc.weightx = 0;
        painel.add(new JLabel("Política de Evição:"), gbc);
        gbc.gridx = 1; gbc.weightx = 1.0;
        comboPoliticaEvicao = new JComboBox<>(new String[]{
            "LRU (Least Recently Used)",
            "LFU (Least Frequently Used)",
            "FIFO (First In First Out)",
            "LIFO (Last In First Out)"
        });
        painel.add(comboPoliticaEvicao, gbc);
        
        // Configurações avançadas
        gbc.gridy = 6; gbc.gridx = 0; gbc.gridwidth = 2; gbc.fill = GridBagConstraints.BOTH;
        gbc.weightx = 1.0; gbc.weighty = 0.1;
        painel.add(new JLabel("Configurações Avançadas (JSON):"), gbc);
        gbc.gridy = 7; gbc.weighty = 0.2;
        JScrollPane scrollPane = new JScrollPane(txtConfiguracoesAvancadas = new JTextArea(5, 50));
        txtConfiguracoesAvancadas.setFont(new Font("Monospaced", Font.PLAIN, 12));
        txtConfiguracoesAvancadas.setText("{\n  \"enableCompression\": true,\n  \"maxEntriesPerSegment\": 1000,\n  \"segmentCount\": 16\n}");
        painel.add(scrollPane, gbc);
        
        return painel;
    }
    
    private JPanel criarPainelEstatisticas() {
        JPanel painel = new JPanel(new BorderLayout());
        painel.setBackground(Color.WHITE);
        painel.setBorder(BorderFactory.createTitledBorder("📊 Estatísticas do Cache"));
        
        // Configurar tabela
        String[] colunas = {"Métrica", "Valor", "Descrição"};
        tableModel = new DefaultTableModel(colunas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        tblCacheStats = new JTable(tableModel);
        tblCacheStats.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tblCacheStats.getTableHeader().setReorderingAllowed(false);
        
        // Configurar largura das colunas
        tblCacheStats.getColumnModel().getColumn(0).setPreferredWidth(150);
        tblCacheStats.getColumnModel().getColumn(1).setPreferredWidth(100);
        tblCacheStats.getColumnModel().getColumn(2).setPreferredWidth(300);
        
        JScrollPane scrollPane = new JScrollPane(tblCacheStats);
        painel.add(scrollPane, BorderLayout.CENTER);
        
        // Painel de informações
        JPanel infoPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        infoPanel.setBackground(new Color(248, 249, 250));
        
        JLabel lblStatus = new JLabel("🟢 Cache Ativo");
        lblStatus.setFont(new Font("Arial", Font.BOLD, 12));
        lblStatus.setForeground(SUCCESS_COLOR);
        infoPanel.add(lblStatus);
        
        infoPanel.add(Box.createHorizontalStrut(20));
        
        JLabel lblMemoria = new JLabel("💾 Memória: 45.2 MB / 100 MB");
        lblMemoria.setFont(new Font("Arial", Font.PLAIN, 12));
        infoPanel.add(lblMemoria);
        
        infoPanel.add(Box.createHorizontalStrut(20));
        
        JLabel lblHitRate = new JLabel("🎯 Hit Rate: 87.5%");
        lblHitRate.setFont(new Font("Arial", Font.PLAIN, 12));
        infoPanel.add(lblHitRate);
        
        painel.add(infoPanel, BorderLayout.NORTH);
        
        return painel;
    }
    
    private JPanel criarPainelAcoes() {
        JPanel painel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        painel.setBackground(Color.WHITE);
        painel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        JButton btnSalvar = LayoutPadrao.criarBotaoSucesso("💾 Salvar Configurações");
        btnSalvar.addActionListener(e -> salvarConfiguracoes());
        
        JButton btnLimpar = LayoutPadrao.criarBotaoPerigo("🗑️ Limpar Cache");
        btnLimpar.addActionListener(e -> limparCache());
        
        JButton btnResetar = LayoutPadrao.criarBotaoPrimario("🔄 Resetar Estatísticas");
        btnResetar.addActionListener(e -> resetarEstatisticas());
        
        JButton btnTestar = LayoutPadrao.criarBotaoPrimario("🧪 Testar Performance");
        btnTestar.addActionListener(e -> testarPerformance());
        
        JButton btnExportar = LayoutPadrao.criarBotaoSecundario("📤 Exportar Config");
        btnExportar.addActionListener(e -> exportarConfiguracoes());
        
        painel.add(btnSalvar);
        painel.add(btnLimpar);
        painel.add(btnResetar);
        painel.add(btnTestar);
        painel.add(btnExportar);
        
        return painel;
    }
    
    private void salvarConfiguracoes() {
        try {
            // Salvar configurações
            configuracoes.put("habilitarCache", chkHabilitarCache.isSelected());
            configuracoes.put("habilitarEstatisticas", chkHabilitarEstatisticas.isSelected());
            configuracoes.put("habilitarPersistencia", chkHabilitarPersistencia.isSelected());
            configuracoes.put("tamanhoMaximo", spnTamanhoMaximo.getValue());
            configuracoes.put("tempoExpiracao", spnTempoExpiracao.getValue());
            configuracoes.put("politicaEvicao", comboPoliticaEvicao.getSelectedItem());
            configuracoes.put("configuracoesAvancadas", txtConfiguracoesAvancadas.getText());
            
            JOptionPane.showMessageDialog(workArea, 
                "Configurações salvas com sucesso!\n" +
                "Cache: " + (chkHabilitarCache.isSelected() ? "Ativado" : "Desativado") + "\n" +
                "Tamanho: " + spnTamanhoMaximo.getValue() + " MB\n" +
                "Expiração: " + spnTempoExpiracao.getValue() + " min",
                "Sucesso", JOptionPane.INFORMATION_MESSAGE);
            
            SystemLogger.ui("Configurações do cache salvas por " + usuarioAtual);
            
        } catch (Exception e) {
            JOptionPane.showMessageDialog(workArea, "Erro ao salvar configurações: " + e.getMessage(), 
                "Erro", JOptionPane.ERROR_MESSAGE);
            SystemLogger.ui("Erro ao salvar configurações do cache: " + e.getMessage());
        }
    }
    
    private void limparCache() {
        int confirmacao = JOptionPane.showConfirmDialog(workArea, 
            "Deseja realmente limpar todo o cache?\nEsta ação não pode ser desfeita!", 
            "Confirmar Limpeza", JOptionPane.YES_NO_OPTION);
        
        if (confirmacao == JOptionPane.YES_OPTION) {
            // Simular limpeza do cache
            estatisticas.clear();
            carregarEstatisticasExemplo();
            atualizarTabelaEstatisticas();
            
            JOptionPane.showMessageDialog(workArea, "Cache limpo com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
            SystemLogger.ui("Cache limpo por " + usuarioAtual);
        }
    }
    
    private void resetarEstatisticas() {
        int confirmacao = JOptionPane.showConfirmDialog(workArea, 
            "Deseja resetar todas as estatísticas?", 
            "Confirmar Reset", JOptionPane.YES_NO_OPTION);
        
        if (confirmacao == JOptionPane.YES_OPTION) {
            // Resetar estatísticas
            carregarEstatisticasExemplo();
            atualizarTabelaEstatisticas();
            
            JOptionPane.showMessageDialog(workArea, "Estatísticas resetadas com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
            SystemLogger.ui("Estatísticas do cache resetadas por " + usuarioAtual);
        }
    }
    
    private void testarPerformance() {
        // Simular teste de performance
        JPanel painelTeste = new JPanel(new BorderLayout());
        painelTeste.setPreferredSize(new Dimension(400, 200));
        
        JProgressBar progressBar = new JProgressBar(0, 100);
        progressBar.setStringPainted(true);
        
        JTextArea txtResultados = new JTextArea("Executando testes de performance...\n");
        txtResultados.setEditable(false);
        
        painelTeste.add(progressBar, BorderLayout.NORTH);
        painelTeste.add(new JScrollPane(txtResultados), BorderLayout.CENTER);
        
        JDialog dialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(workArea), "Teste de Performance", true);
        dialog.add(painelTeste);
        dialog.pack();
        dialog.setLocationRelativeTo(workArea);
        
        // Simular teste em thread separada
        new Thread(() -> {
            try {
                for (int i = 0; i <= 100; i += 10) {
                    final int progress = i;
                    SwingUtilities.invokeLater(() -> {
                        progressBar.setValue(progress);
                        txtResultados.append("Teste " + progress + "% concluído...\n");
                    });
                    Thread.sleep(200);
                }
                
                SwingUtilities.invokeLater(() -> {
                    txtResultados.append("\n=== RESULTADOS ===\n");
                    txtResultados.append("Latência média: 2.3ms\n");
                    txtResultados.append("Throughput: 1,250 ops/sec\n");
                    txtResultados.append("Hit Rate: 87.5%\n");
                    txtResultados.append("Uso memória: 45.2 MB\n");
                    txtResultados.append("\n✅ Teste concluído com sucesso!");
                });
                
            } catch (InterruptedException e) {
                SwingUtilities.invokeLater(() -> {
                    txtResultados.append("\n❌ Teste interrompido!");
                });
            }
        }).start();
        
        dialog.setVisible(true);
        
        SystemLogger.ui("Teste de performance do cache executado por " + usuarioAtual);
    }
    
    private void exportarConfiguracoes() {
        // Simular exportação
        String configJson = "{\n";
        configJson += "  \"habilitarCache\": " + chkHabilitarCache.isSelected() + ",\n";
        configJson += "  \"tamanhoMaximo\": " + spnTamanhoMaximo.getValue() + ",\n";
        configJson += "  \"tempoExpiracao\": " + spnTempoExpiracao.getValue() + ",\n";
        configJson += "  \"politicaEvicao\": \"" + comboPoliticaEvicao.getSelectedItem() + "\"\n";
        configJson += "}";
        
        JTextArea textArea = new JTextArea(configJson);
        textArea.setEditable(false);
        textArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
        
        JOptionPane.showMessageDialog(workArea, new JScrollPane(textArea), 
            "Configurações Exportadas", JOptionPane.INFORMATION_MESSAGE);
        
        SystemLogger.ui("Configurações do cache exportadas por " + usuarioAtual);
    }
    
    private void carregarConfiguracoesPadrao() {
        configuracoes.put("habilitarCache", true);
        configuracoes.put("habilitarEstatisticas", true);
        configuracoes.put("habilitarPersistencia", false);
        configuracoes.put("tamanhoMaximo", 100);
        configuracoes.put("tempoExpiracao", 30);
        configuracoes.put("politicaEvicao", "LRU (Least Recently Used)");
    }
    
    private void carregarEstatisticasExemplo() {
        estatisticas.add(new CacheStats("Total de Entradas", "1,247", "Número total de itens no cache"));
        estatisticas.add(new CacheStats("Hits", "1,091", "Número de acertos no cache"));
        estatisticas.add(new CacheStats("Misses", "156", "Número de falhas no cache"));
        estatisticas.add(new CacheStats("Hit Rate", "87.5%", "Taxa de acertos"));
        estatisticas.add(new CacheStats("Evictions", "23", "Itens removidos por política"));
        estatisticas.add(new CacheStats("Memória Usada", "45.2 MB", "Memória atualmente em uso"));
        estatisticas.add(new CacheStats("Tempo Médio", "2.3ms", "Tempo médio de resposta"));
        estatisticas.add(new CacheStats("Throughput", "1,250 ops/sec", "Operações por segundo"));
    }
    
    private void atualizarTabelaEstatisticas() {
        tableModel.setRowCount(0);
        
        for (CacheStats stat : estatisticas) {
            Object[] rowData = {
                stat.getMetrica(),
                stat.getValor(),
                stat.getDescricao()
            };
            tableModel.addRow(rowData);
        }
    }
    
    // Classe interna para representar estatísticas
    private static class CacheStats {
        private String metrica;
        private String valor;
        private String descricao;
        
        public CacheStats(String metrica, String valor, String descricao) {
            this.metrica = metrica;
            this.valor = valor;
            this.descricao = descricao;
        }
        
        // Getters
        public String getMetrica() { return metrica; }
        public String getValor() { return valor; }
        public String getDescricao() { return descricao; }
    }
}
