package com.br.hermescomercial.factory;

import com.br.hermescomercial.pdv.controller.*;
import com.br.hermescomercial.erp.controller.*;

/**
 * Factory Pattern para criação de controllers
 * Centraliza a criação e instanciação de todos os controllers do sistema
 * Versão 1.0.0 - Design Pattern Implementation
 */
public class ControllerFactory {
    
    private static ControllerFactory instance;
    
    // Singleton da Factory
    private ControllerFactory() {}
    
    public static ControllerFactory getInstance() {
        if (instance == null) {
            instance = new ControllerFactory();
        }
        return instance;
    }
    
    // Controllers PDV
    public PDVLoginSwingController createPDVLoginController() {
        return new PDVLoginSwingController();
    }
    
    public PDVPrincipalSwingController createPDVPrincipalController() {
        return new PDVPrincipalSwingController();
    }
    
    public PDVVendaSwingController createPDVVendaController() {
        return new PDVVendaSwingController();
    }
    
    public PDVProdutosUnificadoSwingController createPDVProdutoController() {
        return new PDVProdutosUnificadoSwingController();
    }
    
    public PDVCaixaSwingController createPDVCaixaController() {
        return new PDVCaixaSwingController();
    }
    
    public PDVDashboardSwingController createPDVDashboardController() {
        return new PDVDashboardSwingController();
    }
    
    public PDVRelatoriosSwingController createPDVRelatorioController() {
        return new PDVRelatoriosSwingController();
    }
    
    public PDVConfiguracoesSwingController createPDVConfiguracaoController() {
        return new PDVConfiguracoesSwingController();
    }
    
    public PDVFecharCaixaSwingController createPDVFecharCaixaController() {
        return new PDVFecharCaixaSwingController();
    }
    
    public PDVNotificacaoSwingController createPDVNotificacaoController() {
        return new PDVNotificacaoSwingController("admin");
    }
    
    // Controllers ERP
    public ERPMenuPrincipalSwingController createERPMenuPrincipalController() {
        return new ERPMenuPrincipalSwingController();
    }
    
    public ERPClienteSwingController createERPClienteController() {
        return new ERPClienteSwingController();
    }
    
    public ERPFinanceiroSwingController createERPFinanceiroController() {
        return new ERPFinanceiroSwingController();
    }
    
    public ERPGestaoFinanceiraSwingController createERPGestaoFinanceiraController() {
        return new ERPGestaoFinanceiraSwingController();
    }
    
    public ERPProdutoSwingController createERPProdutoController() {
        return new ERPProdutoSwingController();
    }
    
    public ERPEstoqueSwingController createERPEstoqueController() {
        return new ERPEstoqueSwingController();
    }
    
    public ERPConfiguracaoSwingController createERPConfiguracaoController() {
        return new ERPConfiguracaoSwingController();
    }
    
    public ERPUsuarioSwingController createERPUsuarioController() {
        return new ERPUsuarioSwingController();
    }
    
    public ERPRelatorioSwingController createERPRelatorioController() {
        return new ERPRelatorioSwingController();
    }
    
    public ERPRelatorioFinanceiroSwingController createERPRelatorioFinanceiroController() {
        return new ERPRelatorioFinanceiroSwingController();
    }
    
    // Controllers Compartilhados
    public TemplateRelatorioSwingController createTemplateRelatorioController() {
        return new TemplateRelatorioSwingController();
    }
    
    public FornecedorSwingController createFornecedorController() {
        return new FornecedorSwingController();
    }
    
    public PagamentoAPISwingController createPagamentoAPIController() {
        return new PagamentoAPISwingController();
    }
    
    public BuscaAvancadaSwingController createBuscaAvancadaController() {
        return new BuscaAvancadaSwingController();
    }
    
    public ExportImportSwingController createExportImportController() {
        return new ExportImportSwingController();
    }
    
    public CacheConfigSwingController createCacheConfigController() {
        return new CacheConfigSwingController();
    }
    
    public SistemaConfigSwingController createSistemaConfigController() {
        return new SistemaConfigSwingController();
    }
    
    /**
     * Método genérico para criar controllers por tipo
     * @param controllerType Tipo do controller
     * @return Instância do controller ou null se não encontrado
     */
    @SuppressWarnings("unchecked")
    public <T> T createController(Class<T> controllerType) {
        try {
            String className = controllerType.getSimpleName();
            
            // Mapeamento dos controllers
            switch (className) {
                case "PDVLoginSwingController":
                    return (T) createPDVLoginController();
                case "PDVPrincipalSwingController":
                    return (T) createPDVPrincipalController();
                case "PDVVendaSwingController":
                    return (T) createPDVVendaController();
                case "PDVProdutosUnificadoSwingController":
                    return (T) createPDVProdutoController();
                case "PDVCaixaSwingController":
                    return (T) createPDVCaixaController();
                case "PDVDashboardSwingController":
                    return (T) createPDVDashboardController();
                case "PDVRelatoriosSwingController":
                    return (T) createPDVRelatorioController();
                case "PDVConfiguracoesSwingController":
                    return (T) createPDVConfiguracaoController();
                case "PDVFecharCaixaSwingController":
                    return (T) createPDVFecharCaixaController();
                case "PDVNotificacaoSwingController":
                    return (T) createPDVNotificacaoController();
                case "ERPMenuPrincipalSwingController":
                    return (T) createERPMenuPrincipalController();
                case "ERPClienteSwingController":
                    return (T) createERPClienteController();
                case "ERPFinanceiroSwingController":
                    return (T) createERPFinanceiroController();
                case "ERPProdutoSwingController":
                    return (T) createERPProdutoController();
                case "ERPEstoqueSwingController":
                    return (T) createERPEstoqueController();
                case "ERPConfiguracaoSwingController":
                    return (T) createERPConfiguracaoController();
                case "ERPUsuarioSwingController":
                    return (T) createERPUsuarioController();
                case "ERPRelatorioSwingController":
                    return (T) createERPRelatorioController();
                case "ERPRelatorioFinanceiroSwingController":
                    return (T) createERPRelatorioFinanceiroController();
                case "FornecedorSwingController":
                    return (T) createFornecedorController();
                case "TemplateRelatorioSwingController":
                    return (T) createTemplateRelatorioController();
                case "PagamentoAPISwingController":
                    return (T) createPagamentoAPIController();
                case "BuscaAvancadaSwingController":
                    return (T) createBuscaAvancadaController();
                case "ExportImportSwingController":
                    return (T) createExportImportController();
                case "CacheConfigSwingController":
                    return (T) createCacheConfigController();
                case "SistemaConfigSwingController":
                    return (T) createSistemaConfigController();
                default:
                    System.err.println("Controller não encontrado: " + className);
                    return null;
            }
        } catch (Exception e) {
            System.err.println("Erro ao criar controller " + controllerType.getSimpleName() + ": " + e.getMessage());
            return null;
        }
    }
}
