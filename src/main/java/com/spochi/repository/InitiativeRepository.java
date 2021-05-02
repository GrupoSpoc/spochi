package com.spochi.repository;

import com.spochi.entity.Initiative;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.stream.Stream;

@Repository
public interface InitiativeRepository extends MongoRepository<Initiative, String> {
    Stream<Initiative> streamAll();
}
