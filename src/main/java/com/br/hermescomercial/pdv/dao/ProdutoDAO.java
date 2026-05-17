package com.br.hermescomercial.pdv.dao;

import com.br.hermescomercial.pdv.model.Produto;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Data Access Object para operações com produtos no banco PostgreSQL
 */
public class ProdutoDAO {
    
    /**
     * Busca todos os produtos ativos
     * @return lista de produtos
     */
    public List<Produto> buscarTodos() {
        List<Produto> produtos = new ArrayList<>();
        String sql = "SELECT p.id, p.nome, p.descricao, p.codigo_barras, p.preco_venda, " +
                    "p.preco_custo, p.estoque_atual, p.estoque_minimo, p.unidade_medida, " +
                    "p.id_categoria, c.nome as categoria_nome, p.id_fornecedor, f.nome as fornecedor_nome " +
                    "FROM hermes_pdv.produtos p " +
                    "LEFT JOIN hermes_pdv.categorias c ON p.id_categoria = c.id " +
                    "LEFT JOIN hermes_pdv.fornecedores f ON p.id_fornecedor = f.id " +
                    "WHERE p.ativo = true " +
                    "ORDER BY p.nome";
        
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        
        try {
            conn = DatabaseConnection.getConnection();
            stmt = conn.prepareStatement(sql);
            rs = stmt.executeQuery();
            
            while (rs.next()) {
                Produto produto = new Produto();
                produto.setId((long) rs.getInt("id"));
                produto.setNome(rs.getString("nome"));
                produto.setDescricao(rs.getString("descricao"));
                produto.setCodigoBarras(rs.getString("codigo_barras"));
                produto.setPrecoVenda(rs.getBigDecimal("preco_venda"));
                produto.setPrecoCusto(rs.getBigDecimal("preco_custo"));
                produto.setEstoqueAtual(rs.getInt("estoque_atual"));
                produto.setEstoqueMinimo(rs.getInt("estoque_minimo"));
                produto.setUnidadeMedida(rs.getString("unidade_medida"));
                produto.setIdCategoria(rs.getInt("id_categoria"));
                produto.setCategoriaNome(rs.getString("categoria_nome"));
                produto.setIdFornecedor(rs.getInt("id_fornecedor"));
                produto.setFornecedorNome(rs.getString("fornecedor_nome"));
                
                produtos.add(produto);
            }
        } catch (SQLException e) {
            System.err.println("Erro ao buscar produtos: " + e.getMessage());
        } finally {
            closeResources(conn, stmt, rs);
        }
        
        return produtos;
    }
    
    /**
     * Busca produtos por nome (busca parcial)
     * @param nome parte do nome do produto
     * @return lista de produtos encontrados
     */
    public List<Produto> buscarPorNome(String nome) {
        List<Produto> produtos = new ArrayList<>();
        String sql = "SELECT p.id, p.nome, p.descricao, p.codigo_barras, p.preco_venda, " +
                    "p.preco_custo, p.estoque_atual, p.estoque_minimo, p.unidade_medida, " +
                    "p.id_categoria, c.nome as categoria_nome, p.id_fornecedor, f.nome as fornecedor_nome " +
                    "FROM hermes_pdv.produtos p " +
                    "LEFT JOIN hermes_pdv.categorias c ON p.id_categoria = c.id " +
                    "LEFT JOIN hermes_pdv.fornecedores f ON p.id_fornecedor = f.id " +
                    "WHERE p.ativo = true AND p.nome ILIKE ? " +
                    "ORDER BY p.nome";
        
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        
        try {
            conn = DatabaseConnection.getConnection();
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, "%" + nome + "%");
            rs = stmt.executeQuery();
            
            while (rs.next()) {
                Produto produto = new Produto();
                produto.setId((long) rs.getInt("id"));
                produto.setNome(rs.getString("nome"));
                produto.setDescricao(rs.getString("descricao"));
                produto.setCodigoBarras(rs.getString("codigo_barras"));
                produto.setPrecoVenda(rs.getBigDecimal("preco_venda"));
                produto.setPrecoCusto(rs.getBigDecimal("preco_custo"));
                produto.setEstoqueAtual(rs.getInt("estoque_atual"));
                produto.setEstoqueMinimo(rs.getInt("estoque_minimo"));
                produto.setUnidadeMedida(rs.getString("unidade_medida"));
                produto.setIdCategoria(rs.getInt("id_categoria"));
                produto.setCategoriaNome(rs.getString("categoria_nome"));
                produto.setIdFornecedor(rs.getInt("id_fornecedor"));
                produto.setFornecedorNome(rs.getString("fornecedor_nome"));
                
                produtos.add(produto);
            }
        } catch (SQLException e) {
            System.err.println("Erro ao buscar produtos por nome: " + e.getMessage());
        } finally {
            closeResources(conn, stmt, rs);
        }
        
        return produtos;
    }
    
    /**
     * Insere um novo produto
     * @param produto produto a ser inserido
     * @return id do produto inserido
     */
    public int inserir(Produto produto) {
        String sql = "INSERT INTO hermes_pdv.produtos (nome, descricao, codigo_barras, preco_venda, " +
                    "preco_custo, estoque_atual, estoque_minimo, unidade_medida, id_categoria, id_fornecedor) " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?) RETURNING id";
        
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        
        try {
            conn = DatabaseConnection.getConnection();
            stmt = conn.prepareStatement(sql);
            
            stmt.setString(1, produto.getNome());
            stmt.setString(2, produto.getDescricao());
            stmt.setString(3, produto.getCodigoBarras());
            stmt.setBigDecimal(4, produto.getPrecoVenda());
            stmt.setBigDecimal(5, produto.getPrecoCusto());
            stmt.setInt(6, produto.getEstoqueAtual());
            stmt.setInt(7, produto.getEstoqueMinimo());
            stmt.setString(8, produto.getUnidadeMedida());
            
            if (produto.getIdCategoria() > 0) {
                stmt.setInt(9, produto.getIdCategoria());
            } else {
                stmt.setNull(9, Types.INTEGER);
            }
            
            if (produto.getIdFornecedor() > 0) {
                stmt.setInt(10, produto.getIdFornecedor());
            } else {
                stmt.setNull(10, Types.INTEGER);
            }
            
            rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt("id");
            }
            
        } catch (SQLException e) {
            System.err.println("Erro ao inserir produto: " + e.getMessage());
            DatabaseConnection.rollback(conn);
        } finally {
            closeResources(conn, stmt, rs);
        }
        
        return -1;
    }
    
    /**
     * Atualiza um produto existente
     * @param produto produto a ser atualizado
     * @return true se atualizado com sucesso
     */
    public boolean atualizar(Produto produto) {
        String sql = "UPDATE hermes_pdv.produtos SET nome = ?, descricao = ?, codigo_barras = ?, " +
                    "preco_venda = ?, preco_custo = ?, estoque_atual = ?, estoque_minimo = ?, " +
                    "unidade_medida = ?, id_categoria = ?, id_fornecedor = ?, " +
                    "data_ultima_atualizacao = CURRENT_TIMESTAMP " +
                    "WHERE id = ?";
        
        Connection conn = null;
        PreparedStatement stmt = null;
        
        try {
            conn = DatabaseConnection.getConnection();
            stmt = conn.prepareStatement(sql);
            
            stmt.setString(1, produto.getNome());
            stmt.setString(2, produto.getDescricao());
            stmt.setString(3, produto.getCodigoBarras());
            stmt.setBigDecimal(4, produto.getPrecoVenda());
            stmt.setBigDecimal(5, produto.getPrecoCusto());
            stmt.setInt(6, produto.getEstoqueAtual());
            stmt.setInt(7, produto.getEstoqueMinimo());
            stmt.setString(8, produto.getUnidadeMedida());
            
            if (produto.getIdCategoria() > 0) {
                stmt.setInt(9, produto.getIdCategoria());
            } else {
                stmt.setNull(9, Types.INTEGER);
            }
            
            if (produto.getIdFornecedor() > 0) {
                stmt.setInt(10, produto.getIdFornecedor());
            } else {
                stmt.setNull(10, Types.INTEGER);
            }
            
            stmt.setInt(11, produto.getId().intValue());
            
            int rowsAffected = stmt.executeUpdate();
            DatabaseConnection.commit(conn);
            
            return rowsAffected > 0;
            
        } catch (SQLException e) {
            System.err.println("Erro ao atualizar produto: " + e.getMessage());
            DatabaseConnection.rollback(conn);
            return false;
        } finally {
            closeResources(conn, stmt, null);
        }
    }
    
    /**
     * Exclui (desativa) um produto
     * @param id id do produto a ser excluído
     * @return true se excluído com sucesso
     */
    public boolean excluir(int id) {
        String sql = "UPDATE hermes_pdv.produtos SET ativo = false, data_ultima_atualizacao = CURRENT_TIMESTAMP WHERE id = ?";
        
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
            System.err.println("Erro ao excluir produto: " + e.getMessage());
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
