package com.spochi.entity;

import com.spochi.repository.fiware.ngsi.*;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "users")
public class User implements NGSISerializable {
    public static NGSIEntityType NGSIType = () -> "User";

    @Id
    private String id;

    @Field(name = "uid")
    private String uid;

    private String nickname;

    @Field(name = "type_id")
    private int typeId;

    public UserType getType() {
        return UserType.fromIdOrElseThrow(this.typeId);
    }

    public void setTypeId(UserType type) {
        this.typeId = type.getId();
    }

    public void setTypeId(int typeId) {
        this.typeId = typeId;
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
        final String id = json.getId();
        final String uid = json.getString(Fields.UID.label());
        final String nickname = json.getString(Fields.NICKNAME.label());
        final UserType type = UserType.fromIdOrElseThrow(json.getInt(Fields.TYPE_ID.label()));

        return new User(id, uid, nickname, type.getId());
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


