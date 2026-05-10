package com.br.hermescomercial.pdv.patterns;

import com.br.hermescomercial.pdv.controller.*;
import com.br.hermescomercial.util.SystemLogger;
import javax.swing.JPanel;

/**
 * Factory Pattern - Fábrica Abstrata para Criação de Formulários
 * Centraliza a criação de formulários e facilita manutenção
 */
public class FormFactory {
    
    /**
     * Enum para tipos de formulários suportados
     */
    public enum FormType {
        DASHBOARD_ANALYTICS("📊 Dashboard Analytics", "dashboard"),
        METRICAS_DIA("📈 Métricas do Dia", "metrics"),
        RESUMO_FINANCEIRO("💰 Resumo Financeiro", "financial"),
        NOVA_VENDA("📋 Nova Venda", "sales"),
        CONSULTAR_VENDAS("🔍 Consultar Vendas", "sales"),
        GESTAO_CLIENTE("👥 Gestão de Cliente", "customers"),
        STATUS_ESTOQUE("📊 Status Estoque", "inventory"),
        DESPESAS("💸 Despesas", "financial"),
        FLUXO_CAIXA("📊 Fluxo de Caixa", "financial"),
        CONTAS_PAGAR("💸 Contas a Pagar", "financial"),
        CONTAS_RECEBER("💰 Contas a Receber", "financial"),
        GESTAO_PRODUTO("📦 Gestão Produto", "products"),
        GESTAO_FORNECEDOR("🏭 Gestão Fornecedores", "suppliers"),
        USUARIOS("👥 Usuários", "admin"),
        PERMISSOES("🔐 Permissões", "admin"),
        CONFIGURACOES("⚙️ Configurações Principais", "admin"),
        NOTIFICACOES("🔔 Central de Notificações", "notifications"),
        EXPORT_IMPORT("📤 Export/Import", "system"),
        BUSCA_AVANCADA("🔍 Busca Avançada", "system"),
        RELATORIO_VENDAS("💰 Relatório de Vendas", "reports"),
        RELATORIO_FINANCEIRO("📊 Relatório Financeiro", "reports");
        
        private final String displayName;
        private final String category;
        
        FormType(String displayName, String category) {
            this.displayName = displayName;
            this.category = category;
        }
        
        public String getDisplayName() { return displayName; }
        public String getCategory() { return category; }
    }
    
    /**
     * Interface para criadores de formulários
     */
    public interface FormCreator {
        JPanel createForm(JPanel workArea, String usuario, String nome);
        String getFormName();
    }
    
    // Map de criadores de formulários
    private static final java.util.Map<FormType, FormCreator> formCreators = new java.util.HashMap<>();
    
    // Inicialização estática dos criadores
    static {
        // Dashboard
        formCreators.put(FormType.DASHBOARD_ANALYTICS, new FormCreator() {
            public JPanel createForm(JPanel workArea, String usuario, String nome) {
                return PDVFormularioDashboard.criarFormularioDashboard();
            }
            public String getFormName() { return "Dashboard Analytics"; }
        });
        
        formCreators.put(FormType.METRICAS_DIA, new FormCreator() {
            public JPanel createForm(JPanel workArea, String usuario, String nome) {
                PDVFormularioMetricasDia form = new PDVFormularioMetricasDia(workArea, usuario, nome);
                return form.criarFormularioMetricasDiaInstance();
            }
            public String getFormName() { return "Métricas do Dia"; }
        });
        
        // Financeiro
        formCreators.put(FormType.RESUMO_FINANCEIRO, new FormCreator() {
            public JPanel createForm(JPanel workArea, String usuario, String nome) {
                PDVFormularioResumoFinanceiro form = new PDVFormularioResumoFinanceiro(workArea, usuario, nome);
                return form.criarFormularioResumoFinanceiro();
            }
            public String getFormName() { return "Resumo Financeiro"; }
        });
        
        formCreators.put(FormType.DESPESAS, new FormCreator() {
            public JPanel createForm(JPanel workArea, String usuario, String nome) {
                PDVFormularioDespesas form = new PDVFormularioDespesas(workArea, usuario, nome);
                return form.criarFormularioDespesas();
            }
            public String getFormName() { return "Despesas"; }
        });
        
        formCreators.put(FormType.FLUXO_CAIXA, new FormCreator() {
            public JPanel createForm(JPanel workArea, String usuario, String nome) {
                PDVFormularioFluxoCaixa form = new PDVFormularioFluxoCaixa(workArea, usuario, nome);
                return form.criarFormularioFluxoCaixa();
            }
            public String getFormName() { return "Fluxo de Caixa"; }
        });
        
        formCreators.put(FormType.CONTAS_PAGAR, new FormCreator() {
            public JPanel createForm(JPanel workArea, String usuario, String nome) {
                PDVFormularioContasPagar form = new PDVFormularioContasPagar(workArea, usuario, nome);
                return form.criarFormularioContasPagar();
            }
            public String getFormName() { return "Contas a Pagar"; }
        });
        
        // Vendas
        formCreators.put(FormType.NOVA_VENDA, new FormCreator() {
            public JPanel createForm(JPanel workArea, String usuario, String nome) {
                PDVFormularioNovaVenda form = new PDVFormularioNovaVenda(workArea, usuario, nome);
                return form.criarFormularioNovaVenda();
            }
            public String getFormName() { return "Nova Venda"; }
        });
        
        formCreators.put(FormType.CONSULTAR_VENDAS, new FormCreator() {
            public JPanel createForm(JPanel workArea, String usuario, String nome) {
                PDVFormularioConsultarVendas form = new PDVFormularioConsultarVendas(workArea, usuario, nome);
                return form.criarFormularioConsultarVendas();
            }
            public String getFormName() { return "Consultar Vendas"; }
        });
        
        // Clientes e Fornecedores
        formCreators.put(FormType.GESTAO_CLIENTE, new FormCreator() {
            public JPanel createForm(JPanel workArea, String usuario, String nome) {
                PDVFormularioGestaoCliente form = new PDVFormularioGestaoCliente(workArea, usuario, nome);
                return form.criarFormularioGestaoCliente();
            }
            public String getFormName() { return "Gestão de Cliente"; }
        });
        
        formCreators.put(FormType.GESTAO_FORNECEDOR, new FormCreator() {
            public JPanel createForm(JPanel workArea, String usuario, String nome) {
                PDVFormularioFornecedor form = new PDVFormularioFornecedor(workArea, usuario, nome);
                return form.criarFormularioFornecedor();
            }
            public String getFormName() { return "Gestão de Fornecedores"; }
        });
        
        // Estoque
        formCreators.put(FormType.STATUS_ESTOQUE, new FormCreator() {
            public JPanel createForm(JPanel workArea, String usuario, String nome) {
                PDVFormularioStatusEstoque form = new PDVFormularioStatusEstoque(workArea, usuario, nome);
                return form.criarFormularioStatusEstoque();
            }
            public String getFormName() { return "Status Estoque"; }
        });
        
        // Administração
        formCreators.put(FormType.USUARIOS, new FormCreator() {
            public JPanel createForm(JPanel workArea, String usuario, String nome) {
                PDVFormularioUsuarios form = new PDVFormularioUsuarios(workArea, usuario, nome);
                return form.criarFormularioUsuarios();
            }
            public String getFormName() { return "Usuários"; }
        });
        
        formCreators.put(FormType.PERMISSOES, new FormCreator() {
            public JPanel createForm(JPanel workArea, String usuario, String nome) {
                PDVFormularioPermissoes form = new PDVFormularioPermissoes(workArea, usuario, nome);
                return form.criarFormularioPermissoes();
            }
            public String getFormName() { return "Permissões"; }
        });
        
        // Relatórios
        formCreators.put(FormType.RELATORIO_VENDAS, new FormCreator() {
            public JPanel createForm(JPanel workArea, String usuario, String nome) {
                PDVFormularioRelatorioVendas form = new PDVFormularioRelatorioVendas(workArea, usuario, nome);
                return form.criarFormularioRelatorioVendas();
            }
            public String getFormName() { return "Relatório de Vendas"; }
        });
    }
    
    /**
     * Cria um formulário baseado no tipo
     */
    public static JPanel createForm(FormType type, JPanel workArea, String usuario, String nome) {
        FormCreator creator = formCreators.get(type);
        if (creator == null) {
            SystemLogger.ui("Formulário não encontrado para tipo: " + type);
            return createErrorForm(type);
        }
        
        SystemLogger.ui("Criando formulário via Factory: " + creator.getFormName());
        return creator.createForm(workArea, usuario, nome);
    }
    
    /**
     * Cria um formulário baseado no nome de exibição
     */
    public static JPanel createForm(String displayName, JPanel workArea, String usuario, String nome) {
        for (FormType type : FormType.values()) {
            if (type.getDisplayName().equals(displayName)) {
                return createForm(type, workArea, usuario, nome);
            }
        }
        
        SystemLogger.ui("Formulário não encontrado para display name: " + displayName);
        return createErrorForm(null);
    }
    
    /**
     * Obtém um tipo de formulário pelo nome de exibição
     */
    public static FormType getFormType(String displayName) {
        for (FormType type : FormType.values()) {
            if (type.getDisplayName().equals(displayName)) {
                return type;
            }
        }
        return null;
    }
    
    /**
     * Lista todos os formulários disponíveis por categoria
     */
    public static java.util.List<FormType> getFormsByCategory(String category) {
        return java.util.Arrays.stream(FormType.values())
                .filter(type -> type.getCategory().equals(category))
                .collect(java.util.stream.Collectors.toList());
    }
    
    /**
     * Lista todas as categorias disponíveis
     */
    public static java.util.List<String> getAvailableCategories() {
        return java.util.Arrays.stream(FormType.values())
                .map(FormType::getCategory)
                .distinct()
                .collect(java.util.stream.Collectors.toList());
    }
    
    /**
     * Verifica se um formulário está disponível
     */
    public static boolean isFormAvailable(String displayName) {
        return getFormType(displayName) != null;
    }
    
    /**
     * Obtém a categoria de um formulário
     */
    public static String getFormCategory(String displayName) {
        FormType type = getFormType(displayName);
        return type != null ? type.getCategory() : "unknown";
    }
    
    /**
     * Cria um painel de erro para formulários não encontrados
     */
    private static JPanel createErrorForm(FormType type) {
        JPanel errorPanel = new JPanel(new java.awt.BorderLayout());
        javax.swing.JLabel errorLabel = new javax.swing.JLabel(
            "❌ Formulário não disponível: " + (type != null ? type.getDisplayName() : "Desconhecido")
        );
        errorLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        errorLabel.setFont(new java.awt.Font("Arial", java.awt.Font.BOLD, 16));
        errorPanel.add(errorLabel, java.awt.BorderLayout.CENTER);
        return errorPanel;
    }
    
    /**
     * Obtém estatísticas da factory
     */
    public static java.util.Map<String, Integer> getFactoryStats() {
        java.util.Map<String, Integer> stats = new java.util.HashMap<>();
        for (FormType type : FormType.values()) {
            stats.merge(type.getCategory(), 1, Integer::sum);
        }
        return stats;
    }
    
    /**
     * Lista todos os formulários disponíveis
     */
    public static void listAvailableForms() {
        SystemLogger.ui("=== FORMULÁRIOS DISPONÍVEIS NA FACTORY ===");
        for (FormType type : FormType.values()) {
            SystemLogger.ui(String.format("%s (%s) - %s", 
                type.getDisplayName(), type.getCategory(), type.name()));
        }
    }
}
