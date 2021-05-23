package com.spochi.repository;

import com.spochi.entity.User;
import com.spochi.repository.UserRepository;
import org.springframework.context.annotation.Primary;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
@Primary
public interface MongoUserRepository extends MongoRepository<User, String>, UserRepository {
    @Override
    default User create(User user) {
        return save(user);
    }

    @Override
    default int getAmountOfInitiatives(String id) {
        return 0;
    }
}
