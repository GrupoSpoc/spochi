package com.spochi.service;

import com.spochi.controller.HttpStatus;
import com.spochi.dto.UserRequestDTO;
import com.spochi.dto.UserResponseDTO;
import com.spochi.entity.User;
import com.spochi.entity.UserType;
import com.spochi.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

@Service
public class UserService {
    @Autowired
    UserRepository repository;

    public UserResponseDTO findByUid(String uid) {
        return repository.findByUid(uid)
                .map(User::toDTO)
                .orElse(null);
    }

    public UserResponseDTO create(UserRequestDTO request, String uid) {
        validate(request, uid);

        final User user = new User();
        user.setNickname(request.getNickname());
        user.setTypeId(request.getType_id());
        user.setUid(uid);

        return repository.persist(user).toDTO();
    }


    private void validate(UserRequestDTO request, String uid) {
        if (repository.findByUid(uid).isPresent()) {
            throw new UserServiceException("this google account already has a user");
        }

        if (ObjectUtils.isEmpty(request.getNickname())) {
            throw new UserServiceException("nickname cannot be null or empty");
        } else {
            if (repository.findByNickname(request.getNickname()).isPresent()) {
                throw new UserServiceException("nickname already taken", HttpStatus.NICKNAME_ALREADY_TAKEN);
            }
        }

        if (request.getType_id() == null) {
            throw new UserServiceException("type_id cannot be null");
        } else {
            UserType.fromIdOrElseThrow(request.getType_id());
        }
    }
}
