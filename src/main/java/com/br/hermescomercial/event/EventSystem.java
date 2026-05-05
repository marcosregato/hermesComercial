package com.br.hermescomercial.event;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Observer Pattern para sistema de eventos
 * Permite comunicação desacoplada entre componentes
 * Versão 1.0.0 - Design Pattern Implementation
 */
public class EventSystem {
    
    private static volatile EventSystem instance;
    private static final Object lock = new Object();
    
    // Map de eventos e seus listeners
    private final Map<String, List<EventListener>> listeners = new ConcurrentHashMap<>();
    
    // Construtor privado para Singleton
    private EventSystem() {}
    
    /**
     * Obtém instância única do EventSystem
     * @return Instância única
     */
    public static EventSystem getInstance() {
        if (instance == null) {
            synchronized (lock) {
                if (instance == null) {
                    instance = new EventSystem();
                }
            }
        }
        return instance;
    }
    
    /**
     * Registra um listener para um tipo de evento
     * @param eventType Tipo do evento
     * @param listener Listener para ser notificado
     */
    public void subscribe(String eventType, EventListener listener) {
        listeners.computeIfAbsent(eventType, k -> new ArrayList<>()).add(listener);
        System.out.println("📡 EventSystem: Listener registrado para evento: " + eventType);
    }
    
    /**
     * Remove um listener de um tipo de evento
     * @param eventType Tipo do evento
     * @param listener Listener a ser removido
     */
    public void unsubscribe(String eventType, EventListener listener) {
        List<EventListener> eventListeners = listeners.get(eventType);
        if (eventListeners != null) {
            eventListeners.remove(listener);
            if (eventListeners.isEmpty()) {
                listeners.remove(eventType);
            }
            System.out.println("📡 EventSystem: Listener removido do evento: " + eventType);
        }
    }
    
    /**
     * Publica um evento para todos os listeners registrados
     * @param event Evento a ser publicado
     */
    public void publish(Event event) {
        List<EventListener> eventListeners = listeners.get(event.getType());
        if (eventListeners != null) {
            System.out.println("📡 EventSystem: Publicando evento: " + event.getType() + 
                             " para " + eventListeners.size() + " listeners");
            
            for (EventListener listener : new ArrayList<>(eventListeners)) {
                try {
                    listener.onEvent(event);
                } catch (Exception e) {
                    System.err.println("❌ EventSystem: Erro ao processar evento " + 
                                     event.getType() + " no listener: " + e.getMessage());
                }
            }
        } else {
            System.out.println("📡 EventSystem: Nenhum listener para evento: " + event.getType());
        }
    }
    
    /**
     * Publica um evento de forma assíncrona
     * @param event Evento a ser publicado
     */
    public void publishAsync(Event event) {
        new Thread(() -> publish(event)).start();
    }
    
    /**
     * Limpa todos os listeners de um tipo de evento
     * @param eventType Tipo do evento
     */
    public void clearListeners(String eventType) {
        listeners.remove(eventType);
        System.out.println("📡 EventSystem: Listeners removidos do evento: " + eventType);
    }
    
    /**
     * Limpa todos os listeners
     */
    public void clearAllListeners() {
        listeners.clear();
        System.out.println("📡 EventSystem: Todos os listeners removidos");
    }
    
    /**
     * Obtém quantidade de listeners para um tipo de evento
     * @param eventType Tipo do evento
     * @return Quantidade de listeners
     */
    public int getListenerCount(String eventType) {
        List<EventListener> eventListeners = listeners.get(eventType);
        return eventListeners != null ? eventListeners.size() : 0;
    }
    
    /**
     * Lista todos os tipos de eventos registrados
     * @return Array com tipos de eventos
     */
    public String[] getRegisteredEventTypes() {
        return listeners.keySet().toArray(new String[0]);
    }
    
    /**
     * Interface para listeners de eventos
     */
    public interface EventListener {
        void onEvent(Event event);
    }
    
    /**
     * Classe base para eventos
     */
    public static class Event {
        private final String type;
        private final Object data;
        private final long timestamp;
        private final String source;
        
        public Event(String type, Object data, String source) {
            this.type = type;
            this.data = data;
            this.source = source;
            this.timestamp = System.currentTimeMillis();
        }
        
        public String getType() {
            return type;
        }
        
        public Object getData() {
            return data;
        }
        
        public long getTimestamp() {
            return timestamp;
        }
        
        public String getSource() {
            return source;
        }
        
        @Override
        public String toString() {
            return String.format("Event{type='%s', source='%s', timestamp=%d}", 
                               type, source, timestamp);
        }
    }
    
    /**
     * Tipos de eventos predefinidos para o sistema
     */
    public static class EventTypes {
        // Eventos de Venda
        public static final String VENDA_INICIADA = "venda.iniciada";
        public static final String VENDA_FINALIZADA = "venda.finalizada";
        public static final String VENDA_CANCELADA = "venda.cancelada";
        public static final String ITEM_ADICIONADO = "venda.item.adicionado";
        public static final String ITEM_REMOVIDO = "venda.item.removido";
        
        // Eventos de Estoque
        public static final String ESTOQUE_ATUALIZADO = "estoque.atualizado";
        public static final String ESTOQUE_BAIXO = "estoque.baixo";
        public static final String ESTOQUE_ZERADO = "estoque.zerado";
        public static final String ENTRADA_ESTOQUE = "estoque.entrada";
        public static final String SAIDA_ESTOQUE = "estoque.saida";
        
        // Eventos de Cliente
        public static final String CLIENTE_CADASTRADO = "cliente.cadastrado";
        public static final String CLIENTE_ATUALIZADO = "cliente.atualizado";
        public static final String CLIENTE_REMOVIDO = "cliente.removido";
        
        // Eventos de Produto
        public static final String PRODUTO_CADASTRADO = "produto.cadastrado";
        public static final String PRODUTO_ATUALIZADO = "produto.atualizado";
        public static final String PRODUTO_REMOVIDO = "produto.removido";
        
        // Eventos de Caixa
        public static final String CAIXA_ABERTO = "caixa.aberto";
        public static final String CAIXA_FECHADO = "caixa.fechado";
        public static final String CAIXA_SANGRIA = "caixa.sangria";
        public static final String CAIXA_SUPRIMENTO = "caixa.suprimento";
        
        // Eventos de Sistema
        public static final String USUARIO_LOGADO = "sistema.usuario.logado";
        public static final String USUARIO_DESLOGADO = "sistema.usuario.deslogado";
        public static final String SISTEMA_INICIADO = "sistema.iniciado";
        public static final String SISTEMA_FINALIZADO = "sistema.finalizado";
        public static final String BACKUP_REALIZADO = "sistema.backup.realizado";
        
        // Eventos de Notificação
        public static final String NOTIFICACAO_ENVIADA = "notificacao.enviada";
        public static final String NOTIFICACAO_LIDA = "notificacao.lida";
        
        // Eventos de Relatório
        public static final String RELATORIO_GERADO = "relatorio.gerado";
        public static final String RELATORIO_EXPORTADO = "relatorio.exportado";
        
        // Eventos de Configuração
        public static final String CONFIGURACAO_ALTERADA = "configuracao.alterada";
        public static final String CONFIGURACAO_CARREGADA = "configuracao.carregada";
    }
}
