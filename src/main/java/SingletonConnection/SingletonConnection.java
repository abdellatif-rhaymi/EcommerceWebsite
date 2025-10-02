package SingletonConnection;

import java.sql.Connection;
import java.sql.DriverManager;

public class SingletonConnection {
    private static Connection connection;
    static {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            connection = DriverManager.getConnection(
                "jdbc:mysql://mysql:3306/ecommerce?serverTimezone=UTC", 
                "root", 
                "root"
            );
        } catch (Exception e) {
            throw new RuntimeException("Erreur de connexion à la base", e);
        }
    }

    public static Connection getConnection() {
        return connection;
    }
}
