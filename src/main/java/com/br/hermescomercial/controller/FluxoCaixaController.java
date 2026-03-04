package com.br.hermescomercial.controller;

import com.br.hermescomercial.util.ValidarCampo;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class FluxoCaixaController {

	@FXML
	private ComboBox<?> comboPagamento;

	@FXML
	private TextField txtNome;

	@FXML
	private TextField txtData;

	@FXML
	private TextField txtValor;

	@FXML
	private Button btCancelar;

	@FXML
	private Button btSalvar;

    private static final org.apache.logging.log4j.Logger logger = LogManager.getLogger(FluxoCaixaController.class);

	public void salvar() {
		try {

		} catch (Exception e) {
			logger.info(e.getMessage());
		}

	}

	public void remove() {
		try {

		} catch (Exception e) {
			logger.info(e.getMessage());
		}

	}

	public void update() {
		try {

		} catch (Exception e) {
			logger.info(e.getMessage());
		}

	}

	public void buscar() {
		try {

		} catch (Exception e) {
			logger.info(e.getMessage());
		}

	}

	public void listar() {
		try {

		} catch (Exception e) {
			logger.info(e.getMessage());
		}

	}

}
