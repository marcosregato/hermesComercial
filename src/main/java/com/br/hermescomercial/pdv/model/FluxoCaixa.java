package com.br.hermescomercial.pdv.model;

/**
 * Classe de modelo para representar um fluxo de caixa
 * Extraída de PDVFormularioFluxoCaixa para melhor organização
 */
public class FluxoCaixa {
    private String data;
    private String descricao;
    private String categoria;
    private String tipo;
    private String valor;
    private String formaPagamento;
    private String status;
    
    // Getters e Setters
    public String getData() { return data; }
    public void setData(String data) { this.data = data; }
    
    public String getDescricao() { return descricao; }
    public void setDescricao(String descricao) { this.descricao = descricao; }
    
    public String getCategoria() { return categoria; }
    public void setCategoria(String categoria) { this.categoria = categoria; }
    
    public String getTipo() { return tipo; }
    public void setTipo(String tipo) { this.tipo = tipo; }
    
    public String getValor() { return valor; }
    public void setValor(String valor) { this.valor = valor; }
    
    public String getFormaPagamento() { return formaPagamento; }
    public void setFormaPagamento(String formaPagamento) { this.formaPagamento = formaPagamento; }
    
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}
