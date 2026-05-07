package com.br.hermescomercial.util;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import java.util.ArrayList;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * Batch Processor para Operações em Massa
 * Processa múltiplas operações SQL em batch para performance máxima
 */
public class BatchProcessor {
    
    private static final int DEFAULT_BATCH_SIZE = 1000;
    private static final ExecutorService batchExecutor = Executors.newFixedThreadPool(4);
    
    /**
     * Processa inserções em batch
     */
    public static CompletableFuture<Integer> batchInsert(Connection conn, String sql, List<Object[]> batchData) {
        return CompletableFuture.supplyAsync(() -> {
            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                conn.setAutoCommit(false);
                
                int batchSize = Math.min(DEFAULT_BATCH_SIZE, batchData.size());
                int totalProcessed = 0;
                int batchCount = 0;
                
                for (Object[] params : batchData) {
                    // Set parameters
                    for (int i = 0; i < params.length; i++) {
                        pstmt.setObject(i + 1, params[i]);
                    }
                    
                    pstmt.addBatch();
                    batchCount++;
                    
                    // Execute batch quando atingir o tamanho limite
                    if (batchCount % batchSize == 0) {
                        int[] results = pstmt.executeBatch();
                        totalProcessed += results.length;
                        conn.commit();
                        
                        System.out.println("📦 Batch executado: " + results.length + " registros");
                    }
                }
                
                // Execute remaining batch
                if (batchCount % batchSize != 0) {
                    int[] results = pstmt.executeBatch();
                    totalProcessed += results.length;
                    conn.commit();
                }
                
                conn.setAutoCommit(true);
                return totalProcessed;
                
            } catch (SQLException e) {
                try {
                    conn.rollback();
                    conn.setAutoCommit(true);
                } catch (SQLException ex) {
                    System.err.println("Erro no rollback: " + ex.getMessage());
                }
                throw new RuntimeException("Erro no batch insert: " + e.getMessage(), e);
            }
        }, batchExecutor);
    }
    
    /**
     * Processa atualizações em batch
     */
    public static CompletableFuture<Integer> batchUpdate(Connection conn, String sql, List<Object[]> batchData) {
        return CompletableFuture.supplyAsync(() -> {
            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                conn.setAutoCommit(false);
                
                int batchSize = Math.min(DEFAULT_BATCH_SIZE, batchData.size());
                int totalProcessed = 0;
                
                for (Object[] params : batchData) {
                    for (int i = 0; i < params.length; i++) {
                        pstmt.setObject(i + 1, params[i]);
                    }
                    
                    pstmt.addBatch();
                    
                    if (totalProcessed % batchSize == 0) {
                        int[] results = pstmt.executeBatch();
                        conn.commit();
                    }
                    
                    totalProcessed++;
                }
                
                // Execute remaining batch
                int[] results = pstmt.executeBatch();
                conn.commit();
                conn.setAutoCommit(true);
                
                return totalProcessed;
                
            } catch (SQLException e) {
                try {
                    conn.rollback();
                    conn.setAutoCommit(true);
                } catch (SQLException ex) {
                    System.err.println("Erro no rollback: " + ex.getMessage());
                }
                throw new RuntimeException("Erro no batch update: " + e.getMessage(), e);
            }
        }, batchExecutor);
    }
    
    /**
     * Processa deleções em batch
     */
    public static CompletableFuture<Integer> batchDelete(Connection conn, String sql, List<Object> ids) {
        return CompletableFuture.supplyAsync(() -> {
            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                conn.setAutoCommit(false);
                
                int batchSize = Math.min(DEFAULT_BATCH_SIZE, ids.size());
                int totalProcessed = 0;
                
                for (Object id : ids) {
                    pstmt.setObject(1, id);
                    pstmt.addBatch();
                    
                    if (totalProcessed % batchSize == 0) {
                        int[] results = pstmt.executeBatch();
                        conn.commit();
                    }
                    
                    totalProcessed++;
                }
                
                // Execute remaining batch
                int[] results = pstmt.executeBatch();
                conn.commit();
                conn.setAutoCommit(true);
                
                return totalProcessed;
                
            } catch (SQLException e) {
                try {
                    conn.rollback();
                    conn.setAutoCommit(true);
                } catch (SQLException ex) {
                    System.err.println("Erro no rollback: " + ex.getMessage());
                }
                throw new RuntimeException("Erro no batch delete: " + e.getMessage(), e);
            }
        }, batchExecutor);
    }
    
    /**
     * Builder para operações batch complexas
     */
    public static class BatchBuilder {
        private Connection connection;
        private String operation;
        private List<Object[]> batchData = new ArrayList<>();
        private int batchSize = DEFAULT_BATCH_SIZE;
        
        public BatchBuilder(Connection conn, String operation) {
            this.connection = conn;
            this.operation = operation;
        }
        
        public BatchBuilder batchSize(int size) {
            this.batchSize = size;
            return this;
        }
        
        public BatchBuilder addData(Object... params) {
            batchData.add(params);
            return this;
        }
        
        public BatchBuilder addAllData(List<Object[]> data) {
            batchData.addAll(data);
            return this;
        }
        
        public CompletableFuture<Integer> execute() {
            if (operation.toLowerCase().contains("insert")) {
                return batchInsert(connection, operation, batchData);
            } else if (operation.toLowerCase().contains("update")) {
                return batchUpdate(connection, operation, batchData);
            } else if (operation.toLowerCase().contains("delete")) {
                List<Object> ids = new ArrayList<>();
                for (Object[] params : batchData) {
                    ids.add(params[0]);
                }
                return batchDelete(connection, operation, ids);
            } else {
                throw new IllegalArgumentException("Operação não suportada: " + operation);
            }
        }
    }
    
    /**
     * Factory method para BatchBuilder
     */
    public static BatchBuilder builder(Connection conn, String sql) {
        return new BatchBuilder(conn, sql);
    }
    
    /**
     * Processa múltiplos batches em paralelo
     */
    public static CompletableFuture<Void> processParallel(List<CompletableFuture<Integer>> batches) {
        return CompletableFuture.allOf(batches.toArray(new CompletableFuture[0]));
    }
    
    /**
     * Shutdown do executor
     */
    public static void shutdown() {
        batchExecutor.shutdown();
        try {
            if (!batchExecutor.awaitTermination(30, TimeUnit.SECONDS)) {
                batchExecutor.shutdownNow();
            }
        } catch (InterruptedException e) {
            batchExecutor.shutdownNow();
            Thread.currentThread().interrupt();
        }
    }
    
    /**
     * Obtém estatísticas do batch processor
     */
    public static String getStatistics() {
        return String.format(
            "📊 BatchProcessor Stats:\n" +
            "• ThreadPool Size: 4\n" +
            "• Default Batch Size: %d\n" +
            "• Executor Active: %s",
            DEFAULT_BATCH_SIZE,
            !batchExecutor.isShutdown()
        );
    }
}
