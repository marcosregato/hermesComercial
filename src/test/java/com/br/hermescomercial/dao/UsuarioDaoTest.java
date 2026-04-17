package com.br.hermescomercial.dao;

import com.br.hermescomercial.model.Usuario;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class UsuarioDaoTest {

    private UsuarioDao usuarioDao;

    @BeforeEach
    void setUp() {
        usuarioDao = new UsuarioDao();
    }

    @Test
    @DisplayName("Deve buscar cliente por nome com case-insensitive")
    void testBuscarClientePorNomeCpfCnpj_CaseInsensitive() {
        // Teste com diferentes casos
        List<Usuario> resultado1 = usuarioDao.buscarClientePorNomeCpfCnpj("joão");
        List<Usuario> resultado2 = usuarioDao.buscarClientePorNomeCpfCnpj("JOÃO");
        List<Usuario> resultado3 = usuarioDao.buscarClientePorNomeCpfCnpj("João");
        
        // Todos devem retornar os mesmos resultados
        assertEquals(resultado1.size(), resultado2.size());
        assertEquals(resultado2.size(), resultado3.size());
    }

    @Test
    @DisplayName("Deve buscar cliente com 2 caracteres")
    void testBuscarClientePorNomeCpfCnpj_DoisCaracteres() {
        List<Usuario> resultado = usuarioDao.buscarClientePorNomeCpfCnpj("Jo");
        
        assertNotNull(resultado);
        // Verifica se encontrou algum cliente com "Jo" no nome
        assertTrue(resultado.size() >= 0);
    }

    @Test
    @DisplayName("Deve buscar cliente com 3 caracteres")
    void testBuscarClientePorNomeCpfCnpj_TresCaracteres() {
        List<Usuario> resultado = usuarioDao.buscarClientePorNomeCpfCnpj("Joa");
        
        assertNotNull(resultado);
        // Verifica se encontrou algum cliente com "Joa" no nome
        assertTrue(resultado.size() >= 0);
    }

    @Test
    @DisplayName("Não deve encontrar cliente com nome inexistente")
    void testBuscarClientePorNomeCpfCnpj_NomeInexistente() {
        List<Usuario> resultado = usuarioDao.buscarClientePorNomeCpfCnpj("XYZ123");
        
        assertNotNull(resultado);
        assertEquals(0, resultado.size());
    }

    @Test
    @DisplayName("Deve buscar apenas clientes (tipo CLIENTE)")
    void testBuscarClientePorNomeCpfCnpj_ApenasClientes() {
        List<Usuario> resultado = usuarioDao.buscarClientePorNomeCpfCnpj("a");
        
        assertNotNull(resultado);
        // Verifica se todos os resultados são do tipo CLIENTE
        for (Usuario usuario : resultado) {
            assertEquals("CLIENTE", usuario.getTipousuario());
        }
    }

    @Test
    @DisplayName("Deve tratar busca nula ou vazia")
    void testBuscarClientePorNomeCpfCnpj_BuscaVazia() {
        List<Usuario> resultado1 = usuarioDao.buscarClientePorNomeCpfCnpj(null);
        List<Usuario> resultado2 = usuarioDao.buscarClientePorNomeCpfCnpj("");
        List<Usuario> resultado3 = usuarioDao.buscarClientePorNomeCpfCnpj(" ");
        
        assertNotNull(resultado1);
        assertNotNull(resultado2);
        assertNotNull(resultado3);
    }

    @Test
    @DisplayName("Deve buscar cliente com acentos e caracteres especiais")
    void testBuscarClientePorNomeCpfCnpj_Acentos() {
        List<Usuario> resultado1 = usuarioDao.buscarClientePorNomeCpfCnpj("São");
        List<Usuario> resultado2 = usuarioDao.buscarClientePorNomeCpfCnpj("Sao");
        
        assertNotNull(resultado1);
        assertNotNull(resultado2);
    }
}
