package com.spochi.auth;

import com.spochi.service.auth.JwtUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@ActiveProfiles("disable-firebase")
class JwtFilterTest {
    MockMvc mvc;

    @Autowired
    WebApplicationContext context;

    @MockBean
    JwtUtil jwtUtil;

    @BeforeEach
    void before() {
        this.mvc = MockMvcBuilders.webAppContextSetup(context)
                .addFilter(new JwtFilter(jwtUtil), "/*")
                .build();
    }


    @Test
    @DisplayName("do filter internal | when token is valid | ok")
    void doFilterInternalTokenValid() throws Exception {
        when(jwtUtil.isTokenValid(anyString())).thenReturn(true);

        mvc.perform(get("/initiative/all")
                .header(JwtFilter.AUTHORIZATION_HEADER, JwtFilter.BEARER_SUFFIX + " token"))
                .andDo(print())
                .andExpect(status().is(HttpStatus.OK.value()))
                .andReturn();
    }

    @Test
    @DisplayName("do filter internal | when token is invalid | should return status forbidden")
    void doFilterInternalTokenInvalid() throws Exception {
        when(jwtUtil.isTokenValid(anyString())).thenReturn(false);

        final MvcResult result = mvc.perform(get("/initiative/all")
                .header(JwtFilter.AUTHORIZATION_HEADER, JwtFilter.BEARER_SUFFIX + " token"))
                .andDo(print())
                .andExpect(status().is(HttpStatus.NOT_ACCEPTABLE.value()))
                .andReturn();

        assertEquals(JwtFilter.INVALID_TOKEN_MESSAGE, result.getResponse().getContentAsString());
    }

    @Test
    @DisplayName("do filter internal | when not sending header | should return status forbidden")
    void doFilterInternalNoHeader() throws Exception {
        when(jwtUtil.isTokenValid(anyString())).thenReturn(true);

        final MvcResult result = mvc.perform(get("/initiative/all"))
                .andDo(print())
                .andExpect(status().is(HttpStatus.NOT_ACCEPTABLE.value()))
                .andReturn();

        assertEquals(JwtFilter.INVALID_TOKEN_MESSAGE, result.getResponse().getContentAsString());
    }

    @Test
    @DisplayName("do filter internal | when not sending Bearer sufix | should return status forbidden")
    void doFilterInternalNoBearerSuffix() throws Exception {
        when(jwtUtil.isTokenValid(anyString())).thenReturn(true);

        final MvcResult result = mvc.perform(get("/initiative/all")
                .header(JwtFilter.AUTHORIZATION_HEADER, "no-bearer-token"))
                .andDo(print())
                .andExpect(status().is(HttpStatus.NOT_ACCEPTABLE.value()))
                .andReturn();

        assertEquals(JwtFilter.INVALID_TOKEN_MESSAGE, result.getResponse().getContentAsString());
    }

    @Test
    @DisplayName("do filter internal | when calling skipped endpoint | when not sending header | ok")
    void doFilterInternalSkippedEndpoint() throws Exception {
        mvc.perform(get("/ping"))
                .andDo(print())
                .andExpect(status().is(HttpStatus.OK.value()))
                .andReturn();
    }
}