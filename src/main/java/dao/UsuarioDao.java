package dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import Repository.RepositoryUsuario;
import connectionDB.ConnectionMySQL;
import model.Cliente;
import model.Usuario;

public class UsuarioDao implements RepositoryUsuario {

	private ConnectionMySQL con = null;
	private Statement smt = null;
	private ResultSet rs = null;


	@Override
	public void salvar(Usuario usuario) {
		try {

			con  = new ConnectionMySQL();
			String query ="INSERT INTO cliente (id, nome, subproduto, codigo, datacompra, codigoncm) VALUES (NULL, null, ?, ?, ?, ?)";
			PreparedStatement ps = con.connection().prepareStatement(query);

			ps.setString(1, usuario.getNome());
			ps.setString(2, usuario.getEndereco());
			ps.setString(4, usuario.getCpf());

			ps.executeUpdate();
			ps.close();

		} catch (Exception e) {
			// TODO: handle exception
			System.out.println(e.getMessage());
		}

	}

	@Override
	public void remove(String nome) {
		try {

			con  = new ConnectionMySQL();
			String query = "delete from login l " +
					"inner join acesso a on l.id = a.fk_login " +
					"inner join usuario u on u.id = a.fK_usuario where u.nome = ?";
			PreparedStatement ps = con.connection().prepareStatement(query);
			ps.setString(1, nome);
			ps.executeUpdate();
			rs.close();
			ps.close();

		} catch (Exception e) {
			// TODO: handle exception
			System.out.println(e.getMessage());
		}
	}

	@Override
	public void update(Usuario usuario) {
		try {
			con  = new ConnectionMySQL();
			String query = "update cliente set nome = ?,subproduto = ? ,codigo = ?, datacompra = ?, codigoncm =?";
			PreparedStatement ps = con.connection().prepareStatement(query);
			ps.setString(1, usuario.getNome());
			ps.setString(3, usuario.getCpf());

			rs.close();
			ps.close();
		} catch (Exception e) {
			// TODO: handle exception
			System.out.println(e.getMessage());
		}
	}

	@Override
	public List<Usuario> lista() {
		try {

			String query = "select u.nome, u.endereco , u.cnjp ,u.cpf ,u.email ,u.tipo from login l " +
					"inner join acesso a on l.id = a.fk_login " +
					"inner join usuario u on u.id = a.fK_usuario ";
			PreparedStatement ps = con.connection().prepareStatement(query);
			List<Usuario> lista = new ArrayList<>();

			rs = ps.executeQuery();
			Usuario usuario = null;
			if (rs.next()) {

				usuario = new Usuario();
				usuario.setNome(rs.getString("nome"));
				usuario.setEndereco(rs.getString("endereco"));
				usuario.setCnjp(rs.getString("cnpj"));
				usuario.setCpf(rs.getString("cpf"));
				usuario.setEmail(rs.getString("email"));
				usuario.setTipousuario(rs.getString("tipo"));

				lista.add(usuario);

			}

			rs.close();
			ps.close();
			return lista;
		} catch (Exception e) {
			// TODO: handle exception
			System.out.println(e.getMessage());
		}	
		return null;
	}

	public List<Usuario> buscar(String nome) {
		try {

			String query = "select u.nome, u.endereco , u.cnjp ,u.cpf ,u.email ,u.tipo from login l " +
					"inner join acesso a on l.id = a.fk_login " +
					"inner join usuario u on u.id = a.fK_usuario where u.nome = ?";
			PreparedStatement ps = con.connection().prepareStatement(query);
			ps.setString(1, nome);

			List<Usuario> lista = new ArrayList<>();

			rs = ps.executeQuery();
			Usuario usuario = null;
			if (rs.next()) {

				usuario = new Usuario();
				usuario.setNome(rs.getString("nome"));
				usuario.setEndereco(rs.getString("endereco"));
				usuario.setCnjp(rs.getString("cnpj"));
				usuario.setCpf(rs.getString("cpf"));
				usuario.setEmail(rs.getString("email"));
				usuario.setTipousuario(rs.getString("tipo"));

				lista.add(usuario);

			}

			rs.close();
			ps.close();
			return lista;

		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		return null;
	}


}
