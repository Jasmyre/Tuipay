package com.store_inventory.services;

import com.store_inventory.models.Student;
import com.store_inventory.models.Transaction;
import com.store_inventory.models.TransactionType;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class TransactionManager {
  public Transaction createTopUpTransaction(Student student, double amount) {
    if (student == null || amount <= 0) {
      return null;
    }
    return new Transaction(student.getStudentId(), TransactionType.TOP_UP, amount,
                           student.getTuitionBalance());
  }

  public Transaction createPaymentTransaction(Student student, double amount) {
    if (student == null || amount <= 0) {
      return null;
    }
    return new Transaction(student.getStudentId(), TransactionType.TUITION_PAYMENT,
                           amount, student.getTuitionBalance());
  }

  public List<Transaction> getAllTransactions(List<Student> students) {
    List<Transaction> all = new ArrayList<>();
    if (students == null) {
      return all;
    }

    for (Student student : students) {
      if (student == null) {
        continue;
      }
      all.addAll(student.getTransactionHistory());
    }
    all.sort(Comparator.comparing(Transaction::getTransactionDate).reversed());
    return all;
  }
}
