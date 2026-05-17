package com.br.hermescomercial.pdv.dao;

import com.br.hermescomercial.pdv.model.Venda;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Data Access Object para operações com vendas no banco PostgreSQL
 */
public class VendaDAO {
    
    /**
     * Busca todas as vendas
     * @return lista de vendas
     */
    public List<Venda> buscarTodas() {
        List<Venda> vendas = new ArrayList<>();
        String sql = "SELECT v.id, v.numero_venda, v.id_cliente, c.nome as cliente_nome, " +
                    "v.valor_total, v.valor_desconto, v.valor_final, v.forma_pagamento, " +
                    "v.status, v.data_venda, v.usuario_vendedor " +
                    "FROM hermes_pdv.vendas v " +
                    "LEFT JOIN hermes_pdv.clientes c ON v.id_cliente = c.id " +
                    "ORDER BY v.data_venda DESC";
        
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        
        try {
            conn = DatabaseConnection.getConnection();
            stmt = conn.prepareStatement(sql);
            rs = stmt.executeQuery();
            
            while (rs.next()) {
                Venda venda = new Venda();
                venda.setId(rs.getInt("id"));
                venda.setNumeroVenda(rs.getString("numero_venda"));
                venda.setIdCliente(rs.getInt("id_cliente"));
                venda.setClienteNome(rs.getString("cliente_nome"));
                venda.setValorTotal(rs.getBigDecimal("valor_total"));
                venda.setValorDesconto(rs.getBigDecimal("valor_desconto"));
                venda.setValorFinal(rs.getBigDecimal("valor_final"));
                venda.setFormaPagamento(rs.getString("forma_pagamento"));
                venda.setStatus(rs.getString("status"));
                venda.setDataVenda(rs.getTimestamp("data_venda"));
                venda.setUsuarioVendedor(rs.getString("usuario_vendedor"));
                
                vendas.add(venda);
            }
        } catch (SQLException e) {
            System.err.println("Erro ao buscar vendas: " + e.getMessage());
        } finally {
            closeResources(conn, stmt, rs);
        }
        
        return vendas;
    }
    
    /**
     * Busca vendas por período
     * @param dataInicio data inicial
     * @param dataFim data final
     * @return lista de vendas no período
     */
    public List<Venda> buscarPorPeriodo(Date dataInicio, Date dataFim) {
        List<Venda> vendas = new ArrayList<>();
        String sql = "SELECT v.id, v.numero_venda, v.id_cliente, c.nome as cliente_nome, " +
                    "v.valor_total, v.valor_desconto, v.valor_final, v.forma_pagamento, " +
                    "v.status, v.data_venda, v.usuario_vendedor " +
                    "FROM hermes_pdv.vendas v " +
                    "LEFT JOIN hermes_pdv.clientes c ON v.id_cliente = c.id " +
                    "WHERE v.data_venda BETWEEN ? AND ? " +
                    "ORDER BY v.data_venda DESC";
        
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        
        try {
            conn = DatabaseConnection.getConnection();
            stmt = conn.prepareStatement(sql);
            stmt.setTimestamp(1, new Timestamp(dataInicio.getTime()));
            stmt.setTimestamp(2, new Timestamp(dataFim.getTime()));
            rs = stmt.executeQuery();
            
            while (rs.next()) {
                Venda venda = new Venda();
                venda.setId(rs.getInt("id"));
                venda.setNumeroVenda(rs.getString("numero_venda"));
                venda.setIdCliente(rs.getInt("id_cliente"));
                venda.setClienteNome(rs.getString("cliente_nome"));
                venda.setValorTotal(rs.getBigDecimal("valor_total"));
                venda.setValorDesconto(rs.getBigDecimal("valor_desconto"));
                venda.setValorFinal(rs.getBigDecimal("valor_final"));
                venda.setFormaPagamento(rs.getString("forma_pagamento"));
                venda.setStatus(rs.getString("status"));
                venda.setDataVenda(rs.getTimestamp("data_venda"));
                venda.setUsuarioVendedor(rs.getString("usuario_vendedor"));
                
                vendas.add(venda);
            }
        } catch (SQLException e) {
            System.err.println("Erro ao buscar vendas por período: " + e.getMessage());
        } finally {
            closeResources(conn, stmt, rs);
        }
        
        return vendas;
    }
    
    /**
     * Insere uma nova venda
     * @param venda venda a ser inserida
     * @return id da venda inserida
     */
    public int inserir(Venda venda) {
        String sql = "INSERT INTO hermes_pdv.vendas (numero_venda, id_cliente, valor_total, " +
                    "valor_desconto, valor_final, forma_pagamento, status, usuario_vendedor) " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?, ?) RETURNING id";
        
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        
        try {
            conn = DatabaseConnection.getConnection();
            stmt = conn.prepareStatement(sql);
            
            stmt.setString(1, venda.getNumeroVenda());
            
            if (venda.getIdCliente() > 0) {
                stmt.setInt(2, venda.getIdCliente());
            } else {
                stmt.setNull(2, Types.INTEGER);
            }
            
            stmt.setBigDecimal(3, venda.getValorTotal());
            stmt.setBigDecimal(4, venda.getValorDesconto());
            stmt.setBigDecimal(5, venda.getValorFinal());
            stmt.setString(6, venda.getFormaPagamento());
            stmt.setString(7, venda.getStatus());
            stmt.setString(8, venda.getUsuarioVendedor());
            
            rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt("id");
            }
            
        } catch (SQLException e) {
            System.err.println("Erro ao inserir venda: " + e.getMessage());
            DatabaseConnection.rollback(conn);
        } finally {
            closeResources(conn, stmt, rs);
        }
        
        return -1;
    }
    
    /**
     * Atualiza uma venda existente
     * @param venda venda a ser atualizada
     * @return true se atualizado com sucesso
     */
    public boolean atualizar(Venda venda) {
        String sql = "UPDATE hermes_pdv.vendas SET id_cliente = ?, valor_total = ?, " +
                    "valor_desconto = ?, valor_final = ?, forma_pagamento = ?, " +
                    "status = ?, data_atualizacao = CURRENT_TIMESTAMP " +
                    "WHERE id = ?";
        
        Connection conn = null;
        PreparedStatement stmt = null;
        
        try {
            conn = DatabaseConnection.getConnection();
            stmt = conn.prepareStatement(sql);
            
            if (venda.getIdCliente() > 0) {
                stmt.setInt(1, venda.getIdCliente());
            } else {
                stmt.setNull(1, Types.INTEGER);
            }
            
            stmt.setBigDecimal(2, venda.getValorTotal());
            stmt.setBigDecimal(3, venda.getValorDesconto());
            stmt.setBigDecimal(4, venda.getValorFinal());
            stmt.setString(5, venda.getFormaPagamento());
            stmt.setString(6, venda.getStatus());
            stmt.setInt(7, venda.getId());
            
            int rowsAffected = stmt.executeUpdate();
            DatabaseConnection.commit(conn);
            
            return rowsAffected > 0;
            
        } catch (SQLException e) {
            System.err.println("Erro ao atualizar venda: " + e.getMessage());
            DatabaseConnection.rollback(conn);
            return false;
        } finally {
            closeResources(conn, stmt, null);
        }
    }
    
    /**
     * Exclui uma venda
     * @param id id da venda a ser excluída
     * @return true se excluída com sucesso
     */
    public boolean excluir(int id) {
        String sql = "DELETE FROM hermes_pdv.vendas WHERE id = ?";
        
        Connection conn = null;
        PreparedStatement stmt = null;
        
        try {
            conn = DatabaseConnection.getConnection();
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, id);
            
            int rowsAffected = stmt.executeUpdate();
            DatabaseConnection.commit(conn);
            
            return rowsAffected > 0;
            
        } catch (SQLException e) {
            System.err.println("Erro ao excluir venda: " + e.getMessage());
            DatabaseConnection.rollback(conn);
            return false;
        } finally {
            closeResources(conn, stmt, null);
        }
    }
    
    /**
     * Fecha os recursos do banco de dados
     */
    private void closeResources(Connection conn, PreparedStatement stmt, ResultSet rs) {
        try {
            if (rs != null) rs.close();
            if (stmt != null) stmt.close();
            if (conn != null) DatabaseConnection.closeConnection(conn);
        } catch (SQLException e) {
            System.err.println("Erro ao fechar recursos: " + e.getMessage());
        }
    }
}
