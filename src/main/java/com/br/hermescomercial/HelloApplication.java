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

    //https://openjfx.io/openjfx-docs/#install-javafx

    @Override
    public void start(Stage stage) throws IOException {
    	
    	try {
           // String pathFileLogin = "view/login.fxml";

            URL pathFileLogin = Paths.get("src/main/resources/view/login.fxml").toUri().toURL();
            Parent root = FXMLLoader.load(pathFileLogin);
            Scene menupScene = new Scene(root);
            stage.setScene(menupScene);
            stage.show();

        
    	}catch (Exception e) {
			// TODO: handle exception
    		e.printStackTrace();
		}
    }

    public static void main(String[] args) {
        launch(args);
    }
}