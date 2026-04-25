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
    @DisplayName("Deve salvar usuário com sucesso")
    void testSalvarUsuario() {
        // Arrange
        Usuario usuario = new Usuario();
        usuario.setNome("João Silva");
        usuario.setEmail("joao@email.com");
        usuario.setTipoUsuario("Cliente");

        // Act & Assert
        assertDoesNotThrow(() -> usuarioDao.salvar(usuario));
    }

    @Test
    @DisplayName("Deve buscar cliente por nome")
    void testBuscarClientePorNome() {
        // Arrange
        Usuario usuario = new Usuario();
        usuario.setNome("Maria Santos");
        usuario.setEmail("maria@email.com");
        usuario.setTipoUsuario("Cliente");
        usuarioDao.salvar(usuario);

        // Act
        List<Usuario> resultado = usuarioDao.buscarClientePorNome("Maria");

        // Assert
        assertNotNull(resultado);
    }

    @Test
    @DisplayName("Deve buscar cliente por CPF/CNPJ")
    void testBuscarClientePorCpfCnpj() {
        // Arrange
        Usuario usuario = new Usuario();
        usuario.setNome("Pedro Costa");
        usuario.setEmail("pedro@email.com");
        usuario.setNumeroDocumeto("12345678901");
        usuario.setTipoUsuario("Cliente");
        usuarioDao.salvar(usuario);

        // Act
        List<Usuario> resultado = usuarioDao.buscarClientePorCpfCnpj("12345678901");

        // Assert
        assertNotNull(resultado);
    }

    @Test
    @DisplayName("Deve buscar cliente por nome e CPF/CNPJ")
    void testBuscarClientePorNomeCpfCnpj() {
        // Arrange
        Usuario usuario = new Usuario();
        usuario.setNome("Ana Oliveira");
        usuario.setEmail("ana@email.com");
        usuario.setNumeroDocumeto("98765432100");
        usuario.setTipoUsuario("Cliente");
        usuarioDao.salvar(usuario);

        // Act
        List<Usuario> resultado = usuarioDao.buscarClientePorNomeCpfCnpj("Ana");

        // Assert
        assertNotNull(resultado);
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
