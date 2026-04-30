/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.br.hermescomercial.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import java.util.ArrayList;
import java.util.List;

import com.br.hermescomercial.connectionBD.ConnectionBD;
import java.math.BigDecimal;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.br.hermescomercial.model.Produto;

/**
 *
 * @author marcos
 */
public class ProdutoDao {

    // private ConnectionBD con = new ConnectionBD(); - não utilizado
    private static final Logger logger = LogManager.getLogger(ProdutoDao.class);

    private Produto mapResultSetToProduto(ResultSet rs) throws SQLException {
        Produto produto = new Produto();
        // Usando coluna 'descricao' em vez de 'nome' que não existe na tabela
        produto.setNome(rs.getString("descricao"));
        produto.setCategoria(rs.getString("categoria"));
        produto.setCodigo(rs.getString("codigo"));
        produto.setMarca(rs.getString("observacoes")); // Usando observacoes como marca
        // Removida referência a subCategoria pois a coluna não existe no banco
        // produto.setSubCategoria(rs.getString("subCategoria"));
        // Removida referência a dataCompra pois a coluna não existe no banco
        // produto.setDataCompra(rs.getString("dataCompra"));
        return produto;
    }


    public boolean salvar(Produto produto) throws SQLException {
        String query ="INSERT INTO produto (codigo, descricao, preco, estoque, categoria, observacoes) VALUES (?, ?, ?, ?, ?, ?)";
        try (PreparedStatement ps = ConnectionBD.getConnection().prepareStatement(query)) {

            ps.setString(1, produto.getCodigo());
            ps.setString(2, produto.getNome());
            ps.setBigDecimal(3, new BigDecimal(String.valueOf(produto.getPreco())));
            ps.setInt(4, produto.getEstoque());
            ps.setString(5, produto.getCategoria());
            ps.setString(6, produto.getMarca());
            // Removida dataCompra pois a coluna não existe no banco
            // ps.setString(6, produto.getDataCompra()));

            ps.executeUpdate();
        } catch (Exception e) {
            System.err.println("Erro ao salvar produto: " + e.getMessage());
            return false;
        }
        return true;
    }

    public boolean remove(String nome) throws SQLException {
        String query = "DELETE FROM produto WHERE descricao=?";
        try (PreparedStatement ps = ConnectionBD.getConnection().prepareStatement(query)) {

            ps.setString(1, nome);
            ps.executeUpdate();

        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        return true;
    }

    public boolean update(Produto produto) throws SQLException {
        String query = "UPDATE produto SET categoria = ?, codigo = ?, observacoes = ? WHERE descricao = ?";
        try (PreparedStatement ps = ConnectionBD.getConnection().prepareStatement(query)) {
            ps.setString(1, produto.getCategoria());
            ps.setString(2, produto.getCodigo());
            ps.setString(3, produto.getMarca());
            ps.setString(4, produto.getNome());
            ps.executeUpdate();
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        return true;
    }

    public List<Produto> buscar(String nome) throws SQLException {
        String sql = "SELECT * FROM produto WHERE descricao LIKE ?";
        List<Produto> lista = new ArrayList<>();
        try (PreparedStatement ps = ConnectionBD.getConnection().prepareStatement(sql)) {
            ps.setString(1, "%" + nome + "%");
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Produto produto = mapResultSetToProduto(rs);
                    lista.add(produto);
                }
            }
        } catch (Exception e) {
            logger.error("Erro ao buscar produto: " + e.getMessage());
        }
        return lista;
    }

    public List<Produto> listar() {
        String sql = "SELECT * FROM produto";
        List<Produto> lista = new ArrayList<>();
        try (PreparedStatement ps = ConnectionBD.getConnection().prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                lista.add(mapResultSetToProduto(rs));
            }
        } catch (Exception e) {
            logger.error("Erro ao listar produtos: " + e.getMessage());
        }
        return lista;
    }

    // Métodos adicionais para compatibilidade
    public List<Produto> buscarComFiltros(String nome, String categoria, String subCategoria, 
                                         String codigoBarras, BigDecimal precoMin, BigDecimal precoMax, 
                                         Integer estoqueMin, boolean ativos, boolean inativos) {
        List<Produto> lista = new ArrayList<>();
        StringBuilder sql = new StringBuilder("SELECT * FROM produto WHERE 1=1");
        
        try (Connection conn = ConnectionBD.getConnection()) {
            if (nome != null && !nome.trim().isEmpty()) {
                sql.append(" AND nome LIKE ?");
            }
            if (categoria != null && !categoria.trim().isEmpty()) {
                sql.append(" AND categoria LIKE ?");
            }
            if (codigoBarras != null && !codigoBarras.trim().isEmpty()) {
                sql.append(" AND codigo LIKE ?");
            }
            if (precoMin != null) {
                sql.append(" AND preco_venda >= ?");
            }
            if (precoMax != null) {
                sql.append(" AND preco_venda <= ?");
            }
            if (estoqueMin != null) {
                sql.append(" AND estoque >= ?");
            }
            
            try (PreparedStatement ps = conn.prepareStatement(sql.toString())) {
                int paramIndex = 1;
                
                if (nome != null && !nome.trim().isEmpty()) {
                    ps.setString(paramIndex++, "%" + nome + "%");
                }
                if (categoria != null && !categoria.trim().isEmpty()) {
                    ps.setString(paramIndex++, "%" + categoria + "%");
                }
                if (codigoBarras != null && !codigoBarras.trim().isEmpty()) {
                    ps.setString(paramIndex++, "%" + codigoBarras + "%");
                }
                if (precoMin != null) {
                    ps.setBigDecimal(paramIndex++, precoMin);
                }
                if (precoMax != null) {
                    ps.setBigDecimal(paramIndex++, precoMax);
                }
                if (estoqueMin != null) {
                    ps.setInt(paramIndex++, estoqueMin);
                }
                
                try (ResultSet rs = ps.executeQuery()) {
                    while (rs.next()) {
                        lista.add(mapResultSetToProduto(rs));
                    }
                }
            }
        } catch (Exception e) {
            logger.error("Erro ao buscar produtos com filtros: " + e.getMessage(), e);
        }
        
        return lista;
    }

    public Produto buscarPorCodigoBarras(String codigoBarras) {
        String sql = "SELECT * FROM produto WHERE codigo = ? OR codigo_barras = ?";
        
        try (Connection conn = ConnectionBD.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setString(1, codigoBarras);
            ps.setString(2, codigoBarras);
            
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToProduto(rs);
                }
            }
        } catch (Exception e) {
            logger.error("Erro ao buscar produto por código de barras: " + e.getMessage(), e);
        }
        
        return null;
    }

}
