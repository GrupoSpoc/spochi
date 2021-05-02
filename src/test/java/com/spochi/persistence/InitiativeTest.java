package com.spochi.persistence;

import com.spochi.entity.Initiative;
import com.spochi.repository.InitiativeRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataMongoTest
class InitiativeTest {
    @Autowired
    InitiativeRepository repository;

    @AfterEach
    void clearDB() {
        repository.deleteAll();
    }

    @Test
    @DisplayName("create initiative | ok")
    void testCreateInitiativeOk() {
        final Initiative beforeSave = new Initiative();

        beforeSave.setAuthor("author");
        beforeSave.setDate(LocalDateTime.ofInstant(Instant.EPOCH, ZoneId.of("UTC")));
        beforeSave.setDescription("description");
        beforeSave.setStatusId(2);
        beforeSave.setImage("image");
        beforeSave.setUserId("user-id");

        final Initiative afterSave = repository.save(beforeSave);

        assertEquals(beforeSave.getAuthor(), afterSave.getAuthor());
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

        beforeSave.setAuthor("author");
        beforeSave.setDate(LocalDateTime.ofInstant(Instant.EPOCH, ZoneId.of("UTC")));
        beforeSave.setDescription("description");
        beforeSave.setStatusId(2);
        beforeSave.setImage("image");
        beforeSave.setUserId("user-id");

        final Initiative afterSave = repository.save(beforeSave);

        afterSave.setStatusId(3);
        final Initiative afterUpdate = repository.save(afterSave);

        assertEquals(beforeSave.getAuthor(), afterUpdate.getAuthor());
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
        builder.author("author");
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
        builder.author("author");
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
}