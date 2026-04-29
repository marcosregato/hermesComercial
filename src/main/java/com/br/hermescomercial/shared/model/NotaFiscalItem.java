package com.br.hermescomercial.shared.model;

import com.br.hermescomercial.pdv.model.ItemVenda;
import java.math.BigDecimal;

public class NotaFiscalItem {
    
    private Long id;
    private Integer numeroItem;
    private String codigoProduto;
    private String descricao;
    private String ncm; // Nomenclatura Comum do Mercosul
    private String cfop; // Código Fiscal de Operações e Prestações
    private String unidade; // UN, KG, LT, etc.
    private BigDecimal quantidade;
    private BigDecimal valorUnitario;
    private BigDecimal valorBruto;
    private BigDecimal valorDesconto;
    private BigDecimal valorTotal;
    private BigDecimal baseCalculoIcms;
    private BigDecimal valorIcms;
    private BigDecimal baseCalculoIcmsSt;
    private BigDecimal valorIcmsSt;
    private BigDecimal valorIpi;
    private BigDecimal valorPis;
    private BigDecimal valorCofins;
    private String aliquotaIcms;
    private String aliquotaIpi;
    private String aliquotaPis;
    private String aliquotaCofins;
    private String cstIcms; // Código de Situação Tributária do ICMS
    private String cstIpi; // Código de Situação Tributária do IPI
    private String cstPis; // Código de Situação Tributária do PIS
    private String cstCofins; // Código de Situação Tributária do COFINS
    private String informacoesAdicionais;
    
    // Relacionamentos
    private NotaFiscal notaFiscal;
    private Produto produto;
    
    public NotaFiscalItem() {
        this.quantidade = BigDecimal.ZERO;
        this.valorUnitario = BigDecimal.ZERO;
        this.valorBruto = BigDecimal.ZERO;
        this.valorDesconto = BigDecimal.ZERO;
        this.valorTotal = BigDecimal.ZERO;
        this.baseCalculoIcms = BigDecimal.ZERO;
        this.valorIcms = BigDecimal.ZERO;
        this.baseCalculoIcmsSt = BigDecimal.ZERO;
        this.valorIcmsSt = BigDecimal.ZERO;
        this.valorIpi = BigDecimal.ZERO;
        this.valorPis = BigDecimal.ZERO;
        this.valorCofins = BigDecimal.ZERO;
        this.unidade = "UN";
        this.cstIcms = "00"; // Tributada integralmente
        this.cstIpi = "00"; // Tributada integralmente
        this.cstPis = "01"; // Operação Tributável - Alíquota Básica
        this.cstCofins = "01"; // Operação Tributável - Alíquota Básica
    }
    
    // Métodos de negócio
    public void calcularTotais() {
        // Calcula valor bruto
        valorBruto = quantidade.multiply(valorUnitario);
        
        // Calcula valor total (bruto - desconto)
        valorTotal = valorBruto.subtract(valorDesconto != null ? valorDesconto : BigDecimal.ZERO);
        
        // Calcula base de cálculo do ICMS (geralmente igual ao valor total)
        baseCalculoIcms = valorTotal;
        
        // Calcula valor do ICMS
        if (aliquotaIcms != null && !aliquotaIcms.isEmpty()) {
            BigDecimal aliquota = new BigDecimal(aliquotaIcms.replace("%", "").replace(",", "."));
            valorIcms = baseCalculoIcms.multiply(aliquota).divide(new BigDecimal("100"));
        }
        
        // Calcula valor do IPI
        if (aliquotaIpi != null && !aliquotaIpi.isEmpty()) {
            BigDecimal aliquota = new BigDecimal(aliquotaIpi.replace("%", "").replace(",", "."));
            valorIpi = valorBruto.multiply(aliquota).divide(new BigDecimal("100"));
        }
        
        // Calcula valor do PIS
        if (aliquotaPis != null && !aliquotaPis.isEmpty()) {
            BigDecimal aliquota = new BigDecimal(aliquotaPis.replace("%", "").replace(",", "."));
            valorPis = valorTotal.multiply(aliquota).divide(new BigDecimal("100"));
        }
        
        // Calcula valor do COFINS
        if (aliquotaCofins != null && !aliquotaCofins.isEmpty()) {
            BigDecimal aliquota = new BigDecimal(aliquotaCofins.replace("%", "").replace(",", "."));
            valorCofins = valorTotal.multiply(aliquota).divide(new BigDecimal("100"));
        }
    }
    
    public void preencherComItemVenda(ItemVenda itemVenda) {
        if (itemVenda != null) {
            this.codigoProduto = itemVenda.getProduto() != null ? itemVenda.getProduto().getId().toString() : "";
            this.quantidade = new BigDecimal(itemVenda.getQuantidade());
            this.valorUnitario = itemVenda.getValorUnitario();
            this.descricao = itemVenda.getProduto() != null ? itemVenda.getProduto().getNome() : "";
            this.unidade = itemVenda.getProduto() != null ? itemVenda.getProduto().getUnidade() : "UN";
            this.valorDesconto = itemVenda.getDesconto() != null ? itemVenda.getDesconto() : BigDecimal.ZERO;
            this.produto = itemVenda.getProduto();
            
            // Valores padrão para NF-e
            this.ncm = "00000000"; // Preencher com NCM correto
            this.cfop = "5102"; // Venda de mercadoria adquirida ou recebida de terceiros
            this.aliquotaIcms = "18.00"; // Alíquota padrão de SP
            this.aliquotaIpi = "0.00"; // IPI isento para produtos revendidos
            this.aliquotaPis = "0.65"; // Alíquota PIS
            this.aliquotaCofins = "3.00"; // Alíquota COFINS
            
            calcularTotais();
        }
    }
    
    public String getValorFormatado() {
        if (valorTotal == null) return "R$ 0,00";
        return String.format("R$ %,.2f", valorTotal).replace('.', ',');
    }
    
    public String getQuantidadeFormatada() {
        if (quantidade == null) return "0";
        return quantidade.toString();
    }
    
    public String getValorUnitarioFormatado() {
        if (valorUnitario == null) return "R$ 0,00";
        return String.format("R$ %,.2f", valorUnitario).replace('.', ',');
    }
    
    public String getAcoes() {
        return "Ações";
    }
    
    // Getters e Setters
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public Integer getNumeroItem() {
        return numeroItem;
    }
    
    public void setNumeroItem(Integer numeroItem) {
        this.numeroItem = numeroItem;
    }
    
    public String getCodigoProduto() {
        return codigoProduto;
    }
    
    public void setCodigoProduto(String codigoProduto) {
        this.codigoProduto = codigoProduto;
    }
    
    public String getDescricao() {
        return descricao;
    }
    
    public void setDescricao(String descricao) {
        this.descricao = descricao;
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
    
    public String getUnidade() {
        return unidade;
    }
    
    public void setUnidade(String unidade) {
        this.unidade = unidade;
    }
    
    public BigDecimal getQuantidade() {
        return quantidade;
    }
    
    public void setQuantidade(BigDecimal quantidade) {
        this.quantidade = quantidade;
    }
    
    public BigDecimal getValorUnitario() {
        return valorUnitario;
    }
    
    public void setValorUnitario(BigDecimal valorUnitario) {
        this.valorUnitario = valorUnitario;
    }
    
    public BigDecimal getValorBruto() {
        return valorBruto;
    }
    
    public void setValorBruto(BigDecimal valorBruto) {
        this.valorBruto = valorBruto;
    }
    
    public BigDecimal getValorDesconto() {
        return valorDesconto;
    }
    
    public void setValorDesconto(BigDecimal valorDesconto) {
        this.valorDesconto = valorDesconto;
    }
    
    public BigDecimal getValorTotal() {
        return valorTotal;
    }
    
    public void setValorTotal(BigDecimal valorTotal) {
        this.valorTotal = valorTotal;
    }
    
    public BigDecimal getBaseCalculoIcms() {
        return baseCalculoIcms;
    }
    
    public void setBaseCalculoIcms(BigDecimal baseCalculoIcms) {
        this.baseCalculoIcms = baseCalculoIcms;
    }
    
    public BigDecimal getValorIcms() {
        return valorIcms;
    }
    
    public void setValorIcms(BigDecimal valorIcms) {
        this.valorIcms = valorIcms;
    }
    
    public BigDecimal getBaseCalculoIcmsSt() {
        return baseCalculoIcmsSt;
    }
    
    public void setBaseCalculoIcmsSt(BigDecimal baseCalculoIcmsSt) {
        this.baseCalculoIcmsSt = baseCalculoIcmsSt;
    }
    
    public BigDecimal getValorIcmsSt() {
        return valorIcmsSt;
    }
    
    public void setValorIcmsSt(BigDecimal valorIcmsSt) {
        this.valorIcmsSt = valorIcmsSt;
    }
    
    public BigDecimal getValorIpi() {
        return valorIpi;
    }
    
    public void setValorIpi(BigDecimal valorIpi) {
        this.valorIpi = valorIpi;
    }
    
    public BigDecimal getValorPis() {
        return valorPis;
    }
    
    public void setValorPis(BigDecimal valorPis) {
        this.valorPis = valorPis;
    }
    
    public BigDecimal getValorCofins() {
        return valorCofins;
    }
    
    public void setValorCofins(BigDecimal valorCofins) {
        this.valorCofins = valorCofins;
    }
    
    public String getAliquotaIcms() {
        return aliquotaIcms;
    }
    
    public void setAliquotaIcms(String aliquotaIcms) {
        this.aliquotaIcms = aliquotaIcms;
    }
    
    public String getAliquotaIpi() {
        return aliquotaIpi;
    }
    
    public void setAliquotaIpi(String aliquotaIpi) {
        this.aliquotaIpi = aliquotaIpi;
    }
    
    public String getAliquotaPis() {
        return aliquotaPis;
    }
    
    public void setAliquotaPis(String aliquotaPis) {
        this.aliquotaPis = aliquotaPis;
    }
    
    public String getAliquotaCofins() {
        return aliquotaCofins;
    }
    
    public void setAliquotaCofins(String aliquotaCofins) {
        this.aliquotaCofins = aliquotaCofins;
    }
    
    public String getCstIcms() {
        return cstIcms;
    }
    
    public void setCstIcms(String cstIcms) {
        this.cstIcms = cstIcms;
    }
    
    public String getCstIpi() {
        return cstIpi;
    }
    
    public void setCstIpi(String cstIpi) {
        this.cstIpi = cstIpi;
    }
    
    public String getCstPis() {
        return cstPis;
    }
    
    public void setCstPis(String cstPis) {
        this.cstPis = cstPis;
    }
    
    public String getCstCofins() {
        return cstCofins;
    }
    
    public void setCstCofins(String cstCofins) {
        this.cstCofins = cstCofins;
    }
    
    public String getInformacoesAdicionais() {
        return informacoesAdicionais;
    }
    
    public void setInformacoesAdicionais(String informacoesAdicionais) {
        this.informacoesAdicionais = informacoesAdicionais;
    }
    
    public NotaFiscal getNotaFiscal() {
        return notaFiscal;
    }
    
    public void setNotaFiscal(NotaFiscal notaFiscal) {
        this.notaFiscal = notaFiscal;
    }
    
    public Produto getProduto() {
        return produto;
    }
    
    public void setProduto(Produto produto) {
        this.produto = produto;
    }
    
    @Override
    public String toString() {
        return "NotaFiscalItem{" +
                "numeroItem=" + numeroItem +
                ", descricao='" + descricao + '\'' +
                ", quantidade=" + quantidade +
                ", valorUnitario=" + valorUnitario +
                ", valorTotal=" + valorTotal +
                '}';
    }
}
