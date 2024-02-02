import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class App  extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception {
        Scene scene = new Scene(LoadFXML(),600,500);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Teste");
        primaryStage.show();

    }

    private static Parent loadFMXL() throws  IOException{
        FXMLLoader ffxmlLoader = new FXMLLoader(App.class.getResource("login" + ".fxml"));
        return ffxmlLoader.load();
    }
}
