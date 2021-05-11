package com.spochi.repository;

import com.spochi.entity.User;
import com.spochi.persistence.UserDummyBuilder;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.*;

@DataMongoTest
@ActiveProfiles("disable-firebase")
class UserRepositoryTest {

    @Autowired
    UserRepository repository;

    @AfterEach
    void clearDB() {
        repository.deleteAll();
    }

    @Test
    @DisplayName("find by google id | when user is found | should return option with user")
    void findByGoogleIdFound() {
        final User user = repository.save(UserDummyBuilder.build());

        final User userFoundByGoogleId = repository.findByGoogleId(user.getGoogleId()).orElse(null);

        assertNotNull(userFoundByGoogleId);
        assertEquals(user.toDTO(), userFoundByGoogleId.toDTO());
    }

    @Test
    @DisplayName("find by google id | when user is not found | should return empty option")
    void findByGoogleId() {
        final User userFoundByGoogleId = repository.findByGoogleId("not-a-google-id").orElse(null);

        assertNull(userFoundByGoogleId);
    }
}