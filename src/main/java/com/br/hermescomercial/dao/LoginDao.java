package com.br.hermescomercial.dao;

import com.br.hermescomercial.connectionDB.ConnectionBD;
import com.br.hermescomercial.model.Pessoa;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.List;

public class LoginDao {

    private ConnectionBD con = new ConnectionBD();
    private static final Logger logger = LogManager.getLogger(LoginDao.class);


//    private Estoque mapResultSetToLogin(ResultSet rs) throws SQLException {
//        Login login = new Login();
//        login.setId(rs.getLong("id"));
//        login.setQuantidade(rs.getString("quantidade"));
//        login.setMaximo(rs.getInt("maximo"));
//        login.setMinimo(rs.getInt("minimo"));
//        return login;
//    }

    public Pessoa acessarPessoa(String login, String senha) {
       
        Pessoa pessoa = null;

        String query = "SELECT p.id, p.nome, p.endereco, p.bairro, p.cidade, p.estado, p.cep, p.cnpj, p.cpf, p.email, p.tipoUsuario " +
                       "FROM login l " +
                       "INNER JOIN pessoa p ON l.fk_pessoa = p.id " +
                       "WHERE l.login = ? AND l.senha = ?";

        try (PreparedStatement ps = con.getConnection("Postgres").prepareStatement(query)) {
            ps.setString(1, login);
            ps.setString(2, senha);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    pessoa = new Pessoa();
                    pessoa.setId(rs.getLong("id"));
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
    
    public void salvar(Pessoa pessoa) {
        String query = "INSERT INTO pessoa (nome, endereco, bairro, cidade, estado, cep, cnpj, cpf, email, tipousuario) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement ps = con.getConnection("Postgres").prepareStatement(query)) {
            ps.setString(1, pessoa.getNome());
            ps.setString(2, pessoa.getEndereco());
            ps.setString(3, pessoa.getBairro());
            ps.setString(4, pessoa.getCidade());
            ps.setString(5,pessoa.getEstado());
            ps.setString(6,pessoa.getCep());
            ps.setString(7,pessoa.getCnpj());
            ps.setString(8,pessoa.getCpf());
            ps.setString(9,pessoa.getEmail());
            ps.setString(10,pessoa.getTipoPessoa());
            ps.executeUpdate();
        } catch (Exception e) { 
            logger.error("Erro ao salvar pessoa: " + e.getMessage());

        }
    }
    
    public void remove(String nome){
        String query = "DELETE FROM pessoa WHERE nome=?";
        try (PreparedStatement ps = con.getConnection("Postgres").prepareStatement(query)) {
            ps.setString(1, nome);
            ps.executeUpdate();
        } catch (Exception e) {
            logger.error("Erro ao remover pessoa: " + e.getMessage());

        }
    }
    
    public void update(Pessoa pessoa){
        String query = "UPDATE pessoa SET nome = ?, endereco = ?, bairro = ?, cidade = ?, estado = ?, cep = ?, cnpj = ?, cpf = ?, email = ?, tipousuario = ? WHERE id = ?";
        try (PreparedStatement ps = con.getConnection("Postgres").prepareStatement(query)) {
            ps.setString(1, pessoa.getNome());
            ps.setString(2, pessoa.getEndereco());
            ps.setString(3, pessoa.getBairro());
            ps.setString(4, pessoa.getCidade());
            ps.setString(5, pessoa.getEstado());
            ps.setString(6, pessoa.getCep());
            ps.setString(7, pessoa.getCnpj());
            ps.setString(8, pessoa.getCpf());
            ps.setString(9, pessoa.getEmail());
            ps.setString(10, pessoa.getTipoPessoa());
            ps.setLong(11, pessoa.getId());
            ps.executeUpdate();
        } catch (Exception e) {
            logger.error("Erro ao atualizar pessoa: " + e.getMessage());

        }
    }
    
    public List<Pessoa> listar(){
        String query = "SELECT * FROM pessoa";
        List<Pessoa> lista = new java.util.ArrayList<>();
        try (PreparedStatement ps = con.getConnection("Postgres").prepareStatement(query);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Pessoa pessoa = new Pessoa();
                pessoa.setId(rs.getLong("id"));
                pessoa.setNome(rs.getString("nome"));
                pessoa.setEndereco(rs.getString("endereco"));
                pessoa.setBairro(rs.getString("bairro"));
                pessoa.setCidade(rs.getString("cidade"));
                pessoa.setEstado(rs.getString("estado"));
                pessoa.setCep(rs.getString("cep"));
                pessoa.setCnpj(rs.getString("cnpj"));
                pessoa.setCpf(rs.getString("cpf"));
                pessoa.setEmail(rs.getString("email"));
                pessoa.setTipoUsuario(rs.getString("tipousuario"));
                lista.add(pessoa);
            }
        } catch (Exception e) {
            logger.error("Erro ao listar pessoa: " + e.getMessage());
        }
        return lista;
    }

}
