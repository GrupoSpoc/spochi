package com.spochi.repository.fiware;

import com.spochi.entity.Initiative;
import com.spochi.entity.User;
import com.spochi.entity.UserType;
import com.spochi.repository.UserRepository;
import com.spochi.repository.fiware.ngsi.NGSIJson;
import com.spochi.repository.fiware.ngsi.NGSIQueryBuilder;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class FiwareUserRepository extends FiwareRepository<User> implements UserRepository {

    @Override
    protected String getEntityType() {
        return User.getEntityType();
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
        queryBuilder.type(Initiative.getEntityType())
                .ref(User.getEntityType(), id)
                .count();

        return super.count(queryBuilder);
    }

    public List<User> findByType(UserType type) {
        final NGSIQueryBuilder queryBuilder = new NGSIQueryBuilder();
        queryBuilder.attribute(User.Fields.TYPE_ID, String.valueOf(type.getId()));

        return find(queryBuilder);
    }
}
