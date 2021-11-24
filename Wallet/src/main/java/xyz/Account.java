package xyz;

import java.sql.*;

class Account{
    private String name, userId, password;
    private Integer accno, balance;

    public Account(String name, Integer accno, String userId, String password, Integer balance) {
        this.name = name;
        this.accno = accno;
        this.userId = userId;
        this.password = password;
        this.balance = balance;
    }
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Integer getAccno() {
        return accno;
    }

    public void setAccno(Integer accno) {
        this.accno = accno;
    }

    public Integer getBalance() {
        return balance;
    }

    public void setBalance(Integer balance) {
        this.balance = balance;
    }

    @Override
    public String toString() {
        return "Account{" +
                "name='" + name + '\'' +
                ", userId='" + userId + '\'' +
                ", password='" + password + '\'' +
                ", accno=" + accno +
                ", balance=" + balance +
                '}';
    }
}