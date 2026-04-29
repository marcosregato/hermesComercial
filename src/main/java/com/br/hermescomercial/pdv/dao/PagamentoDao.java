package com.br.hermescomercial.pdv.dao;

import com.br.hermescomercial.pdv.model.Pagamento;
import com.br.hermescomercial.connectionBD.ConnectionBD;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PagamentoDao {
    private static final Logger logger = LogManager.getLogger(PagamentoDao.class.getName());

    public boolean salvar(Pagamento pagamento) throws SQLException {
        String sql = "INSERT INTO PAGAMENTO (FK_VENDA_PDV, TIPO_PAGAMENTO, VALOR_PAGO, VALOR_TROCO, " +
                    "DATA_PAGAMENTO, STATUS, BANDEIRA_CARTAO, NUMERO_PARCELAS, CHAVE_PIX, OBSERVACOES) " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = ConnectionBD.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setLong(1, pagamento.getFkVendaPdv());
            stmt.setString(2, pagamento.getTipoPagamento());
            stmt.setBigDecimal(3, pagamento.getValorPago());
            stmt.setBigDecimal(4, pagamento.getValorTroco());
            stmt.setTimestamp(5, Timestamp.valueOf(pagamento.getDataPagamento()));
            stmt.setString(6, pagamento.getStatus());
            stmt.setString(7, pagamento.getBandeiraCartao());
            stmt.setString(8, pagamento.getNumeroParcelas());
            stmt.setString(9, pagamento.getChavePix());
            stmt.setString(10, pagamento.getObservacoes());

            int resultado = stmt.executeUpdate();

            if (resultado > 0) {
                try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        pagamento.setId(generatedKeys.getLong(1));
                    }
                }
                return true;
            }
        } catch (SQLException e) {
            logger.error("Erro ao salvar pagamento: " + e.getMessage(), e);
            throw e;
        }
        return false;
    }

    public boolean update(Pagamento pagamento) throws SQLException {
        String sql = "UPDATE PAGAMENTO SET TIPO_PAGAMENTO = ?, VALOR_PAGO = ?, VALOR_TROCO = ?, " +
                    "STATUS = ?, BANDEIRA_CARTAO = ?, NUMERO_PARCELAS = ?, CHAVE_PIX = ?, " +
                    "OBSERVACOES = ? WHERE ID = ?";

        try (Connection conn = ConnectionBD.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, pagamento.getTipoPagamento());
            stmt.setBigDecimal(2, pagamento.getValorPago());
            stmt.setBigDecimal(3, pagamento.getValorTroco());
            stmt.setString(4, pagamento.getStatus());
            stmt.setString(5, pagamento.getBandeiraCartao());
            stmt.setString(6, pagamento.getNumeroParcelas());
            stmt.setString(7, pagamento.getChavePix());
            stmt.setString(8, pagamento.getObservacoes());
            stmt.setLong(9, pagamento.getId());

            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            logger.error("Erro ao atualizar pagamento: " + e.getMessage(), e);
            throw e;
        }
    }

    public boolean remove(String id) throws SQLException {
        String sql = "DELETE FROM PAGAMENTO WHERE ID = ?";

        try (Connection conn = ConnectionBD.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, id);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            logger.error("Erro ao remover pagamento: " + e.getMessage(), e);
            throw e;
        }
    }

    public List<Pagamento> listar() throws SQLException {
        String sql = "SELECT * FROM PAGAMENTO ORDER BY DATA_PAGAMENTO DESC";
        List<Pagamento> pagamentos = new ArrayList<>();

        try (Connection conn = ConnectionBD.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                pagamentos.add(criarPagamentoDoResultSet(rs));
            }
        } catch (SQLException e) {
            logger.error("Erro ao listar pagamentos: " + e.getMessage(), e);
            throw e;
        }
        return pagamentos;
    }

    public List<Pagamento> buscarPorVenda(Long fkVendaPdv) throws SQLException {
        String sql = "SELECT * FROM PAGAMENTO WHERE FK_VENDA_PDV = ? ORDER BY DATA_PAGAMENTO";
        List<Pagamento> pagamentos = new ArrayList<>();

        try (Connection conn = ConnectionBD.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setLong(1, fkVendaPdv);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    pagamentos.add(criarPagamentoDoResultSet(rs));
                }
            }
        } catch (SQLException e) {
            logger.error("Erro ao buscar pagamentos da venda: " + e.getMessage(), e);
            throw e;
        }
        return pagamentos;
    }

    public List<Pagamento> buscarPorTipo(String tipoPagamento) throws SQLException {
        String sql = "SELECT * FROM PAGAMENTO WHERE TIPO_PAGAMENTO = ? ORDER BY DATA_PAGAMENTO DESC";
        List<Pagamento> pagamentos = new ArrayList<>();

        try (Connection conn = ConnectionBD.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, tipoPagamento);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    pagamentos.add(criarPagamentoDoResultSet(rs));
                }
            }
        } catch (SQLException e) {
            logger.error("Erro ao buscar pagamentos por tipo: " + e.getMessage(), e);
            throw e;
        }
        return pagamentos;
    }

    public List<Pagamento> buscarPorPeriodo(Date dataInicio, Date dataFim) throws SQLException {
        String sql = "SELECT * FROM PAGAMENTO WHERE DATA_PAGAMENTO BETWEEN ? AND ? ORDER BY DATA_PAGAMENTO DESC";
        List<Pagamento> pagamentos = new ArrayList<>();

        try (Connection conn = ConnectionBD.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setTimestamp(1, new Timestamp(dataInicio.getTime()));
            stmt.setTimestamp(2, new Timestamp(dataFim.getTime()));

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    pagamentos.add(criarPagamentoDoResultSet(rs));
                }
            }
        } catch (SQLException e) {
            logger.error("Erro ao buscar pagamentos por período: " + e.getMessage(), e);
            throw e;
        }
        return pagamentos;
    }

    public Pagamento buscarPorId(Long id) throws SQLException {
        String sql = "SELECT * FROM PAGAMENTO WHERE ID = ?";

        try (Connection conn = ConnectionBD.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setLong(1, id);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return criarPagamentoDoResultSet(rs);
                }
            }
        } catch (SQLException e) {
            logger.error("Erro ao buscar pagamento por ID: " + e.getMessage(), e);
            throw e;
        }
        return null;
    }

    public BigDecimal buscarTotalPorTipo(String tipoPagamento) throws SQLException {
        String sql = "SELECT COALESCE(SUM(VALOR_PAGO), 0) as total FROM PAGAMENTO WHERE TIPO_PAGAMENTO = ? AND STATUS = 'APROVADO'";

        try (Connection conn = ConnectionBD.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, tipoPagamento);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getBigDecimal("total");
                }
            }
        } catch (SQLException e) {
            logger.error("Erro ao buscar total por tipo de pagamento: " + e.getMessage(), e);
            throw e;
        }
        return BigDecimal.ZERO;
    }

    public int buscarQuantidadePorTipo(String tipoPagamento) throws SQLException {
        String sql = "SELECT COUNT(*) as quantidade FROM PAGAMENTO WHERE TIPO_PAGAMENTO = ? AND STATUS = 'APROVADO'";

        try (Connection conn = ConnectionBD.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, tipoPagamento);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("quantidade");
                }
            }
        } catch (SQLException e) {
            logger.error("Erro ao buscar quantidade por tipo de pagamento: " + e.getMessage(), e);
            throw e;
        }
        return 0;
    }

    private Pagamento criarPagamentoDoResultSet(ResultSet rs) throws SQLException {
        Pagamento pagamento = new Pagamento();
        pagamento.setId(rs.getLong("ID"));
        pagamento.setFkVendaPdv(rs.getLong("FK_VENDA_PDV"));
        pagamento.setTipoPagamento(rs.getString("TIPO_PAGAMENTO"));
        pagamento.setValorPago(rs.getBigDecimal("VALOR_PAGO"));
        pagamento.setValorTroco(rs.getBigDecimal("VALOR_TROCO"));
        pagamento.setDataPagamento(rs.getTimestamp("DATA_PAGAMENTO") != null ? 
                rs.getTimestamp("DATA_PAGAMENTO").toLocalDateTime() : null);
        pagamento.setStatus(rs.getString("STATUS"));
        pagamento.setBandeiraCartao(rs.getString("BANDEIRA_CARTAO"));
        pagamento.setNumeroParcelas(rs.getString("NUMERO_PARCELAS"));
        pagamento.setChavePix(rs.getString("CHAVE_PIX"));
        pagamento.setObservacoes(rs.getString("OBSERVACOES"));
        return pagamento;
    }
}
