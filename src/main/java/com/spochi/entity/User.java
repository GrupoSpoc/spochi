package com.spochi.entity;

import com.spochi.dto.UserResponseDTO;
import com.spochi.repository.fiware.ngsi.*;
import lombok.*;
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
    public static NGSIEntityType NGSIType = () -> "User";

    @Id
    private String _id;

    @Field(name = "uid")
    private String uid;

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
    public NGSIJson toNGSIJson(String id) {
        final NGSIJson json = new NGSIJson();

        json.setId(id);
        json.setType(NGSIType);
        json.addAttribute(Fields.UID, this.uid);
        json.addAttribute(Fields.NICKNAME, this.nickname);
        json.addAttribute(Fields.TYPE_ID, this.typeId);

        return json;
    }

    public static User fromNGSIJson(NGSIJson json) {
        final String id = json.getId(); // todo renombrar
        final String uid = json.getString(Fields.UID.label());
        final String nickname = json.getString(Fields.NICKNAME.label());
        final UserType type = UserType.fromIdOrElseThrow(json.getInt(Fields.TYPE_ID.label()));

        return new User(id, uid, nickname, type.getId(), Collections.emptyList());
    }

    public enum Fields implements NGSIField {
        UID("uid", NGSIFieldType.TEXT),
        NICKNAME("nickname", NGSIFieldType.TEXT),
        TYPE_ID("type_id", NGSIFieldType.INTEGER);

        private final String name;
        private final NGSIFieldType type;

        Fields(String name, NGSIFieldType type) {
            this.name = name;
            this.type = type;
        }

        @Override
        public String label() {
            return this.name;
        }

        @Override
        public NGSIFieldType type() {
            return this.type;
        }
    }

}


