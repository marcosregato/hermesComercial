package com.br.hermescomercial.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.br.hermescomercial.connectionDB.ConnectionBD;
import com.br.hermescomercial.model.Caixa;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


public class CaixaDao {
	
	private ConnectionBD con = null;
    private ResultSet rs = null;
    private static final Logger logger = LogManager.getLogger(CaixaDao.class);


    public void salvar() {
    	try {

			con  = new ConnectionBD();
			String query ="INSERT INTO fornecedor (id, nome, tipofornecedor) VALUES (NULL, ?, ?)";
			PreparedStatement ps = con.getConnection("").prepareStatement(query);

			//ps.setString(1, fornecedor.getNome());
			//ps.setString(2, fornecedor.getTipoFornecedor());

			ps.executeUpdate();
			ps.close();
			
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
    }
    
    public void delete() {
    	try {

			con  = new ConnectionBD();
			String query = "DELETE FROM custo WHERE nome=?";
			PreparedStatement ps = con.getConnection("").prepareStatement(query);
			//ps.setString(1, nome);
			ps.executeUpdate();
			rs.close();
			ps.close();
			
		} catch (Exception e) {
            logger.error(e.getMessage(), e);
		}
    }
    
    public void update() {
    	try {

			con  = new ConnectionBD();
			String query = "update custo set custounitario = ?,custototal = ?";
			PreparedStatement ps = con.getConnection("").prepareStatement(query);
			//ps.setFloat(1, custo.getCustoUnitario());
			//ps.setFloat(2, custo.getCustoTotal());

			rs.close();
			ps.close();
			
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
    }
    
    public List<Caixa> listar() {
    	try {

			con  = new ConnectionBD();
			String query ="select * from fornecedor";
			List<Caixa> lista = new ArrayList<>();
			PreparedStatement ps = con.getConnection("").prepareStatement(query);
			rs = ps.executeQuery();
			while (rs.next()) {
				Caixa item = new Caixa();
				item.setTipo(rs.getString("nome"));
				item.setTipo(rs.getString("subproduto"));
                item.setValorCaixa(rs.getInt("subproduto"));
				lista.add(item);
			}

			return lista;
			
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		return Collections.emptyList();
    }
    
    public List<Caixa> buscar() {
    	try {

			String query =  "SELECT * FROM custo WHERE nome =?";
			PreparedStatement ps = con.getConnection("").prepareStatement(query);
            List<Caixa> lista = new ArrayList<>();
			rs = ps.executeQuery();

			if (rs.next()) {

                Caixa item = new Caixa();

				//item.setCustoUnitario(rs.getFloat("custounitario"));
				//item.setCustoTotal(rs.getFloat("custototal"));
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
