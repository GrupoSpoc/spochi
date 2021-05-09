package com.spochi.controller;

import com.spochi.auth.firebase.FirebaseAuthorizationException;
import com.spochi.auth.firebase.FirebaseService;
import com.spochi.auth.TokenInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/authenticate")
public class AuthenticateController {

    @Autowired
    FirebaseService firebaseService;

    @PostMapping
    public ResponseEntity<TokenInfo> authenticate(@RequestBody (required = false) String firebaseToken) {
        return ResponseEntity.ok(firebaseService.parseToken(firebaseToken));
    }

    @ExceptionHandler(value = {FirebaseAuthorizationException.class})
    protected ResponseEntity<String> handleFirebaseAuthorizationException(FirebaseAuthorizationException e) {
        return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE)
                .body("Invalid or expired token");
    }
}
