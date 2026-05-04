package com.store_inventory.services;

import com.store_inventory.models.Admin;
import com.store_inventory.models.Student;

public class AppManagerService {
  private StudentManager studentManager;
  private TransactionManager transactionManager;
  private AdminManager adminManager;
  private String currentAccountId;
  private String currentAccountRole;

  public static final String ROLE_ADMIN = "ADMIN";
  public static final String ROLE_STUDENT = "STUDENT";

  public AppManagerService() { initializeManagers(); }

  public void initializeManagers() {
    transactionManager = new TransactionManager();
    studentManager = new StudentManager(transactionManager);
    adminManager = new AdminManager(studentManager, transactionManager);
    clearCurrentAccount();
  }

  public StudentManager getStudentManager() { return studentManager; }

  public TransactionManager getTransactionManager() { return transactionManager; }

  public AdminManager getAdminManager() { return adminManager; }

  public void setCurrentAccount(String accountId, String role) {
    currentAccountId = accountId;
    currentAccountRole = role;
  }

  public String getCurrentAccountId() { return currentAccountId; }

  public String getCurrentAccountRole() { return currentAccountRole; }

  public void clearCurrentAccount() {
    currentAccountId = null;
    currentAccountRole = null;
  }

  public Object login(String id, String password) {
    if (adminManager.authenticateAdmin(id, password)) {
      setCurrentAccount(id, ROLE_ADMIN);
      return adminManager.getAdmin();
    }

    Student student = studentManager.findStudentById(id);
    if (student != null && student.getPassword() != null
        && student.getPassword().equals(password)) {
      setCurrentAccount(id, ROLE_STUDENT);
      return student;
    }

    return null;
  }

  public void logout() { clearCurrentAccount(); }

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
