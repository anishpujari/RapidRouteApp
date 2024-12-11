package com.example.rapidroute;

public class User {
    private String phone;
    private String email;
    private String password;
    private String role;

    public User(String phone, String password) {
        this.phone = phone;
        this.password = password;
    }

    public User(String email, String phone, String password, String role) {
        this.phone = phone;
        this.email = email;
        this.password = password;
        this.role = role;
    }

    // Getters and Setters
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}
