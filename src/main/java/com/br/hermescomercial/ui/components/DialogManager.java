package com.br.hermescomercial.ui.components;

import com.br.hermescomercial.ui.layout.LayoutPadrao;

import javax.swing.*;
import java.awt.*;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.function.Supplier;

/**
 * Gerenciador de Diálogos Reutilizável - Versão Refatorada
 * Componente UI para criação rápida de diálogos padronizados
 * Versão 2.0.0 - UI Components Refactoring
 */
public class DialogManager {
    
    // Tipos de diálogo
    public enum DialogType {
        INFO, WARNING, ERROR, SUCCESS, QUESTION, CONFIRMATION
    }
    
    // Resultado do diálogo
    public enum DialogResult {
        YES, NO, OK, CANCEL, CLOSE, CUSTOM
    }
    
    // Configuração de diálogo
    public static class DialogConfig {
        private String title;
        private String message;
        private DialogType type;
        private int messageType;
        private int optionType;
        private String[] options;
        private String customButtonText;
        private boolean modal = true;
        private boolean resizable = false;
        private Dimension size;
        private Component parent;
        private Map<String, Object> metadata;
        
        public DialogConfig(String title, String message, DialogType type) {
            this.title = title;
            this.message = message;
            this.type = type;
            this.metadata = new HashMap<>();
            configureDefaults();
        }
        
        private void configureDefaults() {
            switch (type) {
                case INFO:
                    messageType = JOptionPane.INFORMATION_MESSAGE;
                    optionType = JOptionPane.DEFAULT_OPTION;
                    break;
                case WARNING:
                    messageType = JOptionPane.WARNING_MESSAGE;
                    optionType = JOptionPane.DEFAULT_OPTION;
                    break;
                case ERROR:
                    messageType = JOptionPane.ERROR_MESSAGE;
                    optionType = JOptionPane.DEFAULT_OPTION;
                    break;
                case SUCCESS:
                    messageType = JOptionPane.PLAIN_MESSAGE;
                    optionType = JOptionPane.DEFAULT_OPTION;
                    break;
                case QUESTION:
                    messageType = JOptionPane.QUESTION_MESSAGE;
                    optionType = JOptionPane.YES_NO_OPTION;
                    break;
                case CONFIRMATION:
                    messageType = JOptionPane.WARNING_MESSAGE;
                    optionType = JOptionPane.YES_NO_CANCEL_OPTION;
                    break;
            }
        }
        
        // Fluent setters
        public DialogConfig title(String title) {
            this.title = title;
            return this;
        }
        
        public DialogConfig message(String message) {
            this.message = message;
            return this;
        }
        
        public DialogConfig type(DialogType type) {
            this.type = type;
            configureDefaults();
            return this;
        }
        
        public DialogConfig modal(boolean modal) {
            this.modal = modal;
            return this;
        }
        
        public DialogConfig resizable(boolean resizable) {
            this.resizable = resizable;
            return this;
        }
        
        public DialogConfig size(Dimension size) {
            this.size = size;
            return this;
        }
        
        public DialogConfig size(int width, int height) {
            this.size = new Dimension(width, height);
            return this;
        }
        
        public DialogConfig parent(Component parent) {
            this.parent = parent;
            return this;
        }
        
        public DialogConfig options(String... options) {
            this.options = options;
            this.optionType = JOptionPane.YES_NO_OPTION;
            return this;
        }
        
        public DialogConfig customButton(String customButtonText) {
            this.customButtonText = customButtonText;
            return this;
        }
        
        public DialogConfig addMetadata(String key, Object value) {
            metadata.put(key, value);
            return this;
        }
        
        // Getters
        public String getTitle() { return title; }
        public String getMessage() { return message; }
        public DialogType getType() { return type; }
        public int getMessageType() { return messageType; }
        public int getOptionType() { return optionType; }
        public String[] getOptions() { return options; }
        public String getCustomButtonText() { return customButtonText; }
        public boolean isModal() { return modal; }
        public boolean isResizable() { return resizable; }
        public Dimension getSize() { return size; }
        public Component getParent() { return parent; }
        public Map<String, Object> getMetadata() { return metadata; }
    }
    
    // Cache de diálogos
    private static final Map<String, JDialog> dialogCache = new HashMap<>();
    
    /**
     * Mostra diálogo de informação
     */
    public static void showInfo(String message) {
        showInfo("Informação", message);
    }
    
    public static void showInfo(String title, String message) {
        showDialog(new DialogConfig(title, message, DialogType.INFO));
    }
    
    /**
     * Mostra diálogo de sucesso
     */
    public static void showSuccess(String message) {
        showSuccess("Sucesso", message);
    }
    
    public static void showSuccess(String title, String message) {
        showDialog(new DialogConfig(title, message, DialogType.SUCCESS));
    }
    
    /**
     * Mostra diálogo de aviso
     */
    public static void showWarning(String message) {
        showWarning("Aviso", message);
    }
    
    public static void showWarning(String title, String message) {
        showDialog(new DialogConfig(title, message, DialogType.WARNING));
    }
    
    /**
     * Mostra diálogo de erro
     */
    public static void showError(String message) {
        showError("Erro", message);
    }
    
    public static void showError(String title, String message) {
        showDialog(new DialogConfig(title, message, DialogType.ERROR));
    }
    
    /**
     * Mostra diálogo de erro com exceção
     */
    public static void showError(String title, String message, Throwable exception) {
        String fullMessage = message + "\n\nDetalhes:\n" + getStackTrace(exception);
        showDialog(new DialogConfig(title, fullMessage, DialogType.ERROR));
    }
    
    /**
     * Mostra diálogo de pergunta
     */
    public static boolean showQuestion(String message) {
        return showQuestion("Confirmação", message);
    }
    
    public static boolean showQuestion(String title, String message) {
        return showConfirmDialog(new DialogConfig(title, message, DialogType.QUESTION));
    }
    
    /**
     * Mostra diálogo de confirmação
     */
    public static int showConfirmation(String message) {
        return showConfirmation("Confirmação", message);
    }
    
    public static int showConfirmation(String title, String message) {
        return showOptionDialog(new DialogConfig(title, message, DialogType.CONFIRMATION));
    }
    
    /**
     * Mostra diálogo personalizado
     */
    public static DialogResult showDialog(DialogConfig config) {
        if (config.getOptions() != null) {
            int result = showOptionDialog(config);
            return mapToDialogResult(result);
        } else {
            showSimpleDialog(config);
            return DialogResult.OK;
        }
    }
    
    /**
     * Mostra diálogo de entrada de texto
     */
    public static String showInputDialog(String message) {
        return showInputDialog("Entrada", message);
    }
    
    public static String showInputDialog(String title, String message) {
        return showInputDialog(title, message, "");
    }
    
    public static String showInputDialog(String title, String message, String defaultValue) {
        Object result = JOptionPane.showInputDialog(
            null,
            message,
            title,
            JOptionPane.QUESTION_MESSAGE,
            null,
            null,
            defaultValue
        );
        return result != null ? result.toString() : null;
    }
    
    /**
     * Mostra diálogo de entrada de senha
     */
    public static String showPasswordDialog(String message) {
        return showPasswordDialog("Senha", message);
    }
    
    public static String showPasswordDialog(String title, String message) {
        JPasswordField passwordField = new JPasswordField(20);
        passwordField.setFont(LayoutPadrao.FONTE_CAMPO);
        
        int result = JOptionPane.showConfirmDialog(
            null,
            new Object[]{message, passwordField},
            title,
            JOptionPane.OK_CANCEL_OPTION,
            JOptionPane.PLAIN_MESSAGE
        );
        
        return result == JOptionPane.OK_OPTION ? new String(passwordField.getPassword()) : null;
    }
    
    /**
     * Mostra diálogo de progresso
     */
    public static void showProgressDialog(String title, Supplier<Boolean> task) {
        JDialog dialog = new JDialog();
        dialog.setTitle(title);
        dialog.setModal(true);
        dialog.setResizable(false);
        dialog.setSize(300, 100);
        dialog.setLocationRelativeTo(null);
        
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        JProgressBar progressBar = new JProgressBar();
        progressBar.setIndeterminate(true);
        
        JLabel statusLabel = new JLabel("Processando...");
        statusLabel.setFont(LayoutPadrao.FONTE_TEXTO);
        statusLabel.setHorizontalAlignment(SwingConstants.CENTER);
        
        panel.add(statusLabel, BorderLayout.NORTH);
        panel.add(progressBar, BorderLayout.CENTER);
        
        dialog.add(panel);
        
        // Executar tarefa em thread separada
        new Thread(() -> {
            try {
                Boolean result = task.get();
                
                SwingUtilities.invokeLater(() -> {
                    dialog.dispose();
                    
                    if (result) {
                        showSuccess("Concluído", "Operação concluída com sucesso!");
                    } else {
                        showError("Falha", "Operação falhou. Verifique os logs.");
                    }
                });
                
            } catch (Exception e) {
                SwingUtilities.invokeLater(() -> {
                    dialog.dispose();
                    showError("Erro", "Ocorreu um erro durante o processamento.", e);
                });
            }
        }).start();
        
        dialog.setVisible(true);
    }
    
    /**
     * Mostra diálogo de formulário personalizado
     */
    public static Map<String, Object> showFormDialog(String title, FormPanel formPanel) {
        JDialog dialog = new JDialog();
        dialog.setTitle(title);
        dialog.setModal(true);
        dialog.setResizable(true);
        dialog.setSize(600, 400);
        dialog.setLocationRelativeTo(null);
        
        // Botões
        JButton okButton = LayoutPadrao.criarBotaoSucesso("OK");
        JButton cancelButton = LayoutPadrao.criarBotaoSecundario("Cancelar");
        
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.add(okButton);
        buttonPanel.add(cancelButton);
        
        // Adicionar botões ao formulário
        formPanel.addButton(okButton);
        formPanel.addButton(cancelButton);
        
        // Configurar ações
        final boolean[] result = {false};
        
        okButton.addActionListener(e -> {
            List<String> errors = formPanel.validateRequired();
            if (errors.isEmpty()) {
                result[0] = true;
                dialog.dispose();
            } else {
                showError("Erros de Validação", String.join("\n", errors));
            }
        });
        
        cancelButton.addActionListener(e -> {
            result[0] = false;
            dialog.dispose();
        });
        
        dialog.add(formPanel);
        dialog.setVisible(true);
        
        if (result[0]) {
            Map<String, Object> formData = new HashMap<>();
            for (Map.Entry<String, JComponent> entry : formPanel.getFields().entrySet()) {
                Object value = formPanel.getFieldValue(entry.getKey());
                formData.put(entry.getKey(), value);
            }
            return formData;
        }
        
        return null;
    }
    
    /**
     * Mostra diálogo simples
     */
    private static void showSimpleDialog(DialogConfig config) {
        JOptionPane.showMessageDialog(
            config.getParent(),
            config.getMessage(),
            config.getTitle(),
            config.getMessageType()
        );
    }
    
    /**
     * Mostra diálogo de opções
     */
    private static int showOptionDialog(DialogConfig config) {
        return JOptionPane.showOptionDialog(
            config.getParent(),
            config.getMessage(),
            config.getTitle(),
            config.getOptionType(),
            config.getMessageType(),
            null,
            config.getOptions(),
            config.getOptions() != null ? config.getOptions()[0] : null
        );
    }
    
    /**
     * Mostra diálogo de confirmação
     */
    private static boolean showConfirmDialog(DialogConfig config) {
        return JOptionPane.showConfirmDialog(
            config.getParent(),
            config.getMessage(),
            config.getTitle(),
            config.getOptionType(),
            config.getMessageType()
        ) == JOptionPane.YES_OPTION;
    }
    
    /**
     * Mapeia resultado para DialogResult
     */
    private static DialogResult mapToDialogResult(int result) {
        switch (result) {
            case JOptionPane.YES_OPTION:
                return DialogResult.YES;
            case JOptionPane.NO_OPTION:
                return DialogResult.NO;
            case JOptionPane.CANCEL_OPTION:
                return DialogResult.CANCEL;
            default:
                return DialogResult.CLOSE;
        }
    }
    
    /**
     * Obtém stack trace como string
     */
    private static String getStackTrace(Throwable throwable) {
        StringBuilder sb = new StringBuilder();
        sb.append(throwable.toString()).append("\n");
        
        for (StackTraceElement element : throwable.getStackTrace()) {
            sb.append("\tat ").append(element.toString()).append("\n");
        }
        
        return sb.toString();
    }
    
    /**
     * Limpa cache de diálogos
     */
    public static void clearCache() {
        for (JDialog dialog : dialogCache.values()) {
            dialog.dispose();
        }
        dialogCache.clear();
    }
    
    /**
     * Builder para configuração de diálogo
     */
    public static class DialogBuilder {
        private final DialogConfig config;
        
        public DialogBuilder(String title, String message, DialogType type) {
            this.config = new DialogConfig(title, message, type);
        }
        
        public DialogBuilder modal(boolean modal) {
            config.modal(modal);
            return this;
        }
        
        public DialogBuilder resizable(boolean resizable) {
            config.resizable(resizable);
            return this;
        }
        
        public DialogBuilder size(int width, int height) {
            config.size(width, height);
            return this;
        }
        
        public DialogBuilder parent(Component parent) {
            config.parent(parent);
            return this;
        }
        
        public DialogBuilder options(String... options) {
            config.options(options);
            return this;
        }
        
        public DialogBuilder addMetadata(String key, Object value) {
            config.addMetadata(key, value);
            return this;
        }
        
        public DialogResult show() {
            return showDialog(config);
        }
        
        public void showSimple() {
            showSimpleDialog(config);
        }
        
        public boolean confirm() {
            return showConfirmDialog(config);
        }
        
        public int option() {
            return showOptionDialog(config);
        }
    }
    
    /**
     * Cria builder de diálogo
     */
    public static DialogBuilder builder(String title, String message, DialogType type) {
        return new DialogBuilder(title, message, type);
    }
}
