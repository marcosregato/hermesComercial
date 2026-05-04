package com.br.hermescomercial.util;

import com.br.hermescomercial.ui.layout.LayoutPadrao;
import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Visualizador de Logs para o Sistema Hermes Comercial PDV
 * Permite visualizar e filtrar logs do sistema
 */
public class LogViewer extends JFrame {
    
    private static final String LOG_DIR = System.getProperty("user.home") + "/hermes-pdv-logs";
    
    private JComboBox<String> cbLogLevel;
    private JComboBox<String> cbLogFile;
    private JTextArea txtLogContent;
    private JTable tblLogEntries;
    private DefaultTableModel tableModel;
    private JTextField txtSearch;
    private JLabel lblStats;
    
    public LogViewer() {
        initializeUI();
        loadLogFiles();
        // Carregar conteúdo do primeiro arquivo se disponível
        if (cbLogFile.getItemCount() > 0) {
            cbLogFile.setSelectedIndex(0);
            loadLogContent();
        }
    }
    
    private void initializeUI() {
        setTitle("Visualizador de Logs - Hermes Comercial PDV");
        setSize(1000, 700);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        
        // Painel principal
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        mainPanel.setBackground(Color.WHITE);
        
        // Painel de controles
        JPanel controlPanel = createControlPanel();
        mainPanel.add(controlPanel, BorderLayout.NORTH);
        
        // Painel central com abas
        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.setBackground(Color.WHITE);
        tabbedPane.setForeground(Color.BLACK);
        
        // Aba de texto
        JScrollPane textScrollPane = createTextPanel();
        tabbedPane.addTab("Visualização Texto", textScrollPane);
        
        // Aba de tabela
        JScrollPane tableScrollPane = createTablePanel();
        tabbedPane.addTab("Visualização Tabela", tableScrollPane);
        
        // Adicionar listener para atualizar tabela quando aba for selecionada
        tabbedPane.addChangeListener(e -> {
            if (tabbedPane.getSelectedIndex() == 1) { // Índice 1 = aba Visualização Tabela
                String content = txtLogContent.getText();
                if (!content.trim().isEmpty()) {
                    loadLogTable(content);
                }
            }
        });
        
        mainPanel.add(tabbedPane, BorderLayout.CENTER);
        
        // Painel de estatísticas
        JPanel statsPanel = createStatsPanel();
        mainPanel.add(statsPanel, BorderLayout.SOUTH);
        
        add(mainPanel);
    }
    
    private JPanel createControlPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(new TitledBorder("Controles"));
        panel.setBackground(Color.WHITE);
        
        // Painel superior para filtros com layout em grid
        JPanel filterPanel = new JPanel(new GridBagLayout());
        filterPanel.setOpaque(false);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
        // Primeira linha - Arquivo e Nível
        gbc.gridx = 0; gbc.gridy = 0;
        JLabel lblArquivo = new JLabel("Arquivo:");
        lblArquivo.setForeground(Color.BLACK);
        filterPanel.add(lblArquivo, gbc);
        
        gbc.gridx = 1;
        cbLogFile = new JComboBox<>();
        cbLogFile.setPreferredSize(new Dimension(200, 30));
        cbLogFile.setBackground(Color.WHITE);
        cbLogFile.setForeground(Color.BLACK);
        filterPanel.add(cbLogFile, gbc);
        
        gbc.gridx = 2;
        JLabel lblNivel = new JLabel("Nível:");
        lblNivel.setForeground(Color.BLACK);
        filterPanel.add(lblNivel, gbc);
        
        gbc.gridx = 3;
        cbLogLevel = new JComboBox<>(new String[]{"TODOS", "DEBUG", "INFO", "WARNING", "ERROR", "CRITICAL"});
        cbLogLevel.setPreferredSize(new Dimension(120, 30));
        cbLogLevel.setBackground(Color.WHITE);
        cbLogLevel.setForeground(Color.BLACK);
        filterPanel.add(cbLogLevel, gbc);
        
        // Segunda linha - Busca e botão
        gbc.gridx = 0; gbc.gridy = 1;
        JLabel lblBuscar = new JLabel("Buscar:");
        lblBuscar.setForeground(Color.BLACK);
        filterPanel.add(lblBuscar, gbc);
        
        gbc.gridx = 1; gbc.gridwidth = 2;
        txtSearch = new JTextField(20);
        txtSearch.setPreferredSize(new Dimension(300, 30));
        txtSearch.setBackground(Color.WHITE);
        txtSearch.setForeground(Color.BLACK);
        txtSearch.setCaretColor(Color.BLACK);
        filterPanel.add(txtSearch, gbc);
        
        gbc.gridx = 3; gbc.gridwidth = 1;
        JButton btnSearch = LayoutPadrao.criarBotaoPrimario("🔍 Buscar");
        btnSearch.addActionListener(e -> filterLogs());
        btnSearch.setPreferredSize(new Dimension(100, 30));
        filterPanel.add(btnSearch, gbc);
        
        panel.add(filterPanel, BorderLayout.CENTER);
        
        // Painel inferior para botões
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 10));
        buttonPanel.setOpaque(false);
        
        // Botões
        JButton btnRefresh = LayoutPadrao.criarBotaoSecundario("🔄 Atualizar");
        btnRefresh.addActionListener(this::refreshLogs);
        buttonPanel.add(btnRefresh);
        
        JButton btnClear = LayoutPadrao.criarBotaoAlerta("🧹 Limpar");
        btnClear.addActionListener(this::clearLogs);
        buttonPanel.add(btnClear);
        
        JButton btnExport = LayoutPadrao.criarBotaoSucesso("📤 Exportar");
        btnExport.addActionListener(this::exportLogs);
        buttonPanel.add(btnExport);
        
        panel.add(buttonPanel, BorderLayout.SOUTH);
        
        // Listeners
        cbLogFile.addActionListener(e -> loadLogContent());
        cbLogLevel.addActionListener(e -> filterLogs());
        txtSearch.addActionListener(e -> filterLogs());
        
        // Campo de busca permite digitação livre
        txtSearch.setEditable(true);
        
        return panel;
    }
    
    private JScrollPane createTextPanel() {
        txtLogContent = new JTextArea();
        txtLogContent.setFont(new Font("Monospaced", Font.PLAIN, 12));
        txtLogContent.setEditable(false);
        txtLogContent.setBackground(Color.WHITE);
        txtLogContent.setForeground(Color.BLACK);
        txtLogContent.setCaretColor(Color.BLACK);
        JScrollPane scrollPane = new JScrollPane(txtLogContent);
        scrollPane.getViewport().setBackground(Color.WHITE);
        return scrollPane;
    }
    
    private JScrollPane createTablePanel() {
        String[] columns = {"Data/Hora", "Nível", "Thread", "Mensagem"};
        tableModel = new DefaultTableModel(columns, 0);
        tblLogEntries = new JTable(tableModel);
        tblLogEntries.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
        tblLogEntries.getColumnModel().getColumn(0).setPreferredWidth(150);
        tblLogEntries.getColumnModel().getColumn(1).setPreferredWidth(80);
        tblLogEntries.getColumnModel().getColumn(2).setPreferredWidth(100);
        tblLogEntries.getColumnModel().getColumn(3).setPreferredWidth(500);
        
        // Aplicar tema claro na tabela
        tblLogEntries.setBackground(Color.WHITE);
        tblLogEntries.setForeground(Color.BLACK);
        tblLogEntries.getTableHeader().setBackground(Color.LIGHT_GRAY);
        tblLogEntries.getTableHeader().setForeground(Color.BLACK);
        tblLogEntries.setRowHeight(25);
        
        JScrollPane scrollPane = new JScrollPane(tblLogEntries);
        scrollPane.getViewport().setBackground(Color.WHITE);
        
        return scrollPane;
    }
    
    private JPanel createStatsPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 10));
        panel.setBorder(new TitledBorder("Estatísticas"));
        panel.setBackground(Color.WHITE);
        
        lblStats = new JLabel();
        lblStats.setFont(new Font("Arial", Font.BOLD, 12));
        lblStats.setForeground(Color.BLACK);
        panel.add(lblStats);
        
        // Atualizar estatísticas com nome do arquivo
        updateStatsPanel();
        
        return panel;
    }
    
    private void updateStatsPanel() {
        String selectedFile = (String) cbLogFile.getSelectedItem();
        String fileName = selectedFile != null ? selectedFile : "Nenhum arquivo selecionado";
        
        String stats = LoggerUtil.getLogStatistics();
        String statsText = "<html><b>Arquivo:</b> " + fileName + "<br><br>" + 
                          stats.replace("\n", "<br>") + "</html>";
        lblStats.setText(statsText);
    }
    
    private void loadLogFiles() {
        File logDir = new File(LOG_DIR);
        if (!logDir.exists()) {
            JOptionPane.showMessageDialog(this, 
                "Diretório de logs não encontrado: " + LOG_DIR, 
                "Aviso", 
                JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        File[] logFiles = logDir.listFiles((dir, name) -> name.endsWith(".log"));
        if (logFiles != null) {
            cbLogFile.removeAllItems();
            for (File file : logFiles) {
                cbLogFile.addItem(file.getName());
            }
        }
    }
    
    private void loadLogContent() {
        String selectedFile = (String) cbLogFile.getSelectedItem();
        if (selectedFile == null) return;
        
        File logFile = new File(LOG_DIR, selectedFile);
        if (!logFile.exists()) {
            JOptionPane.showMessageDialog(this, 
                "Arquivo de log não encontrado: " + selectedFile, 
                "Aviso", 
                JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        StringBuilder content = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new FileReader(logFile))) {
            String line;
            while ((line = reader.readLine()) != null) {
                content.append(line).append("\n");
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, 
                "Erro ao ler arquivo de log: " + e.getMessage(), 
                "Erro", 
                JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        txtLogContent.setText(content.toString());
        txtLogContent.setCaretPosition(0);
        
        // Carregar tabela
        loadLogTable(content.toString());
        
        // Atualizar painel de estatísticas
        updateStatsPanel();
    }
    
    private void loadLogTable(String content) {
        tableModel.setRowCount(0);
        
        String[] lines = content.split("\n");
        for (String line : lines) {
            if (line.trim().isEmpty()) continue;
            
            try {
                // Parse da linha: [timestamp] [level] [thread] message
                if (line.startsWith("[") && line.contains("]") && line.contains("[") && line.contains("]")) {
                    String[] parts = line.split("]", 3);
                    if (parts.length >= 4) {
                        String timestamp = parts[0].substring(1).trim();
                        String level = parts[1].substring(2).trim();
                        String thread = parts[2].substring(2).trim();
                        String message = parts[3].substring(1).trim();
                        
                        tableModel.addRow(new Object[]{timestamp, level, thread, message});
                    } else if (parts.length == 3) {
                        // Formato: [timestamp] [level] message
                        String timestamp = parts[0].substring(1).trim();
                        String level = parts[1].substring(2).trim();
                        String message = parts[2].substring(1).trim();
                        
                        tableModel.addRow(new Object[]{timestamp, level, "", message});
                    } else if (parts.length == 2) {
                        // Formato: [timestamp] message
                        String timestamp = parts[0].substring(1).trim();
                        String message = parts[1].substring(1).trim();
                        
                        tableModel.addRow(new Object[]{timestamp, "", "", message});
                    }
                } else {
                    // Linha não formatada, adiciona como mensagem
                    tableModel.addRow(new Object[]{"", "", "", line});
                }
            } catch (Exception e) {
                // Se não conseguir parsear, adiciona como mensagem simples
                tableModel.addRow(new Object[]{"", "", "", line});
            }
        }
    }
    
    private void filterLogs() {
        String selectedLevel = (String) cbLogLevel.getSelectedItem();
        String searchText = txtSearch.getText().toLowerCase().trim();
        
        // Se não houver texto de busca, apenas filtrar por nível
        if (searchText.isEmpty()) {
            filterByLevelOnly(selectedLevel);
            return;
        }
        
        // Buscar arquivos que correspondem ao texto de busca
        File logDir = new File(LOG_DIR);
        if (!logDir.exists()) return;
        
        File[] logFiles = logDir.listFiles((dir, name) -> 
            name.toLowerCase().contains(searchText) && name.endsWith(".log"));
        
        if (logFiles == null || logFiles.length == 0) {
            txtLogContent.setText("Nenhum arquivo de log encontrado com: " + searchText);
            loadLogTable("");
            return;
        }
        
        // Carregar conteúdo do primeiro arquivo encontrado
        StringBuilder content = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new FileReader(logFiles[0]))) {
            String line;
            while ((line = reader.readLine()) != null) {
                // Aplicar filtro por nível se necessário
                if (selectedLevel.equals("TODOS") || 
                    line.toUpperCase().contains("[" + selectedLevel.toUpperCase() + "]")) {
                    content.append(line).append("\n");
                }
            }
        } catch (IOException e) {
            txtLogContent.setText("Erro ao ler arquivo: " + e.getMessage());
            loadLogTable("");
            return;
        }
        
        // Atualizar interface
        txtLogContent.setText(content.toString());
        txtLogContent.setCaretPosition(0);
        loadLogTable(content.toString());
        
        // Atualizar ComboBox para o arquivo encontrado
        String foundFileName = logFiles[0].getName();
        cbLogFile.setSelectedItem(foundFileName);
    }
    
    private void filterByLevelOnly(String selectedLevel) {
        String selectedFile = (String) cbLogFile.getSelectedItem();
        if (selectedFile == null) return;
        
        File logFile = new File(LOG_DIR, selectedFile);
        if (!logFile.exists()) return;
        
        StringBuilder content = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new FileReader(logFile))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (selectedLevel.equals("TODOS") || 
                    line.toUpperCase().contains("[" + selectedLevel.toUpperCase() + "]")) {
                    content.append(line).append("\n");
                }
            }
        } catch (IOException e) {
            return;
        }
        
        txtLogContent.setText(content.toString());
        txtLogContent.setCaretPosition(0);
        loadLogTable(content.toString());
    }
    
    private void refreshLogs(ActionEvent e) {
        loadLogFiles();
        loadLogContent();
        JOptionPane.showMessageDialog(this, 
            "Logs atualizados com sucesso!", "Sucesso", 
            JOptionPane.INFORMATION_MESSAGE);
    }
    
    private void clearLogs(ActionEvent e) {
        txtLogContent.setText("");
        tableModel.setRowCount(0);
        txtSearch.setText("");
    }
    
    private void exportLogs(ActionEvent e) {
        String content = txtLogContent.getText();
        if (content.isEmpty()) {
            JOptionPane.showMessageDialog(this, 
                "Nenhum conteúdo para exportar!", "Aviso", 
                JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setFileFilter(new javax.swing.filechooser.FileNameExtensionFilter("Arquivos de Log (*.log)", "log"));
        fileChooser.setSelectedFile(new File("hermes-pdv-export-" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd-HH-mm-ss")) + ".log"));
        
        if (fileChooser.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();
            if (!selectedFile.getName().endsWith(".log")) {
                selectedFile = new File(selectedFile.getParent(), selectedFile.getName() + ".log");
            }
            
            try (java.io.FileWriter writer = new java.io.FileWriter(selectedFile)) {
                writer.write(content);
                JOptionPane.showMessageDialog(this, 
                    "Logs exportados com sucesso!\nArquivo: " + selectedFile.getAbsolutePath(), 
                    "Sucesso", 
                    JOptionPane.INFORMATION_MESSAGE);
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(this, 
                    "Erro ao exportar logs: " + ex.getMessage(), 
                    "Erro", 
                    JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    /**
     * Exibe o visualizador de logs
     */
    public static void showLogViewer() {
        SwingUtilities.invokeLater(() -> {
            LogViewer viewer = new LogViewer();
            viewer.setVisible(true);
        });
    }
}
