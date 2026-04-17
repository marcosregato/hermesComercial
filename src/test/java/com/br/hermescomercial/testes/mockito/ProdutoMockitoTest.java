package com.br.hermescomercial.testes.mockito;

import com.br.hermescomercial.model.Produto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ProdutoMockitoTest {

    @Mock
    private Produto produtoMock;

    @BeforeEach
    void setUp() {
        // Configuração do mock
        when(produtoMock.getId()).thenReturn(1L);
        when(produtoMock.getNome()).thenReturn("Produto Mock");
        when(produtoMock.getPrecoVenda()).thenReturn(new BigDecimal("15.50"));
        when(produtoMock.getEstoque()).thenReturn(100);
        when(produtoMock.getUnidade()).thenReturn("UN");
        when(produtoMock.getCodigo()).thenReturn("12345");
        when(produtoMock.getCodigoBarras()).thenReturn("7890123456789");
        when(produtoMock.getCategoria()).thenReturn("Categoria Mock");
        when(produtoMock.getSubCategoria()).thenReturn("Subcategoria Mock");
    }

    @Test
    @DisplayName("Deve validar comportamento básico do mock")
    void testMockBasico() {
        // Act
        Long id = produtoMock.getId();
        String nome = produtoMock.getNome();
        BigDecimal preco = produtoMock.getPrecoVenda();
        int estoque = produtoMock.getEstoque();

        // Assert
        assertEquals(1L, id);
        assertEquals("Produto Mock", nome);
        assertEquals(new BigDecimal("15.50"), preco);
        assertEquals(100, estoque);
        
        // Verify
        verify(produtoMock, times(1)).getId();
        verify(produtoMock, times(1)).getNome();
        verify(produtoMock, times(1)).getPrecoVenda();
        verify(produtoMock, times(1)).getEstoque();
    }

    @Test
    @DisplayName("Deve testar múltiplas chamadas ao mesmo método")
    void testMultiplasChamadas() {
        // Arrange
        when(produtoMock.getNome())
            .thenReturn("Primeira Chamada")
            .thenReturn("Segunda Chamada")
            .thenReturn("Terceira Chamada");

        // Act & Assert
        assertEquals("Primeira Chamada", produtoMock.getNome());
        assertEquals("Segunda Chamada", produtoMock.getNome());
        assertEquals("Terceira Chamada", produtoMock.getNome());
        
        verify(produtoMock, times(3)).getNome();
    }

    @Test
    @DisplayName("Deve testar comportamento com spy")
    void testSpyBehavior() {
        // Arrange
        Produto produtoReal = new Produto();
        produtoReal.setId(1L);
        produtoReal.setNome("Produto Real");
        produtoReal.setPrecoVenda(new BigDecimal("10.00"));
        
        Produto produtoSpy = spy(produtoReal);
        
        // Override do comportamento de um método específico
        when(produtoSpy.getNome()).thenReturn("Produto Spy Modificado");

        // Act & Assert
        assertEquals("Produto Spy Modificado", produtoSpy.getNome()); // Mockado
        assertEquals(1L, produtoSpy.getId()); // Método real
        assertEquals(new BigDecimal("10.00"), produtoSpy.getPrecoVenda()); // Método real
        
        verify(produtoSpy, times(1)).getNome();
    }

    @Test
    @DisplayName("Deve testar argument matchers")
    void testArgumentMatchers() {
        // Arrange - Mock que retorna valores diferentes baseado no argumento
        Produto outroProdutoMock = mock(Produto.class);
        when(outroProdutoMock.getNome()).thenReturn("Produto Padrão");
        when(outroProdutoMock.getNome()).thenReturn("Produto Especial");
        
        // Act & Assert
        assertEquals("Produto Especial", outroProdutoMock.getNome());
        
        verify(outroProdutoMock, atLeastOnce()).getNome();
    }

    @Test
    @DisplayName("Deve testar comportamento com lista")
    void testComportamentoComLista() {
        // Arrange
        List<Produto> produtos = new ArrayList<>();
        produtos.add(produtoMock);
        
        List<Produto> listaMock = mock(List.class);
        when(listaMock.size()).thenReturn(1);
        when(listaMock.get(0)).thenReturn(produtoMock);
        when(listaMock.isEmpty()).thenReturn(false);

        // Act & Assert
        assertEquals(1, listaMock.size());
        assertFalse(listaMock.isEmpty());
        assertEquals(produtoMock, listaMock.get(0));
        
        verify(listaMock, times(1)).size();
        verify(listaMock, times(1)).isEmpty();
        verify(listaMock, times(1)).get(0);
    }

    @Test
    @DisplayName("Deve testar verify never")
    void testVerifyNever() {
        // Act - Nenhuma chamada ao método getSubCategoria()
        
        // Assert
        verify(produtoMock, never()).getSubCategoria();
    }

    @Test
    @DisplayName("Deve testar verify atLeast e atMost")
    void testVerifyAtLeastAtMost() {
        // Act
        produtoMock.getNome();
        produtoMock.getNome();
        produtoMock.getPrecoVenda();

        // Assert
        verify(produtoMock, atLeast(2)).getNome();
        verify(produtoMock, atMost(3)).getNome();
        verify(produtoMock, times(1)).getPrecoVenda();
    }

    @Test
    @DisplayName("Deve testar comportamento com doThrow")
    void testDoThrow() {
        // Arrange
        Produto produtoComExcecao = mock(Produto.class);
        doThrow(new RuntimeException("Erro simulado")).when(produtoComExcecao).getNome();

        // Act & Assert
        assertThrows(RuntimeException.class, () -> {
            produtoComExcecao.getNome();
        });
        
        verify(produtoComExcecao, times(1)).getNome();
    }

    @Test
    @DisplayName("Deve testar comportamento com doReturn")
    void testDoReturn() {
        // Arrange
        Produto produtoComDoReturn = mock(Produto.class);
        doReturn("Produto com doReturn").when(produtoComDoReturn).getNome();

        // Act
        String nome = produtoComDoReturn.getNome();

        // Assert
        assertEquals("Produto com doReturn", nome);
        verify(produtoComDoReturn, times(1)).getNome();
    }

    @Test
    @DisplayName("Deve testar reset do mock")
    void testResetMock() {
        // Act
        reset(produtoMock);

        // Assert - Após reset, o mock não tem mais comportamento definido
        assertNull(produtoMock.getNome());
    }
}
