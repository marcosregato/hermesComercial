module com.br.hermescomercial {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires java.persistence;


    opens com.br.hermescomercial to javafx.fxml;
    exports com.br.hermescomercial;
}