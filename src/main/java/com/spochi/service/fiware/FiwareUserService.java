package com.spochi.service.fiware;

import com.spochi.entity.User;
import com.spochi.entity.UserType;
import com.spochi.service.fiware.ngsi.NGSIQueryBuilder;
import org.json.JSONObject;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class FiwareUserService extends FiwareService<User> {

    @Override
    protected String getEntityType() {
        return "User";
    }

    @Override
    protected User fromNGSIJson(JSONObject json) {
        return User.fromNGSIJson(json);
    }

    public Optional<User> findByUid(String uid) {
        final NGSIQueryBuilder queryBuilder = new NGSIQueryBuilder();
        queryBuilder.attribute(User.Fields.UID, uid);

        return findFirst(queryBuilder);
    }

    public Optional<User> findByNickname(String nickname) {
        final NGSIQueryBuilder queryBuilder = new NGSIQueryBuilder();
        queryBuilder.attribute(User.Fields.NICKNAME, nickname);

        return findFirst(queryBuilder);
    }

    public List<User> findByType(UserType type) {
        final NGSIQueryBuilder queryBuilder = new NGSIQueryBuilder();
        queryBuilder.attribute(User.Fields.TYPE_ID, String.valueOf(type.getId()));

        return find(queryBuilder);
    }
}
