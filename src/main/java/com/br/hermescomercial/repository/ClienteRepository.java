package com.br.hermescomercial.repository;

import com.br.hermescomercial.model.Cliente;
import java.util.List;

/**
 * Interface Repository para operações de cliente
 * Segue o Repository Pattern para desacoplamento
 */
public interface ClienteRepository {
    
    /**
     * Salva um cliente
     * @param cliente Cliente a ser salvo
     * @return true se salvo com sucesso
     */
    boolean salvar(Cliente cliente);
    
    /**
     * Atualiza um cliente existente
     * @param cliente Cliente a ser atualizado
     * @return true se atualizado com sucesso
     */
    boolean atualizar(Cliente cliente);
    
    /**
     * Remove um cliente pelo ID
     * @param id ID do cliente
     * @return true se removido com sucesso
     */
    boolean excluir(Long id);
    
    /**
     * Busca um cliente pelo ID
     * @param id ID do cliente
     * @return Cliente encontrado ou null
     */
    Cliente buscarPorId(Long id);
    
    /**
     * Lista todos os clientes
     * @return Lista de todos os clientes
     */
    List<Cliente> listar();
    
    /**
     * Busca clientes com filtros
     * @param nome Nome do cliente
     * @param ativos Se deve buscar apenas clientes ativos
     * @param inativos Se deve buscar apenas clientes inativos
     * @param ordenarPorNome Se deve ordenar por nome
     * @return Lista de clientes encontrados
     */
    List<Cliente> buscarComFiltros(String nome, boolean ativos, boolean inativos, boolean ordenarPorNome);
}
