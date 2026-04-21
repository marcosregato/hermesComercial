package com.br.hermescomercial.repository;

import com.br.hermescomercial.model.Produto;
import java.math.BigDecimal;
import java.util.List;

/**
 * Interface Repository para operações de produto
 * Segue o Repository Pattern para desacoplamento
 */
public interface ProdutoRepository {
    
    /**
     * Salva um produto
     * @param produto Produto a ser salvo
     * @return true se salvo com sucesso
     */
    boolean salvar(Produto produto);
    
    /**
     * Atualiza um produto existente
     * @param produto Produto a ser atualizado
     * @return true se atualizado com sucesso
     */
    boolean atualizar(Produto produto);
    
    /**
     * Remove um produto pelo ID
     * @param id ID do produto
     * @return true se removido com sucesso
     */
    boolean excluir(Long id);
    
    /**
     * Remove um produto pelo nome
     * @param nome Nome do produto
     * @return true se removido com sucesso
     */
    boolean excluir(String nome);
    
    /**
     * Busca um produto pelo ID
     * @param id ID do produto
     * @return Produto encontrado ou null
     */
    Produto buscarPorId(Long id);
    
    /**
     * Busca um produto pelo código de barras
     * @param codigoBarras Código de barras do produto
     * @return Produto encontrado ou null
     */
    Produto buscarPorCodigoBarras(String codigoBarras);
    
    /**
     * Lista todos os produtos
     * @return Lista de todos os produtos
     */
    List<Produto> listar();
    
    /**
     * Busca produtos com filtros avançados
     * @param nome Nome do produto
     * @param categoria Categoria do produto
     * @param subCategoria Subcategoria do produto
     * @param codigoBarras Código de barras
     * @param precoMin Preço mínimo
     * @param precoMax Preço máximo
     * @param estoqueMin Estoque mínimo
     * @param ativos Se deve buscar apenas produtos ativos
     * @param inativos Se deve buscar apenas produtos inativos
     * @return Lista de produtos encontrados
     */
    List<Produto> buscarComFiltros(String nome, String categoria, String subCategoria, 
                                  String codigoBarras, BigDecimal precoMin, BigDecimal precoMax, 
                                  Integer estoqueMin, boolean ativos, boolean inativos);
}
