package com.spochi.dto;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UserResponseDTOTest {
    public static final String NICKNAME = "nickname";
    public static final int TYPE = 1;
    public static final int AMOUNT_INITIATIVES = 10;
    public static final boolean IS_NOT_ADMIN = false;
    public static final boolean IS_ADMIN = true;
    @Test
    public void resposeDTOtest(){
        UserResponseDTO responseDTO = new UserResponseDTO();
        responseDTO.setAmount_of_initiatives(AMOUNT_INITIATIVES);
        responseDTO.setNickname(NICKNAME);
        responseDTO.setType_id(TYPE);
        responseDTO.setAdmin(IS_NOT_ADMIN);

        assertEquals(AMOUNT_INITIATIVES,responseDTO.getAmount_of_initiatives());
        assertEquals(NICKNAME,responseDTO.getNickname());
        assertEquals(TYPE,responseDTO.getType_id());
        assertFalse(responseDTO.isAdmin());


    }


}