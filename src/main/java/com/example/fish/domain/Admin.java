package com.example.fish.domain;
import java.sql.Timestamp;

public class Admin {
    private int adminId;
    private String username;
    private String password;
    private Timestamp createdAt;

    public Admin(int adminId, String username, String password, Timestamp createdAt) {
        this.adminId = adminId;
        this.username = username;
        this.password = password;
        this.createdAt = createdAt;
    }

    public int getAdminId() {
        return adminId;
    }

    public void setAdminId(int adminId) {
        this.adminId = adminId;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
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
}
