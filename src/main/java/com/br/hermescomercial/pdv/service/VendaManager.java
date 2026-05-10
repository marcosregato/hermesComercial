package com.br.hermescomercial.pdv.service;

import com.br.hermescomercial.pdv.model.ItemVenda;
import com.br.hermescomercial.util.SystemLogger;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

/**
 * Classe responsável por gerenciar os itens e cálculos da venda
 * @author Hermes Comercial
 * @version 3.2.0
 */
public class VendaManager {
    
    private List<ItemVenda> itens;
    private String cliente;
    private String cpf;
    private String formaPagamento;
    private String observacoes;
    
    public VendaManager() {
        this.itens = new ArrayList<>();
    }
    
    // Getters e Setters
    public List<ItemVenda> getItens() {
        return itens;
    }
    
    public void setItens(List<ItemVenda> itens) {
        this.itens = itens;
    }
    
    public String getCliente() {
        return cliente;
    }
    
    public void setCliente(String cliente) {
        this.cliente = cliente;
    }
    
    public String getCpf() {
        return cpf;
    }
    
    public void setCpf(String cpf) {
        this.cpf = cpf;
    }
    
    public String getFormaPagamento() {
        return formaPagamento;
    }
    
    public void setFormaPagamento(String formaPagamento) {
        this.formaPagamento = formaPagamento;
    }
    
    public String getObservacoes() {
        return observacoes;
    }
    
    public void setObservacoes(String observacoes) {
        this.observacoes = observacoes;
    }
    
    /**
     * Adiciona um item à venda
     */
    public void adicionarItem(ItemVenda item) {
        if (item != null) {
            item.calcularValorTotal();
            itens.add(item);
            SystemLogger.ui("Item adicionado: " + item.toString());
        }
    }
    
    /**
     * Remove um item da venda
     */
    public void removerItem(int index) {
        if (index >= 0 && index < itens.size()) {
            ItemVenda removido = itens.remove(index);
            SystemLogger.ui("Item removido: " + removido.toString());
        }
    }
    
    /**
     * Limpa todos os itens da venda
     */
    public void limparItens() {
        itens.clear();
        SystemLogger.ui("Todos os itens foram removidos");
    }
    
    /**
     * Calcula o subtotal da venda
     */
    public BigDecimal getSubtotal() {
        return itens.stream()
                .map(ItemVenda::getValorTotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
    
    /**
     * Calcula o total de itens
     */
    public int getTotalItens() {
        return itens.stream()
                .mapToInt(ItemVenda::getQuantidade)
                .sum();
    }
    
    /**
     * Calcula o valor total da venda
     */
    public BigDecimal getTotal() {
        return getSubtotal();
    }
    
    /**
     * Calcula o troco baseado na forma de pagamento e valor pago
     */
    public BigDecimal calcularTroco(BigDecimal valorPago) {
        if ("Dinheiro".equals(formaPagamento) && valorPago != null) {
            return valorPago.subtract(getTotal()).max(BigDecimal.ZERO);
        }
        return BigDecimal.ZERO;
    }
    
    /**
     * Verifica se a venda está vazia
     */
    public boolean isVendaVazia() {
        return itens.isEmpty();
    }
    
    /**
     * Retorna o resumo da venda
     */
    public String getResumo() {
        return String.format(
            "Venda: %s itens | Subtotal: R$ %s | Total: R$ %s | Pagamento: %s",
            getTotalItens(),
            formatarValor(getSubtotal()),
            formatarValor(getTotal()),
            formaPagamento != null ? formaPagamento : "Não definido"
        );
    }
    
    /**
     * Formata valor monetário
     */
    private String formatarValor(BigDecimal valor) {
        if (valor == null) return "R$ 0,00";
        return valor.setScale(2, RoundingMode.HALF_UP).toString().replace(".", ",");
    }
}
