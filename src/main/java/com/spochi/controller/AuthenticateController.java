package com.spochi.controller;

import com.spochi.auth.AuthorizationException;
import com.spochi.auth.TokenInfo;
import com.spochi.auth.firebase.FirebaseService;
import com.spochi.controller.exception.AdminAuthorizationException;
import com.spochi.dto.AdminRequestDTO;
import com.spochi.service.auth.AdminAuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/authenticate")
public class AuthenticateController {

    @Autowired
    FirebaseService firebaseService;

    @Autowired
    AdminAuthService adminService;

    @PostMapping
    public ResponseEntity<TokenInfo> authenticate(@RequestBody (required = false) String firebaseToken) {
        return ResponseEntity.ok(firebaseService.parseToken(firebaseToken));
    }

    @ExceptionHandler(value = {AuthorizationException.class})
    protected ResponseEntity<String> handleFirebaseAuthorizationException(AuthorizationException e) {
        return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE)
                .body("Invalid or expired token");
    }

    @PostMapping("/admin")
    public ResponseEntity<TokenInfo> authenticateAdmin(@RequestBody  AdminRequestDTO requestDTO){
        return ResponseEntity.ok(adminService.authenticate(requestDTO)) ;
    }

    @ExceptionHandler(value = {AdminAuthorizationException.class})
    protected ResponseEntity<String> handleAdminAuthorizationException(AdminAuthorizationException e) {
        return ResponseEntity.status(com.spochi.controller.HttpStatus.BAD_ADMIN_REQUEST.getCode())
                .body("Invalid AdminRequest");
    }
}
