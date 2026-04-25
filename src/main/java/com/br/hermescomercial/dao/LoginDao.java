package com.br.hermescomercial.dao;

import com.br.hermescomercial.model.Usuario;
import com.br.hermescomercial.connectionBD.ConnectionBD;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class LoginDao {
    private static final Logger logger = LogManager.getLogger(LoginDao.class);

    public Usuario acessarUsuario(String login, String senha) {
        try {
            String query = "SELECT u.id, u.nome, u.endereco, u.bairro, u.cidade, u.estado, u.cep, u.cnpj, u.cpf, u.email, u.tipo_pessoa " +
                           "FROM login l " +
                           "INNER JOIN usuario u ON l.fk_usuario = u.id " +
                           "WHERE l.login = ? AND l.senha = ?";
            
            PreparedStatement ps = ConnectionBD.getConnection().prepareStatement(query);
            ps.setString(1, login);
            ps.setString(2, senha);
            
            ResultSet rs = ps.executeQuery();
            Usuario usuario = null;
            
            if (rs.next()) {
                usuario = new Usuario();
                usuario.setId(rs.getLong("id"));
                usuario.setNome(rs.getString("nome"));
                usuario.setEndereco(rs.getString("endereco"));
                usuario.setBairro(rs.getString("bairro"));
                usuario.setCidade(rs.getString("cidade"));
                usuario.setEstado(rs.getString("estado"));
                usuario.setCep(rs.getString("cep"));
                usuario.setNumeroDocumeto(rs.getString("cnpj"));
                usuario.setNumeroDocumeto(rs.getString("cpf"));
                usuario.setEmail(rs.getString("email"));
                usuario.setTipoUsuario(rs.getString("tipo_pessoa"));
            }
            
            rs.close();
            ps.close();
            return usuario;
            
        } catch (Exception e) {
            logger.error("Erro ao acessar usuário: " + e.getMessage(), e);
            return null;
        }
    }

    public void salvar(Usuario usuario) {
        try {
            String query = "INSERT INTO usuario (nome, endereco, bairro, cidade, estado, cep, cnpj, cpf, email, tipo_pessoa) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
            PreparedStatement ps = ConnectionBD.getConnection().prepareStatement(query);
            
            ps.setString(1, usuario.getNome());
            ps.setString(2, usuario.getEndereco());
            ps.setString(3, usuario.getBairro());
            ps.setString(4, usuario.getCidade());
            ps.setString(5, usuario.getEstado());
            ps.setString(6, usuario.getCep());
            ps.setString(7, usuario.getNumeroDocumeto());
            ps.setString(8, usuario.getNumeroDocumeto());
            ps.setString(9, usuario.getEmail());
            ps.setString(10, usuario.getTipousuario());
            
            ps.executeUpdate();
            ps.close();
            logger.info("Usuário salvo com sucesso: " + usuario.getNome());
            
        } catch (Exception e) {
            logger.error("Erro ao salvar usuário: " + e.getMessage(), e);
        }
    }
}
