package com.spochi.util;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("DateUtil test | Unit")
class DateUtilTest {

    @Test
    @DisplayName("dateToMilliUTC | ok")
    void dateToMilliUTC() {
        assertEquals(LocalDateTime.parse("2021-04-12T16:27:52.038"), DateUtil.milliToDateUTC(1618244872038L));
        assertEquals(LocalDateTime.parse("2019-01-23T11:35:21.021"), DateUtil.milliToDateUTC(1548243321021L));

    }

    @Test
    @DisplayName("milliToDateUTC | ok")
    void milliToDateUTC() {
        assertEquals(1648242429221L, DateUtil.dateToMilliUTC(LocalDateTime.parse("2022-03-25T21:07:09.221")));
        assertEquals(1623398123424L, DateUtil.dateToMilliUTC(LocalDateTime.parse("2021-06-11T07:55:23.424")));
    }
}