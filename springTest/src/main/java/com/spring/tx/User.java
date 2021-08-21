package com.spring.tx;

public class User {
    private Integer userId;
    private String name;
    private Integer balance;

    public User(){}
    public User(Integer userId, String name, Integer balance) {
        this.userId = userId;
        this.name = name;
        this.balance = balance;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getBalance() {
        return balance;
    }

    public void setBalance(Integer balance) {
        this.balance = balance;
    }
}
