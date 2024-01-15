package controller;

import dao.LoginDao;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import model.Usuario;
import util.Alerta;
import util.ValidarCampo;

import java.util.List;

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

            String login = validarCampo.campoVazio(txtLogin.getText());
            String senha = validarCampo.campoVazio(txtSenha.getText());
            List<Usuario> usuario = dao.acessarUsuario(login,senha);

            if(usuario.isEmpty()) {
                alerta.showAlert(Alert.AlertType.ERROR,
                        alerta.createRegistrationFormPane().getScene().getWindow(),
                        "Form Error!", "Login ou Senha está incorreta");
            }

        }catch (Exception e ){
            e.printStackTrace();
        }

    }

}