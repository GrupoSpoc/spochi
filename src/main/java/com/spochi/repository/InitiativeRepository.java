package com.spochi.repository;

import com.spochi.entity.Initiative;

import java.util.List;

public interface InitiativeRepository extends  EntityRepository<Initiative> {
    List<Initiative> getAll();
    Initiative create(Initiative initiative);
}
