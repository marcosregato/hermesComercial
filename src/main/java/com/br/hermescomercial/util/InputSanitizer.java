package com.br.hermescomercial.util;

import java.util.regex.Pattern;

/**
 * Utilitário para sanitização de entrada de dados
 * Previne ataques de injeção e XSS
 */
public class InputSanitizer {
    
    // Padrões para detecção de conteúdo malicioso
    private static final Pattern SQL_INJECTION_PATTERN = Pattern.compile(
        "('(''|[^'])*')|(;)|(\b(ALTER|CREATE|DELETE|DROP|EXEC(UTE){0,1}|INSERT( +INTO){0,1}|MERGE|SELECT|UPDATE|UNION( +ALL){0,1})\b)",
        Pattern.CASE_INSENSITIVE
    );
    
    private static final Pattern XSS_PATTERN = Pattern.compile(
        "<(script|iframe|object|embed|form|input|textarea|button|link|meta|style|body|html|head)",
        Pattern.CASE_INSENSITIVE
    );
    
    private static final Pattern JAVASCRIPT_PATTERN = Pattern.compile(
        "javascript:",
        Pattern.CASE_INSENSITIVE
    );
    
    /**
     * Sanitiza string removendo caracteres perigosos
     * @param input String a ser sanitizada
     * @return String sanitizada
     */
    public static String sanitize(String input) {
        if (input == null || input.isEmpty()) {
            return input;
        }
        
        // Remover caracteres de controle
        String sanitized = input.replaceAll("[\\x00-\\x1F\\x7F]", "");
        
        // Remover tentativas de SQL injection
        sanitized = SQL_INJECTION_PATTERN.matcher(sanitized).replaceAll("");
        
        // Remover tentativas de XSS
        sanitized = XSS_PATTERN.matcher(sanitized).replaceAll("");
        
        // Remover javascript:
        sanitized = JAVASCRIPT_PATTERN.matcher(sanitized).replaceAll("");
        
        // Limitar tamanho
        if (sanitized.length() > 1000) {
            sanitized = sanitized.substring(0, 1000);
        }
        
        return sanitized.trim();
    }
    
    /**
     * Sanitiza string para uso em SQL (escapar aspas simples)
     * @param input String a ser sanitizada
     * @return String sanitizada para SQL
     */
    public static String sanitizeForSQL(String input) {
        if (input == null) {
            return null;
        }
        
        // Escapar aspas simples
        return input.replace("'", "''");
    }
    
    /**
     * Sanitiza string para uso em HTML (escapar caracteres especiais)
     * @param input String a ser sanitizada
     * @return String sanitizada para HTML
     */
    public static String sanitizeForHTML(String input) {
        if (input == null) {
            return null;
        }
        
        return input
            .replace("&", "&amp;")
            .replace("<", "&lt;")
            .replace(">", "&gt;")
            .replace("\"", "&quot;")
            .replace("'", "&#39;");
    }
    
    /**
     * Verifica se a string contém padrões suspeitos
     * @param input String a ser verificada
     * @return true se contém padrões suspeitos
     */
    public static boolean containsSuspiciousPatterns(String input) {
        if (input == null || input.isEmpty()) {
            return false;
        }
        
        return SQL_INJECTION_PATTERN.matcher(input).find() ||
               XSS_PATTERN.matcher(input).find() ||
               JAVASCRIPT_PATTERN.matcher(input).find();
    }
    
    /**
     * Sanitiza CPF/CNPJ (apenas números)
     * @param input CPF/CNPJ a ser sanitizado
     * @return CPF/CNPJ sanitizado
     */
    public static String sanitizeCPF_CNPJ(String input) {
        if (input == null) {
            return null;
        }
        
        return input.replaceAll("[^0-9]", "");
    }
    
    /**
     * Sanitiza telefone (apenas números)
     * @param input Telefone a ser sanitizado
     * @return Telefone sanitizado
     */
    public static String sanitizePhone(String input) {
        if (input == null) {
            return null;
        }
        
        return input.replaceAll("[^0-9]", "");
    }
    
    /**
     * Sanitiza email (remove caracteres perigosos mas mantém formato)
     * @param input Email a ser sanitizado
     * @return Email sanitizado
     */
    public static String sanitizeEmail(String input) {
        if (input == null) {
            return null;
        }
        
        // Remover espaços e converter para minúsculas
        String sanitized = input.trim().toLowerCase();
        
        // Remover caracteres perigosos
        sanitized = sanitized.replaceAll("[<>\"'&]", "");
        
        // Limitar tamanho
        if (sanitized.length() > 254) {
            sanitized = sanitized.substring(0, 254);
        }
        
        return sanitized;
    }
    
    /**
     * Sanitiza nome (remove caracteres especiais perigosos)
     * @param input Nome a ser sanitizado
     * @return Nome sanitizado
     */
    public static String sanitizeName(String input) {
        if (input == null) {
            return null;
        }
        
        // Remover caracteres de controle e perigosos
        String sanitized = input.replaceAll("[\\x00-\\x1F\\x7F<>\"'&]", "");
        
        // Limitar tamanho
        if (sanitized.length() > 100) {
            sanitized = sanitized.substring(0, 100);
        }
        
        return sanitized.trim();
    }
    
    /**
     * Sanitiza código de produto (apenas alfanuméricos)
     * @param input Código a ser sanitizado
     * @return Código sanitizado
     */
    public static String sanitizeProductCode(String input) {
        if (input == null) {
            return null;
        }
        
        // Apenas alfanuméricos
        String sanitized = input.replaceAll("[^A-Za-z0-9]", "");
        
        // Limitar tamanho
        if (sanitized.length() > 20) {
            sanitized = sanitized.substring(0, 20);
        }
        
        return sanitized.toUpperCase();
    }
}
