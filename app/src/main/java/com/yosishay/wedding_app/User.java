package com.yosishay.wedding_app;

public class User {
    private String name;
    private String phone;
    private String password;

    private String isAdmin;

    public User() {
        // Default constructor required for Firebase database operations
    }

    public User(String name, String phone, String password) {
        this.name = name;
        this.phone = phone;
        this.password = password;
        isAdmin="0";
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
}