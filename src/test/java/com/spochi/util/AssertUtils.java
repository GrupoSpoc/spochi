package com.spochi.util;

import com.spochi.controller.HttpStatus;
import com.spochi.controller.exception.BadRequestException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class AssertUtils {

    public static <T extends Exception> T assertException(Class<T> clazz, Runnable runnable, String message) {
        final T exception = assertThrows(clazz, runnable::run);
        assertEquals(message, exception.getMessage());
        return exception;
    }

    public static <T extends BadRequestException> void assertBadRequestException(Class<T> clazz, Runnable runnable, String message, HttpStatus status) {
        final T badRequestException = assertException(clazz, runnable, message);
        assertEquals(status, badRequestException.getStatus());
    }
}
