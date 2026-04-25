package com.br.hermescomercial.model;

public class Despesa {
    private Long id;
    private String descricao;
    private Double valor;
    private String tipo;
    private String dataVencimento;
    private String dataPagamento;
    private String status;
    private String categoria;

    public Despesa() {
    }

    public Despesa(Long id, String descricao, Double valor, String tipo, String dataVencimento, String dataPagamento, String status, String categoria) {
        this.id = id;
        this.descricao = descricao;
        this.valor = valor;
        this.tipo = tipo;
        this.dataVencimento = dataVencimento;
        this.dataPagamento = dataPagamento;
        this.status = status;
        this.categoria = categoria;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }
    
    public void setNome(String nome) {
        this.descricao = nome;
    }
    
    public String getNome() {
        return this.descricao;
    }

    public Double getValor() {
        return valor;
    }

    public void setValor(Double valor) {
        this.valor = valor;
    }
    
    public void setValor(float valor) {
        this.valor = (double) valor;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getDataVencimento() {
        return dataVencimento;
    }

    public void setDataVencimento(String dataVencimento) {
        this.dataVencimento = dataVencimento;
    }

    public String getDataPagamento() {
        return dataPagamento;
    }

    public void setDataPagamento(String dataPagamento) {
        this.dataPagamento = dataPagamento;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    @Override
    public String toString() {
        return "Despesa{" +
                "id=" + id +
                ", descricao='" + descricao + '\'' +
                ", valor=" + valor +
                ", tipo='" + tipo + '\'' +
                ", dataVencimento='" + dataVencimento + '\'' +
                ", dataPagamento='" + dataPagamento + '\'' +
                ", status='" + status + '\'' +
                ", categoria='" + categoria + '\'' +
                '}';
    }
}
