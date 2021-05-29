package com.spochi.dto;

import com.spochi.entity.Initiative;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;

import static org.junit.jupiter.api.Assertions.*;

class InitiativeResponseDTOTest {

    @Test
    void initiativeResponseDtoOk() {
        final String nickname = "author";
        final LocalDateTime date = LocalDateTime.ofInstant(Instant.EPOCH, ZoneId.of("UTC"));
        final String description = "description";
        final String image = "image";
        final String user = "user_";

        final Initiative initiative = new Initiative();
        initiative.setImage(image);
        initiative.setNickname(nickname);
        initiative.setDate(date);
        initiative.setStatusId(2);
        initiative.setUserId(user);

        final InitiativeResponseDTO dto = initiative.toDTO(user);

        assertEquals(initiative.getNickname(),dto.getNickname());
        assertEquals(initiative.getDate().toString(),dto.getDate());
        assertEquals(initiative.getDescription(),dto.getDescription());
        assertEquals(initiative.getImage(),dto.getImage());
        assertEquals(initiative.getStatusId(),dto.getStatus_id());
        assertTrue(dto.isFrom_current_user());
    }

}