package com.ecommerce;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.*;
import java.sql.Connection;
import java.sql.Statement;

import SingletonConnection.SingletonConnection;
import UtilisateurDao.UtilisateurDaoImpl;
import entities.Utilisateur;

public class SampleTest {

    private static Connection connection;
    private static UtilisateurDaoImpl utilisateurDao;

    @BeforeAll
    public static void init() throws Exception {
        // ✅ Activer le mode test (H2)
        System.setProperty("TEST_ENV", "true");

        // ✅ Connexion à la base H2
        connection = SingletonConnection.getConnection();

        // ✅ Créer la table utilisateur dans H2
        Statement stmt = connection.createStatement();
        stmt.execute("""
            CREATE TABLE utilisateur (
                id INT AUTO_INCREMENT PRIMARY KEY,
                nom VARCHAR(100),
                email VARCHAR(100),
                mot_de_passe VARCHAR(100),
                contact BIGINT,
                adresse VARCHAR(255),
                role VARCHAR(50)
            );
        """);
        stmt.close();

        utilisateurDao = new UtilisateurDaoImpl();
        System.out.println("✅ Base H2 initialisée pour les tests !");
    }

    @Test
    public void testSaveUtilisateur() {
        Utilisateur u = new Utilisateur();
        u.setNom("Abdullah");
        u.setEmail("abdullah@example.com");
        u.setMotDePasse("12345678");
        u.setContact(822828828L);
        u.setAdresse("Harhoura");
        u.setRole("client");
        
        utilisateurDao.saveUtilisateur(u);

        // Vérifier que l'utilisateur a été inséré
        PreparedStatement ps = connection.prepareStatement(
            "SELECT COUNT(*) FROM utilisateur WHERE email = ?");
        ps.setString(1, u.getEmail());
        ResultSet rs = ps.executeQuery();
        rs.next();
        int count = rs.getInt(1);
        rs.close();
        ps.close();

        assertEquals(1, count, "L'utilisateur doit être inséré dans la base H2");
        
    }

    @AfterAll
    public static void tearDown() throws Exception {
        if (connection != null && !connection.isClosed()) {
            connection.close();
            System.out.println("🔒 Connexion H2 fermée.");
        }
    }
}
