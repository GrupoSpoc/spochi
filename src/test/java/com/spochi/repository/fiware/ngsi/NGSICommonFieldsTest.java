package com.spochi.repository.fiware.ngsi;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class NGSICommonFieldsTest {

    @Test
    void getLabel() {
        assertEquals("id", NGSICommonFields.ID.label());
        assertEquals("type", NGSICommonFields.TYPE.label());
        assertEquals("value", NGSICommonFields.VALUE.label());
    }

    @Test
    void getType() {
        assertEquals(NGSIFieldType.TEXT, NGSICommonFields.ID.type());
        assertEquals(NGSIFieldType.TEXT, NGSICommonFields.TYPE.type());
        assertEquals(NGSIFieldType.TEXT, NGSICommonFields.VALUE.type());
    }
}