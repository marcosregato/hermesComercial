package com.br.hermescomercial.dao;

import com.br.hermescomercial.connectionDB.ConnectionBD;
import com.br.hermescomercial.model.Usuario;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class UsuarioDao {
    
    private static final org.apache.logging.log4j.Logger logger = org.apache.logging.log4j.LogManager.getLogger(UsuarioDao.class);
    private ConnectionBD con = new ConnectionBD();


    private Usuario mapResultSetToUsuario(ResultSet rs) throws SQLException {
        Usuario usuario = new Usuario();
        usuario.setId(rs.getLong("id"));
        usuario.setNome(rs.getString("nome"));
        usuario.setEndereco(rs.getString("endereco"));
        usuario.setBairro(rs.getString("bairro"));
        usuario.setCidade(rs.getString("cidade"));
        usuario.setEstado(rs.getString("estado"));
        usuario.setCep(rs.getString("cep"));
        usuario.setTipoDocumento(rs.getString("tipoDocumento"));
        usuario.setNumeroDocumetn(rs.getString("numeroDocumento"));
        usuario.setWhastsapp(rs.getString("whatsapp"));
        usuario.setTelefone(rs.getString("telefone"));
        usuario.setEmail(rs.getString("email"));
        usuario.setTipousuario(rs.getString("tipoUsuario"));
        return usuario;
    }

    public void salvar(Usuario usuario) {
        String sql = "INSERT INTO USUARIO (nome, endereco, bairro, cidade, estado, cep, tipoDocumento, numeroDocumento, whatsapp, telefone, email, tipoUsuario) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement ps = con.getConnection("Postgres").prepareStatement(sql)) {
            ps.setString(1, usuario.getNome());
            ps.setString(2, usuario.getEndereco());
            ps.setString(3, usuario.getBairro());
            ps.setString(4, usuario.getCidade());
            ps.setString(5, usuario.getEstado());
            ps.setString(6, usuario.getCep());
            ps.setString(7, usuario.getTipoDocumento());
            ps.setString(8, usuario.getNumeroDocumetn());
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
        String sql = "UPDATE USUARIO SET endereco=?, bairro=?, cidade=?, estado=?, cep=?, tipoDocumento=?, numeroDocumento=?, whatsapp=?, telefone=?, email=?, tipoUsuario=? WHERE nome=?";
        try (PreparedStatement ps = con.getConnection("Postgres").prepareStatement(sql)) {
            ps.setString(1, usuario.getEndereco());
            ps.setString(2, usuario.getBairro());
            ps.setString(3, usuario.getCidade());
            ps.setString(4, usuario.getEstado());
            ps.setString(5, usuario.getCep());
            ps.setString(6, usuario.getTipoDocumento());
            ps.setString(7, usuario.getNumeroDocumetn());
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
        try (PreparedStatement ps = con.getConnection("Postgres").prepareStatement(sql)) {
            ps.setString(1, nome);
            ps.executeUpdate();
        } catch (Exception e) {
            logger.error("Erro ao remover usuario: " + e.getMessage());
        }
    }

    public List<Usuario> buscar(String nome) {
        String sql = "SELECT * FROM USUARIO WHERE nome LIKE ?";
        List<Usuario> lista = new ArrayList<>();
        try (PreparedStatement ps = con.getConnection("Postgres").prepareStatement(sql)) {
            ps.setString(1, "%" + nome + "%");
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Usuario usuario = mapResultSetToUsuario(rs);
                    lista.add(usuario);
                }
            }
        } catch (Exception e) {
            logger.error("Erro ao buscar usuario: " + e.getMessage());
        }
        return lista;
    }

    public List<Usuario> lista() {
        String sql = "SELECT * FROM USUARIO";
        List<Usuario> lista = new ArrayList<>();
        try (PreparedStatement ps = con.getConnection("Postgres").prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                lista.add(mapResultSetToUsuario(rs));
            }
        } catch (Exception e) {
            logger.error("Erro ao listar usuarios: " + e.getMessage());
        }
        return lista;
    }
}
