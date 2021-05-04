package com.spochi.controller;

import com.spochi.dto.TokenInfo;
import com.spochi.dto.UserResponseDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/authenticate")
public class AuthenticateController {

    @PostMapping
    public ResponseEntity<TokenInfo> authenticateOk(@RequestBody (required = false) String firebaseToken) {
        if (firebaseToken == null || ObjectUtils.isEmpty(firebaseToken)) {
            return ResponseEntity.status(406).build();
        }

        final TokenInfo tokenInfo = new TokenInfo();
        tokenInfo.setJwt(UUID.randomUUID().toString());

        final UserResponseDTO dto = new UserResponseDTO();

        dto.setAdmin(false);
        dto.setType_id(1);
        dto.setAmount_of_initiatives(4);
        dto.setNickname("test-user");
        tokenInfo.setUser(dto);

        return ResponseEntity.ok(tokenInfo);
    }

    @PostMapping("/badrequest")
    public ResponseEntity<String> authenticateBadRequest() {
        return ResponseEntity.badRequest().body("Something is wrong in the request!");
    }

    @PostMapping("/servererror")
    public ResponseEntity<String> authenticateInternalServerError() {
        return ResponseEntity.status(500).body("Something is wrong in the server!");
    }
}
