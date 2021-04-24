package com.spochi.dto;

public class RewardResponseDTO {

    private int user_type_id;
    private int initiative_type_id;
    private int points;

    public RewardResponseDTO(){

    }

    public int getUser_type_id() {
        return user_type_id;
    }

    public void setUser_type_id(int user_type_id) {
        this.user_type_id = user_type_id;
    }

    public int getInitiative_type_id() {
        return initiative_type_id;
    }

    public void setInitiative_type_id(int initiative_type_id) {
        this.initiative_type_id = initiative_type_id;
    }

    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }
}
