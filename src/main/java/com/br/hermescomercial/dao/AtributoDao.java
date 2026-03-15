package com.br.hermescomercial.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.br.hermescomercial.Repository.RepositoryAtributo;
import com.br.hermescomercial.connectionDB.ConnectionBD;
import com.br.hermescomercial.model.Atributo;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class AtributoDao implements RepositoryAtributo {

    private ConnectionBD con = null;
    private ResultSet rs = null;
    private static final Logger logger = LogManager.getLogger(AtributoDao.class);

    @Override
    public void salvar(Atributo atributo) {
        try {
            con = new ConnectionBD();
            String query = "INSERT INTO atributo (nome) VALUES (?)";
            PreparedStatement ps = con.getConnection("").prepareStatement(query);

            //ps.setString(1, atributo.getNome());

            ps.executeUpdate();
            ps.close();

        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
    }

    @Override
    public void remove(String nome) {
        try {
            con = new ConnectionBD();
            String query = "DELETE FROM atributo WHERE nome=?";
            PreparedStatement ps = con.getConnection("").prepareStatement(query);
            ps.setString(1, nome);
            ps.executeUpdate();
            ps.close();

        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
    }

    @Override
    public void update(Atributo atributo) {
        try {
            con = new ConnectionBD();
            String query = "UPDATE atributo SET nome = ? WHERE id = ?";
            PreparedStatement ps = con.getConnection("").prepareStatement(query);
            ps.setFloat(1, atributo.getImpostoEstadual());
            ps.setFloat(2, atributo.getImpostoMunicipal());
            ps.setFloat(3, atributo.getImpostoFederal());

            ps.executeUpdate();
            ps.close();

        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
    }

    @Override
    public List<Atributo> listar() {
        try {
            con = new ConnectionBD();
            String query = "SELECT * FROM atributo";
            List<Atributo> lista = new ArrayList<>();
            PreparedStatement ps = con.getConnection("").prepareStatement(query);
            rs = ps.executeQuery();
            while (rs.next()) {
                Atributo item = new Atributo();
                item.setImpostoEstadual(rs.getInt("nome"));
                item.setImpostoFederal(rs.getInt("nome"));
                item.setImpostoMunicipal(rs.getInt("nome"));
                lista.add(item);
            }
            rs.close();
            ps.close();
            return lista;

        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        return Collections.emptyList();
    }

    @Override
    public List<Atributo> buscar(String nome) {
        try {
            con = new ConnectionBD();
            String query = "SELECT * FROM atributo WHERE nome LIKE ?";
            List<Atributo> lista = new ArrayList<>();
            PreparedStatement ps = con.getConnection("").prepareStatement(query);
            ps.setString(1, "%" + nome + "%");

            rs = ps.executeQuery();
            while (rs.next()) {
                Atributo item = new Atributo();
                item.setImpostoEstadual(rs.getInt("nome"));
                item.setImpostoFederal(rs.getInt("nome"));
                item.setImpostoMunicipal(rs.getInt("nome"));
                lista.add(item);
            }

            rs.close();
            ps.close();
            return lista;

        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        return Collections.emptyList();
    }
}
