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

import connectionDB.ConnectionPostgreSQL;
import model.Produto;
import model.Usuario;

import java.sql.Connection;




/**
 *
 * @author marcos
 */
public class ProdutoDao {

    private ConnectionPostgreSQL con = null;
    private Statement smt = null;
    private ResultSet rs = null;
    
     public void salvar(Produto produto){
        try {
            con  = new ConnectionPostgreSQL();
            String query ="INSERT INTO produto (id, nome, subproduto, codigo, datacompra, codigoncm) VALUES (NULL, ?, ?, ?, ?, ?)";
            PreparedStatement ps = con.connection().prepareStatement(query);

            ps.setString(1, produto.getNome());
            ps.setString(2, produto.getSubProduto());
            ps.setString(3, produto.getCodigo());
            ps.setString(4, produto.getDataCompra());
            ps.setString(4, produto.getCodigoNcm());

            ps.executeUpdate();
            ps.close();
            
        } catch (Exception e) {
             e.printStackTrace();
        }
    }
    
    public List<Produto> listProduto(){
        try {
            con  = new ConnectionPostgreSQL();
            String query ="select * from produto";
            List<Produto> listProduto = new ArrayList<>();
            PreparedStatement ps = con.connection().prepareStatement(query);
            rs = ps.executeQuery();
            while (rs.next()) {
                Produto produto = new Produto();
                produto.setNome(rs.getString("nome"));
                produto.setSubProduto(rs.getString("subproduto"));
                produto.setCodigo(rs.getString("codigo"));
                produto.setDataCompra(rs.getString("datacompra"));
                produto.setCodigoNcm(rs.getString("codigoncm"));

                listProduto.add(produto);
            }

            return listProduto;
            
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    
    public void update(Produto produto){
        try {
            con  = new ConnectionPostgreSQL();
            String query = "update produto set nome = ?,subproduto = ? ,codigo = ?, datacompra = ?, codigoncm =?";
            PreparedStatement ps = con.connection().prepareStatement(query);
            ps.setString(1, produto.getNome());
            ps.setString(2, produto.getSubProduto());
            ps.setString(3, produto.getCodigo());
            ps.setString(4, produto.getDataCompra());
            ps.setString(5, produto.getCodigoNcm());
            rs.close();
            ps.close();
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public void remove(String nome){
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
    
    public Produto buscar(String nome){

        try {

            String query =  "SELECT * FROM produto WHERE nome =?";
            PreparedStatement ps = con.connection().prepareStatement(query);
            ps.setString(1, nome);

            rs = ps.executeQuery();
            Produto produto = null;
            if (rs.next()) {

                produto = new Produto();

                produto.setNome(rs.getString("nome"));
                produto.setSubProduto(rs.getString("subproduto"));
                produto.setCodigo(rs.getString("codigo"));
                produto.setDataCompra(rs.getString("datacompra"));
                produto.setCodigoNcm(rs.getString("codigoncm"));

            }

            rs.close();
            ps.close();
            return produto;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    
}
