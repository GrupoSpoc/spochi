package com.spochi.repository.fiware.ngsi;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class NGSIQueryBuilder {
    private final Map<String, String> params;

    public NGSIQueryBuilder() {
        params = new HashMap<>();
    }

    public NGSIQueryBuilder type(NGSIEntityType type) {
        params.put("type", type.label());
        return this;
    }

    public NGSIQueryBuilder attributeEq(NGSIField attribute, String value) {
        final String key = "q=" + attribute.label() + "=";

        if (params.containsKey(key)) {
            params.put(key, params.get(key) + "," + value);
        } else {
            params.put(key, value);
        }

        return this;
    }

    public NGSIQueryBuilder attributeLs(NGSIField attribute, String value) {
        params.put("q=" + attribute.label() + "<", value);
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
        params.put("attr", attribute.label());
        return this;
    }

    public NGSIQueryBuilder orderByDesc(NGSIField attribute) {
        params.put("orderBy", "!" + attribute.label());
        return this;
    }

    public NGSIQueryBuilder ref(NGSIEntityType type, String id) {
        params.put("q=ref" + type.label() + "=", id);
        return this;
    }

    public NGSIQueryBuilder offset(int offset) {
        params.put("offset", String.valueOf(offset));
        return this;
    }


    public String build() {
        if (params.isEmpty()) return "";

        return "?" + this.params.entrySet().stream().map(entry -> {
            String key = entry.getKey();
            String value = entry.getValue();
            return key + comparator(key) + value;
        }).collect(Collectors.joining("&"));
    }

    private String comparator(String key) {
        if (key.contains("<") || key.contains(">")) {
            return "";
        } else {
            return "=";
        }
    }
}
