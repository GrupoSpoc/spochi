package com.spochi.repository;

import com.spochi.dto.InitiativeResponseDTO;
import com.spochi.entity.Initiative;
import com.spochi.service.query.InitiativeSorter;
import org.jetbrains.annotations.NotNull;
import org.springframework.context.annotation.Primary;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Repository
@Primary
public interface MongoInitiativeRepositoryInterface extends MongoRepository<Initiative,String>, InitiativeRepository {

   @Override
   default List<Initiative> getAllInitiatives(InitiativeSorter sorter){

       final Stream<Initiative> initiatives = findAll().stream();
       return initiatives
               .sorted(sorter.getComparator())
               .collect(Collectors.toList());
   }

    @Override
    default Initiative create(@NotNull Initiative initiative) {
       return save(initiative);
    }

    @Override
   default Optional<Initiative> findInitiativeById(String id){
       return findById(id);
    }
}
