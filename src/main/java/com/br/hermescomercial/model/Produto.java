/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.br.hermescomercial.model;


import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.sql.Timestamp;

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
    
    // Campos de estoque adicionais
    private int estoqueMinimo;
    private int estoqueMaximo;
    private String localizacaoEstoque;
    private String lote;
    private LocalDate dataValidade;
    
    // Campos adicionais para compatibilidade com Design Patterns
    private String descricao;
    private BigDecimal precoCusto;
    private boolean ativo;
    private LocalDateTime dataCriacao;
    private LocalDateTime dataAtualizacao;
    
    
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

    // Métodos adicionais para compatibilidade
    public BigDecimal getPreco() {
        return precoVenda != null ? precoVenda : BigDecimal.ZERO;
    }

    public int getQuantidadeEstoque() {
        return estoque;
    }

    public String getAcoes() {
        return "Ações"; // Placeholder para coluna de ações
    }
    
    // Getters e Setters dos novos campos de estoque
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
    
    // Métodos de utilidade para estoque
    public boolean precisaReposicao() {
        return estoque <= estoqueMinimo;
    }
    
    public boolean estoqueExcedido() {
        return estoque >= estoqueMaximo;
    }
    
    public boolean proximoDaValidade(int dias) {
        if (dataValidade == null) return false;
        return dataValidade.isBefore(LocalDate.now().plusDays(dias));
    }
    
    public String getStatusEstoque() {
        if (estoque <= estoqueMinimo) return "CRÍTICO";
        if (estoque <= estoqueMinimo * 1.5) return "BAIXO";
        if (estoque >= estoqueMaximo) return "EXCESSO";
        return "NORMAL";
    }
    
    // Getters e Setters para campos adicionais
    public String getDescricao() {
        return descricao;
    }
    
    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }
    
    public BigDecimal getPrecoCusto() {
        return precoCusto;
    }
    
    public void setPrecoCusto(BigDecimal precoCusto) {
        this.precoCusto = precoCusto;
    }
    
    public boolean isAtivo() {
        return ativo;
    }
    
    public void setAtivo(boolean ativo) {
        this.ativo = ativo;
    }
    
    public LocalDateTime getDataCriacao() {
        return dataCriacao;
    }
    
    public void setDataCriacao(LocalDateTime dataCriacao) {
        this.dataCriacao = dataCriacao;
    }
    
    public LocalDateTime getDataAtualizacao() {
        return dataAtualizacao;
    }
    
    public void setDataAtualizacao(LocalDateTime dataAtualizacao) {
        this.dataAtualizacao = dataAtualizacao;
    }
    
    // Métodos de compatibilidade com Timestamp
    public Timestamp getDataCriacaoTimestamp() {
        return dataCriacao != null ? Timestamp.valueOf(dataCriacao) : null;
    }
    
    public void setDataCriacaoTimestamp(Timestamp timestamp) {
        this.dataCriacao = timestamp != null ? timestamp.toLocalDateTime() : null;
    }
    
    public Timestamp getDataAtualizacaoTimestamp() {
        return dataAtualizacao != null ? Timestamp.valueOf(dataAtualizacao) : null;
    }
    
    public void setDataAtualizacaoTimestamp(Timestamp timestamp) {
        this.dataAtualizacao = timestamp != null ? timestamp.toLocalDateTime() : null;
    }
}
