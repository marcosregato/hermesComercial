package com.br.hermescomercial.erp.service;

import com.br.hermescomercial.dao.UsuarioDao;
import com.br.hermescomercial.erp.model.Usuario;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Serviço básico para gerenciamento de usuários
 * Implementa a lógica de negócio para usuários compatível com o model atual
 * @author marcos
 */
public class UsuarioServiceBasico {
    
    private static final Logger logger = LogManager.getLogger(UsuarioServiceBasico.class);
    
    private final UsuarioDao usuarioDao;
    
    public UsuarioServiceBasico() {
        this.usuarioDao = new UsuarioDao();
    }
    
    public UsuarioServiceBasico(UsuarioDao usuarioDao) {
        this.usuarioDao = usuarioDao;
    }
    
    /**
     * Lista todos os usuários
     * @return Lista de todos os usuários
     */
    public List<Usuario> listar() {
        try {
            List<com.br.hermescomercial.model.Usuario> modelUsuarios = usuarioDao.listar();
            return modelUsuarios.stream()
                    .map(this::converterParaERPUsuario)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            logger.error("Erro ao listar usuários: " + e.getMessage(), e);
            throw new RuntimeException("Não foi possível listar usuários", e);
        }
    }
    
    /**
     * Busca um usuário pelo nome
     * @param nome Nome do usuário
     * @return Usuário encontrado ou null
     */
    public Usuario buscarPorNome(String nome) {
        try {
            if (nome == null || nome.trim().isEmpty()) {
                logger.warn("Busca por nome vazia ou nula");
                return null;
            }
            
            List<com.br.hermescomercial.model.Usuario> modelUsuarios = usuarioDao.buscarClientePorNome(nome);
            if (modelUsuarios != null && !modelUsuarios.isEmpty()) {
                logger.debug("Usuário encontrado por nome: {}", nome);
                return converterParaERPUsuario(modelUsuarios.get(0)); // Retorna o primeiro encontrado
            }
            
            logger.debug("Nenhum usuário encontrado com nome: {}", nome);
            return null;
        } catch (Exception e) {
            logger.error("Erro ao buscar usuário por nome '{}': {}", nome, e.getMessage(), e);
            throw new RuntimeException("Não foi possível buscar usuário", e);
        }
    }
    
    /**
     * Salva um usuário
     * @param usuario Usuário a ser salvo
     */
    public void salvar(Usuario usuario) {
        try {
            validarBasico(usuario);
            com.br.hermescomercial.model.Usuario modelUsuario = converterParaModelUsuario(usuario);
            usuarioDao.salvar(modelUsuario);
        } catch (Exception e) {
            logger.error("Erro ao salvar usuário: " + e.getMessage(), e);
            throw new RuntimeException("Não foi possível salvar o usuário", e);
        }
    }
    
    /**
     * Remove um usuário pelo nome
     * @param nome Nome do usuário a ser removido
     */
    public void remover(String nome) {
        try {
            if (nome == null || nome.trim().isEmpty()) {
                throw new IllegalArgumentException("Nome do usuário não pode ser nulo ou vazio");
            }
            usuarioDao.remove(nome);
        } catch (Exception e) {
            logger.error("Erro ao remover usuário: " + e.getMessage(), e);
            throw new RuntimeException("Não foi possível remover o usuário", e);
        }
    }
    
    /**
     * Validação básica do usuário
     * @param usuario Usuário a ser validado
     */
    private void validarBasico(Usuario usuario) {
        if (usuario == null) {
            throw new IllegalArgumentException("Usuário não pode ser nulo");
        }
        
        if (usuario.getNome() == null || usuario.getNome().trim().isEmpty()) {
            throw new IllegalArgumentException("Nome do usuário é obrigatório");
        }
        
        if (usuario.getNome().length() < 3) {
            throw new IllegalArgumentException("Nome do usuário deve ter pelo menos 3 caracteres");
        }
        
        logger.debug("Usuário validado com sucesso: " + usuario.getNome());
    }
    
    /**
     * Verifica se um usuário existe pelo nome
     * @param nome Nome do usuário
     * @return true se o usuário existe
     */
    public boolean existeUsuario(String nome) {
        try {
            if (nome == null || nome.trim().isEmpty()) {
                return false;
            }
            Usuario usuario = buscarPorNome(nome);
            return usuario != null;
        } catch (Exception e) {
            logger.error("Erro ao verificar existência do usuário: " + e.getMessage(), e);
            return false;
        }
    }
    
    /**
     * Converte model.Usuario para ERP.Usuario
     */
    private Usuario converterParaERPUsuario(com.br.hermescomercial.model.Usuario modelUsuario) {
        if (modelUsuario == null) {
            return null;
        }
        Usuario erpUsuario = new Usuario();
        erpUsuario.setId(modelUsuario.getId());
        erpUsuario.setNome(modelUsuario.getNome());
        erpUsuario.setEmail(modelUsuario.getEmail());
        erpUsuario.setTelefone(modelUsuario.getTelefone());
        erpUsuario.setEndereco(modelUsuario.getEndereco());
        erpUsuario.setBairro(modelUsuario.getBairro());
        erpUsuario.setCidade(modelUsuario.getCidade());
        erpUsuario.setEstado(modelUsuario.getEstado());
        erpUsuario.setCep(modelUsuario.getCep());
        erpUsuario.setNumeroDocumeto(modelUsuario.getNumeroDocumeto());
        erpUsuario.setWhastsapp(modelUsuario.getWhastsapp());
        erpUsuario.setTipoDocumento(modelUsuario.getTipoDocumento());
        erpUsuario.setTipoUsuario(modelUsuario.getTipousuario());
        return erpUsuario;
    }
    
    /**
     * Converte ERP.Usuario para model.Usuario
     */
    private com.br.hermescomercial.model.Usuario converterParaModelUsuario(Usuario erpUsuario) {
        if (erpUsuario == null) {
            return null;
        }
        com.br.hermescomercial.model.Usuario modelUsuario = new com.br.hermescomercial.model.Usuario();
        modelUsuario.setId(erpUsuario.getId());
        modelUsuario.setNome(erpUsuario.getNome());
        modelUsuario.setEmail(erpUsuario.getEmail());
        modelUsuario.setTelefone(erpUsuario.getTelefone());
        modelUsuario.setEndereco(erpUsuario.getEndereco());
        modelUsuario.setBairro(erpUsuario.getBairro());
        modelUsuario.setCidade(erpUsuario.getCidade());
        modelUsuario.setEstado(erpUsuario.getEstado());
        modelUsuario.setCep(erpUsuario.getCep());
        modelUsuario.setNumeroDocumeto(erpUsuario.getNumeroDocumeto());
        modelUsuario.setWhastsapp(erpUsuario.getWhastsapp());
        modelUsuario.setTipoDocumento(erpUsuario.getTipoDocumento());
        modelUsuario.setTipoUsuario(erpUsuario.getTipousuario());
        return modelUsuario;
    }
}
