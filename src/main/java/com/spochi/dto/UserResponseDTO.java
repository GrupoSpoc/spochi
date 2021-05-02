package com.spochi.dto;

public class UserResponseDTO {

    private String nickname;
    private int type_id;
    private int amount_initiavies;
    private boolean isAdmin;

    public UserResponseDTO(){

    }

    public boolean isAdmin() {
        return isAdmin;
    }

    public void setAdmin(boolean admin) {
        isAdmin = admin;
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

    public int getAmount_initiavies() { return amount_initiavies; }

    public void setAmount_initiavies(int amount_initiavies) {this.amount_initiavies = amount_initiavies;}

}
