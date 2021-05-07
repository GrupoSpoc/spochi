package com.spochi.repository;

import com.spochi.entity.User;
import com.spochi.persistence.UserDummyBuilder;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.*;

@DataMongoTest
@ActiveProfiles("disable-firebase")
class UserRepositoryTest {

    @Autowired
    UserRepository repository;


    @Test
    void findByGoogleId() {
        final User user = repository.save(UserDummyBuilder.build());

        final User userFoundByGoogleId = repository.findByGoogleId(user.getGoogleId()).orElse(null);

        assertNotNull(userFoundByGoogleId);
        assertEquals(user.toDTO(), userFoundByGoogleId.toDTO());
    }
}