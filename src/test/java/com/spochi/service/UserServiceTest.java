package com.spochi.service;

import com.spochi.controller.HttpStatus;
import com.spochi.dto.UserRequestDTO;
import com.spochi.dto.UserResponseDTO;
import com.spochi.entity.User;
import com.spochi.entity.UserType;
import com.spochi.persistence.UserDummyBuilder;
import com.spochi.MongoUserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;

import java.util.Optional;

import static com.spochi.util.AssertUtils.assertBadRequestException;
import static com.spochi.util.AssertUtils.assertException;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@SpringBootTest
@ActiveProfiles("disable-firebase")
class UserServiceTest {

    @Autowired
    UserService service;

    @MockBean
    MongoUserRepository repository;

    @Test
    @DisplayName("find by uid | when user is found | should return UserResponseDTO | ok")
    void findByUidFound() {
        final User mockedUser = UserDummyBuilder.buildWithId();
        final UserResponseDTO expectedDto = mockedUser.toDTO();

        when(repository.findByUid(anyString())).thenReturn(Optional.of(mockedUser));

        final UserResponseDTO actualDTO = service.findByUid("uid");

        assertEquals(expectedDto, actualDTO);
    }

    @Test
    @DisplayName("find by uid | when user is not found | should return null | ok")
    void findByUidNotFound() {
        when(repository.findByUid(anyString())).thenReturn(Optional.empty());
        assertNull(service.findByUid("uid"));
    }

    @Test
    @DisplayName("create | ok | should return UserResponseDTO | ok")
    void createOk() {
        final String uid = "uid";
        final String nickname = "nickname";
        final int typeId = 1;

        final UserRequestDTO request = new UserRequestDTO();
        request.setNickname(nickname);
        request.setType_id(typeId);

        when(repository.persist(any(User.class)))
                .thenReturn(User.builder().nickname(nickname).uid(uid).typeId(typeId).build());

        final UserResponseDTO result = service.create(request, uid);

        assertAll("Expected result",
                () -> assertEquals(nickname, result.getNickname()),
                () -> assertEquals(typeId, result.getType_id()),
                () -> assertEquals(0, result.getAmount_of_initiatives()));
    }

    @Test
    @DisplayName("create | given null nickname | should throw UserServiceException")
    void createNullNickname() {
        final UserRequestDTO request = new UserRequestDTO();
        request.setType_id(1);

        assertException(UserServiceException.class, () -> service.create(request, "uid"), "nickname cannot be null or empty");
    }

    @Test
    @DisplayName("create | given already used uid | should throw UserServiceException")
    void createAlreadyUsedAccount() {
        final String uid = "uid";

        final UserRequestDTO request = new UserRequestDTO();
        request.setNickname("nickname");
        request.setType_id(1);

        when(repository.findByUid(uid)).thenReturn(Optional.of(mock(User.class)));

        assertException(UserServiceException.class, () -> service.create(request, uid), "this google account already has a user");
    }

    @Test
    @DisplayName("create | given already used nickname | should throw UserServiceException")
    void createAlreadyUsedNickname() {
        final String nickname = "nickname";

        final UserRequestDTO request = new UserRequestDTO();
        request.setNickname(nickname);
        request.setType_id(1);

        when(repository.findByNickname(nickname)).thenReturn(Optional.of(mock(User.class)));

        assertBadRequestException(UserServiceException.class, () -> service.create(request, "uid"), "nickname already taken", HttpStatus.NICKNAME_ALREADY_TAKEN);
    }

    @Test
    @DisplayName("create | given null type_id | should throw UserServiceException")
    void createNullTypeId() {
        final UserRequestDTO request = new UserRequestDTO();
        request.setType_id(null);
        request.setNickname("nickname");

        assertException(UserServiceException.class, () -> service.create(request, "uid"), "type_id cannot be null");
    }

    @Test
    @DisplayName("create | given invalid type_id | should throw UserTypeNotFoundException")
    void createInvalidTypeId() {
        final int invalidTypeId = Integer.MIN_VALUE;

        final UserRequestDTO request = new UserRequestDTO();
        request.setType_id(invalidTypeId);
        request.setNickname("nickname");

        assertException(UserType.UserTypeNotFoundException.class, () -> service.create(request, "uid"), String.format("No UserType with id [%s] present", invalidTypeId));
    }

}