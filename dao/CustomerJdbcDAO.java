// src/main/java/Patika_9_Week_ETicaretSitesi/dao/CustomerJdbcDAO.java
package Patika_9_Week_ETicaretSitesi.dao;

import Patika_9_Week_ETicaretSitesi.Customer;
import Patika_9_Week_ETicaretSitesi.db.ConnectionFactory;

import java.sql.*;
import java.util.Optional;

public class CustomerJdbcDAO implements CustomerDAO {

    @Override
    public Long save(Customer c) {
        final String sql = """
            INSERT INTO customers(full_name, email, password_hash)
            VALUES (?, LOWER(?), ?)
            RETURNING id
            """;
        try (var con = ConnectionFactory.getConnection();
             var ps  = con.prepareStatement(sql)) {
            ps.setString(1, c.getFullName());
            ps.setString(2, c.getEmail());
            ps.setString(3, c.getPassword()); // hash gelmeli
            try (var rs = ps.executeQuery()) {
                if (rs.next()) return rs.getLong("id");
                throw new RuntimeException("Kayıt sonrası id alınamadı");
            }
        } catch (SQLException e) {
            if ("23505".equals(e.getSQLState())) {
                throw new RuntimeException("Bu e-posta zaten kayıtlı!", e);
            }
            throw new RuntimeException("Kayıt sırasında SQL hatası", e);
        }
    }

    @Override
    public Optional<Customer> findByEmail(String email) {
        final String sql = """
            SELECT id, full_name, email, password_hash
            FROM customers
            WHERE LOWER(email)=LOWER(?)
            """;
        try (var con = ConnectionFactory.getConnection();
             var ps  = con.prepareStatement(sql)) {
            ps.setString(1, email);
            try (var rs = ps.executeQuery()) {
                if (rs.next()) {
                    var c = new Customer(
                            rs.getString("full_name"),
                            rs.getString("email"),
                            rs.getString("password_hash")
                    );
                    c.setId(rs.getLong("id"));
                    return Optional.of(c);
                }
                return Optional.empty();
            }
        } catch (SQLException e) {
            throw new RuntimeException("Müşteri aramada hata", e);
        }
    }

    @Override
    public boolean existsByEmail(String email) {
        final String sql = "SELECT 1 FROM customers WHERE LOWER(email)=LOWER(?)";
        try (var con = ConnectionFactory.getConnection();
             var ps  = con.prepareStatement(sql)) {
            ps.setString(1, email);
            try (var rs = ps.executeQuery()) {
                return rs.next();
            }
        } catch (SQLException e) {
            throw new RuntimeException("E-posta kontrolünde hata", e);
        }
    }
}
