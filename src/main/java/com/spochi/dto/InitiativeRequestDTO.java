package com.spochi.dto;

public class InitiativeRequestDTO {

    private String _id;
    private boolean isFromCurrentUser;
    private String description;
    private String image;
    private String date;
    private String nickname;
    private int status_id;

    public InitiativeRequestDTO(){

    }

    public String get_id() { return _id; }

    public void set_id(String _id) { this._id = _id; }

    public boolean isFromCurrentUser() { return isFromCurrentUser; }

    public void setFromCurrentUser(boolean fromCurrentUser) { isFromCurrentUser = fromCurrentUser; }

    public String getDescription() { return description; }

    public void setDescription(String description) { this.description = description; }

    public String getImage() { return image; }

    public void setImage(String image) { this.image = image; }

    public String getDate() { return date; }

    public void setDate(String date) { this.date = date; }

    public String getNickname() { return nickname;}

    public void setNickname(String nickname) { this.nickname = nickname; }

    public int getStatus_id() { return status_id; }

    public void setStatus_id(int status_id) { this.status_id = status_id; }
}


