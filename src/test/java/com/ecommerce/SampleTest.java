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
    	utilisateurDao = new UtilisateurDaoImpl();
        System.out.println("✅ DAO initialisé avec SingletonConnection !");
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
