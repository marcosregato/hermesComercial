package com.br.hermescomercial.dao;

import com.br.hermescomercial.model.Produto;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.List;

/**
 * Adaptador para fazer ProdutoExcelDao funcionar como ProdutoDao
 * @author marcos
 */
public class ProdutoDaoAdapter extends ProdutoDao {
    private static final Logger logger = LogManager.getLogger(ProdutoDaoAdapter.class);
    private final ProdutoExcelDao excelDao;
    
    public ProdutoDaoAdapter(ProdutoExcelDao excelDao) {
        this.excelDao = excelDao;
    }
    
    @Override
    public boolean salvar(Produto produto) throws SQLException {
        logger.info("Salvando produto via Excel: " + produto.getNome());
        return excelDao.salvar(produto);
    }
    
    @Override
    public boolean remove(String nome) throws SQLException {
        logger.info("Removendo produto via Excel: " + nome);
        return excelDao.remove(nome);
    }
    
    @Override
    public boolean update(Produto produto) throws SQLException {
        logger.info("Atualizando produto via Excel: " + produto.getNome());
        return excelDao.update(produto);
    }
    
    @Override
    public List<Produto> buscar(String nome) throws SQLException {
        logger.info("Buscando produtos via Excel: " + nome);
        return excelDao.buscar(nome);
    }
    
    @Override
    public List<Produto> listar() {
        logger.info("Listando produtos via Excel");
        return excelDao.listar();
    }
    
    @Override
    public List<Produto> buscarComFiltros(String nome, String categoria, String subCategoria, 
                                         String codigoBarras, BigDecimal precoMin, BigDecimal precoMax, 
                                         Integer estoqueMin, boolean ativos, boolean inativos) {
        logger.info("Buscando produtos com filtros via Excel");
        return excelDao.buscarComFiltros(nome, categoria, subCategoria, codigoBarras, 
                                       precoMin, precoMax, estoqueMin, ativos, inativos);
    }
    
    @Override
    public Produto buscarPorCodigoBarras(String codigoBarras) {
        logger.info("Buscando produto por código de barras via Excel: " + codigoBarras);
        return excelDao.buscarPorCodigoBarras(codigoBarras);
    }
}
