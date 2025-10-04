package SingletonConnection;

import java.sql.Connection;
import java.sql.DriverManager;

public class SingletonConnection {
    private static Connection connection;

    static {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            connection = DriverManager.getConnection(
                "jdbc:mysql://mysql:3306/ecommerce",
                "root",
                "root"
            );
            System.out.println("✅ Connexion MySQL réussie !");
        } catch (Exception e) {
            System.out.println("❌ Erreur de connexion à la BD : " + e.getMessage());
            e.printStackTrace();
        }
    }

    public static Connection getConnection() {
        return connection;
    }
}
