package com.spochi.repository.fiware.ngsi;

import com.google.api.client.util.DateTime;

import java.time.LocalDateTime;

public enum NGSIFieldType {
    TEXT("Text", String.class),
    INTEGER("Number", Integer.class),
    DATE("LocalDateTime", String.class) {
        @Override
        boolean isValueValid(Object value) {
            if (!super.isValueValid(value)) return false;
            try {
                final LocalDateTime localDateTime = LocalDateTime.parse(value.toString());
                return localDateTime.getNano() == 0;
            } catch (Exception e) {
                return false;
            }
        }
    };

    private final String name;
    private final Class<?> clazz;

    NGSIFieldType(String name, Class<?> clazz) {
        this.name = name;
        this.clazz = clazz;
    }

    public String getName() {
        return name;
    }

    boolean isValueValid(Object value) {
        if (value == null) return false;

        try {
            clazz.cast(value);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
