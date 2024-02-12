package controller;

import java.util.ArrayList;
import java.util.List;

import dao.UsuarioDao;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import model.Usuario;
import util.ValidarCampo;

public class UsuarioController {

    @FXML
    private Button btCancelar;

    @FXML
    private Button btSalvar;
    
    @FXML
    private ComboBox<Usuario> comboPJ_PF;

    @FXML
    private ComboBox<Usuario> comboTipoUsuario;

    @FXML
    private TextField txtEmail;

    @FXML
    private TextField txtEndereco;

    @FXML
    private TextField txtNome;

    @FXML
    private TextField txtPJ_PF;

    @FXML
    private TextField txtTelefone;


    UsuarioDao dao = new UsuarioDao();
    ValidarCampo validarCampo = new ValidarCampo();

    public void salvar(){

        try {
            btSalvar.addEventHandler(ActionEvent.ACTION, new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    Usuario usuario = new Usuario();
                    usuario.setNome(txtNome.getText());
                    usuario.setEndereco(txtEndereco.getText());
                    //TODO Validar CPF e validar CNPJ
                    usuario.setCnjp(String.valueOf(comboPJ_PF.getValue()));
                    usuario.setCpf(String.valueOf(comboPJ_PF.getValue()));

                    usuario.setTipousuario(String.valueOf(comboTipoUsuario.getValue()));
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
        	dao.remove(nome);
        	
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
                    //TODO Validar CPF e validar CNPJ
                    usuario.setCnjp(String.valueOf(comboPJ_PF.getValue()));
                    usuario.setCpf(String.valueOf(comboPJ_PF.getValue()));

                    usuario.setTipousuario(String.valueOf(comboTipoUsuario.getValue()));
                    usuario.setEmail(txtEmail.getText());

                    dao.update(usuario);

                }
            });



        }catch (Exception e){
        	System.out.println(e.getMessage());

        }
    }

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

    public void listar(){
        try {
            dao.lista();
        }catch (Exception e){
        	System.out.println(e.getMessage());

        }
    }


}
