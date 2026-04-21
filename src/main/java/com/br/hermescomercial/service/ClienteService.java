package com.br.hermescomercial.service;

import com.br.hermescomercial.dao.ClienteDao;
import com.br.hermescomercial.model.Cliente;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.SQLException;
import java.util.List;

/**
 * Serviço para gerenciamento de clientes
 * Implementa a lógica de negócio para clientes
 * @author marcos
 */
public class ClienteService {
    
    private static final Logger logger = LogManager.getLogger(ClienteService.class);
    
    private final ClienteDao clienteDao;
    
    public ClienteService() {
        this.clienteDao = new ClienteDao();
    }
    
    public ClienteService(ClienteDao clienteDao) {
        this.clienteDao = clienteDao;
    }
    
    /**
     * Salva um novo cliente
     * @param cliente Cliente a ser salvo
     * @return true se salvo com sucesso
     */
    public boolean salvar(Cliente cliente) {
        try {
            validarCliente(cliente);
            return clienteDao.salvar(cliente);
        } catch (SQLException e) {
            logger.error("Erro ao salvar cliente: " + e.getMessage(), e);
            throw new RuntimeException("Não foi possível salvar o cliente", e);
        }
    }
    
    /**
     * Remove um cliente pelo nome
     * @param nome Nome do cliente a ser removido
     * @return true se removido com sucesso
     */
    public boolean remover(String nome) {
        try {
            if (nome == null || nome.trim().isEmpty()) {
                throw new IllegalArgumentException("Nome do cliente não pode ser nulo ou vazio");
            }
            return clienteDao.remove(nome);
        } catch (SQLException e) {
            logger.error("Erro ao remover cliente: " + e.getMessage(), e);
            throw new RuntimeException("Não foi possível remover o cliente", e);
        }
    }
    
    /**
     * Atualiza um cliente existente
     * @param cliente Cliente com dados atualizados
     * @return true se atualizado com sucesso
     */
    public boolean atualizar(Cliente cliente) {
        try {
            validarCliente(cliente);
            return clienteDao.update(cliente);
        } catch (SQLException e) {
            logger.error("Erro ao atualizar cliente: " + e.getMessage(), e);
            throw new RuntimeException("Não foi possível atualizar o cliente", e);
        }
    }
    
    /**
     * Busca clientes por nome
     * @param nome Termo de busca
     * @return Lista de clientes encontrados
     */
    public List<Cliente> buscarPorNome(String nome) {
        try {
            if (nome == null) {
                return listar();
            }
            return clienteDao.buscarComFiltros(nome, false, false, false);
        } catch (Exception e) {
            logger.error("Erro ao buscar clientes: " + e.getMessage(), e);
            throw new RuntimeException("Não foi possível buscar clientes", e);
        }
    }
    
    /**
     * Lista todos os clientes
     * @return Lista de todos os clientes
     */
    public List<Cliente> listar() {
        try {
            return clienteDao.listar();
        } catch (Exception e) {
            logger.error("Erro ao listar clientes: " + e.getMessage(), e);
            throw new RuntimeException("Não foi possível listar clientes", e);
        }
    }
    
    /**
     * Busca cliente por CPF
     * @param cpf CPF do cliente
     * @return Cliente encontrado ou null
     */
    public Cliente buscarPorCPF(String cpf) {
        try {
            if (cpf == null || cpf.trim().isEmpty()) {
                return null;
            }
            return clienteDao.buscarPorCPF(cpf);
        } catch (SQLException e) {
            logger.error("Erro ao buscar cliente por CPF: " + e.getMessage(), e);
            throw new RuntimeException("Não foi possível buscar cliente por CPF", e);
        }
    }
    
    /**
     * Busca cliente por ID
     * @param id ID do cliente
     * @return Cliente encontrado ou null
     */
    public Cliente buscarPorId(Long id) {
        try {
            if (id == null) {
                return null;
            }
            return clienteDao.buscarPorId(id);
        } catch (SQLException e) {
            logger.error("Erro ao buscar cliente por ID: " + e.getMessage(), e);
            throw new RuntimeException("Não foi possível buscar cliente por ID", e);
        }
    }
    
    /**
     * Busca clientes com filtros avançados
     * @param busca Termo de busca geral
     * @param apenasAtivos Filtrar apenas clientes ativos
     * @param apenasFisica Filtrar apenas pessoas físicas
     * @param apenasJuridica Filtrar apenas pessoas jurídicas
     * @return Lista de clientes filtrados
     */
    public List<Cliente> buscarComFiltros(String busca, boolean apenasAtivos, 
                                          boolean apenasFisica, boolean apenasJuridica) {
        try {
            return clienteDao.buscarComFiltros(busca, apenasAtivos, apenasFisica, apenasJuridica);
        } catch (Exception e) {
            logger.error("Erro ao buscar clientes com filtros: " + e.getMessage(), e);
            throw new RuntimeException("Não foi possível buscar clientes com filtros", e);
        }
    }
    
    /**
     * Valida os dados do cliente
     * @param cliente Cliente a ser validado
     */
    private void validarCliente(Cliente cliente) {
        if (cliente == null) {
            throw new IllegalArgumentException("Cliente não pode ser nulo");
        }
        
        if (cliente.getNome() == null || cliente.getNome().trim().isEmpty()) {
            throw new IllegalArgumentException("Nome do cliente é obrigatório");
        }
        
        if (cliente.getNome().length() < 3) {
            throw new IllegalArgumentException("Nome do cliente deve ter pelo menos 3 caracteres");
        }
        
        if (cliente.getNome().length() > 100) {
            throw new IllegalArgumentException("Nome do cliente não pode ter mais de 100 caracteres");
        }
        
        if (cliente.getCpf() != null && !cliente.getCpf().trim().isEmpty()) {
            if (!validarCPF(cliente.getCpf())) {
                throw new IllegalArgumentException("CPF inválido");
            }
        }
        
        if (cliente.getEmail() != null && !cliente.getEmail().trim().isEmpty()) {
            if (!validarEmail(cliente.getEmail())) {
                throw new IllegalArgumentException("E-mail inválido");
            }
        }
        
        logger.debug("Cliente validado com sucesso: " + cliente.getNome());
    }
    
    /**
     * Valida formato do CPF
     * @param cpf CPF a ser validado
     * @return true se CPF é válido
     */
    private boolean validarCPF(String cpf) {
        if (cpf == null) return false;
        
        // Remove caracteres não numéricos
        String cpfNumeros = cpf.replaceAll("[^0-9]", "");
        
        if (cpfNumeros.length() != 11) {
            return false;
        }
        
        // Validação simples de CPF (não considera dígitos verificadores reais)
        // Para validação completa, implementar algoritmo do CPF
        return !cpfNumeros.equals("00000000000") && 
               !cpfNumeros.equals("11111111111") && 
               !cpfNumeros.equals("22222222222") && 
               !cpfNumeros.equals("33333333333") && 
               !cpfNumeros.equals("44444444444") && 
               !cpfNumeros.equals("55555555555") && 
               !cpfNumeros.equals("66666666666") && 
               !cpfNumeros.equals("77777777777") && 
               !cpfNumeros.equals("88888888888") && 
               !cpfNumeros.equals("99999999999");
    }
    
    /**
     * Valida formato de e-mail
     * @param email E-mail a ser validado
     * @return true se e-mail é válido
     */
    private boolean validarEmail(String email) {
        if (email == null) return false;
        
        return email.contains("@") && 
               email.contains(".") && 
               email.indexOf("@") < email.lastIndexOf(".") &&
               email.indexOf("@") > 0 &&
               email.lastIndexOf(".") < email.length() - 1;
    }
    
    /**
     * Verifica se cliente existe pelo nome
     * @param nome Nome do cliente
     * @return true se cliente existe
     */
    public boolean existePorNome(String nome) {
        try {
            List<Cliente> clientes = buscarPorNome(nome);
            return clientes.stream().anyMatch(c -> c.getNome().equalsIgnoreCase(nome));
        } catch (Exception e) {
            logger.error("Erro ao verificar existência do cliente: " + e.getMessage(), e);
            return false;
        }
    }
    
    /**
     * Verifica se cliente existe pelo CPF
     * @param cpf CPF do cliente
     * @return true se cliente existe
     */
    public boolean existePorCPF(String cpf) {
        try {
            Cliente cliente = buscarPorCPF(cpf);
            return cliente != null;
        } catch (Exception e) {
            logger.error("Erro ao verificar existência do cliente por CPF: " + e.getMessage(), e);
            return false;
        }
    }
    
    /**
     * Ativa ou desativa um cliente
     * @param id ID do cliente
     * @param ativo Status a ser definido
     * @return true se atualizado com sucesso
     */
    public boolean ativarDesativar(Long id, boolean ativo) {
        try {
            Cliente cliente = buscarPorId(id);
            if (cliente == null) {
                throw new IllegalArgumentException("Cliente não encontrado: " + id);
            }
            
            cliente.setAtivo(ativo);
            return atualizar(cliente);
            
        } catch (Exception e) {
            logger.error("Erro ao ativar/desativar cliente: " + e.getMessage(), e);
            throw new RuntimeException("Não foi possível alterar status do cliente", e);
        }
    }
}
