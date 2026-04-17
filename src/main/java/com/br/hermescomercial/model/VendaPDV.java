package com.br.hermescomercial.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public class VendaPDV {
    
    private Long id;
    private String numeroCupom;
    private LocalDateTime dataVenda;
    private BigDecimal valorTotal;
    private BigDecimal valorDesconto;
    private BigDecimal valorFinal;
    private String status; // ABERTA, CONCLUIDA, CANCELADA
    private Cliente cliente;
    private Usuario operador;
    private List<ItemVenda> itens;
    private Pagamento pagamento;
    private String numeroTerminal;
    private int terminal;
    private LocalDateTime dataCancelamento;
    private String motivoCancelamento;
    private String observacao;
    private String observacoes;
    private BigDecimal valorDespesa;

    public VendaPDV() {
        this.dataVenda = LocalDateTime.now();
        this.status = "ABERTA";
        this.valorDesconto = BigDecimal.ZERO;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNumeroCupom() {
        return numeroCupom;
    }

    public void setNumeroCupom(String numeroCupom) {
        this.numeroCupom = numeroCupom;
    }

    public LocalDateTime getDataVenda() {
        return dataVenda;
    }

    public void setDataVenda(LocalDateTime dataVenda) {
        this.dataVenda = dataVenda;
    }

    public BigDecimal getValorTotal() {
        return valorTotal;
    }

    public void setValorTotal(BigDecimal valorTotal) {
        this.valorTotal = valorTotal;
    }

    public BigDecimal getValorDesconto() {
        return valorDesconto;
    }

    public void setValorDesconto(BigDecimal valorDesconto) {
        this.valorDesconto = valorDesconto != null ? valorDesconto : BigDecimal.ZERO;
    }

    public BigDecimal getValorFinal() {
        return valorFinal;
    }

    public void setValorFinal(BigDecimal valorFinal) {
        this.valorFinal = valorFinal;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    public Usuario getOperador() {
        return operador;
    }

    public void setOperador(Usuario operador) {
        this.operador = operador;
    }

    public List<ItemVenda> getItens() {
        return itens;
    }

    public void setItens(List<ItemVenda> itens) {
        this.itens = itens;
    }

    public Pagamento getPagamento() {
        return pagamento;
    }

    public void setPagamento(Pagamento pagamento) {
        this.pagamento = pagamento;
    }

    public String getNumeroTerminal() {
        return numeroTerminal;
    }

    public void setNumeroTerminal(String numeroTerminal) {
        this.numeroTerminal = numeroTerminal;
    }

    public LocalDateTime getDataCancelamento() {
        return dataCancelamento;
    }

    public void setDataCancelamento(LocalDateTime dataCancelamento) {
        this.dataCancelamento = dataCancelamento;
    }

    public String getMotivoCancelamento() {
        return motivoCancelamento;
    }

    public void setMotivoCancelamento(String motivoCancelamento) {
        this.motivoCancelamento = motivoCancelamento;
    }

    public String getObservacao() {
        return observacao;
    }

    public void setObservacao(String observacao) {
        this.observacao = observacao;
    }

    public boolean estaAberta() {
        return "ABERTA".equals(status);
    }

    public boolean estaConcluida() {
        return "CONCLUIDA".equals(status);
    }

    public boolean estaCancelada() {
        return "CANCELADA".equals(status);
    }

    public void concluir() {
        this.status = "CONCLUIDA";
    }

    public void cancelar(String motivo) {
        this.status = "CANCELADA";
        this.dataCancelamento = LocalDateTime.now();
        this.motivoCancelamento = motivo;
    }

    public int getQuantidadeTotalItens() {
        if (itens == null) return 0;
        return itens.stream().mapToInt(ItemVenda::getQuantidade).sum();
    }

    // Métodos adicionais para compatibilidade
    public int getTerminal() {
        return terminal;
    }

    public void setTerminal(int terminal) {
        this.terminal = terminal;
    }

    public BigDecimal getValorDespesa() {
        return valorDespesa;
    }

    public void setValorDespesa(BigDecimal valorDespesa) {
        this.valorDespesa = valorDespesa;
    }

    public String getObservacoes() {
        return observacoes;
    }

    public void setObservacoes(String observacoes) {
        this.observacoes = observacoes;
    }

    @Override
    public String toString() {
        return "VendaPDV{" +
                "id=" + id +
                ", numeroCupom='" + numeroCupom + '\'' +
                ", valorFinal=" + valorFinal +
                ", status='" + status + '\'' +
                '}';
    }
}
