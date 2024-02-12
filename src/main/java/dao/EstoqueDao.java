package dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import Repository.RepositoryEstoque;
import connectionDB.ConnectionPostgreSQL;
import model.Estoque;

public class EstoqueDao implements RepositoryEstoque{

	private ConnectionPostgreSQL con = null;
	private Statement smt = null;
	private ResultSet rs = null;
	
	
	@Override
	public void salvar(Estoque estoque) {
		// TODO Auto-generated method stub
		try {

		} catch (Exception e) {
			System.out.println(e.getMessage());
		}

	}
	@Override
	public void delete(String nome) {
		// TODO Auto-generated method stub
		try {

		} catch (Exception e) {
			System.out.println(e.getMessage());
		}

	}
	@Override
	public void update(Estoque produto) {
		// TODO Auto-generated method stub
		try {

		} catch (Exception e) {
			System.out.println(e.getMessage());
		}

	}
	@Override
	public List<Estoque> listar() {
		try {

			con  = new ConnectionPostgreSQL();
			String query ="select * from cliente";
			List<Estoque> lista = new ArrayList<>();
			PreparedStatement ps = con.connection().prepareStatement(query);
			rs = ps.executeQuery();
			while (rs.next()) {
				Estoque item = new Estoque();
				item.setQuantidade(rs.getString("quantidade"));


				lista.add(item);
			}

			return lista;

		} catch (Exception e) {
			System.out.println(e.getMessage());
		}

		return null;

	}
	@Override
	public List<Estoque> buscar() {
		// TODO Auto-generated method stub
		try {

		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		return null;
	}



}
