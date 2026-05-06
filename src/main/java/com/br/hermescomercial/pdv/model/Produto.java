package com.br.hermescomercial.pdv.model;

import java.math.BigDecimal;

/**
 * Classe que representa um Produto no sistema PDV
 */
public class Produto {
    private Long id;
    private String nome;
    private String descricao;
    private String codigoBarras;
    private BigDecimal precoVenda;
    private BigDecimal precoCusto;
    private int estoqueAtual;
    private int estoqueMinimo;
    private String unidadeMedida;
    private int idCategoria;
    private String categoriaNome;
    private int idFornecedor;
    private String fornecedorNome;
    private boolean ativo;
    
    // Construtores
    public Produto() {}
    
    public Produto(String nome, String descricao, BigDecimal precoVenda) {
        this.nome = nome;
        this.descricao = descricao;
        this.precoVenda = precoVenda;
        this.unidadeMedida = "UN";
        this.estoqueAtual = 0;
        this.estoqueMinimo = 0;
        this.ativo = true;
    }
    
    // Getters e Setters
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public String getNome() {
        return nome;
    }
    
    public void setNome(String nome) {
        this.nome = nome;
    }
    
    public String getDescricao() {
        return descricao;
    }
    
    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }
    
    public String getCodigoBarras() {
        return codigoBarras;
    }
    
    public void setCodigoBarras(String codigoBarras) {
        this.codigoBarras = codigoBarras;
    }
    
    public BigDecimal getPrecoVenda() {
        return precoVenda;
    }
    
    public void setPrecoVenda(BigDecimal precoVenda) {
        this.precoVenda = precoVenda;
    }
    
    public BigDecimal getPrecoCusto() {
        return precoCusto;
    }
    
    public void setPrecoCusto(BigDecimal precoCusto) {
        this.precoCusto = precoCusto;
    }
    
    public int getEstoqueAtual() {
        return estoqueAtual;
    }
    
    public void setEstoqueAtual(int estoqueAtual) {
        this.estoqueAtual = estoqueAtual;
    }
    
    public int getEstoqueMinimo() {
        return estoqueMinimo;
    }
    
    public void setEstoqueMinimo(int estoqueMinimo) {
        this.estoqueMinimo = estoqueMinimo;
    }
        
    public String getUnidadeMedida() {
        return unidadeMedida;
    }
    
    public void setUnidadeMedida(String unidadeMedida) {
        this.unidadeMedida = unidadeMedida;
    }
    
    public int getIdCategoria() {
        return idCategoria;
    }
    
    public void setIdCategoria(int idCategoria) {
        this.idCategoria = idCategoria;
    }
    
    public String getCategoriaNome() {
        return categoriaNome;
    }
    
    public void setCategoriaNome(String categoriaNome) {
        this.categoriaNome = categoriaNome;
    }
    
    public int getIdFornecedor() {
        return idFornecedor;
    }
    
    public void setIdFornecedor(int idFornecedor) {
        this.idFornecedor = idFornecedor;
    }
    
    public String getFornecedorNome() {
        return fornecedorNome;
    }
    
    public void setFornecedorNome(String fornecedorNome) {
        this.fornecedorNome = fornecedorNome;
    }
    
    public boolean isAtivo() {
        return ativo;
    }
    
    public void setAtivo(boolean ativo) {
        this.ativo = ativo;
    }
    
    // Métodos utilitários
    public String getStatusEstoque() {
        if (estoqueAtual <= 0) {
            return "SEM ESTOQUE";
        } else if (estoqueAtual <= estoqueMinimo) {
            return "CRÍTICO";
        } else if (estoqueAtual <= (estoqueMinimo * 2)) {
            return "BAIXO";
        } else {
            return "NORMAL";
        }
    }
    
    public BigDecimal getMargemLucro() {
        if (precoCusto != null && precoCusto.compareTo(BigDecimal.ZERO) > 0) {
            return precoVenda.subtract(precoCusto)
                    .divide(precoCusto, 2, BigDecimal.ROUND_HALF_UP)
                    .multiply(new BigDecimal("100"));
        }
        return BigDecimal.ZERO;
    }
    
    @Override
    public String toString() {
        return nome + " (R$ " + precoVenda + ")";
    }
}
