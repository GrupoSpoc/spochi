package com.spochi.entity;

import com.spochi.dto.InitiativeResponseDTO;
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
public class Initiative {
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

    public InitiativeResponseDTO toDTO(String user_id) {
        final InitiativeResponseDTO dto = new InitiativeResponseDTO();

        dto.set_id(this._id);
        dto.setDate(this.date.toString());
        dto.setDescription(this.description);
        dto.setNickname(this.nickname);
        dto.setStatus_id(this.statusId);
        dto.setImage(this.image);
        dto.setFrom_current_user(user_id.equals(this.userId));

        return dto;
    }
}
