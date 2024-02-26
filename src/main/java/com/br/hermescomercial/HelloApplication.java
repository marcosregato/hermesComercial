package com.br.hermescomercial;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.nio.file.Paths;

public class HelloApplication extends Application {



    @Override
    public void start(Stage stage) throws IOException {
        URL url = Paths.get("./src/main/resources/com/br/hermescomercial/login.fxml").toUri().toURL();
        Parent root  = FXMLLoader.load(url);
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}