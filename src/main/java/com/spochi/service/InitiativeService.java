package com.spochi.service;

import com.spochi.controller.exception.BadRequestException;
import com.spochi.dto.InitiativeRequestDTO;
import com.spochi.dto.InitiativeResponseDTO;
import com.spochi.entity.Initiative;
import com.spochi.entity.InitiativeStatus;
import com.spochi.entity.User;
import com.spochi.repository.InitiativeRepository;
import com.spochi.repository.UserRepository;
import com.spochi.service.query.InitiativeSorter;
import org.apache.tomcat.util.codec.binary.Base64;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class InitiativeService {
    @Autowired
    InitiativeRepository initiativeRepository;

    @Autowired
    UserRepository userRepository;

    public List<InitiativeResponseDTO> getAll(InitiativeSorter sorter) {

        final ArrayList<InitiativeResponseDTO> responseDTOS = new ArrayList<InitiativeResponseDTO>();
        final List<Initiative> orderedInitiatives = initiativeRepository.getAllInitiatives(sorter);

        for (Initiative i : orderedInitiatives) {
            responseDTOS.add(
                    i.toDTO());
        }

        return responseDTOS;
    }

    public InitiativeResponseDTO create(InitiativeRequestDTO request, String uid) {
        final User user;
        final Initiative initiative;
        final InitiativeResponseDTO responseDTO;

        validateFields(request);
        final Optional<User> userOpt = userRepository.findByUid(uid);

        if (!userOpt.isPresent()) {
            throw new InitiativeServiceException("User not found");
        }

        user = userOpt.get();
        initiative = new Initiative(
                request.getDescription(),
                request.getImage(),
                user.getNickname(),
                LocalDateTime.parse(request.getDate()),
                user.getId(),
                InitiativeStatus.PENDING.getId()
        );

        initiativeRepository.create(initiative);
        responseDTO = initiative.toDTO();

        return responseDTO;
    }

    private void validateFields(InitiativeRequestDTO request) throws InitiativeServiceException {
        if (request.getDescription().isEmpty()) {
            throw new InitiativeServiceException("Initiative Description is empty");
        }
        if (request.getImage().isEmpty()) {
            throw new InitiativeServiceException("Initiative Image is empty");
        }
        if (!Base64.isBase64(request.getImage())) {
            throw new InitiativeServiceException("Initiative Image is not Base64");

        }
        if (request.getDate().isEmpty()) {
            throw new InitiativeServiceException("Initiative Date is empty");

        }
        if(!isDateFormatValid(request.getDate()) || LocalDateTime.parse(request.getDate()).isAfter(LocalDateTime.now())){
            throw new InitiativeServiceException("Initiative Date invalid");
        }
    }

    private boolean isDateFormatValid(String date){
        try{
            LocalDateTime.parse(date);
        }catch (Exception e){
            return false;
        }
        return true;
    }

    public static class InitiativeServiceException extends BadRequestException {
        private InitiativeServiceException(String failField) {
            super(String.format("The Services fail because : %s", failField));
        }
    }
}

