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
	            String query ="INSERT INTO produto (id, nome, tipo, subTipo, codigo, codigoncm) VALUES (NULL, ?, ?, ?, ?, ?)";
	            PreparedStatement ps = con.connection().prepareStatement(query);

	            ps.setString(1, produto.getTipo());
	            ps.setString(2, produto.getSubTipo());
	            ps.setString(3, produto.getCodigo());
	            ps.setString(4, produto.getDataCompra());
	            ps.setString(4, produto.getCodigoNcm());

	            ps.executeUpdate();
	            ps.close();
	            
	        } catch (Exception e) {
	             e.printStackTrace();
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
             e.printStackTrace();
        }
		
	}

	@Override
	public void update(Produto produto) {
		
		try {
            con  = new ConnectionPostgreSQL();
            String query = "update produto set nome = ?,tipo = ? ,subTipo = ?, codigo = ?, codigoncm =?";
            PreparedStatement ps = con.connection().prepareStatement(query);
            ps.setString(1, produto.getNome());
            ps.setString(2, produto.getTipo());
            ps.setString(3, produto.getSubTipo());
            ps.setString(4, produto.getCodigo());
            ps.setString(5, produto.getCodigoNcm());
            rs.close();
            ps.close();
            
        } catch (Exception e) {
            e.printStackTrace();
        }
		
	}

	@Override
	public List<Produto> listar() {
		try {
            con  = new ConnectionPostgreSQL();
            String query ="select * from produto";
            List<Produto> listProduto = new ArrayList<>();
            PreparedStatement ps = con.connection().prepareStatement(query);
            rs = ps.executeQuery();
            while (rs.next()) {
                Produto produto = new Produto();
                produto.setNome(rs.getString("nome"));
                produto.setTipo(rs.getString("tipo"));
                produto.setSubTipo(rs.getString("subpTipo"));
                produto.setCodigo(rs.getString("codigo"));
                produto.setCodigoNcm(rs.getString("codigoncm"));

                listProduto.add(produto);
            }

            return listProduto;
            
        } catch (Exception e) {
            e.printStackTrace();
        }
		return null;
	}

	@Override
	public List<Produto> buscar(String nome) {
		// TODO Auto-generated method stub
		return null;
	}
	
    
}
