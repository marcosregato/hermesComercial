package com.br.hermescomercial.controller.pessoa;

import com.br.hermescomercial.dao.PessoaDao;
import com.br.hermescomercial.model.Pessoa;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import org.apache.logging.log4j.LogManager;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.List;

public class CadPessoaController implements Initializable {

    private static final org.apache.logging.log4j.Logger logger = LogManager.getLogger(CadPessoaController.class);

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

    private PessoaDao dao = new PessoaDao();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        if (comboEstado != null) {
            comboEstado.setItems(FXCollections.observableArrayList("AC", "AL", "AP", "AM", "BA", "CE", "DF", "ES", "GO", "MA", "MT", "MS", "MG", "PA", "PB", "PR", "PE", "PI", "RJ", "RN", "RS", "RO", "RR", "SC", "SP", "SE", "TO"));
        }
        if (comboTipo != null) {
            comboTipo.setItems(FXCollections.observableArrayList("Usuário", "Fornecedor", "Cliente", "Funcionário"));
        }
    }

    public void salvar() {
        try {
            Pessoa pessoa = new Pessoa();
            pessoa.setNome(txtNome.getText());
            pessoa.setEndereco(txtEndereco.getText());
            pessoa.setBairro(txtBairro.getText());
            pessoa.setCidade(txtCidade.getText());
            pessoa.setEstado(comboEstado.getValue());
            pessoa.setCep(txtCep.getText());
            pessoa.setEmail(txtEmail.getText());
            pessoa.setTipoUsuario(comboTipo.getValue());

            if ("Fornecedor".equals(comboTipo.getValue())) {
                pessoa.setCnpj(txtCnpjCpf.getText());
            } else {
                pessoa.setCpf(txtCnpjCpf.getText());
            }

            dao.salvar(pessoa);
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
            Pessoa pessoa = new Pessoa();
            pessoa.setNome(txtNome.getText());
            pessoa.setEndereco(txtEndereco.getText());
            pessoa.setBairro(txtBairro.getText());
            pessoa.setCidade(txtCidade.getText());
            pessoa.setEstado(comboEstado.getValue());
            pessoa.setCep(txtCep.getText());
            pessoa.setEmail(txtEmail.getText());
            pessoa.setTipoUsuario(comboTipo.getValue());

            if ("Fornecedor".equals(comboTipo.getValue())) {
                pessoa.setCnpj(txtCnpjCpf.getText());
            } else {
                pessoa.setCpf(txtCnpjCpf.getText());
            }

            dao.update(pessoa);
        } catch (Exception e) {
            logger.error("Erro ao atualizar: " + e.getMessage());
        }
    }

    public void buscar() {
        try {
            if (PesNome != null && !PesNome.getText().isEmpty()) {
                List<Pessoa> resultados = dao.buscar(PesNome.getText());
                if (!resultados.isEmpty()) {
                    Pessoa p = resultados.get(0);
                    txtNome.setText(p.getNome());
                    txtEndereco.setText(p.getEndereco());
                    txtBairro.setText(p.getBairro());
                    txtCidade.setText(p.getCidade());
                    comboEstado.setValue(p.getEstado());
                    txtCep.setText(p.getCep());
                    txtEmail.setText(p.getEmail());
                    comboTipo.setValue(p.getTipoPessoa());
                    txtCnpjCpf.setText("Fornecedor".equals(p.getTipoPessoa()) ? p.getCnpj() : p.getCpf());
                }
            }
        } catch (Exception e) {
            logger.error("Erro ao buscar pessoa: " + e.getMessage());
        }
    }

    public void lista() {
        try {
            List<Pessoa> todos = dao.lista();
            for (Pessoa p : todos) {
                logger.info("Pessoa na lista: " + p.getNome());
            }
        } catch (Exception e) {
            logger.error("Erro ao listar pessoas: " + e.getMessage());
        }
    }
}
