package com.br.hermescomercial.pdv.model;

/**
 * Classe de modelo para representar um produto de estoque
 * Extraída de PDVFormularioConsultarEstoque para melhor organização
 */
public class ProdutoEstoque {
    private String codigo;
    private String produto;
    private String categoria;
    private String fornecedor;
    private String quantidade;
    private String preco;
    private String status;
    private String localizacao;
    
    // Getters e Setters
    public String getCodigo() { return codigo; }
    public void setCodigo(String codigo) { this.codigo = codigo; }
    
    public String getProduto() { return produto; }
    public void setProduto(String produto) { this.produto = produto; }
    
    public String getCategoria() { return categoria; }
    public void setCategoria(String categoria) { this.categoria = categoria; }
    
    public String getFornecedor() { return fornecedor; }
    public void setFornecedor(String fornecedor) { this.fornecedor = fornecedor; }
    
    public String getQuantidade() { return quantidade; }
    public void setQuantidade(String quantidade) { this.quantidade = quantidade; }
    
    public String getPreco() { return preco; }
    public void setPreco(String preco) { this.preco = preco; }
    
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    
    public String getLocalizacao() { return localizacao; }
    public void setLocalizacao(String localizacao) { this.localizacao = localizacao; }
}
