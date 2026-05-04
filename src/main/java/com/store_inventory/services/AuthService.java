package com.store_inventory.services;

import com.store_inventory.models.Admin;
import com.store_inventory.models.Student;
import java.util.List;

public class AuthService {
  private final List<Student> students;
  private final Admin admin;
  private final SessionService sessionService;

  public AuthService(List<Student> students, Admin admin,
                     SessionService sessionService) {
    this.students = students;
    this.admin = admin;
    this.sessionService = sessionService;
  }

  public Object login(String id, String password) {
    if (admin != null && admin.authenticate(id, password)) {
      sessionService.setCurrentAdmin(admin);
      return admin;
    }

    for (Student student : students) {
      if (student.getStudentId().equals(id)
          && student.getPassword().equals(password)) {
        sessionService.setCurrentStudent(student);
        return student;
      }
    }

    return null;
  }

  public void logout() { sessionService.clearSession(); }
}
