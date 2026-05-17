package com.br.hermescomercial.pdv.model;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;

/**
 * Classe que representa um item de venda
 * @author Hermes Comercial
 * @version 3.2.0
 */
public class ItemVenda {
    
    private String codigo;
    private String codigoProduto;
    private String produto;
    private String descricao;
    private int quantidade;
    private BigDecimal valorUnitario;
    private BigDecimal descontoPercentual;
    private BigDecimal valorTotal;
    private String formaPagamento;
    private String observacoes;
    private LocalDateTime dataInclusao;
    
    public ItemVenda() {
        this.dataInclusao = LocalDateTime.now();
        this.descontoPercentual = BigDecimal.ZERO;
    }
    
    // Getters e Setters
    public String getCodigo() {
        return codigo;
    }
    
    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }
    
    public String getCodigoProduto() {
        return codigoProduto;
    }
    
    public void setCodigoProduto(String codigoProduto) {
        this.codigoProduto = codigoProduto;
    }
    
    public String getProduto() {
        return produto;
    }
    
    public void setProduto(String produto) {
        this.produto = produto;
    }
    
    public String getDescricao() {
        return descricao;
    }
    
    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }
    
    public int getQuantidade() {
        return quantidade;
    }
    
    public void setQuantidade(int quantidade) {
        this.quantidade = quantidade;
    }
    
    public BigDecimal getValorUnitario() {
        return valorUnitario;
    }
    
    public void setValorUnitario(BigDecimal valorUnitario) {
        this.valorUnitario = valorUnitario;
    }
    
    public BigDecimal getDescontoPercentual() {
        return descontoPercentual;
    }
    
    public void setDescontoPercentual(BigDecimal descontoPercentual) {
        this.descontoPercentual = descontoPercentual;
    }
    
    public BigDecimal getValorTotal() {
        return valorTotal;
    }
    
    public void setValorTotal(BigDecimal valorTotal) {
        this.valorTotal = valorTotal;
    }
    
    public String getFormaPagamento() {
        return formaPagamento;
    }
    
    public void setFormaPagamento(String formaPagamento) {
        this.formaPagamento = formaPagamento;
    }
    
    public String getObservacoes() {
        return observacoes;
    }
    
    public void setObservacoes(String observacoes) {
        this.observacoes = observacoes;
    }
    
    public LocalDateTime getDataInclusao() {
        return dataInclusao;
    }
    
    public void setDataInclusao(LocalDateTime dataInclusao) {
        this.dataInclusao = dataInclusao;
    }
    
    /**
     * Calcula o valor total com base na quantidade, valor unitário e desconto
     */
    public void calcularValorTotal() {
        if (valorUnitario != null && quantidade > 0) {
            BigDecimal valorBruto = valorUnitario.multiply(new BigDecimal(quantidade));
            BigDecimal valorDesconto = valorBruto.multiply(
                descontoPercentual.divide(new BigDecimal(100), 2, RoundingMode.HALF_UP));
            this.valorTotal = valorBruto.subtract(valorDesconto);
        }
    }
    
    /**
     * Retorna uma representação em array para adicionar na tabela
     */
    public Object[] toArray() {
        return new Object[]{
            codigo,
            codigoProduto,
            produto,
            descricao,
            quantidade,
            "R$ " + formatarValor(valorUnitario),
            descontoPercentual + "%",
            "R$ " + formatarValor(valorTotal),
            formaPagamento,
            observacoes
        };
    }
    
    // Métodos para compatibilidade com código existente
    public Long getId() {
        return codigo != null ? Long.valueOf(codigo) : null;
    }
    
    public void setId(Long id) {
        this.codigo = id != null ? id.toString() : null;
    }
    
    public String getIdVenda() {
        return codigo;
    }
    
    public void setIdVenda(String idVenda) {
        this.codigo = idVenda;
    }
    
    public String getIdProduto() {
        return codigoProduto;
    }
    
    public void setIdProduto(String idProduto) {
        this.codigoProduto = idProduto;
    }
    
    public String getNome() {
        return produto;
    }
    
    public void setNome(String nome) {
        this.produto = nome;
    }
    
    public java.math.BigDecimal getDesconto() {
        return descontoPercentual;
    }
    
    public void setDesconto(java.math.BigDecimal desconto) {
        this.descontoPercentual = desconto;
    }
    
    public java.math.BigDecimal getValorFinal() {
        return valorTotal;
    }
    
    public void setValorFinal(java.math.BigDecimal valorFinal) {
        this.valorTotal = valorFinal;
    }
    
    public String getObservacao() {
        return observacoes;
    }
    
    public void setObservacao(String observacao) {
        this.observacoes = observacao;
    }
    
    public java.time.LocalDateTime getDataCadastro() {
        return dataInclusao;
    }
    
    public void setDataCadastro(java.time.LocalDateTime dataCadastro) {
        this.dataInclusao = dataCadastro;
    }
    
    /**
     * Formata valor monetário
     */
    private String formatarValor(BigDecimal valor) {
        if (valor == null) return "R$ 0,00";
        return valor.setScale(2, RoundingMode.HALF_UP).toString().replace(".", ",");
    }
    
    /**
     * Retorna uma representação em string do item
     */
    @Override
    public String toString() {
        return String.format("Item[%s] %s - %d x R$%.2f = R$%.2f", 
            codigo, produto, quantidade, valorUnitario, valorTotal);
    }
}
