package com.spochi.persistence;

import com.spochi.repository.MongoUserRepository;
import com.spochi.entity.User;
import com.spochi.entity.UserType;
import com.spochi.repository.fiware.ngsi.NGSICommonFields;
import com.spochi.repository.fiware.ngsi.NGSIFieldType;
import com.spochi.repository.fiware.ngsi.NGSIJson;
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
    MongoUserRepository repository;

    @AfterEach
    void clearDB() {
        repository.deleteAll();
    }

    @Test
    @DisplayName("create  | ok")
    void createOk() {
        final User beforeSave = new User();
        beforeSave.setUid("1234");
        beforeSave.setNickname("nickname");
        beforeSave.setTypeId(UserType.ORGANIZATION);

        final User afterSave = repository.create(beforeSave);

        assertEquals(beforeSave.getId(), afterSave.getId());
        assertEquals(beforeSave.getUid(), afterSave.getUid());
        assertEquals(beforeSave.getType(), afterSave.getType());
        assertEquals(beforeSave.getNickname(), afterSave.getNickname());
    }


    @Test
    @DisplayName("find by id | ok")
    void findByIdOk() {
        final User user = new User();
        user.setUid("1234");
        user.setNickname("nickname");
        user.setTypeId(UserType.ORGANIZATION);

        repository.create(user);

        final User result = repository.findById(user.getId()).orElse(null);

        assertNotNull(result);
        assertEquals(user.getId(), result.getId());
    }

    @Test
    @DisplayName("find all | ok")
    void findAllOk() {
        final User.UserBuilder builder = User.builder();
        builder.uid("1234");
        builder.nickname("nickname");
        builder.typeId(UserType.ORGANIZATION.getId());

        final User u1 = builder.build();
        final User u2 = builder.build();
        repository.create(u1);
        repository.create(u2);

        final List<User> result = repository.findAll();

        assertEquals(2, result.size());
        final List<String> expectedIds = Arrays.asList(u1.getId(), u2.getId());
        assertTrue(result.stream().map(User::getId).collect(Collectors.toList()).containsAll(expectedIds));
    }

    @Test
    @DisplayName("NGSIType | ok")
    void NGSIType() {
        assertEquals("User", User.NGSIType.label());
    }

    @Test
    @DisplayName("to NGSIJson | ok")
    void toNGSIJsonOk() {
        final User user = UserDummyBuilder.build();

        final NGSIJson json = user.toNGSIJson(UserDummyBuilder.FIWARE_ID);

        assertAll("Expected result",
                () -> assertEquals(UserDummyBuilder.FIWARE_ID, json.getId()),
                () -> assertEquals(user.getUid(), json.getJSONObject(User.Fields.UID.label()).getString(NGSICommonFields.VALUE.label())),
                () -> assertEquals(user.getNickname(), json.getJSONObject(User.Fields.NICKNAME.label()).getString(NGSICommonFields.VALUE.label())),
                () -> assertEquals(user.getTypeId(), json.getJSONObject(User.Fields.TYPE_ID.label()).getInt(NGSICommonFields.VALUE.label()))
        );
    }

    @Test
    @DisplayName("Fields | label | ok")
    void fieldsValueOk() {
        assertEquals("uid", User.Fields.UID.label());
        assertEquals("nickname", User.Fields.NICKNAME.label());
        assertEquals("type_id", User.Fields.TYPE_ID.label());
    }

    @Test
    @DisplayName("Fields | type | ok")
    void fieldsTypeOk() {
        assertEquals(NGSIFieldType.TEXT, User.Fields.UID.type());
        assertEquals(NGSIFieldType.TEXT, User.Fields.NICKNAME.type());
        assertEquals(NGSIFieldType.INTEGER, User.Fields.TYPE_ID.type());
    }
}
