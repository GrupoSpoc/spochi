package com.spochi.repository.fiware.ngsi;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class NGSIQueryBuilder {
    private static final String Q = "q";
    private static final String SIMPLE_EQ = "=";
    private static final String EQ = "==";
    private static final String LS = "<";


    private final Map<String, String> params;

    public NGSIQueryBuilder() {
        params = new HashMap<>();
    }

    public NGSIQueryBuilder type(NGSIEntityType type) {
        params.put("type", type.label());
        return this;
    }

    public NGSIQueryBuilder attributeEq(NGSIField attribute, String ... values) {
        return qExpression(attribute.label() + EQ + String.join(",", values));
    }

    public NGSIQueryBuilder attributeLs(NGSIField attribute, String value) {
        return qExpression(attribute.label() + LS + value);
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
        return qExpression("ref" + type.label() + EQ + id);
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

    protected static String comparator(String key) {
        if (key.endsWith(LS) || key.endsWith(">")) {
            return "";
        } else {
            return SIMPLE_EQ;
        }
    }

    private NGSIQueryBuilder qExpression(String expression) {
        if (params.containsKey(Q)) {
            params.put(Q, params.get(Q) + ";" + expression);
        } else {
            params.put(Q, expression);
        }

        return this;
    }
}
