/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.br.hermescomercial.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.br.hermescomercial.Repository.RepositoryCusto;
import com.br.hermescomercial.connectionDB.ConnectionBD;
import com.br.hermescomercial.model.Venda;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 *
 * @author marcos
 */
public class VendaDao implements RepositoryCusto {

    private ConnectionBD con = new ConnectionBD();
    private static final Logger logger = LogManager.getLogger(VendaDao.class);

    private Venda mapResultSetToVenda(ResultSet rs) throws SQLException {
        Venda venda = new Venda();
        venda.setId(rs.getLong("id"));
        venda.setPrecoCusto(rs.getFloat("custounitario"));
        venda.setCustoVenda(rs.getFloat("custototal"));
        return venda;
    }

    @Override
    public void salvar(Venda venda) {
        String query = "INSERT INTO custo (custounitario, custototal) VALUES (?, ?)";
        try (PreparedStatement ps = con.getConnection("").prepareStatement(query)) {
            ps.setFloat(1, venda.getPrecoCusto());
            ps.setFloat(2, venda.getCustoVenda());
            ps.executeUpdate();
        } catch (Exception e) {
            logger.error("Erro ao salvar venda: " + e.getMessage(), e);
        }
    }

    @Override
    public void remove(String nome) {
        String query = "DELETE FROM custo WHERE id=?";
        try (PreparedStatement ps = con.getConnection("").prepareStatement(query)) {
            ps.setString(1, nome);
            ps.executeUpdate();
        } catch (Exception e) {
            logger.error("Erro ao remover venda: " + e.getMessage(), e);
        }
    }

    @Override
    public void update(Venda venda) {
        String query = "UPDATE custo SET custounitario = ?, custototal = ? WHERE id = ?";
        try (PreparedStatement ps = con.getConnection("").prepareStatement(query)) {
            ps.setFloat(1, venda.getPrecoCusto());
            ps.setFloat(2, venda.getCustoVenda());
            ps.setLong(3, venda.getId());
            ps.executeUpdate();
        } catch (Exception e) {
            logger.error("Erro ao atualizar venda: " + e.getMessage(), e);
        }
    }

    @Override
    public List<Venda> listar() {
        String query = "SELECT * FROM custo";
        List<Venda> lista = new ArrayList<>();
        try (PreparedStatement ps = con.getConnection("").prepareStatement(query);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                lista.add(mapResultSetToVenda(rs));
            }
            return lista;
        } catch (Exception e) {
            logger.error("Erro ao listar vendas: " + e.getMessage(), e);
        }
        return Collections.emptyList();
    }

    @Override
    public List<Venda> buscar(String nome) {
        String query = "SELECT * FROM custo WHERE id = ?";
        List<Venda> lista = new ArrayList<>();
        try (PreparedStatement ps = con.getConnection("").prepareStatement(query)) {
            ps.setString(1, nome);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    lista.add(mapResultSetToVenda(rs));
                }
            }
            return lista;
        } catch (Exception e) {
            logger.error("Erro ao buscar venda: " + e.getMessage(), e);
        }
        return Collections.emptyList();
    }
}
