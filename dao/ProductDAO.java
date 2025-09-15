// src/main/java/Patika_9_Week_ETicaretSitesi/dao/ProductDAO.java
package Patika_9_Week_ETicaretSitesi.dao;

import Patika_9_Week_ETicaretSitesi.model.Product;
import java.util.List;
import java.util.Optional;

public interface ProductDAO {
    List<Product> listAll();
    List<Product> searchByKeyword(String q);
    Optional<Product> findById(Long id);
}
