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

    public List<InitiativeResponseDTO> getAll(Comparator<Initiative> sorter) {
        final Stream<Initiative> initiatives = initiativeRepository.findAll().stream();

        return initiatives
                .sorted(sorter)
                .map(Initiative::toDTO)
                .collect(Collectors.toList());
    }

    public InitiativeResponseDTO create(InitiativeRequestDTO request, String uid) {
        final User user;
        final Initiative initiative;
        final InitiativeResponseDTO responseDTO;

        validateFields(request);
        final Optional<User> userOpt = userRepository.findByGoogleId(uid);

        if (!userOpt.isPresent()) {
            throw new BadServiceException("User not found");
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
        userRepository.save(user);
        initiativeRepository.save(initiative);
        responseDTO = initiative.toDTO();

        return responseDTO;
    }

    private void validateFields(InitiativeRequestDTO request) throws BadServiceException {


        if (request.getDescription().isEmpty()) {
            throw new BadServiceException("Initiative Description");
        }

        if (request.getImage().isEmpty()) {
            throw new BadServiceException("Initiative Image-Empty");
        }
        if (!Base64.isBase64(request.getImage())) {
            throw new BadServiceException("Initiative Image-Base64");

        }
        if (request.getDate().isEmpty()) {
            throw new BadServiceException("Initiative Date-Empty");

        }
        //todo: agregar la validacion para el caso en que la fecha sea una fecha del futuro
    }


    public static class BadServiceException extends BadRequestException {
        private BadServiceException(String failField) {
            super(String.format("The Services fail in [%s] ", failField));
        }
    }

}
