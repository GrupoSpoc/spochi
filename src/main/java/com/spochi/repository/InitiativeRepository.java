package com.spochi.repository;

import com.spochi.entity.Initiative;
import com.spochi.service.query.InitiativeQuery;
import com.spochi.service.query.InitiativeSorter;

import java.util.List;
import java.util.Optional;

public interface InitiativeRepository extends EntityRepository<Initiative> {
    List<Initiative> getAllInitiatives(InitiativeSorter sorter);
    List<Initiative> getAllInitiatives(InitiativeQuery query);
    Optional<Initiative> findInitiativeById(String id);
}
