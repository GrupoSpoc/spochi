package com.spochi.service;

import com.spochi.dto.InitiativeRequestDTO;
import com.spochi.dto.InitiativeResponseDTO;
import com.spochi.entity.Initiative;
import com.spochi.entity.User;
import com.spochi.repository.InitiativeRepository;
import com.spochi.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@SpringBootTest
@ActiveProfiles("disable-firebase")
class InitiativeServiceTest {

    @Autowired
    InitiativeService service;

    @Autowired
    InitiativeRepository initiativeRepository;

    @MockBean
    UserRepository userRepository;


    private InitiativeRequestDTO wrong_initiative = new InitiativeRequestDTO();
    private InitiativeRequestDTO right_initiative = new InitiativeRequestDTO();
    private String uid = "uid";

    @Test
    void initiativeDescriptionIsEmpty() {
        wrong_initiative.setDescription("");
        assertThrows(InitiativeService.BadServiceException.class, () -> service.create(wrong_initiative, uid));
    }

    @Test
    void initiativeImageIsEmpty() {
        wrong_initiative.setDescription("description");
        wrong_initiative.setImage("");
        assertThrows(InitiativeService.BadServiceException.class, () -> service.create(wrong_initiative, uid));
    }

    @Test
    void initiativeImageIsNotBase64() {
        wrong_initiative.setDescription("description");
        wrong_initiative.setImage("$$$////:");
        assertThrows(InitiativeService.BadServiceException.class, () -> service.create(wrong_initiative, uid));
    }

    @Test
    void createOK() {
        right_initiative.setDescription("description");
        right_initiative.setDate(LocalDateTime.ofInstant(Instant.EPOCH, ZoneId.of("UTC")).toString());
        right_initiative.setImage("iVBORw0KGgoAAAANSUhEUgAAAAEAAAABCAYAAAAfFcSJAAAADUlEQVR42mP8z8BQDwAEhQGAhKmMIQAAAABJRU5ErkJggg==");

        User user = mock(User.class);
        when(user.getNickname()).thenReturn("nickname");
        when(user.get_id()).thenReturn("id");
        when(userRepository.findByGoogleId(uid)).thenReturn(Optional.of(user));

        InitiativeResponseDTO result = service.create(right_initiative, uid);

        assertEquals("nickname", result.getNickname());
        assertEquals(LocalDateTime.ofInstant(Instant.EPOCH, ZoneId.of("UTC")).toString(), result.getDate());
        assertEquals("iVBORw0KGgoAAAANSUhEUgAAAAEAAAABCAYAAAAfFcSJAAAADUlEQVR42mP8z8BQDwAEhQGAhKmMIQAAAABJRU5ErkJggg==", result.getImage());
        assertNotNull(result.get_id());
        assertEquals(1, result.getStatus_id());

        Optional<Initiative> initiativeOpt = initiativeRepository.findById(result.get_id());
        assertTrue(initiativeOpt.isPresent());
    }

    @Test
    void initiativeDateIsEmpty() {
        wrong_initiative.setDescription("description");
        wrong_initiative.setDate("");
        wrong_initiative.setImage("image");

        assertThrows(InitiativeService.BadServiceException.class, () -> service.create(wrong_initiative, uid));
    }

    @Test
    void initiativeDateIsFromFuture() {
        wrong_initiative.setDescription("description");
        wrong_initiative.setImage("image");

    }


}