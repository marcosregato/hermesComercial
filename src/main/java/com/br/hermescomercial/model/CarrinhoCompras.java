package com.br.hermescomercial.model;


import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class CarrinhoCompras {
    
    private Long id;
    private List<ItemVenda> itens;
    private BigDecimal valorTotal;
    private BigDecimal descontoTotal;
    private BigDecimal valorFinal;
    private LocalDateTime dataAbertura;
    private LocalDateTime dataFechamento;
    private String status; // ABERTO, FECHADO, CANCELADO
    private Cliente cliente;
    private Usuario operador;
    private String observacao;

    public CarrinhoCompras() {
        this.itens = new ArrayList<>();
        this.valorTotal = BigDecimal.ZERO;
        this.descontoTotal = BigDecimal.ZERO;
        this.valorFinal = BigDecimal.ZERO;
        this.dataAbertura = LocalDateTime.now();
        this.status = "ABERTO";
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public List<ItemVenda> getItens() {
        return itens;
    }

    public void setItens(List<ItemVenda> itens) {
        this.itens = itens != null ? itens : new ArrayList<>();
        calcularValores();
    }

    public BigDecimal getValorTotal() {
        return valorTotal;
    }

    public void setValorTotal(BigDecimal valorTotal) {
        this.valorTotal = valorTotal != null ? valorTotal : BigDecimal.ZERO;
    }

    public BigDecimal getDescontoTotal() {
        return descontoTotal;
    }

    public void setDescontoTotal(BigDecimal descontoTotal) {
        this.descontoTotal = descontoTotal != null ? descontoTotal : BigDecimal.ZERO;
        calcularValores();
    }

    public BigDecimal getValorFinal() {
        return valorFinal;
    }

    public void setValorFinal(BigDecimal valorFinal) {
        this.valorFinal = valorFinal != null ? valorFinal : BigDecimal.ZERO;
    }

    public LocalDateTime getDataAbertura() {
        return dataAbertura;
    }

    public void setDataAbertura(LocalDateTime dataAbertura) {
        this.dataAbertura = dataAbertura;
    }

    public LocalDateTime getDataFechamento() {
        return dataFechamento;
    }

    public void setDataFechamento(LocalDateTime dataFechamento) {
        this.dataFechamento = dataFechamento;
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

    public String getObservacao() {
        return observacao;
    }

    public void setObservacao(String observacao) {
        this.observacao = observacao;
    }

    public void adicionarItem(Produto produto, int quantidade, BigDecimal valorUnitario) {
        if (produto == null || quantidade <= 0 || valorUnitario == null || valorUnitario.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Dados do item inválidos");
        }

        // Verificar se o produto já existe no carrinho
        ItemVenda itemExistente = buscarItemPorProduto(produto);
        
        if (itemExistente != null) {
            // Atualizar quantidade do item existente
            itemExistente.adicionarQuantidade(quantidade);
        } else {
            // Criar novo item
            ItemVenda novoItem = new ItemVenda();
            novoItem.setProduto(produto);
            novoItem.setQuantidade(quantidade);
            novoItem.setValorUnitario(valorUnitario);
            
            itens.add(novoItem);
        }
        
        calcularValores();
    }

    public void removerItem(ItemVenda item) {
        if (item != null && itens.contains(item)) {
            itens.remove(item);
            calcularValores();
        }
    }

    public void removerItemPorProduto(Produto produto) {
        ItemVenda item = buscarItemPorProduto(produto);
        if (item != null) {
            removerItem(item);
        }
    }

    public void atualizarQuantidadeItem(ItemVenda item, int novaQuantidade) {
        if (item != null && itens.contains(item) && novaQuantidade > 0) {
            item.setQuantidade(novaQuantidade);
            calcularValores();
        }
    }

    public void aplicarDescontoItem(ItemVenda item, BigDecimal desconto) {
        if (item != null && itens.contains(item) && desconto != null) {
            item.setDesconto(desconto);
            calcularValores();
        }
    }

    public void limparCarrinho() {
        itens.clear();
        valorTotal = BigDecimal.ZERO;
        descontoTotal = BigDecimal.ZERO;
        valorFinal = BigDecimal.ZERO;
    }

    public int getQuantidadeTotalItens() {
        return itens.stream().mapToInt(ItemVenda::getQuantidade).sum();
    }

    public boolean estaVazio() {
        return itens.isEmpty();
    }

    public boolean estaAberto() {
        return "ABERTO".equals(status);
    }

    public boolean estaFechado() {
        return "FECHADO".equals(status);
    }

    public void fecharCarrinho() {
        this.status = "FECHADO";
        this.dataFechamento = LocalDateTime.now();
    }

    public void cancelarCarrinho() {
        this.status = "CANCELADO";
        this.dataFechamento = LocalDateTime.now();
    }

    private ItemVenda buscarItemPorProduto(Produto produto) {
        return itens.stream()
                .filter(item -> produto.equals(item.getProduto()))
                .findFirst()
                .orElse(null);
    }

    private void calcularValores() {
        this.valorTotal = itens.stream()
                .map(ItemVenda::getValorTotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        
        BigDecimal totalDescontosItens = itens.stream()
                .map(ItemVenda::getDesconto)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        
        this.descontoTotal = totalDescontosItens.add(descontoTotal);
        this.valorFinal = valorTotal.subtract(descontoTotal);
        
        // Garantir que valores não sejam negativos
        this.valorFinal = valorFinal.compareTo(BigDecimal.ZERO) < 0 ? BigDecimal.ZERO : valorFinal;
    }

    // Métodos adicionais para compatibilidade
    public BigDecimal getValorDesconto() {
        return descontoTotal;
    }

    @Override
    public String toString() {
        return "CarrinhoCompras{" +
                "id=" + id +
                ", valorFinal=" + valorFinal +
                ", status='" + status + '\'' +
                ", quantidadeItens=" + (itens != null ? itens.size() : 0) +
                '}';
    }
}
