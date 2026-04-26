package com.br.hermescomercial.model;


import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class MovimentacaoCaixa {
    
    private LocalDateTime dataHora;
    private String tipoMovimentacao;
    private String descricao;
    private BigDecimal valor;
    
    private DateTimeFormatter formatadorHora = DateTimeFormatter.ofPattern("HH:mm:ss");
    
    public MovimentacaoCaixa() {
    }
    
    public MovimentacaoCaixa(LocalDateTime dataHora, String tipoMovimentacao, String descricao, BigDecimal valor) {
        this.dataHora = dataHora;
        this.tipoMovimentacao = tipoMovimentacao;
        this.descricao = descricao;
        this.valor = valor;
    }
    
    public String getHoraFormatada() {
        if (dataHora != null) {
            return dataHora.format(formatadorHora);
        }
        return "";
    }
    
    public String getValorFormatado() {
        if (valor != null) {
            String valorStr = String.format("R$ %,.2f", valor.abs()).replace('.', ',');
            if (valor.compareTo(BigDecimal.ZERO) < 0) {
                return "-" + valorStr;
            }
            return valorStr;
        }
        return "R$ 0,00";
    }
    
    // Getters e Setters
    public LocalDateTime getDataHora() {
        return dataHora;
    }
    
    public void setDataHora(LocalDateTime dataHora) {
        this.dataHora = dataHora;
    }
    
    public String getTipoMovimentacao() {
        return tipoMovimentacao;
    }
    
    public void setTipoMovimentacao(String tipoMovimentacao) {
        this.tipoMovimentacao = tipoMovimentacao;
    }
    
    public String getDescricao() {
        return descricao;
    }
    
    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }
    
    public BigDecimal getValor() {
        return valor;
    }
    
    public void setValor(BigDecimal valor) {
        this.valor = valor;
    }
    
    @Override
    public String toString() {
        return "MovimentacaoCaixa{" +
                "dataHora=" + dataHora +
                ", tipoMovimentacao='" + tipoMovimentacao + '\'' +
                ", descricao='" + descricao + '\'' +
                ", valor=" + valor +
                '}';
    }
}
