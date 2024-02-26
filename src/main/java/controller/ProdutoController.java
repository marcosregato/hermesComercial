
package controller;

import java.util.ArrayList;
import java.util.List;

import dao.ProdutoDao;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import model.Produto;
import util.Alerta;

/**
 *
 * @author marcos
 */
public class ProdutoController {


    @FXML
    private TextField txtNome;

    @FXML
    private ComboBox<String> comboFornecedor;

    @FXML
    private TextField txtCodigo;

    @FXML
    private TextField txtSubCategoria;

    @FXML
    private Button btEditarProduto;

    @FXML
    private TextField txtDataCompra;

    @FXML
    private Button btExcluirProduto;

    @FXML
    private Button btSalvar;


    ProdutoDao dao;
    Alerta alerta;
    
    
    public void salvar(){
        try {

           /*   btSalvar.addEventHandler(ActionEvent.ACTION, new EventHandler<ActionEvent>(){
                @Override
                public void handle(ActionEvent event) {
                    Produto produto = new Produto();

                  produto.setNome(txtNome.getText());
                    produto.setTipo(txtTipo.getText());
                    produto.setSubTipo(txtSubTipo.getText());
                    produto.setSetor(txtSetor.getText());
                    produto.setCodigoNcm(txtCodigo.getText());
                    produto.setDataCompra(txtDataCompra.getText());
                    produto.setSetor(txtSetor.getText());
                            produto.setValorUnitario(txtValor);

                    dao.salvar(produto);

                }
            });*/


        } catch (Exception e) {
            e.printStackTrace();
        
        }
    }
    
    public void listar(){
        try {

            List<Produto> lista = new ArrayList<>();

            lista = dao.listar();
        } catch (Exception e) {
            e.printStackTrace();
        
        }
       
    }
    
    public void remove(){
        try {

//            if(nome.isEmpty()) {
//                alerta.showAlert(Alert.AlertType.ERROR, alerta.createRegistrationFormPane().getScene().getWindow(), "Form Error!", "Digite o nome do produto");
//                return;
//            }

            dao.remove(txtNome.getText());
        } catch (Exception e) {
            e.printStackTrace();
        
        }
    }
    
    public void update(){
        try {

            /*  btSalvar.addEventHandler(ActionEvent.ACTION, new EventHandler<ActionEvent>(){
                @Override
                public void handle(ActionEvent event) {
                    Produto produto = new Produto();

                  produto.setNome(txtNome.getText());
                    produto.setTipo(txtTipo.getText());
                    produto.setSubTipo(txtSubTipo.getText());
                    produto.setSetor(txtSetor.getText());
                    produto.setCodigoNcm(txtCodigo.getText());
                    produto.setDataCompra(txtDataCompra.getText());
                    produto.setSetor(txtSetor.getText());
                    //produto.setValorUnitario(txtValor);

                    dao.update(produto);

                }
            });*/
        } catch (Exception e) {
            e.printStackTrace();
        
        }
    }
    
    public void buscar(){
        try {


//            btPesquisar.addEventHandler(ActionEvent.ACTION, new EventHandler<ActionEvent>(){
//                @Override
//                public void handle(ActionEvent event) {
//                    List<Produto> lista = new ArrayList<Produto>();
//
//                    lista = dao.buscar(txtNome.getText());
//
//                }
//            });


//            if(txtNome.getText().isEmpty()) {
//                alerta.showAlert(Alert.AlertType.ERROR, alerta.createRegistrationFormPane().getScene().getWindow(), "Form Error!", "Digite o nome do produto");
//                return;
//            }
            
           // lista = dao.buscar(txtNome.getText());
            
            
        } catch (Exception e) {
            e.printStackTrace();
        
        }
    }
    
}
