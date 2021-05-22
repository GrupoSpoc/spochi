package com.spochi.repository;

import com.spochi.MongoUserRepository;
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
        final User user = repository.persist(UserDummyBuilder.build());

        final User userFoundByUid = repository.findByUid(user.getUid()).orElse(null);

        assertNotNull(userFoundByUid);
        assertEquals(user.toDTO(), userFoundByUid.toDTO());
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
        final User user = repository.persist(UserDummyBuilder.build());

        final User userFoundByNickname = repository.findByNickname(user.getNickname()).orElse(null);

        assertNotNull(userFoundByNickname);
        assertEquals(userFoundByNickname.toDTO(), userFoundByNickname.toDTO());
    }

    @Test
    @DisplayName("find by nickname id | when user is not found | should return empty option")
    void findByNicknameNotFound() {
        final User user = repository.persist(UserDummyBuilder.build());

        final User userFoundByNickname = repository.findByNickname(user.getNickname() + "a").orElse(null);

        assertNull(userFoundByNickname);
    }
}