package controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;

public class VendaController {

    @FXML
    private Button btAddCompra;

    @FXML
    private Button btFinalizarVenda;

    @FXML
    private ComboBox<String> comboProduto;

    @FXML
    private TextField exibirQdtVenda;

    @FXML
    private TextField exibirQtdEstoque;

    @FXML
    private TextField exibirValorCompra;

    @FXML
    private TableView<String> tabelaVenda;

}
