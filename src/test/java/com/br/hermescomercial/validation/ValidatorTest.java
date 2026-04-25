package com.br.hermescomercial.validation;

import com.br.hermescomercial.exception.ValidationException;
import com.br.hermescomercial.model.Cliente;
import com.br.hermescomercial.model.Produto;
import com.br.hermescomercial.model.Usuario;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.junit.jupiter.params.provider.NullAndEmptySource;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class ValidatorTest {

    private Cliente clienteValido;
    private Produto produtoValido;
    private Usuario usuarioValido;

    @BeforeEach
    void setUp() {
        clienteValido = new Cliente();
        clienteValido.setNome("João Silva");
        clienteValido.setCpf("12345678900");

        produtoValido = new Produto();
        produtoValido.setNome("Produto Teste");
        produtoValido.setPrecoVenda(new BigDecimal("10.50"));
        produtoValido.setEstoque(10);

        usuarioValido = new Usuario();
        usuarioValido.setNome("Usuário Teste");
    }

    // Testes de validação de Cliente
    @Test
    @DisplayName("Deve aceitar cliente válido")
    void testValidarClienteValido() {
        assertDoesNotThrow(() -> Validator.validarCliente(clienteValido));
    }

    @Test
    @DisplayName("Deve rejeitar cliente nulo")
    void testValidarClienteNulo() {
        ValidationException exception = assertThrows(ValidationException.class, 
            () -> Validator.validarCliente(null));
        assertEquals("Cliente não pode ser nulo", exception.getMessage());
    }

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {"   ", "\t", "\n"})
    @DisplayName("Deve rejeitar cliente com nome inválido")
    void testValidarClienteNomeInvalido(String nomeInvalido) {
        clienteValido.setNome(nomeInvalido);
        
        ValidationException exception = assertThrows(ValidationException.class, 
            () -> Validator.validarCliente(clienteValido));
        assertEquals("Nome do cliente é obrigatório", exception.getMessage());
    }

    @ParameterizedTest
    @ValueSource(strings = {"Jo", "J", "a"})
    @DisplayName("Deve rejeitar cliente com nome muito curto")
    void testValidarClienteNomeCurto(String nomeCurto) {
        clienteValido.setNome(nomeCurto);
        
        ValidationException exception = assertThrows(ValidationException.class, 
            () -> Validator.validarCliente(clienteValido));
        assertEquals("Nome do cliente deve ter pelo menos 3 caracteres", exception.getMessage());
    }

    @ParameterizedTest
    @ValueSource(strings = {"12345678900", "98765432100", "11144477735"})
    @DisplayName("Deve aceitar cliente com CPF válido")
    void testValidarClienteCPFValido(String cpfValido) {
        clienteValido.setCpf(cpfValido);
        
        assertDoesNotThrow(() -> Validator.validarCliente(clienteValido));
    }

    @ParameterizedTest
    @ValueSource(strings = {"123", "123456789012", "123.456.789-00", "abc12345678"})
    @DisplayName("Deve rejeitar cliente com CPF inválido")
    void testValidarClienteCPFInvalido(String cpfInvalido) {
        clienteValido.setCpf(cpfInvalido);
        
        ValidationException exception = assertThrows(ValidationException.class, 
            () -> Validator.validarCliente(clienteValido));
        assertEquals("CPF inválido", exception.getMessage());
    }

    @Test
    @DisplayName("Deve aceitar cliente sem CPF")
    void testValidarClienteSemCPF() {
        clienteValido.setCpf(null);
        
        assertDoesNotThrow(() -> Validator.validarCliente(clienteValido));
        
        clienteValido.setCpf("");
        assertDoesNotThrow(() -> Validator.validarCliente(clienteValido));
    }

    // Testes de validação de Produto
    @Test
    @DisplayName("Deve aceitar produto válido")
    void testValidarProdutoValido() {
        assertDoesNotThrow(() -> Validator.validarProduto(produtoValido));
    }

    @Test
    @DisplayName("Deve rejeitar produto nulo")
    void testValidarProdutoNulo() {
        ValidationException exception = assertThrows(ValidationException.class, 
            () -> Validator.validarProduto(null));
        assertEquals("Produto não pode ser nulo", exception.getMessage());
    }

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {"   ", "\t", "\n"})
    @DisplayName("Deve rejeitar produto com nome inválido")
    void testValidarProdutoNomeInvalido(String nomeInvalido) {
        produtoValido.setNome(nomeInvalido);
        
        ValidationException exception = assertThrows(ValidationException.class, 
            () -> Validator.validarProduto(produtoValido));
        assertEquals("Nome do produto é obrigatório", exception.getMessage());
    }

    @Test
    @DisplayName("Deve rejeitar produto com nome muito curto")
    void testValidarProdutoNomeCurto() {
        produtoValido.setNome("a");
        
        ValidationException exception = assertThrows(ValidationException.class, 
            () -> Validator.validarProduto(produtoValido));
        assertEquals("Nome do produto deve ter pelo menos 2 caracteres", exception.getMessage());
    }

    @Test
    @DisplayName("Deve aceitar produto com nome mínimo válido")
    void testValidarProdutoNomeMinimo() {
        produtoValido.setNome("ab");
        
        assertDoesNotThrow(() -> Validator.validarProduto(produtoValido));
    }

    @ParameterizedTest
    @ValueSource(strings = {"0", "-1", "-10.50", "-0.01"})
    @DisplayName("Deve rejeitar produto com preço inválido")
    void testValidarProdutoPrecoInvalido(String precoInvalido) {
        produtoValido.setPrecoVenda(new BigDecimal(precoInvalido));
        
        ValidationException exception = assertThrows(ValidationException.class, 
            () -> Validator.validarProduto(produtoValido));
        assertEquals("Preço de venda deve ser maior que zero", exception.getMessage());
    }

    @Test
    @DisplayName("Deve rejeitar produto com preço nulo")
    void testValidarProdutoPrecoNulo() {
        produtoValido.setPrecoVenda(null);
        
        ValidationException exception = assertThrows(ValidationException.class, 
            () -> Validator.validarProduto(produtoValido));
        assertEquals("Preço de venda deve ser maior que zero", exception.getMessage());
    }

    @ParameterizedTest
    @ValueSource(ints = {-1, -10, -100})
    @DisplayName("Deve rejeitar produto com estoque negativo")
    void testValidarProdutoEstoqueNegativo(int estoqueNegativo) {
        produtoValido.setEstoque(estoqueNegativo);
        
        ValidationException exception = assertThrows(ValidationException.class, 
            () -> Validator.validarProduto(produtoValido));
        assertEquals("Estoque não pode ser negativo", exception.getMessage());
    }

    @ParameterizedTest
    @ValueSource(ints = {0, 1, 10, 100})
    @DisplayName("Deve aceitar produto com estoque válido")
    void testValidarProdutoEstoqueValido(int estoqueValido) {
        produtoValido.setEstoque(estoqueValido);
        
        assertDoesNotThrow(() -> Validator.validarProduto(produtoValido));
    }

    // Testes de validação de Usuário
    @Test
    @DisplayName("Deve aceitar usuário válido")
    void testValidarUsuarioValido() {
        assertDoesNotThrow(() -> Validator.validarUsuario(usuarioValido));
    }

    @Test
    @DisplayName("Deve rejeitar usuário nulo")
    void testValidarUsuarioNulo() {
        ValidationException exception = assertThrows(ValidationException.class, 
            () -> Validator.validarUsuario(null));
        assertEquals("Usuário não pode ser nulo", exception.getMessage());
    }

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {"   ", "\t", "\n"})
    @DisplayName("Deve rejeitar usuário com nome inválido")
    void testValidarUsuarioNomeInvalido(String nomeInvalido) {
        usuarioValido.setNome(nomeInvalido);
        
        ValidationException exception = assertThrows(ValidationException.class, 
            () -> Validator.validarUsuario(usuarioValido));
        assertEquals("Nome do usuário é obrigatório", exception.getMessage());
    }

    @ParameterizedTest
    @ValueSource(strings = {"Jo", "J", "a"})
    @DisplayName("Deve rejeitar usuário com nome muito curto")
    void testValidarUsuarioNomeCurto(String nomeCurto) {
        usuarioValido.setNome(nomeCurto);
        
        ValidationException exception = assertThrows(ValidationException.class, 
            () -> Validator.validarUsuario(usuarioValido));
        assertEquals("Nome do usuário deve ter pelo menos 3 caracteres", exception.getMessage());
    }

    @Test
    @DisplayName("Deve aceitar usuário com nome mínimo válido")
    void testValidarUsuarioNomeMinimo() {
        usuarioValido.setNome("João");
        
        assertDoesNotThrow(() -> Validator.validarUsuario(usuarioValido));
    }

    // Testes de validação de String
    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {"   ", "\t", "\n"})
    @DisplayName("Deve rejeitar string vazia")
    void testValidarStringNaoVaziaInvalida(String valorInvalido) {
        ValidationException exception = assertThrows(ValidationException.class, 
            () -> Validator.validarStringNaoVazia(valorInvalido, "Campo Teste"));
        assertEquals("Campo Teste é obrigatório", exception.getMessage());
    }

    @ParameterizedTest
    @ValueSource(strings = {"valor", " valor ", "a", "1", "valor com espaços"})
    @DisplayName("Deve aceitar string válida")
    void testValidarStringNaoVaziaValida(String valorValido) {
        assertDoesNotThrow(() -> Validator.validarStringNaoVazia(valorValido, "Campo Teste"));
    }

    // Testes de validação de Número
    @ParameterizedTest
    @ValueSource(doubles = {0.0, -1.0, -0.1, -100.0})
    @DisplayName("Deve rejeitar número não positivo")
    void testValidarNumeroPositivoInvalido(Double valorInvalido) {
        ValidationException exception = assertThrows(ValidationException.class, 
            () -> Validator.validarNumeroPositivo(valorInvalido, "Campo Teste"));
        assertEquals("Campo Teste deve ser maior que zero", exception.getMessage());
    }

    @ParameterizedTest
    @ValueSource(doubles = {0.1, 1.0, 10.5, 100.0})
    @DisplayName("Deve aceitar número positivo")
    void testValidarNumeroPositivoValido(Double valorValido) {
        assertDoesNotThrow(() -> Validator.validarNumeroPositivo(valorValido, "Campo Teste"));
    }

    @Test
    @DisplayName("Deve rejeitar número nulo")
    void testValidarNumeroPositivoNulo() {
        ValidationException exception = assertThrows(ValidationException.class, 
            () -> Validator.validarNumeroPositivo(null, "Campo Teste"));
        assertEquals("Campo Teste deve ser maior que zero", exception.getMessage());
    }

    // Testes com diferentes tipos numéricos
    @Test
    @DisplayName("Deve aceitar inteiro positivo")
    void testValidarInteiroPositivo() {
        assertDoesNotThrow(() -> Validator.validarNumeroPositivo(5, "Inteiro"));
    }

    @Test
    @DisplayName("Deve aceitar float positivo")
    void testValidarFloatPositivo() {
        assertDoesNotThrow(() -> Validator.validarNumeroPositivo(5.5f, "Float"));
    }

    @Test
    @DisplayName("Deve aceitar BigDecimal positivo")
    void testValidarBigDecimalPositivo() {
        assertDoesNotThrow(() -> Validator.validarNumeroPositivo(new BigDecimal("10.50"), "BigDecimal"));
    }

    // Testes de integração
    @Test
    @DisplayName("Deve validar cliente com múltiplos atributos")
    void testValidarClienteCompleto() {
        Cliente cliente = new Cliente();
        cliente.setNome("Maria Santos");
        cliente.setCpf("98765432100");
        
        assertDoesNotThrow(() -> Validator.validarCliente(cliente));
    }

    @Test
    @DisplayName("Deve validar produto com múltiplos atributos")
    void testValidarProdutoCompleto() {
        Produto produto = new Produto();
        produto.setNome("Produto Completo");
        produto.setPrecoVenda(new BigDecimal("99.99"));
        produto.setEstoque(50);
        
        assertDoesNotThrow(() -> Validator.validarProduto(produto));
    }

    @Test
    @DisplayName("Deve validar usuário com múltiplos atributos")
    void testValidarUsuarioCompleto() {
        Usuario usuario = new Usuario();
        usuario.setNome("Administrador");
        
        assertDoesNotThrow(() -> Validator.validarUsuario(usuario));
    }
}
