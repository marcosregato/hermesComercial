package com.br.hermescomercial.pdv.service;

import com.br.hermescomercial.pdv.model.ProdutoEstoque;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.swing.table.DefaultTableModel;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Testes unitários para ProdutoEstoqueService
 */
public class ProdutoEstoqueServiceTest {
    
    private ProdutoEstoqueService estoqueService;
    private DefaultTableModel modelTabela;
    
    @BeforeEach
    public void setUp() {
        estoqueService = new ProdutoEstoqueService();
        modelTabela = new DefaultTableModel(
            new Object[]{"Código", "Produto", "Categoria", "Fornecedor", "Quantidade", "Preço", "Status", "Localização"},
            0
        );
    }
    
    @Test
    public void testAdicionarDadosExemplo() {
        List<ProdutoEstoque> produtos = estoqueService.adicionarDadosExemplo(modelTabela);
        
        assertNotNull(produtos);
        assertEquals(7, produtos.size());
        assertEquals(7, modelTabela.getRowCount());
        
        ProdutoEstoque primeiroProduto = produtos.get(0);
        assertEquals("001", primeiroProduto.getCodigo());
        assertEquals("Arroz Integral 5kg", primeiroProduto.getProduto());
    }
    
    @Test
    public void testBuscarProdutosRapidaComTermo() {
        List<ProdutoEstoque> produtos = estoqueService.adicionarDadosExemplo(modelTabela);
        
        List<ProdutoEstoque> resultado = estoqueService.buscarProdutosRapida("Arroz", produtos);
        
        assertNotNull(resultado);
        assertFalse(resultado.isEmpty());
        assertEquals("Arroz Integral 5kg", resultado.get(0).getProduto());
    }
    
    @Test
    public void testBuscarProdutosRapidaComTermoVazio() {
        List<ProdutoEstoque> produtos = estoqueService.adicionarDadosExemplo(modelTabela);
        
        List<ProdutoEstoque> resultado = estoqueService.buscarProdutosRapida("", produtos);
        
        assertNotNull(resultado);
        assertEquals(produtos.size(), resultado.size());
    }
    
    @Test
    public void testBuscarProdutosRapidaComTermoInexistente() {
        List<ProdutoEstoque> produtos = estoqueService.adicionarDadosExemplo(modelTabela);
        
        List<ProdutoEstoque> resultado = estoqueService.buscarProdutosRapida("Inexistente", produtos);
        
        assertNotNull(resultado);
        assertTrue(resultado.isEmpty());
    }
}
