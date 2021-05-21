package com.spochi.fiware.ngsi;

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

    public NGSIQueryBuilder attribute(String attribute, String value) {
        params.put("q=" + attribute + "=", value);
        return this;
    }

    public NGSIQueryBuilder count() {
        params.put("options", "count");
        return this;
    }

    public NGSIQueryBuilder limit(int limit) {
        params.put("limit", String.valueOf(limit));
        return this;
    }

    public NGSIQueryBuilder getAttribute(String attribute) {
        params.put("attr", attribute);
        return this;
    }

    public String build() {
        return this.params.entrySet().stream().map(entry -> {
            String key = entry.getKey();
            String value = entry.getValue();
            return key + "=" + value;
        }).collect(Collectors.joining("&"));
    }
}
