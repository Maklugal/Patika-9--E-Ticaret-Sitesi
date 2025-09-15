// src/main/java/Patika_9_Week_ETicaretSitesi/dao/OrderJdbcDAO.java
package Patika_9_Week_ETicaretSitesi.dao;

import java.math.BigDecimal;
import java.sql.*;

public class OrderJdbcDAO implements OrderDAO {

    @Override
    public Long insertOrder(Connection con, Long customerId, BigDecimal total) throws SQLException {
        final String sql = "INSERT INTO orders(customer_id,total_tl) VALUES (?,?) RETURNING id";
        try (var ps = con.prepareStatement(sql)) {
            ps.setLong(1, customerId);
            ps.setBigDecimal(2, total);
            try (var rs = ps.executeQuery()) {
                if (rs.next()) return rs.getLong("id");
                throw new SQLException("Order id alınamadı");
            }
        }
    }

    @Override
    public void insertOrderItem(Connection con, Long orderId, Long productId, int qty,
                                BigDecimal unitPrice, BigDecimal lineTotal) throws SQLException {
        final String sql = """
            INSERT INTO order_items(order_id,product_id,qty,unit_price_tl,line_total_tl)
            VALUES (?,?,?,?,?)
            """;
        try (var ps = con.prepareStatement(sql)) {
            ps.setLong(1, orderId);
            ps.setLong(2, productId);
            ps.setInt(3, qty);
            ps.setBigDecimal(4, unitPrice);
            ps.setBigDecimal(5, lineTotal);
            ps.executeUpdate();
        }
    }
}
