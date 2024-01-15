/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controller;

import connectionDB.ConnectionPostgreSQL;
import dao.ClienteDao;
import model.Cliente;

import java.sql.Connection;
import java.sql.Statement;


public class ClienteController {

    private Connection con = null;
    private Statement smt = null;

    
    ClienteDao dao;
    
    public void salvar(Cliente cliente){
        try {


            dao.salvar(cliente);
            
        } catch (Exception e) {
            e.printStackTrace();
        
        }
    }
    
    public void listar(){
        try {
            dao.listCliente();
        } catch (Exception e) {
            e.printStackTrace();
        
        }
    }
    
    public void remove(String nome){
        try {
            
            dao.remove(nome);
        } catch (Exception e) {
            e.printStackTrace();
        
        }
    }
    
    public void update(Cliente cliente){
        try {
            dao.update(cliente);
        } catch (Exception e) {
            e.printStackTrace();
        
        }
    }
    
    public void buscar(String nome){
        try {
            dao.buscar(nome);
            
        } catch (Exception e) {
            e.printStackTrace();
        
        }
    }
    
}
