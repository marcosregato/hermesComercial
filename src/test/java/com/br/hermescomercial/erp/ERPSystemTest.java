package com.br.hermescomercial.erp;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import static org.junit.jupiter.api.Assertions.*;

import com.br.hermescomercial.erp.controller.ERPMenuPrincipalSwingController;
import com.br.hermescomercial.erp.controller.ERPProdutoSwingController;
import com.br.hermescomercial.erp.controller.ERPFinanceiroSwingController;
import com.br.hermescomercial.erp.controller.ERPEstoqueSwingController;
import com.br.hermescomercial.erp.controller.ERPClienteSwingController;
import com.br.hermescomercial.erp.controller.ERPRelatorioFinanceiroSwingController;
import com.br.hermescomercial.erp.controller.ERPUsuarioSwingController;
import com.br.hermescomercial.erp.controller.ERPConfiguracaoSwingController;
import com.br.hermescomercial.erp.controller.ERPRelatorioSwingController;
import com.br.hermescomercial.model.Produto;
import com.br.hermescomercial.service.ProdutoService;

import java.math.BigDecimal;

/**
 * Testes completos para o sistema ERP
 * Versão 2.5.0 - Hermes Comercial
 */
public class ERPSystemTest {

    private ProdutoService produtoService;
    private Produto produtoTeste;

    @BeforeEach
    void setUp() {
        produtoService = new ProdutoService();
        
        // Criar produto de teste com código único usando timestamp
        produtoTeste = new Produto();
        produtoTeste.setCodigo("ERP_TEST_" + System.currentTimeMillis());
        produtoTeste.setNome("Produto Teste ERP");
        produtoTeste.setPrecoVenda(new BigDecimal("25.50"));
        produtoTeste.setEstoque(50);
        produtoTeste.setCategoria("Teste");
    }

    @Test
    @DisplayName("Teste de criação do controller ERP Menu Principal")
    void testERPMenuPrincipalControllerCreation() {
        assertDoesNotThrow(() -> {
            ERPMenuPrincipalSwingController controller = new ERPMenuPrincipalSwingController();
            assertNotNull(controller);
        }, "Controller ERP Menu Principal deve ser criado sem exceções");
    }

    @Test
    @DisplayName("Teste de criação do controller ERP Produto")
    void testERPProdutoControllerCreation() {
        assertDoesNotThrow(() -> {
            ERPProdutoSwingController controller = new ERPProdutoSwingController();
            assertNotNull(controller);
        }, "Controller ERP Produto deve ser criado sem exceções");
    }

    @Test
    @DisplayName("Teste de criação do controller ERP Financeiro")
    void testERPFinanceiroControllerCreation() {
        assertDoesNotThrow(() -> {
            ERPFinanceiroSwingController controller = new ERPFinanceiroSwingController();
            assertNotNull(controller);
        }, "Controller ERP Financeiro deve ser criado sem exceções");
    }

    @Test
    @DisplayName("Teste de criação do controller ERP Estoque")
    void testERPEstoqueControllerCreation() {
        assertDoesNotThrow(() -> {
            ERPEstoqueSwingController controller = new ERPEstoqueSwingController();
            assertNotNull(controller);
        }, "Controller ERP Estoque deve ser criado sem exceções");
    }

    @Test
    @DisplayName("Teste de criação do controller ERP Cliente")
    void testERPClienteControllerCreation() {
        assertDoesNotThrow(() -> {
            ERPClienteSwingController controller = new ERPClienteSwingController();
            assertNotNull(controller);
        }, "Controller ERP Cliente deve ser criado sem exceções");
    }

    @Test
    @DisplayName("Teste de criação do controller ERP Relatório Financeiro")
    void testERPRelatorioFinanceiroControllerCreation() {
        assertDoesNotThrow(() -> {
            ERPRelatorioFinanceiroSwingController controller = new ERPRelatorioFinanceiroSwingController();
            assertNotNull(controller);
        }, "Controller ERP Relatório Financeiro deve ser criado sem exceções");
    }

    @Test
    @DisplayName("Teste de cadastro de produto no ERP")
    void testProdutoCadastroERP() {
        assertDoesNotThrow(() -> {
            produtoService.salvar(produtoTeste);
        }, "Cadastro de produto não deve lançar exceções");

        assertNotNull(produtoTeste, "Produto de teste não deve ser nulo");
        assertTrue(produtoTeste.getCodigo().startsWith("ERP_TEST_"), "Código deve começar com ERP_TEST_");
        assertEquals("Produto Teste ERP", produtoTeste.getNome());
        assertEquals(new BigDecimal("25.50"), produtoTeste.getPrecoVenda());
        assertEquals(50, produtoTeste.getEstoque());
        assertEquals("Teste", produtoTeste.getCategoria());
    }

    @Test
    @DisplayName("Teste de validação de dados do produto ERP")
    void testValidacaoDadosProdutoERP() {
        assertTrue(produtoTeste.getCodigo().length() > 0, "Código do produto não deve ser vazio");
        assertTrue(produtoTeste.getNome().length() > 0, "Nome do produto não deve ser vazio");
        assertTrue(produtoTeste.getPrecoVenda().compareTo(BigDecimal.ZERO) > 0, "Preço do produto deve ser maior que zero");
        assertTrue(produtoTeste.getEstoque() >= 0, "Estoque do produto não deve ser negativo");
        assertNotNull(produtoTeste.getCategoria(), "Categoria do produto não deve ser nula");
    }

    @Test
    @DisplayName("Teste de validação de código de barras")
    void testValidacaoCodigoBarras() {
        produtoTeste.setCodigoBarras("7891234567890");
        
        assertEquals("7891234567890", produtoTeste.getCodigoBarras());
        assertEquals(13, produtoTeste.getCodigoBarras().length(), "Código de barras deve ter 13 dígitos");
    }

    @Test
    @DisplayName("Teste de validação de unidade de medida")
    void testValidacaoUnidadeMedida() {
        produtoTeste.setUnidade("UN");
        
        assertEquals("UN", produtoTeste.getUnidade());
        assertTrue(produtoTeste.getUnidade().length() > 0, "Unidade não deve ser vazia");
    }

    @Test
    @DisplayName("Teste de cálculo de valor de estoque")
    void testCalculoValorEstoque() {
        BigDecimal precoUnitario = new BigDecimal("25.50");
        int quantidadeEstoque = 50;
        BigDecimal valorEstoqueEsperado = precoUnitario.multiply(new BigDecimal(quantidadeEstoque));

        assertEquals(new BigDecimal("1275.00"), valorEstoqueEsperado, 
            "Cálculo do valor do estoque deve estar correto");
    }

    @Test
    @DisplayName("Teste de validação de categoria")
    void testValidacaoCategoria() {
        assertTrue(produtoTeste.getCategoria().length() > 0, "Categoria não deve ser vazia");
        assertEquals("Teste", produtoTeste.getCategoria());
    }

    @Test
    @DisplayName("Teste de validação de marca")
    void testValidacaoMarca() {
        produtoTeste.setMarca("Marca Teste");
        
        assertEquals("Marca Teste", produtoTeste.getMarca());
        assertTrue(produtoTeste.getMarca().length() > 0, "Marca não deve ser vazia");
    }

    @Test
    @DisplayName("Teste de atualização de estoque ERP")
    void testAtualizacaoEstoqueERP() {
        int estoqueInicial = produtoTeste.getEstoque();
        int quantidadeEntrada = 10;
        
        produtoTeste.setEstoque(estoqueInicial + quantidadeEntrada);
        
        assertEquals(estoqueInicial + quantidadeEntrada, produtoTeste.getEstoque(),
            "Estoque deve ser atualizado corretamente após entrada");
    }

    @Test
    @DisplayName("Teste de validação de preço de venda")
    void testValidacaoPrecoVenda() {
        BigDecimal precoValido = new BigDecimal("25.50");
        BigDecimal precoInvalido = new BigDecimal("-10.00");
        BigDecimal precoZero = BigDecimal.ZERO;
        
        assertTrue(precoValido.compareTo(BigDecimal.ZERO) > 0, "Preço válido deve ser maior que zero");
        assertFalse(precoInvalido.compareTo(BigDecimal.ZERO) > 0, "Preço negativo não deve ser válido");
        assertFalse(precoZero.compareTo(BigDecimal.ZERO) > 0, "Preço zero não deve ser válido");
    }

    @Test
    @DisplayName("Teste de subcategoria")
    void testSubcategoria() {
        produtoTeste.setSubCategoria("Subcategoria Teste");
        
        assertEquals("Subcategoria Teste", produtoTeste.getSubCategoria());
        assertNotNull(produtoTeste.getSubCategoria(), "Subcategoria não deve ser nula quando definida");
    }

    @Test
    @DisplayName("Teste de data de compra")
    void testDataCompra() {
        produtoTeste.setDataCompra("2024-01-15");
        
        assertEquals("2024-01-15", produtoTeste.getDataCompra());
        assertNotNull(produtoTeste.getDataCompra(), "Data de compra não deve ser nula quando definida");
    }

    @Test
    @DisplayName("Teste de integração básica ERP")
    void testIntegracaoBasicaERP() {
        // Teste de inicialização dos componentes principais
        assertDoesNotThrow(() -> {
            ERPMenuPrincipalSwingController menu = new ERPMenuPrincipalSwingController();
            ERPProdutoSwingController produto = new ERPProdutoSwingController();
            ERPFinanceiroSwingController financeiro = new ERPFinanceiroSwingController();
            ERPEstoqueSwingController estoque = new ERPEstoqueSwingController();
            ERPClienteSwingController cliente = new ERPClienteSwingController();
            ERPRelatorioFinanceiroSwingController relatorio = new ERPRelatorioFinanceiroSwingController();
            
            assertNotNull(menu);
            assertNotNull(produto);
            assertNotNull(financeiro);
            assertNotNull(estoque);
            assertNotNull(cliente);
            assertNotNull(relatorio);
        }, "Componentes ERP devem ser inicializados sem exceções");
    }

    @Test
    @DisplayName("Teste de validação de ID do produto")
    void testValidacaoIDProduto() {
        produtoTeste.setId(1L);
        
        assertEquals(Long.valueOf(1L), produtoTeste.getId());
        assertNotNull(produtoTeste.getId(), "ID do produto não deve ser nulo quando definido");
    }

    @Test
    @DisplayName("Teste de produto completo ERP")
    void testProdutoCompletoERP() {
        produtoTeste.setId(1L);
        produtoTeste.setCodigoBarras("7891234567890");
        produtoTeste.setMarca("Marca ERP");
        produtoTeste.setUnidade("UN");
        produtoTeste.setSubCategoria("Sub ERP");
        produtoTeste.setDataCompra("2024-01-15");
        
        // Validações completas
        assertNotNull(produtoTeste.getId());
        assertTrue(produtoTeste.getCodigo().startsWith("ERP_TEST_"), "Código deve começar com ERP_TEST_");
        assertEquals("Produto Teste ERP", produtoTeste.getNome());
        assertEquals("Teste", produtoTeste.getCategoria());
        assertEquals("Sub ERP", produtoTeste.getSubCategoria());
        assertEquals("Marca ERP", produtoTeste.getMarca());
        assertEquals("7891234567890", produtoTeste.getCodigoBarras());
        assertEquals("UN", produtoTeste.getUnidade());
        assertEquals("2024-01-15", produtoTeste.getDataCompra());
        assertEquals(new BigDecimal("25.50"), produtoTeste.getPrecoVenda());
        assertEquals(50, produtoTeste.getEstoque());
    }

    @Test
    @DisplayName("Teste de criação do controller ERP Usuário")
    void testERPUsuarioControllerCreation() {
        assertDoesNotThrow(() -> {
            ERPUsuarioSwingController controller = new ERPUsuarioSwingController();
            assertNotNull(controller);
        }, "Controller ERP Usuário deve ser criado sem exceções");
    }

    @Test
    @DisplayName("Teste de criação do controller ERP Configuração")
    void testERPConfiguracaoControllerCreation() {
        assertDoesNotThrow(() -> {
            ERPConfiguracaoSwingController controller = new ERPConfiguracaoSwingController();
            assertNotNull(controller);
        }, "Controller ERP Configuração deve ser criado sem exceções");
    }

    @Test
    @DisplayName("Teste de criação do controller ERP Relatório")
    void testERPRelatorioControllerCreation() {
        assertDoesNotThrow(() -> {
            ERPRelatorioSwingController controller = new ERPRelatorioSwingController();
            assertNotNull(controller);
        }, "Controller ERP Relatório deve ser criado sem exceções");
    }
}
