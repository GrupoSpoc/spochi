package com.spochi.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.spochi.controller.exception.BadRequestException;
import com.spochi.dto.InitiativeListResponseDTO;
import com.spochi.dto.InitiativeRequestDTO;
import com.spochi.dto.InitiativeResponseDTO;
import com.spochi.entity.Initiative;
import com.spochi.entity.User;
import com.spochi.persistence.UserDummyBuilder;
import com.spochi.repository.MongoInitiativeRepositoryInterface;
import com.spochi.repository.MongoUserRepository;
import com.spochi.service.auth.JwtUtil;
import com.spochi.service.query.InitiativeSorter;
import net.minidev.json.JSONValue;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
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
import java.util.List;

import static com.spochi.auth.JwtFilter.AUTHORIZATION_HEADER;
import static com.spochi.auth.JwtFilter.BEARER_SUFFIX;
import static com.spochi.controller.HttpStatus.BAD_REQUEST;
import static com.spochi.controller.InitiativeTestUtil.UNIQUE_ID;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
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
    MongoInitiativeRepositoryInterface repository;

    @Autowired
    MongoUserRepository userRepository;

    @MockBean
    JwtUtil jwtUtil;


    private static final String GET_ALL_PATH = "/initiative/all";
    private static final String CREATE_PATH = "/initiative";
    private static final String UID = "unique_uid";
    private static final String jwt = "jwt";

    @BeforeEach
    public void beforeEach() {
        InitiativeTestUtil.getInitiatives().forEach(initiative -> repository.save(initiative));
        User.UserBuilder builder = User.builder();
        builder.uid(UID);
        builder.id(UNIQUE_ID);
        User user = builder.build();
        userRepository.create(user);
    }


    @AfterEach
    void clearDB() {
        repository.deleteAll();
        userRepository.deleteAll();
    }

    @Test
    @DisplayName("getAll | without order param | ok")
    void getAllWithoutOrderParamOk() throws Exception {
        // setup
        List<InitiativeResponseDTO> expectedDTOs = InitiativeTestUtil.getAllAsDTOs();

        when(jwtUtil.extractUid(jwt)).thenReturn(UID);

        // perform
        final MvcResult result = mvc.perform(get(GET_ALL_PATH)
                .header(AUTHORIZATION_HEADER, BEARER_SUFFIX + jwt))
                .andDo(print())
                .andExpect(status().is(HttpStatus.OK.value()))
                .andReturn();

        InitiativeListResponseDTO responseDTO = objectMapper.readValue(result.getResponse().getContentAsString(), InitiativeListResponseDTO.class);
        List<InitiativeResponseDTO> actualDTOs = responseDTO.getInitiatives();

        // assert
        assertEquals(expectedDTOs.size(), actualDTOs.size());
        assertTrue(actualDTOs.containsAll(expectedDTOs));
        assertFalse(responseDTO.isLast_batch());
    }

    @Test
    @DisplayName("getAll | with order param | date desc | ok")
    void getAllWithValidOrderDateDescOk() throws Exception {
        // perform
        when(jwtUtil.extractUid(jwt)).thenReturn(UID);
        final MvcResult result = mvc.perform(get(GET_ALL_PATH)
                .header(AUTHORIZATION_HEADER, BEARER_SUFFIX + jwt)
                .param("order", String.valueOf(InitiativeSorter.DATE_DESC.getId())))
                .andDo(print())
                .andExpect(status().is(HttpStatus.OK.value()))
                .andReturn();

        InitiativeListResponseDTO responseDTO = objectMapper.readValue(result.getResponse().getContentAsString(), InitiativeListResponseDTO.class);
        List<InitiativeResponseDTO> actualDTOs = responseDTO.getInitiatives();

        // assert
        // bubble compare:
        // recorro todos los DTOs y por cada uno me aseguro que todos los que siguen tengan la misma fecha o anterior
        for (int i = 0; i < actualDTOs.size(); i++) {
            final InitiativeResponseDTO currentDto = actualDTOs.get(i);
            final LocalDateTime currentDate = LocalDateTime.parse(currentDto.getDate());
            for (int j = i + 1; j + 1 < actualDTOs.size(); j++) {
                final InitiativeResponseDTO nextDto = actualDTOs.get(j);
                assertTrue(LocalDateTime.parse(nextDto.getDate()).compareTo(currentDate) <= 0);
            }
        }
    }

    @Test
    @DisplayName("getAll | with order param | date asc | ok")
    void getAllWithValidOrderDateAscOk() throws Exception {
        // perform
        when(jwtUtil.extractUid(jwt)).thenReturn(UID);
        final MvcResult result = mvc.perform(get(GET_ALL_PATH)
                .header(AUTHORIZATION_HEADER, BEARER_SUFFIX + jwt)
                .param("order", String.valueOf(InitiativeSorter.DATE_ASC.getId())))
                .andDo(print())
                .andExpect(status().is(HttpStatus.OK.value()))
                .andReturn();

        InitiativeListResponseDTO responseDTO = objectMapper.readValue(result.getResponse().getContentAsString(), InitiativeListResponseDTO.class);
        List<InitiativeResponseDTO> actualDTOs = responseDTO.getInitiatives();

        // assert
        // bubble compare:
        // recorro todos los DTOs y por cada uno me aseguro que todos los que siguen tengan la misma fecha o posterior
        for (int i = 0; i < actualDTOs.size(); i++) {
            final InitiativeResponseDTO currentDto = actualDTOs.get(i);
            final LocalDateTime currentDate = LocalDateTime.parse(currentDto.getDate());
            for (int j = i + 1; j + 1 < actualDTOs.size(); j++) {
                final InitiativeResponseDTO nextDto = actualDTOs.get(j);
                assertTrue(LocalDateTime.parse(nextDto.getDate()).compareTo(currentDate) >= 0);
            }
        }
    }

    @Test
    @DisplayName("getAll | with limit more than initiatives | last batch")
    void getAllLastBatch() throws Exception {
        // setup
        List<InitiativeResponseDTO> expectedDTOs = InitiativeTestUtil.getAllAsDTOs();

        when(jwtUtil.extractUid(jwt)).thenReturn(UID);

        // perform
        final MvcResult result = mvc.perform(get(GET_ALL_PATH)
                .param("limit", String.valueOf(expectedDTOs.size() + 1))
                .header(AUTHORIZATION_HEADER, BEARER_SUFFIX + jwt))
                .andDo(print())
                .andExpect(status().is(HttpStatus.OK.value()))
                .andReturn();

        InitiativeListResponseDTO responseDTO = objectMapper.readValue(result.getResponse().getContentAsString(), InitiativeListResponseDTO.class);
        List<InitiativeResponseDTO> actualDTOs = responseDTO.getInitiatives();

        // assert
        assertEquals(expectedDTOs.size(), actualDTOs.size());
        assertTrue(actualDTOs.containsAll(expectedDTOs));
        assertTrue(responseDTO.isLast_batch());
    }

    @Test
    @DisplayName("getAll | with invalid order param | error")
    void getAllWithInvalidOrderParamException() throws Exception {
        final String invalidSorterId = "-1";

        // perform
        when(jwtUtil.extractUid(jwt)).thenReturn(UID);
        final MvcResult result = mvc.perform(get(GET_ALL_PATH)
                .header(AUTHORIZATION_HEADER, BEARER_SUFFIX + jwt)
                .param("order", invalidSorterId))
                .andDo(print())
                .andExpect(status().is(BAD_REQUEST.getCode()))
                .andReturn();

        final BadRequestException exception = (BadRequestException) result.getResolvedException();
        assertTrue(exception instanceof InitiativeSorter.InitiativeSorterNotFoundException);
        assertEquals("No InitiativeSorter with id [" + invalidSorterId + "] present", exception.getMessage());
    }

    @Test
    @DisplayName("create | ok")
    void createOK() throws Exception {
       final String uid = "uid";
       final String jwt = "jwt";
        //create user
        final User user = UserDummyBuilder.build(uid);
        userRepository.create(user);
        //create requestDTO
        final String DESCRIPTION = "description";
        final String IMAGE = "image";
        final String DATE = LocalDateTime.now().withNano(0).toString();

        InitiativeRequestDTO requestDTO = new InitiativeRequestDTO();
        requestDTO.setDescription(DESCRIPTION);
        requestDTO.setImage(IMAGE);
        requestDTO.setDate(DATE);

        when(jwtUtil.extractUid(jwt)).thenReturn(uid);
        final MvcResult result = mvc.perform(post(CREATE_PATH)
                .contentType(MediaType.APPLICATION_JSON).content(JSONValue.toJSONString(requestDTO))
                .header(AUTHORIZATION_HEADER, BEARER_SUFFIX + jwt))
                .andDo(print())
                .andExpect(status().is(HttpStatus.OK.value()))
                .andReturn();

        InitiativeResponseDTO resultDTO = objectMapper.readValue(result.getResponse().getContentAsString(), InitiativeResponseDTO.class);

        assertNotNull(repository.findById(resultDTO.get_id()));
        assertEquals(IMAGE, resultDTO.getImage());
        assertEquals(DESCRIPTION, resultDTO.getDescription());
        assertEquals(DATE, resultDTO.getDate());
        assertEquals(user.getNickname(), resultDTO.getNickname());
        assertEquals(1, resultDTO.getStatus_id());

        Initiative save_initiative = repository.findById(resultDTO.get_id()).get();
        assertEquals(IMAGE, save_initiative.getImage());
        assertEquals(DESCRIPTION, save_initiative.getDescription());
        assertEquals(requestDTO.getDate(), save_initiative.getDate().toString());
        assertEquals(user.getNickname(), save_initiative.getNickname());
        assertEquals(1, save_initiative.getStatusId());
    }

    @Test
    void createFail() throws Exception {
        repository.deleteAll();

        //create user
        final String uid = "uid";
        final String jwt = "jwt";
        final User user = UserDummyBuilder.build(uid);
        userRepository.create(user);
        //create requestDTO
        final String DESCRIPTION = "description";
        final String IMAGE = "image";
        final String WRONG_DATE = "0";

        InitiativeRequestDTO requestDTO = new InitiativeRequestDTO();
        requestDTO.setDescription(DESCRIPTION);
        requestDTO.setImage(IMAGE);
        requestDTO.setDate(WRONG_DATE);

        when(jwtUtil.extractUid(jwt)).thenReturn(uid);

        final MvcResult result = mvc.perform(post(CREATE_PATH)
                .contentType(MediaType.APPLICATION_JSON).content(JSONValue.toJSONString(requestDTO))
                .header(AUTHORIZATION_HEADER, BEARER_SUFFIX + jwt))
                .andDo(print())
                .andExpect(status().is(BAD_REQUEST.getCode()))
                .andReturn();

        final BadRequestException exception = (BadRequestException) result.getResolvedException();
        assertEquals(0, repository.findAll().size());
        assertEquals("The Services fail because : Initiative Date invalid", exception.getMessage());
    }
}