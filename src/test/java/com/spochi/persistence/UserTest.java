package com.spochi.persistence;

import com.spochi.entity.Initiative;
import com.spochi.entity.User;
import com.spochi.entity.UserType;
import com.spochi.repository.UserRepository;
import org.json.JSONObject;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

@DataMongoTest
public class UserTest {
    @Autowired
    UserRepository repository;

    @AfterEach
    void clearDB() {
        repository.deleteAll();
    }

    @Test
    @DisplayName("create with initiatives | ok")
    void createOk() {
        final User beforeSave = new User();
        beforeSave.setGoogleId("1234");
        beforeSave.setNickname("nickname");
        beforeSave.setTypeId(UserType.ORGANIZATION);

        final Initiative i1 = InitiativeDummyBuilder.build();
        final Initiative i2 = InitiativeDummyBuilder.build();
        beforeSave.setInitiatives(Arrays.asList(i1, i2));

        final User afterSave = repository.save(beforeSave);

        assertEquals(beforeSave.get_id(), afterSave.get_id());
        assertEquals(beforeSave.getGoogleId(), afterSave.getGoogleId());
        assertEquals(beforeSave.getType(), afterSave.getType());
        assertEquals(beforeSave.getNickname(), afterSave.getNickname());

        // los ids de las iniciativas del afterSave deberían ser
        // las del beforeSave
        assertEquals(2, afterSave.getInitiatives().size());
        assertEquals(beforeSave.getInitiatives().size(), afterSave.getInitiatives().size());
        assertTrue(afterSave.getInitiatives()
                .stream()
                .allMatch(i -> beforeSave.getInitiatives()
                        .stream()
                        .map(Initiative::get_id)
                        .collect(Collectors.toList())
                        .contains(i.get_id())));
    }

    @Test
    @DisplayName("add initiative | ok")
    void addInitiativeOk() {
        final User user = new User();
        user.setGoogleId("1234");
        user.setNickname("nickname");
        user.setTypeId(UserType.ORGANIZATION);

        final Initiative i1 = InitiativeDummyBuilder.build();
        user.addInitiative(i1);

        repository.save(user);

        final Initiative i2 = InitiativeDummyBuilder.build();
        user.addInitiative(i2);
        final User afterUpdate = repository.save(user);

        assertEquals(2, afterUpdate.getInitiatives().size());
        final List<String> expectedInitiativeIds = Arrays.asList(i1.get_id(), i2.get_id());
        assertTrue(afterUpdate.getInitiatives().stream().map(Initiative::get_id)
                .allMatch(expectedInitiativeIds::contains));
    }

    @Test
    @DisplayName("find by id | ok")
    void findByIdOk() {
        final User user = new User();
        user.setGoogleId("1234");
        user.setNickname("nickname");
        user.setTypeId(UserType.ORGANIZATION);

        repository.save(user);

        final User result = repository.findById(user.get_id()).orElse(null);

        assertNotNull(result);
        assertEquals(user.get_id(), result.get_id());
    }

    @Test
    @DisplayName("find all | ok")
    void findAllOk() {
        final User.UserBuilder builder = User.builder();
        builder.googleId("1234");
        builder.nickname("nickname");
        builder.typeId(UserType.ORGANIZATION.getId());

        final User u1 = builder.build();
        final User u2 = builder.build();
        repository.save(u1);
        repository.save(u2);

        final List<User> result = repository.findAll();

        assertEquals(2, result.size());
        final List<String> expectedIds = Arrays.asList(u1.get_id(), u2.get_id());
        assertTrue(result.stream().map(User::get_id).collect(Collectors.toList()).containsAll(expectedIds));
    }

    @Test
    @DisplayName("from NGSIJson | ok")
    void fromNGSIJsonOk() {
        final String id = "urn:ngsi-ld:User:001";
        final String uid = "test-uid";
        final String nickname = "test-nickname";
        final int typeId = 1;

        final JSONObject json = new JSONObject();
        json.put(User.Fields.ID.getValue(), id);
        json.put(User.Fields.UID.getValue(), uid);
        json.put(User.Fields.NICKNAME.getValue(), nickname);
        json.put(User.Fields.TYPE_ID.getValue(), typeId);

        final User user = User.fromNGSIJson(json);

        assertAll("Expected user",
                () -> assertEquals(id, user.get_id()),
                () -> assertEquals(uid, user.getGoogleId()),
                () -> assertEquals(nickname, user.getNickname()),
                () -> assertEquals(typeId, user.getTypeId()));
    }

    @Test
    @DisplayName("Fields | getValue | ok")
    void fieldsGetValueOk() {
        assertEquals("id", User.Fields.ID.getValue());
        assertEquals("uid", User.Fields.UID.getValue());
        assertEquals("nickname", User.Fields.NICKNAME.getValue());
        assertEquals("type_id", User.Fields.TYPE_ID.getValue());
    }
}
