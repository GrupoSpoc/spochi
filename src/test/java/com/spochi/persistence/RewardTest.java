package com.spochi.persistence;

import com.spochi.entity.Reward;
import com.spochi.entity.UserType;
import com.spochi.repository.RewardRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataMongoTest
public class RewardTest {
    @Autowired
    RewardRepository repository;

    @AfterEach
    void clearDB() {
        repository.deleteAll();
    }

    @Test
    @DisplayName("create | ok")
    void createOk() {
        final Reward beforeSave = new Reward();
        beforeSave.setPoints(10);
        beforeSave.setUserType(UserType.COMPANY);
        beforeSave.setInitiativeTypeId(1);

        final Reward afterSave = repository.save(beforeSave);
        assertEquals(beforeSave.get_id(), afterSave.get_id());
        assertEquals(beforeSave.getPoints(), afterSave.getPoints());
        assertEquals(beforeSave.getInitiativeTypeId(), afterSave.getInitiativeTypeId());
        assertEquals(beforeSave.getUserTypeId(), afterSave.getUserTypeId());
        assertEquals(beforeSave.getUserType(), afterSave.getUserType());
    }

    @Test
    @DisplayName("update | ok")
    void updateOk() {
        final Reward reward = new Reward();
        reward.setPoints(10);
        reward.setUserType(UserType.COMPANY);
        reward.setInitiativeTypeId(1);

        repository.save(reward);
        reward.setPoints(20);
        final Reward afterUpdate = repository.save(reward);

        assertEquals(reward.get_id(), afterUpdate.get_id());
        assertEquals(20, afterUpdate.getPoints());
        assertEquals(reward.getInitiativeTypeId(), afterUpdate.getInitiativeTypeId());
        assertEquals(reward.getUserTypeId(), afterUpdate.getUserTypeId());
        assertEquals(reward.getUserType(), afterUpdate.getUserType());
    }

    @Test
    @DisplayName("find all | ok")
    void findAll() {
        Reward.RewardBuilder builder = Reward.builder();
        builder.points(10);
        builder.userTypeId(UserType.PERSON.getId());
        builder.initiativeTypeId(2);

        final Reward r1 = builder.build();
        final Reward r2 = builder.build();
        repository.save(r1);
        repository.save(r2);

        final List<Reward> result = repository.findAll();
        final List<String> expectedIds = List.of(r1.get_id(), r2.get_id());

        assertEquals(2, result.size());
        assertTrue(result.stream().map(Reward::get_id).allMatch(expectedIds::contains));
    }

    @Test
    @DisplayName("find by userType and initiativeType | ok")
    void findByUserTypeAndInitiativeTypeOk() {
        Reward.RewardBuilder builder = Reward.builder();
        builder.points(10);
        builder.userTypeId(UserType.PERSON.getId());
        builder.initiativeTypeId(1);

        final Reward r1 = builder.build();

        // cambio el segundo para asegurarme la precisión de la búsqueda
        builder.userTypeId(2);
        builder.initiativeTypeId(2);
        final Reward r2 = builder.build();

        repository.save(r1);
        repository.save(r2);

        Reward result1 = repository.findByUserTypeIdAndInitiativeTypeId(1, 1).orElseThrow();
        Reward result2 = repository.findByUserTypeIdAndInitiativeTypeId(2, 2).orElseThrow();
        Reward result3 = repository.findByUserTypeIdAndInitiativeTypeId(1, 2).orElse(null);

        assertEquals(r1.get_id(), result1.get_id());
        assertEquals(r2.get_id(), result2.get_id());
        assertNull(result3);
    }
}
