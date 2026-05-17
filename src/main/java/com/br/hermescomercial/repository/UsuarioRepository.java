package com.br.hermescomercial.repository;


import com.br.hermescomercial.model.Usuario;
import java.util.List;

/**
 * Interface Repository para operações de usuário
 * Segue o Repository Pattern para desacoplamento
 */
public interface UsuarioRepository {
    
    /**
     * Salva um usuário
     * @param usuario Usuário a ser salvo
     */
    void salvar(Usuario usuario);
    
    /**
     * Remove um usuário pelo nome
     * @param nome Nome do usuário
     */
    void remove(String nome);
    
    /**
     * Lista todos os usuários
     * @return Lista de todos os usuários
     */
    List<Usuario> listar();
    
    /**
     * Busca usuários pelo nome
     * @param nome Nome do usuário
     * @return Lista de usuários encontrados
     */
    List<Usuario> buscarPorNome(String nome);
    
    /**
     * Busca usuários por CPF/CNPJ
     * @param textoBusca Texto de busca (CPF ou CNPJ)
     * @return Lista de usuários encontrados
     */
    List<Usuario> buscarPorCpfCnpj(String textoBusca);
    
    /**
     * Busca clientes por nome
     * @param nome Nome do cliente
     * @return Lista de clientes encontrados
     */
    List<Usuario> buscarClientePorNome(String nome);
    
    /**
     * Busca clientes por CPF/CNPJ
     * @param textoBusca Texto de busca (CPF ou CNPJ)
     * @return Lista de clientes encontrados
     */
    List<Usuario> buscarClientePorCpfCnpj(String textoBusca);
    
    /**
     * Busca clientes por nome ou CPF/CNPJ
     * @param textoBusca Texto de busca unificado
     * @return Lista de clientes encontrados
     */
    List<Usuario> buscarClientePorNomeCpfCnpj(String textoBusca);
    
    /**
     * Busca todos os usuários
     * @return Lista de todos os usuários
     */
    List<Usuario> buscarTodos();
}
