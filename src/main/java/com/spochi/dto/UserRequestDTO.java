package com.spochi.dto;

public class UserRequestDTO {

    private String nickname;
    private Integer type_id;

    public UserRequestDTO() {

    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public Integer getType_id() {
        return type_id;
    }

    public void setType_id(Integer type_id) {
        this.type_id = type_id;
    }
}
