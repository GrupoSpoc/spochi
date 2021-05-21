package com.spochi.controller;

import com.spochi.dto.UserResponseDTO;
import com.spochi.service.UserService;
import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
public class PingController {
    @Autowired
    UserService service;

    @GetMapping("/ping")
    public UserResponseDTO ping() throws IOException {
        return service.findByUid("Fkv0XkyWchhap04xN7ZsC3giETu2");
    }
}
