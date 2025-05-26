package com.br.hermescomercial.controller;

import com.br.hermescomercial.dao.EstoqueDao;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import com.br.hermescomercial.model.Estoque;

public class EstoqueController {



    @FXML
    private TextField txtEstMinimo;

    @FXML
    private TextField txtEstMaxima;

    @FXML
    private TextField txtEstQuantidade;

    @FXML
    private ComboBox<String> ComboCodigoProduto;

    @FXML
    private Button btPesqCodEstoque;

    EstoqueDao dao;
    Estoque estoque;

    public void salvar(){
        try {
            estoque = new Estoque();
            estoque.setQuantidade(txtEstQuantidade.getText());
            estoque.setMaximo(Integer.valueOf(txtEstMaxima.getText()));
            estoque.setMinimo(Integer.valueOf(txtEstMinimo.getText()));

            if(estoque !=null) {
            	dao = new EstoqueDao();
                dao.salvar(estoque);
            }
            
            //TODO colocar mensagem
            
        } catch (Exception e) {
            System.out.println(e.getMessage());

        }
    }

    public void remove(){
        try {

            dao =new EstoqueDao();

        } catch (Exception e) {
        	System.out.println(e.getMessage());

        }
    }

    public void update(){
        try {

        } catch (Exception e) {
        	System.out.println(e.getMessage());
        }
    }
    
    public void listar(){
        try {
        	
            dao =new EstoqueDao();
            dao.listar();

        } catch (Exception e) {
        	System.out.println(e.getMessage());
        }
    }

    public void buscar(){
        try {
        	
        	if(ComboCodigoProduto.getValue() != null) {
        		dao = new EstoqueDao();
                dao.buscar(ComboCodigoProduto.getValue());
        	}

        } catch (Exception e) {
        	System.out.println(e.getMessage());
        }
    }

}
