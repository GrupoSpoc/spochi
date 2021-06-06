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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;

@Service
public class InitiativeService {
    @Autowired
    InitiativeRepository initiativeRepository;

    @Autowired
    UserRepository userRepository;

    public List<InitiativeResponseDTO> getAll(InitiativeSorter sorter, String uid) {
        final User user = userRepository.findByUid(uid).orElseThrow(()-> new InitiativeServiceException("user not found when initiative getAll"));
        final ArrayList<InitiativeResponseDTO> responseDTOS = new ArrayList<>();
        final List<Initiative> orderedInitiatives = initiativeRepository.getAllInitiatives(sorter);

        for (Initiative i : orderedInitiatives) {
            responseDTOS.add(i.toDTO(user.getId()));
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
        responseDTO = initiative.toDTO(user.getId());

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

    public InitiativeResponseDTO approveInitiative(String initiativeId) throws InitiativeServiceException {

        final Optional<Initiative> toBeApproved = initiativeRepository.findInitiativeById(initiativeId);

        if (!toBeApproved.isPresent()) {
            throw new InitiativeServiceException("There are no initiatives with this id");
        }
        final Initiative initiative = toBeApproved.get();

        if (initiative.getStatusId() != InitiativeStatus.PENDING.getId()) {
            throw new InitiativeServiceException("Only pending initiatives can be approved");
        }

        initiative.setStatusId(InitiativeStatus.APPROVED.getId());

        return initiative.toDTO(initiative.getUserId());
    }

    public static class InitiativeServiceException extends BadRequestException {
        public InitiativeServiceException(String failField) {
            super(String.format("The Services fail because : %s", failField));
        }
    }
}

