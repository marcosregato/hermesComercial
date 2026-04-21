package com.br.hermescomercial.service;

import com.br.hermescomercial.model.Produto;
import com.br.hermescomercial.dao.ProdutoDao;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.util.List;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Classe de teste unitário para ProdutoService
 */
class ProdutoServiceTest {

    @Mock
    private ProdutoDao produtoDao;
    
    private ProdutoService produtoService;
    
    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        produtoService = new ProdutoService(produtoDao);
    }
    
    @Test
    @DisplayName("Deve salvar produto com sucesso")
    void testSalvarProdutoSucesso() throws Exception {
        // Arrange
        Produto produto = new Produto();
        produto.setNome("Produto Teste");
        produto.setPrecoVenda(new BigDecimal("100.00"));
        produto.setEstoque(50);
        produto.setCodigo("PROD001");
        
        when(produtoDao.salvar(any(Produto.class))).thenReturn(true);
        
        // Act
        boolean resultado = produtoService.salvar(produto);
        
        // Assert
        assertTrue(resultado);
        verify(produtoDao, times(1)).salvar(produto);
    }
    
    @Test
    @DisplayName("Deve lançar exceção ao salvar produto nulo")
    void testSalvarProdutoNulo() {
        // Act & Assert
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            produtoService.salvar(null);
        });
        
        assertEquals("Produto não pode ser nulo", exception.getMessage());
    }
    
    @Test
    @DisplayName("Deve listar todos produtos com sucesso")
    void testListarTodosProdutosSucesso() throws Exception {
        // Arrange
        List<Produto> produtosEsperados = new ArrayList<>();
        when(produtoDao.listar()).thenReturn(produtosEsperados);
        
        // Act
        List<Produto> resultado = produtoService.listar();
        
        // Assert
        assertNotNull(resultado);
        verify(produtoDao, times(1)).listar();
    }
    
    @Test
    @DisplayName("Deve buscar produtos com filtros com sucesso")
    void testBuscarProdutosComFiltrosSucesso() throws Exception {
        // Arrange
        List<Produto> produtosEsperados = new ArrayList<>();
        Produto produto1 = new Produto();
        produto1.setNome("Produto 1");
        Produto produto2 = new Produto();
        produto2.setNome("Produto 2");
        produtosEsperados.add(produto1);
        produtosEsperados.add(produto2);
        
        when(produtoDao.buscarComFiltros(anyString(), anyString(), any(), any(), any(), any(), anyInt(), anyBoolean(), anyBoolean())).thenReturn(produtosEsperados);
        
        // Act
        List<Produto> resultado = produtoService.buscarComFiltros("teste", "categoria", null, "001", null, null, 0, true, false);
        
        // Assert
        assertNotNull(resultado);
        assertEquals(2, resultado.size());
        verify(produtoDao, times(1)).buscarComFiltros(anyString(), anyString(), any(), any(), any(), any(), anyInt(), anyBoolean(), anyBoolean());
    }
    
    @Test
    @DisplayName("Deve buscar produto por código de barras com sucesso")
    void testBuscarProdutoPorCodigoBarrasSucesso() throws Exception {
        // Arrange
        Produto produtoEsperado = new Produto();
        produtoEsperado.setCodigoBarras("123456789");
        
        when(produtoDao.buscarPorCodigoBarras("123456789")).thenReturn(produtoEsperado);
        
        // Act
        Produto resultado = produtoService.buscarPorCodigoBarras("123456789");
        
        // Assert
        assertNotNull(resultado);
        assertEquals("123456789", resultado.getCodigoBarras());
        verify(produtoDao, times(1)).buscarPorCodigoBarras("123456789");
    }
    
    }
