package com.store_inventory.models;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Student {
  private String studentId;
  private String username;
  private String fullName;
  private String password;
  private double tuitionBalance;
  private double walletBalance;
  private final List<Transaction> transactions;

  public Student(String studentId, String username, String fullName, String password,
                 double tuitionBalance, double walletBalance) {
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

  public String getUsername() {
    return username;
  }

  public void setUsername(String username) {
    this.username = username;
  }

  public String getFullName() {
    return fullName;
  }

  public void setFullName(String fullName) {
    this.fullName = fullName;
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

  public void topUp(double amount) {
    if (amount <= 0) {
      throw new IllegalArgumentException("Top-up amount must be greater than zero.");
    }
    walletBalance += amount;
  }

  public void payTuition(double amount) {
    if (amount <= 0) {
      throw new IllegalArgumentException("Payment amount must be greater than zero.");
    }
    if (amount > walletBalance) {
      throw new IllegalArgumentException("Insufficient wallet balance.");
    }
    if (tuitionBalance <= 0) {
      throw new IllegalArgumentException("No remaining tuition balance.");
    }

    double payment = Math.min(amount, tuitionBalance);
    walletBalance -= payment;
    tuitionBalance -= payment;
  }

  public void addTransaction(Transaction transaction) {
    if (transaction != null) {
      transactions.add(transaction);
    }
  }

  public List<Transaction> getTransactionHistory() {
    return Collections.unmodifiableList(transactions);
  }
}
