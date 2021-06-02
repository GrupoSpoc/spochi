package com.spochi.service;

import com.spochi.controller.exception.BadRequestException;
import com.spochi.dto.InitiativeRequestDTO;
import com.spochi.dto.InitiativeResponseDTO;
import com.spochi.entity.Initiative;
import com.spochi.entity.InitiativeStatus;
import com.spochi.entity.User;
import com.spochi.repository.InitiativeRepository;
import com.spochi.repository.UserRepository;
import com.spochi.service.query.InitiativeQuery;
import com.spochi.service.query.InitiativeSorter;
import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class InitiativeService {
    @Autowired
    InitiativeRepository initiativeRepository;

    @Autowired
    UserRepository userRepository;

    // todo borrar
    public List<InitiativeResponseDTO> getAll(InitiativeSorter sorter, String uid) {
        final User user = userRepository.findByUid(uid).orElseThrow(()-> new InitiativeServiceException("user not found when initiative getAll"));
        final ArrayList<InitiativeResponseDTO> responseDTOS = new ArrayList<>();
        final List<Initiative> orderedInitiatives = initiativeRepository.getAllInitiatives(sorter);

        for (Initiative i : orderedInitiatives) {
            responseDTOS.add(i.toDTO());
        }

        return responseDTOS;
    }

    // todo mover consulta al repo cuando es currentUser, borrar fromCurrentUser
    public List<InitiativeResponseDTO> getAll(InitiativeQuery query, String uid, boolean currentUser) {

        if (currentUser) {
            final User user = userRepository.findByUid(uid).orElseThrow(()-> new InitiativeServiceException("user not found when initiative getAll"));
            query.withUserId(user.getId());
        }

        final List<Initiative> orderedInitiatives = initiativeRepository.getAllInitiatives(query);

        return orderedInitiatives.stream().map(Initiative::toDTO).collect(Collectors.toList());
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
        if(!isDateFormatValid(request.getDate()) || LocalDateTime.parse(request.getDate()).isAfter(LocalDateTime.now(ZoneId.of("UTC")))) {
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

