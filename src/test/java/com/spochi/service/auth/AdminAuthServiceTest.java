package com.spochi.service.auth;

import com.spochi.auth.AuthorizationException;
import com.spochi.auth.TokenInfo;
import com.spochi.dto.AdminRequestDTO;
import com.spochi.entity.User;
import com.spochi.entity.UserType;
import com.spochi.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.test.context.ActiveProfiles;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@SpringBootTest
@ActiveProfiles("disable-firebase")
class AdminAuthServiceTest {
    @MockBean
    UserRepository repository;

    @Autowired
    AdminAuthService adminAuthService;

    @MockBean
    JwtUtil jwtUtil;

    @Test
    void authenticateOK() {
        final String uid = "uid_admin";
        final String password = "pass";
        final String encrypted_pass = BCrypt.hashpw(password, BCrypt.gensalt());

        User user = new User();
        user.setUid(uid);
        user.setId("2");
        user.setTypeId(UserType.ADMIN);
        user.setPassword(encrypted_pass);

        AdminRequestDTO requestDTO = new AdminRequestDTO();
        requestDTO.setUid(uid);
        requestDTO.setPassword(password);

        when(repository.findByUid(requestDTO.getUid())).thenReturn(Optional.of(user));
        when(jwtUtil.generateAdminToken(requestDTO.getUid())).thenReturn("jwt");
        TokenInfo tokenInfo = adminAuthService.authenticate(requestDTO);

        assertTrue( tokenInfo.getUser().isAdmin());
        assertEquals("jwt", tokenInfo.getJwt());
    }

    @Test
    void authenticateButUserIsNotAdmin(){
        final String uid = "uid";
        final String password = "pass";
        final String encrypted_pass = BCrypt.hashpw(password, BCrypt.gensalt());

        User user = new User();
        user.setUid(uid);
        user.setId("2");
        user.setTypeId(UserType.ORGANIZATION);
        user.setPassword(encrypted_pass);

        AdminRequestDTO requestDTO = new AdminRequestDTO();
        requestDTO.setUid(uid);
        requestDTO.setPassword(password);

        when(repository.findByUid(requestDTO.getUid())).thenReturn(Optional.of(user));
        assertThrows(AuthorizationException.class, ()-> adminAuthService.authenticate(requestDTO));
    }
    @Test
    void authenticateFailPassIncorrect(){
        final String uid = "uid";
        final String password = "pass";
        final String encrypted_pass = BCrypt.hashpw("otra_password", BCrypt.gensalt());

        User user = new User();
        user.setUid(uid);
        user.setId("2");
        user.setTypeId(UserType.ORGANIZATION);
        user.setPassword(encrypted_pass);

        AdminRequestDTO requestDTO = new AdminRequestDTO();
        requestDTO.setUid(uid);
        requestDTO.setPassword(password);

        when(repository.findByUid(requestDTO.getUid())).thenReturn(Optional.of(user));
        assertThrows(AuthorizationException.class, ()-> adminAuthService.authenticate(requestDTO));
    }
}