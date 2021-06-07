package com.spochi.dto;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AdminRequestDTOTest {
    @Test
    void AdminRequestDtoWorks(){
        final String pass = "test";
        final String uid = "uid";

        AdminRequestDTO adminRequestDTO = new AdminRequestDTO();
        adminRequestDTO.setPassword(pass);
        adminRequestDTO.setUid(uid);

        assertEquals(pass,adminRequestDTO.getPassword());
        assertEquals(uid,adminRequestDTO.getUid());
    }

}