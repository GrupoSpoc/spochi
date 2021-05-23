package com.spochi.repository.fiware.ngsi;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class NGSIQueryBuilder {
    private final Map<String, String> params;

    public NGSIQueryBuilder() {
        params = new HashMap<>();
    }

    public NGSIQueryBuilder type(String type) {
        params.put("type", type);
        return this;
    }

    public NGSIQueryBuilder attribute(NGSIField attribute, String value) {
        params.put("q=" + attribute.getName() + "=", value);
        return this;
    }

    public NGSIQueryBuilder count() {
        params.put("options", "count");
        return this;
    }

    public NGSIQueryBuilder keyValues() {
        params.put("options", "keyValues");
        return this;
    }

    public NGSIQueryBuilder limit(int limit) {
        params.put("limit", String.valueOf(limit));
        return this;
    }

    public NGSIQueryBuilder one() {
        return limit(1);
    }

    public NGSIQueryBuilder getAttribute(NGSIField attribute) {
        params.put("attr", attribute.getName());
        return this;
    }

    public NGSIQueryBuilder orderByDesc(NGSIField attribute) {
        params.put("orderBy", "!" + attribute.getName());
        return this;
    }

    public NGSIQueryBuilder ref(String entityType, String id) {
        params.put("q=ref" + entityType + "=", id);
        return this;
    }


    public String build() {
        if (params.isEmpty()) return "";

        return "?" + this.params.entrySet().stream().map(entry -> {
            String key = entry.getKey();
            String value = entry.getValue();
            return key + "=" + value;
        }).collect(Collectors.joining("&"));
    }
}
