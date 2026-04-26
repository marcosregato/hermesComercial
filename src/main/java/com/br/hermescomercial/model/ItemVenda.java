package com.br.hermescomercial.model;


import java.math.BigDecimal;
import java.time.LocalDateTime;

public class ItemVenda {
    
    private Long id;
    private Produto produto;
    private int quantidade;
    private BigDecimal valorUnitario;
    private BigDecimal valorTotal;
    private BigDecimal desconto;
    private BigDecimal valorFinal;
    private LocalDateTime dataCadastro;
    private String observacao;

    public ItemVenda() {
        this.dataCadastro = LocalDateTime.now();
        this.desconto = BigDecimal.ZERO;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Produto getProduto() {
        return produto;
    }

    public void setProduto(Produto produto) {
        this.produto = produto;
    }

    public int getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(int quantidade) {
        this.quantidade = quantidade;
        calcularValores();
    }

    public BigDecimal getValorUnitario() {
        return valorUnitario;
    }

    public void setValorUnitario(BigDecimal valorUnitario) {
        this.valorUnitario = valorUnitario;
        calcularValores();
    }

    public BigDecimal getValorTotal() {
        return valorTotal;
    }

    public void setValorTotal(BigDecimal valorTotal) {
        this.valorTotal = valorTotal;
    }

    public BigDecimal getDesconto() {
        return desconto;
    }

    public void setDesconto(BigDecimal desconto) {
        this.desconto = desconto != null ? desconto : BigDecimal.ZERO;
        calcularValores();
    }

    public BigDecimal getValorFinal() {
        return valorFinal;
    }

    public void setValorFinal(BigDecimal valorFinal) {
        this.valorFinal = valorFinal;
    }

    public LocalDateTime getDataCadastro() {
        return dataCadastro;
    }

    public void setDataCadastro(LocalDateTime dataCadastro) {
        this.dataCadastro = dataCadastro;
    }

    public String getObservacao() {
        return observacao;
    }

    public void setObservacao(String observacao) {
        this.observacao = observacao;
    }

    // Métodos otimizados para PropertyValueFactory - melhor performance
    public String getProdutoId() {
        return produto != null ? String.valueOf(produto.getId()) : "";
    }
    
    public String getProdutoNome() {
        return produto != null ? produto.getNome() : "";
    }
    
    public String getAcoes() {
        return "Remover";
    }

    private void calcularValores() {
        if (valorUnitario != null && quantidade > 0) {
            this.valorTotal = valorUnitario.multiply(BigDecimal.valueOf(quantidade));
            this.valorFinal = valorTotal.subtract(desconto);
        }
    }

    public void adicionarQuantidade(int quantidade) {
        this.quantidade += quantidade;
        calcularValores();
    }

    public void removerQuantidade(int quantidade) {
        if (this.quantidade >= quantidade) {
            this.quantidade -= quantidade;
            calcularValores();
        }
    }

    // Métodos adicionais para compatibilidade
    public Long getIdVenda() {
        return id; // Simplificado - na prática viria de VendaPDV
    }

    public void setIdVenda(Long idVenda) {
        this.id = idVenda;
    }

    public Long getIdProduto() {
        return produto != null ? produto.getId() : null;
    }

    public void setIdProduto(Long idProduto) {
        // Simplificado - não implementado por enquanto
    }

    public void aplicarDesconto(BigDecimal valorDesconto) {
        if (valorDesconto != null && valorDesconto.compareTo(BigDecimal.ZERO) > 0) {
            this.desconto = valorDesconto;
            calcularValores();
        }
    }

    @Override
    public String toString() {
        return "ItemVenda{" +
                "produto=" + (produto != null ? produto.getNome() : "N/A") +
                ", quantidade=" + quantidade +
                ", valorUnitario=" + valorUnitario +
                ", valorFinal=" + valorFinal +
                '}';
    }
}
