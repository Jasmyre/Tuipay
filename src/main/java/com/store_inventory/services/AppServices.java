package com.store_inventory.services;

import com.store_inventory.models.Admin;
import com.store_inventory.models.Student;

public class AppServices {
  private static final String ROLE_ADMIN = "ADMIN";
  private static final String ROLE_STUDENT = "STUDENT";

  private StudentManager studentManager;
  private TransactionManager transactionManager;
  private AdminManager adminManager;
  private String currentAccountId;
  private String currentAccountRole;

  public AppServices() {
    initializeManagers();
  }

  public final void initializeManagers() {
    this.transactionManager = new TransactionManager();
    this.studentManager = new StudentManager(transactionManager);
    this.adminManager = new AdminManager(studentManager, transactionManager);
    clearCurrentAccount();
  }

  public StudentManager getStudentManager() {
    return studentManager;
  }

  public TransactionManager getTransactionManager() {
    return transactionManager;
  }

  public AdminManager getAdminManager() {
    return adminManager;
  }

  public void setCurrentAccount(String accountId, String role) {
    this.currentAccountId = accountId;
    this.currentAccountRole = role;
  }

  public String getCurrentAccountId() {
    return currentAccountId;
  }

  public String getCurrentAccountRole() {
    return currentAccountRole;
  }

  public void clearCurrentAccount() {
    this.currentAccountId = null;
    this.currentAccountRole = null;
  }

  public Object login(String accountId, String password) {
    if (accountId == null || password == null) {
      clearCurrentAccount();
      return null;
    }

    String normalizedId = accountId.trim();
    if (adminManager.authenticate(normalizedId, password)) {
      setCurrentAccount(normalizedId, ROLE_ADMIN);
      return adminManager.getAdmin();
    }

    Student student = studentManager.authenticate(normalizedId, password);
    if (student != null) {
      setCurrentAccount(student.getStudentId(), ROLE_STUDENT);
      return student;
    }

    clearCurrentAccount();
    return null;
  }

  public void logout() {
    clearCurrentAccount();
  }

  public Student getCurrentStudent() {
    if (!ROLE_STUDENT.equals(currentAccountRole) || currentAccountId == null) {
      return null;
    }
    return studentManager.findStudentById(currentAccountId);
  }

  public Admin getCurrentAdmin() {
    if (!ROLE_ADMIN.equals(currentAccountRole) || currentAccountId == null) {
      return null;
    }
    Admin admin = adminManager.getAdmin();
    if (admin == null) {
      return null;
    }
    return currentAccountId.equals(admin.getAdminId()) ? admin : null;
  }
}
