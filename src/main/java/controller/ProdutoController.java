/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controller;

import dao.ProdutoDao;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.Pane;
import model.Produto;
import util.Alerta;
import util.ValidarCampo;

/**
 *
 * @author marcos
 */
public class ProdutoController {


    @FXML
    private Button btBuscar;



    @FXML
    private TableColumn<Produto, String> colDesconto;

    @FXML
    private TableColumn<Produto, String> colNome;

    @FXML
    private TableColumn<Produto, String> colQuantidade;

    @FXML
    private TableColumn<Produto,String> colTipo;

    @FXML
    private TableColumn<Produto, String> colValor;

    @FXML
    private ComboBox<?> comboFornecedor;

    @FXML
    private Pane painel;

    @FXML
    private Pane painel2;

    @FXML
    private TableView<?> tabelaProduto;

    @FXML
    private TextField txtNome;
    
    ProdutoDao dao;
    Alerta alerta;

    
    public void salvar(Produto produto){
        try {
            alerta = new Alerta();

            if(produto.getNome().isEmpty()) {
                alerta.showAlert(Alert.AlertType.ERROR, alerta.createRegistrationFormPane().getScene().getWindow(), "Form Error!", "Digite o nome do produto");
                return;
            }

            if(produto.getSubProduto().isEmpty()) {
                alerta.showAlert(Alert.AlertType.ERROR, alerta.createRegistrationFormPane().getScene().getWindow(), "Form Error!", "Digite o sub produto");
                return;
            }

            if(produto.getCodigo().isEmpty()) {
                alerta.showAlert(Alert.AlertType.ERROR, alerta.createRegistrationFormPane().getScene().getWindow(), "Form Error!", "Digite o nome do codigo");
                return;
            }

            if(produto.getDataCompra().isEmpty()) {
                alerta.showAlert(Alert.AlertType.ERROR, alerta.createRegistrationFormPane().getScene().getWindow(), "Form Error!", "Digite o codigo do produto");
                return;
            }

            if(produto.getCodigoNcm().isEmpty()) {
                alerta.showAlert(Alert.AlertType.ERROR, alerta.createRegistrationFormPane().getScene().getWindow(), "Form Error!", "Digite o código NCM");
                return;
            }


            dao.salvar(produto);
        } catch (Exception e) {
            e.printStackTrace();
        
        }
    }
    
    public void listar(){
        try {
            dao.listProduto();
        } catch (Exception e) {
            e.printStackTrace();
        
        }
    }
    
    public void remove(String nome){
        try {

            if(nome.isEmpty()) {
                alerta.showAlert(Alert.AlertType.ERROR, alerta.createRegistrationFormPane().getScene().getWindow(), "Form Error!", "Digite o nome do produto");
                return;
            }

            dao.remove(nome);
        } catch (Exception e) {
            e.printStackTrace();
        
        }
    }
    
    public void update(Produto produto){
        try {
            dao.update(produto);
        } catch (Exception e) {
            e.printStackTrace();
        
        }
    }
    
    public void buscar(String nome){
        try {
            if(nome.isEmpty()) {
                alerta.showAlert(Alert.AlertType.ERROR, alerta.createRegistrationFormPane().getScene().getWindow(), "Form Error!", "Digite o nome do produto");
                return;
            }
            dao.buscar(nome);
        } catch (Exception e) {
            e.printStackTrace();
        
        }
    }
    
}
