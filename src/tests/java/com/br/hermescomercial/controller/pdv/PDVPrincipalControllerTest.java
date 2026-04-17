package com.br.hermescomercial.controller.pdv;

import com.br.hermescomercial.model.CarrinhoCompras;
import com.br.hermescomercial.model.Produto;
import com.br.hermescomercial.model.Cliente;
import com.br.hermescomercial.model.Usuario;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

public class PDVPrincipalControllerTest {

    @Mock
    private CarrinhoCompras carrinhoCompras;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("Deve criar controller sem erros")
    void testCriarController() {
        PDVPrincipalController controller = new PDVPrincipalController();
        assertNotNull(controller);
    }

    @Test
    @DisplayName("Deve criar produto válido para teste")
    void testCriarProduto() {
        Produto produto = new Produto();
        produto.setId(1L);
        produto.setNome("Produto Teste");
        produto.setPrecoVenda(new BigDecimal("10.50"));
        produto.setEstoque(100);

        assertNotNull(produto);
        assertEquals(1L, produto.getId());
        assertEquals("Produto Teste", produto.getNome());
        assertEquals(new BigDecimal("10.50"), produto.getPrecoVenda());
        assertEquals(100, produto.getEstoque());
    }

    @Test
    @DisplayName("Deve criar cliente válido para teste")
    void testCriarCliente() {
        Cliente cliente = new Cliente();
        cliente.setId(1L);
        cliente.setNome("Cliente Teste");
        cliente.setTipoPessoa("FISICA");
        cliente.setCpf("123.456.789-00");

        assertNotNull(cliente);
        assertEquals(1L, cliente.getId());
        assertEquals("Cliente Teste", cliente.getNome());
        assertEquals("FISICA", cliente.getTipoPessoa());
        assertEquals("123.456.789-00", cliente.getCpf());
    }

    @Test
    @DisplayName("Deve criar usuário válido para teste")
    void testCriarUsuario() {
        Usuario usuario = new Usuario();
        usuario.setId(1L);
        usuario.setNome("Operador Teste");
        usuario.setTipousuario("OPERADOR");

        assertNotNull(usuario);
        assertEquals(1L, usuario.getId());
        assertEquals("Operador Teste", usuario.getNome());
        assertEquals("OPERADOR", usuario.getTipousuario());
    }

    @Test
    @DisplayName("Deve criar carrinho válido para teste")
    void testCriarCarrinho() {
        CarrinhoCompras carrinho = new CarrinhoCompras();
        carrinho.setId(1L);
        carrinho.setObservacao("Venda de teste");

        assertNotNull(carrinho);
        assertEquals(1L, carrinho.getId());
        assertEquals("Venda de teste", carrinho.getObservacao());
        assertEquals("ABERTO", carrinho.getStatus());
    }

    @Test
    @DisplayName("Deve validar cálculo de valor total")
    void testCalculoValorTotal() {
        BigDecimal precoUnitario = new BigDecimal("15.50");
        int quantidade = 3;
        BigDecimal valorEsperado = new BigDecimal("46.50");
        
        BigDecimal valorCalculado = precoUnitario.multiply(new BigDecimal(quantidade));
        
        assertEquals(valorEsperado, valorCalculado);
    }

    @Test
    @DisplayName("Deve validar cálculo de desconto")
    void testCalculoDesconto() {
        BigDecimal valorTotal = new BigDecimal("100.00");
        BigDecimal percentualDesconto = new BigDecimal("10.00");
        BigDecimal descontoEsperado = new BigDecimal("10.00");
        
        BigDecimal descontoCalculado = valorTotal.multiply(percentualDesconto)
            .divide(new BigDecimal("100"));
        
        assertEquals(descontoEsperado, descontoCalculado);
    }

    @Test
    @DisplayName("Deve validar cálculo de troco")
    void testCalculoTroco() {
        BigDecimal valorPago = new BigDecimal("50.00");
        BigDecimal valorTotal = new BigDecimal("35.50");
        BigDecimal trocoEsperado = new BigDecimal("14.50");
        
        BigDecimal trocoCalculado = valorPago.subtract(valorTotal);
        
        assertEquals(trocoEsperado, trocoCalculado);
    }
}
