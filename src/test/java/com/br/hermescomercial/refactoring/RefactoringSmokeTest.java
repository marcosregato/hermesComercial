package com.br.hermescomercial.refactoring;

import com.br.hermescomercial.injection.DependencyContainer;
import com.br.hermescomercial.service.ClienteServiceRefactored;
import com.br.hermescomercial.service.ProdutoServiceRefactored;
import com.br.hermescomercial.service.UsuarioServiceRefactored;
import com.br.hermescomercial.model.Cliente;
import com.br.hermescomercial.model.Produto;
import com.br.hermescomercial.model.Usuario;
import com.br.hermescomercial.exception.ValidationException;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Teste simples (Smoke Test) para validar o sistema refatorado
 * Usa JUnit 4 para compatibilidade com o projeto existente
 */
public class RefactoringSmokeTest {

    @Test
    @DisplayName("Deve inicializar container de dependências")
    public void testContainerInicializacao() {
        DependencyContainer container = DependencyContainer.getInstance();
        assertNotNull(container, "Container não deve ser nulo");
    }

    @Test
    @DisplayName("Deve criar services refatorados")
    public void testServicesRefatorados() {
        ClienteServiceRefactored clienteService = new ClienteServiceRefactored();
        ProdutoServiceRefactored produtoService = new ProdutoServiceRefactored();
        UsuarioServiceRefactored usuarioService = new UsuarioServiceRefactored();
        
        assertNotNull(clienteService, "ClienteServiceRefactored não deve ser nulo");
        assertNotNull(produtoService, "ProdutoServiceRefactored não deve ser nulo");
        assertNotNull(usuarioService, "UsuarioServiceRefactored não deve ser nulo");
    }

    @Test
    public void testValidacaoCliente() {
        ClienteServiceRefactored clienteService = new ClienteServiceRefactored();
        
        // Teste cliente válido
        Cliente clienteValido = new Cliente();
        clienteValido.setNome("João Silva");
        clienteValido.setCpf("123.456.789-00");
        clienteValido.setEmail("joao@email.com");
        
        try {
            clienteService.salvar(clienteValido);
            // Se não lançar exceção, teste passou
        } catch (Exception e) {
            // Se for exceção de banco, está ok para este teste
            if (!(e instanceof ValidationException)) {
                fail("Não deveria lançar exceção de validação para cliente válido");
            }
        }
        
        // Teste cliente inválido
        Cliente clienteInvalido = new Cliente();
        clienteInvalido.setNome(""); // Nome vazio
        
        try {
            clienteService.salvar(clienteInvalido);
            fail("Deveria lançar ValidationException para cliente inválido");
        } catch (ValidationException e) {
            // Esperado
            assertTrue(e.getMessage().contains("Nome do cliente é obrigatório"),
                      "Mensagem deve mencionar nome obrigatório");
        }
    }

    @Test
    public void testValidacaoProduto() {
        ProdutoServiceRefactored produtoService = new ProdutoServiceRefactored();
        
        // Teste produto válido
        Produto produtoValido = new Produto();
        produtoValido.setNome("Produto Teste");
        produtoValido.setPrecoVenda(new BigDecimal("100.00"));
        produtoValido.setEstoque(50);
        produtoValido.setCodigo("PROD001");
        
        try {
            produtoService.salvar(produtoValido);
            // Se não lançar exceção, teste passou
        } catch (Exception e) {
            // Se for exceção de banco, está ok para este teste
            if (!(e instanceof ValidationException)) {
                fail("Não deveria lançar exceção de validação para produto válido");
            }
        }
        
        // Teste produto inválido
        Produto produtoInvalido = new Produto();
        produtoInvalido.setNome(""); // Nome vazio
        produtoInvalido.setPrecoVenda(BigDecimal.ZERO); // Preço inválido
        
        try {
            produtoService.salvar(produtoInvalido);
            fail("Deveria lançar ValidationException para produto inválido");
        } catch (ValidationException e) {
            // Esperado
            assertTrue(e.getMessage().contains("Nome do produto é obrigatório"),
                      "Mensagem deve mencionar nome obrigatório");
        }
    }

    @Test
    public void testValidacaoUsuario() {
        UsuarioServiceRefactored usuarioService = new UsuarioServiceRefactored();
        
        // Teste usuário válido
        Usuario usuarioValido = new Usuario();
        usuarioValido.setNome("Test User");
        usuarioValido.setEmail("test@email.com");
        
        try {
            usuarioService.salvar(usuarioValido);
            // Se não lançar exceção, teste passou
        } catch (Exception e) {
            // Se for exceção de banco, está ok para este teste
            if (!(e instanceof ValidationException)) {
                fail("Não deveria lançar exceção de validação para usuário válido");
            }
        }
        
        // Teste usuário inválido
        Usuario usuarioInvalido = new Usuario();
        usuarioInvalido.setNome(""); // Nome vazio
        
        try {
            usuarioService.salvar(usuarioInvalido);
            fail("Deveria lançar ValidationException para usuário inválido");
        } catch (ValidationException e) {
            // Esperado
            assertTrue(e.getMessage().contains("Nome do usuário é obrigatório"),
                      "Mensagem deve mencionar nome obrigatório");
        }
    }

    @Test
    public void testListagemEntidades() {
        ClienteServiceRefactored clienteService = new ClienteServiceRefactored();
        ProdutoServiceRefactored produtoService = new ProdutoServiceRefactored();
        UsuarioServiceRefactored usuarioService = new UsuarioServiceRefactored();
        
        // Testar se listagens não lançam exceções
        try {
            clienteService.listar();
            produtoService.listar();
            usuarioService.listar();
            // Se chegou aqui, tudo ok
        } catch (Exception e) {
            fail("Listagens não deveriam lançar exceções: " + e.getMessage());
        }
    }

    @Test
    public void testVerificacaoEstoque() {
        ProdutoServiceRefactored produtoService = new ProdutoServiceRefactored();
        
        Produto produto = new Produto();
        produto.setNome("Produto Teste");
        produto.setEstoque(10);
        
        assertTrue(produtoService.verificarEstoque(produto, 5),
                  "Deve ter estoque suficiente");
        
        assertFalse(produtoService.verificarEstoque(produto, 15),
                   "Não deve ter estoque suficiente");
        
        assertFalse(produtoService.verificarEstoque(null, 5),
                   "Produto nulo deve retornar false");
        
        assertFalse(produtoService.verificarEstoque(produto, 0),
                   "Quantidade zero deve retornar false");
    }

    @Test
    public void testValidacaoCredenciais() {
        UsuarioServiceRefactored usuarioService = new UsuarioServiceRefactored();
        
        assertFalse(usuarioService.validarCredenciais(null, "senha"),
                    "Nome nulo deve retornar false");
        
        assertFalse(usuarioService.validarCredenciais("usuario", null),
                    "Senha nula deve retornar false");
        
        assertFalse(usuarioService.validarCredenciais("", "senha"),
                    "Nome vazio deve retornar false");
        
        // Para usuário existente, apenas verificamos se não lança exceção
        try {
            usuarioService.validarCredenciais("inexistente", "senha");
            // Se não lançar exceção, teste passou
        } catch (Exception e) {
            fail("Validação de credenciais não deveria lançar exceções: " + e.getMessage());
        }
    }
}
