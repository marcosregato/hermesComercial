package com.br.hermescomercial;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.nio.file.Paths;

public class HelloApplication extends Application {

    //https://openjfx.io/openjfx-docs/#install-javafx

    @Override
    public void start(Stage stage) throws IOException {


        String pathFileLogin ="C:\\Users\\marcos\\Documents\\NetBeansProjects\\hermesComercial\\src\\main\\java\\com\\br\\hermescomercial\\login.fxml";
        //String pathFileLogin ="\\src\\main\\java\\com\\br\\hermescomercial\\login.fxml";
        /*AnchorPane root = (AnchorPane) FXMLLoader.load(HelloApplication.class.getResource(pathFileLogin));
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.setTitle("Bluerift Timeline");
        stage.show();*/

        /*FXMLLoader loader = new FXMLLoader(this.getClass().getResource(pathFileLogin));
        Parent root = loader.load();
        stage.setScene(new Scene(root));
        stage.show();*/

        URL fxmlResource = HelloApplication.class.getResource(pathFileLogin);
        FXMLLoader loader = new FXMLLoader(fxmlResource);
        StackPane pane = loader.load();

        // Assign the loaded view to the stage and show it
        Scene scene = new Scene(pane);
        stage.setScene(scene);
        stage.show();



        /*URL url = Paths.get(pathFileLogin).toUri().toURL();
        Parent root  = FXMLLoader.load(url);
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();*/
    }

    public static void main(String[] args) {
        launch(args);
    }
}