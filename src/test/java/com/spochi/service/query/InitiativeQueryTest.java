package com.spochi.service.query;

import com.spochi.entity.InitiativeStatus;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("InitiativeQuery test | Unit")
class InitiativeQueryTest {

    @Test
    void withSorter() {
        final InitiativeQuery query = new InitiativeQuery();

        query.withSorter(null);
        assertNull(query.getSorter());

        query.withSorter(InitiativeSorter.DATE_DESC.getId());
        assertEquals(InitiativeSorter.DATE_DESC, query.getSorter());

        assertThrows(InitiativeSorter.InitiativeSorterNotFoundException.class, () -> query.withSorter(-4));
    }

    @Test
    void withStatuses() {
        final InitiativeQuery query = new InitiativeQuery();

        query.withStatuses(null);
        assertNull(query.getStatuses());

        query.withStatuses(new Integer[]{InitiativeStatus.APPROVED.getId()});
        assertEquals(1, query.getStatuses().size());
        assertEquals(InitiativeStatus.APPROVED, query.getStatuses().get(0));

        query.withStatuses(new Integer[]{InitiativeStatus.APPROVED.getId(), InitiativeStatus.PENDING.getId()});
        assertEquals(2, query.getStatuses().size());
        assertEquals(InitiativeStatus.APPROVED, query.getStatuses().get(0));
        assertEquals(InitiativeStatus.PENDING, query.getStatuses().get(1));

        assertThrows(InitiativeStatus.InitiativeStatusNotFoundException.class, () -> query.withStatuses(new Integer[]{-4}));
    }

    @Test
    void withUserId() {
        final InitiativeQuery query = new InitiativeQuery();

        query.withUserId(null);
        assertNull(query.getUserId());

        query.withUserId("user-id");
        assertEquals("user-id", query.getUserId());
    }

    @Test
    void withDateTop() {
        final InitiativeQuery query = new InitiativeQuery();

        final LocalDateTime dateTop = LocalDateTime.now();

        query.withDateTop(null);
        assertNull(query.getDateTop());

        query.withDateTop(dateTop.toString());
        assertEquals(dateTop, query.getDateTop());

        assertThrows(DateTimeParseException.class, () -> query.withDateTop("not-a-date"));
    }

    @Test
    void withLimit() {
        final InitiativeQuery query = new InitiativeQuery();

        query.withLimit(null);
        assertNull(query.getLimit());

        query.withLimit(0);
        assertNull(query.getLimit());

        query.withLimit(5);
        assertEquals(5, query.getLimit());
    }

    @Test
    void withOffset() {
        final InitiativeQuery query = new InitiativeQuery();

        query.withOffset(null);
        assertNull(query.getOffset());

        query.withOffset(-2);
        assertNull(query.getOffset());

        query.withOffset(2);
        assertEquals(2, query.getOffset());
    }
}