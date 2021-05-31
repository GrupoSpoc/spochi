package com.spochi.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.spochi.dto.InitiativeResponseDTO;
import com.spochi.dto.UserResponseDTO;
import com.spochi.entity.Initiative;
import com.spochi.repository.InitiativeRepository;
import com.spochi.service.InitiativeService;
import com.spochi.service.query.InitiativeSorter;
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

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.spochi.auth.JwtFilter.AUTHORIZATION_HEADER;
import static com.spochi.auth.JwtFilter.BEARER_SUFFIX;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles(profiles = {"disable-firebase","disable-jwt-filter"})
public class InitiativeControllerTest {

    @Autowired
    MockMvc mvc;

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    InitiativeRepository repository;

    @MockBean
    InitiativeService service;

    @Test
    @DisplayName("approveInitiativeOk | having a proper initiative | change status to Approved")
    void approveInitiativeOk() throws Exception {

        final Initiative expectedInitiative = new Initiative();
        expectedInitiative.set_id("2");
        expectedInitiative.setUserId("UserId");
        expectedInitiative.setNickname("some Nickname");
        expectedInitiative.setDescription("Description");
        expectedInitiative.setDate(LocalDateTime.now().withNano(0));
        expectedInitiative.setStatusId(1);
        expectedInitiative.setImage("imageData");

        final InitiativeResponseDTO expectedDTO = expectedInitiative.toDTO("someID");

        when(service.approveInitiative(anyString())).thenReturn(expectedDTO);

        final MvcResult result = mvc.perform(post("/initiative/approve/{id}", expectedInitiative.get_id())
                .header(AUTHORIZATION_HEADER, BEARER_SUFFIX + "jwt" ))
                .andExpect(status().is(HttpStatus.OK.value()))
                .andReturn();

        final InitiativeResponseDTO actualResult = objectMapper.readValue(result.getResponse().getContentAsString(), InitiativeResponseDTO.class);

        assertNotNull(actualResult);
    }


}
