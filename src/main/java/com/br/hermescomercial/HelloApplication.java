package com.br.hermescomercial;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.nio.file.Paths;

public class HelloApplication extends Application {

    static Stage stage;
    static Scene telaLogin;
    static Scene telarootPrincipal;

    @Override
    public void start(Stage primaryStage) throws IOException {

        try {
            stage = primaryStage;
            // String pathFileLogin = "view/login.fxml";

            URL pathFileLogin = Paths.get("src/main/resources/view/login.fxml").toUri().toURL();
            Parent rootLogin = FXMLLoader.load(pathFileLogin);
          telaLogin = new Scene(rootLogin);
  //          stage.setScene(telaLogin);


            URL pathFileLayoutPrincipal = Paths.get("src/main/resources/view/Layout_principal.fxml").toUri().toURL();
            Parent rootPrincipal = FXMLLoader.load(pathFileLayoutPrincipal);
            telarootPrincipal = new Scene(rootPrincipal);
//            stage.setScene(telarootPrincipal);
 //           stage.show();


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void chamaStage(String tela){
        switch (tela){
            case "main":
                stage.setScene(telaLogin);
                break;
            case "telaPrincipal":
                stage.setScene(telarootPrincipal);
//                break;
            }
        }

    public static void main(String[] args) {
        launch(args);
    }
}