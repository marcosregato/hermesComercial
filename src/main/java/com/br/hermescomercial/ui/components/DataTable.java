package com.br.hermescomercial.ui.components;

import com.br.hermescomercial.ui.layout.LayoutPadrao;

import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.*;
import java.util.List;
import java.util.ArrayList;
import java.util.function.Consumer;

/**
 * Tabela de Dados Reutilizável - Versão Refatorada
 * Componente UI para exibição e manipulação de dados tabulares
 * Versão 2.0.0 - UI Components Refactoring
 */
public class DataTable extends JPanel {
    
    // Configurações de coluna
    public static class ColumnConfig {
        private final String title;
        private final String fieldName;
        private final int width;
        private final boolean editable;
        private final Class<?> dataType;
        private final TableCellRenderer renderer;
        private final TableCellEditor editor;
        
        public ColumnConfig(String title, String fieldName) {
            this(title, fieldName, 100, false, String.class, null, null);
        }
        
        public ColumnConfig(String title, String fieldName, int width) {
            this(title, fieldName, width, false, String.class, null, null);
        }
        
        public ColumnConfig(String title, String fieldName, int width, boolean editable) {
            this(title, fieldName, width, editable, String.class, null, null);
        }
        
        public ColumnConfig(String title, String fieldName, int width, boolean editable, 
                          Class<?> dataType, TableCellRenderer renderer, TableCellEditor editor) {
            this.title = title;
            this.fieldName = fieldName;
            this.width = width;
            this.editable = editable;
            this.dataType = dataType;
            this.renderer = renderer;
            this.editor = editor;
        }
        
        // Getters
        public String getTitle() { return title; }
        public String getFieldName() { return fieldName; }
        public int getWidth() { return width; }
        public boolean isEditable() { return editable; }
        public Class<?> getDataType() { return dataType; }
        public TableCellRenderer getRenderer() { return renderer; }
        public TableCellEditor getEditor() { return editor; }
    }
    
    // Componentes
    private final JTable table;
    private final DefaultTableModel tableModel;
    private final JScrollPane scrollPane;
    private final JPanel toolbarPanel;
    private final JPanel statusPanel;
    
    // Configurações
    private final List<ColumnConfig> columns = new ArrayList<>();
    private boolean showToolbar = true;
    private boolean showStatus = true;
    private boolean sortable = true;
    private boolean filterable = true;
    private boolean searchable = true;
    
    // UI Elements
    private JTextField searchField;
    private JComboBox<String> filterColumn;
    private JTextField filterValue;
    private JLabel statusLabel;
    private JLabel countLabel;
    
    // Listeners
    private final List<Consumer<Integer>> selectionListeners = new ArrayList<>();
    private final List<Consumer<Integer>> doubleClickListeners = new ArrayList<>();
    
    public DataTable() {
        setLayout(new BorderLayout());
        setOpaque(false);
        
        // Criar modelo de tabela
        tableModel = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int column) {
                if (column < columns.size()) {
                    return columns.get(column).isEditable();
                }
                return false;
            }
            
            @Override
            public Class<?> getColumnClass(int columnIndex) {
                if (columnIndex < columns.size()) {
                    return columns.get(columnIndex).getDataType();
                }
                return String.class;
            }
        };
        
        // Criar tabela
        table = new JTable(tableModel);
        configureTable();
        
        // Scroll pane
        scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        
        // Toolbar
        toolbarPanel = createToolbarPanel();
        
        // Status panel
        statusPanel = createStatusPanel();
        
        // Montar painel
        if (showToolbar) {
            add(toolbarPanel, BorderLayout.NORTH);
        }
        add(scrollPane, BorderLayout.CENTER);
        if (showStatus) {
            add(statusPanel, BorderLayout.SOUTH);
        }
    }
    
    /**
     * Configura tabela
     */
    private void configureTable() {
        table.setFont(LayoutPadrao.FONTE_TEXTO);
        table.getTableHeader().setFont(LayoutPadrao.FONTE_SUBTITULO);
        table.setRowHeight(25);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.setAutoCreateRowSorter(true);
        
        // Listener de seleção
        table.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                int selectedRow = table.getSelectedRow();
                notifySelectionListeners(selectedRow);
            }
        });
        
        // Listener de double click
        table.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    int row = table.rowAtPoint(e.getPoint());
                    notifyDoubleClickListeners(row);
                }
            }
        });
    }
    
    /**
     * Cria painel de toolbar
     */
    private JPanel createToolbarPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setOpaque(false);
        panel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        
        // Painel de busca e filtro
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        searchPanel.setOpaque(false);
        
        if (searchable) {
            searchPanel.add(new JLabel("Buscar:"));
            searchField = new JTextField(20);
            searchField.setFont(LayoutPadrao.FONTE_CAMPO);
            searchField.setToolTipText("Digite para buscar...");
            searchField.addKeyListener(new KeyAdapter() {
                @Override
                public void keyReleased(KeyEvent e) {
                    performSearch();
                }
            });
            searchPanel.add(searchField);
        }
        
        if (filterable) {
            searchPanel.add(new JLabel("Filtrar:"));
            filterColumn = new JComboBox<>();
            filterColumn.setFont(LayoutPadrao.FONTE_CAMPO);
            searchPanel.add(filterColumn);
            
            filterValue = new JTextField(15);
            filterValue.setFont(LayoutPadrao.FONTE_CAMPO);
            filterValue.setToolTipText("Valor do filtro...");
            filterValue.addKeyListener(new KeyAdapter() {
                @Override
                public void keyReleased(KeyEvent e) {
                    performFilter();
                }
            });
            searchPanel.add(filterValue);
        }
        
        // Painel de botões
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.setOpaque(false);
        
        JButton refreshButton = LayoutPadrao.criarBotaoSecundario("🔄 Atualizar");
        refreshButton.addActionListener(e -> refresh());
        buttonPanel.add(refreshButton);
        
        JButton clearButton = LayoutPadrao.criarBotaoSecundario("🧹 Limpar");
        clearButton.addActionListener(e -> clearFilters());
        buttonPanel.add(clearButton);
        
        panel.add(searchPanel, BorderLayout.WEST);
        panel.add(buttonPanel, BorderLayout.EAST);
        
        return panel;
    }
    
    /**
     * Cria painel de status
     */
    private JPanel createStatusPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setOpaque(false);
        panel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        
        statusLabel = new JLabel("🟢 Pronto");
        statusLabel.setFont(LayoutPadrao.FONTE_TEXTO);
        statusLabel.setForeground(new Color(40, 167, 69));
        
        countLabel = new JLabel("📊 0 registros");
        countLabel.setFont(LayoutPadrao.FONTE_TEXTO);
        countLabel.setForeground(LayoutPadrao.COR_TEXTO);
        
        panel.add(statusLabel, BorderLayout.WEST);
        panel.add(countLabel, BorderLayout.EAST);
        
        return panel;
    }
    
    /**
     * Adiciona coluna
     */
    public void addColumn(ColumnConfig config) {
        columns.add(config);
        tableModel.addColumn(config.getTitle());
        
        // Configurar largura
        TableColumn column = table.getColumnModel().getColumn(columns.size() - 1);
        column.setPreferredWidth(config.getWidth());
        
        // Configurar renderer e editor
        if (config.getRenderer() != null) {
            column.setCellRenderer(config.getRenderer());
        }
        if (config.getEditor() != null) {
            column.setCellEditor(config.getEditor());
        }
        
        // Atualizar combo de filtro
        if (filterable && filterColumn != null) {
            filterColumn.addItem(config.getTitle());
        }
    }
    
    /**
     * Adiciona coluna simples
     */
    public void addColumn(String title, String fieldName) {
        addColumn(new ColumnConfig(title, fieldName));
    }
    
    /**
     * Adiciona coluna com largura
     */
    public void addColumn(String title, String fieldName, int width) {
        addColumn(new ColumnConfig(title, fieldName, width));
    }
    
    /**
     * Adiciona linha de dados
     */
    public void addRow(Object... values) {
        tableModel.addRow(values);
        updateCount();
    }
    
    /**
     * Adiciona múltiplas linhas
     */
    public void addRows(List<Object[]> rows) {
        for (Object[] row : rows) {
            tableModel.addRow(row);
        }
        updateCount();
    }
    
    /**
     * Limpa todas as linhas
     */
    public void clearRows() {
        tableModel.setRowCount(0);
        updateCount();
    }
    
    /**
     * Remove linha
     */
    public void removeRow(int index) {
        tableModel.removeRow(index);
        updateCount();
    }
    
    /**
     * Remove linha selecionada
     */
    public void removeSelectedRow() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow >= 0) {
            removeRow(selectedRow);
        }
    }
    
    /**
     * Obtém linha selecionada
     */
    public int getSelectedRow() {
        return table.getSelectedRow();
    }
    
    /**
     * Obtém valor da célula
     */
    public Object getValueAt(int row, int column) {
        return tableModel.getValueAt(row, column);
    }
    
    /**
     * Define valor da célula
     */
    public void setValueAt(Object value, int row, int column) {
        tableModel.setValueAt(value, row, column);
    }
    
    /**
     * Obtém todos os dados
     */
    public List<Object[]> getAllData() {
        List<Object[]> data = new ArrayList<>();
        for (int i = 0; i < tableModel.getRowCount(); i++) {
            Object[] row = new Object[tableModel.getColumnCount()];
            for (int j = 0; j < tableModel.getColumnCount(); j++) {
                row[j] = tableModel.getValueAt(i, j);
            }
            data.add(row);
        }
        return data;
    }
    
    /**
     * Realiza busca
     */
    private void performSearch() {
        if (!searchable || searchField == null) return;
        
        String searchText = searchField.getText().trim().toLowerCase();
        if (searchText.isEmpty()) {
            clearFilters();
            return;
        }
        
        statusLabel.setText("🔍 Buscando...");
        statusLabel.setForeground(Color.ORANGE);
        
        // Implementar busca simples
        TableRowSorter<TableModel> sorter = new TableRowSorter<>(tableModel);
        table.setRowSorter(sorter);
        
        // Criar filtro
        RowFilter<TableModel, Object> filter = RowFilter.regexFilter("(?i)" + searchText);
        sorter.setRowFilter(filter);
        
        updateCount();
        statusLabel.setText("🔍 Busca concluída");
        statusLabel.setForeground(new Color(40, 167, 69));
    }
    
    /**
     * Realiza filtro
     */
    private void performFilter() {
        if (!filterable || filterColumn == null || filterValue == null) return;
        
        int columnIndex = filterColumn.getSelectedIndex();
        String filterText = filterValue.getText().trim();
        
        if (columnIndex < 0 || filterText.isEmpty()) {
            clearFilters();
            return;
        }
        
        statusLabel.setText("🔍 Filtrando...");
        statusLabel.setForeground(Color.ORANGE);
        
        // Implementar filtro simples
        TableRowSorter<TableModel> sorter = new TableRowSorter<>(tableModel);
        table.setRowSorter(sorter);
        
        // Criar filtro para coluna específica
        RowFilter<TableModel, Object> filter = RowFilter.regexFilter("(?i)" + filterText, columnIndex);
        sorter.setRowFilter(filter);
        
        updateCount();
        statusLabel.setText("🔍 Filtro aplicado");
        statusLabel.setForeground(new Color(40, 167, 69));
    }
    
    /**
     * Limpa filtros
     */
    public void clearFilters() {
        table.setRowSorter(null);
        updateCount();
        
        if (searchField != null) {
            searchField.setText("");
        }
        if (filterValue != null) {
            filterValue.setText("");
        }
        
        statusLabel.setText("🟢 Pronto");
        statusLabel.setForeground(new Color(40, 167, 69));
    }
    
    /**
     * Atualiza contador
     */
    private void updateCount() {
        int rowCount = table.getRowCount();
        countLabel.setText("📊 " + rowCount + " registro" + (rowCount == 1 ? "" : "s"));
    }
    
    /**
     * Atualiza tabela
     */
    public void refresh() {
        table.revalidate();
        table.repaint();
        updateCount();
        
        statusLabel.setText("🔄 Atualizado");
        statusLabel.setForeground(new Color(40, 167, 69));
    }
    
    /**
     * Adiciona listener de seleção
     */
    public void addSelectionListener(Consumer<Integer> listener) {
        selectionListeners.add(listener);
    }
    
    /**
     * Adiciona listener de double click
     */
    public void addDoubleClickListener(Consumer<Integer> listener) {
        doubleClickListeners.add(listener);
    }
    
    /**
     * Notifica listeners de seleção
     */
    private void notifySelectionListeners(int row) {
        for (Consumer<Integer> listener : selectionListeners) {
            try {
                listener.accept(row);
            } catch (Exception e) {
                System.err.println("Erro no listener de seleção: " + e.getMessage());
            }
        }
    }
    
    /**
     * Notifica listeners de double click
     */
    private void notifyDoubleClickListeners(int row) {
        for (Consumer<Integer> listener : doubleClickListeners) {
            try {
                listener.accept(row);
            } catch (Exception e) {
                System.err.println("Erro no listener de double click: " + e.getMessage());
            }
        }
    }
    
    // Getters e Setters
    public JTable getTable() { return table; }
    public DefaultTableModel getModel() { return tableModel; }
    public List<ColumnConfig> getColumns() { return new ArrayList<>(columns); }
    
    public boolean isShowToolbar() { return showToolbar; }
    public void setShowToolbar(boolean showToolbar) { this.showToolbar = showToolbar; }
    
    public boolean isShowStatus() { return showStatus; }
    public void setShowStatus(boolean showStatus) { this.showStatus = showStatus; }
    
    public boolean isSortable() { return sortable; }
    public void setSortable(boolean sortable) { 
        this.sortable = sortable; 
        table.setAutoCreateRowSorter(sortable);
    }
    
    public boolean isFilterable() { return filterable; }
    public void setFilterable(boolean filterable) { this.filterable = filterable; }
    
    public boolean isSearchable() { return searchable; }
    public void setSearchable(boolean searchable) { this.searchable = searchable; }
    
    public JLabel getStatusLabel() { return statusLabel; }
    public JLabel getCountLabel() { return countLabel; }
}
