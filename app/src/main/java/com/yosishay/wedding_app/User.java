package com.yosishay.wedding_app;

import java.util.ArrayList;

public class User {
    private String name;
    private String phone;
    private String password;

    private String isAdmin;

    private ArrayList<String>  suppliers;

    public User() {
        // Default constructor required for Firebase database operations
    }

    public User(String name, String phone, String password) {
        this.name = name;
        this.phone = phone;
        this.password = password;
        isAdmin="0";
        suppliers=  new ArrayList<String>();
        suppliers.add("");
    }

    public String getName() {
        return name;
    }

    public String getPhone() {
        return phone;
    }

    public String getPassword() {
        return password;
    }

    public String getIsAdmin(){return isAdmin;}

    public ArrayList<String> getSuppliers() {
        return suppliers;
    }

    public void setSuppliers(ArrayList<String> suppliers) {
        this.suppliers = suppliers;
    }
    public void addSuppliers(String supplier) {
        this.suppliers.add(supplier);
    }
}