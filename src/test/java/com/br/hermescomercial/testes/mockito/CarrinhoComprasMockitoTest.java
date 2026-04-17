package com.br.hermescomercial.testes.mockito;

import com.br.hermescomercial.model.CarrinhoCompras;
import com.br.hermescomercial.model.Produto;
import com.br.hermescomercial.model.ItemVenda;
import com.br.hermescomercial.model.Cliente;
import com.br.hermescomercial.model.Usuario;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CarrinhoComprasMockitoTest {

    @Mock
    private CarrinhoCompras carrinhoMock;

    @Mock
    private Produto produtoMock;

    @Mock
    private Cliente clienteMock;

    @Mock
    private Usuario usuarioMock;

    @Mock
    private ItemVenda itemVendaMock;

    private CarrinhoCompras carrinhoReal;

    @BeforeEach
    void setUp() {
        // Configuração dos mocks
        when(carrinhoMock.getId()).thenReturn(1L);
        when(carrinhoMock.getValorTotal()).thenReturn(new BigDecimal("100.00"));
        when(carrinhoMock.getDescontoTotal()).thenReturn(new BigDecimal("10.00"));
        when(carrinhoMock.getValorFinal()).thenReturn(new BigDecimal("90.00"));
        when(carrinhoMock.getStatus()).thenReturn("ABERTO");
        when(carrinhoMock.getObservacao()).thenReturn("Venda de teste");
        when(carrinhoMock.getValorDesconto()).thenReturn(new BigDecimal("10.00"));

        // Carrinho real para testes com spy
        carrinhoReal = new CarrinhoCompras();
        carrinhoReal.setId(2L);
        carrinhoReal.setValorTotal(new BigDecimal("50.00"));
        carrinhoReal.setDescontoTotal(new BigDecimal("5.00"));
        carrinhoReal.setValorFinal(new BigDecimal("45.00"));

        // Configuração do produto mock
        when(produtoMock.getId()).thenReturn(1L);
        when(produtoMock.getNome()).thenReturn("Produto Mock");
        when(produtoMock.getPrecoVenda()).thenReturn(new BigDecimal("25.00"));

        // Configuração do cliente mock
        when(clienteMock.getId()).thenReturn(1L);
        when(clienteMock.getNome()).thenReturn("Cliente Mock");

        // Configuração do usuário mock
        when(usuarioMock.getId()).thenReturn(1L);
        when(usuarioMock.getNome()).thenReturn("Operador Mock");

        // Configuração do item de venda mock
        when(itemVendaMock.getId()).thenReturn(1L);
        when(itemVendaMock.getProduto()).thenReturn(produtoMock);
        when(itemVendaMock.getQuantidade()).thenReturn(2);
        when(itemVendaMock.getValorUnitario()).thenReturn(new BigDecimal("25.00"));
        when(itemVendaMock.getValorTotal()).thenReturn(new BigDecimal("50.00"));
    }

    @Test
    @DisplayName("Deve validar comportamento básico do carrinho mock")
    void testCarrinhoMockBasico() {
        // Act
        Long id = carrinhoMock.getId();
        BigDecimal valorTotal = carrinhoMock.getValorTotal();
        BigDecimal desconto = carrinhoMock.getDescontoTotal();
        BigDecimal valorFinal = carrinhoMock.getValorFinal();
        String status = carrinhoMock.getStatus();

        // Assert
        assertEquals(1L, id);
        assertEquals(new BigDecimal("100.00"), valorTotal);
        assertEquals(new BigDecimal("10.00"), desconto);
        assertEquals(new BigDecimal("90.00"), valorFinal);
        assertEquals("ABERTO", status);
        
        // Verify
        verify(carrinhoMock, times(1)).getId();
        verify(carrinhoMock, times(1)).getValorTotal();
        verify(carrinhoMock, times(1)).getDescontoTotal();
        verify(carrinhoMock, times(1)).getValorFinal();
        verify(carrinhoMock, times(1)).getStatus();
    }

    @Test
    @DisplayName("Deve testar carrinho com spy")
    void testCarrinhoSpy() {
        // Arrange
        CarrinhoCompras carrinhoSpy = spy(carrinhoReal);
        
        // Override do comportamento de um método específico
        when(carrinhoSpy.getValorTotal()).thenReturn(new BigDecimal("75.00"));

        // Act & Assert
        assertEquals(new BigDecimal("75.00"), carrinhoSpy.getValorTotal()); // Mockado
        assertEquals(2L, carrinhoSpy.getId()); // Método real
        assertEquals(new BigDecimal("5.00"), carrinhoSpy.getDescontoTotal()); // Método real
        
        verify(carrinhoSpy, times(1)).getValorTotal();
    }

    @Test
    @DisplayName("Deve testar lista de itens no carrinho")
    void testListaItensCarrinho() {
        // Arrange
        List<ItemVenda> itensMock = new ArrayList<>();
        itensMock.add(itemVendaMock);
        
        when(carrinhoMock.getItens()).thenReturn(itensMock);

        // Act
        List<ItemVenda> itens = carrinhoMock.getItens();

        // Assert
        assertNotNull(itens);
        assertEquals(1, itens.size());
        assertEquals(itemVendaMock, itens.get(0));
        
        verify(carrinhoMock, times(1)).getItens();
    }

    @Test
    @DisplayName("Deve testar cliente e operador no carrinho")
    void testClienteOperadorCarrinho() {
        // Arrange
        when(carrinhoMock.getCliente()).thenReturn(clienteMock);
        when(carrinhoMock.getOperador()).thenReturn(usuarioMock);

        // Act
        Cliente cliente = carrinhoMock.getCliente();
        Usuario operador = carrinhoMock.getOperador();

        // Assert
        assertNotNull(cliente);
        assertNotNull(operador);
        assertEquals(1L, cliente.getId());
        assertEquals("Cliente Mock", cliente.getNome());
        assertEquals(1L, operador.getId());
        assertEquals("Operador Mock", operador.getNome());
        
        verify(carrinhoMock, times(1)).getCliente();
        verify(carrinhoMock, times(1)).getOperador();
    }

    @Test
    @DisplayName("Deve testar datas no carrinho")
    void testDatasCarrinho() {
        // Arrange
        LocalDateTime agora = LocalDateTime.now();
        when(carrinhoMock.getDataAbertura()).thenReturn(agora);
        when(carrinhoMock.getDataFechamento()).thenReturn(agora.plusHours(1));

        // Act
        LocalDateTime dataAbertura = carrinhoMock.getDataAbertura();
        LocalDateTime dataFechamento = carrinhoMock.getDataFechamento();

        // Assert
        assertNotNull(dataAbertura);
        assertNotNull(dataFechamento);
        assertTrue(dataFechamento.isAfter(dataAbertura));
        
        verify(carrinhoMock, times(1)).getDataAbertura();
        verify(carrinhoMock, times(1)).getDataFechamento();
    }

    @Test
    @DisplayName("Deve testar cálculo de valor final")
    void testCalculoValorFinal() {
        // Arrange
        BigDecimal valorTotal = new BigDecimal("100.00");
        BigDecimal desconto = new BigDecimal("15.00");
        BigDecimal valorFinalEsperado = new BigDecimal("85.00");
        
        when(carrinhoMock.getValorTotal()).thenReturn(valorTotal);
        when(carrinhoMock.getDescontoTotal()).thenReturn(desconto);
        when(carrinhoMock.getValorFinal()).thenReturn(valorFinalEsperado);

        // Act
        BigDecimal valorTotalRetornado = carrinhoMock.getValorTotal();
        BigDecimal descontoRetornado = carrinhoMock.getDescontoTotal();
        BigDecimal valorFinalRetornado = carrinhoMock.getValorFinal();

        // Assert
        assertEquals(valorTotal, valorTotalRetornado);
        assertEquals(desconto, descontoRetornado);
        assertEquals(valorFinalEsperado, valorFinalRetornado);
        
        // Verificação do cálculo
        BigDecimal valorCalculado = valorTotal.subtract(desconto);
        assertEquals(valorFinalEsperado, valorCalculado);
    }

    @Test
    @DisplayName("Deve testar comportamento com lista vazia")
    void testListaVazia() {
        // Arrange
        List<ItemVenda> listaVazia = new ArrayList<>();
        when(carrinhoMock.getItens()).thenReturn(listaVazia);

        // Act
        List<ItemVenda> itens = carrinhoMock.getItens();

        // Assert
        assertNotNull(itens);
        assertTrue(itens.isEmpty());
        
        verify(carrinhoMock, times(1)).getItens();
    }

    @Test
    @DisplayName("Deve testar múltiplos itens no carrinho")
    void testMultiplosItens() {
        // Arrange
        ItemVenda item2Mock = mock(ItemVenda.class);
        when(item2Mock.getId()).thenReturn(2L);
        when(item2Mock.getValorTotal()).thenReturn(new BigDecimal("30.00"));
        
        List<ItemVenda> multiplosItens = new ArrayList<>();
        multiplosItens.add(itemVendaMock);
        multiplosItens.add(item2Mock);
        
        when(carrinhoMock.getItens()).thenReturn(multiplosItens);

        // Act
        List<ItemVenda> itens = carrinhoMock.getItens();

        // Assert
        assertEquals(2, itens.size());
        assertEquals(itemVendaMock, itens.get(0));
        assertEquals(item2Mock, itens.get(1));
        
        // Verificação do valor total dos itens
        BigDecimal valorTotalItens = itens.stream()
            .map(ItemVenda::getValorTotal)
            .reduce(BigDecimal.ZERO, BigDecimal::add);
        assertEquals(new BigDecimal("80.00"), valorTotalItens);
        
        verify(carrinhoMock, times(1)).getItens();
    }

    @Test
    @DisplayName("Deve testar verify never")
    void testVerifyNever() {
        // Act - Nenhuma chamada ao método getObservacao()
        
        // Assert
        verify(carrinhoMock, never()).getObservacao();
    }

    @Test
    @DisplayName("Deve testar comportamento com doThrow")
    void testDoThrow() {
        // Arrange
        CarrinhoCompras carrinhoComExcecao = mock(CarrinhoCompras.class);
        doThrow(new RuntimeException("Carrinho inválido")).when(carrinhoComExcecao).getValorTotal();

        // Act & Assert
        assertThrows(RuntimeException.class, () -> {
            carrinhoComExcecao.getValorTotal();
        });
        
        verify(carrinhoComExcecao, times(1)).getValorTotal();
    }
}
