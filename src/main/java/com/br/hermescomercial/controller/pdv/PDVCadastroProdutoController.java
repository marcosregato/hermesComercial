package com.br.hermescomercial.controller.pdv;

import com.br.hermescomercial.model.Produto;
import com.br.hermescomercial.service.ProdutoService;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.TextFormatter;
import javafx.collections.FXCollections;
// import javafx.collections.ObservableList; - não utilizado

import java.net.URL;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;
import java.util.Timer;
import java.util.TimerTask;

public class PDVCadastroProdutoController implements Initializable {

    // Service
    private ProdutoService produtoService;
    
    // Dados
    private Produto produtoEdicao;
    private boolean modoEdicao = false;
    
    // Timer para atualização de data/hora
    private Timer timer;
    private DateTimeFormatter formatadorDataHora = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");

    // Componentes FXML - Header
    @FXML private Label lblDataHora;
    @FXML private Label lblOperador;
    @FXML private Label lblTerminal;
    
    // Botões de Ação
    @FXML private Button btnNovo;
    @FXML private Button btnSalvar;
    @FXML private Button btnExcluir;
    @FXML private Button btnLimpar;
    @FXML private Button btnVoltar;
    
    // Dados Básicos
    @FXML private TextField txtCodigoInterno;
    @FXML private TextField txtCodigoBarras;
    @FXML private TextField txtNome;
    @FXML private TextArea txtDescricao;
    @FXML private ComboBox<String> comboCategoria;
    @FXML private ComboBox<String> comboUnidade;
    
    // Precificação
    @FXML private TextField txtPrecoCusto;
    @FXML private TextField txtPrecoVenda;
    @FXML private Label lblMargemLucro;
    
    // Estoque
    @FXML private TextField txtEstoqueAtual;
    @FXML private TextField txtEstoqueMinimo;
    @FXML private TextField txtEstoqueMaximo;
    
    // Status
    @FXML private CheckBox chkAtivo;
    @FXML private Label lblStatus;
    @FXML private Label lblMensagem;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        inicializarComponentes();
        inicializarListeners();
        inicializarCombos();
        inicializarTimer();
        configurarValidacaoCamposNumericos();
        carregarDadosIniciais();
    }

    private void inicializarComponentes() {
        // Inicializar Service
        this.produtoService = new ProdutoService();
        
        // Configurar informações do terminal
        lblTerminal.setText("Terminal: 001");
        lblOperador.setText("Operador: João");
        
        // Configurar status inicial
        atualizarStatus("Pronto para operar");
        
        // Configurar estado inicial dos botões
        btnExcluir.setDisable(true);
    }

    private void inicializarListeners() {
        // Listener para calcular margem de lucro automaticamente
        txtPrecoCusto.textProperty().addListener((obs, oldValue, newValue) -> calcularMargemLucro());
        txtPrecoVenda.textProperty().addListener((obs, oldValue, newValue) -> calcularMargemLucro());
        
        // Listener para validar campos obrigatórios
        txtNome.textProperty().addListener((obs, oldValue, newValue) -> validarCamposObrigatorios());
        comboCategoria.getSelectionModel().selectedItemProperty().addListener((obs, oldValue, newValue) -> validarCamposObrigatorios());
        comboUnidade.getSelectionModel().selectedItemProperty().addListener((obs, oldValue, newValue) -> validarCamposObrigatorios());
        txtPrecoCusto.textProperty().addListener((obs, oldValue, newValue) -> validarCamposObrigatorios());
        txtPrecoVenda.textProperty().addListener((obs, oldValue, newValue) -> validarCamposObrigatorios());
    }

    private void inicializarCombos() {
        // Configurar combo de categorias
        comboCategoria.setItems(FXCollections.observableArrayList(
            "Alimentos", "Bebidas", "Limpeza", "Higiene", "Eletrônicos", "Vestuário", "Outros"
        ));
        
        // Configurar combo de unidades
        comboUnidade.setItems(FXCollections.observableArrayList(
            "UN", "KG", "LT", "CX", "PCT", "MT", "M²", "M³"
        ));
    }

    private void inicializarTimer() {
        timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                javafx.application.Platform.runLater(() -> {
                    lblDataHora.setText(LocalDateTime.now().format(formatadorDataHora));
                });
            }
        }, 0, 1000);
    }

    private void configurarValidacaoCamposNumericos() {
        // Configurar TextFormatter para Preço de Custo (apenas números e vírgula decimal)
        txtPrecoCusto.setTextFormatter(new TextFormatter<>(change -> {
            String newText = change.getControlNewText().replaceAll("[^0-9,]", "");
            String[] partes = newText.split(",");
            if (partes.length > 2) {
                newText = partes[0] + "," + partes[1];
            }
            if (partes.length == 2 && partes[1].length() > 2) {
                newText = partes[0] + "," + partes[1].substring(0, 2);
            }
            if (!change.getControlNewText().equals(newText)) {
                change.setText(newText);
                mostrarMensagem("O campo Preço de Custo aceita apenas números e uma vírgula decimal. Caracteres inválidos foram removidos.", "warning");
            }
            return change;
        }));
        
        // Configurar TextFormatter para Preço de Venda (apenas números e vírgula decimal)
        txtPrecoVenda.setTextFormatter(new TextFormatter<>(change -> {
            String newText = change.getControlNewText().replaceAll("[^0-9,]", "");
            String[] partes = newText.split(",");
            if (partes.length > 2) {
                newText = partes[0] + "," + partes[1];
            }
            if (partes.length == 2 && partes[1].length() > 2) {
                newText = partes[0] + "," + partes[1].substring(0, 2);
            }
            if (!change.getControlNewText().equals(newText)) {
                change.setText(newText);
                mostrarMensagem("O campo Preço de Venda aceita apenas números e uma vírgula decimal. Caracteres inválidos foram removidos.", "warning");
            }
            return change;
        }));
        
        // Configurar TextFormatter para Estoque Atual (apenas números)
        txtEstoqueAtual.setTextFormatter(new TextFormatter<>(change -> {
            String newText = change.getControlNewText().replaceAll("[^0-9]", "");
            if (newText.length() > 6) newText = newText.substring(0, 6);
            if (!change.getControlNewText().equals(newText)) {
                change.setText(newText);
                mostrarMensagem("O campo Estoque Atual aceita apenas números. Caracteres inválidos foram removidos.", "warning");
            }
            return change;
        }));
        
        // Configurar TextFormatter para Estoque Mínimo (apenas números)
        txtEstoqueMinimo.setTextFormatter(new TextFormatter<>(change -> {
            String newText = change.getControlNewText().replaceAll("[^0-9]", "");
            if (newText.length() > 6) newText = newText.substring(0, 6);
            if (!change.getControlNewText().equals(newText)) {
                change.setText(newText);
                mostrarMensagem("O campo Estoque Mínimo aceita apenas números. Caracteres inválidos foram removidos.", "warning");
            }
            return change;
        }));
        
        // Configurar TextFormatter para Estoque Máximo (apenas números)
        txtEstoqueMaximo.setTextFormatter(new TextFormatter<>(change -> {
            String newText = change.getControlNewText().replaceAll("[^0-9]", "");
            if (newText.length() > 6) newText = newText.substring(0, 6);
            if (!change.getControlNewText().equals(newText)) {
                change.setText(newText);
                mostrarMensagem("O campo Estoque Máximo aceita apenas números. Caracteres inválidos foram removidos.", "warning");
            }
            return change;
        }));
        
        // Configurar TextFormatter para Código Interno (apenas números)
        txtCodigoInterno.setTextFormatter(new TextFormatter<>(change -> {
            String newText = change.getControlNewText().replaceAll("[^0-9]", "");
            if (newText.length() > 10) newText = newText.substring(0, 10);
            if (!change.getControlNewText().equals(newText)) {
                change.setText(newText);
                mostrarMensagem("O campo Código Interno aceita apenas números. Caracteres inválidos foram removidos.", "warning");
            }
            return change;
        }));
        
        // Configurar TextFormatter para Código de Barras (apenas números)
        txtCodigoBarras.setTextFormatter(new TextFormatter<>(change -> {
            String newText = change.getControlNewText().replaceAll("[^0-9]", "");
            if (newText.length() > 20) newText = newText.substring(0, 20);
            if (!change.getControlNewText().equals(newText)) {
                change.setText(newText);
                mostrarMensagem("O campo Código de Barras aceita apenas números. Caracteres inválidos foram removidos.", "warning");
            }
            return change;
        }));
    }

    private void carregarDadosIniciais() {
        limparCampos();
    }

    @FXML
    private void onNovo() {
        limparCampos();
        modoEdicao = false;
        btnExcluir.setDisable(true);
        atualizarStatus("Novo produto - Preencha os dados");
    }

    @FXML
    private void onSalvar() {
        try {
            if (!validarCamposObrigatorios()) {
                mostrarAlerta("Preencha todos os campos obrigatórios!", Alert.AlertType.WARNING);
                return;
            }

            Produto produto = criarProdutoDosCampos();

            if (modoEdicao && produtoEdicao != null) {
                // Atualizar produto existente
                produtoService.atualizar(produto);
                mostrarAlerta("Produto atualizado com sucesso!", Alert.AlertType.INFORMATION);
                atualizarStatus("Produto atualizado com sucesso");
            } else {
                // Inserir novo produto
                produtoService.salvar(produto);
                mostrarAlerta("Produto cadastrado com sucesso!", Alert.AlertType.INFORMATION);
                atualizarStatus("Produto cadastrado com sucesso");
                limparCampos();
            }

        } catch (Exception e) {
            mostrarAlerta("Erro ao salvar produto: " + e.getMessage(), Alert.AlertType.ERROR);
            atualizarStatus("Erro ao salvar produto");
        }
    }

    @FXML
    private void onExcluir() {
        if (produtoEdicao == null) {
            mostrarAlerta("Selecione um produto para excluir!", Alert.AlertType.WARNING);
            return;
        }

        if (confirmarExclusao()) {
            try {
                produtoService.remover(produtoEdicao.getNome());
                mostrarAlerta("Produto excluído com sucesso!", Alert.AlertType.INFORMATION);
                atualizarStatus("Produto excluído com sucesso");
                limparCampos();
            } catch (Exception e) {
                mostrarAlerta("Erro ao excluir produto: " + e.getMessage(), Alert.AlertType.ERROR);
                atualizarStatus("Erro ao excluir produto");
            }
        }
    }

    @FXML
    private void onLimpar() {
        limparCampos();
        modoEdicao = false;
        btnExcluir.setDisable(true);
        atualizarStatus("Campos limpos - Pronto para novo cadastro");
    }

    @FXML
    private void onVoltar() {
        fecharTela();
    }

    // Métodos Utilitários
    private void calcularMargemLucro() {
        try {
            String custoText = txtPrecoCusto.getText().replace(",", ".");
            String vendaText = txtPrecoVenda.getText().replace(",", ".");
            
            if (!custoText.isEmpty() && !vendaText.isEmpty()) {
                BigDecimal custo = new BigDecimal(custoText);
                BigDecimal venda = new BigDecimal(vendaText);
                
                if (custo.compareTo(BigDecimal.ZERO) > 0) {
                    BigDecimal margem = venda.subtract(custo)
                            .divide(custo, 2, RoundingMode.HALF_UP)
                            .multiply(new BigDecimal("100"));
                    lblMargemLucro.setText(margem.toString().replace(".", ",") + "%");
                    
                    // Definir cor baseada na margem
                    if (margem.compareTo(BigDecimal.ZERO) < 0) {
                        lblMargemLucro.setStyle("-fx-font-weight: bold; -fx-text-fill: #e74c3c;");
                    } else if (margem.compareTo(new BigDecimal("20")) < 0) {
                        lblMargemLucro.setStyle("-fx-font-weight: bold; -fx-text-fill: #f39c12;");
                    } else {
                        lblMargemLucro.setStyle("-fx-font-weight: bold; -fx-text-fill: #27ae60;");
                    }
                } else {
                    lblMargemLucro.setText("0,00%");
                    lblMargemLucro.setStyle("-fx-font-weight: bold; -fx-text-fill: #27ae60;");
                }
            } else {
                lblMargemLucro.setText("0,00%");
                lblMargemLucro.setStyle("-fx-font-weight: bold; -fx-text-fill: #27ae60;");
            }
        } catch (Exception e) {
            lblMargemLucro.setText("0,00%");
            lblMargemLucro.setStyle("-fx-font-weight: bold; -fx-text-fill: #27ae60;");
        }
    }

    private boolean validarCamposObrigatorios() {
        boolean valido = true;
        
        if (txtNome.getText() == null || txtNome.getText().trim().isEmpty()) {
            valido = false;
        }
        
        if (comboCategoria.getSelectionModel().getSelectedItem() == null) {
            valido = false;
        }
        
        if (comboUnidade.getSelectionModel().getSelectedItem() == null) {
            valido = false;
        }
        
        if (txtPrecoCusto.getText() == null || txtPrecoCusto.getText().trim().isEmpty()) {
            valido = false;
        }
        
        if (txtPrecoVenda.getText() == null || txtPrecoVenda.getText().trim().isEmpty()) {
            valido = false;
        }
        
        btnSalvar.setDisable(!valido);
        return valido;
    }

    private Produto criarProdutoDosCampos() {
        Produto produto = new Produto();
        
        // Usar campo código como código interno
        produto.setCodigo(txtCodigoInterno.getText().isEmpty() ? txtCodigoBarras.getText() : txtCodigoInterno.getText());
        produto.setCodigoBarras(txtCodigoBarras.getText());
        produto.setNome(txtNome.getText());
        produto.setCategoria(comboCategoria.getSelectionModel().getSelectedItem());
        produto.setUnidade(comboUnidade.getSelectionModel().getSelectedItem());
        
        // Usar nome como marca temporariamente
        produto.setMarca(txtNome.getText());
        
        // Converter preço de venda
        String vendaText = txtPrecoVenda.getText().replace(",", ".");
        if (!vendaText.isEmpty()) {
            produto.setPrecoVenda(new BigDecimal(vendaText));
        }
        
        // Converter estoque atual para campo estoque
        String estoqueText = txtEstoqueAtual.getText();
        if (!estoqueText.isEmpty()) {
            produto.setEstoque(Integer.parseInt(estoqueText));
        }
        
        // Data atual como data de compra
        java.time.format.DateTimeFormatter dtf = java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy");
        produto.setDataCompra(java.time.LocalDate.now().format(dtf));
        
        return produto;
    }

    private void limparCampos() {
        txtCodigoInterno.clear();
        txtCodigoBarras.clear();
        txtNome.clear();
        txtDescricao.clear();
        comboCategoria.getSelectionModel().clearSelection();
        comboUnidade.getSelectionModel().clearSelection();
        txtPrecoCusto.clear();
        txtPrecoVenda.clear();
        txtEstoqueAtual.clear();
        txtEstoqueMinimo.clear();
        txtEstoqueMaximo.clear();
        chkAtivo.setSelected(true);
        
        lblMargemLucro.setText("0,00%");
        lblMargemLucro.setStyle("-fx-font-weight: bold; -fx-text-fill: #27ae60;");
        
        produtoEdicao = null;
        modoEdicao = false;
        btnExcluir.setDisable(true);
        btnSalvar.setDisable(true);
        
        limparMensagem();
    }

    private void atualizarStatus(String mensagem) {
        lblStatus.setText(mensagem);
    }

    private void mostrarMensagem(String mensagem, String tipo) {
        lblMensagem.setText(mensagem);
        
        switch (tipo.toLowerCase()) {
            case "success":
                lblMensagem.setStyle("-fx-text-fill: #27ae60; -fx-font-size: 14px; -fx-font-weight: bold;");
                break;
            case "warning":
                lblMensagem.setStyle("-fx-text-fill: #f39c12; -fx-font-size: 14px; -fx-font-weight: bold;");
                break;
            case "error":
                lblMensagem.setStyle("-fx-text-fill: #e74c3c; -fx-font-size: 14px; -fx-font-weight: bold;");
                break;
            default:
                lblMensagem.setStyle("-fx-text-fill: #3498db; -fx-font-size: 14px; -fx-font-weight: bold;");
        }
        
        // Limpar mensagem após 5 segundos
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                javafx.application.Platform.runLater(() -> limparMensagem());
            }
        }, 5000);
    }

    private void limparMensagem() {
        lblMensagem.setText("");
    }

    private void mostrarAlerta(String mensagem, Alert.AlertType tipo) {
        Alert alert = new Alert(tipo);
        alert.setTitle("Cadastro de Produto");
        alert.setHeaderText(null);
        alert.setContentText(mensagem);
        alert.showAndWait();
    }

    private boolean confirmarExclusao() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmar Exclusão");
        alert.setHeaderText("Deseja realmente excluir este produto?");
        alert.setContentText("Esta ação não pode ser desfeita.");
        
        return alert.showAndWait().get() == ButtonType.OK;
    }

    private void fecharTela() {
        // Cancelar timer
        if (timer != null) {
            timer.cancel();
        }
        
        // Fechar a janela atual
        javafx.stage.Stage stage = (javafx.stage.Stage) lblStatus.getScene().getWindow();
        stage.close();
    }
}
