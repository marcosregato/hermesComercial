package com.br.hermescomercial.util;

import javax.swing.*;
import javax.swing.text.JTextComponent;
import java.awt.Color;
import java.util.regex.Pattern;

/**
 * Classe utilitária para validação de campos do sistema Hermes Comercial PDV
 * @author Hermes Comercial
 * @version 2.8.0
 */
public class FieldValidator {
    
    // Padrões de validação
    private static final Pattern EMAIL_PATTERN = Pattern.compile("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$");
    private static final Pattern TELEFONE_PATTERN = Pattern.compile("^\\d{10,11}$");
    private static final Pattern CODIGO_PATTERN = Pattern.compile("^[A-Za-z0-9]{3,20}$");
    
    // Cores para feedback visual
    private static final Color COLOR_VALID = new Color(200, 255, 200); // Verde claro
    private static final Color COLOR_INVALID = new Color(255, 200, 200); // Vermelho claro
    private static final Color COLOR_DEFAULT = Color.WHITE;
    
    /**
     * Valida campo de texto obrigatório
     */
    public static boolean validateRequired(JTextComponent field, String fieldName) {
        String value = field.getText().trim();
        
        if (value.isEmpty()) {
            showError(field, fieldName + " é obrigatório!");
            return false;
        }
        
        showValid(field);
        return true;
    }
    
    /**
     * Valida campo de usuário (login)
     */
    public static boolean validateUsuario(JTextComponent field) {
        String value = field.getText().trim();
        
        if (value.isEmpty()) {
            showError(field, "Usuário é obrigatório!");
            return false;
        }
        
        if (value.length() < 3) {
            showError(field, "Usuário deve ter pelo menos 3 caracteres!");
            return false;
        }
        
        if (value.length() > 20) {
            showError(field, "Usuário deve ter no máximo 20 caracteres!");
            return false;
        }
        
        showValid(field);
        return true;
    }
    
    /**
     * Valida campo de senha
     */
    public static boolean validateSenha(JTextComponent field) {
        String value = new String(((JPasswordField) field).getPassword()).trim();
        
        if (value.isEmpty()) {
            showError(field, "Senha é obrigatória!");
            return false;
        }
        
        if (value.length() < 4) {
            showError(field, "Senha deve ter pelo menos 4 caracteres!");
            return false;
        }
        
        showValid(field);
        return true;
    }
    
    /**
     * Valida código de produto
     */
    public static boolean validateCodigoProduto(JTextComponent field) {
        String value = field.getText().trim();
        
        if (value.isEmpty()) {
            showError(field, "Código do produto é obrigatório!");
            return false;
        }
        
        if (!CODIGO_PATTERN.matcher(value).matches()) {
            showError(field, "Código inválido! Use 3-20 caracteres alfanuméricos.");
            return false;
        }
        
        showValid(field);
        return true;
    }
    
    /**
     * Valida nome do produto
     */
    public static boolean validateNomeProduto(JTextComponent field) {
        String value = field.getText().trim();
        
        if (value.isEmpty()) {
            showError(field, "Nome do produto é obrigatório!");
            return false;
        }
        
        if (value.length() < 3) {
            showError(field, "Nome deve ter pelo menos 3 caracteres!");
            return false;
        }
        
        if (value.length() > 100) {
            showError(field, "Nome deve ter no máximo 100 caracteres!");
            return false;
        }
        
        showValid(field);
        return true;
    }
    
    /**
     * Valida preço do produto
     */
    public static boolean validatePreco(JTextComponent field) {
        String value = field.getText().trim();
        
        if (value.isEmpty()) {
            showError(field, "Preço é obrigatório!");
            return false;
        }
        
        try {
            double preco = Double.parseDouble(value.replace(",", "."));
            
            if (preco <= 0) {
                showError(field, "Preço deve ser maior que zero!");
                return false;
            }
            
            if (preco > 999999.99) {
                showError(field, "Preço muito alto! Máximo: R$ 999.999,99");
                return false;
            }
            
            showValid(field);
            return true;
            
        } catch (NumberFormatException e) {
            showError(field, "Preço inválido! Use números apenas.");
            return false;
        }
    }
    
    /**
     * Valida quantidade em estoque
     */
    public static boolean validateQuantidade(JTextComponent field) {
        String value = field.getText().trim();
        
        if (value.isEmpty()) {
            showError(field, "Quantidade é obrigatória!");
            return false;
        }
        
        try {
            int quantidade = Integer.parseInt(value);
            
            if (quantidade < 0) {
                showError(field, "Quantidade não pode ser negativa!");
                return false;
            }
            
            if (quantidade > 99999) {
                showError(field, "Quantidade muito alta! Máximo: 99.999");
                return false;
            }
            
            showValid(field);
            return true;
            
        } catch (NumberFormatException e) {
            showError(field, "Quantidade inválida! Use números inteiros.");
            return false;
        }
    }
    
    /**
     * Valida quantidade mínima de estoque
     */
    public static boolean validateEstoqueMinimo(JTextComponent field) {
        String value = field.getText().trim();
        
        if (value.isEmpty()) {
            showError(field, "Estoque mínimo é obrigatório!");
            return false;
        }
        
        try {
            int minimo = Integer.parseInt(value);
            
            if (minimo < 0) {
                showError(field, "Estoque mínimo não pode ser negativo!");
                return false;
            }
            
            showValid(field);
            return true;
            
        } catch (NumberFormatException e) {
            showError(field, "Estoque mínimo inválido! Use números inteiros.");
            return false;
        }
    }
    
    /**
     * Valida quantidade máxima de estoque
     */
    public static boolean validateEstoqueMaximo(JTextComponent field, JTextComponent minimoField) {
        String value = field.getText().trim();
        
        if (value.isEmpty()) {
            showError(field, "Estoque máximo é obrigatório!");
            return false;
        }
        
        try {
            int maximo = Integer.parseInt(value);
            
            if (maximo < 0) {
                showError(field, "Estoque máximo não pode ser negativo!");
                return false;
            }
            
            // Verificar se máximo >= mínimo
            String minimoValue = minimoField.getText().trim();
            if (!minimoValue.isEmpty()) {
                try {
                    int minimo = Integer.parseInt(minimoValue);
                    if (maximo < minimo) {
                        showError(field, "Estoque máximo deve ser maior ou igual ao mínimo!");
                        return false;
                    }
                } catch (NumberFormatException e) {
                    // Ignorar se campo mínimo for inválido
                }
            }
            
            showValid(field);
            return true;
            
        } catch (NumberFormatException e) {
            showError(field, "Estoque máximo inválido! Use números inteiros.");
            return false;
        }
    }
    
    /**
     * Valida campo de busca
     */
    public static boolean validateBusca(JTextComponent field) {
        String value = field.getText().trim();
        
        if (value.length() > 50) {
            showError(field, "Busca muito longa! Máximo 50 caracteres.");
            return false;
        }
        
        showValid(field);
        return true;
    }
    
    /**
     * Valida email
     */
    public static boolean validateEmail(JTextComponent field) {
        String value = field.getText().trim();
        
        if (value.isEmpty()) {
            showError(field, "Email é obrigatório!");
            return false;
        }
        
        if (!EMAIL_PATTERN.matcher(value).matches()) {
            showError(field, "Email inválido! Use formato: usuario@exemplo.com");
            return false;
        }
        
        showValid(field);
        return true;
    }
    
    /**
     * Valida telefone
     */
    public static boolean validateTelefone(JTextComponent field) {
        String value = field.getText().trim();
        
        if (value.isEmpty()) {
            showError(field, "Telefone é obrigatório!");
            return false;
        }
        
        // Remove caracteres não numéricos
        String numeros = value.replaceAll("[^0-9]", "");
        
        if (!TELEFONE_PATTERN.matcher(numeros).matches()) {
            showError(field, "Telefone inválido! Use 10 ou 11 dígitos.");
            return false;
        }
        
        showValid(field);
        return true;
    }
    
    /**
     * Mostra feedback visual de campo válido
     */
    private static void showValid(JTextComponent field) {
        field.setBackground(COLOR_VALID);
        field.setToolTipText("");
        
        // Remove borda de erro se existir
        if (field.getBorder() instanceof javax.swing.border.LineBorder) {
            javax.swing.border.LineBorder border = (javax.swing.border.LineBorder) field.getBorder();
            if (border.getLineColor().equals(COLOR_INVALID)) {
                field.setBorder(BorderFactory.createLineBorder(Color.GRAY, 1));
            }
        }
    }
    
    /**
     * Mostra feedback visual de campo inválido
     */
    private static void showError(JTextComponent field, String message) {
        field.setBackground(COLOR_INVALID);
        field.setBorder(BorderFactory.createLineBorder(Color.RED, 2));
        field.setToolTipText(message);
        
        // Mostra mensagem de erro (opcional - pode ser removido se for muito intrusivo)
        JOptionPane.showMessageDialog(
            field.getParent(),
            message,
            "Campo Inválido",
            JOptionPane.WARNING_MESSAGE
        );
    }
    
    /**
     * Limpa validação de um campo
     */
    public static void clearValidation(JTextComponent field) {
        field.setBackground(COLOR_DEFAULT);
        field.setBorder(BorderFactory.createLineBorder(Color.GRAY, 1));
        field.setToolTipText("");
    }
    
    /**
     * Limpa validação de múltiplos campos
     */
    public static void clearValidation(JTextComponent... fields) {
        for (JTextComponent field : fields) {
            clearValidation(field);
        }
    }
    
    /**
     * Valida formulário completo de login
     */
    public static boolean validateLoginForm(JTextComponent usuarioField, JTextComponent senhaField) {
        boolean usuarioValid = validateUsuario(usuarioField);
        boolean senhaValid = validateSenha(senhaField);
        
        return usuarioValid && senhaValid;
    }
    
    /**
     * Valida formulário completo de produto
     */
    public static boolean validateProdutoForm(JTextComponent codigoField, 
                                            JTextComponent nomeField, 
                                            JTextComponent precoField,
                                            JTextComponent quantidadeField,
                                            JTextComponent minimoField,
                                            JTextComponent maximoField) {
        boolean codigoValid = validateCodigoProduto(codigoField);
        boolean nomeValid = validateNomeProduto(nomeField);
        boolean precoValid = validatePreco(precoField);
        boolean quantidadeValid = validateQuantidade(quantidadeField);
        boolean minimoValid = validateEstoqueMinimo(minimoField);
        boolean maximoValid = validateEstoqueMaximo(maximoField, minimoField);
        
        return codigoValid && nomeValid && precoValid && quantidadeValid && minimoValid && maximoValid;
    }
    
    /**
     * Adiciona validação em tempo real a um campo
     */
    public static void addRealTimeValidation(JTextComponent field, ValidationType type) {
        field.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
            @Override
            public void insertUpdate(javax.swing.event.DocumentEvent e) {
                validateField(field, type);
            }
            
            @Override
            public void removeUpdate(javax.swing.event.DocumentEvent e) {
                validateField(field, type);
            }
            
            @Override
            public void changedUpdate(javax.swing.event.DocumentEvent e) {
                validateField(field, type);
            }
        });
    }
    
    /**
     * Valida campo baseado no tipo
     */
    private static void validateField(JTextComponent field, ValidationType type) {
        switch (type) {
            case USUARIO:
                validateUsuario(field);
                break;
            case SENHA:
                validateSenha(field);
                break;
            case CODIGO_PRODUTO:
                validateCodigoProduto(field);
                break;
            case NOME_PRODUTO:
                validateNomeProduto(field);
                break;
            case PRECO:
                validatePreco(field);
                break;
            case QUANTIDADE:
                validateQuantidade(field);
                break;
            case ESTOQUE_MINIMO:
                validateEstoqueMinimo(field);
                break;
            case ESTOQUE_MAXIMO:
                validateEstoqueMaximo(field, null);
                break;
            case BUSCA:
                validateBusca(field);
                break;
            case EMAIL:
                validateEmail(field);
                break;
            case TELEFONE:
                validateTelefone(field);
                break;
        }
    }
    
    /**
     * Tipos de validação disponíveis
     */
    public enum ValidationType {
        USUARIO, SENHA, CODIGO_PRODUTO, NOME_PRODUTO, PRECO, 
        QUANTIDADE, ESTOQUE_MINIMO, ESTOQUE_MAXIMO, BUSCA, 
        EMAIL, TELEFONE
    }
}
