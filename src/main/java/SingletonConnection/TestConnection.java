package SingletonConnection;

import java.sql.Connection;
import java.sql.DriverManager;

public class TestConnection {
    public static void main(String[] args) {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection conn = DriverManager.getConnection(
                "jdbc:mysql://localhost:3306/ecommerce", "root", "root"
            );
            if (conn != null && !conn.isClosed()) {
                System.out.println("Connexion réussie !");
            }
            conn.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
