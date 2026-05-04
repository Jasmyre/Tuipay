package com.store_inventory.models;

public class Admin {
    private String adminId;
    private String username;
    private String password;

    public Admin() {
    }

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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public boolean authenticate(String id, String password) {
        return this.adminId != null
                && this.password != null
                && this.adminId.equals(id)
                && this.password.equals(password);
    }
}
