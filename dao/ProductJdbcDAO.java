// src/main/java/Patika_9_Week_ETicaretSitesi/dao/ProductJdbcDAO.java
package Patika_9_Week_ETicaretSitesi.dao;

import Patika_9_Week_ETicaretSitesi.db.ConnectionFactory;
import Patika_9_Week_ETicaretSitesi.model.Product;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ProductJdbcDAO implements ProductDAO {

    @Override
    public List<Product> listAll() {
        final String sql = "SELECT id,name,brand,price_tl,stock FROM products ORDER BY name";
        try (var con = ConnectionFactory.getConnection();
             var ps  = con.prepareStatement(sql);
             var rs  = ps.executeQuery()) {
            List<Product> list = new ArrayList<>();
            while (rs.next()) {
                list.add(map(rs));
            }
            return list;
        } catch (SQLException e) {
            throw new RuntimeException("Ürün listeleme hatası", e);
        }
    }

    @Override
    public List<Product> searchByKeyword(String q) {
        final String sql = """
            SELECT id,name,brand,price_tl,stock
            FROM products
            WHERE LOWER(name) LIKE LOWER(?) OR LOWER(brand) LIKE LOWER(?)
            ORDER BY name
            """;
        try (var con = ConnectionFactory.getConnection();
             var ps  = con.prepareStatement(sql)) {
            String like = "%" + q + "%";
            ps.setString(1, like);
            ps.setString(2, like);
            try (var rs = ps.executeQuery()) {
                List<Product> list = new ArrayList<>();
                while (rs.next()) list.add(map(rs));
                return list;
            }
        } catch (SQLException e) {
            throw new RuntimeException("Ürün arama hatası", e);
        }
    }

    @Override
    public Optional<Product> findById(Long id) {
        final String sql = "SELECT id,name,brand,price_tl,stock FROM products WHERE id=?";
        try (var con = ConnectionFactory.getConnection();
             var ps  = con.prepareStatement(sql)) {
            ps.setLong(1, id);
            try (var rs = ps.executeQuery()) {
                return rs.next() ? Optional.of(map(rs)) : Optional.empty();
            }
        } catch (SQLException e) {
            throw new RuntimeException("Ürün bulma hatası", e);
        }
    }

    private Product map(ResultSet rs) throws SQLException {
        return new Product(
                rs.getLong("id"),
                rs.getString("name"),
                rs.getString("brand"),
                rs.getBigDecimal("price_tl"),
                rs.getInt("stock")
        );
    }
}
