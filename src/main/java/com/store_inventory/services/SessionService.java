package com.store_inventory.services;

import com.store_inventory.models.Admin;
import com.store_inventory.models.Student;

public class SessionService {
  private Student currentStudent;
  private Admin currentAdmin;

  public void setCurrentStudent(Student student) {
    currentStudent = student;
    currentAdmin = null;
  }

  public Student getCurrentStudent() { return currentStudent; }

  public void setCurrentAdmin(Admin admin) {
    currentAdmin = admin;
    currentStudent = null;
  }

  public Admin getCurrentAdmin() { return currentAdmin; }

  public void clearSession() {
    currentStudent = null;
    currentAdmin = null;
  }
}
