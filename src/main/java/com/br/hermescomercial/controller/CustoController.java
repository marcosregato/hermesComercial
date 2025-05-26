/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.br.hermescomercial.controller;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.br.hermescomercial.connectionDB.ConnectionMySQL;
import com.br.hermescomercial.connectionDB.ConnectionSQLite;
import com.br.hermescomercial.model.Atributo;
import com.br.hermescomercial.model.Custo;

/**
 *
 * @author marcos
 */
public class CustoController {

    private ConnectionSQLite con = null;
    private final Statement smt = null;
    private ResultSet rs = null;
    
    public void salvar(Custo custo){
        try {

            con  = new ConnectionSQLite();
            String query ="INSERT INTO fornecedor (id, nome, tipofornecedor) VALUES (NULL, ?, ?)";
            PreparedStatement ps = con.getConnection().prepareStatement(query);

            //ps.setString(1, fornecedor.getNome());
            //ps.setString(2, fornecedor.getTipoFornecedor());

            ps.executeUpdate();
            ps.close();

        } catch (Exception e) {
        	System.out.println(e.getMessage());
        
        }
    }
    
    public List<Atributo> listar(){
        try {

            con  = new ConnectionSQLite();
            String query ="select * from fornecedor";
            List<Atributo> lista = new ArrayList<>();
            PreparedStatement ps = con.getConnection().prepareStatement(query);
            rs = ps.executeQuery();
            while (rs.next()) {
                Atributo item = new Atributo();
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
    
    public void remove(String nome){
        try {

            con  = new ConnectionSQLite();
            String query = "DELETE FROM custo WHERE nome=?";
            PreparedStatement ps = con.getConnection().prepareStatement(query);
            ps.setString(1, nome);
            ps.executeUpdate();
            rs.close();
            ps.close();


        } catch (Exception e) {
        	System.out.println(e.getMessage());
        
        }
    }
    
    public void update(Custo custo){
        try {

            con  = new ConnectionSQLite();
            String query = "update custo set custounitario = ?,custototal = ?";
            PreparedStatement ps = con.getConnection().prepareStatement(query);
            //ps.setFloat(1, custo.getCustoUnitario());
            //ps.setFloat(2, custo.getCustoTotal());

            rs.close();
            ps.close();


        } catch (Exception e) {
        	System.out.println(e.getMessage());
        
        }
    }
    
    public List<Custo> buscar(String nome){
        try {

            /*            String query =  "SELECT * FROM custo WHERE nome =?";
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
            return atributo;

 */


        } catch (Exception e) {
        	System.out.println(e.getMessage());
        
        }

        return null;
    }
    
}
