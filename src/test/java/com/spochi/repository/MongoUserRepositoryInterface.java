package com.spochi.repository;

import com.spochi.entity.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MongoUserRepositoryInterface extends MongoRepository<User, String> {
    Optional<User> findByUid(String uid);

    Optional<User> findByNickname(String nickname);
}
