package com.spochi.repository;

import com.spochi.entity.Initiative;
import com.spochi.entity.InitiativeStatus;
import com.spochi.service.query.InitiativeQuery;

import java.util.List;
import java.util.Optional;

public interface InitiativeRepository extends EntityRepository<Initiative> {
    List<Initiative> getAllInitiatives(InitiativeQuery query);
    Optional<Initiative> findInitiativeById(String id);
    void changeStatus(Initiative initiative, InitiativeStatus status);
}
