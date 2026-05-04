package com.store_inventory.dto;

public class AdminDashboardDTO {
  private final int totalStudents;
  private final double totalPaymentsMade;
  private final double totalRemainingBalances;

  public AdminDashboardDTO(int totalStudents, double totalPaymentsMade,
                           double totalRemainingBalances) {
    this.totalStudents = totalStudents;
    this.totalPaymentsMade = totalPaymentsMade;
    this.totalRemainingBalances = totalRemainingBalances;
  }

  public int getTotalStudents() { return totalStudents; }

  public double getTotalPaymentsMade() { return totalPaymentsMade; }

  public double getTotalRemainingBalances() { return totalRemainingBalances; }
}
