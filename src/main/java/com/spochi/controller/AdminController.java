package com.spochi.controller;

import com.spochi.dto.AdminLoginRequestDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin")
public class AdminController {

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody AdminLoginRequestDTO request) {
        return ResponseEntity.ok("Logeado exitosamente: " + request.getUid() + " - " + request.getPassword());
        // return ResponseEntity.badRequest().body("Logeado exitosamente: " + request.getUid() + " - " + request.getPassword());

    }
}
