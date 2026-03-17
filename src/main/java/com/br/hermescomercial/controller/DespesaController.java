package com.br.hermescomercial.controller;

import com.br.hermescomercial.dao.DespesaDao;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import com.br.hermescomercial.model.Despesa;
import org.apache.logging.log4j.LogManager;


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
    private static final org.apache.logging.log4j.Logger logger = LogManager.getLogger(DespesaController.class);

    public void salvar(){
        try {
            despesa = new Despesa();
            despesa.setNome(txtNome.getText());
            despesa.setValor(Float.parseFloat(txtValor.getText()));
            //despesa.setData(txtData.getText());

            dao.salvar(despesa);
        } catch (Exception e) {
            logger.error("Erro ao salvar Despesa Controller: " + e.getMessage());
        }
    }

    public void remove( ){
        try {
            dao.remove(txtNome.getText());
        } catch (Exception e) {
            logger.error("Erro ao remove Despesa Controller: " + e.getMessage());

        }
    }

    public void update(Despesa despesa ){
        try {
            if(despesa !=null ){
                dao.update(despesa);
            }
           
        } catch (Exception e) {
            logger.error("Erro ao update Despesa Controller: " + e.getMessage());

        }
    }

    public void buscar(String nome ){
        try {
            dao.buscar(nome);
        } catch (Exception e) {
            logger.error("Erro ao buscar Despesa Controller: " + e.getMessage());

        }
    }

    public void listar(){
        try {

            dao.listar();

        } catch (Exception e) {
            logger.error("Erro ao lista Despesa Controller: " + e.getMessage());

        }
    }

}
