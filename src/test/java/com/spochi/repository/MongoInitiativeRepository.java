package com.spochi.repository;

import com.spochi.entity.Initiative;
import com.spochi.service.query.InitiativeSorter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;

import java.util.List;
import java.util.Optional;

@Primary
public class MongoInitiativeRepository implements InitiativeRepository {

    @Autowired
    MongoInitiativeRepositoryInterface initiativeRepositoryInterface;

    @Override
    public List<Initiative> getAllInitiatives(InitiativeSorter sorter) {
        return initiativeRepositoryInterface.getAllInitiatives(sorter);
    }

    @Override
    public Initiative create(Initiative initiative) {
        return initiativeRepositoryInterface.create(initiative);
    }

    @Override
    public Optional<Initiative> findInitiativeById(String id) {
        return initiativeRepositoryInterface.findInitiativeById(id);
    }
}
