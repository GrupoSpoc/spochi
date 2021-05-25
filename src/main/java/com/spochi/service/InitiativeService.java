package com.spochi.service;

import com.spochi.controller.exception.BadRequestException;
import com.spochi.controller.handler.Uid;
import com.spochi.dto.InitiativeRequestDTO;
import com.spochi.dto.InitiativeResponseDTO;
import com.spochi.entity.Initiative;
import com.spochi.entity.InitiativeStatus;
import com.spochi.entity.User;
import com.spochi.repository.InitiativeRepository;
import com.spochi.repository.UserRepository;
import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class InitiativeService {
    @Autowired
    InitiativeRepository initiativeRepository;

    @Autowired
    UserRepository userRepository;

    public List<InitiativeResponseDTO> getAll(Comparator<Initiative> sorter, String uid) {
        final Stream<Initiative> initiatives = initiativeRepository.findAll().stream();

        final User user = userRepository.findByGoogleId(uid).orElseThrow(() -> new BadRequestException("No se encontró al USER"));

        return initiatives
                .sorted(sorter)
                .map(i -> i.toDTO(user.get_id()))
                .collect(Collectors.toList());
    }

    public InitiativeResponseDTO create(InitiativeRequestDTO request, String uid) {
        final User user;
        final Initiative initiative;
        final InitiativeResponseDTO responseDTO;

        validateFields(request);
        final Optional<User> userOpt = userRepository.findByGoogleId(uid);

        if (!userOpt.isPresent()) {
            throw new InitiativeServiceException("User not found");
        }

        user = userOpt.get();
        initiative = new Initiative(
                request.getDescription(),
                request.getImage(),
                user.getNickname(),
                LocalDateTime.parse(request.getDate()),
                user.get_id(),
                InitiativeStatus.PENDING.getId()
        );

        user.addInitiative(initiative);
        initiativeRepository.save(initiative);
        userRepository.save(user);
        responseDTO = initiative.toDTO().set_from_current_user(true);

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
        //todo: agregar la validacion para el caso en que la fecha sea una fecha del futuro
    }


    public static class InitiativeServiceException extends BadRequestException {
        private InitiativeServiceException(String failField) {
            super(String.format("The Services fail because : %s", failField));
        }
    }

}

