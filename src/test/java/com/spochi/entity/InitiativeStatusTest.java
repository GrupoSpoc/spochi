package com.spochi.entity;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class InitiativeStatusTest {

    @Test
    void getId() {
        assertEquals(1, InitiativeStatus.PENDING.getId());
        assertEquals(2, InitiativeStatus.APPROVED.getId());
        assertEquals(3, InitiativeStatus.REJECTED.getId());

    }

    @Test
    void fromIdOrElseThrowOK() {
        assertEquals(InitiativeStatus.PENDING, InitiativeStatus.fromIdOrElseThrow(1));
        assertEquals(InitiativeStatus.APPROVED, InitiativeStatus.fromIdOrElseThrow(2));
        assertEquals(InitiativeStatus.REJECTED, InitiativeStatus.fromIdOrElseThrow(3));

    }

    @Test
    void fromIdOrElseThrowException() {
        final int invalidId = -1;
        final InitiativeStatus.InitiativeStatusNotFoundException exception = assertThrows(InitiativeStatus.InitiativeStatusNotFoundException.class, () -> InitiativeStatus.fromIdOrElseThrow(invalidId));
        assertEquals("No InitiativeStatus with id [" + invalidId + "] present", exception.getMessage());
    }
}