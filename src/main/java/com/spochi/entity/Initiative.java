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

    @Field(name = "type_id")
    private int typeId;

    @Field(name = "status_id")
    private int statusId;

    public InitiativeResponseDTO toDTO() {
        final InitiativeResponseDTO dto = new InitiativeResponseDTO();

        dto.set_id(this.get_id());
        dto.setDate(this.getDate().toString());
        dto.setDescription(this.getDescription());
        dto.setNickname(this.getAuthor());
        dto.setStatus_id(this.getStatusId());
        dto.setImage(this.getImage());

        return dto;
    }
}
