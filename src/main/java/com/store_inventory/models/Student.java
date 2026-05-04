package com.store_inventory.models;

import java.util.ArrayList;
import java.util.List;

public class Student {
    private String studentId;
    private String username;
    private String fullName;
    private String password;
    private double tuitionBalance;
    private double walletBalance;
    private List<Transaction> transactions;

    public Student() {
        this.transactions = new ArrayList<>();
    }

    public Student(String studentId, String username, String fullName, String password, double tuitionBalance, double walletBalance) {
        this.studentId = studentId;
        this.username = username;
        this.fullName = fullName;
        this.password = password;
        this.tuitionBalance = tuitionBalance;
        this.walletBalance = walletBalance;
        this.transactions = new ArrayList<>();
    }

    public String getStudentId() {
        return studentId;
    }

    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public double getTuitionBalance() {
        return tuitionBalance;
    }

    public void setTuitionBalance(double tuitionBalance) {
        this.tuitionBalance = tuitionBalance;
    }

    public double getWalletBalance() {
        return walletBalance;
    }

    public void setWalletBalance(double walletBalance) {
        this.walletBalance = walletBalance;
    }

    public List<Transaction> getTransactions() {
        return transactions;
    }

    public void setTransactions(List<Transaction> transactions) {
        this.transactions = transactions;
    }

    public void addTransaction(Transaction transaction) {
        if (transaction != null) {
            transactions.add(transaction);
        }
    }

    public List<Transaction> getTransactionHistory() {
        return new ArrayList<>(transactions);
    }
}
