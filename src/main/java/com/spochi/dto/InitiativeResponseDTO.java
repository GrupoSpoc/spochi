package com.spochi.dto;

import com.spochi.entity.Initiative;

public class InitiativeResponseDTO {

    public String get_id() {
        return _id;
    }

    public InitiativeResponseDTO set_id(String _id) {
        this._id = _id;
        return this;
    }

    private String _id;
    private String description;
    private String image;
    private String date;
    private String nickname;
    private int status_id;

    public InitiativeResponseDTO(){

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

        return this.nickname.equals(other.nickname) &&
                this.date.equals(other.date) &&
                this.description.equals(other.description) &&
                this.image.equals(other.image) &&
                this.status_id == other.status_id &&
                this._id.equals(other._id);
    }
}
