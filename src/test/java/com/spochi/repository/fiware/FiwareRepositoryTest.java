package com.spochi.repository.fiware;

import com.spochi.repository.fiware.ngsi.NGSIEntityType;
import com.spochi.repository.fiware.ngsi.NGSIJson;
import com.spochi.repository.fiware.ngsi.NGSIQueryBuilder;
import com.spochi.repository.fiware.ngsi.NGSISerializableEntityForTest;
import com.spochi.repository.fiware.rest.RestException;
import com.spochi.repository.fiware.rest.RestPerformer;
import com.spochi.util.AssertUtils;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@DisplayName("Fiware Repository Test | Unit")
class FiwareRepositoryTest {
    private static final NGSISerializableEntityForTest testEntity1 = new NGSISerializableEntityForTest("field1", 1);
    private static final NGSISerializableEntityForTest testEntity2 = new NGSISerializableEntityForTest("field2", 2);
    private static final NGSISerializableEntityForTest testEntity3 = new NGSISerializableEntityForTest("field3", 3);

    private static final String id1 = "id:1";
    private static final String id2 = "id:2";
    private static final String id3 = "id:3";

    private static final String testEntityArrayString = "[" + testEntity2.toNGSIJson(id2).toString() + "," + testEntity1.toNGSIJson(id1) + "]";
    private static final String emptyArray = "[]";

    @Test
    @DisplayName("create | ok")
    void createOk() {
        final RestPerformer performer = mock(RestPerformer.class);
        final FiwareTestEntityRepository repository = new FiwareTestEntityRepository(performer);

        doNothing().when(performer).postJson(anyString(), anyString());
        when(performer.get(anyString()))
                .thenReturn(testEntityArrayString) // next id
                .thenReturn(testEntity3.toNGSIJson("id:3").toString()); // findById

        final NGSISerializableEntityForTest result = repository.create(testEntity3);

        assertAll("Expected result",
                () -> assertEquals(testEntity3, result),
                () -> assertEquals(id3, result.getId()));
    }

    @Test
    @DisplayName("create | when db is empty | ok")
    void createDbEmptyOk() {
        final RestPerformer performer = mock(RestPerformer.class);
        final FiwareTestEntityRepository repository = new FiwareTestEntityRepository(performer);

        doNothing().when(performer).postJson(anyString(), anyString());
        when(performer.get(anyString()))
                .thenReturn(emptyArray) // next id
                .thenReturn(testEntity1.toNGSIJson("id:1").toString()); // findById

        final NGSISerializableEntityForTest result = repository.create(testEntity1);

        assertAll("Expected result",
                () -> assertEquals(testEntity1, result),
                () -> assertEquals(id1, result.getId()));
    }

    @Test
    @DisplayName("create | when exception is thrown | should not catch")
    void createException() {
        final RestPerformer performer = mock(RestPerformer.class);
        final FiwareTestEntityRepository repository = new FiwareTestEntityRepository(performer);

        when(performer.get(anyString())).thenReturn(testEntityArrayString);
        doThrow(new RuntimeException("create-error")).when(performer).postJson(anyString(), anyString());

        AssertUtils.assertException(RuntimeException.class, () -> repository.create(testEntity1), "create-error");
    }

    @Test
    @DisplayName("create | entity not found after creation | RestException")
    void createNotFoundAfterCreationException() {
        final RestPerformer performer = mock(RestPerformer.class);
        final FiwareTestEntityRepository repository = new FiwareTestEntityRepository(performer);

        doNothing().when(performer).postJson(anyString(), anyString());
        when(performer.get(anyString()))
                .thenReturn(testEntityArrayString) // next id
                .thenReturn(null); // findById

        AssertUtils.assertException(RestException.class, () -> repository.create(testEntity3),
                String.format("Fatal: [%s] with id [%s] not found after creation", NGSISerializableEntityForTest.NGSIType.label(), repository.buildId(3)));
    }

    @Test
    @DisplayName("find by id | ok")
    void findByIdOk() {
        final RestPerformer performer = mock(RestPerformer.class);
        final FiwareTestEntityRepository repository = new FiwareTestEntityRepository(performer);

        when(performer.get(anyString())).thenReturn(testEntity1.toNGSIJson(id1).toString());

        final NGSISerializableEntityForTest result = repository.findById(id1).orElseThrow(RuntimeException::new);

        assertAll("Expected result",
                () -> assertEquals(testEntity1, result),
                () -> assertEquals(id1, result.getId()));
    }

    @Test
    @DisplayName("find by id | when not found | should return empty option")
    void findByIdNotFoundOk() {
        final RestPerformer performer = mock(RestPerformer.class);
        final FiwareTestEntityRepository repository = new FiwareTestEntityRepository(performer);

        when(performer.get(anyString())).thenReturn(null);

        final Optional<NGSISerializableEntityForTest> result = repository.findById(id3);

        assertFalse(result.isPresent());
    }

    @Test
    @DisplayName("find by id | when exception is thrown | should not catch")
    void findByIdException() {
        final RestPerformer performer = mock(RestPerformer.class);
        final FiwareTestEntityRepository repository = new FiwareTestEntityRepository(performer);

        when(performer.get(anyString())).thenThrow(new RuntimeException("find-by-id-error"));

        AssertUtils.assertException(RuntimeException.class, () -> repository.findById(id2), "find-by-id-error");
    }

    @Test
    @DisplayName("find first | ok")
    void findFirst() {
        final RestPerformer performer = mock(RestPerformer.class);
        final FiwareTestEntityRepository repository = new FiwareTestEntityRepository(performer);

        when(performer.get(anyString())).thenReturn(testEntityArrayString);

        final NGSISerializableEntityForTest result = repository.findFirst(new NGSIQueryBuilder()).orElseThrow(RuntimeException::new);

        assertAll("Expected result",
                () -> assertEquals(testEntity2, result),
                () -> assertEquals(id2, result.getId()));
    }

    @Test
    @DisplayName("find first | when empty result | should return empty option")
    void findFirstEmptyResultOk() {
        final RestPerformer performer = mock(RestPerformer.class);
        final FiwareTestEntityRepository repository = new FiwareTestEntityRepository(performer);

        when(performer.get(anyString())).thenReturn(emptyArray);

        final Optional<NGSISerializableEntityForTest> result = repository.findFirst(new NGSIQueryBuilder());

        assertFalse(result.isPresent());
    }

    @Test
    @DisplayName("find first | when exception is thrown | should not catch")
    void findFirstException() {
        final RestPerformer performer = mock(RestPerformer.class);
        final FiwareTestEntityRepository repository = new FiwareTestEntityRepository(performer);

        when(performer.get(anyString())).thenThrow(new RuntimeException("find-first-error"));

        AssertUtils.assertException(RuntimeException.class, () -> repository.findFirst(new NGSIQueryBuilder()), "find-first-error");
    }

    @Test
    @DisplayName("find | ok")
    void findOk() {
        final RestPerformer performer = mock(RestPerformer.class);
        final FiwareTestEntityRepository repository = new FiwareTestEntityRepository(performer);

        when(performer.get(anyString())).thenReturn(testEntityArrayString);

        final List<NGSISerializableEntityForTest> result = repository.find(new NGSIQueryBuilder());

        assertAll("Expected result",
                () -> assertEquals(2, result.size()),
                () -> assertTrue(result.containsAll(Arrays.asList(testEntity1, testEntity2))));
    }

    @Test
    @DisplayName("find | when empty result | should return empty list")
    void findEmptyResult() {
        final RestPerformer performer = mock(RestPerformer.class);
        final FiwareTestEntityRepository repository = new FiwareTestEntityRepository(performer);

        when(performer.get(anyString())).thenReturn(emptyArray);

        final List<NGSISerializableEntityForTest> result = repository.find(new NGSIQueryBuilder());

        assertTrue(result.isEmpty());
    }

    @Test
    @DisplayName("find | when exception is thrown | should not catch")
    void findException() {
        final RestPerformer performer = mock(RestPerformer.class);
        final FiwareTestEntityRepository repository = new FiwareTestEntityRepository(performer);

        when(performer.get(anyString())).thenThrow(new RuntimeException("find-error"));

        AssertUtils.assertException(RuntimeException.class, () -> repository.find(new NGSIQueryBuilder()), "find-error");
    }

    @Test
    @DisplayName("count | ok")
    void countOk() {
        final RestPerformer performer = mock(RestPerformer.class);
        final FiwareTestEntityRepository repository = new FiwareTestEntityRepository(performer);

        when(performer.count(anyString())).thenReturn(6);

        assertEquals(6, repository.count(new NGSIQueryBuilder()));
    }

    @Test
    @DisplayName("count | ok")
    void countException() {
        final RestPerformer performer = mock(RestPerformer.class);
        final FiwareTestEntityRepository repository = new FiwareTestEntityRepository(performer);

        when(performer.count(anyString())).thenThrow(new RuntimeException("count-error"));

        AssertUtils.assertException(RuntimeException.class, () -> repository.count(new NGSIQueryBuilder()), "count-error");
    }

    @Test
    @DisplayName("build id | ok")
    void buildId() {
        final FiwareTestEntityRepository repository = new FiwareTestEntityRepository(mock(RestPerformer.class));

        assertEquals(String.format("urn:ngsi-ld:%s:%s", repository.getEntityType().label(), 1), repository.buildId(1));
    }
}

final class FiwareTestEntityRepository extends FiwareRepository<NGSISerializableEntityForTest> {
    public FiwareTestEntityRepository(RestPerformer performer) {
        super(performer);
    }

    @Override
    protected NGSIEntityType getEntityType() {
        return NGSISerializableEntityForTest.NGSIType;
    }

    @Override
    protected NGSISerializableEntityForTest fromNGSIJson(NGSIJson json) {
        return NGSISerializableEntityForTest.fromNGSIJson(json);
    }
}