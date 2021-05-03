package com.spochi.dto;

public class UserResponseDTO {

    private String nickname;
    private int type_id;
    private int amount_of_initiavies;
    private boolean admin;

    public UserResponseDTO(){

    }

    public boolean isAdmin() {
        return admin;
    }

    public void setAdmin(boolean admin) {
        this.admin = admin;
    }

    public int getType_id() {
        return type_id;
    }

    public void setType_id(int type_id) {
        this.type_id = type_id;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public int getAmount_of_initiavies() { return amount_of_initiavies; }

    public void setAmount_of_initiavies(int amount_initiavies) {this.amount_of_initiavies = amount_initiavies;}

}
