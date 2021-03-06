package com.spochi.repository.fiware.ngsi;

import java.time.LocalDateTime;

public enum NGSIFieldType {
    TEXT("Text", String.class),
    INTEGER("Number", Integer.class),
    LONG("Number", Long.class),
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
    },
    REFERENCE("Reference", String.class) {
        @Override
        public void validateValue(Object value) {
            super.validateValue(value);

            final String valueString = value.toString();

            if (!valueString.startsWith(NGSICommonFields.ID.prefix())) {
                throw new InvalidValueException(String.format("Value [%s] for Reference is not a valid id", valueString));
            }
        }
    };

    private final String label;
    private final Class<?> clazz;

    NGSIFieldType(String label, Class<?> clazz) {
        this.label = label;
        this.clazz = clazz;
    }

    public String label() {
        return label;
    }

    public void validateValue(Object value) {
        if (value == null) throw new InvalidValueException("Null values are not allowed");

        try {
            clazz.cast(value);
        } catch (Exception e) {
            throw new InvalidValueException(String.format("Type %s expected an instance of %s but [%s] is a %s", this.label, this.clazz.getSimpleName(), value.toString(), value.getClass().getSimpleName()));
        }
    }

    public static class InvalidValueException extends RuntimeException {

        public InvalidValueException(String message) {
            super(message);
        }
    }
}
