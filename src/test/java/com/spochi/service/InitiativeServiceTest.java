package com.spochi.service;

import com.spochi.dto.InitiativeRequestDTO;
import com.spochi.dto.InitiativeResponseDTO;
import com.spochi.entity.Initiative;
import com.spochi.entity.InitiativeStatus;
import com.spochi.entity.User;
import com.spochi.repository.InitiativeRepository;
import com.spochi.repository.MongoUserRepository;
import com.spochi.service.query.InitiativeSorter;
import com.spochi.util.AssertUtils;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
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
    MongoUserRepository userRepository;

    private final String NICKNAME = "nickname";
    private final String DESCRIPTION = "description";
    private final String IMAGE_BASE64 = "iVBORw0KGgoAAAANSUhEUgAAAAEAAAABCAYAAAAfFcSJAAAADUlEQVR42mP8z8BQDwAEhQGAhKmMIQAAAABJRU5ErkJggg==";
    private final String USER_ID = "id";
    private final String DATE = LocalDateTime.ofInstant(Instant.EPOCH, ZoneId.of("UTC")).toString();
    private final String UID = "uid";
    private final String EMPTY = "";
    private final InitiativeStatus STATUS_DEFAULT = InitiativeStatus.PENDING;

    private final InitiativeRequestDTO wrong_initiative = new InitiativeRequestDTO();
    private final InitiativeRequestDTO right_initiative = new InitiativeRequestDTO();


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
        when(user.getId()).thenReturn(USER_ID);
        when(userRepository.findByUid(UID)).thenReturn(Optional.of(user));

        InitiativeResponseDTO result = service.create(right_initiative, UID);

        assertEquals(NICKNAME, result.getNickname());
        assertEquals(DATE, result.getDate());
        assertEquals(IMAGE_BASE64, result.getImage());
        assertNotNull(result.get_id());
        assertEquals(STATUS_DEFAULT.getId(), result.getStatus_id());

        Optional<Initiative> initiativeOpt = initiativeRepository.findInitiativeById(result.get_id());
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
        final InitiativeSorter sorter = InitiativeSorter.DEFAULT_COMPARATOR;
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
        final Initiative initiativeNotFromCurrentUser = builder.build();

        initiativeRepository.create(initiativeFromCurrentUser_1);
        initiativeRepository.create(initiativeFromCurrentUser_2);
        initiativeRepository.create(initiativeNotFromCurrentUser);


        final User.UserBuilder userBuilder = User.builder();
        userBuilder.uid(UID);
        userBuilder.id(USER_ID);
        final User user = userBuilder.build();
        userRepository.create(user);


        when(userRepository.findByUid(UID)).thenReturn(Optional.of(user));


       List<InitiativeResponseDTO> list_dto = service.getAll(sorter,UID);

       assertEquals(3,list_dto.size());
       assertTrue(list_dto.get(0).isFrom_current_user());
       assertTrue(list_dto.get(1).isFrom_current_user());
       assertFalse(list_dto.get(2).isFrom_current_user());

    }

    @Test
    void getAllInitiativeThrowException(){
        final InitiativeSorter sorter = InitiativeSorter.DEFAULT_COMPARATOR;

        final Initiative.InitiativeBuilder builder = Initiative.builder();
        builder.nickname(NICKNAME);
        builder.date(LocalDateTime.now());
        builder.description(DESCRIPTION);
        builder.statusId(2);
        builder.image(IMAGE_BASE64);
        builder.userId(USER_ID);
        builder._id("1");
        final Initiative initiative = builder.build();

        initiativeRepository.create(initiative);

        AssertUtils.assertException(InitiativeService.InitiativeServiceException.class, () -> service.getAll(sorter, UID), "The Services fail because : user not found when initiative getAll");
    }
    @Test
    void createThrowException(){
        final String wrong_uid = "no user";

        right_initiative.setDescription(DESCRIPTION);
        right_initiative.setDate(DATE);
        right_initiative.setImage(IMAGE_BASE64);


        AssertUtils.assertException(InitiativeService.InitiativeServiceException.class, () -> service.create(right_initiative, wrong_uid), "The Services fail because : User not found");
    }

    @Test
    @DisplayName("approveInitiative | for a pending initiative | change status to approved")
    void approveInitiative(){
        Initiative testInitiative = new Initiative();
        testInitiative.setStatusId(1);
        testInitiative.set_id("someID");
        testInitiative.setUserId("userId");
        testInitiative.setDate(LocalDateTime.now().withNano(0));
        testInitiative.setDescription("SomeDescription");
        testInitiative.setNickname("User Nickname");
        initiativeRepository.create(testInitiative);

        InitiativeResponseDTO initiativeResponseDTO =   service.approveInitiative(testInitiative.get_id());

        assertEquals(initiativeResponseDTO.getStatus_id(), InitiativeStatus.APPROVED.getId());

    }

    @Test
    @DisplayName("approveInitiativeException | for a invalid id initiative | throw invalid id exception")
    void approveInitiativeException(){

        Initiative testInitiative = new Initiative();
        testInitiative.setStatusId(1);
        testInitiative.set_id("an unknown id");
        testInitiative.setUserId("userId");
        testInitiative.setDate(LocalDateTime.now().withNano(0));
        testInitiative.setDescription("SomeDescription");
        testInitiative.setNickname("User Nickname");

        AssertUtils.assertException(InitiativeService.InitiativeServiceException.class, () -> service.approveInitiative(testInitiative.toDTO("an unknown id 1").get_id()), "The Services fail because : There are no initiatives with this id");
    }

    @Test
    @DisplayName("approveNotPendingInitiativeException | for a not pending initiative | throw invalid status exception")
    void approveNotPendingInitiativeException(){

        Initiative testApprovedInitiative = new Initiative();
        testApprovedInitiative.setStatusId(2);
        testApprovedInitiative.set_id("someID");
        testApprovedInitiative.setUserId("userId");
        testApprovedInitiative.setDate(LocalDateTime.now().withNano(0));
        testApprovedInitiative.setDescription("SomeDescription");
        testApprovedInitiative.setNickname("User Nickname");

        Initiative testRejectedInitiative = new Initiative();
        testRejectedInitiative.setStatusId(3);
        testRejectedInitiative.set_id("someID1");
        testRejectedInitiative.setUserId("userId1");
        testRejectedInitiative.setDate(LocalDateTime.now().withNano(0));
        testRejectedInitiative.setDescription("SomeDescription1");
        testRejectedInitiative.setNickname("User Nickname 1");

        initiativeRepository.create(testApprovedInitiative);
        initiativeRepository.create(testRejectedInitiative);

        AssertUtils.assertException(InitiativeService.InitiativeServiceException.class, () -> service.approveInitiative(testApprovedInitiative.toDTO("someUserId").get_id()), "The Services fail because : Only pending initiatives can be approved");
        AssertUtils.assertException(InitiativeService.InitiativeServiceException.class, () -> service.approveInitiative(testRejectedInitiative.toDTO("someUserId1").get_id()), "The Services fail because : Only pending initiatives can be approved");

    }





}