package com.spochi.service;

import com.spochi.dto.InitiativeRequestDTO;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.*;


@SpringBootTest
@ActiveProfiles("disable-firebase")

class InitiativeServiceTest {

    @Autowired
    InitiativeService service;

    private InitiativeRequestDTO wrong_initiative = new InitiativeRequestDTO();
    private InitiativeRequestDTO right_initiative = new InitiativeRequestDTO();
    @Test
    void initiativeDescriptionIsEmpty(){
        wrong_initiative.setDescription("");
        assertThrows(InitiativeService.BadServiceException.class, ()->service.validateDescription(wrong_initiative));
    }

    @Test
    void initiativeImageIsEmpty(){
        wrong_initiative.setImage("");
        assertThrows(InitiativeService.BadServiceException.class,()->service.validateImage(wrong_initiative));
    }

    @Test
    void initiativeImageIsNotBase64(){
        wrong_initiative.setImage("$$$////:");
        assertThrows(InitiativeService.BadServiceException.class,()->service.validateBase64nature(wrong_initiative));
    }

    @Test
    void initiativeImageIsBase64(){
        right_initiative.setImage("iVBORw0KGgoAAAANSUhEUgAAAAEAAAABCAYAAAAfFcSJAAAADUlEQVR42mP8z8BQDwAEhQGAhKmMIQAAAABJRU5ErkJggg==");
        assertTrue(service.validateBase64nature(right_initiative));
    }

    @Test
    void initiativeDateIsEmpty(){
        wrong_initiative.setDate("");
        assertThrows(InitiativeService.BadServiceException.class,()->service.validateDate(wrong_initiative));
    }

    @Test
    void initiativeDateIsFromFuture(){

    }



}