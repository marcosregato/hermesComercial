package com.br.hermescomercial.controller.pdv;

import com.br.hermescomercial.business.pdv.PDVManager;
import com.br.hermescomercial.business.pdv.PagamentoManager;
import com.br.hermescomercial.business.pdv.CupomFiscalManager;
import com.br.hermescomercial.business.pdv.ImpressoraManager;
import com.br.hermescomercial.model.CarrinhoCompras;
import com.br.hermescomercial.model.ItemVenda;
import com.br.hermescomercial.model.Produto;
import com.br.hermescomercial.model.Cliente;
import com.br.hermescomercial.model.Usuario;
import com.br.hermescomercial.model.VendaPDV;
import com.br.hermescomercial.model.Pagamento;
import com.br.hermescomercial.dao.UsuarioDao;
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

public class PDVPrincipalController implements Initializable {

    // Managers
    private PDVManager pdvManager;
    private PagamentoManager pagamentoManager;
    private CupomFiscalManager cupomManager;
    private ImpressoraManager impressoraManager;
    
    // Dados
    private Usuario operadorAtual;
    private ObservableList<ItemVenda> itensCarrinho;
    private ObservableList<Produto> produtosDisponiveis;
    
    // Timer para atualização de data/hora
    private Timer timer;
    private DateTimeFormatter formatadorDataHora = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");

    // Componentes FXML - Header
    @FXML private Label lblDataHora;
    @FXML private Label lblOperador;
    @FXML private Label lblTerminal;
    
    // Botões Principais
    @FXML private Button btnNovaVenda;
    @FXML private Button btnBuscarProduto;
    @FXML private Button btnClientes;
    @FXML private Button btnRelatorios;
    @FXML private Button btnAbrirCaixa;
    @FXML private Button btnFecharCaixa;
    @FXML private Button btnSair;
    
    // Área de Produtos
    @FXML private TextField txtBuscarProduto;
    @FXML private Button btnBuscar;
    @FXML private Button btnCodigoBarras;
    @FXML private TableView<Produto> tblProdutos;
    @FXML private TableColumn<Produto, String> colCodigo;
    @FXML private TableColumn<Produto, String> colDescricao;
    @FXML private TableColumn<Produto, BigDecimal> colPreco;
    @FXML private TableColumn<Produto, Integer> colEstoque;
    @FXML private TableColumn<Produto, String> colAcoes;
    
    // Teclado Numérico
    @FXML private Button btn1, btn2, btn3, btn4, btn5, btn6, btn7, btn8, btn9, btn0, btnDecimal, btnQuantidade;
    
    // Cliente
    @FXML private TextField txtBuscarClienteNome;
    @FXML private Button btnBuscarClienteNome;
    @FXML private TextField txtBuscarClienteCpfCnpj;
    @FXML private Button btnBuscarClienteCpfCnpj;
    @FXML private Button btnNovoCliente;
    @FXML private Button btnLimparCliente;
    @FXML private Label lblClienteSelecionado;
    
    // Carrinho de Compras
    @FXML private TableView<ItemVenda> tblCarrinho;
    @FXML private TableColumn<ItemVenda, String> colItemCodigo;
    @FXML private TableColumn<ItemVenda, String> colItemDescricao;
    @FXML private TableColumn<ItemVenda, Integer> colItemQtd;
    @FXML private TableColumn<ItemVenda, BigDecimal> colItemValor;
    @FXML private TableColumn<ItemVenda, BigDecimal> colItemTotal;
    @FXML private TableColumn<ItemVenda, String> colItemAcoes;
    
    // Labels de Totais
    @FXML private Label lblSubtotal;
    @FXML private Label lblDesconto;
    @FXML private Label lblTotal;
    
    // Botões do Carrinho
    @FXML private Button btnDesconto;
    @FXML private Button btnCancelarItem;
    @FXML private Button btnLimparCarrinho;
    @FXML private Button btnFinalizar;
    
    // Status
    @FXML private Label lblStatusCaixa;
    @FXML private Label lblValorCaixa;
    @FXML private Label lblStatus;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        inicializarManagers();
        inicializarComponentes();
        inicializarListeners();
        inicializarTabelas();
        inicializarTimer();
        carregarDadosIniciais();
    }

    private void inicializarManagers() {
        this.pdvManager = new PDVManager();
        this.pagamentoManager = new PagamentoManager();
        this.cupomManager = new CupomFiscalManager();
        this.impressoraManager = new ImpressoraManager();
        
        // Configurar modo debug para impressora
        impressoraManager.setModoDebug(true);
    }

    private void inicializarComponentes() {
        // Inicializar listas observáveis
        this.itensCarrinho = FXCollections.observableArrayList();
        this.produtosDisponiveis = FXCollections.observableArrayList();
        
        // Configurar informações do terminal
        lblTerminal.setText("Terminal: " + pdvManager.getNumeroTerminal());
        
        // Configurar operador (simulado - viria do login)
        this.operadorAtual = new Usuario();
        operadorAtual.setNome("João Silva");
        operadorAtual.setId(1L);
        lblOperador.setText("Operador: " + operadorAtual.getNome());
        
        // Iniciar sessão PDV
        pdvManager.iniciarSessaoPDV(operadorAtual);
        
        // Configurar status inicial
        atualizarStatusCaixa();
        atualizarStatus("Pronto para operar");
    }

    private void inicializarListeners() {
        // Listener para busca de produtos
        txtBuscarProduto.textProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue != null && !newValue.trim().isEmpty()) {
                buscarProdutos(newValue);
            } else {
                carregarProdutosPadrao();
            }
        });
        
        // Listener para busca de cliente por nome
        txtBuscarClienteNome.textProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue != null && newValue.length() >= 2) {
                buscarClientePorNome(newValue);
            }
        });
        
        // Listener para busca de cliente por CPF/CNPJ
        txtBuscarClienteCpfCnpj.textProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue != null && !newValue.matches("\\d*")) {
                // Remove caracteres não numéricos
                String textoFiltrado = newValue.replaceAll("[^0-9]", "");
                txtBuscarClienteCpfCnpj.setText(textoFiltrado);
                
                // Exibe alerta sobre caracteres inválidos
                mostrarAlerta("O campo CPF/CNPJ aceita apenas números.\n" +
                             "Caracteres inválidos foram removidos automaticamente.", 
                             Alert.AlertType.WARNING);
                return;
            }
            
            // Limita o tamanho máximo (14 para CPF, 18 para CNPJ)
            if (newValue.length() > 18) {
                txtBuscarClienteCpfCnpj.setText(newValue.substring(0, 18));
                return;
            }
            
            if (newValue.length() >= 3) {
                buscarClientePorCpfCnpj(newValue);
            }
        });
        
        // Listener para seleção na tabela de produtos
        tblProdutos.getSelectionModel().selectedItemProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue != null) {
                adicionarProdutoAoCarrinho(newValue);
            }
        });
        
        // Listener para atualização do carrinho
        itensCarrinho.addListener((javafx.collections.ListChangeListener<ItemVenda>) change -> {
            atualizarTotais();
            atualizarBotoesCarrinho();
        });
    }

    private void inicializarTabelas() {
        // Configurar tabela de produtos
        colCodigo.setCellValueFactory(new PropertyValueFactory<>("codigo"));
        colDescricao.setCellValueFactory(new PropertyValueFactory<>("nome"));
        colPreco.setCellValueFactory(new PropertyValueFactory<>("precoVenda"));
        colEstoque.setCellValueFactory(new PropertyValueFactory<>("estoque"));
        colAcoes.setCellValueFactory(new PropertyValueFactory<>("acoes"));
        
        // Configurar tabela de produtos como não editável
        tblProdutos.setEditable(false);
        tblProdutos.setFocusTraversable(false);
        
        // Configurar colunas da tabela de produtos como não editáveis
        colCodigo.setEditable(false);
        colDescricao.setEditable(false);
        colPreco.setEditable(false);
        colEstoque.setEditable(false);
        colAcoes.setEditable(false);
        
        tblProdutos.setItems(produtosDisponiveis);
        
        // Configurar tabela de carrinho
        colItemCodigo.setCellValueFactory(new PropertyValueFactory<>("codigoProduto"));
        colItemDescricao.setCellValueFactory(cellData -> {
            Produto produto = cellData.getValue().getProduto();
            return javafx.beans.binding.Bindings.createStringBinding(() -> 
                produto != null ? produto.getNome() : "");
        });
        colItemQtd.setCellValueFactory(new PropertyValueFactory<>("quantidade"));
        colItemValor.setCellValueFactory(new PropertyValueFactory<>("valorUnitario"));
        colItemTotal.setCellValueFactory(new PropertyValueFactory<>("valorFinal"));
        colItemAcoes.setCellValueFactory(new PropertyValueFactory<>("acoes"));
        
        // Configurar tabela de carrinho como não editável
        tblCarrinho.setEditable(false);
        tblCarrinho.setFocusTraversable(false);
        
        // Configurar colunas da tabela de carrinho como não editáveis
        colItemCodigo.setEditable(false);
        colItemDescricao.setEditable(false);
        colItemQtd.setEditable(false);
        colItemValor.setEditable(false);
        colItemTotal.setEditable(false);
        colItemAcoes.setEditable(false);
        
        tblCarrinho.setItems(itensCarrinho);
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
        atualizarTotais();
        atualizarBotoesCarrinho();
    }

    // Métodos de Ação dos Botões
    @FXML
    private void onNovaVenda() {
        limparCarrinho();
        atualizarStatus("Nova venda iniciada");
    }

    @FXML
    private void onBuscarProduto() {
        System.out.println("DEBUG: Botão Buscar Produto pressionado");
        atualizarStatus("Abrindo tela de busca de produtos...");
        abrirTelaBuscaProdutos();
    }

    @FXML
    private void onCodigoBarras() {
        // Implementar leitura de código de barras
        atualizarStatus("Aguardando leitura do código de barras...");
    }

    @FXML
    private void onClientes() {
        System.out.println("DEBUG: Botão Clientes pressionado");
        atualizarStatus("Abrindo tela de clientes...");
        abrirTelaClientes();
    }

    @FXML
    private void onRelatorios() {
        System.out.println("DEBUG: Botão Relatórios pressionado");
        atualizarStatus("Abrindo tela de relatórios...");
        abrirTelaRelatorios();
    }

    @FXML
    private void onNotaFiscal() {
        System.out.println("DEBUG: Botão Nota Fiscal pressionado");
        atualizarStatus("Abrindo tela de nota fiscal...");
        abrirTelaNotaFiscal();
    }

    @FXML
    private void onConfigImpressora() {
        System.out.println("DEBUG: Botão Configuração de Impressora pressionado");
        atualizarStatus("Abrindo tela de configuração de impressora...");
        abrirTelaConfiguracaoImpressora();
    }

    @FXML
    private void onAbrirCaixa() {
        try {
            if (pdvManager.isCaixaAberto()) {
                // Se o caixa já está aberto, abre a tela de caixa aberto
                abrirTelaCaixaAberto();
            } else {
                if (pdvManager.abrirCaixa()) {
                    atualizarStatusCaixa();
                    mostrarAlerta("Caixa aberto com sucesso", Alert.AlertType.INFORMATION);
                    atualizarStatus("Caixa aberto - Pronto para operar");
                    
                    // Abrir tela de caixa aberto após sucesso
                    abrirTelaCaixaAberto();
                } else {
                    mostrarAlerta("Erro ao abrir caixa", Alert.AlertType.ERROR);
                    atualizarStatus("Falha ao abrir caixa");
                }
            }
        } catch (Exception e) {
            atualizarStatus("Erro ao abrir caixa: " + e.getMessage());
            mostrarAlerta("Erro ao abrir caixa: " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    @FXML
    private void onFecharCaixa() {
        try {
            if (!pdvManager.isCaixaAberto()) {
                mostrarAlerta("Caixa já está fechado", Alert.AlertType.WARNING);
            } else {
                if (pdvManager.fecharCaixa()) {
                    atualizarStatusCaixa();
                    mostrarAlerta("Caixa fechado com sucesso", Alert.AlertType.INFORMATION);
                    atualizarStatus("Caixa fechado");
                } else {
                    mostrarAlerta("Erro ao fechar caixa", Alert.AlertType.ERROR);
                    atualizarStatus("Falha ao fechar caixa");
                }
            }
        } catch (Exception e) {
            atualizarStatus("Erro ao fechar caixa: " + e.getMessage());
            mostrarAlerta("Erro ao fechar caixa: " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    @FXML
    private void onSair() {
        if (confirmarSaida()) {
            timer.cancel();
            javafx.application.Platform.exit();
        }
    }

    // Métodos do Teclado Numérico
    @FXML
    private void onNumeroPressionado(javafx.event.ActionEvent event) {
        Button botao = (Button) event.getSource();
        String texto = botao.getText();
        
        if (txtBuscarProduto.isFocused()) {
            txtBuscarProduto.appendText(texto);
        }
    }

    @FXML
    private void onDecimalPressionado() {
        if (txtBuscarProduto.isFocused()) {
            String texto = txtBuscarProduto.getText();
            if (!texto.contains(".")) {
                txtBuscarProduto.appendText(".");
            }
        }
    }

    @FXML
    private void onQuantidadePressionado() {
        // Implementar diálogo de quantidade
        atualizarStatus("Modo de quantidade ativado");
    }

    // Métodos do Cliente
    @FXML
    private void onBuscarClienteNome() {
        String busca = txtBuscarClienteNome.getText();
        if (busca == null || busca.trim().isEmpty()) {
            mostrarAlerta("O campo de busca por nome não pode estar vazio. Digite um nome para buscar.", Alert.AlertType.WARNING);
            txtBuscarClienteNome.requestFocus();
            return;
        }
        abrirTelaResultadosBusca(busca, "nome");
    }

    @FXML
    private void onBuscarClienteCpfCnpj() {
        String busca = txtBuscarClienteCpfCnpj.getText();
        if (busca == null || busca.trim().isEmpty()) {
            mostrarAlerta("O campo de busca por CPF/CNPJ não pode estar vazio. Digite um CPF ou CNPJ para buscar.", Alert.AlertType.WARNING);
            txtBuscarClienteCpfCnpj.requestFocus();
            return;
        }
        abrirTelaResultadosBusca(busca, "cpf/cnpj");
    }
    
    private void abrirTelaResultadosBusca(String termo, String tipoBusca) {
        try {
            System.out.println("DEBUG: Abrindo tela de resultados para busca: " + termo + " (tipo: " + tipoBusca + ")");
            
            javafx.fxml.FXMLLoader loader = new javafx.fxml.FXMLLoader(
                getClass().getResource("/view/pdv_resultado_busca_clientes.fxml"));
            System.out.println("DEBUG: FXML de resultados carregado, fazendo load...");
            javafx.scene.Parent root = loader.load();
            System.out.println("DEBUG: FXML de resultados carregado com sucesso");
            
            // Obter controller e configurar callback
            PDVResultadoBuscaClientesController controller = loader.getController();
            controller.setCallback(cliente -> {
                // Adicionar cliente ao carrinho
                if (pdvManager.getCarrinhoAtual() != null) {
                    pdvManager.getCarrinhoAtual().setCliente(cliente);
                    atualizarStatus("Cliente adicionado: " + cliente.getNome());
                    atualizarLabelClienteSelecionado(cliente.getNome() + " - " + cliente.getCpf());
                }
            });
            
            // Carregar resultados
            controller.carregarResultados(termo, tipoBusca);
            
            javafx.stage.Stage stage = new javafx.stage.Stage();
            stage.setTitle("Resultado da Busca de Clientes");
            stage.setScene(new javafx.scene.Scene(root));
            stage.initModality(javafx.stage.Modality.APPLICATION_MODAL);
            stage.setResizable(true);
            stage.setWidth(800);
            stage.setHeight(600);
            System.out.println("DEBUG: Stage de resultados configurado, mostrando janela...");
            stage.show();
            System.out.println("DEBUG: Janela de resultados aberta com sucesso");
            
        } catch (Exception e) {
            System.out.println("DEBUG: Erro ao abrir tela de resultados: " + e.getMessage());
            e.printStackTrace();
            atualizarStatus("Erro ao abrir tela de resultados: " + e.getMessage());
        }
    }
    
    private void buscarClienteUnificado() {
        String buscaNome = txtBuscarClienteNome.getText();
        String buscaCpfCnpj = txtBuscarClienteCpfCnpj.getText();
        
        // Verificar se pelo menos um campo está preenchido
        boolean nomePreenchido = buscaNome != null && !buscaNome.trim().isEmpty();
        boolean cpfCnpjPreenchido = buscaCpfCnpj != null && !buscaCpfCnpj.trim().isEmpty();
        
        if (!nomePreenchido && !cpfCnpjPreenchido) {
            mostrarAlerta("Preencha pelo menos um dos campos de busca:\n\n" +
                         "- Campo Nome: Digite o nome do cliente\n" +
                         "- Campo CPF/CNPJ: Digite o CPF ou CNPJ do cliente\n\n" +
                         "Ao preencher um dos campos, clique em Buscar para localizar o cliente.", 
                         Alert.AlertType.WARNING);
            
            // Focar no primeiro campo vazio
            if (!nomePreenchido) {
                txtBuscarClienteNome.requestFocus();
            } else {
                txtBuscarClienteCpfCnpj.requestFocus();
            }
            return;
        }
        
        // Realizar busca pelo campo preenchido
        if (nomePreenchido) {
            buscarClientePorNome(buscaNome);
        } else if (cpfCnpjPreenchido) {
            buscarClientePorCpfCnpj(buscaCpfCnpj);
        }
    }

    @FXML
    private void onNovoCliente() {
        abrirTelaClientes();
    }

    @FXML
    private void onLimparCliente() {
        txtBuscarClienteNome.clear();
        txtBuscarClienteCpfCnpj.clear();
        if (pdvManager.getCarrinhoAtual() != null) {
            pdvManager.getCarrinhoAtual().setCliente(null);
        }
        atualizarStatus("Cliente removido da venda");
        atualizarLabelClienteSelecionado("Nenhum cliente selecionado");
    }

    // Métodos do Carrinho
    @FXML
    private void onDesconto() {
        ItemVenda itemSelecionado = tblCarrinho.getSelectionModel().getSelectedItem();
        if (itemSelecionado != null) {
            aplicarDescontoItem(itemSelecionado);
        } else {
            mostrarAlerta("Selecione um item para aplicar desconto", Alert.AlertType.WARNING);
        }
    }

    @FXML
    private void onCancelarItem() {
        ItemVenda itemSelecionado = tblCarrinho.getSelectionModel().getSelectedItem();
        if (itemSelecionado != null) {
            removerItemDoCarrinho(itemSelecionado);
        } else {
            mostrarAlerta("Selecione um item para cancelar", Alert.AlertType.WARNING);
        }
    }

    @FXML
    private void onLimparCarrinho() {
        if (confirmarLimparCarrinho()) {
            limparCarrinho();
            atualizarStatus("Carrinho limpo");
        }
    }

    @FXML
    private void onFinalizar() {
        if (pdvManager.getCarrinhoAtual() == null || pdvManager.getCarrinhoAtual().estaVazio()) {
            mostrarAlerta("Carrinho vazio. Adicione itens antes de finalizar.", Alert.AlertType.WARNING);
            return;
        }
        
        if (!pdvManager.isCaixaAberto()) {
            mostrarAlerta("Caixa fechado. Abra o caixa antes de finalizar a venda.", Alert.AlertType.WARNING);
            return;
        }
        
        abrirTelaPagamento();
    }

    // Métodos de Negócio
    private void buscarProdutos(String textoBusca) {
        try {
            // Simular busca de produtos
            produtosDisponiveis.clear();
            
            // Adicionar produtos simulados
            for (int i = 1; i <= 5; i++) {
                Produto produto = new Produto();
                produto.setId((long) i);
                produto.setNome("Produto " + i + " - " + textoBusca);
                produto.setPrecoVenda(new BigDecimal(10.50 * i));
                produto.setEstoque(50);
                produto.setUnidade("UN");
                
                if (produto.getNome().toLowerCase().contains(textoBusca.toLowerCase())) {
                    produtosDisponiveis.add(produto);
                }
            }
            
            atualizarStatus(produtosDisponiveis.size() + " produtos encontrados");
            
        } catch (Exception e) {
            atualizarStatus("Erro ao buscar produtos: " + e.getMessage());
        }
    }

    private void carregarProdutosPadrao() {
        produtosDisponiveis.clear();
        
        // Adicionar produtos simulados
        for (int i = 1; i <= 10; i++) {
            Produto produto = new Produto();
            produto.setId((long) i);
            produto.setNome("Produto Padrão " + i);
            produto.setPrecoVenda(new BigDecimal(10.50 * i));
            produto.setEstoque(50);
            produto.setUnidade("UN");
            produtosDisponiveis.add(produto);
        }
    }

    private void buscarClientePorNome(String textoBusca) {
        try {
            if (textoBusca.length() >= 3) {
                UsuarioDao usuarioDao = new UsuarioDao();
                List<Usuario> clientes = usuarioDao.buscarClientePorNomeCpfCnpj(textoBusca);
                
                if (!clientes.isEmpty()) {
                    // Pegar o primeiro cliente encontrado
                    Usuario clienteEncontrado = clientes.get(0);
                    
                    // Criar objeto Cliente para compatibilidade com o carrinho
                    Cliente cliente = new Cliente();
                    cliente.setId(clienteEncontrado.getId());
                    cliente.setNome(clienteEncontrado.getNome());
                    cliente.setCpf(clienteEncontrado.getNumeroDocumeto());
                    cliente.setEmail(clienteEncontrado.getEmail());
                    cliente.setTelefone(clienteEncontrado.getTelefone());
                    
                    if (pdvManager.getCarrinhoAtual() != null) {
                        pdvManager.getCarrinhoAtual().setCliente(cliente);
                        atualizarStatus("Cliente adicionado: " + cliente.getNome());
                        atualizarLabelClienteSelecionado(cliente.getNome() + " - " + cliente.getCpf());
                    }
                } else {
                    atualizarStatus("Nenhum cliente encontrado para: " + textoBusca);
                    atualizarLabelClienteSelecionado("Nenhum cliente encontrado");
                }
            }
        } catch (Exception e) {
            atualizarStatus("Erro ao buscar cliente por nome: " + e.getMessage());
        }
    }
    
    private void buscarClientePorCpfCnpj(String textoBusca) {
        try {
            if (textoBusca.length() >= 3) {
                UsuarioDao usuarioDao = new UsuarioDao();
                List<Usuario> clientes = usuarioDao.buscarClientePorNomeCpfCnpj(textoBusca);
                
                if (!clientes.isEmpty()) {
                    // Pegar o primeiro cliente encontrado
                    Usuario clienteEncontrado = clientes.get(0);
                    
                    // Criar objeto Cliente para compatibilidade com o carrinho
                    Cliente cliente = new Cliente();
                    cliente.setId(clienteEncontrado.getId());
                    cliente.setNome(clienteEncontrado.getNome());
                    cliente.setCpf(clienteEncontrado.getNumeroDocumeto());
                    cliente.setEmail(clienteEncontrado.getEmail());
                    cliente.setTelefone(clienteEncontrado.getTelefone());
                    
                    if (pdvManager.getCarrinhoAtual() != null) {
                        pdvManager.getCarrinhoAtual().setCliente(cliente);
                        atualizarStatus("Cliente adicionado: " + cliente.getNome());
                        atualizarLabelClienteSelecionado(cliente.getNome() + " - " + cliente.getCpf());
                    }
                } else {
                    atualizarStatus("Nenhum cliente encontrado para: " + textoBusca);
                    atualizarLabelClienteSelecionado("Nenhum cliente encontrado");
                }
            }
        } catch (Exception e) {
            atualizarStatus("Erro ao buscar cliente por CPF/CNPJ: " + e.getMessage());
        }
    }
    
    private void atualizarLabelClienteSelecionado(String texto) {
        if (lblClienteSelecionado != null) {
            lblClienteSelecionado.setText(texto);
        }
    }

    private void adicionarProdutoAoCarrinho(Produto produto) {
        try {
            if (produto == null) {
                mostrarAlerta("Selecione um produto válido", Alert.AlertType.WARNING);
                return;
            }
            
            int quantidade = 1; // Padrão
            BigDecimal valorUnitario = produto.getPrecoVenda();
            
            boolean sucesso = pdvManager.adicionarProduto(produto, quantidade);
            
            if (sucesso) {
                atualizarInterfaceCarrinho();
                atualizarStatus("Produto adicionado: " + produto.getNome());
                
                // Limpar busca
                txtBuscarProduto.clear();
                
                // Focar novamente no campo de busca
                txtBuscarProduto.requestFocus();
            } else {
                mostrarAlerta("Erro ao adicionar produto ao carrinho", Alert.AlertType.ERROR);
            }
            
        } catch (Exception e) {
            atualizarStatus("Erro ao adicionar produto: " + e.getMessage());
        }
    }

    private void removerItemDoCarrinho(ItemVenda item) {
        try {
            if (pdvManager.removerItem(item.getProduto().getId().toString())) {
                atualizarInterfaceCarrinho();
                atualizarStatus("Item removido do carrinho");
            }
        } catch (Exception e) {
            atualizarStatus("Erro ao remover item: " + e.getMessage());
        }
    }

    private void aplicarDescontoItem(ItemVenda item) {
        try {
            // Simular aplicação de desconto
            BigDecimal desconto = new BigDecimal("1.00"); // R$ 1,00 de desconto
            
            if (pdvManager.aplicarDescontoItem(item.getProduto().getId().toString(), desconto)) {
                atualizarInterfaceCarrinho();
                atualizarStatus("Desconto aplicado no item");
            }
        } catch (Exception e) {
            atualizarStatus("Erro ao aplicar desconto: " + e.getMessage());
        }
    }

    private void limparCarrinho() {
        itensCarrinho.clear();
        if (pdvManager.getCarrinhoAtual() != null) {
            pdvManager.getCarrinhoAtual().getItens().clear();
        }
        atualizarTotais();
    }

    private void atualizarInterfaceCarrinho() {
        if (pdvManager.getCarrinhoAtual() != null) {
            itensCarrinho.clear();
            itensCarrinho.addAll(pdvManager.getCarrinhoAtual().getItens());
        }
        atualizarTotais();
    }

    private void atualizarTotais() {
        if (pdvManager.getCarrinhoAtual() != null) {
            BigDecimal subtotal = pdvManager.getCarrinhoAtual().getValorTotal();
            BigDecimal desconto = pdvManager.getCarrinhoAtual().getValorDesconto();
            BigDecimal total = pdvManager.getCarrinhoAtual().getValorFinal();
            
            lblSubtotal.setText(formatarMoeda(subtotal));
            lblDesconto.setText(formatarMoeda(desconto));
            lblTotal.setText(formatarMoeda(total));
        } else {
            lblSubtotal.setText("R$ 0,00");
            lblDesconto.setText("R$ 0,00");
            lblTotal.setText("R$ 0,00");
        }
    }

    private void atualizarBotoesCarrinho() {
        boolean temItens = !itensCarrinho.isEmpty();
        
        btnDesconto.setDisable(!temItens);
        btnCancelarItem.setDisable(!temItens);
        btnLimparCarrinho.setDisable(!temItens);
        btnFinalizar.setDisable(!temItens);
    }

    private void atualizarStatusCaixa() {
        if (pdvManager.isCaixaAberto()) {
            lblStatusCaixa.setText("Caixa: ABERTO");
            lblStatusCaixa.setStyle("-fx-text-fill: white; -fx-font-weight: bold; -fx-background-color: #27ae60; -fx-background-radius: 10; -fx-padding: 4 8;");
            lblValorCaixa.setText("Valor em Caixa: R$ 1.000,00");
        } else {
            lblStatusCaixa.setText("Caixa: FECHADO");
            lblStatusCaixa.setStyle("-fx-text-fill: white; -fx-font-weight: bold; -fx-background-color: #e74c3c; -fx-background-radius: 10; -fx-padding: 4 8;");
            lblValorCaixa.setText("Valor em Caixa: R$ 0,00");
        }
    }

    private void atualizarStatus(String mensagem) {
        lblStatus.setText(mensagem);
        
        // Atualizar cor do status baseado na mensagem
        if (mensagem.toLowerCase().contains("erro")) {
            lblStatus.setStyle("-fx-text-fill: #e74c3c; -fx-font-weight: bold;");
        } else if (mensagem.toLowerCase().contains("pronto")) {
            lblStatus.setStyle("-fx-text-fill: #28a745; -fx-font-weight: bold;");
        } else {
            lblStatus.setStyle("-fx-text-fill: #f39c12; -fx-font-weight: bold;");
        }
    }

    // Métodos de Navegação
    private void abrirTelaBuscaProdutos() {
        try {
            System.out.println("DEBUG: Tentando abrir tela de busca de produtos");
            // Carregar FXML da tela de busca
            javafx.fxml.FXMLLoader loader = new javafx.fxml.FXMLLoader(
                getClass().getResource("/view/pdv_busca_produto.fxml"));
            System.out.println("DEBUG: FXML carregado, fazendo load...");
            javafx.scene.Parent root = loader.load();
            System.out.println("DEBUG: FXML carregado com sucesso");
            
            // Abrir em nova janela ou dialog
            javafx.stage.Stage stage = new javafx.stage.Stage();
            stage.setTitle("Busca de Produtos");
            stage.setScene(new javafx.scene.Scene(root));
            System.out.println("DEBUG: Stage configurado, mostrando janela...");
            stage.show();
            System.out.println("DEBUG: Janela de busca de produtos aberta com sucesso");
            
        } catch (Exception e) {
            System.out.println("DEBUG: Erro ao abrir tela de busca: " + e.getMessage());
            e.printStackTrace();
            atualizarStatus("Erro ao abrir tela de busca: " + e.getMessage());
        }
    }

    private void abrirTelaClientes() {
        try {
            System.out.println("DEBUG: Tentando abrir tela de clientes");
            javafx.fxml.FXMLLoader loader = new javafx.fxml.FXMLLoader(
                getClass().getResource("/view/pdv_clientes.fxml"));
            System.out.println("DEBUG: FXML de clientes carregado, fazendo load...");
            javafx.scene.Parent root = loader.load();
            System.out.println("DEBUG: FXML de clientes carregado com sucesso");
            
            javafx.stage.Stage stage = new javafx.stage.Stage();
            stage.setTitle("Clientes");
            stage.setScene(new javafx.scene.Scene(root));
            System.out.println("DEBUG: Stage de clientes configurado, mostrando janela...");
            stage.show();
            System.out.println("DEBUG: Janela de clientes aberta com sucesso");
            
        } catch (Exception e) {
            System.out.println("DEBUG: Erro ao abrir tela de clientes: " + e.getMessage());
            e.printStackTrace();
            atualizarStatus("Erro ao abrir tela de clientes: " + e.getMessage());
        }
    }

    private void abrirTelaRelatorios() {
        try {
            System.out.println("DEBUG: Tentando abrir tela de relatórios");
            javafx.fxml.FXMLLoader loader = new javafx.fxml.FXMLLoader(
                getClass().getResource("/view/pdv_relatorios.fxml"));
            System.out.println("DEBUG: FXML de relatórios carregado, fazendo load...");
            javafx.scene.Parent root = loader.load();
            System.out.println("DEBUG: FXML de relatórios carregado com sucesso");
            
            javafx.stage.Stage stage = new javafx.stage.Stage();
            stage.setTitle("Relatórios");
            stage.setScene(new javafx.scene.Scene(root));
            System.out.println("DEBUG: Stage de relatórios configurado, mostrando janela...");
            stage.show();
            System.out.println("DEBUG: Janela de relatórios aberta com sucesso");
            
        } catch (Exception e) {
            System.out.println("DEBUG: Erro ao abrir tela de relatórios: " + e.getMessage());
            e.printStackTrace();
            atualizarStatus("Erro ao abrir tela de relatórios: " + e.getMessage());
        }
    }

    private void abrirTelaPagamento() {
        try {
            javafx.fxml.FXMLLoader loader = new javafx.fxml.FXMLLoader(
                getClass().getResource("/view/pdv_pagamento.fxml"));
            javafx.scene.Parent root = loader.load();
            
            javafx.stage.Stage stage = new javafx.stage.Stage();
            stage.setTitle("Finalizar Venda");
            stage.setScene(new javafx.scene.Scene(root));
            stage.show();
            
            // Passar dados para o controller de pagamento
            PDVPagamentoController controller = loader.getController();
            controller.setDadosVenda(pdvManager.getCarrinhoAtual(), operadorAtual);
            
        } catch (Exception e) {
            atualizarStatus("Erro ao abrir tela de pagamento: " + e.getMessage());
        }
    }

    private void abrirTelaCaixaAberto() {
        try {
            System.out.println("DEBUG: Tentando abrir tela de caixa aberto");
            javafx.fxml.FXMLLoader loader = new javafx.fxml.FXMLLoader(
                getClass().getResource("/view/pdv_caixa_aberto.fxml"));
            System.out.println("DEBUG: FXML de caixa aberto carregado, fazendo load...");
            javafx.scene.Parent root = loader.load();
            System.out.println("DEBUG: FXML de caixa aberto carregado com sucesso");
            
            javafx.stage.Stage stage = new javafx.stage.Stage();
            stage.setTitle("Caixa Aberto");
            stage.setScene(new javafx.scene.Scene(root));
            System.out.println("DEBUG: Stage de caixa aberto configurado, mostrando janela...");
            stage.show();
            System.out.println("DEBUG: Janela de caixa aberto aberta com sucesso");
            
            // Passar dados para o controller
            PDVCaixaAbertoController controller = loader.getController();
            controller.setPdvManager(pdvManager);
            controller.setOperadorAtual(operadorAtual);
            
        } catch (Exception e) {
            System.out.println("DEBUG: Erro ao abrir tela de caixa aberto: " + e.getMessage());
            e.printStackTrace();
            atualizarStatus("Erro ao abrir tela de caixa aberto: " + e.getMessage());
        }
    }

    private void abrirTelaNotaFiscal() {
        try {
            System.out.println("DEBUG: Tentando abrir tela de nota fiscal");
            javafx.fxml.FXMLLoader loader = new javafx.fxml.FXMLLoader(
                getClass().getResource("/view/pdv_nota_fiscal.fxml"));
            System.out.println("DEBUG: FXML de nota fiscal carregado, fazendo load...");
            javafx.scene.Parent root = loader.load();
            System.out.println("DEBUG: FXML de nota fiscal carregado com sucesso");
            
            javafx.stage.Stage stage = new javafx.stage.Stage();
            stage.setTitle("Nota Fiscal");
            stage.setScene(new javafx.scene.Scene(root));
            System.out.println("DEBUG: Stage de nota fiscal configurado, mostrando janela...");
            stage.show();
            System.out.println("DEBUG: Janela de nota fiscal aberta com sucesso");
            
            // Passar dados para o controller
            PDVNotaFiscalController controller = loader.getController();
            
            // Criar uma venda simulada para demonstração
            VendaPDV vendaSimulada = criarVendaSimulada();
            controller.setDadosNotaFiscal(vendaSimulada, operadorAtual);
            
        } catch (Exception e) {
            System.out.println("DEBUG: Erro ao abrir tela de nota fiscal: " + e.getMessage());
            e.printStackTrace();
            atualizarStatus("Erro ao abrir tela de nota fiscal: " + e.getMessage());
        }
    }

    private void abrirTelaConfiguracaoImpressora() {
        try {
            System.out.println("DEBUG: Tentando abrir tela de configuração de impressora");
            javafx.fxml.FXMLLoader loader = new javafx.fxml.FXMLLoader(
                getClass().getResource("/view/pdv_configuracao_impressora.fxml"));
            System.out.println("DEBUG: FXML de configuração de impressora carregado, fazendo load...");
            javafx.scene.Parent root = loader.load();
            System.out.println("DEBUG: FXML de configuração de impressora carregado com sucesso");
            
            javafx.stage.Stage stage = new javafx.stage.Stage();
            stage.setTitle("Config. Impressora");
            stage.setScene(new javafx.scene.Scene(root));
            System.out.println("DEBUG: Stage de configuração de impressora configurado, mostrando janela...");
            stage.show();
            System.out.println("DEBUG: Janela de configuração de impressora aberta com sucesso");
            
            // Passar dados para o controller
            PDVConfiguracaoImpressoraController controller = loader.getController();
            controller.setOperadorAtual(operadorAtual);
            
        } catch (Exception e) {
            System.out.println("DEBUG: Erro ao abrir tela de configuração de impressora: " + e.getMessage());
            e.printStackTrace();
            atualizarStatus("Erro ao abrir tela de configuração de impressora: " + e.getMessage());
        }
    }

    private VendaPDV criarVendaSimulada() {
        try {
            VendaPDV venda = new VendaPDV();
            venda.setNumeroCupom("CUPOM" + System.currentTimeMillis());
            venda.setDataVenda(LocalDateTime.now());
            venda.setOperador(operadorAtual);
            venda.setNumeroTerminal(pdvManager.getNumeroTerminal());
            
            // Criar cliente simulado
            Cliente cliente = new Cliente();
            cliente.setId(1L);
            cliente.setNome("João Silva");
            cliente.setCpfCnpj("123.456.789-00");
            cliente.setEndereco("Rua das Flores, 456");
            cliente.setCidade("São Paulo");
            cliente.setEstado("SP");
            cliente.setCep("01234-567");
            cliente.setTelefone("(11) 9876-5432");
            venda.setCliente(cliente);
            
            // Criar itens simulados
            java.util.List<ItemVenda> itens = new java.util.ArrayList<>();
            
            // Item 1
            Produto produto1 = new Produto();
            produto1.setId(1L);
            produto1.setNome("Produto 1 - Exemplo");
            produto1.setPrecoVenda(new BigDecimal("50.00"));
            produto1.setUnidade("UN");
            
            ItemVenda item1 = new ItemVenda();
            item1.setProduto(produto1);
            item1.setQuantidade(2);
            item1.setValorUnitario(new BigDecimal("50.00"));
            item1.setDesconto(BigDecimal.ZERO);
            itens.add(item1);
            
            // Item 2
            Produto produto2 = new Produto();
            produto2.setId(2L);
            produto2.setNome("Produto 2 - Exemplo");
            produto2.setPrecoVenda(new BigDecimal("25.50"));
            produto2.setUnidade("UN");
            
            ItemVenda item2 = new ItemVenda();
            item2.setProduto(produto2);
            item2.setQuantidade(1);
            item2.setValorUnitario(new BigDecimal("25.50"));
            item2.setDesconto(new BigDecimal("5.50"));
            itens.add(item2);
            
            venda.setItens(itens);
            
            // Calcular totais
            BigDecimal valorTotal = BigDecimal.ZERO;
            BigDecimal valorDesconto = BigDecimal.ZERO;
            
            for (ItemVenda item : itens) {
                valorTotal = valorTotal.add(item.getValorFinal());
                valorDesconto = valorDesconto.add(item.getDesconto());
            }
            
            venda.setValorTotal(valorTotal.add(valorDesconto)); // Total antes do desconto
            venda.setValorDesconto(valorDesconto);
            venda.setValorFinal(valorTotal);
            
            // Criar pagamento simulado
            Pagamento pagamento = new Pagamento();
            pagamento.setTipoPagamento("DINHEIRO");
            pagamento.setValorPago(valorTotal);
            pagamento.setValorTroco(BigDecimal.ZERO);
            venda.setPagamento(pagamento);
            
            return venda;
            
        } catch (Exception e) {
            System.err.println("Erro ao criar venda simulada: " + e.getMessage());
            return null;
        }
    }

    // Métodos Utilitários
    private String formatarMoeda(BigDecimal valor) {
        if (valor == null) return "R$ 0,00";
        return String.format("R$ %,.2f", valor).replace('.', ',');
    }

    private void mostrarAlerta(String mensagem, Alert.AlertType tipo) {
        Alert alert = new Alert(tipo);
        alert.setTitle("PDV Hermes Comercial");
        alert.setHeaderText(null);
        alert.setContentText(mensagem);
        alert.showAndWait();
    }

    private boolean confirmarSaida() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmar Saída");
        alert.setHeaderText("Deseja realmente sair do sistema?");
        alert.setContentText("Qualquer venda não finalizada será perdida.");
        
        return alert.showAndWait().get() == ButtonType.OK;
    }

    private boolean confirmarLimparCarrinho() {
        if (itensCarrinho.isEmpty()) {
            return true;
        }
        
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmar Limpeza");
        alert.setHeaderText("Deseja realmente limpar o carrinho?");
        alert.setContentText("Todos os itens serão removidos.");
        
        return alert.showAndWait().get() == ButtonType.OK;
    }

    // Getters e Setters
    public PDVManager getPdvManager() {
        return pdvManager;
    }

    public void setPdvManager(PDVManager pdvManager) {
        this.pdvManager = pdvManager;
    }

    public Usuario getOperadorAtual() {
        return operadorAtual;
    }

    public void setOperadorAtual(Usuario operadorAtual) {
        this.operadorAtual = operadorAtual;
        lblOperador.setText("Operador: " + operadorAtual.getNome());
    }
}
