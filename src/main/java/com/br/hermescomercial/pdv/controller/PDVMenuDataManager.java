package com.br.hermescomercial.pdv.controller;

import com.br.hermescomercial.util.SystemLogger;
import java.util.HashMap;
import java.util.Map;

/**
 * Classe especializada para gestão de dados e configurações do menu lateral
 * Extrai responsabilidades de dados do PDVMenuLateralElegante
 */
public class PDVMenuDataManager {
    
    // Dados do usuário e estado
    private String usuarioAtual;
    private String nomeUsuario;
    private String telaAtual;
    private String moduloAtual;
    private String tipoTelaAtual;
    private long timestampTelaAtual;
    
    // Cache de submenus para performance
    private Map<String, String[]> subMenusCache;
    
    // Estrutura de menus principais - Otimizada e simplificada
    private static final String[] MENUS_PRINCIPAIS = {
        "🏠 Dashboard", "📋 Vendas", "📦 Produtos", "👥 Clientes", 
        "📊 Estoque", "💰 Financeiro", "💳 Pagamentos API", "📈 Relatórios", 
        "� Export/Import", "🔍 Busca Avançada", "⚙️ Configurações", "🔔 Notificações"
    };
    
    public PDVMenuDataManager(String usuarioAtual, String nomeUsuario) {
        this.usuarioAtual = usuarioAtual;
        this.nomeUsuario = nomeUsuario;
        this.subMenusCache = new HashMap<>();
        inicializarCacheSubMenus();
    }
    
    /**
     * Inicializa o cache de submenus para melhor performance
     * Estrutura reorganizada para eliminar redundâncias e melhorar fluxo de trabalho
     */
    private void inicializarCacheSubMenus() {
        // Dashboard - Visão geral simplificada
        subMenusCache.put("🏠 Dashboard", new String[]{
            "📊 Dashboard Analytics", "📈 Métricas do Dia", "💰 Resumo Financeiro", 
            "📦 Status Estoque", "🔔 Alertas Ativos"
        });
        
        // Vendas - Fluxo essencial de vendas
        subMenusCache.put("📋 Vendas", new String[]{
            "📋 Nova Venda", "🔍 Consultar Vendas", "↩️ Devoluções", 
            "🏷️ Orçamentos", "🚚 Entregas"
        });
        
        // Produtos - Gestão essencial de produtos
        subMenusCache.put("📦 Produtos", new String[]{
            "📦 Gestão Produto", "🔍 Consultar Produto", "📂 Categorias", 
            "🏭 Gestão Fornecedores"
        });
        
        // Clientes - Gestão unificada
        subMenusCache.put("👥 Clientes", new String[]{
            "👥 Gestão de Cliente", "📊 Histórico de Compras"
        });
        
        // Estoque - Controle essencial
        subMenusCache.put("📊 Estoque", new String[]{
            "📊 Status Estoque", "📊 Consultar Estoque", "📦 Estoque Mínimo", "🔄 Ajuste de Estoque",
            "📦 Movimentações"
        });
        
        // Financeiro - Gestão financeira essencial
        subMenusCache.put("💰 Financeiro", new String[]{
            "💰 Resumo Financeiro", "💰 Gestão de Caixa", "💸 Despesas", "📊 Fluxo de Caixa",
            "💰 Contas a Receber", "💸 Contas a Pagar"
        });
        
        // Pagamentos API - Integração com gateways de pagamento
        subMenusCache.put("💳 Pagamentos API", new String[]{
            "⚙️ Configuração de APIs", "💳 Transações", "🔗 Webhooks", 
            "📊 Relatórios de Pagamentos", "🧪 Testar Conexões", "📋 Histórico"
        });
        
        // Relatórios - Análises essenciais
        subMenusCache.put("📈 Relatórios", new String[]{
            "📊 Relatórios e Análises", "💰 Relatório de Vendas", " Relatório Financeiro"
        });
        
        // Export/Import - Gestão simplificada
        subMenusCache.put("� Export/Import", new String[]{
            "📤 Exportar Dados", "📥 Importar Dados", "📜 Histórico de Operações",
            "⚙️ Configurações de Export/Import"
        });
        
        // Busca Avançada - Sistema de busca simplificado
        subMenusCache.put("🔍 Busca Avançada", new String[]{
            "🔍 Busca Avançada", "⭐ Favoritos", "📜 Histórico de Buscas", 
            "⚙️ Configurações de Busca"
        });
        
        // Configurações - Sistema essencial
        subMenusCache.put("⚙️ Configurações", new String[]{
            "⚙️ Configurações Principais", "🏪 Loja", "💳 Pagamentos", 
            "👥 Usuários", "🔐 Permissões", "⚙️ Cache do Sistema", 
            "🔧 Parâmetros", "💰 Fechar Caixa"
        });
        
        // Notificações - Sistema simplificado
        subMenusCache.put("🔔 Notificações", new String[]{
            "🔔 Central de Notificações", "📬 Não Lidas", "📋 Todas", 
            "⚙️ Configurar Alertas"
        });
        
        SystemLogger.ui("Cache de submenus inicializado com " + subMenusCache.size() + " menus");
    }
    
    /**
     * Obtém os menus principais disponíveis
     */
    public String[] getMenusPrincipais() {
        return MENUS_PRINCIPAIS.clone();
    }
    
    /**
     * Obtém os submenus para um menu principal
     */
    public String[] getSubMenus(String menuText) {
        String[] subMenus = subMenusCache.get(menuText);
        if (subMenus != null) {
            return subMenus.clone();
        }
        return new String[0];
    }
    
    /**
     * Identifica o módulo baseado no texto do menu
     * Atualizado com nova estrutura de menus
     */
    public String identificarModulo(String menuText) {
        if (menuText.contains("Dashboard")) return "DASHBOARD";
        if (menuText.contains("Vendas")) return "VENDAS";
        if (menuText.contains("Produtos")) return "PRODUTOS";
        if (menuText.contains("Clientes")) return "CLIENTES";
        if (menuText.contains("Estoque")) return "ESTOQUE";
        if (menuText.contains("Financeiro")) return "FINANCEIRO";
        if (menuText.contains("Pagamentos API")) return "PAGAMENTOS_API";
        if (menuText.contains("Relatórios")) return "RELATÓRIOS";
        if (menuText.contains("Busca Avançada")) return "BUSCA_AVANCADA";
        if (menuText.contains("Configurações")) return "CONFIGURAÇÕES";
        if (menuText.contains("Notificações")) return "NOTIFICACOES";
        return "OUTROS";
    }
    
    /**
     * Verifica se um menu principal existe
     */
    public boolean isMenuPrincipal(String menuText) {
        for (String menu : MENUS_PRINCIPAIS) {
            if (menu.equals(menuText)) {
                return true;
            }
        }
        return false;
    }
    
    /**
     * Verifica se um submenu existe para um menu principal
     */
    public boolean hasSubMenus(String menuText) {
        return subMenusCache.containsKey(menuText);
    }
    
    /**
     * Conta o número de submenus para um menu principal
     */
    public int contarSubMenus(String menuText) {
        String[] subMenus = subMenusCache.get(menuText);
        return subMenus != null ? subMenus.length : 0;
    }
    
    /**
     * Obtém todos os submenus de um módulo específico
     * Atualizado com nova estrutura de menus
     */
    public String[] getSubMenusPorModulo(String modulo) {
        switch (modulo) {
            case "DASHBOARD":
                return getSubMenus("🏠 Dashboard");
            case "VENDAS":
                return getSubMenus("📋 Vendas");
            case "PRODUTOS":
                return getSubMenus("📦 Produtos");
            case "CLIENTES":
                return getSubMenus("👥 Clientes");
            case "ESTOQUE":
                return getSubMenus("📊 Estoque");
            case "FINANCEIRO":
                return getSubMenus("💰 Financeiro");
            case "RELATÓRIOS":
                return getSubMenus("📈 Relatórios");
            case "CONFIGURAÇÕES":
                return getSubMenus("⚙️ Configurações");
            case "NOTIFICACOES":
                return getSubMenus("🔔 Notificações");
            default:
                return new String[0];
        }
    }
    
    // Getters e Setters para dados do usuário
    
    public String getUsuarioAtual() {
        return usuarioAtual;
    }
    
    public void setUsuarioAtual(String usuarioAtual) {
        this.usuarioAtual = usuarioAtual;
    }
    
    public String getNomeUsuario() {
        return nomeUsuario;
    }
    
    public void setNomeUsuario(String nomeUsuario) {
        this.nomeUsuario = nomeUsuario;
    }
    
    public String getTelaAtual() {
        return telaAtual;
    }
    
    public void setTelaAtual(String telaAtual) {
        this.telaAtual = telaAtual;
    }
    
    public String getModuloAtual() {
        return moduloAtual;
    }
    
    public void setModuloAtual(String moduloAtual) {
        this.moduloAtual = moduloAtual;
    }
    
    public String getTipoTelaAtual() {
        return tipoTelaAtual;
    }
    
    public void setTipoTelaAtual(String tipoTelaAtual) {
        this.tipoTelaAtual = tipoTelaAtual;
    }
    
    public long getTimestampTelaAtual() {
        return timestampTelaAtual;
    }
    
    public void setTimestampTelaAtual(long timestampTelaAtual) {
        this.timestampTelaAtual = timestampTelaAtual;
    }
    
    /**
     * Atualiza as informações da tela atual
     */
    public void atualizarTelaAtual(String itemText, String module) {
        this.telaAtual = itemText;
        this.moduloAtual = module;
        this.tipoTelaAtual = "FORMULÁRIO";
        this.timestampTelaAtual = System.currentTimeMillis();
        
        SystemLogger.ui("Tela atual atualizada: " + itemText + " [" + module + "]");
    }
    
    /**
     * Obtém estatísticas dos menus
     */
    public Map<String, Object> getEstatisticasMenus() {
        Map<String, Object> stats = new HashMap<>();
        stats.put("totalMenusPrincipais", MENUS_PRINCIPAIS.length);
        stats.put("totalSubMenus", subMenusCache.size());
        
        int totalSubMenus = 0;
        for (String[] subMenus : subMenusCache.values()) {
            totalSubMenus += subMenus.length;
        }
        stats.put("totalItensSubMenus", totalSubMenus);
        stats.put("mediaSubMenusPorMenu", subMenusCache.isEmpty() ? 0 : (double) totalSubMenus / subMenusCache.size());
        
        return stats;
    }
    
    /**
     * Limpa o cache de submenus
     */
    public void limparCache() {
        subMenusCache.clear();
        SystemLogger.ui("Cache de submenus limpo");
    }
    
    /**
     * Recarrega o cache de submenus
     */
    public void recarregarCache() {
        limparCache();
        inicializarCacheSubMenus();
        SystemLogger.ui("Cache de submenus recarregado");
    }
}
