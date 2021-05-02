package com.spochi.dto;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DTOComparisonUtilTest {

    @Test
    void nullOrEquals() {
        assertTrue(DTOComparisonUtil.nullOrEquals("texto", "texto"));
        assertTrue(DTOComparisonUtil.nullOrEquals(10, 10));
        assertTrue(DTOComparisonUtil.nullOrEquals(null, null));

        assertFalse(DTOComparisonUtil.nullOrEquals("texto", "text"));
        assertFalse(DTOComparisonUtil.nullOrEquals(10, 5));

        assertFalse(DTOComparisonUtil.nullOrEquals(null, "texto"));
        assertFalse(DTOComparisonUtil.nullOrEquals(10, null));
    }
}