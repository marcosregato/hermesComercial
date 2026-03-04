package com.br.hermescomercial.controller;

import com.br.hermescomercial.util.Alerta;
import com.br.hermescomercial.util.ValidarCampo;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class LoginController {

    @FXML
    private Button btEntrar;

    @FXML
    private Button btFechar;

    @FXML
    private TextField txtLogin;

    @FXML
    private TextField txtSenha;
    
    Alerta alerta;
    ValidarCampo validarCampo = new ValidarCampo();

    private static final org.apache.logging.log4j.Logger logger = LogManager.getLogger(LoginController.class);
    PrincipalController principalController = new PrincipalController();
    
    
    /*public void initialize(URL url, ResourceBundle rb) {
    	principalController.setUsuarioLogado(nome, senha);
        
        carregarTableViewClientes();

        // Limpando a exibição dos detalhes do cliente
        selecionarItemTableViewClientes(null);

        // Listen acionado diante de quaisquer alterações na seleção de itens do TableView
        tableViewClientes.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> selecionarItemTableViewClientes(newValue));
        
    }*/
    
    @FXML
    public void handleBtEntra() {
        try{
    	String nome = validarCampo.campoVazio(txtLogin.getText());
        String senha = validarCampo.campoVazio(txtSenha.getText());
        if((nome != null) || (senha !=null)) {
        	principalController.setUsuarioLogado(nome, senha);
            //return principalController.infoUsuario();
            //PrincipalController.S


        }else{
            alerta.showAlert(Alert.AlertType.ERROR,
            		alerta.createRegistrationFormPane().getScene().getWindow(),
                    "Form Error!", "Login ou Senha está incorreta");
        }
        }catch (Exception e ){
            logger.error("Error saving alert", e);
        }
    }
    
    @FXML
    public void handleBtFechar() {
    	String nome = validarCampo.campoVazio(txtLogin.getText());
        String senha = validarCampo.campoVazio(txtSenha.getText());
        if((nome != null) || (senha !=null)) {
            //List<Usuario> usuario = new  PrincipalController(nome,senha).infoUsuario();
            //return;

        }else{
            alerta.showAlert(Alert.AlertType.ERROR,
            		alerta.createRegistrationFormPane().getScene().getWindow(),
                    "Form Error!", "Login ou Senha está incorreta");
        }
    }


    
    /*public List<Usuario> fazerLogin(){
        try {
        	
            String nome = validarCampo.campoVazio(txtLogin.getText());
            String senha = validarCampo.campoVazio(txtSenha.getText());
            
            btEntrar.addEventHandler(ActionEvent.ACTION, new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    if((nome != null) || (senha !=null)) {
                        List<Usuario> usuario = new  PrincipalController(nome,senha).infoUsuario();
                        return;

                    }else{
                        alerta.showAlert(Alert.AlertType.ERROR,
                        		alerta.createRegistrationFormPane().getScene().getWindow(),
                                "Form Error!", "Login ou Senha está incorreta");
                    }
                }
            });

        }catch (Exception e ){
            e.printStackTrace();
        }
        
        return null;
    }*/

}