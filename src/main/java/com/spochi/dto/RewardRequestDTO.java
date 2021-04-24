package com.spochi.dto;

public class RewardRequestDTO {

    private Integer user_type_id;
    private Integer initiative_type_id;
    private Integer points;

    public RewardRequestDTO(){

    }

    public Integer getUser_type_id() {
        return user_type_id;
    }

    public void setUser_type_id(Integer user_type_id) {
        this.user_type_id = user_type_id;
    }

    public Integer getInitiative_type_id() {
        return initiative_type_id;
    }

    public void setInitiative_type_id(Integer initiative_type_id) {
        this.initiative_type_id = initiative_type_id;
    }

    public Integer getPoints() {
        return points;
    }

    public void setPoints(Integer points) {
        this.points = points;
    }
}
