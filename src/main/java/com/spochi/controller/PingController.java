package com.spochi.controller;

import com.spochi.entity.Initiative;
import com.spochi.repository.InitiativeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;


@RestController
public class PingController {

    @Autowired
    InitiativeRepository initiativeRepository;

    @GetMapping("/ping")
    public String ping() {
        final Optional<Initiative> initiativeById = initiativeRepository.findInitiativeById("urn:ngsi-ld:Initiative:001");
        return "pong";
    }
}
