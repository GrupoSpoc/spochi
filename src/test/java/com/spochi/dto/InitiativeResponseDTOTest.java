package com.spochi.dto;

import com.spochi.entity.Initiative;
import com.spochi.entity.InitiativeStatus;
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
        final String userId = "user_id";
        final String rejectMotive = "some rejection motive";

        final Initiative initiative = new Initiative();
        initiative.setImage(image);
        initiative.setNickname(nickname);
        initiative.setDate(date);
        initiative.setStatusId(InitiativeStatus.APPROVED.getId());
        initiative.setUserId(userId);
        initiative.setDescription(description);
        initiative.setReject_motive(rejectMotive);

        final InitiativeResponseDTO dto = initiative.toDTO();

        assertEquals(initiative.getNickname(), dto.getNickname());
        assertEquals(initiative.getDate().toString(), dto.getDate());
        assertEquals(initiative.getDescription(), dto.getDescription());
        assertEquals(initiative.getImage(), dto.getImage());
        assertEquals(initiative.getStatusId(), dto.getStatus_id());
        assertEquals(initiative.getDescription(), dto.getDescription());
        assertEquals(initiative.getReject_motive(), dto.getReject_motive());
    }

}