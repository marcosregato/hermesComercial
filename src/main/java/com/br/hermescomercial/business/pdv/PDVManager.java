package com.br.hermescomercial.business.pdv;

import com.br.hermescomercial.dao.VendaDao;
import com.br.hermescomercial.model.CarrinhoCompras;
import com.br.hermescomercial.model.Pagamento;
import com.br.hermescomercial.model.Produto;
import com.br.hermescomercial.model.Usuario;
import com.br.hermescomercial.model.VendaPDV;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public class PDVManager {
    
    private static final Logger logger = LogManager.getLogger(PDVManager.class);
    
    private CarrinhoCompras carrinhoAtual;
    private Usuario operadorAtual;
    private VendaDao vendaDao;
    
    // Configurações do PDV
    private boolean caixaAberto;
    private boolean modoTreinamento;
    private String numeroTerminal;

    public PDVManager() {
        this.vendaDao = new VendaDao();
        this.carrinhoAtual = new CarrinhoCompras();
        this.modoTreinamento = false;
        this.numeroTerminal = "001";
        verificarStatusCaixa();
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
            BigDecimal valorUnitario = obterPrecoProduto(produto);
            
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
     * Aplica desconto em um item específico
     * @param codigoProduto Código do produto
     * @param desconto Valor do desconto
     * @return true se aplicado com sucesso
     */
    public boolean aplicarDescontoItem(String codigoProduto, BigDecimal desconto) {
        try {
            if (carrinhoAtual == null || desconto == null || desconto.compareTo(BigDecimal.ZERO) < 0) {
                return false;
            }

            Produto produto = buscarProdutoPorCodigo(codigoProduto);
            if (produto == null) {
                return false;
            }

            // Encontrar item no carrinho
            for (var item : carrinhoAtual.getItens()) {
                if (produto.equals(item.getProduto())) {
                    carrinhoAtual.aplicarDescontoItem(item, desconto);
                    logger.info("Desconto aplicado no item: " + produto.getNome() + " (-" + desconto + ")");
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
     * Verifica se o caixa está aberto
     */
    private void verificarStatusCaixa() {
        // Implementação futura - verificar status do caixa
        this.caixaAberto = false; // Por enquanto, sempre fechado ao iniciar
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
     * Obtém preço do produto
     */
    private BigDecimal obterPrecoProduto(Produto produto) {
        try {
            // Lógica para obter preço - implementar conforme necessário
            return new BigDecimal("10.00"); // Simplificado para exemplo
        } catch (Exception e) {
            logger.error("Erro ao obter preço do produto: " + e.getMessage(), e);
            return BigDecimal.ZERO;
        }
    }

    /**
     * Busca produto por código
     */
    private Produto buscarProdutoPorCodigo(String codigo) {
        try {
            // Implementar busca de produto por código
            return new Produto(); // Simplificado para exemplo
        } catch (Exception e) {
            logger.error("Erro ao buscar produto: " + e.getMessage(), e);
            return null;
        }
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

    public Usuario getOperadorAtual() {
        return operadorAtual;
    }

    public boolean isCaixaAberto() {
        return caixaAberto;
    }

    public boolean isModoTreinamento() {
        return modoTreinamento;
    }

    public void setModoTreinamento(boolean modoTreinamento) {
        this.modoTreinamento = modoTreinamento;
    }

    public String getNumeroTerminal() {
        return numeroTerminal;
    }

    public void setNumeroTerminal(String numeroTerminal) {
        this.numeroTerminal = numeroTerminal;
    }
}
