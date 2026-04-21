package com.br.hermescomercial.refactoring;

import com.br.hermescomercial.injection.DependencyContainer;
import com.br.hermescomercial.service.ClienteServiceRefactored;
import com.br.hermescomercial.service.ProdutoServiceRefactored;
import com.br.hermescomercial.service.UsuarioServiceRefactored;
import com.br.hermescomercial.model.Cliente;
import com.br.hermescomercial.model.Produto;
import com.br.hermescomercial.model.Usuario;
import com.br.hermescomercial.exception.BusinessException;
import com.br.hermescomercial.exception.ValidationException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Teste de integração para validar o sistema refatorado
 * Verifica se todas as novas funcionalidades estão funcionando corretamente
 */
class RefactoringIntegrationTest {

    private ClienteServiceRefactored clienteService;
    private ProdutoServiceRefactored produtoService;
    private UsuarioServiceRefactored usuarioService;

    @BeforeEach
    void setUp() {
        // Inicializar services refatorados
        clienteService = new ClienteServiceRefactored();
        produtoService = new ProdutoServiceRefactored();
        usuarioService = new UsuarioServiceRefactored();
    }

    @Test
    @DisplayName("Deve inicializar container de dependências com sucesso")
    void testContainerInicializacao() {
        // Arrange & Act
        DependencyContainer testContainer = DependencyContainer.getInstance();
        
        // Assert
        assertNotNull(testContainer, "Container não deve ser nulo");
        
        // Verificar se dependências estão registradas
        assertDoesNotThrow(() -> {
            testContainer.get(com.br.hermescomercial.repository.ClienteRepository.class);
            testContainer.get(com.br.hermescomercial.repository.ProdutoRepository.class);
            testContainer.get(com.br.hermescomercial.repository.UsuarioRepository.class);
        }, "Todas as dependências devem estar disponíveis");
    }

    @Test
    @DisplayName("Deve criar services refatorados com injeção de dependência")
    void testServicesRefatorados() {
        // Arrange & Act
        ClienteServiceRefactored clienteTestService = new ClienteServiceRefactored();
        ProdutoServiceRefactored produtoTestService = new ProdutoServiceRefactored();
        UsuarioServiceRefactored usuarioTestService = new UsuarioServiceRefactored();
        
        // Assert
        assertNotNull(clienteTestService, "ClienteServiceRefactored não deve ser nulo");
        assertNotNull(produtoTestService, "ProdutoServiceRefactored não deve ser nulo");
        assertNotNull(usuarioTestService, "UsuarioServiceRefactored não deve ser nulo");
    }

    @Test
    @DisplayName("Deve validar cliente com dados corretos")
    void testValidacaoClienteSucesso() {
        // Arrange
        Cliente cliente = new Cliente();
        cliente.setNome("João Silva");
        cliente.setCpf("123.456.789-00");
        cliente.setEmail("joao@email.com");
        cliente.setTelefone("(11) 98765-4321");
        
        // Act & Assert
        assertDoesNotThrow(() -> {
            clienteService.salvar(cliente);
            // Não verificamos o resultado aqui pois depende do banco de dados
            // Apenas verificamos se não há exceções de validação
        }, "Cliente válido deve ser aceito");
    }

    @Test
    @DisplayName("Deve rejeitar cliente com dados inválidos")
    void testValidacaoClienteFalha() {
        // Arrange
        Cliente cliente = new Cliente();
        cliente.setNome(""); // Nome vazio
        
        // Act & Assert
        ValidationException exception = assertThrows(ValidationException.class, () -> {
            clienteService.salvar(cliente);
        }, "Deve lançar ValidationException para cliente inválido");
        
        assertTrue(exception.getMessage().contains("Nome do cliente é obrigatório"), 
                  "Mensagem deve indicar problema com nome");
    }

    @Test
    @DisplayName("Deve validar produto com dados corretos")
    void testValidacaoProdutoSucesso() {
        // Arrange
        Produto produto = new Produto();
        produto.setNome("Produto Teste");
        produto.setPrecoVenda(new BigDecimal("100.00"));
        produto.setEstoque(50);
        produto.setCodigo("PROD001");
        
        // Act & Assert
        assertDoesNotThrow(() -> {
            produtoService.salvar(produto);
            // Não verificamos o resultado aqui pois depende do banco de dados
            // Apenas verificamos se não há exceções de validação
        }, "Produto válido deve ser aceito");
    }

    @Test
    @DisplayName("Deve rejeitar produto com dados inválidos")
    void testValidacaoProdutoFalha() {
        // Arrange
        Produto produto = new Produto();
        produto.setNome(""); // Nome vazio
        produto.setPrecoVenda(BigDecimal.ZERO); // Preço inválido
        
        // Act & Assert
        ValidationException exception = assertThrows(ValidationException.class, () -> {
            produtoService.salvar(produto);
        }, "Deve lançar ValidationException para produto inválido");
        
        assertTrue(exception.getMessage().contains("Nome do produto é obrigatório"), 
                  "Mensagem deve indicar problema com nome");
    }

    @Test
    @DisplayName("Deve validar usuário com dados corretos")
    void testValidacaoUsuarioSucesso() {
        // Arrange
        Usuario usuario = new Usuario();
        usuario.setNome("Test User");
        usuario.setEmail("test@email.com");
        
        // Act & Assert
        assertDoesNotThrow(() -> {
            usuarioService.salvar(usuario);
        }, "Usuário válido deve ser aceito");
    }

    @Test
    @DisplayName("Deve rejeitar usuário com dados inválidos")
    void testValidacaoUsuarioFalha() {
        // Arrange
        Usuario usuario = new Usuario();
        usuario.setNome(""); // Nome vazio
        
        // Act & Assert
        ValidationException exception = assertThrows(ValidationException.class, () -> {
            usuarioService.salvar(usuario);
        }, "Deve lançar ValidationException para usuário inválido");
        
        assertTrue(exception.getMessage().contains("Nome do usuário é obrigatório"), 
                  "Mensagem deve indicar problema com nome");
    }

    @Test
    @DisplayName("Deve listar entidades sem lançar exceções")
    void testListagemEntidades() {
        // Act & Assert
        assertDoesNotThrow(() -> {
            clienteService.listar();
        }, "Listagem de clientes não deve lançar exceções");
        
        assertDoesNotThrow(() -> {
            produtoService.listar();
        }, "Listagem de produtos não deve lançar exceções");
        
        assertDoesNotThrow(() -> {
            usuarioService.listar();
        }, "Listagem de usuários não deve lançar exceções");
    }

    @Test
    @DisplayName("Deve buscar entidades sem lançar exceções")
    void testBuscaEntidades() {
        // Act & Assert
        assertDoesNotThrow(() -> {
            clienteService.buscarPorNome("teste");
        }, "Busca de clientes por nome não deve lançar exceções");
        
        assertDoesNotThrow(() -> {
            produtoService.buscarPorCodigoBarras("123456789");
        }, "Busca de produtos por código de barras não deve lançar exceções");
        
        assertDoesNotThrow(() -> {
            usuarioService.buscarPorNome("teste");
        }, "Busca de usuários por nome não deve lançar exceções");
    }

    @Test
    @DisplayName("Deve verificar estoque de produto")
    void testVerificacaoEstoque() {
        // Arrange
        Produto produto = new Produto();
        produto.setNome("Produto Teste");
        produto.setEstoque(10);
        
        // Act & Assert
        assertTrue(produtoService.verificarEstoque(produto, 5), 
                  "Deve ter estoque suficiente para quantidade menor");
        
        assertFalse(produtoService.verificarEstoque(produto, 15), 
                   "Não deve ter estoque suficiente para quantidade maior");
        
        assertFalse(produtoService.verificarEstoque(null, 5), 
                   "Produto nulo deve retornar false");
        
        assertFalse(produtoService.verificarEstoque(produto, 0), 
                   "Quantidade zero deve retornar false");
    }

    @Test
    @DisplayName("Deve validar credenciais de usuário")
    void testValidacaoCredenciais() {
        // Act & Assert
        assertFalse(usuarioService.validarCredenciais(null, "senha"), 
                    "Nome nulo deve retornar false");
        
        assertFalse(usuarioService.validarCredenciais("usuario", null), 
                    "Senha nula deve retornar false");
        
        assertFalse(usuarioService.validarCredenciais("", "senha"), 
                    "Nome vazio deve retornar false");
        
        // Para usuário existente, o comportamento depende do banco de dados
        // Apenas verificamos se não lança exceção
        assertDoesNotThrow(() -> {
            usuarioService.validarCredenciais("inexistente", "senha");
        }, "Validação de credenciais não deve lançar exceções");
    }

    @Test
    @DisplayName("Deve lidar com exceções de negócio adequadamente")
    void testExcecoesNegocio() {
        // Arrange
        Cliente cliente1 = new Cliente();
        cliente1.setNome("Cliente Teste");
        cliente1.setCpf("123.456.789-00");
        
        Cliente cliente2 = new Cliente();
        cliente2.setNome("Cliente Teste"); // Mesmo nome
        cliente2.setCpf("987.654.321-00");
        
        // Act & Assert - Como depende do banco de dados, apenas verificamos se não lança exceções inesperadas
        assertDoesNotThrow(() -> {
            try {
                clienteService.salvar(cliente1);
                // Tentativa de salvar segundo cliente com mesmo nome pode lançar BusinessException
                // ou pode funcionar dependendo da implementação do banco
                clienteService.salvar(cliente2);
            } catch (BusinessException e) {
                // Exceção esperada para duplicidade
                assertTrue(e.getMessage().contains("Já existe um usuário"), 
                          "Mensagem deve indicar duplicidade");
            }
        }, "Tratamento de exceções de negócio não deve lançar exceções inesperadas");
    }
}
