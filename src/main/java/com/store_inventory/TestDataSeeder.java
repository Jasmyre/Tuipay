package com.store_inventory;

import com.store_inventory.models.Student;
import com.store_inventory.services.AppServices;
import com.store_inventory.services.StudentManager;

public final class TestDataSeeder {
  private TestDataSeeder() {}

  public static void seed(AppServices services) {
    if (services == null) {
      return;
    }

    StudentManager studentManager = services.getStudentManager();

    seedStudent(studentManager, "202509-0033", "student01", "Juan Dela Cruz",
                "password", 12000, 2000);
    seedStudent(studentManager, "202311-1002", "student02", "Maria Santos",
                "student123", 15000, 500);

    Student student01 = studentManager.findStudentById("202509-0033");
    Student student02 = studentManager.findStudentById("202311-1002");

    if (student01 != null && student01.getTransactionHistory().isEmpty()) {
      studentManager.topUpWallet(student01, 1000);
      studentManager.payTuition(student01, 2500);
      studentManager.topUpWallet(student01, 1000);
      studentManager.payTuition(student01, 2500);
      studentManager.topUpWallet(student01, 1000);
      studentManager.payTuition(student01, 2500);
      studentManager.topUpWallet(student01, 1000);
      studentManager.payTuition(student01, 2500);
      studentManager.topUpWallet(student01, 1000);
      studentManager.payTuition(student01, 2500);
      studentManager.topUpWallet(student01, 1000);
      studentManager.payTuition(student01, 2500);
    }

    if (student02 != null && student02.getTransactionHistory().isEmpty()) {
      studentManager.topUpWallet(student02, 3000);
      studentManager.payTuition(student02, 2000);
      studentManager.payTuition(student02, 10000);
    }
  }

  private static void seedStudent(StudentManager studentManager,
                                  String studentId, String username,
                                  String fullName, String password,
                                  double tuitionBalance, double walletBalance) {
    if (!isValidStudentId(studentId)) {
      return;
    }

    if (studentManager.findStudentById(studentId) != null) {
      return;
    }

    studentManager.addStudent(new Student(studentId, username, fullName,
                                          password, tuitionBalance,
                                          walletBalance));
  }

  private static boolean isValidStudentId(String studentId) {
    return studentId != null && studentId.matches("\\d{6}-\\d{4}");
  }
}
