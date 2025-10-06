package com.ecommerce;

import static org.junit.jupiter.api.Assertions.*;
import java.sql.*;
import org.junit.jupiter.api.*;

import UtilisateurDao.UtilisateurDaoImpl;
import entities.Utilisateur;

public class SampleTest {
	
    private static Connection connection;

    private static UtilisateurDaoImpl utilisateurDao;

   @BeforeAll
	public static void init() {
	    System.out.println("🚀 BeforeAll executed !");
	    utilisateurDao = new UtilisateurDaoImpl();
	}


}
