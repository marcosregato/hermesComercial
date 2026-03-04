package com.br.hermescomercial.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.br.hermescomercial.Repository.RepositoryEstoque;
import com.br.hermescomercial.connectionDB.ConnectionBD;
import com.br.hermescomercial.model.Estoque;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class EstoqueDao implements RepositoryEstoque{

	private ConnectionBD con = null;
	private ResultSet rs = null;
    private static final Logger logger = LogManager.getLogger(EstoqueDao.class);

	@Override
	public void salvar(Estoque estoque) {
		try {
			con  = new ConnectionBD();
			String query ="INSERT INTO estoque (quantidade, maximo,minimo) VALUES (?, ?, ?)";
			PreparedStatement ps = con.getConnection("").prepareStatement(query);

			ps.setString(1, estoque.getQuantidade());
			ps.setInt(2, estoque.getMaximo());
			ps.setInt(3, estoque.getMinimo());

			ps.executeUpdate();
			ps.close();

		} catch (Exception e) {
            logger.error(e.getMessage(), e);
		}

	}

	@Override
	public void remove(String nome) {
		try {

            con  = new ConnectionBD();
			String query = "DELETE FROM estoque WHERE nome=?";
			PreparedStatement ps = con.getConnection("").prepareStatement(query);
			ps.setString(1, nome);
			ps.executeUpdate();
			ps.close();

		} catch (Exception e) {
            logger.error(e.getMessage(), e);
		}

	}

	@Override
	public void update(Estoque estoque) {
		try {

            con  = new ConnectionBD();
			String query = "update estoque set quantidade = ?,maximo = ?, minimo = ?";
			PreparedStatement ps = con.getConnection("").prepareStatement(query);
			ps.setInt(2, estoque.getMaximo());
			ps.setInt(3, estoque.getMinimo());

			ps.close();

		} catch (Exception e) {
            logger.error(e.getMessage(), e);
		}

	}

	@Override
	public List<Estoque> listar() {
		try {

            con  = new ConnectionBD();
			String query ="select * from estoqur";
			List<Estoque> lista = new ArrayList<>();
			PreparedStatement ps = con.getConnection("").prepareStatement(query);
			rs = ps.executeQuery();
			while (rs.next()) {
				Estoque item = new Estoque();
				item.setQuantidade(rs.getString("quantidade"));
				item.setMaximo(rs.getInt("maximo"));
				item.setMinimo(rs.getInt("minimo"));

				lista.add(item);
			}

			return lista;

		} catch (Exception e) {
            logger.error(e.getMessage(), e);
		}
		return Collections.emptyList();
	}

	@Override
	public List<Estoque> buscar(String nome) {
        con  = new ConnectionBD();
		List<Estoque> lista = new ArrayList<>();
		Estoque estoque = null;
		try {

			String query = "SELECT * FROM produto "
					+ "inner join estoque e on p.id = e.fk_produto WHERE p.codigo = ?";
            PreparedStatement ps = con.getConnection("").prepareStatement(query);
            ps.setString(1, nome);
            rs = ps.executeQuery();

				estoque = new Estoque();
				estoque.setQuantidade(rs.getString("quantidade"));
				estoque.setMaximo(rs.getInt("maximo"));
				estoque.setMinimo(rs.getInt("minimo"));

				lista.add(estoque);
            ps.close();
		} catch (Exception e) {
            logger.error(e.getMessage(), e);
		}
		return lista;
	}

    public String getDataCompraEstoque() {
        try {

        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }

        return null;
    }
}
