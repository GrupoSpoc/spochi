
package com.spochi.auth.firebase;

import com.spochi.auth.TokenInfo;
import com.spochi.controller.exception.BadRequestException;
import com.spochi.dto.UserResponseDTO;
import com.spochi.service.UserService;
import com.spochi.service.authenticate.firebase.FirebaseTokenProvider;
import com.spochi.service.authenticate.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

@Service
public class FirebaseService {

    @Autowired
    UserService userService;

    @Autowired
    FirebaseTokenProvider firebaseTokenProvider;

    @Autowired
    JwtUtil jwtUtil;

    public TokenInfo parseToken(String firebaseToken) {

        if (ObjectUtils.isEmpty(firebaseToken)) {
            throw new BadRequestException("Firebase token is empty");
        }

        try {
            String uid = firebaseTokenProvider.getUidFromToken(firebaseToken);

            final String jwt = jwtUtil.generateToken(uid);

            final UserResponseDTO user = userService.findByGoogleId(uid);

            final TokenInfo tokenInfo = new TokenInfo();
            tokenInfo.setJwt(jwt);
            tokenInfo.setUser(user);

            return tokenInfo;


        } catch (Exception e) {
            System.out.println("Firebase authentication failed: " + e.getMessage());
            throw new AuthorizationException();
        }
    }
}

