// src/main/java/Patika_9_Week_ETicaretSitesi/dao/OrderDAO.java
package Patika_9_Week_ETicaretSitesi.dao;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;

public interface OrderDAO {
    Long insertOrder(Connection con, Long customerId, BigDecimal total) throws SQLException;
    void insertOrderItem(Connection con, Long orderId, Long productId, int qty,
                         java.math.BigDecimal unitPrice, java.math.BigDecimal lineTotal) throws SQLException;
}
