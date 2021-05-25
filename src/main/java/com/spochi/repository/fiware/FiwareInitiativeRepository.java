package com.spochi.repository.fiware;

import com.spochi.entity.Initiative;
import com.spochi.repository.InitiativeRepository;
import com.spochi.repository.fiware.ngsi.NGSIEntityType;
import com.spochi.repository.fiware.ngsi.NGSIJson;
import com.spochi.repository.fiware.ngsi.NGSIQueryBuilder;
import com.spochi.repository.fiware.rest.RestPerformer;

import java.time.LocalDateTime;
import java.util.List;

public class FiwareInitiativeRepository extends FiwareRepository<Initiative> implements InitiativeRepository {


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
        final String description = json.getString(Initiative.Fields.DESCRIPTION.label());
        final String image = json.getString(Initiative.Fields.IMAGE.label());
        final String nickname = json.getString(Initiative.Fields.NICKNAME.label());
        final LocalDateTime date = (LocalDateTime) json.get(Initiative.Fields.DATE.label());
        final String userId = json.getString(Initiative.Fields.USER_ID.label());
        final int statusId = json.getInt(Initiative.Fields.STATUS_ID.label());

        return new Initiative(id, description, image, nickname, date, userId, statusId);
    }

    @Override
    public List<Initiative> getAllInitiatives() {
        final NGSIQueryBuilder builder = new NGSIQueryBuilder();
        builder.type(Initiative.NGSIType);
        return getAll(builder);
    }

    @Override
    public Initiative create(Initiative initiative){
        return create(initiative);
    }
}
