package com.br.hermescomercial.command;

import com.br.hermescomercial.event.EventSystem;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Stack;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Command Pattern para sistema de comandos do usuário
 * Permite execução, undo/redo e auditoria de ações
 * Versão 1.0.0 - Command Pattern Implementation
 */
public class CommandSystem {
    
    private static final Logger logger = LogManager.getLogger(CommandSystem.class);
    private static volatile CommandSystem instance;
    private static final Object lock = new Object();
    
    // Pilhas para undo/redo
    private final Stack<Command> undoStack = new Stack<>();
    private final Stack<Command> redoStack = new Stack<>();
    
    // Map de comandos registrados
    private final Map<String, CommandFactory> commandFactories = new HashMap<>();
    
    // Sistema de eventos
    private final EventSystem eventSystem;
    
    // Construtor privado para Singleton
    private CommandSystem() {
        this.eventSystem = EventSystem.getInstance();
        registerDefaultCommands();
    }
    
    /**
     * Obtém instância única do CommandSystem
     * @return Instância única
     */
    public static CommandSystem getInstance() {
        if (instance == null) {
            synchronized (lock) {
                if (instance == null) {
                    instance = new CommandSystem();
                }
            }
        }
        return instance;
    }
    
    /**
     * Executa um comando
     * @param commandName Nome do comando
     * @param parameters Parâmetros do comando
     * @return true se executado com sucesso
     */
    public boolean executeCommand(String commandName, Map<String, Object> parameters) {
        CommandFactory factory = commandFactories.get(commandName);
        if (factory == null) {
            logger.error("Comando não encontrado: {}", commandName);
            return false;
        }
        
        try {
            Command command = factory.createCommand(parameters);
            
            System.out.println("⚡ CommandSystem: Executando comando: " + command.getName());
            
            boolean resultado = command.execute();
            
            if (resultado) {
                // Adicionar à pilha de undo
                undoStack.push(command);
                
                // Limpar pilha de redo (comportamento padrão)
                redoStack.clear();
                
                // Publicar evento de comando executado
                eventSystem.publish(new EventSystem.Event(
                    "command.executado", 
                    command, 
                    "CommandSystem"
                ));
            }
            
            return resultado;
            
        } catch (Exception e) {
            logger.error("Erro ao executar comando {}: {}", commandName, e.getMessage(), e);
            return false;
        }
    }
    
    /**
     * Desfaz o último comando executado
     * @return true se desfeito com sucesso
     */
    public boolean undo() {
        if (undoStack.isEmpty()) {
            System.out.println("⚠️ CommandSystem: Nenhum comando para desfazer");
            return false;
        }
        
        try {
            Command command = undoStack.pop();
            
            System.out.println("⏪ CommandSystem: Desfazendo comando: " + command.getName());
            
            boolean resultado = command.undo();
            
            if (resultado) {
                // Adicionar à pilha de redo
                redoStack.push(command);
                
                // Publicar evento de comando desfeito
                eventSystem.publish(new EventSystem.Event(
                    "command.desfeito", 
                    command, 
                    "CommandSystem"
                ));
            }
            
            return resultado;
            
        } catch (Exception e) {
            logger.error("Erro ao desfazer comando: {}", e.getMessage(), e);
            return false;
        }
    }
    
    /**
     * Refaz o último comando desfeito
     * @return true se refeito com sucesso
     */
    public boolean redo() {
        if (redoStack.isEmpty()) {
            System.out.println("⚠️ CommandSystem: Nenhum comando para refazer");
            return false;
        }
        
        try {
            Command command = redoStack.pop();
            
            System.out.println("⏩ CommandSystem: Refazendo comando: " + command.getName());
            
            boolean resultado = command.execute();
            
            if (resultado) {
                // Adicionar novamente à pilha de undo
                undoStack.push(command);
                
                // Publicar evento de comando refeito
                eventSystem.publish(new EventSystem.Event(
                    "command.refeito", 
                    command, 
                    "CommandSystem"
                ));
            }
            
            return resultado;
            
        } catch (Exception e) {
            logger.error("Erro ao refazer comando: {}", e.getMessage(), e);
            return false;
        }
    }
    
    /**
     * Registra um novo comando
     * @param name Nome do comando
     * @param factory Fábrica do comando
     */
    public void registerCommand(String name, CommandFactory factory) {
        commandFactories.put(name, factory);
        System.out.println("📝 CommandSystem: Comando registrado: " + name);
    }
    
    /**
     * Remove um comando registrado
     * @param name Nome do comando
     * @return true se removido
     */
    public boolean unregisterCommand(String name) {
        CommandFactory removed = commandFactories.remove(name);
        if (removed != null) {
            System.out.println("🗑️ CommandSystem: Comando removido: " + name);
            return true;
        }
        return false;
    }
    
    /**
     * Lista todos os comandos disponíveis
     * @return Array com nomes dos comandos
     */
    public String[] getAvailableCommands() {
        return commandFactories.keySet().toArray(new String[0]);
    }
    
    /**
     * Registra comandos padrão do sistema
     */
    private void registerDefaultCommands() {
        System.out.println("📝 CommandSystem: Sistema de comandos inicializado");
    }
    
    /**
     * Limpa as pilhas de undo e redo (para testes)
     */
    public void clearStacks() {
        undoStack.clear();
        redoStack.clear();
    }
    
    /**
     * Interface para comandos
     */
    public interface Command {
        String getName();
        boolean execute();
        boolean undo();
        String getDescription();
        LocalDateTime getTimestamp();
        String getExecutionId();
    }
    
    /**
     * Interface para fábrica de comandos
     */
    @FunctionalInterface
    public interface CommandFactory {
        Command createCommand(Map<String, Object> parameters);
    }
    
    /**
     * Classe abstrata base para comandos
     */
    public abstract static class AbstractCommand implements Command {
        protected final String executionId;
        protected final LocalDateTime timestamp;
        protected final String name;
        protected final String description;
        
        protected AbstractCommand(String name, String description) {
            this.executionId = UUID.randomUUID().toString();
            this.timestamp = LocalDateTime.now();
            this.name = name;
            this.description = description;
        }
        
        @Override
        public String getName() {
            return name;
        }
        
        @Override
        public LocalDateTime getTimestamp() {
            return timestamp;
        }
        
        @Override
        public String getDescription() {
            return description;
        }
        
        @Override
        public String getExecutionId() {
            return executionId;
        }
        
        @Override
        public String toString() {
            return String.format("Command{id='%s', name='%s', timestamp=%s}", 
                               executionId, name, timestamp.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
        }
    }
}
