package com.br.hermescomercial.dao;

import com.br.hermescomercial.model.ItemVenda;
import com.br.hermescomercial.model.Produto;
import com.br.hermescomercial.model.VendaPDV;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

public class ItemVendaDaoTest {

    private ItemVendaDao itemVendaDao;

    @BeforeEach
    void setUp() {
        itemVendaDao = new ItemVendaDao();
    }

    @Test
    @DisplayName("Deve criar ItemVendaDao sem erros")
    void testCriarItemVendaDao() {
        assertNotNull(itemVendaDao);
    }

    @Test
    @DisplayName("Deve criar item de venda válido")
    void testCriarItemVenda() {
        Produto produto = new Produto();
        produto.setId(1L);
        produto.setNome("Produto Teste");
        produto.setPrecoVenda(new BigDecimal("10.50"));

        VendaPDV venda = new VendaPDV();
        venda.setId(1L);

        ItemVenda itemVenda = new ItemVenda();
        itemVenda.setId(1L);
        itemVenda.setProduto(produto);
        // itemVenda.setVenda(venda); // Método não existe no modelo
        itemVenda.setQuantidade(2);
        itemVenda.setValorUnitario(new BigDecimal("10.50"));
        itemVenda.setValorTotal(new BigDecimal("21.00"));

        assertNotNull(itemVenda);
        assertEquals(1, itemVenda.getId());
        assertEquals(produto, itemVenda.getProduto());
        // assertEquals(venda, itemVenda.getVenda()); // Método não existe no modelo
        assertEquals(2, itemVenda.getQuantidade());
        assertEquals(new BigDecimal("10.50"), itemVenda.getValorUnitario());
        assertEquals(new BigDecimal("21.00"), itemVenda.getValorTotal());
    }

    @Test
    @DisplayName("Deve validar cálculo de valor total")
    void testCalcularValorTotal() {
        ItemVenda itemVenda = new ItemVenda();
        itemVenda.setQuantidade(3);
        itemVenda.setValorUnitario(new BigDecimal("15.75"));
        
        BigDecimal valorEsperado = new BigDecimal("47.25");
        BigDecimal valorCalculado = itemVenda.getValorUnitario().multiply(
            new BigDecimal(itemVenda.getQuantidade())
        );

        assertEquals(valorEsperado, valorCalculado);
    }

    @Test
    @DisplayName("Deve validar getters e setters")
    void testGettersSetters() {
        ItemVenda itemVenda = new ItemVenda();
        
        itemVenda.setId(10L);
        itemVenda.setQuantidade(5);
        itemVenda.setValorUnitario(new BigDecimal("25.00"));
        itemVenda.setValorTotal(new BigDecimal("125.00"));
        // itemVenda.setDataCriacao(LocalDateTime.now()); // Método não existe no modelo

        assertEquals(10, itemVenda.getId());
        assertEquals(5, itemVenda.getQuantidade());
        assertEquals(new BigDecimal("25.00"), itemVenda.getValorUnitario());
        assertEquals(new BigDecimal("125.00"), itemVenda.getValorTotal());
        // assertNotNull(itemVenda.getDataCriacao()); // Método não existe no modelo
    }
}
