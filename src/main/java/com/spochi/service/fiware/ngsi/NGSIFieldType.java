package com.spochi.service.fiware.ngsi;

public enum NGSIFieldType {
    TEXT("Text"),
    NUMBER("Number"),
    DATETIME("DateTime");

    private final String value;

    NGSIFieldType(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
