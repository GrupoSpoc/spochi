package com.spochi.dto;

import com.spochi.entity.InitiativeStatus;

import java.util.HashMap;
import java.util.Map;

import static com.spochi.dto.DTOComparisonUtil.nullOrEquals;

public class UserResponseDTO {

    private String nickname;
    private int type_id;
    private boolean admin;
    private Map<Integer, Integer> initiatives_by_status;

    public UserResponseDTO() {
        this.initiatives_by_status = new HashMap<>();
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

    public void setInitiatives_by_status(Map<Integer, Integer> initiatives_by_status) {

        this.initiatives_by_status = initiatives_by_status;
    }

    public Map<Integer, Integer> getInitiatives_by_status() {
        return initiatives_by_status;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof UserResponseDTO)) return false;
        final UserResponseDTO other = (UserResponseDTO) obj;

        return nullOrEquals(this.nickname, other.nickname) &&
                nullOrEquals(this.type_id, other.type_id) &&
                nullOrEquals(this.admin, other.admin);
    }
}
