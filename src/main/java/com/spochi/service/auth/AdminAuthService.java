package com.spochi.service.auth;

import com.spochi.auth.AuthorizationException;
import com.spochi.auth.TokenInfo;
import com.spochi.controller.exception.AdminAuthorizationException;
import com.spochi.dto.AdminRequestDTO;
import com.spochi.dto.UserResponseDTO;
import com.spochi.entity.User;
import com.spochi.entity.UserType;
import com.spochi.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;
import java.util.Optional;

@Service
public class AdminAuthService {
   @Autowired
    UserRepository userRepository;

    @Autowired
    JwtUtil jwtUtil;

    public TokenInfo authenticate(AdminRequestDTO requestDTO) {

            Optional<User> optionalUser = userRepository.findByUid(requestDTO.getUid());
            if (optionalUser==null||!optionalUser.isPresent()) {
                throw new AdminAuthorizationException();
            }

            User user = optionalUser.get();
            isValidAdmin(user, requestDTO.getPassword());

            final UserResponseDTO userResponseDTO = new UserResponseDTO();
            userResponseDTO.setAdmin(user.getType().equals(UserType.ADMIN));
            userResponseDTO.setType_id(user.getType().getId());
            userResponseDTO.setNickname(user.getNickname());

            TokenInfo tokenInfo = new TokenInfo();
            tokenInfo.setUser(userResponseDTO);
            tokenInfo.setJwt(jwtUtil.generateAdminToken(requestDTO.getUid()));

            return tokenInfo;
    }

    private void isValidAdmin(User user, String password) {

        if (user.getType()==null || !(user.getType() == UserType.ADMIN)) {
            throw new AdminAuthorizationException();
        }
        if (user.getPassword()==null|| !BCrypt.checkpw(password, user.getPassword())) {
            throw new AdminAuthorizationException();
        }
    }
}
