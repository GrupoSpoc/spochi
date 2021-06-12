package com.spochi.controller;

import com.spochi.dto.InitiativeResponseDTO;
import com.spochi.entity.Initiative;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class InitiativeTestUtil {
    private static final List<Initiative> initiatives = new ArrayList<>();
    public static final String UNIQUE_ID = "unique_id";


    static {
        final Initiative.InitiativeBuilder builder = Initiative.builder();
        final LocalDateTime epochDate = LocalDateTime.ofInstant(Instant.EPOCH, ZoneId.of("UTC"));

        builder.description("description");
        builder.statusId(2);
        builder.userId("user");
        builder.image("image");
        builder.nickname("author");
        builder.date(epochDate);

        initiatives.add(builder.date(epochDate.plusYears(10)).build());
        initiatives.add(builder.statusId(1).date(epochDate.plusYears(20)).build());
        initiatives.add(builder.statusId(2).date(epochDate.plusYears(30)).build());
        initiatives.add(builder.statusId(3).date(epochDate).userId(UNIQUE_ID).build());
        initiatives.add(builder.statusId(3).date(epochDate).userId(UNIQUE_ID).build());
    }

    public static List<InitiativeResponseDTO> getAllAsDTOs() {
        return initiatives.stream().map(Initiative::toDTO).collect(Collectors.toList());
    }

    public static List<Initiative> getInitiatives() {
        return initiatives;
    }
}
