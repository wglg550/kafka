package com.example.demo.entity;

public class Account {

    private int id;
    private String name;
    private float balance;

    public Account(String name, float balance) {
        this.name = name;
        this.balance = balance;
    }

    public Account() {

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public float getBalance() {
        return balance;
    }

    public void setBalance(float balance) {
        this.balance = balance;
    }
}
