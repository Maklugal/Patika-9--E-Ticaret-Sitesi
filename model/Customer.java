package Patika_9_Week_ETicaretSitesi.model;

import Patika_3_Week.ProjeKitap.User;

import java.sql.Date;
import java.time.LocalDate;

public class Customer extends BaseModel {

    private String name;
    private String email;
    private String password;  // TODO haslenmeli
    private Date updatedDate;

    public Customer(String name) {
        this.name = name;
    }

    public Customer(String name, String email, String password) {
        this.name = name;
        this.email = email;
        this.password = password;
    }

    public Customer() {

    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setUpdatedDate(Date updateddate) {
        this.updatedDate = updateddate;
    }
}

