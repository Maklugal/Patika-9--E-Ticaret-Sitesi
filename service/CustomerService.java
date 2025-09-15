// src/main/java/Patika_9_Week_ETicaretSitesi/service/CustomerService.java
package Patika_9_Week_ETicaretSitesi.service;

import Patika_9_Week_ETicaretSitesi.Customer;
import Patika_9_Week_ETicaretSitesi.dao.CustomerDAO;
import Patika_9_Week_ETicaretSitesi.util.PasswordUtil;

public class CustomerService {
    private final CustomerDAO dao;

    public CustomerService(CustomerDAO dao) { this.dao = dao; }

    public void save(Customer raw) {
        if (dao.existsByEmail(raw.getEmail())) {
            System.out.println("Bu e-posta ile kayıt zaten var!");
            return;
        }
        String hash = PasswordUtil.hash(raw.getPassword());
        Customer toSave = new Customer(raw.getFullName(), raw.getEmail(), hash);
        Long id = dao.save(toSave);
        System.out.println("Kayıt başarılı: " + raw.getFullName() + " (id=" + id + ")");
    }

    public boolean login(String email, String passwordRaw) {
        return dao.findByEmail(email)
                .map(found -> PasswordUtil.hash(passwordRaw).equals(found.getPassword()))
                .orElse(false);
    }
}
