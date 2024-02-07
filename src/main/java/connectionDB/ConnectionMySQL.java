package connectionDB;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionMySQL {

    private final String url = "jdbc:mysql://localhost/hermesComercial";
    private final String user = "postgres";
    private final String password = "postgres";

    /**
     * Connect to the PostgreSQL database
     *
     * @return a Connection object
     */
    public Connection connection() {
        Connection con = null;
        try {
            con = DriverManager.getConnection(url, user, password);
            System.out.println("Connected to the PostgreSQL server successfully.");
            return con;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return null;

    }
}
