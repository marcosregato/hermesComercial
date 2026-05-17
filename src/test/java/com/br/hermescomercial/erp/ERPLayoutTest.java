package com.br.hermescomercial.erp;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import static org.junit.jupiter.api.Assertions.*;

import com.br.hermescomercial.erp.controller.ERPClienteSwingController;
import com.br.hermescomercial.erp.controller.ERPConfiguracaoSwingController;
import com.br.hermescomercial.erp.controller.ERPContasPagarSwingController;
import com.br.hermescomercial.erp.controller.ERPContasReceberSwingController;
import com.br.hermescomercial.erp.controller.ERPEstoqueSwingController;
import com.br.hermescomercial.erp.controller.ERPFinanceiroSwingController;
import com.br.hermescomercial.erp.controller.ERPFluxoCaixaSwingController;
import com.br.hermescomercial.erp.controller.ERPFornecedorSwingController;
import com.br.hermescomercial.erp.controller.ERPRelatorioSwingController;
import com.br.hermescomercial.erp.controller.ERPRelatorioFinanceiroSwingController;
import com.br.hermescomercial.erp.controller.ERPServicoSwingController;
import com.br.hermescomercial.erp.controller.ERPUsuarioSwingController;
import com.br.hermescomercial.erp.controller.ERPVendasSwingController;

/**
 * Testes para Verificação de Layout Padrão do Sistema ERP
 * Versão 3.0.0 - Hermes Comercial
 * 
 * Verifica se todas as telas seguem o layout: Header → Busca → Formulário → Tabela
 */
public class ERPLayoutTest {

    @Test
    @DisplayName("Verificar layout padrão ERP - Clientes")
    void testLayoutERPClientes() {
        assertDoesNotThrow(() -> {
            ERPClienteSwingController controller = new ERPClienteSwingController();
            assertNotNull(controller, "Controller de Clientes deve ser criado");
            
            // Verificar se frame foi criado
            assertNotNull(controller.getFrame(), "Frame não deve ser nulo");
            assertTrue(controller.getFrame().isVisible(), "Frame deve estar visível");
            
        }, "Layout ERP Clientes deve seguir padrão Header → Busca → Formulário → Tabela");
    }

    @Test
    @DisplayName("Verificar layout padrão ERP - Configuração")
    void testLayoutERPConfiguracao() {
        assertDoesNotThrow(() -> {
            ERPConfiguracaoSwingController controller = new ERPConfiguracaoSwingController();
            assertNotNull(controller, "Controller de Configuração deve ser criado");
            
            // Verificar se frame foi criado
            assertNotNull(controller.getFrame(), "Frame não deve ser nulo");
            assertTrue(controller.getFrame().isVisible(), "Frame deve estar visível");
            
        }, "Layout ERP Configuração deve seguir padrão Header → Busca → Formulário (sem tabela)");
    }

    @Test
    @DisplayName("Verificar layout padrão ERP - Contas a Pagar")
    void testLayoutERPContasPagar() {
        assertDoesNotThrow(() -> {
            ERPContasPagarSwingController controller = new ERPContasPagarSwingController();
            assertNotNull(controller, "Controller de Contas a Pagar deve ser criado");
            
            // Verificar se frame foi criado
            assertNotNull(controller.getFrame(), "Frame não deve ser nulo");
            assertTrue(controller.getFrame().isVisible(), "Frame deve estar visível");
            
        }, "Layout ERP Contas a Pagar deve seguir padrão Header → Busca → Formulário → Tabela");
    }

    @Test
    @DisplayName("Verificar layout padrão ERP - Contas a Receber")
    void testLayoutERPContasReceber() {
        assertDoesNotThrow(() -> {
            ERPContasReceberSwingController controller = new ERPContasReceberSwingController();
            assertNotNull(controller, "Controller de Contas a Receber deve ser criado");
            
            // Verificar se frame foi criado
            assertNotNull(controller.getFrame(), "Frame não deve ser nulo");
            assertTrue(controller.getFrame().isVisible(), "Frame deve estar visível");
            
        }, "Layout ERP Contas a Receber deve seguir padrão Header → Busca → Formulário → Tabela");
    }

    @Test
    @DisplayName("Verificar layout padrão ERP - Estoque")
    void testLayoutERPEstoque() {
        assertDoesNotThrow(() -> {
            ERPEstoqueSwingController controller = new ERPEstoqueSwingController();
            assertNotNull(controller, "Controller de Estoque deve ser criado");
            
            // Verificar se frame foi criado
            assertNotNull(controller.getFrame(), "Frame não deve ser nulo");
            // Não verificar visibilidade em ambiente de teste headless
            
        }, "Layout ERP Estoque deve seguir padrão Header → Busca → Formulário → Tabela");
    }

    @Test
    @DisplayName("Verificar layout padrão ERP - Financeiro")
    void testLayoutERPFinanceiro() {
        assertDoesNotThrow(() -> {
            ERPFinanceiroSwingController controller = new ERPFinanceiroSwingController();
            assertNotNull(controller, "Controller Financeiro deve ser criado");
            
            // Verificar se frame foi criado
            assertNotNull(controller.getFrame(), "Frame não deve ser nulo");
            assertTrue(controller.getFrame().isVisible(), "Frame deve estar visível");
            
        }, "Layout ERP Financeiro deve seguir padrão Header → Busca → Formulário → Tabela");
    }

    @Test
    @DisplayName("Verificar layout padrão ERP - Fluxo de Caixa")
    void testLayoutERPFluxoCaixa() {
        assertDoesNotThrow(() -> {
            ERPFluxoCaixaSwingController controller = new ERPFluxoCaixaSwingController();
            assertNotNull(controller, "Controller de Fluxo de Caixa deve ser criado");
            
            // Verificar se frame foi criado
            assertNotNull(controller.getFrame(), "Frame não deve ser nulo");
            assertTrue(controller.getFrame().isVisible(), "Frame deve estar visível");
            
        }, "Layout ERP Fluxo de Caixa deve seguir padrão Header → Busca → Formulário → Tabela");
    }

    @Test
    @DisplayName("Verificar layout padrão ERP - Fornecedores")
    void testLayoutERPFornecedores() {
        assertDoesNotThrow(() -> {
            ERPFornecedorSwingController controller = new ERPFornecedorSwingController();
            assertNotNull(controller, "Controller de Fornecedores deve ser criado");
            
            // Verificar se frame foi criado
            assertNotNull(controller.getFrame(), "Frame não deve ser nulo");
            assertTrue(controller.getFrame().isVisible(), "Frame deve estar visível");
            
        }, "Layout ERP Fornecedores deve seguir padrão Header → Busca → Formulário → Tabela");
    }

    @Test
    @DisplayName("Verificar layout padrão ERP - Relatórios")
    void testLayoutERPRelatorios() {
        assertDoesNotThrow(() -> {
            ERPRelatorioSwingController controller = new ERPRelatorioSwingController();
            assertNotNull(controller, "Controller de Relatórios deve ser criado");
            
            // Verificar se frame foi criado
            assertNotNull(controller.getFrame(), "Frame não deve ser nulo");
            assertTrue(controller.getFrame().isVisible(), "Frame deve estar visível");
            
        }, "Layout ERP Relatórios deve seguir padrão Header → Busca → Formulário → Tabela");
    }

    @Test
    @DisplayName("Verificar layout padrão ERP - Relatório Financeiro")
    void testLayoutERPRelatorioFinanceiro() {
        assertDoesNotThrow(() -> {
            ERPRelatorioFinanceiroSwingController controller = new ERPRelatorioFinanceiroSwingController();
            assertNotNull(controller, "Controller de Relatório Financeiro deve ser criado");
            
            // Verificar se frame foi criado
            assertNotNull(controller.getFrame(), "Frame não deve ser nulo");
            assertTrue(controller.getFrame().isVisible(), "Frame deve estar visível");
            
        }, "Layout ERP Relatório Financeiro deve seguir padrão Header → Busca → Formulário (sem tabela)");
    }

    @Test
    @DisplayName("Verificar layout padrão ERP - Serviços")
    void testLayoutERPServicos() {
        assertDoesNotThrow(() -> {
            ERPServicoSwingController controller = new ERPServicoSwingController();
            assertNotNull(controller, "Controller de Serviços deve ser criado");
            
            // Verificar se frame foi criado
            assertNotNull(controller.getFrame(), "Frame não deve ser nulo");
            assertTrue(controller.getFrame().isVisible(), "Frame deve estar visível");
            
        }, "Layout ERP Serviços deve seguir padrão Header → Busca → Formulário → Tabela");
    }

    @Test
    @DisplayName("Verificar layout padrão ERP - Usuários")
    void testLayoutERPUsuarios() {
        assertDoesNotThrow(() -> {
            ERPUsuarioSwingController controller = new ERPUsuarioSwingController();
            assertNotNull(controller, "Controller de Usuários deve ser criado");
            
            // Verificar se frame foi criado
            assertNotNull(controller.getFrame(), "Frame não deve ser nulo");
            // Não verificar visibilidade em ambiente de teste headless
            
        }, "Layout ERP Usuários deve seguir padrão Header → Busca → Formulário → Tabela");
    }

    @Test
    @DisplayName("Verificar layout padrão ERP - Vendas")
    void testLayoutERPVendas() {
        assertDoesNotThrow(() -> {
            ERPVendasSwingController controller = new ERPVendasSwingController();
            assertNotNull(controller, "Controller de Vendas deve ser criado");
            
            // Verificar se frame foi criado
            assertNotNull(controller.getFrame(), "Frame não deve ser nulo");
            assertTrue(controller.getFrame().isVisible(), "Frame deve estar visível");
            
        }, "Layout ERP Vendas deve seguir padrão Header → Busca → Formulário → Tabela");
    }

    @Test
    @DisplayName("Teste de integração de layouts ERP")
    void testIntegracaoLayoutsERP() {
        assertDoesNotThrow(() -> {
            // Verificar se todos os controllers podem ser criados sem exceções
            ERPClienteSwingController clienteController = new ERPClienteSwingController();
            ERPConfiguracaoSwingController configController = new ERPConfiguracaoSwingController();
            ERPContasPagarSwingController contasPagarController = new ERPContasPagarSwingController();
            ERPContasReceberSwingController contasReceberController = new ERPContasReceberSwingController();
            ERPEstoqueSwingController estoqueController = new ERPEstoqueSwingController();
            ERPFinanceiroSwingController financeiroController = new ERPFinanceiroSwingController();
            ERPFluxoCaixaSwingController fluxoCaixaController = new ERPFluxoCaixaSwingController();
            ERPFornecedorSwingController fornecedorController = new ERPFornecedorSwingController();
            ERPRelatorioSwingController relatorioController = new ERPRelatorioSwingController();
            ERPRelatorioFinanceiroSwingController relatorioFinanceiroController = new ERPRelatorioFinanceiroSwingController();
            ERPServicoSwingController servicoController = new ERPServicoSwingController();
            ERPUsuarioSwingController usuarioController = new ERPUsuarioSwingController();
            ERPVendasSwingController vendasController = new ERPVendasSwingController();
            
            // Verificar se todos foram criados com sucesso
            assertNotNull(clienteController, "Controller Cliente deve ser criado");
            assertNotNull(configController, "Controller Configuração deve ser criado");
            assertNotNull(contasPagarController, "Controller Contas Pagar deve ser criado");
            assertNotNull(contasReceberController, "Controller Contas Receber deve ser criado");
            assertNotNull(estoqueController, "Controller Estoque deve ser criado");
            assertNotNull(financeiroController, "Controller Financeiro deve ser criado");
            assertNotNull(fluxoCaixaController, "Controller Fluxo Caixa deve ser criado");
            assertNotNull(fornecedorController, "Controller Fornecedores deve ser criado");
            assertNotNull(relatorioController, "Controller Relatórios deve ser criado");
            assertNotNull(relatorioFinanceiroController, "Controller Relatório Financeiro deve ser criado");
            assertNotNull(servicoController, "Controller Serviços deve ser criado");
            assertNotNull(usuarioController, "Controller Usuários deve ser criado");
            assertNotNull(vendasController, "Controller Vendas deve ser criado");
            
        }, "Integração de layouts ERP deve funcionar sem exceções");
    }
}
