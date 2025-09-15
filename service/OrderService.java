// src/main/java/Patika_9_Week_ETicaretSitesi/service/OrderService.java
package Patika_9_Week_ETicaretSitesi.service;

import Patika_9_Week_ETicaretSitesi.dao.OrderDAO;
import Patika_9_Week_ETicaretSitesi.dao.ProductDAO;
import Patika_9_Week_ETicaretSitesi.db.ConnectionFactory;
import Patika_9_Week_ETicaretSitesi.model.Cart;
import Patika_9_Week_ETicaretSitesi.model.Product;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;

public class OrderService {
    private final ProductDAO productDAO;
    private final OrderDAO orderDAO;

    public OrderService(ProductDAO productDAO, OrderDAO orderDAO) {
        this.productDAO = productDAO;
        this.orderDAO = orderDAO;
    }

    public Long placeOrder(Long customerId, Cart cart) {
        if (cart.isEmpty()) throw new RuntimeException("Sepet boş");

        try (Connection con = ConnectionFactory.getConnection()) {
            con.setAutoCommit(false);

            BigDecimal total = BigDecimal.ZERO;

            // 1) Stok kilitle & düş (satır bazlı, koşullu update)
            String decSql = "UPDATE products SET stock = stock - ? WHERE id = ? AND stock >= ?";
            try (PreparedStatement dec = con.prepareStatement(decSql)) {

                for (var e : cart.getItems().entrySet()) {
                    Long productId = e.getKey();
                    int qty = e.getValue();

                    Product p = productDAO.findById(productId)
                            .orElseThrow(() -> new RuntimeException("Ürün bulunamadı: " + productId));

                    // fiyatı DB’den al (güncel)
                    BigDecimal line = p.getPriceTl().multiply(BigDecimal.valueOf(qty));
                    total = total.add(line);

                    dec.setInt(1, qty);
                    dec.setLong(2, productId);
                    dec.setInt(3, qty);
                    int updated = dec.executeUpdate();
                    if (updated == 0) {
                        con.rollback();
                        throw new RuntimeException("Yetersiz stok: " + p.getName());
                    }
                }
            }

            // 2) Sipariş header
            Long orderId = orderDAO.insertOrder(con, customerId, total);

            // 3) Satırları kaydet
            for (var e : cart.getItems().entrySet()) {
                Long productId = e.getKey();
                int qty = e.getValue();
                Product p = productDAO.findById(productId).orElseThrow();
                BigDecimal unit = p.getPriceTl();
                BigDecimal line = unit.multiply(BigDecimal.valueOf(qty));
                orderDAO.insertOrderItem(con, orderId, productId, qty, unit, line);
            }

            con.commit();
            cart.clear();
            return orderId;

        } catch (Exception ex) {
            throw new RuntimeException("Sipariş oluşturulamadı: " + ex.getMessage(), ex);
        }
    }
}
