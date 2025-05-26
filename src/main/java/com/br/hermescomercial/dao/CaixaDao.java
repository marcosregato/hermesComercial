package com.br.hermescomercial.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.br.hermescomercial.connectionDB.ConnectionSQLite;
import com.br.hermescomercial.model.Caixa;


public class CaixaDao {
	

	private ConnectionSQLite con = null;
    private final Statement smt = null;
    private ResultSet rs = null;
    
    
    public void salvar() {
    	try {

			con  = new ConnectionSQLite();
			String query ="INSERT INTO fornecedor (id, nome, tipofornecedor) VALUES (NULL, ?, ?)";
			PreparedStatement ps = con.getConnection().prepareStatement(query);

			//ps.setString(1, fornecedor.getNome());
			//ps.setString(2, fornecedor.getTipoFornecedor());

			ps.executeUpdate();
			ps.close();
			
		} catch (Exception e) {
			// TODO: handle exception
			System.out.println(e.getMessage());
		}
    }
    
    public void delete() {
    	try {

			con  = new ConnectionSQLite();
			String query = "DELETE FROM custo WHERE nome=?";
			PreparedStatement ps = con.getConnection().prepareStatement(query);
			//ps.setString(1, nome);
			ps.executeUpdate();
			rs.close();
			ps.close();
			
		} catch (Exception e) {
			// TODO: handle exception
			System.out.println(e.getMessage());
		}
    }
    
    public void update() {
    	try {

			con  = new ConnectionSQLite();
			String query = "update custo set custounitario = ?,custototal = ?";
			PreparedStatement ps = con.getConnection().prepareStatement(query);
			//ps.setFloat(1, custo.getCustoUnitario());
			//ps.setFloat(2, custo.getCustoTotal());

			rs.close();
			ps.close();
			
		} catch (Exception e) {
			// TODO: handle exception
			System.out.println(e.getMessage());
		}
    }
    
    public List<Caixa> listar() {
    	try {

			con  = new ConnectionSQLite();
			String query ="select * from fornecedor";
			List<Caixa> lista = new ArrayList<>();
			PreparedStatement ps = con.getConnection().prepareStatement(query);
			rs = ps.executeQuery();
			while (rs.next()) {
				Caixa item = new Caixa();
				//	item.setCustoUnitario(rs.getFloat("nome"));
				//	item.setCustoTotal(rs.getFloat("subproduto"));

				lista.add(item);
			}

			return lista;
			
		} catch (Exception e) {
			// TODO: handle exception
			System.out.println(e.getMessage());
		}
		return null;
    }
    
    public List<Caixa> buscar() {
    	try {

			/*
			String query =  "SELECT * FROM custo WHERE nome =?";
			PreparedStatement ps = con.connection().prepareStatement(query);
			ps.setString(1, nome);

			rs = ps.executeQuery();
			Caixa caixa = null;
			if (rs.next()) {

				caixa = new Caixa();

				//custo.setCustoUnitario(rs.getFloat("custounitario"));
				//custo.setCustoTotal(rs.getFloat("custototal"));


			}

			rs.close();
			ps.close();
			return caixa;

			 */
			
		} catch (Exception e) {
			// TODO: handle exception
			System.out.println(e.getMessage());
		}

		return null;
    }
}
