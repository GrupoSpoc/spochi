package com.spochi.repository.fiware;

import com.spochi.entity.User;
import com.spochi.entity.UserType;
import com.spochi.repository.fiware.ngsi.NGSICommonFields;
import com.spochi.repository.fiware.ngsi.NGSIJson;
import com.spochi.repository.fiware.rest.RestPerformer;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static com.spochi.util.AssertUtils.assertException;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

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
    @DisplayName("from NGSIJson | ok")
    void fromNGSIJsonOk() {
        final String id = "urn:ngsi-ld:User:001";
        final String uid = "test-uid";
        final String nickname = "test-nickname";
        final int typeId = 1;

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
                () -> assertEquals(typeId, user.getTypeId()));
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
    @DisplayName("get amount of initiatives | ok")
    void getAmountOfInitiativesOk() {
        final RestPerformer performer = mock(RestPerformer.class);

        when(performer.count(anyString())).thenReturn(3);

        final FiwareUserRepository repository = new FiwareUserRepository(performer);

        assertEquals(3, repository.getAmountOfInitiatives("id"));
    }

    @Test
    @DisplayName("get amount of initiatives | when exception is thrown | should not catch")
    void getAmountOfInitiativesException() {
        final RestPerformer performer = mock(RestPerformer.class);

        when(performer.count(anyString())).thenThrow(new RuntimeException("count-error"));

        final FiwareUserRepository repository = new FiwareUserRepository(performer);

        assertException(RuntimeException.class, () -> repository.getAmountOfInitiatives("id"), "count-error");
    }

    @Test
    @DisplayName("build next id | ok")
    void buildNextIdOk() {
        final FiwareUserRepository repository = new FiwareUserRepository(mock(RestPerformer.class));

        assertEquals(NGSICommonFields.ID.prefix() + User.NGSIType.label() + ":" + testUser1.getUid(), repository.nextId(testUser1));
        assertEquals(NGSICommonFields.ID.prefix() + User.NGSIType.label() + ":" + testUser2.getUid(), repository.nextId(testUser2));
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