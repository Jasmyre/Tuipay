package com.tuipay.services;

import com.tuipay.models.Student;

public class AppServices {
  private StudentManager studentManager;
  private TransactionManager transactionManager;
  private String currentAccountId;

  public AppServices() {
    initializeManagers();
  }

  public final void initializeManagers() {
    this.transactionManager = new TransactionManager();
    this.studentManager = new StudentManager(transactionManager);
    clearCurrentAccount();
  }

  public StudentManager getStudentManager() {
    return studentManager;
  }

  public TransactionManager getTransactionManager() {
    return transactionManager;
  }

  public void setCurrentAccount(String accountId) {
    this.currentAccountId = accountId;
  }

  public String getCurrentAccountId() {
    return currentAccountId;
  }

  public void clearCurrentAccount() {
    this.currentAccountId = null;
  }

  public Student login(String accountId, String password) {
    if (accountId == null || password == null) {
      clearCurrentAccount();
      return null;
    }

    String normalizedId = accountId.trim();
    Student student = studentManager.authenticate(normalizedId, password);
    if (student == null) {
      clearCurrentAccount();
      return null;
    }

    setCurrentAccount(student.getStudentId());
    return student;
  }

  public void logout() {
    clearCurrentAccount();
  }

  public Student getCurrentStudent() {
    if (currentAccountId == null) {
      return null;
    }
    return studentManager.findStudentById(currentAccountId);
  }
}
