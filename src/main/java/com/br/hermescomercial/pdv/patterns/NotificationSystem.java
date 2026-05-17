package com.br.hermescomercial.pdv.patterns;

import com.br.hermescomercial.util.SystemLogger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Observer Pattern - Sistema Central de Notificações
 * Permite que múltiplos observadores recebam notificações do sistema
 */
public class NotificationSystem {
    
    private static volatile NotificationSystem instance;
    private Map<String, List<NotificationObserver>> observers;
    private List<Notification> notificationHistory;
    
    /**
     * Interface Observer para notificações
     */
    public interface NotificationObserver {
        void update(Notification notification);
        String getObserverName();
    }
    
    /**
     * Classe que representa uma notificação
     */
    public static class Notification {
        private String id;
        private String type;
        private String title;
        private String message;
        private String timestamp;
        private String priority;
        private Map<String, Object> data;
        
        public Notification(String type, String title, String message, String priority) {
            this.id = java.util.UUID.randomUUID().toString();
            this.type = type;
            this.title = title;
            this.message = message;
            this.priority = priority;
            this.timestamp = java.time.LocalDateTime.now().toString();
            this.data = new HashMap<>();
        }
        
        // Getters e Setters
        public String getId() { return id; }
        public String getType() { return type; }
        public String getTitle() { return title; }
        public String getMessage() { return message; }
        public String getTimestamp() { return timestamp; }
        public String getPriority() { return priority; }
        public Map<String, Object> getData() { return data; }
        
        public void addData(String key, Object value) {
            data.put(key, value);
        }
        
        @Override
        public String toString() {
            return String.format("[%s] %s - %s", priority.toUpperCase(), title, message);
        }
    }
    
    /**
     * Construtor privado para Singleton
     */
    private NotificationSystem() {
        observers = new HashMap<>();
        notificationHistory = new ArrayList<>();
        SystemLogger.ui("NotificationSystem inicializado (Singleton)");
    }
    
    /**
     * Obtém a instância única (Double-Checked Locking)
     */
    public static NotificationSystem getInstance() {
        if (instance == null) {
            synchronized (NotificationSystem.class) {
                if (instance == null) {
                    instance = new NotificationSystem();
                }
            }
        }
        return instance;
    }
    
    /**
     * Adiciona um observador para um tipo específico de notificação
     */
    public void addObserver(String notificationType, NotificationObserver observer) {
        observers.computeIfAbsent(notificationType, k -> new ArrayList<>()).add(observer);
        SystemLogger.ui("Observador adicionado: " + observer.getObserverName() + " para tipo: " + notificationType);
    }
    
    /**
     * Remove um observador
     */
    public void removeObserver(String notificationType, NotificationObserver observer) {
        List<NotificationObserver> typeObservers = observers.get(notificationType);
        if (typeObservers != null) {
            typeObservers.remove(observer);
            SystemLogger.ui("Observador removido: " + observer.getObserverName() + " do tipo: " + notificationType);
        }
    }
    
    /**
     * Notifica todos os observadores de um tipo específico
     */
    public void notifyObservers(String type, String title, String message, String priority) {
        Notification notification = new Notification(type, title, message, priority);
        notifyObservers(notification);
    }
    
    /**
     * Notifica com dados adicionais
     */
    public void notifyObservers(String type, String title, String message, String priority, Map<String, Object> data) {
        Notification notification = new Notification(type, title, message, priority);
        notification.getData().putAll(data);
        notifyObservers(notification);
    }
    
    /**
     * Notifica todos os observadores
     */
    private void notifyObservers(Notification notification) {
        // Adiciona ao histórico
        notificationHistory.add(notification);
        
        // Notifica observadores do tipo específico
        List<NotificationObserver> typeObservers = observers.get(notification.getType());
        if (typeObservers != null) {
            for (NotificationObserver observer : typeObservers) {
                try {
                    observer.update(notification);
                } catch (Exception e) {
                    SystemLogger.error("Erro ao notificar observador: " + observer.getObserverName(), e);
                }
            }
        }
        
        // Notifica observadores gerais (tipo "*")
        List<NotificationObserver> generalObservers = observers.get("*");
        if (generalObservers != null) {
            for (NotificationObserver observer : generalObservers) {
                try {
                    observer.update(notification);
                } catch (Exception e) {
                    SystemLogger.error("Erro ao notificar observador geral: " + observer.getObserverName(), e);
                }
            }
        }
        
        SystemLogger.ui("Notificação enviada: " + notification.toString());
    }
    
    /**
     * Obtém o histórico de notificações
     */
    public List<Notification> getNotificationHistory() {
        return new ArrayList<>(notificationHistory);
    }
    
    /**
     * Obtém notificações por tipo
     */
    public List<Notification> getNotificationsByType(String type) {
        return notificationHistory.stream()
                .filter(n -> n.getType().equals(type))
                .collect(java.util.stream.Collectors.toList());
    }
    
    /**
     * Obtém notificações por prioridade
     */
    public List<Notification> getNotificationsByPriority(String priority) {
        return notificationHistory.stream()
                .filter(n -> n.getPriority().equals(priority))
                .collect(java.util.stream.Collectors.toList());
    }
    
    /**
     * Limpa o histórico de notificações
     */
    public void clearHistory() {
        notificationHistory.clear();
        SystemLogger.ui("Histórico de notificações limpo");
    }
    
    /**
     * Obtém estatísticas das notificações
     */
    public Map<String, Integer> getNotificationStats() {
        Map<String, Integer> stats = new HashMap<>();
        for (Notification notification : notificationHistory) {
            stats.merge(notification.getType(), 1, Integer::sum);
        }
        return stats;
    }
    
    /**
     * Verifica se há observadores para um tipo
     */
    public boolean hasObservers(String type) {
        List<NotificationObserver> typeObservers = observers.get(type);
        return typeObservers != null && !typeObservers.isEmpty();
    }
    
    /**
     * Obtém o número de observadores por tipo
     */
    public int getObserverCount(String type) {
        List<NotificationObserver> typeObservers = observers.get(type);
        return typeObservers != null ? typeObservers.size() : 0;
    }
    
    /**
     * Lista todos os observadores registrados
     */
    public void listAllObservers() {
        SystemLogger.ui("=== OBSERVADORES REGISTRADOS ===");
        observers.forEach((type, obsList) -> {
            SystemLogger.ui("Tipo: " + type + " (" + obsList.size() + " observadores)");
            for (NotificationObserver observer : obsList) {
                SystemLogger.ui("  - " + observer.getObserverName());
            }
        });
    }
}
