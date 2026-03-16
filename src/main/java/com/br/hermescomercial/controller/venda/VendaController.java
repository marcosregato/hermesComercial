package com.br.hermescomercial.controller.venda;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import org.apache.logging.log4j.LogManager;


public class VendaController {

    @FXML
    private Button btAddCompra;

    @FXML
    private Button btFinalizarVenda;

    @FXML
    private Button btEditarConmpra;

    @FXML
    private Button btCancelarCompra;

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

    private static final org.apache.logging.log4j.Logger logger = LogManager.getLogger(VendaController.class);

    @FXML
    public void salvar() {
        try {
            // Logic for saving/adding sale item
            logger.info("Tentativa de adicionar item à venda");
        } catch (Exception e) {
            logger.error("Erro ao salvar venda: " + e.getMessage(), e);
        }
    }

    public void remove(String nome) {
        try {
            if (nome == null || nome.isEmpty()) {
                logger.warn("Nome do produto vazio para remoção");
                return;
            }
            // Logic to remove item
        } catch (Exception e) {
            logger.error("Erro ao remover item da venda: " + e.getMessage(), e);
        }
    }

    public void update() {
        try {
            // Logic to update item in table
        } catch (Exception e) {
            logger.error("Erro ao atualizar venda: " + e.getMessage(), e);
        }
    }

    public void buscar() {
        try {
            // Logic to search products
        } catch (Exception e) {
            logger.error("Erro ao buscar: " + e.getMessage(), e);
        }
    }

    public void listar() {
        try {
            // Logic to list sales
        } catch (Exception e) {
            logger.error("Erro ao listar: " + e.getMessage(), e);
        }
    }

    @FXML
    public void finalizarCompra() {
        try {
            // Logic to finalize sale
            logger.info("Finalizando compra...");
        } catch (Exception e) {
            logger.error("Erro ao finalizar compra: " + e.getMessage(), e);
        }
    }

}
