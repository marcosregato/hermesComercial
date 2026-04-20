package com.br.hermescomercial.dao;

import com.br.hermescomercial.connectionBD.ConnectionBD;
import com.br.hermescomercial.model.ItemVenda;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ItemVendaDao {
    
    private static final Logger logger = LogManager.getLogger(ItemVendaDao.class);
    
    private static final String INSERT_SQL = 
        "INSERT INTO itens_venda (id_venda, id_produto, quantidade, valor_unitario, valor_total, desconto, valor_final, observacao, data_cadastro) " +
        "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
    
    private static final String UPDATE_SQL = 
        "UPDATE itens_venda SET id_produto = ?, quantidade = ?, valor_unitario = ?, valor_total = ?, desconto = ?, valor_final = ?, observacao = ? " +
        "WHERE id = ?";
    
    private static final String DELETE_SQL = "DELETE FROM itens_venda WHERE id = ?";
    private static final String SELECT_BY_ID_SQL = "SELECT * FROM itens_venda WHERE id = ?";
    private static final String SELECT_BY_VENDA_SQL = "SELECT * FROM itens_venda WHERE id_venda = ? ORDER BY id";
    private static final String SELECT_ALL_SQL = "SELECT * FROM itens_venda ORDER BY data_cadastro DESC";

    public boolean salvar(ItemVenda itemVenda) {
        try {
            if (itemVenda.getId() == null) {
                return inserir(itemVenda);
            } else {
                return atualizar(itemVenda);
            }
        } catch (Exception e) {
            logger.error("Erro ao salvar item venda: " + e.getMessage(), e);
            return false;
        }
    }

    private boolean inserir(ItemVenda itemVenda) {
        try (Connection conn = ConnectionBD.getConnection();
             PreparedStatement stmt = conn.prepareStatement(INSERT_SQL, Statement.RETURN_GENERATED_KEYS)) {
            
            stmt.setLong(1, itemVenda.getIdVenda());
            stmt.setLong(2, itemVenda.getIdProduto());
            stmt.setInt(3, itemVenda.getQuantidade());
            stmt.setBigDecimal(4, itemVenda.getValorUnitario());
            stmt.setBigDecimal(5, itemVenda.getValorTotal());
            stmt.setBigDecimal(6, itemVenda.getDesconto());
            stmt.setBigDecimal(7, itemVenda.getValorFinal());
            stmt.setString(8, itemVenda.getObservacao());
            stmt.setTimestamp(9, Timestamp.valueOf(itemVenda.getDataCadastro()));
            
            int result = stmt.executeUpdate();
            
            if (result > 0) {
                try (ResultSet rs = stmt.getGeneratedKeys()) {
                    if (rs.next()) {
                        itemVenda.setId(rs.getLong(1));
                    }
                }
                return true;
            }
            
        } catch (SQLException e) {
            logger.error("Erro ao inserir item venda: " + e.getMessage(), e);
        }
        
        return false;
    }

    private boolean atualizar(ItemVenda itemVenda) {
        try (Connection conn = ConnectionBD.getConnection();
             PreparedStatement stmt = conn.prepareStatement(UPDATE_SQL)) {
            
            stmt.setLong(1, itemVenda.getIdProduto());
            stmt.setInt(2, itemVenda.getQuantidade());
            stmt.setBigDecimal(3, itemVenda.getValorUnitario());
            stmt.setBigDecimal(4, itemVenda.getValorTotal());
            stmt.setBigDecimal(5, itemVenda.getDesconto());
            stmt.setBigDecimal(6, itemVenda.getValorFinal());
            stmt.setString(7, itemVenda.getObservacao());
            stmt.setLong(8, itemVenda.getId());
            
            return stmt.executeUpdate() > 0;
            
        } catch (SQLException e) {
            logger.error("Erro ao atualizar item venda: " + e.getMessage(), e);
        }
        
        return false;
    }

    public boolean remover(Long id) {
        try (Connection conn = ConnectionBD.getConnection();
             PreparedStatement stmt = conn.prepareStatement(DELETE_SQL)) {
            
            stmt.setLong(1, id);
            
            return stmt.executeUpdate() > 0;
            
        } catch (SQLException e) {
            logger.error("Erro ao remover item venda: " + e.getMessage(), e);
        }
        
        return false;
    }

    public ItemVenda buscar(Long id) {
        try (Connection conn = ConnectionBD.getConnection();
             PreparedStatement stmt = conn.prepareStatement(SELECT_BY_ID_SQL)) {
            
            stmt.setLong(1, id);
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return extrairItemVenda(rs);
                }
            }
            
        } catch (SQLException e) {
            logger.error("Erro ao buscar item venda: " + e.getMessage(), e);
        }
        
        return null;
    }

    public List<ItemVenda> buscarPorVenda(Long idVenda) {
        List<ItemVenda> itens = new ArrayList<>();
        
        try (Connection conn = ConnectionBD.getConnection();
             PreparedStatement stmt = conn.prepareStatement(SELECT_BY_VENDA_SQL)) {
            
            stmt.setLong(1, idVenda);
            
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    itens.add(extrairItemVenda(rs));
                }
            }
            
        } catch (SQLException e) {
            logger.error("Erro ao buscar itens por venda: " + e.getMessage(), e);
        }
        
        return itens;
    }

    public List<ItemVenda> listar() {
        List<ItemVenda> itens = new ArrayList<>();
        
        try (Connection conn = ConnectionBD.getConnection();
             PreparedStatement stmt = conn.prepareStatement(SELECT_ALL_SQL);
             ResultSet rs = stmt.executeQuery()) {
            
            while (rs.next()) {
                itens.add(extrairItemVenda(rs));
            }
            
        } catch (SQLException e) {
            logger.error("Erro ao listar itens venda: " + e.getMessage(), e);
        }
        
        return itens;
    }

    private ItemVenda extrairItemVenda(ResultSet rs) throws SQLException {
        ItemVenda item = new ItemVenda();
        
        item.setId(rs.getLong("id"));
        item.setIdVenda(rs.getLong("id_venda"));
        item.setIdProduto(rs.getLong("id_produto"));
        item.setQuantidade(rs.getInt("quantidade"));
        item.setValorUnitario(rs.getBigDecimal("valor_unitario"));
        item.setValorTotal(rs.getBigDecimal("valor_total"));
        item.setDesconto(rs.getBigDecimal("desconto"));
        item.setValorFinal(rs.getBigDecimal("valor_final"));
        item.setObservacao(rs.getString("observacao"));
        
        Timestamp dataCadastro = rs.getTimestamp("data_cadastro");
        if (dataCadastro != null) {
            item.setDataCadastro(dataCadastro.toLocalDateTime());
        }
        
        return item;
    }

    // Métodos auxiliares para compatibilidade com o modelo existente
    // private void setIdVenda(Long idVenda) - método não utilizado
    // private Long getIdVenda() - método não utilizado
    // private void setIdProduto(Long idProduto) - método não utilizado
    // private Long getIdProduto() - método não utilizado
}
