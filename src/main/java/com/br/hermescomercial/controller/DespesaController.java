package com.br.hermescomercial.controller;

import com.br.hermescomercial.dao.DespesaDao;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import com.br.hermescomercial.model.Despesa;

public class DespesaController {

    @FXML
    private Button btCancelar;

    @FXML
    private Button btSalvar;

    @FXML
    private TextField txtNome;

    @FXML
    private TextField txtValor;

    @FXML
    private TextField txtData;

    DespesaDao dao = new DespesaDao();
    Despesa despesa;
    

    public void salvar( ){
        try {
        	despesa = new Despesa();
        	
        	
            dao.salvar(despesa);
        } catch (Exception e) {
            e.printStackTrace();

        }
    }

    public void remove( ){
        try {
            dao.remove(txtNome.getText());
        } catch (Exception e) {
            e.printStackTrace();

        }
    }

    public void update(Despesa despesa ){
        try {
            dao.update(despesa);
        } catch (Exception e) {
            e.printStackTrace();

        }
    }

    public void buscar(String nome ){
        try {
            dao.buscar(nome);
        } catch (Exception e) {
            e.printStackTrace();

        }
    }



    public void listar(){
        try {

            dao.listar();

        } catch (Exception e) {
            e.printStackTrace();

        }
    }



}
