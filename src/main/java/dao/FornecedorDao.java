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
import model.Fornecedor;

/**
 *
 * @author marcos
 */
public class FornecedorDao {
    
	private ConnectionPostgreSQL con = null;
    private Statement smt = null;
    private ResultSet rs = null;
    
     public void salvar(Fornecedor fornecedor){
        try {
            con  = new ConnectionPostgreSQL();
            String query ="INSERT INTO fornecedor (id, nome, tipofornecedor) VALUES (NULL, ?, ?)";
            PreparedStatement ps = con.connection().prepareStatement(query);

            ps.setString(1, fornecedor.getNome());
            ps.setString(2, fornecedor.getTipoFornecedor());

            ps.executeUpdate();
            ps.close();
            
        } catch (Exception e) {
             e.printStackTrace();
        }
    }
    
    public List<Fornecedor> listar(){
        try {
            con  = new ConnectionPostgreSQL();
            String query ="select * from fornecedor";
            List<Fornecedor> lista = new ArrayList<>();
            PreparedStatement ps = con.connection().prepareStatement(query);
            rs = ps.executeQuery();
            while (rs.next()) {
            	Fornecedor item = new Fornecedor();
            	item.setNome(rs.getString("nome"));
            	item.setTipoFornecedor(rs.getString("tipofornecedor"));

                lista.add(item);
            }

            return lista;
            
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    
    public void update(Fornecedor fornecedor){
        try {
            con  = new ConnectionPostgreSQL();
            String query = "update fornecedor set nome = ?,tipofornecedor = ? ";
            PreparedStatement ps = con.connection().prepareStatement(query);
            ps.setString(1, fornecedor.getNome());
            ps.setString(2, fornecedor.getTipoFornecedor());

            rs.close();
            ps.close();
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public void remove(String nome){
        try {
            con  = new ConnectionPostgreSQL();
            String query = "DELETE FROM fornecedor WHERE nome=?";
            PreparedStatement ps = con.connection().prepareStatement(query);
            ps.setString(1, nome);
            ps.executeUpdate();
            rs.close();
            ps.close();
        
        } catch (Exception e) {
             e.printStackTrace();
        }
    }
    
    public Fornecedor buscar(String nome){

        try {

            String query =  "SELECT * FROM fornecedor WHERE nome =?";
            PreparedStatement ps = con.connection().prepareStatement(query);
            ps.setString(1, nome);

            rs = ps.executeQuery();
            Fornecedor fornecedor = null;
            if (rs.next()) {

            	fornecedor = new Fornecedor();

            	fornecedor.setNome(rs.getString("nome"));
            	fornecedor.setTipoFornecedor(rs.getString("tipofornecedor"));

            }

            rs.close();
            ps.close();
            return fornecedor;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    
}
