package com.br.hermescomercial.ui.components;

import com.br.hermescomercial.ui.layout.LayoutPadrao;

import javax.swing.*;
import java.awt.*;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;

/**
 * Painel de Formulário Reutilizável - Versão Refatorada
 * Componente UI para criação rápida de formulários padronizados
 * Versão 2.0.0 - UI Components Refactoring
 */
public class FormPanel extends JPanel {
    
    // Tipos de campos
    public enum FieldType {
        TEXT, PASSWORD, NUMBER, EMAIL, PHONE, CURRENCY, DATE, TIME, TEXTAREA, COMBOBOX, CHECKBOX, RADIO
    }
    
    // Estrutura de campo
    public static class FieldConfig {
        private final String label;
        private final String name;
        private final FieldType type;
        private final boolean required;
        private final int columns;
        private final Object[] options;
        private final Map<String, Object> metadata;
        
        public FieldConfig(String label, String name, FieldType type) {
            this(label, name, type, false, 20, null);
        }
        
        public FieldConfig(String label, String name, FieldType type, boolean required) {
            this(label, name, type, required, 20, null);
        }
        
        public FieldConfig(String label, String name, FieldType type, boolean required, int columns) {
            this(label, name, type, required, columns, null);
        }
        
        public FieldConfig(String label, String name, FieldType type, boolean required, int columns, Object[] options) {
            this.label = label;
            this.name = name;
            this.type = type;
            this.required = required;
            this.columns = columns;
            this.options = options;
            this.metadata = new HashMap<>();
        }
        
        public FieldConfig addMetadata(String key, Object value) {
            metadata.put(key, value);
            return this;
        }
        
        // Getters
        public String getLabel() { return label; }
        public String getName() { return name; }
        public FieldType getType() { return type; }
        public boolean isRequired() { return required; }
        public int getColumns() { return columns; }
        public Object[] getOptions() { return options; }
        public Map<String, Object> getMetadata() { return metadata; }
    }
    
    // Componentes do formulário
    private final Map<String, JComponent> fields = new HashMap<>();
    private final Map<String, JLabel> labels = new HashMap<>();
    private final List<FieldConfig> fieldConfigs = new ArrayList<>();
    
    // Configurações
    private int columns = 2;
    private int gap = 5;
    private boolean showRequiredIndicator = true;
    
    // Layout
    private final JPanel contentPanel;
    private final JPanel buttonPanel;
    
    public FormPanel() {
        this(2);
    }
    
    public FormPanel(int columns) {
        this.columns = columns;
        setLayout(new BorderLayout());
        setOpaque(false);
        
        // Painel de conteúdo
        contentPanel = new JPanel(new GridBagLayout());
        contentPanel.setOpaque(false);
        contentPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        // Painel de botões
        buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.setOpaque(false);
        
        add(contentPanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);
    }
    
    /**
     * Adiciona campo ao formulário
     */
    public void addField(FieldConfig config) {
        fieldConfigs.add(config);
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(gap, gap, gap, gap);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.WEST;
        
        // Posição do campo
        int totalFields = fields.size();
        int row = totalFields / columns;
        int col = totalFields % columns;
        
        gbc.gridx = col * 2;
        gbc.gridy = row;
        gbc.weightx = 0.0;
        
        // Label
        JLabel label = createLabel(config);
        contentPanel.add(label, gbc);
        labels.put(config.getName(), label);
        
        // Campo
        gbc.gridx = col * 2 + 1;
        gbc.weightx = 1.0;
        
        JComponent field = createField(config);
        contentPanel.add(field, gbc);
        fields.put(config.getName(), field);
    }
    
    /**
     * Adiciona campo simples
     */
    public void addField(String label, String name, FieldType type) {
        addField(new FieldConfig(label, name, type));
    }
    
    /**
     * Adiciona campo obrigatório
     */
    public void addRequiredField(String label, String name, FieldType type) {
        addField(new FieldConfig(label, name, type, true));
    }
    
    /**
     * Adiciona campo com opções
     */
    public void addComboBox(String label, String name, Object[] options) {
        addField(new FieldConfig(label, name, FieldType.COMBOBOX, false, 20, options));
    }
    
    /**
     * Adiciona separador
     */
    public void addSeparator(String text) {
        int totalFields = fields.size();
        int row = (totalFields + columns - 1) / columns;
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = row;
        gbc.gridwidth = columns * 2;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(15, gap, 5, gap);
        
        JSeparator separator = new JSeparator();
        if (text != null && !text.trim().isEmpty()) {
            separator.setBorder(BorderFactory.createTitledBorder(text));
        }
        
        contentPanel.add(separator, gbc);
    }
    
    /**
     * Adiciona botão ao painel de botões
     */
    public void addButton(JButton button) {
        buttonPanel.add(button);
    }
    
    /**
     * Adiciona botão usando LayoutPadrao
     */
    public void addButton(String text, String type) {
        JButton button = createButton(text, type);
        addButton(button);
    }
    
    /**
     * Obtém valor do campo
     */
    public Object getFieldValue(String name) {
        JComponent field = fields.get(name);
        if (field == null) return null;
        
        if (field instanceof JTextField) {
            return ((JTextField) field).getText().trim();
        } else if (field instanceof JPasswordField) {
            return new String(((JPasswordField) field).getPassword());
        } else if (field instanceof JTextArea) {
            return ((JTextArea) field).getText().trim();
        } else if (field instanceof JComboBox) {
            return ((JComboBox<?>) field).getSelectedItem();
        } else if (field instanceof JCheckBox) {
            return ((JCheckBox) field).isSelected();
        } else if (field instanceof JSpinner) {
            return ((JSpinner) field).getValue();
        }
        
        return null;
    }
    
    /**
     * Define valor do campo
     */
    public void setFieldValue(String name, Object value) {
        JComponent field = fields.get(name);
        if (field == null || value == null) return;
        
        if (field instanceof JTextField) {
            ((JTextField) field).setText(value.toString());
        } else if (field instanceof JPasswordField) {
            ((JPasswordField) field).setText(value.toString());
        } else if (field instanceof JTextArea) {
            ((JTextArea) field).setText(value.toString());
        } else if (field instanceof JComboBox) {
            ((JComboBox<?>) field).setSelectedItem(value);
        } else if (field instanceof JCheckBox) {
            ((JCheckBox) field).setSelected(Boolean.valueOf(value.toString()));
        } else if (field instanceof JSpinner) {
            ((JSpinner) field).setValue(value);
        }
    }
    
    /**
     * Obtém campo por nome
     */
    public JComponent getField(String name) {
        return fields.get(name);
    }
    
    /**
     * Limpa todos os campos
     */
    public void clearFields() {
        for (JComponent field : fields.values()) {
            if (field instanceof JTextField) {
                ((JTextField) field).setText("");
            } else if (field instanceof JPasswordField) {
                ((JPasswordField) field).setText("");
            } else if (field instanceof JTextArea) {
                ((JTextArea) field).setText("");
            } else if (field instanceof JComboBox) {
                ((JComboBox<?>) field).setSelectedIndex(0);
            } else if (field instanceof JCheckBox) {
                ((JCheckBox) field).setSelected(false);
            } else if (field instanceof JSpinner) {
                ((JSpinner) field).setValue(0);
            }
        }
    }
    
    /**
     * Habilita/desabilita todos os campos
     */
    public void setFieldsEnabled(boolean enabled) {
        for (JComponent field : fields.values()) {
            field.setEnabled(enabled);
        }
        
        for (JLabel label : labels.values()) {
            label.setEnabled(enabled);
        }
    }
    
    /**
     * Valida campos obrigatórios
     */
    public List<String> validateRequired() {
        List<String> errors = new ArrayList<>();
        
        for (FieldConfig config : fieldConfigs) {
            if (config.isRequired()) {
                Object value = getFieldValue(config.getName());
                if (value == null || value.toString().trim().isEmpty()) {
                    errors.add(config.getLabel() + " é obrigatório");
                }
            }
        }
        
        return errors;
    }
    
    /**
     * Cria label para campo
     */
    private JLabel createLabel(FieldConfig config) {
        String labelText = config.getLabel();
        if (config.isRequired() && showRequiredIndicator) {
            labelText += " *";
        }
        
        JLabel label = new JLabel(labelText);
        label.setFont(LayoutPadrao.FONTE_ROTULO);
        label.setForeground(LayoutPadrao.COR_TEXTO);
        
        return label;
    }
    
    /**
     * Cria campo baseado no tipo
     */
    private JComponent createField(FieldConfig config) {
        switch (config.getType()) {
            case TEXT:
                return createTextField(config.getColumns());
            case PASSWORD:
                return createPasswordField(config.getColumns());
            case NUMBER:
                return createNumberField(config.getColumns());
            case EMAIL:
                return createEmailField(config.getColumns());
            case PHONE:
                return createPhoneField(config.getColumns());
            case CURRENCY:
                return createCurrencyField(config.getColumns());
            case DATE:
                return createDateField(config.getColumns());
            case TIME:
                return createTimeField(config.getColumns());
            case TEXTAREA:
                return createTextArea(config.getColumns(), 3);
            case COMBOBOX:
                return createComboBox(config.getOptions());
            case CHECKBOX:
                return createCheckBox(config.getLabel());
            case RADIO:
                return createRadioButtonGroup(config.getLabel(), config.getOptions());
            default:
                return createTextField(config.getColumns());
        }
    }
    
    /**
     * Cria campo de texto
     */
    private JTextField createTextField(int columns) {
        JTextField field = new JTextField(columns);
        configureField(field);
        return field;
    }
    
    /**
     * Cria campo de senha
     */
    private JPasswordField createPasswordField(int columns) {
        JPasswordField field = new JPasswordField(columns);
        configureField(field);
        return field;
    }
    
    /**
     * Cria campo numérico
     */
    private JSpinner createNumberField(int columns) {
        JSpinner field = new JSpinner(new SpinnerNumberModel(0, 0, Integer.MAX_VALUE, 1));
        configureField(field);
        return field;
    }
    
    /**
     * Cria campo de email
     */
    private JTextField createEmailField(int columns) {
        JTextField field = new JTextField(columns);
        configureField(field);
        field.setToolTipText("Formato: usuario@dominio.com");
        return field;
    }
    
    /**
     * Cria campo de telefone
     */
    private JTextField createPhoneField(int columns) {
        JTextField field = new JTextField(columns);
        configureField(field);
        field.setToolTipText("Formato: (00) 00000-0000");
        return field;
    }
    
    /**
     * Cria campo de moeda
     */
    private JTextField createCurrencyField(int columns) {
        JTextField field = new JTextField(columns);
        configureField(field);
        field.setToolTipText("Formato: 0,00");
        return field;
    }
    
    /**
     * Cria campo de data
     */
    private JTextField createDateField(int columns) {
        JTextField field = new JTextField(columns);
        configureField(field);
        field.setToolTipText("Formato: dd/MM/yyyy");
        return field;
    }
    
    /**
     * Cria campo de hora
     */
    private JTextField createTimeField(int columns) {
        JTextField field = new JTextField(columns);
        configureField(field);
        field.setToolTipText("Formato: HH:mm");
        return field;
    }
    
    /**
     * Cria área de texto
     */
    private JTextArea createTextArea(int columns, int rows) {
        JTextArea field = new JTextArea(rows, columns);
        field.setLineWrap(true);
        field.setWrapStyleWord(true);
        configureField(field);
        return field;
    }
    
    /**
     * Cria combo box
     */
    private JComboBox<Object> createComboBox(Object[] options) {
        JComboBox<Object> field = new JComboBox<>(options);
        configureField(field);
        return field;
    }
    
    /**
     * Cria checkbox
     */
    private JCheckBox createCheckBox(String text) {
        JCheckBox field = new JCheckBox(text);
        field.setFont(LayoutPadrao.FONTE_CAMPO);
        field.setForeground(LayoutPadrao.COR_TEXTO);
        field.setBackground(Color.WHITE);
        return field;
    }
    
    /**
     * Cria grupo de radio buttons
     */
    private JPanel createRadioButtonGroup(String title, Object[] options) {
        JPanel group = new JPanel();
        group.setLayout(new FlowLayout(FlowLayout.LEFT));
        group.setOpaque(false);
        
        ButtonGroup buttonGroup = new ButtonGroup();
        
        for (Object option : options) {
            JRadioButton radio = new JRadioButton(option.toString());
            radio.setFont(LayoutPadrao.FONTE_CAMPO);
            radio.setForeground(LayoutPadrao.COR_TEXTO);
            radio.setBackground(Color.WHITE);
            
            buttonGroup.add(radio);
            group.add(radio);
        }
        
        if (title != null && !title.trim().isEmpty()) {
            group.setBorder(BorderFactory.createTitledBorder(title));
        }
        
        return group;
    }
    
    /**
     * Configura campo com estilo padrão
     */
    private void configureField(JComponent field) {
        if (field instanceof JTextField || field instanceof JPasswordField) {
            field.setFont(LayoutPadrao.FONTE_CAMPO);
            field.setForeground(LayoutPadrao.COR_TEXTO);
            field.setBackground(Color.WHITE);
            field.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(LayoutPadrao.COR_BORDA, 1),
                BorderFactory.createEmptyBorder(5, 5, 5, 5)
            ));
        } else if (field instanceof JTextArea) {
            field.setFont(LayoutPadrao.FONTE_CAMPO);
            field.setForeground(LayoutPadrao.COR_TEXTO);
            field.setBackground(Color.WHITE);
            field.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(LayoutPadrao.COR_BORDA, 1),
                BorderFactory.createEmptyBorder(5, 5, 5, 5)
            ));
        } else if (field instanceof JComboBox || field instanceof JSpinner) {
            field.setFont(LayoutPadrao.FONTE_CAMPO);
            field.setForeground(LayoutPadrao.COR_TEXTO);
            field.setBackground(Color.WHITE);
        }
    }
    
    /**
     * Cria botão usando LayoutPadrao
     */
    private JButton createButton(String text, String type) {
        switch (type.toLowerCase()) {
            case "success":
                return LayoutPadrao.criarBotaoSucesso(text);
            case "primary":
                return LayoutPadrao.criarBotaoPrimario(text);
            case "danger":
                return LayoutPadrao.criarBotaoPerigo(text);
            case "secondary":
                return LayoutPadrao.criarBotaoSecundario(text);
            default:
                return LayoutPadrao.criarBotaoPrimario(text);
        }
    }
    
    // Getters e Setters
    public int getColumns() { return columns; }
    public void setColumns(int columns) { this.columns = columns; }
    
    public int getGap() { return gap; }
    public void setGap(int gap) { this.gap = gap; }
    
    public boolean isShowRequiredIndicator() { return showRequiredIndicator; }
    public void setShowRequiredIndicator(boolean showRequiredIndicator) { 
        this.showRequiredIndicator = showRequiredIndicator; 
    }
    
    public Map<String, JComponent> getFields() { return new HashMap<>(fields); }
    public JPanel getButtonPanel() { return buttonPanel; }
}
