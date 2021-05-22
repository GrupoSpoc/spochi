package com.spochi.entity;

import com.spochi.dto.UserResponseDTO;
import com.spochi.service.fiware.ngsi.NGSIField;
import com.spochi.service.fiware.ngsi.NGSISerializable;
import lombok.*;
import lombok.experimental.FieldNameConstants;
import org.json.JSONObject;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "users")
public class User implements NGSISerializable {
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

    public void setTypeId(int typeId) {
        this.typeId = typeId;
    }

    public UserResponseDTO toDTO() {
        final UserResponseDTO dto = new UserResponseDTO();

        dto.setNickname(this.nickname);
        dto.setAdmin(this.getType() == UserType.ADMIN);
        dto.setType_id(this.getTypeId());
        dto.setAmount_of_initiatives(this.initiatives != null ? this.initiatives.size() : 0);

        return dto;
    }

    @Override
    public String toNGSIJson(String id) {
        return this.toString();
    }

    public static User fromNGSIJson(JSONObject json) {
        final String id = json.getString(Fields.ID.getValue()); // todo renombrar
        final String uid = json.getString(Fields.UID.getValue());
        final String nickname = json.getString(Fields.NICKNAME.getValue());
        final UserType type = UserType.fromIdOrElseThrow(json.getInt(Fields.TYPE_ID.getValue()));

        return new User(id, uid, nickname, type.getId(), Collections.emptyList());
    }

    public enum Fields implements NGSIField {
        ID("id"),
        UID("uid"),
        NICKNAME("nickname"),
        TYPE_ID("type_id");

        private final String value;

        Fields(String value) {
            this.value = value;
        }

        @Override
        public String getValue() {
            return this.value;
        }
    }
}


