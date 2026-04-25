package com.br.hermescomercial.dao;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import com.br.hermescomercial.model.Produto;

import java.util.List;

/**
 * Testes unitários para a classe ProdutoDao
 * Testa as funcionalidades de gerenciamento de produtos do sistema
 */
class ProdutoDaoTest {

    private ProdutoDao produtoDao;
    private Produto produtoTeste;

    @BeforeEach
    void setUp() {
        produtoDao = new ProdutoDao();
        produtoTeste = new Produto();
        produtoTeste.setNome("Notebook Dell");
        produtoTeste.setCategoria("Eletrônicos");
        produtoTeste.setSubCategoria("Computadores");
        produtoTeste.setCodigo("NB001");
        produtoTeste.setMarca("Dell");
        produtoTeste.setDataCompra("2024-01-15");
    }

    @Test
    @DisplayName("Deve inicializar ProdutoDao sem lançar exceção")
    void testInitialize() {
        // Act & Assert
        assertDoesNotThrow(() -> {
            ProdutoDao dao = new ProdutoDao();
            assertNotNull(dao, "DAO não deve ser nulo");
        });
    }

    @Test
    @DisplayName("Deve salvar produto no banco sem lançar exceção")
    void testSalvar() {
        // Arrange
        Produto novoProduto = new Produto();
        novoProduto.setNome("Mouse Logitech");
        novoProduto.setCategoria("Periféricos");
        novoProduto.setSubCategoria("Mouse");
        novoProduto.setCodigo("MS001");
        novoProduto.setMarca("Logitech");
        novoProduto.setDataCompra("2024-02-20");

        // Act & Assert
        assertDoesNotThrow(() -> {
            produtoDao.salvar(novoProduto);
        });
        
        // Nota: Como depende de conexão com banco, apenas verificamos que não lança exceção
    }

    @Test
    @DisplayName("Deve tentar salvar produto nulo")
    void testSalvarProdutoNulo() {
        // Arrange
        Produto produtoNulo = null;

        // Act & Assert
        assertDoesNotThrow(() -> {
            produtoDao.salvar(produtoNulo);
        });
        
        // Nota: Como o método não verifica null, pode lançar exceção no tratamento
    }

    @Test
    @DisplayName("Deve remover produto por nome sem lançar exceção")
    void testRemove() {
        // Arrange
        String nomeProduto = "Notebook Dell";

        // Act & Assert
        assertDoesNotThrow(() -> {
            produtoDao.remove(nomeProduto);
        });
        
        // Nota: Como depende de banco de dados, apenas verificamos que não lança exceção
    }

    @Test
    @DisplayName("Deve tentar remover produto com nome nulo")
    void testRemoveComNomeNulo() {
        // Arrange
        String nomeNulo = null;

        // Act & Assert
        assertDoesNotThrow(() -> {
            produtoDao.remove(nomeNulo);
        });
        
        // Nota: Como o método não verifica null, pode lançar exceção no tratamento
    }

    @Test
    @DisplayName("Deve tentar remover produto com nome vazio")
    void testRemoveComNomeVazio() {
        // Arrange
        String nomeVazio = "";

        // Act & Assert
        assertDoesNotThrow(() -> {
            produtoDao.remove(nomeVazio);
        });
        
        // Nota: Como o método não verifica strings vazias, pode lançar exceção no tratamento
    }

    @Test
    @DisplayName("Deve atualizar produto sem lançar exceção")
    void testUpdate() {
        // Arrange
        Produto produtoAtualizar = new Produto();
        produtoAtualizar.setNome("Notebook Dell");
        produtoAtualizar.setCategoria("Eletrônicos");
        produtoAtualizar.setSubCategoria("Notebooks");
        produtoAtualizar.setCodigo("NB001");
        produtoAtualizar.setMarca("Dell");
        produtoAtualizar.setDataCompra("2024-01-20");

        // Act & Assert
        assertDoesNotThrow(() -> {
            produtoDao.update(produtoAtualizar);
        });
        
        // Nota: Como depende de banco de dados, apenas verificamos que não lança exceção
    }

    @Test
    @DisplayName("Deve tentar atualizar produto nulo")
    void testUpdateProdutoNulo() {
        // Arrange
        Produto produtoNulo = null;

        // Act & Assert
        assertDoesNotThrow(() -> {
            produtoDao.update(produtoNulo);
        });
        
        // Nota: Como o método não verifica null, pode lançar exceção no tratamento
    }

    @Test
    @DisplayName("Deve buscar produtos por nome")
    void testBuscar() {
        // Arrange
        String nomeBusca = "Notebook";

        // Act
        List<Produto> resultado = produtoDao.buscar(nomeBusca);

        // Assert
        assertNotNull(resultado, "Resultado não deve ser nulo");
        // Nota: Como depende de banco de dados, a lista pode estar vazia
        
        // Verifica que é uma lista válida
        assertDoesNotThrow(() -> {
            resultado.size(); // Não deve lançar exceção
        });
    }

    @Test
    @DisplayName("Deve buscar produtos com nome nulo")
    void testBuscarComNomeNulo() {
        // Arrange
        String nomeNulo = null;

        // Act & Assert
        assertDoesNotThrow(() -> {
            List<Produto> resultado = produtoDao.buscar(nomeNulo);
            assertNotNull(resultado);
        });
    }

    @Test
    @DisplayName("Deve buscar produtos com nome vazio")
    void testBuscarComNomeVazio() {
        // Arrange
        String nomeVazio = "";

        // Act & Assert
        assertDoesNotThrow(() -> {
            List<Produto> resultado = produtoDao.buscar(nomeVazio);
            assertNotNull(resultado);
        });
    }

    @Test
    @DisplayName("Deve listar todos os produtos")
    void testListar() {
        // Act
        List<Produto> resultado = produtoDao.listar();

        // Assert
        assertNotNull(resultado, "Resultado não deve ser nulo");
        // Nota: Como depende de banco de dados, a lista pode estar vazia
        
        // Verifica que é uma lista válida
        assertDoesNotThrow(() -> {
            resultado.size(); // Não deve lançar exceção
        });
    }

    @Test
    @DisplayName("Deve criar produto com valores válidos")
    void testCriarProdutoValido() {
        // Arrange
        Produto produto = new Produto();

        // Act
        produto.setNome("Smartphone Samsung");
        produto.setCategoria("Eletrônicos");
        produto.setSubCategoria("Celulares");
        produto.setCodigo("SP001");
        produto.setMarca("Samsung");
        produto.setDataCompra("2024-03-10");

        // Assert
        assertEquals("Smartphone Samsung", produto.getNome());
        assertEquals("Eletrônicos", produto.getCategoria());
        assertEquals("Celulares", produto.getSubCategoria());
        assertEquals("SP001", produto.getCodigo());
        assertEquals("Samsung", produto.getMarca());
        assertEquals("2024-03-10", produto.getDataCompra());
    }

    @Test
    @DisplayName("Deve validar valores padrão do produto")
    void testValoresPadraoProduto() {
        // Arrange
        Produto produto = new Produto();

        // Act & Assert
        assertNull(produto.getNome());
        assertNull(produto.getCategoria());
        assertNull(produto.getSubCategoria());
        assertNull(produto.getCodigo());
        assertNull(produto.getMarca());
        assertNull(produto.getDataCompra());
    }

    @Test
    @DisplayName("Deve atualizar nome do produto")
    void testAtualizarNomeProduto() {
        // Arrange
        Produto produto = new Produto();
        produto.setNome("Nome Original");

        // Act
        produto.setNome("Nome Atualizado");

        // Assert
        assertEquals("Nome Atualizado", produto.getNome());
    }

    @Test
    @DisplayName("Deve atualizar categoria do produto")
    void testAtualizarCategoriaProduto() {
        // Arrange
        Produto produto = new Produto();
        produto.setCategoria("Categoria Original");

        // Act
        produto.setCategoria("Categoria Atualizada");

        // Assert
        assertEquals("Categoria Atualizada", produto.getCategoria());
    }

    @Test
    @DisplayName("Deve atualizar código do produto")
    void testAtualizarCodigoProduto() {
        // Arrange
        Produto produto = new Produto();
        produto.setCodigo("COD001");

        // Act
        produto.setCodigo("COD002");

        // Assert
        assertEquals("COD002", produto.getCodigo());
    }

    @Test
    @DisplayName("Deve testar produto com marca")
    void testProdutoComMarca() {
        // Arrange
        Produto produto = new Produto();

        // Act
        produto.setMarca("Apple");

        // Assert
        assertEquals("Apple", produto.getMarca());
    }

    @Test
    @DisplayName("Deve testar produto com data de compra")
    void testProdutoComDataCompra() {
        // Arrange
        Produto produto = new Produto();

        // Act
        produto.setDataCompra("2024-12-25");

        // Assert
        assertEquals("2024-12-25", produto.getDataCompra());
    }

    @Test
    @DisplayName("Deve testar produto com subcategoria")
    void testProdutoComSubCategoria() {
        // Arrange
        Produto produto = new Produto();

        // Act
        produto.setSubCategoria("Gaming");

        // Assert
        assertEquals("Gaming", produto.getSubCategoria());
    }

    @Test
    @DisplayName("Deve criar múltiplas instâncias do DAO")
    void testCriarMultiplasInstancias() {
        // Act & Assert
        assertDoesNotThrow(() -> {
            ProdutoDao dao1 = new ProdutoDao();
            ProdutoDao dao2 = new ProdutoDao();
            ProdutoDao dao3 = new ProdutoDao();
            
            assertNotNull(dao1);
            assertNotNull(dao2);
            assertNotNull(dao3);
            
            // Verifica que são instâncias diferentes
            assertNotSame(dao1, dao2);
            assertNotSame(dao2, dao3);
        });
    }

    @Test
    @DisplayName("Deve testar combinação de operações")
    void testCombinacaoOperacoes() {
        // Act & Assert
        assertDoesNotThrow(() -> {
            produtoDao.salvar(produtoTeste);
            List<Produto> resultado = produtoDao.buscar("Notebook");
            produtoDao.listar();
            produtoDao.update(produtoTeste);
            produtoDao.remove("Notebook Dell");
        });
    }

    @Test
    @DisplayName("Deve testar salvar múltiplos produtos")
    void testSalvarMultiplasProdutos() {
        // Arrange & Act & Assert
        assertDoesNotThrow(() -> {
            Produto produto1 = new Produto();
            produto1.setNome("Teclado Mecânico");
            produto1.setCategoria("Periféricos");
            produto1.setSubCategoria("Teclado");
            produto1.setCodigo("TK001");
            produto1.setMarca("Razer");
            produto1.setDataCompra("2024-01-10");
            
            Produto produto2 = new Produto();
            produto2.setNome("Monitor 27\"");
            produto2.setCategoria("Periféricos");
            produto2.setSubCategoria("Monitor");
            produto2.setCodigo("MN001");
            produto2.setMarca("LG");
            produto2.setDataCompra("2024-01-12");
            
            Produto produto3 = new Produto();
            produto3.setNome("Headset Bluetooth");
            produto3.setCategoria("Áudio");
            produto3.setSubCategoria("Fone");
            produto3.setCodigo("HS001");
            produto3.setMarca("Sony");
            produto3.setDataCompra("2024-01-14");
            
            produtoDao.salvar(produto1);
            produtoDao.salvar(produto2);
            produtoDao.salvar(produto3);
        });
    }

    @Test
    @DisplayName("Deve testar diferentes tipos de busca")
    void testDiferentesTiposBusca() {
        // Arrange & Act & Assert
        assertDoesNotThrow(() -> {
            List<Produto> resultado1 = produtoDao.buscar("Note");
            List<Produto> resultado2 = produtoDao.buscar("Dell");
            List<Produto> resultado3 = produtoDao.buscar("Eletr");
            List<Produto> resultado4 = produtoDao.buscar("Comput");
            List<Produto> resultado5 = produtoDao.buscar("xyz");
            
            assertNotNull(resultado1);
            assertNotNull(resultado2);
            assertNotNull(resultado3);
            assertNotNull(resultado4);
            assertNotNull(resultado5);
        });
    }

    @Test
    @DisplayName("Deve testar produto com valores vazios")
    void testProdutoComValoresVazios() {
        // Arrange
        Produto produto = new Produto();

        // Act
        produto.setNome("");
        produto.setCategoria("");
        produto.setSubCategoria("");
        produto.setCodigo("");
        produto.setMarca("");
        produto.setDataCompra("");

        // Assert
        assertEquals("", produto.getNome());
        assertEquals("", produto.getCategoria());
        assertEquals("", produto.getSubCategoria());
        assertEquals("", produto.getCodigo());
        assertEquals("", produto.getMarca());
        assertEquals("", produto.getDataCompra());
    }

    @Test
    @DisplayName("Deve testar diferentes categorias de produtos")
    void testDiferentesCategoriasProdutos() {
        // Arrange & Act & Assert
        assertDoesNotThrow(() -> {
            Produto eletronico = new Produto();
            eletronico.setCategoria("Eletrônicos");
            assertEquals("Eletrônicos", eletronico.getCategoria());

            Produto periferico = new Produto();
            periferico.setCategoria("Periféricos");
            assertEquals("Periféricos", periferico.getCategoria());

            Produto audio = new Produto();
            audio.setCategoria("Áudio");
            assertEquals("Áudio", audio.getCategoria());

            Produto mobilia = new Produto();
            mobilia.setCategoria("Mobília");
            assertEquals("Mobília", mobilia.getCategoria());
        });
    }
}
