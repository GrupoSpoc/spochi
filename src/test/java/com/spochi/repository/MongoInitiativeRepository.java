package com.spochi.repository;

import com.spochi.entity.Initiative;
import org.jetbrains.annotations.NotNull;
import org.springframework.context.annotation.Primary;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Primary
public interface MongoInitiativeRepository extends MongoRepository<Initiative,String>, InitiativeRepository {

   @Override
   default List<Initiative> getAll(){
        return findAll();
   }

    @Override
    default Initiative create(@NotNull Initiative initiative) {
       return save(initiative);
    }

}
