package com.spochi.repository.fiware.ngsi;

import java.time.LocalDateTime;

public enum NGSIFieldType {
    TEXT("Text", String.class),
    INTEGER("Number", Integer.class),
    DATE("LocalDateTime", String.class) {
        @Override
        public void validateValue(Object value) {
            super.validateValue(value);
            LocalDateTime localDateTime;

            try {
                localDateTime = LocalDateTime.parse(value.toString());
            } catch (Exception e) {
                throw new InvalidValueException(String.format("Value [%s] is not a valid LocalDateTime", value.toString()));
            }

            if (localDateTime.getNano() != 0) {
                throw new InvalidValueException("Value for LocalDateTime must have 0 nano seconds");
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

    public void validateValue(Object value) {
        if (value == null) throw new InvalidValueException("Null values are not allowed");

        try {
            clazz.cast(value);
        } catch (Exception e) {
            throw new InvalidValueException(String.format("Type %s expected an instance of %s but [%s] is a %s", this.name, this.clazz.getSimpleName(), value.toString(), value.getClass().getSimpleName()));
        }
    }

    public static class InvalidValueException extends RuntimeException {

        public InvalidValueException(String message) {
            super(message);
        }
    }
}
