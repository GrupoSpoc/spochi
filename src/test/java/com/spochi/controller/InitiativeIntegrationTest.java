package com.spochi.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.spochi.dto.InitiativeResponseDTO;
import com.spochi.repository.InitiativeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class InitiativeIntegrationTest {

    @Autowired
    MockMvc mvc;

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    InitiativeRepository repository;

    private static final String GET_ALL_PATH = "/initiative/all";

    @BeforeEach
    public void beforeEach() {
        InitiativeTestUtil.getInitiatives().forEach(initiative -> repository.save(initiative));
    }

    @Test
    @DisplayName("getAll | without date param | ok")
    void getAllWithoutDateParamOk() throws Exception {
        // setup
        List<InitiativeResponseDTO> expectedDTOs = InitiativeTestUtil.getAllAsDTOs();

        // perform
        final MvcResult result = mvc.perform(get(GET_ALL_PATH))
                .andDo(print())
                .andExpect(status().is(HttpStatus.OK.value()))
                .andReturn();

        List<InitiativeResponseDTO> actualDTOs = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {});

        // assert
        assertEquals(expectedDTOs.size(), actualDTOs.size());
        assertTrue(actualDTOs.containsAll(expectedDTOs));
    }

    @Test
    @DisplayName("getAll | with date param | ok")
    void getAllWithDateParamOk() throws Exception {
        // perform
        final MvcResult result = mvc.perform(get(GET_ALL_PATH)
                .param("date", "desc"))
                .andDo(print())
                .andExpect(status().is(HttpStatus.OK.value()))
                .andReturn();

        List<InitiativeResponseDTO> actualDTOs = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {});

        // assert
        final InitiativeResponseDTO firstDTO = actualDTOs.get(0);
        final LocalDateTime firstDTODate = LocalDateTime.parse(firstDTO.getDate());

        assertTrue(actualDTOs.stream()
                .filter(dto -> !dto.equals(firstDTO))
                .allMatch(dto -> LocalDateTime.parse(dto.getDate()).isBefore(firstDTODate)));
    }
}