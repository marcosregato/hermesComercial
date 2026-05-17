package com.br.hermescomercial.pattern.observer;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Gerenciador de Eventos - Implementação do Observer Pattern
 * @author Hermes Comercial
 * @version 2.8.0
 */
public class EventManager {
    
    private static EventManager instance;
    private final Map<String, List<EventListener>> listeners;
    private final Map<String, Object> lastEvents;
    
    public enum EventType {
        VENDA_REALIZADA("venda.realizada"),
        ESTOQUE_BAIXO("estoque.baixo"),
        ESTOQUE_CRITICO("estoque.critico"),
        CAIXA_ABERTO("caixa.aberto"),
        CAIXA_FECHADO("caixa.fechado"),
        PROMOCAO_ATIVA("promocao.ativa"),
        PROMOCAO_FINALIZADA("promocao.finalizada"),
        NOTIFICACAO("notificacao.geral"),
        ERRO_SISTEMA("erro.sistema"),
        USUARIO_LOGIN("usuario.login"),
        USUARIO_LOGOUT("usuario.logout"),
        CONFIGURACAO_ALTERADA("configuracao.alterada"),
        BACKUP_CONCLUIDO("backup.concluido"),
        SINCRONIZACAO_OFFLINE("sincronizacao.offline");
        
        private final String eventName;
        
        EventType(String eventName) {
            this.eventName = eventName;
        }
        
        public String getEventName() {
            return eventName;
        }
    }
    
    public interface EventListener {
        void onEvent(EventType eventType, Object data);
        String getListenerId();
    }
    
    public static class Event {
        private EventType type;
        private Object data;
        private long timestamp;
        private String source;
        
        public Event(EventType type, Object data, String source) {
            this.type = type;
            this.data = data;
            this.source = source;
            this.timestamp = System.currentTimeMillis();
        }
        
        // Getters
        public EventType getType() { return type; }
        public Object getData() { return data; }
        public long getTimestamp() { return timestamp; }
        public String getSource() { return source; }
    }
    
    private EventManager() {
        this.listeners = new ConcurrentHashMap<>();
        this.lastEvents = new ConcurrentHashMap<>();
        
        // Inicializar listas para todos os tipos de eventos
        for (EventType type : EventType.values()) {
            listeners.put(type.getEventName(), new CopyOnWriteArrayList<>());
        }
    }
    
    public static synchronized EventManager getInstance() {
        if (instance == null) {
            instance = new EventManager();
        }
        return instance;
    }
    
    /**
     * Adiciona listener para todos os eventos
     */
    public void addGlobalListener(EventListener listener) {
        for (EventType type : EventType.values()) {
            addListener(type, listener);
        }
    }
    
    /**
     * Adiciona listener para tipo específico de evento
     */
    public void addListener(EventType eventType, EventListener listener) {
        List<EventListener> eventListeners = listeners.get(eventType.getEventName());
        if (eventListeners != null) {
            eventListeners.add(listener);
        }
    }
    
    /**
     * Remove listener específico
     */
    public void removeListener(EventListener listener) {
        for (List<EventListener> eventListeners : listeners.values()) {
            eventListeners.removeIf(l -> l.getListenerId().equals(listener.getListenerId()));
        }
    }
    
    /**
     * Remove listener de tipo específico
     */
    public void removeListener(EventType eventType, EventListener listener) {
        List<EventListener> eventListeners = listeners.get(eventType.getEventName());
        if (eventListeners != null) {
            eventListeners.removeIf(l -> l.getListenerId().equals(listener.getListenerId()));
        }
    }
    
    /**
     * Publica evento
     */
    public void publishEvent(EventType eventType, Object data, String source) {
        Event event = new Event(eventType, data, source);
        
        // Armazenar último evento
        lastEvents.put(eventType.getEventName(), event);
        
        // Notificar listeners
        List<EventListener> eventListeners = listeners.get(eventType.getEventName());
        if (eventListeners != null) {
            for (EventListener listener : eventListeners) {
                try {
                    listener.onEvent(eventType, data);
                } catch (Exception e) {
                    System.err.println("Erro ao notificar listener " + listener.getListenerId() + ": " + e.getMessage());
                }
            }
        }
    }
    
    /**
     * Publica evento sem fonte específica
     */
    public void publishEvent(EventType eventType, Object data) {
        publishEvent(eventType, data, "System");
    }
    
    /**
     * Obtém último evento de um tipo
     */
    public Event getLastEvent(EventType eventType) {
        return (Event) lastEvents.get(eventType.getEventName());
    }
    
    /**
     * Obtém todos os últimos eventos
     */
    public Map<String, Event> getAllLastEvents() {
        Map<String, Event> result = new HashMap<>();
        for (Map.Entry<String, Object> entry : lastEvents.entrySet()) {
            result.put(entry.getKey(), (Event) entry.getValue());
        }
        return result;
    }
    
    /**
     * Obtém estatísticas dos eventos
     */
    public Map<String, Object> getEventStatistics() {
        Map<String, Object> stats = new HashMap<>();
        
        // Contagem de listeners por tipo
        Map<String, Integer> listenersByType = new HashMap<>();
        for (Map.Entry<String, List<EventListener>> entry : listeners.entrySet()) {
            listenersByType.put(entry.getKey(), entry.getValue().size());
        }
        stats.put("listenersByType", listenersByType);
        
        // Total de listeners
        int totalListeners = listeners.values().stream()
            .mapToInt(List::size)
            .sum();
        stats.put("totalListeners", totalListeners);
        
        // Tipos de eventos disponíveis
        stats.put("availableEventTypes", EventType.values().length);
        
        return stats;
    }
    
    /**
     * Limpa todos os listeners
     */
    public void clearAllListeners() {
        for (List<EventListener> eventListeners : listeners.values()) {
            eventListeners.clear();
        }
    }
    
    /**
     * Verifica se há listeners para um tipo de evento
     */
    public boolean hasListeners(EventType eventType) {
        List<EventListener> eventListeners = listeners.get(eventType.getEventName());
        return eventListeners != null && !eventListeners.isEmpty();
    }
    
    /**
     * Obtém quantidade de listeners por tipo
     */
    public int getListenerCount(EventType eventType) {
        List<EventListener> eventListeners = listeners.get(eventType.getEventName());
        return eventListeners != null ? eventListeners.size() : 0;
    }
}
