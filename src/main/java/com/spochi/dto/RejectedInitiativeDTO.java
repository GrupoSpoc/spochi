package com.spochi.dto;

import lombok.Data;


public class RejectedInitiativeDTO {

    private String id;
    private String reject_motive;

    public RejectedInitiativeDTO() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getReject_motive() {
        return reject_motive;
    }

    public void setReject_motive(String reject_motive) {
        this.reject_motive = reject_motive;
    }

}

