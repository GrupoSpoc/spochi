package com.spochi.dto;

public class AdminLoginRequestDTO {
    private String uid;
    private String password;

    public String getUid() {
        return uid;
    }

    public AdminLoginRequestDTO setUid(String uid) {
        this.uid = uid;
        return this;
    }

    public String getPassword() {
        return password;
    }

    public AdminLoginRequestDTO setPassword(String password) {
        this.password = password;
        return this;
    }
}
