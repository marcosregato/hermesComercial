package com.br.hermescomercial.controller;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import com.br.hermescomercial.dao.UsuarioDao;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import com.br.hermescomercial.model.Usuario;
import com.br.hermescomercial.util.ValidarCampo;

public class UsuarioController implements Initializable{
	
	

	   

	    @FXML
	    private TextField PesNome;

	    @FXML
	    private Button brPesquisar;

	    @FXML
	    private Button btEditar;

	    @FXML
	    private Button btExcluir;

	    @FXML
	    private Button btSalvar;
	    
	    @FXML
	    private TableColumn<Usuario, String> ColTIpo;

	    @FXML
	    private TableColumn<Usuario, String> colCNPJ_CPF;

	    @FXML
	    private TableColumn<Usuario, String> colNome;

	    @FXML
	    private ComboBox<String> comboEstado;

	    @FXML
	    private ComboBox<String> comboTipo;

	    @FXML
	    private TableView<String> tabelaUsuario;

	    @FXML
	    private TextField txtBairro;

	    @FXML
	    private TextField txtCep;

	    @FXML
	    private TextField txtCidade;

	    @FXML
	    private TextField txtCnpjCpf;

	    @FXML
	    private TextField txtEmail;

	    @FXML
	    private TextField txtEndereco;

	    @FXML
	    private TextField txtNome;

	    @FXML
	    private TextField txtTelefone;


	UsuarioDao dao = new UsuarioDao();
	ValidarCampo validarCampo = new ValidarCampo();
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		tabelaUsuario.setItems(FXCollections.observableArrayList("Usuário","Funcionário","Cliente"));
	}

	public void salvar(){

		try {
			btSalvar.addEventHandler(ActionEvent.ACTION, new EventHandler<ActionEvent>() {
				@Override
				public void handle(ActionEvent event) {
					Usuario usuario = new Usuario();
					usuario.setNome(txtNome.getText());
					usuario.setEndereco(txtEndereco.getText());

					if(comboTipo.getValue().equals("Fornecedor")) {
						usuario.setTipousuario(comboTipo.getValue());
						usuario.setCnpj(txtCnpjCpf.getText());
					}else if (comboTipo.getValue().equals("Usuário")) {
						usuario.setTipousuario(comboTipo.getValue());
						usuario.setCpf(txtCnpjCpf.getText());
					}else {
						//TODO criar a mensagem
						System.out.println(">>>> ERRO NO TIPO DE USUARIO <<<<<");
					}
					usuario.setEndereco(txtEndereco.getText());
					usuario.setBairro(txtBairro.getText());
					usuario.setCidade(txtCidade.getText());
					usuario.setEstado(comboEstado.getValue());
					usuario.setEmail(txtEmail.getText());

					dao.salvar(usuario);

				}
			});


		}catch (Exception e){
			System.out.println(e.getMessage());
		}
	}

	public void remove(String nome){
		try {
			if(nome.isEmpty()) {
				dao.remove(nome);
			}
			//TODO criar a mensagem

		}catch (Exception e){
			System.out.println(e.getMessage());
		}
	}

	public void update(){

		try {

			btSalvar.addEventHandler(ActionEvent.ACTION, new EventHandler<ActionEvent>() {
				@Override
				public void handle(ActionEvent event) {
					Usuario usuario = new Usuario();
					usuario.setNome(txtNome.getText());
					usuario.setEndereco(txtEndereco.getText());

					if(comboTipo.getValue().equals("Fornecedor")) {
						usuario.setTipousuario(comboTipo.getValue());
						usuario.setCnpj(txtCnpjCpf.getText());
						usuario.setEndereco(txtEndereco.getText());
						usuario.setBairro(txtBairro.getText());
						usuario.setCidade(txtCidade.getText());
						usuario.setEstado(comboEstado.getValue());
						usuario.setEmail(txtEmail.getText());

						dao.update(usuario);
					}else if (comboTipo.getValue().equals("Usuário")) {
						usuario.setTipousuario(comboTipo.getValue());
						usuario.setCpf(txtCnpjCpf.getText());
						usuario.setEndereco(txtEndereco.getText());
						usuario.setBairro(txtBairro.getText());
						usuario.setCidade(txtCidade.getText());
						usuario.setEstado(comboEstado.getValue());
						usuario.setEmail(txtEmail.getText());

						dao.update(usuario);
					}else {
						//TODO criar a mensagem
						System.out.println(">>>> ERRO NO TIPO DE USUARIO <<<<<");
					}

				}
			});

		}catch (Exception e){
			System.out.println(e.getMessage());
		}
	}

	//TODO fazer listagem na tabela
	public void buscar(){
		try {
			List<Usuario> lista = new ArrayList<>();

			if(validarCampo.campoVazio(txtNome.getText()) != "") {
				lista = dao.buscar(txtNome.getText());
			}

		}catch (Exception e){
			System.out.println(e.getMessage());
		}
	}

	

	


}
