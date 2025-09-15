// src/main/java/Patika_9_Week_ETicaretSitesi/model/Product.java
package Patika_9_Week_ETicaretSitesi.model;

import java.math.BigDecimal;

public class Product {
    private Long id;
    private String name;
    private String brand;
    private BigDecimal priceTl;
    private int stock;

    public Product(Long id, String name, String brand, BigDecimal priceTl, int stock) {
        this.id = id; this.name = name; this.brand = brand; this.priceTl = priceTl; this.stock = stock;
    }
    public Long getId() { return id; }
    public String getName() { return name; }
    public String getBrand() { return brand; }
    public BigDecimal getPriceTl() { return priceTl; }
    public int getStock() { return stock; }
}
