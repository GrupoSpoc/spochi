package com.spochi.persistence;

import com.spochi.entity.Initiative;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.UUID;

public class InitiativeDummyBuilder {
    public static Initiative build() {
        final Initiative initiative = new Initiative();
        initiative.set_id(UUID.randomUUID().toString());
        initiative.setAuthor("author");
        initiative.setDate(LocalDateTime.ofInstant(Instant.EPOCH, ZoneId.of("UTC")));
        initiative.setDescription("description");
        initiative.setTypeId(1);
        initiative.setStatusId(2);
        initiative.setImage("image");
        initiative.setUserId("user-id");
        return initiative;
    }
}
