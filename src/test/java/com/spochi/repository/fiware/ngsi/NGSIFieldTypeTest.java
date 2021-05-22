package com.spochi.repository.fiware.ngsi;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("NGSI Field Type Test | Unit")
class NGSIFieldTypeTest {

    @Test
    @DisplayName("get name | ok")
    void getName() {
        assertEquals("Text", NGSIFieldType.TEXT.getName());
        assertEquals("Number", NGSIFieldType.INTEGER.getName());
        assertEquals("LocalDateTime", NGSIFieldType.DATE.getName());
    }

    @Test
    @DisplayName("is value valid | when type is TEXT")
    void isValueValidText() {
        assertTrue(NGSIFieldType.TEXT.isValueValid("a-text"));
        assertFalse(NGSIFieldType.TEXT.isValueValid(10));
        assertFalse(NGSIFieldType.TEXT.isValueValid(null));
    }

    @Test
    @DisplayName("is value valid | when type is INTEGER")
    void isValueValidInteger() {
        assertTrue(NGSIFieldType.INTEGER.isValueValid(10));
        assertFalse(NGSIFieldType.INTEGER.isValueValid(10.4));
        assertFalse(NGSIFieldType.INTEGER.isValueValid("a-text"));
        assertFalse(NGSIFieldType.INTEGER.isValueValid(null));
    }

    @Test
    @DisplayName("is value valid | when type is DATE")
    void isValueValidDateTIme() {
        assertFalse(NGSIFieldType.DATE.isValueValid("a-text"));
        assertFalse(NGSIFieldType.DATE.isValueValid(10));
        assertFalse(NGSIFieldType.DATE.isValueValid(null));
        assertFalse(NGSIFieldType.DATE.isValueValid("2021-05-22T16:53:32.0343"));

        assertTrue(NGSIFieldType.DATE.isValueValid("2021-05-22T16:53:32"));
        assertTrue(NGSIFieldType.DATE.isValueValid("2019-02-14T20:16:01"));
    }
}