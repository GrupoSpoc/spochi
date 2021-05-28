package com.spochi.repository.fiware;

import com.spochi.entity.Initiative;
import com.spochi.repository.fiware.ngsi.NGSIJson;
import com.spochi.repository.fiware.rest.RestPerformer;
import com.spochi.service.query.InitiativeSorter;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;
import java.util.*;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

public class FiwareInitiativeRepositoryTest {
    private static final LocalDateTime dateTime = LocalDateTime.now();
    private static final Initiative initiative1 = Initiative.builder()

            .description("description1")
            .statusId(1)
            .image("image1")
            .nickname("nickname1")
            .date(dateTime)
            .userId("userId1")
            .build();

    private static final Initiative initiative2 = Initiative.builder()

            .description("description2")
            .statusId(2)
            .image("image2")
            .nickname("nickname2")
            .date(dateTime.minusHours(2))
            .userId("userId2")
            .build();

    private static final String id1 = "123";
    private static final String id2 = "456";

    private static final NGSIJson initiative1Json = buildTestInitiativeJsonResponse(initiative1, id1);
    private static final NGSIJson initiative2Json = buildTestInitiativeJsonResponse(initiative2, id2);


    private static final String bothInitiatives = "[" + initiative1Json.toString() + "," + initiative2Json.toString() + "]";
    private static final String noInitiatives = "[]";

    @Test
    @DisplayName("from NGSIJson | ok")
    void getSerializatedInitiative() {
        String description = "testDescription";
        String id = "200";
        int status = 1;
        String image = "testImage";
        String nickname = "testNickname";
        LocalDateTime date = LocalDateTime.now();
        String userId = "testUserId";

        final NGSIJson json = new NGSIJson();
        json.put(Initiative.Fields.ID.label(), id);
        json.put(Initiative.Fields.DESCRIPTION.label(), description);
        json.put(Initiative.Fields.STATUS_ID.label(), status);
        json.put(Initiative.Fields.IMAGE.label(), image);
        json.put(Initiative.Fields.NICKNAME.label(), nickname);
        json.put(Initiative.Fields.DATE.label(), date.toString());
        json.put(Initiative.Fields.USER_ID.label(), userId);

        final FiwareInitiativeRepository repository = new FiwareInitiativeRepository(mock(RestPerformer.class));
        final Initiative testInitiative = repository.fromNGSIJson(json);

        assertAll("Expected initiative",
                () -> assertEquals(testInitiative.get_id(), id),
                () -> assertEquals(testInitiative.getDescription(), description),
                () -> assertEquals(testInitiative.getStatusId(), status),
                () -> assertEquals(testInitiative.getImage(), image),
                () -> assertEquals(testInitiative.getNickname(), nickname),
                () -> assertEquals(testInitiative.getDate(), date),
                () -> assertEquals(testInitiative.getUserId(), userId));
    }

    @Test
    @DisplayName("Request initiatives | having initiatives | return all initiatives in default order ")
    void getAllInitiativesByDefaultOrder() {

        final RestPerformer performer = mock(RestPerformer.class);
        final FiwareInitiativeRepository repository = new FiwareInitiativeRepository(performer);
        when(performer.get(anyString())).thenReturn(bothInitiatives);
        final List<Initiative> initiatives = repository.getAllInitiatives(InitiativeSorter.DEFAULT_COMPARATOR);

        verify(performer, times(1)).get("http://localhost:1026/v2/entities?options=keyValues&type=Initiative");

        assertAll("Expected result",
                () -> assertEquals(2, initiatives.size()),
                () -> assertEquals(id1, initiatives.get(0).get_id()),
                () -> assertEquals(id2, initiatives.get(1).get_id()));
    }


    @Test
    @DisplayName("Request initiatives by desc date | having initiatives | return all initiatives by desc date order ")
    void getAllInitiativesByDescDateOrder() {

        final RestPerformer performer = mock(RestPerformer.class);
        final FiwareInitiativeRepository repository = new FiwareInitiativeRepository(performer);
        when(performer.get(anyString())).thenReturn(bothInitiatives);
        final List<Initiative> initiatives = repository.getAllInitiatives(InitiativeSorter.DATE_DESC);

        verify(performer, times(1)).get("http://localhost:1026/v2/entities?options=keyValues&orderBy=!date&type=Initiative");

        assertAll("Expected result",
                () -> assertEquals(2, initiatives.size()),
                () -> assertEquals(id1, initiatives.get(0).get_id()),
                () -> assertEquals(id2, initiatives.get(1).get_id()));
    }

    @Test
    @DisplayName("Request initiatives | without having initiatives | return an empty Json ")
    void getAllInitiativesWithoutHavingCreated() {

        final RestPerformer performer = mock(RestPerformer.class);
        final FiwareInitiativeRepository repository = new FiwareInitiativeRepository(performer);
        when(performer.get(anyString())).thenReturn(noInitiatives);
        final List<Initiative> initiatives = repository.getAllInitiatives(InitiativeSorter.DEFAULT_COMPARATOR);

        verify(performer, times(1)).get("http://localhost:1026/v2/entities?options=keyValues&type=Initiative");

        assertTrue(initiatives.isEmpty());
    }

    private static NGSIJson buildTestInitiativeJsonResponse(Initiative initiative, String id1) {
        final NGSIJson json = new NGSIJson();

        json.setId(id1);
        json.put(Initiative.Fields.DESCRIPTION.label(), initiative.getDescription());
        json.put(Initiative.Fields.STATUS_ID.label(), initiative.getStatusId());
        json.put(Initiative.Fields.IMAGE.label(), initiative.getImage());
        json.put(Initiative.Fields.NICKNAME.label(), initiative.getNickname());
        json.put(Initiative.Fields.USER_ID.label(), initiative.getUserId());
        json.put(Initiative.Fields.DATE.label(), initiative.getDate());

        return json;
    }

}
