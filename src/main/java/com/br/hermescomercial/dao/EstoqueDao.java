package com.br.hermescomercial.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.br.hermescomercial.Repository.RepositoryEstoque;
import com.br.hermescomercial.connectionDB.ConnectionSQLite;
import com.br.hermescomercial.model.Estoque;

public class EstoqueDao implements RepositoryEstoque{

	private ConnectionSQLite con = null;
	private final Statement smt = null;
	private ResultSet rs = null;


	@Override
	public void salvar(Estoque estoque) {
		// TODO Auto-generated method stub
		try {
			con  = new ConnectionSQLite();
			String query ="INSERT INTO estoque (quantidade, maximo,minimo) VALUES (?, ?, ?)";
			PreparedStatement ps = con.getConnection().prepareStatement(query);

			ps.setString(1, estoque.getQuantidade());
			ps.setInt(2, estoque.getMaximo());
			ps.setInt(3, estoque.getMinimo());

			ps.executeUpdate();
			ps.close();

		} catch (Exception e) {
			System.err.println(e.getMessage());
		}

	}

	@Override
	public void remove(String nome) {
		// TODO Auto-generated method stub
		try {

			con  = new ConnectionSQLite();
			String query = "DELETE FROM estoque WHERE nome=?";
			PreparedStatement ps = con.getConnection().prepareStatement(query);
			ps.setString(1, nome);
			ps.executeUpdate();
			rs.close();
			ps.close();

		} catch (Exception e) {
			System.err.println(e.getMessage());
		}

	}

	@Override
	public void update(Estoque estoque) {
		// TODO Auto-generated method stub
		try {

			con  = new ConnectionSQLite();
			String query = "update estoque set quantidade = ?,maximo = ?, minimo = ?";
			PreparedStatement ps = con.getConnection().prepareStatement(query);
			//ps.setString(1, estoque.getId());
			ps.setInt(2, estoque.getMaximo());
			ps.setInt(3, estoque.getMinimo());

			rs.close();
			ps.close();

		} catch (Exception e) {
			System.err.println(e.getMessage());
		}

	}

	@Override
	public List<Estoque> listar() {
		try {

			con  = new ConnectionSQLite();
			String query ="select * from estoqur";
			List<Estoque> lista = new ArrayList<>();
			PreparedStatement ps = con.getConnection().prepareStatement(query);
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
			System.err.println(e.getMessage());
		}
		return null;
	}

	@Override
	public List<Estoque> buscar(String nome) {

		List<Estoque> lista = new ArrayList<>();
		Estoque estoque = null;
		try {

			String query = "SELECT * FROM produto "
					+ "inner join estoque e on p.id = e.fk_produto WHERE p.codigo = ?";
            PreparedStatement ps = con.getConnection().prepareStatement(query);
            ps.setString(1, nome);
            rs = ps.executeQuery();

				estoque = new Estoque();
				estoque.setQuantidade(rs.getString("quantidade"));
				estoque.setMaximo(rs.getInt("maximo"));
				estoque.setMinimo(rs.getInt("minimo"));

				lista.add(estoque);
            rs.close();
            ps.close();
		} catch (Exception e) {
			System.err.println(e.getMessage());
		}
		return lista;
	}
}
