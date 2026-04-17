package com.br.hermescomercial.controller.pdv;

import com.br.hermescomercial.model.Produto;
import com.br.hermescomercial.model.ItemVenda;
import com.br.hermescomercial.dao.ProdutoDao;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.net.URL;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class PDVBuscaProdutoController implements Initializable {

    // DAO
    private ProdutoDao produtoDao;
    
    // Dados
    private ObservableList<Produto> produtosEncontrados;
    private Produto produtoSelecionado;
    
    // Timer para atualização de data/hora
    private Timer timer;
    private DateTimeFormatter formatadorDataHora = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");

    // Componentes FXML - Header
    @FXML private Label lblDataHora;
    @FXML private Button btnVoltar;
    
    // Filtros de Busca
    @FXML private TextField txtCodigoBarras;
    @FXML private TextField txtCodigoInterno;
    @FXML private TextField txtDescricao;
    @FXML private ComboBox<String> comboCategoria;
    @FXML private TextField txtPrecoMinimo;
    @FXML private TextField txtPrecoMaximo;
    @FXML private TextField txtEstoqueMinimo;
    @FXML private CheckBox chkApenasComEstoque;
    @FXML private CheckBox chkApenasAtivos;
    @FXML private Button btnBuscar;
    @FXML private Button btnLimparFiltros;
    @FXML private Button btnLeitorCodigo;
    
    // Resultados
    @FXML private Label lblResultados;
    @FXML private Label lblTempoBusca;
    @FXML private TableView<Produto> tblProdutos;
    @FXML private TableColumn<Produto, String> colCodigo;
    @FXML private TableColumn<Produto, String> colCodigoBarras;
    @FXML private TableColumn<Produto, String> colDescricao;
    @FXML private TableColumn<Produto, String> colCategoria;
    @FXML private TableColumn<Produto, String> colUnidade;
    @FXML private TableColumn<Produto, BigDecimal> colPrecoVenda;
    @FXML private TableColumn<Produto, Integer> colEstoque;
    @FXML private TableColumn<Produto, String> colStatus;
    @FXML private TableColumn<Produto, String> colAcoes;
    
    // Botões de Ação
    @FXML private Button btnAdicionarAoCarrinho;
    @FXML private Button btnVerDetalhes;
    @FXML private Button btnEditarProduto;
    @FXML private Button btnNovoProduto;
    
    // Status
    @FXML private Label lblStatus;
    @FXML private Label lblOperador;
    @FXML private Label lblTerminal;
    
    // Diálogo de Quantidade
    @FXML private Dialog<?> dialogQuantidade;
    @FXML private Label lblProdutoDialog;
    @FXML private Label lblPrecoDialog;
    @FXML private Label lblEstoqueDialog;
    @FXML private TextField txtQuantidadeDialog;
    @FXML private Label lblUnidadeDialog;
    @FXML private TextField txtDescontoDialog;
    @FXML private Label lblValorDescontoDialog;
    @FXML private Label lblTotalDialog;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        inicializarComponentes();
        inicializarListeners();
        inicializarTabelas();
        inicializarTimer();
        carregarDadosIniciais();
    }

    private void inicializarComponentes() {
        // Inicializar DAO
        this.produtoDao = new ProdutoDao();
        
        // Inicializar lista observável
        this.produtosEncontrados = FXCollections.observableArrayList();
        
        // Configurar combo de categorias
        comboCategoria.setItems(FXCollections.observableArrayList(
            "Todas", "Alimentos", "Bebidas", "Limpeza", "Higiene", "Eletrônicos", "Vestuário", "Outros"
        ));
        comboCategoria.getSelectionModel().selectFirst();
        
        // Configurar informações do terminal
        lblTerminal.setText("Terminal: 001");
        lblOperador.setText("Operador: João");
        
        // Configurar status inicial
        atualizarStatus("Pronto para busca");
    }

    private void inicializarListeners() {
        // Listener para busca automática por código de barras
        txtCodigoBarras.textProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue != null && newValue.length() >= 8) {
                buscarPorCodigoBarras(newValue);
            }
        });
        
        // Listener para busca por descrição
        txtDescricao.textProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue != null && newValue.length() >= 3) {
                realizarBusca();
            }
        });
        
        // Listener para seleção na tabela
        tblProdutos.getSelectionModel().selectedItemProperty().addListener((obs, oldValue, newValue) -> {
            produtoSelecionado = newValue;
            atualizarBotoesAcao();
        });
        
    }

    private void inicializarTabelas() {
        // Configurar tabela de produtos
        colCodigo.setCellValueFactory(new PropertyValueFactory<>("codigo"));
        colCodigoBarras.setCellValueFactory(new PropertyValueFactory<>("codigoBarras"));
        colDescricao.setCellValueFactory(new PropertyValueFactory<>("nome"));
        colCategoria.setCellValueFactory(new PropertyValueFactory<>("categoria"));
        colUnidade.setCellValueFactory(new PropertyValueFactory<>("unidade"));
        colPrecoVenda.setCellValueFactory(new PropertyValueFactory<>("precoVenda"));
        colEstoque.setCellValueFactory(new PropertyValueFactory<>("quantidadeEstoque"));
        colStatus.setCellValueFactory(cellData -> {
            Produto produto = cellData.getValue();
            return javafx.beans.binding.Bindings.createStringBinding(() -> 
                produto.getQuantidadeEstoque() > 0 ? "ATIVO" : "SEM ESTOQUE");
        });
        colAcoes.setCellValueFactory(new PropertyValueFactory<>("acoes"));
        
        tblProdutos.setItems(produtosEncontrados);
        
        // Configurar renderização customizada para status
        colStatus.setCellFactory(column -> new TableCell<Produto, String>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                
                if (empty || item == null) {
                    setText(null);
                    setStyle("");
                } else {
                    setText(item);
                    if ("SEM ESTOQUE".equals(item)) {
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
        carregarTodosProdutos();
        atualizarBotoesAcao();
    }

    // Métodos de Ação dos Botões
    @FXML
    private void onVoltar() {
        fecharTela();
    }

    @FXML
    private void onBuscar() {
        realizarBusca();
    }

    @FXML
    private void onLimparFiltros() {
        limparFiltros();
        carregarTodosProdutos();
    }

    @FXML
    private void onLeitorCodigo() {
        atualizarStatus("Aguardando leitura do código de barras...");
        txtCodigoBarras.requestFocus();
    }

    @FXML
    private void onAdicionarAoCarrinho() {
        if (produtoSelecionado == null) {
            mostrarAlerta("Selecione um produto", Alert.AlertType.WARNING);
            return;
        }
        
        if (produtoSelecionado.getQuantidadeEstoque() <= 0) {
            mostrarAlerta("Produto sem estoque disponível", Alert.AlertType.WARNING);
            return;
        }
        
        abrirDialogQuantidade();
    }

    @FXML
    private void onVerDetalhes() {
        if (produtoSelecionado != null) {
            mostrarDetalhesProduto(produtoSelecionado);
        }
    }

    @FXML
    private void onEditarProduto() {
        if (produtoSelecionado != null) {
            atualizarStatus("Abrindo edição do produto: " + produtoSelecionado.getNome());
            // Implementar edição de produto
        }
    }

    @FXML
    private void onNovoProduto() {
        atualizarStatus("Abrindo cadastro de novo produto");
        // Implementar cadastro de novo produto
    }

    // Métodos de Busca
    private void realizarBusca() {
        long inicio = System.currentTimeMillis();
        
        try {
            String codigoBarras = txtCodigoBarras.getText();
            String codigoInterno = txtCodigoInterno.getText();
            String descricao = txtDescricao.getText();
            String categoria = comboCategoria.getSelectionModel().getSelectedItem();
            String precoMinimoTexto = txtPrecoMinimo.getText();
            String precoMaximoTexto = txtPrecoMaximo.getText();
            String estoqueMinimoTexto = txtEstoqueMinimo.getText();
            
            BigDecimal precoMinimo = null;
            BigDecimal precoMaximo = null;
            Integer estoqueMinimo = null;
            
            try {
                if (precoMinimoTexto != null && !precoMinimoTexto.trim().isEmpty()) {
                    precoMinimo = new BigDecimal(precoMinimoTexto.replace(",", "."));
                }
                if (precoMaximoTexto != null && !precoMaximoTexto.trim().isEmpty()) {
                    precoMaximo = new BigDecimal(precoMaximoTexto.replace(",", "."));
                }
                if (estoqueMinimoTexto != null && !estoqueMinimoTexto.trim().isEmpty()) {
                    estoqueMinimo = Integer.parseInt(estoqueMinimoTexto);
                }
            } catch (NumberFormatException e) {
                mostrarAlerta("Valores numéricos inválidos", Alert.AlertType.WARNING);
                return;
            }
            
            List<Produto> produtos = produtoDao.buscarComFiltros(
                codigoBarras, codigoInterno, descricao, categoria,
                precoMinimo, precoMaximo, estoqueMinimo,
                chkApenasComEstoque.isSelected(), chkApenasAtivos.isSelected()
            );
            
            produtosEncontrados.clear();
            produtosEncontrados.addAll(produtos);
            
            long fim = System.currentTimeMillis();
            long tempo = fim - inicio;
            
            atualizarResultados(produtos.size(), tempo);
            atualizarStatus(produtos.size() + " produtos encontrados");
            
        } catch (Exception e) {
            atualizarStatus("Erro na busca: " + e.getMessage());
        }
    }

    private void buscarPorCodigoBarras(String codigoBarras) {
        try {
            Produto produto = produtoDao.buscarPorCodigoBarras(codigoBarras);
            
            if (produto != null) {
                produtosEncontrados.clear();
                produtosEncontrados.add(produto);
                
                // Selecionar automaticamente
                tblProdutos.getSelectionModel().select(produto);
                
                atualizarResultados(1, 0);
                atualizarStatus("Produto encontrado pelo código de barras");
                
                // Adicionar automaticamente ao carrinho se tiver estoque
                if (produto.getQuantidadeEstoque() > 0) {
                    abrirDialogQuantidade();
                } else {
                    mostrarAlerta("Produto encontrado mas sem estoque", Alert.AlertType.WARNING);
                }
            } else {
                atualizarStatus("Nenhum produto encontrado para este código de barras");
            }
            
        } catch (Exception e) {
            atualizarStatus("Erro ao buscar por código de barras: " + e.getMessage());
        }
    }

    private void carregarTodosProdutos() {
        try {
            List<Produto> produtos = produtoDao.listar();
            
            produtosEncontrados.clear();
            
            // Filtrar apenas produtos ativos se checkbox estiver marcado
            for (Produto produto : produtos) {
                if (!chkApenasAtivos.isSelected() || produto.getQuantidadeEstoque() > 0) {
                    produtosEncontrados.add(produto);
                }
            }
            
            atualizarResultados(produtosEncontrados.size(), 0);
            atualizarStatus("Todos os produtos carregados");
            
        } catch (Exception e) {
            atualizarStatus("Erro ao carregar produtos: " + e.getMessage());
        }
    }

    private void limparFiltros() {
        txtCodigoBarras.clear();
        txtCodigoInterno.clear();
        txtDescricao.clear();
        comboCategoria.getSelectionModel().selectFirst();
        txtPrecoMinimo.clear();
        txtPrecoMaximo.clear();
        txtEstoqueMinimo.clear();
        chkApenasComEstoque.setSelected(false);
        chkApenasAtivos.setSelected(true);
    }

    // Métodos do Diálogo de Quantidade
    private void abrirDialogQuantidade() {
        if (produtoSelecionado == null) return;
        
        // Configurar informações no diálogo
        lblProdutoDialog.setText(produtoSelecionado.getNome());
        lblPrecoDialog.setText(formatarMoeda(produtoSelecionado.getPreco()));
        lblEstoqueDialog.setText(String.valueOf(produtoSelecionado.getQuantidadeEstoque()));
        lblUnidadeDialog.setText(produtoSelecionado.getUnidade());
        
        // Configurar valores padrão
        txtQuantidadeDialog.setText("1");
        txtDescontoDialog.setText("0,00");
        
        // Calcular total inicial
        calcularTotalDialog();
        
        // Mostrar diálogo
        dialogQuantidade.showAndWait().ifPresent(result -> {
            if (result == ButtonType.OK) {
                adicionarProdutoAoCarrinho();
            }
        });
    }

    private void calcularTotalDialog() {
        try {
            int quantidade = txtQuantidadeDialog.getText() != null && !txtQuantidadeDialog.getText().isEmpty()
                ? Integer.parseInt(txtQuantidadeDialog.getText())
                : 1;
            
            BigDecimal precoUnitario = produtoSelecionado.getPreco();
            BigDecimal descontoPercentual = txtDescontoDialog.getText() != null && !txtDescontoDialog.getText().isEmpty()
                ? new BigDecimal(txtDescontoDialog.getText().replace(",", "."))
                : BigDecimal.ZERO;
            
            BigDecimal subtotal = precoUnitario.multiply(new BigDecimal(quantidade));
            BigDecimal valorDesconto = subtotal.multiply(descontoPercentual).divide(new BigDecimal(100));
            BigDecimal total = subtotal.subtract(valorDesconto);
            
            lblValorDescontoDialog.setText(formatarMoeda(valorDesconto));
            lblTotalDialog.setText(formatarMoeda(total));
            
        } catch (Exception e) {
            lblValorDescontoDialog.setText("R$ 0,00");
            lblTotalDialog.setText("R$ 0,00");
        }
    }

    private void adicionarProdutoAoCarrinho() {
        try {
            int quantidade = Integer.parseInt(txtQuantidadeDialog.getText());
            BigDecimal desconto = txtDescontoDialog.getText() != null && !txtDescontoDialog.getText().isEmpty()
                ? new BigDecimal(txtDescontoDialog.getText().replace(",", "."))
                : BigDecimal.ZERO;
            
            if (quantidade <= 0) {
                mostrarAlerta("Quantidade deve ser maior que zero", Alert.AlertType.WARNING);
                return;
            }
            
            if (quantidade > produtoSelecionado.getQuantidadeEstoque()) {
                mostrarAlerta("Quantidade maior que o estoque disponível", Alert.AlertType.WARNING);
                return;
            }
            
            // Criar item de venda
            ItemVenda itemVenda = new ItemVenda();
            itemVenda.setProduto(produtoSelecionado);
            itemVenda.setQuantidade(quantidade);
            itemVenda.setValorUnitario(produtoSelecionado.getPreco());
            itemVenda.setValorFinal(itemVenda.getValorUnitario().multiply(new BigDecimal(quantidade)));
            
            if (desconto.compareTo(BigDecimal.ZERO) > 0) {
                itemVenda.aplicarDesconto(desconto);
            }
            
            // Adicionar ao carrinho (implementar integração com o PDV principal)
            // Por enquanto, apenas mostrar sucesso
            mostrarAlerta("Produto adicionado ao carrinho com sucesso!", Alert.AlertType.INFORMATION);
            
            // Limpar seleção e fechar diálogo
            tblProdutos.getSelectionModel().clearSelection();
            atualizarStatus("Produto adicionado: " + produtoSelecionado.getNome());
            
        } catch (NumberFormatException e) {
            mostrarAlerta("Quantidade inválida", Alert.AlertType.ERROR);
        } catch (Exception e) {
            mostrarAlerta("Erro ao adicionar produto: " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    // Métodos Utilitários
    private void atualizarResultados(int quantidade, long tempoMs) {
        lblResultados.setText(quantidade + " produtos encontrados");
        lblTempoBusca.setText("Tempo: " + tempoMs + "ms");
    }

    private void atualizarBotoesAcao() {
        boolean temSelecao = produtoSelecionado != null;
        boolean temEstoque = temSelecao && produtoSelecionado.getQuantidadeEstoque() > 0;
        
        btnAdicionarAoCarrinho.setDisable(!temEstoque);
        btnVerDetalhes.setDisable(!temSelecao);
    }

    private void atualizarStatus(String mensagem) {
        lblStatus.setText(mensagem);
        
        // Atualizar cor do status
        if (mensagem.toLowerCase().contains("erro")) {
            lblStatus.setStyle("-fx-text-fill: #e74c3c; -fx-font-weight: bold;");
        } else if (mensagem.toLowerCase().contains("encontrado") || mensagem.toLowerCase().contains("adicionado")) {
            lblStatus.setStyle("-fx-text-fill: #28a745; -fx-font-weight: bold;");
        } else {
            lblStatus.setStyle("-fx-text-fill: #f39c12; -fx-font-weight: bold;");
        }
    }

    private void mostrarDetalhesProduto(Produto produto) {
        StringBuilder detalhes = new StringBuilder();
        detalhes.append("Código: ").append(produto.getCodigo()).append("\n");
        detalhes.append("Nome: ").append(produto.getNome()).append("\n");
        detalhes.append("Categoria: ").append(produto.getCategoria()).append("\n");
        detalhes.append("Preço: ").append(formatarMoeda(produto.getPreco())).append("\n");
        detalhes.append("Estoque: ").append(produto.getQuantidadeEstoque()).append(" ").append(produto.getUnidade()).append("\n");
        
        if (produto.getCodigoBarras() != null && !produto.getCodigoBarras().trim().isEmpty()) {
            detalhes.append("Cód. Barras: ").append(produto.getCodigoBarras()).append("\n");
        }
        
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Detalhes do Produto");
        alert.setHeaderText(produto.getNome());
        alert.setContentText(detalhes.toString());
        alert.showAndWait();
    }

    private String formatarMoeda(BigDecimal valor) {
        if (valor == null) return "R$ 0,00";
        return String.format("R$ %,.2f", valor).replace('.', ',');
    }

    private void mostrarAlerta(String mensagem, Alert.AlertType tipo) {
        Alert alert = new Alert(tipo);
        alert.setTitle("Busca de Produtos");
        alert.setHeaderText(null);
        alert.setContentText(mensagem);
        alert.showAndWait();
    }

    private void fecharTela() {
        // Fechar a janela atual
        javafx.stage.Stage stage = (javafx.stage.Stage) lblStatus.getScene().getWindow();
        stage.close();
    }

    // Getters e Setters
    public Produto getProdutoSelecionado() {
        return produtoSelecionado;
    }

    public void setProdutoSelecionado(Produto produtoSelecionado) {
        this.produtoSelecionado = produtoSelecionado;
    }
}
