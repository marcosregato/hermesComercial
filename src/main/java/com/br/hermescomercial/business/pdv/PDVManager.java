package com.br.hermescomercial.business.pdv;

// import com.br.hermescomercial.dao.VendaDao; - não utilizado
import com.br.hermescomercial.model.CarrinhoCompras;
import com.br.hermescomercial.model.Pagamento;
import com.br.hermescomercial.model.Produto;
import com.br.hermescomercial.model.Usuario;
import com.br.hermescomercial.model.VendaPDV;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.math.BigDecimal;
import java.time.LocalDateTime;
// import java.util.List; - não utilizado

public class PDVManager {
    
    private static final Logger logger = LogManager.getLogger(PDVManager.class);
    
    // Instância Singleton
    private static volatile PDVManager instance;
    
    private CarrinhoCompras carrinhoAtual;
    private Usuario operadorAtual;
    // private VendaDao vendaDao; - não utilizado
    
    // Configurações do PDV
    private boolean caixaAberto;
    private boolean modoTreinamento;
    private String numeroTerminal;
    
    // Cache para performance
    private java.util.Map<String, Produto> cacheProdutos;
    private long cacheProdutosTimestamp;
    private static final long CACHE_DURATION_MS = 300000; // 5 minutos

    // Construtor privado para Singleton
    private PDVManager() {
        // this.vendaDao = new VendaDao(); - não utilizado
        this.caixaAberto = false;
        this.modoTreinamento = false;
        this.numeroTerminal = "001";
        this.cacheProdutos = new java.util.concurrent.ConcurrentHashMap<>();
        this.cacheProdutosTimestamp = 0;
        logger.info("PDVManager inicializado");
    }
    
    /**
     * Método Singleton para obter a única instância do PDVManager
     * @return Instância única do PDVManager
     */
    public static PDVManager getInstance() {
        if (instance == null) {
            synchronized (PDVManager.class) {
                if (instance == null) {
                    instance = new PDVManager();
                }
            }
        }
        return instance;
    }

    /**
     * Inicia uma nova sessão do PDV
     * @param operador Usuário operador
     * @return true se iniciado com sucesso
     */
    public boolean iniciarSessaoPDV(Usuario operador) {
        try {
            if (operador == null) {
                logger.error("Operador não pode ser nulo para iniciar sessão PDV");
                return false;
            }

            if (!caixaAberto) {
                logger.warn("Caixa não está aberto. Operador deve abrir o caixa primeiro.");
                return false;
            }

            this.operadorAtual = operador;
            this.carrinhoAtual = new CarrinhoCompras();
            this.carrinhoAtual.setOperador(operador);

            logger.info("Sessão PDV iniciada para operador: " + operador.getNome());
            return true;

        } catch (Exception e) {
            logger.error("Erro ao iniciar sessão PDV: " + e.getMessage(), e);
            return false;
        }
    }

    /**
     * Adiciona produto ao carrinho
     * @param produto Produto a ser adicionado
     * @param quantidade Quantidade
     * @return true se adicionado com sucesso
     */
    public boolean adicionarProduto(Produto produto, int quantidade) {
        try {
            if (carrinhoAtual == null || !carrinhoAtual.estaAberto()) {
                logger.error("Carrinho não está aberto para adicionar produtos");
                return false;
            }

            if (produto == null || quantidade <= 0) {
                logger.error("Produto ou quantidade inválidos");
                return false;
            }

            // Verificar estoque disponível
            if (!verificarEstoqueDisponivel(produto, quantidade)) {
                logger.warn("Estoque insuficiente para produto: " + produto.getNome());
                return false;
            }

            // Obter preço do produto
            BigDecimal valorUnitario = produto.getPrecoVenda();
            
            carrinhoAtual.adicionarItem(produto, quantidade, valorUnitario);
            
            logger.info("Produto adicionado ao carrinho: " + produto.getNome() + 
                       " (Qtd: " + quantidade + ", Valor: " + valorUnitario + ")");
            
            return true;

        } catch (Exception e) {
            logger.error("Erro ao adicionar produto ao carrinho: " + e.getMessage(), e);
            return false;
        }
    }

    /**
     * Remove item do carrinho
     * @param codigoProduto Código do produto
     * @return true se removido com sucesso
     */
    public boolean removerItem(String codigoProduto) {
        try {
            if (carrinhoAtual == null) {
                logger.error("Carrinho não está disponível");
                return false;
            }

            // Buscar produto pelo código
            Produto produto = buscarProdutoPorCodigo(codigoProduto);
            if (produto == null) {
                logger.error("Produto não encontrado: " + codigoProduto);
                return false;
            }

            carrinhoAtual.removerItemPorProduto(produto);
            
            logger.info("Item removido do carrinho: " + produto.getNome());
            return true;

        } catch (Exception e) {
            logger.error("Erro ao remover item do carrinho: " + e.getMessage(), e);
            return false;
        }
    }

    /**
     * Aplica desconto em item do carrinho
     * @param produtoId ID do produto
     * @param desconto Valor do desconto
     * @return true se aplicado com sucesso
     */
    public boolean aplicarDescontoItem(String produtoId, BigDecimal desconto) {
        try {
            if (carrinhoAtual == null || carrinhoAtual.estaVazio()) {
                logger.error("Carrinho vazio para aplicar desconto");
                return false;
            }

            // Encontrar item no carrinho
            for (var item : carrinhoAtual.getItens()) {
                if (produtoId.equals(item.getProduto().getId().toString())) {
                    carrinhoAtual.aplicarDescontoItem(item, desconto);
                    logger.info("Desconto aplicado no item: " + item.getProduto().getNome() + " (-" + desconto + ")");
                    return true;
                }
            }

            return false;

        } catch (Exception e) {
            logger.error("Erro ao aplicar desconto: " + e.getMessage(), e);
            return false;
        }
    }

    /**
     * Valida quantidade de itens no carrinho
     * @param quantidade Quantidade a ser validada
     * @return true se válida
     */
    public boolean validarQuantidade(int quantidade) {
        try {
            if (quantidade <= 0) {
                return false;
            }
            
            // Validar quantidade máxima permitida (ex: 1000 itens)
            if (quantidade > 1000) {
                return false;
            }
            
            return true;
        } catch (Exception e) {
            logger.error("Erro ao validar quantidade: " + e.getMessage(), e);
            return false;
        }
    }

    /**
     * Valida produto para adicionar ao carrinho
     * @param produto Produto a ser validado
     * @return true se válido
     */
    public boolean validarProduto(Produto produto) {
        try {
            if (produto == null) {
                return false;
            }
            
            if (produto.getNome() == null || produto.getNome().trim().isEmpty()) {
                return false;
            }
            
            if (produto.getPrecoVenda() == null || produto.getPrecoVenda().compareTo(BigDecimal.ZERO) <= 0) {
                return false;
            }
            
            return true;
        } catch (Exception e) {
            logger.error("Erro ao validar produto: " + e.getMessage(), e);
            return false;
        }
    }

    /**
     * Processa pagamento da venda
     * @param tipoPagamento Tipo de pagamento
     * @param valorPago Valor pago
     * @return Pagamento processado
     */
    public Pagamento processarPagamento(String tipoPagamento, BigDecimal valorPago) {
        try {
            if (carrinhoAtual == null || carrinhoAtual.estaVazio()) {
                logger.error("Carrinho vazio para processar pagamento");
                return null;
            }

            Pagamento pagamento = new Pagamento();
            pagamento.setTipoPagamento(tipoPagamento);
            pagamento.setValorPago(valorPago);

            // Calcular troco para pagamento em dinheiro
            if ("DINHEIRO".equals(tipoPagamento)) {
                BigDecimal troco = valorPago.subtract(carrinhoAtual.getValorFinal());
                pagamento.setValorTroco(troco.compareTo(BigDecimal.ZERO) > 0 ? troco : BigDecimal.ZERO);
            }

            pagamento.aprovar();
            logger.info("Pagamento processado: " + tipoPagamento + " - Valor: " + valorPago);
            
            return pagamento;

        } catch (Exception e) {
            logger.error("Erro ao processar pagamento: " + e.getMessage(), e);
            return null;
        }
    }

    /**
     * Finaliza a venda
     * @param pagamento Pagamento processado
     * @return Venda finalizada
     */
    public VendaPDV finalizarVenda(Pagamento pagamento) {
        try {
            if (carrinhoAtual == null || carrinhoAtual.estaVazio() || pagamento == null) {
                logger.error("Dados inválidos para finalizar venda");
                return null;
            }

            // Criar registro de venda
            VendaPDV venda = new VendaPDV();
            venda.setValorTotal(carrinhoAtual.getValorTotal());
            venda.setValorFinal(carrinhoAtual.getValorFinal());
            venda.setDataVenda(LocalDateTime.now());
            venda.setOperador(operadorAtual);
            venda.setItens(carrinhoAtual.getItens());
            venda.setPagamento(pagamento);
            venda.setNumeroTerminal(numeroTerminal);
            venda.setNumeroCupom(gerarNumeroCupom());
            
            if (carrinhoAtual.getCliente() != null) {
                venda.setCliente(carrinhoAtual.getCliente());
            }

            venda.concluir();

            // Salvar venda no banco
            if (!modoTreinamento) {
                // vendaDao.salvar(venda); // Implementar quando DAO estiver pronto
                
                // Dar baixa no estoque
                for (var item : carrinhoAtual.getItens()) {
                    darBaixaEstoque(item.getProduto(), item.getQuantidade());
                }
                
                // Registrar movimento no caixa (implementação futura)
                logger.info("Registrando entrada no caixa: {} - Terminal {}", 
                           carrinhoAtual.getValorFinal(), numeroTerminal);
            }

            // Fechar carrinho
            carrinhoAtual.fecharCarrinho();

            logger.info("Venda finalizada com sucesso - Valor: " + venda.getValorFinal());
            return venda;

        } catch (Exception e) {
            logger.error("Erro ao finalizar venda: " + e.getMessage(), e);
            return null;
        }
    }

    /**
     * Gera número do cupom fiscal
     */
    private String gerarNumeroCupom() {
        return "CUPOM" + System.currentTimeMillis();
    }

    /**
     * Cancela a venda atual
     * @return true se cancelado com sucesso
     */
    public boolean cancelarVenda() {
        try {
            if (carrinhoAtual == null || !carrinhoAtual.estaAberto()) {
                logger.warn("Não há venda aberta para cancelar");
                return false;
            }

            carrinhoAtual.cancelarCarrinho();
            logger.info("Venda cancelada pelo operador: " + operadorAtual.getNome());
            
            // Iniciar novo carrinho
            this.carrinhoAtual = new CarrinhoCompras();
            this.carrinhoAtual.setOperador(operadorAtual);
            
            return true;

        } catch (Exception e) {
            logger.error("Erro ao cancelar venda: " + e.getMessage(), e);
            return false;
        }
    }

    /**
     * Abre o caixa
     * @return true se aberto com sucesso
     */
    public boolean abrirCaixa() {
        try {
            if (caixaAberto) {
                logger.warn("Caixa já está aberto");
                return false;
            }

            // Aqui futuramente implementar verificação de permissões, validações, etc.
            // Por enquanto, apenas altera o status
            this.caixaAberto = true;
            logger.info("Caixa aberto com sucesso - Terminal: " + numeroTerminal);
            return true;

        } catch (Exception e) {
            logger.error("Erro ao abrir caixa: " + e.getMessage(), e);
            return false;
        }
    }

    /**
     * Fecha o caixa
     * @return true se fechado com sucesso
     */
    public boolean fecharCaixa() {
        try {
            if (!caixaAberto) {
                logger.warn("Caixa já está fechado");
                return false;
            }

            // Aqui futuramente implementar validações, cálculos de fechamento, etc.
            // Por enquanto, apenas altera o status
            this.caixaAberto = false;
            logger.info("Caixa fechado com sucesso - Terminal: " + numeroTerminal);
            return true;

        } catch (Exception e) {
            logger.error("Erro ao fechar caixa: " + e.getMessage(), e);
            return false;
        }
    }

    
    /**
     * Verifica se há estoque disponível
     */
    private boolean verificarEstoqueDisponivel(Produto produto, int quantidade) {
        try {
            // Lógica para verificar estoque - implementar conforme necessário
            return true; // Simplificado para exemplo
        } catch (Exception e) {
            logger.error("Erro ao verificar estoque: " + e.getMessage(), e);
            return false;
        }
    }

    /**
     * Busca produto usando cache para melhorar performance
     */
    public Produto buscarProdutoPorCodigo(String codigo) {
        // Verificar se o cache está válido
        long currentTime = System.currentTimeMillis();
        if (currentTime - cacheProdutosTimestamp > CACHE_DURATION_MS) {
            // Cache expirou, limpar
            cacheProdutos.clear();
            cacheProdutosTimestamp = currentTime;
        }
        
        // Buscar do cache primeiro
        Produto produto = cacheProdutos.get(codigo);
        if (produto != null) {
            logger.debug("Produto encontrado no cache: " + codigo);
            return produto;
        }
        
        // Se não encontrou no cache, buscar do banco (simulado)
        produto = buscarProdutoNoBanco(codigo);
        if (produto != null) {
            cacheProdutos.put(codigo, produto);
            logger.debug("Produto carregado e cacheado: " + codigo);
        }
        
        return produto;
    }
    
    /**
     * Simula busca de produto no banco de dados
     */
    private Produto buscarProdutoNoBanco(String codigo) {
        // Simulação - em produção seria consulta real ao banco
        if (codigo != null && !codigo.trim().isEmpty()) {
            Produto produto = new Produto();
            produto.setId(Long.parseLong(codigo.replaceAll("[^0-9]", "")));
            produto.setCodigo(codigo);
            produto.setNome("Produto " + codigo);
            produto.setPrecoVenda(new java.math.BigDecimal("10.00"));
            produto.setEstoque(100);
            return produto;
        }
        return null;
    }
    
    /**
     * Limpa cache de produtos
     */
    public void limparCacheProdutos() {
        cacheProdutos.clear();
        cacheProdutosTimestamp = 0;
        logger.info("Cache de produtos limpo");
    }

    /**
     * Dá baixa no estoque
     */
    private void darBaixaEstoque(Produto produto, int quantidade) {
        try {
            // Implementar baixa de estoque
            logger.info("Baixa de estoque: " + produto.getNome() + " - Qtd: " + quantidade);
        } catch (Exception e) {
            logger.error("Erro ao dar baixa no estoque: " + e.getMessage(), e);
        }
    }

    // Getters e Setters
    public CarrinhoCompras getCarrinhoAtual() {
        return carrinhoAtual;
    }

    public boolean isCaixaAberto() {
        return caixaAberto;
    }

    public Usuario getOperadorAtual() {
        return operadorAtual;
    }

    public String getNumeroTerminal() {
        return numeroTerminal;
    }

    public boolean isModoTreinamento() {
        return modoTreinamento;
    }

    /**
     * Valida valor monetário
     * @param valor Valor a ser validado
     * @return true se válido
     */
    public boolean validarValor(BigDecimal valor) {
        try {
            if (valor == null) {
                return false;
            }
            
            // Validações básicas
            if (valor.compareTo(BigDecimal.ZERO) < 0) {
                return false;
            }
            
            // Valor máximo permitido (ex: R$ 10.000,00)
            BigDecimal valorMaximo = new BigDecimal("10000.00");
            if (valor.compareTo(valorMaximo) > 0) {
                return false;
            }
            
            return true;
        } catch (Exception e) {
            logger.error("Erro ao validar valor: " + e.getMessage(), e);
            return false;
        }
    }

    /**
     * Cria objeto VendaPDV a partir do carrinho atual
     * @return VendaPDV criada
     */
    public VendaPDV criarVendaPDV() {
        try {
            if (carrinhoAtual == null || carrinhoAtual.estaVazio()) {
                logger.error("Carrinho vazio para criar venda");
                return null;
            }

            VendaPDV venda = new VendaPDV();
            venda.setValorTotal(carrinhoAtual.getValorTotal());
            venda.setValorFinal(carrinhoAtual.getValorFinal());
            venda.setDataVenda(LocalDateTime.now());
            venda.setOperador(operadorAtual);
            venda.setItens(carrinhoAtual.getItens());
            venda.setNumeroTerminal(numeroTerminal);
            venda.setNumeroCupom(gerarNumeroCupom());
            
            if (carrinhoAtual.getCliente() != null) {
                venda.setCliente(carrinhoAtual.getCliente());
            }

            return venda;
        } catch (Exception e) {
            logger.error("Erro ao criar venda PDV: " + e.getMessage(), e);
            return null;
        }
    }

    /**
     * Finaliza sessão PDV
     * @return true se finalizada com sucesso
     */
    public boolean finalizarSessaoPDV() {
        try {
            if (carrinhoAtual != null) {
                carrinhoAtual.fecharCarrinho();
                this.carrinhoAtual = null;
            }
            
            this.operadorAtual = null;
            logger.info("Sessão PDV finalizada");
            return true;
        } catch (Exception e) {
            logger.error("Erro ao finalizar sessão PDV: " + e.getMessage(), e);
            return false;
        }
    }

    /**
     * Obtém sessão PDV atual
     * @return Sessão atual ou null
     */
    public Object getSessaoAtual() {
        return carrinhoAtual != null ? carrinhoAtual : null;
    }

    /**
     * Cancela venda por ID
     * @param idVenda ID da venda
     * @return true se cancelada com sucesso
     */
    public boolean cancelarVenda(Long idVenda) {
        try {
            // Implementar lógica de cancelamento de venda
            // Por enquanto, apenas registra o log
            logger.info("Cancelando venda ID: " + idVenda);
            
            // Aqui deveria buscar a venda no banco e marcar como cancelada
            // vendaDao.cancelar(idVenda); // Implementar quando DAO estiver pronto
            
            return true;
        } catch (Exception e) {
            logger.error("Erro ao cancelar venda: " + e.getMessage(), e);
            return false;
        }
    }

    /**
     * Busca venda por ID
     * @param idVenda ID da venda
     * @return Venda encontrada ou null
     */
    public VendaPDV buscarVendaPorId(Long idVenda) {
        try {
            // Implementar busca no banco
            // return vendaDao.buscarPorId(idVenda); // Implementar quando DAO estiver pronto
            
            // Por enquanto, retorna null
            return null;
        } catch (Exception e) {
            logger.error("Erro ao buscar venda por ID: " + e.getMessage(), e);
            return null;
        }
    }

    public void setModoTreinamento(boolean modoTreinamento) {
        this.modoTreinamento = modoTreinamento;
    }

    public void setNumeroTerminal(String numeroTerminal) {
        this.numeroTerminal = numeroTerminal;
    }
}
