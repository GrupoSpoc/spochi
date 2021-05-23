package com.spochi.repository.fiware;

import com.spochi.repository.fiware.ngsi.NGSICommonFields;
import com.spochi.repository.fiware.ngsi.NGSIJson;
import com.spochi.repository.fiware.ngsi.NGSIQueryBuilder;
import com.spochi.repository.fiware.ngsi.NGSISerializable;
import com.spochi.repository.fiware.rest.RestPerformer;
import org.json.JSONArray;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public abstract class FiwareRepository<T extends NGSISerializable> {
    // private static final String BASE_URL = "http://46.17.108.37:1026/v2";
    private static final String BASE_URL = "http://localhost:1026/v2";
    private static final String ENTITIES_URL = BASE_URL + "/entities";

    @Autowired
    private RestPerformer performer;

    public T create(T instance) {
        final String id = nextId();
        create(instance.toNGSIJson(id));
        return findById(id).orElseThrow(RuntimeException::new); // todo
    }

    public Optional<T> findById(String id) {
        final String query = new NGSIQueryBuilder().type(getEntityType())
                .keyValues()
                .one()
                .build();

        return Optional.of(performer.get(ENTITIES_URL + "/" + id + query, json -> fromNGSIJson(new NGSIJson(json))));
    }

    protected abstract String getEntityType();
    protected abstract T fromNGSIJson(NGSIJson json);

    protected Optional<T> findFirst(NGSIQueryBuilder queryBuilder) {
        final String query = queryBuilder
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

    private void create(NGSIJson json) {
        performer.postJson(ENTITIES_URL, json.toString());
    }

    private Optional<T> findFirst(String url) {
        return performer.get(url, json ->
                this.getFirstElementAsNGSIJson(json).map(this::fromNGSIJson));
    }

    private List<T> find(String url) {
        return performer.get(url, json ->
                new JSONArray(json).toList()
                        .stream().map(j -> this.fromNGSIJson(new NGSIJson(j.toString())))
                        .collect(Collectors.toList()));
    }

    private String nextId() {
        final String query = new NGSIQueryBuilder()
                .type(getEntityType())
                .keyValues()
                .one()
                .orderByDesc(NGSICommonFields.ID)
                .build();

        final Optional<String> lastId = performer.get(ENTITIES_URL + query,
                json -> this.getFirstElementAsNGSIJson(json).map(NGSIJson::getId));

        return lastId.map(this::nextId).orElseGet(this::firstId);
    }

    private String nextId(String lastId) {
        final int index = lastId.lastIndexOf(":") + 1;
        final int lastIdentifier = Integer.parseInt(lastId.substring(index));
        return buildId(lastIdentifier + 1);
    }

    private String firstId() {
        return buildId(1);
    }

    private String buildId(int identifier) {
        return "urn:ngsi-ld:" + getEntityType() + ":" + identifier;
    }

    private Optional<NGSIJson> getFirstElementAsNGSIJson(String arrayString) {
        final JSONArray jsonArray = new JSONArray(arrayString);
        return jsonArray.toList().stream().map(o -> new NGSIJson(o.toString())).findFirst();
    }
}
