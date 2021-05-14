package com.spochi.controller;

import com.spochi.controller.handler.Uid;
import com.spochi.dto.UserRequestDTO;
import com.spochi.dto.UserResponseDTO;
import com.spochi.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    UserService service;

    @PostMapping
    UserResponseDTO create(@RequestBody UserRequestDTO request, @Uid String uid) {
        return service.create(request, uid);
    }
}
