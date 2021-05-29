package com.spochi.service;

import com.spochi.dto.InitiativeRequestDTO;
import com.spochi.dto.InitiativeResponseDTO;
import com.spochi.entity.Initiative;
import com.spochi.entity.User;
import com.spochi.persistence.InitiativeDummyBuilder;
import com.spochi.repository.InitiativeRepository;
import com.spochi.repository.UserRepository;
import com.spochi.service.query.InitiativeSorter;
import com.spochi.util.AssertUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.anyString;
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
        AssertUtils.assertException(InitiativeService.InitiativeServiceException.class, () -> service.create(wrong_initiative, UID), "The Services fail because : Initiative Description is empty");
    }

    @Test
    void initiativeImageIsEmpty() {
        wrong_initiative.setDescription(DESCRIPTION);
        wrong_initiative.setImage(EMPTY);

        AssertUtils.assertException(InitiativeService.InitiativeServiceException.class, () -> service.create(wrong_initiative, UID), "The Services fail because : Initiative Image is empty");
    }

    @Test
    void initiativeImageIsNotBase64() {
        wrong_initiative.setDescription(DESCRIPTION);
        wrong_initiative.setImage("$$$////:");
        AssertUtils.assertException(InitiativeService.InitiativeServiceException.class, () -> service.create(wrong_initiative, UID), "The Services fail because : Initiative Image is not Base64");
    }

    @Test
    void createOK() {
        right_initiative.setDescription(DESCRIPTION);
        right_initiative.setDate(DATE);
        right_initiative.setImage(IMAGE_BASE64);

        User user = mock(User.class);
        when(user.getNickname()).thenReturn(NICKNAME);
        when(user.get_id()).thenReturn(USER_ID);
        when(userRepository.findByGoogleId(UID)).thenReturn(Optional.of(user));

        InitiativeResponseDTO result = service.create(right_initiative, UID);

        assertEquals(NICKNAME, result.getNickname());
        assertEquals(DATE, result.getDate());
        assertEquals(IMAGE_BASE64, result.getImage());
        assertNotNull(result.get_id());
        assertEquals(STATUS_DEFAULT, result.getStatus_id());

        Optional<Initiative> initiativeOpt = initiativeRepository.findById(result.get_id());
        assertTrue(initiativeOpt.isPresent());
    }

    @Test
    void initiativeDateIsEmpty() {
        wrong_initiative.setDescription(DESCRIPTION);
        wrong_initiative.setDate(EMPTY);
        wrong_initiative.setImage(IMAGE_BASE64);

        AssertUtils.assertException(InitiativeService.InitiativeServiceException.class, () -> service.create(wrong_initiative, UID), "The Services fail because : Initiative Date is empty");
    }

    @Test
    void initiativeDateIsInvalid() {
        LocalDateTime futureDate = LocalDateTime.now().plusDays(10);
        wrong_initiative.setDescription(DESCRIPTION);
        wrong_initiative.setImage(IMAGE_BASE64);
        wrong_initiative.setDate(futureDate.toString());

        AssertUtils.assertException(InitiativeService.InitiativeServiceException.class, () -> service.create(wrong_initiative, UID), "The Services fail because : Initiative Date invalid");
    }

    @Test
    void DateFormatIsWrong() {
        wrong_initiative.setDescription(DESCRIPTION);
        wrong_initiative.setImage(IMAGE_BASE64);
        wrong_initiative.setDate("0");

        AssertUtils.assertException(InitiativeService.InitiativeServiceException.class, () -> service.create(wrong_initiative, UID), "The Services fail because : Initiative Date invalid");
    }

    @Test
    void getAllInitiativeOK() {
        final Comparator<Initiative> sorter = InitiativeSorter.DEFAULT_COMPARATOR.getComparator();
        final Initiative.InitiativeBuilder builder = Initiative.builder();
        builder.nickname(NICKNAME);
        builder.date(LocalDateTime.now());
        builder.description(DESCRIPTION);
        builder.statusId(2);
        builder.image(IMAGE_BASE64);
        builder.userId(USER_ID);
        builder._id("1");

        final Initiative initiativeFromCurrentUser_1= builder.build();
        final Initiative initiativeFromCurrentUser_2= builder._id("2").build();
        builder.userId("another_user");
        builder._id("3");
        final Initiative initiativeNotFromCurrentUser = builder.build();;

        initiativeRepository.save(initiativeFromCurrentUser_1);
        initiativeRepository.save(initiativeFromCurrentUser_2);
        initiativeRepository.save(initiativeNotFromCurrentUser);


        final User.UserBuilder userBuilder = User.builder();
        userBuilder.googleId(UID);
        userBuilder._id(USER_ID);
        final User user = userBuilder.build();
        userRepository.save(user);


        when(userRepository.findByGoogleId(UID)).thenReturn(Optional.of(user));


       List<InitiativeResponseDTO> list_dto = service.getAll(sorter,UID);

       assertEquals(3,list_dto.size());
       assertTrue(list_dto.get(0).isFrom_current_user());
       assertTrue(list_dto.get(1).isFrom_current_user());
       assertFalse(list_dto.get(2).isFrom_current_user());

    }

    @Test
    void getAllInitiativeThrowException(){
        final Comparator<Initiative> sorter = InitiativeSorter.DEFAULT_COMPARATOR.getComparator();

        final Initiative.InitiativeBuilder builder = Initiative.builder();
        builder.nickname(NICKNAME);
        builder.date(LocalDateTime.now());
        builder.description(DESCRIPTION);
        builder.statusId(2);
        builder.image(IMAGE_BASE64);
        builder.userId(USER_ID);
        builder._id("1");
        final Initiative initiative = builder.build();

        initiativeRepository.save(initiative);

        AssertUtils.assertException(InitiativeService.InitiativeServiceException.class, () -> service.getAll(sorter, UID), "The Services fail because : user not found when initiative getAll");
    }
    @Test
    void createThrowException(){
        final Comparator<Initiative> sorter = InitiativeSorter.DEFAULT_COMPARATOR.getComparator();
        final String wrong_uid = "no user";

        right_initiative.setDescription(DESCRIPTION);
        right_initiative.setDate(DATE);
        right_initiative.setImage(IMAGE_BASE64);


        AssertUtils.assertException(InitiativeService.InitiativeServiceException.class, () -> service.create(right_initiative, wrong_uid), "The Services fail because : User not found");
    }
}