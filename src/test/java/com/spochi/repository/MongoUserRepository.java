package com.spochi.repository;

import com.spochi.entity.User;
import com.spochi.service.query.InitiativeSorter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

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
    public int getAmountOfInitiatives(String id) {
        return (int) initiativeRepository.getAllInitiatives(InitiativeSorter.DEFAULT_COMPARATOR)
                .stream()
                .filter(i -> i.getUserId() != null && i.getUserId().equalsIgnoreCase(id))
                .count();
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
}
