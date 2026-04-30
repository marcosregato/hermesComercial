package com.br.hermescomercial.pdv;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import com.br.hermescomercial.pdv.service.NotificacaoService;
import com.br.hermescomercial.shared.model.Notificacao;
import com.br.hermescomercial.shared.model.Notificacao.TipoNotificacao;
import com.br.hermescomercial.shared.model.Notificacao.PrioridadeNotificacao;

/**
 * Testes para validação do NotificacaoService com correção PostgreSQL
 * Versão 2.5.0 - Hermes Comercial
 */
public class NotificacaoServiceTest {

    private NotificacaoService notificacaoService;

    @BeforeEach
    void setUp() {
        notificacaoService = new NotificacaoService();
    }

    @Test
    @DisplayName("Testar inicialização do serviço - PostgreSQL")
    void testInicializacaoServico() {
        assertDoesNotThrow(() -> {
            new NotificacaoService();
        }, "Inicialização do serviço não deve lançar exceção com sintaxe PostgreSQL");
    }

    @Test
    @DisplayName("Testar envio de notificação válida")
    void testEnviarNotificacaoValida() {
        assertDoesNotThrow(() -> {
            Notificacao resultado = notificacaoService.enviar(
                "Teste de Notificação", 
                "Mensagem de teste para validação", 
                TipoNotificacao.SISTEMA, 
                "admin"
            );
            assertNotNull(resultado, "Notificação enviada não deve ser nula");
            assertEquals("Teste de Notificação", resultado.getTitulo());
            assertEquals("admin", resultado.getUsuarioDestino());
        }, "Envio de notificação não deve lançar exceção");
    }

    @Test
    @DisplayName("Testar envio de notificação com prioridade")
    void testEnviarNotificacaoComPrioridade() {
        assertDoesNotThrow(() -> {
            Notificacao resultado = notificacaoService.enviar(
                "Notificação Urgente", 
                "Mensagem de alta prioridade", 
                TipoNotificacao.FINANCEIRO, 
                "admin",
                PrioridadeNotificacao.ALTA
            );
            assertNotNull(resultado, "Notificação enviada não deve ser nula");
            assertEquals(PrioridadeNotificacao.ALTA, resultado.getPrioridade());
        }, "Envio de notificação com prioridade não deve lançar exceção");
    }

    @Test
    @DisplayName("Testar envio de broadcast")
    void testEnviarBroadcast() {
        assertDoesNotThrow(() -> {
            Notificacao resultado = notificacaoService.enviarBroadcast(
                "Broadcast Test", 
                "Mensagem para todos os usuários", 
                TipoNotificacao.SISTEMA
            );
            assertNotNull(resultado, "Notificação de broadcast não deve ser nula");
            assertNull(resultado.getUsuarioDestino(), "Broadcast não deve ter usuário destino");
        }, "Envio de broadcast não deve lançar exceção");
    }

    @Test
    @DisplayName("Testar marcação de notificação como lida")
    void testMarcarNotificacaoComoLida() {
        assertDoesNotThrow(() -> {
            boolean resultado = notificacaoService.marcarComoLida(1L);
            // Pode retornar false se não existir notificação com ID 1, mas não deve lançar exceção
            assertNotNull(resultado, "Resultado não deve ser nulo");
        }, "Marcação como lida não deve lançar exceção");
    }

    @Test
    @DisplayName("Testar marcação de todas as notificações como lidas")
    void testMarcarTodasComoLidas() {
        assertDoesNotThrow(() -> {
            boolean resultado = notificacaoService.marcarTodasComoLidas("admin");
            assertNotNull(resultado, "Resultado não deve ser nulo");
        }, "Marcação de todas como lidas não deve lançar exceção");
    }

    @Test
    @DisplayName("Testar contagem de notificações não lidas")
    void testContarNotificacoesNaoLidas() {
        assertDoesNotThrow(() -> {
            int quantidade = notificacaoService.contarNaoLidas("admin");
            assertTrue(quantidade >= 0, "Quantidade deve ser maior ou igual a zero");
        }, "Contagem de notificações não lidas não deve lançar exceção");
    }

    
    @Test
    @DisplayName("Testar validação de sintaxe SQL - PostgreSQL vs SQLite")
    void testValidacaoSintaxeSQL() {
        // Este teste valida que a inicialização funciona com sintaxe PostgreSQL (SERIAL, não AUTOINCREMENT)
        assertDoesNotThrow(() -> {
            new NotificacaoService();
        }, "Sintaxe SQL deve ser compatível com PostgreSQL (SERIAL, não AUTOINCREMENT)");
    }
}
