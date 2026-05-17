package com.br.hermescomercial.shared.dao;

import com.br.hermescomercial.model.Cliente;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.SQLException;
import java.util.List;

/**
 * Adaptador para fazer ClienteExcelDao funcionar como ClienteDao
 * @author marcos
 */
public class ClienteDaoAdapter extends ClienteDao {
    private static final Logger logger = LogManager.getLogger(ClienteDaoAdapter.class);
    private final ClienteExcelDao excelDao;
    
    public ClienteDaoAdapter(ClienteExcelDao excelDao) {
        this.excelDao = excelDao;
    }
    
    @Override
    public boolean salvar(Cliente cliente) throws SQLException {
        logger.info("Salvando cliente via Excel: " + cliente.getNome());
        return excelDao.salvar(cliente);
    }
    
    @Override
    public boolean remove(String nome) throws SQLException {
        logger.info("Removendo cliente via Excel: " + nome);
        return excelDao.remove(nome);
    }
    
    @Override
    public List<Cliente> listar() {
        logger.info("Listando clientes via Excel");
        return excelDao.listar();
    }
    
    @Override
    public boolean update(Cliente cliente) throws SQLException {
        logger.info("Atualizando cliente via Excel: " + cliente.getNome());
        return excelDao.update(cliente);
    }
    
    @Override
    public Cliente buscarPorId(Long id) throws SQLException {
        logger.info("Buscando cliente por ID via Excel: " + id);
        // Como o Excel não tem busca por ID, vamos buscar por nome ou retornar null
        return null;
    }
}
