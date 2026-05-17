package com.br.hermescomercial.repository.impl;

import com.br.hermescomercial.dao.ClienteDao;
import com.br.hermescomercial.model.Cliente;
import com.br.hermescomercial.repository.ClienteRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Implementação do ClienteRepository usando ClienteDao existente
 * Segue padrões de arquitetura definidos em docs/ARQUITETURA.md
 */
public class ClienteRepositoryImpl implements ClienteRepository {
    
    private static final Logger logger = LogManager.getLogger(ClienteRepositoryImpl.class);
    private final ClienteDao clienteDao = new ClienteDao();
    
    @Override
    public boolean salvar(Cliente cliente) {
        try {
            return clienteDao.salvar(cliente);
        } catch (Exception e) {
            logger.error("Erro ao salvar cliente: {}", cliente.getNome(), e);
            throw new RuntimeException("Erro ao salvar cliente", e);
        }
    }
    
    @Override
    public boolean atualizar(Cliente cliente) {
        try {
            return clienteDao.update(cliente);
        } catch (Exception e) {
            logger.error("Erro ao atualizar cliente: {}", cliente.getNome(), e);
            throw new RuntimeException("Erro ao atualizar cliente", e);
        }
    }
    
    @Override
    public boolean excluir(Long id) {
        try {
            Cliente cliente = clienteDao.buscarPorId(id);
            if (cliente != null) {
                return clienteDao.remove(cliente.getNome());
            }
            return false;
        } catch (Exception e) {
            logger.error("Erro ao excluir cliente com ID: {}", id, e);
            throw new RuntimeException("Erro ao excluir cliente", e);
        }
    }
    
    @Override
    public Cliente buscarPorId(Long id) {
        try {
            return clienteDao.buscarPorId(id);
        } catch (Exception e) {
            logger.error("Erro ao buscar cliente por ID: {}", id, e);
            throw new RuntimeException("Erro ao buscar cliente por ID", e);
        }
    }
    
    @Override
    public java.util.List<Cliente> listar() {
        try {
            return clienteDao.listar();
        } catch (Exception e) {
            logger.error("Erro ao listar clientes", e);
            throw new RuntimeException("Erro ao listar clientes", e);
        }
    }
    
    @Override
    public java.util.List<Cliente> buscarComFiltros(String nome, boolean ativos, boolean inativos, boolean ordenarPorNome) {
        try {
            return clienteDao.buscarComFiltros(nome, ativos, inativos, ordenarPorNome);
        } catch (Exception e) {
            logger.error("Erro ao buscar clientes com filtros", e);
            throw new RuntimeException("Erro ao buscar clientes com filtros", e);
        }
    }
}
