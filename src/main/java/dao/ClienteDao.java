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

import connectionDB.ConnectionMySQL;
import connectionDB.ConnectionPostgreSQL;
import model.Cliente;
import model.Custo;

/**
 *
 * @author marcos
 */
public class ClienteDao {
    
	private ConnectionMySQL con = null;
    private Statement smt = null;
    private ResultSet rs = null;
    
     public void salvar(Cliente cliente){
        try {
            con  = new ConnectionMySQL();
            String query ="INSERT INTO cliente (id, nome, subproduto, codigo, datacompra, codigoncm) VALUES (NULL, null, ?, ?, ?, ?)";
            PreparedStatement ps = con.connection().prepareStatement(query);

            ps.setInt(1, cliente.getCodigoCliente());
            ps.setString(2, cliente.getNome());
            ps.setString(3, cliente.getCnpj());
            ps.setString(4, cliente.getCpf());

            ps.executeUpdate();
            ps.close();
            
        } catch (Exception e) {
             e.printStackTrace();
        }
    }
    
    public List<Cliente> listCliente(){
        try {
            con  = new ConnectionMySQL();
            String query ="select * from cliente";
            List<Cliente> lista = new ArrayList<>();
            PreparedStatement ps = con.connection().prepareStatement(query);
            rs = ps.executeQuery();
            while (rs.next()) {
            	Cliente item = new Cliente();
            	item.setCodigoCliente(rs.getInt("codigocliente"));
            	item.setNome(rs.getString("nome"));
                item.setCnpj(rs.getString("cnpj"));
                item.setCpf(rs.getString("cpf"));

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
            con  = new ConnectionMySQL();
            String query = "update cliente set nome = ?,subproduto = ? ,codigo = ?, datacompra = ?, codigoncm =?";
            PreparedStatement ps = con.connection().prepareStatement(query);
            ps.setInt(1, cliente.getCodigoCliente());
            ps.setString(2, cliente.getNome());
            ps.setString(3, cliente.getCnpj());
            ps.setString(4, cliente.getCpf());
            
            rs.close();
            ps.close();
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public void remove(String nome){
        try {
            con  = new ConnectionMySQL();
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
                cliente.setCodigoCliente(rs.getInt("codigocliente"));
                cliente.setNome(rs.getString("nome"));
                cliente.setCnpj(rs.getString("cnpj"));
                cliente.setCpf(rs.getString("cpf"));

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
