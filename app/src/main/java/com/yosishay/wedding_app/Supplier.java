package com.yosishay.wedding_app;

import java.util.ArrayList;

public class Supplier {
    private String name;
    private String phone;
    private String prof;
    private String url;
    private String voters;
    private String avg;
    private ArrayList<String> phones;

    public Supplier() {
        // Default constructor required for Firebase database operations
    }
    public Supplier(String name, String phone, String prof,String url) {
        this.name = name;
        this.phone = phone;
        this.prof = prof;
        this.url=url;
        voters="0";
        avg="0";
        phones = new ArrayList<String>();
        phones.add("");
    }

    public String getName() {
        return name;
    }

    public String getPhone() {
        return phone;
    }

    public String getProf() {
        return prof;
    }
    public String getUrl() {
        return url;
    }

    public String getVoters() {
        return voters;
    }

    public void setVoters(String voters) {
        this.voters = voters;
    }

    public String getAvg() {
        return avg;
    }

    public void setAvg(String avg) {
        this.avg = avg;
    }

    public ArrayList<String> getPhones() {
        return phones;
    }

    public void setPhones(ArrayList<String> phones) {
        this.phones = phones;
    }
    public void addPhone(String phone) {
        this.phones.add(phone);
    }
}