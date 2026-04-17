package com.br.hermescomercial.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

public class CarrinhoComprasTestSimplificado {

    private CarrinhoCompras carrinho;
    private Produto produto;

    @BeforeEach
    void setUp() {
        carrinho = new CarrinhoCompras();
        
        produto = new Produto();
        produto.setId(1L);
        produto.setNome("Produto Teste");
        produto.setPrecoVenda(new BigDecimal("10.50"));
    }

    @Test
    @DisplayName("Deve criar carrinho vazio")
    void testCriarCarrinhoVazio() {
        assertNotNull(carrinho);
        assertTrue(carrinho.getItens().isEmpty());
        assertEquals(BigDecimal.ZERO, carrinho.getValorTotal());
        assertEquals("ABERTO", carrinho.getStatus());
        assertNotNull(carrinho.getDataAbertura());
    }

    @Test
    @DisplayName("Deve validar getters e setters")
    void testGettersSetters() {
        carrinho.setId(1L);
        carrinho.setObservacao("Observação teste");
        carrinho.setStatus("FECHADO");
        
        assertEquals(1L, carrinho.getId());
        assertEquals("Observação teste", carrinho.getObservacao());
        assertEquals("FECHADO", carrinho.getStatus());
    }

    @Test
    @DisplayName("Deve validar valores monetários")
    void testValoresMonetarios() {
        carrinho.setValorTotal(new BigDecimal("100.00"));
        carrinho.setDescontoTotal(new BigDecimal("10.00"));
        carrinho.setValorFinal(new BigDecimal("90.00"));
        
        assertEquals(new BigDecimal("100.00"), carrinho.getValorTotal());
        assertEquals(new BigDecimal("10.00"), carrinho.getDescontoTotal());
        assertEquals(new BigDecimal("90.00"), carrinho.getValorFinal());
    }

    @Test
    @DisplayName("Deve validar valor do desconto")
    void testValorDesconto() {
        carrinho.setDescontoTotal(new BigDecimal("5.00"));
        assertEquals(new BigDecimal("5.00"), carrinho.getValorDesconto());
    }

    @Test
    @DisplayName("Deve validar datas")
    void testDatas() {
        LocalDateTime agora = LocalDateTime.now();
        carrinho.setDataFechamento(agora);
        
        assertEquals(agora, carrinho.getDataFechamento());
        assertNotNull(carrinho.getDataAbertura());
    }

    @Test
    @DisplayName("Deve adicionar item à lista")
    void testAdicionarItem() {
        ItemVenda item = new ItemVenda();
        item.setId(1L);
        item.setProduto(produto);
        item.setQuantidade(2);
        item.setValorUnitario(new BigDecimal("10.50"));
        item.setValorTotal(new BigDecimal("21.00"));
        
        carrinho.getItens().add(item);
        
        assertEquals(1, carrinho.getItens().size());
        assertEquals(item, carrinho.getItens().get(0));
    }

    @Test
    @DisplayName("Deve limpar itens")
    void testLimparItens() {
        ItemVenda item = new ItemVenda();
        item.setId(1L);
        item.setProduto(produto);
        
        carrinho.getItens().add(item);
        assertEquals(1, carrinho.getItens().size());
        
        carrinho.getItens().clear();
        assertTrue(carrinho.getItens().isEmpty());
    }

    @Test
    @DisplayName("Deve validar cliente e operador")
    void testClienteOperador() {
        Cliente cliente = new Cliente();
        cliente.setId(1L);
        cliente.setNome("Cliente Teste");
        
        Usuario operador = new Usuario();
        operador.setId(1L);
        operador.setNome("Operador Teste");
        
        carrinho.setCliente(cliente);
        carrinho.setOperador(operador);
        
        assertEquals(cliente, carrinho.getCliente());
        assertEquals(operador, carrinho.getOperador());
    }
}
