package controller;

import dao.EstoqueDao;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import model.Estoque;

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

            dao = new EstoqueDao();
            dao.salvar(estoque);

        } catch (Exception e) {
            e.printStackTrace();

        }
    }

    public void listar(){
        try {

            dao =new EstoqueDao();
            dao.listar();

        } catch (Exception e) {
            e.printStackTrace();

        }

    }

    public void remove(){
        try {

            dao =new EstoqueDao();
           // dao.delete(txt);


        } catch (Exception e) {
            e.printStackTrace();

        }
    }

    public void update(){
        try {

        } catch (Exception e) {
            e.printStackTrace();

        }
    }

    public void buscar(){
        try {
        	
        	if(ComboCodigoProduto.getValue() != null) {
        		dao = new EstoqueDao();
                dao.buscar(ComboCodigoProduto.getValue());
        	}

            


        } catch (Exception e) {
            e.printStackTrace();

        }
    }

}
