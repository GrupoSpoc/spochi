package com.spochi.repository.fiware.ngsi;

public enum NGSICommonFields implements NGSIField {
    ID("id", NGSIFieldType.TEXT),
    TYPE("type", NGSIFieldType.TEXT),
    VALUE("value", NGSIFieldType.TEXT);

    private final String name;
    private final NGSIFieldType type;

    NGSICommonFields(String name, NGSIFieldType type) {
        this.name = name;
        this.type = type;
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public NGSIFieldType getType() {
        return this.type;
    }
}