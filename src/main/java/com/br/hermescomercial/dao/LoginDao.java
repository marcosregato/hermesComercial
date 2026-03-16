package com.br.hermescomercial.dao;

import com.br.hermescomercial.connectionDB.ConnectionBD;
import com.br.hermescomercial.model.Pessoa;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class LoginDao {

    private static final Logger logger = LogManager.getLogger(LoginDao.class);

    public Pessoa acessarUsuario(String login, String senha) {
        ConnectionBD con = new ConnectionBD();
        Pessoa pessoa = null;
        
        String query = "SELECT u.id, p.nome, p.endereco, p.bairro, p.cidade, p.estado, p.cep, p.cnpj, p.cpf, p.email, u.tipoUsuario " +
                       "FROM login l " +
                       "INNER JOIN pessoa p ON l.fk_usuario = p.id " +
                       "WHERE l.login = ? AND l.senha = ?";

        try (PreparedStatement ps = con.getConnection("Postgres").prepareStatement(query)) {
            ps.setString(1, login);
            ps.setString(2, senha);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    pessoa = new Pessoa();
                    pessoa.setNome(rs.getString("nome"));
                    pessoa.setEndereco(rs.getString("endereco"));
                    pessoa.setBairro(rs.getString("bairro"));
                    pessoa.setCidade(rs.getString("cidade"));
                    pessoa.setEstado(rs.getString("estado"));
                    pessoa.setCep(rs.getString("cep"));
                    pessoa.setCnpj(rs.getString("cnpj"));
                    pessoa.setCpf(rs.getString("cpf"));
                    pessoa.setEmail(rs.getString("email"));
                    pessoa.setTipoUsuario(rs.getString("tipoUsuario"));
                }
            }
        } catch (Exception e) {
            logger.error("Erro ao acessar usuário", e);
        }
        return pessoa;
    }
}
