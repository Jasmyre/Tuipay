package com.store_inventory;

import com.store_inventory.models.Admin;
import com.store_inventory.models.Student;
import com.store_inventory.services.AppManagerService;

public final class TestDataSeeder {
  private TestDataSeeder() {}

  public static void seed(AppManagerService services) {
    if (services == null) {
      return;
    }

    services.getStudentManager().clearStudents();

    services.getStudentManager().addStudent(
        new Student("202311-1001", "student01", "Student One", "student123",
                    15000.0, 3000.0));
    services.getStudentManager().addStudent(
        new Student("202311-1002", "student02", "Student Two", "student123",
                    12000.0, 1500.0));

    services.getAdminManager().setAdmin(
        new Admin("ADMIN001", "admin", "admin123"));

    services.clearCurrentAccount();
  }
}
