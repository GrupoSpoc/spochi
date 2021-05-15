package com.spochi.entity;

import com.spochi.dto.UserResponseDTO;
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
    private String googleId;

    private String nickname;

    @Field(name = "type_id")
    private int typeId;

    @DBRef(lazy = true)
    private List<Initiative> initiatives;

    public void addInitiative(Initiative i) {
        if (initiatives == null) {
            initiatives = new ArrayList<>();
        }
        initiatives.add(i);
    }

    public UserType getType() {
        return UserType.fromIdOrElseThrow(this.typeId);
    }

    public void setTypeId(UserType type) {
        this.typeId = type.getId();
    }

    public UserResponseDTO toDTO() {
        final UserResponseDTO dto = new UserResponseDTO();

        dto.setNickname(this.nickname);
        dto.setAdmin(this.getType() == UserType.ADMIN);
        dto.setType_id(this.getTypeId());
        dto.setAmount_of_initiatives(this.initiatives != null ? this.initiatives.size() : 0);

        return dto;
    }
}


