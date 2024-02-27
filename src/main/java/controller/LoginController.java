package controller;

import dao.LoginDao;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import model.Login;
import util.Alerta;
import util.ValidarCampo;

public class LoginController {

    @FXML
    private Button btEntrar;

    @FXML
    private Button btFechar;

    @FXML
    private TextField txtLogin;

    @FXML
    private TextField txtSenha;


    LoginDao dao ;

    Alerta alerta;
    ValidarCampo validarCampo = new ValidarCampo();
    public void fazerLogin(){
        try {
        	dao = new LoginDao();

            String nome = validarCampo.campoVazio(txtLogin.getText());
            String senha = validarCampo.campoVazio(txtSenha.getText());
            Login login = new Login();

            if((txtLogin.getText() != null) || (txtSenha.getText() !=null)) {
               new  PrincipalController(dao.acessarUsuario(txtLogin.getText(),
            		   txtSenha.getText()));

            }else{
                alerta.showAlert(Alert.AlertType.ERROR,
                        alerta.createRegistrationFormPane().getScene().getWindow(),
                        "Form Error!", "Login ou Senha está incorreta");
            }

        }catch (Exception e ){
            e.printStackTrace();
        }

    }

}