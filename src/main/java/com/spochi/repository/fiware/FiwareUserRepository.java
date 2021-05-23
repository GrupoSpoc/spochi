package com.spochi.repository.fiware;

import com.spochi.entity.Initiative;
import com.spochi.entity.User;
import com.spochi.entity.UserType;
import com.spochi.repository.UserRepository;
import com.spochi.repository.fiware.ngsi.NGSIEntityType;
import com.spochi.repository.fiware.ngsi.NGSIJson;
import com.spochi.repository.fiware.ngsi.NGSIQueryBuilder;
import com.spochi.repository.fiware.rest.RestPerformer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

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
        return User.fromNGSIJson(json);
    }

    @Override
    public Optional<User> findByUid(String uid) {
        final NGSIQueryBuilder queryBuilder = new NGSIQueryBuilder();
        queryBuilder.attribute(User.Fields.UID, uid);

        return findFirst(queryBuilder);
    }

    @Override
    public Optional<User> findByNickname(String nickname) {
        final NGSIQueryBuilder queryBuilder = new NGSIQueryBuilder();
        queryBuilder.attribute(User.Fields.NICKNAME, nickname);

        return findFirst(queryBuilder);
    }

    @Override
    public int getAmountOfInitiatives(String id) {
        final NGSIQueryBuilder queryBuilder = new NGSIQueryBuilder();
        queryBuilder.type(Initiative.NGSIType)
                .ref(getEntityType(), id)
                .count();

        return super.count(queryBuilder);
    }

    public List<User> findByType(UserType type) {
        final NGSIQueryBuilder queryBuilder = new NGSIQueryBuilder();
        queryBuilder.attribute(User.Fields.TYPE_ID, String.valueOf(type.getId()));

        return find(queryBuilder);
    }
}
