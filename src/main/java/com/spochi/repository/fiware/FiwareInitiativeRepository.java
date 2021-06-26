package com.spochi.repository.fiware;

import com.spochi.entity.Initiative;
import com.spochi.entity.InitiativeStatus;
import com.spochi.entity.User;
import com.spochi.repository.InitiativeRepository;
import com.spochi.repository.fiware.ngsi.NGSIEntityType;
import com.spochi.repository.fiware.ngsi.NGSIJson;
import com.spochi.repository.fiware.ngsi.NGSIQueryBuilder;
import com.spochi.repository.fiware.rest.RestPerformer;
import com.spochi.service.query.InitiativeQuery;
import com.spochi.service.query.InitiativeSorter;
import com.spochi.util.DateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public class FiwareInitiativeRepository extends FiwareRepository<Initiative> implements InitiativeRepository {

    @Autowired
    public FiwareInitiativeRepository(RestPerformer performer) {
        super(performer);
    }

    @Override
    public Optional<Initiative> findInitiativeById(String id) {
        return findById(id);
    }

    @Override
    public void changeStatus(Initiative initiative, InitiativeStatus status, String rejectedDTOMotive) {
        final NGSIJson statusJson = new NGSIJson();
        statusJson.addAttribute(Initiative.Fields.STATUS_ID, status.getId());
        statusJson.addAttribute(Initiative.Fields.REJECT_MOTIVE, rejectedDTOMotive);
        update(initiative.get_id(),statusJson);
    }

    @Override
    public List<Initiative> getAllInitiatives(InitiativeQuery query) {
        final NGSIQueryBuilder queryBuilder = parseInitiativeQuery(query);
        return find(queryBuilder);
    }

    @Override
    protected NGSIEntityType getEntityType() {
        return Initiative.NGSIType;
    }

    @Override
    protected String nextId(Initiative initiative) {
        return buildId(String.valueOf(DateUtil.dateToMilliUTC(initiative.getDate())));
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
        final String rejectMotive = json.getStringOrNull(Initiative.Fields.REJECT_MOTIVE);

        return new Initiative(id, description, image, nickname, date, userId, statusId,rejectMotive);
    }

    private NGSIQueryBuilder parseInitiativeQuery(InitiativeQuery initiativeQuery) {
        final NGSIQueryBuilder ngsiQueryBuilder = new NGSIQueryBuilder();

        if (initiativeQuery.getUserId() != null) {
            ngsiQueryBuilder.ref(User.NGSIType, initiativeQuery.getUserId());
        }

        if (initiativeQuery.getSorter() == InitiativeSorter.DATE_DESC) {
            ngsiQueryBuilder.orderByDesc(Initiative.Fields.DATE);
        }

        if (initiativeQuery.getStatuses() != null) {
            ngsiQueryBuilder.attributeEQ(Initiative.Fields.STATUS_ID,
                    initiativeQuery.getStatuses().stream().map(s -> String.valueOf(s.getId())).toArray(String[]::new));
        }

        if (initiativeQuery.getDateTo() != null) {
            final long milliTo = DateUtil.dateToMilliUTC(initiativeQuery.getDateTo());
            ngsiQueryBuilder.attributeLS(Initiative.Fields.DATE, String.valueOf(milliTo));
        }

        if (initiativeQuery.getDateFrom() != null) {
            final long milliFrom = DateUtil.dateToMilliUTC(initiativeQuery.getDateFrom());
            ngsiQueryBuilder.attributeGT(Initiative.Fields.DATE, String.valueOf(milliFrom));
        }

        if (initiativeQuery.getLimit() != null) {
            ngsiQueryBuilder.limit(initiativeQuery.getLimit());
        }

        if (initiativeQuery.getOffset() != null) {
            ngsiQueryBuilder.offset(initiativeQuery.getOffset());
        }

        return ngsiQueryBuilder;
    }
}
