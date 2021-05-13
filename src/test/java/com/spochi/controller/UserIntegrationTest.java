package com.spochi.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.spochi.dto.UserResponseDTO;
import com.spochi.entity.User;
import com.spochi.persistence.UserDummyBuilder;
import com.spochi.repository.UserRepository;
import com.spochi.service.auth.JwtUtil;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static com.spochi.auth.JwtFilter.AUTHORIZATION_HEADER;
import static com.spochi.auth.JwtFilter.BEARER_SUFFIX;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles(profiles = {"disable-firebase", "disable-jwt-filter"})
public class UserIntegrationTest {
    @Autowired
    MockMvc mvc;

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    UserRepository repository;

    @MockBean
    JwtUtil jwtUtil;

    private static final String PATH = "/user";

    @Test
    @DisplayName("get user | when user is found | ok")
    void getUserFoundOk() throws Exception {
        final String uid = "uid";
        final String jwt = "jwt";

        final User user = UserDummyBuilder.build(uid);

        repository.save(user);

        when(jwtUtil.extractUid(jwt)).thenReturn(uid);

        final MvcResult result = mvc.perform(get(PATH)
                .header(AUTHORIZATION_HEADER, BEARER_SUFFIX + jwt))
                .andDo(print())
                .andExpect(status().is(HttpStatus.OK.value()))
                .andReturn();

        final UserResponseDTO actualResult = objectMapper.readValue(result.getResponse().getContentAsString(), UserResponseDTO.class);

        assertNotNull(actualResult);
        assertEquals(user.toDTO(), actualResult);
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
}
