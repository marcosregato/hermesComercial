package dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import Repository.RepositoryEstoque;
import connectionDB.ConnectionMySQL;
import model.Estoque;
import model.Produto;

public class EstoqueDao implements RepositoryEstoque{

	private ConnectionMySQL con = null;
	private Statement smt = null;
	private ResultSet rs = null;


	@Override
	public void salvar(Estoque estoque) {
		// TODO Auto-generated method stub
		try {
			con  = new ConnectionMySQL();
			String query ="INSERT INTO estoque (id, quantidade, maximo,minimo) VALUES (NULL, ?, ?, ?)";
			PreparedStatement ps = con.connection().prepareStatement(query);

			ps.setString(1, estoque.getQuantidade());
			ps.setInt(2, estoque.getMaximo());
			ps.setInt(3, estoque.getMinimo());

			ps.executeUpdate();
			ps.close();

		} catch (Exception e) {
			System.out.println(e.getMessage());
		}

	}

	@Override
	public void remove(String nome) {
		// TODO Auto-generated method stub
		try {

			con  = new ConnectionMySQL();
			String query = "DELETE FROM estoque WHERE nome=?";
			PreparedStatement ps = con.connection().prepareStatement(query);
			ps.setString(1, nome);
			ps.executeUpdate();
			rs.close();
			ps.close();

		} catch (Exception e) {
			System.out.println(e.getMessage());
		}

	}

	@Override
	public void update(Estoque estoque) {
		// TODO Auto-generated method stub
		try {

			con  = new ConnectionMySQL();
			String query = "update estoque set quantidade = ?,maximo = ?, minimo = ?";
			PreparedStatement ps = con.connection().prepareStatement(query);
			ps.setString(1, estoque.getQuantidade());
			ps.setInt(2, estoque.getMaximo());
			ps.setInt(3, estoque.getMinimo());

			rs.close();
			ps.close();

		} catch (Exception e) {
			System.out.println(e.getMessage());
		}

	}

	@Override
	public List<Estoque> listar() {
		try {

			con  = new ConnectionMySQL();
			String query ="select * from estoqur";
			List<Estoque> lista = new ArrayList<>();
			PreparedStatement ps = con.connection().prepareStatement(query);
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
			System.out.println(e.getMessage());
		}

		return null;

	}
	


	@Override
	public List<Estoque> buscar(String nome) {
		// TODO Auto-generated method stub
		/*try {

			String query = "SELECT * FROM produto "
					+ "inner join estoque e on p.id = e.fk_produto WHERE p.codigo = ?";
            PreparedStatement ps = con.connection().prepareStatement(query);
            ps.setString(1, nome);

            rs = ps.executeQuery();
            Produto produto = null;
            if (rs.next()) {

            	produto = new Produto();

            	produto.setNome(rs.getString("nome"));
            	//produto.setCustoTotal(rs.getFloat("custototal"));


            }

            rs.close();
            ps.close();
            return produto;

 

		} catch (Exception e) {
			System.out.println(e.getMessage());

		}*/
		return null;
	}


	


}
