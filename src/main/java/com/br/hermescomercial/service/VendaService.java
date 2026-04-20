package com.br.hermescomercial.service;

import com.br.hermescomercial.business.pdv.PDVManager;
import com.br.hermescomercial.business.pdv.PagamentoManager;
// import com.br.hermescomercial.business.pdv.CupomFiscalManager; - não utilizado
import com.br.hermescomercial.business.pdv.ImpressoraManager;
import com.br.hermescomercial.model.VendaPDV;
import com.br.hermescomercial.model.ItemVenda;
import com.br.hermescomercial.model.Produto;
import com.br.hermescomercial.model.Usuario;
import com.br.hermescomercial.model.Pagamento;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Camada de serviço para gerenciamento de vendas
 * Centraliza a lógica de negócio relacionada a vendas
 */
public class VendaService {
    
    private final PDVManager pdvManager;
    private final PagamentoManager pagamentoManager;
    // private final CupomFiscalManager cupomManager; - não utilizado
    private final ImpressoraManager impressoraManager;
    
    public VendaService() {
        this.pdvManager = PDVManager.getInstance();
        this.pagamentoManager = PagamentoManager.getInstance();
        // this.cupomManager = CupomFiscalManager.getInstance(); - não utilizado
        this.impressoraManager = ImpressoraManager.getInstance();
    }
    
    /**
     * Inicia uma nova venda
     */
    public VendaPDV iniciarNovaVenda(Usuario operador) {
        VendaPDV venda = new VendaPDV();
        venda.setNumeroCupom("V" + System.currentTimeMillis());
        venda.setDataVenda(LocalDateTime.now());
        venda.setOperador(operador);
        venda.setStatus("EM_ANDAMENTO");
        
        // O carrinho é criado automaticamente ao obter getCarrinhoAtual()
        // Operador é definido através do método criarVendaPDV()
        
        return venda;
    }
    
    /**
     * Adiciona produto ao carrinho
     */
    public boolean adicionarProduto(String codigoProduto, int quantidade) {
        try {
            Produto produto = pdvManager.buscarProdutoPorCodigo(codigoProduto);
            if (produto == null) {
                return false;
            }
            
            if (!verificarEstoqueDisponivel(produto, quantidade)) {
                return false;
            }
            
            return pdvManager.adicionarProduto(produto, quantidade);
            
        } catch (Exception e) {
            return false;
        }
    }
    
    /**
     * Remove item do carrinho
     */
    public boolean removerItem(String codigoProduto) {
        try {
            return pdvManager.removerItem(codigoProduto);
        } catch (Exception e) {
            return false;
        }
    }
    
    /**
     * Aplica desconto em item
     */
    public boolean aplicarDescontoItem(String codigoProduto, BigDecimal desconto) {
        try {
            return pdvManager.aplicarDescontoItem(codigoProduto, desconto);
        } catch (Exception e) {
            return false;
        }
    }
    
    /**
     * Processa pagamento
     */
    public Pagamento processarPagamento(String formaPagamento, BigDecimal valorPago, BigDecimal valorRecebido) {
        try {
            return pagamentoManager.processarPagamentoUnico(formaPagamento, valorPago, valorRecebido);
        } catch (Exception e) {
            return null;
        }
    }
    
    /**
     * Finaliza venda
     */
    public boolean finalizarVenda(Pagamento pagamento) {
        try {
            // Usar carrinho atual em vez de venda direta
            var carrinhoAtual = pdvManager.getCarrinhoAtual();
            if (carrinhoAtual == null) {
                return false;
            }
            
            // Gerar cupom fiscal simplificado
            String cupom = "CUPOM FISCAL\n" +
                          "Data: " + LocalDateTime.now().format(java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")) + "\n" +
                          "Valor: R$ " + carrinhoAtual.getValorFinal() + "\n" +
                          "Forma: " + pagamento.getTipoPagamento() + "\n" +
                          "------------------------";
            
            // Imprimir cupom
            impressoraManager.imprimirCupom(cupom);
            
            // Venda finalizada com sucesso
            
            return true;
            
        } catch (Exception e) {
            return false;
        }
    }
    
    /**
     * Cancela venda
     */
    public boolean cancelarVenda() {
        try {
            return pdvManager.cancelarVenda();
        } catch (Exception e) {
            return false;
        }
    }
    
    /**
     * Obtém resumo da venda atual
     */
    public VendaResumo obterResumoVenda() {
        try {
            var carrinho = pdvManager.getCarrinhoAtual();
            if (carrinho == null) {
                return null;
            }
            
            List<ItemVenda> itens = carrinho.getItens();
            BigDecimal subtotal = BigDecimal.ZERO;
            BigDecimal descontoTotal = BigDecimal.ZERO;
            
            for (ItemVenda item : itens) {
                subtotal = subtotal.add(item.getValorTotal());
                if (item.getDesconto() != null) {
                    descontoTotal = descontoTotal.add(item.getDesconto());
                }
            }
            
            BigDecimal valorTotal = subtotal.subtract(descontoTotal);
            
            return new VendaResumo(
                "V" + System.currentTimeMillis(), // Número cupom simulado
                LocalDateTime.now(), // Data atual
                pdvManager.getOperadorAtual(), // Operador atual
                itens.size(),
                subtotal,
                descontoTotal,
                valorTotal,
                carrinho.estaAberto() ? "EM_ANDAMENTO" : "FINALIZADA"
            );
            
        } catch (Exception e) {
            return null;
        }
    }
    
    /**
     * Verifica se há estoque disponível
     */
    private boolean verificarEstoqueDisponivel(Produto produto, int quantidade) {
        if (produto == null || produto.getEstoque() < quantidade) {
            return false;
        }
        return true;
    }
    
    /**
     * Classe interna para resumo de venda
     */
    public static class VendaResumo {
        private final String numeroCupom;
        private final LocalDateTime dataVenda;
        private final Usuario operador;
        private final int quantidadeItens;
        private final BigDecimal subtotal;
        private final BigDecimal descontoTotal;
        private final BigDecimal valorTotal;
        private final String status;
        
        public VendaResumo(String numeroCupom, LocalDateTime dataVenda, Usuario operador,
                          int quantidadeItens, BigDecimal subtotal, BigDecimal descontoTotal,
                          BigDecimal valorTotal, String status) {
            this.numeroCupom = numeroCupom;
            this.dataVenda = dataVenda;
            this.operador = operador;
            this.quantidadeItens = quantidadeItens;
            this.subtotal = subtotal;
            this.descontoTotal = descontoTotal;
            this.valorTotal = valorTotal;
            this.status = status;
        }
        
        // Getters
        public String getNumeroCupom() { return numeroCupom; }
        public LocalDateTime getDataVenda() { return dataVenda; }
        public Usuario getOperador() { return operador; }
        public int getQuantidadeItens() { return quantidadeItens; }
        public BigDecimal getSubtotal() { return subtotal; }
        public BigDecimal getDescontoTotal() { return descontoTotal; }
        public BigDecimal getValorTotal() { return valorTotal; }
        public String getStatus() { return status; }
    }
}
