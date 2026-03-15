package com.br.hermescomercial.controller;

import com.br.hermescomercial.model.Usuario;
import com.br.hermescomercial.util.Alerta;
import com.br.hermescomercial.util.ValidarCampo;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;

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

            Usuario usuario = principalController.infoUsuario(txtLogin.getText(), txtSenha.getText());
            if (usuario != null) {
                System.out.println("Usuário logado com sucesso!");
                try {
                    // Carrega o FXML da tela principal
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/Layout_principal.fxml"));
                    Parent root = loader.load();

                    // Cria uma nova cena
                    Scene scene = new Scene(root);

                    // Obtém o palco (Stage) atual a partir de qualquer nó na cena atual
                    Stage stage = (Stage) btEntrar.getScene().getWindow();

                    // Define a nova cena no palco
                    stage.setScene(scene);
                    stage.setTitle("Hermes Comercial - Principal");
                    stage.show();

                } catch (IOException e) {
                    e.printStackTrace();
                    alerta.showAlert(Alert.AlertType.ERROR,
                            btEntrar.getScene().getWindow(),
                            "Erro ao carregar a tela", "Não foi possível carregar a tela principal.");
                }
            } else {
                alerta.showAlert(Alert.AlertType.ERROR,
                        btEntrar.getScene().getWindow(),
                        "Erro de Login", "Login ou Senha incorreta");
            }

    }

    @FXML
    public void handleBtFechar() {
        Stage stage = (Stage) btFechar.getScene().getWindow();
        stage.close();
    }
}
