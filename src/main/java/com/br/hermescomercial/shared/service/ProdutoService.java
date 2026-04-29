package com.br.hermescomercial.shared.service;

import com.br.hermescomercial.dao.ProdutoDao;
import com.br.hermescomercial.model.Produto;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.SQLException;
import java.util.List;

/**
 * Serviço para gerenciamento de produtos
 * Implementa a lógica de negócio para produtos
 * @author marcos
 */
public class ProdutoService {
    
    private static final Logger logger = LogManager.getLogger(ProdutoService.class);
    
    private final ProdutoDao produtoDao;
    
    public ProdutoService() {
        this.produtoDao = new ProdutoDao();
    }
    
    public ProdutoService(ProdutoDao produtoDao) {
        this.produtoDao = produtoDao;
    }
    
    /**
     * Salva um novo produto
     * @param produto Produto a ser salvo
     * @return true se salvo com sucesso
     */
    public boolean salvar(Produto produto) {
        try {
            validarProduto(produto);
            return produtoDao.salvar(produto);
        } catch (SQLException e) {
            logger.error("Erro ao salvar produto: " + e.getMessage(), e);
            throw new RuntimeException("Não foi possível salvar o produto", e);
        }
    }
    
    /**
     * Remove um produto pelo nome
     * @param nome Nome do produto a ser removido
     * @return true se removido com sucesso
     */
    public boolean remover(String nome) {
        try {
            if (nome == null || nome.trim().isEmpty()) {
                throw new IllegalArgumentException("Nome do produto não pode ser nulo ou vazio");
            }
            return produtoDao.remove(nome);
        } catch (Exception e) {
            logger.error("Erro ao remover produto: " + e.getMessage(), e);
            throw new RuntimeException("Não foi possível remover o produto", e);
        }
    }
    
    /**
     * Atualiza um produto existente
     * @param produto Produto com dados atualizados
     * @return true se atualizado com sucesso
     */
    public boolean atualizar(Produto produto) {
        try {
            validarProduto(produto);
            return produtoDao.update(produto);
        } catch (Exception e) {
            logger.error("Erro ao atualizar produto: " + e.getMessage(), e);
            throw new RuntimeException("Não foi possível atualizar o produto", e);
        }
    }
    
    /**
     * Busca produtos por nome
     * @param nome Termo de busca
     * @return Lista de produtos encontrados
     */
    public List<Produto> buscarPorNome(String nome) {
        try {
            if (nome == null) {
                return listar();
            }
            return produtoDao.buscar(nome);
        } catch (Exception e) {
            logger.error("Erro ao buscar produtos: " + e.getMessage(), e);
            throw new RuntimeException("Não foi possível buscar produtos", e);
        }
    }
    
    /**
     * Lista todos os produtos
     * @return Lista de todos os produtos
     */
    public List<Produto> listar() {
        try {
            return produtoDao.listar();
        } catch (Exception e) {
            logger.error("Erro ao listar produtos: " + e.getMessage(), e);
            throw new RuntimeException("Não foi possível listar produtos", e);
        }
    }
    
    /**
     * Busca produtos com filtros avançados
     * @param nome Filtro por nome
     * @param categoria Filtro por categoria
     * @param codigo Filtro por código
     * @param precoMin Preço mínimo
     * @param precoMax Preço máximo
     * @param estoqueMin Estoque mínimo
     * @param ativos Incluir apenas ativos
     * @param inativos Incluir apenas inativos
     * @return Lista de produtos filtrados
     */
    public List<Produto> buscarComFiltros(String nome, String categoria, String subCategoria, 
                                         String codigo, java.math.BigDecimal precoMin, 
                                         java.math.BigDecimal precoMax, Integer estoqueMin, 
                                         boolean ativos, boolean inativos) {
        try {
            return produtoDao.buscarComFiltros(nome, categoria, subCategoria, codigo, 
                                               precoMin, precoMax, estoqueMin, 
                                               ativos, inativos);
        } catch (Exception e) {
            logger.error("Erro ao buscar produtos com filtros: " + e.getMessage(), e);
            throw new RuntimeException("Não foi possível buscar produtos com filtros", e);
        }
    }
    
    /**
     * Busca produto por código de barras
     * @param codigoBarras Código de barras do produto
     * @return Produto encontrado ou null
     */
    public Produto buscarPorCodigoBarras(String codigoBarras) {
        try {
            if (codigoBarras == null || codigoBarras.trim().isEmpty()) {
                return null;
            }
            return produtoDao.buscarPorCodigoBarras(codigoBarras);
        } catch (Exception e) {
            logger.error("Erro ao buscar produto por código de barras: " + e.getMessage(), e);
            throw new RuntimeException("Não foi possível buscar produto por código de barras", e);
        }
    }
    
    /**
     * Valida os dados do produto antes de salvar/atualizar
     * @param produto Produto a ser validado
     */
    private void validarProduto(Produto produto) {
        if (produto == null) {
            throw new IllegalArgumentException("Produto não pode ser nulo");
        }
        
        if (produto.getNome() == null || produto.getNome().trim().isEmpty()) {
            throw new IllegalArgumentException("Nome do produto é obrigatório");
        }
        
        if (produto.getPrecoVenda() != null && produto.getPrecoVenda().doubleValue() < 0) {
            throw new IllegalArgumentException("Preço de venda não pode ser negativo");
        }
        
        if (produto.getEstoque() < 0) {
            throw new IllegalArgumentException("Estoque não pode ser negativo");
        }
        
        logger.debug("Produto validado com sucesso: " + produto.getNome());
    }
    
    /**
     * Verifica se produto existe pelo nome
     * @param nome Nome do produto
     * @return true se produto existe
     */
    public boolean existePorNome(String nome) {
        try {
            List<Produto> produtos = buscarPorNome(nome);
            return produtos.stream().anyMatch(p -> p.getNome().equalsIgnoreCase(nome));
        } catch (Exception e) {
            logger.error("Erro ao verificar existência do produto: " + e.getMessage(), e);
            return false;
        }
    }
    
    /**
     * Verifica se produto existe pelo código
     * @param codigo Código do produto
     * @return true se produto existe
     */
    public boolean existePorCodigo(String codigo) {
        try {
            Produto produto = buscarPorCodigoBarras(codigo);
            return produto != null;
        } catch (Exception e) {
            logger.error("Erro ao verificar existência do produto por código: " + e.getMessage(), e);
            return false;
        }
    }
    
    /**
     * Atualiza estoque de um produto
     * @param codigo Código do produto
     * @param quantidade Quantidade a ser adicionada (positiva) ou removida (negativa)
     * @return true se atualizado com sucesso
     */
    public boolean atualizarEstoque(String codigo, int quantidade) {
        try {
            Produto produto = buscarPorCodigoBarras(codigo);
            if (produto == null) {
                throw new IllegalArgumentException("Produto não encontrado: " + codigo);
            }
            
            int novoEstoque = produto.getEstoque() + quantidade;
            if (novoEstoque < 0) {
                throw new IllegalArgumentException("Estoque insuficiente para o produto: " + codigo);
            }
            
            produto.setEstoque(novoEstoque);
            return atualizar(produto);
            
        } catch (Exception e) {
            logger.error("Erro ao atualizar estoque: " + e.getMessage(), e);
            throw new RuntimeException("Não foi possível atualizar estoque", e);
        }
    }
}
