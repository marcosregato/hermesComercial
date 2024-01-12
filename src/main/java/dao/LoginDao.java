package dao;

import connectionDB.ConnectionPostgreSQL;
import model.Usuario;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class LoginDao {

    //Logger logger = Logger.getLogger(LoginDao.class);


    private ConnectionPostgreSQL con = null;
    private Statement smt = null;
    private ResultSet rs = null;

    public List<Usuario> acessarUsuario(String login, String senha){
        try {
            con  = new ConnectionPostgreSQL();
            String query ="select l.login, l.senha from loginusuario l inner join usuario p on l.idusuario = p.id where l.login ='"+login+"' and l.senha ='"+senha+"'";
            List<Usuario> usuario = new ArrayList<>();
            PreparedStatement ps = con.connection().prepareStatement(query);
            rs = ps.executeQuery();
            while (rs.next()) {
                Usuario lg = new Usuario();
                lg.setLogin(rs.getString("login"));
                lg.setSenha(rs.getString("senha"));
                usuario.add(lg);
            }

            return usuario;

        } catch (Exception e) {
           // logger.info( e.getClass().getName() + " : " + e.getMessage() );
        }
        //con.close();
        return null;
    }
}
