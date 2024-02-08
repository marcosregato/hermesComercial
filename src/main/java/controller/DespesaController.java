package controller;

import dao.DespesaDao;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import model.Custo;

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

    public void salvar( ){
        try {
            dao.salvar();
        } catch (Exception e) {
            e.printStackTrace();

        }
    }

    public void delete( ){
        try {
            dao.delete();
        } catch (Exception e) {
            e.printStackTrace();

        }
    }

    public void update( ){
        try {
            dao.update();
        } catch (Exception e) {
            e.printStackTrace();

        }
    }

    public void buscar( ){
        try {
            dao.buscar();
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
