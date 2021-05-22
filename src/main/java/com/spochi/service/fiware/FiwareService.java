package com.spochi.service.fiware;

import com.spochi.service.fiware.ngsi.NGSIJson;
import com.spochi.service.fiware.ngsi.NGSIQueryBuilder;
import com.spochi.service.fiware.ngsi.NGSISerializable;
import com.spochi.service.fiware.rest.RestPerformer;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public abstract class FiwareService <T extends NGSISerializable> {
    // private static final String BASE_URL = "http://46.17.108.37:1026/v2";
    private static final String BASE_URL = "http://localhost:1026/v2";
    private static final String ENTITIES_URL = BASE_URL + "/entities";

    @Autowired
    private RestPerformer performer;

    public T save(T instance) {
        final String id = nextId();
        save(instance.toNGSIJson(id).toString());
        return findById(id).orElseThrow(RuntimeException::new); // todo
    }

    // Todo y si no lo encuentra?
    public Optional<T> findById(String id) {
        return performer.get(ENTITIES_URL + id, json -> {
            final NGSIJson ngsiJson = new NGSIJson(json);
            return Optional.of(fromNGSIJson(ngsiJson));
        });
    }

    protected abstract String getEntityType();
    protected abstract T fromNGSIJson(NGSIJson json);

    protected Optional<T> findFirst(NGSIQueryBuilder queryBuilder) {
        final String query = queryBuilder.type(getEntityType())
                .keyValues()
                .limit(1)
                .build();

        return findFirst(ENTITIES_URL + query);
    }

    protected List<T> find(NGSIQueryBuilder queryBuilder) {
        final String query = queryBuilder.type(getEntityType()).keyValues().build();

        return find(ENTITIES_URL + query);
    }

    // Todo y si no lo encuentra?
    protected Optional<T> findFirst(String url) {
        return performer.get(url, json -> {
            final JSONArray jsonArray = new JSONArray(json);
            return Optional.of(fromNGSIJson(new NGSIJson(jsonArray.getString(0))));
        });
    }

    protected List<T> find(String url) {
        return performer.get(url, json -> {
            final JSONArray jsonArray = new JSONArray(json);

            List<T> list = new ArrayList<>();
            jsonArray.forEach(j -> list.add(this.fromNGSIJson(new NGSIJson(j.toString())))); // Todo
            return list;
        });
    }

    private void save(String json) {
        System.out.println(json);
    }

    private String nextId() {
        final NGSIQueryBuilder queryBuilder = new NGSIQueryBuilder();
        final String query = queryBuilder.type(getEntityType())
                .keyValues()
                .limit(1)
                .orderByDesc("id")
                .build();

        final Optional<String> lastId = performer.get(ENTITIES_URL + query, json -> {
            final JSONArray jsonArray = new JSONArray(json);
            if (jsonArray.length() > 0) {
                final JSONObject jsonObject = jsonArray.getJSONObject(0);
                return Optional.of(jsonObject.getString("id"));
            } else {
                return Optional.empty();
            }
        });

        return lastId.map(this::nextId).orElseGet(this::firstId);
    }

    private String firstId() {
        return buildId(1);
    }

    private String nextId(String lastId) {
        final int index = lastId.lastIndexOf(":");
        final int lastIdentifier = Integer.parseInt(lastId.substring(index));
        return buildId(lastIdentifier + 1);
    }

    private String buildId(int identifier) {
        return "urn:ngsi-ld:" + getEntityType() + ":" + identifier;
    }

}
