package com.br.hermescomercial.model;

import java.math.BigDecimal;

public class NotaFiscalTransporte {
    
    private Long id;
    private String modalidadeFrete; // 0=Por conta do emitente, 1=Por conta do destinatário, 2=Por conta de terceiros, 9=Sem frete
    private String nomeTransportadora;
    private String cnpjTransportadora;
    private String inscricaoEstadualTransportadora;
    private String enderecoTransportadora;
    private String municipioTransportadora;
    private String ufTransportadora;
    private String cepTransportadora;
    private String telefoneTransportadora;
    
    // Veículo
    private String placaVeiculo;
    private String ufPlaca;
    private String rntc; // Registro Nacional de Transportador de Carga
    
    // Volumes
    private Integer quantidadeVolumes;
    private String especieVolumes; // Caixas, pacotes, etc.
    private String marcaVolumes;
    private String numeracaoVolumes;
    private BigDecimal pesoLiquido;
    private BigDecimal pesoBruto;
    
    // Valores
    private BigDecimal valorFrete;
    private BigDecimal valorSeguro;
    private BigDecimal valorDespesasAcessorias;
    private BigDecimal valorTotalFrete;
    
    // Relacionamentos
    private NotaFiscal notaFiscal;
    
    public NotaFiscalTransporte() {
        this.modalidadeFrete = "9"; // Sem frete por padrão para NFC-e
        this.valorFrete = BigDecimal.ZERO;
        this.valorSeguro = BigDecimal.ZERO;
        this.valorDespesasAcessorias = BigDecimal.ZERO;
        this.valorTotalFrete = BigDecimal.ZERO;
        this.pesoLiquido = BigDecimal.ZERO;
        this.pesoBruto = BigDecimal.ZERO;
        this.quantidadeVolumes = 0;
    }
    
    // Métodos de negócio
    public void calcularTotalFrete() {
        valorTotalFrete = valorFrete
            .add(valorSeguro != null ? valorSeguro : BigDecimal.ZERO)
            .add(valorDespesasAcessorias != null ? valorDespesasAcessorias : BigDecimal.ZERO);
    }
    
    public boolean temFrete() {
        return !"9".equals(modalidadeFrete) && 
               (valorFrete != null && valorFrete.compareTo(BigDecimal.ZERO) > 0);
    }
    
    public String getModalidadeFreteDescricao() {
        switch (modalidadeFrete) {
            case "0": return "Por conta do emitente";
            case "1": return "Por conta do destinatário";
            case "2": return "Por conta de terceiros";
            case "9": return "Sem frete";
            default: return "Não definido";
        }
    }
    
    // Getters e Setters
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public String getModalidadeFrete() {
        return modalidadeFrete;
    }
    
    public void setModalidadeFrete(String modalidadeFrete) {
        this.modalidadeFrete = modalidadeFrete;
    }
    
    public String getNomeTransportadora() {
        return nomeTransportadora;
    }
    
    public void setNomeTransportadora(String nomeTransportadora) {
        this.nomeTransportadora = nomeTransportadora;
    }
    
    public String getCnpjTransportadora() {
        return cnpjTransportadora;
    }
    
    public void setCnpjTransportadora(String cnpjTransportadora) {
        this.cnpjTransportadora = cnpjTransportadora;
    }
    
    public String getInscricaoEstadualTransportadora() {
        return inscricaoEstadualTransportadora;
    }
    
    public void setInscricaoEstadualTransportadora(String inscricaoEstadualTransportadora) {
        this.inscricaoEstadualTransportadora = inscricaoEstadualTransportadora;
    }
    
    public String getEnderecoTransportadora() {
        return enderecoTransportadora;
    }
    
    public void setEnderecoTransportadora(String enderecoTransportadora) {
        this.enderecoTransportadora = enderecoTransportadora;
    }
    
    public String getMunicipioTransportadora() {
        return municipioTransportadora;
    }
    
    public void setMunicipioTransportadora(String municipioTransportadora) {
        this.municipioTransportadora = municipioTransportadora;
    }
    
    public String getUfTransportadora() {
        return ufTransportadora;
    }
    
    public void setUfTransportadora(String ufTransportadora) {
        this.ufTransportadora = ufTransportadora;
    }
    
    public String getCepTransportadora() {
        return cepTransportadora;
    }
    
    public void setCepTransportadora(String cepTransportadora) {
        this.cepTransportadora = cepTransportadora;
    }
    
    public String getTelefoneTransportadora() {
        return telefoneTransportadora;
    }
    
    public void setTelefoneTransportadora(String telefoneTransportadora) {
        this.telefoneTransportadora = telefoneTransportadora;
    }
    
    public String getPlacaVeiculo() {
        return placaVeiculo;
    }
    
    public void setPlacaVeiculo(String placaVeiculo) {
        this.placaVeiculo = placaVeiculo;
    }
    
    public String getUfPlaca() {
        return ufPlaca;
    }
    
    public void setUfPlaca(String ufPlaca) {
        this.ufPlaca = ufPlaca;
    }
    
    public String getRntc() {
        return rntc;
    }
    
    public void setRntc(String rntc) {
        this.rntc = rntc;
    }
    
    public Integer getQuantidadeVolumes() {
        return quantidadeVolumes;
    }
    
    public void setQuantidadeVolumes(Integer quantidadeVolumes) {
        this.quantidadeVolumes = quantidadeVolumes;
    }
    
    public String getEspecieVolumes() {
        return especieVolumes;
    }
    
    public void setEspecieVolumes(String especieVolumes) {
        this.especieVolumes = especieVolumes;
    }
    
    public String getMarcaVolumes() {
        return marcaVolumes;
    }
    
    public void setMarcaVolumes(String marcaVolumes) {
        this.marcaVolumes = marcaVolumes;
    }
    
    public String getNumeracaoVolumes() {
        return numeracaoVolumes;
    }
    
    public void setNumeracaoVolumes(String numeracaoVolumes) {
        this.numeracaoVolumes = numeracaoVolumes;
    }
    
    public BigDecimal getPesoLiquido() {
        return pesoLiquido;
    }
    
    public void setPesoLiquido(BigDecimal pesoLiquido) {
        this.pesoLiquido = pesoLiquido;
    }
    
    public BigDecimal getPesoBruto() {
        return pesoBruto;
    }
    
    public void setPesoBruto(BigDecimal pesoBruto) {
        this.pesoBruto = pesoBruto;
    }
    
    public BigDecimal getValorFrete() {
        return valorFrete;
    }
    
    public void setValorFrete(BigDecimal valorFrete) {
        this.valorFrete = valorFrete;
    }
    
    public BigDecimal getValorSeguro() {
        return valorSeguro;
    }
    
    public void setValorSeguro(BigDecimal valorSeguro) {
        this.valorSeguro = valorSeguro;
    }
    
    public BigDecimal getValorDespesasAcessorias() {
        return valorDespesasAcessorias;
    }
    
    public void setValorDespesasAcessorias(BigDecimal valorDespesasAcessorias) {
        this.valorDespesasAcessorias = valorDespesasAcessorias;
    }
    
    public BigDecimal getValorTotalFrete() {
        return valorTotalFrete;
    }
    
    public void setValorTotalFrete(BigDecimal valorTotalFrete) {
        this.valorTotalFrete = valorTotalFrete;
    }
    
    public NotaFiscal getNotaFiscal() {
        return notaFiscal;
    }
    
    public void setNotaFiscal(NotaFiscal notaFiscal) {
        this.notaFiscal = notaFiscal;
    }
    
    @Override
    public String toString() {
        return "NotaFiscalTransporte{" +
                "modalidadeFrete='" + modalidadeFrete + '\'' +
                ", nomeTransportadora='" + nomeTransportadora + '\'' +
                ", valorTotalFrete=" + valorTotalFrete +
                '}';
    }
}
