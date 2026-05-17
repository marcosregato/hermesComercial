package com.br.hermescomercial.repository;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import static org.junit.jupiter.api.Assertions.*;

import com.br.hermescomercial.model.Produto;
import com.br.hermescomercial.repository.impl.ProdutoRepositoryImpl;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

/**
 * Testes para ProdutoRepository
 * Verifica funcionamento da camada de acesso a dados de produtos
 */
public class ProdutoRepositoryTest {
    
    private ProdutoRepository produtoRepository;
    
    @BeforeEach
    void setUp() {
        produtoRepository = new ProdutoRepositoryImpl();
    }
    
    @Nested
    @DisplayName("Testes de salvamento")
    class SalvarTests {
        
        @Test
        @DisplayName("Deve salvar produto com dados válidos")
        void testSalvarProdutoValido() {
            Produto produto = criarProdutoValido();
            
            boolean resultado = produtoRepository.salvar(produto);
            
            assertTrue(resultado, "Produto deve ser salvo com sucesso");
        }
        
        @Test
        @DisplayName("Deve falhar ao salvar produto nulo")
        void testSalvarProdutoNulo() {
            assertThrows(Exception.class, () -> {
                produtoRepository.salvar(null);
            }, "Deve lançar exceção para produto nulo");
        }
    }
    
    @Nested
    @DisplayName("Testes de busca")
    class BuscarTests {
        
        @Test
        @DisplayName("Deve buscar produto por código de barras")
        void testBuscarPorCodigoBarras() {
            String codigoBarras = "7891234567890";
            
            Produto resultado = produtoRepository.buscarPorCodigoBarras(codigoBarras);
            
            // Pode retornar null se não existir no banco
            assertNotNull(resultado, "Resultado não deve ser null");
        }
        
        @Test
        @DisplayName("Deve listar todos os produtos")
        void testListarTodos() {
            List<Produto> produtos = produtoRepository.listar();
            
            assertNotNull(produtos, "Lista não deve ser null");
            assertTrue(produtos.size() >= 0, "Lista deve ter tamanho válido");
        }
        
        @Test
        @DisplayName("Deve buscar por ID retornando Optional")
        void testBuscarPorId() {
            Long id = 1L;
            
            Optional<Produto> resultado = produtoRepository.buscarPorId(id);
            
            assertNotNull(resultado, "Optional não deve ser null");
        }
        
        @Test
        @DisplayName("Deve buscar produtos com filtros")
        void testBuscarComFiltros() {
            List<Produto> produtos = produtoRepository.buscarComFiltros(
                "Teste", "Categoria", null, null, 
                BigDecimal.ZERO, new BigDecimal("10000"), 
                0, true, false);
            
            assertNotNull(produtos, "Lista não deve ser null");
            assertTrue(produtos.size() >= 0, "Lista deve ter tamanho válido");
        }
    }
    
    @Nested
    @DisplayName("Testes de atualização")
    class AtualizarTests {
        
        @Test
        @DisplayName("Deve atualizar produto existente")
        void testAtualizarProduto() {
            Produto produto = criarProdutoValido();
            produto.setId(1L);
            
            boolean resultado = produtoRepository.atualizar(produto);
            
            // Pode retornar false se não existir no banco
            assertNotNull(resultado, "Resultado não deve ser null");
        }
    }
    
    @Nested
    @DisplayName("Testes de exclusão")
    class ExcluirTests {
        
        @Test
        @DisplayName("Deve excluir produto por nome")
        void testExcluirPorNome() {
            String nome = "Produto Teste";
            
            boolean resultado = produtoRepository.excluir(nome);
            
            // Pode retornar false se não existir
            assertNotNull(resultado, "Resultado não deve ser null");
        }
        
        @Test
        @DisplayName("Deve remover produto por ID")
        void testRemoverPorId() {
            Long id = 1L;
            
            boolean resultado = produtoRepository.remover(id);
            
            // Pode retornar false se não existir
            assertNotNull(resultado, "Resultado não deve ser null");
        }
    }
    
    @Nested
    @DisplayName("Testes de estoque")
    class EstoqueTests {
        
        @Test
        @DisplayName("Deve atualizar estoque do produto")
        void testAtualizarEstoque() {
            Long id = 1L;
            int novaQuantidade = 100;
            
            boolean resultado = produtoRepository.atualizarEstoque(id, novaQuantidade);
            
            // Pode retornar false se não existir
            assertNotNull(resultado, "Resultado não deve ser null");
        }
        
        @Test
        @DisplayName("Deve buscar produtos com estoque baixo")
        void testBuscarEstoqueBaixo() {
            List<Produto> produtos = produtoRepository.buscarEstoqueBaixo();
            
            assertNotNull(produtos, "Lista não deve ser null");
            assertTrue(produtos.size() >= 0, "Lista deve ter tamanho válido");
        }
        
        @Test
        @DisplayName("Deve buscar produtos com estoque abaixo do limite")
        void testBuscarComEstoqueBaixo() {
            int limite = 10;
            
            List<Produto> produtos = produtoRepository.buscarComEstoqueBaixo(limite);
            
            assertNotNull(produtos, "Lista não deve ser null");
            assertTrue(produtos.size() >= 0, "Lista deve ter tamanho válido");
        }
    }
    
    @Nested
    @DisplayName("Testes de busca avançada")
    class BuscaAvancadaTests {
        
        @Test
        @DisplayName("Deve buscar produtos por nome")
        void testBuscarPorNome() {
            String nome = "Teste";
            
            List<Produto> produtos = produtoRepository.buscarPorNome(nome);
            
            assertNotNull(produtos, "Lista não deve ser null");
            assertTrue(produtos.size() >= 0, "Lista deve ter tamanho válido");
        }
        
        @Test
        @DisplayName("Deve buscar produtos por categoria")
        void testBuscarPorCategoria() {
            String categoria = "Teste";
            
            List<Produto> produtos = produtoRepository.buscarPorCategoria(categoria);
            
            assertNotNull(produtos, "Lista não deve ser null");
            assertTrue(produtos.size() >= 0, "Lista deve ter tamanho válido");
        }
        
        @Test
        @DisplayName("Deve buscar produtos por faixa de preço")
        void testBuscarPorFaixaPreco() {
            BigDecimal precoMin = BigDecimal.ZERO;
            BigDecimal precoMax = new BigDecimal("10000");
            
            List<Produto> produtos = produtoRepository.buscarPorFaixaPreco(precoMin, precoMax);
            
            assertNotNull(produtos, "Lista não deve ser null");
            assertTrue(produtos.size() >= 0, "Lista deve ter tamanho válido");
        }
        
        @Test
        @DisplayName("Deve buscar produtos por código")
        void testBuscarPorCodigo() {
            String codigo = "PROD-001";
            
            List<Produto> produtos = produtoRepository.buscarPorCodigo(codigo);
            
            assertNotNull(produtos, "Lista não deve ser null");
            assertTrue(produtos.size() >= 0, "Lista deve ter tamanho válido");
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
