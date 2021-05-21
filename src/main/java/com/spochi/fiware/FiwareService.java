package com.spochi.fiware;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.JsonArray;
import com.spochi.dto.InitiativeResponseDTO;
import com.spochi.fiware.ngsi.NGSIQueryBuilder;
import com.spochi.fiware.ngsi.NGSISerializable;
import okhttp3.*;
import org.json.JSONArray;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Service
public abstract class FiwareService <T extends NGSISerializable> {
    @Autowired
    ObjectMapper objectMapper;

    private static final String BASE_URL = "http://46.17.108.37:1026/v2";
    private static final String ENTITIES_URL = BASE_URL + "/entities";

    protected abstract String getEntityType();
    protected abstract T fromNGSIJson(String json);

    public T save(T instance) {
        save(instance.toNGSIJson(nextId()));
        return instance;
    }

    private void save(String json) {
        System.out.println(json);
    }

    private String nextId() {
        return "";
    }

    protected Optional<T> find(NGSIQueryBuilder queryBuilder) {
        OkHttpClient client = new OkHttpClient();

        final HttpUrl.Builder httpBuilder = HttpUrl.parse(ENTITIES_URL).newBuilder();

        final Request.Builder builder = new Request.Builder().get().url(httpBuilder.build()
        + "?" + queryBuilder.type(getEntityType()).build());

        Call call = client.newCall(builder.build());

        try {
            Response response = call.execute();
            final String json = response.body().string();
            final JSONArray jsonArray = new JSONArray(json);
            System.out.println(jsonArray.get(0).toString());
            return Optional.of(fromNGSIJson(jsonArray.get(0).toString()));
        } catch (Exception e) {
            return Optional.empty();
        }
    }
}
