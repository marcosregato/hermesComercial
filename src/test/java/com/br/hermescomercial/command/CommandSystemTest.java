package com.br.hermescomercial.command;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.AfterEach;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Map;
import java.util.HashMap;

/**
 * Testes para CommandSystem
 * Verifica funcionamento do padrão Command com undo/redo
 */
public class CommandSystemTest {
    
    private CommandSystem commandSystem;
    
    @BeforeEach
    void setUp() {
        commandSystem = CommandSystem.getInstance();
    }
    
    @AfterEach
    void tearDown() {
        // Limpar comandos registrados
        String[] commands = commandSystem.getAvailableCommands();
        for (String cmd : commands) {
            commandSystem.unregisterCommand(cmd);
        }
        // Limpar pilhas de undo/redo
        commandSystem.clearStacks();
    }
    
    @Test
    @DisplayName("Deve obter instância singleton do CommandSystem")
    void testGetInstance() {
        CommandSystem instance1 = CommandSystem.getInstance();
        CommandSystem instance2 = CommandSystem.getInstance();
        
        assertSame(instance1, instance2, "Deve retornar a mesma instância");
    }
    
    @Test
    @DisplayName("Deve registrar comando")
    void testRegisterCommand() {
        assertDoesNotThrow(() -> {
            commandSystem.registerCommand("test", params -> new TestCommand());
        }, "Deve registrar comando sem exceções");
        
        String[] commands = commandSystem.getAvailableCommands();
        assertTrue(commands.length > 0, "Comando deve estar registrado");
    }
    
    @Test
    @DisplayName("Deve remover comando registrado")
    void testUnregisterCommand() {
        commandSystem.registerCommand("test", params -> new TestCommand());
        String[] commandsBefore = commandSystem.getAvailableCommands();
        assertTrue(commandsBefore.length > 0, "Comando deve estar registrado");
        
        boolean removed = commandSystem.unregisterCommand("test");
        
        assertTrue(removed, "Comando deve ser removido");
        String[] commandsAfter = commandSystem.getAvailableCommands();
        assertEquals(commandsBefore.length - 1, commandsAfter.length, "Comando não deve estar registrado após remoção");
    }
    
    @Test
    @DisplayName("Deve executar comando registrado")
    void testExecuteCommand() {
        commandSystem.registerCommand("test", params -> new TestCommand());
        
        boolean result = commandSystem.executeCommand("test", new HashMap<>());
        
        assertTrue(result, "Comando deve ser executado com sucesso");
    }
    
    @Test
    @DisplayName("Deve falhar ao executar comando não registrado")
    void testExecuteUnregisteredCommand() {
        boolean result = commandSystem.executeCommand("inexistente", new HashMap<>());
        
        assertFalse(result, "Deve falhar ao executar comando não registrado");
    }
    
    @Test
    @DisplayName("Deve desfazer comando executado")
    void testUndoCommand() {
        commandSystem.registerCommand("test", params -> new TestCommand());
        commandSystem.executeCommand("test", new HashMap<>());
        
        boolean result = commandSystem.undo();
        
        assertTrue(result, "Deve desfazer comando com sucesso");
    }
    
    @Test
    @DisplayName("Deve falhar ao desfazer sem comandos na pilha")
    void testUndoWithoutCommands() {
        boolean result = commandSystem.undo();
        
        assertFalse(result, "Deve falhar ao desfazer sem comandos");
    }
    
    @Test
    @DisplayName("Deve refazer comando desfeito")
    void testRedoCommand() {
        commandSystem.registerCommand("test", params -> new TestCommand());
        commandSystem.executeCommand("test", new HashMap<>());
        commandSystem.undo();
        
        boolean result = commandSystem.redo();
        
        assertTrue(result, "Deve refazer comando com sucesso");
    }
    
    @Test
    @DisplayName("Deve falhar ao refazer sem comandos na pilha")
    void testRedoWithoutCommands() {
        boolean result = commandSystem.redo();
        
        assertFalse(result, "Deve falhar ao refazer sem comandos");
    }
    
    @Test
    @DisplayName("Deve listar comandos disponíveis")
    void testGetAvailableCommands() {
        commandSystem.registerCommand("cmd1", params -> new TestCommand());
        commandSystem.registerCommand("cmd2", params -> new TestCommand());
        
        String[] commands = commandSystem.getAvailableCommands();
        
        assertEquals(2, commands.length, "Deve ter 2 comandos registrados");
    }
    
    @Test
    @DisplayName("Deve limpar pilha de redo após novo comando")
    void testClearRedoStackOnNewCommand() {
        commandSystem.registerCommand("test", params -> new TestCommand());
        commandSystem.executeCommand("test", new HashMap<>());
        commandSystem.undo();
        
        // Executar novo comando deve limpar pilha de redo
        commandSystem.executeCommand("test", new HashMap<>());
        
        boolean redoResult = commandSystem.redo();
        assertFalse(redoResult, "Pilha de redo deve estar vazia após novo comando");
    }
    
    @Test
    @DisplayName("Deve passar parâmetros para o comando")
    void testExecuteCommandWithParameters() {
        commandSystem.registerCommand("param", params -> new ParameterCommand(params));
        
        Map<String, Object> params = new HashMap<>();
        params.put("valor", "teste");
        
        boolean result = commandSystem.executeCommand("param", params);
        
        assertTrue(result, "Comando deve ser executado com parâmetros");
    }
    
    @Test
    @DisplayName("Deve remover comando não registrado retornar false")
    void testUnregisterNonExistentCommand() {
        boolean result = commandSystem.unregisterCommand("inexistente");
        
        assertFalse(result, "Deve retornar false para comando não existente");
    }
    
    // Classes de teste
    static class TestCommand implements CommandSystem.Command {
        private final String executionId = java.util.UUID.randomUUID().toString();
        private final java.time.LocalDateTime timestamp = java.time.LocalDateTime.now();
        private final String name = "TestCommand";
        private final String description = "Comando de teste";
        private boolean executed = false;
        private boolean undone = false;
        
        @Override
        public String getName() {
            return name;
        }
        
        @Override
        public boolean execute() {
            executed = true;
            return true;
        }
        
        @Override
        public boolean undo() {
            undone = true;
            return true;
        }
        
        @Override
        public String getDescription() {
            return description;
        }
        
        @Override
        public java.time.LocalDateTime getTimestamp() {
            return timestamp;
        }
        
        @Override
        public String getExecutionId() {
            return executionId;
        }
        
        public boolean wasExecuted() {
            return executed;
        }
        
        public boolean wasUndone() {
            return undone;
        }
    }
    
    static class ParameterCommand implements CommandSystem.Command {
        private final Map<String, Object> parameters;
        private final String executionId = java.util.UUID.randomUUID().toString();
        private final java.time.LocalDateTime timestamp = java.time.LocalDateTime.now();
        private final String name = "ParameterCommand";
        private final String description = "Comando com parâmetros";
        
        public ParameterCommand(Map<String, Object> parameters) {
            this.parameters = parameters;
        }
        
        @Override
        public String getName() {
            return name;
        }
        
        @Override
        public boolean execute() {
            return parameters != null && parameters.containsKey("valor");
        }
        
        @Override
        public boolean undo() {
            return true;
        }
        
        @Override
        public String getDescription() {
            return description;
        }
        
        @Override
        public java.time.LocalDateTime getTimestamp() {
            return timestamp;
        }
        
        @Override
        public String getExecutionId() {
            return executionId;
        }
    }
}
