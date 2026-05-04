package com.store_inventory.services;

import com.store_inventory.dto.StudentDashboardDTO;
import com.store_inventory.models.Student;
import com.store_inventory.models.Transaction;
import java.util.List;

public class StudentService {
  private final TransactionService transactionService;

  public StudentService(TransactionService transactionService) {
    this.transactionService = transactionService;
  }

  public StudentDashboardDTO viewStudentDashboard(Student student) {
    return new StudentDashboardDTO(student.getFullName(), student.getStudentId(),
                                   student.getTuitionBalance(),
                                   student.getWalletBalance());
  }

  public void topUpWallet(Student student, double amount) {
    if (student == null || amount <= 0) {
      return;
    }
    student.topUp(amount);
    Transaction transaction =
        transactionService.createTopUpTransaction(student, amount);
    student.addTransaction(transaction);
  }

  public boolean payTuition(Student student, double amount) {
    if (student == null || amount <= 0) {
      return false;
    }
    boolean successful = student.payTuition(amount);
    if (successful) {
      Transaction transaction =
          transactionService.createPaymentTransaction(student, amount);
      student.addTransaction(transaction);
    }
    return successful;
  }

  public List<Transaction> getPaymentHistory(Student student) {
    return student.getTransactionHistory();
  }
}
