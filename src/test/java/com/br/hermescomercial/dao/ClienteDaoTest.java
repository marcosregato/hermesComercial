package com.br.hermescomercial.dao;

import com.br.hermescomercial.model.Cliente;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

public class ClienteDaoTest {

    private ClienteDao clienteDao;

    @BeforeEach
    void setUp() {
        clienteDao = new ClienteDao();
    }

    @Test
    @DisplayName("Deve criar ClienteDao sem erros")
    void testCriarClienteDao() {
        assertNotNull(clienteDao);
    }

    @Test
    @DisplayName("Deve criar cliente pessoa física válido")
    void testCriarClientePessoaFisica() {
        Cliente cliente = new Cliente();
        cliente.setId(1L);
        cliente.setNome("João Silva");
        cliente.setTipoPessoa("FISICA");
        cliente.setCpf("123.456.789-00");
        cliente.setRg("MG-12.345.678");
        cliente.setDataNascimento("15/03/1985");
        cliente.setTelefone("(31) 98765-4321");
        cliente.setEmail("joao@email.com");
        cliente.setAtivo(true);

        assertNotNull(cliente);
        assertEquals(1, cliente.getId());
        assertEquals("João Silva", cliente.getNome());
        assertEquals("FISICA", cliente.getTipoPessoa());
        assertEquals("123.456.789-00", cliente.getCpf());
        assertEquals("MG-12.345.678", cliente.getRg());
        assertEquals("15/03/1985", cliente.getDataNascimento());
        assertEquals("(31) 98765-4321", cliente.getTelefone());
        assertEquals("joao@email.com", cliente.getEmail());
        assertTrue(cliente.isAtivo());
    }

    @Test
    @DisplayName("Deve criar cliente pessoa jurídica válido")
    void testCriarClientePessoaJuridica() {
        Cliente cliente = new Cliente();
        cliente.setId(2L);
        cliente.setNome("Empresa ABC Ltda");
        cliente.setTipoPessoa("JURIDICA");
        cliente.setCnpj("12.345.678/0001-90");
        cliente.setNomeFantasia("ABC Company");
        cliente.setInscricaoEstadual("123456789");
        cliente.setTelefone("(31) 3333-4444");
        cliente.setEmail("contato@empresa.com");
        cliente.setAtivo(true);

        assertNotNull(cliente);
        assertEquals(2, cliente.getId());
        assertEquals("Empresa ABC Ltda", cliente.getNome());
        assertEquals("JURIDICA", cliente.getTipoPessoa());
        assertEquals("12.345.678/0001-90", cliente.getCnpj());
        assertEquals("ABC Company", cliente.getNomeFantasia());
        assertEquals("123456789", cliente.getInscricaoEstadual());
        assertEquals("(31) 3333-4444", cliente.getTelefone());
        assertEquals("contato@empresa.com", cliente.getEmail());
        assertTrue(cliente.isAtivo());
    }

    @Test
    @DisplayName("Deve validar endereço do cliente")
    void testEnderecoCliente() {
        Cliente cliente = new Cliente();
        cliente.setEndereco("Rua das Flores, 123");
        cliente.setBairro("Centro");
        cliente.setCidade("Belo Horizonte");
        cliente.setEstado("MG");
        cliente.setCep("30100-000");

        assertEquals("Rua das Flores, 123", cliente.getEndereco());
        assertEquals("Centro", cliente.getBairro());
        assertEquals("Belo Horizonte", cliente.getCidade());
        assertEquals("MG", cliente.getEstado());
        assertEquals("30100-000", cliente.getCep());
    }

    @Test
    @DisplayName("Deve validar datas de cadastro e atualização")
    void testDatasCadastroAtualizacao() {
        Cliente cliente = new Cliente();
        LocalDateTime agora = LocalDateTime.now();
        
        cliente.setDataCadastro(agora);
        cliente.setDataAtualizacao(agora);

        assertEquals(agora, cliente.getDataCadastro());
        assertEquals(agora, cliente.getDataAtualizacao());
    }

    @Test
    @DisplayName("Deve validar status ativo do cliente")
    void testStatusAtivo() {
        Cliente cliente = new Cliente();
        
        // Por padrão, cliente deve estar ativo
        assertTrue(cliente.isAtivo());
        
        cliente.setAtivo(false);
        assertFalse(cliente.isAtivo());
        
        cliente.setAtivo(true);
        assertTrue(cliente.isAtivo());
    }
}
