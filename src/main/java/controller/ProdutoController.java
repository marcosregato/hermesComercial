
package controller;

import java.util.ArrayList;
import java.util.List;

import dao.ProdutoDao;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import model.Produto;
import util.Alerta;

/**
 *
 * @author marcos
 */
public class ProdutoController {


    @FXML
    private MenuItem colAlerta;

    @FXML
    private MenuItem colBancoDados;

    @FXML
    private MenuItem colCliente;

    @FXML
    private MenuItem colDespesa;

    @FXML
    private MenuItem colFluxoCaixa;

    @FXML
    private MenuItem colFuncionario;

    @FXML
    private MenuItem colPermissao;

    @FXML
    private MenuItem colProduto;

    @FXML
    private TextField txtCodigo;

    @FXML
    private TextField txtDataCompra;

    @FXML
    private TextArea txtDescricao;

    @FXML
    private TextField txtEmailEmpresa;

    @FXML
    private TextField txtNome;

    @FXML
    private TextField txtSetor;

    @FXML
    private TextField txtSubTipo;

    @FXML
    private TextField txtTipo;

    @FXML
    private TextField txtValorTotal;

    @FXML
    private Text txtValorVenda;

    @FXML
    private Button btCancelar;

    @FXML
    private Button btSalvar;

    @FXML
    private Button btPesquisar;
    
    ProdutoDao dao;
    Alerta alerta;
    
    
    public void salvar(){
        try {

            btSalvar.addEventHandler(ActionEvent.ACTION, new EventHandler<ActionEvent>(){
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

                    dao.salvar(produto);

                }
            });


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

            btSalvar.addEventHandler(ActionEvent.ACTION, new EventHandler<ActionEvent>(){
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
            });
        } catch (Exception e) {
            e.printStackTrace();
        
        }
    }
    
    public void buscar(){
        try {


            btPesquisar.addEventHandler(ActionEvent.ACTION, new EventHandler<ActionEvent>(){
                @Override
                public void handle(ActionEvent event) {
                    List<Produto> lista = new ArrayList<Produto>();

                    lista = dao.buscar(txtNome.getText());

                }
            });


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
