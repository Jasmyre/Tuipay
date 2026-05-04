package com.store_inventory.models;

public class Admin {
  private String adminId;
  private String username;
  private String password;

  public Admin(String adminId, String username, String password) {
    this.adminId = adminId;
    this.username = username;
    this.password = password;
  }

  public String getAdminId() {
    return adminId;
  }

  public void setAdminId(String adminId) {
    this.adminId = adminId;
  }

  public String getUsername() {
    return username;
  }

  public void setUsername(String username) {
    this.username = username;
  }

  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  public boolean authenticate(String id, String password) {
    return adminId != null && this.password != null && adminId.equals(id)
           && this.password.equals(password);
  }
}
