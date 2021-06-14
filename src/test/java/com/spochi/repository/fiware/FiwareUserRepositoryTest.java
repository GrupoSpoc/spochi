package com.spochi.repository.fiware;

import com.spochi.entity.User;
import com.spochi.entity.UserType;
import com.spochi.repository.fiware.ngsi.NGSICommonFields;
import com.spochi.repository.fiware.ngsi.NGSIJson;
import com.spochi.repository.fiware.rest.RestPerformer;
import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.data.relational.core.sql.In;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static com.spochi.util.AssertUtils.assertException;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.contains;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.times;

class FiwareUserRepositoryTest {

    private static final User testUser1 = User.builder()
            .nickname("test-user-1")
            .typeId(UserType.PERSON.getId())
            .uid("test-uid-2")
            .build();

    private static final User testUser2 = User.builder()
            .nickname("test-user-2")
            .typeId(UserType.ORGANIZATION.getId())
            .uid("test-uid-2")
            .build();

    private static final String id1 = "id-1";
    private static final String id2 = "id-2";

    private static final NGSIJson testUser1Json = buildTestUserJsonResponse(testUser1, id1);
    private static final NGSIJson testUser2Json = buildTestUserJsonResponse(testUser2, id2);

    private static final String twoUsersArray = "[" + testUser1Json.toString() + "," + testUser2Json.toString() + "]";
    private static final String emptyArray = "[]";

    @Test
    @DisplayName("get entity type | ok")
    void getEntityType() {
        final FiwareUserRepository repository = new FiwareUserRepository(mock(RestPerformer.class));

        assertEquals(User.NGSIType, repository.getEntityType());
    }

    @Test
    @DisplayName("from NGSIJson | with password | ok")
    void fromNGSIJsonWithoutPasswordOk() {
        final String id = NGSICommonFields.ID.prefix() + ":User:001";
        final String uid = "test-uid";
        final String nickname = "test-nickname";
        final int typeId = 3;

        final NGSIJson json = new NGSIJson();
        json.setId(id);
        json.put(User.Fields.UID.label(), uid);
        json.put(User.Fields.NICKNAME.label(), nickname);
        json.put(User.Fields.TYPE_ID.label(), typeId);

        final FiwareUserRepository repository = new FiwareUserRepository(mock(RestPerformer.class));

        final User user = repository.fromNGSIJson(json);

        assertAll("Expected user",
                () -> assertEquals(id, user.getId()),
                () -> assertEquals(uid, user.getUid()),
                () -> assertEquals(nickname, user.getNickname()),
                () -> assertEquals(typeId, user.getTypeId()),
                () -> assertNull(user.getPassword()));
    }

    @Test
    @DisplayName("from NGSIJson | with password | ok")
    void fromNGSIJsonWithPasswordOk() {
        final String id = NGSICommonFields.ID.prefix() + ":User:001";
        final String uid = "test-uid";
        final String nickname = "test-nickname";
        final String password = "test-password";
        final int typeId = 3;

        final NGSIJson json = new NGSIJson();
        json.setId(id);
        json.put(User.Fields.UID.label(), uid);
        json.put(User.Fields.NICKNAME.label(), nickname);
        json.put(User.Fields.TYPE_ID.label(), typeId);
        json.put(User.Fields.PASSWORD.label(), password);

        final FiwareUserRepository repository = new FiwareUserRepository(mock(RestPerformer.class));

        final User user = repository.fromNGSIJson(json);

        assertAll("Expected user",
                () -> assertEquals(id, user.getId()),
                () -> assertEquals(uid, user.getUid()),
                () -> assertEquals(nickname, user.getNickname()),
                () -> assertEquals(typeId, user.getTypeId()),
                () -> assertEquals(password, user.getPassword()));
    }

    @Test
    @DisplayName("find by uid | ok")
    void findByUidOk() {
        final RestPerformer performer = mock(RestPerformer.class);

        when(performer.get(anyString())).thenReturn(testUser1Json.toString());

        final FiwareUserRepository repository = new FiwareUserRepository(performer);

        final User result = repository.findByUid("uid").orElseThrow(RuntimeException::new);

        assertAll("Expected result",
                () -> assertEquals(testUser1.getUid(), result.getUid()),
                () -> assertEquals(testUser1.getNickname(), result.getNickname()),
                () -> assertEquals(testUser1.getTypeId(), result.getTypeId()),
                () -> assertEquals(id1, result.getId())
        );
    }

    @Test
    @DisplayName("find by uid | when no result | should return empty option")
    void findByUidNoResultOk() {
        final RestPerformer performer = mock(RestPerformer.class);

        when(performer.get(anyString())).thenReturn(null);

        final FiwareUserRepository repository = new FiwareUserRepository(performer);

        final Optional<User> result = repository.findByUid("uid");

        assertFalse(result.isPresent());
    }

    @Test
    @DisplayName("find by uid | when exception is thrown | should not catch")
    void getFindByUidException() {
        final RestPerformer performer = mock(RestPerformer.class);

        when(performer.get(anyString())).thenThrow(new FiwareException("uid-error"));

        final FiwareUserRepository repository = new FiwareUserRepository(performer);

        assertException(FiwareException.class, () -> repository.findByUid("uid"), "uid-error");
    }

    @Test
    @DisplayName("find by nickname | ok")
    void findByNicknameOk() {
        final RestPerformer performer = mock(RestPerformer.class);

        when(performer.get(anyString())).thenReturn(twoUsersArray);

        final FiwareUserRepository repository = new FiwareUserRepository(performer);

        final User result = repository.findByNickname("nickname").orElseThrow(RuntimeException::new);

        assertAll("Expected result",
                () -> assertEquals(testUser1.getUid(), result.getUid()),
                () -> assertEquals(testUser1.getNickname(), result.getNickname()),
                () -> assertEquals(testUser1.getTypeId(), result.getTypeId()),
                () -> assertEquals(id1, result.getId())
        );
    }

    @Test
    @DisplayName("find by nickname | when no result | should return empty option")
    void findByNicknameEmptyResultOk() {
        final RestPerformer performer = mock(RestPerformer.class);

        when(performer.get(anyString())).thenReturn(emptyArray);

        final FiwareUserRepository repository = new FiwareUserRepository(performer);

        final Optional<User> result = repository.findByNickname("nickname");

        assertFalse(result.isPresent());
    }

    @Test
    @DisplayName("find by nickname | when exception is thrown | should not catch")
    void getFindByNicknameException() {
        final RestPerformer performer = mock(RestPerformer.class);

        when(performer.get(anyString())).thenThrow(new IllegalArgumentException("nickname-error"));

        final FiwareUserRepository repository = new FiwareUserRepository(performer);

        assertException(IllegalArgumentException.class, () -> repository.findByNickname("nickname"), "nickname-error");
    }




    @Test
    @DisplayName("build next id | ok")
    void buildNextIdOk() {
        final FiwareUserRepository repository = new FiwareUserRepository(mock(RestPerformer.class));

        assertEquals(NGSICommonFields.ID.prefix() + User.NGSIType.label() + ":" + testUser1.getUid(), repository.nextId(testUser1));
        assertEquals(NGSICommonFields.ID.prefix() + User.NGSIType.label() + ":" + testUser2.getUid(), repository.nextId(testUser2));
    }

    @Test
    @DisplayName("getInitiativesByStatus | ok")
    void getInitiativesByStatus() {
        final String uid = "user id";
        final RestPerformer performer = mock(RestPerformer.class);
        final FiwareUserRepository repository = new FiwareUserRepository(performer);

        when(performer.get(anyString())).thenReturn("[{\"id\":\"urn:ngsi-ld:Initiative:001\",\"type\":\"Initiative\",\"status_id\":2},{\"id\":\"urn:ngsi-ld:Initiative:2\",\"type\":\"Initiative\",\"status_id\":1},{\"id\":\"urn:ngsi-ld:Initiative:3\",\"type\":\"Initiative\",\"status_id\":2},{\"id\":\"urn:ngsi-ld:Initiative:4\",\"type\":\"Initiative\",\"status_id\":3}]");

        Map<Integer, Integer> initiativeMap = repository.getUserInitiativesByStatus(uid);

        verify(performer, times(1)).get(contains("/v2/entities?q=refUser==user id&options=keyValues&type=Initiative&attr=status_id"));
        assertAll("Fiware response",
        () -> assertEquals(initiativeMap.size(), 3),
                () -> assertEquals(initiativeMap.get(1), 1),
                () -> assertEquals(initiativeMap.get(2), 2),
                () -> assertEquals(initiativeMap.get(3), 1));
    }

    @Test
    @DisplayName("getInitiativesByStatus | no initiatives")
    void getInitiativesByStatusNoInitiatives() {
        final String uid = "";
        final RestPerformer performer = mock(RestPerformer.class);
        final FiwareUserRepository repository = new FiwareUserRepository(performer);

        when(performer.get(anyString())).thenReturn("[]");

        Map<Integer, Integer> initiativeMap = repository.getUserInitiativesByStatus(uid);

        verify(performer, times(1)).get(contains("/v2/entities?q=refUser==&options=keyValues&type=Initiative&attr=status_id"));
        assertEquals("{1=0, 2=0, 3=0}",initiativeMap.toString() );
    }

    private static NGSIJson buildTestUserJsonResponse(User user, String id1) {
        final NGSIJson json = new NGSIJson();

        json.setId(id1);
        json.setType(User.NGSIType);
        json.put(User.Fields.NICKNAME.label(), user.getNickname());
        json.put(User.Fields.UID.label(), user.getUid());
        json.put(User.Fields.TYPE_ID.label(), user.getTypeId());

        return json;
    }
}