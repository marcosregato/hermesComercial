package com.br.hermescomercial.business.pdv;

import com.br.hermescomercial.model.Pagamento;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class PagamentoManagerTest {

    private PagamentoManager pagamentoManager;

    @BeforeEach
    void setUp() {
        pagamentoManager = PagamentoManager.getInstance();
    }

    @Nested
    @DisplayName("Testes de Pagamento Único")
    class PagamentoUnico {

        @Test
        @DisplayName("Deve processar pagamento em dinheiro com sucesso")
        void testProcessarPagamentoUnico_Dinheiro_Sucesso() {
            // Arrange
            String tipoPagamento = "DINHEIRO";
            BigDecimal valorPago = new BigDecimal("150.00");
            BigDecimal valorTotal = new BigDecimal("100.00");

            // Act
            Pagamento pagamento = pagamentoManager.processarPagamentoUnico(tipoPagamento, valorPago, valorTotal);

            // Assert
            assertNotNull(pagamento);
            assertEquals("DINHEIRO", pagamento.getTipoPagamento());
            assertEquals(valorPago, pagamento.getValorPago());
            assertEquals(new BigDecimal("50.00"), pagamento.getValorTroco());
            assertTrue(pagamento.isAprovado());
            assertNotNull(pagamento.getDataPagamento());
        }

        @Test
        @DisplayName("Deve processar pagamento em cartão débito com sucesso")
        void testProcessarPagamentoUnico_CartaoDebito_Sucesso() {
            // Arrange
            String tipoPagamento = "CARTAO_DEBITO";
            BigDecimal valorPago = new BigDecimal("200.00");
            BigDecimal valorTotal = new BigDecimal("200.00");

            // Act
            Pagamento pagamento = pagamentoManager.processarPagamentoUnico(tipoPagamento, valorPago, valorTotal);

            // Assert
            assertNotNull(pagamento);
            assertEquals("CARTAO_DEBITO", pagamento.getTipoPagamento());
            assertEquals(valorPago, pagamento.getValorPago());
            assertNull(pagamento.getValorTroco()); // Não há troco em cartão
            assertTrue(pagamento.isAprovado());
            assertNotNull(pagamento.getNumeroAutorizacao());
            assertNotNull(pagamento.getBandeiraCartao());
        }

        @Test
        @DisplayName("Deve processar pagamento em PIX com sucesso")
        void testProcessarPagamentoUnico_PIX_Sucesso() {
            // Arrange
            // String tipoPagamento = "PIX"; - não utilizado
            BigDecimal valorPago = new BigDecimal("75.50");
            BigDecimal valorTotal = new BigDecimal("75.50");
            String chavePix = "chave-pix-teste@example.com";

            // Act
            Pagamento pagamento = pagamentoManager.processarPagamentoPix(valorTotal, chavePix);

            // Assert
            assertNotNull(pagamento);
            assertEquals("PIX", pagamento.getTipoPagamento());
            assertEquals(valorPago, pagamento.getValorPago());
            assertTrue(pagamento.getObservacao().contains(chavePix));
            assertTrue(pagamento.isAprovado());
            assertNotNull(pagamento.getDataPagamento());
        }

        @Test
        @DisplayName("Deve falhar com forma de pagamento inválida")
        void testProcessarPagamentoUnico_FormaPagamentoInvalida() {
            // Arrange
            String tipoPagamento = "BITCOIN"; // Forma não permitida
            BigDecimal valorPago = new BigDecimal("100.00");
            BigDecimal valorTotal = new BigDecimal("100.00");

            // Act
            Pagamento pagamento = pagamentoManager.processarPagamentoUnico(tipoPagamento, valorPago, valorTotal);

            // Assert
            assertNull(pagamento);
        }

        @Test
        @DisplayName("Deve falhar com valor pago insuficiente")
        void testProcessarPagamentoUnico_ValorInsuficiente() {
            // Arrange
            String tipoPagamento = "DINHEIRO";
            BigDecimal valorPago = new BigDecimal("50.00");
            BigDecimal valorTotal = new BigDecimal("100.00");

            // Act
            Pagamento pagamento = pagamentoManager.processarPagamentoUnico(tipoPagamento, valorPago, valorTotal);

            // Assert
            assertNull(pagamento);
        }

        @Test
        @DisplayName("Deve falhar com valor pago nulo")
        void testProcessarPagamentoUnico_ValorNulo() {
            // Arrange
            String tipoPagamento = "DINHEIRO";
            BigDecimal valorPago = null;
            BigDecimal valorTotal = new BigDecimal("100.00");

            // Act
            Pagamento pagamento = pagamentoManager.processarPagamentoUnico(tipoPagamento, valorPago, valorTotal);

            // Assert
            assertNull(pagamento);
        }
    }

    @Nested
    @DisplayName("Testes de Pagamento Parcelado")
    class PagamentoParcelado {

        @Test
        @DisplayName("Deve processar pagamento parcelado com sucesso")
        void testProcessarPagamentoParcelado_Sucesso() {
            // Arrange
            BigDecimal valorTotal = new BigDecimal("300.00");
            int numeroParcelas = 3;
            String bandeiraCartao = "VISA";

            // Act
            Pagamento pagamento = pagamentoManager.processarPagamentoParcelado(valorTotal, numeroParcelas, bandeiraCartao);

            // Assert
            assertNotNull(pagamento);
            assertEquals("CARTAO_CREDITO", pagamento.getTipoPagamento());
            assertEquals(valorTotal, pagamento.getValorPago());
            assertEquals("3", pagamento.getNumeroParcelas());
            assertEquals("VISA", pagamento.getBandeiraCartao());
            assertTrue(pagamento.isAprovado());
            assertNotNull(pagamento.getNumeroAutorizacao());
            assertNotNull(pagamento.getNsu());
            assertNotNull(pagamento.getDataPagamento());
        }

        @Test
        @DisplayName("Deve processar pagamento parcelado com valor correto")
        void testProcessarPagamentoParcelado_ValorCorreto() {
            // Arrange
            BigDecimal valorTotal = new BigDecimal("150.00");
            int numeroParcelas = 3;
            String bandeiraCartao = "MASTERCARD";

            // Act
            Pagamento pagamento = pagamentoManager.processarPagamentoParcelado(valorTotal, numeroParcelas, bandeiraCartao);

            // Assert
            assertNotNull(pagamento);
            assertEquals(new BigDecimal("50.00"), pagamento.getValorPago().divide(new BigDecimal("3"))); // 150/3 = 50
        }

        @Test
        @DisplayName("Deve falhar com número de parcelas inválido")
        void testProcessarPagamentoParcelado_NumeroParcelasInvalido() {
            // Arrange
            BigDecimal valorTotal = new BigDecimal("100.00");
            int numeroParcelas = 0; // Inválido
            String bandeiraCartao = "VISA";

            // Act
            Pagamento pagamento = pagamentoManager.processarPagamentoParcelado(valorTotal, numeroParcelas, bandeiraCartao);

            // Assert
            assertNull(pagamento);
        }

        @Test
        @DisplayName("Deve falhar com número de parcelas excessivo")
        void testProcessarPagamentoParcelado_NumeroParcelasExcessivo() {
            // Arrange
            BigDecimal valorTotal = new BigDecimal("100.00");
            int numeroParcelas = 13; // Excede o máximo de 12
            String bandeiraCartao = "VISA";

            // Act
            Pagamento pagamento = pagamentoManager.processarPagamentoParcelado(valorTotal, numeroParcelas, bandeiraCartao);

            // Assert
            assertNull(pagamento);
        }
    }

    @Nested
    @DisplayName("Testes de Múltiplos Pagamentos")
    class MultiplosPagamentos {

        @Test
        @DisplayName("Deve processar múltiplos pagamentos com sucesso")
        void testProcessarMultiplosPagamentos_Sucesso() {
            // Arrange
            List<Pagamento> pagamentos = criarListaPagamentos();
            BigDecimal valorTotal = new BigDecimal("200.00");

            // Act
            boolean resultado = pagamentoManager.processarMultiplosPagamentos(pagamentos, valorTotal);

            // Assert
            assertTrue(resultado);
            
            // Verificar se todos foram aprovados
            for (Pagamento pgto : pagamentos) {
                assertTrue(pgto.isAprovado());
            }
        }

        @Test
        @DisplayName("Deve falhar com valor total insuficiente")
        void testProcessarMultiplosPagamentos_ValorInsuficiente() {
            // Arrange
            List<Pagamento> pagamentos = criarListaPagamentos();
            BigDecimal valorTotal = new BigDecimal("500.00"); // Maior que a soma

            // Act
            boolean resultado = pagamentoManager.processarMultiplosPagamentos(pagamentos, valorTotal);

            // Assert
            assertFalse(resultado);
        }

        @Test
        @DisplayName("Deve falhar com forma de pagamento inválida")
        void testProcessarMultiplosPagamentos_FormaInvalida() {
            // Arrange
            List<Pagamento> pagamentos = new ArrayList<>();
            Pagamento pagamentoInvalido = new Pagamento();
            pagamentoInvalido.setTipoPagamento("BITCOIN"); // Inválido
            pagamentoInvalido.setValorPago(new BigDecimal("100.00"));
            pagamentoInvalido.aprovar();
            pagamentos.add(pagamentoInvalido);
            
            BigDecimal valorTotal = new BigDecimal("100.00");

            // Act
            boolean resultado = pagamentoManager.processarMultiplosPagamentos(pagamentos, valorTotal);

            // Assert
            assertFalse(resultado);
        }
    }

    @Nested
    @DisplayName("Testes de Cancelamento")
    class Cancelamento {

        @Test
        @DisplayName("Deve cancelar pagamento ativo com sucesso")
        void testCancelarPagamento_Ativo_Sucesso() {
            // Arrange
            Pagamento pagamento = criarPagamentoMock();
            pagamento.aprovar();

            // Act
            boolean resultado = pagamentoManager.cancelarPagamento(pagamento);

            // Assert
            assertTrue(resultado);
            assertFalse(pagamento.isAprovado());
        }

        @Test
        @DisplayName("Não deve cancelar pagamento já cancelado")
        void testCancelarPagamento_JaCancelado() {
            // Arrange
            Pagamento pagamento = criarPagamentoMock();
            pagamento.cancelar();

            // Act
            boolean resultado = pagamentoManager.cancelarPagamento(pagamento);

            // Assert
            assertFalse(resultado);
            assertFalse(pagamento.isAprovado()); // Permanece cancelado
        }

        @Test
        @DisplayName("Deve falhar ao cancelar pagamento nulo")
        void testCancelarPagamento_Nulo() {
            // Act
            boolean resultado = pagamentoManager.cancelarPagamento(null);

            // Assert
            assertFalse(resultado);
        }
    }

    @Nested
    @DisplayName("Testes de Configuração")
    class Configuracao {

        @Test
        @DisplayName("Deve obter formas de pagamento permitidas")
        void testGetFormasPagamentoPermitidas() {
            // Act
            List<String> formas = pagamentoManager.getFormasPagamentoPermitidas();

            // Assert
            assertNotNull(formas);
            assertTrue(formas.contains("DINHEIRO"));
            assertTrue(formas.contains("CARTAO_DEBITO"));
            assertTrue(formas.contains("CARTAO_CREDITO"));
            assertTrue(formas.contains("PIX"));
            assertTrue(formas.contains("TRANSFERENCIA"));
            assertEquals(5, formas.size());
        }

        @Test
        @DisplayName("Deve adicionar nova forma de pagamento")
        void testAdicionarFormaPagamento() {
            // Arrange
            String novaForma = "CRIPTOMOEDA";
            int tamanhoAntes = pagamentoManager.getFormasPagamentoPermitidas().size();

            // Act
            pagamentoManager.adicionarFormaPagamento(novaForma);

            // Assert
            List<String> formas = pagamentoManager.getFormasPagamentoPermitidas();
            assertTrue(formas.contains(novaForma));
            assertEquals(tamanhoAntes + 1, formas.size());
        }

        @Test
        @DisplayName("Deve remover forma de pagamento existente")
        void testRemoverFormaPagamento() {
            // Arrange
            String formaRemover = "TRANSFERENCIA";
            int tamanhoAntes = pagamentoManager.getFormasPagamentoPermitidas().size();

            // Act
            pagamentoManager.removerFormaPagamento(formaRemover);

            // Assert
            List<String> formas = pagamentoManager.getFormasPagamentoPermitidas();
            assertFalse(formas.contains(formaRemover));
            assertEquals(tamanhoAntes - 1, formas.size());
        }

        @Test
        @DisplayName("Não deve adicionar forma de pagamento duplicada")
        void testAdicionarFormaPagamento_Duplicada() {
            // Arrange
            String forma = "DINHEIRO"; // Já existe
            int tamanhoAntes = pagamentoManager.getFormasPagamentoPermitidas().size();

            // Act
            pagamentoManager.adicionarFormaPagamento(forma);

            // Assert
            List<String> formas = pagamentoManager.getFormasPagamentoPermitidas();
            assertEquals(tamanhoAntes, formas.size()); // Não adiciona duplicado
        }

        @Test
        @DisplayName("Deve configurar valor mínimo de pagamento")
        void testSetValorMinimoPagamento() {
            // Arrange
            BigDecimal novoValorMinimo = new BigDecimal("5.00");

            // Act
            pagamentoManager.setValorMinimoPagamento(novoValorMinimo);

            // Assert
            assertEquals(novoValorMinimo, pagamentoManager.getValorMinimoPagamento());
        }

        @Test
        @DisplayName("Deve configurar máximo de parcelas")
        void testSetMaximoParcelas() {
            // Arrange
            int novoMaximo = 18;

            // Act
            pagamentoManager.setMaximoParcelas(novoMaximo);

            // Assert
            assertEquals(novoMaximo, pagamentoManager.getMaximoParcelas());
        }

        @Test
        @DisplayName("Deve configurar múltiplos pagamentos")
        void testSetPermiteMultiplosPagamentos() {
            // Arrange
            boolean permite = false;

            // Act
            pagamentoManager.setPermiteMultiplosPagamentos(permite);

            // Assert
            assertEquals(permite, pagamentoManager.isPermiteMultiplosPagamentos());
        }
    }

    @Nested
    @DisplayName("Testes de Validação")
    class Validacao {

        @Test
        @DisplayName("Deve validar valor mínimo padrão")
        void testValorMinimoPadrao() {
            // Assert
            assertEquals(new BigDecimal("0.01"), pagamentoManager.getValorMinimoPagamento());
        }

        @Test
        @DisplayName("Deve permitir múltiplos pagamentos por padrão")
        void testPermiteMultiplosPadrao() {
            // Assert
            assertTrue(pagamentoManager.isPermiteMultiplosPagamentos());
        }

        @Test
        @DisplayName("Deve ter máximo de parcelas padrão")
        void testMaximoParcelasPadrao() {
            // Assert
            assertEquals(12, pagamentoManager.getMaximoParcelas());
        }
    }

    // Métodos auxiliares
    private List<Pagamento> criarListaPagamentos() {
        List<Pagamento> pagamentos = new ArrayList<>();
        
        Pagamento pagamento1 = new Pagamento();
        pagamento1.setTipoPagamento("DINHEIRO");
        pagamento1.setValorPago(new BigDecimal("100.00"));
        pagamento1.aprovar();
        pagamentos.add(pagamento1);
        
        Pagamento pagamento2 = new Pagamento();
        pagamento2.setTipoPagamento("CARTAO_CREDITO");
        pagamento2.setValorPago(new BigDecimal("100.00"));
        pagamento2.aprovar();
        pagamentos.add(pagamento2);
        
        return pagamentos;
    }

    private Pagamento criarPagamentoMock() {
        Pagamento pagamento = new Pagamento();
        pagamento.setTipoPagamento("DINHEIRO");
        pagamento.setValorPago(new BigDecimal("150.00"));
        pagamento.setDataPagamento(LocalDateTime.now());
        pagamento.aprovar();
        return pagamento;
    }
}
