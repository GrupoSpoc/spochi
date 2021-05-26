package com.spochi.entity;

import com.spochi.dto.InitiativeResponseDTO;
import com.spochi.repository.fiware.ngsi.*;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "initiatives")
public class Initiative implements NGSISerializable {

    public static NGSIEntityType NGSIType = () -> "Initiative";

    @Id
    private String _id;
    private String description;
    private String image;
    private String nickname;
    private LocalDateTime date;

    @Field(name = "user_id")
    private String userId;

    @Field(name = "status_id")
    private int statusId;

    public Initiative(String description, String image, String nickname, LocalDateTime date, String userId, int statusId) {
        this.description = description;
        this.image = image;
        this.nickname = nickname;
        this.date = date;
        this.userId = userId;
        this.statusId = statusId;
    }

    public InitiativeResponseDTO toDTO() {
        final InitiativeResponseDTO dto = new InitiativeResponseDTO();

        dto.set_id(this._id);
        dto.setDate(this.date.toString());
        dto.setDescription(this.description);
        dto.setNickname(this.nickname);
        dto.setStatus_id(this.statusId);
        dto.setImage(this.image);

        return dto;
    }

    @Override
    public NGSIJson toNGSIJson(String id) {
        NGSIJson json = new NGSIJson();

        json.setId(id);
        json.setType(NGSIType);

        json.addAttribute(Fields.DESCRIPTION, this.description);
        json.addAttribute(Fields.IMAGE, this.image);
        json.addAttribute(Fields.NICKNAME, this.nickname);
        json.addAttribute(Fields.DATE, this.date);
        json.addAttribute(Fields.USER_ID, this.userId);
        json.addAttribute(Fields.STATUS_ID, this.statusId);

        return json;
    }


    public enum Fields implements NGSIField {
        ID("id", NGSIFieldType.INTEGER),
        DESCRIPTION("description", NGSIFieldType.TEXT),
        IMAGE("image", NGSIFieldType.TEXT),
        NICKNAME("nickname", NGSIFieldType.TEXT),
        DATE("date", NGSIFieldType.DATE),
        USER_ID("refUser", NGSIFieldType.TEXT),
        STATUS_ID("statusId", NGSIFieldType.INTEGER);

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
