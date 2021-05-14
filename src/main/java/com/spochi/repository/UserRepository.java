package com.spochi.repository;

import com.spochi.entity.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends MongoRepository<User, String> {
    // todo cambiar a findByUid cuando se cambie googleId por uid en User
    Optional<User> findByGoogleId(String uid);

    Optional<User> findByNickname(String nickname);
}
