package com.spochi.service;

import com.spochi.controller.HttpStatus;
import com.spochi.controller.exception.BadRequestException;
import com.spochi.dto.InitiativeListResponseDTO;
import com.spochi.dto.InitiativeRequestDTO;
import com.spochi.dto.InitiativeResponseDTO;
import com.spochi.dto.RejectedInitiativeDTO;
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

    public InitiativeListResponseDTO getAll(InitiativeQuery query, String uid, boolean currentUser) {
        if (currentUser) {
            final User user = userRepository.findByUid(uid).orElseThrow(() -> new InitiativeServiceException("user not found when initiative getAll"));
            query.withUserId(user.getId());
        }

        final List<Initiative> initiatives = initiativeRepository.getAllInitiatives(query);

        final InitiativeListResponseDTO dto = new InitiativeListResponseDTO();
        dto.setInitiatives(initiatives.stream().map(Initiative::toDTO).collect(Collectors.toList()));

        boolean lastBatch;

        if (query.getLimit() != null) {
            lastBatch = initiatives.size() < query.getLimit();
        } else {
            lastBatch = initiatives.size() == 0;
        }

        dto.setLast_batch(lastBatch);

        return dto;
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
                InitiativeStatus.PENDING.getId(),
                null
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
        if (!isDateFormatValid(request.getDate()) || LocalDateTime.parse(request.getDate()).isAfter(LocalDateTime.now(ZoneId.of("UTC")))) {
            throw new InitiativeServiceException("Initiative Date invalid");
        }
    }

    private boolean isDateFormatValid(String date) {
        try {
            LocalDateTime.parse(date);
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    public InitiativeResponseDTO approveInitiative(String initiativeId) throws InitiativeServiceException {

        return updateStatus(initiativeId, InitiativeStatus.APPROVED);
    }

    public InitiativeResponseDTO rejectInitiative(RejectedInitiativeDTO rejectedDTOMotive) {

        return updateStatus(rejectedDTOMotive.getId(), InitiativeStatus.REJECTED, rejectedDTOMotive);
    }

    //For approve
    private InitiativeResponseDTO updateStatus(String initiativeId, InitiativeStatus status){

        final Optional<Initiative> toBeApproved = initiativeRepository.findInitiativeById(initiativeId);

        if (!toBeApproved.isPresent()) {
            throw new InitiativeServiceException("There are no initiatives with this id", HttpStatus.INITIATIVE_NOT_FOUND);
        }
        final Initiative initiative = toBeApproved.get();

        if (initiative.getStatusId() != InitiativeStatus.PENDING.getId()) {
            throw new InitiativeServiceException("Only pending initiatives can be approved", HttpStatus.BAD_INITIATIVE_STATUS);

        }

        initiativeRepository.changeStatus(initiative, status);

        final Optional<Initiative> updatedInitiative = initiativeRepository.findInitiativeById(initiativeId);

        return updatedInitiative.map(Initiative::toDTO).orElse(null);

    }

    // For reject
    private InitiativeResponseDTO updateStatus(String initiativeId, InitiativeStatus status, RejectedInitiativeDTO rejectedDTOMotive) {

        final Optional<Initiative> toBeRejected = initiativeRepository.findInitiativeById(initiativeId);

        if (!toBeRejected.isPresent()) {
            throw new InitiativeServiceException("There are no initiatives with this id", HttpStatus.INITIATIVE_NOT_FOUND);
        }
        final Initiative initiative = toBeRejected.get();

        if (initiative.getStatusId() != InitiativeStatus.PENDING.getId()) {
            throw new InitiativeServiceException("Only pending initiatives can be approved", HttpStatus.BAD_INITIATIVE_STATUS);

        }

        //Validar caracteres especiales y longitud

        initiativeRepository.changeStatus(initiative, status, rejectedDTOMotive.getReject_Motive());

        final Optional<Initiative> updatedInitiative = initiativeRepository.findInitiativeById(initiativeId);

        return updatedInitiative.map(Initiative::toDTO).orElse(null);
    }

    private boolean validateFiwareSpecialCharacters(String reject_motive) {


        return true;
    }

    public static class InitiativeServiceException extends BadRequestException {
        public InitiativeServiceException(String failField) {
            super(String.format("The Services fail because : %s", failField));
        }
        public InitiativeServiceException(String message, HttpStatus status) {
            super(String.format("The Services fail because : %s", message), status);
        }
    }
}

