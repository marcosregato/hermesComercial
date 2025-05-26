/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.br.hermescomercial.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.br.hermescomercial.Repository.RepositoryProduto;
import com.br.hermescomercial.connectionDB.ConnectionPostgreSQL;
import com.br.hermescomercial.model.Produto;


/**
 *
 * @author marcos
 */
public class ProdutoDao implements RepositoryProduto{

	private final Statement smt = null;
	private ResultSet rs = null;
	@Override
	public void salvar(Produto produto) {
		try {
			ConnectionPostgreSQL con  = new ConnectionPostgreSQL();
			String query ="INSERT INTO produto (nome, categoria, subCategoria, codigo, marca,dataCompra) VALUES (?, ?, ?, ?, ?, ?)";
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
			System.err.println(e.getMessage());
		}

	}

	@Override
	public void remove(String nome) {

		try {
			ConnectionPostgreSQL con  = new ConnectionPostgreSQL();
			String query = "DELETE FROM produto WHERE nome=?";
			PreparedStatement ps = con.connection().prepareStatement(query);
			ps.setString(1, nome);
			ps.executeUpdate();
			ps.close();

		} catch (Exception e) {
			System.err.println(e.getMessage());
		}

	}

	@Override
	public void update(Produto produto) {

		try {
			ConnectionPostgreSQL con  = new ConnectionPostgreSQL();
			String query = "update produto set nome = ?,categoria = ? ,subCategoria = ?, codigo = ?, marca =?,dataCompra =? ";
			PreparedStatement ps = con.connection().prepareStatement(query);
			ps.setString(1, produto.getNome());
			ps.setString(2, produto.getCategoria());
			ps.setString(3, produto.getSubCategoria());
			ps.setString(4, produto.getCodigo());
			ps.setString(5, produto.getMarca());
			ps.setString(6, produto.getDataCompra());
			ps.close();

		} catch (Exception e) {
			System.err.println(e.getMessage());
		}
	}

	@Override
	public List<Produto> listar() {
		List<Produto> listProduto = new ArrayList<>();
		try {
			ConnectionPostgreSQL con  = new ConnectionPostgreSQL();
			String query ="select nome,categoria,subCategoria,codigo,marca,dataCompra from produto";

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

		} catch (Exception e) {
			System.err.println(e.getMessage());
		}
		return listProduto;
	}

	@Override
	public List<Produto> buscar(String nome) {
		List<Produto> lista = new ArrayList<>();
		try {
			ConnectionPostgreSQL con  = new ConnectionPostgreSQL();
			String query = "select nome,categoria,subCategoria,codigo,marca,dataCompra from produto where u.nome = ?";
			PreparedStatement buscar  = con.connection().prepareStatement(query);
			buscar.setString(1, nome);

			ResultSet resultadoBusca  = buscar.executeQuery();

			resultadoBusca.next();
			Produto produto = new Produto();
			produto.setNome(resultadoBusca.getString("nome"));
			produto.setCategoria(resultadoBusca.getString("categoria"));
			produto.setSubCategoria(resultadoBusca.getString("subCategoria"));
			produto.setCodigo(resultadoBusca.getString("codigo"));
			produto.setMarca(resultadoBusca.getString("marca"));
			produto.setDataCompra(resultadoBusca.getString("dataCompra"));
			lista.add(produto);
			resultadoBusca.close();
			buscar.close();

		} catch (Exception e) {
			System.err.println(e.getMessage());
		}
		return lista;
	}




}
