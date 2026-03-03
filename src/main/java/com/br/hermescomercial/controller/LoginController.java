package com.br.hermescomercial.controller;

import com.br.hermescomercial.model.Usuario;
import com.br.hermescomercial.util.Alerta;
import com.br.hermescomercial.util.ValidarCampo;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

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
    
    private final Alerta alerta = new Alerta();
    private final ValidarCampo validarCampo = new ValidarCampo();
    
    private PrincipalController principalController = new PrincipalController();
    
    @FXML
    public void handleBtEntra() {
        String nome = txtLogin.getText();
        String senha = txtSenha.getText();

        if (!nome.isEmpty() && !senha.isEmpty()) {
            System.out.println("asdf");
            List<Usuario> usuario = principalController.infoUsuario(nome, senha);
            if (usuario != null && !usuario.isEmpty()) {
                // Aqui você pode adicionar a lógica para abrir a janela principal
                System.out.println("Usuário logado com sucesso!");
            } else {
                alerta.showAlert(Alert.AlertType.ERROR,
                        btEntrar.getScene().getWindow(),
                        "Form Error!", "Login ou Senha incorreta");
            }
        } else {
            alerta.showAlert(Alert.AlertType.ERROR,
                    btEntrar.getScene().getWindow(),
                    "Form Error!", "Login ou Senha incorreta");
        }
    }
    
    @FXML
    public void handleBtFechar() {
        Stage stage = (Stage) btFechar.getScene().getWindow();
        stage.close();
    }
}
