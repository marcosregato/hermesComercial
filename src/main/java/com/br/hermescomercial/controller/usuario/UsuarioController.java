package com.br.hermescomercial.controller.usuario;

import com.br.hermescomercial.dao.UsuarioDao;
import com.br.hermescomercial.model.Usuario;
import com.br.hermescomercial.util.ValidarCampo;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class UsuarioController implements Initializable {

    private static final Logger logger = LogManager.getLogger(UsuarioController.class);

    @FXML
    private TextField txtNome;

    @FXML
    private TextField txtEndereco;

    @FXML
    private TextField txtBairro;

    @FXML
    private TextField txtCidade;

    @FXML
    private ComboBox<String> comboEstado;

    @FXML
    private TextField txtCep;


    @FXML
    private ComboBox<String> comboTipoDocumento;

    @FXML
    private TextField txtNumeroDocumento;

    @FXML
    private TextField txtEmail;

    @FXML
    private TextField txtTelefone;

    @FXML
    private TextField txtWhatsapp;

    @FXML
    private ComboBox<String> comboTipoUsuario;

    @FXML
    private Button btSalvar;

    @FXML
    private Button btExcluir;

    @FXML
    private Button btEditar;

    @FXML
    private TextField PesNome;

    private UsuarioDao dao = new UsuarioDao();

    private ValidarCampo validarCampo = new ValidarCampo();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        if (comboEstado != null) {
            comboEstado.setItems(FXCollections.observableArrayList("AC", "AL", "AP", "AM", "BA", "CE", "DF", "ES", "GO", "MA", "MT", "MS", "MG", "PA", "PB", "PR", "PE", "PI", "RJ", "RN", "RS", "RO", "RR", "SC", "SP", "SE", "TO"));
        }
        if (comboTipoUsuario != null) {
            comboTipoUsuario.setItems(FXCollections.observableArrayList("Usuario", "Fornecedor", "Cliente", "Funcionário"));
        }
        if (comboTipoDocumento != null) {
            comboTipoDocumento.setItems(FXCollections.observableArrayList("CPF", "CNPJ"));
        }
    }

    public void salvar() {
        try {
            Usuario usuario = new Usuario();
            usuario.setNome(txtNome.getText());
            usuario.setEndereco(txtEndereco.getText());
            usuario.setBairro(txtBairro.getText());
            usuario.setCidade(txtCidade.getText());
            usuario.setEstado(comboEstado.getValue());
            usuario.setWhastsapp(txtWhatsapp.getText());
            usuario.setTelefone(txtTelefone.getText());
            usuario.setTipoDocumento(comboTipoDocumento.getValue());
            usuario.setNumeroDocumetn(txtNumeroDocumento.getText());
            usuario.setCep(txtCep.getText());
            usuario.setEmail(txtEmail.getText());
            usuario.setTipousuario(comboTipoUsuario.getValue());

            if ("Fornecedor".equals(comboTipoUsuario.getValue()) && "CNPJ".equals(comboTipoDocumento.getValue())) {
                validarCampo.isCNPJ(txtNumeroDocumento.getText());

            } else {
                validarCampo.isCPF(txtNumeroDocumento.getText());
            }

            dao.salvar(usuario);
        } catch (Exception e) {
            logger.error("Erro ao salvar: " + e.getMessage());
        }
    }

    public void delete() {
        try {
            if (txtNome.getText() != null && !txtNome.getText().isEmpty()) {
                dao.remove(txtNome.getText());
            }
        } catch (Exception e) {
            logger.error("Erro ao remover: " + e.getMessage());
        }
    }

    public void update() {
        try {
            Usuario usuario = new Usuario();
            usuario.setNome(txtNome.getText());
            usuario.setEndereco(txtEndereco.getText());
            usuario.setBairro(txtBairro.getText());
            usuario.setCidade(txtCidade.getText());
            usuario.setEstado(comboEstado.getValue());
            usuario.setWhastsapp(txtWhatsapp.getText());
            usuario.setTelefone(txtTelefone.getText());
            usuario.setTipoDocumento(comboTipoDocumento.getValue());
            usuario.setNumeroDocumetn(txtNumeroDocumento.getText());
            usuario.setCep(txtCep.getText());
            usuario.setEmail(txtEmail.getText());
            usuario.setTipousuario(comboTipoUsuario.getValue());

            if ("Fornecedor".equals(comboTipoUsuario.getValue()) && "CNPJ".equals(comboTipoDocumento.getValue())) {
                validarCampo.isCNPJ(txtNumeroDocumento.getText());

            } else {
                validarCampo.isCPF(txtNumeroDocumento.getText());
            }

            dao.update(usuario);
        } catch (Exception e) {
            logger.error("Erro ao atualizar: " + e.getMessage());
        }
    }

    public void buscar() {
        try {
            if (PesNome != null && !PesNome.getText().isEmpty()) {
                List<Usuario> resultados = dao.buscar(PesNome.getText());
                if (!resultados.isEmpty()) {
                    Usuario p = resultados.get(0);
                    txtNome.setText(p.getNome());
                    txtEndereco.setText(p.getEndereco());
                    txtBairro.setText(p.getBairro());
                    txtCidade.setText(p.getCidade());
                    comboEstado.setValue(p.getEstado());
                    txtWhatsapp.setText(p.getWhastsapp());
                    txtTelefone.setText(p.getTelefone());
                    comboTipoDocumento.setValue(p.getTipoDocumento());
                    txtNumeroDocumento.setText(p.getNumeroDocumetn());
                    txtCep.setText(p.getCep());

                    txtEmail.setText(p.getEmail());
                    comboTipoUsuario.setValue(p.getTipousuario());
                }
            }
        } catch (Exception e) {
            logger.error("Erro ao buscar usuario: " + e.getMessage());
        }
    }

    public void lista() {
        try {
            List<Usuario> todos = dao.lista();
            for (Usuario p : todos) {
                logger.info("Usuario na lista: " + p.getNome());
            }
        } catch (Exception e) {
            logger.error("Erro ao listar usuarios: " + e.getMessage());
        }
    }
}
