package com.br.hermescomercial.validation;

import com.br.hermescomercial.exception.ValidationException;
import com.br.hermescomercial.model.Cliente;
import com.br.hermescomercial.model.Produto;
import com.br.hermescomercial.model.Usuario;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Classe central de validação para entidades do sistema
 */
public class Validator {
    
    private static final Logger logger = LogManager.getLogger(Validator.class);
    
    /**
     * Valida um cliente
     * @param cliente Cliente a ser validado
     * @throws ValidationException Se o cliente for inválido
     */
    public static void validarCliente(Cliente cliente) {
        if (cliente == null) {
            throw new ValidationException("Cliente não pode ser nulo");
        }
        
        if (cliente.getNome() == null || cliente.getNome().trim().isEmpty()) {
            throw new ValidationException("Nome do cliente é obrigatório");
        }
        
        if (cliente.getNome().length() < 3) {
            throw new ValidationException("Nome do cliente deve ter pelo menos 3 caracteres");
        }
        
        // Validação de CPF/CNPJ se fornecido
        if (cliente.getCpf() != null && !cliente.getCpf().trim().isEmpty()) {
            if (!isValidCPF(cliente.getCpf())) {
                throw new ValidationException("CPF inválido");
            }
        }
        
        logger.debug("Cliente validado com sucesso: {}", cliente.getNome());
    }
    
    /**
     * Valida um produto
     * @param produto Produto a ser validado
     * @throws ValidationException Se o produto for inválido
     */
    public static void validarProduto(Produto produto) {
        if (produto == null) {
            throw new ValidationException("Produto não pode ser nulo");
        }
        
        if (produto.getNome() == null || produto.getNome().trim().isEmpty()) {
            throw new ValidationException("Nome do produto é obrigatório");
        }
        
        if (produto.getNome().length() < 2) {
            throw new ValidationException("Nome do produto deve ter pelo menos 2 caracteres");
        }
        
        if (produto.getPrecoVenda() == null || produto.getPrecoVenda().compareTo(java.math.BigDecimal.ZERO) <= 0) {
            throw new ValidationException("Preço de venda deve ser maior que zero");
        }
        
        if (produto.getEstoque() < 0) {
            throw new ValidationException("Estoque não pode ser negativo");
        }
        
        logger.debug("Produto validado com sucesso: {}", produto.getNome());
    }
    
    /**
     * Valida um usuário
     * @param usuario Usuário a ser validado
     * @throws ValidationException Se o usuário for inválido
     */
    public static void validarUsuario(Usuario usuario) {
        if (usuario == null) {
            throw new ValidationException("Usuário não pode ser nulo");
        }
        
        if (usuario.getNome() == null || usuario.getNome().trim().isEmpty()) {
            throw new ValidationException("Nome do usuário é obrigatório");
        }
        
        if (usuario.getNome().length() < 3) {
            throw new ValidationException("Nome do usuário deve ter pelo menos 3 caracteres");
        }
        
        logger.debug("Usuário validado com sucesso: {}", usuario.getNome());
    }
    
    /**
     * Valida se uma string não é nula ou vazia
     * @param valor String a ser validada
     * @param nomeCampo Nome do campo para mensagem de erro
     * @throws ValidationException Se a string for inválida
     */
    public static void validarStringNaoVazia(String valor, String nomeCampo) {
        if (valor == null || valor.trim().isEmpty()) {
            throw new ValidationException(nomeCampo + " é obrigatório");
        }
    }
    
    /**
     * Valida se um número é positivo
     * @param valor Número a ser validado
     * @param nomeCampo Nome do campo para mensagem de erro
     * @throws ValidationException Se o número for inválido
     */
    public static void validarNumeroPositivo(Number valor, String nomeCampo) {
        if (valor == null || valor.doubleValue() <= 0) {
            throw new ValidationException(nomeCampo + " deve ser maior que zero");
        }
    }
    
    /**
     * Validação simples de CPF (aplicação básica)
     * @param cpf CPF a ser validado
     * @return true se CPF parece válido
     */
    private static boolean isValidCPF(String cpf) {
        if (cpf == null || cpf.length() != 11) {
            return false;
        }
        
        // Remove caracteres não numéricos
        cpf = cpf.replaceAll("[^0-9]", "");
        
        // Verifica se todos os dígitos são iguais
        if (cpf.matches("(\\d)\\1{10}")) {
            return false;
        }
        
        // Validação básica - poderia ser implementada validação completa
        return cpf.length() == 11;
    }
}
