package com.spochi.controller;

import com.spochi.auth.AuthorizationException;
import com.spochi.auth.firebase.FirebaseService;
import com.spochi.auth.TokenInfo;
import com.spochi.dto.AdminLoginRequestDTO;
import com.spochi.repository.UserRepository;
import com.spochi.service.auth.JwtUtil;
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
    JwtUtil jwtUtil;

    @PostMapping
    public ResponseEntity<TokenInfo> authenticate(@RequestBody (required = false) String firebaseToken) {
        return ResponseEntity.ok(firebaseService.parseToken(firebaseToken));
    }

    @PostMapping("/admin")
    protected ResponseEntity<TokenInfo> authenticateAdmin(@RequestBody AdminLoginRequestDTO requestDTO) {
        final String jwt = jwtUtil.generateToken(requestDTO.getUid());
        final TokenInfo tokenInfo = new TokenInfo();
        tokenInfo.setJwt(jwt);

        return ResponseEntity.ok(tokenInfo);
    }

    @ExceptionHandler(value = {AuthorizationException.class})
    protected ResponseEntity<String> handleFirebaseAuthorizationException(AuthorizationException e) {
        return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE)
                .body("Invalid or expired token");
    }


}
