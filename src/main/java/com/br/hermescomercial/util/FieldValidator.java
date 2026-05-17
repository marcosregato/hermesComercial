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
    private static final Pattern DATA_PATTERN = Pattern.compile("^\\d{2}/\\d{2}/\\d{4}$");
    
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
        String value;
        if (field instanceof JPasswordField) {
            value = new String(((JPasswordField) field).getPassword()).trim();
        } else {
            value = field.getText().trim();
        }
        
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
            case CPF_CNPJ:
                validateCpfCnpj(field);
                break;
            case DATA:
                validateData(field);
                break;
            case CODIGO_VENDA:
                validateCodigoVenda(field);
                break;
            case QUANTIDADE_VENDA:
                validateQuantidadeVenda(field);
                break;
            case NOME_CLIENTE:
                validateNomeCliente(field);
                break;
        }
    }
    
    /**
     * Valida CPF/CNPJ
     */
    public static boolean validateCpfCnpj(JTextComponent field) {
        String value = field.getText().trim();
        
        if (value.isEmpty()) {
            showError(field, "CPF/CNPJ é obrigatório!");
            return false;
        }
        
        // Remove caracteres não numéricos
        String numeros = value.replaceAll("[^0-9]", "");
        
        if (numeros.length() == 11) {
            // Validar CPF
            if (!isValidCPF(numeros)) {
                showError(field, "CPF inválido!");
                return false;
            }
        } else if (numeros.length() == 14) {
            // Validar CNPJ
            if (!isValidCNPJ(numeros)) {
                showError(field, "CNPJ inválido!");
                return false;
            }
        } else {
            showError(field, "CPF deve ter 11 dígitos ou CNPJ deve ter 14 dígitos!");
            return false;
        }
        
        showValid(field);
        return true;
    }
    
    /**
     * Valida data no formato dd/MM/yyyy
     */
    public static boolean validateData(JTextComponent field) {
        String value = field.getText().trim();
        
        if (value.isEmpty()) {
            showError(field, "Data é obrigatória!");
            return false;
        }
        
        if (!DATA_PATTERN.matcher(value).matches()) {
            showError(field, "Data inválida! Use formato: dd/MM/yyyy");
            return false;
        }
        
        try {
            String[] partes = value.split("/");
            int dia = Integer.parseInt(partes[0]);
            int mes = Integer.parseInt(partes[1]);
            int ano = Integer.parseInt(partes[2]);
            
            // Validação básica
            if (dia < 1 || dia > 31) {
                showError(field, "Dia inválido! Use 1-31");
                return false;
            }
            
            if (mes < 1 || mes > 12) {
                showError(field, "Mês inválido! Use 1-12");
                return false;
            }
            
            if (ano < 1900 || ano > 2100) {
                showError(field, "Ano inválido! Use 1900-2100");
                return false;
            }
            
            // Validação de dias por mês
            if (mes == 2) {
                boolean bissexto = (ano % 4 == 0 && ano % 100 != 0) || (ano % 400 == 0);
                int maxDias = bissexto ? 29 : 28;
                if (dia > maxDias) {
                    showError(field, "Fevereiro tem apenas " + maxDias + " dias!");
                    return false;
                }
            } else if ((mes == 4 || mes == 6 || mes == 9 || mes == 11) && dia > 30) {
                showError(field, "Este mês tem apenas 30 dias!");
                return false;
            }
            
        } catch (Exception e) {
            showError(field, "Data inválida! Use formato: dd/MM/yyyy");
            return false;
        }
        
        showValid(field);
        return true;
    }
    
    /**
     * Valida quantidade para vendas
     */
    public static boolean validateQuantidadeVenda(JTextComponent field) {
        String value = field.getText().trim();
        
        if (value.isEmpty()) {
            showError(field, "Quantidade é obrigatória!");
            return false;
        }
        
        try {
            int quantidade = Integer.parseInt(value);
            
            if (quantidade <= 0) {
                showError(field, "Quantidade deve ser maior que zero!");
                return false;
            }
            
            if (quantidade > 999) {
                showError(field, "Quantidade muito alta! Máximo: 999");
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
     * Valida código de produto para vendas
     */
    public static boolean validateCodigoVenda(JTextComponent field) {
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
     * Valida nome de cliente
     */
    public static boolean validateNomeCliente(JTextComponent field) {
        String value = field.getText().trim();
        
        if (value.isEmpty()) {
            showError(field, "Nome do cliente é obrigatório!");
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
     * Valida período de datas (data início <= data fim)
     */
    public static boolean validatePeriodoDatas(JTextComponent dataInicioField, JTextComponent dataFimField) {
        boolean dataInicioValid = validateData(dataInicioField);
        boolean dataFimValid = validateData(dataFimField);
        
        if (!dataInicioValid || !dataFimValid) {
            return false;
        }
        
        try {
            String inicioStr = dataInicioField.getText().trim();
            String fimStr = dataFimField.getText().trim();
            
            java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("dd/MM/yyyy");
            java.util.Date dataInicio = sdf.parse(inicioStr);
            java.util.Date dataFim = sdf.parse(fimStr);
            
            if (dataInicio.after(dataFim)) {
                showError(dataFimField, "Data fim deve ser maior ou igual à data início!");
                return false;
            }
            
            // Validar período máximo (1 ano)
            long diffMillis = dataFim.getTime() - dataInicio.getTime();
            long diffDays = diffMillis / (1000 * 60 * 60 * 24);
            
            if (diffDays > 365) {
                showError(dataFimField, "Período muito longo! Máximo: 365 dias");
                return false;
            }
            
        } catch (Exception e) {
            showError(dataInicioField, "Erro ao validar datas!");
            return false;
        }
        
        showValid(dataInicioField);
        showValid(dataFimField);
        return true;
    }
    
    /**
     * Valida formulário completo de vendas
     */
    public static boolean validateVendaForm(JTextComponent codigoField, JTextComponent quantidadeField) {
        boolean codigoValid = validateCodigoVenda(codigoField);
        boolean quantidadeValid = validateQuantidadeVenda(quantidadeField);
        
        return codigoValid && quantidadeValid;
    }
    
    /**
     * Valida formulário completo de cliente
     */
    public static boolean validateClienteForm(JTextComponent nomeField, JTextComponent cpfCnpjField, 
                                            JTextComponent telefoneField, JTextComponent emailField) {
        boolean nomeValid = validateNomeCliente(nomeField);
        boolean cpfCnpjValid = validateCpfCnpj(cpfCnpjField);
        boolean telefoneValid = validateTelefone(telefoneField);
        boolean emailValid = validateEmail(emailField);
        
        return nomeValid && cpfCnpjValid && telefoneValid && emailValid;
    }
    
    /**
     * Valida CPF (algoritmo de validação)
     */
    private static boolean isValidCPF(String cpf) {
        if (cpf.length() != 11) return false;
        
        // Verificar se todos os dígitos são iguais
        if (cpf.matches("(\\d)\\1{10}")) return false;
        
        try {
            // Primeiro dígito verificador
            int soma = 0;
            for (int i = 0; i < 9; i++) {
                soma += Character.getNumericValue(cpf.charAt(i)) * (10 - i);
            }
            int resto = soma % 11;
            int digito1 = resto < 2 ? 0 : 11 - resto;
            
            if (digito1 != Character.getNumericValue(cpf.charAt(9))) return false;
            
            // Segundo dígito verificador
            soma = 0;
            for (int i = 0; i < 10; i++) {
                soma += Character.getNumericValue(cpf.charAt(i)) * (11 - i);
            }
            resto = soma % 11;
            int digito2 = resto < 2 ? 0 : 11 - resto;
            
            return digito2 == Character.getNumericValue(cpf.charAt(10));
            
        } catch (Exception e) {
            return false;
        }
    }
    
    /**
     * Valida CNPJ (algoritmo de validação)
     */
    private static boolean isValidCNPJ(String cnpj) {
        if (cnpj.length() != 14) return false;
        
        // Verificar se todos os dígitos são iguais
        if (cnpj.matches("(\\d)\\1{13}")) return false;
        
        try {
            // Primeiro dígito verificador
            int soma = 0;
            int[] peso1 = {5, 4, 3, 2, 9, 8, 7, 6, 5, 4, 3, 2};
            for (int i = 0; i < 12; i++) {
                soma += Character.getNumericValue(cnpj.charAt(i)) * peso1[i];
            }
            int resto = soma % 11;
            int digito1 = resto < 2 ? 0 : 11 - resto;
            
            if (digito1 != Character.getNumericValue(cnpj.charAt(12))) return false;
            
            // Segundo dígito verificador
            soma = 0;
            int[] peso2 = {6, 5, 4, 3, 2, 9, 8, 7, 6, 5, 4, 3, 2};
            for (int i = 0; i < 13; i++) {
                soma += Character.getNumericValue(cnpj.charAt(i)) * peso2[i];
            }
            resto = soma % 11;
            int digito2 = resto < 2 ? 0 : 11 - resto;
            
            return digito2 == Character.getNumericValue(cnpj.charAt(13));
            
        } catch (Exception e) {
            return false;
        }
    }
    
    /**
     * Tipos de validação disponíveis
     */
    public enum ValidationType {
        USUARIO, SENHA, CODIGO_PRODUTO, NOME_PRODUTO, PRECO, 
        QUANTIDADE, ESTOQUE_MINIMO, ESTOQUE_MAXIMO, BUSCA, 
        EMAIL, TELEFONE, CPF_CNPJ, DATA, CODIGO_VENDA, QUANTIDADE_VENDA, NOME_CLIENTE
    }
}
