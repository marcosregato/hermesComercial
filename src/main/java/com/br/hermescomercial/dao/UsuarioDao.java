package com.br.hermescomercial.dao;

import com.br.hermescomercial.connectionBD.ConnectionBD;
import com.br.hermescomercial.model.Usuario;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class UsuarioDao {
    
    private static final org.apache.logging.log4j.Logger logger = org.apache.logging.log4j.LogManager.getLogger(UsuarioDao.class);
    // private ConnectionBD con = new ConnectionBD(); - não utilizado


    private Usuario mapResultSetToUsuario(ResultSet rs) throws SQLException {
        Usuario usuario = new Usuario();
        usuario.setId(rs.getInt("ID"));
        usuario.setNome(rs.getString("NOME"));
        usuario.setEndereco(rs.getString("ENDERECO"));
        usuario.setBairro(rs.getString("BAIRRO"));
        usuario.setCidade(rs.getString("CIDADE"));
        usuario.setEstado(rs.getString("ESTADO"));
        usuario.setCep(rs.getString("CEP"));
        usuario.setWhastsapp(rs.getString("WHATSAPP"));
        usuario.setTelefone(rs.getString("TELEFONE"));
        usuario.setEmail(rs.getString("EMAIL"));
        usuario.setTipoUsuario(rs.getString("TIPO_USUARIO"));
        
        // Para CPF e CNPJ, usar os campos diretos da tabela
        String cpf = rs.getString("CPF");
        String cnpj = rs.getString("CNPJ");
        if (cpf != null && !cpf.isEmpty()) {
            usuario.setNumeroDocumeto(cpf);
            usuario.setTipoDocumento("F"); // Pessoa Física
        } else if (cnpj != null && !cnpj.isEmpty()) {
            usuario.setNumeroDocumeto(cnpj);
            usuario.setTipoDocumento("J"); // Pessoa Jurídica
        }
        
        return usuario;
    }

    public void salvar(Usuario usuario) {
        String sql = "INSERT INTO USUARIO (nome, endereco, bairro, cidade, estado, cep, tipoDocumento, numeroDocumento, whatsapp, telefone, email, tipo_usuario) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement ps = ConnectionBD.getConnection().prepareStatement(sql)) {
            ps.setString(1, usuario.getNome());
            ps.setString(2, usuario.getEndereco());
            ps.setString(3, usuario.getBairro());
            ps.setString(4, usuario.getCidade());
            ps.setString(5, usuario.getEstado());
            ps.setString(6, usuario.getCep());
            ps.setString(7, usuario.getTipoDocumento());
            ps.setString(8, usuario.getNumeroDocumeto());
            ps.setString(9, usuario.getWhastsapp());
            ps.setString(10, usuario.getTelefone());
            ps.setString(11, usuario.getEmail());
            ps.setString(12, usuario.getTipousuario());
            ps.executeUpdate();
        } catch (Exception e) {
            logger.error("Erro ao salvar usuario: " + e.getMessage());
        }
    }

    public void update(Usuario usuario) {
        String sql = "UPDATE USUARIO SET endereco=?, bairro=?, cidade=?, estado=?, cep=?, tipoDocumento=?, numeroDocumento=?, whatsapp=?, telefone=?, email=?, tipo_usuario=? WHERE nome=?";
        try (PreparedStatement ps = ConnectionBD.getConnection().prepareStatement(sql)) {
            ps.setString(1, usuario.getEndereco());
            ps.setString(2, usuario.getBairro());
            ps.setString(3, usuario.getCidade());
            ps.setString(4, usuario.getEstado());
            ps.setString(5, usuario.getCep());
            ps.setString(6, usuario.getTipoDocumento());
            ps.setString(7, usuario.getNumeroDocumeto());
            ps.setString(8, usuario.getWhastsapp());
            ps.setString(9, usuario.getTelefone());
            ps.setString(10, usuario.getEmail());
            ps.setString(11, usuario.getTipousuario());
            ps.setString(12, usuario.getNome());
            ps.executeUpdate();
        } catch (Exception e) {
            logger.error("Erro ao atualizar usuario: " + e.getMessage());
        }
    }

    public void remove(String nome) {
        String sql = "DELETE FROM USUARIO WHERE nome=?";
        try (PreparedStatement ps = ConnectionBD.getConnection().prepareStatement(sql)) {
            ps.setString(1, nome);
            ps.executeUpdate();
        } catch (Exception e) {
            logger.error("Erro ao remover usuario: " + e.getMessage());
        }
    }

    public List<Usuario> buscarClientePorNome(String nome) {
        String sql = "SELECT * FROM USUARIO WHERE TIPO_USUARIO = 'CLIENTE' AND NOME ILIKE ?";
        List<Usuario> lista = new ArrayList<>();
        try {
            System.out.println("DEBUG DAO: Executando SQL por nome: " + sql);
            System.out.println("DEBUG DAO: Parâmetro: %" + nome + "%");
            
            // Primeiro, vamos verificar todos os usuários no banco
            String sqlTodos = "SELECT * FROM USUARIO";
            try (PreparedStatement psTodos = ConnectionBD.getConnection().prepareStatement(sqlTodos)) {
                try (ResultSet rsTodos = psTodos.executeQuery()) {
                    System.out.println("DEBUG DAO: Verificando todos os usuários no banco:");
                    int totalUsuarios = 0;
                    int totalClientes = 0;
                    while (rsTodos.next()) {
                        totalUsuarios++;
                        String tipoUsuario = rsTodos.getString("TIPO_USUARIO");
                        String nomeUsuario = rsTodos.getString("NOME");
                        System.out.println("DEBUG DAO: Usuário " + totalUsuarios + " - Nome: " + nomeUsuario + ", Tipo: " + tipoUsuario);
                        
                        if ("CLIENTE".equals(tipoUsuario)) {
                            totalClientes++;
                            if (nomeUsuario.toLowerCase().contains(nome.toLowerCase())) {
                                System.out.println("DEBUG DAO: *** CLIENTE ENCONTRADO COM 'jo' no nome: " + nomeUsuario);
                            }
                        }
                    }
                    System.out.println("DEBUG DAO: Total de usuários no banco: " + totalUsuarios);
                    System.out.println("DEBUG DAO: Total de clientes no banco: " + totalClientes);
                }
            }
            
            // Agora executa a busca específica
            try (PreparedStatement ps = ConnectionBD.getConnection().prepareStatement(sql)) {
                String busca = "%" + nome + "%";
                ps.setString(1, busca);
                
                try (ResultSet rs = ps.executeQuery()) {
                    int count = 0;
                    while (rs.next()) {
                        count++;
                        Usuario usuario = mapResultSetToUsuario(rs);
                        System.out.println("DEBUG DAO: Cliente " + count + " - ID: " + usuario.getId() + ", Nome: " + usuario.getNome());
                        lista.add(usuario);
                    }
                    System.out.println("DEBUG DAO: Total de clientes encontrados por nome: " + count);
                }
            }
        } catch (Exception e) {
            System.out.println("DEBUG DAO: Erro na busca por nome: " + e.getMessage());
            e.printStackTrace();
            logger.error("Erro ao buscar usuario: " + e.getMessage());
        }
        return lista;
    }

    public List<Usuario> buscarClientePorCpfCnpj(String textoBusca) {
        String sql = "SELECT * FROM USUARIO WHERE TIPO_USUARIO = 'CLIENTE' AND (CPF ILIKE ? OR CNPJ ILIKE ?)";
        List<Usuario> lista = new ArrayList<>();
        try {
            System.out.println("DEBUG DAO: Executando SQL: " + sql);
            System.out.println("DEBUG DAO: Parâmetro: %" + textoBusca + "%");
            
            try (PreparedStatement ps = ConnectionBD.getConnection().prepareStatement(sql)) {
                String busca = "%" + textoBusca + "%";
                ps.setString(1, busca);
                ps.setString(2, busca);
                
                try (ResultSet rs = ps.executeQuery()) {
                    int count = 0;
                    while (rs.next()) {
                        count++;
                        Usuario usuario = mapResultSetToUsuario(rs);
                        System.out.println("DEBUG DAO: Cliente " + count + " - ID: " + usuario.getId() + ", Nome: " + usuario.getNome() + ", CPF/CNPJ: " + usuario.getNumeroDocumeto());
                        lista.add(usuario);
                    }
                    System.out.println("DEBUG DAO: Total de clientes encontrados: " + count);
                }
            }
        } catch (Exception e) {
            System.out.println("DEBUG DAO: Erro na busca: " + e.getMessage());
            logger.error("Erro ao buscar cliente por CPF/CNPJ: " + e.getMessage());
        }
        return lista;
    }

    public List<Usuario> buscarClientePorNomeCpfCnpj(String textoBusca) {
        String sql = "SELECT * FROM USUARIO WHERE TIPO_USUARIO = 'CLIENTE' AND (NOME ILIKE ? OR CPF ILIKE ? OR CNPJ ILIKE ?)";
        List<Usuario> lista = new ArrayList<>();
        try {
            System.out.println("DEBUG DAO: Executando SQL unificado: " + sql);
            System.out.println("DEBUG DAO: Parâmetro: %" + textoBusca + "%");
            
            try (PreparedStatement ps = ConnectionBD.getConnection().prepareStatement(sql)) {
                String busca = "%" + textoBusca + "%";
                ps.setString(1, busca);
                ps.setString(2, busca);
                ps.setString(3, busca);
                
                try (ResultSet rs = ps.executeQuery()) {
                    int count = 0;
                    while (rs.next()) {
                        count++;
                        Usuario usuario = mapResultSetToUsuario(rs);
                        System.out.println("DEBUG DAO: Cliente " + count + " - ID: " + usuario.getId() + ", Nome: " + usuario.getNome() + ", CPF/CNPJ: " + usuario.getNumeroDocumeto());
                        lista.add(usuario);
                    }
                    System.out.println("DEBUG DAO: Total de clientes encontrados: " + count);
                }
            }
        } catch (Exception e) {
            System.out.println("DEBUG DAO: Erro na busca unificada: " + e.getMessage());
            logger.error("Erro ao buscar cliente por Nome/CPF/CNPJ: " + e.getMessage());
        }
        return lista;
    }

    public List<Usuario> buscarTodos() {
        String sql = "SELECT * FROM USUARIO";
        List<Usuario> lista = new ArrayList<>();
        try (PreparedStatement ps = ConnectionBD.getConnection().prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                lista.add(mapResultSetToUsuario(rs));
            }
        } catch (Exception e) {
            logger.error("Erro ao listar usuarios: " + e.getMessage());
        }
        return lista;
    }

    public List<Usuario> listar() {
        return buscarTodos();
    }
}
