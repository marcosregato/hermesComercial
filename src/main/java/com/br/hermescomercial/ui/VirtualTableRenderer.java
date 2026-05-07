package com.br.hermescomercial.ui;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import java.awt.*;
import java.util.List;
import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Renderizador Virtual para Tabelas Grandes
 * Renderiza apenas as linhas visíveis para performance extrema
 */
public class VirtualTableRenderer extends JTable {
    
    private static final int CACHE_SIZE = 100;
    private static final int PREFETCH_ROWS = 20;
    
    private List<Object[]> fullData;
    private String[] columnNames;
    private ConcurrentHashMap<Integer, Object[]> rowCache = new ConcurrentHashMap<>();
    private int viewportHeight = 0;
    private int firstVisibleRow = 0;
    private int lastVisibleRow = 0;
    
    public VirtualTableRenderer(String[] columnNames, List<Object[]> fullData) {
        this.columnNames = columnNames;
        this.fullData = fullData != null ? fullData : new ArrayList<>();
        
        setModel(new VirtualTableModel());
        setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        setRowHeight(25);
        
        // Otimizações de renderização
        setFillsViewportHeight(true);
        setOpaque(true);
        setBackground(Color.WHITE);
        
        // Listener para viewport changes
        addHierarchyListener(e -> {
            if (isShowing()) {
                updateViewportBounds();
            }
        });
    }
    
    /**
     * Atualiza os limites do viewport
     */
    private void updateViewportBounds() {
        JScrollPane scrollPane = (JScrollPane) SwingUtilities.getAncestorOfClass(JScrollPane.class, this);
        if (scrollPane != null) {
            JViewport viewport = scrollPane.getViewport();
            viewportHeight = viewport.getHeight();
            
            Point viewPosition = viewport.getViewPosition();
            firstVisibleRow = Math.max(0, viewPosition.y / getRowHeight());
            lastVisibleRow = Math.min(fullData.size() - 1, 
                                   firstVisibleRow + (viewportHeight / getRowHeight()) + PREFETCH_ROWS);
            
            // Prefetching inteligente
            prefetchRows();
        }
    }
    
    /**
     * Carrega em cache as linhas próximas à área visível
     */
    private void prefetchRows() {
        int start = Math.max(0, firstVisibleRow - PREFETCH_ROWS);
        int end = Math.min(fullData.size() - 1, lastVisibleRow + PREFETCH_ROWS);
        
        for (int i = start; i <= end; i++) {
            if (!rowCache.containsKey(i) && i < fullData.size()) {
                rowCache.put(i, fullData.get(i));
            }
        }
        
        // Limpa cache antigo
        rowCache.entrySet().removeIf(entry -> 
            entry.getKey() < start - CACHE_SIZE || entry.getKey() > end + CACHE_SIZE);
    }
    
    /**
     * Table Model Virtual
     */
    private class VirtualTableModel extends AbstractTableModel {
        
        @Override
        public int getRowCount() {
            return fullData.size();
        }
        
        @Override
        public int getColumnCount() {
            return columnNames.length;
        }
        
        @Override
        public String getColumnName(int column) {
            return columnNames[column];
        }
        
        @Override
        public Object getValueAt(int row, int column) {
            // Cache lookup primeiro
            Object[] cachedRow = rowCache.get(row);
            if (cachedRow != null) {
                return cachedRow[column];
            }
            
            // Se não está no cache, busca do dataset principal
            if (row < fullData.size()) {
                Object[] dataRow = fullData.get(row);
                
                // Adiciona ao cache se estiver na área visível
                if (row >= firstVisibleRow - PREFETCH_ROWS && 
                    row <= lastVisibleRow + PREFETCH_ROWS) {
                    rowCache.put(row, dataRow);
                }
                
                return dataRow[column];
            }
            
            return null;
        }
        
        @Override
        public void setValueAt(Object value, int row, int column) {
            if (row < fullData.size()) {
                Object[] dataRow = fullData.get(row);
                dataRow[column] = value;
                
                // Atualiza cache
                if (rowCache.containsKey(row)) {
                    rowCache.get(row)[column] = value;
                }
                
                fireTableCellUpdated(row, column);
            }
        }
        
        @Override
        public boolean isCellEditable(int row, int column) {
            return false; // Tabela read-only para performance
        }
    }
    
    /**
     * Atualiza dados com performance otimizada
     */
    public void updateData(List<Object[]> newData) {
        this.fullData = newData != null ? newData : new ArrayList<>();
        rowCache.clear();
        
        // Atualiza viewport
        SwingUtilities.invokeLater(() -> {
            updateViewportBounds();
            ((AbstractTableModel) getModel()).fireTableDataChanged();
        });
    }
    
    /**
     * Adiciona linha com performance otimizada
     */
    public void addRow(Object[] rowData) {
        fullData.add(rowData);
        
        int rowIndex = fullData.size() - 1;
        ((AbstractTableModel) getModel()).fireTableRowsInserted(rowIndex, rowIndex);
        
        // Cache se estiver próximo da área visível
        if (Math.abs(rowIndex - firstVisibleRow) <= PREFETCH_ROWS) {
            rowCache.put(rowIndex, rowData);
        }
    }
    
    /**
     * Remove linha com performance otimizada
     */
    public void removeRow(int rowIndex) {
        if (rowIndex < fullData.size()) {
            fullData.remove(rowIndex);
            rowCache.remove(rowIndex);
            
            // Reindexa cache
            ConcurrentHashMap<Integer, Object[]> newCache = new ConcurrentHashMap<>();
            for (int i = 0; i < fullData.size(); i++) {
                Object[] cachedRow = rowCache.get(i + 1);
                if (cachedRow != null) {
                    newCache.put(i, cachedRow);
                }
            }
            rowCache = newCache;
            
            ((AbstractTableModel) getModel()).fireTableRowsDeleted(rowIndex, rowIndex);
        }
    }
    
    /**
     * Obtém estatísticas de performance
     */
    public String getPerformanceStats() {
        return String.format(
            "📊 VirtualTable Stats:\n" +
            "• Total Rows: %d\n" +
            "• Cached Rows: %d\n" +
            "• Visible Range: %d-%d\n" +
            "• Cache Hit Ratio: %.1f%%\n" +
            "• Memory Usage: ~%.1f MB",
            fullData.size(),
            rowCache.size(),
            firstVisibleRow,
            lastVisibleRow,
            rowCache.size() > 0 ? (double) rowCache.size() / (lastVisibleRow - firstVisibleRow + 1) * 100 : 0,
            (rowCache.size() * 8 * 50) / (1024.0 * 1024.0) // Estimativa
        );
    }
    
    /**
     * Limpa cache forçado
     */
    public void clearCache() {
        rowCache.clear();
        System.gc(); // Sugere garbage collection
    }
    
    /**
     * Factory para criar tabelas virtuais otimizadas
     */
    public static VirtualTableRenderer createOptimizedTable(String[] columnNames, List<Object[]> data) {
        VirtualTableRenderer table = new VirtualTableRenderer(columnNames, data);
        
        // Otimizações adicionais
        table.setShowHorizontalLines(false);
        table.setShowVerticalLines(false);
        table.setIntercellSpacing(new Dimension(0, 0));
        table.getTableHeader().setReorderingAllowed(false);
        
        return table;
    }
}
