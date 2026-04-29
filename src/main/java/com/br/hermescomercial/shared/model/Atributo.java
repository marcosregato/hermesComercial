package com.br.hermescomercial.shared.model;


public class Atributo {
    private Long id;
    private String nome;
    private String descricao;
    private String tipo;
    private Double valor;
    private float impostoFederal;
    private float impostoEstadual;
    private float impostoMunicipal;

    public Atributo() {
    }

    public Atributo(Long id, String nome, String descricao, String tipo, Double valor) {
        this.id = id;
        this.nome = nome;
        this.descricao = descricao;
        this.tipo = tipo;
        this.valor = valor;
    }

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

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public Double getValor() {
        return valor;
    }

    public void setValor(Double valor) {
        this.valor = valor;
    }

    public float getImpostoFederal() {
        return impostoFederal;
    }

    public void setImpostoFederal(float impostoFederal) {
        this.impostoFederal = impostoFederal;
    }

    public float getImpostoEstadual() {
        return impostoEstadual;
    }

    public void setImpostoEstadual(float impostoEstadual) {
        this.impostoEstadual = impostoEstadual;
    }

    public float getImpostoMunicipal() {
        return impostoMunicipal;
    }

    public void setImpostoMunicipal(float impostoMunicipal) {
        this.impostoMunicipal = impostoMunicipal;
    }

    @Override
    public String toString() {
        return "Atributo{" +
                "id=" + id +
                ", nome='" + nome + '\'' +
                ", descricao='" + descricao + '\'' +
                ", tipo='" + tipo + '\'' +
                ", valor=" + valor +
                '}';
    }
}
