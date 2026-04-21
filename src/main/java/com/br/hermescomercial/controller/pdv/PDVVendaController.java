package com.br.hermescomercial.controller.pdv;

// import com.br.hermescomercial.business.pdv.PDVManager; - não utilizado
import com.br.hermescomercial.model.ItemVenda;
import com.br.hermescomercial.model.Produto;
import com.br.hermescomercial.model.VendaPDV;
import com.br.hermescomercial.model.Usuario;
// import com.br.hermescomercial.model.Cliente; - não utilizado

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.net.URL;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;
import java.util.Timer;
import java.util.TimerTask;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class PDVVendaController implements Initializable {

    private static final Logger logger = LogManager.getLogger(PDVVendaController.class);
    
    // private PDVManager pdvManager; - não utilizado
    private VendaPDV vendaAtual;
    private Usuario operadorAtual;
    private ObservableList<Produto> produtosDisponiveis;
    private ObservableList<ItemVenda> itensCarrinho;
    private Timer timer;
    private DateTimeFormatter formatadorDataHora = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");

    // Componentes FXML - Cabeçalho
    @FXML private Label lblNumeroVenda;
    @FXML private Label lblDataHora;
    @FXML private Label lblOperador;
    @FXML private Button btnVoltar;

    // Componentes FXML - Produtos
    @FXML private TextField txtBuscarProduto;
    @FXML private Button btnBuscar;
    @FXML private Button btnCodigoBarras;
    @FXML private TableView<Produto> tblProdutos;
    @FXML private TableColumn<Produto, String> colCodigo;
    @FXML private TableColumn<Produto, String> colDescricao;
    @FXML private TableColumn<Produto, BigDecimal> colPreco;
    @FXML private TableColumn<Produto, Integer> colEstoque;
    @FXML private TableColumn<Produto, String> colAcoes;

    // Componentes FXML - Carrinho
    @FXML private TableView<ItemVenda> tblCarrinho;
    @FXML private TableColumn<ItemVenda, String> colItemCodigo;
    @FXML private TableColumn<ItemVenda, String> colItemDescricao;
    @FXML private TableColumn<ItemVenda, Integer> colItemQtd;
    @FXML private TableColumn<ItemVenda, BigDecimal> colItemValor;
    @FXML private TableColumn<ItemVenda, BigDecimal> colItemTotal;
    @FXML private TableColumn<ItemVenda, String> colItemAcoes;

    // Componentes FXML - Resumo do Carrinho
    @FXML private Label lblSubtotal;
    @FXML private Label lblDesconto;
    @FXML private Label lblTotal;
    @FXML private Button btnDesconto;
    @FXML private Button btnCancelarItem;
    @FXML private Button btnLimparCarrinho;

    // Componentes FXML - Cliente
    @FXML private TextField txtBuscarClienteCpfCnpj;
    @FXML private Button btnBuscarCliente;
    @FXML private Label lblNomeCliente;
    @FXML private Label lblCpfCnpjCliente;
    @FXML private Button btnLimparCliente;

    // Componentes FXML - Pagamento
    @FXML private RadioButton rbDinheiro;
    @FXML private RadioButton rbCartao;
    @FXML private RadioButton rbPix;
    @FXML private RadioButton rbOutro;
    @FXML private VBox paneDinheiro;
    @FXML private TextField txtValorRecebido;
    @FXML private Label lblTroco;

    // Componentes FXML - Finalização
    @FXML private Button btnFinalizar;
    @FXML private Button btnCancelar;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        inicializarComponentes();
        inicializarTabelas();
        carregarProdutosExemplo(); // Carregar produtos após configurar as tabelas
        inicializarListeners();
        configurarCampoCpfCnpj();
        inicializarTimer();
        carregarDadosIniciais();
    }

    // public void setPDVManager(PDVManager pdvManager) - método não utilizado
    // this.pdvManager = pdvManager != null ? pdvManager : PDVManager.getInstance();

    public void setOperadorAtual(Usuario operadorAtual) {
        this.operadorAtual = operadorAtual;
        atualizarInformacoesOperador();
    }

    private void inicializarComponentes() {
        produtosDisponiveis = FXCollections.observableArrayList();
        itensCarrinho = FXCollections.observableArrayList();
        
        // Criar nova venda
        vendaAtual = new VendaPDV();
        vendaAtual.setNumeroCupom("V" + System.currentTimeMillis());
        vendaAtual.setDataVenda(LocalDateTime.now());
        
        // Configurar grupo de botões de pagamento
        ToggleGroup grupoPagamento = new ToggleGroup();
        rbDinheiro.setToggleGroup(grupoPagamento);
        rbCartao.setToggleGroup(grupoPagamento);
        rbPix.setToggleGroup(grupoPagamento);
        rbOutro.setToggleGroup(grupoPagamento);
        
        // Listener para mudança de forma de pagamento
        grupoPagamento.selectedToggleProperty().addListener((obs, oldVal, newVal) -> {
            atualizarCamposPagamento();
        });
    }

    private void inicializarTabelas() {
        // Configurar tabela de produtos
        colCodigo.setCellValueFactory(new PropertyValueFactory<>("codigo"));
        colDescricao.setCellValueFactory(new PropertyValueFactory<>("nome"));
        colPreco.setCellValueFactory(new PropertyValueFactory<>("precoVenda"));
        colEstoque.setCellValueFactory(new PropertyValueFactory<>("estoque"));
        colAcoes.setCellValueFactory(cellData -> javafx.beans.binding.Bindings.createStringBinding(() -> "Adicionar"));
        
        tblProdutos.setEditable(false);
        tblProdutos.setFocusTraversable(false);
        tblProdutos.setItems(produtosDisponiveis);
        
        // Permitir cliques na tabela para que os botões funcionem
        tblProdutos.getSelectionModel().clearSelection();
        
        // Adicionar botões de ação em vez de seleção por clique
        colAcoes.setCellFactory(col -> new TableCell<Produto, String>() {
            private final Button btnAdicionar = new Button("Adicionar");
            
            {
                btnAdicionar.setStyle("-fx-background-color: #28a745; -fx-text-fill: white;");
                btnAdicionar.setOnAction(e -> {
                    try {
                        // Obter o produto diretamente da célula atual
                        Produto produto = getTableView().getItems().get(getIndex());
                        
                        if (produto != null) {
                            logger.debug("Adicionando produto ao carrinho: {}", produto.getNome());
                            adicionarProdutoAoCarrinho(produto);
                        } else {
                            logger.warn("Tentativa de adicionar produto nulo ao carrinho");
                        }
                    } catch (Exception ex) {
                        logger.error("Erro ao adicionar produto ao carrinho: {}", ex.getMessage(), ex);
                        mostrarAlerta("Não foi possível adicionar o produto ao carrinho.", Alert.AlertType.ERROR);
                    }
                });
            }
            
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    setGraphic(btnAdicionar);
                }
            }
        });
        
        // Configurar tabela de carrinho com PropertyValueFactory otimizado
        colItemCodigo.setCellValueFactory(new PropertyValueFactory<>("produtoId"));
        colItemDescricao.setCellValueFactory(new PropertyValueFactory<>("produtoNome"));
        colItemQtd.setCellValueFactory(new PropertyValueFactory<>("quantidade"));
        colItemValor.setCellValueFactory(new PropertyValueFactory<>("valorUnitario"));
        colItemTotal.setCellValueFactory(new PropertyValueFactory<>("valorTotal"));
        colItemAcoes.setCellValueFactory(cellData -> javafx.beans.binding.Bindings.createStringBinding(() -> "Remover"));
        
        tblCarrinho.setEditable(false);
        tblCarrinho.setFocusTraversable(false);
        tblCarrinho.setItems(itensCarrinho);
        
        // Adicionar botões de remover na tabela do carrinho
        colItemAcoes.setCellFactory(col -> new TableCell<ItemVenda, String>() {
            private final Button btnRemover = new Button("Remover");
            
            {
                btnRemover.setStyle("-fx-background-color: #dc3545; -fx-text-fill: white;");
                btnRemover.setOnAction(e -> {
                    try {
                        // Obter o item diretamente da célula atual
                        ItemVenda item = getTableView().getItems().get(getIndex());
                        
                        if (item != null) {
                            logger.debug("Removendo item do carrinho: {}", item.getProdutoNome());
                            removerItemDoCarrinho(item);
                        } else {
                            logger.warn("Tentativa de remover item nulo do carrinho");
                        }
                    } catch (Exception ex) {
                        logger.error("Erro ao remover item do carrinho: {}", ex.getMessage(), ex);
                        mostrarAlerta("Não foi possível remover o item do carrinho.", Alert.AlertType.ERROR);
                    }
                });
            }
            
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    setGraphic(btnRemover);
                }
            }
        });
    }

    private void inicializarListeners() {
        // Listener para busca de produtos com debounce para melhorar performance
        txtBuscarProduto.textProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue != null && !newValue.trim().isEmpty()) {
                // Debounce: esperar 300ms antes de buscar
                javafx.application.Platform.runLater(() -> {
                    try {
                        Thread.sleep(300);
                        if (txtBuscarProduto.getText().equals(newValue)) {
                            buscarProdutos(newValue);
                        }
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    }
                });
            } else {
                carregarProdutosPadrao();
            }
        });
        
        // Removido listener de seleção para evitar conflito com botões Adicionar
        // A adição de produtos agora é feita apenas pelos botões na coluna Ações
        
        // Listener para atualização do carrinho - otimizado com batch updates
        itensCarrinho.addListener((javafx.collections.ListChangeListener<ItemVenda>) change -> {
            if (change.next()) {
                // Fazer atualizações em batch para melhor performance
                javafx.application.Platform.runLater(() -> {
                    atualizarTotais();
                    atualizarBotoesCarrinho();
                });
            }
        });
        
        // Listener para valor recebido com validação otimizada
        txtValorRecebido.textProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue != null && !newValue.equals(oldValue)) {
                // Validar formato antes de calcular
                if (newValue.matches("\\d*(\\,\\d{0,2})?")) {
                    calcularTroco();
                }
            }
        });
        
        // Configurar TextField para aceitar apenas números
        txtValorRecebido.textProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue != null && !newValue.equals(oldValue)) {
                // Permitir apenas números e uma vírgula decimal
                if (!newValue.matches("\\d*(\\,\\d{0,2})?")) {
                    txtValorRecebido.setText(oldValue);
                }
            }
        });
    }
    
    @FXML
    private void validateValorRecebidoInput(javafx.scene.input.KeyEvent event) {
        String character = event.getCharacter();
        // Permitir apenas dígitos e uma vírgula
        if (!character.matches("\\d,\\b")) { // \\b = backspace
            event.consume(); // Bloqueia a entrada do caractere
        }
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
        carregarProdutosPadrao();
        atualizarInformacoesVenda();
        atualizarTotais();
        atualizarBotoesCarrinho();
    }

    private void carregarProdutosPadrao() {
        produtosDisponiveis.clear();
        // Adicionar alguns produtos de exemplo
        for (int i = 1; i <= 10; i++) {
            Produto produto = new Produto();
            produto.setId((long) i);
            produto.setCodigo(String.format("PROD%03d", i));
            produto.setNome("Produto " + i);
            produto.setPrecoVenda(new BigDecimal(i * 10.50));
            produto.setEstoque(i * 5);
            produtosDisponiveis.add(produto);
        }
    }

    private void buscarProdutos(String textoBusca) {
        produtosDisponiveis.clear();
        // Simulação de busca - em produção seria consulta ao banco
        for (int i = 1; i <= 10; i++) {
            Produto produto = new Produto();
            produto.setId((long) i);
            produto.setCodigo(String.format("PROD%03d", i));
            produto.setNome("Produto " + i);
            produto.setPrecoVenda(new BigDecimal(i * 10.50));
            produto.setEstoque(i * 5);
            
            // Filtrar por texto de busca
            if (produto.getNome().toLowerCase().contains(textoBusca.toLowerCase()) ||
                produto.getCodigo().toLowerCase().contains(textoBusca.toLowerCase())) {
                produtosDisponiveis.add(produto);
            }
        }
    }
    
    private void adicionarProdutoAoCarrinho(Produto produto) {
        System.out.println("DEBUG: Tentando adicionar produto ao carrinho: " + (produto != null ? produto.getNome() : "NULL"));
        
        if (!validarProdutoParaAdicao(produto)) {
            System.out.println("DEBUG: Produto inválido para adição");
            return;
        }
        
        ItemVenda itemExistente = buscarItemExistenteNoCarrinho(produto);
        
        if (itemExistente != null) {
            System.out.println("DEBUG: Item existente encontrado, incrementando quantidade");
            incrementarQuantidadeItem(itemExistente);
        } else {
            System.out.println("DEBUG: Novo item, adicionando ao carrinho");
            adicionarNovoItemAoCarrinho(produto);
        }
        
        atualizarVendaECarrinho();
        atualizarTotais();
        atualizarBotoesCarrinho();
        atualizarStatus("Produto adicionado: " + produto.getNome());
    }
    
    private boolean validarProdutoParaAdicao(Produto produto) {
        if (produto == null) {
            mostrarAlerta("Produto inválido", Alert.AlertType.WARNING);
            return false;
        }
        
        if (produto.getEstoque() <= 0) {
            mostrarAlerta("Produto sem estoque disponível", Alert.AlertType.WARNING);
            return false;
        }
        
        return true;
    }
    
    private ItemVenda buscarItemExistenteNoCarrinho(Produto produto) {
        return itensCarrinho.stream()
            .filter(item -> item.getProduto() != null && 
                          item.getProduto().getId().equals(produto.getId()))
            .findFirst()
            .orElse(null);
    }
    
    private void incrementarQuantidadeItem(ItemVenda item) {
        item.setQuantidade(item.getQuantidade() + 1);
        atualizarValorTotalItem(item);
    }
    
    private void adicionarNovoItemAoCarrinho(Produto produto) {
        ItemVenda novoItem = criarNovoItemVenda(produto);
        itensCarrinho.add(novoItem);
    }
    
    private ItemVenda criarNovoItemVenda(Produto produto) {
        ItemVenda novoItem = new ItemVenda();
        novoItem.setId(System.currentTimeMillis());
        novoItem.setProduto(produto);
        novoItem.setQuantidade(1);
        novoItem.setValorUnitario(produto.getPrecoVenda());
        atualizarValorTotalItem(novoItem);
        return novoItem;
    }
    
    private void atualizarValorTotalItem(ItemVenda item) {
        BigDecimal valorUnitario = item.getValorUnitario();
        int quantidade = item.getQuantidade();
        item.setValorTotal(valorUnitario.multiply(new BigDecimal(quantidade)));
    }
    
    private void removerItemDoCarrinho(ItemVenda item) {
        System.out.println("DEBUG: Tentando remover item do carrinho: " + (item != null ? item.getProdutoNome() : "NULL"));
        
        if (item != null) {
            itensCarrinho.remove(item);
            vendaAtual.getItens().remove(item);
            atualizarTotais();
            atualizarBotoesCarrinho();
            atualizarStatus("Item removido: " + item.getProdutoNome());
            System.out.println("DEBUG: Item removido com sucesso");
        } else {
            System.out.println("DEBUG: Item nulo, não foi possível remover");
        }
    }
    
    private void atualizarVendaECarrinho() {
        if (vendaAtual != null) {
            vendaAtual.getItens().clear();
            vendaAtual.getItens().addAll(itensCarrinho);
        }
    }
    
    private void atualizarInformacoesOperador() {
        if (operadorAtual != null) {
            lblOperador.setText(operadorAtual.getNome());
        }
    }
    
    private void atualizarInformacoesVenda() {
        if (vendaAtual != null) {
            lblNumeroVenda.setText(vendaAtual.getNumeroCupom());
            lblDataHora.setText(vendaAtual.getDataVenda().format(formatadorDataHora));
        }
    }
    
    private void atualizarStatus(String mensagem) {
        // Temporariamente usando System.out.println até implementar lblStatus no FXML
        System.out.println("STATUS: " + mensagem);
    }

    private void atualizarTotais() {
        BigDecimal subtotal = BigDecimal.ZERO;
        BigDecimal desconto = BigDecimal.ZERO;
        
        for (ItemVenda item : itensCarrinho) {
            subtotal = subtotal.add(item.getValorTotal());
            if (item.getDesconto() != null) {
                desconto = desconto.add(item.getDesconto());
            }
        }
        
        BigDecimal total = subtotal.subtract(desconto);
        
        lblSubtotal.setText(formatarMoeda(subtotal));
        lblDesconto.setText(formatarMoeda(desconto));
        lblTotal.setText(formatarMoeda(total));
        
        // Atualizar venda
        if (vendaAtual != null) {
            vendaAtual.setValorTotal(subtotal);
            vendaAtual.setValorDesconto(desconto);
            vendaAtual.setValorFinal(total);
        }
    }

    private void atualizarBotoesCarrinho() {
        boolean temItens = !itensCarrinho.isEmpty();
        
        btnDesconto.setDisable(!temItens);
        btnCancelarItem.setDisable(!temItens);
        btnLimparCarrinho.setDisable(!temItens);
        btnFinalizar.setDisable(!temItens);
    }

    private void atualizarCamposPagamento() {
        boolean isDinheiro = rbDinheiro.isSelected();
        paneDinheiro.setVisible(isDinheiro);
        paneDinheiro.setManaged(isDinheiro);
        
        if (!isDinheiro) {
            txtValorRecebido.clear();
            lblTroco.setText("Troco: R$ 0,00");
        }
    }

    private void calcularTroco() {
        if (rbDinheiro.isSelected() && txtValorRecebido.getText() != null && !txtValorRecebido.getText().isEmpty()) {
            try {
                BigDecimal valorRecebido = new BigDecimal(txtValorRecebido.getText().replace(",", "."));
                BigDecimal total = vendaAtual.getValorFinal();
                
                if (valorRecebido.compareTo(total) >= 0) {
                    BigDecimal troco = valorRecebido.subtract(total);
                    lblTroco.setText("Troco: " + formatarMoeda(troco));
                    lblTroco.setStyle("-fx-text-fill: #28a745; -fx-font-weight: bold;");
                } else {
                    BigDecimal falta = total.subtract(valorRecebido);
                    lblTroco.setText("Falta: " + formatarMoeda(falta));
                    lblTroco.setStyle("-fx-text-fill: #dc3545; -fx-font-weight: bold;");
                }
            } catch (NumberFormatException e) {
                lblTroco.setText("Troco: R$ 0,00");
            }
        }
    }

    private String formatarMoeda(BigDecimal valor) {
        if (valor == null) return "R$ 0,00";
        return String.format("R$ %,.2f", valor).replace('.', ',');
    }

    // Métodos de Ação dos Botões
    @FXML
    private void onVoltar() {
        fecharTela();
    }

    @FXML
    private void onBuscarProduto() {
        String textoBusca = txtBuscarProduto.getText();
        if (textoBusca != null && !textoBusca.trim().isEmpty()) {
            buscarProdutos(textoBusca);
        }
    }

    @FXML
    private void onCodigoBarras() {
        // Implementar leitura de código de barras
        mostrarAlerta("Funcionalidade de código de barras em desenvolvimento", Alert.AlertType.INFORMATION);
    }

    @FXML
    private void onDesconto() {
        ItemVenda itemSelecionado = tblCarrinho.getSelectionModel().getSelectedItem();
        if (itemSelecionado != null) {
            // Implementar diálogo de desconto
            mostrarAlerta("Funcionalidade de desconto em desenvolvimento", Alert.AlertType.INFORMATION);
        } else {
            mostrarAlerta("Selecione um item para aplicar desconto", Alert.AlertType.WARNING);
        }
    }

    @FXML
    private void onCancelarItem() {
        ItemVenda itemSelecionado = tblCarrinho.getSelectionModel().getSelectedItem();
        if (itemSelecionado != null) {
            itensCarrinho.remove(itemSelecionado);
            vendaAtual.getItens().remove(itemSelecionado);
            atualizarTotais();
        } else {
            mostrarAlerta("Selecione um item para cancelar", Alert.AlertType.WARNING);
        }
    }

    @FXML
    private void onLimparCarrinho() {
        if (confirmarLimparCarrinho()) {
            itensCarrinho.clear();
            vendaAtual.getItens().clear();
            atualizarTotais();
        }
    }

    @FXML
    private void onBuscarCliente() {
        String cpfCnpj = txtBuscarClienteCpfCnpj.getText();
        if (cpfCnpj != null && !cpfCnpj.trim().isEmpty()) {
            // Implementar busca de cliente
            mostrarAlerta("Funcionalidade de busca de cliente em desenvolvimento", Alert.AlertType.INFORMATION);
        }
    }
    
    // Configurar TextField CPF/CNPJ para aceitar apenas números
    private void configurarCampoCpfCnpj() {
        txtBuscarClienteCpfCnpj.textProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue != null && !newValue.equals(oldValue)) {
                // Permitir apenas números
                if (!newValue.matches("\\d*")) {
                    txtBuscarClienteCpfCnpj.setText(oldValue);
                }
            }
        });
    }
    
    @FXML
    private void validateCpfCnpjInput(javafx.scene.input.KeyEvent event) {
        String character = event.getCharacter();
        // Permitir apenas dígitos
        if (!character.matches("\\d")) {
            event.consume(); // Bloqueia a entrada do caractere
        }
    }

    @FXML
    private void onLimparCliente() {
        txtBuscarClienteCpfCnpj.clear();
        lblNomeCliente.setText("Nenhum cliente selecionado");
        lblCpfCnpjCliente.setText("");
        vendaAtual.setCliente(null);
    }

    @FXML
    private void onFinalizar() {
        if (itensCarrinho.isEmpty()) {
            mostrarAlerta("Carrinho vazio. Adicione itens antes de finalizar.", Alert.AlertType.WARNING);
            return;
        }
        
        if (rbDinheiro.isSelected() && (txtValorRecebido.getText() == null || txtValorRecebido.getText().isEmpty())) {
            mostrarAlerta("Informe o valor recebido para pagamento em dinheiro.", Alert.AlertType.WARNING);
            return;
        }
        
        // Validar pagamento
        if (rbDinheiro.isSelected()) {
            try {
                BigDecimal valorRecebido = new BigDecimal(txtValorRecebido.getText().replace(",", "."));
                if (valorRecebido.compareTo(vendaAtual.getValorFinal()) < 0) {
                    mostrarAlerta("Valor recebido insuficiente.", Alert.AlertType.WARNING);
                    return;
                }
            } catch (NumberFormatException e) {
                mostrarAlerta("Valor recebido inválido.", Alert.AlertType.WARNING);
                return;
            }
        }
        
        // Finalizar venda
        if (confirmarFinalizacao()) {
            finalizarVenda();
        }
    }

    @FXML
    private void onCancelar() {
        if (confirmarCancelamento()) {
            fecharTela();
        }
    }

    private boolean confirmarLimparCarrinho() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Limpar Carrinho");
        alert.setHeaderText("Deseja limpar o carrinho?");
        alert.setContentText("Todos os itens serão removidos do carrinho.");
        return alert.showAndWait().orElse(ButtonType.CANCEL) == ButtonType.OK;
    }

    private boolean confirmarFinalizacao() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Finalizar Venda");
        alert.setHeaderText("Deseja finalizar esta venda?");
        alert.setContentText("Valor total: " + lblTotal.getText());
        return alert.showAndWait().orElse(ButtonType.CANCEL) == ButtonType.OK;
    }

    private boolean confirmarCancelamento() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Cancelar Venda");
        alert.setHeaderText("Deseja cancelar esta venda?");
        alert.setContentText("Todos os dados serão perdidos.");
        return alert.showAndWait().orElse(ButtonType.CANCEL) == ButtonType.OK;
    }

    private void finalizarVenda() {
        // Implementar finalização da venda
        mostrarAlerta("Venda finalizada com sucesso!\n\n" +
                      "Nº: " + vendaAtual.getNumeroCupom() + "\n" +
                      "Total: " + lblTotal.getText(), Alert.AlertType.INFORMATION);
        
        // Limpar para nova venda
        itensCarrinho.clear();
        vendaAtual = new VendaPDV();
        vendaAtual.setNumeroCupom("V" + System.currentTimeMillis());
        vendaAtual.setDataVenda(LocalDateTime.now());
        atualizarInformacoesVenda();
        atualizarTotais();
    }

    private void mostrarAlerta(String mensagem, Alert.AlertType tipo) {
        Alert alert = new Alert(tipo);
        alert.setTitle("Sistema PDV");
        alert.setHeaderText(null);
        alert.setContentText(mensagem);
        alert.showAndWait();
    }

    private void carregarProdutosExemplo() {
        // Criar produtos de exemplo para a tabela
        Produto p1 = new Produto();
        p1.setId(1L);
        p1.setCodigo("001");
        p1.setNome("Notebook Dell Inspiron");
        p1.setPrecoVenda(new java.math.BigDecimal("3500.00"));
        p1.setEstoque(10);
        
        Produto p2 = new Produto();
        p2.setId(2L);
        p2.setCodigo("002");
        p2.setNome("Mouse Wireless Logitech");
        p2.setPrecoVenda(new java.math.BigDecimal("89.90"));
        p2.setEstoque(25);
        
        Produto p3 = new Produto();
        p3.setId(3L);
        p3.setCodigo("003");
        p3.setNome("Teclado Mecânico RGB");
        p3.setPrecoVenda(new java.math.BigDecimal("199.99"));
        p3.setEstoque(15);
        
        Produto p4 = new Produto();
        p4.setId(4L);
        p4.setCodigo("004");
        p4.setNome("Monitor 24\" Full HD");
        p4.setPrecoVenda(new java.math.BigDecimal("899.00"));
        p4.setEstoque(8);
        
        Produto p5 = new Produto();
        p5.setId(5L);
        p5.setCodigo("005");
        p5.setNome("Webcam HD 1080p");
        p5.setPrecoVenda(new java.math.BigDecimal("149.90"));
        p5.setEstoque(20);
        
        produtosDisponiveis.addAll(p1, p2, p3, p4, p5);
    }

    private void fecharTela() {
        // Fechar janela atual
        if (btnVoltar.getScene() != null && btnVoltar.getScene().getWindow() != null) {
            btnVoltar.getScene().getWindow().hide();
        }
    }
}
