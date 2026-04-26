package com.br.hermescomercial.util;

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
    
    public LogViewer() {
        initializeUI();
        loadLogFiles();
    }
    
    private void initializeUI() {
        setTitle("Visualizador de Logs - Hermes Comercial PDV");
        setSize(1000, 700);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        
        // Painel principal
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        // Painel de controles
        JPanel controlPanel = createControlPanel();
        mainPanel.add(controlPanel, BorderLayout.NORTH);
        
        // Painel central com abas
        JTabbedPane tabbedPane = new JTabbedPane();
        
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
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(new TitledBorder("Controles"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
        // Arquivo de log
        gbc.gridx = 0; gbc.gridy = 0;
        panel.add(new JLabel("Arquivo:"), gbc);
        gbc.gridx = 1;
        cbLogFile = new JComboBox<>();
        cbLogFile.setPreferredSize(new Dimension(200, 25));
        panel.add(cbLogFile, gbc);
        
        // Nível de log
        gbc.gridx = 2; gbc.gridy = 0;
        panel.add(new JLabel("Nível:"), gbc);
        gbc.gridx = 3;
        cbLogLevel = new JComboBox<>(new String[]{"TODOS", "DEBUG", "INFO", "WARNING", "ERROR", "CRITICAL"});
        cbLogLevel.setPreferredSize(new Dimension(120, 25));
        panel.add(cbLogLevel, gbc);
        
        // Busca
        gbc.gridx = 4; gbc.gridy = 0;
        panel.add(new JLabel("Buscar:"), gbc);
        gbc.gridx = 5;
        txtSearch = new JTextField(20);
        panel.add(txtSearch, gbc);
        
        // Botões
        gbc.gridx = 6; gbc.gridy = 0;
        JButton btnRefresh = new JButton("Atualizar");
        btnRefresh.addActionListener(this::refreshLogs);
        panel.add(btnRefresh, gbc);
        
        gbc.gridx = 7;
        JButton btnClear = new JButton("Limpar");
        btnClear.addActionListener(this::clearLogs);
        panel.add(btnClear, gbc);
        
        gbc.gridx = 8;
        JButton btnExport = new JButton("Exportar");
        btnExport.addActionListener(this::exportLogs);
        panel.add(btnExport, gbc);
        
        // Listeners
        cbLogFile.addActionListener(e -> loadLogContent());
        cbLogLevel.addActionListener(e -> filterLogs());
        txtSearch.addActionListener(e -> filterLogs());
        
        return panel;
    }
    
    private JScrollPane createTextPanel() {
        txtLogContent = new JTextArea();
        txtLogContent.setFont(new Font("Monospaced", Font.PLAIN, 12));
        txtLogContent.setEditable(false);
        txtLogContent.setBackground(Color.WHITE);
        return new JScrollPane(txtLogContent);
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
        
        return new JScrollPane(tblLogEntries);
    }
    
    private JPanel createStatsPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panel.setBorder(new TitledBorder("Estatísticas"));
        
        JLabel lblStats = new JLabel();
        lblStats.setFont(new Font("Arial", Font.BOLD, 12));
        panel.add(lblStats);
        
        // Carregar estatísticas
        String stats = LoggerUtil.getLogStatistics();
        lblStats.setText("<html>" + stats.replace("\n", "<br>") + "</html>");
        
        return panel;
    }
    
    private void loadLogFiles() {
        File logDir = new File(LOG_DIR);
        if (!logDir.exists()) {
            JOptionPane.showMessageDialog(this, 
                "Diretório de logs não encontrado: " + LOG_DIR, 
                "Aviso", JOptionPane.WARNING_MESSAGE);
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
        if (!logFile.exists()) return;
        
        StringBuilder content = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new FileReader(logFile))) {
            String line;
            while ((line = reader.readLine()) != null) {
                content.append(line).append("\n");
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, 
                "Erro ao ler arquivo de log: " + e.getMessage(), 
                "Erro", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        txtLogContent.setText(content.toString());
        txtLogContent.setCaretPosition(0);
        
        // Carregar tabela
        loadLogTable(content.toString());
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
        String searchText = txtSearch.getText().toLowerCase();
        
        if (selectedLevel.equals("TODOS") && searchText.isEmpty()) {
            loadLogContent();
            return;
        }
        
        // Filtrar conteúdo do texto
        String content = txtLogContent.getText();
        StringBuilder filteredContent = new StringBuilder();
        
        String[] lines = content.split("\n");
        for (String line : lines) {
            boolean matchesLevel = selectedLevel.equals("TODOS") || line.contains("[" + selectedLevel + "]");
            boolean matchesSearch = searchText.isEmpty() || line.toLowerCase().contains(searchText);
            
            if (matchesLevel && matchesSearch) {
                filteredContent.append(line).append("\n");
            }
        }
        
        txtLogContent.setText(filteredContent.toString());
        loadLogTable(filteredContent.toString());
    }
    
    private void refreshLogs(ActionEvent e) {
        loadLogFiles();
        loadLogContent();
        JOptionPane.showMessageDialog(this, "Logs atualizados com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
    }
    
    private void clearLogs(ActionEvent e) {
        txtLogContent.setText("");
        tableModel.setRowCount(0);
        txtSearch.setText("");
    }
    
    private void exportLogs(ActionEvent e) {
        String content = txtLogContent.getText();
        if (content.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Nenhum conteúdo para exportar!", "Aviso", JOptionPane.WARNING_MESSAGE);
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
                JOptionPane.showMessageDialog(this, "Logs exportados com sucesso!\nArquivo: " + selectedFile.getAbsolutePath(), "Sucesso", JOptionPane.INFORMATION_MESSAGE);
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(this, "Erro ao exportar logs: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
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
