
package com.spochi.auth.firebase;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseToken;
import com.spochi.auth.JwtUtil;
import com.spochi.auth.TokenInfo;
import com.spochi.controller.handler.BadRequestException;
import com.spochi.dto.UserResponseDTO;
import com.spochi.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

@Service
public class FirebaseService {

    @Autowired
    UserService userService;

    public TokenInfo parseToken(String firebaseToken) {

        if (ObjectUtils.isEmpty(firebaseToken)) {
            throw new BadRequestException("Firebase token is empty");
        }

        try {

            FirebaseToken token = FirebaseAuth.getInstance().verifyIdToken(firebaseToken);

            final String uid = token.getUid();
            final String jwt = new JwtUtil().generateToken(uid);

            final UserResponseDTO user = userService.findByGoogleId(uid);

            return new TokenInfo(jwt, user);

        } catch (Exception e) {
            System.out.println("Firebase authentication failed: " + e.getMessage());
            throw new FirebaseAuthorizationException();
        }
    }
}

