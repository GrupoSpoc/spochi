package com.spochi.repository.fiware.ngsi;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class NGSICommonFieldsTest {

    @Test
    void getValue() {
        assertEquals("id", NGSICommonFields.ID.getName());
        assertEquals("type", NGSICommonFields.TYPE.getName());
        assertEquals("value", NGSICommonFields.VALUE.getName());
    }

    @Test
    void getType() {
        assertEquals(NGSIFieldType.TEXT, NGSICommonFields.ID.getType());
        assertEquals(NGSIFieldType.TEXT, NGSICommonFields.TYPE.getType());
        assertEquals(NGSIFieldType.TEXT, NGSICommonFields.VALUE.getType());
    }
}