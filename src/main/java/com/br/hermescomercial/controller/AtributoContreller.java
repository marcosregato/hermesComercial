package com.br.hermescomercial.controller;

import com.br.hermescomercial.dao.AtributoDao;
import com.br.hermescomercial.model.Atributo;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import org.apache.logging.log4j.LogManager;

import java.util.List;


public class AtributoContreller {

    @FXML
    private ComboBox<String> comboCodigo;

    @FXML
    private Button btPesqCod;

    @FXML
    private TextField txtImpostoFederal;

    @FXML
    private TextField txtImpostoEstadual;

    @FXML
    private TextField txtImpostoMunicipal;

    @FXML
    private TextField txtPrecoCusto;

    @FXML
    private TextField txtPrecoPrazo;

    @FXML
    private TextField txtPrecoVista;

    @FXML
    private TextField txtPrecoDesconto;

    @FXML
    private Button btSalvar;

    AtributoDao dao;

    private static final org.apache.logging.log4j.Logger logger = LogManager.getLogger(AtributoContreller.class);

    public Boolean salvar(Atributo atributo){
        try {
            if(atributo != null){
                dao.salvar(atributo);
                return true;
            }
            return false;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<Atributo> listar(){
        try {

            return dao.listar();


        } catch (Exception e) {
            e.printStackTrace();

        }
        return null;

    }

    public void remove(String nome){
        try {
            dao.remove(nome);

        } catch (Exception e) {
            e.printStackTrace();

        }
    }

    public void update(Atributo atributo){
        try {
            dao.update(atributo);

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
