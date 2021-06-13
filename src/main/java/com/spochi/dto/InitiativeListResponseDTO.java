package com.spochi.dto;

import java.util.List;

public class InitiativeListResponseDTO {
    private List<InitiativeResponseDTO> initiatives;
    private boolean last_batch;

    public List<InitiativeResponseDTO> getInitiatives() {
        return initiatives;
    }

    public InitiativeListResponseDTO setInitiatives(List<InitiativeResponseDTO> initiatives) {
        this.initiatives = initiatives;
        return this;
    }

    public boolean isLast_batch() {
        return last_batch;
    }

    public InitiativeListResponseDTO setLast_batch(boolean last_batch) {
        this.last_batch = last_batch;
        return this;
    }
}
