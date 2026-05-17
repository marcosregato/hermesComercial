package com.br.hermescomercial.validation;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import static org.junit.jupiter.api.Assertions.*;

import com.br.hermescomercial.exception.ValidationException;
import com.br.hermescomercial.model.Cliente;
import com.br.hermescomercial.model.Produto;
import com.br.hermescomercial.model.Usuario;

import java.math.BigDecimal;

/**
 * Testes para Validator
 * Verifica validação de entidades do sistema
 */
public class ValidatorTest {
    
    @Nested
    @DisplayName("Testes de validação de Cliente")
    class ClienteValidationTests {
        
        @Test
        @DisplayName("Deve lançar exceção quando cliente é nulo")
        void testClienteNulo() {
            assertThrows(ValidationException.class, () -> Validator.validarCliente(null));
        }
        
        @Test
        @DisplayName("Deve lançar exceção quando nome é vazio")
        void testNomeVazio() {
            Cliente cliente = new Cliente();
            cliente.setNome("");
            
            assertThrows(ValidationException.class, () -> Validator.validarCliente(cliente));
        }
        
        @Test
        @DisplayName("Deve lançar exceção quando nome tem menos de 3 caracteres")
        void testNomeCurto() {
            Cliente cliente = new Cliente();
            cliente.setNome("AB");
            
            assertThrows(ValidationException.class, () -> Validator.validarCliente(cliente));
        }
        
        @Test
        @DisplayName("Deve validar cliente com nome válido")
        void testClienteValido() {
            Cliente cliente = new Cliente();
            cliente.setNome("João Silva");
            cliente.setCpf("12345678900");
            
            assertDoesNotThrow(() -> Validator.validarCliente(cliente));
        }
    }
    
    @Nested
    @DisplayName("Testes de validação de Produto")
    class ProdutoValidationTests {
        
        @Test
        @DisplayName("Deve lançar exceção quando produto é nulo")
        void testProdutoNulo() {
            assertThrows(ValidationException.class, () -> Validator.validarProduto(null));
        }
        
        @Test
        @DisplayName("Deve lançar exceção quando nome é vazio")
        void testNomeVazio() {
            Produto produto = new Produto();
            produto.setNome("");
            
            assertThrows(ValidationException.class, () -> Validator.validarProduto(produto));
        }
        
        @Test
        @DisplayName("Deve lançar exceção quando preço é zero")
        void testPrecoZero() {
            Produto produto = new Produto();
            produto.setNome("Produto Teste");
            produto.setPrecoVenda(BigDecimal.ZERO);
            
            assertThrows(ValidationException.class, () -> Validator.validarProduto(produto));
        }
        
        @Test
        @DisplayName("Deve lançar exceção quando estoque é negativo")
        void testEstoqueNegativo() {
            Produto produto = new Produto();
            produto.setNome("Produto Teste");
            produto.setPrecoVenda(new BigDecimal("10.00"));
            produto.setEstoque(-1);
            
            assertThrows(ValidationException.class, () -> Validator.validarProduto(produto));
        }
        
        @Test
        @DisplayName("Deve validar produto com dados válidos")
        void testProdutoValido() {
            Produto produto = new Produto();
            produto.setNome("Produto Teste");
            produto.setPrecoVenda(new BigDecimal("10.00"));
            produto.setEstoque(10);
            
            assertDoesNotThrow(() -> Validator.validarProduto(produto));
        }
    }
    
    @Nested
    @DisplayName("Testes de validação de Usuário")
    class UsuarioValidationTests {
        
        @Test
        @DisplayName("Deve lançar exceção quando usuário é nulo")
        void testUsuarioNulo() {
            assertThrows(ValidationException.class, () -> Validator.validarUsuario(null));
        }
        
        @Test
        @DisplayName("Deve lançar exceção quando nome é vazio")
        void testNomeVazio() {
            Usuario usuario = new Usuario();
            usuario.setNome("");
            
            assertThrows(ValidationException.class, () -> Validator.validarUsuario(usuario));
        }
        
        @Test
        @DisplayName("Deve validar usuário com nome válido")
        void testUsuarioValido() {
            Usuario usuario = new Usuario();
            usuario.setNome("João Silva");
            
            assertDoesNotThrow(() -> Validator.validarUsuario(usuario));
        }
    }
    
    @Nested
    @DisplayName("Testes de validação genérica")
    class GenericValidationTests {
        
        @Test
        @DisplayName("Deve lançar exceção quando string é nula")
        void testStringNula() {
            assertThrows(ValidationException.class, () -> Validator.validarStringNaoVazia(null, "Campo"));
        }
        
        @Test
        @DisplayName("Deve lançar exceção quando string é vazia")
        void testStringVazia() {
            assertThrows(ValidationException.class, () -> Validator.validarStringNaoVazia("", "Campo"));
        }
        
        @Test
        @DisplayName("Deve validar string não vazia")
        void testStringValida() {
            assertDoesNotThrow(() -> Validator.validarStringNaoVazia("Valor", "Campo"));
        }
        
        @Test
        @DisplayName("Deve lançar exceção quando número é nulo")
        void testNumeroNulo() {
            assertThrows(ValidationException.class, () -> Validator.validarNumeroPositivo(null, "Campo"));
        }
        
        @Test
        @DisplayName("Deve lançar exceção quando número é zero")
        void testNumeroZero() {
            assertThrows(ValidationException.class, () -> Validator.validarNumeroPositivo(0, "Campo"));
        }
        
        @Test
        @DisplayName("Deve validar número positivo")
        void testNumeroPositivo() {
            assertDoesNotThrow(() -> Validator.validarNumeroPositivo(10, "Campo"));
        }
    }
}
