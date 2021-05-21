package com.spochi.fiware;

import com.spochi.entity.User;
import com.spochi.fiware.ngsi.NGSIQueryBuilder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class FiwareUserService extends FiwareService<User> {

    @Override
    protected String getEntityType() {
        return "User";
    }

    @Override
    protected User fromNGSIJson(String json) {
        return User.fromNGSIJson(json);
    }

    public Optional<User> findByUid(String uid) {
        final NGSIQueryBuilder query = new NGSIQueryBuilder();
        query.attribute("uid", uid).limit(1);
        return find(query);
    }
}
