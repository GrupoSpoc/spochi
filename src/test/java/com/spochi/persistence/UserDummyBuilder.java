package com.spochi.persistence;

import com.spochi.entity.User;
import com.spochi.entity.UserType;


public class UserDummyBuilder {
    public static final String UID = "uid";
    public static final String FIWARE_ID = "urn:ngsi-ld:User:001";


    public static User buildWithId() {
        final User user = build();
        user.setId("user-id");

        return user;
    }

    public static User build() {
        return build(UID);
    }

    public static User build(String uid) {
        final User user = new User();

        user.setNickname("user");
        user.setUid(uid);
        user.setTypeId(UserType.ORGANIZATION);

        return user;
    }
}
