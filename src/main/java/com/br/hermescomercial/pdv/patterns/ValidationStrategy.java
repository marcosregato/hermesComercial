package com.br.hermescomercial.pdv.patterns;

import javax.swing.*;
import java.awt.Color;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

/**
 * Strategy Pattern - Estratégias de Validação
 * Permite diferentes algoritmos de validação para diferentes tipos de dados
 */
public class ValidationStrategy {
    
    /**
     * Interface base para estratégias de validação
     */
    public interface Validator {
        ValidationResult validate(Object value, String fieldName);
        String getValidatorName();
    }
    
    /**
     * Resultado de uma validação
     */
    public static class ValidationResult {
        private boolean valid;
        private String errorMessage;
        private String fieldName;
        private String suggestion;
        
        public ValidationResult(boolean valid, String errorMessage, String fieldName) {
            this(valid, errorMessage, fieldName, null);
        }
        
        public ValidationResult(boolean valid, String errorMessage, String fieldName, String suggestion) {
            this.valid = valid;
            this.errorMessage = errorMessage;
            this.fieldName = fieldName;
            this.suggestion = suggestion;
        }
        
        public boolean isValid() { return valid; }
        public String getErrorMessage() { return errorMessage; }
        public String getFieldName() { return fieldName; }
        public String getSuggestion() { return suggestion; }
    }
    
    /**
     * Estratégia para validação de campos obrigatórios
     */
    public static class RequiredValidator implements Validator {
        @Override
        public ValidationResult validate(Object value, String fieldName) {
            if (value == null || (value instanceof String && ((String) value).trim().isEmpty())) {
                return new ValidationResult(false, 
                    "⚠️ O campo " + fieldName + " é obrigatório!\n\nPor favor, informe o " + fieldName.toLowerCase() + ".", 
                    fieldName);
            }
            return new ValidationResult(true, null, fieldName);
        }
        
        @Override
        public String getValidatorName() { return "Required"; }
    }
    
    /**
     * Estratégia para validação de email
     */
    public static class EmailValidator implements Validator {
        private static final Pattern EMAIL_PATTERN = 
            Pattern.compile("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$");
        
        @Override
        public ValidationResult validate(Object value, String fieldName) {
            if (value == null || ((String) value).trim().isEmpty()) {
                return new ValidationResult(false, 
                    "⚠️ O Email é obrigatório!\n\nPor favor, informe um email válido.", 
                    fieldName);
            }
            
            String email = ((String) value).trim();
            if (!EMAIL_PATTERN.matcher(email).matches()) {
                return new ValidationResult(false, 
                    "⚠️ Email inválido!\n\nPor favor, informe um email válido (ex: usuario@dominio.com).", 
                    fieldName, "Ex: usuario@dominio.com");
            }
            
            return new ValidationResult(true, null, fieldName);
        }
        
        @Override
        public String getValidatorName() { return "Email"; }
    }
    
    /**
     * Estratégia para validação de valores numéricos
     */
    public static class NumericValidator implements Validator {
        private final double minValue;
        private final boolean allowZero;
        
        public NumericValidator() {
            this(0.01, false); // Padrão: não permite zero, mínimo 0.01
        }
        
        public NumericValidator(double minValue, boolean allowZero) {
            this.minValue = minValue;
            this.allowZero = allowZero;
        }
        
        @Override
        public ValidationResult validate(Object value, String fieldName) {
            if (value == null || ((String) value).trim().isEmpty()) {
                return new ValidationResult(false, 
                    "⚠️ O campo " + fieldName + " é obrigatório!\n\nPor favor, informe um valor numérico.", 
                    fieldName);
            }
            
            try {
                double numValue = Double.parseDouble(((String) value).replace(",", "."));
                
                if (!allowZero && numValue <= 0) {
                    return new ValidationResult(false, 
                        "⚠️ O " + fieldName + " deve ser maior que zero!\n\nPor favor, informe um valor válido.", 
                        fieldName);
                }
                
                if (numValue < minValue) {
                    return new ValidationResult(false, 
                        "⚠️ O " + fieldName + " deve ser maior que " + minValue + "!\n\nPor favor, informe um valor válido.", 
                        fieldName);
                }
                
            } catch (NumberFormatException e) {
                return new ValidationResult(false, 
                    "⚠️ Valor numérico inválido!\n\nPor favor, informe apenas números no campo " + fieldName + ".", 
                    fieldName);
            }
            
            return new ValidationResult(true, null, fieldName);
        }
        
        @Override
        public String getValidatorName() { return "Numeric"; }
    }
    
    /**
     * Estratégia para validação de CPF/CNPJ
     */
    public static class DocumentValidator implements Validator {
        @Override
        public ValidationResult validate(Object value, String fieldName) {
            if (value == null || ((String) value).trim().isEmpty()) {
                return new ValidationResult(false, 
                    "⚠️ O " + fieldName + " é obrigatório!\n\nPor favor, informe o documento.", 
                    fieldName);
            }
            
            String doc = ((String) value).replaceAll("[^0-9]", "");
            
            if (doc.length() == 11) {
                return validateCPF(doc, fieldName);
            } else if (doc.length() == 14) {
                return validateCNPJ(doc, fieldName);
            } else {
                return new ValidationResult(false, 
                    "⚠️ Documento inválido!\n\nPor favor, informe um CPF (11 dígitos) ou CNPJ (14 dígitos).", 
                    fieldName);
            }
        }
        
        private ValidationResult validateCPF(String cpf, String fieldName) {
            if (cpf.length() != 11 || cpf.matches("(\\d)\\1{10}")) {
                return new ValidationResult(false, 
                    "⚠️ CPF inválido!\n\nPor favor, informe um CPF válido.", 
                    fieldName);
            }
            return new ValidationResult(true, null, fieldName);
        }
        
        private ValidationResult validateCNPJ(String cnpj, String fieldName) {
            if (cnpj.length() != 14 || cnpj.matches("(\\d)\\1{13}")) {
                return new ValidationResult(false, 
                    "⚠️ CNPJ inválido!\n\nPor favor, informe um CNPJ válido.", 
                    fieldName);
            }
            return new ValidationResult(true, null, fieldName);
        }
        
        @Override
        public String getValidatorName() { return "Document"; }
    }
    
    /**
     * Estratégia para validação de senhas
     */
    public static class PasswordValidator implements Validator {
        private final int minLength;
        private final boolean requireUppercase;
        private final boolean requireNumbers;
        private final boolean requireSpecialChars;
        
        public PasswordValidator() {
            this(6, true, true, false);
        }
        
        public PasswordValidator(int minLength, boolean requireUppercase, boolean requireNumbers, boolean requireSpecialChars) {
            this.minLength = minLength;
            this.requireUppercase = requireUppercase;
            this.requireNumbers = requireNumbers;
            this.requireSpecialChars = requireSpecialChars;
        }
        
        @Override
        public ValidationResult validate(Object value, String fieldName) {
            if (value == null || ((String) value).trim().isEmpty()) {
                return new ValidationResult(false, 
                    "⚠️ A Senha é obrigatória!\n\nPor favor, informe uma senha.", 
                    fieldName);
            }
            
            String password = ((String) value).trim();
            List<String> errors = new ArrayList<>();
            
            if (password.length() < minLength) {
                errors.add("mínimo " + minLength + " caracteres");
            }
            
            if (requireUppercase && !password.matches(".*[A-Z].*")) {
                errors.add("pelo menos uma letra maiúscula");
            }
            
            if (requireNumbers && !password.matches(".*[0-9].*")) {
                errors.add("pelo menos um número");
            }
            
            if (requireSpecialChars && !password.matches(".*[!@#$%^&*].*")) {
                errors.add("pelo menos um caractere especial (!@#$%^&*)");
            }
            
            if (!errors.isEmpty()) {
                String errorMsg = "⚠️ Senha inválida!\n\nA senha deve conter: " + String.join(", ", errors) + ".";
                return new ValidationResult(false, errorMsg, fieldName);
            }
            
            return new ValidationResult(true, null, fieldName);
        }
        
        @Override
        public String getValidatorName() { return "Password"; }
    }
    
    /**
     * Estratégia para validação de seleção em combos
     */
    public static class SelectionValidator implements Validator {
        private final String invalidValue;
        
        public SelectionValidator() {
            this(""); // Padrão: string vazia é inválida
        }
        
        public SelectionValidator(String invalidValue) {
            this.invalidValue = invalidValue;
        }
        
        @Override
        public ValidationResult validate(Object value, String fieldName) {
            if (value == null || value.toString().equals(invalidValue)) {
                return new ValidationResult(false, 
                    "⚠️ A seleção do campo " + fieldName + " é obrigatória!\n\nPor favor, selecione uma opção válida.", 
                    fieldName);
            }
            return new ValidationResult(true, null, fieldName);
        }
        
        @Override
        public String getValidatorName() { return "Selection"; }
    }
    
    /**
     * Contexto de validação que aplica múltiplas estratégias
     */
    public static class ValidationContext {
        private List<Validator> validators;
        private List<ValidationResult> results;
        
        public ValidationContext() {
            this.validators = new ArrayList<>();
            this.results = new ArrayList<>();
        }
        
        public ValidationContext addValidator(Validator validator) {
            validators.add(validator);
            return this;
        }
        
        public ValidationContext addRequired(String fieldName, Object value) {
            return addValidator(new RequiredFieldValidator(fieldName, value));
        }
        
        public ValidationContext addEmail(String fieldName, Object value) {
            return addValidator(new EmailFieldValidator(fieldName, value));
        }
        
        public ValidationContext addNumeric(String fieldName, Object value) {
            return addValidator(new NumericFieldValidator(fieldName, value));
        }
        
        public ValidationContext addDocument(String fieldName, Object value) {
            return addValidator(new DocumentFieldValidator(fieldName, value));
        }
        
        public ValidationContext addPassword(String fieldName, Object value) {
            return addValidator(new PasswordFieldValidator(fieldName, value));
        }
        
        public ValidationContext addSelection(String fieldName, Object value) {
            return addValidator(new SelectionFieldValidator(fieldName, value));
        }
        
        public boolean validate() {
            results.clear();
            boolean allValid = true;
            
            for (Validator validator : validators) {
                ValidationResult result = validator.validate(null, ""); // Valores são injetados nos validadores específicos
                results.add(result);
                if (!result.isValid()) {
                    allValid = false;
                }
            }
            
            return allValid;
        }
        
        public List<ValidationResult> getResults() {
            return new ArrayList<>(results);
        }
        
        public List<String> getErrorMessages() {
            return results.stream()
                    .filter(r -> !r.isValid())
                    .map(ValidationResult::getErrorMessage)
                    .collect(java.util.stream.Collectors.toList());
        }
        
        public void applyErrorStyles(JComponent... components) {
            for (int i = 0; i < Math.min(results.size(), components.length); i++) {
                ValidationResult result = results.get(i);
                if (result.isValid()) {
                    components[i].setBorder(BorderFactory.createLineBorder(Color.GRAY));
                } else {
                    components[i].setBorder(BorderFactory.createLineBorder(Color.RED, 2));
                    components[i].requestFocus();
                }
            }
        }
    }
    
    // Classes internas para validação de campos específicos
    private static class RequiredFieldValidator implements Validator {
        private final String fieldName;
        private final Object value;
        
        public RequiredFieldValidator(String fieldName, Object value) {
            this.fieldName = fieldName;
            this.value = value;
        }
        
        @Override
        public ValidationResult validate(Object value, String fieldName) {
            return new RequiredValidator().validate(this.value, this.fieldName);
        }
        
        @Override
        public String getValidatorName() { return "RequiredField"; }
    }
    
    private static class EmailFieldValidator implements Validator {
        private final String fieldName;
        private final Object value;
        
        public EmailFieldValidator(String fieldName, Object value) {
            this.fieldName = fieldName;
            this.value = value;
        }
        
        @Override
        public ValidationResult validate(Object value, String fieldName) {
            return new EmailValidator().validate(this.value, this.fieldName);
        }
        
        @Override
        public String getValidatorName() { return "EmailField"; }
    }
    
    private static class NumericFieldValidator implements Validator {
        private final String fieldName;
        private final Object value;
        
        public NumericFieldValidator(String fieldName, Object value) {
            this.fieldName = fieldName;
            this.value = value;
        }
        
        @Override
        public ValidationResult validate(Object value, String fieldName) {
            return new NumericValidator().validate(this.value, this.fieldName);
        }
        
        @Override
        public String getValidatorName() { return "NumericField"; }
    }
    
    private static class DocumentFieldValidator implements Validator {
        private final String fieldName;
        private final Object value;
        
        public DocumentFieldValidator(String fieldName, Object value) {
            this.fieldName = fieldName;
            this.value = value;
        }
        
        @Override
        public ValidationResult validate(Object value, String fieldName) {
            return new DocumentValidator().validate(this.value, this.fieldName);
        }
        
        @Override
        public String getValidatorName() { return "DocumentField"; }
    }
    
    private static class PasswordFieldValidator implements Validator {
        private final String fieldName;
        private final Object value;
        
        public PasswordFieldValidator(String fieldName, Object value) {
            this.fieldName = fieldName;
            this.value = value;
        }
        
        @Override
        public ValidationResult validate(Object value, String fieldName) {
            return new PasswordValidator().validate(this.value, this.fieldName);
        }
        
        @Override
        public String getValidatorName() { return "PasswordField"; }
    }
    
    private static class SelectionFieldValidator implements Validator {
        private final String fieldName;
        private final Object value;
        
        public SelectionFieldValidator(String fieldName, Object value) {
            this.fieldName = fieldName;
            this.value = value;
        }
        
        @Override
        public ValidationResult validate(Object value, String fieldName) {
            return new SelectionValidator().validate(this.value, this.fieldName);
        }
        
        @Override
        public String getValidatorName() { return "SelectionField"; }
    }
    
    /**
     * Método utilitário para validação rápida
     */
    public static ValidationContext createValidation() {
        return new ValidationContext();
    }
    
    /**
     * Validação completa para formulário de despesas
     */
    public static ValidationContext createDespesaValidation(
            String descricao, String valor, String data, String categoria) {
        
        return createValidation()
                .addRequired("Descrição", descricao)
                .addNumeric("Valor", valor)
                .addRequired("Data", data)
                .addRequired("Categoria", categoria);
    }
    
    /**
     * Validação completa para formulário de fornecedor
     */
    public static ValidationContext createFornecedorValidation(
            String nome, String documento, String telefone) {
        
        return createValidation()
                .addRequired("Nome", nome)
                .addDocument("Documento", documento)
                .addRequired("Telefone", telefone);
    }
    
    /**
     * Validação completa para formulário de usuário
     */
    public static ValidationContext createUsuarioValidation(
            String nome, String usuario, String email, String senha) {
        
        return createValidation()
                .addRequired("Nome", nome)
                .addRequired("Usuário", usuario)
                .addEmail("Email", email)
                .addPassword("Senha", senha);
    }
    
    /**
     * Exibe mensagens de erro em lote
     */
    public static void showValidationErrors(List<ValidationResult> errors, JFrame parent) {
        if (errors.isEmpty()) return;
        
        StringBuilder message = new StringBuilder("⚠️ Foram encontrados os seguintes erros de validação:\n\n");
        for (int i = 0; i < errors.size(); i++) {
            ValidationResult error = errors.get(i);
            message.append(i + 1).append(". ").append(error.getErrorMessage()).append("\n");
        }
        
        JOptionPane.showMessageDialog(parent, message.toString(), 
            "Validação - Erros Encontrados", JOptionPane.WARNING_MESSAGE);
    }
}
