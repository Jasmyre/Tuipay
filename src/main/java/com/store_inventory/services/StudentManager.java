package com.store_inventory.services;

import com.store_inventory.models.Student;
import com.store_inventory.models.Transaction;
import java.util.ArrayList;
import java.util.List;

public class StudentManager {
  private final List<Student> students = new ArrayList<>();
  private final TransactionManager transactionManager;

  public StudentManager(TransactionManager transactionManager) {
    this.transactionManager = transactionManager;
  }

  public List<Student> getAllStudents() { return students; }

  public void addStudent(Student student) {
    if (student != null) {
      students.add(student);
    }
  }

  public void clearStudents() { students.clear(); }

  public Student findStudentById(String studentId) {
    if (studentId == null) {
      return null;
    }
    for (Student student : students) {
      if (student != null && studentId.equals(student.getStudentId())) {
        return student;
      }
    }
    return null;
  }

  public void topUpWallet(Student student, double amount) {
    if (student == null || amount <= 0) {
      return;
    }
    student.setWalletBalance(student.getWalletBalance() + amount);
    Transaction transaction =
        transactionManager.createTopUpTransaction(student, amount);
    student.addTransaction(transaction);
  }

  public boolean payTuition(Student student, double amount) {
    if (student == null || amount <= 0 || amount > student.getWalletBalance()
        || amount > student.getTuitionBalance()) {
      return false;
    }

    student.setWalletBalance(student.getWalletBalance() - amount);
    student.setTuitionBalance(student.getTuitionBalance() - amount);
    Transaction transaction =
        transactionManager.createPaymentTransaction(student, amount);
    student.addTransaction(transaction);
    return true;
  }

  public void updateTuitionBalance(String studentId, double amount) {
    Student student = findStudentById(studentId);
    if (student == null) {
      return;
    }
    student.setTuitionBalance(student.getTuitionBalance() + amount);
  }

  public List<Transaction> getPaymentHistory(Student student) {
    if (student == null) {
      return new ArrayList<>();
    }
    return student.getTransactionHistory();
  }
}
