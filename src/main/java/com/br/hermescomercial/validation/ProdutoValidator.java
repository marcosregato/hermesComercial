package com.br.hermescomercial.validation;

import com.br.hermescomercial.dto.ProdutoDTO;
import com.br.hermescomercial.exception.BusinessException;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * Validador para Produto
 * Versão 2.0.0 - Validation Pattern Implementation
 */
public class ProdutoValidator {
    
    /**
     * Valida um produto completo
     */
    public void validar(ProdutoDTO produto) throws BusinessException {
        List<String> erros = new ArrayList<>();
        
        // Validação do nome
        if (produto.getNome() == null || produto.getNome().trim().isEmpty()) {
            erros.add("Nome do produto é obrigatório");
        } else if (produto.getNome().trim().length() < 3) {
            erros.add("Nome do produto deve ter pelo menos 3 caracteres");
        } else if (produto.getNome().trim().length() > 100) {
            erros.add("Nome do produto não pode ter mais de 100 caracteres");
        }
        
        // Validação da categoria
        if (produto.getCategoria() == null || produto.getCategoria().trim().isEmpty()) {
            erros.add("Categoria do produto é obrigatória");
        }
        
        // Validação do preço
        if (produto.getPreco() == null) {
            erros.add("Preço do produto é obrigatório");
        } else if (produto.getPreco().compareTo(BigDecimal.ZERO) <= 0) {
            erros.add("Preço do produto deve ser maior que zero");
        } else if (produto.getPreco().compareTo(new BigDecimal("999999.99")) > 0) {
            erros.add("Preço do produto não pode ser maior que R$ 999.999,99");
        }
        
        // Validação do estoque
        if (produto.getEstoque() == null) {
            erros.add("Estoque do produto é obrigatório");
        } else if (produto.getEstoque() < 0) {
            erros.add("Estoque do produto não pode ser negativo");
        } else if (produto.getEstoque() > 999999) {
            erros.add("Estoque do produto não pode ser maior que 999.999");
        }
        
        // Validação da unidade
        if (produto.getUnidade() == null || produto.getUnidade().trim().isEmpty()) {
            erros.add("Unidade do produto é obrigatória");
        }
        
        // Validação do status
        if (produto.getStatus() == null || produto.getStatus().trim().isEmpty()) {
            erros.add("Status do produto é obrigatório");
        } else {
            String[] statusValidos = {"Ativo", "Inativo", "Descontinuado"};
            boolean statusValido = false;
            for (String status : statusValidos) {
                if (status.equals(produto.getStatus())) {
                    statusValido = true;
                    break;
                }
            }
            if (!statusValido) {
                erros.add("Status do produto deve ser: Ativo, Inativo ou Descontinuado");
            }
        }
        
        // Validação do código (se informado)
        if (produto.getCodigo() != null && !produto.getCodigo().trim().isEmpty()) {
            if (produto.getCodigo().trim().length() > 20) {
                erros.add("Código do produto não pode ter mais de 20 caracteres");
            }
        }
        
        // Validação da descrição (se informada)
        if (produto.getDescricao() != null && !produto.getDescricao().trim().isEmpty()) {
            if (produto.getDescricao().trim().length() > 500) {
                erros.add("Descrição do produto não pode ter mais de 500 caracteres");
            }
        }
        
        if (!erros.isEmpty()) {
            throw new BusinessException(String.join("\n", erros));
        }
    }
    
    /**
     * Valida apenas campos obrigatórios
     */
    public void validarCamposObrigatorios(ProdutoDTO produto) throws BusinessException {
        List<String> erros = new ArrayList<>();
        
        if (produto.getNome() == null || produto.getNome().trim().isEmpty()) {
            erros.add("Nome do produto é obrigatório");
        }
        
        if (produto.getCategoria() == null || produto.getCategoria().trim().isEmpty()) {
            erros.add("Categoria do produto é obrigatória");
        }
        
        if (produto.getPreco() == null) {
            erros.add("Preço do produto é obrigatório");
        }
        
        if (produto.getEstoque() == null) {
            erros.add("Estoque do produto é obrigatório");
        }
        
        if (!erros.isEmpty()) {
            throw new BusinessException(String.join("\n", erros));
        }
    }
    
    /**
     * Valida regras de negócio específicas
     */
    public void validarRegrasNegocio(ProdutoDTO produto) throws BusinessException {
        List<String> erros = new ArrayList<>();
        
        // Regra: Produtos inativos não podem ter estoque positivo
        if ("Inativo".equals(produto.getStatus()) && produto.getEstoque() > 0) {
            erros.add("Produtos inativos não podem ter estoque positivo");
        }
        
        // Regra: Produtos descontinuados devem ter estoque zero
        if ("Descontinuado".equals(produto.getStatus()) && produto.getEstoque() > 0) {
            erros.add("Produtos descontinuados devem ter estoque zero");
        }
        
        // Regra: Preço de custo não pode ser maior que preço de venda
        // (Esta regra seria implementada se tivéssemos preço de custo no DTO)
        
        if (!erros.isEmpty()) {
            throw new BusinessException(String.join("\n", erros));
        }
    }
}
