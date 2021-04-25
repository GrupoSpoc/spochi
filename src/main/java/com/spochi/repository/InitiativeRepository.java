package com.spochi.repository;

import com.spochi.entity.Initiative;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InitiativeRepository extends MongoRepository<Initiative, String> {
}
