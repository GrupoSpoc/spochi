package com.spochi.util;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class AssertUtils {

    public static void assertException (Class<? extends Exception> clazz, Runnable runnable, String message) {
        final Exception exception = assertThrows(clazz, runnable::run);
        assertEquals(message, exception.getMessage());
    }
}
