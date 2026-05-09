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
    // PDVLoginSwingController removido - usando PDVLoginSimpleController
    
    public PDVPrincipalSwingController createPDVPrincipalController() {
        return new PDVPrincipalSwingController();
    }
    
    // Removido - usando apenas PDVMenuLateralElegante.java
    // public PDVVendaSwingController createPDVVendaController() {
    //     return new PDVVendaSwingController();
    // }
    
    // PDVProdutosUnificadoSwingController removido - usando PDVFormularioGestaoProduto com PDVMenuLateralElegante
    // public PDVProdutosUnificadoSwingController createPDVProdutoController() {
    //     return new PDVProdutosUnificadoSwingController();
    // }
    
    // PDVCaixaSwingController removido - usando PDVFormularioCaixa com PDVMenuLateralElegante
    // public PDVCaixaSwingController createPDVCaixaController() {
    //     return new PDVCaixaSwingController();
    // }
    
    // PDVDashboardSwingController removido - usando PDVFormularioDashboard com PDVMenuLateralElegante
    // public PDVDashboardSwingController createPDVDashboardController() {
    //     return new PDVDashboardSwingController();
    // }
    
    // PDVRelatoriosSwingController removido - usando PDVFormularioRelatorios com PDVMenuLateralElegante
    // public PDVRelatoriosSwingController createPDVRelatorioController() {
    //     return new PDVRelatoriosSwingController();
    // }
    
    // PDVConfiguracoesSwingController removido - usando PDVFormularioConfiguracoes com PDVMenuLateralElegante
    // public PDVConfiguracoesSwingController createPDVConfiguracaoController() {
    //     return new PDVConfiguracoesSwingController();
    // }
    
    // PDVFecharCaixaSwingController removido - usando PDVFormularioFecharCaixa com PDVMenuLateralElegante
    // public PDVFecharCaixaSwingController createPDVFecharCaixaController() {
    //     return new PDVFecharCaixaSwingController();
    // }
    
    // PDVNotificacaoSwingController removido - usando PDVFormularioNotificacoes com PDVMenuLateralElegante
    // public PDVNotificacaoSwingController createPDVNotificacaoController() {
    //     return new PDVNotificacaoSwingController("admin");
    // }
    
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
    // TemplateRelatorioSwingController removido - usando PDVFormularioRelatorios com PDVMenuLateralElegante
    // public TemplateRelatorioSwingController createTemplateRelatorioController() {
    //     return new TemplateRelatorioSwingController();
    // }
    
    // FornecedorSwingController removido - usando PDVFormularioFornecedor com PDVMenuLateralElegante
    // public FornecedorSwingController createFornecedorController() {
    //     return new FornecedorSwingController();
    // }
    
    // PagamentoAPISwingController removido - usando PDVFormularioPagamentoAPI com PDVMenuLateralElegante
    // public PagamentoAPISwingController createPagamentoAPIController() {
    //     return new PagamentoAPISwingController();
    // }
    
    // BuscaAvancadaSwingController removido - usando PDVFormularioBuscaAvancada com PDVMenuLateralElegante
    // public BuscaAvancadaSwingController createBuscaAvancadaController() {
    //     return new BuscaAvancadaSwingController();
    // }
    
    // ExportImportSwingController removido - usando PDVFormularioExportImport com PDVMenuLateralElegante
    // public ExportImportSwingController createExportImportController() {
    //     return new ExportImportSwingController();
    // }
    
    // CacheConfigSwingController removido - usando PDVFormularioCacheConfig com PDVMenuLateralElegante
    // public CacheConfigSwingController createCacheConfigController() {
    //     return new CacheConfigSwingController();
    // }
    
    // SistemaConfigSwingController removido - usando PDVFormularioConfiguracoes com PDVMenuLateralElegante
    // public SistemaConfigSwingController createSistemaConfigController() {
    //     return new SistemaConfigSwingController();
    // }
    
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
                // PDVLoginSwingController removido - usando PDVLoginSimpleController
                case "PDVPrincipalSwingController":
                    return (T) createPDVPrincipalController();
                // Removido - usando apenas PDVMenuLateralElegante.java
                // case "PDVVendaSwingController":
                //     return (T) createPDVVendaController();
                // PDVProdutosUnificadoSwingController removido - usando PDVFormularioGestaoProduto com PDVMenuLateralElegante
                // case "PDVProdutosUnificadoSwingController":
                //     return (T) createPDVProdutoController();
                // PDVCaixaSwingController removido - usando PDVFormularioCaixa com PDVMenuLateralElegante
                // case "PDVCaixaSwingController":
                //     return (T) createPDVCaixaController();
                // PDVDashboardSwingController removido - usando PDVFormularioDashboard com PDVMenuLateralElegante
                // case "PDVDashboardSwingController":
                //     return (T) createPDVDashboardController();
                // PDVRelatoriosSwingController removido - usando PDVFormularioRelatorios com PDVMenuLateralElegante
                // case "PDVRelatoriosSwingController":
                //     return (T) createPDVRelatorioController();
                // PDVConfiguracoesSwingController removido - usando PDVFormularioConfiguracoes com PDVMenuLateralElegante
                // case "PDVConfiguracoesSwingController":
                //     return (T) createPDVConfiguracaoController();
                // PDVFecharCaixaSwingController removido - usando PDVFormularioFecharCaixa com PDVMenuLateralElegante
                // case "PDVFecharCaixaSwingController":
                //     return (T) createPDVFecharCaixaController();
                // PDVNotificacaoSwingController removido - usando PDVFormularioNotificacoes com PDVMenuLateralElegante
                // case "PDVNotificacaoSwingController":
                //     return (T) createPDVNotificacaoController();
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
                // FornecedorSwingController removido - usando PDVFormularioFornecedor com PDVMenuLateralElegante
                // case "FornecedorSwingController":
                //     return (T) createFornecedorController();
                // TemplateRelatorioSwingController removido - usando PDVFormularioRelatorios com PDVMenuLateralElegante
                // case "TemplateRelatorioSwingController":
                //     return (T) createTemplateRelatorioController();
                // PagamentoAPISwingController removido - usando PDVFormularioPagamentoAPI com PDVMenuLateralElegante
                // case "PagamentoAPISwingController":
                //     return (T) createPagamentoAPIController();
                // BuscaAvancadaSwingController removido - usando PDVFormularioBuscaAvancada com PDVMenuLateralElegante
                // case "BuscaAvancadaSwingController":
                //     return (T) createBuscaAvancadaController();
                // ExportImportSwingController removido - usando PDVFormularioExportImport com PDVMenuLateralElegante
                // case "ExportImportSwingController":
                //     return (T) createExportImportController();
                // CacheConfigSwingController removido - usando PDVFormularioCacheConfig com PDVMenuLateralElegante
                // case "CacheConfigSwingController":
                //     return (T) createCacheConfigController();
                // SistemaConfigSwingController removido - usando PDVFormularioConfiguracoes com PDVMenuLateralElegante
                // case "SistemaConfigSwingController":
                //     return (T) createSistemaConfigController();
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
