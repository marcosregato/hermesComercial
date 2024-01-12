package controller;

import dao.LoginDao;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
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
    ValidarCampo validarCampo = new ValidarCampo();
    public void fazerLogin(){
        try {

            dao = new LoginDao();

            String login = validarCampo.campoVazio(txtLogin.getText());
            String senha = validarCampo.campoVazio(txtSenha.getText());
            dao.acessarUsuario(login,senha);

        }catch (Exception e ){
            e.printStackTrace();

        }



    }



}