package com.spochi.service;

import com.spochi.dto.InitiativeResponseDTO;
import com.spochi.entity.Initiative;
import com.spochi.repository.InitiativeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class InitiativeService {
   @Autowired
   InitiativeRepository repository;

   public List<InitiativeResponseDTO> getAll(String date) {
      final Comparator<Initiative> comparator;

      if (date != null && date.equals("desc")) {
         comparator = Comparator.comparing(Initiative::getDate).reversed();
      } else {
         comparator = (i1, i2) -> 0;
      }

      final List<Initiative> initiatives = repository.findAll();

      return initiatives.stream()
              .sorted(comparator)
              .map(Initiative::toDTO)
              .collect(Collectors.toList());
   }
}
