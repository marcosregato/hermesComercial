package com.br.hermescomercial.shared.model;

import com.br.hermescomercial.pdv.model.Pagamento;
import java.math.BigDecimal;

public class NotaFiscalPagamentoItem {
    
    private Long id;
    private Integer numeroParcela;
    private String formaPagamento; // 01=Dinheiro, 02=Cheque, 03=Cartão de Crédito, 04=Cartão de Débito, 05=Crédito Loja, 10=Vale Alimentação, 11=Vale Refeição, 12=Vale Presente, 13=Vale Combustível, 14=Duplicata Mercantil, 15=Boleto Bancário, 90=Sem pagamento, 99=Outros
    private String descricaoFormaPagamento;
    private BigDecimal valorPagamento;
    private String bandeiraCartao; // Visa, Mastercard, Elo, etc.
    private String autorizacaoCartao;
    private String cnpjCredenciadora;
    private String bandeiraCredenciadora;
    
    // Relacionamentos
    private NotaFiscalPagamento notaFiscalPagamento;
    
    public NotaFiscalPagamentoItem() {
        this.valorPagamento = BigDecimal.ZERO;
        this.numeroParcela = 1;
    }
    
    // Métodos de negócio
    public String getFormaPagamentoDescricao() {
        switch (formaPagamento) {
            case "01": return "Dinheiro";
            case "02": return "Cheque";
            case "03": return "Cartão de Crédito";
            case "04": return "Cartão de Débito";
            case "05": return "Crédito Loja";
            case "10": return "Vale Alimentação";
            case "11": return "Vale Refeição";
            case "12": return "Vale Presente";
            case "13": return "Vale Combustível";
            case "14": return "Duplicata Mercantil";
            case "15": return "Boleto Bancário";
            case "90": return "Sem pagamento";
            case "99": return "Outros";
            default: return descricaoFormaPagamento != null ? descricaoFormaPagamento : "Não definido";
        }
    }
    
    public void preencherComPagamento(Pagamento pagamento) {
        if (pagamento != null) {
            this.valorPagamento = pagamento.getValorPago();
            
            // Mapear tipo de pagamento para forma de pagamento da NF-e
            if ("DINHEIRO".equalsIgnoreCase(pagamento.getTipoPagamento())) {
                this.formaPagamento = "01";
                this.descricaoFormaPagamento = "Dinheiro";
            } else if ("CARTAO_CREDITO".equalsIgnoreCase(pagamento.getTipoPagamento())) {
                this.formaPagamento = "03";
                this.descricaoFormaPagamento = "Cartão de Crédito";
            } else if ("CARTAO_DEBITO".equalsIgnoreCase(pagamento.getTipoPagamento())) {
                this.formaPagamento = "04";
                this.descricaoFormaPagamento = "Cartão de Débito";
            } else if ("CHEQUE".equalsIgnoreCase(pagamento.getTipoPagamento())) {
                this.formaPagamento = "02";
                this.descricaoFormaPagamento = "Cheque";
            } else {
                this.formaPagamento = "99";
                this.descricaoFormaPagamento = pagamento.getTipoPagamento();
            }
        }
    }
    
    public String getValorFormatado() {
        if (valorPagamento == null) return "R$ 0,00";
        return String.format("R$ %,.2f", valorPagamento).replace('.', ',');
    }
    
    // Getters e Setters
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public Integer getNumeroParcela() {
        return numeroParcela;
    }
    
    public void setNumeroParcela(Integer numeroParcela) {
        this.numeroParcela = numeroParcela;
    }
    
    public String getFormaPagamento() {
        return formaPagamento;
    }
    
    public void setFormaPagamento(String formaPagamento) {
        this.formaPagamento = formaPagamento;
    }
    
    public String getDescricaoFormaPagamento() {
        return descricaoFormaPagamento;
    }
    
    public void setDescricaoFormaPagamento(String descricaoFormaPagamento) {
        this.descricaoFormaPagamento = descricaoFormaPagamento;
    }
    
    public BigDecimal getValorPagamento() {
        return valorPagamento;
    }
    
    public void setValorPagamento(BigDecimal valorPagamento) {
        this.valorPagamento = valorPagamento;
    }
    
    public String getBandeiraCartao() {
        return bandeiraCartao;
    }
    
    public void setBandeiraCartao(String bandeiraCartao) {
        this.bandeiraCartao = bandeiraCartao;
    }
    
    public String getAutorizacaoCartao() {
        return autorizacaoCartao;
    }
    
    public void setAutorizacaoCartao(String autorizacaoCartao) {
        this.autorizacaoCartao = autorizacaoCartao;
    }
    
    public String getCnpjCredenciadora() {
        return cnpjCredenciadora;
    }
    
    public void setCnpjCredenciadora(String cnpjCredenciadora) {
        this.cnpjCredenciadora = cnpjCredenciadora;
    }
    
    public String getBandeiraCredenciadora() {
        return bandeiraCredenciadora;
    }
    
    public void setBandeiraCredenciadora(String bandeiraCredenciadora) {
        this.bandeiraCredenciadora = bandeiraCredenciadora;
    }
    
    public NotaFiscalPagamento getNotaFiscalPagamento() {
        return notaFiscalPagamento;
    }
    
    public void setNotaFiscalPagamento(NotaFiscalPagamento notaFiscalPagamento) {
        this.notaFiscalPagamento = notaFiscalPagamento;
    }
    
    @Override
    public String toString() {
        return "NotaFiscalPagamentoItem{" +
                "numeroParcela=" + numeroParcela +
                ", formaPagamento='" + formaPagamento + '\'' +
                ", descricaoFormaPagamento='" + descricaoFormaPagamento + '\'' +
                ", valorPagamento=" + valorPagamento +
                '}';
    }
}
