// src/main/java/Patika_9_Week_ETicaretSitesi/db/ConnectionFactory.java
package Patika_9_Week_ETicaretSitesi.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionFactory {
    // KENDİ bilgilerin:
    private static final String URL  = "jdbc:postgresql://localhost:5432/eticaret";
    private static final String USER = "postgres";      // ya da kendi kullanıcın
    private static final String PASS = "123456";

    public static Connection getConnection() {
        try {
            return DriverManager.getConnection(URL, USER, PASS);
        } catch (SQLException e) {
            throw new RuntimeException("DB bağlantısı kurulamadı", e);
        }
    }
}
