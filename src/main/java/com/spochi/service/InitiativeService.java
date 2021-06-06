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
import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class InitiativeService {
    @Autowired
    InitiativeRepository initiativeRepository;

    @Autowired
    UserRepository userRepository;

    public List<InitiativeResponseDTO> getAll(InitiativeQuery query, String uid, boolean currentUser) {
        if (currentUser) {
            final User user = userRepository.findByUid(uid).orElseThrow(()-> new InitiativeServiceException("user not found when initiative getAll"));
            query.withUserId(user.getId());
        }

        final List<Initiative> initiatives = initiativeRepository.getAllInitiatives(query);

        return initiatives.stream().map(Initiative::toDTO).collect(Collectors.toList());
    }

    public InitiativeResponseDTO create(InitiativeRequestDTO request, String uid) {
        final User user;
        final Initiative initiative;

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

        final Initiative initiativeCreated = initiativeRepository.create(initiative);

        return initiativeCreated.toDTO();
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

        return initiative.toDTO();
    }

    public static class InitiativeServiceException extends BadRequestException {
        public InitiativeServiceException(String failField) {
            super(String.format("The Services fail because : %s", failField));
        }
    }
}

