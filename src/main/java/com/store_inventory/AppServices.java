package com.store_inventory;

import com.store_inventory.services.AdminService;
import com.store_inventory.services.AuthService;
import com.store_inventory.services.SeedDataService;
import com.store_inventory.services.SessionService;
import com.store_inventory.services.StudentService;
import com.store_inventory.services.TemporaryStorageService;
import com.store_inventory.services.TransactionService;

public class AppServices {
  private final SeedDataService seedDataService;
  private final TemporaryStorageService temporaryStorageService;
  private final SessionService sessionService;
  private final TransactionService transactionService;
  private final AuthService authService;
  private final StudentService studentService;
  private final AdminService adminService;

  public AppServices() {
    seedDataService = new SeedDataService();
    temporaryStorageService = new TemporaryStorageService(seedDataService);
    sessionService = new SessionService();
    transactionService = new TransactionService();

    authService = new AuthService(temporaryStorageService.getStudents(),
                                  temporaryStorageService.getAdmin(),
                                  sessionService);
    studentService = new StudentService(transactionService);
    adminService = new AdminService(temporaryStorageService.getStudents(),
                                    transactionService);
  }

  public SeedDataService getSeedDataService() { return seedDataService; }

  public TemporaryStorageService getTemporaryStorageService() {
    return temporaryStorageService;
  }

  public SessionService getSessionService() { return sessionService; }

  public TransactionService getTransactionService() { return transactionService; }

  public AuthService getAuthService() { return authService; }

  public StudentService getStudentService() { return studentService; }

  public AdminService getAdminService() { return adminService; }
}
