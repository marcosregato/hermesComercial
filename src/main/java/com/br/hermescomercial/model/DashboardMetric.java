package com.br.hermescomercial.model;

import java.awt.Color;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;

/**
 * Modelo de métricas para dashboard analítico
 * Versão 2.3.0 - Dashboard com KPIs e gráficos
 */
public class DashboardMetric {
    
    private Long id;
    private String nome;
    private BigDecimal valor;
    private String unidade;
    private LocalDate dataReferencia;
    private TipoMetrica tipo;
    private String categoria;
    private BigDecimal valorAnterior;
    private BigDecimal meta;
    private String descricao;
    
    public enum TipoMetrica {
        VENDAS_DIARIAS("Vendas Diárias"),
        VENDAS_MENSAIS("Vendas Mensais"),
        LUCRATIVIDADE("Lucratividade"),
        TOTAL_CLIENTES("Total de Clientes"),
        PRODUTOS_VENDIDOS("Produtos Vendidos"),
        TICKET_MEDIO("Ticket Médio"),
        ESTOQUE_BAIXO("Estoque Baixo"),
        META_VENDAS("Meta de Vendas"),
        TAXA_CONVERSAO("Taxa de Conversão"),
        FATURAMENTO("Faturamento"),
        CUSTOS("Custos"),
        MARGEM_LUCRO("Margem de Lucro");
        
        private final String descricao;
        
        TipoMetrica(String descricao) {
            this.descricao = descricao;
        }
        
        public String getDescricao() {
            return descricao;
        }
    }
    
    // Construtores
    public DashboardMetric() {
        this.valor = BigDecimal.ZERO;
        this.valorAnterior = BigDecimal.ZERO;
        this.meta = BigDecimal.ZERO;
        this.dataReferencia = LocalDate.now();
    }
    
    public DashboardMetric(String nome, BigDecimal valor, TipoMetrica tipo) {
        this();
        this.nome = nome;
        this.valor = valor;
        this.tipo = tipo;
    }
    
    public DashboardMetric(String nome, BigDecimal valor, String unidade, TipoMetrica tipo) {
        this(nome, valor, tipo);
        this.unidade = unidade;
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
    
    public BigDecimal getValor() {
        return valor;
    }
    
    public void setValor(BigDecimal valor) {
        this.valor = valor;
    }
    
    public String getUnidade() {
        return unidade;
    }
    
    public void setUnidade(String unidade) {
        this.unidade = unidade;
    }
    
    public LocalDate getDataReferencia() {
        return dataReferencia;
    }
    
    public void setDataReferencia(LocalDate dataReferencia) {
        this.dataReferencia = dataReferencia;
    }
    
    public TipoMetrica getTipo() {
        return tipo;
    }
    
    public void setTipo(TipoMetrica tipo) {
        this.tipo = tipo;
    }
    
    public String getCategoria() {
        return categoria;
    }
    
    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }
    
    public BigDecimal getValorAnterior() {
        return valorAnterior;
    }
    
    public void setValorAnterior(BigDecimal valorAnterior) {
        this.valorAnterior = valorAnterior;
    }
    
    public BigDecimal getMeta() {
        return meta;
    }
    
    public void setMeta(BigDecimal meta) {
        this.meta = meta;
    }
    
    public String getDescricao() {
        return descricao;
    }
    
    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }
    
    // Métodos utilitários
    public BigDecimal getVariacaoPercentual() {
        if (valorAnterior == null || valorAnterior.compareTo(BigDecimal.ZERO) == 0) {
            return BigDecimal.ZERO;
        }
        
        BigDecimal variacao = valor.subtract(valorAnterior);
        return variacao.divide(valorAnterior, 4, RoundingMode.HALF_UP)
                       .multiply(new BigDecimal("100"));
    }
    
    public boolean isMetaAtingida() {
        if (meta == null || meta.compareTo(BigDecimal.ZERO) == 0) {
            return false;
        }
        return valor.compareTo(meta) >= 0;
    }
    
    public String getVariacaoFormatada() {
        BigDecimal variacao = getVariacaoPercentual();
        String sinal = variacao.compareTo(BigDecimal.ZERO) >= 0 ? "+" : "";
        return sinal + variacao.setScale(2, RoundingMode.HALF_UP) + "%";
    }
    
    public boolean isPositiva() {
        return getVariacaoPercentual().compareTo(BigDecimal.ZERO) >= 0;
    }
    
    public String getValorFormatado() {
        if (valor == null) {
            return "0";
        }
        
        // Formatação baseada no tipo de métrica
        switch (tipo) {
            case VENDAS_DIARIAS:
            case VENDAS_MENSAIS:
            case FATURAMENTO:
            case CUSTOS:
            case TICKET_MEDIO:
                return "R$ " + valor.setScale(2, RoundingMode.HALF_UP);
                
            case LUCRATIVIDADE:
            case TAXA_CONVERSAO:
            case MARGEM_LUCRO:
                return valor.setScale(2, RoundingMode.HALF_UP) + "%";
                
            case TOTAL_CLIENTES:
            case PRODUTOS_VENDIDOS:
            case ESTOQUE_BAIXO:
                return valor.setScale(0, RoundingMode.HALF_UP).toString();
                
            default:
                return valor.toString();
        }
    }
    
    public StatusMetrica getStatus() {
        if (meta == null || meta.compareTo(BigDecimal.ZERO) == 0) {
            return StatusMetrica.SEM_META;
        }
        
        BigDecimal percentualMeta = valor.divide(meta, 4, RoundingMode.HALF_UP)
                                    .multiply(new BigDecimal("100"));
        
        if (percentualMeta.compareTo(new BigDecimal("100")) >= 0) {
            return StatusMetrica.SUPERADA;
        } else if (percentualMeta.compareTo(new BigDecimal("80")) >= 0) {
            return StatusMetrica.ATENDIDA;
        } else if (percentualMeta.compareTo(new BigDecimal("50")) >= 0) {
            return StatusMetrica.EM_ANDAMENTO;
        } else {
            return StatusMetrica.BAIXA;
        }
    }
    
    public enum StatusMetrica {
        SUPERADA("Superada", new Color(0, 200, 83)),
        ATENDIDA("Atendida", new Color(52, 152, 219)),
        EM_ANDAMENTO("Em Andamento", new Color(255, 193, 7)),
        BAIXA("Baixa", new Color(220, 53, 69)),
        SEM_META("Sem Meta", new Color(108, 117, 125));
        
        private final String descricao;
        private final Color cor;
        
        StatusMetrica(String descricao, Color cor) {
            this.descricao = descricao;
            this.cor = cor;
        }
        
        public String getDescricao() {
            return descricao;
        }
        
        public Color getCor() {
            return cor;
        }
    }
    
    @Override
    public String toString() {
        return "DashboardMetric{" +
                "id=" + id +
                ", nome='" + nome + '\'' +
                ", valor=" + valor +
                ", unidade='" + unidade + '\'' +
                ", dataReferencia=" + dataReferencia +
                ", tipo=" + tipo +
                ", categoria='" + categoria + '\'' +
                '}';
    }
}
