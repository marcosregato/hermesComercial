package com.br.hermescomercial.pdv.model;

/**
 * Classe de modelo para representar uma transação diária
 * Extraída de PDVFormularioResumoDiario para melhor organização
 */
public class TransacaoDiaria {
    private String dataHora;
    private String tipo;
    private String numeroDocumento;
    private String cliente;
    private String valor;
    private String formaPagamento;
    private String vendedor;
    private String status;
    
    // Getters e Setters
    public String getDataHora() { return dataHora; }
    public void setDataHora(String dataHora) { this.dataHora = dataHora; }
    
    public String getTipo() { return tipo; }
    public void setTipo(String tipo) { this.tipo = tipo; }
    
    public String getNumeroDocumento() { return numeroDocumento; }
    public void setNumeroDocumento(String numeroDocumento) { this.numeroDocumento = numeroDocumento; }
    
    public String getCliente() { return cliente; }
    public void setCliente(String cliente) { this.cliente = cliente; }
    
    public String getValor() { return valor; }
    public void setValor(String valor) { this.valor = valor; }
    
    public String getFormaPagamento() { return formaPagamento; }
    public void setFormaPagamento(String formaPagamento) { this.formaPagamento = formaPagamento; }
    
    public String getVendedor() { return vendedor; }
    public void setVendedor(String vendedor) { this.vendedor = vendedor; }
    
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}
