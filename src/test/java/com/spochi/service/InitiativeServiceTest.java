package com.spochi.service;

import com.spochi.dto.InitiativeRequestDTO;
import com.spochi.dto.InitiativeResponseDTO;
import com.spochi.entity.Initiative;
import com.spochi.entity.User;
import com.spochi.repository.InitiativeRepository;
import com.spochi.repository.MongoUserRepository;
import com.spochi.util.AssertUtils;
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
    MongoUserRepository mongoUserRepository;

    private final String NICKNAME = "nickname";
    private final String DESCRIPTION = "description";
    private final String IMAGE_BASE64 = "iVBORw0KGgoAAAANSUhEUgAAAAEAAAABCAYAAAAfFcSJAAAADUlEQVR42mP8z8BQDwAEhQGAhKmMIQAAAABJRU5ErkJggg==";
    private final String USER_ID = "id";
    private final String DATE = LocalDateTime.ofInstant(Instant.EPOCH, ZoneId.of("UTC")).toString();
    private final String UID = "uid";
    private final String EMPTY = "";
    private final int STATUS_DEFAULT = 1;

    private InitiativeRequestDTO wrong_initiative = new InitiativeRequestDTO();
    private InitiativeRequestDTO right_initiative = new InitiativeRequestDTO();


    @Test
    void initiativeDescriptionIsEmpty() {
        wrong_initiative.setDescription(EMPTY);
        AssertUtils.assertException(InitiativeService.InitiativeServiceException.class, () -> service.create(wrong_initiative, UID),"The Services fail because : Initiative Description is empty");
    }

    @Test
    void initiativeImageIsEmpty() {
        wrong_initiative.setDescription(DESCRIPTION);
        wrong_initiative.setImage(EMPTY);

        AssertUtils.assertException(InitiativeService.InitiativeServiceException.class, () -> service.create(wrong_initiative, UID),"The Services fail because : Initiative Image is empty");
    }

    @Test
    void initiativeImageIsNotBase64() {
        wrong_initiative.setDescription(DESCRIPTION);
        wrong_initiative.setImage("$$$////:");
        AssertUtils.assertException(InitiativeService.InitiativeServiceException.class, () -> service.create(wrong_initiative, UID),"The Services fail because : Initiative Image is not Base64");
    }

    @Test
    void createOK() {
        right_initiative.setDescription(DESCRIPTION);
        right_initiative.setDate(DATE);
        right_initiative.setImage(IMAGE_BASE64);

        User user = mock(User.class);
        when(user.getNickname()).thenReturn(NICKNAME);
        when(user.getId()).thenReturn(USER_ID);
        when(mongoUserRepository.findByUid(UID)).thenReturn(Optional.of(user));

        InitiativeResponseDTO result = service.create(right_initiative, UID);

        assertEquals(NICKNAME, result.getNickname());
        assertEquals(DATE, result.getDate());
        assertEquals(IMAGE_BASE64, result.getImage());
        assertNotNull(result.get_id());
        assertEquals(STATUS_DEFAULT, result.getStatus_id());

        Optional<Initiative> initiativeOpt = initiativeRepository.findInitiativeById(result.get_id());
        assertTrue(initiativeOpt.isPresent());
    }

    @Test
    void initiativeDateIsEmpty() {
        wrong_initiative.setDescription(DESCRIPTION);
        wrong_initiative.setDate(EMPTY);
        wrong_initiative.setImage(IMAGE_BASE64);

        AssertUtils.assertException(InitiativeService.InitiativeServiceException.class, () -> service.create(wrong_initiative, UID),"The Services fail because : Initiative Date is empty");
    }
    @Test
    void initiativeDateIsInvalid(){
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime futureDate = now.plusDays(10);
        wrong_initiative.setDescription(DESCRIPTION);
        wrong_initiative.setImage(IMAGE_BASE64);
        wrong_initiative.setDate(futureDate.toString());

        AssertUtils.assertException(InitiativeService.InitiativeServiceException.class,() -> service.create(wrong_initiative, UID),"The Services fail because : Initiative Date invalid");
    }

    @Test
    void DateFormatIsWrong(){
        wrong_initiative.setDescription(DESCRIPTION);
        wrong_initiative.setImage(IMAGE_BASE64);
        wrong_initiative.setDate("0");

        AssertUtils.assertException(InitiativeService.InitiativeServiceException.class,() -> service.create(wrong_initiative, UID),"The Services fail because : Initiative Date invalid");
    }
}