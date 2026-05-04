package com.store_inventory.services;

import com.store_inventory.models.Admin;
import com.store_inventory.models.Student;
import com.store_inventory.models.Transaction;
import java.util.List;

public class AdminManager {
  private final StudentManager studentManager;
  private final TransactionManager transactionManager;
  private Admin admin;

  public AdminManager(StudentManager studentManager,
                      TransactionManager transactionManager) {
    this.studentManager = studentManager;
    this.transactionManager = transactionManager;
  }

  public Admin getAdmin() { return admin; }

  public void setAdmin(Admin admin) { this.admin = admin; }

  public boolean authenticateAdmin(String id, String password) {
    return admin != null && admin.authenticate(id, password);
  }

  public List<Student> getAllStudents() { return studentManager.getAllStudents(); }

  public List<Student> viewStudentRecords() { return getAllStudents(); }

  public List<Transaction> viewAllTransactions() {
    return transactionManager.getAllTransactions(studentManager.getAllStudents());
  }
}
