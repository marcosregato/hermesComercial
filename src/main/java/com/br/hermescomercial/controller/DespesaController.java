package com.br.hermescomercial.controller;

import com.br.hermescomercial.dao.DespesaDao;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import com.br.hermescomercial.model.Despesa;
import org.apache.log4j.Logger;

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
    Logger logger = Logger.getLogger(getClass().getName());

    public void salvar( ){
        try {
        	despesa = new Despesa();
        	
        	
            dao.salvar(despesa);
        } catch (Exception e) {
           logger.info(e.getMessage());

        }
    }

    public void remove( ){
        try {
            dao.remove(txtNome.getText());
        } catch (Exception e) {
           logger.info(e.getMessage());

        }
    }

    public void update(Despesa despesa ){
        try {
            dao.update(despesa);
        } catch (Exception e) {
           logger.info(e.getMessage());

        }
    }

    public void buscar(String nome ){
        try {
            dao.buscar(nome);
        } catch (Exception e) {
           logger.info(e.getMessage());

        }
    }

    public void listar(){
        try {

            dao.listar();

        } catch (Exception e) {
           logger.info(e.getMessage());

        }
    }

}
