package com.spochi.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
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