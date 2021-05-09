package com.spochi.dto;

import static com.spochi.dto.DTOComparisonUtil.nullOrEquals;

public class UserResponseDTO {

    private String nickname;
    private int type_id;
    private int amount_of_initiatives;
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

    public int getAmount_of_initiatives() { return amount_of_initiatives; }

    public void setAmount_of_initiatives(int amount_initiavies) {this.amount_of_initiatives = amount_initiavies;}

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof UserResponseDTO)) return false;
        final UserResponseDTO other = (UserResponseDTO) obj;

        return nullOrEquals(this.nickname, other.nickname) &&
                nullOrEquals(this.type_id, other.type_id) &&
                nullOrEquals(this.amount_of_initiatives, other.amount_of_initiatives) &&
                nullOrEquals(this.admin, other.admin);
    }
}
