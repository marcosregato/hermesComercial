package com.br.hermescomercial.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import static org.junit.jupiter.api.Assertions.*;

import com.br.hermescomercial.model.Produto;
import com.br.hermescomercial.repository.ProdutoRepository;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.util.List;

import static org.mockito.Mockito.*;

/**
 * Testes unitários para ProdutoService
 * Verifica lógica de negócio relacionada a produtos
 */
public class ProdutoServiceTest {
    
    @Mock
    private ProdutoRepository produtoRepository;
    
    private ProdutoService produtoService;
    
    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        produtoService = new ProdutoService(produtoRepository);
    }
    
    @Nested
    @DisplayName("Testes de salvamento de produto")
    class SalvarProdutoTests {
        
        @Test
        @DisplayName("Deve salvar produto com dados válidos")
        void testSalvarProdutoValido() {
            Produto produto = criarProdutoValido();
            when(produtoRepository.salvar(produto)).thenReturn(true);
            
            boolean resultado = produtoService.salvar(produto);
            
            assertTrue(resultado, "Produto deve ser salvo com sucesso");
            verify(produtoRepository).salvar(produto);
        }
        
        @Test
        @DisplayName("Deve falhar ao salvar produto com nome vazio")
        void testSalvarProdutoNomeVazio() {
            Produto produto = criarProdutoValido();
            produto.setNome("");
            
            assertThrows(com.br.hermescomercial.exception.ValidationException.class, () -> {
                produtoService.salvar(produto);
            }, "Deve lançar ValidationException para nome vazio");
        }
        
        @Test
        @DisplayName("Deve falhar ao salvar produto com preço negativo")
        void testSalvarProdutoPrecoNegativo() {
            Produto produto = criarProdutoValido();
            produto.setPrecoVenda(new BigDecimal("-10.00"));
            
            assertThrows(com.br.hermescomercial.exception.ValidationException.class, () -> {
                produtoService.salvar(produto);
            }, "Deve lançar ValidationException para preço negativo");
        }
        
        @Test
        @DisplayName("Deve falhar ao salvar produto com estoque negativo")
        void testSalvarProdutoEstoqueNegativo() {
            Produto produto = criarProdutoValido();
            produto.setEstoque(-5);
            
            assertThrows(com.br.hermescomercial.exception.ValidationException.class, () -> {
                produtoService.salvar(produto);
            }, "Deve lançar ValidationException para estoque negativo");
        }
    }
    
    @Nested
    @DisplayName("Testes de busca de produto")
    class BuscarProdutoTests {
        
        @Test
        @DisplayName("Deve buscar produto por ID")
        void testBuscarPorId() {
            Long id = 1L;
            Produto produto = criarProdutoValido();
            produto.setId(id);
            
            when(produtoRepository.buscarPorId(id)).thenReturn(java.util.Optional.of(produto));
            
            Produto resultado = produtoService.buscarPorId(id);
            
            assertNotNull(resultado, "Produto deve ser encontrado");
            assertEquals(id, resultado.getId(), "ID deve corresponder");
            verify(produtoRepository).buscarPorId(id);
        }
        
        @Test
        @DisplayName("Deve retornar null quando produto não encontrado por ID")
        void testBuscarPorIdNaoEncontrado() {
            Long id = 999L;
            when(produtoRepository.buscarPorId(id)).thenReturn(java.util.Optional.empty());
            
            Produto resultado = produtoService.buscarPorId(id);
            
            assertNull(resultado, "Produto não deve ser encontrado");
            verify(produtoRepository).buscarPorId(id);
        }
        
        @Test
        @DisplayName("Deve buscar produto por código de barras")
        void testBuscarPorCodigoBarras() {
            String codigoBarras = "7891234567890";
            Produto produto = criarProdutoValido();
            produto.setCodigoBarras(codigoBarras);
            
            when(produtoRepository.buscarPorCodigoBarras(codigoBarras)).thenReturn(produto);
            
            Produto resultado = produtoService.buscarPorCodigoBarras(codigoBarras);
            
            assertNotNull(resultado, "Produto deve ser encontrado");
            assertEquals(codigoBarras, resultado.getCodigoBarras(), "Código de barras deve corresponder");
            verify(produtoRepository).buscarPorCodigoBarras(codigoBarras);
        }
        
        @Test
        @DisplayName("Deve retornar null para código de barras vazio")
        void testBuscarPorCodigoBarrasVazio() {
            Produto resultado = produtoService.buscarPorCodigoBarras("");
            
            assertNull(resultado, "Deve retornar null para código vazio");
            verify(produtoRepository, never()).buscarPorCodigoBarras(anyString());
        }
        
        @Test
        @DisplayName("Deve listar todos os produtos")
        void testListarTodos() {
            List<Produto> produtos = List.of(criarProdutoValido(), criarProdutoValido());
            when(produtoRepository.listar()).thenReturn(produtos);
            
            List<Produto> resultado = produtoService.listar();
            
            assertEquals(2, resultado.size(), "Deve retornar 2 produtos");
            verify(produtoRepository).listar();
        }
        
        @Test
        @DisplayName("Deve buscar produtos com filtros")
        void testBuscarComFiltros() {
            String nome = "Notebook";
            String categoria = "Eletrônicos";
            BigDecimal precoMin = new BigDecimal("1000");
            BigDecimal precoMax = new BigDecimal("5000");
            List<Produto> produtos = List.of(criarProdutoValido());
            
            when(produtoRepository.buscarComFiltros(any(), any(), any(), any(), any(), any(), any(), anyBoolean(), anyBoolean()))
                .thenReturn(produtos);
            
            List<Produto> resultado = produtoService.buscarComFiltros(nome, categoria, precoMin, precoMax);
            
            assertEquals(1, resultado.size(), "Deve retornar 1 produto");
            verify(produtoRepository).buscarComFiltros(nome, categoria, null, null, precoMin, precoMax, null, true, false);
        }
    }
    
    @Nested
    @DisplayName("Testes de atualização de produto")
    class AtualizarProdutoTests {
        
        @Test
        @DisplayName("Deve atualizar produto existente")
        void testAtualizarProduto() {
            Produto produto = criarProdutoValido();
            produto.setId(1L);
            when(produtoRepository.atualizar(produto)).thenReturn(true);
            
            boolean resultado = produtoService.atualizar(produto);
            
            assertTrue(resultado, "Produto deve ser atualizado com sucesso");
            verify(produtoRepository).atualizar(produto);
        }
        
        @Test
        @DisplayName("Deve falhar ao atualizar produto sem ID")
        void testAtualizarProdutoSemId() {
            Produto produto = criarProdutoValido();
            
            assertThrows(com.br.hermescomercial.exception.ValidationException.class, () -> {
                produtoService.atualizar(produto);
            }, "Deve lançar ValidationException para produto sem ID");
        }
    }
    
    @Nested
    @DisplayName("Testes de exclusão de produto")
    class ExcluirProdutoTests {
        
        @Test
        @DisplayName("Deve excluir produto por ID")
        void testExcluirPorId() {
            Long id = 1L;
            when(produtoRepository.remover(id)).thenReturn(true);
            
            boolean resultado = produtoService.excluir(id);
            
            assertTrue(resultado, "Produto deve ser excluído com sucesso");
            verify(produtoRepository).remover(id);
        }
        
        @Test
        @DisplayName("Deve falhar ao excluir produto inexistente")
        void testExcluirProdutoInexistente() {
            Long id = 999L;
            when(produtoRepository.remover(id)).thenReturn(false);
            
            boolean resultado = produtoService.excluir(id);
            
            assertFalse(resultado, "Não deve excluir produto inexistente");
            verify(produtoRepository).remover(id);
        }
    }
    
    @Nested
    @DisplayName("Testes de estoque")
    class EstoqueTests {
        
        @Test
        @DisplayName("Deve verificar estoque disponível")
        void testVerificarEstoqueDisponivel() {
            Produto produto = criarProdutoValido();
            produto.setEstoque(50);
            
            boolean resultado = produtoService.verificarEstoque(produto, 30);
            
            assertTrue(resultado, "Estoque deve ser suficiente");
        }
        
        @Test
        @DisplayName("Deve verificar estoque insuficiente")
        void testVerificarEstoqueInsuficiente() {
            Produto produto = criarProdutoValido();
            produto.setEstoque(10);
            
            boolean resultado = produtoService.verificarEstoque(produto, 30);
            
            assertFalse(resultado, "Estoque não deve ser suficiente");
        }
        
        @Test
        @DisplayName("Deve retornar false para produto nulo")
        void testVerificarEstoqueProdutoNulo() {
            boolean resultado = produtoService.verificarEstoque(null, 10);
            
            assertFalse(resultado, "Deve retornar false para produto nulo");
        }
        
        @Test
        @DisplayName("Deve retornar false para quantidade inválida")
        void testVerificarEstoqueQuantidadeInvalida() {
            Produto produto = criarProdutoValido();
            
            boolean resultado = produtoService.verificarEstoque(produto, -5);
            
            assertFalse(resultado, "Deve retornar false para quantidade negativa");
        }
    }
    
    // Métodos auxiliares
    private Produto criarProdutoValido() {
        Produto produto = new Produto();
        produto.setCodigo("PROD-" + System.currentTimeMillis());
        produto.setNome("Produto Teste");
        produto.setPrecoVenda(new BigDecimal("50.00"));
        produto.setEstoque(10);
        produto.setCategoria("Teste");
        produto.setEstoqueMinimo(5);
        return produto;
    }
}
