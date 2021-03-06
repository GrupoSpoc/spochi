package com.spochi.service.auth;

import com.spochi.auth.TokenInfo;
import com.spochi.controller.exception.AdminAuthorizationException;
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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
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

        assertEquals(UserType.ADMIN.getId(),tokenInfo.getUser().getType_id());
        assertEquals("jwt", tokenInfo.getJwt());
    }

    @Test
    void authenticateFailsUserIsNotAdmin(){
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
        assertThrows(AdminAuthorizationException.class, ()-> adminAuthService.authenticate(requestDTO));
    }
    @Test
    void authenticateFailsPassIncorrect(){
        final String uid = "uid";
        final String password = "pass";
        final String encrypted_pass = BCrypt.hashpw("otra_password", BCrypt.gensalt());

        User user = new User();
        user.setUid(uid);
        user.setId("2");
        user.setTypeId(UserType.ADMIN);
        user.setPassword(encrypted_pass);

        AdminRequestDTO requestDTO = new AdminRequestDTO();
        requestDTO.setUid(uid);
        requestDTO.setPassword(password);

        when(repository.findByUid(requestDTO.getUid())).thenReturn(Optional.of(user));
        assertThrows(AdminAuthorizationException.class, ()-> adminAuthService.authenticate(requestDTO));
    }
    @Test
    void authenticateFailsUserNotFound(){
        AdminRequestDTO requestDTO = new AdminRequestDTO();

        when(repository.findByUid(requestDTO.getUid())).thenReturn(Optional.empty());
        assertThrows(AdminAuthorizationException.class, ()-> adminAuthService.authenticate(requestDTO));
    }
}