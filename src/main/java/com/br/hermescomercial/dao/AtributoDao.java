package com.br.hermescomercial.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.br.hermescomercial.Repository.RepositoryAtributo;
import com.br.hermescomercial.connectionDB.ConnectionSQLite;
import com.br.hermescomercial.model.Atributo;

public class AtributoDao implements RepositoryAtributo {

    private ConnectionSQLite con = null;
    private final Statement smt = null;
    private ResultSet rs = null;


    @Override
    public void salvar(Atributo atributo) {
        try {

            con  = new ConnectionSQLite();
            String query ="INSERT INTO fornecedor (id, nome, tipofornecedor) VALUES (NULL, ?, ?)";
            PreparedStatement ps = con.getConnection().prepareStatement(query);

            //ps.setString(1, fornecedor.getNome());
            //ps.setString(2, fornecedor.getTipoFornecedor());

            ps.executeUpdate();
            ps.close();

        }catch (Exception e){
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void remove(String nome) {
        try {

            con  = new ConnectionSQLite();
            String query = "DELETE FROM custo WHERE nome=?";
            PreparedStatement ps = con.getConnection().prepareStatement(query);
            ps.setString(1, nome);
            ps.executeUpdate();
            rs.close();
            ps.close();

        }catch (Exception e){
            System.out.println(e.getMessage());
        }

    }

    @Override
    public void update(Atributo atributo) {
        try {

            con  = new ConnectionSQLite();
            String query = "update custo set custounitario = ?,custototal = ?";
            PreparedStatement ps = con.getConnection().prepareStatement(query);
            //ps.setFloat(1, custo.getCustoUnitario());
            //ps.setFloat(2, custo.getCustoTotal());

            rs.close();
            ps.close();

        }catch (Exception e){
            System.out.println(e.getMessage());
        }

    }

    @Override
    public List<Atributo> listar() {
        try {

            con  = new ConnectionSQLite();
            String query ="select * from fornecedor";
            List<Atributo> lista = new ArrayList<>();
            PreparedStatement ps = con.getConnection().prepareStatement(query);
            rs = ps.executeQuery();
            while (rs.next()) {
                Atributo item = new Atributo();
                //	item.setCustoUnitario(rs.getFloat("nome"));
                //	item.setCustoTotal(rs.getFloat("subproduto"));

                lista.add(item);
            }

            return lista;

        }catch (Exception e){
            System.out.println(e.getMessage());
        }
        return null;
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
            System.out.println(e.getMessage());
        }
        return null;
    }
}
