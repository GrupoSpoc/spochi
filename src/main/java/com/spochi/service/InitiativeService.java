package com.spochi.service;

import com.spochi.dto.InitiativeResponseDTO;
import com.spochi.entity.Initiative;
import com.spochi.repository.InitiativeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class InitiativeService {
   @Autowired
   InitiativeRepository repository;

   public List<InitiativeResponseDTO> getAll(Comparator<Initiative> sorter) {
      final Stream<Initiative> initiatives = repository.streamAll();

      return initiatives
              .sorted(sorter)
              .map(Initiative::toDTO)
              .collect(Collectors.toList());
   }
}
