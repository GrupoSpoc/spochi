package com.spochi.repository;

import com.spochi.entity.Initiative;
import com.spochi.entity.InitiativeStatus;
import com.spochi.entity.User;
import com.spochi.repository.fiware.ngsi.NGSIJson;
import com.spochi.service.query.InitiativeQuery;
import com.spochi.service.query.InitiativeSorter;
import org.json.JSONArray;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.data.relational.core.sql.In;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@Primary
public class MongoUserRepository implements UserRepository {

    @Autowired
    MongoUserRepositoryInterface userRepository;

    @Autowired
    MongoInitiativeRepositoryInterface initiativeRepository;

    @Override
    public User create(User entity) {
        return userRepository.save(entity);
    }

    @Override
    public Optional<User> findByUid(String uid) {
        return userRepository.findByUid(uid);
    }

    @Override
    public Optional<User> findByNickname(String nickname) {
        return userRepository.findByNickname(nickname);
    }

    @Override
    public Map<Integer,Integer> getUserInitiativesByStatus(String id) {
        final InitiativeQuery initiativeQuery = new InitiativeQuery();
        initiativeQuery.withSorter(InitiativeSorter.DEFAULT_COMPARATOR.getId());

        List<Initiative> initiatives = initiativeRepository.getAllInitiatives(initiativeQuery)
                .stream()
                .filter(i -> i.getUserId() != null && i.getUserId().equalsIgnoreCase(id))
                .collect(Collectors.toList());

        return splitInitiativesByStatus(initiatives);
    }

    public void deleteAll() {
        userRepository.deleteAll();
    }

    public Optional<User> findById(String id) {
        return userRepository.findById(id);
    }

    public List<User> findAll() {
        return userRepository.findAll();
    }

    private Map<Integer, Integer> splitInitiativesByStatus(List<Initiative> array) {
        Map<Integer, Integer> resultMap = new HashMap<>();
        int pendingInitiatives = 0, approvedInitiatives = 0, rejectedInitiatives = 0;

        for (Initiative i : array) {

            if (i.getStatusId() == InitiativeStatus.PENDING.getId()) {
                pendingInitiatives++;
            } else if (i.getStatusId() == InitiativeStatus.APPROVED.getId()) {
                approvedInitiatives++;
            } else if(i.getStatusId() == InitiativeStatus.REJECTED.getId()) {
                rejectedInitiatives++;
            }
        }
        resultMap.put(InitiativeStatus.PENDING.getId(), pendingInitiatives);
        resultMap.put(InitiativeStatus.APPROVED.getId(), approvedInitiatives);
        resultMap.put(InitiativeStatus.REJECTED.getId(), rejectedInitiatives);
        return resultMap;
    }
}
