package com.ecommerce;

import static org.junit.jupiter.api.Assertions.*;
import java.sql.*;
import org.junit.jupiter.api.*;

import UtilisateurDao.UtilisateurDaoImpl;
import entities.Utilisateur;

public class SampleTest {
	

    private static UtilisateurDaoImpl utilisateurDao;

   @BeforeAll
	public static void init() throws Exception {
	    // Charger le driver JDBC
	    Class.forName("com.mysql.cj.jdbc.Driver");
	    Connection connection = DriverManager.getConnection(
	        "jdbc:mysql://mysql:3306/ecommerce", "root", "root");
	
	    // Injecter la connexion dans le DAO
	    utilisateurDao = new UtilisateurDaoImpl(connection);
	
	    System.out.println("✅ DAO initialisé avec connexion JDBC !");
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
