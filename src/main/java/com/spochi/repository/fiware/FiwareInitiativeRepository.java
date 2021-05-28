package com.spochi.repository.fiware;

import com.spochi.entity.Initiative;
import com.spochi.entity.InitiativeStatus;
import com.spochi.repository.InitiativeRepository;
import com.spochi.repository.fiware.ngsi.NGSIEntityType;
import com.spochi.repository.fiware.ngsi.NGSIJson;
import com.spochi.repository.fiware.ngsi.NGSIQueryBuilder;
import com.spochi.repository.fiware.rest.RestPerformer;
import com.spochi.service.query.InitiativeSorter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.*;

@Repository
public class FiwareInitiativeRepository extends FiwareRepository<Initiative> implements InitiativeRepository {

    @Autowired
    public FiwareInitiativeRepository(RestPerformer performer) {
        super(performer);
    }

    @Override
    protected NGSIEntityType getEntityType() {
        return Initiative.NGSIType;
    }

    @Override
    protected Initiative fromNGSIJson(NGSIJson json) {

        final String id = json.getId();
        final String description = json.getString(Initiative.Fields.DESCRIPTION);
        final String image = json.getString(Initiative.Fields.IMAGE);
        final String nickname = json.getString(Initiative.Fields.NICKNAME);
        final LocalDateTime date = LocalDateTime.parse(json.getString(Initiative.Fields.DATE));
        final String userId = json.getString(Initiative.Fields.USER_ID);
        final int statusId = InitiativeStatus.fromIdOrElseThrow(json.getInt(Initiative.Fields.STATUS_ID)).getId();

        return new Initiative(id, description, image, nickname, date, userId, statusId);
    }

    @Override
    public List<Initiative> getAllInitiatives(InitiativeSorter sorter) {

        final NGSIQueryBuilder builder = new NGSIQueryBuilder();

        if (sorter == InitiativeSorter.DATE_DESC) {
            builder.orderByDesc(Initiative.Fields.DATE);
        }
        return find(builder);
    }

    @Override
    public Optional<Initiative> findInitiativeById(String id) {
        return findById(id);
    }
}
