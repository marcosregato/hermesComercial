package com.br.hermescomercial.pdv.patterns;

import com.br.hermescomercial.util.SystemLogger;
import javax.swing.*;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

/**
 * Implementação do Command Pattern para o sistema PDV
 * Permite executar, desfazer e refazer ações no sistema
 */
public class CommandPattern {
    
    /**
     * Interface Command - define operações que podem ser executadas
     */
    public interface Command {
        void execute();
        void undo();
        boolean canUndo();
        String getDescription();
    }
    
    /**
     * Command Invoker - responsável por executar e gerenciar comandos
     */
    public static class CommandInvoker {
        private final List<Command> commandHistory = new ArrayList<>();
        private int currentIndex = -1;
        private final List<Consumer<Command>> historyListeners = new ArrayList<>();
        
        public void executeCommand(Command command) {
            try {
                command.execute();
                
                // Remove comandos após o atual se não for possível desfazer
                while (currentIndex < commandHistory.size() - 1) {
                    commandHistory.remove(currentIndex + 1);
                }
                
                commandHistory.add(command);
                currentIndex = commandHistory.size() - 1;
                
                SystemLogger.ui("Comando executado: " + command.getDescription());
                notifyHistoryListeners(command, "EXECUTE", commandHistory);
                
            } catch (Exception e) {
                SystemLogger.error("Erro ao executar comando", e);
            }
        }
        
        public void undo() {
            if (canUndo() && currentIndex >= 0) {
                try {
                    Command command = commandHistory.get(currentIndex);
                    command.undo();
                    currentIndex--;
                    
                    SystemLogger.ui("Comando desfeito: " + command.getDescription());
                    notifyHistoryListeners(command, "UNDO", commandHistory);
                    
                } catch (Exception e) {
                    SystemLogger.error("Erro ao desfazer comando", e);
                }
            }
        }
        
        public void redo() {
            if (canRedo() && currentIndex < commandHistory.size() - 1) {
                try {
                    currentIndex++;
                    Command command = commandHistory.get(currentIndex);
                    command.execute();
                    
                    SystemLogger.ui("Comando refeito: " + command.getDescription());
                    notifyHistoryListeners(command, "REDO", commandHistory);
                    
                } catch (Exception e) {
                    SystemLogger.error("Erro ao refazer comando", e);
                }
            }
        }
        
        public boolean canUndo() {
            return currentIndex >= 0;
        }
        
        public boolean canRedo() {
            return currentIndex < commandHistory.size() - 1;
        }
        
        public void clearHistory() {
            commandHistory.clear();
            currentIndex = -1;
            SystemLogger.ui("Histórico de comandos limpo");
            notifyHistoryListeners(null, "CLEAR", commandHistory);
        }
        
        public List<Command> getCommandHistory() {
            return new ArrayList<>(commandHistory);
        }
        
        public void addHistoryListener(Consumer<Command> listener) {
            historyListeners.add(listener);
        }
        
        private void notifyHistoryListeners(Command command, String action, List<Command> history) {
            for (Consumer<Command> listener : historyListeners) {
                try {
                    listener.accept(command);
                } catch (Exception e) {
                    SystemLogger.error("Erro ao notificar listener", e);
                }
            }
        }
    }
    
    /**
     * Comandos concretos para operações comuns
     */
    public static class SaveCommand implements Command {
        private final String entityName;
        private final Object entity;
        private final Runnable saveAction;
        private final Runnable undoAction;
        
        public SaveCommand(String entityName, Object entity, Runnable saveAction, Runnable undoAction) {
            this.entityName = entityName;
            this.entity = entity;
            this.saveAction = saveAction;
            this.undoAction = undoAction;
        }
        
        @Override
        public void execute() {
            saveAction.run();
            SystemLogger.ui("Salvando " + entityName + ": " + entity.toString());
        }
        
        @Override
        public void undo() {
            if (undoAction != null) {
                undoAction.run();
                SystemLogger.ui("Desfazendo salvamento de " + entityName);
            }
        }
        
        @Override
        public boolean canUndo() {
            return undoAction != null;
        }
        
        @Override
        public String getDescription() {
            return "Salvar " + entityName;
        }
    }
    
    /**
     * Utilitário para criar botões com suporte a Command Pattern
     */
    public static class CommandButton extends JButton {
        private final Command command;
        private final CommandInvoker invoker;
        
        public CommandButton(String text, Command command, CommandInvoker invoker) {
            super(text);
            this.command = command;
            this.invoker = invoker;
            addActionListener(e -> invoker.executeCommand(command));
        }
        
        public Command getCommand() {
            return command;
        }
        
        public boolean canUndo() {
            return command.canUndo();
        }
        
        public CommandInvoker getInvoker() {
            return invoker;
        }
    }
    
    /**
     * Painel de controle de comandos (Undo/Redo/Clear)
     */
    public static class CommandControlPanel extends JPanel {
        private final CommandInvoker invoker;
        private final JButton undoButton;
        private final JButton redoButton;
        private final JButton clearButton;
        private final JLabel statusLabel;
        
        public CommandControlPanel(CommandInvoker invoker) {
            this.invoker = invoker;
            setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT));
            
            // Botão Undo
            undoButton = new JButton("↶ Desfazer");
            undoButton.setToolTipText("Desfazer última ação (Ctrl+Z)");
            undoButton.addActionListener(e -> invoker.undo());
            
            // Botão Redo
            redoButton = new JButton("↷ Refazer");
            redoButton.setToolTipText("Refazer última ação (Ctrl+Y)");
            redoButton.addActionListener(e -> invoker.redo());
            
            // Botão Clear
            clearButton = new JButton("🗑️ Limpar Histórico");
            clearButton.setToolTipText("Limpar histórico de ações");
            clearButton.addActionListener(e -> invoker.clearHistory());
            
            // Label de status
            statusLabel = new JLabel("Histórico: 0 ações");
            statusLabel.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 0));
            
            // Adiciona componentes
            add(undoButton);
            add(redoButton);
            add(javax.swing.Box.createHorizontalStrut(10));
            add(clearButton);
            add(javax.swing.Box.createHorizontalStrut(20));
            add(statusLabel);
            
            // Atualiza estado inicial
            updateButtonStates();
            
            // Adiciona listener para atualização automática
            invoker.addHistoryListener(command -> {
                SwingUtilities.invokeLater(this::updateButtonStates);
            });
        }
        
        private void updateButtonStates() {
            undoButton.setEnabled(invoker.canUndo());
            redoButton.setEnabled(invoker.canRedo());
            statusLabel.setText("Histórico: " + invoker.getCommandHistory().size() + " ações");
        }
    }
}
