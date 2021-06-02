package com.spochi.repository;

import com.spochi.entity.Initiative;
import com.spochi.entity.InitiativeStatus;
import com.spochi.entity.User;
import com.spochi.service.query.InitiativeQuery;
import com.spochi.service.query.InitiativeSorter;
import com.spochi.util.DateUtil;
import org.jetbrains.annotations.NotNull;
import org.springframework.context.annotation.Primary;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;
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
    default List<Initiative> getAllInitiatives(InitiativeQuery query){
        final Stream<Initiative> initiatives = findAll().stream();

        final Predicate<Initiative> predicate = parseQuery(query);

        return initiatives
                .filter(predicate)
                .sorted(query.getSorter().getComparator())
                .collect(Collectors.toList());
    }

    default Predicate<Initiative> parseQuery(InitiativeQuery initiativeQuery) {
        List<Predicate<Initiative>> predicates = new ArrayList<>();
        if (initiativeQuery.getUserId() != null) {
            predicates.add(i -> i.getUserId().equals(initiativeQuery.getUserId()));
        }

        if (initiativeQuery.getStatuses() != null) {
            predicates.add(i -> initiativeQuery.getStatuses()
                    .stream()
                    .map(InitiativeStatus::getId)
                    .anyMatch(s -> s == i.getStatusId()));
        }

        if (initiativeQuery.getDateTop() != null) {
            predicates.add(i -> i.getDate().isBefore(initiativeQuery.getDateTop()));
        }

        return i -> predicates.stream().allMatch(p -> p.test(i));
    };

    @Override
    default Initiative create(@NotNull Initiative initiative) {
       return save(initiative);
    }

    @Override
    default Optional<Initiative> findInitiativeById(String id){
       return findById(id);
    }
}
