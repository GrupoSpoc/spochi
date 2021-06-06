package com.spochi.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.spochi.controller.exception.BadRequestException;
import com.spochi.controller.handler.ControllerExceptionHandler;
import com.spochi.dto.InitiativeRequestDTO;
import com.spochi.dto.InitiativeResponseDTO;
import com.spochi.entity.Initiative;
import com.spochi.entity.InitiativeStatus;
import com.spochi.repository.InitiativeRepository;
import com.spochi.service.InitiativeService;

import net.minidev.json.JSONValue;
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

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;

import static com.spochi.auth.JwtFilter.AUTHORIZATION_HEADER;
import static com.spochi.auth.JwtFilter.BEARER_SUFFIX;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles(profiles = {"disable-firebase", "disable-jwt-filter"})
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
        expectedInitiative.setStatusId(InitiativeStatus.APPROVED.getId());
        expectedInitiative.setImage("imageData");

        final InitiativeResponseDTO expectedDTO = expectedInitiative.toDTO();

        when(service.approveInitiative(anyString())).thenReturn(expectedDTO);

        final MvcResult result = mvc.perform(post("/initiative/approve/{id}", expectedInitiative.get_id())
                .header(AUTHORIZATION_HEADER, BEARER_SUFFIX + "jwt"))
                .andExpect(status().is(HttpStatus.OK.value()))
                .andReturn();

        final InitiativeResponseDTO actualResult = objectMapper.readValue(result.getResponse().getContentAsString(), InitiativeResponseDTO.class);

        assertNotNull(actualResult);
    }

    @Test
    @DisplayName("rejectInitiativeOk | having a proper initiative | change status to Rejected")
    void rejectInitiativeOk() throws Exception {

        final Initiative expectedInitiative = new Initiative();
        expectedInitiative.set_id("2");
        expectedInitiative.setUserId("UserId");
        expectedInitiative.setNickname("some Nickname");
        expectedInitiative.setDescription("Description");
        expectedInitiative.setDate(LocalDateTime.now().withNano(0));
        expectedInitiative.setStatusId(InitiativeStatus.PENDING.getId());
        expectedInitiative.setImage("imageData");

        final InitiativeResponseDTO expectedDTO = expectedInitiative.toDTO();

        when(service.rejectInitiative(anyString())).thenReturn(expectedDTO);

        final MvcResult result = mvc.perform(post("/initiative/reject/{id}", expectedInitiative.get_id())
                .header(AUTHORIZATION_HEADER, BEARER_SUFFIX + "jwt"))
                .andExpect(status().is(HttpStatus.OK.value()))
                .andReturn();

        final InitiativeResponseDTO actualResult = objectMapper.readValue(result.getResponse().getContentAsString(), InitiativeResponseDTO.class);

        assertNotNull(actualResult);
    }


    @Test
    @DisplayName("approveInitiativeException | when initiative is not found | 800 Bad_Request")
    void approveInitiativeException() throws Exception {

        final InitiativeRequestDTO requestDTO = new InitiativeRequestDTO();


        when(service.approveInitiative("a")).thenThrow(new InitiativeService.InitiativeServiceException("TEST RESULT approve"));

       final MvcResult result =  mvc.perform(post("/initiative/approve/{id}", "a")
               .contentType(MediaType.APPLICATION_JSON).content(JSONValue.toJSONString(requestDTO))
                .header(AUTHORIZATION_HEADER, BEARER_SUFFIX + "jwt"))
               .andDo(print())
                .andExpect(status().is(com.spochi.controller.HttpStatus.BAD_REQUEST.getCode()))
                .andReturn();

       assertTrue(result.getResolvedException() instanceof InitiativeService.InitiativeServiceException);
       assertEquals(result.getResponse().getContentAsString(), "The Services fail because : TEST RESULT approve");
    }

    @Test
    @DisplayName("rejectInitiativeException | when initiative is not found | 800 Bad_Request")
    void rejectInitiativeException() throws Exception {

        final InitiativeRequestDTO requestDTO = new InitiativeRequestDTO();


        when(service.rejectInitiative("a")).thenThrow(new InitiativeService.InitiativeServiceException("TEST RESULT Reject"));

        final MvcResult result =  mvc.perform(post("/initiative/reject/{id}", "a")
                .contentType(MediaType.APPLICATION_JSON).content(JSONValue.toJSONString(requestDTO))
                .header(AUTHORIZATION_HEADER, BEARER_SUFFIX + "jwt"))
                .andDo(print())
                .andExpect(status().is(com.spochi.controller.HttpStatus.BAD_REQUEST.getCode()))
                .andReturn();

        assertTrue(result.getResolvedException() instanceof InitiativeService.InitiativeServiceException);
        assertEquals(result.getResponse().getContentAsString(), "The Services fail because : TEST RESULT Reject");
    }
}
