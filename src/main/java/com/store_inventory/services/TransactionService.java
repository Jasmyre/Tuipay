package com.store_inventory.services;

import com.store_inventory.models.Student;
import com.store_inventory.models.Transaction;
import com.store_inventory.models.TransactionType;
import java.util.ArrayList;
import java.util.List;

public class TransactionService {
  public Transaction createTopUpTransaction(Student student, double amount) {
    return new Transaction(student.getStudentId(), TransactionType.TOP_UP, amount,
                           student.getTuitionBalance());
  }

  public Transaction createPaymentTransaction(Student student, double amount) {
    return new Transaction(student.getStudentId(),
                           TransactionType.TUITION_PAYMENT,
                           amount,
                           student.getTuitionBalance());
  }

  public List<Transaction> getAllTransactions(List<Student> students) {
    List<Transaction> allTransactions = new ArrayList<>();
    for (Student student : students) {
      allTransactions.addAll(student.getTransactionHistory());
    }
    return allTransactions;
  }
}
