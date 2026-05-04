package com.store_inventory.services;

import com.store_inventory.dto.AdminDashboardDTO;
import com.store_inventory.models.Student;
import com.store_inventory.models.Transaction;
import com.store_inventory.models.TransactionType;
import java.util.List;

public class AdminService {
  private final List<Student> students;
  private final TransactionService transactionService;

  public AdminService(List<Student> students,
                      TransactionService transactionService) {
    this.students = students;
    this.transactionService = transactionService;
  }

  public AdminDashboardDTO viewAdminDashboard() {
    double totalPaymentsMade = 0.0;
    double totalRemainingBalances = 0.0;

    for (Student student : students) {
      totalRemainingBalances += student.getTuitionBalance();
      for (Transaction transaction : student.getTransactionHistory()) {
        if (transaction.getType() == TransactionType.TUITION_PAYMENT) {
          totalPaymentsMade += transaction.getAmount();
        }
      }
    }

    return new AdminDashboardDTO(students.size(), totalPaymentsMade,
                                 totalRemainingBalances);
  }

  public List<Student> getAllStudents() { return students; }

  public List<Transaction> getAllTransactions() {
    return transactionService.getAllTransactions(students);
  }

  public void updateTuitionBalance(String studentId, double amount) {
    for (Student student : students) {
      if (student.getStudentId().equals(studentId)) {
        student.setTuitionBalance(amount);
        break;
      }
    }
  }
}
