package com.br.hermescomercial.pdv.dao;

import com.br.hermescomercial.shared.model.Notificacao;
import com.br.hermescomercial.connectionBD.ConnectionBD;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO para acesso a dados de notificações
 * Versão 2.2.0 - Sistema de notificações em tempo real
 */
public class NotificacaoDAO {
    
    public NotificacaoDAO() {
    }
    
    /**
     * Salva uma notificação no banco de dados
     */
    public com.br.hermescomercial.shared.model.Notificacao save(com.br.hermescomercial.shared.model.Notificacao notificacao) throws SQLException {
        // Verificar se estamos usando SQLite ou PostgreSQL
        com.br.hermescomercial.config.DatabaseConfig dbConfig = com.br.hermescomercial.config.DatabaseConfig.getInstance();
        boolean isSQLite = dbConfig.isSQLite();
        
        String sql = "INSERT INTO notificacao (titulo, mensagem, tipo, data_criacao, lida, usuario_destino, prioridade) " +
                   "VALUES (?, ?, ?, ?, ?, ?, ?)";
        
        try (Connection conn = ConnectionBD.getConnection()) {
            
            if (isSQLite) {
                // Para SQLite, não usar RETURN_GENERATED_KEYS
                try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                    
                    stmt.setString(1, notificacao.getTitulo());
                    stmt.setString(2, notificacao.getMensagem());
                    stmt.setString(3, notificacao.getTipo().name());
                    stmt.setTimestamp(4, Timestamp.valueOf(notificacao.getDataCriacao()));
                    stmt.setBoolean(5, notificacao.isLida());
                    stmt.setString(6, notificacao.getUsuarioDestino());
                    stmt.setString(7, notificacao.getPrioridade().name());
                    
                    int affectedRows = stmt.executeUpdate();
                    
                    if (affectedRows == 0) {
                        throw new SQLException("Falha ao criar notificação, nenhuma linha afetada.");
                    }
                    
                    // Para SQLite, buscar o último ID inserido manualmente
                    try (Statement idStmt = conn.createStatement();
                         ResultSet rs = idStmt.executeQuery("SELECT last_insert_rowid()")) {
                        if (rs.next()) {
                            notificacao.setId(rs.getLong(1));
                        }
                    }
                }
            } else {
                // Para PostgreSQL, usar RETURN_GENERATED_KEYS
                try (PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
                    
                    stmt.setString(1, notificacao.getTitulo());
                    stmt.setString(2, notificacao.getMensagem());
                    stmt.setString(3, notificacao.getTipo().name());
                    stmt.setTimestamp(4, Timestamp.valueOf(notificacao.getDataCriacao()));
                    stmt.setBoolean(5, notificacao.isLida());
                    stmt.setString(6, notificacao.getUsuarioDestino());
                    stmt.setString(7, notificacao.getPrioridade().name());
                    
                    int affectedRows = stmt.executeUpdate();
                    
                    if (affectedRows == 0) {
                        throw new SQLException("Falha ao criar notificação, nenhuma linha afetada.");
                    }
                    
                    try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                        if (generatedKeys.next()) {
                            notificacao.setId(generatedKeys.getLong(1));
                        }
                    }
                }
            }
        }
        
        return notificacao;
    }
    
    /**
     * Busca todas as notificações
     */
    public List<Notificacao> findAll() throws SQLException {
        String sql = "SELECT * FROM notificacao ORDER BY data_criacao DESC";
        List<Notificacao> notificacao = new ArrayList<>();
        
        try (Connection conn = ConnectionBD.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            
            while (rs.next()) {
                notificacao.add(mapResultSetToNotificacao(rs));
            }
        }
        
        return notificacao;
    }
    
    /**
     * Busca notificações não lidas
     */
    public List<Notificacao> findByNaoLidas() throws SQLException {
        String sql = "SELECT * FROM notificacao WHERE lida = false ORDER BY data_criacao DESC";
        List<Notificacao> notificacao = new ArrayList<>();
        
        try (Connection conn = ConnectionBD.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            
            while (rs.next()) {
                notificacao.add(mapResultSetToNotificacao(rs));
            }
        }
        
        return notificacao;
    }
    
    /**
     * Busca notificações por usuário
     */
    public List<Notificacao> findByUsuario(String usuario) throws SQLException {
        String sql = "SELECT * FROM notificacao WHERE usuario_destino = ? OR usuario_destino IS NULL ORDER BY data_criacao DESC";
        List<Notificacao> notificacao = new ArrayList<>();
        
        try (Connection conn = ConnectionBD.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, usuario);
            
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    notificacao.add(mapResultSetToNotificacao(rs));
                }
            }
        }
        
        return notificacao;
    }
    
    /**
     * Busca notificações por tipo
     */
    public List<Notificacao> findByTipo(Notificacao.TipoNotificacao tipo) throws SQLException {
        String sql = "SELECT * FROM notificacao WHERE tipo = ? ORDER BY data_criacao DESC";
        List<Notificacao> notificacao = new ArrayList<>();
        
        try (Connection conn = ConnectionBD.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, tipo.name());
            
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    notificacao.add(mapResultSetToNotificacao(rs));
                }
            }
        }
        
        return notificacao;
    }
    
    /**
     * Marca notificação como lida
     */
    public boolean markAsRead(Long id) throws SQLException {
        String sql = "UPDATE notificacao SET lida = true WHERE id = ?";
        
        try (Connection conn = ConnectionBD.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setLong(1, id);
            
            return stmt.executeUpdate() > 0;
        }
    }
    
    /**
     * Marca todas as notificações como lidas
     */
    public boolean markAllAsRead(String usuario) throws SQLException {
        String sql = "UPDATE notificacao SET lida = true WHERE usuario_destino = ? OR usuario_destino IS NULL";
        
        try (Connection conn = ConnectionBD.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, usuario);
            
            return stmt.executeUpdate() > 0;
        }
    }
    
    /**
     * Exclui notificações antigas (mais de 30 dias)
     */
    public int deleteAntigas() throws SQLException {
        String sql = "DELETE FROM notificacao WHERE data_criacao < ?";
        
        try (Connection conn = ConnectionBD.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setTimestamp(1, Timestamp.valueOf(LocalDateTime.now().minusDays(30)));
            
            return stmt.executeUpdate();
        }
    }
    
    /**
     * Conta notificações não lidas
     */
    public int countNaoLidas(String usuario) throws SQLException {
        String sql = "SELECT COUNT(*) FROM notificacao WHERE lida = false AND (usuario_destino = ? OR usuario_destino IS NULL)";
        
        try (Connection conn = ConnectionBD.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, usuario);
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
        }
        
        return 0;
    }
    
    /**
     * Mapeia ResultSet para Notificacao
     */
    private Notificacao mapResultSetToNotificacao(ResultSet rs) throws SQLException {
        Notificacao notificacao = new Notificacao();
        
        notificacao.setId(rs.getLong("id"));
        notificacao.setTitulo(rs.getString("titulo"));
        notificacao.setMensagem(rs.getString("mensagem"));
        notificacao.setTipo(Notificacao.TipoNotificacao.valueOf(rs.getString("tipo")));
        notificacao.setDataCriacao(rs.getTimestamp("data_criacao").toLocalDateTime());
        notificacao.setLida(rs.getBoolean("lida"));
        notificacao.setUsuarioDestino(rs.getString("usuario_destino"));
        notificacao.setPrioridade(Notificacao.PrioridadeNotificacao.valueOf(rs.getString("prioridade")));
        
        return notificacao;
    }
    
    /**
     * Cria tabela de notificações se não existir
     */
    public void createTableIfNotExists() throws SQLException {
        // Verificar se estamos usando SQLite ou PostgreSQL
        com.br.hermescomercial.config.DatabaseConfig dbConfig = com.br.hermescomercial.config.DatabaseConfig.getInstance();
        boolean isSQLite = dbConfig.isSQLite();
        
        String sql;
        if (isSQLite) {
            sql = """
                CREATE TABLE IF NOT EXISTS notificacao (
                    id INTEGER PRIMARY KEY AUTOINCREMENT,
                    titulo VARCHAR(255) NOT NULL,
                    mensagem TEXT NOT NULL,
                    tipo VARCHAR(50) NOT NULL,
                    data_criacao TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
                    lida BOOLEAN NOT NULL DEFAULT FALSE,
                    usuario_destino VARCHAR(100),
                    prioridade VARCHAR(50) NOT NULL DEFAULT 'MEDIA'
                )
                """;
        } else {
            sql = """
                CREATE TABLE IF NOT EXISTS notificacao (
                    id SERIAL PRIMARY KEY,
                    titulo VARCHAR(255) NOT NULL,
                    mensagem TEXT NOT NULL,
                    tipo VARCHAR(50) NOT NULL,
                    data_criacao TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
                    lida BOOLEAN NOT NULL DEFAULT FALSE,
                    usuario_destino VARCHAR(100),
                    prioridade VARCHAR(50) NOT NULL DEFAULT 'MEDIA'
                )
                """;
        }
        
        try (Connection conn = ConnectionBD.getConnection();
             Statement stmt = conn.createStatement()) {
            
            stmt.execute(sql);
        }
    }
}
