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

    static {
        final Initiative.InitiativeBuilder builder = Initiative.builder();
        final LocalDateTime epochDate = LocalDateTime.ofInstant(Instant.EPOCH, ZoneId.of("UTC"));

        builder.description("description");
        builder.statusId(0);
        builder.userId("user");
        builder.image("image");
        builder.author("author");
        builder.date(epochDate);

        initiatives.add(builder.build());
        initiatives.add(builder.statusId(1).build());
        initiatives.add(builder.statusId(2).date(epochDate.plusYears(30)).build());
    }

    public static List<InitiativeResponseDTO> getAllAsDTOs() {
        return initiatives.stream().map(Initiative::toDTO).collect(Collectors.toList());
    }

    public static List<Initiative> getInitiatives() {
        return initiatives;
    }
}
