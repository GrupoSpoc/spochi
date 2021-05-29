package com.spochi.repository;

import com.spochi.dto.InitiativeResponseDTO;
import com.spochi.entity.Initiative;
import com.spochi.service.query.InitiativeSorter;

import java.util.List;
import java.util.Optional;

public interface InitiativeRepository extends EntityRepository<Initiative> {
    List<Initiative> getAllInitiatives(InitiativeSorter sorter);
    Optional<Initiative> findInitiativeById(String id);
}
