package com.ecommerce;

import static org.junit.jupiter.api.Assertions.*;
import java.sql.*;
import org.junit.jupiter.api.*;

import SingletonConnection.SingletonConnection;
import UtilisateurDao.UtilisateurDaoImpl;
import entities.Utilisateur;

public class SampleTest {
	
    private static Connection connection;

    private static UtilisateurDaoImpl utilisateurDao;

    @BeforeAll
    public static void init() throws Exception {
        // Charger le driver JDBC
        Class.forName("com.mysql.cj.jdbc.Driver");

        String url = "jdbc:mysql://localhost:3306/ecommerce";
        String user = "root";
        String password = "root";

        connection = DriverManager.getConnection(url, user, password);
        utilisateurDao = new UtilisateurDaoImpl();

        System.out.println("✅ Connexion à la base réussie !");
    }

    @AfterAll
    public static void close() throws Exception {
        if (connection != null && !connection.isClosed()) {
            connection.close();
            System.out.println("🔒 Connexion fermée.");
        }
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

        assertDoesNotThrow(() -> {
            utilisateurDao.saveUtilisateur(u);
        }, "L'enregistrement d'un utilisateur ne doit pas lever d'exception.");
    }
}
