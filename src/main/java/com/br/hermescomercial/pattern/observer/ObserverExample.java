package com.br.hermescomercial.pattern.observer;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

/**
 * Exemplo de uso do Observer Pattern no Hermes Comercial PDV
 * @author Hermes Comercial
 * @version 2.8.0
 */
public class ObserverExample {
    
    public static void main(String[] args) {
        // Inicializar o EventManager
        EventManager eventManager = EventManager.getInstance();
        
        // Criar e registrar listeners
        EventListeners.NotificacaoListener notificacaoListener = new EventListeners.NotificacaoListener();
        EventListeners.AuditoriaListener auditoriaListener = new EventListeners.AuditoriaListener();
        EventListeners.CacheListener cacheListener = new EventListeners.CacheListener();
        EventListeners.DashboardListener dashboardListener = new EventListeners.DashboardListener();
        EventListeners.OfflineListener offlineListener = new EventListeners.OfflineListener();
        EventListeners.BackupListener backupListener = new EventListeners.BackupListener();
        
        // Registrar listeners para eventos específicos
        eventManager.addListener(EventManager.EventType.VENDA_REALIZADA, notificacaoListener);
        eventManager.addListener(EventManager.EventType.VENDA_REALIZADA, auditoriaListener);
        eventManager.addListener(EventManager.EventType.VENDA_REALIZADA, cacheListener);
        eventManager.addListener(EventManager.EventType.VENDA_REALIZADA, dashboardListener);
        eventManager.addListener(EventManager.EventType.VENDA_REALIZADA, offlineListener);
        
        eventManager.addListener(EventManager.EventType.ESTOQUE_BAIXO, notificacaoListener);
        eventManager.addListener(EventManager.EventType.ESTOQUE_BAIXO, cacheListener);
        eventManager.addListener(EventManager.EventType.ESTOQUE_BAIXO, dashboardListener);
        
        eventManager.addListener(EventManager.EventType.ESTOQUE_CRITICO, notificacaoListener);
        eventManager.addListener(EventManager.EventType.ESTOQUE_CRITICO, cacheListener);
        eventManager.addListener(EventManager.EventType.ESTOQUE_CRITICO, dashboardListener);
        
        eventManager.addListener(EventManager.EventType.CAIXA_FECHADO, auditoriaListener);
        eventManager.addListener(EventManager.EventType.CAIXA_FECHADO, dashboardListener);
        eventManager.addListener(EventManager.EventType.CAIXA_FECHADO, backupListener);
        
        eventManager.addListener(EventManager.EventType.ERRO_SISTEMA, notificacaoListener);
        eventManager.addListener(EventManager.EventType.ERRO_SISTEMA, auditoriaListener);
        
        // Simular eventos do sistema
        System.out.println("=== SIMULANDO EVENTOS DO SISTEMA ===\n");
        
        // Simular venda realizada
        simularVendaRealizada(eventManager);
        
        // Simular alerta de estoque baixo
        simularEstoqueBaixo(eventManager);
        
        // Simular alerta de estoque crítico
        simularEstoqueCritico(eventManager);
        
        // Simular fechamento de caixa
        simularFechamentoCaixa(eventManager);
        
        // Simular erro do sistema
        simularErroSistema(eventManager);
        
        // Exibir estatísticas
        System.out.println("\n=== ESTATÍSTICAS DOS EVENTOS ===");
        Map<String, Object> stats = eventManager.getEventStatistics();
        stats.forEach((key, value) -> System.out.println(key + ": " + value));
        
        // Exibir último evento de cada tipo
        System.out.println("\n=== ÚLTIMOS EVENTOS REGISTRADOS ===");
        for (EventManager.EventType type : EventManager.EventType.values()) {
            EventManager.Event lastEvent = eventManager.getLastEvent(type);
            if (lastEvent != null) {
                System.out.println(type.getEventName() + ": " + lastEvent.getData() + 
                                 " (Timestamp: " + lastEvent.getTimestamp() + ")");
            }
        }
    }
    
    private static void simularVendaRealizada(EventManager eventManager) {
        System.out.println("🔔 Evento: VENDA_REALIZADA");
        
        Map<String, Object> dadosVenda = new HashMap<>();
        dadosVenda.put("id", "V2024001");
        dadosVenda.put("valor", new BigDecimal("150.75"));
        dadosVenda.put("cliente", "João Silva");
        dadosVenda.put("itens", 5);
        dadosVenda.put("operador", "Maria");
        
        eventManager.publishEvent(EventManager.EventType.VENDA_REALIZADA, dadosVenda, "PDV");
        System.out.println();
    }
    
    private static void simularEstoqueBaixo(EventManager eventManager) {
        System.out.println("⚠️ Evento: ESTOQUE_BAIXO");
        
        Map<String, Object> dadosEstoque = new HashMap<>();
        dadosEstoque.put("codigo", "PROD001");
        dadosEstoque.put("nome", "Notebook Samsung");
        dadosEstoque.put("atual", 8);
        dadosEstoque.put("minimo", 10);
        
        eventManager.publishEvent(EventManager.EventType.ESTOQUE_BAIXO, dadosEstoque, "EstoqueService");
        System.out.println();
    }
    
    private static void simularEstoqueCritico(EventManager eventManager) {
        System.out.println("🔴 Evento: ESTOQUE_CRITICO");
        
        Map<String, Object> dadosEstoque = new HashMap<>();
        dadosEstoque.put("codigo", "PROD002");
        dadosEstoque.put("nome", "Mouse Wireless");
        dadosEstoque.put("atual", 2);
        
        eventManager.publishEvent(EventManager.EventType.ESTOQUE_CRITICO, dadosEstoque, "EstoqueService");
        System.out.println();
    }
    
    private static void simularFechamentoCaixa(EventManager eventManager) {
        System.out.println("🔒 Evento: CAIXA_FECHADO");
        
        Map<String, Object> dadosCaixa = new HashMap<>();
        dadosCaixa.put("operador", "Maria");
        dadosCaixa.put("data", "2024-01-15");
        dadosCaixa.put("total_vendas", new BigDecimal("2850.50"));
        dadosCaixa.put("total_dinheiro", new BigDecimal("2000.00"));
        dadosCaixa.put("total_cartao", new BigDecimal("850.50"));
        
        eventManager.publishEvent(EventManager.EventType.CAIXA_FECHADO, dadosCaixa, "CaixaService");
        System.out.println();
    }
    
    private static void simularErroSistema(EventManager eventManager) {
        System.out.println("❌ Evento: ERRO_SISTEMA");
        
        Map<String, Object> dadosErro = new HashMap<>();
        dadosErro.put("codigo", "DB_CONNECTION_ERROR");
        dadosErro.put("mensagem", "Falha na conexão com o banco de dados");
        dadosErro.put("modulo", "VendasService");
        dadosErro.put("gravidade", "ALTA");
        
        eventManager.publishEvent(EventManager.EventType.ERRO_SISTEMA, dadosErro, "SystemMonitor");
        System.out.println();
    }
}
