package com.br.hermescomercial.pdv;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import static org.junit.jupiter.api.Assertions.*;

import com.br.hermescomercial.pdv.controller.SistemaConfigSwingController;

/**
 * Testes para tela de Configurações do Sistema PDV
 * Versão 3.0.0 - Hermes Comercial
 * 
 * Tela IMPORTANTE para configurações globais do sistema
 * Prioridade: ALTA
 */
public class SistemaConfigTest {

    @Test
    @DisplayName("Teste de criação do controller Configurações")
    void testSistemaConfigControllerCreation() {
        assertDoesNotThrow(() -> {
            SistemaConfigSwingController controller = new SistemaConfigSwingController();
            assertNotNull(controller);
        }, "Controller Configurações deve ser criado sem exceções");
    }

    @Test
    @DisplayName("Teste de inicialização de componentes de configuração")
    void testInicializacaoComponentesConfiguracao() {
        assertDoesNotThrow(() -> {
            SistemaConfigSwingController controller = new SistemaConfigSwingController();
            
            // Verificar se frame foi inicializado
            assertNotNull(controller.getFrame(), "Frame de configurações não deve ser nulo");
            assertTrue(controller.getFrame().isVisible(), "Frame deve estar visível após criação");
            
            // Verificar se abas foram criadas
            assertNotNull(controller.getTabbedPane(), "Abas não devem ser nulas");
            assertEquals(5, controller.getTabbedPane().getTabCount(), "Devem existir 5 abas de configuração");
            
        }, "Componentes de configuração devem ser inicializados sem exceções");
    }

    @Test
    @DisplayName("Teste de configurações de sistema")
    void testConfiguracoesSistema() {
        assertDoesNotThrow(() -> {
            SistemaConfigSwingController controller = new SistemaConfigSwingController();
            
            // Testar configurações padrão
            assertDoesNotThrow(() -> {
                controller.carregarConfiguracoesPadrao();
            }, "Carregamento de configurações padrão não deve lançar exceções");
            
        }, "Configurações de sistema devem funcionar corretamente");
    }

    @Test
    @DisplayName("Teste de configurações de banco de dados")
    void testConfiguracoesBancoDados() {
        assertDoesNotThrow(() -> {
            SistemaConfigSwingController controller = new SistemaConfigSwingController();
            
            // Testar configurações de banco
            assertDoesNotThrow(() -> {
                controller.configurarBancoDados("localhost", "5432", "hermes_comercial", "postgres", "admin", "123456");
            }, "Configurações de banco de dados não devem lançar exceções");
            
        }, "Configurações de banco de dados devem funcionar corretamente");
    }

    @Test
    @DisplayName("Teste de configurações de interface")
    void testConfiguracoesInterface() {
        assertDoesNotThrow(() -> {
            SistemaConfigSwingController controller = new SistemaConfigSwingController();
            
            // Testar configurações de interface
            assertDoesNotThrow(() -> {
                controller.configurarInterface("Tema Claro", "Português", "USD", "dd/MM/yyyy");
            }, "Configurações de interface não devem lançar exceções");
            
        }, "Configurações de interface devem funcionar corretamente");
    }

    @Test
    @DisplayName("Teste de backup e restauração")
    void testBackupRestauracao() {
        assertDoesNotThrow(() -> {
            SistemaConfigSwingController controller = new SistemaConfigSwingController();
            
            // Testar backup
            assertDoesNotThrow(() -> {
                controller.realizarBackup();
            }, "Realização de backup não deve lançar exceções");
            
            // Testar restauração
            assertDoesNotThrow(() -> {
                controller.restaurarBackup();
            }, "Restauração de backup não deve lançar exceções");
            
        }, "Backup e restauração devem funcionar corretamente");
    }

    @Test
    @DisplayName("Teste de validação de configurações")
    void testValidacaoConfiguracoes() {
        assertDoesNotThrow(() -> {
            SistemaConfigSwingController controller = new SistemaConfigSwingController();
            
            // Testar validação
            assertDoesNotThrow(() -> {
                controller.validarConfiguracoes();
            }, "Validação de configurações não deve lançar exceções");
            
        }, "Validação de configurações deve funcionar corretamente");
    }

    @Test
    @DisplayName("Teste de salvamento de configurações")
    void testSalvamentoConfiguracoes() {
        assertDoesNotThrow(() -> {
            SistemaConfigSwingController controller = new SistemaConfigSwingController();
            
            // Testar salvamento
            assertDoesNotThrow(() -> {
                controller.salvarConfiguracoes();
            }, "Salvamento de configurações não deve lançar exceções");
            
        }, "Salvamento de configurações deve funcionar corretamente");
    }
}
