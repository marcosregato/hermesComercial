package com.br.hermescomercial.repository;

import com.br.hermescomercial.model.Produto;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

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
    boolean remover(Long id);
    
    /**
     * Remove um produto pelo nome
     * @param nome Nome do produto
     * @return true se removido com sucesso
     */
    boolean excluir(String nome);
    
    /**
     * Busca um produto pelo ID
     * @param id ID do produto
     * @return Produto encontrado ou Optional vazio
     */
    Optional<Produto> buscarPorId(Long id);
    
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
    List<Produto> buscarTodos();
    
    /**
     * Busca produtos por nome
     * @param nome Nome do produto
     * @return Lista de produtos encontrados
     */
    List<Produto> buscarPorNome(String nome);
    
    /**
     * Busca produtos por categoria
     * @param categoria Categoria do produto
     * @return Lista de produtos encontrados
     */
    List<Produto> buscarPorCategoria(String categoria);
    
    /**
     * Busca produtos com estoque baixo
     * @return Lista de produtos com estoque baixo
     */
    List<Produto> buscarEstoqueBaixo();
    
    /**
     * Atualiza estoque de um produto
     * @param id ID do produto
     * @param novaQuantidade Nova quantidade de estoque
     * @return true se atualizado com sucesso
     */
    boolean atualizarEstoque(Long id, int novaQuantidade);
    
    /**
     * Lista todos os produtos (método legado)
     * @return Lista de todos os produtos
     */
    List<Produto> listar();
    
    /**
     * Busca produtos por faixa de preço
     * @param precoMin Preço mínimo
     * @param precoMax Preço máximo
     * @return Lista de produtos encontrados
     */
    List<Produto> buscarPorFaixaPreco(BigDecimal precoMin, BigDecimal precoMax);
    
    /**
     * Busca produtos com estoque baixo
     * @param limite Limite máximo de estoque
     * @return Lista de produtos com estoque baixo
     */
    List<Produto> buscarComEstoqueBaixo(int limite);
    
    /**
     * Busca produtos por código de barras
     * @param codigo Código do produto
     * @return Lista de produtos encontrados
     */
    List<Produto> buscarPorCodigo(String codigo);
    
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
