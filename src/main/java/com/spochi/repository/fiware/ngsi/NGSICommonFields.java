package com.spochi.repository.fiware.ngsi;

public enum NGSICommonFields implements NGSIField {
    ID("id", NGSIFieldType.TEXT),
    TYPE("type", NGSIFieldType.TEXT),
    VALUE("value", NGSIFieldType.TEXT);

    private final String value;
    private final NGSIFieldType type;

    NGSICommonFields(String value, NGSIFieldType type) {
        this.value = value;
        this.type = type;
    }

    @Override
    public String getValue() {
        return this.value;
    }

    @Override
    public NGSIFieldType getType() {
        return this.type;
    }
}
