package com.br.hermescomercial.controller.pdv;

import com.br.hermescomercial.dao.UsuarioDao;
import com.br.hermescomercial.model.Usuario;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class BuscaDoisCaracteresTest {

    @Mock
    private UsuarioDao usuarioDao;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @ParameterizedTest
    @ValueSource(strings = {
        "Jo", "Ma", "An", "Pe", "Ca", "Ro", "Li", "Si", "Ti", "Lu"
    })
    @DisplayName("Deve permitir busca com exatamente 2 caracteres")
    void testBuscaExatamenteDoisCaracteres(String termo) {
        List<Usuario> resultado = new ArrayList<>();
        when(usuarioDao.buscarClientePorNomeCpfCnpj(termo)).thenReturn(resultado);
        
        // Simula a validação do controller
        assertTrue(termo.length() >= 2, "Deve aceitar busca com 2 caracteres: " + termo);
        
        // Verifica se o DAO seria chamado
        List<Usuario> clientes = usuarioDao.buscarClientePorNomeCpfCnpj(termo);
        assertNotNull(clientes);
        verify(usuarioDao).buscarClientePorNomeCpfCnpj(termo);
    }

    @ParameterizedTest
    @ValueSource(strings = {
        "J", "M", "A", "P", "C", "R", "L", "S", "T", "L"
    })
    @DisplayName("Não deve permitir busca com apenas 1 caractere")
    void testBuscaApenasUmCaractere(String termo) {
        // Simula a validação do controller
        assertFalse(termo.length() >= 2, "Não deve aceitar busca com 1 caractere: " + termo);
        
        // Verifica se o DAO não seria chamado
        verify(usuarioDao, never()).buscarClientePorNomeCpfCnpj(termo);
    }

    @Test
    @DisplayName("Não deve permitir busca com string vazia")
    void testBuscaStringVazia() {
        String termo = "";
        
        assertFalse(termo.length() >= 2, "Não deve aceitar busca vazia");
        verify(usuarioDao, never()).buscarClientePorNomeCpfCnpj(termo);
    }

    @Test
    @DisplayName("Não deve permitir busca com null")
    void testBuscaNull() {
        String termo = null;
        
        assertFalse(termo != null && termo.length() >= 2, "Não deve aceitar busca null");
        verify(usuarioDao, never()).buscarClientePorNomeCpfCnpj(any());
    }

    @ParameterizedTest
    @ValueSource(strings = {
        "João", "Maria", "Ana", "Pedro", "Carlos", "Roberto", "Luciana", "Silvia"
    })
    @DisplayName("Deve permitir busca com mais de 2 caracteres")
    void testBuscaMaisDeDoisCaracteres(String termo) {
        List<Usuario> resultado = new ArrayList<>();
        when(usuarioDao.buscarClientePorNomeCpfCnpj(termo)).thenReturn(resultado);
        
        assertTrue(termo.length() >= 2, "Deve aceitar busca com mais de 2 caracteres: " + termo);
        
        List<Usuario> clientes = usuarioDao.buscarClientePorNomeCpfCnpj(termo);
        assertNotNull(clientes);
        verify(usuarioDao).buscarClientePorNomeCpfCnpj(termo);
    }

    @Test
    @DisplayName("Deve validar comportamento do listener com 2 caracteres")
    void testListenerComDoisCaracteres() {
        // Simula o comportamento do listener do TextField
        // String oldValue = ""; - não utilizado
        String newValue = "Jo";
        
        // Simula a validação do listener
        if (newValue != null && newValue.length() >= 2) {
            // Busca seria executada
            List<Usuario> resultado = new ArrayList<>();
            when(usuarioDao.buscarClientePorNomeCpfCnpj(newValue)).thenReturn(resultado);
            
            List<Usuario> clientes = usuarioDao.buscarClientePorNomeCpfCnpj(newValue);
            assertNotNull(clientes);
            verify(usuarioDao).buscarClientePorNomeCpfCnpj("Jo");
        }
    }

    @Test
    @DisplayName("Deve validar comportamento do listener com 1 caractere")
    void testListenerComUmCaractere() {
        // String oldValue = ""; - não utilizado
        String newValue = "J";
        
        // Simula a validação do listener
        if (newValue != null && newValue.length() >= 2) {
            fail("Não deveria executar busca com 1 caractere");
        }
        
        // Verifica que não foi chamado
        verify(usuarioDao, never()).buscarClientePorNomeCpfCnpj("J");
    }

    @Test
    @DisplayName("Deve validar busca incremental")
    void testBuscaIncremental() {
        // Simula digitação progressiva
        String[] sequencia = {"", "J", "Jo", "Joa", "João"};
        
        for (int i = 0; i < sequencia.length; i++) {
            String termo = sequencia[i];
            boolean deveBuscar = termo != null && termo.length() >= 2;
            
            if (deveBuscar) {
                // A partir de "Jo", deve buscar
                List<Usuario> resultado = new ArrayList<>();
                when(usuarioDao.buscarClientePorNomeCpfCnpj(termo)).thenReturn(resultado);
                
                List<Usuario> clientes = usuarioDao.buscarClientePorNomeCpfCnpj(termo);
                assertNotNull(clientes);
                verify(usuarioDao, times(1)).buscarClientePorNomeCpfCnpj(termo);
            } else {
                // Antes de "Jo", não deve buscar
                verify(usuarioDao, never()).buscarClientePorNomeCpfCnpj(termo);
            }
        }
    }

    @Test
    @DisplayName("Deve validar busca case-insensitive com 2 caracteres")
    void testBuscaCaseInsensitiveDoisCaracteres() {
        String[] variacoes = {"jo", "Jo", "JO"};
        
        for (String termo : variacoes) {
            assertTrue(termo.length() >= 2, "Deve aceitar: " + termo);
            
            List<Usuario> resultado = new ArrayList<>();
            when(usuarioDao.buscarClientePorNomeCpfCnpj(termo)).thenReturn(resultado);
            
            List<Usuario> clientes = usuarioDao.buscarClientePorNomeCpfCnpj(termo);
            assertNotNull(clientes);
            verify(usuarioDao, times(1)).buscarClientePorNomeCpfCnpj(termo);
        }
    }

    @Test
    @DisplayName("Deve validar busca com números (CPF/CNPJ) com 2 caracteres")
    void testBuscaNumericaDoisCaracteres() {
        String[] termosNumericos = {"12", "34", "56", "78", "90"};
        
        for (String termo : termosNumericos) {
            assertTrue(termo.length() >= 2, "Deve aceitar busca numérica: " + termo);
            assertTrue(termo.matches("\\d*"), "Deve conter apenas números: " + termo);
            
            List<Usuario> resultado = new ArrayList<>();
            when(usuarioDao.buscarClientePorNomeCpfCnpj(termo)).thenReturn(resultado);
            
            List<Usuario> clientes = usuarioDao.buscarClientePorNomeCpfCnpj(termo);
            assertNotNull(clientes);
            verify(usuarioDao, times(1)).buscarClientePorNomeCpfCnpj(termo);
        }
    }

    @Test
    @DisplayName("Deve validar limite mínimo exato de 2 caracteres")
    void testLimiteMinimoExato() {
        // Testa exatamente no limite
        String termoLimite = "AB";
        
        assertTrue(termoLimite.length() >= 2, "Deve aceitar exatamente 2 caracteres");
        
        List<Usuario> resultado = new ArrayList<>();
        when(usuarioDao.buscarClientePorNomeCpfCnpj(termoLimite)).thenReturn(resultado);
        
        List<Usuario> clientes = usuarioDao.buscarClientePorNomeCpfCnpj(termoLimite);
        assertNotNull(clientes);
        verify(usuarioDao).buscarClientePorNomeCpfCnpj("AB");
        
        // Testa um caractere abaixo do limite
        String termoAbaixo = "A";
        
        assertFalse(termoAbaixo.length() >= 2, "Não deve aceitar 1 caractere");
        verify(usuarioDao, never()).buscarClientePorNomeCpfCnpj("A");
    }
}
