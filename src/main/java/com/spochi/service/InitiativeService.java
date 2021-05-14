package com.spochi.service;

import com.spochi.controller.exception.BadRequestException;
import com.spochi.dto.InitiativeRequestDTO;
import com.spochi.dto.InitiativeResponseDTO;
import com.spochi.entity.Initiative;
import com.spochi.entity.InitiativeStatus;
import com.spochi.entity.User;
import com.spochi.repository.InitiativeRepository;
import com.spochi.repository.UserRepository;
import org.apache.tomcat.util.codec.binary.Base64;
import org.joda.time.LocalDateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.Comparator;
import java.util.List;
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


            // esto lo puedo poner en otro metodo que sea validarDto() y use adentro estos métodos?
            validateImage(request);
            validateDate(request);
            validateDescription(request);
            validateBase64nature(request);

            //recuperar al usario siempre que exista en el repositorio
            if(!userRepository.findByGoogleId(uid).isPresent()) {
               throw new BadServiceException("User not foud");
            }
            user = userRepository.findByGoogleId(uid).get();
            initiative = new Initiative(
                    request.get_id(),
                    request.getDescription(),
                    request.getImage(),
                    user.getNickname(),
                    LocalDateTime.parse(request.getDate()),
                    user.get_id(),
                    InitiativeStatus.PENDING.getId()

            );

            // agregarle al user la iniciativa
            user.addInitiative(initiative);

            // mandarle al repository del user el user

            //initiativeRepository.save();

            // generar el response




        return null;
    }

    protected boolean validateDescription(InitiativeRequestDTO request) throws BadServiceException {
        if (request.getDescription().isEmpty()) {
            throw new BadServiceException("Initiative Description");
        }
        return true;
    }

    protected boolean validateImage(InitiativeRequestDTO request) throws BadServiceException {
        if (request.getImage().isEmpty()) {
            throw new BadServiceException("Initiative Image-Empty");
        }
        return true;
    }

    protected boolean validateBase64nature(InitiativeRequestDTO request) throws BadServiceException {
        if (!Base64.isBase64(request.getImage())) {
            throw new BadServiceException("Initiative Image-Base64");

        }
        return true;
    }
    // todo: validar que la fecha no sea del futuro
    protected boolean validateDate(InitiativeRequestDTO request) throws BadServiceException {

        if (request.getDate().isEmpty()) {
            throw new BadServiceException("Initiative Date-Empty");

        }
      return true;
    }


    public static class BadServiceException extends BadRequestException {
        private BadServiceException(String failField) {
            super(String.format("The Services fail in [%s] ", failField));
        }
    }

}
}