package dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

import connectionDB.ConnectionPostgreSQL;
import model.Cliente;
import model.Permissao;

public class PermissaoDao {
	
	private ConnectionPostgreSQL con = null;
    private Statement smt = null;
    private ResultSet rs = null;
	
	/*public String getPermissaoUsuario(String usuario) {

        try {

            String query =  "SELECT * FROM permissao WHERE nome =?";
            PreparedStatement ps = con.connection().prepareStatement(query);
            ps.setString(1, usuario);

            rs = ps.executeQuery();
            Permissao permissao = null;
            if (rs.next()) {

            	permissao = new Permissao();
            	permissao.setTipo(rs.getString("tipo"));
                

            }

            rs.close();
            ps.close();
            return permissao;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }*/
	

}
