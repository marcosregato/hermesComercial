package com.br.hermescomercial.shared.validator;

import com.br.hermescomercial.model.Produto;
import com.br.hermescomercial.repository.ProdutoRepository;
import com.br.hermescomercial.repository.impl.ProdutoRepositoryImpl;

import java.math.BigDecimal;
import java.util.List;

/**
 * Validador de produtos para regras de negócio
 * Versão 1.0.0 - Design Pattern Implementation
 */
public class ProdutoValidator {
    
    private final ProdutoRepository produtoRepository;
    
    public ProdutoValidator() {
        this.produtoRepository = new ProdutoRepositoryImpl();
    }
    
    /**
     * Valida dados básicos do produto
     * @param produto Produto a ser validado
     * @throws BusinessException Se houver erro de validação
     */
    public void validarProduto(Produto produto) throws BusinessException {
        if (produto == null) {
            throw new BusinessException("Produto não pode ser nulo");
        }
        
        if (produto.getNome() == null || produto.getNome().trim().isEmpty()) {
            throw new BusinessException("Nome do produto é obrigatório");
        }
        
        if (produto.getNome().length() > 100) {
            throw new BusinessException("Nome do produto não pode exceder 100 caracteres");
        }
        
        if (produto.getPrecoVenda() == null || produto.getPrecoVenda().compareTo(BigDecimal.ZERO) <= 0) {
            throw new BusinessException("Preço de venda deve ser maior que zero");
        }
        
        if (produto.getEstoque() < 0) {
            throw new BusinessException("Estoque não pode ser negativo");
        }
        
        if (produto.getEstoqueMinimo() < 0) {
            throw new BusinessException("Estoque mínimo não pode ser negativo");
        }
        
        if (produto.getPrecoCusto() != null && produto.getPrecoCusto().compareTo(BigDecimal.ZERO) < 0) {
            throw new BusinessException("Preço de custo não pode ser negativo");
        }
    }
    
    /**
     * Valida se o nome do produto é único
     * @param nome Nome a ser validado
     * @param id ID do produto (null para novos produtos)
     * @throws BusinessException Se nome já existir
     */
    public void validarNomeUnico(String nome, Long id) throws BusinessException {
        List<Produto> produtos = produtoRepository.buscarPorNome(nome);
        
        for (Produto p : produtos) {
            if (id == null || !p.getId().equals(id)) {
                throw new BusinessException("Já existe um produto com este nome: " + nome);
            }
        }
    }
    
    /**
     * Valida se o código de barras é único
     * @param codigoBarras Código de barras a ser validado
     * @param id ID do produto (null para novos produtos)
     * @throws BusinessException Se código já existir
     */
    public void validarCodigoBarrasUnico(String codigoBarras, Long id) throws BusinessException {
        if (codigoBarras == null || codigoBarras.trim().isEmpty()) {
            return; // Código de barras não é obrigatório
        }
        
        // Simulação - na implementação real, buscaria por código de barras
        // Por enquanto, apenas valida formato
        if (codigoBarras.length() < 8 || codigoBarras.length() > 20) {
            throw new BusinessException("Código de barras deve ter entre 8 e 20 caracteres");
        }
        
        // Verificar se contém apenas números
        if (!codigoBarras.matches("[0-9]+")) {
            throw new BusinessException("Código de barras deve conter apenas números");
        }
    }
    
    /**
     * Valida preços de custo e venda
     * @param precoCusto Preço de custo
     * @param precoVenda Preço de venda
     * @throws BusinessException Se preços forem inválidos
     */
    public void validarPreco(BigDecimal precoCusto, BigDecimal precoVenda) throws BusinessException {
        if (precoVenda == null || precoVenda.compareTo(BigDecimal.ZERO) <= 0) {
            throw new BusinessException("Preço de venda deve ser maior que zero");
        }
        
        if (precoCusto != null && precoCusto.compareTo(BigDecimal.ZERO) < 0) {
            throw new BusinessException("Preço de custo não pode ser negativo");
        }
        
        if (precoCusto != null && precoCusto.compareTo(precoVenda) >= 0) {
            throw new BusinessException("Preço de custo deve ser menor que preço de venda");
        }
    }
    
    /**
     * Valida estoque e estoque mínimo
     * @param estoque Estoque atual
     * @param estoqueMinimo Estoque mínimo
     * @throws BusinessException Se valores forem inválidos
     */
    public void validarEstoque(int estoque, int estoqueMinimo) throws BusinessException {
        if (estoque < 0) {
            throw new BusinessException("Estoque não pode ser negativo");
        }
        
        if (estoqueMinimo < 0) {
            throw new BusinessException("Estoque mínimo não pode ser negativo");
        }
        
        if (estoqueMinimo > estoque) {
            throw new BusinessException("Estoque mínimo não pode ser maior que estoque atual");
        }
    }
    
    /**
     * Exceção de validação de negócio
     */
    public static class BusinessException extends Exception {
        public BusinessException(String message) {
            super(message);
        }
        
        public BusinessException(String message, Throwable cause) {
            super(message, cause);
        }
    }
}
