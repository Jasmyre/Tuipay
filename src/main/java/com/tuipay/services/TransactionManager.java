package com.tuipay.services;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.tuipay.models.Student;
import com.tuipay.models.Transaction;
import com.tuipay.models.TransactionType;

public class TransactionManager {
  private final List<Transaction> transactions = new ArrayList<>();

  public Transaction createTopUpTransaction(Student student, double amount) {
    Transaction transaction =
        new Transaction(student.getStudentId(), TransactionType.TOP_UP, amount,
                        new Date(), student.getTuitionBalance());
    transactions.add(transaction);
    return transaction;
  }

  public Transaction createPaymentTransaction(Student student, double amount) {
    Transaction transaction =
        new Transaction(student.getStudentId(), TransactionType.TUITION_PAYMENT,
                        amount, new Date(), student.getTuitionBalance());
    transactions.add(transaction);
    return transaction;
  }

  public void addTransaction(Transaction transaction) {
    if (transaction != null) {
      transactions.add(transaction);
    }
  }

  public List<Transaction> getAllTransactions(List<Student> students) {
    return transactions;
  }

  public List<Transaction> getAllTransactions() {
    return transactions;
  }
}
