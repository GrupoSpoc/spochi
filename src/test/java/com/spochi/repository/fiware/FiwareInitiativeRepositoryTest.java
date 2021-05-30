package com.spochi.repository.fiware;

import com.spochi.entity.Initiative;
import com.spochi.repository.fiware.ngsi.NGSIFieldType;
import com.spochi.repository.fiware.ngsi.NGSIJson;
import com.spochi.repository.fiware.rest.RestPerformer;
import com.spochi.service.query.InitiativeSorter;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
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
    @DisplayName("get entity type | ok")
    void getEntityType() {
        final FiwareInitiativeRepository repository = new FiwareInitiativeRepository(mock(RestPerformer.class));

        assertEquals(Initiative.NGSIType, repository.getEntityType());
    }

    @Test
    @DisplayName("ToNGSIJson | returns initiative")
    void toNGSIJsonOk() {
        final RestPerformer performer = mock(RestPerformer.class);
        final FiwareInitiativeRepository repository = new FiwareInitiativeRepository(performer);
        final Initiative testInitiative = repository.fromNGSIJson(initiative1Json);

        assertAll("Expected initiative",
                () -> assertEquals(testInitiative.get_id(), id1),
                () -> assertEquals(testInitiative.getUserId(), initiative1Json.getString(Initiative.Fields.USER_ID)),
                () -> assertEquals(testInitiative.getDate().toString(), initiative1Json.getString(Initiative.Fields.DATE)),
                () -> assertEquals(testInitiative.getDescription(), initiative1Json.getString(Initiative.Fields.DESCRIPTION)),
                () -> assertEquals(testInitiative.getNickname(), initiative1Json.getString(Initiative.Fields.NICKNAME)),
                () -> assertEquals(testInitiative.getStatusId(), initiative1Json.getInt(Initiative.Fields.STATUS_ID)),
                () -> assertEquals(testInitiative.getImage(), initiative1Json.getString(Initiative.Fields.IMAGE))
        );
    }

    @Test
    @DisplayName("InitiativeFieldsTest | Ok")
    void initiativeFieldsTest() {
        String id = "id";
        String description = "description";
        String image = "image";
        String nickname = "nickname";
        String date = "date";
        String userId = "refUser";
        String statusId = "status_id";
        NGSIFieldType idType = NGSIFieldType.INTEGER;
        NGSIFieldType textType = NGSIFieldType.TEXT;
        NGSIFieldType dateType = NGSIFieldType.DATE;
        NGSIFieldType referenceType = NGSIFieldType.REFERENCE;

        assertAll("Labels",
                () -> assertEquals(id, Initiative.Fields.ID.label()),
                () -> assertEquals(description, Initiative.Fields.DESCRIPTION.label()),
                () -> assertEquals(image, Initiative.Fields.IMAGE.label()),
                () -> assertEquals(nickname, Initiative.Fields.NICKNAME.label()),
                () -> assertEquals(date, Initiative.Fields.DATE.label()),
                () -> assertEquals(userId, Initiative.Fields.USER_ID.label()),
                () -> assertEquals(statusId, Initiative.Fields.STATUS_ID.label()));

        assertAll("Types",
                () -> assertEquals(idType, Initiative.Fields.ID.type()),
                () -> assertEquals(textType, Initiative.Fields.DESCRIPTION.type()),
                () -> assertEquals(dateType, Initiative.Fields.DATE.type()),
                () -> assertEquals(referenceType, Initiative.Fields.USER_ID.type()));
    }

    @Test
    @DisplayName("getSerializatedInitiative | ok")
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
                () -> assertEquals(id, testInitiative.get_id()),
                () -> assertEquals(description, testInitiative.getDescription()),
                () -> assertEquals(status, testInitiative.getStatusId()),
                () -> assertEquals(image, testInitiative.getImage()),
                () -> assertEquals(nickname, testInitiative.getNickname()),
                () -> assertEquals(date, testInitiative.getDate()),
                () -> assertEquals(userId, testInitiative.getUserId()));
    }

    @Test
    @DisplayName("getAllInitiativesByDefaultOrder | having initiatives | return all initiatives in default order ")
    void getAllInitiativesByDefaultOrder() {

        final RestPerformer performer = mock(RestPerformer.class);
        final FiwareInitiativeRepository repository = new FiwareInitiativeRepository(performer);
        when(performer.get(anyString())).thenReturn(bothInitiatives);
        final List<Initiative> initiatives = repository.getAllInitiatives(InitiativeSorter.DEFAULT_COMPARATOR);

        verify(performer, times(1)).get(contains("/v2/entities?options=keyValues&type=Initiative"));

        assertAll("Expected result",
                () -> assertEquals(2, initiatives.size()),
                () -> assertEquals(id1, initiatives.get(0).get_id()),
                () -> assertEquals(id2, initiatives.get(1).get_id()));
    }


    @Test
    @DisplayName("getAllInitiativesByDescDateOrder | having initiatives | return all initiatives by desc date order ")
    void getAllInitiativesByDescDateOrder() {

        final RestPerformer performer = mock(RestPerformer.class);
        final FiwareInitiativeRepository repository = new FiwareInitiativeRepository(performer);
        when(performer.get(anyString())).thenReturn(bothInitiatives);
        final List<Initiative> initiatives = repository.getAllInitiatives(InitiativeSorter.DATE_DESC);

        verify(performer, times(1)).get(contains("/v2/entities?options=keyValues&orderBy=!date&type=Initiative"));

        assertAll("Expected result",
                () -> assertEquals(2, initiatives.size()),
                () -> assertEquals(id1, initiatives.get(0).get_id()),
                () -> assertEquals(id2, initiatives.get(1).get_id()));
    }

    @Test
    @DisplayName("getAllInitiativesWithoutHavingCreated | without having initiatives | return an empty Json ")
    void getAllInitiativesWithoutHavingCreated() {

        final RestPerformer performer = mock(RestPerformer.class);
        final FiwareInitiativeRepository repository = new FiwareInitiativeRepository(performer);
        when(performer.get(anyString())).thenReturn(noInitiatives);
        final List<Initiative> initiatives = repository.getAllInitiatives(InitiativeSorter.DEFAULT_COMPARATOR);

        verify(performer, times(1)).get(contains("/v2/entities?options=keyValues&type=Initiative"));

        assertTrue(initiatives.isEmpty());
    }

    @Test
    @DisplayName("getInitiativeById | having initiatives | return initiative")
    void getInitiativeById(){
        final RestPerformer performer = mock(RestPerformer.class);
        final FiwareInitiativeRepository repository = new FiwareInitiativeRepository(performer);

        final Optional<Initiative> testInitiative = repository.findById(id1);
    }

    private static NGSIJson buildTestInitiativeJsonResponse(Initiative initiative, String id1) {
        final NGSIJson json = new NGSIJson();

        json.setId(id1);
        json.put(Initiative.Fields.DESCRIPTION.label(), initiative.getDescription());
        json.put(Initiative.Fields.STATUS_ID.label(), initiative.getStatusId());
        json.put(Initiative.Fields.IMAGE.label(), initiative.getImage());
        json.put(Initiative.Fields.NICKNAME.label(), initiative.getNickname());
        json.put(Initiative.Fields.USER_ID.label(), initiative.getUserId());
        json.put(Initiative.Fields.DATE.label(), initiative.getDate().withNano(0).toString());

        return json;
    }

}
