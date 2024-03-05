/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import Repository.RepositoryCusto;
import connectionDB.ConnectionMySQL;
import connectionDB.ConnectionPostgreSQL;
import model.Custo;

/**
 *
 * @author marcos
 */
public class CustoDao implements RepositoryCusto{

	private ConnectionMySQL con = null;
	private final Statement smt = null;
	private ResultSet rs = null;
	
	
	@Override
	public void salvar(Custo custo) {
		try {
			con  = new ConnectionMySQL();
			String query ="INSERT INTO custo (id, custounitario, custototal) VALUES (NULL, ?, ?)";
			PreparedStatement ps = con.connection().prepareStatement(query);

			// ps.setFloat(1, custo.getCustoUnitario());
			//ps.setFloat(2, custo.getCustoTotal());

			ps.executeUpdate();
			ps.close();

		} catch (Exception e) {
			System.out.println(e.getMessage());
		}

	}
	@Override
	public void remove(String nome) {
		try {
			con  = new ConnectionMySQL();
			String query = "DELETE FROM custo WHERE nome=?";
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
	public void update(Custo custo) {
		try {
			con  = new ConnectionMySQL();
			String query = "update custo set custounitario = ?,custototal = ?";
			PreparedStatement ps = con.connection().prepareStatement(query);
			//ps.setFloat(1, custo.getCustoUnitario());
			//ps.setFloat(2, custo.getCustoTotal());

			rs.close();
			ps.close();

		} catch (Exception e) {
			System.out.println(e.getMessage());
		}

	}
	@Override
	public List<Custo> listar() {
		try {
			con  = new ConnectionMySQL();
			String query ="select * from fornecedor";
			List<Custo> lista = new ArrayList<>();
			PreparedStatement ps = con.connection().prepareStatement(query);
			rs = ps.executeQuery();
			while (rs.next()) {
				Custo item = new Custo();
				//	item.setCustoUnitario(rs.getFloat("nome"));
				//	item.setCustoTotal(rs.getFloat("subproduto"));

				lista.add(item);
			}

			return lista;

		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		return null;
	}
	@Override
	public List<Custo> buscar(String nome) {
		/*try {

			String query =  "SELECT * FROM custo WHERE nome =?";
			PreparedStatement ps = con.connection().prepareStatement(query);
			ps.setString(1, nome);

			rs = ps.executeQuery();
			Custo custo = null;
			if (rs.next()) {

				custo = new Custo();

				//custo.setCustoUnitario(rs.getFloat("custounitario"));
				//custo.setCustoTotal(rs.getFloat("custototal"));


			}

			rs.close();
			ps.close();
			return custo;

		} catch (Exception e) {
			System.out.println(e.getMessage());
		}*/
		return null;
	}



}
