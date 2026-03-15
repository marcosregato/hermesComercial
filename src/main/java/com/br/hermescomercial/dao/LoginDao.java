package com.br.hermescomercial.dao;

import com.br.hermescomercial.connectionDB.ConnectionBD;
import com.br.hermescomercial.model.Usuario;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class LoginDao {

    private static final Logger logger = LogManager.getLogger(LoginDao.class);

    public Usuario acessarUsuario(String login, String senha) {
        ConnectionBD con = new ConnectionBD();
        Usuario usuario = null;
        
        String query = "SELECT u.id, u.nome, u.endereco, u.bairro, u.cidade, u.estado, u.cep, u.cnpj, u.cpf, u.email, u.tipoUsuario " +
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
                    usuario.setTipousuario(rs.getString("tipoUsuario"));
                }
            }
        } catch (Exception e) {
            logger.error("Erro ao acessar usuário", e);
        }
        return usuario;
    }
}
