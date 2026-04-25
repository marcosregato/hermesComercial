package com.br.hermescomercial.service;

import com.br.hermescomercial.business.pdv.PDVManager;
import com.br.hermescomercial.business.pdv.PagamentoManager;
import com.br.hermescomercial.business.pdv.ImpressoraManager;
import com.br.hermescomercial.model.VendaPDV;
import com.br.hermescomercial.model.Usuario;
import com.br.hermescomercial.model.Produto;
import com.br.hermescomercial.model.ItemVenda;
import com.br.hermescomercial.model.Pagamento;
import com.br.hermescomercial.model.CarrinhoCompras;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.MockedStatic;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class VendaServiceTest {

    private VendaService vendaService;
    private Usuario operador;
    private Produto produto;

    @BeforeEach
    void setUp() {
        vendaService = new VendaService();
        operador = new Usuario();
        operador.setNome("Operador Teste");
        operador.setId(1L);

        produto = new Produto();
        produto.setCodigo("001");
        produto.setNome("Produto Teste");
        produto.setPrecoVenda(new BigDecimal("10.00"));
        produto.setEstoque(100);
    }

    @Test
    @DisplayName("Deve iniciar nova venda com sucesso")
    void testIniciarNovaVenda() {
        VendaPDV venda = vendaService.iniciarNovaVenda(operador);

        assertNotNull(venda);
        assertNotNull(venda.getNumeroCupom());
        assertTrue(venda.getNumeroCupom().startsWith("V"));
        assertNotNull(venda.getDataVenda());
        assertEquals(operador, venda.getOperador());
        assertEquals("EM_ANDAMENTO", venda.getStatus());
    }

    @Test
    @DisplayName("Deve gerar número de cupom único")
    void testNumeroCupomUnico() throws InterruptedException {
        VendaPDV venda1 = vendaService.iniciarNovaVenda(operador);
        Thread.sleep(1); // Pequena pausa para garantir timestamp diferente
        VendaPDV venda2 = vendaService.iniciarNovaVenda(operador);

        assertNotEquals(venda1.getNumeroCupom(), venda2.getNumeroCupom());
    }

    @Test
    @DisplayName("Deve adicionar produto com sucesso")
    void testAdicionarProdutoSucesso() {
        try (MockedStatic<PDVManager> mockedPDV = mockStatic(PDVManager.class)) {
            PDVManager mockManager = mock(PDVManager.class);
            mockedPDV.when(PDVManager::getInstance).thenReturn(mockManager);
            
            when(mockManager.buscarProdutoPorCodigo("001")).thenReturn(produto);
            when(mockManager.adicionarProduto(produto, 2)).thenReturn(true);

            VendaService service = new VendaService();
            boolean resultado = service.adicionarProduto("001", 2);

            assertTrue(resultado);
            verify(mockManager).buscarProdutoPorCodigo("001");
            verify(mockManager).adicionarProduto(produto, 2);
        }
    }

    @Test
    @DisplayName("Deve falhar ao adicionar produto não encontrado")
    void testAdicionarProdutoNaoEncontrado() {
        try (MockedStatic<PDVManager> mockedPDV = mockStatic(PDVManager.class)) {
            PDVManager mockManager = mock(PDVManager.class);
            mockedPDV.when(PDVManager::getInstance).thenReturn(mockManager);
            
            when(mockManager.buscarProdutoPorCodigo("999")).thenReturn(null);

            VendaService service = new VendaService();
            boolean resultado = service.adicionarProduto("999", 1);

            assertFalse(resultado);
            verify(mockManager).buscarProdutoPorCodigo("999");
            verify(mockManager, never()).adicionarProduto(any(), anyInt());
        }
    }

    @Test
    @DisplayName("Deve falhar ao adicionar produto sem estoque")
    void testAdicionarProdutoSemEstoque() {
        produto.setEstoque(0);

        try (MockedStatic<PDVManager> mockedPDV = mockStatic(PDVManager.class)) {
            PDVManager mockManager = mock(PDVManager.class);
            mockedPDV.when(PDVManager::getInstance).thenReturn(mockManager);
            
            when(mockManager.buscarProdutoPorCodigo("001")).thenReturn(produto);

            VendaService service = new VendaService();
            boolean resultado = service.adicionarProduto("001", 1);

            assertFalse(resultado);
            verify(mockManager).buscarProdutoPorCodigo("001");
            verify(mockManager, never()).adicionarProduto(any(), anyInt());
        }
    }

    @Test
    @DisplayName("Deve falhar ao adicionar produto com quantidade maior que estoque")
    void testAdicionarProdutoQuantidadeMaiorEstoque() {
        produto.setEstoque(5);

        try (MockedStatic<PDVManager> mockedPDV = mockStatic(PDVManager.class)) {
            PDVManager mockManager = mock(PDVManager.class);
            mockedPDV.when(PDVManager::getInstance).thenReturn(mockManager);
            
            when(mockManager.buscarProdutoPorCodigo("001")).thenReturn(produto);

            VendaService service = new VendaService();
            boolean resultado = service.adicionarProduto("001", 10);

            assertFalse(resultado);
            verify(mockManager).buscarProdutoPorCodigo("001");
            verify(mockManager, never()).adicionarProduto(any(), anyInt());
        }
    }

    @Test
    @DisplayName("Deve remover item com sucesso")
    void testRemoverItemSucesso() {
        try (MockedStatic<PDVManager> mockedPDV = mockStatic(PDVManager.class)) {
            PDVManager mockManager = mock(PDVManager.class);
            mockedPDV.when(PDVManager::getInstance).thenReturn(mockManager);
            
            when(mockManager.removerItem("001")).thenReturn(true);

            VendaService service = new VendaService();
            boolean resultado = service.removerItem("001");

            assertTrue(resultado);
            verify(mockManager).removerItem("001");
        }
    }

    @Test
    @DisplayName("Deve falhar ao remover item inexistente")
    void testRemoverItemInexistente() {
        try (MockedStatic<PDVManager> mockedPDV = mockStatic(PDVManager.class)) {
            PDVManager mockManager = mock(PDVManager.class);
            mockedPDV.when(PDVManager::getInstance).thenReturn(mockManager);
            
            when(mockManager.removerItem("999")).thenReturn(false);

            VendaService service = new VendaService();
            boolean resultado = service.removerItem("999");

            assertFalse(resultado);
            verify(mockManager).removerItem("999");
        }
    }

    @Test
    @DisplayName("Deve aplicar desconto em item com sucesso")
    void testAplicarDescontoItemSucesso() {
        BigDecimal desconto = new BigDecimal("1.00");

        try (MockedStatic<PDVManager> mockedPDV = mockStatic(PDVManager.class)) {
            PDVManager mockManager = mock(PDVManager.class);
            mockedPDV.when(PDVManager::getInstance).thenReturn(mockManager);
            
            when(mockManager.aplicarDescontoItem("001", desconto)).thenReturn(true);

            VendaService service = new VendaService();
            boolean resultado = service.aplicarDescontoItem("001", desconto);

            assertTrue(resultado);
            verify(mockManager).aplicarDescontoItem("001", desconto);
        }
    }

    @Test
    @DisplayName("Deve processar pagamento com sucesso")
    void testProcessarPagamentoSucesso() {
        BigDecimal valorPago = new BigDecimal("10.00");
        BigDecimal valorRecebido = new BigDecimal("10.00");
        Pagamento pagamentoEsperado = new Pagamento();
        pagamentoEsperado.setTipoPagamento("DINHEIRO");

        try (MockedStatic<PagamentoManager> mockedPagamento = mockStatic(PagamentoManager.class)) {
            PagamentoManager mockManager = mock(PagamentoManager.class);
            mockedPagamento.when(PagamentoManager::getInstance).thenReturn(mockManager);
            
            when(mockManager.processarPagamentoUnico("DINHEIRO", valorPago, valorRecebido))
                .thenReturn(pagamentoEsperado);

            VendaService service = new VendaService();
            Pagamento resultado = service.processarPagamento("DINHEIRO", valorPago, valorRecebido);

            assertNotNull(resultado);
            assertEquals(pagamentoEsperado, resultado);
            verify(mockManager).processarPagamentoUnico("DINHEIRO", valorPago, valorRecebido);
        }
    }

    @Test
    @DisplayName("Deve retornar null ao processar pagamento com erro")
    void testProcessarPagamentoComErro() {
        try (MockedStatic<PagamentoManager> mockedPagamento = mockStatic(PagamentoManager.class)) {
            PagamentoManager mockManager = mock(PagamentoManager.class);
            mockedPagamento.when(PagamentoManager::getInstance).thenReturn(mockManager);
            
            when(mockManager.processarPagamentoUnico(any(), any(), any()))
                .thenThrow(new RuntimeException("Erro no pagamento"));

            VendaService service = new VendaService();
            Pagamento resultado = service.processarPagamento("CARTAO", 
                new BigDecimal("10.00"), new BigDecimal("10.00"));

            assertNull(resultado);
        }
    }

    @Test
    @DisplayName("Deve finalizar venda com sucesso")
    void testFinalizarVendaSucesso() {
        Pagamento pagamento = new Pagamento();
        pagamento.setTipoPagamento("DINHEIRO");

        CarrinhoCompras carrinho = mock(CarrinhoCompras.class);
        ItemVenda item = new ItemVenda();
        item.setValorTotal(new BigDecimal("10.00"));
        item.setDesconto(BigDecimal.ZERO);

        when(carrinho.getItens()).thenReturn(Arrays.asList(item));
        when(carrinho.getValorFinal()).thenReturn(new BigDecimal("10.00"));
        when(carrinho.estaAberto()).thenReturn(true);

        try (MockedStatic<PDVManager> mockedPDV = mockStatic(PDVManager.class);
             MockedStatic<ImpressoraManager> mockedImpressora = mockStatic(ImpressoraManager.class)) {
            
            PDVManager mockPDV = mock(PDVManager.class);
            ImpressoraManager mockImpressora = mock(ImpressoraManager.class);
            
            mockedPDV.when(PDVManager::getInstance).thenReturn(mockPDV);
            mockedImpressora.when(ImpressoraManager::getInstance).thenReturn(mockImpressora);
            
            when(mockPDV.getCarrinhoAtual()).thenReturn(carrinho);

            VendaService service = new VendaService();
            boolean resultado = service.finalizarVenda(pagamento);

            assertTrue(resultado);
            verify(mockImpressora).imprimirCupom(contains("CUPOM FISCAL"));
        }
    }

    @Test
    @DisplayName("Deve falhar ao finalizar venda sem carrinho")
    void testFinalizarVendaSemCarrinho() {
        try (MockedStatic<PDVManager> mockedPDV = mockStatic(PDVManager.class)) {
            PDVManager mockManager = mock(PDVManager.class);
            mockedPDV.when(PDVManager::getInstance).thenReturn(mockManager);
            
            when(mockManager.getCarrinhoAtual()).thenReturn(null);

            VendaService service = new VendaService();
            boolean resultado = service.finalizarVenda(new Pagamento());

            assertFalse(resultado);
        }
    }

    @Test
    @DisplayName("Deve cancelar venda com sucesso")
    void testCancelarVendaSucesso() {
        try (MockedStatic<PDVManager> mockedPDV = mockStatic(PDVManager.class)) {
            PDVManager mockManager = mock(PDVManager.class);
            mockedPDV.when(PDVManager::getInstance).thenReturn(mockManager);
            
            when(mockManager.cancelarVenda()).thenReturn(true);

            VendaService service = new VendaService();
            boolean resultado = service.cancelarVenda();

            assertTrue(resultado);
            verify(mockManager).cancelarVenda();
        }
    }

    @Test
    @DisplayName("Deve obter resumo da venda com sucesso")
    void testObterResumoVendaSucesso() {
        ItemVenda item = new ItemVenda();
        item.setValorTotal(new BigDecimal("20.00"));
        item.setDesconto(new BigDecimal("2.00"));

        CarrinhoCompras carrinho = mock(CarrinhoCompras.class);
        when(carrinho.getItens()).thenReturn(Arrays.asList(item));
        when(carrinho.getValorFinal()).thenReturn(new BigDecimal("18.00"));
        when(carrinho.estaAberto()).thenReturn(true);

        try (MockedStatic<PDVManager> mockedPDV = mockStatic(PDVManager.class)) {
            PDVManager mockManager = mock(PDVManager.class);
            mockedPDV.when(PDVManager::getInstance).thenReturn(mockManager);
            
            when(mockManager.getCarrinhoAtual()).thenReturn(carrinho);
            when(mockManager.getOperadorAtual()).thenReturn(operador);

            VendaService service = new VendaService();
            VendaService.VendaResumo resumo = service.obterResumoVenda();

            assertNotNull(resumo);
            assertEquals(1, resumo.getQuantidadeItens());
            assertEquals(new BigDecimal("20.00"), resumo.getSubtotal());
            assertEquals(new BigDecimal("2.00"), resumo.getDescontoTotal());
            assertEquals(new BigDecimal("18.00"), resumo.getValorTotal());
            assertEquals("EM_ANDAMENTO", resumo.getStatus());
            assertEquals(operador, resumo.getOperador());
        }
    }

    @Test
    @DisplayName("Deve retornar null ao obter resumo sem carrinho")
    void testObterResumoVendaSemCarrinho() {
        try (MockedStatic<PDVManager> mockedPDV = mockStatic(PDVManager.class)) {
            PDVManager mockManager = mock(PDVManager.class);
            mockedPDV.when(PDVManager::getInstance).thenReturn(mockManager);
            
            when(mockManager.getCarrinhoAtual()).thenReturn(null);

            VendaService service = new VendaService();
            VendaService.VendaResumo resumo = service.obterResumoVenda();

            assertNull(resumo);
        }
    }

    @Test
    @DisplayName("Deve obter resumo com carrinho vazio")
    void testObterResumoVendaCarrinhoVazio() {
        CarrinhoCompras carrinho = mock(CarrinhoCompras.class);
        when(carrinho.getItens()).thenReturn(Collections.emptyList());
        when(carrinho.getValorFinal()).thenReturn(BigDecimal.ZERO);
        when(carrinho.estaAberto()).thenReturn(false);

        try (MockedStatic<PDVManager> mockedPDV = mockStatic(PDVManager.class)) {
            PDVManager mockManager = mock(PDVManager.class);
            mockedPDV.when(PDVManager::getInstance).thenReturn(mockManager);
            
            when(mockManager.getCarrinhoAtual()).thenReturn(carrinho);
            when(mockManager.getOperadorAtual()).thenReturn(operador);

            VendaService service = new VendaService();
            VendaService.VendaResumo resumo = service.obterResumoVenda();

            assertNotNull(resumo);
            assertEquals(0, resumo.getQuantidadeItens());
            assertEquals(BigDecimal.ZERO, resumo.getSubtotal());
            assertEquals(BigDecimal.ZERO, resumo.getDescontoTotal());
            assertEquals(BigDecimal.ZERO, resumo.getValorTotal());
            assertEquals("FINALIZADA", resumo.getStatus());
        }
    }

    @ParameterizedTest
    @ValueSource(strings = {"DINHEIRO", "CARTAO", "PIX", "CHEQUE"})
    @DisplayName("Deve processar diferentes formas de pagamento")
    void testProcessarDiferentesFormasPagamento(String formaPagamento) {
        BigDecimal valorPago = new BigDecimal("100.00");
        BigDecimal valorRecebido = new BigDecimal("100.00");
        Pagamento pagamento = new Pagamento();
        pagamento.setTipoPagamento(formaPagamento);

        try (MockedStatic<PagamentoManager> mockedPagamento = mockStatic(PagamentoManager.class)) {
            PagamentoManager mockManager = mock(PagamentoManager.class);
            mockedPagamento.when(PagamentoManager::getInstance).thenReturn(mockManager);
            
            when(mockManager.processarPagamentoUnico(formaPagamento, valorPago, valorRecebido))
                .thenReturn(pagamento);

            VendaService service = new VendaService();
            Pagamento resultado = service.processarPagamento(formaPagamento, valorPago, valorRecebido);

            assertNotNull(resultado);
            assertEquals(formaPagamento, resultado.getTipoPagamento());
        }
    }

    @Test
    @DisplayName("Deve lidar com exceções em adicionar produto")
    void testAdicionarProdutoComExcecao() {
        try (MockedStatic<PDVManager> mockedPDV = mockStatic(PDVManager.class)) {
            PDVManager mockManager = mock(PDVManager.class);
            mockedPDV.when(PDVManager::getInstance).thenReturn(mockManager);
            
            when(mockManager.buscarProdutoPorCodigo(any()))
                .thenThrow(new RuntimeException("Erro de conexão"));

            VendaService service = new VendaService();
            boolean resultado = service.adicionarProduto("001", 1);

            assertFalse(resultado);
        }
    }

    @Test
    @DisplayName("VendaResumo deve manter valores imutáveis")
    void testVendaResumoImutabilidade() {
        LocalDateTime data = LocalDateTime.now();
        VendaService.VendaResumo resumo = new VendaService.VendaResumo(
            "CUPOM001", data, operador, 2,
            new BigDecimal("50.00"), new BigDecimal("5.00"),
            new BigDecimal("45.00"), "FINALIZADA"
        );

        assertEquals("CUPOM001", resumo.getNumeroCupom());
        assertEquals(data, resumo.getDataVenda());
        assertEquals(operador, resumo.getOperador());
        assertEquals(2, resumo.getQuantidadeItens());
        assertEquals(new BigDecimal("50.00"), resumo.getSubtotal());
        assertEquals(new BigDecimal("5.00"), resumo.getDescontoTotal());
        assertEquals(new BigDecimal("45.00"), resumo.getValorTotal());
        assertEquals("FINALIZADA", resumo.getStatus());
    }
}
