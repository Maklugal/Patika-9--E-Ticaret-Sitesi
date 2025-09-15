// src/main/java/Patika_9_Week_ETicaretSitesi/dao/CustomerDAO.java
package Patika_9_Week_ETicaretSitesi.dao;

import Patika_9_Week_ETicaretSitesi.Customer;
import java.util.Optional;

public interface CustomerDAO {
    Long save(Customer c);
    Optional<Customer> findByEmail(String email);
    boolean existsByEmail(String email);
}
