package com.spochi.repository.fiware.ngsi;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class NGSIQueryBuilder {
    private static final String Q_KEY = "q";
    private static final String SIMPLE_EQ = "=";
    private static final String DOUBLE_EQ = "==";
    private static final String LS = "<";
    private static final String GT = ">";
    private static final String Q_SEPARATOR = ";";
    private static final String BLANK = "";

    private final Map<String, String> params;

    public NGSIQueryBuilder() {
        params = new HashMap<>();
    }

    public NGSIQueryBuilder type(NGSIEntityType type) {
        params.put("type", type.label());
        return this;
    }

    public NGSIQueryBuilder attributeEQ(NGSIField attribute, String ... values) {
        return qExpression(attribute.label() + DOUBLE_EQ + String.join(",", values));
    }

    public NGSIQueryBuilder attributeLS(NGSIField attribute, String value) {
        return qExpression(attribute.label() + LS + value);
    }

    public NGSIQueryBuilder attributeGT(NGSIField attribute, String value) {
        return qExpression(attribute.label() + GT + value);
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
        return qExpression("ref" + type.label() + DOUBLE_EQ + id);
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
        if (key.endsWith(LS) || key.endsWith(GT)) {
            return BLANK;
        } else {
            return SIMPLE_EQ;
        }
    }

    private NGSIQueryBuilder qExpression(String expression) {
        if (params.containsKey(Q_KEY)) {
            params.put(Q_KEY, params.get(Q_KEY) + Q_SEPARATOR + expression);
        } else {
            params.put(Q_KEY, expression);
        }

        return this;
    }
}
