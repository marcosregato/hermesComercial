package com.br.hermescomercial.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.br.hermescomercial.Repository.RepositoryEstoque;
import com.br.hermescomercial.connectionDB.ConnectionBD;
import com.br.hermescomercial.model.Estoque;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class EstoqueDao implements RepositoryEstoque{

	private ConnectionBD con = new ConnectionBD();
    private static final Logger logger = LogManager.getLogger(EstoqueDao.class);


    private Estoque mapResultSetToEstoque(ResultSet rs) throws SQLException {
        Estoque estoque = new Estoque();
        estoque.setId(rs.getLong("id"));
        estoque.setQuantidade(rs.getString("quantidade"));
        estoque.setMaximo(rs.getInt("maximo"));
        estoque.setMinimo(rs.getInt("minimo"));
        estoque.setLote(rs.getString("lote"));
        estoque.setDtVencimento(rs.getString("dtVencimento"));
        return estoque;
    }

	@Override
	public void salvar(Estoque estoque) {
        String query ="INSERT INTO estoque (quantidade, maximo, minimo, lote, dtvencimento) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement ps = con.getConnection("Postgres").prepareStatement(query)) {
			ps.setString(1, estoque.getQuantidade());
			ps.setInt(2, estoque.getMaximo());
			ps.setInt(3, estoque.getMinimo());
			ps.setString(4, estoque.getLote());
			ps.setString(5, estoque.getDtVencimento());
			ps.executeUpdate();
		} catch (Exception e) {
            logger.error("Erro ao salvar estoque: " + e.getMessage());
		}
	}

	@Override
	public void remove(String nome) {
        String query = "DELETE FROM estoque WHERE id IN (SELECT e.id FROM estoque e INNER JOIN produto p ON p.id = e.fk_produto WHERE p.nome = ?)";
        try (PreparedStatement ps = con.getConnection("Postgres").prepareStatement(query)) {
            ps.setString(1, nome);
			ps.executeUpdate();
		} catch (Exception e) {
            logger.error("Erro ao remover estoque: " + e.getMessage());
		}
	}

	@Override
	public void update(Estoque estoque) {
        String query = "UPDATE estoque SET quantidade = ?, maximo = ?, minimo = ?, lote = ?, dtvencimento = ? WHERE id = ?";
        try (PreparedStatement ps = con.getConnection("Postgres").prepareStatement(query)) {
			ps.setString(1, estoque.getQuantidade());
			ps.setInt(2, estoque.getMaximo());
			ps.setInt(3, estoque.getMinimo());
			ps.setString(4, estoque.getLote());
			ps.setString(5, estoque.getDtVencimento());
			ps.setLong(6, estoque.getId());
			ps.executeUpdate();
		} catch (Exception e) {
            logger.error("Erro ao atualizar estoque: " + e.getMessage());
		}
	}

	@Override
	public List<Estoque> listar() {
        String query ="SELECT * FROM estoque";
        List<Estoque> lista = new ArrayList<>();
        try (PreparedStatement ps = con.getConnection("Postgres").prepareStatement(query);
             ResultSet rs = ps.executeQuery()) {
			while (rs.next()) {
				lista.add(mapResultSetToEstoque(rs));
			}
			return lista;
		} catch (Exception e) {
            logger.error("Erro ao listar estoque: " + e.getMessage());
		}
		return Collections.emptyList();
	}

	@Override
	public List<Estoque> buscar(String nome) {
		List<Estoque> lista = new ArrayList<>();
        String query = "SELECT e.* FROM produto p "
                + "INNER JOIN estoque e ON p.id = e.fk_produto WHERE p.codigo = ?";
        try (PreparedStatement ps = con.getConnection("Postgres").prepareStatement(query)) {
            ps.setString(1, nome);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    lista.add(mapResultSetToEstoque(rs));
                }
            }
            return lista;
		} catch (Exception e) {
            logger.error("Erro ao buscar estoque: " + e.getMessage());
		}
		return lista;
	}

    public String getDataCompraEstoque() {
        String query = "SELECT data_compra FROM estoque ORDER BY data_compra DESC LIMIT 1";
        try (PreparedStatement ps = con.getConnection("Postgres").prepareStatement(query);
             ResultSet rs = ps.executeQuery()) {
            if (rs.next()) {
                return rs.getString("data_compra");
            }
        } catch (Exception e) {
            logger.error("Erro ao obter data de compra do estoque: " + e.getMessage());
        }
        return null;
    }
}
