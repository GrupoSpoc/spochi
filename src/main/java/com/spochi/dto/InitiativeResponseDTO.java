package com.spochi.dto;

import lombok.Setter;

import static com.spochi.dto.DTOComparisonUtil.nullOrEquals;

public class InitiativeResponseDTO {

    private String _id;
    private String description;
    private String image;
    private String date;
    private String nickname;
    private int status_id;
    private boolean is_from_current_user;

    public InitiativeResponseDTO(){

    }

    public String get_id() {
        return _id;
    }


    public void set_id(String _id) {
        this._id = _id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public int getStatus_id() {
        return status_id;
    }

    public void setStatus_id(int status_id) {
        this.status_id = status_id;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof InitiativeResponseDTO)) return false;
        final InitiativeResponseDTO other = (InitiativeResponseDTO) obj;

        return nullOrEquals(this.nickname, other.nickname) &&
                nullOrEquals(this.date, other.date) &&
                nullOrEquals(this.description, other.description) &&
                nullOrEquals(this.image, other.image) &&
                nullOrEquals(this.status_id, other.status_id) &&
                nullOrEquals(this._id, other._id);
    }

    public InitiativeResponseDTO set_from_current_user(boolean is_from_current_user) {
        this.is_from_current_user = is_from_current_user;
        return this;
    }

    public boolean isIs_from_current_user() {
        return is_from_current_user;
    }
}
