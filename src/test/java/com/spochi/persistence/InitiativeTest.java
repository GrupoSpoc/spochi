package com.spochi.persistence;

import com.spochi.dto.InitiativeResponseDTO;
import com.spochi.entity.Initiative;
import com.spochi.repository.MongoInitiativeRepositoryInterface;
import com.spochi.repository.fiware.ngsi.NGSICommonFields;
import com.spochi.repository.fiware.ngsi.NGSIJson;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.test.context.ActiveProfiles;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataMongoTest
@ActiveProfiles("disable-firebase")
class InitiativeTest {
    @Autowired
    MongoInitiativeRepositoryInterface repository;

    @AfterEach
    void clearDB() {
        repository.deleteAll();
    }

    @Test
    @DisplayName("initiativeToJson | ok")
    void initiativeToJson() {

        final Initiative testInitiative = new Initiative();
        testInitiative.set_id("test1");
        testInitiative.setUserId(NGSICommonFields.ID.prefix() + "userTest1");
        testInitiative.setDate(LocalDateTime.now().withNano(0));
        testInitiative.setNickname("nickname1");
        testInitiative.setDescription("Some description");
        testInitiative.setImage("myImage");
        testInitiative.setStatusId(1);

        final NGSIJson json= testInitiative.toNGSIJson("test1");

        assertAll("expectedJsonData",
                () -> assertEquals(testInitiative.get_id(), json.getId()),
                () -> assertEquals(testInitiative.getUserId(), json.getJSONObject(Initiative.Fields.USER_ID.label()).getString(NGSICommonFields.VALUE.label())),
                () -> assertEquals(testInitiative.getDate().toString(), json.getJSONObject(Initiative.Fields.DATE.label()).getString(NGSICommonFields.VALUE.label())),
                () -> assertEquals(testInitiative.getNickname(), json.getJSONObject(Initiative.Fields.NICKNAME.label()).getString(NGSICommonFields.VALUE.label())),
                () -> assertEquals(testInitiative.getDescription(), json.getJSONObject(Initiative.Fields.DESCRIPTION.label()).getString(NGSICommonFields.VALUE.label())),
                () -> assertEquals(testInitiative.getImage(), json.getJSONObject(Initiative.Fields.IMAGE.label()).getString(NGSICommonFields.VALUE.label())),
                () -> assertEquals(testInitiative.getStatusId(), json.getJSONObject(Initiative.Fields.STATUS_ID.label()).getInt(NGSICommonFields.VALUE.label()))
        );
    }

    @Test
    @DisplayName("create initiative | ok")
    void testCreateInitiativeOk() {
        final Initiative beforeSave = new Initiative();

        beforeSave.setNickname("author");
        beforeSave.setDate(LocalDateTime.ofInstant(Instant.EPOCH, ZoneId.of("UTC")));
        beforeSave.setDescription("description");
        beforeSave.setStatusId(2);
        beforeSave.setImage("image");
        beforeSave.setUserId("user-id");

        final Initiative afterSave = repository.save(beforeSave);

        assertEquals(beforeSave.getNickname(), afterSave.getNickname());
        assertEquals(beforeSave.getDescription(), afterSave.getDescription());
        assertEquals(beforeSave.getDate(), afterSave.getDate());
        assertEquals(beforeSave.getUserId(), afterSave.getUserId());
        assertEquals(beforeSave.getImage(), afterSave.getImage());
        assertEquals(beforeSave.getStatusId(), afterSave.getStatusId());
        assertEquals(beforeSave.get_id(), afterSave.get_id());
    }

    @Test
    @DisplayName("update initiative | ok")
    void testUpdateInitiativeOk() {
        final Initiative beforeSave = new Initiative();

        beforeSave.setNickname("author");
        beforeSave.setDate(LocalDateTime.ofInstant(Instant.EPOCH, ZoneId.of("UTC")));
        beforeSave.setDescription("description");
        beforeSave.setStatusId(2);
        beforeSave.setImage("image");
        beforeSave.setUserId("user-id");

        final Initiative afterSave = repository.save(beforeSave);

        afterSave.setStatusId(3);
        final Initiative afterUpdate = repository.save(afterSave);

        assertEquals(beforeSave.getNickname(), afterUpdate.getNickname());
        assertEquals(beforeSave.getDescription(), afterUpdate.getDescription());
        assertEquals(beforeSave.getDate(), afterUpdate.getDate());
        assertEquals(beforeSave.getUserId(), afterUpdate.getUserId());
        assertEquals(beforeSave.getImage(), afterUpdate.getImage());
        assertEquals(3, afterSave.getStatusId());
        assertEquals(beforeSave.get_id(), afterUpdate.get_id());
    }

    @Test
    @DisplayName("get all | ok")
    void testGetAllOk() {
        final Initiative.InitiativeBuilder builder = Initiative.builder();
        builder.nickname("author");
        builder.date(LocalDateTime.ofInstant(Instant.EPOCH, ZoneId.of("UTC")));
        builder.description("description");
        builder.statusId(2);
        builder.image("image");
        builder.userId("user-id");

        final Initiative initiative1 = builder.build();
        final Initiative initiative2 = builder.build();

        repository.save(initiative1);
        repository.save(initiative2);

        final List<Initiative> initiatives = repository.findAll();

        assertEquals(2, initiatives.size());
        assertTrue(initiatives.stream().anyMatch(i -> i.get_id().equals(initiative1.get_id())));
        assertTrue(initiatives.stream().anyMatch(i -> i.get_id().equals(initiative2.get_id())));
    }

    @Test
    @DisplayName("get by id | ok")
    void testGetByIdOk() {
        final Initiative.InitiativeBuilder builder = Initiative.builder();
        builder.nickname("author");
        builder.date(LocalDateTime.ofInstant(Instant.EPOCH, ZoneId.of("UTC")));
        builder.description("description");
        builder.statusId(2);
        builder.image("image");
        builder.userId("user-id");

        final Initiative initiative = builder.build();

        repository.save(initiative);

        final Initiative result = repository.findById(initiative.get_id()).orElse(null);

        assertNotNull(result);
    }

    @Test
    @DisplayName("get entity type | ok")
    void getEntityTypeOk() {
        assertEquals("Initiative", Initiative.NGSIType.label());
    }

    @Test
    void testToDtoOK(){
        final String userId = "user-id";
        final LocalDateTime date = LocalDateTime.ofInstant(Instant.EPOCH, ZoneId.of("UTC"));

        final Initiative.InitiativeBuilder builder = Initiative.builder();
        builder.nickname("author");
        builder.date(date);
        builder.description("description");
        builder.statusId(2);
        builder.image("image");
        builder.userId(userId);


        final InitiativeResponseDTO dtoFromCurrentUser = builder.build().toDTO(userId);
        final InitiativeResponseDTO dtoNotFromCurrentUSer = builder.userId("otro_id").build().toDTO(userId);

        assertTrue(dtoFromCurrentUser.isFrom_current_user());
        assertFalse(dtoNotFromCurrentUSer.isFrom_current_user());

        assertEquals("author",dtoFromCurrentUser.getNickname());
        assertEquals(date.toString(),dtoFromCurrentUser.getDate());
        assertEquals("description",dtoFromCurrentUser.getDescription());
        assertEquals(2,dtoFromCurrentUser.getStatus_id());
        assertEquals("image",dtoFromCurrentUser.getImage());
    }
}