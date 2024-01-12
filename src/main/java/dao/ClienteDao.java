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
import model.Cliente;
import model.Custo;

/**
 *
 * @author marcos
 */
public class ClienteDao {
    
	private ConnectionPostgreSQL con = null;
    private Statement smt = null;
    private ResultSet rs = null;
    
     public void salvar(Cliente cliente){
        try {
            con  = new ConnectionPostgreSQL();
            String query ="INSERT INTO cliente (id, nome, subproduto, codigo, datacompra, codigoncm) VALUES (NULL, null, ?, ?, ?, ?)";
            PreparedStatement ps = con.connection().prepareStatement(query);

            ps.setFloat(1, cliente.getCustoUnitario());
            ps.setFloat(2, cliente.getCustoTotal());
            
            ps.executeUpdate();
            ps.close();
            
        } catch (Exception e) {
             e.printStackTrace();
        }
    }
    
    public List<Cliente> listFornecedor(){
        try {
            con  = new ConnectionPostgreSQL();
            String query ="select * from cliente";
            List<Custo> lista = new ArrayList<>();
            PreparedStatement ps = con.connection().prepareStatement(query);
            rs = ps.executeQuery();
            while (rs.next()) {
            	Custo item = new Custo();
            	item.setCustoUnitario(rs.getFloat("nome"));
            	item.setCustoTotal(rs.getFloat("subproduto"));
            	
                lista.add(item);
            }

            return lista;
            
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    
    public void update(Cliente cliente){
        try {
            con  = new ConnectionPostgreSQL();
            String query = "update cliente set nome = ?,subproduto = ? ,codigo = ?, datacompra = ?, codigoncm =?";
            PreparedStatement ps = con.connection().prepareStatement(query);
            ps.setFloat(1, cliente.getCustoUnitario());
            ps.setFloat(2, cliente.getCustoTotal());
            
            rs.close();
            ps.close();
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public void remove(String nome){
        try {
            con  = new ConnectionPostgreSQL();
            String query = "DELETE FROM cliente WHERE nome=?";
            PreparedStatement ps = con.connection().prepareStatement(query);
            ps.setString(1, nome);
            ps.executeUpdate();
            rs.close();
            ps.close();
        
        } catch (Exception e) {
             e.printStackTrace();
        }
    }
    
    public Cliente buscar(String nome){

        try {

            String query =  "SELECT * FROM cliente WHERE nome =?";
            PreparedStatement ps = con.connection().prepareStatement(query);
            ps.setString(1, nome);

            rs = ps.executeQuery();
            Cliente cliente = null;
            if (rs.next()) {

            	cliente = new Cliente();

            	cliente.setCustoUnitario(rs.getFloat("custounitario"));
            	cliente.setCustoTotal(rs.getFloat("custototal"));
            

            }

            rs.close();
            ps.close();
            return cliente;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    
    
}
