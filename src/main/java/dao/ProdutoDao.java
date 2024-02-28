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

import Repository.RepositoryProduto;
import connectionDB.ConnectionPostgreSQL;
import model.Produto;




/**
 *
 * @author marcos
 */
public class ProdutoDao implements RepositoryProduto{

	private ConnectionPostgreSQL con = null;
	private Statement smt = null;
	private ResultSet rs = null;
	@Override
	public void salvar(Produto produto) {
		try {
			con  = new ConnectionPostgreSQL();
			String query ="INSERT INTO produto (id, nome, categoria, subCategoria, codigo, marca,dataCompra) VALUES (NULL, ?, ?, ?, ?, ?, ?)";
			PreparedStatement ps = con.connection().prepareStatement(query);

			ps.setString(1, produto.getNome());
			ps.setString(2, produto.getCategoria());
			ps.setString(3, produto.getSubCategoria());
			ps.setString(4, produto.getCodigo());
			ps.setString(5, produto.getMarca());
			ps.setString(6, produto.getDataCompra());

			ps.executeUpdate();
			ps.close();

		} catch (Exception e) {
			System.out.println(e.getMessage());
		}

	}

	@Override
	public void remove(String nome) {

		try {
			con  = new ConnectionPostgreSQL();
			String query = "DELETE FROM produto WHERE nome=?";
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
	public void update(Produto produto) {

		try {
			con  = new ConnectionPostgreSQL();
			String query = "update produto set nome = ?,categoria = ? ,subCategoria = ?, codigo = ?, marca =?,dataCompra =? ";
			PreparedStatement ps = con.connection().prepareStatement(query);
			ps.setString(1, produto.getNome());
			ps.setString(2, produto.getCategoria());
			ps.setString(3, produto.getSubCategoria());
			ps.setString(4, produto.getCodigo());
			ps.setString(5, produto.getMarca());
			ps.setString(6, produto.getDataCompra());
			rs.close();
			ps.close();

		} catch (Exception e) {
			System.out.println(e.getMessage());
		}

	}

	@Override
	public List<Produto> listar() {
		try {
			con  = new ConnectionPostgreSQL();
			String query ="select nome,categoria,subCategoria,codigo,marca,dataCompra from produto";
			List<Produto> listProduto = new ArrayList<>();
			PreparedStatement ps = con.connection().prepareStatement(query);
			rs = ps.executeQuery();
			while (rs.next()) {
				Produto produto = new Produto();
				produto.setNome(rs.getString("nome"));
				produto.setCategoria(rs.getString("categoria"));
				produto.setSubCategoria(rs.getString("subCategoria"));
				produto.setCodigo(rs.getString("codigo"));
				produto.setMarca(rs.getString("marca"));
				produto.setDataCompra(rs.getString("dataCompra"));

				listProduto.add(produto);
			}

			return listProduto;

		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		return null;
	}

	@Override
	public List<Produto> buscar(String nome) {
		// TODO Auto-generated method stub
		return null;
	}


}
