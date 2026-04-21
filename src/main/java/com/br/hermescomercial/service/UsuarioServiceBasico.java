package com.br.hermescomercial.service;

import com.br.hermescomercial.dao.UsuarioDao;
import com.br.hermescomercial.model.Usuario;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

import java.util.List;

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
            return usuarioDao.listar();
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
                return null;
            }
            // TODO: Implementar busca quando método estiver disponível no DAO
            return null;
        } catch (Exception e) {
            logger.error("Erro ao buscar usuário por nome: " + e.getMessage(), e);
            throw new RuntimeException("Não foi possível buscar usuário", e);
        }
    }
    
    /**
     * Salva um novo usuário (sem validação de senha)
     * @param usuario Usuário a ser salvo
     */
    public void salvar(Usuario usuario) {
        try {
            validarBasico(usuario);
            usuarioDao.salvar(usuario);
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
}
