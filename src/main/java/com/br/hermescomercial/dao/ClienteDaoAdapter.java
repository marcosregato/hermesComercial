package com.br.hermescomercial.dao;

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
    public boolean update(Cliente cliente) throws SQLException {
        logger.info("Atualizando cliente via Excel: " + cliente.getNome());
        return excelDao.update(cliente);
    }
    
    @Override
    public List<Cliente> buscarComFiltros(String busca, boolean apenasAtivos, 
                                          boolean apenasFisica, boolean apenasJuridica) {
        logger.info("Buscando clientes com filtros via Excel: " + busca);
        return excelDao.buscar(busca); // Simplificado - usa busca básica
    }
    
    @Override
    public List<Cliente> listar() {
        logger.info("Listando clientes via Excel");
        return excelDao.listar();
    }
    
    @Override
    public Cliente buscarPorCPF(String cpf) throws SQLException {
        logger.info("Buscando cliente por CPF via Excel: " + cpf);
        return excelDao.buscarPorCpf(cpf);
    }
}
