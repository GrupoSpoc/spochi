package com.spochi.dto;

import com.spochi.entity.Initiative;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;

import static org.junit.jupiter.api.Assertions.*;

class InitiativeResponseDTOTest {

    @Test
    void equalsWorks() {
        final String nickname = "author";
        final LocalDateTime date = LocalDateTime.ofInstant(Instant.EPOCH, ZoneId.of("UTC"));
        final String descprition = "description";
        final String image = "image";
        final String user = "user_";


        final Initiative.InitiativeBuilder builder = Initiative.builder();
        builder.nickname(nickname);
        builder.date(date);
        builder.description(descprition);
        builder.statusId(2);
        builder.image(image);
        builder.userId(user);

        InitiativeResponseDTO dto = builder.build().toDTO(user);

        assertEquals(nickname,dto.getNickname());
        assertEquals(date.toString(),dto.getDate());
        assertEquals(descprition,dto.getDescription());
        assertEquals(image,dto.getImage());
        assertEquals(2,dto.getStatus_id());
        assertTrue(dto.isFrom_current_user());
    }

}