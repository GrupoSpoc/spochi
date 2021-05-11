package com.spochi.service;

import com.spochi.dto.UserResponseDTO;
import com.spochi.entity.User;
import com.spochi.persistence.UserDummyBuilder;
import com.spochi.repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@SpringBootTest
@ActiveProfiles("disable-firebase")
class UserServiceTest {

    @Autowired
    UserService service;

    @MockBean
    UserRepository repository;

    @Test
    @DisplayName("find by google id | when user is found | should return UserResponseDTO | ok")
    void findByGoogleIdFound() {
        final User mockedUser = UserDummyBuilder.buildWithId();
        final UserResponseDTO expectedDto = mockedUser.toDTO();

        when(repository.findByGoogleId(anyString())).thenReturn(Optional.of(mockedUser));

        final UserResponseDTO actualDTO = service.findByUid("google-id");

        assertEquals(expectedDto, actualDTO);
    }

    @Test
    @DisplayName("find by google id | when user is not found | should return null | ok")
    void findByGoogleIdNotFound() {
        when(repository.findByGoogleId(anyString())).thenReturn(Optional.empty());
        assertNull(service.findByUid("google-id"));
    }
}