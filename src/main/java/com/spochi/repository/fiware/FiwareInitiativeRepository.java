package com.spochi.repository.fiware;

import com.spochi.entity.Initiative;
import com.spochi.entity.InitiativeStatus;
import com.spochi.entity.User;
import com.spochi.repository.InitiativeRepository;
import com.spochi.repository.fiware.ngsi.NGSICommonFields;
import com.spochi.repository.fiware.ngsi.NGSIEntityType;
import com.spochi.repository.fiware.ngsi.NGSIJson;
import com.spochi.repository.fiware.ngsi.NGSIQueryBuilder;
import com.spochi.repository.fiware.rest.RestPerformer;
import com.spochi.service.query.InitiativeQuery;
import com.spochi.service.query.InitiativeSorter;
import com.spochi.util.DateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
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

        final long milli = json.getLong(Initiative.Fields.DATE.label());
        final LocalDateTime date = DateUtil.milliToDateUTC(milli);

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

    @Override
    public List<Initiative> getAllInitiatives(InitiativeQuery query) {
        final NGSIQueryBuilder queryBuilder = parseQuery(query);
        return new ArrayList<>();
    }

    private NGSIQueryBuilder parseQuery(InitiativeQuery initiativeQuery) {
        final NGSIQueryBuilder ngsiQueryBuilder = new NGSIQueryBuilder();

        if (initiativeQuery.getUserId() != null) {
            ngsiQueryBuilder.ref(User.NGSIType, initiativeQuery.getUserId());
        }

        if (initiativeQuery.getSorter() != null) {
            if (initiativeQuery.getSorter() == InitiativeSorter.DATE_DESC) {
                ngsiQueryBuilder.orderByDesc(Initiative.Fields.DATE);
            }
        }

        if (initiativeQuery.getStatus() != null) {
            ngsiQueryBuilder.attribute(Initiative.Fields.STATUS_ID, String.valueOf(initiativeQuery.getStatus().getId()));
        }

        // todo guardar milisegundos en iniciativas para poder comparar
        if (initiativeQuery.getDateFrom() != null) {
            final long millis = initiativeQuery.getDateFrom().atZone(ZoneId.of("UTC")).toInstant().toEpochMilli();

        }

        return ngsiQueryBuilder;
    }
}
