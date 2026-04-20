package com.br.hermescomercial.business.pdv;

import com.br.hermescomercial.model.Pagamento;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class PagamentoManager {
    
    private static final Logger logger = LogManager.getLogger(PagamentoManager.class);
    
    // Instância Singleton
    private static volatile PagamentoManager instance;
    
    private List<String> formasPagamentoPermitidas;
    private BigDecimal valorMinimoPagamento;
    private boolean permiteMultiplosPagamentos;
    private int maximoParcelas;

    // Construtor privado para Singleton
    private PagamentoManager() {
        this.formasPagamentoPermitidas = new ArrayList<>();
        this.formasPagamentoPermitidas.add("DINHEIRO");
        this.formasPagamentoPermitidas.add("CARTAO_DEBITO");
        this.formasPagamentoPermitidas.add("CARTAO_CREDITO");
        this.formasPagamentoPermitidas.add("PIX");
        this.formasPagamentoPermitidas.add("TRANSFERENCIA");
        
        this.valorMinimoPagamento = new BigDecimal("0.01");
        this.permiteMultiplosPagamentos = true;
        this.maximoParcelas = 12;
    }
    
    /**
     * Método Singleton para obter a única instância do PagamentoManager
     * @return Instância única do PagamentoManager
     */
    public static PagamentoManager getInstance() {
        if (instance == null) {
            synchronized (PagamentoManager.class) {
                if (instance == null) {
                    instance = new PagamentoManager();
                }
            }
        }
        return instance;
    }

    /**
     * Processa pagamento único
     * @param tipoPagamento Tipo de pagamento
     * @param valorPago Valor pago
     * @param valorTotal Valor total da compra
     * @return Pagamento processado ou null se erro
     */
    public Pagamento processarPagamentoUnico(String tipoPagamento, BigDecimal valorPago, BigDecimal valorTotal) {
        try {
            if (!validarFormaPagamento(tipoPagamento)) {
                logger.error("Forma de pagamento não permitida: " + tipoPagamento);
                return null;
            }

            if (!validarValorPagamento(valorPago, valorTotal)) {
                logger.error("Valor pago inválido: " + valorPago + " para total: " + valorTotal);
                return null;
            }

            Pagamento pagamento = new Pagamento();
            pagamento.setTipoPagamento(tipoPagamento);
            pagamento.setValorPago(valorPago);
            pagamento.setDataPagamento(LocalDateTime.now());

            // Configurar dados específicos por tipo
            configurarDadosPorTipo(pagamento, tipoPagamento, valorTotal);

            // Calcular troco para dinheiro
            if ("DINHEIRO".equals(tipoPagamento)) {
                BigDecimal troco = valorPago.subtract(valorTotal);
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
     * Processa pagamento parcelado
     * @param valorTotal Valor total
     * @param numeroParcelas Número de parcelas
     * @param bandeiraCartao Bandeira do cartão
     * @return Pagamento processado ou null se erro
     */
    public Pagamento processarPagamentoParcelado(BigDecimal valorTotal, int numeroParcelas, String bandeiraCartao) {
        try {
            if (numeroParcelas <= 0 || numeroParcelas > maximoParcelas) {
                logger.error("Número de parcelas inválido: " + numeroParcelas);
                return null;
            }

            BigDecimal valorParcela = valorTotal.divide(BigDecimal.valueOf(numeroParcelas), 2, RoundingMode.HALF_UP);

            Pagamento pagamento = new Pagamento();
            pagamento.setTipoPagamento("CARTAO_CREDITO");
            pagamento.setValorPago(valorTotal);
            pagamento.setNumeroParcelas(String.valueOf(numeroParcelas));
            pagamento.setBandeiraCartao(bandeiraCartao);
            pagamento.setDataPagamento(LocalDateTime.now());

            // Gerar dados simulados de autorização
            pagamento.setNumeroAutorizacao("AUTH" + System.currentTimeMillis());
            pagamento.setNsu("NSU" + System.currentTimeMillis());

            pagamento.aprovar();
            
            logger.info("Pagamento parcelado processado: " + numeroParcelas + "x de " + valorParcela);
            return pagamento;

        } catch (Exception e) {
            logger.error("Erro ao processar pagamento parcelado: " + e.getMessage(), e);
            return null;
        }
    }

    /**
     * Processa pagamento PIX
     * @param valorTotal Valor total
     * @param chavePix Chave PIX
     * @return Pagamento processado ou null se erro
     */
    public Pagamento processarPagamentoPix(BigDecimal valorTotal, String chavePix) {
        try {
            Pagamento pagamento = new Pagamento();
            pagamento.setTipoPagamento("PIX");
            pagamento.setValorPago(valorTotal);
            pagamento.setDataPagamento(LocalDateTime.now());
            pagamento.setObservacao("PIX: " + chavePix);

            // Simular confirmação PIX (em sistema real, integrar com API PIX)
            pagamento.aprovar();
            
            logger.info("Pagamento PIX processado: " + valorTotal);
            return pagamento;

        } catch (Exception e) {
            logger.error("Erro ao processar pagamento PIX: " + e.getMessage(), e);
            return null;
        }
    }

    /**
     * Processa múltiplos pagamentos
     * @param pagamentos Lista de pagamentos
     * @param valorTotal Valor total da compra
     * @return true se processado com sucesso
     */
    public boolean processarMultiplosPagamentos(List<Pagamento> pagamentos, BigDecimal valorTotal) {
        try {
            if (!permiteMultiplosPagamentos) {
                logger.error("Múltiplos pagamentos não permitidos");
                return false;
            }

            BigDecimal totalPago = BigDecimal.ZERO;
            
            for (Pagamento pagamento : pagamentos) {
                if (!validarFormaPagamento(pagamento.getTipoPagamento())) {
                    logger.error("Forma de pagamento inválida: " + pagamento.getTipoPagamento());
                    return false;
                }
                
                totalPago = totalPago.add(pagamento.getValorPago());
                pagamento.aprovar();
            }

            if (totalPago.compareTo(valorTotal) < 0) {
                logger.error("Valor pago insuficiente: " + totalPago + " para total: " + valorTotal);
                return false;
            }

            logger.info("Múltiplos pagamentos processados - Total: " + totalPago);
            return true;

        } catch (Exception e) {
            logger.error("Erro ao processar múltiplos pagamentos: " + e.getMessage(), e);
            return false;
        }
    }

    /**
     * Cancela pagamento
     * @param pagamento Pagamento a ser cancelado
     * @return true se cancelado com sucesso
     */
    public boolean cancelarPagamento(Pagamento pagamento) {
        try {
            if (pagamento == null) {
                logger.error("Pagamento nulo para cancelamento");
                return false;
            }

            if (!pagamento.isAprovado()) {
                logger.warn("Pagamento não está aprovado para cancelamento");
                return false;
            }

            pagamento.cancelar();
            logger.info("Pagamento cancelado: " + pagamento.getTipoPagamento());
            return true;

        } catch (Exception e) {
            logger.error("Erro ao cancelar pagamento: " + e.getMessage(), e);
            return false;
        }
    }

    /**
     * Valida forma de pagamento
     */
    private boolean validarFormaPagamento(String tipoPagamento) {
        return tipoPagamento != null && formasPagamentoPermitidas.contains(tipoPagamento.toUpperCase());
    }

    /**
     * Valida valor do pagamento
     */
    private boolean validarValorPagamento(BigDecimal valorPago, BigDecimal valorTotal) {
        if (valorPago == null || valorTotal == null) {
            return false;
        }
        
        return valorPago.compareTo(valorMinimoPagamento) >= 0 && 
               valorPago.compareTo(BigDecimal.ZERO) > 0;
    }

    /**
     * Configura dados específicos por tipo de pagamento
     */
    private void configurarDadosPorTipo(Pagamento pagamento, String tipo, BigDecimal valorTotal) {
        switch (tipo.toUpperCase()) {
            case "CARTAO_CREDITO":
                pagamento.setNumeroParcelas("1");
                pagamento.setBandeiraCartao("VISA"); // Padrão, pode ser configurável
                pagamento.setNumeroAutorizacao("AUTH" + System.currentTimeMillis());
                break;
                
            case "CARTAO_DEBITO":
                pagamento.setBandeiraCartao("MAESTRO"); // Padrão
                pagamento.setNumeroAutorizacao("AUTH" + System.currentTimeMillis());
                break;
                
            case "TRANSFERENCIA":
                pagamento.setObservacao("Transferência bancária");
                break;
        }
    }

    /**
     * Obtém formas de pagamento permitidas
     */
    public List<String> getFormasPagamentoPermitidas() {
        return new ArrayList<>(formasPagamentoPermitidas);
    }

    /**
     * Adiciona nova forma de pagamento
     */
    public void adicionarFormaPagamento(String formaPagamento) {
        if (formaPagamento != null && !formasPagamentoPermitidas.contains(formaPagamento.toUpperCase())) {
            formasPagamentoPermitidas.add(formaPagamento.toUpperCase());
            logger.info("Nova forma de pagamento adicionada: " + formaPagamento);
        }
    }

    /**
     * Remove forma de pagamento
     */
    public void removerFormaPagamento(String formaPagamento) {
        if (formaPagamento != null && formasPagamentoPermitidas.contains(formaPagamento.toUpperCase())) {
            formasPagamentoPermitidas.remove(formaPagamento.toUpperCase());
            logger.info("Forma de pagamento removida: " + formaPagamento);
        }
    }

    // Getters e Setters
    public BigDecimal getValorMinimoPagamento() {
        return valorMinimoPagamento;
    }

    public void setValorMinimoPagamento(BigDecimal valorMinimoPagamento) {
        this.valorMinimoPagamento = valorMinimoPagamento;
    }

    public boolean isPermiteMultiplosPagamentos() {
        return permiteMultiplosPagamentos;
    }

    public void setPermiteMultiplosPagamentos(boolean permiteMultiplosPagamentos) {
        this.permiteMultiplosPagamentos = permiteMultiplosPagamentos;
    }

    public int getMaximoParcelas() {
        return maximoParcelas;
    }

    public void setMaximoParcelas(int maximoParcelas) {
        this.maximoParcelas = maximoParcelas;
    }
}
