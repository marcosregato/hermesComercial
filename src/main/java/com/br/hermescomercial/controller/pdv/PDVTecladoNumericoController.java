package com.br.hermescomercial.controller.pdv;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;
import javafx.scene.control.Button;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.function.Consumer;

public class PDVTecladoNumericoController implements Initializable {

    @FXML private TextField txtDisplay;
    @FXML private Button btn0, btn1, btn2, btn3, btn4, btn5, btn6, btn7, btn8, btn9;
    @FXML private Button btnVirgula, btnLimpar, btnEnter;

    private Consumer<String> onEnterCallback;
    private String valorAtual = "";

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        configurarBotoesNumericos();
        configurarBotoesAcao();
    }

    private void configurarBotoesNumericos() {
        // Botões numéricos
        btn0.setOnAction(e -> adicionarDigito("0"));
        btn1.setOnAction(e -> adicionarDigito("1"));
        btn2.setOnAction(e -> adicionarDigito("2"));
        btn3.setOnAction(e -> adicionarDigito("3"));
        btn4.setOnAction(e -> adicionarDigito("4"));
        btn5.setOnAction(e -> adicionarDigito("5"));
        btn6.setOnAction(e -> adicionarDigito("6"));
        btn7.setOnAction(e -> adicionarDigito("7"));
        btn8.setOnAction(e -> adicionarDigito("8"));
        btn9.setOnAction(e -> adicionarDigito("9"));
        
        // Vírgula decimal
        btnVirgula.setOnAction(e -> adicionarVirgula());
    }

    private void configurarBotoesAcao() {
        // Limpar
        btnLimpar.setOnAction(e -> limpar());
        
        // Enter
        btnEnter.setOnAction(e -> confirmar());
    }

    private void adicionarDigito(String digito) {
        valorAtual += digito;
        atualizarDisplay();
    }

    private void adicionarVirgula() {
        if (!valorAtual.contains(",")) {
            valorAtual += ",";
            atualizarDisplay();
        }
    }

    private void limpar() {
        valorAtual = "";
        atualizarDisplay();
    }

    private void confirmar() {
        if (onEnterCallback != null && !valorAtual.isEmpty()) {
            onEnterCallback.accept(valorAtual);
        }
    }

    private void atualizarDisplay() {
        txtDisplay.setText(valorAtual.isEmpty() ? "0,00" : valorAtual);
    }

    public void setOnEnterCallback(Consumer<String> callback) {
        this.onEnterCallback = callback;
    }

    public void setValorInicial(String valor) {
        this.valorAtual = valor;
        atualizarDisplay();
    }

    public void limparTeclado() {
        limpar();
    }
}
