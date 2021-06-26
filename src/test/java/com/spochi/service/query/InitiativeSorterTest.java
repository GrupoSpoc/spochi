package com.spochi.service.query;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class InitiativeSorterTest {

    @Test
    void getId() {
        assertEquals(1, InitiativeSorter.DATE_DESC.getId());
        assertEquals(2, InitiativeSorter.DATE_ASC.getId());
        assertEquals(Integer.MIN_VALUE, InitiativeSorter.DEFAULT_COMPARATOR.getId());
    }

    @Test
    void fromIdOrElseThrowOK() {
        assertEquals(InitiativeSorter.DATE_DESC, InitiativeSorter.fromIdOrElseThrow(1));
        assertEquals(InitiativeSorter.DATE_ASC, InitiativeSorter.fromIdOrElseThrow(2));
        assertEquals(InitiativeSorter.DEFAULT_COMPARATOR, InitiativeSorter.fromIdOrElseThrow(Integer.MIN_VALUE));
    }

    @Test
    void fromIdOrElseThrowException() {
        final int invalidId = -1;
        final InitiativeSorter.InitiativeSorterNotFoundException exception = assertThrows(InitiativeSorter.InitiativeSorterNotFoundException.class, () -> InitiativeSorter.fromIdOrElseThrow(invalidId));
        assertEquals("No InitiativeSorter with id [" + invalidId + "] present", exception.getMessage());
    }
}