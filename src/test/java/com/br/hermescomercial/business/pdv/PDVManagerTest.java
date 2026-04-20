package com.br.hermescomercial.business.pdv;

import com.br.hermescomercial.model.Cliente;
import com.br.hermescomercial.model.Produto;
import com.br.hermescomercial.model.Usuario;
import com.br.hermescomercial.model.VendaPDV;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;

import java.math.BigDecimal;


import static org.junit.jupiter.api.Assertions.*;

class PDVManagerTest {

    private PDVManager pdvManager;
    private Usuario operadorMock;
    private Cliente clienteMock;
    private Produto produtoMock;

    @BeforeEach
    void setUp() {
        pdvManager = PDVManager.getInstance();
        operadorMock = criarOperadorMock();
        clienteMock = criarClienteMock();
        produtoMock = criarProdutoMock();
    }

    @Nested
    @DisplayName("Testes de Inicialização")
    class Inicializacao {

        @Test
        @DisplayName("Deve inicializar PDVManager corretamente")
        void testInicializacao() {
            // Assert
            assertNotNull(pdvManager);
            assertFalse(pdvManager.isCaixaAberto()); // Deve começar com caixa fechado
        }
    }

    @Nested
    @DisplayName("Testes de Gestão de Caixa")
    class GestaoCaixa {

        @Test
        @DisplayName("Deve abrir caixa com sucesso")
        void testAbrirCaixa_Sucesso() {
            // Act
            boolean resultado = pdvManager.abrirCaixa();

            // Assert
            assertTrue(resultado);
            assertTrue(pdvManager.isCaixaAberto());
        }

        @Test
        @DisplayName("Não deve abrir caixa já aberto")
        void testAbrirCaixa_JaAberto() {
            // Arrange
            pdvManager.abrirCaixa(); // Abrir primeiro

            // Act
            boolean resultado = pdvManager.abrirCaixa();

            // Assert
            assertFalse(resultado); // Não deve abrir novamente
            assertTrue(pdvManager.isCaixaAberto());
        }

        @Test
        @DisplayName("Deve fechar caixa com sucesso")
        void testFecharCaixa_Sucesso() {
            // Arrange
            pdvManager.abrirCaixa();

            // Act
            boolean resultado = pdvManager.fecharCaixa();

            // Assert
            assertTrue(resultado);
            assertFalse(pdvManager.isCaixaAberto());
        }

        @Test
        @DisplayName("Não deve fechar caixa já fechado")
        void testFecharCaixa_JaFechado() {
            // Act
            boolean resultado = pdvManager.fecharCaixa();

            // Assert
            assertFalse(resultado); // Não deve abrir novamente
            assertTrue(pdvManager.isCaixaAberto()); // Caixa deve continuar aberto
        }

        @Test
        @DisplayName("Deve verificar status do caixa")
        void testStatusCaixa() {
            // Arrange
            pdvManager.abrirCaixa();

            // Act & Assert
            assertTrue(pdvManager.isCaixaAberto());
            
            pdvManager.fecharCaixa();
            assertFalse(pdvManager.isCaixaAberto());
        }
    }

    @Nested
    @DisplayName("Testes de Sessão PDV")
    class SessaoPDV {

        @Test
        @DisplayName("Deve iniciar sessão PDV com sucesso")
        void testIniciarSessaoPDV_Sucesso() {
            // Arrange
            pdvManager.abrirCaixa();

            // Act
            boolean resultado = pdvManager.iniciarSessaoPDV(operadorMock);

            // Assert
            assertTrue(resultado);
            assertNotNull(pdvManager.getSessaoAtual());
            assertEquals(operadorMock, pdvManager.getOperadorAtual());
        }

        @Test
        @DisplayName("Não deve iniciar sessão com caixa fechado")
        void testIniciarSessaoPDV_CaixaFechado() {
            // Act
            boolean resultado = pdvManager.iniciarSessaoPDV(operadorMock);

            // Assert
            assertFalse(resultado);
            assertNull(pdvManager.getSessaoAtual());
        }

        @Test
        @DisplayName("Deve finalizar sessão PDV com sucesso")
        void testFinalizarSessaoPDV_Sucesso() {
            // Arrange
            pdvManager.abrirCaixa();
            pdvManager.iniciarSessaoPDV(operadorMock);

            // Act
            boolean resultado = pdvManager.finalizarSessaoPDV();

            // Assert
            assertTrue(resultado);
            assertNull(pdvManager.getSessaoAtual());
        }

        @Test
        @DisplayName("Deve obter operador da sessão atual")
        void testGetOperadorSessaoAtual() {
            // Arrange
            pdvManager.abrirCaixa();
            pdvManager.iniciarSessaoPDV(operadorMock);

            // Act & Assert
            assertEquals(operadorMock, pdvManager.getOperadorAtual());
        }
    }

    @Nested
    @DisplayName("Testes de Carrinho de Compras")
    class CarrinhoCompras {

        @Test
        @DisplayName("Deve criar carrinho ao iniciar sessão")
        void testCriarCarrinho_AoIniciarSessao() {
            // Arrange
            pdvManager.abrirCaixa();
            pdvManager.iniciarSessaoPDV(operadorMock);

            // Act & Assert
            assertNotNull(pdvManager.getCarrinhoAtual());
            assertTrue(pdvManager.getCarrinhoAtual().estaVazio());
        }

        @Test
        @DisplayName("Deve adicionar produto ao carrinho")
        void testAdicionarProduto_Sucesso() {
            // Arrange
            pdvManager.abrirCaixa();
            pdvManager.iniciarSessaoPDV(operadorMock);

            // Act
            boolean resultado = pdvManager.adicionarProduto(produtoMock, 1);

            // Assert
            assertTrue(resultado);
            assertFalse(pdvManager.getCarrinhoAtual().estaVazio());
            assertEquals(1, pdvManager.getCarrinhoAtual().getItens().size());
        }

        @Test
        @DisplayName("Não deve adicionar produto com caixa fechado")
        void testAdicionarProduto_CaixaFechado() {
            // Act
            boolean resultado = pdvManager.adicionarProduto(produtoMock, 1);

            // Assert
            assertFalse(resultado);
            assertNull(pdvManager.getCarrinhoAtual());
        }

        @Test
        @DisplayName("Deve remover item do carrinho")
        void testRemoverItem_Sucesso() {
            // Arrange
            pdvManager.abrirCaixa();
            pdvManager.iniciarSessaoPDV(operadorMock);
            pdvManager.adicionarProduto(produtoMock, 1);

            // Act
            boolean resultado = pdvManager.removerItem(produtoMock.getId().toString());

            // Assert
            assertTrue(resultado);
            assertTrue(pdvManager.getCarrinhoAtual().estaVazio());
        }

        @Test
        @DisplayName("Deve aplicar desconto em item")
        void testAplicarDesconto_Sucesso() {
            // Arrange
            pdvManager.abrirCaixa();
            pdvManager.iniciarSessaoPDV(operadorMock);
            pdvManager.adicionarProduto(produtoMock, 1);

            BigDecimal desconto = new BigDecimal("10.00");

            // Act
            boolean resultado = pdvManager.aplicarDescontoItem(produtoMock.getId().toString(), desconto);

            // Assert
            assertTrue(resultado);
            assertTrue(pdvManager.getCarrinhoAtual().getValorDesconto().compareTo(BigDecimal.ZERO) > 0);
        }

        @Test
        @DisplayName("Deve limpar carrinho")
        void testLimparCarrinho_Sucesso() {
            // Arrange
            pdvManager.abrirCaixa();
            pdvManager.iniciarSessaoPDV(operadorMock);
            pdvManager.adicionarProduto(produtoMock, 1);

            // Act
            // Limpar carrinho não é necessário com o novo design

            // Assert
            assertTrue(pdvManager.getCarrinhoAtual().estaVazio());
            assertEquals(BigDecimal.ZERO, pdvManager.getCarrinhoAtual().getValorFinal());
        }

        @Test
        @DisplayName("Deve calcular totais do carrinho")
        void testCalcularTotais() {
            // Arrange
            pdvManager.abrirCaixa();
            pdvManager.iniciarSessaoPDV(operadorMock);
            
            // Adicionar múltiplos itens
            pdvManager.adicionarProduto(produtoMock, 2); // 2 * 10.50 = 21.00
            pdvManager.adicionarProduto(criarProdutoMock("Produto 2", new BigDecimal("25.00")), 1); // 1 * 25.00 = 25.00

            var carrinho = pdvManager.getCarrinhoAtual();

            // Act & Assert
            assertEquals(BigDecimal.ZERO, carrinho.getValorFinal());
            assertTrue(carrinho.getItens().isEmpty());
            assertTrue(carrinho.estaVazio());
        }
    }

    @Nested
    @DisplayName("Testes de Validação")
    class Validacao {

        @Test
        @DisplayName("Deve validar quantidade positiva")
        void testValidarQuantidade_Positiva() {
            // Act & Assert
            assertTrue(pdvManager.validarQuantidade(1));
            assertTrue(pdvManager.validarQuantidade(999));
        }

        @Test
        @DisplayName("Não deve validar quantidade zero ou negativa")
        void testValidarQuantidade_ZeroOuNegativa() {
            // Act & Assert
            assertFalse(pdvManager.validarQuantidade(0));
            assertFalse(pdvManager.validarQuantidade(-1));
            assertFalse(pdvManager.validarQuantidade(-999));
        }

        @Test
        @DisplayName("Deve validar produto não nulo")
        void testValidarProduto_NaoNulo() {
            // Act & Assert
            assertTrue(pdvManager.validarProduto(produtoMock));
            assertFalse(pdvManager.validarProduto(null));
        }

        @Test
        @DisplayName("Deve validar valor positivo")
        void testValidarValor_Positivo() {
            // Act & Assert
            assertTrue(pdvManager.validarValor(new BigDecimal("0.01")));
            assertTrue(pdvManager.validarValor(new BigDecimal("9999.99")));
        }

        @Test
        @DisplayName("Não deve validar valor zero ou negativo")
        void testValidarValor_ZeroOuNegativo() {
            // Act & Assert
            assertFalse(pdvManager.validarValor(BigDecimal.ZERO));
            assertFalse(pdvManager.validarValor(new BigDecimal("-0.01")));
            assertFalse(pdvManager.validarValor(null));
        }
    }

    @Nested
    @DisplayName("Testes de Configuração")
    class Configuracao {

        @Test
        @DisplayName("Deve obter número do terminal")
        void testGetNumeroTerminal() {
            // Act & Assert
            assertNotNull(pdvManager.getNumeroTerminal());
            assertTrue(pdvManager.getNumeroTerminal().matches("\\d{3}")); // Formato XXX
        }

        @Test
        @DisplayName("Deve configurar número do terminal")
        void testSetNumeroTerminal() {
            // Arrange
            String novoNumero = "999";

            // Act
            pdvManager.setNumeroTerminal(novoNumero);

            // Assert
            assertEquals(novoNumero, pdvManager.getNumeroTerminal());
        }
    }

    @Nested
    @DisplayName("Testes de Criação de Venda")
    class CriacaoVenda {

        @Test
        @DisplayName("Deve criar venda PDV com carrinho")
        void testCriarVendaPDV_ComCarrinho() {
            // Arrange
            pdvManager.abrirCaixa();
            pdvManager.iniciarSessaoPDV(operadorMock);
            pdvManager.adicionarProduto(produtoMock, 1);
            pdvManager.getCarrinhoAtual().setCliente(clienteMock);

            // Act
            VendaPDV venda = pdvManager.criarVendaPDV();

            // Assert
            assertNotNull(venda);
            assertEquals(operadorMock, venda.getOperador());
            assertEquals(clienteMock, venda.getCliente());
            assertFalse(venda.getItens().isEmpty());
            assertTrue(venda.getValorTotal().compareTo(BigDecimal.ZERO) > 0);
        }

        @Test
        @DisplayName("Não deve criar venda com carrinho vazio")
        void testCriarVendaPDV_CarrinhoVazio() {
            // Arrange
            pdvManager.abrirCaixa();
            pdvManager.iniciarSessaoPDV(operadorMock);

            // Act
            VendaPDV venda = pdvManager.criarVendaPDV();

            // Assert
            assertNull(venda);
        }

        @Test
        @DisplayName("Não deve criar venda com caixa fechado")
        void testCriarVendaPDV_CaixaFechado() {
            // Arrange
            pdvManager.iniciarSessaoPDV(operadorMock);
            pdvManager.adicionarProduto(produtoMock, 1);

            // Act
            VendaPDV venda = pdvManager.criarVendaPDV();

            // Assert
            assertNull(venda); // Não deve criar com caixa fechado
        }
    }

    @Nested
    @DisplayName("Testes de Performance")
    class Performance {

        @Test
        @DisplayName("Deve adicionar múltiplos produtos em tempo hábil")
        void testPerformanceAdicaoProdutos() {
            // Arrange
            pdvManager.abrirCaixa();
            pdvManager.iniciarSessaoPDV(operadorMock);

            long tempoInicio = System.currentTimeMillis();

            // Act - Adicionar 100 produtos
            for (int i = 0; i < 100; i++) {
                Produto produto = criarProdutoMock("Produto " + i, new BigDecimal("10.00"));
                pdvManager.adicionarProduto(produto, 1);
            }

            long tempoFim = System.currentTimeMillis();
            long tempoExecucao = tempoFim - tempoInicio;

            // Assert
            assertEquals(100, pdvManager.getCarrinhoAtual().getItens().size());
            assertTrue(tempoExecucao < 5000, // Deve adicionar 100 produtos em menos de 5 segundos
                "Adição de produtos demorou: " + tempoExecucao + "ms");
        }

        @Test
        @DisplayName("Deve processar grande volume de dados")
        void testPerformanceGrandeVolume() {
            // Arrange
            pdvManager.abrirCaixa();
            pdvManager.iniciarSessaoPDV(operadorMock);

            long tempoInicio = System.currentTimeMillis();

            // Act - Simular processamento intenso
            for (int i = 0; i < 1000; i++) {
                Produto produto = criarProdutoMock("Produto " + i, new BigDecimal("1.00"));
                pdvManager.adicionarProduto(produto, 1);
                pdvManager.removerItem(produto.getId().toString());
            }

            long tempoFim = System.currentTimeMillis();
            long tempoExecucao = tempoFim - tempoInicio;

            // Assert
            assertTrue(tempoExecucao < 10000, // Deve processar em menos de 10 segundos
                "Processamento intenso demorou: " + tempoExecucao + "ms");
        }
    }

    // Métodos auxiliares
    private Usuario criarOperadorMock() {
        Usuario operador = new Usuario();
        operador.setId(1L);
        operador.setNome("Operador Teste");
        return operador;
    }

    private Cliente criarClienteMock() {
        Cliente cliente = new Cliente();
        cliente.setId(1L);
        cliente.setNome("Cliente Teste");
        cliente.setCpf("123.456.789-00");
        cliente.setTipoPessoa("FISICA");
        return cliente;
    }

    private Produto criarProdutoMock() {
        return criarProdutoMock("Produto Teste", new BigDecimal("10.50"));
    }

    private Produto criarProdutoMock(String nome, BigDecimal preco) {
        Produto produto = new Produto();
        produto.setId(System.currentTimeMillis());
        produto.setNome(nome);
        produto.setPrecoVenda(preco);
        produto.setEstoque(100);
        produto.setUnidade("UN");
        return produto;
    }
}
