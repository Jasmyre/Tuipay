package com.store_inventory.models;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.UUID;

public class Transaction {
  private static final DecimalFormat CURRENCY = new DecimalFormat("#,##0.00");
  private static final SimpleDateFormat DATE_FORMAT =
      new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH);

  private String transactionId;
  private String studentId;
  private TransactionType type;
  private double amount;
  private Date transactionDate;
  private double remainingTuitionBalance;

  public Transaction(String studentId, TransactionType type, double amount,
                     Date transactionDate, double remainingTuitionBalance) {
    this(UUID.randomUUID().toString(), studentId, type, amount, transactionDate,
         remainingTuitionBalance);
  }

  public Transaction(String transactionId, String studentId, TransactionType type,
                     double amount, Date transactionDate,
                     double remainingTuitionBalance) {
    this.transactionId = transactionId;
    this.studentId = studentId;
    this.type = type;
    this.amount = amount;
    this.transactionDate =
        transactionDate == null ? new Date() : new Date(transactionDate.getTime());
    this.remainingTuitionBalance = remainingTuitionBalance;
  }

  public String getTransactionId() {
    return transactionId;
  }

  public void setTransactionId(String transactionId) {
    this.transactionId = transactionId;
  }

  public String getStudentId() {
    return studentId;
  }

  public void setStudentId(String studentId) {
    this.studentId = studentId;
  }

  public TransactionType getType() {
    return type;
  }

  public void setType(TransactionType type) {
    this.type = type;
  }

  public double getAmount() {
    return amount;
  }

  public void setAmount(double amount) {
    this.amount = amount;
  }

  public Date getTransactionDate() {
    return new Date(transactionDate.getTime());
  }

  public void setTransactionDate(Date transactionDate) {
    this.transactionDate =
        transactionDate == null ? new Date() : new Date(transactionDate.getTime());
  }

  public double getRemainingTuitionBalance() {
    return remainingTuitionBalance;
  }

  public void setRemainingTuitionBalance(double remainingTuitionBalance) {
    this.remainingTuitionBalance = remainingTuitionBalance;
  }

  public String generateReceipt() {
    return "Transaction ID: " + transactionId + "\n"
           + "Student ID: " + studentId + "\n"
           + "Type: " + type + "\n"
           + "Amount: PHP " + CURRENCY.format(amount) + "\n"
           + "Date: " + DATE_FORMAT.format(transactionDate) + "\n"
           + "Remaining Tuition Balance: PHP "
           + CURRENCY.format(remainingTuitionBalance);
  }
}
