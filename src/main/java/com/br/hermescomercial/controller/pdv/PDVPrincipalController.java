package com.br.hermescomercial.controller.pdv;

import com.br.hermescomercial.business.pdv.PDVManager;
// import com.br.hermescomercial.business.pdv.PagamentoManager; - não utilizado
// import com.br.hermescomercial.business.pdv.CupomFiscalManager; - não utilizado
import com.br.hermescomercial.business.pdv.ImpressoraManager;
import com.br.hermescomercial.model.ItemVenda;
import com.br.hermescomercial.model.Produto;
import com.br.hermescomercial.model.Usuario;
import com.br.hermescomercial.model.VendaPDV;
import com.br.hermescomercial.model.Cliente;
import com.br.hermescomercial.model.Pagamento;
import com.br.hermescomercial.service.ProdutoService;
import com.br.hermescomercial.service.ClienteService;
import com.br.hermescomercial.service.UsuarioServiceBasico;
import com.br.hermescomercial.service.VendaService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.control.ButtonType;
import javafx.scene.layout.VBox;
import java.math.BigDecimal;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.Timer;
import java.util.TimerTask;

public class PDVPrincipalController implements Initializable {

    private static final Logger logger = LogManager.getLogger(PDVPrincipalController.class);
    
    // Managers
    private PDVManager pdvManager;
    // private PagamentoManager pagamentoManager; - não utilizado
    // private CupomFiscalManager cupomManager; - não utilizado
    private ImpressoraManager impressoraManager;
    
    // Services
    private ProdutoService produtoService;
    private ClienteService clienteService;
    private UsuarioServiceBasico usuarioService;
    private VendaService vendaService;
    
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
    @FXML private Button btnAbrirVenda;
    @FXML private Button btnFecharVenda;
    @FXML private Button btnBuscarProduto;
    @FXML private Button btnCadastrarProduto;
    @FXML private Button btnClientes;
    @FXML private Button btnRelatorios;
    @FXML private Button btnAbrirCaixa;
    @FXML private Button btnFecharCaixa;
    @FXML private Button btnSair;
    
    // Área de Produtos
    @FXML private TextField txtBuscarProduto;
    @FXML private Button btnBuscar;
    @FXML private Button btnCodigoBarras;
    @FXML private Button btnNotaFiscal;
    @FXML private Button btnCupomFiscal;
    @FXML private Button btnConfigImpressora;
    @FXML private Button btnConfigSistema;
    @FXML private TableView<Produto> tblProdutos;
    @FXML private TableColumn<Produto, String> colCodigo;
    @FXML private TableColumn<Produto, String> colDescricao;
    @FXML private TableColumn<Produto, BigDecimal> colPreco;
    @FXML private TableColumn<Produto, Integer> colEstoque;
    @FXML private TableColumn<Produto, String> colAcoes;
    
    // Teclado Numérico Antigo (legado)
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
    
    // Painel do Caixa Aberto
    @FXML private VBox painelCaixaAberto;
    @FXML private Label lblDataAbertura;
    @FXML private Label lblOperadorCaixa;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        inicializarManagers();
        inicializarServices();
        inicializarComponentes();
        inicializarListeners();
        inicializarTabelas();
        inicializarTimer();
        carregarDadosIniciais();
        
        // Configurar validação para campo CPF/CNPJ (apenas números)
        configurarValidacaoCpfCnpj();
        
        // Atualizar status do caixa
        atualizarStatusCaixa();
    }
    
    private void configurarValidacaoCpfCnpj() {
        // Configurar TextFormatter para aceitar apenas números no campo CPF/CNPJ
        txtBuscarClienteCpfCnpj.setTextFormatter(new TextFormatter<>(change -> {
            if (change.getControlNewText().isEmpty()) {
                return change;
            }
            
            // Permitir apenas números
            String newText = change.getControlNewText().replaceAll("[^0-9]", "");
            
            // Limitar tamanho máximo (18 para CNPJ, 14 para CPF)
            if (newText.length() > 18) {
                newText = newText.substring(0, 18);
            }
            
            if (change.getControlNewText().equals(newText)) {
                return change;
            } else {
                change.setText(newText);
                return change;
            }
        }));
        
        // Adicionar listener para mostrar alerta sobre caracteres inválidos
        txtBuscarClienteCpfCnpj.textProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue != null && !newValue.matches("\\d*")) {
                mostrarAlerta("O campo CPF/CNPJ aceita apenas números.\n" +
                           "Caracteres inválidos foram removidos automaticamente.", 
                           Alert.AlertType.WARNING);
            }
        });
    }
    
    /**
     * Inicializa os serviços de negócio
     */
    private void inicializarServices() {
        try {
            // Inicializar services
            produtoService = new ProdutoService();
            clienteService = new ClienteService();
            usuarioService = new UsuarioServiceBasico();
            vendaService = new VendaService();
            
            System.out.println("DEBUG: Services inicializados com sucesso");
            
        } catch (Exception e) {
            System.err.println("ERRO ao inicializar serviços: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void inicializarManagers() {
        // Inicializar managers usando Singleton
        this.pdvManager = PDVManager.getInstance();
        // this.pagamentoManager = PagamentoManager.getInstance(); - não utilizado
        // this.cupomManager = CupomFiscalManager.getInstance(); - não utilizado
        this.impressoraManager = ImpressoraManager.getInstance();
        
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
        // Listener para busca de produtos (verificar se componente existe no FXML)
        if (txtBuscarProduto != null) {
            txtBuscarProduto.textProperty().addListener((obs, oldValue, newValue) -> {
                if (newValue != null && !newValue.trim().isEmpty()) {
                    buscarProdutos();
                }
            });
        }
        
        // Listener para busca de cliente por nome
        txtBuscarClienteNome.textProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue != null && !newValue.trim().isEmpty()) {
                buscarClientePorNome(newValue);
            }
        });
        
        // Listener para busca de cliente por CPF/CNPJ
        txtBuscarClienteCpfCnpj.textProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue != null && !newValue.matches("\\d*")) {
                // Remove caracteres não numéricos apenas se for diferente
                String textoFiltrado = newValue.replaceAll("[^0-9]", "");
                if (!newValue.equals(textoFiltrado)) {
                    txtBuscarClienteCpfCnpj.setText(textoFiltrado);
                    
                    // Exibe alerta sobre caracteres inválidos
                    mostrarAlerta("O campo CPF/CNPJ aceita apenas números.\n" +
                                 "Caracteres inválidos foram removidos automaticamente.", 
                                 Alert.AlertType.WARNING);
                }
                return;
            }
            
            // Limita o tamanho máximo (14 para CPF, 18 para CNPJ)
            if (newValue.length() > 18) {
                String textoLimitado = newValue.substring(0, 18);
                if (!newValue.equals(textoLimitado)) {
                    txtBuscarClienteCpfCnpj.setText(textoLimitado);
                }
                return;
            }
            
            if (newValue != null && !newValue.trim().isEmpty()) {
                buscarClientePorCpfCnpj(newValue);
            }
        });
        
        // Listener para seleção na tabela de produtos (removido - tela de produtos não está mais na principal)
        // tblProdutos foi removido do FXML
        
        // Listener para atualização do carrinho
        itensCarrinho.addListener((javafx.collections.ListChangeListener<ItemVenda>) change -> {
            atualizarTotais();
            atualizarBotoesCarrinho();
        });
    }

    private void inicializarTabelas() {
        // Configurar tabela de produtos (removido - tela de produtos não está mais na principal)
        // tblProdutos e colunas foram removidos do FXML
        
        // Configurar tabela de carrinho (removido - tela de carrinho não está mais na principal)
        // tblCarrinho e colunas foram removidos do FXML
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
        // carregarItensExemploCarrinho() removido - tela de carrinho não está mais na principal
        // atualizarTotais() removido - labels do carrinho foram removidos do FXML
        // atualizarBotoesCarrinho() removido - botões do carrinho foram removidos do FXML
    }

    // Métodos de Ação dos Botões

    
    @FXML
    private void onAbrirVenda() {
        logger.debug("Botão Abrir Venda pressionado");
        atualizarStatus("Abrindo nova venda...");
        abrirNovaVenda();
    }
    
    /**
     * Abre uma nova venda no PDV
     */
    private void abrirNovaVenda() {
        try {
            // Limpa o carrinho atual
            if (itensCarrinho != null) {
                itensCarrinho.clear();
            } else {
                itensCarrinho = FXCollections.observableArrayList();
            }
            
            // Validar operador usando usuarioService
            if (operadorAtual != null && usuarioService != null) {
                // Verificar se o operador está válido
                if (usuarioService.existeUsuario(operadorAtual.getNome())) {
                    logger.info("Operador validado: {}", operadorAtual.getNome());
                } else {
                    logger.warn("Aviso: Operador não encontrado no sistema: {}", operadorAtual.getNome());
                }
            }
            
            // Criar nova venda usando o serviço
            if (vendaService != null) {
                // TODO: Implementar criação de venda quando o método estiver disponível
                // vendaService.criarVenda(operadorAtual);
            }
            
            // Abrir tela de venda
            abrirTelaVenda();
            
            atualizarStatus("Nova venda aberta com sucesso");
            logger.info("Nova venda aberta pelo operador: {}", operadorAtual != null ? operadorAtual.getNome() : "Desconhecido");
            
        } catch (Exception e) {
            atualizarStatus("Erro ao abrir nova venda");
            showAlert("Erro", "Não foi possível abrir nova venda: " + e.getMessage());
        }
    }
    
    /**
     * Mostra um alerta para o usuário
     * @param titulo Título do alerta
     * @param mensagem Mensagem do alerta
     */
    private void showAlert(String titulo, String mensagem) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensagem);
        alert.showAndWait();
    }

    @FXML
    private void onFecharVenda() {
        System.out.println("DEBUG: Botão Fechar Venda pressionado");
        
        // Verificar se há uma venda em andamento
        if (pdvManager.getCarrinhoAtual() == null || pdvManager.getCarrinhoAtual().estaVazio()) {
            mostrarAlerta("Não há venda em andamento para fechar.", Alert.AlertType.WARNING);
            return;
        }
        
        // Confirmar ação do usuário
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmar Fechamento de Venda");
        alert.setHeaderText("Deseja realmente fechar esta venda?");
        alert.setContentText("Esta ação não poderá ser desfeita.");
        
        if (alert.showAndWait().orElse(ButtonType.CANCEL) == ButtonType.OK) {
            try {
                // Cancelar a venda atual usando o serviço quando disponível
                boolean sucesso = pdvManager.cancelarVenda();
                
                // TODO: Usar vendaService quando implementado
                // if (vendaService != null && pdvManager.getCarrinhoAtual() != null) {
                //     vendaService.cancelarVenda(pdvManager.getCarrinhoAtual().getId());
                // }
                
                if (sucesso) {
                    atualizarStatus("Venda fechada com sucesso!");
                    limparCarrinho();
                    mostrarAlerta("Venda fechada com sucesso!", Alert.AlertType.INFORMATION);
                } else {
                    atualizarStatus("Erro ao fechar venda!");
                    mostrarAlerta("Ocorreu um erro ao fechar a venda.", Alert.AlertType.ERROR);
                }
            } catch (Exception e) {
                atualizarStatus("Erro ao fechar venda: " + e.getMessage());
                mostrarAlerta("Erro ao fechar venda: " + e.getMessage(), Alert.AlertType.ERROR);
            }
        }
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
    private void onCupomFiscal() {
        System.out.println("DEBUG: Botão Cupom Fiscal pressionado");
        atualizarStatus("Abrindo tela de cupom fiscal...");
        abrirTelaCupomFiscal();
    }

    @FXML
    private void onCadastrarProduto() {
        System.out.println("DEBUG: Botão Cadastrar Produto pressionado");
        atualizarStatus("Abrindo tela de cadastro de produto...");
        abrirTelaCadastroProduto();
    }
    
    @FXML
    private void onBuscarProduto() {
        logger.debug("Botão Buscar Produto pressionado");
        atualizarStatus("Abrindo tela de busca de produtos...");
        abrirTelaBuscaProduto();
    }
    
    private void buscarProdutos() {
        try {
            if (txtBuscarProduto == null) {
                logger.warn("txtBuscarProduto é nulo, não é possível buscar produtos");
                atualizarStatus("Campo de busca de produtos não disponível");
                return;
            }
            
            String termoBusca = txtBuscarProduto.getText();
            List<Produto> produtos = produtoService.buscarPorNome(termoBusca);
            produtosDisponiveis.setAll(produtos);
            tblProdutos.refresh();
            atualizarStatus("Encontrados " + produtos.size() + " produtos");
        } catch (Exception e) {
            logger.error("Erro ao buscar produtos: {}", e.getMessage(), e);
            atualizarStatus("Erro ao buscar produtos");
            showAlert("Erro", "Não foi possível buscar produtos: " + e.getMessage());
        }
    }

        
    @FXML
    private void onConfigSistema() {
        System.out.println("DEBUG: Botão Configuração de Sistema pressionado");
        atualizarStatus("Abrindo tela de configuração do sistema...");
        abrirTelaConfiguracaoSistema();
    }
    
    private void abrirTelaConfiguracaoSistema() {
        try {
            System.out.println("DEBUG: Tentando abrir tela de configuração do sistema");
            javafx.fxml.FXMLLoader loader = new javafx.fxml.FXMLLoader(
                getClass().getResource("/view/sistema_config.fxml"));
            javafx.scene.Parent root = loader.load();
            
            javafx.stage.Stage stage = new javafx.stage.Stage();
            stage.setTitle("Configurações do Sistema");
            stage.setScene(new javafx.scene.Scene(root));
            stage.initModality(javafx.stage.Modality.APPLICATION_MODAL);
            stage.showAndWait();
            
            atualizarStatus("Configurações do sistema carregadas");
            
        } catch (Exception e) {
            System.err.println("ERRO ao abrir tela de configuração do sistema: " + e.getMessage());
            e.printStackTrace();
            atualizarStatus("Erro ao abrir configurações do sistema");
            javafx.scene.control.Alert alert = new javafx.scene.control.Alert(javafx.scene.control.Alert.AlertType.ERROR);
            alert.setTitle("Erro");
            alert.setHeaderText("Não foi possível abrir a tela de configuração");
            alert.setContentText("Erro: " + e.getMessage());
            alert.showAndWait();
        }
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
        
        try {
            // Usar clienteService para buscar clientes
            List<Cliente> clientes = clienteService.buscarPorNome(busca);
            
            if (clientes.isEmpty()) {
                mostrarAlerta("Nenhum cliente encontrado com o nome: " + busca, Alert.AlertType.INFORMATION);
                return;
            }
            
            // TODO: Implementar exibição dos resultados na interface
            atualizarStatus("Encontrados " + clientes.size() + " clientes");
            
        } catch (Exception e) {
            System.err.println("Erro ao buscar clientes por nome: " + e.getMessage());
            atualizarStatus("Erro ao buscar clientes");
            showAlert("Erro", "Não foi possível buscar clientes: " + e.getMessage());
        }
        
        // Carregar dados do último cliente selecionado se o campo estiver vazio
        if (txtBuscarClienteCpfCnpj.getText().trim().isEmpty()) {
            carregarUltimoClienteSelecionado();
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
            
            // Carregar resultados e verificar se encontrou algo
            boolean encontrouResultados = controller.carregarResultados(termo, tipoBusca);
            
            if (!encontrouResultados) {
                System.out.println("DEBUG: Nenhum cliente encontrado, tela não será exibida");
                atualizarStatus("Nenhum cliente encontrado para: " + termo);
                return; // Não exibe a tela se não encontrou resultados
            }
            
            javafx.stage.Stage stage = new javafx.stage.Stage();
            stage.setTitle("Resultado da Busca de Clientes");
            stage.setScene(new javafx.scene.Scene(root));
            // Removendo modalidade para evitar bloqueio
            stage.initModality(javafx.stage.Modality.NONE);
            stage.setResizable(true);
            stage.setWidth(800);
            stage.setHeight(600);
            System.out.println("DEBUG: Stage de resultados configurado, mostrando janela...");
            
            // Usar show() não bloqueante
            stage.show();
            System.out.println("DEBUG: Janela de resultados aberta com sucesso");
            
        } catch (Exception e) {
            System.out.println("DEBUG: Erro ao abrir tela de resultados: " + e.getMessage());
            e.printStackTrace();
            atualizarStatus("Erro ao abrir tela de resultados: " + e.getMessage());
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
            if (textoBusca != null && !textoBusca.trim().isEmpty()) {
                // TODO: Implementar busca por nome quando disponível no service
                List<Usuario> clientes = new ArrayList<>(); // usuarioService.buscarPorNome(textoBusca);
                
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
            if (textoBusca != null && !textoBusca.trim().isEmpty()) {
                // TODO: Implementar busca por CPF/CNPJ quando disponível no service
                List<Usuario> clientes = new ArrayList<>(); // usuarioService.buscarPorCpfCnpj(textoBusca);
                
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
            // Atualizar label no header
            lblStatusCaixa.setText("Caixa: ABERTO");
            lblStatusCaixa.setStyle("-fx-text-fill: #27ae60; -fx-font-size: 14px; -fx-font-weight: bold; -fx-padding: 0 0 0 20;");
            
            // Mostrar painel do caixa aberto
            painelCaixaAberto.setVisible(true);
            painelCaixaAberto.setManaged(true);
            
            // Atualizar informações do painel
            LocalDateTime agora = LocalDateTime.now();
            DateTimeFormatter formatador = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
            lblDataAbertura.setText("Aberto em: " + agora.format(formatador));
            lblOperadorCaixa.setText("Operador: " + (operadorAtual != null ? operadorAtual.getNome() : "João"));
            lblValorCaixa.setText("Valor em Caixa: R$ 1.000,00");
            
        } else {
            // Atualizar label no header
            lblStatusCaixa.setText("Caixa: FECHADO");
            lblStatusCaixa.setStyle("-fx-text-fill: #e74c3c; -fx-font-size: 14px; -fx-font-weight: bold; -fx-padding: 0 0 0 20;");
            
            // Esconder painel do caixa aberto
            painelCaixaAberto.setVisible(false);
            painelCaixaAberto.setManaged(false);
            
            // Atualizar valor do caixa
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
    
    private void carregarUltimoClienteSelecionado() {
        try {
            if (pdvManager.getCarrinhoAtual() != null && 
                pdvManager.getCarrinhoAtual().getCliente() != null) {
                
                Cliente ultimoCliente = pdvManager.getCarrinhoAtual().getCliente();
                
                // Carregar nos campos de busca
                txtBuscarClienteNome.setText(ultimoCliente.getNome());
                txtBuscarClienteCpfCnpj.setText(ultimoCliente.getCpf() != null ? ultimoCliente.getCpf() : "");
                
                atualizarLabelClienteSelecionado(ultimoCliente.getNome() + " - " + 
                    (ultimoCliente.getCpf() != null ? ultimoCliente.getCpf() : ""));
                
                atualizarStatus("Último cliente carregado: " + ultimoCliente.getNome());
            }
        } catch (Exception e) {
            System.err.println("Erro ao carregar último cliente selecionado: " + e.getMessage());
            atualizarStatus("Erro ao carregar último cliente: " + e.getMessage());
        }
    }

    private void abrirTelaCadastroProduto() {
        try {
            System.out.println("DEBUG: Tentando abrir tela de cadastro de produto");
            javafx.fxml.FXMLLoader loader = new javafx.fxml.FXMLLoader(
                getClass().getResource("/view/pdv_cadastro_produto.fxml"));
            System.out.println("DEBUG: FXML de cadastro de produto carregado, fazendo load...");
            javafx.scene.Parent root = loader.load();
            System.out.println("DEBUG: FXML de cadastro de produto carregado com sucesso");
            
            javafx.stage.Stage stage = new javafx.stage.Stage();
            stage.setTitle("Cadastro de Produto");
            stage.setScene(new javafx.scene.Scene(root));
            stage.setMaximized(true);
            System.out.println("DEBUG: Stage de cadastro de produto configurado em modo maximizado, mostrando janela...");
            stage.show();
            System.out.println("DEBUG: Janela de cadastro de produto aberta com sucesso");
            
        } catch (Exception e) {
            System.out.println("DEBUG: Erro ao abrir tela de cadastro de produto: " + e.getMessage());
            atualizarStatus("Erro ao abrir tela de cadastro de produto");
            e.printStackTrace();
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
            stage.setMaximized(true);
            System.out.println("DEBUG: Stage de caixa aberto configurado em modo maximizado, mostrando janela...");
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

    
    private void abrirTelaCupomFiscal() {
        try {
            System.out.println("DEBUG: Tentando abrir tela de cupom fiscal");
            javafx.fxml.FXMLLoader loader = new javafx.fxml.FXMLLoader(
                getClass().getResource("/view/pdv_cupom_fiscal.fxml"));
            System.out.println("DEBUG: FXML de cupom fiscal carregado, fazendo load...");
            javafx.scene.Parent root = loader.load();
            System.out.println("DEBUG: FXML de cupom fiscal carregado com sucesso");
            
            javafx.stage.Stage stage = new javafx.stage.Stage();
            stage.setTitle("Cupom Fiscal");
            stage.setScene(new javafx.scene.Scene(root));
            stage.setResizable(true);
            stage.setWidth(1200);
            stage.setHeight(700);
            System.out.println("DEBUG: Stage de cupom fiscal configurado, mostrando janela...");
            stage.show();
            System.out.println("DEBUG: Janela de cupom fiscal aberta com sucesso");
            
            // Passar dados para o controller
            // PDVCupomFiscalController controller = loader.getController(); - não utilizado
            // Aqui você pode passar dados adicionais se necessário
            
        } catch (Exception e) {
            System.out.println("DEBUG: Erro ao abrir tela de cupom fiscal: " + e.getMessage());
            e.printStackTrace();
            atualizarStatus("Erro ao abrir tela de cupom fiscal: " + e.getMessage());
        }
    }

    private void abrirTelaVenda() {
        try {
            logger.debug("Tentando abrir tela de venda");
            javafx.fxml.FXMLLoader loader = new javafx.fxml.FXMLLoader(
                getClass().getResource("/view/pdv_venda.fxml"));
            System.out.println("DEBUG: FXML de venda carregado, fazendo load...");
            javafx.scene.Parent root = loader.load();
            System.out.println("DEBUG: FXML de venda carregado com sucesso");
            
            javafx.stage.Stage stage = new javafx.stage.Stage();
            stage.setTitle("Nova Venda");
            stage.setScene(new javafx.scene.Scene(root));
            stage.setResizable(true);
            stage.setMaximized(true);
            System.out.println("DEBUG: Stage de venda configurado em modo maximizado, mostrando janela...");
            stage.show();
            System.out.println("DEBUG: Janela de venda aberta com sucesso");
            
            // Passar dados para o controller
            PDVVendaController controller = loader.getController();
            if (controller != null) {
                // Aqui você pode passar dados adicionais se necessário
                logger.info("Controller da tela de venda obtido com sucesso");
            }
            
        } catch (Exception e) {
            logger.error("Erro ao abrir tela de venda: {}", e.getMessage(), e);
            atualizarStatus("Erro ao abrir tela de venda: " + e.getMessage());
        }
    }

    private void abrirTelaBuscaProduto() {
        try {
            logger.debug("Tentando abrir tela de busca de produtos");
            javafx.fxml.FXMLLoader loader = new javafx.fxml.FXMLLoader(
                getClass().getResource("/view/pdv_busca_produto.fxml"));
            System.out.println("DEBUG: FXML de busca de produto carregado, fazendo load...");
            javafx.scene.Parent root = loader.load();
            System.out.println("DEBUG: FXML de busca de produto carregado com sucesso");
            
            javafx.stage.Stage stage = new javafx.stage.Stage();
            stage.setTitle("Buscar Produtos");
            stage.setScene(new javafx.scene.Scene(root));
            stage.setResizable(true);
            stage.setMaximized(true);
            System.out.println("DEBUG: Stage de busca de produto configurado em modo maximizado, mostrando janela...");
            stage.show();
            System.out.println("DEBUG: Janela de busca de produto aberta com sucesso");
            
            // Passar dados para o controller
            PDVBuscaProdutoController controller = loader.getController();
            if (controller != null) {
                // Aqui você pode passar dados adicionais se necessário
                logger.info("Controller da tela de busca de produto obtido com sucesso");
            }
            
        } catch (Exception e) {
            logger.error("Erro ao abrir tela de busca de produtos: {}", e.getMessage(), e);
            atualizarStatus("Erro ao abrir tela de busca de produtos: " + e.getMessage());
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
