/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.br.hermescomercial.shared.model;


import java.math.BigDecimal;
import java.time.LocalDate;

/**
 *
 * @author marcos
 */
public class Produto  {

	private Long id;
	private String nome;
    private String categoria;
    private String subCategoria;
    private String codigo;
    private String marca;
    private String dataCompra;
    private String codigoBarras;
    private String unidade;
    private BigDecimal precoVenda;
    private int estoque;
    private int estoqueMinimo;
    private int estoqueMaximo;
    private String localizacaoEstoque;
    private String lote;
    private LocalDate dataValidade;
    private String ncm; // Nomenclatura Comum do Mercosul
    private String cfop; // Código Fiscal de Operações e Prestações
    private MetodoControleEstoque metodoControleEstoque = MetodoControleEstoque.CUSTO_MEDIO; // Método padrão
    
    
	public String getNome() {
		return nome;
	}
	public void setNome(String nome) {
		this.nome = nome;
	}
	public String getCategoria() {
		return categoria;
	}
	public void setCategoria(String categoria) {
		this.categoria = categoria;
	}
	public String getSubCategoria() {
		return subCategoria;
	}
	public void setSubCategoria(String subCategoria) {
		this.subCategoria = subCategoria;
	}
	public String getCodigo() {
		return codigo;
	}
	public void setCodigo(String codigo) {
		this.codigo = codigo;
	}
	public String getMarca() {
		return marca;
	}
	public void setMarca(String marca) {
		this.marca = marca;
	}
	public String getDataCompra() {
		return dataCompra;
	}
	public void setDataCompra(String dataCompra) {
		this.dataCompra = dataCompra;
	}

    // Métodos adicionais para compatibilidade
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCodigoBarras() {
        return codigoBarras;
    }

    public void setCodigoBarras(String codigoBarras) {
        this.codigoBarras = codigoBarras;
    }

    public String getUnidade() {
        return unidade;
    }

    public void setUnidade(String unidade) {
        this.unidade = unidade;
    }

    public BigDecimal getPrecoVenda() {
        return precoVenda;
    }

    public void setPrecoVenda(BigDecimal precoVenda) {
        this.precoVenda = precoVenda;
    }

    public int getEstoque() {
        return estoque;
    }

    public void setEstoque(int estoque) {
        this.estoque = estoque;
    }

    public int getEstoqueMinimo() {
        return estoqueMinimo;
    }

    public void setEstoqueMinimo(int estoqueMinimo) {
        this.estoqueMinimo = estoqueMinimo;
    }

    public int getEstoqueMaximo() {
        return estoqueMaximo;
    }

    public void setEstoqueMaximo(int estoqueMaximo) {
        this.estoqueMaximo = estoqueMaximo;
    }

    public String getLocalizacaoEstoque() {
        return localizacaoEstoque;
    }

    public void setLocalizacaoEstoque(String localizacaoEstoque) {
        this.localizacaoEstoque = localizacaoEstoque;
    }

    public String getLote() {
        return lote;
    }

    public void setLote(String lote) {
        this.lote = lote;
    }

    public LocalDate getDataValidade() {
        return dataValidade;
    }

    public void setDataValidade(LocalDate dataValidade) {
        this.dataValidade = dataValidade;
    }

    public String getNcm() {
        return ncm;
    }

    public void setNcm(String ncm) {
        this.ncm = ncm;
    }

    public String getCfop() {
        return cfop;
    }

    public void setCfop(String cfop) {
        this.cfop = cfop;
    }

    public MetodoControleEstoque getMetodoControleEstoque() {
        return metodoControleEstoque;
    }

    public void setMetodoControleEstoque(MetodoControleEstoque metodoControleEstoque) {
        this.metodoControleEstoque = metodoControleEstoque != null ? metodoControleEstoque : MetodoControleEstoque.CUSTO_MEDIO;
    }

    // Métodos adicionais para compatibilidade
    public BigDecimal getPreco() {
        return precoVenda;
    }

    public int getQuantidadeEstoque() {
        return estoque;
    }

    public String getAcoes() {
        return "Ações"; // Placeholder para coluna de ações
    }
}
