package com.br.hermescomercial.business.pdv;

import com.br.hermescomercial.model.Usuario;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class PDVManagerTest {
    
    private PDVManager pdvManager;
    private Usuario operador;
    
    @BeforeEach
    void setUp() {
        pdvManager = new PDVManager();
        operador = new Usuario();
        operador.setId(1L);
        operador.setNome("Test Operator");
    }
    
    @Test
    void testAbrirCaixa_ComSucesso() {
        // Verifica estado inicial
        assertFalse(pdvManager.isCaixaAberto(), "Caixa deve iniciar fechado");
        
        // Abre o caixa
        boolean resultado = pdvManager.abrirCaixa();
        
        // Verifica resultado
        assertTrue(resultado, "Abrir caixa deve retornar true");
        assertTrue(pdvManager.isCaixaAberto(), "Caixa deve estar aberto após abrir");
    }
    
    @Test
    void testAbrirCaixa_JaAberto() {
        // Abre o caixa primeiro
        pdvManager.abrirCaixa();
        
        // Tenta abrir novamente
        boolean resultado = pdvManager.abrirCaixa();
        
        // Verifica resultado
        assertFalse(resultado, "Abrir caixa já aberto deve retornar false");
        assertTrue(pdvManager.isCaixaAberto(), "Caixa deve continuar aberto");
    }
    
    @Test
    void testFecharCaixa_ComSucesso() {
        // Abre o caixa primeiro
        pdvManager.abrirCaixa();
        assertTrue(pdvManager.isCaixaAberto(), "Caixa deve estar aberto");
        
        // Fecha o caixa
        boolean resultado = pdvManager.fecharCaixa();
        
        // Verifica resultado
        assertTrue(resultado, "Fechar caixa deve retornar true");
        assertFalse(pdvManager.isCaixaAberto(), "Caixa deve estar fechado após fechar");
    }
    
    @Test
    void testFecharCaixa_JaFechado() {
        // Garante que o caixa está fechado
        assertFalse(pdvManager.isCaixaAberto(), "Caixa deve iniciar fechado");
        
        // Tenta fechar
        boolean resultado = pdvManager.fecharCaixa();
        
        // Verifica resultado
        assertFalse(resultado, "Fechar caixa já fechado deve retornar false");
        assertFalse(pdvManager.isCaixaAberto(), "Caixa deve continuar fechado");
    }
    
    @Test
    void testIniciarSessaoPDV_CaixaFechado() {
        // Tenta iniciar sessão com caixa fechado
        boolean resultado = pdvManager.iniciarSessaoPDV(operador);
        
        // Verifica resultado
        assertFalse(resultado, "Iniciar sessão com caixa fechado deve retornar false");
    }
    
    @Test
    void testIniciarSessaoPDV_CaixaAberto() {
        // Abre o caixa primeiro
        pdvManager.abrirCaixa();
        
        // Inicia sessão
        boolean resultado = pdvManager.iniciarSessaoPDV(operador);
        
        // Verifica resultado
        assertTrue(resultado, "Iniciar sessão com caixa aberto deve retornar true");
        assertEquals(operador, pdvManager.getOperadorAtual(), "Operador deve ser definido");
        assertNotNull(pdvManager.getCarrinhoAtual(), "Carrinho deve ser criado");
    }
}
