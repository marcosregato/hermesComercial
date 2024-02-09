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

import Repository.RepositoryFornecedor;
import connectionDB.ConnectionMySQL;
import model.Fornecedor;

/**
 *
 * @author marcos
 */
public class FornecedorDao implements RepositoryFornecedor{
    
	private ConnectionMySQL con = null;
    private Statement smt = null;
    private ResultSet rs = null;
    
	@Override
	public void salvar(Fornecedor fornecedor) {
		// TODO Auto-generated method stub
		 try {
	            con  = new ConnectionMySQL();
	            String query ="INSERT INTO fornecedor (id, nome, tipofornecedor) VALUES (NULL, ?, ?)";
	            PreparedStatement ps = con.connection().prepareStatement(query);

	            ps.setString(1, fornecedor.getNome());
	            ps.setString(2, fornecedor.getTipoFornecedor());

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
            String query = "DELETE FROM fornecedor WHERE nome=?";
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
	public void update(Fornecedor fornecedor) {
	       try {
	            con  = new ConnectionMySQL();
	            String query = "update fornecedor set nome = ?,tipofornecedor = ? ";
	            PreparedStatement ps = con.connection().prepareStatement(query);
	            ps.setString(1, fornecedor.getNome());
	            ps.setString(2, fornecedor.getTipoFornecedor());

	            rs.close();
	            ps.close();
	            
	        } catch (Exception e) {
	        	System.out.println(e.getMessage());
	        }
		
	}
	@Override
	public List<Fornecedor> listar() {
		 try {
	            con  = new ConnectionMySQL();
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
	        	System.out.println(e.getMessage());
	        }
	        return null;
	}
	
	@Override
	public List<Fornecedor> buscar(String nome) {
		// TODO Auto-generated method stub
		try {
            con  = new ConnectionMySQL();
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
           System.out.println(e.getMessage());
        }
        return null;
	}
    
     
    
}
