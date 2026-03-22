package com.br.hermescomercial.controller.usuario;

import com.br.hermescomercial.dao.UsuarioDao;
import com.br.hermescomercial.model.Usuario;
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

public class CadUsuarioController implements Initializable {

    private static final Logger logger = LogManager.getLogger(CadUsuarioController.class);

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
    private TextField txtCnpjCpf;

    @FXML
    private TextField txtEmail;

    @FXML
    private ComboBox<String> comboTipo;

    @FXML
    private Button btSalvar;

    @FXML
    private Button btExcluir;

    @FXML
    private Button btEditar;

    @FXML
    private TextField PesNome;

    private UsuarioDao dao = new UsuarioDao();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        if (comboEstado != null) {
            comboEstado.setItems(FXCollections.observableArrayList("AC", "AL", "AP", "AM", "BA", "CE", "DF", "ES", "GO", "MA", "MT", "MS", "MG", "PA", "PB", "PR", "PE", "PI", "RJ", "RN", "RS", "RO", "RR", "SC", "SP", "SE", "TO"));
        }
        if (comboTipo != null) {
            comboTipo.setItems(FXCollections.observableArrayList("Usuario", "Fornecedor", "Cliente", "Funcionário"));
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
            usuario.setCep(txtCep.getText());
            usuario.setEmail(txtEmail.getText());
            usuario.setTipousuario(comboTipo.getValue());

            if ("Fornecedor".equals(comboTipo.getValue())) {
                usuario.setTipoDocumento("CNPJ");
                usuario.setCnpj(txtCnpjCpf.getText());
            } else {
                usuario.setTipoDocumento("CPF");
                usuario.setCpf(txtCnpjCpf.getText());
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
            usuario.setCep(txtCep.getText());
            usuario.setEmail(txtEmail.getText());
            usuario.setTipousuario(comboTipo.getValue());

            if ("Fornecedor".equals(comboTipo.getValue())) {
                usuario.setTipoDocumento("CNPJ");
                usuario.setCnpj(txtCnpjCpf.getText());
            } else {
                usuario.setTipoDocumento("CPF");
                usuario.setCpf(txtCnpjCpf.getText());
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
                    txtCep.setText(p.getCep());
                    txtEmail.setText(p.getEmail());
                    comboTipo.setValue(p.getTipousuario());
                    txtCnpjCpf.setText("Fornecedor".equals(p.getTipousuario()) ? p.getCnpj() : p.getCpf());
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
