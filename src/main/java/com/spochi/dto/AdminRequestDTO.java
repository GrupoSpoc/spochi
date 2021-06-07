package com.spochi.dto;

public class AdminRequestDTO {

    private String uid;
    private String password;

    public AdminRequestDTO(){

    }

    public String getUid() {return uid;}

    public void setUid(String uid) {this.uid = uid;}

    public String getPassword() {return password;}

    public void setPassword(String password) {this.password = password;}
}
