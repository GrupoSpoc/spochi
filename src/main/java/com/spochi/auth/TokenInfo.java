package com.spochi.auth;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.spochi.dto.UserResponseDTO;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class TokenInfo {
    private String jwt;
    private UserResponseDTO user;
}
