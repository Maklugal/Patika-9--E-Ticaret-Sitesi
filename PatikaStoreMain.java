// File: src/main/java/Patika_9_Week_ETicaretSitesi/PatikaStoreMain.java
package Patika_9_Week_ETicaretSitesi;

import Patika_9_Week_ETicaretSitesi.dao.*;
import Patika_9_Week_ETicaretSitesi.model.Cart;
import Patika_9_Week_ETicaretSitesi.model.Product;
import Patika_9_Week_ETicaretSitesi.service.CustomerService;
import Patika_9_Week_ETicaretSitesi.service.OrderService;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Scanner;

public class PatikaStoreMain {

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        // DAO & Service kurulumları
        CustomerJdbcDAO customerDAO = new CustomerJdbcDAO();
        CustomerService customerService = new CustomerService(customerDAO);
        ProductJdbcDAO productDAO = new ProductJdbcDAO();
        OrderJdbcDAO orderDAO = new OrderJdbcDAO();
        OrderService orderService = new OrderService(productDAO, orderDAO);

        // Oturum durumu
        Customer currentUser = null;
        Cart cart = new Cart();

        while (true) {
            if (currentUser == null) {
                printGuestMenu();
                String choice = sc.nextLine().trim();
                switch (choice) {
                    case "1" -> registerFlow(sc, customerService);
                    case "2" -> {
                        currentUser = loginFlow(sc, customerService, customerDAO);
                        if (currentUser != null) {
                            System.out.println("Giriş başarılı. Hoş geldin, " + currentUser.getFullName() + "!");
                        } else {
                            System.out.println("E-posta veya şifre hatalı.");
                        }
                    }
                    case "0" -> {
                        System.out.println("Çıkış yapılıyor...");
                        return;
                    }
                    default -> System.out.println("Geçersiz seçim!");
                }
            } else {
                printUserMenu(currentUser);
                String choice = sc.nextLine().trim();
                switch (choice) {
                    case "3" -> listProducts(productDAO.listAll());
                    case "4" -> searchProducts(sc, productDAO);
                    case "5" -> addToCartFlow(sc, cart);
                    case "6" -> showCart(cart);
                    case "7" -> checkoutFlow(orderService, currentUser, cart);
                    case "9" -> {
                        currentUser = null;
                        cart.clear();
                        System.out.println("Oturum kapatıldı.");
                    }
                    case "0" -> {
                        System.out.println("Çıkış yapılıyor...");
                        return;
                    }
                    default -> System.out.println("Geçersiz seçim!");
                }
            }
        }
    }

    /* --------------------- MENÜLER --------------------- */

    private static void printGuestMenu() {
        System.out.println("\n=== Patika Store === (Misafir)");
        System.out.println(" 1 - Müşteri Kaydı");
        System.out.println(" 2 - Giriş Yap");
        System.out.println(" 0 - Programdan Çık");
        System.out.print("Seçim: ");
    }

    private static void printUserMenu(Customer currentUser) {
        System.out.println("\n=== Patika Store === (Kullanıcı: " + currentUser.getEmail() + ")");
        System.out.println(" 3 - Ürünleri Listele");
        System.out.println(" 4 - Ürün Ara");
        System.out.println(" 5 - Sepete Ekle");
        System.out.println(" 6 - Sepeti Göster");
        System.out.println(" 7 - Siparişi Tamamla");
        System.out.println(" 9 - Oturumu Kapat");
        System.out.println(" 0 - Programdan Çık");
        System.out.print("Seçim: ");
    }

    /* --------------------- AKIŞLAR --------------------- */

    private static void registerFlow(Scanner sc, CustomerService customerService) {
        System.out.print("Ad Soyad: ");
        String fullName = sc.nextLine().trim();
        System.out.print("E-posta: ");
        String email = sc.nextLine().trim();
        System.out.print("Şifre: ");
        String password = sc.nextLine();

        if (fullName.isEmpty() || email.isEmpty() || password.isEmpty()) {
            System.out.println("Lütfen tüm alanları doldurun.");
            return;
        }
        customerService.save(new Customer(fullName, email, password)); // service içinde hash'lenir
    }

    private static Customer loginFlow(Scanner sc, CustomerService customerService, CustomerJdbcDAO customerDAO) {
        System.out.print("E-posta: ");
        String email = sc.nextLine().trim();
        System.out.print("Şifre: ");
        String password = sc.nextLine();

        boolean ok = customerService.login(email, password);
        if (!ok) return null;

        // Not: login boolean; oturum için kullanıcıyı çekiyoruz
        Optional<Customer> found = customerDAO.findByEmail(email);
        return found.orElse(null);
    }

    private static void listProducts(List<Product> products) {
        if (products.isEmpty()) {
            System.out.println("Hiç ürün yok.");
            return;
        }
        System.out.println("\n--- Ürün Listesi ---");
        for (Product p : products) {
            System.out.printf("[%d] %s (%s) | %,.2f TL | stok=%d%n",
                    p.getId(), p.getName(), p.getBrand(),
                    p.getPriceTl().doubleValue(), p.getStock());
        }
    }

    private static void searchProducts(Scanner sc, ProductJdbcDAO productDAO) {
        System.out.print("Anahtar kelime: ");
        String q = sc.nextLine().trim();
        List<Product> results = productDAO.searchByKeyword(q);
        if (results.isEmpty()) {
            System.out.println("Sonuç yok.");
            return;
        }
        System.out.println("\n--- Arama Sonuçları ---");
        for (Product p : results) {
            System.out.printf("[%d] %s (%s) | %,.2f TL | stok=%d%n",
                    p.getId(), p.getName(), p.getBrand(),
                    p.getPriceTl().doubleValue(), p.getStock());
        }
    }

    private static void addToCartFlow(Scanner sc, Cart cart) {
        Long pid = readLong(sc, "Ürün id: ");
        if (pid == null) return;
        Integer qty = readInt(sc, "Adet: ");
        if (qty == null || qty <= 0) {
            System.out.println("Adet pozitif olmalı.");
            return;
        }
        cart.add(pid, qty);
        System.out.println("Sepete eklendi.");
    }

    private static void showCart(Cart cart) {
        if (cart.isEmpty()) {
            System.out.println("Sepet boş.");
            return;
        }
        System.out.println("\n--- Sepet ---");
        for (Map.Entry<Long, Integer> e : cart.getItems().entrySet()) {
            System.out.println("ÜrünId=" + e.getKey() + " | Adet=" + e.getValue());
        }
    }

    private static void checkoutFlow(OrderService orderService, Customer currentUser, Cart cart) {
        if (cart.isEmpty()) {
            System.out.println("Sepet boş.");
            return;
        }
        try {
            Long orderId = orderService.placeOrder(currentUser.getId(), cart);
            System.out.println("Sipariş oluşturuldu. OrderId=" + orderId);
        } catch (Exception ex) {
            System.out.println("Sipariş başarısız: " + ex.getMessage());
        }
    }

    /* --------------------- KÜÇÜK YARDIMCILAR --------------------- */

    private static Long readLong(Scanner sc, String prompt) {
        System.out.print(prompt);
        String s = sc.nextLine().trim();
        try {
            return Long.parseLong(s);
        } catch (NumberFormatException e) {
            System.out.println("Geçersiz sayı.");
            return null;
        }
    }

    private static Integer readInt(Scanner sc, String prompt) {
        System.out.print(prompt);
        String s = sc.nextLine().trim();
        try {
            return Integer.parseInt(s);
        } catch (NumberFormatException e) {
            System.out.println("Geçersiz sayı.");
            return null;
        }
    }
}
