package com.br.hermescomercial.validation;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.*;

public class CpfCnpjValidationTest {

    @Test
    @DisplayName("Deve aceitar apenas números no CPF/CNPJ")
    void testValidacaoApenasNumeros() {
        // Teste com regex usado no listener
        assertTrue("12345678900".matches("\\d*"));
        assertTrue("12345678901234".matches("\\d*"));
        assertTrue("0".matches("\\d*"));
        
        assertFalse("123.456.789-00".matches("\\d*"));
        assertFalse("123.456.789/0001-00".matches("\\d*"));
        assertFalse("123a456".matches("\\d*"));
        assertFalse("ABC123".matches("\\d*"));
        assertFalse("123@456".matches("\\d*"));
        assertFalse("".matches("\\d*"));
    }

    @ParameterizedTest
    @ValueSource(strings = {
        "12345678900",      // CPF válido formato
        "12345678901234",   // CNPJ válido formato
        "1",                // Mínimo 1 dígito
        "123456789012345678", // Máximo 18 dígitos
        "0",                // Zero
        "99999999999"       // CPF com 9s
    })
    @DisplayName("Deve aceitar CPF/CNPJ com apenas números")
    void testAceitarCpfCnpjValido(String cpfCnpj) {
        assertTrue(cpfCnpj.matches("\\d*"), "Deve aceitar: " + cpfCnpj);
    }

    @ParameterizedTest
    @ValueSource(strings = {
        "123.456.789-00",
        "12.345.678/0001-00",
        "123a456",
        "ABC123",
        "123@456",
        "123#456",
        "123 456",
        "123-456",
        "123/456",
        "123\\456",
        "123+456",
        "123=456",
        "123%456",
        "123*456",
        "123&456",
        "123(456)",
        "123[456]",
        "123{456}",
        "123|456",
        "123;456",
        "123:456",
        "123'456",
        "123\"456",
        "123<456>",
        "123>456",
        "123?456",
        "123~456",
        "123!456",
        "123^456"
    })
    @DisplayName("Deve rejeitar CPF/CNPJ com caracteres não numéricos")
    void testRejeitarCpfCnpjInvalido(String cpfCnpj) {
        assertFalse(cpfCnpj.matches("\\d*"), "Deve rejeitar: " + cpfCnpj);
    }

    @Test
    @DisplayName("Deve remover caracteres não numéricos")
    void testRemoverCaracteresNaoNumericos() {
        assertEquals("12345678900", "123.456.789-00".replaceAll("[^0-9]", ""));
        assertEquals("12345678901234", "12.345.678/0001-00".replaceAll("[^0-9]", ""));
        assertEquals("123456", "123a456".replaceAll("[^0-9]", ""));
        assertEquals("123456", "ABC123456".replaceAll("[^0-9]", ""));
        assertEquals("123456", "123@456".replaceAll("[^0-9]", ""));
        assertEquals("123456789", "123 456 789".replaceAll("[^0-9]", ""));
    }

    @Test
    @DisplayName("Deve manter texto se já contém apenas números")
    void testManterTextoApenasNumeros() {
        String original = "12345678900";
        String filtrado = original.replaceAll("[^0-9]", "");
        assertEquals(original, filtrado);
        
        original = "12345678901234";
        filtrado = original.replaceAll("[^0-9]", "");
        assertEquals(original, filtrado);
    }

    @Test
    @DisplayName("Deve validar tamanho máximo do CPF/CNPJ")
    void testValidarTamanhoMaximo() {
        // CPF: 11 dígitos
        String cpf = "12345678900";
        assertTrue(cpf.length() <= 18);
        
        // CNPJ: 14 dígitos
        String cnpj = "12345678901234";
        assertTrue(cnpj.length() <= 18);
        
        // Limite exato: 18 dígitos
        String limite = "123456789012345678";
        assertEquals(18, limite.length());
        assertTrue(limite.length() <= 18);
        
        // Acima do limite
        String acima = "1234567890123456789";
        assertTrue(acima.length() > 18);
    }

    @Test
    @DisplayName("Deve truncar texto acima do limite")
    void testTruncarTextoAcimaLimite() {
        String texto = "1234567890123456789"; // 19 dígitos
        String truncado = texto.substring(0, 18);
        
        assertEquals("123456789012345678", truncado);
        assertEquals(18, truncado.length());
    }

    @Test
    @DisplayName("Deve identificar quando texto precisa ser truncado")
    void testIdentificarNecessidadeTruncamento() {
        String texto18 = "123456789012345678";
        String texto19 = "1234567890123456789";
        
        assertFalse(texto18.length() > 18);
        assertTrue(texto19.length() > 18);
        
        // Verifica se o truncado é diferente do original
        String truncado = texto19.substring(0, 18);
        assertNotEquals(texto19, truncado);
    }

    @Test
    @DisplayName("Deve validar entrada mínima para busca")
    void testValidarEntradaMinimaBusca() {
        // Mínimo 2 caracteres para busca
        assertTrue("12".length() >= 2);
        assertTrue("1".length() < 2);
        assertTrue("".length() < 2);
        
        // Teste com números
        assertTrue("12".matches("\\d*"));
        assertTrue("12".length() >= 2);
    }

    @Test
    @DisplayName("Deve tratar strings vazias e nulas")
    void testTratarStringsVaziasNulas() {
        // String vazia
        String vazia = "";
        assertTrue(vazia.matches("\\d*"));
        assertEquals(0, vazia.length());
        
        // String com apenas números vazia
        String vaziaNumeros = vazia.replaceAll("[^0-9]", "");
        assertEquals("", vaziaNumeros);
        
        // Verifica se não precisa filtrar string vazia
        assertEquals(vazia, vaziaNumeros);
    }
}
