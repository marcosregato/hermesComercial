package com.br.hermescomercial.repository;

import com.br.hermescomercial.dao.ProdutoDao;
import com.br.hermescomercial.model.Produto;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;

/**
 * Implementação do ProdutoRepository usando ProdutoDao
 * @author marcos
 */
public class ProdutoRepositoryImpl implements ProdutoRepository {
    
    private static final Logger logger = LogManager.getLogger(ProdutoRepositoryImpl.class);
    private final ProdutoDao produtoDao;
    
    public ProdutoRepositoryImpl() {
        this.produtoDao = new ProdutoDao();
    }
    
    @Override
    public boolean salvar(Produto produto) {
        try {
            return produtoDao.salvar(produto);
        } catch (Exception e) {
            logger.error("Erro ao salvar produto no repository: {}", e.getMessage(), e);
            return false;
        }
    }
    
    @Override
    public boolean atualizar(Produto produto) {
        try {
            return produtoDao.update(produto);
        } catch (Exception e) {
            logger.error("Erro ao atualizar produto no repository: {}", e.getMessage(), e);
            return false;
        }
    }
    
    @Override
    public boolean excluir(Long id) {
        try {
            return produtoDao.remove(id.toString());
        } catch (Exception e) {
            logger.error("Erro ao excluir produto no repository: {}", e.getMessage(), e);
            return false;
        }
    }
    
    @Override
    public boolean excluir(String nome) {
        try {
            return produtoDao.remove(nome);
        } catch (Exception e) {
            logger.error("Erro ao excluir produto no repository: {}", e.getMessage(), e);
            return false;
        }
    }
    
    @Override
    public Produto buscarPorId(Long id) {
        try {
            List<Produto> produtos = produtoDao.buscar(id.toString());
            return produtos.isEmpty() ? null : produtos.get(0);
        } catch (Exception e) {
            logger.error("Erro ao buscar produto por ID no repository: {}", e.getMessage(), e);
            return null;
        }
    }
    
    @Override
    public Produto buscarPorCodigoBarras(String codigoBarras) {
        try {
            return produtoDao.buscarPorCodigoBarras(codigoBarras);
        } catch (Exception e) {
            logger.error("Erro ao buscar produto por código de barras no repository: {}", e.getMessage(), e);
            return null;
        }
    }
    
    @Override
    public List<Produto> listar() {
        try {
            return produtoDao.listar();
        } catch (Exception e) {
            logger.error("Erro ao listar produtos no repository: {}", e.getMessage(), e);
            return List.of();
        }
    }
    
    @Override
    public List<Produto> buscarComFiltros(String nome, String categoria, String subCategoria, 
                                         String codigoBarras, java.math.BigDecimal precoMin, 
                                         java.math.BigDecimal precoMax, Integer estoqueMin, 
                                         boolean ativos, boolean inativos) {
        try {
            return produtoDao.buscarComFiltros(nome, categoria, subCategoria, codigoBarras, 
                                             precoMin, precoMax, estoqueMin, 
                                             ativos, inativos);
        } catch (Exception e) {
            logger.error("Erro ao buscar produtos com filtros no repository: {}", e.getMessage(), e);
            return List.of();
        }
    }
}
