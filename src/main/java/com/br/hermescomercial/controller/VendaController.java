package com.br.hermescomercial.controller;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import com.br.hermescomercial.model.Usuario;

import java.util.ArrayList;
import java.util.List;

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


    public void salvar(){

        try {
            btAddCompra.addEventHandler(ActionEvent.ACTION, new EventHandler<ActionEvent>() {

                @Override
                public void handle(ActionEvent event) {


                }
            });


        }catch (Exception e){
            System.out.println(e.getMessage());
        }
    }

    public void remove(String nome){
        try {
            if(nome.isEmpty()) {

            }
            //TODO criar a mensagem

        }catch (Exception e){
            System.out.println(e.getMessage());
        }
    }

    public void update(){

        try {

            btAddCompra.addEventHandler(ActionEvent.ACTION, new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {

                }
            });

        }catch (Exception e){
            System.out.println(e.getMessage());
        }
    }

    //TODO fazer listagem na tabela
    public void buscar(){
        try {


        }catch (Exception e){
            System.out.println(e.getMessage());
        }
    }

    public void listar(){
        try {

        }catch (Exception e){
            System.out.println(e.getMessage());

        }
    }


    public void finalizarCompra(){

        btFinalizarVenda.addEventHandler(ActionEvent.ACTION, new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {

            }
        });
    }

}
