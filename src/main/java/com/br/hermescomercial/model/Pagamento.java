package com.br.hermescomercial.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class Pagamento {
    
    private Long id;
    private String tipoPagamento; // DINHEIRO, CARTAO_CREDITO, CARTAO_DEBITO, PIX, TRANSFERENCIA
    private BigDecimal valorPago;
    private BigDecimal valorTroco;
    private String status; // PENDENTE, APROVADO, CANCELADO
    private LocalDateTime dataPagamento;
    private String numeroParcelas;
    private String bandeiraCartao;
    private String numeroAutorizacao;
    private String nsu;
    private String cnpjEstabelecimento;
    private String observacao;

    public Pagamento() {
        this.dataPagamento = LocalDateTime.now();
        this.status = "PENDENTE";
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTipoPagamento() {
        return tipoPagamento;
    }

    public void setTipoPagamento(String tipoPagamento) {
        this.tipoPagamento = tipoPagamento;
    }

    public BigDecimal getValorPago() {
        return valorPago;
    }

    public void setValorPago(BigDecimal valorPago) {
        this.valorPago = valorPago;
    }

    public BigDecimal getValorTroco() {
        return valorTroco;
    }

    public void setValorTroco(BigDecimal valorTroco) {
        this.valorTroco = valorTroco;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public LocalDateTime getDataPagamento() {
        return dataPagamento;
    }

    public void setDataPagamento(LocalDateTime dataPagamento) {
        this.dataPagamento = dataPagamento;
    }

    public String getNumeroParcelas() {
        return numeroParcelas;
    }

    public void setNumeroParcelas(String numeroParcelas) {
        this.numeroParcelas = numeroParcelas;
    }

    public String getBandeiraCartao() {
        return bandeiraCartao;
    }

    public void setBandeiraCartao(String bandeiraCartao) {
        this.bandeiraCartao = bandeiraCartao;
    }

    public String getNumeroAutorizacao() {
        return numeroAutorizacao;
    }

    public void setNumeroAutorizacao(String numeroAutorizacao) {
        this.numeroAutorizacao = numeroAutorizacao;
    }

    public String getNsu() {
        return nsu;
    }

    public void setNsu(String nsu) {
        this.nsu = nsu;
    }

    public String getCnpjEstabelecimento() {
        return cnpjEstabelecimento;
    }

    public void setCnpjEstabelecimento(String cnpjEstabelecimento) {
        this.cnpjEstabelecimento = cnpjEstabelecimento;
    }

    public String getObservacao() {
        return observacao;
    }

    public void setObservacao(String observacao) {
        this.observacao = observacao;
    }

    public boolean isDinheiro() {
        return "DINHEIRO".equals(tipoPagamento);
    }

    public boolean isCartaoCredito() {
        return "CARTAO_CREDITO".equals(tipoPagamento);
    }

    public boolean isCartaoDebito() {
        return "CARTAO_DEBITO".equals(tipoPagamento);
    }

    public boolean isPix() {
        return "PIX".equals(tipoPagamento);
    }

    public boolean isTransferencia() {
        return "TRANSFERENCIA".equals(tipoPagamento);
    }

    public boolean isAprovado() {
        return "APROVADO".equals(status);
    }

    public boolean isPendente() {
        return "PENDENTE".equals(status);
    }

    public boolean isCancelado() {
        return "CANCELADO".equals(status);
    }

    public void aprovar() {
        this.status = "APROVADO";
    }

    public void cancelar() {
        this.status = "CANCELADO";
    }

    // Métodos adicionais para compatibilidade
    public Long getFkVendaPdv() {
        return id; // Simplificado - na prática viria de VendaPDV
    }

    public void setFkVendaPdv(Long fkVendaPdv) {
        this.id = fkVendaPdv;
    }

    public String getChavePix() {
        return nsu; // Simplificado - usando NSU como chave PIX
    }

    public void setChavePix(String chavePix) {
        this.nsu = chavePix;
    }

    public String getObservacoes() {
        return observacao;
    }

    public void setObservacoes(String observacoes) {
        this.observacao = observacoes;
    }

    @Override
    public String toString() {
        return "Pagamento{" +
                "tipoPagamento='" + tipoPagamento + '\'' +
                ", valorPago=" + valorPago +
                ", status='" + status + '\'' +
                '}';
    }
}
