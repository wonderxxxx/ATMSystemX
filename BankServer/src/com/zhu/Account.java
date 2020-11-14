package com.zhu;

import java.io.Serializable;

public class Account{

    private int id;

    private String name;

    private int bankCard;

    private int password;

    private double balance;

    private int bankCardFreeze;

    public Account(int id, String name, int bankCard, int password, double balance, int bankCardFreeze) {
        this.id = id;
        this.name = name;
        this.bankCard = bankCard;
        this.password = password;
        this.balance = balance;
        this.bankCardFreeze = bankCardFreeze;
    }

    public int getBankCardFreeze() {
        return bankCardFreeze;
    }

    public void setBankCardFreeze(int bankCardFreeze) {
        this.bankCardFreeze = bankCardFreeze;
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

    public int getBankCard() {
        return bankCard;
    }

    public void setBankCard(int bankCard) {
        this.bankCard = bankCard;
    }

    public int getPassword() {
        return password;
    }

    public void setPassword(int password) {
        this.password = password;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    @Override
    public String toString() {
        return "Account{" +
                "ID=" + id +
                ", 姓名='" + name + '\'' +
                ", 银行卡号=" + bankCard +
                ", 密码=" + password +
                ", 余额=" + balance +
                ", 账户冻结情况=" + bankCardFreeze +
                '}';
    }
}
