
package com.br.hermescomercial.controller;

import java.util.ArrayList;
import java.util.List;

import com.br.hermescomercial.dao.ProdutoDao;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import com.br.hermescomercial.model.Produto;
import com.br.hermescomercial.util.Alerta;

/**
 *
 * @author marcos
 */
public class ProdutoController {


	@FXML
	private TextField txtNome;

	@FXML
	private ComboBox<String> comboFornecedor;

	@FXML
	private TextField txtCodigo;

	@FXML
	private TextField txtCategoria;

	@FXML
	private TextField txtSubCategoria;

	@FXML
	private TextField txtMarca;

	@FXML
	private Button btEditarProduto;

	@FXML
	private TextField txtDataCompra;

	@FXML
	private Button btExcluirProduto;

	@FXML
	private Button btSalvar;


	ProdutoDao dao;
	Alerta alerta;


	public void salvar(){
		try {

			btSalvar.addEventHandler(ActionEvent.ACTION, new EventHandler<ActionEvent>(){
				@Override
				public void handle(ActionEvent event) {
					Produto produto = new Produto();

					produto.setNome(txtNome.getText());
					produto.setCodigo(txtCodigo.getText());
					produto.setCategoria(txtCategoria.getText());
					produto.setSubCategoria(txtSubCategoria.getText());
					produto.setMarca(txtMarca.getText());
					produto.setDataCompra(txtDataCompra.getText());
					if(produto !=null) {
						dao.salvar(produto);
					}
				}
			});

		} catch (Exception e) {
			System.out.println(e.getMessage());

		}
	}



	public void remove(){
		try {

			if(txtNome.getText().isEmpty()) {
				dao = new ProdutoDao();
				dao.remove(txtNome.getText());

				//alerta.showAlert(Alert.AlertType.ERROR, alerta.createRegistrationFormPane().getScene().getWindow(), "Form Error!", "Digite o nome do produto");
				//return;
			}



		} catch (Exception e) {
			System.out.println(e.getMessage());

		}
	}

	public void update(){
		try {

			btSalvar.addEventHandler(ActionEvent.ACTION, new EventHandler<ActionEvent>(){
				@Override
				public void handle(ActionEvent event) {
					Produto produto = new Produto();
					produto.setNome(txtNome.getText());
					produto.setCodigo(txtCodigo.getText());
					produto.setCategoria(txtCategoria.getText());
					produto.setSubCategoria(txtSubCategoria.getText());
					produto.setMarca(txtMarca.getText());
					produto.setDataCompra(txtDataCompra.getText());
					dao.update(produto);

				}
			});
		} catch (Exception e) {
			System.out.println(e.getMessage());

		}
	}


	public void listar(){
		try {

			List<Produto> lista = new ArrayList<>();

			lista = dao.listar();
		} catch (Exception e) {
			System.out.println(e.getMessage());

		}

	}

	public void buscar(){
		try {


			//            btPesquisar.addEventHandler(ActionEvent.ACTION, new EventHandler<ActionEvent>(){
			//                @Override
			//                public void handle(ActionEvent event) {
			//                    List<Produto> lista = new ArrayList<Produto>();
			//
			//                    lista = dao.buscar(txtNome.getText());
			//
			//                }
			//            });


			//            if(txtNome.getText().isEmpty()) {
			//                alerta.showAlert(Alert.AlertType.ERROR, alerta.createRegistrationFormPane().getScene().getWindow(), "Form Error!", "Digite o nome do produto");
			//                return;
			//            }

			// lista = dao.buscar(txtNome.getText());


		} catch (Exception e) {
			System.out.println(e.getMessage());

		}
	}

}
