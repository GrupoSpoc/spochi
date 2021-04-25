package com.spochi.entity;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "rewards")
public class Reward {
    @Id
    private String _id;

    @Field(name = "user_type_id")
    private int userTypeId;

    @Field(name = "initiative_type_id")
    private int initiativeTypeId;

    private int points;

    public UserType getUserType() {
        return UserType.fromIdOrElseThrow(this.userTypeId);
    }

    public void setUserType(UserType userType) {
        this.userTypeId = userType.getId();
    }
}
