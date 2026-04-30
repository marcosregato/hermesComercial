package com.br.hermescomercial.service;

import com.br.hermescomercial.model.Usuario;
import com.br.hermescomercial.repository.UsuarioRepository;
import com.br.hermescomercial.exception.BusinessException;
import com.br.hermescomercial.exception.ValidationException;
import com.br.hermescomercial.validation.Validator;
import com.br.hermescomercial.injection.DependencyContainer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;

/**
 * Serviço refatorado para gerenciamento de usuários
 * Implementa injeção de dependências e validações adequadas
 * @author marcos
 */
public class UsuarioService {
    
    private static final Logger logger = LogManager.getLogger(UsuarioService.class);
    private final UsuarioRepository usuarioRepository;
    
    /**
     * Construtor com injeção de dependência
     * @param usuarioRepository Repository de usuários
     */
    public UsuarioService(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
        logger.info("UsuarioServiceRefactored inicializado com repository injetado");
    }
    
    /**
     * Construtor padrão usando container de dependências
     */
    public UsuarioService() {
        this(DependencyContainer.getInstance().get(UsuarioRepository.class));
    }
    
    /**
     * Salva um novo usuário com validação
     * @param usuario Usuário a ser salvo
     * @throws ValidationException Se o usuário for inválido
     * @throws BusinessException Se ocorrer erro de negócio
     */
    public void salvar(Usuario usuario) {
        try {
            logger.debug("Iniciando salvamento do usuário: {}", usuario != null ? usuario.getNome() : "null");
            
            // Validação do usuário
            Validator.validarUsuario(usuario);
            
            // Verificar se usuário já existe (mesmo nome)
            if (usuario.getNome() != null && !usuario.getNome().trim().isEmpty()) {
                List<Usuario> existentes = usuarioRepository.buscarPorNome(usuario.getNome());
                for (Usuario existente : existentes) {
                    if (usuario.getNome().equals(existente.getNome()) && 
                        existente.getId() != usuario.getId()) {
                        throw new BusinessException("Já existe um usuário cadastrado com este nome");
                    }
                }
            }
            
            usuarioRepository.salvar(usuario);
            logger.info("Usuário salvo com sucesso: {}", usuario.getNome());
            
        } catch (ValidationException e) {
            logger.warn("Validação falhou ao salvar usuário: {}", e.getMessage());
            throw e;
        } catch (BusinessException e) {
            logger.warn("Erro de negócio ao salvar usuário: {}", e.getMessage());
            throw e;
        } catch (Exception e) {
            logger.error("Erro ao salvar usuário: {}", e.getMessage(), e);
            throw new BusinessException("Não foi possível salvar o usuário", e);
        }
    }
    
    /**
     * Remove um usuário pelo nome
     * @param nome Nome do usuário
     * @throws ValidationException Se o nome for inválido
     * @throws BusinessException Se ocorrer erro de negócio
     */
    public void remover(String nome) {
        try {
            logger.debug("Iniciando remoção do usuário: {}", nome);
            
            if (nome == null || nome.trim().isEmpty()) {
                throw new ValidationException("Nome do usuário não pode ser nulo ou vazio");
            }
            
            // Verificar se usuário existe
            List<Usuario> existentes = usuarioRepository.buscarPorNome(nome);
            if (existentes.isEmpty()) {
                throw new BusinessException("Usuário não encontrado para remoção");
            }
            
            usuarioRepository.remove(nome);
            logger.info("Usuário removido com sucesso: {}", nome);
            
        } catch (ValidationException e) {
            logger.warn("Validação falhou ao remover usuário: {}", e.getMessage());
            throw e;
        } catch (BusinessException e) {
            logger.warn("Erro de negócio ao remover usuário: {}", e.getMessage());
            throw e;
        } catch (Exception e) {
            logger.error("Erro ao remover usuário '{}': {}", nome, e.getMessage(), e);
            throw new BusinessException("Não foi possível remover o usuário", e);
        }
    }
    
    /**
     * Lista todos os usuários
     * @return Lista de todos os usuários
     */
    public List<Usuario> listar() {
        try {
            logger.debug("Listando todos os usuários");
            
            List<Usuario> usuarios = usuarioRepository.listar();
            
            logger.info("Total de usuários listados: {}", usuarios.size());
            return usuarios;
            
        } catch (Exception e) {
            logger.error("Erro ao listar usuários: {}", e.getMessage(), e);
            throw new BusinessException("Não foi possível listar usuários", e);
        }
    }
    
    /**
     * Busca um usuário pelo nome
     * @param nome Nome do usuário
     * @return Usuário encontrado ou null
     */
    public Usuario buscarPorNome(String nome) {
        try {
            logger.debug("Buscando usuário por nome: {}", nome);
            
            if (nome == null || nome.trim().isEmpty()) {
                logger.warn("Busca por nome vazia ou nula");
                return null;
            }
            
            List<Usuario> usuarios = usuarioRepository.buscarPorNome(nome);
            if (!usuarios.isEmpty()) {
                logger.debug("Usuário encontrado por nome: {}", usuarios.get(0).getNome());
                return usuarios.get(0); // Retorna o primeiro encontrado
            }
            
            logger.debug("Nenhum usuário encontrado com nome: {}", nome);
            return null;
            
        } catch (Exception e) {
            logger.error("Erro ao buscar usuário por nome '{}': {}", nome, e.getMessage(), e);
            throw new BusinessException("Não foi possível buscar usuário por nome", e);
        }
    }
    
    /**
     * Busca usuários por CPF/CNPJ
     * @param textoBusca Texto de busca (CPF ou CNPJ)
     * @return Lista de usuários encontrados
     */
    public List<Usuario> buscarPorCpfCnpj(String textoBusca) {
        try {
            logger.debug("Buscando usuários por CPF/CNPJ: {}", textoBusca);
            
            if (textoBusca == null || textoBusca.trim().isEmpty()) {
                logger.warn("Busca por CPF/CNPJ vazia ou nula");
                return List.of();
            }
            
            List<Usuario> usuarios = usuarioRepository.buscarPorCpfCnpj(textoBusca);
            
            logger.info("Encontrados {} usuários com CPF/CNPJ contendo '{}'", usuarios.size(), textoBusca);
            return usuarios;
            
        } catch (Exception e) {
            logger.error("Erro ao buscar usuários por CPF/CNPJ '{}': {}", textoBusca, e.getMessage(), e);
            throw new BusinessException("Não foi possível buscar usuários por CPF/CNPJ", e);
        }
    }
    
    /**
     * Busca clientes por nome
     * @param nome Nome do cliente
     * @return Lista de clientes encontrados
     */
    public List<Usuario> buscarClientesPorNome(String nome) {
        try {
            logger.debug("Buscando clientes por nome: {}", nome);
            
            if (nome == null || nome.trim().isEmpty()) {
                logger.warn("Busca por nome vazia ou nula");
                return List.of();
            }
            
            List<Usuario> clientes = usuarioRepository.buscarClientePorNome(nome);
            
            logger.info("Encontrados {} clientes com nome contendo '{}'", clientes.size(), nome);
            return clientes;
            
        } catch (Exception e) {
            logger.error("Erro ao buscar clientes por nome '{}': {}", nome, e.getMessage(), e);
            throw new BusinessException("Não foi possível buscar clientes por nome", e);
        }
    }
    
    /**
     * Busca clientes por CPF/CNPJ
     * @param textoBusca Texto de busca (CPF ou CNPJ)
     * @return Lista de clientes encontrados
     */
    public List<Usuario> buscarClientesPorCpfCnpj(String textoBusca) {
        try {
            logger.debug("Buscando clientes por CPF/CNPJ: {}", textoBusca);
            
            if (textoBusca == null || textoBusca.trim().isEmpty()) {
                logger.warn("Busca por CPF/CNPJ vazia ou nula");
                return List.of();
            }
            
            List<Usuario> clientes = usuarioRepository.buscarClientePorCpfCnpj(textoBusca);
            
            logger.info("Encontrados {} clientes com CPF/CNPJ contendo '{}'", clientes.size(), textoBusca);
            return clientes;
            
        } catch (Exception e) {
            logger.error("Erro ao buscar clientes por CPF/CNPJ '{}': {}", textoBusca, e.getMessage(), e);
            throw new BusinessException("Não foi possível buscar clientes por CPF/CNPJ", e);
        }
    }
    
    /**
     * Busca clientes por nome ou CPF/CNPJ (busca unificada)
     * @param textoBusca Texto de busca unificado
     * @return Lista de clientes encontrados
     */
    public List<Usuario> buscarClientesPorNomeCpfCnpj(String textoBusca) {
        try {
            logger.debug("Buscando clientes por Nome/CPF/CNPJ: {}", textoBusca);
            
            if (textoBusca == null || textoBusca.trim().isEmpty()) {
                logger.warn("Busca unificada vazia ou nula");
                return List.of();
            }
            
            List<Usuario> clientes = usuarioRepository.buscarClientePorNomeCpfCnpj(textoBusca);
            
            logger.info("Encontrados {} clientes com Nome/CPF/CNPJ contendo '{}'", clientes.size(), textoBusca);
            return clientes;
            
        } catch (Exception e) {
            logger.error("Erro ao buscar clientes por Nome/CPF/CNPJ '{}': {}", textoBusca, e.getMessage(), e);
            throw new BusinessException("Não foi possível buscar clientes por Nome/CPF/CNPJ", e);
        }
    }
    
    /**
     * Verifica se um usuário existe pelo nome
     * @param nome Nome do usuário
     * @return true se o usuário existir
     */
    public boolean existe(String nome) {
        try {
            if (nome == null || nome.trim().isEmpty()) {
                return false;
            }
            
            Usuario usuario = buscarPorNome(nome);
            return usuario != null;
            
        } catch (Exception e) {
            logger.error("Erro ao verificar existência do usuário '{}': {}", nome, e.getMessage(), e);
            return false;
        }
    }
    
    /**
     * Valida credenciais de usuário (implementação básica)
     * @param nome Nome do usuário
     * @param senha Senha do usuário
     * @return true se as credenciais forem válidas
     */
    public boolean validarCredenciais(String nome, String senha) {
        try {
            logger.debug("Validando credenciais do usuário: {}", nome);
            
            if (nome == null || nome.trim().isEmpty() || senha == null) {
                logger.warn("Credenciais inválidas - nome ou senha nulos");
                return false;
            }
            
            // Implementação básica - verificar se usuário existe
            Usuario usuario = buscarPorNome(nome);
            if (usuario == null) {
                logger.warn("Usuário não encontrado: {}", nome);
                return false;
            }
            
            // TODO: Implementar validação de senha quando disponível
            logger.debug("Credenciais validadas para usuário: {}", nome);
            return true;
            
        } catch (Exception e) {
            logger.error("Erro ao validar credenciais do usuário '{}': {}", nome, e.getMessage(), e);
            return false;
        }
    }
}
