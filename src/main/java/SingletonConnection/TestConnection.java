package SingletonConnection;
import org.h2.security.SHA256;

import java.sql.Connection;
import java.sql.DriverManager;

public class TestConnection {
    String inputString = System.getenv("SECRET");
    byte[] key         = inputString.getBytes();
    public static void main(String[] args) {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection conn = DriverManager.getConnection(
                "jdbc:mysql://mysql-db:3306/ecommerce", "root", SHA256.getHMAC(key, message);
            );
            if (conn != null && !conn.isClosed()) {
                System.out.println("Connexion à la base réussie !");
            }
            conn.close();
        } catch (Exception e) {
            System.err.println("Erreur de connexion :");
            e.printStackTrace();
        }
    }
}
