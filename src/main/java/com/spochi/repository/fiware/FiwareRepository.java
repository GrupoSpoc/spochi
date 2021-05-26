package com.spochi.repository.fiware;

import com.spochi.repository.fiware.ngsi.*;
import com.spochi.repository.fiware.rest.RestPerformer;
import org.apache.http.HttpStatus;
import org.json.JSONArray;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
public abstract class FiwareRepository<T extends NGSISerializable> {
    // private static final String BASE_URL = "http://46.17.108.37:1026/v2";
    private static final String BASE_URL = "http://localhost:1026/v2";
    private static final String ENTITIES_URL = BASE_URL + "/entities";

    private final RestPerformer performer;

    @Autowired
    public FiwareRepository(RestPerformer performer) {
        this.performer = performer;
    }

    public T create(T instance) {
        final String id = nextId();
        create(instance.toNGSIJson(id));
        return findById(id).orElseThrow(() ->
                new FiwareException(String.format("Fatal: [%s] with id [%s] not found after creation", getEntityType().label(), id), HttpStatus.SC_NOT_FOUND));
    }

    public Optional<T> findById(String id) {
        final String query = new NGSIQueryBuilder()
                .keyValues()
                .build();

        final String response = performer.get(ENTITIES_URL + "/" + id + query);
        return Optional.ofNullable(response).map(r -> fromNGSIJson(new NGSIJson(r)));
    }

    public void update(String id, NGSIJson payload) {
        validateForUpdate(payload);
        performer.patch(ENTITIES_URL + "/" + id + "/attrs", payload.toString());
    }

    protected abstract NGSIEntityType getEntityType();
    protected abstract T fromNGSIJson(NGSIJson json);

    protected Optional<T> findFirst(NGSIQueryBuilder queryBuilder) {
        final String query = queryBuilder
                .type(getEntityType())
                .keyValues()
                .one()
                .build();

        return findFirst(ENTITIES_URL + query);
    }

    protected List<T> find(NGSIQueryBuilder queryBuilder) {
        final String query = queryBuilder.type(getEntityType()).keyValues().build();

        return find(ENTITIES_URL + query);
    }

    protected int count(NGSIQueryBuilder queryBuilder) {
        return performer.count(ENTITIES_URL + queryBuilder.build());
    }

    protected String buildId(int identifier) {
        return NGSICommonFields.ID.prefix() + getEntityType().label() + ":" + identifier;
    }

    private void create(NGSIJson json) {
        performer.post(ENTITIES_URL, json.toString());
    }

    private Optional<T> findFirst(String url) {
        final String response = performer.get(url);
        return getFirstElementAsNGSIJson(response).map(this::fromNGSIJson);
    }

    private List<T> find(String url) {
        final String response = performer.get(url);
        final List<T> result = new ArrayList<>();

        new JSONArray(response).forEach(j -> result.add(this.fromNGSIJson(new NGSIJson(j.toString()))));
        return result;
    }

    private String nextId() {
        final String query = new NGSIQueryBuilder()
                .type(getEntityType())
                .keyValues()
                .one()
                .orderByDesc(NGSICommonFields.ID)
                .build();

        final String response = performer.get(ENTITIES_URL + query);
        final Optional<String> lastId = getFirstElementAsNGSIJson(response).map(NGSIJson::getId);

        return lastId.map(this::nextId).orElseGet(this::firstId);
    }

    private Optional<NGSIJson> getFirstElementAsNGSIJson(String arrayString) {
        final JSONArray jsonArray = new JSONArray(arrayString);
        if (jsonArray.length() == 0) return Optional.empty();
        return Optional.of(new NGSIJson(jsonArray.get(0).toString()));
    }

    protected String nextId(String lastId) {
        final int index = lastId.lastIndexOf(":") + 1;
        final int lastIdentifier = Integer.parseInt(lastId.substring(index));
        return buildId(lastIdentifier + 1);
    }

    private String firstId() {
        return buildId(1);
    }

    private void validateForUpdate(NGSIJson payload) {
        if (payload.keySet().isEmpty()) {
            throw new FiwareException("Update body cannot be empty");
        }

        if (payload.has(NGSICommonFields.ID.label()) || payload.has(NGSICommonFields.TYPE.label())) {
            throw new FiwareException("Update body cannot specify id or type");
        }
    }
}
