package com.br.hermescomercial.repository.impl;

import com.br.hermescomercial.dao.UsuarioDao;
import com.br.hermescomercial.model.Usuario;
import com.br.hermescomercial.repository.UsuarioRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Implementação do UsuarioRepository usando UsuarioDao existente
 * Segue padrões de arquitetura definidos em docs/ARQUITETURA.md
 */
public class UsuarioRepositoryImpl implements UsuarioRepository {
    
    private static final Logger logger = LogManager.getLogger(UsuarioRepositoryImpl.class);
    private final UsuarioDao usuarioDao = new UsuarioDao();
    
    @Override
    public void salvar(Usuario usuario) {
        try {
            usuarioDao.salvar(usuario);
        } catch (Exception e) {
            logger.error("Erro ao salvar usuário: {}", usuario.getNome(), e);
            throw new RuntimeException("Erro ao salvar usuário", e);
        }
    }
    
    @Override
    public void remove(String nome) {
        try {
            usuarioDao.remove(nome);
        } catch (Exception e) {
            logger.error("Erro ao remover usuário: {}", nome, e);
            throw new RuntimeException("Erro ao remover usuário", e);
        }
    }
    
    @Override
    public java.util.List<Usuario> listar() {
        try {
            return usuarioDao.listar();
        } catch (Exception e) {
            logger.error("Erro ao listar usuários", e);
            throw new RuntimeException("Erro ao listar usuários", e);
        }
    }
    
    @Override
    public java.util.List<Usuario> buscarPorNome(String nome) {
        try {
            return usuarioDao.buscarTodos().stream()
                .filter(u -> u.getNome() != null && u.getNome().toLowerCase().contains(nome.toLowerCase()))
                .collect(java.util.stream.Collectors.toList());
        } catch (Exception e) {
            logger.error("Erro ao buscar usuários por nome: {}", nome, e);
            throw new RuntimeException("Erro ao buscar usuários por nome", e);
        }
    }
    
    @Override
    public java.util.List<Usuario> buscarPorCpfCnpj(String textoBusca) {
        try {
            return usuarioDao.buscarTodos().stream()
                .filter(u -> u.getNumeroDocumeto() != null && u.getNumeroDocumeto().contains(textoBusca))
                .collect(java.util.stream.Collectors.toList());
        } catch (Exception e) {
            logger.error("Erro ao buscar usuários por CPF/CNPJ: {}", textoBusca, e);
            throw new RuntimeException("Erro ao buscar usuários por CPF/CNPJ", e);
        }
    }
    
    @Override
    public java.util.List<Usuario> buscarClientePorNome(String nome) {
        try {
            return usuarioDao.buscarClientePorNome(nome);
        } catch (Exception e) {
            logger.error("Erro ao buscar clientes por nome: {}", nome, e);
            throw new RuntimeException("Erro ao buscar clientes por nome", e);
        }
    }
    
    @Override
    public java.util.List<Usuario> buscarClientePorCpfCnpj(String textoBusca) {
        try {
            return usuarioDao.buscarClientePorCpfCnpj(textoBusca);
        } catch (Exception e) {
            logger.error("Erro ao buscar clientes por CPF/CNPJ: {}", textoBusca, e);
            throw new RuntimeException("Erro ao buscar clientes por CPF/CNPJ", e);
        }
    }
    
    @Override
    public java.util.List<Usuario> buscarClientePorNomeCpfCnpj(String textoBusca) {
        try {
            return usuarioDao.buscarClientePorNomeCpfCnpj(textoBusca);
        } catch (Exception e) {
            logger.error("Erro ao buscar clientes por Nome/CPF/CNPJ: {}", textoBusca, e);
            throw new RuntimeException("Erro ao buscar clientes por Nome/CPF/CNPJ", e);
        }
    }
    
    @Override
    public java.util.List<Usuario> buscarTodos() {
        try {
            return usuarioDao.buscarTodos();
        } catch (Exception e) {
            logger.error("Erro ao buscar todos os usuários", e);
            throw new RuntimeException("Erro ao buscar todos os usuários", e);
        }
    }
}
