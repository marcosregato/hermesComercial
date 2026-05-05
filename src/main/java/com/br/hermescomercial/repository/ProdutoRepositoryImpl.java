package com.br.hermescomercial.repository;

import com.br.hermescomercial.dao.ProdutoDao;
import com.br.hermescomercial.model.Produto;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;
import java.math.BigDecimal;

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
    public boolean remover(Long id) {
        try {
            return produtoDao.remove(id.toString());
        } catch (Exception e) {
            logger.error("Erro ao remover produto no repository: {}", e.getMessage(), e);
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
    public java.util.Optional<Produto> buscarPorId(Long id) {
        try {
            List<Produto> produtos = produtoDao.buscar(id.toString());
            return produtos.isEmpty() ? java.util.Optional.empty() : java.util.Optional.of(produtos.get(0));
        } catch (Exception e) {
            logger.error("Erro ao buscar produto por ID no repository: {}", e.getMessage(), e);
            return java.util.Optional.empty();
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
                                              precoMin, precoMax, estoqueMin, ativos, inativos);
        } catch (Exception e) {
            logger.error("Erro ao buscar produtos com filtros no repository: {}", e.getMessage(), e);
            return List.of();
        }
    }
    
    @Override
    public List<Produto> buscarTodos() {
        try {
            return produtoDao.listar();
        } catch (Exception e) {
            logger.error("Erro ao listar todos os produtos no repository: {}", e.getMessage(), e);
            return List.of();
        }
    }
    
    @Override
    public List<Produto> buscarPorNome(String nome) {
        try {
            List<Produto> todos = produtoDao.listar();
            return todos.stream()
                .filter(p -> p.getNome() != null && p.getNome().toLowerCase().contains(nome.toLowerCase()))
                .collect(java.util.stream.Collectors.toList());
        } catch (Exception e) {
            logger.error("Erro ao buscar produtos por nome no repository: {}", e.getMessage(), e);
            return List.of();
        }
    }
    
    @Override
    public List<Produto> buscarPorCategoria(String categoria) {
        try {
            List<Produto> todos = produtoDao.listar();
            return todos.stream()
                .filter(p -> p.getCategoria() != null && p.getCategoria().equalsIgnoreCase(categoria))
                .collect(java.util.stream.Collectors.toList());
        } catch (Exception e) {
            logger.error("Erro ao buscar produtos por categoria no repository: {}", e.getMessage(), e);
            return List.of();
        }
    }
    
    @Override
    public List<Produto> buscarEstoqueBaixo() {
        try {
            List<Produto> todos = produtoDao.listar();
            return todos.stream()
                .filter(p -> p.getEstoque() <= p.getEstoqueMinimo())
                .collect(java.util.stream.Collectors.toList());
        } catch (Exception e) {
            logger.error("Erro ao buscar produtos com estoque baixo no repository: {}", e.getMessage(), e);
            return List.of();
        }
    }
    
    @Override
    public boolean atualizarEstoque(Long id, int novaQuantidade) {
        try {
            List<Produto> produtos = produtoDao.buscar(id.toString());
            if (!produtos.isEmpty()) {
                Produto produto = produtos.get(0);
                produto.setEstoque(novaQuantidade);
                return produtoDao.update(produto);
            }
            return false;
        } catch (Exception e) {
            logger.error("Erro ao atualizar estoque no repository: {}", e.getMessage(), e);
            return false;
        }
    }
    
    @Override
    public List<Produto> buscarPorFaixaPreco(BigDecimal precoMin, BigDecimal precoMax) {
        try {
            List<Produto> produtos = produtoDao.listar();
            return produtos.stream()
                .filter(p -> p.getPrecoVenda() != null && 
                        p.getPrecoVenda().compareTo(precoMin) >= 0 && 
                        p.getPrecoVenda().compareTo(precoMax) <= 0)
                .collect(java.util.stream.Collectors.toList());
        } catch (Exception e) {
            logger.error("Erro ao buscar produtos por faixa de preço no repository: {}", e.getMessage(), e);
            return List.of();
        }
    }
    
    @Override
    public List<Produto> buscarComEstoqueBaixo(int limite) {
        try {
            List<Produto> produtos = produtoDao.listar();
            return produtos.stream()
                .filter(p -> p.getEstoque() < limite)
                .collect(java.util.stream.Collectors.toList());
        } catch (Exception e) {
            logger.error("Erro ao buscar produtos com estoque baixo no repository: {}", e.getMessage(), e);
            return List.of();
        }
    }
    
    @Override
    public List<Produto> buscarPorCodigo(String codigo) {
        try {
            List<Produto> produtos = produtoDao.listar();
            return produtos.stream()
                .filter(p -> p.getCodigoBarras() != null && p.getCodigoBarras().equals(codigo))
                .collect(java.util.stream.Collectors.toList());
        } catch (Exception e) {
            logger.error("Erro ao buscar produtos por código no repository: {}", e.getMessage(), e);
            return List.of();
        }
    }
}
