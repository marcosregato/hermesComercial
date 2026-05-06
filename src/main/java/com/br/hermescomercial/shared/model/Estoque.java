package com.br.hermescomercial.shared.model;


public class Estoque {
    private Long id;
    private Long idProduto;
    private Integer quantidade;
    private Integer estoqueMinimo;
    private Double valorUnitario;
    private Double valorTotal;
    private String status;

    public Estoque() {
    }

    public Estoque(Long id, Long idProduto, Integer quantidade, Integer estoqueMinimo, Double valorUnitario, Double valorTotal, String status) {
        this.id = id;
        this.idProduto = idProduto;
        this.quantidade = quantidade;
        this.estoqueMinimo = estoqueMinimo;
        this.valorUnitario = valorUnitario;
        this.valorTotal = valorTotal;
        this.status = status;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getIdProduto() {
        return idProduto;
    }

    public void setIdProduto(Long idProduto) {
        this.idProduto = idProduto;
    }

    public Integer getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(Integer quantidade) {
        this.quantidade = quantidade;
    }

    public Integer getEstoqueMinimo() {
        return estoqueMinimo;
    }

    public void setEstoqueMinimo(Integer estoqueMinimo) {
        this.estoqueMinimo = estoqueMinimo;
    }

    public Double getValorUnitario() {
        return valorUnitario;
    }

    public void setValorUnitario(Double valorUnitario) {
        this.valorUnitario = valorUnitario;
    }

    public Double getValorTotal() {
        return valorTotal;
    }

    public void setValorTotal(Double valorTotal) {
        this.valorTotal = valorTotal;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "Estoque{" +
                "id=" + id +
                ", idProduto=" + idProduto +
                ", quantidade=" + quantidade +
                ", estoqueMinimo=" + estoqueMinimo +
                ", valorUnitario=" + valorUnitario +
                ", valorTotal=" + valorTotal +
                ", status='" + status + '\'' +
                '}';
    }
}
