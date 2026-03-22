package com.br.hermescomercial.dao;

import com.br.hermescomercial.connectionDB.ConnectionBD;
import com.br.hermescomercial.model.Usuario;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.List;

public class LoginDao {

    private ConnectionBD con = new ConnectionBD();
    private static final Logger logger = LogManager.getLogger(LoginDao.class);

    public Usuario acessarUsuario(String login, String senha) {
       
        Usuario usuario = null;

        String query = "SELECT u.id, u.nome, u.endereco, u.bairro, u.cidade, u.estado, u.cep, u.cnpj, u.cpf, u.email, u.TIPOUSUARIO, u.TIPODOCUMENTO " +
                       "FROM login l " +
                       "INNER JOIN usuario u ON l.fk_usuario = u.id " +
                       "WHERE l.login = ? AND l.senha = ?";

        try (PreparedStatement ps = con.getConnection("Postgres").prepareStatement(query)) {
            ps.setString(1, login);
            ps.setString(2, senha);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    usuario = new Usuario();
                    usuario.setId(rs.getLong("id"));
                    usuario.setNome(rs.getString("nome"));
                    usuario.setEndereco(rs.getString("endereco"));
                    usuario.setBairro(rs.getString("bairro"));
                    usuario.setCidade(rs.getString("cidade"));
                    usuario.setEstado(rs.getString("estado"));
                    usuario.setCep(rs.getString("cep"));
                    usuario.setCnpj(rs.getString("cnpj"));
                    usuario.setCpf(rs.getString("cpf"));
                    usuario.setEmail(rs.getString("email"));
                    usuario.setTipousuario(rs.getString("TIPOUSUARIO"));
                    usuario.setTipoDocumento(rs.getString("TIPODOCUMENTO"));
                }
            }
        } catch (Exception e) {
            logger.error("Erro ao acessar usuario", e);
        }
        return usuario;
    }
    
    public void salvar(Usuario usuario) {
        String query = "INSERT INTO usuario (nome, endereco, bairro, cidade, estado, cep, cnpj, cpf, email, TIPOUSUARIO, TIPODOCUMENTO) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement ps = con.getConnection("Postgres").prepareStatement(query)) {
            ps.setString(1, usuario.getNome());
            ps.setString(2, usuario.getEndereco());
            ps.setString(3, usuario.getBairro());
            ps.setString(4, usuario.getCidade());
            ps.setString(5,usuario.getEstado());
            ps.setString(6,usuario.getCep());
            ps.setString(7,usuario.getCnpj());
            ps.setString(8,usuario.getCpf());
            ps.setString(9,usuario.getEmail());
            ps.setString(10,usuario.getTipousuario());
            ps.setString(11,usuario.getTipoDocumento());
            ps.executeUpdate();
        } catch (Exception e) { 
            logger.error("Erro ao salvar usuario: " + e.getMessage());

        }
    }
    
    public void remove(String nome){
        String query = "DELETE FROM usuario WHERE nome=?";
        try (PreparedStatement ps = con.getConnection("Postgres").prepareStatement(query)) {
            ps.setString(1, nome);
            ps.executeUpdate();
        } catch (Exception e) {
            logger.error("Erro ao remover usuario: " + e.getMessage());

        }
    }
    
    public void update(Usuario usuario){
        String query = "UPDATE usuario SET nome = ?, endereco = ?, bairro = ?, cidade = ?, estado = ?, cep = ?, cnpj = ?, cpf = ?, email = ?, TIPOUSUARIO = ?, TIPODOCUMENTO = ? WHERE id = ?";
        try (PreparedStatement ps = con.getConnection("Postgres").prepareStatement(query)) {
            ps.setString(1, usuario.getNome());
            ps.setString(2, usuario.getEndereco());
            ps.setString(3, usuario.getBairro());
            ps.setString(4, usuario.getCidade());
            ps.setString(5, usuario.getEstado());
            ps.setString(6, usuario.getCep());
            ps.setString(7, usuario.getCnpj());
            ps.setString(8, usuario.getCpf());
            ps.setString(9, usuario.getEmail());
            ps.setString(10, usuario.getTipousuario());
            ps.setString(11, usuario.getTipoDocumento());
            ps.setLong(12, usuario.getId());
            ps.executeUpdate();
        } catch (Exception e) {
            logger.error("Erro ao atualizar usuario: " + e.getMessage());

        }
    }
    
    public List<Usuario> listar(){
        String query = "SELECT * FROM usuario";
        List<Usuario> lista = new java.util.ArrayList<>();
        try (PreparedStatement ps = con.getConnection("Postgres").prepareStatement(query);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Usuario usuario = new Usuario();
                usuario.setId(rs.getLong("id"));
                usuario.setNome(rs.getString("nome"));
                usuario.setEndereco(rs.getString("endereco"));
                usuario.setBairro(rs.getString("bairro"));
                usuario.setCidade(rs.getString("cidade"));
                usuario.setEstado(rs.getString("estado"));
                usuario.setCep(rs.getString("cep"));
                usuario.setCnpj(rs.getString("cnpj"));
                usuario.setCpf(rs.getString("cpf"));
                usuario.setEmail(rs.getString("email"));
                usuario.setTipousuario(rs.getString("TIPOUSUARIO"));
                usuario.setTipoDocumento(rs.getString("TIPODOCUMENTO"));
                lista.add(usuario);
            }
        } catch (Exception e) {
            logger.error("Erro ao listar usuario: " + e.getMessage());
        }
        return lista;
    }

}
