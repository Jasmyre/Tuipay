package com.store_inventory.models;

import java.util.Date;
import java.util.UUID;

public class Transaction {
    private String transactionId;
    private String studentId;
    private TransactionType type;
    private double amount;
    private Date transactionDate;
    private double remainingTuitionBalance;

    public Transaction() {
        this.transactionId = UUID.randomUUID().toString();
        this.transactionDate = new Date();
    }

    public Transaction(String studentId, TransactionType type, double amount, double remainingTuitionBalance) {
        this.transactionId = UUID.randomUUID().toString();
        this.studentId = studentId;
        this.type = type;
        this.amount = amount;
        this.transactionDate = new Date();
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
        return transactionDate;
    }

    public void setTransactionDate(Date transactionDate) {
        this.transactionDate = transactionDate;
    }

    public double getRemainingTuitionBalance() {
        return remainingTuitionBalance;
    }

    public void setRemainingTuitionBalance(double remainingTuitionBalance) {
        this.remainingTuitionBalance = remainingTuitionBalance;
    }

    public String generateReceipt() {
        return "Receipt\n"
                + "Transaction ID: " + transactionId + "\n"
                + "Student ID: " + studentId + "\n"
                + "Type: " + type + "\n"
                + "Amount: " + amount + "\n"
                + "Date: " + transactionDate + "\n"
                + "Remaining Tuition Balance: " + remainingTuitionBalance;
    }
}
