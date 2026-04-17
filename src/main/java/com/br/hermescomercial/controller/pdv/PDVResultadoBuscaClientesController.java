package com.br.hermescomercial.controller.pdv;

import com.br.hermescomercial.dao.UsuarioDao;
import com.br.hermescomercial.model.Usuario;
import com.br.hermescomercial.model.Cliente;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class PDVResultadoBuscaClientesController implements Initializable {

    // Componentes FXML
    @FXML private Label lblTermoBusca;
    @FXML private Label lblQuantidadeResultados;
    @FXML private TableView<Usuario> tblResultados;
    @FXML private TableColumn<Usuario, Long> colId;
    @FXML private TableColumn<Usuario, String> colNome;
    @FXML private TableColumn<Usuario, String> colDocumento;
    @FXML private TableColumn<Usuario, String> colTelefone;
    @FXML private TableColumn<Usuario, String> colEmail;
    @FXML private TableColumn<Usuario, String> colAcoes;
    
    // Labels de detalhes
    @FXML private Label lblDetalheId;
    @FXML private Label lblDetalheNome;
    @FXML private Label lblDetalheDocumento;
    @FXML private Label lblDetalheTelefone;
    @FXML private Label lblDetalheEmail;
    @FXML private Label lblDetalheEndereco;
    
    // Botões
    @FXML private Button btnFechar;
    @FXML private Button btnSelecionar;
    @FXML private Button btnNovaBusca;
    
    // Dados
    private ObservableList<Usuario> resultados;
    private UsuarioDao usuarioDao;
    private Usuario clienteSelecionado;
    private String termoBusca;
    
    // Callback para retornar cliente selecionado
    private ClienteSelectionCallback callback;
    
    public interface ClienteSelectionCallback {
        void onClienteSelecionado(Cliente cliente);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        usuarioDao = new UsuarioDao();
        resultados = FXCollections.observableArrayList();
        
        inicializarTabela();
        inicializarListeners();
    }
    
    private void inicializarTabela() {
        // Configurar colunas
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colNome.setCellValueFactory(new PropertyValueFactory<>("nome"));
        colTelefone.setCellValueFactory(new PropertyValueFactory<>("telefone"));
        colEmail.setCellValueFactory(new PropertyValueFactory<>("email"));
        
        // Coluna de documento (CPF/CNPJ)
        colDocumento.setCellValueFactory(cellData -> {
            Usuario usuario = cellData.getValue();
            String documento = usuario.getNumeroDocumeto();
            return new javafx.beans.property.SimpleStringProperty(documento != null ? documento : "-");
        });
        
        // Coluna de ações
        colAcoes.setCellFactory(param -> new TableCell<Usuario, String>() {
            private final Button btnSelecionarItem = new Button("Selecionar");
            
            {
                btnSelecionarItem.setStyle("-fx-background-color: #3498db; -fx-text-fill: white;");
                btnSelecionarItem.setOnAction(event -> {
                    Usuario usuario = getTableView().getItems().get(getIndex());
                    selecionarCliente(usuario);
                });
            }
            
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    setGraphic(btnSelecionarItem);
                }
            }
        });
        
        tblResultados.setItems(resultados);
    }
    
    private void inicializarListeners() {
        // Listener para seleção na tabela
        tblResultados.getSelectionModel().selectedItemProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue != null) {
                clienteSelecionado = newValue;
                atualizarDetalhes(newValue);
                btnSelecionar.setDisable(false);
            } else {
                clienteSelecionado = null;
                limparDetalhes();
                btnSelecionar.setDisable(true);
            }
        });
        
        // Botões inicialmente desabilitados
        btnSelecionar.setDisable(true);
    }
    
    public void carregarResultados(String termo, String tipoBusca) {
        this.termoBusca = termo;
        
        try {
            List<Usuario> usuarios;
            if ("nome".equals(tipoBusca)) {
                usuarios = usuarioDao.buscarClientePorNomeCpfCnpj(termo);
            } else {
                usuarios = usuarioDao.buscarClientePorNomeCpfCnpj(termo);
            }
            
            resultados.clear();
            resultados.addAll(usuarios);
            
            // Atualizar informações
            lblTermoBusca.setText("Termo da busca: " + termo + (tipoBusca != null ? " (por " + tipoBusca + ")" : ""));
            lblQuantidadeResultados.setText("Resultados encontrados: " + usuarios.size());
            
            if (resultados.isEmpty()) {
                mostrarAlerta("Nenhum cliente encontrado para o termo: " + termo, Alert.AlertType.INFORMATION);
            }
            
        } catch (Exception e) {
            mostrarAlerta("Erro ao carregar resultados: " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }
    
    private void atualizarDetalhes(Usuario usuario) {
        lblDetalheId.setText(String.valueOf(usuario.getId()));
        lblDetalheNome.setText(usuario.getNome() != null ? usuario.getNome() : "-");
        lblDetalheDocumento.setText(usuario.getNumeroDocumeto() != null ? usuario.getNumeroDocumeto() : "-");
        lblDetalheTelefone.setText(usuario.getTelefone() != null ? usuario.getTelefone() : "-");
        lblDetalheEmail.setText(usuario.getEmail() != null ? usuario.getEmail() : "-");
        
        // Endereço completo
        String endereco = "";
        if (usuario.getEndereco() != null) endereco += usuario.getEndereco();
        if (usuario.getBairro() != null) endereco += " - " + usuario.getBairro();
        if (usuario.getCidade() != null) endereco += ", " + usuario.getCidade();
        if (usuario.getEstado() != null) endereco += " - " + usuario.getEstado();
        if (usuario.getCep() != null) endereco += " CEP: " + usuario.getCep();
        
        lblDetalheEndereco.setText(endereco.isEmpty() ? "-" : endereco);
    }
    
    private void limparDetalhes() {
        lblDetalheId.setText("-");
        lblDetalheNome.setText("-");
        lblDetalheDocumento.setText("-");
        lblDetalheTelefone.setText("-");
        lblDetalheEmail.setText("-");
        lblDetalheEndereco.setText("-");
    }
    
    private void selecionarCliente(Usuario usuario) {
        if (callback != null) {
            // Converter Usuario para Cliente
            Cliente cliente = new Cliente();
            cliente.setId(usuario.getId());
            cliente.setNome(usuario.getNome());
            cliente.setCpf(usuario.getNumeroDocumeto());
            cliente.setEmail(usuario.getEmail());
            cliente.setTelefone(usuario.getTelefone());
            cliente.setEndereco(usuario.getEndereco());
            cliente.setBairro(usuario.getBairro());
            cliente.setCidade(usuario.getCidade());
            cliente.setEstado(usuario.getEstado());
            cliente.setCep(usuario.getCep());
            
            callback.onClienteSelecionado(cliente);
        }
        
        // Fechar a janela
        fecharJanela();
    }
    
    @FXML
    private void onSelecionar() {
        if (clienteSelecionado != null) {
            selecionarCliente(clienteSelecionado);
        }
    }
    
    @FXML
    private void onNovaBusca() {
        // Limpar resultados e detalhes
        resultados.clear();
        limparDetalhes();
        clienteSelecionado = null;
        btnSelecionar.setDisable(true);
        
        // Atualizar labels
        lblTermoBusca.setText("Termo da busca: -");
        lblQuantidadeResultados.setText("Resultados encontrados: 0");
    }
    
    @FXML
    private void onFechar() {
        fecharJanela();
    }
    
    private void fecharJanela() {
        // Fechar a janela atual
        javafx.stage.Stage stage = (javafx.stage.Stage) btnFechar.getScene().getWindow();
        stage.close();
    }
    
    private void mostrarAlerta(String mensagem, Alert.AlertType tipo) {
        Alert alert = new Alert(tipo);
        alert.setTitle("Resultado da Busca");
        alert.setHeaderText(null);
        alert.setContentText(mensagem);
        alert.showAndWait();
    }
    
    public void setCallback(ClienteSelectionCallback callback) {
        this.callback = callback;
    }
}
