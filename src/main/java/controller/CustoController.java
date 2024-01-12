/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controller;

import dao.CustoDao;
import model.Custo;

/**
 *
 * @author marcos
 */
public class CustoController {
    
    CustoDao dao;
    
    public void salvar(Custo custo){
        try {
            dao.salvar(custo);
        } catch (Exception e) {
            e.printStackTrace();
        
        }
    }
    
    public void listar(){
        try {
            
            dao.listCusto();
            
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
    
    public void update(Custo custo){
        try {
            dao.update(custo);
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
