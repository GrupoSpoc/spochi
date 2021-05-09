package com.spochi.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.firebase.auth.FirebaseAuthException;
import com.spochi.service.auth.JwtUtil;
import com.spochi.auth.TokenInfo;
import com.spochi.auth.AuthorizationException;
import com.spochi.service.auth.firebase.FirebaseTokenProvider;
import com.spochi.controller.exception.BadRequestException;
import com.spochi.dto.UserResponseDTO;
import com.spochi.service.UserService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles(profiles = {"disable-firebase", "disable-jwt-filter"})
class AuthenticateControllerTest {
    @Autowired
    MockMvc mvc;

    @Autowired
    ObjectMapper objectMapper;

    @MockBean
    FirebaseTokenProvider tokenProvider;

    @MockBean
    JwtUtil jwtUtil;

    @MockBean
    UserService userService;

    private static final String PATH = "/authenticate";

    @Test
    @DisplayName("authenticate | when given token is null | should throw BadRequestException")
    void authenticateNullToken() throws Exception {
        final MvcResult result = mvc.perform(post(PATH))
                .andDo(print())
                .andExpect(status().is(HttpStatus.BAD_REQUEST.value()))
                .andReturn();

        assertTrue(result.getResolvedException() instanceof BadRequestException);
        assertEquals("Firebase token is empty", result.getResponse().getContentAsString());
    }

    @Test
    @DisplayName("authenticate | when given token is empty | should throw BadRequestException")
    void authenticateEmptyToken() throws Exception {
        final MvcResult result = mvc.perform(post(PATH)
                .contentType(MediaType.TEXT_PLAIN).content(""))
                .andDo(print())
                .andExpect(status().is(HttpStatus.BAD_REQUEST.value()))
                .andReturn();

        assertTrue(result.getResolvedException() instanceof BadRequestException);
        assertEquals("Firebase token is empty", result.getResponse().getContentAsString());
    }

    @Test
    @DisplayName("authenticate | given token | when provider fails with FirebaseAuthException | should throw FirebaseAuthorizationException")
    void authenticateProviderFails() throws Exception {
        when(tokenProvider.getUidFromToken(anyString())).thenThrow(new FirebaseAuthException("error-code", "error-message"));

        final MvcResult result = mvc.perform(post(PATH)
                .contentType(MediaType.TEXT_PLAIN).content("token"))
                .andDo(print())
                .andExpect(status().is(HttpStatus.NOT_ACCEPTABLE.value()))
                .andReturn();

        assertTrue(result.getResolvedException() instanceof AuthorizationException);
        assertEquals("Invalid or expired token", result.getResponse().getContentAsString());
    }

    @Test
    @DisplayName("authenticate | given token | when provider returns uid | ok | should return TokenInfo")
    void authenticateOk() throws Exception {
        final UserResponseDTO expectedDTO = new UserResponseDTO();

        expectedDTO.setAmount_of_initiatives(3);
        expectedDTO.setAdmin(false);
        expectedDTO.setNickname("user");
        expectedDTO.setType_id(1);

        when(userService.findByGoogleId(anyString())).thenReturn(expectedDTO);
        when(tokenProvider.getUidFromToken(anyString())).thenReturn("user-uid");
        when(jwtUtil.generateToken(anyString())).thenReturn("jwt");

        final MvcResult result = mvc.perform(post(PATH)
                .contentType(MediaType.TEXT_PLAIN).content("token"))
                .andDo(print())
                .andExpect(status().is(HttpStatus.OK.value()))
                .andReturn();

        final TokenInfo tokenInfo = objectMapper.readValue(result.getResponse().getContentAsString(), TokenInfo.class);

        final UserResponseDTO actualDTO = tokenInfo.getUser();
        assertAll("Expected userDTO",
                () -> assertEquals(expectedDTO.getNickname(), actualDTO.getNickname()),
                () -> assertEquals(expectedDTO.getType_id(), actualDTO.getType_id()),
                () -> assertEquals(expectedDTO.getAmount_of_initiatives(), actualDTO.getAmount_of_initiatives()),
                () -> assertFalse(actualDTO.isAdmin()));
        assertEquals("jwt", tokenInfo.getJwt());
    }

}