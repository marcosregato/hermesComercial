package com.br.hermescomercial.dao;

import com.br.hermescomercial.model.VendaPDV;
import com.br.hermescomercial.connectionBD.ConnectionBD;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class VendaDao {
    private static final Logger logger = LogManager.getLogger(VendaDao.class.getName());

    public boolean salvar(VendaPDV venda) throws SQLException {
        String sql = "INSERT INTO VENDA_PDV (NUMERO_CUPOM, FK_CLIENTE, FK_OPERADOR, FK_TERMINAL, " +
                    "DATA_VENDA, VALOR_TOTAL, VALOR_DESPESA, VALOR_DESCONTO, VALOR_FINAL, " +
                    "STATUS, OBSERVACOES) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = ConnectionBD.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, venda.getNumeroCupom());
            
            if (venda.getCliente() != null) {
                stmt.setLong(2, venda.getCliente().getId());
            } else {
                stmt.setNull(2, Types.BIGINT);
            }
            
            stmt.setLong(3, venda.getOperador().getId());
            stmt.setInt(4, venda.getTerminal());
            stmt.setTimestamp(5, Timestamp.valueOf(venda.getDataVenda()));
            stmt.setBigDecimal(6, venda.getValorTotal());
            stmt.setBigDecimal(7, venda.getValorDespesa());
            stmt.setBigDecimal(8, venda.getValorDesconto());
            stmt.setBigDecimal(9, venda.getValorFinal());
            stmt.setString(10, venda.getStatus());
            stmt.setString(11, venda.getObservacoes());

            int resultado = stmt.executeUpdate();

            if (resultado > 0) {
                try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        venda.setId(generatedKeys.getLong(1));
                    }
                }
                return true;
            }
        } catch (SQLException e) {
            logger.error("Erro ao salvar venda: " + e.getMessage(), e);
            throw e;
        }
        return false;
    }

    public boolean update(VendaPDV venda) throws SQLException {
        String sql = "UPDATE VENDA_PDV SET NUMERO_CUPOM = ?, FK_CLIENTE = ?, FK_OPERADOR = ?, " +
                    "FK_TERMINAL = ?, DATA_VENDA = ?, VALOR_TOTAL = ?, VALOR_DESPESA = ?, " +
                    "VALOR_DESCONTO = ?, VALOR_FINAL = ?, STATUS = ?, OBSERVACOES = ? WHERE ID = ?";

        try (Connection conn = ConnectionBD.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, venda.getNumeroCupom());
            
            if (venda.getCliente() != null) {
                stmt.setLong(2, venda.getCliente().getId());
            } else {
                stmt.setNull(2, Types.BIGINT);
            }
            
            stmt.setLong(3, venda.getOperador().getId());
            stmt.setInt(4, venda.getTerminal());
            stmt.setTimestamp(5, Timestamp.valueOf(venda.getDataVenda()));
            stmt.setBigDecimal(6, venda.getValorTotal());
            stmt.setBigDecimal(7, venda.getValorDespesa());
            stmt.setBigDecimal(8, venda.getValorDesconto());
            stmt.setBigDecimal(9, venda.getValorFinal());
            stmt.setString(10, venda.getStatus());
            stmt.setString(11, venda.getObservacoes());
            stmt.setLong(12, venda.getId());

            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            logger.error("Erro ao atualizar venda: " + e.getMessage(), e);
            throw e;
        }
    }

    public boolean remove(String id) throws SQLException {
        String sql = "DELETE FROM VENDA_PDV WHERE ID = ?";

        try (Connection conn = ConnectionBD.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, id);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            logger.error("Erro ao remover venda: " + e.getMessage(), e);
            throw e;
        }
    }

    public List<VendaPDV> listar() {
        String sql = "SELECT * FROM VENDA_PDV ORDER BY DATA_VENDA DESC";
        List<VendaPDV> vendas = new ArrayList<>();

        try (Connection conn = ConnectionBD.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                vendas.add(criarVendaDoResultSet(rs));
            }
        } catch (SQLException e) {
            logger.error("Erro ao listar vendas: " + e.getMessage(), e);
        }
        return vendas;
    }

    public List<VendaPDV> buscarPorPeriodo(Date dataInicio, Date dataFim) {
        String sql = "SELECT * FROM VENDA_PDV WHERE DATA_VENDA BETWEEN ? AND ? ORDER BY DATA_VENDA DESC";
        List<VendaPDV> vendas = new ArrayList<>();

        try (Connection conn = ConnectionBD.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setTimestamp(1, new Timestamp(dataInicio.getTime()));
            stmt.setTimestamp(2, new Timestamp(dataFim.getTime()));

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    vendas.add(criarVendaDoResultSet(rs));
                }
            }
        } catch (SQLException e) {
            logger.error("Erro ao buscar vendas por período: " + e.getMessage(), e);
        }
        return vendas;
    }

    public List<VendaPDV> buscarPorOperador(Long fkOperador) {
        String sql = "SELECT * FROM VENDA_PDV WHERE FK_OPERADOR = ? ORDER BY DATA_VENDA DESC";
        List<VendaPDV> vendas = new ArrayList<>();

        try (Connection conn = ConnectionBD.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setLong(1, fkOperador);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    vendas.add(criarVendaDoResultSet(rs));
                }
            }
        } catch (SQLException e) {
            logger.error("Erro ao buscar vendas do operador: " + e.getMessage(), e);
        }
        return vendas;
    }

    public List<VendaPDV> buscarPorStatus(String status) {
        String sql = "SELECT * FROM VENDA_PDV WHERE STATUS = ? ORDER BY DATA_VENDA DESC";
        List<VendaPDV> vendas = new ArrayList<>();

        try (Connection conn = ConnectionBD.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, status);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    vendas.add(criarVendaDoResultSet(rs));
                }
            }
        } catch (SQLException e) {
            logger.error("Erro ao buscar vendas por status: " + e.getMessage(), e);
        }
        return vendas;
    }

    public VendaPDV buscarPorId(Long id) throws SQLException {
        String sql = "SELECT * FROM VENDA_PDV WHERE ID = ?";

        try (Connection conn = ConnectionBD.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setLong(1, id);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return criarVendaDoResultSet(rs);
                }
            }
        } catch (SQLException e) {
            logger.error("Erro ao buscar venda por ID: " + e.getMessage(), e);
            throw e;
        }
        return null;
    }

    public VendaPDV buscarPorNumeroCupom(String numeroCupom) throws SQLException {
        String sql = "SELECT * FROM VENDA_PDV WHERE NUMERO_CUPOM = ?";

        try (Connection conn = ConnectionBD.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, numeroCupom);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return criarVendaDoResultSet(rs);
                }
            }
        } catch (SQLException e) {
            logger.error("Erro ao buscar venda por número do cupom: " + e.getMessage(), e);
            throw e;
        }
        return null;
    }

    public int buscarQuantidadePorPeriodo(Date dataInicio, Date dataFim) throws SQLException {
        String sql = "SELECT COUNT(*) as quantidade FROM VENDA_PDV WHERE DATA_VENDA BETWEEN ? AND ? AND STATUS = 'CONCLUIDA'";

        try (Connection conn = ConnectionBD.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setTimestamp(1, new Timestamp(dataInicio.getTime()));
            stmt.setTimestamp(2, new Timestamp(dataFim.getTime()));

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("quantidade");
                }
            }
        } catch (SQLException e) {
            logger.error("Erro ao buscar quantidade de vendas por período: " + e.getMessage(), e);
            throw e;
        }
        return 0;
    }

    public BigDecimal buscarTotalPorPeriodo(Date dataInicio, Date dataFim) throws SQLException {
        String sql = "SELECT COALESCE(SUM(VALOR_FINAL), 0) as total FROM VENDA_PDV WHERE DATA_VENDA BETWEEN ? AND ? AND STATUS = 'CONCLUIDA'";

        try (Connection conn = ConnectionBD.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setTimestamp(1, new Timestamp(dataInicio.getTime()));
            stmt.setTimestamp(2, new Timestamp(dataFim.getTime()));

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getBigDecimal("total");
                }
            }
        } catch (SQLException e) {
            logger.error("Erro ao buscar total de vendas por período: " + e.getMessage(), e);
            throw e;
        }
        return BigDecimal.ZERO;
    }

    public int buscarQuantidadeCanceladasPorPeriodo(Date dataInicio, Date dataFim) throws SQLException {
        String sql = "SELECT COUNT(*) as quantidade FROM VENDA_PDV WHERE DATA_VENDA BETWEEN ? AND ? AND STATUS = 'CANCELADA'";

        try (Connection conn = ConnectionBD.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setTimestamp(1, new Timestamp(dataInicio.getTime()));
            stmt.setTimestamp(2, new Timestamp(dataFim.getTime()));

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("quantidade");
                }
            }
        } catch (SQLException e) {
            logger.error("Erro ao buscar quantidade de vendas canceladas por período: " + e.getMessage(), e);
            throw e;
        }
        return 0;
    }

    private VendaPDV criarVendaDoResultSet(ResultSet rs) throws SQLException {
        VendaPDV venda = new VendaPDV();
        venda.setId(rs.getLong("ID"));
        venda.setNumeroCupom(rs.getString("NUMERO_CUPOM"));
        
        // Cliente será carregado separadamente se necessário
        Long fkCliente = rs.getLong("FK_CLIENTE");
        if (!rs.wasNull()) {
            // Aqui você poderia carregar o cliente se necessário
        }
        
        // Operador será carregado separadamente se necessário
        Long fkOperador = rs.getLong("FK_OPERADOR");
        if (!rs.wasNull()) {
            // Aqui você poderia carregar o operador se necessário
        }
        
        venda.setTerminal(rs.getInt("FK_TERMINAL"));
        venda.setDataVenda(rs.getTimestamp("DATA_VENDA") != null ? rs.getTimestamp("DATA_VENDA").toLocalDateTime() : null);
        venda.setValorTotal(rs.getBigDecimal("VALOR_TOTAL"));
        venda.setValorDespesa(rs.getBigDecimal("VALOR_DESPESA"));
        venda.setValorDesconto(rs.getBigDecimal("VALOR_DESCONTO"));
        venda.setValorFinal(rs.getBigDecimal("VALOR_FINAL"));
        venda.setStatus(rs.getString("STATUS"));
        venda.setObservacoes(rs.getString("OBSERVACOES"));
        
        return venda;
    }
}
