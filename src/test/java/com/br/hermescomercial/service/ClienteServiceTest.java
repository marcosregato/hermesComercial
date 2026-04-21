package com.br.hermescomercial.service;

import com.br.hermescomercial.model.Cliente;
import com.br.hermescomercial.dao.ClienteDao;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Classe de teste unitário para ClienteService
 */
class ClienteServiceTest {

    @Mock
    private ClienteDao clienteDao;
    
    private ClienteService clienteService;
    
    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        clienteService = new ClienteService(clienteDao);
    }
    
    @Test
    @DisplayName("Deve salvar cliente com sucesso")
    void testSalvarClienteSucesso() throws Exception {
        // Arrange
        Cliente cliente = new Cliente();
        cliente.setNome("João Silva");
        cliente.setCpf("123.456.789-00");
        cliente.setEmail("joao@email.com");
        cliente.setTelefone("(11) 98765-4321");
        
        when(clienteDao.salvar(any(Cliente.class))).thenReturn(true);
        
        // Act
        boolean resultado = clienteService.salvar(cliente);
        
        // Assert
        assertTrue(resultado);
        verify(clienteDao, times(1)).salvar(cliente);
    }
    
    @Test
    @DisplayName("Deve lançar exceção ao salvar cliente nulo")
    void testSalvarClienteNulo() {
        // Act & Assert
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            clienteService.salvar(null);
        });
        
        assertEquals("Cliente não pode ser nulo", exception.getMessage());
    }
    
    @Test
    @DisplayName("Deve listar todos clientes com sucesso")
    void testListarTodosClientesSucesso() throws Exception {
        // Arrange
        List<Cliente> clientesEsperados = new ArrayList<>();
        when(clienteDao.listar()).thenReturn(clientesEsperados);
        
        // Act
        List<Cliente> resultado = clienteService.listar();
        
        // Assert
        assertNotNull(resultado);
        verify(clienteDao, times(1)).listar();
    }
    
    @Test
    @DisplayName("Deve buscar clientes com filtros com sucesso")
    void testBuscarClientesComFiltrosSucesso() throws Exception {
        // Arrange
        List<Cliente> clientesEsperados = new ArrayList<>();
        Cliente cliente1 = new Cliente();
        cliente1.setNome("João Silva");
        Cliente cliente2 = new Cliente();
        cliente2.setNome("João Santos");
        clientesEsperados.add(cliente1);
        clientesEsperados.add(cliente2);
        
        when(clienteDao.buscarComFiltros(anyString(), anyBoolean(), anyBoolean(), anyBoolean())).thenReturn(clientesEsperados);
        
        // Act
        List<Cliente> resultado = clienteService.buscarComFiltros("João", true, false, false);
        
        // Assert
        assertNotNull(resultado);
        assertEquals(2, resultado.size());
        verify(clienteDao, times(1)).buscarComFiltros(anyString(), anyBoolean(), anyBoolean(), anyBoolean());
    }
    
    @Test
    @DisplayName("Deve buscar cliente por ID com sucesso")
    void testBuscarClientePorIdSucesso() throws Exception {
        // Arrange
        Cliente clienteEsperado = new Cliente();
        clienteEsperado.setId(1L);
        clienteEsperado.setNome("João Silva");
        
        when(clienteDao.buscarPorId(1L)).thenReturn(clienteEsperado);
        
        // Act
        Cliente resultado = clienteService.buscarPorId(1L);
        
        // Assert
        assertNotNull(resultado);
        assertEquals(1L, resultado.getId());
        assertEquals("João Silva", resultado.getNome());
        verify(clienteDao, times(1)).buscarPorId(1L);
    }
}
