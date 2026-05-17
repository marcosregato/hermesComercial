package com.br.hermescomercial.dao;

import com.br.hermescomercial.model.Atributo;
import com.br.hermescomercial.connectionBD.ConnectionBD;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class AtributoDao {

    private static final Logger logger = LogManager.getLogger(AtributoDao.class);

    public void salvar(Atributo atributo) {
        try {
            String query = "INSERT INTO atributo (nome, descricao, tipo, valor, imposto_federal, imposto_estadual, imposto_municipal) VALUES (?, ?, ?, ?, ?, ?, ?)";
            PreparedStatement ps = ConnectionBD.getConnection().prepareStatement(query);
            
            ps.setString(1, atributo.getNome());
            ps.setString(2, atributo.getDescricao());
            ps.setString(3, atributo.getTipo());
            ps.setDouble(4, atributo.getValor());
            ps.setFloat(5, atributo.getImpostoFederal());
            ps.setFloat(6, atributo.getImpostoEstadual());
            ps.setFloat(7, atributo.getImpostoMunicipal());
            
            ps.executeUpdate();
            ps.close();
            logger.info("Atributo salvo com sucesso: " + atributo.getNome());
        } catch (Exception e) {
            logger.error("Erro ao salvar atributo: " + e.getMessage(), e);
        }
    }

    public void remove(String nome) {
        try {
            String query = "DELETE FROM atributo WHERE nome = ?";
            PreparedStatement ps = ConnectionBD.getConnection().prepareStatement(query);
            ps.setString(1, nome);
            ps.executeUpdate();
            ps.close();
            logger.info("Atributo removido com sucesso: " + nome);
        } catch (Exception e) {
            logger.error("Erro ao remover atributo: " + e.getMessage(), e);
        }
    }

    public void update(Atributo atributo) {
        try {
            String query = "UPDATE atributo SET descricao = ?, tipo = ?, valor = ?, imposto_federal = ?, imposto_estadual = ?, imposto_municipal = ? WHERE nome = ?";
            PreparedStatement ps = ConnectionBD.getConnection().prepareStatement(query);
            
            ps.setString(1, atributo.getDescricao());
            ps.setString(2, atributo.getTipo());
            ps.setDouble(3, atributo.getValor());
            ps.setFloat(4, atributo.getImpostoFederal());
            ps.setFloat(5, atributo.getImpostoEstadual());
            ps.setFloat(6, atributo.getImpostoMunicipal());
            ps.setString(7, atributo.getNome());
            
            ps.executeUpdate();
            ps.close();
            logger.info("Atributo atualizado com sucesso: " + atributo.getNome());
        } catch (Exception e) {
            logger.error("Erro ao atualizar atributo: " + e.getMessage(), e);
        }
    }

    public List<Atributo> listar() {
        List<Atributo> lista = new ArrayList<>();
        try {
            String query = "SELECT * FROM atributo";
            PreparedStatement ps = ConnectionBD.getConnection().prepareStatement(query);
            ResultSet rs = ps.executeQuery();
            
            while (rs.next()) {
                Atributo atributo = new Atributo();
                atributo.setId(rs.getLong("id"));
                atributo.setNome(rs.getString("nome"));
                atributo.setDescricao(rs.getString("descricao"));
                atributo.setTipo(rs.getString("tipo"));
                atributo.setValor(rs.getDouble("valor"));
                atributo.setImpostoFederal(rs.getFloat("imposto_federal"));
                atributo.setImpostoEstadual(rs.getFloat("imposto_estadual"));
                atributo.setImpostoMunicipal(rs.getFloat("imposto_municipal"));
                lista.add(atributo);
            }
            
            rs.close();
            ps.close();
        } catch (Exception e) {
            logger.error("Erro ao listar atributos: " + e.getMessage(), e);
        }
        return lista;
    }

    public List<Atributo> buscar(String nome) {
        List<Atributo> lista = new ArrayList<>();
        try {
            String query = "SELECT * FROM atributo WHERE nome LIKE ?";
            PreparedStatement ps = ConnectionBD.getConnection().prepareStatement(query);
            ps.setString(1, "%" + nome + "%");
            ResultSet rs = ps.executeQuery();
            
            while (rs.next()) {
                Atributo atributo = new Atributo();
                atributo.setId(rs.getLong("id"));
                atributo.setNome(rs.getString("nome"));
                atributo.setDescricao(rs.getString("descricao"));
                atributo.setTipo(rs.getString("tipo"));
                atributo.setValor(rs.getDouble("valor"));
                atributo.setImpostoFederal(rs.getFloat("imposto_federal"));
                atributo.setImpostoEstadual(rs.getFloat("imposto_estadual"));
                atributo.setImpostoMunicipal(rs.getFloat("imposto_municipal"));
                lista.add(atributo);
            }
            
            rs.close();
            ps.close();
        } catch (Exception e) {
            logger.error("Erro ao buscar atributos: " + e.getMessage(), e);
        }
        return lista;
    }
}
