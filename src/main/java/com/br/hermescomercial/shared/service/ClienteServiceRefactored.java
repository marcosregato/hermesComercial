package com.br.hermescomercial.shared.service;

import com.br.hermescomercial.model.Cliente;
import com.br.hermescomercial.repository.ClienteRepository;
import com.br.hermescomercial.exception.BusinessException;
import com.br.hermescomercial.exception.ValidationException;
import com.br.hermescomercial.validation.Validator;
import com.br.hermescomercial.injection.DependencyContainer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;

/**
 * Serviço refatorado para gerenciamento de clientes
 * Implementa injeção de dependências e validações adequadas
 * @author marcos
 */
public class ClienteServiceRefactored {
    
    private static final Logger logger = LogManager.getLogger(ClienteServiceRefactored.class);
    private final ClienteRepository clienteRepository;
    
    /**
     * Construtor com injeção de dependência
     * @param clienteRepository Repository de clientes
     */
    public ClienteServiceRefactored(ClienteRepository clienteRepository) {
        this.clienteRepository = clienteRepository;
        logger.info("ClienteServiceRefactored inicializado com repository injetado");
    }
    
    /**
     * Construtor padrão usando container de dependências
     */
    public ClienteServiceRefactored() {
        this(DependencyContainer.getInstance().get(ClienteRepository.class));
    }
    
    /**
     * Salva um novo cliente com validação
     * @param cliente Cliente a ser salvo
     * @return true se salvo com sucesso
     * @throws ValidationException Se o cliente for inválido
     * @throws BusinessException Se ocorrer erro de negócio
     */
    public boolean salvar(Cliente cliente) {
        try {
            logger.debug("Iniciando salvamento do cliente: {}", cliente != null ? cliente.getNome() : "null");
            
            // Validação do cliente
            Validator.validarCliente(cliente);
            
            // Verificar se cliente já existe (mesmo CPF/CNPJ)
            if (cliente.getCpf() != null && !cliente.getCpf().trim().isEmpty()) {
                Cliente existente = buscarPorCPF(cliente.getCpf());
                if (existente != null && !existente.getId().equals(cliente.getId())) {
                    throw new BusinessException("Já existe um cliente cadastrado com este CPF");
                }
            }
            
            boolean resultado = clienteRepository.salvar(cliente);
            
            if (resultado) {
                logger.info("Cliente salvo com sucesso: {}", cliente.getNome());
            } else {
                logger.warn("Falha ao salvar cliente: {}", cliente.getNome());
            }
            
            return resultado;
            
        } catch (ValidationException e) {
            logger.warn("Validação falhou ao salvar cliente: {}", e.getMessage());
            throw e;
        } catch (BusinessException e) {
            logger.warn("Erro de negócio ao salvar cliente: {}", e.getMessage());
            throw e;
        } catch (Exception e) {
            logger.error("Erro ao salvar cliente: {}", e.getMessage(), e);
            throw new BusinessException("Não foi possível salvar o cliente", e);
        }
    }
    
    /**
     * Atualiza um cliente existente com validação
     * @param cliente Cliente a ser atualizado
     * @return true se atualizado com sucesso
     */
    public boolean atualizar(Cliente cliente) {
        try {
            logger.debug("Iniciando atualização do cliente: {}", cliente != null ? cliente.getNome() : "null");
            
            if (cliente == null || cliente.getId() == null) {
                throw new ValidationException("Cliente e ID são obrigatórios para atualização");
            }
            
            // Validação do cliente
            Validator.validarCliente(cliente);
            
            // Verificar se cliente existe
            Cliente existente = clienteRepository.buscarPorId(cliente.getId());
            if (existente == null) {
                throw new BusinessException("Cliente não encontrado para atualização");
            }
            
            boolean resultado = clienteRepository.atualizar(cliente);
            
            if (resultado) {
                logger.info("Cliente atualizado com sucesso: {}", cliente.getNome());
            } else {
                logger.warn("Falha ao atualizar cliente: {}", cliente.getNome());
            }
            
            return resultado;
            
        } catch (ValidationException e) {
            logger.warn("Validação falhou ao atualizar cliente: {}", e.getMessage());
            throw e;
        } catch (BusinessException e) {
            logger.warn("Erro de negócio ao atualizar cliente: {}", e.getMessage());
            throw e;
        } catch (Exception e) {
            logger.error("Erro ao atualizar cliente: {}", e.getMessage(), e);
            throw new BusinessException("Não foi possível atualizar o cliente", e);
        }
    }
    
    /**
     * Exclui um cliente pelo ID
     * @param id ID do cliente
     * @return true se excluído com sucesso
     */
    public boolean excluir(Long id) {
        try {
            logger.debug("Iniciando exclusão do cliente ID: {}", id);
            
            if (id == null) {
                throw new ValidationException("ID do cliente é obrigatório para exclusão");
            }
            
            // Verificar se cliente existe
            Cliente cliente = clienteRepository.buscarPorId(id);
            if (cliente == null) {
                throw new BusinessException("Cliente não encontrado para exclusão");
            }
            
            boolean resultado = clienteRepository.excluir(id);
            
            if (resultado) {
                logger.info("Cliente excluído com sucesso: {} (ID: {})", cliente.getNome(), id);
            } else {
                logger.warn("Falha ao excluir cliente ID: {}", id);
            }
            
            return resultado;
            
        } catch (ValidationException e) {
            logger.warn("Validação falhou ao excluir cliente: {}", e.getMessage());
            throw e;
        } catch (BusinessException e) {
            logger.warn("Erro de negócio ao excluir cliente: {}", e.getMessage());
            throw e;
        } catch (Exception e) {
            logger.error("Erro ao excluir cliente ID {}: {}", id, e.getMessage(), e);
            throw new BusinessException("Não foi possível excluir o cliente", e);
        }
    }
    
    /**
     * Busca clientes por nome
     * @param nome Termo de busca
     * @return Lista de clientes encontrados
     */
    public List<Cliente> buscarPorNome(String nome) {
        try {
            logger.debug("Buscando clientes por nome: {}", nome);
            
            if (nome == null || nome.trim().isEmpty()) {
                logger.warn("Busca por nome vazia, retornando lista completa");
                return listar();
            }
            
            List<Cliente> clientes = clienteRepository.buscarComFiltros(nome, true, false, true);
            
            logger.info("Encontrados {} clientes com nome contendo '{}'", clientes.size(), nome);
            return clientes;
            
        } catch (Exception e) {
            logger.error("Erro ao buscar clientes por nome '{}': {}", nome, e.getMessage(), e);
            throw new BusinessException("Não foi possível buscar clientes", e);
        }
    }
    
    /**
     * Busca cliente por CPF
     * @param cpf CPF do cliente
     * @return Cliente encontrado ou null
     */
    public Cliente buscarPorCPF(String cpf) {
        try {
            logger.debug("Buscando cliente por CPF: {}", cpf);
            
            if (cpf == null || cpf.trim().isEmpty()) {
                logger.warn("Busca por CPF vazia");
                return null;
            }
            
            // Buscar todos clientes e filtrar por CPF (implementação workaround)
            List<Cliente> todos = listar();
            for (Cliente cliente : todos) {
                if (cpf.equals(cliente.getCpf())) {
                    logger.debug("Cliente encontrado por CPF: {}", cliente.getNome());
                    return cliente;
                }
            }
            
            logger.debug("Nenhum cliente encontrado com CPF: {}", cpf);
            return null;
            
        } catch (Exception e) {
            logger.error("Erro ao buscar cliente por CPF '{}': {}", cpf, e.getMessage(), e);
            throw new BusinessException("Não foi possível buscar cliente por CPF", e);
        }
    }
    
    /**
     * Lista todos os clientes
     * @return Lista de todos os clientes
     */
    public List<Cliente> listar() {
        try {
            logger.debug("Listando todos os clientes");
            
            List<Cliente> clientes = clienteRepository.listar();
            
            logger.info("Total de clientes listados: {}", clientes.size());
            return clientes;
            
        } catch (Exception e) {
            logger.error("Erro ao listar clientes: {}", e.getMessage(), e);
            throw new BusinessException("Não foi possível listar clientes", e);
        }
    }
    
    /**
     * Busca cliente pelo ID
     * @param id ID do cliente
     * @return Cliente encontrado ou null
     */
    public Cliente buscarPorId(Long id) {
        try {
            logger.debug("Buscando cliente por ID: {}", id);
            
            if (id == null) {
                logger.warn("Busca por ID nula");
                return null;
            }
            
            Cliente cliente = clienteRepository.buscarPorId(id);
            
            if (cliente != null) {
                logger.debug("Cliente encontrado por ID: {}", cliente.getNome());
            } else {
                logger.debug("Nenhum cliente encontrado com ID: {}", id);
            }
            
            return cliente;
            
        } catch (Exception e) {
            logger.error("Erro ao buscar cliente por ID '{}': {}", id, e.getMessage(), e);
            throw new BusinessException("Não foi possível buscar cliente por ID", e);
        }
    }
}
