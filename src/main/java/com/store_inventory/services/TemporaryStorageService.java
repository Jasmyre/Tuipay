package com.store_inventory.services;

import com.store_inventory.models.Admin;
import com.store_inventory.models.Student;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TemporaryStorageService {
  private List<Student> students;
  private Admin admin;
  private final Map<String, Object> sessionData;
  private final SeedDataService seedDataService;

  public TemporaryStorageService(SeedDataService seedDataService) {
    this.seedDataService = seedDataService;
    this.sessionData = new HashMap<>();
    initializeSeedData();
  }

  public void initializeSeedData() {
    students = seedDataService.seedStudents();
    admin = seedDataService.seedAdmin();
    sessionData.clear();
  }

  public void resetOnAppClose() {
    students = null;
    admin = null;
    sessionData.clear();
  }

  public List<Student> getStudents() { return students; }

  public Admin getAdmin() { return admin; }

  public Map<String, Object> getSessionData() { return sessionData; }
}
