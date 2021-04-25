package com.spochi.entity;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.LocalDate;

@Getter
@Setter
@Document(collection = "initiatives")
public class Initiative {
    @Id
    private String _id;

    private String title;
    private String description;
    private String image;
    private String author;
    private LocalDate date;

    @Field(name = "user_id")
    private String userId;

    @Field(name = "type_id")
    private int typeId;

    @Field(name = "status_id")
    private int statusId;
}
