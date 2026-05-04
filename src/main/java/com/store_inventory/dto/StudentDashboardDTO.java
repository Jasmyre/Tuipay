package com.store_inventory.dto;

public class StudentDashboardDTO {
  private final String fullName;
  private final String studentId;
  private final double tuitionBalance;
  private final double walletBalance;

  public StudentDashboardDTO(String fullName, String studentId,
                             double tuitionBalance, double walletBalance) {
    this.fullName = fullName;
    this.studentId = studentId;
    this.tuitionBalance = tuitionBalance;
    this.walletBalance = walletBalance;
  }

  public String getFullName() { return fullName; }

  public String getStudentId() { return studentId; }

  public double getTuitionBalance() { return tuitionBalance; }

  public double getWalletBalance() { return walletBalance; }
}
