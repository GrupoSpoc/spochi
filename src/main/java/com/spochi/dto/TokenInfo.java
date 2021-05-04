package com.spochi.dto;

public class TokenInfo {
    private String jwt;
    private UserResponseDTO user;

    public String getJwt() {
        return jwt;
    }

    public TokenInfo setJwt(String jwt) {
        this.jwt = jwt;
        return this;
    }

    public UserResponseDTO getUser() {
        return user;
    }

    public TokenInfo setUser(UserResponseDTO user) {
        this.user = user;
        return this;
    }
}
