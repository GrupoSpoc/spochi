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
    private String author;
    private LocalDateTime date;

    @Field(name = "user_id")
    private String userId;

    @Field(name = "status_id")
    private int statusId;

    public InitiativeResponseDTO toDTO() {
        final InitiativeResponseDTO dto = new InitiativeResponseDTO();

        dto.set_id(this._id);
        dto.setDate(this.date.toString());
        dto.setDescription(this.description);
        dto.setNickname(this.author);
        dto.setStatus_id(this.statusId);
        dto.setImage(this.image);

        return dto;
    }
}
