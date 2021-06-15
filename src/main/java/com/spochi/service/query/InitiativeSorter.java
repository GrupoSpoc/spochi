package com.spochi.service.query;

import com.spochi.controller.exception.BadRequestException;
import com.spochi.entity.Initiative;
import lombok.Getter;

import java.util.Arrays;
import java.util.Comparator;

@Getter
public enum InitiativeSorter {
    DEFAULT_COMPARATOR(Integer.MIN_VALUE, (i1, i2) -> 0),
    DATE_DESC (1, Comparator.comparing(Initiative::getDate).reversed()),
    DATE_ASC (2, Comparator.comparing(Initiative::getDate));

    private final int id;
    private final Comparator<Initiative> comparator;

    InitiativeSorter(int id, Comparator<Initiative> comparator) {
        this.id = id;
        this.comparator = comparator;
    }

    public static InitiativeSorter fromIdOrElseThrow(int id) {
        return Arrays.stream(values())
                .filter(v -> v.id == id)
                .findFirst()
                .orElseThrow(() -> new InitiativeSorterNotFoundException(id));
    }

    public static class InitiativeSorterNotFoundException extends BadRequestException {
        private InitiativeSorterNotFoundException(int id) {
            super(String.format("No InitiativeSorter with id [%s] present", id));
        }
    }
}
