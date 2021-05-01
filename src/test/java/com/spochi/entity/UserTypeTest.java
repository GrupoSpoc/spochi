package com.spochi.entity;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UserTypeTest {

    @Test
    void getId() {
        assertEquals(1, UserType.PERSON.getId());
        assertEquals(2, UserType.ORGANIZATION.getId());
        assertEquals(3, UserType.ADMIN.getId());

    }

    @Test
    void fromIdOrElseThrowOK() {
        assertEquals(UserType.PERSON, UserType.fromIdOrElseThrow(1));
        assertEquals(UserType.ORGANIZATION, UserType.fromIdOrElseThrow(2));
        assertEquals(UserType.ADMIN, UserType.fromIdOrElseThrow(3));

    }

    @Test
    void fromIdOrElseThrowException() {
        final int invalidId = -1;
        final UserType.UserTypeNotFoundException exception = assertThrows(UserType.UserTypeNotFoundException.class, () -> UserType.fromIdOrElseThrow(invalidId));
        assertEquals("No UserType with id [" + invalidId + "] present", exception.getMessage());
    }
}