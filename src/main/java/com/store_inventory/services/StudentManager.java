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

  public List<Student> getAllStudents() {
    return students;
  }

  public Student findStudentById(String studentId) {
    if (studentId == null) {
      return null;
    }
    for (Student student : students) {
      if (studentId.equals(student.getStudentId())) {
        return student;
      }
    }
    return null;
  }

  public Student authenticate(String studentId, String password) {
    Student student = findStudentById(studentId);
    if (student == null) {
      return null;
    }
    if (student.getPassword() == null || password == null) {
      return null;
    }
    return student.getPassword().equals(password) ? student : null;
  }

  public void addStudent(Student student) {
    if (student == null || student.getStudentId() == null
        || findStudentById(student.getStudentId()) != null) {
      return;
    }
    students.add(student);
  }

  public void topUpWallet(Student student, double amount) {
    if (student == null) {
      throw new IllegalArgumentException("Student is required.");
    }
    student.topUp(amount);
    Transaction transaction =
        transactionManager.createTopUpTransaction(student, amount);
    student.addTransaction(transaction);
  }

  public boolean payTuition(Student student, double amount) {
    if (student == null) {
      return false;
    }
    try {
      student.payTuition(amount);
      Transaction transaction =
          transactionManager.createPaymentTransaction(student, amount);
      student.addTransaction(transaction);
      return true;
    } catch (IllegalArgumentException ignored) {
      return false;
    }
  }

  public void updateTuitionBalance(String studentId, double amount) {
    Student student = findStudentById(studentId);
    if (student == null) {
      return;
    }
    double updatedBalance = student.getTuitionBalance() + amount;
    student.setTuitionBalance(Math.max(0, updatedBalance));
  }

  public List<Transaction> getPaymentHistory(Student student) {
    if (student == null) {
      return new ArrayList<>();
    }
    return new ArrayList<>(student.getTransactionHistory());
  }
}
