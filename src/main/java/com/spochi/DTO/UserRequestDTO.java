package com.spochi.DTO;

public class UserRequestDTO {

    private String nickname;
    private int type_id;

    public UserRequestDTO() {

    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public int getType_id() {
        return type_id;
    }

    public void setType_id(int type_id) {
        this.type_id = type_id;
    }
}
