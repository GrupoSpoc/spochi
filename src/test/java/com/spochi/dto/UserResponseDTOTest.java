package com.spochi.dto;

import org.junit.jupiter.api.Test;


import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class UserResponseDTOTest {
    public static final String NICKNAME = "nickname";
    public static final int TYPE = 1;
    public static final int AMOUNT_INITIATIVES = 10;
    public static final boolean IS_NOT_ADMIN = false;
    public  final Map<Integer, Integer> initiativesByStatus = new HashMap<>();

    @Test
    public void responseDTOtest(){
        populateMap();
        UserResponseDTO responseDTO = new UserResponseDTO();
        responseDTO.setAmount_of_initiatives(AMOUNT_INITIATIVES);
        responseDTO.setNickname(NICKNAME);
        responseDTO.setType_id(TYPE);
        responseDTO.setAdmin(IS_NOT_ADMIN);
        responseDTO.setInitiatives_by_status(initiativesByStatus);

        System.out.println(initiativesByStatus);
        assertEquals(AMOUNT_INITIATIVES,responseDTO.getAmount_of_initiatives());
        assertEquals(NICKNAME,responseDTO.getNickname());
        assertEquals(TYPE,responseDTO.getType_id());
        assertFalse(responseDTO.isAdmin());
        assertEquals(initiativesByStatus, responseDTO.getInitiatives_by_status());
    }

    public void populateMap(){
        this.initiativesByStatus.put(1,5);
        this.initiativesByStatus.put(2,2);
        this.initiativesByStatus.put(3,7);
    }
}