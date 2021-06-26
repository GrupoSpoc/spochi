package com.spochi.repository.fiware;

import com.spochi.entity.Initiative;
import com.spochi.entity.InitiativeStatus;
import com.spochi.entity.User;
import com.spochi.entity.UserType;
import com.spochi.repository.UserRepository;
import com.spochi.repository.fiware.ngsi.NGSIEntityType;
import com.spochi.repository.fiware.ngsi.NGSIJson;
import com.spochi.repository.fiware.ngsi.NGSIQueryBuilder;
import com.spochi.repository.fiware.rest.RestPerformer;


import org.json.JSONArray;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
public class FiwareUserRepository extends FiwareRepository<User> implements UserRepository {

    @Autowired
    public FiwareUserRepository(RestPerformer performer) {
        super(performer);
    }

    @Override
    protected NGSIEntityType getEntityType() {
        return User.NGSIType;
    }

    @Override
    protected User fromNGSIJson(NGSIJson json) {
        final String id = json.getId();
        final String uid = json.getString(User.Fields.UID.label());
        final String nickname = json.getString(User.Fields.NICKNAME.label());
        final UserType type = UserType.fromIdOrElseThrow(json.getInt(User.Fields.TYPE_ID.label()));
        final String password = json.getStringOrNull(User.Fields.PASSWORD);

        return new User(id, uid, nickname, type.getId(), password);
    }

    @Override
    public Optional<User> findByUid(String uid) {
        final String userId = buildId(uid);

        return findById(userId);
    }

    @Override
    public Optional<User> findByNickname(String nickname) {
        final NGSIQueryBuilder queryBuilder = new NGSIQueryBuilder();
        queryBuilder.attributeEQ(User.Fields.NICKNAME, nickname);

        return findFirst(queryBuilder);
    }

    @Override
    public Map<Integer, Integer> getUserInitiativesByStatus(String id) {
        final NGSIQueryBuilder queryBuilder = new NGSIQueryBuilder();

        queryBuilder.type(Initiative.NGSIType)
                .ref(getEntityType(), id)
                .getAttribute(Initiative.Fields.STATUS_ID)
                .keyValues();

        String response = super.performer.get(ENTITIES_URL + queryBuilder.build());
        JSONArray array = new JSONArray(response);

        return splitInitiativesByStatus(array);
    }

    protected Map<Integer, Integer> splitInitiativesByStatus(JSONArray array) {
        Map<Integer, Integer> resultMap = new HashMap<>();
        int pendingInitiatives = 0, approvedInitiatives = 0, rejectedInitiatives = 0;

        for (Object i : array) {

            NGSIJson json = new NGSIJson(i.toString());
            InitiativeStatus status = InitiativeStatus.fromIdOrElseThrow(json.getInt(Initiative.Fields.STATUS_ID));

            if (status == InitiativeStatus.PENDING) {
                pendingInitiatives++;
            } else if (status == InitiativeStatus.APPROVED) {
                approvedInitiatives++;
            } else if(status == InitiativeStatus.REJECTED) {
                rejectedInitiatives++;
            }
        }
        resultMap.put(InitiativeStatus.PENDING.getId(), pendingInitiatives);
        resultMap.put(InitiativeStatus.APPROVED.getId(), approvedInitiatives);
        resultMap.put(InitiativeStatus.REJECTED.getId(), rejectedInitiatives);
        return resultMap;
    }

    @Override
    protected String nextId(User user) {
        return buildId(user.getUid());
    }
}
