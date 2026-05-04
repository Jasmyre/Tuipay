package com.store_inventory.services;

import com.store_inventory.models.Admin;
import com.store_inventory.models.Student;
import java.util.ArrayList;
import java.util.List;

public class SeedDataService {
  public List<Student> seedStudents() {
    List<Student> students = new ArrayList<>();
    students.add(new Student("202311-1001", "student1", "Juan Dela Cruz",
                             "student123", 35000.0, 2000.0));
    students.add(new Student("202311-1002", "student2", "Maria Santos",
                             "student123", 42000.0, 1500.0));
    students.add(new Student("202311-1003", "student3", "Carlo Reyes",
                             "student123", 28000.0, 500.0));
    return students;
  }

  public Admin seedAdmin() {
    return new Admin("ADMIN001", "admin", "admin123");
  }
}
