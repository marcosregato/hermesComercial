package com.br.hermescomercial.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.br.hermescomercial.Repository.RepositoryAtributo;
import com.br.hermescomercial.connectionDB.ConnectionBD;
import com.br.hermescomercial.model.Atributo;
import org.apache.log4j.Logger;

public class AtributoDao implements RepositoryAtributo {

    private ConnectionBD con = null;
    private ResultSet rs = null;
    Logger logger = Logger.getLogger(getClass().getName());

    @Override
    public void salvar(Atributo atributo) {
        try {

            con  = new ConnectionBD();
            String query ="INSERT INTO fornecedor (id, nome, tipofornecedor) VALUES (NULL, ?, ?)";
            PreparedStatement ps = con.getConnection("").prepareStatement(query);

            //ps.setString(1, fornecedor.getNome());
            //ps.setString(2, fornecedor.getTipoFornecedor());

            ps.executeUpdate();
            ps.close();

        }catch (Exception e){
            logger.info(e.getMessage());
        }
    }

    @Override
    public void remove(String nome) {
        try {

            con  = new ConnectionBD();
            String query = "DELETE FROM custo WHERE nome=?";
            PreparedStatement ps = con.getConnection("").prepareStatement(query);
            ps.setString(1, nome);
            ps.executeUpdate();
            rs.close();
            ps.close();

        }catch (Exception e){
            logger.info(e.getMessage());
        }

    }

    @Override
    public void update(Atributo atributo) {
        try {

            con  = new ConnectionBD();
            String query = "update custo set custounitario = ?,custototal = ?";
            PreparedStatement ps = con.getConnection("").prepareStatement(query);
            //ps.setFloat(1, custo.getCustoUnitario());
            //ps.setFloat(2, custo.getCustoTotal());

            rs.close();
            ps.close();

        }catch (Exception e){
            logger.info(e.getMessage());
        }

    }

    @Override
    public List<Atributo> listar() {
        try {

            con  = new ConnectionBD();
            String query ="select * from fornecedor";
            List<Atributo> lista = new ArrayList<>();
            PreparedStatement ps = con.getConnection("").prepareStatement(query);
            rs = ps.executeQuery();
            while (rs.next()) {
                Atributo item = new Atributo();
                //	item.setCustoUnitario(rs.getFloat("nome"));
                //	item.setCustoTotal(rs.getFloat("subproduto"));

                lista.add(item);
            }

            return lista;

        }catch (Exception e){
            logger.info(e.getMessage());
        }
        return Collections.emptyList();
    }

    @Override
    public List<Atributo> buscar(String nome) {
        try {

/*            String query =  "SELECT * FROM custo WHERE nome =?";
            PreparedStatement ps = con.connection().prepareStatement(query);
            ps.setString(1, nome);

            rs = ps.executeQuery();
            Atributo atributo = null;
            if (rs.next()) {

                atributo = new Atributo();

                //custo.setCustoUnitario(rs.getFloat("custounitario"));
                //custo.setCustoTotal(rs.getFloat("custototal"));


            }

            rs.close();
            ps.close();
            return atributo;

 */

        }catch (Exception e){
            logger.info(e.getMessage());
        }
        return Collections.emptyList();
    }
}
