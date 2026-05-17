package com.br.hermescomercial.shared.model;


import java.math.BigDecimal;
import java.util.List;

public class NotaFiscalPagamento {
    
    private Long id;
    private NotaFiscal notaFiscal;
    private List<NotaFiscalPagamentoItem> pagamentos;
    
    public NotaFiscalPagamento() {
    }
    
    // Métodos de negócio
    public BigDecimal getValorTotalPagamentos() {
        if (pagamentos == null || pagamentos.isEmpty()) {
            return BigDecimal.ZERO;
        }
        
        return pagamentos.stream()
            .map(NotaFiscalPagamentoItem::getValorPagamento)
            .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
    
    public void adicionarPagamento(String formaPagamento, BigDecimal valor) {
        if (pagamentos != null) {
            NotaFiscalPagamentoItem item = new NotaFiscalPagamentoItem();
            item.setFormaPagamento(formaPagamento);
            item.setValorPagamento(valor);
            item.setNumeroParcela(pagamentos.size() + 1);
            pagamentos.add(item);
        }
    }
    
    // Getters e Setters
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public NotaFiscal getNotaFiscal() {
        return notaFiscal;
    }
    
    public void setNotaFiscal(NotaFiscal notaFiscal) {
        this.notaFiscal = notaFiscal;
    }
    
    public List<NotaFiscalPagamentoItem> getPagamentos() {
        return pagamentos;
    }
    
    public void setPagamentos(List<NotaFiscalPagamentoItem> pagamentos) {
        this.pagamentos = pagamentos;
    }
    
    @Override
    public String toString() {
        return "NotaFiscalPagamento{" +
                "valorTotal=" + getValorTotalPagamentos() +
                ", quantidadePagamentos=" + (pagamentos != null ? pagamentos.size() : 0) +
                '}';
    }
}
