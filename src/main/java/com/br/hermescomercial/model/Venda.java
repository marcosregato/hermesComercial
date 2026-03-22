/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.br.hermescomercial.model;

/**
 *
 * @author marcos
 */
public class Venda {
    
    private long id;
    private float precoCusto;
    private float custoVenda;
    private Long margemLucro;
    private Long fk_fornecedor;
    private String unidade;
    private int QtdMinima;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public float getPrecoCusto() {
        return precoCusto;
    }

    public void setPrecoCusto(float precoCusto) {
        this.precoCusto = precoCusto;
    }

    public float getCustoVenda() {
        return custoVenda;
    }

    public void setCustoVenda(float custoVenda) {
        this.custoVenda = custoVenda;
    }

    public Long getMargemLucro() {
        return margemLucro;
    }

    public void setMargemLucro(Long margemLucro) {
        this.margemLucro = margemLucro;
    }

    public Long getFk_fornecedor() {
        return fk_fornecedor;
    }

    public void setFk_fornecedor(Long fk_fornecedor) {
        this.fk_fornecedor = fk_fornecedor;
    }

    public String getUnidade() {
        return unidade;
    }

    public void setUnidade(String unidade) {
        this.unidade = unidade;
    }

    public int getQtdMinima() {
        return QtdMinima;
    }

    public void setQtdMinima(int QtdMinima) {
        this.QtdMinima = QtdMinima;
    }
}
