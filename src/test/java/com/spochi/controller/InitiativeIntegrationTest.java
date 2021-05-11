package com.spochi.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.spochi.controller.exception.BadRequestException;
import com.spochi.dto.InitiativeResponseDTO;
import com.spochi.repository.InitiativeRepository;
import com.spochi.service.query.InitiativeSorter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles(profiles = {"disable-firebase", "disable-jwt-filter"})
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
    @DisplayName("getAll | without order param | ok")
    void getAllWithoutOrderParamOk() throws Exception {
        // setup
        List<InitiativeResponseDTO> expectedDTOs = InitiativeTestUtil.getAllAsDTOs();

        // perform
        final MvcResult result = mvc.perform(get(GET_ALL_PATH))
                .andDo(print())
                .andExpect(status().is(HttpStatus.OK.value()))
                .andReturn();

        List<InitiativeResponseDTO> actualDTOs = Arrays.asList(objectMapper.readValue(result.getResponse().getContentAsString(), InitiativeResponseDTO[].class));

        // assert
        assertEquals(expectedDTOs.size(), actualDTOs.size());
        assertTrue(actualDTOs.containsAll(expectedDTOs));
    }

    @Test
    @DisplayName("getAll | with order param | date desc | ok")
    void getAllWithValidOrderParamOk() throws Exception {
        // perform
        final MvcResult result = mvc.perform(get(GET_ALL_PATH)
                .param("order", String.valueOf(InitiativeSorter.DATE_DESC.getId())))
                .andDo(print())
                .andExpect(status().is(HttpStatus.OK.value()))
                .andReturn();

        List<InitiativeResponseDTO> actualDTOs = Arrays.asList(objectMapper.readValue(result.getResponse().getContentAsString(), InitiativeResponseDTO[].class));

        // assert
        // bubble compare:
        // recorro todos los DTOs y por cada uno me aseguro que todos los que siguen tengan la misma fecha o anterior
        for (int i = 0; i < actualDTOs.size(); i++) {
            final InitiativeResponseDTO currentDto = actualDTOs.get(i);
            final LocalDateTime currentDate = LocalDateTime.parse(currentDto.getDate());
            for (int j = i + 1; j + 1  < actualDTOs.size(); j++) {
                final InitiativeResponseDTO nextDto = actualDTOs.get(j);
                assertTrue(LocalDateTime.parse(nextDto.getDate()).compareTo(currentDate) <= 0);
            }
        }
    }

    @Test
    @DisplayName("getAll | with invalid order param | error")
    void getAllWithInvalidOrderParamException() throws Exception {
        final String invalidSorterId = "-1";

        // perform
        final MvcResult result = mvc.perform(get(GET_ALL_PATH)
                .param("order", invalidSorterId))
                .andDo(print())
                .andExpect(status().is(HttpStatus.BAD_REQUEST.value()))
                .andReturn();

        final BadRequestException exception = (BadRequestException) result.getResolvedException();
        assertTrue(exception instanceof InitiativeSorter.InitiativeSorterNotFoundException);
        assertEquals("No InitiativeSorter with id [" + invalidSorterId + "] present", exception.getMessage());
    }
}