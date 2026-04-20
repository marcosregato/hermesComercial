package com.br.hermescomercial.dao;

import com.br.hermescomercial.model.Cliente;
import com.br.hermescomercial.connectionBD.ConnectionBD;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ClienteDao {
    private static final Logger logger = LogManager.getLogger(ClienteDao.class.getName());

    public boolean salvar(Cliente cliente) throws SQLException {
        String sql = "INSERT INTO USUARIO (NOME, TIPO_USUARIO, TIPO_PESSOA, CPF, RG, DATA_NASCIMENTO, CNPJ, " +
                    "NOME_FANTASIA, INSCRICAO_ESTADUAL, TELEFONE, CELULAR, EMAIL, CEP, ENDERECO, " +
                    "NUMERO, COMPLEMENTO, BAIRRO, CIDADE, ESTADO, OBSERVACOES, ATIVO) " +
                    "VALUES (?, 'CLIENTE', ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = ConnectionBD.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, cliente.getNome());
            stmt.setString(2, cliente.getTipoPessoa());
            stmt.setString(3, cliente.getCpf());
            stmt.setString(4, cliente.getRg());
            stmt.setString(5, cliente.getDataNascimento());
            stmt.setString(6, cliente.getCnpj());
            stmt.setString(7, cliente.getNomeFantasia());
            stmt.setString(8, cliente.getInscricaoEstadual());
            stmt.setString(9, cliente.getTelefone());
            stmt.setString(10, cliente.getCelular());
            stmt.setString(11, cliente.getEmail());
            stmt.setString(12, cliente.getCep());
            stmt.setString(13, cliente.getEndereco());
            stmt.setString(14, cliente.getNumero());
            stmt.setString(15, cliente.getComplemento());
            stmt.setString(16, cliente.getBairro());
            stmt.setString(17, cliente.getCidade());
            stmt.setString(18, cliente.getEstado());
            stmt.setString(19, cliente.getObservacoes());
            stmt.setBoolean(20, cliente.isAtivo());

            int resultado = stmt.executeUpdate();

            if (resultado > 0) {
                try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        cliente.setId(generatedKeys.getLong(1));
                    }
                }
                return true;
            }
        } catch (SQLException e) {
            logger.error("Erro ao salvar cliente: " + e.getMessage(), e);
            throw e;
        }
        return false;
    }

    public boolean update(Cliente cliente) throws SQLException {
        String sql = "UPDATE USUARIO SET NOME = ?, TIPO_PESSOA = ?, CPF = ?, RG = ?, DATA_NASCIMENTO = ?, " +
                    "CNPJ = ?, NOME_FANTASIA = ?, INSCRICAO_ESTADUAL = ?, TELEFONE = ?, CELULAR = ?, " +
                    "EMAIL = ?, CEP = ?, ENDERECO = ?, NUMERO = ?, COMPLEMENTO = ?, BAIRRO = ?, " +
                    "CIDADE = ?, ESTADO = ?, OBSERVACOES = ?, ATIVO = ?, DATA_ATUALIZACAO = CURRENT_TIMESTAMP " +
                    "WHERE ID = ? AND TIPO_USUARIO = 'CLIENTE'";

        try (Connection conn = ConnectionBD.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, cliente.getNome());
            stmt.setString(2, cliente.getTipoPessoa());
            stmt.setString(3, cliente.getCpf());
            stmt.setString(4, cliente.getRg());
            stmt.setString(5, cliente.getDataNascimento());
            stmt.setString(6, cliente.getCnpj());
            stmt.setString(7, cliente.getNomeFantasia());
            stmt.setString(8, cliente.getInscricaoEstadual());
            stmt.setString(9, cliente.getTelefone());
            stmt.setString(10, cliente.getCelular());
            stmt.setString(11, cliente.getEmail());
            stmt.setString(12, cliente.getCep());
            stmt.setString(13, cliente.getEndereco());
            stmt.setString(14, cliente.getNumero());
            stmt.setString(15, cliente.getComplemento());
            stmt.setString(16, cliente.getBairro());
            stmt.setString(17, cliente.getCidade());
            stmt.setString(18, cliente.getEstado());
            stmt.setString(19, cliente.getObservacoes());
            stmt.setBoolean(20, cliente.isAtivo());
            stmt.setLong(21, cliente.getId());

            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            logger.error("Erro ao atualizar cliente: " + e.getMessage(), e);
            throw e;
        }
    }

    public boolean remove(String id) throws SQLException {
        String sql = "DELETE FROM USUARIO WHERE ID = ? AND TIPO_USUARIO = 'CLIENTE'";

        try (Connection conn = ConnectionBD.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, id);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            logger.error("Erro ao remover cliente: " + e.getMessage(), e);
            throw e;
        }
    }

    public List<Cliente> listar() {
        String sql = "SELECT * FROM USUARIO WHERE TIPO_USUARIO = 'CLIENTE' ORDER BY NOME";
        List<Cliente> clientes = new ArrayList<>();

        try (Connection conn = ConnectionBD.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                clientes.add(criarClienteDoResultSet(rs));
            }
        } catch (SQLException e) {
            logger.error("Erro ao listar clientes: " + e.getMessage(), e);
        }
        return clientes;
    }

    public List<Cliente> buscarComFiltros(String busca, boolean apenasAtivos, 
                                          boolean apenasFisica, boolean apenasJuridica) {
        StringBuilder sql = new StringBuilder("SELECT * FROM USUARIO WHERE TIPO_USUARIO = 'CLIENTE'");
        List<Object> parametros = new ArrayList<>();

        if (busca != null && !busca.trim().isEmpty()) {
            sql.append(" AND (NOME ILIKE ? OR CPF ILIKE ? OR CNPJ ILIKE ? OR EMAIL ILIKE ?)");
            String buscaParam = "%" + busca + "%";
            parametros.add(buscaParam);
            parametros.add(buscaParam);
            parametros.add(buscaParam);
            parametros.add(buscaParam);
        }

        if (apenasAtivos) {
            sql.append(" AND ATIVO = ?");
            parametros.add(true);
        }

        if (apenasFisica && !apenasJuridica) {
            sql.append(" AND TIPO_PESSOA = 'F'");
        } else if (apenasJuridica && !apenasFisica) {
            sql.append(" AND TIPO_PESSOA = 'J'");
        }

        sql.append(" ORDER BY NOME");

        List<Cliente> clientes = new ArrayList<>();

        try (Connection conn = ConnectionBD.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql.toString())) {

            for (int i = 0; i < parametros.size(); i++) {
                stmt.setObject(i + 1, parametros.get(i));
            }

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    clientes.add(criarClienteDoResultSet(rs));
                }
            }
        } catch (SQLException e) {
            logger.error("Erro ao buscar clientes com filtros: " + e.getMessage(), e);
        }
        return clientes;
    }

    public Cliente buscarPorId(Long id) throws SQLException {
        String sql = "SELECT * FROM USUARIO WHERE ID = ? AND TIPO_USUARIO = 'CLIENTE'";

        try (Connection conn = ConnectionBD.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setLong(1, id);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return criarClienteDoResultSet(rs);
                }
            }
        } catch (SQLException e) {
            logger.error("Erro ao buscar cliente por ID: " + e.getMessage(), e);
            throw e;
        }
        return null;
    }

    public Cliente buscarPorCPF(String cpf) throws SQLException {
        String sql = "SELECT * FROM USUARIO WHERE CPF = ? AND ATIVO = true AND TIPO_USUARIO = 'CLIENTE'";

        try (Connection conn = ConnectionBD.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, cpf);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return criarClienteDoResultSet(rs);
                }
            }
        } catch (SQLException e) {
            logger.error("Erro ao buscar cliente por CPF: " + e.getMessage(), e);
            throw e;
        }
        return null;
    }

    public Cliente buscarPorCNPJ(String cnpj) throws SQLException {
        String sql = "SELECT * FROM USUARIO WHERE CNPJ = ? AND ATIVO = true AND TIPO_USUARIO = 'CLIENTE'";

        try (Connection conn = ConnectionBD.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, cnpj);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return criarClienteDoResultSet(rs);
                }
            }
        } catch (SQLException e) {
            logger.error("Erro ao buscar cliente por CNPJ: " + e.getMessage(), e);
            throw e;
        }
        return null;
    }

    private Cliente criarClienteDoResultSet(ResultSet rs) throws SQLException {
        Cliente cliente = new Cliente();
        cliente.setId(rs.getLong("ID"));
        cliente.setNome(rs.getString("NOME"));
        cliente.setTipoPessoa(rs.getString("TIPO_PESSOA"));
        cliente.setCpf(rs.getString("CPF"));
        cliente.setRg(rs.getString("RG"));
        cliente.setDataNascimento(rs.getString("DATA_NASCIMENTO"));
        cliente.setCnpj(rs.getString("CNPJ"));
        cliente.setNomeFantasia(rs.getString("NOME_FANTASIA"));
        cliente.setInscricaoEstadual(rs.getString("INSCRICAO_ESTADUAL"));
        cliente.setTelefone(rs.getString("TELEFONE"));
        cliente.setCelular(rs.getString("CELULAR"));
        cliente.setEmail(rs.getString("EMAIL"));
        cliente.setCep(rs.getString("CEP"));
        cliente.setEndereco(rs.getString("ENDERECO"));
        cliente.setNumero(rs.getString("NUMERO"));
        cliente.setComplemento(rs.getString("COMPLEMENTO"));
        cliente.setBairro(rs.getString("BAIRRO"));
        cliente.setCidade(rs.getString("CIDADE"));
        cliente.setEstado(rs.getString("ESTADO"));
        cliente.setObservacoes(rs.getString("OBSERVACOES"));
        cliente.setAtivo(rs.getBoolean("ATIVO"));
        cliente.setDataCadastro(rs.getTimestamp("DATA_CADASTRO") != null ? 
            rs.getTimestamp("DATA_CADASTRO").toLocalDateTime() : null);
        cliente.setDataAtualizacao(rs.getTimestamp("DATA_ATUALIZACAO") != null ? 
            rs.getTimestamp("DATA_ATUALIZACAO").toLocalDateTime() : null);
        return cliente;
    }
}
