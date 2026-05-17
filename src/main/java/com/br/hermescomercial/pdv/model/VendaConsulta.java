package com.br.hermescomercial.pdv.model;

/**
 * Classe de modelo para representar uma venda na consulta
 * Extraída de PDVFormularioConsultarVendas para melhor organização
 */
public class VendaConsulta {
    private String numero;
    private String data;
    private String cliente;
    private String cpf;
    private String vendedor;
    private String valorTotal;
    private String status;
    private String formaPagamento;
    
    // Getters e Setters
    public String getNumero() { return numero; }
    public void setNumero(String numero) { this.numero = numero; }
    
    public String getData() { return data; }
    public void setData(String data) { this.data = data; }
    
    public String getCliente() { return cliente; }
    public void setCliente(String cliente) { this.cliente = cliente; }
    
    public String getCpf() { return cpf; }
    public void setCpf(String cpf) { this.cpf = cpf; }
    
    public String getVendedor() { return vendedor; }
    public void setVendedor(String vendedor) { this.vendedor = vendedor; }
    
    public String getValorTotal() { return valorTotal; }
    public void setValorTotal(String valorTotal) { this.valorTotal = valorTotal; }
    
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    
    public String getFormaPagamento() { return formaPagamento; }
    public void setFormaPagamento(String formaPagamento) { this.formaPagamento = formaPagamento; }
}
