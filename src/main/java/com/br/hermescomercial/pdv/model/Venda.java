package com.br.hermescomercial.pdv.model;

import java.math.BigDecimal;
import java.sql.Timestamp;

/**
 * Classe que representa uma Venda no sistema PDV
 */
public class Venda {
    private int id;
    private String numeroVenda;
    private int idCliente;
    private String clienteNome;
    private BigDecimal valorTotal;
    private BigDecimal valorDesconto;
    private BigDecimal valorFinal;
    private String formaPagamento;
    private String status;
    private Timestamp dataVenda;
    private String usuarioVendedor;
    
    // Construtores
    public Venda() {}
    
    public Venda(String numeroVenda, BigDecimal valorTotal, String formaPagamento, String usuarioVendedor) {
        this.numeroVenda = numeroVenda;
        this.valorTotal = valorTotal;
        this.valorFinal = valorTotal;
        this.formaPagamento = formaPagamento;
        this.usuarioVendedor = usuarioVendedor;
        this.status = "PENDENTE";
        this.valorDesconto = BigDecimal.ZERO;
    }
    
    // Getters e Setters
    public int getId() {
        return id;
    }
    
    public void setId(int id) {
        this.id = id;
    }
    
    public String getNumeroVenda() {
        return numeroVenda;
    }
    
    public void setNumeroVenda(String numeroVenda) {
        this.numeroVenda = numeroVenda;
    }
    
    public int getIdCliente() {
        return idCliente;
    }
    
    public void setIdCliente(int idCliente) {
        this.idCliente = idCliente;
    }
    
    public String getClienteNome() {
        return clienteNome;
    }
    
    public void setClienteNome(String clienteNome) {
        this.clienteNome = clienteNome;
    }
    
    public BigDecimal getValorTotal() {
        return valorTotal;
    }
    
    public void setValorTotal(BigDecimal valorTotal) {
        this.valorTotal = valorTotal;
    }
    
    public BigDecimal getValorDesconto() {
        return valorDesconto;
    }
    
    public void setValorDesconto(BigDecimal valorDesconto) {
        this.valorDesconto = valorDesconto;
    }
    
    public BigDecimal getValorFinal() {
        return valorFinal;
    }
    
    public void setValorFinal(BigDecimal valorFinal) {
        this.valorFinal = valorFinal;
    }
    
    public String getFormaPagamento() {
        return formaPagamento;
    }
    
    public void setFormaPagamento(String formaPagamento) {
        this.formaPagamento = formaPagamento;
    }
    
    public String getStatus() {
        return status;
    }
    
    public void setStatus(String status) {
        this.status = status;
    }
    
    public Timestamp getDataVenda() {
        return dataVenda;
    }
    
    public void setDataVenda(Timestamp dataVenda) {
        this.dataVenda = dataVenda;
    }
    
    public String getUsuarioVendedor() {
        return usuarioVendedor;
    }
    
    public void setUsuarioVendedor(String usuarioVendedor) {
        this.usuarioVendedor = usuarioVendedor;
    }
    
    // Métodos utilitários
    public String getStatusFormatado() {
        switch (status) {
            case "CONCLUIDA":
                return "✅ Concluída";
            case "PENDENTE":
                return "⏳ Pendente";
            case "CANCELADA":
                return "❌ Cancelada";
            default:
                return status;
        }
    }
    
    public String getFormaPagamentoFormatada() {
        switch (formaPagamento) {
            case "DINHEIRO":
                return "💵 Dinheiro";
            case "CARTAO_CREDITO":
                return "💳 Cartão Crédito";
            case "CARTAO_DEBITO":
                return "💳 Cartão Débito";
            case "PIX":
                return "📱 PIX";
            case "BOLETO":
                return "📄 Boleto";
            default:
                return formaPagamento;
        }
    }
    
    @Override
    public String toString() {
        return numeroVenda + " - " + clienteNome + " (R$ " + valorFinal + ")";
    }
}
