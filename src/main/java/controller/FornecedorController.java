/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controller;

import dao.FornecedorDao;
import model.Fornecedor;

/**
 *
 * @author marcos
 */
public class FornecedorController {
    
    FornecedorDao dao;
    
    public void salvar(Fornecedor fornecedor){
        try {
            dao.salvar(fornecedor);
        } catch (Exception e) {
            e.printStackTrace();
        
        }
    }
    
    public void listar(){
        try {
            dao.listFornecedor();
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
    
    public void update(Fornecedor fornecedor){
        try {
            dao.update(fornecedor);
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
