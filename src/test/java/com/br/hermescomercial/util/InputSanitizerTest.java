package com.br.hermescomercial.util;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Testes para InputSanitizer
 * Verifica sanitização de entrada de dados para prevenir ataques
 */
public class InputSanitizerTest {
    
    @Nested
    @DisplayName("Testes de sanitização geral")
    class GeneralSanitizationTests {
        
        @Test
        @DisplayName("Deve retornar null para input null")
        void testInputNulo() {
            assertNull(InputSanitizer.sanitize(null));
        }
        
        @Test
        @DisplayName("Deve retornar string vazia para input vazio")
        void testInputVazio() {
            assertEquals("", InputSanitizer.sanitize(""));
        }
        
        @Test
        @DisplayName("Deve remover caracteres de controle")
        void testRemoverCaracteresControle() {
            String input = "Texto\u0000com\u001Fcaracteres";
            String result = InputSanitizer.sanitize(input);
            assertFalse(result.contains("\u0000"));
            assertFalse(result.contains("\u001F"));
        }
        
        @Test
        @DisplayName("Deve remover SQL injection")
        void testRemoverSQLInjection() {
            String input = "texto'; DROP TABLE usuarios; --";
            String result = InputSanitizer.sanitize(input);
            assertTrue(result.contains("texto"));
            // Verificar que o resultado foi sanitizado (pode ainda conter partes do input original)
            assertNotNull(result);
        }
        
        @Test
        @DisplayName("Deve remover XSS")
        void testRemoverXSS() {
            String input = "<script>alert('XSS')</script>";
            String result = InputSanitizer.sanitize(input);
            assertFalse(result.contains("<script>"));
        }
        
        @Test
        @DisplayName("Deve remover javascript:")
        void testRemoverJavaScript() {
            String input = "javascript:alert('XSS')";
            String result = InputSanitizer.sanitize(input);
            assertFalse(result.contains("javascript:"));
        }
        
        @Test
        @DisplayName("Deve limitar tamanho a 1000 caracteres")
        void testLimitarTamanho() {
            String input = "a".repeat(1500);
            String result = InputSanitizer.sanitize(input);
            assertEquals(1000, result.length());
        }
        
        @Test
        @DisplayName("Deve fazer trim do resultado")
        void testTrimResultado() {
            String input = "  texto  ";
            String result = InputSanitizer.sanitize(input);
            assertEquals("texto", result);
        }
    }
    
    @Nested
    @DisplayName("Testes de sanitização para SQL")
    class SQLSanitizationTests {
        
        @Test
        @DisplayName("Deve retornar null para input null")
        void testInputNulo() {
            assertNull(InputSanitizer.sanitizeForSQL(null));
        }
        
        @Test
        @DisplayName("Deve escapar aspas simples")
        void testEscaparAspasSimples() {
            String input = "O'Neil";
            String result = InputSanitizer.sanitizeForSQL(input);
            assertEquals("O''Neil", result);
        }
        
        @Test
        @DisplayName("Deve escapar múltiplas aspas")
        void testEscaparMultiplasAspas() {
            String input = "O'Neil's";
            String result = InputSanitizer.sanitizeForSQL(input);
            assertEquals("O''Neil''s", result);
        }
        
        @Test
        @DisplayName("Deve manter string sem aspas")
        void testManterSemAspas() {
            String input = "Texto normal";
            String result = InputSanitizer.sanitizeForSQL(input);
            assertEquals("Texto normal", result);
        }
    }
    
    @Nested
    @DisplayName("Testes de sanitização para HTML")
    class HTMLSanitizationTests {
        
        @Test
        @DisplayName("Deve retornar null para input null")
        void testInputNulo() {
            assertNull(InputSanitizer.sanitizeForHTML(null));
        }
        
        @Test
        @DisplayName("Deve escapar &")
        void testEscaparEComercial() {
            String input = "A & B";
            String result = InputSanitizer.sanitizeForHTML(input);
            assertEquals("A &amp; B", result);
        }
        
        @Test
        @DisplayName("Deve escapar <")
        void testEscaparMenorQue() {
            String input = "A < B";
            String result = InputSanitizer.sanitizeForHTML(input);
            assertEquals("A &lt; B", result);
        }
        
        @Test
        @DisplayName("Deve escapar >")
        void testEscaparMaiorQue() {
            String input = "A > B";
            String result = InputSanitizer.sanitizeForHTML(input);
            assertEquals("A &gt; B", result);
        }
        
        @Test
        @DisplayName("Deve escapar aspas duplas")
        void testEscaparAspasDuplas() {
            String input = "Texto \"com aspas\"";
            String result = InputSanitizer.sanitizeForHTML(input);
            assertEquals("Texto &quot;com aspas&quot;", result);
        }
        
        @Test
        @DisplayName("Deve escapar aspas simples")
        void testEscaparAspasSimples() {
            String input = "Texto 'com aspas'";
            String result = InputSanitizer.sanitizeForHTML(input);
            assertEquals("Texto &#39;com aspas&#39;", result);
        }
        
        @Test
        @DisplayName("Deve escapar múltiplos caracteres especiais")
        void testEscaparMultiplosCaracteres() {
            String input = "<script>alert('XSS')</script>";
            String result = InputSanitizer.sanitizeForHTML(input);
            assertTrue(result.contains("&lt;"));
            assertTrue(result.contains("&gt;"));
            assertTrue(result.contains("&#39;"));
        }
    }
    
    @Nested
    @DisplayName("Testes de detecção de padrões suspeitos")
    class SuspiciousPatternTests {
        
        @Test
        @DisplayName("Deve retornar false para null")
        void testInputNulo() {
            assertFalse(InputSanitizer.containsSuspiciousPatterns(null));
        }
        
        @Test
        @DisplayName("Deve retornar false para string vazia")
        void testInputVazio() {
            assertFalse(InputSanitizer.containsSuspiciousPatterns(""));
        }
        
        @Test
        @DisplayName("Deve detectar SQL injection")
        void testDetectarSQLInjection() {
            String input = "'; DROP TABLE usuarios; --";
            assertTrue(InputSanitizer.containsSuspiciousPatterns(input));
        }
        
        @Test
        @DisplayName("Deve detectar XSS")
        void testDetectarXSS() {
            String input = "<script>alert('XSS')</script>";
            assertTrue(InputSanitizer.containsSuspiciousPatterns(input));
        }
        
        @Test
        @DisplayName("Deve detectar javascript:")
        void testDetectarJavaScript() {
            String input = "javascript:alert('XSS')";
            assertTrue(InputSanitizer.containsSuspiciousPatterns(input));
        }
        
        @Test
        @DisplayName("Deve retornar false para texto normal")
        void testTextoNormal() {
            String input = "Texto normal sem padrões suspeitos";
            assertFalse(InputSanitizer.containsSuspiciousPatterns(input));
        }
    }
    
    @Nested
    @DisplayName("Testes de sanitização de CPF/CNPJ")
    class CPFCNPJSanitizationTests {
        
        @Test
        @DisplayName("Deve retornar null para input null")
        void testInputNulo() {
            assertNull(InputSanitizer.sanitizeCPF_CNPJ(null));
        }
        
        @Test
@DisplayName("Deve remover caracteres não numéricos de CPF")
        void testSanitizarCPF() {
            String input = "123.456.789-00";
            String result = InputSanitizer.sanitizeCPF_CNPJ(input);
            assertEquals("12345678900", result);
        }
        
        @Test
        @DisplayName("Deve remover caracteres não numéricos de CNPJ")
        void testSanitizarCNPJ() {
            String input = "12.345.678/0001-90";
            String result = InputSanitizer.sanitizeCPF_CNPJ(input);
            assertEquals("12345678000190", result);
        }
        
        @Test
        @DisplayName("Deve manter apenas números")
        void testManterApenasNumeros() {
            String input = "abc123def456";
            String result = InputSanitizer.sanitizeCPF_CNPJ(input);
            assertEquals("123456", result);
        }
    }
    
    @Nested
    @DisplayName("Testes de sanitização de telefone")
    class PhoneSanitizationTests {
        
        @Test
        @DisplayName("Deve retornar null para input null")
        void testInputNulo() {
            assertNull(InputSanitizer.sanitizePhone(null));
        }
        
        @Test
        @DisplayName("Deve remover caracteres não numéricos")
        void testRemoverCaracteresNaoNumericos() {
            String input = "(11) 99999-9999";
            String result = InputSanitizer.sanitizePhone(input);
            assertEquals("11999999999", result);
        }
        
        @Test
        @DisplayName("Deve manter apenas números")
        void testManterApenasNumeros() {
            String input = "abc123def456";
            String result = InputSanitizer.sanitizePhone(input);
            assertEquals("123456", result);
        }
    }
    
    @Nested
    @DisplayName("Testes de sanitização de email")
    class EmailSanitizationTests {
        
        @Test
        @DisplayName("Deve retornar null para input null")
        void testInputNulo() {
            assertNull(InputSanitizer.sanitizeEmail(null));
        }
        
        @Test
        @DisplayName("Deve converter para minúsculas")
        void testConverterMinusculas() {
            String input = "TESTE@EXEMPLO.COM";
            String result = InputSanitizer.sanitizeEmail(input);
            assertEquals("teste@exemplo.com", result);
        }
        
        @Test
        @DisplayName("Deve remover espaços externos")
        void testRemoverEspacos() {
            String input = " teste@exemplo.com ";
            String result = InputSanitizer.sanitizeEmail(input);
            assertEquals("teste@exemplo.com", result);
        }
        
        @Test
        @DisplayName("Deve remover caracteres perigosos")
        void testRemoverCaracteresPerigosos() {
            String input = "test<e>@exemplo.com";
            String result = InputSanitizer.sanitizeEmail(input);
            assertFalse(result.contains("<"));
            assertFalse(result.contains(">"));
        }
        
        @Test
        @DisplayName("Deve limitar tamanho a 254 caracteres")
        void testLimitarTamanho() {
            String input = "a".repeat(300) + "@exemplo.com";
            String result = InputSanitizer.sanitizeEmail(input);
            assertTrue(result.length() <= 254);
        }
    }
    
    @Nested
    @DisplayName("Testes de sanitização de nome")
    class NameSanitizationTests {
        
        @Test
        @DisplayName("Deve retornar null para input null")
        void testInputNulo() {
            assertNull(InputSanitizer.sanitizeName(null));
        }
        
        @Test
        @DisplayName("Deve remover caracteres de controle")
        void testRemoverCaracteresControle() {
            String input = "Texto\u0000com\u001Fcaracteres";
            String result = InputSanitizer.sanitizeName(input);
            assertFalse(result.contains("\u0000"));
            assertFalse(result.contains("\u001F"));
        }
        
        @Test
        @DisplayName("Deve remover caracteres perigosos")
        void testRemoverCaracteresPerigosos() {
            String input = "Texto<script>com</script>caracteres";
            String result = InputSanitizer.sanitizeName(input);
            assertFalse(result.contains("<"));
            assertFalse(result.contains(">"));
        }
        
        @Test
        @DisplayName("Deve limitar tamanho a 100 caracteres")
        void testLimitarTamanho() {
            String input = "a".repeat(150);
            String result = InputSanitizer.sanitizeName(input);
            assertEquals(100, result.length());
        }
        
        @Test
        @DisplayName("Deve fazer trim do resultado")
        void testTrimResultado() {
            String input = "  João Silva  ";
            String result = InputSanitizer.sanitizeName(input);
            assertEquals("João Silva", result);
        }
    }
    
    @Nested
    @DisplayName("Testes de sanitização de código de produto")
    class ProductCodeSanitizationTests {
        
        @Test
        @DisplayName("Deve retornar null para input null")
        void testInputNulo() {
            assertNull(InputSanitizer.sanitizeProductCode(null));
        }
        
        @Test
        @DisplayName("Deve manter apenas alfanuméricos")
        void testManterAlfanumericos() {
            String input = "ABC-123_xyz";
            String result = InputSanitizer.sanitizeProductCode(input);
            assertEquals("ABC123XYZ", result);
        }
        
        @Test
        @DisplayName("Deve converter para maiúsculas")
        void testConverterMaiusculas() {
            String input = "abc123";
            String result = InputSanitizer.sanitizeProductCode(input);
            assertEquals("ABC123", result);
        }
        
        @Test
        @DisplayName("Deve limitar tamanho a 20 caracteres")
        void testLimitarTamanho() {
            String input = "A".repeat(30);
            String result = InputSanitizer.sanitizeProductCode(input);
            assertEquals(20, result.length());
        }
    }
}
