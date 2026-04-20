package com.br.hermescomercial.integration;

import com.br.hermescomercial.business.pdv.PDVManager;
// import com.br.hermescomercial.business.pdv.PagamentoManager; - não utilizado
// import com.br.hermescomercial.business.pdv.CupomFiscalManager; - não utilizado
import com.br.hermescomercial.model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import java.math.BigDecimal;
// import java.time.LocalDateTime; - não utilizado
// import java.util.List; - não utilizado

import static org.junit.jupiter.api.Assertions.*;

class SimpleIntegrationTest {

    private PDVManager pdvManager;
    // private PagamentoManager pagamentoManager; - não utilizado
    // private CupomFiscalManager cupomManager; - não utilizado
    private Usuario operador;
    // private Cliente cliente; - não utilizado
    private Produto produto;

    @BeforeEach
    void setUp() {
        pdvManager = PDVManager.getInstance();
        // pagamentoManager = PagamentoManager.getInstance(); - não utilizado
        // cupomManager = CupomFiscalManager.getInstance(); - não utilizado
        operador = criarOperador();
        // cliente = criarCliente(); - não utilizado
        produto = criarProduto();
    }

    @Test
    @DisplayName("Teste simples de integração")
    void testIntegracaoSimples() {
        // Setup
        assertTrue(pdvManager.abrirCaixa());
        assertTrue(pdvManager.iniciarSessaoPDV(operador));

        // Adicionar produto
        assertTrue(pdvManager.adicionarProduto(produto, 1));
        assertNotNull(pdvManager.getCarrinhoAtual());

        // Criar venda
        VendaPDV venda = pdvManager.criarVendaPDV();
        assertNotNull(venda);

        // Finalizar sessão
        assertTrue(pdvManager.finalizarSessaoPDV());
        assertNull(pdvManager.getSessaoAtual());
    }

    // Métodos auxiliares
    private Usuario criarOperador() {
        Usuario operador = new Usuario();
        operador.setId(1L);
        operador.setNome("Operador Teste");
        return operador;
    }

    private Produto criarProduto() {
        return criarProduto("Produto Teste", new BigDecimal("10.50"));
    }

    private Produto criarProduto(String nome, BigDecimal preco) {
        Produto produto = new Produto();
        produto.setId(System.currentTimeMillis());
        produto.setNome(nome);
        produto.setPrecoVenda(preco);
        produto.setEstoque(100);
        produto.setUnidade("UN");
        return produto;
    }
}
