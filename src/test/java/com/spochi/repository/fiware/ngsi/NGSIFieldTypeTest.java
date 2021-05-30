package com.spochi.repository.fiware.ngsi;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static com.spochi.repository.fiware.ngsi.NGSIFieldType.*;
import static com.spochi.util.AssertUtils.assertException;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;

@DisplayName("NGSI Field Type Test | Unit")
class NGSIFieldTypeTest {

    @Test
    @DisplayName("label | ok")
    void getName() {
        assertEquals("Text", TEXT.label());
        assertEquals("Number", INTEGER.label());
        assertEquals("LocalDateTime", DATE.label());
        assertEquals("Reference", REFERENCE.label());
    }

    @Test
    @DisplayName("validate value | when type is TEXT")
    void validateValueText() {
        assertDoesNotThrow(() -> TEXT.validateValue("a-text"));

        assertException(InvalidValueException.class, () -> TEXT.validateValue(10), "Type Text expected an instance of String but [10] is a Integer");
        assertException(InvalidValueException.class, () -> TEXT.validateValue(null), "Null values are not allowed");
    }

    @Test
    @DisplayName("validate value | when type is INTEGER")
    void validateValueInteger() {
        assertDoesNotThrow(() -> INTEGER.validateValue(10));

        assertException(InvalidValueException.class, () -> INTEGER.validateValue(10.4d), "Type Number expected an instance of Integer but [10.4] is a Double");
        assertException(InvalidValueException.class, () -> INTEGER.validateValue("a-text"), "Type Number expected an instance of Integer but [a-text] is a String");
        assertException(InvalidValueException.class, () -> INTEGER.validateValue(null), "Null values are not allowed");
    }

    @Test
    @DisplayName("validate value | when type is DATE")
    void validateValueDateTime() {
        assertDoesNotThrow(() -> DATE.validateValue("2021-05-22T16:53:32"));
        assertDoesNotThrow(() -> DATE.validateValue("2019-02-14T20:16:01"));
        
        assertException(InvalidValueException.class, () -> DATE.validateValue("a-text"), "Value [a-text] is not a valid LocalDateTime");
        assertException(InvalidValueException.class, () -> DATE.validateValue(10), "Type LocalDateTime expected an instance of String but [10] is a Integer");
        assertException(InvalidValueException.class, () -> DATE.validateValue(null), "Null values are not allowed");
        assertException(InvalidValueException.class, () -> DATE.validateValue("2021-05-22T16:53:32.0343"), "Value for LocalDateTime must have 0 nano seconds");
    }

    @Test
    @DisplayName("validate value | when type is REFERENCE")
    void validateValueReference() {
        assertDoesNotThrow(() -> REFERENCE.validateValue(NGSICommonFields.ID.prefix() + "User:user-uid"));
        assertDoesNotThrow(() -> REFERENCE.validateValue(NGSICommonFields.ID.prefix() + "Initiative:1"));

        assertException(InvalidValueException.class, () -> REFERENCE.validateValue("not-an-id"), "Value [not-an-id] for Reference is not a valid id");
        assertException(InvalidValueException.class, () -> REFERENCE.validateValue(10), "Type Reference expected an instance of String but [10] is a Integer");
        assertException(InvalidValueException.class, () -> REFERENCE.validateValue(null), "Null values are not allowed");
    }
}