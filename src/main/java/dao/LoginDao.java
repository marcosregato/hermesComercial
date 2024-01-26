package dao;

import connectionDB.ConnectionPostgreSQL;
import model.Cliente;
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

    public String acessarUsuario(String login, String senha){
        try {
            con  = new ConnectionPostgreSQL();

            String query ="select p.tipo from usuario u inner join" +
                    "acesso a on a.fk_usuario = u.id inner join" +
                    "permissao p on a.fk_permissao = p.id where u.login = ? and u.senha = ?";

            PreparedStatement ps = con.connection().prepareStatement(query);
            ps.setString(1, login);
            ps.setString(2, senha);

            rs = ps.executeQuery();
            Usuario usuario = null;
            if (rs.next()) {

                usuario = new Usuario();
                usuario.setTipoAcesso(rs.getString("tipo"));


            }

            rs.close();
            ps.close();
            return String.valueOf(usuario);

        } catch (Exception e) {
           // logger.info( e.getClass().getName() + " : " + e.getMessage() );
        }
        //con.close();
        return null;
    }
}
