package com.spochi.entity;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "users")
public class User {
    @Id
    private String _id;

    @Field(name = "google_id")
    private int googleId;

    private String nickname;

    @Field(name = "type_id")
    private int typeId;

    @DBRef(lazy = true)
    private List<Initiative> initiatives;

    public void addInitiative(Initiative i) {
        final List<Initiative> initiatives = new ArrayList<>(this.initiatives);
        initiatives.add(i);
        setInitiatives(initiatives);
    }


    public UserType getTypeId() {
        return UserType.fromIdOrElseThrow(this.typeId);
    }

    public void setTypeId(UserType type) {
        this.typeId = type.getId();
    }
}

