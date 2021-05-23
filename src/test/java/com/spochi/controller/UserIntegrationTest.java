package com.spochi.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.spochi.dto.UserRequestDTO;
import com.spochi.dto.UserResponseDTO;
import com.spochi.entity.User;
import com.spochi.persistence.UserDummyBuilder;
import com.spochi.MongoUserRepository;
import com.spochi.service.UserServiceException;
import com.spochi.service.auth.JwtUtil;
import net.minidev.json.JSONValue;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static com.spochi.auth.JwtFilter.AUTHORIZATION_HEADER;
import static com.spochi.auth.JwtFilter.BEARER_SUFFIX;
import static com.spochi.controller.HttpStatus.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles(profiles = {"disable-firebase", "disable-jwt-filter"})
@TestPropertySource(properties="fiware-repositories=false")
public class UserIntegrationTest {
    @Autowired
    MockMvc mvc;

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    MongoUserRepository repository;

    @MockBean
    JwtUtil jwtUtil;

    @AfterEach
    void clearDB() {
        repository.deleteAll();
    }

    private static final String PATH = "/user";

    @Test
    @DisplayName("get user | when user is found | ok")
    void getUserFoundOk() throws Exception {
        final String uid = "uid";
        final String jwt = "jwt";

        final User user = UserDummyBuilder.build(uid);

        final UserResponseDTO expectedResult = new UserResponseDTO();
        expectedResult.setType_id(user.getTypeId());
        expectedResult.setAmount_of_initiatives(0);
        expectedResult.setNickname(user.getNickname());
        expectedResult.setAdmin(false);

        repository.create(user);

        when(jwtUtil.extractUid(jwt)).thenReturn(uid);

        final MvcResult result = mvc.perform(get(PATH)
                .header(AUTHORIZATION_HEADER, BEARER_SUFFIX + jwt))
                .andDo(print())
                .andExpect(status().is(HttpStatus.OK.value()))
                .andReturn();

        final UserResponseDTO actualResult = objectMapper.readValue(result.getResponse().getContentAsString(), UserResponseDTO.class);

        assertNotNull(actualResult);
        assertEquals(expectedResult, actualResult);
    }

    @Test
    @DisplayName("get user | when user is not found | 404 not found")
    void getUserNotFound() throws Exception {
        final String uid = "uid";
        final String jwt = "jwt";

        when(jwtUtil.extractUid(jwt)).thenReturn(uid);

        mvc.perform(get(PATH)
                .header(AUTHORIZATION_HEADER, BEARER_SUFFIX + jwt))
                .andDo(print())
                .andExpect(status().is(HttpStatus.NOT_FOUND.value()))
                .andReturn();
    }

    @Test
    @DisplayName("create | ok")
    void createOk() throws Exception {
        final String uid = "uid";
        final String jwt = "jwt";

        final UserRequestDTO request = new UserRequestDTO();
        request.setNickname("nickname");
        request.setType_id(1);

        when(jwtUtil.extractUid(jwt)).thenReturn(uid);

        final MvcResult result = mvc.perform(post(PATH)
                .contentType(MediaType.APPLICATION_JSON).content(JSONValue.toJSONString(request))
                .header(AUTHORIZATION_HEADER, BEARER_SUFFIX + jwt))
                .andDo(print())
                .andExpect(status().is(HttpStatus.OK.value()))
                .andReturn();

        final UserResponseDTO response = objectMapper.readValue(result.getResponse().getContentAsString(), UserResponseDTO.class);

        final User createdUser = repository.findByUid(uid).orElse(null);
        final UserResponseDTO expectedResult = new UserResponseDTO();
        expectedResult.setType_id(createdUser.getTypeId());
        expectedResult.setAmount_of_initiatives(0);
        expectedResult.setNickname(createdUser.getNickname());
        expectedResult.setAdmin(false);

        assertAll("Expected result",
                () -> assertNotNull(createdUser),
                () -> assertEquals(request.getNickname(), createdUser.getNickname()),
                () -> assertEquals(request.getType_id(), createdUser.getTypeId()),
                () -> assertEquals(uid, createdUser.getUid()),
                () -> assertEquals(expectedResult, response)
        );
    }

    @Test
    @DisplayName("create | when nickname is null | bad request")
    void createNicknameNull() throws Exception {
        final String uid = "uid";
        final String jwt = "jwt";

        final UserRequestDTO request = new UserRequestDTO();
        request.setType_id(1);

        when(jwtUtil.extractUid(jwt)).thenReturn(uid);

        final MvcResult result = mvc.perform(post(PATH)
                .contentType(MediaType.APPLICATION_JSON).content(JSONValue.toJSONString(request))
                .header(AUTHORIZATION_HEADER, BEARER_SUFFIX + jwt))
                .andDo(print())
                .andExpect(status().is(BAD_REQUEST.getCode()))
                .andReturn();

        assertTrue(result.getResolvedException() instanceof UserServiceException);
        assertEquals("nickname cannot be null or empty", result.getResponse().getContentAsString());
    }

    @Test
    @DisplayName("create | when uid is already used | bad request")
    void createUidAlreadyUsed() throws Exception {
        final String uid = "uid";
        final String jwt = "jwt";

        repository.create(User.builder().nickname("nickname").typeId(1).uid(uid).build());

        final UserRequestDTO request = new UserRequestDTO();
        request.setType_id(1);

        when(jwtUtil.extractUid(jwt)).thenReturn(uid);

        final MvcResult result = mvc.perform(post(PATH)
                .contentType(MediaType.APPLICATION_JSON).content(JSONValue.toJSONString(request))
                .header(AUTHORIZATION_HEADER, BEARER_SUFFIX + jwt))
                .andDo(print())
                .andExpect(status().is(BAD_REQUEST.getCode()))
                .andReturn();

        assertTrue(result.getResolvedException() instanceof UserServiceException);
        assertEquals("this google account already has a user", result.getResponse().getContentAsString());
    }
}
