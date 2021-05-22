package com.spochi.persistence;

import com.spochi.entity.User;
import com.spochi.entity.UserType;

import java.util.ArrayList;


public class UserDummyBuilder {
    public static final String GOOGLE_ID = "google-id";
    public static final String FIWARE_ID = "urn:ngsi-ld:User:001";


    public static User buildWithId() {
        final User user = build();
        user.set_id("user-id");

        return user;
    }

    public static User build() {
        return build(GOOGLE_ID);
    }

    public static User build(String uid) {
        final User user = new User();

        user.setNickname("user");
        user.setGoogleId(uid);
        user.setTypeId(UserType.ORGANIZATION);
        user.setInitiatives(new ArrayList<>());

        return user;
    }
}
