package com.br.hermescomercial.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.br.hermescomercial.connectionDB.ConnectionBD;
import com.br.hermescomercial.model.Usuario;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class LoginDao {

    private static final Logger logger = LogManager.getLogger(LoginDao.class);


	private ConnectionBD con = null;
	private final Statement smt = null;
	private ResultSet rs = null;

	public List<Usuario> infoUsuario(String login, String senha){
		try {
			con  = new ConnectionBD();

			String query ="SELECT u.nome ,u.endereco ,u.cnjp ,u.cpf ,u.email ,u.tipo  FROM login l" +
					"inner join acesso a on l.id = a.fK_login" +
					"inner JOIN usuario u on u.id = a.fK_usuario where l.login = ? and l.senha = ? ";

			PreparedStatement ps = con.getConnection("").prepareStatement(query);
			ps.setString(1, login);
			ps.setString(2, senha);
			
			List<Usuario> lista = new ArrayList<>();

			rs = ps.executeQuery();
			Usuario usuario = null;
			if (rs.next()) {

				usuario = new Usuario();
				usuario.setNome(rs.getString("nome"));
				usuario.setEndereco(rs.getString("endereco"));
				usuario.setCnpj(rs.getString("cnpj"));
				usuario.setCpf(rs.getString("cpf"));
				usuario.setEmail(rs.getString("email"));
				usuario.setTipousuario(rs.getString("tipo"));
			}

			lista.add(usuario);
			rs.close();
			ps.close();
			return lista;

		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		//con.close();
		return Collections.emptyList();
	}
	
	
	public List<Usuario> acessarUsuario(String login, String senha){
		try {
			con  = new ConnectionBD();

			String query ="SELECT u.tipo  FROM login l" +
					"inner join acesso a on l.id = a.fK_login" +
					"inner JOIN usuario u on u.id = a.fK_usuario where l.login = ? and l.senha = ? ";

			PreparedStatement ps = con.getConnection("").prepareStatement(query);
			ps.setString(1, login);
			ps.setString(2, senha);
			
			List<Usuario> lista = new ArrayList<>();

			rs = ps.executeQuery();
			Usuario usuario = null;
			if (rs.next()) {

				usuario = new Usuario();
				usuario.setNome(rs.getString("nome"));
				usuario.setEndereco(rs.getString("endereco"));
				usuario.setCnpj(rs.getString("cnpj"));
				usuario.setCpf(rs.getString("cpf"));
				usuario.setEmail(rs.getString("email"));
				usuario.setTipousuario(rs.getString("tipo"));

			}

			lista.add(usuario);
			rs.close();
			ps.close();
			return lista;

		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		//con.close();
		return Collections.emptyList();
	}
}
