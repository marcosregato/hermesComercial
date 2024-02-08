
package controller;

import java.util.Collection;
import java.util.List;

import dao.ProdutoDao;
import javafx.fxml.FXML;
import javafx.scene.control.*;
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
    
    ProdutoDao dao;
    Alerta alerta;

    
    public void salvar(Produto produto){
        try {
            alerta = new Alerta();

            if(produto.getTipo().isEmpty()) {
                alerta.showAlert(Alert.AlertType.ERROR, alerta.createRegistrationFormPane().getScene().getWindow(), "Form Error!", "Digite o nome do produto");
                return;
            }

            if(produto.getSubTipo().isEmpty()) {
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
    
    public List<Produto> listar(){
        try {
           return dao.listProduto();
        } catch (Exception e) {
            e.printStackTrace();
        
        }
        return null;
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
