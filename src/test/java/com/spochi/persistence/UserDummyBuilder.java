package com.spochi.persistence;

import com.spochi.entity.User;
import com.spochi.entity.UserType;

import java.util.ArrayList;


public class UserDummyBuilder {
    public static final String GOOGLE_ID = "google-id";

    public static User buildWithId() {
        final User user = build();
        user.set_id("user-id");

        return user;
    }

    public static User build() {
        final User user = new User();

        user.setNickname("user");
        user.set_id("user-id");
        user.setGoogleId(GOOGLE_ID);
        user.setTypeId(UserType.ORGANIZATION);
        user.setInitiatives(new ArrayList<>());

        return user;
    }
}
