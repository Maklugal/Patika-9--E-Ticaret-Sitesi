// File: src/main/java/Patika_9_Week_ETicaretSitesi/Customer.java
package Patika_9_Week_ETicaretSitesi;

public class Customer {
    private Long id;
    private final String fullName;
    private final String email;
    private final String password; // raw şifre (service katmanında hash’lenecek)

    public Customer(String fullName, String email, String password) {
        this.fullName = fullName;
        this.email = email;
        this.password = password;
    }
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getFullName() { return fullName; }
    public String getEmail() { return email; }
    public String getPassword() { return password; }
}
