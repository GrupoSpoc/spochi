package com.spochi.auth;

import com.spochi.dto.UserResponseDTO;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TokenInfoTest {
    
    @Test
    void testInstantiation() {
        final UserResponseDTO userDTO = new UserResponseDTO();

        userDTO.setAdmin(false);
        userDTO.setNickname("user");
        userDTO.setType_id(1);

        final TokenInfo tokenInfo = new TokenInfo("jwt", userDTO);

        final UserResponseDTO actualDTO = tokenInfo.getUser();
        assertAll("Expected userDTO",
                () -> assertEquals(userDTO.getNickname(), actualDTO.getNickname()),
                () -> assertEquals(userDTO.getType_id(), actualDTO.getType_id()),
                () -> assertFalse(actualDTO.isAdmin()));
        assertEquals("jwt", tokenInfo.getJwt());
    }

}