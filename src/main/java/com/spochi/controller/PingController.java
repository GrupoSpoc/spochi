package com.spochi.controller;

import com.spochi.entity.Initiative;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class PingController {

    @GetMapping("/ping")
    public String ping() {
        return "pong";
    }
}
