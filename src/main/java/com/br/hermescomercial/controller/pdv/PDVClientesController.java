package com.br.hermescomercial.controller.pdv;

import com.br.hermescomercial.model.Cliente;
import com.br.hermescomercial.dao.ClienteDao;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.net.URL;
// import java.time.LocalDate; - não utilizado
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class PDVClientesController implements Initializable {

    // DAO
    private ClienteDao clienteDao;
    
    // Dados
    private ObservableList<Cliente> clientesEncontrados;
    private Cliente clienteSelecionado;
    private Cliente clienteEdicao;
    
    // Timer para atualização de data/hora
    private Timer timer;
    private DateTimeFormatter formatadorDataHora = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");

    // Componentes FXML - Header
    @FXML private Label lblDataHora;
    @FXML private Button btnVoltar;
    
    // Área de Busca
    @FXML private TextField txtBuscarCliente;
    @FXML private Button btnBuscar;
    @FXML private Button btnLimparBusca;
    @FXML private CheckBox chkApenasAtivos;
    @FXML private CheckBox chkPessoaFisica;
    @FXML private CheckBox chkPessoaJuridica;
    
    // Lista de Clientes
    @FXML private TableView<Cliente> tblClientes;
    @FXML private TableColumn<Cliente, String> colNome;
    @FXML private TableColumn<Cliente, String> colDocumento;
    @FXML private TableColumn<Cliente, String> colTelefone;
    @FXML private TableColumn<Cliente, String> colCidade;
    @FXML private TableColumn<Cliente, String> colStatus;
    
    // Botões da Lista
    @FXML private Button btnNovoCliente;
    @FXML private Button btnEditarCliente;
    @FXML private Button btnSelecionarCliente;
    
    // Formulário - Tipo de Pessoa
    @FXML private RadioButton rbPessoaFisica;
    @FXML private RadioButton rbPessoaJuridica;
    
    // Formulário - Pessoa Física
    @FXML private javafx.scene.layout.VBox panePessoaFisica;
    @FXML private TextField txtNome;
    @FXML private TextField txtCPF;
    @FXML private TextField txtRG;
    @FXML private DatePicker dpDataNascimento;
    
    // Formulário - Pessoa Jurídica
    @FXML private javafx.scene.layout.VBox panePessoaJuridica;
    @FXML private TextField txtRazaoSocial;
    @FXML private TextField txtNomeFantasia;
    @FXML private TextField txtCNPJ;
    @FXML private TextField txtInscricaoEstadual;
    
    // Formulário - Contato
    @FXML private TextField txtTelefone;
    @FXML private TextField txtCelular;
    @FXML private TextField txtEmail;
    
    // Formulário - Endereço
    @FXML private TextField txtCEP;
    @FXML private Button btnBuscarCEP;
    @FXML private TextField txtEndereco;
    @FXML private TextField txtNumero;
    @FXML private TextField txtComplemento;
    @FXML private TextField txtBairro;
    @FXML private TextField txtCidade;
    @FXML private ComboBox<String> comboEstado;
    
    // Formulário - Informações Adicionais
    @FXML private TextArea txtObservacoes;
    @FXML private CheckBox chkAtivo;
    
    // Botões do Formulário
    @FXML private Button btnSalvar;
    @FXML private Button btnLimpar;
    @FXML private Button btnExcluir;
    
    // Status
    @FXML private Label lblStatus;
    @FXML private Label lblOperador;
    @FXML private Label lblTerminal;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        inicializarComponentes();
        inicializarListeners();
        inicializarTabelas();
        inicializarTimer();
        configurarValidacaoCamposNumericos();
        carregarDadosIniciais();
    }

    private void inicializarComponentes() {
        // Inicializar DAO
        this.clienteDao = new ClienteDao();
        
        // Inicializar lista observável
        this.clientesEncontrados = FXCollections.observableArrayList();
        
        // Configurar combo de estados
        comboEstado.setItems(FXCollections.observableArrayList(
            "AC", "AL", "AP", "AM", "BA", "CE", "DF", "ES", "GO", "MA", "MT", "MS", "MG",
            "PA", "PB", "PR", "PE", "PI", "RJ", "RN", "RS", "RO", "RR", "SC", "SP", "SE", "TO"
        ));
        
        // Configurar informações do terminal
        lblTerminal.setText("Terminal: 001");
        lblOperador.setText("Operador: João");
        
        // Configurar status inicial
        atualizarStatus("Pronto para operar");
        
        // Configurar tipo de pessoa padrão
        rbPessoaFisica.setSelected(true);
        mostrarFormularioPessoaFisica();
    }

    private void inicializarListeners() {
        // Listener para busca automática
        txtBuscarCliente.textProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue != null && newValue.length() >= 3) {
                buscarClientes();
            }
        });
        
        // Listener para seleção na tabela
        tblClientes.getSelectionModel().selectedItemProperty().addListener((obs, oldValue, newValue) -> {
            clienteSelecionado = newValue;
            atualizarBotoesLista();
            if (newValue != null) {
                carregarClienteNoFormulario(newValue);
            }
        });
        
        // Listener para tipo de pessoa
        rbPessoaFisica.selectedProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue) {
                mostrarFormularioPessoaFisica();
            }
        });
        
        rbPessoaJuridica.selectedProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue) {
                mostrarFormularioPessoaJuridica();
            }
        });
    }

    private void inicializarTabelas() {
        // Configurar tabela de clientes
        colNome.setCellValueFactory(new PropertyValueFactory<>("nome"));
        colDocumento.setCellValueFactory(cellData -> {
            Cliente cliente = cellData.getValue();
            return javafx.beans.binding.Bindings.createStringBinding(() -> 
                "FISICA".equals(cliente.getTipoPessoa()) ? cliente.getCpf() : cliente.getCnpj());
        });
        colTelefone.setCellValueFactory(new PropertyValueFactory<>("telefone"));
        colCidade.setCellValueFactory(new PropertyValueFactory<>("cidade"));
        colStatus.setCellValueFactory(cellData -> {
            Cliente cliente = cellData.getValue();
            return javafx.beans.binding.Bindings.createStringBinding(() -> 
                cliente.isAtivo() ? "ATIVO" : "INATIVO");
        });
        
        tblClientes.setItems(clientesEncontrados);
        
        // Configurar renderização customizada para status
        colStatus.setCellFactory(column -> new TableCell<Cliente, String>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                
                if (empty || item == null) {
                    setText(null);
                    setStyle("");
                } else {
                    setText(item);
                    if ("INATIVO".equals(item)) {
                        setStyle("-fx-text-fill: #e74c3c; -fx-font-weight: bold;");
                    } else {
                        setStyle("-fx-text-fill: #27ae60; -fx-font-weight: bold;");
                    }
                }
            }
        });
    }

    private void inicializarTimer() {
        timer = new Timer(true);
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                javafx.application.Platform.runLater(() -> {
                    lblDataHora.setText(LocalDateTime.now().format(formatadorDataHora));
                });
            }
        }, 0, 1000);
    }

    private void carregarDadosIniciais() {
        carregarTodosClientes();
        atualizarBotoesLista();
        limparFormulario();
    }

    // Métodos de Ação dos Botões
    @FXML
    private void onVoltar() {
        fecharTela();
    }

    @FXML
    private void onBuscar() {
        buscarClientes();
    }

    @FXML
    private void onLimparBusca() {
        txtBuscarCliente.clear();
        chkApenasAtivos.setSelected(true);
        chkPessoaFisica.setSelected(false);
        chkPessoaJuridica.setSelected(false);
        carregarTodosClientes();
    }

    @FXML
    private void onNovoCliente() {
        clienteEdicao = null;
        limparFormulario();
        rbPessoaFisica.setSelected(true);
        chkAtivo.setSelected(true);
        txtNome.requestFocus();
        atualizarStatus("Novo cliente - preencha os dados");
    }

    @FXML
    private void onEditarCliente() {
        if (clienteSelecionado != null) {
            clienteEdicao = clienteSelecionado;
            carregarClienteNoFormulario(clienteSelecionado);
            atualizarStatus("Editando cliente: " + clienteSelecionado.getNome());
        }
    }

    @FXML
    private void onSelecionarCliente() {
        if (clienteSelecionado != null) {
            // Retornar cliente selecionado para a tela principal
            mostrarAlerta("Cliente selecionado: " + clienteSelecionado.getNome(), Alert.AlertType.INFORMATION);
            fecharTela();
        }
    }

    @FXML
    private void onSalvar() {
        if (validarFormulario()) {
            try {
                Cliente cliente = criarClienteDoFormulario();
                
                if (clienteEdicao == null) {
                    // Novo cliente
                    clienteDao.salvar(cliente);
                    atualizarStatus("Cliente salvo com sucesso: " + cliente.getNome());
                } else {
                    // Editando cliente
                    cliente.setId(clienteEdicao.getId());
                    clienteDao.update(cliente);
                    atualizarStatus("Cliente atualizado com sucesso: " + cliente.getNome());
                }
                
                // Recarregar lista e limpar formulário
                carregarTodosClientes();
                limparFormulario();
                clienteEdicao = null;
                
            } catch (Exception e) {
                atualizarStatus("Erro ao salvar cliente: " + e.getMessage());
                mostrarAlerta("Erro ao salvar cliente: " + e.getMessage(), Alert.AlertType.ERROR);
            }
        }
    }

    @FXML
    private void onLimpar() {
        limparFormulario();
        clienteEdicao = null;
        atualizarStatus("Formulário limpo");
    }

    @FXML
    private void onExcluir() {
        if (clienteSelecionado != null && confirmarExclusao()) {
            try {
                clienteDao.remove(clienteSelecionado.getId().toString());
                atualizarStatus("Cliente excluído com sucesso");
                carregarTodosClientes();
                limparFormulario();
                clienteEdicao = null;
            } catch (Exception e) {
                atualizarStatus("Erro ao excluir cliente: " + e.getMessage());
                mostrarAlerta("Erro ao excluir cliente: " + e.getMessage(), Alert.AlertType.ERROR);
            }
        }
    }

    @FXML
    private void onBuscarCEP() {
        String cep = txtCEP.getText();
        if (cep != null && !cep.trim().isEmpty()) {
            buscarEnderecoPorCEP(cep);
        }
    }

    // Métodos de Busca
    private void buscarClientes() {
        try {
            String busca = txtBuscarCliente.getText();
            boolean apenasAtivos = chkApenasAtivos.isSelected();
            boolean apenasFisica = chkPessoaFisica.isSelected();
            boolean apenasJuridica = chkPessoaJuridica.isSelected();
            
            List<Cliente> clientes = clienteDao.buscarComFiltros(busca, apenasAtivos, apenasFisica, apenasJuridica);
            
            clientesEncontrados.clear();
            clientesEncontrados.addAll(clientes);
            
            atualizarStatus(clientes.size() + " clientes encontrados");
            
        } catch (Exception e) {
            atualizarStatus("Erro na busca: " + e.getMessage());
        }
    }

    private void carregarTodosClientes() {
        try {
            List<Cliente> clientes = clienteDao.listar();
            
            clientesEncontrados.clear();
            
            // Filtrar apenas clientes ativos se checkbox estiver marcado
            for (Cliente cliente : clientes) {
                if (!chkApenasAtivos.isSelected() || cliente.isAtivo()) {
                    clientesEncontrados.add(cliente);
                }
            }
            
            atualizarStatus("Todos os clientes carregados");
            
        } catch (Exception e) {
            atualizarStatus("Erro ao carregar clientes: " + e.getMessage());
        }
    }

    // Métodos do Formulário
    private void mostrarFormularioPessoaFisica() {
        panePessoaFisica.setVisible(true);
        panePessoaJuridica.setVisible(false);
    }

    private void mostrarFormularioPessoaJuridica() {
        panePessoaFisica.setVisible(false);
        panePessoaJuridica.setVisible(true);
    }

    private void carregarClienteNoFormulario(Cliente cliente) {
        if (cliente == null) return;
        
        // Tipo de pessoa
        if ("FISICA".equals(cliente.getTipoPessoa())) {
            rbPessoaFisica.setSelected(true);
            txtNome.setText(cliente.getNome());
            txtCPF.setText(cliente.getCpf());
            txtRG.setText(cliente.getRg());
            if (cliente.getDataNascimento() != null) {
                // Cliente usa String para dataNascimento, mas dpDataNascimento espera LocalDate
                // Simplificado - não converter por enquanto
            }
        } else {
            rbPessoaJuridica.setSelected(true);
            txtRazaoSocial.setText(cliente.getNome());
            txtNomeFantasia.setText(cliente.getNomeFantasia());
            txtCNPJ.setText(cliente.getCnpj());
            txtInscricaoEstadual.setText(cliente.getInscricaoEstadual());
        }
        
        // Contato
        txtTelefone.setText(cliente.getTelefone());
        txtCelular.setText(cliente.getCelular());
        txtEmail.setText(cliente.getEmail());
        
        // Endereço
        txtCEP.setText(cliente.getCep());
        txtEndereco.setText(cliente.getEndereco());
        txtNumero.setText(cliente.getNumero());
        txtComplemento.setText(cliente.getComplemento());
        txtBairro.setText(cliente.getBairro());
        txtCidade.setText(cliente.getCidade());
        if (cliente.getEstado() != null) {
            comboEstado.getSelectionModel().select(cliente.getEstado());
        }
        
        // Informações adicionais
        txtObservacoes.setText(cliente.getObservacoes());
        chkAtivo.setSelected(cliente.isAtivo());
    }

    private void limparFormulario() {
        // Tipo de pessoa
        rbPessoaFisica.setSelected(true);
        
        // Pessoa Física
        txtNome.clear();
        txtCPF.clear();
        txtRG.clear();
        dpDataNascimento.setValue(null);
        
        // Pessoa Jurídica
        txtRazaoSocial.clear();
        txtNomeFantasia.clear();
        txtCNPJ.clear();
        txtInscricaoEstadual.clear();
        
        // Contato
        txtTelefone.clear();
        txtCelular.clear();
        txtEmail.clear();
        
        // Endereço
        txtCEP.clear();
        txtEndereco.clear();
        txtNumero.clear();
        txtComplemento.clear();
        txtBairro.clear();
        txtCidade.clear();
        comboEstado.getSelectionModel().clearSelection();
        
        // Informações adicionais
        txtObservacoes.clear();
        chkAtivo.setSelected(true);
    }

    private Cliente criarClienteDoFormulario() {
        Cliente cliente = new Cliente();
        
        // Tipo de pessoa
        if (rbPessoaFisica.isSelected()) {
            cliente.setTipoPessoa("FISICA");
            cliente.setNome(txtNome.getText());
            cliente.setCpf(txtCPF.getText());
            cliente.setRg(txtRG.getText());
            cliente.setDataNascimento(dpDataNascimento.getValue() != null ? 
                dpDataNascimento.getValue().toString() : null);
        } else {
            cliente.setTipoPessoa("JURIDICA");
            cliente.setNome(txtRazaoSocial.getText());
            cliente.setNomeFantasia(txtNomeFantasia.getText());
            cliente.setCnpj(txtCNPJ.getText());
            cliente.setInscricaoEstadual(txtInscricaoEstadual.getText());
        }
        
        // Contato
        cliente.setTelefone(txtTelefone.getText());
        cliente.setCelular(txtCelular.getText());
        cliente.setEmail(txtEmail.getText());
        
        // Endereço
        cliente.setCep(txtCEP.getText());
        cliente.setEndereco(txtEndereco.getText());
        cliente.setNumero(txtNumero.getText());
        cliente.setComplemento(txtComplemento.getText());
        cliente.setBairro(txtBairro.getText());
        cliente.setCidade(txtCidade.getText());
        cliente.setEstado(comboEstado.getSelectionModel().getSelectedItem());
        
        // Informações adicionais
        cliente.setObservacoes(txtObservacoes.getText());
        cliente.setAtivo(chkAtivo.isSelected());
        
        return cliente;
    }

    private boolean validarFormulario() {
        StringBuilder erros = new StringBuilder();
        
        // Validação básica
        if (rbPessoaFisica.isSelected()) {
            if (txtNome.getText() == null || txtNome.getText().trim().isEmpty()) {
                erros.append("Nome é obrigatório.\n");
            }
            if (txtCPF.getText() == null || !validarCPF(txtCPF.getText())) {
                erros.append("CPF inválido.\n");
            }
        } else {
            if (txtRazaoSocial.getText() == null || txtRazaoSocial.getText().trim().isEmpty()) {
                erros.append("Razão Social é obrigatória.\n");
            }
            if (txtCNPJ.getText() == null || !validarCNPJ(txtCNPJ.getText())) {
                erros.append("CNPJ inválido.\n");
            }
        }
        
        // Validação de contato
        if (txtTelefone.getText() == null || txtTelefone.getText().trim().isEmpty()) {
            erros.append("Telefone é obrigatório.\n");
        }
        
        if (txtEmail.getText() != null && !txtEmail.getText().trim().isEmpty() && !validarEmail(txtEmail.getText())) {
            erros.append("E-mail inválido.\n");
        }
        
        if (erros.length() > 0) {
            mostrarAlerta("Erros de validação:\n" + erros.toString(), Alert.AlertType.ERROR);
            return false;
        }
        
        return true;
    }

    // Métodos Utilitários
    private void buscarEnderecoPorCEP(String cep) {
        try {
            // Simular busca de CEP (implementar serviço real)
            atualizarStatus("Buscando endereço para CEP: " + cep);
            
            // Dados simulados
            txtEndereco.setText("Rua Exemplo");
            txtBairro.setText("Centro");
            txtCidade.setText("São Paulo");
            comboEstado.getSelectionModel().select("SP");
            
            atualizarStatus("Endereço encontrado para o CEP");
            
        } catch (Exception e) {
            atualizarStatus("Erro ao buscar CEP: " + e.getMessage());
        }
    }

    private boolean validarCPF(String cpf) {
        // Validação simplificada de CPF
        return cpf != null && cpf.replaceAll("\\D", "").length() == 11;
    }

    private boolean validarCNPJ(String cnpj) {
        // Validação simplificada de CNPJ
        return cnpj != null && cnpj.replaceAll("\\D", "").length() == 14;
    }

    private boolean validarEmail(String email) {
        // Validação simplificada de e-mail
        return email != null && email.contains("@") && email.contains(".");
    }

    private void atualizarBotoesLista() {
        boolean temSelecao = clienteSelecionado != null;
        
        btnEditarCliente.setDisable(!temSelecao);
        btnSelecionarCliente.setDisable(!temSelecao);
        btnExcluir.setDisable(!temSelecao);
    }

    private void atualizarStatus(String mensagem) {
        lblStatus.setText(mensagem);
        
        // Atualizar cor do status
        if (mensagem.toLowerCase().contains("erro")) {
            lblStatus.setStyle("-fx-text-fill: #e74c3c; -fx-font-weight: bold;");
        } else if (mensagem.toLowerCase().contains("sucesso") || mensagem.toLowerCase().contains("salvo")) {
            lblStatus.setStyle("-fx-text-fill: #28a745; -fx-font-weight: bold;");
        } else {
            lblStatus.setStyle("-fx-text-fill: #f39c12; -fx-font-weight: bold;");
        }
    }

    private void mostrarAlerta(String mensagem, Alert.AlertType tipo) {
        Alert alert = new Alert(tipo);
        alert.setTitle("Gerenciamento de Clientes");
        alert.setHeaderText(null);
        alert.setContentText(mensagem);
        alert.showAndWait();
    }

    private boolean confirmarExclusao() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmar Exclusão");
        alert.setHeaderText("Deseja realmente excluir este cliente?");
        alert.setContentText("Esta ação não pode ser desfeita.");
        
        return alert.showAndWait().get() == ButtonType.OK;
    }

    private void configurarValidacaoCamposNumericos() {
        // Configurar TextFormatter para CPF (apenas números, máximo 11 dígitos)
        txtCPF.setTextFormatter(new TextFormatter<>(change -> {
            String newText = change.getControlNewText().replaceAll("[^0-9]", "");
            if (newText.length() > 11) newText = newText.substring(0, 11);
            if (!change.getControlNewText().equals(newText)) {
                change.setText(newText);
                mostrarAlerta("O campo CPF aceita apenas números. Caracteres inválidos foram removidos.", Alert.AlertType.WARNING);
            }
            return change;
        }));
        
        // Configurar TextFormatter para CNPJ (apenas números, máximo 14 dígitos)
        txtCNPJ.setTextFormatter(new TextFormatter<>(change -> {
            String newText = change.getControlNewText().replaceAll("[^0-9]", "");
            if (newText.length() > 14) newText = newText.substring(0, 14);
            if (!change.getControlNewText().equals(newText)) {
                change.setText(newText);
                mostrarAlerta("O campo CNPJ aceita apenas números. Caracteres inválidos foram removidos.", Alert.AlertType.WARNING);
            }
            return change;
        }));
        
        // Configurar TextFormatter para RG (apenas números e letras, máximo 15 caracteres)
        txtRG.setTextFormatter(new TextFormatter<>(change -> {
            String newText = change.getControlNewText().replaceAll("[^0-9A-Za-z]", "");
            if (newText.length() > 15) newText = newText.substring(0, 15);
            if (!change.getControlNewText().equals(newText)) {
                change.setText(newText);
                mostrarAlerta("O campo RG aceita apenas números e letras. Caracteres inválidos foram removidos.", Alert.AlertType.WARNING);
            }
            return change;
        }));
        
        // Configurar TextFormatter para Telefone (apenas números, máximo 11 dígitos)
        txtTelefone.setTextFormatter(new TextFormatter<>(change -> {
            String newText = change.getControlNewText().replaceAll("[^0-9]", "");
            if (newText.length() > 11) newText = newText.substring(0, 11);
            if (!change.getControlNewText().equals(newText)) {
                change.setText(newText);
                mostrarAlerta("O campo Telefone aceita apenas números. Caracteres inválidos foram removidos.", Alert.AlertType.WARNING);
            }
            return change;
        }));
        
        // Configurar TextFormatter para Celular (apenas números, máximo 11 dígitos)
        txtCelular.setTextFormatter(new TextFormatter<>(change -> {
            String newText = change.getControlNewText().replaceAll("[^0-9]", "");
            if (newText.length() > 11) newText = newText.substring(0, 11);
            if (!change.getControlNewText().equals(newText)) {
                change.setText(newText);
                mostrarAlerta("O campo Celular aceita apenas números. Caracteres inválidos foram removidos.", Alert.AlertType.WARNING);
            }
            return change;
        }));
        
        // Configurar TextFormatter para CEP (apenas números, máximo 8 dígitos)
        txtCEP.setTextFormatter(new TextFormatter<>(change -> {
            String newText = change.getControlNewText().replaceAll("[^0-9]", "");
            if (newText.length() > 8) newText = newText.substring(0, 8);
            if (!change.getControlNewText().equals(newText)) {
                change.setText(newText);
                mostrarAlerta("O campo CEP aceita apenas números. Caracteres inválidos foram removidos.", Alert.AlertType.WARNING);
            }
            return change;
        }));
        
        // Configurar TextFormatter para campo de busca (apenas números para CPF/CNPJ)
        txtBuscarCliente.setTextFormatter(new TextFormatter<>(change -> {
            String newText = change.getControlNewText().replaceAll("[^0-9]", "");
            if (newText.length() > 18) newText = newText.substring(0, 18);
            if (!change.getControlNewText().equals(newText)) {
                change.setText(newText);
                mostrarAlerta("O campo de busca por CPF/CNPJ aceita apenas números. Caracteres inválidos foram removidos.", Alert.AlertType.WARNING);
            }
            return change;
        }));
    }

    private void fecharTela() {
        // Fechar a janela atual
        javafx.stage.Stage stage = (javafx.stage.Stage) lblStatus.getScene().getWindow();
        stage.close();
    }

    // Getters e Setters
    public Cliente getClienteSelecionado() {
        return clienteSelecionado;
    }

    public void setClienteSelecionado(Cliente clienteSelecionado) {
        this.clienteSelecionado = clienteSelecionado;
    }
}
