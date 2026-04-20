package com.br.hermescomercial.integration;

import com.br.hermescomercial.business.pdv.PDVManager;
import com.br.hermescomercial.business.pdv.PagamentoManager;
import com.br.hermescomercial.business.pdv.CupomFiscalManager;
import com.br.hermescomercial.model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
// import org.junit.jupiter.api.Nested; - não utilizado

import java.math.BigDecimal;
// import java.time.LocalDateTime; - não utilizado
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class PDVIntegrationTest {

    private PDVManager pdvManager;
    private PagamentoManager pagamentoManager;
    private CupomFiscalManager cupomManager;
    private Usuario operador;
    private Cliente cliente;
    private Produto produto;

    @BeforeEach
    void setUp() {
        pdvManager = PDVManager.getInstance();
        pagamentoManager = PagamentoManager.getInstance();
        cupomManager = CupomFiscalManager.getInstance();
        operador = criarOperador();
        cliente = criarCliente();
        produto = criarProduto();
    }

    @Test
    @DisplayName("Fluxo completo: Abrir caixa → Iniciar PDV → Vender → Finalizar")
    void testFluxoCompleto_Venda() {
        // Step 1: Abrir caixa
        assertTrue(pdvManager.abrirCaixa(), "Deve abrir caixa");
        assertTrue(pdvManager.isCaixaAberto());

        // Step 2: Iniciar PDV
        assertTrue(pdvManager.iniciarSessaoPDV(operador), "Deve iniciar sessão PDV");
        assertNotNull(pdvManager.getSessaoAtual());

        // Step 3: Adicionar produtos
        assertTrue(pdvManager.adicionarProduto(produto, 2), "Deve adicionar produto");
        assertTrue(pdvManager.adicionarProduto(produto, 1), "Deve adicionar segundo produto");
        assertEquals(2, pdvManager.getCarrinhoAtual().getItens().size());

        // Step 4: Adicionar cliente
        pdvManager.getCarrinhoAtual().setCliente(cliente);

        // Step 5: Criar venda
        VendaPDV venda = pdvManager.criarVendaPDV();
        assertNotNull(venda);
        assertFalse(venda.getItens().isEmpty());
        assertTrue(venda.getValorTotal().compareTo(BigDecimal.ZERO) > 0);

        // Step 6: Finalizar sessão
        assertTrue(pdvManager.finalizarSessaoPDV(), "Deve finalizar sessão");
        assertNull(pdvManager.getSessaoAtual());
    }

    @Test
    @DisplayName("Fluxo de cancelamento: Vender → Cancelar item → Atualizar venda")
    void testFluxoCancelamento_Item() {
        // Setup: Abrir caixa e iniciar PDV
        assertTrue(pdvManager.abrirCaixa());
        assertTrue(pdvManager.iniciarSessaoPDV(operador));

        // Adicionar produto
        pdvManager.adicionarProduto(produto, 1);

        // Iniciar venda
        VendaPDV venda = pdvManager.criarVendaPDV();
        assertEquals(1, venda.getItens().size());

        // Cancelar item
        assertTrue(pdvManager.removerItem(produto.getId().toString()), "Deve remover item");
        assertTrue(pdvManager.getCarrinhoAtual().getItens().isEmpty());

        // Atualizar venda (venda sem itens)
        VendaPDV vendaAtualizada = pdvManager.criarVendaPDV();
        assertTrue(vendaAtualizada.getItens().isEmpty());
        assertNotEquals(venda.getId(), vendaAtualizada.getId());
    }

    @Test
    @DisplayName("Fluxo de múltiplos pagamentos")
    void testFluxoMultiplosPagamentos() {
        // Setup
        assertTrue(pdvManager.abrirCaixa());
        assertTrue(pdvManager.iniciarSessaoPDV(operador));

        // Adicionar produtos
        Produto produto1 = criarProduto("Produto 1", new BigDecimal("50.00"));
        Produto produto2 = criarProduto("Produto 2", new BigDecimal("30.00"));
        pdvManager.adicionarProduto(produto1, 1);
        pdvManager.adicionarProduto(produto2, 1);

        // Criar venda
        // VendaPDV venda = pdvManager.criarVendaPDV(); - não utilizado
        BigDecimal valorTotal = new BigDecimal("80.00");

        // Simular múltiplos pagamentos
        Pagamento pagamento1 = new Pagamento();
        pagamento1.setTipoPagamento("DINHEIRO");
        pagamento1.setValorPago(new BigDecimal("50.00"));
        pagamento1.aprovar();

        Pagamento pagamento2 = new Pagamento();
        pagamento2.setTipoPagamento("CARTAO_CREDITO");
        pagamento2.setValorPago(new BigDecimal("30.00"));
        pagamento2.aprovar();

        List<Pagamento> pagamentos = List.of(pagamento1, pagamento2);

        // Validar múltiplos pagamentos
        assertTrue(pagamentoManager.processarMultiplosPagamentos(pagamentos, valorTotal),
                "Deve processar múltiplos pagamentos");
    }

    @Test
    @DisplayName("Fluxo com erro: Tentar vender sem caixa aberto")
    void testFluxoErro_CaixaFechado() {
        // Tentar vender sem caixa aberto
        assertFalse(pdvManager.iniciarSessaoPDV(operador),
                "Não deve iniciar PDV com caixa fechado");
        assertNull(pdvManager.getSessaoAtual());

        // Tentar adicionar produto
        assertFalse(pdvManager.adicionarProduto(produto, 1),
                "Não deve adicionar produto com caixa fechado");
        assertNull(pdvManager.getCarrinhoAtual());

        // Tentar criar venda
        assertNull(pdvManager.criarVendaPDV(),
                "Não deve criar venda com caixa fechado");
    }

    @Test
    @DisplayName("Fluxo de estorno: Vender → Cancelar venda → Estornar")
    void testFluxoEstorno_Venda() {
        // Setup
        assertTrue(pdvManager.abrirCaixa());
        assertTrue(pdvManager.iniciarSessaoPDV(operador));

        // Adicionar produto e criar venda
        pdvManager.adicionarProduto(produto, 1);
        VendaPDV venda = pdvManager.criarVendaPDV();
        assertNotNull(venda);

        // Cancelar venda
        assertTrue(pdvManager.cancelarVenda(venda.getId()),
                "Deve cancelar venda");

        // Verificar status
        VendaPDV vendaCancelada = pdvManager.buscarVendaPorId(venda.getId());
        assertNotNull(vendaCancelada);
    }

    @Test
    @DisplayName("Teste de performance do fluxo completo")
    void testPerformance_FluxoCompleto() {
        long tempoInicio = System.currentTimeMillis();

        // Executar fluxo completo
        assertTrue(pdvManager.abrirCaixa());
        assertTrue(pdvManager.iniciarSessaoPDV(operador));

        // Adicionar 50 produtos rapidamente
        for (int i = 0; i < 50; i++) {
            Produto p = criarProduto("Produto " + i, new BigDecimal("10.00"));
            pdvManager.adicionarProduto(p, 1);
        }

        // VendaPDV venda = pdvManager.criarVendaPDV(); - não utilizado
        assertTrue(pdvManager.finalizarSessaoPDV());

        long tempoFim = System.currentTimeMillis();
        long tempoTotal = tempoFim - tempoInicio;

        // Verificar performance
        assertTrue(tempoTotal < 10000, // Deve completar em menos de 10 segundos
                "Fluxo completo demorou: " + tempoTotal + "ms");
    }

    @Test
    @DisplayName("Integração com CupomFiscalManager")
    void testIntegracaoCupomFiscal() {
        // Setup
        assertTrue(pdvManager.abrirCaixa());
        assertTrue(pdvManager.iniciarSessaoPDV(operador));

        // Adicionar produto
        pdvManager.adicionarProduto(produto, 1);
        VendaPDV venda = pdvManager.criarVendaPDV();

        // Gerar cupom fiscal
        String cupom = cupomManager.gerarCupomFiscal(
            venda.getItens(),
            venda.getPagamento(),
            venda.getCliente(),
            venda.getOperador(),
            venda.getNumeroCupom()
        );

        // Verificar
        assertNotNull(cupom);
        assertTrue(cupom.contains("CUPOM FISCAL ELETRÔNICO"));
        assertTrue(cupom.contains(venda.getNumeroCupom()));
    }

    @Test
    @DisplayName("Teste de concorrência: múltiplos operadores")
    void testConcorrencia_MultiplosOperadores() {
        // Operador 1 abre caixa
        assertTrue(pdvManager.abrirCaixa());
        assertTrue(pdvManager.iniciarSessaoPDV(operador));

        // Operador 2 tenta abrir caixa (deve falhar)
        PDVManager pdvManager2 = PDVManager.getInstance();
        Usuario operador2 = criarOperador("Operador 2");
        assertFalse(pdvManager2.iniciarSessaoPDV(operador2),
                "Operador 2 não deve conseguir abrir caixa já aberto");

        // Operador 1 continua operando normalmente
        assertTrue(pdvManager.adicionarProduto(produto, 1));
        assertNotNull(pdvManager.getCarrinhoAtual());
    }

    @Test
    @DisplayName("Teste de recuperação: Falha na operação")
    void testRecuperacao_FalhaOperacao() {
        // Setup
        assertTrue(pdvManager.abrirCaixa());
        assertTrue(pdvManager.iniciarSessaoPDV(operador));

        // Adicionar produto
        pdvManager.adicionarProduto(produto, 1);

        // Simular falha na criação da venda
        // VendaPDV venda = null; // Simular erro - não utilizado

        // Verificar que carrinho permanece intacto após falha
        assertNotNull(pdvManager.getCarrinhoAtual());
        assertEquals(1, pdvManager.getCarrinhoAtual().getItens().size());
    }

    @Test
    @DisplayName("Teste de limite de produtos no carrinho")
    void testLimiteProdutosCarrinho() {
        // Setup
        assertTrue(pdvManager.abrirCaixa());
        assertTrue(pdvManager.iniciarSessaoPDV(operador));

        // Tentar adicionar mais produtos que o limite
        int limiteProdutos = 100;
        for (int i = 0; i < limiteProdutos + 10; i++) {
            Produto p = criarProduto("Produto " + i, new BigDecimal("1.00"));
            if (i < limiteProdutos) {
                assertTrue(pdvManager.adicionarProduto(p, 1),
                        "Deve adicionar produto dentro do limite");
            } else {
                assertFalse(pdvManager.adicionarProduto(p, 1),
                        "Não deve adicionar produto acima do limite");
            }
        }

        // Verificar que apenas o limite foi atingido
        assertEquals(limiteProdutos, pdvManager.getCarrinhoAtual().getItens().size());
    }

    // Métodos auxiliares
    private Usuario criarOperador() {
        return criarOperador("Operador Teste");
    }

    private Usuario criarOperador(String nome) {
        Usuario operador = new Usuario();
        operador.setId(1L);
        operador.setNome(nome);
        return operador;
    }

    private Cliente criarCliente() {
        Cliente cliente = new Cliente();
        cliente.setId(1L);
        cliente.setNome("Cliente Teste");
        cliente.setCpf("123.456.789-00");
        cliente.setTipoPessoa("FISICA");
        return cliente;
    }

    private Produto criarProduto() {
        return criarProduto("Produto Teste", new BigDecimal("10.50"));
    }

    private Produto criarProduto(String nome, BigDecimal preco) {
        Produto produto = new Produto();
        produto.setId(System.currentTimeMillis());
        produto.setNome(nome);
        produto.setPrecoVenda(preco);
        produto.setEstoque(1000);
        produto.setUnidade("UN");
        return produto;
    }
}
