package com.spochi.repository;

import com.spochi.MongoUserRepository;
import com.spochi.dto.UserResponseDTO;
import com.spochi.entity.User;
import com.spochi.persistence.UserDummyBuilder;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.*;

@DataMongoTest
@ActiveProfiles("disable-firebase")
class MongoEntityRepositoryTest {

    @Autowired
    MongoUserRepository repository;

    @AfterEach
    void clearDB() {
        repository.deleteAll();
    }

    @Test
    @DisplayName("find by uid | when user is found | should return option with user")
    void findByUid() {
        final User user = repository.create(UserDummyBuilder.build());
        final UserResponseDTO expectedResult = new UserResponseDTO();
        expectedResult.setType_id(user.getTypeId());
        expectedResult.setAmount_of_initiatives(0);
        expectedResult.setNickname(user.getNickname());
        expectedResult.setAdmin(false);


        final User userFoundByUid = repository.findByUid(user.getUid()).orElse(null);
        final UserResponseDTO actualResult = new UserResponseDTO();
        actualResult.setType_id(userFoundByUid.getTypeId());
        actualResult.setAmount_of_initiatives(0);
        actualResult.setNickname(userFoundByUid.getNickname());
        actualResult.setAdmin(false);

        assertNotNull(userFoundByUid);
        assertEquals(expectedResult, actualResult);
    }

    @Test
    @DisplayName("find by uid | when user is not found | should return empty option")
    void findByUidNotFound() {
        final User userFoundByUid = repository.findByUid("not-a-uid").orElse(null);

        assertNull(userFoundByUid);
    }

    @Test
    @DisplayName("find by nickname | when user is found | should return option with user")
    void findByNicknameFound() {
        final User user = repository.create(UserDummyBuilder.build());
        final UserResponseDTO expectedResult = new UserResponseDTO();
        expectedResult.setType_id(user.getTypeId());
        expectedResult.setAmount_of_initiatives(0);
        expectedResult.setNickname(user.getNickname());
        expectedResult.setAdmin(false);

        final User userFoundByNickname = repository.findByNickname(user.getNickname()).orElse(null);
        final UserResponseDTO actualResult = new UserResponseDTO();
        actualResult.setType_id(userFoundByNickname.getTypeId());
        actualResult.setAmount_of_initiatives(0);
        actualResult.setNickname(userFoundByNickname.getNickname());
        actualResult.setAdmin(false);

        assertNotNull(userFoundByNickname);
        assertEquals(expectedResult, actualResult);
    }

    @Test
    @DisplayName("find by nickname id | when user is not found | should return empty option")
    void findByNicknameNotFound() {
        final User user = repository.create(UserDummyBuilder.build());

        final User userFoundByNickname = repository.findByNickname(user.getNickname() + "a").orElse(null);

        assertNull(userFoundByNickname);
    }
}