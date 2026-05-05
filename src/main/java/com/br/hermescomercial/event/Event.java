package com.br.hermescomercial.event;

import java.time.LocalDateTime;
import java.util.Map;

/**
 * Event class for the Observer Pattern
 * Versão 2.0.0 - Event System Implementation
 */
public class Event {
    
    private final String type;
    private final Object data;
    private final LocalDateTime timestamp;
    private final String source;
    private final Map<String, Object> metadata;
    
    public Event(String type, Object data) {
        this.type = type;
        this.data = data;
        this.timestamp = LocalDateTime.now();
        this.source = Thread.currentThread().getName();
        this.metadata = null;
    }
    
    public Event(String type, Object data, String source) {
        this.type = type;
        this.data = data;
        this.timestamp = LocalDateTime.now();
        this.source = source;
        this.metadata = null;
    }
    
    public Event(String type, Object data, String source, Map<String, Object> metadata) {
        this.type = type;
        this.data = data;
        this.timestamp = LocalDateTime.now();
        this.source = source;
        this.metadata = metadata;
    }
    
    public String getType() {
        return type;
    }
    
    public Object getData() {
        return data;
    }
    
    public LocalDateTime getTimestamp() {
        return timestamp;
    }
    
    public String getSource() {
        return source;
    }
    
    public Map<String, Object> getMetadata() {
        return metadata;
    }
    
    @Override
    public String toString() {
        return "Event{" +
                "type='" + type + '\'' +
                ", timestamp=" + timestamp +
                ", source='" + source + '\'' +
                '}';
    }
}
