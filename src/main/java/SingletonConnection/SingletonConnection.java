package SingletonConnection;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class SingletonConnection {
    private static Connection connection;

    static {
        try {
            // Utiliser le driver MySQL moderne
            Class.forName("com.mysql.cj.jdbc.Driver");

            // URL JDBC pour Docker Compose : le host est le nom du service MySQL
            String url = "jdbc:mysql://mysql:3306/ecommerce?serverTimezone=UTC";
            String user = "root";   // mot de passe défini dans docker-compose
            String password = "root";

            connection = DriverManager.getConnection(url, user, password);

            System.out.println("Connexion MySQL réussie !");
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Erreur de connexion à la base de données", e);
        }
    }

    public static Connection getConnection() {
        return connection;
    }
}
